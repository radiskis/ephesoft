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

package com.ephesoft.dcma.gwt.rv.client;

import com.ephesoft.dcma.gwt.core.client.DCMAEntryPoint;
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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class ReviewValidate extends DCMAEntryPoint<ReviewValidateDocServiceAsync> {
	
	@Override
	public void onLoad() {
		
		Document.get().setTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.rv_title));
		ReviewValidateController controller = new ReviewValidateController(rpcService, eventBus);
		
		ReviewValidateView view = controller.getPresenter().getView();
		
		final RootPanel rootPanel = new RootPanel(view.getOuter());
		rootPanel.getHeader().setEventBus(eventBus);
		rootPanel.getHeader().setShowDialogBoxOnTabClick(true);
		rootPanel.getHeader().setDialogMessage(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.msg_backButton_confm));
		rootPanel.getHeader().addTab(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.tabLabel_home), "BatchList.html",true);
		rootPanel.getHeader().addTab(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.tabLabel_batch_detail), "ReviewValidate.html",true);
		rootPanel.getHeader().addTab(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.tabLabel_web_scanner),"WebScanner.html",true);
		rootPanel.getHeader().getTabBar().selectTab(1);
		ScreenMaskUtility.maskScreen();
		
		rpcService.getRowsCount(new AsyncCallback<Integer>() {

			public void onFailure(Throwable caught) {
			}

			public void onSuccess(Integer result) {
				if(result == null || result.intValue() == 0){
					rootPanel.getHeader().getTabBar().setTabEnabled(1, false);
				}
			}
		});
		
		rpcService.getUserName(new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String userName) {
				rootPanel.getHeader().setUserName(userName);
				ScreenMaskUtility.unmaskScreen();
			}
			
			@Override
			public void onFailure(Throwable arg0) {
				ScreenMaskUtility.unmaskScreen();
				 ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						 ReviewValidateMessages.msg_userName_error));
			}
		});
		
		RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
		rootLayoutPanel.clear();
		rootLayoutPanel.add(rootPanel);

		final FocusPanel focusPanel = new FocusPanel();
		focusPanel.add(rootLayoutPanel);
		
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
		
		com.google.gwt.user.client.ui.RootPanel.get().add(focusPanel);
		
		controller.go(null);
		
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
}
