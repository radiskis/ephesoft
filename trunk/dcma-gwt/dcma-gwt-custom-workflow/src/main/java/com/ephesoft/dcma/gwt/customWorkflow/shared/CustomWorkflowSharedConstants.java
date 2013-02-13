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

package com.ephesoft.dcma.gwt.customworkflow.shared;


public interface CustomWorkflowSharedConstants {
	
	String PLUGIN_IN_USE_MESSAGE = "plugin_in_use_cannot_delete";

	String APPLICATION_CONTEXT_ENTRY_ERROR_MESSAGE = " Error making application context entry for the plugin.";
	
	String INVALID_XML_CONTENT_MESSAGE = "Invalid xml content. A mandatory tag is missing.";
	
	String NEW_WORKFLOWS_BASE_PATH = "newWorkflows.basePath";
	
	String META_INF_DCMA_WORKFLOWS_PROPERTIES = "dcma-workflows\\dcma-workflows";

	String XML_INDENT_AMOUNT = "{http://xml.apache.org/xslt}indent-amount";
	
	String YES = "yes";
	
	String CLASSPATH_META_INF = "classpath:/META-INF/";
	
	String RESOURCE = "resource";
	
	String EMPTY_STRING = "";
	
	String PLUGINS = "plugins";
	
	String LIB = "lib";
	
	String DCMA_HOME = "DCMA_HOME";
	
	String WEB_INF = "WEB-INF";
	
	String OR_CONSTANT = "/";
	
	String AND = ",";
	
	String APPLICATION_CONTEXT_PATH_XML = "applicationContext.xml";
	
	String VERSION = "1.0.0.0";
	
	String PLUGIN_EXPR = "/plugin";
	
	String JAR_NAME_EXPR = "jar-name";
	
	String PLUGIN_DESC_EXPR = "plugin-desc";
	
	String PLUGIN_WORKFLOW_NAME_EXPR = "plugin-workflow-name";
	
	String PLUGIN_NAME_EXPR = "plugin-name";
	
	String SERVICE_NAME_EXPR = "plugin-service-instance";
	
	String METHOD_NAME_EXPR = "method-name";
	
	String IS_SCRIPT_PLUGIN_EXPR = "is-scripting";
	
	String SCRIPT_FILE_NAME_EXPR = "script-name";
	
	String BACK_UP_FILE_NAME_EXPR = "back-up-file-name";
	
	String PLUGIN_PROPERTY_EXPR = "plugin-properties/plugin-property";
	
	String PLUGIN_PROPERTY_NAME_EXPR = "name";
	
	String PLUGIN_PROPERTY_TYPE_EXPR = "type";
	
	String PLUGIN_PROPERTY_DESC_EXPR = "description";
	
	String PLUGIN_PROPERTY_SAMPLE_VALUES_EXPR = "sample-values";
	
	String PLUGIN_PROPERTY_SAMPLE_VALUE_EXPR = "sample-value";
	
	String PLUGIN_PROPERTY_IS_MANDETORY_EXPR = "is-mandatory";
	
	String PLUGIN_PROPERTY_IS_MULTI_VALUES_EXPR = "is-multivalue";
	
	String DEPENDENCIES_LIST_DEPENDENCY = "dependencies/dependency";
	
	String PLUGIN_DEPENDENCY_TYPE = "type-of-dependency";
	
	String PLUGIN_DEPENDENCY_VALUE = "dependency-value";
	
	String APPLICATION_CONTEXT_PATH = "application-context-path";
	
	String BEANS_TAG = "beans";
	
	String IMPORT_TAG = "import";
	
	String OVERRIDE_EXISTING = "override-existing";
	
	String OPERATION = "operation";
	
	String ADD_OPERATION = "add";
	
	String UPDATE_OPERATION = "update";
	
	String DELETE_OPERATION = "delete";
}
