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

package com.ephesoft.dcma.da.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import com.ephesoft.dcma.core.service.DBScriptExecuter;
import com.ephesoft.dcma.da.constant.DataAccessConstant;
import com.ephesoft.dcma.da.dao.ModuleConfigDao;
import com.ephesoft.dcma.da.dao.ModuleDao;
import com.ephesoft.dcma.da.dao.PluginConfigDao;
import com.ephesoft.dcma.da.dao.PluginDao;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchClassModuleConfig;
import com.ephesoft.dcma.da.domain.BatchClassPlugin;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;
import com.ephesoft.dcma.da.domain.Module;
import com.ephesoft.dcma.da.domain.ModuleConfig;
import com.ephesoft.dcma.da.domain.Plugin;
import com.ephesoft.dcma.da.domain.PluginConfig;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.util.FileUtils;

public class ExecuteUpdatePatch {

	private static final Logger log = LoggerFactory.getLogger(ExecuteUpdatePatch.class);

	@Autowired
	private BatchClassService batchClassService;

	@Autowired
	private PluginConfigDao dao;

	@Autowired
	private ModuleConfigDao moduleConfigDao;

	@Autowired
	private PluginDao pluginDao;

	@Autowired
	private ModuleDao moduleDao;

	@Autowired
	private DBScriptExecuter executer;

	private static final String SERIALIZATION_EXT = ".ser";

	private String dbPatchFolderLocation;

	private boolean patchEnabled;

	private Map<Integer, List<BatchClassPluginConfig>> newPluginConfigMap = new HashMap<Integer, List<BatchClassPluginConfig>>();

	private Map<String, List<BatchClassModule>> batchClassNameVsModulesMap = new HashMap<String, List<BatchClassModule>>();

	private Map<String, List<BatchClass>> nameVsBatchClassListMap = new HashMap<String, List<BatchClass>>();

	private Map<String, List<BatchClassPlugin>> batchClassNameVsBatchClassPluginMap = new HashMap<String, List<BatchClassPlugin>>();

	private Map<String, List<BatchClass>> batchClassNameVsBatchClassMap = new HashMap<String, List<BatchClass>>();

	private Map<Long, List<BatchClassModuleConfig>> moduleIdVsBatchClassModuleConfigs = new HashMap<Long, List<BatchClassModuleConfig>>();

	public void init() {
		if (!patchEnabled) {
			log.info("No Upgrade Patch Configured. Continuing with the start up.");
			return;
		}
		try {
			log.info("==============Running the Upgrade Patch=======================");

			executer.execute(new ClassPathResource("META-INF/dcma-data-access/pre-schema.sql"));

			populateMap();

			log.info("==========Running Upgrade Patch for Module Configs.==========");

			updateModuleConfigs();

			log.info("Upgrade Patch for Module Configs executed successfully.");

			log.info("==========Running Upgrade Patch for Module.==========");

			updateModules();

			log.info("Upgrade Patch for Modules executed successfully.");

			log.info("==========Running Upgrade Patch for Plugins.==========");

			updatePlugin();

			log.info("Upgrade Patch for Plugins executed successfully.");

			log.info("==========Running Upgrade Patch for Plugin Configs.==========");

			updatePluginConfig();

			log.info("Upgrade Patch for Plugin Configs executed successfully.");

			log.info("==========Running Upgrade Patch for Batch Class.==========");

			updateBatchClasses();

			log.info("Upgrade Patch for Batch Class executed successfully.");

			executer.execute(new ClassPathResource("META-INF/dcma-data-access/post-schema.sql"));

			log.info("==========Upgrade Patch finished successfully.==========");

			updateSQLFiles();

		} catch (Exception e) {
			log.error("An Exception occurred while executing the patch." + e.getMessage(), e);
		}
	}

	private void updateSQLFiles() {
		try {
			ClassPathResource classPathResource = new ClassPathResource("META-INF/dcma-data-access/dcma-db.properties");
			File propertyFile = classPathResource.getFile();
			Map<String, String> propertyMap = new HashMap<String, String>();
			propertyMap.put("upgradePatch.enable", "false");

			String comments = "Patch disabled.";

			FileUtils.updateProperty(propertyFile, propertyMap, comments);
		} catch (IOException e) {
		}
	}

	public void updateModules() {

		batchClassNameVsModulesMap = readModuleSerializeFile();
		if (batchClassNameVsModulesMap == null || batchClassNameVsModulesMap.isEmpty()) {
			log.info("No data recovered from serialized file. Returning..");
			return;
		}

		updatePluginConfigsForNewModules();

		for (String batchClassName : batchClassNameVsModulesMap.keySet()) {

			List<BatchClass> batchClasses = nameVsBatchClassListMap.get(batchClassName);
			List<BatchClassModule> newModulesToBeAdded = batchClassNameVsModulesMap.get(batchClassName);
			if (newModulesToBeAdded != null && !newModulesToBeAdded.isEmpty()) {
				for (BatchClass batchClass : batchClasses) {
					List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
					for (BatchClassModule bcmNew : newModulesToBeAdded) {
						boolean isModulePresent = false;
						for (BatchClassModule bcm : batchClassModules) {
							if (bcmNew.getModule().getId() == bcm.getModule().getId()) {
								isModulePresent = true;
								break;
							}
						}

						if (!isModulePresent) {
							batchClass.getBatchClassModules().add(bcmNew);
						}

					}
				}
			}
			log.info("Updating Batch Classes....");
			for (BatchClass batchClass : batchClasses) {
				batchClassService.merge(batchClass);
			}

		}

	}

	public void updatePlugin() {

		readPluginSerializeFile();

		nameVsBatchClassListMap.clear();
		populateMap();

		if (batchClassNameVsBatchClassPluginMap == null || batchClassNameVsBatchClassPluginMap.isEmpty()) {
			log.info("No data recovered from serialized file.");
			return;
		}

		for (String key : batchClassNameVsBatchClassPluginMap.keySet()) {
			List<BatchClassPlugin> newPlugins = batchClassNameVsBatchClassPluginMap.get(key);
			updatePluginConfigsForNewConfigs(newPlugins);
		}

		for (String key : batchClassNameVsBatchClassPluginMap.keySet()) {
			StringTokenizer pluginConfigTokens = new StringTokenizer(key, ",");
			String batchClassName = null;
			String moduleId = null;
			try {
				batchClassName = pluginConfigTokens.nextToken();
				moduleId = pluginConfigTokens.nextToken();
				int modId = Integer.parseInt(moduleId);

				List<BatchClass> batchClasses = nameVsBatchClassListMap.get(batchClassName);
				List<BatchClassPlugin> newPlugins = batchClassNameVsBatchClassPluginMap.get(key);
				for (BatchClass batchClass : batchClasses) {
					List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
					for (BatchClassModule bcm : batchClassModules) {
						if (bcm.getModule().getId() == modId) {
							for (BatchClassPlugin newPlugin : newPlugins) {
								boolean isPluginPresent = isPluginAlreadyPresent(bcm, newPlugin.getPlugin().getPluginName());
								if (!isPluginPresent) {
									bcm.getBatchClassPlugins().add(newPlugin);
								}
							}
						}
					}
				}

				log.info("Updating Batch Classes....");
				for (BatchClass batchClass : batchClasses) {
					batchClass = batchClassService.merge(batchClass);
				}

				nameVsBatchClassListMap.clear();
				populateMap();

			} catch (NoSuchElementException e) {
				log.error("Incomplete data specified in properties file.", e);
				batchClassNameVsBatchClassPluginMap.clear();
			} catch (NumberFormatException e) {
				log.error("Module Id or Plugin id is not numeric.", e);
				batchClassNameVsBatchClassPluginMap.clear();
			}
		}

		nameVsBatchClassListMap.clear();
		populateMap();
	}

	public void updateBatchClasses() {

		batchClassNameVsBatchClassMap = readBatchClassSerializeFile();
		if (batchClassNameVsBatchClassMap == null || batchClassNameVsBatchClassMap.isEmpty()) {
			log.info("No data recovered from serialized file. Returning..");
			return;
		}

		for (String batchClassName : batchClassNameVsBatchClassMap.keySet()) {
			List<BatchClass> batchClasses = batchClassService.getAllBatchClasses();
			for (BatchClass batchClass : batchClasses) {
				if (batchClassName.equalsIgnoreCase(batchClass.getName())) {
					batchClassNameVsBatchClassMap.remove(batchClassName);
					break;
				}
			}
		}
		if (batchClassNameVsBatchClassMap.size() > 0) {

			updatePluginConfigsForBatchClass();

			updateModuleConfigsForBatchClass();

			for (String batchClassName : batchClassNameVsBatchClassMap.keySet()) {

				List<BatchClass> newBatchClassToBeAdded = batchClassNameVsBatchClassMap.get(batchClassName);
				ClassPathResource classPathResource = new ClassPathResource("META-INF/dcma-batch/dcma-batch.properties");
				String uncFolderLocation = null;
				try {
					File propertyFile = classPathResource.getFile();
					Properties properties = new Properties();
					FileInputStream fileInputStream = new FileInputStream(propertyFile);
					properties.load(fileInputStream);
					uncFolderLocation = properties.getProperty(DataAccessConstant.BASE_FOLDER_LOCATION);
				} catch (IOException e) {
					log.error("Unable to retriving property file :" + e.getMessage(), e);
				}

				if (newBatchClassToBeAdded != null && !newBatchClassToBeAdded.isEmpty()) {
					for (BatchClass batchClass : newBatchClassToBeAdded) {
						uncFolderLocation = uncFolderLocation + File.separator + new File(batchClass.getUncFolder()).getName();
						batchClass.setUncFolder(uncFolderLocation);
						File file = new File(uncFolderLocation);
						if (!file.exists()) {
							file.mkdir();
						}
						batchClassService.createBatchClassWithoutWatch(batchClass);
					}
				}
				log.info("Updating Batch Classes....");
			}
		}
	}

	private Map<String, List<BatchClassModule>> readModuleSerializeFile() {

		FileInputStream fileInputStream = null;
		Map<String, List<BatchClassModule>> newModulesMap = null;
		File serializedFile = null;
		try {
			String moduleFilePath = dbPatchFolderLocation + File.separator + "ModuleUpdate" + SERIALIZATION_EXT;
			serializedFile = new File(moduleFilePath);
			fileInputStream = new FileInputStream(serializedFile);
			newModulesMap = (Map<String, List<BatchClassModule>>) SerializationUtils.deserialize(fileInputStream);
			updateFile(serializedFile, moduleFilePath);
		} catch (IOException e) {
			log.info("Error during reading the serialized file. ");
		} catch (Exception e) {
			log.error("Error during de-serializing the properties for Database Upgrade: ", e);
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (Exception e) {
				if (serializedFile != null)
					log.error("Problem closing stream for file :" + serializedFile.getName());
			}
		}

		return newModulesMap;
	}

	private Map<String, List<BatchClass>> readBatchClassSerializeFile() {

		FileInputStream fileInputStream = null;
		Map<String, List<BatchClass>> newBatchClassMap = null;
		File serializedFile = null;
		try {
			String batchClassFilePath = dbPatchFolderLocation + File.separator + "BatchClassUpdate" + SERIALIZATION_EXT;
			serializedFile = new File(batchClassFilePath);
			fileInputStream = new FileInputStream(serializedFile);
			newBatchClassMap = (Map<String, List<BatchClass>>) SerializationUtils.deserialize(fileInputStream);
			updateFile(serializedFile, batchClassFilePath);
		} catch (IOException e) {
			log.info("Error during reading the serialized file. ");
		} catch (Exception e) {
			log.error("Error during de-serializing the properties for Database Upgrade: ", e);
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (Exception e) {
				if (serializedFile != null)
					log.error("Problem closing stream for file :" + serializedFile.getName());
			}
		}

		return newBatchClassMap;
	}

	private void readPluginSerializeFile() {
		FileInputStream fileInputStream = null;
		File serializedFile = null;
		try {
			String pluginFilePath = dbPatchFolderLocation + File.separator + "PluginUpdate" + SERIALIZATION_EXT;
			serializedFile = new File(pluginFilePath);
			fileInputStream = new FileInputStream(serializedFile);
			batchClassNameVsBatchClassPluginMap = (Map<String, List<BatchClassPlugin>>) SerializationUtils
					.deserialize(fileInputStream);
			updateFile(serializedFile, pluginFilePath);
		} catch (IOException e) {
			log.info("Error during reading the serialized file. ");
		} catch (Exception e) {
			log.error("Error during de-serializing the properties for Database Upgrade: ", e);
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (Exception e) {
				if (serializedFile != null)
					log.error("Problem closing stream for file :" + serializedFile.getName());
			}
		}
	}

	private Map<Integer, List<BatchClassPluginConfig>> readPluginConfigSerializeFile() {
		FileInputStream fileInputStream = null;
		Map<Integer, List<BatchClassPluginConfig>> newPluginConfigsMap = null;
		File serializedFile = null;
		try {
			String pluginConfigFilePath = dbPatchFolderLocation + File.separator + "PluginConfigUpdate" + SERIALIZATION_EXT;
			serializedFile = new File(pluginConfigFilePath);
			fileInputStream = new FileInputStream(serializedFile);
			newPluginConfigsMap = (Map<Integer, List<BatchClassPluginConfig>>) SerializationUtils.deserialize(fileInputStream);
			updateFile(serializedFile, pluginConfigFilePath);
		} catch (IOException e) {
			log.info("Error during reading the serialized file. ");
		} catch (Exception e) {
			log.error("Error during de-serializing the properties for Database Upgrade: ", e);
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (Exception e) {
				if (serializedFile != null)
					log.error("Problem closing stream for file :" + serializedFile.getName());
			}
		}
		return newPluginConfigsMap;
	}

	private Map<Long, List<BatchClassModuleConfig>> readModuleConfigSerializeFile() {
		FileInputStream fileInputStream = null;
		Map<Long, List<BatchClassModuleConfig>> newModuleConfigsMap = null;
		File serializedFile = null;
		try {
			String moduleConfigFilePath = dbPatchFolderLocation + File.separator + "ModuleConfigUpdate" + SERIALIZATION_EXT;
			serializedFile = new File(moduleConfigFilePath);
			fileInputStream = new FileInputStream(serializedFile);
			newModuleConfigsMap = (Map<Long, List<BatchClassModuleConfig>>) SerializationUtils.deserialize(fileInputStream);
			updateFile(serializedFile, moduleConfigFilePath);
		} catch (IOException e) {
			log.info("Error during reading the serialized file. ");
		} catch (Exception e) {
			log.error("Error during de-serializing the properties for Database Upgrade: ", e);
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (Exception e) {
				if (serializedFile != null)
					log.error("Problem closing stream for file :" + serializedFile.getName());
			}
		}
		return newModuleConfigsMap;
	}

	public void updatePluginConfig() throws CloneNotSupportedException {
		newPluginConfigMap = readPluginConfigSerializeFile();
		if (newPluginConfigMap == null || newPluginConfigMap.isEmpty()) {
			log.info("No data recovered from serialized file.");
			return;
		}
		for (Integer pluginId : newPluginConfigMap.keySet()) {
			List<BatchClassPluginConfig> pluginConfigs = newPluginConfigMap.get(pluginId);
			if (pluginConfigs != null && !pluginConfigs.isEmpty()) {
				setUpdatedPluginConfigs(pluginConfigs);
			}
		}

		List<BatchClass> batchClasses = batchClassService.getAllLoadedBatchClassExcludeDeleted();
		for (Integer plgId : newPluginConfigMap.keySet()) {
			for (BatchClass batchClass : batchClasses) {
				List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
				for (BatchClassModule bcm : batchClassModules) {
					List<BatchClassPlugin> batchClassPlugins = bcm.getBatchClassPlugins();
					for (BatchClassPlugin bcp : batchClassPlugins) {
						if (bcp.getPlugin().getId() == plgId.longValue()) {
							List<BatchClassPluginConfig> toBeAddedConfigs = newPluginConfigMap.get(new Integer(plgId));
							for (BatchClassPluginConfig bcpc : toBeAddedConfigs) {
								boolean isPresnt = isBatchClassPluginConfigPresent(bcpc, bcp.getBatchClassPluginConfigs());
								if (!isPresnt) {
									bcp.getBatchClassPluginConfigs().add((BatchClassPluginConfig) bcpc.clone());
								}
							}

						}
					}
				}
			}

		}

		for (BatchClass batchClass : batchClasses) {
			batchClassService.merge(batchClass);
		}
	}

	public void updateModuleConfigs() {
		moduleIdVsBatchClassModuleConfigs = readModuleConfigSerializeFile();
		if (moduleIdVsBatchClassModuleConfigs == null || moduleIdVsBatchClassModuleConfigs.isEmpty()) {
			log.info("No Serialize file present for Module Configs... Returning..");
			return;
		}
		for (Long pluginId : moduleIdVsBatchClassModuleConfigs.keySet()) {
			List<BatchClassModuleConfig> moduleConfigs = moduleIdVsBatchClassModuleConfigs.get(pluginId);
			if (moduleConfigs != null && !moduleConfigs.isEmpty()) {
				setUpdatedModuleConfigs(moduleConfigs);
			}
		}
		List<BatchClass> batchClasses = batchClassService.getAllLoadedBatchClassExcludeDeleted();
		for (Long modId : moduleIdVsBatchClassModuleConfigs.keySet()) {
			for (BatchClass batchClass : batchClasses) {
				List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
				for (BatchClassModule bcm : batchClassModules) {
					if (bcm.getModule().getId() == modId.longValue()) {
						List<BatchClassModuleConfig> toBeAddedConfigs = moduleIdVsBatchClassModuleConfigs.get(new Long(modId));
						for (BatchClassModuleConfig bcmc : toBeAddedConfigs) {
							boolean isPresnt = isBatchClassModuleConfigPresent(bcmc, bcm.getBatchClassModuleConfig());
							if (!isPresnt) {
								if (bcm.getBatchClassModuleConfig() != null) {
									bcm.getBatchClassModuleConfig().add((BatchClassModuleConfig) bcmc);
								} else {
									List<BatchClassModuleConfig> batchClassModuleConfigs = new ArrayList<BatchClassModuleConfig>();
									batchClassModuleConfigs.add((BatchClassModuleConfig) bcmc);
									bcm.setBatchClassModuleConfig(batchClassModuleConfigs);
								}
							}
						}

					}
				}
			}
		}
		log.info("Updating Batch Classes....");
		for (BatchClass batchClass : batchClasses) {
			batchClass = batchClassService.merge(batchClass);
		}
	}

	public void setDbPatchFolderLocation(String dbPatchFolderLocation) {
		this.dbPatchFolderLocation = dbPatchFolderLocation;
	}

	public void setPatchEnabled(boolean isPatchEnabled) {
		this.patchEnabled = isPatchEnabled;
	}

	private void populateMap() {
		List<BatchClass> batchClasses = batchClassService.getAllLoadedBatchClassExcludeDeleted();
		for (BatchClass batchClass : batchClasses) {
			if (nameVsBatchClassListMap.get(batchClass.getName()) == null) {
				List<BatchClass> batchList = new ArrayList<BatchClass>();
				batchList.add(batchClass);
				nameVsBatchClassListMap.put(batchClass.getName(), batchList);
			} else {
				List<BatchClass> batchList = nameVsBatchClassListMap.get(batchClass.getName());
				batchList.add(batchClass);
			}
		}
	}

	private boolean isBatchClassPluginConfigPresent(BatchClassPluginConfig config, List<BatchClassPluginConfig> configs) {
		boolean isPresent = false;
		for (BatchClassPluginConfig batchClassPluginConfig : configs) {
			if (batchClassPluginConfig.getPluginConfig().getName().equalsIgnoreCase(config.getPluginConfig().getName())) {
				isPresent = true;
			}
		}
		return isPresent;
	}

	private void updatePluginConfigsForNewConfigs(List<BatchClassPlugin> newPlugins) {
		for (BatchClassPlugin bcp : newPlugins) {
			List<BatchClassPluginConfig> pluginConfigs = bcp.getBatchClassPluginConfigs();
			setUpdatedPluginConfigs(pluginConfigs);
			Plugin plugin = pluginDao.getPluginByName(bcp.getPlugin().getPluginName());
			if (plugin != null) {
				bcp.setPlugin(plugin);
			}
		}
	}

	private void updateModuleForNewConfigs(List<BatchClassModule> newModules) {
		for (BatchClassModule bcm : newModules) {
			List<BatchClassModuleConfig> moduleConfigs = bcm.getBatchClassModuleConfig();
			setUpdatedModuleConfigs(moduleConfigs);
			Module module = moduleDao.getModuleByName(bcm.getModule().getName());
			if (module != null) {
				bcm.setModule(module);
			}
		}
	}

	private void setUpdatedPluginConfigs(List<BatchClassPluginConfig> pluginConfigs) {
		for (BatchClassPluginConfig bcpc : pluginConfigs) {
			PluginConfig pluginConfig = dao.getPluginConfigByName(bcpc.getPluginConfig().getName());
			if (pluginConfig != null) {
				bcpc.setPluginConfig(pluginConfig);
			}
		}
	}

	private void setUpdatedModuleConfigs(List<BatchClassModuleConfig> moduleConfigs) {
		for (BatchClassModuleConfig bcmc : moduleConfigs) {
			String childKey = bcmc.getModuleConfig().getChildKey();
			if (childKey != null) {
				List<ModuleConfig> moduleConfigsList = moduleConfigDao.getModuleByChildName(childKey);
				for (ModuleConfig moduleConfig : moduleConfigsList) {
					if (moduleConfig != null) {
						bcmc.setModuleConfig(moduleConfig);
					}
				}
			} else {
				List<ModuleConfig> moduleConfigsList = moduleConfigDao.getModuleByChildName(childKey);
				for (ModuleConfig moduleConfig : moduleConfigsList) {
					if (moduleConfig != null && bcmc.getModuleConfig().isMandatory() == moduleConfig.isMandatory()) {
						bcmc.setModuleConfig(moduleConfig);
					}
				}
			}
		}
	}

	private boolean isPluginAlreadyPresent(BatchClassModule bcm, String pluginName) {
		boolean isPluginPresent = false;
		List<BatchClassPlugin> batchClassPlugins = bcm.getBatchClassPlugins();
		for (BatchClassPlugin bcp : batchClassPlugins) {
			if (bcp.getPlugin().getPluginName().equalsIgnoreCase(pluginName)) {
				isPluginPresent = true;
				break;
			}
		}
		return isPluginPresent;
	}

	private void updatePluginConfigsForNewModules() {
		for (String batchClassName : batchClassNameVsModulesMap.keySet()) {
			List<BatchClassModule> newModules = batchClassNameVsModulesMap.get(batchClassName);
			for (BatchClassModule batchClassModule : newModules) {
				List<BatchClassPlugin> batchClassPlugins = batchClassModule.getBatchClassPlugins();
				for (BatchClassPlugin batchClassPlugin : batchClassPlugins) {
					Plugin plugin = pluginDao.get(batchClassPlugin.getPlugin().getId());
					if (plugin != null) {
						batchClassPlugin.setPlugin(plugin);
					}
				}
				updatePluginConfigsForNewConfigs(batchClassPlugins);
			}
		}
	}

	private void updatePluginConfigsForBatchClass() {
		for (String batchClassName : batchClassNameVsBatchClassMap.keySet()) {
			List<BatchClass> batchClassList = batchClassNameVsBatchClassMap.get(batchClassName);
			for (BatchClass batchClass : batchClassList) {
				List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
				for (BatchClassModule batchClassModule : batchClassModules) {
					List<BatchClassPlugin> batchClassPlugins = batchClassModule.getBatchClassPlugins();
					for (BatchClassPlugin batchClassPlugin : batchClassPlugins) {
						Plugin plugin = pluginDao.get(batchClassPlugin.getPlugin().getId());
						if (plugin != null) {
							batchClassPlugin.setPlugin(plugin);
						}
					}
					updatePluginConfigsForNewConfigs(batchClassPlugins);
				}
			}
		}
	}

	private void updateModuleConfigsForBatchClass() {
		for (String batchClassName : batchClassNameVsBatchClassMap.keySet()) {
			List<BatchClass> batchClassList = batchClassNameVsBatchClassMap.get(batchClassName);
			for (BatchClass batchClass : batchClassList) {
				List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
				for (BatchClassModule batchClassModule : batchClassModules) {
					List<BatchClassModuleConfig> batchClassModuleConfigs = batchClassModule.getBatchClassModuleConfig();
					for (BatchClassModuleConfig batchClassModuleConfig : batchClassModuleConfigs) {
						ModuleConfig moduleConfig = moduleConfigDao.get(batchClassModuleConfig.getModuleConfig().getId());
						if (moduleConfig != null) {
							batchClassModuleConfig.setModuleConfig(moduleConfig);
							batchClassModuleConfig.setId(0);
						}
					}
				}
				updateModuleForNewConfigs(batchClassModules);
			}
		}
	}

	private void updateFile(File serialFile, String filePath) {
		try {
			File dest = new File(filePath + "-executed");
			boolean renameSuccess = serialFile.renameTo(dest);
			if (renameSuccess) {
				log.info(serialFile.getName() + " renamed successfully to " + dest.getName());
			} else {
				log.debug("Unable to rename the serialize file " + serialFile.getName() + " to " + dest.getName());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}

	private boolean isBatchClassModuleConfigPresent(BatchClassModuleConfig config, List<BatchClassModuleConfig> configs) {
		boolean isPresent = false;
		if (configs != null) {
			for (BatchClassModuleConfig batchClassModuleConfig : configs) {
				if (batchClassModuleConfig != null && batchClassModuleConfig.getModuleConfig() != null) {
					String childKey = batchClassModuleConfig.getModuleConfig().getChildKey();
					if (childKey != null && childKey.equalsIgnoreCase(config.getModuleConfig().getChildKey())
							&& batchClassModuleConfig.getModuleConfig().isMandatory() == config.getModuleConfig().isMandatory()) {
						isPresent = true;
						break;
					} else if (childKey == null && config.getModuleConfig() != null
							&& childKey == config.getModuleConfig().getChildKey()
							&& batchClassModuleConfig.getModuleConfig().isMandatory() == config.getModuleConfig().isMandatory()) {
						isPresent = true;
						break;
					}
				}
			}
		}
		return isPresent;
	}
}
