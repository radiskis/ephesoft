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

package com.ephesoft.dcma.gwt.home.client.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.gwt.core.client.EphesoftAsyncCallback;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView;
import com.ephesoft.dcma.gwt.core.client.ui.table.Record;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.PaginationListner;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.RowSelectionListner;
import com.ephesoft.dcma.gwt.core.shared.BatchInstanceDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchPriority;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.DataFilter;
import com.ephesoft.dcma.gwt.home.client.BatchListController;
import com.ephesoft.dcma.gwt.home.client.TableModelServiceAsync;
import com.ephesoft.dcma.gwt.home.client.event.BatchListKeyDownEvent;
import com.ephesoft.dcma.gwt.home.client.event.BatchListKeyDownEventHandler;
import com.ephesoft.dcma.gwt.home.client.i18n.BatchListConstants;
import com.ephesoft.dcma.gwt.home.client.i18n.BatchListMessages;
import com.ephesoft.dcma.gwt.home.client.view.LandingView;
import com.ephesoft.dcma.gwt.home.client.view.reviewtable.ReviewValidateTable;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * The presenter for the view the user gets on reaching batch list page.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.ui.table.ListView.PaginationListner
 * @see com.ephesoft.dcma.gwt.core.client.ui.table.RowSelectionListner
 */
public class LandingPresenter extends AbstractBatchListPresenter<LandingView> implements PaginationListner, RowSelectionListner {

	/**
	 * Used to get values from the server.
	 */
	private final TableModelServiceAsync service;

	/**
	 * The table showing all the batches in review state.
	 */
	private ReviewValidateTable reviewTable;

	/**
	 * The table showing all batches in validation state.
	 */
	private ReviewValidateTable validateTable;

	/**
	 * The filter used to filter the batches. filter[0] is used to filter on basis of priority and filter[1] is used to filter on basis
	 * of status.
	 */
	private DataFilter[] filters = new DataFilter[2];

	/**
	 * The view when user gets to batch list page.
	 */
	private final LandingView view;

	/**
	 * A constant empty string.
	 */
	private static final String EMPTY_STRING = "";

	/**
	 * To indicate refresh functionality.
	 */
	private boolean isRefresh = false;

	/**
	 * Constructor.
	 * 
	 * @param view corresponding this presenter
	 * @param service to be used for server calls.
	 */
	public LandingPresenter(final BatchListController controller, final LandingView view) {
		super(controller, view);
		this.view = view;
		this.service = controller.getRpcService();
	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	public void bind() {
		reviewTable = view.getReviewTable();
		service.getBatchListTableRowCount(new EphesoftAsyncCallback<Integer>() {

			@Override
			public void customFailure(Throwable arg0) {
				/*
				 * on failure
				 */

			}

			@Override
			public void onSuccess(Integer arg0) {
				if (arg0 != null && arg0 > 0) {
					reviewTable.getListView().setTableRowCount(arg0);
				}
			}
		});
		isRefresh = false;
		validateTable = view.getValidateTable();
		final ListBox reviewTableListBox = reviewTable.getPriorityListBox();
		final ListBox validateTableListBox = validateTable.getPriorityListBox();
		service.getBatchListPriorityFilter(new EphesoftAsyncCallback<Map<BatchInstanceStatus, Integer>>() {

			public void customFailure(final Throwable caught) {
				ScreenMaskUtility.unmaskScreen();
				loadDefaultData();
				addClickHandlers();
			}

			public void onSuccess(final Map<BatchInstanceStatus, Integer> priorityFilter) {
				if (priorityFilter != null && !priorityFilter.isEmpty()) {
					Integer reviewBatchListPriority = priorityFilter.get(BatchInstanceStatus.READY_FOR_REVIEW);
					Integer validateBatchListPriority = priorityFilter.get(BatchInstanceStatus.READY_FOR_VALIDATION);
					reviewTableListBox.setSelectedIndex(reviewBatchListPriority);
					validateTableListBox.setSelectedIndex(validateBatchListPriority);
				}
				loadDefaultData();
				addClickHandlers();
			}
		});

	}

	private void addClickHandlers() {
		reviewTable.getPriorityListBox().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(final ChangeEvent arg0) {
				persistPriorityFilter(reviewTable.getPriorityListBox().getSelectedIndex(), validateTable.getPriorityListBox()
						.getSelectedIndex());
				reviewTable.getSearchBatchTextBox().setText(EMPTY_STRING);
				updateTableData(reviewTable);
			}
		});

		validateTable.getPriorityListBox().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(final ChangeEvent arg0) {
				persistPriorityFilter(reviewTable.getPriorityListBox().getSelectedIndex(), validateTable.getPriorityListBox()
						.getSelectedIndex());
				validateTable.getSearchBatchTextBox().setText(EMPTY_STRING);
				updateTableData(validateTable);
			}
		});
		reviewTable.getRefreshButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				isRefresh = true;
				refreshTable();
			}
		});
		validateTable.getRefreshButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				isRefresh = true;
				refreshTable();
			}
		});
		reviewTable.getSearchBatchButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				if (checkTextEntered(reviewTable.getSearchBatchTextBox())) {
					onSearchButtonClicked(reviewTable);
				}
			}
		});
		validateTable.getSearchBatchButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				if (checkTextEntered(validateTable.getSearchBatchTextBox())) {
					onSearchButtonClicked(validateTable);
				}
			}
		});
	}

	private void refreshTable() {

		// Empty search text box according to selected tab.
		if (view.getReviewValidateTabLayoutPanel().getSelectedIndex() == 0) {
			reviewTable.getSearchBatchTextBox().setText(EMPTY_STRING);
		} else {
			validateTable.getSearchBatchTextBox().setText(EMPTY_STRING);
		}
		ReviewValidateTable rvTable = getSelectedTable();
		populateFilters(rvTable);
		ListView listView = rvTable.getListView();
		getRowCount(listView.getStartIndex(), listView.getTableRowCount(), listView.getTableOrder(), null);
	}

	private boolean checkTextEntered(TextBox searchBatchTextBox) {
		boolean check = true;
		if (searchBatchTextBox == null || searchBatchTextBox.getText().isEmpty()) {
			check = false;
		}
		return check;
	}

	private void onSearchButtonClicked(final ReviewValidateTable selectedTable) {
		ScreenMaskUtility.maskScreen();
		selectedTable.getPriorityListBox().setSelectedIndex(0);
		populateFilters(selectedTable);
		final String batchName = selectedTable.getSearchBatchTextBox().getText();

		service.getRowsCount(batchName, filters, new EphesoftAsyncCallback<Integer>() {

			@Override
			public void customFailure(final Throwable arg0) {
				// nothing to do in case of failure.
			}

			@Override
			public void onSuccess(final Integer arg0) {

				service.getRows(batchName, 0, selectedTable.getListView().getTableRowCount(), filters, null,
						new EphesoftAsyncCallback<List<BatchInstanceDTO>>() {

							public void customFailure(final Throwable caught) {
								ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
										BatchListMessages.MSG_SEARCH_ERROR));
								ScreenMaskUtility.unmaskScreen();
							}

							public void onSuccess(final List<BatchInstanceDTO> result) {
								updateDefaultData();
								if (null != result) {
									updateTable(result, 0, null, arg0);
								}
								ScreenMaskUtility.unmaskScreen();
							}
						});
			}

		});

	}

	private void persistPriorityFilter(final int reviewBatchListPriority, final int validateBatchListPriority) {
		service.setBatchListPriorityFilter(reviewBatchListPriority, validateBatchListPriority, new EphesoftAsyncCallback<Void>() {

			public void customFailure(final Throwable caught) {
				// Priority filter cannot be saved in HTTP session.
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchListMessages.ERROR_WHILE_RETAINING_BATCH_LIST_PRIORITY));
			}

			public void onSuccess(Void result) {
				// Priority filter saved in HTTP session.
			}
		});
	}

	protected void populateFilters(final ReviewValidateTable table) {
		ListBox listBox = table.getPriorityListBox();
		int selectedIndex = listBox.getSelectedIndex();
		if (selectedIndex != 0) {
			String selectedValue = listBox.getValue(selectedIndex);
			filters[0] = new DataFilter("priority", selectedValue);
		} else {
			filters[0] = new DataFilter("priority", "0");
		}
		if (table.isReview()) {
			filters[1] = new DataFilter("status", "9");
		} else {
			filters[1] = new DataFilter("status", "10");
		}
	}

	private void updateTableData(final ReviewValidateTable table) {
		populateFilters(table);
		updateRowsCount(filters, new EphesoftAsyncCallback<Integer>() {

			public void customFailure(final Throwable caught) {
				ScreenMaskUtility.unmaskScreen();
				ConfirmationDialogUtil.showConfirmationDialogError(caught.getMessage());
			}

			public void onSuccess(final Integer result) {
				updateTable(table, result);
			}
		});
	}

	/**
	 * To update changes done on table.
	 * 
	 * @param table ReviewValidateTable
	 * @param rowCount int
	 */
	public void updateTable(final ReviewValidateTable table, final int rowCount) {
		ScreenMaskUtility.maskScreen();
		service.getRows(null, 0, table.getListView().getTableRowCount(), filters, table.getListView().getTableOrder(),
				new EphesoftAsyncCallback<List<BatchInstanceDTO>>() {

					public void customFailure(final Throwable caught) {
						ScreenMaskUtility.unmaskScreen();
						ConfirmationDialogUtil.showConfirmationDialogError(caught.getMessage());
					}

					public void onSuccess(final List<BatchInstanceDTO> result) {
						updateTable(result, 0, table, rowCount);
						ScreenMaskUtility.unmaskScreen();
					}
				});
	}

	/**
	 * To load default data.
	 */
	public void loadDefaultData() {
		ScreenMaskUtility.maskScreen();
		service.getIndividualRowCounts(new EphesoftAsyncCallback<Integer[]>() {

			@Override
			public void customFailure(final Throwable arg0) {
				ScreenMaskUtility.unmaskScreen();
				ConfirmationDialogUtil.showConfirmationDialogError(arg0.getMessage());
			}

			@Override
			public void onSuccess(final Integer[] result) {
				updateTabLabels(result);
				updateTables();
			}
		});
	}

	private void updateTables() {
		populateFilters(reviewTable);
		service.getRowsCount(filters, new EphesoftAsyncCallback<Integer>() {

			public void customFailure(final Throwable caught) {
				ScreenMaskUtility.unmaskScreen();
			}

			public void onSuccess(final Integer rowCount) {
				initializeTable(reviewTable, rowCount);
				populateFilters(validateTable);
				service.getRowsCount(filters, new EphesoftAsyncCallback<Integer>() {

					public void customFailure(final Throwable caught) {
						ScreenMaskUtility.unmaskScreen();
					}

					public void onSuccess(final Integer rowCount) {
						initializeTable(validateTable, rowCount);
					}
				});
			}
		});
	}

	/**
	 * To update default data.
	 */
	public void updateDefaultData() {
		ScreenMaskUtility.maskScreen();
		service.getIndividualRowCounts(new EphesoftAsyncCallback<Integer[]>() {

			@Override
			public void customFailure(final Throwable arg0) {
				ScreenMaskUtility.unmaskScreen();
				ConfirmationDialogUtil.showConfirmationDialogError(arg0.getMessage());
			}

			@Override
			public void onSuccess(final Integer[] result) {
				updateTabLabels(result);
				populateFilters(reviewTable);
				populateFilters(validateTable);
				ScreenMaskUtility.unmaskScreen();
			}
		});
	}

	/**
	 * This method updates table labels for review and validate screen with the given result.
	 * @param result {@link Integer} count of batch instances for review and validation
	 */
	private void updateTabLabels(final Integer[] result) {
		view.getReviewLabel().setText(EMPTY_STRING + result[0]);
		view.getValidationLabel().setText(EMPTY_STRING + result[1]);
		view.getTotalBatchLabel().setText(EMPTY_STRING + (result[0] + result[1]));

		if (isRefresh) {
			isRefresh = false;
			if (view.getReviewValidateTabLayoutPanel().getSelectedIndex() == 0) {
				updateReviewTableLabel(result[0]);
			} else {
				updateValidateTableLabel(result[1]);
			}
		} else {
			updateReviewTableLabel(result[0]);
			updateValidateTableLabel(result[1]);
		}
	}
	
	/**
	 * This method updates review table label for the given count.
	 * @param count {@link Integer} count of batch instances
	 */
	private void updateReviewTableLabel(Integer count) {
		view.getReviewValidateTabLayoutPanel().setTabText(0,
				LocaleDictionary.get().getConstantValue(BatchListConstants.LABEL_TABLE_REVIEW) + " (" + count + ")");
		view.getReviewValidateTabLayoutPanel().getTabWidget(0).setTitle(
				LocaleDictionary.get().getConstantValue(BatchListConstants.REVIEW_TAB_SHORTCUT));
	}
	
	/**
	 * This method updates validate table label for the given count.
	 * @param count {@link Integer} count of batch instances
	 */
	private void updateValidateTableLabel(Integer count) {
		view.getReviewValidateTabLayoutPanel().setTabText(1,
				LocaleDictionary.get().getConstantValue(BatchListConstants.LABEL_TABLE_VALIDATION) + " (" + count + ")");
		view.getReviewValidateTabLayoutPanel().getTabWidget(1).setTitle(
				LocaleDictionary.get().getConstantValue(BatchListConstants.VALIDATE_TAB_SHORTCUT));
	}

	private void updateRowsCount(final DataFilter[] filters, final EphesoftAsyncCallback<Integer> completedCallback) {
		ScreenMaskUtility.maskScreen();
		service.getRowsCount(filters, new EphesoftAsyncCallback<Integer>() {

			public void customFailure(final Throwable caught) {
				ScreenMaskUtility.unmaskScreen();
				completedCallback.onFailure(caught);
			}

			public void onSuccess(final Integer result) {
				completedCallback.onSuccess(result);
				ScreenMaskUtility.unmaskScreen();
			}
		});
	}

	private void initializeTable(final ReviewValidateTable table, final Integer count) {
		ScreenMaskUtility.maskScreen();
		service.getRows(null, 0, table.getListView().getTableRowCount(), filters, null, new EphesoftAsyncCallback<List<BatchInstanceDTO>>() {

			public void customFailure(final Throwable caught) {
				ScreenMaskUtility.unmaskScreen();
			}

			public void onSuccess(final List<BatchInstanceDTO> result) {
				List<Record> list = new ArrayList<Record>();
				for (BatchInstanceDTO batch : result) {
					Record record = new Record(batch.getBatchIdentifier());
					String[] priority = convertPriority(batch.getPriority());
					Label property = new Label(priority[0]);
					record.addWidget(table.priority, property);
					property.setStyleName(priority[1]);
					record.addWidget(table.batchId, new Label(batch.getBatchIdentifier()));
					record.addWidget(table.batchClassName, new Label(batch.getBatchClassName()));
					record.addWidget(table.batchName, new Label(batch.getBatchName()));
					record.addWidget(table.batchUpdatedOn, new Label(batch.getUploadedOn()));
					record.addWidget(table.batchCreatedOn, new Label(batch.getCreatedOn()));
					list.add(record);
				}
				initializeTable(table, list, count);
				ScreenMaskUtility.unmaskScreen();
			}
		});
	}

	private void initializeTable(final ReviewValidateTable table, final List<Record> list, final Integer count) {
		table.getListView().initTable(count, this, this, list, true, false, null, false);
	}

	private String[] convertPriority(final int priority) {
		String[] priorityList = new String[2];
		if (BatchPriority.URGENT.getLowerLimit() <= priority && priority <= BatchPriority.URGENT.getUpperLimit()) {
			priorityList[0] = LocaleDictionary.get().getConstantValue(BatchListConstants.BATCH_PRIORITY_URGENT);
			priorityList[1] = "batch-priority-urgent";
		} else if (BatchPriority.HIGH.getLowerLimit() <= priority && priority <= BatchPriority.HIGH.getUpperLimit()) {
			priorityList[0] = LocaleDictionary.get().getConstantValue(BatchListConstants.BATCH_PRIORITY_HIGH);
			priorityList[1] = "batch-priority-high";
		} else if (BatchPriority.MEDIUM.getLowerLimit() <= priority && priority <= BatchPriority.MEDIUM.getUpperLimit()) {
			priorityList[0] = LocaleDictionary.get().getConstantValue(BatchListConstants.BATCH_PRIORITY_MEDIUM);
			priorityList[1] = "batch-priority-medium";
		} else if (BatchPriority.LOW.getLowerLimit() <= priority && priority <= BatchPriority.LOW.getUpperLimit()) {
			priorityList[0] = LocaleDictionary.get().getConstantValue(BatchListConstants.BATCH_PRIORITY_LOW);
			priorityList[1] = "batch-priority-low";
		}
		return priorityList;
	}

	/**
	 * To perform operations on pagination.
	 * 
	 * @param startIndex int
	 * @param maxResult int
	 * @param order Order
	 */
	@Override
	public void onPagination(final int startIndex, final int maxResult, final Order order) {
		ReviewValidateTable rvTable = getSelectedTable();
		populateFilters(rvTable);
		final String batchName = rvTable.getSearchBatchTextBox().getText();
		getRowCount(startIndex, maxResult, order, batchName);
	}

	private void getRowCount(final int startIndex, final int maxResult, final Order order, final String batchName) {
		service.getRowsCount(batchName, filters, new EphesoftAsyncCallback<Integer>() {

			@Override
			public void customFailure(final Throwable arg0) {
				// nothing to do in case of failure.
			}

			@Override
			public void onSuccess(final Integer arg0) {

				service.getRows(batchName, startIndex, maxResult, filters, order, new EphesoftAsyncCallback<List<BatchInstanceDTO>>() {

					public void customFailure(final Throwable caught) {
						ScreenMaskUtility.unmaskScreen();
						ConfirmationDialogUtil.showConfirmationDialogError(caught.getMessage());
					}

					public void onSuccess(final List<BatchInstanceDTO> result) {
						updateDefaultData();
						updateTable(result, startIndex, null, arg0);
						ScreenMaskUtility.unmaskScreen();
					}
				});
			}

		});
	}

	private void updateTable(final List<BatchInstanceDTO> batchList, final int startIndex, final ReviewValidateTable selectedTable,
			final int count) {
		List<Record> list = new ArrayList<Record>();
		ReviewValidateTable table = selectedTable;
		if (selectedTable == null) {
			table = getSelectedTable();
		}
		for (BatchInstanceDTO batch : batchList) {
			Record record = new Record(batch.getBatchIdentifier());
			String[] priority = convertPriority(batch.getPriority());
			Label property = new Label(priority[0]);
			record.addWidget(table.priority, property);
			property.setStyleName(priority[1]);
			record.addWidget(table.batchId, new Label(batch.getBatchIdentifier()));
			record.addWidget(table.batchClassName, new Label(batch.getBatchClassName()));
			record.addWidget(table.batchName, new Label(batch.getBatchName()));
			record.addWidget(table.batchUpdatedOn, new Label(batch.getUploadedOn()));
			record.addWidget(table.batchCreatedOn, new Label(batch.getCreatedOn()));
			list.add(record);
		}
		table.getListView().updateRecords(list, startIndex, count);
	}

	private ReviewValidateTable getSelectedTable() {
		ReviewValidateTable returnTable = validateTable;
		if (view.getReviewValidateTabLayoutPanel().getSelectedIndex() == 0) {
			returnTable = reviewTable;
		}
		return returnTable;
	}

	@Override
	public void onRowSelected(final String identifer) {
		view.gotoReviewAndValidatePage(identifer);
	}

	/**
	 * To handle events.
	 * 
	 * @param eventBus HandlerManager
	 */
	@Override
	public void injectEvents(HandlerManager eventBus) {
		eventBus.addHandler(BatchListKeyDownEvent.TYPE, new BatchListKeyDownEventHandler() {

			@Override
			public void onKeyDown(BatchListKeyDownEvent event) {
				if (event.getEvent().isControlKeyDown()) {
					switch (event.getEvent().getNativeEvent().getKeyCode()) {
						case 'j':
						case 'J':
							event.getEvent().getNativeEvent().preventDefault();
							view.setReviewTableSelected();
							break;
						case 'k':
						case 'K':
							event.getEvent().getNativeEvent().preventDefault();
							view.setValidateTableSeleted();
							break;
						default:
							break;
					}
				}
			}
		});
	}
}
