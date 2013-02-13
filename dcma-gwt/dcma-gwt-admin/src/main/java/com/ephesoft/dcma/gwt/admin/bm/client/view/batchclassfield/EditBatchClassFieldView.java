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

package com.ephesoft.dcma.gwt.admin.bm.client.view.batchclassfield;

import java.util.Arrays;
import java.util.List;

import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.batchclassfield.EditBatchClassFieldPresenter;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class provides functionality to edit individual batch class field and it's child.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class EditBatchClassFieldView extends View<EditBatchClassFieldPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, EditBatchClassFieldView> {
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
	 * descriptionLabel Label.
	 */
	@UiField
	protected Label descriptionLabel;

	/**
	 * descriptionStar Label.
	 */
	@UiField
	protected Label descriptionStar;

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
	 * fieldOrderNumberLabel Label.
	 */
	@UiField
	protected Label fieldOrderNumberLabel;

	/**
	 * fieldOrderNumberStar Label.
	 */
	@UiField
	protected Label fieldOrderNumberStar;

	/**
	 * fieldOrderNumber TextBox.
	 */
	@UiField
	protected TextBox fieldOrderNumber;

	/**
	 * sampleValueLabel Label.
	 */
	@UiField
	protected Label sampleValueLabel;

	/**
	 * sampleValue TextBox.
	 */
	@UiField
	protected TextBox sampleValue;

	/**
	 * validationPatternLabel Label.
	 */
	@UiField
	protected Label validationPatternLabel;

	/**
	 * validationPattern TextBox.
	 */
	@UiField
	protected TextBox validationPattern;

	/**
	 * To get Validation Pattern Label.
	 * 
	 * @return Label
	 */
	public Label getValidationPatternLabel() {
		return validationPatternLabel;
	}

	/**
	 * To set Validation Pattern Label.
	 * 
	 * @param validationPatternLabel Label
	 */
	public void setValidationPatternLabel(Label validationPatternLabel) {
		this.validationPatternLabel = validationPatternLabel;
	}

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
	 * scrollPanel ScrollPanel.
	 */
	@UiField
	protected ScrollPanel scrollPanel;

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
	 * validateNameTextBox ValidatableWidget<TextBox>.
	 */
	private ValidatableWidget<TextBox> validateNameTextBox;

	/**
	 * validateDescriptionTextBox ValidatableWidget<TextBox>.
	 */
	private ValidatableWidget<TextBox> validateDescriptionTextBox;

	/**
	 * validateFieldOrderNumberTextBox ValidatableWidget<TextBox>.
	 */
	private ValidatableWidget<TextBox> validateFieldOrderNumberTextBox;

	/**
	 * validateValidationPatternTextBox RegExValidatableWidget<TextBox>.
	 */
	private RegExValidatableWidget<TextBox> validateValidationPatternTextBox;

	/**
	 * editBatchClassFieldViewPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel editBatchClassFieldViewPanel;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Constructor.
	 */
	public EditBatchClassFieldView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		saveButton.setText(AdminConstants.OK_BUTTON);
		cancelButton.setText(AdminConstants.CANCEL_BUTTON);
		samplePatternButton.setText(AdminConstants.SAMPLE_REGEX_BUTTON);
		saveButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		cancelButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		samplePatternButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		validateButton.setTitle(AdminConstants.VALIDATE_BUTTON);
		validateButton.setStyleName(AdminConstants.VALIDATE_BUTTON_IMAGE);
		createValidatableWidgets();

		editBatchClassFieldViewPanel.setSpacing(BatchClassManagementConstants.FIVE);

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
		scrollPanel.setStyleName(AdminConstants.SCROLL_PANEL_HEIGHT);

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
	 * To do operations on sample pattern button click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("samplePatternButton")
	public void onSamplePatternButtonClicked(ClickEvent clickEvent) {
		presenter.getController().getMainPresenter().getSamplePatterns();
	}

	/**
	 * To do operations on validate pattern button click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("validateButton")
	public void onValidateButtonClicked(ClickEvent clickEvent) {
		presenter.onValidateButtonClicked();
	}

	/**
	 * To get name.
	 * 
	 * @return String
	 */
	public String getName() {
		return this.name.getValue();
	}

	/**
	 * To get name.
	 * 
	 * @param name String
	 */
	public void setName(String name) {
		this.name.setValue(name);
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
	 * To set Description.
	 * 
	 * @param description String
	 */
	public void setDescription(String description) {
		this.description.setValue(description);
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
		for (DataType dType : allDataTypes) {
			if (dType.name().equals(selected)) {
				tempDataType = dType;
				break;
			}
		}
		return tempDataType;
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
	 * To get Field Order Number.
	 * 
	 * @return String
	 */
	public String getFieldOrderNumber() {
		return this.fieldOrderNumber.getValue();
	}

	/**
	 * To set Field Order Number.
	 * 
	 * @param fieldOrderNumber String
	 */
	public void setFieldOrderNumber(String fieldOrderNumber) {
		this.fieldOrderNumber.setValue(fieldOrderNumber);
	}

	/**
	 * To get Sample Value.
	 * 
	 * @return String
	 */
	public String getSampleValue() {
		return this.sampleValue.getValue();
	}

	/**
	 * To set Sample Value.
	 * 
	 * @param sampleValue String
	 */
	public void setSampleValue(String sampleValue) {
		this.sampleValue.setValue(sampleValue);
	}

	/**
	 * To get Validation Pattern.
	 * 
	 * @return String
	 */
	public String getValidationPattern() {
		return this.validationPattern.getValue();
	}

	/**
	 * To set Validation Pattern.
	 * 
	 * @param validationPattern String
	 */
	public void setValidationPattern(String validationPattern) {
		this.validationPattern.setValue(validationPattern);
	}

	/**
	 * To get Field Option Value List.
	 * 
	 * @return String
	 */
	public String getFieldOptionValueList() {
		return this.fieldOptionValueList.getValue();
	}

	/**
	 * To set Field Option Value List.
	 * 
	 * @param fieldOptionValueList String
	 */
	public void setFieldOptionValueList(String fieldOptionValueList) {
		this.fieldOptionValueList.setValue(fieldOptionValueList);
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
	 * To get Validate Description TextBox.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateDescriptionTextBox() {
		return validateDescriptionTextBox;
	}

	/**
	 * To get Validate Field Order Number TextBox.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateFieldOrderNumberTextBox() {
		return validateFieldOrderNumberTextBox;
	}

	/**
	 * To get Validate Validation Pattern TextBox.
	 * 
	 * @return RegExValidatableWidget<TextBox>
	 */
	public RegExValidatableWidget<TextBox> getValidateValidationPatternTextBox() {
		return validateValidationPatternTextBox;
	}

	/**
	 * To get Name TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getNameTextBox() {
		return this.name;
	}

	/**
	 * To get Description TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getDescriptionTextBox() {
		return this.description;
	}

	/**
	 * To get Field Order Number TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getFieldOrderNumberTextBox() {
		return this.fieldOrderNumber;
	}

	/**
	 * To get Sample Value TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getSampleValueTextBox() {
		return this.sampleValue;
	}

	/**
	 * To get Validation Pattern TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getValidationPatternTextBox() {
		return this.validationPattern;
	}

	/**
	 * To get Field Option Value List TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getFieldOptionValueListTextBox() {
		return this.fieldOptionValueList;
	}

	/**
	 * To get Field Order Number Label.
	 * 
	 * @return Label
	 */
	public Label getFieldOrderNumberLabel() {
		return fieldOrderNumberLabel;
	}

	/**
	 * To create Validatable Widgets.
	 */
	public final void createValidatableWidgets() {
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

		validateValidationPatternTextBox = new RegExValidatableWidget<TextBox>(validationPattern, true);
		validateValidationPatternTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (validateValidationPatternTextBox.getWidget().getText().isEmpty()) {
					validateValidationPatternTextBox.setValid(true);
				} else {
					validateValidationPatternTextBox.setValid(false);
				}
			}
		});
	}

	/**
	 * To set Save Button Enable.
	 * 
	 * @param isEnable boolean
	 */
	public void setSaveButtonEnable(boolean isEnable) {
		saveButton.setEnabled(isEnable);
	}

}
