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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.kvextraction;

import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.kvextraction.EditKVExtractionView;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.validator.NumberValidator;
import com.ephesoft.dcma.gwt.core.client.validator.RegExValidator;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.KVExtractionDTO;
import com.google.gwt.event.shared.HandlerManager;

/**
 * The presenter for view that shows the edit KV extraction details.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class EditKVExtractionPresenter extends AbstractBatchClassPresenter<EditKVExtractionView> {

	/**
	 * Constructor.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view EditFunctionKeyView
	 */
	public EditKVExtractionPresenter(BatchClassManagementController controller, EditKVExtractionView view) {
		super(controller, view);
	}

	/**
	 * In case of cancel click.
	 */
	public void onCancel() {
		if (controller.isAdd()) {
			controller.getMainPresenter().showFieldTypeView(controller.getSelectedDocument(), true);
			controller.setAdd(false);
		}
		controller.getMainPresenter().getKvExtractionPresenter().showKVExtractionView();
	}

	/**
	 * In case of save click.
	 */
	public void onSave() {
		boolean validFlag = true;
		if ((!view.getValidateKeyPatternTextBox().isValid())) {
			if (view.getValidateKeyPatternTextBox().getWidget().getText().isEmpty()) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.MANDATORY_FIELDS_BLANK));
			} else {
				String label = view.getKeyPatternLabel().getText();
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.VALIDATE_THE_REGEX_PATTERN, label.subSequence(0, label.length() - 1)));
			}
			validFlag = false;
		}
		if (validFlag && !view.getValidateValuePatternTextBox().isValid()) {
			if (view.getValidateValuePatternTextBox().getWidget().getText().isEmpty()) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.MANDATORY_FIELDS_BLANK));
			} else {
				String label = view.getValuePatternLabel().getText();
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.VALIDATE_THE_REGEX_PATTERN, label.subSequence(0, label.length() - 1)));
			}
			validFlag = false;
		}

		if (validFlag
				&& (!view.getValidateNoOfWordsTextBox().validate() && (view.getNoOfWords() == null || view.getNoOfWords().length() == 0))) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.BLANK_ERROR));
			validFlag = false;
		}

		if (validFlag && !view.getValidateNoOfWordsTextBox().validate()) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.NUMBER_ERROR)
					+ BatchClassManagementConstants.SPACE
					+ view.getNoOfWordsLabel().getText().substring(0, view.getNoOfWordsLabel().getText().length()));
			validFlag = false;
		}

		if (validFlag
				&& (controller.isAdd() && controller.getSelectedDocumentLevelField().checkKVExtractionDetails(view.getKeyPattern(),
						view.getValuePattern(), view.getLocation()))) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.NAME_COMMON_ERROR));
			validFlag = false;
		}
		if (validFlag) {
			if (controller.isAdd()) {
				controller.getSelectedDocumentLevelField().addKvExtraction(controller.getSelectedKVExtraction());
				controller.setAdd(false);
			}
			controller.getSelectedKVExtraction().setLength(null);
			controller.getSelectedKVExtraction().setWidth(null);
			controller.getSelectedKVExtraction().setXoffset(null);
			controller.getSelectedKVExtraction().setYoffset(null);
			controller.getSelectedKVExtraction().setMultiplier(null);
			controller.getSelectedKVExtraction().setFetchValue(null);
			controller.getSelectedKVExtraction().setKvPageValue(null);
			controller.getSelectedKVExtraction().setUseExistingKey(false);
			controller.getSelectedKVExtraction().setKeyPattern(view.getKeyPattern());
			controller.getSelectedKVExtraction().setLocationType(view.getLocation());
			controller.getSelectedKVExtraction().setValuePattern(view.getValuePattern());
			if (view.getNoOfWords() != null && view.getNoOfWords().length() != 0) {
				controller.getSelectedKVExtraction().setNoOfWords(Integer.parseInt(view.getNoOfWords()));
			}
			controller.getMainPresenter().getKvExtractionPresenter().bind();
			controller.getMainPresenter().getKvExtractionPresenter().showKVExtractionView();
		}
	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {
		if (controller.getSelectedKVExtraction() != null) {
			view.setKeyPattern(controller.getSelectedKVExtraction().getKeyPattern());
			view.setLocation(controller.getSelectedKVExtraction().getLocationType());
			view.setValuePattern(controller.getSelectedKVExtraction().getValuePattern());
			if (controller.getSelectedKVExtraction().getNoOfWords() != null) {
				view.setNoOfWords(String.valueOf(controller.getSelectedKVExtraction().getNoOfWords()));
			}
		} else {
			KVExtractionDTO kvExtractionDTO = controller.getMainPresenter().getFieldTypeViewPresenter().createKVExtractionDTOObject();
			kvExtractionDTO.setKeyPattern(view.getKeyPattern());
			kvExtractionDTO.setValuePattern(view.getValuePattern());

			// controller.setAdd(true);
			controller.setSelectedKVExtraction(kvExtractionDTO);
		}

		view.getValidateKeyPatternTextBox().addValidator(
				new RegExValidator(view.getValidateKeyPatternTextBox(), view.getKeyPatternTextBox(), true, false, true, null,
						controller.getRpcService()));
		view.getValidateKeyPatternTextBox().toggleValidDateBox();
		view.getValidateValuePatternTextBox().addValidator(
				new RegExValidator(view.getValidateValuePatternTextBox(), view.getValuePatternTextBox(), true, false, true, null,
						controller.getRpcService()));
		view.getValidateValuePatternTextBox().toggleValidDateBox();
		view.getValidateNoOfWordsTextBox().addValidator(new NumberValidator(view.getNoOfWordsTextBox(), false, false));
		view.getValidateKeyPatternTextBox().toggleValidDateBox();
		view.getKeyPatternTextBox().setFocus(true);
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
	 * To show Advanced KV Extraction View.
	 */
	public void showAdvancedKVExtractionView() {
		controller.getMainPresenter().showAdvancedKVExtractionView();
	}

	/**
	 * To perform operations on Key Pattern Validate button clicked.
	 */
	public void onKeyPatternValidateButtonClicked() {
		view.getValidateKeyPatternTextBox().validate();
	}

	/**
	 * To perform operations on Value Pattern Validate button clicked.
	 */
	public void onValuePatternValidateButtonClicked() {
		view.getValidateValuePatternTextBox().validate();
	}
}
