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

package com.ephesoft.dcma.gwt.home.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.EphesoftUser;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.property.BatchInstanceFilter;
import com.ephesoft.dcma.da.property.BatchPriority;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.gwt.core.server.DCMARemoteServiceServlet;
import com.ephesoft.dcma.gwt.core.shared.BatchInstanceDTO;
import com.ephesoft.dcma.gwt.core.shared.DataFilter;
import com.ephesoft.dcma.gwt.home.client.TableModelService;
import com.ephesoft.dcma.gwt.home.client.i18n.BatchListConstants;

/**
 * The server side implementation for the calls coming from client side.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.home.client.TableModelService
 */
public class LandingPageServiceImpl extends DCMARemoteServiceServlet implements TableModelService {

	/**
	 *Serial version id.
	 */
	private static final long serialVersionUID = 263606265567100312L;

	/**
	 * Date format used in batch class update date.
	 */
	private static final String DATE_FORMAT = "dd-MMM-yyyy HH:mm:ss";

	/**
	 * API to get Rows of the table in the form of BatchInstanceDTO for the given batch and filters.
	 * 
	 * @param batchNameToBeSearched {@link String}
	 * @param startRow int
	 * @param rowsCount int
	 * @param filters {@link DataFilter}[ ]
	 * @param order {@link Order}
	 * @return List< {@link BatchInstanceDTO}>
	 */
	@Override
	public List<BatchInstanceDTO> getRows(final String batchNameToBeSearched, final int startRow, final int rowsCount,
			final DataFilter[] filters, final Order order) {
		List<BatchInstanceStatus> statusList = getStatusList(filters);

		List<Order> orderList = null;
		if (order != null) {
			orderList = new ArrayList<Order>();
			orderList.add(order);
		}
		List<BatchInstanceFilter> filterClauseList = null;

		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		List<BatchInstance> batchInstanceList = null;

		BatchPriority priority = getPriorityValue(filters[0]);
		List<BatchPriority> batchPriorities = new ArrayList<BatchPriority>();
		batchPriorities.add(priority);
		EphesoftUser ephesoftUser = EphesoftUser.NORMAL_USER;
		batchInstanceList = batchInstanceService.getBatchInstancesExcludedRemoteBatch(batchNameToBeSearched, statusList, startRow,
				rowsCount, orderList, filterClauseList, batchPriorities, getUserName(), getUserRoles(), ephesoftUser);

		BatchInstanceDTO batch = null;
		BatchClass batchClass = null;
		ArrayList<BatchInstanceDTO> batches = new ArrayList<BatchInstanceDTO>();

		for (BatchInstance instance : batchInstanceList) {
			batchClass = instance.getBatchClass();
			Date date = instance.getLastModified();
			Date creationDate = instance.getCreationDate();
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
			// TODO : Data for documents etc need to be calculated.
			batch = new BatchInstanceDTO();
			batch.setPriority(instance.getPriority());
			batch.setBatchIdentifier(instance.getIdentifier());
			batch.setBatchName(instance.getBatchName());
			batch.setBatchClassName(batchClass.getDescription());
			batch.setUploadedOn(sdf.format(date));
			batch.setCreatedOn(sdf.format(creationDate));
			batch.setNoOfDocuments(null);
			batch.setReviewStatus(null);
			batch.setValidationStatus(null);
			batch.setNoOfPages(null);
			batch.setStatus(instance.getStatus().name());
			batch.setCurrentUser(instance.getCurrentUser() != null ? instance.getCurrentUser() : BatchListConstants.EMPTY_STRING);
			batches.add(batch);
		}
		return batches;
	}

	private List<BatchInstanceStatus> getStatusList(final DataFilter[] filters) {
		List<BatchInstanceStatus> statusList = new ArrayList<BatchInstanceStatus>();
		if (filters == null || filters[1] == null) {
			statusList.add(BatchInstanceStatus.READY_FOR_REVIEW);
			statusList.add(BatchInstanceStatus.READY_FOR_VALIDATION);
		} else {
			String value = filters[1].getValue();
			BatchInstanceStatus statusFilter = getStatusFilter(value);
			statusList.add(statusFilter);
		}
		return statusList;
	}

	private BatchInstanceStatus getStatusFilter(final String value) {

		BatchInstanceStatus batchInstanceStatus = null;
		if (value != null) {
			int statusInt = Integer.parseInt(value);
			BatchInstanceStatus[] statusArray = BatchInstanceStatus.values();

			for (BatchInstanceStatus status : statusArray) {
				if (status.getId().intValue() == statusInt) {
					batchInstanceStatus = status;
				}
			}
		}
		return batchInstanceStatus;

	}

	private BatchPriority getPriorityValue(final DataFilter filter) {
		BatchPriority priorityValue = null;
		if (filter != null) {
			int priorityInt = Integer.parseInt(filter.getValue());
			BatchPriority[] priorities = BatchPriority.values();

			for (BatchPriority priority : priorities) {
				if (priority.getLowerLimit() != null && priority.getLowerLimit().intValue() == priorityInt) {
					priorityValue = priority;
				}
			}
		}
		return priorityValue;
	}

	/**
	 * API to get total Rows Count for the given data filters.
	 * 
	 * @param filters {@link DataFilter}[ ]
	 * @return {@link Integer}
	 */
	@Override
	public Integer getRowsCount(final DataFilter[] filters) {
		List<BatchInstanceStatus> statusList = getStatusList(filters);
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		BatchPriority priority = getPriorityValue(filters[0]);
		List<BatchPriority> batchPriorities = new ArrayList<BatchPriority>();
		batchPriorities.add(priority);
		Integer rowCount = null;
		EphesoftUser ephesoftUser = EphesoftUser.NORMAL_USER;
		rowCount = batchInstanceService.getCount(statusList, batchPriorities, getUserRoles(), getUserName(), ephesoftUser);
		return rowCount;
	}

	/**
	 * API to get Individual Row Counts for each batch.
	 * 
	 * @return {@link Integer}[]
	 */
	@Override
	public Integer[] getIndividualRowCounts() {
		Integer[] countByStatus = new Integer[2];
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		String userName = getUserName();

		EphesoftUser ephesoftUser = EphesoftUser.NORMAL_USER;
		countByStatus[0] = batchInstanceService.getCount(null, BatchInstanceStatus.READY_FOR_REVIEW, userName, null, getUserRoles(),
				ephesoftUser);

		countByStatus[1] = batchInstanceService.getCount(null, BatchInstanceStatus.READY_FOR_VALIDATION, userName, null,
				getUserRoles(), ephesoftUser);
		return countByStatus;
	}

	/**
	 * API to get Next Batch Instance.
	 * 
	 * @return {@link String}
	 */
	@Override
	public String getNextBatchInstance() {
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		BatchInstance nextBatchInstance = batchInstanceService.getHighestPriorityBatchInstance(getUserRoles(),
				EphesoftUser.NORMAL_USER);
		String returnValue = null;
		if (nextBatchInstance != null) {
			returnValue = nextBatchInstance.getIdentifier();
		}
		return returnValue;
	}

	/**
	 * API to get Rows Count of a batch passing the given data filters.
	 * 
	 * @param batchName {@link String}
	 * @param filters {@link DataFilter}[ ]
	 * @return {@link Integer}
	 */
	@Override
	public Integer getRowsCount(String batchName, DataFilter[] filters) {
		BatchInstanceStatus status = BatchInstanceStatus.READY_FOR_REVIEW;
		BatchPriority priority = null;
		if (filters[1] != null) {
			status = getStatusFilter(filters[1].getValue());
		}
		if (filters[0] != null) {
			priority = getPriorityValue(filters[0]);
		}
		int rowCount = 0;
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		rowCount = batchInstanceService.getCount(batchName, status, getUserName(), priority, getUserRoles(), EphesoftUser.NORMAL_USER);
		return rowCount;
	}
}
