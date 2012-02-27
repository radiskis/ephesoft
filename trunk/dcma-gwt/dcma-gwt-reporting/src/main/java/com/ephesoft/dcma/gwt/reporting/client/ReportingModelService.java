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

package com.ephesoft.dcma.gwt.reporting.client;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.common.WorkflowType;
import com.ephesoft.dcma.gwt.core.client.DCMARemoteService;
import com.ephesoft.dcma.gwt.core.shared.ReportDTO;
import com.ephesoft.dcma.gwt.core.shared.exception.GWTException;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * This class enables the user to generate execution reports on various basis.
 * 
 * @author Ephesoft
 * @see com.ephesoft.dcma.gwt.reporting.server.ReportingServiceImpl
 */
@RemoteServiceRelativePath("reportingService")
public interface ReportingModelService extends DCMARemoteService {

	/**
	 * API to get System Statistics between the given startDate and endDate.
	 * 
	 * @param startDate {@link Date}
	 * @param endDate {@link Date}
	 * @return List<{@link Integer}>
	 * @throws GWTException
	 */
	List<Integer> getSystemStatistics(Date startDate, Date endDate) throws GWTException;

	/**
	 * API to get flag whether another user is already connected with the report DB.
	 * 
	 * @return Boolean
	 * @throws GWTException
	 */
	Boolean isAnotherUserConnected() throws GWTException;

	/**
	 * API to get Table Data for a workflow type between the given startDate and endDate for a list of batch Class Ids in the specified
	 * order.
	 * 
	 * @param batchClassIds List<{@link String}>
	 * @param workflowType {@link WorkflowType}
	 * @param startDate {@link Date}
	 * @param endDate {@link Date}
	 * @param startIndex int
	 * @param maxResults int
	 * @param order {@link Order}
	 * @return List<{@link ReportDTO}>
	 * @throws GWTException
	 */
	List<ReportDTO> getTableData(List<String> batchClassIds, WorkflowType workflowType, Date startDate, Date endDate, int startIndex,
			int maxResults, Order order) throws GWTException;

	/**
	 * API to get Table Data For User between the given startDate and endDate for a list of batch Class Ids in the specified order.
	 * 
	 * @param batchClassIds List<{@link String}>
	 * @param user {@link String}
	 * @param startDate {@link Date}
	 * @param endDate {@link Date}
	 * @param startIndex int
	 * @param maxResults int
	 * @param order {@link Order}
	 * @return List<{@link ReportDTO}>
	 * @throws GWTException
	 */
	List<ReportDTO> getTableDataForUser(List<String> batchClassIds, String user, Date startDate, Date endDate, int startIndex,
			int maxResults, Order order) throws GWTException;

	/**
	 * API to get All Users available.
	 * 
	 * @return List<{@link String}>
	 */
	List<String> getAllUsers();

	/**
	 * API to get custom report button pop-up title, url,xDimension and yDimension from properties file
	 * 
	 * @return Map<{@link String}, {@link String}>
	 * @throws GWTException
	 */
	Map<String,String> getCustomReportButtonPopUpConfigs() throws GWTException;

	/**
	 * API to get All Batch Classes available in the form of map of identifier and it's description.
	 * 
	 * @return HashMap<{@link String}, {@link String}>
	 */
	HashMap<String, String> getAllBatchClasses();

	/**
	 * API to get Total Row Count for workflow type between the given startDate and endDate and list of batch class id's.
	 * 
	 * @param batchClassIds List<{@link String}>
	 * @param workflowType {@link WorkflowType}
	 * @param startDate {@link Date}
	 * @param endDate {@link Date}
	 * @return {@link Integer}
	 * @throws GWTException
	 */
	Integer getTotalRowCount(List<String> batchClassIds, WorkflowType workflowType, Date startDate, Date endDate) throws GWTException;

	/**
	 * API to API to get Total Row Count for a user between the given startDate and endDate and list of batch class id's.
	 * 
	 * @param batchClassIds List<{@link String}>
	 * @param user {@link String}
	 * @param startDate {@link Date}
	 * @param endDate {@link Date}
	 * @return {@link Integer}
	 * @throws GWTException
	 */
	Integer getTotalRowCountForUser(List<String> batchClassIds, String user, Date startDate, Date endDate) throws GWTException;

	/**
	 * API to sync Database for reporting.
	 * 
	 * @throws GWTException
	 */
	void syncDatabase() throws GWTException;

}
