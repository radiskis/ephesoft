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

package com.ephesoft.dcma.kvextarction;

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
import com.ephesoft.dcma.kvextraction.service.KVExtractionService;
import com.ephesoft.dcma.util.FileUtils;

/**
 * This is Junit test for KVExtractionService. It contains three scenarios: two are positive, first case applies KV extraction on
 * Application Checklist and second one applies KV extraction on US Invoice , third scenario is negative where HOCR xml file is not
 * provided.
 * 
 * @author Ephesoft
 * @see com.ephesoft.dcma.kvextarction.AbstractKVExtractionTest
 * 
 */

public class KVExtarctionTest extends AbstractKVExtractionTest {

	/**
	 * String constants.
	 */
	private static final String PROP_FILE_KV_TEST = "test.properties";
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
	 * Instance of KVExtractionService.
	 */
	@Autowired
	private transient KVExtractionService kvExtractionService;
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
	 * Batch instance for positive test scenario.
	 */
	private transient String batchInstanceIdDocType1;
	/**
	 * Batch instance for positive test scenario.
	 */
	private transient String batchInstanceIdDocType2;
	/**
	 * Batch instance for negative test scenario.
	 */
	private transient String batchInstanceIdFailure;
	/**
	 * Batch class for positive test scenario.
	 */
	private transient String batchClassIdDocType1;
	/**
	 * Batch class for positive test scenario.
	 */
	private transient String batchClassIdDocType2;
	/**
	 * Batch class for negative test scenario.
	 */
	private transient String batchClassIdFailure;

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
		
		batchInstanceIdDocType1 = "BI75";
		batchInstanceIdDocType2 = "BI7B";
		batchInstanceIdFailure = "BI71";
		batchClassIdDocType1 = "BC2";
		batchClassIdDocType2 = "BC2";
		batchClassIdFailure = "BC2";

		localFolderLocation = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceIdDocType1);
		testFolderLocation = batchSchemaService.getTestFolderLocation();
		try {
			prop.load(KVExtarctionTest.class.getClassLoader().getResourceAsStream(PROP_FILE_KV_TEST));
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
	 * This is a test scenario for KV extraction on document type Application Checklist.
	 */
	@Test
	public void testKVExtractionApplicationCheckListSuccess() {
		boolean result = false;
		boolean created = false;
		BatchClass initialBatchClass = new BatchClass();
		try {
			BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdDocType1);
			if (null == batchInstance) {
				batchInstance = new BatchInstance();
				batchInstance.setIdentifier(batchInstanceIdDocType1);
				BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdDocType1);
				batchInstance.setBatchClass(batchClass);
				batchInstanceService.createBatchInstance(batchInstance);
				created = true;
			} else {
				initialBatchClass = batchInstance.getBatchClass();
				BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdDocType1);
				batchInstance.setBatchClass(batchClass);

			}
			kvExtractionService.extractKVDocumentFields(new BatchInstanceID(batchInstanceIdDocType1), null);
			compareXMLs(actualOutputFolder, expectedOutputFolder, batchInstanceIdDocType1);
		} catch (DCMAException e) {
			assertTrue(e.getMessage(), result);
		} catch (Exception e) {
			assertTrue(e.getMessage(), result);
		} finally {
			if (created) {
				BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdDocType1);
				if (batchInstance != null) {
					batchInstanceService.removeBatchInstance(batchInstance);
				}
			} else {
				BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdDocType1);
				batchInstance.setBatchClass(initialBatchClass);
				batchInstanceService.updateBatchInstance(batchInstance);
			}
		}
	}

	/**
	 * This is a test scenario for KV extraction on document type US Invoice.
	 */
	@Test
	public void testKVExtractionUSInvoiceSuccess() {
		boolean result = false;
		boolean created = false;
		BatchClass initialBatchClass = new BatchClass();
		try {
			BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdDocType2);
			if (null == batchInstance) {
				batchInstance = new BatchInstance();
				batchInstance.setIdentifier(batchInstanceIdDocType2);
				BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdDocType2);
				batchInstance.setBatchClass(batchClass);
				batchInstanceService.createBatchInstance(batchInstance);
				created = true;
			} else {
				initialBatchClass = batchInstance.getBatchClass();
				BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdDocType2);
				batchInstance.setBatchClass(batchClass);

			}
			kvExtractionService.extractKVDocumentFields(new BatchInstanceID(batchInstanceIdDocType2), null);
			compareXMLs(actualOutputFolder, expectedOutputFolder, batchInstanceIdDocType2);
		} catch (DCMAException e) {
			assertTrue(e.getMessage(), result);
		} catch (Exception e) {
			assertTrue(e.getMessage(), result);
		} finally {
			if (created) {
				BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdDocType2);
				if (batchInstance != null) {
					batchInstanceService.removeBatchInstance(batchInstance);
				}
			} else {
				BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdDocType2);
				batchInstance.setBatchClass(initialBatchClass);
				batchInstanceService.updateBatchInstance(batchInstance);
			}
		}
	}

	/**
	 * This is a test scenario when HOCR xml file is not provided.
	 */
	@Test
	public void testKVExtractionFailure() {
		boolean result = true;
		boolean created = false;
		BatchClass initialBatchClass = new BatchClass();
		try {
			BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdFailure);
			if (null == batchInstance) {
				batchInstance = new BatchInstance();
				batchInstance.setIdentifier(batchInstanceIdFailure);
				BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdFailure);
				batchInstance.setBatchClass(batchClass);
				batchInstanceService.createBatchInstance(batchInstance);
				created = true;
			} else {
				initialBatchClass = batchInstance.getBatchClass();
				BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdFailure);
				batchInstance.setBatchClass(batchClass);

			}
			kvExtractionService.extractKVDocumentFields(new BatchInstanceID(batchInstanceIdFailure), null);
			compareXMLs(actualOutputFolder, expectedOutputFolder, batchInstanceIdFailure);
		} catch (DCMAException e) {
			assertTrue(e.getMessage(), result);
		} catch (Exception e) {
			assertTrue(e.getMessage(), result);
		} finally {
			if (created) {
				BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdFailure);
				if (batchInstance != null) {
					batchInstanceService.removeBatchInstance(batchInstance);
				}
			} else {
				BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdFailure);
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

		batchSchemaService.setLocalFolderLocation(localFolderLocation);
		FileUtils.deleteAllXMLs(actualOutputFolder + File.separator + batchInstanceIdDocType1);
		FileUtils.deleteAllXMLs(actualOutputFolder + File.separator + batchInstanceIdDocType2);
		FileUtils.deleteAllXMLs(actualOutputFolder + File.separator + batchInstanceIdFailure);
		FileUtils.copyAllXMLFiles(samplesFolder + File.separator + batchInstanceIdDocType1, actualOutputFolder + File.separator
				+ batchInstanceIdDocType1);
		FileUtils.copyAllXMLFiles(samplesFolder + File.separator + batchInstanceIdDocType2, actualOutputFolder + File.separator
				+ batchInstanceIdDocType2);
		FileUtils.copyAllXMLFiles(samplesFolder + File.separator + batchInstanceIdFailure, actualOutputFolder + File.separator
				+ batchInstanceIdFailure);

	}

}
