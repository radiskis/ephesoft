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

package com.ephesoft.dcma.gwt.uploadbatch.client.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.swfupload.client.SWFUpload;
import org.swfupload.client.UploadBuilder;
import org.swfupload.client.SWFUpload.ButtonAction;
import org.swfupload.client.SWFUpload.ButtonCursor;
import org.swfupload.client.event.UploadCompleteHandler;
import org.swfupload.client.event.UploadErrorHandler;
import org.swfupload.client.event.UploadProgressHandler;
import org.swfupload.client.event.UploadStartHandler;

import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.uploadbatch.client.i18n.UploadBatchConstants;
import com.ephesoft.dcma.gwt.uploadbatch.client.i18n.UploadBatchMessages;
import com.ephesoft.dcma.gwt.uploadbatch.client.presenter.UploadBatchPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UploadBatchView extends View<UploadBatchPresenter> {

	@UiField
	ListBox batchClassNameListBox;

	@UiField
	protected VerticalPanel uploadBatchViewPanel;

	@UiField
	protected Label uploadLabel;

	@UiField
	protected Label uploadStar;

	@UiField
	protected Label uploadFilePlaceholder;

	@UiField
	protected Button uploadButton;

	@UiField
	protected Button resetButton;

	@UiField
	protected Button addBCFButton;

	@UiField
	protected Button finishButton;

	@UiField
	protected Button deleteButton;

	@UiField
	protected HorizontalPanel buttonPanel1;

	@UiField
	protected HorizontalPanel batchClassPanel;

	@UiField
	protected FlexTable fileListTable;

	@UiField
	protected ScrollPanel fileListPanel;

	@UiField
	protected CaptionPanel fileUploadCaptionPanel;

	@UiField
	protected CaptionPanel batchClassCaptionPanel;

	@UiField
	protected CaptionPanel actionCaptionPanel;

	@UiField
	protected CaptionPanel deleteCaptionPanel;

	@UiField
	protected HorizontalPanel selectAllCell;

	@UiField
	protected DockLayoutPanel mainDockPanel;

	private UploadBuilder uploadBuilder = new UploadBuilder();

	private FlexTable editTable;
	private CheckBox deleteAllBox;
	private List<CheckBox> allFilesBox;

	private Map<String, String> batchClassMapping;
	private AssociateBCFView associateBCFView;
	private List<String> allFileList;
	private List<String> deleteSelectedFileList;
	private SWFUpload swfUpload;

	interface Binder extends UiBinder<VerticalPanel, UploadBatchView> {
	}

	private static final String UPLOAD_FORM_ACTION = "dcma-gwt-upload-batch/uploadBatch?";
	private static final Binder binder = GWT.create(Binder.class);

	public UploadBatchView() {
		super();
		initWidget(binder.createAndBindUi(this));

		buttonPanel1.addStyleName("paddingTop");
		uploadBatchViewPanel.addStyleName("padding");
		mainDockPanel.addStyleName("deleteTable");

		batchClassPanel.addStyleName("paddingTop");

		fileUploadCaptionPanel.setCaptionText(LocaleDictionary.get().getConstantValue(UploadBatchConstants.UPLOAD_BUTTON));
		fileUploadCaptionPanel.addStyleName("panel_bg");
		fileUploadCaptionPanel.addStyleName("bold_text");
		fileUploadCaptionPanel.addStyleName("marginLocal");

		batchClassCaptionPanel.setCaptionText(LocaleDictionary.get().getConstantValue(UploadBatchConstants.BATCH_DETAIL));
		batchClassCaptionPanel.addStyleName("panel_bg");
		batchClassCaptionPanel.addStyleName("bold_text");
		batchClassCaptionPanel.addStyleName("marginLocal");

		actionCaptionPanel.setCaptionText(LocaleDictionary.get().getConstantValue(UploadBatchConstants.ACTION));
		actionCaptionPanel.addStyleName("panel_bg");
		actionCaptionPanel.addStyleName("bold_text");
		actionCaptionPanel.addStyleName("marginLocal");

		deleteCaptionPanel.setCaptionText(LocaleDictionary.get().getConstantValue(UploadBatchConstants.FILE_LIST));
		deleteCaptionPanel.addStyleName("panel_bg");
		deleteCaptionPanel.addStyleName("bold_text");
		deleteCaptionPanel.addStyleName("marginLocal");

		editTable = new FlexTable();
		editTable.setWidth("100%");

		fileListTable.setWidget(0, 0, editTable);
		fileListTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);

		associateBCFView = new AssociateBCFView();
		allFileList = new LinkedList<String>();
		deleteSelectedFileList = new LinkedList<String>();
		allFilesBox = new ArrayList<CheckBox>();

		finishButton.setText(LocaleDictionary.get().getConstantValue(UploadBatchConstants.FINISH_BUTTON));
		// disable Finish button by default
		finishButton.setEnabled(Boolean.FALSE);
		// disable Field(s) button by default
		addBCFButton.setEnabled(Boolean.FALSE);
		// disable Delete button by default
		deleteButton.setEnabled(Boolean.FALSE);
		deleteButton.setVisible(Boolean.FALSE);
		deleteCaptionPanel.setVisible(Boolean.FALSE);

		uploadLabel.setText(LocaleDictionary.get().getConstantValue(UploadBatchConstants.UPLOAD_TEXT));
		uploadStar.setText(UploadBatchConstants.STAR);
		uploadStar.setStyleName("font_red");
		uploadButton.setText(LocaleDictionary.get().getConstantValue(UploadBatchConstants.UPLOAD_BUTTON));

		resetButton.setText(LocaleDictionary.get().getConstantValue(UploadBatchConstants.RESET_BUTTON));
		addBCFButton.setText(LocaleDictionary.get().getConstantValue(UploadBatchConstants.ASSOCIATE_BCF_BUTTON));
		deleteButton.setText(LocaleDictionary.get().getConstantValue(UploadBatchConstants.DELETE_BUTTON));

		HTMLPanel panel = new HTMLPanel("<div id=\"swfupload\"></div>");
		uploadFilePlaceholder.getElement().appendChild(panel.getElement());
		RootPanel.get().clear();
		RootPanel.get().add(this);

		uploadBuilder.setButtonAction(ButtonAction.SELECT_FILES);
		uploadBuilder.setButtonCursor(ButtonCursor.HAND);
		uploadBuilder.setButtonImageURL("images/browse.png");
		//uploadBuilder.setButtonText(LocaleDictionary.get().getMessageValue(UploadBatchMessages.BROWSE));
		uploadBuilder.setButtonHeight(25);
		uploadBuilder.setButtonWidth(68);

		uploadBuilder.setButtonPlaceholderID("swfupload");
		uploadBuilder.setFileTypes("*.tif;*.tiff;*.pdf");
		uploadBuilder.setFileTypesDescription(LocaleDictionary.get().getConstantValue(
				UploadBatchConstants.FILE_TYPES));

		/*uploadBuilder.setFileDialogCompleteHandler(new FileDialogCompleteHandler() {

			@Override
			public void onFileDialogComplete(FileDialogCompleteEvent e) {
				presenter.onSubmit(UPLOAD_FORM_ACTION);
				uploadButton.setEnabled(true);
			}
		});
		 */

		uploadBuilder.setUploadProgressHandler(new UploadProgressHandler() {

			@Override
			public void onUploadProgress(UploadProgressHandler.UploadProgressEvent e) {
				String fileName = e.getFile().getName();
				double percent = Math.ceil(((e.getBytesComplete() * 1000.0 / e.getBytesTotal()) * 100.0) / 1000.0);
				int indexInList = allFileList.indexOf(fileName);
				if (indexInList != -1) {
					String innerHTML = LocaleDictionary.get().getConstantValue(
							UploadBatchConstants.UPLOAD_PROGRESS) + percent;
					innerHTML = innerHTML.split("\\.")[0] + "%";
					DOM.getElementById("progressBar_" + indexInList).setInnerHTML(innerHTML);
				}
			}
		});

		uploadBuilder.setUploadErrorHandler(new UploadErrorHandler() {

			@Override
			public void onUploadError(UploadErrorEvent event) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						UploadBatchMessages.UPLOAD_UNSUCCESSFUL)
						+ event.getFile());
				return;
			}
		});
		uploadBuilder.setUploadStartHandler(new UploadStartHandler() {

			@Override
			public void onUploadStart(UploadStartEvent event) {
				String currentFileName = event.getFile().getName();
				// update the file list
				if (!allFileList.contains(currentFileName)) {
					allFileList.add(currentFileName);
				}
				presenter.repaintFileList();
			}
		});

		uploadBuilder.setUploadCompleteHandler(new UploadCompleteHandler() {

			@Override
			public void onUploadComplete(UploadCompleteEvent event) {
				String currentFileName = event.getFile().getName();
				// update the file list
				if (!allFileList.contains(currentFileName)) {
					allFileList.add(currentFileName);
				}
				int fileLength = swfUpload.getStats().getFilesQueued();
				if (fileLength > 0) {
					swfUpload.startUpload();
				} else {
					ScreenMaskUtility.unmaskScreen();

					if (!getAllFileList().isEmpty()) {
						// enable finish button
						finishButton.setEnabled(Boolean.TRUE);
						// enable field(s) button
						addBCFButton.setEnabled(Boolean.TRUE);
						// enable delete button
						deleteButton.setVisible(Boolean.TRUE);
						deleteButton.setEnabled(Boolean.TRUE);
						deleteCaptionPanel.setVisible(Boolean.TRUE);
						fileListPanel.addStyleName("deleteTable");

					}
					ConfirmationDialogUtil.showConfirmationDialogSuccess(LocaleDictionary.get().getMessageValue(
							UploadBatchMessages.FILE_UPLOAD_COMPLETE_ALERT));

				}
			}
		});

		uploadBuilder.preventSWFCaching(true);
		setSwfUpload(uploadBuilder.build());
	}

	public void formatRow(int row) {
		editTable.getCellFormatter().setWidth(row, 0, "1%");
		editTable.getFlexCellFormatter().setAlignment(row, 0, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_MIDDLE);
		editTable.getCellFormatter().setWidth(row, 1, "48%");
		editTable.getCellFormatter().setWidth(row, 2, "30%");
		editTable.getFlexCellFormatter().addStyleName(0, 0, "bold_text");
		editTable.getFlexCellFormatter().addStyleName(0, 1, "bold_text");
		editTable.getFlexCellFormatter().addStyleName(1, 0, "bold_text");
		editTable.getFlexCellFormatter().addStyleName(1, 1, "bold_text");
	}

	public void addWidget(int row, int column, Widget widget) {
		editTable.setWidget(row, column, widget);
	}

	@UiHandler("addBCFButton")
	public void onAddBCFButtonClick(ClickEvent clickEvent) {
		showAssociateBatchClassField();
	}

	private void showAssociateBatchClassField(Boolean isFinishButtonClicked) {
		if (isFinishButtonClicked != null && isFinishButtonClicked) {
			presenter.getController().setIsFinishButtonClicked(isFinishButtonClicked);
		} else {
			presenter.getController().setIsFinishButtonClicked(Boolean.FALSE);
		}

		String identifier = getSelectedBatchClassNameListBoxValue();
		if (identifier == null || identifier.isEmpty()) {
			ConfirmationDialogUtil.showConfirmationDialogError((LocaleDictionary.get()
					.getMessageValue(UploadBatchMessages.NONE_SELECTED_WARNING)));
		} else {
			final DialogBox dialogBox = new DialogBox();
			associateBCFView.setDialogBox(dialogBox);
			presenter.onAssociateBatchClassFieldButtonClicked(identifier);
			associateBCFView.getSave().setFocus(true);
		}
	}

	@UiHandler("resetButton")
	public void onResetClick(ClickEvent clickEvent) {
		presenter.onResetButtonClick();
	}

	@UiHandler("uploadButton")
	public void onUploadClick(ClickEvent clickEvent) {
		int fileLength = swfUpload.getStats().getFilesQueued();
		if (fileLength == 0) {
			ConfirmationDialogUtil.showConfirmationDialogError((LocaleDictionary.get()
					.getMessageValue(UploadBatchMessages.UPLOAD_FILE_INVALID_TYPE)));
			return;
		}
		ScreenMaskUtility.maskScreen();
		presenter.onSubmit(UPLOAD_FORM_ACTION);
	}

	@UiHandler("deleteButton")
	public void onDeleteClick(ClickEvent clickEvent) {
		presenter.onDeleteButtonClicked();
	}

	public ListBox getBatchClassNameListBox() {
		return batchClassNameListBox;
	}

	public void setBatchClassNameListBoxValues(Collection<String> batchListValues) {
		if (!batchListValues.isEmpty()) {
			this.batchClassNameListBox.setEnabled(true);
			this.batchClassNameListBox.setVisibleItemCount(1);
			for (String value : batchListValues) {
				this.batchClassNameListBox.addItem(value);
			}
		} else {
			this.batchClassNameListBox.setEnabled(false);
			this.batchClassNameListBox.setWidth("150px");
		}
	}

	@UiHandler("batchClassNameListBox")
	void onChange(ChangeEvent event) {
		presenter.getAssociateBCFPresenter().setBatchClassFieldDTOs(getSelectedBatchClassNameListBoxValue(), true);
	}

	@UiHandler("finishButton")
	void onFinish(ClickEvent event) {
		String currentBatchUploadFolder = presenter.getController().getCurrentBatchUploadFolder();
		if (currentBatchUploadFolder != null && !currentBatchUploadFolder.isEmpty()) {
			boolean flag = presenter.getAssociateBCFPresenter().validateBatchClassField();
			if (presenter.getAssociateBCFPresenter().getBatchClassFieldDTOs() != null
					&& presenter.getAssociateBCFPresenter().getBatchClassFieldDTOs().size() > 0 && flag) {
				showAssociateBatchClassField(Boolean.TRUE);
			} else {
				presenter.onFinishButtonClicked();
				batchClassNameListBox.setEnabled(true);
			}
		} else {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					UploadBatchMessages.ERROR_FINISH_CLICKED));
		}
	}

	private void showAssociateBatchClassField() {
		showAssociateBatchClassField(null);
	}

	public void toggleAllCheckBoxes(Boolean isSelect) {
		for (CheckBox fileCheckBox : allFilesBox) {
			fileCheckBox.setValue(isSelect);
		}
	}

	public String getSelectedBatchClassNameListBoxValue() {
		if (this.batchClassNameListBox != null && this.batchClassNameListBox.getSelectedIndex() >= 0) {
			String selectedValue = this.batchClassNameListBox.getItemText(this.batchClassNameListBox.getSelectedIndex());
			return batchClassMapping.get(selectedValue);
		} else {
			return null;
		}
	}

	public Map<String, String> getBatchClassMapping() {
		return batchClassMapping;
	}

	public void setBatchClassMapping(Map<String, String> batchClassMapping) {
		this.batchClassMapping = batchClassMapping;
	}

	public VerticalPanel getUploadBatchViewPanel() {
		return uploadBatchViewPanel;
	}

	public AssociateBCFView getAssociateBCFView() {
		return associateBCFView;
	}

	public Button getFinishButton() {
		return finishButton;
	}

	public Button getAddBCFButton() {
		return addBCFButton;
	}

	public List<String> getAllFileList() {
		return allFileList;
	}

	public FlexTable getEditTable() {
		return editTable;
	}

	public List<String> getDeleteSelectedFileList() {
		return deleteSelectedFileList;
	}

	public Button getDeleteButton() {
		return deleteButton;
	}

	public CheckBox getDeleteAllBox() {
		return deleteAllBox;
	}

	public void setDeleteAllBox(CheckBox deleteAllBox) {
		this.deleteAllBox = deleteAllBox;
	}

	public List<CheckBox> getAllFilesBox() {
		return allFilesBox;
	}

	public ScrollPanel getFileListPanel() {
		return fileListPanel;
	}

	public CaptionPanel getDeleteCaptionPanel() {
		return deleteCaptionPanel;
	}

	public HorizontalPanel getSelectAllCell() {
		return selectAllCell;
	}

	public void setSwfUpload(SWFUpload swfUpload) {
		this.swfUpload = swfUpload;
	}

	public SWFUpload getSwfUpload() {
		return swfUpload;
	}
}


