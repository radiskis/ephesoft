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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.functionkey;

import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.functionkey.FunctionKeyView;
import com.google.gwt.event.shared.HandlerManager;

/**
 * The presenter for view that shows the function key view details.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class FunctionKeyViewPresenter extends AbstractBatchClassPresenter<FunctionKeyView> {

	/**
	 * functionKeyDetailPresenter FunctionKeyDetailPresenter.
	 */
	private final FunctionKeyDetailPresenter functionKeyDetailPresenter;

	/**
	 * editFunctionKeyPresenter EditFunctionKeyPresenter.
	 */
	private final EditFunctionKeyPresenter editFunctionKeyPresenter;

	/**
	 * Constructor.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view FunctionKeyView
	 */
	public FunctionKeyViewPresenter(BatchClassManagementController controller, FunctionKeyView view) {
		super(controller, view);
		this.functionKeyDetailPresenter = new FunctionKeyDetailPresenter(controller, view.getFunctionKeyDetailView());
		this.editFunctionKeyPresenter = new EditFunctionKeyPresenter(controller, view.getEditFunctionKeyView());
	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	public void bind() {
		this.functionKeyDetailPresenter.bind();
		this.editFunctionKeyPresenter.bind();

	}

	/**
	 * To handle events.
	 * 
	 * @param eventBus HandlerManager
	 */
	@Override
	public void injectEvents(HandlerManager eventBus) {
		// no implementation
	}

	/**
	 * To show Edit Function Key View.
	 */
	public void showEditFunctionKeyView() {
		view.getFunctionKeyVerticalPanel().setVisible(Boolean.FALSE);
		view.getFunctionKeyConfigVerticalPanel().setVisible(Boolean.TRUE);
	}

	/**
	 * To show Function Key View.
	 */
	public void showFunctionKeyView() {
		view.getFunctionKeyVerticalPanel().setVisible(Boolean.TRUE);
		view.getFunctionKeyConfigVerticalPanel().setVisible(Boolean.FALSE);
	}

	/**
	 * To perform operations on Edit Function Key Properties button click.
	 */
	public void onEditFunctionKeyPropertiesButtonClicked() {
		controller.setAdd(false);
		showEditFunctionKeyView();
		controller.getBatchClass().setDirty(Boolean.TRUE);
		editFunctionKeyPresenter.bind();

	}

	/**
	 * To get Edit Function Key Presenter.
	 * 
	 * @return EditFunctionKeyPresenter
	 */
	public EditFunctionKeyPresenter getEditFunctionKeyPresenter() {
		return editFunctionKeyPresenter;
	}
}
