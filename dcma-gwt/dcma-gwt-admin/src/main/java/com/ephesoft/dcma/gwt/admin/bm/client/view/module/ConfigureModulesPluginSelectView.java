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
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.module.ConfigureModulePluginsPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.view.MultipleSelectTwoSidedListBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * This class provides functionality to configure module plugin.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class ConfigureModulesPluginSelectView extends View<ConfigureModulePluginsPresenter> {

	/**
	 * SELECTED_PLUGINS String.
	 */
	private static final String SELECTED_PLUGINS = "Selected Plugins";

	/**
	 * AVAILABLE_PLUGINS String.
	 */
	private static final String AVAILABLE_PLUGINS = "Available Plugins";

	/**
	 * CANCEL String.
	 */
	private static final String CANCEL = "Cancel";

	/**
	 * BUTTON_TEXT_OK String.
	 */
	private static final String BUTTON_TEXT_OK = "Ok";

	/**
	 * RESET String.
	 */
	private static final String RESET = "Reset";

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<DockLayoutPanel, ConfigureModulesPluginSelectView> {
	}

	/**
	 * multipleSelectTwoSidedListBox MultipleSelectTwoSidedListBox.
	 */
	protected MultipleSelectTwoSidedListBox multipleSelectTwoSidedListBox;

	/**
	 * twoSidedListBoxHorizontalPanel HorizontalPanel.
	 */
	protected HorizontalPanel twoSidedListBoxHorizontalPanel;

	/**
	 * buttonsHorizontalPanel HorizontalPanel.
	 */
	@UiField
	protected HorizontalPanel buttonsHorizontalPanel;

	/**
	 * okButton Button.
	 */
	@UiField
	protected Button okButton;

	/**
	 * cancelButton Button.
	 */
	@UiField
	protected Button cancelButton;

	/**
	 * resetButton Button.
	 */
	@UiField
	protected Button resetButton;

	/**
	 * customWorkflowVerticalPanel DockLayoutPanel.
	 */
	@UiField
	protected DockLayoutPanel customWorkflowVerticalPanel;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * eventBus HandlerManager.
	 */
	private HandlerManager eventBus;

	/**
	 * Constructor.
	 */
	public ConfigureModulesPluginSelectView() {
		this(null);
	}

	/**
	 * Constructor.
	 * 
	 * @param eventBus HandlerManager
	 */
	public ConfigureModulesPluginSelectView(HandlerManager eventBus) {
		super();
		initWidget(BINDER.createAndBindUi(this));
		multipleSelectTwoSidedListBox = new MultipleSelectTwoSidedListBox(eventBus);
		customWorkflowVerticalPanel.addNorth(multipleSelectTwoSidedListBox, BatchClassManagementConstants.THREE_HUNDRED);

		addCSSStyle();
		addFieldText();
		multipleSelectTwoSidedListBox.getLeftHandSideListBox().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent changeEvent) {
				presenter.onPluginSelect();
			}
		});

		multipleSelectTwoSidedListBox.getRightHandSideListBox().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent changeEvent) {
				// On change
			}
		});

	}

	private void addFieldText() {
		okButton.setText(BUTTON_TEXT_OK);
		cancelButton.setText(CANCEL);
		resetButton.setText(RESET);
		multipleSelectTwoSidedListBox.getAvailableLabel().setText(AVAILABLE_PLUGINS);
		multipleSelectTwoSidedListBox.getSelectedLabel().setText(SELECTED_PLUGINS);
	}

	private void addCSSStyle() {

		buttonsHorizontalPanel.setSpacing(BatchClassManagementConstants.THIRTY);
		okButton.addStyleName("button-style");
		cancelButton.addStyleName("button-style");
		resetButton.addStyleName("button-style");

		buttonsHorizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		buttonsHorizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		customWorkflowVerticalPanel.addStyleName("mainPanelLayout");

	}

	/**
	 * To get Custom Workflow Vertical Panel.
	 * 
	 * @return the customWorkflowVerticalPanel
	 */
	public DockLayoutPanel getCustomWorkflowVerticalPanel() {
		return customWorkflowVerticalPanel;
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
		presenter.setBatchClassDTOPluginsList(getMultipleSelectTwoSidedListBox().getAllValuesMapFromList(
				getMultipleSelectTwoSidedListBox().getRightHandSideListBox()));
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
	 * To perform operations on reset click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("resetButton")
	public void onResetClicked(ClickEvent clickEvent) {
		presenter.bind();
	}

	/**
	 * To get Event Bus.
	 * 
	 * @return the eventBus
	 */
	public HandlerManager getEventBus() {
		return eventBus;
	}

	/**
	 * To set Event Bus.
	 * 
	 * @param eventBus HandlerManager
	 */
	public void setEventBus(HandlerManager eventBus) {
		this.eventBus = eventBus;
	}

}
