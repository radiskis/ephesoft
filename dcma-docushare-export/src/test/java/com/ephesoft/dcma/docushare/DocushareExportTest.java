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

package com.ephesoft.dcma.docushare;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.docushare.service.DocushareExportService;
import com.ephesoft.dcma.util.FileUtils;

/**
 * This is Junit test for DocushareExportService. It contains two scenarios: one is a positive test case when batch xml of the instance
 * to be tested is present and second is s negative test case when batch xml of the instance to be tested is not present.
 * 
 * @author Ephesoft
 */
public class DocushareExportTest extends AbstractDocushareExportTest {

	/**
	 * String constants.
	 */
	private static final String PROP_FILE_DOCUSHARE_TEST = "test.properties";
	/**
	 * String constants.
	 */
	private static final String ACTUAL_OUTPUT_FOLDER = "actual.output.location";
	/**
	 * String constants.
	 */
	private static final String BASE_FOLDER = "base.location";
	/**
	 * String constants.
	 */
	private static final String DOCUSHARE_FOLDER = "docushare-export-folder";
	/**
	 * String constants.
	 */
	private static final String DOCUSHARE_ZIPPED_FILE_NAME = "_docushare.zip";
	/**
	 * String constants.
	 */
	private static final String DOCUSHARE_BATCH_FOLDER = "_docushare";
	/**
	 * String constants.
	 */
	private static final String DOCUSHARE_XML = "_docushare.xml";
	/**
	 * Instance of DocushareExportService.
	 */
	@Autowired
	private transient DocushareExportService docushareExportService;
	/**
	 * Instance of BatchSchemaService.
	 */
	@Autowired
	private transient BatchSchemaService batchSchemaService;
	/**
	 * Batch instance for positive test scenario.
	 */
	private transient String batchInstanceIdSuccess;
	/**
	 * Batch instance for negative test scenario.
	 */
	private transient String batchInstanceIdFailure;

	/**
	 * The folder where input files are kept and output files will be written.
	 */
	private transient String actualOutputFolder;

	/**
	 * The folder containing expected output batch xml.
	 */
	private transient String localBaseFolder;
	/**
	 * Variable stores initial local folder location.
	 */
	private transient String localFolderLocation;
	/**
	 * Variable stores initial base folder location.
	 */
	private transient String baseFolderLocation;
	/**
	 * Variable for property file.
	 */
	private final transient Properties prop = new Properties();


	
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
		String testFolderLocation;
		batchInstanceIdSuccess = "BI41";
		batchInstanceIdFailure = "BI40";
		localFolderLocation = batchInstanceService.getBatchClassIdentifier(batchInstanceIdSuccess);
		testFolderLocation = batchSchemaService.getTestFolderLocation();
		baseFolderLocation = batchSchemaService.getBaseFolderLocation();
		try {
			prop.load(DocushareExportTest.class.getClassLoader().getResourceAsStream(PROP_FILE_DOCUSHARE_TEST));
		} catch (IOException e) {
			assertTrue(e.getMessage(), result);
		}
		actualOutputFolder = (testFolderLocation + File.separator + prop.getProperty(ACTUAL_OUTPUT_FOLDER));
		localBaseFolder = (testFolderLocation + File.separator + prop.getProperty(BASE_FOLDER));
		batchSchemaService.setLocalFolderLocation(actualOutputFolder);
		batchSchemaService.setBaseFolderLocation(localBaseFolder);
	}

	/**
	 * This is a test scenario when Batch xml is present.
	 */
	@Test
	public void testDocushareExportSuccess() {
		boolean result = false;
		try {
			docushareExportService.exportDocushareFiles(new BatchInstanceID(batchInstanceIdSuccess), null);
			if (!docushareExportTest(batchInstanceIdSuccess)) {
				throw new DCMAException();
			}

		} catch (DCMAException e) {
			assertTrue(e.getMessage(), result);
		} catch (Exception e) {
			assertTrue(e.getMessage(), result);
		}
	}

	/**
	 * This is a test scenario when Batch xml is not present.
	 */
	@Test
	public void testDocushareExportFailure() {
		boolean result = true;
		try {
			docushareExportService.exportDocushareFiles(new BatchInstanceID(batchInstanceIdFailure), null);
			if (!docushareExportTest(batchInstanceIdFailure)) {
				throw new DCMAException();
			}
		} catch (DCMAException e) {
			assertTrue(e.getMessage(), result);
		} catch (Exception e) {
			assertTrue(e.getMessage(), result);
		}
	}

	/**
	 * This methos does the clean-up operation : deleting the newly modified xml in inputOutput folder and replacing them with
	 * corresponding xml in samples folder.
	 */
	@After
	public void tearDown() {

		batchSchemaService.setLocalFolderLocation(localFolderLocation);
		batchSchemaService.setBaseFolderLocation(baseFolderLocation);
		FileUtils.deleteDirectoryAndContents(localBaseFolder + File.separator + batchInstanceIdSuccess + DOCUSHARE_BATCH_FOLDER);
		FileUtils.deleteDirectoryAndContents(localBaseFolder + File.separator + DOCUSHARE_FOLDER);
		File file = new File(actualOutputFolder + File.separator + batchInstanceIdSuccess + File.separator + batchInstanceIdSuccess
				+ DOCUSHARE_XML);
		file.delete();
		FileUtils.deleteDirectoryAndContents(localBaseFolder + File.separator + batchInstanceIdFailure + DOCUSHARE_BATCH_FOLDER);
		File file2 = new File(actualOutputFolder + File.separator + batchInstanceIdFailure + File.separator + batchInstanceIdFailure
				+ DOCUSHARE_XML);
		file2.delete();

	}

	/**
	 * This method checks whether docushare plugin has successfully created the zipped folder.
	 * 
	 * @param batchInstanceId {@link String}
	 * @return boolean
	 * @throws DCMAException {@link DCMAException}
	 * @throws IOException {@link IOException}
	 */
	@SuppressWarnings("unchecked")
	public boolean docushareExportTest(final String batchInstanceId) throws DCMAException, IOException {
		boolean result = false;
		File file = new File(localBaseFolder + File.separator + DOCUSHARE_FOLDER + File.separator + batchInstanceId
				+ DOCUSHARE_ZIPPED_FILE_NAME);
		FileUtils.unzip(file, localBaseFolder + File.separator + batchInstanceId + DOCUSHARE_BATCH_FOLDER);
		List<String> files = FileUtils.listDirectory(new File(localBaseFolder + File.separator + batchInstanceId
				+ DOCUSHARE_BATCH_FOLDER));
		for (Iterator iterator = files.iterator(); iterator.hasNext();) {
			String fileName = (String) iterator.next();
			if ((fileName.contains(DOCUSHARE_XML)) || (fileName.contains(".tif")) || (fileName.contains(".pdf"))) {
				if (fileName.contains(DOCUSHARE_XML)) {
					result = true;
				}
				continue;
			}

		}
		return result;

	}
}
