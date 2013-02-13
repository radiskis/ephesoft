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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.fieldtype;

import java.util.Collection;
import java.util.List;

import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.fieldtype.EditFieldTypeView;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.validator.EmptyStringValidator;
import com.ephesoft.dcma.gwt.core.client.validator.NumberValidator;
import com.ephesoft.dcma.gwt.core.client.validator.RegExValidator;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.FieldTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.RegexDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The presenter for view that shows the edit field type details.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class EditFieldTypePresenter extends AbstractBatchClassPresenter<EditFieldTypeView> {

	/**
	 * Constructor.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view EditFieldTypeView
	 */
	public EditFieldTypePresenter(BatchClassManagementController controller, EditFieldTypeView view) {
		super(controller, view);
	}

	/**
	 * In case of cancel click.
	 */
	public void onCancel() {
		if (controller.isAdd()) {
			controller.getMainPresenter().showDocumentTypeView(controller.getSelectedDocumentLevelField().getDocTypeDTO(), true);
			controller.setAdd(false);
		}
		controller.getMainPresenter().getFieldTypeViewPresenter().showFieldTypeView();
	}

	/**
	 * In case of save click.
	 */
	public void onSave() {
		boolean validFlag = true;
		int fieldOrder = 0;
		if (validFlag && view.getFdOrder() != null && !view.getFdOrder().isEmpty()) {
			try {
				fieldOrder = Integer.parseInt(view.getFdOrder());

			} catch (NumberFormatException nfe) {
				validFlag = false;
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NUMBER_ERROR)
						+ BatchClassManagementConstants.SPACE
						+ view.getFdOrderLabel().getText().subSequence(0, view.getFdOrderLabel().getText().length() - 1));
			}
		}

		if (validFlag) {

			// checking whether the field order is negative.If negative,show error message.
			validFlag = !checkForNegativesAndShowErrorMessage(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.NEGATIVE_FIELD_ORDER_ERROR), fieldOrder);

			Collection<FieldTypeDTO> dlfList = controller.getSelectedDocument().getFields();
			String identifier = controller.getSelectedDocumentLevelField().getIdentifier();
			if (null != dlfList) {
				validFlag = validateDocumentList(validFlag, dlfList, identifier, fieldOrder);
			}
		}

		if (validFlag
				&& (!view.getValidatePatternTextBox().isValid() || !view.getValidateDescriptionTextBox().validate()
						|| !view.getValidateNameTextBox().validate() || !view.getValidateFdOrderTextBox().validate())) {
			if (view.getValidatePatternTextBox().getWidget().getText().isEmpty()
					|| view.getValidateFdOrderTextBox().getWidget().getText().isEmpty()) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.MANDATORY_FIELDS_BLANK));
			} else {
				String label = view.getPatternLabel().getText();
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.VALIDATE_THE_REGEX_PATTERN, label.subSequence(0, label.length() - 1)));
			}
			validFlag = false;
		}

		if (validFlag && (controller.isAdd() && controller.getSelectedDocument().checkFieldTypeName(view.getName()))) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.NAME_COMMON_ERROR));
			validFlag = false;
		}

		if (validFlag) {
			if (controller.isAdd()) {
				controller.getSelectedDocument().addFieldType(controller.getSelectedDocumentLevelField());
				controller.setAdd(false);
			}

			final List<RegexDTO> regexList = controller.getSelectedDocumentLevelField().getRegexList();
			if (!regexList.isEmpty()) {
				validateRegexList(regexList);
			} else {
				setDocumentLevelAttributes();
				controller.getMainPresenter().getFieldTypeViewPresenter().bind();
				controller.getMainPresenter().getFieldTypeViewPresenter().showFieldTypeView();
				if (controller.getSelectedDocumentLevelField().isNew()) {
					controller.getMainPresenter().getBatchClassBreadCrumbPresenter().createBreadCrumb(
							controller.getSelectedDocumentLevelField());
				}
			}
		}
	}

	private void validateRegexList(final List<RegexDTO> regexList) {
		if (view.isHidden() || view.isMultiLine() || view.getIsReadonlyValue()) {
			final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get()
					.getMessageValue(BatchClassManagementMessages.DELETE_REGEX_VALIDATION_CONFIRMATION), LocaleDictionary.get()
					.getConstantValue(BatchClassManagementConstants.DELETE_REGEX_VALIDATION), Boolean.FALSE);

			confirmationDialog.addDialogListener(new DialogListener() {

				@Override
				public void onOkClick() {
					setDocumentLevelAttributes();
					for (RegexDTO regexObj : regexList) {
						regexObj.setDeleted(true);
						controller.getBatchClass().setDirty(true);

					}
					controller.getMainPresenter().getFieldTypeViewPresenter().bind();
					controller.getMainPresenter().getFieldTypeViewPresenter().showFieldTypeView();
					if (controller.getSelectedDocumentLevelField().isNew()) {
						controller.getMainPresenter().getBatchClassBreadCrumbPresenter().createBreadCrumb(
								controller.getSelectedDocumentLevelField());
					}
				}

				@Override
				public void onCancelClick() {
					if (view.isHidden()) {
						view.setHidden(false);
					}
					if (view.isMultiLine()) {
						view.setMultiLine(false);
					}
					if (view.getIsReadonlyValue()) {
						view.setReadOnly(false);
					}
					confirmationDialog.hide();
				}
			});

		}

		else {
			setDocumentLevelAttributes();
			controller.getMainPresenter().getFieldTypeViewPresenter().bind();
			controller.getMainPresenter().getFieldTypeViewPresenter().showFieldTypeView();
			if (controller.getSelectedDocumentLevelField().isNew()) {
				controller.getMainPresenter().getBatchClassBreadCrumbPresenter().createBreadCrumb(
						controller.getSelectedDocumentLevelField());
			}
		}
	}

	private boolean validateDocumentList(boolean validFlag, Collection<FieldTypeDTO> dlfList, String identifier, int fieldOrder) {
		boolean valid = validFlag;
		for (FieldTypeDTO fieldTypeDTO : dlfList) {
			if (identifier.equals(fieldTypeDTO.getIdentifier())) {
				continue;
			}
			if (Integer.parseInt(fieldTypeDTO.getFieldOrderNumber()) == fieldOrder) {
				valid = false;
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.FIELD_ORDER_DUPLICATE_ERROR, fieldOrder));
				break;
			}
		}
		return valid;
	}

	/**
	 * API checks whether the given number is negative,if negative then shows the error message.
	 * 
	 * @param value int
	 * @param errorMessage String
	 * @return isNegative{@link boolean}
	 */
	private boolean checkForNegativesAndShowErrorMessage(String errorMessage, int value) {
		boolean isNegative = false;
		if (value < 0) {
			isNegative = true;
			ConfirmationDialogUtil.showConfirmationDialogError(errorMessage);
		}
		return isNegative;
	}

	private void setDocumentLevelAttributes() {
		controller.getSelectedDocumentLevelField().setDescription(view.getDescription());
		controller.getSelectedDocumentLevelField().setName(view.getName());
		controller.getSelectedDocumentLevelField().setPattern(view.getPattern());
		controller.getSelectedDocumentLevelField().setDataType(view.getDataType());
		controller.getSelectedDocumentLevelField().setFieldOrderNumber(view.getFdOrder());
		controller.getSelectedDocumentLevelField().setSampleValue(view.getSampleValue());
		controller.getSelectedDocumentLevelField().setFieldOptionValueList(view.getFieldOptionValueList());
		controller.getSelectedDocumentLevelField().setBarcodeType(view.getBarcodeType());
		controller.getSelectedDocumentLevelField().setHidden(view.isHidden());
		controller.getSelectedDocumentLevelField().setMultiLine(view.isMultiLine());
		controller.getSelectedDocumentLevelField().setReadOnly(view.getIsReadonlyValue());

	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {
		if (controller.getSelectedDocumentLevelField() != null) {
			getAllBarcodeValues();
			view.setDescription(controller.getSelectedDocumentLevelField().getDescription());
			view.setName(controller.getSelectedDocumentLevelField().getName());
			view.setDataType(controller.getSelectedDocumentLevelField().getDataType());
			view.setPattern(controller.getSelectedDocumentLevelField().getPattern());
			view.setFdOrder(controller.getSelectedDocumentLevelField().getFieldOrderNumber());
			view.setSampleValue(controller.getSelectedDocumentLevelField().getSampleValue());
			view.setFieldOptionValueList(controller.getSelectedDocumentLevelField().getFieldOptionValueList());
			view.setHidden(controller.getSelectedDocumentLevelField().isHidden());
			view.setMultiLine(controller.getSelectedDocumentLevelField().isMultiLine());
			view.setReadOnly(controller.getSelectedDocumentLevelField().getIsReadOnly());
			view.getNameTextBox().setReadOnly(!controller.getSelectedDocumentLevelField().isNew());
		} else {
			FieldTypeDTO fieldTypeDTO = controller.getMainPresenter().getDocumentTypeViewPresenter().createFieldTypeDTOObject();
			fieldTypeDTO.setDescription(view.getDescription());
			fieldTypeDTO.setName(view.getName());
			fieldTypeDTO.setPattern(view.getPattern());
			fieldTypeDTO.setFieldOrderNumber(view.getFdOrder());
			fieldTypeDTO.setSampleValue(view.getSampleValue());
			fieldTypeDTO.setFieldOptionValueList(view.getFieldOptionValueList());
			fieldTypeDTO.setHidden(view.isHidden());
			fieldTypeDTO.setMultiLine(view.isMultiLine());
			fieldTypeDTO.setReadOnly(view.getIsReadonlyValue());
			// controller.setAdd(true);
			controller.setSelectedDocumentLevelField(fieldTypeDTO);
		}

		view.getValidateNameTextBox().addValidator(new EmptyStringValidator(view.getNameTextBox()));
		view.getValidateDescriptionTextBox().addValidator(new EmptyStringValidator(view.getDescriptionTextBox()));
		view.getValidatePatternTextBox().addValidator(
				new RegExValidator(view.getValidatePatternTextBox(), view.getPatternTextBox(), true, true, true,
						BatchClassManagementConstants.PATTERN_DELIMITER, controller.getRpcService()));
		view.getValidateFdOrderTextBox().addValidator(new EmptyStringValidator(view.getFdOrderTextBox()));
		view.getValidateFdOrderTextBox().addValidator(new NumberValidator(view.getFdOrderTextBox(), false, false, true));

		view.getValidatePatternTextBox().toggleValidDateBox();
		view.getValidateDescriptionTextBox().toggleValidDateBox();
		view.getValidateNameTextBox().toggleValidDateBox();
		view.getValidateFdOrderTextBox().toggleValidDateBox();
		if (!view.getNameTextBox().isReadOnly()) {
			view.getNameTextBox().setFocus(true);
		} else {
			view.getDescriptionTextBox().setFocus(true);
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
	 * To get all Barcode values.
	 */
	public void getAllBarcodeValues() {
		controller.getRpcService().getAllBarcodeTypes(new AsyncCallback<List<String>>() {

			@Override
			public void onSuccess(List<String> allValues) {
				view.setAllBarcodeValues(allValues);
				view.setBarcodeType(controller.getSelectedDocumentLevelField().getBarcodeType());
			}

			@Override
			public void onFailure(Throwable arg0) {
				/*
				 * On Failure
				 */
			}
		});
	}

	/**
	 * In case of validate button clicked.
	 */
	public void onValidateButtonClicked() {
		view.getValidatePatternTextBox().validate();
	}

}
