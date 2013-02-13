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

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.batch.CopyBatchClassPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.validator.EmptyStringValidator;
import com.ephesoft.dcma.gwt.core.client.validator.ValidatableWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class provides functionality to copy individual batch class and it's child.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class CopyBatchClassView extends View<CopyBatchClassPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, CopyBatchClassView> {
	}

	/**
	 * priority TextBox.
	 */
	@UiField
	protected TextBox priority;

	/**
	 * uncFolder TextBox.
	 */
	@UiField
	protected TextBox uncFolder;

	/**
	 * description TextBox.
	 */
	@UiField
	protected TextBox description;

	/**
	 * name TextBox.
	 */
	@UiField
	protected TextBox name;

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
	 * nameLabel Label.
	 */
	@UiField
	protected Label nameLabel;

	/**
	 * uncLabel Label.
	 */
	@UiField
	protected Label uncLabel;

	/**
	 * star Label.
	 */
	@UiField
	protected Label star;

	/**
	 * uncStar Label.
	 */
	@UiField
	protected Label uncStar;

	/**
	 * nameStar Label.
	 */
	@UiField
	protected Label nameStar;

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
	 * validateTextBox ValidatableWidget<TextBox>.
	 */
	private final ValidatableWidget<TextBox> validateTextBox;

	/**
	 * validateDescTextBox ValidatableWidget<TextBox>.
	 */
	private final ValidatableWidget<TextBox> validateDescTextBox;

	/**
	 * validateUNCTextBox ValidatableWidget<TextBox>.
	 */
	private final ValidatableWidget<TextBox> validateUNCTextBox;

	/**
	 * validateNameTextBox ValidatableWidget<TextBox>.
	 */
	private final ValidatableWidget<TextBox> validateNameTextBox;

	/**
	 * dialogBox DialogBox.
	 */
	private DialogBox dialogBox;

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
	public CopyBatchClassView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		saveButton.setText(AdminConstants.SAVE_BUTTON);
		cancelButton.setText(AdminConstants.CANCEL_BUTTON);
		saveButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		cancelButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		name.setReadOnly(false);
		validateNameTextBox = new ValidatableWidget<TextBox>(name);
		validateNameTextBox.addValidator(new EmptyStringValidator(name));
		validateNameTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> arg0) {
				validateNameTextBox.toggleValidDateBox();
			}
		});

		validateTextBox = new ValidatableWidget<TextBox>(priority);
		validateTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateTextBox.toggleValidDateBox();
			}
		});
		validateTextBox.addValidator(new EmptyStringValidator(priority));

		validateDescTextBox = new ValidatableWidget<TextBox>(description);
		validateDescTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateDescTextBox.toggleValidDateBox();
			}
		});
		validateDescTextBox.addValidator(new EmptyStringValidator(description));

		validateUNCTextBox = new ValidatableWidget<TextBox>(uncFolder);
		validateUNCTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> arg0) {
				validateUNCTextBox.toggleValidDateBox();

			}
		});
		validateUNCTextBox.addValidator(new EmptyStringValidator(uncFolder));

		editBatchClassViewPanel.setSpacing(BatchClassManagementConstants.FIVE);

		nameLabel.setText(AdminConstants.NAME);
		priorityLabel.setText(AdminConstants.PRIORITY);
		descLabel.setText(AdminConstants.DESCRIPTION);
		uncLabel.setText(AdminConstants.UNC_FOLDER);

		star.setText(AdminConstants.STAR);
		descStar.setText(AdminConstants.STAR);
		uncStar.setText(AdminConstants.STAR);
		nameStar.setText(AdminConstants.STAR);

		nameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		priorityLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		descLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		uncLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);

		star.setStyleName(AdminConstants.FONT_RED_STYLE);
		descStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		uncStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		nameStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		editBatchPanel.setSpacing(BatchClassManagementConstants.TEN);

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
	 * To set Description.
	 * 
	 * @param description String
	 */
	public void setDescription(String description) {
		this.description.setValue(description);
	}

	/**
	 * To get Validate Text Box.
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
	 * To get Validate UNC TextBox.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateUNCTextBox() {
		return validateUNCTextBox;
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
	 * To get Save Button.
	 * 
	 * @return Button
	 */
	public Button getSaveButton() {
		return saveButton;
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
	 * To get Name TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getNameTextBox() {
		return name;
	}

	/**
	 * To get Name.
	 * 
	 * @return String
	 */
	public String getName() {
		return name.getValue();
	}

	/**
	 * To set name.
	 * 
	 * @param name String
	 */
	public void setName(String name) {
		this.name.setValue(name);
	}

	/**
	 * To get Dialog Box.
	 * 
	 * @return DialogBox
	 */
	public DialogBox getDialogBox() {
		return dialogBox;
	}

	/**
	 * To set Dialog Box.
	 * 
	 * @param dialogBox DialogBox
	 */
	public void setDialogBox(DialogBox dialogBox) {
		this.dialogBox = dialogBox;
	}

	/**
	 * To perform operations on OK click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("saveButton")
	public void onOkClick(ClickEvent clickEvent) {
		presenter.onOkClicked();
	}

	/**
	 * To get Validate Name TextBox.
	 * 
	 * @return the validateNameTextBox
	 */
	public ValidatableWidget<TextBox> getValidateNameTextBox() {
		return validateNameTextBox;
	}

	/**
	 * To perform operations on cancel click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("cancelButton")
	public void onCancelClick(ClickEvent clickEvent) {
		dialogBox.hide(true);
	}

	/**
	 * Setter for the system folder text box value.
	 * 
	 * @param systemFolder {@link String}
	 */
	public void setSystemFolder(String systemFolder) {
		this.systemFolder.setText(systemFolder);
	}

	/**
	 * Getter for the system folder text box value.
	 * 
	 * @return {@link String}
	 */
	public String getSystemFolder() {
		return systemFolder.getValue();
	}

	/**
	 * Getter for the system folder text box object.
	 * 
	 * @return {@link TextBox}
	 */
	public TextBox getSystemFolderTextBox() {
		return systemFolder;
	}

	/**
	 * Getter for the validatable system folder text box object.
	 * 
	 * @return the validateSystemFolderTextBox {@link ValidatableWidget < {@link TextBox}>}
	 */
	public ValidatableWidget<TextBox> getValidateSystemFolderTextBox() {
		return validateSystemFolderTextBox;
	}
}
