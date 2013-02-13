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

package com.ephesoft.dcma.cleanup;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.cleanup.service.CleanupService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.util.FileUtils;

/**
 * This is Junit test for CleanupService. It contains two scenarios: one is a positive test case when batch folder of the instance to
 * be tested is present and second is s negative test case when batch folder of the instance to be tested is not present.
 * 
 * @author Ephesoft
 * @see com.ephesoft.dcma.cleanup.AbstractCleanupTest
 * 
 */

public class CleanupTest extends AbstractCleanupTest {

	/**
	 * String constants.
	 */
	private static final String PROP_FILE_CLEANUP_TEST = "test.properties";
	/**
	 * String constants.
	 */
	private static final String ACTUAL_OUTPUT_FOLDER = "actual.output.location";
	/**
	 * String constants.
	 */
	private static final String LOCAL_UNC_FOLDER = "unc.location";
	/**
	 * String constants.
	 */
	private static final String SAMPLES_FOLDER = "samples.location";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE2_BATCH_INSTANCE_ID = "testCase2.batchInstanceID";
	
	/**
	 * String constants.
	 */
	private static final String TEST_CASE2_BATCH_CLASS_ID = "testCase2.batchClassID";

	/**
	 * Instance of Cleanup service.
	 */
	@Autowired
	private transient CleanupService cleanupService;
	/**
	 * Instance of BatchSchema service.
	 */
	@Autowired
	private transient BatchSchemaService batchSchemaService;
	/**
	 * Instance of BatchInstance service.
	 */
	@Autowired
	private transient BatchInstanceService batchInstanceService;
	/**
	 * Instance of BatchClass service.
	 */
	@Autowired
	private transient BatchClassService batchClassService;

	/**
	 * Batch instance for test scenario.
	 */
	private transient String batchInstanceId;
	
	/**
	 * The folder where input files are kept and output files will be written.
	 */
	private transient String actualOutputFolder;

	/**
	 * The folder containing expected output batch xml.
	 */
	private transient String localUncFolder;

	/**
	 * The folder containing sample xmls.
	 */
	private transient String samplesFolder;
	/**
	 * Variable stores initial local folder location.
	 */
	private transient String localFolderLocation;
	/**
	 * Variable stores test folder location.
	 */
	private transient String testFolderLocation;
	/**
	 * Variable stores initial batch local folder location.
	 */
	private transient String sBatchLocalFolder;
	/**
	 * Variable stores initial batch unc folder location.
	 */
	private transient String sBatchUncFolder;
	/**
	 * Variable for property file.
	 */
	private final transient Properties prop = new Properties();

	/**
	 * Method to initialize resources.
	 */
	@Before
	public void setUp() {
		boolean result = false;
		Properties prop = new Properties();
		localFolderLocation = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceId);
		testFolderLocation = batchSchemaService.getTestFolderLocation();
		try {
			prop.load(CleanupTest.class.getClassLoader().getResourceAsStream(PROP_FILE_CLEANUP_TEST));
		} catch (IOException e) {
			assertTrue(e.getMessage(), result);
		}
		actualOutputFolder = (testFolderLocation + File.separator + prop.getProperty(ACTUAL_OUTPUT_FOLDER));
		localUncFolder = (testFolderLocation + File.separator + prop.getProperty(LOCAL_UNC_FOLDER));
		samplesFolder = (testFolderLocation + File.separator + prop.getProperty(SAMPLES_FOLDER));
		batchSchemaService.setLocalFolderLocation(actualOutputFolder);

	}

	/**
	 * This is a test scenario when Batch folder is present.
	 */
//	@Test
//	public void testCleanupTrue() {
//		boolean result = false;
//		batchInstanceId = prop.getProperty(TEST_CASE1_BATCH_INSTANCE_ID);
//		batchClassId = prop.getProperty(TEST_CASE1_BATCH_CLASS_ID);
//		boolean created = false;
//		BatchClass initialBatchClass = new BatchClass();
//		try {
//			BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceId);
//			if (null == batchInstance) {
//				batchInstance = new BatchInstance();
//				batchInstance.setIdentifier(batchInstanceId);
//				BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);
//				batchInstance.setBatchClass(batchClass);
//				batchInstanceService.createBatchInstance(batchInstance);
//				created = true;
//			} else {
//				initialBatchClass = batchInstance.getBatchClass();
//				BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);
//				batchInstance.setBatchClass(batchClass);
//
//			}
//			batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceId);
//			if (batchInstance != null) {
//				sBatchLocalFolder = batchInstance.getLocalFolder();
//				sBatchUncFolder = batchInstance.getUncSubfolder();
//			} else {
//				batchInstance = new BatchInstance();
//				batchInstance.setIdentifier(batchInstanceId);
//				BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);
//				batchInstance.setBatchClass(batchClass);
//				batchInstance.setLocalFolder(actualOutputFolder);
//				batchInstance.setUncSubfolder(localUncFolder + File.separator + batchInstanceId);
//				batchInstanceService.createBatchInstance(batchInstance);
//			}
//			cleanupService.cleanup(new BatchInstanceID(batchInstanceId), "CLEANUP");
//			cleanupTest(batchInstanceId, actualOutputFolder);
//		} catch (DCMAException e) {
//			assertTrue(e.getMessage(), result);
//		} catch (Exception e) {
//			assertTrue(e.getMessage(), result);
//		} finally {
//			if (created) {
//				BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceId);
//				if (batchInstance != null) {
//					batchInstanceService.removeBatchInstance(batchInstance);
//				}
//			} else {
//				BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceId);
//				batchInstance.setBatchClass(initialBatchClass);
//				batchInstanceService.updateBatchInstance(batchInstance);
//			}
//		}
//	}

	/**
	 * This is a test scenario when test folder is not present.
	 */
	@Test
	public void testCleanupFalse() {
		boolean result = true;
		batchInstanceId = prop.getProperty(TEST_CASE2_BATCH_INSTANCE_ID);
		String batchClassId = prop.getProperty(TEST_CASE2_BATCH_CLASS_ID);
		boolean created = false;
		BatchClass initialBatchClass = new BatchClass();
		try {
			BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceId);
			if (null == batchInstance) {
				batchInstance = new BatchInstance();
				batchInstance.setIdentifier(batchInstanceId);
				BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);
				batchInstance.setBatchClass(batchClass);
				batchInstanceService.createBatchInstance(batchInstance);
				created = true;
			} else {
				initialBatchClass = batchInstance.getBatchClass();
				BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);
				batchInstance.setBatchClass(batchClass);

			}
			batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceId);
			if (batchInstance != null) {
				sBatchLocalFolder = batchInstance.getLocalFolder();
				sBatchUncFolder = batchInstance.getUncSubfolder();
			} else {
				batchInstance = new BatchInstance();
				batchInstance.setIdentifier(batchInstanceId);
				BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);
				batchInstance.setBatchClass(batchClass);
				batchInstance.setLocalFolder(actualOutputFolder);
				batchInstance.setUncSubfolder(localUncFolder + File.separator + batchInstanceId);
				batchInstanceService.createBatchInstance(batchInstance);
			}
			cleanupService.cleanup(new BatchInstanceID(batchInstanceId), "CLEANUP");
			cleanupTest(batchInstanceId, actualOutputFolder);
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
				batchInstanceService.updateBatchInstance(batchInstance);
			}
		}

	}

	/**
	 * This methos does the clean-up operation : deleting the newly modified xml in inputOutput folder and replacing them with
	 * corresponding xml in samples folder.
	 */
	@After
	public void tearDown() {
		boolean result = false;
		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceId);
		if (batchInstance != null) {
			batchInstance.setLocalFolder(sBatchLocalFolder);
			batchInstance.setUncSubfolder(sBatchUncFolder);
			batchInstanceService.updateBatchInstance(batchInstance);
		}
		batchSchemaService.setLocalFolderLocation(localFolderLocation);
		FileUtils.deleteDirectoryAndContents(actualOutputFolder);
		FileUtils.deleteDirectoryAndContents(localUncFolder);

		try {
			FileUtils.copyDirectoryWithContents(samplesFolder, testFolderLocation + File.separator + "dcma-cleanup");

		} catch (IOException e) {
			assertTrue(e.getMessage(), result);
		}
	}

	/**
	 * This method tests whether clean up operation is performed successfully or not, if clean up is not performed successfully
	 * exception is thrown.
	 * 
	 * @param batchInstanceId  {@link String}
	 * @param folderPath {@link String}
	 * @throws DCMAException {@link DCMAException} exception to be thrown 
	 */
	public void cleanupTest(final String batchInstanceId, final String folderPath) throws DCMAException {
		final File file = new File(folderPath + File.separator + batchInstanceId);
		if (file.exists()) {
			throw new DCMAException();
		}

	}

}
