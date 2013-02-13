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

package com.ephesoft.dcma.barcodeextraction;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.barcodeextraction.service.BarcodeExtractionService;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.FieldType;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.da.service.FieldTypeService;
import com.ephesoft.dcma.util.FileUtils;

/**
 * This is Junit test for BarcodeExtractionService. It contains two scenarios: one is a positive test case when barcode type is
 * specified correctly for particular instance and second is a negative test case when barcode type is not specified correctly for
 * particular instance.
 * 
 * @author Ephesoft
 * 
 */

public class BarcodeExtractionReaderTest extends AbstractBarcodeExtractionTests {

	/**
	 * String constants.
	 */
	private static final String PROP_FILE_BARCODE_TEST = "test.properties";
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
	 * Instance of BarcodeExtraction service.
	 */
	@Autowired
	private transient BarcodeExtractionService barcodeExtractionService;
	/**
	 * Instance of BatchSchema service.
	 */
	@Autowired
	private transient BatchSchemaService batchSchemaService;
	/**
	 * Instance of FieldType service.
	 */
	@Autowired
	private transient FieldTypeService filFieldTypeService;
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
	 * Batch instance for positive test scenario.
	 */
	private transient String batchInstanceIdSuccess;
	/**
	 * Batch instance for negative test scenario.
	 */
	private transient String batchInstanceIdFailure;
	/**
	 * Batch class for positive test scenario.
	 */
	private transient String batchClassIdSuccess;
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
	 * Method to initialize resources.
	 */
	@Before
	public void setUp() {
		boolean result = false;
		String testFolderLocation;
		localFolderLocation = batchInstanceService.getSystemFolderForBatchInstanceId("BI1A");
		testFolderLocation = batchSchemaService.getTestFolderLocation();
		batchInstanceIdSuccess = "BI1A";
		batchInstanceIdFailure = "BI15";
		batchClassIdFailure = "BC2";
		batchClassIdSuccess = "BC2";
		Properties prop = new Properties();
		try {
			prop.load(BarcodeExtractionReaderTest.class.getClassLoader().getResourceAsStream(PROP_FILE_BARCODE_TEST));
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
	 * This is a test scenario when bar-code is found on image file.
	 */
	@Test
	public void testBarcodeExtractionPluginTrue() {
		String initialBarcodeType;
		boolean result = false;
		boolean created = false;
		BatchClass initialBatchClass = new BatchClass();
		try {
			BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdSuccess);
			if (null == batchInstance) {
				batchInstance = new BatchInstance();
				batchInstance.setIdentifier(batchInstanceIdSuccess);
				BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdSuccess);
				batchInstance.setBatchClass(batchClass);
				batchInstanceService.createBatchInstance(batchInstance);
				created = true;
			} else {
				initialBatchClass = batchInstance.getBatchClass();
				BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdSuccess);
				batchInstance.setBatchClass(batchClass);
			}
			FieldType fieldType = filFieldTypeService.getFieldType("State", "US-Invoice-data", batchInstanceIdSuccess);
			initialBarcodeType = fieldType.getBarcodeType();

			FieldType fieldType2 = filFieldTypeService.getFieldType("State", "US-Invoice-data", batchInstanceIdSuccess);
			fieldType.setBarcodeType("QR");
			filFieldTypeService.updateFieldType(fieldType2);
			barcodeExtractionService.extractPageBarCode(new BatchInstanceID(batchInstanceIdSuccess), "BARCODE-EXTRACTION");
			// compare two XML's
			compareXMLs(actualOutputFolder, expectedOutputFolder, batchInstanceIdSuccess);
			FieldType fieldType3 = filFieldTypeService.getFieldType("State", "US-Invoice-data", batchInstanceIdSuccess);
			fieldType.setBarcodeType(initialBarcodeType);
			filFieldTypeService.updateFieldType(fieldType3);
		} catch (DCMAException e) {
			assertTrue(e.getMessage(), result);
		} catch (Exception e) {
			assertTrue(e.getMessage(), result);
		} finally {
			if (created) {
				BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdSuccess);
				if (batchInstance != null) {
					batchInstanceService.removeBatchInstance(batchInstance);
				}
			} else {
				BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdSuccess);
				batchInstance.setBatchClass(initialBatchClass);
				batchInstanceService.updateBatchInstance(batchInstance);
			}
		}
	}

	/**
	 * This is a test scenario when type of barcode is not specified for the document type.
	 */
	@Test
	public void testBarcodeExtractionPluginFalse() {
		boolean created = false;
		boolean result = false;
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
			barcodeExtractionService.extractPageBarCode(new BatchInstanceID(batchInstanceIdFailure), "BARCODE-EXTRACTION");
			// compare two XML's
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
	 * This method does the clean-up operation : deleting the newly modified xml in inputOutput folder and replacing them with
	 * corresponding xml in samples folder.
	 */
	@After
	public void tearDown() {
		String batchFailureFolder = actualOutputFolder + File.separator + batchInstanceIdFailure;
		String batchSuccessFolder = actualOutputFolder + File.separator + batchInstanceIdSuccess;
		batchSchemaService.setLocalFolderLocation(localFolderLocation);
		FileUtils.deleteAllXMLs(batchFailureFolder);
		FileUtils.deleteAllXMLs(batchSuccessFolder);
		FileUtils.copyAllXMLFiles(samplesFolder + File.separator + batchInstanceIdFailure, batchFailureFolder);
		FileUtils.copyAllXMLFiles(samplesFolder + File.separator + batchInstanceIdSuccess, batchSuccessFolder);
	}

}
