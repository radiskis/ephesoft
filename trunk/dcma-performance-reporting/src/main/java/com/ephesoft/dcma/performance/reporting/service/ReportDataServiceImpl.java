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

package com.ephesoft.dcma.performance.reporting.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.common.WorkflowType;
import com.ephesoft.dcma.core.hibernate.DynamicHibernateDao;
import com.ephesoft.dcma.performance.reporting.ReportingConstants;
import com.ephesoft.dcma.performance.reporting.domain.ReportDisplayData;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.ApplicationContextUtil;
import com.ephesoft.dcma.util.OSUtil;

@Service
public class ReportDataServiceImpl implements ReportDataService {

	private DynamicHibernateDao dynamicHibernateDao = new DynamicHibernateDao(ReportingConstants.TARGET_DB_CFG);

	private static final Logger LOGGER = LoggerFactory.getLogger(ReportDataServiceImpl.class);
	
	/**
	 * The path where the ant is placed.
	 */
	public static final String ANT_HOME_PATH = "ANT_HOME_PATH";

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ephesoft.dcma.performance.reporting.service.ReportDataService#getSystemStatistics(java.util.Date, java.util.Date)
	 */
	public List<Integer> getSystemStatistics(Date endTime, Date startTime) throws DCMAException {
		StatelessSession session = dynamicHibernateDao.getStatelessSession();
		Query qry = session.getNamedQuery("getSystemStatistics");

		// Adding 1 Day in the End Time to show all the records for that Day
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endTime);
		calendar.add(Calendar.DATE, 1);

		qry.setParameter("end_time", new java.sql.Date(calendar.getTimeInMillis()));

		qry.setParameter("start_time", new java.sql.Date(startTime.getTime()));
		List<?> results = qry.list();
		List<Integer> finalResult = new ArrayList<Integer>();

		Object[] object = (Object[]) results.get(0);
		for (int i = 0; i < object.length; i++) {
			if (object[i] != null) {
				finalResult.add(Integer.parseInt(object[i].toString()));
			}
		}
		return finalResult;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ephesoft.dcma.performance.reporting.service.ReportDataService#getReportByWorkflow(java.util.List,
	 * com.ephesoft.dcma.core.common.WorkflowType, java.util.Date, java.util.Date, int, int, com.ephesoft.dcma.core.common.Order)
	 */
	@SuppressWarnings("unchecked")
	public List<ReportDisplayData> getReportByWorkflow(List<String> batchClassIds, WorkflowType workflowType, Date endTime,
			Date startTime, int StartIndex, int range, Order order) throws DCMAException {
		StatelessSession session = dynamicHibernateDao.getStatelessSession();
		Query qry = session.getNamedQuery("getReportByWorkflow");
		qry.setResultTransformer(Transformers.aliasToBean(ReportDisplayData.class));

		// Adding 1 Day in the End Time to show all the records for that Day
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endTime);
		calendar.add(Calendar.DATE, 1);

		qry.setParameter("end_time", new java.sql.Date(calendar.getTimeInMillis()));

		qry.setParameter("start_time", new java.sql.Date(startTime.getTime()));
		qry.setParameter("start_index", StartIndex);
		qry.setParameter("range", range);
		/*
		 * qry.setParameter("order_property", "entityName"); boolean isBool = false; qry.setParameter("order", (isBool ? "ASC" :
		 * "DESC"));
		 */

		qry.setParameter("workflow_type", workflowType.name());
		qry.setParameterList("batch_class_id_list", batchClassIds);

		return qry.list();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ephesoft.dcma.performance.reporting.service.ReportDataService#getReportByUser(java.util.List, java.lang.String,
	 * java.util.Date, java.util.Date, int, int, com.ephesoft.dcma.core.common.Order)
	 */
	@SuppressWarnings("unchecked")
	public List<ReportDisplayData> getReportByUser(List<String> batchClassIds, String userName, Date endTime, Date startTime,
			int StartIndex, int range, Order order) throws DCMAException {
		StatelessSession session = dynamicHibernateDao.getStatelessSession();
		Query qry;
		if (userName.equalsIgnoreCase("ALL")) {
			qry = session.getNamedQuery("getReportForAllUsers");
		} else {
			qry = session.getNamedQuery("getReportByUserName");
			qry.setParameter("user_name", userName);
		}
		qry.setResultTransformer(Transformers.aliasToBean(ReportDisplayData.class));
		qry.setParameterList("batch_class_id_list", batchClassIds);

		// Adding 1 Day in the End Time to show all the records for that Day
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endTime);
		calendar.add(Calendar.DATE, 1);

		qry.setParameter("end_time", new java.sql.Date(calendar.getTimeInMillis()));
		qry.setParameter("start_time", new java.sql.Date(startTime.getTime()));
		qry.setParameter("start_index", StartIndex);
		qry.setParameter("range", range);
		/*
		 * qry.setParameter("order_property", "entityName"); boolean isBool = false; qry.setParameter("order", (isBool ? "ASC" :
		 * "DESC"));
		 */
		return qry.list();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ephesoft.dcma.performance.reporting.service.ReportDataService#getReportTotalRowCountByWorkflow(java.util.List,
	 * com.ephesoft.dcma.core.common.WorkflowType, java.util.Date, java.util.Date)
	 */
	public Integer getReportTotalRowCountByWorkflow(List<String> batchClassIds, WorkflowType workflowType, Date endTime, Date startTime)
			throws DCMAException {
		StatelessSession session = dynamicHibernateDao.getStatelessSession();
		Query qry = session.getNamedQuery("getTotalRowCountByWorkflow");
		qry.setParameterList("batch_class_id_list", batchClassIds);

		// Adding 1 Day in the End Time to show all the records for that Day
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endTime);
		calendar.add(Calendar.DATE, 1);

		qry.setParameter("end_time", new java.sql.Date(calendar.getTimeInMillis()));
		qry.setParameter("start_time", new java.sql.Date(startTime.getTime()));
		qry.setParameter("workflow_type", workflowType.name());
		List<?> results = qry.list();
		Integer finalResult = 0;
		if (results != null) {
			finalResult = (Integer) results.get(0);
		}
		return finalResult;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ephesoft.dcma.performance.reporting.service.ReportDataService#getReportTotalRowCountByUser(java.util.List,
	 * java.lang.String, java.util.Date, java.util.Date)
	 */
	public Integer getReportTotalRowCountByUser(List<String> batchClassIds, String userName, Date endTime, Date startTime)
			throws DCMAException {
		StatelessSession session = dynamicHibernateDao.getStatelessSession();
		Query qry;
		if (userName.equalsIgnoreCase("ALL")) {
			qry = session.getNamedQuery("getTotalRowCountByAllUsers");
		} else {
			qry = session.getNamedQuery("getTotalRowCountByUserName");
			qry.setParameter("user_name", userName);
		}
		qry.setParameterList("batch_class_id_list", batchClassIds);
		// Adding 1 Day in the End Time to show all the records for that Day
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endTime);
		calendar.add(Calendar.DATE, 1);

		qry.setParameter("end_time", new java.sql.Date(calendar.getTimeInMillis()));
		qry.setParameter("start_time", new java.sql.Date(startTime.getTime()));
		List<?> results = qry.list();
		Integer finalResult = 0;
		if (results != null) {
			finalResult = (Integer) results.get(0);
		}
		return finalResult;
	}

	public Boolean isAnotherUserConnected() throws DCMAException {
	   	LOGGER.info("Entering is already user connected method.");
		StatelessSession session = dynamicHibernateDao.getStatelessSession();
		List<?> results = null;
		try {
			Query qry = session.getNamedQuery("getIsAlreadyUserConnected");
			results = qry.list();
		} catch (MappingException e) {
			String errorMesg = "Unable to get the named query:\"getIsAlreadyUserConnected\" from mapping file.";
			LOGGER.error(errorMesg + "Exception thrown is:", e);
			throw new DCMAException(errorMesg);
		} catch (Exception e) {
			String errorMesg = "An error occurred with the reporting query. Please check the logs for further details.";
			LOGGER.error(errorMesg + "Exception thrown is:", e);
			throw new DCMAException(errorMesg);
		}
		Boolean isAnotherUserAlreadyConnected = false;
		if (results != null && (!results.isEmpty())) {
			isAnotherUserAlreadyConnected = (Boolean) results.get(0);
		}
		LOGGER.info("Value of flag variable \"is_already_user connected\" is: " + isAnotherUserAlreadyConnected);
		return isAnotherUserAlreadyConnected;
	}

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
	
	@Override
	public void syncDatabase(String antPath) throws Exception {
		InputStreamReader inputStreamReader = null;
		BufferedReader input = null;
		try {
			String commandStr = "";
			if (OSUtil.isWindows()) {
				commandStr = "cmd /c";
			}
			commandStr = commandStr + " ant manual-report-generator -f " + antPath;
			Process process = Runtime.getRuntime().exec(commandStr, null, new File(System.getenv(ANT_HOME_PATH)));
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
			throw new Exception("Exception while reading Ant File. " + ioe.getMessage());
		} catch (Exception e) {
			LOGGER.error("Exception while Executing Ant Task." + e.getMessage(), e);
			throw new Exception("Exception while Executing Ant Task." + e.getMessage());
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
