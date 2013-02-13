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

package com.ephesoft.dcma.test;

import java.io.File;

import javax.annotation.PostConstruct;

import org.custommonkey.xmlunit.XMLTestCase;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;
import org.w3c.dom.Document;

import com.ephesoft.dcma.core.annotation.Module;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.service.DBScriptExecuter;
import com.ephesoft.dcma.util.XMLUtil;

/**
 * Base class for unit tests involving the database.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.service.DBScriptExecuter
 */
@SuppressWarnings("PMD")
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@TestExecutionListeners( {TransactionalTestExecutionListener.class})
@Transactional
public abstract class DcmaTestCase extends DcmaTestBase {

	/**
	 * META_INF String.
	 */
	private static final String META_INF = "META-INF";

	/**
	 * INIT_TESTDATA_SQL String.
	 */
	private static final String INIT_TESTDATA_SQL = "init-testdata.sql";

	/**
	 * run boolean.
	 */
	private static boolean run = false;

	/**
	 * xmlTest XMLTestCase.
	 */
	private XMLTestCase xmlTest;

	/**
	 * dbScriptExecuter {@link DBScriptExecuter}.
	 */
	@Autowired
	private DBScriptExecuter dbScriptExecuter;

	/**
	 * Initializing method.
	 */
	@PostConstruct
	public void initialize() {
		xmlTest = new XMLTestCase() {
		};
	}

	/** Prepare the environment settings for the test. */
	@BeforeClass
	public static void prepareTestEnvironment() {
		System.setProperty("org.apache.cocoon.mode", "test");
	}

	/** Load information into the database. */
	@BeforeTransaction
	public void prepareDatabase() {
		synchronized (this) {
			if (!run) {
				prepareTestData(getModuleName());
			}
			run = true;
		}
		((ConfigurableApplicationContext) this.applicationContext).registerShutdownHook();
	}

	/**
	 * To get Module Name.
	 * 
	 * @return String
	 */
	public String getModuleName() {
		Class<?> userClass = ClassUtils.getUserClass(this.getClass());
		Module module = getModuleAnnotation(userClass);
		return module.value();
	}

	private Module getModuleAnnotation(Class<?> clazz) {
		Module module = null;
		if (clazz.getAnnotation(Module.class) != null) {
			module = clazz.getAnnotation(Module.class);
		}
		if (clazz.getGenericSuperclass() != null) {
			module = getModuleAnnotation(clazz.getSuperclass());
		}
		return module;
	}

	/**
	 * To prepare Test Data.
	 * 
	 * @param moduleName String
	 */
	public void prepareTestData(String moduleName) {
		Resource resource = new ClassPathResource(File.separator + META_INF + File.separator + moduleName + File.separator
				+ INIT_TESTDATA_SQL, this.getClass());
		dbScriptExecuter.execute(resource);
	}

	/**
	 * To compare XMLs.
	 * 
	 * @param actualOutLoc String
	 * @param expOutLoc String
	 * @param batchInstanceId String
	 * @throws Exception in case of error
	 */
	public void compareXMLs(String actualOutLoc, String expOutLoc, String batchInstanceId) throws Exception {
		File actualOutXML = getXMLFile(new File(actualOutLoc + File.separator + batchInstanceId));
		File expectedOutXML = getXMLFile(new File(expOutLoc + File.separator + batchInstanceId));
		Document actualOutDoc = XMLUtil.createDocumentFrom(actualOutXML);
		Document expectedOutDoc = XMLUtil.createDocumentFrom(expectedOutXML);
		xmlTest.assertXMLEqual(actualOutDoc, expectedOutDoc);

	}

	/**
	 * To compare XMLs.
	 * 
	 * @param actualOutLoc String
	 * @param expOutLoc String
	 * @param batchInstanceId String
	 * @throws Exception in case of error
	 */
	public void compareXMLsNotEqual(String actualOutLoc, String expOutLoc, String batchInstanceId) throws Exception {
		File actualOutXML = getXMLFile(new File(actualOutLoc + File.separator + batchInstanceId));
		File expectedOutXML = getXMLFile(new File(expOutLoc + File.separator + batchInstanceId));
		Document actualOutDoc = XMLUtil.createDocumentFrom(actualOutXML);
		Document expectedOutDoc = XMLUtil.createDocumentFrom(expectedOutXML);
		xmlTest.assertXMLNotEqual(actualOutDoc, expectedOutDoc);

	}

	private File getXMLFile(File folderToBeExported) {
		File[] files = folderToBeExported.listFiles();
		File returnFile = null;
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().endsWith(FileType.XML.getExtensionWithDot())) {
				returnFile = files[i];
				break;
			}
		}
		return returnFile;
	}

}
