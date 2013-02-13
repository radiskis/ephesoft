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

package com.ephesoft.dcma.gwt.uploadbatch.client;

import com.ephesoft.dcma.gwt.core.client.AbstractController;
import com.ephesoft.dcma.gwt.core.client.DCMARemoteServiceAsync;
import com.ephesoft.dcma.gwt.core.shared.BatchClassFieldDTO;
import com.ephesoft.dcma.gwt.uploadbatch.client.presenter.UploadBatchPresenter;
import com.ephesoft.dcma.gwt.uploadbatch.client.view.UploadBatchView;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Composite;

public class UploadBatchController extends AbstractController {

	private UploadBatchPresenter uploadBatchPresenter;
	private UploadBatchView uploadBatchView;
	private BatchClassFieldDTO selectedBatchClassField;
	private String currentBatchUploadFolder;
	private boolean finishButtonClicked;

	public UploadBatchController(HandlerManager eventBus, DCMARemoteServiceAsync rpcService) {
		super(eventBus, rpcService);
	}

	@Override
	public Composite createView() {
		this.uploadBatchView = new UploadBatchView();
		this.uploadBatchPresenter = new UploadBatchPresenter(this, uploadBatchView);
		return this.uploadBatchView;
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		/*
		 * injectEvents
		 */
	}

	@Override
	public UploadBatchServiceAsync getRpcService() {
		return (UploadBatchServiceAsync) super.getRpcService();
	}

	@Override
	public void refresh() {
		/*
		 * Refresh
		 */
	}

	public UploadBatchView getUploadBatchView() {
		return uploadBatchView;
	}

	public UploadBatchPresenter getMainPresenter() {
		return uploadBatchPresenter;
	}

	public BatchClassFieldDTO getSelectedBatchClassField() {
		return selectedBatchClassField;
	}

	public void setSelectedBatchClassField(BatchClassFieldDTO selectedBatchClassField) {
		this.selectedBatchClassField = selectedBatchClassField;
	}

	public String getCurrentBatchUploadFolder() {
		return this.currentBatchUploadFolder;
	}

	public void setCurrentBatchUploadFolder(String currentBatchUploadFolder) {
		this.currentBatchUploadFolder = currentBatchUploadFolder;
	}

	public boolean isFinishButtonClicked() {
		return finishButtonClicked;
	}

	public void setIsFinishButtonClicked(boolean finishButtonClicked) {
		this.finishButtonClicked = finishButtonClicked;
	}
}
