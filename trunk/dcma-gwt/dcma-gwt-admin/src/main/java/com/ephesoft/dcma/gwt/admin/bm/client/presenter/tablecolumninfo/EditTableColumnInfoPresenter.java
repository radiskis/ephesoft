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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.tablecolumninfo;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.tablecolumninfo.EditTableColumnInfoView;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.validator.EmptyStringValidator;
import com.ephesoft.dcma.gwt.core.client.validator.NumberValidator;
import com.ephesoft.dcma.gwt.core.client.validator.RegExValidator;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.TableColumnInfoDTO;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;

/**
 * The presenter for view that shows the edit table column info.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class EditTableColumnInfoPresenter extends AbstractBatchClassPresenter<EditTableColumnInfoView> {

	/**
	 * Constructor.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view EditTableColumnInfoView
	 */
	public EditTableColumnInfoPresenter(BatchClassManagementController controller, EditTableColumnInfoView view) {
		super(controller, view);
	}

	/**
	 * In case of cancel click.
	 */
	public void onCancel() {
		if (controller.isAdd()) {
			controller.getMainPresenter().showTableInfoView(controller.getSelectedTableInfoField(), true);
			controller.setAdd(false);
		}
		controller.getMainPresenter().getTableColumnInfoPresenter().showTcInfoView();
	}

	/**
	 * In case of save click.
	 */
	public void onSave() {
		boolean validFlag = true;
		if (validFlag && !view.getValidateColumnNameTextBox().validate()) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.MANDATORY_FIELDS_BLANK));
			validFlag = false;
		}
		if (validFlag && !view.getValidateColumnPatternTextBox().isValid()) {
			String label = view.getColumnPatternLabel().getText();
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.VALIDATE_THE_REGEX_PATTERN, label.subSequence(0, label.length() - 1)));
			validFlag = false;
		}
		if (validFlag && !view.getValidateBetweenLeftTextBox().isValid()) {
			String label = view.getBetweenLeftLabel().getText();
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.VALIDATE_THE_REGEX_PATTERN, label.subSequence(0, label.length() - 1)));
			validFlag = false;
		}
		if (validFlag && !view.getValidateBetweenRightTextBox().isValid()) {
			String label = view.getBetweenRightLabel().getText();
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.VALIDATE_THE_REGEX_PATTERN, label.subSequence(0, label.length() - 1)));
			validFlag = false;
		}
		if (validFlag && !view.getValidateColumnHeaderPatternTextBox().isValid()) {
			String label = view.getColumnHeaderPatternLabel().getText();
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.VALIDATE_THE_REGEX_PATTERN, label.subSequence(0, label.length() - 1)));
			validFlag = false;
		}
		if (validFlag && !view.getValidateColumnStartCoordTextBox().validate()) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.NUMBER_ERROR)
					+ AdminConstants.SPACE
					+ LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.COLUMN_START_COORDINATE_LABEL));
			validFlag = false;
		}
		if (validFlag && !view.getValidateColumnEndCoordTextBox().validate()) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.NUMBER_ERROR)
					+ AdminConstants.SPACE
					+ LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.COLUMN_END_COORDINATE_LABEL));
			validFlag = false;
		}
		if (validFlag && view.getValidateColumnPatternTextBox().getWidget().getText().isEmpty()
				&& view.getValidateColumnHeaderPatternTextBox().getWidget().getText().isEmpty()
				&& view.getValidateColumnStartCoordTextBox().getWidget().getText().isEmpty()
				&& view.getValidateColumnEndCoordTextBox().getWidget().getText().isEmpty()) {
			validFlag = false;
			final ConfirmationDialog confirmationDialog = new ConfirmationDialog();
			confirmationDialog.setMessage(LocaleDictionary.get()
					.getMessageValue(BatchClassManagementMessages.MISSING_MANDATORY_FIELDS));
			confirmationDialog.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.WARNING_TITLE));

			confirmationDialog.okButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent arg0) {
					saveData();
				}
			});
			confirmationDialog.show();
			confirmationDialog.center();
			confirmationDialog.okButton.setFocus(true);
		}

		if (validFlag) {
			saveData();
		}
	}

	private void saveData() {
		if (controller.isAdd()) {
			controller.getSelectedTableInfoField().addColumnInfo(controller.getSelectedTableColumnInfoField());
			controller.setAdd(false);
		}

		controller.getSelectedTableColumnInfoField().setColumnName(view.getColumnName());
		controller.getSelectedTableColumnInfoField().setColumnPattern(view.getColumnPattern());
		controller.getSelectedTableColumnInfoField().setBetweenLeft(view.getBetweenLeft());
		controller.getSelectedTableColumnInfoField().setBetweenRight(view.getBetweenRight());
		controller.getSelectedTableColumnInfoField().setRequired(view.isRequired());
		controller.getSelectedTableColumnInfoField().setMandatory(view.isMandatory());
		controller.getSelectedTableColumnInfoField().setColumnHeaderPattern(view.getColumnHeaderPattern());
		controller.getSelectedTableColumnInfoField().setColumnStartCoordinate(view.getColumnStartCoordinate());
		controller.getSelectedTableColumnInfoField().setColumnEndCoordinate(view.getColumnEndCoordinate());
		controller.getMainPresenter().getTableColumnInfoPresenter().bind();
		controller.getMainPresenter().getTableColumnInfoPresenter().showTcInfoView();
	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {
		if (controller.getSelectedTableColumnInfoField() != null) {
			view.setBetweenLeft(controller.getSelectedTableColumnInfoField().getBetweenLeft());
			view.setBetweenRight(controller.getSelectedTableColumnInfoField().getBetweenRight());
			view.setColumnName(controller.getSelectedTableColumnInfoField().getColumnName());
			view.setColumnPattern(controller.getSelectedTableColumnInfoField().getColumnPattern());
			view.setRequired(controller.getSelectedTableColumnInfoField().isRequired());
			view.setMandatory(controller.getSelectedTableColumnInfoField().isMandatory());
			view.setColumnHeaderPattern(controller.getSelectedTableColumnInfoField().getColumnHeaderPattern());
			view.setColumnStartCoordinate(controller.getSelectedTableColumnInfoField().getColumnStartCoordinate());
			view.setColumnEndCoordinate(controller.getSelectedTableColumnInfoField().getColumnEndCoordinate());

		} else {
			TableColumnInfoDTO tcColumnInfoDTO = controller.getMainPresenter().getTableInfoViewPresenter()
					.createTableColumnInfoDTOObject();
			tcColumnInfoDTO.setBetweenLeft(view.getBetweenLeft());
			tcColumnInfoDTO.setBetweenRight(view.getBetweenRight());
			tcColumnInfoDTO.setColumnName(view.getColumnName());
			tcColumnInfoDTO.setColumnPattern(view.getColumnPattern());
			tcColumnInfoDTO.setRequired(view.isRequired());
			tcColumnInfoDTO.setMandatory(view.isMandatory());
			tcColumnInfoDTO.setColumnHeaderPattern(view.getColumnHeaderPattern());
			if (view.getColumnStartCoordinate() != null && !view.getColumnStartCoordinate().isEmpty()) {
				tcColumnInfoDTO.setColumnStartCoordinate(view.getColumnStartCoordinate());
			} else {
				tcColumnInfoDTO.setColumnStartCoordinate(BatchClassManagementConstants.EMPTY_STRING);
			}
			if (view.getColumnEndCoordinate() != null && !view.getColumnEndCoordinate().isEmpty()) {
				tcColumnInfoDTO.setColumnEndCoordinate(view.getColumnEndCoordinate());
			} else {
				tcColumnInfoDTO.setColumnEndCoordinate(BatchClassManagementConstants.EMPTY_STRING);
			}

			// controller.setAdd(true);
			controller.setSelectedTableColumnInfoField(tcColumnInfoDTO);
		}

		view.getValidateColumnNameTextBox().addValidator(new EmptyStringValidator(view.getColumnNameTextBox()));
		view.getValidateColumnNameTextBox().toggleValidDateBox();
		view.getValidateColumnPatternTextBox().addValidator(
				new RegExValidator(view.getValidateColumnPatternTextBox(), view.getColumnPatternTextBox(), false, false, true, null,
						controller.getRpcService()));
		view.getValidateColumnPatternTextBox().toggleValidDateBox();
		view.getValidateBetweenLeftTextBox().addValidator(
				new RegExValidator(view.getValidateBetweenLeftTextBox(), view.getBetweenLeftTextBox(), false, false, true, null,
						controller.getRpcService()));
		view.getValidateBetweenLeftTextBox().toggleValidDateBox();
		view.getValidateBetweenRightTextBox().addValidator(
				new RegExValidator(view.getValidateBetweenRightTextBox(), view.getBetweenRightTextBox(), false, false, true, null,
						controller.getRpcService()));
		view.getValidateBetweenRightTextBox().toggleValidDateBox();
		view.getValidateColumnHeaderPatternTextBox().addValidator(
				new RegExValidator(view.getValidateColumnHeaderPatternTextBox(), view.getColumnHeaderPatternTextBox(), false, false,
						true, null, controller.getRpcService()));
		view.getValidateColumnHeaderPatternTextBox().toggleValidDateBox();
		view.getValidateColumnStartCoordTextBox().addValidator(
				new NumberValidator(view.getColumnStartCoordinateTextBox(), false, true));
		view.getValidateColumnStartCoordTextBox().toggleValidDateBox();
		view.getValidateColumnEndCoordTextBox().addValidator(new NumberValidator(view.getColumnEndCoordinateTextBox(), false, true));
		view.getValidateColumnEndCoordTextBox().toggleValidDateBox();
		view.getValidateColumnPatternTextBox().getWidget().setFocus(true);
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
	 * To validate on click of between left pattern validate button.
	 */
	public void onBetweenLeftPatternValidateButtonClicked() {
		view.getValidateBetweenLeftTextBox().validate();

	}

	/**
	 * To validate on click of between right pattern validate button.
	 */
	public void onBetweenRightPatternValidateButtonClicked() {
		view.getValidateBetweenRightTextBox().validate();
	}

	/**
	 * To validate on click of column header pattern validate button.
	 */
	public void onColumnHeaderPatternValidateButtonClicked() {
		view.getValidateColumnHeaderPatternTextBox().validate();
	}

	/**
	 * To validate on click of column pattern validate button.
	 */
	public void onColumnPatternValidateButtonClicked() {
		view.getValidateColumnPatternTextBox().validate();

	}
}
