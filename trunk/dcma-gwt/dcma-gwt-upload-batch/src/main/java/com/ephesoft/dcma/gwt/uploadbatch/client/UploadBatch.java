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

package com.ephesoft.dcma.gwt.uploadbatch.client;

import com.ephesoft.dcma.gwt.core.client.DCMAEntryPoint;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleInfo;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.client.view.RootPanel;
import com.ephesoft.dcma.gwt.uploadbatch.client.i18n.UploadBatchConstants;
import com.ephesoft.dcma.gwt.uploadbatch.client.i18n.UploadBatchMessages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class UploadBatch extends DCMAEntryPoint<UploadBatchServiceAsync> {

	@Override
	public LocaleInfo createLocaleInfo(String locale) {
		return new LocaleInfo(locale, "uploadBatchConstants", "uploadBatchMesseges");
	}

	@Override
	public UploadBatchServiceAsync createRpcService() {
		return GWT.create(UploadBatchService.class);
	}

	@Override
	public String getHomePage() {
		return "UploadBatch.html";
	}
	

	@Override
	public void onLoad() {
		LayoutPanel layoutPanel = new LayoutPanel();

		UploadBatchController controller = new UploadBatchController(eventBus, rpcService);
		layoutPanel.add(controller.createView());
		
		controller.getMainPresenter().bind();
		final RootPanel rootPanel = new RootPanel(layoutPanel);

		rootPanel.getHeader().setEventBus(eventBus);
		
		rootPanel.getHeader().setDialogMessage(LocaleDictionary.get().getMessageValue(UploadBatchMessages.BACK_WITHOUT_FINISH_UPLOAD));
		rootPanel.getHeader().setButtonText(LocaleDictionary.get().getConstantValue(UploadBatchConstants.OK), LocaleDictionary.get().getConstantValue(UploadBatchConstants.CANCEL));
		rootPanel.getHeader().setShowDialogBoxOnTabClick(true);
		
		rootPanel.getHeader().addTab(LocaleDictionary.get().getConstantValue(UploadBatchConstants.TAB_LABEL_HOME), "BatchList.html",false);
		ScreenMaskUtility.maskScreen();

		rpcService.getRowsCount(new AsyncCallback<Integer>() {

			public void onFailure(Throwable caught) {
			}

			public void onSuccess(Integer result) {
				if (result == null || result.intValue() == 0) {
					rootPanel.getHeader().addNonClickableTab(LocaleDictionary.get().getConstantValue(UploadBatchConstants.TAB_LABEL_BATCH_DETAIL),
							"UploadBatch.html");
					rootPanel.getHeader().getTabBar().setTabEnabled(1, false);
				} else {
					rootPanel.getHeader().addTab(LocaleDictionary.get().getConstantValue(UploadBatchConstants.TAB_LABEL_BATCH_DETAIL),
							"ReviewValidate.html",false);
				}
				rootPanel.getHeader().getTabBar().selectTab(0);
				rootPanel.getHeader().addTab(LocaleDictionary.get().getConstantValue(UploadBatchConstants.TAB_LABEL_WEB_SCANNER),
						"WebScanner.html",false);
				
				rpcService.isUploadBatchEnabled(new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(Boolean isUploadBatchEnabled) {
						if(isUploadBatchEnabled) {
							rootPanel.getHeader().addNonClickableTab(LocaleDictionary.get().getConstantValue(UploadBatchConstants.TAB_LABEL_UPLOAD_BATCH),
									"UploadBatch.html");
						}
					}
				});
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
			}
		});

		RootLayoutPanel.get().add(rootPanel);
	}
}
