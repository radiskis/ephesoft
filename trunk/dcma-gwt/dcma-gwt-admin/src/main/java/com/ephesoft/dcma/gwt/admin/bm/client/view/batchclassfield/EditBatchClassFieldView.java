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

package com.ephesoft.dcma.gwt.admin.bm.client.view.batchclassfield;

import java.util.Arrays;
import java.util.List;

import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.batchclassfield.EditBatchClassFieldPresenter;
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
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EditBatchClassFieldView extends View<EditBatchClassFieldPresenter> {

	interface Binder extends UiBinder<VerticalPanel, EditBatchClassFieldView> {
	}

	@UiField
	protected Label nameLabel;
	@UiField
	protected Label nameStar;
	@UiField
	protected TextBox name;

	@UiField
	protected Label descriptionLabel;
	@UiField
	protected Label descriptionStar;
	@UiField
	protected TextBox description;

	@UiField
	protected Label dataTypeLabel;
	@UiField
	protected Label dataTypeStar;
	@UiField
	protected ListBox dataType;

	@UiField
	protected Label fieldOrderNumberLabel;
	@UiField
	protected Label fieldOrderNumberStar;
	@UiField
	protected TextBox fieldOrderNumber;

	@UiField
	protected Label sampleValueLabel;
	@UiField
	protected TextBox sampleValue;

	@UiField
	protected Label validationPatternLabel;
	@UiField
	protected TextBox validationPattern;

	@UiField
	protected Label fieldOptionValueListLabel;
	@UiField
	protected TextBox fieldOptionValueList;

	@UiField
	protected Button saveButton;
	@UiField
	protected Button cancelButton;

	private ValidatableWidget<TextBox> validateNameTextBox;
	private ValidatableWidget<TextBox> validateDescriptionTextBox;
	private ValidatableWidget<TextBox> validateFieldOrderNumberTextBox;
	private ValidatableWidget<TextBox> validateValidationPatternTextBox;

	@UiField
	protected VerticalPanel editBatchClassFieldViewPanel;

	private static final Binder BINDER = GWT.create(Binder.class);

	public EditBatchClassFieldView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		saveButton.setText(AdminConstants.OK_BUTTON);
		cancelButton.setText(AdminConstants.CANCEL_BUTTON);

		createValidatableWidgets();

		editBatchClassFieldViewPanel.setSpacing(5);

		nameLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.BATCH_CLASS_FIELD_NAME)
				+ AdminConstants.COLON);
		descriptionLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.BATCH_CLASS_FIELD_DESCRIPTION)
				+ AdminConstants.COLON);
		dataTypeLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.BATCH_CLASS_FIELD_TYPE)
				+ AdminConstants.COLON);
		fieldOrderNumberLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.BATCH_CLASS_FIELD_ORDER)
				+ AdminConstants.COLON);
		sampleValueLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.BATCH_CLASS_FIELD_SAMPLE)
				+ AdminConstants.COLON);
		validationPatternLabel.setText(LocaleDictionary.get()
				.getConstantValue(BatchClassManagementConstants.BATCH_CLASS_FIELD_PATTERN)
				+ AdminConstants.COLON);
		fieldOptionValueListLabel.setText(LocaleDictionary.get().getConstantValue(
				BatchClassManagementConstants.BATCH_CLASS_FIELD_OVLIST)
				+ AdminConstants.COLON);

		nameStar.setText(AdminConstants.STAR);
		descriptionStar.setText(AdminConstants.STAR);
		dataTypeStar.setText(AdminConstants.STAR);
		fieldOrderNumberStar.setText(AdminConstants.STAR);

		nameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		descriptionLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		dataTypeLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		fieldOrderNumberLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		sampleValueLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		validationPatternLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		fieldOptionValueListLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);

		nameStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		descriptionStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		dataTypeStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		fieldOrderNumberStar.setStyleName(AdminConstants.FONT_RED_STYLE);

	}

	@UiHandler("saveButton")
	public void onSaveClicked(ClickEvent clickEvent) {
		presenter.onSave();
	}

	@UiHandler("cancelButton")
	public void onCancelClicked(ClickEvent clickEvent) {
		presenter.onCancel();
	}

	public String getName() {
		return this.name.getValue();
	}

	public void setName(String name) {
		this.name.setValue(name);
	}

	public String getDescription() {
		return this.description.getValue();
	}

	public void setDescription(String description) {
		this.description.setValue(description);
	}

	public DataType getDataType() {
		String selected = this.dataType.getItemText(this.dataType.getSelectedIndex());
		DataType[] allDataTypes = DataType.values();
		for (DataType dataType2 : allDataTypes) {
			if (dataType2.name().equals(selected)) {
				return dataType2;
			}
		}
		return allDataTypes[0];
	}

	private int findIndex(DataType datatype) {
		if (datatype == null) {
			return 0;
		}
		DataType[] allDataTypes = DataType.values();
		List<DataType> tempList = Arrays.asList(allDataTypes);
		return tempList.indexOf(datatype);
	}

	public void setDataType() {
		this.dataType.setVisibleItemCount(1);
		DataType[] allDataTypes = DataType.values();
		for (DataType dataType2 : allDataTypes) {
			this.dataType.addItem(dataType2.name());
		}
	}

	public void setDataType(DataType datatype) {
		if (this.dataType.getItemCount() == 0) {
			setDataType();
		}
		this.dataType.setSelectedIndex(findIndex(datatype));
	}

	public String getFieldOrderNumber() {
		return this.fieldOrderNumber.getValue();
	}

	public void setFieldOrderNumber(String fieldOrderNumber) {
		this.fieldOrderNumber.setValue(fieldOrderNumber);
	}

	public String getSampleValue() {
		return this.sampleValue.getValue();
	}

	public void setSampleValue(String sampleValue) {
		this.sampleValue.setValue(sampleValue);
	}

	public String getValidationPattern() {
		return this.validationPattern.getValue();
	}

	public void setValidationPattern(String validationPattern) {
		this.validationPattern.setValue(validationPattern);
	}

	public String getFieldOptionValueList() {
		return this.fieldOptionValueList.getValue();
	}

	public void setFieldOptionValueList(String fieldOptionValueList) {
		this.fieldOptionValueList.setValue(fieldOptionValueList);
	}

	public ValidatableWidget<TextBox> getValidateNameTextBox() {
		return validateNameTextBox;
	}

	public ValidatableWidget<TextBox> getValidateDescriptionTextBox() {
		return validateDescriptionTextBox;
	}

	public ValidatableWidget<TextBox> getValidateFieldOrderNumberTextBox() {
		return validateFieldOrderNumberTextBox;
	}

	public ValidatableWidget<TextBox> getValidateValidationPatternTextBox() {
		return validateValidationPatternTextBox;
	}

	public TextBox getNameTextBox() {
		return this.name;
	}

	public TextBox getDescriptionTextBox() {
		return this.description;
	}

	public TextBox getFieldOrderNumberTextBox() {
		return this.fieldOrderNumber;
	}

	public TextBox getSampleValueTextBox() {
		return this.sampleValue;
	}

	public TextBox getValidationPatternTextBox() {
		return this.validationPattern;
	}

	public TextBox getFieldOptionValueListTextBox() {
		return this.fieldOptionValueList;
	}

	public Label getFieldOrderNumberLabel() {
		return fieldOrderNumberLabel;
	}

	public void createValidatableWidgets() {
		validateNameTextBox = new ValidatableWidget<TextBox>(name);
		validateNameTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateNameTextBox.toggleValidDateBox();
			}
		});

		validateDescriptionTextBox = new ValidatableWidget<TextBox>(description);
		validateDescriptionTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateDescriptionTextBox.toggleValidDateBox();
			}
		});

		validateFieldOrderNumberTextBox = new ValidatableWidget<TextBox>(fieldOrderNumber);
		validateFieldOrderNumberTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateFieldOrderNumberTextBox.toggleValidDateBox();
			}
		});

		validateValidationPatternTextBox = new ValidatableWidget<TextBox>(validationPattern);

	}

}
