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

package com.ephesoft.dcma.gwt.admin.bm.client.view.documenttype;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.documenttype.CopyDocumentTypePresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.validator.EmptyStringValidator;
import com.ephesoft.dcma.gwt.core.client.validator.NumberValidator;
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

public class CopyDocumentView extends View<CopyDocumentTypePresenter> {

	interface Binder extends UiBinder<VerticalPanel, CopyDocumentView> {
	}

	@UiField
	Label nameLabel;
	@UiField
	Label nameStar;
	@UiField
	TextBox name;

	@UiField
	Label descLabel;
	@UiField
	Label descStar;
	@UiField
	TextBox description;

	@UiField
	Label minConfidenceThresholdLabel;
	@UiField
	Label minConfidenceThresholdStar;
	@UiField
	TextBox minConfidenceThreshold;

	@UiField
	Button saveButton;
	@UiField
	Button cancelButton;

	@UiField
	HorizontalPanel editDocumentPanel;

	private ValidatableWidget<TextBox> validateNameTextBox;
	private ValidatableWidget<TextBox> validateDescriptionTextBox;
	private ValidatableWidget<TextBox> validatePriorityTextBox;

	private DialogBox dialogBox;

	@UiField
	VerticalPanel editDocumentViewPanel;

	private static final Binder binder = GWT.create(Binder.class);

	public CopyDocumentView() {
		initWidget(binder.createAndBindUi(this));

		saveButton.setText(AdminConstants.SAVE_BUTTON);
		cancelButton.setText(AdminConstants.CANCEL_BUTTON);
		
		validatePriorityTextBox = new ValidatableWidget<TextBox>(minConfidenceThreshold);
		validatePriorityTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validatePriorityTextBox.toggleValidDateBox();
			}
		});

		validatePriorityTextBox.addValidator(new NumberValidator(minConfidenceThreshold));

		validateDescriptionTextBox = new ValidatableWidget<TextBox>(description);
		validateDescriptionTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateDescriptionTextBox.toggleValidDateBox();
			}
		});
		validateDescriptionTextBox.addValidator(new EmptyStringValidator(description));

		validateNameTextBox = new ValidatableWidget<TextBox>(name);
		validateNameTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> arg0) {
				validateNameTextBox.toggleValidDateBox();

			}
		});
		validateNameTextBox.addValidator(new EmptyStringValidator(name));

		editDocumentViewPanel.setSpacing(5);

		nameLabel.setText(AdminConstants.NAME);
		minConfidenceThresholdLabel.setText(AdminConstants.MIN_CONFIDENCE_THRESHOLD);
		descLabel.setText(AdminConstants.DESCRIPTION);

		minConfidenceThresholdStar.setText(AdminConstants.STAR);
		descStar.setText(AdminConstants.STAR);
		nameStar.setText(AdminConstants.STAR);

		nameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		minConfidenceThresholdLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		descLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);

		minConfidenceThresholdStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		descStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		nameStar.setStyleName(AdminConstants.FONT_RED_STYLE);

		editDocumentPanel.setSpacing(10);
	}

	public String getMinConfidenceThreshold() {
		return minConfidenceThreshold.getValue();
	}

	public void setMinConfidenceThreshold(String priority) {
		this.minConfidenceThreshold.setValue(priority);
	}

	public String getDescription() {
		return description.getValue();
	}

	public void setDescription(String description) {
		this.description.setValue(description);
	}

	public ValidatableWidget<TextBox> getValidateNameTextBox() {
		return validateNameTextBox;
	}

	public ValidatableWidget<TextBox> getValidateDescriptionTextBox() {
		return validateDescriptionTextBox;
	}

	public ValidatableWidget<TextBox> getValidatePriorityTextBox() {
		return validatePriorityTextBox;
	}

	public TextBox getMinConfidenceThresholdTextBox() {
		return minConfidenceThreshold;
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
