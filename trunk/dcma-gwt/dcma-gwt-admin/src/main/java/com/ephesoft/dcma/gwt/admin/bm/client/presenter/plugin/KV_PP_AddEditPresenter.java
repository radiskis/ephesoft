/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2010-2011 Ephesoft Inc. 
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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin;

import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.plugin.KV_PP_AddEditView;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.validator.EmptyStringValidator;
import com.ephesoft.dcma.gwt.core.client.validator.NumberValidator;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginConfigDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.KVPageProcessDTO;
import com.google.gwt.event.shared.HandlerManager;

public class KV_PP_AddEditPresenter extends AbstractBatchClassPresenter<KV_PP_AddEditView> {

	public KV_PP_AddEditPresenter(BatchClassManagementController controller, KV_PP_AddEditView view) {
		super(controller, view);
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {

	}

	@Override
	public void bind() {
		KVPageProcessDTO kvPageProcessDTO = controller.getSelectedKvPageProcessDTO();
		if (controller.isAdd()) {
			view.setKeyPattern("");
			view.setLocation(view.getDefaultLocation());
			view.setValuePattern("");
			view.setNoOfWords("0");
			view.setPageLevelFieldName("");
			view.getValidateKeyPatternTextBox().addValidator(new EmptyStringValidator(view.getKeyPatternTextBox()));
			view.getValidateKeyPatternTextBox().toggleValidDateBox();
			view.getValidateValuePatternTextBox().addValidator(new EmptyStringValidator(view.getValuePatternTextBox()));
			view.getValidateNoOfWordsTextBox().addValidator(new NumberValidator(view.getNoOfWordsTextBox()));
			view.getValidateValuePatternTextBox().toggleValidDateBox();
			view.getValidatePageLevelFieldNameLabelTextBox().addValidator(
					new EmptyStringValidator(view.getPageLevelFieldNameTextBox()));
			view.getValidatePageLevelFieldNameLabelTextBox().toggleValidDateBox();
		} else if (kvPageProcessDTO != null) {
			view.setKeyPattern(kvPageProcessDTO.getKeyPattern());
			view.setLocation(kvPageProcessDTO.getLocationType());
			view.setValuePattern(kvPageProcessDTO.getValuePattern());
			view.setNoOfWords(kvPageProcessDTO.getNoOfWords().toString());
			view.setPageLevelFieldName(kvPageProcessDTO.getPageLevelFieldName());
			view.getValidateKeyPatternTextBox().addValidator(new EmptyStringValidator(view.getKeyPatternTextBox()));
			view.getValidateKeyPatternTextBox().toggleValidDateBox();
			view.getValidateValuePatternTextBox().addValidator(new EmptyStringValidator(view.getValuePatternTextBox()));
			view.getValidateNoOfWordsTextBox().addValidator(new NumberValidator(view.getNoOfWordsTextBox()));
			view.getValidateValuePatternTextBox().toggleValidDateBox();
			view.getValidatePageLevelFieldNameLabelTextBox().addValidator(
					new EmptyStringValidator(view.getPageLevelFieldNameTextBox()));
			view.getValidatePageLevelFieldNameLabelTextBox().toggleValidDateBox();
		}
	}

	public void onSave() {
		boolean validFlag = true;
		if (validFlag && (!view.getValidateKeyPatternTextBox().validate() || !view.getValidateValuePatternTextBox().validate())) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.BLANK_ERROR));
			validFlag = false;
		}

		if (validFlag
				&& (controller.isAdd() && controller.getPluginConfigDTO().checkKVPageProcessDetails(view.getKeyPattern(),
						view.getValuePattern(), view.getLocation()))) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.NAME_COMMON_ERROR));
			validFlag = false;
		}

		if (validFlag && !view.getValidateNoOfWordsTextBox().validate()) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.NUMBER_ERROR)
					+ " " + view.getNoOfWordsLabel().getText().substring(0, view.getNoOfWordsLabel().getText().length()));
			validFlag = false;
		}

		if (validFlag
				&& (!view.getValidatePageLevelFieldNameLabelTextBox().validate() || !view.getValidatePageLevelFieldNameLabelTextBox()
						.validate())) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.BLANK_ERROR));
			validFlag = false;
		}

		if (validFlag) {
			if (controller.isAdd()) {
				KVPageProcessDTO kvPageProcessDTO = controller.getMainPresenter().getKvPPPropertiesPresenter()
						.getKvPPConfigPresenter().getKvPageProcessDTO();
				BatchClassPluginConfigDTO batchClassPluginConfigDTO = controller.getMainPresenter().getKvPPPropertiesPresenter()
						.getKvPPConfigPresenter().getBatchClassPluginConfigDTO();
				batchClassPluginConfigDTO.addKVPageProcessDTO(kvPageProcessDTO);
				controller.setAdd(false);
			}
			controller.getSelectedKvPageProcessDTO().setKeyPattern(view.getKeyPattern());
			controller.getSelectedKvPageProcessDTO().setLocationType(view.getLocation());
			controller.getSelectedKvPageProcessDTO().setValuePattern(view.getValuePattern());
			controller.getSelectedKvPageProcessDTO().setNoOfWords(Integer.parseInt(view.getNoOfWords()));
			controller.getSelectedKvPageProcessDTO().setPageLevelFieldName(view.getPageLevelFieldName());
			controller.getMainPresenter().getKvPPPropertiesPresenter().bind();
			controller.getMainPresenter().showKVppPluginConfigAddEditView();
			controller.getMainPresenter().getKvPPAddEditListPresenter().showKVPPDetailView();
			controller.getBatchClass().setDirty(true);
		}
	}

	public void onCancel() {
		if (controller.isAdd()) {
			controller.getPluginConfigDTO().removeKVPageProcessDTO(controller.getSelectedKvPageProcessDTO());
			controller.getMainPresenter().getKvPPPropertiesPresenter().bind();
			controller.getMainPresenter().showKVppPluginView();
			controller.getMainPresenter().getKvPPPropertiesPresenter().showKVppPluginConfigView();
			controller.setAdd(false);
			return;
		}
		controller.getMainPresenter().getModuleViewPresenter().bind();
		controller.getMainPresenter().showModuleView();
	}
}
