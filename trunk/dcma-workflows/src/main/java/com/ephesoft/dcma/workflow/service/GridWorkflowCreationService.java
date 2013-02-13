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

package com.ephesoft.dcma.workflow.service;

import java.util.List;

import com.ephesoft.dcma.core.common.ModuleJpdlPluginCreationInfo;
import com.ephesoft.dcma.core.common.PluginJpdlCreationInfo;

/**
 * 
 * This class is responsible for creating the workflow Jpdl with respect to Grid Computing Batch class.
 * 
 * @author ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.workflow.service.GridWorkflowCreationService
 */
public interface GridWorkflowCreationService {

	/**
	 * JPDL_XML_INITIALIZATION_STRING String.
	 */
	String JPDL_XML_INITIALIZATION_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	/**
	 * MODULE_REMOTE_CONSTANT String.
	 */
	String MODULE_REMOTE_CONSTANT = "-Remote";

	/**
	 * IS_CONSTANT String.
	 */
	String IS_CONSTANT = "Is-";

	/**
	 * EMPTY_STRING String.
	 */
	String EMPTY_STRING = "";

	/**
	 * SUB_PROCESS_NAME String.
	 */
	String SUB_PROCESS_NAME = "???SUB_PROCESS_NAME???";

	/**
	 * SUB_PROCESS_KEY String.
	 */
	String SUB_PROCESS_KEY = "???SUB_PROCESS_KEY???";

	/**
	 * JPDL_PROCESS_NAME String.
	 */
	String JPDL_PROCESS_NAME = "???JPDL_PROCESS_NAME???";

	/**
	 * SCRIPT_NAME String.
	 */
	String SCRIPT_NAME = "???SCRIPT_NAME???";

	/**
	 * BACK_UP_FILE_NAME String.
	 */
	String BACK_UP_FILE_NAME = "???BACK_UP_FILE_NAME???";

	/**
	 * TRANSITION_SUB_PROCESS_NAME String.
	 */
	String TRANSITION_SUB_PROCESS_NAME = "???TRANSITION_SUB_PROCESS_NAME???";

	/**
	 * DECISION_TRANSITION_PROCESS String.
	 */
	String DECISION_TRANSITION_PROCESS = "DECISION_TRANSITION_PROCESS";

	/**
	 * SUB_PROCESS_JPDL_PLUGIN_STRING String.
	 */
	String SUB_PROCESS_JPDL_PLUGIN_STRING = "<sub-process continue=\"async\" name=\"" + SUB_PROCESS_NAME + "\" sub-process-key=\""
			+ SUB_PROCESS_KEY + "\">";

	/**
	 * PARAMETERS_JPDL_EXPR_BIID String.
	 */
	String PARAMETERS_JPDL_EXPR_BIID = "<parameter-in subvar=\"batchInstanceID\" var=\"batchInstanceID\"/>";

	/**
	 * PARAMETERS_JPDL_EXPR_SCRIPTNAME String.
	 */
	String PARAMETERS_JPDL_EXPR_SCRIPTNAME = "<parameter-in expr=\"" + SCRIPT_NAME + "\" subvar=\"scriptName\"/>";

	/**
	 * PARAMETERS_JPDL_EXPR_BACKUPFILENAME String.
	 */
	String PARAMETERS_JPDL_EXPR_BACKUPFILENAME = "<parameter-in expr=\"" + BACK_UP_FILE_NAME + "\" subvar=\"backUpFileName\"/>";

	/**
	 * PARAMETERS_JPDL_EXPR_REMOTE String.
	 */
	String PARAMETERS_JPDL_EXPR_REMOTE = "<parameter-in subvar=\"isModuleRemote\" var=\"isModuleRemote\"/>";

	/**
	 * SUB_PROCESS_JPDL_PLUGIN_STRING_TRANSITION String.
	 */
	String SUB_PROCESS_JPDL_PLUGIN_STRING_TRANSITION = "<transition to=\"" + TRANSITION_SUB_PROCESS_NAME + "\"/></sub-process>";

	/**
	 * JPDL_PROCESS_START_STRING String.
	 */
	String JPDL_PROCESS_START_STRING = "<process name=\"" + JPDL_PROCESS_NAME + "\" xmlns=\"http://jbpm.org/4.3/jpdl\">";

	/**
	 * JPDL_PROCESS_EXECUTION_START String.
	 */
	String JPDL_PROCESS_EXECUTION_START = "<start><transition to=\"" + SUB_PROCESS_NAME + "\"/></start>";

	/**
	 * JPDL_PROCESS_EXECUTION_END String.
	 */
	String JPDL_PROCESS_EXECUTION_END = "<end name=\"end\"/>";

	/**
	 * JPDL_PROCESS_END_STRING String.
	 */
	String JPDL_PROCESS_END_STRING = "</process>";

	/**
	 * END_CONSTANT String.
	 */
	String END_CONSTANT = "end";

	/**
	 * WORKFLOW_PATH String.
	 */
	String WORKFLOW_PATH = "C:\\JPDL\\";

	/**
	 * EVENT_LISTENER_TAG String.
	 */
	String EVENT_LISTENER_TAG = "<on event=\"start\"> <event-listener class=\"com.ephesoft.dcma.workflow.listener.ModuleExecutionStartListener\" /></on>";

	/**
	 * DECISION_TAG String.
	 */
	String DECISION_TAG = "<decision name=\"Resume_Option\">";

	/**
	 * DECISION_HANDLER_CLASS String.
	 */
	String DECISION_HANDLER_CLASS = "<handler class=\"com.ephesoft.dcma.workflow.decisionhandler.ResumeEvaluation\"/>";

	/**
	 * DECISION_TRANSITION_OPTION String.
	 */
	String DECISION_TRANSITION_OPTION = "<transition name=\"" + DECISION_TRANSITION_PROCESS + "\" to=\"" + DECISION_TRANSITION_PROCESS
			+ "\"/>";

	/**
	 * DECISION_TAG_END String.
	 */
	String DECISION_TAG_END = "</decision>";

	/**
	 * RESUME_OPTION String.
	 */
	String RESUME_OPTION = "Resume_Option";

	/**
	 * WORKFLOW_STATUS_FINISHED String.
	 */
	String WORKFLOW_STATUS_FINISHED = "Workflow_Status_Finished";

	/**
	 * WORKFLOW_STATUS_RUNNING String.
	 */
	String WORKFLOW_STATUS_RUNNING = "Workflow_Status_Running";

	/**
	 * REVIEW_DOCUMENT_PLUGIN String.
	 */
	String REVIEW_DOCUMENT_PLUGIN = "Review_Document_Plugin";

	/**
	 * VALIDATE_DOCUMENT_PLUGIN String.
	 */
	String VALIDATE_DOCUMENT_PLUGIN = "Validate_Document_Plugin";

	/**
	 * IS_REVIEW_REQUIRED String.
	 */
	String IS_REVIEW_REQUIRED = "is-review-required";

	/**
	 * IS_VALIDATION_REQUIRED String.
	 */
	String IS_VALIDATION_REQUIRED = "is-validation-required";

	/**
	 * REVIEW_EVALUATION String.
	 */
	String REVIEW_EVALUATION = "com.ephesoft.dcma.workflow.decisionhandler.ReviewEvaluation";

	/**
	 * VALIDATION_EVALUATION String.
	 */
	String VALIDATION_EVALUATION = "com.ephesoft.dcma.workflow.decisionhandler.ValidationEvaluation";

	/**
	 * DECISION_TO_NO String.
	 */
	String DECISION_TO_NO = "???DECISION_TO_NO???";

	/**
	 * DECISION_TO_YES String.
	 */
	String DECISION_TO_YES = "???DECISION_TO_YES???";

	/**
	 * DECISION_NAME_TAG String.
	 */
	String DECISION_NAME_TAG = "???DECISION_NAME_TAG???";

	/**
	 * DECISION_TAG_HANDLER_CLASS String.
	 */
	String DECISION_TAG_HANDLER_CLASS = "???DECISION_TAG_HANDLER_CLASS???";

	/**
	 * DECISION_HANDLING_TAG String.
	 */
	String DECISION_HANDLING_TAG = "<decision name=\"" + DECISION_NAME_TAG + "\"> <handler class=\"" + DECISION_TAG_HANDLER_CLASS
			+ "\" /><transition  name=\"yes\" to=\"" + DECISION_TO_YES + "\"/> <transition  name=\"no\" to=\"" + DECISION_TO_NO
			+ "\"/></decision>";

	/**
	 * REMOTE_CHECK String.
	 */
	String REMOTE_CHECK = "_Remote_Check";

	/**
	 * DECISION_TO_INVALID String.
	 */
	String DECISION_TO_INVALID = "???DECISION_TO_INVALID???";

	/**
	 * DECISION_REMOTE_HANDLER_CLASS String.
	 */
	String DECISION_REMOTE_HANDLER_CLASS = "com.ephesoft.dcma.workflow.decisionhandler.RemoteEvaluation";

	/**
	 * DECISION_REMOTE_HANDLING_TAG String.
	 */
	String DECISION_REMOTE_HANDLING_TAG = "<decision name=\"" + DECISION_NAME_TAG + "\"> " + "<handler class=\""
			+ DECISION_TAG_HANDLER_CLASS + "\" />" + "<transition  name=\"" + "yes" + "\" to=\"" + DECISION_TO_YES + "\"/>"
			+ "<transition  name=\"no\" to=\"" + DECISION_TO_NO + "\"/>" + "<transition  name=\"invalid\" to=\"" + DECISION_TO_INVALID
			+ "\"/></decision>";

	/**
	 * WORKFLOW_STATUS_REMOTE String.
	 */
	String WORKFLOW_STATUS_REMOTE = "Workflow_Status_Remote";

	/**
	 * WORKFLOW_STATUS_ERROR String.
	 */
	String WORKFLOW_STATUS_ERROR = "Workflow_Status_Error";

	/**
	 * WORKFLOW_CONTINUE_CHECK String.
	 */
	String WORKFLOW_CONTINUE_CHECK = "Workflow_Continue_Check";

	/**
	 * API to create JPDL for a workflow/module given it's process name, list of sub process names and workflow path.
	 * 
	 * @param workflowPath {@link String}
	 * @param jpdlProcessName {@link ModuleJpdlPluginCreationInfo}
	 * @param subProcessNameList {@link List}< {@link ModuleJpdlPluginCreationInfo}>
	 * @param isWorkflow boolean
	 * @return {@link String} relative path of the created JPDL.
	 */
	String writeJPDL(String workflowPath, ModuleJpdlPluginCreationInfo jpdlProcessName,
			List<ModuleJpdlPluginCreationInfo> subProcessNameList, boolean isWorkflow);
}
