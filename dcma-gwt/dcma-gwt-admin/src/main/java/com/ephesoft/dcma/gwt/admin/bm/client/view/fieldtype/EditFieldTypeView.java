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

package com.ephesoft.dcma.gwt.admin.bm.client.view.fieldtype;

import java.util.Arrays;
import java.util.List;

import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.fieldtype.EditFieldTypePresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.validator.RegExValidatableWidget;
import com.ephesoft.dcma.gwt.core.client.validator.ValidatableWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class provides functionality to edit field type.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class EditFieldTypeView extends View<EditFieldTypePresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, EditFieldTypeView> {
	}

	/**
	 * nameLabel Label.
	 */
	@UiField
	protected Label nameLabel;

	/**
	 * nameStar Label.
	 */
	@UiField
	protected Label nameStar;

	/**
	 * name TextBox.
	 */
	@UiField
	protected TextBox name;

	/**
	 * descLabel Label.
	 */
	@UiField
	protected Label descLabel;

	/**
	 * descStar Label.
	 */
	@UiField
	protected Label descStar;

	/**
	 * description TextBox.
	 */
	@UiField
	protected TextBox description;

	/**
	 * dataTypeLabel Label.
	 */
	@UiField
	protected Label dataTypeLabel;

	/**
	 * dataTypeStar Label.
	 */
	@UiField
	protected Label dataTypeStar;

	/**
	 * dataType ListBox.
	 */
	@UiField
	protected ListBox dataType;

	/**
	 * patternLabel Label.
	 */
	@UiField
	protected Label patternLabel;

	/**
	 * To get Pattern Label.
	 * 
	 * @return Label
	 */
	public Label getPatternLabel() {
		return patternLabel;
	}

	/**
	 * To set Pattern Label.
	 * 
	 * @param patternLabel Label
	 */
	public void setPatternLabel(Label patternLabel) {
		this.patternLabel = patternLabel;
	}

	/**
	 * patternStar Label.
	 */
	@UiField
	protected Label patternStar;

	/**
	 * pattern TextBox.
	 */
	@UiField
	protected TextBox pattern;

	/**
	 * fdOrderLabel Label.
	 */
	@UiField
	protected Label fdOrderLabel;

	/**
	 * fdOrderStar Label.
	 */
	@UiField
	protected Label fdOrderStar;

	/**
	 * fdOrder TextBox.
	 */
	@UiField
	protected TextBox fdOrder;

	/**
	 * sampleValueLabel Label.
	 */
	@UiField
	protected Label sampleValueLabel;

	/**
	 * sampleValueStar Label.
	 */
	@UiField
	protected Label sampleValueStar;

	/**
	 * sampleValue TextBox.
	 */
	@UiField
	protected TextBox sampleValue;

	/**
	 * fieldOptionValueListLabel Label.
	 */
	@UiField
	protected Label fieldOptionValueListLabel;

	/**
	 * fieldOptionValueList TextBox.
	 */
	@UiField
	protected TextBox fieldOptionValueList;

	/**
	 * barcodeTypeLabel Label.
	 */
	@UiField
	protected Label barcodeTypeLabel;

	/**
	 * barcodeTypeListBox ListBox.
	 */
	@UiField
	protected ListBox barcodeTypeListBox;

	/**
	 * isHiddenLabel Label.
	 */
	@UiField
	protected Label isHiddenLabel;

	/**
	 * hidden CheckBox.
	 */
	@UiField
	protected CheckBox hidden;

	/**
	 * isMultiLineLabel Label.
	 */
	@UiField
	protected Label isMultiLineLabel;

	/**
	 * multiLine CheckBox.
	 */
	@UiField
	protected CheckBox multiLine;

	/**
	 * isReadOnly CheckBox.
	 */
	@UiField
	protected CheckBox isReadOnly;

	/**
	 * isReadOnlyLabel Label.
	 */
	@UiField
	protected Label isReadOnlyLabel;

	/**
	 * saveButton Button.
	 */
	@UiField
	protected Button saveButton;

	/**
	 * samplePatternButton Button.
	 */
	@UiField
	protected Button samplePatternButton;

	/**
	 * validateButton Button.
	 */
	@UiField
	protected Button validateButton;

	/**
	 * To get Is MultiLine.
	 * 
	 * @return CheckBox
	 */
	public CheckBox getIsMultiLine() {
		return multiLine;
	}

	/**
	 * To get Is Hidden.
	 * 
	 * @return CheckBox
	 */
	public CheckBox getIsHidden() {
		return hidden;
	}

	/**
	 * To get Is Readonly.
	 * 
	 * @return CheckBox
	 */
	public CheckBox getIsReadonly() {
		return isReadOnly;
	}

	/**
	 * cancelButton Button.
	 */
	@UiField
	protected Button cancelButton;

	/**
	 * validateNameTextBox ValidatableWidget<TextBox>.
	 */
	private ValidatableWidget<TextBox> validateNameTextBox;

	/**
	 * validateDescriptionTextBox ValidatableWidget<TextBox>.
	 */
	private ValidatableWidget<TextBox> validateDescriptionTextBox;

	/**
	 * validatePatternTextBox ValidatableWidget<TextBox>.
	 */
	private RegExValidatableWidget<TextBox> validatePatternTextBox;

	/**
	 * validateFdOrderTextBox ValidatableWidget<TextBox>.
	 */
	private ValidatableWidget<TextBox> validateFdOrderTextBox;

	/**
	 * editDocumentTypeViewPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel editDocumentTypeViewPanel;

	/**
	 * allBarcodeValues List<String>.
	 */
	private List<String> allBarcodeValues = null;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Constructor.
	 */
	public EditFieldTypeView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		saveButton.setText(AdminConstants.OK_BUTTON);
		cancelButton.setText(AdminConstants.CANCEL_BUTTON);
		samplePatternButton.setText(AdminConstants.SAMPLE_REGEX_BUTTON);
		validateButton.setTitle(AdminConstants.VALIDATE_BUTTON);
		validateButton.setStyleName(AdminConstants.VALIDATE_BUTTON_IMAGE);

		// This is added to make it compatible with all browsers
		samplePatternButton.addStyleName("sample_regex_button");
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

		validatePatternTextBox = new RegExValidatableWidget<TextBox>(pattern);
		validatePatternTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validatePatternTextBox.setValid(false);
			}
		});

		validateFdOrderTextBox = new ValidatableWidget<TextBox>(fdOrder);
		validateFdOrderTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateFdOrderTextBox.toggleValidDateBox();
			}
		});

		editDocumentTypeViewPanel.setSpacing(BatchClassManagementConstants.FIVE);

		nameLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.NAME) + ":");
		descLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.DESCRIPTION) + ":");
		dataTypeLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.DATA_TYPE) + ":");
		patternLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.PATTERN) + ":");
		fdOrderLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.FIELD_ORDER) + ":");
		sampleValueLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.SAMPLE_VALUE)
				+ AdminConstants.COLON);

		fieldOptionValueListLabel.setText(LocaleDictionary.get().getConstantValue(
				BatchClassManagementConstants.FIELD_OPTION_VALUE_LIST)
				+ AdminConstants.COLON);
		barcodeTypeLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.BARCODE_TYPE)
				+ AdminConstants.COLON);
		isHiddenLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.IS_HIDDEN) + AdminConstants.COLON);
		isMultiLineLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.IS_MULTILINE)
				+ AdminConstants.COLON);
		isReadOnlyLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.IS_READONLY)
				+ AdminConstants.COLON);
		nameStar.setText(AdminConstants.STAR);
		descStar.setText(AdminConstants.STAR);
		patternStar.setText(AdminConstants.STAR);
		dataTypeStar.setText(AdminConstants.STAR);
		fdOrderStar.setText(AdminConstants.STAR);
		nameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		descLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		dataTypeLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		patternLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		fdOrderLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		sampleValueLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		fieldOptionValueListLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		nameStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		descStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		dataTypeStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		patternStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		fdOrderStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		barcodeTypeLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		isHiddenLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		isMultiLineLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		isReadOnlyLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
	}

	/**
	 * To perform operations on Save Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("saveButton")
	public void onSaveClicked(ClickEvent clickEvent) {
		presenter.onSave();

	}

	/**
	 * To perform operations on cancel Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("cancelButton")
	public void onCancelClicked(ClickEvent clickEvent) {
		presenter.onCancel();
	}

	/**
	 * To perform operations on sample pattern button click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("samplePatternButton")
	public void onSamplePatternButtonClicked(ClickEvent clickEvent) {
		presenter.getController().getMainPresenter().getSamplePatterns();
	}

	/**
	 * To perform operations on validate pattern button click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("validateButton")
	public void onValidateButtonClicked(ClickEvent clickEvent) {
		presenter.onValidateButtonClicked();
	}

	/**
	 * To set Name.
	 * 
	 * @param name TextBox
	 */
	public void setName(TextBox name) {
		this.name = name;
	}

	/**
	 * To set Name.
	 * 
	 * @param name String
	 */
	public void setName(String name) {
		this.name.setValue(name);
	}

	/**
	 * To get Name.
	 * 
	 * @return String
	 */
	public String getName() {
		return this.name.getValue();
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
	 * To get Description.
	 * 
	 * @return String
	 */
	public String getDescription() {
		return this.description.getValue();
	}

	/**
	 * To get Data Type.
	 * 
	 * @return DataType
	 */
	public DataType getDataType() {
		String selected = this.dataType.getItemText(this.dataType.getSelectedIndex());
		DataType[] allDataTypes = DataType.values();
		DataType tempDataType = allDataTypes[0];
		for (DataType dataType2 : allDataTypes) {
			if (dataType2.name().equals(selected)) {
				tempDataType = dataType2;
				break;
			}
		}
		return tempDataType;
	}

	/**
	 * To get Barcode Type.
	 * 
	 * @return String
	 */
	public String getBarcodeType() {
		return this.barcodeTypeListBox.getItemText(this.barcodeTypeListBox.getSelectedIndex());
	}

	private int findIndex(DataType datatype) {
		int index = 0;
		if (datatype != null) {
			DataType[] allDataTypes = DataType.values();
			List<DataType> tempList = Arrays.asList(allDataTypes);
			index = tempList.indexOf(datatype);
		}
		return index;
	}

	private int findIndex(String barcodeType) {
		int index = 0;
		if (barcodeType != null && !barcodeType.isEmpty() && allBarcodeValues != null && !allBarcodeValues.isEmpty()) {
			index = (allBarcodeValues.indexOf(barcodeType)) + 1;
		}
		return index;
	}

	/**
	 * To set Data Type.
	 */
	public void setDataType() {
		this.dataType.setVisibleItemCount(1);
		DataType[] allDataTypes = DataType.values();
		for (DataType dataType2 : allDataTypes) {
			this.dataType.addItem(dataType2.name());
		}
	}

	/**
	 * To set Barcode Type.
	 */
	public void setBarcodeType() {
		this.barcodeTypeListBox.setVisibleItemCount(1);
		this.barcodeTypeListBox.addItem(AdminConstants.EMPTY_STRING);
		if (allBarcodeValues != null && !allBarcodeValues.isEmpty()) {
			for (String barcodeType : allBarcodeValues) {
				this.barcodeTypeListBox.addItem(barcodeType);
			}
		}
	}

	/**
	 * To set Data Type.
	 * 
	 * @param datatype DataType
	 */
	public void setDataType(DataType datatype) {
		if (this.dataType.getItemCount() == 0) {
			setDataType();
		}
		this.dataType.setSelectedIndex(findIndex(datatype));
	}

	/**
	 * To set Barcode Type.
	 * 
	 * @param barcodeType String
	 */
	public void setBarcodeType(String barcodeType) {
		if (this.barcodeTypeListBox.getItemCount() == 0) {
			setBarcodeType();
		}
		this.barcodeTypeListBox.setSelectedIndex(findIndex(barcodeType));
	}

	/**
	 * To get Validate Name TextBox.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateNameTextBox() {
		return validateNameTextBox;
	}

	/**
	 * To set validate Name TextBox.
	 * 
	 * @param validateNameTextBox ValidatableWidget<TextBox>
	 */
	public void setValidateNameTextBox(ValidatableWidget<TextBox> validateNameTextBox) {
		this.validateNameTextBox = validateNameTextBox;
	}

	/**
	 * To get Validate Description TextBox.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateDescriptionTextBox() {
		return validateDescriptionTextBox;
	}

	/**
	 * To set Validate Description TextBox.
	 * 
	 * @param validateDescriptionTextBox ValidatableWidget<TextBox>
	 */
	public void setValidateDescriptionTextBox(ValidatableWidget<TextBox> validateDescriptionTextBox) {
		this.validateDescriptionTextBox = validateDescriptionTextBox;
	}

	/**
	 * To get Validate Pattern TextBox.
	 * 
	 * @return RegExValidatableWidget<TextBox>
	 */
	public RegExValidatableWidget<TextBox> getValidatePatternTextBox() {
		return validatePatternTextBox;
	}

	/**
	 * To set Validate Pattern TextBox.
	 * 
	 * @param validatePatternTextBox RegExValidatableWidget<TextBox>
	 */
	public void setValidatePatternTextBox(RegExValidatableWidget<TextBox> validatePatternTextBox) {
		this.validatePatternTextBox = validatePatternTextBox;
	}

	/**
	 * To get Validate Fd Order TextBox.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateFdOrderTextBox() {
		return validateFdOrderTextBox;
	}

	/**
	 * To set Validate Fd Order TextBox.
	 * 
	 * @param validateFdOrderTextBox ValidatableWidget<TextBox>
	 */
	public void setValidateFdOrderTextBox(ValidatableWidget<TextBox> validateFdOrderTextBox) {
		this.validateFdOrderTextBox = validateFdOrderTextBox;
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
	 * To get Description TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getDescriptionTextBox() {
		return description;
	}

	/**
	 * To get pattern.
	 * 
	 * @return String
	 */
	public String getPattern() {
		return this.pattern.getValue();
	}

	/**
	 * To set pattern.
	 * 
	 * @param pattern String
	 */
	public void setPattern(String pattern) {
		this.pattern.setText(pattern);
	}

	/**
	 * To get Pattern TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getPatternTextBox() {
		return pattern;
	}

	/**
	 * To get Fd Order.
	 * 
	 * @return String
	 */
	public String getFdOrder() {
		return fdOrder.getText();
	}

	/**
	 * To set Fd Order.
	 * 
	 * @param fdOrder String
	 */
	public void setFdOrder(String fdOrder) {
		this.fdOrder.setText(fdOrder);
	}

	/**
	 * To get Fd Order TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getFdOrderTextBox() {
		return this.fdOrder;
	}

	/**
	 * To get Fd Order label.
	 * 
	 * @return Label
	 */
	public Label getFdOrderLabel() {
		return fdOrderLabel;
	}

	/**
	 * To get Sample Value.
	 * 
	 * @return String
	 */
	public String getSampleValue() {
		return this.sampleValue.getText();
	}

	/**
	 * To set Sample Value.
	 * 
	 * @param sampleValue2
	 */
	public void setSampleValue(String sampleValue2) {
		this.sampleValue.setText(sampleValue2);
	}

	/**
	 * To get Field Option Value List.
	 * 
	 * @return String
	 */
	public String getFieldOptionValueList() {
		return this.fieldOptionValueList.getText();
	}

	/**
	 * To set Field Option Value List.
	 * 
	 * @param fieldValueList String
	 */
	public void setFieldOptionValueList(String fieldValueList) {
		this.fieldOptionValueList.setText(fieldValueList);
	}

	/**
	 * To set all Barcode Values.
	 * 
	 * @param allBarcodeValues List<String>
	 */
	public void setAllBarcodeValues(List<String> allBarcodeValues) {
		this.allBarcodeValues = allBarcodeValues;
	}

	/**
	 * To get value of hidden.
	 * 
	 * @return boolean
	 */
	public boolean isHidden() {
		return this.hidden.getValue();
	}

	/**
	 * To set Hidden.
	 * 
	 * @param isHidden boolean
	 */
	public void setHidden(boolean isHidden) {
		this.hidden.setValue(isHidden);
	}

	/**
	 * To get value of multiline.
	 * 
	 * @return boolean
	 */
	public boolean isMultiLine() {
		return this.multiLine.getValue();
	}

	/**
	 * To set MultiLine.
	 * 
	 * @param isMultiLine boolean
	 */
	public void setMultiLine(boolean isMultiLine) {
		this.multiLine.setValue(isMultiLine);
	}

	/**
	 * To set Save Button Enable.
	 * 
	 * @param isEnable boolean
	 */
	public void setSaveButtonEnable(boolean isEnable) {
		saveButton.setEnabled(isEnable);

	}

	/**
	 * API to get the boolean value of isReadOnly checkbox.
	 * 
	 * @return boolean
	 */
	public boolean getIsReadonlyValue() {
		return this.isReadOnly.getValue();
	}

	/**
	 * API to set the 'isReadOnly' property for document level field.
	 * 
	 * @param isReadOnly boolean
	 */
	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly.setValue(isReadOnly);
	}

}
