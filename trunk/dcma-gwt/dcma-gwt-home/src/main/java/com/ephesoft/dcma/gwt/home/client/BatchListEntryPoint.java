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

package com.ephesoft.dcma.gwt.home.client;

import com.ephesoft.dcma.gwt.core.client.DCMAEntryPoint;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleInfo;
import com.ephesoft.dcma.gwt.core.client.view.RootPanel;
import com.ephesoft.dcma.gwt.core.shared.DataFilter;
import com.ephesoft.dcma.gwt.home.client.i18n.BatchListConstants;
import com.ephesoft.dcma.gwt.home.client.presenter.LandingPresenter;
import com.ephesoft.dcma.gwt.home.client.view.LandingView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * This is the entry point class for Batch List. It creates the view that the user gets when landing on the batch list page. It sets
 * the username, creates the tab's required, and also the view for each tab.
 * 
 * @author Ephesoft
 * @version 1.0
 * 
 */

public class BatchListEntryPoint extends DCMAEntryPoint<TableModelServiceAsync> {
	BatchListController controller = null;

	@Override
	public TableModelServiceAsync createRpcService() {
		return GWT.create(TableModelService.class);
	}

	@Override
	public void onLoad() {

		Document.get().setTitle(LocaleDictionary.get().getConstantValue(BatchListConstants.BATCH_LIST_TITLE));
		controller = new BatchListController(eventBus, rpcService);
		controller.createView();
		LandingView landingView = controller.getView();
		LandingPresenter landingPresenter = controller.getPresenter().getLandingPresenter();
		landingPresenter.bind();

		final RootPanel rootPanel = new RootPanel(landingView.getOuter());

		rootPanel.addStyleName("set_position");
		rootPanel.getHeader().setEventBus(eventBus);
		rootPanel.getHeader().addTab(LocaleDictionary.get().getConstantValue(BatchListConstants.TAB_LABEL_HOME), "BatchList.html",
				false);
		rootPanel.getHeader().getTabBar().selectTab(0);

		DataFilter[] filters = new DataFilter[2];
		rpcService.getRowsCount(filters, new AsyncCallback<Integer>() {

			public void onFailure(final Throwable caught) {
				// Do not create any tabs in case of failure
			}

			public void onSuccess(final Integer result) {
				if (result == null || result.intValue() == 0) {
					rootPanel.getHeader().addTab(LocaleDictionary.get().getConstantValue(BatchListConstants.TAB_LABEL_BATCH_DETAIL),
							"BatchList.html", false);
					rootPanel.getHeader().getTabBar().setTabEnabled(1, false);
				} else {
					rootPanel.getHeader().addTab(LocaleDictionary.get().getConstantValue(BatchListConstants.TAB_LABEL_BATCH_DETAIL),
							"ReviewValidate.html", false);
				}
				rootPanel.getHeader().addTab(LocaleDictionary.get().getConstantValue(BatchListConstants.TAB_LABEL_WEB_SCANNER),
						"WebScanner.html", false);
			}
		});

		rpcService.getUserName(new AsyncCallback<String>() {

			@Override
			public void onSuccess(final String userName) {
				rootPanel.getHeader().setUserName(userName);
			}

			@Override
			public void onFailure(final Throwable arg0) {
				// Username cannot be set if the call failed.
			}
		});

		RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
		rootLayoutPanel.clear();
		rootLayoutPanel.add(rootPanel);

		final FocusPanel focusPanel = new FocusPanel();
		focusPanel.add(rootLayoutPanel);

		com.google.gwt.user.client.ui.RootPanel.get().add(focusPanel);

	}

	@Override
	public String getHomePage() {
		return "BatchList.html";
	}

	@Override
	public LocaleInfo createLocaleInfo(final String locale) {
		return new LocaleInfo(locale, "batchListConstants", "batchListMessages");
	}
	
	
	public BatchListController getController() {
		return controller;
	}
}
