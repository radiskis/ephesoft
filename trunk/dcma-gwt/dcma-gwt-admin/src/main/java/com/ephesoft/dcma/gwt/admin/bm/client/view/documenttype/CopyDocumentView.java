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

package com.ephesoft.dcma.gwt.admin.bm.client.view.documenttype;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.documenttype.CopyDocumentTypePresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.validator.EmptyStringValidator;
import com.ephesoft.dcma.gwt.core.client.validator.NumberValidator;
import com.ephesoft.dcma.gwt.core.client.validator.ValidatableWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class provides functionality to copy document.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class CopyDocumentView extends View<CopyDocumentTypePresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, CopyDocumentView> {
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
	 * minConfidenceThresholdLabel Label.
	 */
	@UiField
	protected Label minConfidenceThresholdLabel;

	/**
	 * minConfidenceThresholdStar Label.
	 */
	@UiField
	protected Label minConfidenceThresholdStar;

	/**
	 * minConfidenceThreshold TextBox.
	 */
	@UiField
	protected TextBox minConfidenceThreshold;

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
	 * editDocumentPanel HorizontalPanel.
	 */
	@UiField
	protected HorizontalPanel editDocumentPanel;

	/**
	 * validateNameTextBox ValidatableWidget<TextBox>.
	 */
	private final ValidatableWidget<TextBox> validateNameTextBox;

	/**
	 * validateDescriptionTextBox ValidatableWidget<TextBox>.
	 */
	private final ValidatableWidget<TextBox> validateDescriptionTextBox;

	/**
	 * validatePriorityTextBox ValidatableWidget<TextBox>.
	 */
	private final ValidatableWidget<TextBox> validatePriorityTextBox;

	/**
	 * dialogBox DialogBox.
	 */
	private DialogBox dialogBox;

	/**
	 * editDocumentViewPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel editDocumentViewPanel;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Constructor.
	 */
	public CopyDocumentView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		saveButton.setText(AdminConstants.SAVE_BUTTON);
		cancelButton.setText(AdminConstants.CANCEL_BUTTON);
		saveButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		cancelButton.setHeight(AdminConstants.BUTTON_HEIGHT);

		validatePriorityTextBox = new ValidatableWidget<TextBox>(minConfidenceThreshold);
		validatePriorityTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validatePriorityTextBox.toggleValidDateBox();
			}
		});

		validatePriorityTextBox.addValidator(new NumberValidator(minConfidenceThreshold));

		validateDescriptionTextBox = new ValidatableWidget<TextBox>(description);
		validateDescriptionTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateDescriptionTextBox.toggleValidDateBox();
			}
		});
		validateDescriptionTextBox.addValidator(new EmptyStringValidator(description));

		validateNameTextBox = new ValidatableWidget<TextBox>(name);
		validateNameTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> arg0) {
				validateNameTextBox.toggleValidDateBox();

			}
		});
		validateNameTextBox.addValidator(new EmptyStringValidator(name));

		editDocumentViewPanel.setSpacing(BatchClassManagementConstants.FIVE);

		nameLabel.setText(AdminConstants.NAME);
		minConfidenceThresholdLabel.setText(AdminConstants.MIN_CONFIDENCE_THRESHOLD);
		descLabel.setText(AdminConstants.DESCRIPTION);

		minConfidenceThresholdStar.setText(AdminConstants.STAR);
		descStar.setText(AdminConstants.STAR);
		nameStar.setText(AdminConstants.STAR);

		nameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		minConfidenceThresholdLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		descLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);

		minConfidenceThresholdStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		descStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		nameStar.setStyleName(AdminConstants.FONT_RED_STYLE);

		editDocumentPanel.setSpacing(BatchClassManagementConstants.TEN);
	}

	/**
	 * To get Minimum Confidence Threshold.
	 * 
	 * @return String
	 */
	public String getMinConfidenceThreshold() {
		return minConfidenceThreshold.getValue();
	}

	/**
	 * To set Minimum Confidence Threshold.
	 * 
	 * @param priority String
	 */
	public void setMinConfidenceThreshold(String priority) {
		this.minConfidenceThreshold.setValue(priority);
	}

	/**
	 * To get Description.
	 * 
	 * @return String
	 */
	public String getDescription() {
		return description.getValue();
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
	 * To get Validate Priority TextBox.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidatePriorityTextBox() {
		return validatePriorityTextBox;
	}

	/**
	 * To get Minimum Confidence Threshold TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getMinConfidenceThresholdTextBox() {
		return minConfidenceThreshold;
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
	 * To get Save Button.
	 * 
	 * @return Button
	 */
	public Button getSaveButton() {
		return saveButton;
	}

	/**
	 * To get Cancel Button.
	 * 
	 * @return Button
	 */
	public Button getCancelButton() {
		return cancelButton;
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
	 * To get Name.
	 * 
	 * @return String
	 */
	public String getName() {
		return name.getValue();
	}

	/**
	 * To set name.
	 * 
	 * @param name String
	 */
	public void setName(String name) {
		this.name.setValue(name);
	}

	/**
	 * To get Dialog Box.
	 * 
	 * @return DialogBox
	 */
	public DialogBox getDialogBox() {
		return dialogBox;
	}

	/**
	 * To set Dialog Box.
	 * 
	 * @param dialogBox DialogBox
	 */
	public void setDialogBox(DialogBox dialogBox) {
		this.dialogBox = dialogBox;
	}

	/**
	 * To perform operations on OK click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("saveButton")
	public void onOkClick(ClickEvent clickEvent) {
		presenter.onOkClicked();
	}

	/**
	 * To perform operations on cancel click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("cancelButton")
	public void onCancelClick(ClickEvent clickEvent) {
		dialogBox.hide(true);
	}
}
