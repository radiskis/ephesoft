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

package com.ephesoft.dcma.tesseract;

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
import com.ephesoft.dcma.tesseract.service.TesseractService;
import com.ephesoft.dcma.util.FileUtils;

/**
 * This is Junit test for TesseractService. It contains two test cases for generating HOCR file, first is positive where batch is of
 * type Tesseract, second is negative where batch is not of Tesseract.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.tesseract.AbstractTesseractTests
 */
public class TesseractReaderTest extends AbstractTesseractTests {

	/**
	 * String constants.
	 */
	private static final String PROPERTIES_FILE_TESSERACT_TEST = "test.properties";
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
	 * Instance of tesseract service.
	 */
	@Autowired
	private transient TesseractService tesseractService;
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
	 * Instance of BatchInstanceService.
	 */
	@Autowired
	private transient BatchClassService batchClassService;
	/**
	 * Batch instance id for positive test scenario.
	 */
	private transient String batchInstanceIdSuccess;
	/**
	 * Batch instance id for negative test scenario.
	 */
	private transient String batchInstanceIdFailure;
	/**
	 * Batch class id for positive test scenario.
	 */
	private transient String batchClassIdSuccess;
	/**
	 * Batch class id for negative test scenario.
	 */
	private transient String batchClassIdFailure;
	/**
	 * Folder where the input files are kept and output files are written.
	 */
	private transient String actualOutputFolder;
	/**
	 * Folder where expected output batch xml is kept.
	 */
	private transient String expectedOutputFolder;
	/**
	 * Folder where sample images and XML are kept.
	 */
	private transient String samplesFolder;
	/**
	 * Variable stores initial local folder location.
	 */
	private transient String localFolderLocation;

	/**
	 * This method initialize all the resources that would be required by tesseract service.
	 */
	@Before
	public void setUp() {
		boolean result = false;
		String testFolderLocation;
		batchInstanceIdSuccess = "BI8";
		batchInstanceIdFailure = "BI3D";
		batchClassIdSuccess = "BC2";
		batchClassIdFailure = "BC1";
		Properties prop = new Properties();
		try {
			prop.load(TesseractReaderTest.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_TESSERACT_TEST));
		} catch (IOException e) {
			assertTrue(e.getMessage(), result);
		}
		localFolderLocation = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceIdSuccess);
		testFolderLocation = batchSchemaService.getTestFolderLocation();
		actualOutputFolder = (testFolderLocation + File.separator + prop.getProperty(ACTUAL_OUTPUT_FOLDER));
		expectedOutputFolder = (testFolderLocation + File.separator + prop.getProperty(EXPECTED_OUTPUT_FOLDER));
		samplesFolder = (testFolderLocation + File.separator + prop.getProperty(SAMPLES_FOLDER));
		batchSchemaService.setLocalFolderLocation(actualOutputFolder);
	}

	/**
	 * This method tests the functionality of Tesseract service, compares xml formed and checks for HOCR file, where batch is of
	 * Tesseract.
	 */
	@Test
	public void testTesseractReaderSuccess() {
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
			tesseractService.generateHOCRFiles(new BatchInstanceID(batchInstanceIdSuccess), null);
			compareXMLs(actualOutputFolder, expectedOutputFolder, batchInstanceIdSuccess);
			boolean hocrExists = FileUtils.checkHocrFileExist(actualOutputFolder + File.separator + batchInstanceIdSuccess);
			if (!hocrExists) {
				assertTrue("HOCR file not created", result);
			}
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
	 * This method tests the functionality of Tesseract service, compares xml formed and checks for HOCR file,where batch is not of
	 * Tesseract.
	 */
	@Test
	public void testTesseractReaderFailure() {
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
			tesseractService.generateHOCRFiles(new BatchInstanceID(batchInstanceIdFailure), null);
			compareXMLs(actualOutputFolder, expectedOutputFolder, batchInstanceIdFailure);
			boolean hocrExists = FileUtils.checkHocrFileExist(actualOutputFolder + File.separator + batchInstanceIdFailure);
			if (!hocrExists) {
				assertTrue("HOCR file not created", result);
			}
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
	 * This method does the clean-up operation like deleting the xml in inputOutput folder and copying the sample xml from samples
	 * folder.
	 */
	@After
	public void tearDown() {
		batchSchemaService.setLocalFolderLocation(localFolderLocation);
		FileUtils.deleteAllXMLs(actualOutputFolder + File.separator + batchInstanceIdSuccess);
		FileUtils.deleteAllHocrFiles(actualOutputFolder + File.separator + batchInstanceIdSuccess);
		FileUtils.copyAllXMLFiles(samplesFolder + File.separator + batchInstanceIdSuccess, actualOutputFolder + File.separator
				+ batchInstanceIdSuccess);
		FileUtils.deleteAllXMLs(actualOutputFolder + File.separator + batchInstanceIdFailure);
		FileUtils.deleteAllHocrFiles(actualOutputFolder + File.separator + batchInstanceIdFailure);
		FileUtils.copyAllXMLFiles(samplesFolder + File.separator + batchInstanceIdSuccess, actualOutputFolder + File.separator
				+ batchInstanceIdFailure);
	}
}
