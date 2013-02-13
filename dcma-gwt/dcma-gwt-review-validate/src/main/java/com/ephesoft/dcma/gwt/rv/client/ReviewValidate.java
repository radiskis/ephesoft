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

package com.ephesoft.dcma.gwt.rv.client;

import com.ephesoft.dcma.gwt.core.client.DCMAEntryPoint;
import com.ephesoft.dcma.gwt.core.client.EphesoftAsyncCallback;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleInfo;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.client.view.RootPanel;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyDownEvent;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyUpEvent;
import com.ephesoft.dcma.gwt.rv.client.i18n.ReviewValidateConstants;
import com.ephesoft.dcma.gwt.rv.client.i18n.ReviewValidateMessages;
import com.ephesoft.dcma.gwt.rv.client.view.ReviewValidateView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class ReviewValidate extends DCMAEntryPoint<ReviewValidateDocServiceAsync> {

	@Override
	public void onLoad() {
		defineBridgeMethod();
		Document.get().setTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.RV_TITLE));
		final ReviewValidateController controller = new ReviewValidateController(rpcService, eventBus);

		ReviewValidateView view = controller.getPresenter().getView();

		final RootPanel rootPanel = new RootPanel(view.getOuter(), rpcService);
		rootPanel.getHeader().setEventBus(eventBus);
		rootPanel.getHeader().setShowDialogBoxOnTabClick(true);
		rootPanel.getHeader().setDialogMessage(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.MSG_BACKBUTTON_CONFM));
		rootPanel.getHeader().addTab(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TAB_LABEL_HOME),
				"BatchList.html", true);
		rootPanel.getHeader().addNonClickableTab(
				LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TAB_LABEL_BATCH_DETAIL), "ReviewValidate.html");
		rootPanel.getHeader().addTab(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TAB_LABEL_WEB_SCANNER),
				"WebScanner.html", true);

		checkAndDisplayUploadBatchTab(rootPanel);

		ScreenMaskUtility.maskScreen();

		rpcService.getRowsCount(new EphesoftAsyncCallback<Integer>() {

			public void customFailure(Throwable caught) {
				// no change needed if there is a failure in getting row count
			}

			public void onSuccess(Integer result) {
				if (result == null || result.intValue() == 0) {
					rootPanel.getHeader().getTabBar().setTabEnabled(1, false);
				}
			}
		});

		rpcService.getUserName(new EphesoftAsyncCallback<String>() {

			@Override
			public void onSuccess(String userName) {
				rootPanel.getHeader().setUserName(userName);
				ScreenMaskUtility.unmaskScreen();
			}

			@Override
			public void customFailure(Throwable arg0) {
				ScreenMaskUtility.unmaskScreen();
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						ReviewValidateMessages.MSG_USERNAME_ERROR));
			}
		});

		RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
		rootLayoutPanel.clear();
		rootLayoutPanel.add(rootPanel);

		final FocusPanel focusPanel = new FocusPanel();
		focusPanel.add(rootLayoutPanel);

		addFocusPanelHandlers(focusPanel);

		com.google.gwt.user.client.ui.RootPanel.get().add(focusPanel);

		Window.addWindowClosingHandler(new Window.ClosingHandler() {

			@Override
			public void onWindowClosing(ClosingEvent arg0) {
				onWindowClose();

			}
		});

		controller.onPresenterLoad(null);

	}

	/**
	 * @param focusPanel
	 */
	private void addFocusPanelHandlers(final FocusPanel focusPanel) {
		focusPanel.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				eventBus.fireEvent(new RVKeyUpEvent(event));
			}
		});

		focusPanel.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				eventBus.fireEvent(new RVKeyDownEvent(event));
			}
		});
	}

	/**
	 * @param rootPanel
	 */
	private void checkAndDisplayUploadBatchTab(final RootPanel rootPanel) {
		rpcService.isUploadBatchEnabled(new EphesoftAsyncCallback<Boolean>() {

			@Override
			public void customFailure(Throwable arg0) {

				/*
				 * On failure handling to be done.
				 */
			}

			@Override
			public void onSuccess(Boolean isUploadBatchEnabled) {
				if (isUploadBatchEnabled) {
					rootPanel.getHeader().addTab(
							LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TAB_LABEL_UPLOAD_BATCH),
							"UploadBatch.html", true);
				}
			}
		});

		rootPanel.getHeader().getTabBar().selectTab(1);
	}

	@Override
	public ReviewValidateDocServiceAsync createRpcService() {
		return GWT.create(ReviewValidateDocService.class);
	}

	@Override
	public String getHomePage() {
		return "BatchList.html";
	}

	@Override
	public LocaleInfo createLocaleInfo(String locale) {
		return new LocaleInfo(locale, "rvConstants", "rvMessages");
	}

	public native void defineBridgeMethod() /*-{
											var _this = this;
											$wnd.onunload = function() {
											return _this.@com.ephesoft.dcma.gwt.rv.client.ReviewValidate::onWindowClose()();
											}
											}-*/;

	private void onWindowClose() {
		String title = Window.getTitle();
		if (title != null && !title.isEmpty()) {
			int index = title.indexOf(ReviewValidateConstants.BATCH_INSTANCE_ABBREVIATION);
			if (index != -1) {
				String batchInstanceIdentifier = title.substring(index);
				updateEndTimeAndCleanCurrentBatch(batchInstanceIdentifier);
			}
		}
	}

	/**
	 * @param batchInstanceIdentifier
	 */
	private void updateEndTimeAndCleanCurrentBatch(String batchInstanceIdentifier) {
		if (batchInstanceIdentifier != null && !batchInstanceIdentifier.isEmpty()) {
			rpcService.updateEndTimeAndCalculateDuration(batchInstanceIdentifier, new EphesoftAsyncCallback<Void>() {

				@Override
				public void customFailure(Throwable arg0) {
					// no need to do anything on the failure of updation of Review-Valudate end time
				}

				@Override
				public void onSuccess(Void arg0) {
					// no need to do anything on the success of updation of Review-Valudate end time
				}

			});
			rpcService.cleanUpCurrentBatch(batchInstanceIdentifier, new EphesoftAsyncCallback<Void>() {

				@Override
				public void customFailure(Throwable arg0) {
					// no need to do anything if there is a failure in cleaning the current user of the current batch
				}

				@Override
				public void onFailure(Throwable arg0) {
					// no need to do anything if there is a failure in cleaning the current user of the current batch
				}

				@Override
				public void onSuccess(Void arg0) {
					// no need to do anything if the current user of the current batch is cleaned successfully
				}
			});
		}
	}
}
