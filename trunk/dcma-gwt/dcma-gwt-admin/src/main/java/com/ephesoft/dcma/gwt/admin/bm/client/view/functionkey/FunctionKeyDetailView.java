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

package com.ephesoft.dcma.gwt.admin.bm.client.view.functionkey;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.functionkey.FunctionKeyDetailPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class provides functionality to show function key detail.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class FunctionKeyDetailView extends View<FunctionKeyDetailPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, FunctionKeyDetailView> {
	}

	/**
	 * methodNameLabel Label.
	 */
	@UiField
	protected Label methodNameLabel;

	/**
	 * methodName Label.
	 */
	@UiField
	protected Label methodName;

	/**
	 * methodDescriptionLabel Label.
	 */
	@UiField
	protected Label methodDescriptionLabel;

	/**
	 * methodDescription Label.
	 */
	@UiField
	protected Label methodDescription;

	/**
	 * keyNameLabel Label.
	 */
	@UiField
	protected Label keyNameLabel;

	/**
	 * keyName Label.
	 */
	@UiField
	protected Label keyName;

	/**
	 * scrollPanel ScrollPanel.
	 */
	@UiField
	protected ScrollPanel scrollPanel;

	/**
	 * editFunctionKeyPropertiesButton Button.
	 */
	@UiField
	protected Button editFunctionKeyPropertiesButton;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Constructor.
	 */
	public FunctionKeyDetailView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		methodNameLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.FUNCTION_KEY_METHOD_NAME)
				+ AdminConstants.COLON);
		methodDescriptionLabel.setText(LocaleDictionary.get().getConstantValue(
				BatchClassManagementConstants.FUNCTION_KEY_METHOD_DESCRIPTION)
				+ AdminConstants.COLON);
		keyNameLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.FUNCTION_KEY_KEY_NAME)
				+ AdminConstants.COLON);
		methodNameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		keyNameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		methodDescriptionLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		editFunctionKeyPropertiesButton.setText(AdminConstants.EDIT_BUTTON);
		scrollPanel.setStyleName(AdminConstants.SCROLL_PANEL_HEIGHT);
	}

	/**
	 * To set Method Name.
	 * 
	 * @param name String
	 */
	public void setMethodName(String name) {
		this.methodName.setText(name);
	}

	/**
	 * To set Method Description.
	 * 
	 * @param description String
	 */
	public void setMethodDescription(String description) {
		this.methodDescription.setText(description);
	}

	/**
	 * To set Key Name.
	 * 
	 * @param name String
	 */
	public void setKeyName(String name) {
		this.keyName.setText(name);
	}

	/**
	 * To get Edit Function Key Properties Button.
	 * 
	 * @return Button
	 */
	public Button getEditFunctionKeyPropertiesButton() {
		return editFunctionKeyPropertiesButton;
	}

	/**
	 * To perform operations on Edit Function Key Properties Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("editFunctionKeyPropertiesButton")
	public void onEditFunctionKeyPropertiesButtonClick(ClickEvent clickEvent) {
		presenter.getController().getBatchClassManagementPresenter().getFunctionKeyViewPresenter()
				.onEditFunctionKeyPropertiesButtonClicked();
	}

}
