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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.MappingException;
import org.hibernate.Query;
import org.hibernate.StatelessSession;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.common.WorkflowType;
import com.ephesoft.dcma.core.hibernate.DynamicHibernateDao;
import com.ephesoft.dcma.performance.reporting.ReportingConstants;
import com.ephesoft.dcma.performance.reporting.domain.ReportDisplayData;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.OSUtil;

/**
 * This service provides reporting APIs.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.performance.reporting.service.ReportDataService
 * 
 */
@Service
public class ReportDataServiceImpl implements ReportDataService {

	/**
	 * ERROR_CREATING_DATABASE_CONNECTION String.
	 */
	private static final String ERROR_CREATING_DATABASE_CONNECTION = "Error creating database connection ";

	/**
	 * ERROR_CLOSING_DATABASE_CONNECTION String.
	 */
	private static final String ERROR_CLOSING_DATABASE_CONNECTION = "Error creating database connection ";

	/**
	 * dynamicHibernateDao DynamicHibernateDao.
	 */
	final private DynamicHibernateDao dynamicHibernateDao = new DynamicHibernateDao(ReportingConstants.TARGET_DB_CFG);

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportDataServiceImpl.class);

	
	/**
	 * Method to get System Level Statistics for a specified time.
	 * 
	 * @param endTime {@link Date} upto which the reporting data needs to be fetched
	 * @param startTime {@link Date}Starting Date from which the reporting data needs to be fetched
	 * @param batchClassIdList List<String>
	 * @return List<{@link Integer}> list of integers specifying the system statistics
	 * @throws DCMAException if error occurs in database creation
	 */
	public List<Integer> getSystemStatistics(Date endTime, Date startTime,List<String> batchClassIdList) throws DCMAException {
	       LOGGER.info("Inside getSystemStatistics.. ");
		Connection connection = null;
		List<Integer> finalResult = new ArrayList<Integer>();
		try {
			connection = dynamicHibernateDao.getConnectionProvider().getConnection();

			StatelessSession session = dynamicHibernateDao.getStatelessSession(connection);
			Query qry = session.getNamedQuery(ReportingConstants.GET_SYSTEM_STATISTICS);

			// Adding 1 Day in the End Time to show all the records for that Day
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endTime);
			calendar.add(Calendar.DATE, 1);
	        LOGGER.info("batchClassIdList::"+batchClassIdList);

			qry.setParameter(ReportingConstants.END_TIME, new java.sql.Date(calendar.getTimeInMillis()));

			qry.setParameter(ReportingConstants.START_TIME, new java.sql.Date(startTime.getTime()));
	         qry.setParameterList(ReportingConstants.GET_SYSTEM_STATISTICS_BATCH_CLASS_ID_LIST, batchClassIdList);
		
			List<?> results = qry.list();

			Object[] object = (Object[]) results.get(0);
			for (int i = 0; i < object.length; i++) {
				if (object[i] != null) {
					finalResult.add(Integer.parseInt(object[i].toString()));
				}
			}
		} catch (SQLException e) {
			LOGGER.error(ERROR_CREATING_DATABASE_CONNECTION + e.getMessage(), e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					LOGGER.error(ERROR_CLOSING_DATABASE_CONNECTION + e.getMessage(), e);
				}
			}
		}
		return finalResult;
	}

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
	 * @throws DCMAException if error occurs in database creation
	 */
	@SuppressWarnings("unchecked")
	public List<ReportDisplayData> getReportByWorkflow(List<String> batchClassIds, WorkflowType workflowType, Date endTime,
			Date startTime, int StartIndex, int range, Order order) throws DCMAException {
		Connection connection = null;
		Query qry = null;
		List<ReportDisplayData> displayDatas = null;
		try {
			connection = dynamicHibernateDao.getConnectionProvider().getConnection();

			StatelessSession session = dynamicHibernateDao.getStatelessSession(connection);
			qry = session.getNamedQuery(ReportingConstants.GET_REPORT_BY_WORKFLOW);
			qry.setResultTransformer(Transformers.aliasToBean(ReportDisplayData.class));

			// Adding 1 Day in the End Time to show all the records for that Day
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endTime);
			calendar.add(Calendar.DATE, 1);

			qry.setParameter(ReportingConstants.END_TIME, new java.sql.Date(calendar.getTimeInMillis()));

			qry.setParameter(ReportingConstants.START_TIME, new java.sql.Date(startTime.getTime()));
			qry.setParameter(ReportingConstants.START_INDEX, StartIndex);
			qry.setParameter(ReportingConstants.RANGE, range);
			qry.setParameter(ReportingConstants.WORK_FLOW_TYPE, workflowType.name());
			qry.setParameterList(ReportingConstants.BATCH_CLASS_ID_LIST, batchClassIds);
			displayDatas = qry.list();
		} catch (SQLException e) {
			LOGGER.error(ERROR_CREATING_DATABASE_CONNECTION + e.getMessage(), e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					LOGGER.error(ERROR_CLOSING_DATABASE_CONNECTION + e.getMessage(), e);
				}
			}
		}
		return displayDatas;
	}

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
	 * @throws DCMAException if error occurs in database creation
	 */
	@SuppressWarnings("unchecked")
	public List<ReportDisplayData> getReportByUser(List<String> batchClassIds, String userName, Date endTime, Date startTime,
			int StartIndex, int range, Order order) throws DCMAException {
		Connection connection = null;
		Query qry = null;
		List<ReportDisplayData> resultList = null;
		try {
			connection = dynamicHibernateDao.getConnectionProvider().getConnection();
			StatelessSession session = dynamicHibernateDao.getStatelessSession(connection);

			if (userName.equalsIgnoreCase(ReportingConstants.ALL)) {
				qry = session.getNamedQuery(ReportingConstants.GET_REPORT_FOR_ALL_USERS);
			} else {
				qry = session.getNamedQuery(ReportingConstants.GET_REPORT_BY_USER_NAME);
				qry.setParameter(ReportingConstants.USER_NAME, userName);
			}
			qry.setResultTransformer(Transformers.aliasToBean(ReportDisplayData.class));
			qry.setParameterList(ReportingConstants.BATCH_CLASS_ID_LIST, batchClassIds);

			// Adding 1 Day in the End Time to show all the records for that Day
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endTime);
			calendar.add(Calendar.DATE, 1);

			qry.setParameter(ReportingConstants.END_TIME, new java.sql.Date(calendar.getTimeInMillis()));
			qry.setParameter(ReportingConstants.START_TIME, new java.sql.Date(startTime.getTime()));
			qry.setParameter(ReportingConstants.START_INDEX, StartIndex);
			qry.setParameter(ReportingConstants.RANGE, range);
			resultList = qry.list();
		} catch (SQLException e) {
			LOGGER.error(ERROR_CREATING_DATABASE_CONNECTION + e.getMessage(), e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					LOGGER.error(ERROR_CLOSING_DATABASE_CONNECTION + e.getMessage(), e);
				}
			}
		}

		return resultList;
	}

	/**
	 * Method to get total records for a WorkflowType for a specified time.
	 * 
	 * @param batchClassIds List<{@link String}>, Batch Class Ids for which the report data needs to be fetched
	 * @param workflowType {@link WorkflowType}, One of Module , Plugin or Workflow specifying the type of filter
	 * @param endTime {@link Date}, Date upto which the reporting data needs to be fetched
	 * @param startTime {@link Date}, Starting Date from which the reporting data needs to be fetched
	 * @return Total {@link Integer}, Record count for the crtieria parameters
	 * @throws DCMAException if error occurs in database creation
	 */
	public Integer getReportTotalRowCountByWorkflow(List<String> batchClassIds, WorkflowType workflowType, Date endTime, Date startTime)
			throws DCMAException {
		Connection connection = null;
		Integer finalResult = 0;
		try {
			connection = dynamicHibernateDao.getConnectionProvider().getConnection();
			StatelessSession session = dynamicHibernateDao.getStatelessSession(connection);

			Query qry = session.getNamedQuery(ReportingConstants.GET_TOTAL_ROW_COUNT_BY_WORKFLOW);
			qry.setParameterList(ReportingConstants.BATCH_CLASS_ID_LIST, batchClassIds);

			// Adding 1 Day in the End Time to show all the records for that Day
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endTime);
			calendar.add(Calendar.DATE, 1);

			qry.setParameter(ReportingConstants.END_TIME, new java.sql.Date(calendar.getTimeInMillis()));
			qry.setParameter(ReportingConstants.START_TIME, new java.sql.Date(startTime.getTime()));
			qry.setParameter(ReportingConstants.WORK_FLOW_TYPE, workflowType.name());
			List<?> results = qry.list();

			if (results != null) {
				finalResult = (Integer) results.get(0);
			}
		} catch (SQLException e) {
			LOGGER.error(ERROR_CREATING_DATABASE_CONNECTION + e.getMessage(), e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					LOGGER.error(ERROR_CLOSING_DATABASE_CONNECTION + e.getMessage(), e);
				}
			}
		}
		return finalResult;
	}

	/**
	 * Method to get total records for a User for a specified time.
	 * 
	 * @param batchClassIds List<{@link String}>, Batch Class Ids for which the report data needs to be fetched
	 * @param userName {@link String}, User name for which the report are to be fetched
	 * @param endTime {@link Date}, Date upto which the reporting data needs to be fetched
	 * @param startTime {@link Date}, Starting Date from which the reporting data needs to be fetched
	 * @return Total {@link Integer}, Record count for the crtieria parameters
	 * @throws DCMAException if error occurs in database creation
	 */
	public Integer getReportTotalRowCountByUser(List<String> batchClassIds, String userName, Date endTime, Date startTime)
			throws DCMAException {
		Connection connection = null;
		Integer finalResult = 0;
		try {
			connection = dynamicHibernateDao.getConnectionProvider().getConnection();
			StatelessSession session = dynamicHibernateDao.getStatelessSession(connection);
			Query qry;
			if (userName.equalsIgnoreCase(ReportingConstants.ALL)) {
				qry = session.getNamedQuery(ReportingConstants.GET_TOTAL_ROW_COUNT_BY_ALL_USERS);
			} else {
				qry = session.getNamedQuery(ReportingConstants.GET_TOTAL_ROW_COUNT_BY_USER_NAME);
				qry.setParameter(ReportingConstants.USER_NAME, userName);
			}
			qry.setParameterList(ReportingConstants.BATCH_CLASS_ID_LIST, batchClassIds);
			// Adding 1 Day in the End Time to show all the records for that Day
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endTime);
			calendar.add(Calendar.DATE, 1);

			qry.setParameter(ReportingConstants.END_TIME, new java.sql.Date(calendar.getTimeInMillis()));
			qry.setParameter(ReportingConstants.START_TIME, new java.sql.Date(startTime.getTime()));
			List<?> results = qry.list();
			finalResult = 0;
			if (results != null && (!results.isEmpty())) {
				finalResult = (Integer) results.get(0);
			}
		} catch (SQLException e) {
			LOGGER.error(ERROR_CREATING_DATABASE_CONNECTION + e.getMessage(), e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					LOGGER.error(ERROR_CLOSING_DATABASE_CONNECTION + e.getMessage(), e);
				}
			}
		}
		return finalResult;
	}

	/**
	 * Method to get whether another user is connected to the reporting DB.
	 * 
	 * @return Boolean , true if another user is connected to the report database
	 * @throws DCMAException if error occurs in database creation
	 */
	public Boolean isAnotherUserConnected() throws DCMAException {
		LOGGER.info("Entering is already user connected method.");
		Connection connection = null;
		Boolean isAnotherUserAlreadyConnected = false;
		try {
			connection = dynamicHibernateDao.getConnectionProvider().getConnection();
			StatelessSession session = dynamicHibernateDao.getStatelessSession(connection);
			List<?> results = null;
			try {
				Query qry = session.getNamedQuery(ReportingConstants.GET_IS_ALREADY_USER_CONNECTED);
				results = qry.list();
			} catch (MappingException e) {
				String errorMesg = "Unable to get the named query:\"getIsAlreadyUserConnected\" from mapping file.";
				LOGGER.error(errorMesg + "Exception thrown is:", e);
				throw new DCMAException(errorMesg, e);
			} catch (Exception e) {
				String errorMesg = "An error occurred with the reporting query. Please check the logs for further details.";
				LOGGER.error(errorMesg + "Exception thrown is:", e);
				throw new DCMAException(errorMesg, e);
			}
			isAnotherUserAlreadyConnected = false;
			if (results != null && (!results.isEmpty())) {
				isAnotherUserAlreadyConnected = (Boolean) results.get(0);
			}
		} catch (SQLException e) {
			LOGGER.error(ERROR_CREATING_DATABASE_CONNECTION + e.getMessage(), e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					LOGGER.error(ERROR_CLOSING_DATABASE_CONNECTION + e.getMessage(), e);
				}
			}
		}
		LOGGER.info("Value of flag variable \"is_already_user connected\" is: " + isAnotherUserAlreadyConnected);
		return isAnotherUserAlreadyConnected;
	}

	/**
	 * Method to get custom reports button configs from properties file.
	 * 
	 * @return Total {@link Map<String,String>}, Map of pop-up configs
	 * @throws DCMAException if error occurs
	 */
	@Override
	public Map<String, String> getCustomReportButtonPopUpConfigs() throws DCMAException {
		Map<String, String> popUpConfigs = new HashMap<String, String>();
		try {
			ApplicationConfigProperties prop = ApplicationConfigProperties.getApplicationConfigProperties();
			popUpConfigs.put(ReportingConstants.CUSTOM_REPORT_TITLE, prop.getProperty(ReportingConstants.CUSTOM_REPORT_TITLE));
			popUpConfigs.put(ReportingConstants.CUSTOM_REPORT_URL, prop.getProperty(ReportingConstants.CUSTOM_REPORT_URL));
			popUpConfigs.put(ReportingConstants.CUSTOM_REPORT_POP_UP_XDIMENSION, prop
					.getProperty(ReportingConstants.CUSTOM_REPORT_POP_UP_XDIMENSION));
			popUpConfigs.put(ReportingConstants.CUSTOM_REPORT_POP_UP_YDIMENSION, prop
					.getProperty(ReportingConstants.CUSTOM_REPORT_POP_UP_YDIMENSION));
			LOGGER.info("Custom reports title value:" + popUpConfigs.get(ReportingConstants.CUSTOM_REPORT_TITLE));
			LOGGER.info("Custom reports url value:" + popUpConfigs.get(ReportingConstants.CUSTOM_REPORT_URL));
			LOGGER.info("Custom reports button pop-up window xDimension:"
					+ popUpConfigs.get(ReportingConstants.CUSTOM_REPORT_POP_UP_XDIMENSION));
			LOGGER.info("Custom reports button pop-up window yDimension:"
					+ popUpConfigs.get(ReportingConstants.CUSTOM_REPORT_POP_UP_YDIMENSION));
		} catch (IOException ioe) {
			LOGGER.error("Unable to read the custom reports button pop-up configs from properties file .Exception thrown is:"
					+ ioe.getMessage(), ioe);
		}
		return popUpConfigs;
	}

	/**
	 * Method to run reporting Sync DB option.
	 * 
	 * @param antPath {@link String}
	 * @throws DCMAException if error occurs in reading or executing
	 */
	@Override
	public void syncDatabase(String antPath) throws DCMAException {
		InputStreamReader inputStreamReader = null;
		BufferedReader input = null;
		try {
			String commandStr = ReportingConstants.EMPTY;
			if (OSUtil.isWindows()) {
				commandStr = ReportingConstants.COMMAND_STR;
			}
			StringBuilder commandSB = new StringBuilder();
			commandSB.append(commandStr);
			commandSB.append(ReportingConstants.COMMAND_APPEND);
			commandSB.append(antPath);
			commandStr = commandSB.toString();
			Process process = Runtime.getRuntime().exec(commandStr, null, new File(System.getenv(ReportingConstants.ANT_HOME_PATH)));
			inputStreamReader = new InputStreamReader(process.getInputStream());
			input = new BufferedReader(inputStreamReader);
			String line = null;
			do {
				line = input.readLine();
				LOGGER.debug(line);
			} while (line != null);
			int exitValue = process.waitFor();
			LOGGER.debug("System exited with error code:" + exitValue);
			if (exitValue != 0) {
				LOGGER.debug("exitValue for command:" + exitValue);
				LOGGER.error("Non-zero exit value for command found. So exiting the application.");
				throw new Exception("Non-zero exit value for command found. So exiting the application");
			}
		} catch (IOException ioe) {
			LOGGER.error("Exception while Reading Ant File." + ioe.getMessage(), ioe);
			throw new DCMAException("Exception while reading Ant File. " + ioe.getMessage(), ioe);
		} catch (Exception e) {
			LOGGER.error("Exception while Executing Ant Task." + e.getMessage(), e);
			throw new DCMAException("Exception while Executing Ant Task." + e.getMessage(), e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			if (inputStreamReader != null) {
				try {
					inputStreamReader.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}
	}
}
