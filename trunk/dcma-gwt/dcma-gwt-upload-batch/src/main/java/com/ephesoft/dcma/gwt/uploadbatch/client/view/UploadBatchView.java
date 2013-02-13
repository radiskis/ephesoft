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

package com.ephesoft.dcma.gwt.uploadbatch.client.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.swfupload.client.File;
import org.swfupload.client.SWFUpload;
import org.swfupload.client.UploadBuilder;
import org.swfupload.client.SWFUpload.ButtonAction;
import org.swfupload.client.SWFUpload.ButtonCursor;
import org.swfupload.client.event.FileQueuedHandler;
import org.swfupload.client.event.UploadCompleteHandler;
import org.swfupload.client.event.UploadErrorHandler;
import org.swfupload.client.event.UploadProgressHandler;
import org.swfupload.client.event.UploadStartHandler;

import com.ephesoft.dcma.core.common.UserType;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.uploadbatch.client.i18n.UploadBatchConstants;
import com.ephesoft.dcma.gwt.uploadbatch.client.i18n.UploadBatchMessages;
import com.ephesoft.dcma.gwt.uploadbatch.client.presenter.UploadBatchPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
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
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UploadBatchView extends View<UploadBatchPresenter> {

	private static final String MARGIN_LOCAL = "marginLocal";

	private static final String BOLD_TEXT = "bold_text";

	private static final String PANEL_BG = "panel_bg";
	
	/**
	 * The SIZE_MULTIPLIER {@link Long} 
	 */
	private static final Long SIZE_MULTIPLIER = 1024L;

	@UiField
	protected ListBox batchClassNameListBox;

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

	private final UploadBuilder uploadBuilder = new UploadBuilder();

	private final FlexTable editTable;
	private CheckBox deleteAllBox;
	private final List<CheckBox> allFilesBox;

	private Map<String, String> batchClassMapping;
	private final AssociateBCFView associateBCFView;
	private final List<String> allFileList;
	private final List<String> deleteSelectedFileList;
	private SWFUpload swfUpload;
	
	
	private int oldIndexListBox = 0;
	
	private int oversizedFileCount = 0;

	/**
	 * The fileIndexList {@link List} is a list containing file indexes used for 
	 * cancel upload if limit is exceeded.
	 */
	private List<Integer> fileIndexList;

	interface Binder extends UiBinder<VerticalPanel, UploadBatchView> {
	}

	private static final String UPLOAD_FORM_ACTION = "dcma-gwt-upload-batch/uploadBatch?";
	private static final Binder BINDER = GWT.create(Binder.class);

	public UploadBatchView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		buttonPanel1.addStyleName("paddingTop");
		uploadBatchViewPanel.addStyleName("padding");
		mainDockPanel.addStyleName("deleteTable");

		batchClassPanel.addStyleName("paddingTop");

		fileUploadCaptionPanel.setCaptionHTML(LocaleDictionary.get().getConstantValue(UploadBatchConstants.UPLOAD_LABEL));
		fileUploadCaptionPanel.addStyleName(PANEL_BG);
		fileUploadCaptionPanel.addStyleName(BOLD_TEXT);
		fileUploadCaptionPanel.addStyleName(MARGIN_LOCAL);

		batchClassCaptionPanel.setCaptionHTML(LocaleDictionary.get().getConstantValue(UploadBatchConstants.BATCH_DETAIL));
		batchClassCaptionPanel.addStyleName(PANEL_BG);
		batchClassCaptionPanel.addStyleName(BOLD_TEXT);
		batchClassCaptionPanel.addStyleName(MARGIN_LOCAL);

		actionCaptionPanel.setCaptionHTML(LocaleDictionary.get().getConstantValue(UploadBatchConstants.ACTION));
		actionCaptionPanel.addStyleName(PANEL_BG);
		actionCaptionPanel.addStyleName(BOLD_TEXT);
		actionCaptionPanel.addStyleName(MARGIN_LOCAL);

		deleteCaptionPanel.setCaptionHTML(LocaleDictionary.get().getConstantValue(UploadBatchConstants.FILE_LIST));
		deleteCaptionPanel.addStyleName(PANEL_BG);
		deleteCaptionPanel.addStyleName(BOLD_TEXT);
		deleteCaptionPanel.addStyleName(MARGIN_LOCAL);

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
		
		fileIndexList = new ArrayList<Integer>();

		/*uploadBuilder.setFileDialogCompleteHandler(new FileDialogCompleteHandler() {

			@Override
			public void onFileDialogComplete(FileDialogCompleteEvent e) {
				presenter.onSubmit(UPLOAD_FORM_ACTION);
				uploadButton.setEnabled(true);
			}
		});
		 */
		
		// To maintain index of file queued for upload.
		uploadBuilder.setFileQueuedHandler(new FileQueuedHandler() {

			@Override
			public void onFileQueued(FileQueuedEvent event) {
				File file = event.getFile();
				fileIndexList.add(file.getIndex());
			}
		});

		uploadBuilder.setUploadProgressHandler(new UploadProgressHandler() {

			@Override
			public void onUploadProgress(UploadProgressHandler.UploadProgressEvent event) {
				String fileName = event.getFile().getName();
				double percent = Math.ceil(((event.getBytesComplete() * 1000.0 / event.getBytesTotal()) * 100.0) / 1000.0);
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
						
						// If user browser is IE
						if (ScreenMaskUtility.getUserAgent().contains(UploadBatchConstants.IE_BROWSER)) {
							fileListPanel.setWidth(UploadBatchConstants.WIDTH_26_EM);
						}

					}
					if (oversizedFileCount > 0) {
						String fileSizeMessage = oversizedFileCount + LocaleDictionary.get().getMessageValue(
								UploadBatchMessages.FILE_SIZE_EXCEED_MESSAGE) + UploadBatchConstants.SPACE + 
								(presenter.getFileSizeLimit()/SIZE_MULTIPLIER) + UploadBatchConstants.SPACE +
								UploadBatchConstants.MEGA_BYTE;
						String title  = LocaleDictionary.get().getMessageValue(
								UploadBatchMessages.LIMIT_REACHED);
						ConfirmationDialogUtil.showConfirmationDialog(fileSizeMessage, title, true);
					} else {
						ConfirmationDialogUtil.showConfirmationDialogSuccess(LocaleDictionary.get().getMessageValue(
							UploadBatchMessages.FILE_UPLOAD_COMPLETE_ALERT));
					}
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
		editTable.getFlexCellFormatter().addStyleName(0, 0, BOLD_TEXT);
		editTable.getFlexCellFormatter().addStyleName(0, 1, BOLD_TEXT);
		editTable.getFlexCellFormatter().addStyleName(1, 0, BOLD_TEXT);
		editTable.getFlexCellFormatter().addStyleName(1, 1, BOLD_TEXT);
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
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get()
					.getMessageValue(UploadBatchMessages.NONE_SELECTED_WARNING));
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

	/**
	 * The <code>onUploadClick</code> is a method for handling on click
	 * event of Upload Image Button.
	 * 
	 * @param clickEvent {@link ClickEvent}
	 */
	@UiHandler("uploadButton")
	public void onUploadClick(ClickEvent clickEvent) {
		
		// Check for batch class limit error
		if (presenter.checkForBatchClassLimit(getSelectedBatchClassNameListBoxValue())) {
			String message = LocaleDictionary.get().getMessageValue(
					UploadBatchMessages.UPLOAD_INSTANCE_LIMIT_ERROR);
			String title  = LocaleDictionary.get().getMessageValue(
					UploadBatchMessages.LIMIT_REACHED);
			ConfirmationDialogUtil.showConfirmationDialog(message, title, true);
			clearTempUploadOnError();
		} else { // Check for page count error
			int fileLength = swfUpload.getStats().getFilesQueued();
			
			// Image limit for a particular batch class
			Integer imageLimit = presenter.
					getBatchClassImageLimit(batchClassNameListBox.getValue(batchClassNameListBox.getSelectedIndex()));
			if (fileLength == 0) {
				
				//Disable the "Browse" button.
				swfUpload.setButtonDisabled(true);
				ConfirmationDialog confirmationDialog= ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get()
						.getMessageValue(UploadBatchMessages.UPLOAD_FILE_INVALID_TYPE));
				
				//Adding close handler to ConfirmationDialog to handle closing of dialog panel through "escape" button.
				confirmationDialog.addCloseHandler(new CloseHandler<PopupPanel>() {
					
					@Override
					public void onClose(CloseEvent<PopupPanel> closeEvent) {
						
						//Enable the "Browse: button.
						swfUpload.setButtonDisabled(false);
						
					}
				});
				confirmationDialog.okButton.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent clickEvent) {
						
						//Enable the "Browse: button.
						swfUpload.setButtonDisabled(false);
					}
				});
			} else if (null != imageLimit && imageLimit > 0 && (fileLength + allFileList.size()) > imageLimit) {
				 String message = LocaleDictionary.get().getMessageValue(
							UploadBatchMessages.UPLOAD_IMAGE_LIMIT_ERROR) + imageLimit + UploadBatchConstants.SPACE 
							+ LocaleDictionary.get().getMessageValue(
									UploadBatchMessages.UPLOAD_INSTANCE_LIMIT_APPENDED_MESSAGE);
					String title  = LocaleDictionary.get().getMessageValue(
							UploadBatchMessages.LIMIT_REACHED);
					ConfirmationDialogUtil.showConfirmationDialog(message, title, true);
				clearTempUploadOnError();
			} else {
				oversizedFileCount = checkForFileSize();
				if (fileLength == oversizedFileCount) {
					String fileSizeMessage = oversizedFileCount + LocaleDictionary.get().getMessageValue(
							UploadBatchMessages.FILE_SIZE_EXCEED_MESSAGE) + UploadBatchConstants.SPACE + 
							(presenter.getFileSizeLimit()/SIZE_MULTIPLIER) + UploadBatchConstants.SPACE +
							UploadBatchConstants.MEGA_BYTE;
					String title  = LocaleDictionary.get().getMessageValue(
							UploadBatchMessages.LIMIT_REACHED);
					ConfirmationDialogUtil.showConfirmationDialog(fileSizeMessage, title, true);
				} else {
					fileIndexList.clear();
					ScreenMaskUtility.maskScreen();
					presenter.onSubmit(UPLOAD_FORM_ACTION);
				}
			}
		}
	}

	@UiHandler("deleteButton")
	public void onDeleteClick(ClickEvent clickEvent) {
		presenter.onDeleteButtonClicked();
	}

	public ListBox getBatchClassNameListBox() {
		return batchClassNameListBox;
	}

	public void setBatchClassNameListBoxValues(Map<String, String> batchClassInfoMap, String selectedBatchClassInfo) {
		if (!batchClassInfoMap.isEmpty()) {
			this.batchClassNameListBox.setEnabled(true);
			this.batchClassNameListBox.setVisibleItemCount(1);
			int indexCount = 0;
			boolean isSelectedIndexSet = false;
			for (String key : batchClassInfoMap.keySet()) {
				this.batchClassNameListBox.addItem(key, batchClassInfoMap.get(key));
				if (!isSelectedIndexSet && null != selectedBatchClassInfo  
						&& batchClassInfoMap.get(key).trim().equals(selectedBatchClassInfo.trim())) {
					this.batchClassNameListBox.setItemSelected(indexCount, true);
					oldIndexListBox = indexCount;
					isSelectedIndexSet = true;
				}
				indexCount++;
			}
		} else {
			this.batchClassNameListBox.setEnabled(false);
			this.batchClassNameListBox.setWidth("150px");
		}
	}

	@UiHandler("batchClassNameListBox")
	protected void onChange(ChangeEvent event) {
		String newValue  = getSelectedBatchClassNameListBoxValue();
		Integer imageLimit = presenter.getBatchClassImageLimit(newValue);
		int newIndex = batchClassNameListBox.getSelectedIndex();
		
		// Check for batch class instance limit error
		if (presenter.checkForBatchClassLimit(newValue)) {
			batchClassNameListBox.setSelectedIndex(oldIndexListBox);
			String message = LocaleDictionary.get().getMessageValue(
					UploadBatchMessages.UPLOAD_INSTANCE_LIMIT_ERROR);
			String title  = LocaleDictionary.get().getMessageValue(
					UploadBatchMessages.LIMIT_REACHED);
			ConfirmationDialogUtil.showConfirmationDialog(message, title, true);
		} else if (null != imageLimit && allFileList.size() > imageLimit) { // Check for page count limit error
			batchClassNameListBox.setSelectedIndex(oldIndexListBox);
			 String message = LocaleDictionary.get().getMessageValue(
						UploadBatchMessages.UPLOAD_IMAGE_LIMIT_ERROR) + imageLimit + UploadBatchConstants.SPACE
						+ LocaleDictionary.get().getMessageValue(
								UploadBatchMessages.UPLOAD_INSTANCE_LIMIT_APPENDED_MESSAGE);
				String title  = LocaleDictionary.get().getMessageValue(
						UploadBatchMessages.LIMIT_REACHED);
				ConfirmationDialogUtil.showConfirmationDialog(message, title, true);
		} else {
			presenter.getAssociateBCFPresenter().setBatchClassFieldDTOs(newValue, true);
			presenter.setSelectedBatchClassInfoToSession(batchClassNameListBox.getValue(newIndex));
			oldIndexListBox = newIndex;
		}
	}

	@UiHandler("finishButton")
	protected void onFinish(ClickEvent event) {
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
		String selectedValue = null;
		if (this.batchClassNameListBox != null && this.batchClassNameListBox.getSelectedIndex() >= 0) {
			selectedValue = this.batchClassNameListBox.getValue(this.batchClassNameListBox.getSelectedIndex());			
		}
		return selectedValue;
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

	public final void setSwfUpload(SWFUpload swfUpload) {
		this.swfUpload = swfUpload;
	}

	public SWFUpload getSwfUpload() {
		return swfUpload;
	}
	
	/**
	 * The <code>clearTempUploadOnError</code> method is used for clearing upload
	 * for error files.
	 */
	private void clearTempUploadOnError() {
		Iterator<Integer> fileIndexIterator = fileIndexList.listIterator();
		
		// Cancel all current upload
		while (fileIndexIterator.hasNext()) {
			int index = fileIndexIterator.next();
			File file = swfUpload.getFile(index);
			swfUpload.cancelUpload(file.getId(), false);
		}
		fileIndexList.clear();
	}
	
	/**
	 * The <code>checkForFileSize</code> method is used for checking files crossing
	 * file size limit.
	 * 
	 * @return over-sized file count
	 */
	private int checkForFileSize() {
		int overSizeFileCount = 0;
		Integer userType = presenter.getUserType();
		if (null != userType && userType.intValue() == UserType.LIMITED.getUserType()) {
			Iterator<Integer> fileIndexIterator = fileIndexList.listIterator();
			
			List<Integer> removedFileIndexList = new ArrayList<Integer>(fileIndexList.size());
			
			// Cancel all over-sized current upload
			while (fileIndexIterator.hasNext()) {
				int index = fileIndexIterator.next();
				File file = swfUpload.getFile(index);
				if (file.getSize() > (presenter.getFileSizeLimit() * SIZE_MULTIPLIER)) {
					swfUpload.cancelUpload(file.getId(), false);
					overSizeFileCount++;
					removedFileIndexList.add(index);
				}
				
			}
			if (overSizeFileCount > 0) {
				fileIndexList.removeAll(removedFileIndexList);
			}
		}
		return overSizeFileCount;
	}
}


