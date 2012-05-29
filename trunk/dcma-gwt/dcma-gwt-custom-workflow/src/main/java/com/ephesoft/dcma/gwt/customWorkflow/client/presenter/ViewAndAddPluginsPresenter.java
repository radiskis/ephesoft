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

package com.ephesoft.dcma.gwt.customWorkflow.client.presenter;

import java.util.Collections;
import java.util.List;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.da.property.PluginProperty;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.PaginationListner;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.PluginDetailsDTO;
import com.ephesoft.dcma.gwt.core.shared.comparator.PluginComparator;
import com.ephesoft.dcma.gwt.customWorkflow.client.CustomWorkflowController;
import com.ephesoft.dcma.gwt.customWorkflow.client.view.ViewAndAddPluginsView;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.TextArea;

public class ViewAndAddPluginsPresenter extends AbstractCustomWorkflowPresenter<ViewAndAddPluginsView> implements PaginationListner {

	private static final int TABLE_ROW_COUNT = 15;
	private int startIndex;
	private int maxResult;
	private Order order;
	private final AllPluginsListPresenter allPluginsListPresenter;

	public ViewAndAddPluginsPresenter(CustomWorkflowController customWorkflowController, ViewAndAddPluginsView viewAndAddPluginsView) {
		super(customWorkflowController, viewAndAddPluginsView);

		this.allPluginsListPresenter = new AllPluginsListPresenter(getController(), view.getAllPluginsListView());
		view.getAllPluginsListView().listView.setTableRowCount(TABLE_ROW_COUNT);

	}

	@Override
	public void injectEvents(HandlerManager eventBus) {

	}

	@Override
	public void bind() {
		if (controller.getAllPluginsNameToDescriptionMap() != null) {
			setIndexes(0, view.getAllPluginsListView().listView.getTableRowCount(), null);
			getView().createPluginsList(controller.getAllPlugins());
		}
	}

	public void showEntryView() {

		controller.getCustomWorkflowManagementPresenter().getView().getCustomWorkflowEntryView().setVisible(true);

	}

	public void addNewPlugin() {
		controller.getCustomWorkflowManagementPresenter().showImportPluginView();
	}

	private void setIndexes(int startIndex2, int maxResult2, Order order2) {
		this.startIndex = startIndex2;
		this.maxResult = maxResult2;
		this.order = order2;
	}

	/**
	 * @return the allPluginsListPresenter
	 */
	public AllPluginsListPresenter getAllPluginsListPresenter() {
		return allPluginsListPresenter;
	}

	public void onAddNewPluginHelp() {
		ConfirmationDialogUtil.showConfirmationDialog("For adding a new plugin (NEW_PLUGIN):" + "\n"
				+ "a zip file(NEW_PLUGIN.zip) containing " + "\n" + "1. a jar(NEW_PLUGIN.jar) " + "\n" + "2. an xml(NEW_PLUGIN.xml) "
				+ "\n" + " must be selected", "Help", true);

	}

	public void showDependenciesView() {

		controller.getCustomWorkflowManagementPresenter().showDependenciesView();
	}

	@Override
	public void onPagination(int startIndex, int maxResult, Order order) {
		if (order == null) {
			order = new Order(PluginProperty.NAME, true);
		}
		PluginComparator pluginComparator = new PluginComparator(order);
		List<PluginDetailsDTO> allPluginsList = controller.getAllPlugins();

		Collections.sort(allPluginsList, pluginComparator);
		int totalSize = allPluginsList.size();
		int lastIndex = startIndex + maxResult;
		view.updatePluginsList(allPluginsList, totalSize, startIndex, Math.min(totalSize, lastIndex));
	}

	/**
	 * @return the startIndex
	 */
	public int getStartIndex() {
		return startIndex;
	}

	/**
	 * @param startIndex the startIndex to set
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * @return the maxResult
	 */
	public int getMaxResult() {
		return maxResult;
	}

	/**
	 * @param maxResult the maxResult to set
	 */
	public void setMaxResult(int maxResult) {
		this.maxResult = maxResult;
	}

	/**
	 * @return the order
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

}
