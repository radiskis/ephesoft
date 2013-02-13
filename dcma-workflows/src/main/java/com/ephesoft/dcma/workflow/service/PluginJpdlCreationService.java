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

import com.ephesoft.dcma.core.common.PluginJpdlCreationInfo;

/**
 * This is service class for plugin JPDL creation.
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.workflow.service.PluginJpdlCreationServiceImpl
 */
public interface PluginJpdlCreationService {

	/**
	 * JPDL_XML_INITIALIZATION_STRING String.
	 */
	String JPDL_XML_INITIALIZATION_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	/**
	 * SUB_PROCESS_NAME String.
	 */
	String SUB_PROCESS_NAME = "???SUB_PROCESS_NAME???";
	
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
	 * EXPRESSION String.
	 */
	String EXPRESSION = "???EXPRESSION???";
	
	/**
	 * METHOD String.
	 */
	String METHOD = "???METHOD???";
	
	/**
	 * ARGUEMENT String.
	 */
	String ARGUEMENT = "???ARG???";

	/**
	 * PLUGIN_JPDL_STRING String.
	 */
	String PLUGIN_JPDL_STRING = "<java continue=\"async\" expr=\"#{"+EXPRESSION+"}\" method=\""+METHOD+"\" name=\""+ SUB_PROCESS_NAME +"\">";
	
	/**
	 * PLUGIN_JPDL_ARG_STRING String.
	 */
	String PLUGIN_JPDL_ARG_STRING = "<arg> <object expr=\"#{"+ ARGUEMENT +"}\"/> </arg>";
	
	/**
	 * PLUGIN_JPDL_NON_SCRIPTING_ARG_STRING String.
	 */
	String PLUGIN_JPDL_NON_SCRIPTING_ARG_STRING = "<arg> <object expr=\""+ ARGUEMENT +"\"/> </arg>";
	
	/**
	 * SUB_PROCESS_JPDL_PLUGIN_STRING_TRANSITION String.
	 */
	String SUB_PROCESS_JPDL_PLUGIN_STRING_TRANSITION = "<transition to=\"" + TRANSITION_SUB_PROCESS_NAME + "\"/></java>";

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
	 * BATCH_INSTANCE_ID_CONSTANT String.
	 */
	String BATCH_INSTANCE_ID_CONSTANT  = "batchInstanceID";
	
	/**
	 * SCRIPT_NAME_CONSTANT String.
	 */
	String SCRIPT_NAME_CONSTANT  = "backUpFileName";
	
	/**
	 * BACK_UP_FILE_NAME_CONSTANT String.
	 */
	String BACK_UP_FILE_NAME_CONSTANT  = "scriptName";


	/**
	 * API to create JPDL for plugin given it's process name, list of sub process names and workflow path.
	 * @param workflowPath {@link String}
	 * @param jpdlProcessName {@link String}
	 * @param subProcessNameList {@link List}< {@link PluginJpdlCreationInfo}>
	 * @return {@link String} relative path of the created JPDL.
	 */
	String writeJPDL(String workflowPath,String jpdlProcessName, List<PluginJpdlCreationInfo> subProcessNameList);
}
