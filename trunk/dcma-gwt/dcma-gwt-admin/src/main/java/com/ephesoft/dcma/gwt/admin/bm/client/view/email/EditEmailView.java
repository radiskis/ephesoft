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
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class provides functionality to edit email.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class EditEmailView extends View<EditEmailPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, EditEmailView> {
	}

	/**
	 * userNameLabel Label.
	 */
	@UiField
	protected Label userNameLabel;

	/**
	 * userNameStar Label.
	 */
	@UiField
	protected Label userNameStar;

	/**
	 * userName TextBox.
	 */
	@UiField
	protected TextBox userName;

	/**
	 * passwordLabel Label.
	 */
	@UiField
	protected Label passwordLabel;

	/**
	 * passwordStar Label.
	 */
	@UiField
	protected Label passwordStar;

	/**
	 * password TextBox.
	 */
	@UiField
	protected TextBox password;

	/**
	 * serverNameLabel Label.
	 */
	@UiField
	protected Label serverNameLabel;

	/**
	 * serverNameStar Label.
	 */
	@UiField
	protected Label serverNameStar;

	/**
	 * serverName TextBox.
	 */
	@UiField
	protected TextBox serverName;

	/**
	 * serverTypeLabel Label.
	 */
	@UiField
	protected Label serverTypeLabel;

	/**
	 * serverTypeStar Label.
	 */
	@UiField
	protected Label serverTypeStar;

	/**
	 * serverType TextBox.
	 */
	@UiField
	protected TextBox serverType;

	/**
	 * folderNameLabel Label.
	 */
	@UiField
	protected Label folderNameLabel;

	/**
	 * folderNameStar Label.
	 */
	@UiField
	protected Label folderNameStar;

	/**
	 * folderName TextBox.
	 */
	@UiField
	protected TextBox folderName;

	/**
	 * isSSLLabel Label.
	 */
	@UiField
	protected Label isSSLLabel;

	/**
	 * isSSL CheckBox.
	 */
	@UiField
	protected CheckBox isSSL;

	/**
	 * portNumberLabel Label.
	 */
	@UiField
	protected Label portNumberLabel;

	/**
	 * portNumberStar Label.
	 */
	@UiField
	protected Label portNumberStar;

	/**
	 * portNumber TextBox.
	 */
	@UiField
	protected TextBox portNumber;

	/**
	 * saveButton Button.
	 */
	@UiField
	protected Button saveButton;

	/**
	 * cancelButton Button.
	 */
	@UiField
	protected Button cancelButton;

	/**
	 * scrollPanel ScrollPanel.
	 */
	@UiField
	protected ScrollPanel scrollPanel;

	/**
	 * validateUserNameTextBox ValidatableWidget<TextBox>.
	 */
	private ValidatableWidget<TextBox> validateUserNameTextBox;

	/**
	 * validatePasswordTextBox ValidatableWidget<TextBox>.
	 */
	private ValidatableWidget<TextBox> validatePasswordTextBox;

	/**
	 * validateServerNameTextBox ValidatableWidget<TextBox>.
	 */
	private ValidatableWidget<TextBox> validateServerNameTextBox;

	/**
	 * validateServerTypeTextBox ValidatableWidget<TextBox>.
	 */
	private ValidatableWidget<TextBox> validateServerTypeTextBox;

	/**
	 * validateFolderNameTextBox ValidatableWidget<TextBox>.
	 */
	private ValidatableWidget<TextBox> validateFolderNameTextBox;

	/**
	 * validatePortNumberTextBox ValidatableWidget<TextBox>.
	 */
	private ValidatableWidget<TextBox> validatePortNumberTextBox;

	/**
	 * editEmailViewPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel editEmailViewPanel;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	public EditEmailView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		saveButton.setText(AdminConstants.OK_BUTTON);
		cancelButton.setText(AdminConstants.CANCEL_BUTTON);
		saveButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		cancelButton.setHeight(AdminConstants.BUTTON_HEIGHT);

		createValidatableWidgets();

		editEmailViewPanel.setSpacing(BatchClassManagementConstants.FIVE);

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
		scrollPanel.setStyleName(AdminConstants.SCROLL_PANEL_HEIGHT);

		isSSL.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				portNumberStar.setVisible(event.getValue());
			}
		});

	}

	/**
	 * To get Port Number Star.
	 * 
	 * @return Label
	 */
	public Label getPortNumberStar() {
		return portNumberStar;
	}

	/**
	 * To perform operations on Save Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("saveButton")
	public void onSaveClicked(ClickEvent clickEvent) {
		presenter.onSave();
	}

	/**
	 * To perform operations on cancel Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("cancelButton")
	public void onCancelClicked(ClickEvent clickEvent) {
		presenter.onCancel();
	}

	/**
	 * To set User name.
	 * 
	 * @param name String
	 */
	public void setUsername(String name) {
		this.userName.setValue(name);
	}

	/**
	 * To get User name.
	 * 
	 * @return String
	 */
	public String getUsername() {
		return this.userName.getValue();
	}

	/**
	 * To set Password.
	 * 
	 * @param password String
	 */
	public void setPassword(String password) {
		this.password.setValue(password);
	}

	/**
	 * To get Password.
	 * 
	 * @return String
	 */
	public String getPassword() {
		return this.password.getValue();
	}

	/**
	 * To get Server Name.
	 * 
	 * @return String
	 */
	public String getServerName() {
		return this.serverName.getValue();
	}

	/**
	 * To set Server Name.
	 * 
	 * @param serverName String
	 */
	public void setServerName(String serverName) {
		this.serverName.setValue(serverName);
	}

	/**
	 * To get Server Type.
	 * 
	 * @return String
	 */
	public String getServerType() {
		return serverType.getValue();
	}

	/**
	 * To set Server Type.
	 * 
	 * @param serverType String
	 */
	public void setServerType(String serverType) {
		this.serverType.setValue(serverType);
	}

	/**
	 * To get Folder Name.
	 * 
	 * @return String
	 */
	public String getFolderName() {
		return folderName.getValue();
	}

	/**
	 * To set Folder Name.
	 * 
	 * @param folderName String
	 */
	public void setFolderName(String folderName) {
		this.folderName.setValue(folderName);
	}

	/**
	 * To get Validate UserName TextBox.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateUserNameTextBox() {
		return validateUserNameTextBox;
	}

	/**
	 * To get Validate Password TextBox.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidatePasswordTextBox() {
		return validatePasswordTextBox;
	}

	/**
	 * To get Validate Server Name TextBox.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateServerNameTextBox() {
		return validateServerNameTextBox;
	}

	/**
	 * To get Validate Server Type TextBox.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateServerTypeTextBox() {
		return validateServerTypeTextBox;
	}

	/**
	 * To get Validate Folder Name TextBox.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateFolderNameTextBox() {
		return validateFolderNameTextBox;
	}

	/**
	 * To get Validate Port Number TextBox.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidatePortNumberTextBox() {
		return validatePortNumberTextBox;
	}

	/**
	 * To get User Name TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getUserNameTextBox() {
		return this.userName;
	}

	/**
	 * To get Password TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getPasswordTextBox() {
		return this.password;
	}

	/**
	 * To get Server Name TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getServerNameTextBox() {
		return this.serverName;
	}

	/**
	 * To get Server Type TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getServerTypeTextBox() {
		return this.serverType;
	}

	/**
	 * To get Folder Name TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getFolderNameTextBox() {
		return this.folderName;
	}

	/**
	 * To create Validatable Widgets.
	 */
	public final void createValidatableWidgets() {
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

	/**
	 * To get Is SSL.
	 * 
	 * @return Boolean
	 */
	@SuppressWarnings("deprecation")
	public Boolean getIsSSL() {
		return this.isSSL.isChecked();
	}

	/**
	 * To get Port Number.
	 * 
	 * @return String
	 */
	public String getPortNumber() {
		return portNumber.getText();
	}

	/**
	 * To set SSL.
	 * 
	 * @param isSSL2 Boolean
	 */
	@SuppressWarnings("deprecation")
	public void setSSL(Boolean isSSL2) {
		this.isSSL.setChecked(isSSL2);
	}

	/**
	 * To set Port Number.
	 * 
	 * @param portNumber2 String
	 */
	public void setPortNumber(String portNumber2) {
		this.portNumber.setText(portNumber2);
	}

	/**
	 * To get Port Number TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getPortNumberTextBox() {
		return this.portNumber;
	}

	/**
	 * To get Port Number Label.
	 * 
	 * @return String
	 */
	public String getPortNumberLabel() {
		return this.portNumberLabel.getText();
	}

}
