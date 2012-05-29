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

package com.ephesoft.dcma.gwt.customWorkflow.client;

import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.gwt.core.client.Controller;
import com.ephesoft.dcma.gwt.core.shared.DependencyDTO;
import com.ephesoft.dcma.gwt.core.shared.PluginDetailsDTO;
import com.ephesoft.dcma.gwt.customWorkflow.client.presenter.CustomWorkflowManagementPresenter;
import com.ephesoft.dcma.gwt.customWorkflow.client.view.CustomWorkflowManagementView;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Composite;

public class CustomWorkflowController extends Controller {

	private CustomWorkflowManagementPresenter customWorkflowManagementPresenter;
	private CustomWorkflowManagementView customWorkflowManagementView;
	private DependencyDTO selectedDependencyDTO;
	private String selectedPlugin;
	
	private List<PluginDetailsDTO> allPlugins;
	
	Map<Integer, String> pluginIndexToNameMap;
	
	private List<String> dependenciesType;

	private Map<String, String> allPluginsNameToDescriptionMap;
	
	public CustomWorkflowController(HandlerManager eventBus, CustomWorkflowServiceAsync rpcService) {
		super(eventBus, rpcService);
	}

	@Override
	public Composite createView() {

		this.customWorkflowManagementView = new CustomWorkflowManagementView();
		this.customWorkflowManagementPresenter = new CustomWorkflowManagementPresenter(this, this.customWorkflowManagementView);

		return this.customWorkflowManagementView;
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {

	}

	@Override
	public void refresh() {

	}

	@Override
	public CustomWorkflowServiceAsync getRpcService() {
		return (CustomWorkflowServiceAsync) super.getRpcService();
	}

	/**
	 * @return the customWorkflowManagementPresenter
	 */
	public CustomWorkflowManagementPresenter getCustomWorkflowManagementPresenter() {
		return customWorkflowManagementPresenter;
	}

	/**
	 * @return the customWorkflowManagementView
	 */
	public CustomWorkflowManagementView getCustomWorkflowManagementView() {
		return customWorkflowManagementView;
	}


	/**
	 * @return the selectedDependencyDTO
	 */
	public DependencyDTO getSelectedDependencyDTO() {
		return selectedDependencyDTO;
	}

	
	/**
	 * @param selectedDependencyDTO the selectedDependencyDTO to set
	 */
	public void setSelectedDependencyDTO(DependencyDTO selectedDependencyDTO) {
		this.selectedDependencyDTO = selectedDependencyDTO;
	}

	
	/**
	 * @return the selectedPlugin
	 */
	public String getSelectedPlugin() {
		return selectedPlugin;
	}

	
	/**
	 * @param selectedPlugin the selectedPlugin to set
	 */
	public void setSelectedPlugin(String selectedPlugin) {
		this.selectedPlugin = selectedPlugin;
	}

	
	/**
	 * @param customWorkflowManagementPresenter the customWorkflowManagementPresenter to set
	 */
	public void setCustomWorkflowManagementPresenter(CustomWorkflowManagementPresenter customWorkflowManagementPresenter) {
		this.customWorkflowManagementPresenter = customWorkflowManagementPresenter;
	}

	
	/**
	 * @param customWorkflowManagementView the customWorkflowManagementView to set
	 */
	public void setCustomWorkflowManagementView(CustomWorkflowManagementView customWorkflowManagementView) {
		this.customWorkflowManagementView = customWorkflowManagementView;
	}

	
	/**
	 * @return the allPlugins
	 */
	public List<PluginDetailsDTO> getAllPlugins() {
		return allPlugins;
	}

	
	/**
	 * @param allPlugins the allPlugins to set
	 */
	public void setAllPlugins(List<PluginDetailsDTO> allPlugins) {
		this.allPlugins = allPlugins;
	}

	
	/**
	 * @return the pluginIndexToNameMap
	 */
	public Map<Integer, String> getPluginIndexToNameMap() {
		return pluginIndexToNameMap;
	}

	
	/**
	 * @param pluginIndexToNameMap the pluginIndexToNameMap to set
	 */
	public void setPluginIndexToNameMap(Map<Integer, String> pluginIndexToNameMap) {
		this.pluginIndexToNameMap = pluginIndexToNameMap;
	}

	
	/**
	 * @return the dependenciesType
	 */
	public List<String> getDependenciesType() {
		return dependenciesType;
	}

	
	/**
	 * @param dependenciesType the dependenciesType to set
	 */
	public void setDependenciesType(List<String> dependenciesType) {
		this.dependenciesType = dependenciesType;
	}

	
	/**
	 * @return the allPluginsNameToDescriptionMap
	 */
	public Map<String, String> getAllPluginsNameToDescriptionMap() {
		return allPluginsNameToDescriptionMap;
	}

	
	/**
	 * @param allPluginsNameToDescriptionMap the allPluginsNameToDescriptionMap to set
	 */
	public void setAllPluginsNameToDescriptionMap(Map<String, String> allPluginsNameToDescriptionMap) {
		this.allPluginsNameToDescriptionMap = allPluginsNameToDescriptionMap;
	}

	
}
