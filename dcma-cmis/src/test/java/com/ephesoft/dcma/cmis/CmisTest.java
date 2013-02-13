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

package com.ephesoft.dcma.cmis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.cmis.service.CMISExportService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchClassPluginConfigService;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.util.FileUtils;

/**
 * This is Junit test for CMISExportService. It contains three scenarios: two are positive, first case uploads a pdf file in repository
 * and second one uploads tif file for a batch, third scenario is negative where repository Id is entered incorrectly.
 * 
 * @author Ephesoft
 * @see com.ephesoft.dcma.cmis.AbstractCmisTest
 * 
 * 
 */
public class CmisTest extends AbstractCmisTest {

	/**
	 * String constants.
	 */
	private static final String PROP_FILE_CMIS_TEST = "test.properties";
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
	private static final String TEST_CASE1_SERVER_URL = "testcase1.server.url";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE1_ROOT_FOLDER = "testcase1.root.folder";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE1_SERVER_USERNAME = "testcase1.server.user.name";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE1_SERVER_PWD = "testcase1.server.password";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE1_REPOSITORY_ID = "testcase1.repository.id";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE1_UPLOAD_FILE_EXT = "testcase1.uploaded.file.ext";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE1_SWITCH = "testcase1.switch";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE2_BATCH_INSTANCE_ID = "testcase2.batchInstanceId";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE2_SERVER_URL = "testcase2.server.url";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE2_ROOT_FOLDER = "testcase2.root.folder";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE2_SERVER_USERNAME = "testcase2.server.user.name";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE2_SERVER_PWD = "testcase2.server.password";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE2_REPOSITORY_ID = "testcase2.repository.id";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE2_UPLOAD_FILE_EXT = "testcase2.uploaded.file.ext";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE2_SWITCH = "testcase2.switch";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE3_BATCH_INSTANCE_ID = "testcase3.batchInstanceId";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE3_SERVER_URL = "testcase3.server.url";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE3_ROOT_FOLDER = "testcase3.root.folder";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE3_SERVER_USERNAME = "testcase3.server.user.name";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE3_SERVER_PWD = "testcase3.server.password";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE3_REPOSITORY_ID = "testcase3.repository.id";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE3_UPLOAD_FILE_EXT = "testcase3.uploaded.file.ext";
	/**
	 * String constants.
	 */
	private static final String TEST_CASE3_SWITCH = "testcase3.switch";
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
	private static final String TEST_CASE3_BATCH_CLASS_ID = "testcase3.batchClassId";
	/**
	 * String constants.
	 */
	private static final String CMIS_PLUGIN = "CMIS_EXPORT";

	/**
	 * Instance of CMIS service.
	 */
	@Autowired
	private transient CMISExportService cmisExportService;
	/**
	 * Instance of BatchSchema service.
	 */
	@Autowired
	private transient BatchSchemaService batchSchemaService;
	/**
	 * Instance of BatchClassPluginConfig service.
	 */
	@Autowired
	private transient BatchClassPluginConfigService batchClassPluginConfigService;
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
	 * Batch class for test scenario.
	 */
	private transient String batchClassId;
	/**
	 * Variable stores initial local folder location.
	 */
	private transient String localFolderLocation;
	/**
	 * Variable for property file.
	 */
	private final Properties prop = new Properties();
	/**
	 * Variable for initial property values.
	 */
	private final List<String> initialPropertyValues = new ArrayList<String>();

	/**
	 * This method prepares and initializes all the resources that would be required by the plugin.
	 */
	@Before
	public void setUp() {
		boolean result = false;
		String testFolderLocation;
		String actualOutputFolder;
		localFolderLocation = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceId);
		testFolderLocation = batchSchemaService.getTestFolderLocation();

		try {
			prop.load(CmisTest.class.getClassLoader().getResourceAsStream(PROP_FILE_CMIS_TEST));
		} catch (IOException e) {
			assertTrue(e.getMessage(), result);
		}
		actualOutputFolder = (testFolderLocation + File.separator + prop.getProperty(ACTUAL_OUTPUT_FOLDER));
		batchSchemaService.setLocalFolderLocation(actualOutputFolder);

		FileUtils.deleteDirectoryAndContents(actualOutputFolder + File.separator + "properties");

	}

	/**
	 * This is a test scenario to upload pdf file in repository, with repository id as empty string.
	 */
	@Test
	public void testCMISExportPdf() {
		boolean result = false;
		batchInstanceId = prop.getProperty(TEST_CASE1_BATCH_INSTANCE_ID);
		batchClassId = prop.getProperty(TEST_CASE1_BATCH_CLASS_ID);
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
			List<BatchClassPluginConfig> batchClassPluginConfigs = batchClassPluginConfigService.getAllPluginConfiguration(
					batchInstanceId, CMIS_PLUGIN);
			if (initialPropertyValues.size() != 0) {
				initialPropertyValues.clear();
			}
			for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
				initialPropertyValues.add(batchClassPluginConfig.getValue());
			}
			for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
				if (batchClassPluginConfig.getName().equals("cmis.root_folder")) {
					batchClassPluginConfig.setValue(prop.getProperty(TEST_CASE1_ROOT_FOLDER));
				}
				if (batchClassPluginConfig.getName().equals("cmis.upload_file_type_ext")) {
					batchClassPluginConfig.setValue(prop.getProperty(TEST_CASE1_UPLOAD_FILE_EXT));
				}
				if (batchClassPluginConfig.getName().equals("cmis.server_URL")) {
					batchClassPluginConfig.setValue(prop.getProperty(TEST_CASE1_SERVER_URL));
				}
				if (batchClassPluginConfig.getName().equals("cmis.server_username")) {
					batchClassPluginConfig.setValue(prop.getProperty(TEST_CASE1_SERVER_USERNAME));
				}
				if (batchClassPluginConfig.getName().equals("cmis.server_password")) {
					batchClassPluginConfig.setValue(prop.getProperty(TEST_CASE1_SERVER_PWD));
				}
				if (batchClassPluginConfig.getName().equals("cmis.repository_id")) {
					batchClassPluginConfig.setValue(prop.getProperty(TEST_CASE1_REPOSITORY_ID));
				}
				if (batchClassPluginConfig.getName().equals("cmis.switch")) {
					batchClassPluginConfig.setValue(prop.getProperty(TEST_CASE1_SWITCH));
				}
			}
			batchClassPluginConfigService.updatePluginConfiguration(batchClassPluginConfigs);
			cmisExportService.exportContent(new BatchInstanceID(batchInstanceId), CMIS_PLUGIN);
			cmisExportService.deleteDocument(new BatchInstanceID(batchInstanceId), null);

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
	 * This is a test scenario to upload tif file in repository, with correct value of repository id.
	 */
	@Test
	public void testCMISExportTiff() {
		boolean result = false;
		batchInstanceId = prop.getProperty(TEST_CASE2_BATCH_INSTANCE_ID);
		batchClassId = prop.getProperty(TEST_CASE2_BATCH_CLASS_ID);
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
			List<BatchClassPluginConfig> batchClassPluginConfigs = batchClassPluginConfigService.getAllPluginConfiguration(
					batchInstanceId, CMIS_PLUGIN);
			if (initialPropertyValues.size() != 0) {
				initialPropertyValues.clear();
			}
			for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
				initialPropertyValues.add(batchClassPluginConfig.getValue());
			}
			for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
				if (batchClassPluginConfig.getName().equals("cmis.root_folder")) {
					batchClassPluginConfig.setValue(prop.getProperty(TEST_CASE2_ROOT_FOLDER));
				}
				if (batchClassPluginConfig.getName().equals("cmis.upload_file_type_ext")) {
					batchClassPluginConfig.setValue(prop.getProperty(TEST_CASE2_UPLOAD_FILE_EXT));
				}
				if (batchClassPluginConfig.getName().equals("cmis.server_URL")) {
					batchClassPluginConfig.setValue(prop.getProperty(TEST_CASE2_SERVER_URL));
				}
				if (batchClassPluginConfig.getName().equals("cmis.server_username")) {
					batchClassPluginConfig.setValue(prop.getProperty(TEST_CASE2_SERVER_USERNAME));
				}
				if (batchClassPluginConfig.getName().equals("cmis.server_password")) {
					batchClassPluginConfig.setValue(prop.getProperty(TEST_CASE2_SERVER_PWD));
				}
				if (batchClassPluginConfig.getName().equals("cmis.repository_id")) {
					batchClassPluginConfig.setValue(prop.getProperty(TEST_CASE2_REPOSITORY_ID));
				}
				if (batchClassPluginConfig.getName().equals("cmis.switch")) {
					batchClassPluginConfig.setValue(prop.getProperty(TEST_CASE2_SWITCH));
				}
			}
			batchClassPluginConfigService.updatePluginConfiguration(batchClassPluginConfigs);
			cmisExportService.exportContent(new BatchInstanceID(batchInstanceId), CMIS_PLUGIN);
			cmisExportService.deleteDocument(new BatchInstanceID(batchInstanceId), null);

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
	 * This is a test scenario to upload pdf file in repository, with incorrect value of repository id.
	 */
	@Test
	public void testCMISExportFailure() {
		boolean result = true;
		batchInstanceId = prop.getProperty(TEST_CASE3_BATCH_INSTANCE_ID);
		batchClassId = prop.getProperty(TEST_CASE3_BATCH_CLASS_ID);
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
			List<BatchClassPluginConfig> batchClassPluginConfigs = batchClassPluginConfigService.getAllPluginConfiguration(
					batchInstanceId, CMIS_PLUGIN);
			if (initialPropertyValues.size() != 0) {
				initialPropertyValues.clear();
			}
			for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
				initialPropertyValues.add(batchClassPluginConfig.getValue());
			}
			for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
				if (batchClassPluginConfig.getName().equals("cmis.root_folder")) {
					batchClassPluginConfig.setValue(prop.getProperty(TEST_CASE3_ROOT_FOLDER));
				}
				if (batchClassPluginConfig.getName().equals("cmis.upload_file_type_ext")) {
					batchClassPluginConfig.setValue(prop.getProperty(TEST_CASE3_UPLOAD_FILE_EXT));
				}
				if (batchClassPluginConfig.getName().equals("cmis.server_URL")) {
					batchClassPluginConfig.setValue(prop.getProperty(TEST_CASE3_SERVER_URL));
				}
				if (batchClassPluginConfig.getName().equals("cmis.server_username")) {
					batchClassPluginConfig.setValue(prop.getProperty(TEST_CASE3_SERVER_USERNAME));
				}
				if (batchClassPluginConfig.getName().equals("cmis.server_password")) {
					batchClassPluginConfig.setValue(prop.getProperty(TEST_CASE3_SERVER_PWD));
				}
				if (batchClassPluginConfig.getName().equals("cmis.repository_id")) {
					batchClassPluginConfig.setValue(prop.getProperty(TEST_CASE3_REPOSITORY_ID));
				}
				if (batchClassPluginConfig.getName().equals("cmis.switch")) {
					batchClassPluginConfig.setValue(prop.getProperty(TEST_CASE3_SWITCH));
				}
			}
			batchClassPluginConfigService.updatePluginConfiguration(batchClassPluginConfigs);
			cmisExportService.exportContent(new BatchInstanceID(batchInstanceId), CMIS_PLUGIN);
			result = false;
			assertTrue("Success", result);
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
	 * This methos does the clean-up operation : reverting back the changes done in plugin properties.
	 */
	@After
	public void tearDown() {

		int index = 0;
		List<BatchClassPluginConfig> batchClassPluginConfigs = batchClassPluginConfigService.getAllPluginConfiguration(
				batchInstanceId, CMIS_PLUGIN);
		batchSchemaService.setLocalFolderLocation(localFolderLocation);
		for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
			batchClassPluginConfig.setValue(initialPropertyValues.get(index));
			index++;
		}
		batchClassPluginConfigService.updatePluginConfiguration(batchClassPluginConfigs);

	}

}
