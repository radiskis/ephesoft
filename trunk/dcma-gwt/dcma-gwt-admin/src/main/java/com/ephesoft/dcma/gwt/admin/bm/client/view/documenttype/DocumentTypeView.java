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

package com.ephesoft.dcma.gwt.admin.bm.client.view.documenttype;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.documenttype.DocumentTypeViewPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.fieldtype.FieldTypeListView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.functionkey.FunctionKeyListView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.tableinfo.TableInfoListView;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView;
import com.ephesoft.dcma.gwt.core.client.ui.table.Record;
import com.ephesoft.dcma.gwt.core.client.ui.table.TableHeader.HeaderColumn;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.FieldTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.FunctionKeyDTO;
import com.ephesoft.dcma.gwt.core.shared.PageTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.TableInfoDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DocumentTypeView extends View<DocumentTypeViewPresenter> {

	interface Binder extends UiBinder<DockLayoutPanel, DocumentTypeView> {
	}

	@UiField
	protected DocumentTypeDetailView documentTypeDetailView;

	@UiField
	protected EditDocumentTypeView editDocumentTypeView;

	@UiField
	protected LayoutPanel pageTypeListPanel;

	private PageTypeListView pageTypeListView;

	@UiField
	protected LayoutPanel functionKeyListPanel;

	private FunctionKeyListView functionKeyListView;

	@UiField
	protected Button addFunctionKeyButton;

	@UiField
	protected Button editFunctionKeyButton;

	@UiField
	protected Button deleteFunctionKeyButton;

	@UiField
	protected LayoutPanel fieldTypeListPanel;

	@UiField
	protected VerticalPanel documentTypeVerticalPanel;

	@UiField
	protected VerticalPanel documentTypeConfigVerticalPanel;

	private FieldTypeListView fieldTypeListView;

	@UiField
	protected CaptionPanel documentConfigurationCaptionPanel;

	@UiField
	protected Button editDocumentPropertiesButton;

	@UiField
	DockLayoutPanel fieldTypeCompletePanel;

	@UiField
	protected Button addFieldButton;

	@UiField
	protected Button editFieldButton;

	@UiField
	protected Button deleteFieldButton;

	@UiField
	protected HorizontalPanel fieldButtonPanel;

	private TableInfoListView tableInfoListView;

	@UiField
	DockLayoutPanel tableInfoCompletePanel;

	@UiField
	protected Button addTableInfoFieldButton;

	@UiField
	protected Button editTableInfoFieldButton;

	@UiField
	protected Button deleteTableInfoFieldButton;

	@UiField
	protected LayoutPanel tableInfoListPanel;

	@UiField
	protected HorizontalPanel tableInfoButtonPanel;

	@UiField
	protected Button testTableButton;

	private static final Binder BINDER = GWT.create(Binder.class);

	public DocumentTypeView() {
		initWidget(BINDER.createAndBindUi(this));

		pageTypeListView = new PageTypeListView();
		addFieldButton.setText(AdminConstants.ADD_BUTTON);
		documentConfigurationCaptionPanel.setCaptionHTML(AdminConstants.DOCUMENT_CONFIGURATION_HTML);
		pageTypeListPanel.add(pageTypeListView.listView);

		functionKeyListView = new FunctionKeyListView();
		addFunctionKeyButton.setText(AdminConstants.ADD_BUTTON);
		editFunctionKeyButton.setText(AdminConstants.EDIT_BUTTON);
		deleteFunctionKeyButton.setText(AdminConstants.DELETE_BUTTON);

		functionKeyListPanel.add(functionKeyListView.listView);

		fieldTypeListView = new FieldTypeListView();
		fieldTypeListPanel.add(fieldTypeListView.listView);

		editDocumentPropertiesButton.setText(AdminConstants.EDIT_BUTTON);
		editFieldButton.setText(AdminConstants.EDIT_BUTTON);
		deleteFieldButton.setText(AdminConstants.DELETE_BUTTON);
		documentTypeVerticalPanel.add(editDocumentPropertiesButton);

		addFieldButton.setHeight("20px");
		editFieldButton.setHeight("20px");
		deleteFieldButton.setHeight("20px");
		addFunctionKeyButton.setHeight("20px");
		editFunctionKeyButton.setHeight("20px");
		deleteFunctionKeyButton.setHeight("20px");

		fieldButtonPanel.addStyleName("topPadd");

		// Table Info List View Changes
		tableInfoListView = new TableInfoListView();
		tableInfoListPanel.add(tableInfoListView.listView);
		tableInfoButtonPanel.addStyleName("topPadd");

		addTableInfoFieldButton.setText(AdminConstants.ADD_BUTTON);
		addTableInfoFieldButton.setHeight("20px");

		deleteTableInfoFieldButton.setText(AdminConstants.DELETE_BUTTON);
		deleteTableInfoFieldButton.setHeight("20px");

		testTableButton.setText(AdminConstants.TEST_TABLE_BUTTON);
		testTableButton.setHeight("20px");
		testTableButton.addStyleName("margin");

		editTableInfoFieldButton.setText(AdminConstants.EDIT_BUTTON);
		editTableInfoFieldButton.setHeight("20px");
	}

	public void createPageTypeList(Collection<PageTypeDTO> pageTypes) {
		List<Record> recordList = setPluginList(pageTypes);

		pageTypeListView.listView.initTable(recordList.size(), recordList);
	}

	public void createFieldTypeList(Collection<FieldTypeDTO> fieldTypes) {
		List<Record> recordList = setFieldsList(fieldTypes);

		fieldTypeListView.listView.initTable(recordList.size(), recordList, true);
	}

	public void createTableInfoList(Collection<TableInfoDTO> tableInfos) {
		List<Record> recordList = setTableInfoList(tableInfos);
		tableInfoListView.listView.initTable(recordList.size(), recordList, true);
	}

	private List<Record> setFieldsList(Collection<FieldTypeDTO> fields) {

		List<Record> recordList = new LinkedList<Record>();
		for (final FieldTypeDTO fieldTypeDTO : fields) {
			CheckBox isHidden = new CheckBox();
			isHidden.setValue(fieldTypeDTO.isHidden());
			isHidden.setEnabled(false);
			Record record = new Record(fieldTypeDTO.getIdentifier());
			record.addWidget(fieldTypeListView.name, new Label(fieldTypeDTO.getName()));
			record.addWidget(fieldTypeListView.description, new Label(fieldTypeDTO.getDescription()));
			record.addWidget(fieldTypeListView.type, new Label(fieldTypeDTO.getDataType().name()));
			record.addWidget(fieldTypeListView.fdOrder, new Label(fieldTypeDTO.getFieldOrderNumber()));
			record.addWidget(fieldTypeListView.sampleValue, new Label(fieldTypeDTO.getSampleValue()));
			record.addWidget(fieldTypeListView.barcode, new Label(fieldTypeDTO.getBarcodeType()));
			record.addWidget(fieldTypeListView.isHidden, isHidden);
			recordList.add(record);
		}

		return recordList;
	}

	private List<Record> setFunctionKeyList(Collection<FunctionKeyDTO> functionKeyDTOs) {
		List<Record> recordList = new LinkedList<Record>();
		for (final FunctionKeyDTO functionKeyDTO : functionKeyDTOs) {
			Record record = new Record(functionKeyDTO.getIdentifier());
			record.addWidget(functionKeyListView.keyName, new Label(functionKeyDTO.getShortcutKeyName()));
			record.addWidget(functionKeyListView.methodName, new Label(functionKeyDTO.getMethodName()));
			record.addWidget(functionKeyListView.description, new Label(functionKeyDTO.getMethodDescription()));
			recordList.add(record);
		}
		return recordList;
	}

	private List<Record> setTableInfoList(Collection<TableInfoDTO> tableInfos) {

		List<Record> recordList = new LinkedList<Record>();
		for (final TableInfoDTO tableInfoDTO : tableInfos) {
			Record record = new Record(tableInfoDTO.getIdentifier());
			record.addWidget(tableInfoListView.name, new Label(tableInfoDTO.getName()));
			record.addWidget(tableInfoListView.startPattern, new Label(tableInfoDTO.getStartPattern()));
			record.addWidget(tableInfoListView.endPattern, new Label(tableInfoDTO.getEndPattern()));
			recordList.add(record);
		}

		return recordList;
	}

	private List<Record> setPluginList(Collection<PageTypeDTO> pageTypes) {

		if (null == pageTypes) {
			return null;
		}

		List<Record> recordList = new LinkedList<Record>();
		int index = 1;

		for (final PageTypeDTO pageType : pageTypes) {
			Record record = new Record(String.valueOf(index));
			record.addWidget(pageTypeListView.name, new Label(pageType.getName()));
			record.addWidget(pageTypeListView.description, new Label(pageType.getDescription()));

			recordList.add(record);
			index++;
		}
		return recordList;
	}

	public void setPresenter(DocumentTypeViewPresenter presenter) {
		this.presenter = presenter;
	}

	public DocumentTypeDetailView getDocumentTypeDetailView() {
		return documentTypeDetailView;
	}

	public ListView getPageTypeListView() {
		return pageTypeListView.listView;
	}

	public ListView getFieldTypeView() {
		return fieldTypeListView.getFieldsListView();
	}

	public ListView getFunctionKeyListView() {
		return functionKeyListView.listView;
	}

	public EditDocumentTypeView getEditDocumentTypeView() {
		return editDocumentTypeView;
	}

	public Button getEditDocumentPropertiesButtonButton() {
		return editDocumentPropertiesButton;
	}

	public class PageTypeListView {

		HeaderColumn blank = new HeaderColumn(1, "", 5);
		HeaderColumn name = new HeaderColumn(1, "Name", 30);
		HeaderColumn description = new HeaderColumn(2, "Description", 65);

		ListView listView = new ListView();

		public PageTypeListView() {
			listView.addHeaderColumns(blank, name, description);
		}
	}

	public VerticalPanel getDocumentTypeConfigVerticalPanel() {
		return documentTypeConfigVerticalPanel;
	}

	public VerticalPanel getDocumentTypeVerticalPanel() {
		return documentTypeVerticalPanel;
	}

	public Button getAddFieldButtonButton() {
		return addFieldButton;
	}

	@UiHandler("addFieldButton")
	public void onAddFieldButtonClick(ClickEvent clickEvent) {
		presenter.onAddFieldButtonClicked();
	}

	@UiHandler("addFunctionKeyButton")
	public void onAddFunctionKeyButtonClick(ClickEvent clickEvent) {
		presenter.onAddFunctionKeyButtonClicked();
	}

	@UiHandler("editFunctionKeyButton")
	public void onEditFunctionKeyButtonClick(ClickEvent clickEvent) {
		String identifier = functionKeyListView.listView.getSelectedRowIndex();
		int rowCount = functionKeyListView.listView.getTableRecordCount();
		if (identifier == null || identifier.isEmpty()) {
			if (rowCount == 0) {
				final ConfirmationDialog confirmationDialog = new ConfirmationDialog(true);
				confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.NO_RECORD_TO_EDIT));
				confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.EDIT_FUNCTION_KEY_TITLE));
				confirmationDialog.addDialogListener(new DialogListener() {

					@Override
					public void onOkClick() {
						confirmationDialog.hide();
					}

					@Override
					public void onCancelClick() {
						confirmationDialog.hide();
					}

				});
				confirmationDialog.center();
				confirmationDialog.show();
				confirmationDialog.okButton.setFocus(true);
			} else {
				final ConfirmationDialog confirmationDialog = new ConfirmationDialog(true);
				confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NONE_SELECTED_WARNING));
				confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.EDIT_FUNCTION_KEY_TITLE));
				confirmationDialog.addDialogListener(new DialogListener() {

					@Override
					public void onOkClick() {
						confirmationDialog.hide();
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
			return;
		}

		presenter.onEditFunctionKeyButtonClicked(identifier);
	}

	@UiHandler("deleteFunctionKeyButton")
	public void onDeleteFunctionKeyButtonClick(ClickEvent clickEvent) {
		final String identifier = functionKeyListView.listView.getSelectedRowIndex();
		int rowCount = functionKeyListView.listView.getTableRecordCount();
		if (identifier == null || identifier.isEmpty()) {
			if (rowCount == 0) {
				final ConfirmationDialog confirmationDialog = new ConfirmationDialog(true);
				confirmationDialog
						.setMessage(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.NO_RECORD_TO_DELETE));
				confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.DELETE_FUNCTION_KEY_TITLE));
				confirmationDialog.addDialogListener(new DialogListener() {

					@Override
					public void onOkClick() {
						confirmationDialog.hide();
					}

					@Override
					public void onCancelClick() {
						confirmationDialog.hide();
					}

				});
				confirmationDialog.center();
				confirmationDialog.show();
				confirmationDialog.okButton.setFocus(true);

			} else {
				final ConfirmationDialog confirmationDialog = new ConfirmationDialog(true);
				confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NONE_SELECTED_WARNING));
				confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.DELETE_FUNCTION_KEY_TITLE));
				confirmationDialog.addDialogListener(new DialogListener() {

					@Override
					public void onOkClick() {
						confirmationDialog.hide();
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
			return;
		}
		final ConfirmationDialog confirmationDialog = new ConfirmationDialog();
		confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(
				BatchClassManagementMessages.DELETE_FUNCTION_KEY_CONFORMATION));
		confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(
				BatchClassManagementConstants.DELETE_FUNCTION_KEY_TITLE));
		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				confirmationDialog.hide();
				presenter.onDeleteFunctionKeyButtonClicked(identifier);
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

	@UiHandler("editDocumentPropertiesButton")
	public void onEditDocumentPropertiesButtonClick(ClickEvent clickEvent) {
		presenter.onEditDocumentPropertiesButtonClicked();
	}

	@UiHandler("editFieldButton")
	public void onEditFieldButtonClicked(ClickEvent clickEvent) {
		String identifier = fieldTypeListView.listView.getSelectedRowIndex();
		int rowCount = fieldTypeListView.listView.getTableRecordCount();
		if (identifier == null || identifier.isEmpty()) {
			if (rowCount == 0) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NO_RECORD_TO_EDIT));
			} else {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NONE_SELECTED_WARNING));
			}
			return;
		}
		presenter.onEditFieldButtonClicked(identifier);

	}

	@UiHandler("deleteFieldButton")
	public void onDeleteFieldButtonClicked(ClickEvent clickEvent) {
		final String identifier = fieldTypeListView.listView.getSelectedRowIndex();
		int rowCount = fieldTypeListView.listView.getTableRecordCount();
		if (identifier == null || identifier.isEmpty()) {
			if (rowCount == 0) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NO_RECORD_TO_DELETE));
			} else {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NONE_SELECTED_WARNING));
			}
		}
		final ConfirmationDialog confirmationDialog = new ConfirmationDialog();
		confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(
				BatchClassManagementMessages.DELETE_FIELD_TYPE_CONFORMATION));
		confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.DELETE_FIELD_TITLE));
		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				confirmationDialog.hide();
				presenter.onDeleteFieldButtonClicked(identifier);
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

	@UiHandler("addTableInfoFieldButton")
	public void onAddTableInfoFieldButtonClick(ClickEvent clickEvent) {
		presenter.onAddTableInfoFieldButtonClicked();
	}

	@UiHandler("editTableInfoFieldButton")
	public void onEditTableInfoFieldButtonClick(ClickEvent clickEvent) {
		String identifier = tableInfoListView.listView.getSelectedRowIndex();
		int rowCount = tableInfoListView.listView.getTableRecordCount();
		if (identifier == null || identifier.isEmpty()) {
			if (rowCount == 0) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NO_RECORD_TO_EDIT));
			} else {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NONE_SELECTED_WARNING));
			}
			return;
		}
		presenter.onEditTableInfoFieldButtonClicked(identifier);
	}

	@UiHandler("deleteTableInfoFieldButton")
	public void onDeleteTableInfoFieldButtonClicked(ClickEvent clickEvent) {
		final String identifier = tableInfoListView.listView.getSelectedRowIndex();
		int rowCount = tableInfoListView.listView.getTableRecordCount();
		if (identifier == null || identifier.isEmpty()) {
			if (rowCount == 0) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NO_RECORD_TO_DELETE));
			} else {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NONE_SELECTED_WARNING));
			}
			return;
		}
		final ConfirmationDialog confirmationDialog = new ConfirmationDialog();
		confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(
				BatchClassManagementMessages.DELETE_TABLE_INFO_CONFORMATION));
		confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.DELETE_TABLE_TITLE));
		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				confirmationDialog.hide();
				presenter.onDeleteTableInfoButtonClicked(identifier);
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

	@UiHandler("testTableButton")
	public void onTestTableButtonClicked(ClickEvent clickEvent) {
		String identifier = tableInfoListView.listView.getSelectedRowIndex();
		int rowCount = tableInfoListView.listView.getTableRecordCount();
		if (identifier == null || identifier.isEmpty()) {
			if (rowCount == 0) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NO_TABLE_TO_TEST));
			} else {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NONE_SELECTED_WARNING));
			}
			return;
		}
		presenter.onTestTableButtonClicked(identifier);
	}

	public void createDocumentFunctionKeyList(Collection<FunctionKeyDTO> functionKeyInfo) {
		List<Record> recordList = setFunctionKeyList(functionKeyInfo);
		functionKeyListView.listView.initTable(recordList.size(), recordList, true);
	}
}
