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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.da.property.DependencyProperty;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.ui.table.Record;
import com.ephesoft.dcma.gwt.core.shared.DependencyDTO;
import com.ephesoft.dcma.gwt.core.shared.comparator.DependencyComparator;
import com.ephesoft.dcma.gwt.customWorkflow.client.i18n.CustomWorkflowConstants;
import com.ephesoft.dcma.gwt.customWorkflow.client.presenter.dependencies.DependencyManagementPresenter;
import com.ephesoft.dcma.gwt.customWorkflow.client.view.CustomWorkflowBreadCrumbView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ListBox;

public class DependencyManagementView extends View<DependencyManagementPresenter> {

	@UiField
	CustomWorkflowBreadCrumbView workflowBreadCrumbView;

	@UiField
	DependencyView dependencyView;

	@UiField
	EditDependencyView editDependencyView;

	@UiField
	LayoutPanel layoutPanel;

	@UiField
	HorizontalPanel dependencyBottomButtonsPanel;

	@UiField
	Button saveButton;

	@UiField
	Button applyButton;

	@UiField
	Button cancelButton;

	interface Binder extends UiBinder<DockLayoutPanel, DependencyManagementView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	public DependencyManagementView() {
		super();
		initWidget(binder.createAndBindUi(this));

		addUIComponentsText();

		addCssStyle();
	}

	/**
	 * 
	 */
	private void addUIComponentsText() {
		saveButton.setText(CustomWorkflowConstants.SAVE_BUTTON);
		applyButton.setText(CustomWorkflowConstants.APPLY_BUTTON);
		cancelButton.setText(CustomWorkflowConstants.CANCEL_BUTTON);

	}

	/**
	 * 
	 */
	private void addCssStyle() {
		dependencyBottomButtonsPanel.setSpacing(CustomWorkflowConstants.SPACING_CONSTANT_10);
	}

	public void createDependenciesList(Collection<DependencyDTO> dependencyDTOs) {
		Order order = new Order(DependencyProperty.DEPENDENCY_NAME, true);

		DependencyComparator dependencyComparator = new DependencyComparator(order);

		Collections.sort(new ArrayList<DependencyDTO>(dependencyDTOs), dependencyComparator);
		List<Record> recordList = setDependeciesList(dependencyDTOs);
		int recordListSize = recordList.size();
		int visibleRowCount = Math.min(recordListSize, DependencyView.TABLE_ROW_COUNT);
		presenter.getDependencyPresenter().getDependencyListPresenter().getView().listView.initTable(recordListSize, presenter
				.getDependencyPresenter(), recordList.subList(0, visibleRowCount), true, false, presenter.getDependencyPresenter().getDependencyListPresenter());

	}

	public void updateDependenciesList(Collection<DependencyDTO> dependencyDTOs, int totalCount, int startIndex, int lastIndex) {
		List<Record> recordList = setDependeciesList(dependencyDTOs);

		presenter.getDependencyPresenter().getDependencyListPresenter().getView().listView.updateRecords(recordList.subList(
				startIndex, lastIndex), startIndex, totalCount);
	}

	List<Record> setDependeciesList(Collection<DependencyDTO> dependencyDTOs) {
		List<Record> recordList = new LinkedList<Record>();

		if (dependencyDTOs != null) {

			for (DependencyDTO dependencyDTO : dependencyDTOs) {
				if (!dependencyDTO.isDeleted()) {
					Record record = new Record(dependencyDTO.getIdentifier());
					record.addWidget(presenter.getDependencyPresenter().getDependencyListPresenter().getView().pluginName, new Label(
							dependencyDTO.getPluginDTO().getPluginName()));
					record.addWidget(presenter.getDependencyPresenter().getDependencyListPresenter().getView().dependencyType,
							new Label(dependencyDTO.getDependencyType()));
					record.addWidget(presenter.getDependencyPresenter().getDependencyListPresenter().getView().dependencies,
							new Label((dependencyDTO.getDependencies())));
					recordList.add(record);
				}
			}
		}
		return recordList;
	}

	@UiHandler("saveButton")
	void onSaveClicked(ClickEvent clickEvent) {
		presenter.onSaveClicked();
	}

	@UiHandler("applyButton")
	void onApplyClicked(ClickEvent clickEvent) {
		presenter.onApplyClicked();
	}

	@UiHandler("cancelButton")
	void onCancelClicked(ClickEvent clickEvent) {
		presenter.onCancelClicked();
	}

	public void showDependencyView() {
		layoutPanel.clear();
		dependencyView.setVisible(true);
		layoutPanel.add(dependencyView);
	}

	public void showEditDependencyView() {
		layoutPanel.clear();
		editDependencyView.setVisible(true);
		layoutPanel.add(editDependencyView);

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
	 * @return the workflowBreadCrumbView
	 */
	public CustomWorkflowBreadCrumbView getWorkflowBreadCrumbView() {
		return workflowBreadCrumbView;
	}

	/**
	 * @return the dependencyView
	 */
	public DependencyView getDependencyView() {
		return dependencyView;
	}

	/**
	 * @return the editDependencyView
	 */
	public EditDependencyView getEditDependencyView() {
		return editDependencyView;
	}

	/**
	 * @return the layoutPanel
	 */
	public LayoutPanel getLayoutPanel() {
		return layoutPanel;
	}
}
