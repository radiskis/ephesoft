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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.da.property.ModuleProperty;
import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.module.ModuleListView;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.table.Record;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.DoubleClickListner;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.PaginationListner;
import com.ephesoft.dcma.gwt.core.shared.BatchClassModuleDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.comparator.ModuleComparator;
import com.google.gwt.event.shared.HandlerManager;

public class ModuleListPresenter extends AbstractBatchClassPresenter<ModuleListView> implements PaginationListner, DoubleClickListner {

	private Collection<BatchClassModuleDTO> ModuleDTOList;

	public Collection<BatchClassModuleDTO> getModuleDTOList() {
		return ModuleDTOList;
	}

	public void setModuleDTOList(Collection<BatchClassModuleDTO> moduleDTOList) {
		ModuleDTOList = moduleDTOList;
	}

	public ModuleListPresenter(BatchClassManagementController controller, ModuleListView view) {
		super(controller, view);
	}

	@Override
	public void bind() {

	}

	@Override
	public void injectEvents(HandlerManager eventBus) {

	}

	@Override
	public void onPagination(int startIndex, int maxResult, Order order) {
		if (order == null) {
			order = new Order(ModuleProperty.ORDER, true);
		}
		ModuleComparator comparator = new ModuleComparator(order);
		List<BatchClassModuleDTO> newModuleDTOList = new ArrayList<BatchClassModuleDTO>(ModuleDTOList);
		Collections.sort(newModuleDTOList, comparator);
		List<Record> moduleRecordList = getController().getMainPresenter().getView().getBatchClassView().setModuleList(
				newModuleDTOList);
		int totalSize = moduleRecordList.size();
		int lastIndex = startIndex + maxResult;
		int count = Math.min(totalSize, lastIndex);
		this.getView().getModuleListView().updateRecords(moduleRecordList.subList(startIndex, count), startIndex, totalSize);
	}

	@Override
	public void onDoubleClickTable() {
		onEditButtonClicked();
	}

	public void onEditButtonClicked() {
		String identifier = view.getModuleListView().getSelectedRowIndex();
		if (identifier == null || identifier.isEmpty()) {
			ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.NONE_SELECTED_WARNING), LocaleDictionary.get().getConstantValue(
					BatchClassManagementConstants.EDIT_MODULE_TITLE), Boolean.TRUE);

		} else {
			BatchClassModuleDTO batchClassModuleDTO = controller.getBatchClass().getModuleByIdentifier(identifier);
			controller.getMainPresenter().showModuleView(batchClassModuleDTO);
		}
	}
}
