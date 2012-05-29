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

package com.ephesoft.dcma.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

public class WebServiceUtil {

	public static final String serverInputFolderName = "in";
	public static final String serverOutputFolderName = "out";
	public static final String EMPTY_STRING = "";
	public static final String DOT = ".";
	public static final String RSP_EXTENSION = ".rsp";
	public static final String ON_STRING = "ON";
	public static final String OFF_STRING = "OFF";
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static int bufferSize = 1024;
	private static final String BACKUP_PROPERTY_FILE = "META-INF" + File.separator + "dcma-util" + File.separator
			+ "dcma-backup-service.properties";
	public static final String DOCUMENTID = "docWS";

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger logger = LoggerFactory.getLogger(WebServiceUtil.class);

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

	public static String createWebServiceWorkingDir(final String parentDir) throws Exception {
		long currentWorkingFolderName = generateRandomFolderName();
		File dir = new File(parentDir + File.separator + currentWorkingFolderName + File.separator + serverInputFolderName);
		boolean createDir = dir.mkdirs();
		if (!createDir) {
			createDir = dir.mkdirs();
			if (!createDir) {
				throw new Exception("Error occured while creating the working dir for web services. Returning from web services.");
			}
		}
		return dir.getAbsolutePath();
	}

	public static long generateRandomFolderName() {
		return System.currentTimeMillis();
	}

	public static String createWebServiceOutputDir(String workingDir) throws Exception {
		File dir = new File(new File(workingDir).getParent() + File.separator + WebServiceUtil.serverOutputFolderName);
		boolean createDir = dir.mkdirs();
		if (!createDir) {
			createDir = dir.mkdirs();
			if (!createDir) {
				throw new Exception("Error occured while creating the working dir for web services. Returning from web services.");
			}
		}
		return dir.getAbsolutePath();
	}

	public static String validateSplitAPI(MultiValueMap<String, MultipartFile> fileMap, boolean isGSTool, String outputParams, String inputParams) {
		String results = EMPTY_STRING;
		if(fileMap.size() == 0) {
			results = "No files provided to split";
		} else if (isGSTool && inputParams.isEmpty()) {
			results = "Input Params expected with GhostScript tool flag. Please set the input params.";
		}
		return results;
	}

	public static String validateExportBatchClassAPI(String imBaseFolderName, String identifier, String searchSampleName) {
		String results = EMPTY_STRING;
		if (imBaseFolderName.isEmpty() || identifier.isEmpty() || searchSampleName.isEmpty()) {
			results = "Either of the input params are incomplete. Please set the input params.";
		}
		return results;
	}

	public static String validateSearchableAPI(String outputPDFFileName, String projectFile, String pdfFileType,
			String checkSearchableImage, String checkColorImage) {
		String results = EMPTY_STRING;
		if (outputPDFFileName == null || outputPDFFileName.isEmpty() || !outputPDFFileName.endsWith(pdfFileType)) {
			results = "Invalid name for pdf file name.";
		}
		if (checkSearchableImage == null || checkSearchableImage.isEmpty()) {
			results = "Either invalid value or parameter checkSearchableImage not specified.";
		}
		if (checkColorImage == null || checkColorImage.isEmpty()) {
			results = "Either invalid value or parameter checkColorImage not specified.";
		}
		if (projectFile == null || projectFile.isEmpty() || !projectFile.endsWith(WebServiceUtil.RSP_EXTENSION)) {
			results = "Invalid name for projectFile file name.";
		}
		return results;
	}


	public static String validateCreateOCRAPI(String workingDir, String tool, String colorSwitch, String projectFile,
			String tesseractVersion, String cmdLanguage) {
		String results = EMPTY_STRING;
		File rspFile = new File(workingDir + File.separator + projectFile);
		if (tool == null || tool.isEmpty()) {
			results = "Please select the tool for creating OCR.";
		}
		if (tool.equalsIgnoreCase("tesseract")) {
			if (tesseractVersion == null || tesseractVersion.isEmpty() || cmdLanguage == null || cmdLanguage.isEmpty()) {
				results = "Either of the input params are incomplete. Please set the input params.";
			}
			// cmdLanguage and Tesseract Version expected
		} else if (tool.equalsIgnoreCase("recostar")) {
			if (projectFile == null || projectFile.isEmpty() || colorSwitch == null || colorSwitch.isEmpty()) {
				results = "Either of the input params are incomplete. Please set the input params.";
			} else if (!rspFile.exists()) {
				results = "RSP file as specified in the params is not supplied in valid input. Please specify the correct input params.";
			}
		} else {
			results = "Please select the valid tool for creating OCR.";
		}
		return results;
	}


	
	public static String validateCreateMultiPageFile(String ghostscriptPdfParameters, String imageProcessingAPI, String pdfOptimizationSwitch, String multipageTifSwitch, String pdfOptimizationParams) {
		String results = EMPTY_STRING;
		if(imageProcessingAPI.isEmpty() || !(imageProcessingAPI.equalsIgnoreCase("IMAGE_MAGICK") || imageProcessingAPI.equalsIgnoreCase("GHOSTSCRIPT") || 
				imageProcessingAPI.equalsIgnoreCase("ITEXT"))) {
			results = "ToolName incorrect or not specified. Please provide either of the following values:IMAGE_MAGICK or GHOSTSCRIPT or ITEXT.";
		} else if(!(pdfOptimizationSwitch.equalsIgnoreCase("ON") || pdfOptimizationSwitch.equalsIgnoreCase("OFF"))){
			results = "pdfOptimizationSwitch incorrect or not specified. Please provide either of the following ON or OFF.";
		} else if(!(multipageTifSwitch.equalsIgnoreCase("ON") || multipageTifSwitch.equalsIgnoreCase("OFF"))){
			results = "multipageTifSwitch incorrect or not specified. Please provide either of the following ON or OFF.";
		}
		if(results.isEmpty()) {
			if(imageProcessingAPI.equalsIgnoreCase("GHOSTSCRIPT")) {
				if(ghostscriptPdfParameters.isEmpty()) {
					results = "Please provide ghostscriptPdfParameters with GHOSTSCRIPT tool.";
				}
			}
			if(pdfOptimizationSwitch.equalsIgnoreCase("on") && !imageProcessingAPI.equalsIgnoreCase("IMAGE_MAGICK") && pdfOptimizationParams.isEmpty()) {
				results = "Please provide pdfOptimizationParams with pdfOptimizationSwitch ON.";
			}
		}
		return results;
	}
	
		public static String validateExtractFuzzyDBAPI(String workingDir, String hocrFilePath, String batchClassIdentifier,
			String documentType) {
		String results = EMPTY_STRING;
		File hocrFile = new File(workingDir + File.separator + hocrFilePath);
		if (workingDir == null || workingDir.isEmpty() || hocrFilePath == null || hocrFilePath.isEmpty()
				|| batchClassIdentifier == null || batchClassIdentifier.isEmpty() || !hocrFile.exists() || documentType == null
				|| documentType.isEmpty()) {
			results = "Either of the input params are incomplete. Please set the input params.";
		}
		return results;
	}

	public static String validateExtractFixedFormAPI(String workingDir, String projectFile, String colorSwitch) {
		String results = EMPTY_STRING;
		if (results.isEmpty() && (workingDir == null || workingDir.isEmpty())) {
			results = "Invalid working directory path.";
		}

		if (results.isEmpty() && (projectFile == null || projectFile.isEmpty() || !projectFile.endsWith(RSP_EXTENSION))) {
			results = "Either invalid value or parameter projectFile not specified.";
		}

		if (results.isEmpty() && (colorSwitch == null || colorSwitch.isEmpty())) {
			results = "Either invalid value or parameter colorSwitch not specified.";
		}

		if (results.isEmpty() && (projectFile != null && projectFile != null)) {
			File file = new File(workingDir + File.separator + projectFile);
			if (!file.exists()) {
				results = "Unable to found the project file in working dir";
			}
		}
		return results;
	}

	public static String validateConvertTiffToPdfAPI(String pdfGeneratorEngine, String inputParams, String outputParams) {
		String results = EMPTY_STRING;
		if (pdfGeneratorEngine == null || pdfGeneratorEngine.isEmpty()
				|| (!pdfGeneratorEngine.equalsIgnoreCase("IMAGE_MAGICK") && !pdfGeneratorEngine.equalsIgnoreCase("ITEXT"))) {
			results = "Invalid pdf generator engine.";
		}
		return results;
	}
}
