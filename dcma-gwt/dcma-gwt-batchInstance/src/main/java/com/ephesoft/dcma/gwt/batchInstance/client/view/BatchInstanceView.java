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

package com.ephesoft.dcma.gwt.batchInstance.client.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.gwt.batchInstance.client.i18n.BatchInstanceConstants;
import com.ephesoft.dcma.gwt.batchInstance.client.i18n.BatchInstanceMessages;
import com.ephesoft.dcma.gwt.batchInstance.client.presenter.BatchInstancePresenter;
import com.ephesoft.dcma.gwt.batchInstance.client.presenter.BatchInstancePresenter.ActionableStatus;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.client.ui.table.Record;
import com.ephesoft.dcma.gwt.core.shared.BatchInstanceDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchPriority;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BatchInstanceView extends View<BatchInstancePresenter> {

	@UiField
	HorizontalPanel filterAndSearchPanel;

	@UiField
	DockLayoutPanel mainLayoutPanel;

	@UiField
	Label totalBatchesLabel;

	@UiField
	Label totalBatches;

	@UiField
	Label deletedBatchesLabel;

	@UiField
	Label deletedBatches;

	@UiField
	Label restartedBatchesLabel;

	@UiField
	Label restartedBatches;

	@UiField
	VerticalPanel labelPanel;

	@UiField
	Label batchAlerts;

	@UiField
	HorizontalPanel valuesPanel;

	@UiField
	HorizontalPanel batchAlertPanel;

	@UiField
	LayoutPanel batchInstanceLayoutPanel;

	@UiField
	DockLayoutPanel batchInstanceListPanel;

	@UiField
	HorizontalPanel actionPanel;

	private Button restartBatchButton;

	private Button deleteBatchButton;

	private HorizontalPanel filterPanel;

	private HorizontalPanel searchPanel;

	private ListBox priorityListBox;

	private ListBox batchInstanceListBox;

	private Label priorityLabel;

	private Label batchInstanceLabel;

	private Button searchBatchButton;

	private Label searchBatchLabel;

	private HorizontalPanel deleteButtonPanel;

	private HorizontalPanel restartPanel;

	private TextBox searchBatchTextBox;

	private Button refreshButton;

	public ListBox restartOptions;

	private BatchInstanceListView batchInstanceListView;

	private Map<String, BatchInstanceDTO> batchInstanceDTOMap = new HashMap<String, BatchInstanceDTO>();

	interface Binder extends UiBinder<DockLayoutPanel, BatchInstanceView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	public BatchInstanceView() {
		initWidget(binder.createAndBindUi(this));

		restartBatchButton = new Button();

		deleteBatchButton = new Button();

		filterPanel = new HorizontalPanel();

		searchPanel = new HorizontalPanel();

		priorityListBox = new ListBox();

		batchInstanceListBox = new ListBox();

		priorityLabel = new Label();

		batchInstanceLabel = new Label();

		searchBatchButton = new Button();

		searchBatchLabel = new Label();

		deleteButtonPanel = new HorizontalPanel();

		restartPanel = new HorizontalPanel();

		searchBatchTextBox = new TextBox();

		refreshButton = new Button();

		restartOptions = new ListBox();

		refreshButton.setText(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.BUTTON_REFRESH));
		searchBatchLabel.setText(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.LABEL_SEARCH_BATCH)
				+ BatchInstanceConstants.COLON);
		searchBatchButton.setText(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.BUTTON_SEARCH_BATCH));
		restartBatchButton.setText(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.BUTTON_RESTART_BATCH));
		deleteBatchButton.setText(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.BUTTON_DELETE_BATCH));
		totalBatchesLabel.setText(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.TOTAL_BATCHES));
		deletedBatchesLabel.setText(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.DELETED_BATCHES));
		// restartedBatchesLabel.setText(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.RESTARTED_BATCHES));
		searchBatchTextBox.setText(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.SEARCH_CRITERIA));
		batchAlerts.setText(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.BATCH_ALERTS));
		batchInstanceListView = new BatchInstanceListView();
		batchInstanceLayoutPanel.add(batchInstanceListView.listView);
		fillPriorityListBox();
		fillBatchInstanceListBox();
		setDefaultFilters();
		batchInstanceListPanel.addStyleName("mainPanelLayout");

		searchPanel.addStyleName("groupPanelLayout");
		filterPanel.addStyleName("groupPanelLayout");
		restartPanel.addStyleName("groupPanelLayout");
		deleteButtonPanel.addStyleName("groupPanelLayout");

		filterAndSearchPanel.addStyleName("compositePanelLayout");
		actionPanel.addStyleName("compositePanelLayout");

		filterAndSearchPanel.setSpacing(5);
		actionPanel.setSpacing(5);

		filterPanel.add(priorityLabel);
		filterPanel.add(priorityListBox);
		filterPanel.add(batchInstanceLabel);
		filterPanel.add(batchInstanceListBox);
		filterPanel.add(refreshButton);
		filterPanel.setCellVerticalAlignment(priorityLabel, HasVerticalAlignment.ALIGN_MIDDLE);
		filterPanel.setCellVerticalAlignment(batchInstanceLabel, HasVerticalAlignment.ALIGN_MIDDLE);
		filterPanel.setCellVerticalAlignment(refreshButton, HasVerticalAlignment.ALIGN_MIDDLE);
		filterPanel.setSpacing(5);

		searchPanel.add(searchBatchLabel);
		searchPanel.add(searchBatchTextBox);
		searchPanel.add(searchBatchButton);
		searchPanel.setCellVerticalAlignment(searchBatchLabel, HasVerticalAlignment.ALIGN_MIDDLE);
		searchPanel.setCellVerticalAlignment(searchBatchTextBox, HasVerticalAlignment.ALIGN_MIDDLE);
		searchPanel.setCellVerticalAlignment(searchBatchButton, HasVerticalAlignment.ALIGN_MIDDLE);
		searchPanel.setSpacing(5);

		restartPanel.add(restartOptions);
		restartPanel.setCellWidth(restartOptions, "200px");
		restartPanel.add(restartBatchButton);
		restartPanel.setCellVerticalAlignment(restartBatchButton, HasVerticalAlignment.ALIGN_MIDDLE);
		restartPanel.setCellVerticalAlignment(restartOptions, HasVerticalAlignment.ALIGN_MIDDLE);
		restartPanel.setSpacing(5);

		deleteButtonPanel.add(deleteBatchButton);
		deleteButtonPanel.setCellVerticalAlignment(deleteBatchButton, HasVerticalAlignment.ALIGN_MIDDLE);
		deleteButtonPanel.setSpacing(5);

		filterAndSearchPanel.add(filterPanel);
		filterAndSearchPanel.add(searchPanel);
		actionPanel.add(restartPanel);
		actionPanel.add(deleteButtonPanel);

		batchAlertPanel.setBorderWidth(0);
		labelPanel.addStyleName("top-box");
		setLabelsStyle("batchAlertText");
		batchAlerts.setStyleName("blk_bold_text");
		labelPanel.setSpacing(5);
		batchInstanceLabel.setText(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.LABEL_TABLE_COLUMN_STATUS)
				+ BatchInstanceConstants.COLON);
		priorityLabel.setText(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.LABEL_TABLE_COLUMN_PRIORITY)
				+ BatchInstanceConstants.COLON);
		mainLayoutPanel.addStyleName("middle");
		priorityListBox.setVisibleItemCount(3);
		batchInstanceListBox.setVisibleItemCount(3);
		clearRestartOptions();
		restartOptions.setWidth("100%");

		refreshButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				clearSearchBatchBox();
				presenter.updateTable();

			}
		});

		deleteBatchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				if (checkRowSelection()) {
					if (!checkRowSelectedIsValid(batchInstanceListView.listView.getSelectedRowIndex())) {
						ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
								BatchInstanceMessages.MSG_NON_DELETABLE));
					} else {
						showConfirmationDialog(true, batchInstanceListView.listView.getSelectedRowIndex());
					}
				}

			}
		});

		restartBatchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				if (checkRowSelection()) {
					if (!checkRowSelectedIsValid(batchInstanceListView.listView.getSelectedRowIndex())) {
						ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
								BatchInstanceMessages.MSG_NON_RESTARTABLE));
					} else {
						String identifier = batchInstanceListView.listView.getSelectedRowIndex();
						if (restartOptions.getItemText(restartOptions.getSelectedIndex()).equalsIgnoreCase(
								LocaleDictionary.get().getConstantValue(BatchInstanceConstants.SELECT))) {
							BatchInstanceDTO batchInstanceDTO = batchInstanceDTOMap.get(identifier);
							if (batchInstanceDTO.getRemoteBatchInstanceDTO() != null) {
								showRestartConfirmationDialog(identifier, batchInstanceDTO.getRemoteBatchInstanceDTO()
										.getSourceModule());
							} else {
								showRestartConfirmationDialog(identifier, null);
							}
						} else {
							showRestartConfirmationDialog(identifier, restartOptions.getValue(restartOptions.getSelectedIndex()));
						}
					}
				}
			}
		});
		searchBatchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				if (checkTextEntered()) {
					presenter.onSearchButtonClicked(searchBatchTextBox.getText().toUpperCase());
				}
			}
		});

		priorityListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent arg0) {
				clearSearchBatchBox();
				presenter.updateTable(0, batchInstanceListView.listView.getTableRowCount(), null);

			}
		});

		batchInstanceListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent arg0) {
				clearSearchBatchBox();
				presenter.updateTable(0, batchInstanceListView.listView.getTableRowCount(), null);
			}
		});

		searchBatchTextBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				clearSearchBatchBox();
			}
		});

		searchBatchTextBox.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					searchBatchButton.click();
				}
			}
		});
	}

	private void clearSearchBatchBox() {
		searchBatchTextBox.setText("");
	}

	private void setLabelsStyle(String styleName) {
		totalBatchesLabel.setStyleName(styleName);
		totalBatches.setStyleName(styleName);
		deletedBatches.setStyleName(styleName);
		deletedBatchesLabel.setStyleName(styleName);
		restartedBatches.setStyleName(styleName);
		restartedBatchesLabel.setStyleName(styleName);
	}

	private void fillBatchInstanceListBox() {
		batchInstanceListBox.addItem(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.LABEL_TABLE_DEFAULT), "-2");
		batchInstanceListBox.addItem(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.LABEL_TABLE_ALL), "-1");
		batchInstanceListBox.addItem(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.TEXT_BATCH_STATUS_NEW),
				BatchInstanceStatus.NEW.getId().toString());
		batchInstanceListBox.addItem(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.TEXT_BATCH_STATUS_LOCKED),
				BatchInstanceStatus.LOCKED.getId().toString());
		batchInstanceListBox.addItem(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.TEXT_BATCH_STATUS_READY),
				BatchInstanceStatus.READY.getId().toString());
		batchInstanceListBox.addItem(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.TEXT_BATCH_STATUS_ERROR),
				BatchInstanceStatus.ERROR.getId().toString());
		batchInstanceListBox.addItem(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.TEXT_BATCH_STATUS_FINISHED),
				BatchInstanceStatus.FINISHED.getId().toString());
		batchInstanceListBox.addItem(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.TEXT_BATCH_STATUS_RUNNING),
				BatchInstanceStatus.RUNNING.getId().toString());
		batchInstanceListBox.addItem(LocaleDictionary.get()
				.getConstantValue(BatchInstanceConstants.TEXT_BATCH_STATUS_READY_FOR_REVIEW), BatchInstanceStatus.READY_FOR_REVIEW
				.getId().toString());
		batchInstanceListBox.addItem(LocaleDictionary.get().getConstantValue(
				BatchInstanceConstants.TEXT_BATCH_STATUS_READY_FOR_VALIDATION), BatchInstanceStatus.READY_FOR_VALIDATION.getId()
				.toString());
		batchInstanceListBox.addItem(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.TEXT_BATCH_STATUS_RESTARTED),
				BatchInstanceStatus.RESTARTED.getId().toString());
		batchInstanceListBox.addItem(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.TEXT_BATCH_STATUS_DELETED),
				BatchInstanceStatus.DELETED.getId().toString());
		batchInstanceListBox.addItem(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.TEXT_BATCH_STATUS_TRANSFERRED),
				BatchInstanceStatus.TRANSFERRED.getId().toString());

		batchInstanceListBox.addItem(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.TEXT_RESTART_IN_PROGRESS),
				BatchInstanceStatus.RESTART_IN_PROGRESS.getId().toString());
	}

	private void fillPriorityListBox() {
		priorityListBox.addItem(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.LABEL_TABLE_ALL), "0");
		priorityListBox.addItem(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.TEXT_BATCH_PRIORITY_URGENT),
				BatchPriority.URGENT.getLowerLimit().toString());
		priorityListBox.addItem(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.TEXT_BATCH_PRIORITY_HIGH),
				BatchPriority.HIGH.getLowerLimit().toString());
		priorityListBox.addItem(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.TEXT_BATCH_PRIORITY_MEDIUM),
				BatchPriority.MEDIUM.getLowerLimit().toString());
		priorityListBox.addItem(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.TEXT_BATCH_PRIORITY_LOW),
				BatchPriority.LOW.getLowerLimit().toString());
	}

	public void createBatchInstanceList(Collection<BatchInstanceDTO> batchInstanceDTOs, int count) {
		List<Record> recordList = createRecords(batchInstanceDTOs);
		batchInstanceListView.listView.initTable(count, presenter, presenter, recordList, true, true);
		toggleButtons();
	}

	public void updateBatchInstance(BatchInstanceDTO batchInstanceDTO) {
		List<BatchInstanceDTO> batchInstanceDTOList = new ArrayList<BatchInstanceDTO>();
		int count = 0;
		if (null != batchInstanceDTO) {
			batchInstanceDTOList.add(batchInstanceDTO);
			count = 1;
		}
		updateBatchInstanceList(batchInstanceDTOList, 0, count);
	}

	private String[] convertPriority(int priority) {
		String[] priorityList = new String[2];
		if (BatchPriority.URGENT.getLowerLimit() <= priority && priority <= BatchPriority.URGENT.getUpperLimit()) {
			priorityList[0] = LocaleDictionary.get().getConstantValue(BatchInstanceConstants.TEXT_BATCH_PRIORITY_URGENT);
			priorityList[1] = "batch-priority-urgent";
		} else if (BatchPriority.HIGH.getLowerLimit() <= priority && priority <= BatchPriority.HIGH.getUpperLimit()) {
			priorityList[0] = LocaleDictionary.get().getConstantValue(BatchInstanceConstants.TEXT_BATCH_PRIORITY_HIGH);
			priorityList[1] = "batch-priority-high";
		} else if (BatchPriority.MEDIUM.getLowerLimit() <= priority && priority <= BatchPriority.MEDIUM.getUpperLimit()) {
			priorityList[0] = LocaleDictionary.get().getConstantValue(BatchInstanceConstants.TEXT_BATCH_PRIORITY_MEDIUM);
			priorityList[1] = "batch-priority-medium";
		} else if (BatchPriority.LOW.getLowerLimit() <= priority && priority <= BatchPriority.LOW.getUpperLimit()) {
			priorityList[0] = LocaleDictionary.get().getConstantValue(BatchInstanceConstants.TEXT_BATCH_PRIORITY_LOW);
			priorityList[1] = "batch-priority-low";
		}
		return priorityList;
	}

	public List<String> getPrioritySelected() {
		List<String> priorities = new ArrayList<String>();
		for (int i = 0; i < priorityListBox.getItemCount(); i++) {
			if (priorityListBox.isItemSelected(i)) {
				priorities.add(this.priorityListBox.getValue(i));
			}
		}
		return priorities;
	}

	public List<String> getBatchInstanceSelected() {
		List<String> batchInstances = new ArrayList<String>();
		for (int i = 0; i < batchInstanceListBox.getItemCount(); i++) {
			if (batchInstanceListBox.isItemSelected(i)) {
				batchInstances.add(this.batchInstanceListBox.getValue(i));
			}
		}
		return batchInstances;
	}

	public BatchInstanceListView getBatchInstanceListView() {
		return batchInstanceListView;
	}

	public void updateBatchInstanceList(List<BatchInstanceDTO> arg0, int startIndex, int count) {
		List<Record> list = createRecords(arg0);
		batchInstanceListView.listView.updateRecords(list, startIndex, count);
		toggleButtons();
	}

	private List<Record> createRecords(Collection<BatchInstanceDTO> batchInstanceDTOs) {
		List<Record> recordList = new LinkedList<Record>();
		batchInstanceDTOMap.clear();
		for (final BatchInstanceDTO batchInstanceDTO : batchInstanceDTOs) {
			CheckBox checkBox = new CheckBox();
			checkBox.setValue(batchInstanceDTO.isRemote());
			checkBox.setEnabled(false);
			Record record = new Record(batchInstanceDTO.getBatchIdentifier());
			String[] priority = convertPriority(batchInstanceDTO.getPriority());
			Label property = new Label(priority[0]);
			record.addWidget(batchInstanceListView.priority, property);
			property.setStyleName(priority[1]);
			record.addWidget(batchInstanceListView.batchId, new Label(batchInstanceDTO.getBatchIdentifier()));
			record.addWidget(batchInstanceListView.batchClassName, new Label(batchInstanceDTO.getBatchClassName()));
			record.addWidget(batchInstanceListView.batchName, new Label(batchInstanceDTO.getBatchName()));
			record.addWidget(batchInstanceListView.batchUpdatedOn, new Label(batchInstanceDTO.getUploadedOn()));
			record.addWidget(batchInstanceListView.status, new Label(batchInstanceDTO.getStatus()));
			record.addWidget(batchInstanceListView.isRemote, checkBox);
			recordList.add(record);
			batchInstanceDTOMap.put(batchInstanceDTO.getBatchIdentifier(), batchInstanceDTO);
		}
		if (recordList.isEmpty()) {
			disableButtons();
		}

		return recordList;
	}

	private void showRestartConfirmationDialog(final String identifier, final String moduleName) {
		final ConfirmationDialog confirmationDialog = new ConfirmationDialog();
		if (moduleName == null) {
			confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(BatchInstanceMessages.RESTART_FROM_MODULE_NAME) + " "
					+ BatchInstanceConstants.FOLDER_IMPORT_MODULE);
		} else {
			confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(BatchInstanceMessages.RESTART_FROM_MODULE_NAME) + " "
					+ moduleName);
		}
		confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.RESTART_TITLE));
		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				confirmationDialog.hide();
				presenter.onRestartBatchButtonClicked(identifier, moduleName);

			}

			@Override
			public void onCancelClick() {
				confirmationDialog.hide();

			}
		});
		confirmationDialog.center();
		confirmationDialog.show();
		confirmationDialog.okButton.setFocus(true);
	}

	private boolean checkTextEntered() {
		boolean check = true;
		if (searchBatchTextBox.getText().isEmpty()) {
			check = false;
		}
		return check;
	}

	private void showConfirmationDialog(final boolean isDelete, final String identifier) {
		final ConfirmationDialog confirmationDialog = new ConfirmationDialog();
		if (isDelete) {
			confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(BatchInstanceMessages.MSG_DELETE_CONFIRMATION_TEXT));
			confirmationDialog.setDialogTitle(LocaleDictionary.get().getMessageValue(
					BatchInstanceMessages.MSG_DELETE_CONFIRMATION_TITLE));
		} else {
			confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(BatchInstanceMessages.MSG_RESTART_CONFIRMATION_TEXT));
			confirmationDialog.setDialogTitle(LocaleDictionary.get().getMessageValue(
					BatchInstanceMessages.MSG_RESTART_CONFIRMATION_TITLE));
		}
		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				confirmationDialog.hide();
				if (isDelete) {
					presenter.onDeleteBatchButtonClicked(identifier);
				} else {
					presenter.onRestartBatchButtonClicked(identifier, restartOptions.getValue(restartOptions.getSelectedIndex()));
				}
			}

			@Override
			public void onCancelClick() {
				confirmationDialog.hide();
			}
		});
		confirmationDialog.center();
		confirmationDialog.show();
		confirmationDialog.okButton.setFocus(true);
	}

	private boolean checkRowSelection() {
		final String identifier = batchInstanceListView.listView.getSelectedRowIndex();
		int rowCount = batchInstanceListView.listView.getTableRecordCount();
		boolean rowValid = true;
		if (identifier == null || identifier.isEmpty()) {
			if (rowCount == 0) {
				/*ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchInstanceMessages.MSG_NO_RECORD));*/
			} else {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchInstanceMessages.MSG_NONE_SELECTED_WARNING));
			}
			rowValid = false;
		}
		return rowValid;
	}

	public void setTotalBatches(String totalBatches) {
		this.totalBatches.setText(totalBatches);
	}

	public void setDeletedBatches(String deletedBatches) {
		this.deletedBatches.setText(deletedBatches);
	}

	public void setRestartedBatches(String restartedBatches) {
		// this.restartedBatches.setText(restartedBatches);
	}

	public void updateBatchAlertsPanel(Integer[] countlist) {
		this.setTotalBatches(String.valueOf(countlist[0]));
		this.setDeletedBatches(String.valueOf(countlist[1]));
		this.setRestartedBatches(String.valueOf(countlist[2]));
	}

	public Map<String, BatchInstanceDTO> getBatchInstanceDTOMap() {
		return batchInstanceDTOMap;
	}

	private void toggleButtons() {
		String identifer = batchInstanceListView.listView.getSelectedRowIndex();
		if (!checkRowSelectedIsValid(identifer) || checkRemotelyExecutingBatch(identifer)) {
			disableButtons();
		} else if (checkRemoteBatch(identifer)) {
			disableDeleteButtons();
		} else {
			enableButtons();
		}
	}

	public boolean checkRowSelectedIsValid(String identifer) {
		BatchInstanceDTO batchInstanceDTO = batchInstanceDTOMap.get(identifer);
		boolean isValidRow = false;
		if (null != batchInstanceDTO) {
			if (ActionableStatus.valuesAsString().contains(batchInstanceDTO.getStatus()))
				isValidRow = true;
		}
		return isValidRow;
	}

	public boolean checkRemoteBatch(String identifer) {
		BatchInstanceDTO batchInstanceDTO = batchInstanceDTOMap.get(identifer);
		boolean isRemoteRow = false;
		if (null != batchInstanceDTO) {
			if (batchInstanceDTO.getRemoteBatchInstanceDTO() != null) {
				if (batchInstanceDTO.getRemoteBatchInstanceDTO().getPreviousRemoteURL() != null
						|| batchInstanceDTO.getRemoteBatchInstanceDTO().getRemoteURL() != null) {
					isRemoteRow = true;
				}
			}
		}
		return isRemoteRow;
	}

	public boolean checkRemotelyExecutingBatch(String identifer) {
		BatchInstanceDTO batchInstanceDTO = batchInstanceDTOMap.get(identifer);
		boolean isRemoteRow = false;
		if (null != batchInstanceDTO) {
			if (batchInstanceDTO.isRemote()) {
				isRemoteRow = true;
			}
		}
		return isRemoteRow;
	}

	public void disableButtons() {
		restartBatchButton.setEnabled(false);
		deleteBatchButton.setEnabled(false);
		restartBatchButton.removeStyleName(BatchInstanceConstants.STYLE_BUTTON);
		deleteBatchButton.removeStyleName(BatchInstanceConstants.STYLE_BUTTON);
		restartBatchButton.addStyleName(BatchInstanceConstants.DISABLED_BUTTON_STYLE);
		deleteBatchButton.addStyleName(BatchInstanceConstants.DISABLED_BUTTON_STYLE);
		restartOptions.setEnabled(false);
		clearRestartOptions();
	}

	public void disableDeleteButtons() {
		deleteBatchButton.setEnabled(false);
		deleteBatchButton.removeStyleName(BatchInstanceConstants.STYLE_BUTTON);
		deleteBatchButton.addStyleName(BatchInstanceConstants.DISABLED_BUTTON_STYLE);
		restartBatchButton.setEnabled(true);
		restartBatchButton.removeStyleName(BatchInstanceConstants.DISABLED_BUTTON_STYLE);
		restartBatchButton.addStyleName(BatchInstanceConstants.STYLE_BUTTON);
		restartOptions.setEnabled(true);
		clearRestartOptions();
	}

	public void enableButtons() {
		restartBatchButton.setEnabled(true);
		deleteBatchButton.setEnabled(true);
		restartBatchButton.removeStyleName(BatchInstanceConstants.DISABLED_BUTTON_STYLE);
		deleteBatchButton.removeStyleName(BatchInstanceConstants.DISABLED_BUTTON_STYLE);
		restartBatchButton.addStyleName(BatchInstanceConstants.STYLE_BUTTON);
		deleteBatchButton.addStyleName(BatchInstanceConstants.STYLE_BUTTON);
		restartOptions.setEnabled(true);
		clearRestartOptions();
	}

	public void clearRestartOptions() {
		restartOptions.clear();
		String selectValue = LocaleDictionary.get().getConstantValue(BatchInstanceConstants.SELECT);
		restartOptions.addItem(selectValue);

	}

	public void setPriorityListBox(int priority) {

		if (BatchPriority.URGENT.getLowerLimit() <= priority && priority <= BatchPriority.URGENT.getUpperLimit()) {
			priorityListBox.setItemSelected(1, true);
		} else if (BatchPriority.HIGH.getLowerLimit() <= priority && priority <= BatchPriority.HIGH.getUpperLimit()) {
			priorityListBox.setItemSelected(2, true);
		} else if (BatchPriority.MEDIUM.getLowerLimit() <= priority && priority <= BatchPriority.MEDIUM.getUpperLimit()) {
			priorityListBox.setItemSelected(3, true);
		} else if (BatchPriority.LOW.getLowerLimit() <= priority && priority <= BatchPriority.LOW.getUpperLimit()) {
			priorityListBox.setItemSelected(4, true);
		}
	}

	public void setBatchInstanceListBox(String status) {
		int index = 2;
		while (!batchInstanceListBox.getValue(index++).equals(BatchInstanceStatus.valueOf(status).getId().toString()))
			;
		batchInstanceListBox.setItemSelected(index - 1, true);
	}

	public void showErrorRetrievingRestartOptions(String batchInstanceIdentifier) {
		final ConfirmationDialog confirmationDialog = new ConfirmationDialog(Boolean.TRUE);
		confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(BatchInstanceMessages.RESTART_OPTIONS_FAILURE,
				batchInstanceIdentifier));
		confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.FAILURE));
		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				confirmationDialog.hide();

			}

			@Override
			public void onCancelClick() {
				confirmationDialog.hide();
			}
		});
		confirmationDialog.center();
		confirmationDialog.show();
		confirmationDialog.okButton.setFocus(true);
		ScreenMaskUtility.unmaskScreen();
	}

	public void setRestartOptions(Map<String, String> restartOptionsList) {
		clearRestartOptions();
		if (restartOptionsList != null) {
			Set<String> restartOptionKeySet = restartOptionsList.keySet();
			for (String restartOptionKey : restartOptionKeySet) {
				restartOptions.addItem(restartOptionsList.get(restartOptionKey), restartOptionKey);
			}
		} else {
			showErrorRetrievingRestartOptions(batchInstanceListView.listView.getSelectedRowIndex());
		}
	}

	public void clearFilters() {
		priorityListBox.setSelectedIndex(-1);
		batchInstanceListBox.setSelectedIndex(-1);
	}

	public void setDefaultFilters() {
		priorityListBox.setSelectedIndex(0);
		batchInstanceListBox.setSelectedIndex(0);

	}

	public void performRestartOptionsPopulate() {
		if (checkRowSelection()) {
			if (checkRowSelectedIsValid(batchInstanceListView.listView.getSelectedRowIndex())) {
				presenter.getRestartOptions(batchInstanceListView.listView.getSelectedRowIndex());
			}
		}
	}
}
