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

package com.ephesoft.dcma.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/** 
 * This class provides access to class-related utility operations.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see java.lang.reflect.ParameterizedType
 */
public final class ClassUtil {

	/**
	 * Private constructor for utility class.
	 */
	private ClassUtil() {
	}

	/**
	 * to get Entity Class.
	 * @param <T> the type
	 * @param target the target
	 * @return the parameterized type of {@code target}'s class
	 */
	public static <T> Class<T> getEntityClass(Object target) {
		Class<?> targetClass = target.getClass();
		return getParameterizedClass(targetClass);
	}

	/**
	 * To get Parameterized Class.
	 * @param <T> the type of the class
	 * @param targetClass the requested target class
	 * @return the generic type of {@code targetClass}
	 * @throws IllegalArgumentException if the underlying parameterized class could not be inferred
	 *             via reflection.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getParameterizedClass(Class<?> targetClass)
			throws IllegalArgumentException {
		Type type = targetClass.getGenericSuperclass();
		Class<T> returnClass = null;
		Class<?> superClass = targetClass.getSuperclass();
		if (type instanceof ParameterizedType) {
			ParameterizedType paramType = (ParameterizedType) type;
			returnClass = (Class<T>) paramType.getActualTypeArguments()[0];
		} else if (!superClass.equals(targetClass)) {
			// check that we do not run into recursion
			returnClass = getParameterizedClass(superClass);
		} else {
			throw new IllegalArgumentException(
					"Could not guess entity class by reflection");
		}
		return returnClass;
	}

	/**
	 * Gets a field value from an <code>Object</code> not caring about its
	 * scope. Note that only fields from the <code>Class</code> directly are
	 * accessible with this method, not from any super <code>Class</code>.
	 * 
	 * @param object Object
	 * @param field {2link String}
	 * @return the field value
	 * @throws NoSuchFieldException {@link NoSuchFieldException}
	 * @throws IllegalAccessException {@link IllegalAccessException}
	 */
	public static Object getField(Object object, String field)
			throws NoSuchFieldException, IllegalAccessException {
		Field decField = object.getClass().getDeclaredField(field);
		decField.setAccessible(true);
		return decField.get(object);
	}

}
