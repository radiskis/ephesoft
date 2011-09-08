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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.email;

import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.email.EmailView;
import com.google.gwt.event.shared.HandlerManager;

public class EmailViewPresenter extends AbstractBatchClassPresenter<EmailView> {

	private final EmailDetailPresenter emailDetailPresenter;

	private final EditEmailPresenter editEmailPresenter;

	public EmailViewPresenter(BatchClassManagementController controller, EmailView view) {

		super(controller, view);
		this.emailDetailPresenter = new EmailDetailPresenter(controller, view.getEmailDetailView());
		this.editEmailPresenter = new EditEmailPresenter(controller, view.getEditEmailView());
	}

	public void showEmailView() {
		view.getEmailVerticalPanel().setVisible(Boolean.TRUE);
		view.getEmailConfigVerticalPanel().setVisible(Boolean.FALSE);
	}

	public void showEditEmailView() {
		view.getEmailVerticalPanel().setVisible(Boolean.FALSE);
		view.getEmailConfigVerticalPanel().setVisible(Boolean.TRUE);
	}

	public void onDetailViewClicked() {
		emailDetailPresenter.bind();
	}

	public EmailDetailPresenter getEmailDetailPresenter() {
		return emailDetailPresenter;
	}

	@Override
	public void bind() {
		this.editEmailPresenter.bind();
		this.emailDetailPresenter.bind();
	}

	public void onEditEmailPropertiesButtonClicked() {
		controller.setAdd(false);
		editEmailPresenter.bind();
		showEditEmailView();
		controller.getBatchClass().setDirty(Boolean.TRUE);
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		// Event handling to be done here.
	}
}
