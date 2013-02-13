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
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.module.ModuleDetailPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class provides functionality to show module detail.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class ModuleDetailView extends View<ModuleDetailPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, ModuleDetailView> {
	}

	/**
	 * name Label.
	 */
	@UiField
	protected Label name;

	/**
	 * description Label.
	 */
	@UiField
	protected Label description;

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
	 * remoteUrl Label.
	 */
	@UiField
	protected Label remoteUrl;

	/**
	 * remoteBatchClassIdentifier Label.
	 */
	@UiField
	protected Label remoteBatchClassIdentifier;

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
	 * editModuleButton Button.
	 */
	@UiField
	protected Button editModuleButton;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Constructor.
	 */
	public ModuleDetailView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

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
		editModuleButton.setText(AdminConstants.EDIT_BUTTON);
		editModuleButton.setHeight(AdminConstants.BUTTON_HEIGHT);

	}

	/**
	 * To set Name.
	 * 
	 * @param name String
	 */
	public void setName(String name) {
		this.name.setText(name);
	}

	/**
	 * To set Description.
	 * 
	 * @param description String
	 */
	public void setDescription(String description) {
		this.description.setText(description);
	}

	/**
	 * To set Presenter.
	 * 
	 * @param presenter ModuleDetailPresenter
	 */
	public void setPresenter(ModuleDetailPresenter presenter) {
		this.presenter = presenter;
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
	 * To set Remote Url.
	 * 
	 * @param remoteUrl String
	 */
	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl.setText(remoteUrl);
	}

	/**
	 * To get controller on edit Module Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("editModuleButton")
	public void onEditModuleButtonClick(ClickEvent clickEvent) {
		presenter.getController().getBatchClassManagementPresenter().getModuleViewPresenter().onEditModuleButtonClick();
	}
}
