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

import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.fieldtype.FieldTypeDetailPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class provides functionality to show field type detail.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class FieldTypeDetailView extends View<FieldTypeDetailPresenter> {

	/**
	 * To get value of is hidden field.
	 * 
	 * @return CheckBox
	 */
	public CheckBox getIsHidden() {
		return isHidden;
	}

	/**
	 * To get value of is multiline field.
	 * 
	 * @return CheckBox
	 */
	public CheckBox getIsMultiLine() {
		return isMultiLine;
	}

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, FieldTypeDetailView> {
	}

	/**
	 * name Label.
	 */
	@UiField
	protected Label name;

	/**
	 * description Label.
	 */
	@UiField
	protected Label description;

	/**
	 * nameLabel Label.
	 */
	@UiField
	protected Label nameLabel;

	/**
	 * descriptionLabel Label.
	 */
	@UiField
	protected Label descriptionLabel;

	/**
	 * dataType Label.
	 */
	@UiField
	protected Label dataType;

	/**
	 * pattern Label.
	 */
	@UiField
	protected Label pattern;

	/**
	 * dataTypeLabel Label.
	 */
	@UiField
	protected Label dataTypeLabel;

	/**
	 * patternLabel Label.
	 */
	@UiField
	protected Label patternLabel;

	/**
	 * fdOrderLabel Label.
	 */
	@UiField
	protected Label fdOrderLabel;

	/**
	 * fdOrder Label.
	 */
	@UiField
	protected Label fdOrder;

	/**
	 * sampleValueLabel Label.
	 */
	@UiField
	protected Label sampleValueLabel;

	/**
	 * sampleValue Label.
	 */
	@UiField
	protected Label sampleValue;

	/**
	 * fieldOptionValueListLabel Label.
	 */
	@UiField
	protected Label fieldOptionValueListLabel;

	/**
	 * fieldOptionValueList Label.
	 */
	@UiField
	protected Label fieldOptionValueList;

	/**
	 * barcodeTypeLabel Label.
	 */
	@UiField
	protected Label barcodeTypeLabel;

	/**
	 * barcodeType Label.
	 */
	@UiField
	protected Label barcodeType;

	/**
	 * isHiddenLabel Label.
	 */
	@UiField
	protected Label isHiddenLabel;

	/**
	 * isHidden CheckBox.
	 */
	@UiField
	protected CheckBox isHidden;

	/**
	 * isMultiLineLabel Label.
	 */
	@UiField
	protected Label isMultiLineLabel;

	/**
	 * isMultiLine CheckBox.
	 */
	@UiField
	protected CheckBox isMultiLine;

	/**
	 * Label for 'readonly' flag for field types.
	 */
	@UiField
	protected Label isReadOnlyLabel;

	/**
	 * Checkbox for 'readonly' flag for field types.
	 */
	@UiField
	protected CheckBox isReadOnly;

	/**
	 * editFieldPropertiesButton Button.
	 */
	@UiField
	protected Button editFieldPropertiesButton;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Constructor.
	 */
	public FieldTypeDetailView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		nameLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.NAME) + AdminConstants.COLON);
		descriptionLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.DESCRIPTION)
				+ AdminConstants.COLON);
		patternLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.PATTERN) + AdminConstants.COLON);
		dataTypeLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.DATA_TYPE) + AdminConstants.COLON);
		fdOrderLabel
				.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.FIELD_ORDER) + AdminConstants.COLON);
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
		nameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		descriptionLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		dataTypeLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		patternLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		fdOrderLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		sampleValueLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		fieldOptionValueListLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		barcodeTypeLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		isHiddenLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		isHidden.setEnabled(false);
		isMultiLineLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		isReadOnlyLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		isMultiLine.setEnabled(false);
		isReadOnly.setEnabled(false);
		editFieldPropertiesButton.setText(AdminConstants.EDIT_BUTTON);
		editFieldPropertiesButton.setHeight(AdminConstants.BUTTON_HEIGHT);
	}

	/**
	 * To perform operations on Edit Field Properties Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("editFieldPropertiesButton")
	public void onEditFieldPropertiesButtonClick(ClickEvent clickEvent) {
		presenter.getController().getBatchClassManagementPresenter().getFieldTypeViewPresenter().onEditFieldPropertiesButtonClicked();
	}

	/**
	 * To get Is Hidden Label.
	 * 
	 * @return Label
	 */
	public Label getIsHiddenLabel() {
		return isHiddenLabel;
	}

	/**
	 * To get Is MultiLine Label.
	 * 
	 * @return Label
	 */
	public Label getIsMultiLineLabel() {
		return isMultiLineLabel;
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
		this.description.setText(description);
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
	 * To set data type.
	 * 
	 * @param dataType DataType
	 */
	public void setDataType(DataType dataType) {
		if (dataType != null) {
			this.dataType.setText(dataType.name());
		} else {
			this.dataType.setText(DataType.STRING.name());
		}
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
	 * To set Sample Value.
	 * 
	 * @param sampleValue String
	 */
	public void setSampleValue(String sampleValue) {
		this.sampleValue.setText(sampleValue);
	}

	/**
	 * To set Barcode Type.
	 * 
	 * @param barcodeType String
	 */
	public void setBarcodeType(String barcodeType) {
		this.barcodeType.setText(barcodeType);
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
	 * To set is hidden field.
	 * 
	 * @param isHidden boolean
	 */
	public void setIsHidden(boolean isHidden) {
		this.isHidden.setValue(isHidden);
	}

	/**
	 * To set is multiline field.
	 * 
	 * @param isMultiLine boolean
	 */
	public void setIsMultiLine(boolean isMultiLine) {
		this.isMultiLine.setValue(isMultiLine);
	}

	/**
	 * To get Edit Field Properties Button.
	 * 
	 * @return Button
	 */
	public Button getEditFieldPropertiesButtonButton() {
		return editFieldPropertiesButton;
	}

	/**
	 * API to get the checkbox object for 'readonly' flag.
	 * 
	 * @return isReadOnly{@link CheckBox}
	 */
	public CheckBox getIsReadOnly() {
		return isReadOnly;
	}

	/**
	 * API to set the value for readonly flag for fields types.
	 * 
	 * @param isReadOnly boolean
	 */
	public void setIsReadOnly(boolean isReadOnly) {
		this.isReadOnly.setValue(isReadOnly);
	}
}
