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

package com.ephesoft.dcma.export;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Batch.Documents;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.service.BatchInstanceService;

/**
 * This class is used to copy the batch folder files to export folder and update batch.xml.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.export.service.ExportServiceImpl
 */
@Component
public class FolderExporter implements ICommonConstants {
	/**
	 * Copy batch xml plug in name.
	 */
	private static final String COPY_BATCH_XML_PLUGIN = "COPY_BATCH_XML";
	/**
	 * An instance of Logger for proper logging using slf4j.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(FolderExporter.class);
	/**
	 * String to store invalid characters.
	 */
	private String invalidChars;
	/**
	 * String to store the characters to be replaced for invalid characters.
	 */
	private String replaceChar;
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
	 * Instance of {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * @return {@link BatchSchemaService} the batchSchemaService.
	 */
	public BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * @param batchSchemaService {@link BatchSchemaService} the batchSchemaService to set.
	 */
	public void setBatchSchemaService(BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * @return {@link PluginPropertiesService} the pluginPropertiesService.
	 */
	public PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	/**
	 * @param pluginPropertiesService {@link PluginPropertiesService} the pluginPropertiesService to set.
	 */
	public void setPluginPropertiesService(PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	/**
	 * @return {@link String} the invalid characters list.
	 */
	public String getInvalidChars() {
		return invalidChars;
	}

	/**
	 * @param invalidChars {@link String} invalid characters list to set.
	 */
	public void setInvalidChars(String invalidChars) {
		this.invalidChars = invalidChars;
	}

	/**
	 * @return {@link String} the replace character for invalid chars.
	 */
	public String getReplaceChar() {
		return replaceChar;
	}

	/**
	 * @param replaceChar {@link String} replace character to be set.
	 */
	public void setReplaceChar(String replaceChar) {
		this.replaceChar = replaceChar;
	}

	/**
	 * This method simply reads the batch.xml file in the sFolderToBeExported. It finds the names of multipage tif and pdf files from
	 * the batch.xml. Then it moves these files to the export to folder.
	 * 
	 * @param batchInstanceID {@link String}
	 * @throws JAXBException root exception class for all JAXB exceptions
	 * @throws DCMAApplicationException if any exception occurs.
	 */
	public void exportFiles(String batchInstanceID) throws JAXBException, DCMAApplicationException {
		String exportToFolderSwitch = pluginPropertiesService.getPropertyValue(batchInstanceID, COPY_BATCH_XML_PLUGIN,
				ExportProperties.EXPORT_TO_FOLDER_SWITCH);
		LOGGER.info("Export to folder switch: " + exportToFolderSwitch);
		boolean isZipSwitchOn = batchSchemaService.isZipSwitchOn();
		LOGGER.info("Zipped Batch XML switch is:" + isZipSwitchOn);
		if (IExportCommonConstants.ON_SWITCH.equalsIgnoreCase(exportToFolderSwitch)) {
			// Initialize properties
			LOGGER.info("Initializing properties...");
			String sFolderToBeExported = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceID) + File.separator
					+ batchInstanceID;
			String exportToFolder = pluginPropertiesService.getPropertyValue(batchInstanceID, COPY_BATCH_XML_PLUGIN,
					ExportProperties.EXPORT_FOLDER);
			String folderName = pluginPropertiesService.getPropertyValue(batchInstanceID, COPY_BATCH_XML_PLUGIN,
					ExportProperties.FOLDER_NAME);
			String fileName = pluginPropertiesService.getPropertyValue(batchInstanceID, COPY_BATCH_XML_PLUGIN,
					ExportProperties.FILE_NAME);
			String batchXmlFolderName = pluginPropertiesService.getPropertyValue(batchInstanceID, COPY_BATCH_XML_PLUGIN,
					ExportProperties.BATCH_XML_EXPORT_FOLDER);
			LOGGER.info("Properties Initialized Successfully");
			if (sFolderToBeExported == null) {
				LOGGER.error("No export folder specified ......");
				throw new DCMAApplicationException("No export folder specified ......");
			}
			File fFolderToBeExported = new File(sFolderToBeExported);

			String[] invalidChars = this.invalidChars.split(IExportCommonConstants.INVALID_CHAR_SEPARATOR);
			if (!fFolderToBeExported.exists() || !fFolderToBeExported.isDirectory()) {
				LOGGER.error("Folder does not exist folder name=" + fFolderToBeExported);
				throw new DCMABusinessException("Folder not found.");
			}

			File exportToBatchFolder = null;
			if (batchXmlFolderName == null || batchXmlFolderName.isEmpty()
					|| batchXmlFolderName.equalsIgnoreCase(IExportCommonConstants.FINAL_EXPORT_FOLDER)) {
				exportToBatchFolder = new File(exportToFolder);
			} else {
				exportToBatchFolder = new File(exportToFolder + File.separator + batchInstanceID);
			}
			exportToBatchFolder.mkdir();
			if (!exportToBatchFolder.exists()) {
				throw new DCMABusinessException("Could not create folder= " + exportToBatchFolder.getAbsolutePath());
			}
			LOGGER.info("Created Export To Folder=" + exportToBatchFolder.getAbsolutePath());
			String xmlFileName = batchInstanceID + ICommonConstants.UNDERSCORE_BATCH_XML;
			String xmlZipFileName = sFolderToBeExported + File.separator + xmlFileName;

			File xmlFile = null;
			File xmlZipFile = null;

			if (isZipSwitchOn) {
				if (com.ephesoft.dcma.util.FileUtils.isZipFileExists(xmlZipFileName)) {
					xmlZipFile = batchSchemaService.getFile(batchInstanceID, batchInstanceID
							+ ICommonConstants.UNDERSCORE_BATCH_XML_ZIP);
					LOGGER.info("Found zip xml file");
				} else {
					xmlFile = batchSchemaService.getFile(batchInstanceID, batchInstanceID + ICommonConstants.UNDERSCORE_BATCH_XML);
					if (!xmlFile.exists()) {
						throw new DCMABusinessException("Could not find batch.xml or batch xml zip file in the folder "
								+ fFolderToBeExported);
					}
					LOGGER.info("Found xml file");
				}
			} else {
				try {
					xmlFile = batchSchemaService.getFile(batchInstanceID, batchInstanceID + ICommonConstants.UNDERSCORE_BATCH_XML);
					if (xmlFile.exists()) {
						LOGGER.info("Found xml file");

					}
				} catch (DCMABusinessException dcmae) {
					if (com.ephesoft.dcma.util.FileUtils.isZipFileExists(xmlZipFileName)) {
						xmlZipFile = batchSchemaService.getFile(batchInstanceID, batchInstanceID
								+ ICommonConstants.UNDERSCORE_BATCH_XML_ZIP);
						LOGGER.info("Found zip xml file");
					}
				}
			}
			Batch batch = batchSchemaService.getBatch(batchInstanceID);

			Documents documents = batch.getDocuments();
			if (documents != null && documents.getDocument() != null) {
				List<Document> listOfDocuments = documents.getDocument();
				// Export batch XML file to exportToBatchFolder.
				File destinationXmlFile = new File(exportToBatchFolder.getPath() + File.separator + xmlFileName);
				exportBatchXML(batchInstanceID, isZipSwitchOn, sFolderToBeExported, xmlFileName, xmlFile, xmlZipFile,
						destinationXmlFile);
				exportMultiPagePdfAndTiffFiles(batchInstanceID, folderName, fileName, fFolderToBeExported, exportToFolder,
						listOfDocuments, invalidChars);
				updateExportedBatchXml(batch, destinationXmlFile);
			}
		}
	}

	private void updateExportedBatchXml(Batch batch, File destinationXmlFile) {
		LOGGER.info("Entering updateExportedBatchXml method");
		LOGGER.info("Updating batch XML: " + destinationXmlFile.getAbsolutePath());
		batchSchemaService.update(batch, destinationXmlFile.getAbsolutePath());
		LOGGER.info("Batch XML updated successfully: " + destinationXmlFile.getAbsolutePath());
		LOGGER.info("Exiting updateExportedBatchXml method");
	}

	private void exportMultiPagePdfAndTiffFiles(final String batchInstanceID, final String fileNameFormat, final String fileNameArr,
			final File fFolderToBeExported, final String exportToFolder, final List<Document> listOfDocuments,
			final String[] invalidChars) {
		String fileNameFormatArr[] = null;
		String folderNameFormatArr[] = null;
		if (fileNameArr != null && !fileNameArr.isEmpty()) {
			fileNameFormatArr = fileNameArr.split(IExportCommonConstants.FILE_FORMAT_SEPARATOR);
		}
		if (fileNameFormat != null && !fileNameFormat.isEmpty()) {
			folderNameFormatArr = fileNameFormat.split(IExportCommonConstants.FILE_FORMAT_SEPARATOR);
		}
		LOGGER.info("File format specified by admin : " + fileNameArr);
		LOGGER.info("Folder format specified by admin : " + fileNameFormat);
		File exportToBatchFolder = null;
		for (Document document : listOfDocuments) {
			LOGGER.info("Exporiting files for document: " + document.getType());
			List<DocField> docFieldList = null;
			if (document.getDocumentLevelFields() != null) {
				docFieldList = document.getDocumentLevelFields().getDocumentLevelField();
			}
			String sMultiPagePdf = document.getMultiPagePdfFile();
			String sMultiPageTif = document.getMultiPageTiffFile();
			String finalMultiPagePdf = sMultiPagePdf;
			String finalMultiPageTif = sMultiPageTif;

			// Get updated folder name based on the format specified by Admin.
			String exportToFolderName = null;
			if (folderNameFormatArr != null && folderNameFormatArr.length > 0) {
				LOGGER.info("Getting updated folder name.");
				exportToFolderName = getUpdatedFileName(batchInstanceID, document.getIdentifier(), folderNameFormatArr, docFieldList);
				LOGGER.info("Updated folder name: " + exportToFolderName);
				if (exportToFolderName == null || exportToFolderName.isEmpty()) {
					exportToFolderName = IExportCommonConstants.DEFAULT_FOLDER_NAME;
				} else {
					exportToFolderName = replaceInvalidFileChars(exportToFolderName, invalidChars);
					LOGGER.info("Updated folder name: " + exportToFolderName);
				}
			} else {
				exportToFolderName = batchInstanceID;
			}
			exportToBatchFolder = new File(exportToFolder + File.separator + exportToFolderName);
			LOGGER.info("Exporting files to folder : " + exportToBatchFolder.getAbsolutePath());
			String finalMultiPageFileName = null;
			if (fileNameFormatArr != null && fileNameFormatArr.length > 0) {
				LOGGER.info("Getting updated file name.");
				finalMultiPageFileName = getUpdatedFileName(batchInstanceID, document.getIdentifier(), fileNameFormatArr, docFieldList);
				LOGGER.info("Updated file name: " + finalMultiPageFileName);
				if (finalMultiPageFileName != null && !finalMultiPageFileName.isEmpty()) {
					finalMultiPageFileName = replaceInvalidFileChars(finalMultiPageFileName, invalidChars);
					LOGGER.info("Updated file name: " + finalMultiPageFileName);
					finalMultiPagePdf = finalMultiPageFileName + FileType.PDF.getExtensionWithDot();
					finalMultiPageTif = finalMultiPageFileName + FileType.TIF.getExtensionWithDot();
				}
			}
			// Export pdf and tiff files to exportToBatchFolder and rename the multi page pdf and tiff files in exported batch.xml.
			if (sMultiPagePdf != null && !sMultiPagePdf.isEmpty()) {
				document
						.setMultiPagePdfFile(exportPdfFile(fFolderToBeExported, exportToBatchFolder, sMultiPagePdf, finalMultiPagePdf));

			}
			if (sMultiPageTif != null && !sMultiPageTif.isEmpty()) {
				document.setMultiPageTiffFile(exportTiffFile(fFolderToBeExported, exportToBatchFolder, sMultiPageTif,
						finalMultiPageTif));
			}
		}
	}

	private String replaceInvalidFileChars(final String fileName, final String[] invalidChars) {
		LOGGER.info("Entering removeInvalidFileChars method");
		String updatedFileName = fileName;
		if (fileName != null && !fileName.isEmpty() && invalidChars != null && invalidChars.length > 0) {
			if (this.replaceChar == null || this.replaceChar.isEmpty()) {
				LOGGER.info("Replace character not specified. Using default character '-' as a replace character.");
				this.replaceChar = IExportCommonConstants.DEFAULT_REPLACE_CHAR;
			}
			for (String invalidChar : invalidChars) {
				if (this.replaceChar.equals(invalidChar)) {
					LOGGER
							.info("Replace character not specified or an invalid character. Using default character '-' as a replace character.");
					this.replaceChar = IExportCommonConstants.DEFAULT_REPLACE_CHAR;
				}
				updatedFileName = updatedFileName.replace(invalidChar, this.replaceChar);
			}
		}
		LOGGER.info("Exiting removeInvalidFileChars method");
		return updatedFileName;
	}

	private String getUpdatedFileName(final String batchInstanceID, final String documentIdentifier, final String[] fileNameFormat,
			final List<DocField> docFieldList) {
		LOGGER.info("Entering getUpdatedFileName method.");
		final StringBuffer updatedFileName = new StringBuffer();
		boolean isValidParamForFileName = false;
		String dlfValue = null;
		for (String fileFormat : fileNameFormat) {
			fileFormat = fileFormat.trim();
			LOGGER.info("Paramter : " + fileFormat);
			if (fileFormat.startsWith(IExportCommonConstants.PARAM_START_DELIMETER)) {
				fileFormat = fileFormat.substring(1);
				if (fileFormat.equalsIgnoreCase(IExportCommonConstants.EPHESOFT_BATCH_ID)) {
					isValidParamForFileName = true;
					updatedFileName.append(batchInstanceID);
				} else if (fileFormat.equalsIgnoreCase(IExportCommonConstants.EPHESOFT_DOCUMENT_ID)) {
					isValidParamForFileName = true;
					updatedFileName.append(documentIdentifier);
				} else if (docFieldList != null && !docFieldList.isEmpty()) {
					dlfValue = getDlfValue(docFieldList, fileFormat);
					if (dlfValue != null && !dlfValue.isEmpty()) {
						isValidParamForFileName = true;
						updatedFileName.append(dlfValue);
					}
				}
			} else if (isValidParamForFileName) {
				isValidParamForFileName = false;
				updatedFileName.append(fileFormat);
			}
			LOGGER.info("Updated file name: " + updatedFileName);
		}
		LOGGER.info("Exiting getUpdatedFileName method.");
		return updatedFileName.toString();
	}

	private String getDlfValue(final List<DocField> docFieldList, final String dlfName) {
		LOGGER.info("Entering getDlfValue method.");
		String dlfValue = null;
		boolean dlfFound = false;
		LOGGER.info("Get value for dlf: " + dlfName);
		for (final DocField docField : docFieldList) {
			if (docField.getName() != null && docField.getName().equalsIgnoreCase(dlfName)) {
				String value = docField.getValue();
				dlfFound = true;
				if (value != null && !value.trim().isEmpty()) {
					LOGGER.info("Dlf found, Value for Dlf: " + value);
					dlfValue = value.trim();
				}
				break;
			}
		}
		LOGGER.info("Dlf found: " + dlfFound);
		LOGGER.info("Exiting getDlfValue method.");
		return dlfValue;
	}

	private String exportTiffFile(final File fFolderToBeExported, final File exportToBatchFolder, final String sMultiPageTif,
			final String dMultiPageTif) {
		File fDestinationTifFile = null;
		if (fFolderToBeExported != null && exportToBatchFolder != null) {
			File fSourceTifFile = new File(fFolderToBeExported.getPath() + File.separator + sMultiPageTif);
			fDestinationTifFile = new File(exportToBatchFolder.getPath() + File.separator + dMultiPageTif);
			try {
				FileUtils.copyFile(fSourceTifFile, fDestinationTifFile, false);
			} catch (IOException e) {
				LOGGER.error("Problem copying Tiff file : " + fSourceTifFile, e);
				// throw new DCMAApplicationException("Problem copying Tiff file : " + fSourceTifFile,e);
			}
			if (!fDestinationTifFile.exists()) {
				throw new DCMABusinessException("Unable to export file " + fSourceTifFile);
			}
		}
		return fDestinationTifFile.getAbsolutePath();
	}

	private String exportPdfFile(final File fFolderToBeExported, final File exportToBatchFolder, final String sMultiPagePdf,
			final String dMultiPagePdf) {
		File fDestinationPdfFile = null;
		if (fFolderToBeExported != null && exportToBatchFolder != null) {
			File fSourcePdfFile = new File(fFolderToBeExported.getPath() + File.separator + sMultiPagePdf);
			fDestinationPdfFile = new File(exportToBatchFolder.getPath() + File.separator + dMultiPagePdf);
			try {
				FileUtils.copyFile(fSourcePdfFile, fDestinationPdfFile, false);
			} catch (IOException e) {
				LOGGER.error("Problem copying PDF file : " + fSourcePdfFile, e);
				// throw new DCMAApplicationException("Problem copying PDF file : " + fSourcePdfFile,e);
			}
			if (!fDestinationPdfFile.exists()) {
				throw new DCMABusinessException("Unable to export file " + fSourcePdfFile);
			}
		}
		return fDestinationPdfFile.getAbsolutePath();
	}
	private void exportBatchXML(final String batchInstanceID, final boolean isZipSwitchOn, final String sFolderToBeExported,
			final String xmlFileName, final File xmlFile, final File xmlZipFile, final File destinationXmlFile)
			throws DCMAApplicationException {
		InputStream inpStream = null;
		try {
			if (isZipSwitchOn) {
				if (xmlZipFile != null && xmlZipFile.exists()) {
					inpStream = com.ephesoft.dcma.util.FileUtils.getInputStreamFromZip(sFolderToBeExported + File.separator
							+ xmlFileName, xmlFileName);
					FileUtils.copyInputStreamToFile(inpStream, destinationXmlFile);
				} else {
					FileUtils.copyFile(xmlFile, destinationXmlFile, false);
				}
			} else {
				if (xmlFile != null && xmlFile.exists()) {
					FileUtils.copyFile(xmlFile, destinationXmlFile, false);
				} else {
					inpStream = com.ephesoft.dcma.util.FileUtils.getInputStreamFromZip(sFolderToBeExported + File.separator
							+ xmlFileName, xmlFileName);
					FileUtils.copyInputStreamToFile(inpStream, destinationXmlFile);
				}
			}
			LOGGER.info("Successfully export file for batch Instance identifier : " + batchInstanceID);
			if (!destinationXmlFile.exists()) {
				throw new DCMABusinessException("Unable to export file " + destinationXmlFile);
			}
		} catch (IOException e) {
			LOGGER.error("Problem copying XML file : " + xmlFileName, e);
			throw new DCMAApplicationException("Problem copying XML file : " + xmlFileName, e);
		} finally {
			try {
				if (inpStream != null) {
					inpStream.close();
				}
			} catch (IOException ioe) {
				LOGGER.info("IOException in closing file input stream in Folder Exporter.");
			}
		}
	}
}
