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
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.cmisimporter.CmisImporterDetailPresenter;
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
 * This class provides functionality to show individual cmis importer detail.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class CmisImporterDetailView extends View<CmisImporterDetailPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, CmisImporterDetailView> {
	}

	/**
	 * serverUrlLabel Label.
	 */
	@UiField
	protected Label serverUrlLabel;

	/**
	 * serverUrl Label.
	 */
	@UiField
	protected Label serverUrl;

	/**
	 * userNameLabel Label.
	 */
	@UiField
	protected Label userNameLabel;

	/**
	 * userName Label.
	 */
	@UiField
	protected Label userName;

	/**
	 * passwordLabel Label.
	 */
	@UiField
	protected Label passwordLabel;

	/**
	 * password Label.
	 */
	@UiField
	protected Label password;

	/**
	 * repositoryIDLabel Label.
	 */
	@UiField
	protected Label repositoryIDLabel;

	/**
	 * repositoryID Label.
	 */
	@UiField
	protected Label repositoryID;

	/**
	 * fileExtensionLabel Label.
	 */
	@UiField
	protected Label fileExtensionLabel;

	/**
	 * fileExtension Label.
	 */
	@UiField
	protected Label fileExtension;

	/**
	 * folderNameLabel Label.
	 */
	@UiField
	protected Label folderNameLabel;

	/**
	 * folderName Label.
	 */
	@UiField
	protected Label folderName;

	/**
	 * cmisPropertyLabel Label.
	 */
	@UiField
	protected Label cmisPropertyLabel;

	/**
	 * cmisProperty Label.
	 */
	@UiField
	protected Label cmisProperty;

	/**
	 * valueLabel Label.
	 */
	@UiField
	protected Label valueLabel;

	/**
	 * value Label.
	 */
	@UiField
	protected Label value;

	/**
	 * valueToUpdateLabel Label.
	 */
	@UiField
	protected Label valueToUpdateLabel;

	/**
	 * valueToUpdate Label.
	 */
	@UiField
	protected Label valueToUpdate;

	/**
	 * scrollPanel ScrollPanel.
	 */
	@UiField
	protected ScrollPanel scrollPanel;

	/**
	 * editCmisImporterPropertiesButton Button.
	 */
	@UiField
	protected Button editCmisImporterPropertiesButton;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Constructor.
	 */
	public CmisImporterDetailView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

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

		serverUrlLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		userNameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		passwordLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		repositoryIDLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		fileExtensionLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		folderNameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		cmisPropertyLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		valueLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		valueToUpdateLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);

		editCmisImporterPropertiesButton.setText(AdminConstants.EDIT_BUTTON);
		editCmisImporterPropertiesButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		scrollPanel.setStyleName(AdminConstants.SCROLL_PANEL_HEIGHT);
	}

	/**
	 * To get Password.
	 * 
	 * @return String
	 */
	public String getPassword() {
		return password.getText();
	}

	/**
	 * To set Password.
	 * 
	 * @param password String
	 */
	public void setPassword(String password) {
		this.password.setText(password);
	}

	/**
	 * To get Cmis Property.
	 * 
	 * @return String
	 */
	public String getCmisProperty() {
		return cmisProperty.getText();
	}

	/**
	 * To set Cmis Property.
	 * 
	 * @param cmisProperty
	 */
	public void setCmisProperty(String cmisProperty) {
		this.cmisProperty.setText(cmisProperty);
	}

	/**
	 * To get User Name.
	 * 
	 * @return String
	 */
	public String getUserName() {
		return userName.getText();
	}

	/**
	 * To set User Name.
	 * 
	 * @param userName String
	 */
	public void setUserName(String userName) {
		this.userName.setText(userName);
	}

	/**
	 * To get Server Url.
	 * 
	 * @return String
	 */
	public String getServerUrl() {
		return serverUrl.getText();
	}

	/**
	 * To set Server Url.
	 * 
	 * @param serverUrl String
	 */
	public void setServerUrl(String serverUrl) {
		this.serverUrl.setText(serverUrl);
	}

	/**
	 * To get Repository ID.
	 * 
	 * @return String
	 */
	public String getRepositoryID() {
		return repositoryID.getText();
	}

	/**
	 * To set Repository ID.
	 * 
	 * @param repositoryID String
	 */
	public void setRepositoryID(String repositoryID) {
		this.repositoryID.setText(repositoryID);
	}

	/**
	 * To get Value.
	 * 
	 * @return String
	 */
	public String getValue() {
		return value.getText();
	}

	/**
	 * To set Value.
	 * 
	 * @param value String
	 */
	public void setValue(String value) {
		this.value.setText(value);
	}

	/**
	 * To get Value To Update.
	 * 
	 * @return String
	 */
	public String getValueToUpdate() {
		return valueToUpdate.getText();
	}

	/**
	 * To set Value To Update.
	 * 
	 * @param valueToUpdate String
	 */
	public void setValueToUpdate(String valueToUpdate) {
		this.valueToUpdate.setText(valueToUpdate);
	}

	/**
	 * To get File Extension.
	 * 
	 * @return String
	 */
	public String getFileExtension() {
		return fileExtension.getText();
	}

	/**
	 * To set File Extension.
	 * 
	 * @param fileExtension String
	 */
	public void setFileExtension(String fileExtension) {
		this.fileExtension.setText(fileExtension);
	}

	/**
	 * To get Folder Name.
	 * 
	 * @return String
	 */
	public String getFolderName() {
		return folderName.getText();
	}

	/**
	 * To set Folder Name.
	 * 
	 * @param folderName String
	 */
	public void setFolderName(String folderName) {
		this.folderName.setText(folderName);
	}

	/**
	 * To get Edit Cmis Importer Properties Button.
	 * 
	 * @return Button
	 */
	public Button getEditCmisImporterPropertiesButton() {
		return editCmisImporterPropertiesButton;
	}

	/**
	 * To perform operations on edit Cmis Importer Properties Button Clicked.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("editCmisImporterPropertiesButton")
	public void onEditCmisImporterPropertiesButtonClicked(ClickEvent clickEvent) {
		presenter.getController().getBatchClassManagementPresenter().getCmisImporterViewPresenter()
				.onEditCmisImporterPropertiesButtonClicked();
	}

}
