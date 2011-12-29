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

package com.ephesoft.dcma.gwt.rv.client.view;

import com.ephesoft.dcma.batch.schema.BatchStatus;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.BatchDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyDownEvent;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyDownEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.TreeRefreshEvent;
import com.ephesoft.dcma.gwt.rv.client.i18n.ReviewValidateConstants;
import com.ephesoft.dcma.gwt.rv.client.i18n.ReviewValidateMessages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TopPanel extends RVBasePanel {

	interface Binder extends UiBinder<Widget, TopPanel> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);

	@UiField
	protected Anchor nextBatch;
	@UiField
	protected Label batchIdText;
	@UiField
	protected Label batchId;
	@UiField
	protected Label batchNameText;
	@UiField
	protected Label batchName;
	@UiField
	protected Label batchClassNameText;
	@UiField
	protected Label batchClassName;
	/*@UiField
	protected Label batchStatusText;
	@UiField
	protected Label batchStatus;*/
	@UiField
	protected Label pipe;
	@UiField
	protected Anchor info;
	@UiField
	protected Label secondPipe;

	@UiField
	protected HorizontalPanel topHorizontalPanel;

	public TopPanel() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		batchIdText.setText(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.title_topPanel_batchId) + ReviewValidateConstants.COLON);
		batchIdText.setStyleName(ReviewValidateConstants.BOLD_TEXT);
		batchNameText.setText(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TITLE_TOPANEL_BATCHNAME) + ReviewValidateConstants.COLON);
		batchNameText.setStyleName(ReviewValidateConstants.BOLD_TEXT);
		batchClassNameText.setText(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.title_topPanel_batchClass) + ReviewValidateConstants.COLON);
		//batchClassNameText.setText("name;");
		batchClassNameText.setStyleName(ReviewValidateConstants.BOLD_TEXT);
		/*batchStatusText.setText(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.title_topPanel_batch_status) + ReviewValidateConstants.COLON);
		batchStatusText.setStyleName(ReviewValidateConstants.BOLD_TEXT);*/
	  
		pipe.setText(ReviewValidateConstants.FULL_STOP);
		pipe.setStyleName(ReviewValidateConstants.PIPE);
		secondPipe.setText(ReviewValidateConstants.FULL_STOP);
		secondPipe.setStyleName(ReviewValidateConstants.PIPE);
		nextBatch.setVisible(Boolean.FALSE);
		nextBatch.setStyleName(ReviewValidateConstants.FONT_BLUE);
		info.setText(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.title_topPanel_info));
		info.setTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.title_topPanel_info));
		info.setStyleName(ReviewValidateConstants.FONT_BLUE);
		ScreenMaskUtility.maskScreen();

		nextBatch.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				nextBatchPage();
			}
		});

		info.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				showKeyboardShortctPopUp();
			}
		});
	}

	@Override
	public void initializeWidget() {
		batchId.setText(presenter.batchDTO.getBatch().getBatchInstanceIdentifier());
		batchId.setStyleName(ReviewValidateConstants.BATCH_ALERT_TEXT);
		batchName.setText(presenter.batchDTO.getBatch().getBatchName());
		batchName.setStyleName(ReviewValidateConstants.BATCH_ALERT_TEXT);
		//changes to be made here 
		
		batchClassName.setText(presenter.batchDTO.getBatch().getBatchClassDescription());
		batchClassName.setStyleName(ReviewValidateConstants.BATCH_ALERT_TEXT);
		//BatchStatus batchStatusType = presenter.batchDTO.getBatch().getBatchStatus();
//		batchStatus.setText(fetchBatchStatus(batchStatusType));
//		batchStatus.setStyleName(ReviewValidateConstants.BATCH_ALERT_TEXT);
		
		nextBatch.setText(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.title_topPanel_next) + " >");
		presenter.rpcService.getRowsCount(new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable event) {

			}

			@Override
			public void onSuccess(Integer numberOfBatches) {
				if (numberOfBatches < 1) {
					nextBatch.setVisible(Boolean.FALSE);
				} else {
					nextBatch.setVisible(Boolean.TRUE);
				}
			}
		});

	}

	public String fetchBatchStatus(BatchStatus batchStatusType) {
		StringBuffer returnStatus = new StringBuffer();
		if (batchStatusType != null && batchStatusType.value() != null && !batchStatusType.value().isEmpty()) {
			switch (batchStatusType) {
				case LOCKED:
					returnStatus.append(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.batch_status_locked));
					break;
				case ERROR:
					returnStatus.append(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.batch_status_error));
					break;
				case FINISHED:
					returnStatus.append(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.batch_status_finished));
					break;
				case READY:
					returnStatus.append(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.batch_status_ready));
					break;
				case READY_FOR_REVIEW:
					returnStatus.append(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.batch_status_readyForReview));
					break;
				case READY_FOR_VALIDATION:
					returnStatus.append(LocaleDictionary.get().getConstantValue(
							ReviewValidateConstants.batch_status_readyForValidation));
					break;
				case REVIEWED:
					returnStatus.append(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.batch_status_reviewed));
					break;
				case RUNNING:
					returnStatus.append(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.batch_status_running));
					break;
				case VALIDATED:
					returnStatus.append(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.batch_status_validated));
					break;
				default:
					returnStatus.append(ReviewValidateConstants.EMPTY_STRING);
					break;
			}
		}
		return returnStatus.toString();
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		// TODO Auto-generated method stub
		eventBus.addHandler(RVKeyDownEvent.TYPE, new RVKeyDownEventHandler() {

			@Override
			public void onKeyDown(RVKeyDownEvent event) {

				if (event.getEvent().isControlKeyDown()) {
					switch (event.getEvent().getNativeEvent().getKeyCode()) {
						// Ctrl + Shift + ->
						case 39: {
							if (event.getEvent().isShiftKeyDown()) {
								event.getEvent().getNativeEvent().preventDefault();
								nextBatchPage();
								break;
							}
						}
						default:
							break;
					}
				}
			}
		});

	}

	// private void backPage() {
	// final ConfirmationDialog confirmationDialog = new ConfirmationDialog();
	// confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.msg_backButton_confm));
	// confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.title_revVal_backButton));
	// confirmationDialog.okButton.setText(LocaleDictionary.get().getMessageValue(ReviewValidateConstants.title_confirmation_save));
	// confirmationDialog.cancelButton.setText(LocaleDictionary.get().getMessageValue(
	// ReviewValidateConstants.title_confirmation_discard));
	// confirmationDialog.addDialogListener(new DialogListener() {
	//
	// @Override
	// public void onOkClick() {
	// presenter.rpcService.updateBatch(presenter.batchDTO.getBatch(), new AsyncCallback<BatchStatus>() {
	//
	// @Override
	// public void onFailure(Throwable arg0) {
	// ScreenMaskUtility.unmaskScreen();
	// Window.alert(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.error_topPanel_ok_failure,
	// presenter.batchDTO.getBatch().getBatchInstanceIdentifier(), arg0.getMessage()));
	// }
	//
	// @Override
	// public void onSuccess(BatchStatus arg0) {
	// presenter.rpcService.cleanup(new AsyncCallback<Void>() {
	//
	// @Override
	// public void onFailure(Throwable arg0) {
	// Window.alert(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.error_topPanel_ok_failure,
	// presenter.batchDTO.getBatch().getBatchInstanceIdentifier()));
	// }
	//
	// @Override
	// public void onSuccess(Void arg0) {
	// moveToLandingPage();
	// }
	// });
	// ScreenMaskUtility.unmaskScreen();
	// }
	// });
	//
	// }
	//
	// @Override
	// public void onCancelClick() {
	// confirmationDialog.hide();
	// presenter.rpcService.cleanup(new AsyncCallback<Void>() {
	//
	// @Override
	// public void onFailure(Throwable arg0) {
	// Window.alert(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.error_topPanel_ok_success,
	// presenter.batchDTO.getBatch().getBatchInstanceIdentifier()));
	// }
	//
	// @Override
	// public void onSuccess(Void arg0) {
	// moveToLandingPage();
	// }
	// });
	// ScreenMaskUtility.unmaskScreen();
	//
	// }
	// });
	//
	// confirmationDialog.center();
	// confirmationDialog.show();
	// confirmationDialog.okButton.setFocus(true);
	//
	// }

	private void nextBatchPage() {
		final ConfirmationDialog confirmationDialog = new ConfirmationDialog();
		confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.msg_backButton_confm));
		confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.title_revVal_nextButton));
		confirmationDialog.okButton.setText(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.title_confirmation_save));
		confirmationDialog.cancelButton.setText(LocaleDictionary.get().getConstantValue(
				ReviewValidateConstants.title_confirmation_discard));
		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				ScreenMaskUtility.maskScreen();
				presenter.rpcService.updateBatch(presenter.batchDTO.getBatch(), new AsyncCallback<BatchStatus>() {

					@Override
					public void onFailure(Throwable arg0) {
						ScreenMaskUtility.unmaskScreen();
						ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.error_topPanel_ok_failure,
								presenter.batchDTO.getBatch().getBatchInstanceIdentifier(), arg0.getMessage()));
					}

					@Override
					public void onSuccess(BatchStatus arg0) {
						confirmationDialog.hide();
						getHighestPriorityBatch();
					}
				});

			}

			@Override
			public void onCancelClick() {
				confirmationDialog.hide();
				getHighestPriorityBatch();
			}
		});

		confirmationDialog.center();
		confirmationDialog.show();
		confirmationDialog.okButton.setFocus(true);
	}

	private void getHighestPriorityBatch() {

		presenter.rpcService.cleanup(new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						ReviewValidateMessages.error_topPanel_ok_success, presenter.batchDTO.getBatch().getBatchInstanceIdentifier()));
			}

			@Override
			public void onSuccess(Void arg0) {
				presenter.rpcService.getHighestPriortyBatch(new AsyncCallback<BatchDTO>() {

					@Override
					public void onSuccess(final BatchDTO batchDTO) {
						if (batchDTO == null) {
							ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
									ReviewValidateMessages.error_topPanel_noMoreBatches));
						}
						final String batchID2 = batchDTO.getBatch().getBatchInstanceIdentifier();
						presenter.rpcService.acquireLock(batchID2, new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable arg0) {
								ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
										ReviewValidateMessages.error_topPanel_lock_acquire, batchID2, arg0.getMessage()));
							}

							@Override
							public void onSuccess(Void arg0) {
								presenter.batchDTO = batchDTO;
								initializeWidget();
								// presenter.getView().asWidget();
								presenter.onTableViewBackButtonClicked();
								presenter.getView().imgOverlayPanel.clearPanel();
								presenter.getView().getRvPanel().setVisibility();
								TopPanel.this.fireEvent(new TreeRefreshEvent(batchDTO, null, null));
							}

						});

					}

					@Override
					public void onFailure(Throwable arg0) {
						ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.error_ret_next_batch));
					}
				});
			}
		});
		ScreenMaskUtility.unmaskScreen();

	}

	// private void moveToLandingPage() {
	// String href = Window.Location.getHref();
	// String baseUrl = href.substring(0, href.lastIndexOf("/"));
	// Window.Location.assign(baseUrl + "/" + "BatchList.html");
	// }

	public HorizontalPanel getTopHorizontalPanel() {
		return topHorizontalPanel;
	}

	public String createKeyboardShortcuts() {
		String keyBoardShortcuts = "<table border =\"1\"><tr><td>1 </td><td> "
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.info_save_document)
				+ "</td><td>CTRL + s " + LocaleDictionary.get().getConstantValue(ReviewValidateConstants.info_or)
				+ " S </td></tr><tr><td>2 </td><td>" + LocaleDictionary.get().getConstantValue(ReviewValidateConstants.info_split_document)
				+ "</td><td>CTRL + t " + LocaleDictionary.get().getConstantValue(ReviewValidateConstants.info_or)
				+ " T </td></tr>" + "<tr><td>3 </td><td>" + LocaleDictionary.get().getConstantValue(ReviewValidateConstants.info_zoom_in)
				+ "</td><td>CTRL+ 1 " + LocaleDictionary.get().getConstantValue(ReviewValidateConstants.info_or)
				+ " ! </td></tr><tr><td>4 </td><td>" + LocaleDictionary.get().getConstantValue(ReviewValidateConstants.info_zoom_out)
				+ "</td><td>CTRL+ 2 " + LocaleDictionary.get().getConstantValue(ReviewValidateConstants.info_or)
				+ " @ </td></tr>" + "<tr><td>5 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.info_move_cursor_next_field)
				+ "</td><td>CTRL+ Tab </td></tr><tr><td>6 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.info_move_cursor_next_field_error)
				+ "</td><td>CTRL + > " + LocaleDictionary.get().getConstantValue(ReviewValidateConstants.info_or)
				+ " . </td></tr>" + "<tr><td>7 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.info_move_cursor_previous_field_error)
				+ "</td><td>CTRL + < " + LocaleDictionary.get().getConstantValue(ReviewValidateConstants.info_or)
				+ " , </td></tr>" + "<tr><td>8 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.info_duplicate_page)
				+ "</td><td>CTRL + d " + LocaleDictionary.get().getConstantValue(ReviewValidateConstants.info_or)
				+ " D </td></tr>" + "<tr><td>9 </td><td>" + LocaleDictionary.get().getConstantValue(ReviewValidateConstants.info_move_page)
				+ "</td><td>CTRL + m " + LocaleDictionary.get().getConstantValue(ReviewValidateConstants.info_or)
				+ " M </td></tr>" + "<tr><td>10 </td><td>" + LocaleDictionary.get().getConstantValue(ReviewValidateConstants.info_fit_page)
				+ "</td><td>F12 </td></tr>" + "<tr><td>11 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.info_rotate_page)
				+ "</td><td>CTRL + r " + LocaleDictionary.get().getConstantValue(ReviewValidateConstants.info_or)
				+ " R </td></tr>" + "<tr><td>12 </td><td>" + LocaleDictionary.get().getConstantValue(ReviewValidateConstants.info_remove_page)
				+ "</td><td>Shift + del </td></tr> <tr><td>13 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.info_review_to_validate)
				+ "</td><td>CTRL + q " + LocaleDictionary.get().getConstantValue(ReviewValidateConstants.info_or)
				+ " Q </td></tr><tr><td>14 </td><td>" + LocaleDictionary.get().getConstantValue(ReviewValidateConstants.change_document_type)
				+ "</td><td>CTRL + ;</td></tr>" + "<tr><td>15 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.merge_document_to_previous_one)
				+ "</td><td>CTRL + /</td></tr>" + "<tr><td>16 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.MSG_NEXT_BATCH)
				+ "</td><td>CTRL + Shift + -> </td></tr>" + "<tr><td>17 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.fuzzy_search_tooltip)
				+ "</td><td>CTRL + z or Z</td></tr>" + "<tr><td>18 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TABLE_VIEW_SHORTCUT)
				+ "</td><td>CTRL + 5</td></tr>" + "<tr><td>19 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TABLE_VIEW_BACK_SHORTCUT)
				+ "</td><td>CTRL + 6</td></tr>" + "<tr><td>20 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TABLE_INSERT_ROW_SHORTCUT)
				+ "</td><td>CTRL + i</td></tr>" + "<tr><td>21 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TABLE_DELETE_ROW_SHORTCUT)
				+ "</td><td>CTRL + j</td></tr>" + "<tr><td>22 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TABLE_DELETE_ALL_ROW_SHORTCUT)
				+ "</td><td>CTRL + u</td></tr>" + "<tr><td>23 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TRAVERSE_TABLES_SHORTCUT)
				+ "</td><td>CTRL + k</td></tr>" + "<tr><td>24 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.ADD_NEW_TABLE_SHORTCUT)
				+ "</td><td>CTRL + 3</td></tr>" + "<tr><td>25 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.MANUAL_TABLE_EXTRACTION_SHORTCUT)
				+ "</td><td>CTRL + y or Y</td></tr>" + "<tr><td>26 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.IMAGE_SCROLL_LEFT_SHORTCUT)
				+ "</td><td>CTRL + left arrow</td></tr>" + "<tr><td>27 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.IMAGE_SCROLL_RIGHT_SHORTCUT)
				+ "</td><td>CTRL + right arrow</td></tr>" + "<tr><td>28 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.IMAGE_SCROLL_DOWN_SHORTCUT)
				+ "</td><td>CTRL + down arrow</td></tr>" + "<tr><td>29 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.IMAGE_SCROLL_UP_SHORTCUT)
				+ "</td><td>CTRL + up arrow</td></tr>" + "<tr><td>30 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.IMAGE_ZOOM_LOCK)
				+ "</td><td>CTRL + l or L</td></tr>" + "<tr><td>31 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.DOC_TYPE_TOGGLE)
				+ "</td><td>CTRL + 0</td></tr>" + "<tr><td>32 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.NEXT_DOC_TYPE)
				+ "</td><td>CTRL + n</td></tr>" + "<tr><td>33 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.PREV_DOC_TYPE)
				+ "</td><td>CTRL + Shift + n</td></tr>" + "<tr><td>34 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.NEXT_PAGE_TYPE)
				+ "</td><td>CTRL + p</td></tr>" + "<tr><td>35 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.PREV_PAGE_TYPE)
				+ "</td><td>CTRL + Shift + p</td></tr>" +"<tr><td>36 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.FUNCTION_KEY_SHORTCUTS)
				+ "</td><td>F1 to F11[except F5]" +"<tr><td>37 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.MODAL_WINDOW_SHORTCUTS)
				+ "</td><td>CTRL + [4/7/8/9]</td></tr>" + "<tr><td>38 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.DISCLOSURE_PANEL_SHORCUT)
				+ "</td><td>CTRL + g or G</td></tr>" + "</table>";
		return keyBoardShortcuts;
	}
	
	public Anchor getInfo()
	{
		return info;
	}
	
	public void showKeyboardShortctPopUp() {
		final ConfirmationDialog confirmationDialog = new ConfirmationDialog(Boolean.TRUE);
		String keyBoardShortcuts = createKeyboardShortcuts();
		confirmationDialog.setMessage(keyBoardShortcuts);
		confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.info_title));
		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onCancelClick() {
				confirmationDialog.hide();
				info.setFocus(true);
			}

			@Override
			public void onOkClick() {
				confirmationDialog.hide();
				info.setFocus(true);
			}

		});
		confirmationDialog.center();
		confirmationDialog.show();
		confirmationDialog.getPanel().getElementById("textPanel").addClassName("scrollClass");
		confirmationDialog.okButton.setFocus(true);
	}
}
