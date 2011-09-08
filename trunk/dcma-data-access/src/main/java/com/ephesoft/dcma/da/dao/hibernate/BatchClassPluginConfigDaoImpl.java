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

@Repository
public class BatchClassPluginConfigDaoImpl extends HibernateDao<BatchClassPluginConfig> implements BatchClassPluginConfigDao {

	@Override
	public List<BatchClassPluginConfig> getPluginPropertiesForBatch(String batchInstanceIdentifier, String pluginName) {

		DetachedCriteria criteria = criteria();
		criteria.createAlias("batchClassPlugin", "batchClassPlugin", JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.batchClassModule", "batchClassModule", JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.batchClassModule.batchClass", "batchClass", JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.plugin", "plugin", JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq("plugin.pluginName", pluginName));
		DetachedCriteria subQuery = criteria(BatchInstance.class);
		subQuery.add(Restrictions.eq("identifier", batchInstanceIdentifier));
		subQuery.createAlias("batchClass", "batchClass1", JoinFragment.INNER_JOIN);
		subQuery.setProjection(Projections.property("batchClass1.identifier"));
		criteria.add(Subqueries.propertyEq("batchClass.identifier", subQuery));
		List<BatchClassPluginConfig> batchClassPluginConfigDaoImpls = find(criteria);
		return batchClassPluginConfigDaoImpls;

	}

	@Override
	public List<BatchClassPluginConfig> getPluginPropertiesForBatchClass(String batchClassIdentifier, String pluginName,
			PluginProperty pluginProperty) {

		DetachedCriteria criteria = criteria();
		criteria.createAlias("batchClassPlugin", "batchClassPlugin", JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.batchClassModule", "batchClassModule", JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.batchClassModule.batchClass", "batchClass", JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.plugin", "plugin", JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.batchClassPluginConfigs", "batchClassPluginConfigs", JoinFragment.INNER_JOIN);
		if (pluginProperty != null) {
			criteria.add(Restrictions.eq("batchClassPluginConfigs.name", pluginProperty.getPropertyKey()));
		}
		criteria.add(Restrictions.eq("plugin.pluginName", pluginName));
		criteria.add(Restrictions.eq("batchClass.identifier", batchClassIdentifier));
		return find(criteria);
	}

	@Override
	public List<BatchClassPluginConfig> getPluginConfigurationForPluginId(Long pluginId) {
		DetachedCriteria criteria = criteria();
		criteria.createAlias("batchClassPlugin", "batchClassPlugin", JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq("batchClassPlugin.identifier", pluginId));
		return find(criteria);
	}

	@Override
	public void updatePluginConfiguration(List<BatchClassPluginConfig> batchClassPluginConfigs) {

		for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
			saveOrUpdate(batchClassPluginConfig);
		}
	}

	@Override
	public List<BatchClassPluginConfig> getAllPluginPropertiesForBatchInstance(String batchInstanceIdentifier) {
		DetachedCriteria criteria = criteria();
		criteria.createAlias("batchClassPlugin", "batchClassPlugin");
		criteria.createAlias("batchClassPlugin.batchClassModule", "batchClassModule");
		criteria.createAlias("batchClassModule.batchClass", "batchClass");

		DetachedCriteria subQuery = criteria(BatchInstance.class);
		subQuery.add(Restrictions.eq("identifier", batchInstanceIdentifier));
		subQuery.setProjection(Projections.property("batchClass.id"));

		criteria.add(Subqueries.propertyEq("batchClass.id", subQuery));

		return find(criteria);
	}

	@Override
	public List<BatchClassPluginConfig> getAllPluginPropertiesForBatchClassByQualifier(String batchClassIdentifier,
			String pluginName, String qualifier) {
		DetachedCriteria criteria = criteria();
		criteria.createAlias("batchClassPlugin", "batchClassPlugin", JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.batchClassModule", "batchClassModule", JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.batchClassModule.batchClass", "batchClass", JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.plugin", "plugin", JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.batchClassPluginConfigs", "batchClassPluginConfigs", JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq("plugin.pluginName", pluginName));
		if (qualifier != null) {
			criteria.add(Restrictions.eq("batchClassPluginConfigs.qualifier", qualifier));
		}
		criteria.add(Restrictions.eq("batchClass.identifier", batchClassIdentifier));
		return find(criteria);
	}

	@Override
	public List<BatchClassPluginConfig> getAllPluginPropertiesForBatchClass(String batchClassIdentifier) {
		DetachedCriteria criteria = criteria();
		criteria.createAlias("batchClassPlugin", "batchClassPlugin", JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.batchClassModule", "batchClassModule", JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.batchClassModule.batchClass", "batchClass", JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.plugin", "plugin", JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.batchClassPluginConfigs", "batchClassPluginConfigs", JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq("batchClass.identifier", batchClassIdentifier));
		return find(criteria);
	}

	@Override
	public List<BatchClassPluginConfig> getPluginPropertiesForBatchClass(String batchClassIdentifier, String pluginName) {
		DetachedCriteria criteria = criteria();
		criteria.createAlias("batchClassPlugin", "batchClassPlugin", JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.batchClassModule", "batchClassModule", JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.batchClassModule.batchClass", "batchClass", JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.plugin", "plugin", JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.batchClassPluginConfigs", "batchClassPluginConfigs", JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq("plugin.pluginName", pluginName));
		criteria.add(Restrictions.eq("batchClass.identifier", batchClassIdentifier));
		return find(criteria);
	}

	@Override
	public void updateSinglePluginConfiguration(BatchClassPluginConfig batchClassPluginConfig) {
		if (batchClassPluginConfig.getId() != 0l) {
			saveOrUpdate(batchClassPluginConfig);
		} else {
			create(batchClassPluginConfig);
		}
	}

	@Override
	public void removeBatchClassPluginConfig(BatchClassPluginConfig batchClassPluginConfig) {
		remove(batchClassPluginConfig);

	}
}
