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

package com.ephesoft.dcma.gwt.admin.bm.client.view.kvextraction;

import java.util.Arrays;
import java.util.List;

import com.ephesoft.dcma.core.common.LocationType;
import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.kvextraction.EditKVExtractionPresenter;
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

public class EditKVExtractionView extends View<EditKVExtractionPresenter> {

	interface Binder extends UiBinder<VerticalPanel, EditKVExtractionView> {
	}

	@UiField
	Label keyPatternLabel;
	@UiField
	Label keyPatternStar;
	@UiField
	TextBox keyPattern;

	@UiField
	Label valuePatternLabel;
	@UiField
	Label valuePatternStar;
	@UiField
	TextBox valuePattern;

	@UiField
	Label locationLabel;
	@UiField
	Label locationStar;
	@UiField
	ListBox location;

	@UiField
	Label noOfWordsLabel;
	@UiField
	TextBox noOfWords;
	@UiField
	Label noOFWordsStar;

	@UiField
	Button saveButton;
	@UiField
	Button cancelButton;

	private ValidatableWidget<TextBox> validateKeyPatternTextBox;
	private ValidatableWidget<TextBox> validateValuePatternTextBox;
	private ValidatableWidget<TextBox> validateNoOfWordsTextBox;

	@UiField
	VerticalPanel editKVTypeViewPanel;

	private static final Binder binder = GWT.create(Binder.class);

	public EditKVExtractionView() {
		initWidget(binder.createAndBindUi(this));
		saveButton.setText(AdminConstants.OK_BUTTON);
		cancelButton.setText(AdminConstants.CANCEL_BUTTON);
	
		validateKeyPatternTextBox = new ValidatableWidget<TextBox>(keyPattern);
		validateKeyPatternTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateKeyPatternTextBox.toggleValidDateBox();
			}
		});

		validateValuePatternTextBox = new ValidatableWidget<TextBox>(valuePattern);
		validateValuePatternTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateValuePatternTextBox.toggleValidDateBox();
			}
		});

		validateNoOfWordsTextBox = new ValidatableWidget<TextBox>(noOfWords);
		validateNoOfWordsTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateNoOfWordsTextBox.toggleValidDateBox();
			}
		});

		editKVTypeViewPanel.setSpacing(5);

		keyPatternLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.KEY_PATTERN)
				+ AdminConstants.COLON);
		valuePatternLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.VALUE_PATTERN)
				+ AdminConstants.COLON);
		locationLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.LOCATION) + AdminConstants.COLON);
		noOfWordsLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.NO_OF_WORDS)
				+ AdminConstants.COLON);
		keyPatternStar.setText(AdminConstants.STAR);
		valuePatternStar.setText(AdminConstants.STAR);
		locationStar.setText(AdminConstants.STAR);
		noOFWordsStar.setText(AdminConstants.STAR);

		keyPatternLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		valuePatternLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		locationLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		noOfWordsLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		keyPatternStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		valuePatternStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		locationStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		noOFWordsStar.setStyleName(AdminConstants.FONT_RED_STYLE);

	}

	@UiHandler("saveButton")
	void onSaveClicked(ClickEvent clickEvent) {
		presenter.onSave();
	}

	@UiHandler("cancelButton")
	void onCancelClicked(ClickEvent clickEvent) {
		presenter.onCancel();
	}

	public void setKeyPattern(String keyPattern) {
		this.keyPattern.setValue(keyPattern);
	}

	public String getKeyPattern() {
		return this.keyPattern.getValue();
	}

	public void setValuePattern(String description) {
		this.valuePattern.setValue(description);
	}

	public void setNoOfWords(String noOfWords) {
		this.noOfWords.setValue(noOfWords);
	}

	public String getValuePattern() {
		return this.valuePattern.getValue();
	}

	public String getNoOfWords() {
		return this.noOfWords.getValue();
	}

	public LocationType getLocation() {
		String selected = this.location.getItemText(this.location.getSelectedIndex());
		LocationType[] allLocationTypes = LocationType.values();
		for (LocationType locationType : allLocationTypes) {
			if (locationType.name().equals(selected))
				return locationType;
		}
		return allLocationTypes[0];
	}

	private int findIndex(LocationType locationType) {
		if (locationType == null)
			return 0;
		LocationType[] allLocationTypes = LocationType.values();
		List<LocationType> tempList = Arrays.asList(allLocationTypes);
		return tempList.indexOf(locationType);
	}

	public void setLocation() {
		this.location.setVisibleItemCount(1);
		LocationType[] allLocationTypes = LocationType.values();
		for (LocationType locationType2 : allLocationTypes) {
			this.location.addItem(locationType2.name());
		}
	}

	public void setLocation(LocationType locationType) {
		if (this.location.getItemCount() == 0)
			setLocation();
		this.location.setSelectedIndex(findIndex(locationType));
	}

	public ValidatableWidget<TextBox> getValidateKeyPatternTextBox() {
		return validateKeyPatternTextBox;
	}

	public void setValidateValuePatternTextBox(ValidatableWidget<TextBox> validateValuePatternTextBox) {
		this.validateValuePatternTextBox = validateValuePatternTextBox;
	}

	public void setValidateNoOfWordsTextBox(ValidatableWidget<TextBox> validateNoOfWordsTextBox) {
		this.validateNoOfWordsTextBox = validateNoOfWordsTextBox;
	}

	public ValidatableWidget<TextBox> getValidateValuePatternTextBox() {
		return validateValuePatternTextBox;
	}

	public ValidatableWidget<TextBox> getValidateNoOfWordsTextBox() {
		return validateNoOfWordsTextBox;
	}

	public void setValidateKeyPatternTextBox(ValidatableWidget<TextBox> validateKeyPatternTextBox) {
		this.validateKeyPatternTextBox = validateKeyPatternTextBox;
	}

	public TextBox getKeyPatternTextBox() {
		return keyPattern;
	}

	public TextBox getValuePatternTextBox() {
		return valuePattern;
	}

	public TextBox getNoOfWordsTextBox() {
		return noOfWords;
	}

	public Label getNoOfWordsLabel() {
		return noOfWordsLabel;
	}
}
