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

import java.util.List;
import java.util.Set;

import org.hibernate.exception.LockAcquisitionException;

import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.exception.BatchAlreadyLockedException;
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
 * @see com.ephesoft.dcma.da.service.BatchInstanceServiceImpl
 */
public interface BatchInstanceService {

	/**
	 * An api to fetch all batch instance by batch class.
	 * 
	 * @param batchClass BatchClass BatchClass
	 * @return List<BatchInstance>
	 */
	List<BatchInstance> getBatchInstByBatchClass(BatchClass batchClass);

	/**
	 * An api to fetch all batch instance by review user name.
	 * 
	 * @param reviewUserName String
	 * @return List<BatchInstance>
	 */
	List<BatchInstance> getBatchInstByReviewUserName(String reviewUserName);

	/**
	 * An api to fetch all batch instance by validation user name.
	 * 
	 * @param validationUserName String
	 * @return List<BatchInstance>
	 */
	List<BatchInstance> getBatchInstByValidationUserName(String validationUserName);

	/**
	 * An api to fetch all batch instance by BatchInstanceStatus.
	 * 
	 * @param batchInstanceStatus BatchInstanceStatus
	 * @return List<BatchInstance>
	 */
	List<BatchInstance> getBatchInstByStatus(BatchInstanceStatus batchInstanceStatus);

	/**
	 * An api to fetch count of the batch instance table for batch instance status.
	 * 
	 * @param filterClauseList List<BatchInstanceFilter>
	 * @return count of the batch instance present for the batch instance status.
	 */
	int getCount(BatchInstanceStatus batchInstanceStatus, String userName, Set<String> userRole);

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
	 * @param userName current user
	 * @return List<BatchInstance> return the batch instance list.
	 */
	List<BatchInstance> getBatchInstances(List<BatchInstanceStatus> statusList, final int firstResult, final int maxResults,
			final List<Order> orderList, final List<BatchInstanceFilter> filterClauseList, final List<BatchPriority> batchPriorities,
			String currentUser, Set<String> currentUserRoles);

	/**
	 * An api to fetch all the batch instances on the basis of BatchInstanceFilters list.
	 * 
	 * @param batchInstanceFilters List<BatchInstanceFilter> this will add the where clause to the criteria query based on the property
	 *            name and value.
	 * @return
	 */
	List<BatchInstance> getBatchInstances(final List<BatchInstanceFilter> batchInstanceFilters);

	/**
	 * An api to fetch next batch instance from batch instance table for status READY_FOR_REVIEW and READY_FOR_VALIDATION. This will
	 * have minimum priority with above restrictions and result of the query will be order by last modified.
	 * 
	 * @return BatchInstance batch instance for status READY_FOR_REVIEW and READY_FOR_VALIDATION and result of the query will be order
	 *         by last modified.
	 */
	BatchInstance getHighestPriorityBatchInstance(final Set<String> userRole);

	/**
	 * An api to fetch count of the batch instance table for batch instance filters.
	 * 
	 * @param filterClauseList List<BatchInstanceFilter>
	 * @return count of the batch instance present for the batch instance filters.
	 */
	int getCount(final List<BatchInstanceFilter> filterClauseList);

	/**
	 * An api to fetch count of the batch instance table for batch instance status list and batch priority list.
	 * 
	 * @param batchInstStatusList List<BatchInstanceStatus>
	 * @return count of the batch instance present for the batch instance status list and batch priority list.
	 */
	int getCount(final List<BatchInstanceStatus> batchInstStatusList, final List<BatchPriority> batchPriorities,
			final Set<String> currentRole);

	/**
	 * An api to fetch count of the batch instances for a given status list and batch priority and current user required
	 * 
	 * @param batchInstStatusList List<BatchInstanceStatus>
	 * @param batchPriorities the priority list of the batches
	 * @param isCurrUsrNotReq true if the current user can be anyone. False if current user cannot be null.
	 * @return the count satisfying the above requirements
	 */
	int getCount(final List<BatchInstanceStatus> batchInstStatusList, final List<BatchPriority> batchPriorities,
			final boolean isCurrUsrNotReq, final String currentUser, final Set<String> currentRole);

	/**
	 * An api to return total count of batches from the batch instance table.
	 * 
	 * @return total count
	 */
	int getAllCount(final String currentUser, final Set<String> currentRole);

	/**
	 * An api to fetch all the batch instance by BatchPriority.
	 * 
	 * @param batchPriority BatchPriority this will add the where clause to the criteria query based on the priority column.
	 * @return List<BatchInstance> return the batch instance list.
	 */
	List<BatchInstance> getBatchInstance(BatchPriority batchPriority);

	/**
	 * An api to fetch all batch instance by batch Class Name.
	 * 
	 * @param batchClassName String
	 * @return List<BatchInstance>
	 */
	List<BatchInstance> getBatchInstByBatchClassName(String batchClassName);

	/**
	 * An api to fetch all batch instance by batch Class Process Name.
	 * 
	 * @param batchClassProcessName String
	 * @return List<BatchInstance>
	 */
	List<BatchInstance> getBatchInstByBatchClassProcessName(String batchClassProcessName);

	/**
	 * This method will create the batch instance for input batch class, unc sub folder path and local folder path.
	 * 
	 * @param batchClass BatchClass
	 * @param uncSubFolder String
	 * @param localFolder String
	 * @param priority int
	 * @return BatchInstance
	 */
	BatchInstance createBatchInstance(BatchClass batchClass, String uncSubFolder, String localFolder, int priority);

	/**
	 * This method will update the status for batch instance.
	 * 
	 * @param batchInstance BatchInstance
	 * @param status BatchInstanceStatus
	 */
	void updateBatchInstanceStatus(BatchInstance batchInstance, BatchInstanceStatus status);

	/**
	 * This method will update the status of batch instance for input id.
	 * 
	 * @param id String
	 * @param status BatchInstanceStatus
	 */
	void updateBatchInstanceStatusByIdentifier(String identifier, BatchInstanceStatus status);

	/**
	 * This method will update the status of batch instance for input id and status.
	 * 
	 * @param identifier String
	 * @param status String
	 */
	void updateBatchInstanceStatusByIdentifier(String identifier, String status);

	/**
	 * This method will create a new batch instance.
	 * 
	 * @param batchInstance BatchInstance
	 */
	void createBatchInstance(BatchInstance batchInstance);

	/**
	 * This method will update the existing batch instance.
	 * 
	 * @param batchInstance BatchInstance
	 */
	void updateBatchInstance(BatchInstance batchInstance);

	/**
	 * This method will remove the existing batch instance.
	 * 
	 * @param batchInstance BatchInstance
	 */
	void removeBatchInstance(BatchInstance batchInstance);

	/**
	 * An api to fetch all batch instance by batch Class Name and batch instance id's list.
	 * 
	 * @param batchClassName String
	 * @return List<BatchInstance>
	 */
	List<BatchInstance> getBatchInstanceList(String batchClassName, List<String> batchInstanceIdentifier);

	/**
	 * An api to fetch batch class ID for batch instance ID.
	 * 
	 * @param batchInstanceIdentifier String
	 * @return String batchClassID
	 */
	String getBatchClassIdentifier(String batchInstanceIdentifier);

	/**
	 * API to fetch BatchInstance on the basis of batchInstanceIdentifier
	 * 
	 * @param batchInstanceIdentifier
	 * @return BatchInstance
	 */
	BatchInstance getBatchInstanceByIdentifier(String batchInstanceIdentifier);

	/**
	 * API to acquire a batch on the basis of batchInstanceIdentifier and userName.
	 * 
	 * @param batchInstanceIdentifier
	 * @param userName
	 * @return TODO
	 */
	BatchInstance acquireBatchInstance(String batchInstanceIdentifier, String currentUser) throws BatchAlreadyLockedException;

	/**
	 * Unlocks the currently acquired batch by the user (currentUser).
	 * 
	 * @param batchInstanceIdentifier
	 * @param currentUser
	 */
	void unlockCurrentBatchInstance(String batchInstanceIdentifier);

	/**
	 * Unlocks all the batches acquired by the user.
	 * 
	 * @param currentUser
	 */
	void unlockAllBatchInstancesForCurrentUser(String currentUser);

	/**
	 * API to get all batch instances which are currently being executed for a specific IP Address. Output will be all batch instances
	 * which are not in NEW, ERROR or FINISHED state.
	 * 
	 * @param serverInfo
	 * @return List<BatchInstance>
	 */
	List<BatchInstance> getRunningBatchInstancesFor(ServerRegistry lockOwner);

	/**
	 * API to get a batch by applying Hibernate level optimistic locking and to set lock owner ino db.
	 * 
	 * @param identifier
	 * @param lockOwner
	 * @throws LockAcquisitionException
	 */
	void lockBatch(long identifier, ServerRegistry lockOwner) throws LockAcquisitionException;

	/**
	 * API to update the batch instance status 
	 * 
	 * @param identifier
	 * @param status
	 */
	void updateBatchInstanceStatus(BatchInstanceID identifier, BatchInstanceStatus status);

	/**
	 * API to acquire lock on a batch instance
	 * 
	 * @param batchId
	 * @throws LockAcquisitionException
	 */
	void lockBatch(long batchId) throws LockAcquisitionException;

	/**
	 * API to get all the batch instances in sorted order.
	 * 
	 * @return List<BatchInstance> - List of all batch instances.
	 */
	List<BatchInstance> getAllBatchInstances(List<Order> order);

	/**
	 * API to get all the batch instances by matching Batch Name.
	 * 
	 * @return List<BatchInstance> - List of all batch instances.
	 */
	List<BatchInstance> getBatchInstanceByBatchName(String batchName);
	
	/**
	 * API return all unfinished batch instances.
	 * 
	 * @param uncFolder
	 * @return List<BatchInstance> - List of all unfinished batch instances.
	 */
	List<BatchInstance> getAllUnFinishedBatchInstances(String uncFolder);
	
	/**
	 * API to merge batch instance and returning updated batch instance.
	 * 
	 * @param batchInstance
	 * @return BatchInstance {@link BatchInstance}
	 */
	BatchInstance merge(BatchInstance batchInstance);
	
	/**
	 * API to get all unfinshedRemotelyExecutedBatchInstance.
	 * 
	 * @return List<BatchInstance> - List of batch instances.
	 */
	List<BatchInstance> getAllUnfinshedRemotelyExecutedBatchInstance();

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
	 * @param userName current user
	 * @return List<BatchInstance> return the batch instance list.
	 */
	List<BatchInstance> getBatchInstancesExcludedRemoteBatch(List<BatchInstanceStatus> statusList, final int firstResult,
			final int maxResults, final List<Order> orderList, final List<BatchInstanceFilter> filterClauseList,
			final List<BatchPriority> batchPriorities, String currentUser, Set<String> currentUserRoles);

	/**
	 * An api to fetch all the batch instances remotely executing batches by status list. Parameter firstResult set a limit upon the
	 * number of objects to be retrieved. Parameter maxResults set the first result to be retrieved. Parameter orderList set the sort
	 * property and order of that property. If orderList parameter is null or empty then this parameter is avoided.
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
	 * @param userName current user
	 * @return List<BatchInstance> return the batch instance list.
	 */
	List<BatchInstance> getRemoteBatchInstances(List<BatchInstanceStatus> statusList, final int firstResult, final int maxResults,
			final List<Order> orderList, final List<BatchInstanceFilter> filterClauseList, final List<BatchPriority> batchPriorities,
			String currentUser, Set<String> currentUserRoles);

}
