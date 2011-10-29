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
import com.ephesoft.dcma.gwt.admin.bm.client.view.batchclassfield.BatchClassFieldView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.documenttype.DocumentTypeView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.email.EmailView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.fieldtype.FieldTypeView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.functionkey.FunctionKeyView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.kvextraction.KVExtractionView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.kvextraction.AdvancedKVExtraction.AdvancedKVExtractionView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.module.ModuleView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.plugin.DocTypeFieldsMappingView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.plugin.DocTypeMappingView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.plugin.FuzzyDBPluginView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.plugin.KV_PP_AddEditListView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.plugin.KV_PP_PropertiesView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.plugin.PluginView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.regex.RegexView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.tablecolumninfo.TableColumnInfoView;
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

public class BatchClassManagementView extends View<BatchClassManagementPresenter> {

	interface Binder extends UiBinder<DockLayoutPanel, BatchClassManagementView> {
	}

	@UiField
	protected LayoutPanel tabLayoutPanel;

	@UiField
	protected BatchClassBreadCrumbView batchClassBreadCrumbView;

	@UiField
	protected BatchClassView batchClassView;

	@UiField
	protected ModuleView moduleView;

	@UiField
	protected PluginView pluginView;

	@UiField
	protected DocumentTypeView documentTypeView;

	@UiField
	protected EmailView emailView;

	@UiField
	protected BatchClassFieldView batchClassFieldView;

	@UiField
	protected DockLayoutPanel mainPanel;

	@UiField
	protected FuzzyDBPluginView fuzzyDBPluginView;

	@UiField
	protected DocTypeFieldsMappingView docTypeFieldsMappingView;

	@UiField
	protected FieldTypeView fieldTypeView;

	@UiField
	protected FunctionKeyView functionKeyView;

	@UiField
	protected TableInfoView tableInfoView;

	@UiField
	protected DocTypeMappingView docTypeMappingView;

	@UiField
	protected KVExtractionView kvExtractionView;

	@UiField
	protected TableColumnInfoView tableColumnInfoView;

	@UiField
	protected RegexView regexView;

	@UiField
	protected KV_PP_PropertiesView kvPPPropertiesView;

	@UiField
	protected KV_PP_AddEditListView kvPPAddEditListView;

	@UiField
	protected AdvancedKVExtractionView advancedKVExtractionView;

	protected final LayoutPanel batchListPanel;

	private final HorizontalPanel buttonPanel;

	private BatchClassListView batchClassListView;

	@UiField
	protected HorizontalPanel bottomButtons;

	@UiField
	protected Button submit;

	@UiField
	protected Button cancel;

	@UiField
	protected Button sample;

	@UiField
	protected Button learn;

	@UiField
	protected Button learnDB;

	@UiField
	protected DockLayoutPanel mainDockPanel;

	@UiField
	protected Button apply;

	@UiField
	protected VerticalPanel bottomPanel;

	@UiField
	protected HorizontalPanel rightButtons;

	private Button edit;
	private Button copy;
	private Button delete;
	private Button export;

	private Button importButton;

	private static final Binder BINDER = GWT.create(Binder.class);

	public BatchClassManagementView() {
		initWidget(BINDER.createAndBindUi(this));

		edit = new Button();
		copy = new Button();
		delete = new Button();
		export = new Button();
		importButton = new Button();
		bottomPanel.addStyleName("leftPadding");
		bottomPanel.addStyleName("fullWidth");
		edit.setText(AdminConstants.EDIT_BUTTON);
		copy.setText(AdminConstants.COPY_BUTTON);
		delete.setText(AdminConstants.DELETE_BUTTON);
		export.setText(AdminConstants.EXPORT_BUTTON);
		importButton.setText(AdminConstants.IMPORT_BUTTON);
		apply.setText(AdminConstants.APPLY_BUTTON);
		buttonPanel = new HorizontalPanel();
		buttonPanel.setWidth("100%");
		buttonPanel.setSpacing(5);
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		buttonPanel.setVerticalAlignment(HasAlignment.ALIGN_TOP);
		buttonPanel.add(export);
		buttonPanel.add(importButton);
		buttonPanel.add(edit);
		buttonPanel.setCellWidth(edit, "95%");
		buttonPanel.add(new Label(""));
		buttonPanel.add(copy);
		buttonPanel.add(new Label(""));
		buttonPanel.add(delete);

		batchListPanel = new LayoutPanel();
		batchClassListView = new BatchClassListView();
		batchListPanel.add(batchClassListView.listView);
		submit.setText(AdminConstants.SAVE_BUTTON);
		cancel.setText(AdminConstants.CANCEL_BUTTON);
		learn.setText(AdminConstants.LEARN_FILES_BUTTON);
		sample.setText(AdminConstants.GENERATE_FOLDERS_BUTTON);
		learnDB.setText(AdminConstants.LEARN_DB_BUTTON);
		edit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				String identifier = batchClassListView.listView.getSelectedRowIndex();
				if (identifier == null || identifier.isEmpty()) {
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							BatchClassManagementMessages.NONE_SELECTED_WARNING));
					return;
				}
				presenter.onEditButtonClicked(identifier);
			}
		});

		copy.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				String identifier = batchClassListView.listView.getSelectedRowIndex();
				if (identifier == null || identifier.isEmpty()) {
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							BatchClassManagementMessages.NONE_SELECTED_WARNING));
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
							BatchClassManagementMessages.NONE_SELECTED_WARNING));
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
							BatchClassManagementMessages.NONE_SELECTED_WARNING));
					return;
				}
				final ConfirmationDialog confirmationDialog = new ConfirmationDialog();
				confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.DELETE_BATCH_CLASS_CONFORMATION));
				confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.DELETE_BATCH_CLASS_TITLE));
				confirmationDialog.center();
				confirmationDialog.show();
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
				confirmationDialog.okButton.setFocus(true);
			}
		});
	}

	public BatchClassView getBatchClassView() {
		return batchClassView;
	}

	public PluginView getPluginView() {
		return pluginView;
	}

	/**
	 * @return the moduleView
	 */
	public ModuleView getModuleView() {
		return moduleView;
	}

	/**
	 * @return the documentTypeView
	 */
	public DocumentTypeView getDocumentTypeView() {
		return documentTypeView;
	}

	/**
	 * 
	 * @return the email view
	 */
	public EmailView getEmailView() {
		return emailView;
	}

	/**
	 * 
	 * @return the batch class field view
	 */
	public BatchClassFieldView getBatchClassFieldView() {
		return batchClassFieldView;
	}

	public BatchClassBreadCrumbView getBatchClassBreadCrumbView() {
		return batchClassBreadCrumbView;
	}

	public BatchClassListView getBatchClassListView() {
		return batchClassListView;
	}

	public FuzzyDBPluginView getFuzzyDBPluginView() {
		return fuzzyDBPluginView;
	}

	public DocTypeFieldsMappingView getDocTypeFieldsMappingView() {
		return docTypeFieldsMappingView;
	}

	public FunctionKeyView getFunctionKeyView() {
		return functionKeyView;
	}

	public FieldTypeView getFieldTypeView() {
		return fieldTypeView;
	}

	public DocTypeMappingView getDocTypeMappingView() {
		return docTypeMappingView;
	}

	public KVExtractionView getKvExtractionView() {
		return kvExtractionView;
	}

	public TableColumnInfoView getTableColumnInfoView() {
		return tableColumnInfoView;
	}

	public TableInfoView getTableInfoView() {
		return tableInfoView;
	}

	public LayoutPanel getBatchListPanel() {
		return batchListPanel;
	}

	public RegexView getRegexView() {
		return regexView;
	}

	public KV_PP_PropertiesView getKvPPPropertiesView() {
		return kvPPPropertiesView;
	}

	public void createBatchList(List<BatchClassDTO> batches) {
		List<Record> recordList = setBatchList(batches);
		batchClassListView.listView.initTable(recordList.size(), presenter, recordList, true, false);
	}

	public void createBatchList(List<BatchClassDTO> batches, int totalCount) {
		List<Record> recordList = setBatchList(batches);
		batchClassListView.listView.initTable(totalCount, presenter, recordList, true, false);
	}

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

	public void showBatchClassView() {
		tabLayoutPanel.clear();
		tabLayoutPanel.add(batchClassView);
		batchClassView.setVisible(true);
		bottomButtons.setVisible(true);
		mainDockPanel.clear();
		mainDockPanel.addNorth(batchClassBreadCrumbView, 6.0);
		presenter.getBatchClassBreadCrumbPresenter().getView().getBreadCrumbPanel().setWidth("100%");
		mainDockPanel.addSouth(bottomPanel, 8.0);
		mainDockPanel.add(mainPanel);
	}

	public void showModuleView() {
		tabLayoutPanel.clear();
		moduleView.setVisible(true);
		moduleView.getEditModuleViewPanel().setVisible(false);
		moduleView.getModuleDetailViewPanel().setVisible(true);
		tabLayoutPanel.add(moduleView);
	}

	public void showPluginView() {
		tabLayoutPanel.clear();
		pluginView.setVisible(true);
		tabLayoutPanel.add(pluginView);
	}

	public void showDocumentView() {
		tabLayoutPanel.clear();
		documentTypeView.setVisible(true);
		tabLayoutPanel.add(documentTypeView);
	}

	public void showEmailView() {
		tabLayoutPanel.clear();
		emailView.setVisible(true);
		tabLayoutPanel.add(emailView);
	}

	public void showBatchClassFieldView() {
		tabLayoutPanel.clear();
		batchClassFieldView.setVisible(true);
		tabLayoutPanel.add(batchClassFieldView);
	}

	public void showBatchClassListView() {
		mainPanel.clear();
		buttonPanel.addStyleName("width100");
		mainPanel.addNorth(buttonPanel, 7);
		mainPanel.add(batchListPanel);
		mainPanel.setStyleName("mainPanelLayout");
		bottomButtons.setVisible(false);
	}

	public void showKVExtractionView() {
		tabLayoutPanel.clear();
		kvExtractionView.setVisible(true);
		tabLayoutPanel.add(kvExtractionView);
	}

	public void showAdvancedKVExtractionView() {
		tabLayoutPanel.clear();
		advancedKVExtractionView.setVisible(true);
		advancedKVExtractionView.togglePageImageShowHide(false);
		toggleBottomPanelShowHide(false);
		tabLayoutPanel.add(advancedKVExtractionView);
	}

	public void showRegexView() {
		tabLayoutPanel.clear();
		regexView.setVisible(true);
		tabLayoutPanel.add(regexView);
	}

	public void showFuzzyDBPluginView() {
		tabLayoutPanel.clear();
		fuzzyDBPluginView.setVisible(true);
		tabLayoutPanel.add(fuzzyDBPluginView);
	}

	public void showDocTypeFieldMappingView() {
		tabLayoutPanel.clear();
		docTypeFieldsMappingView.setVisible(true);
		tabLayoutPanel.add(docTypeFieldsMappingView);
	}

	public void showFunctionKeyView() {
		tabLayoutPanel.clear();
		functionKeyView.setVisible(true);
		tabLayoutPanel.add(functionKeyView);
	}

	public void showFieldTypeView() {
		tabLayoutPanel.clear();
		fieldTypeView.setVisible(true);
		tabLayoutPanel.add(fieldTypeView);
	}

	public void showTcInfoView() {
		tabLayoutPanel.clear();
		tableColumnInfoView.setVisible(true);
		tabLayoutPanel.add(tableColumnInfoView);
	}

	public void showTableInfoView() {
		tabLayoutPanel.clear();
		tableInfoView.setVisible(true);
		tabLayoutPanel.add(tableInfoView);
	}

	public void showDocTypeMappingView() {
		tabLayoutPanel.clear();
		docTypeMappingView.setVisible(true);
		tabLayoutPanel.add(docTypeMappingView);
	}

	public void showKvPPPropertiesView() {
		tabLayoutPanel.clear();
		kvPPPropertiesView.setVisible(true);
		tabLayoutPanel.add(kvPPPropertiesView);
	}

	public Button getSubmitButton() {
		return submit;
	}

	public Button getCancelButton() {
		return cancel;
	}

	public Button getSampleButton() {
		return sample;
	}

	public Button getLearnButton() {
		return learn;
	}

	public Button getLearnDBButton() {
		return learnDB;
	}

	public Button getApplyButton() {
		return apply;
	}

	public void setScreenVisibility(final BatchClassDTO batchClassDTO) {
		mainPanel.clear();
		mainPanel.removeStyleName("mainPanelLayout");
		tabLayoutPanel.addStyleName("leftPadding");
		batchClassBreadCrumbView.addStyleName("leftPadding");
		tabLayoutPanel.setVisible(Boolean.TRUE);
		tabLayoutPanel.clear();
		tabLayoutPanel.add(batchClassView);
		mainPanel.addNorth(tabLayoutPanel, 100);
		presenter.initiateBatchClassView(batchClassDTO.getIdentifier());
	}

	@UiHandler("submit")
	public void onSaveClicked(ClickEvent event) {
		presenter.onSaveClicked();
	}

	@UiHandler("cancel")
	public void onCancelClicked(ClickEvent event) {
		presenter.onCancelClicked();
	}

	@UiHandler("sample")
	public void onSampleFoldersClicked(ClickEvent event) {
		presenter.onSampleFoldersClicked();
	}

	@UiHandler("learn")
	public void onLearnFilesClicked(ClickEvent event) {
		presenter.onLearnFilesClicked();
	}

	@UiHandler("learnDB")
	public void onLearnDBClicked(ClickEvent event) {
		presenter.onLearnDBClicked();
	}

	@UiHandler("apply")
	public void onApplyClicked(ClickEvent event) {
		presenter.onApplyClicked();
	}

	public KV_PP_AddEditListView getKvPPAddEditView() {
		return kvPPAddEditListView;
	}

	public void showKvPPAddEditListView() {
		tabLayoutPanel.clear();
		kvPPAddEditListView.setVisible(true);
		tabLayoutPanel.add(kvPPAddEditListView);
	}

	public AdvancedKVExtractionView getAdvancedKVExtractionView() {
		return advancedKVExtractionView;
	}

	public void toggleBottomPanelShowHide(boolean visibile) {
		bottomPanel.setVisible(visibile);
	}

}
