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

package com.ephesoft.dcma.gwt.foldermanager.client.presenter;

import java.util.ArrayList;
import java.util.List;

import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.FileWrapper;
import com.ephesoft.dcma.gwt.core.client.EphesoftAsyncCallback;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.ephesoft.dcma.gwt.foldermanager.client.FolderManagementController;
import com.ephesoft.dcma.gwt.foldermanager.client.event.BatchClassChangeEvent;
import com.ephesoft.dcma.gwt.foldermanager.client.event.BatchClassChangeEventHandler;
import com.ephesoft.dcma.gwt.foldermanager.client.event.FolderCutEvent;
import com.ephesoft.dcma.gwt.foldermanager.client.event.FolderTableRefreshEvent;
import com.ephesoft.dcma.gwt.foldermanager.client.event.FolderTableRefreshEventHandler;
import com.ephesoft.dcma.gwt.foldermanager.client.event.FolderTreeRefreshEvent;
import com.ephesoft.dcma.gwt.foldermanager.client.event.FolderTreeRefreshEventHandler;
import com.ephesoft.dcma.gwt.foldermanager.client.event.FolderUpEvent;
import com.ephesoft.dcma.gwt.foldermanager.client.event.PathRefreshEvent;
import com.ephesoft.dcma.gwt.foldermanager.client.event.PathRefreshEventHandler;
import com.ephesoft.dcma.gwt.foldermanager.client.view.FolderTableView;
import com.ephesoft.dcma.gwt.foldermanager.client.view.widget.FolderUploadWidget;
import com.ephesoft.dcma.gwt.foldermanager.shared.constant.FolderManagementConstants;
import com.ephesoft.dcma.gwt.foldermanager.shared.constant.FolderManagementMessages;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;

public class FolderTablePresenter extends AbstractFolderManagementPresenter<FolderTableView> {

	private static final String HEIGHT = ",height=";
	private static final String WIDTH = ",width=";
	private static final String OPEN_WINDOW_OPTIONS = "menubar=yes,location=no,resizable=yes,scrollbars=yes,status=no,toolbar=true";
	private FileOperation lastFileOperation;
	private String cutFilesFolderPath;
	private List<String> cutOrCopyFileList;
	private String folderPath;
	private final String parentFolderPath;
	private final String baseHttpURL;
	private FileWrapper selectedFile;

	private enum FileOperation {
		CUT, COPY, PASTE;
	}

	public FolderTablePresenter(FolderManagementController controller, FolderTableView view, String parentFolderPath,
			String baseHttpURL) {
		super(controller, view);
		this.parentFolderPath = parentFolderPath;
		this.baseHttpURL = baseHttpURL;
		view.setFileUploadWidget(new FolderUploadWidget(controller.getEventBus()));
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		eventBus.addHandler(FolderTableRefreshEvent.type, new FolderTableRefreshEventHandler() {

			@Override
			public void refreshTable(FolderTableRefreshEvent tableRefreshEvent) {
				refreshContent(tableRefreshEvent.getFolderPath());

			}
		});

		eventBus.addHandler(FolderTreeRefreshEvent.type, new FolderTreeRefreshEventHandler() {

			@Override
			public void refreshTree(FolderTreeRefreshEvent treeRefreshEvent) {
				String path = treeRefreshEvent.getFolderPath();
				if (path != null) {
					folderPath = path;
					controller.getEventBus().fireEvent(new PathRefreshEvent(folderPath));
				} else {
					refreshContent();
				}
			}

		});

		eventBus.addHandler(PathRefreshEvent.type, new PathRefreshEventHandler() {

			@Override
			public void onFolderPathChange(PathRefreshEvent pathRefreshEvent) {
				folderPath = pathRefreshEvent.getFolderPath();
			}
		});
		eventBus.addHandler(BatchClassChangeEvent.type, new BatchClassChangeEventHandler() {

			@Override
			public void onBatchClassChange(BatchClassChangeEvent batchClassChangeEvent) {
				view.cleanupTableContent();
				if (batchClassChangeEvent.getBatchClassID() == null && batchClassChangeEvent.getBatchClassName() == null) {
					view.addNoContentLabel();
				}
			}
		});
	}

	public void onNewFolderClicked() {
		ScreenMaskUtility.maskScreen(LocaleDictionary.get().getMessageValue(
				FolderManagementMessages.PLEASE_WAIT_WHILE_A_NEW_FOLDER_IS_CREATED));
		controller.getRpcService().createNewFolder(folderPath, new EphesoftAsyncCallback<String>() {

			@Override
			public void customFailure(Throwable error) {

				StringBuffer errorMsg = new StringBuffer(LocaleDictionary.get().getMessageValue(
						FolderManagementMessages.FAILED_TO_CREATE_A_NEW_FOLDER));
				String localizedMessage = error.getLocalizedMessage();
				if (localizedMessage != null && !localizedMessage.isEmpty()) {
					errorMsg.append(localizedMessage);
				}

				showErrorInConfirmationDialog(errorMsg.toString());
				ScreenMaskUtility.unmaskScreen();
			}

			@Override
			public void onSuccess(String newFolderName) {
				final ConfirmationDialog showConfirmationDialogSuccess = ConfirmationDialogUtil
						.showConfirmationDialogSuccess(LocaleDictionary.get().getMessageValue(
								FolderManagementMessages.SUCCESSFULLY_CREATED_THE_NEW_FOLDER, newFolderName));
				showConfirmationDialogSuccess.addDialogListener(new DialogListener() {

					@Override
					public void onOkClick() {
						showConfirmationDialogSuccess.hide();
						controller.getEventBus().fireEvent(new FolderTreeRefreshEvent());
					}

					@Override
					public void onCancelClick() {
						//no need of implementing cancel click
					}
				});
				showConfirmationDialogSuccess.okButton.setFocus(true);
				ScreenMaskUtility.unmaskScreen();
			}
		});

	}

	public void onPasteClicked() {
		if (cutOrCopyFileList != null && !cutOrCopyFileList.isEmpty()) {
			switch (lastFileOperation) {
				case COPY:
					ScreenMaskUtility.maskScreen(LocaleDictionary.get().getMessageValue(FolderManagementMessages.PASTING));
					controller.getRpcService().copyFiles(cutOrCopyFileList, folderPath, new EphesoftAsyncCallback<Boolean>() {

						@Override
						public void customFailure(Throwable caught) {
							String localizedMessage = caught.getLocalizedMessage();
							if (localizedMessage != null && !localizedMessage.isEmpty()) {
								showErrorInConfirmationDialog(localizedMessage);
							} else {
								showErrorInConfirmationDialog(LocaleDictionary.get().getMessageValue(
										FolderManagementMessages.UNABLE_TO_COMPLETE_COPY_PASTE_OPERATION));
							}
							ScreenMaskUtility.unmaskScreen();
						}

						@Override
						public void onSuccess(Boolean result) {
							ScreenMaskUtility.unmaskScreen();
							controller.getEventBus().fireEvent(new FolderTreeRefreshEvent());
						}

					});
					break;
				case CUT:
					view.setPasteEnabled(false);
					ScreenMaskUtility.maskScreen(FolderManagementMessages.PASTING);
					lastFileOperation = FileOperation.PASTE;
					controller.getRpcService().cutFiles(cutOrCopyFileList, folderPath, new EphesoftAsyncCallback<Boolean>() {

						@Override
						public void customFailure(Throwable caught) {
							String localizedMessage = caught.getLocalizedMessage();
							if (localizedMessage != null && !localizedMessage.isEmpty()) {
								showErrorInConfirmationDialog(localizedMessage);
							} else {
								showErrorInConfirmationDialog(LocaleDictionary.get().getMessageValue(
										FolderManagementMessages.UNABLE_TO_COMPLETE_CUT_PASTE_OPERATION));
							}
							ScreenMaskUtility.unmaskScreen();
						}

						@Override
						public void onSuccess(Boolean result) {
							ScreenMaskUtility.unmaskScreen();
							controller.getEventBus().fireEvent(new FolderCutEvent(cutOrCopyFileList, cutFilesFolderPath));
							controller.getEventBus().fireEvent(new FolderTreeRefreshEvent());
						}

					});
					break;
				default:
					break;
			}
		}
	}

	@Override
	public void bind() {
		//no implementation needed in this presenter
	}

	public void onCutClicked() {
		cutOrCopyFileList = view.getSelectedFileList();
		if (cutOrCopyFileList.isEmpty()) {
			showErrorInConfirmationDialog(LocaleDictionary.get().getMessageValue(
					FolderManagementMessages.NO_FILES_FOLDERS_SELECTED_FOR_OPERATION,
					LocaleDictionary.get().getConstantValue(FolderManagementConstants.CUT)));

		} else {
			view.setPasteEnabled(true);
			lastFileOperation = FileOperation.CUT;
			cutFilesFolderPath = folderPath;
		}

	}

	public void onCopyClicked() {
		cutOrCopyFileList = view.getSelectedFileList();
		if (cutOrCopyFileList.isEmpty()) {
			showErrorInConfirmationDialog(LocaleDictionary.get().getMessageValue(
					FolderManagementMessages.NO_FILES_FOLDERS_SELECTED_FOR_OPERATION,
					LocaleDictionary.get().getConstantValue(FolderManagementConstants.COPY)));
		} else {
			view.setPasteEnabled(true);
			lastFileOperation = FileOperation.COPY;
		}

	}

	public void onDeleteClicked() {

		final List<String> deleteFileList = view.getSelectedFileList();
		if (deleteFileList.isEmpty()) {
			showErrorInConfirmationDialog(LocaleDictionary.get().getMessageValue(
					FolderManagementMessages.NO_FILES_FOLDERS_SELECTED_FOR_OPERATION,
					LocaleDictionary.get().getConstantValue(FolderManagementConstants.DELETE)));
		} else {
			final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get()
					.getMessageValue(FolderManagementMessages.ARE_YOU_SURE_YOU_WANT_TO_DELETE_ALL_THE_SELECTED_FILES),
					LocaleDictionary.get().getMessageValue(FolderManagementMessages.CONFIRM_DELETE_OPERATION), false, true);
			confirmationDialog.addDialogListener(new DialogListener() {

				@Override
				public void onOkClick() {
					if (!deleteFileList.isEmpty()) {
						ScreenMaskUtility.maskScreen(LocaleDictionary.get().getMessageValue(
								FolderManagementMessages.PLEASE_WAIT_WHILE_THE_SELECTED_ITEMS_ARE_DELETED));
						controller.getRpcService().deleteFiles(deleteFileList, new EphesoftAsyncCallback<String>() {

							@Override
							public void onSuccess(String result) {
								confirmationDialog.hide();
								ScreenMaskUtility.unmaskScreen();
								if (result != null && !result.isEmpty()) {
									showErrorInConfirmationDialog(LocaleDictionary.get().getMessageValue(
											FolderManagementMessages.UNABLE_TO_DELETE_THE_FOLLOWING)
											+ result);
								} else {
									controller.getEventBus().fireEvent(new FolderTreeRefreshEvent());
								}
							}

							@Override
							public void customFailure(Throwable caught) {
								confirmationDialog.hide();
								ScreenMaskUtility.unmaskScreen();
								showErrorInConfirmationDialog(LocaleDictionary.get().getMessageValue(
										FolderManagementMessages.UNABLE_TO_PERFORM_DELETE_OPERATION));
							}
						});
					}

				}

				@Override
				public void onCancelClick() {
					confirmationDialog.hide();
				}
			});
			confirmationDialog.okButton.setFocus(true);
		}

	}

	public void onRefreshClicked() {
		controller.getEventBus().fireEvent(new FolderTreeRefreshEvent());

	}

	public void onPathChange(String folderPath) {
		controller.getEventBus().fireEvent(new PathRefreshEvent(folderPath));

	}

	public void onFileCopy(String absoluteFilePath) {
		cutOrCopyFileList = new ArrayList<String>();
		cutOrCopyFileList.add(absoluteFilePath);
		lastFileOperation = FileOperation.COPY;
	}

	public void onFileCut(String absoluteFilePath) {
		cutOrCopyFileList = new ArrayList<String>();
		cutOrCopyFileList.add(absoluteFilePath);
		lastFileOperation = FileOperation.CUT;
		cutFilesFolderPath = folderPath;

	}

	public void onRename(final String fileName, final String newFileName) {

		controller.getRpcService().renameFile(fileName, newFileName, folderPath, new EphesoftAsyncCallback<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					controller.getEventBus().fireEvent(new FolderTreeRefreshEvent());
				} else {
					showErrorInConfirmationDialog(LocaleDictionary.get().getMessageValue(
							FolderManagementMessages.FAILED_TO_RENAME_FILE,
							FolderManagementConstants.QUOTES + fileName + FolderManagementConstants.QUOTES,
							FolderManagementConstants.QUOTES + newFileName + FolderManagementConstants.QUOTES));
				}
			}

			@Override
			public void customFailure(Throwable caught) {
				showErrorInConfirmationDialog(LocaleDictionary.get().getMessageValue(FolderManagementMessages.FAILED_TO_RENAME_FILE,
						FolderManagementConstants.QUOTES + fileName + FolderManagementConstants.QUOTES,
						FolderManagementConstants.QUOTES + newFileName + FolderManagementConstants.QUOTES));

			}
		});

	}

	public void showErrorInConfirmationDialog(String errorMsg) {
		final ConfirmationDialog errorDialog = ConfirmationDialogUtil.showConfirmationDialogError(errorMsg, true);

		errorDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				errorDialog.hide();
				controller.getEventBus().fireEvent(new FolderTreeRefreshEvent());
			}

			@Override
			public void onCancelClick() {
				// does not need to be implemented
			}
		});
		errorDialog.okButton.setFocus(true);
	}

	private void refreshContent() {
		refreshContent(folderPath);
	}

	public void refreshContent(final String path) {
		if (null != path) {
			folderPath = path;
			controller.getEventBus().fireEvent(new PathRefreshEvent(folderPath));
			view.cleanupTableContent();
			controller.getRpcService().getContents(path, new EphesoftAsyncCallback<List<FileWrapper>>() {

				@Override
				public void customFailure(Throwable throwable) {
					String message = throwable.getLocalizedMessage();
					if (message.contains(FolderManagementConstants.ERROR_TYPE_1)) {
						view.addNoContentLabel();
					}
				}

				@Override
				public void onSuccess(List<FileWrapper> result) {
					if (path.equals(folderPath)) {
						view.setEnableAttributeForTableContent(true);
						view.updateTableContent(path, result);
					}
				}
			});
		}
	}

	public void openItem(FileWrapper file) {
		if (file != null) {
			String fileName = file.getName();
			String absoluteFilePath = file.getPath();
			if (!file.getKind().equals(FileType.DIR)) {
				String url = baseHttpURL
						+ FolderManagementConstants.URL_SEPARATOR
						+ (absoluteFilePath.substring(absoluteFilePath.lastIndexOf(parentFolderPath) + parentFolderPath.length() + 1))
								.replace(FolderManagementConstants.SHARED_PATH_SEPARATOR_STRING,
										FolderManagementConstants.URL_SEPARATOR);
				try {
					Window.open(url, "", OPEN_WINDOW_OPTIONS + WIDTH + Window.getClientWidth() + HEIGHT + Window.getClientHeight());
				} catch (Exception e) {
					showErrorInConfirmationDialog(LocaleDictionary.get().getMessageValue(
							FolderManagementMessages.COULD_NOT_OPEN_THE_FILE)
							+ FolderManagementConstants.QUOTES + fileName + FolderManagementConstants.QUOTES);
				}
			} else {
				controller.getEventBus().fireEvent(new FolderTreeRefreshEvent(absoluteFilePath, fileName));
			}
		}

	}

	public void setFolderPath(String path) {
		this.folderPath = path;

	}

	public void setSelectedFile(FileWrapper file) {
		this.selectedFile = file;

	}

	public void onFileDelete(final String fileName, String absoluteFilePath) {
		ScreenMaskUtility.maskScreen();
		controller.getRpcService().deleteFile(absoluteFilePath, new EphesoftAsyncCallback<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {

				ScreenMaskUtility.unmaskScreen();
				if (result) {
					controller.getEventBus().fireEvent(new FolderTreeRefreshEvent());
				} else {

					showErrorInConfirmationDialog(LocaleDictionary.get().getMessageValue(
							FolderManagementMessages.UNABLE_TO_DELETE_THE_FILE)
							+ FolderManagementConstants.QUOTES + fileName + FolderManagementConstants.QUOTES);
				}
			}

			@Override
			public void customFailure(Throwable caught) {
				ScreenMaskUtility.unmaskScreen();
				showErrorInConfirmationDialog(LocaleDictionary.get().getMessageValue(
						FolderManagementMessages.UNABLE_TO_DELETE_THE_FILE)
						+ FolderManagementConstants.QUOTES + fileName + FolderManagementConstants.QUOTES);
			}
		});

	}

	public String getFolderPath() {
		return folderPath;
	}

	public void openItem() {
		openItem(selectedFile);
	}

	public void onFolderUpClicked() {
		controller.getEventBus().fireEvent(new FolderUpEvent());
	}

	public String getParentFolderPath() {
		return parentFolderPath;
	}

	public void onFileDownload(FileWrapper file) {
		if (file != null) {
			String absoluteFilePath = file.getPath();
			sendDownloadRequest(absoluteFilePath);
		}
	}

	public void sendDownloadRequest(String absoluteFilePath) {
		String href = Window.Location.getHref();
		String baseUrl = href.substring(0, href.lastIndexOf(FolderManagementConstants.URL_SEPARATOR));
		StringBuffer urlBuffer = new StringBuffer(baseUrl);
		urlBuffer.append(FolderManagementConstants.URL_SEPARATOR);
		urlBuffer.append(FolderManagementConstants.UPLOAD_DOWNLOAD_SERVLET_PATH);
		if (baseUrl.contains(FolderManagementConstants.QUESTION_MARK)) {
			urlBuffer.append(FolderManagementConstants.AMPERSAND);
		} else {
			urlBuffer.append(FolderManagementConstants.QUESTION_MARK);
		}
		urlBuffer.append(FolderManagementConstants.CURRENT_FILE_DOWNLOAD_PATH);
		urlBuffer.append(FolderManagementConstants.EQUALS);
		urlBuffer.append(absoluteFilePath);
		Window.open(urlBuffer.toString(), null, null);

	}

	public List<String> getCutOrCopyFileList() {
		return cutOrCopyFileList;
	}

}
