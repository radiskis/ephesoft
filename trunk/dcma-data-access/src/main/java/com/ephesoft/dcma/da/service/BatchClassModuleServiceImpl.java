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

package com.ephesoft.dcma.da.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.da.dao.BatchClassModuleDao;
import com.ephesoft.dcma.da.domain.BatchClassDynamicPluginConfig;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchClassModuleConfig;
import com.ephesoft.dcma.da.domain.BatchClassPlugin;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;
import com.ephesoft.dcma.da.domain.KVPageProcess;
import com.ephesoft.dcma.da.domain.Module;
import com.ephesoft.dcma.da.domain.ModuleConfig;

@Service
public class BatchClassModuleServiceImpl implements BatchClassModuleService {

	private static final String BATCH_CLASS_ID = "Batch class id : ";

	/**
	 * LOGGER to print the Logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchClassModuleServiceImpl.class);

	@Autowired
	private BatchClassModuleDao batchClassModuleDao;

	@Override
	public Integer countModules(String batchClassIdentifier) {
		LOGGER.info(BATCH_CLASS_ID + batchClassIdentifier);
		return batchClassModuleDao.countModules(batchClassIdentifier);
	}

	@Override
	public List<Module> getBatchClassModule(String batchClassIdentifier) {
		LOGGER.info(BATCH_CLASS_ID + batchClassIdentifier);
		return batchClassModuleDao.getBatchClassModule(batchClassIdentifier);
	}

	@Override
	public List<Module> getModules(String batchClassIdentifier, int firstIndex, int maxResults) {
		return batchClassModuleDao.getModules(batchClassIdentifier, firstIndex, maxResults);
	}

	@Override
	public BatchClassModule getBatchClassModuleByName(String batchClassIdentifier, String moduleName) {
		LOGGER.info(BATCH_CLASS_ID + batchClassIdentifier);
		BatchClassModule batchClassModule = batchClassModuleDao.getModuleByName(batchClassIdentifier, moduleName);

		List<BatchClassModuleConfig> modConfigs = batchClassModule.getBatchClassModuleConfig();

		for (BatchClassModuleConfig BCModConfig : modConfigs) {
			ModuleConfig moduleConfig = BCModConfig.getModuleConfig();
			if (LOGGER.isDebugEnabled() && moduleConfig != null) {
				LOGGER.debug(moduleConfig.getChildDisplayName());
			}
		}

		List<BatchClassPlugin> plugins = batchClassModule.getBatchClassPlugins();
		for (BatchClassPlugin plugin : plugins) {
			List<BatchClassPluginConfig> pluginConfigs = plugin.getBatchClassPluginConfigs();
			List<BatchClassDynamicPluginConfig> dynamicPluginConfigs = plugin.getBatchClassDynamicPluginConfigs();
			for (BatchClassPluginConfig conf : pluginConfigs) {
				List<KVPageProcess> kvs = conf.getKvPageProcesses();
				for (KVPageProcess kv : kvs) {
					if (LOGGER.isDebugEnabled() && kv != null) {
						LOGGER.debug(kv.getKeyPattern());
					}
				}
			}
			for (BatchClassDynamicPluginConfig dyPluginConfig : dynamicPluginConfigs) {

				List<BatchClassDynamicPluginConfig> children = dyPluginConfig.getChildren();
				for (BatchClassDynamicPluginConfig child : children) {
					if (LOGGER.isDebugEnabled() && child != null) {
						LOGGER.debug(child.getName());
					}
				}

				if (LOGGER.isDebugEnabled() && dyPluginConfig != null) {
					LOGGER.debug(dyPluginConfig.getName());
				}
			}
		}
		return batchClassModule;
	}

	@Override
	public BatchClassModule getBatchClassModuleByWorkflowName(String batchClassIdentifier, String workflowName) {
		LOGGER.info(BATCH_CLASS_ID + batchClassIdentifier);
		BatchClassModule batchClassModule = batchClassModuleDao.getModuleByWorkflowName(batchClassIdentifier, workflowName);
		if(batchClassModule == null) {
			LOGGER.error("No module found with workflowName:" + workflowName + " in batch class id:" + batchClassIdentifier + ". Skipping the updates for this module.");
		} else {
			List<BatchClassModuleConfig> modConfigs = batchClassModule.getBatchClassModuleConfig();
			
			for (BatchClassModuleConfig BCModConfig : modConfigs) {
				ModuleConfig moduleConfig = BCModConfig.getModuleConfig();
				if (LOGGER.isDebugEnabled() && moduleConfig != null) {
					LOGGER.debug(moduleConfig.getChildDisplayName());
				}
			}
			
			List<BatchClassPlugin> plugins = batchClassModule.getBatchClassPlugins();
			for (BatchClassPlugin plugin : plugins) {
				List<BatchClassPluginConfig> pluginConfigs = plugin.getBatchClassPluginConfigs();
				List<BatchClassDynamicPluginConfig> dynamicPluginConfigs = plugin.getBatchClassDynamicPluginConfigs();
				for (BatchClassPluginConfig conf : pluginConfigs) {
					List<KVPageProcess> kvs = conf.getKvPageProcesses();
					for (KVPageProcess kv : kvs) {
						if (LOGGER.isDebugEnabled() && kv != null) {
							LOGGER.debug(kv.getKeyPattern());
						}
					}
				}
				for (BatchClassDynamicPluginConfig dyPluginConfig : dynamicPluginConfigs) {
					
					List<BatchClassDynamicPluginConfig> children = dyPluginConfig.getChildren();
					for (BatchClassDynamicPluginConfig child : children) {
						if (LOGGER.isDebugEnabled() && child != null) {
							LOGGER.debug(child.getName());
						}
					}
					
					if (LOGGER.isDebugEnabled() && dyPluginConfig != null) {
						LOGGER.debug(dyPluginConfig.getName());
					}
				}
			}
		}
		return batchClassModule;
	}

	@Override
	@Transactional
	public void evict(BatchClassModule batchClassModule) {
		batchClassModuleDao.evict(batchClassModule);
	}

	@Override
	public List<BatchClassModule> getAllBatchClassModulesByIdentifier(String batchClassIdentifier) {
		List<BatchClassModule> batchClassModules = null;
		List<Module> batchClassModule = batchClassModuleDao.getBatchClassModule(batchClassIdentifier);
		if (batchClassModule != null) {
			batchClassModules = new ArrayList<BatchClassModule>();
			for (Module module : batchClassModule) {
				BatchClassModule moduleByName = batchClassModuleDao.getModuleByName(batchClassIdentifier, module.getName());
				if (moduleByName != null) {
					batchClassModules.add(moduleByName);
				}
			}
		}
		return batchClassModules;

	}

	@Override
	public List<BatchClassModule> getAllBatchClassModules() {
		LOGGER.info("Getting list of all batch class modules");
		return batchClassModuleDao.getAll();
	}

	@Override
	public List<BatchClassModule> getAllBatchClassModules(Order order) {
		return batchClassModuleDao.getAllBatchClassModulesInOrder(order);
	}

}
