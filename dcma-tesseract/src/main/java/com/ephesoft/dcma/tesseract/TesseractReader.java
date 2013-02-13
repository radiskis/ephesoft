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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.ephesoft.dcma.core.TesseractVersionProperty;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.tesseract.constant.TesseractConstants;
import com.ephesoft.dcma.util.CustomFileFilter;
import com.ephesoft.dcma.util.FileUtils;

/**
 * This class reads the image files from batch xml, generates HOCR file for each one of them and writes the name of each HOCR file in
 * build.xml.It uses Tesseract plugin for generating HOCR files which can accept images in three languages : English, French and
 * Spanish.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.component.ICommonConstants
 * @see com.ephesoft.dcma.batch.service.BatchSchemaService
 * 
 */
@Component
public class TesseractReader implements ICommonConstants {

	/**
	 * Instance of {@link BatchSchemaService}.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Instance of {@link PluginPropertiesService}.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * Logger instance for logging using slf4j.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(TesseractReader.class);

	/**
	 * Instance of {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * To get Batch Schema Service.
	 * 
	 * @return the batchSchemaService
	 */
	public BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * To set Batch Schema Service.
	 * 
	 * @param batchSchemaService BatchSchemaService
	 */
	public void setBatchSchemaService(final BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * To get Plugin Properties Service.
	 * 
	 * @return the pluginPropertiesService
	 */
	public PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	/**
	 * To set Plugin Properties Service.
	 * 
	 * @param pluginPropertiesService PluginPropertiesService
	 */
	public void setPluginPropertiesService(final PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	/**
	 * Tesseract command for windows.
	 */
	private transient String windowsCmd;

	/**
	 * Tesseract command for unix/linux support.
	 */
	private transient String unixCmd;

	/**
	 * Tesseract command for overwriteHOCR.
	 */
	private transient String overwriteHOCR;

	/**
	 * Tesseract command parameters.
	 */
	private transient String cmdParams;

	/**
	 * hocr folder name.
	 */
	private transient String defaultHocrfileFolder;

	/**
	 * hocr file name for error handling.
	 */
	private transient String defaultHocrfileName;

	/**
	 * To get Default Hocr file name.
	 * 
	 * @return String
	 */
	public String getDefaultHocrfileName() {
		return defaultHocrfileName;
	}

	/**
	 * To set Default Hocr file name.
	 * 
	 * @param defaultHocrfileName String
	 */
	public void setDefaultHocrfileName(String defaultHocrfileName) {
		this.defaultHocrfileName = defaultHocrfileName;
	}

	/**
	 * To set Default Hocr file folder.
	 * 
	 * @param defaultHocrfileFolder String
	 */
	public void setDefaultHocrfileFolder(String defaultHocrfileFolder) {
		this.defaultHocrfileFolder = defaultHocrfileFolder;
	}

	/**
	 * To get Default Hocr file folder.
	 * 
	 * @return String
	 */
	public String getDefaultHocrfileFolder() {
		return defaultHocrfileFolder;
	}

	/**
	 * To get Cmd Params.
	 * 
	 * @return String
	 */
	public String getCmdParams() {
		return cmdParams;
	}

	/**
	 * To set Cmd Params.
	 * 
	 * @param cmdParams String
	 */
	public void setCmdParams(String cmdParams) {
		this.cmdParams = cmdParams;
	}

	/**
	 * To get Overwrite HOCR.
	 * 
	 * @return the overwriteHOCR
	 */
	public String getOverwriteHOCR() {
		return overwriteHOCR;
	}

	/**
	 * To set Overwrite HOCR.
	 * 
	 * @param overwriteHOCR String
	 */
	public void setOverwriteHOCR(String overwriteHOCR) {
		this.overwriteHOCR = overwriteHOCR;
	}

	/**
	 * To set Windows Cmd.
	 * 
	 * @param windowsCmd String
	 */
	public void setWindowsCmd(String windowsCmd) {
		this.windowsCmd = windowsCmd;
	}

	/**
	 * To set Unix Cmd.
	 * 
	 * @param unixCmd String
	 */
	public void setUnixCmd(String unixCmd) {
		this.unixCmd = unixCmd;
	}

	/**
	 * This method cleans up all the intermediate PNG files which are formed by tesseract plugin in process of creation of HOCR files.
	 * 
	 * @param folderName String
	 */
	public void cleanUpIntrmediatePngs(final String folderName) {
		File file = new File(folderName);
		if (file.isDirectory()) {
			File[] allFiles = file.listFiles();
			for (int i = 0; i < allFiles.length; i++) {
				if (allFiles[i].getName().toLowerCase().contains(TesseractConstants.TIF_FILE_EXT)
						&& allFiles[i].getName().toLowerCase().contains(TesseractConstants.PNG_FILE_EXT)) {
					allFiles[i].delete();
				}
			}
		}
	}

	/**
	 * This method generates HOCR files for each image file and updates the batch xml with each HOCR file name.
	 * 
	 * @param batchInstanceID String
	 * @param pluginName String
	 * @throws DCMAApplicationException in case of error
	 */
	public void readOCR(final String batchInstanceID, String pluginName) throws DCMAApplicationException {
		String tesseractSwitch = pluginPropertiesService.getPropertyValue(batchInstanceID, TesseractConstants.TESSERACT_HOCR_PLUGIN,
				TesseractProperties.TESSERACT_SWITCH);
		if ("ON".equalsIgnoreCase(tesseractSwitch)) {
			LOGGER.info("Started Processing image at " + new Date());
			String actualFolderLocation = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceID) + File.separator
					+ batchInstanceID;
			// Initialize properties
			LOGGER.info("Initializing properties...");
			String validExt = pluginPropertiesService.getPropertyValue(batchInstanceID, TesseractConstants.TESSERACT_HOCR_PLUGIN,
					TesseractProperties.TESSERACT_VALID_EXTNS);
			String cmdLanguage = pluginPropertiesService.getPropertyValue(batchInstanceID, TesseractConstants.TESSERACT_HOCR_PLUGIN,
					TesseractProperties.TESSERACT_LANGUAGE);
			String tesseractVersion = pluginPropertiesService.getPropertyValue(batchInstanceID,
					TesseractConstants.TESSERACT_HOCR_PLUGIN, TesseractVersionProperty.TESSERACT_VERSIONS);
			String colorSwitch = pluginPropertiesService.getPropertyValue(batchInstanceID, TesseractConstants.TESSERACT_HOCR_PLUGIN,
					TesseractProperties.TESSERACT_COLOR_SWITCH);
			LOGGER.info("Properties Initialized Successfully");

			String[] validExtensions = validExt.split(TesseractConstants.SEMI_COLON);
			Batch batch = batchSchemaService.getBatch(batchInstanceID);
			List<String> allPages = null;
			try {
				allPages = findAllPagesFromXML(batch, colorSwitch);
			} catch (DCMAApplicationException e1) {
				LOGGER.error("Exception while reading from XML" + e1.getMessage());
				throw new DCMAApplicationException(e1.getMessage(), e1);
			}
			BatchInstanceThread batchInstanceThread = new BatchInstanceThread(batchInstanceID);
			String defaultHocrFilePath = batchSchemaService.getBaseFolderLocation() + File.separator + defaultHocrfileFolder;
			List<TesseractProcessExecutor> tesseractProcessExecutors = new ArrayList<TesseractProcessExecutor>();
			if (!allPages.isEmpty()) {
				for (String eachPage : allPages) {
					eachPage = eachPage.trim();
					boolean isFileValid = false;
					if (validExtensions != null && validExtensions.length > 0) {
						for (int l = 0; l < validExtensions.length; l++) {
							if (eachPage.substring(eachPage.indexOf(TesseractConstants.DOT) + 1).equalsIgnoreCase(validExtensions[l])) {
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
							LOGGER.info("Adding to thread pool");
							tesseractProcessExecutors.add(new TesseractProcessExecutor(eachPage, batch, batchInstanceID,
									actualFolderLocation, cmdLanguage, batchInstanceThread, tesseractVersion, colorSwitch, windowsCmd,
									unixCmd, overwriteHOCR, cmdParams));
						} catch (DCMAApplicationException e) {
							LOGGER.error("Image Processing or XML updation failed for image: " + actualFolderLocation + File.separator
									+ eachPage);
							throw new DCMAApplicationException(e.getMessage(), e);
						}
					} else {
						LOGGER.debug("File " + eachPage + " has invalid extension.");
						throw new DCMABusinessException("File " + eachPage + " has invalid extension.");
					}
				}
				try {
					LOGGER.info("Started execution using thread pool");
					batchInstanceThread.execute();
					LOGGER.info("Finished execution using thread pool");
				} catch (DCMAApplicationException dcmae) {
					LOGGER.error("Error in tesseract reader executor using threadpool" + dcmae.getMessage(), dcmae);
					batchInstanceThread.remove();
					batchSchemaService.updateBatch(batch);
					// Throw the exception to set the batch status to Error by Application aspect
					throw new DCMAApplicationException(dcmae.getMessage(), dcmae);
				}
				for (TesseractProcessExecutor tesseractProcessExecutor : tesseractProcessExecutors) {
					LOGGER.info("Started Updating batch XML");
					updateBatchXML(tesseractProcessExecutor.getFileName(), tesseractProcessExecutor.getTargetHOCR(), batch,
							colorSwitch);
					LOGGER.info("Finished Updating batch XML");
					LOGGER.info("Cleaning up intermediate PNG files");
				}
				cleanUpIntrmediatePngs(actualFolderLocation);
			} else {
				LOGGER.error("No pages found in batch XML.");
				throw new DCMAApplicationException("No pages found in batch XML.");
			}
			// Start Changes for :Error handling for those cases where html file is not formed and batch proceeds further
			File fPageFolder = new File(actualFolderLocation);
			String[] listOfHtmlFiles = fPageFolder.list(new CustomFileFilter(false, FileType.HTML.getExtensionWithDot()));

			for (TesseractProcessExecutor tesseractProcessExecutor : tesseractProcessExecutors) {
				validateTesseractProcessExecutor(actualFolderLocation, defaultHocrFilePath, listOfHtmlFiles, tesseractProcessExecutor);
			}
			LOGGER.info("Started Updating batch XML");
			batchSchemaService.updateBatch(batch);
			LOGGER.info("Finished Updating batch XML");
			LOGGER.info("Processing finished at " + new Date());
		} else {
			LOGGER.info("Skipping tesseract plgugin. Switch set as OFF");
		}
	}

	private void validateTesseractProcessExecutor(String actualFolderLocation, String defaultHocrFilePath, String[] listOfHtmlFiles,
			TesseractProcessExecutor tesseractProcessExecutor) throws DCMAApplicationException {
		boolean isHOCRCreated = checkHocrExists(tesseractProcessExecutor.getTargetHOCR(), listOfHtmlFiles);

		if (!isHOCRCreated) {
			LOGGER.info("HOCR file is not generated by tesseract.Target hocr file name " + tesseractProcessExecutor.getTargetHOCR()
					+ ".Trying to copy the template hocr file.");
			try {
				// Error handling for those cases where html file is not formed and batch proceeds further
				FileUtils.copyFile(new File(defaultHocrFilePath + File.separator + defaultHocrfileName), new File(actualFolderLocation
						+ File.separator + tesseractProcessExecutor.getTargetHOCR()));
			} catch (Exception e1) {
				LOGGER.error("Exception copying the HOCR file" + e1.getMessage(), e1);
				throw new DCMAApplicationException("Exception copying the HOCR file" + e1.getMessage(), e1);
			}
		} else {
			LOGGER.info("HOCR file is generated successfully by tesseract.Target hocr file name "
					+ tesseractProcessExecutor.getTargetHOCR() + ".Skipping to copy the template hocr file.");
		}
		// END Changes for :Error handling for those cases where html file is not formed and batch proceeds further
	}

	/**
	 * This method finds all the processable pages from batch.xml using new file name.
	 * 
	 * @param batch Batch
	 * @param colorSwitch String
	 * @return List<String>
	 * @throws DCMAApplicationException in case of error
	 */
	public List<String> findAllPagesFromXML(final Batch batch, final String colorSwitch) throws DCMAApplicationException {
		List<String> allPages = new ArrayList<String>();
		List<Document> xmlDocuments = batch.getDocuments().getDocument();
		for (int i = 0; i < xmlDocuments.size(); i++) {
			Document document = (Document) xmlDocuments.get(i);
			List<Page> listOfPages = document.getPages().getPage();
			for (int j = 0; j < listOfPages.size(); j++) {
				Page page = listOfPages.get(j);
				String sImageFile;
				if (TesseractConstants.ON.equals(colorSwitch)) {
					sImageFile = page.getOCRInputFileName();
				} else {
					sImageFile = page.getNewFileName();
				}
				if (sImageFile != null && sImageFile.length() > 0) {
					allPages.add(sImageFile);
				}
			}
		}
		return allPages;
	}

	/**
	 * This method updates the batch.xml for each image file processed using tesseract engine.
	 * 
	 * @param fileName String
	 * @param targetHOCR String
	 * @param batch Batch
	 * @param colorSwitch String
	 * @throws DCMAApplicationException in case of error
	 */
	public void updateBatchXML(final String fileName, final String targetHOCR, final Batch batch, final String colorSwitch)
			throws DCMAApplicationException {

		List<Document> xmlDocuments = batch.getDocuments().getDocument();
		for (int i = 0; i < xmlDocuments.size(); i++) {
			Document document = (Document) xmlDocuments.get(i);
			List<Page> listOfPages = document.getPages().getPage();
			for (int j = 0; j < listOfPages.size(); j++) {
				Page page = listOfPages.get(j);
				String sImageFile;
				if (TesseractConstants.ON.equals(colorSwitch)) {
					sImageFile = page.getOCRInputFileName();
				} else {
					sImageFile = page.getNewFileName();
				}
				if (fileName.equalsIgnoreCase(sImageFile)) {
					page.setHocrFileName(targetHOCR);
				}
			}
		}
	}

	private boolean checkHocrExists(String hocrName, final String[] listOfFiles) {
		boolean returnValue = false;
		String localHOCRFileName = hocrName;
		if (listOfFiles != null && listOfFiles.length > 0 && localHOCRFileName != null) {
			for (String eachFile : listOfFiles) {
				if (eachFile.equalsIgnoreCase(localHOCRFileName)) {
					returnValue = true;
				}
			}
		}
		return returnValue;
	}

	/**
	 * This method is generates the HOCR for given image name and generates the output result at the output folder location.
	 * 
	 * @param actualFolderLocation {@link String}
	 * @param colorSwitch {@link String}
	 * @param imageName {@link String}
	 * @param batchInstanceThread {@link BatchInstanceThread}
	 * @param outputFolderLocation {@link String}
	 * @param cmdLanguage {@link String}
	 * @param tesseractVersion {@link String}
	 * @throws DCMAApplicationException in case of error
	 */
	public void createOCR(final String actualFolderLocation, String colorSwitch, String imageName,
			BatchInstanceThread batchInstanceThread, String outputFolderLocation, String cmdLanguage, String tesseractVersion)
			throws DCMAApplicationException {
		LOGGER.info("Started processing of image \"" + imageName + "\"");

		List<TesseractProcessExecutor> tesseractProcessExecutors = new ArrayList<TesseractProcessExecutor>();
		try {
			LOGGER.info("Adding to thread pool");
			tesseractProcessExecutors.add(new TesseractProcessExecutor(imageName, actualFolderLocation, cmdLanguage,
					batchInstanceThread, tesseractVersion, colorSwitch, windowsCmd, unixCmd, cmdParams, outputFolderLocation));
		} catch (DCMAApplicationException e) {
			LOGGER.error("Image Processing or XML updation failed for image: " + actualFolderLocation + File.separator + imageName);
			throw new DCMAApplicationException(e.getMessage(), e);
		}
		LOGGER.info("Processing finished of image \"" + imageName + "\" at " + new Date());
	}
}
