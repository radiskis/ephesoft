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

package com.ephesoft.dcma.gwt.admin.bm.client.view.email;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.email.EditEmailPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.validator.ValidatableWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EditEmailView extends View<EditEmailPresenter> {

	interface Binder extends UiBinder<VerticalPanel, EditEmailView> {
	}

	@UiField
	Label userNameLabel;
	@UiField
	Label userNameStar;
	@UiField
	TextBox userName;

	@UiField
	Label passwordLabel;
	@UiField
	Label passwordStar;
	@UiField
	TextBox password;

	@UiField
	Label serverNameLabel;
	@UiField
	Label serverNameStar;
	@UiField
	TextBox serverName;

	@UiField
	Label serverTypeLabel;
	@UiField
	Label serverTypeStar;
	@UiField
	TextBox serverType;

	@UiField
	Label folderNameLabel;
	@UiField
	Label folderNameStar;
	@UiField
	TextBox folderName;

	@UiField
	Label isSSLLabel;
	// @UiField
	// Label isSSLStar;
	@UiField
	CheckBox isSSL;

	@UiField
	Label portNumberLabel;
	@UiField
	Label portNumberStar;
	@UiField
	TextBox portNumber;

	@UiField
	Button saveButton;
	@UiField
	Button cancelButton;

	private ValidatableWidget<TextBox> validateUserNameTextBox;
	private ValidatableWidget<TextBox> validatePasswordTextBox;
	private ValidatableWidget<TextBox> validateServerNameTextBox;
	private ValidatableWidget<TextBox> validateServerTypeTextBox;
	private ValidatableWidget<TextBox> validateFolderNameTextBox;
	private ValidatableWidget<TextBox> validatePortNumberTextBox;

	@UiField
	VerticalPanel editEmailViewPanel;

	private static final Binder binder = GWT.create(Binder.class);

	public EditEmailView() {
		initWidget(binder.createAndBindUi(this));
		saveButton.setText(AdminConstants.OK_BUTTON);
		cancelButton.setText(AdminConstants.CANCEL_BUTTON);

		createValidatableWidgets();

		editEmailViewPanel.setSpacing(5);

		userNameLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.USERNAME) + AdminConstants.COLON);
		passwordLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.PASSWORD) + AdminConstants.COLON);
		serverNameLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.SERVER_NAME)
				+ AdminConstants.COLON);
		serverTypeLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.SERVER_TYPE)
				+ AdminConstants.COLON);
		folderNameLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.FOLDER_NAME)
				+ AdminConstants.COLON);
		isSSLLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.ISSSL) + AdminConstants.COLON);
		portNumberLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.PORTNUMBER)
				+ AdminConstants.COLON);

		userNameStar.setText(AdminConstants.STAR);
		passwordStar.setText(AdminConstants.STAR);
		serverNameStar.setText(AdminConstants.STAR);
		serverTypeStar.setText(AdminConstants.STAR);
		folderNameStar.setText(AdminConstants.STAR);
		portNumberStar.setText(AdminConstants.STAR);
		portNumberStar.setVisible(Boolean.FALSE);
		// isSSLStar.setText(AdminConstants.STAR);

		userNameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		passwordLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		serverNameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		serverTypeLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		folderNameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		isSSLLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		portNumberLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);

		userNameStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		passwordStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		serverNameStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		serverTypeStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		folderNameStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		portNumberStar.setStyleName(AdminConstants.FONT_RED_STYLE);

		isSSL.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				portNumberStar.setVisible(event.getValue());
			}
		});
		// isSSLStar.setStyleName(AdminConstants.FONT_RED_STYLE);

	}

	public Label getPortNumberStar() {
		return portNumberStar;
	}

	@UiHandler("saveButton")
	void onSaveClicked(ClickEvent clickEvent) {
		presenter.onSave();
	}

	@UiHandler("cancelButton")
	void onCancelClicked(ClickEvent clickEvent) {
		presenter.onCancel();
	}

	public void setUsername(String name) {
		this.userName.setValue(name);
	}

	public String getUsername() {
		return this.userName.getValue();
	}

	public void setPassword(String password) {
		this.password.setValue(password);
	}

	public String getPassword() {
		return this.password.getValue();
	}

	public String getServerName() {
		return this.serverName.getValue();
	}

	public void setServerName(String serverName) {
		this.serverName.setValue(serverName);
	}

	public String getServerType() {
		return serverType.getValue();
	}

	public void setServerType(String serverType) {
		this.serverType.setValue(serverType);
	}

	public String getFolderName() {
		return folderName.getValue();
	}

	public void setFolderName(String folderName) {
		this.folderName.setValue(folderName);
	}

	public ValidatableWidget<TextBox> getValidateUserNameTextBox() {
		return validateUserNameTextBox;
	}

	public ValidatableWidget<TextBox> getValidatePasswordTextBox() {
		return validatePasswordTextBox;
	}

	public ValidatableWidget<TextBox> getValidateServerNameTextBox() {
		return validateServerNameTextBox;
	}

	public ValidatableWidget<TextBox> getValidateServerTypeTextBox() {
		return validateServerTypeTextBox;
	}

	public ValidatableWidget<TextBox> getValidateFolderNameTextBox() {
		return validateFolderNameTextBox;
	}

	public ValidatableWidget<TextBox> getValidatePortNumberTextBox() {
		return validatePortNumberTextBox;
	}

	public TextBox getUserNameTextBox() {
		return this.userName;
	}

	public TextBox getPasswordTextBox() {
		return this.password;
	}

	public TextBox getServerNameTextBox() {
		return this.serverName;
	}

	public TextBox getServerTypeTextBox() {
		return this.serverType;
	}

	public TextBox getFolderNameTextBox() {
		return this.folderName;
	}

	public void createValidatableWidgets() {
		validateUserNameTextBox = new ValidatableWidget<TextBox>(userName);
		validateUserNameTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateUserNameTextBox.toggleValidDateBox();
			}
		});

		validatePasswordTextBox = new ValidatableWidget<TextBox>(password);
		validatePasswordTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validatePasswordTextBox.toggleValidDateBox();
			}
		});

		validateServerNameTextBox = new ValidatableWidget<TextBox>(serverName);
		validateServerNameTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateServerNameTextBox.toggleValidDateBox();
			}
		});

		validateServerTypeTextBox = new ValidatableWidget<TextBox>(serverType);
		validateServerTypeTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateServerTypeTextBox.toggleValidDateBox();
			}
		});

		validateFolderNameTextBox = new ValidatableWidget<TextBox>(folderName);
		validateFolderNameTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateFolderNameTextBox.toggleValidDateBox();
			}
		});

		validatePortNumberTextBox = new ValidatableWidget<TextBox>(portNumber);
		validatePortNumberTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validatePortNumberTextBox.toggleValidDateBox();
			}
		});
	}

	@SuppressWarnings("deprecation")
	public Boolean getIsSSL() {
		return this.isSSL.isChecked();
	}

	public String getPortNumber() {
		return portNumber.getText();
	}

	@SuppressWarnings("deprecation")
	public void setSSL(Boolean isSSL2) {
		this.isSSL.setChecked(isSSL2);
	}

	public void setPortNumber(String portNumber2) {
		this.portNumber.setText(portNumber2);
	}

	public TextBox getPortNumberTextBox() {
		return this.portNumber;
	}

	public String getPortNumberLabel() {
		return this.portNumberLabel.getText();
	}

}
