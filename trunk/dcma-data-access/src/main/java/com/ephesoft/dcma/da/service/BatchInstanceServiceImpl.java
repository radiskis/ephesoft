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

package com.ephesoft.dcma.da.service;

import java.util.ArrayList;
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
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.exception.BatchAlreadyLockedException;
import com.ephesoft.dcma.da.dao.BatchInstanceDao;
import com.ephesoft.dcma.da.domain.BatchClass;
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

	private static final String BATCH_ALREADY_LOCKED = "Batch already locked";

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
	 * An api to fetch all batch instance by batch class.
	 * 
	 * @param batchClass BatchClass BatchClass
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
	 * An api to fetch all batch instance by review user name.
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
	 * An api to fetch all batch instance by validation user name.
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
	 * An api to fetch all batch instance by BatchInstanceStatus.
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
	 * An api to fetch count of the batch instance table for batch instance status.
	 * 
	 * @param filterClauseList List<BatchInstanceFilter>
	 * @return count of the batch instance present for the batch instance status.
	 */
	@Transactional(readOnly = true)
	@Override
	public int getCount(BatchInstanceStatus batchInstanceStatus, String userName, Set<String> userRole) {
		int count = -1;
		if (null == batchInstanceStatus) {
			LOGGER.info("batchInstanceStatus is null.");
		} else {
			count = batchInstanceDao.getCount(batchInstanceStatus, userName, userRole);
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
	 * @param currentUserRoles current user.
	 * @return List<BatchInstance> return the batch instance list.
	 */
	@Transactional(readOnly = true)
	@Override
	public List<BatchInstance> getBatchInstances(List<BatchInstanceStatus> statusList, final int firstResult, final int maxResults,
			final List<Order> orderList, final List<BatchInstanceFilter> filterClauseList, final List<BatchPriority> batchPriorities,
			String userName, Set<String> currentUserRoles) {
		List<BatchInstance> batchInstance = null;
		if (null == statusList || statusList.isEmpty()) {
			LOGGER.info("batchInstanceStatus is null or empty.");
		} else {
			batchInstance = batchInstanceDao.getBatchInstances(statusList, firstResult, maxResults, orderList, filterClauseList,
					batchPriorities, userName, currentUserRoles);
		}
		return batchInstance;
	}

	/**
	 * An api to fetch all the batch instances on the basis of BatchInstanceFilters list.
	 * 
	 * @param batchInstanceFilters List<BatchInstanceFilter> this will add the where clause to the criteria query based on the property
	 *            name and value.
	 * @return
	 */
	@Transactional(readOnly = true)
	@Override
	public List<BatchInstance> getBatchInstances(final List<BatchInstanceFilter> batchInstanceFilters) {
		List<BatchInstance> batchInstance = null;
		if (null == batchInstanceFilters || batchInstanceFilters.isEmpty()) {
			LOGGER.info("batchInstanceFilters is null or empty.");
		} else {
			batchInstance = batchInstanceDao.getBatchInstances(null, -1, -1, null, batchInstanceFilters, null, null, null);
		}
		return batchInstance;
	}

	/**
	 * An api to fetch next batch instance from batch instance table for status READY_FOR_REVIEW and READY_FOR_VALIDATION. This will
	 * have minimum priority with above restrictions.
	 * 
	 * @return BatchInstance batch instance for status READY_FOR_REVIEW and READY_FOR_VALIDATION.
	 */
	@Transactional(readOnly = true)
	@Override
	public BatchInstance getHighestPriorityBatchInstance(final Set<String> userRole) {
		BatchInstance batchInstance = null;
		int firstResult = 0;
		int maxResults = 1;
		List<BatchInstanceStatus> statusList = new ArrayList<BatchInstanceStatus>();
		statusList.add(BatchInstanceStatus.READY_FOR_REVIEW);
		statusList.add(BatchInstanceStatus.READY_FOR_VALIDATION);

		List<BatchInstance> batchInstanceList = batchInstanceDao.getBatchInstances(statusList, firstResult, maxResults, userRole);

		if (null != batchInstanceList && !batchInstanceList.isEmpty()) {
			batchInstance = batchInstanceList.get(0);
		}

		return batchInstance;
	}

	/**
	 * An api to fetch count of the batch instance table for batch instance filters.
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
	 * An api to fetch count of the batch instance table for a given status list and batch priority and current user required
	 * 
	 * @param batchInstStatusList List<BatchInstanceStatus>
	 * @param batchPriorities the priority list of the batches
	 * @param isCurrUsrNotReq true if the current user can be anyone. False if current user cannot be null.
	 * @return the count satisfying the above requirements
	 */
	@Transactional(readOnly = true)
	@Override
	public int getCount(final List<BatchInstanceStatus> batchInstStatusList, final List<BatchPriority> batchPriorities,
			final boolean isCurrUsrNotReq, final String currentUser, final Set<String> currentRole) {
		return batchInstanceDao.getCount(batchInstStatusList, batchPriorities, isCurrUsrNotReq, currentRole);
	}

	/**
	 * An api to return total count of batches in the batch instance table.
	 * 
	 * @return total count
	 */
	@Transactional(readOnly = true)
	@Override
	public int getAllCount(final String currentUser, final Set<String> currentRole) {
		return batchInstanceDao.getAllCount(currentRole);
	}

	/**
	 * An api to fetch count of the batch instance table for batch instance status list and batch priority list.
	 * 
	 * @param batchInstStatusList List<BatchInstanceStatus>
	 * @return count of the batch instance present for the batch instance status list and batch priority list.
	 */
	@Transactional(readOnly = true)
	@Override
	public int getCount(final List<BatchInstanceStatus> batchInstStatusList, final List<BatchPriority> batchPriorities,
			final Set<String> currentRole) {
		int count = -1;
		if (null == batchInstStatusList || batchInstStatusList.isEmpty()) {
			LOGGER.info("filterClauseList is null or empty.");
		} else {
			count = batchInstanceDao.getCount(batchInstStatusList, batchPriorities, currentRole);
		}
		return count;
	}

	/**
	 * An api to fetch all the batch instance by BatchPriority.
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
	 * An api to fetch all batch instance by batch Class Name.
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
	 * An api to fetch all batch instance by batch Class Process Name.
	 * 
	 * @param batchClassProcessName String
	 * @return List<BatchInstance>
	 */
	@Transactional(readOnly = true)
	@Override
	public List<BatchInstance> getBatchInstByBatchClassProcessName(String batchClassProcessName) {
		List<BatchInstance> batchInstance = null;
		if (null == batchClassProcessName || "".equals(batchClassProcessName)) {
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
	 * An api to fetch all batch instance by batch Class Name and batch instance id's list.
	 * 
	 * @param batchClassName String
	 * @return List<BatchInstance>
	 */
	@Transactional(readOnly = true)
	@Override
	public List<BatchInstance> getBatchInstanceList(String batchClassName, List<String> batchInstanceIdentifierList) {
		List<BatchInstance> batchInstanceList = null;
		if (null == batchClassName || "".equals(batchClassName) || batchInstanceIdentifierList == null
				|| batchInstanceIdentifierList.isEmpty()) {
			LOGGER.info("batchClassName/batchInstanceIdentifierList is null or empty.");
		} else {
			batchInstanceList = batchInstanceDao.getBatchInstanceList(batchClassName, batchInstanceIdentifierList);
		}
		return batchInstanceList;
	}

	/**
	 * An api to fetch batch class ID for batch instance ID.
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
		if (null == identifier || null == status || "".equals(status)) {
			LOGGER.info("id/status are null or empty.");
		} else {
			this.updateBatchInstanceStatusByIdentifier(identifier, BatchInstanceStatus.valueOf(status));
		}
	}

	@Transactional
	@Override
	public void updateBatchInstanceStatus(BatchInstanceID identifier, BatchInstanceStatus status) {
		Assert.notNull(identifier, "Batch Instance ID can not be null");
		this.updateBatchInstanceStatusByIdentifier(identifier.getID(), status);
	}

	@Transactional
	@Override
	public BatchInstance getBatchInstanceByIdentifier(String batchInstanceIdentifierentifier) {
		BatchInstance batchInstance = null;
		batchInstance = batchInstanceDao.getBatchInstancesForIdentifier(batchInstanceIdentifierentifier);
		return batchInstance;
	}

	@Transactional
	@Override
	public List<BatchInstance> getBatchInstanceByBatchName(String batchName) {
		List<BatchInstance> batchInstances = null;
		batchInstances = batchInstanceDao.getBatchInstancesByBatchName(batchName);
		return batchInstances;
	}

	@Override
	@Transactional(readOnly = false)
	public synchronized BatchInstance acquireBatchInstance(String batchInstanceIdentifier, String currentUser)
			throws BatchAlreadyLockedException {
		BatchInstance batchInstance = null;
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
					batchInstance.setCurrentUser(currentUser);
				}
				batchInstanceDao.updateBatchInstance(batchInstance);
			}
		} else {
			LOGGER.warn("batchInstanceIdentifier id is null. Cannot Proceed further..Returning");
		}
		return batchInstance;
	}

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

	@Override
	@Transactional(readOnly = true)
	public List<BatchInstance> getRunningBatchInstancesFor(ServerRegistry lockOwner) {
		return batchInstanceDao.getRunningBatchInstancesFor(lockOwner);
	}

	@Transactional
	@Override
	public void lockBatch(long batchId, ServerRegistry lockOwner) throws LockAcquisitionException {
		try {
			BatchInstance batchInstance = batchInstanceDao.getBatch(batchId, LockMode.UPGRADE_NOWAIT);
			if (batchInstance.getLockOwner() == null) {
				batchInstance.setLockOwner(lockOwner);
				batchInstanceDao.saveOrUpdate(batchInstance);
			} else {
				throw new LockAcquisitionException(BATCH_ALREADY_LOCKED, null);
			}
		} catch (Exception e) {
			throw new LockAcquisitionException(BATCH_ALREADY_LOCKED, null);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void lockBatch(long batchId) throws LockAcquisitionException {
		try {
			BatchInstance batchInstance = batchInstanceDao.getBatch(batchId, LockMode.UPGRADE_NOWAIT);
			if (batchInstance.getStatus() == BatchInstanceStatus.NEW) {
				batchInstance.setStatus(BatchInstanceStatus.LOCKED);
				batchInstanceDao.saveOrUpdate(batchInstance);
			} else {
				throw new LockAcquisitionException(BATCH_ALREADY_LOCKED, null);
			}
		} catch (LockAcquisitionException le) {
			throw (LockAcquisitionException) le;
		} catch (Exception e) {
			LOGGER.error("Exeception occur while acquiring lock." + e.getMessage(), e);
			throw new LockAcquisitionException(BATCH_ALREADY_LOCKED, null);
		}
	}

	@Override
	public List<BatchInstance> getAllBatchInstances(List<Order> orders) {
		return batchInstanceDao.getAllBatchInstances(orders);
	}

	/**
	 * API return all unfinished batch instances.
	 * 
	 * @param uncFolder
	 * @return List<BatchInstance> - List of all unfinished batch instances.
	 */
	@Override
	public List<BatchInstance> getAllUnFinishedBatchInstances(String uncFolder) {
		return batchInstanceDao.getAllUnFinishedBatchInstances(uncFolder);
	}

	/**
	 * API to merge batch instance and returning updated batch instance.
	 * 
	 * @param batchInstance
	 * @return BatchInstance {@link BatchInstance}
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
	 * An api to fetch all the batch instances excluded remotely executing batches by status list. Parameter firstResult set a limit
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
	 * @param currentUserRoles current user.
	 * @return List<BatchInstance> return the batch instance list.
	 */
	@Transactional(readOnly = true)
	@Override
	public List<BatchInstance> getBatchInstancesExcludedRemoteBatch(List<BatchInstanceStatus> statusList, final int firstResult,
			final int maxResults, final List<Order> orderList, final List<BatchInstanceFilter> filterClauseList,
			final List<BatchPriority> batchPriorities, String userName, Set<String> currentUserRoles) {
		List<BatchInstance> batchInstance = null;
		if (null == statusList || statusList.isEmpty()) {
			LOGGER.info("batchInstanceStatus is null or empty.");
		} else {
			batchInstance = batchInstanceDao.getBatchInstancesExcludedRemoteBatch(statusList, firstResult, maxResults, orderList,
					filterClauseList, batchPriorities, userName, currentUserRoles);
		}
		return batchInstance;
	}

	/**
	 * An api to fetch all the batch instances excluding remotely executing batches by status list. Parameter firstResult set a limit
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
	 * @param currentUserRoles current user.
	 * @return List<BatchInstance> return the batch instance list.
	 */
	@Transactional(readOnly = true)
	@Override
	public List<BatchInstance> getRemoteBatchInstances(List<BatchInstanceStatus> statusList, final int firstResult,
			final int maxResults, final List<Order> orderList, final List<BatchInstanceFilter> filterClauseList,
			final List<BatchPriority> batchPriorities, String userName, Set<String> currentUserRoles) {
		List<BatchInstance> batchInstance = null;
		if (null == statusList || statusList.isEmpty()) {
			LOGGER.info("batchInstanceStatus is null or empty.");
		} else {
			batchInstance = batchInstanceDao.getRemoteBatchInstances(statusList, firstResult, maxResults, orderList, filterClauseList,
					batchPriorities, userName, currentUserRoles);
		}
		return batchInstance;
	}
}
