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

package com.ephesoft.dcma.gwt.uploadbatch.client.presenter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.core.common.UserType;
import com.ephesoft.dcma.gwt.core.client.EphesoftAsyncCallback;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.BatchClassCloudConfigDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.uploadbatch.client.UploadBatchController;
import com.ephesoft.dcma.gwt.uploadbatch.client.i18n.UploadBatchConstants;
import com.ephesoft.dcma.gwt.uploadbatch.client.i18n.UploadBatchMessages;
import com.ephesoft.dcma.gwt.uploadbatch.client.view.UploadBatchView;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;

public class UploadBatchPresenter extends AbstractUploadBatchPresenter<UploadBatchView> {

	private AssociateBCFPresenter associateBCFPresenter;
	
	/**
	 * The batchClassLimitMap {@link Map} is used for storing page count and instance 
	 * limit per batch class.
	 */
	private Map<String, BatchClassCloudConfigDTO> batchClassLimitMap = new HashMap<String, BatchClassCloudConfigDTO>();
	
	/**
	 * The userType {@link Integer} is used for storing user type.
	 */
	private Integer userType;
	
	/**
	 * The fileSizeLimit {@link Long} is used for storing batch class limit allowed
	 * for each file uploaded.
	 */
	private Long fileSizeLimit = 0L;

	public UploadBatchPresenter(UploadBatchController controller, UploadBatchView view) {
		super(controller, view);
	}

	public void bind() {
		setUserType();
		setFileSizeLimit();
		getBatchClassName();
		getCurrentBatchUploadFolder();
		setBatchClassLimitMap();
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		/*
		 * 
		 */
	}

	public AssociateBCFPresenter getAssociateBCFPresenter() {
		return associateBCFPresenter;
	}

	public void onAssociateBatchClassFieldButtonClicked(String batchClassId) {
		associateBCFPresenter.setBatchClassFieldDTOs(batchClassId, false);
	}

	public void getBatchClassName() {
		controller.getRpcService().getBatchClassName(new EphesoftAsyncCallback<Map<String, String>>() {

			@Override
			public void customFailure(Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						UploadBatchMessages.ERROR_GETTING_BATCH_CLASS_LIST));
			}

			@Override
			public void onSuccess(final Map<String, String> arg0) {
				view.setBatchClassMapping(arg0);
				controller.getRpcService().getBatchClassInfoFromSession(new EphesoftAsyncCallback<String>() {

					@Override
					public void customFailure(Throwable throwable) {
						populateBatchClassListBox(arg0,	null);
					}

					@Override
					public void onSuccess(String selectedBatchClassIdentifier) {
						populateBatchClassListBox(arg0,
								selectedBatchClassIdentifier);
						
					}
				});
				associateBCFPresenter = new AssociateBCFPresenter(controller, view.getAssociateBCFView());
			}
		});
	}

	public void resetCurrentBatchUploadFolderOnServer() {
		associateBCFPresenter.clearBatchClassFieldValues();
		controller.getRpcService().resetCurrentBatchUploadFolder(controller.getCurrentBatchUploadFolder(), new EphesoftAsyncCallback<Void>() {

			@Override
			public void customFailure(Throwable arg0) {
				/*
				 * 
				 */
			}

			@Override
			public void onSuccess(Void arg0) {
				/*
				 * 
				 */
			}
		});
	}

	public void onFinishButtonClicked() {
		ScreenMaskUtility.maskScreen();
		final String identifier = view.getSelectedBatchClassNameListBoxValue();
		controller.getRpcService().finishBatch(controller.getCurrentBatchUploadFolder(), identifier,
				new EphesoftAsyncCallback<String>() {

					@Override
					public void customFailure(Throwable throwable) {
						final String errorMessage = throwable.getMessage();
						
						// For batch class instance page count error
						if (UploadBatchConstants.IMAGE_ERROR.equals(errorMessage)) {
							 final String message = LocaleDictionary.get().getMessageValue(
									UploadBatchMessages.UPLOAD_IMAGE_LIMIT_ERROR) + getBatchClassImageLimit(identifier)
									+ UploadBatchConstants.SPACE + LocaleDictionary.get().getMessageValue(
											UploadBatchMessages.UPLOAD_INSTANCE_LIMIT_APPENDED_MESSAGE);
							final String title  = LocaleDictionary.get().getMessageValue(
									UploadBatchMessages.LIMIT_REACHED);
							ConfirmationDialogUtil.showConfirmationDialog(message, title, true);
							ScreenMaskUtility.unmaskScreen();
						} else if (UploadBatchConstants.INSTANCE_ERROR.equals(errorMessage)) {// For batch class instance limit error
							final String message = LocaleDictionary.get().getMessageValue(
									UploadBatchMessages.UPLOAD_INSTANCE_LIMIT_ERROR);
							final String title  = LocaleDictionary.get().getMessageValue(
									UploadBatchMessages.LIMIT_REACHED);
							ConfirmationDialogUtil.showConfirmationDialog(message, title, true);
							ScreenMaskUtility.unmaskScreen();
							onLimitError();
							batchClassLimitMap.clear();
							setBatchClassLimitMap();
						} else {
							ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
									UploadBatchMessages.BATCH_PROCESS_FAIL)
									+ " " + errorMessage);
							ScreenMaskUtility.unmaskScreen();
						}
						
					}

					@Override
					public void onSuccess(String arg0) {
						controller.setCurrentBatchUploadFolder("");
						ConfirmationDialogUtil.showConfirmationDialogSuccess(LocaleDictionary.get().getMessageValue(
								UploadBatchMessages.FINISH_UPLOAD_ALERT));
						ScreenMaskUtility.unmaskScreen();
						associateBCFPresenter.clearBatchClassFieldValues();
						onResetButtonClick();
						batchClassLimitMap.clear();
						setBatchClassLimitMap();
					}
				});
	}

	public void onResetButtonClick() {
		String currentBatchUploadFolder = controller.getCurrentBatchUploadFolder();
		if (currentBatchUploadFolder != null && !currentBatchUploadFolder.isEmpty()) {
			resetCurrentBatchUploadFolderOnServer();
			controller.setCurrentBatchUploadFolder("");
		}
		// view.getUploadBatchFilePanel().reset();
		view.getAllFileList().clear();
		view.getDeleteSelectedFileList().clear();

		// disable finish button
		view.getFinishButton().setEnabled(Boolean.FALSE);
		// disable Field(s) button
		view.getAddBCFButton().setEnabled(Boolean.FALSE);
		// disable delete button
		view.getDeleteButton().setEnabled(Boolean.FALSE);
		view.getDeleteButton().setVisible(Boolean.FALSE);
		view.getDeleteCaptionPanel().setVisible(Boolean.FALSE);

		if (view.getBatchClassNameListBox().getItemCount() > 0) {
			view.getBatchClassNameListBox().setSelectedIndex(0);
		}
		// reset the batch class field DTO's
		associateBCFPresenter.setBatchClassFieldDTOs(view.getSelectedBatchClassNameListBoxValue(), true);
		
		repaintFileList();
	}

	public void repaintFileList() {
		view.getAllFilesBox().clear();
		view.getEditTable().removeAllRows();
		view.getDeleteCaptionPanel().setVisible(Boolean.FALSE);
		view.getSelectAllCell().clear();

		if (!view.getAllFileList().isEmpty()) {
			// view.formatRow(0);

			CheckBox deleteAllBox = new CheckBox();
			view.setDeleteAllBox(deleteAllBox);
			deleteAllBox.setStyleName("paddingSelectAll");
			deleteAllBox.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					boolean checked = ((CheckBox) event.getSource()).getValue();
					if (checked) {
						// toggle all the checkboxes to select
						view.toggleAllCheckBoxes(Boolean.TRUE);
						view.getDeleteSelectedFileList().addAll(view.getAllFileList());
					} else {
						// toggle all the checkboxes to deselect
						view.toggleAllCheckBoxes(Boolean.FALSE);

						view.getDeleteSelectedFileList().clear();
					}

				}
			});

			view.getSelectAllCell().add(deleteAllBox);
			deleteAllBox.setStyleName("paddingSelectAllLabel");
			Label label = new Label(LocaleDictionary.get().getConstantValue(UploadBatchConstants.SELECT_ALL));
			label.setWidth("25em");
			label.addStyleName("bold_text");
			view.getSelectAllCell().add(label);
			

			int row = 0;
			for (Iterator<String> fileListIterator = view.getAllFileList().iterator(); fileListIterator.hasNext();) {
				final String fileName = (String) fileListIterator.next();
				view.formatRow(row);

				CheckBox deleteBox = new CheckBox();
				view.getAllFilesBox().add(deleteBox);
				deleteBox.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						boolean checked = ((CheckBox) event.getSource()).getValue();
						if (checked) {
							view.getDeleteSelectedFileList().add(fileName);
						} else {
							// uncheck the select all check box
							view.getDeleteAllBox().setValue(Boolean.FALSE);

							view.getDeleteSelectedFileList().remove(fileName);
						}
					}
				});
				
				view.addWidget(row, 0, deleteBox);
				view.addWidget(row, 1, new Label(fileName));
				HTMLPanel progressPanel = new HTMLPanel("<span id=\"progressBar_" + row +"\"></span>");
				view.addWidget(row, 2, progressPanel);
				
				if(row != 0 || row < view.getAllFileList().size()) {
					String innerHTML = LocaleDictionary.get().getConstantValue(
							UploadBatchConstants.UPLOAD_PROGRESS) + "100" + "%";
					DOM.getElementById("progressBar_" + row).setInnerHTML(innerHTML);
				}
				row++;
			}

			view.getDeleteCaptionPanel().setVisible(Boolean.TRUE);
		}
	}

	public void onDeleteButtonClicked() {
		if (view.getDeleteSelectedFileList().isEmpty()) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					UploadBatchMessages.NONE_FILE_TO_DELETE_SELECTED_WARNING));
			return;
		}
		ScreenMaskUtility.maskScreen();
		controller.getRpcService().deleteFilesByName(controller.getCurrentBatchUploadFolder(), view.getDeleteSelectedFileList(),
				new EphesoftAsyncCallback<List<String>>() {

					@Override
					public void customFailure(Throwable arg0) {
						ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
								UploadBatchMessages.ERROR_DELETING_FILES));
						ScreenMaskUtility.unmaskScreen();
					};

					@Override
					public void onSuccess(List<String> filesNotDeleted) {
						view.getAllFileList().removeAll(view.getDeleteSelectedFileList());
						view.getDeleteSelectedFileList().clear();
						view.getEditTable().removeAllRows();
						if (filesNotDeleted != null && !filesNotDeleted.isEmpty()) {
							view.getAllFileList().addAll(filesNotDeleted);
							ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
									UploadBatchMessages.FEW_FILES_NOT_DELETED));
						} else {
							ConfirmationDialogUtil.showConfirmationDialogSuccess(LocaleDictionary.get().getMessageValue(
									UploadBatchMessages.SUCCESS_DELETING_FILES));
						}
						repaintFileList();
						if (view.getAllFileList().isEmpty()) {
							view.getFinishButton().setEnabled(Boolean.FALSE);
							view.getDeleteButton().setEnabled(Boolean.FALSE);
							view.getDeleteButton().setVisible(Boolean.FALSE);
							view.getDeleteCaptionPanel().setVisible(Boolean.FALSE);

							view.getAddBCFButton().setEnabled(Boolean.FALSE);
						}
						ScreenMaskUtility.unmaskScreen();
					};
				});
	}

	public void onSubmit(final String uploadFormAction) {

		String currentBatchUploadFolder = controller.getCurrentBatchUploadFolder();
		if (currentBatchUploadFolder == null || currentBatchUploadFolder.isEmpty()) {
			// rpc call for fetching the folder name
			controller.getRpcService().getCurrentBatchFolderName(new EphesoftAsyncCallback<String>() {

				@Override
				public void onSuccess(String currentBatchUploadFolder) {
					setAndStartUpload(uploadFormAction, currentBatchUploadFolder);
					ScreenMaskUtility.unmaskScreen();
				}

				@Override
				public void customFailure(Throwable arg0) {
					/*
					 * 
					 */
				}
			});
		} else {
			setAndStartUpload(uploadFormAction, currentBatchUploadFolder);
		}
	}

	private void setAndStartUpload(final String uploadFormAction, String currentBatchUploadFolder) {
		controller.setCurrentBatchUploadFolder(currentBatchUploadFolder);
		String currentBatchUploadFolderName = "currentBatchUploadFolderName=" + controller.getCurrentBatchUploadFolder();
		view.getSwfUpload().setUploadURL(uploadFormAction + currentBatchUploadFolderName);
		view.getSwfUpload().startUpload();
	}

	private void getCurrentBatchUploadFolder() {
		// rpc call for fetching the folder name
		controller.getRpcService().getCurrentBatchFolderName(new EphesoftAsyncCallback<String>() {

			@Override
			public void onSuccess(String currentBatchUploadFolder) {
				controller.setCurrentBatchUploadFolder(currentBatchUploadFolder);
			}

			@Override
			public void customFailure(Throwable arg0) {
				/*
				 * 
				 */

			}
		});
	}
	
	private void populateBatchClassListBox(final Map<String, String> arg0,
			String selectedBatchClassIdentifier) {
		if (view.getBatchClassNameListBox().getItemCount() <= 0) {
			view.setBatchClassNameListBoxValues(arg0, selectedBatchClassIdentifier);
		}
	}
	
	public void setSelectedBatchClassInfoToSession(String selectedBatchClassInfo) {
		controller.getRpcService().setBatchClassInfoFromSession(selectedBatchClassInfo, new EphesoftAsyncCallback<Void>() {

			@Override
			public void customFailure(Throwable arg0) {
				/*
				 * 
				 */
			}

			@Override
			public void onSuccess(Void arg0) {
				//Empty method
			}
		});
	}
	
	/**
	 * The <code>getBatchClassImageLimit</code> is a method for retrieving instance limit
	 * for a given batch class.
	 * 
	 * @param identifier {@link String} batch class identifier.
	 * @return {@link Integer} instance limit
	 */
	public Integer getBatchClassImageLimit(String identifier) {
		Integer imageLimit = null;
		if (null != batchClassLimitMap && !batchClassLimitMap.isEmpty() && batchClassLimitMap.containsKey(identifier)) {
			BatchClassCloudConfigDTO batchClassCloudConfigDTO = batchClassLimitMap.get(identifier);
			if (null != batchClassCloudConfigDTO) {
				Integer batchInstanceImageLimit = batchClassCloudConfigDTO.getBatchInstanceImageLimit();
				if (null != batchInstanceImageLimit && null != userType && userType.intValue() == UserType.LIMITED.getUserType()) {
					imageLimit = batchInstanceImageLimit;
				}
			}
		}
		return imageLimit;
	}
	
	
	
	/**
	 * The <code>setBatchClassLimitMap</code> is a method for setting batch class instance limit
	 * map for validating number of images allowed to upload.
	 */
	private void setBatchClassLimitMap() {
		controller.getRpcService().getBatchClassImageLimit(new EphesoftAsyncCallback<Map<String,BatchClassCloudConfigDTO>>() {

			@Override
			public void customFailure(Throwable arg0) {
				//Do Nothing
			}

			@Override
			public void onSuccess(Map<String, BatchClassCloudConfigDTO> batchClassImageLimitMap) {
				if (null != batchClassImageLimitMap && !batchClassImageLimitMap.isEmpty()) {
					batchClassLimitMap.putAll(batchClassImageLimitMap);
				}
			}

		});		
	}
	
	/**
	 * The <code>setUserType</code> method is used for setting current 
	 * user type.
	 */
	private void setUserType() {
		controller.getRpcService().getUserType(new EphesoftAsyncCallback<Integer>() {

			@Override
			public void customFailure(Throwable arg0) {
				// Do nothing
			}

			@Override
			public void onSuccess(Integer userType) {
				if (null != userType) {
					setUserType(userType);
				}
			}
		});
	}
	
	/**
	 * The <code>setFileSizeLimit</code> method is used for setting current 
	 * file size limit.
	 */
	private void setFileSizeLimit() {
		controller.getRpcService().getFileSizeLimit(new EphesoftAsyncCallback<Long>() {

			@Override
			public void customFailure(Throwable arg0) {
				// Do Nothing
			}

			@Override
			public void onSuccess(Long fileSize) {
				if (null != fileSizeLimit) {
					fileSizeLimit = fileSize;
				}
			}
		});
	}
	
	
	
	/**
	 * The <code>checkForBatchClassLimit</code> method is used for checking batch class limit
	 * of batch class id passed.
	 * 
	 * @param identifier {@link String} batch class identifier
	 * @return true/false 
	 */
	public boolean checkForBatchClassLimit(String identifier) {
		boolean isUploadLimit = false;
		if (null != identifier && null != batchClassLimitMap && !batchClassLimitMap.isEmpty() && batchClassLimitMap.containsKey(identifier)) {
			BatchClassCloudConfigDTO batchClassCloudConfigDTO = batchClassLimitMap.get(identifier);
			if (null != batchClassCloudConfigDTO) {
				Integer batchInstanceLimit = batchClassCloudConfigDTO.getBatchInstanceLimit();
				Integer batchInstanceCounter = batchClassCloudConfigDTO.getBatchInstanceCounter();
				isUploadLimit = (null != batchInstanceLimit && null != batchInstanceCounter 
											&& null != userType && userType.intValue() == UserType.LIMITED.getUserType()) ? 
													batchInstanceCounter.intValue() >= batchInstanceLimit.intValue() : false;
			}
		}
		return isUploadLimit;
	}
	
	/**
	 * The <code>onLimitError</code> method is used for repainting file view
	 * if batch class limit error.
	 */
	private void onLimitError() {
		view.getAllFileList().clear();
		view.getDeleteSelectedFileList().clear();

		// disable finish button
		view.getFinishButton().setEnabled(Boolean.FALSE);
		
		// disable Field(s) button
		view.getAddBCFButton().setEnabled(Boolean.FALSE);
		
		// disable delete button
		view.getDeleteButton().setEnabled(Boolean.FALSE);
		view.getDeleteButton().setVisible(Boolean.FALSE);
		view.getDeleteCaptionPanel().setVisible(Boolean.FALSE);
		
		repaintFileList();
	}
	
	/**
	 * Getter for user type.
	 * 
	 * @return {@link Integer} user type
	 */
	public Integer getUserType() {
		return userType;
	}

	/**
	 * Setter for user type.
	 * 
	 * @param userType {@link Integer} user type
	 */
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	
	/**
	 * Getter for file size limit.
	 * 
	 * @return {@link Long} file size limit
	 */
	public Long getFileSizeLimit() {
		return fileSizeLimit;
	}

}
