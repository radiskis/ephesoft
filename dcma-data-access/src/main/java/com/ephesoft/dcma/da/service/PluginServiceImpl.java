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

package com.ephesoft.dcma.da.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ephesoft.dcma.da.dao.PluginConfigDao;
import com.ephesoft.dcma.da.dao.PluginDao;
import com.ephesoft.dcma.da.domain.Dependency;
import com.ephesoft.dcma.da.domain.Plugin;
import com.ephesoft.dcma.da.domain.PluginConfig;

/**
 * This is a database service to get the plugin details for a plugin.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.da.service.PluginService
 */
@Service
public class PluginServiceImpl implements PluginService {

	/**
	 * LOGGER to print the Logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchClassServiceImpl.class);

	/**
	 * pluginDao {@link PluginDao}.
	 */
	@Autowired
	private PluginDao pluginDao;

	/**
	 * pluginConfigDao {@link PluginConfigDao}.
	 */
	@Autowired
	private PluginConfigDao pluginConfigDao;

	/**
	 * API to get the plugin details by Id.
	 * 
	 * @param pluginId {@link Long}
	 * @return Plugin {@link Plugin}
	 */
	@Override
	@Transactional
	public Plugin getPluginPropertiesForPluginId(Long pluginId) {
		LOGGER.info("plugin id : " + pluginId);
		return pluginDao.getPluginPropertiesForPluginId(pluginId);
	}

	/**
	 * API to get the plugin details by Id.
	 * 
	 * @param pluginId {@link String}
	 * @return Plugin {@link Plugin}
	 */
	@Override
	public Plugin getPluginPropertiesForPluginName(String pluginName) {
		LOGGER.info("plugin name : " + pluginName);
		return pluginDao.getPluginByName(pluginName);
	}

	/**
	 * API to get plugins.
	 * 
	 * @param moduleId {@link Long}
	 * @param startResult int
	 * @param maxResult int
	 * @return List<{@link Plugin}>
	 */
	@Override
	public List<Plugin> getPlugins(Long moduleId, int startResult, int maxResult) {
		List<Plugin> pluginList = null;
		if (null == moduleId) {
			LOGGER.info("moduleId is null");
		} else {
			pluginList = pluginDao.getPlugins(moduleId, startResult, maxResult);
		}
		return pluginList;
	}

	/**
	 * API to get all the plugins.
	 * 
	 * @return {@link List} <{@link Plugin}>
	 */
	@Override
	@Transactional
	public List<Plugin> getAllPlugins() {
		LOGGER.info("Getting list of all plugins");
		List<Plugin> allPlugins = pluginDao.getAll();
		for (Plugin plugin : allPlugins) {
			List<Dependency> dependencies = plugin.getDependencies();
			for (Dependency dependency : dependencies) {
				dependency.getDependencies();
			}
		}
		return allPlugins;
	}

	/**
	 * API to create a new plugin.
	 * 
	 * @param plugin {@link Plugin}
	 */
	@Override
	@Transactional(readOnly = false)
	public void createNewPlugin(Plugin plugin) {
		LOGGER.info("Creating a new plugin: " + plugin.getPluginName());
		pluginDao.create(plugin);
	}

	/**
	 * API to merge/update the given plugin.
	 * 
	 * @param plugin {@link Plugin}
	 */
	@Override
	@Transactional(readOnly = false)
	public void mergePlugin(Plugin plugin) {
		LOGGER.info("Updating plugin: " + plugin.getPluginName());
		pluginDao.saveOrUpdate(plugin);
	}

	/**
	 * API To delete the given plugin.
	 * 
	 * @param plugin {@link Plugin}
	 * @param removeReferences boolean
	 */
	@Override
	@Transactional
	public void removePluginAndReferences(Plugin plugin, boolean removeReferences) {
		LOGGER.info("Entering method removePluginAndReferences.");
		if (plugin != null) {
			LOGGER.info("Deleting plugin with id: " + plugin.getId() + " with name: " + plugin.getPluginName());
			pluginDao.remove(plugin);

			LOGGER.debug("Remove references is " + removeReferences);
			if (removeReferences) {
				LOGGER.info("Removing the plugin configs associated with the plugin.");
				final List<PluginConfig> pluginConfigs = pluginConfigDao.getPluginConfigForPluginId(plugin.getId());

				for (PluginConfig pluginConfig : pluginConfigs) {
					LOGGER.info("Removing plugin config with id : " + pluginConfig.getId() + " and name : " + pluginConfig.getName());
					pluginConfigDao.remove(pluginConfig);
				}
			}
		}
		LOGGER.info("Exiting method removePluginAndReferences.");
	}

	/**
	 * API to retrieve names of all the plugins.
	 * 
	 * @return {@link List}< {@link String}>
	 */
	@Override
	public List<String> getAllPluginsNames() {
		LOGGER.info("Retriving all plugin names.");
		return pluginDao.getAllPluginsNames();
	}

}
