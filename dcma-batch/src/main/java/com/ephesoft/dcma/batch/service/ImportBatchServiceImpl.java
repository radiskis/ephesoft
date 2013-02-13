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

package com.ephesoft.dcma.batch.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.Folders.Folder;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.Scripts.Script;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassEmailConfiguration;
import com.ephesoft.dcma.da.domain.BatchClassGroups;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchClassModuleConfig;
import com.ephesoft.dcma.da.domain.BatchClassPlugin;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;
import com.ephesoft.dcma.da.domain.BatchClassScannerConfiguration;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.Module;
import com.ephesoft.dcma.da.domain.Plugin;
import com.ephesoft.dcma.da.domain.PluginConfig;
import com.ephesoft.dcma.da.domain.ScannerMasterConfiguration;
import com.ephesoft.dcma.da.service.BatchClassEmailConfigService;
import com.ephesoft.dcma.da.service.BatchClassPluginConfigService;
import com.ephesoft.dcma.da.service.BatchClassPluginService;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.da.service.MasterScannerService;
import com.ephesoft.dcma.da.service.ModuleService;
import com.ephesoft.dcma.da.service.PluginConfigService;
import com.ephesoft.dcma.da.service.PluginService;
import com.ephesoft.dcma.util.FileUtils;

/**
 * Service to import the batch class.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.batch.service.ImportBatchService
 */
public class ImportBatchServiceImpl implements ImportBatchService {

	/**
	 * WITH_FILE String.
	 */
	private static final String WITH_FILE = "with file : ";
	
	/**
	 * EMPTY_STRING String.
	 */
	private static final String EMPTY_STRING = "";
	
	/**
	 * SEMI_COLON String.
	 */
	private static final String SEMI_COLON = ";";
	
	/**
	 * INPUT_BATCH_XML String.
	 */
	private static final String INPUT_BATCH_XML = "backup.input_batch_xml";

	/**
	 * OUTPUT_BATCH_XML String.
	 */
	private static final String OUTPUT_BATCH_XML = "backup.output_batch_xml";
	
	/**
	 * INPUT_BATCH_XML_ZIP String.
	 */
	private static final String INPUT_BATCH_XML_ZIP = "backup.input_batch_xml_zip";
	
	/**
	 * SCRIPTING_PLUGIN String.
	 */
	private static final String SCRIPTING_PLUGIN = "Scripting_Plugin";

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ImportBatchServiceImpl.class);

	/**
	 * Instance of BatchSchemaService.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Instance of BatchClassService.
	 */
	@Autowired
	private BatchClassService batchClassService;

	/**
	 * Instance of BatchInstanceService.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * Instance of ModuleService.
	 */
	@Autowired
	private ModuleService moduleService;

	/**
	 * Instance of PluginService.
	 */ 
	@Autowired
	private PluginService pluginService;

	/**
	 * Instance of PluginConfigService.
	 */
	@Autowired
	private PluginConfigService pluginConfigService;

	/**
	 * Instance of BatchClassEmailConfigService.
	 */
	@Autowired
	private BatchClassEmailConfigService bcEmailConfigService;

	/**
	 * Instance of BatchClassPluginService.
	 */
	@Autowired
	private BatchClassPluginService batchClassPluginService;

	/**
	 * Instance of BatchClassPluginConfigService.
	 */
	@Autowired
	private BatchClassPluginConfigService batchClassPluginConfigService;

	/**
	 * Instance of MasterScannerService.
	 */
	@Autowired
	private MasterScannerService masterScannerService;

	private BatchClassPlugin getLastPluginFor(BatchClassModule previousBatchClassModule) {
		List<BatchClassPlugin> batchClassPlugins = previousBatchClassModule.getBatchClassPlugins();
		BatchClassPlugin lastBatchClassPlugin = null;
		for (BatchClassPlugin batchClassPlugin : batchClassPlugins) {
			if (lastBatchClassPlugin == null) {
				lastBatchClassPlugin = batchClassPlugin;
			} else {
				if (batchClassPlugin.getOrderNumber() > lastBatchClassPlugin.getOrderNumber()) {
					lastBatchClassPlugin = batchClassPlugin;
				}
			}
		}
		return lastBatchClassPlugin;
	}

	/**
	 * Method to act as utility for Restart Batch API.
	 * 
	 * @param properties {@link Properties}
	 * @param batchInstance {@link BatchInstance}
	 * @param moduleName {@link String}
	 * @param isZipSwitchOn boolean
	 * @throws DCMAApplicationException if unable to find restart option for batch instance
	 */
	@Override
	public void updateBatchFolders(Properties properties, BatchInstance batchInstance, String moduleName, boolean isZipSwitchOn)
			throws DCMAApplicationException {

		// removing .SER files of deleted batch instances
		boolean isRemoveSuccessful = removeFolders(batchInstance);
		if (!isRemoveSuccessful) {
			LOGGER.error("Exception in removing .SER files of batch Instance:" + batchInstance.getIdentifier()
					+ ". Continuing further in deleting folders.");
		}
		String batchXmlExtension = ICommonConstants.UNDERSCORE_BATCH_XML;
		if (properties != null) {
			batchXmlExtension = properties.getProperty(INPUT_BATCH_XML);
		}
		File batchInstanceFolder = new File(batchInstance.getLocalFolder() + File.separator + batchInstance.getIdentifier());
		String batchXmlPath = batchInstanceFolder.getAbsolutePath() + File.separator + batchInstance.getIdentifier()
				+ batchXmlExtension;

		File batchZipFile = null;
		File batchXmlFile = null;
		boolean isZip = false;
		if (isZipSwitchOn) {
			if (FileUtils.isZipFileExists(batchXmlPath)) {
				isZip = true;
			}
		} else {
			batchXmlFile = new File(batchXmlPath);
			if (batchXmlFile.exists()) {
				isZip = false;
			} else {
				isZip = true;
			}
		}

		LOGGER.info("isZip in restarting batch is : " + isZip);
		if (isZip) {
			batchZipFile = getBackUpXMLZipFile(properties, batchInstance, batchInstanceFolder);
		} else {
			batchXmlFile = getBackUpXMLFile(batchInstance, batchInstanceFolder, batchXmlPath);
		}

		List<BatchClassModule> batchClassModuleList = batchInstance.getBatchClass().getBatchClassModules();
		BatchClassModule currentBatchClassModule = null;
		for (BatchClassModule batchClassModule : batchClassModuleList) {
			if (moduleName.equalsIgnoreCase(batchClassModule.getWorkflowName())) {
				currentBatchClassModule = batchClassModule;
				break;
			}
		}
		if (currentBatchClassModule != null) {
			BatchClassModule previousBatchClassModule = null;
			for (BatchClassModule batchClassModule : batchClassModuleList) {
				if (previousBatchClassModule == null && batchClassModule.getOrderNumber() < currentBatchClassModule.getOrderNumber()) {
					previousBatchClassModule = batchClassModule;
				} else {
					if (previousBatchClassModule != null
							&& batchClassModule.getOrderNumber() > previousBatchClassModule.getOrderNumber()
							&& batchClassModule.getOrderNumber() < currentBatchClassModule.getOrderNumber()) {
						previousBatchClassModule = batchClassModule;
					}
				}
			}

			String batchBakXml = ICommonConstants.UNDERSCORE_BATCH_BAK_XML;
			if (properties != null) {
				batchBakXml = properties.getProperty(OUTPUT_BATCH_XML);
			}

			if (previousBatchClassModule != null) {
				BatchClassPlugin prevBatchClassPlugin = getLastPluginFor(previousBatchClassModule);
				String prevPluginFilePath = batchInstanceFolder.getAbsolutePath() + File.separator + batchInstance.getIdentifier()
						+ "_" + prevBatchClassPlugin.getPlugin().getWorkflowName() + batchBakXml;
				File prevPluginBatchXml = new File(prevPluginFilePath);

				if (!prevPluginBatchXml.exists() && !FileUtils.isZipFileExists(prevPluginFilePath)) {
					String prevPath = prevPluginBatchXml.getAbsolutePath();
					prevPluginFilePath = batchInstanceFolder.getAbsolutePath() + File.separator + batchInstance.getIdentifier() + BatchConstants.UNDER_SCORE
							+ previousBatchClassModule.getWorkflowName() + BatchConstants.UNDER_SCORE + SCRIPTING_PLUGIN + batchBakXml;
					prevPluginBatchXml = new File(prevPluginFilePath);
					if (!prevPluginBatchXml.exists() && !FileUtils.isZipFileExists(prevPluginFilePath)) {
						LOGGER.error("Unable to find backup batch xml for batch instance : " + batchInstance.getIdentifier()
								+ WITH_FILE + prevPluginBatchXml.getAbsolutePath() + "or " + prevPath);
						throw new DCMAApplicationException("Unable to update batch xml for batch instance : "
								+ batchInstance.getIdentifier() + WITH_FILE + prevPluginBatchXml.getAbsolutePath() + "or " + prevPath);
					}
				}
				copyFileInfo(batchInstance, batchZipFile, batchXmlFile, prevPluginBatchXml);
			}
		} else {
			LOGGER.error("Could not find restart option for batch instance : " + batchInstance.getIdentifier() + "restart option "
					+ moduleName);
			throw new DCMAApplicationException("Could not find restart option for batch instance : " + batchInstance.getIdentifier()
					+ "restart option " + moduleName);
		}
	}

	private File getBackUpXMLFile(BatchInstance batchInstance, File batchInstanceFolder, String batchXmlPath)
			throws DCMAApplicationException {
		File batchXmlFile;
		batchXmlFile = new File(batchXmlPath);
		File backupXmlFile = new File(batchInstanceFolder.getAbsolutePath() + File.separator + batchInstance.getIdentifier()
				+ ICommonConstants.UNDERSCORE_BAK_BATCH_XML);
		try {
			if (batchXmlFile.exists()) {
				FileUtils.copyFile(batchXmlFile, backupXmlFile);
			}
		} catch (IOException e) {
			LOGGER.error(BatchConstants.BACKUP_BATCH_FILE_NOT_CREATED + batchInstance.getIdentifier());
			throw new DCMAApplicationException(BatchConstants.BACKUP_BATCH_FILE_NOT_CREATED + batchInstance.getIdentifier(), e);
		}
		return batchXmlFile;
	}

	private void copyFileInfo(BatchInstance batchInstance, File batchZipFile, File batchXmlFile, File prevPluginBatchXml)
			throws DCMAApplicationException {
		String prevPluginFilePath;
		try {
			if (batchZipFile != null && batchZipFile.exists()) {
				prevPluginFilePath = prevPluginBatchXml + FileType.ZIP.getExtensionWithDot();
				FileUtils.copyFile(new File(prevPluginFilePath), batchZipFile);
			} else {
				FileUtils.copyFile(prevPluginBatchXml, batchXmlFile);
			}
		} catch (IOException e) {
			LOGGER.error("Unable to update batch xml for batch instance : " + batchInstance.getIdentifier() + WITH_FILE
					+ prevPluginBatchXml.getAbsolutePath());
			throw new DCMAApplicationException("Unable to update batch xml for batch instance : " + batchInstance.getIdentifier()
					+ WITH_FILE + prevPluginBatchXml.getAbsolutePath(), e);
		}
	}

	private File getBackUpXMLZipFile(Properties properties, BatchInstance batchInstance, File batchInstanceFolder)
			throws DCMAApplicationException {
		String batchXmlExtension;
		File batchZipFile;
		batchXmlExtension = ICommonConstants.UNDERSCORE_BATCH_XML_ZIP;
		if (properties != null) {
			batchXmlExtension = properties.getProperty(INPUT_BATCH_XML_ZIP);
		}
		String batchZipPath = batchInstanceFolder.getAbsolutePath() + File.separator + batchInstance.getIdentifier()
				+ batchXmlExtension;
		batchZipFile = new File(batchZipPath);
		File backupXmlZipFile = new File(batchInstanceFolder.getAbsolutePath() + File.separator + batchInstance.getIdentifier()
				+ ICommonConstants.UNDERSCORE_BAK_BATCH_XML_ZIP);
		try {
			FileUtils.copyFile(batchZipFile, backupXmlZipFile);
		} catch (IOException e) {
			LOGGER.error(BatchConstants.BACKUP_BATCH_FILE_NOT_CREATED + batchInstance.getIdentifier());
			throw new DCMAApplicationException(BatchConstants.BACKUP_BATCH_FILE_NOT_CREATED + batchInstance.getIdentifier(), e);
		}
		return batchZipFile;
	}

	/**
	 * Method to import the batch class as specified with the options XML.
	 * 
	 * @param optionXML {@link ImportBatchClassOptions}
	 * @param isDeployed boolean
	 * @param isFromWebService boolean
	 * @param userRolesToAssign {@link Set<{@link String}>}
	 * @return {@link Map<{@link Boolean}, {@link String}>}
	 */
	@Override
	public Map<Boolean, String> importBatchClass(ImportBatchClassOptions optionXML, boolean isDeployed, boolean isFromWebService,
			Set<String> userRolesToAssign) {
		Map<Boolean, String> resultsMap = new HashMap<Boolean, String>();
		String tempOutputUnZipDir = optionXML.getZipFilePath();
		File originalFolder = new File(tempOutputUnZipDir);
		String serializableFilePath = FileUtils.getFileNameOfTypeFromFolder(tempOutputUnZipDir, FileType.SER.getExtensionWithDot());

		InputStream serializableFileStream = null;
		try {
			serializableFileStream = new FileInputStream(serializableFilePath);
			// Import Into Database from the serialized file
			BatchClass importBatchClass = (BatchClass) SerializationUtils.deserialize(serializableFileStream);

			// delete serialization file from folder and create test-extraction folder
			new File(serializableFilePath).delete();
			new File(tempOutputUnZipDir + File.separator + batchSchemaService.getTestKVExtractionFolderName()).mkdir();
			new File(tempOutputUnZipDir + File.separator + batchSchemaService.getTestTableFolderName()).mkdir();

			// if from web service, implement workflow content equality check
			if (isFromWebService) {
				boolean isNameExistInDatabase = (batchClassService.getBatchClassByNameIncludingDeleted(optionXML.getName()) != null
						? true : false);
				if (!optionXML.isUseExisting() && isNameExistInDatabase) {
					String errorMessg = "Incorrect user input. Please specify another name for workflow while importing a new batch class. Returning from import.";
					resultsMap.put(false, errorMessg);
					LOGGER.error(errorMessg);
					return resultsMap;
				}

				boolean isEqual = isImportWorkflowEqualDeployedWorkflow(importBatchClass, optionXML.getName());
				if (isNameExistInDatabase && !isEqual) {
					String errorMessg = "Incorrect user input. Workflow name specified exists with different configuration. Returning from import.";
					resultsMap.put(false, errorMessg);
					LOGGER.error(errorMessg);
					return resultsMap;
				}

				// verify the contents are equal to the deployed work flow
				if (!optionXML.isImportIfConflict() && !isNameExistInDatabase && isDeployed) {
					String errorMessg = "Zip contains a workflow name that is already deployed but no batch class exists corresponding to this workflow name. It may be possible that the zip contains different configuration. Please specify another name for workflow.";
					resultsMap.put(false, errorMessg);
					LOGGER.error(errorMessg);
					return resultsMap;
				}
			}

			if (optionXML.getName() == null || optionXML.getName().isEmpty()) {
				optionXML.setName(importBatchClass.getName());
			}

			if (optionXML.isUseExisting()) {
				overrideExistingBatchClass(resultsMap, optionXML, tempOutputUnZipDir, originalFolder, importBatchClass,
						userRolesToAssign);
			} else {
				importNewBatchClass(resultsMap, optionXML, tempOutputUnZipDir, originalFolder, serializableFileStream,
						importBatchClass, userRolesToAssign);
			}
		} catch (Exception e) {
			String errorMessg = "Error while importing." + e.getMessage();
			resultsMap.put(false, errorMessg);
			LOGGER.error(errorMessg, e);
		}
		return resultsMap;
	}

	private void checkAndCreateNewSystemFolder(BatchClass dbBatchClass, BatchClass zipBatchClass) throws DCMAApplicationException {
		String existingSystemFolderPath = null;
		String newSystemFolderPath = null;
		if (null != dbBatchClass) {
			existingSystemFolderPath = dbBatchClass.getSystemFolder();

		}
		newSystemFolderPath = zipBatchClass.getSystemFolder();
		if (null != newSystemFolderPath) {
			File newSystemFolder = new File(newSystemFolderPath);
			if (existingSystemFolderPath == null
					|| (!existingSystemFolderPath.equals(newSystemFolderPath) && !newSystemFolder.exists())) {
				newSystemFolder.mkdirs();
				createAndLockFolder(newSystemFolder);
			}
		} else {
			String errorMessage = "Invalid value for system folder. System folder is null.";
			LOGGER.error(errorMessage);
			throw new DCMAApplicationException(errorMessage);
		}
	}

	/**
	 * @param newSystemFolder
	 * @throws Exception
	 */
	private void createAndLockFolder(File newSystemFolder) throws DCMAApplicationException {
		final boolean isLockAcquired = FileUtils.lockFolder(newSystemFolder.getPath());
		if (!isLockAcquired) {
			String errorMessage = "Could not acquire lock on the system folder.";
			LOGGER.error(errorMessage);
			throw new DCMAApplicationException(errorMessage);
		}
	}

	private void importNewBatchClass(Map<Boolean, String> resultsImport, ImportBatchClassOptions optionXML, String tempOutputUnZipDir,
			File originalFolder, InputStream serializableFileStream, BatchClass importBatchClass, Set<String> userRolesToAssign)
			throws IOException {
		// create a new batch class from zip file
		BatchClass tempImportBatchClass = importBatchClass;
		String errorMessg = BatchConstants.EMPTY;
		boolean isSuccess = true;
		resultsImport.put(isSuccess, errorMessg);
		try {
			tempImportBatchClass.setId(0L);
			List<ImportBatchClassOptions.BatchClassDefinition> bcdList = optionXML.getBatchClassDefinition();
			BatchClassDefinition bcd = bcdList.get(BatchConstants.ZERO);
			if (!optionXML.isRolesImported()) {
				// do not import the roles
				tempImportBatchClass.getAssignedGroups().clear();
			}

			for (Folder folder : bcd.getFolders().getFolder()) {
				if (batchSchemaService.getImagemagickBaseFolderName().equalsIgnoreCase(folder.getFileName()) && !folder.isSelected()) {

					// do not import the image samples
					String imagemagickBaseFolder = FileUtils.getFileNameOfTypeFromFolder(tempOutputUnZipDir, batchSchemaService
							.getImagemagickBaseFolderName());
					FileUtils.deleteDirectoryAndContentsRecursive(new File(imagemagickBaseFolder));

				}
				if (batchSchemaService.getSearchSampleName().equalsIgnoreCase(folder.getFileName()) && !folder.isSelected()) {

					// do not import the lucene samples
					String searchSampleFolder = FileUtils.getFileNameOfTypeFromFolder(tempOutputUnZipDir, batchSchemaService
							.getSearchSampleName());
					FileUtils.deleteDirectoryAndContentsRecursive(new File(searchSampleFolder));

				}
			}
			setCurrentUserRoleToBatchClass(tempImportBatchClass, userRolesToAssign);
			tempImportBatchClass.setSystemFolder(optionXML.getSystemFolder());
			if (!optionXML.isUseSource()) {
				tempImportBatchClass.setDescription(optionXML.getDescription());
				tempImportBatchClass.setName(optionXML.getName());
				tempImportBatchClass.setPriority(optionXML.getPriority());
			}
			new File(optionXML.getUncFolder()).mkdirs();
			tempImportBatchClass.setUncFolder(optionXML.getUncFolder());
			try {
				BatchClass dbBatchClass = batchClassService.getLoadedBatchClassByNameIncludingDeleted(optionXML.getName());
				if (dbBatchClass != null) {
					batchClassService.evict(dbBatchClass);
				}
				updateSerializableBatchClass(dbBatchClass, tempImportBatchClass);
				batchClassService.evict(tempImportBatchClass);
				tempImportBatchClass = batchClassService.createBatchClass(tempImportBatchClass);
				resultsImport.put(Boolean.TRUE, tempImportBatchClass.getIdentifier());
			} catch (Exception exception) {
				errorMessg = "Unable to create/override batch Class." + exception.getMessage();
				LOGGER.error(errorMessg, exception);
				isSuccess = false;
				resultsImport.put(isSuccess, errorMessg);
				new File(optionXML.getUncFolder()).delete();
			}

			File copiedFolder = new File(batchSchemaService.getBaseSampleFDLock() + File.separator
					+ tempImportBatchClass.getIdentifier());
			FileUtils.copyDirectoryWithContents(originalFolder, copiedFolder);

		} catch (FileNotFoundException e) {
			errorMessg = "Serializable file not found." + e.getMessage();
			LOGGER.error(errorMessg, e);
			isSuccess = false;
			resultsImport.put(isSuccess, errorMessg);
			new File(optionXML.getUncFolder()).delete();
		} catch (IOException e) {
			errorMessg = "Unable to copy the learning folders." + e.getMessage();
			LOGGER.error(errorMessg, e);
			isSuccess = false;
			resultsImport.put(isSuccess, errorMessg);
			new File(optionXML.getUncFolder()).delete();
		} finally {
			if (serializableFileStream != null) {
				serializableFileStream.close();
			}
			FileUtils.deleteDirectoryAndContentsRecursive(originalFolder);
			new File(optionXML.getZipFilePath()).delete();
		}
	}

	private void setCurrentUserRoleToBatchClass(BatchClass importBatchClass, Set<String> userRolesToAssign) {
		if (userRolesToAssign != null) {
			List<BatchClassGroups> batchClassGroupsList = importBatchClass.getAssignedGroups();
			for (String roleToAssign : userRolesToAssign) {
				boolean validRoleToAdd = true;
				for (BatchClassGroups batchClassGroup : batchClassGroupsList) {
					if (batchClassGroup.getGroupName().equals(roleToAssign)) {
						validRoleToAdd = false;
						break;
					}

				}
				if (validRoleToAdd) {
					BatchClassGroups batchClassGroup = new BatchClassGroups();
					batchClassGroup.setGroupName(roleToAssign);
					batchClassGroup.setId(BatchConstants.ZERO);
					batchClassGroup.setBatchClass(null);
					batchClassGroupsList.add(batchClassGroup);
				}
			}
			importBatchClass.setAssignedGroups(batchClassGroupsList);
		}
	}

	private void overrideExistingBatchClass(Map<Boolean, String> resultsImport, ImportBatchClassOptions optionXML,
			final String tempOutputUnZipDir, File originalFolder, BatchClass importBatchClass, Set<String> userRolesToAssign)
			throws IOException {
		BatchClass tempImportBatchClass = importBatchClass;
		// overriding a batch class
		BatchClass existingBatchClass = batchClassService.getLoadedBatchClassByUNC(optionXML.getUncFolder());
		batchClassService.evict(existingBatchClass);
		List<String> scriptsList = new ArrayList<String>();
		if (existingBatchClass != null) {
			// check the workflow name with UNC folder
			String existingUncFolder = existingBatchClass.getUncFolder();
			setCurrentUserRoleToBatchClass(tempImportBatchClass, userRolesToAssign);
			// update the configurations of imported batch class from DB / Zip
			if (!optionXML.isRolesImported()) {
				// import the roles from database
				updateRoles(tempImportBatchClass, existingBatchClass, Boolean.TRUE);
			}
			if (!optionXML.isEmailAccounts()) {
				updateEmailConfigurations(tempImportBatchClass, existingBatchClass, Boolean.TRUE);
			}
			List<ImportBatchClassOptions.BatchClassDefinition> bcdList = optionXML.getBatchClassDefinition();
			BatchClassDefinition bcd = bcdList.get(BatchConstants.ZERO);
			if (bcd.getScripts() != null && !bcd.getScripts().getScript().isEmpty()) {
				scriptsList = updateScriptsFiles(tempOutputUnZipDir, bcd.getScripts().getScript(), existingBatchClass.getIdentifier());
			}
			if (bcd.getFolders() != null && !bcd.getFolders().getFolder().isEmpty()) {
				for (Folder folder : bcd.getFolders().getFolder()) {
					if (!folder.isSelected()) {
						// import from database
						String zipBaseFolder = FileUtils.getFileNameOfTypeFromFolder(tempOutputUnZipDir, folder.getFileName());
						String existingBaseFolder = batchSchemaService.getAbsolutePath(existingBatchClass.getIdentifier(), folder
								.getFileName(), false);
						overrideFromDB(resultsImport, zipBaseFolder, existingBaseFolder, tempOutputUnZipDir);
						String errorMessgImport = resultsImport.get(Boolean.FALSE);
						if (errorMessgImport != null && !errorMessgImport.isEmpty()) {
							return;
						}
					}
				}
			}
			if (bcd.getBatchClassModules() != null && !bcd.getBatchClassModules().getBatchClassModule().isEmpty()) {
				for (com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.BatchClassModules.BatchClassModule bcm : bcd
						.getBatchClassModules().getBatchClassModule()) {
					updatePlgConfig(resultsImport, bcm, tempImportBatchClass, existingBatchClass);
					String errorMessgImport = resultsImport.get(Boolean.FALSE);
					if (errorMessgImport != null && !errorMessgImport.isEmpty()) {
						return;
					}
				}
			}
			boolean isSuccess = !resultsImport.containsKey(false);
			String errorMessg = "";
			if (isSuccess) {
				// override the existing batch class
				tempImportBatchClass.setId(0L);
				tempImportBatchClass.setUncFolder("dummyUncFolder" + System.currentTimeMillis());
				tempImportBatchClass.setSystemFolder(optionXML.getSystemFolder());
				if (!optionXML.isUseSource()) {
					tempImportBatchClass.setDescription(optionXML.getDescription());
					tempImportBatchClass.setName(optionXML.getName());
					tempImportBatchClass.setPriority(optionXML.getPriority());
				}
			}
			try {
				tempImportBatchClass = getOverridedBatchClass(resultsImport, tempOutputUnZipDir, originalFolder, tempImportBatchClass,
						existingBatchClass, scriptsList, existingUncFolder);
			} catch (Exception e) {
				errorMessg = "Error while overriding, reverting the changes:" + e.getMessage();
				isSuccess = false;
				resultsImport.put(isSuccess, errorMessg);
				LOGGER.info(errorMessg, e);
				tempImportBatchClass.setDeleted(true);
				tempImportBatchClass = batchClassService.merge(tempImportBatchClass, true);
			}

		}
	}

	private BatchClass getOverridedBatchClass(Map<Boolean, String> resultsImport, final String tempOutputUnZipDir,
			File originalFolder, BatchClass tempImportBatchClass, BatchClass existingBatchClass, List<String> scriptsList,
			String existingUncFolder) throws DCMAApplicationException {
		boolean isSuccess;
		String errorMessg;
		BatchClass localExistingBatchClass = existingBatchClass;
		BatchClass localTempImportBatchClass = tempImportBatchClass;
		updateSerializableBatchClass(localExistingBatchClass, localTempImportBatchClass);
		localTempImportBatchClass = batchClassService.createBatchClassWithoutWatch(localTempImportBatchClass);
		batchClassService.evict(localTempImportBatchClass);
		localTempImportBatchClass = batchClassService.getLoadedBatchClassByIdentifier(localTempImportBatchClass.getIdentifier());

		String dummyUncFolder = existingUncFolder + "-" + localExistingBatchClass.getIdentifier() + "-deleted";
		localExistingBatchClass = batchClassService.getBatchClassByIdentifier(localExistingBatchClass.getIdentifier());
		localExistingBatchClass.setUncFolder(dummyUncFolder);
		deleteEmailConfigForBatchClass(localExistingBatchClass);
		localExistingBatchClass = batchClassService.merge(localExistingBatchClass);
		try {
			localTempImportBatchClass.setUncFolder(existingUncFolder);
			localTempImportBatchClass = batchClassService.createBatchClass(localTempImportBatchClass);
			try {
				FileUtils.deleteSelectedFilesFromDirectory(tempOutputUnZipDir + File.separator
						+ batchSchemaService.getScriptFolderName(), scriptsList);
				File copiedFolder = new File(batchSchemaService.getBaseSampleFDLock() + File.separator
						+ localTempImportBatchClass.getIdentifier());
				FileUtils.copyDirectoryWithContents(originalFolder, copiedFolder);

				List<BatchInstance> newBatchInstances = batchInstanceService.getBatchInstByBatchClass(localExistingBatchClass);
				for (BatchInstance batchInstance : newBatchInstances) {
					if (batchInstance.getStatus().equals(BatchInstanceStatus.NEW)) {
						batchInstance.setBatchClass(localTempImportBatchClass);
						batchInstance.setPriority(localTempImportBatchClass.getPriority());
						batchInstanceService.updateBatchInstance(batchInstance);
					}
				}
				resultsImport.put(Boolean.TRUE, localTempImportBatchClass.getIdentifier());
			} catch (Exception e) {
				errorMessg = "Error while overriding, reverting the changes:" + e.getMessage();
				LOGGER.info(errorMessg, e);
				isSuccess = false;
				resultsImport.put(isSuccess, errorMessg);
				localTempImportBatchClass.setDeleted(true);
				localTempImportBatchClass = batchClassService.merge(localTempImportBatchClass, true);

				localExistingBatchClass.setUncFolder(existingUncFolder);
				localExistingBatchClass = batchClassService.merge(localExistingBatchClass);
			}
			localExistingBatchClass.setDeleted(true);
			localExistingBatchClass = batchClassService.merge(localExistingBatchClass, true);
			new File(dummyUncFolder).mkdirs();
		} catch (Exception e) {
			errorMessg = "Error while overriding, reverting the changes:" + e.getMessage();
			isSuccess = false;
			resultsImport.put(isSuccess, errorMessg);
			LOGGER.info(errorMessg, e);
			localExistingBatchClass.setUncFolder(existingUncFolder);
			localExistingBatchClass = batchClassService.merge(localExistingBatchClass);
		}
		return localTempImportBatchClass;
	}

	private void deleteEmailConfigForBatchClass(final BatchClass batchClass) {
		List<BatchClassEmailConfiguration> emailConfigList = bcEmailConfigService.getEmailConfigByBatchClassIdentifier(batchClass
				.getIdentifier());
		for (BatchClassEmailConfiguration batchClassEmailConfiguration : emailConfigList) {
			bcEmailConfigService.removeEmailConfiguration(batchClassEmailConfiguration);
		}
	}

	/**
	 * To update Serializable Batch Class.
	 * 
	 * @param dbBatchClass {@link BatchClass} 
	 * @param serializedBatchClass {@link BatchClass}
	 * @throws DCMAApplicationException if required scanner configuration is not found
	 */
	public void updateSerializableBatchClass(BatchClass dbBatchClass, BatchClass serializedBatchClass) throws DCMAApplicationException {
		// fix for "Not Mapped yet." issue while importing old batch classes.
		checkAndCreateNewSystemFolder(dbBatchClass, serializedBatchClass);
		List<DocumentType> documentTypes = serializedBatchClass.getDocumentTypes();
		if (documentTypes != null) {
			for (DocumentType documentType : documentTypes) {
				if (documentType.getRspProjectFileName() != null
						&& documentType.getRspProjectFileName().equalsIgnoreCase("Not Mapped yet.")) {
					documentType.setRspProjectFileName(null);
				}
			}
		}

		List<BatchClassModule> batchClassModules = serializedBatchClass.getBatchClassModules();
		for (BatchClassModule batchClassModule : batchClassModules) {
			Module currentModule = moduleService.getModuleByName(batchClassModule.getModule().getName());
			if (currentModule == null) {
				continue;
			} else {
				batchClassModule.setModule(currentModule);
			}
	
			batchClassModule.getBatchClassModuleConfig().clear();
			batchClassModule.setBatchClassModuleConfig(new ArrayList<BatchClassModuleConfig>());

			List<BatchClassPlugin> batchClassPlugins = batchClassModule.getBatchClassPlugins();
			for (BatchClassPlugin batchClassPlugin : batchClassPlugins) {
				Plugin currentPlugin = pluginService.getPluginPropertiesForPluginName(batchClassPlugin.getPlugin().getPluginName());
				if (currentPlugin == null) {
					continue;
				} else {
					batchClassPlugin.setPlugin(currentPlugin);
				}
				List<BatchClassPluginConfig> batchClassPluginConfigs = batchClassPlugin.getBatchClassPluginConfigs();
				Iterator<BatchClassPluginConfig> iter = batchClassPluginConfigs.iterator();
				while (iter.hasNext()) {
					BatchClassPluginConfig batchClassPluginConfig = iter.next();
					PluginConfig currentPluginConfig = pluginConfigService.getPluginConfigByName(batchClassPluginConfig
							.getPluginConfig().getName());

					if (currentPluginConfig == null) {
						continue;
					} else {
						batchClassPluginConfig.setPluginConfig(currentPluginConfig);
					}
				}
			}
		}
		updateScannerConfig(serializedBatchClass.getBatchClassScannerConfiguration());
		updateBatchClassModules(batchClassModules);
	}

	/**
	 * To update the scanner configuration.
	 * 
	 * @param batchClassScannerConfigs List<BatchClassScannerConfiguration>
	 */
	public void updateScannerConfig(List<BatchClassScannerConfiguration> batchClassScannerConfigs) {
		if (batchClassScannerConfigs == null || batchClassScannerConfigs.isEmpty()) {
			LOGGER.info("No Scanner Configs present in the imported batch class.");
			return;
		}
		for (BatchClassScannerConfiguration batchClassScannerConfig : batchClassScannerConfigs) {
			ScannerMasterConfiguration importedScannerMasterConfig = batchClassScannerConfig.getScannerMasterConfig();
			if (null != importedScannerMasterConfig) {
				String masterScannerConfigName = importedScannerMasterConfig.getName();
				ScannerMasterConfiguration scannerMasterConfig = masterScannerService
						.getScannerMasterConfigForProfile(masterScannerConfigName);
				if (scannerMasterConfig != null) {
					batchClassScannerConfig.setScannerMasterConfig(scannerMasterConfig);
				} else {
					LOGGER.error("Scanner config with name:" + masterScannerConfigName
							+ " is not present in the scanner_master_configuration table. So skipping this scanner config.");
				}
			}
		}

	}

	private void updateBatchClassModules(List<BatchClassModule> zipBatchClassModules) {
		List<BatchClassModule> validBatchClassModules = new ArrayList<BatchClassModule>();
		for (BatchClassModule zipBatchClassModule : zipBatchClassModules) {
			updateBatchClassPlugins(zipBatchClassModule.getBatchClassPlugins());
			validBatchClassModules.add(zipBatchClassModule);
		}
		if (validBatchClassModules != null) {
			zipBatchClassModules.clear();
			zipBatchClassModules.addAll(validBatchClassModules);
		}
	}

	private void updateBatchClassPlugins(List<BatchClassPlugin> zipBatchClassPlugins) {
		List<BatchClassPlugin> validBatchClassPlugins = new ArrayList<BatchClassPlugin>();
		for (BatchClassPlugin zipBatchClassPlugin : zipBatchClassPlugins) {
			updateBatchClassPluginConfigs(zipBatchClassPlugin);
			validBatchClassPlugins.add(zipBatchClassPlugin);
		}
		if (validBatchClassPlugins != null) {
			zipBatchClassPlugins.clear();
			zipBatchClassPlugins.addAll(validBatchClassPlugins);
		}
	}

	private void updateBatchClassPluginConfigs(BatchClassPlugin zipBatchClassPlugin) {
		getBatchClassPluginConfigs(zipBatchClassPlugin);
	}

	private List<BatchClassPluginConfig> addBatchClassPluginConfigsForNewPlugin(List<PluginConfig> existingPluginConfig,
			List<BatchClassPluginConfig> zipBatchClassPluginConfigs) {
		List<BatchClassPluginConfig> validBCPluginConfigs = new ArrayList<BatchClassPluginConfig>();
		for (PluginConfig pluginConfig : existingPluginConfig) {
			boolean pluginConfigExits = false;
			String pluginConfigName = pluginConfig.getName();
			for (BatchClassPluginConfig batchClassPluginConfig : zipBatchClassPluginConfigs) {
				String existingPluginConfigName = batchClassPluginConfig.getName();
				if (existingPluginConfigName.equals(pluginConfigName)) {
					updateBatchClassPluginConfigValue(batchClassPluginConfig, pluginConfig);
					pluginConfigExits = true;
					validBCPluginConfigs.add(batchClassPluginConfig);
					break;
				}
			}
			if (!pluginConfigExits) {
				BatchClassPluginConfig batchClassPluginConfig = new BatchClassPluginConfig();
				batchClassPluginConfig.setPluginConfig(pluginConfig);
				setDefaultValueForNewConfig(batchClassPluginConfig);
				validBCPluginConfigs.add(batchClassPluginConfig);
			}
		}
		return validBCPluginConfigs;
	}

	private List<BatchClassPluginConfig> addBatchClassPluginConfigsForExitingPlugin(Plugin dbBatchClassPlugin,
			BatchClassPlugin zipBatchClassPlugin) {
		List<BatchClassPluginConfig> validBCPluginConfigs = new ArrayList<BatchClassPluginConfig>();
		long batchClassPluginId = -BatchConstants.ONE;
		long pluginId = dbBatchClassPlugin.getId();
		List<BatchClassPluginConfig> batchClassPluginConfigsList = new ArrayList<BatchClassPluginConfig>(0);
		try {
			List<BatchClassPlugin> batchClassPlugins = batchClassPluginService.getBatchClassPluginForPluginId(pluginId);
			for (BatchClassPlugin batchClassPlugin : batchClassPlugins) {
				if (batchClassPlugin.getPlugin().getPluginName().equals(dbBatchClassPlugin.getPluginName())) {
					batchClassPluginId = batchClassPlugin.getId();
					break;
				}

			}
		} catch (NumberFormatException e) {
			LOGGER.error("Error converting number: " + e.getMessage(), e);
		}
		if (batchClassPluginId != -BatchConstants.ONE) {
			batchClassPluginConfigsList = batchClassPluginConfigService.getPluginConfigurationForPluginId(batchClassPluginId);

			if (batchClassPluginConfigsList != null) {
				List<PluginConfig> existingPluginConfigs = new ArrayList<PluginConfig>();
				for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigsList) {
					PluginConfig pluginConfig = batchClassPluginConfig.getPluginConfig();
					existingPluginConfigs.add(pluginConfig);
				}
				validBCPluginConfigs = addBatchClassPluginConfigsForNewPlugin(existingPluginConfigs, zipBatchClassPlugin
						.getBatchClassPluginConfigs());
			}

		}
		return validBCPluginConfigs;
	}

	private void getBatchClassPluginConfigs(BatchClassPlugin zipBatchClassPlugin) {
		long pluginId;// = dbBatchClassPlugin.getPlugin().getId();
		String pluginName = zipBatchClassPlugin.getPlugin().getPluginName();
		Plugin plugin = pluginService.getPluginPropertiesForPluginName(pluginName);
		pluginId = plugin.getId();
		List<PluginConfig> pluginConfigs = pluginConfigService.getPluginConfigForPluginId(String.valueOf(pluginId));
		List<BatchClassPluginConfig> newBatchClassClassPluginConfigs = null;
		if (pluginConfigs != null && !pluginConfigs.isEmpty()) {
			// Newly added plugins
			newBatchClassClassPluginConfigs = addBatchClassPluginConfigsForNewPlugin(pluginConfigs, zipBatchClassPlugin
					.getBatchClassPluginConfigs());
		} else {
			// workaround for Plugins added before 3.0 with no entry for plugin id in plugin config table.
			newBatchClassClassPluginConfigs = addBatchClassPluginConfigsForExitingPlugin(plugin, zipBatchClassPlugin);
		}

		if (newBatchClassClassPluginConfigs != null) {
			zipBatchClassPlugin.getBatchClassPluginConfigs().clear();
			zipBatchClassPlugin.getBatchClassPluginConfigs().addAll(newBatchClassClassPluginConfigs);
		}

	}

	private static void setDefaultValueForNewConfig(BatchClassPluginConfig batchClassPluginConfig) {
		DataType pluginConfigDataType = batchClassPluginConfig.getPluginConfig().getDataType();
		if (pluginConfigDataType == DataType.BOOLEAN) {
			batchClassPluginConfig.setValue("Yes");
		} else {
			boolean isMandatory = batchClassPluginConfig.getPluginConfig().isMandatory();
			if (pluginConfigDataType == DataType.STRING && isMandatory) {
				List<String> sampleValues = batchClassPluginConfig.getSampleValue();
				if (sampleValues == null || sampleValues.isEmpty()) {
					batchClassPluginConfig.setValue("Default");
				} else if (sampleValues.size() > 0) {
					batchClassPluginConfig.setValue(sampleValues.get(0));
				}
			} else if (pluginConfigDataType == DataType.INTEGER && isMandatory) {
				batchClassPluginConfig.setValue("0");
			}

		}
	}

	private void updateBatchClassPluginConfigValue(BatchClassPluginConfig zipBatchClassPluginConfig, PluginConfig pluginConfig) {
		PluginConfig tempPluginConfig = pluginConfig;
		if (tempPluginConfig != null) {
			tempPluginConfig = pluginConfigService.getPluginConfigByName(tempPluginConfig.getName());
			// get sample values for that plugin config.
			List<String> pcSampleValue = tempPluginConfig.getSampleValue();
			// get batch class plugin config value in zip.
			String bcpcValue = zipBatchClassPluginConfig.getValue();
			// Check if batch class plugin config value is valid or not.
			if (pcSampleValue != null && !pcSampleValue.isEmpty() && !isBcpcValueExistsInDB(pcSampleValue, bcpcValue)) {
				// update imported batch class plugin value with plugin config value for system batch class.
				zipBatchClassPluginConfig.setValue(pcSampleValue.get(0));
			}
		}
	}

	private boolean isBcpcValueExistsInDB(List<String> dbBatchClassPluginConfig, String zipBcpcValue) {
		boolean isPresent = false;
		if (zipBcpcValue != null) {
			String[] valueArray = zipBcpcValue.split(SEMI_COLON);
			for (int index = BatchConstants.ZERO; index < valueArray.length; index++) {
				String eachValue = valueArray[index];
				for (String sampleValue : dbBatchClassPluginConfig) {
					// Check if BCPC value exists in database.
					if (sampleValue.equalsIgnoreCase(eachValue)) {
						isPresent = true;
						break;
					}
				}
				if (!isPresent) {
					// Value not present in sample values. Return false.
					break;
				} else if (index == valueArray.length - BatchConstants.ONE) {
					// Reached end of array. Each BCPC value present. Return true.
					break;
				} else {
					// BCPC value present. Continue with next value in array.
					isPresent = false;
				}
			}
		}
		return isPresent;
	}

	/**
	 * To update plugin configuration.
	 * 
	 * @param resultsImport Map<Boolean, String>
	 * @param bcm com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.BatchClassModules.BatchClassModule
	 * @param importBatchClass {@link BatchClass}
	 * @param existingBatchClass {@link BatchClass}
	 */
	public void updatePlgConfig(Map<Boolean, String> resultsImport,
			com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.BatchClassModules.BatchClassModule bcm,
			BatchClass importBatchClass, BatchClass existingBatchClass) {
		String errorMessg = BatchConstants.EMPTY;
		boolean results = true;
		BatchClassModule importBatchClassModule = importBatchClass.getBatchClassModuleByWorkflowName(bcm.getModuleName());
		BatchClassModule existingBatchClassModule = batchSchemaService.getDetachedBatchClassModuleByName(existingBatchClass
				.getIdentifier(), bcm.getModuleName());
		if (!bcm.isPluginConfiguration()) {
			// import the plug-in configuration for the module from database
			if (existingBatchClassModule == null || importBatchClassModule == null) {
				errorMessg = "Either no module found with workflowName:\"" + bcm.getModuleName() + "\" in batch class id:"
						+ existingBatchClass.getIdentifier()
						+ " or no module found in zip batch class. Returning with error while batch class import.";
				LOGGER.error(errorMessg);
				results = false;
			} else {
				List<BatchClassModule> importedBatchClassMod = importBatchClass.getBatchClassModules();
				importedBatchClassMod.set(importedBatchClassMod.indexOf(importBatchClassModule), existingBatchClassModule);
			}
			resultsImport.put(results, errorMessg);

		} else {
			if (importBatchClassModule == null) {
				// not found in zip throw in error
				errorMessg = "No module found with workflowName:\"" + bcm.getModuleName()
						+ "\" in zip batch class. Returning with error while batch class import.";
				LOGGER.error(errorMessg);
				results = false;
				resultsImport.put(results, errorMessg);
			}
		}
	}

	/**
	 * To update roles of assigned gropus.
	 * 
	 * @param importBatchClass {@link BatchClass}
	 * @param existingBatchClass {@link BatchClass}
	 * @param isFromDB Boolean
	 */
	public void updateRoles(BatchClass importBatchClass, BatchClass existingBatchClass, Boolean isFromDB) {
		List<BatchClassGroups> batchClassGroups = new ArrayList<BatchClassGroups>();
		if (isFromDB) {
			batchClassGroups = existingBatchClass.getAssignedGroups();
		} else {
			batchClassGroups = importBatchClass.getAssignedGroups();
		}
		List<BatchClassGroups> newBatchClassGroupsList = new ArrayList<BatchClassGroups>();
		for (BatchClassGroups batchClassGroup : batchClassGroups) {
			newBatchClassGroupsList.add(batchClassGroup);
			batchClassGroup.setId(BatchConstants.ZERO);
			batchClassGroup.setBatchClass(null);
		}
		if (isFromDB) {
			importBatchClass.getAssignedGroups().clear();
			importBatchClass.setAssignedGroups(newBatchClassGroupsList);
		} else {
			existingBatchClass.getAssignedGroups().clear();
			existingBatchClass.setAssignedGroups(newBatchClassGroupsList);
		}
	}

	/**
	 * To update email configuration.
	 * 
	 * @param importBatchClass {@link BatchClass}
	 * @param existingBatchClass {@link BatchClass}
	 * @param isFromDB Boolean
	 */
	public void updateEmailConfigurations(BatchClass importBatchClass, BatchClass existingBatchClass, Boolean isFromDB) {
		List<BatchClassEmailConfiguration> batchClassEmailConfigurations = new ArrayList<BatchClassEmailConfiguration>();
		if (isFromDB) {
			batchClassEmailConfigurations = existingBatchClass.getEmailConfigurations();
		} else {
			batchClassEmailConfigurations = importBatchClass.getEmailConfigurations();
		}

		List<BatchClassEmailConfiguration> newBatchClassEmailConfigurationList = new ArrayList<BatchClassEmailConfiguration>();
		for (BatchClassEmailConfiguration batchClassEmailConfiguration : batchClassEmailConfigurations) {
			newBatchClassEmailConfigurationList.add(batchClassEmailConfiguration);
			batchClassEmailConfiguration.setId(BatchConstants.ZERO);
			batchClassEmailConfiguration.setBatchClass(null);
		}
		if (isFromDB) {
			importBatchClass.getEmailConfigurations().clear();
			importBatchClass.setEmailConfigurations(newBatchClassEmailConfigurationList);
		} else {
			existingBatchClass.getEmailConfigurations().clear();
			existingBatchClass.setEmailConfigurations(newBatchClassEmailConfigurationList);
		}
	}

	/**
	 * To override folder from database.
	 * 
	 * @param resultsImport Map<Boolean, String> 
	 * @param zipFolderName String
	 * @param existingFolderName String
	 * @param zipDir String
	 */
	public void overrideFromDB(Map<Boolean, String> resultsImport, String zipFolderName, String existingFolderName, String zipDir) {
		String tempZipFolderName = zipFolderName;
		String errorMessg = BatchConstants.EMPTY;
		boolean isSuccess = true;
		// import the samples from database
		if (tempZipFolderName.isEmpty()) {
			// create a folder in the directory of zip file
			tempZipFolderName = new File(tempZipFolderName).getAbsolutePath();
		} else {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(tempZipFolderName), false);
		}
		if (tempZipFolderName != null && !tempZipFolderName.isEmpty() && existingFolderName != null && !existingFolderName.isEmpty()) {

			boolean copiedSuccessfully = true;
			try {
				FileUtils.copyDirectoryWithContents(existingFolderName, tempZipFolderName);
			} catch (IOException ioe) {
				errorMessg = "Unable to override folder" + existingFolderName + " for batch class.";
				LOGGER.info(errorMessg + ioe, ioe);
				copiedSuccessfully = false;
			}
			if (!copiedSuccessfully) {
				isSuccess = false;
				resultsImport.put(isSuccess, errorMessg);
			}

		}
	}

	/**
	 * To update the scripts files.
	 * 
	 * @param tempOutputUnZipDir String
	 * @param allScriptsList List<ImportBatchClassOptions.BatchClassDefinition.Scripts.Script>
	 * @param existingBCID String
	 * @return List<String>
	 * @throws IOException if case error occurs
	 */
	public List<String> updateScriptsFiles(String tempOutputUnZipDir,
			List<ImportBatchClassOptions.BatchClassDefinition.Scripts.Script> allScriptsList, String existingBCID) throws IOException {
		List<String> scriptsList = new ArrayList<String>();
		if (!allScriptsList.isEmpty()) {
			String existingScriptFilePath = EMPTY_STRING;
			String zipScriptFilePath = EMPTY_STRING;
			String scriptsFolderName = batchSchemaService.getScriptFolderName();
			for (Script script : allScriptsList) {
				String scriptFileName = script.getFileName();
				boolean isSelected = script.isSelected();

				existingScriptFilePath = FileUtils.getFileNameOfTypeFromFolder(batchSchemaService.getAbsolutePath(existingBCID,
						scriptsFolderName, false), scriptFileName);
				zipScriptFilePath = FileUtils.getFileNameOfTypeFromFolder(tempOutputUnZipDir + File.separator + scriptsFolderName,
						scriptFileName);

				File zipScriptFile = null;
				File existingScriptFile = null;
				if (zipScriptFilePath != null && !zipScriptFilePath.isEmpty()) {
					zipScriptFile = new File(zipScriptFilePath);
				}
				if (!isSelected) {
					// import the script for the module from database

					if (zipScriptFile != null && zipScriptFile.exists()) {
						zipScriptFile.delete();
					}
					if (existingScriptFilePath != null && !existingScriptFilePath.isEmpty()) {
						existingScriptFile = new File(existingScriptFilePath);
					}
					if (existingScriptFile != null && existingScriptFile.exists()) {
						if (zipScriptFile == null) {
							zipScriptFile = new File(tempOutputUnZipDir + File.separator + scriptsFolderName + File.separator
									+ existingScriptFile.getName());
						}

						FileUtils.copyFile(existingScriptFile, zipScriptFile);
						scriptsList.add(zipScriptFile.getName());
					}
				} else {
					if (zipScriptFile != null) {
						scriptsList.add(zipScriptFile.getName());
					}
				}
			}
		}
		return scriptsList;
	}

	/**
	 * To validate the XML input.
	 * 
	 * @param optionXML {@link ImportBatchClassOptions}
	 * @return Map<Boolean, String>
	 */
	@Override
	public Map<Boolean, String> validateInputXML(ImportBatchClassOptions optionXML) {
		boolean isValid = true;
		String errorMessg = BatchConstants.EMPTY;
		// check for the description and priority from the XML file
		if (!optionXML.isUseSource()
				&& (optionXML.getName() == null || optionXML.getDescription() == null || optionXML.getName().isEmpty()
						|| optionXML.getDescription().isEmpty() || optionXML.getPriority() <= BatchConstants.ZERO || optionXML.getPriority() > BatchConstants.HUNDRED)) {
			errorMessg = "Either name, description is empty or priority is not between 1 and 100 inclusive.";
			isValid = false;
		}
		if (isValid) {
			String name = optionXML.getName();
			if (name.indexOf(BatchConstants.SPACE) > -BatchConstants.ONE || name.indexOf(BatchConstants.HYPHEN) > -BatchConstants.ONE) {
				errorMessg = "Workflow name should not contain space or hyphen character.";
				isValid = false;
			}
		}
		if (isValid) {
			if (optionXML.getUncFolder() == null || optionXML.getUncFolder().isEmpty()) {
				errorMessg = "UNC folder is not specified.";
				isValid = false;
			} else {
				File unc = new File(optionXML.getUncFolder());
				if (optionXML.isUseExisting()) {
					// check for the existence of UNC folder in case it is override batch class
					isValid = unc.exists();
					if (!isValid) {
						errorMessg = "UNC folder does not exist. Can not override an existing batch class.";
					}
				} else {
					// check for NOT existence of UNC folder in case it is new batch class
					isValid = !unc.exists();
					if (!isValid) {
						errorMessg = "UNC folder exists already. Can not import a new batch class with the existing UNC folder.";
					}
				}
			}
		}
		Map<Boolean, String> results = new HashMap<Boolean, String>();
		results.put(isValid, errorMessg);
		return results;
	}

	/**
	 * To check whether Import Workflow equals Deployed Workflow.
	 * 
	 * @param importBatchClass {@link BatchClass}
	 * @param userInputWorkflowName {@link String}
	 * @return boolean
	 */
	@Override
	public boolean isImportWorkflowEqualDeployedWorkflow(final BatchClass importBatchClass, final String userInputWorkflowName) {
		BatchClass dbBatchClass = batchClassService.getBatchClassByNameIncludingDeleted(userInputWorkflowName);
		boolean isEqual = true;
		// check for the number of modules
		if (importBatchClass != null && dbBatchClass != null) {
			List<BatchClassModule> zipBCMs = importBatchClass.getBatchClassModules();
			List<BatchClassModule> dbBCMs = dbBatchClass.getBatchClassModules();
			if (dbBCMs.size() == zipBCMs.size()) {
				bcmLoop: for (int index = BatchConstants.ZERO; index < dbBCMs.size(); index++) {
					BatchClassModule zipBCM = zipBCMs.get(index);
					BatchClassModule dbBCM = dbBCMs.get(index);
					if (!dbBCM.getWorkflowName().equals(zipBCM.getWorkflowName())) {
						isEqual = false;
						break;
					}

					List<BatchClassPlugin> zipBCPs = zipBCM.getBatchClassPlugins();
					List<BatchClassPlugin> dbBCPs = dbBCM.getBatchClassPlugins();
					if (dbBCPs.size() == zipBCPs.size()) {
						for (int indexPlg = BatchConstants.ZERO; indexPlg < dbBCPs.size(); indexPlg++) {
							BatchClassPlugin zipBCP = zipBCPs.get(indexPlg);
							BatchClassPlugin dbBCP = dbBCPs.get(indexPlg);
							if (!dbBCP.getPlugin().getWorkflowName().equals(zipBCP.getPlugin().getWorkflowName())) {
								isEqual = false;
								break bcmLoop;
							}
						}
					} else {
						isEqual = false;
						break bcmLoop;
					}

				}
			} else {
				isEqual = false;
			}
		} else {
			isEqual = false;
		}
		return isEqual;
	}

	/**
	 * To remove folders from given batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @return boolean
	 */
	@Override
	public boolean removeFolders(BatchInstance batchInstance) {
		boolean isDeleted = true;
		File propertiesFile = new File(batchInstance.getLocalFolder() + File.separator + BatchConstants.PROPERTIES_DIRECTORY
				+ File.separator + batchInstance.getIdentifier() + FileType.SER.getExtensionWithDot());

		if (null != propertiesFile) {
			isDeleted &= propertiesFile.delete();
		}
		return isDeleted;
	}
}
