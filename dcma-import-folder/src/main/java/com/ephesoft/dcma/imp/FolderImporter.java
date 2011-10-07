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

package com.ephesoft.dcma.imp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.BatchLevelField;
import com.ephesoft.dcma.batch.schema.BatchStatus;
import com.ephesoft.dcma.batch.schema.Direction;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.ObjectFactory;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Batch.BatchLevelFields;
import com.ephesoft.dcma.batch.schema.Batch.Documents;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.EphesoftProperty;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassField;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.imagemagick.service.ImageProcessService;
import com.ephesoft.dcma.util.CustomFileFilter;
import com.ephesoft.dcma.util.FileNameFormatter;
import com.ephesoft.dcma.util.FileUtils;

/**
 * This class implements the functionality of moving a particular folder to its destination folder. If the move is successful the
 * original folder is deleted.
 * 
 * @author Ephesoft
 * 
 */
@Component
public final class FolderImporter implements ICommonConstants {

	private static final String DOT_DELIMITER = ".";

	private static final String SEMICOLON_DELIMITER = ";";

	private static final String SER_EXTENSION = ";ser";

	private static final String IMPORT_MULTIPAGE_FILES_PLUGIN = "IMPORT_MULTIPAGE_FILES";

	private static final String IMPORT_BATCH_FOLDER_PLUGIN = "IMPORT_BATCH_FOLDER";

	private static final String SERIALIZATION_EXT = ".ser";
	private static final String BCF_SER_FILE_NAME = "BCF_ASSO";
	private static final Logger LOGGER = LoggerFactory.getLogger(FolderImporter.class);

	@Autowired
	private BatchSchemaService batchSchemaService;

	@Autowired
	private ImageProcessService imageProcessService;
	/**
	 * Instance of PluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * File name ignore char list with semi colon.
	 */
	private String folderIgnoreCharList;

	/**
	 * Folder name replace char.
	 */
	private String ignoreReplaceChar;

	/**
	 * Reference of invalidCharList.
	 */
	private String invalidCharList;

	/**
	 * @return the batchSchemaService
	 */
	public BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * @param batchSchemaService the batchSchemaService to set
	 */
	public void setBatchSchemaService(BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * @return the pluginPropertiesService
	 */
	public PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	/**
	 * @param pluginPropertiesService the pluginPropertiesService to set
	 */
	public void setPluginPropertiesService(PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	/**
	 * @return the folderIgnoreCharList
	 */
	public String getFolderIgnoreCharList() {
		return folderIgnoreCharList;
	}

	/**
	 * @param folderIgnoreCharList the folderIgnoreCharList to set
	 */
	public void setFolderIgnoreCharList(String folderIgnoreCharList) {
		this.folderIgnoreCharList = folderIgnoreCharList;
	}

	/**
	 * @return the ignoreReplaceChar
	 */
	public String getIgnoreReplaceChar() {
		return ignoreReplaceChar;
	}

	/**
	 * @param ignoreReplaceChar the ignoreReplaceChar to set
	 */
	public void setIgnoreReplaceChar(String ignoreReplaceChar) {
		this.ignoreReplaceChar = ignoreReplaceChar;
	}

	/**
	 * invalidCharList.
	 * 
	 * @return {@link String} invalidCharList.
	 */
	public final String getInvalidCharList() {
		return invalidCharList;
	}

	/**
	 * invalidCharList.
	 * 
	 * @param invalidCharList {@link String}
	 */
	public final void setInvalidCharList(final String invalidCharList) {
		this.invalidCharList = invalidCharList;
	}

	/**
	 * This constructor takes the moveToFolder as an argument since the move to folder is going to be predefined we are storing it as a
	 * instance variable so that while calling copyAndMove method we need to specify the full path of the folder to be moved.
	 * 
	 * @throws DCMAApplicationException
	 */

	public FolderImporter() throws DCMAApplicationException {
		super();
	}

	/**
	 * This method moves the given folder under the moveToFolder (specified in the constructor)
	 * 
	 * @param batchInstance
	 * 
	 * @param sFolderToBeMoved
	 * @param batchInstanceIdentifier full path of the folder which has to be moved
	 * @return returns true if the folder move was sucsessful.False otherwise.
	 * @throws DCMAApplicationException
	 */
	public boolean copyAndMove(BatchInstance batchInstance, final String sFolderToBeMoved, final String batchInstanceIdentifier)
			throws DCMAApplicationException {

		String sMoveToFolder;
		File fFolderToBeMoved;
		File fMoveToFolder;
		boolean isCopySuccesful = false;
		fFolderToBeMoved = new File(sFolderToBeMoved);

		if (null == this.getInvalidCharList()) {
			throw new DCMAApplicationException("Unable to initialize in valid character list from properties file.");
		}

		String[] invalidCharList = this.getInvalidCharList().split(SEMICOLON_DELIMITER);
		// Initialize properties
		LOGGER.info("Initializing properties...");
		String validExt = pluginPropertiesService.getPropertyValue(batchInstance.getIdentifier(), IMPORT_BATCH_FOLDER_PLUGIN,
				FolderImporterProperties.FOLDER_IMPORTER_VALID_EXTNS);
		LOGGER.info("Properties Initialized Successfully");
		// If folder name contains invalid character throw batch to error.
		for (String inValidChar : invalidCharList) {
			if (inValidChar != null && !inValidChar.trim().isEmpty() && sFolderToBeMoved.contains(inValidChar)) {
				throw new DCMAApplicationException("Invalid characters present in folder name. Charater is " + inValidChar);
			}
		}

		// ADD extension to the serialized file in case of Batch class field association
		if (null != validExt) {
			validExt = validExt + SER_EXTENSION;
		}

		String[] validExtensions = validExt.split(SEMICOLON_DELIMITER);
		if (validExtensions == null || validExtensions.length == 0) {
			throw new DCMABusinessException("Could not find validExtensions properties in the property file");
		}

		if (!isFolderValid(sFolderToBeMoved, validExtensions, invalidCharList)) {
			throw new DCMABusinessException("Folder Invalid Folder name = " + sFolderToBeMoved);

		}
		sMoveToFolder = batchSchemaService.getLocalFolderLocation() + File.separator + batchInstanceIdentifier;

		File folderToBeMovedTo = new File(sMoveToFolder);
		if (folderToBeMovedTo.exists()) {
			FileUtils.deleteContentsOnly(sMoveToFolder);
		} else {
			boolean newDirCreationSuccess = new File(sMoveToFolder).mkdir();
			if (!newDirCreationSuccess) {
				throw new DCMABusinessException("Unable To Create directory" + newDirCreationSuccess);

			}
		}
		fMoveToFolder = new File(sMoveToFolder);
		try {
			copyDirectoryWithContents(fFolderToBeMoved, fMoveToFolder);
			isCopySuccesful = true;
		} catch (IOException e) {
			isCopySuccesful = false;
			throw new DCMAApplicationException("Unable to Copy Directory", e);
		}
		if (isCopySuccesful) {
			renameFiles(batchInstance, fMoveToFolder, batchInstanceIdentifier);
		}

		return isCopySuccesful;
	}

	/**
	 * This method renames all the moved files.
	 * 
	 * @param fMoveToFolder
	 * @param batchInstanceIdentifier
	 * @throws DCMAApplicationException
	 */
	private void renameFiles(final BatchInstance batchInstance, File fMoveToFolder, String batchInstanceIdentifier)
			throws DCMAApplicationException {
		ObjectFactory objectFactory = new ObjectFactory();
		String[] files = fMoveToFolder.list(new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(), FileType.TIFF
				.getExtensionWithDot(), FileType.PDF.getExtensionWithDot()));
		Batch batchXmlObj = generateBatchObject(batchInstance, batchInstanceIdentifier);
		Pages pages = new Pages();
		List<Page> listOfPages = pages.getPage();
		Arrays.sort(files);

		LOGGER.info("Starting rename of folder <<" + fMoveToFolder + ">>" + " total number of files=" + files.length);
		int pageId = 0;
		for (String fileName : files) {
			File movedFile = new File(fMoveToFolder.getAbsolutePath() + File.separator + fileName);
			FileNameFormatter formatter = null;
			try {
				formatter = new FileNameFormatter();
			} catch (Exception e) {
				throw new DCMAApplicationException("Could not instantiate FileNameFormatter", e);
			}
			String extension = getFileExtension(fileName);
			String newFileName = "";
			try {
				newFileName = formatter.getNewFileName(batchInstanceIdentifier, fileName, Integer.toString(pageId), extension);
			} catch (Exception e) {
				throw new DCMAApplicationException("Problem in obtaining the new file name", e);
			}
			File renamedFile = new File(fMoveToFolder.getAbsolutePath() + File.separator + newFileName);
			Page pageType = objectFactory.createPage();
			pageType.setIdentifier(EphesoftProperty.PAGE.getProperty() + pageId);
			pageType.setNewFileName(newFileName);
			pageType.setOldFileName(fileName);
			pageType.setDirection(Direction.NORTH);
			pageType.setIsRotated(false);
			listOfPages.add(pageType);

			movedFile.renameTo(renamedFile);
			pageId++;
		}

		Documents documents = new Documents();

		List<Document> listOfDocuments = documents.getDocument();
		Document document = objectFactory.createDocument();
		document.setIdentifier(EphesoftProperty.DOCUMENT.getProperty() + IFolderImporterConstants.ZERO);
		document.setConfidence(IFolderImporterConstants.ZERO);
		document.setType(EphesoftProperty.UNKNOWN.getProperty());
		document.setPages(pages);
		listOfDocuments.add(document);

		batchXmlObj.setDocuments(documents);
		addBatchClassFieldToBatch(batchXmlObj, batchInstance, objectFactory);
		batchSchemaService.updateBatch(batchXmlObj);

	}

	/**
	 * 
	 * @param batchXmlObj
	 * @param batchInstance
	 * @param objectFactory
	 */
	private void addBatchClassFieldToBatch(Batch batchXmlObj, BatchInstance batchInstance, ObjectFactory objectFactory) {

		String localFolder = batchInstance.getLocalFolder();
		String batchInstanceIdentifier = batchInstance.getIdentifier();
		String sFinalFolder = localFolder + File.separator + batchInstanceIdentifier;
		List<BatchClassField> batchClassFieldList = readBatchClassFieldSerializeFile(sFinalFolder);
		if (batchClassFieldList != null && !batchClassFieldList.isEmpty()) {
			BatchLevelFields batchLevelFields = new BatchLevelFields();
			List<BatchLevelField> listOfBatchLevelFields = batchLevelFields.getBatchLevelField();
			for (BatchClassField batchClassField : batchClassFieldList) {

				BatchLevelField batchlLevelField = objectFactory.createBatchLevelField();
				batchlLevelField.setName(batchClassField.getName());
				batchlLevelField.setValue(batchClassField.getValue());
				batchlLevelField.setType(batchClassField.getDataType().name());
				listOfBatchLevelFields.add(batchlLevelField);
			}
			batchXmlObj.setBatchLevelFields(batchLevelFields);
		}
	}

	/**
	 * This method returns the extension of the file.
	 * 
	 * @param fileName
	 * @return extension of file name.
	 */
	private String getFileExtension(String fileName) {
		String extension = "";
		String[] strArr = fileName.split("\\.");
		if (strArr.length == 2) {
			extension = strArr[1];
		}
		if (strArr.length > 2) {
			extension = strArr[strArr.length - 1];
		}
		return DOT_DELIMITER + extension;
	}

	/**
	 * This method copies a folder with all its contents from the source path to the destination path if the destination path does not
	 * exist it is created first.
	 * 
	 * @param batchInstance
	 * 
	 * @param srcPath The source folder whose contents are to be moved
	 * @param dstPath The destination folder to which all the contents are to be moved.
	 * @throws IOException
	 */
	private void copyDirectoryWithContents(final File srcPath, final File dstPath) throws IOException {

		if (srcPath.isDirectory()) {
			if (!dstPath.exists()) {
				dstPath.mkdir();
			}

			String[] files = srcPath.list(new CustomFileFilter(false, FileType.PDF.getExtension(), FileType.TIF.getExtension(),
					FileType.TIFF.getExtension(), FileType.SER.getExtension()));
			if (null == files || files.length == IFolderImporterConstants.ZERO) {
				throw new DCMABusinessException("Source directory is empty" + srcPath);
			}

			Arrays.sort(files);
			LOGGER.info("Starting copy of folder <<" + srcPath + ">>" + " total number of files=" + files.length);
			for (String fileName : files) {
				LOGGER.info("\tcopying file " + fileName);
				copyDirectoryWithContents(new File(srcPath, fileName), new File(dstPath, fileName));
			}
			LOGGER.info("Files copied sucsessfully to folder <<" + dstPath + ">>" + " total number of files=" + files.length);

		} else {
			if (!srcPath.exists()) {
				LOGGER.error("File or directory does not exist.");
				throw new DCMABusinessException("Source file does not exist Path=" + srcPath);
			} else {
				InputStream inStream = new FileInputStream(srcPath);
				OutputStream out = new FileOutputStream(dstPath);
				// Transfer bytes from in to out
				byte[] buf = new byte[IFolderImporterConstants.KBYTE];
				int len;
				while ((len = inStream.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				if (inStream != null) {
					inStream.close();
				}
				if (out != null) {
					out.close();
				}
			}
		}

	}

	/**
	 * Validates Business rules for folder validity. A folder is valid if it contains files with valid extensions a folder is invalid
	 * if 1. It is empty. 2. It contains sub folders. 3. It contains files with extensions other than the valid extensions.
	 * 
	 * @param sFolderToBeMoved
	 * @return boolean
	 */
	private boolean isFolderValid(final String sFolderToBeMoved, final String[] validExtensions, final String[] invalidCharList) {
		boolean folderValid = true;
		LOGGER.info("Validating buisiness Rules for folder <<" + sFolderToBeMoved + ">>");

		File fFolderToBeMoved = new File(sFolderToBeMoved);
		String[] files = fFolderToBeMoved.list();
		Arrays.sort(files);

		// Do not Move if Folder Empty
		if (files.length == 0) {
			LOGGER.info("\tBuisiness Rule Violation --Empty Folder--  Folder" + fFolderToBeMoved + " will not be moved");
			folderValid = false;
		}

		for (String fileName : files) {
			// if any of file name contains invalid character move batch to error.
			for (String inValidChar : invalidCharList) {
				if (inValidChar != null && !inValidChar.trim().isEmpty() && fileName.contains(inValidChar)) {
					folderValid = false;
					LOGGER.error("In valid characters present in the file name. File name is " + fileName + ". Charater is "
							+ inValidChar);
					break;
				}
			}
			
			if (folderValid) {
				File indivisualFile = new File(fFolderToBeMoved, fileName);

				if (indivisualFile.isDirectory()) {
					LOGGER.info("\tBuisiness Rule Violation Folder --Contains " + "Subfolders -- " + fFolderToBeMoved
							+ " will not be moved");
					folderValid = false;
					break;
				}

				String nameOfFile = fileName;
				boolean invalidFileExtension = true;
				for (String validExt : validExtensions) {
					if (nameOfFile.substring(nameOfFile.indexOf(DOT_DELIMITER.charAt(0)) + 1).equalsIgnoreCase(validExt)) {
						invalidFileExtension = false;
					}

				}
				if (invalidFileExtension) {
					LOGGER.info("\tBuisiness Rule Violation Folder --" + "File with Invalid Extensions-- "
							+ indivisualFile.getAbsolutePath() + " will not be moved");
				}

			}
		}

		LOGGER.info("Folder <<" + sFolderToBeMoved + ">> is valid");
		return folderValid;

	}

	/**
	 * Generates the batch.xml file.
	 * 
	 * @param batchInstance
	 * @param batchInstanceID
	 * @throws DCMAApplicationException
	 */
	public void generateBatchXML(final BatchInstance batchInstance, final String batchInstanceID) throws DCMAApplicationException {
		BatchClass batchClass = batchInstance.getBatchClass();
		String localFolder = batchInstance.getLocalFolder();
		String batchInstanceIdentifier = batchInstance.getIdentifier();
		String sFianlFolder = localFolder + File.separator + batchInstanceIdentifier;
		File fFinalFolder = new File(sFianlFolder);
		ObjectFactory objectFactory = new ObjectFactory();

		Batch objBatchXml = objectFactory.createBatch();
		objBatchXml.setBatchInstanceIdentifier(batchInstanceID);
		objBatchXml.setBatchClassIdentifier(batchClass.getIdentifier());
		objBatchXml.setBatchClassName(batchClass.getName());
		objBatchXml.setBatchClassDescription(batchClass.getDescription());
		objBatchXml.setBatchClassVersion(batchClass.getVersion());
		objBatchXml.setBatchName(batchInstance.getBatchName());
		objBatchXml.setBatchLocalPath(batchInstance.getLocalFolder());
		objBatchXml.setBatchPriority(Integer.toString(batchClass.getPriority()));
		objBatchXml.setBatchStatus(BatchStatus.READY);

		String[] listOfFiles = fFinalFolder.list();
		Arrays.sort(listOfFiles);
		Pages pages = new Pages();
		List<Page> listOfPages = pages.getPage();
		int identifierValue = 0;
		for (String fileName : listOfFiles) {
			Page pageType = objectFactory.createPage();
			pageType.setIdentifier(EphesoftProperty.PAGE.getProperty() + identifierValue);
			pageType.setNewFileName(fileName);
			String[] strArr = fileName.split(batchInstanceIdentifier + "_");
			pageType.setOldFileName(strArr[1]);
			pageType.setDirection(Direction.NORTH);
			pageType.setIsRotated(false);
			listOfPages.add(pageType);
			identifierValue++;
		}

		Documents documents = new Documents();
		BatchLevelFields batchLevelFields = new BatchLevelFields();
		if (listOfFiles.length > IFolderImporterConstants.ZERO) {
			List<Document> listOfDocuments = documents.getDocument();
			Document document = objectFactory.createDocument();
			document.setIdentifier(EphesoftProperty.DOCUMENT.getProperty() + IFolderImporterConstants.ONE);
			document.setConfidence(IFolderImporterConstants.ZERO);
			document.setType(EphesoftProperty.UNKNOWN.getProperty());
			document.setPages(pages);
			listOfDocuments.add(document);

			ArrayList<BatchClassField> batchClassFieldList = readBatchClassFieldSerializeFile(sFianlFolder);
			List<BatchLevelField> listOfBatchLevelFields = batchLevelFields.getBatchLevelField();
			for (BatchClassField batchClassField : batchClassFieldList) {
				BatchLevelField batchlLevelField = objectFactory.createBatchLevelField();
				batchlLevelField.setName(batchClassField.getName());
				batchlLevelField.setValue(batchClassField.getValue());
				batchlLevelField.setType(batchClassField.getDataType().name());
				listOfBatchLevelFields.add(batchlLevelField);
			}
		}
		objBatchXml.setDocuments(documents);
		objBatchXml.setBatchLevelFields(batchLevelFields);

		batchSchemaService.updateBatch(objBatchXml);

	}

	private ArrayList<BatchClassField> readBatchClassFieldSerializeFile(String sFinalFolder) {
		FileInputStream fileInputStream = null;
		ArrayList<BatchClassField> batchClassFieldList = null;
		File serializedFile = null;
		try {
			String serializedFilePath = sFinalFolder + File.separator + BCF_SER_FILE_NAME + SERIALIZATION_EXT;
			serializedFile = new File(serializedFilePath);
			fileInputStream = new FileInputStream(serializedFile);
			batchClassFieldList = (ArrayList<BatchClassField>) SerializationUtils.deserialize(fileInputStream);
			// updateFile(serializedFile, serializedFilePath);
		} catch (IOException e) {
			LOGGER.info("Error during reading the serialized file. ");
		} catch (Exception e) {
			LOGGER.error("Error during de-serializing the properties for Database Upgrade: ", e);
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (Exception e) {
				if (serializedFile != null) {
					LOGGER.error("Problem closing stream for file :" + serializedFile.getName());
				}
			}
		}

		return batchClassFieldList;
	}

	/**
	 * Generates the batch.xml file.
	 * 
	 * @param batchInstance
	 * @param batchInstanceID
	 * @throws DCMAApplicationException
	 */
	public Batch generateBatchObject(final BatchInstance batchInstance, final String batchInstanceID) throws DCMAApplicationException {
		BatchClass batchClass = batchInstance.getBatchClass();

		ObjectFactory objectFactory = new ObjectFactory();

		Batch objBatchXml = objectFactory.createBatch();
		objBatchXml.setBatchInstanceIdentifier(batchInstanceID);
		objBatchXml.setBatchClassIdentifier(batchClass.getIdentifier());
		objBatchXml.setBatchClassName(batchClass.getName());
		objBatchXml.setBatchClassDescription(batchClass.getDescription());
		objBatchXml.setBatchName(batchInstance.getBatchName());
		objBatchXml.setBatchClassVersion(batchClass.getVersion());
		objBatchXml.setBatchLocalPath(batchInstance.getLocalFolder());
		objBatchXml.setBatchPriority(Integer.toString(batchClass.getPriority()));
		objBatchXml.setBatchStatus(BatchStatus.READY);
		return objBatchXml;

	}

	public void createMultiPageTiff(BatchInstance batchInstance, final String folderPath) throws DCMAException {
		BatchClass batchClass = batchInstance.getBatchClass();
		String importMultiPage = pluginPropertiesService.getPropertyValue(batchInstance.getIdentifier(),
				IMPORT_MULTIPAGE_FILES_PLUGIN, FolderImporterProperties.FOLDER_IMPORTER_MULTI_PAGE_IMPORT);
		if (!(importMultiPage.equalsIgnoreCase(IFolderImporterConstants.TRUE) || importMultiPage
				.equalsIgnoreCase(IFolderImporterConstants.YES))) {
			return;
		}
		File folder = new File(folderPath);
		if (folder != null) {
			String[] folderList = folder.list(new CustomFileFilter(false, FileType.PDF.getExtension(), FileType.TIF.getExtension(),
					FileType.TIFF.getExtension()));

			String folderIgnoreCharList = getFolderIgnoreCharList();
			if (null == folderIgnoreCharList || getIgnoreReplaceChar() == null || folderIgnoreCharList.isEmpty()
					|| getIgnoreReplaceChar().isEmpty() || getIgnoreReplaceChar().length() > 1) {
				throw new DCMAException("Invalid property file configuration...");
			}

			String fdIgList[] = folderIgnoreCharList.split(SEMICOLON_DELIMITER);
			List<File> deleteFileList = new ArrayList<File>();

			boolean isFound = false;
			BatchInstanceThread threadList = new BatchInstanceThread();

			if (folderList != null) {
				for (String string : folderList) {

					if (string == null || string.isEmpty()) {
						continue;
					}

					try {
						isFound = false;
						File fileOriginal = new File(folderPath + File.separator + string);
						File fileNew = null;

						for (String nameStr : fdIgList) {
							if (string.contains(nameStr)) {
								isFound = true;
								string = string.replaceAll(nameStr, getIgnoreReplaceChar());
							}
						}

						if (isFound) {
							fileNew = new File(folderPath + File.separator + string);
							fileOriginal.renameTo(fileNew);

							LOGGER.info("Converting multi page file : " + fileNew.getAbsolutePath());
							imageProcessService.convertPdfOrMultiPageTiffToTiff(batchClass, fileNew, threadList);
							deleteFileList.add(fileNew);
						} else {
							LOGGER.info("Converting multi page file : " + fileOriginal.getAbsolutePath());
							imageProcessService.convertPdfOrMultiPageTiffToTiff(batchClass, fileOriginal, threadList);
							deleteFileList.add(fileOriginal);
						}
					} catch (Exception e) {
						LOGGER.error("Error in converting multi page file to single page tiff files", e);
						throw new DCMAException("Error in breaking image to single pages " + e.getMessage(), e);
					}
				}
				try {
					LOGGER.info("Executing conversion of multi page file using thread pool");
					threadList.execute();
					LOGGER.info("Completed conversion of multi page file using thread pool");
				} catch (DCMAApplicationException e) {
					LOGGER.error(e.getMessage(), e);
					throw new DCMAException(e.getMessage(), e);
				}
				// Clean up operation
				LOGGER.info("Cleaning up the old files.");
				for (File file : deleteFileList) {
					boolean isDeleted = file.delete();
					LOGGER.debug(" File " + file.getAbsolutePath() + " deleted " + isDeleted);
				}
			}
		}
	}

}
