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

import java.util.ArrayList;
import java.util.List;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.MessageConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.batch.ImportBatchClassView;
import com.ephesoft.dcma.gwt.core.client.EphesoftAsyncCallback;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.BatchClassDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchFolderListDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.ImportBatchClassSuperConfig;
import com.ephesoft.dcma.gwt.core.shared.UNCFolderConfig;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.ephesoft.dcma.gwt.core.shared.constants.CoreCommonConstants;
import com.ephesoft.dcma.gwt.core.shared.importtree.Node;
import com.google.gwt.event.shared.HandlerManager;

/**
 * Presenter for Importing a batch class.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class ImportBatchClassPresenter extends AbstractBatchClassPresenter<ImportBatchClassView> {

	/**
	 * The batch class that is to being imported.
	 */
	private BatchFolderListDTO batchFolderListDTO;

	/**
	 * uiConfigList List<Node>.
	 */
	private final List<Node> uiConfigList = new ArrayList<Node>();

	/**
	 * uncFolderConfigList List<UNCFolderConfig>.
	 */
	private List<UNCFolderConfig> uncFolderConfigList;

	/**
	 * Constructor.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view ImportBatchClassView
	 */
	public ImportBatchClassPresenter(final BatchClassManagementController controller, final ImportBatchClassView view) {
		super(controller, view);
	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {
		// no implementation
	}

	/**
	 * To show Batch Class Import View.
	 */
	public void showBatchClassImportView() {
		view.getDialogBox().setWidth("100%");
		view.getDialogBox().add(view);
		view.getDialogBox().center();
		view.getDialogBox().show();
		view.getDialogBox().setText(MessageConstants.BATCH_CLASS_IMPORT);
	}

	/**
	 * For event handling.
	 * 
	 * @param eventBus HandlerManager
	 */
	@Override
	public void injectEvents(final HandlerManager eventBus) {
		// Event handling to be done here.
	}

	/**
	 * To get Batch Folder List DTO.
	 * 
	 * @return BatchFolderListDTO
	 */
	public BatchFolderListDTO getBatchFolderListDTO() {
		return batchFolderListDTO;
	}

	/**
	 * To set Batch Folder List DTO.
	 * 
	 * @param batchFolderListDTO BatchFolderListDTO
	 */
	public void setBatchFolderListDTO(BatchFolderListDTO batchFolderListDTO) {
		this.batchFolderListDTO = batchFolderListDTO;
	}

	/**
	 * To get UI Configuration List.
	 * 
	 * @return List<Node>
	 */
	public List<Node> getUiConfigList() {
		return uiConfigList;
	}

	/**
	 * In case of submit complete.
	 */
	public void onSubmitComplete() {
		ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(
				MessageConstants.BATCH_CLASS_IMPORTED_SUCCESSFULLY, MessageConstants.IMPORT_SUCCESSFUL, Boolean.TRUE);

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

	/**
	 * To perform operations in case of OK clicked.
	 * 
	 * @param isUseSource boolean
	 * @param isImportExisting boolean
	 */
	public void onOkClicked(final boolean isUseSource, final boolean isImportExisting) {
		boolean validCheck = true;
		if (!isUseSource) {
			boolean isPriorityNumber = isNumber(view.getPriority());

			if (validCheck
					&& (!view.getValidateTextBox().validate() || !view.getValidateDescTextBox().validate() || !view
							.getValidateNameTextBox().validate())) {
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.MANDATORY_FIELDS_ERROR_MSG);
				ScreenMaskUtility.unmaskScreen();
				validCheck = false;
			}
			if (validCheck && !isPriorityNumber) {
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.PRIORITY_SHOULD_BE_NUMERIC);
				ScreenMaskUtility.unmaskScreen();
				validCheck = false;
			}
			if (validCheck
					&& (Integer.parseInt(view.getPriority()) < AdminConstants.PRIORITY_LOWER_LIMIT || Integer.parseInt(view
							.getPriority()) > AdminConstants.PRIORITY_UPPER_LIMIT)) {
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.PRIORITY_SHOULD_BE_BETWEEN_1_AND_100);
				ScreenMaskUtility.unmaskScreen();
				validCheck = false;
			}

			if (validCheck
					&& (view.getName().indexOf(BatchClassManagementConstants.SPACE) > -1 || view.getName().indexOf(
							BatchClassManagementConstants.HYPHEN) > -1)) {
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.WORKFLOW_IMPROPER_CHARACTER);
				ScreenMaskUtility.unmaskScreen();
				validCheck = false;
			}
		}

		if (validCheck && !isImportExisting && !view.getValidateUNCTextBox().validate()) {
			ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.MANDATORY_FIELDS_ERROR_MSG);
			ScreenMaskUtility.unmaskScreen();
			validCheck = false;
		}

		if (validCheck && !view.getValidateSystemFolderTextBox().validate()) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.INVALID_SYSTEM_FOLDER_PATH)
					+ " \" "
					+ CoreCommonConstants.INVALID_FILE_EXTENSIONS
					+ " \". "
					+ LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.CHANGE_AND_TRY_AGAIN));
			ScreenMaskUtility.unmaskScreen();
			validCheck = false;
		}
		if (validCheck) {
			controller.getRpcService().getAllBatchClassesIncludingDeleted(new EphesoftAsyncCallback<List<BatchClassDTO>>() {

				@Override
				public void customFailure(final Throwable arg0) {
					ScreenMaskUtility.unmaskScreen();
					ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.UNC_PATH_NOT_VERIFIED);
				}

				@Override
				public void onSuccess(final List<BatchClassDTO> batches) {
					boolean isUNCPathUnique = true;
					boolean isNameExistInDatabase = false;
					// Perform validation on batchClass UNC path (should not exist in Database)
					for (BatchClassDTO batchDTO : batches) {
						if (batchDTO.getUncFolder().equalsIgnoreCase(view.getUncFolder())) {
							isUNCPathUnique = false;
						}

						// Perform validation on batchClass "Name" (Should exist in database)
						if (isNameExistInDatabase || batchDTO.getName().equalsIgnoreCase(view.getName())) {
							isNameExistInDatabase = true;
						}
					}
					final boolean isNameExistInDatabaseFinal = isNameExistInDatabase;

					if (!isImportExisting) {
						// new batch class
						// UNC path should be unique
						if (!isUNCPathUnique) {
							ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.UNC_PATH_NOT_UNIQUE);
							ScreenMaskUtility.unmaskScreen();
							return;
						}
						if (isNameExistInDatabaseFinal) {
							ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.BATCH_CLASS_NAME_NOT_UNIQUE);
							ScreenMaskUtility.unmaskScreen();
							return;
						}
					}
					saveBatchClass();
				}
			});
		} else if (validCheck && isImportExisting) {
			saveBatchClass();
		}

	}

	private void saveBatchClass() {
		view.getImportBatchClassUserOptionDTO().setName(view.getName());
		controller.getRpcService().importBatchClass(view.getImportBatchClassUserOptionDTO(), new EphesoftAsyncCallback<Boolean>() {

			@Override
			public void onSuccess(final Boolean isSuccess) {
				ScreenMaskUtility.unmaskScreen();
				if (isSuccess) {
					ConfirmationDialogUtil.showConfirmationDialog(MessageConstants.BATCH_CLASS_IMPORTED_SUCCESSFULLY,
							MessageConstants.BATCH_CLASS_IMPORT, Boolean.TRUE);
					controller.getMainPresenter().init();
				} else {
					ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.IMPORT_UNSUCCESSFUL);
				}
				view.getDialogBox().hide(true);
			}

			@Override
			public void customFailure(final Throwable arg0) {
				ScreenMaskUtility.unmaskScreen();
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.IMPORT_UNSUCCESSFUL);
				view.getDialogBox().hide(true);
			}

		});
	}

	/**
	 * To set Folder List.
	 */
	public void setFolderList() {
		view.getbatchFolderListView(uiConfigList);
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

	/**
	 * In case of complete attachment.
	 * 
	 * @param workFlowName String
	 * @param lastAttachedZipSourcePath String
	 */
	public void onAttachComplete(final String workFlowName, final String lastAttachedZipSourcePath) {

		controller.getRpcService().getImportBatchClassUIConfig(workFlowName, lastAttachedZipSourcePath,
				new EphesoftAsyncCallback<ImportBatchClassSuperConfig>() {

					@Override
					public void customFailure(Throwable arg0) {
						ConfirmationDialogUtil.showConfirmationDialogError(arg0.getMessage());
					}

					@Override
					public void onSuccess(ImportBatchClassSuperConfig importBatchClassSuperConfig) {
						if (importBatchClassSuperConfig == null) {
							view.getDialogBox().hide();
							ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.IMPORT_UNSUCCESSFUL);
							return;
						}
						List<UNCFolderConfig> uncFolderConfigList = importBatchClassSuperConfig.getUncFolderConfigList();
						view.setUNCFolderList(uncFolderConfigList);
						if (uncFolderConfigList == null || uncFolderConfigList.isEmpty()) {
							view.toggleUseExistingState(false);
						}
						setUncFolderConfigList(uncFolderConfigList);
						// if (!view.getImportBatchClassUserOptionDTO().isWorkflowEqual()
						// || !view.getImportBatchClassUserOptionDTO().isWorkflowExistsInBatchClass()) {
						if (!view.getImportBatchClassUserOptionDTO().isWorkflowEqual()) {
							// view.toggleUseExistingState(false);
							view.setErrorText(MessageConstants.WORKFLOWNAME_EXISTS);
						} else {
							view.toggleUseExistingState(true);
							view.setErrorText(BatchClassManagementConstants.EMPTY_STRING);
						}
						uiConfigList.clear();
						uiConfigList.addAll(importBatchClassSuperConfig.getUiConfigRoot().getChildren());
						setFolderList();
					}
				});
	}

	/**
	 * In case of deletion of attached folders.
	 * 
	 * @param lastAttachedZipSourcePath String
	 */
	public void deleteAttachedFolders(final String lastAttachedZipSourcePath) {
		controller.getRpcService().deleteAttachedFolders(lastAttachedZipSourcePath, new EphesoftAsyncCallback<Void>() {

			@Override
			public void customFailure(Throwable arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(Void arg0) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void setUncFolderConfigList(final List<UNCFolderConfig> uncFolderConfigList) {
		this.uncFolderConfigList = uncFolderConfigList;
	}

	/**
	 * To get Selected Batch Name.
	 * 
	 * @param selectedUNCFolder String
	 * @return String
	 */
	public String getSelectedBatchName(final String selectedUNCFolder) {
		String batchClassName = null;
		if (uncFolderConfigList != null) {
			for (UNCFolderConfig uncFolderConfig : uncFolderConfigList) {
				String uncFolder = uncFolderConfig.getUncFolder();
				if (uncFolder.equalsIgnoreCase(selectedUNCFolder)) {
					batchClassName = uncFolderConfig.getBatchClassName();
					break;
				}
			}
		}
		return batchClassName;
	}

	/**
	 * To get selected Batch Class System Folder.
	 * 
	 * @param selectedBatchName String
	 * @return String
	 */
	public String getSelectedBatchClassSystemFolder(final String selectedBatchName) {
		String systemFolderName = null;
		final List<BatchClassDTO> batchClassList = controller.getBatchClassList();
		if (null != batchClassList) {
			for (BatchClassDTO batchClassDTO : batchClassList) {
				if (null != batchClassDTO && batchClassDTO.getName().equals(selectedBatchName)) {
					systemFolderName = batchClassDTO.getSystemFolder();
				}
			}
		}
		return systemFolderName;
	}

}
