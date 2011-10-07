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

import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.MessageConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.documenttype.CopyDocumentView;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.DocumentTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CopyDocumentTypePresenter extends AbstractBatchClassPresenter<CopyDocumentView> {

	/**
	 * The batch class that is to being copied.
	 */
	private DocumentTypeDTO documentTypeDTO;

	public CopyDocumentTypePresenter(final BatchClassManagementController controller, final CopyDocumentView view) {
		super(controller, view);
	}

	@Override
	public void bind() {
		if (documentTypeDTO != null) {
			view.setName(documentTypeDTO.getName());
			view.setDescription(documentTypeDTO.getDescription());
			view.setMinConfidenceThreshold(String.valueOf(documentTypeDTO.getMinConfidenceThreshold()));
		}
	}

	public void showDocumentCopyView() {
		view.getDialogBox().setWidth("100%");
		view.getDialogBox().center();
		view.getDialogBox().add(view);
		view.getDialogBox().show();
		view.getDialogBox().setText(MessageConstants.DOCUMENT_TYPE_COPY);
	}

	@Override
	public void injectEvents(final HandlerManager eventBus) {
		// Event handling to be done here.
	}

	public DocumentTypeDTO getDocumentTypeDTO() {
		return documentTypeDTO;
	}

	public void setDocumentTypeDTO(DocumentTypeDTO documentTypeDTO) {
		this.documentTypeDTO = documentTypeDTO;
	}

	public void onOkClicked() {
		ScreenMaskUtility.maskScreen();
		boolean validCheck = true;
		if (validCheck && (!view.getValidateNameTextBox().validate() || !view.getValidateDescriptionTextBox().validate())) {
			ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.MANDATORY_FIELDS_ERROR_MSG);
			validCheck = false;
			ScreenMaskUtility.unmaskScreen();

		}
		if (validCheck && !view.getValidatePriorityTextBox().validate()) {
			if (view.getMinConfidenceThreshold() != null && !view.getMinConfidenceThreshold().isEmpty()) {
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.CONFIDENCE_SHOULD_BE_NUMERIC);
				ScreenMaskUtility.unmaskScreen();
			} else {
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.MANDATORY_FIELDS_ERROR_MSG);
				ScreenMaskUtility.unmaskScreen();
			}
			validCheck = false;
		}
		if (validCheck) {
			for (DocumentTypeDTO documentTypeDTO : controller.getBatchClass().getDocuments()) {
				if (view.getName().equalsIgnoreCase(documentTypeDTO.getName())) {
					ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.DOCUMENT_NAME_SAME_ERROR);
					validCheck = false;
					ScreenMaskUtility.unmaskScreen();
					break;
				}
				if (view.getDescription().equalsIgnoreCase(documentTypeDTO.getDescription())) {
					ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.DOCUMENT_DESCRIPTION_SAME_ERROR);
					validCheck = false;
					ScreenMaskUtility.unmaskScreen();
					break;
				}
			}

		}

		if (validCheck) {
			this.documentTypeDTO.setName(view.getName());
			this.documentTypeDTO.setDescription(view.getDescription());
			this.documentTypeDTO.setMinConfidenceThreshold(Integer.parseInt(view.getMinConfidenceThreshold()));
			controller.getRpcService().copyDocument(documentTypeDTO, new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable arg0) {
					ScreenMaskUtility.unmaskScreen();
					ConfirmationDialog confirmationDialog = new ConfirmationDialog(true);
					confirmationDialog.setText(MessageConstants.COPY_FAILURE);
					confirmationDialog.setMessage(MessageConstants.DOCUMENT_COPY_FAILURE + arg0.getMessage());
					confirmationDialog.addDialogListener(new DialogListener() {

						@Override
						public void onOkClick() {
							controller.getMainPresenter().onEditButtonClicked(controller.getBatchClass().getIdentifier());
							view.getDialogBox().hide(true);
						}

						@Override
						public void onCancelClick() {
							// do nothing.
						}
					});

					confirmationDialog.center();
					confirmationDialog.show();
					confirmationDialog.okButton.setFocus(true);
				}

				@Override
				public void onSuccess(Void arg0) {
					ScreenMaskUtility.unmaskScreen();
					ConfirmationDialog confirmationDialog = new ConfirmationDialog(true);
					confirmationDialog.setText(MessageConstants.COPY_SUCCESSFUL);
					confirmationDialog.setMessage(MessageConstants.DOCUMENT_COPY_CREATED_SUCCESSFULLY);
					confirmationDialog.addDialogListener(new DialogListener() {

						@Override
						public void onOkClick() {
							controller.getMainPresenter().onEditButtonClicked(controller.getBatchClass().getIdentifier());
							view.getDialogBox().hide(true);
						}

						@Override
						public void onCancelClick() {
							// do nothing.
						}
					});

					confirmationDialog.center();
					confirmationDialog.show();
					confirmationDialog.okButton.setFocus(true);
				}

			});
		}

	}
}
