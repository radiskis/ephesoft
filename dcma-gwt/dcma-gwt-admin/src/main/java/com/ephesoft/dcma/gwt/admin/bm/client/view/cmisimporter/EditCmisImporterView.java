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

package com.ephesoft.dcma.gwt.admin.bm.client.view.cmisimporter;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.cmisimporter.EditCmisImporterPresenter;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class provides functionality to edit cmis importer.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class EditCmisImporterView extends View<EditCmisImporterPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, EditCmisImporterView> {
	}

	/**
	 * serverUrlLabel Label.
	 */
	@UiField
	protected Label serverUrlLabel;

	/**
	 * serverUrlStar Label.
	 */
	@UiField
	protected Label serverUrlStar;

	/**
	 * serverUrl TextBox.
	 */
	@UiField
	protected TextBox serverUrl;

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
	 * repositoryIDLabel Label.
	 */
	@UiField
	protected Label repositoryIDLabel;

	/**
	 * repositoryIDStar Label.
	 */
	@UiField
	protected Label repositoryIDStar;

	/**
	 * repositoryID TextBox.
	 */
	@UiField
	protected TextBox repositoryID;

	/**
	 * fileExtensionLabel Label.
	 */
	@UiField
	protected Label fileExtensionLabel;

	/**
	 * fileExtensionStar Label.
	 */
	@UiField
	protected Label fileExtensionStar;

	/**
	 * fileExtension TextBox.
	 */
	@UiField
	protected TextBox fileExtension;

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
	 * cmisPropertyLabel Label.
	 */
	@UiField
	protected Label cmisPropertyLabel;

	/**
	 * cmisPropertyStar Label.
	 */
	@UiField
	protected Label cmisPropertyStar;

	/**
	 * cmisProperty TextBox.
	 */
	@UiField
	protected TextBox cmisProperty;

	/**
	 * valueLabel Label.
	 */
	@UiField
	protected Label valueLabel;

	/**
	 * valueStar Label.
	 */
	@UiField
	protected Label valueStar;

	/**
	 * value TextBox.
	 */
	@UiField
	protected TextBox value;

	/**
	 * valueToUpdateLabel Label.
	 */
	@UiField
	protected Label valueToUpdateLabel;

	/**
	 * valueToUpdateStar Label.
	 */
	@UiField
	protected Label valueToUpdateStar;

	/**
	 * valueToUpdate TextBox.
	 */
	@UiField
	protected TextBox valueToUpdate;

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
	 * validateCmisPropertyTextBox ValidatableWidget<TextBox>.
	 */
	private ValidatableWidget<TextBox> validateCmisPropertyTextBox;

	/**
	 * validateRepositoryIDTextBox ValidatableWidget<TextBox>.
	 */
	private ValidatableWidget<TextBox> validateRepositoryIDTextBox;

	/**
	 * validateFolderNameTextBox ValidatableWidget<TextBox>.
	 */
	private ValidatableWidget<TextBox> validateFolderNameTextBox;

	/**
	 * validateServerURLTextBox ValidatableWidget<TextBox>.
	 */
	private ValidatableWidget<TextBox> validateServerURLTextBox;

	/**
	 * validateFileExtensionTextBox ValidatableWidget<TextBox>.
	 */
	private ValidatableWidget<TextBox> validateFileExtensionTextBox;

	/**
	 * validateValueTextBox ValidatableWidget<TextBox>.
	 */
	private ValidatableWidget<TextBox> validateValueTextBox;

	/**
	 * validateValueToUpdateTextBox ValidatableWidget<TextBox>.
	 */
	private ValidatableWidget<TextBox> validateValueToUpdateTextBox;

	/**
	 * editCmisViewPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel editCmisViewPanel;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Constructor.
	 */
	public EditCmisImporterView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		saveButton.setText(AdminConstants.OK_BUTTON);
		cancelButton.setText(AdminConstants.CANCEL_BUTTON);
		saveButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		cancelButton.setHeight(AdminConstants.BUTTON_HEIGHT);

		createValidatableWidgets();

		editCmisViewPanel.setSpacing(BatchClassManagementConstants.FIVE);

		serverUrlLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.SERVER_URL)
				+ AdminConstants.COLON);
		userNameLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.CMIS_USERNAME)
				+ AdminConstants.COLON);
		passwordLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.CMIS_PASSWORD)
				+ AdminConstants.COLON);
		repositoryIDLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.REPOSITORY_ID)
				+ AdminConstants.COLON);
		fileExtensionLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.FILE_EXTENSION)
				+ AdminConstants.COLON);
		folderNameLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.CMIS_FOLDER_NAME)
				+ AdminConstants.COLON);
		cmisPropertyLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.CMIS_PROPERTY)
				+ AdminConstants.COLON);
		valueLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.CMIS_VALUE) + AdminConstants.COLON);
		valueToUpdateLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.CMIS_VALUE_TO_UPDATE)
				+ AdminConstants.COLON);

		serverUrlStar.setText(AdminConstants.STAR);
		userNameStar.setText(AdminConstants.STAR);
		passwordStar.setText(AdminConstants.STAR);
		repositoryIDStar.setText(AdminConstants.STAR);
		fileExtensionStar.setText(AdminConstants.STAR);
		folderNameStar.setText(AdminConstants.STAR);
		cmisPropertyStar.setText(AdminConstants.STAR);
		valueStar.setText(AdminConstants.STAR);
		valueToUpdateStar.setText(AdminConstants.STAR);

		serverUrlLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		userNameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		passwordLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		repositoryIDLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		fileExtensionLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		folderNameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		cmisPropertyLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		valueLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		valueToUpdateLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);

		serverUrlStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		userNameStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		passwordStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		repositoryIDStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		fileExtensionStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		folderNameStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		cmisPropertyStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		valueStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		valueToUpdateStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		scrollPanel.setStyleName(AdminConstants.SCROLL_PANEL_HEIGHT);
	}

	/**
	 * To do operations on save click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("saveButton")
	public void onSaveClicked(ClickEvent clickEvent) {
		presenter.onSave();
	}

	/**
	 * To do operations on cancel click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("cancelButton")
	public void onCancelClicked(ClickEvent clickEvent) {
		presenter.onCancel();
	}

	/**
	 * To get Validate Cmis Property TextBox.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateCmisPropertyTextBox() {
		return validateCmisPropertyTextBox;
	}

	/**
	 * To get Validate File Extension TextBox.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateFileExtensionTextBox() {
		return validateFileExtensionTextBox;
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
	 * To get Validate Password TextBox.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidatePasswordTextBox() {
		return validatePasswordTextBox;
	}

	/**
	 * To get Validate Repository ID TextBox.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateRepositoryIDTextBox() {
		return validateRepositoryIDTextBox;
	}

	/**
	 * To get Validate Server URL TextBox.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateServerURLTextBox() {
		return validateServerURLTextBox;
	}

	/**
	 * To get Validate User Name TextBox.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateUserNameTextBox() {
		return validateUserNameTextBox;
	}

	/**
	 * To get Validate Value TextBox.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateValueTextBox() {
		return validateValueTextBox;
	}

	/**
	 * To get Validate Value to Update TextBox.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateValueToUpdateTextBox() {
		return validateValueToUpdateTextBox;
	}

	/**
	 * To get Value TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getValueTextBox() {
		return this.value;
	}

	/**
	 * To get Cmis Property TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getCmisPropertyTextBox() {
		return this.cmisProperty;
	}

	/**
	 * To get Server Url TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getServerUrlTextBox() {
		return this.serverUrl;
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
	 * To get Repository ID TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getRepositoryIDTextBox() {
		return this.repositoryID;
	}

	/**
	 * To get File Extension TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getFileExtensionTextBox() {
		return this.fileExtension;
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
	 * To get Value to Update TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getValueToUpdateTextBox() {
		return this.valueToUpdate;
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

		validateServerURLTextBox = new ValidatableWidget<TextBox>(serverUrl);
		validateServerURLTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateServerURLTextBox.toggleValidDateBox();
			}
		});

		validateCmisPropertyTextBox = new ValidatableWidget<TextBox>(cmisProperty);
		validateCmisPropertyTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateCmisPropertyTextBox.toggleValidDateBox();
			}
		});

		validateFolderNameTextBox = new ValidatableWidget<TextBox>(folderName);
		validateFolderNameTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateFolderNameTextBox.toggleValidDateBox();
			}
		});

		validateRepositoryIDTextBox = new ValidatableWidget<TextBox>(repositoryID);
		validateRepositoryIDTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateRepositoryIDTextBox.toggleValidDateBox();
			}
		});

		validateValueTextBox = new ValidatableWidget<TextBox>(value);
		validateValueTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateValueTextBox.toggleValidDateBox();
			}
		});

		validateValueToUpdateTextBox = new ValidatableWidget<TextBox>(valueToUpdate);
		validateValueToUpdateTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateValueToUpdateTextBox.toggleValidDateBox();
			}
		});

		validateFileExtensionTextBox = new ValidatableWidget<TextBox>(fileExtension);
		validateFileExtensionTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateFileExtensionTextBox.toggleValidDateBox();
			}
		});
	}

	/**
	 * To set Password.
	 * 
	 * @return String
	 */
	public String getPassword() {
		return password.getValue();
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
	 * To get Cmis Property.
	 * 
	 * @return String
	 */
	public String getCmisProperty() {
		return cmisProperty.getValue();
	}

	/**
	 * To set Cmis Property.
	 * 
	 * @param cmisProperty String
	 */
	public void setCmisProperty(String cmisProperty) {
		this.cmisProperty.setValue(cmisProperty);
	}

	/**
	 * To get User Name.
	 * 
	 * @return String
	 */
	public String getUserName() {
		return userName.getValue();
	}

	/**
	 * To set User Name.
	 * 
	 * @param userName String
	 */
	public void setUserName(String userName) {
		this.userName.setValue(userName);
	}

	/**
	 * To get Server Url.
	 * 
	 * @return String
	 */
	public String getServerUrl() {
		return serverUrl.getValue();
	}

	/**
	 * To set Server Url.
	 * 
	 * @param serverUrl String
	 */
	public void setServerUrl(String serverUrl) {
		this.serverUrl.setValue(serverUrl);
	}

	/**
	 * To get Repository ID.
	 * 
	 * @return String
	 */
	public String getRepositoryID() {
		return repositoryID.getValue();
	}

	/**
	 * To set Repository ID.
	 * 
	 * @param repositoryID String
	 */
	public void setRepositoryID(String repositoryID) {
		this.repositoryID.setValue(repositoryID);
	}

	/**
	 * To get value.
	 * 
	 * @return String
	 */
	public String getValue() {
		return value.getValue();
	}

	/**
	 * To set value.
	 * 
	 * @param value String
	 */
	public void setValue(String value) {
		this.value.setValue(value);
	}

	/**
	 * To get Value to Update.
	 * 
	 * @return String
	 */
	public String getValueToUpdate() {
		return valueToUpdate.getValue();
	}

	/**
	 * To set Value to Update.
	 * 
	 * @param valueToUpdate String
	 */
	public void setValueToUpdate(String valueToUpdate) {
		this.valueToUpdate.setValue(valueToUpdate);
	}

	/**
	 * To get File Extension.
	 * 
	 * @return String
	 */
	public String getFileExtension() {
		return fileExtension.getValue();
	}

	/**
	 * To set File Extension.
	 * 
	 * @param fileExtension String
	 */
	public void setFileExtension(String fileExtension) {
		this.fileExtension.setValue(fileExtension);
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
	 * To get Save Button.
	 * 
	 * @return Button
	 */
	public Button getSaveButton() {
		return saveButton;
	}

	/**
	 * To set Save Button.
	 * 
	 * @param saveButton Button
	 */
	public void setSaveButton(Button saveButton) {
		this.saveButton = saveButton;
	}

	/**
	 * To get Cancel Button.
	 * 
	 * @return Button
	 */
	public Button getCancelButton() {
		return cancelButton;
	}

	/**
	 * To set Cancel Button.
	 * 
	 * @param cancelButton Button
	 */
	public void setCancelButton(Button cancelButton) {
		this.cancelButton = cancelButton;
	}
}
