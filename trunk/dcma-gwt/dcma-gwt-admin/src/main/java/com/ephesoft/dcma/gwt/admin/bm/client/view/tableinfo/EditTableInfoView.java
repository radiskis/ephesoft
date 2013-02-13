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

package com.ephesoft.dcma.gwt.admin.bm.client.view.tableinfo;

import com.ephesoft.dcma.core.common.TableExtractionTechnique;
import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.tableinfo.EditTableInfoPresenter;
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
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class provides functionality to edit table column info view.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class EditTableInfoView extends View<EditTableInfoPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, EditTableInfoView> {
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
	 * startPatternLabel Label.
	 */
	@UiField
	protected Label startPatternLabel;

	/**
	 * startPatternStar Label.
	 */
	@UiField
	protected Label startPatternStar;

	/**
	 * startPattern TextBox.
	 */
	@UiField
	protected TextBox startPattern;

	/**
	 * startPatternValidateButton Button.
	 */
	@UiField
	protected Button startPatternValidateButton;

	/**
	 * endPatternLabel Label.
	 */
	@UiField
	protected Label endPatternLabel;

	/**
	 * endPatternStar Label.
	 */
	@UiField
	protected Label endPatternStar;

	/**
	 * endPattern TextBox.
	 */
	@UiField
	protected TextBox endPattern;

	/**
	 * endPatternValidateButton Button.
	 */
	@UiField
	protected Button endPatternValidateButton;

	/**
	 * tableExtractionAPI Label.
	 */
	@UiField
	protected Label tableExtractionAPI;

	/**
	 * tableExtractionStar Label.
	 */
	@UiField
	protected Label tableExtractionStar;

	/**
	 * colCoordValidationCheckBox CheckBox.
	 */
	@UiField
	protected CheckBox colCoordValidationCheckBox;

	/**
	 * andOrListBox1 ListBox.
	 */
	@UiField
	protected ListBox andOrListBox1;

	/**
	 * colHeaderValidationCheckBox CheckBox.
	 */
	@UiField
	protected CheckBox colHeaderValidationCheckBox;

	/**
	 * andOrListBox2 ListBox.
	 */
	@UiField
	protected ListBox andOrListBox2;

	/**
	 * regexValidationCheckBox CheckBox.
	 */
	@UiField
	protected CheckBox regexValidationCheckBox;

	/**
	 * widthOfMultiLineLabel Label.
	 */
	@UiField
	protected Label widthOfMultiLineLabel;

	/**
	 * widthOfMultiLine TextBox.
	 */
	@UiField
	protected TextBox widthOfMultiLine;

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
	 * scrollPanel ScrollPanel.
	 */
	@UiField
	protected ScrollPanel scrollPanel;

	/**
	 * validateNameTextBox ValidatableWidget<TextBox>.
	 */
	private ValidatableWidget<TextBox> validateNameTextBox;

	/**
	 * validateStartPatternTextBox RegExValidatableWidget<TextBox>.
	 */
	private RegExValidatableWidget<TextBox> validateStartPatternTextBox;

	/**
	 * validateEndPatternTextBox RegExValidatableWidget<TextBox>.
	 */
	private RegExValidatableWidget<TextBox> validateEndPatternTextBox;

	/**
	 * validateWidthOfMultiLineTextBox validateWidthOfMultiLineTextBox.
	 */
	private final ValidatableWidget<TextBox> validateWidthOfMultiLineTextBox;

	/**
	 * editTableInfoViewPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel editTableInfoViewPanel;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Constructor.
	 */
	public EditTableInfoView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		saveButton.setText(AdminConstants.OK_BUTTON);
		cancelButton.setText(AdminConstants.CANCEL_BUTTON);
		samplePatternButton.setText(AdminConstants.SAMPLE_REGEX_BUTTON);
		saveButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		cancelButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		samplePatternButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		startPatternValidateButton.setTitle(AdminConstants.VALIDATE_BUTTON);
		endPatternValidateButton.setTitle(AdminConstants.VALIDATE_BUTTON);
		startPatternValidateButton.setStyleName(AdminConstants.VALIDATE_BUTTON_IMAGE);
		endPatternValidateButton.setStyleName(AdminConstants.VALIDATE_BUTTON_IMAGE);
		regexValidationCheckBox.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.REGEX_VALIDATION));
		colHeaderValidationCheckBox.setText(LocaleDictionary.get().getConstantValue(
				BatchClassManagementConstants.COL_HEADER_VALIDATION));
		colCoordValidationCheckBox.setText(LocaleDictionary.get().getConstantValue(
				BatchClassManagementConstants.COL_COORDINATES_VALIDATION));

		andOrListBox1.addItem(AdminConstants.OR_LEBEL);
		andOrListBox1.addItem(AdminConstants.AND_LABEL);
		andOrListBox1.setEnabled(false);

		andOrListBox2.addItem(AdminConstants.OR_LEBEL);
		andOrListBox2.addItem(AdminConstants.AND_LABEL);
		andOrListBox2.setEnabled(false);
		scrollPanel.setStyleName(AdminConstants.SCROLL_PANEL_HEIGHT);

		colCoordValidationCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					andOrListBox1.setEnabled(true);
				} else {
					andOrListBox1.setEnabled(false);
				}
			}
		});

		colHeaderValidationCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					andOrListBox2.setEnabled(true);
				} else {
					andOrListBox2.setEnabled(false);
				}
			}
		});

		validateNameTextBox = new ValidatableWidget<TextBox>(name);
		validateNameTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateNameTextBox.toggleValidDateBox();
			}
		});

		validateStartPatternTextBox = new RegExValidatableWidget<TextBox>(startPattern);
		validateStartPatternTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateStartPatternTextBox.setValid(false);
			}
		});

		validateEndPatternTextBox = new RegExValidatableWidget<TextBox>(endPattern);
		validateEndPatternTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateEndPatternTextBox.setValid(false);
			}
		});

		validateWidthOfMultiLineTextBox = new ValidatableWidget<TextBox>(widthOfMultiLine);
		validateWidthOfMultiLineTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateWidthOfMultiLineTextBox.toggleValidDateBox();
			}
		});

		editTableInfoViewPanel.setSpacing(BatchClassManagementConstants.FIVE);

		nameLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.NAME)
				+ BatchClassManagementConstants.COLON);
		startPatternLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.START_PATTERN)
				+ BatchClassManagementConstants.COLON);
		endPatternLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.END_PATTERN)
				+ BatchClassManagementConstants.COLON);
		tableExtractionAPI.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.TABLE_EXTRACTION_TECHNIQUE)
				+ BatchClassManagementConstants.COLON);
		widthOfMultiLineLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.WIDTH_OF_MULTILINE)
				+ BatchClassManagementConstants.COLON);

		nameStar.setText(AdminConstants.STAR);
		startPatternStar.setText(AdminConstants.STAR);
		endPatternStar.setText(AdminConstants.STAR);
		tableExtractionStar.setText(AdminConstants.STAR);

		nameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		startPatternLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		endPatternLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		tableExtractionAPI.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		widthOfMultiLineLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);

		nameStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		startPatternStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		endPatternStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		tableExtractionStar.setStyleName(AdminConstants.FONT_RED_STYLE);
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
	 * To perform operations on start pattern validate Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("startPatternValidateButton")
	public void onStartPatternValidateButtonClicked(ClickEvent clickEvent) {
		presenter.onStartPatternValidateButtonClicked();
	}

	/**
	 * To perform operations on end pattern validate Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("endPatternValidateButton")
	public void onEndPatternValidateButtonClicked(ClickEvent clickEvent) {
		presenter.onEndPatternValidateButtonClicked();
	}

	/**
	 * To get Validate Name Text Box.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateNameTextBox() {
		return validateNameTextBox;
	}

	/**
	 * To set Validate Name Text Box.
	 * 
	 * @param validateNameTextBox ValidatableWidget<TextBox>
	 */
	public void setValidateNameTextBox(ValidatableWidget<TextBox> validateNameTextBox) {
		this.validateNameTextBox = validateNameTextBox;
	}

	/**
	 * To get Validate Start Pattern Text Box.
	 * 
	 * @return RegExValidatableWidget<TextBox>
	 */
	public RegExValidatableWidget<TextBox> getValidateStartPatternTextBox() {
		return validateStartPatternTextBox;
	}

	/**
	 * To set Validate Start Pattern Text Box.
	 * 
	 * @param validateStartPatternTextBox RegExValidatableWidget<TextBox>
	 */
	public void setValidateStartPatternTextBox(RegExValidatableWidget<TextBox> validateStartPatternTextBox) {
		this.validateStartPatternTextBox = validateStartPatternTextBox;
	}

	/**
	 * To get Validate End Pattern Text Box.
	 * 
	 * @return RegExValidatableWidget<TextBox>
	 */
	public RegExValidatableWidget<TextBox> getValidateEndPatternTextBox() {
		return validateEndPatternTextBox;
	}

	/**
	 * To set Validate End Pattern Text Box.
	 * 
	 * @param validateEndPatternTextBox RegExValidatableWidget<TextBox>
	 */
	public void setValidateEndPatternTextBox(RegExValidatableWidget<TextBox> validateEndPatternTextBox) {
		this.validateEndPatternTextBox = validateEndPatternTextBox;
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
	 * To get name.
	 * 
	 * @return String
	 */
	public String getName() {
		return this.name.getValue();
	}

	/**
	 * To get Name Text Box.
	 * 
	 * @return TextBox
	 */
	public TextBox getNameTextBox() {
		return name;
	}

	/**
	 * To set Start Pattern.
	 * 
	 * @param startPattern String
	 */
	public void setStartPattern(String startPattern) {
		this.startPattern.setValue(startPattern);
	}

	/**
	 * To get Start Pattern.
	 * 
	 * @return String
	 */
	public String getStartPattern() {
		return this.startPattern.getValue();
	}

	/**
	 * To get Start Pattern Text Box.
	 * 
	 * @return TextBox
	 */
	public TextBox getStartPatternTextBox() {
		return startPattern;
	}

	/**
	 * To set End Pattern.
	 * 
	 * @param endPattern String
	 */
	public void setEndPattern(String endPattern) {
		this.endPattern.setValue(endPattern);
	}

	/**
	 * To get End Pattern.
	 * 
	 * @return String
	 */
	public String getEndPattern() {
		return this.endPattern.getValue();
	}

	/**
	 * To get End Pattern Text Box.
	 * 
	 * @return TextBox
	 */
	public TextBox getEndPatternTextBox() {
		return endPattern;
	}

	/**
	 * To get Table Extraction API.
	 * 
	 * @return String
	 */
	public String getTableExtractionAPI() {
		StringBuffer extractionAPI = new StringBuffer();
		boolean regexValidationRequired = regexValidationCheckBox.getValue();
		boolean colHeaderValidationRequired = colHeaderValidationCheckBox.getValue();
		boolean colCoodValidationRequired = colCoordValidationCheckBox.getValue();
		if (colCoodValidationRequired) {
			extractionAPI.append(TableExtractionTechnique.COLUMN_COORDINATES_VALIDATION);
		}
		if (colCoodValidationRequired && (colHeaderValidationRequired || regexValidationRequired)) {
			if (this.andOrListBox1.getItemText(this.andOrListBox1.getSelectedIndex()).equalsIgnoreCase(AdminConstants.AND_LABEL)) {
				extractionAPI.append(AdminConstants.SPACE);
				extractionAPI.append(AdminConstants.LOGICAL_AND_OPERATOR);
				extractionAPI.append(AdminConstants.SPACE);
			} else {
				extractionAPI.append(AdminConstants.SPACE);
				extractionAPI.append(AdminConstants.LOGICAL_OR_OPERATOR);
				extractionAPI.append(AdminConstants.SPACE);
			}
		}
		if (colHeaderValidationRequired) {
			extractionAPI.append(TableExtractionTechnique.COLUMN_HEADER_VALIDATION);
		}
		if (colHeaderValidationRequired && regexValidationRequired) {
			if (this.andOrListBox2.getItemText(this.andOrListBox2.getSelectedIndex()).equalsIgnoreCase(AdminConstants.AND_LABEL)) {
				extractionAPI.append(AdminConstants.SPACE);
				extractionAPI.append(AdminConstants.LOGICAL_AND_OPERATOR);
				extractionAPI.append(AdminConstants.SPACE);
			} else {
				extractionAPI.append(AdminConstants.SPACE);
				extractionAPI.append(AdminConstants.LOGICAL_OR_OPERATOR);
				extractionAPI.append(AdminConstants.SPACE);
			}
		}
		if (regexValidationRequired) {
			extractionAPI.append(TableExtractionTechnique.REGEX_VALIDATION);
		}
		return extractionAPI.toString();
	}

	/**
	 * To set Table Extraction API.
	 * 
	 * @param tableExtractionAPI String
	 */
	public void setTableExtractionAPI(final String tableExtractionAPI) {
		if (tableExtractionAPI != null) {
			String localTableExtractionAPI = tableExtractionAPI.toUpperCase();
			if (localTableExtractionAPI.contains(TableExtractionTechnique.COLUMN_COORDINATES_VALIDATION.name())) {
				localTableExtractionAPI = localTableExtractionAPI.replace(
						TableExtractionTechnique.COLUMN_COORDINATES_VALIDATION.name(), AdminConstants.EMPTY_STRING).trim();
				this.colCoordValidationCheckBox.setValue(true);
				ValueChangeEvent.fire(this.colCoordValidationCheckBox, true);
			} else {
				this.colCoordValidationCheckBox.setValue(false);
				ValueChangeEvent.fire(this.colCoordValidationCheckBox, false);
			}
			if (localTableExtractionAPI.contains(TableExtractionTechnique.COLUMN_HEADER_VALIDATION.name())) {
				localTableExtractionAPI = localTableExtractionAPI.replace(TableExtractionTechnique.COLUMN_HEADER_VALIDATION.name(),
						AdminConstants.EMPTY_STRING).trim();
				this.colHeaderValidationCheckBox.setValue(true);
				ValueChangeEvent.fire(this.colHeaderValidationCheckBox, true);
			} else {
				this.colHeaderValidationCheckBox.setValue(false);
				ValueChangeEvent.fire(this.colHeaderValidationCheckBox, false);
			}
			if (localTableExtractionAPI.contains(TableExtractionTechnique.REGEX_VALIDATION.name())) {
				localTableExtractionAPI = localTableExtractionAPI.replace(TableExtractionTechnique.REGEX_VALIDATION.name(),
						AdminConstants.EMPTY_STRING).trim();
				this.regexValidationCheckBox.setValue(true);
			} else {
				this.regexValidationCheckBox.setValue(false);
			}
			if (!localTableExtractionAPI.isEmpty()) {
				if (localTableExtractionAPI.startsWith(AdminConstants.LOGICAL_AND_OPERATOR)) {
					andOrListBox1.setSelectedIndex(1);
				} else {
					andOrListBox1.setSelectedIndex(0);
				}
				if (localTableExtractionAPI.endsWith(AdminConstants.LOGICAL_AND_OPERATOR)) {
					andOrListBox2.setSelectedIndex(1);
				} else {
					andOrListBox2.setSelectedIndex(0);
				}
			}
		} else {
			this.colCoordValidationCheckBox.setValue(false);
			ValueChangeEvent.fire(this.colCoordValidationCheckBox, false);
			this.colHeaderValidationCheckBox.setValue(false);
			ValueChangeEvent.fire(this.colHeaderValidationCheckBox, false);
			this.regexValidationCheckBox.setValue(false);
		}
	}

	/**
	 * To set Width Of MultiLine.
	 * 
	 * @param widthOfMultiLine String
	 */
	public void setWidthOfMultiLine(String widthOfMultiLine) {
		this.widthOfMultiLine.setValue(widthOfMultiLine);
	}

	/**
	 * To get Width Of MultiLine.
	 * 
	 * @return String
	 */
	public String getWidthOfMultiLine() {
		return this.widthOfMultiLine.getValue();
	}

	/**
	 * To get Width Of MultiLine Text Box.
	 * 
	 * @return TextBox
	 */
	public TextBox getWidthOfMultiLineTextBox() {
		return widthOfMultiLine;
	}

	/**
	 * To get Save Button.
	 * 
	 * @return Button
	 */
	public Button getSaveButton() {
		return saveButton;
	}

	/**
	 * To get Start Pattern Label.
	 * 
	 * @return Label
	 */
	public Label getStartPatternLabel() {
		return startPatternLabel;
	}

	/**
	 * To set Start Pattern Label.
	 * 
	 * @param startPatternLabel Label
	 */
	public void setStartPatternLabel(Label startPatternLabel) {
		this.startPatternLabel = startPatternLabel;
	}

	/**
	 * To get End Pattern Label.
	 * 
	 * @return Label
	 */
	public Label getEndPatternLabel() {
		return endPatternLabel;
	}

	/**
	 * To set End Pattern Label.
	 * 
	 * @param endPatternLabel Label
	 */
	public void setEndPatternLabel(Label endPatternLabel) {
		this.endPatternLabel = endPatternLabel;
	}

	/**
	 * To get Validate Width Of MultiLine Text Box.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateWidthOfMultiLineTextBox() {
		return validateWidthOfMultiLineTextBox;
	}

	/**
	 * To get Width Of MultiLine Label.
	 * 
	 * @return Label
	 */
	public Label getWidthOfMultiLineLabel() {
		return widthOfMultiLineLabel;
	}

}
