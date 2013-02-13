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

package com.ephesoft.dcma.gwt.admin.bm.client.view.functionkey;

import java.util.ArrayList;
import java.util.List;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.functionkey.EditFunctionKeyPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.validator.ValidatableWidget;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class provides functionality to edit function key type.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class EditFunctionKeyView extends View<EditFunctionKeyPresenter> {

	/**
	 * KEY_CODE_BACKSPACE int.
	 */
	private static final int KEY_CODE_BACKSPACE = 8;

	/**
	 * KEY_CODE_ENTER int.
	 */
	private static final int KEY_CODE_ENTER = 13;

	/**
	 * KEY_CODE_TAB int.
	 */
	private static final int KEY_CODE_TAB = 9;

	/**
	 * KEY_CODE_F5 int.
	 */
	private static final int KEY_CODE_F5 = 116;

	/**
	 * KEY_CODE_F11 int.
	 */
	private static final int KEY_CODE_F11 = 122;

	/**
	 * KEY_CODE_F10 int.
	 */
	private static final int KEY_CODE_F10 = 121;

	/**
	 * KEY_CODE_F9 int.
	 */
	private static final int KEY_CODE_F9 = 120;

	/**
	 * KEY_CODE_F8 int.
	 */
	private static final int KEY_CODE_F8 = 119;

	/**
	 * KEY_CODE_F7 int.
	 */
	private static final int KEY_CODE_F7 = 118;

	/**
	 * KEY_CODE_F6 int.
	 */
	private static final int KEY_CODE_F6 = 117;

	/**
	 * KEY_CODE_F4 int.
	 */
	private static final int KEY_CODE_F4 = 115;

	/**
	 * KEY_CODE_F3 int.
	 */
	private static final int KEY_CODE_F3 = 114;

	/**
	 * KEY_CODE_F2 int.
	 */
	private static final int KEY_CODE_F2 = 113;

	/**
	 * KEY_CODE_F1 int.
	 */
	private static final int KEY_CODE_F1 = 112;

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, EditFunctionKeyView> {
	}

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * methodNameLabel Label.
	 */
	@UiField
	protected Label methodNameLabel;

	/**
	 * methodNameStar Label.
	 */
	@UiField
	protected Label methodNameStar;

	/**
	 * methodName TextBox.
	 */
	@UiField
	protected TextBox methodName;

	/**
	 * keyNameLabel Label.
	 */
	@UiField
	protected Label keyNameLabel;

	/**
	 * keyNameStar Label.
	 */
	@UiField
	protected Label keyNameStar;

	/**
	 * keyName TextBox.
	 */
	@UiField
	protected TextBox keyName;

	/**
	 * keyWariningLabel Label.
	 */
	@UiField
	protected Label keyWariningLabel;

	/**
	 * methodDescriptionLabel Label.
	 */
	@UiField
	protected Label methodDescriptionLabel;

	/**
	 * methodDescription TextBox.
	 */
	@UiField
	protected TextBox methodDescription;

	/**
	 * methodDescriptionStar Label.
	 */
	@UiField
	protected Label methodDescriptionStar;

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
	 * scrollPanel ScrollPanel.
	 */
	@UiField
	protected ScrollPanel scrollPanel;

	/**
	 * editFunctionKeyViewPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel editFunctionKeyViewPanel;

	/**
	 * validateMethodNameTextBox ValidatableWidget<TextBox>.
	 */
	private ValidatableWidget<TextBox> validateMethodNameTextBox;

	/**
	 * validateMethodDescriptionTextBox ValidatableWidget<TextBox>.
	 */
	private ValidatableWidget<TextBox> validateMethodDescriptionTextBox;

	/**
	 * allowedKeys List<String>.
	 */
	private final List<String> allowedKeys = new ArrayList<String>();

	/**
	 * Constructor.
	 */
	public EditFunctionKeyView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		validateMethodNameTextBox = new ValidatableWidget<TextBox>(methodName);
		validateMethodNameTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateMethodNameTextBox.toggleValidDateBox();
			}
		});
		validateMethodDescriptionTextBox = new ValidatableWidget<TextBox>(methodDescription);
		validateMethodDescriptionTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateMethodDescriptionTextBox.toggleValidDateBox();
			}
		});
		saveButton.setText(AdminConstants.OK_BUTTON);
		cancelButton.setText(AdminConstants.CANCEL_BUTTON);
		saveButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		cancelButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		editFunctionKeyViewPanel.setSpacing(BatchClassManagementConstants.FIVE);
		methodNameLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.FUNCTION_KEY_METHOD_NAME)
				+ AdminConstants.COLON);
		methodDescriptionLabel.setText(LocaleDictionary.get().getConstantValue(
				BatchClassManagementConstants.FUNCTION_KEY_METHOD_DESCRIPTION)
				+ AdminConstants.COLON);
		keyNameLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.FUNCTION_KEY_KEY_NAME)
				+ AdminConstants.COLON);
		keyWariningLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.FUNCTION_KEY_KEY_WARNING));
		methodNameStar.setText(AdminConstants.STAR);
		keyNameStar.setText(AdminConstants.STAR);
		methodDescriptionStar.setText(AdminConstants.STAR);
		methodNameStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		keyNameStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		methodDescriptionStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		methodNameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		keyNameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		keyWariningLabel.setStyleName(AdminConstants.FONT_RED_STYLE);
		methodDescriptionLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		scrollPanel.setStyleName(AdminConstants.SCROLL_PANEL_HEIGHT);
		setAllowedKeys();
		keyName.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent arg0) {
				switch (arg0.getNativeKeyCode()) {
					case KEY_CODE_F1:
					case KEY_CODE_F2:
					case KEY_CODE_F3:
					case KEY_CODE_F4:
					case KEY_CODE_F5:
					case KEY_CODE_F6:
					case KEY_CODE_F7:
					case KEY_CODE_F8:
					case KEY_CODE_F9:
					case KEY_CODE_F10:
					case KEY_CODE_F11:
						arg0.getNativeEvent().preventDefault();
						break;
					default:
						break;
				}
			}
		});

		keyName.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent arg0) {
				if (!keyName.getText().isEmpty()) {
					if (!allowedKeys.contains(keyName.getText().toUpperCase())) {
						showKeyErrorDialog("Only keys from F1 to F11(excluding F5) allowed.");
					} else {
						keyName.setText(keyName.getText().toUpperCase());
						keyName.removeStyleName(AdminConstants.VALIDATION_STYLE);
					}
				} else {
					keyName.addStyleName(AdminConstants.VALIDATION_STYLE);
				}
			}
		});

		keyName.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent arg0) {
				switch (arg0.getNativeKeyCode()) {
					case KEY_CODE_F1:
						setKeyName("F1");
						break;
					case KEY_CODE_F2:
						setKeyName("F2");
						break;
					case KEY_CODE_F3:
						setKeyName("F3");
						break;
					case KEY_CODE_F4:
						setKeyName("F4");
						break;
					case KEY_CODE_F6:
						setKeyName("F6");
						break;
					case KEY_CODE_F7:
						setKeyName("F7");
						break;
					case KEY_CODE_F8:
						setKeyName("F8");
						break;
					case KEY_CODE_F9:
						setKeyName("F9");
						break;
					case KEY_CODE_F10:
						setKeyName("F10");
						break;
					case KEY_CODE_F11:
						setKeyName("F11");
						break;
					case KEY_CODE_TAB:
						break;
					case KEY_CODE_ENTER:
						setKeyName(null);
						break;
					case KEY_CODE_BACKSPACE:
						setKeyName(null);
						break;
					default:
						break;
				}
			}
		});
	}

	/**
	 * To get Validate Method Name Text Box.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateMethodNameTextBox() {
		return validateMethodNameTextBox;
	}

	/**
	 * To set Validate Method Name Text Box.
	 * 
	 * @param validateMethodNameTextBox ValidatableWidget<TextBox>
	 */
	public void setValidateMethodNameTextBox(ValidatableWidget<TextBox> validateMethodNameTextBox) {
		this.validateMethodNameTextBox = validateMethodNameTextBox;
	}

	/**
	 * To get Validate Method Description Text Box.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateMethodDescriptionTextBox() {
		return validateMethodDescriptionTextBox;
	}

	/**
	 * To set Validate Method Description Text Box.
	 * 
	 * @param validateMethodDescriptionTextBox ValidatableWidget<TextBox>
	 */
	public void setValidateMethodDescriptionTextBox(ValidatableWidget<TextBox> validateMethodDescriptionTextBox) {
		this.validateMethodDescriptionTextBox = validateMethodDescriptionTextBox;
	}

	private void setAllowedKeys() {
		allowedKeys.add("F1");
		allowedKeys.add("F2");
		allowedKeys.add("F3");
		allowedKeys.add("F4");
		allowedKeys.add("F6");
		allowedKeys.add("F7");
		allowedKeys.add("F8");
		allowedKeys.add("F9");
		allowedKeys.add("F10");
		allowedKeys.add("F11");
	}

	@UiHandler("saveButton")
	protected void onSaveClicked(ClickEvent clickEvent) {
		if (checkEnteredDetails()) {
			if (!keyName.getText().isEmpty()) {
				if (presenter.checkKeyUsedAlready(keyName.getText())) {
					showKeyErrorDialog("The key is already in use.");
				} else {
					presenter.onSave();
				}
			}
		} else {
			final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get()
					.getMessageValue(BatchClassManagementMessages.ALL_DETAILS_NECESSARY), LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.INCOMPLETE_DETAILS), Boolean.TRUE);
			confirmationDialog.addDialogListener(new DialogListener() {

				@Override
				public void onOkClick() {
					confirmationDialog.hide();
				}

				@Override
				public void onCancelClick() {
					// On cancel
				}
			});

		}
	}

	private void showKeyErrorDialog(String errorMessage) {

		final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(errorMessage, LocaleDictionary
				.get().getMessageValue(BatchClassManagementMessages.KEY_NOT_ALLOWED), Boolean.TRUE);
		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				confirmationDialog.hide();
				keyName.setFocus(true);
			}

			@Override
			public void onCancelClick() {
				// On cancel
			}
		});

		setKeyName(null);
	}

	private boolean checkEnteredDetails() {
		boolean validDetails = true;
		if (keyName.getText().isEmpty()) {
			validDetails = false;
		}
		if (methodDescription.getText().isEmpty()) {
			validDetails = false;
		}
		if (methodName.getText().isEmpty()) {
			validDetails = false;
		}
		return validDetails;
	}

	@UiHandler("cancelButton")
	protected void onCancelClicked(ClickEvent clickEvent) {
		presenter.onCancel();
	}

	/**
	 * To get Method Name.
	 * 
	 * @return String
	 */
	public String getMethodName() {
		return this.methodName.getValue();
	}

	/**
	 * To set Method Name.
	 * 
	 * @param name String
	 */
	public void setMethodName(String name) {
		this.methodName.setValue(name);
	}

	/**
	 * To get Method Description.
	 * 
	 * @return String
	 */
	public String getMethodDescription() {
		return this.methodDescription.getValue();
	}

	/**
	 * To set Method Description.
	 * 
	 * @param description String
	 */
	public void setMethodDescription(String description) {
		this.methodDescription.setValue(description);
	}

	/**
	 * To get Key Name.
	 * 
	 * @return String
	 */
	public String getKeyName() {
		return this.keyName.getValue();
	}

	/**
	 * To set Key Name.
	 * 
	 * @param name String
	 */
	public void setKeyName(String name) {
		if (name == null || (name.isEmpty())) {
			this.keyName.addStyleName(AdminConstants.VALIDATION_STYLE);
		} else {
			this.keyName.removeStyleName(AdminConstants.VALIDATION_STYLE);
		}
		this.keyName.setValue(name);
	}

	/**
	 * To get Method Name Text Box.
	 * 
	 * @return TextBox
	 */
	public TextBox getMethodNameTextBox() {
		return this.methodName;
	}

	/**
	 * To get Method Description Text Box.
	 * 
	 * @return TextBox
	 */
	public TextBox getMethodDescriptionTextBox() {
		return methodDescription;
	}

}
