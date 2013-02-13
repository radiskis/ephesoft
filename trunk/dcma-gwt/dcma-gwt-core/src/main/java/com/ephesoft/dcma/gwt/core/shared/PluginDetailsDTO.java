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

package com.ephesoft.dcma.gwt.core.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PluginDetailsDTO implements IsSerializable {

	private String identifier;
	private String pluginName;
	private String pluginDescription;
	private String pluginWorkflowName;
	private String scriptName;
	private String pluginInformation;
	private List<DependencyDTO> dependencies;
	private boolean dirty;
	
	/**
	 * @return the dependencies
	 */
	public List<DependencyDTO> getDependencies() {
		return dependencies;
	}

	
	/**
	 * @param dependencies the dependencies to set
	 */
	public void setDependencies(List<DependencyDTO> dependencies) {
		this.dependencies = dependencies;
	}

	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getPluginName() {
		return pluginName;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	public String getPluginDescription() {
		return pluginDescription;
	}

	public void setPluginDescription(String pluginDescription) {
		this.pluginDescription = pluginDescription;
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
	 * @return the scriptName
	 */
	public String getScriptName() {
		return scriptName;
	}

	/**
	 * @param scriptName the scriptName to set
	 */
	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}
	
	/**
	 * @return the pluginInformation
	 */
	public String getPluginInformation() {
		return pluginInformation;
	}


	/**
	 * @param pluginDetailedDecription the pluginInformation to set
	 */
	public void setPluginInformation(String pluginInformation) {
		this.pluginInformation = pluginInformation;
	}

	public DependencyDTO getDependencyDTOByIdentifier(PluginDetailsDTO pluginDetailsDTO, String identifier)
	{
		DependencyDTO dependencyDTO = new DependencyDTO();
		for (DependencyDTO dependency : pluginDetailsDTO.getDependencies()) {
			if (dependency.getIdentifier().equals(identifier)) {
				dependencyDTO = dependency;
				break;
			}
		}
		return dependencyDTO;
	}


	
	/**
	 * @return the isDirty
	 */
	public boolean isDirty() {
		return dirty;
	}


	
	/**
	 * @param dirty the isDirty to set
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
}
