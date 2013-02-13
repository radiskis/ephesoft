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

package com.ephesoft.dcma.da.dao;

import java.util.List;

import com.ephesoft.dcma.core.common.PluginProperty;
import com.ephesoft.dcma.core.dao.CacheableDao;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;

/**
 * This is dao class for batch class plugin configuration.
 *  
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.da.dao.hibernate.BatchClassPluginConfigDaoImpl
 */
public interface BatchClassPluginConfigDao extends CacheableDao<BatchClassPluginConfig> {

	/**
	 * API to get plugin properties for Batch.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @return List<BatchClassPluginConfig> 
	 */
	List<BatchClassPluginConfig> getPluginPropertiesForBatch(String batchInstanceIdentifier, String pluginName);

	/**
	 * API to get plugin configuration for plugin id.
	 * 
	 * @param pluginId {@link Long}
	 * @return List<BatchClassPluginConfig> 
	 */
	List<BatchClassPluginConfig> getPluginConfigurationForPluginId(Long pluginId);

	/**
	 * API to get plugin properties for Batch class.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @return List<BatchClassPluginConfig> 
	 */
	List<BatchClassPluginConfig> getPluginPropertiesForBatchClass(String batchClassIdentifier, String pluginName);

	/**
	 * API to update plugin configuration.
	 * 
	 * @param batchClassPluginConfig {@link BatchClassPluginConfig}
	 */
	void updatePluginConfiguration(List<BatchClassPluginConfig> batchClassPluginConfigs);

	/**
	 * API to update the single plugin configuration.
	 * 
	 * @param batchClassPluginConfig {@link BatchClassPluginConfig}
	 */
	void updateSinglePluginConfiguration(BatchClassPluginConfig batchClassPluginConfig);

	/**
	 * API to remove the batch class plugin configuration.
	 * 
	 * @param batchClassPluginConfig {@link BatchClassPluginConfig}
	 */
	void removeBatchClassPluginConfig(BatchClassPluginConfig batchClassPluginConfig);

	/**
	 * API to get all plugin properties for Batch instance.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @return List<BatchClassPluginConfig> 
	 */
	List<BatchClassPluginConfig> getAllPluginPropertiesForBatchInstance(String batchInstanceIdentifier);

	/**
	 * API to get all plugin properties for Batch class.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @return List<BatchClassPluginConfig> 
	 */
	List<BatchClassPluginConfig> getAllPluginPropertiesForBatchClass(String batchClassIdentifier);

	/**
	 * API to get all plugin properties for Batch class by qualifier.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @param qualifier {@link String}
	 * @return List<BatchClassPluginConfig> 
	 */
	List<BatchClassPluginConfig> getAllPluginPropertiesForBatchClassByQualifier(String batchClassIdentifier,
			String pluginName, String qualifier);

	/**
	 * API to get plugin properties for Batch class.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @param pluginProperty {@link PluginProperty}
	 * @return List<BatchClassPluginConfig> 
	 */
	List<BatchClassPluginConfig> getPluginPropertiesForBatchClass(String batchClassIdentifier, String pluginName,
			PluginProperty pluginProperty);
	
	/**
	 * API to get the batch class plugin configuration details by plugin config Id.
	 * 
	 * @param pluginConfigId {@link Long}
	 * @return List<{@link BatchClassPluginConfig}>
	 */
	List<BatchClassPluginConfig> getBatchClassPluginConfigurationForPluginConfigId(Long pluginConfigId);
}
