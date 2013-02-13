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
import java.util.Set;

import org.hibernate.LockMode;

import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.EphesoftUser;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.dao.Dao;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.ServerRegistry;
import com.ephesoft.dcma.da.property.BatchInstanceFilter;
import com.ephesoft.dcma.da.property.BatchPriority;

/**
 * This class is responsible to fetch data of batch instance table from data base.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see ephesoft.dcma.da.dao.hibernate.BatchInstanceDaoImpl
 */
public interface BatchInstanceDao extends Dao<BatchInstance> {

	/**
	 * An API to fetch all batch instances by matching batch name or batch identifier.
	 * 
	 * @param searchString {@link String}
	 * @param userRoles Set<{@link String}>
	 * @return List<BatchInstance>
	 */
	List<BatchInstance> getBatchInstancesByBatchNameOrId(String searchString, Set<String> userRoles);

	/**
	 * An API to fetch a single batch instance by batch identifier.
	 * 
	 * @param identifier String
	 * @return BatchInstance
	 */
	BatchInstance getBatchInstancesForIdentifier(String identifier);

	/**
	 * An API to fetch all batch instance by batch class.
	 * 
	 * @param batchClass BatchClass
	 * @return List<BatchInstance>
	 */
	List<BatchInstance> getBatchInstByBatchClass(BatchClass batchClass);

	/**
	 * An API to fetch all batch instance by review user name.
	 * 
	 * @param reviewUserName String
	 * @return List<BatchInstance>
	 */
	List<BatchInstance> getBatchInstByReviewUserName(String reviewUserName);

	/**
	 * An API to fetch all batch instance by validation user name.
	 * 
	 * @param validationUserName String
	 * @return List<BatchInstance>
	 */
	List<BatchInstance> getBatchInstByValidationUserName(String validationUserName);

	/**
	 * An API to fetch all batch instance by BatchInstanceStatus.
	 * 
	 * @param batchInstanceStatus BatchInstanceStatus
	 * @return List<BatchInstance>
	 */
	List<BatchInstance> getBatchInstByStatus(BatchInstanceStatus batchInstanceStatus);

	/**
	 * An API to fetch the count of the batch instances on batch instance status and batch priority basis. API will return those batch
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
	int getCount(String batchName, BatchInstanceStatus batchInstanceStatus, String userName, BatchPriority priority,
			Set<String> userRoles, EphesoftUser ephesoftUser);

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
	 * @param userName Current User.
	 * @param userRoles Current User Role.
	 * @param ephesoftUser EphesoftUser
	 * @param searchString the searchString on which batch instances have to be fetched
	 * @return List<BatchInstance> return the batch instance list.
	 */
	List<BatchInstance> getBatchInstances(List<BatchInstanceStatus> statusList, final int firstResult, final int maxResults,
			final List<Order> orderList, final List<BatchInstanceFilter> filterClauseList, final List<BatchPriority> batchPriorities,
			String userName, Set<String> userRoles, EphesoftUser ephesoftUser, String searchString);

	/**
	 * An API to fetch all the batch instance for status list input. API will return those batch
	 * instance having access by the user roles on the basis of ephesoft user.
	 * 
	 * @param statusList List<BatchInstanceStatus>
	 * @param firstResult int
	 * @param maxResults int
	 * @param userRoles Set<String>
	 * @param ephesoftUser EphesoftUser
	 * @return List<BatchInstance>
	 */
	List<BatchInstance> getBatchInstances(List<BatchInstanceStatus> statusList, final int firstResult, final int maxResults,
			final Set<String> userRoles, EphesoftUser ephesoftUser);

	/**
	 * An API to fetch count of the batch instance table for batch instance filters.
	 * 
	 * @param filterClauseList List<BatchInstanceFilter>
	 * @return count of the batch instance present for the batch instance filters.
	 */
	int getCount(final List<BatchInstanceFilter> filterClauseList);

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
	int getCount(final List<BatchInstanceStatus> batchInstStatusList, final List<BatchPriority> batchPriorities,
			final Set<String> userRoles, final String currentUserName, EphesoftUser ephesoftUser);

	/**
	 * An API to fetch count of the batch instances for a given status list and batch priority and isCurrUsrNotReq is used for adding
	 * the batch instance access by the current user. This API will return the batch instance having access by the user roles on the
	 * basis of ephesoft user.
	 * 
	 * @param batchInstStatusList List<{@link BatchInstanceStatus}>
	 * @param batchPriorities the priority list of the batches
	 * @param isCurrUsrNotReq true if the current user can be anyone. False if current user cannot be null.
	 * @param currentUserName {@link String}
	 * @param userRoles Set<{@link String}>
	 * @param ephesoftUser {@link EphesoftUser}
	 * @param searchString the searchString on which batch instances have to be fetched
	 * @return int, the count satisfying the above requirements
	 */
	int getCount(final List<BatchInstanceStatus> batchInstStatusList, final List<BatchPriority> batchPriorities,
			final boolean isCurrUsrNotReq, final Set<String> userRoles, final String currentUserName, EphesoftUser ephesoftUser,
			final String searchString);

	/**
	 * An API to return total count of batches from the batch instance table having access by the user roles on the basis of ephesoft
	 * user.
	 * 
	 * @param currentUser {@link String}
	 * @param userRoles Set<{@link String}>
	 * @param ephesoftUser {@link EphesoftUser}
	 * @return int, total count
	 */
	int getAllCount(final String currentUser, final Set<String> currentRole, EphesoftUser ephesoftUser);

	/**
	 * An API to fetch all the batch instance by BatchPriority.
	 * 
	 * @param batchPriority BatchPriority this will add the where clause to the criteria query based on the priority column.
	 * @return List<BatchInstance> return the batch instance list.
	 */
	List<BatchInstance> getBatchInstance(BatchPriority batchPriority);

	/**
	 * An API to fetch all batch instance by batch Class Name.
	 * 
	 * @param batchClassName String
	 * @return List<BatchInstance>
	 */
	List<BatchInstance> getBatchInstByBatchClassName(String batchClassName);

	/**
	 * An API to fetch all batch instance by batch Class Process Name.
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
	 * An API to fetch all batch instance by batch Class Name and batch instance id's list.
	 * 
	 * @param batchClassName String
	 * @param batchInstanceIDList List<String>
	 * @return List<BatchInstance>
	 */
	List<BatchInstance> getBatchInstanceList(String batchClassName, List<String> batchInstanceIDList);

	/**
	 * API to fetch BatchInstance for the current user.
	 * 
	 * @param currentUser String
	 * @return List<BatchInstance>
	 */
	List<BatchInstance> getAllBatchInstancesForCurrentUser(String currentUser);

	/**
	 * API to get all batch instances which are currently being executed for a specific IP Address. Output will be all batch instances
	 * which are not in NEW, ERROR or FINISHED state.
	 * 
	 * @param lockOwner ServerRegistry
	 * @return List<BatchInstance>
	 */
	List<BatchInstance> getRunningBatchInstancesFor(ServerRegistry lockOwner);

	/**
	 * API to get a batch by applying Hibernate level optimistic locking.
	 * 
	 * @param batchId long
	 * @param lockMode LockMode
	 * @return BatchInstance
	 */
	BatchInstance getBatch(long batchId, LockMode lockMode);

	/**
	 * API to get all the batch instances in sorted order.
	 * 
	 * @param orders List<Order>
	 * @return List<BatchInstance> - List of all batch instances.
	 */
	List<BatchInstance> getAllBatchInstances(List<Order> orders);

	/**
	 * API to get all unfinished batch instances.
	 * 
	 * @param uncFolder String
	 * @return List<BatchInstance> - List of all batch instances.
	 */
	List<BatchInstance> getAllUnFinishedBatchInstances(String uncFolder);

	/**
	 * API to update local folder for a batch.
	 * 
	 * @param batchInsctance BatchInstance
	 * @param localFolder String
	 */
	void updateBatchInstanceLocalFolder(BatchInstance batchInsctance, String localFolder);

	/**
	 * API to update unc folder for a batch.
	 * 
	 * @param batchInsctance BatchInstance
	 * @param uncFolder String 
	 */
	void updateBatchInstanceUncFolder(BatchInstance batchInsctance, String uncFolder);

	/**
	 * API to get all unfinished remotely executed batches.
	 * 
	 * @return List<BatchInstance> list of batch instance
	 */
	List<BatchInstance> getAllUnfinshedRemotelyExecutedBatchInstance();

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
	 * @param userName {@link String}
	 * @param userRoles Set<{@link String}>
	 * @param ephesoftUser {@link EphesoftUser}
	 * @return List<{@link BatchInstance}> return the batch instance list.
	 */
	List<BatchInstance> getBatchInstancesExcludedRemoteBatch(final String batchNameToBeSearched, List<BatchInstanceStatus> statusList,
			final int firstResult, final int maxResults, final List<Order> orderList,
			final List<BatchInstanceFilter> filterClauseList, final List<BatchPriority> batchPriorities, String userName,
			final Set<String> userRoles, EphesoftUser ephesoftUser);

	/**
	 * An API to fetch all the batch instances only remotely executing batches by status list. Parameter firstResult set a limit upon
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
	 * @param userName Current User.
	 * @param userRoles Current User Role.
	 * @return List<{@link BatchInstance}> return the batch instance list.
	 */
	List<BatchInstance> getRemoteBatchInstances(List<BatchInstanceStatus> statusList, final int firstResult, final int maxResults,
			final List<Order> orderList, final List<BatchInstanceFilter> filterClauseList, final List<BatchPriority> batchPriorities,
			String userName, final Set<String> userRoles);

	/**
	 * This API returns the list of running job executing on the server by the server IP.
	 * 
	 * @param serverIP String
	 * @return List<{@link BatchInstance}>
	 */
	List<BatchInstance> getExecutingJobByServerIP(String serverIP);

	/**
	 * API to fetch Batch Instance by executing Job Server IP and batch status.
	 * 
	 * @param executingServerIP String
	 * @param batchInstanceStatus BatchInstanceStatus
	 * @return List<BatchInstance>
	 */
	List<BatchInstance> getBatchInstanceByExecutingServerIPAndBatchStatus(String executingServerIP,
			BatchInstanceStatus batchInstanceStatus);

	/**
	 * API to fetch batch instance list by Batch name and status.
	 * 
	 * @param batchName String
	 * @param batchStatus BatchInstanceStatus
	 * @param userName String
	 * @param userRoles Set<String>
	 * @return List<BatchInstance>
	 */
	List<BatchInstance> getBatchInstanceListByBatchNameAndStatus(String batchName, BatchInstanceStatus batchStatus, String userName,
			Set<String> userRoles);

	/**
	 * This API for clearing the current user for given batch instance identifier.
	 * 
	 * @param batchInstanceIdentifier String
	 */
	void clearCurrentUser(String batchInstanceIdentifier);

	/**
	 * This API fetches all the batch instances on the basis of batch status list passed.
	 * 
	 * @param batchStatusList List<{@link BatchInstanceStatus}>
	 * @return List<{@link BatchInstance}>
	 */
	List<BatchInstance> getBatchInstanceByStatusList(List<BatchInstanceStatus> batchStatusList);

	/**
	 * This API fetches batch instance on the basis of user name and the roles defined for that user name in the batch class.
	 * 
	 * @param userRoles Set<String>
	 * @param batchInstanceIdentifier String
	 * @param currentUserName String
	 * @param ephesoftUser EphesoftUser
	 * @return {@link BatchInstance}
	 */ 
	BatchInstance getBatchInstanceByUserRole(Set<String> userRoles, String batchInstanceIdentifier, String currentUserName,
			EphesoftUser ephesoftUser);

	/**
	 * This API performs a fetch over all the batch instances on the basis of status, priority and for the given user role.
	 * 
	 * @param statusList batch status list
	 * @param batchPriorities batch priorities
	 * @param userRoles batch class id's for the role for which the current user is logged in
	 * @return List<{@link BatchInstance}>
	 */
	List<BatchInstance> getBatchInstancesForStatusPriority(List<BatchInstanceStatus> statusList, List<BatchPriority> batchPriorities,
			Set<String> userRoles);

	/**
	 * API to fetch all batch instance by BatchInstanceStatus for a batch class.
	 * 
	 * @param statusList List<BatchInstanceStatus>
	 * @param batchClass BatchClass
	 * @return List<BatchInstance>
	 */
	List<BatchInstance> getBatchInstByStatusAndBatchClass(List<BatchInstanceStatus> statusList, BatchClass batchClass);

	/**
	 * API to fetch the system folder for the given batch instance id.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @return {@link String} the system folder path.
	 */
	String getSystemFolderForBatchInstanceId(String batchInstanceIdentifier);

	/**
	 * API to fetch whether the batch class has any of its batches under processing i.e. not finished.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @return int, count of batch instances
	 */
	int getAllUnFinishedBatchInstancesCount(String batchClassIdentifier);

	/**
	 * This API fetches all the batch instances on the basis of batch status list passed.
	 * 
	 * @param batchStatusList List<{@link BatchInstanceStatus}>
	 * @return List<{@link BatchInstance}>
	 */
	List<BatchInstance> getBatchInstanceByStatusListBatchClass(List<BatchInstanceStatus> batchStatusList);

}
