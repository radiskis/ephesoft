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

package com.ephesoft.dcma.gwt.batchinstance.client.presenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.gwt.batchinstance.client.BatchInstanceController;
import com.ephesoft.dcma.gwt.batchinstance.client.i18n.BatchInstanceConstants;
import com.ephesoft.dcma.gwt.batchinstance.client.i18n.BatchInstanceMessages;
import com.ephesoft.dcma.gwt.batchinstance.client.view.BatchInstanceListView;
import com.ephesoft.dcma.gwt.batchinstance.client.view.BatchInstanceView;
import com.ephesoft.dcma.gwt.core.client.EphesoftAsyncCallback;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.PaginationListner;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.RowSelectionListner;
import com.ephesoft.dcma.gwt.core.shared.BatchInstanceDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchPriority;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.DataFilter;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The presenter for view that shows the batch instance details.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.batchinstance.client.presenter.AbstractBatchInstancePresenter
 */
public class BatchInstancePresenter extends AbstractBatchInstancePresenter<BatchInstanceView> implements PaginationListner,
		RowSelectionListner {

	/**
	 * filters List<DataFilter>.
	 */
	private final List<DataFilter> filters = new ArrayList<DataFilter>();

	/**
	 * TABLE_ROW_COUNT int.
	 */
	private static final int TABLE_ROW_COUNT = 15;

	/**
	 * REVIEW_VALIDATE_HTML String.
	 */
	private static final String REVIEW_VALIDATE_HTML = "/ReviewValidate.html";

	/**
	 * BATCH_ID_URL String.
	 */
	private static final String BATCH_ID_URL = "?batch_id=";

	/**
	 * startIndex int.
	 */
	private int startIndex;

	/**
	 * maxResult int.
	 */
	private int maxResult;

	/**
	 * order Order.
	 */
	private Order order;

	/**
	 * Enum that handles the action status the batch is in.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 */
	public enum ActionableStatus {

		/**
		 * ERROR.
		 */
		ERROR(4),
		/**
		 * READY_FOR_REVIEW.
		 */
		READY_FOR_REVIEW(9),
		/**
		 * READY_FOR_VALIDATION.
		 */
		READY_FOR_VALIDATION(10),
		/**
		 * RUNNING.
		 */
		RUNNING(8);

		/**
		 * identifier Integer.
		 */
		private Integer identifier;

		private ActionableStatus(final int statusId) {
			identifier = statusId;
		}

		/**
		 * To get values as List.
		 * 
		 * @return List<ActionableStatus>
		 */
		public static List<ActionableStatus> valuesAsList() {
			return Arrays.asList(values());
		}

		/**
		 * To get id.
		 * 
		 * @return Integer
		 */
		public Integer getId() {
			return identifier;
		}

		/**
		 * To get values as strings.
		 * 
		 * @return List<String>
		 */
		public static List<String> valuesAsString() {
			final List<String> valuesAsString = new ArrayList<String>();
			for (final ActionableStatus actionableStatus : ActionableStatus.valuesAsList()) {
				valuesAsString.add(actionableStatus.name());
			}
			return valuesAsString;
		}
	}

	/**
	 * Enum for result.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 */
	public enum Results {

		/**
		 * SUCCESSFUL.
		 */
		SUCCESSFUL,
		/**
		 * FAILURE.
		 */
		FAILURE;
	}

	/**
	 * Constructor.
	 * 
	 * @param controller BatchInstanceController
	 * @param view BatchInstanceView
	 */
	public BatchInstancePresenter(final BatchInstanceController controller, final BatchInstanceView view) {
		super(controller, view);
		view.getBatchInstanceListView().listView.setTableRowCount(TABLE_ROW_COUNT);
		bind();
	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public final void bind() {
		setIndexes(0, view.getBatchInstanceListView().listView.getTableRowCount(), null);
		getRowCount(filters, true, startIndex, maxResult, order, null);
		enableRestartAll();
	}

	/**
	 * To enable all the batches to restart.
	 */
	public final void enableRestartAll() {
		controller.getRpcService().isRestartAllBatchEnabled(new EphesoftAsyncCallback<Boolean>() {

			@Override
			public void customFailure(final Throwable arg0) {
				// Empty method.
			}

			@Override
			public void onSuccess(final Boolean arg0) {
				if (arg0) {
					view.getRestartAllButton().setEnabled(true);
				}
			}
		});
	}

	private void getRowCount(final List<DataFilter> dataFilters, final int start, final int maxResults, final Order order) {
		getRowCount(dataFilters, false, start, maxResults, order, view.getSearchTextEntered());
	}

	private void getRowCount(final List<DataFilter> filters, final boolean initialize, final int start, final int maxResults,
			final Order order, final String searchString) {
		controller.getRpcService().getRowCount(filters, searchString, new EphesoftAsyncCallback<Integer>() {

			@Override
			public void customFailure(final Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchInstanceMessages.MSG_BATCH_LIST_RETRIEVE_FAILURE), Boolean.TRUE);
			}

			@Override
			public void onSuccess(final Integer arg0) {
				getBatchInstanceRows(start, maxResults, filters, order, !initialize, arg0);
			}
		});
	}

	private void getBatchInstanceRows(final int start, final int maxResult, final List<DataFilter> filters, final Order order,
			final boolean update, final int count) {
		ScreenMaskUtility.maskScreen();
		controller.getRpcService().getBatchInstanceDTOs(start, maxResult, filters, order, view.getSearchTextEntered(),
				new EphesoftAsyncCallback<List<BatchInstanceDTO>>() {

					@Override
					public void customFailure(final Throwable arg0) {
						ScreenMaskUtility.unmaskScreen();
						ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
								BatchInstanceMessages.MSG_BATCH_LIST_RETRIEVE_FAILURE), Boolean.TRUE);
					}

					@Override
					public void onSuccess(final List<BatchInstanceDTO> arg0) {
						controller.getRpcService().getIndividualRowCount(new EphesoftAsyncCallback<Integer[]>() {

							@Override
							public void customFailure(final Throwable paramThrowable) {
								ScreenMaskUtility.unmaskScreen();
								ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
										BatchInstanceMessages.MSG_BATCH_LIST_RETRIEVE_FAILURE), Boolean.TRUE);
							}

							@Override
							public void onSuccess(final Integer[] countlist) {
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

	/**
	 * To handle events.
	 * 
	 * @param eventBus HandlerManager
	 */
	@Override
	public void injectEvents(final HandlerManager eventBus) {
		// Empty method.
	}

	/**
	 * To update the table.
	 * 
	 * @param startIndex int
	 * @param maxResult int
	 * @param order Order
	 */
	public void updateTable(final int startIndex, final int maxResult, final Order order) {
		final List<String> priorities = view.getPrioritySelected();
		final List<String> batchInstances = view.getBatchInstanceSelected();
		filters.clear();
		for (final String priority : priorities) {
			filters.add(new DataFilter(BatchInstanceConstants.PRIORITY, priority));
		}
		for (final String batchInstance : batchInstances) {
			filters.add(new DataFilter(BatchInstanceConstants.STATUS, batchInstance));
		}
		setIndexes(startIndex, maxResult, order);
		getRowCount(filters, this.startIndex, this.maxResult, this.order);
	}

	/**
	 * To update the table.
	 */
	public void updateTable() {
		final List<String> priorities = view.getPrioritySelected();
		final List<String> batchInstances = view.getBatchInstanceSelected();
		if (priorities.isEmpty() && batchInstances.isEmpty()) {
			view.setDefaultFilters();
		}
		filters.clear();
		for (final String priority : priorities) {
			filters.add(new DataFilter(BatchInstanceConstants.PRIORITY, priority));
		}
		for (final String batchInstance : batchInstances) {
			filters.add(new DataFilter(BatchInstanceConstants.STATUS, batchInstance));
		}
		getRowCount(filters, this.startIndex, this.maxResult, this.order);
	}

	private void setIndexes(final int startIndex2, final int maxResult2, final Order order2) {
		this.startIndex = startIndex2;
		this.maxResult = maxResult2;
		this.order = order2;
	}

	/**
	 * To set index on pagination.
	 * 
	 * @param startIndex int
	 * @param maxResult int
	 * @param order Order
	 */
	@Override
	public void onPagination(final int startIndex, final int maxResult, final Order order) {
		setIndexes(startIndex, maxResult, order);
		getRowCount(filters, startIndex, maxResult, order);
	}

	/**
	 * To perform operations on delete batch button clicked.
	 * 
	 * @param identifier String
	 */
	public void onDeleteBatchButtonClicked(final String identifier) {
		// ScreenMaskUtility.maskScreen();

		controller.getRpcService().deleteBatchInstance(identifier, new EphesoftAsyncCallback<Results>() {

			@Override
			public void customFailure(final Throwable arg0) {
				// ScreenMaskUtility.unmaskScreen();
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchInstanceMessages.MSG_DELETE_FAILURE), Boolean.TRUE);
			}

			@Override
			public void onSuccess(final Results arg0) {
				if (arg0.name().equals(Results.SUCCESSFUL.name())) {
					showSuccessConfirmation(LocaleDictionary.get().getMessageValue(BatchInstanceMessages.MSG_DELETE_SUCCESSFUL,
							identifier), startIndex, maxResult, order);
					controller.getRpcService().deleteBatchFolders(identifier, new EphesoftAsyncCallback<Results>() {

						@Override
						public void customFailure(final Throwable arg0) {
							// ScreenMaskUtility.unmaskScreen();
						}

						@Override
						public void onSuccess(final Results arg0) {
							// Empty method.
						}
					});
				} else {
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							BatchInstanceMessages.MSG_DELETE_FAILURE), Boolean.TRUE);
				}
			}
		});
	}

	/**
	 * To perform operations on unlock batch button clicked.
	 * 
	 * @param identifier String
	 */
	public void onUnlockButtonClicked(final String identifier) {
		controller.getRpcService().clearCurrentUser(identifier, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable arg0) {

			}

			@Override
			public void onSuccess(Void arg0) {
				ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialogSuccess(LocaleDictionary.get()
						.getMessageValue(BatchInstanceMessages.MSG_CLEAR_CURRENT_USER));
				confirmationDialog.addDialogListener(new DialogListener() {

					@Override
					public void onOkClick() {
						updateTable();
					}

					@Override
					public void onCancelClick() {

					}
				});
			}
		});
	}

	private void showSuccessConfirmation(final String messageValue, final int startIndex, final int maxResult, final Order order) {
		final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(messageValue, LocaleDictionary
				.get().getMessageValue(BatchInstanceMessages.SUCCESS), Boolean.TRUE, Boolean.TRUE);

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

	}

	/**
	 * To perform operations on restart batch button clicked.
	 * 
	 * @param identifier String
	 * @param module String
	 */
	public void onRestartBatchButtonClicked(final String identifier, final String module) {
		controller.getRpcService().updateBatchInstanceStatus(identifier, BatchInstanceStatus.RESTART_IN_PROGRESS,
				new EphesoftAsyncCallback<Results>() {

					@Override
					public void customFailure(final Throwable arg0) {
						ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
								BatchInstanceMessages.MSG_RESTART_FAILURE, identifier), Boolean.TRUE);
					}

					@Override
					public void onSuccess(final Results arg0) {
						ConfirmationDialogUtil.showConfirmationDialogSuccess(LocaleDictionary.get().getMessageValue(
								BatchInstanceMessages.MSG_RESTART_SUCCESSFUL, identifier), Boolean.TRUE);

						controller.getRpcService().restartBatchInstance(identifier, module, new EphesoftAsyncCallback<Results>() {

							@Override
							public void customFailure(final Throwable arg0) {
								// Empty method.
							}

							@Override
							public void onSuccess(final Results arg0) {
								updateTable(startIndex, maxResult, order);

							}
						});

					}
				});

	}

	/**
	 * To perform operations on rows selected.
	 * 
	 * @param identifer String
	 */
	@Override
	public void onRowSelected(final String identifer) {
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

	/**
	 * To perform operations on search button clicked.
	 * 
	 * @param identifier String
	 */
	public void onSearchButtonClicked(final String identifier) {
		ScreenMaskUtility.maskScreen();

		controller.getRpcService().getBatchInstanceDTOs(identifier, new EphesoftAsyncCallback<List<BatchInstanceDTO>>() {

			@Override
			public void onSuccess(final List<BatchInstanceDTO> batchInstanceDTOs) {
				view.clearFilters();
				filters.clear();
				for (final BatchInstanceDTO batchInstanceDTO : batchInstanceDTOs) {
					if (null != batchInstanceDTO) {
						view.setPriorityListBox(batchInstanceDTO.getPriority());
						view.setBatchInstanceListBox(batchInstanceDTO.getStatus());

						// Adds default selection in filters
						addPriorityAndStatusInFilters(batchInstanceDTO);
					}
				}
				ScreenMaskUtility.unmaskScreen();

				final int listSize = batchInstanceDTOs.size();
				final int viewRecordsSize = Math.min(listSize, maxResult);
				view.updateBatchInstanceList(batchInstanceDTOs.subList(0, viewRecordsSize), 0, listSize);
			}

			@Override
			public void customFailure(final Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchInstanceMessages.MSG_SEARCH_FAILURE));
				ScreenMaskUtility.unmaskScreen();
			}

		});

	}

	/**
	 * To get restart options.
	 * 
	 * @param batchInstanceIdentifier String
	 */
	public void getRestartOptions(final String batchInstanceIdentifier) {
		controller.getRpcService().getRestartOptions(batchInstanceIdentifier, new EphesoftAsyncCallback<Map<String, String>>() {

			@Override
			public void customFailure(final Throwable paramThrowable) {
				view.showErrorRetrievingRestartOptions(batchInstanceIdentifier);
			}

			@Override
			public void onSuccess(final Map<String, String> restartOptionsList) {
				view.setRestartOptions(restartOptionsList);
			}
		});
	}

	/**
	 * To perform operations on restart all button clicked.
	 */
	public void onRestartAllButtonClicked() {
		ScreenMaskUtility.maskScreen(LocaleDictionary.get().getMessageValue(BatchInstanceMessages.MSG_RESTART_ALL_TEXT));
		view.disableRestartAllButton();
		controller.getRpcService().restartAllBatchInstances(new EphesoftAsyncCallback<Void>() {

			@Override
			public void customFailure(final Throwable paramThrowable) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchInstanceMessages.MSG_RESTART_ALL_FAILURE), Boolean.TRUE);
				ScreenMaskUtility.unmaskScreen();
				view.enableRestartAllButton();
			}

			@Override
			public void onSuccess(final Void arg0) {
				controller.getRpcService().disableRestartAllButton(new EphesoftAsyncCallback<Void>() {

					@Override
					public void customFailure(final Throwable paramThrowable) {
						ScreenMaskUtility.unmaskScreen();
					}

					@Override
					public void onSuccess(final Void arg0) {
						updateTable();
						ScreenMaskUtility.unmaskScreen();
					}
				});
			}
		});
	}

	/**
	 * To get Batch Instance DTO.
	 * 
	 * @param isDelete boolean
	 * @param batchIdentifier String
	 */
	public void getBatchInstanceDTO(final boolean isDelete, final String batchIdentifier) {
		controller.getRpcService().getBatchInstanceDTO(batchIdentifier, new EphesoftAsyncCallback<BatchInstanceDTO>() {

			@Override
			public void customFailure(final Throwable paramThrowable) {
				if (isDelete) {
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							BatchInstanceMessages.MSG_DELETE_FAILURE));
				} else {
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							BatchInstanceMessages.MSG_RESTART_FAILURE, batchIdentifier));
				}
			}

			@Override
			public void onSuccess(final BatchInstanceDTO batchInstanceDTO) {
				view.isBatchInstanceLocked(isDelete, batchInstanceDTO);
			}
		});
	}

	/**
	 * API for redirecting user to batch detail page for selected batch.
	 * 
	 * @param batchInstanceIdentifier String
	 */
	public void redirectToBatchDetailPage(final String batchInstanceIdentifier) {
		if (batchInstanceIdentifier != null && !batchInstanceIdentifier.isEmpty()) {
			final String href = Window.Location.getHref();
			final String baseUrl = href.substring(0, href.lastIndexOf('/'));
			final StringBuffer newUrl = new StringBuffer();
			newUrl.append(baseUrl).append(REVIEW_VALIDATE_HTML);
			newUrl.append(BATCH_ID_URL).append(batchInstanceIdentifier);
			Window.open(newUrl.toString(), "_blank", "");
		}
	}

	/**
	 * To perform operations on Delete all Batch Button Clicked.
	 * 
	 * @param statusFilters List<DataFilter>
	 */
	public void onDeleteAllBatchButtonClick(final List<DataFilter> statusFilters) {
		ScreenMaskUtility.maskScreen();
		controller.getRpcService().deleteAllBatchInstancesByStatus(statusFilters, new EphesoftAsyncCallback<List<String>>() {

			@Override
			public void customFailure(final Throwable arg0) {
				ScreenMaskUtility.unmaskScreen();
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchInstanceMessages.MSG_DELETE_ALL_FAILURE));
			}

			@Override
			public void onSuccess(final List<String> batchInstanceId) {
				view.clearSearchBatchBox();
				if (!batchInstanceId.isEmpty()) {
					showSuccessConfirmation(LocaleDictionary.get().getMessageValue(BatchInstanceMessages.MSG_DELETE_ALL_SUCCESS),
							startIndex, maxResult, order);
					ScreenMaskUtility.unmaskScreen();
					controller.getRpcService().deleteAllBatchInstancesFolders(batchInstanceId, new EphesoftAsyncCallback<Void>() {

						@Override
						public void customFailure(final Throwable arg0) {
							// Empty method.
						}

						@Override
						public void onSuccess(final Void arg0) {
							// Empty method.
						}
					});
				} else {
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							BatchInstanceMessages.MSG_DEL_LOCKED_BATCHES));
					ScreenMaskUtility.unmaskScreen();
				}
			}

		});
	}

	/**
	 * To clear user.
	 * 
	 * @param batchInstanceIdentifier String
	 */
	public void clearUser(final String batchInstanceIdentifier) {
		controller.getRpcService().clearCurrentUser(batchInstanceIdentifier, new EphesoftAsyncCallback<Void>() {

			@Override
			public void customFailure(final Throwable arg0) {
				// Empty method.
			}

			@Override
			public void onSuccess(final Void arg0) {
				// Empty method.
			}
		});

	}

	/**
	 * This API is for showing confirmation dialog box for opening batch.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param batchInstanceDTO BatchInstanceDTO
	 */
	private void openingBatchConfirmationDialog(final String batchInstanceIdentifier, final BatchInstanceDTO batchInstanceDTO) {

		final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get()
				.getMessageValue(BatchInstanceMessages.MSG_PRESS_OK_TO_NEVIGATE), LocaleDictionary.get().getMessageValue(
				BatchInstanceMessages.MSG_NAVIGATE_TO_REVIEW_VALIDATE_SCREEN), Boolean.FALSE);
		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				confirmationDialog.hide();
				redirectToBatchDetailPage(batchInstanceIdentifier);
			}

			@Override
			public void onCancelClick() {
				confirmationDialog.hide();
				batchInstanceDTO.setCurrentUser(null);
				clearUser(batchInstanceIdentifier);
			}
		});
	}

	/**
	 * This API is for getting batch instance DTO and lock the selected batch.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param batchInstanceDTOMap Map<String, BatchInstanceDTO>
	 * @param batchInstanceListView BatchInstanceListView
	 */
	public void getBatchDtoAndAcquireLock(final String batchInstanceIdentifier,
			final Map<String, BatchInstanceDTO> batchInstanceDTOMap, final BatchInstanceListView batchInstanceListView) {
		controller.getRpcService().getBatchInstanceDTO(batchInstanceIdentifier, new EphesoftAsyncCallback<BatchInstanceDTO>() {

			@Override
			public void customFailure(final Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchInstanceMessages.MSG_UNABLE_TO_RETRIEVE_BATCH));
			}

			@Override
			public void onSuccess(final BatchInstanceDTO batchInstanceDTO) {
				batchInstanceDTOMap.put(batchInstanceDTO.getBatchIdentifier(), batchInstanceDTO);
				if (batchInstanceDTO.getCurrentUser() != null && !batchInstanceDTO.getCurrentUser().isEmpty()) {
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							BatchInstanceMessages.MSG_LOCKED_BATCH));
				} else {
					controller.getRpcService().acquireLock(batchInstanceIdentifier, new EphesoftAsyncCallback<Void>() {

						@Override
						public void customFailure(final Throwable arg0) {

							ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
									BatchInstanceMessages.MSG_UNABLE_TO_GET_LOCK));
						}

						@Override
						public void onSuccess(final Void arg0) {
							openingBatchConfirmationDialog(batchInstanceListView.listView.getSelectedRowIndex(), batchInstanceDTO);
						}
					});
				}
			}
		});
	}

	/**
	 * This method adds priority and status default selection in filters.
	 * 
	 * @param batchInstanceDTO - instance of BatchInstanceDTO
	 */
	private void addPriorityAndStatusInFilters(final BatchInstanceDTO batchInstanceDTO) {
		Integer priority = null;
		String status = null;
		if (BatchPriority.URGENT.getLowerLimit() <= batchInstanceDTO.getPriority()
				&& batchInstanceDTO.getPriority() <= BatchPriority.URGENT.getUpperLimit()) {
			priority = BatchPriority.URGENT.getLowerLimit();
		} else if (BatchPriority.HIGH.getLowerLimit() <= batchInstanceDTO.getPriority()
				&& batchInstanceDTO.getPriority() <= BatchPriority.HIGH.getUpperLimit()) {
			priority = BatchPriority.HIGH.getLowerLimit();
		} else if (BatchPriority.MEDIUM.getLowerLimit() <= batchInstanceDTO.getPriority()
				&& batchInstanceDTO.getPriority() <= BatchPriority.MEDIUM.getUpperLimit()) {
			priority = BatchPriority.MEDIUM.getLowerLimit();
		} else if (BatchPriority.LOW.getLowerLimit() <= batchInstanceDTO.getPriority()
				&& batchInstanceDTO.getPriority() <= BatchPriority.LOW.getUpperLimit()) {
			priority = BatchPriority.LOW.getLowerLimit();
		}
		status = BatchInstanceStatus.valueOf(batchInstanceDTO.getStatus()).getId().toString();
		if (null != priority) {
			filters.add(new DataFilter(BatchInstanceConstants.PRIORITY, priority.toString()));
		}
		if (null != status) {
			filters.add(new DataFilter(BatchInstanceConstants.STATUS, status));
		}
	}

}
