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

package com.ephesoft.dcma.gwt.admin.bm.client.view.tableinfo;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.tableinfo.EditTableInfoPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.validator.ValidatableWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EditTableInfoView extends View<EditTableInfoPresenter> {

	interface Binder extends UiBinder<VerticalPanel, EditTableInfoView> {
	}

	@UiField
	Label nameLabel;
	@UiField
	Label nameStar;
	@UiField
	TextBox name;

	@UiField
	Label startPatternLabel;
	@UiField
	Label startPatternStar;
	@UiField
	TextBox startPattern;

	@UiField
	Label endPatternLabel;
	@UiField
	Label endPatternStar;
	@UiField
	TextBox endPattern;

	@UiField
	Button saveButton;
	@UiField
	Button cancelButton;

	private ValidatableWidget<TextBox> validateNameTextBox;
	private ValidatableWidget<TextBox> validateStartPatternTextBox;
	private ValidatableWidget<TextBox> validateEndPatternTextBox;
	

	@UiField
	VerticalPanel editTableInfoViewPanel;

	private static final Binder binder = GWT.create(Binder.class);

	public EditTableInfoView() {
		initWidget(binder.createAndBindUi(this));
		saveButton.setText(AdminConstants.OK_BUTTON);
		cancelButton.setText(AdminConstants.CANCEL_BUTTON);
		validateNameTextBox = new ValidatableWidget<TextBox>(name);
		validateNameTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateNameTextBox.toggleValidDateBox();
			}
		});

		validateStartPatternTextBox = new ValidatableWidget<TextBox>(startPattern);
		validateStartPatternTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateStartPatternTextBox.toggleValidDateBox();
			}
		});

		validateEndPatternTextBox = new ValidatableWidget<TextBox>(endPattern);
		validateEndPatternTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateEndPatternTextBox.toggleValidDateBox();
			}
		});


		editTableInfoViewPanel.setSpacing(5);

		nameLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.NAME) + ":");
		startPatternLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.START_PATTERN) + ":");
		endPatternLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.END_PATTERN) + ":");

		nameStar.setText(AdminConstants.STAR);
		startPatternStar.setText(AdminConstants.STAR);
		endPatternStar.setText(AdminConstants.STAR);
		
		nameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		startPatternLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		endPatternLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);

		nameStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		startPatternStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		endPatternStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		
	}

	@UiHandler("saveButton")
	void onSaveClicked(ClickEvent clickEvent) {
		presenter.onSave();
	}

	@UiHandler("cancelButton")
	void onCancelClicked(ClickEvent clickEvent) {
		presenter.onCancel();
	}

	public ValidatableWidget<TextBox> getValidateNameTextBox() {
		return validateNameTextBox;
	}

	public void setValidateNameTextBox(ValidatableWidget<TextBox> validateNameTextBox) {
		this.validateNameTextBox = validateNameTextBox;
	}

	public ValidatableWidget<TextBox> getValidateStartPatternTextBox() {
		return validateStartPatternTextBox;
	}
	
	public void setValidateStartPatternTextBox(
			ValidatableWidget<TextBox> validateStartPatternTextBox) {
		this.validateStartPatternTextBox = validateStartPatternTextBox;
	}
	
	public ValidatableWidget<TextBox> getValidateEndPatternTextBox() {
		return validateEndPatternTextBox;
	}
	
	public void setValidateEndPatternTextBox(
			ValidatableWidget<TextBox> validateEndPatternTextBox) {
		this.validateEndPatternTextBox = validateEndPatternTextBox;
	}
	
	public void setName(TextBox name) {
		this.name = name;
	}

	public void setName(String name) {
		this.name.setValue(name);
	}

	public String getName() {
		return this.name.getValue();
	}
	
	public TextBox getNameTextBox() {
		return name;
	}

	public void setStartPattern(String startPattern) {
		this.startPattern.setValue(startPattern);
	}

	public String getStartPattern() {
		return this.startPattern.getValue();
	}
	
	public TextBox getStartPatternTextBox() {
		return startPattern;
	}
	
	public void setEndPattern(String endPattern) {
		this.endPattern.setValue(endPattern);
	}

	public String getEndPattern() {
		return this.endPattern.getValue();
	}
	
	public TextBox getEndPatternTextBox() {
		return endPattern;
	}
}
