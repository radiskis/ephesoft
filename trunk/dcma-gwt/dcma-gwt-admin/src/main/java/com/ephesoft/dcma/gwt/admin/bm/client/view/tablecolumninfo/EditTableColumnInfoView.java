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

package com.ephesoft.dcma.gwt.admin.bm.client.view.tablecolumninfo;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.tablecolumninfo.EditTableColumnInfoPresenter;
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
public class EditTableColumnInfoView extends View<EditTableColumnInfoPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, EditTableColumnInfoView> {
	}

	/**
	 * betweenLeftLabel Label.
	 */
	@UiField
	protected Label betweenLeftLabel;

	/**
	 * betweenLeft TextBox.
	 */
	@UiField
	protected TextBox betweenLeft;

	/**
	 * betweenLeftPatternValidateButton Button.
	 */
	@UiField
	protected Button betweenLeftPatternValidateButton;

	/**
	 * betweenRightLabel Label.
	 */
	@UiField
	protected Label betweenRightLabel;

	/**
	 * betweenRight TextBox.
	 */
	@UiField
	protected TextBox betweenRight;

	/**
	 * betweenRightPatternValidateButton Button.
	 */
	@UiField
	protected Button betweenRightPatternValidateButton;

	/**
	 * columnNameLabel Label.
	 */
	@UiField
	protected Label columnNameLabel;

	/**
	 * columnNameStar Label.
	 */
	@UiField
	protected Label columnNameStar;

	/**
	 * columnName TextBox.
	 */
	@UiField
	protected TextBox columnName;

	/**
	 * columnHeaderPatternLabel Label.
	 */
	@UiField
	protected Label columnHeaderPatternLabel;

	/**
	 * columnHeaderPattern TextBox.
	 */
	@UiField
	protected TextBox columnHeaderPattern;

	/**
	 * columnHeaderPatternValidateButton Button.
	 */
	@UiField
	protected Button columnHeaderPatternValidateButton;

	/**
	 * columnPatternLabel Label.
	 */
	@UiField
	protected Label columnPatternLabel;

	/**
	 * columnPattern TextBox.
	 */
	@UiField
	protected TextBox columnPattern;

	/**
	 * columnPatternValidateButton Button.
	 */
	@UiField
	protected Button columnPatternValidateButton;

	/**
	 * isRequiredLabel Label.
	 */
	@UiField
	protected Label isRequiredLabel;

	/**
	 * required CheckBox.
	 */
	@UiField
	protected CheckBox required;

	/**
	 * isMandatoryLabel Label.
	 */
	@UiField
	protected Label isMandatoryLabel;

	/**
	 * mandatory CheckBox.
	 */
	@UiField
	protected CheckBox mandatory;

	/**
	 * columnStartCoordinateLabel Label.
	 */
	@UiField
	protected Label columnStartCoordinateLabel;

	/**
	 * columnStartCoordinate TextBox.
	 */
	@UiField
	protected TextBox columnStartCoordinate;

	/**
	 * columnEndCoordinateLabel Label.
	 */
	@UiField
	protected Label columnEndCoordinateLabel;

	/**
	 * columnEndCoordinate TextBox.
	 */
	@UiField
	protected TextBox columnEndCoordinate;

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
	 * validateColumnNameTextBox ValidatableWidget<TextBox>.
	 */
	private ValidatableWidget<TextBox> validateColumnNameTextBox;

	/**
	 * validateColumnPatternTextBox RegExValidatableWidget<TextBox>.
	 */
	private RegExValidatableWidget<TextBox> validateColumnPatternTextBox;

	/**
	 * validateBetweenLeftTextBox RegExValidatableWidget<TextBox>.
	 */
	private RegExValidatableWidget<TextBox> validateBetweenLeftTextBox;

	/**
	 * validateBetweenRightTextBox RegExValidatableWidget<TextBox>.
	 */
	private RegExValidatableWidget<TextBox> validateBetweenRightTextBox;

	/**
	 * validateColumnHeaderPatternTextBox RegExValidatableWidget<TextBox>.
	 */
	private final RegExValidatableWidget<TextBox> validateColumnHeaderPatternTextBox;

	/**
	 * validateColumnStartCoordTextBox ValidatableWidget<TextBox>.
	 */
	private final ValidatableWidget<TextBox> validateColumnStartCoordTextBox;

	/**
	 * validateColumnEndCoordTextBox ValidatableWidget<TextBox>.
	 */
	private final ValidatableWidget<TextBox> validateColumnEndCoordTextBox;

	/**
	 * editTableColumnInfoViewPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel editTableColumnInfoViewPanel;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Constructor.
	 */
	public EditTableColumnInfoView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		saveButton.setText(AdminConstants.OK_BUTTON);
		samplePatternButton.setText(AdminConstants.SAMPLE_REGEX_BUTTON);
		saveButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		cancelButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		samplePatternButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		betweenLeftPatternValidateButton.setTitle(AdminConstants.VALIDATE_BUTTON);
		betweenRightPatternValidateButton.setTitle(AdminConstants.VALIDATE_BUTTON);
		betweenLeftPatternValidateButton.setStyleName(AdminConstants.VALIDATE_BUTTON_IMAGE);
		betweenRightPatternValidateButton.setStyleName(AdminConstants.VALIDATE_BUTTON_IMAGE);
		columnHeaderPatternValidateButton.setTitle(AdminConstants.VALIDATE_BUTTON);
		scrollPanel.setStyleName(AdminConstants.SCROLL_PANEL_HEIGHT);
		columnPatternValidateButton.setTitle(AdminConstants.VALIDATE_BUTTON);
		columnHeaderPatternValidateButton.setStyleName(AdminConstants.VALIDATE_BUTTON_IMAGE);
		columnPatternValidateButton.setStyleName(AdminConstants.VALIDATE_BUTTON_IMAGE);
		cancelButton.setText(AdminConstants.CANCEL_BUTTON);
		scrollPanel.setStyleName(AdminConstants.SCROLL_PANEL_HEIGHT);
		validateColumnNameTextBox = new ValidatableWidget<TextBox>(columnName);
		validateColumnNameTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateColumnNameTextBox.toggleValidDateBox();
			}
		});

		validateColumnPatternTextBox = new RegExValidatableWidget<TextBox>(columnPattern);
		validateColumnPatternTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (validateColumnPatternTextBox.getWidget().getText().isEmpty()) {
					validateColumnPatternTextBox.setValid(true);
				} else {
					validateColumnPatternTextBox.setValid(false);
				}
			}
		});
		validateBetweenLeftTextBox = new RegExValidatableWidget<TextBox>(betweenLeft);
		validateBetweenLeftTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (validateBetweenLeftTextBox.getWidget().getText().isEmpty()) {
					validateBetweenLeftTextBox.setValid(true);
				} else {
					validateBetweenLeftTextBox.setValid(false);
				}
			}
		});
		validateBetweenRightTextBox = new RegExValidatableWidget<TextBox>(betweenRight);
		validateBetweenRightTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (validateBetweenRightTextBox.getWidget().getText().isEmpty()) {
					validateBetweenRightTextBox.setValid(true);
				} else {
					validateBetweenRightTextBox.setValid(false);
				}
			}
		});
		validateColumnHeaderPatternTextBox = new RegExValidatableWidget<TextBox>(columnHeaderPattern);
		validateColumnHeaderPatternTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (validateColumnHeaderPatternTextBox.getWidget().getText().isEmpty()) {
					validateColumnHeaderPatternTextBox.setValid(true);
				} else {
					validateColumnHeaderPatternTextBox.setValid(false);
				}
			}
		});

		validateColumnStartCoordTextBox = new ValidatableWidget<TextBox>(columnStartCoordinate);
		validateColumnStartCoordTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateColumnStartCoordTextBox.toggleValidDateBox();
			}
		});

		validateColumnEndCoordTextBox = new ValidatableWidget<TextBox>(columnEndCoordinate);
		validateColumnEndCoordTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateColumnEndCoordTextBox.toggleValidDateBox();
			}
		});

		editTableColumnInfoViewPanel.setSpacing(BatchClassManagementConstants.FIVE);

		betweenLeftLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.BETWEEN_LEFT).toString()
				+ AdminConstants.COLON);
		betweenRightLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.BETWEEN_RIGHT).toString()
				+ AdminConstants.COLON);
		columnNameLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.COLUMN_NAME).toString()
				+ AdminConstants.COLON);
		columnPatternLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.COLUMN_PATTERN).toString()
				+ AdminConstants.COLON);
		isRequiredLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.IS_REQUIRED).toString()
				+ AdminConstants.COLON);
		isMandatoryLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.IS_MANDATORY).toString()
				+ AdminConstants.COLON);
		columnHeaderPatternLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.COLUMN_HEADER_PATTERN)
				.toString()
				+ AdminConstants.COLON);
		columnStartCoordinateLabel.setText(LocaleDictionary.get().getConstantValue(
				BatchClassManagementConstants.COLUMN_START_COORDINATE_LABEL).toString()
				+ AdminConstants.COLON);
		columnEndCoordinateLabel.setText(LocaleDictionary.get().getConstantValue(
				BatchClassManagementConstants.COLUMN_END_COORDINATE_LABEL).toString()
				+ AdminConstants.COLON);

		columnNameStar.setText(AdminConstants.STAR);

		betweenLeftLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		betweenRightLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		columnNameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		columnPatternLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		columnHeaderPatternLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		isRequiredLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		isMandatoryLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		columnStartCoordinateLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		columnEndCoordinateLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		columnNameStar.setStyleName(AdminConstants.FONT_RED_STYLE);

	}

	/**
	 * To get Between Left label.
	 * 
	 * @return Label
	 */
	public Label getBetweenLeftLabel() {
		return betweenLeftLabel;
	}

	/**
	 * To set Between Left label.
	 * 
	 * @param betweenLeftLabel Label
	 */
	public void setBetweenLeftLabel(Label betweenLeftLabel) {
		this.betweenLeftLabel = betweenLeftLabel;
	}

	/**
	 * To get Between right label.
	 * 
	 * @return Label
	 */
	public Label getBetweenRightLabel() {
		return betweenRightLabel;
	}

	/**
	 * To set Between right label.
	 * 
	 * @param betweenRightLabel Label
	 */
	public void setBetweenRightLabel(Label betweenRightLabel) {
		this.betweenRightLabel = betweenRightLabel;
	}

	/**
	 * To set Column Pattern.
	 * 
	 * @param columnPattern TextBox
	 */
	public void setColumnPattern(TextBox columnPattern) {
		this.columnPattern = columnPattern;
	}

	/**
	 * To get Column Header Pattern Label.
	 * 
	 * @return Label
	 */
	public Label getColumnHeaderPatternLabel() {
		return columnHeaderPatternLabel;
	}

	/**
	 * To set Column Header Pattern Label.
	 * 
	 * @param columnHeaderPatternLabel Label
	 */
	public void setColumnHeaderPatternLabel(Label columnHeaderPatternLabel) {
		this.columnHeaderPatternLabel = columnHeaderPatternLabel;
	}

	/**
	 * To get Column Start Coordinate Text Box.
	 * 
	 * @return TextBox
	 */
	public TextBox getColumnStartCoordinateTextBox() {
		return columnStartCoordinate;
	}

	/**
	 * To get Column end Coordinate Text Box.
	 * 
	 * @return TextBox
	 */
	public TextBox getColumnEndCoordinateTextBox() {
		return columnEndCoordinate;
	}

	/**
	 * To get Validate Column Header Pattern Text Box.
	 * 
	 * @return RegExValidatableWidget<TextBox>
	 */
	public RegExValidatableWidget<TextBox> getValidateColumnHeaderPatternTextBox() {
		return validateColumnHeaderPatternTextBox;
	}

	/**
	 * To get Validate Column Start Coordinate Text Box.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateColumnStartCoordTextBox() {
		return validateColumnStartCoordTextBox;
	}

	/**
	 * To get Validate Column End Coordinate Text Box.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateColumnEndCoordTextBox() {
		return validateColumnEndCoordTextBox;
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
	 * To get controller on Sample Pattern Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("samplePatternButton")
	public void onSamplePatternButtonClicked(ClickEvent clickEvent) {
		presenter.getController().getMainPresenter().getSamplePatterns();
	}

	/**
	 * To perform operations on Between Left Pattern Validate Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("betweenLeftPatternValidateButton")
	public void onBetweenLeftPatternValidateButtonClicked(ClickEvent clickEvent) {
		presenter.onBetweenLeftPatternValidateButtonClicked();
	}

	/**
	 * To perform operations on Between right Pattern Validate Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("betweenRightPatternValidateButton")
	public void onBetweenRightPatternValidateButtonClicked(ClickEvent clickEvent) {
		presenter.onBetweenRightPatternValidateButtonClicked();
	}

	/**
	 * To perform operations on Column Header Pattern Validate Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("columnHeaderPatternValidateButton")
	public void onColumnHeaderPatternValidateButtonClicked(ClickEvent clickEvent) {
		presenter.onColumnHeaderPatternValidateButtonClicked();
	}

	/**
	 * To perform operations on Column Pattern Validate Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("columnPatternValidateButton")
	public void onColumnPatternValidateButtonClicked(ClickEvent clickEvent) {
		presenter.onColumnPatternValidateButtonClicked();
	}

	/**
	 * To perform operations on Cancel Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("cancelButton")
	public void onCancelClicked(ClickEvent clickEvent) {
		presenter.onCancel();
	}

	/**
	 * To set Column Name.
	 * 
	 * @param columnName String
	 */
	public void setColumnName(String columnName) {
		this.columnName.setValue(columnName);
	}

	/**
	 * To set Column Name.
	 * 
	 * @return String
	 */
	public String getColumnName() {
		return this.columnName.getValue();
	}

	/**
	 * To set Between Left.
	 * 
	 * @param betweenLeft String
	 */
	public void setBetweenLeft(String betweenLeft) {
		this.betweenLeft.setValue(betweenLeft);
	}

	/**
	 * To get Between Left.
	 * 
	 * @return String
	 */
	public String getBetweenLeft() {
		return this.betweenLeft.getValue();
	}

	/**
	 * To set Between right.
	 * 
	 * @param betweenRight String
	 */
	public void setBetweenRight(String betweenRight) {
		this.betweenRight.setValue(betweenRight);
	}

	/**
	 * To get Between right.
	 * 
	 * @return String
	 */
	public String getBetweenRight() {
		return this.betweenRight.getValue();
	}

	/**
	 * To set Column Pattern.
	 * 
	 * @param columnPattern String
	 */
	public void setColumnPattern(String columnPattern) {
		this.columnPattern.setValue(columnPattern);
	}

	/**
	 * To get column Pattern.
	 * 
	 * @return String
	 */
	public String getColumnPattern() {
		return this.columnPattern.getValue();
	}

	/**
	 * To set Required field.
	 * 
	 * @param isRequired boolean
	 */
	public void setRequired(boolean isRequired) {
		this.required.setValue(isRequired);
	}

	/**
	 * To get value of required field.
	 * 
	 * @return boolean
	 */
	public boolean isRequired() {
		return this.required.getValue();
	}

	/**
	 * To set Mandatory.
	 * 
	 * @param isMandatory boolean
	 */
	public void setMandatory(boolean isMandatory) {
		this.mandatory.setValue(isMandatory);
	}

	/**
	 * To get value of mandatory field.
	 * 
	 * @return boolean
	 */
	public boolean isMandatory() {
		return this.mandatory.getValue();
	}

	/**
	 * To get Column Header Pattern.
	 * 
	 * @return String
	 */
	public String getColumnHeaderPattern() {
		return this.columnHeaderPattern.getValue();
	}

	/**
	 * To set Column Header Pattern.
	 * 
	 * @param columnHeaderPattern String
	 */
	public void setColumnHeaderPattern(String columnHeaderPattern) {
		this.columnHeaderPattern.setValue(columnHeaderPattern);
	}

	/**
	 * To get Column Start Coordinate.
	 * 
	 * @return String
	 */
	public String getColumnStartCoordinate() {
		return columnStartCoordinate.getValue();
	}

	/**
	 * To set Column Start Coordinate.
	 * 
	 * @param columnStartCoordinate String
	 */
	public void setColumnStartCoordinate(String columnStartCoordinate) {
		this.columnStartCoordinate.setValue(columnStartCoordinate);
	}

	/**
	 * To get Column End Coordinate.
	 * 
	 * @return String
	 */
	public String getColumnEndCoordinate() {
		return columnEndCoordinate.getValue();
	}

	/**
	 * To set Column End Coordinate.
	 * 
	 * @param columnEndCoordinate String
	 */
	public void setColumnEndCoordinate(String columnEndCoordinate) {
		this.columnEndCoordinate.setValue(columnEndCoordinate);
	}

	/**
	 * To get Validate Column Name Text Box.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateColumnNameTextBox() {
		return validateColumnNameTextBox;
	}

	/**
	 * To set Validate Column Name Text Box.
	 * 
	 * @param validateColumnNameTextBox ValidatableWidget<TextBox>
	 */
	public void setValidateColumnNameTextBox(ValidatableWidget<TextBox> validateColumnNameTextBox) {
		this.validateColumnNameTextBox = validateColumnNameTextBox;
	}

	/**
	 * To get Validate Column Pattern text Box.
	 * 
	 * @return RegExValidatableWidget<TextBox>
	 */
	public RegExValidatableWidget<TextBox> getValidateColumnPatternTextBox() {
		return validateColumnPatternTextBox;
	}

	/**
	 * To set Validate Column Pattern Text Box.
	 * 
	 * @param validateColumnPatternTextBox RegExValidatableWidget<TextBox>
	 */
	public void setValidateColumnPatternTextBox(RegExValidatableWidget<TextBox> validateColumnPatternTextBox) {
		this.validateColumnPatternTextBox = validateColumnPatternTextBox;
	}

	/**
	 * To get Between Left Text Box.
	 * 
	 * @return TextBox
	 */
	public TextBox getBetweenLeftTextBox() {
		return betweenLeft;
	}

	/**
	 * To get Between right Text Box.
	 * 
	 * @return TextBox
	 */
	public TextBox getBetweenRightTextBox() {
		return betweenRight;
	}

	/**
	 * To get Column Name Text Box.
	 * 
	 * @return TextBox
	 */
	public TextBox getColumnNameTextBox() {
		return columnName;
	}

	/**
	 * To get Column Pattern Text Box.
	 * 
	 * @return TextBox
	 */
	public TextBox getColumnPatternTextBox() {
		return columnPattern;
	}

	/**
	 * To get Column Pattern Label.
	 * 
	 * @return Label
	 */
	public Label getColumnPatternLabel() {
		return columnPatternLabel;
	}

	/**
	 * To set Column Pattern Label.
	 * 
	 * @param columnPatternLabel Label
	 */
	public void setColumnPatternLabel(Label columnPatternLabel) {
		this.columnPatternLabel = columnPatternLabel;
	}

	/**
	 * To get Column Header Pattern Text Box.
	 * 
	 * @return TextBox
	 */
	public TextBox getColumnHeaderPatternTextBox() {
		return columnHeaderPattern;
	}

	/**
	 * To get Validate Between Left Text Box.
	 * 
	 * @return RegExValidatableWidget<TextBox>
	 */
	public RegExValidatableWidget<TextBox> getValidateBetweenLeftTextBox() {
		return validateBetweenLeftTextBox;
	}

	/**
	 * To set Validate Between Left Text Box.
	 * 
	 * @param validateBetweenLeftTextBox RegExValidatableWidget<TextBox>
	 */
	public void setValidateBetweenLeftTextBox(RegExValidatableWidget<TextBox> validateBetweenLeftTextBox) {
		this.validateBetweenLeftTextBox = validateBetweenLeftTextBox;
	}

	/**
	 * To get Validate Between Right Text Box.
	 * 
	 * @return RegExValidatableWidget<TextBox>
	 */
	public RegExValidatableWidget<TextBox> getValidateBetweenRightTextBox() {
		return validateBetweenRightTextBox;
	}

	/**
	 * To set Validate Between Right Text Box.
	 * 
	 * @param validateBetweenRightTextBox RegExValidatableWidget<TextBox>
	 */
	public void setValidateBetweenRightTextBox(RegExValidatableWidget<TextBox> validateBetweenRightTextBox) {
		this.validateBetweenRightTextBox = validateBetweenRightTextBox;
	}

	/**
	 * To get Column Start Coordinates Label.
	 * 
	 * @return String
	 */
	public String getColumnStartCoordinatesLabel() {
		return columnStartCoordinateLabel.getText();
	}

	/**
	 * To get Column End Coordinates Label.
	 * 
	 * @return String
	 */
	public String getColumnEndCoordinatesLabel() {
		return columnEndCoordinateLabel.getText();
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
