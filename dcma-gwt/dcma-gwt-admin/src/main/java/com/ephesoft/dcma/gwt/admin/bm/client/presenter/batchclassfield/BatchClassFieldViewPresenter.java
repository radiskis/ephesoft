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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.batchclassfield;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.batchclassfield.BatchClassFieldView;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.PaginationListner;
import com.google.gwt.event.shared.HandlerManager;

/**
 * The presenter for view that shows the batch class field view details.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class BatchClassFieldViewPresenter extends AbstractBatchClassPresenter<BatchClassFieldView> implements PaginationListner {

	/**
	 * batchClassFieldDetailPresenter BatchClassFieldDetailPresenter.
	 */
	private final BatchClassFieldDetailPresenter batchClassFieldDetailPresenter;

	/**
	 * editBatchClassFieldPresenter EditBatchClassFieldPresenter.
	 */
	private final EditBatchClassFieldPresenter editBatchClassFieldPresenter;

	/**
	 * Constructor.
	 * @param controller BatchClassManagementController
	 * @param view BatchClassFieldView
	 */
	public BatchClassFieldViewPresenter(BatchClassManagementController controller, BatchClassFieldView view) {

		super(controller, view);
		this.batchClassFieldDetailPresenter = new BatchClassFieldDetailPresenter(controller, view.getBatchClassFieldDetailView());
		this.editBatchClassFieldPresenter = new EditBatchClassFieldPresenter(controller, view.getEditBatchClassFieldView());
	}

	/**
	 * To show Batch Class Field View.
	 */
	public void showBatchClassFieldView() {
		view.getBatchClassFieldVerticalPanel().setVisible(Boolean.TRUE);
		view.getBatchClassFieldConfigVerticalPanel().setVisible(Boolean.FALSE);
	}

	/**
	 * To show Edit Batch Class Field View.
	 */
	public void showEditBatchClassFieldView() {
		view.getBatchClassFieldVerticalPanel().setVisible(Boolean.FALSE);
		view.getBatchClassFieldConfigVerticalPanel().setVisible(Boolean.TRUE);
	}

	/**
	 * In case of Detail View Clicked.
	 */
	public void onDetailViewClicked() {
		batchClassFieldDetailPresenter.bind();
	}

	/**
	 * To get Batch Class Field Detail Presenter.
	 * @return BatchClassFieldDetailPresenter
	 */
	public BatchClassFieldDetailPresenter getBatchClassFieldDetailPresenter() {
		return batchClassFieldDetailPresenter;
	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {
		this.batchClassFieldDetailPresenter.bind();
		this.editBatchClassFieldPresenter.bind();
	}

	/**
	 * To perform operations in case of edit batch class field properties button clicked.
	 */
	public void onEditBatchClassFieldPropertiesButtonClicked() {
		controller.setAdd(false);
		showEditBatchClassFieldView();
		controller.getBatchClass().setDirty(Boolean.TRUE);
		editBatchClassFieldPresenter.bind();
	}

	/**
	 * To handle events.
	 * 
	 * @param eventBus HandlerManager
	 */
	@Override
	public void injectEvents(HandlerManager eventBus) {
		// Event handling to be done here.
	}

	/**
	 * In case of pagination.
	 * 
	 * @param startIndex int
	 * @param maxResult int
	 * @param paramOrder Order
	 */
	@Override
	public void onPagination(int startIndex, int maxResult, Order order) {
		order.getSortProperty();
		order.isAscending();
	}
}
