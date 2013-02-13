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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.ModuleJpdlPluginCreationInfo;
import com.ephesoft.dcma.workflow.constant.WorkFlowConstants;

/**
 * This is service to create JPDL for a workflow/module given it's process name, list of sub process names and workflow path.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.workflow.service.WorkflowCreationService
 * 
 */
public class WorkflowCreationServiceImpl implements WorkflowCreationService {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowCreationServiceImpl.class);

	private String getSubProcessString(String subProcessName, String subProcessKey) {
		LOGGER.info("Inserting sub process tag for " + subProcessName + " having sub process key as " + subProcessKey);
		String subProcessJPDLString = SUB_PROCESS_JPDL_PLUGIN_STRING.replace(SUB_PROCESS_NAME, subProcessName).replace(
				SUB_PROCESS_KEY, subProcessKey);
		return subProcessJPDLString;
	}

	private String getScriptNameParameterString(String scriptName) {
		LOGGER.info("Inserting script name parameter " + scriptName);
		String scriptParameterName = PARAMETERS_JPDL_EXPR_SCRIPTNAME.replace(SCRIPT_NAME, scriptName);

		return scriptParameterName;
	}

	private String getBackUpFileNameString(String backUpFileName) {
		LOGGER.info("Inserting back up file name parameter " + backUpFileName);
		String backUpParameterName = PARAMETERS_JPDL_EXPR_BACKUPFILENAME.replace(BACK_UP_FILE_NAME, backUpFileName);

		return backUpParameterName;
	}

	private String getSubProcessTransitionString(String transitionName) {
		LOGGER.info("Inserting transition tag for transition to " + transitionName);
		String transitionString = SUB_PROCESS_JPDL_PLUGIN_STRING_TRANSITION.replace(TRANSITION_SUB_PROCESS_NAME, transitionName);

		return transitionString;
	}

	private String getBatchInstanceIdParameterString() {
		LOGGER.info("Inserting batch instance id paramter tag");
		return PARAMETERS_JPDL_EXPR_BIID;
	}

	private String getJPDLContentInStringForm(ModuleJpdlPluginCreationInfo JPDLProcessName,
			List<ModuleJpdlPluginCreationInfo> subProcessNameList, boolean isWorkflow) {
		StringBuffer jpdlStringBuffer = new StringBuffer();
		LOGGER.info("Initializing JPDL with xml tag");
		jpdlStringBuffer.append(JPDL_XML_INITIALIZATION_STRING);

		LOGGER.info("Creating process name tag for JPDL with process naem: " + JPDLProcessName);
		jpdlStringBuffer.append(JPDL_PROCESS_START_STRING.replace(JPDL_PROCESS_NAME, JPDLProcessName.getSubProcessName()));
		if (subProcessNameList.size() > 0) {

			String startSubProcessName = subProcessNameList.get(0).getSubProcessName();
			LOGGER.info("Creating start tag for sub process " + startSubProcessName);

			if (isWorkflow) {
				LOGGER.info("Creating JPDL for a workflow hence adding resume options tag.");
				jpdlStringBuffer.append(JPDL_PROCESS_EXECUTION_START.replace(SUB_PROCESS_NAME, RESUME_OPTION));
				String decisionTagString = getResumeOptionDecisionTagContent(subProcessNameList);
				jpdlStringBuffer.append(decisionTagString);
			} else {
				if (startSubProcessName.equalsIgnoreCase(REVIEW_DOCUMENT_PLUGIN)) {
					LOGGER.info(startSubProcessName + " Sub process is a review plugin, hence adding the decision tag.");
					startSubProcessName = IS_REVIEW_REQUIRED;
				} else if (startSubProcessName.equalsIgnoreCase(VALIDATE_DOCUMENT_PLUGIN)) {
					LOGGER.info(startSubProcessName + "Sub process is a validate pluguin, hence adding the decision tag.");
					startSubProcessName = IS_VALIDATION_REQUIRED;
				}
				jpdlStringBuffer.append(JPDL_PROCESS_EXECUTION_START.replace(SUB_PROCESS_NAME, startSubProcessName));
				String decisionTagString = getDecisionHandlerTagContent(subProcessNameList);
				jpdlStringBuffer.append(decisionTagString);
			}

			ModuleJpdlPluginCreationInfo subProcess = null;
			subProcess = createInitialSubProcessTags(subProcessNameList, isWorkflow, jpdlStringBuffer);
			// Append string for last sub-process
			createLastSubProcessTag(isWorkflow, jpdlStringBuffer, subProcess);
		} else {
			jpdlStringBuffer.append(JPDL_PROCESS_EXECUTION_START.replace(SUB_PROCESS_NAME, END_CONSTANT));
		}
		LOGGER.info("Inserting End tag");
		jpdlStringBuffer.append(JPDL_PROCESS_EXECUTION_END);
		jpdlStringBuffer.append(JPDL_PROCESS_END_STRING);
		return jpdlStringBuffer.toString();
	}

	private ModuleJpdlPluginCreationInfo createInitialSubProcessTags(List<ModuleJpdlPluginCreationInfo> subProcessNameList,
			boolean isWorkflow, StringBuffer jpdlStringBuffer) {
		ModuleJpdlPluginCreationInfo subProcess = null;
		for (ModuleJpdlPluginCreationInfo transitionSubProcess : subProcessNameList) {
			if (subProcess == null) {
				subProcess = transitionSubProcess;
			} else {
				jpdlStringBuffer.append(getSubProcessString(subProcess.getSubProcessName(), subProcess.getSubProcessKey()));

				// Append BIID to string always
				jpdlStringBuffer.append(getBatchInstanceIdParameterString());

				// if scripting plug-in,Append ScriptName and backUpFileName to string
				if (subProcess.isScriptingPlugin()) {
					LOGGER.info(subProcess.getSubProcessName() + " is a scripting plugin");
					jpdlStringBuffer.append(getScriptNameParameterString(subProcess.getScriptingFileName()));
					jpdlStringBuffer.append(getBackUpFileNameString(subProcess.getBackUpFileName()));
				} else {
					LOGGER.info(subProcess.getSubProcessName() + " is not a scripting plugin");
				}

				String subProcessName = subProcess.getSubProcessName();
				String transitionSubProcessName = transitionSubProcess.getSubProcessName();
				if (isWorkflow) {
					if (!(subProcessName.equalsIgnoreCase(WORKFLOW_STATUS_RUNNING) || subProcessName
							.equalsIgnoreCase(WORKFLOW_STATUS_FINISHED))) {
						// Event listener tag only when process is a workflow.
						LOGGER.info("Sub process is a workflow, hence adding the event listener tag.");
						jpdlStringBuffer.append(EVENT_LISTENER_TAG);
					}
				} else {
					if (transitionSubProcessName.equalsIgnoreCase(REVIEW_DOCUMENT_PLUGIN)) {
						LOGGER.info("Sub process is a review or validate, hence adding the decision tag.");
						transitionSubProcessName = IS_REVIEW_REQUIRED;
					} else if (subProcessName.equalsIgnoreCase(VALIDATE_DOCUMENT_PLUGIN)) {
						transitionSubProcessName = IS_VALIDATION_REQUIRED;
					}
				}

				// Transition to transitionSubProcessName always
				jpdlStringBuffer.append(getSubProcessTransitionString(transitionSubProcessName));

				subProcess = transitionSubProcess;
			}
		}
		return subProcess;
	}

	private void createLastSubProcessTag(boolean isWorkflow, StringBuffer jpdlStringBuffer, ModuleJpdlPluginCreationInfo subProcess) {
		jpdlStringBuffer.append(getSubProcessString(subProcess.getSubProcessName(), subProcess.getSubProcessKey()));
		jpdlStringBuffer.append(getBatchInstanceIdParameterString());
		// if scripting plug-in,Append ScriptName and backUpFileName to string
		if (subProcess.isScriptingPlugin()) {
			jpdlStringBuffer.append(getScriptNameParameterString(subProcess.getScriptingFileName()));
			jpdlStringBuffer.append(getBackUpFileNameString(subProcess.getBackUpFileName()));
		}
		String subProcessName = subProcess.getSubProcessName();

		if (!(subProcessName.equalsIgnoreCase(WORKFLOW_STATUS_RUNNING) || subProcessName.equalsIgnoreCase(WORKFLOW_STATUS_FINISHED))
				&& isWorkflow) {
			// Event listener tag only when process is a workflow.
			LOGGER.info("Sub process is a workflow, hence adding the event listener tag.");
			jpdlStringBuffer.append(EVENT_LISTENER_TAG);
		}
		// Transition to transitionSubProcessName
		jpdlStringBuffer.append(getSubProcessTransitionString(END_CONSTANT));
	}

	private String getResumeOptionDecisionTagContent(List<ModuleJpdlPluginCreationInfo> subProcessNameList) {
		LOGGER.info("Preparing resume option decision tag.");
		StringBuffer decisionTagStringBuffer = new StringBuffer();
		decisionTagStringBuffer.append(DECISION_TAG);
		decisionTagStringBuffer.append(DECISION_HANDLER_CLASS);
		for (ModuleJpdlPluginCreationInfo moduleJpdlPluginCreationInfo : subProcessNameList) {

			if (!(moduleJpdlPluginCreationInfo.getSubProcessName().equalsIgnoreCase(WORKFLOW_STATUS_FINISHED))) {
				decisionTagStringBuffer.append(DECISION_TRANSITION_OPTION.replaceAll(DECISION_TRANSITION_PROCESS,
						moduleJpdlPluginCreationInfo.getSubProcessName()));
			}
		}
		decisionTagStringBuffer.append(DECISION_TAG_END);
		String resumeOptionsTag = decisionTagStringBuffer.toString();
		LOGGER.info("Complete resume option decision tag is " + resumeOptionsTag);
		return resumeOptionsTag;
	}

	private String getDecisionHandlerTagContent(List<ModuleJpdlPluginCreationInfo> subProcessNameList) {
		StringBuffer decisionHandlingTagStringBuffer = new StringBuffer();
		String decisionNameTag = WorkFlowConstants.EMPTY_STRING;
		String decisionHandlerClass = WorkFlowConstants.EMPTY_STRING;
		String decisionToNo = WorkFlowConstants.EMPTY_STRING;
		String decisionToYes = WorkFlowConstants.EMPTY_STRING;
		String decisionHandletag = WorkFlowConstants.EMPTY_STRING;

		LOGGER.info("Preparing decision handle tag.");

		for (ModuleJpdlPluginCreationInfo moduleJpdlPluginCreationInfo : subProcessNameList) {

			String subProcessName = moduleJpdlPluginCreationInfo.getSubProcessName();
			if (subProcessName.equals(REVIEW_DOCUMENT_PLUGIN)) {
				decisionNameTag = IS_REVIEW_REQUIRED;
				decisionHandlerClass = REVIEW_EVALUATION;
				int indexOfReview = subProcessNameList.indexOf(moduleJpdlPluginCreationInfo);
				if (indexOfReview < subProcessNameList.size() - 1) {
					ModuleJpdlPluginCreationInfo transitionProcess = subProcessNameList.get(indexOfReview + 1);
					decisionToNo = transitionProcess.getSubProcessName();
				} else {
					decisionToNo = END_CONSTANT;
				}
				decisionToYes = subProcessName;
				decisionHandletag = getDecisionHandleTagContent(decisionNameTag, decisionHandlerClass, decisionToNo, decisionToYes);
				decisionHandlingTagStringBuffer.append(decisionHandletag);
			} else if (subProcessName.equals(VALIDATE_DOCUMENT_PLUGIN)) {
				decisionNameTag = IS_VALIDATION_REQUIRED;
				decisionHandlerClass = VALIDATION_EVALUATION;
				int indexOfReview = subProcessNameList.indexOf(moduleJpdlPluginCreationInfo);
				if (indexOfReview < subProcessNameList.size() - 1) {
					ModuleJpdlPluginCreationInfo transitionProcess = subProcessNameList.get(indexOfReview + 1);
					decisionToNo = transitionProcess.getSubProcessName();
				} else {
					decisionToNo = END_CONSTANT;
				}
				decisionToYes = subProcessName;
				decisionHandletag = getDecisionHandleTagContent(decisionNameTag, decisionHandlerClass, decisionToNo, decisionToYes);
				decisionHandlingTagStringBuffer.append(decisionHandletag);
			}
		}
		String decisionHandleTag = decisionHandlingTagStringBuffer.toString();
		LOGGER.info("Complete decision handle tag is " + decisionHandleTag);
		return decisionHandleTag;
	}

	private String getDecisionHandleTagContent(String decisionNameTag, String decisionHandlerClass, String decisionToNo,
			String decisionToYes) {
		String decisionHandletag = DECISION_HANDLING_TAG;
		decisionHandletag = decisionHandletag.replace(DECISION_NAME_TAG, decisionNameTag).replace(DECISION_TAG_HANDLER_CLASS,
				decisionHandlerClass).replace(DECISION_TO_NO, decisionToNo).replace(DECISION_TO_YES, decisionToYes);
		return decisionHandletag;
	}

	/**
	 * API to create JPDL for a workflow/module given it's process name, list of sub process names and workflow path.
	 * 
	 * @param workflowPath {@link String}
	 * @param jpdlProcessName {@link ModuleJpdlPluginCreationInfo}
	 * @param subProcessNameList {@link List}< {@link ModuleJpdlPluginCreationInfo}>
	 * @param isWorkflow boolean
	 * @return {@link String} relative path of the created JPDL.
	 */
	@Override
	public String writeJPDL(String workflowPath, ModuleJpdlPluginCreationInfo jpdlProcessName,
			List<ModuleJpdlPluginCreationInfo> subProcessNameList, boolean isWorkflow) {

		LOGGER.info("Creating JPDL for " + jpdlProcessName);
		LOGGER.info("at " + workflowPath);
		String processDefinitionPath = WorkFlowConstants.EMPTY_STRING;
		String jpdlPath = WorkFlowConstants.EMPTY_STRING;
		FileWriter fstream = null;
		BufferedWriter out = null;
		try {

			if (!(jpdlProcessName.getSubProcessName().equalsIgnoreCase(WORKFLOW_STATUS_RUNNING) || jpdlProcessName.getSubProcessName()
					.equalsIgnoreCase(WORKFLOW_STATUS_FINISHED))) {
				jpdlPath = File.separator + jpdlProcessName.getSubProcessName() + FileType.JPDL.getExtensionWithDot();
				processDefinitionPath = workflowPath + jpdlPath;
				LOGGER.info("Complete JPDL path " + processDefinitionPath);
				fstream = new FileWriter(processDefinitionPath);
				out = new BufferedWriter(fstream);
				if (subProcessNameList != null) {
					LOGGER.info("Writing JPDL content..");
					String jpdlFileContent = getJPDLContentInStringForm(jpdlProcessName, subProcessNameList, isWorkflow);
					out.write(jpdlFileContent);
				}
			}
		} catch (IOException e) {
			LOGGER.error("Error while creating JPDL " + e.getMessage(), e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (fstream != null) {
					fstream.close();
				}
			} catch (IOException e) {
				LOGGER.error("Error while creating JPDL " + e.getMessage(), e);
			}
		}
		LOGGER.info("JPDL relative path " + jpdlPath);
		return jpdlPath;
	}
}
