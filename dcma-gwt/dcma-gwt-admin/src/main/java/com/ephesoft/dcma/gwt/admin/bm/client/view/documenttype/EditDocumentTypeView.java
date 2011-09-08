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

import java.util.List;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.documenttype.EditDocumentTypePresenter;
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

public class EditDocumentTypeView extends View<EditDocumentTypePresenter> {

	interface Binder extends UiBinder<VerticalPanel, EditDocumentTypeView> {
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
	Label confLabel;
	@UiField
	Label confStar;
	@UiField
	TextBox confidence;

	@UiField
	Label recostarExtractionLabel;
	@UiField
	ListBox recostarExtraction;

	@UiField
	Button saveButton;
	@UiField
	Button cancelButton;

	private ValidatableWidget<TextBox> validateDescriptionTextBox;
	private ValidatableWidget<TextBox> validateConfidenceTextBox;
	private ValidatableWidget<TextBox> validateNameTextBox;

	@UiField
	VerticalPanel editDocumentTypeViewPanel;

	private static final Binder binder = GWT.create(Binder.class);

	public EditDocumentTypeView() {
		initWidget(binder.createAndBindUi(this));
		saveButton.setText(AdminConstants.OK_BUTTON);
		cancelButton.setText(AdminConstants.CANCEL_BUTTON);

		validateDescriptionTextBox = new ValidatableWidget<TextBox>(description);
		validateDescriptionTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateDescriptionTextBox.toggleValidDateBox();
			}
		});

		validateConfidenceTextBox = new ValidatableWidget<TextBox>(confidence);
		validateConfidenceTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateConfidenceTextBox.toggleValidDateBox();
			}
		});

		validateNameTextBox = new ValidatableWidget<TextBox>(name);
		validateNameTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateNameTextBox.toggleValidDateBox();
			}
		});

		editDocumentTypeViewPanel.setSpacing(5);

		nameLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.NAME) + AdminConstants.COLON);
		descLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.DESCRIPTION) + AdminConstants.COLON);
		confLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.MINIMUM_CONFIDENCE_THRESHOLD)
				+ AdminConstants.COLON);
		recostarExtractionLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.RECOSTAR_PROJECT_FILE)
				+ AdminConstants.COLON);

		nameStar.setText(AdminConstants.STAR);
		descStar.setText(AdminConstants.STAR);
		confStar.setText(AdminConstants.STAR);

		nameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		descLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		confLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		recostarExtractionLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);

		nameStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		descStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		confStar.setStyleName(AdminConstants.FONT_RED_STYLE);

		recostarExtraction.setWidth("125px");
	}

	@UiHandler("saveButton")
	void onSaveClicked(ClickEvent clickEvent) {
		presenter.onSave();
	}

	@UiHandler("cancelButton")
	void onCancelClicked(ClickEvent clickEvent) {
		presenter.onCancel();
	}

	public void setName(String name) {
		this.name.setText(name);
	}

	public String getName() {
		return this.name.getText();
	}

	public void setDescription(String description) {
		this.description.setValue(description);
	}

	public String getDescription() {
		return this.description.getValue();
	}

	public String getConfidence() {
		return this.confidence.getValue();
	}

	public void setConfidence(String confidence) {
		this.confidence.setValue(confidence);
	}

	public void addConfidence(String minThreshConfidence) {
		this.confidence.setValue(minThreshConfidence);
	}

	public ValidatableWidget<TextBox> getValidateDescriptionTextBox() {
		return validateDescriptionTextBox;
	}

	public void setValidateDescriptionTextBox(ValidatableWidget<TextBox> validateDescriptionTextBox) {
		this.validateDescriptionTextBox = validateDescriptionTextBox;
	}

	public ValidatableWidget<TextBox> getValidateConfidenceTextBox() {
		return validateConfidenceTextBox;
	}

	public void setValidateConfidenceTextBox(ValidatableWidget<TextBox> validateConfidenceTextBox) {
		this.validateConfidenceTextBox = validateConfidenceTextBox;
	}

	public ValidatableWidget<TextBox> getValidateNameTextBox() {
		return validateNameTextBox;
	}

	public void setValidateNameTextBox(ValidatableWidget<TextBox> validateNameTextBox) {
		this.validateNameTextBox = validateNameTextBox;
	}

	public TextBox getConfidenceTextBox() {
		return confidence;
	}

	public TextBox getDescriptionTextBox() {
		return description;
	}

	public String getConfLabel() {
		return confLabel.getText();
	}

	public TextBox getNameTextBox() {
		return name;
	}

	public String getRecostarExtraction() {

		int index = this.recostarExtraction.getSelectedIndex();
		String selected = null;
		if (index > 0)
			selected = this.recostarExtraction.getItemText(index);

		return selected;
	}

	public void setRecostarExtraction(String rspFileName, List<String> rspFileNameList) {
		this.recostarExtraction.setVisibleItemCount(1);

		this.recostarExtraction.clear();
		if (null == rspFileNameList || rspFileNameList.isEmpty()) {
			this.recostarExtraction.setEnabled(false);
			return;
		}
		this.recostarExtraction.addItem(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.SELECT_OPTION));
		for (String name : rspFileNameList) {
			this.recostarExtraction.addItem(name);
		}

		if (this.recostarExtraction.getItemCount() == 0) {
			this.recostarExtraction.setEnabled(false);
		} else {
			this.recostarExtraction.setEnabled(true);
			if (null != rspFileName && !rspFileName.isEmpty()) {
				int index = 1;
				for (String s : rspFileNameList) {
					if (s.equals(rspFileName)) {
						this.recostarExtraction.setSelectedIndex(index);
					}
					index++;
				}
			}
		}
	}

}
