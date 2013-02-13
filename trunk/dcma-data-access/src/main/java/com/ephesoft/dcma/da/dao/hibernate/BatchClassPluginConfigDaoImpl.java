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

package com.ephesoft.dcma.da.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinFragment;
import org.springframework.stereotype.Repository;

import com.ephesoft.dcma.core.common.PluginProperty;
import com.ephesoft.dcma.core.dao.hibernate.HibernateDao;
import com.ephesoft.dcma.da.dao.BatchClassPluginConfigDao;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;
import com.ephesoft.dcma.da.domain.BatchInstance;

/**
 * This is dao class for batch class plugin configuration.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.da.dao.BatchClassPluginConfigDao
 */
@Repository
public class BatchClassPluginConfigDaoImpl extends HibernateDao<BatchClassPluginConfig> implements BatchClassPluginConfigDao {

	/**
	 * BATCH_CLASS_PLUGIN_CONFIGS String.
	 */
	private static final String BATCH_CLASS_PLUGIN_CONFIGS = "batchClassPluginConfigs";
	
	/**
	 * BATCH_CLASS_PLUGIN_BATCH_CLASS_PLUGIN_CONFIGS String.
	 */
	private static final String BATCH_CLASS_PLUGIN_BATCH_CLASS_PLUGIN_CONFIGS = "batchClassPlugin.batchClassPluginConfigs";

	/** 
	 * BATCH_CLASS_IDENTIFIER String.
	 */
	private static final String BATCH_CLASS_IDENTIFIER = "batchClass.identifier";
	
	/**
	 * PLUGIN_PLUGIN_NAME String.
	 */
	private static final String PLUGIN_PLUGIN_NAME = "plugin.pluginName";
	
	/**
	 * PLUGIN String.
	 */ 
	private static final String PLUGIN = "plugin";
	
	/**
	 * BATCH_CLASS_PLUGIN_PLUGIN String.
	 */
	private static final String BATCH_CLASS_PLUGIN_PLUGIN = "batchClassPlugin.plugin";
	
	/**
	 * BATCH_CLASS_PLUGIN_BATCH_CLASS_MODULE_BATCH_CLASS String.
	 */
	private static final String BATCH_CLASS_PLUGIN_BATCH_CLASS_MODULE_BATCH_CLASS = "batchClassPlugin.batchClassModule.batchClass";
	
	/**
	 * BATCH_CLASS String.
	 */
	private static final String BATCH_CLASS = "batchClass";
	
	/**
	 * BATCH_CLASS_MODULE String.
	 */
	private static final String BATCH_CLASS_MODULE = "batchClassModule";
	
	/**
	 * BATCH_CLASS_PLUGIN_BATCH_CLASS_MODULE String.
	 */
	private static final String BATCH_CLASS_PLUGIN_BATCH_CLASS_MODULE = "batchClassPlugin.batchClassModule";
	
	/**
	 * BATCH_CLASS_PLUGIN String.
	 */
	private static final String BATCH_CLASS_PLUGIN = "batchClassPlugin";
	
	/**
	 * PLUGIN_CONFIG_ID String.
	 */
	private static final String PLUGIN_CONFIG_ID = "pluginConfig.id";

	/**
	 * API to get plugin properties for Batch.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @return List<BatchClassPluginConfig> 
	 */
	@Override
	public List<BatchClassPluginConfig> getPluginPropertiesForBatch(String batchInstanceIdentifier, String pluginName) {

		DetachedCriteria criteria = criteria();
		criteria.createAlias(BATCH_CLASS_PLUGIN, BATCH_CLASS_PLUGIN, JoinFragment.INNER_JOIN);
		criteria.createAlias(BATCH_CLASS_PLUGIN_BATCH_CLASS_MODULE, BATCH_CLASS_MODULE, JoinFragment.INNER_JOIN);
		criteria.createAlias(BATCH_CLASS_PLUGIN_BATCH_CLASS_MODULE_BATCH_CLASS, BATCH_CLASS, JoinFragment.INNER_JOIN);
		criteria.createAlias(BATCH_CLASS_PLUGIN_PLUGIN, PLUGIN, JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq(PLUGIN_PLUGIN_NAME, pluginName));
		DetachedCriteria subQuery = criteria(BatchInstance.class);
		subQuery.add(Restrictions.eq("identifier", batchInstanceIdentifier));
		subQuery.createAlias(BATCH_CLASS, "batchClass1", JoinFragment.INNER_JOIN);
		subQuery.setProjection(Projections.property("batchClass1.identifier"));
		criteria.add(Subqueries.propertyEq(BATCH_CLASS_IDENTIFIER, subQuery));
		return find(criteria);

	}

	/**
	 * API to get plugin properties for Batch class.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @param pluginProperty {@link PluginProperty}
	 * @return List<BatchClassPluginConfig> 
	 */
	@Override
	public List<BatchClassPluginConfig> getPluginPropertiesForBatchClass(String batchClassIdentifier, String pluginName,
			PluginProperty pluginProperty) {

		DetachedCriteria criteria = criteria();
		criteria.createAlias(BATCH_CLASS_PLUGIN, BATCH_CLASS_PLUGIN, JoinFragment.INNER_JOIN);
		criteria.createAlias(BATCH_CLASS_PLUGIN_BATCH_CLASS_MODULE, BATCH_CLASS_MODULE, JoinFragment.INNER_JOIN);
		criteria.createAlias(BATCH_CLASS_PLUGIN_BATCH_CLASS_MODULE_BATCH_CLASS, BATCH_CLASS, JoinFragment.INNER_JOIN);
		criteria.createAlias(BATCH_CLASS_PLUGIN_PLUGIN, PLUGIN, JoinFragment.INNER_JOIN);
		criteria.createAlias(BATCH_CLASS_PLUGIN_BATCH_CLASS_PLUGIN_CONFIGS, BATCH_CLASS_PLUGIN_CONFIGS, JoinFragment.INNER_JOIN);
		if (pluginProperty != null) {
			criteria.add(Restrictions.eq("batchClassPluginConfigs.name", pluginProperty.getPropertyKey()));
		}
		criteria.add(Restrictions.eq(PLUGIN_PLUGIN_NAME, pluginName));
		criteria.add(Restrictions.eq(BATCH_CLASS_IDENTIFIER, batchClassIdentifier));
		return find(criteria);
	}

	/**
	 * API to get plugin configuration for plugin id.
	 * 
	 * @param pluginId {@link Long}
	 * @return List<BatchClassPluginConfig> 
	 */
	@Override
	public List<BatchClassPluginConfig> getPluginConfigurationForPluginId(Long pluginId) {
		DetachedCriteria criteria = criteria();
		criteria.createAlias(BATCH_CLASS_PLUGIN, BATCH_CLASS_PLUGIN, JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq("batchClassPlugin.id", pluginId));
		return find(criteria);
	}

	/**
	 * API to update plugin configuration.
	 * 
	 * @param batchClassPluginConfig {@link BatchClassPluginConfig}
	 */
	@Override
	public void updatePluginConfiguration(List<BatchClassPluginConfig> batchClassPluginConfigs) {

		for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
			saveOrUpdate(batchClassPluginConfig);
		}
	}

	/**
	 * API to get all plugin properties for Batch instance.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @return List<BatchClassPluginConfig> 
	 */
	@Override
	public List<BatchClassPluginConfig> getAllPluginPropertiesForBatchInstance(String batchInstanceIdentifier) {
		DetachedCriteria criteria = criteria();
		criteria.createAlias(BATCH_CLASS_PLUGIN, BATCH_CLASS_PLUGIN);
		criteria.createAlias(BATCH_CLASS_PLUGIN_BATCH_CLASS_MODULE, BATCH_CLASS_MODULE);
		criteria.createAlias("batchClassModule.batchClass", BATCH_CLASS);

		DetachedCriteria subQuery = criteria(BatchInstance.class);
		subQuery.add(Restrictions.eq("identifier", batchInstanceIdentifier));
		subQuery.setProjection(Projections.property("batchClass.id"));

		criteria.add(Subqueries.propertyEq("batchClass.id", subQuery));

		return find(criteria);
	}

	/**
	 * API to get all plugin properties for Batch class by qualifier.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @param qualifier {@link String}
	 * @return List<BatchClassPluginConfig> 
	 */
	@Override
	public List<BatchClassPluginConfig> getAllPluginPropertiesForBatchClassByQualifier(String batchClassIdentifier,
			String pluginName, String qualifier) {
		DetachedCriteria criteria = criteria();
		criteria.createAlias(BATCH_CLASS_PLUGIN, BATCH_CLASS_PLUGIN, JoinFragment.INNER_JOIN);
		criteria.createAlias(BATCH_CLASS_PLUGIN_BATCH_CLASS_MODULE, BATCH_CLASS_MODULE, JoinFragment.INNER_JOIN);
		criteria.createAlias(BATCH_CLASS_PLUGIN_BATCH_CLASS_MODULE_BATCH_CLASS, BATCH_CLASS, JoinFragment.INNER_JOIN);
		criteria.createAlias(BATCH_CLASS_PLUGIN_PLUGIN, PLUGIN, JoinFragment.INNER_JOIN);
		criteria.createAlias(BATCH_CLASS_PLUGIN_BATCH_CLASS_PLUGIN_CONFIGS, BATCH_CLASS_PLUGIN_CONFIGS, JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq(PLUGIN_PLUGIN_NAME, pluginName));
		if (qualifier != null) {
			criteria.add(Restrictions.eq("batchClassPluginConfigs.qualifier", qualifier));
		}
		criteria.add(Restrictions.eq(BATCH_CLASS_IDENTIFIER, batchClassIdentifier));
		return find(criteria);
	}

	/**
	 * API to get all plugin properties for Batch class.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @return List<BatchClassPluginConfig> 
	 */
	@Override
	public List<BatchClassPluginConfig> getAllPluginPropertiesForBatchClass(String batchClassIdentifier) {
		DetachedCriteria criteria = criteria();
		criteria.createAlias(BATCH_CLASS_PLUGIN, BATCH_CLASS_PLUGIN, JoinFragment.INNER_JOIN);
		criteria.createAlias(BATCH_CLASS_PLUGIN_BATCH_CLASS_MODULE, BATCH_CLASS_MODULE, JoinFragment.INNER_JOIN);
		criteria.createAlias(BATCH_CLASS_PLUGIN_BATCH_CLASS_MODULE_BATCH_CLASS, BATCH_CLASS, JoinFragment.INNER_JOIN);
		criteria.createAlias(BATCH_CLASS_PLUGIN_PLUGIN, PLUGIN, JoinFragment.INNER_JOIN);
		criteria.createAlias(BATCH_CLASS_PLUGIN_BATCH_CLASS_PLUGIN_CONFIGS, BATCH_CLASS_PLUGIN_CONFIGS, JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq(BATCH_CLASS_IDENTIFIER, batchClassIdentifier));
		return find(criteria);
	}

	/**
	 * API to get plugin properties for Batch class.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @return List<BatchClassPluginConfig> 
	 */
	@Override
	public List<BatchClassPluginConfig> getPluginPropertiesForBatchClass(String batchClassIdentifier, String pluginName) {
		DetachedCriteria criteria = criteria();
		criteria.createAlias(BATCH_CLASS_PLUGIN, BATCH_CLASS_PLUGIN, JoinFragment.INNER_JOIN);
		criteria.createAlias(BATCH_CLASS_PLUGIN_BATCH_CLASS_MODULE, BATCH_CLASS_MODULE, JoinFragment.INNER_JOIN);
		criteria.createAlias(BATCH_CLASS_PLUGIN_BATCH_CLASS_MODULE_BATCH_CLASS, BATCH_CLASS, JoinFragment.INNER_JOIN);
		criteria.createAlias(BATCH_CLASS_PLUGIN_PLUGIN, PLUGIN, JoinFragment.INNER_JOIN);
		criteria.createAlias(BATCH_CLASS_PLUGIN_BATCH_CLASS_PLUGIN_CONFIGS, BATCH_CLASS_PLUGIN_CONFIGS, JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq(PLUGIN_PLUGIN_NAME, pluginName));
		criteria.add(Restrictions.eq(BATCH_CLASS_IDENTIFIER, batchClassIdentifier));
		return find(criteria);
	}

	/**
	 * API to update the single plugin configuration.
	 * 
	 * @param batchClassPluginConfig {@link BatchClassPluginConfig}
	 */
	@Override
	public void updateSinglePluginConfiguration(BatchClassPluginConfig batchClassPluginConfig) {
		if (batchClassPluginConfig.getId() != 0l) {
			saveOrUpdate(batchClassPluginConfig);
		} else {
			create(batchClassPluginConfig);
		}
	}

	/**
	 * API to remove the batch class plugin configuration.
	 * 
	 * @param batchClassPluginConfig {@link BatchClassPluginConfig}
	 */
	@Override
	public void removeBatchClassPluginConfig(BatchClassPluginConfig batchClassPluginConfig) {
		remove(batchClassPluginConfig);

	}

	/**
	 * API to get the batch class plugin configuration details by plugin config Id.
	 * 
	 * @param pluginConfigId {@link Long}
	 * @return List<{@link BatchClassPluginConfig}>
	 */
	@Override
	public List<BatchClassPluginConfig> getBatchClassPluginConfigurationForPluginConfigId(Long pluginConfigId) {
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(PLUGIN_CONFIG_ID, pluginConfigId));
		return find(criteria);
	}

}
