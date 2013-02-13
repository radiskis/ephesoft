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

package com.ephesoft.dcma.gwt.admin.bm.client.view.batch;

import java.util.List;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.batch.BatchClassDetailPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.shared.RoleDTO;
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
 * This class provides functionality to show individual batch class detail.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class BatchClassDetailView extends View<BatchClassDetailPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, BatchClassDetailView> {
	}

	/**
	 * name Label.
	 */
	@UiField
	protected Label name;

	/**
	 * priority Label.
	 */
	@UiField
	protected Label priority;

	/**
	 * uncFolder Label.
	 */
	@UiField
	protected Label uncFolder;

	/**
	 * description Label.
	 */
	@UiField
	protected Label description;

	/**
	 * version Label.
	 */
	@UiField
	protected Label version;

	/**
	 * role Label.
	 */
	@UiField
	protected Label role;

	/**
	 * nameLabel Label.
	 */
	@UiField
	protected Label nameLabel;

	/**
	 * priorityLabel Label.
	 */
	@UiField
	protected Label priorityLabel;

	/**
	 * descLabel Label.
	 */
	@UiField
	protected Label descLabel;

	/**
	 * uncLabel Label.
	 */
	@UiField
	protected Label uncLabel;

	/**
	 * versionLabel Label.
	 */
	@UiField
	protected Label versionLabel;

	/**
	 * roleLabel Label.
	 */
	@UiField
	protected Label roleLabel;

	/**
	 * editBatchPropertiesButton Button.
	 */
	@UiField
	protected Button editBatchPropertiesButton;

	/**
	 * scrollPanel ScrollPanel.
	 */
	@UiField
	protected ScrollPanel scrollPanel;

	/**
	 * systemFolder Label.
	 */
	@UiField
	protected Label systemFolder;

	/**
	 * systemFolderLabel Label.
	 */
	@UiField
	protected Label systemFolderLabel;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Constructor.
	 */
	public BatchClassDetailView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		priorityLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.PRIORITY) + AdminConstants.COLON);
		descLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.DESCRIPTION) + AdminConstants.COLON);
		uncLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.UNC_FOLDER) + AdminConstants.COLON);
		versionLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.VERSION) + AdminConstants.COLON);
		roleLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.ROLE) + AdminConstants.COLON);
		nameLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.NAME) + AdminConstants.COLON);

		nameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		priorityLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		descLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		uncLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		versionLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		roleLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		editBatchPropertiesButton.setText(AdminConstants.EDIT_BUTTON);
		editBatchPropertiesButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		scrollPanel.setHeight(AdminConstants.SCROLL_PANEL_HEIGHT_IE);
		systemFolderLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.SYSTEM_FOLDER)
				+ AdminConstants.COLON);
		systemFolderLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);

	}

	/**
	 * To get Edit Batch Properties Button.
	 * 
	 * @return Button
	 */
	public Button getEditBatchPropertiesButton() {
		return editBatchPropertiesButton;
	}

	/**
	 * To get presenter on edit batch properties button click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("editBatchPropertiesButton")
	public void onEditBatchPropertiesButtonClick(ClickEvent clickEvent) {
		presenter.getController().getBatchClassManagementPresenter().getBatchClassViewPresenter().onEditBatchPropertiesButtonClicked();
	}

	/**
	 * To set Priority.
	 * 
	 * @param priority String
	 */
	public void setPriority(String priority) {
		this.priority.setText(priority);
	}

	/**
	 * To set Unc Folder.
	 * 
	 * @param uncFolder String
	 */
	public void setUncFolder(String uncFolder) {
		this.uncFolder.setText(uncFolder);
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
	 * To set name.
	 * 
	 * @param name String
	 */
	public void setName(String name) {
		this.name.setText(name);
	}

	/**
	 * To set version.
	 * 
	 * @param version String
	 */
	public void setVersion(String version) {
		this.version.setText(version);
	}

	/**
	 * To set Role Label.
	 * 
	 * @param roleLabel String
	 */
	public void setRoleLabel(String roleLabel) {
		this.roleLabel.setText(roleLabel);
	}

	/**
	 * To set Name Label.
	 * 
	 * @param nameLabel String
	 */
	public void setNameLabel(String nameLabel) {
		this.nameLabel.setText(nameLabel);
	}

	/**
	 * To set role.
	 * 
	 * @param roleName List<RoleDTO>
	 */
	public void setRole(List<RoleDTO> roleName) {
		StringBuffer role = new StringBuffer(BatchClassManagementConstants.EMPTY_STRING);
		boolean isFirst = true;
		List<RoleDTO> allRoles = presenter.getController().getAllRoles();
		for (RoleDTO roleDTO : roleName) {
			if (allRoles != null && allRoles.contains(roleDTO)) {
				if (isFirst) {
					isFirst = false;
				} else {
					role.append(BatchClassManagementConstants.SEMICOLON);
				}
				role.append(roleDTO.getName());
			}
		}
		this.role.setText(role.toString());
	}

	/**
	 * This method enable/disable buttons for super-admin role.
	 * 
	 * @param enable Boolean
	 */
	public void setButtonsEnableAttributeForSuperAdmin(Boolean enable) {
		if (null != enable) {
			editBatchPropertiesButton.setEnabled(enable);
		}
	}

	/**
	 * To set System Folder.
	 * 
	 * @param systemFolder String
	 */
	public void setSystemFolder(String systemFolder) {
		this.systemFolder.setText(systemFolder);
	}
}
