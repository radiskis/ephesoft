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

package com.ephesoft.dcma.gwt.admin.bm.client.view;

import java.util.LinkedList;
import java.util.List;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.BatchClassManagementPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.batch.CopyBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.batch.BatchClassListView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.batch.BatchClassView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.batch.CopyBatchClassView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.batchclassfield.BatchClassFieldListView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.batchclassfield.BatchClassFieldView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.cmisimporter.CmisImporterView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.documenttype.DocumentTypeView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.email.EmailView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.fieldtype.FieldTypeView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.functionkey.FunctionKeyView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.kvextraction.KVExtractionView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.kvextraction.advancedkvextraction.AdvancedKVExtractionView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.module.ConfigureModuleView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.module.ConfigureModulesPluginSelectView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.module.ModuleView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.plugin.BoxExporterPluginView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.plugin.DocTypeFieldsMappingView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.plugin.DocTypeMappingView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.plugin.FuzzyDBPluginView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.plugin.KV_PP_AddEditListView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.plugin.KV_PP_ConfigView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.plugin.KV_PP_PropertiesView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.plugin.PluginView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.regex.RegexView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.scanner.ScannerView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.tablecolumninfo.TableColumnInfoView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.tablecolumninfo.advancedtableextraction.AdvancedTableExtractionView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.tableinfo.TableInfoView;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.table.Record;
import com.ephesoft.dcma.gwt.core.shared.BatchClassDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class provides functionality to show batch class and add event listeners.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.view
 */
public class BatchClassManagementView extends View<BatchClassManagementPresenter> {

	/**
	 * VALIDATE String.
	 */
	private static final String VALIDATE = "Validate";

	/**
	 * BUTTON_STYLE String.
	 */
	private static final String BUTTON_STYLE = "button-style";

	/**
	 * Instantiates a class via deferred binding.
	 */
	interface Binder extends UiBinder<DockLayoutPanel, BatchClassManagementView> {
	}

	/**
	 * tabLayoutPanel LayoutPanel.
	 */
	@UiField
	protected LayoutPanel tabLayoutPanel;

	/**
	 * batchClassBreadCrumbView BatchClassBreadCrumbView.
	 */
	@UiField
	protected BatchClassBreadCrumbView batchClassBreadCrumbView;

	/**
	 * batchClassView BatchClassView.
	 */
	@UiField
	protected BatchClassView batchClassView;

	/**
	 * moduleView ModuleView.
	 */
	@UiField
	protected ModuleView moduleView;

	/**
	 * pluginView PluginView.
	 */
	@UiField
	protected PluginView pluginView;

	/**
	 * documentTypeView DocumentTypeView.
	 */
	@UiField
	protected DocumentTypeView documentTypeView;

	/**
	 * emailView EmailView.
	 */
	@UiField
	protected EmailView emailView;

	/**
	 * cmisImporterView CmisImporterView.
	 */
	@UiField
	protected CmisImporterView cmisImporterView;

	/**
	 * boxExporterPluginView BoxExporterPluginView.
	 */
	@UiField
	protected BoxExporterPluginView boxExporterPluginView;

	/**
	 * scannerView ScannerView.
	 */
	@UiField
	protected ScannerView scannerView;

	/**
	 * batchClassFieldView BatchClassFieldView.
	 */
	@UiField
	protected BatchClassFieldView batchClassFieldView;

	/**
	 * mainPanel DockLayoutPanel.
	 */
	@UiField
	protected DockLayoutPanel mainPanel;

	/**
	 * fuzzyDBPluginView FuzzyDBPluginView.
	 */
	@UiField
	protected FuzzyDBPluginView fuzzyDBPluginView;

	/**
	 * docTypeFieldsMappingView DocTypeFieldsMappingView.
	 */
	@UiField
	protected DocTypeFieldsMappingView docTypeFieldsMappingView;

	/**
	 * fieldTypeView FieldTypeView.
	 */
	@UiField
	protected FieldTypeView fieldTypeView;

	/**
	 * functionKeyView FunctionKeyView.
	 */
	@UiField
	protected FunctionKeyView functionKeyView;

	/**
	 * tableInfoView TableInfoView.
	 */
	@UiField
	protected TableInfoView tableInfoView;

	/**
	 * docTypeMappingView DocTypeMappingView.
	 */
	@UiField
	protected DocTypeMappingView docTypeMappingView;

	/**
	 * kvExtractionView KVExtractionView.
	 */
	@UiField
	protected KVExtractionView kvExtractionView;

	/**
	 * tableColumnInfoView TableColumnInfoView.
	 */
	@UiField
	protected TableColumnInfoView tableColumnInfoView;

	/**
	 * regexView RegexView.
	 */
	@UiField
	protected RegexView regexView;

	/**
	 * kvPPPropertiesView KV_PP_PropertiesView.
	 */
	@UiField
	protected KV_PP_PropertiesView kvPPPropertiesView;

	/**
	 * kvPPConfigView KV_PP_ConfigView.
	 */
	@UiField
	protected KV_PP_ConfigView kvPPConfigView;

	/**
	 * kvPPAddEditListView KV_PP_AddEditListView.
	 */
	@UiField
	protected KV_PP_AddEditListView kvPPAddEditListView;

	/**
	 * advancedKVExtractionView AdvancedKVExtractionView.
	 */
	@UiField
	protected AdvancedKVExtractionView advancedKVExtractionView;

	/**
	 * advancedTableExtractionView AdvancedTableExtractionView.
	 */
	@UiField
	protected AdvancedTableExtractionView advancedTableExtractionView;

	/**
	 * addModuleView ConfigureModuleView.
	 */
	protected ConfigureModuleView addModuleView;

	/**
	 * editModulesPluginSelectViewLayoutPanel LayoutPanel.
	 */
	@UiField
	protected LayoutPanel editModulesPluginSelectViewLayoutPanel;

	/**
	 * addModulesViewLayoutPanel LayoutPanel.
	 */
	@UiField
	protected LayoutPanel addModulesViewLayoutPanel;

	/**
	 * editModulesPluginSelectView ConfigureModulesPluginSelectView.
	 */
	protected ConfigureModulesPluginSelectView editModulesPluginSelectView;

	/**
	 * batchListPanel LayoutPanel.
	 */
	protected final LayoutPanel batchListPanel;

	/**
	 * buttonPanel HorizontalPanel.
	 */
	private final HorizontalPanel buttonPanel;

	/**
	 * batchClassListView BatchClassListView.
	 */
	private BatchClassListView batchClassListView;

	/**
	 * batchClassFieldListView BatchClassFieldListView.
	 */
	private BatchClassFieldListView batchClassFieldListView;

	/**
	 * bottomButtons HorizontalPanel.
	 */
	@UiField
	protected HorizontalPanel bottomButtons;

	/**
	 * submit Button.
	 */
	@UiField
	protected Button submit;

	/**
	 * cancel Button.
	 */
	@UiField
	protected Button cancel;

	/**
	 * deploy Button.
	 */
	@UiField
	protected Button deploy;

	/**
	 * validate Button.
	 */
	@UiField
	protected Button validate;

	/**
	 * sample Button.
	 */
	@UiField
	protected Button sample;

	/**
	 * learn Button.
	 */
	@UiField
	protected Button learn;

	/**
	 * learnDB Button.
	 */
	@UiField
	protected Button learnDB;

	/**
	 * mainDockPanel DockLayoutPanel.
	 */
	@UiField
	protected DockLayoutPanel mainDockPanel;

	/**
	 * apply Button.
	 */
	@UiField
	protected Button apply;

	/**
	 * bottomPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel bottomPanel;

	/**
	 * rightButtons HorizontalPanel.
	 */
	@UiField
	protected HorizontalPanel rightButtons;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * eventBus HandlerManager.
	 */
	private HandlerManager eventBus;

	/**
	 * copy Button.
	 */
	private Button copy;

	/**
	 * importButton Button.
	 */
	private Button importButton;

	/**
	 * Constructor.
	 */
	public BatchClassManagementView() {
		this(null);
	}

	/**
	 * Constructor.
	 * 
	 * @param eventBus HandlerManager
	 */
	public BatchClassManagementView(HandlerManager eventBus) {
		super();
		initWidget(BINDER.createAndBindUi(this));

		editModulesPluginSelectView = new ConfigureModulesPluginSelectView(eventBus);
		addModuleView = new ConfigureModuleView(eventBus);

		editModulesPluginSelectViewLayoutPanel.add(editModulesPluginSelectView);
		addModulesViewLayoutPanel.add(addModuleView);
		Button edit = new Button();
		copy = new Button();
		Button delete = new Button();
		Button export = new Button();
		importButton = new Button();
		bottomPanel.addStyleName("leftPadding");
		bottomPanel.addStyleName("fullWidth");
		edit.setText(AdminConstants.EDIT_BUTTON);
		copy.setText(AdminConstants.COPY_BUTTON);
		delete.setText(AdminConstants.DELETE_BUTTON);
		export.setText(AdminConstants.EXPORT_BUTTON);
		importButton.setText(AdminConstants.IMPORT_BUTTON);
		apply.setText(AdminConstants.APPLY_BUTTON);

		validate.setText(VALIDATE);
		validate.addStyleName(BUTTON_STYLE);

		buttonPanel = new HorizontalPanel();
		buttonPanel.setWidth("100%");
		buttonPanel.setSpacing(BatchClassManagementConstants.FIVE);
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		buttonPanel.setVerticalAlignment(HasAlignment.ALIGN_TOP);
		buttonPanel.add(export);
		buttonPanel.add(importButton);
		buttonPanel.add(edit);
		buttonPanel.setCellWidth(edit, "95%");
		buttonPanel.add(new Label(BatchClassManagementConstants.EMPTY_STRING));
		buttonPanel.add(copy);
		buttonPanel.add(new Label(BatchClassManagementConstants.EMPTY_STRING));
		buttonPanel.add(delete);

		batchListPanel = new LayoutPanel();
		batchClassListView = new BatchClassListView();
		batchClassFieldListView = new BatchClassFieldListView();
		batchListPanel.add(batchClassListView.listView);
		submit.setText(AdminConstants.SAVE_BUTTON);
		cancel.setText(AdminConstants.CANCEL_BUTTON);
		deploy.setText(AdminConstants.DEPLOY_BUTTON);
		learn.setText(AdminConstants.LEARN_FILES_BUTTON);
		sample.setText(AdminConstants.GENERATE_FOLDERS_BUTTON);
		learnDB.setText(AdminConstants.LEARN_DB_BUTTON);
		bottomButtons.setVisible(false);
		batchClassBreadCrumbView.setVisible(false);
		toggleDeployButtonEnable(false);

		edit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				presenter.onEditButtonClicked();
			}
		});

		copy.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				String identifier = batchClassListView.listView.getSelectedRowIndex();
				if (identifier == null || identifier.isEmpty()) {
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							BatchClassManagementMessages.NO_RECORD_TO_COPY));
					return;
				}
				final DialogBox dialogBox = new DialogBox();
				final CopyBatchClassView copyBatchClassView = new CopyBatchClassView();
				CopyBatchClassPresenter copyBatchClassPresenter = new CopyBatchClassPresenter(presenter.getController(),
						copyBatchClassView);
				copyBatchClassPresenter.setBatchClassDTO(presenter.getController().getBatchClassByIdentifier(identifier));
				copyBatchClassView.setDialogBox(dialogBox);
				copyBatchClassPresenter.bind();
				copyBatchClassPresenter.showBatchClassCopyView();
				copyBatchClassView.getSaveButton().setFocus(true);
			}
		});

		importButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				presenter.onImportButtonClicked();

			}
		});

		export.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				String identifier = batchClassListView.listView.getSelectedRowIndex();
				if (identifier == null || identifier.isEmpty()) {
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							BatchClassManagementMessages.NO_RECORD_TO_EXPORT));
					return;
				}
				presenter.onExportButtonClicked(identifier);
			}
		});

		delete.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				final String identifier = batchClassListView.listView.getSelectedRowIndex();
				if (identifier == null || identifier.isEmpty()) {
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							BatchClassManagementMessages.NO_RECORD_TO_DELETE));
					return;
				}
				final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get()
						.getMessageValue(BatchClassManagementMessages.DELETE_BATCH_CLASS_CONFORMATION), LocaleDictionary.get()
						.getConstantValue(BatchClassManagementConstants.DELETE_BATCH_CLASS_TITLE), Boolean.FALSE);

				confirmationDialog.addDialogListener(new DialogListener() {

					@Override
					public void onOkClick() {
						confirmationDialog.hide();
						presenter.onDeleteButtonClicked(identifier);
					}

					@Override
					public void onCancelClick() {
						confirmationDialog.hide();
					}
				});

			}
		});
	}

	/**
	 * To get Batch Class View.
	 * 
	 * @return BatchClassView
	 */
	public BatchClassView getBatchClassView() {
		return batchClassView;
	}

	/**
	 * To get Plugin View.
	 * 
	 * @return PluginView
	 */
	public PluginView getPluginView() {
		return pluginView;
	}

	/**
	 * To get Module View.
	 * 
	 * @return the moduleView
	 */
	public ModuleView getModuleView() {
		return moduleView;
	}

	/**
	 * To get Document Type View.
	 * 
	 * @return the documentTypeView
	 */
	public DocumentTypeView getDocumentTypeView() {
		return documentTypeView;
	}

	/**
	 * To get Email View.
	 * 
	 * @return the email view
	 */
	public EmailView getEmailView() {
		return emailView;
	}

	/**
	 * To get Scanner View.
	 * 
	 * @return the scanner view
	 */
	public ScannerView getScannerView() {
		return scannerView;
	}

	/**
	 * To get Batch Class Field View.
	 * 
	 * @return the batch class field view
	 */
	public BatchClassFieldView getBatchClassFieldView() {
		return batchClassFieldView;
	}

	/**
	 * To get Batch Class Bread Crumb View.
	 * 
	 * @return BatchClassBreadCrumbView
	 */
	public BatchClassBreadCrumbView getBatchClassBreadCrumbView() {
		return batchClassBreadCrumbView;
	}

	/**
	 * To get Batch Class List View.
	 * 
	 * @return BatchClassListView
	 */
	public BatchClassListView getBatchClassListView() {
		return batchClassListView;
	}

	/**
	 * To get Fuzzy DB Plugin View.
	 * 
	 * @return FuzzyDBPluginView
	 */
	public FuzzyDBPluginView getFuzzyDBPluginView() {
		return fuzzyDBPluginView;
	}

	/**
	 * To get Doc Type Fields Mapping View.
	 * 
	 * @return DocTypeFieldsMappingView
	 */
	public DocTypeFieldsMappingView getDocTypeFieldsMappingView() {
		return docTypeFieldsMappingView;
	}

	/**
	 * To get Function Key View.
	 * 
	 * @return FunctionKeyView
	 */
	public FunctionKeyView getFunctionKeyView() {
		return functionKeyView;
	}

	/**
	 * To get Edit Modules Plugin Select View.
	 * 
	 * @return the editModulesPluginSelectView
	 */
	public ConfigureModulesPluginSelectView getEditModulesPluginSelectView() {
		return editModulesPluginSelectView;
	}

	/**
	 * To get Field Type View.
	 * 
	 * @return FieldTypeView
	 */
	public FieldTypeView getFieldTypeView() {
		return fieldTypeView;
	}

	/**
	 * To get KV PP Config View.
	 * 
	 * @return KV_PP_ConfigView
	 */
	public KV_PP_ConfigView getKVPPConfigView() {
		return kvPPConfigView;
	}

	/**
	 * To get Doc Type Mapping View.
	 * 
	 * @return DocTypeMappingView
	 */
	public DocTypeMappingView getDocTypeMappingView() {
		return docTypeMappingView;
	}

	/**
	 * To get Kv Extraction View.
	 * 
	 * @return KVExtractionView
	 */
	public KVExtractionView getKvExtractionView() {
		return kvExtractionView;
	}

	/**
	 * To get Table Column Info View.
	 * 
	 * @return TableColumnInfoView
	 */
	public TableColumnInfoView getTableColumnInfoView() {
		return tableColumnInfoView;
	}

	/**
	 * To get Table Info View.
	 * 
	 * @return TableInfoView
	 */
	public TableInfoView getTableInfoView() {
		return tableInfoView;
	}

	/**
	 * To get Batch List Panel.
	 * 
	 * @return LayoutPanel
	 */
	public LayoutPanel getBatchListPanel() {
		return batchListPanel;
	}

	/**
	 * To get Regex View.
	 * 
	 * @return RegexView
	 */
	public RegexView getRegexView() {
		return regexView;
	}

	/**
	 * To get Kv PP Properties View.
	 * 
	 * @return KV_PP_PropertiesView
	 */
	public KV_PP_PropertiesView getKvPPPropertiesView() {
		return kvPPPropertiesView;
	}

	/**
	 * To create Batch Class List.
	 * 
	 * @param batches List<BatchClassDTO>
	 */
	public void createBatchClassList(List<BatchClassDTO> batches) {
		List<Record> recordList = setBatchList(batches);
		batchClassListView.listView.initTable(recordList.size(), presenter, recordList, true, false, presenter, null, false);
	}

	/**
	 * To create Batch Class List.
	 * 
	 * @param batches List<BatchClassDTO>
	 * @param totalCount int
	 */
	public void createBatchClassList(List<BatchClassDTO> batches, int totalCount) {
		List<Record> recordList = setBatchList(batches);
		batchClassListView.listView.initTable(totalCount, presenter, recordList, true, false, presenter, null, false);
	}

	/**
	 * To set Batch List.
	 * 
	 * @param batches List<BatchClassDTO>
	 * @return List<Record>
	 */
	public List<Record> setBatchList(List<BatchClassDTO> batches) {
		List<Record> recordList = new LinkedList<Record>();
		for (final BatchClassDTO batchClassDTO : batches) {
			Record record = new Record(batchClassDTO.getIdentifier());
			record.addWidget(batchClassListView.identifier, new Label(batchClassDTO.getIdentifier()));
			record.addWidget(batchClassListView.name, new Label(batchClassDTO.getName()));
			record.addWidget(batchClassListView.description, new Label(batchClassDTO.getDescription()));
			record.addWidget(batchClassListView.priority, new Label(batchClassDTO.getPriority()));
			record.addWidget(batchClassListView.version, new Label(batchClassDTO.getVersion()));
			recordList.add(record);
		}
		mainDockPanel.clear();
		mainDockPanel.add(mainPanel);
		mainPanel.clear();
		return recordList;
	}

	/**
	 * To show Batch Class View.
	 */
	public void showBatchClassView() {
		tabLayoutPanel.clear();
		tabLayoutPanel.add(batchClassView);
		batchClassView.setVisible(true);
		bottomButtons.setVisible(true);
		mainDockPanel.clear();
		batchClassBreadCrumbView.setVisible(true);
		mainDockPanel.addNorth(batchClassBreadCrumbView, BatchClassManagementConstants.SIX_POINT_ZERO);
		presenter.getBatchClassBreadCrumbPresenter().getView().getBreadCrumbPanel().setWidth("100%");
		mainDockPanel.addSouth(bottomPanel, BatchClassManagementConstants.EIGHT_POINT_ZERO);
		mainDockPanel.add(mainPanel);
	}

	/**
	 * To show Module View.
	 */
	public void showModuleView() {
		tabLayoutPanel.clear();
		moduleView.setVisible(true);
		moduleView.getEditModuleViewPanel().setVisible(false);
		moduleView.getModuleDetailViewPanel().setVisible(true);
		tabLayoutPanel.add(moduleView);
	}

	/**
	 * To show Add Module View.
	 */
	public void showAddModuleView() {
		tabLayoutPanel.clear();
		addModuleView.setVisible(true);
		tabLayoutPanel.add(addModuleView);
	}

	/**
	 * To show Edit Module View.
	 */
	public void showEditModuleView() {
		tabLayoutPanel.clear();
		editModulesPluginSelectView.setVisible(true);
		tabLayoutPanel.add(editModulesPluginSelectView);
	}

	/**
	 * To show Plugin View.
	 */
	public void showPluginView() {
		tabLayoutPanel.clear();
		pluginView.setVisible(true);
		tabLayoutPanel.add(pluginView);
	}

	/**
	 * To show Document View.
	 */
	public void showDocumentView() {
		tabLayoutPanel.clear();
		documentTypeView.setVisible(true);
		tabLayoutPanel.add(documentTypeView);
	}

	/**
	 * To show Email View.
	 */
	public void showEmailView() {
		tabLayoutPanel.clear();
		emailView.setVisible(true);
		tabLayoutPanel.add(emailView);
	}

	/**
	 * To show Scanner View.
	 */
	public void showScannerView() {
		tabLayoutPanel.clear();
		scannerView.setVisible(true);
		tabLayoutPanel.add(scannerView);
	}

	/**
	 * To show Batch Class Field View.
	 */
	public void showBatchClassFieldView() {
		tabLayoutPanel.clear();
		batchClassFieldView.setVisible(true);
		tabLayoutPanel.add(batchClassFieldView);
	}

	/**
	 * To show Batch Class List View.
	 */
	public void showBatchClassListView() {
		mainPanel.clear();
		buttonPanel.addStyleName("width100");
		mainPanel.addNorth(buttonPanel, BatchClassManagementConstants.SEVEN);
		mainPanel.add(batchListPanel);
		mainPanel.setStyleName("mainPanelLayout");
		bottomButtons.setVisible(false);
		mainDockPanel.clear();
		presenter.getBatchClassBreadCrumbPresenter().createBreadCrumb();
		mainDockPanel.addNorth(batchClassBreadCrumbView, BatchClassManagementConstants.SIX_POINT_ZERO);
		presenter.getBatchClassBreadCrumbPresenter().getView().getBreadCrumbPanel().setWidth("100%");
		mainDockPanel.add(mainPanel);
	}

	/**
	 * To show KV Extraction View.
	 */
	public void showKVExtractionView() {
		tabLayoutPanel.clear();
		kvExtractionView.setVisible(true);
		tabLayoutPanel.add(kvExtractionView);
	}

	/**
	 * To get Deploy.
	 * 
	 * @return the deploy
	 */
	public Button getDeploy() {
		return deploy;
	}

	/**
	 * To get Validate.
	 * 
	 * @return the validate
	 */
	public Button getValidate() {
		return validate;
	}

	/**
	 * To show Advanced KV Extraction View.
	 */
	public void showAdvancedKVExtractionView() {
		tabLayoutPanel.clear();
		advancedKVExtractionView.setVisible(true);
		advancedKVExtractionView.togglePageImageShowHide(false);
		toggleBottomPanelShowHide(false);
		advancedKVExtractionView.initialize();
		tabLayoutPanel.add(advancedKVExtractionView);
	}

	/**
	 * To show Regex View.
	 */
	public void showRegexView() {
		tabLayoutPanel.clear();
		regexView.setVisible(true);
		tabLayoutPanel.add(regexView);
	}

	/**
	 * To show Fuzzy DB Plugin View.
	 */
	public void showFuzzyDBPluginView() {
		tabLayoutPanel.clear();
		fuzzyDBPluginView.setVisible(true);
		tabLayoutPanel.add(fuzzyDBPluginView);
	}

	/**
	 * To show Doc Type Field Mapping View.
	 */
	public void showDocTypeFieldMappingView() {
		tabLayoutPanel.clear();
		docTypeFieldsMappingView.setVisible(true);
		tabLayoutPanel.add(docTypeFieldsMappingView);
	}

	/**
	 * To show Function Key View.
	 */
	public void showFunctionKeyView() {
		tabLayoutPanel.clear();
		functionKeyView.setVisible(true);
		tabLayoutPanel.add(functionKeyView);
	}

	/**
	 * To show Field Type View.
	 */
	public void showFieldTypeView() {
		tabLayoutPanel.clear();
		fieldTypeView.setVisible(true);
		tabLayoutPanel.add(fieldTypeView);
	}

	/**
	 * To show Tc Info View.
	 */
	public void showTcInfoView() {
		tabLayoutPanel.clear();
		tableColumnInfoView.setVisible(true);
		tabLayoutPanel.add(tableColumnInfoView);
	}

	/**
	 * To show Table Info View.
	 */
	public void showTableInfoView() {
		tabLayoutPanel.clear();
		tableInfoView.setVisible(true);
		tabLayoutPanel.add(tableInfoView);
	}

	/**
	 * To show Doc Type Mapping View.
	 */
	public void showDocTypeMappingView() {
		tabLayoutPanel.clear();
		docTypeMappingView.setVisible(true);
		tabLayoutPanel.add(docTypeMappingView);
	}

	/**
	 * To show KV PP Plugin Configuration View.
	 */
	public void showKVPPPluginConfigView() {
		presenter.getKvPPPropertiesPresenter().getView().getKvppViewEditPluginPanel().clear();
		presenter.getKvPPPropertiesPresenter().getView().getKvppViewEditPluginPanel().add(kvPPConfigView);
		kvPPConfigView.setVisible(true);
	}

	/**
	 * To show Kv PP Properties View.
	 */
	public void showKvPPPropertiesView() {
		tabLayoutPanel.clear();
		kvPPPropertiesView.setVisible(true);
		tabLayoutPanel.add(kvPPPropertiesView);
	}

	/**
	 * To show Advanced Table Extraction View.
	 */
	public void showAdvancedTableExtractionView() {
		tabLayoutPanel.clear();
		advancedTableExtractionView.setVisible(true);
		advancedTableExtractionView.togglePageImageShowHide(false);
		toggleBottomPanelShowHide(false);
		tabLayoutPanel.add(advancedTableExtractionView);
	}

	/**
	 * To get Submit Button.
	 * 
	 * @return Button
	 */
	public Button getSubmitButton() {
		return submit;
	}

	/**
	 * To get Cancel Button.
	 * 
	 * @return Button
	 */
	public Button getCancelButton() {
		return cancel;
	}

	/**
	 * To get Add Module View.
	 * 
	 * @return the addModuleView
	 */
	public ConfigureModuleView getAddModuleView() {
		return addModuleView;
	}

	/**
	 * To get Sample Button.
	 * 
	 * @return Button
	 */
	public Button getSampleButton() {
		return sample;
	}

	/**
	 * To get Learn Button.
	 * 
	 * @return Button
	 */
	public Button getLearnButton() {
		return learn;
	}

	/**
	 * To get Learn DB Button.
	 * 
	 * @return Button
	 */
	public Button getLearnDBButton() {
		return learnDB;
	}

	/**
	 * To get Apply Button.
	 * 
	 * @return Button
	 */
	public Button getApplyButton() {
		return apply;
	}

	/**
	 * To set Screen Visibility.
	 * 
	 * @param batchClassDTO BatchClassDTO
	 */
	public void setScreenVisibility(final BatchClassDTO batchClassDTO) {
		mainPanel.clear();
		mainPanel.removeStyleName("mainPanelLayout");
		tabLayoutPanel.addStyleName("leftPadding");
		batchClassBreadCrumbView.addStyleName("leftPadding");
		tabLayoutPanel.setVisible(Boolean.TRUE);
		tabLayoutPanel.clear();
		tabLayoutPanel.add(batchClassView);
		mainPanel.addNorth(tabLayoutPanel, BatchClassManagementConstants.HUNDRED);
		presenter.initiateBatchClassView(batchClassDTO.getIdentifier());
	}

	/**
	 * To perform operations on save click.
	 * 
	 * @param event ClickEvent
	 */
	@UiHandler("submit")
	public void onSaveClicked(ClickEvent event) {
		presenter.onSaveClicked();
	}

	/**
	 * To perform operations on cancel click.
	 * 
	 * @param event ClickEvent
	 */
	@UiHandler("cancel")
	public void onCancelClicked(ClickEvent event) {
		presenter.onCancelClicked();
	}

	/**
	 * To perform operations on deploy click.
	 * 
	 * @param event ClickEvent
	 */
	@UiHandler("deploy")
	public void onDeployClicked(ClickEvent event) {
		presenter.onDeployClicked();
	}

	/**
	 * To perform operations on validate click.
	 * 
	 * @param event ClickEvent
	 */
	@UiHandler("validate")
	public void onValidateClicked(ClickEvent event) {
		presenter.onValidateClicked();
	}

	/**
	 * To perform operations on sample folders click.
	 * 
	 * @param event ClickEvent
	 */
	@UiHandler("sample")
	public void onSampleFoldersClicked(ClickEvent event) {
		presenter.onSampleFoldersClicked();
	}

	/**
	 * To perform operations on learn files click.
	 * 
	 * @param event ClickEvent
	 */
	@UiHandler("learn")
	public void onLearnFilesClicked(ClickEvent event) {
		presenter.onLearnFilesClicked();
	}

	/**
	 * To perform operations on learn DB click.
	 * 
	 * @param event ClickEvent
	 */
	@UiHandler("learnDB")
	public void onLearnDBClicked(ClickEvent event) {
		presenter.onLearnDBClicked();
	}

	/**
	 * To perform operations on apply click.
	 * 
	 * @param event ClickEvent
	 */
	@UiHandler("apply")
	public void onApplyClicked(ClickEvent event) {
		presenter.onApplyClicked();
	}

	/**
	 * To get Kv PP Add Edit View.
	 * 
	 * @return KV_PP_AddEditListView
	 */
	public KV_PP_AddEditListView getKvPPAddEditView() {
		return kvPPAddEditListView;
	}

	/**
	 * To show KV PP Add Edit List View.
	 */
	public void showKvPPAddEditListView() {
		tabLayoutPanel.clear();
		kvPPAddEditListView.setVisible(true);
		tabLayoutPanel.add(kvPPAddEditListView);
	}

	/**
	 * To get Advanced KV Extraction View.
	 * 
	 * @return AdvancedKVExtractionView
	 */
	public AdvancedKVExtractionView getAdvancedKVExtractionView() {
		return advancedKVExtractionView;
	}

	/**
	 * To get Advanced Table Extraction View.
	 * 
	 * @return AdvancedTableExtractionView
	 */
	public AdvancedTableExtractionView getAdvancedTableExtractionView() {
		return advancedTableExtractionView;
	}

	/**
	 * To toggle Bottom Panel Show Hide.
	 * 
	 * @param visibile boolean
	 */
	public void toggleBottomPanelShowHide(boolean visibile) {
		bottomPanel.setVisible(visibile);
	}

	/**
	 * To set Total Row Count.
	 * 
	 * @param totalRowCount String
	 */
	public void setTotalRowCount(String totalRowCount) {
		int rowCount = 0;
		try {
			if (totalRowCount != null && !totalRowCount.isEmpty()) {
				rowCount = Integer.parseInt(totalRowCount);
			}
		} catch (NumberFormatException nfe) {
			rowCount = BatchClassManagementConstants.DEFAULT_ROW_COUNT;
		}
		batchClassListView.listView.setTableRowCount(rowCount);
	}

	/**
	 * To toggle Deploy Button Enable.
	 * 
	 * @param enable boolean
	 */
	public void toggleDeployButtonEnable(boolean enable) {
		deploy.setEnabled(enable);
		validate.setEnabled(!enable);

	}

	/**
	 * To get Batch Class Field List View.
	 * 
	 * @return BatchClassFieldListView
	 */
	public BatchClassFieldListView getBatchClassFieldListView() {
		return batchClassFieldListView;
	}

	/**
	 * To set Deploy And Validate Button Enable.
	 * 
	 * @param enable boolean
	 */
	public void setDeployAndValidateButtonEnable(boolean enable) {
		deploy.setEnabled(enable);
		validate.setEnabled(enable);
	}

	/**
	 * This method sets buttons enable attribute for super-admin.
	 * 
	 * @param enable {link: Boolean} true/false.
	 */
	public void setButtonsEnableAttributeForSuperAdmin(Boolean enable) {
		if (null != enable) {
			importButton.setEnabled(enable);
			copy.setEnabled(enable);
		}
	}

	/**
	 * To get Event Bus.
	 * 
	 * @return the eventBus
	 */
	public HandlerManager getEventBus() {
		return eventBus;
	}

	/**
	 * To set Event Bus.
	 * 
	 * @param eventBus HandlerManager
	 */
	public void setEventBus(HandlerManager eventBus) {
		this.eventBus = eventBus;
	}

	/**
	 * To get Cmis Importer View.
	 * 
	 * @return CmisImporterView
	 */
	public CmisImporterView getCmisImporterView() {
		return cmisImporterView;
	}

	/**
	 * To show Cmis Importer View.
	 */
	public void showCmisImporterView() {
		tabLayoutPanel.clear();
		cmisImporterView.setVisible(true);
		tabLayoutPanel.add(cmisImporterView);
	}

	/**
	 * To get Box Exporter Plugin View.
	 * 
	 * @return BoxExporterPluginView
	 */
	public BoxExporterPluginView getBoxExporterPluginView() {
		return boxExporterPluginView;
	}

	/**
	 * To show Box Plugin View.
	 */
	public void showBoxPluginView() {
		tabLayoutPanel.clear();
		boxExporterPluginView.setVisible(true);
		tabLayoutPanel.add(boxExporterPluginView);
	}

}
