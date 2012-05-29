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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin;

import java.util.Collection;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.plugin.KV_PP_ConfigView;
import com.ephesoft.dcma.gwt.core.client.RandomIdGenerator;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.DoubleClickListner;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginConfigDTO;
import com.ephesoft.dcma.gwt.core.shared.KVPageProcessDTO;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Widget;

public class KV_PP_ConfigPresenter extends AbstractBatchClassPresenter<KV_PP_ConfigView> implements DoubleClickListner{

	private BatchClassPluginConfigDTO batchClassPluginConfigDTO;

	private KVPageProcessDTO kvPageProcessDTO;

	private Collection<KVPageProcessDTO> kvPageProcessDTOs;

	public KV_PP_ConfigPresenter(BatchClassManagementController controller, KV_PP_ConfigView view) {
		super(controller, view);
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
	}

	@Override
	public void bind() {
		if (controller.getSelectedPlugin() != null) {
			batchClassPluginConfigDTO = controller.getSelectedPlugin().getBatchClassPluginConfigDTOByName(
					AdminConstants.KV_PAGE_PROCESS_KV_PATTERN);
			if (batchClassPluginConfigDTO != null) {
				kvPageProcessDTOs = batchClassPluginConfigDTO.getKvPageProcessDTOs();
				view.createKVPageProcessList(kvPageProcessDTOs);
				view.getEditKVPPConfigButton().setEnabled(false);
				view.getDeleteKVPPConfigButton().setEnabled(false);
				disableButtonCSS(view.getEditKVPPConfigButton(), view.getDeleteKVPPConfigButton());
				if (view.getKvPPConfigListView().listView.getTableRecordCount() > 0) {
					view.getEditKVPPConfigButton().setEnabled(true);
					view.getDeleteKVPPConfigButton().setEnabled(true);
					enableButtonCSS(view.getEditKVPPConfigButton(), view.getDeleteKVPPConfigButton());
				}
			}
		}
	}

	public void onEditKVPPClicked() {
		String selectedRow = view.getKvPPConfigListView().listView.getSelectedRowIndex();
		long selectedRowIndex;
		if (selectedRow != null && !selectedRow.isEmpty()) {
			selectedRowIndex = Long.valueOf(selectedRow);
			Collection<KVPageProcessDTO> kvPageProcessDTOs = batchClassPluginConfigDTO.getKvPageProcessDTOs();
			if (kvPageProcessDTOs != null && !kvPageProcessDTOs.isEmpty()) {
				for (KVPageProcessDTO kvPageProcessDTO : kvPageProcessDTOs) {
					if (kvPageProcessDTO.getIdentifier() == selectedRowIndex) {
						controller.setKvPageProcessDTO(kvPageProcessDTO);
						kvPageProcessDTO.setBatchClassPluginConfigDTO(batchClassPluginConfigDTO);
						break;
					}
				}
			}
			controller.setPluginConfigDTO(batchClassPluginConfigDTO);
			controller.setAdd(false);
		}
		controller.getMainPresenter().showKVppPluginConfigAddEditView();
		controller.getMainPresenter().getKvPPAddEditListPresenter().showKVPPEditView();
	}

	public void onAddKVPPClicked() {
		kvPageProcessDTO = new KVPageProcessDTO();
		kvPageProcessDTO.setIsNew(true);
		kvPageProcessDTO.setIdentifier(RandomIdGenerator.getIdentifier());
		kvPageProcessDTO.setBatchClassPluginConfigDTO(batchClassPluginConfigDTO);
		controller.setPluginConfigDTO(batchClassPluginConfigDTO);
		controller.setAdd(true);
		controller.setKvPageProcessDTO(kvPageProcessDTO);
		controller.getMainPresenter().showKVppPluginConfigAddEditView();
		controller.getMainPresenter().getKvPPAddEditListPresenter().showKVPPEditView();
	}

	public void onDeleteKVPPClicked() {
		long selectedRowIndex = Long.valueOf(view.getKvPPConfigListView().listView.getSelectedRowIndex());
		Collection<KVPageProcessDTO> kvPageProcessDTOs = batchClassPluginConfigDTO.getKvPageProcessDTOs();
		if (kvPageProcessDTOs != null && !kvPageProcessDTOs.isEmpty()) {
			for (KVPageProcessDTO kvPageProcessDTO : kvPageProcessDTOs) {
				if (kvPageProcessDTO.getIdentifier() == selectedRowIndex) {
					kvPageProcessDTO.setIsDeleted(Boolean.TRUE);
					kvPageProcessDTO.setBatchClassPluginConfigDTO(batchClassPluginConfigDTO);
					controller.getMainPresenter().getKvPPPropertiesPresenter().bind();
					controller.getMainPresenter().showKVppPluginView();
					controller.getMainPresenter().getKvPPPropertiesPresenter().showKVppPluginConfigView();
					controller.getBatchClass().setDirty(true);
					break;
				}
			}
		}
	}

	private void enableButtonCSS(Widget... widgets) {
		for (Widget widget : widgets) {
			widget.removeStyleName(AdminConstants.DISABLED_BUTTON_STYLE);
			widget.addStyleName(AdminConstants.BUTTON_STYLE);
		}
	}

	private void disableButtonCSS(Widget... widgets) {
		for (Widget widget : widgets) {
			widget.removeStyleName(AdminConstants.BUTTON_STYLE);
			widget.addStyleName(AdminConstants.DISABLED_BUTTON_STYLE);
		}
	}

	public BatchClassPluginConfigDTO getBatchClassPluginConfigDTO() {
		return batchClassPluginConfigDTO;
	}

	public KVPageProcessDTO getKvPageProcessDTO() {
		return kvPageProcessDTO;
	}

	@Override
	public void onDoubleClickTable() {
		onEditKVPPClicked();
	}
}
