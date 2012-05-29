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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.da.property.DependencyTypeProperty;
import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.MessageConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.batch.BatchClassViewPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.batch.ExportBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.batch.ImportBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.batch.SamplePatternPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.batchclassfield.BatchClassFieldViewPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.documenttype.DocumentTypeViewPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.email.EmailViewPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.fieldtype.FieldTypeViewPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.functionkey.FunctionKeyViewPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.kvextraction.KVExtractionPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.kvextraction.AdvancedKVExtraction.AdvancedKVExtractionPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.module.ConfigureModulePluginsPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.module.ConfigureModulePresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.module.ModuleViewPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin.DocTypeFieldsMappingPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin.DocTypeMappingPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin.FuzzyDBPluginPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin.KV_PP_AddEditListPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin.KV_PP_ConfigPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin.KV_PP_PropertiesPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin.PluginViewPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.regex.RegexPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.tablecolumninfo.TableColumnInfoPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.tableinfo.TableInfoViewPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.BatchClassManagementView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.batch.ExportBatchClassView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.batch.ImportBatchClassView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.batch.SamplePatternView;
import com.ephesoft.dcma.gwt.core.client.event.BindEvent;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.client.ui.table.Record;
import com.ephesoft.dcma.gwt.core.client.ui.table.Table;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.DoubleClickListner;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.PaginationListner;
import com.ephesoft.dcma.gwt.core.shared.BatchClassDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassDynamicPluginConfigDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassModuleDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginConfigDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchFolderListDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.DependencyDTO;
import com.ephesoft.dcma.gwt.core.shared.DocumentTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.EmailConfigurationDTO;
import com.ephesoft.dcma.gwt.core.shared.KVExtractionDTO;
import com.ephesoft.dcma.gwt.core.shared.PluginDetailsDTO;
import com.ephesoft.dcma.gwt.core.shared.RoleDTO;
import com.ephesoft.dcma.gwt.core.shared.SamplePatternDTO;
import com.ephesoft.dcma.gwt.core.shared.TableInfoDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;

public class BatchClassManagementPresenter extends AbstractBatchClassPresenter<BatchClassManagementView> implements PaginationListner,
		DoubleClickListner {

	private static final String PLUGIN_MUST_OCCUR_BEFORE = " plugin must occur before ";
	private static final String MUST_BE_UNIQUE_IN_THE_WORKFLOW = " must be unique in the workflow";
	private static final String DEPENDENCIES_VALIDATED_SUCCESSFULLY = "Dependencies Validated Successfully";
	private static final String WORKFLOW_DEPLOYED_SUCCESSFULLY = "Workflow deployed successfully";
	private static final String AN_ERROR_OCCURED_WHILE_DEPLOYING_THE_WORKFLOW = "An error occured while deploying the workflow";
	private static final String DEPLOYING_WORKFLOW = "Deploying Workflow";
	private static final String LOADING_MODULES = "Loading Modules";
	private static final String DEPENDENCY_VIOLATED = "Dependency violated!: ";
	private static final String OR = "/";
	public static final String AND = ",";

	private final BatchClassBreadCrumbPresenter batchClassBreadCrumbPresenter;
	private final BatchClassViewPresenter batchClassViewPresenter;
	private final PluginViewPresenter pluginViewPresenter;
	private final ModuleViewPresenter moduleViewPresenter;
	private final DocumentTypeViewPresenter documentTypeViewPresenter;
	private final EmailViewPresenter emailViewPresenter;
	private final BatchClassFieldViewPresenter batchClassFieldViewPresenter;
	private final FuzzyDBPluginPresenter fuzzyDBPluginPresenter;
	private final DocTypeFieldsMappingPresenter docTypeFieldsMappingPresenter;
	private final DocTypeMappingPresenter docTypeMappingPresenter;
	private final FieldTypeViewPresenter fieldTypeViewPresenter;
	private final FunctionKeyViewPresenter functionKeyViewPresenter;
	private final TableInfoViewPresenter tableInfoViewPresenter;
	private final TableColumnInfoPresenter tableColumnInfoPresenter;
	private final KVExtractionPresenter kvExtractionPresenter;
	private final RegexPresenter regexPresenter;
	private final KV_PP_PropertiesPresenter kvPPPropertiesPresenter;
	private final KV_PP_AddEditListPresenter kvPPAddEditListPresenter;
	private final KV_PP_ConfigPresenter kvPPConfigPresenter;
	private final AdvancedKVExtractionPresenter advancedKVExtractionPresenter;
	private final ConfigureModulePresenter addModulePresenter;
	private final ConfigureModulePluginsPresenter editModulePluginsPresenter;

	public BatchClassManagementPresenter(final BatchClassManagementController controller, final BatchClassManagementView view) {

		super(controller, view);
		this.batchClassBreadCrumbPresenter = new BatchClassBreadCrumbPresenter(controller, view.getBatchClassBreadCrumbView());
		this.batchClassViewPresenter = new BatchClassViewPresenter(controller, view.getBatchClassView());

		this.moduleViewPresenter = new ModuleViewPresenter(controller, view.getModuleView());
		this.pluginViewPresenter = new PluginViewPresenter(controller, view.getPluginView());
		this.fuzzyDBPluginPresenter = new FuzzyDBPluginPresenter(controller, view.getFuzzyDBPluginView());
		this.docTypeMappingPresenter = new DocTypeMappingPresenter(controller, view.getDocTypeMappingView());
		this.docTypeFieldsMappingPresenter = new DocTypeFieldsMappingPresenter(controller, view.getDocTypeFieldsMappingView());
		this.fieldTypeViewPresenter = new FieldTypeViewPresenter(controller, view.getFieldTypeView());
		this.functionKeyViewPresenter = new FunctionKeyViewPresenter(controller, view.getFunctionKeyView());
		this.kvExtractionPresenter = new KVExtractionPresenter(controller, view.getKvExtractionView());
		this.tableInfoViewPresenter = new TableInfoViewPresenter(controller, view.getTableInfoView());
		this.tableColumnInfoPresenter = new TableColumnInfoPresenter(controller, view.getTableColumnInfoView());
		this.documentTypeViewPresenter = new DocumentTypeViewPresenter(controller, view.getDocumentTypeView());
		this.emailViewPresenter = new EmailViewPresenter(controller, view.getEmailView());
		this.batchClassFieldViewPresenter = new BatchClassFieldViewPresenter(controller, view.getBatchClassFieldView());
		this.regexPresenter = new RegexPresenter(controller, view.getRegexView());
		this.kvPPPropertiesPresenter = new KV_PP_PropertiesPresenter(controller, view.getKvPPPropertiesView());
		this.kvPPAddEditListPresenter = new KV_PP_AddEditListPresenter(controller, view.getKvPPAddEditView());
		this.advancedKVExtractionPresenter = new AdvancedKVExtractionPresenter(controller, view.getAdvancedKVExtractionView());
		this.kvPPConfigPresenter = new KV_PP_ConfigPresenter(controller, view.getKVPPConfigView());
		this.addModulePresenter = new ConfigureModulePresenter(controller, view.getAddModuleView());
		this.editModulePluginsPresenter = new ConfigureModulePluginsPresenter(controller, view.getEditModulesPluginSelectView());
		init();
	}

	public final void init() {
		controller.getRpcService().getAllRolesOfUser(new AsyncCallback<HashSet<String>>() {

			@Override
			public void onFailure(Throwable arg0) {

			}

			@Override
			public void onSuccess(HashSet<String> arg0) {
				controller.setUserRoles(arg0);
			}
		}

		);
		controller.getRpcService().isUserSuperAdmin(new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable arg0) {

			}

			@Override
			public void onSuccess(Boolean arg0) {
				controller.setSuperAdmin(arg0);

			}
		});
		controller.getRpcService().getBatchClassRowCount(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable arg0) {

			}

			@Override
			public void onSuccess(String rowCount) {
				view.setTotalRowCount(rowCount);
			}
		});
		controller.getRpcService().getAllRoles(new AsyncCallback<List<RoleDTO>>() {

			@Override
			public void onFailure(Throwable arg0) {

			}

			@Override
			public void onSuccess(List<RoleDTO> roleList) {
				controller.setRoleDTOs(roleList);
				controller.getRpcService().countAllBatchClassesExcludeDeleted(new AsyncCallback<Integer>() {

					@Override
					public void onFailure(Throwable arg0) {
						// Do nothing
					}

					@Override
					public void onSuccess(final Integer count) {
						controller.getRpcService().getBatchClasses(0, Table.VISIBLE_RECORD_COUNT, null,
								new AsyncCallback<List<BatchClassDTO>>() {

									@Override
									public void onSuccess(List<BatchClassDTO> batches) {
										controller.setBatchClassList(batches);
										view.createBatchClassList(batches, count);
										view.showBatchClassListView();
									}

									@Override
									public void onFailure(Throwable arg0) {
										ConfirmationDialogUtil.showConfirmationDialogError(
												MessageConstants.BATCH_CLASS_LIST_ERROR_MSG, true);
									}
								});
					}
				});
			}
		});
	}

	public void bind() {
		// Processing to be done on load comes here.
	}

	public BatchClassViewPresenter getBatchClassViewPresenter() {
		return batchClassViewPresenter;
	}

	public PluginViewPresenter getPluginViewPresenter() {
		return pluginViewPresenter;
	}

	public ModuleViewPresenter getModuleViewPresenter() {
		return moduleViewPresenter;
	}

	public DocumentTypeViewPresenter getDocumentTypeViewPresenter() {
		return documentTypeViewPresenter;
	}

	public EmailViewPresenter getEmailViewPresenter() {
		return emailViewPresenter;
	}

	public BatchClassFieldViewPresenter getBatchClassFieldViewPresenter() {
		return batchClassFieldViewPresenter;
	}

	public BatchClassBreadCrumbPresenter getBatchClassBreadCrumbPresenter() {
		return batchClassBreadCrumbPresenter;
	}

	public FieldTypeViewPresenter getFieldTypeViewPresenter() {
		return fieldTypeViewPresenter;
	}

	public KVExtractionPresenter getKvExtractionPresenter() {
		return kvExtractionPresenter;
	}

	public TableColumnInfoPresenter getTableColumnInfoPresenter() {
		return tableColumnInfoPresenter;
	}

	public FunctionKeyViewPresenter getFunctionKeyViewPresenter() {
		return functionKeyViewPresenter;
	}

	public TableInfoViewPresenter getTableInfoViewPresenter() {
		return tableInfoViewPresenter;
	}

	public RegexPresenter getRegexPresenter() {
		return regexPresenter;
	}

	public KV_PP_PropertiesPresenter getKvPPPropertiesPresenter() {
		return kvPPPropertiesPresenter;
	}

	public KV_PP_AddEditListPresenter getKvPPAddEditListPresenter() {
		return kvPPAddEditListPresenter;
	}

	public AdvancedKVExtractionPresenter getAdvancedKVExtractionPresenter() {
		return advancedKVExtractionPresenter;
	}

	public void initiateBatchClassView(String batchClassIdentifier) {
		controller.getRpcService().getBatchClass(batchClassIdentifier, new AsyncCallback<BatchClassDTO>() {

			@Override
			public void onFailure(Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.BATCH_CLASS_ERROR_MSG);
			}

			@Override
			public void onSuccess(BatchClassDTO batchClassDTO) {
				controller.setBatchClass(batchClassDTO);
				batchClassBreadCrumbPresenter.createBreadCrumb(controller.getBatchClass());
				if (batchClassDTO.isDeployed()) {
					view.setDeployAndValidateButtonEnable(false);
					view.getBatchClassView().setAddModuleButtonVisibility(false);
					moduleViewPresenter.setAddButtonEnable(false);
				} else {
					view.toggleDeployButtonEnable(false);
					view.getBatchClassView().setAddModuleButtonVisibility(true);
					moduleViewPresenter.setAddButtonEnable(true);
				}
				view.showBatchClassView();
				controller.getEventBus().fireEvent(new BindEvent());
			}
		});
	}

	public void showBatchClassView(BatchClassDTO batchClassDTO) {
		controller.setBatchClass(batchClassDTO);
		showBatchClassView();
	}

	public void showBatchClassView() {
		if (!getController().getBatchClass().isDeployed()) {
			getController().getMainPresenter().getView().toggleDeployButtonEnable(false);
		} else {
			setValidateAndDeployButtonEnable(false);
		}
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getBatchClass());
		batchClassViewPresenter.bind();
		view.showBatchClassView();
	}

	public void showBatchClassListView() {
		view.showBatchClassListView();
	}

	public void showModuleView(BatchClassModuleDTO module) {
		controller.setSelectedModule(module);
		showModuleView();
	}

	public void showModuleView() {
		if (!getController().getBatchClass().isDeployed()) {
			getController().getMainPresenter().getView().toggleDeployButtonEnable(false);
		} else {
			setValidateAndDeployButtonEnable(false);
		}
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedModule());
		moduleViewPresenter.bind();
		view.showModuleView();
	}

	public void showAddModuleView(List<String> modulesList) {
		ScreenMaskUtility.maskScreen(LOADING_MODULES);
		controller.setModulesList(modulesList);
		getBatchClassModules();
		setValidateAndDeployButtonEnable(false);
	}

	public void showAddModuleView() {
		batchClassBreadCrumbPresenter.createBreadCrumbForModules();
		addModulePresenter.bind();
		view.showAddModuleView();
	}

	/**
	 * 
	 */
	private void getBatchClassModules() {

		controller.getRpcService().getAllBatchClassModulesWorkflowName(new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable arg0) {

			}

			@Override
			public void onSuccess(List<String> allModuleNames) {
				controller.setAllModules(allModuleNames);
				showAddModuleView();
			}
		});

	}

	public void setValidateAndDeployButtonEnable(boolean enable) {
		view.getDeploy().setEnabled(enable);
		view.getValidate().setEnabled(enable);
	}

	public void showAddPluginView() {
		String moduleIdentifier = controller.getSelectedModule().getIdentifier();
		showAddPluginView(moduleIdentifier);
		setValidateAndDeployButtonEnable(false);
	}

	public void showAddPluginView(String moduleIdentifier) {
		batchClassBreadCrumbPresenter.createBreadCrumbForPluginsSelect(moduleIdentifier);
		editModulePluginsPresenter.bind();
		view.showEditModuleView();
	}

	public void showDocumentTypeView(boolean isEditableDocumentType) {
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedDocument());
		documentTypeViewPresenter.bind();
		if (isEditableDocumentType) {
			controller.getBatchClass().setDirty(true);
			documentTypeViewPresenter.showEditDocumentTypeView();
		} else {
			documentTypeViewPresenter.showDocumentTypeView();
		}
		view.showDocumentView();
	}

	public void showDocumentTypeView(DocumentTypeDTO documentTypeDTO, boolean isEditableDocumentType) {
		controller.setSelectedDocument(documentTypeDTO);
		showDocumentTypeView(isEditableDocumentType);
	}

	public void showEmailView(boolean isEditableEmail) {
		if (null != controller.getSelectedEmailConfiguration()) {
			batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedEmailConfiguration());
		}
		emailViewPresenter.bind();
		if (isEditableEmail) {
			controller.getBatchClass().setDirty(true);
			emailViewPresenter.showEditEmailView();
		} else {
			emailViewPresenter.showEmailView();
		}
		view.showEmailView();
	}

	public void showEmailView(EmailConfigurationDTO emailConfigurationDTO, boolean isEditableDocumentType) {
		controller.setSelectedEmailConfiguration(emailConfigurationDTO);
		showEmailView(isEditableDocumentType);
	}

	public void showBatchClassFieldView(boolean isEditableBatchClassField) {
		if (null != controller.getSelectedBatchClassField()) {
			batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedBatchClassField());
		}
		batchClassFieldViewPresenter.bind();
		if (isEditableBatchClassField) {
			controller.getBatchClass().setDirty(true);
			batchClassFieldViewPresenter.showEditBatchClassFieldView();
		} else {
			batchClassFieldViewPresenter.showBatchClassFieldView();
		}
		view.showBatchClassFieldView();
	}

	public void showKVExtractionView(KVExtractionDTO kvExtractionDTO, boolean isEditable) {
		controller.setSelectedKVExtraction(kvExtractionDTO);
		showKVExtractionView(isEditable);
	}

	public void showFieldTypeView(DocumentTypeDTO documentTypeDTO, boolean isEditable) {
		controller.setSelectedDocument(documentTypeDTO);
		showFieldTypeView(isEditable);
	}

	public void showKVExtractionView(boolean isEditable) {
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedKVExtraction());
		kvExtractionPresenter.bind();
		if (isEditable) {
			controller.getBatchClass().setDirty(true);
			kvExtractionPresenter.showEditKVExtractionView();
		} else {
			kvExtractionPresenter.showKVExtractionView();
		}
		view.showKVExtractionView();
	}

	public void showRegexView(boolean isEditable) {
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedRegex());
		regexPresenter.bind();
		if (isEditable) {
			controller.getBatchClass().setDirty(true);
			regexPresenter.showEditRegexView();
		} else {
			regexPresenter.showRegexDetailView();
		}
		view.showRegexView();
	}

	public void showPluginView(BatchClassPluginDTO batchClassPluginDTO) {
		controller.setSelectedPlugin(batchClassPluginDTO);
		showPluginView();
	}

	public void showKVppPluginView(BatchClassPluginDTO batchClassPluginDTO) {
		controller.setSelectedPlugin(batchClassPluginDTO);
		showKVppPluginView();
	}

	public void showKVppPluginView() {
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedPlugin());
		kvPPPropertiesPresenter.bind();
		view.showKvPPPropertiesView();
	}

	public void showKVppPluginConfigView() {
		batchClassBreadCrumbPresenter.createBreadCrumbForKVPPPluginConfig(controller.getSelectedPlugin());
		kvPPPropertiesPresenter.bind();
		kvPPConfigPresenter.bind();
		view.showKvPPPropertiesView();
		view.showKVPPPluginConfigView();

	}

	public void showPluginView() {
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedPlugin());
		pluginViewPresenter.bind();
		view.showPluginView();
	}

	public void showKVPPAddEditPluginView(BatchClassPluginDTO batchClassPluginDTO) {
		batchClassBreadCrumbPresenter.createBreadCrumbForKVPPPlugin(controller.getPluginConfigDTO());
		controller.setSelectedPlugin(batchClassPluginDTO);
		showKVppPluginConfigAddEditView();
	}

	public void showKVppPluginConfigAddEditView() {
		batchClassBreadCrumbPresenter.createBreadCrumbForKVPPPlugin(controller.getPluginConfigDTO());
		kvPPAddEditListPresenter.bind();
		view.showKvPPAddEditListView();
	}

	public void showFieldTypeView(boolean isEditable) {
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedDocumentLevelField());
		fieldTypeViewPresenter.bind();
		if (isEditable) {
			controller.getBatchClass().setDirty(true);
			fieldTypeViewPresenter.showEditFieldTypeView();
		} else {
			fieldTypeViewPresenter.showFieldTypeView();
		}
		view.showFieldTypeView();
	}

	public void showFunctionKeyView(boolean isEditable) {
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedFunctionKeyDTO());
		functionKeyViewPresenter.bind();
		if (isEditable) {
			controller.getBatchClass().setDirty(true);
			functionKeyViewPresenter.showEditFunctionKeyView();
		} else {
			functionKeyViewPresenter.showFunctionKeyView();
		}
		view.showFunctionKeyView();
	}

	public void showTableInfoView(TableInfoDTO tableInfoDTO, boolean isEditableTableInfoType) {
		controller.setTableInfoSelectedField(tableInfoDTO);
		showTableInfoView(isEditableTableInfoType);
	}

	public void showTableInfoView(boolean isEditable) {
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedTableInfoField());
		tableInfoViewPresenter.bind();
		if (isEditable) {
			controller.getBatchClass().setDirty(true);
			tableInfoViewPresenter.showEditTableInfoView();
		} else {
			tableInfoViewPresenter.showTableInfoView();
		}
		view.showTableInfoView();
	}

	public void showTableColumnInfoView(boolean isEditable) {
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedTableColumnInfoField());
		tableColumnInfoPresenter.bind();
		if (isEditable) {
			controller.getBatchClass().setDirty(true);
			tableColumnInfoPresenter.showEditTableColumnInfoView();
		} else {
			tableColumnInfoPresenter.showTcInfoView();
		}
		view.showTcInfoView();
	}

	public void showFuzzyDBPluginView(BatchClassPluginDTO batchClassPluginDTO) {
		controller.setSelectedPlugin(batchClassPluginDTO);
		getDocTypeMappingPresenter().getView().clearDetailsTable();
		getDocTypeFieldsMappingPresenter().getView().clearDetailsTable();
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedPlugin());
		fuzzyDBPluginPresenter.bind();
		view.showFuzzyDBPluginView();
	}

	public void showDocTypeMappingView() {
		getDocTypeMappingPresenter().getView().clearDetailsTable();
		batchClassBreadCrumbPresenter.createBreadCrumbForDocumentType();
		docTypeMappingPresenter.bind();
		docTypeMappingPresenter.setTablesToFuzzyDBView();
		view.showDocTypeMappingView();
	}

	public void showFuzzyDBPluginView() {
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedPlugin());
		getDocTypeFieldsMappingPresenter().getView().clearDetailsTable();
		fuzzyDBPluginPresenter.bind();
		view.showFuzzyDBPluginView();
	}

	public void showDocTypeFieldMappingView(BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO) {
		batchClassBreadCrumbPresenter.createBreadCrumb(batchClassDynamicPluginConfigDTO);
		getDocTypeMappingPresenter().getView().clearDetailsTable();
		docTypeFieldsMappingPresenter.bind();
		docTypeFieldsMappingPresenter.setColumnsToFuzzyDBView();
		view.showDocTypeFieldMappingView();
	}

	public void showDocTypeFieldMappingView() {
		docTypeFieldsMappingPresenter.bind();
		docTypeFieldsMappingPresenter.setColumnsToFuzzyDBView();
		view.showDocTypeFieldMappingView();
	}

	public void showAdvancedKVExtractionView() {
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedKVExtraction());
		advancedKVExtractionPresenter.bind();
		if (advancedKVExtractionPresenter.isEditAdvancedKV()) {
			advancedKVExtractionPresenter.setImageUrlAndCoordinates();
		}
		view.showAdvancedKVExtractionView();
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		// Event handling is done here.
	}

	public void acquireLock(final BatchClassDTO batchClassDTO) {

		ScreenMaskUtility.maskScreen();

		controller.getRpcService().acquireLock(batchClassDTO.getIdentifier(), new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void arg0) {
				ScreenMaskUtility.unmaskScreen();
				view.setScreenVisibility(batchClassDTO);
			}

			@Override
			public void onFailure(Throwable arg0) {
				ScreenMaskUtility.unmaskScreen();
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.UNABLE_TO_ACQUIRE_LOCK
						+ batchClassDTO.getIdentifier());
				init();
			}
		});

	}

	public void onEditButtonClicked(String identifier) {
		BatchClassDTO batchClassDTO = controller.getBatchClassByIdentifier(identifier);
		acquireLock(batchClassDTO);
	}

	public void refreshDocumentView(String batchClassIdentifier) {
		ScreenMaskUtility.maskScreen();
		controller.getRpcService().getBatchClass(batchClassIdentifier, new AsyncCallback<BatchClassDTO>() {

			@Override
			public void onFailure(Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.UNABLE_TO_GET_BATCH_CLASS);
				ScreenMaskUtility.unmaskScreen();
			}

			@Override
			public void onSuccess(BatchClassDTO batchClassDTO) {
				ScreenMaskUtility.unmaskScreen();
				view.setScreenVisibility(batchClassDTO);
			}
		});
	}

	public FuzzyDBPluginPresenter getFuzzyDBPluginPresenter() {
		return fuzzyDBPluginPresenter;
	}

	public DocTypeFieldsMappingPresenter getDocTypeFieldsMappingPresenter() {
		return docTypeFieldsMappingPresenter;
	}

	public DocTypeMappingPresenter getDocTypeMappingPresenter() {
		return docTypeMappingPresenter;
	}

	@Override
	public void onPagination(final int startIndex, final int maxResult, final Order order) {
		controller.getRpcService().countAllBatchClassesExcludeDeleted(new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable arg0) {
				// Do nothing if call fails.
			}

			@Override
			public void onSuccess(final Integer count) {
				controller.getRpcService().getBatchClasses(startIndex, maxResult, order, new AsyncCallback<List<BatchClassDTO>>() {

					@Override
					public void onFailure(Throwable throwable) {
						ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.PROBLEM_FETCHING_BATCH_CLASSES);
					}

					@Override
					public void onSuccess(List<BatchClassDTO> batches) {
						controller.setBatchClassList(batches);
						List<Record> recordList = view.setBatchList(batches);
						view.getBatchClassListView().getModuleListView().updateRecords(recordList, startIndex, count);
						view.showBatchClassListView();
					}
				});
			}
		});

	}

	public void onSaveClicked() {

		ScreenMaskUtility.maskScreen();

		if (controller.getSelectedPlugin() != null) {
			Collection<BatchClassDynamicPluginConfigDTO> dynamicPluginConfigDTOs = controller.getSelectedPlugin()
					.getBatchClassDynamicPluginConfigs();
			if (dynamicPluginConfigDTOs != null && !dynamicPluginConfigDTOs.isEmpty()) {
				boolean isRowIdSet = false;
				for (BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO : dynamicPluginConfigDTOs) {
					isRowIdSet = false;
					if (batchClassDynamicPluginConfigDTO.getName().equals(AdminConstants.DOCUMENT_TYPE)) {
						Collection<BatchClassDynamicPluginConfigDTO> children = batchClassDynamicPluginConfigDTO.getChildren();
						if (children != null) {
							for (BatchClassDynamicPluginConfigDTO batchClassDynamicPluginDTO : children) {
								if (batchClassDynamicPluginDTO.getName().equalsIgnoreCase(AdminConstants.ROW_TYPE)) {
									isRowIdSet = true;
									break;
								}
							}
						}
						if (!isRowIdSet) {
							ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.NO_ROW_ID_SET
									+ batchClassDynamicPluginConfigDTO.getDescription());
							ScreenMaskUtility.unmaskScreen();
							return;
						} else if (children != null && children.size() < 2) {
							ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.ERROR_FIELD_TYPE_MAPPING);
							ScreenMaskUtility.unmaskScreen();
							return;
						}
					}
				}
			}
		}
		controller.getRpcService().updateBatchClass(controller.getBatchClass(), new AsyncCallback<BatchClassDTO>() {

			@Override
			public void onFailure(Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.PERSISTANCE_ERROR);
				ScreenMaskUtility.unmaskScreen();

			}

			@Override
			public void onSuccess(BatchClassDTO batchClass) {
				controller.getRpcService().cleanup(new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void arg0) {
						// Generate Confirmation to show changes successfully saved in database
						final ConfirmationDialog saveConfirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(
								LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.SAVE_SUCCESSFUL), LocaleDictionary
										.get().getMessageValue(BatchClassManagementMessages.SUCCESS), Boolean.TRUE);

						saveConfirmationDialog.addDialogListener(new DialogListener() {

							@Override
							public void onOkClick() {
								saveConfirmationDialog.hide();
								init();
							}

							@Override
							public void onCancelClick() {
								saveConfirmationDialog.hide();
							}
						});

					}

					@Override
					public void onFailure(Throwable arg0) {
						// Do nothing if call fails.
					}
				});
				ScreenMaskUtility.unmaskScreen();
			}
		});
	}

	public void onCancelClicked() {

		ScreenMaskUtility.maskScreen();
		controller.getRpcService().getBatchClass(controller.getBatchClass().getIdentifier(), new AsyncCallback<BatchClassDTO>() {

			@Override
			public void onFailure(Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.UNABLE_TO_GET_BATCH_CLASS);
				ScreenMaskUtility.unmaskScreen();
			}

			@Override
			public void onSuccess(BatchClassDTO batchClass) {
				controller.setBatchClass(batchClass);
				controller.getRpcService().cleanup(new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void arg0) {
						init();
					}

					@Override
					public void onFailure(Throwable arg0) {
						// Do nothing on failure.
					}
				});
				ScreenMaskUtility.unmaskScreen();
			}
		});

	}

	public void onSampleFoldersClicked() {

		ScreenMaskUtility.maskScreen();
		List<String> batchClassIdList = new ArrayList<String>();
		batchClassIdList.add(controller.getBatchClass().getIdentifier());
		controller.getRpcService().sampleGeneration(batchClassIdList, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.SAMPLE_FOLDER_GENERATION_ERROR);
				ScreenMaskUtility.unmaskScreen();

			}

			@Override
			public void onSuccess(Void arg0) {
				ConfirmationDialogUtil.showConfirmationDialogSuccess(MessageConstants.SAMPLE_FOLDER_GENERATION_SUCCESSFUL);
				ScreenMaskUtility.unmaskScreen();

			}
		});

	}

	public void onLearnFilesClicked() {

		ScreenMaskUtility.maskScreen();
		String batchClassID = controller.getBatchClass().getIdentifier();
		view.getLearnButton().setEnabled(false);
		controller.getRpcService().learnFileForBatchClass(batchClassID, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.PROBLEM_LEARNING_FILES);
				view.getLearnButton().setEnabled(true);
				ScreenMaskUtility.unmaskScreen();

			}

			@Override
			public void onSuccess(Void arg0) {
				ConfirmationDialogUtil.showConfirmationDialogSuccess(MessageConstants.FILES_LEARNED_SUCCESSFULLY);
				view.getLearnButton().setEnabled(true);
				ScreenMaskUtility.unmaskScreen();
			}
		});

	}

	public void onLearnDBClicked() {

		boolean isBatchClassDirty = false;
		Collection<BatchClassModuleDTO> batchClassModules = controller.getBatchClass().getModules();
		for (BatchClassModuleDTO batchClassModule : batchClassModules) {
			Collection<BatchClassPluginDTO> batchClassPluginDTOs = batchClassModule.getBatchClassPlugins();
			for (BatchClassPluginDTO batchClassPlugin : batchClassPluginDTOs) {
				if (batchClassPlugin.getPlugin().getPluginName().equalsIgnoreCase(AdminConstants.FUZZY_DB_PLUGIN)) {
					Collection<BatchClassPluginConfigDTO> batchClassPluginConfigDTOs = batchClassPlugin.getBatchClassPluginConfigs();
					for (BatchClassPluginConfigDTO batchClassPluginConfig : batchClassPluginConfigDTOs) {
						if (batchClassPluginConfig.getPluginConfig() != null && batchClassPluginConfig.getPluginConfig().isDirty()) {
							isBatchClassDirty = true;
						}
					}
				}
			}
		}
		if (!isBatchClassDirty) {
			ScreenMaskUtility.maskScreen();
			controller.getRpcService().learnDataBase(controller.getBatchClass().getIdentifier(), true, new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable arg0) {
					ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.PROBLEM_LEARNING_DB);
					ScreenMaskUtility.unmaskScreen();

				}

				@Override
				public void onSuccess(Void arg0) {
					ConfirmationDialogUtil.showConfirmationDialogSuccess(MessageConstants.DB_LEARNED_SUCCESSFULLY);
					ScreenMaskUtility.unmaskScreen();
				}
			});
		} else {
			ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.CONFIRMATION_MESSAGE);
		}
	}

	public void onApplyClicked() {

		ScreenMaskUtility.maskScreen();

		if (controller.getSelectedPlugin() != null) {
			Collection<BatchClassDynamicPluginConfigDTO> dynamicPluginConfigDTOs = controller.getSelectedPlugin()
					.getBatchClassDynamicPluginConfigs();
			if (dynamicPluginConfigDTOs != null && !dynamicPluginConfigDTOs.isEmpty()) {
				boolean isRowIdSet = false;
				for (BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO : dynamicPluginConfigDTOs) {
					isRowIdSet = false;
					if (batchClassDynamicPluginConfigDTO.getName().equals(AdminConstants.DOCUMENT_TYPE)) {
						Collection<BatchClassDynamicPluginConfigDTO> children = batchClassDynamicPluginConfigDTO.getChildren();
						if (children != null) {
							for (BatchClassDynamicPluginConfigDTO child : children) {
								if (child.getName().equalsIgnoreCase(AdminConstants.ROW_TYPE)) {
									isRowIdSet = true;
									break;
								}
							}
						}
						if (!isRowIdSet) {
							ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.NO_ROW_ID_SET
									+ batchClassDynamicPluginConfigDTO.getDescription());
							ScreenMaskUtility.unmaskScreen();
							return;
						} else if (children != null && children.size() < 2) {
							ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.ERROR_FIELD_TYPE_MAPPING);
							ScreenMaskUtility.unmaskScreen();
							return;
						}
					}
				}
			}
		}
		controller.getRpcService().updateBatchClass(controller.getBatchClass(), new AsyncCallback<BatchClassDTO>() {

			@Override
			public void onFailure(Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.PERSISTANCE_ERROR);
				ScreenMaskUtility.unmaskScreen();
			}

			@Override
			public void onSuccess(final BatchClassDTO batchClassDTO) {
				controller.setBatchClass(batchClassDTO);
				controller.refresh();

				// Generate Confirmation to show changes successfully applied in database
				ConfirmationDialogUtil.showConfirmationDialogSuccess(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.APPLY_SUCCESSFUL));
				ScreenMaskUtility.unmaskScreen();
			}
		});
	}

	public void onDeleteButtonClicked(String identifier) {
		BatchClassDTO batchClassDTO = controller.getBatchClassByIdentifier(identifier);
		batchClassDTO.setDeleted(true);
		controller.setBatchClass(batchClassDTO);
		// delete batch class API
		controller.getRpcService().deleteBatchClass(controller.getBatchClass(), new AsyncCallback<BatchClassDTO>() {

			@Override
			public void onFailure(Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.PERSISTANCE_ERROR, Boolean.TRUE);
				ScreenMaskUtility.unmaskScreen();

			}

			@Override
			public void onSuccess(BatchClassDTO batchClass) {
				controller.getRpcService().cleanup(new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void arg0) {
						// Generate Confirmation to show changes successfully saved in database
						final ConfirmationDialog saveConfirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(
								LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.DELETE_SUCCESS_MESSAGE),
								LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.DELETE_SUCCESS_TITLE),
								Boolean.TRUE, Boolean.TRUE);

						saveConfirmationDialog.addDialogListener(new DialogListener() {

							@Override
							public void onOkClick() {
								saveConfirmationDialog.hide();
								init();
							}

							@Override
							public void onCancelClick() {
								saveConfirmationDialog.hide();
							}
						});

					}

					@Override
					public void onFailure(Throwable arg0) {
						// Do nothing if call fails.
					}
				});
				ScreenMaskUtility.unmaskScreen();
			}
		});
	}

	public void onExportButtonClicked(final String batchClassIdentifier) {
		controller.getRpcService().getBatchFolderList(new AsyncCallback<BatchFolderListDTO>() {

			@Override
			public void onFailure(Throwable arg0) {

			}

			@Override
			public void onSuccess(BatchFolderListDTO batchFolderList) {

				final DialogBox dialogBox = new DialogBox();
				final ExportBatchClassView exportBatchClassView = new ExportBatchClassView();
				ExportBatchClassPresenter exportBatchClassPresenter = new ExportBatchClassPresenter(controller, exportBatchClassView);

				exportBatchClassPresenter.setBatchFolderListDTO(batchFolderList);
				exportBatchClassView.setDialogBox(dialogBox);
				// exportBatchClassView.getDialogBox().show();
				exportBatchClassView.setExportBatchClassIdentifier(batchClassIdentifier);
				exportBatchClassPresenter.bind();
				exportBatchClassPresenter.showBatchClassExportView();
				exportBatchClassView.getSaveButton().setFocus(true);
				exportBatchClassPresenter.setFolderList();

			}
		});
	}

	public void onImportButtonClicked() {
		controller.getRpcService().getBatchFolderList(new AsyncCallback<BatchFolderListDTO>() {

			@Override
			public void onFailure(Throwable arg0) {

			}

			@Override
			public void onSuccess(BatchFolderListDTO batchFolderList) {

				final DialogBox dialogBox = new DialogBox();
				final ImportBatchClassView importBatchClassView = new ImportBatchClassView();
				ImportBatchClassPresenter importBatchClassPresenter = new ImportBatchClassPresenter(getController(),
						importBatchClassView);

				importBatchClassPresenter.setBatchFolderListDTO(batchFolderList);
				importBatchClassView.setDialogBox(dialogBox);
				importBatchClassPresenter.bind();
				importBatchClassPresenter.showBatchClassImportView();
				importBatchClassView.getSaveButton().setFocus(true);
				// importBatchClassPresenter.setFolderList();
			}
		});
	}

	public void onDeployClicked() {
		ScreenMaskUtility.maskScreen(DEPLOYING_WORKFLOW);

		controller.getRpcService().createAndDeployWorkflowJPDL(controller.getBatchClass().getIdentifier(), controller.getBatchClass(),
				new AsyncCallback<BatchClassDTO>() {

					@Override
					public void onFailure(Throwable arg0) {
						ConfirmationDialogUtil.showConfirmationDialogError(AN_ERROR_OCCURED_WHILE_DEPLOYING_THE_WORKFLOW);
						ScreenMaskUtility.unmaskScreen();
					}

					@Override
					public void onSuccess(BatchClassDTO batchClassDTO) {
						batchClassDTO.setDeployed(true);
						controller.setBatchClass(batchClassDTO);
						view.setDeployAndValidateButtonEnable(false);
						view.getBatchClassView().setAddModuleButtonVisibility(false);
						moduleViewPresenter.setAddButtonEnable(false);
						ConfirmationDialogUtil.showConfirmationDialogSuccess(WORKFLOW_DEPLOYED_SUCCESSFULLY);
						ScreenMaskUtility.unmaskScreen();
					}
				});
	}

	@Override
	public void onDoubleClickTable() {
		onEditButtonClicked();
	}

	public void onEditButtonClicked() {
		String identifier = view.getBatchClassListView().listView.getSelectedRowIndex();
		if (identifier == null || identifier.isEmpty()) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.NO_RECORD_TO_EDIT));
		} else {
			onEditButtonClicked(identifier);
		}
	}

	public void getSamplePatterns() {
		ScreenMaskUtility.maskScreen();
		controller.getRpcService().getSamplePatterns(new AsyncCallback<SamplePatternDTO>() {

			@Override
			public void onFailure(Throwable arg0) {
				ScreenMaskUtility.unmaskScreen();
			}

			@Override
			public void onSuccess(SamplePatternDTO samplePatterns) {
				ScreenMaskUtility.unmaskScreen();
				final DialogBox dialogBox = new DialogBox();
				final SamplePatternView samplePatternView = new SamplePatternView();
				SamplePatternPresenter samplePatternPresenter = new SamplePatternPresenter(controller, samplePatternView);
				samplePatternView.createSamplePatternList(samplePatterns);
				samplePatternView.setDialogBox(dialogBox);
				samplePatternPresenter.bind();
				samplePatternPresenter.showSamplePatternView(samplePatterns);

			}
		});

	}

	public void onValidateClicked() {
		validateDependencies();
	}

	public void sortPluginList(List<BatchClassPluginDTO> pluginsList) {
		Collections.sort(pluginsList, new Comparator<BatchClassPluginDTO>() {

			@Override
			public int compare(BatchClassPluginDTO batchClassPluginDTO1, BatchClassPluginDTO batchClassPluginDTO2) {
				int result;
				Integer orderNumberOne = batchClassPluginDTO1.getOrderNumber();
				Integer orderNumberTwo = batchClassPluginDTO2.getOrderNumber();
				if (orderNumberOne != null && orderNumberTwo != null) {
					result = orderNumberOne.compareTo(orderNumberTwo);
				} else if (orderNumberOne == null && orderNumberTwo == null) {
					result = 0;
				} else if (orderNumberOne == null) {
					result = -1;
				} else {
					result = 1;
				}
				return result;
			}
		});
	}

	public void sortPluginDetailsDTOList(List<PluginDetailsDTO> pluginsList, final boolean ascending) {
		Collections.sort(pluginsList, new Comparator<PluginDetailsDTO>() {

			@Override
			public int compare(PluginDetailsDTO PluginDTO1, PluginDetailsDTO PluginDTO2) {
				int result;
				String orderNumberOne = PluginDTO1.getPluginName();
				String orderNumberTwo = PluginDTO2.getPluginName();
				if (orderNumberOne != null && orderNumberTwo != null) {
					result = orderNumberOne.compareTo(orderNumberTwo);
				} else if (orderNumberOne == null && orderNumberTwo == null) {
					result = 0;
				} else if (orderNumberOne == null) {
					result = -1;
				} else {
					result = 1;
				}
				return result;
			}
		});
	}

	/**
	 * @param modulesList
	 */
	public void sortModulesList(List<BatchClassModuleDTO> modulesList) {
		Collections.sort(modulesList, new Comparator<BatchClassModuleDTO>() {

			@Override
			public int compare(BatchClassModuleDTO batchClassModuleDTO1, BatchClassModuleDTO batchClassModuleDTO2) {
				int result;
				Integer orderNumberOne = batchClassModuleDTO1.getOrderNumber();
				Integer orderNumberTwo = batchClassModuleDTO2.getOrderNumber();
				if (orderNumberOne != null && orderNumberTwo != null) {
					result = orderNumberOne.compareTo(orderNumberTwo);
				} else if (orderNumberOne == null && orderNumberTwo == null) {
					result = 0;
				} else if (orderNumberOne == null) {
					result = -1;
				} else {
					result = 1;
				}
				return result;
			}
		});
	}

	public void validateDependencies() {
		boolean allDependenciesValidated = true;
		Set<PluginDetailsDTO> pluginDetailsDTOs = new HashSet<PluginDetailsDTO>();
		Map<Integer, String> orderedPluginNames = new LinkedHashMap<Integer, String>(0);
		int index = 0;

		// Get Plugin Names in order for above moduleWorkflowNames
		// and Distinct PDDTOs

		List<BatchClassModuleDTO> batchClassModuleDTOs = new ArrayList<BatchClassModuleDTO>(controller.getBatchClass().getModules());
		sortModulesList(batchClassModuleDTOs);

		for (BatchClassModuleDTO batchClassModuleDTO : batchClassModuleDTOs) {
			List<BatchClassPluginDTO> batchClassPluginDTOs = new ArrayList<BatchClassPluginDTO>(batchClassModuleDTO
					.getBatchClassPlugins());
			sortPluginList(batchClassPluginDTOs);

			for (BatchClassPluginDTO batchClassPluginDTO : batchClassPluginDTOs) {
				if (!batchClassPluginDTO.isDeleted()) {
					orderedPluginNames.put(index++, batchClassPluginDTO.getPlugin().getPluginName());
					pluginDetailsDTOs.add(batchClassPluginDTO.getPlugin());
				}
			}
		}
		// Validate Dependency for each PDDTO
		for (PluginDetailsDTO pluginDetailsDTO : pluginDetailsDTOs) {
			if (pluginDetailsDTO.getDependencies() != null && !pluginDetailsDTO.getDependencies().isEmpty()) {
				for (DependencyDTO dependencyDTO : pluginDetailsDTO.getDependencies()) {
					if (dependencyDTO.getDependencyType().equals(DependencyTypeProperty.ORDER_BEFORE.getProperty())) {
						allDependenciesValidated = checkPluginsDependencies(dependencyDTO.getPluginDTO().getPluginName(),
								dependencyDTO.getDependencies(), orderedPluginNames);

						if (!allDependenciesValidated) {
							break;
						}
					} else if (dependencyDTO.getDependencyType().equals(DependencyTypeProperty.UNIQUE.getProperty())) {
						allDependenciesValidated = checkPluginsIfUnique(dependencyDTO.getPluginDTO().getPluginName(),
								orderedPluginNames);

						if (!allDependenciesValidated) {
							break;
						}
					}

				}
				if (!allDependenciesValidated) {
					break;
				}
			}

		}

		if (allDependenciesValidated) {
			ConfirmationDialogUtil.showConfirmationDialogSuccess(DEPENDENCIES_VALIDATED_SUCCESSFULLY);
			view.toggleDeployButtonEnable(true);
		}
	}

	private boolean checkPluginsIfUnique(String pluginName, Map<Integer, String> orderedPluginNames) {
		boolean isUnique = true;
		int count = 0;
		Set<Entry<Integer, String>> entrySet = orderedPluginNames.entrySet();
		for (Map.Entry pluginEntry : entrySet) {
			if (pluginEntry.getValue().equals(pluginName)) {
				count++;
			}
			if (count > 1) {
				isUnique = false;
				ConfirmationDialogUtil.showConfirmationDialogError(DEPENDENCY_VIOLATED + pluginName + MUST_BE_UNIQUE_IN_THE_WORKFLOW);
				break;
			}
		}
		return isUnique;
	}

	private boolean checkPluginsDependencies(String pluginName, String dependencies, Map<Integer, String> orderedPluginNames) {
		boolean pluginDependencyValidated = true;
		int pluginNameIndex = 0;

		// Get index of the plugin under Consideration
		pluginNameIndex = getIndexForValueFromMap(pluginName, orderedPluginNames);

		// Get index of all the Dependencies and check if they are less than plugin's index
		String[] andDependencies = dependencies.split(AND);
		for (String andDependency : andDependencies) {

			String[] orDependencies = andDependency.split(OR);

			for (String dependencyName : orDependencies) {

				int dependencyIndex = getIndexForValueFromMap(dependencyName, orderedPluginNames);
				if (dependencyIndex == -1 || dependencyIndex >= pluginNameIndex) {
					// Dependency occurs after plugin or is not present
					ConfirmationDialogUtil.showConfirmationDialogError(DEPENDENCY_VIOLATED + dependencyName + PLUGIN_MUST_OCCUR_BEFORE
							+ pluginName);
					pluginDependencyValidated = false;
					break;
				}
			}
			if (!pluginDependencyValidated) {
				break;
			}
		}
		return pluginDependencyValidated;
	}

	/**
	 * @param pluginName
	 * @param orderedPluginNames
	 */
	private int getIndexForValueFromMap(String pluginName, Map<Integer, String> orderedPluginNames) {
		int pluginNameIndex = -1;
		for (Map.Entry pluginEntry : orderedPluginNames.entrySet()) {
			if (pluginEntry.getValue().equals(pluginName)) {
				pluginNameIndex = Integer.parseInt(pluginEntry.getKey().toString());
				break;
			}
		}
		return pluginNameIndex;
	}
}
