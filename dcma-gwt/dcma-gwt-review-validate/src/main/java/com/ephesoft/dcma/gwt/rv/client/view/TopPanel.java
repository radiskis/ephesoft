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

package com.ephesoft.dcma.gwt.rv.client.view;

import com.ephesoft.dcma.gwt.core.client.EphesoftAsyncCallback;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.BatchDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.StringUtil;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.ephesoft.dcma.gwt.core.shared.listener.ThirdButtonListener;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyDownEvent;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyDownEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.TreeRefreshEvent;
import com.ephesoft.dcma.gwt.rv.client.i18n.ReviewValidateConstants;
import com.ephesoft.dcma.gwt.rv.client.i18n.ReviewValidateMessages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * The <code>TopPanel</code> class represents view for top panel shown in review and validate screen. It contains view for next, save
 * and shortcut link.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.rv.client.view
 */
public class TopPanel extends RVBasePanel {

	interface Binder extends UiBinder<Widget, TopPanel> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);

	@UiField
	protected transient Label nextBatch;
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
	@UiField
	protected Anchor saveBatch;
	/*
	 * @UiField protected Label batchStatusText;
	 * 
	 * @UiField protected Label batchStatus;
	 */
	@UiField
	protected Label pipe;
	@UiField
	protected Anchor info;
	@UiField
	protected Label secondPipe;

	@UiField
	protected HorizontalPanel topHorizontalPanel;

	/**
	 * The shortcutsConfirmationDialog {@link ConfirmationDialog} is a confirmation dialog for showing short cut keys box.
	 */
	private ConfirmationDialog shortcutsConfirmationDialog;

	public TopPanel() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		batchIdText.setText(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TITLE_TOPPANEL_BATCHID)
				+ ReviewValidateConstants.COLON);
		batchIdText.setStyleName(ReviewValidateConstants.BOLD_TEXT);
		batchNameText.setText(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TITLE_TOPANEL_BATCHNAME)
				+ ReviewValidateConstants.COLON);
		batchNameText.setStyleName(ReviewValidateConstants.BOLD_TEXT);
		saveBatch.setStyleName(ReviewValidateConstants.FONT_BLUE);
		saveBatch.setText(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TITLE_TOPPANEL_SAVE_ANCHOR) + " >");
		saveBatch.setTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.SAVE_BUTTON_TOOLTIP));
		saveBatch.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				presenter.getView().getRvPanel().getValidatePanel().updateDocument(null, null);
				presenter.setControlSorQPressed(true);
				presenter.performOperationsOnCtrlSPress();
			}
		});

		batchClassNameText.setText(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TITLE_TOPPANEL_BATCHCLASS)
				+ ReviewValidateConstants.COLON);
		// batchClassNameText.setText("name;");
		batchClassNameText.setStyleName(ReviewValidateConstants.BOLD_TEXT);
		/*
		 * batchStatusText.setText(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.title_topPanel_batch_status) +
		 * ReviewValidateConstants.COLON); batchStatusText.setStyleName(ReviewValidateConstants.BOLD_TEXT);
		 */

		pipe.setText(ReviewValidateConstants.FULL_STOP);
		pipe.setStyleName(ReviewValidateConstants.PIPE);
		secondPipe.setText(ReviewValidateConstants.FULL_STOP);
		secondPipe.setStyleName(ReviewValidateConstants.PIPE);
		nextBatch.setVisible(Boolean.FALSE);
		nextBatch.setStyleName(ReviewValidateConstants.BLUE_LINK);
		info.setText(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TITLE_TOPPANEL_INFO));
		info.setTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.shortcut_button_tooltip));
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
		String tempBatchName = presenter.batchDTO.getBatch().getBatchName();
		batchName.setText(StringUtil.getDotAppendedSubString(ReviewValidateConstants.BATCH_CLASS_NAME_DESC_CHARACTER_LIMIT,
				tempBatchName));
		batchName.setTitle(tempBatchName);
		batchName.setStyleName(ReviewValidateConstants.BATCH_ALERT_TEXT);
		String tempBatchClassName = presenter.batchDTO.getBatch().getBatchClassDescription();
		batchClassName.setText(StringUtil.getDotAppendedSubString(ReviewValidateConstants.BATCH_CLASS_NAME_DESC_CHARACTER_LIMIT,
				tempBatchClassName));
		batchClassName.setTitle(tempBatchClassName);
		batchClassName.setStyleName(ReviewValidateConstants.BATCH_ALERT_TEXT);
		nextBatch.setText(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TITLE_TOPPANEL_NEXT) + " >");
		this.presenter.setBatchListScreenTab();
		presenter.rpcService.getRowsCount(new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable event) {
				// no need to do anything here as we dont need to signal failure in this case.
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

	@Override
	public void injectEvents(HandlerManager eventBus) {
		// TODO Auto-generated method stub
		eventBus.addHandler(RVKeyDownEvent.type, new RVKeyDownEventHandler() {

			@Override
			public void onKeyDown(RVKeyDownEvent event) {

				KeyDownEvent keyDownEvent = event.getEvent();
				if (keyDownEvent.isControlKeyDown() && keyDownEvent.isShiftKeyDown()
						&& keyDownEvent.getNativeEvent().getKeyCode() == 39) {
					// Ctrl + Shift + ->
					keyDownEvent.getNativeEvent().preventDefault();
					nextBatchPage();
				}
			}
		});

	}

	private void nextBatchPage() {
		moveToNextBatch();
	}

	private void moveToNextBatch() {
		final ConfirmationDialog confirmationDialog = new ConfirmationDialog(true);
		confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.MSG_BACKBUTTON_CONFM));
		confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TITLE_REVVAL_NEXTBUTTON));
		confirmationDialog.okButton.setText(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TITLE_CONFIRMATION_SAVE));
		confirmationDialog.cancelButton.setText(LocaleDictionary.get().getConstantValue(
				ReviewValidateConstants.TITLE_CONFIRMATION_DISCARD));
		confirmationDialog.thirdButton.setText(LocaleDictionary.get().getConstantValue(
				ReviewValidateConstants.TITLE_CONFIRMATION_CANCEL));
		confirmationDialog.show();
		confirmationDialog.center();
		confirmationDialog.addDialogListener(new ThirdButtonListener() {

			@Override
			public void onOkClick() {
				ScreenMaskUtility.maskScreen();
				presenter.rpcService.saveBatch(presenter.batchDTO.getBatch(), new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable arg0) {
						ScreenMaskUtility.unmaskScreen();
						if (!presenter.displayErrorMessage(arg0)) {
							ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
									ReviewValidateMessages.ERROR_TOPPANEL_OK_FAILURE,
									presenter.batchDTO.getBatch().getBatchInstanceIdentifier(), arg0.getMessage()), Boolean.TRUE);
						}
					}

					@Override
					public void onSuccess(Void arg0) {
						confirmationDialog.hide();

						// To set the initial dimensions of new batch page image
						presenter.getView().imgOverlayPanel.clearInitialPageDimensionsMap();
						getHighestPriorityBatch();
					}
				});

			}

			@Override
			public void onCancelClick() {
				confirmationDialog.hide();

				// To set the initial dimensions of new batch page image
				presenter.getView().imgOverlayPanel.clearInitialPageDimensionsMap();
				getHighestPriorityBatch();
			}

			@Override
			public void onThirdButtonClick() {
				confirmationDialog.hide();
			}
		});
		confirmationDialog.okButton.setFocus(true);

	}

	private void getHighestPriorityBatch() {
		presenter.rpcService.getHighestPriortyBatch(new AsyncCallback<BatchDTO>() {

			@Override
			public void onSuccess(final BatchDTO batchDTO) {
				// setting the default zoom count value from properties file
				presenter.setZoomCount();
				presenter.rpcService.updateEndTimeAndCalculateDuration(presenter.batchDTO.getBatch().getBatchInstanceIdentifier(),
						new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable arg0) {
								// no need to do anything here as we are only firing a call to server and not looking for the call back
							}

							@Override
							public void onSuccess(Void arg0) {
								// no need to do anything here as we are only firing a call to server and not looking for the call back
							}
						});

				presenter.rpcService.cleanUpCurrentBatch(presenter.batchDTO.getBatch().getBatchInstanceIdentifier(),
						new EphesoftAsyncCallback<Void>() {

							@Override
							public void customFailure(Throwable arg0) {
								// no need to do anything here as we are only firing a call to server and not looking for the call back
							}

							@Override
							public void onSuccess(Void arg0) {
								// no need to do anything here as we are only firing a call to server and not looking for the call back
							}
						});

				if (null == batchDTO) {
					ConfirmationDialog showConfirmationDialogError = ConfirmationDialogUtil.showConfirmationDialogError(
							LocaleDictionary.get().getMessageValue(ReviewValidateMessages.ERROR_TOPPANEL_NOMOREBATCHES), Boolean.TRUE);
					showConfirmationDialogError.setPerformCancelOnEscape(true);
					showConfirmationDialogError.addDialogListener(new DialogListener() {

						@Override
						public void onOkClick() {
							presenter.moveToLandingPage();

						}

						@Override
						public void onCancelClick() {
							presenter.moveToLandingPage();

						}
					});
				} else {
					presenter.setTitleOfTab(batchDTO.getBatch().getBatchInstanceIdentifier());
					presenter.batchDTO = batchDTO;
					initializeWidget();
					TopPanel.this.fireEvent(new TreeRefreshEvent(batchDTO, null, null));
					presenter.onTableViewBackButtonClicked();
					presenter.getView().imgOverlayPanel.clearPanel();
					presenter.getView().getRvPanel().setVisibility();
				}

			}

			@Override
			public void onFailure(Throwable arg0) {
				String errorMessage = arg0.getLocalizedMessage();
				if (errorMessage.contains(ReviewValidateConstants.ERROR_TYPE_5)) {
					int indexOfError = errorMessage.indexOf(ReviewValidateConstants.ERROR_TYPE_5);
					int errorMessageLength = ReviewValidateConstants.ERROR_TYPE_5.length();
					String batchID = errorMessage.substring(indexOfError + errorMessageLength + 1);
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							ReviewValidateMessages.ERROR_TOPPANEL_LOCK_ACQUIRE, batchID, errorMessage.substring(0, indexOfError)),
							Boolean.TRUE);

				} else if (!presenter.displayErrorMessage(arg0)) {
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							ReviewValidateMessages.ERROR_RET_NEXT_BATCH), Boolean.TRUE);
				}
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
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.INFO_SAVE_DOCUMENT) + "</td><td>CTRL + s "
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.INFO_OR) + " S </td></tr><tr><td>2 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.INFO_SPLIT_DOCUMENT) + "</td><td>CTRL + t "
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.INFO_OR) + " T </td></tr>" + "<tr><td>3 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.INFO_ZOOM_IN) + "</td><td>CTRL+ 1 "
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.INFO_OR) + " ! </td></tr><tr><td>4 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.INFO_ZOOM_OUT) + "</td><td>CTRL+ 2 "
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.INFO_OR) + " @ </td></tr>" + "<tr><td>5 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.INFO_MOVE_CURSOR_NEXT_FIELD)
				+ "</td><td>CTRL+ Tab </td></tr><tr><td>6 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.INFO_MOVE_CURSOR_NEXT_FIELD_ERROR)
				+ "</td><td>CTRL + > " + LocaleDictionary.get().getConstantValue(ReviewValidateConstants.INFO_OR) + " . </td></tr>"
				+ "<tr><td>7 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.INFO_MOVE_CURSOR_PREVIOUS_FIELD_ERROR)
				+ "</td><td>CTRL + < " + LocaleDictionary.get().getConstantValue(ReviewValidateConstants.INFO_OR) + " , </td></tr>"
				+ "<tr><td>8 </td><td>" + LocaleDictionary.get().getConstantValue(ReviewValidateConstants.INFO_DUPLICATE_PAGE)
				+ "</td><td>CTRL + d " + LocaleDictionary.get().getConstantValue(ReviewValidateConstants.INFO_OR) + " D </td></tr>"
				+ "<tr><td>9 </td><td>" + LocaleDictionary.get().getConstantValue(ReviewValidateConstants.INFO_MOVE_PAGE)
				+ "</td><td>CTRL + m " + LocaleDictionary.get().getConstantValue(ReviewValidateConstants.INFO_OR) + " M </td></tr>"
				+ "<tr><td>10 </td><td>" + LocaleDictionary.get().getConstantValue(ReviewValidateConstants.INFO_FIT_PAGE)
				+ "</td><td>F12 </td></tr>" + "<tr><td>11 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.INFO_ROTATE_PAGE) + "</td><td>CTRL + r "
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.INFO_OR) + " R </td></tr>" + "<tr><td>12 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.INFO_REMOVE_PAGE)
				+ "</td><td>Shift + del </td></tr> <tr><td>13 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.INFO_REVIEW_TO_VALIDATE) + "</td><td>CTRL + q "
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.INFO_OR) + " Q </td></tr><tr><td>14 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.CHANGE_DOCUMENT_TYPE)
				+ "</td><td>CTRL + ;</td></tr>" + "<tr><td>15 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.MERGE_DOCUMENT_TO_PREVIOUS_ONE)
				+ "</td><td>CTRL + /</td></tr>" + "<tr><td>16 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.MSG_NEXT_BATCH)
				+ "</td><td>CTRL + Shift + -> </td></tr>" + "<tr><td>17 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.FUZZY_SEARCH_TOOLTIP)
				+ "</td><td>CTRL + z or Z</td></tr>" + "<tr><td>18 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TABLE_VIEW_SHORTCUT) + "</td><td>CTRL + 5</td></tr>"
				+ "<tr><td>19 </td><td>" + LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TABLE_VIEW_BACK_SHORTCUT)
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
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.DOC_TYPE_TOGGLE) + "</td><td>CTRL + 0</td></tr>"
				+ "<tr><td>32 </td><td>" + LocaleDictionary.get().getConstantValue(ReviewValidateConstants.NEXT_DOC_TYPE)
				+ "</td><td>CTRL + n</td></tr>" + "<tr><td>33 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.PREV_DOC_TYPE)
				+ "</td><td>CTRL + Shift + n</td></tr>" + "<tr><td>34 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.NEXT_PAGE_TYPE) + "</td><td>CTRL + p</td></tr>"
				+ "<tr><td>35 </td><td>" + LocaleDictionary.get().getConstantValue(ReviewValidateConstants.PREV_PAGE_TYPE)
				+ "</td><td>CTRL + Shift + p</td></tr>" + "<tr><td>36 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.FUNCTION_KEY_SHORTCUTS)
				+ "</td><td>F1 to F11[except F5]" + "<tr><td>37 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.MODAL_WINDOW_SHORTCUTS)
				+ "</td><td>CTRL + [4/7/8/9]</td></tr>" + "<tr><td>38 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.DISCLOSURE_PANEL_SHORCUT)
				+ "</td><td>CTRL + g or G</td></tr>" + "<tr><td>39 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.REGEX_ACTIVATE_DEACTIVATE_TOGGLE_SHORTCUT)
				+ "</td><td>CTRL + b or B</td></tr>" + "<tr><td>40 </td><td>"
				+ LocaleDictionary.get().getConstantValue(ReviewValidateConstants.OPEN_KEYBOARD_SHORTCUTS_POP_UP)
				+ "</td><td>CTRL + e or E</td></tr>" + "</table>";
		return keyBoardShortcuts;
	}

	public Anchor getInfo() {
		return info;
	}

	public void showKeyboardShortctPopUp() {
		String keyBoardShortcuts = createKeyboardShortcuts();
		final ConfirmationDialog confirmationDialog = new ConfirmationDialog();
		confirmationDialog.setMessage(keyBoardShortcuts);
		confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.INFO_TITLE));
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
		confirmationDialog.show();
		confirmationDialog.center();
		confirmationDialog.okButton.setFocus(true);
		confirmationDialog.cancelButton.setVisible(false);
		confirmationDialog.getPanel().getElementById("textPanel").addClassName("scrollClass");
	}

	/**
	 * The <code>setFocusForOkButtonOnShortcutPopup</code> method is for setting focus of OK button for shortcut keys on put up.
	 * 
	 * @param focus true/false
	 */
	public void setFocusForOkButtonOnShortcutPopup(boolean focus) {
		if (null != shortcutsConfirmationDialog) {
			shortcutsConfirmationDialog.okButton.setFocus(focus);
		}
	}
}
