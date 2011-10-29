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

package com.ephesoft.dcma.gwt.admin.bm.client.view.fieldtype;

import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.fieldtype.FieldTypeDetailPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FieldTypeDetailView extends View<FieldTypeDetailPresenter> {

	
	public CheckBox getIsHidden() {
		return isHidden;
	}

	interface Binder extends UiBinder<VerticalPanel, FieldTypeDetailView> {
	}

	@UiField
	protected Label name;

	@UiField
	protected Label description;

	@UiField
	protected Label nameLabel;

	@UiField
	protected Label descriptionLabel;

	@UiField
	protected Label dataType;

	@UiField
	protected Label pattern;

	@UiField
	protected Label dataTypeLabel;

	@UiField
	protected Label patternLabel;

	@UiField
	protected Label fdOrderLabel;

	@UiField
	protected Label fdOrder;

	@UiField
	protected Label sampleValueLabel;

	@UiField
	protected Label sampleValue;

	@UiField
	protected Label fieldOptionValueListLabel;
	@UiField
	protected Label fieldOptionValueList;

	@UiField
	protected Label barcodeTypeLabel;

	@UiField
	protected Label barcodeType;

	@UiField
	protected Label isHiddenLabel;
	@UiField
	protected CheckBox isHidden;

	private static final Binder BINDER = GWT.create(Binder.class);

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
	}

	public Label getIsHiddenLabel() {
		return isHiddenLabel;
	}

	public void setName(String name) {
		this.name.setText(name);
	}

	public void setDescription(String description) {
		this.description.setText(description);
	}

	public void setPattern(String pattern) {
		this.pattern.setText(pattern);
	}

	public void setDataType(DataType dataType) {
		if (dataType != null) {
			this.dataType.setText(dataType.name());
		} else {
			this.dataType.setText(DataType.STRING.name());
		}
	}

	public void setFdOrder(String fdOrder) {
		this.fdOrder.setText(fdOrder);
	}

	public void setSampleValue(String sampleValue) {
		this.sampleValue.setText(sampleValue);
	}

	public void setBarcodeType(String barcodeType) {
		this.barcodeType.setText(barcodeType);
	}

	public void setFieldOptionValueList(String fieldValueList) {
		this.fieldOptionValueList.setText(fieldValueList);
	}

	public void setIsHidden(boolean isHidden) {
		this.isHidden.setValue(isHidden);
	}

}
