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

package com.ephesoft.dcma.webservice.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.ephesoft.dcma.batch.dao.xml.BatchSchemaDao;
import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.UserInformation;
import com.ephesoft.dcma.batch.schema.Batch.Documents;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.exception.FTPDataDownloadException;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassCloudConfig;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.RemoteBatchInstance;
import com.ephesoft.dcma.da.domain.User;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchClassCloudConfigService;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.da.service.UserService;
import com.ephesoft.dcma.ftp.service.FTPService;
import com.ephesoft.dcma.mail.WizardMailException;
import com.ephesoft.dcma.mail.service.WizardMailService;
import com.ephesoft.dcma.recostar.service.RecostarService;
import com.ephesoft.dcma.user.connectivity.exception.InvalidCredentials;
import com.ephesoft.dcma.user.service.UserConnectivityService;
import com.ephesoft.dcma.util.CustomFileFilter;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.WebServiceUtil;
import com.ephesoft.dcma.util.XMLUtil;
import com.ephesoft.dcma.webservice.constants.WebserviceConstants;
import com.ephesoft.dcma.workflow.constant.WorkFlowConstants;
import com.ephesoft.dcma.workflow.service.HTMLFileHandler;
import com.ephesoft.dcma.workflow.service.JbpmService;
import com.ephesoft.dcma.workflow.service.common.DeploymentService;
import com.ephesoft.dcma.workflow.service.common.WorkflowService;

/**
 * This Class provides the functionality of the Web services feature used to preparation and restarting batch instances
 * {@link EphesoftWebService}.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.batch.service.BatchSchemaService
 * @see com.ephesoft.dcma.batch.service.PluginPropertiesService
 */
@Controller
public class EphesoftWebService {

	/**
	 * ERROR_IN_SENDING_STATUS_USING_WEB_SERVICE String.
	 */
	private static final String ERROR_IN_SENDING_STATUS_USING_WEB_SERVICE = "Error in sending status using web service";

	/**
	 * CONSTANT_TO String.
	 */
	private static final String CONSTANT_TO = " to ";

	/**
	 * Initializing pluginPropertiesService {@link PluginPropertiesService}.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * Initializing batchSchemaService {@link BatchSchemaService}.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Initializing batchClassService {@link BatchClassService}.
	 */
	@Autowired
	private BatchClassService batchClassService;

	/**
	 * Initializing batchInstanceService {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * Initializing workflowService {@link WorkflowService}.
	 */
	@Autowired
	private WorkflowService workflowService;

	/**
	 * Initializing ftpService {@link FTPService}.
	 */
	@Autowired
	private FTPService ftpService;

	/**
	 * Initializing userService {@link UserService}.
	 */
	@Autowired
	private UserService userService;

	/**
	 * Initializing batchClassCloudConfigService {@link BatchClassCloudConfigService}.
	 */
	@Autowired
	private BatchClassCloudConfigService batchClassCloudConfigService;

	/**
	 * Initializing htmlFileHandler {@link HTMLFileHandler}.
	 */
	@Autowired
	private HTMLFileHandler htmlFileHandler;

	/**
	 * userConnectivityService {@link UserConnectivityService}.
	 */
	@Autowired
	private UserConnectivityService userConnectivityService;

	/**
	 * batchSchemaDao {@link BatchSchemaDao}.
	 */
	@Autowired
	private BatchSchemaDao batchSchemaDao;

	/**
	 * wizardMailService {@link WizardMailService}.
	 */
	@Autowired
	private WizardMailService wizardMailService;

	/**
	 * deploymentService {@link DeploymentService}.
	 */
	@Autowired
	private DeploymentService deploymentService;

	/**
	 * jbpmService {@link JbpmService}.
	 */
	@Autowired
	private JbpmService jbpmService;

	/**
	 * Initializing recostarService {@link RecostarService}.
	 */
	@Autowired
	private RecostarService recostarService;
	
	/**
	 * Initializing logger {@link Logger}.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(EphesoftWebService.class);

	/**
	 * HOCR XML Extension String.
	 */
	private static final String HOCR_XML_EXTN = "HOCR.xml";
	
	/**
	 * HOCR_XML_ZIP_EXTN String.
	 */
	private static final String HOCR_XML_ZIP_EXTN = "HOCR.xml.zip";

	/**
	 * To prepare data and restart the batch.
	 * @param batchId String
	 * @param serverURL String
	 * @param folderPath String
	 * @param batchClassId String
	 * @param moduleName String
	 * @param newBatchInstanceIdentifier String
	 * @param batchName String
	 * @return String
	 */
	@RequestMapping(value = "/batchIdentifier/{batchId}/server/{serverURL}/folderLocation/{folderPath}/batchClassId/{batchClassId}/moduleName/{moduleName}/newBatchInstanceIdentifier/{newBatchInstanceIdentifier}/batchName/{batchName}", method = RequestMethod.GET)
	@ResponseBody
	public String prepareDataAndRestartBatch(@PathVariable("batchId") final String batchId,
			@PathVariable("serverURL") final String serverURL, @PathVariable("folderPath") final String folderPath,
			@PathVariable("batchClassId") final String batchClassId, @PathVariable("moduleName") final String moduleName,
			@PathVariable("newBatchInstanceIdentifier") final String newBatchInstanceIdentifier,
			@PathVariable("batchName") final String batchName) {
		String newBatchInstanceID = null;
		newBatchInstanceID = EphesoftStringUtil.getDecodedString(newBatchInstanceIdentifier);
		String folderPathLocalVariable = folderPath;
		String serverURLLocalVariable = EphesoftStringUtil.getDecodedString(serverURL);
		String moduleNameDecoded = EphesoftStringUtil.getDecodedString(moduleName);
		folderPathLocalVariable = EphesoftStringUtil.getDecodedString(folderPathLocalVariable).replace(WorkFlowConstants.CARET_SYMBOL,
				WorkFlowConstants.BACK_SLASH_SYMBOL);
		serverURLLocalVariable = EphesoftStringUtil.getDecodedString(serverURLLocalVariable).replace(WorkFlowConstants.CARET_SYMBOL,
				WorkFlowConstants.FORWARD_SLASH_SYMBOL);
		String batchNameLocalVariable = EphesoftStringUtil.getDecodedString(batchName);
		LOGGER.info("==========Inside EphesoftWebService=============");
		LOGGER.info("Folder path is " + folderPathLocalVariable);
		LOGGER.info("Server URL is" + serverURLLocalVariable);
		Random random = new Random();
		BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);

		boolean isZipSwitchOn = batchSchemaService.isZipSwitchOn();
		LOGGER.info("Zipped Batch XML switch is:" + isZipSwitchOn);

		String sourceDirectory = batchId;
		String oldBatchId = batchId.split(WorkFlowConstants.UNDERSCORE_SYMBOL)[WebserviceConstants.ZERO];
		LOGGER.info("Source URL Directory is" + sourceDirectory);
		long folderName = random.nextLong();
		if (folderName < WebserviceConstants.ZERO) {
			folderName = Math.abs(folderName);
		}
		String downloadDirectory = batchSchemaService.getLocalFolderLocation() + File.separator + folderName;
		LOGGER.info("Preparing to download data from the FTP server");
		try {
			ftpService.downloadDirectory(sourceDirectory, downloadDirectory, WebserviceConstants.ZERO, true);
			newBatchInstanceID = checkBatchInstanceIdentifier(newBatchInstanceIdentifier, serverURLLocalVariable, oldBatchId,
					batchClassId, batchNameLocalVariable, moduleNameDecoded);
			if (newBatchInstanceID != null) {
				boolean isPreparedData = preparedFiles(newBatchInstanceID, batchClass, oldBatchId, folderName, batchNameLocalVariable,
						isZipSwitchOn);
				if (isPreparedData) {
					LOGGER.info("Restarting workflow batchInstanceIdentifier" + newBatchInstanceIdentifier + "module name"
							+ moduleNameDecoded);
					LOGGER.info("Starting to create serialize file");
					pluginPropertiesService.getPluginProperties(newBatchInstanceID);
					LOGGER.info("Created serialize file");
					String moduleWorkflowName = getModuleWorkflowNameForBatchClassId(batchClassId, moduleNameDecoded);
					workflowService.startWorkflow(new BatchInstanceID(newBatchInstanceID), moduleWorkflowName);
				} else {
					LOGGER.info("Error in preparing data " + newBatchInstanceIdentifier + ".Returning null");
				}
				LOGGER.info("Returning New batch instance identifier" + newBatchInstanceID);
			}
		} catch (FTPDataDownloadException e) {
			LOGGER.error("Error in downloading data from FTP. Marking batch as error.... " + e.getMessage(), e);
		}
		return newBatchInstanceID;
	}

	/**
	 * To get Module Workflow Name for Batch Class Id.
	 * @param batchClassId String
	 * @param moduleNameDecoded String
	 * @return String
	 */
	private String getModuleWorkflowNameForBatchClassId(final String batchClassId, String moduleNameDecoded) {
		String moduleName = "";
		LOGGER.info("Retrieving batch class module workflow name for " + moduleNameDecoded + " module in the batch class with id: "
				+ batchClassId);
		final BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);
		for (BatchClassModule bcm : batchClass.getBatchClassModules()) {
			if (bcm.getModule().getName().equalsIgnoreCase(moduleNameDecoded)) {
				moduleName = bcm.getWorkflowName();
				break;
			}
		}
		if (moduleName.isEmpty()) {
			LOGGER.info("No Batch Class Module with module name" + moduleNameDecoded + " found in the batch class with id: "
					+ batchClassId);
		} else {
			LOGGER.info("Batch Class Module with module name" + moduleNameDecoded + " found in the batch class with id: "
					+ batchClassId);
			LOGGER.info("Batch Class Workflow name: " + moduleName);
		}
		return moduleName;
	}

	/**
	 * API to check the batch instance is existing batch or not. If its existing batch its return old batch instance identifier else it
	 * will create new batch instance and return new batch instance identifier.
	 * 
	 * @param newBatchInstanceIdentifier {@link String}
	 * @param serverURLLocalVariable {@link String}
	 * @param batchId {@link String}
	 * @param batchClassId {@link String}
	 * @param batchName {@link String}
	 * @param moduleName {@link String}
	 * @return newBatchInstanceID {@link String}
	 */
	private String checkBatchInstanceIdentifier(final String newBatchInstanceIdentifier, final String serverURLLocalVariable,
			final String batchId, final String batchClassId, final String batchName, final String moduleName) {
		String newBatchInstanceID = null;
		try {
			if (!newBatchInstanceIdentifier.equalsIgnoreCase(WorkFlowConstants.NOT_APPLICABLE)) {
				String moduleNameValue = null;
				newBatchInstanceID = newBatchInstanceIdentifier;
				BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);
				BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(newBatchInstanceIdentifier);
				List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
				for (BatchClassModule bcm : batchClassModules) {
					if (bcm.getModule().getName().equalsIgnoreCase(moduleName)) {
						moduleNameValue = bcm.getModule().getName();
						break;
					}
				}
				RemoteBatchInstance remoteBatchInstance = new RemoteBatchInstance();
				remoteBatchInstance.setPreviousRemoteBatchInstanceIdentifier(batchId);
				remoteBatchInstance.setPreviousRemoteURL(serverURLLocalVariable);
				remoteBatchInstance.setId(WebserviceConstants.ZERO);
				remoteBatchInstance.setSourceModule(moduleNameValue);
				batchInstance.setRemoteBatchInstance(remoteBatchInstance);
				batchInstance.setRemote(false);
				batchInstanceService.updateBatchInstance(batchInstance);
			} else {
				newBatchInstanceID = getNewBatchInstanceIdentifier(batchClassId, serverURLLocalVariable, batchId, batchName,
						moduleName);
			}
		} catch (Exception e) {
			LOGGER.error("Unable to get newBatchInstanceIdentifier :" + newBatchInstanceID + " " + e.getMessage(), e);
		}
		return newBatchInstanceID;
	}

	/**
	 * API to generate new batch instance identifier.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param previousEphesoftInstance {@link String}
	 * @param previousBatchInstanceID {@link String}
	 * @param batchName {@link String}
	 * @param moduleName {@link String}
	 * @return newBatchInstanceIdentfier {@link String}
	 */
	public String getNewBatchInstanceIdentifier(final String batchClassIdentifier, final String previousEphesoftInstance,
			final String previousBatchInstanceID, final String batchName, final String moduleName) {
		BatchInstance updatedbatchInstance = null;
		String newBatchInstanceIdentfier = null;
		boolean isValid = true;
		if (batchClassIdentifier == null) {
			LOGGER.error("batchClassIdentifier is null");
			if (isValid) {
				isValid = false;
			}
		}
		if (previousEphesoftInstance == null) {
			LOGGER.error("previousEphesoftInstance is null");
			if (isValid) {
				isValid = false;
			}
		}
		if (previousBatchInstanceID == null) {
			LOGGER.error("previousBatchInstanceID is null");
			if (isValid) {
				isValid = false;
			}
		}
		if (batchName == null) {
			LOGGER.error("batchName is null");
			if (isValid) {
				isValid = false;
			}
		}
		if (moduleName == null) {
			LOGGER.error("moduleName is null");
			if (isValid) {
				isValid = false;
			}
		}
		if (isValid) {
			String moduleNameValue = null;
			LOGGER.info("Generating new batch instance identifier");
			BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdentifier);
			BatchInstance batchInstance = new BatchInstance();
			List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
			for (BatchClassModule bcm : batchClassModules) {

				if (bcm.getModule().getName().equalsIgnoreCase(moduleName)) {
					moduleNameValue = bcm.getModule().getName();
					break;
				}
			}
			batchInstanceService.updateBatchInstance(batchInstance);
			batchInstance.setBatchClass(batchClass);
			batchInstance.setLocalFolder(batchSchemaService.getLocalFolderLocation());
			batchInstance.setBatchName(batchName);
			batchInstance.setPriority(batchClass.getPriority());
			batchInstance.setStatus(BatchInstanceStatus.REMOTE);
			RemoteBatchInstance remoteBatchInstance = new RemoteBatchInstance();
			remoteBatchInstance.setPreviousRemoteBatchInstanceIdentifier(previousBatchInstanceID);
			remoteBatchInstance.setPreviousRemoteURL(previousEphesoftInstance);
			remoteBatchInstance.setId(WebserviceConstants.ZERO);
			if (remoteBatchInstance.getSourceModule() == null) {
				remoteBatchInstance.setSourceModule(moduleNameValue);
			}
			batchInstance.setRemoteBatchInstance(remoteBatchInstance);
			updatedbatchInstance = batchInstanceService.merge(batchInstance);
			newBatchInstanceIdentfier = updatedbatchInstance.getIdentifier();
		}
		return newBatchInstanceIdentfier;
	}

	/**
	 * API to prepare files and modify the internal contents of the oldBatchInstance with newBatchInstance.
	 * 
	 * @param newBatchInstanceIdentfier {@link String}
	 * @param batchClass {@link String}
	 * @param oldBatchInstanceIdentifier {@link String}
	 * @param folderName {@link String}
	 * @param batchName {@link String}
	 * @param isZipSwitchOn boolean
	 * @return boolean
	 */
	public boolean preparedFiles(final String newBatchInstanceIdentfier, final BatchClass batchClass,
			final String oldBatchInstanceIdentifier, final long folderName, final String batchName, final boolean isZipSwitchOn) {
		boolean isPreparedData = false;
		if (oldBatchInstanceIdentifier != null && newBatchInstanceIdentfier != null && batchClass != null) {
			boolean isRenameComplete = false;
			String folderPath = batchSchemaService.getLocalFolderLocation() + File.separator + folderName;
			LOGGER.info("Folder path is: " + folderPath);
			File ftpDownloadFolder = new File(folderPath);
			if (ftpDownloadFolder.isDirectory()) {
				String[] string = ftpDownloadFolder.list(new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(), FileType.TIFF
						.getExtensionWithDot(), FileType.PNG.getExtensionWithDot(), FileType.HTML.getExtensionWithDot(), FileType.XML
						.getExtensionWithDot(), FileType.ZIP.getExtensionWithDot()));
				for (String fileName : string) {
					isRenameComplete = false;
					LOGGER.info("File name is: " + fileName);
					String newFileName = changeDataName(fileName, oldBatchInstanceIdentifier, newBatchInstanceIdentfier);
					File newFile = new File(ftpDownloadFolder.getAbsolutePath() + File.separator + fileName);
					String finalFilePath = ftpDownloadFolder.getAbsolutePath() + File.separator + newFileName;
					while (!isRenameComplete) {
						LOGGER.info("Renaming file" + newFile.getAbsolutePath() + " to : " + finalFilePath);
						isRenameComplete = newFile.renameTo(new File(finalFilePath));
					}
					LOGGER.info("Renamed file :" + finalFilePath);
				}
				String batchInstanceSystemPath = batchSchemaService.getLocalFolderLocation() + File.separator
						+ newBatchInstanceIdentfier;
				File file = new File(batchInstanceSystemPath);
				if (file.exists()) {
					LOGGER.info("Batch Instance folder exists, so deleting it's content " + batchInstanceSystemPath);
					boolean isDeletedSucessfully = FileUtils.deleteContentsOnly(batchInstanceSystemPath);
					if (!isDeletedSucessfully) {
						LOGGER.error("Error in deleting existing " + newBatchInstanceIdentfier + " proceeding with copying files");
					}
					try {
						LOGGER.info("Copying contents from " + folderPath + CONSTANT_TO + batchInstanceSystemPath);
						FileUtils.copyDirectoryWithContents(folderPath, batchInstanceSystemPath);
					} catch (IOException e) {
						LOGGER.error("Error in copying files from :" + folderPath + " to :" + batchInstanceSystemPath + " "
								+ e.getMessage(), e);
					} finally {
						LOGGER.info("Deleting contents from " + ftpDownloadFolder.getAbsolutePath());
						FileUtils.deleteDirectoryAndContents(ftpDownloadFolder);
					}
				} else {
					try {
						LOGGER.info("Batch Instance folder does not exists, so Renaming FTP Folder "
								+ ftpDownloadFolder.getAbsolutePath() + CONSTANT_TO + file.getAbsolutePath());
						isRenameComplete = ftpDownloadFolder.renameTo(file);
						while (!isRenameComplete) {
							LOGGER.info("Trying again. Batch Instance folder does not exists, so Renaming FTP Folder "
									+ ftpDownloadFolder.getAbsolutePath() + CONSTANT_TO + file.getAbsolutePath());
							isRenameComplete = ftpDownloadFolder.renameTo(file);
						}
					} catch (SecurityException e) {
						LOGGER.error("Security exception naming the ftp folder.");
					}
				}
				if (isRenameComplete) {
					renameAllFiles(newBatchInstanceIdentfier, batchClass, oldBatchInstanceIdentifier, file, isZipSwitchOn);
					isPreparedData = true;
				} else {
					isPreparedData = false;
				}
			}
		}

		return isPreparedData;
	}

	/**
	 * API to rename all the files and changing the internal contents of the oldBatchInstanceIdentifier with
	 * newBatchInstanceIdentifier.
	 * 
	 * @param newBatchInstanceIdentfier {@link String}
	 * @param batchClass {@link BatchClass}
	 * @param oldBatchInstanceIdentifier {@link String}
	 * @param file {@link File}
	 * @param isZipSwitchOn boolean
	 */
	private void renameAllFiles(final String newBatchInstanceIdentfier, final BatchClass batchClass,
			final String oldBatchInstanceIdentifier, final File file, final boolean isZipSwitchOn) {

		renameBatchFile(newBatchInstanceIdentfier, file, isZipSwitchOn);

		renameHtmlFiles(newBatchInstanceIdentfier, oldBatchInstanceIdentifier, file);

		// Zip Switch handling new code
		renameBackUpFiles(newBatchInstanceIdentfier, batchClass, oldBatchInstanceIdentifier, file);
		renameOriginalBatchFile(newBatchInstanceIdentfier, batchClass, oldBatchInstanceIdentifier, file, isZipSwitchOn);
	}


	private void renameOriginalBatchFile(final String newBatchInstanceIdentfier, final BatchClass batchClass,
			final String oldBatchInstanceIdentifier, final File file, final boolean isZipSwitchOn) {
		String srcFilePath;
		String destFilePath;
		boolean isRenamed = false;

		srcFilePath = file.getAbsoluteFile() + File.separator + newBatchInstanceIdentfier + ICommonConstants.UNDERSCORE_BATCH_XML
				+ WorkFlowConstants.UNDERSCORE_ABC;

		destFilePath = file.getAbsoluteFile() + File.separator + newBatchInstanceIdentfier + ICommonConstants.UNDERSCORE_BATCH_XML;

		if (isZipSwitchOn) {
			srcFilePath = file.getAbsoluteFile() + File.separator + newBatchInstanceIdentfier
					+ ICommonConstants.UNDERSCORE_BATCH_XML_ZIP + WorkFlowConstants.UNDERSCORE_ABC;

			destFilePath = file.getAbsoluteFile() + File.separator + newBatchInstanceIdentfier
					+ ICommonConstants.UNDERSCORE_BATCH_XML_ZIP;
		}

		File newFileName = new File(srcFilePath);
		File finalZipFile = new File(destFilePath);
		while (!isRenamed) {
			isRenamed = newFileName.renameTo(finalZipFile);
		}

		if (isZipSwitchOn) {
			renameZipFileEntries(finalZipFile, batchClass, oldBatchInstanceIdentifier, newBatchInstanceIdentfier);
		} else {
			updateTranferredBatch(newBatchInstanceIdentfier, batchClass, oldBatchInstanceIdentifier);
		}
	}

	
	private void renameBackUpFiles(final String newBatchInstanceIdentfier, final BatchClass batchClass,
			final String oldBatchInstanceIdentifier, final File file) {
		boolean isRenamed = false;
		String[] extArray = {FileType.XML.getExtensionWithDot(), FileType.ZIP.getExtensionWithDot()};
		String[] xmlFiles = file.list(new CustomFileFilter(false, extArray));
		for (String xmlFileName : xmlFiles) {
			String[] checkHOCRFile = xmlFileName.split(WebserviceConstants.UNDERSCORE);
			File xmlFile = new File(file.getAbsoluteFile() + File.separator + xmlFileName);
			if (checkHOCRFile[checkHOCRFile.length - WebserviceConstants.ONE].equalsIgnoreCase(HOCR_XML_EXTN)
					|| checkHOCRFile[checkHOCRFile.length - WebserviceConstants.ONE].equalsIgnoreCase(HOCR_XML_ZIP_EXTN)) {
				renameHocrFiles(xmlFile, newBatchInstanceIdentfier, oldBatchInstanceIdentifier);
				continue;
			}
			if (xmlFileName.endsWith(ICommonConstants.UNDERSCORE_BATCH_XML_ZIP)
					|| xmlFileName.endsWith(ICommonConstants.UNDERSCORE_BATCH_BAK_XML_ZIP)
					|| xmlFileName.endsWith(ICommonConstants.UNDERSCORE_BATCH_XML)
					|| xmlFileName.endsWith(ICommonConstants.UNDERSCORE_BATCH_BAK_XML)) {

				isRenamed = false;
				String newXMLFileName = newBatchInstanceIdentfier + ICommonConstants.UNDERSCORE_BATCH_XML_ZIP;
				if (xmlFileName.endsWith(ICommonConstants.UNDERSCORE_BATCH_XML)
						|| xmlFileName.endsWith(ICommonConstants.UNDERSCORE_BATCH_BAK_XML)) {
					newXMLFileName = newBatchInstanceIdentfier + ICommonConstants.UNDERSCORE_BATCH_XML;
				}
				File tempBatchFile = new File(file.getAbsoluteFile() + File.separator + newXMLFileName);
				while (!isRenamed) {
					isRenamed = xmlFile.renameTo(tempBatchFile);
				}

				if (xmlFileName.endsWith(ICommonConstants.UNDERSCORE_BATCH_XML_ZIP)
						|| xmlFileName.endsWith(ICommonConstants.UNDERSCORE_BATCH_BAK_XML_ZIP)) {
					renameZipFileEntries(tempBatchFile, batchClass, oldBatchInstanceIdentifier, newBatchInstanceIdentfier);
				} else {
					updateTranferredBatch(newBatchInstanceIdentfier, batchClass, oldBatchInstanceIdentifier);
				}
				File newFileName = tempBatchFile;
				isRenamed = false;
				while (!isRenamed) {
					isRenamed = newFileName.renameTo(new File(file.getAbsoluteFile() + File.separator + xmlFileName));
				}
			}
		}
	}

	private void renameHtmlFiles(final String newBatchInstanceIdentfier, final String oldBatchInstanceIdentifier, final File file) {
		String[] htmlFiles = file.list(new CustomFileFilter(false, FileType.HTML.getExtensionWithDot()));

		for (String htmlFile : htmlFiles) {
			String htmlFilePath = batchSchemaService.getLocalFolderLocation() + File.separator + newBatchInstanceIdentfier
					+ File.separator + htmlFile;
			htmlFileHandler.parseHTMLFile(htmlFilePath, newBatchInstanceIdentfier, oldBatchInstanceIdentifier);
		}
	}

	private void renameBatchFile(final String newBatchInstanceIdentfier, final File file, final boolean isZipSwitchOn) {

		boolean isRenamed = false;
		String srcFilePath = file.getAbsoluteFile() + File.separator + newBatchInstanceIdentfier
				+ ICommonConstants.UNDERSCORE_BATCH_XML;

		String destFilePath = file.getAbsoluteFile() + File.separator + newBatchInstanceIdentfier
				+ ICommonConstants.UNDERSCORE_BATCH_XML + WorkFlowConstants.UNDERSCORE_ABC;

		if (isZipSwitchOn && FileUtils.isZipFileExists(srcFilePath)) {
			srcFilePath = file.getAbsoluteFile() + File.separator + newBatchInstanceIdentfier
					+ ICommonConstants.UNDERSCORE_BATCH_XML_ZIP;
			destFilePath = file.getAbsoluteFile() + File.separator + newBatchInstanceIdentfier
					+ ICommonConstants.UNDERSCORE_BATCH_XML_ZIP + WorkFlowConstants.UNDERSCORE_ABC;
		}

		File srcZipFile = new File(srcFilePath);
		File finalZipPath = new File(destFilePath);
		while (!isRenamed) {
			isRenamed = srcZipFile.renameTo(finalZipPath);
		}
	}

	private void renameHocrFiles(final File oldXmlFile, final String newBatchInstanceIdentfier, final String oldBatchInstanceIdentifier) {

		LOGGER.info("Retrived list of zip files from " + oldXmlFile.getAbsolutePath());
	
		String finalZipPath = oldXmlFile.getAbsolutePath();
		File finalZipFile = new File(finalZipPath);

		String destDirectory = finalZipFile.getParent();
		String unzippedFilePath = finalZipFile.getAbsolutePath();
		LOGGER.info("Unziping the " + unzippedFilePath + CONSTANT_TO + destDirectory);

		FileUtils.unzip(finalZipFile, destDirectory);
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(finalZipFile);

			// Only 1st entry picked. Considering that our zip file contains only a single file.
			String xmlFileName = zipFile.entries().nextElement().getName();
		
			StringBuilder xmlFileNameBuilder = new StringBuilder();
			xmlFileNameBuilder.append(destDirectory);
			xmlFileNameBuilder.append(File.separator);
			xmlFileNameBuilder.append(xmlFileName);
			String xmlFilePath = xmlFileNameBuilder.toString();
			File xmlFile = new File(xmlFilePath);

			xmlFileName = xmlFileName.replaceAll(oldBatchInstanceIdentifier, newBatchInstanceIdentfier);
			StringBuilder newXmlFileNameBuilder = new StringBuilder();
			newXmlFileNameBuilder.append(destDirectory);
			newXmlFileNameBuilder.append(File.separator);
			newXmlFileNameBuilder.append(xmlFileName);
			String newXmlFilePath = newXmlFileNameBuilder.toString();
			File newXmlFile = new File(newXmlFilePath);

			LOGGER.info("Renaming " + unzippedFilePath + CONSTANT_TO + xmlFileName);

			xmlFile.renameTo(newXmlFile);

			List<String> fileNames = new ArrayList<String>();
			fileNames.add(newXmlFile.getAbsolutePath());

			LOGGER.info("Zipping the altered files to " + finalZipPath);
		
			FileUtils.zipMultipleFiles(fileNames, finalZipPath);
			newXmlFile.delete();
		} catch (ZipException e) {
			LOGGER.error("Error Creating zip file " + e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.error("I/O Error while Creating zip file " + e.getMessage(), e);
		}

	}

	
	private void updateTranferredBatch(final String newBatchInstanceIdentfier, final BatchClass batchClass,
			final String oldBatchInstanceIdentifier) {
		Batch batch = batchSchemaService.getBatch(newBatchInstanceIdentfier);
		if (batch != null) {
			batch.setBatchClassIdentifier(batchClass.getIdentifier());
			batch.setBatchClassDescription(batchClass.getDescription());
			batch.setBatchClassName(batchClass.getName());
			batch.setBatchClassVersion(batchClass.getVersion());
			batch.setBatchLocalPath(batchSchemaService.getLocalFolderLocation());
			batch.setBatchPriority(String.valueOf(batchClass.getPriority()));
			batch.setBatchInstanceIdentifier(newBatchInstanceIdentfier);
			Documents documents = batch.getDocuments();
			List<Document> documentList = documents.getDocument();
			for (Document document : documentList) {
				if (document != null) {
					Pages pagesList = document.getPages();
					List<Page> pageList = pagesList.getPage();
					for (Page page : pageList) {
						if (page != null) {
							page.setNewFileName(changeDataName(page.getNewFileName(), oldBatchInstanceIdentifier,
									newBatchInstanceIdentfier));
							page.setHocrFileName(changeDataName(page.getHocrFileName(), oldBatchInstanceIdentifier,
									newBatchInstanceIdentfier));
							page.setThumbnailFileName(changeDataName(page.getThumbnailFileName(), oldBatchInstanceIdentifier,
									newBatchInstanceIdentfier));
							page.setComparisonThumbnailFileName(changeDataName(page.getComparisonThumbnailFileName(),
									oldBatchInstanceIdentifier, newBatchInstanceIdentfier));
							page.setDisplayFileName(changeDataName(page.getDisplayFileName(), oldBatchInstanceIdentifier,
									newBatchInstanceIdentfier));
							page.setOCRInputFileName(changeDataName(page.getOCRInputFileName(), oldBatchInstanceIdentifier,
									newBatchInstanceIdentfier));
						}
					}
				}
			}
			batchSchemaService.updateBatch(batch);
		}
	}

	
	private void renameZipFileEntries(final File oldZipFile, final BatchClass batchClass, final String oldBatchInstanceIdentifier,
			final String newBatchInstanceIdentfier) {
		LOGGER.info("Zip files to be modified " + oldZipFile.getAbsolutePath());
		String finalZipPath = oldZipFile.getAbsolutePath();
		File finalZipFile = new File(finalZipPath);

		String destDirectory = finalZipFile.getParent();
		String unzippedFilePath = finalZipFile.getAbsolutePath();
		LOGGER.info("Unziping the " + unzippedFilePath + CONSTANT_TO + destDirectory);

		FileUtils.unzip(finalZipFile, destDirectory);
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(finalZipFile);

			// Only 1st entry picked. Considering that our zip file contains only a single file.
			String xmlFileName = zipFile.entries().nextElement().getName();
	
			StringBuilder xmlFileNameBuilder = new StringBuilder();
			xmlFileNameBuilder.append(destDirectory);
			xmlFileNameBuilder.append(File.separator);
			xmlFileNameBuilder.append(xmlFileName);
			String xmlFilePath = xmlFileNameBuilder.toString();
			File xmlFile = new File(xmlFilePath);

			xmlFileName = xmlFileName.replaceAll(oldBatchInstanceIdentifier, newBatchInstanceIdentfier);
			StringBuilder newXmlFileNameBuilder = new StringBuilder();
			newXmlFileNameBuilder.append(destDirectory);
			newXmlFileNameBuilder.append(File.separator);
			newXmlFileNameBuilder.append(xmlFileName);
			String newXmlFilePath = newXmlFileNameBuilder.toString();
			File newXmlFile = new File(newXmlFilePath);

			LOGGER.info("Renaming " + unzippedFilePath + CONSTANT_TO + xmlFileName);

			xmlFile.renameTo(newXmlFile);

			List<String> fileNames = new ArrayList<String>();
			fileNames.add(newXmlFile.getAbsolutePath());

			LOGGER.info("Zipping the altered files to " + finalZipPath);

			FileUtils.zipMultipleFiles(fileNames, finalZipPath);
			updateTranferredBatch(newBatchInstanceIdentfier, batchClass, oldBatchInstanceIdentifier);
			newXmlFile.delete();
		} catch (ZipException e) {
			LOGGER.error("Error Creating zip file " + e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.error("I/O Error while Creating zip file " + e.getMessage(), e);
		}

	}

	/**
	 * API to changes the content of the file name from oldBatchIdentifier to newBatchIdentifier.
	 * 
	 * @param fileName {@link String}
	 * @param oldBatchIdentifier {@link String}
	 * @param newBatchIdentifier {@link String}
	 * @return newFileName {@link String}
	 */
	public String changeDataName(final String fileName, final String oldBatchIdentifier, final String newBatchIdentifier) {
		String newFileName = null;
		if (fileName != null) {
			newFileName = fileName.replace(oldBatchIdentifier, newBatchIdentifier);
		}
		return newFileName;
	}

	/**
	 * To get Batch Status of Remote Batch.
	 * @param remoteBatchInstanceIdentifier {@link String}
	 * @return {@link BatchInstanceStatus}
	 */
	@RequestMapping(value = "/remoteBatchInstanceIdentifier/{remoteBatchInstanceIdentifier}", method = RequestMethod.GET)
	@ResponseBody
	public BatchInstanceStatus getBatchStatusofRemoteBatch(
			@PathVariable("remoteBatchInstanceIdentifier") final String remoteBatchInstanceIdentifier) {
		BatchInstanceStatus batchInstanceStatus = null;
		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(remoteBatchInstanceIdentifier);
		if (batchInstance != null) {
			batchInstanceStatus = batchInstance.getStatus();
		}
		return batchInstanceStatus;
	}

	/**
	 * To get Previous Batch Instance of Remote Batch.
	 * @param previousRemoteBatchInstanceIdentifier {@link String}
	 * @return {@link BatchInstance}
	 */
	@RequestMapping(value = "/previousRemoteBatchInstanceIdentifier/{previousRemoteBatchInstanceIdentifier}", method = RequestMethod.GET)
	@ResponseBody
	public BatchInstance getPreviousBatchInstanceOfRemoteBatch(
			@PathVariable("previousRemoteBatchInstanceIdentifier") final String previousRemoteBatchInstanceIdentifier) {
		return batchInstanceService.getBatchInstanceByIdentifier(previousRemoteBatchInstanceIdentifier);
	}

	@RequestMapping(value = "/targetBatchInstanceIdentifier/{targetBatchInstanceIdentifier}/previousURL/{previousURL}/previousBatchInstanceIdentifier/{previousBatchInstanceIdentifier}/nextURL/{nextURL}/nextBatchInstanceIdentifier/{nextBatchInstanceIdentifier}/isRemote/{isRemote}", method = RequestMethod.GET)
	@ResponseBody
	public void updateInfoOfRemoteBatchInstance(
			@PathVariable("targetBatchInstanceIdentifier") final String targetBatchInstanceIdentifier,
			@PathVariable("previousURL") final String previousURL,
			@PathVariable("previousBatchInstanceIdentifier") final String previousBatchInstanceIdentifier,
			@PathVariable("nextURL") final String nextURL,
			@PathVariable("nextBatchInstanceIdentifier") final String nextBatchInstanceIdentifier,
			@PathVariable("isRemote") final boolean isRemote) {
		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(targetBatchInstanceIdentifier);
		RemoteBatchInstance remoteBatchInstance = batchInstance.getRemoteBatchInstance();
		if (remoteBatchInstance != null) {
			if (previousBatchInstanceIdentifier.equalsIgnoreCase(WorkFlowConstants.NOT_APPLICABLE)
					&& previousURL.equalsIgnoreCase(WorkFlowConstants.NOT_APPLICABLE)
					&& nextBatchInstanceIdentifier.equalsIgnoreCase(WorkFlowConstants.NOT_APPLICABLE)
					&& nextURL.equalsIgnoreCase(WorkFlowConstants.NOT_APPLICABLE)) {
				remoteBatchInstance.setPreviousRemoteBatchInstanceIdentifier(null);
				remoteBatchInstance.setPreviousRemoteURL(null);
				remoteBatchInstance.setRemoteBatchInstanceIdentifier(null);
				remoteBatchInstance.setRemoteURL(null);
			} else if (previousBatchInstanceIdentifier.equalsIgnoreCase(WorkFlowConstants.NOT_APPLICABLE)
					&& previousURL.equalsIgnoreCase(WorkFlowConstants.NOT_APPLICABLE)) {
				remoteBatchInstance.setRemoteBatchInstanceIdentifier(null);
				remoteBatchInstance.setRemoteURL(null);
			} else {
				remoteBatchInstance.setPreviousRemoteBatchInstanceIdentifier(null);
				remoteBatchInstance.setPreviousRemoteURL(null);
			}
		}
		batchInstance.setRemote(isRemote);
		batchInstance.setRemoteBatchInstance(remoteBatchInstance);
		batchInstanceService.updateBatchInstance(batchInstance);
	}

	/**
	 * To check the user existence.
	 * @param request {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @return boolean
	 */
	@RequestMapping(value = "/checkUserExistence", method = RequestMethod.POST)
	@ResponseBody
	public boolean checkUserExistence(final HttpServletRequest request, final HttpServletResponse response) {
		final String userName = request.getParameter("userName");
		return userConnectivityService.checkUserExistence(userName);
	}

	/**
	 * Sign up method.
	 * @param request {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 */
	@RequestMapping(value = "/signUp", method = RequestMethod.POST)
	@ResponseBody
	public void signUp(final HttpServletRequest request, final HttpServletResponse response) {
		LOGGER.info("Start processing sign up process");
		String workingDir = WebServiceUtil.EMPTY_STRING;
		InputStream instream = null;
		if (request instanceof DefaultMultipartHttpServletRequest) {
			UserInformation userInformation = null;
			User user = null;
			String receiverName = null;
			try {
				final String webServiceFolderPath = batchSchemaService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
				LOGGER.info("workingDir:" + workingDir);
				final String outputDir = WebServiceUtil.createWebServiceOutputDir(workingDir);
				LOGGER.info("outputDir:" + outputDir);
				final DefaultMultipartHttpServletRequest multipartRequest = (DefaultMultipartHttpServletRequest) request;
				final String batchClassId = request.getParameter("batchClassId");
				final String batchClassPriority = request.getParameter("batchClassPriority");
				final String batchClassDescription = request.getParameter("batchClassDescription");
				String batchClassName = request.getParameter("batchClassName");
				batchClassName = getUniqueBatchClassName(batchClassName);
				final String batchInstanceLimit = request.getParameter("batchInstanceLimit");
				final String noOfDays = request.getParameter("noOfDays");
				final String pageCount = request.getParameter("pageCount");
				String uncFolder = "unc" + ICommonConstants.HYPHEN + batchClassName;
				LOGGER.info("Batch Class ID value is: " + batchClassId);
				LOGGER.info("Batch Class Priority value is: " + batchClassPriority);
				LOGGER.info("Batch Class Description value is: " + batchClassDescription);
				LOGGER.info("Batch Class Name value is: " + batchClassName);
				LOGGER.info("UNC Folder value is: " + uncFolder);
				final MultiValueMap<String, MultipartFile> fileMap = multipartRequest.getMultiFileMap();
				for (final String fileName : fileMap.keySet()) {
					if (fileName.toLowerCase(Locale.getDefault()).indexOf(FileType.XML.getExtension().toLowerCase()) > -WebserviceConstants.ONE) {
						final MultipartFile multipartFile = multipartRequest.getFile(fileName);
						instream = multipartFile.getInputStream();
						final Source source = XMLUtil.createSourceFromStream(instream);
						userInformation = (UserInformation) batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().unmarshal(source);
						user = createUserObjectFromUserInformation(userInformation);
						break;
					}
				}
				if (userInformation != null && user != null) {
					LOGGER.info("Recevier name created: " + receiverName);
					userConnectivityService.addUser(userInformation);
					LOGGER.info("Successfully added user for email id: " + userInformation.getEmail());
					userConnectivityService.addGroup(userInformation);
					LOGGER.info("Successfully added group for email id: " + userInformation.getEmail());
					BatchClass batchClass = batchClassService.copyBatchClass(batchClassId, batchClassName, batchClassDescription,
							userInformation.getCompanyName() + ICommonConstants.UNDERSCORE + userInformation.getEmail(),
							batchClassPriority, uncFolder, true);
					LOGGER.info("Adding user information into database");
					user.setBatchClass(batchClass);
					userService.createUser(user);
					LOGGER.info("Successfully added user information into database");
					BatchClassCloudConfig batchClassCloudConfig = createBatchClassCloudConfig(batchInstanceLimit, noOfDays, pageCount,
							batchClass);
					batchClassCloudConfigService.createBatchClassCloudConfig(batchClassCloudConfig);
					LOGGER.info("Successfully copied batch class for batch class: " + batchClassId);
					deploymentService.createAndDeployBatchClassJpdl(batchClass);
					LOGGER.info("Batch Class deployed successfully");
					wizardMailService.sendConfirmationMail(userInformation, false, null);
					LOGGER.info("User login information sent for email id: " + userInformation.getEmail());
				} else {
					LOGGER.error("user Information file is invalid. Unable create the User Information Object from XML.");
				}
			} catch (WizardMailException wizardMailException) {
				try {
					response.sendError(HttpServletResponse.SC_CREATED);
				} catch (IOException e) {
					LOGGER.error(ERROR_IN_SENDING_STATUS_USING_WEB_SERVICE);
				}
			} catch (Exception e) {
				LOGGER.error("Exception occurs while sign up process: " + e.getMessage(), e);
				if (userInformation != null && user != null) {
					LOGGER.info("Deleting created user/groups while signup for : " + userInformation.getEmail());
					userConnectivityService.deleteUser(userInformation.getEmail());
					userConnectivityService.deleteGroup(userInformation.getCompanyName() + ICommonConstants.UNDERSCORE
							+ userInformation.getEmail());
					userService.deleteUser(user);
					LOGGER.info("Successfully deleted user/groups while signup for : " + userInformation.getEmail());
					LOGGER.info("Sending error mail");
					try {
						wizardMailService.sendConfirmationMail(userInformation, true, ExceptionUtils.getStackTrace(e));
						LOGGER.info("Error mail sent succesfully");
					} catch (WizardMailException e1) {
						LOGGER.error("Error in sending error mail to client");
					}
				}
				try {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				} catch (IOException e1) {
					LOGGER.error(ERROR_IN_SENDING_STATUS_USING_WEB_SERVICE);
				}

			}
		}
	}

	private BatchClassCloudConfig createBatchClassCloudConfig(final String batchInstanceLimit, final String noOfDays,
			final String pageCount, BatchClass batchClass) {
		Integer batchInstanceLimitInt = null;
		if (!batchInstanceLimit.isEmpty()) {
			batchInstanceLimitInt = ICommonConstants.DEFAULT_BATCH_INSTANCE_LIMIT;
			try {
				batchInstanceLimitInt = Integer.parseInt(batchInstanceLimit);
			} catch (NumberFormatException e) {
				LOGGER.error("Invalid batchInstance Limit is passed. Using default value is "
						+ ICommonConstants.DEFAULT_BATCH_INSTANCE_LIMIT);
			}
		}

		Integer pageCountInt = null;
		if (!pageCount.isEmpty()) {
			pageCountInt = ICommonConstants.DEFAULT_PAGE_COUNT_LIMIT;
			try {
				pageCountInt = Integer.parseInt(pageCount);
			} catch (NumberFormatException e) {
				LOGGER.error("Invalid pageCount is passed. Using default value is " + ICommonConstants.DEFAULT_PAGE_COUNT_LIMIT);
			}
		}

		Integer noOfDaysInt = null;
		if (!noOfDays.isEmpty()) {
			noOfDaysInt = ICommonConstants.DEFAULT_NO_OF_DAYS_LIMIT;
			try {
				noOfDaysInt = Integer.parseInt(noOfDays);
			} catch (NumberFormatException e) {
				LOGGER.error("Invalid noOfDays is passed. Using default value is " + ICommonConstants.DEFAULT_NO_OF_DAYS_LIMIT);
			}
		}

		BatchClassCloudConfig batchClassCloudConfig = new BatchClassCloudConfig();
		batchClassCloudConfig.setBatchInstanceLimit(batchInstanceLimitInt);
		batchClassCloudConfig.setPageCount(pageCountInt);
		batchClassCloudConfig.setCurrentCounter(WebserviceConstants.ZERO);
		batchClassCloudConfig.setLastReset(new Date());
		batchClassCloudConfig.setNoOfDays(noOfDaysInt);
		batchClassCloudConfig.setBatchClass(batchClass);
		return batchClassCloudConfig;
	}

	/**
	 * To modify the password.
	 * @param request {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 */
	@RequestMapping(value = "/modifyPassword", method = RequestMethod.POST)
	@ResponseBody
	public void modifyPassword(final HttpServletRequest request, final HttpServletResponse response) {
		final String userName = request.getParameter("userName");
		final String newPassword = request.getParameter("newPassword");
		try {
			LOGGER.info("Modifying password for username: " + userName + " and password: " + newPassword);
			userConnectivityService.modifyUserPassword(userName, newPassword);
			LOGGER.info("LDAP account update, now updating DB");
			User user = userService.getUser(userName);
			user.setPassword(newPassword);
			user = userService.updateUser(user);
			LOGGER.info("DB updated and now sendin mail");
			wizardMailService.sendResetPasswordMail(user);
		} catch (NamingException namingException) {
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (IOException ioException) {
				LOGGER.error(ERROR_IN_SENDING_STATUS_USING_WEB_SERVICE + ioException.toString());
			}
		} catch (WizardMailException wizardMailException) {
			try {
				response.sendError(HttpServletResponse.SC_CREATED);
			} catch (IOException ioMailException) {
				LOGGER.error(ERROR_IN_SENDING_STATUS_USING_WEB_SERVICE + ioMailException.toString());
			}
		}
	}

	/**
	 * To change the password.
	 * @param request {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 */
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	@ResponseBody
	public void changePassword(final HttpServletRequest request, final HttpServletResponse response) {
		final String userName = request.getParameter("userName");
		final String oldPassword = request.getParameter("oldPassword");
		final String newPassword = request.getParameter("newPassword");
		try {
			LOGGER.info("Change password for username: " + userName + " and old password: " + newPassword + " with new password: "
					+ newPassword);
			userConnectivityService.verifyandmodifyUserPassword(userName, oldPassword, newPassword);
			LOGGER.info("LDAP account updated, now updating DB");
			User user = userService.getUser(userName);
			user.setPassword(newPassword);
			user = userService.updateUser(user);
			LOGGER.info("DB updated and now sending mail");
			wizardMailService.sendChangePasswordMail(user);
		} catch (InvalidCredentials invalidCredentialException) {
			try {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
			} catch (IOException ioException) {
				LOGGER.error(ERROR_IN_SENDING_STATUS_USING_WEB_SERVICE + ioException.toString());
			}
		} catch (NamingException namingException) {
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (IOException ioException) {
				LOGGER.error(ERROR_IN_SENDING_STATUS_USING_WEB_SERVICE + ioException.toString());
			}
		} catch (WizardMailException wizardMailException) {
			try {
				response.sendError(HttpServletResponse.SC_CREATED);
			} catch (IOException ioMailException) {
				LOGGER.error(ERROR_IN_SENDING_STATUS_USING_WEB_SERVICE + ioMailException.toString());
			}
		}
	}

	private String getUniqueBatchClassName(String batchClassName) {
		BatchClass batchClass = batchClassService.getBatchClassbyName(batchClassName);
		String newbatchClassName = batchClassName;
		if (batchClass != null) {
			for (int i = WebserviceConstants.ONE; i < Integer.MAX_VALUE; i++) {
				newbatchClassName = batchClassName + i;
				BatchClass batchClass2 = batchClassService.getBatchClassbyName(newbatchClassName);
				if (batchClass2 == null) {
					break;
				}
			}
		}
		return newbatchClassName;
	}

	private User createUserObjectFromUserInformation(UserInformation userInformation) {
		User user = new User();
		user.setFirstName(userInformation.getFirstName());
		user.setLastName(userInformation.getLastName());
		user.setEmail(userInformation.getEmail());
		user.setPassword(userInformation.getPassword());
		user.setCompanyName(userInformation.getCompanyName());
		user.setPhoneNumber(userInformation.getPhoneNumber());
		return user;
	}

	/**
	 * To restart Batch Instance.
	 * @param batchInstanceIdentifier {@link String}
	 * @param moduleName {@link String}
	 * @param throwException boolean
	 * @return boolean
	 * @throws DCMAApplicationException if error occurs
	 */
	@RequestMapping(value = "/batchInstanceIdentifier/{batchInstanceIdentifier}/moduleName/{moduleName}/throwException/{throwException}", method = RequestMethod.GET)
	@ResponseBody
	public boolean restartBatchInstance(@PathVariable("batchInstanceIdentifier") final String batchInstanceIdentifier,
			@PathVariable("moduleName") final String moduleName, @PathVariable("throwException") final boolean throwException)
			throws DCMAApplicationException {
		return jbpmService.restartBatchInstance(batchInstanceIdentifier, moduleName, throwException);
	}

	/**
	 * To delete Batch Instance.
	 * @param batchInstanceIdentifier {@link String}
	 * @return boolean
	 * @throws DCMAApplicationException if error occurs
	 */
	@RequestMapping(value = "/batchInstanceIdentifier/{batchInstanceIdentifier}", method = RequestMethod.GET)
	@ResponseBody
	public boolean deleteBatchInstance(@PathVariable("batchInstanceIdentifier") final String batchInstanceIdentifier)
			throws DCMAApplicationException {
		return jbpmService.deleteBatchInstance(batchInstanceIdentifier);
	}
	
	/**
	 * To verify valid Ephesoft license is installed or not.
	 * @param request {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 */
	@RequestMapping(value = "/verifyEphesoftLicense", method = RequestMethod.POST)
	@ResponseBody
	public void verifyEphesoftLicense(final HttpServletRequest request, final HttpServletResponse response) {
		LOGGER.info("Start processing verify Ephesoft License web service");
		Boolean isLicenseInstalled = Boolean.FALSE;
		try {
			recostarService.generateHOCRFiles(null, null);
		    isLicenseInstalled = true;
			LOGGER.info("Ephesoft license is installed on this machine.");
		}  catch (DCMAException ex) {
			LOGGER.error("An exception is occured. Unable to verify license. " , ex);
		}  catch (Exception ex) {
			LOGGER.error("An exception is occured. Unable to verify license. " , ex);
		}
		LOGGER.info("Successfully executed verify Ephesoft License web service. Ephesoft License installed value is "
				+ isLicenseInstalled);
		try {
			response.getWriter().write(isLicenseInstalled.toString());
		}  catch (final IOException ioe) {
			LOGGER.error("Exception in sending message to client. Logged the exception for debugging.", ioe);
		} 	catch (final Exception e) {
			LOGGER.error("Exception in sending message to client. Logged the exception for debugging.", e);
		}
	}
}
