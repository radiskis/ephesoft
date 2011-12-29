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

package com.ephesoft.dcma.gwt.batchInstance.client.presenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.gwt.batchInstance.client.BatchInstanceController;
import com.ephesoft.dcma.gwt.batchInstance.client.i18n.BatchInstanceConstants;
import com.ephesoft.dcma.gwt.batchInstance.client.i18n.BatchInstanceMessages;
import com.ephesoft.dcma.gwt.batchInstance.client.view.BatchInstanceView;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.PaginationListner;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.RowSelectionListner;
import com.ephesoft.dcma.gwt.core.shared.BatchInstanceDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.DataFilter;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.ephesoft.dcma.gwt.core.shared.exception.GWTException;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class BatchInstancePresenter extends AbstractBatchInstancePresenter<BatchInstanceView> implements PaginationListner,
		RowSelectionListner {

	private List<DataFilter> filters = new ArrayList<DataFilter>();

	private static int TABLE_ROW_COUNT = 15;

	private int startIndex;
	private int maxResult;
	private Order order;

	public enum ActionableStatus {

		ERROR(4), READY_FOR_REVIEW(9), READY_FOR_VALIDATION(10), RUNNING(8);

		private Integer id;

		private ActionableStatus(int statusId) {
			id = statusId;
		}

		public static List<ActionableStatus> valuesAsList() {
			return Arrays.asList(values());
		}

		public Integer getId() {
			return id;
		}

		public static List<String> valuesAsString() {
			List<String> valuesAsString = new ArrayList<String>();
			for (ActionableStatus actionableStatus : ActionableStatus.valuesAsList()) {
				valuesAsString.add(actionableStatus.name());
			}
			return valuesAsString;
		}
	}

	public enum Results {
		SUCCESSFUL, FAILURE;
	}

	public BatchInstancePresenter(BatchInstanceController controller, BatchInstanceView view) {
		super(controller, view);
		view.getBatchInstanceListView().listView.setTableRowCount(TABLE_ROW_COUNT);
		bind();
	}

	@Override
	public void bind() {
		setIndexes(0, view.getBatchInstanceListView().listView.getTableRowCount(), null);
		getRowCount(filters, true, startIndex, maxResult, order);
	}

	private void getRowCount(List<DataFilter> dataFilters, final int start, final int maxResults, Order order) {
		getRowCount(dataFilters, false, start, maxResults, order);
	}

	private void getRowCount(final List<DataFilter> filters, final boolean initialize, final int start, final int maxResults,
			final Order order) {
		controller.getRpcService().getRowCount(filters, new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchInstanceMessages.MSG_BATCH_LIST_RETRIEVE_FAILURE));
			}

			@Override
			public void onSuccess(Integer arg0) {
				getBatchInstanceRows(start, maxResults, filters, order, !initialize, arg0);
			}
		});
	}

	private void getBatchInstanceRows(final int start, final int maxResult, List<DataFilter> filters, Order order,
			final boolean update, final int count) {
		ScreenMaskUtility.maskScreen();
		controller.getRpcService().getBatchInstanceDTOs(start, maxResult, filters, order, new AsyncCallback<List<BatchInstanceDTO>>() {

			@Override
			public void onFailure(Throwable arg0) {
				ScreenMaskUtility.unmaskScreen();
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchInstanceMessages.MSG_BATCH_LIST_RETRIEVE_FAILURE));
			}

			@Override
			public void onSuccess(final List<BatchInstanceDTO> arg0) {
				controller.getRpcService().getIndividualRowCount(new AsyncCallback<Integer[]>() {

					@Override
					public void onFailure(Throwable paramThrowable) {
						ScreenMaskUtility.unmaskScreen();
						ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
								BatchInstanceMessages.MSG_BATCH_LIST_RETRIEVE_FAILURE));
					}

					@Override
					public void onSuccess(Integer[] countlist) {
						ScreenMaskUtility.unmaskScreen();
						view.updateBatchAlertsPanel(countlist);
						if (update) {
							view.updateBatchInstanceList(arg0, start, count);
						} else {
							view.createBatchInstanceList(arg0, count);
						}
					}
				});
			}
		});
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {

	}

	public void updateTable(int startIndex, int maxResult, Order order) {
		List<String> priorities = view.getPrioritySelected();
		List<String> batchInstances = view.getBatchInstanceSelected();
		filters.clear();
		for (String priority : priorities) {
			filters.add(new DataFilter(BatchInstanceConstants.PRIORITY, priority));
		}
		for (String batchInstance : batchInstances) {
			filters.add(new DataFilter(BatchInstanceConstants.STATUS, batchInstance));
		}
		setIndexes(startIndex, maxResult, order);
		getRowCount(filters, this.startIndex, this.maxResult, this.order);
	}

	public void updateTable() {
		List<String> priorities = view.getPrioritySelected();
		List<String> batchInstances = view.getBatchInstanceSelected();
		if (priorities.isEmpty() && batchInstances.isEmpty()) {
			view.setDefaultFilters();
		}
		filters.clear();
		for (String priority : priorities) {
			filters.add(new DataFilter("priority", priority));
		}
		for (String batchInstance : batchInstances) {
			filters.add(new DataFilter("status", batchInstance));
		}
		getRowCount(filters, this.startIndex, this.maxResult, this.order);
	}

	private void setIndexes(int startIndex2, int maxResult2, Order order2) {
		this.startIndex = startIndex2;
		this.maxResult = maxResult2;
		this.order = order2;
	}

	@Override
	public void onPagination(int startIndex, int maxResult, Order order) {
		setIndexes(startIndex, maxResult, order);
		getRowCount(filters, startIndex, maxResult, order);

	}

	public void onDeleteBatchButtonClicked(final String identifier) {
		// ScreenMaskUtility.maskScreen();
		try {
			controller.getRpcService().deleteBatchInstance(identifier, new AsyncCallback<Results>() {

				@Override
				public void onFailure(Throwable arg0) {
					// ScreenMaskUtility.unmaskScreen();
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							BatchInstanceMessages.MSG_DELETE_FAILURE));
				}

				@Override
				public void onSuccess(Results arg0) {
					if (arg0.name().equals(Results.SUCCESSFUL.name())) {
						showSuccessConfirmation(LocaleDictionary.get().getMessageValue(BatchInstanceMessages.MSG_DELETE_SUCCESSFUL,
								identifier), startIndex, maxResult, order);
						controller.getRpcService().deleteBatchFolders(identifier, new AsyncCallback<Results>() {
							@Override
							public void onFailure(Throwable arg0) {
								// ScreenMaskUtility.unmaskScreen();
							}
							@Override
							public void onSuccess(Results arg0) {
								
							}
						});
					} else {
						ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
								BatchInstanceMessages.MSG_DELETE_FAILURE));
					}
				}
			});
		} catch (GWTException e) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchInstanceMessages.MSG_DELETE_ERROR, e.getMessage()));
		}
	}

	private void showSuccessConfirmation(final String messageValue, final int startIndex, final int maxResult, final Order order) {
		final ConfirmationDialog confirmationDialog = new ConfirmationDialog(Boolean.TRUE);
		confirmationDialog.setMessage(messageValue);
		confirmationDialog.setDialogTitle(LocaleDictionary.get().getMessageValue(BatchInstanceMessages.SUCCESS));
		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				// ScreenMaskUtility.unmaskScreen();
				updateTable(startIndex, maxResult, order);
			}

			@Override
			public void onCancelClick() {
				ScreenMaskUtility.unmaskScreen();
			}
		});
		confirmationDialog.center();
		confirmationDialog.show();
		confirmationDialog.okButton.setFocus(true);
	}

	public void onRestartBatchButtonClicked(final String identifier, final String module) {
		controller.getRpcService().updateBatchInstanceStatus(identifier, BatchInstanceStatus.RESTART_IN_PROGRESS,
				new AsyncCallback<Results>() {

					@Override
					public void onFailure(Throwable arg0) {
						ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
								BatchInstanceMessages.MSG_RESTART_FAILURE, identifier));
					}

					@Override
					public void onSuccess(Results arg0) {
						ConfirmationDialogUtil.showConfirmationDialogSuccess(LocaleDictionary.get().getMessageValue(
								BatchInstanceMessages.MSG_RESTART_SUCCESSFUL, identifier));

						try {
							controller.getRpcService().restartBatchInstance(identifier, module, new AsyncCallback<Results>() {

								@Override
								public void onFailure(Throwable arg0) {

								}

								@Override
								public void onSuccess(Results arg0) {
									updateTable(startIndex, maxResult, order);

								}
							});
						} catch (GWTException e) {

						}

					}
				});

	}

	@Override
	public void onRowSelected(String identifer) {
		view.restartOptions.setSelectedIndex(0);
		view.performRestartOptionsPopulate();
		if (!view.checkRowSelectedIsValid(identifer) || view.checkRemotelyExecutingBatch(identifer)) {
			view.disableButtons();
		} else if (view.checkRemoteBatch(identifer)) {
			view.disableDeleteButtons();
		} else {
			view.enableButtons();
		}
	}

	public void onSearchButtonClicked(String identifier) {
		ScreenMaskUtility.maskScreen();
		try {
			controller.getRpcService().getBatchInstanceDTOs(identifier, new AsyncCallback<List<BatchInstanceDTO>>() {

				@Override
				public void onSuccess(List<BatchInstanceDTO> batchInstanceDTOs) {
					view.clearFilters();
					for (BatchInstanceDTO batchInstanceDTO : batchInstanceDTOs) {
						if (null != batchInstanceDTO) {
							view.setPriorityListBox(batchInstanceDTO.getPriority());
							view.setBatchInstanceListBox(batchInstanceDTO.getStatus());
						}
					}
					ScreenMaskUtility.unmaskScreen();
					view.updateBatchInstanceList(batchInstanceDTOs, 0, batchInstanceDTOs.size());
				}

				@Override
				public void onFailure(Throwable arg0) {
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							BatchInstanceMessages.MSG_SEARCH_FAILURE));
					ScreenMaskUtility.unmaskScreen();
				}

			});
		} catch (GWTException e) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchInstanceMessages.MSG_SEARCH_ERROR, e.getMessage()));
		}

	}

	public void getRestartOptions(final String batchInstanceIdentifier) {
		controller.getRpcService().getRestartOptions(batchInstanceIdentifier, new AsyncCallback<Map<String, String>>() {

			@Override
			public void onFailure(Throwable paramThrowable) {
				view.showErrorRetrievingRestartOptions(batchInstanceIdentifier);
			}

			@Override
			public void onSuccess(Map<String, String> restartOptionsList) {
				view.setRestartOptions(restartOptionsList);
			}
		});
	}
}
