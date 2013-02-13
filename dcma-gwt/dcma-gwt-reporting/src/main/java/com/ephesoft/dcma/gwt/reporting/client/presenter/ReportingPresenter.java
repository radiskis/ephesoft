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

package com.ephesoft.dcma.gwt.reporting.client.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.common.WorkflowType;
import com.ephesoft.dcma.da.property.ReportProperty;
import com.ephesoft.dcma.gwt.core.client.EphesoftAsyncCallback;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.client.ui.table.Record;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.PaginationListner;
import com.ephesoft.dcma.gwt.core.client.validator.RegExValidator;
import com.ephesoft.dcma.gwt.core.client.view.ExternalAppDialogBox;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.ReportDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.ephesoft.dcma.gwt.reporting.client.ReportingController;
import com.ephesoft.dcma.gwt.reporting.client.i18n.ReportingConstants;
import com.ephesoft.dcma.gwt.reporting.client.i18n.ReportingMessages;
import com.ephesoft.dcma.gwt.reporting.client.view.ReportingView;
import com.google.gwt.event.shared.HandlerManager;

/**
 * The presenter for the view the user gets on reaching reporting page..
 * 
 * @author Ephesoft
 * 
 */
public class ReportingPresenter extends AbstractReportingPresenter<ReportingView> implements PaginationListner {

	private int startIndex;

	private int maxResults;

	private int totalCount;

	private Order order;

	private final List<String> batchClassIdList = new ArrayList<String>();

	/**
	 * Constructor.
	 * 
	 * @param view corresponding this presenter
	 * @param service to be used for server calls.
	 */
	public ReportingPresenter(final ReportingController controller, final ReportingView view) {
		super(controller, view);
	}

	private void updateSystemStatistics() {

		// getting all roles of the logged in user
		controller.getRpcService().getUserRoles(new EphesoftAsyncCallback<Set<String>>() {

			@Override
			public void customFailure(Throwable arg0) {
				showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						ReportingMessages.UNABLE_TO_FETCH_USER_ROLES_FOR_LOGGED_IN_USER));
			}

			@Override
			public void onSuccess(Set<String> userRoles) {
				// getting all batch classes for the all the roles of which the logged in user is part of
				getAllBatchClassesForLoggedInUser(order, userRoles);
			}
		});
	}

	public void bind() {
		view.getReportingListView().listView.setTableRowCount(ReportingConstants.TABLE_DATA_COUNT);

		final Order order = new Order(ReportProperty.NAME, true);
		updateSystemStatistics();

		setIndexes(startIndex, ReportingConstants.TABLE_DATA_COUNT, order);

		controller.getRpcService().getAllUsers(new EphesoftAsyncCallback<List<String>>() {

			@Override
			public void onSuccess(List<String> users) {
				view.populateUserListBox(users);
			}

			@Override
			public void customFailure(Throwable throwable) {
				showConfirmationDialog(LocaleDictionary.get().getMessageValue(ReportingMessages.UNABLE_TO_FETCH_USERS));
			}
		});

		view.getStartDateWidget().addValidator(
				new RegExValidator(view.getStartDateWidget(), ReportingConstants.DATE_VALIDATION_PATTERN, view.getStartDateTextBox(),
						controller.getRpcService()));
		view.getEndDateWidget().addValidator(
				new RegExValidator(view.getEndDateWidget(), ReportingConstants.DATE_VALIDATION_PATTERN, view.getEndDateTextBox(),
						controller.getRpcService()));

	}

	/**
	 * API to get all batch classes for the logged in user.This API also updates the system statistics and the table data.
	 * 
	 * @param order{@link Order}
	 * @param userRoles{@link Set<String>}
	 */
	private void getAllBatchClassesForLoggedInUser(final Order order, Set<String> userRoles) {
		controller.getRpcService().getAllBatchClassesForUserRoles(userRoles, new EphesoftAsyncCallback<Map<String, String>>() {

			@Override
			public void onSuccess(Map<String, String> batchClasses) {
				if (view.getSelectedBatchClassIds() != null && view.getSelectedBatchClassIds().isEmpty()) {
					view.populateBatchClassListBox(batchClasses);
				}

				batchClassIdList.addAll(batchClasses.keySet());
				// getting system statistics for the list of batch classes as in 'batchClassIdList'
				controller.getRpcService().getSystemStatistics(view.getStartDate(), view.getEndDate(), batchClassIdList,
						new EphesoftAsyncCallback<List<Integer>>() {

							@Override
							public void customFailure(Throwable throwable) {
								showConfirmationDialog(LocaleDictionary.get().getMessageValue(
										ReportingMessages.UNABLE_TO_GET_SYSTEM_STATISTICS));
							}

							@Override
							public void onSuccess(List<Integer> counts) {
								view.setTotalInfo(counts);
							}
						});
				// setting the table data
				WorkflowType workflowType = null;
				if (view.getSelectedRadioValue().equals(ReportingConstants.MODULE)) {
					workflowType = WorkflowType.MODULE;
				} else if (view.getSelectedRadioValue().equals(ReportingConstants.PLUGIN)) {
					workflowType = WorkflowType.PLUGIN;
				}
				if (workflowType == null) {
					getTableDataForUser(order);
				} else {
					ReportingPresenter.this.getTableData(workflowType, order);
				}
			}

			@Override
			public void customFailure(Throwable throwable) {
				showConfirmationDialog(LocaleDictionary.get().getMessageValue(ReportingMessages.UNABLE_TO_GET_BATCH_CLASSES));
			}
		});
	}

	@Override
	public void onPagination(final int startIndex, int maxResult, final Order order) {
		setIndexes(startIndex, maxResult, order);
		if (view.getStartDate().before(view.getEndDate()) || view.getStartDate().compareTo(view.getEndDate()) == 0) {
			WorkflowType workflowType = null;
			if (view.getSelectedRadioValue().equals(ReportingConstants.MODULE)) {
				workflowType = WorkflowType.MODULE;
			} else if (view.getSelectedRadioValue().equals(ReportingConstants.PLUGIN)) {
				workflowType = WorkflowType.PLUGIN;
			}
			if (workflowType == null) {
				updateTableDataForUser(startIndex, order);
			} else {
				updateTableData(workflowType, startIndex, order);
			}
		} else {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					ReportingMessages.START_DATE_CANNOT_BE_GREATER_THAN_END_DATE));
		}
	}

	public void onGoClicked() {
		startIndex = 0;
		totalCount = 0;
		if (view.getStartDateWidget().validate() && view.getStartDateWidget().validate()) {
			if (view.getStartDate().before(view.getEndDate()) || view.getStartDate().compareTo(view.getEndDate()) == 0) {
				updateSystemStatistics();

			} else {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						ReportingMessages.START_DATE_CANNOT_BE_GREATER_THAN_END_DATE));
			}
		} else {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					ReportingMessages.START_DATE_OR_END_DATE_NOT_VALID));
		}
	}

	private void updateTableData(final WorkflowType workflowType, final int startIndex, final Order order) {
		controller.getRpcService().getTotalRowCount(view.getSelectedBatchClassIds(), workflowType, view.getStartDate(),
				view.getEndDate(), new EphesoftAsyncCallback<Integer>() {

					@Override
					public void customFailure(Throwable arg0) {
						showConfirmationDialog(LocaleDictionary.get().getMessageValue(ReportingMessages.UNABLE_TO_GET_TOTAL_ROW_COUNT));
					}

					@Override
					public void onSuccess(final Integer count) {
						totalCount = count;
						controller.getRpcService().getTableData(view.getSelectedBatchClassIds(), workflowType, view.getStartDate(),
								view.getEndDate(), startIndex, maxResults, order, new EphesoftAsyncCallback<List<ReportDTO>>() {

									@Override
									public void customFailure(Throwable arg0) {
										showConfirmationDialog(LocaleDictionary.get().getMessageValue(
												ReportingMessages.UNABLE_TO_GET_REPORT_DATA));
									}

									@Override
									public void onSuccess(List<ReportDTO> data) {
										List<Record> records = view.createTableData(data, Integer.parseInt(view.getIntervalListBox()
												.getValue(view.getIntervalListBox().getSelectedIndex())));
										updateTableHeaders();
										view.getReportingListView().listView.updateRecords(records, startIndex, count);
									}
								});
					}
				});
	}

	private void getTableData(final WorkflowType workflowType, final Order order) {
		controller.getRpcService().getTotalRowCount(view.getSelectedBatchClassIds(), workflowType, view.getStartDate(),
				view.getEndDate(), new EphesoftAsyncCallback<Integer>() {

					@Override
					public void customFailure(Throwable arg0) {
						showConfirmationDialog(LocaleDictionary.get().getMessageValue(ReportingMessages.UNABLE_TO_GET_TOTAL_ROW_COUNT));
					}

					@Override
					public void onSuccess(final Integer count) {
						totalCount = count;
						controller.getRpcService().getTableData(view.getSelectedBatchClassIds(), workflowType, view.getStartDate(),
								view.getEndDate(), startIndex, maxResults, order, new EphesoftAsyncCallback<List<ReportDTO>>() {

									@Override
									public void customFailure(Throwable arg0) {
										showConfirmationDialog(LocaleDictionary.get().getMessageValue(
												ReportingMessages.UNABLE_TO_GET_REPORT_DATA));
									}

									@Override
									public void onSuccess(List<ReportDTO> data) {
										List<Record> records = view.createTableData(data, Integer.parseInt(view.getIntervalListBox()
												.getValue(view.getIntervalListBox().getSelectedIndex())));
										updateTableHeaders();
										view.getReportingListView().listView.initTable(count, controller.getPresenter(), records,
												false, false, null, false);
									}
								});
					}
				});
	}

	private void updateTableDataForUser(final int startIndex, final Order order) {
		controller.getRpcService().getTotalRowCountForUser(view.getSelectedBatchClassIds(), view.getSelectedRadioValue(),
				view.getStartDate(), view.getEndDate(), new EphesoftAsyncCallback<Integer>() {

					@Override
					public void customFailure(Throwable arg0) {
						showConfirmationDialog(LocaleDictionary.get().getMessageValue(ReportingMessages.UNABLE_TO_GET_TOTAL_ROW_COUNT));
					}

					@Override
					public void onSuccess(final Integer count) {
						totalCount = count;
						controller.getRpcService().getTableDataForUser(view.getSelectedBatchClassIds(), view.getSelectedRadioValue(),
								view.getStartDate(), view.getEndDate(), startIndex, maxResults, order,
								new EphesoftAsyncCallback<List<ReportDTO>>() {

									@Override
									public void customFailure(Throwable arg0) {
										showConfirmationDialog(LocaleDictionary.get().getMessageValue(
												ReportingMessages.UNABLE_TO_GET_REPORT_DATA));
									}

									@Override
									public void onSuccess(List<ReportDTO> data) {
										List<Record> records = view.createTableData(data, Integer.parseInt(view.getIntervalListBox()
												.getValue(view.getIntervalListBox().getSelectedIndex())));
										updateTableHeaders();
										view.getReportingListView().listView.updateRecords(records, startIndex, count);
									}
								});
					}
				});

	}

	private void getTableDataForUser(final Order order) {
		controller.getRpcService().getTotalRowCountForUser(view.getSelectedBatchClassIds(), view.getSelectedRadioValue(),
				view.getStartDate(), view.getEndDate(), new EphesoftAsyncCallback<Integer>() {

					@Override
					public void customFailure(Throwable arg0) {
						showConfirmationDialog(LocaleDictionary.get().getMessageValue(ReportingMessages.UNABLE_TO_GET_TOTAL_ROW_COUNT));
					}

					@Override
					public void onSuccess(final Integer count) {
						totalCount = count;
						controller.getRpcService().getTableDataForUser(view.getSelectedBatchClassIds(), view.getSelectedRadioValue(),
								view.getStartDate(), view.getEndDate(), startIndex, maxResults, order,
								new EphesoftAsyncCallback<List<ReportDTO>>() {

									@Override
									public void customFailure(Throwable arg0) {
										showConfirmationDialog(LocaleDictionary.get().getMessageValue(
												ReportingMessages.UNABLE_TO_GET_REPORT_DATA));
									}

									@Override
									public void onSuccess(List<ReportDTO> data) {
										List<Record> records = view.createTableData(data, Integer.parseInt(view.getIntervalListBox()
												.getValue(view.getIntervalListBox().getSelectedIndex())));
										updateTableHeaders();
										view.getReportingListView().listView.initTable(count, controller.getPresenter(), records,
												false, false, null, false);
									}
								});
					}
				});

	}

	private void showConfirmationDialog(String message) {
		final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(message, LocaleDictionary.get()
				.getMessageValue(ReportingMessages.DIALOG_TITLE_ERROR), Boolean.TRUE);

		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				confirmationDialog.hide(true);
			}

			@Override
			public void onCancelClick() {
				// Auto-generated method stub
			}
		});

	}

	public void onSyncDBClicked() {
		ScreenMaskUtility.maskScreen();

		controller.getRpcService().isAnotherUserConnected(new EphesoftAsyncCallback<Boolean>() {

			@Override
			public void customFailure(Throwable throwable) {
				// showConfirmationDialog(LocaleDictionary.get().getMessageValue(ReportingMessages.UNABLE_TO_GET_FLAG_FOR_MULTIUSER));
				showConfirmationDialog(throwable.getMessage());
				ScreenMaskUtility.unmaskScreen();
			}

			@Override
			public void onSuccess(Boolean isAlreadyInUse) {

				if (!isAlreadyInUse) {
					ScreenMaskUtility.maskScreen();
					controller.getRpcService().syncDatabase(new EphesoftAsyncCallback<Void>() {

						@Override
						public void customFailure(Throwable throwable) {
							showConfirmationDialog(ReportingMessages.UNABLE_TO_SYNC_DATABASE);
							// showConfirmationDialog(throwable.getMessage());
							ScreenMaskUtility.unmaskScreen();
						}

						@Override
						public void onSuccess(Void arg0) {
							bind();
							ScreenMaskUtility.unmaskScreen();
						}
					});

				} else {

					showConfirmationDialog(LocaleDictionary.get().getMessageValue(ReportingMessages.ANOTHER_USER_ALREADY_CONNECTED));
					ScreenMaskUtility.unmaskScreen();
				}
			}

		});

	}

	/*
	 * public void onSyncDBClicked() { ScreenMaskUtility.maskScreen(); controller.getRpcService().syncDatabase(new
	 * EphesoftAsyncCallback<Void>() {
	 * 
	 * @Override public void customFailure(Throwable throwable) { showConfirmationDialog(ReportingMessages.UNABLE_TO_SYNC_DATABASE);
	 * ScreenMaskUtility.unmaskScreen(); }
	 * 
	 * @Override public void onSuccess(Void arg0) { bind(); ScreenMaskUtility.unmaskScreen(); } }); }
	 */

	public void onCustomReportButtonClicked() {

		controller.getRpcService().getCustomReportButtonPopUpConfigs(new EphesoftAsyncCallback<Map<String, String>>() {

			@Override
			public void customFailure(Throwable throwable) {
				showConfirmationDialog(ReportingMessages.UNABLE_TO_GET_CUSTOM_REPORTS_BUTTON_POP_UP_CONFIGS);
				ScreenMaskUtility.unmaskScreen();
			}

			@Override
			public void onSuccess(Map<String, String> popUpConfigs) {
				String popUpTitle = popUpConfigs.get(ReportingConstants.CUSTOM_REPORT_TITLE);
				String url = popUpConfigs.get(ReportingConstants.CUSTOM_REPORT_URL);
				int xDimension = Integer.parseInt(popUpConfigs.get(ReportingConstants.CUSTOM_REPORT_POP_UP_XDIMENSION));
				int yDimension = Integer.parseInt(popUpConfigs.get(ReportingConstants.CUSTOM_REPORT_POP_UP_YDIMENSION));
				final ExternalAppDialogBox externalAppDialogBox = new ExternalAppDialogBox(url, xDimension, yDimension);
				externalAppDialogBox.setDialogTitle(popUpTitle);
				externalAppDialogBox.addDialogBoxListener(new ExternalAppDialogBox.DialogBoxListener() {

					@Override
					public void onOkClick() {
						externalAppDialogBox.hide();
					}

					@Override
					public void onCloseClick() {
						externalAppDialogBox.hide();
					}
				});
			}
		});

	}

	private void setIndexes(int startIndex, int maxResults, Order order) {
		this.startIndex = startIndex;
		this.maxResults = maxResults;
		this.order = order;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getStartIndex() {
		return startIndex;
	}

	private void updateTableHeaders() {
		String headerName = view.getSelectedRadioValue();
		if (!(headerName.equalsIgnoreCase(ReportingConstants.MODULE) || headerName.equalsIgnoreCase(ReportingConstants.PLUGIN))) {
			headerName = ReportingConstants.MODULE;
		}
		view.getReportingListView().updateHeaders(headerName,
				view.getIntervalListBox().getItemText(view.getIntervalListBox().getSelectedIndex()));
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		// TODO Auto-generated method stub

	}
}
