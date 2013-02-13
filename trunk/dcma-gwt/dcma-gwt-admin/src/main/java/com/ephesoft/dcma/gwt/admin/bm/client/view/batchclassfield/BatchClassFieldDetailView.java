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

import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.batchclassfield.BatchClassFieldDetailPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class provides functionality to show individual batch class field detail.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class BatchClassFieldDetailView extends View<BatchClassFieldDetailPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, BatchClassFieldDetailView> {
	}

	/**
	 * nameLabel Label.
	 */
	@UiField
	protected Label nameLabel;

	/**
	 * name Label.
	 */
	@UiField
	protected Label name;

	/**
	 * descriptionLabel Label.
	 */
	@UiField
	protected Label descriptionLabel;

	/**
	 * description Label.
	 */
	@UiField
	protected Label description;

	/**
	 * dataTypeLabel Label.
	 */
	@UiField
	protected Label dataTypeLabel;

	/**
	 * dataType Label.
	 */
	@UiField
	protected Label dataType;

	/**
	 * fieldOrderNumberLabel Label.
	 */
	@UiField
	protected Label fieldOrderNumberLabel;

	/**
	 * fieldOrderNumber Label.
	 */
	@UiField
	protected Label fieldOrderNumber;

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
	 * validationPatternLabel Label.
	 */
	@UiField
	protected Label validationPatternLabel;

	/**
	 * validationPattern Label.
	 */
	@UiField
	protected Label validationPattern;

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
	 * scrollPanel ScrollPanel.
	 */
	@UiField
	protected ScrollPanel scrollPanel;

	/**
	 * editBatchClassFieldPropertiesButton Button.
	 */
	@UiField
	protected Button editBatchClassFieldPropertiesButton;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Constructor.
	 */
	public BatchClassFieldDetailView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
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

		scrollPanel.setStyleName(AdminConstants.SCROLL_PANEL_HEIGHT);
		nameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		descriptionLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		dataTypeLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		fieldOrderNumberLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		sampleValueLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		validationPatternLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		fieldOptionValueListLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		editBatchClassFieldPropertiesButton.setText(AdminConstants.EDIT_BUTTON);
		editBatchClassFieldPropertiesButton.setHeight(AdminConstants.BUTTON_HEIGHT);
	}

	/**
	 * To set Data Type.
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
	 * To set name.
	 * 
	 * @param name String
	 */
	public void setName(String name) {
		this.name.setText(name);
	}

	/**
	 * To set description.
	 * 
	 * @param description String
	 */
	public void setDescription(String description) {
		this.description.setText(description);
	}

	/**
	 * To set Field Order Number.
	 * 
	 * @param fieldOrderNumber String
	 */
	public void setFieldOrderNumber(String fieldOrderNumber) {
		this.fieldOrderNumber.setText(fieldOrderNumber);
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
	 * To set Validation Pattern.
	 * 
	 * @param validationPattern String
	 */
	public void setValidationPattern(String validationPattern) {
		this.validationPattern.setText(validationPattern);
	}

	/**
	 * To set Field Option Value List.
	 * 
	 * @param fieldOptionValueList String
	 */
	public void setFieldOptionValueList(String fieldOptionValueList) {
		this.fieldOptionValueList.setText(fieldOptionValueList);
	}

	/**
	 * To get Edit Batch Class Field Properties Button.
	 * 
	 * @return Button
	 */
	public Button getEditBatchClassFieldPropertiesButton() {
		return editBatchClassFieldPropertiesButton;
	}

	/**
	 * To perform operations on Edit Document Properties Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("editBatchClassFieldPropertiesButton")
	public void onEditDocumentPropertiesButtonClick(ClickEvent clickEvent) {
		presenter.getController().getBatchClassManagementPresenter().getBatchClassFieldViewPresenter()
				.onEditBatchClassFieldPropertiesButtonClicked();
	}

}
