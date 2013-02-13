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

package com.ephesoft.dcma.workflow.constant;

import java.io.File;

/**
 * This is a common constants file for Workflow plugin.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public interface WorkFlowConstants {

	/**
	 * Variable for yes.
	 */
	String YES_STRING = "yes";

	/**
	 * Variable for no.
	 */
	String NO_STRING = "no";

	/**
	 * Variable for review document plugin.
	 */
	String REVIEW_DOCUMENT_PLUGIN = "Review_Document_Plugin";

	/**
	 * Variable for validate document plugin.
	 */
	String VALIDATE_DOCUMENT_PLUGIN = "Validate_Document_Plugin";

	/**
	 * Variable for not applicable.
	 */
	String NOT_APPLICABLE = "NA";

	/**
	 * Variable for invalid.
	 */
	String INVALID = "invalid";

	/**
	 * Variable for is.
	 */
	String IS_STRING = "is";

	/**
	 * Variable for remote.
	 */
	String REMOTE = "remote";

	/**
	 * Symbol for underscore sign.
	 */
	String UNDERSCORE_SYMBOL = "_";

	/**
	 * Symbol for minus sign.
	 */
	String MINUS_SYMBOL = "-";

	/**
	 * Symbol for back slash sign.
	 */
	char BACK_SLASH_SYMBOL = '\\';

	/**
	 * Symbol for forward slash sign.
	 */
	char FORWARD_SLASH_SYMBOL = '/';

	/**
	 * Symbol for caret sign.
	 */
	char CARET_SYMBOL = '^';

	/**
	 * Variable for underscore abc.
	 */
	String UNDERSCORE_ABC = "_abc";

	/**
	 * Variable for dot symbol.
	 */
	String DOT_SYMBOL = ".";

	/**
	 * Variable for semicolon symbol.
	 */
	String SEMICOLON = ";";

	/**
	 * Variable for div html tag.
	 */
	String DIV_HTML_TAG = "div";

	/**
	 * Variable for title html tag.
	 */
	String TITLE_HTML_TAG = "title";

	/**
	 * Variable for colon symbol.
	 */
	String COLON = ":";

	/**
	 * variable for workflow.deploy property.
	 */
	String WORKFLOW_DEPLOY = "workflow.deploy";

	/**
	 * variable for properties file name.
	 */
	String DCMA_WORKFLOWS_PROPERTIES = "dcma-workflows.properties";

	/**
	 * variable for plugins.
	 */
	String PLUGINS_CONSTANT = "plugins";

	/**
	 * variable for workflows.
	 */
	String WORKFLOWS_CONSTANT = "workflows";

	/**
	 * variable for modules.
	 */
	String MODULES_CONSTANT = "modules";

	/**
	 * variable for Workflow Status Running module.
	 */
	String WORKFLOW_STATUS_RUNNING = "Workflow_Status_Running";

	/**
	 * variable for Workflow Status Finished module.
	 */
	String WORKFLOW_STATUS_FINISHED = "Workflow_Status_Finished";

	/**
	 * variable for workflows directory path.
	 */
	String META_INF_DCMA_WORKFLOWS = "META-INF\\dcma-workflows";

	/**
	 * variable for empty string.
	 */
	String EMPTY_STRING = "";

	/**
	 * variable for lastModified string.
	 */
	String LAST_MODIFIED = "lastModified";

	/**
	 * variable for FIFO string.
	 */
	String FIFO = "FIFO";

	/**
	 * variable for workflow.batchPickingAlgo string.
	 */
	String WORKFLOW_BATCH_PICKING_ALGO = "workflow.batchPickingAlgo";

	/**
	 * variable for RoundRobin string.
	 */
	String ROUND_ROBIN = "RoundRobin";

	/**
	 * Constant for MINUS_P_KEYWORD.
	 */
	String MINUS_P_KEYWORD = "-p";

	/**
	 * Constant for MINUS_M_KEYWORD.
	 */
	String MINUS_M_KEYWORD = "-m";

	/**
	 * Constant for PERIOD.
	 */
	char PERIOD = '.';

	/**
	 * Batch name constant.
	 */
	public static final String CONSTANT_BATCH_NAME = "/batchName/";

	/**
	 * New batch instance identifier constant.
	 */
	public static final String CONSTANT_NEW_BATCH_INSTANCE_IDENTIFIER = "/newBatchInstanceIdentifier/";

	/**
	 * Module name constant.
	 */
	public static final String CONSTANT_MODULE_NAME = "/moduleName/";

	/**
	 * Batch class id constant.
	 */
	public static final String CONSTANT_BATCH_CLASS_ID = "/batchClassId/";

	/**
	 * Folder location constant.
	 */
	public static final String CONSTANT_FOLDER_LOCATION = "/folderLocation/";

	/**
	 * Server constant.
	 */
	public static final String CONSTANT_SERVER = "/server/";

	/**
	 * Batch identifier constant.
	 */
	public static final String CONSTANT_BATCH_IDENTIFIER = "/batchIdentifier/";

	/**
	 * HTTP Status code 403.
	 */
	public static final int STATUS_CODE_403 = 403;

	/**
	 * HTTP Status code 200.
	 */
	public static final int STATUS_CODE_200 = 200;

	/**
	 * NA constant.
	 */
	public static final String CONSTANT_NA = "NA";

	/**
	 * SPACE Constant.
	 */
	public static final String SPACE = " ";

	/**
	 * COMMA Constant.
	 */
	public static final String COMMA = ",";

	/**
	 * LOCK_MILLIS int.
	 */
	public static final int LOCK_MILLIS = 30 * 60 * 1000;

	/**
	 * HISTORY_SIZE int.
	 */
	public static final int HISTORY_SIZE = 200;

	/**
	 * IDLE_MILLIS_MAX int.
	 */
	public static final int IDLE_MILLIS_MAX = 5 * 60 * 1000;

	/**
	 * IDLE_MILLIS int.
	 */
	public static final int IDLE_MILLIS = 5 * 1000;

	/**
	 * NO_OF_THREADS int.
	 */
	public static final int NO_OF_THREADS = 3;

	/**
	 * FORWARD_SLASH String.
	 */
	public static final String FORWARD_SLASH = "/";

	/**
	 * WEB_SERVICE String.
	 */
	public static final String WEB_SERVICE = "ws";

	/**
	 * HTTP String.
	 */
	public static final String HTTP = "http://";

	/**
	 * BACKUP_PROPERTY_FILE String.
	 */
	public static final String BACKUP_PROPERTY_FILE = "META-INF" + File.separator + "dcma-util" + File.separator
			+ "dcma-backup-service.properties";

	/**
	 * BATCH_INSTANCE_ID String.
	 */
	public static final String BATCH_INSTANCE_ID = "batchInstanceID";

	/**
	 * RESTART_WORKFLOW String.
	 */
	public static final String RESTART_WORKFLOW = "restartWorkflow";

	/**
	 * IS_MODULE_REMOTE String.
	 */
	public static final String IS_MODULE_REMOTE = "isModuleRemote";
	
	/**
	 * THREAD_SLEEP int.
	 */
	public static final int THREAD_SLEEP = 200;

}
