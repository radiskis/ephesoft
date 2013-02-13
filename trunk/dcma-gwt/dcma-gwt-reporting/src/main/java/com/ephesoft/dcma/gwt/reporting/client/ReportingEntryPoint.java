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

package com.ephesoft.dcma.gwt.reporting.client;

import com.ephesoft.dcma.gwt.core.client.DCMAEntryPoint;
import com.ephesoft.dcma.gwt.core.client.EphesoftAsyncCallback;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleInfo;
import com.ephesoft.dcma.gwt.core.client.view.RootPanel;
import com.ephesoft.dcma.gwt.reporting.client.presenter.ReportingPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * This is the entry point class for Reporting Module. It creates the view that the user gets when landing on the reporting page. It
 * sets the username, creates the tab's required, and also the view for each tab.
 * 
 * @author Ephesoft
 * @version 1.0
 * 
 */

public class ReportingEntryPoint extends DCMAEntryPoint<ReportingModelServiceAsync> {

	private ReportingController controller = null;

	@Override
	public ReportingModelServiceAsync createRpcService() {
		return GWT.create(ReportingModelService.class);
	}

	@Override
	public void onLoad() {

		LayoutPanel layoutPanel = new LayoutPanel();
		controller = new ReportingController(eventBus, rpcService);
		layoutPanel.add(controller.createView());
		ReportingPresenter reportingPresenter = controller.getPresenter();
		reportingPresenter.bind();

		final RootPanel rootPanel = new RootPanel(layoutPanel, rpcService);

		rootPanel.getHeader().addTab("Batch Class Management", "BatchClassManagement.html", false);
		rootPanel.getHeader().addTab("Batch Instance Management", "BatchInstanceManagement.html", false);
		rootPanel.getHeader().addTab("Workflow Management", "CustomWorkflowManagement.html", false);
		rootPanel.getHeader().addTab("Folder Management", "FolderManager.html", false);
		rootPanel.getHeader().addNonClickableTab("Reports", "Reporting.html");
		rootPanel.getHeader().getTabBar().selectTab(4);
		rootPanel.addStyleName("set_position");
		rootPanel.getHeader().setEventBus(eventBus);

		rpcService.getUserName(new EphesoftAsyncCallback<String>() {

			@Override
			public void onSuccess(final String userName) {
				rootPanel.getHeader().setUserName(userName);
			}

			@Override
			public void customFailure(final Throwable arg0) {
				// Username cannot be set if the call failed.
			}
		});

		RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
		rootLayoutPanel.clear();
		rootLayoutPanel.add(rootPanel);

	}

	@Override
	public String getHomePage() {
		return "Reporting.html";
	}

	@Override
	public LocaleInfo createLocaleInfo(final String locale) {
		return new LocaleInfo(locale, "reportingConstants", "reportingMessages");
	}

	public ReportingController getController() {
		return controller;
	}
}
