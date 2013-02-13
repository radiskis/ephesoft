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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.batchclassfield;

import java.util.Collection;

import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.batchclassfield.EditBatchClassFieldView;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.validator.EmptyStringValidator;
import com.ephesoft.dcma.gwt.core.client.validator.NumberValidator;
import com.ephesoft.dcma.gwt.core.client.validator.RegExValidator;
import com.ephesoft.dcma.gwt.core.shared.BatchClassFieldDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.google.gwt.event.shared.HandlerManager;

/**
 * The presenter for view that shows the edit batch class field details.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class EditBatchClassFieldPresenter extends AbstractBatchClassPresenter<EditBatchClassFieldView> {

	/**
	 * Constructor.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view EditBatchClassFieldView
	 */
	public EditBatchClassFieldPresenter(BatchClassManagementController controller, EditBatchClassFieldView view) {
		super(controller, view);
	}

	/**
	 * In case of cancel click.
	 */
	public void onCancel() {
		if (controller.isAdd()) {
			controller.getMainPresenter().showBatchClassView(controller.getBatchClass());
			controller.setAdd(false);
		} else {
			controller.getMainPresenter().getBatchClassFieldViewPresenter().showBatchClassFieldView();
		}
	}

	/**
	 * In case of save click.
	 */
	public void onSave() {
		boolean validFlag = true;
		int fieldOrder = 0;
		if (validFlag
				&& (controller.getBatchClass().checkBatchClassFieldName(view.getName(), controller.getSelectedBatchClassField()
						.getIdentifier()))) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.BATCH_CLASS_FIELD_NAME_COMMON_ERROR));
			validFlag = false;
		}
		if (!view.getValidateValidationPatternTextBox().isValid()) {
			String label = view.getValidationPatternLabel().getText();
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.VALIDATE_THE_REGEX_PATTERN, label.subSequence(0, label.length() - 1)));
			validFlag = false;
		}
		if (validFlag && view.getFieldOrderNumber() != null && !view.getFieldOrderNumber().isEmpty()) {
			try {
				fieldOrder = Integer.parseInt(view.getFieldOrderNumber());
			} catch (NumberFormatException nfe) {
				validFlag = false;
				if (controller.isAdd()) {
					ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
							BatchClassManagementMessages.NUMBER_ERROR)
							+ BatchClassManagementConstants.SPACE
							+ view.getFieldOrderNumberLabel().getText().subSequence(0,
									view.getFieldOrderNumberLabel().getText().length() - 1), LocaleDictionary.get().getConstantValue(
							BatchClassManagementConstants.ADD_BATCH_CLASS_FIELD_TITLE), Boolean.TRUE);
				} else {
					ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
							BatchClassManagementMessages.NUMBER_ERROR)
							+ BatchClassManagementConstants.SPACE
							+ view.getFieldOrderNumberLabel().getText().subSequence(0,
									view.getFieldOrderNumberLabel().getText().length() - 1), LocaleDictionary.get().getConstantValue(
							BatchClassManagementConstants.EDIT_BATCH_CLASS_FIELD_TITLE), Boolean.TRUE);

				}
			}
		}

		if (validFlag) {
			// checking whether the field order is negative.If negative,show error message.
			validFlag = !checkForNegativesAndShowErrorMessage(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.NEGATIVE_FIELD_ORDER_ERROR), fieldOrder);

			Collection<BatchClassFieldDTO> bcfList = controller.getBatchClass().getBatchClassField();
			String identifier = controller.getSelectedBatchClassField().getIdentifier();
			if (null != bcfList) {
				for (BatchClassFieldDTO batchClassFieldDTO : bcfList) {
					if (identifier.equals(batchClassFieldDTO.getIdentifier())) {
						continue;
					}
					if (Integer.parseInt(batchClassFieldDTO.getFieldOrderNumber()) == fieldOrder) {
						validFlag = false;
						if (controller.isAdd()) {
							ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
									BatchClassManagementMessages.FIELD_ORDER_DUPLICATE_ERROR, fieldOrder), LocaleDictionary.get()
									.getConstantValue(BatchClassManagementConstants.ADD_BATCH_CLASS_FIELD_TITLE), Boolean.TRUE);
						} else {
							ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
									BatchClassManagementMessages.FIELD_ORDER_DUPLICATE_ERROR, fieldOrder), LocaleDictionary.get()
									.getConstantValue(BatchClassManagementConstants.EDIT_BATCH_CLASS_FIELD_TITLE), Boolean.TRUE);
						}
						break;
					}
				}
			}
		}
		if (validFlag
				&& (!view.getValidateNameTextBox().validate() || !view.getValidateDescriptionTextBox().validate()
						|| !view.getValidateValidationPatternTextBox().validate() || !view.getValidateFieldOrderNumberTextBox()
						.validate())) {
			if (controller.isAdd()) {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.BLANK_ERROR), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.ADD_BATCH_CLASS_FIELD_TITLE), Boolean.TRUE);
			} else {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.BLANK_ERROR), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.EDIT_BATCH_CLASS_FIELD_TITLE), Boolean.TRUE);
			}
			validFlag = false;
		}
		if (validFlag) {
			updateSelectedBatchClassField();
		}
	}

	private void updateSelectedBatchClassField() {
		if (controller.isAdd()) {
			controller.getBatchClass().addBatchClassField(controller.getSelectedBatchClassField());
			controller.setAdd(false);
		}
		controller.getSelectedBatchClassField().setName(view.getName());
		controller.getSelectedBatchClassField().setDescription(view.getDescription());
		controller.getSelectedBatchClassField().setDataType(view.getDataType());
		controller.getSelectedBatchClassField().setFieldOrderNumber(view.getFieldOrderNumber());
		controller.getSelectedBatchClassField().setSampleValue(view.getSampleValue());
		controller.getSelectedBatchClassField().setValidationPattern(view.getValidationPattern());
		controller.getSelectedBatchClassField().setFieldOptionValueList(view.getFieldOptionValueList());
		controller.getMainPresenter().getBatchClassFieldViewPresenter().bind();
		controller.getMainPresenter().getBatchClassFieldViewPresenter().showBatchClassFieldView();
		if (controller.getSelectedBatchClassField().isNew()) {
			controller.getMainPresenter().getBatchClassBreadCrumbPresenter().createBreadCrumb(controller.getSelectedBatchClassField());
		}
	}

	/**
	 * API checks whether the given number is negative,if negative then shows the error message.
	 * 
	 * @param message{@link String}
	 * @param value{@link int}
	 * @return isNegative{@link boolean}
	 */
	private boolean checkForNegativesAndShowErrorMessage(String message, int value) {
		boolean isNegative = false;
		if (value < 0) {
			isNegative = true;
			ConfirmationDialogUtil.showConfirmationDialogError(message);
		}
		return isNegative;
	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {
		if (controller.getSelectedBatchClassField() != null) {
			view.setName(controller.getSelectedBatchClassField().getName());
			view.setDescription(controller.getSelectedBatchClassField().getDescription());
			view.setDataType(controller.getSelectedBatchClassField().getDataType());
			view.setFieldOrderNumber(controller.getSelectedBatchClassField().getFieldOrderNumber());
			view.setSampleValue(controller.getSelectedBatchClassField().getSampleValue());
			view.setValidationPattern(controller.getSelectedBatchClassField().getValidationPattern());
			view.setFieldOptionValueList(controller.getSelectedBatchClassField().getFieldOptionValueList());

			view.getValidateNameTextBox().addValidator(new EmptyStringValidator(view.getNameTextBox()));
			view.getValidateDescriptionTextBox().addValidator(new EmptyStringValidator(view.getDescriptionTextBox()));
			view.getValidateFieldOrderNumberTextBox().addValidator(
					new NumberValidator(view.getFieldOrderNumberTextBox(), false, false, true));
			view.getValidateValidationPatternTextBox().addValidator(
					new RegExValidator(view.getValidateValidationPatternTextBox(), view.getValidationPatternTextBox(), false, false,
							true, null, controller.getRpcService()));
			view.getValidateNameTextBox().toggleValidDateBox();
			view.getValidateDescriptionTextBox().toggleValidDateBox();
			view.getValidateFieldOrderNumberTextBox().toggleValidDateBox();
			view.getNameTextBox().setFocus(true);
		}
	}

	/**
	 * To handle events.
	 * 
	 * @param eventBus HandlerManager
	 */
	@Override
	public void injectEvents(HandlerManager eventBus) {
		// Event handling is done here.
	}

	/**
	 * To perform operations on Validate button clicked.
	 */
	public void onValidateButtonClicked() {
		view.getValidateValidationPatternTextBox().validate();

	}

}
