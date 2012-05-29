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

package com.ephesoft.dcma.gwt.customWorkflow.client.view;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.da.property.PluginProperty;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.ui.table.Record;
import com.ephesoft.dcma.gwt.core.shared.PluginDetailsDTO;
import com.ephesoft.dcma.gwt.core.shared.comparator.PluginComparator;
import com.ephesoft.dcma.gwt.customWorkflow.client.i18n.CustomWorkflowConstants;
import com.ephesoft.dcma.gwt.customWorkflow.client.presenter.ViewAndAddPluginsPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;

@UiTemplate(value = "ViewAndAddPluginsView.ui.xml")
public class ViewAndAddPluginsView extends View<ViewAndAddPluginsPresenter> {

	private static int TABLE_ROW_COUNT = 15;

	private AllPluginsListView allPluginsListView;

	@UiField
	Label pluginsListingLabel;

	@UiField
	Button addNewPluginButton;

	@UiField
	Button addNewPluginHelpButton;

	@UiField
	Button dependenciesButton;

	@UiField
	protected LayoutPanel allPluginsListPanel;

	@UiField
	protected HorizontalPanel pluginButtonsPanel;

	@UiField
	DockLayoutPanel pluginsLayoutPanel;

	interface Binder extends UiBinder<DockLayoutPanel, ViewAndAddPluginsView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	public ViewAndAddPluginsView() {
		super();
		initWidget(binder.createAndBindUi(this));

		allPluginsListView = new AllPluginsListView();

		allPluginsListPanel.add(allPluginsListView.getAllPluginsListView());

		/* Add handlers */
		addCSSStyle();
		addFieldText();

	}

	public void showEntryView() {
		presenter.showEntryView();
	}

	private void addFieldText() {
		dependenciesButton.setText(CustomWorkflowConstants.DEPENDENCIES_CONSTANT);
		addNewPluginButton.setText(CustomWorkflowConstants.ADD_PLUGIN_STRING);
		addNewPluginHelpButton.setText(CustomWorkflowConstants.HELP_BUTTON);
		pluginsListingLabel.setText(CustomWorkflowConstants.PLUGIN_NAMES_STRING);
	}

	public void createPluginsList(List<PluginDetailsDTO> allPluginsList) {
		Order order = new Order(PluginProperty.NAME, true);
		PluginComparator pluginComparator = new PluginComparator(order);
		Collections.sort(allPluginsList, pluginComparator);

		List<Record> recordList = setPluginsList(allPluginsList);

		presenter.getAllPluginsListPresenter().getView().listView.initTable(recordList.size(), presenter, recordList.subList(0,
				TABLE_ROW_COUNT), false, false, null);

	}

	public void updatePluginsList(List<PluginDetailsDTO> allPluginsList, int totalCount, int startIndex, int lastIndex) {
		List<Record> recordList = setPluginsList(allPluginsList);
		presenter.getAllPluginsListPresenter().getView().listView.updateRecords(recordList.subList(startIndex, lastIndex), startIndex,
				totalCount);
	}

	public List<Record> setPluginsList(List<PluginDetailsDTO> allPluginsList) {
		List<Record> recordList = new LinkedList<Record>();

		if (allPluginsList != null) {

			for (PluginDetailsDTO pluginEntry : allPluginsList) {

				{
					Record record = new Record(pluginEntry.getIdentifier());
					record.addWidget(presenter.getAllPluginsListPresenter().getView().pluginName, new Label(pluginEntry
							.getPluginName()));
					record.addWidget(presenter.getAllPluginsListPresenter().getView().description, new Label(pluginEntry
							.getPluginDescription()));
					recordList.add(record);
				}

			}
		}
		return recordList;
	}

	private void addCSSStyle() {
		pluginButtonsPanel.setSpacing(CustomWorkflowConstants.SPACING_CONSTANT_10);
		pluginsListingLabel.setStyleName(CustomWorkflowConstants.BOLD_TEXT_STYLE);
	}

	@UiHandler("addNewPluginButton")
	public void onAddNewPluginClicked(ClickEvent clickEvent) {
		presenter.addNewPlugin();
	}

	@UiHandler("dependenciesButton")
	public void onDependenciesButtonClicked(ClickEvent clickEvent) {
		presenter.showDependenciesView();
	}

	@UiHandler("addNewPluginHelpButton")
	public void onAddNewPluginHelpClicked(ClickEvent clickEvent) {
		presenter.onAddNewPluginHelp();
	}

	/**
	 * @return the allPluginsListView
	 */
	public AllPluginsListView getAllPluginsListView() {
		return allPluginsListView;
	}

}
