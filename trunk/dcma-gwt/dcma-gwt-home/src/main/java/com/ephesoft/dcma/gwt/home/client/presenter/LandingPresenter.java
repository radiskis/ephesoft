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

package com.ephesoft.dcma.gwt.home.client.presenter;

import java.util.ArrayList;
import java.util.List;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
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
import com.ephesoft.dcma.gwt.home.client.view.LandingView;
import com.ephesoft.dcma.gwt.home.client.view.ReviewTable.ReviewValidateTable;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

/**
 * The presenter for the view the user gets on reaching batch list page..
 * 
 * @author Ephesoft
 * 
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

	public void bind() {
		reviewTable = view.getReviewTable();
		validateTable = view.getValidateTable();
		loadDefaultData();
		reviewTable.getPriorityListBox().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(final ChangeEvent arg0) {
				updateTableData(reviewTable);

			}
		});

		validateTable.getPriorityListBox().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(final ChangeEvent arg0) {
				updateTableData(validateTable);

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
		ScreenMaskUtility.maskScreen();
		populateFilters(table);
		updateRowsCount(filters, new AsyncCallback<Integer>() {

			public void onFailure(final Throwable caught) {
				ScreenMaskUtility.unmaskScreen();
				ConfirmationDialogUtil.showConfirmationDialogError(caught.getMessage());
			}

			public void onSuccess(final Integer result) {
				updateTable(table, result);
				ScreenMaskUtility.unmaskScreen();
			}
		});
	}

	public void updateTable(final ReviewValidateTable table, final int rowCount) {
		ScreenMaskUtility.maskScreen();
		service.getRows(0, table.getListView().getTableRowCount(), filters, null, new AsyncCallback<List<BatchInstanceDTO>>() {

			public void onFailure(final Throwable caught) {
				ScreenMaskUtility.unmaskScreen();
				ConfirmationDialogUtil.showConfirmationDialogError(caught.getMessage());
			}

			public void onSuccess(final List<BatchInstanceDTO> result) {
				updateTable(result, 0, table, rowCount);
				ScreenMaskUtility.unmaskScreen();
			}
		});
	}

	public void loadDefaultData() {
		ScreenMaskUtility.maskScreen();
		service.getIndividualRowCounts(new AsyncCallback<Integer[]>() {

			@Override
			public void onFailure(final Throwable arg0) {
				ScreenMaskUtility.unmaskScreen();
				ConfirmationDialogUtil.showConfirmationDialogError(arg0.getMessage());
			}

			@Override
			public void onSuccess(final Integer[] result) {
				udateTabLabels(result);
				populateFilters(reviewTable);
				initializeTable(reviewTable, result[0]);
				populateFilters(validateTable);
				initializeTable(validateTable, result[1]);
				ScreenMaskUtility.unmaskScreen();
			}
		});
	}

	public void updateDefaultData() {
		ScreenMaskUtility.maskScreen();
		service.getIndividualRowCounts(new AsyncCallback<Integer[]>() {

			@Override
			public void onFailure(final Throwable arg0) {
				ScreenMaskUtility.unmaskScreen();
				ConfirmationDialogUtil.showConfirmationDialogError(arg0.getMessage());
			}

			@Override
			public void onSuccess(final Integer[] result) {
				udateTabLabels(result);
				populateFilters(reviewTable);
				populateFilters(validateTable);
				ScreenMaskUtility.unmaskScreen();
			}
		});
	}

	private void udateTabLabels(final Integer[] result) {
		view.getReviewLabel().setText(EMPTY_STRING + result[0]);
		view.getValidationLabel().setText(EMPTY_STRING + result[1]);
		view.getTotalBatchLabel().setText(EMPTY_STRING + (result[0] + result[1]));
		view.getReviewValidateTabLayoutPanel().setTabText(0,
				LocaleDictionary.get().getConstantValue(BatchListConstants.LABEL_TABLE_REVIEW) + " (" + result[0] + ")");
		view.getReviewValidateTabLayoutPanel().getTabWidget(0).setTitle(
				LocaleDictionary.get().getConstantValue(BatchListConstants.REVIEW_TAB_SHORTCUT));

		view.getReviewValidateTabLayoutPanel().setTabText(1,
				LocaleDictionary.get().getConstantValue(BatchListConstants.LABEL_TABLE_VALIDATION) + " (" + result[1] + ")");
		view.getReviewValidateTabLayoutPanel().getTabWidget(1).setTitle(
				LocaleDictionary.get().getConstantValue(BatchListConstants.VALIDATE_TAB_SHORTCUT));
	}

	private void updateRowsCount(final DataFilter[] filters, final AsyncCallback<Integer> completedCallback) {
		ScreenMaskUtility.maskScreen();
		service.getRowsCount(filters, new AsyncCallback<Integer>() {

			public void onFailure(final Throwable caught) {
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
		service.getRows(0, table.getListView().getTableRowCount(), filters, null, new AsyncCallback<List<BatchInstanceDTO>>() {

			public void onFailure(final Throwable caught) {
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
					list.add(record);
				}
				initializeTable(table, list, count);
				ScreenMaskUtility.unmaskScreen();
			}
		});
	}

	private void initializeTable(final ReviewValidateTable table, final List<Record> list, final Integer count) {
		table.getListView().initTable(count, this, this, list, true, false);
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

	@Override
	public void onPagination(final int startIndex, final int maxResult, final Order order) {
		populateFilters(getSelectedTable());
		service.getRowsCount(filters, new AsyncCallback<Integer>() {

			@Override
			public void onFailure(final Throwable arg0) {
				// nothing to do in case of failure.

			}

			@Override
			public void onSuccess(final Integer arg0) {

				service.getRows(startIndex, maxResult, filters, order, new AsyncCallback<List<BatchInstanceDTO>>() {

					public void onFailure(final Throwable caught) {
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
	public BatchListController getController() {
		return super.getController();
	}

	@Override
	public void onRowSelected(final String identifer) {
		view.gotoReviewAndValidatePage(identifer);
	}

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
