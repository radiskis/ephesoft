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

package com.ephesoft.dcma.gwt.uploadbatch.client.presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.client.validator.ValidatableWidget;
import com.ephesoft.dcma.gwt.core.shared.BatchClassFieldDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.uploadbatch.client.UploadBatchController;
import com.ephesoft.dcma.gwt.uploadbatch.client.i18n.UploadBatchConstants;
import com.ephesoft.dcma.gwt.uploadbatch.client.i18n.UploadBatchMessages;
import com.ephesoft.dcma.gwt.uploadbatch.client.view.AssociateBCFView;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

public class AssociateBCFPresenter extends AbstractUploadBatchPresenter<AssociateBCFView> {

	private List<EditableWidgetStorage> docFieldWidgets;

	private boolean setVisibleDialogue = true;

	private List<BatchClassFieldDTO> batchClassFieldDTOs = new ArrayList<BatchClassFieldDTO>();

	public AssociateBCFPresenter(UploadBatchController controller, AssociateBCFView view) {
		super(controller, view);
		setBatchClassFieldDTOs(controller.getUploadBatchView().getSelectedBatchClassNameListBoxValue(), true);

	}

	public void setBatchClassFieldDTOs(final String batchClassId, final boolean isOnLoad) {
		controller.getRpcService().getBatchClassFieldDTOByBatchClassIdentifier(batchClassId,
				new AsyncCallback<List<BatchClassFieldDTO>>() {

					@Override
					public void onFailure(Throwable arg0) {
						ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
								UploadBatchMessages.ERROR_RETRIEVING_BCF));

					}

					@Override
					public void onSuccess(List<BatchClassFieldDTO> batchClassFieldDTOs1) {

						if (batchClassFieldDTOs1 != null && batchClassFieldDTOs1.size() > 0) {
							if (batchClassFieldDTOs.size() == batchClassFieldDTOs1.size() && batchClassFieldDTOs.size() > 0) {
								if (!(batchClassFieldDTOs.get(0).getBatchClass().getIdentifier().equalsIgnoreCase(batchClassFieldDTOs1
										.get(0).getBatchClass().getIdentifier()))) {
									batchClassFieldDTOs = batchClassFieldDTOs1;
								}
							} else {
								batchClassFieldDTOs = batchClassFieldDTOs1;
							}

							Collections.sort(batchClassFieldDTOs1, new Comparator<BatchClassFieldDTO>() {

								@Override
								public int compare(final BatchClassFieldDTO batchClassFieldDTO1,
										final BatchClassFieldDTO batchClassFieldDTO2) {
									int compare = 0;
									if (batchClassFieldDTO1 != null && batchClassFieldDTO2 != null) {
										int fieldOrderNumberOne = Integer.parseInt(batchClassFieldDTO1.getFieldOrderNumber());
										int fieldOrderNumberSec = Integer.parseInt(batchClassFieldDTO2.getFieldOrderNumber());
										if (fieldOrderNumberOne > fieldOrderNumberSec) {
											compare = 1;
										} else {
											if (fieldOrderNumberOne < fieldOrderNumberSec) {
												compare = -1;
											} else {
												compare = 0;
											}
										}
									}
									return compare;
								};
							});
						} else {
							batchClassFieldDTOs = batchClassFieldDTOs1;
						}
						if (!isOnLoad) {
							bind();
							showAssociateBCFView();
						}
					}
				});
	}

	public boolean isSetVisibleDialogue() {
		return setVisibleDialogue;
	}

	public void setSetVisibleDialogue(boolean setVisibleDialogue) {
		this.setVisibleDialogue = setVisibleDialogue;
	}

	public void onSave() {
		boolean fieldsValid = false;

		for (int index = 0; index < docFieldWidgets.size(); index++) {
			if (docFieldWidgets.get(index).isValidatable()) {
				if (!docFieldWidgets.get(index).isListBox()) {
					if (!docFieldWidgets.get(index).getTextBoxWidget().validate()) {
						ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
								UploadBatchMessages.INVALID_REGEX_PATTERN), LocaleDictionary.get().getConstantValue(
								UploadBatchConstants.ASSOCIATE_BCF_BUTTON));
						fieldsValid = true;
						setVisibleDialogue = false;
						break;
					}
				} else {
					// in case of select to be used in drop down
					if (docFieldWidgets.get(index).getListBoxwidget().getSelectedIndex() == -1) {
						ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
								UploadBatchMessages.BLANK_ERROR), LocaleDictionary.get().getConstantValue(
								UploadBatchConstants.ASSOCIATE_BCF_BUTTON));
						fieldsValid = true;
						break;
					}

				}
			}
		}

		if (!fieldsValid) {
			ScreenMaskUtility.maskScreen(LocaleDictionary.get().getMessageValue(UploadBatchMessages.WAITING_MESSAGE));
			setVisibleDialogue = true;
			int index = 0;
			for (BatchClassFieldDTO batchClassFieldDTO : batchClassFieldDTOs) {
				if (docFieldWidgets.get(index).isListBox) {
					batchClassFieldDTO.setValue(docFieldWidgets.get(index).getListBoxwidget().getItemText(
							docFieldWidgets.get(index).getListBoxwidget().getSelectedIndex()));
				} else {
					batchClassFieldDTO.setValue(docFieldWidgets.get(index).getTextBoxWidget().getWidget().getValue());
				}
				index++;
			}
			String currentBatchUploadFolder = controller.getCurrentBatchUploadFolder();
			if (currentBatchUploadFolder == null || currentBatchUploadFolder.isEmpty()) {

				// rpc call for fetching the folder name
				controller.getRpcService().getCurrentBatchFolderName(new AsyncCallback<String>() {

					@Override
					public void onSuccess(String currentBatchUploadFolder) {
						serializeBatchClassField(currentBatchUploadFolder);
					}

					@Override
					public void onFailure(Throwable arg0) {

					}
				});
			} else {
				serializeBatchClassField(currentBatchUploadFolder);
			}
		}
	}

	private void serializeBatchClassField(final String currentBatchUploadFolder){
		controller.setCurrentBatchUploadFolder(currentBatchUploadFolder);
		controller.getRpcService().serializeBatchClassField(controller.getCurrentBatchUploadFolder(),
				batchClassFieldDTOs, new AsyncCallback<Void>() {

				@Override
				public void onSuccess(Void arg0) {
					ScreenMaskUtility.unmaskScreen();
					if (controller.isFinishButtonClicked()) {
						controller.getMainPresenter().onFinishButtonClicked();
						controller.getUploadBatchView().getBatchClassNameListBox().setEnabled(true);
					} else {
						ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
								UploadBatchMessages.ASSOCIATE_BATCH_CLASS_FIELD_SUCCESS), LocaleDictionary.get()
								.getConstantValue(UploadBatchConstants.ASSOCIATE_BCF_BUTTON));
					}
				}

				@Override
				public void onFailure(Throwable arg0) {
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							UploadBatchMessages.ERROR_SAVING_BCF));

				}
			});
	
	}
	private void setProperties() {
		docFieldWidgets = new ArrayList<EditableWidgetStorage>();
		if (batchClassFieldDTOs != null && batchClassFieldDTOs.size() > 0) {
			int row = 0;
			for (final BatchClassFieldDTO batchClassFieldDTO : batchClassFieldDTOs) {
				view.formatRow(row);
				view.addWidget(row, 0, new Label(batchClassFieldDTO.getDescription() + ":"));
				// view.addWidgetStar(row, 1);
				if (batchClassFieldDTO.getFieldOptionValueList() != null && !batchClassFieldDTO.getFieldOptionValueList().isEmpty()) {
					// Create a drop down
					final ListBox fieldValue = view.addDropDown(batchClassFieldDTO.getFieldOptionValueList(), batchClassFieldDTO
							.getValue());
					view.addWidget(row, 1, fieldValue);
					docFieldWidgets.add(new EditableWidgetStorage(fieldValue));
				} else {
					// Create a text box
					ValidatableWidget<TextBox> validatableTextBox = view.addTextBox(batchClassFieldDTO);
					view.addWidget(row, 1, validatableTextBox.getWidget());

					docFieldWidgets.add(new EditableWidgetStorage(validatableTextBox));
				}
				row++;
			}
			row++;
			view.addButtons(row);
		} else {
			List<String> fileList = new ArrayList<String>();
			fileList.add(UploadBatchConstants.BCF_SER_FILE_NAME + UploadBatchConstants.SERIALIZATION_EXT);
			controller.getRpcService().deleteFilesByName(controller.getCurrentBatchUploadFolder(), fileList,
					new AsyncCallback<List<String>>() {

						@Override
						public void onSuccess(List<String> filesNotDeleted) {
							view.getDialogBox().hide();
							ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
									UploadBatchMessages.NO_BATCH_CLASS_FIELD_FOUND), LocaleDictionary.get().getConstantValue(
									UploadBatchConstants.ASSOCIATE_BCF_BUTTON));
						}

						@Override
						public void onFailure(Throwable arg0) {
							ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
									UploadBatchMessages.ERROR_DELETING_BCF));

						}
					});
		}

	}

	public boolean validateBatchClassField() {
		boolean flag = false;
		if (batchClassFieldDTOs != null && batchClassFieldDTOs.size() > 0) {
			for (final BatchClassFieldDTO batchClassFieldDTO : batchClassFieldDTOs) {
				if ((batchClassFieldDTO.getValue() == null || batchClassFieldDTO.getValue().isEmpty())
						&& (batchClassFieldDTO.getValidationPattern() != null && !batchClassFieldDTO.getValidationPattern().isEmpty())) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	public void clearBatchClassFieldValues() {
		for (BatchClassFieldDTO batchClassFieldDTO : batchClassFieldDTOs) {
			batchClassFieldDTO.setValue(null);
		}
	}

	private static class EditableWidgetStorage {

		private ValidatableWidget<TextBox> widget;
		private boolean isListBox;
		private ListBox listBoxwidget;
		private boolean isValidatable;

		public EditableWidgetStorage(ValidatableWidget<TextBox> widget) {
			this.widget = widget;
			this.isListBox = false;
			this.isValidatable = true;
		}

		public EditableWidgetStorage(ListBox listBoxwidget) {
			this.listBoxwidget = listBoxwidget;
			this.isListBox = true;
			this.isValidatable = false;
		}

		public ValidatableWidget<TextBox> getTextBoxWidget() {
			return widget;
		}

		public ListBox getListBoxwidget() {
			return listBoxwidget;
		}

		public boolean isListBox() {
			return isListBox;
		}

		public boolean isValidatable() {
			return isValidatable;
		}

	}

	@Override
	public void bind() {
		view.setView();
		setProperties();
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		// to be used in case of event handling
	}

	public void showAssociateBCFView() {
		if (batchClassFieldDTOs != null && batchClassFieldDTOs.size() > 0) {
			view.getDialogBox().setWidth("100%");
			view.getDialogBox().center();
			view.getDialogBox().add(view);
			view.getDialogBox().show();
			view.getDialogBox().setText(LocaleDictionary.get().getConstantValue(UploadBatchConstants.ASSOCIATE_BCF_BUTTON));
		}
	}

	
	public List<BatchClassFieldDTO> getBatchClassFieldDTOs() {
		return batchClassFieldDTOs;
	}
}
