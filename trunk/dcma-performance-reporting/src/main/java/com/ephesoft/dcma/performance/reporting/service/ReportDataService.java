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

package com.ephesoft.dcma.performance.reporting.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.common.WorkflowType;
import com.ephesoft.dcma.performance.reporting.domain.ReportDisplayData;

/**
 * This service provides reporting APIs.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.performance.reporting.service.ReportDataServiceImpl
 * 
 */
public interface ReportDataService {

	/**
	 * Method to get System Level Statistics for a specified time.
	 * 
	 * @param endTime {@link Date} upto which the reporting data needs to be fetched
	 * @param startTime {@link Date}Starting Date from which the reporting data needs to be fetched
	 * @param batchClassIdList List<String>
	 * @return List<{@link Integer}> list of integers specifying the system statistics
	 * @throws DCMAException
	 */
	List<Integer> getSystemStatistics(Date endTime, Date startTime, List<String> batchClassIdList) throws DCMAException;

	/**
	 * Method to get whether another user is connected to the reporting DB.
	 * 
	 * @return Boolean , true if another user is connected to the report database
	 * @throws DCMAException
	 */
	Boolean isAnotherUserConnected() throws DCMAException;

	/**
	 * Method to get Reports Per page for a WorkflowType for a specified time.
	 * 
	 * @param batchClassIds List<{@link String}> Batch Class Ids for which the report data needs to be fetched
	 * @param workflowType {@link WorkflowType}, One of Module , Plugin or Workflow specifying the type of filter
	 * @param endTime {@link Date} upto which the reporting data needs to be fetched
	 * @param startTime {@link Date} Starting Date from which the reporting data needs to be fetched
	 * @param StartIndex int, Start Index for pagination
	 * @param range int Number of records per page
	 * @param order {@link Order} By field
	 * @return List<{@link ReportDisplayData}> List of RepoertDisplayData DTOs
	 * @throws DCMAException
	 */
	List<ReportDisplayData> getReportByWorkflow(List<String> batchClassIds, WorkflowType workflowType, Date endTime, Date startTime,
			int StartIndex, int range, Order order) throws DCMAException;

	/**
	 * Method to get Reports Per page for a User for a specified time.
	 * 
	 * @param batchClassIds List<{@link String}>, Batch Class Ids for which the report data needs to be fetched
	 * @param userName {@link String}, User name for which the report are to be fetched
	 * @param endTime {@link Date}, Date upto which the reporting data needs to be fetched
	 * @param startTime {@link Date}, Starting Date from which the reporting data needs to be fetched
	 * @param StartIndex int, Start Index for pagination
	 * @param range int, Number of records per page
	 * @param order {@link Order}, By field
	 * @return List<{@link ReportDisplayData}>, List of RepoertDisplayData DTOs
	 * @throws DCMAException
	 */
	List<ReportDisplayData> getReportByUser(List<String> batchClassIds, String userName, Date endTime, Date startTime, int StartIndex,
			int range, Order order) throws DCMAException;

	/**
	 * Method to get total records for a WorkflowType for a specified time.
	 * 
	 * @param batchClassIds List<{@link String}>, Batch Class Ids for which the report data needs to be fetched
	 * @param workflowType {@link WorkflowType}, One of Module , Plugin or Workflow specifying the type of filter
	 * @param endTime {@link Date}, Date upto which the reporting data needs to be fetched
	 * @param startTime {@link Date}, Starting Date from which the reporting data needs to be fetched
	 * @return Total {@link Integer}, Record count for the crtieria parameters
	 * @throws DCMAException
	 */
	Integer getReportTotalRowCountByWorkflow(List<String> batchClassIds, WorkflowType workflowType, Date endTime, Date startTime)
			throws DCMAException;

	/**
	 * Method to get total records for a User for a specified time.
	 * 
	 * @param batchClassIds List<{@link String}>, Batch Class Ids for which the report data needs to be fetched
	 * @param userName {@link String}, User name for which the report are to be fetched
	 * @param endTime {@link Date}, Date upto which the reporting data needs to be fetched
	 * @param startTime {@link Date}, Starting Date from which the reporting data needs to be fetched
	 * @return Total {@link Integer}, Record count for the crtieria parameters
	 * @throws DCMAException
	 */
	Integer getReportTotalRowCountByUser(List<String> batchClassIds, String userName, Date endTime, Date startTime)
			throws DCMAException;

	/**
	 * Method to get custom reports button configs from properties file.
	 * 
	 * @return Total {@link Map<String,String>}, Map of pop-up configs
	 * @throws DCMAException
	 */
	Map<String, String> getCustomReportButtonPopUpConfigs() throws DCMAException;

	/**
	 * Method to run reporting Sync DB option.
	 * 
	 * @param antPath {@link String}
	 * @throws DCMAException
	 */
	void syncDatabase(String antPath) throws DCMAException;

}
