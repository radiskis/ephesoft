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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.regex;

import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.regex.EditRegexView;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.validator.RegExValidator;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.RegexDTO;
import com.google.gwt.event.shared.HandlerManager;

/**
 * The presenter for view that shows the edit regex details.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class EditRegexPresenter extends AbstractBatchClassPresenter<EditRegexView> {

	/**
	 * Constructor.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view EditRegexView
	 */
	public EditRegexPresenter(BatchClassManagementController controller, EditRegexView view) {
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
		controller.getMainPresenter().getRegexPresenter().showRegexDetailView();
	}

	/**
	 * In case of save click.
	 */
	public void onSave() {
		boolean validFlag = true;
		if (validFlag && !view.getValidatePatternTextBox().isValid()) {
			if (view.getValidatePatternTextBox().getWidget().getText().isEmpty()) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.MANDATORY_FIELDS_BLANK));
			} else {
				String label = view.getPatternLabel().getText();
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.VALIDATE_THE_REGEX_PATTERN, label.subSequence(0, label.length() - 1)));
			}
			validFlag = false;
		}

		if (validFlag && (controller.isAdd() && controller.getSelectedDocumentLevelField().checkRegex(view.getPattern()))) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.NAME_COMMON_ERROR));
			validFlag = false;
		}
		if (validFlag) {
			if (controller.isAdd()) {
				controller.getSelectedDocumentLevelField().addRegex(controller.getSelectedRegex());
				controller.setAdd(false);
			}

			controller.getSelectedRegex().setPattern(view.getPattern());
			controller.getMainPresenter().getRegexPresenter().bind();
			controller.getMainPresenter().getRegexPresenter().showRegexDetailView();
		}
	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {
		if (controller.getSelectedRegex() != null) {
			view.setPattern(controller.getSelectedRegex().getPattern());
		} else {
			RegexDTO regexDTO = controller.getMainPresenter().getFieldTypeViewPresenter().createRegexDTOObject();
			regexDTO.setPattern(view.getPattern());
			// controller.setAdd(true);
			controller.setSelectedRegex(regexDTO);
		}
		view.getValidatePatternTextBox().addValidator(
				new RegExValidator(view.getValidatePatternTextBox(), view.getPatternTextBox(), true, false, true, null, controller
						.getRpcService()));
		view.getValidatePatternTextBox().toggleValidDateBox();
		view.getValidatePatternTextBox().getWidget().setFocus(true);
	}

	/**
	 * To handle events.
	 * 
	 * @param eventBus HandlerManager
	 */
	@Override
	public void injectEvents(HandlerManager eventBus) {
		// To be used in case of event handling.
	}

	/**
	 * To validate on validate pattern button clicked.
	 */
	public void onValidatePatternButtonClicked() {
		view.getValidatePatternTextBox().validate();
	}

}
