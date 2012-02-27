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

package com.ephesoft.dcma.cmis;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.Policy;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.Ace;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Batch.Documents;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.service.BatchInstanceService;

/**
 * This class is responsible for uploading all the output files to the repository folder. This will reads the batch.xml file. It finds
 * the names of multipage tif and pdf files from the batch.xml. Then it upload these files to the repository main root folder. At a
 * time it will upload only pdf or tif files.
 * 
 * @author Ephesoft
 * 
 * @version 1.0
 * @see com.ephesoft.dcma.cmis.service.CMISExportServiceImpl
 */
@Component
public class CMISExporter implements ICommonConstants {

	private static final String CONVERTING = "Converting";

	private static final String CMIS_EXPORT_PLUGIN = "CMIS_EXPORT";

	private static final Logger LOGGER = LoggerFactory.getLogger(CMISExporter.class);

	private static final String IMAGE_MIME_TYPE = "image/tiff";

	private static final String PDF_MIME_TYPE = "application/pdf";

	private static final String EXCEPTION_MESSAGE_CONSTANT_1 = "The WSDL URL of the CMIS Navigation Service must be specified as the value of the \"dcma-cmis.properties\"configuration file property \" ";
	private static final String EXCEPTION_MESSAGE_CONSTANT_2 = " when WS-Security is specified as the CMIS security mode.HTTP Basic Authentication will be used by default.";
	private static final String SERVER_URL = "{serverURL}";
	private static final String FALSE = "false";
	/**
	 * Instance of BatchInstanceService.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * Instance of BatchSchemaService.
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
	 * @return the batchInstanceService
	 */
	public BatchInstanceService getBatchInstanceService() {
		return batchInstanceService;
	}

	/**
	 * @param batchInstanceService the batchInstanceService to set
	 */
	public void setBatchInstanceService(BatchInstanceService batchInstanceService) {
		this.batchInstanceService = batchInstanceService;
	}

	/**
	 * plugin mapping file name.
	 */
	private String pluginMappingFileName;

	/**
	 * document versioning state name.
	 */
	private String documentVersioningState;

	private String securityMode;

	private String repoCreateBatchFolder;

	public String getSecurityMode() {
		return securityMode;
	}

	public void setSecurityMode(String securityMode) {
		this.securityMode = securityMode;
	}

	public String getUrlAclService() {
		return urlAclService;
	}

	public void setUrlAclService(String urlAclService) {
		this.urlAclService = urlAclService;
	}

	public String getUrlDiscoveryService() {
		return urlDiscoveryService;
	}

	public void setUrlDiscoveryService(String urlDiscoveryService) {
		this.urlDiscoveryService = urlDiscoveryService;
	}

	public String getUrlMultifilingService() {
		return urlMultifilingService;
	}

	public void setUrlMultifilingService(String urlMultifilingService) {
		this.urlMultifilingService = urlMultifilingService;
	}

	public String getUrlNavigationService() {
		return urlNavigationService;
	}

	public void setUrlNavigationService(String urlNavigationService) {
		this.urlNavigationService = urlNavigationService;
	}

	public String getUrlObjectService() {
		return urlObjectService;
	}

	public void setUrlObjectService(String urlObjectService) {
		this.urlObjectService = urlObjectService;
	}

	public String getUrlPolicyService() {
		return urlPolicyService;
	}

	public void setUrlPolicyService(String urlPolicyService) {
		this.urlPolicyService = urlPolicyService;
	}

	public String getUrlRelationshipService() {
		return urlRelationshipService;
	}

	public void setUrlRelationshipService(String urlRelationshipService) {
		this.urlRelationshipService = urlRelationshipService;
	}

	public String getUrlRepositoryService() {
		return urlRepositoryService;
	}

	public void setUrlRepositoryService(String urlRepositoryService) {
		this.urlRepositoryService = urlRepositoryService;
	}

	public String getUrlVersioningService() {
		return urlVersioningService;
	}

	public void setUrlVersioningService(String urlVersioningService) {
		this.urlVersioningService = urlVersioningService;
	}

	public String getRepoCreateBatchFolder() {
		return repoCreateBatchFolder;
	}

	public void setRepoCreateBatchFolder(String repoCreateBatchFolder) {
		this.repoCreateBatchFolder = repoCreateBatchFolder;
	}

	private String urlAclService;
	private String urlDiscoveryService;
	private String urlMultifilingService;
	private String urlNavigationService;
	private String urlObjectService;
	private String urlPolicyService;
	private String urlRelationshipService;
	private String urlRepositoryService;
	private String urlVersioningService;

	/**
	 * @return the pluginMappingFileName
	 */
	public String getPluginMappingFileName() {
		return pluginMappingFileName;
	}

	/**
	 * @param pluginMappingFileName the pluginMappingFileName to set
	 */
	public void setPluginMappingFileName(String pluginMappingFileName) {
		this.pluginMappingFileName = pluginMappingFileName;
	}

	private String dateFormat;

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * @param documentVersioningState the Versioning state of the document
	 */
	public void setDocumentVersioningState(String documentVersioningState) {
		this.documentVersioningState = documentVersioningState;
	}

	/**
	 * This method reads the batch.xml file. It finds the names of multipage tif and pdf files from the batch.xml. Then it upload these
	 * files to the repository main root folder. At a time it will upload only pdf or tif files.
	 * 
	 * 
	 * @param batchInstanceIdentifier Long
	 * @throws JAXBException
	 * @throws DCMAApplicationException If not able to upload files to repository server. If invalid input parameters.
	 */
	public void exportFiles(String batchInstanceIdentifier) throws JAXBException, DCMAApplicationException {
		LOGGER.info("CMIS export plugin.");

		LOGGER.info("Initializing properties...");

		String isCMISON = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, CMIS_EXPORT_PLUGIN,
				CMISProperties.CMIS_SWITCH);

		if (isCMISON == null || !isCMISON.equals("ON")) {
			return;
		}

		String rootFolder = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, CMIS_EXPORT_PLUGIN,
				CMISProperties.CMIS_ROOT_FOLDER);
		String serverURL = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, CMIS_EXPORT_PLUGIN,
				CMISProperties.CMIS_SERVER_URL);
		String serverUserName = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, CMIS_EXPORT_PLUGIN,
				CMISProperties.CMIS_SERVER_USER_NAME);
		String serverPassword = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, CMIS_EXPORT_PLUGIN,
				CMISProperties.CMIS_SERVER_PASSWORD);
		String repositoryID = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, CMIS_EXPORT_PLUGIN,
				CMISProperties.CMIS_REPOSITORY_ID);
		String uploadFileTypeExt = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, CMIS_EXPORT_PLUGIN,
				CMISProperties.CMIS_UPLOAD_FILE_EXT);

		/*
		 * String rootFolder = "Ephesoft_test"; String serverURL = "http://172.16.1.38:9090/alfresco/service/cmis"; String
		 * serverUserName = "admin"; String serverPassword = "admin"; String repositoryID = "0e5a3a1c-d22d-457a-a04b-4bf379b868af";
		 * String uploadFileTypeExt = "pdf";
		 */

		if (null == serverURL || "".equals(serverURL)) {
			throw new DCMAApplicationException("Server Url is null/empty from the data base. Invalid initializing of properties.");
		}

		if (null == serverUserName || "".equals(serverUserName)) {
			throw new DCMAApplicationException(
					"Server User Name is null/empty from the data base. Invalid initializing of properties.");
		}

		if (null == serverPassword || "".equals(serverPassword)) {
			throw new DCMAApplicationException(
					"Server User Password is null/empty from the data base. Invalid initializing of properties.");
		}

		if (null == uploadFileTypeExt || "".equals(uploadFileTypeExt)) {
			throw new DCMAApplicationException(
					"UploadFileTypeExt is null/empty from the data base. Invalid initializing of properties.");
		}

		if (null == rootFolder || "".equals(rootFolder)) {
			throw new DCMAApplicationException("RootFolder is null/empty from the data base. Invalid initializing of properties.");
		}

		try {
			Session session = null;

			Map<String, String> sessionParameters = createAtomPubParameters(serverURL, serverUserName, serverPassword, repositoryID);

			// create session
			SessionFactory sessionFactory = SessionFactoryImpl.newInstance();

			if (null == repositoryID || repositoryID.isEmpty()) {
				List<Repository> repositories = sessionFactory.getRepositories(sessionParameters);
				sessionParameters.put(SessionParameter.REPOSITORY_ID, repositories.get(0).getId());
			}

			session = sessionFactory.createSession(sessionParameters);

			// test
			// ****************************************************************************************
			// BEGIN: Zia Consulting Enhancement
			// ****************************************************************************************
			// Get a handle to the repository root folder.
			Folder root = session.getRootFolder();

			// Split the specified repository folder path into its individual parts using "/" as the
			// delimeter.
			String[] folderPathList = rootFolder.split("/");

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
			Folder mainFolder = checkCreateFolder(session, root, "/", startIndex, folderPathList);

			// Determine if a batch specific folder should be created within the main folder.
			Folder batchInstanceFolder = null;
			if (this.isBatchInSubfolder()) {
				// The batch files are to be placed within a subfolder. See if this folder exists, or otherwise create it.
				String[] batchFolderPathList = (rootFolder + "/" + batchInstanceIdentifier).split("/");
				batchInstanceFolder = this.checkCreateFolder(session, mainFolder, rootFolder, batchFolderPathList.length - 1,
						batchFolderPathList);
			} else {
				// The batch files are not to be placed within a subfolder. Therefore, the main folder
				// is the batch instance folder.
				batchInstanceFolder = mainFolder;
			}
			// ****************************************************************************************
			// END: Zia Consulting Enhancement
			// ****************************************************************************************
			/*
			if (null == batchInstanceFolder) {
				ObjectId batchInstanceFolderID = createBatchInstanceFolder(session, mainFolder, batchInstanceIdentifier);
				// get an object
				CmisObject object = session.getObject(batchInstanceFolderID);
				if (object instanceof Folder) {
					batchInstanceFolder = (Folder) object;
					LOGGER.info("batchInstance folder : " + batchInstanceFolder);
					LOGGER.info("batchInstance folder ID : " + batchInstanceFolderID);
				}
			}
			*/
			if (null == batchInstanceFolder) {
				ObjectId batchInstanceFolderID = createBatchInstanceFolder(session, mainFolder, batchInstanceIdentifier);
				// get an object
				CmisObject object = session.getObject(batchInstanceFolderID);
				if (object instanceof Folder) {
					batchInstanceFolder = (Folder) object;
					LOGGER.info("batchInstance folder : " + batchInstanceFolder);
					LOGGER.info("batchInstance folder ID : " + batchInstanceFolderID);
				}
			}

			if (null == batchInstanceFolder) {
				throw new DCMABusinessException("Batch Instance folder can not be created.");
			}

			String sFolderToBeExported = batchSchemaService.getLocalFolderLocation() + File.separator + batchInstanceIdentifier;

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

				String fileName = uploadFileTypeExt;

				if (null == fileName) {
					throw new DCMAApplicationException("Upload file extenstion is null. Illegel input in the properties file for"
							+ " the upload file extension. Only 'pdf' or 'tif' is allowed.");
				}

				// creating the document inside the batch instance folder.

				if (fileName.equalsIgnoreCase(UPLOADEXT.PDF.getUploadFileExt())) {
					String sMultiPagePdf = document.getMultiPagePdfFile();
					if (sMultiPagePdf != null && !sMultiPagePdf.isEmpty()) {
						File fSourcePdfFile = new File(sFolderToBeExported + File.separator + sMultiPagePdf);
						try {
							uploadDocument(session, batchInstanceFolder, fSourcePdfFile, document,
									batchClassIdentifier, true);
						} catch (IOException e) {
							LOGGER.error("Problem uploading PDF file : " + fSourcePdfFile, e);
							throw new DCMAApplicationException("Unable to upload the document : " + fSourcePdfFile, e);
						}
					}
				} else {
					if (fileName.equalsIgnoreCase(UPLOADEXT.TIF.getUploadFileExt())) {
						String sMultiPageTif = document.getMultiPageTiffFile();
						if (sMultiPageTif != null && !sMultiPageTif.isEmpty()) {

							File fSourceTifFile = new File(sFolderToBeExported + File.separator + sMultiPageTif);
							try {
								uploadDocument(session, batchInstanceFolder, fSourceTifFile, document,
										batchClassIdentifier, false);
							} catch (Exception e) {
								LOGGER.error("Problem uploading Tiff file : " + fSourceTifFile, e);
								throw new DCMAApplicationException("Unable to upload the document : " + sMultiPageTif, e);
							}
						}
					} else {
						throw new DCMAApplicationException("Illegel input in the properties file for the upload file extension."
								+ " Only 'pdf' or 'tif' is allowed.");
					}
				}

			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new DCMAApplicationException(e.getMessage(), e);
		}
	}

	/**
	 * @param serverURL
	 * @param serverUserName
	 * @param serverPassword
	 * @param repositoryID
	 * @return Map<String, String>
	 */
	private Map<String, String> createAtomPubParameters(String serverURL, String serverUserName, String serverPassword,
			String repositoryID) {

		Map<String, String> sessionParameters = new HashMap<String, String>();

		// ****************************************************************************************
		// BEGIN: Zia Consulting Enhancement
		// ****************************************************************************************
		// Set the values of the properties that are used for all security modes.
		sessionParameters.put(SessionParameter.USER, serverUserName);
		sessionParameters.put(SessionParameter.PASSWORD, serverPassword);

		// By default, we'll perform HTTP basic security which is what is done today.
		boolean basicSecurity = true;

		String aclServiceURL = null;
		String discoverServiceURL = null;
		String multifilingServiceURL = null;
		String navigationServiceURL = null;
		String objectServiceURL = null;
		String policyServiceURL = null;
		String relationshipServiceURL = null;
		String repositoryServiceURL = null;
		String versioningServiceURL = null;

		try {
			StringBuffer errorMsg = new StringBuffer();
			// Determine if a security mode property was specified in the file.
			String securityMode = getSecurityMode();

			if ((securityMode != null) && (securityMode.trim().isEmpty())) {

				if (securityMode.trim().equalsIgnoreCase("wssecurity")) {
					// We will be using WS-Security.
					basicSecurity = false;

					// Get the URL's for each of the services.
					aclServiceURL = getUrlAclService(); // bundle.getString("cmis.url.acl_service");
					if ((aclServiceURL == null) || (aclServiceURL.trim().length() <= 0)) {

						errorMsg.append(EXCEPTION_MESSAGE_CONSTANT_1);
						errorMsg.append("cmis.url.acl_service");
						errorMsg.append(EXCEPTION_MESSAGE_CONSTANT_2);
						throw new Exception(errorMsg.toString());
					} else {
						// Make sure that it's tidy.
						aclServiceURL = aclServiceURL.trim();

						// Determine if a part of the specified URL needs to be replaced with the server URL
						// configured in the batch classe.
						if (aclServiceURL.contains(SERVER_URL)) {
							aclServiceURL = aclServiceURL.replace(SERVER_URL, serverURL);
						}
					}

					discoverServiceURL = getUrlDiscoveryService(); // bundle.getString("cmis.url.discovery_service");
					if ((discoverServiceURL == null) || (discoverServiceURL.trim().length() <= 0)) {
						errorMsg.setLength(0);
						errorMsg.append(EXCEPTION_MESSAGE_CONSTANT_1);
						errorMsg.append("cmis.url.discovery_service");
						errorMsg.append(EXCEPTION_MESSAGE_CONSTANT_2);
						throw new Exception(errorMsg.toString());
					} else {
						// Make sure that it's tidy.
						discoverServiceURL = discoverServiceURL.trim();

						// Determine if a part of the specified URL needs to be replaced with the server URL
						// configured in the batch classe.
						if (discoverServiceURL.contains(SERVER_URL)) {
							discoverServiceURL = discoverServiceURL.replace(SERVER_URL, serverURL);
						}
					}

					multifilingServiceURL = getUrlMultifilingService(); // bundle.getString("cmis.url.multifiling_service");
					if ((multifilingServiceURL == null) || (multifilingServiceURL.trim().length() <= 0)) {
						errorMsg.setLength(0);
						errorMsg.append(EXCEPTION_MESSAGE_CONSTANT_1);
						errorMsg.append("cmis.url.multifiling_service");
						errorMsg.append(EXCEPTION_MESSAGE_CONSTANT_2);
						throw new Exception(errorMsg.toString());
					} else {
						// Make sure that it's tidy.
						multifilingServiceURL = multifilingServiceURL.trim();

						// Determine if a part of the specified URL needs to be replaced with the server URL
						// configured in the batch classe.
						if (multifilingServiceURL.contains(SERVER_URL)) {
							multifilingServiceURL = multifilingServiceURL.replace(SERVER_URL, serverURL);
						}
					}

					navigationServiceURL = getUrlNavigationService(); // bundle.getString("cmis.url.navigation_service");
					if ((navigationServiceURL == null) || (navigationServiceURL.trim().length() <= 0)) {
						errorMsg.setLength(0);
						errorMsg.append(EXCEPTION_MESSAGE_CONSTANT_1);
						errorMsg.append("cmis.url.navigation_service");
						errorMsg.append(EXCEPTION_MESSAGE_CONSTANT_2);
						throw new Exception(errorMsg.toString()

						);
					} else {
						// Make sure that it's tidy.
						navigationServiceURL = navigationServiceURL.trim();

						// Determine if a part of the specified URL needs to be replaced with the server URL
						// configured in the batch classe.
						if (navigationServiceURL.contains(SERVER_URL)) {
							navigationServiceURL = navigationServiceURL.replace(SERVER_URL, serverURL);
						}
					}

					objectServiceURL = getUrlObjectService(); // bundle.getString("cmis.url.object_service");
					if ((objectServiceURL == null) || (objectServiceURL.trim().length() <= 0)) {
						errorMsg.setLength(0);
						errorMsg.append(EXCEPTION_MESSAGE_CONSTANT_1);
						errorMsg.append("cmis.url.object_service");
						errorMsg.append(EXCEPTION_MESSAGE_CONSTANT_2);
						throw new Exception(errorMsg.toString());
					} else {
						// Make sure that it's tidy.
						objectServiceURL = objectServiceURL.trim();

						// Determine if a part of the specified URL needs to be replaced with the server URL
						// configured in the batch classe.
						if (objectServiceURL.contains(SERVER_URL)) {
							objectServiceURL = objectServiceURL.replace(SERVER_URL, serverURL);
						}
					}

					policyServiceURL = getUrlPolicyService(); // bundle.getString("cmis.url.policy_service");
					if ((policyServiceURL == null) || (policyServiceURL.trim().length() <= 0)) {
						errorMsg.setLength(0);
						errorMsg.append(EXCEPTION_MESSAGE_CONSTANT_1);
						errorMsg.append("cmis.url.policy_service");
						errorMsg.append(EXCEPTION_MESSAGE_CONSTANT_2);
						throw new Exception(errorMsg.toString());
					} else {
						// Make sure that it's tidy.
						policyServiceURL = policyServiceURL.trim();

						// Determine if a part of the specified URL needs to be replaced with the server URL
						// configured in the batch classe.
						if (policyServiceURL.contains(SERVER_URL)) {
							policyServiceURL = policyServiceURL.replace(SERVER_URL, serverURL);
						}
					}

					relationshipServiceURL = getUrlRelationshipService(); // bundle.getString("cmis.url.relationship_service");
					if ((relationshipServiceURL == null) || (relationshipServiceURL.trim().length() <= 0)) {
						errorMsg.setLength(0);
						errorMsg.append(EXCEPTION_MESSAGE_CONSTANT_1);
						errorMsg.append("cmis.url.relationship_service");
						errorMsg.append(EXCEPTION_MESSAGE_CONSTANT_2);
						throw new Exception(errorMsg.toString());
					} else {
						// Make sure that it's tidy.
						relationshipServiceURL = relationshipServiceURL.trim();

						// Determine if a part of the specified URL needs to be replaced with the server URL
						// configured in the batch classe.
						if (relationshipServiceURL.contains(SERVER_URL)) {
							relationshipServiceURL = relationshipServiceURL.replace(SERVER_URL, serverURL);
						}
					}

					repositoryServiceURL = getUrlRepositoryService(); // bundle.getString("cmis.url.repository_service");
					if ((repositoryServiceURL == null) || (repositoryServiceURL.trim().length() <= 0)) {
						errorMsg.setLength(0);
						errorMsg.append(EXCEPTION_MESSAGE_CONSTANT_1);
						errorMsg.append("cmis.url.relationship_service");
						errorMsg.append(EXCEPTION_MESSAGE_CONSTANT_2);
						throw new Exception(errorMsg.toString());
					} else {
						// Make sure that it's tidy.
						repositoryServiceURL = repositoryServiceURL.trim();

						// Determine if a part of the specified URL needs to be replaced with the server URL
						// configured in the batch classe.
						if (repositoryServiceURL.contains(SERVER_URL)) {
							repositoryServiceURL = repositoryServiceURL.replace(SERVER_URL, serverURL);
						}
					}

					versioningServiceURL = getUrlVersioningService();// bundle.getString("cmis.url.versioning_service");
					if ((versioningServiceURL == null) || (versioningServiceURL.trim().length() <= 0)) {
						errorMsg.setLength(0);
						errorMsg.append(EXCEPTION_MESSAGE_CONSTANT_1);
						errorMsg.append("cmis.url.versioning_service");
						errorMsg.append(EXCEPTION_MESSAGE_CONSTANT_2);
						throw new Exception(errorMsg.toString());
					} else {
						// Make sure that it's tidy.
						versioningServiceURL = versioningServiceURL.trim();

						// Determine if a part of the specified URL needs to be replaced with the server URL
						// configured in the batch classe.
						if (versioningServiceURL.contains(SERVER_URL)) {
							versioningServiceURL = versioningServiceURL.replace(SERVER_URL, serverURL);
						}
					}

				} else if (!securityMode.trim().equalsIgnoreCase("basic")) {
					// This security mode isn't recognized.
					LOGGER.warn("CMIS CONFIGURATION WARNING: \"cmis.security.mode\" property value \"" + securityMode.trim()
							+ "\" isn't supported. HTTP Basic Authentication will be used by default.");
				}
			}
		} catch (Exception configEx) {
			LOGGER.error("CMIS CONFIGURATION ERROR: An error occurred while attempting to obtain configuration properties from the "
					+ "configuraiton file. The error was: " + configEx.getMessage());
		}

		// Set the remaining parameters based on the selected security mode.
		if (basicSecurity) {
			LOGGER.info("CMIS: HTTP Basic Authentication will be used for CMIS messaging.");
			sessionParameters.put(SessionParameter.ATOMPUB_URL, serverURL);
			sessionParameters.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
		} else {
			LOGGER.info("CMIS: WS-Security will be used for CMIS messaging.");

			// Configure Open Chemistry for WS-Security.
			sessionParameters.put(SessionParameter.BINDING_TYPE, BindingType.WEBSERVICES.value());
			sessionParameters.put(SessionParameter.AUTH_SOAP_USERNAMETOKEN, "true");
			sessionParameters.put(SessionParameter.AUTH_HTTP_BASIC, FALSE);

			sessionParameters.put(SessionParameter.WEBSERVICES_ACL_SERVICE, aclServiceURL);
			sessionParameters.put(SessionParameter.WEBSERVICES_DISCOVERY_SERVICE, discoverServiceURL);
			sessionParameters.put(SessionParameter.WEBSERVICES_MULTIFILING_SERVICE, multifilingServiceURL);
			sessionParameters.put(SessionParameter.WEBSERVICES_NAVIGATION_SERVICE, navigationServiceURL);
			sessionParameters.put(SessionParameter.WEBSERVICES_OBJECT_SERVICE, objectServiceURL);
			sessionParameters.put(SessionParameter.WEBSERVICES_POLICY_SERVICE, policyServiceURL);
			sessionParameters.put(SessionParameter.WEBSERVICES_RELATIONSHIP_SERVICE, relationshipServiceURL);
			sessionParameters.put(SessionParameter.WEBSERVICES_REPOSITORY_SERVICE, repositoryServiceURL);
			sessionParameters.put(SessionParameter.WEBSERVICES_VERSIONING_SERVICE, versioningServiceURL);
		}

		// ****************************************************************************************
		// END: Zia Consulting Enhancement
		// ****************************************************************************************

		if (null != repositoryID && !repositoryID.isEmpty()) {
			sessionParameters.put(SessionParameter.REPOSITORY_ID, repositoryID);
		}

		return sessionParameters;
	}

	/**
	 * @param session Session
	 * @param parentId ObjectId
	 * @param rootFolder String
	 * @return ObjectId
	 */
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

	/**
	 * @param root Folder
	 * @param rootFolder String
	 * @return Folder
	 */
	private Folder checkCreateFolder(Session session, Folder parentFolder, String parentFolderPath, int currentFolderIndex,
			String[] folderPathList) {
		// ****************************************************************************************
		// BEGIN: Zia Consulting Enhancement
		// Modified to support a nested folder structure within the repository rather than simply
		// a root level folder. Recursion is employed in order to walk the folder tree.
		// ****************************************************************************************
		Folder targetFolder = null;
		Folder targetFolderReturnValue=null;

		// Get the name of the folder at the current index in the path.
		String currentFolderName = folderPathList[currentFolderIndex];

		LOGGER.info("Determining if the target folder \"" + currentFolderName + "\" exists within the repository folder \""
				+ parentFolderPath + "\".");

		// Iterate through the child nodes of the parent node until we find the specified child folder.
		for (CmisObject childNode : parentFolder.getChildren()) {
			if (childNode.getName().equals(currentFolderName) && (childNode instanceof Folder)) {
				targetFolder = (Folder) childNode;
				LOGGER.info("Folder already present");
				LOGGER.info("Found the child folder. Its folder ID is " + childNode.getId() + ".");
				break;
			}
		}

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

		else {
			// The folder does exist, but we need to determine if we are at the end of the chain.
			int newFolderIndex = currentFolderIndex + 1;
			if (newFolderIndex < folderPathList.length) {
				String subfolderPath = parentFolderPath;
				StringBuffer tempSubfolderPath=new StringBuffer();
				tempSubfolderPath.append(subfolderPath);
				if (!("/").equals(parentFolderPath)) {
					tempSubfolderPath.append('/');
				}
				tempSubfolderPath.append(currentFolderName);
				subfolderPath=tempSubfolderPath.toString();

				// We are not at the end of the tree. Iterate to the next node in the path.
				targetFolder = this.checkCreateFolder(session, targetFolder, subfolderPath, newFolderIndex, folderPathList);
			}
			// Else this is the end of the line.
		targetFolderReturnValue=targetFolder;
		}
		// ****************************************************************************************
		// END: Zia Consulting Enhancement
		// ****************************************************************************************
		
		return targetFolderReturnValue;
	} // End checkCreateFolder

	/**
	 * @param mainFolder Folder
	 * @param batchInstanceID Long
	 * @return Folder
	 */
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

	/**
	 * @param session Session
	 * @param folder Folder
	 * @param batchInstanceID String
	 * @return ObjectId
	 */
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
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		return batchInstanceFolderID;
	}

	/**
	 * @param session Session
	 * @param folder Folder
	 * @param file File
	 * @param document DocumentType
	 * @param batchInstanceIdentifier String
	 * @param batchClassIdentifier String
	 * @throws IOException
	 */
	private void uploadDocument(Session session, Folder folder, File file, Document document,
			String batchClassIdentifier, boolean isPdfFile) throws IOException, DCMAApplicationException {
		String mimeType = null;
		try {
			FileNameMap fileNameMap = URLConnection.getFileNameMap();
			mimeType = fileNameMap.getContentTypeFor(file.getAbsolutePath());
		} catch (Exception e) {
			LOGGER.error("Could not find mime type. Using other method", e);
		}
		if (mimeType == null || !(mimeType.equalsIgnoreCase(IMAGE_MIME_TYPE) || mimeType.equalsIgnoreCase(PDF_MIME_TYPE))) {
			mimeType = IMAGE_MIME_TYPE;
			if (isPdfFile) {
				mimeType = PDF_MIME_TYPE;
			}
		}

		LOGGER.info("Content mime type is: " + mimeType);

		FileInputStream fis = new FileInputStream(file);
		DataInputStream dis = new DataInputStream(fis);
		byte[] bytes = new byte[(int) file.length()];
		dis.readFully(bytes);

		Map<String, Object> newDocProps = getPropertyMap(file, document, batchClassIdentifier);

		LOGGER.info(newDocProps.toString());

		List<Ace> addAces = new LinkedList<Ace>();
		List<Ace> removeAces = new LinkedList<Ace>();
		List<Policy> policies = new LinkedList<Policy>();

		try {

			// BigInteger bInteger = new BigInteger("10");
			ContentStream contentStream = new ContentStreamImpl(file.getAbsolutePath(), null, mimeType,
					new ByteArrayInputStream(bytes));

			VersioningState versioningState;// default Versioning State of Document

			try {
				versioningState = VersioningState.valueOf(documentVersioningState.trim());
			} catch (Exception e) {
				LOGGER.error("In valid parameter set in the property file " + e.getMessage(), e);
				versioningState = VersioningState.NONE;
			}

			org.apache.chemistry.opencmis.client.api.Document doc = folder.createDocument(newDocProps, contentStream, versioningState,
					policies, removeAces, addAces, session.getDefaultContext());
			if (null != doc) {
				LOGGER.info("*** Created Document : " + file.getAbsolutePath() + "  " + doc.getCheckinComment());
			}
			// Document created

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new DCMAApplicationException("Unable to upload the document : " + file.getName(), e);
		} finally {
			try {
				if (dis != null) {
					dis.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (IOException io) {
				LOGGER.error("Unable to close the input stream " + io.getMessage());
			}
		}

	}

	/**
	 * @param file File
	 * @param document DocumentType
	 * @param batchClassIdentifier String
	 * @return Map<String, String>
	 * @throws DCMAApplicationException
	 */
	private Map<String, Object> getPropertyMap(File file, Document document, String batchClassIdentifier)
			throws DCMAApplicationException {

		if (null == file) {
			throw new DCMAApplicationException("File can not be null.");
		}

		Map<String, Object> newDocProps = new HashMap<String, Object>();
		newDocProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
		newDocProps.put(PropertyIds.NAME, file.getName());

		Properties properties = new Properties();
		FileInputStream fis = null;
		try {

			String propertyFolderPath = batchSchemaService.getAbsolutePath(batchClassIdentifier, batchSchemaService
					.getCmisPluginMappingFolderName(), false);

			if (null == propertyFolderPath) {
				throw new DCMAApplicationException("In valid folder name in properties file.");
			}

			String propertyFilePath = propertyFolderPath + File.separator + getPluginMappingFileName();

			File propertyFile = new File(propertyFilePath);
			fis = new FileInputStream(propertyFile);
			properties.load(fis);
		} catch (IOException io) {
			LOGGER.error("Unable to load the property file. " + io.getMessage());
			throw new DCMAApplicationException("Unable to load the property file.", io);
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException io) {
				LOGGER.error("Unable to close the input stream of the property file. " + io.getMessage());
				throw new DCMAApplicationException("Unable to close the input stream of the property file. ", io);
			}
		}

		Set<Object> set = properties.keySet();
		Iterator<Object> itr = set.iterator();

		DocumentLevelFields documentLevelFields = document.getDocumentLevelFields();
		if (null == documentLevelFields) {
			throw new DCMAApplicationException("Document level fields can not be null.");
		}

		List<DocField> documentLevelFieldList = documentLevelFields.getDocumentLevelField();
		if (documentLevelFieldList.isEmpty()) {
			throw new DCMAApplicationException("Document level fields can not be empty.");
		}

		while (itr.hasNext()) {
			Object key = itr.next();
			String keyString = (String) key;
			if (null != keyString) {
				String type = document.getType();
				if (null != type) {
					type = type.replaceAll(" ", "");
					if (keyString.contains(type)) {
						String value = properties.getProperty(keyString);
						LOGGER.info("property - keyString : " + keyString + " , property-value : " + value);

						if (keyString.equals(type)) {
							newDocProps.put(PropertyIds.OBJECT_TYPE_ID, value);
						} else {
							for (DocField fdType : documentLevelFieldList) {
								String name = fdType.getName();
								if (null != name) {
									String nameDLF = name.replaceAll(" ", "");
									if (keyString.contains(nameDLF)) {
										String valueFdType = fdType.getValue();
										if (!(valueFdType == null || valueFdType.trim().isEmpty())) {
											Object valueDLF = convert(valueFdType, fdType.getType(), nameDLF);
											newDocProps.put(value, valueDLF);
										}
										break;
									}
								}
							}
						}

					}
				}
			}
		}

		return newDocProps;

	}

	private Object convert(String value, String type, String property) {
		Object returnValue = null;
		switch (DataType.getDataType(type)) {
			case DATE:
				try {
					GregorianCalendar calendar = new GregorianCalendar();
					DateFormat formatter = null;
					Date date;
					formatter = new SimpleDateFormat(getDateFormat(), Locale.ENGLISH);
					date = (Date) formatter.parse(value);
					calendar.setTime(date);
					returnValue = calendar;
					LOGGER.info(CONVERTING + property + " to dataType date");
				} catch (ParseException e) {
					LOGGER.error("Could not parse date. Using string instead to upload document" + e);
					returnValue = (Object) value;
				} catch (Exception e) {
					LOGGER.error("Could not parse date. Using string instead to upload document" + e);
					returnValue = (Object) value;
				}
				break;
			case DOUBLE:
			case FLOAT:
				LOGGER.info(CONVERTING + property + " to dataType BigDecimal");
				returnValue = new BigDecimal(value);
				break;
			case INTEGER:
				LOGGER.info(CONVERTING + property + " to dataType Integer");
				returnValue = Integer.valueOf(value);
				break;
			case LONG:
				LOGGER.info(CONVERTING + property + " to dataType Long");
				returnValue = Long.valueOf(value);
				break;
			case STRING:
				LOGGER.info(CONVERTING + property + " to dataType String");
				returnValue = (Object) value;
				break;
			case BOOLEAN:
				LOGGER.info(CONVERTING + property + " to dataType Boolean");
				String valueToConvert = value;

				if (value.equalsIgnoreCase("yes")) {
					valueToConvert = "true";
				} else {
					try {
						int valueInt = Integer.parseInt(valueToConvert);
						if (valueInt != 0) {
							valueToConvert = "true";
						} else {
							valueToConvert = FALSE;
						}
					} catch (NumberFormatException nfe) {
						valueToConvert = FALSE;
						LOGGER.info("Found non integer value in boolean field.");
					}
				}

				returnValue =  Boolean.valueOf(valueToConvert);
				break;
			default:
				LOGGER.info(CONVERTING + property + " to default value of String");
				returnValue = (Object) value;
				break;
		}
		return returnValue;
	}

	/**
	 * Enum for file extension.
	 * 
	 */
	public enum UPLOADEXT {
		PDF("pdf"), TIF("tif");

		private String uploadFileExt;

		UPLOADEXT(String uploadFileExt) {
			this.uploadFileExt = uploadFileExt;
		}

		public String getUploadFileExt() {
			return this.uploadFileExt;
		}
	}

	/**
	 * Called to determine if at batch folder should be created in the repository for each set of batch documents that are exported to
	 * CMIS. The configuration file property cmis.repo.create_batch_subfolders specifies a value of "false" in order to override the
	 * default behavior, which is to create a batch sub-folder.
	 * 
	 * @return A boolean value indicating whether batch documents should be exported to their own sub-folders.
	 */
	private boolean isBatchInSubfolder() {
		boolean batchInSubfolder = true;

		try {
			// Get the value of the configuration property.
			String batchInSubfolderString = getRepoCreateBatchFolder();

			if ((batchInSubfolderString != null) && (batchInSubfolderString.trim().isEmpty())
					&& (batchInSubfolderString.trim().equalsIgnoreCase(FALSE))) {
				// Batches do not get their own subfolders.
				batchInSubfolder = false;
			}
		} catch (Exception configEx) {
			LOGGER.info("Ignore exceptions related to this feature, as this is not a required behavior");
			
			// Ignore exceptions related to this feature, as this is not a required behavior.
		}

		return batchInSubfolder;
	} // End isBatchInSubfolder

	/**
	 * This method deletes the file for a batch folder whose extension is set in property of batch.
	 * 
	 * @param batchInstanceIdentifier
	 */
	public void deleteDocFromRepository(String batchInstanceIdentifier) throws DCMAApplicationException {
		String isCMISON = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, CMIS_EXPORT_PLUGIN,
				CMISProperties.CMIS_SWITCH);

		if (isCMISON == null || !isCMISON.equals("ON")) {
			return;
		}

		String rootFolder = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, CMIS_EXPORT_PLUGIN,
				CMISProperties.CMIS_ROOT_FOLDER);
		String serverURL = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, CMIS_EXPORT_PLUGIN,
				CMISProperties.CMIS_SERVER_URL);
		String serverUserName = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, CMIS_EXPORT_PLUGIN,
				CMISProperties.CMIS_SERVER_USER_NAME);
		String serverPassword = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, CMIS_EXPORT_PLUGIN,
				CMISProperties.CMIS_SERVER_PASSWORD);
		String repositoryID = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, CMIS_EXPORT_PLUGIN,
				CMISProperties.CMIS_REPOSITORY_ID);
		String uploadFileTypeExt = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, CMIS_EXPORT_PLUGIN,
				CMISProperties.CMIS_UPLOAD_FILE_EXT);

		try {
			Session session = null;

			Map<String, String> sessionParameters = createAtomPubParameters(serverURL, serverUserName, serverPassword, repositoryID);

			// create session
			SessionFactory sessionFactory = SessionFactoryImpl.newInstance();

			if (null == repositoryID || repositoryID.isEmpty()) {
				List<Repository> repositories = sessionFactory.getRepositories(sessionParameters);
				sessionParameters.put(SessionParameter.REPOSITORY_ID, repositories.get(0).getId());
			}

			session = sessionFactory.createSession(sessionParameters);

			// ****************************************************************************************
			// BEGIN: Zia Consulting Enhancement
			// ****************************************************************************************
			// Get a handle to the repository root folder.
			Folder root = session.getRootFolder();

			// Split the specified repository folder path into its individual parts using "/" as the
			// delimeter.
			String[] folderPathList = rootFolder.split("/");

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
			Folder mainFolder = checkCreateFolder(session, root, "/", startIndex, folderPathList);

			// Determine if the batch file was placed within a batch sub-folder
			Folder batchInstanceFolder = null;
			if (this.isBatchInSubfolder()) {
				// Batch files are to be placed within a sub-folder. Get a handle to that folder.
				batchInstanceFolder = checkBatchInstanceFolder(mainFolder, batchInstanceIdentifier);

				if (batchInstanceFolder == null) {
					throw new Exception("Unable to locate the batch folder \"" + rootFolder + "/" + batchInstanceIdentifier
							+ "\" within the repository.");
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

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new DCMAApplicationException(e.getMessage(), e);
		}
	}
}
