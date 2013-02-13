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

package com.ephesoft.dcma.imp;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.imp.service.ImportService;
import com.ephesoft.dcma.util.FileUtils;

/**
 * 
 * This is Junit test for ImageMagick. It tests Two Positive Scenarios. 1. Importing of image folder 2. Import of multi page tif and
 * PDF files.
 * 
 * @author Ephesoft
 * @see com.ephesoft.dcma.imp.AbstractFolderImporterTest
 * 
 */
public class FolderImporterTest extends AbstractFolderImporterTest {

	/**
	 * String constants.
	 */
	private static final String PROP_FILE_EXPORT_TEST = "test.properties";
	/**
	 * String constants.
	 */
	private static final String ACTUAL_OUTPUT_FOLDER = "actual.output.location";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE1_BATCH_INSTANCE_ID = "testcase1.batchInstanceId";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE2_BATCH_INSTANCE_ID = "testcase2.batchInstanceId";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE1_BATCH_CLASS_ID = "testcase1.batchClassId";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE2_BATCH_CLASS_ID = "testcase2.batchClassId";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE1_UNC_FOLDER = "testcase1.unc.folder";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE2_UNC_FOLDER = "testcase2.unc.folder";

	/**
	 * Instance of ImportService.
	 */
	@Autowired
	private transient ImportService importService;
	/**
	 * Instance of BatchSchemaService.
	 */
	@Autowired
	private transient BatchSchemaService batchSchemaService;
	/**
	 * Instance of BatchInstanceService.
	 */
	@Autowired
	private transient BatchInstanceService batchInstanceService;
	/**
	 * Instance of BatchClassService.
	 */
	@Autowired
	private transient BatchClassService batchClassService;
	/**
	 * Batch instance for test scenario.
	 */
	private transient String batchInstanceId;
	/**
	 * Batch class for test scenario.
	 */
	private transient String batchClassId;

	/**
	 * The folder where input files are kept and output files will be written.
	 */
	private transient String actualOutputFolder;
	/**
	 * Variable stores initial local folder location.
	 */
	private transient String localFolderLocation;
	/**
	 * Variable stores initial test folder location.
	 */
	private transient String testFolderLocation;
	/**
	 * Variable for property file.
	 */
	private final Properties prop = new Properties();

	/**
	 * This method prepares and initializes all the resources that would be required by the plugin.
	 */
	@Before
	public void setUp() {
		boolean result = false;
		localFolderLocation = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceId);
		testFolderLocation = batchSchemaService.getTestFolderLocation();

		try {
			prop.load(AbstractFolderImporterTest.class.getClassLoader().getResourceAsStream(PROP_FILE_EXPORT_TEST));
		} catch (IOException e) {
			assertTrue(e.getMessage(), result);
		}
		actualOutputFolder = (testFolderLocation + File.separator + prop.getProperty(ACTUAL_OUTPUT_FOLDER));
		batchSchemaService.setLocalFolderLocation(actualOutputFolder);

		FileUtils.deleteDirectoryAndContents(actualOutputFolder + File.separator + "properties");

	}

	/**
	 * This is a test scenario to import image folder.
	 */
	@Test
	public void testImportFolder() {
		boolean result = false;
		batchInstanceId = prop.getProperty(TEST_CASE1_BATCH_INSTANCE_ID);
		batchClassId = prop.getProperty(TEST_CASE1_BATCH_CLASS_ID);
		boolean created = false;
		BatchClass initialBatchClass = new BatchClass();
		String initialUncFolder = null;
		try {
			BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceId);
			if (null == batchInstance) {
				batchInstance = new BatchInstance();
				batchInstance.setIdentifier(batchInstanceId);
				BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);
				batchInstance.setUncSubfolder(testFolderLocation + File.separator + prop.getProperty(TEST_CASE1_UNC_FOLDER));
				batchInstance.setBatchClass(batchClass);
				batchInstanceService.createBatchInstance(batchInstance);
				created = true;
			} else {
				initialBatchClass = batchInstance.getBatchClass();
				BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);
				initialUncFolder = batchInstance.getUncSubfolder();
				batchInstance.setUncSubfolder(testFolderLocation + File.separator + prop.getProperty(TEST_CASE1_UNC_FOLDER));
				batchInstance.setBatchClass(batchClass);
				batchInstanceService.updateBatchInstance(batchInstance);
			}
			importService.importBatchFolder(new BatchInstanceID(batchInstanceId), null);
			File file = new File(actualOutputFolder + File.separator + batchInstanceId);
			if (!file.exists()) {
				throw new DCMAException();
			}

		} catch (DCMAException e) {
			assertTrue(e.getMessage(), result);
		} catch (Exception e) {
			assertTrue(e.getMessage(), result);
		} finally {
			if (created) {
				BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceId);
				if (batchInstance != null) {
					batchInstanceService.removeBatchInstance(batchInstance);
				}
			} else {
				BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceId);
				batchInstance.setBatchClass(initialBatchClass);
				batchInstance.setUncSubfolder(initialUncFolder);
				batchInstanceService.updateBatchInstance(batchInstance);
			}
		}
	}

	/**
	 * This is a test scenario to import multi page images.
	 */
	@Test
	public void testImportMultiPageFiles() {
		boolean result = false;
		batchInstanceId = prop.getProperty(TEST_CASE2_BATCH_INSTANCE_ID);
		batchClassId = prop.getProperty(TEST_CASE2_BATCH_CLASS_ID);
		boolean created = false;
		BatchClass initialBatchClass = new BatchClass();
		String initialUncFolder = null;
		try {
			BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceId);
			if (null == batchInstance) {
				batchInstance = new BatchInstance();
				batchInstance.setIdentifier(batchInstanceId);
				BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);
				batchInstance.setUncSubfolder(testFolderLocation + File.separator + prop.getProperty(TEST_CASE2_UNC_FOLDER));
				batchInstance.setBatchClass(batchClass);
				batchInstanceService.createBatchInstance(batchInstance);
				created = true;
			} else {
				initialBatchClass = batchInstance.getBatchClass();
				BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);
				initialUncFolder = batchInstance.getUncSubfolder();
				batchInstance.setUncSubfolder(testFolderLocation + File.separator + prop.getProperty(TEST_CASE2_UNC_FOLDER));
				batchInstance.setBatchClass(batchClass);
				batchInstanceService.updateBatchInstance(batchInstance);
			}
			importService.importMultiPage(new BatchInstanceID(batchInstanceId), null);

		} catch (DCMAException e) {
			assertTrue(e.getMessage(), result);
		} catch (Exception e) {
			assertTrue(e.getMessage(), result);
		} finally {
			if (created) {
				BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceId);
				if (batchInstance != null) {
					batchInstanceService.removeBatchInstance(batchInstance);
				}
			} else {
				BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceId);
				batchInstance.setBatchClass(initialBatchClass);
				batchInstance.setUncSubfolder(initialUncFolder);
				batchInstanceService.updateBatchInstance(batchInstance);
			}
		}
	}

	/**
	 * This method does the clean-up operation : reverting back the changes done in plugin properties.
	 */
	@After
	public void tearDown() {
		batchSchemaService.setLocalFolderLocation(localFolderLocation);
		FileUtils.deleteDirectoryAndContents(actualOutputFolder + File.separator + batchInstanceId);

	}

}
