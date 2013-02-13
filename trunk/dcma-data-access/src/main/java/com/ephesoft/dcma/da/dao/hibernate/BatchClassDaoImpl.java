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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.dao.hibernate.HibernateDao;
import com.ephesoft.dcma.core.hibernate.EphesoftCriteria;
import com.ephesoft.dcma.da.dao.BatchClassDao;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassDynamicPluginConfig;
import com.ephesoft.dcma.da.domain.BatchClassGroups;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchClassModuleConfig;
import com.ephesoft.dcma.da.domain.BatchClassPlugin;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;
import com.ephesoft.dcma.da.domain.BatchClassScannerConfiguration;
import com.ephesoft.dcma.da.domain.KVPageProcess;

/**
 * This Dao deals with Batch Class.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.da.dao.BatchClassDao
 */
@Repository
public class BatchClassDaoImpl extends HibernateDao<BatchClass> implements BatchClassDao {

	/**
	 * Initializing SYSTEM_FOLDER, constant String.
	 */
	private static final String SYSTEM_FOLDER = "systemFolder";
	
	/**
	 * Initializing BATCH_ID, constant String.
	 */
	private static final String BATCH_ID = "id";
	
	/**
	 * Initializing IS_DELETED, constant String.
	 */
	private static final String IS_DELETED = "deleted";
	
	/**
	 * Initializing IDENTIFIER, constant String.
	 */
	private static final String IDENTIFIER = "identifier";
	
	/**
	 * Initializing ASSIGNED_GROUPS_NAME, constant String.
	 */
	private static final String ASSIGNED_GROUPS_NAME = "assignedGroups.groupName";
	
	/**
	 * Initializing UNC_FOLDER, constant String.
	 */
	private static final String UNC_FOLDER = "uncFolder";
	
	/**
	 * Initializing NAME, constant String.
	 */
	private static final String NAME = "name";
	
	/**
	 * Initializing CURRENT_USER, constant String.
	 */
	private static final String CURRENT_USER = "currentUser";
	
	/**
	 * Initializing PROCESS_NAME, constant String.
	 */
	private static final String PROCESS_NAME = "processName";
	
	/**
	 * Initializing ASSIGNED_GROUPS, constant String.
	 */
	private static final String ASSIGNED_GROUPS = "assignedGroups";
	
	/**
	 * LOG to print the logging information.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(BatchClassDaoImpl.class);

	/**
	 * An api to getch batch class by unc folder name.
	 * 
	 * @param folderName {@link String}
	 * @return {@link BatchClass}
	 */
	@Override
	public BatchClass getBatchClassbyUncFolder(final String folderName) {
		LOG.info("folder name : " + folderName);
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(UNC_FOLDER, folderName));
		return this.findSingle(criteria);
	}

	/**
	 * An api to fetch BatchClass by batch Class name.
	 * 
	 * @param batchClassName {@link String}
	 * @return {@link BatchClass}
	 */
	@Override
	public BatchClass getBatchClassbyName(final String batchClassName) {
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(NAME, batchClassName));
		criteria.add(Restrictions.or(Restrictions.isNull(IS_DELETED), Restrictions.eq(IS_DELETED, false)));
		return this.findSingle(criteria);
	}

	/**
	 * An api to fetch BatchClass by batch Class processName.
	 * 
	 * @param processName {@link String}
	 * @return {@link BatchClass}
	 */
	@Override
	public BatchClass getBatchClassbyProcessName(final String processName) {
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(PROCESS_NAME, processName));
		criteria.add(Restrictions.or(Restrictions.isNull(IS_DELETED), Restrictions.eq(IS_DELETED, false)));
		return this.findSingle(criteria);
	}

	/**
	 * This API will fetch all the batch classes.
	 * 
	 * @return List<BatchClass>
	 */
	@Override
	public List<BatchClass> getAllBatchClasses() {
		DetachedCriteria criteria = criteria();
		return this.find(criteria);
	}

	/**
	 * This API will fetch all the unlocked batch classes.
	 * 
	 * @return List<BatchClass>
	 */
	@Override
	public List<BatchClass> getAllUnlockedBatchClasses() {
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.isNull(CURRENT_USER));
		criteria.add(Restrictions.or(Restrictions.isNull(IS_DELETED), Restrictions.eq(IS_DELETED, false)));
		return this.find(criteria);
	}

	/**
	 * To get Batch Class by Identifier.
	 * @param batchClassIdentifier String
	 * @return BatchClass
	 */
	@Override
	public BatchClass getBatchClassByIdentifier(final String batchClassIdentifier) {
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(IDENTIFIER, batchClassIdentifier));
		criteria.add(Restrictions.or(Restrictions.isNull(IS_DELETED), Restrictions.eq(IS_DELETED, false)));
		return this.findSingle(criteria);
	}

	/**
	 * This method will update the existing batch class.
	 * 
	 * @param batchClass {@link BatchClass}
	 */
	@Override
	public void updateBatchClass(final BatchClass batchClass) {
		saveOrUpdate(batchClass);
	}
	
	/**
	 * API to fetch BatchClass for the current user.
	 * 
	 * @param currentUser {@link String}
	 * @return List<BatchClass>
	 */
	@Override
	public List<BatchClass> getAllBatchClassesForCurrentUser(final String currentUser) {
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(CURRENT_USER, currentUser));
		criteria.add(Restrictions.or(Restrictions.isNull(IS_DELETED), Restrictions.eq(IS_DELETED, false)));
		return find(criteria);
	}

	/**
	 * This API will fetch all the unc folder paths.
	 * 
	 * @return List<String>
	 */
	@Override
	public List<String> getAllUNCFolderPaths() {
		DetachedCriteria criteria = criteria();
		criteria.setProjection(Projections.property(UNC_FOLDER));
		return find(criteria);
	}
	
	/**
	 * API to get the list of Batch Classes specifying startindex, no of results and sorting if any.
	 * 
	 * @param firstResult int
	 * @param maxResults int 
	 * @param order List<Order>
	 * @param userRoles Set<String>
	 * @return List of batch class.
	 */
	@Override
	public List<BatchClass> getBatchClassList(final int firstResult, final int maxResults, final List<Order> order,
			final Set<String> userRoles) {
		EphesoftCriteria criteria = criteria();
		List<BatchClass> batchClassList = null;
		if (userRoles == null) {
			batchClassList = new ArrayList<BatchClass>(0);
		} else {
			List<String> roleList = new ArrayList<String>();
			for (String userRole : userRoles) {
				if (null == userRole || userRole.isEmpty()) {
					continue;
				}
				roleList.add(userRole);
			}
			DetachedCriteria detachedCriteria = criteria();
			detachedCriteria.createAlias(ASSIGNED_GROUPS, ASSIGNED_GROUPS);
			detachedCriteria.add(Restrictions.in(ASSIGNED_GROUPS_NAME, roleList));
			detachedCriteria.setProjection(Projections.distinct(Projections.property(BATCH_ID)));
			criteria.add(Subqueries.propertyIn(BATCH_ID, detachedCriteria));
			criteria.add(Restrictions.or(Restrictions.isNull(IS_DELETED), Restrictions.eq(IS_DELETED, false)));
			batchClassList = find(criteria, firstResult, maxResults, order.toArray(new Order[order.size()]));
		}
		return batchClassList;
	}
	
	/**
	 * This API will fetch all the batch classes of current user role.
	 * 
	 * @param userRoles Set<String>
	 * @return List<BatchClass>
	 */
	@Override
	public List<BatchClass> getAllBatchClassesByUserRoles(final Set<String> userRoles) {
		DetachedCriteria criteria = criteria();
		List<BatchClass> batchClassList = null;
		if (userRoles == null) {
			batchClassList = new ArrayList<BatchClass>();
		} else {
			List<String> roleList = new ArrayList<String>();
			for (String userRole : userRoles) {
				if (null == userRole || userRole.isEmpty()) {
					continue;
				}
				roleList.add(userRole);
			}
			criteria.createAlias(ASSIGNED_GROUPS, ASSIGNED_GROUPS);
			criteria.add(Restrictions.in(ASSIGNED_GROUPS_NAME, roleList));
			criteria.add(Restrictions.or(Restrictions.isNull(IS_DELETED), Restrictions.eq(IS_DELETED, false)));
			criteria.addOrder(org.hibernate.criterion.Order.asc(BATCH_ID));
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			batchClassList = find(criteria);
		}

		return batchClassList;
	}

	/**
	 * This API will fetch the size of batchclass.
	 * 
	 * @param userRoles Set<String>
	 * @return batchClass size
	 */
	@Override
	public int getAllBatchClassCountExcludeDeleted(Set<String> userRoles) {
		DetachedCriteria criteria = criteria();
		if (userRoles != null && !userRoles.isEmpty()) {
			List<String> roleList = new ArrayList<String>();
			for (String userRole : userRoles) {
				if (null == userRole || userRole.isEmpty()) {
					continue;
				}
				roleList.add(userRole);
			}
			criteria.createAlias(ASSIGNED_GROUPS, ASSIGNED_GROUPS);
			criteria.add(Restrictions.in(ASSIGNED_GROUPS_NAME, roleList));
		}
		criteria.add(Restrictions.or(Restrictions.isNull(IS_DELETED), Restrictions.eq(IS_DELETED, false)));
		return count(criteria);
	}

	/**
	 * This API will fetch the batch class list excluding the deleted batch class.
	 * 
	 * @return List<BatchClass>
	 */
	@Override
	public List<BatchClass> getAllBatchClassExcludeDeleted() {
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.or(Restrictions.isNull(IS_DELETED), Restrictions.eq(IS_DELETED, false)));
		return this.find(criteria);
	}

	/**
	 * This API will fetch the batch class (eagerly loaded) list excluding the deleted batch class.
	 * 
	 * @return List<BatchClass>
	 */
	@Override
	public List<BatchClass> getAllLoadedBatchClassExcludeDeleted() {
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.or(Restrictions.isNull(IS_DELETED), Restrictions.eq(IS_DELETED, false)));
		List<BatchClass> batchClasses = this.find(criteria);
		for (BatchClass batchClass : batchClasses) {
			for (BatchClassGroups batchClassGroups : batchClass.getAssignedGroups()) {
				if (LOG.isDebugEnabled() && batchClassGroups != null) {
					LOG.debug(batchClassGroups.getGroupName());
				}
			}
			for (BatchClassScannerConfiguration batchClassScannerConfig : batchClass.getBatchClassScannerConfiguration()) {
				if (LOG.isDebugEnabled() && batchClassScannerConfig != null) {
					LOG.debug(batchClassScannerConfig.getValue());
					for (BatchClassScannerConfiguration childScannerConfig : batchClassScannerConfig.getChildren()) {
						if (LOG.isDebugEnabled() && childScannerConfig != null) {
							LOG.debug(childScannerConfig.getValue());
						}
					}
				}
			}
			for (BatchClassModule mod : batchClass.getBatchClassModules()) {
				List<BatchClassPlugin> plugins = mod.getBatchClassPlugins();
				List<BatchClassModuleConfig> batchClassModuleConfig = mod.getBatchClassModuleConfig();
				for (BatchClassPlugin plugin : plugins) {
					List<BatchClassPluginConfig> pluginConfigs = plugin.getBatchClassPluginConfigs();
					List<BatchClassDynamicPluginConfig> dynamicPluginConfigs = plugin.getBatchClassDynamicPluginConfigs();
					for (BatchClassPluginConfig conf : pluginConfigs) {
						List<KVPageProcess> kvs = conf.getKvPageProcesses();
						for (KVPageProcess kv : kvs) {
							if (LOG.isDebugEnabled() && kv != null) {
								LOG.debug(kv.getKeyPattern());
							}
						}
					}
					for (BatchClassDynamicPluginConfig dyPluginConfig : dynamicPluginConfigs) {
						if (LOG.isDebugEnabled() && dyPluginConfig != null) {
							LOG.debug(dyPluginConfig.getName());
						}
					}
				}
				for (BatchClassModuleConfig bcmc : batchClassModuleConfig) {
					if (LOG.isDebugEnabled() && bcmc != null) {
						LOG.debug(bcmc.getBatchClassModule().getModule().getName());
					}
				}
			}
		}
		return batchClasses;
	}

	/**
	 * API to fetch the UNC folders for a batch class by name.
	 * 
	 * @param batchClassName {@link String}
	 * @return List<String>
	 */
	@Override
	public List<String> getAllAssociatedUNCFolders(String batchClassName) {
		DetachedCriteria criteria = criteria();
		criteria.setProjection(Projections.property(UNC_FOLDER));
		criteria.add(Restrictions.eq(NAME, batchClassName));
		criteria.addOrder(org.hibernate.criterion.Order.asc(IDENTIFIER));
		criteria.add(Restrictions.or(Restrictions.isNull(IS_DELETED), Restrictions.eq(IS_DELETED, false)));
		return this.find(criteria);
	}

	/**
	 * API to get Batch Class by name including deleted.
	 * 
	 * @param batchClassName {@link String}
	 * @return {@link BatchClass}
	 */
	@Override
	public BatchClass getBatchClassByNameIncludingDeleted(String batchClassName) {
		BatchClass batchClass = null;
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(NAME, batchClassName));
		List<BatchClass> batchClasses = this.find(criteria);
		if (batchClasses != null && !batchClasses.isEmpty()) {
			batchClass = batchClasses.get(0);
		}
		return batchClass;
	}

	/**
	 * API to get batch class identifier by UNC folder.
	 * 
	 * @param uncFolder {@link String}
	 * @return {@link String}
	 */
	@Override
	public String getBatchClassIdentifierByUNCfolder(String uncFolder) {
		DetachedCriteria criteria = criteria();
		criteria.setProjection(Projections.property(IDENTIFIER));
		criteria.add(Restrictions.eq(UNC_FOLDER, uncFolder));
		criteria.add(Restrictions.or(Restrictions.isNull(IS_DELETED), Restrictions.eq(IS_DELETED, false)));
		return this.findSingle(criteria);
	}

	/**
	 * This API returns list of all batch class identifiers.
	 * 
	 * @return List<String>
	 */
	@Override
	public List<String> getAllBatchClassIdentifiers() {
		DetachedCriteria criteria = criteria();
		criteria.setProjection(Projections.property(IDENTIFIER));
		criteria.add(Restrictions.or(Restrictions.isNull(IS_DELETED), Restrictions.eq(IS_DELETED, false)));
		return this.find(criteria);
	}

	/**
	 * API to get all batch classes on the basis of excluding the deleted batch class and on the basis of ascending or desending order
	 * of specified property.
	 * 
	 * @param isExcludeDeleted boolean
	 * @param isAsc boolean
	 * @param propertyName {@link String}
	 * @return List<{@link BatchClass}>
	 */
	@Override
	public List<BatchClass> getAllBatchClasses(boolean isExcludeDeleted, boolean isAsc, String propertyName) {
		DetachedCriteria criteria = criteria();
		if (isExcludeDeleted) {
			criteria.add(Restrictions.or(Restrictions.isNull(IS_DELETED), Restrictions.eq(IS_DELETED, false)));
		}
		if (propertyName != null) {
			if (isAsc) {
				criteria.addOrder(org.hibernate.criterion.Order.asc(propertyName));
			} else {
				criteria.addOrder(org.hibernate.criterion.Order.desc(propertyName));
			}
		}
		return this.find(criteria);
	}
	
	/**
	 * API to get all the UNC folders on the basis of excluding the deleted batch class.
	 * 
	 * @param isExcludeDeleted {@link Boolean}
	 * @return {@link List}<{@link String}>
	 */
	@Override
	public List<String> getAllUNCList(boolean isExcludeDeleted) {
		DetachedCriteria criteria = criteria();
		if (isExcludeDeleted) {
			criteria.add(Restrictions.or(Restrictions.isNull(IS_DELETED), Restrictions.eq(IS_DELETED, false)));
		}
		criteria.setProjection(Projections.property(UNC_FOLDER));
		return this.find(criteria);
	}
	
	/**
	 * API to fetch the system folder for the given batch class identifier.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @return {@link String} the system folder path.
	 */
	@Override
	public String getSystemFolderForBatchClassIdentifier(String batchClassIdentifier) {
		EphesoftCriteria criteria = criteria();
		criteria.add(Restrictions.eq(IDENTIFIER, batchClassIdentifier));
		criteria.setProjection(Projections.property(SYSTEM_FOLDER));
		return findSingle(criteria);

	}
	
	/**
	 * API to the batch class with respect to the given batch class id and the roles provided.
	 * 
	 * @param userRoles {@link Set<{@link String}>}
	 * @param batchClassID {@link String}
	 * @return {@link BatchClass}
	 */
	@Override
	public BatchClass getBatchClassByUserRoles(final Set<String> userRoles, String batchClassID) {
		DetachedCriteria criteria = criteria();
		List<BatchClass> batchClassList = null;
		BatchClass batchClass = null;
		if (userRoles == null) {
			batchClassList = new ArrayList<BatchClass>();
		} else {
			List<String> roleList = new ArrayList<String>();
			for (String userRole : userRoles) {
				if (null == userRole || userRole.isEmpty()) {
					continue;
				}
				roleList.add(userRole);
			}
			criteria.createAlias(ASSIGNED_GROUPS, ASSIGNED_GROUPS);
			criteria.add(Restrictions.in(ASSIGNED_GROUPS_NAME, roleList));
			criteria.add(Restrictions.eq(IDENTIFIER, batchClassID));
			criteria.add(Restrictions.or(Restrictions.isNull(IS_DELETED), Restrictions.eq(IS_DELETED, false)));
			criteria.addOrder(org.hibernate.criterion.Order.asc(BATCH_ID));
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			batchClassList = find(criteria);
			if(batchClassList != null && !batchClassList.isEmpty()){
				batchClass = batchClassList.get(0);
			}
		}

		return batchClass;
	}

}
