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
import java.util.Map;

import com.ephesoft.dcma.core.common.PluginProperty;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;

/**
 * This is a database service to read data required by Batch Class plugin configuration Service.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.da.service.BatchClassPluginConfigServiceImpl
 */
public interface BatchClassPluginConfigService {

	/**
	 * API to get Plug-in Properties For Batch given it's identifier and plug-in name.
	 * @param batchInstanceIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @return Map<{@link String}, {@link String}>
	 */
	Map<String, String> getPluginPropertiesForBatch(String batchInstanceIdentifier, String pluginName);

	/**
	 * API to get the plugin configuration details by plugin Id.
	 * 
	 * @param pluginId  {@link Long}
	 * @return List<{@link BatchClassPluginConfig}>
	 */
	List<BatchClassPluginConfig> getPluginConfigurationForPluginId(Long pluginId);
	
	/**
	 * API to get All Plug-in Configurations available given the batch instance identifier and plug-in name.
	 * @param batchInstanceIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @return List<{@link BatchClassPluginConfig}>
	 */
	List<BatchClassPluginConfig> getAllPluginConfiguration(String batchInstanceIdentifier, String pluginName);

	/**
	 * API to get Plugin Properties For Batch Class given it's identifier,plug-in name and plug-in property.
	 * @param batchClassId {@link String}
	 * @param pluginName {@link String}
	 * @param pluginProperty {@link PluginProperty}
	 * @return Map<{@link String}, {@link String}>
	 */
	Map<String, String> getPluginPropertiesForBatchClass(String batchClassIdentifier, String pluginName,
			PluginProperty pluginProperty);

	/**
	 * API to save the plugin configuration values into database.
	 * 
	 * @param batchClassPluginConfigs List<{@link BatchClassPluginConfig}>
	 */
	void updatePluginConfiguration(List<BatchClassPluginConfig> batchClassPluginConfigs);

	/**
	 * API to save the plugin configuration value into database.
	 * 
	 * @param batchClassPluginConfig List<{@link BatchClassPluginConfig}>
	 */
	void updateSinglePluginConfiguration(BatchClassPluginConfig batchClassPluginConfig);

	/**
	 * API to get the plugin properties based on configuration type.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @param qualifier {@link String}
	 * @return Map<{@link String}, {@link String}>
	 */
	Map<String, String> getPluginPropertiesForBatchClassByQualifier(String batchClassIdentifier, String pluginName,
			String qualifier);

	/**
	 * API to remove configuration.
	 * 
	 * @param batchClassPluginConfig {@link BatchClassPluginConfig}
	 */
	void removeBatchClassPluginConfig(BatchClassPluginConfig batchClassPluginConfig);
	
	/**
	 * API to evict the batch class plugin config object.
	 * @param batchClassPluginConfig {@link BatchClassPluginConfig}
	 */
	void evict(BatchClassPluginConfig batchClassPluginConfig);
	
	/**
	 * API to get the batch class plugin configuration details by plugin config Id.
	 * 
	 * @param pluginConfigId  {@link Long}
	 * @return List<{@link BatchClassPluginConfig}>
	 */
	List<BatchClassPluginConfig> getBatchClassPluginConfigForPluginConfigId(Long pluginConfigId);

}
