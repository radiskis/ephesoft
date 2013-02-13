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

package com.ephesoft.dcma.gwt.customworkflow.client.view.dependencies;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.customworkflow.client.i18n.CustomWorkflowConstants;
import com.ephesoft.dcma.gwt.customworkflow.client.presenter.dependencies.EditDependencyPresenter;
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
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EditDependencyView extends View<EditDependencyPresenter> {

	@UiField
	protected DockLayoutPanel editDependencyDockLayoutPanel;

	@UiField
	protected VerticalPanel editDependencyPanel;

	@UiField
	protected VerticalPanel buttonsPanel;

	@UiField
	protected Label pluginLabel;

	@UiField
	protected Label dependencyTypeLabel;

	@UiField
	protected Label dependenciesLabel;

	@UiField
	protected Label pluginNamesList;

	@UiField
	protected ListBox dependencyTypeList;

	@UiField
	protected HorizontalPanel dependenciesListHorizontalPanel;

	protected ListBox dependenciesList;

	@UiField
	protected Button okButton;

	@UiField
	protected Button cancelButton;

	@UiField
	protected Button andButton;

	@UiField
	protected Button orButton;

	@UiField
	protected TextArea dependenciesTextArea;

	interface Binder extends UiBinder<DockLayoutPanel, EditDependencyView> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);

	public EditDependencyView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		dependenciesList = new ListBox();
		addUiComponentText();

		addCssStyle();
		okButton.setEnabled(true);
	}

	/**
	 * 
	 */
	private void addUiComponentText() {
		okButton.setText(LocaleDictionary.get().getConstantValue(CustomWorkflowConstants.OK_CONSTANT));
		cancelButton.setText(LocaleDictionary.get().getConstantValue(CustomWorkflowConstants.RESET_BUTTON));
		andButton.setText(LocaleDictionary.get().getConstantValue(CustomWorkflowConstants.AND_CONSTANT));
		orButton.setText(LocaleDictionary.get().getConstantValue(CustomWorkflowConstants.OR_CONSTANT));
		pluginLabel.setText(LocaleDictionary.get().getConstantValue(CustomWorkflowConstants.PLUGIN_LABEL_CONSTANT));
		dependencyTypeLabel.setText(LocaleDictionary.get().getConstantValue(CustomWorkflowConstants.DEPENDENCY_TYPE_LABEL_CONSTANT));
		dependenciesLabel.setText(LocaleDictionary.get().getConstantValue(CustomWorkflowConstants.DEPENDENCIES_CONSTANT));
	}

	/**
	 * 
	 */
	private void addCssStyle() {
		editDependencyPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		editDependencyPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		andButton.setSize(CustomWorkflowConstants._50PX, CustomWorkflowConstants._25PX);
		orButton.setSize(CustomWorkflowConstants._50PX, CustomWorkflowConstants._25PX);
		buttonsPanel.setSpacing(CustomWorkflowConstants.SPACING_15);
		dependenciesList.setVisibleItemCount(CustomWorkflowConstants.VISIBLE_ITEM_10);
		dependenciesListHorizontalPanel.add(dependenciesList);
		dependenciesTextArea.setSize(CustomWorkflowConstants._280PX, CustomWorkflowConstants._147PX);
		dependenciesTextArea.setTextAlignment(TextArea.ALIGN_JUSTIFY);
		editDependencyDockLayoutPanel.addStyleName(CustomWorkflowConstants.CUSTOM_WORKFLOW_PANEL);
		editDependencyDockLayoutPanel.addStyleName(CustomWorkflowConstants.MAIN_PANEL_LAYOUT);

		pluginLabel.addStyleName(CustomWorkflowConstants.STRONG_LABEL);
		dependencyTypeLabel.addStyleName(CustomWorkflowConstants.STRONG_LABEL);
		dependenciesLabel.addStyleName(CustomWorkflowConstants.STRONG_LABEL);
		pluginNamesList.addStyleName(CustomWorkflowConstants.STRONG_LABEL);
	}

	@UiHandler("okButton")
	public void onOkButtonClicked(ClickEvent clickEvent) {
		presenter.onOkButtonClicked();
	}

	@UiHandler("cancelButton")
	public void onCancelButtonClicked(ClickEvent clickEvent) {
		presenter.onCancelButtonClicked();
	}

	@UiHandler("andButton")
	public void onAndButtonClicked(ClickEvent clickEvent) {
		presenter.onAndButtonClicked();
	}

	@UiHandler("orButton")
	public void onOrButtonClicked(ClickEvent clickEvent) {
		presenter.onOrButtonClicked();
	}

	@UiHandler("dependencyTypeList")
	public void onDependencyTypeChange(ChangeEvent changeEvent) {
		presenter.onDependencyTypeChange();
	}

	/**
	 * @return the okButton
	 */
	public Button getOkButton() {
		return okButton;
	}

	public void populateListBoxWithValues(ListBox listBox, List<String> values) {

		listBox.clear();
		if (values != null) {
			for (String value : values) {
				listBox.addItem(value);
			}
		}
	}

	public void populateListBoxWithValuesMap(ListBox listBox, Map<Integer, String> values) {

		listBox.clear();
		if (values != null) {
			int index = 0;
			for (Entry<Integer, String> pluginEntry : values.entrySet()) {
				listBox.addItem(pluginEntry.getValue(), pluginEntry.getKey().toString());
				listBox.getElement().getElementsByTagName(CustomWorkflowConstants.OPTION).getItem(index++).setTitle(
						pluginEntry.getValue());
			}
		}
	}

	/**
	 * @return the pluginNamesList
	 */
	public Label getPluginNamesList() {
		return pluginNamesList;
	}

	/**
	 * @return the dependencyTypeList
	 */
	public ListBox getDependencyTypeList() {
		return dependencyTypeList;
	}

	/**
	 * @param dependencyTypeList the dependencyTypeList to set
	 */
	public void setDependencyTypeList(ListBox dependencyTypeList) {
		this.dependencyTypeList = dependencyTypeList;
	}

	/**
	 * @return the dependenciesList
	 */
	public ListBox getDependenciesList() {
		return dependenciesList;
	}

	/**
	 * @param dependenciesList the dependenciesList to set
	 */
	public void setDependenciesList(ListBox dependenciesList) {
		this.dependenciesList = dependenciesList;
	}

	/**
	 * @return the dependenciesTextArea
	 */
	public TextArea getDependenciesTextArea() {
		return dependenciesTextArea;
	}

	/**
	 * @param dependenciesTextArea the dependenciesTextArea to set
	 */
	public void setDependenciesTextArea(TextArea dependenciesTextArea) {
		this.dependenciesTextArea = dependenciesTextArea;
	}

	/**
	 * @return the andButton
	 */
	public Button getAndButton() {
		return andButton;
	}

	/**
	 * @param andButton the andButton to set
	 */
	public void setAndButton(Button andButton) {
		this.andButton = andButton;
	}

	/**
	 * @return the orButton
	 */
	public Button getOrButton() {
		return orButton;
	}

	/**
	 * @param orButton the orButton to set
	 */
	public void setOrButton(Button orButton) {
		this.orButton = orButton;
	}

}
