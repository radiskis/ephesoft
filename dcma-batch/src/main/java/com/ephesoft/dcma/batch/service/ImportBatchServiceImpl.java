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

import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.Folders.Folder;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.Scripts.Script;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassEmailConfiguration;
import com.ephesoft.dcma.da.domain.BatchClassGroups;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchClassModuleConfig;
import com.ephesoft.dcma.da.domain.BatchClassPlugin;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.Module;
import com.ephesoft.dcma.da.domain.ModuleConfig;
import com.ephesoft.dcma.da.domain.Plugin;
import com.ephesoft.dcma.da.domain.PluginConfig;
import com.ephesoft.dcma.da.service.BatchClassEmailConfigService;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.da.service.ModuleConfigService;
import com.ephesoft.dcma.da.service.ModuleService;
import com.ephesoft.dcma.da.service.PluginConfigService;
import com.ephesoft.dcma.da.service.PluginService;
import com.ephesoft.dcma.util.FileUtils;

/**
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.batch.service.ImportBatchServiceImpl
 */
public class ImportBatchServiceImpl implements ImportBatchService {

	private static final String EMPTY_STRING = "";

	private static final String SEMI_COLON = ";";
	
	private static final String INPUT_BATCH_XML = "backup.input_batch_xml";
	private static final String OUTPUT_BATCH_XML = "backup.output_batch_xml";
	private static final String INPUT_BATCH_XML_ZIP = "backup.input_batch_xml_zip";
	private static final String SCRIPTING_PLUGIN = "Scripting_Plugin";


	/**
	 * LOGGER to print the logging information.
	 */
	private Logger logger = LoggerFactory.getLogger(ImportBatchServiceImpl.class);

	@Autowired
	BatchSchemaService batchSchemaService;

	@Autowired
	BatchClassService batchClassService;

	@Autowired
	BatchInstanceService batchInstanceService;

	@Autowired
	private ModuleService moduleService;
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
	private PluginConfigService pluginConfigService;
	
	@Autowired
	private ModuleConfigService moduleConfigService;
	
	@Autowired
	private BatchClassEmailConfigService bcEmailConfigService;
	
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

	@Override
	public void updateBatchFolders(Properties properties , BatchInstance batchInstance, String moduleName, boolean isZipSwitchOn) throws Exception {
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

		logger.info("isZip in restarting batch is : " + isZip);
		if (isZip) {
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
			} catch (Exception e) {
				logger.error("Unable to create backup copy of batch file for batch instance : " + batchInstance.getIdentifier());
				throw new Exception("Unable to create backup copy of batch file for batch instance : "
						+ batchInstance.getIdentifier());
			}
		} else {
			batchXmlFile = new File(batchXmlPath);
			File backupXmlFile = new File(batchInstanceFolder.getAbsolutePath() + File.separator + batchInstance.getIdentifier()
					+ ICommonConstants.UNDERSCORE_BAK_BATCH_XML);
			try {
				if (batchXmlFile.exists()) {
					FileUtils.copyFile(batchXmlFile, backupXmlFile);
				}
			} catch (Exception e) {
				logger.error("Unable to create backup copy of batch file for batch instance : " + batchInstance.getIdentifier());
				throw new Exception("Unable to create backup copy of batch file for batch instance : "
						+ batchInstance.getIdentifier());
			}

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
					prevPluginFilePath = batchInstanceFolder.getAbsolutePath() + File.separator + batchInstance.getIdentifier() + "_"
							+ previousBatchClassModule.getWorkflowName() + "_" + SCRIPTING_PLUGIN + batchBakXml;
					prevPluginBatchXml = new File(prevPluginFilePath);
					if (!prevPluginBatchXml.exists() && !FileUtils.isZipFileExists(prevPluginFilePath)) {
						logger.error("Unable to find backup batch xml for batch instance : " + batchInstance.getIdentifier()
								+ "with file : " + prevPluginBatchXml.getAbsolutePath() + "or " + prevPath);
						throw new Exception("Unable to update batch xml for batch instance : " + batchInstance.getIdentifier()
								+ "with file : " + prevPluginBatchXml.getAbsolutePath() + "or " + prevPath);
					}
				}

				try {
					if (batchZipFile != null && batchZipFile.exists()) {
						prevPluginFilePath = prevPluginBatchXml + FileType.ZIP.getExtensionWithDot();
						FileUtils.copyFile(new File(prevPluginFilePath), batchZipFile);
					} else {
						FileUtils.copyFile(prevPluginBatchXml, batchXmlFile);
					}
				} catch (Exception e) {
					logger.error("Unable to update batch xml for batch instance : " + batchInstance.getIdentifier() + "with file : "
							+ prevPluginBatchXml.getAbsolutePath());
					throw new Exception("Unable to update batch xml for batch instance : " + batchInstance.getIdentifier()
							+ "with file : " + prevPluginBatchXml.getAbsolutePath());
				}
			}
		} else {
			logger.error("Could not find restart option for batch instance : " + batchInstance.getIdentifier() + "restart option "
					+ moduleName);
			throw new Exception("Could not find restart option for batch instance : " + batchInstance.getIdentifier()
					+ "restart option " + moduleName);
		}
	}

	/* (non-Javadoc)
	 * @see com.ephesoft.dcma.batch.service.ImportBatchService#importBatchClass(com.ephesoft.dcma.batch.schema.ImportBatchClassOptions, boolean, boolean)
	 */
	@Override
	public Map<Boolean, String> importBatchClass(ImportBatchClassOptions optionXML, boolean isDeployed, boolean isFromWebService,
			String userRole) {
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
				boolean isEqual = isImportWorkflowEqualDeployedWorkflow(importBatchClass, optionXML.getName());
				if (isNameExistInDatabase && !isEqual) {
					String errorMessg = "Incorrect user input. Workflow name specified exists with different configuration. Returning from import.";
					resultsMap.put(false, errorMessg);
					logger.error(errorMessg);
					return resultsMap;
				}

				// verify the contents are equal to the deployed workflow
				if (!optionXML.isImportIfConflict() && !isNameExistInDatabase && isDeployed) {
					String errorMessg = "Zip contains a workflow name that is already deployed but no batch class exists corresponding to this workflow name. It may be possible that the zip contains different configuration. Please specify another name for workflow.";
					resultsMap.put(false, errorMessg);
					logger.error(errorMessg);
					return resultsMap;
				}
			}

			if (optionXML.getName() == null || optionXML.getName().isEmpty()) {
				optionXML.setName(importBatchClass.getName());
			}
			if (optionXML.isUseExisting()) {
				overrideExistingBatchClass(resultsMap, optionXML, tempOutputUnZipDir, originalFolder, importBatchClass,userRole);
			} else {
				importNewBatchClass(resultsMap, optionXML, tempOutputUnZipDir, originalFolder, serializableFileStream,
						importBatchClass, userRole);
			}
		} catch (Exception e) {
			String errorMessg = "Error while importing." + e.getMessage();
			resultsMap.put(false, errorMessg);
			logger.error(errorMessg, e);
		}
		return resultsMap;
	}

	/** Method to import a new batch class.
	 * 
	 * @param resultsImport
	 * @param optionXML
	 * @param tempOutputUnZipDir
	 * @param originalFolder
	 * @param serializableFileStream
	 * @param importBatchClass
	 * @throws IOException
	 */
	private void importNewBatchClass(Map<Boolean, String> resultsImport, ImportBatchClassOptions optionXML, String tempOutputUnZipDir,
			File originalFolder, InputStream serializableFileStream, BatchClass importBatchClass, String userRole) throws IOException {
		// create a new batch class from zip file
		String errorMessg = "";
		boolean isSuccess = true;
		resultsImport.put(isSuccess, errorMessg);
		try {
			importBatchClass.setId(0L);
			List<ImportBatchClassOptions.BatchClassDefinition> bcdList = optionXML.getBatchClassDefinition();
			BatchClassDefinition bcd = bcdList.get(0);
			if (!optionXML.isRolesImported()) {
				// do not import the roles
				importBatchClass.getAssignedGroups().clear();
			}

			for (Folder folder : bcd.getFolders().getFolder()) {
				if (batchSchemaService.getImagemagickBaseFolderName().equalsIgnoreCase(folder.getFileName())) {
					if (!folder.isSelected()) {
						// do not import the image samples
						String imagemagickBaseFolder = FileUtils.getFileNameOfTypeFromFolder(tempOutputUnZipDir, batchSchemaService
								.getImagemagickBaseFolderName());
						FileUtils.deleteDirectoryAndContentsRecursive(new File(imagemagickBaseFolder));
					}
				}
				if (batchSchemaService.getSearchSampleName().equalsIgnoreCase(folder.getFileName())) {
					if (!folder.isSelected()) {
						// do not import the lucene samples
						String searchSampleFolder = FileUtils.getFileNameOfTypeFromFolder(tempOutputUnZipDir, batchSchemaService
								.getSearchSampleName());
						FileUtils.deleteDirectoryAndContentsRecursive(new File(searchSampleFolder));
					}
				}
			}
			setCurrentUserRoleToBatchClass(importBatchClass,userRole);
			if (!optionXML.isUseSource()) {
				importBatchClass.setDescription(optionXML.getDescription());
				importBatchClass.setName(optionXML.getName());
				importBatchClass.setPriority(optionXML.getPriority());
			}
			new File(optionXML.getUncFolder()).mkdirs();
			importBatchClass.setUncFolder(optionXML.getUncFolder());
			try {
				BatchClass dbBatchClass = batchClassService.getLoadedBatchClassByNameIncludingDeleted(optionXML.getName());
				if (dbBatchClass != null) {
					batchClassService.evict(dbBatchClass);
				}
				updateSerializableBatchClass(dbBatchClass, importBatchClass);
				batchClassService.evict(importBatchClass);
				importBatchClass = batchClassService.createBatchClass(importBatchClass);
			} catch (Exception exception) {
				errorMessg = "Unable to create/override batch Class." + exception.getMessage();
				logger.error(errorMessg, exception);
				isSuccess = false;
				resultsImport.put(isSuccess, errorMessg);
				new File(optionXML.getUncFolder()).delete();
			}

			File copiedFolder = new File(batchSchemaService.getBaseSampleFDLock() + File.separator + importBatchClass.getIdentifier());
			FileUtils.copyDirectoryWithContents(originalFolder, copiedFolder);

		} catch (FileNotFoundException e) {
			errorMessg = "Serializable file not found." + e.getMessage();
			logger.error(errorMessg, e);
			isSuccess = false;
			resultsImport.put(isSuccess, errorMessg);
			new File(optionXML.getUncFolder()).delete();
		} catch (IOException e) {
			errorMessg = "Unable to copy the learning folders." + e.getMessage();
			logger.error(errorMessg, e);
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

	/**
	 * This API sets first role of current user to this import batch class.
	 * 
	 * @param importBatchClass
	 * @param userRoles
	 */
	private void setCurrentUserRoleToBatchClass(BatchClass importBatchClass, String userRole) {
		if(userRole != null){
		List<BatchClassGroups> batchClassGroupsList = importBatchClass.getAssignedGroups();
		BatchClassGroups batchClassGroup = new BatchClassGroups();
		batchClassGroup.setGroupName(userRole);
		batchClassGroup.setId(0);
		batchClassGroup.setBatchClass(null);
		batchClassGroupsList.add(batchClassGroup);
		importBatchClass.setAssignedGroups(batchClassGroupsList);
		}
	}

	/**
	 * Method to override the existing batch class
	 * 
	 * @param resultsImport
	 * @param optionXML
	 * @param tempOutputUnZipDir
	 * @param originalFolder
	 * @param importBatchClass
	 * @throws Exception
	 */
	private void overrideExistingBatchClass(Map<Boolean, String> resultsImport, ImportBatchClassOptions optionXML,
			final String tempOutputUnZipDir, File originalFolder, BatchClass importBatchClass, String userRole) throws Exception {

		// overriding a batch class
		BatchClass existingBatchClass = batchClassService.getLoadedBatchClassByUNC(optionXML.getUncFolder());
		batchClassService.evict(existingBatchClass);
		List<String> scriptsList = new ArrayList<String>();
		if (existingBatchClass != null) {
			// check the workflow name with UNC folder
			/*
			 * if (!existingBatchClass.getName().equalsIgnoreCase(importBatchClass.getName())) { logger.error(
			 * "Returning from import batch service with error. Mismathing workflow type from zip and from UNC folder specified. Zip workflow name:"
			 * + importBatchClass.getName() + ". UNC workflow name:" + existingBatchClass.getName()); isSuccess = false; return
			 * isSuccess; }
			 */

			String existingUncFolder = existingBatchClass.getUncFolder();
			setCurrentUserRoleToBatchClass(importBatchClass,userRole);
			// update the configurations of imported batch class from DB / Zip
			if (!optionXML.isRolesImported()) {
				// import the roles from database
				updateRoles(importBatchClass, existingBatchClass, Boolean.TRUE);
			}
			if (!optionXML.isEmailAccounts()) {
				updateEmailConfigurations(importBatchClass, existingBatchClass, Boolean.TRUE);
			}
			List<ImportBatchClassOptions.BatchClassDefinition> bcdList = optionXML.getBatchClassDefinition();
			BatchClassDefinition bcd = bcdList.get(0);
			if (bcd.getScripts() != null && !bcd.getScripts().getScript().isEmpty()) {
				scriptsList = updateScriptsFiles(tempOutputUnZipDir, bcd.getScripts().getScript(), existingBatchClass.getIdentifier());
			}
			if(bcd.getFolders() != null && !bcd.getFolders().getFolder().isEmpty()) {
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
			if(bcd.getBatchClassModules() != null && !bcd.getBatchClassModules().getBatchClassModule().isEmpty()) {
				for (com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.BatchClassModules.BatchClassModule bcm : bcd
						.getBatchClassModules().getBatchClassModule()) {
					updatePlgConfig(resultsImport, bcm, importBatchClass, existingBatchClass);
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
				importBatchClass.setId(0L);
				importBatchClass.setUncFolder("dummyUncFolder" + System.currentTimeMillis());

				if (!optionXML.isUseSource()) {
					importBatchClass.setDescription(optionXML.getDescription());
					importBatchClass.setName(optionXML.getName());
					importBatchClass.setPriority(optionXML.getPriority());
				}
			}

			updateSerializableBatchClass(existingBatchClass, importBatchClass);
			importBatchClass = batchClassService.createBatchClassWithoutWatch(importBatchClass);
			batchClassService.evict(importBatchClass);
			importBatchClass = batchClassService.getLoadedBatchClassByIdentifier(importBatchClass.getIdentifier());

			try {
				String dummyUncFolder = existingUncFolder + "-" + existingBatchClass.getIdentifier() + "-deleted";
				existingBatchClass = batchClassService.getBatchClassByIdentifier(existingBatchClass.getIdentifier());
				existingBatchClass.setUncFolder(dummyUncFolder);
				deleteEmailConfigForBatchClass(existingBatchClass);
				existingBatchClass = batchClassService.merge(existingBatchClass);
				try {
					importBatchClass.setUncFolder(existingUncFolder);
					importBatchClass = batchClassService.createBatchClass(importBatchClass);
					try {
						FileUtils.deleteSelectedFilesFromDirectory(tempOutputUnZipDir + File.separator
								+ batchSchemaService.getScriptFolderName(), scriptsList);
						File copiedFolder = new File(batchSchemaService.getBaseSampleFDLock() + File.separator
								+ importBatchClass.getIdentifier());
						FileUtils.copyDirectoryWithContents(originalFolder, copiedFolder);

						List<BatchInstance> newBatchInstances = batchInstanceService.getBatchInstByBatchClass(existingBatchClass);
						for (BatchInstance batchInstance : newBatchInstances) {
							if (batchInstance.getStatus().equals(BatchInstanceStatus.NEW)) {
								batchInstance.setBatchClass(importBatchClass);
								batchInstance.setPriority(importBatchClass.getPriority());
								batchInstanceService.updateBatchInstance(batchInstance);
							}
						}
					} catch (Exception e) {
						errorMessg = "Error while overriding, reverting the changes:" + e.getMessage();
						logger.info(errorMessg, e);
						isSuccess = false;
						resultsImport.put(isSuccess, errorMessg);
						importBatchClass.setDeleted(true);
						importBatchClass = batchClassService.merge(importBatchClass, true);

						existingBatchClass.setUncFolder(existingUncFolder);
						existingBatchClass = batchClassService.merge(existingBatchClass);
					}
					existingBatchClass.setDeleted(true);
					existingBatchClass = batchClassService.merge(existingBatchClass, true);
					new File(dummyUncFolder).mkdirs();

				} catch (Exception e) {
					errorMessg = "Error while overriding, reverting the changes:" + e.getMessage();
					isSuccess = false;
					resultsImport.put(isSuccess, errorMessg);
					logger.info(errorMessg, e);
					existingBatchClass.setUncFolder(existingUncFolder);
					existingBatchClass = batchClassService.merge(existingBatchClass);
				}

			} catch (Exception e) {
				errorMessg = "Error while overriding, reverting the changes:" + e.getMessage();
				isSuccess = false;
				resultsImport.put(isSuccess, errorMessg);
				logger.info(errorMessg, e);
				importBatchClass.setDeleted(true);
				importBatchClass = batchClassService.merge(importBatchClass, true);
			}

		}
	}

	/** Method to delete the email configurations from the specified batch class.
	 * @param batchClass
	 */
	private void deleteEmailConfigForBatchClass(final BatchClass batchClass) {
		List<BatchClassEmailConfiguration> emailConfigList = bcEmailConfigService.getEmailConfigByBatchClassIdentifier(batchClass
				.getIdentifier());
		for (BatchClassEmailConfiguration batchClassEmailConfiguration : emailConfigList) {
			bcEmailConfigService.removeEmailConfiguration(batchClassEmailConfiguration);
		}
	}

	/**
	 * Utility Method to synchronize the serializable file Batch class with the current System Batch Class. In case the exported batch
	 * class contains any additional plugins,modules,plugin Config or plugin config sample values, they are imported as well.
	 * 
	 * @param dbBatchClass
	 * @param moduleConfigService
	 * @param moduleService
	 * @param pluginService
	 * @param pluginConfigService
	 * @param batchClassService
	 * @param batchSchemaService
	 * @param serializedBatchClass
	 */
	public void updateSerializableBatchClass(BatchClass dbBatchClass, BatchClass serializedBatchClass) {
		// fix for "Not Mapped yet." issue while importing old batch classes.
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
			List<BatchClassModuleConfig> batchClassModConfigs = batchClassModule.getBatchClassModuleConfig();
			boolean isClearBatchClassModuleConfig = false;
			if (batchClassModConfigs != null) {
				for (BatchClassModuleConfig batchClassModConfig : batchClassModConfigs) {
					if (batchClassModConfig != null) {
						if (batchClassModConfig.getModuleConfig() != null) {
							ModuleConfig currentModuleConfig = moduleConfigService.getModuleConfigByKeyAndMandatory(
									batchClassModConfig.getModuleConfig().getChildKey(), batchClassModConfig.getModuleConfig()
											.isMandatory());
							if (currentModuleConfig == null) {
								continue;
							} else {
								batchClassModConfig.setModuleConfig(currentModuleConfig);
							}
						} else if (!isClearBatchClassModuleConfig) {
							isClearBatchClassModuleConfig = true;
						}
					}
				}

				// backward compatibility support for 2.2 release exported batch classes
				if (isClearBatchClassModuleConfig) {
					batchClassModule.getBatchClassModuleConfig().clear();
					batchClassModule.setBatchClassModuleConfig(new ArrayList<BatchClassModuleConfig>());
				}
			}

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
		if (dbBatchClass != null) {
			updateBatchClassModules(pluginConfigService, batchSchemaService, dbBatchClass, batchClassModules);
		}
	}

	/**
	 * This method synchronizes the zip batch class modules with the system batch class modules.
	 * 
	 * @param pluginConfigService
	 * @param batchSchemaService
	 * @param dbBatchClass
	 * @param zipBatchClassModules
	 */
	private void updateBatchClassModules(PluginConfigService pluginConfigService, BatchSchemaService batchSchemaService,
			BatchClass dbBatchClass, List<BatchClassModule> zipBatchClassModules) {
		List<BatchClassModule> dbBatchClassModules = dbBatchClass.getBatchClassModules();
		List<BatchClassModule> validBatchClassModules = new ArrayList<BatchClassModule>();
		if (dbBatchClassModules != null) {
			BatchClassModule bcModule = null;
			for (BatchClassModule dbBatchClassModule : dbBatchClassModules) {
				boolean isPresent = false;
				for (BatchClassModule zipBatchClassModule : zipBatchClassModules) {
					if (zipBatchClassModule.getWorkflowName().equalsIgnoreCase(dbBatchClassModule.getWorkflowName())) {
						isPresent = true;
						validBatchClassModules.add(zipBatchClassModule);
						bcModule = zipBatchClassModule;
						break;
					}
				}
				if (isPresent) {
					BatchClassModule detachedBatchClassModule = batchSchemaService.getDetachedBatchClassModuleByName(dbBatchClass
							.getIdentifier(), bcModule.getWorkflowName());
					if (detachedBatchClassModule != null) {
						updateBatchClassPlugins(pluginConfigService, detachedBatchClassModule, bcModule.getBatchClassPlugins());
					} else {
						logger.info("No module found with workflowName:" + bcModule.getWorkflowName() + " in batch class id:"
								+ dbBatchClass.getIdentifier() + ". Skipping the updates for this module.");
					}
				} else {
					BatchClassModule detachedBatchClassModule = batchSchemaService.getDetachedBatchClassModuleByName(dbBatchClass
							.getIdentifier(), dbBatchClassModule.getWorkflowName());
					if (detachedBatchClassModule != null) {
						validBatchClassModules.add(detachedBatchClassModule);
					} else {
						logger.info("No module found with workflowName:" + dbBatchClassModule.getWorkflowName()
								+ " in batch class id:" + dbBatchClass.getIdentifier() + ". Skipping the updates for this module.");
					}
				}
			}
		}
		if (validBatchClassModules != null) {
			zipBatchClassModules.clear();
			zipBatchClassModules.addAll(validBatchClassModules);
		}
	}

	/**
	 * This method synchronizes the zip batch class plugins of specific batch class module with batch class plugins of corresponding
	 * batch class module of system batch class.
	 * 
	 * @param pluginConfigService
	 * @param dbBatchClassModule
	 * @param zipBatchClassPlugins
	 */
	private void updateBatchClassPlugins(PluginConfigService pluginConfigService, BatchClassModule dbBatchClassModule,
			List<BatchClassPlugin> zipBatchClassPlugins) {
		List<BatchClassPlugin> dbBatchClassPlugins = dbBatchClassModule.getBatchClassPlugins();
		List<BatchClassPlugin> validBatchClassPlugins = new ArrayList<BatchClassPlugin>();
		if (dbBatchClassPlugins != null) {
			BatchClassPlugin bcPlugin = null;
			for (BatchClassPlugin dbBatchClassPlugin : dbBatchClassPlugins) {
				boolean isPresent = false;
				for (BatchClassPlugin zipBatchClassPlugin : zipBatchClassPlugins) {
					if (zipBatchClassPlugin.getPlugin().getPluginName().equalsIgnoreCase(
							dbBatchClassPlugin.getPlugin().getPluginName())) {
						isPresent = true;
						validBatchClassPlugins.add(zipBatchClassPlugin);
						bcPlugin = zipBatchClassPlugin;
						break;
					}
				}
				if (isPresent) {
					updateBatchClassPluginConfigs(pluginConfigService, dbBatchClassPlugin, bcPlugin.getBatchClassPluginConfigs());
				} else {
					validBatchClassPlugins.add(dbBatchClassPlugin);
				}
			}
		}
		if (validBatchClassPlugins != null) {
			zipBatchClassPlugins.clear();
			zipBatchClassPlugins.addAll(validBatchClassPlugins);
		}
	}

	/**
	 * This method synchronizes the zip batch class plugin configs of specific batch class plugin with the corresponding batch class
	 * plugin config in system batch class.
	 * 
	 * @param pluginConfigService
	 * @param dbBatchClassPlugin
	 * @param zipBatchClassPluginConfigs
	 */
	private void updateBatchClassPluginConfigs(PluginConfigService pluginConfigService, BatchClassPlugin dbBatchClassPlugin,
			List<BatchClassPluginConfig> zipBatchClassPluginConfigs) {
		if (dbBatchClassPlugin != null) {
			List<BatchClassPluginConfig> dbBatchClassPluginConfigs = dbBatchClassPlugin.getBatchClassPluginConfigs();
			List<BatchClassPluginConfig> validBCPluginConfigs = new ArrayList<BatchClassPluginConfig>();
			BatchClassPluginConfig bcPluginConfig = null;
			if (dbBatchClassPluginConfigs != null) {
				for (BatchClassPluginConfig dbBatchClassPluginConfig : dbBatchClassPluginConfigs) {
					boolean isPresent = false;
					for (BatchClassPluginConfig zipBatchClassPluginConfig : zipBatchClassPluginConfigs) {
						if (zipBatchClassPluginConfig.getPluginConfig().getName().equalsIgnoreCase(
								dbBatchClassPluginConfig.getPluginConfig().getName())) {
							isPresent = true;
							bcPluginConfig = zipBatchClassPluginConfig;
							validBCPluginConfigs.add(zipBatchClassPluginConfig);
							break;
						}
					}
					if (isPresent) {
						// update batch class plugin config value.
						updateBatchClassPluginConfigValue(pluginConfigService, bcPluginConfig, dbBatchClassPluginConfig);
					} else {
						// add new batch class plugin config to imported batch class.
						validBCPluginConfigs.add(dbBatchClassPluginConfig);
					}
				}
			}
			if (validBCPluginConfigs != null) {
				zipBatchClassPluginConfigs.clear();
				zipBatchClassPluginConfigs.addAll(validBCPluginConfigs);
			}
		}
	}

	/**
	 * This method sychronizes the zip batch class plugin config value with the value in plugin config sample value.
	 * 
	 * @param pluginConfigService
	 * @param zipBatchClassPluginConfig
	 * @param dbBatchClassPluginConfig
	 */
	private void updateBatchClassPluginConfigValue(PluginConfigService pluginConfigService,
			BatchClassPluginConfig zipBatchClassPluginConfig, BatchClassPluginConfig dbBatchClassPluginConfig) {
		PluginConfig pluginConfig = dbBatchClassPluginConfig.getPluginConfig();
		if (pluginConfig != null) {
			pluginConfig = pluginConfigService.getPluginConfigByName(pluginConfig.getName());
			// get sample values for that plugin config.
			List<String> pcSampleValue = pluginConfig.getSampleValue();
			// get batch class plugin config value in zip.
			String bcpcValue = zipBatchClassPluginConfig.getValue();
			// Check if batch class plugin config value is valid or not.
			if (pcSampleValue != null && !pcSampleValue.isEmpty() && !isBcpcValueExistsInDB(pcSampleValue, bcpcValue)) {
				// update imported batch class plugin value with plugin config value for system batch class.
				zipBatchClassPluginConfig.setValue(dbBatchClassPluginConfig.getValue());
			}
		}
	}

	/**
	 * This method returns true or false depending on whether zip batch class plugin config value exists in its sample values in
	 * database.
	 * 
	 * @param dbBatchClassPluginConfig
	 * @param zipBcpcValue
	 * @return
	 */
	private boolean isBcpcValueExistsInDB(List<String> dbBatchClassPluginConfig, String zipBcpcValue) {
		boolean isPresent = false;
		if (zipBcpcValue != null) {
			String[] valueArray = zipBcpcValue.split(SEMI_COLON);
			for (int index = 0; index < valueArray.length; index++) {
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
				} else if (index == valueArray.length - 1) {
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

	/** Method to update the plugin configurations of the batch class as per the options specified.
	 * @param resultsImport
	 * @param bcm
	 * @param importBatchClass
	 * @param existingBatchClass
	 */
	public void updatePlgConfig(Map<Boolean, String> resultsImport,
			com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.BatchClassModules.BatchClassModule bcm,
			BatchClass importBatchClass, BatchClass existingBatchClass) {
		String errorMessg = "";
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
				logger.error(errorMessg);
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
				logger.error(errorMessg);
				results = false;
				resultsImport.put(results, errorMessg);
			}
		}
	}

	/** Method to update the roles of the specified batch class.
	 * @param importBatchClass
	 * @param existingBatchClass
	 * @param isFromDB
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
			batchClassGroup.setId(0);
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
	 * @param importBatchClass
	 * @param existingBatchClass
	 * @param isFromDB
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
			batchClassEmailConfiguration.setId(0);
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
	 * @param resultsImport
	 * @param existingFolderName
	 * @param zipFolderName
	 * @param zipDir
	 */
	public void overrideFromDB(Map<Boolean, String> resultsImport, String zipFolderName, String existingFolderName, String zipDir) {
		String errorMessg = "";
		boolean isSuccess = true;
		// import the samples from database
		if (zipFolderName.isEmpty()) {
			// create a folder in the directory of zip file
			zipFolderName = new File(zipFolderName).getAbsolutePath();
		} else {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(zipFolderName), false);
		}
		if (zipFolderName != null && !zipFolderName.isEmpty()) {
			if (existingFolderName != null && !existingFolderName.isEmpty()) {
				boolean copiedSuccessfully = true;
				try {
					FileUtils.copyDirectoryWithContents(existingFolderName, zipFolderName);
				} catch (IOException ioe) {
					errorMessg = "Unable to override folder" + existingFolderName + " for batch class.";
					logger.info(errorMessg + ioe, ioe);
					copiedSuccessfully = false;
				}
				if (!copiedSuccessfully) {
					isSuccess = false;
					resultsImport.put(isSuccess, errorMessg);
				}
			}
		}
	}

	/**
	 * @param tempOutputUnZipDir
	 * @param allScriptsList
	 * @param existingBCID
	 * @return
	 * @throws Exception
	 */
	public List<String> updateScriptsFiles(String tempOutputUnZipDir,
			List<ImportBatchClassOptions.BatchClassDefinition.Scripts.Script> allScriptsList, String existingBCID) throws Exception {
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

	/* (non-Javadoc)
	 * @see com.ephesoft.dcma.batch.service.ImportBatchService#validateInputXML(com.ephesoft.dcma.batch.schema.ImportBatchClassOptions)
	 */
	@Override
	public Map<Boolean, String> validateInputXML(ImportBatchClassOptions optionXML) {
		boolean isValid = true;
		String errorMessg = "";
		if (!optionXML.isUseSource()) {
			// check for the description and priority from the XML file
			if (optionXML.getName() == null || optionXML.getDescription() == null || optionXML.getName().isEmpty() || optionXML.getDescription().isEmpty() || optionXML.getPriority() <= 0
					|| optionXML.getPriority() > 100) {
				errorMessg = "Either name, description is empty or priority is not between 1 and 100 inclusive.";
				isValid = false;
			}
		}
		if (isValid) {
			String name = optionXML.getName();
			if (name.indexOf(" ") > -1 || name.indexOf("-") > -1) {
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

	/* (non-Javadoc)
	 * @see com.ephesoft.dcma.batch.service.ImportBatchService#isImportWorkflowEqualDeployedWorkflow(com.ephesoft.dcma.da.domain.BatchClass, java.lang.String)
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
				bcmLoop: for (int index = 0; index < dbBCMs.size(); index++) {
					BatchClassModule zipBCM = zipBCMs.get(index);
					BatchClassModule dbBCM = dbBCMs.get(index);
					if (!dbBCM.getWorkflowName().equals(zipBCM.getWorkflowName())) {
						isEqual = false;
						break;
					}

					List<BatchClassPlugin> zipBCPs = zipBCM.getBatchClassPlugins();
					List<BatchClassPlugin> dbBCPs = dbBCM.getBatchClassPlugins();
					if (dbBCPs.size() == zipBCPs.size()) {
						for (int indexPlg = 0; indexPlg < dbBCPs.size(); indexPlg++) {
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
}
