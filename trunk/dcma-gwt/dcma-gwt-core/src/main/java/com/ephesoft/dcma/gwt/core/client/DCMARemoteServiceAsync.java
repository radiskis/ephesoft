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

package com.ephesoft.dcma.gwt.core.client;

import java.util.Map;
import java.util.Set;

import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DCMARemoteServiceAsync {

	/**
	 * API to acquire Lock on a batch class given it's identifier asynchronously.
	 * 
	 * @param batchIdentifier
	 * @param callback
	 */
	void acquireLock(String batchIdentifier, AsyncCallback<Void> callback);

	/**
	 * API to initiate Remote Services on the application asynchronously.
	 * 
	 * @param callback
	 */
	void initRemoteService(AsyncCallback<Void> callback);

	/**
	 * API to setup remote services on the application asynchronously.
	 * 
	 * @param callback
	 */
	void setup(AsyncCallback<Void> callback);

	/**
	 * API to clean up asynchronously.
	 * 
	 * @param callback
	 */
	void cleanup(AsyncCallback<Void> callback);

	/**
	 * API to clean up the current user for the current batch
	 * 
	 * @param batchIdentifier
	 * @param callback
	 */
	void cleanUpCurrentBatch(String batchIdentifier, AsyncCallback<Void> callback);

	/**
	 * API to get User Name using the remote services on the application asynchronously.
	 * 
	 * @param callback
	 */
	void getUserName(AsyncCallback<String> callback);

	/**
	 * API to logout from remote services on the application asynchronously.
	 * 
	 * @param callback
	 * @param url
	 */
	void logout(String url, AsyncCallback<Void> callback);

	/**
	 * API to get locale of the user using the remote services on the application asynchronously.
	 * 
	 * @param callback
	 */
	void getLocale(AsyncCallback<String> callback);

	/**
	 * API to get roles of the user using the remote services on the application asynchronously.
	 * 
	 * @param callback
	 */
	void getUserRoles(AsyncCallback<Set<String>> callback);

	/**
	 * API to check if reporting is enabled asynchronously.
	 * 
	 * @param callback
	 */
	void isReportingEnabled(AsyncCallback<Boolean> callback);

	/**
	 * API to get all user's asynchronously.
	 * 
	 * @param callback
	 */
	void getAllUser(AsyncCallback<Set<String>> callback);

	/**
	 * API to get all user groups asynchronously.
	 * 
	 * @param callback
	 */
	void getAllGroups(AsyncCallback<Set<String>> callback);

	/**
	 * 
	 * API to get All BatchClass By User Roles asynchronously.
	 * 
	 * @param callback
	 */
	void getAllBatchClassByUserRoles(AsyncCallback<Set<String>> callback);

	/**
	 * API to check if Upload Batch is Enabled asynchronously.
	 * 
	 * @param callback
	 */
	void isUploadBatchEnabled(AsyncCallback<Boolean> callback);

	/**
	 * API to get BatchList Priority Filter asynchronously.
	 * 
	 * @param callback
	 */
	void getBatchListPriorityFilter(AsyncCallback<Map<BatchInstanceStatus, Integer>> callback);

	/**
	 * API to set BatchList Priority Filter asynchronously.
	 * 
	 * @param reviewBatchListPriority
	 * @param validateBatchListPriority
	 * @param callback
	 */
	void setBatchListPriorityFilter(Integer reviewBatchListPriority, Integer validateBatchListPriority, AsyncCallback<Void> callback);

	/**
	 * API to check if Restart All Batch is Enabled asynchronously.
	 * 
	 * @param callback
	 */
	void isRestartAllBatchEnabled(AsyncCallback<Boolean> callback);

	/**
	 * API to get the Batch List table row count asynchronously.
	 * 
	 * @param callback
	 */
	void getBatchListTableRowCount(AsyncCallback<Integer> callback);

	/**
	 * API to get the batch list screen tab asynchronously.
	 * 
	 * @param callback
	 */
	void getBatchListScreenTab(String userName, AsyncCallback<BatchInstanceStatus> callback);

	/**
	 * API to set the batch list screen tab asynchronously.
	 * 
	 * @param userName
	 * @param batchDTOStatus
	 * @param callback
	 */
	void setBatchListScreenTab(String userName, BatchInstanceStatus batchDTOStatus, AsyncCallback<Void> callback);

	/**
	 * API to disable the restart all button for current session.
	 * 
	 * @return
	 */
	void disableRestartAllButton(AsyncCallback<Void> callback);

	/**
	 * API to get current user of a batch.
	 * 
	 * @param batchInstanceIdentifier
	 * @param callback
	 */
	void getCurrentUser(String batchInstanceIdentifier, AsyncCallback<String> callback);

	/**
	 * API to show License expiry message on login.
	 * 
	 */
	void setUpForLicenseExpiryAlert(AsyncCallback<Void> callback);

	/**
	 * API to show License expiry message on login.
	 */
	void initRemoteServiceForLicenseAlert(AsyncCallback<Void> callback);

	/**
	 * API to validate a regex expression.
	 * 
	 * @param regex
	 * @param callback
	 */
	void validateRegEx(String regex, AsyncCallback<Boolean> callback);

	/**
	 * API to validate a value with the regular expression pattern.
	 * 
	 * @param value {@link String} - input value to be matched with regular expression pattern.
	 * @param regex {@link String} - regular expression pattern for matching.
	 * @param callback
	 */
	void validateValueWithRegEx(final String input, final String regex, AsyncCallback<Boolean> callback);

	/**
	 * API to get the help url from properties file.
	 * 
	 * @return
	 */
	void getHelpUrl(AsyncCallback<String> callback);

	/**
	 * API to get selected batch class from session.
	 * 
	 * @param asyncCallback
	 */
	void getBatchClassInfoFromSession(AsyncCallback<String> asyncCallback);

	/**
	 * API to set the selected batch class info into session.
	 * 
	 * @param batchClassInfo
	 * @param callback
	 */
	void setBatchClassInfoFromSession(String batchClassInfo, AsyncCallback<Void> callback);

	/**
	 * API to get the text and link values for the UI footer.
	 * 
	 * @return {@link Map}< {@link String}, {@link String}>
	 */
	void getFooterProperties(AsyncCallback<Map<String, String>> asyncCallback);

	/**
	 * The <code>getUserType</code> method is used to get the Ephesoft Cloud user type.
	 * 
	 * @param callback {@link AsyncCallback < {@link Integer} .
	 */
	void getUserType(AsyncCallback<Integer> callback);

	/**
	 * API to create the xml file for upload batch info
	 * 
	 * @param uploadBatchFolderPath{@link String} path to xml file to create and store upload batch info.
	 * @param callback.
	 */

	void createXmlForUploadBatchInfo(String uploadBatchFolderPath, AsyncCallback<Void> callback);
}
