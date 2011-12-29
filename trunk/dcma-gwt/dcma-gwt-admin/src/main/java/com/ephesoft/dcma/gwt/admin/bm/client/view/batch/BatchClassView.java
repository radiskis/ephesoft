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

package com.ephesoft.dcma.gwt.admin.bm.client.view.batch;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.batch.BatchClassViewPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.batchclassfield.BatchClassFieldListPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.documenttype.CopyDocumentTypePresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.documenttype.DocumentTypeListPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.email.EmailListPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.module.ModuleListPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.batchclassfield.BatchClassFieldListView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.documenttype.CopyDocumentView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.documenttype.DocumentTypeListView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.email.EmailListView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.module.ModuleListView;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView;
import com.ephesoft.dcma.gwt.core.client.ui.table.Record;
import com.ephesoft.dcma.gwt.core.shared.BatchClassFieldDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassModuleDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.DocumentTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.EmailConfigurationDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BatchClassView extends View<BatchClassViewPresenter> {

	interface Binder extends UiBinder<DockLayoutPanel, BatchClassView> {
	}

	@UiField
	protected LayoutPanel viewEditLayoutPanel;

	@UiField
	protected BatchClassDetailView batchClassDetailView;

	@UiField
	protected EditBatchClassView editBatchClassView;

	@UiField
	protected LayoutPanel moduleListPanel;

	@UiField
	protected DockLayoutPanel docTypeListPanel;

	@UiField
	protected DockLayoutPanel moduleTypeListPanel;

	@UiField
	protected Button editBatchPropertiesButton;

	@UiField
	protected CaptionPanel batchClassConfigurationCaptionPanel;

	@UiField
	protected VerticalPanel batchClassViewVerticalPanel;

	@UiField
	protected VerticalPanel batchClassConfigVerticalPanel;

	private final ModuleListView moduleListView;

	private final DocumentTypeListView docTypeListView;

	private final EmailListView emailListView;

	@UiField
	protected LayoutPanel docTypeLayoutPanel;

	@UiField
	protected LayoutPanel emailLayoutPanel;

	@UiField
	protected Button addDocumentButton;

	@UiField
	protected Button editDocumentButton;

	@UiField
	protected Button copyDocumentButton;

	@UiField
	protected Button deleteDocumentButton;

	@UiField
	protected Button editModuleButton;

	@UiField
	protected HorizontalPanel buttonPanel;

	@UiField
	protected HorizontalPanel documentButtonPanel;

	@UiField
	protected DockLayoutPanel emailListPanel;

	@UiField
	protected HorizontalPanel emailButtonPanel;

	@UiField
	protected Button addEmailButton;

	@UiField
	protected Button editEmailButton;

	@UiField
	protected Button deleteEmailButton;

	private final BatchClassFieldListView batchClassFieldListView;

	private BatchClassFieldListPresenter batchClassFieldListPresenter;

	private ModuleListPresenter moduleListPresenter;

	private EmailListPresenter emailListPresenter;
	@UiField
	protected LayoutPanel batchClassFieldLayoutPanel;

	@UiField
	protected DockLayoutPanel batchClassFieldListPanel;

	@UiField
	protected HorizontalPanel batchClassFieldButtonPanel;

	@UiField
	protected Button addBatchClassFieldButton;

	@UiField
	protected Button editBatchClassFieldButton;

	@UiField
	protected Button deleteBatchClassFieldButton;

	private DocumentTypeListPresenter documentTypeListPresenter;

	private static final Binder BINDER = GWT.create(Binder.class);

	private static final String TWENTY_PIXEL = "20px";

	public BatchClassView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		moduleListView = new ModuleListView();
		addDocumentButton.setText(AdminConstants.ADD_BUTTON);
		addEmailButton.setText(AdminConstants.ADD_BUTTON);
		editDocumentButton.setText(AdminConstants.EDIT_BUTTON);
		copyDocumentButton.setText(AdminConstants.COPY_BUTTON);
		editEmailButton.setText(AdminConstants.EDIT_BUTTON);
		deleteDocumentButton.setText(AdminConstants.DELETE_BUTTON);
		deleteEmailButton.setText(AdminConstants.DELETE_BUTTON);
		docTypeListView = new DocumentTypeListView();
		moduleListPanel.add(moduleListView.listView);
		docTypeLayoutPanel.add(docTypeListView.listView);
		emailListView = new EmailListView();
		emailLayoutPanel.add(emailListView.listView);
		batchClassConfigurationCaptionPanel.setCaptionHTML(AdminConstants.BATCH_CLASS_CONFIGURATION_HTML);
		editBatchPropertiesButton.setText(AdminConstants.EDIT_BUTTON);
		batchClassViewVerticalPanel.add(editBatchPropertiesButton);
		addDocumentButton.setHeight(TWENTY_PIXEL);
		addEmailButton.setHeight(TWENTY_PIXEL);
		editDocumentButton.setHeight(TWENTY_PIXEL);
		copyDocumentButton.setHeight(TWENTY_PIXEL);
		editEmailButton.setHeight(TWENTY_PIXEL);
		deleteDocumentButton.setHeight(TWENTY_PIXEL);
		deleteEmailButton.setHeight(TWENTY_PIXEL);
		editModuleButton.setText(AdminConstants.EDIT_BUTTON);
		editModuleButton.setHeight(TWENTY_PIXEL);
		buttonPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		documentButtonPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		emailButtonPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		buttonPanel.addStyleName(AdminConstants.PADDING_STYLE_TOPPADD);
		documentButtonPanel.addStyleName(AdminConstants.PADDING_STYLE_TOPPADD);
		emailButtonPanel.addStyleName(AdminConstants.PADDING_STYLE_TOPPADD);

		addBatchClassFieldButton.setText(AdminConstants.ADD_BUTTON);
		editBatchClassFieldButton.setText(AdminConstants.EDIT_BUTTON);
		deleteBatchClassFieldButton.setText(AdminConstants.DELETE_BUTTON);
		addBatchClassFieldButton.setHeight(TWENTY_PIXEL);
		editBatchClassFieldButton.setHeight(TWENTY_PIXEL);
		deleteBatchClassFieldButton.setHeight(TWENTY_PIXEL);
		batchClassFieldButtonPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		batchClassFieldButtonPanel.addStyleName(AdminConstants.PADDING_STYLE_TOPPADD);
		batchClassFieldListView = new BatchClassFieldListView();
		batchClassFieldListPresenter = null;
		documentTypeListPresenter = null;
		moduleListPresenter = null;
		emailListPresenter = null;

		batchClassFieldLayoutPanel.add(batchClassFieldListView.listView);
	}

	public void createModuleList(Collection<BatchClassModuleDTO> modules) {
		List<Record> recordList = setModuleList(modules);
		moduleListPresenter = new ModuleListPresenter(presenter.getController(), moduleListView);
		moduleListPresenter.setModuleDTOList(modules);

		moduleListView.listView.initTable(recordList.size(), moduleListPresenter, recordList, true, false);
	}

	public List<Record> setModuleList(Collection<BatchClassModuleDTO> modules) {

		List<Record> recordList = new LinkedList<Record>();
		for (final BatchClassModuleDTO batchClassModuleDTO : modules) {
			Record record = new Record(batchClassModuleDTO.getIdentifier());
			record.addWidget(moduleListView.name, new Label(batchClassModuleDTO.getModule().getName()));
			record.addWidget(moduleListView.description, new Label(batchClassModuleDTO.getModule().getDescription()));
			recordList.add(record);
		}
		return recordList;
	}

	public void createEmailList(Collection<EmailConfigurationDTO> emailConfigurationDTOs) {
		List<Record> recordList = setEmailList(emailConfigurationDTOs);
		emailListPresenter = new EmailListPresenter(presenter.getController(), emailListView);
		emailListPresenter.setEmailConfigurationDTOList(emailConfigurationDTOs);
		emailListView.listView.initTable(recordList.size(), emailListPresenter, recordList, true, false);
	}

	public List<Record> setEmailList(Collection<EmailConfigurationDTO> emailConfigurationDTOs) {

		List<Record> recordList = new LinkedList<Record>();
		for (final EmailConfigurationDTO emailConfigurationDTO : emailConfigurationDTOs) {
			Record record = new Record(emailConfigurationDTO.getIdentifier());
			record.addWidget(emailListView.userName, new Label(emailConfigurationDTO.getUserName()));
			record.addWidget(emailListView.password, new Label(emailConfigurationDTO.getPassword()));
			record.addWidget(emailListView.serverName, new Label(emailConfigurationDTO.getServerName()));
			record.addWidget(emailListView.serverType, new Label(emailConfigurationDTO.getServerType()));
			record.addWidget(emailListView.folderName, new Label(emailConfigurationDTO.getFolderName()));
			Integer portNumber = emailConfigurationDTO.getPortNumber();
			if (portNumber != null) {
				record.addWidget(emailListView.portNumbner, new Label(String.valueOf(portNumber)));
			} else {
				record.addWidget(emailListView.portNumbner, new Label(AdminConstants.EMPTY_STRING));
			}

			Boolean isSSL = emailConfigurationDTO.getIsSSL();
			if (isSSL != null) {
				record.addWidget(emailListView.isSSL, new Label(String.valueOf(isSSL)));
			} else {
				record.addWidget(emailListView.isSSL, new Label(Boolean.FALSE.toString()));
			}
			recordList.add(record);
		}
		return recordList;
	}

	public void createBatchClassFieldList(Collection<BatchClassFieldDTO> batchClassFieldDTOs) {

		List<Record> recordList = setBatchClassFieldList(batchClassFieldDTOs);
		batchClassFieldListPresenter = new BatchClassFieldListPresenter(presenter.getController(), batchClassFieldListView);
		batchClassFieldListPresenter.setBatchClassFieldDTOList(batchClassFieldDTOs);
		batchClassFieldListView.listView.initTable(recordList.size(), batchClassFieldListPresenter, recordList, true, false);
	}

	public List<Record> setBatchClassFieldList(Collection<BatchClassFieldDTO> batchClassFieldDTOs) {

		List<Record> recordList = new LinkedList<Record>();
		for (final BatchClassFieldDTO batchClassFieldDTO : batchClassFieldDTOs) {
			Record record = new Record(batchClassFieldDTO.getIdentifier());
			record.addWidget(batchClassFieldListView.name, new Label(batchClassFieldDTO.getName()));
			record.addWidget(batchClassFieldListView.description, new Label(batchClassFieldDTO.getDescription()));
			record.addWidget(batchClassFieldListView.type, new Label(batchClassFieldDTO.getDataType().name()));
			record.addWidget(batchClassFieldListView.fdOrder, new Label(String.valueOf(batchClassFieldDTO.getFieldOrderNumber())));
			record.addWidget(batchClassFieldListView.sampleValue, new Label(String.valueOf(batchClassFieldDTO.getSampleValue())));
			record.addWidget(batchClassFieldListView.validationPattern, new Label(batchClassFieldDTO.getValidationPattern()));
			recordList.add(record);
		}
		return recordList;
	}

	public void createDocumentTypeList(Collection<DocumentTypeDTO> documentTypeDTOs) {
		List<Record> recordList = setDocumentTypeList(documentTypeDTOs);
		documentTypeListPresenter = new DocumentTypeListPresenter(presenter.getController(), docTypeListView);
		documentTypeListPresenter.setDocumentTypeDTOList(documentTypeDTOs);
		docTypeListView.listView.initTable(recordList.size(), documentTypeListPresenter, recordList, true, false);
	}

	public List<Record> setDocumentTypeList(Collection<DocumentTypeDTO> documentTypeDTOs) {

		List<Record> recordList = new LinkedList<Record>();
		for (final DocumentTypeDTO documentTypeDTO : documentTypeDTOs) {
			if (!documentTypeDTO.getName().equalsIgnoreCase(AdminConstants.DOCUMENT_TYPE_UNKNOWN)) {
				CheckBox isHidden = new CheckBox();
				isHidden.setValue(documentTypeDTO.isHidden());
				isHidden.setEnabled(false);
				Record record = new Record(documentTypeDTO.getIdentifier());
				record.addWidget(docTypeListView.name, new Label(documentTypeDTO.getName()));
				record.addWidget(docTypeListView.description, new Label(documentTypeDTO.getDescription()));
				record.addWidget(docTypeListView.isHidden, isHidden);
				recordList.add(record);
			}
		}
		return recordList;
	}

	public BatchClassDetailView getBatchClassDetailView() {
		return batchClassDetailView;
	}

	public EditBatchClassView getEditBatchClassView() {
		return editBatchClassView;
	}

	public ListView getModuleListView() {
		return moduleListView.listView;
	}

	public ListView getDocTypeListView() {
		return docTypeListView.listView;
	}

	public VerticalPanel getBatchClassViewVerticalPanel() {
		return batchClassViewVerticalPanel;
	}

	public Button getEditBatchPropertiesButton() {
		return editBatchPropertiesButton;
	}

	public Button getAddDocumentButton() {
		return addDocumentButton;
	}

	public Button getAddEmailButton() {
		return addEmailButton;
	}

	public VerticalPanel getBatchClassConfigVerticalPanel() {
		return batchClassConfigVerticalPanel;
	}

	@UiHandler("editBatchPropertiesButton")
	public void onEditBatchPropertiesButtonClick(ClickEvent clickEvent) {
		presenter.onEditBatchPropertiesButtonClicked();
	}

	@UiHandler("addDocumentButton")
	public void onAddDocumentButtonClick(ClickEvent clickEvent) {
		presenter.onAddDocumentButtonClicked();
	}

	@UiHandler("editDocumentButton")
	public void onEditDocumentButtonClicked(ClickEvent clickEvent) {
		String rowIndex = docTypeListView.listView.getSelectedRowIndex();
		int rowCount = docTypeListView.listView.getTableRecordCount();
		if (rowIndex == null || rowIndex.isEmpty()) {
			if (rowCount == 0) {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NO_RECORD_TO_EDIT), LocaleDictionary.get().getMessageValue(
						BatchClassManagementConstants.EDIT_DOCUMENT_TITLE));
			} else {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NONE_SELECTED_WARNING), LocaleDictionary.get().getMessageValue(
						BatchClassManagementConstants.EDIT_DOCUMENT_TITLE));
			}
			return;
		}
		presenter.onEditDocumentButtonClicked(rowIndex);
	}

	@UiHandler("copyDocumentButton")
	public void onCopyDocumentButtonClicked(ClickEvent clickEvent) {
		String rowIndex = docTypeListView.listView.getSelectedRowIndex();
		int rowCount = docTypeListView.listView.getTableRecordCount();
		if (rowIndex == null || rowIndex.isEmpty()) {
			if (rowCount == 0) {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NO_RECORD_TO_COPY), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.COPY_DOCUMENT_TITLE));
			} else {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NONE_SELECTED_WARNING), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.COPY_DOCUMENT_TITLE));
			}
			return;
		}
		final DialogBox dialogBox = new DialogBox();
		final CopyDocumentView copyDocumentView = new CopyDocumentView();
		CopyDocumentTypePresenter copyDocumentTypePresenter = new CopyDocumentTypePresenter(presenter.getController(),
				copyDocumentView);
		copyDocumentTypePresenter.setDocumentTypeDTO(presenter.getController().getDocumentByIdentifier(rowIndex));
		copyDocumentView.setDialogBox(dialogBox);
		copyDocumentTypePresenter.bind();
		copyDocumentTypePresenter.showDocumentCopyView();
		copyDocumentView.getSaveButton().setFocus(true);
	}

	@UiHandler("deleteDocumentButton")
	public void onDeleteDocumentButtonClicked(ClickEvent clickEvent) {
		final String rowIndex = docTypeListView.listView.getSelectedRowIndex();
		int rowCount = docTypeListView.listView.getTableRecordCount();
		if (rowIndex == null || rowIndex.isEmpty()) {
			if (rowCount == 0) {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NO_RECORD_TO_DELETE), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.DELETE_DOCUMENT_TITLE));
			} else {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NONE_SELECTED_WARNING), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.DELETE_DOCUMENT_TITLE));
			}
			return;
		}
		final ConfirmationDialog confirmationDialog = new ConfirmationDialog();
		confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(
				BatchClassManagementMessages.DELETE_DOCUMENT_TYPE_CONFORMATION));
		confirmationDialog
				.setDialogTitle(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.DELETE_DOCUMENT_TITLE));
		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				confirmationDialog.hide();
				presenter.onDeleteDocumentButtonClicked(rowIndex);
			}

			@Override
			public void onCancelClick() {
				confirmationDialog.hide();
			}
		});
		confirmationDialog.center();
		confirmationDialog.show();
		confirmationDialog.okButton.setFocus(true);
	}

	@UiHandler("editModuleButton")
	public void onEditModuleButtonClicked(ClickEvent clickEvent) {
		String identifier = moduleListView.listView.getSelectedRowIndex();
		if (identifier == null || identifier.isEmpty()) {
			ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.NONE_SELECTED_WARNING), LocaleDictionary.get().getConstantValue(
					BatchClassManagementConstants.EDIT_MODULE_TITLE));
			return;
		}
		BatchClassModuleDTO batchClassModuleDTO = presenter.getController().getBatchClass().getModuleByIdentifier(identifier);
		/*
		 * if (batchClassModuleDTO.getModule().getName().contains( AdminConstants.REVIEW_DOCUMENT)) {
		 * ConfirmationDialogUtil.showConfirmationDialog (LocaleDictionary.get().getMessageValue(
		 * BatchClassManagementMessages.NOT_EDITABLE_WARNING), LocaleDictionary.get().getConstantValue(
		 * BatchClassManagementConstants.EDIT_MODULE_TITLE)); return; }
		 */
		presenter.getController().getMainPresenter().showModuleView(batchClassModuleDTO);
	}

	@UiHandler("addEmailButton")
	public void onAddEmailButtonClicked(ClickEvent clickEvent) {
		presenter.onAddEmailButtonClicked();
	}

	@UiHandler("editEmailButton")
	public void onEditEmailButtonClicked(ClickEvent clickEvent) {
		String rowIndex = emailListView.listView.getSelectedRowIndex();
		int rowCount = emailListView.listView.getTableRecordCount();
		if (rowIndex == null || rowIndex.isEmpty()) {
			if (rowCount == 0) {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NO_RECORD_TO_EDIT), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.EDIT_EMAIL_CONFIGURATION_TITLE));
			} else {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NONE_SELECTED_WARNING), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.EDIT_EMAIL_CONFIGURATION_TITLE));
			}
			return;
		}
		presenter.onEditEmailButtonClicked(rowIndex);
	}

	@UiHandler("deleteEmailButton")
	public void onDeleteEmailButtonClicked(ClickEvent clickEvent) {
		final String rowIndex = emailListView.listView.getSelectedRowIndex();
		int rowCount = emailListView.listView.getTableRecordCount();
		if (rowIndex == null || rowIndex.isEmpty()) {
			if (rowCount == 0) {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NO_RECORD_TO_DELETE), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.DELETE_EMAIL_CONFIGURATION_TITLE));
			} else {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NONE_SELECTED_WARNING), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.DELETE_EMAIL_CONFIGURATION_TITLE));
			}
			return;
		}
		final ConfirmationDialog confirmationDialog = new ConfirmationDialog();
		confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(
				BatchClassManagementMessages.DELETE_EMAIL_CONFIGURATION_CONFORMATION));
		confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(
				BatchClassManagementConstants.DELETE_EMAIL_CONFIGURATION_TITLE));
		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				confirmationDialog.hide();
				presenter.onDeleteEmailButtonClicked(rowIndex);
			}

			@Override
			public void onCancelClick() {
				confirmationDialog.hide();
			}
		});
		confirmationDialog.center();
		confirmationDialog.show();
		confirmationDialog.okButton.setFocus(true);
	}

	@UiHandler("addBatchClassFieldButton")
	public void onAddBatchClassFieldButtonClicked(ClickEvent clickEvent) {
		presenter.onAddBatchClassFieldButtonClicked();
	}

	@UiHandler("editBatchClassFieldButton")
	public void onEditBatchClassFieldButtonClicked(ClickEvent clickEvent) {
		String rowIndex = batchClassFieldListView.listView.getSelectedRowIndex();
		int rowCount = batchClassFieldListView.listView.getTableRecordCount();
		if (rowIndex == null || rowIndex.isEmpty()) {
			if (rowCount == 0) {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NO_RECORD_TO_EDIT), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.EDIT_BATCH_CLASS_FIELD_TITLE));
			} else {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NONE_SELECTED_WARNING), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.EDIT_BATCH_CLASS_FIELD_TITLE));
			}
			return;
		}
		presenter.onEditBatchClassFieldButtonClicked(rowIndex);
	}

	@UiHandler("deleteBatchClassFieldButton")
	public void onDeleteBatchClassFieldButtonClicked(ClickEvent clickEvent) {
		final String rowIndex = batchClassFieldListView.listView.getSelectedRowIndex();
		int rowCount = batchClassFieldListView.listView.getTableRecordCount();
		if (rowIndex == null || rowIndex.isEmpty()) {
			if (rowCount == 0) {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NO_RECORD_TO_DELETE), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.DELETE_BATCH_CLASS_FIELD_TITLE));
			} else {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NONE_SELECTED_WARNING), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.DELETE_BATCH_CLASS_FIELD_TITLE));
			}
			return;
		}
		final ConfirmationDialog confirmationDialog = new ConfirmationDialog();
		confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(
				BatchClassManagementMessages.DELETE_BATCH_CLASS_FIELD_CONFORMATION));
		confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(
				BatchClassManagementConstants.DELETE_BATCH_CLASS_FIELD_TITLE));
		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				confirmationDialog.hide();
				presenter.onDeleteBatchClassFieldButtonClicked(rowIndex);
			}

			@Override
			public void onCancelClick() {
				confirmationDialog.hide();
			}
		});
		confirmationDialog.center();
		confirmationDialog.show();
		confirmationDialog.okButton.setFocus(true);
	}

	public LayoutPanel getViewEditLayoutPanel() {
		return viewEditLayoutPanel;
	}

	public DockLayoutPanel getDocTypeListPanel() {
		return docTypeListPanel;
	}

	public DockLayoutPanel getModuleTypeListPanel() {
		return moduleTypeListPanel;
	}

	public DockLayoutPanel getEmailListPanel() {
		return emailListPanel;
	}

	public DockLayoutPanel getBatchClassFieldListPanel() {
		return batchClassFieldListPanel;
	}

	public void setBatchClassFieldListPanel(DockLayoutPanel batchClassFieldListPanel) {
		this.batchClassFieldListPanel = batchClassFieldListPanel;
	}

	public LayoutPanel getBatchClassFieldLayoutPanel() {
		return batchClassFieldLayoutPanel;
	}

	public void setBatchClassFieldLayoutPanel(LayoutPanel batchClassFieldLayoutPanel) {
		this.batchClassFieldLayoutPanel = batchClassFieldLayoutPanel;
	}

	public BatchClassFieldListView getBatchClassFieldListView() {
		return batchClassFieldListView;
	}
	/*
	 * public BatchClassFieldListPresenter getBatchClassFieldListPresenter() { return batchClassFieldListPresenter; }
	 */
}
