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

public class EditModuleView extends View<EditModulePresenter> {

	interface Binder extends UiBinder<VerticalPanel, EditModuleView> {
	}

	@UiField
	TextBox name;
	@UiField
	TextBox description;
	@UiField
	TextBox remoteUrl;
	@UiField
	TextBox remoteBatchClassIdentifier;

	@UiField
	Label nameLabel;
	@UiField
	Label descLabel;
	@UiField
	Label remoteUrlLabel;
	@UiField
	Label remoteBatchClassIdentifierLabel;
	@UiField
	HorizontalPanel editModulePanel;
	@UiField
	Button saveButton;
	@UiField
	Button cancelButton;

	@UiField
	VerticalPanel editPanel;

	private static final Binder binder = GWT.create(Binder.class);

	public EditModuleView() {
		initWidget(binder.createAndBindUi(this));

		editPanel.setSpacing(5);

		saveButton.setText(AdminConstants.OK_BUTTON);
		cancelButton.setText(AdminConstants.CANCEL_BUTTON);

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

	public void setName(String name) {
		this.name.setText(name);
	}

	public void setDescription(String description) {
		this.description.setText(description);
	}

	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl.setText(remoteUrl);
	}

	public void setRemoteBatchClassIdentifier(String remoteBatchClassIdentifier) {
		this.remoteBatchClassIdentifier.setText(remoteBatchClassIdentifier);
	}

	public void setPresenter(EditModulePresenter presenter) {
		this.presenter = presenter;
	}

	@UiHandler("saveButton")
	void onSaveClicked(ClickEvent clickEvent) {
		presenter.onSave();
	}

	@UiHandler("cancelButton")
	void onCancelClicked(ClickEvent clickEvent) {
		presenter.onCancel();
	}

	public String getRemoteUrl() {
		return remoteUrl.getText();
	}

	public String getRemoteBatchClassIdentifier() {
		return remoteBatchClassIdentifier.getText();
	}

}
