/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2010-2011 Ephesoft Inc. 
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

package com.ephesoft.dcma.test;

import java.util.Collection;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import com.ephesoft.dcma.util.ApplicationContextUtil;

/**
 * Base test case with standard injection mechanism. Subclasses have to set the {@link ContextConfiguration} annotation to correctly
 * load a context for dependency injection.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( {DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class})
public abstract class DcmaTestBase extends TestCase implements ApplicationContextAware {

	/** Logger available to subclasses. */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * The {@link ApplicationContext} that was injected into this test instance via {@link #setApplicationContext(ApplicationContext)}.
	 */
	protected ApplicationContext applicationContext;

	/**
	 * Set the {@link ApplicationContext} to be used by this test instance, provided via {@link ApplicationContextAware} semantics.
	 */
	public final void setApplicationContext(final ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * @param <T> the type of the bean in question
	 * @param c the class
	 * @return the bean of type {@code c} with its name in a single map entry
	 */
	public <T> Map.Entry<String, T> getSingleBeanWithName(Class<T> c) {
		Map<String, T> beans = applicationContext.getBeansOfType(c);
		assertEquals(1, beans.size());
		return beans.entrySet().iterator().next();
	}

	/**
	 * Compares collections
	 * 
	 * @param expected the expected collection
	 * @param actual the actual collection
	 */
	public static void compareCollections(Collection<?> expected, Collection<?> actual) {
		if (expected == null && actual == null) {
			return;
		}

		if (expected == null) {
			fail("Expected is null, but actuals is not.");
			throw new AssertionError("cannot be reached");
		}

		assertEquals("Size differs", expected.size(), actual.size());
		assertTrue("Contents differ", expected.containsAll(actual));
	}

	/**
	 * @param <T> the base type
	 * @param c the collection
	 * @return the only element from the collection
	 */
	protected <T> T getSingle(Collection<T> c) {
		assertNotNull(c);
		assertEquals(1, c.size());
		return c.iterator().next();
	}

	/**
	 * @param <T> the type of objects to return
	 * @param resourceName the resource name
	 * @return map of names and references to objects added
	 */
	protected <T> Map<String, T> initBeans(String resourceName) {
		return ApplicationContextUtil.initializeBeans((ConfigurableApplicationContext) applicationContext, loadResource(resourceName));
	}

	/**
	 * @param resourceName name relative to current class
	 * @return resource for {@code resourceName}
	 */
	protected Resource loadResource(String resourceName) {
		return new ClassPathResource(resourceName, getClass());
	}
}
