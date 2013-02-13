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
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.PluginJpdlCreationInfo;
import com.ephesoft.dcma.workflow.constant.WorkFlowConstants;

/**
 * This is service class for plugin JPDL creation.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.workflow.service.PluginJpdlCreationService
 */
public class PluginJpdlCreationServiceImpl implements PluginJpdlCreationService {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PluginJpdlCreationServiceImpl.class);

	/**
	 * workflowPath String.
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
	public void setWorkflowPath(String workflowPath) {
		this.workflowPath = workflowPath;
	}

	/**
	 * To get Sub Process String.
	 * 
	 * @param subProcessName String
	 * @param expression String
	 * @param method String
	 * @return String
	 */
	public String getSubProcessString(String subProcessName, String expression, String method) {
		LOGGER.info("Inserting sub process tag for: " + subProcessName + " with method: " + method + " & expression: " + expression);
		String subProcessJPDLString = PLUGIN_JPDL_STRING.replace(SUB_PROCESS_NAME, subProcessName).replace(EXPRESSION, expression)
				.replace(METHOD, method);
		return subProcessJPDLString;
	}

	private String getScriptNameParameterString() {
		LOGGER.info("Inserting script name parameter tag");
		return PLUGIN_JPDL_ARG_STRING.replace(ARGUEMENT, SCRIPT_NAME_CONSTANT);
	}

	private String getBackUpFileNameString() {
		LOGGER.info("Inserting back up file name parameter tag");
		return PLUGIN_JPDL_ARG_STRING.replace(ARGUEMENT, BACK_UP_FILE_NAME_CONSTANT);
	}

	private String getSubProcessTransitionString(String transitionName) {
		LOGGER.info("Inserting transition tag for transition to " + transitionName);
		return SUB_PROCESS_JPDL_PLUGIN_STRING_TRANSITION.replace(TRANSITION_SUB_PROCESS_NAME, transitionName);
	}

	private String getBatchInstanceIdParameterString() {
		LOGGER.info("Inserting batch instance id paramter tag");
		return PLUGIN_JPDL_ARG_STRING.replace(ARGUEMENT, BATCH_INSTANCE_ID_CONSTANT);
	}

	private String getNonScriptingParameterString(String arguement) {
		LOGGER.info("Inserting non scripting parameter tag");
		return PLUGIN_JPDL_NON_SCRIPTING_ARG_STRING.replace(ARGUEMENT, arguement);
	}

	/**
	 * To get JPDL Content in String Form.
	 * 
	 * @param JPDLProcessName String
	 * @param subProcessNameList List<PluginJpdlCreationInfo>
	 * @return String
	 */
	public String getJPDLContentInStringForm(String JPDLProcessName, List<PluginJpdlCreationInfo> subProcessNameList) {
		StringBuffer jpdlStringBuffer = new StringBuffer();
		LOGGER.info("Initializing JPDL with xml tag");
		jpdlStringBuffer.append(JPDL_XML_INITIALIZATION_STRING);

		LOGGER.info("Creating process name tag for JPDL with process naem: " + JPDLProcessName);
		jpdlStringBuffer.append(JPDL_PROCESS_START_STRING.replace(JPDL_PROCESS_NAME, JPDLProcessName));

		String firstSubProcessName = subProcessNameList.get(0).getSubProcessName();
		LOGGER.info("Creating start tag for sub process " + firstSubProcessName);
		jpdlStringBuffer.append(JPDL_PROCESS_EXECUTION_START.replace(SUB_PROCESS_NAME, firstSubProcessName));

		PluginJpdlCreationInfo subProcessName = null;
		for (PluginJpdlCreationInfo transitionSubProcessName : subProcessNameList) {
			if (subProcessName == null) {
				subProcessName = transitionSubProcessName;
			} else {
				jpdlStringBuffer.append(getSubProcessString(subProcessName.getSubProcessName(), subProcessName.getServiceName(),
						subProcessName.getMethodName()));

				// Append BIID to string always
				jpdlStringBuffer.append(getBatchInstanceIdParameterString());

				// if scripting plug-in,Append ScriptName and backUpFileName to string
				if (subProcessName.isScriptingPlugin()) {
					LOGGER.info(subProcessName.getSubProcessName() + " is a scripting plugin");
					jpdlStringBuffer.append(getScriptNameParameterString());
					jpdlStringBuffer.append(getBackUpFileNameString());
				} else {
					LOGGER.info(subProcessName.getSubProcessName() + " is not a scripting plugin");
				}

				// Transition to transitionSubProcessName always
				jpdlStringBuffer.append(getSubProcessTransitionString(transitionSubProcessName.getSubProcessName()));

				subProcessName = transitionSubProcessName;
			}
		}

		jpdlStringBuffer.append(getSubProcessString(subProcessName.getSubProcessName(), subProcessName.getServiceName(),
				subProcessName.getMethodName()));

		// Append string for last sub-process
		jpdlStringBuffer.append(getBatchInstanceIdParameterString());

		// if scripting plug-in,Append ScriptName and backUpFileName to string
		if (subProcessName.isScriptingPlugin()) {
			jpdlStringBuffer.append(getScriptNameParameterString());
			jpdlStringBuffer.append(getBackUpFileNameString());
		} else {
			jpdlStringBuffer.append(getNonScriptingParameterString(subProcessName.getSubProcessName()));
		}
		// Transition to transitionSubProcessName
		jpdlStringBuffer.append(getSubProcessTransitionString(END_CONSTANT));

		LOGGER.info("Inserting End tag");
		jpdlStringBuffer.append(JPDL_PROCESS_EXECUTION_END);
		jpdlStringBuffer.append(JPDL_PROCESS_END_STRING);
		return jpdlStringBuffer.toString();
	}

	/**
	 * API to create JPDL for plugin given it's process name, list of sub process names and workflow path.
	 * 
	 * @param workflowPath {@link String}
	 * @param jpdlProcessName {@link String}
	 * @param subProcessNameList {@link List}< {@link PluginJpdlCreationInfo}>
	 * @return {@link String} relative path of the created JPDL.
	 */
	@Override
	public String writeJPDL(String workflowPath, String JPDLProcessName, List<PluginJpdlCreationInfo> subProcessNameList) {
		LOGGER.info("Creating JPDL for " + JPDLProcessName);
		LOGGER.info("at " + workflowPath);
		String processDefinitionPath = WorkFlowConstants.EMPTY_STRING;
		String jpdlPath = WorkFlowConstants.EMPTY_STRING;
		FileWriter fstream = null;
		BufferedWriter out = null;
		try {
			{
				jpdlPath = "\\" + JPDLProcessName + FileType.JPDL.getExtensionWithDot();
				processDefinitionPath = workflowPath + jpdlPath;
				LOGGER.info("Complete JPDL path " + processDefinitionPath);
				fstream = new FileWriter(processDefinitionPath);
				out = new BufferedWriter(fstream);
				if (subProcessNameList != null) {
					LOGGER.info("Writing JPDL content..");
					String jpdlFileContent = getJPDLContentInStringForm(JPDLProcessName, subProcessNameList);
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
