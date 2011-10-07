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

package com.ephesoft.dcma.gwt.admin.bm.client.view.batch;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.batch.CopyBatchClassPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
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

public class CopyBatchClassView extends View<CopyBatchClassPresenter> {

	interface Binder extends UiBinder<VerticalPanel, CopyBatchClassView> {
	}

	@UiField
	protected TextBox priority;
	@UiField
	protected TextBox uncFolder;
	@UiField
	protected TextBox description;
	@UiField
	protected TextBox name;

	@UiField
	protected Button saveButton;
	@UiField
	protected Button cancelButton;

	@UiField
	protected Label priorityLabel;
	@UiField
	protected Label descLabel;
	@UiField
	protected Label nameLabel;
	@UiField
	protected Label uncLabel;

	@UiField
	protected Label star;

	@UiField
	protected Label uncStar;

	@UiField
	protected HorizontalPanel editBatchPanel;

	@UiField
	protected Label descStar;

	private final ValidatableWidget<TextBox> validateTextBox;
	private final ValidatableWidget<TextBox> validateDescTextBox;
	private final ValidatableWidget<TextBox> validateUNCTextBox;

	private DialogBox dialogBox;

	@UiField
	protected VerticalPanel editBatchClassViewPanel;

	private static final Binder BINDER = GWT.create(Binder.class);

	public CopyBatchClassView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		saveButton.setText(AdminConstants.SAVE_BUTTON);
		cancelButton.setText(AdminConstants.CANCEL_BUTTON);
		
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

		editBatchClassViewPanel.setSpacing(5);

		nameLabel.setText(AdminConstants.NAME);
		priorityLabel.setText(AdminConstants.PRIORITY);
		descLabel.setText(AdminConstants.DESCRIPTION);
		uncLabel.setText(AdminConstants.UNC_FOLDER);

		star.setText(AdminConstants.STAR);
		descStar.setText(AdminConstants.STAR);
		uncStar.setText(AdminConstants.STAR);

		nameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		priorityLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		descLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		uncLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);

		star.setStyleName(AdminConstants.FONT_RED_STYLE);
		descStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		uncStar.setStyleName(AdminConstants.FONT_RED_STYLE);

		editBatchPanel.setSpacing(10);
	}

	public String getPriority() {
		return priority.getValue();
	}

	public void setPriority(String priority) {
		this.priority.setValue(priority);
	}

	public String getUncFolder() {
		return uncFolder.getText();
	}

	public void setUncFolder(String uncFolder) {
		this.uncFolder.setText(uncFolder);
	}

	public String getDescription() {
		return description.getValue();
	}

	public void setDescription(String description) {
		this.description.setValue(description);
	}

	public ValidatableWidget<TextBox> getValidateTextBox() {
		return validateTextBox;
	}

	public ValidatableWidget<TextBox> getValidateDescTextBox() {
		return validateDescTextBox;
	}

	public ValidatableWidget<TextBox> getValidateUNCTextBox() {
		return validateUNCTextBox;
	}

	public TextBox getPriorityTextBox() {
		return priority;
	}

	public TextBox getDescriptionTextBox() {
		return description;
	}

	public Button getSaveButton() {
		return saveButton;
	}

	public Button getCancelButton() {
		return cancelButton;
	}

	public TextBox getNameTextBox() {
		return name;
	}

	public String getName() {
		return name.getValue();
	}

	public void setName(String name) {
		this.name.setValue(name);
	}

	public DialogBox getDialogBox() {
		return dialogBox;
	}

	public void setDialogBox(DialogBox dialogBox) {
		this.dialogBox = dialogBox;
	}

	@UiHandler("saveButton")
	public void onOkClick(ClickEvent clickEvent) {
		presenter.onOkClicked();
	}

	@UiHandler("cancelButton")
	public void onCancelClick(ClickEvent clickEvent) {
		dialogBox.hide(true);
	}
}
