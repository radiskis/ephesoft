/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2010-2012 Ephesoft Inc. 
* 
* This program is free software; you can redistribute it and/or modify it under 
* the terms of the GNU Affero General Public License version 3 as published by the 
* Free Software Foundation with the addition of the following permission added 
* to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK 
* IN WHICH THE COPYRIGHT IS OWNED BY EPHESOFT, EPHESOFT DISCLAIMS THE WARRANTY 
* OF NON INFRINGEMENT OF THIRD PARTY RIGHTS. 
* 
* This program is distributed in the hope that it will be useful, but WITHOUT 
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
* FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more 
* details. 
* 
* You should have received a copy of the GNU Affero General Public License along with 
* this program; if not, see http://www.gnu.org/licenses or write to the Free 
* Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 
* 02110-1301 USA. 
* 
* You can contact Ephesoft, Inc. headquarters at 111 Academy Way, 
* Irvine, CA 92617, USA. or at email address info@ephesoft.com. 
* 
* The interactive user interfaces in modified source and object code versions 
* of this program must display Appropriate Legal Notices, as required under 
* Section 5 of the GNU Affero General Public License version 3. 
* 
* In accordance with Section 7(b) of the GNU Affero General Public License version 3, 
* these Appropriate Legal Notices must retain the display of the "Ephesoft" logo. 
* If the display of the logo is not reasonably feasible for 
* technical reasons, the Appropriate Legal Notices must display the words 
* "Powered by Ephesoft". 
********************************************************************************/ 

package com.ephesoft.dcma.core.convertor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is bean converter class.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see java.lang.reflect.ParameterizedType
 */
public class BeanConverter {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(BeanConverter.class);

	public static void convertToBean(List<Object> list, Object bean, Object parent) {
		try {
			Class<?> dtoClass = bean.getClass();
			Field dtoFieldlist[] = dtoClass.getDeclaredFields();
			for (int i = 0; i < dtoFieldlist.length; i++) {
				Mapping dtoMapping = (Mapping) dtoFieldlist[i].getAnnotation(Mapping.class);
				ListMapping dtoListMapping = (ListMapping) dtoFieldlist[i].getAnnotation(ListMapping.class);
				MapMapping dtoMapMapping = (MapMapping) dtoFieldlist[i].getAnnotation(MapMapping.class);
				ParentMapping dtoParentMapping = (ParentMapping) dtoFieldlist[i].getAnnotation(ParentMapping.class);
				SubMapping subMapping = (SubMapping) dtoFieldlist[i].getAnnotation(SubMapping.class);
				if (dtoMapping != null) {
					setFields(list, dtoMapping, dtoFieldlist[i], bean);
				}
				if (null != dtoListMapping) {
					setListFields(list, dtoListMapping, dtoFieldlist[i], bean);
				}
				if (null != dtoMapMapping) {
					setMapFields(list, dtoMapMapping, dtoFieldlist[i], bean);
				}
				if (null != dtoParentMapping) {
					setParent(dtoFieldlist[i], bean, parent);
				}
				if (null != subMapping) {
					setSubFields(list, subMapping, dtoFieldlist[i], bean);
				}
			}

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);

		}
	}

	private static void setSubFields(List<Object> list, SubMapping subMapping, Field field, Object bean) {
		Class<?> fieldMappingClass = subMapping.clazz();
		Object obj = getObject(list, fieldMappingClass);
		List<Field> pojoFieldList = getAllFields(fieldMappingClass);
		for (Field pojoField : pojoFieldList) {
			if (pojoField.getName().equals(subMapping.fieldName())) {
				try {
					Field fieldToSet = field;
					fieldToSet.setAccessible(true);
					Field fieldValue = pojoField;
					fieldValue.setAccessible(true);
					Object object = fieldValue.get(obj);
					List<Object> iteratableObject = new ArrayList<Object>();
					iteratableObject.add(object);
					Type type = fieldToSet.getGenericType();
					Object objct = ((Class<?>) type).newInstance();
					convertToBean(iteratableObject, objct, bean);
					fieldToSet.set(bean, objct);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
	}

	private static void setParent(Field field, Object bean, Object parent) {
		try {
			Field fieldToSet = field;
			fieldToSet.setAccessible(true);
			fieldToSet.set(bean, parent);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	private static void setMapFields(List<Object> list, MapMapping dtoMapMapping, Field field, Object bean) {
		Class<?> fieldMappingClass = dtoMapMapping.clazz();
		Object obj = getObject(list, fieldMappingClass);
		List<Field> pojoFieldList = getAllFields(fieldMappingClass);
		for (Field pojoField : pojoFieldList) {
			if (pojoField.getName().equals(dtoMapMapping.fieldName())) {
				try {
					Field fieldToSet = field;
					fieldToSet.setAccessible(true);
					Field fieldValue = pojoField;
					fieldValue.setAccessible(true);
					List<?> listOfObject = (List<?>) fieldValue.get(obj);
					Type type = fieldToSet.getGenericType();
					if (type instanceof ParameterizedType) {
						ParameterizedType paramType = (ParameterizedType) type;
						Type[] typeList = paramType.getActualTypeArguments();
						Type requiredType = typeList[1];
						Class<?> reqClass = (Class<?>) requiredType;
						Map<Object, Object> finalList = (Map<Object, Object>) dtoMapMapping.mapClass().newInstance();
						for (Object objk : listOfObject) {
							updateFinalList(dtoMapMapping, bean, reqClass, finalList, objk);
						}
						fieldToSet.set(bean, finalList);
					}
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
	}

	private static void updateFinalList(MapMapping dtoMapMapping, Object bean, Class<?> reqClass, Map<Object, Object> finalList,
			Object objk) throws InstantiationException, IllegalAccessException {
		List<Object> iteratableObject = new ArrayList<Object>();
		iteratableObject.add(objk);
		Object objct = reqClass.newInstance();
		convertToBean(iteratableObject, objct, bean);
		Object keyObject = getKey(objct, dtoMapMapping.keyName());
		if (keyObject != null) {
			finalList.put(keyObject, objct);
		}
	}

	private static Object getKey(Object objct, String keyName) {
		Object key = null;
		try {
			Field field = objct.getClass().getDeclaredField(keyName);
			field.setAccessible(true);
			key = field.get(objct);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return key;
	}

	@SuppressWarnings("unchecked")
	private static void setListFields(List<Object> list, ListMapping dtoListMapping, Field dtoField, Object bean) {
		Class<?> fieldMappingClass = dtoListMapping.clazz();
		Object obj = getObject(list, fieldMappingClass);
		List<Field> pojoFieldList = getAllFields(fieldMappingClass);
		for (Field pojoField : pojoFieldList) {
			if (pojoField.getName().equals(dtoListMapping.fieldName())) {
				try {
					Field fieldToSet = dtoField;
					fieldToSet.setAccessible(true);
					Field fieldValue = pojoField;
					fieldValue.setAccessible(true);
					List<?> listOfObject = (List<?>) fieldValue.get(obj);
					Type type = fieldToSet.getGenericType();
					if (type instanceof ParameterizedType) {
						ParameterizedType paramType = (ParameterizedType) type;
						Type[] typeList = paramType.getActualTypeArguments();
						Type requiredType = typeList[0];
						Class<?> reqClass = (Class<?>) requiredType;
						List<Object> finalList = (List<Object>) dtoListMapping.listClass().newInstance();
						for (Object objk : listOfObject) {
							List<Object> iteratableObject = new ArrayList<Object>();
							iteratableObject.add(objk);
							Object objct = reqClass.newInstance();
							convertToBean(iteratableObject, objct, bean);
							finalList.add(objct);
						}
						fieldToSet.set(bean, finalList);
					}
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
	}

	private static void setFields(List<Object> list, Mapping dtoMapping, Field dtoField, Object bean) {
		Class<?> fieldMappingClass = dtoMapping.clazz();
		Object obj = getObject(list, fieldMappingClass);
		List<Field> pojoFieldList = getAllFields(fieldMappingClass);
		for (Field pojoField : pojoFieldList) {
			if (pojoField.getName().equals(dtoMapping.fieldName())) {
				try {
					Field fieldToSet = dtoField;
					Field fieldValue = pojoField;
					fieldValue.setAccessible(true);
					fieldToSet.setAccessible(true);
					if (dtoMapping.converterClass().equals(Object.class)) {
						fieldToSet.set(bean, fieldValue.get(obj));
					} else {
						Class<?> converterClass = dtoMapping.converterClass();
						Object converterClassObject = converterClass.newInstance();
						Method method = getMethod(converterClass);
						fieldToSet.set(bean, method.invoke(converterClassObject, fieldValue.get(obj)));
					}
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
	}

	private static Method getMethod(Class<?> converterClass) {
		Method methodObject=null;
		for (Method method : converterClass.getMethods()) {
			if (method.getName().equals("convert")) {
				methodObject=method;
				break;
			}
		}
		return methodObject;
	}

	private static Object getObject(List<Object> list, Class<?> fieldMappingClass) {
		Object fieldMappingClassObject=null; 
		for (Object obj : list) {
			if (obj.getClass().equals(fieldMappingClass)) {
				fieldMappingClassObject=obj;
				break;
			}
		}
		return fieldMappingClassObject;
	}

	private static List<Field> getAllFields(Class<?> className) {
		List<Field> fields = new ArrayList<Field>();
		for (Field field : className.getDeclaredFields()) {
			fields.add(field);
		}

		if (className.getSuperclass() != null) {
			fields.addAll(getAllFields(className.getSuperclass()));
		}

		return fields;
	}

}
