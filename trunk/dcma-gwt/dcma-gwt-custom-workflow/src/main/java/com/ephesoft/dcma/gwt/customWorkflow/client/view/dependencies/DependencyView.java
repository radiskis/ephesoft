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

package com.ephesoft.dcma.gwt.customWorkflow.client.view.dependencies;

import java.util.Map;
import java.util.Map.Entry;

import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.customWorkflow.client.i18n.CustomWorkflowConstants;
import com.ephesoft.dcma.gwt.customWorkflow.client.presenter.dependencies.DependencyPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ListBox;

public class DependencyView extends View<DependencyPresenter> {

	private static final String BUTTONS_PANEL_LAYOUT = "buttonsPanelLayout";

	private static final String MAIN_PANEL_LAYOUT = "mainPanelLayout";

	public static int TABLE_ROW_COUNT = 15;
	
	private final DependencyListView dependencyListView;

	@UiField
	protected Button editDependencyButton;

	@UiField
	protected Button addDependencyButton;

	@UiField
	protected Button deleteDependencyButton;

	@UiField
	protected LayoutPanel dependencyListPanel;

	@UiField
	protected HorizontalPanel dependenciesButtonPanel;

	@UiField
	Label pluginLabel;

	@UiField
	ListBox pluginNames;

	@UiField
	DockLayoutPanel dependencyLayoutPanel;
	

	interface Binder extends UiBinder<DockLayoutPanel, DependencyView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	private static final String TWENTY_PIXEL = "20px";

	public DependencyView() {
		super();
		initWidget(binder.createAndBindUi(this));

		dependencyListView = new DependencyListView();

		dependencyListPanel.add(dependencyListView.listView);

		addUIComponentsText();

		addCssStyle();
	}

	/**
	 * 
	 */
	private void addUIComponentsText() {
		addDependencyButton.setText(CustomWorkflowConstants.ADD_CONSTANT);
		editDependencyButton.setText(CustomWorkflowConstants.EDIT_CONSTANT);
		deleteDependencyButton.setText(CustomWorkflowConstants.DELETE_CONSTANT);
		pluginLabel.setText(CustomWorkflowConstants.PLUGIN_LABEL_CONSTANT);
		
	}

	/**
	 * 
	 */
	private void addCssStyle() {
		addDependencyButton.setHeight(TWENTY_PIXEL);
		editDependencyButton.setHeight(TWENTY_PIXEL);
		deleteDependencyButton.setHeight(TWENTY_PIXEL);
		dependenciesButtonPanel.setSpacing(5);
		dependenciesButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		dependenciesButtonPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		dependencyLayoutPanel.addStyleName(MAIN_PANEL_LAYOUT);
		dependenciesButtonPanel.addStyleName(BUTTONS_PANEL_LAYOUT);
	}

	@UiHandler("editDependencyButton")
	void onEditDependencyButtonClicked(ClickEvent clickEvent) {
		presenter.showEditDependencyView();
	}

	@UiHandler("addDependencyButton")
	void onAddDependencyButtonClicked(ClickEvent clickEvent) {
		presenter.showAddDependencyView();
	}

	@UiHandler("deleteDependencyButton")
	void onDeleteDependencyButtonClicked(ClickEvent clickEvent) {
		presenter.onDeleteDependencyClicked();
	}

	@UiHandler("pluginNames")
	void onPluginChange(ChangeEvent changeEvent) {
		presenter.onPluginChange(pluginNames.getValue(pluginNames.getSelectedIndex()));
	}

	
	/**
	 * @return the dependencyListView
	 */
	public DependencyListView getDependencyListView() {
		return dependencyListView;
	}

	public void populateListBoxWithValuesMap(ListBox listBox, Map<Integer, String> values) {

		listBox.clear();
		if (values != null) {
			for (Entry<Integer, String> pluginEntry : values.entrySet()) {
				listBox.addItem(pluginEntry.getValue(), pluginEntry.getKey().toString());
			}
		}
	}

	/**
	 * @return the pluginNames
	 */
	public ListBox getPluginNames() {
		return pluginNames;
	}

}
