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

package com.ephesoft.dcma.ocr;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.ocr.constants.OcropusConstants;
import com.ephesoft.dcma.util.FileNameFormatter;

/**
 * This class reads the image files from batch xml, generates HOCR file for each one of them and writes the name of each HOCR file in
 * build.xml.It uses Ocropus plugin for generating HOCR files from images.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.component.ICommonConstants
 * 
 */
@Component
public class OcrReader implements ICommonConstants {

	/**
	 * OCROPUS_PLUGIN, constant String.
	 */
	private static final String OCROPUS_PLUGIN = "OCROPUS";

	/**
	 * Logger instance for logging using slf4j.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(OcrReader.class);

	/**
	 * Default size of commands array.
	 */
	private static final int CMD_ARRAY_SIZE = 3;

	/**
	 * Reference of batchSchemaService.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Instance of PluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * List of all ocropus commands separated by ";".
	 */
	private String mandatorycmds;

	/**
	 * Instance of {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * To set Mandatorycmds.
	 * @param mandatorycmds String
	 */
	public void setMandatorycmds(final String mandatorycmds) {
		this.mandatorycmds = mandatorycmds;
	}

	/**
	 * To get Batch Schema Service.
	 * @return the batchSchemaService
	 */
	public BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * To set Batch Schema Service.
	 * @param batchSchemaService {@link BatchSchemaService}
	 */
	public void setBatchSchemaService(final BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * To get Plugin Properties Service.
	 * @return the pluginPropertiesService
	 */
	public PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	/**
	 * To set Plugin Properties Service.
	 * @param pluginPropertiesService {@link PluginPropertiesService}
	 */
	public void setPluginPropertiesService(final PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	/**
	 * This method populates the list of commands from ";" seperated String of commands.
	 */
	public List<String> populateCommandsList() {
		List<String> cmdList = new ArrayList<String>();
		StringTokenizer token = new StringTokenizer(mandatorycmds, OcropusConstants.SEMI_COLON);
		while (token.hasMoreTokens()) {
			String eachCmd = token.nextToken();
			cmdList.add(eachCmd);
		}
		return cmdList;
	}

	/**
	 * This method calls the Ocropus plugin using java.lang.Runtime(). If command exits with a status of "0", then the execution was
	 * successful.A status less than 0 means that image cannot be read using this plugin.
	 * 
	 * @param batch {@link Batch}
	 * @param fileName String
	 * @param batchInstanceId String
	 * @param cmdList List<String>
	 * @param actualFolderLocation String
	 * @throws DCMAApplicationException if exception occurs while generating HOCR for image
	 */
	public String process(final String fileName, final Batch batch, final String batchInstanceId, final List<String> cmdList,
			final String actualFolderLocation) throws DCMAApplicationException {
		if (fileName.contains(OcropusConstants.SPACE)) {
			LOGGER.error(" Space found in the name of image:" + fileName + ".So it acnnot be processed");
			throw new DCMAApplicationException(" Space found in the name of image:" + fileName + ".So it acnnot be processed");
		}
		// process each image file to generate HOCR files
		String targetDirectoryName = fileName.substring(0, fileName.indexOf(".tif"));
		String targetHOCR = OcropusConstants.EMPTY;
		String oldFileName = OcropusConstants.EMPTY;
		String ocrInputFileName = OcropusConstants.EMPTY;
		String pageId = OcropusConstants.EMPTY;
		List<Document> xmlDocuments = batch.getDocuments().getDocument();
		for (int i = OcropusConstants.ZERO; i < xmlDocuments.size(); i++) {
			Document document = (Document) xmlDocuments.get(i);
			List<Page> listOfPages = document.getPages().getPage();
			for (int j = OcropusConstants.ZERO; j < listOfPages.size(); j++) {
				Page page = listOfPages.get(j);
				String sImageFile = page.getNewFileName();
				if (fileName.equalsIgnoreCase(sImageFile)) {
					oldFileName = page.getOldFileName();
					ocrInputFileName = page.getOCRInputFileName();
					pageId = page.getIdentifier().toString();
				}
			}
		}
		try {
			FileNameFormatter fileFormatter = new FileNameFormatter();
			targetHOCR = fileFormatter.getHocrFileName(batchInstanceId, oldFileName, fileName, ocrInputFileName, ".html", pageId,
					false);
		} catch (Exception e1) {
			LOGGER.error("Exception retrieving the name of HOCR file" + e1.getMessage(), e1);
			throw new DCMAApplicationException("Exception retrieving the name of HOCR file" + e1.getMessage(), e1);
		}
		String targetPNG = "tempOCR" + fileName.substring(0, fileName.indexOf(".tif")) + ".png";

		LOGGER.info("image file name is: " + fileName);
		LOGGER.info("Target directory name is: " + targetDirectoryName);
		LOGGER.info("Target HOCR name is: " + targetHOCR);
		LOGGER.info("Target PNG name is: " + targetPNG);
		try {
			Runtime runtime = Runtime.getRuntime();
			for (int i = OcropusConstants.ZERO; i < cmdList.size(); i++) {
				String[] cmds = new String[CMD_ARRAY_SIZE];
				cmds[OcropusConstants.ZERO] = "sh";
				cmds[OcropusConstants.ONE] = "-c";
				if (cmdList.get(i).contains("convert")) {
					LOGGER.debug("inside convert");
					cmds[OcropusConstants.TWO] = cmdList.get(i) + " \"" + actualFolderLocation + File.separator + fileName + "\" \""
							+ actualFolderLocation + File.separator + targetPNG + "\"";
				} else if (cmdList.get(i).contains("export")) {
					LOGGER.debug("inside export");
					cmds[OcropusConstants.TWO] = cmdList.get(i);
				} else if (cmdList.get(i).contains("ocroscript")) {
					LOGGER.debug("inside ocroscript");
					cmds[OcropusConstants.TWO] = cmdList.get(i) + " \"" + actualFolderLocation + File.separator + targetPNG + "\" > \""
							+ actualFolderLocation + File.separator + targetHOCR + "\"";
				} else if (cmdList.get(i).contains("rm")) {
					LOGGER.debug("inside rm");
					cmds[OcropusConstants.TWO] = cmdList.get(i) + " \"" + actualFolderLocation + File.separator + targetPNG + "\"";
				}
				LOGGER.info("command formed is :" + cmds[OcropusConstants.TWO]);
				Process process = runtime.exec(cmds);

				InputStreamReader inputStreamReader = null;
				BufferedReader input = null;
				try {
					inputStreamReader = new InputStreamReader(process.getInputStream());
					input = new BufferedReader(inputStreamReader);
					String line = null;
					do {
						line = input.readLine();
						LOGGER.debug(line);
					} while (line != null);
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				} finally {
					if (input != null) {
						try {
							input.close();
						} catch (IOException e) {
							LOGGER.error(e.getMessage(), e);
						}
					}
					if (inputStreamReader != null) {
						try {
							inputStreamReader.close();
						} catch (IOException e) {
							LOGGER.error(e.getMessage(), e);
						}
					}
				}
				LOGGER.info("Command " + i + " exited with error code no " + process.exitValue());
			}
		} catch (Exception e) {
			LOGGER.error("Exception while generating HOCRfor image" + fileName + e.getMessage());
			throw new DCMAApplicationException("Exception while generating HOCR for image" + fileName + e.getMessage(), e);
		}
		return targetHOCR;
	}

	/**
	 * This method generates HOCR files for each image file and updates the batch xml with each HOCR file name.
	 * 
	 * @param batchInstanceID String
	 * @return boolean
	 * @throws DCMAApplicationException if exception occurs while reading from XML
	 * @throws DCMABusinessException if file has invalid extension
	 */
	public boolean readOCR(final String batchInstanceID) throws DCMAApplicationException {
		LOGGER.info("Started Processing image at " + new Date());

		LOGGER.info("Initializing properties...");
		String validExt = pluginPropertiesService.getPropertyValue(batchInstanceID, OCROPUS_PLUGIN, OCRProperties.OCROPUS_VALID_EXTNS);
		LOGGER.info("Properties Initialized Successfully");

		String[] validExtensions = validExt.split(OcropusConstants.SEMI_COLON);

		String actualFolderLocation = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceID) + File.separator
				+ batchInstanceID;
		List<String> cmdList = populateCommandsList();

		Batch batch = batchSchemaService.getBatch(batchInstanceID);

		List<String> allPages = null;
		try {
			allPages = findAllPagesFromXML(batch);
		} catch (DCMAApplicationException e1) {
			LOGGER.error("Exception while reading from XML" + e1.getMessage());
			throw new DCMAApplicationException(e1.getMessage(), e1);
		}

		if (!allPages.isEmpty()) {
			for (int i = OcropusConstants.ZERO; i < allPages.size(); i++) {
				String eachPage = allPages.get(i);
				eachPage = eachPage.trim();
				boolean isFileValid = false;
				if (validExtensions != null && validExtensions.length > OcropusConstants.ZERO) {
					for (int l = OcropusConstants.ZERO; l < validExtensions.length; l++) {
						if (eachPage.endsWith(validExtensions[l])) {
							isFileValid = true;
							break;
						}
					}
				} else {
					LOGGER.error("No valid extensions are specified in resources");
					throw new DCMAApplicationException("No valid extensions are specified in resources");
				}
				if (isFileValid) {
					try {
						LOGGER.info("Rendering control to Ocropus OCR engine");
						String targetHOCR = process(eachPage, batch, String.valueOf(batchInstanceID), cmdList, actualFolderLocation);
						LOGGER.info("Control back from Ocropus OCR engine");
						LOGGER.info("Started Updating batch XML");
						updateBatchXML(eachPage, targetHOCR, batch);
						LOGGER.info("Finished Updating batch XML");
					} catch (DCMAApplicationException e) {
						LOGGER.error("Image Processing or XML updation failed for image: " + actualFolderLocation + File.separator
								+ eachPage);
						throw new DCMAApplicationException(e.getMessage(), e);
					}
				} else {
					LOGGER.error("File " + eachPage + " has invalid extension.");
					throw new DCMABusinessException("File " + eachPage + " has invalid extension.");
				}
			}
		} else {
			LOGGER.error("No pages found in batch XML.");
			throw new DCMAApplicationException("No pages found in batch XML.");
		}
		batchSchemaService.updateBatch(batch);
		LOGGER.info("Batch XML updated.");
		return true;
	}

	/**
	 * This method finds all the processable pages from batch.xml using new file name.
	 * 
	 * @param batch Batch
	 * @return List<String>
	 * @throws DCMAApplicationException if error occurs
	 */
	public List<String> findAllPagesFromXML(final Batch batch) throws DCMAApplicationException {
		List<String> allPages = new ArrayList<String>();

		List<Document> xmlDocuments = batch.getDocuments().getDocument();
		for (int i = OcropusConstants.ZERO; i < xmlDocuments.size(); i++) {
			Document document = (Document) xmlDocuments.get(i);
			List<Page> listOfPages = document.getPages().getPage();
			for (int j = OcropusConstants.ZERO; j < listOfPages.size(); j++) {
				Page page = listOfPages.get(j);
				String sImageFile = page.getNewFileName();
				if (sImageFile != null && sImageFile.length() > OcropusConstants.ZERO) {
					allPages.add(sImageFile);
				}
			}
		}
		return allPages;
	}

	/**
	 * This method updates the batch.xml for each iamge file processed using ocropus plugin.
	 * 
	 * @param fileName String
	 * @param targetHOCR String 
	 * @param batch Batch
	 * @throws DCMAApplicationException if error occurs
	 */
	public void updateBatchXML(final String fileName, final String targetHOCR, final Batch batch) throws DCMAApplicationException {

		List<Document> xmlDocuments = batch.getDocuments().getDocument();
		for (int i = OcropusConstants.ZERO; i < xmlDocuments.size(); i++) {
			Document document = (Document) xmlDocuments.get(i);
			List<Page> listOfPages = document.getPages().getPage();
			for (int j = OcropusConstants.ZERO; j < listOfPages.size(); j++) {
				Page page = listOfPages.get(j);
				String sImageFile = page.getNewFileName();
				if (fileName.equalsIgnoreCase(sImageFile)) {
					page.setHocrFileName(targetHOCR);
				}
			}
		}
	}
}
