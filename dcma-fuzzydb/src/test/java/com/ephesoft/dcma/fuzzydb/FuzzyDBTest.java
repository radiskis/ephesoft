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

package com.ephesoft.dcma.fuzzydb;

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
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.fuzzydb.service.FuzzyDBSearchService;
import com.ephesoft.dcma.util.FileUtils;

/**
 * This is Junit test for FuzzyDBSearchService. It contains three scenarios: first is a positive test case for extractDataFields in
 * which database is learned correctly,second is negative testcase for extractDataFields in which database is not learned and third is
 * testcase for fuzzy search.
 * 
 * @author Ephesoft
 * @see com.ephesoft.dcma.fuzzydb.AbstractFuzzyDBTest
 * 
 */
public class FuzzyDBTest extends AbstractFuzzyDBTest {

	/**
	 * String constants.
	 */
	private static final String PROP_FILE_FUZZY_TEST = "test.properties";
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
	private static final String SAMPLES_FOLDER = "samples.location";
	/**
	 * String constants.
	 */
	private static final String SEACRCH_STRING = "search.string";

	/**
	 * Instance of FuzzyDBSearchService.
	 */
	@Autowired
	private transient FuzzyDBSearchService fuzzyDBSearchService;
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
	 * The folder containing expected output batch xml.
	 */
	private transient String expectedOutputFolder;

	/**
	 * The folder containing sample xmls.
	 */
	private transient String samplesFolder;
	/**
	 * Variable stores initial local folder location.
	 */
	private transient String localFolderLocation;
	/**
	 * Variable for property file.
	 */
	private final Properties prop = new Properties();

	/**
	 * Method to initialize resources.
	 */
	@Before
	public void setUp() {
		boolean result = false;
		String testFolderLocation;
		localFolderLocation = batchClassService.getSystemFolderForBatchClassIdentifier(batchClassId);
		testFolderLocation = batchSchemaService.getTestFolderLocation();
		batchInstanceId = "BI75";
		batchClassId = "BC2";

		try {
			prop.load(FuzzyDBTest.class.getClassLoader().getResourceAsStream(PROP_FILE_FUZZY_TEST));
		} catch (IOException e) {
			assertTrue(e.getMessage(), result);
		}
		actualOutputFolder = (testFolderLocation + File.separator + prop.getProperty(ACTUAL_OUTPUT_FOLDER));
		expectedOutputFolder = (testFolderLocation + File.separator + prop.getProperty(EXPECTED_OUTPUT_FOLDER));
		samplesFolder = (testFolderLocation + File.separator + prop.getProperty(SAMPLES_FOLDER));
		batchSchemaService.setLocalFolderLocation(actualOutputFolder);
		FileUtils.deleteDirectoryAndContents(actualOutputFolder + File.separator + "properties");

	}

	/**
	 * This is a test scenario when database is learned correctly before fuzzydb extraction.
	 */
	@Test
	public void testFuzzyDBPluginSuccess() {
		boolean result = false;
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
			fuzzyDBSearchService.learnDataBase(new BatchClassID(batchClassId), true);
			fuzzyDBSearchService.extractDataBaseFields(new BatchInstanceID(batchInstanceId), null);
			compareXMLs(actualOutputFolder, expectedOutputFolder, batchInstanceId);
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
	 * This is a test scenario when database is learned correctly before fuzzydb extraction.
	 */
	@Test
	public void testFuzzyDBPluginFailure() {
		boolean result = true;
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
			fuzzyDBSearchService.learnDataBase(new BatchClassID(batchClassId), false);
			fuzzyDBSearchService.extractDataBaseFields(new BatchInstanceID(batchInstanceId), null);
			compareXMLsNotEqual(actualOutputFolder, expectedOutputFolder, batchInstanceId);
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
	 * This is test scenario to test fuzzy search.
	 */
	@Test
	public void testFuzzySearch() {
		boolean result = false;
		try {
			String searchString = prop.getProperty(SEACRCH_STRING);
			fuzzyDBSearchService.learnDataBase(new BatchClassID(batchClassId), true);
			List<List<String>> searchResult = fuzzyDBSearchService.fuzzyTextSearch(new BatchInstanceID(batchInstanceId), null,
					searchString);
			if (searchResult.size() == 0) {
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
		FileUtils.deleteAllXMLs(actualOutputFolder + File.separator + batchInstanceId);
		FileUtils.copyAllXMLFiles(samplesFolder + File.separator + batchInstanceId, actualOutputFolder + File.separator
				+ batchInstanceId);

	}

}
