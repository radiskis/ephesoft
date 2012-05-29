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
import com.ephesoft.dcma.gwt.admin.bm.client.view.plugin.DocTypeFieldsMappingView;
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

public class DocTypeFieldsMappingPresenter extends AbstractBatchClassPresenter<DocTypeFieldsMappingView> {

	private final FuzzyDBDocTypeDetailPresenter fuzzyDBDocTypeDetailPresenter;

	public DocTypeFieldsMappingPresenter(BatchClassManagementController controller, DocTypeFieldsMappingView view) {
		super(controller, view);
		this.fuzzyDBDocTypeDetailPresenter = new FuzzyDBDocTypeDetailPresenter(controller, view.getFuzzyDBDocTypeDetailView());

	}

	@Override
	public void bind() {
		if (!(controller.getSelectedPlugin() != null && controller.getSelectedPlugin().getPlugin().getPluginName().equals(
				PluginNameConstants.FUZZYDB_PLUGIN))) {
			return;
		}
		view.clearDetailsTable();
		fuzzyDBDocTypeDetailPresenter.bind();
		BatchClassPluginDTO batchPluginDTO = controller.getSelectedPlugin();
		for (BatchClassPluginConfigDTO pluginConfigDTO : batchPluginDTO.getBatchClassPluginConfigs()) {
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

	public void setColumnsToFuzzyDBView() {
		controller.getRpcService().getAllColumnsForTable(view.getDriverName(), view.getUrl(), view.getUserName(), view.getPassword(),
				controller.getSelectedTable(), new AsyncCallback<Map<String, String>>() {

					@Override
					public void onSuccess(Map<String, String> map) {
						controller.setColumnsMap(map);
						Collection<DocumentTypeDTO> documentTypeDTOs = controller.getBatchClass().getDocuments();
						for (final DocumentTypeDTO documentTypeDTO : documentTypeDTOs) {
							if (controller.getBatchClassDynamicPluginConfigDTO() != null
									&& documentTypeDTO.getName().equalsIgnoreCase(
											controller.getBatchClassDynamicPluginConfigDTO().getDescription())) {
								String documentName = documentTypeDTO.getName();
								controller.getRpcService().getDocumentLevelFields(documentName,
										controller.getBatchClass().getIdentifier(), new AsyncCallback<Map<String, String>>() {

											@Override
											public void onFailure(Throwable arg0) {
												ConfirmationDialogUtil
														.showConfirmationDialogError(MessageConstants.UNABLE_TO_RETRIEVE_FIELDS);

											}

											@Override
											public void onSuccess(Map<String, String> docFieldsVsDataTypeMap) {
												controller.setSelectedDocument(documentTypeDTO);
												controller.setDocFieldsVsDataTypeMap(docFieldsVsDataTypeMap);
												CustomWidget widget = new CustomWidget(AdminConstants.ROWID);
												view.addRow(widget);
												for (String fieldName : docFieldsVsDataTypeMap.keySet()) {
													CustomWidget widget1 = new CustomWidget(fieldName);
													view.addRow(widget1);
												}
											}
										});
								break;
							}
						}
					}

					@Override
					public void onFailure(Throwable arg0) {
						ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.UNABLE_TO_RETRIEVE_COLUMNS);

					}
				});
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		// To be used in case of event handling.
	}

	public class CustomWidget extends Composite {

		private final HorizontalPanel panel;
		private final Button associateAndVerify;
		private final ListBox fields;
		private final Label fieldTypeLabel;
		private final Button editButton;

		public CustomWidget(final String fieldType) {
			super();
			String tobeDeletedConfig = null;
			panel = new HorizontalPanel();
			associateAndVerify = new Button();
			editButton = new Button();
			editButton.setText(AdminConstants.EDIT_BUTTON);
			editButton.setVisible(false);
			associateAndVerify.setText(AdminConstants.MAP_BUTTON);
			fields = new ListBox();
			fields.addItem(AdminConstants.EMPTY_STRING);
			for (String fieldName : controller.getColumnsMap().keySet()) {
				fields.addItem(fieldName);
			}

			if (controller.getBatchClassDynamicPluginConfigDTO() != null
					&& controller.getBatchClassDynamicPluginConfigDTO().getChildren() != null) {
				Collection<BatchClassDynamicPluginConfigDTO> children = controller.getBatchClassDynamicPluginConfigDTO().getChildren();
				for (BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO : children) {
					boolean columnExists = false;
					if (batchClassDynamicPluginConfigDTO.getDescription() != null
							&& batchClassDynamicPluginConfigDTO.getDescription().equalsIgnoreCase(fieldType)) {
						for (int index = 0; index < fields.getItemCount(); index++) {
							if (fields.getItemText(index).equalsIgnoreCase(batchClassDynamicPluginConfigDTO.getValue())) {
								columnExists = true;
								fields.setSelectedIndex(index);
								editButton.setVisible(true);
								associateAndVerify.setEnabled(false);
								associateAndVerify.setStyleName(AdminConstants.DISABLED_BUTTON_STYLE);
								fields.setEnabled(false);
							}
						}
						if (!columnExists) {
							controller.getBatchClass().setDirty(true);
							tobeDeletedConfig = batchClassDynamicPluginConfigDTO.getIdentifier();
							break;
						}
					}
				}
			}

			fieldTypeLabel = new Label();
			fieldTypeLabel.setText(fieldType);
			editButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent arg0) {
					editButton.setVisible(false);
					associateAndVerify.setEnabled(true);
					associateAndVerify.setStyleName(AdminConstants.BUTTON_STYLE);
					fields.setEnabled(true);
				}
			});

			associateAndVerify.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent arg0) {

					final BatchClassPluginDTO selectedPlugin = controller.getSelectedPlugin();
					Collection<BatchClassDynamicPluginConfigDTO> children = controller.getBatchClassDynamicPluginConfigDTO()
							.getChildren();
					String toBeDeleted = null;
					if (children != null && !children.isEmpty() && fields.getItemText(fields.getSelectedIndex()).length() == 0) {
						for (BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO : children) {
							if ((batchClassDynamicPluginConfigDTO.getDescription() != null && batchClassDynamicPluginConfigDTO
									.getDescription().equals(fieldType))
									|| (batchClassDynamicPluginConfigDTO.getDescription() == null && batchClassDynamicPluginConfigDTO
											.getName().equalsIgnoreCase(AdminConstants.ROWID))) {
								controller.getBatchClass().setDirty(true);
								toBeDeleted = batchClassDynamicPluginConfigDTO.getIdentifier();
								break;
							}

						}

						if (toBeDeleted != null) {
							selectedPlugin.getBatchClassDynamicPluginConfigs().remove(
									selectedPlugin.getBatchClassDynamicPluginConfigDTOById(toBeDeleted));
							controller.getBatchClassDynamicPluginConfigDTO().getChildren().remove(
									controller.getBatchClassDynamicPluginConfigDTO().getChildById(toBeDeleted));
						}
						return;
					}

					if (fieldType.equalsIgnoreCase(AdminConstants.ROWID)) {
						if (controller.getSelectedTableType().equals("TABLE")) {
							controller.getRpcService().getAllPrimaryKeysForTable(view.getDriverName(), view.getUrl(),
									view.getUserName(), view.getPassword(), controller.getSelectedTable(),
									controller.getSelectedTableType(), new AsyncCallback<List<String>>() {

										@Override
										public void onSuccess(List<String> list) {
											boolean isPrimaryKey = false;
											int index = fields.getSelectedIndex();
											for (String key : list) {
												if (fields.getItemText(index).equals(key)) {
													isPrimaryKey = true;
													setPluginConfigDTOForRowId(fieldType, selectedPlugin);
													break;
												}
											}
											if (!isPrimaryKey) {
												ConfirmationDialogUtil
														.showConfirmationDialogError(MessageConstants.ROWID_SET_INCORRECTLY);
												return;
											}
										}

										@Override
										public void onFailure(Throwable arg0) {
											ConfirmationDialogUtil
													.showConfirmationDialogError(MessageConstants.UNABLE_TO_RETRIEVE_COLUMNS);
											return;
										}
									});
						} else {
							setPluginConfigDTOForRowId(fieldType, selectedPlugin);

						}

					} else {
						String columnDataType = controller.getColumnsMap().get(fields.getItemText(fields.getSelectedIndex()));
						String fieldDataType = convertFieldType(controller.getDocFieldsVsDataTypeMap().get(fieldType));

						if (!columnDataType.contains(fieldDataType)) {
							ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.DATATYPE_COMPATIBILITY_CHECK);
							return;
						} else {
							getBatchClassDynamicPluginConfigDTO(selectedPlugin, fieldType);
							editButton.setVisible(true);
							associateAndVerify.setEnabled(false);
							associateAndVerify.setStyleName(AdminConstants.DISABLED_BUTTON_STYLE);
							fields.setEnabled(false);
						}
					}
				}
			});

			if (tobeDeletedConfig != null) {
				controller.getSelectedPlugin().getBatchClassDynamicPluginConfigs().remove(
						controller.getSelectedPlugin().getBatchClassDynamicPluginConfigDTOById(tobeDeletedConfig));
				controller.getBatchClassDynamicPluginConfigDTO().getChildren().remove(
						controller.getBatchClassDynamicPluginConfigDTO().getChildById(tobeDeletedConfig));
			}
			panel.setWidth("100%");
			panel.add(fieldTypeLabel);
			panel.setCellWidth(fieldTypeLabel, "30%");
			panel.add(fields);
			panel.setCellWidth(fields, "30%");
			panel.add(associateAndVerify);
			panel.setCellWidth(associateAndVerify, "30%");
			panel.add(editButton);
			panel.setCellWidth(editButton, "10%");
			panel.setSpacing(20);
			initWidget(panel);
		}

		private void setPluginConfigDTOForRowId(final String fieldType, BatchClassPluginDTO selectedPlugin) {
			BatchClassDynamicPluginConfigDTO batchClassDynamicConfigDTO = null;

			boolean isRowIdPresent = false;
			Collection<BatchClassDynamicPluginConfigDTO> configs = controller.getBatchClassDynamicPluginConfigDTO().getChildren();
			if (configs != null && !configs.isEmpty()) {
				for (BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO : configs) {
					if (batchClassDynamicPluginConfigDTO.getName().equals(AdminConstants.ROW_TYPE)) {
						batchClassDynamicPluginConfigDTO.setValue(fields.getItemText(fields.getSelectedIndex()));
						batchClassDynamicConfigDTO = batchClassDynamicPluginConfigDTO;
						isRowIdPresent = true;
					}
				}
			}
			if (!isRowIdPresent) {
				batchClassDynamicConfigDTO = new BatchClassDynamicPluginConfigDTO();
				batchClassDynamicConfigDTO.setBatchClassPlugin(selectedPlugin);
				batchClassDynamicConfigDTO.setChildren(null);
				if (controller.getBatchClassDynamicPluginConfigDTO().getChildren() == null) {
					List<BatchClassDynamicPluginConfigDTO> children = new ArrayList<BatchClassDynamicPluginConfigDTO>();
					controller.getBatchClassDynamicPluginConfigDTO().setChildren(children);
				}
				controller.getBatchClassDynamicPluginConfigDTO().getChildren().add(batchClassDynamicConfigDTO);
				batchClassDynamicConfigDTO.setDescription(fieldType);
				batchClassDynamicConfigDTO.setName(AdminConstants.ROW_TYPE);
				String itemText = fields.getItemText(fields.getSelectedIndex());
				batchClassDynamicConfigDTO.setValue(itemText);
				batchClassDynamicConfigDTO.setIdentifier(String.valueOf(Id.getIdentifier()));
			}
			selectedPlugin.addBatchClassDynamicPluginConfig(String.valueOf(Id.getIdentifier()), batchClassDynamicConfigDTO);
			editButton.setVisible(true);
			associateAndVerify.setEnabled(false);
			associateAndVerify.setStyleName(AdminConstants.DISABLED_BUTTON_STYLE);
			fields.setEnabled(false);
		}

		private BatchClassDynamicPluginConfigDTO getBatchClassDynamicPluginConfigDTO(BatchClassPluginDTO selectedPlugin,
				String fieldType) {
			Collection<BatchClassDynamicPluginConfigDTO> pluginConfigDTOs = controller.getBatchClassDynamicPluginConfigDTO()
					.getChildren();
			if (pluginConfigDTOs != null) {
				for (BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO : pluginConfigDTOs) {
					if (batchClassDynamicPluginConfigDTO.getDescription() != null
							&& batchClassDynamicPluginConfigDTO.getDescription().equals(fieldType)) {
						batchClassDynamicPluginConfigDTO.setValue(fields.getValue(fields.getSelectedIndex()));
						return batchClassDynamicPluginConfigDTO;
					}

				}
			}
			BatchClassDynamicPluginConfigDTO batchClassDynamicConfigDTO = new BatchClassDynamicPluginConfigDTO();
			batchClassDynamicConfigDTO.setBatchClassPlugin(selectedPlugin);
			batchClassDynamicConfigDTO.setChildren(null);
			batchClassDynamicConfigDTO.setParent(controller.getBatchClassDynamicPluginConfigDTO());
			if (controller.getBatchClassDynamicPluginConfigDTO().getChildren() == null) {
				List<BatchClassDynamicPluginConfigDTO> children = new ArrayList<BatchClassDynamicPluginConfigDTO>();
				controller.getBatchClassDynamicPluginConfigDTO().setChildren(children);
			}
			controller.getBatchClassDynamicPluginConfigDTO().getChildren().add(batchClassDynamicConfigDTO);
			controller.getBatchClass().setDirty(true);
			batchClassDynamicConfigDTO.setDescription(fieldType);
			batchClassDynamicConfigDTO.setName(AdminConstants.FIELD_TYPE);
			String itemText = fields.getItemText(fields.getSelectedIndex());
			batchClassDynamicConfigDTO.setValue(itemText);
			batchClassDynamicConfigDTO.setIdentifier(String.valueOf(Id.getIdentifier()));
			selectedPlugin.addBatchClassDynamicPluginConfig(String.valueOf(Id.getIdentifier()), batchClassDynamicConfigDTO);
			return batchClassDynamicConfigDTO;
		}

	}

	private String convertFieldType(String inputFieldType) {
		String fieldType = inputFieldType;
		if (fieldType.equalsIgnoreCase(AdminConstants.DATABASE_TYPE_DATE)) {
			fieldType = AdminConstants.JAVA_TYPE_DATE;
		} else if (fieldType.equalsIgnoreCase(AdminConstants.DATABASE_TYPE_STRING)) {
			fieldType = AdminConstants.JAVA_TYPE_STRING;
		} else if (fieldType.equalsIgnoreCase(AdminConstants.DATABASE_TYPE_LONG)) {
			fieldType = AdminConstants.JAVA_TYPE_LONG;
		} else if (fieldType.equalsIgnoreCase(AdminConstants.DATABASE_TYPE_DOUBLE)) {
			fieldType = AdminConstants.JAVA_TYPE_DOUBLE;
		} else if (fieldType.equalsIgnoreCase(AdminConstants.DATABASE_TYPE_INTEGER)) {
			fieldType = AdminConstants.JAVA_TYPE_INTEGER;
		} else if (fieldType.equalsIgnoreCase(AdminConstants.DATABASE_TYPE_FLOAT)) {
			fieldType = AdminConstants.JAVA_TYPE_FLOAT;
		} else if (fieldType.equalsIgnoreCase(AdminConstants.DATABASE_TYPE_BIGDECIMAL)) {
			fieldType = AdminConstants.JAVA_TYPE_BIGDECIMAL;
		}
		return fieldType;
	}

}
