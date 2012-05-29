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

package com.ephesoft.dcma.gwt.customWorkflow.client.presenter.dependencies;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.ephesoft.dcma.gwt.core.shared.DependencyDTO;
import com.ephesoft.dcma.gwt.customWorkflow.client.CustomWorkflowController;
import com.ephesoft.dcma.gwt.customWorkflow.client.presenter.AbstractCustomWorkflowPresenter;
import com.ephesoft.dcma.gwt.customWorkflow.client.presenter.CustomWorkflowManagementPresenter;
import com.ephesoft.dcma.gwt.customWorkflow.client.view.dependencies.EditDependencyView;
import com.google.gwt.event.shared.HandlerManager;

public class EditDependencyPresenter extends AbstractCustomWorkflowPresenter<EditDependencyView> {

	public EditDependencyPresenter(CustomWorkflowController controller, EditDependencyView view) {
		super(controller, view);
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {

	}

	@Override
	public void bind() {
		if (controller.getDependenciesType() != null) {
			// view.populateListBoxWithValuesMap(view.getPluginNamesList(), controller.getPluginIndexToNameMap());
			view.populateListBoxWithValues(view.getDependencyTypeList(), controller.getDependenciesType());
			view.getDependenciesList().setSelectedIndex(-1);
			setDependenciesTextArea("");
			getController().getCustomWorkflowManagementPresenter().getDependencyManagementPresenter()
					.enableItemsOnDependencyTypeChange(true);
			//getController().getCustomWorkflowManagementPresenter().getDependencyManagementPresenter().toggleOkButtonEnable(false);
		}
	}

	/**
	 * 
	 */
	public void setAvailableDependenciesList() {
		Map<Integer, String> listOfAvailableDependencies = new HashMap<Integer, String>(controller.getPluginIndexToNameMap());
		String pluginName = view.getPluginNamesList().getText();
		int pluginIndex = getKeyForValue(listOfAvailableDependencies, pluginName);
		listOfAvailableDependencies.remove(pluginIndex);

		view.populateListBoxWithValuesMap(view.getDependenciesList(), listOfAvailableDependencies);

	}

	/**
	 * @param listOfAvailableDependencies
	 * @param pluginName
	 * @return
	 */
	private int getKeyForValue(Map<Integer, String> listOfAvailableDependencies, String pluginName) {
		int pluginIndex = -1;
		if (listOfAvailableDependencies.containsValue(pluginName)) {
			for (Entry<Integer, String> dependency : listOfAvailableDependencies.entrySet()) {
				if (dependency.getValue().equals(pluginName)) {
					pluginIndex = dependency.getKey();
					break;
				}
			}
		}
		return pluginIndex;
	}

	public void onOkButtonClicked() {
		controller.getCustomWorkflowManagementPresenter().getDependencyManagementPresenter().updatePluginDependency();
	}

	public void onCancelButtonClicked() {
		DependencyDTO selectedDependencyDTO = controller.getSelectedDependencyDTO();
		if (selectedDependencyDTO == null) {
			bind();
		} else {
			getController().getCustomWorkflowManagementPresenter().getDependencyManagementPresenter().setSelectedValuesInEditView(
					selectedDependencyDTO);
		}
	}

	public void onAndButtonClicked() {
		controller.getCustomWorkflowManagementPresenter().getDependencyManagementPresenter().addToDependenciesList(
				CustomWorkflowManagementPresenter.AND);
	}

	public void onOrButtonClicked() {

		controller.getCustomWorkflowManagementPresenter().getDependencyManagementPresenter().addToDependenciesList(
				CustomWorkflowManagementPresenter.OR);

	}

	public void onValidateButtonClicked() {
		controller.getCustomWorkflowManagementPresenter().getDependencyManagementPresenter().validateCurrentDependencies();
	}

	public void onPluginChange() {
		setAvailableDependenciesList();
		setDependenciesTextArea("");
	}

	private void setDependenciesTextArea(String string) {
		view.getDependenciesTextArea().setText(string);
	}

	public void onDependencyTypeChange() {
		controller.getCustomWorkflowManagementPresenter().getDependencyManagementPresenter().onDependencyTypeChange();
	}

	public void onDependenciesChange() {
		//getController().getCustomWorkflowManagementPresenter().getDependencyManagementPresenter().toggleOkButtonEnable(false);
	}

}
