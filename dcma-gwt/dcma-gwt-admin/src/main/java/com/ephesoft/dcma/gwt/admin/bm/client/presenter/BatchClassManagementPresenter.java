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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.cmisimporter.CmisImporterViewPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.documenttype.DocumentTypeViewPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.email.EmailViewPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.fieldtype.FieldTypeViewPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.functionkey.FunctionKeyViewPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.kvextraction.KVExtractionPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.kvextraction.advancedkvextraction.AdvancedKVExtractionPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.module.AddNewModulePresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.module.ConfigureModulePluginsPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.module.ConfigureModulePresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.module.ModuleViewPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin.BoxExporterPluginPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin.DocTypeFieldsMappingPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin.DocTypeMappingPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin.FuzzyDBPluginPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin.KV_PP_AddEditListPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin.KV_PP_ConfigPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin.KV_PP_PropertiesPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin.PluginViewPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.regex.RegexPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.scanner.ScannerViewPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.tablecolumninfo.TableColumnInfoPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.tablecolumninfo.advancedtableextraction.AdvancedTableExtractionPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.tableinfo.TableInfoViewPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.BatchClassManagementView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.batch.ExportBatchClassView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.batch.ImportBatchClassView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.batch.SamplePatternView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.module.AddNewModuleView;
import com.ephesoft.dcma.gwt.core.client.EphesoftAsyncCallback;
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
import com.ephesoft.dcma.gwt.core.shared.FieldTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.KVExtractionDTO;
import com.ephesoft.dcma.gwt.core.shared.ModuleDTO;
import com.ephesoft.dcma.gwt.core.shared.PluginDetailsDTO;
import com.ephesoft.dcma.gwt.core.shared.RoleDTO;
import com.ephesoft.dcma.gwt.core.shared.SamplePatternDTO;
import com.ephesoft.dcma.gwt.core.shared.TableInfoDTO;
import com.ephesoft.dcma.gwt.core.shared.WebScannerConfigurationDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.DialogBox;

/**
 * This class provides functionality to handle events and show view for Batch Classes like save, apply, show.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter
 */
public class BatchClassManagementPresenter extends AbstractBatchClassPresenter<BatchClassManagementView> implements PaginationListner,
		DoubleClickListner {

	/**
	 * OR_TEXT String.
	 */
	private static final String OR_TEXT = " or ";

	/**
	 * PLUGIN_MUST_OCCUR_BEFORE String.
	 */
	private static final String PLUGIN_MUST_OCCUR_BEFORE = " plugin must occur before ";

	/**
	 * MUST_BE_UNIQUE_IN_THE_WORKFLOW String.
	 */
	private static final String MUST_BE_UNIQUE_IN_THE_WORKFLOW = " must be unique in the workflow";

	/**
	 * DEPENDENCIES_VALIDATED_SUCCESSFULLY String.
	 */
	private static final String DEPENDENCIES_VALIDATED_SUCCESSFULLY = "Dependencies Validated Successfully";

	/**
	 * WORKFLOW_DEPLOYED_SUCCESSFULLY String.
	 */
	private static final String WORKFLOW_DEPLOYED_SUCCESSFULLY = "Workflow deployed successfully";

	/**
	 * AN_ERROR_OCCURED_WHILE_DEPLOYING_THE_WORKFLOW String.
	 */
	private static final String AN_ERROR_OCCURED_WHILE_DEPLOYING_THE_WORKFLOW = "An error occured while deploying the workflow";

	/**
	 * DEPLOYING_WORKFLOW String.
	 */
	private static final String DEPLOYING_WORKFLOW = "Deploying Workflow";

	/**
	 * LOADING_MODULES String.
	 */
	private static final String LOADING_MODULES = "Loading Modules";

	/**
	 * DEPENDENCY_VIOLATED String.
	 */
	private static final String DEPENDENCY_VIOLATED = "Dependency violated!: ";

	/**
	 * SLASH String.
	 */
	private static final String SLASH = "/";

	/**
	 * SEPERATOR String.
	 */
	public static final String SEPERATOR = ",";

	/**
	 * batchClassBreadCrumbPresenter BatchClassBreadCrumbPresenter.
	 */
	private final BatchClassBreadCrumbPresenter batchClassBreadCrumbPresenter;

	/**
	 * batchClassViewPresenter BatchClassViewPresenter.
	 */
	private final BatchClassViewPresenter batchClassViewPresenter;

	/**
	 * pluginViewPresenter PluginViewPresenter.
	 */
	private final PluginViewPresenter pluginViewPresenter;

	/**
	 * moduleViewPresenter ModuleViewPresenter.
	 */
	private final ModuleViewPresenter moduleViewPresenter;

	/**
	 * documentTypeViewPresenter DocumentTypeViewPresenter.
	 */
	private final DocumentTypeViewPresenter documentTypeViewPresenter;

	/**
	 * emailViewPresenter EmailViewPresenter.
	 */
	private final EmailViewPresenter emailViewPresenter;

	/**
	 * scannerViewPresenter ScannerViewPresenter.
	 */
	private final ScannerViewPresenter scannerViewPresenter;

	/**
	 * batchClassFieldViewPresenter BatchClassFieldViewPresenter.
	 */
	private final BatchClassFieldViewPresenter batchClassFieldViewPresenter;

	/**
	 * fuzzyDBPluginPresenter FuzzyDBPluginPresenter.
	 */
	private final FuzzyDBPluginPresenter fuzzyDBPluginPresenter;

	/**
	 * docTypeFieldsMappingPresenter DocTypeFieldsMappingPresenter.
	 */
	private final DocTypeFieldsMappingPresenter docTypeFieldsMappingPresenter;

	/**
	 * docTypeMappingPresenter DocTypeMappingPresenter.
	 */
	private final DocTypeMappingPresenter docTypeMappingPresenter;

	/**
	 * fieldTypeViewPresenter FieldTypeViewPresenter.
	 */
	private final FieldTypeViewPresenter fieldTypeViewPresenter;

	/**
	 * functionKeyViewPresenter FunctionKeyViewPresenter.
	 */
	private final FunctionKeyViewPresenter functionKeyViewPresenter;

	/**
	 * tableInfoViewPresenter TableInfoViewPresenter.
	 */
	private final TableInfoViewPresenter tableInfoViewPresenter;

	/**
	 * tableColumnInfoPresenter TableColumnInfoPresenter.
	 */
	private final TableColumnInfoPresenter tableColumnInfoPresenter;

	/**
	 * kvExtractionPresenter KVExtractionPresenter.
	 */
	private final KVExtractionPresenter kvExtractionPresenter;

	/**
	 * regexPresenter RegexPresenter.
	 */
	private final RegexPresenter regexPresenter;

	/**
	 * kvPPPropertiesPresenter KV_PP_PropertiesPresenter.
	 */
	private final KV_PP_PropertiesPresenter kvPPPropertiesPresenter;

	/**
	 * kvPPAddEditListPresenter KV_PP_AddEditListPresenter.
	 */
	private final KV_PP_AddEditListPresenter kvPPAddEditListPresenter;

	/**
	 * kvPPConfigPresenter KV_PP_ConfigPresenter.
	 */
	private final KV_PP_ConfigPresenter kvPPConfigPresenter;

	/**
	 * advancedKVExtractionPresenter AdvancedKVExtractionPresenter.
	 */
	private final AdvancedKVExtractionPresenter advancedKVExtractionPresenter;

	/**
	 * configureModulesPresenter ConfigureModulePresenter.
	 */
	private final ConfigureModulePresenter configureModulesPresenter;

	/**
	 * configureModulePluginsPresenter ConfigureModulePluginsPresenter.
	 */
	private final ConfigureModulePluginsPresenter configureModulePluginsPresenter;

	/**
	 * advancedTableExtractionPresenter AdvancedTableExtractionPresenter.
	 */
	private final AdvancedTableExtractionPresenter advancedTableExtractionPresenter;

	/**
	 * cmisImporterViewPresenter CmisImporterViewPresenter.
	 */
	private final CmisImporterViewPresenter cmisImporterViewPresenter;

	/**
	 * boxExporterPluginPresenter BoxExporterPluginPresenter.
	 */
	private final BoxExporterPluginPresenter boxExporterPluginPresenter;

	/**
	 * Constructor.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view BatchClassManagementView
	 */
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
		this.scannerViewPresenter = new ScannerViewPresenter(controller, view.getScannerView());
		this.batchClassFieldViewPresenter = new BatchClassFieldViewPresenter(controller, view.getBatchClassFieldView());
		this.regexPresenter = new RegexPresenter(controller, view.getRegexView());
		this.kvPPPropertiesPresenter = new KV_PP_PropertiesPresenter(controller, view.getKvPPPropertiesView());
		this.kvPPAddEditListPresenter = new KV_PP_AddEditListPresenter(controller, view.getKvPPAddEditView());
		this.advancedKVExtractionPresenter = new AdvancedKVExtractionPresenter(controller, view.getAdvancedKVExtractionView());
		this.kvPPConfigPresenter = new KV_PP_ConfigPresenter(controller, view.getKVPPConfigView());
		this.configureModulesPresenter = new ConfigureModulePresenter(controller, view.getAddModuleView());
		this.configureModulePluginsPresenter = new ConfigureModulePluginsPresenter(controller, view.getEditModulesPluginSelectView());
		this.advancedTableExtractionPresenter = new AdvancedTableExtractionPresenter(controller, view.getAdvancedTableExtractionView());
		this.cmisImporterViewPresenter = new CmisImporterViewPresenter(controller, view.getCmisImporterView());
		this.boxExporterPluginPresenter = new BoxExporterPluginPresenter(controller, view.getBoxExporterPluginView());
		init();
	}

	/**
	 * Initial processing to take place.
	 */
	public final void init() {
		controller.getRpcService().getAllRolesOfUser(new EphesoftAsyncCallback<Set<String>>() {

			@Override
			public void customFailure(Throwable arg0) {
				/*
				 * On Failure
				 */
			}

			@Override
			public void onSuccess(Set<String> arg0) {
				controller.setUserRoles(arg0);
			}
		}

		);
		controller.getRpcService().isUserSuperAdmin(new EphesoftAsyncCallback<Boolean>() {

			@Override
			public void customFailure(Throwable arg0) {
				/*
				 * On Failure
				 */
			}

			@Override
			public void onSuccess(Boolean isSuperAdmin) {
				controller.setSuperAdmin(isSuperAdmin);

				// To enable/diable button according to user role
				view.setButtonsEnableAttributeForSuperAdmin(isSuperAdmin);
			}
		});
		controller.getRpcService().getBatchClassRowCount(new EphesoftAsyncCallback<String>() {

			@Override
			public void customFailure(Throwable arg0) {
				/*
				 * On Failure
				 */
			}

			@Override
			public void onSuccess(String rowCount) {
				view.setTotalRowCount(rowCount);
			}
		});
		controller.getRpcService().getAllRoles(new EphesoftAsyncCallback<List<RoleDTO>>() {

			@Override
			public void customFailure(Throwable arg0) {
				/*
				 * On Failure
				 */
			}

			@Override
			public void onSuccess(List<RoleDTO> roleList) {
				controller.setRoleDTOs(roleList);
				controller.getRpcService().countAllBatchClassesExcludeDeleted(new EphesoftAsyncCallback<Integer>() {

					@Override
					public void customFailure(Throwable arg0) {
						// Do nothing
					}

					@Override
					public void onSuccess(final Integer count) {
						controller.getRpcService().getBatchClasses(0, Table.visibleRecodrCount, null,
								new EphesoftAsyncCallback<List<BatchClassDTO>>() {

									@Override
									public void onSuccess(List<BatchClassDTO> batches) {
										controller.setBatchClassList(batches);
										view.createBatchClassList(batches, count);
										view.showBatchClassListView();
									}

									@Override
									public void customFailure(Throwable arg0) {
										ConfirmationDialogUtil.showConfirmationDialogError(
												MessageConstants.BATCH_CLASS_LIST_ERROR_MSG, true);
									}
								});
					}
				});
			}
		});
	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	public void bind() {
		// Processing to be done on load comes here.
	}

	/**
	 * To get Advanced Table Extraction Presenter.
	 * 
	 * @return AdvancedTableExtractionPresenter
	 */
	public AdvancedTableExtractionPresenter getAdvancedTableExtractionPresenter() {
		return advancedTableExtractionPresenter;
	}

	/**
	 * To get Batch Class View Presenter.
	 * 
	 * @return BatchClassViewPresenter
	 */
	public BatchClassViewPresenter getBatchClassViewPresenter() {
		return batchClassViewPresenter;
	}

	/**
	 * To get Plugin View Presenter.
	 * 
	 * @return PluginViewPresenter
	 */
	public PluginViewPresenter getPluginViewPresenter() {
		return pluginViewPresenter;
	}

	/**
	 * To get Module View Presenter.
	 * 
	 * @return ModuleViewPresenter
	 */
	public ModuleViewPresenter getModuleViewPresenter() {
		return moduleViewPresenter;
	}

	/**
	 * To get Document Type View Presenter.
	 * 
	 * @return DocumentTypeViewPresenter
	 */
	public DocumentTypeViewPresenter getDocumentTypeViewPresenter() {
		return documentTypeViewPresenter;
	}

	/**
	 * To get Email View Presenter.
	 * 
	 * @return EmailViewPresenter
	 */
	public EmailViewPresenter getEmailViewPresenter() {
		return emailViewPresenter;
	}

	/**
	 * To get Scanner View Presenter.
	 * 
	 * @return ScannerViewPresenter
	 */
	public ScannerViewPresenter getScannerViewPresenter() {
		return scannerViewPresenter;
	}

	/**
	 * To get Batch Class Field View Presenter.
	 * 
	 * @return BatchClassFieldViewPresenter
	 */
	public BatchClassFieldViewPresenter getBatchClassFieldViewPresenter() {
		return batchClassFieldViewPresenter;
	}

	/**
	 * To get Batch Class Bread Crumb Presenter.
	 * 
	 * @return BatchClassBreadCrumbPresenter
	 */
	public BatchClassBreadCrumbPresenter getBatchClassBreadCrumbPresenter() {
		return batchClassBreadCrumbPresenter;
	}

	/**
	 * To get Field Type View Presenter.
	 * 
	 * @return FieldTypeViewPresenter
	 */
	public FieldTypeViewPresenter getFieldTypeViewPresenter() {
		return fieldTypeViewPresenter;
	}

	/**
	 * To get KV Extraction Presenter.
	 * 
	 * @return KVExtractionPresenter
	 */
	public KVExtractionPresenter getKvExtractionPresenter() {
		return kvExtractionPresenter;
	}

	/**
	 * To get Table Column Info Presenter.
	 * 
	 * @return TableColumnInfoPresenter
	 */
	public TableColumnInfoPresenter getTableColumnInfoPresenter() {
		return tableColumnInfoPresenter;
	}

	/**
	 * To get Function Key View Presenter.
	 * 
	 * @return FunctionKeyViewPresenter
	 */
	public FunctionKeyViewPresenter getFunctionKeyViewPresenter() {
		return functionKeyViewPresenter;
	}

	/**
	 * To get Table Info View Presenter.
	 * 
	 * @return TableInfoViewPresenter
	 */
	public TableInfoViewPresenter getTableInfoViewPresenter() {
		return tableInfoViewPresenter;
	}

	/**
	 * To get Regex Presenter.
	 * 
	 * @return RegexPresenter
	 */
	public RegexPresenter getRegexPresenter() {
		return regexPresenter;
	}

	/**
	 * To get KV PP Properties Presenter.
	 * 
	 * @return KV_PP_PropertiesPresenter
	 */
	public KV_PP_PropertiesPresenter getKvPPPropertiesPresenter() {
		return kvPPPropertiesPresenter;
	}

	/**
	 * To get KV PP add edit List Presenter.
	 * 
	 * @return KV_PP_AddEditListPresenter
	 */
	public KV_PP_AddEditListPresenter getKvPPAddEditListPresenter() {
		return kvPPAddEditListPresenter;
	}

	/**
	 * To get Advanced KV Extraction Presenter.
	 * 
	 * @return AdvancedKVExtractionPresenter
	 */
	public AdvancedKVExtractionPresenter getAdvancedKVExtractionPresenter() {
		return advancedKVExtractionPresenter;
	}

	/**
	 * To initiate Batch Class View.
	 * 
	 * @param batchClassIdentifier String
	 */
	public void initiateBatchClassView(String batchClassIdentifier) {
		controller.getRpcService().getBatchClass(batchClassIdentifier, new EphesoftAsyncCallback<BatchClassDTO>() {

			@Override
			public void customFailure(Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.BATCH_CLASS_ERROR_MSG);
			}

			@Override
			public void onSuccess(final BatchClassDTO batchClassDTO) {
				// For setting maximum 'row count'.
				controller.getRpcService().getBatchClassRowCount(new EphesoftAsyncCallback<String>() {

					@Override
					public void customFailure(Throwable arg0) {
						/*
						 * On Failure
						 */
					}

					@Override
					public void onSuccess(String rowCount) {
						view.setTotalRowCount(rowCount);
					}
				});
				controller.setBatchClass(batchClassDTO);
				batchClassBreadCrumbPresenter.createBreadCrumb(controller.getBatchClass());
				view.toggleDeployButtonEnable(false);
				view.showBatchClassView();
				controller.getEventBus().fireEvent(new BindEvent());
			}
		});
	}

	/**
	 * To show Batch Class View.
	 * 
	 * @param batchClassDTO BatchClassDTO
	 */
	public void showBatchClassView(BatchClassDTO batchClassDTO) {
		controller.setBatchClass(batchClassDTO);
		showBatchClassView();
	}

	/**
	 * To show Batch Class View.
	 */
	public void showBatchClassView() {
		view.toggleDeployButtonEnable(false);
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getBatchClass());
		batchClassViewPresenter.bind();
		view.showBatchClassView();
	}

	/**
	 * To show Batch Class List View.
	 */
	public void showBatchClassListView() {
		view.showBatchClassListView();
	}

	/**
	 * To show Module View.
	 * 
	 * @param module BatchClassModuleDTO
	 */
	public void showModuleView(BatchClassModuleDTO module) {
		controller.setSelectedModule(module);
		showModuleView();
	}

	/**
	 * To show Module View.
	 */
	public void showModuleView() {
		view.toggleDeployButtonEnable(false);
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedModule());
		view.showModuleView();
		moduleViewPresenter.bind();
	}

	/**
	 * To show Add Module View.
	 * 
	 * @param modulesList List<String>
	 */
	public void showAddModuleView(List<String> modulesList) {
		ScreenMaskUtility.maskScreen(LOADING_MODULES);
		controller.setModulesList(modulesList);
		getModules();
		view.setDeployAndValidateButtonEnable(false);
	}

	/**
	 * To show Add Module View.
	 */
	public void showAddModuleView() {
		batchClassBreadCrumbPresenter.createBreadCrumbForModules();
		configureModulesPresenter.bind();
		view.showAddModuleView();
	}

	/**
	 * To show Add New Module View.
	 */
	public void showAddNewModuleView() {
		final DialogBox dialogBox = new DialogBox();
		AddNewModuleView addNewModuleView = new AddNewModuleView();
		AddNewModulePresenter addNewModulePresenter = new AddNewModulePresenter(controller, addNewModuleView);
		addNewModuleView.setDialogBox(dialogBox);
		addNewModulePresenter.bind();
		addNewModulePresenter.showAddNewModuleView();
	}

	private void getModules() {
		controller.getRpcService().getAllModules(new EphesoftAsyncCallback<List<ModuleDTO>>() {

			@Override
			public void customFailure(Throwable arg0) {
				/*
				 * On Failure
				 */
			}

			@Override
			public void onSuccess(List<ModuleDTO> moduleDTOs) {
				List<String> moduleNames = new ArrayList<String>();
				Map<String, ModuleDTO> moduleNameToDtoMap = new HashMap<String, ModuleDTO>();
				for (ModuleDTO moduleDTO : moduleDTOs) {
					String moduleName = moduleDTO.getName();
					moduleNames.add(moduleName);
					moduleNameToDtoMap.put(moduleName, moduleDTO);
				}
				Collections.sort(moduleNames);
				controller.setAllModules(moduleNames);
				controller.setModuleNameToDtoMap(moduleNameToDtoMap);
				showAddModuleView();
			}
		});

	}

	/**
	 * To show Add Plugin View.
	 */
	public void showAddPluginView() {
		String moduleIdentifier = controller.getSelectedModule().getIdentifier();
		showAddPluginView(moduleIdentifier);
		view.setDeployAndValidateButtonEnable(false);
	}

	/**
	 * To show Add Plugin View.
	 * 
	 * @param moduleIdentifier String
	 */
	public void showAddPluginView(String moduleIdentifier) {
		batchClassBreadCrumbPresenter.createBreadCrumbForPluginsSelect(moduleIdentifier);
		configureModulePluginsPresenter.bind();
		view.showEditModuleView();
	}

	/**
	 * To show Document Type View.
	 * 
	 * @param isEditableDocumentType boolean
	 */
	public void showDocumentTypeView(boolean isEditableDocumentType) {
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedDocument());
		if (isEditableDocumentType) {
			controller.getBatchClass().setDirty(true);
			documentTypeViewPresenter.showEditDocumentTypeView();
		} else {
			documentTypeViewPresenter.showDocumentTypeView();
		}
		view.showDocumentView();
		documentTypeViewPresenter.bind();
	}

	/**
	 * To show Document Type View.
	 * 
	 * @param documentTypeDTO DocumentTypeDTO
	 * @param isEditableDocumentType boolean
	 */
	public void showDocumentTypeView(DocumentTypeDTO documentTypeDTO, boolean isEditableDocumentType) {
		controller.setSelectedDocument(documentTypeDTO);
		showDocumentTypeView(isEditableDocumentType);
	}

	/**
	 * To show Email View.
	 * 
	 * @param isEditableEmail boolean
	 */
	public void showEmailView(boolean isEditableEmail) {
		if (null != controller.getSelectedEmailConfiguration()) {
			batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedEmailConfiguration());
		}
		if (isEditableEmail) {
			controller.getBatchClass().setDirty(true);
			emailViewPresenter.showEditEmailView();
		} else {
			emailViewPresenter.showEmailView();
		}
		view.showEmailView();
		emailViewPresenter.bind();
	}

	/**
	 * To show Scanner View.
	 * 
	 * @param isEditable boolean
	 */
	public void showScannerView(boolean isEditable) {
		if (null != controller.getSelectedWebScannerConfiguration()) {
			batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedWebScannerConfiguration());
		}
		if (isEditable) {
			controller.getBatchClass().setDirty(true);
			scannerViewPresenter.showEditScannerView();
		} else {
			scannerViewPresenter.showScannerView();
		}
		view.showScannerView();
		scannerViewPresenter.bind();
	}

	/**
	 * To show Scanner View.
	 * 
	 * @param configurationDTO WebScannerConfigurationDTO
	 * @param isEditable boolean
	 */
	public void showScannerView(WebScannerConfigurationDTO configurationDTO, boolean isEditable) {
		controller.setSelectedWebScannerConfiguration(configurationDTO);
		showScannerView(isEditable);
	}

	/**
	 * To show Email View.
	 * 
	 * @param emailConfigurationDTO EmailConfigurationDTO
	 * @param isEditableDocumentType boolean
	 */
	public void showEmailView(EmailConfigurationDTO emailConfigurationDTO, boolean isEditableDocumentType) {
		controller.setSelectedEmailConfiguration(emailConfigurationDTO);
		showEmailView(isEditableDocumentType);
	}

	/**
	 * To show Batch Class Field View.
	 * 
	 * @param isEditableBatchClassField boolean
	 */
	public void showBatchClassFieldView(boolean isEditableBatchClassField) {
		if (null != controller.getSelectedBatchClassField()) {
			batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedBatchClassField());
		}
		if (isEditableBatchClassField) {
			controller.getBatchClass().setDirty(true);
			batchClassFieldViewPresenter.showEditBatchClassFieldView();
		} else {
			batchClassFieldViewPresenter.showBatchClassFieldView();
		}
		view.showBatchClassFieldView();
		batchClassFieldViewPresenter.bind();
	}

	/**
	 * To show KV Extraction View.
	 * 
	 * @param kvExtractionDTO KVExtractionDTO
	 * @param isEditable boolean
	 */
	public void showKVExtractionView(KVExtractionDTO kvExtractionDTO, boolean isEditable) {
		controller.setSelectedKVExtraction(kvExtractionDTO);
		showKVExtractionView(isEditable);
	}

	/**
	 * To show Field Type View.
	 * 
	 * @param documentTypeDTO DocumentTypeDTO
	 * @param isEditable boolean
	 */
	public void showFieldTypeView(DocumentTypeDTO documentTypeDTO, boolean isEditable) {
		controller.setSelectedDocument(documentTypeDTO);
		showFieldTypeView(isEditable);
	}

	/**
	 * To show KV Extraction View.
	 * 
	 * @param isEditable boolean
	 */
	public void showKVExtractionView(boolean isEditable) {
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedKVExtraction());
		if (isEditable) {
			controller.getBatchClass().setDirty(true);
			kvExtractionPresenter.showEditKVExtractionView();
		} else {
			kvExtractionPresenter.showKVExtractionView();
		}
		view.showKVExtractionView();
		kvExtractionPresenter.bind();
	}

	/**
	 * To show Regex View.
	 * 
	 * @param isEditable boolean
	 */
	public void showRegexView(boolean isEditable) {
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedRegex());
		if (isEditable) {
			controller.getBatchClass().setDirty(true);
			regexPresenter.showEditRegexView();
		} else {
			regexPresenter.showRegexDetailView();
		}
		view.showRegexView();
		regexPresenter.bind();
	}

	/**
	 * To show Plugin View.
	 * 
	 * @param batchClassPluginDTO BatchClassPluginDTO
	 */
	public void showPluginView(BatchClassPluginDTO batchClassPluginDTO) {
		controller.setSelectedPlugin(batchClassPluginDTO);
		showPluginView();
	}

	/**
	 * To show KV PP Plugin View.
	 * 
	 * @param batchClassPluginDTO BatchClassPluginDTO
	 */
	public void showKVppPluginView(BatchClassPluginDTO batchClassPluginDTO) {
		controller.setSelectedPlugin(batchClassPluginDTO);
		showKVppPluginView();
	}

	/**
	 * To show KV PP Plugin View.
	 */
	public void showKVppPluginView() {
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedPlugin());
		kvPPPropertiesPresenter.bind();
		view.showKvPPPropertiesView();
	}

	/**
	 * To show KV PP Plugin Config View.
	 */
	public void showKVppPluginConfigView() {
		batchClassBreadCrumbPresenter.createBreadCrumbForKVPPPluginConfig(controller.getSelectedPlugin());
		kvPPPropertiesPresenter.bind();
		kvPPConfigPresenter.bind();
		view.showKvPPPropertiesView();
		view.showKVPPPluginConfigView();

	}

	/**
	 * To show Plugin View.
	 */
	public void showPluginView() {
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedPlugin());
		pluginViewPresenter.bind();
		view.showPluginView();
	}

	/**
	 * To show KV PP add edit Plugin View.
	 * 
	 * @param batchClassPluginDTO BatchClassPluginDTO
	 */
	public void showKVPPAddEditPluginView(BatchClassPluginDTO batchClassPluginDTO) {
		batchClassBreadCrumbPresenter.createBreadCrumbForKVPPPlugin(controller.getPluginConfigDTO());
		controller.setSelectedPlugin(batchClassPluginDTO);
		showKVppPluginConfigAddEditView();
	}

	/**
	 * To show KV PP Plugin Config add Edit View.
	 */
	public void showKVppPluginConfigAddEditView() {
		batchClassBreadCrumbPresenter.createBreadCrumbForKVPPPlugin(controller.getPluginConfigDTO());
		view.showKvPPAddEditListView();
		kvPPAddEditListPresenter.bind();

	}

	/**
	 * To show Field Type View.
	 * 
	 * @param isEditable boolean
	 */
	public void showFieldTypeView(boolean isEditable) {
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedDocumentLevelField());
		if (isEditable) {
			controller.getBatchClass().setDirty(true);
			fieldTypeViewPresenter.showEditFieldTypeView();
		} else {
			fieldTypeViewPresenter.showFieldTypeView();
		}
		view.showFieldTypeView();
		fieldTypeViewPresenter.bind();
	}

	/**
	 * To show Function Key View.
	 * 
	 * @param isEditable boolean
	 */
	public void showFunctionKeyView(boolean isEditable) {
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedFunctionKeyDTO());
		if (isEditable) {
			controller.getBatchClass().setDirty(true);
			functionKeyViewPresenter.showEditFunctionKeyView();
		} else {
			functionKeyViewPresenter.showFunctionKeyView();
		}
		view.showFunctionKeyView();
		functionKeyViewPresenter.bind();
	}

	/**
	 * To show Table Info View.
	 * 
	 * @param tableInfoDTO TableInfoDTO
	 * @param isEditableTableInfoType boolean
	 */
	public void showTableInfoView(TableInfoDTO tableInfoDTO, boolean isEditableTableInfoType) {
		controller.setTableInfoSelectedField(tableInfoDTO);
		showTableInfoView(isEditableTableInfoType);
	}

	/**
	 * To show Table Info View.
	 * 
	 * @param isEditable boolean
	 */
	public void showTableInfoView(boolean isEditable) {
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedTableInfoField());
		if (isEditable) {
			controller.getBatchClass().setDirty(true);
			tableInfoViewPresenter.showEditTableInfoView();
		} else {
			tableInfoViewPresenter.showTableInfoView();
		}
		view.showTableInfoView();
		tableInfoViewPresenter.bind();
	}

	/**
	 * To show Table Column Info View.
	 * 
	 * @param isEditable boolean
	 */
	public void showTableColumnInfoView(boolean isEditable) {
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedTableColumnInfoField());
		if (isEditable) {
			controller.getBatchClass().setDirty(true);
			tableColumnInfoPresenter.showEditTableColumnInfoView();
		} else {
			tableColumnInfoPresenter.showTcInfoView();
		}
		view.showTcInfoView();
		tableColumnInfoPresenter.bind();
	}

	/**
	 * To show Table Column Coordinate Info View.
	 * 
	 * @param isEditable boolean
	 */
	public void showTableColumnCoordInfoView(boolean isEditable) {
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedTableInfoField());
		advancedTableExtractionPresenter.bind();
		if (isEditable) {
			controller.getBatchClass().setDirty(true);
			tableColumnInfoPresenter.showEditTableColumnInfoView();
		} else {
			tableColumnInfoPresenter.showTcInfoView();
		}
		view.showTcInfoView();
	}

	/**
	 * To show Fuzzy DB Plugin View.
	 * 
	 * @param batchClassPluginDTO BatchClassPluginDTO
	 */
	public void showFuzzyDBPluginView(BatchClassPluginDTO batchClassPluginDTO) {
		controller.setSelectedPlugin(batchClassPluginDTO);
		getDocTypeMappingPresenter().getView().clearDetailsTable();
		getDocTypeFieldsMappingPresenter().getView().clearDetailsTable();
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedPlugin());
		fuzzyDBPluginPresenter.bind();
		view.showFuzzyDBPluginView();
	}

	/**
	 * To show Doc Type Mapping View.
	 */
	public void showDocTypeMappingView() {
		getDocTypeMappingPresenter().getView().clearDetailsTable();
		batchClassBreadCrumbPresenter.createBreadCrumbForDocumentType();
		docTypeMappingPresenter.bind();
		docTypeMappingPresenter.setTablesToFuzzyDBView();
		view.showDocTypeMappingView();
	}

	/**
	 * To show Fuzzy DB Plugin View.
	 */
	public void showFuzzyDBPluginView() {
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedPlugin());
		getDocTypeFieldsMappingPresenter().getView().clearDetailsTable();
		fuzzyDBPluginPresenter.bind();
		view.showFuzzyDBPluginView();
	}

	/**
	 * To show Box Exporter Plugin View.
	 */
	public void showBoxExporterPluginView() {
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedPlugin());
		getDocTypeFieldsMappingPresenter().getView().clearDetailsTable();
		boxExporterPluginPresenter.bind();
		view.showBoxPluginView();
	}

	/**
	 * To show Doc Type Field Mapping View.
	 * 
	 * @param batchClassDynamicPluginConfigDTO BatchClassDynamicPluginConfigDTO
	 */
	public void showDocTypeFieldMappingView(BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO) {
		batchClassBreadCrumbPresenter.createBreadCrumb(batchClassDynamicPluginConfigDTO);
		getDocTypeMappingPresenter().getView().clearDetailsTable();
		docTypeFieldsMappingPresenter.bind();
		docTypeFieldsMappingPresenter.setColumnsToFuzzyDBView();
		view.showDocTypeFieldMappingView();
	}

	/**
	 * To show Doc Type Field Mapping View.
	 */
	public void showDocTypeFieldMappingView() {
		docTypeFieldsMappingPresenter.bind();
		docTypeFieldsMappingPresenter.setColumnsToFuzzyDBView();
		view.showDocTypeFieldMappingView();
	}

	/**
	 * To show Advanced KV Extraction View.
	 */
	public void showAdvancedKVExtractionView() {
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedKVExtraction());
		advancedKVExtractionPresenter.bind();
		controller.getBatchClass().setDirty(true);
		if (advancedKVExtractionPresenter.isEditAdvancedKV()) {
			advancedKVExtractionPresenter.setImageUrlAndCoordinates();
		}
		view.showAdvancedKVExtractionView();

	}

	/**
	 * To show Advanced Table Extraction View.
	 */
	public void showAdvancedTableExtractionView() {
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedTableColumnInfoField());
		advancedTableExtractionPresenter.bind();
		controller.getBatchClass().setDirty(true);
		advancedTableExtractionPresenter.getImageURL();
		view.showAdvancedTableExtractionView();
	}

	/**
	 * To handle events.
	 * 
	 * @param eventBus HandlerManager
	 */
	@Override
	public void injectEvents(HandlerManager eventBus) {
		// Event handling is done here.
	}

	/**
	 * To acquire lock.
	 * 
	 * @param batchClassDTO BatchClassDTO
	 */
	public void acquireLock(final BatchClassDTO batchClassDTO) {

		ScreenMaskUtility.maskScreen();

		controller.getRpcService().acquireLock(batchClassDTO.getIdentifier(), new EphesoftAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void arg0) {
				ScreenMaskUtility.unmaskScreen();
				view.setScreenVisibility(batchClassDTO);
			}

			@Override
			public void customFailure(Throwable arg0) {
				ScreenMaskUtility.unmaskScreen();
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.UNABLE_TO_ACQUIRE_LOCK
						+ batchClassDTO.getIdentifier());
				init();
			}
		});

	}

	/**
	 * To perform operations on edit button clicked.
	 * 
	 * @param identifier String
	 */
	public void onEditButtonClicked(String identifier) {
		BatchClassDTO batchClassDTO = controller.getBatchClassByIdentifier(identifier);
		acquireLock(batchClassDTO);
	}

	/**
	 * To refresh Document View.
	 * 
	 * @param batchClassIdentifier String
	 */
	public void refreshDocumentView(String batchClassIdentifier) {
		ScreenMaskUtility.maskScreen();
		controller.getRpcService().getBatchClass(batchClassIdentifier, new EphesoftAsyncCallback<BatchClassDTO>() {

			@Override
			public void customFailure(Throwable arg0) {
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

	/**
	 * To get Fuzzy DB Plugin Presenter.
	 * 
	 * @return FuzzyDBPluginPresenter
	 */
	public FuzzyDBPluginPresenter getFuzzyDBPluginPresenter() {
		return fuzzyDBPluginPresenter;
	}

	/**
	 * To get Doc Type Fields Mapping Presenter.
	 * 
	 * @return DocTypeFieldsMappingPresenter
	 */
	public DocTypeFieldsMappingPresenter getDocTypeFieldsMappingPresenter() {
		return docTypeFieldsMappingPresenter;
	}

	/**
	 * To get Doc Type Mapping Presenter.
	 * 
	 * @return DocTypeMappingPresenter
	 */
	public DocTypeMappingPresenter getDocTypeMappingPresenter() {
		return docTypeMappingPresenter;
	}

	/**
	 * To get Configure Modules Presenter.
	 * 
	 * @return the configureModulesPresenter
	 */
	public ConfigureModulePresenter getConfigureModulesPresenter() {
		return configureModulesPresenter;
	}

	/**
	 * To get Configure Module Plugins Presenter.
	 * 
	 * @return the configureModulePluginsPresenter
	 */
	public ConfigureModulePluginsPresenter getConfigureModulePluginsPresenter() {
		return configureModulePluginsPresenter;
	}

	/**
	 * To do processing on pagination.
	 * 
	 * @param startIndex int
	 * @param maxResult int
	 * @param order Order
	 */
	@Override
	public void onPagination(final int startIndex, final int maxResult, final Order order) {
		controller.getRpcService().countAllBatchClassesExcludeDeleted(new EphesoftAsyncCallback<Integer>() {

			@Override
			public void customFailure(Throwable arg0) {
				// Do nothing if call fails.
			}

			@Override
			public void onSuccess(final Integer count) {
				controller.getRpcService().getBatchClasses(startIndex, maxResult, order, new EphesoftAsyncCallback<List<BatchClassDTO>>() {

					@Override
					public void customFailure(Throwable throwable) {
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

	/**
	 * To do processing on save click.
	 */
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
		controller.getRpcService().updateBatchClass(controller.getBatchClass(), new EphesoftAsyncCallback<BatchClassDTO>() {

			@Override
			public void customFailure(Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.PERSISTANCE_ERROR);
				ScreenMaskUtility.unmaskScreen();

			}

			@Override
			public void onSuccess(BatchClassDTO batchClass) {
				controller.setBatchClass(batchClass);
				controller.getRpcService().cleanup(new EphesoftAsyncCallback<Void>() {

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
					public void customFailure(Throwable arg0) {
						// Do nothing if call fails.
					}
				});
				ScreenMaskUtility.unmaskScreen();
			}
		});
	}

	/**
	 * To do processing on cancel click.
	 */
	public void onCancelClicked() {

		ScreenMaskUtility.maskScreen();
		controller.getRpcService().getBatchClass(controller.getBatchClass().getIdentifier(), new EphesoftAsyncCallback<BatchClassDTO>() {

			@Override
			public void customFailure(Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.UNABLE_TO_GET_BATCH_CLASS);
				ScreenMaskUtility.unmaskScreen();
			}

			@Override
			public void onSuccess(BatchClassDTO batchClass) {
				controller.setBatchClass(batchClass);
				controller.getRpcService().cleanup(new EphesoftAsyncCallback<Void>() {

					@Override
					public void onSuccess(Void arg0) {
						init();
					}

					@Override
					public void customFailure(Throwable arg0) {
						// Do nothing on failure.
					}
				});
				ScreenMaskUtility.unmaskScreen();
			}
		});

	}

	/**
	 * To do processing on sample folders click.
	 */
	public void onSampleFoldersClicked() {

		ScreenMaskUtility.maskScreen();
		List<String> batchClassIdList = new ArrayList<String>();
		batchClassIdList.add(controller.getBatchClass().getIdentifier());
		controller.getRpcService().sampleGeneration(batchClassIdList, new EphesoftAsyncCallback<Void>() {

			@Override
			public void customFailure(Throwable arg0) {
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

	/**
	 * To do processing on learn files click.
	 */
	public void onLearnFilesClicked() {

		ScreenMaskUtility.maskScreen();
		String batchClassID = controller.getBatchClass().getIdentifier();
		view.getLearnButton().setEnabled(false);
		controller.getRpcService().learnFileForBatchClass(batchClassID, new EphesoftAsyncCallback<Void>() {

			@Override
			public void customFailure(Throwable arg0) {
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

	/**
	 * To do processing on learn DB click.
	 */
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
			controller.getRpcService().learnDataBase(controller.getBatchClass().getIdentifier(), true, new EphesoftAsyncCallback<Void>() {

				@Override
				public void customFailure(Throwable arg0) {
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

	/**
	 * To do processing on apply click.
	 */
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
		controller.getRpcService().updateBatchClass(controller.getBatchClass(), new EphesoftAsyncCallback<BatchClassDTO>() {

			@Override
			public void customFailure(Throwable arg0) {
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

	/**
	 * To do processing on delete button click.
	 * 
	 * @param identifier String
	 */
	public void onDeleteButtonClicked(String identifier) {
		BatchClassDTO batchClassDTO = controller.getBatchClassByIdentifier(identifier);
		batchClassDTO.setDeleted(true);
		controller.setBatchClass(batchClassDTO);
		// delete batch class API
		controller.getRpcService().deleteBatchClass(controller.getBatchClass(), new EphesoftAsyncCallback<BatchClassDTO>() {

			@Override
			public void customFailure(Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.PERSISTANCE_ERROR, Boolean.TRUE);
				ScreenMaskUtility.unmaskScreen();

			}

			@Override
			public void onSuccess(BatchClassDTO batchClass) {
				controller.getRpcService().cleanup(new EphesoftAsyncCallback<Void>() {

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
					public void customFailure(Throwable arg0) {
						// Do nothing if call fails.
					}
				});
				ScreenMaskUtility.unmaskScreen();
			}
		});
	}

	/**
	 * To do processing on export button click.
	 * 
	 * @param batchClassIdentifier String
	 */
	public void onExportButtonClicked(final String batchClassIdentifier) {
		controller.getRpcService().getBatchFolderList(new EphesoftAsyncCallback<BatchFolderListDTO>() {

			@Override
			public void customFailure(Throwable arg0) {
				/*
				 * On Failure
				 */
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

	/**
	 * To do processing on import button click.
	 */
	public void onImportButtonClicked() {
		controller.getRpcService().getBatchFolderList(new EphesoftAsyncCallback<BatchFolderListDTO>() {

			@Override
			public void customFailure(Throwable arg0) {
				/*
				 * On Failure
				 */
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
			}
		});
	}

	/**
	 * To do processing on deploy click.
	 */
	public void onDeployClicked() {
		ScreenMaskUtility.maskScreen(DEPLOYING_WORKFLOW);

		controller.getRpcService().createAndDeployWorkflowJPDL(controller.getBatchClass().getIdentifier(), controller.getBatchClass(),
				new EphesoftAsyncCallback<BatchClassDTO>() {

					@Override
					public void customFailure(Throwable arg0) {
						ConfirmationDialogUtil.showConfirmationDialogError(AN_ERROR_OCCURED_WHILE_DEPLOYING_THE_WORKFLOW);
						ScreenMaskUtility.unmaskScreen();
					}

					@Override
					public void onSuccess(BatchClassDTO batchClassDTO) {
						batchClassDTO.setDeployed(true);
						controller.setBatchClass(batchClassDTO);
						view.toggleDeployButtonEnable(false);
						ConfirmationDialogUtil.showConfirmationDialogSuccess(WORKFLOW_DEPLOYED_SUCCESSFULLY);
						ScreenMaskUtility.unmaskScreen();
						controller.refresh();
					}
				});
	}

	/**
	 * To open edit view in case of double click.
	 */
	@Override
	public void onDoubleClickTable() {
		onEditButtonClicked();
	}

	/**
	 * To get the view on edit Button Clicked.
	 */
	public void onEditButtonClicked() {
		String identifier = view.getBatchClassListView().listView.getSelectedRowIndex();
		if (identifier == null || identifier.isEmpty()) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.NO_RECORD_TO_EDIT));
		} else {
			onEditButtonClicked(identifier);
		}
	}

	/**
	 * To get Sample Patterns.
	 */
	public void getSamplePatterns() {
		ScreenMaskUtility.maskScreen();
		controller.getRpcService().getSamplePatterns(new EphesoftAsyncCallback<SamplePatternDTO>() {

			@Override
			public void customFailure(Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.UNABLE_TO_READ_SAMPLE_PATTERN_FILE));
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

	/**
	 * To get RPC service on validate click.
	 */
	public void onValidateClicked() {
		controller.getRpcService().getAllPluginsNames(new EphesoftAsyncCallback<List<String>>() {

			@Override
			public void customFailure(Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialogError("Could Not validate Workflow.");
			}

			@Override
			public void onSuccess(List<String> pluginNames) {
				controller.setAllPluginNames(pluginNames);
				validateDependencies();
			}
		});

	}

	/**
	 * To sort Document Level Fields List.
	 * 
	 * @param fieldTypes List<FieldTypeDTO>
	 */
	public void sortDocumentLevelFieldsList(List<FieldTypeDTO> fieldTypes) {
		Collections.sort(fieldTypes, new Comparator<FieldTypeDTO>() {

			@Override
			public int compare(FieldTypeDTO fieldTypeDTO1, FieldTypeDTO fieldTypeDTO2) {
				int result;
				int orderNumberOne;
				int orderNumberTwo;
				try {
					orderNumberOne = Integer.parseInt(fieldTypeDTO1.getFieldOrderNumber());
					orderNumberTwo = Integer.parseInt(fieldTypeDTO2.getFieldOrderNumber());
				} catch (NumberFormatException numberFormatException) {
					orderNumberOne = 0;
					orderNumberTwo = 0;
				}
				result = orderNumberOne < orderNumberTwo ? -1 : (orderNumberOne == orderNumberTwo ? 0 : 1);
				return result;
			}
		});
	}

	/**
	 * To sort Plugin List.
	 * 
	 * @param pluginsList List<BatchClassPluginDTO>
	 */
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

	/**
	 * To sort Plugin Details DTO List.
	 * 
	 * @param pluginsList List<PluginDetailsDTO>
	 * @param ascending boolean
	 */
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
	 * To sort the module list.
	 * 
	 * @param modulesList List<BatchClassModuleDTO>
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

	/**
	 * To validate Dependencies.
	 */
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
		Set<Entry<Integer, String>> pluginEntrySet = orderedPluginNames.entrySet();
		for (Entry<Integer, String> pluginEntry : pluginEntrySet) {
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
		String[] andDependencies = dependencies.split(SEPERATOR);
		for (String andDependency : andDependencies) {
			boolean validDependency = false;
			String[] orDependencies = andDependency.split(SLASH);
			boolean orDependenciesValidated = true;
			for (String dependencyName : orDependencies) {

				if (controller.getAllPluginNames().contains(dependencyName)) {
					validDependency = true;
					int dependencyIndex = getIndexForValueFromMap(dependencyName, orderedPluginNames);
					if (dependencyIndex == -1 || dependencyIndex >= pluginNameIndex) {
						// Dependency occurs after plugin or is not present
						orDependenciesValidated = false;
					} else {
						orDependenciesValidated = true;
						break;
					}
				}
			}
			if (validDependency) {
				pluginDependencyValidated = pluginDependencyValidated && orDependenciesValidated;
			}
			if (!pluginDependencyValidated) {
				ConfirmationDialogUtil.showConfirmationDialogError(DEPENDENCY_VIOLATED + andDependency.replace(SLASH, OR_TEXT)
						+ PLUGIN_MUST_OCCUR_BEFORE + pluginName);
				pluginDependencyValidated = false;
				break;
			}
		}
		return pluginDependencyValidated;
	}

	private int getIndexForValueFromMap(String pluginName, Map<Integer, String> orderedPluginNames) {
		int pluginNameIndex = -1;
		Set<Entry<Integer, String>> pluginEntrySet = orderedPluginNames.entrySet();
		for (Entry<Integer, String> pluginEntry : pluginEntrySet) {
			if (pluginEntry.getValue().equals(pluginName)) {
				pluginNameIndex = Integer.parseInt(pluginEntry.getKey().toString());
				break;
			}
		}
		return pluginNameIndex;
	}

	/**
	 * To get Cmis Importer View Presenter.
	 * 
	 * @return CmisImporterViewPresenter
	 */
	public CmisImporterViewPresenter getCmisImporterViewPresenter() {
		return cmisImporterViewPresenter;
	}
	/**
	 * To show Cmis Importer View.
	 * 
	 * @param isEditableCmis boolean
	 */
	public void showCmisImporterView(boolean isEditableCmis) {
		if (null != controller.getSelectedCmisConfiguration()) {
			batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedCmisConfiguration());
		}
		if (isEditableCmis) {
			controller.getBatchClass().setDirty(true);
			cmisImporterViewPresenter.showEditCmisImporterView();
		} else {
			cmisImporterViewPresenter.showCmisImporterView();
		}
		view.showCmisImporterView();
		cmisImporterViewPresenter.bind();
	}

	/**
	 * To get Box Exporter Presenter.
	 * 
	 * @return BoxExporterPluginPresenter
	 */
	public BoxExporterPluginPresenter getBoxExporterPresenter() {
		return boxExporterPluginPresenter;
	}

	/**
	 * To show Box Plugin View.
	 * 
	 * @param batchClassPluginDTO BatchClassPluginDTO
	 */
	public void showBoxPluginView(BatchClassPluginDTO batchClassPluginDTO) {
		controller.setSelectedPlugin(batchClassPluginDTO);
		getDocTypeMappingPresenter().getView().clearDetailsTable();
		getDocTypeFieldsMappingPresenter().getView().clearDetailsTable();
		batchClassBreadCrumbPresenter.createBreadCrumb(controller.getSelectedPlugin());
		boxExporterPluginPresenter.bind();
		view.showBoxPluginView();
	}

}
