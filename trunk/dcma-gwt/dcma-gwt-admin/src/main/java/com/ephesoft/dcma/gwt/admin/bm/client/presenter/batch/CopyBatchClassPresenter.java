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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.batch;

import java.util.List;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.MessageConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.batch.CopyBatchClassView;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.EphesoftAsyncCallback;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.BatchClassDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.google.gwt.event.shared.HandlerManager;

/**
 * Presenter for copying a batch class.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class CopyBatchClassPresenter extends AbstractBatchClassPresenter<CopyBatchClassView> {

	/**
	 * The batch class that is to being copied.
	 */
	private BatchClassDTO batchClassDTO;

	/**
	 * Constructor.
	 * @param controller BatchClassManagementController
	 * @param view CopyBatchClassView
	 */
	public CopyBatchClassPresenter(final BatchClassManagementController controller, final CopyBatchClassView view) {
		super(controller, view);
	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {
		if (batchClassDTO != null) {
			view.setName(batchClassDTO.getName());
			view.setUncFolder(batchClassDTO.getUncFolder());
			view.setPriority(batchClassDTO.getPriority());
			view.setDescription(batchClassDTO.getDescription());
			view.setSystemFolder(batchClassDTO.getSystemFolder());
		}
	}

	/**
	 * To show Batch Class Copy View.
	 */
	public void showBatchClassCopyView() {
		view.getDialogBox().setWidth("100%");
		view.getDialogBox().center();
		view.getDialogBox().add(view);
		view.getDialogBox().show();
		view.getDialogBox().setText(MessageConstants.BATCH_CLASS_COPY);
	}

	/**
	 * For handling event.
	 */
	@Override
	public void injectEvents(final HandlerManager eventBus) {
		// Event handling to be done here.
	}

	/**
	 * To get Batch Class DTO.
	 * @return BatchClassDTO
	 */
	public BatchClassDTO getBatchClassDTO() {
		return batchClassDTO;
	}

	/**
	 * To set Batch Class DTO.
	 * @param batchClassDTO BatchClassDTO
	 */
	public void setBatchClassDTO(final BatchClassDTO batchClassDTO) {
		this.batchClassDTO = batchClassDTO;
	}

	/**
	 * To perform operations on OK click.
	 */
	public void onOkClicked() {
		String batchClassName = view.getName();
		batchClassDTO.setName(batchClassName);
		boolean isPriorityNumber = isNumber(view.getPriority());
		boolean validCheck = validateFields(batchClassName, isPriorityNumber);
		if (validCheck) {
			ScreenMaskUtility.maskScreen();
			controller.getRpcService().getAllBatchClasses(new EphesoftAsyncCallback<List<BatchClassDTO>>() {

				@Override
				public void customFailure(final Throwable arg0) {
					ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.UNC_PATH_NOT_VERIFIED);
					ScreenMaskUtility.unmaskScreen();
				}

				@Override
				public void onSuccess(final List<BatchClassDTO> batches) {
					boolean isUNCPathUnique = true;
					boolean isBatchClassNameUnique = true;
					for (BatchClassDTO batchDTO : batches) {
						// checking whether unc path is unique
						if (batchDTO.getUncFolder().equalsIgnoreCase(view.getUncFolder())) {
							isUNCPathUnique = false;
						}
						// checking whether batch class name is unique
						if (batchDTO.getName().equalsIgnoreCase(view.getName())) {
							isBatchClassNameUnique = false;
						}
					}
					if (!isUNCPathUnique || view.getUncFolder().isEmpty()) {
						ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.UNC_PATH_NOT_UNIQUE);
						ScreenMaskUtility.unmaskScreen();
						return;
					}
					if (!isBatchClassNameUnique || view.getName().isEmpty()) {
						ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.BATCH_CLASS_NAME_NOT_UNIQUE);
						ScreenMaskUtility.unmaskScreen();
						return;
					}
					batchClassDTO.setName(view.getName());
					batchClassDTO.setDescription(view.getDescription());
					batchClassDTO.setPriority(view.getPriority());
					batchClassDTO.setUncFolder(view.getUncFolder());
					batchClassDTO.setSystemFolder(view.getSystemFolder());
					controller.getRpcService().createUncFolder(batchClassDTO.getUncFolder(), new EphesoftAsyncCallback<Void>() {

						@Override
						public void customFailure(final Throwable arg0) {
							ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.UNC_PATH_NOT_VERIFIED);
							ScreenMaskUtility.unmaskScreen();
						}

						@Override
						public void onSuccess(final Void arg0) {
							controller.getRpcService().copyBatchClass(batchClassDTO, new EphesoftAsyncCallback<Void>() {

								@Override
								public void onSuccess(final Void arg0) {
									ScreenMaskUtility.unmaskScreen();

									ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(
											MessageConstants.BATCH_CLASS_COPY_CREATED_SUCCESSFULLY, MessageConstants.COPY_SUCCESSFUL,
											Boolean.TRUE);
									confirmationDialog.addDialogListener(new DialogListener() {

										@Override
										public void onOkClick() {
											controller.getMainPresenter().init();
											view.getDialogBox().hide(true);
										}

										@Override
										public void onCancelClick() {
											// do nothing.
										}
									});

								}

								@Override
								public void customFailure(final Throwable arg0) {
									ScreenMaskUtility.unmaskScreen();
									ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.UNABLE_TO_CREATE_COPY);
									view.getDialogBox().hide(true);
								}
							});
						}
					});

				}

			});

		}

	}

	private boolean validateFields(String batchClassName, boolean isPriorityNumber) {
		boolean validCheck = true;
		if (!view.getValidateTextBox().validate() || !view.getValidateDescTextBox().validate()
				|| !view.getValidateNameTextBox().validate()) {
			ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.MANDATORY_FIELDS_ERROR_MSG);
			validCheck = false;
		}
		if (validCheck && !isPriorityNumber) {
			ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.PRIORITY_SHOULD_BE_NUMERIC);
			validCheck = false;
		}
		if (validCheck
				&& (Integer.parseInt(view.getPriority()) < AdminConstants.PRIORITY_LOWER_LIMIT || Integer.parseInt(view.getPriority()) > AdminConstants.PRIORITY_UPPER_LIMIT)) {
			ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.PRIORITY_SHOULD_BE_BETWEEN_1_AND_100);
			validCheck = false;
		}
		if (validCheck && (batchClassName.contains(BatchClassManagementConstants.SPACE) || batchClassName.contains(BatchClassManagementConstants.HYPHEN))) {
			ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.BATCH_CLASS_NAME_ERROR);
			validCheck = false;
		}
		if (validCheck && !view.getValidateSystemFolderTextBox().validate()) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.INVALID_SYSTEM_FOLDER_PATH)
					+ LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.CHANGE_AND_TRY_AGAIN));
			validCheck = false;
		}
		return validCheck;
	}

	private boolean isNumber(final String priority) {
		boolean returnValue = true;
		try {
			Integer.parseInt(priority);
		} catch (NumberFormatException e) {
			returnValue = false;
		}
		return returnValue;
	}
}
