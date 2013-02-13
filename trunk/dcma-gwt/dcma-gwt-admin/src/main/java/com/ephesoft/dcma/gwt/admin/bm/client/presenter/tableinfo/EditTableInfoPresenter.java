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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.tableinfo;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.tableinfo.EditTableInfoView;
import com.ephesoft.dcma.gwt.core.client.RandomIdGenerator;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.validator.EmptyStringValidator;
import com.ephesoft.dcma.gwt.core.client.validator.NumberValidator;
import com.ephesoft.dcma.gwt.core.client.validator.RegExValidator;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.TableInfoDTO;
import com.google.gwt.event.shared.HandlerManager;

/**
 * The presenter for view that shows the edit table info.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class EditTableInfoPresenter extends AbstractBatchClassPresenter<EditTableInfoView> {

	/**
	 * Constructor.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view EditTableInfoView
	 */
	public EditTableInfoPresenter(BatchClassManagementController controller, EditTableInfoView view) {
		super(controller, view);
	}

	/**
	 * In case of cancel click.
	 */
	public void onCancel() {
		if (controller.isAdd()) {
			controller.getMainPresenter().showDocumentTypeView(controller.getSelectedTableInfoField().getDocTypeDTO(), true);
			controller.setAdd(false);
		}
		controller.getMainPresenter().getTableInfoViewPresenter().showTableInfoView();
	}

	/**
	 * In case of save click.
	 */
	public void onSave() {
		boolean validFlag = true;

		if (validFlag && !view.getValidateNameTextBox().validate()) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.MANDATORY_FIELDS_BLANK));
			validFlag = false;
		}
		if (validFlag && !view.getValidateStartPatternTextBox().isValid()) {
			if (view.getValidateStartPatternTextBox().getWidget().getText().isEmpty()) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.MANDATORY_FIELDS_BLANK));
			} else {
				String label = view.getStartPatternLabel().getText();
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.VALIDATE_THE_REGEX_PATTERN, label.subSequence(0, label.length() - 1)));
			}
			validFlag = false;
		}
		if (validFlag && !view.getValidateEndPatternTextBox().isValid()) {
			if (view.getValidateEndPatternTextBox().getWidget().getText().isEmpty()) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.MANDATORY_FIELDS_BLANK));
			} else {
				String label = view.getEndPatternLabel().getText();
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.VALIDATE_THE_REGEX_PATTERN, label.subSequence(0, label.length() - 1)));
			}
			validFlag = false;
		}
		if (validFlag && (view.getTableExtractionAPI() == null || view.getTableExtractionAPI().isEmpty())) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.NO_EXTRACTION_API_SELECTED));
			validFlag = false;
		}

		if (validFlag && !view.getValidateWidthOfMultiLineTextBox().validate()) {
			ConfirmationDialogUtil
					.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.NUMBER_ERROR)
							+ AdminConstants.SPACE
							+ LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.WIDTH_OF_MULTILINE));
			validFlag = false;
		}

		if (validFlag) {
			if (controller.isAdd()) {
				controller.getSelectedDocument().addTableInfoDTO(controller.getSelectedTableInfoField());
				controller.setAdd(false);
			}
			controller.getSelectedTableInfoField().setName(view.getName());
			controller.getSelectedTableInfoField().setStartPattern(view.getStartPattern());
			controller.getSelectedTableInfoField().setEndPattern(view.getEndPattern());
			controller.getSelectedTableInfoField().setTableExtractionAPI(view.getTableExtractionAPI());
			controller.getSelectedTableInfoField().setWidthOfMultiline(view.getWidthOfMultiLine());

			controller.getMainPresenter().getTableInfoViewPresenter().bind();
			controller.getMainPresenter().getTableInfoViewPresenter().showTableInfoView();
			if (controller.getSelectedTableInfoField().isNew()) {
				controller.getMainPresenter().getBatchClassBreadCrumbPresenter().createBreadCrumb(
						controller.getSelectedTableInfoField());
			}
		}
	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {
		if (controller.getSelectedTableInfoField() != null) {
			view.setName(controller.getSelectedTableInfoField().getName());
			view.setStartPattern(controller.getSelectedTableInfoField().getStartPattern());
			view.setEndPattern(controller.getSelectedTableInfoField().getEndPattern());
			view.setTableExtractionAPI(controller.getSelectedTableInfoField().getTableExtractionAPI());
			view.setWidthOfMultiLine(controller.getSelectedTableInfoField().getWidthOfMultiline());
		} else {
			TableInfoDTO tableInfoDTO = controller.getMainPresenter().getDocumentTypeViewPresenter().createTableInfoDTOObject();
			tableInfoDTO.setName(view.getName());
			tableInfoDTO.setStartPattern(view.getStartPattern());
			tableInfoDTO.setEndPattern(view.getEndPattern());
			tableInfoDTO.setTableExtractionAPI(view.getTableExtractionAPI());
			tableInfoDTO.setWidthOfMultiline(view.getWidthOfMultiLine());
			tableInfoDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
			controller.setTableInfoSelectedField(tableInfoDTO);
		}

		view.getValidateNameTextBox().addValidator(new EmptyStringValidator(view.getNameTextBox()));
		view.getValidateStartPatternTextBox().addValidator(
				new RegExValidator(view.getValidateStartPatternTextBox(), view.getStartPatternTextBox(), true, false, true, null,
						controller.getRpcService()));
		view.getValidateEndPatternTextBox().addValidator(
				new RegExValidator(view.getValidateEndPatternTextBox(), view.getEndPatternTextBox(), true, false, true, null,
						controller.getRpcService()));
		view.getValidateNameTextBox().toggleValidDateBox();
		view.getValidateStartPatternTextBox().toggleValidDateBox();
		view.getValidateEndPatternTextBox().toggleValidDateBox();
		view.getValidateWidthOfMultiLineTextBox().addValidator(new NumberValidator(view.getWidthOfMultiLineTextBox(), false, true));
		view.getValidateNameTextBox().getWidget().setFocus(true);
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
	 * To validate on click of start pattern validate button.
	 */
	public void onStartPatternValidateButtonClicked() {
		view.getValidateStartPatternTextBox().validate();

	}

	/**
	 * To validate on click of end pattern validate button.
	 */
	public void onEndPatternValidateButtonClicked() {
		view.getValidateEndPatternTextBox().validate();

	}

}
