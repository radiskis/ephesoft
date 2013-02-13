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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.LockMode;
import org.hibernate.exception.LockAcquisitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.EphesoftUser;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.exception.BatchAlreadyLockedException;
import com.ephesoft.dcma.da.constant.DataAccessConstant;
import com.ephesoft.dcma.da.dao.BatchInstanceDao;
import com.ephesoft.dcma.da.dao.BatchInstanceGroupsDao;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassGroups;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.ServerRegistry;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.property.BatchInstanceFilter;
import com.ephesoft.dcma.da.property.BatchPriority;

/**
 * This is a database service to read data required by Batch Instance Service.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.da.service.BatchInstanceService
 */
public class BatchInstanceServiceImpl implements BatchInstanceService {
	
	/**
	 * LOGGER to print the LOGGERging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchInstanceServiceImpl.class);

	/**
	 * Reference of BatchInstanceDao.
	 */
	@Autowired
	private BatchInstanceDao batchInstanceDao;

	/**
	 * Reference of BatchInstanceDao.
	 */
	@Autowired
	private BatchInstanceGroupsDao batchInstanceGrpDao;

	/**
	 * object Object.
	 */
	private final Object object = new Object();

	/**
	 * An API to fetch all batch instance by batch class.
	 * 
	 * @param batchClass {@link BatchClass}
	 * @return List<BatchInstance>
	 */
	@Transactional(readOnly = true)
	@Override
	public List<BatchInstance> getBatchInstByBatchClass(BatchClass batchClass) {
		List<BatchInstance> batchInstance = null;
		if (null == batchClass) {
			LOGGER.info("batchClass is null.");
		} else {
			batchInstance = batchInstanceDao.getBatchInstByBatchClass(batchClass);
		}
		return batchInstance;
	}

	/**
	 * An API to fetch all batch instance by review user name.
	 * 
	 * @param reviewUserName String
	 * @return List<BatchInstance>
	 */
	@Transactional(readOnly = true)
	@Override
	public List<BatchInstance> getBatchInstByReviewUserName(String reviewUserName) {
		List<BatchInstance> batchInstance = null;
		if (null == reviewUserName || "".equals(reviewUserName)) {
			LOGGER.info("reviewUserName is null or empty.");
		} else {
			batchInstance = batchInstanceDao.getBatchInstByReviewUserName(reviewUserName);
		}
		return batchInstance;
	}

	/**
	 * An API to fetch all batch instance by validation user name.
	 * 
	 * @param validationUserName String
	 * @return List<BatchInstance>
	 */
	@Transactional(readOnly = true)
	@Override
	public List<BatchInstance> getBatchInstByValidationUserName(String validationUserName) {
		List<BatchInstance> batchInstance = null;
		if (null == validationUserName || "".equals(validationUserName)) {
			LOGGER.info("validationUserName is null or empty.");
		} else {
			batchInstance = batchInstanceDao.getBatchInstByValidationUserName(validationUserName);
		}
		return batchInstance;
	}

	/**
	 * An API to fetch all batch instance by BatchInstanceStatus.
	 * 
	 * @param batchInstanceStatus BatchInstanceStatus
	 * @return List<BatchInstance>
	 */
	@Transactional(readOnly = true)
	@Override
	public List<BatchInstance> getBatchInstByStatus(BatchInstanceStatus batchInstanceStatus) {
		List<BatchInstance> batchInstance = null;
		if (null == batchInstanceStatus) {
			LOGGER.info("batchInstanceStatus is null.");
		} else {
			batchInstance = batchInstanceDao.getBatchInstByStatus(batchInstanceStatus);
		}
		return batchInstance;
	}

	/**
	 * An API to fetch count of the batch instance table for batch instance status and batch priority. API will return those batch
	 * instance having access by the user roles on the basis of ephesoft user.
	 * 
	 * @param batchName {@link String}
	 * @param batchInstanceStatus {@link BatchInstanceStatus}
	 * @param userName {@link String}
	 * @param priority {@link BatchPriority}
	 * @param userRoles Set<{@link String}>
	 * @param ephesoftUser {@link EphesoftUser}
	 * @return int, count of the batch instance present for the batch instance status.
	 */
	@Transactional(readOnly = true)
	@Override
	public int getCount(String batchName, BatchInstanceStatus batchInstanceStatus, String userName, BatchPriority priority,
			Set<String> userRoles, EphesoftUser ephesoftUser) {
		int count = -1;
		if (null == batchInstanceStatus) {
			LOGGER.info("batchInstanceStatus is null.");
		} else {
			count = batchInstanceDao.getCount(batchName, batchInstanceStatus, userName, priority, userRoles, ephesoftUser);
		}
		return count;
	}

	/**
	 * An API to fetch all the batch instances by status list. Parameter firstResult set a limit upon the number of objects to be
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
	 * @param userRoles current user.
	 * @param ephesoftUser current Ephesoft-user.
	 * @param searchString the searchString on which batch instances have to be fetched
	 * @return List<BatchInstance> return the batch instance list.
	 */
	@Transactional(readOnly = true)
	@Override
	public List<BatchInstance> getBatchInstances(List<BatchInstanceStatus> statusList, final int firstResult, final int maxResults,
			final List<Order> orderList, final List<BatchInstanceFilter> filterClauseList, final List<BatchPriority> batchPriorities,
			String userName, Set<String> userRoles, EphesoftUser ephesoftUser, String searchString) {
		List<BatchInstance> batchInstance = null;
		if (null == statusList || statusList.isEmpty()) {
			LOGGER.info("batchInstanceStatus is null or empty.");
		} else {
			batchInstance = batchInstanceDao.getBatchInstances(statusList, firstResult, maxResults, orderList, filterClauseList,
					batchPriorities, userName, userRoles, ephesoftUser, searchString);
		}
		return batchInstance;
	}

	/**
	 * An API to fetch all the batch instances on the basis of BatchInstanceFilters list.
	 * 
	 * @param batchInstanceFilters List<BatchInstanceFilter> this will add the where clause to the criteria query based on the property
	 *            name and value.
	 * @return List<BatchInstance>
	 */
	@Transactional(readOnly = true)
	@Override
	public List<BatchInstance> getBatchInstances(final List<BatchInstanceFilter> batchInstanceFilters) {
		List<BatchInstance> batchInstance = null;
		if (null == batchInstanceFilters || batchInstanceFilters.isEmpty()) {
			LOGGER.info("batchInstanceFilters is null or empty.");
		} else {
			batchInstance = batchInstanceDao.getBatchInstances(null, -1, -1, null, batchInstanceFilters, null, null, null, null, null);
		}
		return batchInstance;
	}

	/**
	 * This API fetches over all the batch instances on the basis of status, priority and for the given user role.
	 * 
	 * @param statusList List<{@link BatchInstanceStatus}>
	 * @param batchPriorities List<{@link BatchPriority}>
	 * @param userRoles Set<{@link String}>
	 * @return List<BatchInstance>
	 */
	@Transactional(readOnly = true)
	@Override
	public List<BatchInstance> getBatchInstancesForStatusPriority(final List<BatchInstanceStatus> statusList,
			final List<BatchPriority> batchPriorities, Set<String> userRoles) {
		return batchInstanceDao.getBatchInstancesForStatusPriority(statusList, batchPriorities, userRoles);
	}

	/**
	 * This API fetches the roles for a batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @return Set<String>
	 */
	@Override
	public Set<String> getRolesForBatchInstance(BatchInstance batchInstance) {
		Set<String> roles = new HashSet<String>();
		// add all the groups of the batch class
		List<BatchClassGroups> bcGrps = batchInstance.getBatchClass().getAssignedGroups();
		for (BatchClassGroups grp : bcGrps) {
			roles.add(grp.getGroupName());
		}

		// add all the groups of the batch instance
		roles.addAll(batchInstanceGrpDao.getRolesForBatchInstance(batchInstance.getIdentifier()));
		return roles;
	}

	/**
	 * An API to fetch next batch instance from batch instance table for status READY_FOR_REVIEW and READY_FOR_VALIDATION. This will
	 * have minimum priority with above restrictions and result of the query will be order by last modified. API will return those
	 * batch instance having the access by the user role on the basis of ephesoft user.
	 * 
	 * @param userRoles Set<{@link String}>
	 * @param ephesoftUser {@link EphesoftUser}
	 * @return {@link BatchInstance} batch instance for status READY_FOR_REVIEW and READY_FOR_VALIDATION and result of the query will
	 *         be order by last modified.
	 */
	@Transactional(readOnly = true)
	@Override
	public BatchInstance getHighestPriorityBatchInstance(final Set<String> userRoles, EphesoftUser ephesoftUser) {
		BatchInstance batchInstance = null;
		int firstResult = 0;
		int maxResults = 1;
		List<BatchInstanceStatus> statusList = new ArrayList<BatchInstanceStatus>();
		statusList.add(BatchInstanceStatus.READY_FOR_REVIEW);
		statusList.add(BatchInstanceStatus.READY_FOR_VALIDATION);

		List<BatchInstance> batchInstanceList = batchInstanceDao.getBatchInstances(statusList, firstResult, maxResults, userRoles,
				ephesoftUser);

		if (null != batchInstanceList && !batchInstanceList.isEmpty()) {
			batchInstance = batchInstanceList.get(0);
		}

		return batchInstance;
	}

	/**
	 * An API to fetch count of the batch instance table for batch instance filters.
	 * 
	 * @param filterClauseList List<BatchInstanceFilter>
	 * @return count of the batch instance present for the batch instance filters.
	 */
	@Transactional(readOnly = true)
	@Override
	public int getCount(final List<BatchInstanceFilter> filterClauseList) {
		int count = -1;
		if (null == filterClauseList) {
			LOGGER.info("filterClauseList is null.");
		} else {
			count = batchInstanceDao.getCount(filterClauseList);
		}
		return count;
	}

	/**
	 * An API to fetch count of the batch instances for a given status list and batch priority and isCurrUsrNotReq is used for adding
	 * the batch instance access by the current user. This API will return the batch instance having access by the user roles on the
	 * basis of ephesoft user.
	 * 
	 * @param batchInstStatusList List<{@link BatchInstanceStatus}>
	 * @param batchPriorities the priority list of the batches
	 * @param isCurrUsrNotReq true if the current user can be anyone. False if current user cannot be null.
	 * @param currentUser {@link String}
	 * @param userRoles Set<{@link String}>
	 * @param ephesoftUser {@link EphesoftUser}
	 * @param searchString the searchString on which batch instances have to be fetched
	 * @return int, the count satisfying the above requirements
	 */
	@Transactional(readOnly = true)
	@Override
	public int getCount(final List<BatchInstanceStatus> batchInstStatusList, final List<BatchPriority> batchPriorities,
			final boolean isCurrUsrNotReq, final String currentUser, final Set<String> userRoles, EphesoftUser ephesoftUser,
			final String searhString) {
		return batchInstanceDao.getCount(batchInstStatusList, batchPriorities, isCurrUsrNotReq, userRoles, currentUser, ephesoftUser,
				searhString);
	}

	/**
	 * An API to return total count of batches from the batch instance table having access by the user roles on the basis of ephesoft
	 * user.
	 * 
	 * @param currentUser {@link String}
	 * @param userRoles Set<{@link String}>
	 * @param ephesoftUser {@link EphesoftUser}
	 * @return int, total count
	 */
	@Transactional(readOnly = true)
	@Override
	public int getAllCount(final String currentUser, final Set<String> userRoles, EphesoftUser ephesoftUser) {
		return batchInstanceDao.getAllCount(currentUser, userRoles, ephesoftUser);
	}

	/**
	 * An API to fetch count of the batch instance table for batch instance status list, batch priority list on the basis of the user
	 * roles. API will return the count for the batch instance having access by the user roles and current user name on the basis of
	 * the ephesoft user.
	 * 
	 * @param batchInstStatusList List<{@link BatchInstanceStatus}>
	 * @param batchPriorities List<{@link BatchPriority}>
	 * @param userRoles Set<{@link String}>
	 * @param currentUserName {@link String} current logged in user name.
	 * @param ephesoftUser Enum for ephesoft user.
	 * @return int,count of the batch instance present for the batch instance status list and batch priority list.
	 */
	@Transactional(readOnly = true)
	@Override
	public int getCount(final List<BatchInstanceStatus> batchInstStatusList, final List<BatchPriority> batchPriorities,
			final Set<String> userRoles, final String currentUserName, EphesoftUser ephesoftUser) {
		int count = -1;
		if (null == batchInstStatusList || batchInstStatusList.isEmpty()) {
			LOGGER.info("filterClauseList is null or empty.");
		} else {
			count = batchInstanceDao.getCount(batchInstStatusList, batchPriorities, userRoles, currentUserName, ephesoftUser);
		}
		return count;
	}

	/**
	 * An API to fetch all the batch instance by BatchPriority.
	 * 
	 * @param batchPriority BatchPriority this will add the where clause to the criteria query based on the priority column.
	 * @return List<BatchInstance> return the batch instance list.
	 */
	@Transactional(readOnly = true)
	@Override
	public List<BatchInstance> getBatchInstance(BatchPriority batchPriority) {
		List<BatchInstance> batchInstance = null;
		if (null == batchPriority) {
			LOGGER.info("batchPriority is null.");
		} else {
			batchInstance = batchInstanceDao.getBatchInstance(batchPriority);
		}
		return batchInstance;
	}

	/**
	 * An API to fetch all batch instance by batch Class Name.
	 * 
	 * @param batchClassName String
	 * @return List<BatchInstance>
	 */
	@Transactional(readOnly = true)
	@Override
	public List<BatchInstance> getBatchInstByBatchClassName(String batchClassName) {
		List<BatchInstance> batchInstance = null;
		if (null == batchClassName || "".equals(batchClassName)) {
			LOGGER.info("batchClassName is null or empty.");
		} else {
			batchInstance = batchInstanceDao.getBatchInstByBatchClassName(batchClassName);
		}
		return batchInstance;
	}

	/**
	 * An API to fetch all batch instance by batch Class Process Name.
	 * 
	 * @param batchClassProcessName String
	 * @return List<BatchInstance>
	 */
	@Transactional(readOnly = true)
	@Override
	public List<BatchInstance> getBatchInstByBatchClassProcessName(String batchClassProcessName) {
		List<BatchInstance> batchInstance = null;
		if (null == batchClassProcessName || DataAccessConstant.EMPTY.equals(batchClassProcessName)) {
			LOGGER.info("batchClassProcessName is null or empty.");
		} else {
			batchInstance = batchInstanceDao.getBatchInstByBatchClassProcessName(batchClassProcessName);
		}
		return batchInstance;
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
	@Transactional
	@Override
	public BatchInstance createBatchInstance(BatchClass batchClass, String uncSubFolder, String localFolder, int priority) {
		BatchInstance batchInstance = null;
		if (null == batchClass || null == uncSubFolder || null == localFolder) {
			LOGGER.info("batchClass or uncSubFolder or localFolder is null.");
		} else {
			batchInstance = this.batchInstanceDao.createBatchInstance(batchClass, uncSubFolder, localFolder, priority);
		}
		return batchInstance;
	}

	/**
	 * This method will update the status for batch instance.
	 * 
	 * @param batchInstance BatchInstance
	 * @param status BatchInstanceStatus
	 */
	@Transactional
	@Override
	public void updateBatchInstanceStatus(BatchInstance batchInstance, BatchInstanceStatus status) {
		if (null == batchInstance || null == status) {
			LOGGER.info("batchInstance or status is null.");
		} else {
			this.batchInstanceDao.updateBatchInstanceStatus(batchInstance, status);
			LOGGER.info("Batch Instance status: " + status);
		}
	}

	/**
	 * This method will update the status of batch instance for input id.
	 * 
	 * @param identifier String
	 * @param status BatchInstanceStatus
	 */
	@Transactional
	@Override
	public void updateBatchInstanceStatusByIdentifier(String identifier, BatchInstanceStatus status) {
		if (null == identifier || null == status) {
			LOGGER.info("batchInstance id or status is null.");
		} else {
			BatchInstance batchInstance = batchInstanceDao.getBatchInstancesForIdentifier(identifier);
			String batchStatus = batchInstance.getStatus().name();
			if (!(batchStatus.equals(BatchInstanceStatus.DELETED.name()) || batchStatus.equals(BatchInstanceStatus.RESTARTED.name()))) {
				this.updateBatchInstanceStatus(batchInstance, status);
			} else {
				LOGGER.error("Cannot update batch instance with identifier " + identifier + " as batch status is "
						+ batchInstance.getStatus().name());
			}
		}
	}

	/**
	 * This method will create a new batch instance.
	 * 
	 * @param batchInstance BatchInstance
	 */
	@Transactional
	@Override
	public void createBatchInstance(BatchInstance batchInstance) {
		if (null == batchInstance) {
			LOGGER.info("batchInstance is null.");
		} else {
			batchInstanceDao.createBatchInstance(batchInstance);
		}
	}

	/**
	 * This method will update the existing batch instance.
	 * 
	 * @param batchInstance BatchInstance
	 */
	@Transactional
	@Override
	public void updateBatchInstance(BatchInstance batchInstance) {
		if (null == batchInstance) {
			LOGGER.info("batchInstance is null.");
		} else {
			batchInstanceDao.updateBatchInstance(batchInstance);
		}
	}

	/**
	 * This method will remove the existing batch instance.
	 * 
	 * @param batchInstance BatchInstance
	 */
	@Transactional
	@Override
	public void removeBatchInstance(BatchInstance batchInstance) {
		if (null == batchInstance) {
			LOGGER.info("batchInstance is null.");
		} else {
			batchInstanceDao.removeBatchInstance(batchInstance);
		}
	}

	/**
	 * An API to fetch all batch instance by batch Class Name and batch instance id's list.
	 * 
	 * @param batchClassName String
	 * @param batchInstanceIdentifierList List<String>
	 * @return List<BatchInstance>
	 */
	@Transactional(readOnly = true)
	@Override
	public List<BatchInstance> getBatchInstanceList(String batchClassName, List<String> batchInstanceIdentifierList) {
		List<BatchInstance> batchInstanceList = null;
		if (null == batchClassName || DataAccessConstant.EMPTY.equals(batchClassName) || batchInstanceIdentifierList == null
				|| batchInstanceIdentifierList.isEmpty()) {
			LOGGER.info("batchClassName/batchInstanceIdentifierList is null or empty.");
		} else {
			batchInstanceList = batchInstanceDao.getBatchInstanceList(batchClassName, batchInstanceIdentifierList);
		}
		return batchInstanceList;
	}

	/**
	 * An API to fetch batch class ID for batch instance ID.
	 * 
	 * @param batchInstanceIdentifier String
	 * @return String batchClassID
	 */
	@Transactional(readOnly = true)
	@Override
	public String getBatchClassIdentifier(String batchInstanceIdentifier) {
		String batchClassID = null;
		if (null == batchInstanceIdentifier) {
			LOGGER.info("batchInstanceIdentifier id is null.");
		} else {
			BatchInstance batchInstance = batchInstanceDao.getBatchInstancesForIdentifier(batchInstanceIdentifier);
			BatchClass batchClass = batchInstance.getBatchClass();
			if (null != batchClass) {
				batchClassID = batchClass.getIdentifier();
			}
		}
		return batchClassID;
	}

	/**
	 * This method will update the status of batch instance for input id and status.
	 * 
	 * @param identifier String
	 * @param status String
	 */
	@Transactional
	@Override
	public void updateBatchInstanceStatusByIdentifier(String identifier, String status) {
		if (null == identifier || null == status || DataAccessConstant.EMPTY.equals(status)) {
			LOGGER.info("id/status are null or empty.");
		} else {
			this.updateBatchInstanceStatusByIdentifier(identifier, BatchInstanceStatus.valueOf(status));
		}
	}

	/**
	 * API to update the batch instance status.
	 * 
	 * @param identifier {@link BatchInstanceID}
	 * @param status {@link BatchInstanceStatus}
	 */
	@Transactional
	@Override
	public void updateBatchInstanceStatus(BatchInstanceID identifier, BatchInstanceStatus status) {
		Assert.notNull(identifier, "Batch Instance ID can not be null");
		this.updateBatchInstanceStatusByIdentifier(identifier.getID(), status);
	}

	/**
	 * API to fetch BatchInstance on the basis of batchInstanceIdentifier.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @return {@link BatchInstance}
	 */
	@Transactional
	@Override
	public BatchInstance getBatchInstanceByIdentifier(String batchInstanceIdentifierentifier) {
		BatchInstance batchInstance = null;
		batchInstance = batchInstanceDao.getBatchInstancesForIdentifier(batchInstanceIdentifierentifier);
		return batchInstance;
	}

	/**
	 * API to get all the batch instances by matching Batch Name or batch identifier.
	 * 
	 * @param searchString {@link String}
	 * @param userRoles Set<{@link String}>
	 * @return List<{@link BatchInstance}> - List of all batch instances.
	 */
	@Transactional
	@Override
	public List<BatchInstance> getBatchInstancesByBatchNameOrId(String searchString, Set<String> userRoles) {
		List<BatchInstance> batchInstances = null;
		batchInstances = batchInstanceDao.getBatchInstancesByBatchNameOrId(searchString, userRoles);
		return batchInstances;
	}

	/**
	 * API to acquire a batch on the basis of batchInstanceIdentifier and userName.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @param currentUser {@link String}	
	 * @return {@link BatchInstance}
	 */
	@Override
	@Transactional(readOnly = false)
	public BatchInstance acquireBatchInstance(String batchInstanceIdentifier, String currentUser) throws BatchAlreadyLockedException {
		BatchInstance batchInstance = null;
		synchronized (object) {
			if (null != batchInstanceIdentifier) {
				batchInstance = getBatchInstanceByIdentifier(batchInstanceIdentifier);
				if (batchInstance.getCurrentUser() != null && !(batchInstance.getCurrentUser().equalsIgnoreCase(currentUser))) {
					throw new BatchAlreadyLockedException("Batch Instance " + batchInstance + " is already locked by "
							+ batchInstance.getCurrentUser());
				} else {
					// Updating the state of the batch to locked and saving in the
					// database.
					LOGGER.info(currentUser + " is getting lock on batch " + batchInstanceIdentifier);
					if (!currentUser.trim().isEmpty()) {
						switch (batchInstance.getStatus()) {
							case READY_FOR_REVIEW:
								batchInstance.setReviewUserName(currentUser);
								batchInstance.setCurrentUser(currentUser);
								break;
							case READY_FOR_VALIDATION:
								batchInstance.setValidationUserName(currentUser);
								batchInstance.setCurrentUser(currentUser);
								break;
							default:
								break;
						}
					}
					batchInstanceDao.updateBatchInstance(batchInstance);
				}
			} else {
				LOGGER.warn("batchInstanceIdentifier id is null. Cannot Proceed further..Returning");
			}
		}
		return batchInstance;

	}

	/**
	 * Unlocks all the batches acquired by the user.
	 * 
	 * @param currentUser {@link String}
	 */
	@Transactional
	@Override
	public void unlockAllBatchInstancesForCurrentUser(String currentUser) {
		if (currentUser != null && !currentUser.isEmpty()) {
			List<BatchInstance> batchInstanceList = batchInstanceDao.getAllBatchInstancesForCurrentUser(currentUser);
			if (batchInstanceList == null || batchInstanceList.isEmpty()) {
				LOGGER.warn("No batches exist for the username " + currentUser);
				return;
			}
			for (BatchInstance batchInstance : batchInstanceList) {
				LOGGER.info("Unlocking batches for " + currentUser);
				batchInstance.setCurrentUser(null);
				batchInstanceDao.updateBatchInstance(batchInstance);

			}
		} else {
			LOGGER.warn("Username not specified or is Null. Returning.");
		}
	}

	/**
	 * Unlocks the currently acquired batch by the user (currentUser).
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * 
	 */
	@Transactional
	@Override
	public void unlockCurrentBatchInstance(String batchInstanceIdentifier) {
		BatchInstance batchInstance = null;
		if (null == batchInstanceIdentifier) {
			LOGGER.warn("batchInstanceIdentifier id is null. Cannot Proceed further..Returning");
			return;
		}
		batchInstance = getBatchInstanceByIdentifier(batchInstanceIdentifier);
		batchInstance.setCurrentUser(null);
		batchInstanceDao.updateBatchInstance(batchInstance);
	}

	/**
	 * API to get all batch instances which are currently being executed for a specific IP Address. Output will be all batch instances
	 * which are not in NEW, ERROR or FINISHED state.
	 * 
	 * @param lockOwner {@link ServerRegistry}
	 * @return List<{@link BatchInstance}>
	 */
	@Override
	@Transactional(readOnly = true)
	public List<BatchInstance> getRunningBatchInstancesFor(ServerRegistry lockOwner) {
		return batchInstanceDao.getRunningBatchInstancesFor(lockOwner);
	}

	/**
	 * API to get a batch by applying Hibernate level optimistic locking and to set lock owner ino db.
	 * 
	 * @param identifier long
	 * @param lockOwner {@link ServerRegistry}
	 * @throws LockAcquisitionException in case of error
	 */
	@Transactional
	@Override
	public void lockBatch(long batchId, ServerRegistry lockOwner) throws LockAcquisitionException {
		BatchInstance batchInstance = batchInstanceDao.getBatch(batchId, LockMode.UPGRADE_NOWAIT);
		if (batchInstance.getLockOwner() == null) {
			batchInstance.setLockOwner(lockOwner);
			batchInstanceDao.saveOrUpdate(batchInstance);
		} else {
			throw new LockAcquisitionException(DataAccessConstant.BATCH_ALREADY_LOCKED, null);
		}
	}

	/**
	 * API to acquire lock on a batch instance.
	 * 
	 * @param batchId long
	 * @throws LockAcquisitionException in case of error
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void lockBatch(long batchId) throws LockAcquisitionException {
		BatchInstance batchInstance = batchInstanceDao.getBatch(batchId, LockMode.UPGRADE_NOWAIT);
		if (batchInstance.getStatus() == BatchInstanceStatus.NEW) {
			batchInstance.setStatus(BatchInstanceStatus.LOCKED);
			batchInstanceDao.saveOrUpdate(batchInstance);
		} else {
			throw new LockAcquisitionException(DataAccessConstant.BATCH_ALREADY_LOCKED, null);
		}
	}

	/**
	 * API to get all the batch instances in sorted order.
	 * 
	 * @param order List<{@link Order}>
	 * @return List<{@link BatchInstance}> - List of all batch instances.
	 */
	@Override
	public List<BatchInstance> getAllBatchInstances(List<Order> orders) {
		return batchInstanceDao.getAllBatchInstances(orders);
	}

	/**
	 * API return all unfinished batch instances.
	 * 
	 * @param uncFolder String
	 * @return List<BatchInstance> - List of all unfinished batch instances.
	 */
	@Override
	public List<BatchInstance> getAllUnFinishedBatchInstances(String uncFolder) {
		return batchInstanceDao.getAllUnFinishedBatchInstances(uncFolder);
	}

	/**
	 * API to merge batch instance and returning updated batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @return {@link BatchInstance}
	 */
	@Override
	@Transactional
	public BatchInstance merge(BatchInstance batchInstance) {
		return batchInstanceDao.merge(batchInstance);
	}

	/**
	 * API to get all unfinshedRemotelyExecutedBatchInstance.
	 * 
	 * @return List<BatchInstance> - List of batch instances.
	 */
	@Transactional
	@Override
	public List<BatchInstance> getAllUnfinshedRemotelyExecutedBatchInstance() {
		List<BatchInstance> batchInstances = null;
		batchInstances = batchInstanceDao.getAllUnfinshedRemotelyExecutedBatchInstance();
		return batchInstances;
	}

	/**
	 * An API to fetch all the batch instances excluding remotely executing batches by status list. Parameter firstResult set a limit
	 * upon the number of objects to be retrieved. Parameter maxResults set the first result to be retrieved. Parameter orderList set
	 * the sort property and order of that property. If orderList parameter is null or empty then this parameter is avoided. This will
	 * return only those batch instance which having access by the user roles on the basis of the ephesoft user.
	 * 
	 * @param batchNameToBeSearched {@link String}
	 * @param statusList List<{@link BatchInstanceStatus}> status list of batch instance status.
	 * @param firstResult the first result to retrieve, numbered from <tt>0</tt>
	 * @param maxResults maxResults the maximum number of results
	 * @param orderList List<{@link Order}> orderList set the sort property and order of that property. If orderList parameter is null
	 *            or empty then this parameter is avoided.
	 * @param filterClauseList List<{@link BatchInstanceFilter}> this will add the where clause to the criteria query based on the
	 *            property name and value. If filterClauseList parameter is null or empty then this parameter is avoided.
	 * @param batchPriorities List<{@link BatchPriority}> this will add the where clause to the criteria query based on the priority
	 *            list selected. If batchPriorities parameter is null or empty then this parameter is avoided.
	 * @param currentUser {@link String}
	 * @param userRoles Set<{@link String}>
	 * @param ephesoftUser {@link EphesoftUser}
	 * @return List<{@link BatchInstance}> return the batch instance list.
	 */
	@Transactional(readOnly = true)
	@Override
	public List<BatchInstance> getBatchInstancesExcludedRemoteBatch(final String batchNameToBeSearched,
			List<BatchInstanceStatus> statusList, final int firstResult, final int maxResults, final List<Order> orderList,
			final List<BatchInstanceFilter> filterClauseList, final List<BatchPriority> batchPriorities, String userName,
			Set<String> userRoles, EphesoftUser ephesoftUser) {
		List<BatchInstance> batchInstance = null;
		if (null == statusList || statusList.isEmpty()) {
			LOGGER.info("batchInstanceStatus is null or empty.");
		} else {
			batchInstance = batchInstanceDao.getBatchInstancesExcludedRemoteBatch(batchNameToBeSearched, statusList, firstResult,
					maxResults, orderList, filterClauseList, batchPriorities, userName, userRoles, ephesoftUser);
		}
		return batchInstance;
	}

	/**
	 * An API to fetch all the batch instances excluding remotely executing batches by status list. Parameter firstResult set a limit
	 * upon the number of objects to be retrieved. Parameter maxResults set the first result to be retrieved. Parameter orderList set
	 * the sort property and order of that property. If orderList parameter is null or empty then this parameter is avoided.
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
	 * @param userRoles current user.
	 * @return List<BatchInstance> return the batch instance list.
	 */
	@Transactional(readOnly = true)
	@Override
	public List<BatchInstance> getRemoteBatchInstances(List<BatchInstanceStatus> statusList, final int firstResult,
			final int maxResults, final List<Order> orderList, final List<BatchInstanceFilter> filterClauseList,
			final List<BatchPriority> batchPriorities, String userName, Set<String> userRoles) {
		List<BatchInstance> batchInstance = null;
		if (null == statusList || statusList.isEmpty()) {
			LOGGER.info("batchInstanceStatus is null or empty.");
		} else {
			batchInstance = batchInstanceDao.getRemoteBatchInstances(statusList, firstResult, maxResults, orderList, filterClauseList,
					batchPriorities, userName, userRoles);
		}
		return batchInstance;
	}

	/**
	 * This API returns the list of running job executing on the server by the server IP.
	 * 
	 * @param serverIP {@link String}
	 * @return List<{@link BatchInstance}>
	 */
	@Transactional(readOnly = true)
	@Override
	public List<BatchInstance> getExecutingJobByServerIP(String serverIP) {
		return batchInstanceDao.getExecutingJobByServerIP(serverIP);
	}

	/**
	 * API to evict/remove the batch instance from session.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 */
	@Transactional
	@Override
	public void evict(BatchInstance batchInstance) {
		batchInstanceDao.evict(batchInstance);
	}

	/**
	 * This API returns the list of batch instance on the basis of executing server IP and batch status.
	 * 
	 * @param executingServerIP {@link String}
	 * @param batchInstanceStatus {@link BatchInstanceStatus}
	 * @return List<{@link BatchInstance}>
	 */
	@Override
	public List<BatchInstance> getBatchInstanceByExecutingServerIPAndBatchStatus(String executingServerIP,
			BatchInstanceStatus batchInstanceStatus) {
		return batchInstanceDao.getBatchInstanceByExecutingServerIPAndBatchStatus(executingServerIP, batchInstanceStatus);
	}

	/**
	 * This API returns the list of batch instances for current user by matching batch name and batch status.
	 * 
	 * @param batchName {@link String} batch name to be searched
	 * @param userName {@link String} current user
	 * @param batchStatus {@link BatchInstanceStatus} batch status to be matched
	 * @param userRoles Set<{@link String}>
	 * @return List<{@link BatchInstance}>
	 */
	@Override
	public List<BatchInstance> getBatchInstanceListByBatchNameAndStatus(String batchName, BatchInstanceStatus batchStatus,
			String userName, Set<String> userRoles) {
		List<BatchInstance> batchInstanceList = null;
		if (null == batchStatus) {
			LOGGER.info("batchInstanceStatus is null.");
		} else {
			batchInstanceList = batchInstanceDao.getBatchInstanceListByBatchNameAndStatus(batchName, batchStatus, userName, userRoles);
		}
		return batchInstanceList;
	}

	/**
	 * API to clear current user for a batch instance given it's identifier.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 */
	@Transactional
	@Override
	public void clearCurrentUser(String batchInstanceIdentifier) {
		batchInstanceDao.clearCurrentUser(batchInstanceIdentifier);
	}

	/**
	 * This API fetches all the batch instances on the basis of batch status list passed.
	 * 
	 * @param batchStatusList List<{@link BatchInstanceStatus}>
	 * @return List<{@link BatchInstance}>
	 */
	@Override
	public List<BatchInstance> getBatchInstanceByStatusList(List<BatchInstanceStatus> batchStatusList) {
		List<BatchInstance> batchInstanceList = null;
		if (null != batchStatusList && !batchStatusList.isEmpty()) {
			batchInstanceList = batchInstanceDao.getBatchInstanceByStatusList(batchStatusList);
		}
		return batchInstanceList;
	}

	/**
	 * This API fetches batch instances which having access by the user roles on the basis of ephesoft user.
	 * 
	 * @param userRoles Set<String>
	 * @param batchInstanceIdentifier String
	 * @param currentUserName String
	 * @param ephesoftUser EphesoftUser
	 * @return {@link BatchInstance}
	 */
	@Transactional(readOnly = true)
	@Override
	public BatchInstance getBatchInstanceByUserRole(Set<String> userRoles, String batchInstanceIdentifier, String currentUserName,
			EphesoftUser ephesoftUser) {
		return batchInstanceDao.getBatchInstanceByUserRole(userRoles, batchInstanceIdentifier, currentUserName, ephesoftUser);
	}

	/**
	 * This API performs a fetch all the batch instance on the basis of the staus and batchclass.
	 * 
	 * @param batchInstanceStatus List<BatchInstanceStatus>
	 * @param batchClass {@link BatchClass}
	 * @return List<BatchInstance>
	 */
	@Override
	public List<BatchInstance> getBatchInstByStatusAndBatchClass(List<BatchInstanceStatus> statusList, BatchClass batchClass) {
		return batchInstanceDao.getBatchInstByStatusAndBatchClass(statusList, batchClass);
	}

	/**
	 * API to fetch the system folder for the given batch instance id.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @return {@link String} the system folder path.
	 */
	@Override
	public String getSystemFolderForBatchInstanceId(String batchInstanceIdentifier) {
		return batchInstanceDao.getSystemFolderForBatchInstanceId(batchInstanceIdentifier);
	}

	/**
	 * API to fetch whether the batch class has any of its batches under processing i.e. not finished.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @return int, count of batch instances
	 */
	@Override
	public int getAllUnFinishedBatchInstancesCount(String batchClassIdentifier) {
		return batchInstanceDao.getAllUnFinishedBatchInstancesCount(batchClassIdentifier);
	}

	/**
	 * An API to fetch a single batch instance by batch identifier.
	 * 
	 * @param identifier {@link String}
	 * @return {@link BatchInstance}
	 */
	@Override
	public BatchInstance getBatchInstancesForIdentifier(String identifier) {
		return batchInstanceDao.getBatchInstancesForIdentifier(identifier);
	}

	/**
	 * API  API to fetch a batch instance by Status List Batch Class.
	 * @param batchInstanceStatusList List<BatchInstanceStatus>
	 * @return List<BatchInstance>
	 */
	@Override
	public List<BatchInstance> getBatchInstanceByStatusListBatchClass(List<BatchInstanceStatus> batchStatusList) {
		List<BatchInstance> batchInstanceList = null;
		if (null != batchStatusList && !batchStatusList.isEmpty()) {
			batchInstanceList = batchInstanceDao.getBatchInstanceByStatusListBatchClass(batchStatusList);
		}
		return batchInstanceList;
	}

}
