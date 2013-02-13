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

/**
 * 
 * This class is responsible for creating the workflow Jpdl with respect to Grid Computing Batch class.
 * 
 * @author ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.workflow.service.GridWorkflowCreationService
 */
public class GridWorkflowCreationServiceImpl implements GridWorkflowCreationService {

	/**
	 * LOGGER for logging the information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(GridWorkflowCreationServiceImpl.class);

	/**
	 * Workflow deploy path to be read from the properties file.
	 */
	private String workflowPath;

	/**
	 * To get Workflow Path.
	 * 
	 * @return the workflowPath
	 */
	public String getWorkflowPath() {
		return workflowPath;
	}

	/**
	 * To set Workflow Path.
	 * 
	 * @param workflowPath String
	 */
	public void setWorkflowPath(final String workflowPath) {
		this.workflowPath = workflowPath;
	}

	private String getSubProcessString(final String subProcessName, final String subProcessKey) {
		LOGGER.info("Inserting sub process tag for " + subProcessName + " having sub process key as " + subProcessKey);
		String subProcessJPDLString = SUB_PROCESS_JPDL_PLUGIN_STRING.replace(SUB_PROCESS_NAME, subProcessName).replace(
				SUB_PROCESS_KEY, subProcessKey);
		return subProcessJPDLString;
	}

	private String getSubProcessTransitionString(final String transitionName) {
		LOGGER.info("Inserting transition tag for transition to " + transitionName);
		String transitionString = SUB_PROCESS_JPDL_PLUGIN_STRING_TRANSITION.replace(TRANSITION_SUB_PROCESS_NAME, transitionName);

		return transitionString;
	}

	private String getBatchInstanceIdParameterString() {
		LOGGER.info("Inserting batch instance id paramter tag.");
		return PARAMETERS_JPDL_EXPR_BIID;
	}

	private String getIsModuleRemoteParameterString() {
		LOGGER.info("Inserting Is Module Remote paramter tag.");
		return PARAMETERS_JPDL_EXPR_REMOTE;
	}

	private String getJPDLContentInStringForm(final ModuleJpdlPluginCreationInfo jpdlProcessName,
			final List<ModuleJpdlPluginCreationInfo> subProcessNameList, final boolean isWorkflow) {
		StringBuilder jpdlStringBuffer = new StringBuilder();
		LOGGER.info("Initializing JPDL with xml tag");
		jpdlStringBuffer.append(JPDL_XML_INITIALIZATION_STRING);

		LOGGER.info("Creating process name tag for JPDL with process name: " + jpdlProcessName);
		jpdlStringBuffer.append(JPDL_PROCESS_START_STRING.replace(JPDL_PROCESS_NAME, jpdlProcessName.getSubProcessName()));

		jpdlStringBuffer.append(getMainSubProcessTags(subProcessNameList, isWorkflow));
		jpdlStringBuffer.append(getRemoteSubProcessTags(subProcessNameList, isWorkflow));
		jpdlStringBuffer.append(getDecisionRemoteHandlerTagContent(subProcessNameList));

		LOGGER.info("Inserting End tag");
		jpdlStringBuffer.append(JPDL_PROCESS_EXECUTION_END);
		jpdlStringBuffer.append(JPDL_PROCESS_END_STRING);
		return jpdlStringBuffer.toString();
	}

	private String getRemoteSubProcessTags(final List<ModuleJpdlPluginCreationInfo> subProcessNameList, final boolean isWorkflow) {
		StringBuilder remoteSubProcessBuilder = new StringBuilder();
		// ModuleJpdlPluginCreationInfo subProcess = null;
		for (ModuleJpdlPluginCreationInfo subProcess : subProcessNameList) {
			String subProcessName = subProcess.getSubProcessName();
			if ((!(subProcessName.equalsIgnoreCase(WORKFLOW_STATUS_RUNNING) || subProcessName
					.equalsIgnoreCase(WORKFLOW_STATUS_FINISHED)))
					&& isWorkflow) {
				StringBuilder subProcessNameBuilder = new StringBuilder();
				subProcessNameBuilder.append(IS_CONSTANT);
				subProcessNameBuilder.append(subProcessName);
				subProcessNameBuilder.append(MODULE_REMOTE_CONSTANT);
				remoteSubProcessBuilder.append(getSubProcessString(subProcessNameBuilder.toString(), WORKFLOW_CONTINUE_CHECK));
				// Append BIID to string always
				remoteSubProcessBuilder.append(getBatchInstanceIdParameterString());
				remoteSubProcessBuilder.append(getIsModuleRemoteParameterString());
				StringBuilder transProcessNameBuilder = new StringBuilder();
				transProcessNameBuilder.append(subProcessName);
				transProcessNameBuilder.append(REMOTE_CHECK);
				// Transition to transitionSubProcessName always
				remoteSubProcessBuilder.append(getSubProcessTransitionString(transProcessNameBuilder.toString()));
			}
		}
		return remoteSubProcessBuilder.toString();
	}

	private String getMainSubProcessTags(final List<ModuleJpdlPluginCreationInfo> subProcessNameList, final boolean isWorkflow) {
		StringBuilder jpdlStringBuffer = new StringBuilder();
		if (subProcessNameList.size() > 0) {

			String startSubProcessName = subProcessNameList.get(0).getSubProcessName();
			LOGGER.info("Creating start tag for sub process " + startSubProcessName);

			if (isWorkflow) {
				LOGGER.info("Creating JPDL for a workflow hence adding resume options tag.");
				jpdlStringBuffer.append(JPDL_PROCESS_EXECUTION_START.replace(SUB_PROCESS_NAME, RESUME_OPTION));
				String decisionTagString = getResumeOptionDecisionTagContent(subProcessNameList);
				jpdlStringBuffer.append(decisionTagString);
			}

			ModuleJpdlPluginCreationInfo subProcess = null;
			for (ModuleJpdlPluginCreationInfo transitionSubProcess : subProcessNameList) {
				if (subProcess == null) {
					subProcess = transitionSubProcess;
				} else {
					String subProcessName = subProcess.getSubProcessName();
					String subProcessKey = subProcess.getSubProcessKey();

					jpdlStringBuffer.append(getSubProcessString(subProcessName, subProcessKey));

					// Append BIID to string always
					jpdlStringBuffer.append(getBatchInstanceIdParameterString());

					String transitionSubProcessName = transitionSubProcess.getSubProcessName();
					if (isWorkflow
							&& (!(subProcessName.equalsIgnoreCase(WORKFLOW_STATUS_RUNNING) || subProcessName
									.equalsIgnoreCase(WORKFLOW_STATUS_FINISHED)))) {
						// Event listener tag only when process is a workflow.
						LOGGER.info("Sub process is a workflow, hence adding the event listener tag.");
						jpdlStringBuffer.append(EVENT_LISTENER_TAG);
					}

					String transitionName = transitionSubProcessName;

					if (!(transitionSubProcessName.equalsIgnoreCase(WORKFLOW_STATUS_RUNNING) || transitionSubProcessName
							.equalsIgnoreCase(WORKFLOW_STATUS_FINISHED))) {
						StringBuilder stringBuilder = new StringBuilder();
						stringBuilder.append(IS_CONSTANT);
						stringBuilder.append(transitionSubProcessName);
						stringBuilder.append(MODULE_REMOTE_CONSTANT);
						transitionName = stringBuilder.toString();
					}
					// Transition to transitionSubProcessName always
					jpdlStringBuffer.append(getSubProcessTransitionString(transitionName));

					// get is remote module sub process tag

					// get remote decision node

					subProcess = transitionSubProcess;
				}
			}
			jpdlStringBuffer.append(getlastMainSubProcessTag(false, subProcess));
			jpdlStringBuffer.append(getlastMainSubProcessTag(false, new ModuleJpdlPluginCreationInfo(WORKFLOW_STATUS_REMOTE)));
			jpdlStringBuffer.append(getlastMainSubProcessTag(false, new ModuleJpdlPluginCreationInfo(WORKFLOW_STATUS_ERROR)));

		} else {
			jpdlStringBuffer.append(JPDL_PROCESS_EXECUTION_START.replace(SUB_PROCESS_NAME, END_CONSTANT));
		}
		return jpdlStringBuffer.toString();
	}

	private String getlastMainSubProcessTag(final boolean isWorkflow, final ModuleJpdlPluginCreationInfo subProcess) {
		StringBuilder subProcessBuilder = new StringBuilder();
		String subProcessName = subProcess.getSubProcessName();
		// Append string for last sub-process
		subProcessBuilder.append(getSubProcessString(subProcessName, subProcess.getSubProcessKey()));
		subProcessBuilder.append(getBatchInstanceIdParameterString());
		if (isWorkflow
				&& (!(subProcessName.equalsIgnoreCase(WORKFLOW_STATUS_RUNNING) || subProcessName
						.equalsIgnoreCase(WORKFLOW_STATUS_FINISHED)))) {
			// Event listener tag only when process is a workflow.
			LOGGER.info("Sub process is a workflow, hence adding the event listener tag.");
			subProcessBuilder.append(EVENT_LISTENER_TAG);
		}
		// Transition to transitionSubProcessName
		subProcessBuilder.append(getSubProcessTransitionString(END_CONSTANT));

		return subProcessBuilder.toString();
	}

	private String getResumeOptionDecisionTagContent(final List<ModuleJpdlPluginCreationInfo> subProcessNameList) {
		LOGGER.info("Preparing resume option decision tag.");
		StringBuilder decisionTagStringBuffer = new StringBuilder();
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

	private String getDecisionRemoteHandlerTagContent(final List<ModuleJpdlPluginCreationInfo> subProcessNameList) {
		StringBuilder decisionHandlingTagStringBuffer = new StringBuilder();
		String decisionNameTag = EMPTY_STRING;
		String decisionHandlerClass = EMPTY_STRING;
		String decisionToNo = EMPTY_STRING;
		String decisionToYes = EMPTY_STRING;
		String decisionToInvalid = EMPTY_STRING;
		String decisionHandletag = EMPTY_STRING;

		LOGGER.info("Preparing decision handle tag.");

		for (ModuleJpdlPluginCreationInfo moduleJpdlPluginCreationInfo : subProcessNameList) {
			String subProcessName = moduleJpdlPluginCreationInfo.getSubProcessName();
			if (!(subProcessName.equalsIgnoreCase(WORKFLOW_STATUS_RUNNING) || subProcessName
					.equalsIgnoreCase(WORKFLOW_STATUS_FINISHED))) {

				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(subProcessName);
				stringBuilder.append(REMOTE_CHECK);
				decisionNameTag = stringBuilder.toString();
				decisionHandlerClass = DECISION_REMOTE_HANDLER_CLASS;
				decisionToNo = subProcessName;
				decisionToYes = WORKFLOW_STATUS_REMOTE;
				decisionToInvalid = WORKFLOW_STATUS_ERROR;
				decisionHandletag = getDecisionRemoteHandleTagContent(decisionNameTag, decisionHandlerClass, decisionToNo,
						decisionToYes, decisionToInvalid);
				decisionHandlingTagStringBuffer.append(decisionHandletag);
			}
		}
		String decisionHandleTag = decisionHandlingTagStringBuffer.toString();
		LOGGER.info("Complete decision handle tag is " + decisionHandleTag);
		return decisionHandleTag;
	}

	private String getDecisionRemoteHandleTagContent(final String decisionNameTag, final String decisionHandlerClass,
			final String decisionToNo, final String decisionToYes, final String decisionToInvalid) {

		String decisionHandletag = DECISION_REMOTE_HANDLING_TAG;
		String replacedName = decisionHandletag.replace(DECISION_NAME_TAG, decisionNameTag);
		String replacedClass = replacedName.replace(DECISION_TAG_HANDLER_CLASS, decisionHandlerClass);
		String replacedDecision = replacedClass.replace(DECISION_TO_NO, decisionToNo);
		String replacedDecisionYEs = replacedDecision.replace(DECISION_TO_YES, decisionToYes);
		decisionHandletag = replacedDecisionYEs.replace(DECISION_TO_INVALID, decisionToInvalid);
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
	public String writeJPDL(final String workflowPath, final ModuleJpdlPluginCreationInfo jpdlProcessName,
			final List<ModuleJpdlPluginCreationInfo> subProcessNameList, final boolean isWorkflow) {

		LOGGER.info("Creating JPDL for " + jpdlProcessName);
		LOGGER.info("at " + workflowPath);
		String processDefinitionPath = EMPTY_STRING;
		String jpdlPath = EMPTY_STRING;
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
