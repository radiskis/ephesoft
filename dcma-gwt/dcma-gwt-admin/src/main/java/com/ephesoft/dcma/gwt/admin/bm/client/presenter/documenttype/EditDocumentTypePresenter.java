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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.documenttype;

import java.util.List;

import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.MessageConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.documenttype.EditDocumentTypeView;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.validator.EmptyStringValidator;
import com.ephesoft.dcma.gwt.core.client.validator.NumberValidator;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class EditDocumentTypePresenter extends AbstractBatchClassPresenter<EditDocumentTypeView> {

	public EditDocumentTypePresenter(BatchClassManagementController controller, EditDocumentTypeView view) {
		super(controller, view);
	}

	public void onCancel() {
		if (controller.isAdd()) {
			controller.getMainPresenter().showBatchClassView(controller.getSelectedDocument().getBatchClass());
			controller.setAdd(false);
		} else {
			controller.getMainPresenter().getDocumentTypeViewPresenter().showDocumentTypeView();
		}
	}

	public void onSave() {
		boolean validFlag = true;
		if (validFlag && view.getConfidence().length() != 0) {
			try {
				controller.getSelectedDocument().setMinConfidenceThreshold(Float.parseFloat(view.getConfidence()));
			} catch (NumberFormatException e) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.INTEGER_ERROR)
						+ "  " + view.getConfLabel().substring(0, view.getConfLabel().length() - 1));
				validFlag = false;
			}
		}
		if (validFlag
				&& (!view.getValidateConfidenceTextBox().validate() || !view.getValidateDescriptionTextBox().validate() || !view
						.getValidateNameTextBox().validate())) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.BLANK_ERROR));
			validFlag = false;
		}
		if (validFlag && (controller.isAdd() && controller.getBatchClass().checkDocumentTypeName(view.getName()))) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.NAME_COMMON_ERROR));
			validFlag = false;
		}
		if (validFlag) {
			if (controller.isAdd()) {
				controller.getBatchClass().addDocumentType(controller.getSelectedDocument());
				controller.setAdd(false);
			}
			controller.getSelectedDocument().setName(view.getName());
			controller.getSelectedDocument().setDescription(view.getDescription());
			controller.getSelectedDocument().setRspProjectFileName(view.getRecostarExtraction());
			controller.getSelectedDocument().setMinConfidenceThreshold(Float.parseFloat(view.getConfidence()));
			controller.getSelectedDocument().setHidden(view.isHidden());
			controller.getMainPresenter().getDocumentTypeViewPresenter().bind();
			controller.getMainPresenter().getDocumentTypeViewPresenter().showDocumentTypeView();
			if (controller.getSelectedDocument().isNew()) {
				controller.getMainPresenter().getBatchClassBreadCrumbPresenter().createBreadCrumb(controller.getSelectedDocument());
			}
		}
	}

	@Override
	public void bind() {
		if (controller.getSelectedDocument() != null) {
			view.setDescription(controller.getSelectedDocument().getDescription());

			controller.getRpcService().getProjectFilesForDocumentType(controller.getBatchClass().getIdentifier(),
					controller.getSelectedDocument().getName(), new AsyncCallback<List<String>>() {

						@Override
						public void onFailure(Throwable arg0) {
							ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.UNABLE_TO_RETRIEVE_FIELDS);

						}

						@Override
						public void onSuccess(List<String> rspFileNameList) {
							view.setRecostarExtraction(controller.getSelectedDocument().getRspProjectFileName(), rspFileNameList);
						}
					});

			view.setName(controller.getSelectedDocument().getName());
			view.setConfidence(String.valueOf(controller.getSelectedDocument().getMinConfidenceThreshold()));
			view.getNameTextBox().setReadOnly(!controller.getSelectedDocument().isNew());
			view.setHidden(controller.getSelectedDocument().isHidden());
			view.getValidateNameTextBox().addValidator(new EmptyStringValidator(view.getNameTextBox()));
			view.getValidateConfidenceTextBox().addValidator(new EmptyStringValidator(view.getConfidenceTextBox()));
			view.getValidateDescriptionTextBox().addValidator(new EmptyStringValidator(view.getDescriptionTextBox()));
			view.getValidateConfidenceTextBox().addValidator(new NumberValidator(view.getConfidenceTextBox(), true));

			view.getValidateNameTextBox().toggleValidDateBox();
			view.getValidateConfidenceTextBox().toggleValidDateBox();
			view.getValidateDescriptionTextBox().toggleValidDateBox();
		}
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		// Event handling is done here.
	}

}
