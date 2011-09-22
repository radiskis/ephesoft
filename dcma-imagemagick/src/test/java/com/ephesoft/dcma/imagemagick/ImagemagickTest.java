/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2010-2011 Ephesoft Inc. 
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

/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2010-2011 Ephesoft Inc. 
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

package com.ephesoft.dcma.imagemagick;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.imagemagick.service.ImageProcessService;
import com.ephesoft.dcma.util.FileUtils;

/**
 * This is Junit test for ImageMagick. It tests Two Positive Scenarios. 1. Generation of Png files and Thumbnail Files 2. Generation of
 * multi page tif and PDF files.
 * 
 * @author Ephesoft
 * @see com.ephesoft.dcma.reader.AbstractBarcodeTests
 * 
 */
public class ImagemagickTest extends AbstractImageMagickTests {

	/**
	 * String constants.
	 */
	private static final String PROP_FILE_IMAGEMAGICK_TEST = "test.properties";
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
	private static final String MULTI_PAGE_TIF1 = "BI41_documentDOC1.tif";
	/**
	 * String constants.
	 */
	private static final String MULTI_PAGE_TIF2 = "BI41_documentDOC2.tif";
	/**
	 * String constants.
	 */
	private static final String MULTI_PAGE_PDF1 = "BI41_documentDOC1.pdf";
	/**
	 * String constants.
	 */
	private static final String MULTI_PAGE_PDF2 = "BI41_documentDOC2.pdf";

	/**
	 * Instance of ImageProcessService.
	 */
	@Autowired
	private ImageProcessService imageProcessService;
	/**
	 * Instance of BatchSchemaService.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Batch instance for positive test scenario for Thumbnail and PNG creation.
	 */
	private transient String batchInstanceIdPngThumbnail;

	/**
	 * Batch instance for multi page tif and PDF creation.
	 */
	private transient String batchInstanceIdMultiPage;

	/**
	 * The folder where input files are kept and output files will be written.
	 */
	private transient String actualOutputFolder;

	/**
	 * The folder containing expected output batch xml.and other files.
	 */
	private transient String expectedOutputFolder;
	/**
	 * Variable stores initial local folder location.
	 */
	private transient String localFolderLocation;

	/**
	 * The folder containing sample input files.
	 */
	private String samplesFolder;

	/**
	 * This method prepares and initializes all the resources that would be required by the plugin.
	 * 
	 * @throws IOException
	 */
	@Before
	public void setUp() throws IOException {
		boolean result = false;
		String testFolderLocation;
		batchInstanceIdPngThumbnail = "BI8";
		batchInstanceIdMultiPage = "BI41";
		Properties prop = new Properties();
		try {
			prop.load(ImagemagickTest.class.getClassLoader().getResourceAsStream(PROP_FILE_IMAGEMAGICK_TEST));
		} catch (IOException e) {
			assertTrue(e.getMessage(), result);
		}
		localFolderLocation = batchSchemaService.getLocalFolderLocation();
		testFolderLocation = batchSchemaService.getTestFolderLocation();
		actualOutputFolder = testFolderLocation + File.separator + prop.getProperty(ACTUAL_OUTPUT_FOLDER);
		expectedOutputFolder = testFolderLocation + File.separator + prop.getProperty(EXPECTED_OUTPUT_FOLDER);
		samplesFolder = testFolderLocation + File.separator + prop.getProperty(SAMPLES_FOLDER);
		batchSchemaService.setLocalFolderLocation(actualOutputFolder);

	}

	/**
	 * This is a test scenario In which png files and thumbnail files are generated from the input files and btach.xml file.
	 */
	@Test
	public void testPNGThumbnailCreationTrue() {
		boolean result = false;
		try {
			String batchFolder = actualOutputFolder + File.separator + batchInstanceIdPngThumbnail;
			imageProcessService.createOcrInputImages(new BatchInstanceID(batchInstanceIdPngThumbnail), null, null);
			imageProcessService.createThumbnails(new BatchInstanceID(batchInstanceIdPngThumbnail), null);
			imageProcessService.createDisplayImages(new BatchInstanceID(batchInstanceIdPngThumbnail), null);
			compareXMLs(batchSchemaService.getLocalFolderLocation(), expectedOutputFolder, batchInstanceIdPngThumbnail);
			Batch batch = batchSchemaService.getBatch(batchInstanceIdPngThumbnail);
			Document document = batch.getDocuments().getDocument().get(0);
			List<Page> listOfPages = document.getPages().getPage();
			boolean fileExists = false;
			for (Page page : listOfPages) {
				File pngFile = new File(batchFolder + File.separator + page.getOCRInputFileName());
				File displayThumbnail = new File(batchFolder + File.separator + page.getThumbnailFileName());
				File compareThumbnail = new File(batchFolder + File.separator + page.getComparisonThumbnailFileName());
				File displayFile = new File(batchFolder + File.separator + page.getDisplayFileName());
				fileExists = pngFile.exists() && displayThumbnail.exists() && compareThumbnail.exists() && displayFile.exists();
				if (!fileExists) {
					break;
				}
			}
			assertTrue("Success", fileExists);
		} catch (DCMAException e) {
			assertTrue(e.getMessage(), result);
		} catch (Exception e) {
			assertTrue(e.getMessage(), result);
		}
	}

	/**
	 * This method tests the Multi page tif pdf genertaion.
	 */
	@Test
	public void testMultiPageFilesTrue() {
		boolean result = false;
		boolean filesExist = false;
		String batchFolder = actualOutputFolder + File.separator + batchInstanceIdMultiPage;
		try {
			imageProcessService.createMultiPageFiles(new BatchInstanceID(batchInstanceIdMultiPage), null);
			compareXMLs(actualOutputFolder, expectedOutputFolder, batchInstanceIdMultiPage);
			File multiPageTif1 = new File(batchFolder + File.separator + MULTI_PAGE_TIF1);
			File multiPageTif2 = new File(batchFolder + File.separator + MULTI_PAGE_TIF2);
			File multiPagePdf1 = new File(batchFolder + File.separator + MULTI_PAGE_PDF1);
			File multiPagePdf2 = new File(batchFolder + File.separator + MULTI_PAGE_PDF2);
			filesExist = multiPageTif1.exists() && multiPageTif2.exists() && multiPagePdf1.exists() && multiPagePdf2.exists();
			assertTrue("Success", filesExist);

		} catch (DCMAException ex) {
			assertTrue(ex.getMessage(), result);
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
		boolean result = false;
		batchSchemaService.setLocalFolderLocation(localFolderLocation);
		FileUtils.deleteDirectoryAndContents(actualOutputFolder + File.separator + batchInstanceIdPngThumbnail);

		try {
			FileUtils.copyDirectoryWithContents(samplesFolder, actualOutputFolder);

		} catch (IOException e) {
			assertTrue(e.getMessage(), result);
		}

	}

}
