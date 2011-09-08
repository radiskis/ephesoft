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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.MessageConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.PluginNameConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.plugin.DocTypeMappingView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.plugin.FuzzyDBPluginView.Id;
import com.ephesoft.dcma.gwt.core.shared.BatchClassDynamicPluginConfigDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginConfigDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.DocumentTypeDTO;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

public class DocTypeMappingPresenter extends AbstractBatchClassPresenter<DocTypeMappingView> {

	private final PluginDataPresenter pluginDataPresenter;

	public DocTypeMappingPresenter(BatchClassManagementController controller, DocTypeMappingView view) {
		super(controller, view);
		this.pluginDataPresenter = new PluginDataPresenter(controller, view.getPluginDataView());
	}

	@Override
	public void bind() {
		if (controller.getSelectedPlugin() != null
				&& controller.getSelectedPlugin().getPlugin().getPluginName().equals(PluginNameConstants.FUZZYDB_PLUGIN)) {
			this.pluginDataPresenter.bind();

			BatchClassPluginDTO batchPluginDTO = controller.getSelectedPlugin();
			for (BatchClassPluginConfigDTO pluginConfigDTO : batchPluginDTO.getBatchClassPluginConfigs()) {
				if (!(pluginConfigDTO.getName().equalsIgnoreCase(AdminConstants.DOCUMENT_TYPE)
						|| pluginConfigDTO.getName().equalsIgnoreCase(AdminConstants.FIELD_TYPE) || pluginConfigDTO.getName()
						.equalsIgnoreCase(AdminConstants.ROW_TYPE))) {
					if (pluginConfigDTO.getName().equalsIgnoreCase(AdminConstants.DATABASE_DRIVER)) {
						view.setDriverName(pluginConfigDTO.getValue());
					} else if (pluginConfigDTO.getName().equalsIgnoreCase(AdminConstants.DATABASE_URL)) {
						view.setUrl(pluginConfigDTO.getValue());
					} else if (pluginConfigDTO.getName().equalsIgnoreCase(AdminConstants.DATABASE_USERNAME)) {
						view.setUserName(pluginConfigDTO.getValue());
					} else if (pluginConfigDTO.getName().equalsIgnoreCase(AdminConstants.DATABASE_PASSWORD)) {
						view.setPassword(pluginConfigDTO.getValue());
					}
				}
			}

			controller.getRpcService().getAllTables(view.getDriverName(), view.getUrl(), view.getUserName(), view.getPassword(),
					new AsyncCallback<Map<String, List<String>>>() {

						@Override
						public void onSuccess(Map<String, List<String>> tableNames) {
							view.clearDetailsTable();
							controller.setTables(tableNames);
							Collection<DocumentTypeDTO> documentTypes = controller.getBatchClass().getDocuments();
							for (DocumentTypeDTO documentTypeDTO : documentTypes) {
								if (!documentTypeDTO.getName().equalsIgnoreCase(AdminConstants.DOCUMENT_TYPE_UNKNOWN)) {
									CustomWidget widget = new CustomWidget(documentTypeDTO.getName());
									view.addRow(widget);
								}
							}

						}

						@Override
						public void onFailure(Throwable arg0) {
							ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.INVALID_DB_CONNECTION);

						}
					});
		}
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		// TODO Auto-generated method stub
	}

	public class CustomWidget extends Composite {

		private final HorizontalPanel panel;
		private final Button associateAndVerify;
		private final ListBox tables;
		private final Label documentTypeLabel;
		private final Button editButton;

		public CustomWidget(final String documentType) {
			super();
			panel = new HorizontalPanel();
			associateAndVerify = new Button();
			editButton = new Button();
			editButton.setText(AdminConstants.EDIT_BUTTON);
			editButton.setVisible(false);
			associateAndVerify.setText(AdminConstants.MAP_BUTTON);
			tables = new ListBox();
			tables.addItem(AdminConstants.EMPTY_STRING);
			List<String> tableList = controller.getTables().get("TABLE");
			List<String> viewList = controller.getTables().get("VIEW");
			for (String tableName : tableList) {
				tables.addItem(tableName, "TABLE");
			}
			for (String viewName : viewList) {
				tables.addItem(viewName, "VIEW");
			}
			Collection<BatchClassDynamicPluginConfigDTO> batchClassPluginConfigDTOs = controller.getSelectedPlugin()
					.getBatchClassDynamicPluginConfigs();
			for (BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO : batchClassPluginConfigDTOs) {
				if (batchClassDynamicPluginConfigDTO.getDescription() != null
						&& batchClassDynamicPluginConfigDTO.getDescription().equalsIgnoreCase(documentType)) {
					for (int index = 0; index < tables.getItemCount(); index++) {
						if (tables.getItemText(index).equalsIgnoreCase(batchClassDynamicPluginConfigDTO.getValue())) {
							tables.setSelectedIndex(index);
							editButton.setVisible(true);
							associateAndVerify.setEnabled(false);
							associateAndVerify.setStyleName(AdminConstants.DISABLED_BUTTON_STYLE);
							tables.setEnabled(false);
						}
					}
				}
			}

			documentTypeLabel = new Label();
			documentTypeLabel.setText(documentType);
			editButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent arg0) {
					editButton.setVisible(false);
					associateAndVerify.setEnabled(true);
					associateAndVerify.setStyleName(AdminConstants.BUTTON_STYLE);
					tables.setEnabled(true);
				}
			});

			associateAndVerify.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent arg0) {
					List<String> childIds = null;
					Collection<BatchClassDynamicPluginConfigDTO> dynamicPluginConfigDTOs = controller.getSelectedPlugin()
							.getBatchClassDynamicPluginConfigs();
					boolean isMappingPresent = false;
					BatchClassPluginDTO selectedPlugin = controller.getSelectedPlugin();
					BatchClassDynamicPluginConfigDTO batchClassDynamicConfigDTO = new BatchClassDynamicPluginConfigDTO();
					for (BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO : dynamicPluginConfigDTOs) {
						if (batchClassDynamicPluginConfigDTO.getDescription() != null
								&& batchClassDynamicPluginConfigDTO.getDescription().equals(documentType)) {
							if (batchClassDynamicPluginConfigDTO.getChildren() != null
									&& !batchClassDynamicPluginConfigDTO.getValue().equalsIgnoreCase(
											tables.getItemText(tables.getSelectedIndex()))) {
								childIds = setChildIds(batchClassDynamicPluginConfigDTO);
								if (tables.getItemText(tables.getSelectedIndex()).length() == 0) {
									childIds.add(batchClassDynamicPluginConfigDTO.getIdentifier());
								}
								batchClassDynamicPluginConfigDTO.setChildren(null);
							}
							batchClassDynamicPluginConfigDTO.setValue(tables.getItemText(tables.getSelectedIndex()));
							batchClassDynamicConfigDTO = batchClassDynamicPluginConfigDTO;
							isMappingPresent = true;
						}

					}
					// remove childs...

					if (childIds != null) {
						controller.getBatchClass().setDirty(true);
						for (int index = 0; index < childIds.size(); index++) {
							for (BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO : dynamicPluginConfigDTOs) {
								if (childIds.get(index).equalsIgnoreCase(batchClassDynamicPluginConfigDTO.getIdentifier())) {
									dynamicPluginConfigDTOs.remove(batchClassDynamicPluginConfigDTO);
									break;
								}
							}
						}
					}

					if (!isMappingPresent && tables.getItemText(tables.getSelectedIndex()).length() > 0) {
						batchClassDynamicConfigDTO.setBatchClassPlugin(selectedPlugin);
						batchClassDynamicConfigDTO.setChildren(null);
						batchClassDynamicConfigDTO.setParent(null);
						batchClassDynamicConfigDTO.setDescription(documentType);
						batchClassDynamicConfigDTO.setName(AdminConstants.DOCUMENT_TYPE);
						String itemText = tables.getItemText(tables.getSelectedIndex());
						batchClassDynamicConfigDTO.setValue(itemText);
						batchClassDynamicConfigDTO.setIdentifier(String.valueOf(Id.getIdentifier()));
						selectedPlugin
								.addBatchClassDynamicPluginConfig(String.valueOf(Id.getIdentifier()), batchClassDynamicConfigDTO);
					}

					if (tables.getItemText(tables.getSelectedIndex()).length() > 0) {
						editButton.setVisible(true);
						associateAndVerify.setEnabled(false);
						associateAndVerify.setStyleName("disableButton");
						tables.setEnabled(false);
						controller.setBatchClassDynamicPluginConfigDTO(batchClassDynamicConfigDTO);
						controller.setSelectedTable(tables.getItemText(tables.getSelectedIndex()));
						controller.setSelectedTableType(tables.getValue(tables.getSelectedIndex()));
						controller.getMainPresenter().showDocTypeFieldMappingView(batchClassDynamicConfigDTO);
					}
				}
			});

			panel.setWidth("100%");
			panel.add(documentTypeLabel);
			panel.setCellWidth(documentTypeLabel, "30%");
			panel.add(tables);
			panel.setCellWidth(tables, "30%");
			panel.add(associateAndVerify);
			panel.setCellWidth(associateAndVerify, "30%");
			panel.add(editButton);
			panel.setCellWidth(editButton, "10%");
			panel.setSpacing(20);
			initWidget(panel);
		}

	}

	private List<String> setChildIds(BatchClassDynamicPluginConfigDTO batchConfigDTO) {
		Collection<BatchClassDynamicPluginConfigDTO> children = batchConfigDTO.getChildren();
		List<String> childIds = new ArrayList<String>();
		for (BatchClassDynamicPluginConfigDTO child : children) {
			childIds.add(child.getIdentifier());
		}
		return childIds;
	}

}
