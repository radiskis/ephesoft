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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinFragment;
import org.springframework.stereotype.Repository;

import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.dao.hibernate.HibernateDao;
import com.ephesoft.dcma.core.hibernate.EphesoftCriteria;
import com.ephesoft.dcma.da.dao.BatchInstanceDao;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.ServerRegistry;
import com.ephesoft.dcma.da.property.BatchInstanceFilter;
import com.ephesoft.dcma.da.property.BatchInstanceProperty;
import com.ephesoft.dcma.da.property.BatchPriority;

/**
 * This class is responsible to fetch data of batch instance table from data base.
 * 
 * @author Ephesoft
 * @version 1.0
 */
@Repository
public class BatchInstanceDaoImpl extends HibernateDao<BatchInstance> implements BatchInstanceDao {

	private static final String BATCH_CLASS_PROCESS_NAME = "batchClass.processName";
	private static final String BATCH_CLASS_NAME = "batchClass.name";
	private static final String VALIDATION_USER_NAME = "validationUserName";
	private static final String REVIEW_USER_NAME = "reviewUserName";
	private static final String BATCH_NAME = "batchName";
	private static final String BATCH_CLASS_IDENTIFIER = "batchClass.identifier";
	private static final String CURRENT_USER = "currentUser";
	private static final String IS_REMOTE = "isRemote";
	private static final String PRIORITY = "priority";
	private static final String STATUS = "status";
	private static final String BATCH_CLASS = "batchClass";
	private static final String EXECUTING_SERVER = "executingServer";

	/**
	 * An api to fetch all batch instance by batch class.
	 * 
	 * @param batchClass BatchClass BatchClass
	 * @return List<BatchInstance>
	 */
	@Override
	public List<BatchInstance> getBatchInstByBatchClass(BatchClass batchClass) {
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(BATCH_CLASS, batchClass));
		return find(criteria);
	}

	@Override
	public List<BatchInstance> getBatchInstancesByBatchName(String batchName) {
		DetachedCriteria criteria = criteria();
		String batchNameLocal = batchName.replaceAll("%", "\\\\%");
		criteria.add(Restrictions.like(BATCH_NAME, "%" + batchNameLocal + "%"));
		return find(criteria);
	}

	@Override
	public BatchInstance getBatchInstancesForIdentifier(String identifier) {

		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq("identifier", identifier));
		return findSingle(criteria);

	}

	/**
	 * An api to fetch all batch instance by review user name.
	 * 
	 * @param reviewUserName String
	 * @return List<BatchInstance>
	 */
	@Override
	public List<BatchInstance> getBatchInstByReviewUserName(String reviewUserName) {
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(REVIEW_USER_NAME, reviewUserName));
		return find(criteria);
	}

	/**
	 * An api to fetch all batch instance by validation user name.
	 * 
	 * @param validationUserName String
	 * @return List<BatchInstance>
	 */
	@Override
	public List<BatchInstance> getBatchInstByValidationUserName(String validationUserName) {
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(VALIDATION_USER_NAME, validationUserName));
		return find(criteria);
	}

	/**
	 * An api to fetch all batch instance by BatchInstanceStatus.
	 * 
	 * @param batchInstanceStatus BatchInstanceStatus
	 * @return List<BatchInstance>
	 */
	@Override
	public List<BatchInstance> getBatchInstByStatus(BatchInstanceStatus batchInstanceStatus) {
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(STATUS, batchInstanceStatus));
		criteria.addOrder(org.hibernate.criterion.Order.asc(PRIORITY));
		return find(criteria);
	}

	/**
	 * An api to fetch count of the batch instance table for batch instance status.
	 * 
	 * @param filterClauseList List<BatchInstanceFilter>
	 * @return count of the batch instance present for the batch instance status.
	 */
	@Override
	public int getCount(BatchInstanceStatus batchInstanceStatus, String userName, Set<String> batchClassIdentifier) {
		int count = 0;
		if (null != batchClassIdentifier && batchClassIdentifier.size() > 0) {
			DetachedCriteria criteria = criteria();
			criteria.add(Restrictions.eq(STATUS, batchInstanceStatus));
			criteria.add(Restrictions.eq(IS_REMOTE, false));
			criteria.add(Restrictions.or(Restrictions.isNull(CURRENT_USER), Restrictions.eq(CURRENT_USER, userName)));
			criteria.createAlias(BATCH_CLASS, BATCH_CLASS);
			criteria.add(Restrictions.in(BATCH_CLASS_IDENTIFIER, batchClassIdentifier));
			count = count(criteria);
		}
		return count;

	}

	/**
	 * An api to fetch all the batch instances by status list. Parameter firstResult set a limit upon the number of objects to be
	 * retrieved. Parameter maxResults set the first result to be retrieved. Parameter orderList set the sort property and order of
	 * that property. If orderList parameter is null or empty then this parameter is avoided.
	 * 
	 * @param statusList List<BatchInstanceStatus> status list of batch instance status.
	 * @param firstResult the first result to retrieve, numbered from <tt>0</tt>
	 * @param maxResults maxResults the maximum number of results
	 * @param orderList List<Order> orderList set the sort property and order of that property. If orderList parameter is null or empty
	 *            then this parameter is avoided.
	 * @param filterClauseList List<BatchInstanceFilter> this will add the where clause to the criteria query based on the property
	 *            name and value. If filterClauseList parameter is null or empty then this parameter is avoided.
	 * @param batchPriorities List<BatchPriority> this will add the where clause to the criteria query based on the priority list
	 *            selected. If batchPriorities parameter is null or empty then this parameter is avoided.
	 *@param userName Current user name.
	 *@param currentUserRoles
	 * @return List<BatchInstance> return the batch instance list.
	 */
	@Override
	public List<BatchInstance> getBatchInstances(List<BatchInstanceStatus> statusList, final int firstResult, final int maxResults,
			final List<Order> orderList, final List<BatchInstanceFilter> filterClauseList, final List<BatchPriority> batchPriorities,
			String userName, final Set<String> batchClassIdentifier) {
		EphesoftCriteria criteria = criteria();

		if (null != statusList) {
			criteria.add(Restrictions.in(STATUS, statusList));
			//criteria.add(Restrictions.or(Restrictions.isNull(CURRENT_USER), Restrictions.eq(CURRENT_USER, userName)));
		}

		if (null != batchPriorities && !(batchPriorities.isEmpty())) {
			Disjunction disjunction = Restrictions.disjunction();
			for (BatchPriority batchPriority : batchPriorities) {
				if (null != batchPriority) {
					Integer lowValue = batchPriority.getLowerLimit();
					Integer upperValue = batchPriority.getUpperLimit();
					disjunction.add(Restrictions.between(PRIORITY, lowValue, upperValue));
				} else {
					disjunction = Restrictions.disjunction();
					break;
				}
			}
			criteria.add(disjunction);

		}

		List<BatchInstance> batchInstaceList = new ArrayList<BatchInstance>();
		if (null != batchClassIdentifier && batchClassIdentifier.size() > 0) {

			BatchInstanceFilter[] filters = null;
			if (filterClauseList != null) {
				filters = filterClauseList.toArray(new BatchInstanceFilter[filterClauseList.size()]);
			}
			Order[] orders = null;
			if (orderList != null) {
				orders = orderList.toArray(new Order[orderList.size()]);
			}

			criteria.createAlias(BATCH_CLASS, BATCH_CLASS);
			criteria.add(Restrictions.in(BATCH_CLASS_IDENTIFIER, batchClassIdentifier));
			batchInstaceList = find(criteria, firstResult, maxResults, filters, orders);
		}
		return batchInstaceList;
	}

	/**
	 * An api to fetch all the batch instances by status list. Parameter firstResult set a limit upon the number of objects to be
	 * retrieved. Parameter maxResults set the first result to be retrieved. Parameter orderList set the sort property and order of
	 * that property. If orderList parameter is null or empty then this parameter is avoided.
	 * 
	 * @param statusList List<BatchInstanceStatus> status list of batch instance status.
	 * @param firstResult the first result to retrieve, numbered from <tt>0</tt>
	 * @param maxResults maxResults the maximum number of results
	 * @param orderList List<Order> orderList set the sort property and order of that property. If orderList parameter is null or empty
	 *            then this parameter is avoided.
	 * @param filterClauseList List<BatchInstanceFilter> this will add the where clause to the criteria query based on the property
	 *            name and value. If filterClauseList parameter is null or empty then this parameter is avoided.
	 * @param batchPriorities List<BatchPriority> this will add the where clause to the criteria query based on the priority list
	 *            selected. If batchPriorities parameter is null or empty then this parameter is avoided.
	 *@param userName Current user name.
	 *@param currentUserRoles
	 * @return List<BatchInstance> return the batch instance list.
	 */
	@Override
	public List<BatchInstance> getBatchInstancesExcludedRemoteBatch(List<BatchInstanceStatus> statusList, final int firstResult,
			final int maxResults, final List<Order> orderList, final List<BatchInstanceFilter> filterClauseList,
			final List<BatchPriority> batchPriorities, String userName, final Set<String> batchClassIdentifier) {
		EphesoftCriteria criteria = criteria();

		if (null != statusList) {
			criteria.add(Restrictions.in(STATUS, statusList));
			criteria.add(Restrictions.or(Restrictions.isNull(CURRENT_USER), Restrictions.eq(CURRENT_USER, userName)));
			criteria.add(Restrictions.eq(IS_REMOTE, false));
		}

		if (null != batchPriorities && !(batchPriorities.isEmpty())) {
			Disjunction disjunction = Restrictions.disjunction();
			for (BatchPriority batchPriority : batchPriorities) {
				if (null != batchPriority) {
					Integer lowValue = batchPriority.getLowerLimit();
					Integer upperValue = batchPriority.getUpperLimit();
					disjunction.add(Restrictions.between(PRIORITY, lowValue, upperValue));
				} else {
					disjunction = Restrictions.disjunction();
					break;
				}
			}
			criteria.add(disjunction);

		}
		List<BatchInstance> batchInstanceList = new ArrayList<BatchInstance>();

		if (null != batchClassIdentifier && batchClassIdentifier.size() > 0) {
			BatchInstanceFilter[] filters = null;
			if (filterClauseList != null) {
				filters = filterClauseList.toArray(new BatchInstanceFilter[filterClauseList.size()]);
			}
			Order[] orders = null;
			if (orderList != null) {
				orders = orderList.toArray(new Order[orderList.size()]);
			}

			criteria.createAlias(BATCH_CLASS, BATCH_CLASS);
			criteria.add(Restrictions.in(BATCH_CLASS_IDENTIFIER, batchClassIdentifier));
			batchInstanceList = find(criteria, firstResult, maxResults, filters, orders);
		}
		return batchInstanceList;
	}

	/**
	 * An api to fetch all the batch instance for status list input.
	 * 
	 * @param statusList List<BatchInstanceStatus>
	 * @param firstResult int
	 * @param maxResults int
	 * @return List<BatchInstance>
	 */
	@Override
	public List<BatchInstance> getBatchInstances(List<BatchInstanceStatus> statusList, final int firstResult, final int maxResults,
			final Set<String> batchClassIdentifier) {

		List<BatchInstance> batchInstances = new ArrayList<BatchInstance>();
		if (null != batchClassIdentifier && batchClassIdentifier.size() > 0) {
			EphesoftCriteria criteria = criteria();
			DetachedCriteria subQuery = criteria();
			subQuery.add(Restrictions.in(STATUS, statusList));
			subQuery.setProjection(Projections.min(PRIORITY));
			subQuery.add(Restrictions.isNull(CURRENT_USER));
			criteria.add(Subqueries.propertyIn(PRIORITY, subQuery));
			criteria.add(Restrictions.in(STATUS, statusList));
			criteria.add(Restrictions.isNull(CURRENT_USER));
			criteria.add(Restrictions.eq(IS_REMOTE, false));
			criteria.createAlias(BATCH_CLASS, BATCH_CLASS);
			criteria.add(Restrictions.in(BATCH_CLASS_IDENTIFIER, batchClassIdentifier));
			List<Order> orderList = new ArrayList<Order>();
			Order order = new Order(BatchInstanceProperty.LASTMODIFIED, true);
			orderList.add(order);

			batchInstances = find(criteria, firstResult, maxResults, orderList.toArray(new Order[orderList.size()]));
		}
		return batchInstances;
	}

	/**
	 * An api to fetch count of the batch instance table for batch instance filters.
	 * 
	 * @param filterClauseList List<BatchInstanceFilter>
	 * @return count of the batch instance present for the batch instance filters.
	 */
	@Override
	public int getCount(final List<BatchInstanceFilter> filterClauseList) {
		int count = -1;
		DetachedCriteria criteria = criteria();
		if (null != filterClauseList) {
			for (BatchInstanceFilter batchInstanceFilter : filterClauseList) {
				criteria.add(Restrictions.eq(batchInstanceFilter.getNameProperty().getProperty(), batchInstanceFilter
						.getValueProperty()));
			}
			count = count(criteria);
		}
		return count;
	}

	/**
	 * An api to fetch count of the batch instance table for batch instance status list and batch priority list.
	 * 
	 * @param batchInstStatusList List<BatchInstanceStatus>
	 * @return count of the batch instance present for the batch instance status list and batch priority list.
	 */
	@Override
	public int getCount(final List<BatchInstanceStatus> batchInstStatusList, final List<BatchPriority> batchPriorities,
			final Set<String> currentRole) {
		return getCount(batchInstStatusList, batchPriorities, false, currentRole);
	}

	@Override
	public int getAllCount(final Set<String> currentRole) {
		return getCount(null, null, true, currentRole);
	}

	@Override
	public int getCount(final List<BatchInstanceStatus> batchInstStatusList, final List<BatchPriority> batchPriorities,
			final boolean isAllCurrUsrReq, final Set<String> batchClassIdentifier) {
		DetachedCriteria criteria = criteria();

		if (null != batchInstStatusList) {
			criteria.add(Restrictions.in(STATUS, batchInstStatusList));
		}

		if (null != batchPriorities && !(batchPriorities.isEmpty())) {
			Disjunction disjunction = Restrictions.disjunction();
			for (BatchPriority batchPriority : batchPriorities) {
				if (null != batchPriority) {
					Integer lowValue = batchPriority.getLowerLimit();
					Integer upperValue = batchPriority.getUpperLimit();
					disjunction.add(Restrictions.between(PRIORITY, lowValue, upperValue));
				} else {
					disjunction = Restrictions.disjunction();
					break;
				}
			}
			criteria.add(disjunction);
		}
		int count = 0;
		if (null != batchClassIdentifier && batchClassIdentifier.size() > 0) {
			criteria.createAlias(BATCH_CLASS, BATCH_CLASS);
			criteria.add(Restrictions.in(BATCH_CLASS_IDENTIFIER, batchClassIdentifier));
			// Add check for null current users only.
			// Now we will count only for those current users those are null.
			if (!isAllCurrUsrReq) {
				criteria.add(Restrictions.isNull(CURRENT_USER));
			}
			count = count(criteria);
		}
		return count;
	}

	/**
	 * An api to fetch all the batch instance by BatchPriority.
	 * 
	 * @param batchPriority BatchPriority this will add the where clause to the criteria query based on the priority column.
	 * @return List<BatchInstance> return the batch instance list.
	 */
	@Override
	public List<BatchInstance> getBatchInstance(BatchPriority batchPriority) {
		DetachedCriteria criteria = criteria();
		Integer lowValue = batchPriority.getLowerLimit();
		Integer upperValue = batchPriority.getUpperLimit();
		criteria.add(Restrictions.between(PRIORITY, lowValue, upperValue));
		return find(criteria);
	}

	/**
	 * An api to fetch all batch instance by batch Class Name.
	 * 
	 * @param batchClassName String
	 * @return List<BatchInstance>
	 */
	@Override
	public List<BatchInstance> getBatchInstByBatchClassName(String batchClassName) {
		DetachedCriteria criteria = criteria();
		criteria.createAlias(BATCH_CLASS, BATCH_CLASS, JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq(BATCH_CLASS_NAME, batchClassName));
		return find(criteria);
	}

	/**
	 * An api to fetch all batch instance by batch Class Process Name.
	 * 
	 * @param batchClassProcessName String
	 * @return List<BatchInstance>
	 */
	@Override
	public List<BatchInstance> getBatchInstByBatchClassProcessName(String batchClassProcessName) {
		DetachedCriteria criteria = criteria();
		criteria.createAlias(BATCH_CLASS, BATCH_CLASS, JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq(BATCH_CLASS_PROCESS_NAME, batchClassProcessName));
		return find(criteria);
	}

	/**
	 * This method will create the batch instance for input batch class, unc sub folder path and local folder path.
	 * 
	 * @param batchClass BatchClass
	 * @param uncSubFolder String
	 * @param localFolder String
	 * @param priority int
	 * @return BatchInstance
	 */
	@Override
	public BatchInstance createBatchInstance(BatchClass batchClass, String uncSubfolder, String localFolder, int priority) {
		BatchInstance batchInstance = new BatchInstance();

		batchInstance.setBatchClass(batchClass);
		batchInstance.setUncSubfolder(uncSubfolder);
		batchInstance.setLocalFolder(localFolder);
		batchInstance.setPriority(priority);
		batchInstance.setStatus(BatchInstanceStatus.NEW);

		String batchName = null;
		if (null != uncSubfolder) {
			int index = uncSubfolder.lastIndexOf('\\');
			if (index != -1) {
				batchName = uncSubfolder.substring(index + 1);
			} else {
				index = uncSubfolder.lastIndexOf('/');
				if (index != -1) {
					batchName = uncSubfolder.substring(index + 1);
				}
			}
		}

		batchInstance.setBatchName(batchName);

		this.create(batchInstance);
		return batchInstance;
	}

	/**
	 * This method will update the status for batch instance.
	 * 
	 * @param batchInstance BatchInstance
	 * @param status BatchInstanceStatus
	 */
	@Override
	public void updateBatchInstanceStatus(BatchInstance batchInstance, BatchInstanceStatus status) {
		batchInstance.setStatus(status);
		this.saveOrUpdate(batchInstance);
	}

	/**
	 * This method will create a new batch instance.
	 * 
	 * @param batchInstance BatchInstance
	 */
	@Override
	public void createBatchInstance(BatchInstance batchInstance) {
		create(batchInstance);
	}

	/**
	 * This method will update the existing batch instance.
	 * 
	 * @param batchInstance BatchInstance
	 */
	@Override
	public void updateBatchInstance(BatchInstance batchInstance) {
		saveOrUpdate(batchInstance);
	}

	/**
	 * This method will remove the existing batch instance.
	 * 
	 * @param batchInstance BatchInstance
	 */
	@Override
	public void removeBatchInstance(BatchInstance batchInstance) {
		remove(batchInstance);
	}

	/**
	 * An api to fetch all batch instance by batch Class Process and batch instance id's list.
	 * 
	 * @param batchClassName String
	 * @return List<BatchInstance>
	 */
	@Override
	public List<BatchInstance> getBatchInstanceList(String batchClassName, List<String> batchInstanceIDList) {
		DetachedCriteria criteria = criteria();
		criteria.createAlias(BATCH_CLASS, BATCH_CLASS, JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq(BATCH_CLASS_NAME, batchClassName));
		criteria.add(Restrictions.in("identifier", batchInstanceIDList));
		return find(criteria);
	}

	@Override
	public List<BatchInstance> getAllBatchInstancesForCurrentUser(String currentUser) {
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(CURRENT_USER, currentUser));
		return find(criteria);
	}

	@Override
	public BatchInstance getBatch(long batchId, LockMode lockMode) {
		return get(batchId, lockMode);
	}

	@Override
	public List<BatchInstance> getRunningBatchInstancesFor(ServerRegistry lockOwner) {
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.ne(STATUS, BatchInstanceStatus.NEW));
		criteria.add(Restrictions.ne(STATUS, BatchInstanceStatus.ERROR));
		criteria.add(Restrictions.ne(STATUS, BatchInstanceStatus.FINISHED));
		criteria.add(Restrictions.ne(STATUS, BatchInstanceStatus.READY_FOR_REVIEW));
		criteria.add(Restrictions.ne(STATUS, BatchInstanceStatus.READY_FOR_VALIDATION));
		criteria.add(Restrictions.eq("lockOwner", lockOwner));
		return find(criteria);
	}

	@Override
	public List<BatchInstance> getAllBatchInstances(List<Order> orders) {
		EphesoftCriteria criteria = criteria();
		return find(criteria, orders.toArray(new Order[orders.size()]));
	}

	@Override
	public List<BatchInstance> getAllUnFinishedBatchInstances(String uncFolder) {
		DetachedCriteria criteria = criteria();
		criteria.createAlias(BATCH_CLASS, BATCH_CLASS, JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq("batchClass.uncFolder", uncFolder));

		List<BatchInstanceStatus> statusList = new ArrayList<BatchInstanceStatus>();
		statusList.add(BatchInstanceStatus.NEW);
		statusList.add(BatchInstanceStatus.ERROR);
		statusList.add(BatchInstanceStatus.READY_FOR_REVIEW);
		statusList.add(BatchInstanceStatus.READY_FOR_VALIDATION);
		statusList.add(BatchInstanceStatus.RUNNING);
		statusList.add(BatchInstanceStatus.READY);
		statusList.add(BatchInstanceStatus.RESTART_IN_PROGRESS);
		criteria.add(Restrictions.in(STATUS, statusList));

		return find(criteria);
	}

	@Override
	public void updateBatchInstanceLocalFolder(BatchInstance batchInsctance, String localFolder) {
		batchInsctance.setLocalFolder(localFolder);
		saveOrUpdate(batchInsctance);
	}

	@Override
	public void updateBatchInstanceUncFolder(BatchInstance batchInsctance, String uncFolder) {
		batchInsctance.setUncSubfolder(uncFolder);
		saveOrUpdate(batchInsctance);
	}

	/**
	 * An API return all unfinished remotely executing batch instances.
	 */
	@Override
	public List<BatchInstance> getAllUnfinshedRemotelyExecutedBatchInstance() {
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(IS_REMOTE, true));
		List<BatchInstanceStatus> statusList = new ArrayList<BatchInstanceStatus>();
		statusList.add(BatchInstanceStatus.READY_FOR_REVIEW);
		statusList.add(BatchInstanceStatus.READY_FOR_VALIDATION);
		statusList.add(BatchInstanceStatus.RUNNING);
		statusList.add(BatchInstanceStatus.TRANSFERRED);
		statusList.add(BatchInstanceStatus.READY);

		criteria.add(Restrictions.in(STATUS, statusList));
		return find(criteria);

	}

	/**
	 * An api to fetch all the batch instances only remotely executing batches by status list. Parameter firstResult set a limit upon
	 * the number of objects to be retrieved. Parameter maxResults set the first result to be retrieved. Parameter orderList set the
	 * sort property and order of that property. If orderList parameter is null or empty then this parameter is avoided.
	 * 
	 * @param statusList List<BatchInstanceStatus> status list of batch instance status.
	 * @param firstResult the first result to retrieve, numbered from <tt>0</tt>
	 * @param maxResults maxResults the maximum number of results
	 * @param orderList List<Order> orderList set the sort property and order of that property. If orderList parameter is null or empty
	 *            then this parameter is avoided.
	 * @param filterClauseList List<BatchInstanceFilter> this will add the where clause to the criteria query based on the property
	 *            name and value. If filterClauseList parameter is null or empty then this parameter is avoided.
	 * @param batchPriorities List<BatchPriority> this will add the where clause to the criteria query based on the priority list
	 *            selected. If batchPriorities parameter is null or empty then this parameter is avoided.
	 *@param userName Current user name.
	 *@param currentUserRoles
	 * @return List<BatchInstance> return the batch instance list.
	 */
	@Override
	public List<BatchInstance> getRemoteBatchInstances(List<BatchInstanceStatus> statusList, final int firstResult,
			final int maxResults, final List<Order> orderList, final List<BatchInstanceFilter> filterClauseList,
			final List<BatchPriority> batchPriorities, String userName, final Set<String> batchClassIdentifier) {
		EphesoftCriteria criteria = criteria();

		if (null != statusList) {
			criteria.add(Restrictions.in(STATUS, statusList));
			criteria.add(Restrictions.or(Restrictions.isNull(CURRENT_USER), Restrictions.eq(CURRENT_USER, userName)));
			criteria.add(Restrictions.eq(IS_REMOTE, true));
		}

		if (null != batchPriorities && !(batchPriorities.isEmpty())) {
			Disjunction disjunction = Restrictions.disjunction();
			for (BatchPriority batchPriority : batchPriorities) {
				if (null != batchPriority) {
					Integer lowValue = batchPriority.getLowerLimit();
					Integer upperValue = batchPriority.getUpperLimit();
					disjunction.add(Restrictions.between(PRIORITY, lowValue, upperValue));
				} else {
					disjunction = Restrictions.disjunction();
					break;
				}
			}
			criteria.add(disjunction);

		}

		List<BatchInstance> batchInstances = new ArrayList<BatchInstance>();

		if (null != batchClassIdentifier && batchClassIdentifier.size() > 0) {
			BatchInstanceFilter[] filters = null;
			if (filterClauseList != null) {
				filters = filterClauseList.toArray(new BatchInstanceFilter[filterClauseList.size()]);
			}
			Order[] orders = null;
			if (orderList != null) {
				orders = orderList.toArray(new Order[orderList.size()]);
			}

			criteria.createAlias(BATCH_CLASS, BATCH_CLASS);
			criteria.add(Restrictions.in(BATCH_CLASS_IDENTIFIER, batchClassIdentifier));
			batchInstances = find(criteria, firstResult, maxResults, filters, orders);
		}

		return batchInstances;
	}

	@Override
	public List<BatchInstance> getExecutingJobByServerIP(String serverIP) {
		EphesoftCriteria criteria = criteria();
		if (null != serverIP) {
			criteria.add(Restrictions.eq(EXECUTING_SERVER, serverIP));
			criteria.add(Restrictions.eq(STATUS, BatchInstanceStatus.RUNNING));
		}
		return find(criteria);
	}
}
