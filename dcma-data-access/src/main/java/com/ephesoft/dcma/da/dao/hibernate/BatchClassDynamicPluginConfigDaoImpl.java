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
import com.ephesoft.dcma.da.dao.BatchClassDynamicPluginConfigDao;
import com.ephesoft.dcma.da.domain.BatchClassDynamicPluginConfig;
import com.ephesoft.dcma.da.domain.BatchInstance;

@Repository
public class BatchClassDynamicPluginConfigDaoImpl extends HibernateDao<BatchClassDynamicPluginConfig> implements
		BatchClassDynamicPluginConfigDao {

	private static final String BATCH_CLASS_MODULE = "batchClassModule";
	private static final String BATCH_CLASS_PLUGIN_BATCH_CLASS_MODULE = "batchClassPlugin.batchClassModule";
	private static final String BATCH_CLASS = "batchClass";
	private static final String BATCH_CLASS_PLUGIN = "batchClassPlugin";

	@Override
	public List<BatchClassDynamicPluginConfig> getDynamicPluginPropertiesForBatchClass(String batchClassIdentifier, String pluginName,
			PluginProperty pluginProperty) {

		DetachedCriteria criteria = criteria();
		criteria.createAlias(BATCH_CLASS_PLUGIN, BATCH_CLASS_PLUGIN, JoinFragment.INNER_JOIN);
		criteria.createAlias(BATCH_CLASS_PLUGIN_BATCH_CLASS_MODULE, BATCH_CLASS_MODULE, JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.batchClassModule.batchClass", BATCH_CLASS, JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.plugin", "plugin", JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.batchClassDynamicPluginConfigs", "batchClassDynamicPluginConfigs",
				JoinFragment.INNER_JOIN);
		if (pluginProperty != null) {
			criteria.add(Restrictions.eq("batchClassDynamicPluginConfigs.name", pluginProperty.getPropertyKey()));
		}
		criteria.add(Restrictions.eq("plugin.pluginName", pluginName));
		criteria.add(Restrictions.eq("batchClass.identifier", batchClassIdentifier));
		return find(criteria);
	}

	@Override
	public List<BatchClassDynamicPluginConfig> getAllDynamicPluginPropertiesForBatchInstance(String batchInstanceIdentifier) {
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

	@Override
	public List<BatchClassDynamicPluginConfig> getAllDynamicPluginPropertiesForBatchClass(String batchClassIdentifier) {
		DetachedCriteria criteria = criteria();
		criteria.createAlias(BATCH_CLASS_PLUGIN, BATCH_CLASS_PLUGIN, JoinFragment.INNER_JOIN);
		criteria.createAlias(BATCH_CLASS_PLUGIN_BATCH_CLASS_MODULE, BATCH_CLASS_MODULE, JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.batchClassModule.batchClass", BATCH_CLASS, JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.plugin", "plugin", JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.batchClassDynamicPluginConfigs", "batchClassDynamicPluginConfigs",
				JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq("batchClass.identifier", batchClassIdentifier));
		return find(criteria);
	}

	@Override
	public List<BatchClassDynamicPluginConfig> getDynamicPluginPropertiesForBatchClass(String batchClassIdentifier, String pluginName) {
		DetachedCriteria criteria = criteria();
		criteria.createAlias(BATCH_CLASS_PLUGIN, BATCH_CLASS_PLUGIN, JoinFragment.INNER_JOIN);
		criteria.createAlias(BATCH_CLASS_PLUGIN_BATCH_CLASS_MODULE, BATCH_CLASS_MODULE, JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.batchClassModule.batchClass", BATCH_CLASS, JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.plugin", "plugin", JoinFragment.INNER_JOIN);
		criteria.createAlias("batchClassPlugin.batchClassDynamicPluginConfigs", "batchClassDynamicPluginConfigs",
				JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq("plugin.pluginName", pluginName));
		criteria.add(Restrictions.eq("batchClass.identifier", batchClassIdentifier));
		return find(criteria);
	}

}
