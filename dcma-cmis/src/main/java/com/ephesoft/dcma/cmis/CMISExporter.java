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

package com.ephesoft.dcma.cmis;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.Policy;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.Ace;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Batch.Documents;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.cmis.constant.CMISExportConstant;
import com.ephesoft.dcma.cmis.factory.CMISSessionFactory;
import com.ephesoft.dcma.cmis.impl.BasicCMISSession;
import com.ephesoft.dcma.cmis.impl.OAuthCMISSession;
import com.ephesoft.dcma.cmis.impl.WebServiceCMISSession;
import com.ephesoft.dcma.cmis.service.AbstractUploadFile;
import com.ephesoft.dcma.cmis.service.UploadPdfFile;
import com.ephesoft.dcma.cmis.service.UploadTifFile;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.service.BatchInstanceService;

/**
 * This class is responsible for uploading all the output files to the repository folder. This will reads the batch.xml file. It finds
 * the names of multi page tif and pdf files from the batch.xml. Then it upload these files to the repository main root folder. At a
 * time it will upload only pdf or tif files.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.cmis.service.CMISExportServiceImpl
 */

public class CMISExporter implements ICommonConstants {

	/**
	 * Logger instance for logging.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CMISExporter.class);

	/**
	 * An instance of {@link CMISDocumentDetails}.
	 */
	@Autowired
	private CMISDocumentDetails documentDetails;
	/**
	 * Instance of {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

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
	private PluginPropertiesService batchInstancePluginPropertiesService;

	@Autowired
	private CMISSessionFactory cmisSessionFactory;

	/**
	 * getter for batchSchemaService.
	 * 
	 * @return the {@link BatchSchemaService}.
	 */
	public BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * setter for batchSchemaService.
	 * 
	 * @param batchSchemaService {@link BatchSchemaService}
	 */
	public void setBatchSchemaService(BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * getter for batchInstanceService.
	 * 
	 * @return {@link BatchInstanceService}
	 */
	public BatchInstanceService getBatchInstanceService() {
		return batchInstanceService;
	}

	/**
	 * setter for batchInstanceService.
	 * 
	 * @param batchInstanceService {@link BatchInstanceService}
	 */
	public void setBatchInstanceService(BatchInstanceService batchInstanceService) {
		this.batchInstanceService = batchInstanceService;
	}

	/**
	 * A private string.
	 */
	private String repoCreateBatchFolder;

	/**
	 * getter for repoCreateBatchFolder.
	 * 
	 * @return {@link String}
	 */
	public String getRepoCreateBatchFolder() {
		return repoCreateBatchFolder;
	}

	/**
	 * setter for repoCreateBatchFolder.
	 * 
	 * @param repoCreateBatchFolder {@link String}
	 */
	public void setRepoCreateBatchFolder(String repoCreateBatchFolder) {
		this.repoCreateBatchFolder = repoCreateBatchFolder;
	}

	/**
	 * setter for documentDetails.
	 * 
	 * @param documentDetails {@link CMISDocumentDetails}
	 */
	public void setDocumentDetails(CMISDocumentDetails documentDetails) {
		this.documentDetails = documentDetails;
	}

	/**
	 * getter for documentDetails.
	 * 
	 * @return {@link CMISDocumentDetails}
	 */
	public CMISDocumentDetails getDocumentDetails() {
		return documentDetails;
	}

	/**
	 * This method tests cmis export plug in properties for connection with repository.
	 * 
	 * @param pluginPropertyValues {@link Map<String, String>}
	 * @return map {@link Map<String, String>}
	 * @throws JAXBException {@link JAXBException}
	 * @throws DCMAApplicationException {@link DCMAApplicationException} If not able to connect to repository server. If invalid input
	 *             parameters.
	 */
	public Map<String, String> cmisConnectionTest(final Map<String, String> pluginPropertyValues) throws JAXBException, DCMAApplicationException {
		LOGGER.info("CMIS export plugin connection tester.");
		LOGGER.info("Initializing properties...");

		Map<String, String> map = new HashMap<String, String>();

		// Getting cmis configuration properties
		if (pluginPropertyValues == null || pluginPropertyValues.isEmpty()) {
			throw new DCMAApplicationException(CMISExportConstant.CMIS_CONNECTION_EMPTY_PROPERTIES);
		}

		String serverURL = pluginPropertyValues.get(CMISProperties.CMIS_SERVER_URL.getPropertyKey());
		LOGGER.info("server URL " + serverURL);
		validateServerUrl(serverURL);

		String serverUserName = pluginPropertyValues.get(CMISProperties.CMIS_SERVER_USER_NAME.getPropertyKey());
		LOGGER.info("server user name " + serverUserName);
		validateServerUserName(serverUserName);

		String serverPassword = pluginPropertyValues.get(CMISProperties.CMIS_SERVER_PASSWORD.getPropertyKey());
		LOGGER.info("server password " + serverPassword);
		validateServerPassword(serverPassword);

		String repositoryID = pluginPropertyValues.get(CMISProperties.CMIS_REPOSITORY_ID.getPropertyKey());
		LOGGER.info("server repository Id " + repositoryID);

		String alfrescoAspectSwitch = pluginPropertyValues.get(CMISProperties.CMIS_ASPECTS_SWITCH.getPropertyKey());
		LOGGER.info("alfrescoAspectSwitch " + alfrescoAspectSwitch);

		String clientKey = pluginPropertyValues.get(CMISProperties.CMIS_CLIENT_KEY.getPropertyKey());
		String secretKey = pluginPropertyValues.get(CMISProperties.CMIS_SECRET_KEY.getPropertyKey());
		String refreshToken = pluginPropertyValues.get(CMISProperties.CMIS_REFRESH_TOKEN.getPropertyKey());
		String network = pluginPropertyValues.get(CMISProperties.CMIS_NETWROK.getPropertyKey());
		LOGGER.info("server password " + serverPassword);

		CMISSession cmisSession = cmisSessionFactory.getImplementation();
		if (cmisSession instanceof BasicCMISSession) {
			BasicCMISSession basicCMISSession = (BasicCMISSession) cmisSession;
			basicCMISSession.setAlfrescoAspectSwitch(alfrescoAspectSwitch);
			basicCMISSession.setRepositoryID(repositoryID);
			basicCMISSession.setServerPassword(serverPassword);
			basicCMISSession.setServerURL(serverURL);
			basicCMISSession.setServerUserName(serverUserName);
			map = basicCMISSession.getSession();
		} else if (cmisSession instanceof WebServiceCMISSession) {
			WebServiceCMISSession webServiceCMISSession = (WebServiceCMISSession) cmisSession;
			webServiceCMISSession.setAlfrescoAspectSwitch(alfrescoAspectSwitch);
			webServiceCMISSession.setRepositoryID(repositoryID);
			webServiceCMISSession.setServerPassword(serverPassword);
			webServiceCMISSession.setServerURL(serverURL);
			webServiceCMISSession.setServerUserName(serverUserName);
			map = webServiceCMISSession.getSession();
		} else if (cmisSession instanceof OAuthCMISSession) {
			OAuthCMISSession oAuthCMISSession = (OAuthCMISSession) cmisSession;
			oAuthCMISSession.setClientKey(clientKey);
			oAuthCMISSession.setNetwork(network);
			oAuthCMISSession.setRefreshToken(refreshToken);
			oAuthCMISSession.setSecretKey(secretKey);
			map = oAuthCMISSession.getSession();
		}
		return map;
	}

	/**
	 * This method reads the batch.xml file. It finds the names of multi page tif and pdf files from the batch.xml. Then it upload
	 * these files to the repository main root folder. At a time it will upload only pdf or tif files.
	 * 
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @throws JAXBException {@link JAXBException}
	 * @throws DCMAApplicationException If not able to upload files to repository server. If invalid input parameters.
	 */
	public void exportFiles(String batchInstanceIdentifier) throws JAXBException, DCMAApplicationException {
		LOGGER.info("CMIS export plugin.");
		LOGGER.info("Initializing properties...");
		String isCMISON = batchInstancePluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				CMISExportConstant.CMIS_EXPORT_PLUGIN, CMISProperties.CMIS_SWITCH);
		if (isCMISON == null || !isCMISON.equals(CMISExportConstant.ON_STRING)) {
			return;
		}
		String rootFolder = batchInstancePluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				CMISExportConstant.CMIS_EXPORT_PLUGIN, CMISProperties.CMIS_ROOT_FOLDER);
		validateRootFolder(rootFolder);
		String serverURL = batchInstancePluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				CMISExportConstant.CMIS_EXPORT_PLUGIN, CMISProperties.CMIS_SERVER_URL);
		validateServerUrl(serverURL);
		String serverUserName = batchInstancePluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				CMISExportConstant.CMIS_EXPORT_PLUGIN, CMISProperties.CMIS_SERVER_USER_NAME);
		validateServerUserName(serverUserName);
		String serverPassword = batchInstancePluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				CMISExportConstant.CMIS_EXPORT_PLUGIN, CMISProperties.CMIS_SERVER_PASSWORD);
		validateServerPassword(serverPassword);
		String uploadFileTypeExt = batchInstancePluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				CMISExportConstant.CMIS_EXPORT_PLUGIN, CMISProperties.CMIS_UPLOAD_FILE_EXT);
		validateUploadFileTypeText(uploadFileTypeExt);
		String alfrescoAspectSwitch = batchInstancePluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				CMISExportConstant.CMIS_EXPORT_PLUGIN, CMISProperties.CMIS_ASPECTS_SWITCH);

		try {
			CMISSession cmisSession = cmisSessionFactory.getImplementation();

			Session session = cmisSession.getSession(batchInstanceIdentifier);

			Folder batchInstanceFolder = getBatchInstanceFolder(session, rootFolder, batchInstanceIdentifier);
			final String batchInstanceSystemFolder = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceIdentifier);
			LOGGER.info("System folder for batch instance: " + batchInstanceIdentifier + " is : " + batchInstanceSystemFolder);
			String sFolderToBeExported = batchInstanceSystemFolder + File.separator + batchInstanceIdentifier;
			Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);
			String batchClassIdentifier = batch.getBatchClassIdentifier();

			Documents documents = batch.getDocuments();
			if (documents == null) {
				throw new DCMABusinessException("Document type can not be null.");
			}
			List<Document> listOfDocuments = documents.getDocument();
			if (listOfDocuments == null) {
				throw new DCMABusinessException("Document type list can not be null.");
			}

			for (Document document : listOfDocuments) {
				uploadFile(batchInstanceIdentifier, rootFolder, uploadFileTypeExt, alfrescoAspectSwitch, session, batchInstanceFolder,
						sFolderToBeExported, batchClassIdentifier, document);
			}

		} catch (CmisBaseException e) {
			String errorContent = ((CmisBaseException) e).getErrorContent();
			if (null != errorContent) {
				String errorText = AbstractUploadFile.getTextFromHtmlString(errorContent);
				LOGGER.error(CMISExportConstant.CUSTOM_ERROR_MESSAGE_FROM_CMIS_PLUGIN + errorText);
			}
			throw new DCMAApplicationException(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new DCMAApplicationException(e.getMessage(), e);
		} finally {
			AbstractUploadFile.resetAspectProperties(getDocumentDetails());
		}
	}

	private Folder getBatchInstanceFolder(Session session, String rootFolder, String batchInstanceIdentifier) {

		// test
		// ****************************************************************************************
		// BEGIN: Zia Consulting Enhancement
		// ****************************************************************************************
		// Get a handle to the repository root folder.
		Folder root = session.getRootFolder();

		// Split the specified repository folder path into its individual parts using "/" as the
		// delimeter.
		String[] folderPathList = rootFolder.split(CMISExportConstant.FOLDER_SEPARATOR);

		// Determine if there is an empty string in the first index of the folder path list.
		// This happens because split inserts an empty string if the first character is "/".
		// In older Ephesoft world, however, one doesn't specified a leading "/" because
		// the connector couldn't write to a nested folder. Therefore, we have to handle
		// both new and old scenarios.
		int startIndex = getStartIndex(folderPathList);
		// Get a handle to the target folder. If it doesn't exist, then it will be created.
		Folder mainFolder = checkCreateFolder(session, root, CMISExportConstant.FOLDER_SEPARATOR, startIndex, folderPathList);
		// Determine if a batch specific folder should be created within the main folder.
		Folder batchInstanceFolder = null;
		if (this.isBatchInSubfolder()) {
			// The batch files are to be placed within a subfolder. See if this folder exists, or otherwise create it.
			String[] batchFolderPathList = (rootFolder + CMISExportConstant.FOLDER_SEPARATOR + batchInstanceIdentifier)
					.split(CMISExportConstant.FOLDER_SEPARATOR);
			batchInstanceFolder = this.checkCreateFolder(session, mainFolder, CMISExportConstant.FOLDER_SEPARATOR + rootFolder,
					batchFolderPathList.length - 1, batchFolderPathList);
		} else {
			// The batch files are not to be placed within a subfolder. Therefore, the main folder
			// is the batch instance folder.
			batchInstanceFolder = mainFolder;
		}

		if (null == batchInstanceFolder) {
			ObjectId batchInstanceFolderID = createBatchInstanceFolder(session, mainFolder, batchInstanceIdentifier);
			if (batchInstanceFolderID == null) {
				LOGGER.error("Unable to create batch instance folder.");
				throw new DCMABusinessException("Batch Instance folder can not be created.");
			} else {
				// get an object
				CmisObject object = session.getObject(batchInstanceFolderID);
				if (object instanceof Folder) {
					batchInstanceFolder = (Folder) object;
					LOGGER.info("batchInstance folder : " + batchInstanceFolder);
					LOGGER.info("batchInstance folder ID : " + batchInstanceFolderID);
				}
			}
		}

		if (null == batchInstanceFolder) {
			throw new DCMABusinessException("Batch Instance folder can not be created.");
		}

		return batchInstanceFolder;
	}

	private int getStartIndex(String[] folderPathList) {
		int startIndex = 0;
		if (folderPathList[0].length() == 0) {
			startIndex += 1;
		}
		return startIndex;
	}

	private void validateUploadFileTypeText(String uploadFileTypeExt) throws DCMAApplicationException {
		if (null == uploadFileTypeExt || "".equals(uploadFileTypeExt)) {
			throw new DCMAApplicationException(
					"UploadFileTypeExt is null/empty from the data base. Invalid initializing of properties.");
		}
	}

	private void validateServerPassword(String serverPassword) throws DCMAApplicationException {
		if (null == serverPassword || "".equals(serverPassword)) {
			throw new DCMAApplicationException(
					"Server User Password is null/empty from the data base. Invalid initializing of properties.");
		}
	}

	private void validateServerUserName(String serverUserName) throws DCMAApplicationException {
		if (null == serverUserName || "".equals(serverUserName)) {
			throw new DCMAApplicationException(
					"Server User Name is null/empty from the data base. Invalid initializing of properties.");
		}
	}

	private void validateServerUrl(String serverURL) throws DCMAApplicationException {
		if (null == serverURL || "".equals(serverURL)) {
			throw new DCMAApplicationException("Server Url is null/empty from the data base. Invalid initializing of properties.");
		}
	}

	private void validateRootFolder(String rootFolder) throws DCMAApplicationException {
		if (null == rootFolder || "".equals(rootFolder)) {
			throw new DCMAApplicationException("RootFolder is null/empty from the data base. Invalid initializing of properties.");
		}
	}

	private void uploadFile(String batchInstanceIdentifier, String rootFolder, String uploadFileTypeExt, String alfrescoAspectSwitch,
			Session session, Folder batchInstanceFolder, String sFolderToBeExported, String batchClassIdentifier, Document document)
			throws DCMAApplicationException {
		String fileName = uploadFileTypeExt;

		String cmisFileName = batchInstancePluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				CMISExportConstant.CMIS_EXPORT_PLUGIN, CMISProperties.CMIS_FILE_NAME);
		if (null == fileName) {
			throw new DCMAApplicationException("Upload file extenstion is null. Illegel input in the properties file for"
					+ " the upload file extension. Only 'pdf' or 'tif' is allowed.");
		}

		// creating the document inside the batch instance folder.

		String fileNameFormatArr[] = null;

		if (cmisFileName != null && !cmisFileName.isEmpty()) {
			fileNameFormatArr = cmisFileName.split(CMISExportConstant.FILE_FORMAT_SEPARATOR);
		}

		String updatedCmisFileName = null;

		if (fileNameFormatArr != null) {
			updatedCmisFileName = getUpdatedFileName(batchInstanceIdentifier, document.getIdentifier(), fileNameFormatArr, document
					.getDocumentLevelFields().getDocumentLevelField());
		}

		AbstractUploadFile uploadFile;
		AbstractUploadFile.setBatchSchemaService(batchSchemaService);
		if (fileName.equalsIgnoreCase(CMISExportConstant.UPLOADEXT.PDF.getUploadFileExt())) {
			uploadFile = new UploadPdfFile();
		} else {
			if (fileName.equalsIgnoreCase(CMISExportConstant.UPLOADEXT.TIF.getUploadFileExt())) {
				uploadFile = new UploadTifFile();
			} else {
				throw new DCMAApplicationException("Illegel input in the properties file for the upload file extension."
						+ " Only 'pdf' or 'tif' is allowed.");
			}
		}

		uploadFile.uploadFile(batchInstanceIdentifier, rootFolder, alfrescoAspectSwitch, session, batchInstanceFolder,
				sFolderToBeExported, batchClassIdentifier, document, updatedCmisFileName, getDocumentDetails());
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
			if (fileFormat.startsWith(CMISExportConstant.PARAM_START_DELIMETER)) {
				fileFormat = fileFormat.substring(1);
				if (fileFormat.equalsIgnoreCase(CMISExportConstant.EPHESOFT_BATCH_ID)) {
					isValidParamForFileName = true;
					updatedFileName.append(batchInstanceID);
				} else if (fileFormat.equalsIgnoreCase(CMISExportConstant.EPHESOFT_DOCUMENT_ID)) {
					isValidParamForFileName = true;
					updatedFileName.append(documentIdentifier);
				} else {
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
		}
		LOGGER.info("Updated file name: " + updatedFileName);
		LOGGER.info("Exiting getUpdatedFileName method.");
		return updatedFileName.toString();
	}

	private String getDlfValue(final List<DocField> docFieldList, final String dlfName) {
		LOGGER.info("Entering getDlfValue method.");
		String dlfValue = null;
		boolean dlfFound = false;
		LOGGER.info("Get value for dlf: " + dlfName);
		if (docFieldList != null) {
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
		}
		LOGGER.info("Dlf found: " + dlfFound);
		LOGGER.info("Exiting getDlfValue method.");
		return dlfValue;
	}

	private ObjectId createFolder(Session session, ObjectId parentId, String folderName) {
		ObjectId mainFolderID = null;

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PropertyIds.OBJECT_TYPE_ID, BaseTypeId.CMIS_FOLDER.value());

		properties.put(PropertyIds.NAME, folderName);

		List<Ace> addAces = new LinkedList<Ace>();
		List<Ace> removeAces = new LinkedList<Ace>();
		List<Policy> policies = new LinkedList<Policy>();

		// create the folder if it doesn't exists
		mainFolderID = session.createFolder(properties, parentId, policies, addAces, removeAces);
		// session.save();

		return mainFolderID;
	}

	private Folder checkCreateFolder(Session session, Folder parentFolder, String parentFolderPath, int currentFolderIndex,
			String[] folderPathList) {
		// ****************************************************************************************
		// BEGIN: Zia Consulting Enhancement
		// Modified to support a nested folder structure within the repository rather than simply
		// a root level folder. Recursion is employed in order to walk the folder tree.
		// ****************************************************************************************
		Folder targetFolder = null;

		// Get the name of the folder at the current index in the path.
		String currentFolderName = folderPathList[currentFolderIndex];

		LOGGER.info("Determining if the target folder \"" + currentFolderName + "\" exists within the repository folder \""
				+ parentFolderPath + "\".");

		// Build a proper path to the folder.
		String subfolderPath = parentFolderPath;
		StringBuilder subfolderPathString = new StringBuilder();
		subfolderPathString.append(subfolderPath);
		if (!parentFolderPath.equals(CMISExportConstant.FOLDER_SEPARATOR)) {
			// subfolderPath += FOLDER_SEPARATOR;
			subfolderPathString.append(CMISExportConstant.FOLDER_SEPARATOR);
		}
		// subfolderPath += currentFolderName;
		subfolderPathString.append(currentFolderName);
		subfolderPath = subfolderPathString.toString();

		try {
			// Determine if the folder already exists.
			CmisObject folderCmisObj = session.getObjectByPath(subfolderPath);

			if (folderCmisObj != null) {
				// The folder does exist, so use it.
				LOGGER.info("Folder already present");
				LOGGER.info("Found the child folder. Its folder ID is " + folderCmisObj.getId() + CMISExportConstant.DOT);
				targetFolder = (Folder) folderCmisObj;
			}
		} catch (Exception objEx) {
			LOGGER.error(objEx.getMessage(), objEx);
		}

		// If no folder exists, then have it created.
		if (targetFolder == null) {
			LOGGER.info("The folder doesn't exist. Creating it...");

			try {
				// Have the folder created.
				ObjectId newFolderID = this.createFolder(session, session.createObjectId(parentFolder.getId()), currentFolderName);

				// Convert the object ID to a folder reference.
				targetFolder = (Folder) session.getObject(newFolderID);
			} catch (Exception createEx) {
				LOGGER.error("An error occurred while attempting to create the folder node \"" + currentFolderName
						+ "\" within the parent folder node \"" + currentFolderName + "\". The error was: " + createEx.getMessage());
			}
		}

		if (targetFolder != null) {
			// The folder does exist, but we need to determine if we are at the end of the chain.
			int newFolderIndex = currentFolderIndex + 1;
			if (newFolderIndex < folderPathList.length) {
				// We are not at the end of the tree. Iterate to the next node in the path.
				targetFolder = this.checkCreateFolder(session, targetFolder, subfolderPath, newFolderIndex, folderPathList);
			}
			// Else this is the end of the line.
		}
		// ****************************************************************************************
		// END: Zia Consulting Enhancement
		// ****************************************************************************************

		return targetFolder;
	} // End checkCreateFolder

	private Folder checkBatchInstanceFolder(Folder mainFolder, String batchInstanceID) {
		String folderId = null;
		CmisObject folderObj = null;
		for (CmisObject childrens : mainFolder.getChildren()) {
			if (childrens.getName().equals(batchInstanceID)) {
				LOGGER.info("Folder already present");
				folderId = childrens.getId();
				folderObj = childrens;
				LOGGER.info("folderId : " + folderId);
				break;
			}
		}

		Folder folder = null;

		if (folderObj instanceof Folder) {
			folder = (Folder) folderObj;
		}

		return folder;

	}

	private ObjectId createBatchInstanceFolder(Session session, Folder folder, String batchInstanceID) {
		ObjectId mainFolderID = session.createObjectId(folder.getId());
		ObjectId batchInstanceFolderID = null;
		try {
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(PropertyIds.OBJECT_TYPE_ID, BaseTypeId.CMIS_FOLDER.value());
			properties.put(PropertyIds.NAME, batchInstanceID);

			List<Ace> addAces = new LinkedList<Ace>();
			List<Ace> removeAces = new LinkedList<Ace>();
			List<Policy> policies = new LinkedList<Policy>();

			// create the folder if it doesn't exists
			batchInstanceFolderID = session.createFolder(properties, mainFolderID, policies, addAces, removeAces);
			// session.save();
			LOGGER.info("Batch instance folder created successfully : " + batchInstanceFolderID);
		} catch (CmisBaseException e) {
			String errorContent = ((CmisBaseException) e).getErrorContent();
			if (null != errorContent) {
				String errorText = AbstractUploadFile.getTextFromHtmlString(errorContent);
				LOGGER.error(CMISExportConstant.CUSTOM_ERROR_MESSAGE_FROM_CMIS_PLUGIN + errorText);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return batchInstanceFolderID;
	}

	private boolean isBatchInSubfolder() {
		boolean batchInSubfolder = true;

		try {
			// Get the value of the configuration property.
			String batchInSubfolderString = getRepoCreateBatchFolder();

			if ((batchInSubfolderString != null) && (!batchInSubfolderString.trim().isEmpty())
					&& (batchInSubfolderString.trim().equalsIgnoreCase(CMISExportConstant.FALSE))) {
				// Batches do not get their own subfolders.
				batchInSubfolder = false;
			}
		} catch (Exception configEx) {
			LOGGER.info("Ignore exceptions related to this feature, as this is not a required behavior");

			// Ignore exceptions related to this feature, as this is not a required behavior.
		}
		LOGGER.info("value of property:cmis.repo.create_batch_subfolders is:" + batchInSubfolder);
		return batchInSubfolder;
	} // End isBatchInSubfolder

	/**
	 * This method deletes the file for a batch folder whose extension is set in property of batch.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @throws DCMAApplicationException if any exception occurs.
	 */
	public void deleteDocFromRepository(String batchInstanceIdentifier) throws DCMAApplicationException {
		String isCMISON = batchInstancePluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				CMISExportConstant.CMIS_EXPORT_PLUGIN, CMISProperties.CMIS_SWITCH);

		if (isCMISON == null || !isCMISON.equals("ON")) {
			return;
		}

		String rootFolder = batchInstancePluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				CMISExportConstant.CMIS_EXPORT_PLUGIN, CMISProperties.CMIS_ROOT_FOLDER);
		String uploadFileTypeExt = batchInstancePluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				CMISExportConstant.CMIS_EXPORT_PLUGIN, CMISProperties.CMIS_UPLOAD_FILE_EXT);
		try {
			Session session = null;

			CMISSession cmisSession = cmisSessionFactory.getImplementation();

			session = cmisSession.getSession(batchInstanceIdentifier);

			// ****************************************************************************************
			// BEGIN: Zia Consulting Enhancement
			// ****************************************************************************************
			// Get a handle to the repository root folder.
			Folder root = session.getRootFolder();

			// Split the specified repository folder path into its individual parts using "/" as the
			// delimeter.
			String[] folderPathList = rootFolder.split(CMISExportConstant.FOLDER_SEPARATOR);

			// Determine if there is an empty string in the first index of the folder path list.
			// This happens because split inserts an empty string if the first character is "/".
			// In older Ephesoft world, however, one doesn't specified a leading "/" because
			// the connector couldn't write to a nested folder. Therefore, we have to handle
			// both new and old scenarios.
			int startIndex = 0;
			if (folderPathList[0].length() == 0) {
				startIndex += 1;
			}

			// Get a handle to the target folder. If it doesn't exist, then it will be created.
			// We start at index 1, because split inserts and empty string into index 0.
			Folder mainFolder = checkCreateFolder(session, root, CMISExportConstant.FOLDER_SEPARATOR, startIndex, folderPathList);

			// Determine if the batch file was placed within a batch sub-folder
			Folder batchInstanceFolder = null;
			if (this.isBatchInSubfolder()) {
				// Batch files are to be placed within a sub-folder. Get a handle to that folder.
				batchInstanceFolder = checkBatchInstanceFolder(mainFolder, batchInstanceIdentifier);

				if (batchInstanceFolder == null) {
					throw new Exception("Unable to locate the batch folder \"" + rootFolder + CMISExportConstant.FOLDER_SEPARATOR
							+ batchInstanceIdentifier + "\" within the repository.");
				}
			} else {
				// Batch files are not to be written to sub-folder. Therefore, the configured root folder
				// is the target folder.
				batchInstanceFolder = mainFolder;
			}
			// ****************************************************************************************
			// END: Zia Consulting Enhancement
			// ****************************************************************************************

			for (CmisObject childrens : batchInstanceFolder.getChildren()) {
				if (childrens.getName().contains(uploadFileTypeExt)) {
					LOGGER.debug(childrens.getName());
				}
				childrens.delete(true);
			}

		} catch (CmisBaseException e) {
			String errorContent = ((CmisBaseException) e).getErrorContent();
			if (null != errorContent) {
				String errorText = AbstractUploadFile.getTextFromHtmlString(errorContent);
				LOGGER.error(CMISExportConstant.CUSTOM_ERROR_MESSAGE_FROM_CMIS_PLUGIN + errorText);
			}
			throw new DCMAApplicationException(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new DCMAApplicationException(e.getMessage(), e);
		}
	}

}
