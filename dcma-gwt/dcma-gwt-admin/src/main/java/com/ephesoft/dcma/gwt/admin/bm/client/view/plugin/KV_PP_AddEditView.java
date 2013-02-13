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

package com.ephesoft.dcma.gwt.admin.bm.client.view.plugin;

import java.util.Arrays;
import java.util.List;

import com.ephesoft.dcma.core.common.LocationType;
import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin.KV_PP_AddEditPresenter;
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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class provides functionality to edit KV PP.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class KV_PP_AddEditView extends View<KV_PP_AddEditPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, KV_PP_AddEditView> {
	}

	/**
	 * keyPatternLabel Label.
	 */
	@UiField
	protected Label keyPatternLabel;

	/**
	 * To get Key Pattern Label.
	 * 
	 * @return Label
	 */
	public Label getKeyPatternLabel() {
		return keyPatternLabel;
	}

	/**
	 * To set Key Pattern Label.
	 * 
	 * @param keyPatternLabel Label
	 */
	public void setKeyPatternLabel(Label keyPatternLabel) {
		this.keyPatternLabel = keyPatternLabel;
	}

	/**
	 * To get Value Pattern Label.
	 * 
	 * @return Label
	 */
	public Label getValuePatternLabel() {
		return valuePatternLabel;
	}

	/**
	 * To set Value Pattern Label.
	 * 
	 * @param valuePatternLabel Label
	 */
	public void setValuePatternLabel(Label valuePatternLabel) {
		this.valuePatternLabel = valuePatternLabel;
	}

	/**
	 * keyPatternStar Label.
	 */
	@UiField
	protected Label keyPatternStar;

	/**
	 * keyPattern TextBox.
	 */
	@UiField
	protected TextBox keyPattern;

	/**
	 * keyPatternValidateButton Button.
	 */
	@UiField
	protected Button keyPatternValidateButton;

	/**
	 * valuePatternLabel Label.
	 */
	@UiField
	protected Label valuePatternLabel;

	/**
	 * valuePatternStar Label.
	 */
	@UiField
	protected Label valuePatternStar;

	/**
	 * valuePattern TextBox.
	 */
	@UiField
	protected TextBox valuePattern;

	/**
	 * valuePatternValidateButton Button.
	 */
	@UiField
	protected Button valuePatternValidateButton;

	/**
	 * locationLabel Label.
	 */
	@UiField
	protected Label locationLabel;

	/**
	 * locationStar Label.
	 */
	@UiField
	protected Label locationStar;

	/**
	 * location ListBox.
	 */
	@UiField
	protected ListBox location;

	/**
	 * noOfWordsLabel Label.
	 */
	@UiField
	protected Label noOfWordsLabel;

	/**
	 * noOfWordsStar Label.
	 */
	@UiField
	protected Label noOfWordsStar;

	/**
	 * noOfWords TextBox.
	 */
	@UiField
	protected TextBox noOfWords;

	/**
	 * pageLevelFieldNameLabel Label.
	 */
	@UiField
	protected Label pageLevelFieldNameLabel;

	/**
	 * pageLevelFieldNameStar Label.
	 */
	@UiField
	protected Label pageLevelFieldNameStar;

	/**
	 * pageLevelFieldName TextBox.
	 */
	@UiField
	protected TextBox pageLevelFieldName;

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
	 * validateKeyPatternTextBox RegExValidatableWidget<TextBox>.
	 */
	private RegExValidatableWidget<TextBox> validateKeyPatternTextBox;

	/**
	 * validateValuePatternTextBox RegExValidatableWidget<TextBox>.
	 */
	private RegExValidatableWidget<TextBox> validateValuePatternTextBox;

	/**
	 * validateNoOfWordsTextBox ValidatableWidget<TextBox>.
	 */
	private ValidatableWidget<TextBox> validateNoOfWordsTextBox;

	/**
	 * validatePageLevelFieldNameLabelTextBox ValidatableWidget<TextBox>.
	 */
	private ValidatableWidget<TextBox> validatePageLevelFieldNameLabelTextBox;

	/**
	 * editKVPPTypeViewPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel editKVPPTypeViewPanel;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Constructor.
	 */
	public KV_PP_AddEditView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		saveButton.setText(AdminConstants.OK_BUTTON);
		cancelButton.setText(AdminConstants.CANCEL_BUTTON);
		samplePatternButton.setText(AdminConstants.SAMPLE_REGEX_BUTTON);
		keyPatternValidateButton.setTitle(AdminConstants.VALIDATE_BUTTON);
		valuePatternValidateButton.setTitle(AdminConstants.VALIDATE_BUTTON);
		keyPatternValidateButton.setStyleName(AdminConstants.VALIDATE_BUTTON_IMAGE);
		valuePatternValidateButton.setStyleName(AdminConstants.VALIDATE_BUTTON_IMAGE);
		validateKeyPatternTextBox = new RegExValidatableWidget<TextBox>(keyPattern);
		validateKeyPatternTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateKeyPatternTextBox.setValid(false);
			}
		});

		validateValuePatternTextBox = new RegExValidatableWidget<TextBox>(valuePattern);
		validateValuePatternTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateValuePatternTextBox.setValid(false);
			}
		});

		validateNoOfWordsTextBox = new ValidatableWidget<TextBox>(noOfWords);
		validateNoOfWordsTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateNoOfWordsTextBox.toggleValidDateBox();
			}
		});

		validatePageLevelFieldNameLabelTextBox = new ValidatableWidget<TextBox>(pageLevelFieldName);
		validatePageLevelFieldNameLabelTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validatePageLevelFieldNameLabelTextBox.toggleValidDateBox();
			}
		});

		editKVPPTypeViewPanel.setSpacing(BatchClassManagementConstants.FIVE);

		keyPatternLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.KEY_PATTERN)
				+ AdminConstants.COLON);
		valuePatternLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.VALUE_PATTERN)
				+ AdminConstants.COLON);
		locationLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.LOCATION) + AdminConstants.COLON);
		noOfWordsLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.NO_OF_WORDS)
				+ AdminConstants.COLON);
		pageLevelFieldNameLabel.setText(LocaleDictionary.get()
				.getConstantValue(BatchClassManagementConstants.KV_PAGE_LEVEL_FIELD_NAME)
				+ AdminConstants.COLON);

		keyPatternStar.setText(AdminConstants.STAR);
		valuePatternStar.setText(AdminConstants.STAR);
		locationStar.setText(AdminConstants.STAR);
		noOfWordsStar.setText(AdminConstants.STAR);
		pageLevelFieldNameStar.setText(AdminConstants.STAR);

		keyPatternLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		valuePatternLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		locationLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		noOfWordsLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		pageLevelFieldNameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		keyPatternStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		valuePatternStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		locationStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		noOfWordsStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		pageLevelFieldNameStar.setStyleName(AdminConstants.FONT_RED_STYLE);
	}

	/**
	 * To get No. of Words Label.
	 * 
	 * @return the noOfWordsLabel
	 */
	public Label getNoOfWordsLabel() {
		return noOfWordsLabel;
	}

	/**
	 * To perform operations on save click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("saveButton")
	public void onSaveClicked(ClickEvent clickEvent) {
		presenter.onSave();
	}

	/**
	 * To perform operations on cancel click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("cancelButton")
	public void onCancelClicked(ClickEvent clickEvent) {
		presenter.onCancel();
	}

	/**
	 * To get controller on Sample Pattern Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("samplePatternButton")
	public void onSamplePatternButtonClicked(ClickEvent clickEvent) {
		presenter.getController().getMainPresenter().getSamplePatterns();
	}

	/**
	 * To get controller on key Pattern validate Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("keyPatternValidateButton")
	public void onKeyPatternValidateButtonClicked(ClickEvent clickEvent) {
		presenter.onKeyPatternValidateButtonClicked();
	}

	/**
	 * To perform operations on Value Pattern Validate Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("valuePatternValidateButton")
	public void onValuePatternValidateButtonClicked(ClickEvent clickEvent) {
		presenter.onValuePatternValidateButtonClicked();
	}

	/**
	 * To set Key Pattern.
	 * 
	 * @param keyPattern String
	 */
	public void setKeyPattern(String keyPattern) {
		this.keyPattern.setValue(keyPattern);
	}

	/**
	 * To get Key Pattern.
	 * 
	 * @return String
	 */
	public String getKeyPattern() {
		return this.keyPattern.getValue();
	}

	/**
	 * To set Value Pattern.
	 * 
	 * @param description String
	 */
	public void setValuePattern(String description) {
		this.valuePattern.setValue(description);
	}

	/**
	 * To get Value Pattern.
	 * 
	 * @return String
	 */
	public String getValuePattern() {
		return this.valuePattern.getValue();
	}

	/**
	 * To get No. of Words.
	 * 
	 * @return String
	 */
	public String getNoOfWords() {
		return this.noOfWords.getValue();
	}

	/**
	 * To set No. of Words.
	 * 
	 * @param noOfWords String
	 */
	public void setNoOfWords(String noOfWords) {
		this.noOfWords.setValue(noOfWords);
	}

	/**
	 * To get Location.
	 * 
	 * @return LocationType
	 */
	public LocationType getLocation() {
		String selected = this.location.getItemText(this.location.getSelectedIndex());
		LocationType[] allLocationTypes = LocationType.values();
		LocationType locationType = allLocationTypes[0];
		for (LocationType locType : allLocationTypes) {
			if (locType.name().equals(selected)) {
				locationType = locType;
				break;
			}
		}
		return locationType;
	}

	/**
	 * To get Default Location.
	 * 
	 * @return LocationType
	 */
	public LocationType getDefaultLocation() {
		return LocationType.TOP;
	}

	private int findIndex(LocationType locationType) {
		int index = 0;
		if (locationType != null) {
			LocationType[] allLocationTypes = LocationType.values();
			List<LocationType> tempList = Arrays.asList(allLocationTypes);
			index = tempList.indexOf(locationType);
		}
		return index;
	}

	/**
	 * To set Location.
	 */
	public void setLocation() {
		this.location.setVisibleItemCount(1);
		LocationType[] allLocationTypes = LocationType.values();
		for (LocationType locationType2 : allLocationTypes) {
			this.location.addItem(locationType2.name());
		}
	}

	/**
	 * To set Location.
	 * 
	 * @param locationType LocationType
	 */
	public void setLocation(LocationType locationType) {
		if (this.location.getItemCount() == 0) {
			setLocation();
		}
		this.location.setSelectedIndex(findIndex(locationType));
	}

	/**
	 * To get Validate Key Pattern Text Box.
	 * 
	 * @return RegExValidatableWidget<TextBox>
	 */
	public RegExValidatableWidget<TextBox> getValidateKeyPatternTextBox() {
		return validateKeyPatternTextBox;
	}

	/**
	 * To set Validate Value Pattern Text Box.
	 * 
	 * @param validateValuePatternTextBox RegExValidatableWidget<TextBox>
	 */
	public void setValidateValuePatternTextBox(RegExValidatableWidget<TextBox> validateValuePatternTextBox) {
		this.validateValuePatternTextBox = validateValuePatternTextBox;
	}

	/**
	 * To get Validate Value Pattern Text Box.
	 * 
	 * @return RegExValidatableWidget<TextBox>
	 */
	public RegExValidatableWidget<TextBox> getValidateValuePatternTextBox() {
		return validateValuePatternTextBox;
	}

	/**
	 * To set Validate No. of Words Text Box.
	 * 
	 * @param validateNoOfWordsTextBox ValidatableWidget<TextBox>
	 */
	public void setValidateNoOfWordsTextBox(ValidatableWidget<TextBox> validateNoOfWordsTextBox) {
		this.validateNoOfWordsTextBox = validateNoOfWordsTextBox;
	}

	/**
	 * To get Validate No. of Words Text Box.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateNoOfWordsTextBox() {
		return validateNoOfWordsTextBox;
	}

	/**
	 * To set Validate Key Pattern Text Box.
	 * 
	 * @param validateKeyPatternTextBox RegExValidatableWidget<TextBox>
	 */
	public void setValidateKeyPatternTextBox(RegExValidatableWidget<TextBox> validateKeyPatternTextBox) {
		this.validateKeyPatternTextBox = validateKeyPatternTextBox;
	}

	/**
	 * To get Key Pattern Text Box.
	 * 
	 * @return TextBox
	 */
	public TextBox getKeyPatternTextBox() {
		return keyPattern;
	}

	/**
	 * To get Value Pattern Text Box.
	 * 
	 * @return TextBox
	 */
	public TextBox getValuePatternTextBox() {
		return valuePattern;
	}

	/**
	 * To get No. of Words Text Box.
	 * 
	 * @return TextBox
	 */
	public TextBox getNoOfWordsTextBox() {
		return noOfWords;
	}

	/**
	 * To get Validate Page Level Field Name Label Text Box.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidatePageLevelFieldNameLabelTextBox() {
		return validatePageLevelFieldNameLabelTextBox;
	}

	/**
	 * To set Validate Page Level Field Name Label Text Box.
	 * 
	 * @param validatePageLevelFieldNameLabelTextBox ValidatableWidget<TextBox>
	 */
	public void setValidatePageLevelFieldNameLabelTextBox(ValidatableWidget<TextBox> validatePageLevelFieldNameLabelTextBox) {
		this.validatePageLevelFieldNameLabelTextBox = validatePageLevelFieldNameLabelTextBox;
	}

	/**
	 * To get Description Label.
	 * 
	 * @return Label
	 */
	public Label getDescriptionLabel() {
		return pageLevelFieldNameLabel;
	}

	/**
	 * To get Page Level Field Name.
	 * 
	 * @return String
	 */
	public String getPageLevelFieldName() {
		return this.pageLevelFieldName.getValue();
	}

	/**
	 * To set Page Level Field Name.
	 * 
	 * @param description String
	 */
	public void setPageLevelFieldName(String description) {
		this.pageLevelFieldName.setValue(description);
	}

	/**
	 * To get Page Level Field Name Text Box.
	 * 
	 * @return TextBox
	 */
	public TextBox getPageLevelFieldNameTextBox() {
		return pageLevelFieldName;
	}

	/**
	 * To set save button enable.
	 * 
	 * @param isEnable boolean
	 */
	public void setSaveButtonEnable(boolean isEnable) {
		saveButton.setEnabled(isEnable);

	}

}
