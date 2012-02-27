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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.ephesoft.dcma.core.common.PluginProperty;
import com.ephesoft.dcma.da.dao.BatchClassPluginConfigDao;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;

/* Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.da.service.BatchClassService
 */
public class BatchClassPluginConfigServiceImpl implements BatchClassPluginConfigService {

	private static final String INVALID_PLUGIN_NAME = "Invalid Plugin name.";

	/**
	 * LOGGER to print the LOGGERging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchClassPluginConfigServiceImpl.class);

	@Autowired
	private BatchClassPluginConfigDao batchClassPluginConfigDao;

	@Transactional(readOnly = true)
	@Override
	public Map<String, String> getPluginPropertiesForBatch(String batchInstanceIdentifier, String pluginName) {
		Map<String, String> returnMap = new HashMap<String, String>();
		if (pluginName != null && pluginName.length() > 0) {
			List<BatchClassPluginConfig> allPluginProperties = batchClassPluginConfigDao.getPluginPropertiesForBatch(
					batchInstanceIdentifier, pluginName);
			for (BatchClassPluginConfig config : allPluginProperties) {
				returnMap.put(config.getName(), config.getValue());
			}
		} else {
			LOGGER.debug(INVALID_PLUGIN_NAME);
		}
		return returnMap;
	}

	@Transactional(readOnly = true)
	@Override
	public List<BatchClassPluginConfig> getAllPluginConfiguration(String batchInstanceIdentifier, String pluginName) {

		List<BatchClassPluginConfig> allPluginProperties = null;

		if (pluginName != null && pluginName.length() > 0) {
			allPluginProperties = batchClassPluginConfigDao.getPluginPropertiesForBatch(batchInstanceIdentifier, pluginName);
		} else {
			LOGGER.debug(INVALID_PLUGIN_NAME);
		}
		
		return allPluginProperties;
		
	}

	@Transactional(readOnly = true)
	@Override
	public Map<String, String> getPluginPropertiesForBatchClass(String batchClassIdentifier, String pluginName,
			PluginProperty pluginProperty) {
		Map<String, String> returnMap = new HashMap<String, String>();
		if (pluginName != null && pluginName.length() > 0) {
			List<BatchClassPluginConfig> allPluginProperties = batchClassPluginConfigDao.getPluginPropertiesForBatchClass(
					batchClassIdentifier, pluginName, pluginProperty);
			for (BatchClassPluginConfig config : allPluginProperties) {
				returnMap.put(config.getName(), config.getValue());
			}
		} else {
			LOGGER.debug(INVALID_PLUGIN_NAME);
		}
		return returnMap;
	}

	@Override
	public List<BatchClassPluginConfig> getPluginConfigurationForPluginId(Long pluginId) {
		return batchClassPluginConfigDao.getPluginConfigurationForPluginId(pluginId);

	}

	@Transactional(readOnly = false)
	@Override
	public void updatePluginConfiguration(List<BatchClassPluginConfig> batchClassPluginConfigs) {
		batchClassPluginConfigDao.updatePluginConfiguration(batchClassPluginConfigs);
	}

	@Override
	public Map<String, String> getPluginPropertiesForBatchClassByQualifier(String batchClassIdentifier, String pluginName,
			String qualifier) {
		Map<String, String> returnMap = new HashMap<String, String>();
		if (pluginName != null && pluginName.length() > 0) {
			List<BatchClassPluginConfig> allPluginProperties = batchClassPluginConfigDao
					.getAllPluginPropertiesForBatchClassByQualifier(batchClassIdentifier, pluginName, qualifier);
			for (BatchClassPluginConfig config : allPluginProperties) {
				returnMap.put(config.getName(), config.getValue());
			}
		} else {
			LOGGER.debug(INVALID_PLUGIN_NAME);
		}
		return returnMap;
	}

	@Transactional(readOnly = false)
	@Override
	public void updateSinglePluginConfiguration(BatchClassPluginConfig batchClassPluginConfig) {
		batchClassPluginConfigDao.updateSinglePluginConfiguration(batchClassPluginConfig);

	}

	@Transactional(readOnly = false)
	@Override
	public void removeBatchClassPluginConfig(BatchClassPluginConfig batchClassPluginConfig) {
		batchClassPluginConfigDao.removeBatchClassPluginConfig(batchClassPluginConfig);

	}

}
