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
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.email.EmailDetailPresenter;
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
 * This class provides functionality to show email detail.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class EmailDetailView extends View<EmailDetailPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, EmailDetailView> {
	}

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
	 * serverNameLabel Label.
	 */
	@UiField
	protected Label serverNameLabel;

	/**
	 * serverName Label.
	 */
	@UiField
	protected Label serverName;

	/**
	 * serverTypeLabel Label.
	 */
	@UiField
	protected Label serverTypeLabel;

	/**
	 * serverType Label.
	 */
	@UiField
	protected Label serverType;

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
	 * sslLabel Label.
	 */
	@UiField
	protected Label sslLabel;

	/**
	 * ssl Label.
	 */
	@UiField
	protected Label ssl;

	/**
	 * portNumberLabel Label.
	 */
	@UiField
	protected Label portNumberLabel;

	/**
	 * portNumber Label.
	 */
	@UiField
	protected Label portNumber;

	/**
	 * editEmailPropertiesButton Button.
	 */
	@UiField
	protected Button editEmailPropertiesButton;

	/**
	 * testEmailConfigButton Button.
	 */
	@UiField
	protected Button testEmailConfigButton;

	/**
	 * scrollPanel ScrollPanel.
	 */
	@UiField
	protected ScrollPanel scrollPanel;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Constructor.
	 */
	public EmailDetailView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		testEmailConfigButton.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.TEST_EMAIL_BUTTON)
				+ AdminConstants.COLON);
		userNameLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.USERNAME) + AdminConstants.COLON);
		passwordLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.PASSWORD) + AdminConstants.COLON);
		serverNameLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.SERVER_NAME)
				+ AdminConstants.COLON);
		serverTypeLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.SERVER_TYPE)
				+ AdminConstants.COLON);
		folderNameLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.FOLDER_NAME)
				+ AdminConstants.COLON);
		sslLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.ISSSL) + AdminConstants.COLON);
		portNumberLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.PORTNUMBER)
				+ AdminConstants.COLON);

		userNameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		passwordLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		serverNameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		serverTypeLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		folderNameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		portNumberLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		sslLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		editEmailPropertiesButton.setText(AdminConstants.EDIT_BUTTON);
		scrollPanel.setStyleName(AdminConstants.SCROLL_PANEL_HEIGHT);
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
	 * To set Password.
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password.setText(password);
	}

	/**
	 * To set Server Name.
	 * 
	 * @param serverName String
	 */
	public void setServerName(String serverName) {
		this.serverName.setText(serverName);
	}

	/**
	 * To set Server Type.
	 * 
	 * @param serverType String
	 */
	public void setServerType(String serverType) {
		this.serverType.setText(serverType);
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
	 * To set Ssl.
	 * 
	 * @param ssl String
	 */
	public void setSsl(String ssl) {
		this.ssl.setText(ssl);
	}

	/**
	 * To set Port Number.
	 * 
	 * @param portNumber String
	 */
	public void setPortNumber(String portNumber) {
		this.portNumber.setText(portNumber);
	}

	/**
	 * To get Edit Email Properties Button.
	 * 
	 * @return Button
	 */
	public Button getEditEmailPropertiesButton() {
		return editEmailPropertiesButton;
	}

	/**
	 * To perform operations on Edit Document Properties Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("editEmailPropertiesButton")
	public void onEditDocumentPropertiesButtonClick(ClickEvent clickEvent) {
		presenter.getController().getBatchClassManagementPresenter().getEmailViewPresenter().onEditEmailPropertiesButtonClicked();
	}

	/**
	 * To perform operations on Test Email Configuration Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("testEmailConfigButton")
	public void onTestEmailConfigButtonClick(ClickEvent clickEvent) {
		presenter.onTestEmailConfigButtonClick(userName.getText(), password.getText(), portNumber.getText(), folderName.getText(), ssl
				.getText(), serverName.getText(), serverType.getText());
	}
}
