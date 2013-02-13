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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

/** 
 * This class contains methods to manipulate spring's application context. 
 * 
 * @author Ephesoft
 * @version 1.0 
 * @see org.springframework.context.ApplicationContext
 */
public class ApplicationContextUtil {

	/**
	 * To initialize the beans.
	 * @param <T> the type of beans to be created (for type safety in caller method).
	 * @param context the context
	 * @param res the resource containing new context information
	 * @return initialized beans from definitions in {@code res} initialized with {@code context}
	 */
	public static <T> Map<String, T> initializeBeans(ConfigurableApplicationContext context, Resource res) {
		BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) context.getBeanFactory();

		XmlBeanFactory factory = new XmlBeanFactory(res);
		String[] names = factory.getBeanDefinitionNames();
		for (String name : names) {
			beanDefinitionRegistry.registerBeanDefinition(name, factory.getBeanDefinition(name));
		}

		return getBeansFromContext(context, names);
	}

	/**
	 * To get beans from context.
	 * @param <T> the type (for type safety in caller method)
	 * @param beanFactory the bean factory
	 * @param names a list of names to be retrieved from {@code bf}
	 * @return names of and references to the beans identified by {@code names}.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Map<String, T> getBeansFromContext(BeanFactory beanFactory, String[] names) {
		Map<String, T> beans = new HashMap<String, T>(names.length);
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			beans.put(name, (T) beanFactory.getBean(name));
		}
		return beans;
	}

	/**
	 * To get beans from context.
	 * @param <T> the type (for type safety in caller method)
	 * @param beanFactory the bean factory
	 * @param names a list of names to be retrieved from {@code bf}
	 * @return names of and references to the beans identified by {@code names}.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBeanFromContext(BeanFactory beanFactory, String name, Class<T> type) {
		return (T) beanFactory.getBean(name);
	}

	/**
	 * To get beans of specified type.
	 * @param ctx the application context
	 * @param type the type to be returned
	 * @param <T> type parameter
	 * @return collection beans of type {@code type} from {@code ctx}
	 */
	public static <T> Collection<T> getBeansOfType(ApplicationContext ctx, Class<T> type) {
		Map<String, T> map = ctx.getBeansOfType(type);
		return map.values();
	}

	/**
	 * To get Single Bean of specified type.
	 * @param <T> the type of the bean
	 * @param ctx the context holding the bean
	 * @param type the typed class
	 * @return the bean of the same type as {@code type}
	 * @throws NoSuchBeanDefinitionException if a unique bean of {@code type} could not be found (0 or more than 1 results)
	 * @see #getBeansOfType(ApplicationContext, Class)
	 */
	public static <T> T getSingleBeanOfType(ApplicationContext ctx, Class<T> type) throws NoSuchBeanDefinitionException {
		Collection<T> beans = getBeansOfType(ctx, type);
		if (beans.size() != 1) {
			throw new NoSuchBeanDefinitionException("No single bean of type " + type.getName() + " found in application context "
					+ ctx.getDisplayName());
		}
		return beans.iterator().next();
	}
}
