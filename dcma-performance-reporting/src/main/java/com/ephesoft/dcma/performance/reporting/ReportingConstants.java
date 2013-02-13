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

package com.ephesoft.dcma.performance.reporting;

/**
 * This is Reporting Constants class.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public interface ReportingConstants {

	/**
	 * TARGET_DB_CFG String.
	 */
	String TARGET_DB_CFG = "META-INF/dcma-performance-reporting/hibernate.cfg.xml";

	/**
	 * TARGET_DB_TABLENAME String.
	 */
	String TARGET_DB_TABLENAME = "report_data";

	/**
	 * CUSTOM_REPORT_URL String.
	 */
	String CUSTOM_REPORT_URL = "custom_reports_url";

	/**
	 * CUSTOM_REPORT_TITLE String.
	 */
	String CUSTOM_REPORT_TITLE = "custom_reports_title";

	/**
	 * CUSTOM_REPORT_POP_UP_XDIMENSION String.
	 */
	String CUSTOM_REPORT_POP_UP_XDIMENSION = "custom_reports_pop_up_xdimension";

	/**
	 * CUSTOM_REPORT_POP_UP_YDIMENSION String.
	 */
	String CUSTOM_REPORT_POP_UP_YDIMENSION = "custom_reports_pop_up_ydimension";
	
	/**
	 * GET_SYSTEM_STATISTICS String.
	 */
	String GET_SYSTEM_STATISTICS = "getSystemStatistics";
	
	/**
	 * GET_REPORT_BY_WORKFLOW String.
	 */
	String GET_REPORT_BY_WORKFLOW = "getReportByWorkflow";
	
	/**
	 * GET_SYSTEM_STATISTICS_BATCH_CLASS_ID_LIST String.
	 */
	String GET_SYSTEM_STATISTICS_BATCH_CLASS_ID_LIST = "batchClassIdList";
	
	/**
	 * END_TIME String.
	 */
	String END_TIME = "end_time";

	/**
	 * START_TIME String.
	 */
	String START_TIME = "start_time";
	
	/**
	 * START_INDEX String.
	 */
	String START_INDEX = "start_index";

	/**
	 * BATCH_CLASS_ID_LIST String.
	 */
	String BATCH_CLASS_ID_LIST = "batch_class_id_list";
	
	/**
	 * The path where the ant is placed.
	 */
	String ANT_HOME_PATH = "ANT_HOME_PATH";
	
	/**
	 * String constant for empty.
	 */
	String EMPTY = "";
	
	/**
	 * String constant for all.
	 */
	String ALL = "ALL";
	
	/**
	 * String constant for range.
	 */
	String RANGE = "range";

	/**
	 * String constant for workflow_type.
	 */
	String WORK_FLOW_TYPE = "workflow_type";
	
	/**
	 * GET_REPORT_FOR_ALL_USERS String.
	 */
	String GET_REPORT_FOR_ALL_USERS = "getReportForAllUsers";
	
	/**
	 * GET_REPORT_BY_USER_NAME String.
	 */
	String GET_REPORT_BY_USER_NAME = "getReportByUserName";

    /**
     * USER_NAME String.
     */
	String USER_NAME = "user_name";
	
	/**
	 * GET_TOTAL_ROW_COUNT_BY_WORKFLOW String.
	 */
	 String GET_TOTAL_ROW_COUNT_BY_WORKFLOW = "getTotalRowCountByWorkflow";

	 /**
	  * GET_TOTAL_ROW_COUNT_BY_ALL_USERS String.
	  */
	 String GET_TOTAL_ROW_COUNT_BY_ALL_USERS = "getTotalRowCountByAllUsers";
	 
	 /**
	  * GET_TOTAL_ROW_COUNT_BY_USER_NAME String.
	  */
	 String GET_TOTAL_ROW_COUNT_BY_USER_NAME = "getTotalRowCountByUserName";
	 
	 /**
	  * GET_IS_ALREADY_USER_CONNECTED String.
	  */
	 String GET_IS_ALREADY_USER_CONNECTED = "getIsAlreadyUserConnected";
	 
	 /**
	  * COMMAND_STR String. 
	  */
	 String COMMAND_STR = "cmd /c";
	 
	 /**
	  * COMMAND_APPEND String.
	  */
	 String COMMAND_APPEND = " ant manual-report-generator -f ";
}
