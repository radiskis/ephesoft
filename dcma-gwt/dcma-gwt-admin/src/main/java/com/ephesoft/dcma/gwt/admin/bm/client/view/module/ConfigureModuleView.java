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

package com.ephesoft.dcma.gwt.admin.bm.client.view.module;

import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.module.ConfigureModulePresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.view.MultipleSelectTwoSidedListBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class provides functionality to configure module.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class ConfigureModuleView extends View<ConfigureModulePresenter> {

	/**
	 * ADD_NEW_MODULE String.
	 */
	private static final String ADD_NEW_MODULE = "Add New Module";

	/**
	 * SELECTED_MODULES String.
	 */
	private static final String SELECTED_MODULES = "Selected Modules";

	/**
	 * AVAILABLE_MODULES String.
	 */
	private static final String AVAILABLE_MODULES = "Available Modules";

	/**
	 * BUTTON_STYLE String.
	 */
	private static final String BUTTON_STYLE = "button-style";

	/**
	 * RESET String.
	 */
	private static final String RESET = "Reset";

	/**
	 * BUTTON_TEXT_OK String.
	 */
	private static final String BUTTON_TEXT_OK = "Ok";

	/**
	 * CANCEL String.
	 */
	private static final String CANCEL = "Cancel";

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<DockLayoutPanel, ConfigureModuleView> {
	}

	/**
	 * multipleSelectTwoSidedListBox MultipleSelectTwoSidedListBox.
	 */
	protected MultipleSelectTwoSidedListBox multipleSelectTwoSidedListBox;

	/**
	 * buttonsVerticalPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel buttonsVerticalPanel;

	/**
	 * addNewModule Button.
	 */
	@UiField
	protected Button addNewModule;

	/**
	 * okButton Button.
	 */
	@UiField
	protected Button okButton;

	/**
	 * resetButton Button.
	 */
	@UiField
	protected Button resetButton;

	/**
	 * cancelButton Button.
	 */
	@UiField
	protected Button cancelButton;

	/**
	 * customWorkflowPanel DockLayoutPanel.
	 */
	@UiField
	protected DockLayoutPanel customWorkflowPanel;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Constructor.
	 */
	public ConfigureModuleView() {
		this(null);
	}

	/**
	 * Constructor.
	 * 
	 * @param eventBus HandlerManager
	 */
	public ConfigureModuleView(HandlerManager eventBus) {
		super();
		initWidget(BINDER.createAndBindUi(this));
		multipleSelectTwoSidedListBox = new MultipleSelectTwoSidedListBox(eventBus);

		customWorkflowPanel.addNorth(multipleSelectTwoSidedListBox, BatchClassManagementConstants.SEVENTY);
		addCSSStyle();
		addFieldText();

	}

	private void addFieldText() {
		okButton.setText(BUTTON_TEXT_OK);
		resetButton.setText(RESET);
		addNewModule.setText(ADD_NEW_MODULE);
		cancelButton.setText(CANCEL);
		multipleSelectTwoSidedListBox.getAvailableLabel().setText(AVAILABLE_MODULES);
		multipleSelectTwoSidedListBox.getSelectedLabel().setText(SELECTED_MODULES);
	}

	private void addCSSStyle() {
		addNewModule.addStyleName(BUTTON_STYLE);
		okButton.addStyleName(BUTTON_STYLE);
		resetButton.addStyleName(BUTTON_STYLE);
		cancelButton.addStyleName(BUTTON_STYLE);

		buttonsVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		buttonsVerticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		customWorkflowPanel.addStyleName("mainPanelLayout");
		multipleSelectTwoSidedListBox.addStyleName("custom-workflow-panel");

	}

	/**
	 * To get Multiple Select Two Sided List Box.
	 * 
	 * @return the multipleSelectTwoSidedListBox
	 */
	public MultipleSelectTwoSidedListBox getMultipleSelectTwoSidedListBox() {
		return multipleSelectTwoSidedListBox;
	}

	/**
	 * To perform operations on OK click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("okButton")
	public void onOkClicked(ClickEvent clickEvent) {
		presenter.setBatchClassDTOModulesList(getMultipleSelectTwoSidedListBox().getAllValuesMapFromList(
				getMultipleSelectTwoSidedListBox().getRightHandSideListBox()));
	}

	@UiHandler("resetButton")
	public void onResetClicked(ClickEvent clickEvent) {
		presenter.bind();
	}

	@UiHandler("addNewModule")
	public void onAddNewModuleClicked(ClickEvent clickEvent) {
		presenter.addNewModule();
	}

	/**
	 * To perform operations on cancel click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("cancelButton")
	public void onCancelClicked(ClickEvent clickEvent) {
		presenter.onCancelButtonClicked();
	}

	/**
	 * To get OK button.
	 * 
	 * @return the okButton
	 */
	public Button getOkButton() {
		return okButton;
	}

	/**
	 * To set OK button.
	 * 
	 * @param okButton Button
	 */
	public void setOkButton(Button okButton) {
		this.okButton = okButton;
	}

	/**
	 * To get Reset Button.
	 * 
	 * @return the resetButton
	 */
	public Button getResetButton() {
		return resetButton;
	}

	/**
	 * To set Reset Button.
	 * 
	 * @param resetButton Button
	 */
	public void setResetButton(Button resetButton) {
		this.resetButton = resetButton;
	}

	/**
	 * To set Multiple Select Two Sided ListBox.
	 * 
	 * @param multipleSelectTwoSidedListBox MultipleSelectTwoSidedListBox
	 */
	public void setMultipleSelectTwoSidedListBox(MultipleSelectTwoSidedListBox multipleSelectTwoSidedListBox) {
		this.multipleSelectTwoSidedListBox = multipleSelectTwoSidedListBox;
	}

	/**
	 * To get Custom Workflow Panel.
	 * 
	 * @return the customWorkflowPanel
	 */
	public DockLayoutPanel getCustomWorkflowPanel() {
		return customWorkflowPanel;
	}

	/**
	 * To set Custom Workflow Panel.
	 * 
	 * @param customWorkflowPanel DockLayoutPanel
	 */
	public void setCustomWorkflowPanel(DockLayoutPanel customWorkflowPanel) {
		this.customWorkflowPanel = customWorkflowPanel;
	}

}
