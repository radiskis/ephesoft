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

package com.ephesoft.dcma.gwt.foldermanager.client.presenter;

import java.util.Map;

import com.ephesoft.dcma.gwt.core.client.EphesoftAsyncCallback;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.foldermanager.client.FolderManagementController;
import com.ephesoft.dcma.gwt.foldermanager.client.view.FolderManagementView;
import com.ephesoft.dcma.gwt.foldermanager.shared.constant.FolderManagementMessages;
import com.google.gwt.event.shared.HandlerManager;

public class FolderManagementPresenter extends AbstractFolderManagementPresenter<FolderManagementView> {

	public FolderManagementPresenter(FolderManagementController controller, FolderManagementView view) {
		super(controller, view);
		init();
	}

	public final void init() {
		controller.getRpcService().getParentFolder(new EphesoftAsyncCallback<String>() {

			@Override
			public void customFailure(Throwable caught) {
				ScreenMaskUtility.unmaskScreen();
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						FolderManagementMessages.UNABLE_TO_FETCH_BASE_FOLDER_PATH), true);
			}

			@Override
			public void onSuccess(final String parentFolderPath) {
				controller.getRpcService().getBatchClassNames(new EphesoftAsyncCallback<Map<String, String>>() {

					@Override
					public void onSuccess(final Map<String, String> batchClassesMap) {

						controller.getRpcService().getBaseHttpURL(new EphesoftAsyncCallback<String>() {

							@Override
							public void customFailure(Throwable caught) {
								ScreenMaskUtility.unmaskScreen();
								ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
										FolderManagementMessages.UNABLE_TO_FETCH_BASE_HTTP_URL), true);
							}

							@Override
							public void onSuccess(final String baseFolderUrl) {
								ScreenMaskUtility.unmaskScreen();
								view.setInitialFolderManagementView(controller, parentFolderPath, baseFolderUrl, batchClassesMap);
							}
						});
					}

					@Override
					public void customFailure(Throwable caught) {
						ScreenMaskUtility.unmaskScreen();
						ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
								FolderManagementMessages.UNABLE_TO_FETCH_BATCH_CLASSES_BY_ROLES_ASSIGNED_TO_USER), true);

					}
				});
			}
		});
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		//No need to use the event injection in this class
	}

	@Override
	public void bind() {
		//No need to use the bind method in this class
	}

}
