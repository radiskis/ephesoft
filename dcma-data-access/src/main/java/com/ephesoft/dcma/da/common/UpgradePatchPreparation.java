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

package com.ephesoft.dcma.da.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.da.constant.DataAccessConstant;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassDynamicPluginConfig;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchClassModuleConfig;
import com.ephesoft.dcma.da.domain.BatchClassPlugin;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;
import com.ephesoft.dcma.da.domain.BatchClassScannerConfiguration;
import com.ephesoft.dcma.da.domain.Dependency;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.FieldType;
import com.ephesoft.dcma.da.domain.FunctionKey;
import com.ephesoft.dcma.da.domain.KVExtraction;
import com.ephesoft.dcma.da.domain.KVPageProcess;
import com.ephesoft.dcma.da.domain.Module;
import com.ephesoft.dcma.da.domain.PageType;
import com.ephesoft.dcma.da.domain.Plugin;
import com.ephesoft.dcma.da.domain.RegexValidation;
import com.ephesoft.dcma.da.domain.ScannerMasterConfiguration;
import com.ephesoft.dcma.da.domain.TableColumnsInfo;
import com.ephesoft.dcma.da.domain.TableInfo;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.ModuleService;
import com.ephesoft.dcma.da.service.PluginService;
import com.ephesoft.dcma.util.ApplicationContextUtil;

/**
 * This class upgrade the patch preparation.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.da.domain.BatchClassDynamicPluginConfig
 */
public class UpgradePatchPreparation {

	/**
	 * pluginService PluginService.
	 */ 
	private static PluginService pluginService;

	/**
	 * moduleService ModuleService.
	 */
	private static ModuleService moduleService;
	
	/**
	 * ERROR_OCCURRED_WHILE_CREATING_THE_SERIALIZABLE_FILE, constant String.
	 */
	private static final String ERROR_OCCURRED_WHILE_CREATING_THE_SERIALIZABLE_FILE = "Error occurred while creating the serializable file.";

	/**
	 * LOG to print the logging information.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(UpgradePatchPreparation.class);

	/**
	 * SERIALIZATION_EXT String.
	 */
	private static final String SERIALIZATION_EXT = FileType.SER.getExtensionWithDot();

	/**
	 * upgradePatchFolderPath String.
	 */
	private static String upgradePatchFolderPath = null;
	
	/**
	 * pluginNameVsBatchPluginConfigList, HashMap.
	 */
	private static HashMap<String, ArrayList<BatchClassPluginConfig>> pluginNameVsBatchPluginConfigList = new HashMap<String, ArrayList<BatchClassPluginConfig>>();

	/**
	 * batchClassNameVsModulesMap, HashMap.
	 */
	private static HashMap<String, ArrayList<BatchClassModule>> batchClassNameVsModulesMap = new HashMap<String, ArrayList<BatchClassModule>>();

	/**
	 * batchClassNameVsPluginsMap, HashMap.
	 */
	private static HashMap<String, ArrayList<BatchClassPlugin>> batchClassNameVsPluginsMap = new HashMap<String, ArrayList<BatchClassPlugin>>();

	/**
	 * moduleNameVsBatchClassModuleConfigMap, HashMap.
	 */
	private static HashMap<String, ArrayList<BatchClassModuleConfig>> moduleNameVsBatchClassModuleConfigMap = new HashMap<String, ArrayList<BatchClassModuleConfig>>();

	/**
	 * batchClassNameVsBatchClassMap, HashMap.
	 */
	private static HashMap<String, BatchClass> batchClassNameVsBatchClassMap = new LinkedHashMap<String, BatchClass>();

	/**
	 * batchClassScannerConfigList, List.
	 */
	private static List<BatchClassScannerConfiguration> batchClassScannerConfigList = new ArrayList<BatchClassScannerConfiguration>();

	/**
	 * pluginNameVsDependencyMap, HashMap.
	 */
	private static HashMap<String, ArrayList<Dependency>> pluginNameVsDependencyMap = new HashMap<String, ArrayList<Dependency>>();

	/**
	 * This method loads the required properties.
	 * 
	 * @param propertyName {@link String}
	 * @return Properties
	 * @throws IOException may occur while reading the file.
	 */
	private static Properties loadProperties(final String propertyName) throws IOException {
		final String filePath = DataAccessConstant.META_INF_PATH + File.separator + propertyName + DataAccessConstant.PROPERTIES;
		final Properties properties = new Properties();
		InputStream propertyInStream = null;
		try {
			propertyInStream = new ClassPathResource(filePath).getInputStream();

			properties.load(propertyInStream);
		} finally {
			if (propertyInStream != null) {
				propertyInStream.close();
			}
		}
		return properties;
	}

	/**
	 * This method creates patch for DB.
	 * 
	 * @param service {@link BatchClassService}
	 */
	public static void createDBPatch(final BatchClassService service) {
		String pluginInfo = null;
		String pluginConfigInfo = null;
		String moduleInfo = null;
		String batchClassInfo = null;
		String moduleConfigInfo = null;
		String scannerConfigInfo = null;
		try {
			final Properties props = loadProperties(DataAccessConstant.PROPERTY_FILE);

			upgradePatchFolderPath = props.getProperty("upgradePatch.folder");
			if (upgradePatchFolderPath == null || upgradePatchFolderPath.isEmpty()) {
				LOG.error("Patch folder not specified. Unable to complete patch creation.");
				return;
			}

			scannerConfigInfo = props.getProperty("upgradePatch.scanner_property");
			if (scannerConfigInfo != null && !scannerConfigInfo.trim().isEmpty()) {
				createPatchForScannerConfig(service, scannerConfigInfo);
			}

			moduleInfo = props.getProperty("upgradePatch.module");
			if (moduleInfo != null && !moduleInfo.trim().isEmpty()) {
				createPatchForModule(service, moduleInfo);
			}

			pluginInfo = props.getProperty("upgradePatch.plugin");
			if (pluginInfo != null && !pluginInfo.trim().isEmpty()) {
				createPatchForPlugin(service, pluginInfo);
			}

			pluginConfigInfo = props.getProperty("upgradePatch.plugin_property");
			if (pluginConfigInfo != null && !pluginConfigInfo.isEmpty()) {
				createPatchForPluginConfig(service, pluginConfigInfo);
			}

			moduleConfigInfo = props.getProperty("upgradePatch.module_property");
			if (moduleConfigInfo != null && !moduleConfigInfo.isEmpty()) {
				createPatchForBatchClassModuleConfigs(service);
			}
			batchClassInfo = props.getProperty("upgradePatch.batch_class");
			if (batchClassInfo != null && !batchClassInfo.isEmpty()) {
				createPatchForBatchClass( service,batchClassInfo);
			}
			createPatchForDependencies();
		} catch (IOException e) {
			LOG.error("Unable to load properties file.", e);
		}
	}

	/**
	 * This method creates patch for plugin.
	 * 
	 * @param service {@link BatchClassService}
	 * @param pluginInfo {@link String}
	 */
	private static void createPatchForPlugin(final BatchClassService service, final String pluginInfo) {
		final StringTokenizer pluginTokens = new StringTokenizer(pluginInfo, DataAccessConstant.SEMI_COLON);
		while (pluginTokens.hasMoreTokens()) {
			String pluginToken = pluginTokens.nextToken();
			StringTokenizer pluginConfigTokens = new StringTokenizer(pluginToken, DataAccessConstant.COMMA);
			String batchClassIdentifier = null;
			String moduleId = null;
			String pluginId = null;
			try {
				batchClassIdentifier = pluginConfigTokens.nextToken();
				moduleId = pluginConfigTokens.nextToken();
				pluginId = pluginConfigTokens.nextToken();
				BatchClassPlugin createdPlugin = createPatch(batchClassIdentifier, moduleId, pluginId, service);
				if (createdPlugin != null) {
					BatchClass batchClass = service.getBatchClassByIdentifier(batchClassIdentifier);
					Module module = moduleService.getModulePropertiesForModuleId(Long.valueOf(moduleId));
					String key = batchClass.getName() + DataAccessConstant.COMMA + module.getName();
					ArrayList<BatchClassPlugin> pluginsList = batchClassNameVsPluginsMap.get(key);
					if (pluginsList == null) {
						pluginsList = new ArrayList<BatchClassPlugin>();
						batchClassNameVsPluginsMap.put(key, pluginsList);
					}
					pluginsList.add(createdPlugin);
				}

			} catch (NoSuchElementException e) {
				LOG.info("Incomplete data specifiedin properties file.", e);
			}
		}

		try {
			File serializedExportFile = new File(upgradePatchFolderPath + File.separator + "PluginUpdate" + SERIALIZATION_EXT);
			SerializationUtils.serialize(batchClassNameVsPluginsMap, new FileOutputStream(serializedExportFile));
		} catch (FileNotFoundException e) {
			// Unable to read serializable file
			LOG.error(ERROR_OCCURRED_WHILE_CREATING_THE_SERIALIZABLE_FILE + e.getMessage(), e);
		}
	}

	/**
	 * This method creates patch for plugin config. 
	 * 
	 * @param service {@link BatchClassService}
	 * @param pluginConfigInfo {@link String}
	 */
	private static void createPatchForPluginConfig(BatchClassService service, final String pluginConfigInfo) {
		StringTokenizer pluginTokens = new StringTokenizer(pluginConfigInfo, DataAccessConstant.SEMI_COLON);
		while (pluginTokens.hasMoreTokens()) {
			String pluginToken = pluginTokens.nextToken();
			StringTokenizer pluginConfigTokens = new StringTokenizer(pluginToken, DataAccessConstant.COMMA);
			String pluginId = null;
			String pluginConfigId = null;
			try {
				pluginId = pluginConfigTokens.nextToken();
				pluginConfigId = pluginConfigTokens.nextToken();
				createPatch(pluginId, pluginConfigId, service);

			} catch (NoSuchElementException e) {
				LOG.error("Incomplete data specified in properties file.", e);
			}
		}

		try {
			File serializedExportFile = new File(upgradePatchFolderPath + File.separator + "PluginConfigUpdate" + SERIALIZATION_EXT);
			SerializationUtils.serialize(pluginNameVsBatchPluginConfigList, new FileOutputStream(serializedExportFile));
		} catch (FileNotFoundException e) {
			// Unable to read serializable file
			LOG.error(ERROR_OCCURRED_WHILE_CREATING_THE_SERIALIZABLE_FILE + e.getMessage(), e);
		}
	}

	/**
	 * This method creates patch for module.
	 * 
	 * @param service {@link BatchClassService}
	 * @param moduleInfo {@link String}
	 */
	private static void createPatchForModule(BatchClassService service, String moduleInfo) {
		StringTokenizer moduleTokens = new StringTokenizer(moduleInfo, DataAccessConstant.SEMI_COLON);
		while (moduleTokens.hasMoreTokens()) {
			String moduleToken = moduleTokens.nextToken();
			StringTokenizer pluginConfigTokens = new StringTokenizer(moduleToken, DataAccessConstant.COMMA);
			String batchClassName = null;
			String moduleId = null;
			try {
				batchClassName = pluginConfigTokens.nextToken();
				moduleId = pluginConfigTokens.nextToken();
				BatchClassModule createdModule = createPatchForModule(batchClassName, moduleId, service);
				if (createdModule != null) {
					BatchClass batchClass = service.getBatchClassByIdentifier(batchClassName);
					ArrayList<BatchClassModule> bcmList = batchClassNameVsModulesMap.get(batchClass.getName());
					if (bcmList == null) {
						bcmList = new ArrayList<BatchClassModule>();
						batchClassNameVsModulesMap.put(batchClass.getName(), bcmList);
					}
					bcmList.add(createdModule);
				}

			} catch (NoSuchElementException e) {
				LOG.error("Incomplete data specified in properties file.", e);
			}
		}

		try {
			File serializedExportFile = new File(upgradePatchFolderPath + File.separator + "ModuleUpdate" + SERIALIZATION_EXT);
			SerializationUtils.serialize(batchClassNameVsModulesMap, new FileOutputStream(serializedExportFile));
		} catch (FileNotFoundException e) {
			// Unable to read serializable file
			LOG.error(ERROR_OCCURRED_WHILE_CREATING_THE_SERIALIZABLE_FILE + e.getMessage(), e);
		}

	}

	/**
	 * This method creates patch for scanner config.
	 * 
	 * @param service {@link BatchClassService}
	 * @param scannerConfigInfo {@link String}
	 */
	private static void createPatchForScannerConfig(BatchClassService service, String scannerConfigInfo) {
		String batchClassId = scannerConfigInfo.trim();
		BatchClass batchClass = service.getLoadedBatchClassByIdentifier(batchClassId);
		List<BatchClassScannerConfiguration> batchClassScannerConfigs = batchClass.getBatchClassScannerConfiguration();
		service.evict(batchClass);
		for (BatchClassScannerConfiguration scannerConfig : batchClassScannerConfigs) {
			if (scannerConfig.getParent() == null) {
				scannerConfig.setBatchClass(null);
				scannerConfig.setId(0);
				ScannerMasterConfiguration masterConfig=scannerConfig.getScannerMasterConfig();
				masterConfig.setId(0);
				scannerConfig.setScannerMasterConfig(masterConfig);
				for (BatchClassScannerConfiguration childScannerConfig : scannerConfig.getChildren()) {
					childScannerConfig.setParent(null);
					childScannerConfig.setBatchClass(null);
					childScannerConfig.setId(0);
					ScannerMasterConfiguration childMasterConfig=childScannerConfig.getScannerMasterConfig();
					childMasterConfig.setId(0);
					childScannerConfig.setScannerMasterConfig(childMasterConfig);
					LOG.info("Getting the child configs of parent scanner configs...");
				}
				LOG.info("Adding the parent scanner configs to the list...");
				batchClassScannerConfigList.add(scannerConfig);
			}
		}
		try {
			File serializedExportFile = new File(upgradePatchFolderPath + File.separator + "ScannerConfigUpdate" + SERIALIZATION_EXT);
			SerializationUtils.serialize((Serializable) batchClassScannerConfigList, new FileOutputStream(serializedExportFile));
		} catch (FileNotFoundException e) {
			// Unable to create serializable file
			LOG.error(ERROR_OCCURRED_WHILE_CREATING_THE_SERIALIZABLE_FILE + e.getMessage(), e);
		}

	}

	/**
	 * This method creates patch for batch class.
	 * 
	 * @param service {@link BatchClassService}
	 * @param batchClassInfo {@link String}
	 */
	private static void createPatchForBatchClass(BatchClassService service, String batchClassInfo) {
		StringTokenizer batchClassTokens = new StringTokenizer(batchClassInfo, DataAccessConstant.SEMI_COLON);
		while (batchClassTokens.hasMoreTokens()) {
			String batchClassName = batchClassTokens.nextToken();
			try {
				BatchClass createdBatchClass = createPatchForBatchClass(batchClassName, service);
				if (createdBatchClass != null) {
					batchClassNameVsBatchClassMap.put(createdBatchClass.getName(), createdBatchClass);
				}

			} catch (NoSuchElementException e) {
				LOG.error("Incomplete data specified in properties file.", e);
			}
		}

		try {
			File serializedExportFile = new File(upgradePatchFolderPath + File.separator + "BatchClassUpdate" + SERIALIZATION_EXT);
			SerializationUtils.serialize(batchClassNameVsBatchClassMap, new FileOutputStream(serializedExportFile));
		} catch (FileNotFoundException e) {
			// Unable to read serializable file
			LOG.error(ERROR_OCCURRED_WHILE_CREATING_THE_SERIALIZABLE_FILE + e.getMessage(), e);
		}
	}

	/**
	 * This method creates patch for modules.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param moduleName {@link String}
	 * @param batchClassService {@link BatchClassService}
	 * @return {@link BatchClassModule}
	 */
	private static BatchClassModule createPatchForModule(String batchClassIdentifier, String moduleName,
			BatchClassService batchClassService) {

		BatchClassModule createdModule = null;
		try {
			BatchClass batchClass = batchClassService.getLoadedBatchClassByIdentifier(batchClassIdentifier);
			batchClassService.evict(batchClass);
			List<BatchClassModule> bcms = batchClass.getBatchClassModules();
			for (BatchClassModule bcm : bcms) {
				if (bcm.getModule().getName().equalsIgnoreCase(moduleName)) {
					createdModule = bcm;
					break;
				}
			}
			if (createdModule != null) {
				createdModule.setId(0);
				createdModule.setBatchClass(null);
				List<BatchClassPlugin> newBatchClassPluginsList = new ArrayList<BatchClassPlugin>();
				for (BatchClassPlugin bcp : createdModule.getBatchClassPlugins()) {
					preparePluginForSerialization(bcp);
					newBatchClassPluginsList.add(bcp);
				}
				createdModule.setBatchClassPlugins(newBatchClassPluginsList);
			}

		} catch (NumberFormatException e) {
			LOG.error("Module Id should be numeric." + e.getMessage(), e);
		}
		return createdModule;
	}

	/**
	 * This method creates patch for batch class.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param batchClassService {@link BatchClassService}
	 * @return {@link BatchClass}
	 */
	private static BatchClass createPatchForBatchClass(String batchClassIdentifier, BatchClassService batchClassService) {
		BatchClass createdBatchClass = null;
		BatchClassModule createdBatchClassModule = null;
		List<BatchClassModule> batchClassModules = new ArrayList<BatchClassModule>();
		try {
			BatchClass batchClass = batchClassService.getLoadedBatchClassByIdentifier(batchClassIdentifier);
			batchClassService.evict(batchClass);

			// preparing to copy batch class modules..
			List<BatchClassModule> bcms = batchClass.getBatchClassModules();
			for (BatchClassModule bcm : bcms) {
				String moduleName = bcm.getModule().getName();
				if (moduleName != null) {
					createdBatchClassModule = createPatchForModule(batchClassIdentifier, moduleName, batchClassService);
					if (createdBatchClassModule != null) {
						List<BatchClassPlugin> newBatchClassPluginsList = new ArrayList<BatchClassPlugin>();
						for (BatchClassPlugin bcp : createdBatchClassModule.getBatchClassPlugins()) {
							preparePluginForSerialization(bcp);
							newBatchClassPluginsList.add(bcp);
						}
						createdBatchClassModule.setBatchClassPlugins(newBatchClassPluginsList);
						batchClassModules.add(createdBatchClassModule);
					}
				}
			}

			// preparing to copy document types..
			List<DocumentType> documentTypes = batchClass.getDocumentTypes();
			List<DocumentType> newDocumentType = new ArrayList<DocumentType>();
			for (DocumentType documentType : documentTypes) {
				newDocumentType.add(documentType);
				documentType.setId(0);
				documentType.setBatchClass(null);
				documentType.setIdentifier(null);
				preparePageTypeForSerialization(documentType);
				prepareFieldTypeForSerialization(documentType);
				prepareTableInfoForSerialization(documentType);
				prepareFunctionKeyForSerialization(documentType);
			}
			batchClass.setDocumentTypes(newDocumentType);
			createdBatchClass = batchClass;
			if (createdBatchClass != null) {
				prepareBatchClassForSerialization(createdBatchClass, batchClassModules, newDocumentType);
			}
		} catch (NumberFormatException e) {
			LOG.error("Module Id should be numeric." + e.getMessage(), e);
		}
		return createdBatchClass;
	}

	/**
	 * This method is used to create patch.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param moduleId {@link String}
	 * @param pluginId {@link String}
	 * @param batchClassService {@link BatchClassService}
	 * @return {@link BatchClassPlugin}
	 */
	private static BatchClassPlugin createPatch(String batchClassIdentifier, String moduleId, String pluginId,
			BatchClassService batchClassService) {
		BatchClassPlugin createdPlugin = null;
		try {
			int modId = Integer.parseInt(moduleId);
			int plgId = Integer.parseInt(pluginId);
			BatchClass batchClass = batchClassService.getLoadedBatchClassByIdentifier(batchClassIdentifier);
			batchClassService.evict(batchClass);
			List<BatchClassModule> mods = batchClass.getBatchClassModules();
			List<BatchClassPlugin> plugins = null;
			boolean pluginFound = false;
			for (BatchClassModule bcm : mods) {
				if (bcm.getModule().getId() == modId) {
					plugins = bcm.getBatchClassPlugins();
					for (BatchClassPlugin bcp : plugins) {
						if (bcp.getPlugin().getId() == plgId) {
							createdPlugin = bcp;
							pluginFound = true;
							break;
						}
					}
					if (pluginFound) {
						break;
					}
				}
			}

			// preparing the plugin for addition to batch classes.
			if (createdPlugin != null) {
				preparePluginForSerialization(createdPlugin);
			}

		} catch (NumberFormatException e) {
			LOG.error("Module Id and Plugin Id should be numeric." + e.getMessage(), e);
		}
		return createdPlugin;
	}

	/**
	 * This method is to prepare Batch Class for serialization.
	 * 
	 * @param createdBatchClass {@link BatchClass}
	 * @param batchClassModules {@link List<BatchClassModule>}
	 * @param documentTypes {@link List<DocumentType>}
	 */
	private static void prepareBatchClassForSerialization(BatchClass createdBatchClass, List<BatchClassModule> batchClassModules,
			List<DocumentType> documentTypes) {
		createdBatchClass.setId(0);
		createdBatchClass.setIdentifier(null);
		createdBatchClass.setAssignedGroups(null);
		createdBatchClass.setBatchClassModules(batchClassModules);
		createdBatchClass.setDocumentTypes(documentTypes);
		createdBatchClass.setCurrentUser(null);
	}

	/**
	 * This method prepares plugin for serialization.
	 * 
	 * @param createdPlugin {@link BatchClassPlugin}
	 */
	private static void preparePluginForSerialization(BatchClassPlugin createdPlugin) {
		createdPlugin.setBatchClassModule(null);
		createdPlugin.setId(0);
		ArrayList<BatchClassPluginConfig> newPluginConfigs = new ArrayList<BatchClassPluginConfig>();
		for (BatchClassPluginConfig config : createdPlugin.getBatchClassPluginConfigs()) {
			config.setId(0);
			config.setBatchClassPlugin(null);
			newPluginConfigs.add(config);
			ArrayList<KVPageProcess> newKVPageProcess = new ArrayList<KVPageProcess>();
			for (KVPageProcess kv : config.getKvPageProcesses()) {
				kv.setId(0);
				kv.setBatchClassPluginConfig(null);
				newKVPageProcess.add(kv);
			}
			config.setKvPageProcesses(newKVPageProcess);
		}

		ArrayList<BatchClassDynamicPluginConfig> newDynamicPluginConfigs = new ArrayList<BatchClassDynamicPluginConfig>();
		for (BatchClassDynamicPluginConfig dynamicConfig : createdPlugin.getBatchClassDynamicPluginConfigs()) {
			dynamicConfig.setId(0);
			dynamicConfig.setBatchClassPlugin(null);
			newDynamicPluginConfigs.add(dynamicConfig);
			ArrayList<BatchClassDynamicPluginConfig> newChildren = new ArrayList<BatchClassDynamicPluginConfig>();
			for (BatchClassDynamicPluginConfig child : dynamicConfig.getChildren()) {
				child.setId(0);
				child.setBatchClassPlugin(null);
				child.setParent(null);
				newChildren.add(child);
			}
			dynamicConfig.setChildren(newChildren);
		}

		createdPlugin.setBatchClassPluginConfigs(newPluginConfigs);
	}

	/**
	 * This method prepares page type for serialization.
	 * 
	 * @param documentType {@link DocumentType}
	 */
	public static void preparePageTypeForSerialization(DocumentType documentType) {
		List<PageType> pages = documentType.getPages();
		List<PageType> newPageTypes = new ArrayList<PageType>();
		for (PageType pageType : pages) {
			newPageTypes.add(pageType);
			pageType.setId(0);
			pageType.setDocType(null);
			pageType.setIdentifier(null);
		}
		documentType.setPages(newPageTypes);
	}

	/**
	 * This method prepares field type for serialization.
	 * 
	 * @param documentType {@link DocumentType}
	 */
	public static void prepareFieldTypeForSerialization(DocumentType documentType) {
		List<FieldType> fieldTypes = documentType.getFieldTypes();
		List<FieldType> newFieldType = new ArrayList<FieldType>();
		for (FieldType fieldType : fieldTypes) {
			newFieldType.add(fieldType);
			fieldType.setId(0);
			fieldType.setDocType(null);
			fieldType.setIdentifier(null);
			prepareKVExtractionFieldForSerialization(fieldType);
			prepareRegexForSerialization(fieldType);
		}
		documentType.setFieldTypes(newFieldType);
	}

	/**
	 * This method prepares table info for serialization.
	 * 
	 * @param documentType {@link DocumentType}
	 */
	public static void prepareTableInfoForSerialization(DocumentType documentType) {
		List<TableInfo> tableInfos = documentType.getTableInfos();
		List<TableInfo> newTableInfo = new ArrayList<TableInfo>();
		for (TableInfo tableInfo : tableInfos) {
			newTableInfo.add(tableInfo);
			tableInfo.setId(0);
			tableInfo.setDocType(null);
			prepareTableColumnsInfoForSerialization(tableInfo);
		}
		documentType.setTableInfos(newTableInfo);
	}

	/**
	 * This method prepares table columns info for serialization.
	 * 
	 * @param tableInfo {@link TableInfo}
	 */
	public static void prepareTableColumnsInfoForSerialization(TableInfo tableInfo) {
		List<TableColumnsInfo> tableColumnsInfos = tableInfo.getTableColumnsInfo();
		List<TableColumnsInfo> newTableColumnsInfo = new ArrayList<TableColumnsInfo>();
		for (TableColumnsInfo tableColumnsInfo : tableColumnsInfos) {
			newTableColumnsInfo.add(tableColumnsInfo);
			tableColumnsInfo.setId(0);
		}
		tableInfo.setTableColumnsInfo(newTableColumnsInfo);
	}

	/**
	 * This method prepares KV Extraction field info for serialization.
	 * 
	 * @param fieldType {@link FieldType}
	 */
	public static void prepareKVExtractionFieldForSerialization(FieldType fieldType) {
		List<KVExtraction> kvExtraction2 = fieldType.getKvExtraction();
		List<KVExtraction> newKvExtraction = new ArrayList<KVExtraction>();
		for (KVExtraction kvExtraction : kvExtraction2) {
			newKvExtraction.add(kvExtraction);
			kvExtraction.setId(0);
			kvExtraction.setFieldType(null);
		}
		fieldType.setKvExtraction(newKvExtraction);
	}

	/**
	 *  This method prepares Regex for serialization.
	 * 
	 * @param fieldType {@link FieldType}
	 */
	public static void prepareRegexForSerialization(FieldType fieldType) {
		List<RegexValidation> regexValidations = fieldType.getRegexValidation();
		List<RegexValidation> regexValidations2 = new ArrayList<RegexValidation>();
		for (RegexValidation regexValidation : regexValidations) {
			regexValidations2.add(regexValidation);
			regexValidation.setId(0);
			regexValidation.setFieldType(null);
		}
		fieldType.setRegexValidation(regexValidations2);
	}
	
	/**
	 * This method prepares function key for serialization.
	 * 
	 * @param documentType {@link DocumentType}
	 */
	public static void prepareFunctionKeyForSerialization(DocumentType documentType) {
		List<FunctionKey> functionKeys = documentType.getFunctionKeys();
		List<FunctionKey> newFunctionKeys = new ArrayList<FunctionKey>();
		for (FunctionKey functionKey : functionKeys) {
			newFunctionKeys.add(functionKey);
		}
		documentType.setFunctionKeys(newFunctionKeys);

	}

	private static void createPatch(String pluginId, String pluginConfigId, BatchClassService batchClassService) {
		try {
			Plugin plugin = pluginService.getPluginPropertiesForPluginId(Long.valueOf(pluginId));
			ArrayList<BatchClassPluginConfig> newPluginConfigs = pluginNameVsBatchPluginConfigList.get(plugin.getPluginName());
			if (newPluginConfigs == null) {
				newPluginConfigs = new ArrayList<BatchClassPluginConfig>();
				pluginNameVsBatchPluginConfigList.put(plugin.getPluginName(), newPluginConfigs);
			}
			int plgId = Integer.parseInt(pluginId);
			int plgConfId = Integer.parseInt(pluginConfigId);
			boolean bcpcFound = false;
			BatchClassPluginConfig createdBatchClassPluginConfig = null;
			List<BatchClass> batchClasses = batchClassService.getAllLoadedBatchClassExcludeDeleted();
			for (BatchClass batchClass : batchClasses) {
				BatchClass loadedBatchClass = batchClassService.getLoadedBatchClassByIdentifier(batchClass.getIdentifier());
				List<BatchClassModule> batchClassModules = loadedBatchClass.getBatchClassModules();
				for (BatchClassModule bcm : batchClassModules) {
					List<BatchClassPlugin> batchClassPlugins = bcm.getBatchClassPlugins();
					for (BatchClassPlugin bcp : batchClassPlugins) {
						if (bcp.getPlugin().getId() == plgId) {
							List<BatchClassPluginConfig> batchClassPluginConfigs = bcp.getBatchClassPluginConfigs();
							bcpcFound = false;
							for (BatchClassPluginConfig bcpc : batchClassPluginConfigs) {
								if (bcpc.getId() == plgConfId) {
									createdBatchClassPluginConfig = bcpc;
									bcpcFound = true;
									break;
								}
							}

							if (bcpcFound) {
								createdBatchClassPluginConfig.setId(0);
								createdBatchClassPluginConfig.setBatchClassPlugin(null);
								newPluginConfigs.add(createdBatchClassPluginConfig);
								ArrayList<KVPageProcess> newKVPageProcess = new ArrayList<KVPageProcess>();
								for (KVPageProcess kv : createdBatchClassPluginConfig.getKvPageProcesses()) {
									kv.setId(0);
									kv.setBatchClassPluginConfig(null);
									newKVPageProcess.add(kv);
								}
								createdBatchClassPluginConfig.setKvPageProcesses(newKVPageProcess);
							}
						}
					}
				}
			}

		} catch (NumberFormatException e) {
			LOG.error("Plugin Id and Plugin Config Id should be numeric." + e.getMessage(), e);
		}

	}

	private static void createPatchForBatchClassModuleConfigs(BatchClassService service) {
		List<BatchClass> batchClassList = service.getAllBatchClassesExcludeDeleted();
		if (batchClassList != null && !batchClassList.isEmpty()) {
			BatchClass batchClass = service.getLoadedBatchClassByIdentifier(batchClassList.get(0).getIdentifier());
			service.evict(batchClass);
			List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
			for (BatchClassModule bcm : batchClassModules) {
				if (bcm != null && bcm.getBatchClassModuleConfig() != null && !bcm.getBatchClassModuleConfig().isEmpty()) {
					ArrayList<BatchClassModuleConfig> batchClassModuleConfigList = new ArrayList<BatchClassModuleConfig>();
					List<BatchClassModuleConfig> bcmcList = bcm.getBatchClassModuleConfig();
					for (BatchClassModuleConfig bcmc : bcmcList) {
						bcmc.setId(0);
						bcmc.setBatchClassModule(null);
						batchClassModuleConfigList.add(bcmc);
					}
					Module module = moduleService.getModulePropertiesForModuleId(Long.valueOf(bcm.getModule().getId()));
					moduleNameVsBatchClassModuleConfigMap.put(module.getName(), batchClassModuleConfigList);
				}
			}

			try {
				File serializedExportFile = new File(upgradePatchFolderPath + File.separator + "ModuleConfigUpdate"
						+ SERIALIZATION_EXT);
				SerializationUtils.serialize(moduleNameVsBatchClassModuleConfigMap, new FileOutputStream(serializedExportFile));
			} catch (FileNotFoundException e) {
				LOG.error(ERROR_OCCURRED_WHILE_CREATING_THE_SERIALIZABLE_FILE + e.getMessage(), e);
			}
		}
	}

	private static void createPatchForDependencies() {
		List<Plugin> pluginsList = pluginService.getAllPlugins();

		for (Plugin plugin : pluginsList) {
			ArrayList<Dependency> pluginDependencies = new ArrayList<Dependency>(plugin.getDependencies());
			changePluginIdToName(pluginDependencies);
			pluginNameVsDependencyMap.put(plugin.getPluginName(), pluginDependencies);
		}

		try {
			File serializedExportFile = new File(upgradePatchFolderPath + File.separator + DataAccessConstant.DEPENDENCY_UPDATE
					+ SERIALIZATION_EXT);
			SerializationUtils.serialize(pluginNameVsDependencyMap, new FileOutputStream(serializedExportFile));
		} catch (FileNotFoundException e) {
			LOG.error(ERROR_OCCURRED_WHILE_CREATING_THE_SERIALIZABLE_FILE + e.getMessage(), e);
		}
	}


	private static void changePluginIdToName(List<Dependency> dependencies) {
		for (Dependency dependency : dependencies) {
			dependency.setId(0);
			String dependenciesString = dependency.getDependencies();
			dependenciesString = changeDependenciesIdentifierToName(dependenciesString);
			dependency.setDependencies(dependenciesString);
		}
	}

	private static String changeDependenciesIdentifierToName(String dependencyNames) {

		String[] andDependencies = dependencyNames.split(DataAccessConstant.AND);
		StringBuffer andDependenciesNameAsString = new StringBuffer();

		for (String andDependency : andDependencies) {

			if (!andDependenciesNameAsString.toString().isEmpty()) {
				andDependenciesNameAsString.append(DataAccessConstant.AND);
			}

			String[] orDependencies = andDependency.split(DataAccessConstant.OR_SYMBOL);
			StringBuffer orDependenciesNameAsString = new StringBuffer();

			for (String dependencyIdentifier : orDependencies) {
				if (!orDependenciesNameAsString.toString().isEmpty()) {
					orDependenciesNameAsString.append(DataAccessConstant.OR_SYMBOL);
				}

				try {
					long dependencyId = Long.valueOf(dependencyIdentifier);
					orDependenciesNameAsString.append(pluginService.getPluginPropertiesForPluginId(dependencyId).getPluginName());
				} catch (NumberFormatException e) {
					LOG.error(e.getMessage());
				}
			}

			andDependenciesNameAsString.append(orDependenciesNameAsString);
			orDependenciesNameAsString = new StringBuffer();
		}
		return andDependenciesNameAsString.toString();
	}

	/**
	 * This is the main method.
	 * 
	 * @param args {@link String[]}
	 */
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:/META-INF/applicationContext-data-access.xml");
		context.start();
		BatchClassService batchClassService = ApplicationContextUtil.getSingleBeanOfType(context, BatchClassService.class);
		pluginService = ApplicationContextUtil.getSingleBeanOfType(context, PluginService.class);
		moduleService = ApplicationContextUtil.getSingleBeanOfType(context, ModuleService.class);
		createDBPatch(batchClassService);
	}
}
