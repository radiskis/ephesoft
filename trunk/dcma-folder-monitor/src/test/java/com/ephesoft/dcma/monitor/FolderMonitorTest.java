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

package com.ephesoft.dcma.monitor;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.id.BatchClassID;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.monitor.service.FolderMonitorService;
import com.ephesoft.dcma.monitor.service.FolderMonitorServiceImpl;
import com.ephesoft.dcma.util.FileUtils;

/**
 * This is Junit test for FolderMonitorService. It contains two scenarios: first scenario checks FolderMoniterService for BC1, second
 * scenario checks FolderMoniterService for BC2.
 * 
 * @author Ephesoft
 * @see com.ephesoft.dcma.monitor.AbstractFolderMonitorTest
 * 
 */
public class FolderMonitorTest extends AbstractFolderMonitorTest {
	/**
	 * A int type constant for synchronizing(sleep time).
	 */
	private static final int SLEEP_TIME_5000 = 5000;
	/**
	 * String constants.
	 */
	private static final String PROPERTIES_FILE_FOLDER_MONITER_TEST = "test.properties";
	/**
	 * String constants.
	 */
	private static final String UNC_FOLDER = "unc.folder.location";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE1_BATCHCLASSID = "testcase1.batchClassId";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE2_BATCHCLASSID = "testcase2.batchClassId";
	/**
	 * String constants.
	 */
	private static final String ACTUAL_OUTPUT_FOLDER = "actual.output.location";

	/**
	 * Instance of folderMonitor service.
	 */
	@Autowired
	private transient FolderMonitorService folderMonitorService;
	/**
	 * Instance of BatchClassService.
	 */
	@Autowired
	private transient BatchClassService batchClassService;
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
	 * Batch Class Id for test scenario.
	 */
	private transient String batchClassId;
	/**
	 * Folder from where batches are picked.
	 */
	private transient String uncFolder;

	/**
	 * Actual output folder for sending output.
	 */
	private transient String actualOutputFolder;

	/**
	 * Local folder location.
	 */
	private transient String localFolderLocation;

	/**
	 * Initial unc folder from where batches are picked.
	 */
	private transient String initialUncFolder;

	/**
	 * A new object of {@link Properties}.
	 */
	private final transient Properties prop = new Properties();

	/**
	 * Initial batches.
	 */
	private transient int initialBatches;

	/**
	 * This method initialize all the resources that would be required by folder monitor service.
	 */
	@Before
	public void setUp() {
		String testFolderLocation;
		try {
			prop.load(FolderMonitorTest.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_FOLDER_MONITER_TEST));
		} catch (IOException e) {
			assertTrue(false);
		}
		testFolderLocation = batchSchemaService.getTestFolderLocation();
		localFolderLocation = batchClassService.getSystemFolderForBatchClassIdentifier(batchClassId);
		uncFolder = (testFolderLocation + File.separator + prop.getProperty(UNC_FOLDER));
		actualOutputFolder = (testFolderLocation + File.separator + prop.getProperty(ACTUAL_OUTPUT_FOLDER));

	}

	/**
	 * This method tests the functionality of folder monitor service for BC1.
	 */
	@Test
	public void testFolderMonitorForBC1() {
		try {
			int newBatches;
			batchClassId = prop.getProperty(TEST_CASE1_BATCHCLASSID);
			batchSchemaService.setLocalFolderLocation(actualOutputFolder + File.separator + batchClassId);
			BatchClass batchClass1 = batchClassService.getBatchClassByIdentifier("BC1");
			folderMonitorService.removeWatchFromBatchClass(new BatchClassID(batchClassId));
			List<BatchInstance> bacthInstances = batchInstanceService.getBatchInstByBatchClass(batchClass1);
			initialBatches = bacthInstances.size();
			BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);
			initialUncFolder = batchClass.getUncFolder();
			batchClass.setUncFolder(uncFolder + File.separator + batchClassId);
			batchClassService.saveOrUpdate(batchClass);

			folderMonitorService.monitorBatchClass(new BatchClassID(batchClassId));
			File file = new File(uncFolder + File.separator + batchClassId + File.separator + System.currentTimeMillis() + "batch");
			file.mkdir();

			FolderMonitorServiceImpl folderMonitor = (FolderMonitorServiceImpl) folderMonitorService;
			folderMonitor.start();

			try {
				Thread.sleep(SLEEP_TIME_5000);
			} catch (InterruptedException e) {

			}

			batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);
			List<BatchInstance> newBacthInstances = batchInstanceService.getBatchInstByBatchClass(batchClass);
			newBatches = newBacthInstances.size();
			if (newBatches == initialBatches) {
				throw new DCMAException();

			}

		} catch (DCMAException e) {
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	/**
	 * This method tests the functionality of folder monitor service for BC2.
	 */
	@Test
	public void testFolderMonitorForBC2() {
		try {
			int newBatches;
			batchClassId = prop.getProperty(TEST_CASE2_BATCHCLASSID);
			batchSchemaService.setLocalFolderLocation(actualOutputFolder + File.separator + batchClassId);
			BatchClass batchClass1 = batchClassService.getBatchClassByIdentifier(batchClassId);
			folderMonitorService.removeWatchFromBatchClass(new BatchClassID(batchClassId));
			List<BatchInstance> bacthInstances = batchInstanceService.getBatchInstByBatchClass(batchClass1);
			initialBatches = bacthInstances.size();
			BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);
			initialUncFolder = batchClass.getUncFolder();
			batchClass.setUncFolder(uncFolder + File.separator + batchClassId);
			batchClassService.saveOrUpdate(batchClass);

			folderMonitorService.monitorBatchClass(new BatchClassID(batchClassId));
			File file = new File(uncFolder + File.separator + batchClassId + File.separator + System.currentTimeMillis() + "batch");
			file.mkdir();

			FolderMonitorServiceImpl folderMonitor = (FolderMonitorServiceImpl) folderMonitorService;
			folderMonitor.start();

			try {
				Thread.sleep(SLEEP_TIME_5000);
			} catch (InterruptedException e) {

			}

			batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);
			List<BatchInstance> newBacthInstances = batchInstanceService.getBatchInstByBatchClass(batchClass);
			newBatches = newBacthInstances.size();
			if (newBatches == initialBatches) {
				throw new DCMAException();

			}

		} catch (DCMAException e) {
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	/**
	 * This method does the clean-up operation.
	 * folder.
	 */
	@After
	public void tearDown() {
		batchSchemaService.setLocalFolderLocation(localFolderLocation);
		BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);
		batchClass.setUncFolder(initialUncFolder);
		batchClassService.saveOrUpdate(batchClass);
		FileUtils.deleteContentsOnly(uncFolder + File.separator + batchClassId);
	}

}
