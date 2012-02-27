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

package com.ephesoft.dcma.gwt.core.client;

import java.util.Map;
import java.util.Set;

import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.gwt.core.shared.exception.GWTException;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DCMARemoteService extends RemoteService {

	/**
	 * API to acquire Lock on a batch class given it's identifier.
	 * @param batchClassIdentifier {@link String}
	 * @throws GWTException
	 */
	void acquireLock(String batchClassIdentifier) throws GWTException;

	/**
	 * API to initiate Remote Services on the application.
	 * @throws Exception
	 */
	void initRemoteService() throws Exception;

	/**
	 * API to setup remote services on the application.
	 */
	void setup();

	/**
	 * API to logout from remote services on the application.
	 */
	void logout();

	/**
	 * API to clean up.
	 */
	void cleanup();

	/**
	 * API to get User Name using the remote services on the application.
	 * @return {@link String}
	 */
	String getUserName();

	/**
	 * API to get roles of the user using the remote services on the application.
	 * @return Set<{@link String}>
	 */
	Set<String> getUserRoles();

	/**
	 * API to get locale of the user using the remote services on the application.
	 * @return {@link String}
	 */
	String getLocale();

	/**
	 * API to check if reporting is enabled.
	 * @return {@link Boolean}
	 */
	Boolean isReportingEnabled();

	/**
	 * API to get all user's.
	 * @return Set<{@link String}>
	 */
	Set<String> getAllUser();

	/**
	 * API to get all user groups.
	 * @return Set<{@link String}>
	 */
	Set<String> getAllGroups(); 

	/**
	 * API to get All BatchClass By User Roles.
	 * @return Set<{@link String}>
	 */
	Set<String> getAllBatchClassByUserRoles();

	/**
	 * API to check if Upload Batch is Enabled 
	 * @return {@link Boolean}
	 */
	Boolean isUploadBatchEnabled();

	/**
	 * API to get BatchList Priority Filter.
	 * @return Map<{@link BatchInstanceStatus}, {@link Integer}>
	 */
	Map<BatchInstanceStatus, Integer> getBatchListPriorityFilter();

	/**
	 * API to set BatchList Priority Filter.
	 * @param reviewBatchListPriority {@link Integer}
	 * @param validateBatchListPriority {@link Integer}
	 */
	void setBatchListPriorityFilter(Integer reviewBatchListPriority, Integer validateBatchListPriority);
	
	/**
	 * API to check if Restart All Batch is Enabled 
	 * @return {@link Boolean}
	 */
	Boolean isRestartAllBatchEnabled();
	
	/**
	 * API to get the batch list table row count.
	 * @return {@link Integer}
	 */
	Integer getBatchListTableRowCount();
	
	/**
	 * API to get the batch list screen tab for user.
	 * @return {@link BatchInstanceStatus}
	 */
	BatchInstanceStatus getBatchListScreenTab(String userName);
	
	/**
	 * API to set the batch list screen tab for user.
	 * @param userName {@link String}
	 * @param batchDTOStatus {@link BatchInstanceStatus}
	 */
	void setBatchListScreenTab(String userName, BatchInstanceStatus batchDTOStatus);
	
	/**
	 * API to disable the restart all button for current session.
	 * @return 
	 */
	void disableRestartAllButton();
}
