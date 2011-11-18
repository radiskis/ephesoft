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

package com.ephesoft.dcma.da.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.LockMode;

import com.ephesoft.dcma.core.common.BatchInstanceStatus;
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
 */
public interface BatchInstanceDao extends Dao<BatchInstance> {

	/**
	 * An api to fetch all batch instances by matching batch name.
	 * 
	 * */

	List<BatchInstance> getBatchInstancesByBatchName(String batchName);

	/**
	 * An api to fetch a single batch instance by batch identifier.
	 * 
	 * */
	BatchInstance getBatchInstancesForIdentifier(String identifier);

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
	 * @param userName Current User.
	 * @param currentUserRoles Current User Role.
	 * @return List<BatchInstance> return the batch instance list.
	 */
	List<BatchInstance> getBatchInstances(List<BatchInstanceStatus> statusList, final int firstResult, final int maxResults,
			final List<Order> orderList, final List<BatchInstanceFilter> filterClauseList, final List<BatchPriority> batchPriorities,
			String userName, Set<String> currentUserRoles);

	/**
	 * An api to fetch all the batch instance for status list input.
	 * 
	 * @param statusList List<BatchInstanceStatus>
	 * @param firstResult int
	 * @param maxResults int
	 * @return List<BatchInstance>
	 */
	List<BatchInstance> getBatchInstances(List<BatchInstanceStatus> statusList, final int firstResult, final int maxResults,
			final Set<String> userRoles);

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

	int getCount(final List<BatchInstanceStatus> batchInstStatusList, final List<BatchPriority> batchPriorities,
			final boolean isCurrUsrNotReq, final Set<String> currentRole);

	int getAllCount(final Set<String> currentRole);

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
	List<BatchInstance> getBatchInstanceList(String batchClassName, List<String> batchInstanceIDList);

	/**
	 * API to fetch BatchInstance for the current user.
	 * 
	 * @param currentUser
	 * @return
	 */
	List<BatchInstance> getAllBatchInstancesForCurrentUser(String currentUser);

	/**
	 * API to get all batch instances which are currently being executed for a specific IP Address. Output will be all batch instances
	 * which are not in NEW, ERROR or FINISHED state.
	 * 
	 * @param serverInfo
	 * @return List<BatchInstance>
	 */
	List<BatchInstance> getRunningBatchInstancesFor(ServerRegistry lockOwner);

	/**
	 * API to get a batch by applying Hibernate level optimistic locking.
	 * 
	 * @param batchId
	 * @param lockMode
	 * @return BatchInstance
	 */
	BatchInstance getBatch(long batchId, LockMode lockMode);

	/**
	 * API to get all the batch instances in sorted order.
	 * 
	 * @return List<BatchInstance> - List of all batch instances.
	 */
	List<BatchInstance> getAllBatchInstances(List<Order> orders);

	/**
	 * API to get all unfinished batch instances.
	 * 
	 * @param uncFolder
	 * @return List<BatchInstance> - List of all batch instances.
	 */
	List<BatchInstance> getAllUnFinishedBatchInstances(String uncFolder);

	/**
	 * API to update local folder for a batch.
	 * 
	 * @param batchInsctance
	 * @param localFolder
	 */
	void updateBatchInstanceLocalFolder(BatchInstance batchInsctance, String localFolder);

	/**
	 * API to update unc folder for a batch.
	 * 
	 * @param batchInsctance
	 * @param uncFolder
	 */
	void updateBatchInstanceUncFolder(BatchInstance batchInsctance, String uncFolder);

	/**
	 * API to get all unfinished remotely executed batches.
	 * 
	 * @return List<BatchInstance> list of batch instance
	 */
	List<BatchInstance> getAllUnfinshedRemotelyExecutedBatchInstance();

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
	 * @param userName Current User.
	 * @param currentUserRoles Current User Role.
	 * @return List<BatchInstance> return the batch instance list.
	 */
	List<BatchInstance> getBatchInstancesExcludedRemoteBatch(List<BatchInstanceStatus> statusList, final int firstResult,
			final int maxResults, final List<Order> orderList, final List<BatchInstanceFilter> filterClauseList,
			final List<BatchPriority> batchPriorities, String userName, final Set<String> batchClassIdentifier);

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
	 * @param userName Current User.
	 * @param currentUserRoles Current User Role.
	 * @return List<BatchInstance> return the batch instance list.
	 */
	List<BatchInstance> getRemoteBatchInstances(List<BatchInstanceStatus> statusList, final int firstResult,
			final int maxResults, final List<Order> orderList, final List<BatchInstanceFilter> filterClauseList,
			final List<BatchPriority> batchPriorities, String userName, final Set<String> batchClassIdentifier);
	
	/**
	 * This API returns the list of running job executing on the server by the server IP.
	 * @param serverIP
	 * @return
	 */
	List<BatchInstance> getExecutingJobByServerIP(String serverIP);
}
