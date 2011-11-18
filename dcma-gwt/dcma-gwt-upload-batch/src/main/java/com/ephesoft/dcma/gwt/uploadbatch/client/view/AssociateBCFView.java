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

package com.ephesoft.dcma.gwt.uploadbatch.client.view;

import java.util.Arrays;
import java.util.List;

import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.validator.RegExValidator;
import com.ephesoft.dcma.gwt.core.client.validator.ValidatableWidget;
import com.ephesoft.dcma.gwt.core.client.validator.Validator;
import com.ephesoft.dcma.gwt.core.shared.BatchClassFieldDTO;
import com.ephesoft.dcma.gwt.uploadbatch.client.i18n.UploadBatchMessages;
import com.ephesoft.dcma.gwt.uploadbatch.client.presenter.AssociateBCFPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AssociateBCFView extends View<AssociateBCFPresenter> {

	interface Binder extends UiBinder<FlexTable, AssociateBCFView> {
	}

	@UiField
	FlexTable flexEditTable;

	private Label validationMessage;

	private FlexTable editTable;

	private Button cancel;
	private Button save;

	private DialogBox dialogBox;

	private static final Binder binder = GWT.create(Binder.class);

	public AssociateBCFView() {
		initWidget(binder.createAndBindUi(this));
		validationMessage = new Label();
	}

	public FlexTable getFlexEditTable() {
		return flexEditTable;
	}

	public void setView() {

		cancel = new Button();
		cancel.setText("Cancel");
		cancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				validationMessage = new Label();
				dialogBox.hide();
			}
		});
		save = new Button();
		save.setText("Ok");
		save.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				presenter.onSave();
				if (presenter.isSetVisibleDialogue()) {
					dialogBox.hide();
				}
			}
		});
		editTable = new FlexTable();
		editTable.setWidth("100%");
		flexEditTable.setWidget(1, 0, editTable);
		flexEditTable.setWidget(0, 0, validationMessage);
		flexEditTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
	}

	public void formatRow(int row) {
		editTable.getCellFormatter().setWidth(row, 0, "40%");
		editTable.getFlexCellFormatter().addStyleName(row, 0, "blk_bold_text");
		editTable.getFlexCellFormatter().setAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
		// editTable.getCellFormatter().setWidth(row, 1, "1%");
		editTable.getCellFormatter().setWidth(row, 1, "60%");
		editTable.getFlexCellFormatter().addStyleName(row, 0, "blk_bold_text");
	}

	public void addWidget(int row, int column, Widget widget) {
		editTable.setWidget(row, column, widget);
	}

	public void addWidgetStar(int row, int column) {
		Label star = new Label("*");
		editTable.setWidget(row, column, star);
		star.setStyleName("red_star");
	}

	public Button getSave() {
		return save;
	}

	public Button getCancel() {
		return cancel;
	}

	public void addButtons(int row) {
		editTable.setWidget(row, 0, save);
		editTable.setWidget(row, 1, cancel);
		editTable.getFlexCellFormatter().setAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
	}

	public ValidatableWidget<TextBox> addTextBox(final BatchClassFieldDTO batchClassFieldDTO) {
		TextBox fieldValue = new TextBox();
		fieldValue.setText(batchClassFieldDTO.getValue());
		final ValidatableWidget<TextBox> validatableTextBox = new ValidatableWidget<TextBox>(fieldValue);
		if (batchClassFieldDTO != null) {
			if(batchClassFieldDTO.getValidationPattern()!=null && !batchClassFieldDTO.getValidationPattern().isEmpty()){
			validatableTextBox.addValidator((Validator) new RegExValidator(batchClassFieldDTO.getValidationPattern(), fieldValue));
			validatableTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					validatableTextBox.toggleValidDateBox();
					if (!validatableTextBox.validate()) {
						validationMessage.setStyleName("error_style");
						validationMessage.setText(LocaleDictionary.get().getMessageValue(
								UploadBatchMessages.BCF_VALIDATION_REGEX_TYPE_MESSAGE)
								+ batchClassFieldDTO.getSampleValue());
					} else {
						validationMessage.setText("");
					}
				}
			});
			validatableTextBox.toggleValidDateBox();
			}
		}

		return validatableTextBox;
	}

	public ListBox addDropDown(String fieldOptionValueList, String value) {
		ListBox fieldValue = new ListBox();
		fieldValue.setVisibleItemCount(1);
		String[] selectedValue = fieldOptionValueList.split(";");
		List<String> selectedValueList = Arrays.asList(selectedValue);
		int i = 0;
		for (String item : selectedValueList) {
			if (!item.trim().isEmpty()) {
				fieldValue.addItem(item);
				i++;
			}
		}
		if(value!=null && !value.isEmpty()){
			fieldValue.setItemSelected(selectedValueList.indexOf(value), true);
		}
		return fieldValue;
	}

	public DialogBox getDialogBox() {
		return dialogBox;
	}

	public void setDialogBox(DialogBox dialogBox) {
		this.dialogBox = dialogBox;
	}

	public void setSave(Button save) {
		this.save = save;
	}

	public Label getValidationMessage() {
		return validationMessage;
	}

	public void setValidationMessage(Label validationMessage) {
		this.validationMessage = validationMessage;
	}

}
