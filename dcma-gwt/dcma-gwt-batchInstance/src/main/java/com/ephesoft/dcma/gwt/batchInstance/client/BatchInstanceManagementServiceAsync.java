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

package com.ephesoft.dcma.gwt.batchinstance.client;

import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.gwt.batchinstance.client.presenter.BatchInstancePresenter.Results;
import com.ephesoft.dcma.gwt.core.client.DCMARemoteServiceAsync;
import com.ephesoft.dcma.gwt.core.shared.BatchInstanceDTO;
import com.ephesoft.dcma.gwt.core.shared.DataFilter;
import com.ephesoft.dcma.gwt.core.shared.exception.GWTException;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous Service class to handle batches, their updation and deletion.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.batchinstance.server.BatchInstanceManagementServiceImpl
 */
public interface BatchInstanceManagementServiceAsync extends DCMARemoteServiceAsync {

	/**
	 * API to get BatchInstanceDTO's for the given filter and in defined order.
	 * 
	 * @param startRow int
	 * @param rowsCount int
	 * @param filters {@link DataFilter}
	 * @param order {@link Order}
	 * @param searchString the searchString on which batch instances have to be fetched
	 * @return List<{@link BatchInstanceDTO}>
	 */
	void getBatchInstanceDTOs(final int startRow, final int rowsCount, final List<DataFilter> filters, final Order order,
			final String searchString, AsyncCallback<List<BatchInstanceDTO>> asyncCallback);

	/**
	 * API to get Row Count passing the provided DataFilter.
	 * 
	 * @param dataFilters {@link DataFilter}
	 * @param searchString the searchString on which batch instances have to be fetched
	 * @return {@link Integer}
	 */
	void getRowCount(List<DataFilter> filters, String searchString, AsyncCallback<Integer> asyncCallback);

	/**
	 * API to delete BatchInstance by identifier.
	 * 
	 * @param identifier {@link String}
	 * @return {@link Results}
	 * @throws GWTException
	 */
	void deleteBatchInstance(String identifier, AsyncCallback<Results> asyncCallback);

	/**
	 * API to restart BatchInstance given it's identifier and module name.
	 * 
	 * @param identifier {@link String}
	 * @param moduleName {@link String}
	 * @return {@link Results}
	 * @throws GWTException
	 */
	void restartBatchInstance(String identifier, String moduleName, AsyncCallback<Results> asyncCallback);

	/**
	 * API to get Individual Row Count.
	 * 
	 * @return {@link Integer}[]
	 */
	void getIndividualRowCount(AsyncCallback<Integer[]> asyncCallback);

	/**
	 * API to get BatchInstanceDTO given it's identifier.
	 * 
	 * @param identifier {@link String}
	 * @return {@link BatchInstanceDTO}
	 * @throws GWTException
	 */
	void getBatchInstanceDTO(String identifier, AsyncCallback<BatchInstanceDTO> asyncCallback);

	/**
	 * API to get Restart Options for a BatchInstance given it's identifier.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @return Map<{@link String},{@link String}>
	 */
	void getRestartOptions(String batchInstanceIdentifier, AsyncCallback<Map<String, String>> asyncCallback);

	/**
	 * API to get BatchInstanceDTO's given the batch name or batch identifier.
	 * 
	 * @param searchString {@link String}
	 * @return List<{@link BatchInstanceDTO}>
	 * @throws GWTException
	 */
	void getBatchInstanceDTOs(String searchString, AsyncCallback<List<BatchInstanceDTO>> asyncCallback);

	/**
	 * API to update BatchInstance Status given it's identifier, to the provided BatchInstanceStatus.
	 * 
	 * @param identifier {@link String}
	 * @param biStatus {@link BatchInstanceStatus}
	 * @return {@link Results}
	 * @throws GWTException
	 */
	void updateBatchInstanceStatus(String identifier, BatchInstanceStatus restartInProgress, AsyncCallback<Results> asyncCallback);

	/**
	 * API to delete Batch Folders for a batch instance given it's identifier.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @return {@link Results}
	 * @throws GWTException
	 */
	void deleteBatchFolders(String identifier, AsyncCallback<Results> asyncCallback);

	/**
	 * API to clear current user for a batch instance given it's identifier.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @return {@link Boolean}
	 * @throws GWTException
	 */
	void clearCurrentUser(String batchInstanceIdentifier, AsyncCallback<Void> asyncCallback);

	/**
	 * API for restarting batch instance having batch status READY_FOR_REVIEW and READY_FOR_VALIDATION.
	 */
	void restartAllBatchInstances(AsyncCallback<Void> asyncCallback);

	/**
	 * API for deleting all batch instances having given batch status and priority filter.
	 */
	void deleteAllBatchInstancesByStatus(List<DataFilter> statusFilters, AsyncCallback<List<String>> asyncCallback);

	/**
	 * API for deleting the batch instance folders for batch instance id's.
	 * 
	 * @param batchInstanceId List<String>
	 */
	void deleteAllBatchInstancesFolders(List<String> batchInstanceId, AsyncCallback<Void> asyncCallback);

}
