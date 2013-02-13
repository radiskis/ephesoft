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

package com.ephesoft.dcma.gwt.customworkflow.client.presenter.dependencies;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.da.property.DependencyProperty;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.PaginationListner;
import com.ephesoft.dcma.gwt.core.shared.DependencyDTO;
import com.ephesoft.dcma.gwt.core.shared.PluginDetailsDTO;
import com.ephesoft.dcma.gwt.core.shared.comparator.DependencyComparator;
import com.ephesoft.dcma.gwt.customworkflow.client.CustomWorkflowController;
import com.ephesoft.dcma.gwt.customworkflow.client.presenter.AbstractCustomWorkflowPresenter;
import com.ephesoft.dcma.gwt.customworkflow.client.view.dependencies.DependencyView;
import com.google.gwt.event.shared.HandlerManager;

public class DependencyPresenter extends AbstractCustomWorkflowPresenter<DependencyView> implements PaginationListner {

	private DependencyListPresenter dependencyListPresenter;

	public DependencyPresenter(CustomWorkflowController controller, DependencyView view) {
		super(controller, view);
		this.dependencyListPresenter = new DependencyListPresenter(controller, view.getDependencyListView());
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		/**
		 * Inject your events here
		 */
	}

	@Override
	public void bind() {

		if (controller.getAllPlugins() != null) {
			Map<Integer, String> pluginIndexToNameMap = controller.getPluginIndexToNameMap();
			// populate plugin names
			if (pluginIndexToNameMap == null) {
				pluginIndexToNameMap = new HashMap<Integer, String>(0);
			} else {
				pluginIndexToNameMap.clear();
			}
			int index = 0;
			for (PluginDetailsDTO pluginDetailsDTO : controller.getAllPlugins()) {
				pluginIndexToNameMap.put(index++, pluginDetailsDTO.getPluginName());
			}
			controller.setPluginIndexToNameMap(pluginIndexToNameMap);
			view.populateListBoxWithValuesMap(view.getPluginNames(), pluginIndexToNameMap);

			// populate dependencies of selected plugin
			getDependenciesList();
		}

	}

	/**
	 * 
	 */
	private void getDependenciesList() {
		int selectedIndex = Integer.parseInt(view.getPluginNames().getValue(view.getPluginNames().getSelectedIndex()));
		controller.getCustomWorkflowManagementPresenter().getDependencyManagementPresenter().getView().createDependenciesList(
				controller.getAllPlugins().get(selectedIndex).getDependencies());
	}

	public void showEditDependencyView() {
		controller.getCustomWorkflowManagementPresenter().getDependencyManagementPresenter().showEditDependenciesView();
	}

	public void onDeleteDependencyClicked() {
		controller.getCustomWorkflowManagementPresenter().getDependencyManagementPresenter().showDeleteDependenciesView();
	}

	public void onPluginChange(String pluginName) {
		getDependenciesList();
	}

	/**
	 * @return the dependencyListPresenter
	 */
	public DependencyListPresenter getDependencyListPresenter() {
		return dependencyListPresenter;
	}

	/**
	 * @param dependencyListPresenter the dependencyListPresenter to set
	 */
	public void setDependencyListPresenter(DependencyListPresenter dependencyListPresenter) {
		this.dependencyListPresenter = dependencyListPresenter;
	}

	public void showAddDependencyView() {
		controller.getCustomWorkflowManagementPresenter().getDependencyManagementPresenter().showAddDependenciesView();
	}

	@Override
	public void onPagination(int startIndex, int maxResult, Order order) {
		Order finalOrder = order;
		if (order == null) {
			finalOrder = new Order(DependencyProperty.DEPENDENCY_NAME, true);
		}
		DependencyComparator dependencyComparator = new DependencyComparator(finalOrder);
		int selectedIndex = Integer.parseInt(view.getPluginNames().getValue(view.getPluginNames().getSelectedIndex()));
		List<DependencyDTO> dependenciesList = controller.getAllPlugins().get(selectedIndex).getDependencies();

		Collections.sort(dependenciesList, dependencyComparator);

		int totalSize = dependenciesList.size();
		int lastIndex = startIndex + maxResult;
		getController().getCustomWorkflowManagementPresenter().getDependencyManagementPresenter().getView().updateDependenciesList(
				dependenciesList, totalSize, startIndex, Math.min(totalSize, lastIndex));
	}

}
