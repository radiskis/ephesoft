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

import java.util.ArrayList;
import java.util.List;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.batch.EditBatchClassPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.validator.ValidatableWidget;
import com.ephesoft.dcma.gwt.core.shared.RoleDTO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class provides functionality to edit individual batch class and it's child.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class EditBatchClassView extends View<EditBatchClassPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, EditBatchClassView> {
	}

	/**
	 * priority TextBox.
	 */
	@UiField
	protected TextBox priority;

	/**
	 * uncFolder Label.
	 */
	@UiField
	protected Label uncFolder;

	/**
	 * name Label.
	 */
	@UiField
	protected Label name;

	/**
	 * description TextBox.
	 */
	@UiField
	protected TextBox description;

	/**
	 * version Label.
	 */
	@UiField
	protected Label version;

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
	 * star Label.
	 */
	@UiField
	protected Label star;

	/**
	 * editBatchPanel HorizontalPanel.
	 */
	@UiField
	protected HorizontalPanel editBatchPanel;

	/**
	 * descStar Label.
	 */
	@UiField
	protected Label descStar;

	/**
	 * roleLabel Label.
	 */
	@UiField
	protected Label roleLabel;

	/**
	 * listBoxPanel HorizontalPanel.
	 */
	@UiField
	protected HorizontalPanel listBoxPanel;

	/**
	 * role ListBox.
	 */
	private final ListBox role;

	/**
	 * validateTextBox ValidatableWidget<TextBox>.
	 */
	private final ValidatableWidget<TextBox> validateTextBox;

	/**
	 * validateDescTextBox ValidatableWidget<TextBox>.
	 */
	private final ValidatableWidget<TextBox> validateDescTextBox;

	/**
	 * editBatchClassViewPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel editBatchClassViewPanel;

	/**
	 * systemFolder TextBox.
	 */
	@UiField
	protected TextBox systemFolder;

	/**
	 * systemFolderStar Label.
	 */
	@UiField
	protected Label systemFolderStar;

	/**
	 * systemFolderLabel Label.
	 */
	@UiField
	protected Label systemFolderLabel;

	/**
	 * validateSystemFolderTextBox ValidatableWidget<TextBox>.
	 */
	private final ValidatableWidget<TextBox> validateSystemFolderTextBox;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Constructor.
	 */
	public EditBatchClassView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		saveButton.setText(AdminConstants.OK_BUTTON);
		cancelButton.setText(AdminConstants.CANCEL_BUTTON);
		validateTextBox = new ValidatableWidget<TextBox>(priority);
		role = new ListBox(true);
		validateTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateTextBox.toggleValidDateBox();
			}
		});

		validateDescTextBox = new ValidatableWidget<TextBox>(description);
		validateDescTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateDescTextBox.toggleValidDateBox();
			}
		});

		nameLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.NAME) + AdminConstants.COLON);
		priorityLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.PRIORITY) + AdminConstants.COLON);
		descLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.DESCRIPTION) + AdminConstants.COLON);
		uncLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.UNC_FOLDER) + AdminConstants.COLON);
		versionLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.VERSION) + AdminConstants.COLON);
		roleLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.ROLE) + AdminConstants.COLON);
		star.setText(AdminConstants.STAR);
		descStar.setText(AdminConstants.STAR);

		nameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		priorityLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		descLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		uncLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		versionLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		roleLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		star.setStyleName(AdminConstants.FONT_RED_STYLE);
		descStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		listBoxPanel.add(role);
		saveButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		cancelButton.setHeight(AdminConstants.BUTTON_HEIGHT);

		systemFolderLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.SYSTEM_FOLDER)
				+ AdminConstants.COLON);
		systemFolderLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		systemFolderStar.setText(AdminConstants.STAR);
		systemFolderStar.setStyleName(AdminConstants.FONT_RED_STYLE);

		validateSystemFolderTextBox = new ValidatableWidget<TextBox>(systemFolder);
		validateSystemFolderTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateSystemFolderTextBox.toggleValidDateBox();
			}
		});
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
	 * To get Priority.
	 * 
	 * @return String
	 */
	public String getPriority() {
		return priority.getValue();
	}

	/**
	 * To set Priority.
	 * 
	 * @param priority String
	 */
	public void setPriority(String priority) {
		this.priority.setValue(priority);
	}

	/**
	 * To get Unc Folder.
	 * 
	 * @return String
	 */
	public String getUncFolder() {
		return uncFolder.getText();
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
	 * To get Description.
	 * 
	 * @return String
	 */
	public String getDescription() {
		return description.getValue();
	}

	/**
	 * To get name.
	 * 
	 * @return String
	 */
	public String getname() {
		return name.getText();
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
	 * To set Description.
	 * 
	 * @param description String
	 */
	public void setDescription(String description) {
		this.description.setValue(description);
	}

	/**
	 * To get Version.
	 * 
	 * @return String
	 */
	public String getVersion() {
		return version.getText();
	}

	/**
	 * To set Version.
	 * 
	 * @param version String
	 */
	public void setVersion(String version) {
		this.version.setText(version);
	}

	/**
	 * To get Validate TextBox.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateTextBox() {
		return validateTextBox;
	}

	/**
	 * To get Validate Desc TextBox.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateDescTextBox() {
		return validateDescTextBox;
	}

	/**
	 * To get Priority TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getPriorityTextBox() {
		return priority;
	}

	/**
	 * To get Description TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getDescriptionTextBox() {
		return description;
	}

	/**
	 * To get role.
	 * 
	 * @return String
	 */
	public String getRole() {
		int numberOfRole = role.getItemCount();
		StringBuffer selected = new StringBuffer(BatchClassManagementConstants.EMPTY_STRING);
		if (role.getSelectedIndex() >= 0) {
			for (int index = 0; index < numberOfRole; index++) {
				if (role.isItemSelected(index)) {
					selected.append(role.getItemText(index)).append(BatchClassManagementConstants.SEMICOLON);
				}
			}
		}
		return selected.toString();
	}

	/**
	 * To get role list.
	 * 
	 * @return List<RoleDTO>
	 */
	public List<RoleDTO> getRoleList() {
		List<RoleDTO> assignedRole = new ArrayList<RoleDTO>();
		List<RoleDTO> allRoleList = presenter.getController().getAllRoles();
		int index = 0;
		if (null != allRoleList) {
			for (RoleDTO roleDTO : allRoleList) {
				if (role.isItemSelected(index)) {
					assignedRole.add(roleDTO);
				}
				index++;
			}
		}
		return assignedRole;
	}

	/**
	 * To get Name Label.
	 * 
	 * @return the nameLabel
	 */
	public Label getNameLabel() {
		return nameLabel;
	}

	/**
	 * To set Name Label.
	 * 
	 * @param nameLabel Label
	 */
	public void setNameLabel(Label nameLabel) {
		this.nameLabel = nameLabel;
	}

	/**
	 * To set role.
	 * 
	 * @param assignedRole List<RoleDTO>
	 * @param roleList List<RoleDTO>
	 */
	public void setRole(List<RoleDTO> assignedRole, List<RoleDTO> roleList) {
		if (roleList.size() > BatchClassManagementConstants.FOUR) {
			role.setVisibleItemCount(BatchClassManagementConstants.FOUR);
		} else {
			role.setVisibleItemCount(roleList.size());
		}
		role.clear();
		if (null == roleList || roleList.isEmpty()) {
			role.setEnabled(false);
			return;
		}
		for (RoleDTO roleDTO : roleList) {
			role.addItem(roleDTO.getName());
		}

		if (role.getItemCount() == 0) {
			role.setEnabled(false);
		} else {
			role.setEnabled(true);
			if (null != assignedRole && !assignedRole.isEmpty()) {
				int index = 0;
				for (RoleDTO roleDTO : roleList) {
					for (RoleDTO assignedRoleDTO : assignedRole) {
						if (roleDTO != null && assignedRoleDTO != null && roleDTO.getName().equals(assignedRoleDTO.getName())) {
							role.setItemSelected(index, true);
						}
					}
					index++;
				}
			}
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

	/**
	 * To get System Folder.
	 * 
	 * @return String
	 */
	public String getSystemFolder() {
		return systemFolder.getValue();
	}

	/**
	 * To get System Folder TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getSystemFolderTextBox() {
		return systemFolder;
	}

	/**
	 * To get Validate System Folder Text Box.
	 * 
	 * @return the validateSystemFolderTextBox
	 */
	public ValidatableWidget<TextBox> getValidateSystemFolderTextBox() {
		return validateSystemFolderTextBox;
	}
}
