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

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.module.EditModulePresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class provides functionality to edit module type.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class EditModuleView extends View<EditModulePresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, EditModuleView> {
	}

	/**
	 * name TextBox.
	 */
	@UiField
	protected TextBox name;

	/**
	 * description TextBox.
	 */
	@UiField
	protected TextBox description;

	/**
	 * remoteUrl TextBox.
	 */
	@UiField
	protected TextBox remoteUrl;

	/**
	 * remoteBatchClassIdentifier TextBox.
	 */
	@UiField
	protected TextBox remoteBatchClassIdentifier;

	/**
	 * nameLabel Label.
	 */
	@UiField
	protected Label nameLabel;

	/**
	 * descLabel Label.
	 */
	@UiField
	protected Label descLabel;

	/**
	 * remoteUrlLabel Label.
	 */
	@UiField
	protected Label remoteUrlLabel;

	/**
	 * remoteBatchClassIdentifierLabel Label.
	 */
	@UiField
	protected Label remoteBatchClassIdentifierLabel;

	/**
	 * editModulePanel HorizontalPanel.
	 */
	@UiField
	protected HorizontalPanel editModulePanel;

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
	 * editPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel editPanel;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Constructor.
	 */
	public EditModuleView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		editPanel.setSpacing(BatchClassManagementConstants.FIVE);

		saveButton.setText(AdminConstants.OK_BUTTON);
		cancelButton.setText(AdminConstants.CANCEL_BUTTON);
		saveButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		cancelButton.setHeight(AdminConstants.BUTTON_HEIGHT);

		name.setReadOnly(true);
		description.setReadOnly(true);

		nameLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.NAME) + AdminConstants.COLON);
		descLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.DESCRIPTION) + AdminConstants.COLON);
		remoteUrlLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.REMOTE_URL)
				+ AdminConstants.COLON);
		remoteBatchClassIdentifierLabel.setText(LocaleDictionary.get().getConstantValue(
				BatchClassManagementConstants.REMOTE_BC_IDENTIFIER)
				+ AdminConstants.COLON);
		nameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		descLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		remoteUrlLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		remoteBatchClassIdentifierLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
	}

	/**
	 * To set name.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name.setText(name);
	}

	/**
	 * To set Description.
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description.setText(description);
	}

	/**
	 * To set Remote Url.
	 * 
	 * @param remoteUrl String
	 */
	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl.setText(remoteUrl);
	}

	/**
	 * To set Remote Batch Class Identifier.
	 * 
	 * @param remoteBatchClassIdentifier String
	 */
	public void setRemoteBatchClassIdentifier(String remoteBatchClassIdentifier) {
		this.remoteBatchClassIdentifier.setText(remoteBatchClassIdentifier);
	}

	/**
	 * To set Presenter.
	 * 
	 * @param presenter EditModulePresenter
	 */
	public void setPresenter(EditModulePresenter presenter) {
		this.presenter = presenter;
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
	 * To get Remote Url.
	 * 
	 * @return String
	 */
	public String getRemoteUrl() {
		return remoteUrl.getText();
	}

	/**
	 * To get Remote Batch Class Identifier.
	 * 
	 * @return String
	 */
	public String getRemoteBatchClassIdentifier() {
		return remoteBatchClassIdentifier.getText();
	}

	/**
	 * To get Name.
	 * 
	 * @return TextBox
	 */
	public TextBox getName() {
		return name;
	}

	/**
	 * To get Remote Url Text Box.
	 * 
	 * @return TextBox
	 */
	public TextBox getRemoteUrlTextBox() {
		return remoteUrl;
	}
}
