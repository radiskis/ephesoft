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

package com.ephesoft.dcma.gwt.core.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PluginXmlDTO implements IsSerializable {

	private String jarName;
	private String pluginDesc;
	private String pluginWorkflowName;
	private String pluginName;
	private String serviceName;
	private String methodName;
	private Boolean isScriptPlugin;
	private String scriptFileName;
	private String backUpFileName;
	private List<PluginConfigXmlDTO> configXmlDTOs;
	private List<PluginDependencyXmlDTO> dependencyXmlDTOs;
	

	
	/**
	 * @return the dependencyXmlDTOs
	 */
	public List<PluginDependencyXmlDTO> getDependencyXmlDTOs() {
		return dependencyXmlDTOs;
	}

	
	/**
	 * @param dependencyXmlDTOs the dependencyXmlDTOs to set
	 */
	public void setDependencyXmlDTOs(List<PluginDependencyXmlDTO> dependencyXmlDTOs) {
		this.dependencyXmlDTOs = dependencyXmlDTOs;
	}

	/**
	 * @return the jarName
	 */
	public String getJarName() {
		return jarName;
	}

	/**
	 * @param jarName the jarName to set
	 */
	public void setJarName(String jarName) {
		this.jarName = jarName;
	}

	/**
	 * @return the pluginDesc
	 */
	public String getPluginDesc() {
		return pluginDesc;
	}

	/**
	 * @param pluginDesc the pluginDesc to set
	 */
	public void setPluginDesc(String pluginDesc) {
		this.pluginDesc = pluginDesc;
	}

	/**
	 * @return the pluginWorkflowName
	 */
	public String getPluginWorkflowName() {
		return pluginWorkflowName;
	}

	/**
	 * @param pluginWorkflowName the pluginWorkflowName to set
	 */
	public void setPluginWorkflowName(String pluginWorkflowName) {
		this.pluginWorkflowName = pluginWorkflowName;
	}

	/**
	 * @return the pluginName
	 */
	public String getPluginName() {
		return pluginName;
	}

	/**
	 * @param pluginName the pluginName to set
	 */
	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * @param methodName the methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * @return the isScriptPlugin
	 */
	public Boolean getIsScriptPlugin() {
		return isScriptPlugin;
	}

	/**
	 * @param isScriptPlugin the isScriptPlugin to set
	 */
	public void setIsScriptPlugin(Boolean isScriptPlugin) {
		this.isScriptPlugin = isScriptPlugin;
	}

	/**
	 * @return the scriptFileName
	 */
	public String getScriptFileName() {
		return scriptFileName;
	}

	/**
	 * @param scriptFileName the scriptFileName to set
	 */
	public void setScriptFileName(String scriptFileName) {
		this.scriptFileName = scriptFileName;
	}

	/**
	 * @return the backUpFileName
	 */
	public String getBackUpFileName() {
		return backUpFileName;
	}

	/**
	 * @param backUpFileName the backUpFileName to set
	 */
	public void setBackUpFileName(String backUpFileName) {
		this.backUpFileName = backUpFileName;
	}

	/**
	 * @return the configXmlDTOs
	 */
	public List<PluginConfigXmlDTO> getConfigXmlDTOs() {
		return configXmlDTOs;
	}

	/**
	 * @param configXmlDTOs the configXmlDTOs to set
	 */
	public void setConfigXmlDTOs(List<PluginConfigXmlDTO> configXmlDTOs) {
		this.configXmlDTOs = configXmlDTOs;
	}

}
