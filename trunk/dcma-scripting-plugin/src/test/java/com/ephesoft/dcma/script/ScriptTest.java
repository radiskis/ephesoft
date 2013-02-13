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

package com.ephesoft.dcma.script;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.script.service.ScriptService;
import com.ephesoft.dcma.util.FileUtils;

/**
 * 
 * This is Junit test for ScriptService. It contains positive test case for executing script.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.script.AbstractScriptTest
 */
public class ScriptTest extends AbstractScriptTest {

	/**
	 * String constants.
	 */
	private static final String PROP_FILE_TEST = "test.properties";
	/**
	 * String constants.
	 */
	private static final String ACTUAL_OUTPUT_FOLDER = "actual.output.location";
	/**
	 * String constants.
	 */
	private static final String EXPECTED_OUTPUT_FOLDER = "expected.output.location";
	/**
	 * String constants.
	 */
	private static final String EXT_BATCH_XML_FILE = "_batch.xml";

	/**
	 * Instance of BatchSchemaService.
	 */
	@Autowired
	private transient BatchSchemaService batchSchemaService;

	/**
	 * Instance of ScriptService.
	 */
	@Autowired
	private transient ScriptService scriptService;

	/**
	 * Batch instance for test scenario.
	 */
	private transient String batchInstanceId1;

	/**
	 * The folder containing expected output batch xml.
	 */
	private transient String expectedOutputFolder;

	/**
	 * Variable stores initial local folder location.
	 */
	private String localFolderLocation;

	/**
	 * Instance of {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * Method to initialize resources.
	 */
	@Before
	public void setUp() {
		boolean result = false;
		String actualOutputFolder;
		batchInstanceId1 = "BI7C";
		String testFolderLocation;
		localFolderLocation = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceId1);
		testFolderLocation = batchSchemaService.getTestFolderLocation();
		Properties prop = new Properties();
		try {
			prop.load(ScriptTest.class.getClassLoader().getResourceAsStream(PROP_FILE_TEST));
		} catch (IOException e) {
			assertTrue(e.getMessage(), result);
		}
		actualOutputFolder = (testFolderLocation + File.separator + prop.getProperty(ACTUAL_OUTPUT_FOLDER));
		expectedOutputFolder = (testFolderLocation + File.separator + prop.getProperty(EXPECTED_OUTPUT_FOLDER));
		batchSchemaService.setLocalFolderLocation(actualOutputFolder);

		FileUtils.deleteDirectoryAndContents(actualOutputFolder + File.separator + "properties");
	}

	/**
	 * Tests Script service.
	 */
	@Test
	public void testScriptingPlugin() {
		boolean result = false;
		try {
			scriptService.executeScript(new BatchInstanceID(batchInstanceId1), null, "TestScript");
			File file = new File(expectedOutputFolder + File.separator + batchInstanceId1 + File.separator + batchInstanceId1
					+ EXT_BATCH_XML_FILE);
			if (!file.exists()) {
				throw new DCMAException();
			}
		} catch (DCMAException e) {
			assertTrue(e.getMessage(), result);
		} catch (Exception e) {
			assertTrue(e.getMessage(), result);
		}
	}

	/**
	 * Does clean up operation.
	 */
	@After
	public void tearDown() {
		batchSchemaService.setLocalFolderLocation(localFolderLocation);
	}

}
