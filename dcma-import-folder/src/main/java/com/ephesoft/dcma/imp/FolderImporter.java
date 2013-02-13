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

package com.ephesoft.dcma.imp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.batch.dao.xml.UploadBatchXmlDao;
import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.BatchLevelField;
import com.ephesoft.dcma.batch.schema.BatchStatus;
import com.ephesoft.dcma.batch.schema.Direction;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.ObjectFactory;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.UploadBatchMetaData;
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
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.imagemagick.service.ImageProcessService;
import com.ephesoft.dcma.util.CustomFileFilter;
import com.ephesoft.dcma.util.FileNameFormatter;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.PDFUtil;
import com.ephesoft.dcma.util.TIFFUtil;

/**
 * This class implements the functionality of moving a particular folder to its destination folder. If the move is successful the
 * original folder is deleted.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.imp.service.ImportService
 */
@Component
public final class FolderImporter implements ICommonConstants {

	/**
	 * Logger for proper logging using slf4j.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(FolderImporter.class);

	/**
	 * An instance of {@link BatchSchemaService}.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * An instance of {@link ImageProcessService}.
	 */
	@Autowired
	private ImageProcessService imageProcessService;
	
	/**
	 * An instance of {@link UploadBatchXmlDao}.
	 */
	@Autowired
	private UploadBatchXmlDao uploadBatchXmlDao;
	
	/**
	 * Instance of {@link PluginPropertiesService}.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * File name ignore char list with semicolon.
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
	 * Instance of {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * To get Batch Schema Service.
	 * @return the {@link BatchSchemaService}
	 */
	public BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * To set Batch Schema Service.
	 * @param batchSchemaService the {@link BatchSchemaService}
	 */
	public void setBatchSchemaService(final BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * To get Plugin Properties Service.
	 * @return the {@link PluginPropertiesService}
	 */
	public PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	/**
	 * To set Plugin Properties Service.
	 * @param pluginPropertiesService the {@link PluginPropertiesService}
	 */
	public void setPluginPropertiesService(final PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	/**
	 * To get Folder Ignore Char List.
	 * @return {@link String}
	 */
	public String getFolderIgnoreCharList() {
		return folderIgnoreCharList;
	}

	/**
	 * To set Folder Ignore Char List.
	 * @param folderIgnoreCharList {@link String}
	 */
	public void setFolderIgnoreCharList(final String folderIgnoreCharList) {
		this.folderIgnoreCharList = folderIgnoreCharList;
	}

	/**
	 * To get Ignore Replace Char.
	 * @return {@link String}
	 */
	public String getIgnoreReplaceChar() {
		return ignoreReplaceChar;
	}

	/**
	 * To set Ignore Replace Char.
	 * @param ignoreReplaceChar {@link String}
	 */
	public void setIgnoreReplaceChar(final String ignoreReplaceChar) {
		this.ignoreReplaceChar = ignoreReplaceChar;
	}

	/**
	 * To get invalid Character List.
	 * 
	 * @return {@link String} invalidCharList.
	 */
	public String getInvalidCharList() {
		return invalidCharList;
	}

	/**
	 * To set invalid Character List.
	 * 
	 * @param invalidCharList {@link String}
	 */
	public void setInvalidCharList(final String invalidCharList) {
		this.invalidCharList = invalidCharList;
	}

	/**
	 * This constructor takes the moveToFolder as an argument since the move to folder is going to be predefined we are storing it as a
	 * instance variable so that while calling copyAndMove method we need to specify the full path of the folder to be moved.
	 * 
	 * @throws DCMAApplicationException if error occurs
	 */
	public FolderImporter() throws DCMAApplicationException {
		super();
	}

	/**
	 * This method moves the given folder under the moveToFolder (specified in the constructor).
	 * @param batchInstance {@link BatchInstance}
	 * @param sFolderToBeMoved {@link String}
	 * @param batchInstanceIdentifier {@link String}
	 * @return boolean
	 * @throws DCMAApplicationException if error occurs
	 */
	public boolean copyAndMove(final BatchInstance batchInstance, final String sFolderToBeMoved, final String batchInstanceIdentifier)
			throws DCMAApplicationException {
		String sMoveToFolder;
		File fFolderToBeMoved;
		File fMoveToFolder;
		boolean isCopySuccesful = false;
		fFolderToBeMoved = new File(sFolderToBeMoved);

		if (null == this.getInvalidCharList()) {
			throw new DCMAApplicationException("Unable to initialize in valid character list from properties file.");
		}

		final String[] invalidCharList = this.getInvalidCharList().split(IFolderImporterConstants.SEMICOLON_DELIMITER);
		// Initialize properties
		LOGGER.info("Initializing properties...");
		final String validExt = pluginPropertiesService.getPropertyValue(batchInstance.getIdentifier(), IFolderImporterConstants.IMPORT_BATCH_FOLDER_PLUGIN,
				FolderImporterProperties.FOLDER_IMPORTER_VALID_EXTNS);
		LOGGER.info("Properties Initialized Successfully");
		// If folder name contains invalid character throw batch to error.
		for (final String inValidChar : invalidCharList) {
			if (inValidChar != null && !inValidChar.trim().isEmpty() && sFolderToBeMoved.contains(inValidChar)) {
				throw new DCMAApplicationException("Invalid characters present in folder name. Charater is " + inValidChar);
			}
		}

		// ADD extension to the serialized file in case of Batch class field
		// association
		StringBuilder validExtBuilder = null;
		String[] validExtensions = null;
		if (null != validExt) {
			validExtBuilder = new StringBuilder(validExt);
			validExtBuilder.append(IFolderImporterConstants.SER_EXTENSION);
			validExtensions = validExtBuilder.toString().split(IFolderImporterConstants.SEMICOLON_DELIMITER);
		}

		if (validExtensions == null || validExtensions.length == 0) {
			throw new DCMABusinessException("Could not find validExtensions properties in the property file");
		}

		// retrieving the data of UploadBatchMetaData.xml from unc folder in uploadBatchMetaDataXMLObject
		UploadBatchMetaData uploadBatchMetaDataXMLObject = getUploadBatchMetaData(sFolderToBeMoved);

		// Checking for the batch having files with valid extensions.If not throws exception.
		if (!isFolderValid(sFolderToBeMoved, validExtensions, invalidCharList)) {
			throw new DCMABusinessException("Folder Invalid Folder name = " + sFolderToBeMoved);

		}
		sMoveToFolder = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceIdentifier) + File.separator
				+ batchInstanceIdentifier;

		final File folderToBeMovedTo = new File(sMoveToFolder);
		if (folderToBeMovedTo.exists()) {

			// Deleting the batch from unc folder
			FileUtils.deleteContentsOnly(sMoveToFolder);
		} else {
			final boolean newDirCreationSuccess = new File(sMoveToFolder).mkdir();
			if (!newDirCreationSuccess) {
				throw new DCMABusinessException("Unable To Create directory" + newDirCreationSuccess);

			}
		}
		fMoveToFolder = new File(sMoveToFolder);
		try {

			// Copying the batch from unc folder to ephesoft-system-folder
			copyDirectoryWithContents(fFolderToBeMoved, fMoveToFolder);
			isCopySuccesful = true;
		} catch (final IOException e) {
			isCopySuccesful = false;
			throw new DCMAApplicationException("Unable to Copy Directory", e);
		}
		if (isCopySuccesful) {
			renameFiles(batchInstance, fMoveToFolder, batchInstanceIdentifier, uploadBatchMetaDataXMLObject);
		}

		return isCopySuccesful;
	}

	private UploadBatchMetaData getUploadBatchMetaData(final String sourceFolderPath) {
		UploadBatchMetaData uploadBatchMetaDataXMLObject = null;
		final File sourceFolder = new File(sourceFolderPath);

		// Getting the list of all the xml file in the given folder
		final File[] xmlFiles = sourceFolder.listFiles(new CustomFileFilter(false, FileType.XML.getExtensionWithDot()));
		for (final File file : xmlFiles) {

			// Retrieve the data from upload batch info xml file
			if (file.getName().equals(ICommonConstants.UPLOAD_BATCH_META_DATA_XML_FILE_NAME)) {
				uploadBatchMetaDataXMLObject = uploadBatchXmlDao.getObjectFromFilePath(file.getAbsolutePath());
				break;
			}
		}
		return uploadBatchMetaDataXMLObject;
	}

	private String createUNCFolderBackup(final String sFolderToBeMoved) throws DCMAException {
		String backUpFolderName = null;

		if (sFolderToBeMoved != null) {
			backUpFolderName = sFolderToBeMoved + File.separator + ICommonConstants.DOWNLOAD_FOLDER_NAME;
			File emailBackUpFolder = new File(backUpFolderName);
			File folderBackup = null;
			if (!emailBackUpFolder.exists()) {
				backUpFolderName = sFolderToBeMoved + File.separator + new File(sFolderToBeMoved).getName()
						+ ICommonConstants.BACK_UP_FOLDER_NAME;
				folderBackup = new File(backUpFolderName);
			} else {
				folderBackup = emailBackUpFolder;
			}
			if (folderBackup != null && !folderBackup.exists()) {
				File[] filesToCopy = new File(sFolderToBeMoved).listFiles();
				if (filesToCopy != null && (filesToCopy.length != 0)) {
					folderBackup.mkdir();
					for (File srcFile : filesToCopy) {
						if (srcFile != null && srcFile.exists()) {
							File destFile = new File(folderBackup.getAbsolutePath() + File.separator + srcFile.getName());
							try {
								if (srcFile.isFile()) {
									FileUtils.copyFile(srcFile, destFile);
								}
							} catch (FileNotFoundException e) {
								LOGGER.error("Exception while copying file from source to destination folder" + e.getMessage());
							} catch (IOException e) {
								LOGGER.error("Exception while reading or writing file" + e.getMessage());
							} catch (Exception e) {
								LOGGER.error("Exception while reading or writing file" + e.getMessage());
							}
						}
					}
				}
			} else if (folderBackup.isDirectory() && folderBackup.exists()) {
				FileUtils.deleteContentsOnly(sFolderToBeMoved);
				try {
					FileUtils.copyDirectoryWithContents(folderBackup.getAbsolutePath(), sFolderToBeMoved);
				} catch (IOException e) {
					throw new DCMAException("Error in copying file from backup to unc folder. Throwing batch into error"
							+ e.getMessage(), e);
				}
			}
		}
		return backUpFolderName;
	}

	private void renameFiles(final BatchInstance batchInstance, final File fMoveToFolder, final String batchInstanceIdentifier,
			final UploadBatchMetaData uploadBatchMetaDataXMLObj) throws DCMAApplicationException {
		final ObjectFactory objectFactory = new ObjectFactory();

		// getting the name-list of tiff files in batch folder.
		// In case of multipage tiff/PDF files, this list contains all the tiff
		// files after breaking the multipage files.
		final String[] files = fMoveToFolder.list(new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(), FileType.TIFF
				.getExtensionWithDot(), FileType.PDF.getExtensionWithDot()));

		// Creating the batch.xml file for the given batch
		final Batch batchXmlObj = generateBatchObject(batchInstance, batchInstanceIdentifier);
		final Pages pages = new Pages();
		final List<Page> listOfPages = pages.getPage();
		Arrays.sort(files);

		LOGGER.info("Starting rename of folder <<" + fMoveToFolder + ">>" + " total number of files=" + files.length);
		int pageId = 0;
		for (final String fileName : files) {
			final File movedFile = new File(fMoveToFolder.getAbsolutePath() + File.separator + fileName);
			FileNameFormatter formatter = null;
			try {
				formatter = new FileNameFormatter();
			} catch (final Exception e) {
				throw new DCMAApplicationException("Could not instantiate FileNameFormatter", e);
			}
			final String extension = getFileExtension(fileName);
			String newFileName = IFolderImporterConstants.EMPTY;
			try {
				newFileName = formatter.getNewFileName(batchInstanceIdentifier, fileName, Integer.toString(pageId), extension);
			} catch (final Exception e) {
				throw new DCMAApplicationException("Problem in obtaining the new file name", e);
			}
			final File renamedFile = new File(fMoveToFolder.getAbsolutePath() + File.separator + newFileName);
			final Page pageType = objectFactory.createPage();
			pageType.setIdentifier(EphesoftProperty.PAGE.getProperty() + pageId);
			pageType.setNewFileName(newFileName);
			pageType.setOldFileName(fileName);
			pageType.setDirection(Direction.NORTH);
			pageType.setIsRotated(false);
			listOfPages.add(pageType);

			movedFile.renameTo(renamedFile);
			pageId++;
		}

		final Documents documents = new Documents();

		final List<Document> listOfDocuments = documents.getDocument();
		final Document document = objectFactory.createDocument();
		document.setIdentifier(EphesoftProperty.DOCUMENT.getProperty() + IFolderImporterConstants.ZERO);
		document.setConfidence(IFolderImporterConstants.ZERO);
		document.setType(EphesoftProperty.UNKNOWN.getProperty());
		document.setPages(pages);
		document.setErrorMessage(BatchConstants.EMPTY);
		document.setDocumentDisplayInfo(BatchConstants.EMPTY);
		listOfDocuments.add(document);
		
		// storing the initial count of total pages of the input batch in batch.xml file
		batchXmlObj.setInputPageCount(pageId);

		// Storing the user name in batch xml,for uploaded batches
		if (uploadBatchMetaDataXMLObj != null) {
			final String userName = uploadBatchMetaDataXMLObj.getUserName();
			LOGGER.info("User name of uploded batch:" + userName);
			if (userName != null) {
				batchXmlObj.setUserName(userName);
			}
		}

		// storing the documents information of the batch in batch.xml file
		batchXmlObj.setDocuments(documents);
		addBatchClassFieldToBatch(batchXmlObj, batchInstance, objectFactory);
		batchSchemaService.updateBatch(batchXmlObj, true);

	}

	private void addBatchClassFieldToBatch(final Batch batchXmlObj, final BatchInstance batchInstance, final ObjectFactory objectFactory) {
		final String localFolder = batchInstance.getLocalFolder();
		final String batchInstanceIdentifier = batchInstance.getIdentifier();
		final String sFinalFolder = localFolder + File.separator + batchInstanceIdentifier;
		final List<BatchClassField> batchClassFieldList = readBatchClassFieldSerializeFile(sFinalFolder);
		if (batchClassFieldList != null && !batchClassFieldList.isEmpty()) {
			final BatchLevelFields batchLevelFields = new BatchLevelFields();
			final List<BatchLevelField> listOfBatchLevelFields = batchLevelFields.getBatchLevelField();
			for (final BatchClassField batchClassField : batchClassFieldList) {

				final BatchLevelField batchlLevelField = objectFactory.createBatchLevelField();
				batchlLevelField.setName(batchClassField.getName());
				batchlLevelField.setValue(batchClassField.getValue());
				batchlLevelField.setType(batchClassField.getDataType().name());
				listOfBatchLevelFields.add(batchlLevelField);
			}
			batchXmlObj.setBatchLevelFields(batchLevelFields);
		}
	}

	private String getFileExtension(final String fileName) {
		String extension = IFolderImporterConstants.EMPTY;
		final String[] strArr = fileName.split("\\.");
		if (strArr.length == 2) {
			extension = strArr[1];
		}
		if (strArr.length > 2) {
			extension = strArr[strArr.length - 1];
		}
		return IFolderImporterConstants.DOT_DELIMITER + extension;
	}

	private void copyDirectoryWithContents(final File srcPath, final File dstPath) throws IOException {

		if (srcPath.isDirectory()) {
			if (!dstPath.exists()) {
				dstPath.mkdir();
			}

			final String[] files = srcPath.list(new CustomFileFilter(false, FileType.PDF.getExtension(), FileType.TIF.getExtension(),
					FileType.TIFF.getExtension(), FileType.SER.getExtension()));
			if (null == files || files.length == IFolderImporterConstants.ZERO) {
				throw new DCMABusinessException("Source directory is empty" + srcPath);
			}

			Arrays.sort(files);
			LOGGER.info("Starting copy of folder <<" + srcPath + ">>" + " total number of files=" + files.length);
			for (final String fileName : files) {
				LOGGER.info("\tcopying file " + fileName);
				copyDirectoryWithContents(new File(srcPath, fileName), new File(dstPath, fileName));
			}
			LOGGER.info("Files copied sucsessfully to folder <<" + dstPath + ">>" + " total number of files=" + files.length);

		} else {
			if (!srcPath.exists()) {
				LOGGER.error("File or directory does not exist.");
				throw new DCMABusinessException("Source file does not exist Path=" + srcPath);
			} else {
				final InputStream inStream = new FileInputStream(srcPath);
				final OutputStream out = new FileOutputStream(dstPath);
				// Transfer bytes from in to out
				final byte[] buf = new byte[IFolderImporterConstants.KBYTE];
				int len;
				try {
					while ((len = inStream.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
				} finally {
					if (inStream != null) {
						inStream.close();
					}
					if (out != null) {
						out.close();
					}
				}
			}
		}

	}

	private boolean isFolderValid(final String sFolderToBeMoved, final String[] validExtensions, final String[] invalidCharList) {
		boolean folderValid = true;
		LOGGER.info("Validating buisiness Rules for folder <<" + sFolderToBeMoved + ">>");

		final File fFolderToBeMoved = new File(sFolderToBeMoved);
		final String[] files = fFolderToBeMoved.list();
		Arrays.sort(files);

		// Do not Move if Folder Empty
		if (files.length == 0) {
			LOGGER.error("\tBuisiness Rule Violation --Empty Folder--  Folder" + fFolderToBeMoved + " will not be moved");
			folderValid = false;
		}

		for (final String fileName : files) {
			// if any of file name contains invalid character move batch to
			// error.
			for (final String inValidChar : invalidCharList) {
				if (inValidChar != null && !inValidChar.trim().isEmpty() && fileName.contains(inValidChar)) {
					folderValid = false;
					LOGGER.error("In valid characters present in the file name. File name is " + fileName + ". Charater is "
							+ inValidChar);
					break;
				}
			}

			if (folderValid) {
				final File indivisualFile = new File(fFolderToBeMoved, fileName);
				final String nameOfFile = fileName;
				boolean invalidFileExtension = true;
				for (final String validExt : validExtensions) {
					if (indivisualFile.isDirectory()
							|| nameOfFile.substring(nameOfFile.lastIndexOf(IFolderImporterConstants.DOT_DELIMITER.charAt(0)) + 1).equalsIgnoreCase(validExt)) {
						invalidFileExtension = false;
					}

				}
				if (invalidFileExtension) {
					LOGGER.info("\tBuisiness Rule Violation Folder --" + "File with Invalid Extensions (or is directory) -- "
							+ indivisualFile.getAbsolutePath() + " will not be moved");
				}

			}
		}

		LOGGER.info("Folder <<" + sFolderToBeMoved + ">> is valid");
		return folderValid;

	}

	/**
	 * Generates the batch.xml file.
	 * @param batchInstance {@link BatchInstance}
	 * @param batchInstanceID {@link String}
	 * @throws DCMAApplicationException if error occurs
	 */
	public void generateBatchXML(final BatchInstance batchInstance, final String batchInstanceID) throws DCMAApplicationException {
		final BatchClass batchClass = batchInstance.getBatchClass();
		final String localFolder = batchInstance.getLocalFolder();
		final String batchInstanceIdentifier = batchInstance.getIdentifier();
		final String sFinalFolder = localFolder + File.separator + batchInstanceIdentifier;
		final File fFinalFolder = new File(sFinalFolder);
		final ObjectFactory objectFactory = new ObjectFactory();

		final Batch objBatchXml = objectFactory.createBatch();
		objBatchXml.setBatchInstanceIdentifier(batchInstanceID);
		objBatchXml.setBatchClassIdentifier(batchClass.getIdentifier());
		objBatchXml.setBatchClassName(batchClass.getName());
		objBatchXml.setBatchClassDescription(batchClass.getDescription());
		objBatchXml.setBatchClassVersion(batchClass.getVersion());
		objBatchXml.setBatchName(batchInstance.getBatchName());
		objBatchXml.setBatchLocalPath(batchInstance.getLocalFolder());
		objBatchXml.setBatchPriority(Integer.toString(batchClass.getPriority()));
		objBatchXml.setBatchStatus(BatchStatus.READY);

		final String[] listOfFiles = fFinalFolder.list();
		Arrays.sort(listOfFiles);
		final Pages pages = new Pages();
		final List<Page> listOfPages = pages.getPage();
		int identifierValue = 0;
		for (final String fileName : listOfFiles) {
			final Page pageType = objectFactory.createPage();
			pageType.setIdentifier(EphesoftProperty.PAGE.getProperty() + identifierValue);
			pageType.setNewFileName(fileName);
			final String[] strArr = fileName.split(batchInstanceIdentifier + IFolderImporterConstants.UNDERSCORE);
			pageType.setOldFileName(strArr[1]);
			pageType.setDirection(Direction.NORTH);
			pageType.setIsRotated(false);
			listOfPages.add(pageType);
			identifierValue++;
		}

		final Documents documents = new Documents();
		final BatchLevelFields batchLevelFields = new BatchLevelFields();
		if (listOfFiles.length > IFolderImporterConstants.ZERO) {
			final List<Document> listOfDocuments = documents.getDocument();
			final Document document = objectFactory.createDocument();
			document.setIdentifier(EphesoftProperty.DOCUMENT.getProperty() + IFolderImporterConstants.ONE);
			document.setConfidence(IFolderImporterConstants.ZERO);
			document.setType(EphesoftProperty.UNKNOWN.getProperty());
			document.setPages(pages);
			document.setErrorMessage(BatchConstants.EMPTY);
			document.setDocumentDisplayInfo(BatchConstants.EMPTY);
			listOfDocuments.add(document);

			final List<BatchClassField> batchClassFieldList = readBatchClassFieldSerializeFile(sFinalFolder);
			final List<BatchLevelField> listOfBatchLevelFields = batchLevelFields.getBatchLevelField();
			for (final BatchClassField batchClassField : batchClassFieldList) {
				final BatchLevelField batchlLevelField = objectFactory.createBatchLevelField();
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

	private List<BatchClassField> readBatchClassFieldSerializeFile(final String sFinalFolder) {
		FileInputStream fileInputStream = null;
		List<BatchClassField> batchClassFieldList = null;
		File serializedFile = null;
		try {
			final String serializedFilePath = sFinalFolder + File.separator + IFolderImporterConstants.BCF_SER_FILE_NAME + IFolderImporterConstants.SERIALIZATION_EXT;
			serializedFile = new File(serializedFilePath);
			fileInputStream = new FileInputStream(serializedFile);
			batchClassFieldList = (List<BatchClassField>) SerializationUtils.deserialize(fileInputStream);
			// updateFile(serializedFile, serializedFilePath);
		} catch (final IOException e) {
			LOGGER.info("Error during reading the serialized file. ");
		} catch (final Exception e) {
			LOGGER.error("Error during de-serializing the properties for Database Upgrade: ", e);
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (final Exception e) {
				if (serializedFile != null) {
					LOGGER.error("Problem closing stream for file :" + serializedFile.getName());
				}
			}
		}

		return batchClassFieldList;
	}

	/**
	 * Generates the batch.xml file.
	 * @param batchInstance {@link BatchInstance}
	 * @param batchInstanceID {@link String}
	 * @return {@link Batch}
	 * @throws DCMAApplicationException if error occurs
	 */
	public Batch generateBatchObject(final BatchInstance batchInstance, final String batchInstanceID) throws DCMAApplicationException {
		final BatchClass batchClass = batchInstance.getBatchClass();

		final ObjectFactory objectFactory = new ObjectFactory();

		final Batch objBatchXml = objectFactory.createBatch();
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

	/**
	 * This method is used to create the multi page tiff files.
	 * @param batchInstance {@link BatchInstance}
	 * @param folderPath {@link String}
	 * @throws DCMAException if error occurs
	 */
	public void createMultiPageTiff(final BatchInstance batchInstance, final String folderPath) throws DCMAException {
		String backUpFolderPath = createUNCFolderBackup(folderPath);

		BatchClass batchClass = batchInstance.getBatchClass();
		String importMultiPage = pluginPropertiesService.getPropertyValue(batchInstance.getIdentifier(),
				IFolderImporterConstants.IMPORT_MULTIPAGE_FILES_PLUGIN, FolderImporterProperties.FOLDER_IMPORTER_MULTI_PAGE_IMPORT);
		if (!(importMultiPage.equalsIgnoreCase(IFolderImporterConstants.TRUE) || importMultiPage
				.equalsIgnoreCase(IFolderImporterConstants.YES))) {
			return;
		}
		File folder = new File(folderPath);
		File backUpFolder = new File(backUpFolderPath);
		if (folder != null && backUpFolder != null && folder.exists() && backUpFolder.exists()) {
			// FileType.TIF.getExtension(), FileType.TIFF.getExtension())).length;
			int expectedTifFilesCount = getTiffPagesCount(backUpFolder);
			String folderIgnoreCharList = getFolderIgnoreCharList();
			if (null == folderIgnoreCharList || getIgnoreReplaceChar() == null || folderIgnoreCharList.isEmpty()
					|| getIgnoreReplaceChar().isEmpty() || getIgnoreReplaceChar().length() > 1) {
				throw new DCMAException("Invalid property file configuration....");
			}

			String fdIgList[] = folderIgnoreCharList.split(IFolderImporterConstants.SEMICOLON_DELIMITER);
			final List<File> deleteFileList = new ArrayList<File>();

			BatchInstanceThread threadList = new BatchInstanceThread(batchInstance.getIdentifier());
			threadList.setUsingGhostScript(true);
			processInputPDFFiles(folderPath, batchClass, folder, fdIgList, deleteFileList, threadList);

			threadList = new BatchInstanceThread(batchInstance.getIdentifier());
			processInputTiffFiles(folderPath, batchClass, fdIgList, deleteFileList, threadList, folder);
			// Deleting the intermediate files.
			deleteTempFiles(deleteFileList);

			// Counting the number of files converted in the batch folder.
			int actualTiffFilesCount = folder.list(new CustomFileFilter(false, FileType.TIF.getExtension(), FileType.TIFF
					.getExtension())).length;
			LOGGER.info("expectedTifFilesCount :: " + expectedTifFilesCount);
			LOGGER.info("Actual tiff files count :: " + actualTiffFilesCount);
			if (actualTiffFilesCount != expectedTifFilesCount) {
				LOGGER.error("Converted Tiff files count not equal to the the TIFF pages count. actualTiffFilesCount :: "
						+ actualTiffFilesCount + ", expectedTifFilesCount :: " + expectedTifFilesCount, " for batch instance ::"
						+ batchInstance.getIdentifier());
				throw new DCMAException("Converted Tiff files count not equal to the the TIFF pages count.");
			}
		}
	}

	private void deleteTempFiles(final List<File> deleteFileList) {
		if (deleteFileList != null) {
			LOGGER.info("Cleaning up the old pdf and tiff files.");
			for (File file : deleteFileList) {
				boolean isDeleted = file.delete();
				LOGGER.debug(" File " + file.getAbsolutePath() + " deleted " + isDeleted);
			}
		}
	}

	private void processInputTiffFiles(final String folderPath, final BatchClass batchClass, final String[] fdIgList,
			final List<File> deleteFileList, final BatchInstanceThread threadList, final File folder) throws DCMAException {
		String[] tiffFolderList = folder.list(new CustomFileFilter(false, FileType.TIF.getExtension(), FileType.TIFF.getExtension()));
		boolean isFound = false;
		if (tiffFolderList != null) {
			for (String fileName : tiffFolderList) {

				if (fileName == null || fileName.isEmpty()) {
					continue;
				}

				try {
					isFound = false;
					LOGGER.info("File Name = " + fileName);
					File fileOriginal = new File(folderPath + File.separator + fileName);
					File fileNew = null;
					for (String nameStr : fdIgList) {
						if (fileName.contains(nameStr)) {
							isFound = true;
							fileName = fileName.replaceAll(nameStr, getIgnoreReplaceChar());
						}
					}

					if (isFound) {
						fileNew = new File(folderPath + File.separator + fileName);
						fileOriginal.renameTo(fileNew);
						LOGGER.info("Converting multi page tiff file : " + fileNew.getAbsolutePath());
						imageProcessService.convertPdfOrMultiPageTiffToTiff(batchClass, fileNew, null, threadList, false);
						deleteFileList.add(fileNew);
					} else {
						LOGGER.info("Converting multi page tiff file : " + fileOriginal.getAbsolutePath());
						imageProcessService.convertPdfOrMultiPageTiffToTiff(batchClass, fileOriginal, null, threadList, false);
						deleteFileList.add(fileOriginal);
					}
				} catch (Exception e) {
					LOGGER.error("Error in converting multi page tiff file to single page tiff files", e);
					throw new DCMAException("Error in breaking image to single pages " + e.getMessage(), e);
				}
			}
			try {
				LOGGER.info("Executing conversion of multi page tiff file using thread pool");
				threadList.execute();
				LOGGER.info("Completed conversion of multi page tiff file using thread pool");
			} catch (DCMAApplicationException e) {
				LOGGER.error(e.getMessage(), e);
				throw new DCMAException(e.getMessage(), e);
			}
		}
	}

	private void processInputPDFFiles(final String folderPath, BatchClass batchClass, File folder, String[] fdIgList,
			final List<File> deleteFileList, BatchInstanceThread threadList) throws DCMAException {
		boolean isFound = false;
		String[] pdfFolderList = folder.list(new CustomFileFilter(false, FileType.PDF.getExtension()));
		if (pdfFolderList != null) {

			for (String fileName : pdfFolderList) {

				if (fileName == null || fileName.isEmpty()) {
					continue;
				}

				try {
					isFound = false;
					File fileOriginal = new File(folderPath + File.separator + fileName);
					LOGGER.info("File Name = " + fileName);
					File fileNew = null;
					for (String nameStr : fdIgList) {
						if (fileName.contains(nameStr)) {
							isFound = true;
							fileName = fileName.replaceAll(nameStr, getIgnoreReplaceChar());
						}
					}

					if (isFound) {
						fileNew = new File(folderPath + File.separator + fileName);
						fileOriginal.renameTo(fileNew);
						LOGGER.info("Converting multi page pdf file : " + fileNew.getAbsolutePath());
						imageProcessService.convertPdfToSinglePageTiffs(batchClass, fileNew, threadList);
						deleteFileList.add(fileNew);
					} else {
						LOGGER.info("Converting multi page pdf file : " + fileOriginal.getAbsolutePath());
						imageProcessService.convertPdfToSinglePageTiffs(batchClass, fileOriginal, threadList);
						deleteFileList.add(fileOriginal);
					}
				} catch (Exception e) {
					LOGGER.error("Error in converting multi page pdf file to mutli page tiff file", e);
					throw new DCMAException("Error in converting pdf file to multi page tiff file" + e.getMessage(), e);
				}
			}
			try {
				LOGGER.info("Executing conversion of multi page pdf file using thread pool");
				threadList.execute();
				LOGGER.info("Completed conversion of multi page pdf file using thread pool");
			} catch (DCMAApplicationException e) {
				LOGGER.error(e.getMessage(), e);
				throw new DCMAException(e.getMessage(), e);
			}
		}
	}

		private int getTiffPagesCount(final File folder) {
		LOGGER.info("Inside getTiffFilesCount ....");
		int tiffFileCount = 0;
		File[] pdfFileList = folder.listFiles(new CustomFileFilter(false, FileType.PDF.getExtension()));
		File[] tiffFilesList = folder
				.listFiles(new CustomFileFilter(false, FileType.TIF.getExtension(), FileType.TIFF.getExtension()));
		if (pdfFileList != null) {
			for (File file : pdfFileList) {
				tiffFileCount += PDFUtil.getPDFPageCount(file.getAbsolutePath());
			}
		}
		if (tiffFilesList != null) {
			for (File file : tiffFilesList) {
				tiffFileCount += TIFFUtil.getTIFFPageCount(file.getAbsolutePath());
			}
		}
		LOGGER.info("Exiting getTiffFilesCount ....");
		return tiffFileCount;
	}

}
