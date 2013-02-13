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

package com.ephesoft.dcma.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

/**
 * This is Web service util class.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see org.apache.commons.io.IOUtils
 *
 */
public class WebServiceUtil {

	/**
	 * RANDOM_CONST int.
	 */
	private static final int RANDOM_CONST = 1000;

	/**
	 * BUFFER_SIZE int.
	 */
	private static final int BUFFER_SIZE = 1024;

	/**
	 * SERVERINPUTFOLDERNAME String.
	 */
	public static final String SERVERINPUTFOLDERNAME = "in";
	
	/**
	 * SERVEROUTPUTFOLDERNAME String.
	 */
	public static final String SERVEROUTPUTFOLDERNAME = "out";
	
	/**
	 * EMPTY_STRING String.
	 */
	public static final String EMPTY_STRING = "";
	
	/**
	 * DOT String.
	 */
	public static final String DOT = ".";
	
	/**
	 * RSP_EXTENSION String.
	 */
	public static final String RSP_EXTENSION = ".rsp";
	
	/**
	 * ON_STRING String.
	 */
	public static final String ON_STRING = "ON";
	
	/**
	 * OFF_STRING String.
	 */
	public static final String OFF_STRING = "OFF";
	
	/**
	 * TRUE String.
	 */
	public static final String TRUE = "true";

	/**
	 * FALSE String.
	 */
	public static final String FALSE = "false";
	
	/**
	 * bufferSize String.
	 */
	public static int bufferSize = BUFFER_SIZE;
	
	/**
	 * BACKUP_PROPERTY_FILE String.
	 */
	private static final String BACKUP_PROPERTY_FILE = "META-INF" + File.separator + "dcma-util" + File.separator
			+ "dcma-backup-service.properties";

	/**
	 * DOCUMENTID String.
	 */
	public static final String DOCUMENTID = "docWS";

	/**
	 * Constant for content disposition header value in the response.
	 */
	public static final String ATTACHMENT_FILENAME = "attachment; filename=\"";

	/**
	 * Constant for content disposition header name in the response.
	 */
	public static final String CONTENT_DISPOSITION = "Content-Disposition";

	/**
	 * Constant for content type header in the response.
	 */
	public static final String APPLICATION_X_ZIP = "application/x-zip\r\n";

	/**
	 * TESSERACT String.
	 */
	public static final String TESSERACT = "tesseract";
	
	/**
	 * RECOSTAR String.
	 */
	public static final String RECOSTAR = "recostar";

	/**
	 * CMD_LANGUAGE String.
	 */	
	public static final String CMD_LANGUAGE = "cmdLanguage";

	/**
	 * TESSERACT_VERSION String.
	 */
	public static final String TESSERACT_VERSION = "tesseractVersion";

	/**
	 * PROJECT_FILE String.
	 */
	public static final String PROJECT_FILE = "projectFile";

	/**
	 * COLOR_SWITCH String.
	 */
	public static final String COLOR_SWITCH = "colorSwitch";

	/**
	 * BATCH_CLASS_IDENTIFIER String.
	 */
	public static final String BATCH_CLASS_IDENTIFIER = "batchClassIdentifier";

	/**
	 * IMAGE_NAME String.
	 */
	public static final String IMAGE_NAME = "imageName";

	/**
	 * DOC_TYPE String.
	 */
	public static final String DOC_TYPE = "docType";
	
	/**
	 * OCR_ENGINE String.
	 */
	public static final String OCR_ENGINE = "ocrEngine";
	
	/**
	 * OUTPUT_PARAMS String.
	 */
	public static final String OUTPUT_PARAMS = "outputParams";
	
	/**
	 * INPUT_PARAMS String.
	 */
	public static final String INPUT_PARAMS = "inputParams";
	
	/**
	 * IS_GHOSTSCRIPT String.
	 */
	public static final String IS_GHOSTSCRIPT = "isGhostscript";
	
	/**
	 * LUCENE_SEARCH_CLASSIFICATION_SAMPLE String.
	 */
	public static final String LUCENE_SEARCH_CLASSIFICATION_SAMPLE = "lucene-search-classification-sample";
	
	/**
	 * IMAGE_CLASSIFICATION_SAMPLE String.
	 */
	public static final String IMAGE_CLASSIFICATION_SAMPLE = "image-classification-sample";
	
	/**
	 * IDENTIFIER2 String.
	 */
	public static final String IDENTIFIER2 = "identifier";
	
	/**
	 * DCMA_GWT_ADMIN_EXPORT_BATCH_CLASS_DOWNLOAD String.
	 */
	public static final String DCMA_GWT_ADMIN_EXPORT_BATCH_CLASS_DOWNLOAD = "/dcma-gwt-admin/exportBatchClassDownload";

	/**
	 * PG0 String.
	 */
	public static final String PG0 = "PG0";

	/**
	 * BUILD_XML String.
	 */
	public static final String BUILD_XML = "build.xml";
	
	/**
	 * PDF_GENERATOR_ENGINE String.
	 */
	public static final String PDF_GENERATOR_ENGINE = "pdfGeneratorEngine";

	/**
	 * GHOSTSCRIPT_PDF_PARAMETERS String.
	 */
	public static final String GHOSTSCRIPT_PDF_PARAMETERS = "ghostscriptPdfParameters";

	/**
	 * EXTRACTION_API String.
	 */
	public static final String EXTRACTION_API = "extractionAPI";

	/**
	 * PDF_OPTIMIZATION_SWITCH String.
	 */
	public static final String PDF_OPTIMIZATION_SWITCH = "pdfOptimizationSwitch";
	
	/**
	 * MULTIPAGE_TIF_SWITCH String.
	 */
	public static final String MULTIPAGE_TIF_SWITCH = "multipageTifSwitch";
	
	/**
	 * PDF_OPTIMIZATION_PARAMS String.
	 */
	public static final String PDF_OPTIMIZATION_PARAMS = "pdfOptimizationParams";

	/**
	 * IMAGE_PROCESSING_API String.
	 */
	public static final String IMAGE_PROCESSING_API = "imageProcessingAPI";
	
	/**
	 * DOCUMENT_TYPE String.
	 */
	public static final String DOCUMENT_TYPE = "documentType";
	
	/**
	 * HOCR_FILE String.
	 */
	public static final String HOCR_FILE = "hocrFile";

	/**
	 * To fetch Configuration.
	 * @return Properties
	 * @throws IOException in case of error
	 */
	public static Properties fetchConfig() throws IOException {

		final ClassPathResource classPathResource = new ClassPathResource(BACKUP_PROPERTY_FILE);
		final Properties properties = new Properties();

		InputStream input = null;
		try {
			input = classPathResource.getInputStream();
			properties.load(input);
		} finally {
			if (input != null) {
				IOUtils.closeQuietly(input);
			}
		}
		return properties;
	}

	/**
	 * To create WebService Working Directory.
	 * @param parentDir {@link String}
	 * @return {@link String}
	 * @throws FileCreationException in case of error
	 */
	public static String createWebServiceWorkingDir(final String parentDir) throws FileCreationException {
		String currentWorkingFolderName = generateRandomFolderName();
		File dir = new File(parentDir + File.separator + currentWorkingFolderName + File.separator + SERVERINPUTFOLDERNAME);

		boolean createDir = dir.mkdirs();
		if (!createDir) {
			createDir = dir.mkdirs();
			if (!createDir) {
				throw new FileCreationException(
						"Error occured while creating the working dir for web services. Returning from web services.");
			}
		}
		return dir.getAbsolutePath();
	}

	/**
	 * To generate Random Folder Name.
	 * @return {@link String}
	 */
	public static String generateRandomFolderName() {
		Random random = new Random();
		return String.valueOf(System.nanoTime()) + String.valueOf(random.nextInt(RANDOM_CONST)) + String.valueOf(random.nextInt(RANDOM_CONST))
				+ String.valueOf(random.nextInt(RANDOM_CONST));
	}

	/**
	 * To create WebService Output Dir.
	 * @param workingDir {@link String}
	 * @return {@link String}
	 * @throws FileCreationException in case of error
	 */
	public static String createWebServiceOutputDir(String workingDir) throws FileCreationException {
		File dir = new File(new File(workingDir).getParent() + File.separator + WebServiceUtil.SERVEROUTPUTFOLDERNAME);
		boolean createDir = dir.mkdirs();
		if (!createDir) {
			createDir = dir.mkdirs();
			if (!createDir) {
				throw new FileCreationException("Error occured while creating the working dir for web services. Returning from web services.");
			}
		}
		return dir.getAbsolutePath();
	}

	/**
	 * To validate Split API.
	 * @param fileMap MultiValueMap<String, MultipartFile>
	 * @param isGSTool boolean
	 * @param outputParams {@link String}
	 * @param inputParams {@link String}
	 * @return {@link String}
	 */
	public static String validateSplitAPI(MultiValueMap<String, MultipartFile> fileMap, boolean isGSTool, String outputParams,
			String inputParams) {
		String results = EMPTY_STRING;
		if (fileMap.size() == 0) {
			results = "No files provided to split";
		} else if (isGSTool && inputParams.isEmpty()) {
			results = "Input Params expected with GhostScript tool flag. Please set the input params.";
		}
		return results;
	}

	/**
	 * To validate Export Batch Class API.
	 * @param imBaseFolderName {@link String}
	 * @param identifier {@link String}
	 * @param searchSampleName {@link String}
	 * @return {@link String}
	 */
	public static String validateExportBatchClassAPI(String imBaseFolderName, String identifier, String searchSampleName) {
		String results = EMPTY_STRING;
		if (imBaseFolderName.isEmpty()) {
			results = "Please input the valid imBaseFolderName.";
		} else if (identifier.isEmpty()) {
			results = "Please input the valid identifier.";
		} else if (searchSampleName.isEmpty()) {
			results = "Please input the valid searchSampleName.";
		}
		return results;
	}

	/**
	 * To validate Searchable API.
	 * @param outputPDFFileName {@link String}
	 * @param projectFile {@link String}
	 * @param pdfFileType {@link String}
	 * @param checkSearchableImage {@link String}
	 * @param checkColorImage {@link String}
	 * @return {@link String}
	 */
	public static String validateSearchableAPI(String outputPDFFileName, String projectFile, String pdfFileType,
			String checkSearchableImage, String checkColorImage) {
		String results = EMPTY_STRING;
		if (outputPDFFileName == null || outputPDFFileName.isEmpty() || !outputPDFFileName.endsWith(pdfFileType)) {
			results = "Invalid name for pdf file name.";
		} else if (checkSearchableImage == null || checkSearchableImage.isEmpty()) {
			results = "Either invalid value or parameter checkSearchableImage not specified.";
		} else if (checkColorImage == null || checkColorImage.isEmpty()) {
			results = "Either invalid value or parameter checkColorImage not specified.";
		} else if (projectFile == null || projectFile.isEmpty() || !projectFile.endsWith(WebServiceUtil.RSP_EXTENSION)) {
			results = "Invalid name for projectFile file name.";
		}
		return results;
	}

	/**
	 * To validate Create OCR API.
	 * @param workingDir {@link String}
	 * @param tool {@link String}
	 * @param colorSwitch {@link String}
	 * @param projectFile {@link String}
	 * @param tesseractVersion {@link String}
	 * @param cmdLanguage {@link String}
	 * @return {@link String}
	 */
	public static String validateCreateOCRAPI(String workingDir, String tool, String colorSwitch, String projectFile,
			String tesseractVersion, String cmdLanguage) {
		String results = EMPTY_STRING;
		File rspFile = new File(workingDir + File.separator + projectFile);
		if (tool == null || tool.isEmpty()) {
			results = "Please select the tool for creating OCR.";
		}
		if (results.isEmpty() && tool.equalsIgnoreCase("tesseract")) {
			if (tesseractVersion == null || tesseractVersion.isEmpty()) {
				results = "Please input the valid tesseractVersion.";
			} else if (cmdLanguage == null || cmdLanguage.isEmpty()) {
				results = "Please input the valid cmdLanguage.";
			}
			// cmdLanguage and Tesseract Version expected
		} else if (tool.equalsIgnoreCase("recostar")) {
			if (projectFile == null || projectFile.isEmpty()) {
				results = "Please input the valid projectFile.";
			} else if (colorSwitch == null || colorSwitch.isEmpty()) {
				results = "Please input the valid colorSwitch.";
			} else if (!rspFile.exists()) {
				results = "RSP file as specified in the params is not supplied in valid input. Please specify the correct input params.";
			}
		} else {
			results = "Please select the valid tool for creating OCR.";
		}
		return results;
	}

	/**
	 * To validate Barcode Extraction Input.
	 * @param batchClassIdentifier {@link String}
	 * @param imageName {@link String}
	 * @param docType {@link String}
	 * @return {@link String}
	 */
	public static String validateBarcodeExtractionInput(String batchClassIdentifier, String imageName, String docType) {
		String results = EMPTY_STRING;
		if (batchClassIdentifier == null || batchClassIdentifier.isEmpty()) {
			results = "Batch Class Identifier is not valid.";
		} else if ((imageName == null || imageName.isEmpty()) && results.isEmpty()) {
			results = "image name is not valid.";
		} else if ((docType == null || docType.isEmpty()) && results.isEmpty()) {
			results = "doc type is not valid.";
		}
		return results;
	}

	/**
	 * To validate Create Multi Page File.
	 * @param ghostscriptPdfParameters {@link String} 
	 * @param imageProcessingAPI {@link String} 
	 * @param pdfOptimizationSwitch {@link String} 
	 * @param multipageTifSwitch {@link String} 
	 * @param pdfOptimizationParams  {@link String} 
	 * @return {@link String} 
	 */
	public static String validateCreateMultiPageFile(String ghostscriptPdfParameters, String imageProcessingAPI,
			String pdfOptimizationSwitch, String multipageTifSwitch, String pdfOptimizationParams) {
		String results = EMPTY_STRING;
		if (imageProcessingAPI.isEmpty()
				|| !(imageProcessingAPI.equalsIgnoreCase("IMAGE_MAGICK") || imageProcessingAPI.equalsIgnoreCase("GHOSTSCRIPT") || imageProcessingAPI
						.equalsIgnoreCase("ITEXT"))) {
			results = "ToolName incorrect or not specified. Please provide either of the following values:IMAGE_MAGICK or GHOSTSCRIPT or ITEXT.";
		} else if (!(pdfOptimizationSwitch.equalsIgnoreCase("ON") || pdfOptimizationSwitch.equalsIgnoreCase("OFF"))) {
			results = "pdfOptimizationSwitch incorrect or not specified. Please provide either of the following ON or OFF.";
		} else if (!(multipageTifSwitch.equalsIgnoreCase("ON") || multipageTifSwitch.equalsIgnoreCase("OFF"))) {
			results = "multipageTifSwitch incorrect or not specified. Please provide either of the following ON or OFF.";
		}
		if (results.isEmpty()) {
			if (imageProcessingAPI.equalsIgnoreCase("GHOSTSCRIPT") && ghostscriptPdfParameters.isEmpty()) {
				results = "Please provide ghostscriptPdfParameters with GHOSTSCRIPT tool.";
			}
			if (pdfOptimizationSwitch.equalsIgnoreCase("on") && !imageProcessingAPI.equalsIgnoreCase("IMAGE_MAGICK")
					&& pdfOptimizationParams.isEmpty()) {
				results = "Please provide pdfOptimizationParams with pdfOptimizationSwitch ON.";
			}
		}
		return results;
	}

	/**
	 * To validate Extract Fuzzy DB API.
	 * @param workingDir {@link String} 
	 * @param hocrFilePath {@link String} 
	 * @param batchClassIdentifier {@link String} 
	 * @param documentType {@link String} 
	 * @return {@link String} 
	 */
	public static String validateExtractFuzzyDBAPI(String workingDir, String hocrFilePath, String batchClassIdentifier,
			String documentType) {
		String results = EMPTY_STRING;
		File hocrFile = new File(workingDir + File.separator + hocrFilePath);
		if (workingDir == null || workingDir.isEmpty()) {
			results = "Please input the valid workingDir.";
		} else if (hocrFilePath == null || hocrFilePath.isEmpty()) {
			results = "Please input the valid hocrFilePath.";
		} else if (batchClassIdentifier == null || batchClassIdentifier.isEmpty()) {
			results = "Please input the valid batchClassIdentifier.";
		} else if (documentType == null || documentType.isEmpty()) {
			results = "Please input the valid documentType.";
		} else if (!hocrFile.exists()) {
			results = "HOCR file as specified in the params is not supplied in valid input. Please specify the correct input params.";
		}
		return results;
	}

	/**
	 * To validate Extract Fixed form API.
	 * @param workingDir {@link String} 
	 * @param projectFile {@link String} 
	 * @param colorSwitch {@link String} 
	 * @return {@link String} 
	 */
	public static String validateExtractFixedFormAPI(String workingDir, String projectFile, String colorSwitch) {
		String results = EMPTY_STRING;
		if (results.isEmpty() && (workingDir == null || workingDir.isEmpty())) {
			results = "Invalid working directory path.";
		} else if (projectFile == null || projectFile.isEmpty() || !projectFile.endsWith(RSP_EXTENSION)) {
			results = "Either invalid value or parameter projectFile not specified.";
		} else if (colorSwitch == null || colorSwitch.isEmpty()) {
			results = "Either invalid value or parameter colorSwitch not specified.";
		} else if (projectFile != null && projectFile != null) {
			File file = new File(workingDir + File.separator + projectFile);
			if (!file.exists()) {
				results = "Unable to found the project file in working dir";
			}
		}
		return results;
	}

	/**
	 * To validate Convert Tiff to Pdf API.
	 * @param pdfGeneratorEngine {@link String} 
	 * @param inputParams {@link String} 
	 * @param outputParams {@link String} 
	 * @return {@link String} 
	 */
	public static String validateConvertTiffToPdfAPI(String pdfGeneratorEngine, String inputParams, String outputParams) {
		String results = EMPTY_STRING;
		if (pdfGeneratorEngine == null || pdfGeneratorEngine.isEmpty()
				|| (!pdfGeneratorEngine.equalsIgnoreCase("IMAGE_MAGICK") && !pdfGeneratorEngine.equalsIgnoreCase("ITEXT"))) {
			results = "Invalid pdf generator engine.";
		}
		return results;
	}

	/**
	 * To validate Extract Regex Fields API.
	 * @param workingDir {@link String} 
	 * @param batchClassIdentifier {@link String} 
	 * @param documentType {@link String} 
	 * @param hocrFileName {@link String} 
	 * @return {@link String} 
	 */
	public static String validateExtractRegexFieldsAPI(final String workingDir, final String batchClassIdentifier,
			final String documentType, final String hocrFileName) {
		String results = EMPTY_STRING;
		if (results.isEmpty() && (workingDir == null || workingDir.isEmpty())) {
			results = "Invalid working directory path.";
		}

		if (results.isEmpty() && (batchClassIdentifier == null || batchClassIdentifier.isEmpty())) {
			results = "Either invalid or null value for batchClassIdentifier...";
		}

		if (results.isEmpty() && (documentType == null || documentType.isEmpty())) {
			results = "Either invalid or null value for documenType...";
		}

		if (results.isEmpty() && (hocrFileName == null || hocrFileName.isEmpty())) {
			results = "Either invalid or null value for hocrFileName...";
		}
		return results;
	}
	
	/**
	 * This api is used to validate the parameters given during uploading a batch through web service.
	 * @param batchClassIdentifier {@link String}
	 * @param batchInstanceName {@link String}
	 * @return {@link String}
	 */
	public static String validateUploadBatchParameters(
			String batchClassIdentifier, String batchInstanceName) {
		String results = EMPTY_STRING;
		if (batchClassIdentifier.isEmpty()) {
			results = "Please enter proper entries for batchClassIdentifier";
		} else if (batchInstanceName.isEmpty()) {
			results = "Please enter a proper value for batchClassName.";
		}
		return results;
	}
	

	/**
	 * To validate Extract from Image API.
	 * @param workingDir {@link String} 
	 * @param ocrTool {@link String} 
	 * @param colorSwitch {@link String} 
	 * @param projectFile {@link String} 
	 * @param cmdLanguage {@link String} 
	 * @param tifFileName {@link String} 
	 * @return {@link String} 
	 */
	public static String validateExtractFromImageAPI(final String workingDir, final String ocrTool, final String colorSwitch,
			final String projectFile, final String cmdLanguage, String tifFileName) {
		String results = EMPTY_STRING;
		if (ocrTool == null || ocrTool.isEmpty()) {
			results = "OCR ENGINE is null or empty. Please provide a valid value.";
		} else if (results.isEmpty() && ocrTool.equalsIgnoreCase(WebServiceUtil.TESSERACT)) {
			if (cmdLanguage == null || cmdLanguage.isEmpty()) {
				results = "Please input the valid cmdLanguage.";
			}
			// cmdLanguage and Tesseract Version expected
		} else if (ocrTool.equalsIgnoreCase(WebServiceUtil.RECOSTAR)) {
			if (projectFile == null || projectFile.isEmpty()) {
				results = "Please input the valid projectFile.";
			} else if (colorSwitch == null || colorSwitch.isEmpty()) {
				results = "Please input the valid colorSwitch.";
			} else if (!new File(projectFile).exists()) {
				results = "RSP file as specified in the params is not supplied in valid input. Please specify the correct input params.";
			}
		} else if (tifFileName == null || tifFileName.isEmpty()) {
			results = "No Image selected. Please make sure an image with an extension tif/tiff/png is attached.";
		} else {
			results = "Please select the valid tool for creating OCR.";
		}
		return results;
	}
}
