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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.scanner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.scanner.ScannerListView;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.table.Record;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.DoubleClickListner;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.PaginationListner;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.WebScannerConfigurationDTO;
import com.google.gwt.event.shared.HandlerManager;

/**
 * The presenter for view that shows the scanner list details.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class ScannerListPresenter extends AbstractBatchClassPresenter<ScannerListView> implements PaginationListner,
		DoubleClickListner {

	/**
	 * scannerConfigurationDTOList Collection<WebScannerConfigurationDTO>.
	 */
	private Collection<WebScannerConfigurationDTO> scannerConfigurationDTOList;

	/**
	 * To get scanner configuration DTO list.
	 * 
	 * @return Collection<WebScannerConfigurationDTO>
	 */
	public Collection<WebScannerConfigurationDTO> getScannerConfigurationDTOList() {
		return scannerConfigurationDTOList;
	}

	/**
	 * To set scanner configuration DTO list.
	 * 
	 * @param ScannerConfigurationDTOList Collection<WebScannerConfigurationDTO>
	 */
	public void setScannerConfigurationDTOList(Collection<WebScannerConfigurationDTO> ScannerConfigurationDTOList) {
		this.scannerConfigurationDTOList = ScannerConfigurationDTOList;
	}

	/**
	 * Constructor.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view ScannerListView
	 */
	public ScannerListPresenter(BatchClassManagementController controller, ScannerListView view) {
		super(controller, view);
	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {
		// No implementation
	}

	/**
	 * To handle events.
	 * 
	 * @param eventBus HandlerManager
	 */
	@Override
	public void injectEvents(HandlerManager eventBus) {
		// No implementation
	}

	/**
	 * In case of Double Click on Table, open the edit view.
	 */
	@Override
	public void onDoubleClickTable() {
		onEditButtonClicked();
	}

	/**
	 * To perform operations in case of edit button clicked.
	 */
	public void onEditButtonClicked() {
		String rowIndex = view.getScannerListView().getSelectedRowIndex();
		int rowCount = view.getScannerListView().getTableRecordCount();
		if (rowIndex == null || rowIndex.isEmpty()) {
			if (rowCount == 0) {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NO_RECORD_TO_EDIT), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.EDIT_EMAIL_CONFIGURATION_TITLE), Boolean.TRUE);
			} else {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NONE_SELECTED_WARNING), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.EDIT_EMAIL_CONFIGURATION_TITLE), Boolean.TRUE);
			}

		} else {
			controller.getMainPresenter().getBatchClassViewPresenter().onEditScannerButtonClicked(rowIndex);
		}
	}

	/**
	 * To perform operations on pagination.
	 * 
	 * @param startIndex int
	 * @param maxResult int
	 * @param order Order
	 */
	@Override
	public void onPagination(int startIndex, int maxResult, Order order) {
		List<WebScannerConfigurationDTO> newScannerConfigurationDTOList = new ArrayList<WebScannerConfigurationDTO>(
				scannerConfigurationDTOList);
		List<Record> scannerConfigurationRecordList = getController().getMainPresenter().getBatchClassViewPresenter().getView()
				.setScannerList(newScannerConfigurationDTOList);
		int totalSize = scannerConfigurationRecordList.size();
		int lastIndex = startIndex + maxResult;
		int count = Math.min(totalSize, lastIndex);
		this.getView().getScannerListView().updateRecords(scannerConfigurationRecordList.subList(startIndex, count), startIndex,
				totalSize);
	}

}
