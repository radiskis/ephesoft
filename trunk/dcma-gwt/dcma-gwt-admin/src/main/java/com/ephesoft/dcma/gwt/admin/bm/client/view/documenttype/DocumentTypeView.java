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

package com.ephesoft.dcma.gwt.admin.bm.client.view.documenttype;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.documenttype.DocumentTypeViewPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.fieldtype.FieldTypeListPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.functionkey.FunctionKeyListPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.tableinfo.TableInfoListPresenter;
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

/**
 * This class handles document type view seen to user on editing batch class.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class DocumentTypeView extends View<DocumentTypeViewPresenter> {

	/**
	 * MARGIN String.
	 */
	private static final String MARGIN = "margin";

	/**
	 * TOP_PADD String.
	 */
	private static final String TOP_PADD = "topPadd";

	/**
	 * HEIGHT String.
	 */
	private static final String HEIGHT = "20px";

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<DockLayoutPanel, DocumentTypeView> {
	}

	/**
	 * documentTypeDetailView DocumentTypeDetailView.
	 */
	@UiField
	protected DocumentTypeDetailView documentTypeDetailView;

	/**
	 * editDocumentTypeView EditDocumentTypeView.
	 */
	@UiField
	protected EditDocumentTypeView editDocumentTypeView;

	/**
	 * pageTypeListPanel LayoutPanel.
	 */
	@UiField
	protected LayoutPanel pageTypeListPanel;

	/**
	 * pageTypeListView PageTypeListView.
	 */
	private final PageTypeListView pageTypeListView;

	/**
	 * functionKeyListPanel LayoutPanel.
	 */
	@UiField
	protected LayoutPanel functionKeyListPanel;

	/**
	 * functionKeyListView FunctionKeyListView.
	 */
	private final FunctionKeyListView functionKeyListView;

	/**
	 * addFunctionKeyButton Button.
	 */
	@UiField
	protected Button addFunctionKeyButton;

	/**
	 * editFunctionKeyButton Button.
	 */
	@UiField
	protected Button editFunctionKeyButton;

	/**
	 * deleteFunctionKeyButton Button.
	 */
	@UiField
	protected Button deleteFunctionKeyButton;

	/**
	 * fieldTypeListPanel LayoutPanel.
	 */
	@UiField
	protected LayoutPanel fieldTypeListPanel;

	/**
	 * documentTypeVerticalPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel documentTypeVerticalPanel;

	/**
	 * documentTypeConfigVerticalPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel documentTypeConfigVerticalPanel;

	/**
	 * fieldTypeListView FieldTypeListView.
	 */
	private final FieldTypeListView fieldTypeListView;

	/**
	 * documentConfigurationCaptionPanel CaptionPanel.
	 */
	@UiField
	protected CaptionPanel documentConfigurationCaptionPanel;

	/**
	 * fieldTypeCompletePanel DockLayoutPanel.
	 */
	@UiField
	protected DockLayoutPanel fieldTypeCompletePanel;

	/**
	 * addFieldButton Button.
	 */
	@UiField
	protected Button addFieldButton;

	/**
	 * editFieldButton Button.
	 */
	@UiField
	protected Button editFieldButton;

	/**
	 * deleteFieldButton Button.
	 */
	@UiField
	protected Button deleteFieldButton;

	/**
	 * fieldButtonPanel HorizontalPanel.
	 */
	@UiField
	protected HorizontalPanel fieldButtonPanel;

	/**
	 * tableInfoListView TableInfoListView.
	 */
	private final TableInfoListView tableInfoListView;

	/**
	 * tableInfoCompletePanel DockLayoutPanel.
	 */
	@UiField
	protected DockLayoutPanel tableInfoCompletePanel;

	/**
	 * addTableInfoFieldButton Button.
	 */
	@UiField
	protected Button addTableInfoFieldButton;

	/**
	 * editTableInfoFieldButton Button.
	 */
	@UiField
	protected Button editTableInfoFieldButton;

	/**
	 * deleteTableInfoFieldButton Button.
	 */
	@UiField
	protected Button deleteTableInfoFieldButton;

	/**
	 * tableInfoListPanel LayoutPanel.
	 */
	@UiField
	protected LayoutPanel tableInfoListPanel;

	/**
	 * tableInfoButtonPanel HorizontalPanel.
	 */
	@UiField
	protected HorizontalPanel tableInfoButtonPanel;

	/**
	 * testTableButton Button.
	 */
	@UiField
	protected Button testTableButton;

	/**
	 * fieldTypeListPresenter FieldTypeListPresenter.
	 */
	private FieldTypeListPresenter fieldTypeListPresenter;

	/**
	 * tableInfoListPresenter TableInfoListPresenter.
	 */
	private TableInfoListPresenter tableInfoListPresenter;

	/**
	 * functionKeyListPresenter FunctionKeyListPresenter.
	 */
	private FunctionKeyListPresenter functionKeyListPresenter;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Constructor.
	 */
	public DocumentTypeView() {
		super();
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

		editFieldButton.setText(AdminConstants.EDIT_BUTTON);
		deleteFieldButton.setText(AdminConstants.DELETE_BUTTON);

		addFieldButton.setHeight(HEIGHT);
		editFieldButton.setHeight(HEIGHT);
		deleteFieldButton.setHeight(HEIGHT);
		addFunctionKeyButton.setHeight(HEIGHT);
		editFunctionKeyButton.setHeight(HEIGHT);
		deleteFunctionKeyButton.setHeight(HEIGHT);

		fieldButtonPanel.addStyleName(TOP_PADD);

		// Table Info List View Changes
		tableInfoListView = new TableInfoListView();
		tableInfoListPanel.add(tableInfoListView.listView);
		tableInfoButtonPanel.addStyleName(TOP_PADD);

		addTableInfoFieldButton.setText(AdminConstants.ADD_BUTTON);
		addTableInfoFieldButton.setHeight(HEIGHT);

		deleteTableInfoFieldButton.setText(AdminConstants.DELETE_BUTTON);
		deleteTableInfoFieldButton.setHeight(HEIGHT);

		testTableButton.setText(AdminConstants.TEST_TABLE_BUTTON);
		testTableButton.setHeight(HEIGHT);
		testTableButton.addStyleName(MARGIN);

		editTableInfoFieldButton.setText(AdminConstants.EDIT_BUTTON);
		editTableInfoFieldButton.setHeight(HEIGHT);
	}

	/**
	 * To create Page Type List.
	 * 
	 * @param pageTypes Collection<PageTypeDTO>
	 */
	public void createPageTypeList(Collection<PageTypeDTO> pageTypes) {
		List<Record> recordList = setPluginList(pageTypes);

		pageTypeListView.listView.initTable(recordList.size(), recordList);
	}

	/**
	 * To create Field Type List.
	 * 
	 * @param fieldTypes Collection<FieldTypeDTO>
	 */
	public void createFieldTypeList(Collection<FieldTypeDTO> fieldTypes) {
		List<FieldTypeDTO> fieldTypeList = new ArrayList<FieldTypeDTO>(fieldTypes);
		presenter.getController().getMainPresenter().sortDocumentLevelFieldsList(fieldTypeList);
		List<Record> recordList = setFieldsList(fieldTypeList);
		fieldTypeListPresenter = new FieldTypeListPresenter(presenter.getController(), fieldTypeListView);
		fieldTypeListPresenter.setFieldTypeDTOList(fieldTypeList);
		fieldTypeListView.listView.initTable(recordList.size(), null, null, recordList, true, false, fieldTypeListPresenter,
				fieldTypeListPresenter, true);
	}

	/**
	 * To create Table Info List.
	 * 
	 * @param tableInfos Collection<TableInfoDTO>
	 */
	public void createTableInfoList(Collection<TableInfoDTO> tableInfos) {
		List<Record> recordList = setTableInfoList(tableInfos);
		tableInfoListPresenter = new TableInfoListPresenter(presenter.getController(), tableInfoListView);
		tableInfoListView.listView.initTable(recordList.size(), null, null, recordList, true, false, tableInfoListPresenter, null,
				false);
	}

	/**
	 * To set Fields List.
	 * 
	 * @param fields Collection<FieldTypeDTO>
	 * @return List<Record>
	 */
	public List<Record> setFieldsList(Collection<FieldTypeDTO> fields) {

		List<Record> recordList = new LinkedList<Record>();
		for (final FieldTypeDTO fieldTypeDTO : fields) {
			CheckBox isHidden = new CheckBox();
			CheckBox isMultiLine = new CheckBox();
			CheckBox isReadOnly = new CheckBox();
			isHidden.setValue(fieldTypeDTO.isHidden());
			isHidden.setEnabled(false);
			isMultiLine.setValue(fieldTypeDTO.isMultiLine());
			isMultiLine.setEnabled(false);
			isReadOnly.setValue(fieldTypeDTO.getIsReadOnly());
			isReadOnly.setEnabled(false);
			Record record = new Record(fieldTypeDTO.getIdentifier());
			record.addWidget(fieldTypeListView.name, new Label(fieldTypeDTO.getName()));
			record.addWidget(fieldTypeListView.description, new Label(fieldTypeDTO.getDescription()));
			record.addWidget(fieldTypeListView.type, new Label(fieldTypeDTO.getDataType().name()));
			record.addWidget(fieldTypeListView.fdOrder, new Label(fieldTypeDTO.getFieldOrderNumber()));
			record.addWidget(fieldTypeListView.sampleValue, new Label(fieldTypeDTO.getSampleValue()));
			record.addWidget(fieldTypeListView.barcode, new Label(fieldTypeDTO.getBarcodeType()));
			record.addWidget(fieldTypeListView.isHidden, isHidden);
			record.addWidget(fieldTypeListView.isMultiLine, isMultiLine);
			record.addWidget(fieldTypeListView.isReadOnly, isReadOnly);
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
			record.addWidget(tableInfoListView.tableExtractionAPI, new Label(tableInfoDTO.getTableExtractionAPI()));
			record.addWidget(tableInfoListView.widthOfMultiline, new Label(tableInfoDTO.getWidthOfMultiline()));
			recordList.add(record);
		}

		return recordList;
	}

	private List<Record> setPluginList(Collection<PageTypeDTO> pageTypes) {

		List<Record> recordList = new LinkedList<Record>();
		if (null == pageTypes) {
			recordList = null;
		} else {
			int index = 1;
			for (final PageTypeDTO pageType : pageTypes) {
				Record record = new Record(String.valueOf(index));
				record.addWidget(pageTypeListView.name, new Label(pageType.getName()));
				record.addWidget(pageTypeListView.description, new Label(pageType.getDescription()));

				recordList.add(record);
				index++;
			}
		}
		return recordList;
	}

	/**
	 * To set Presenter.
	 * 
	 * @param presenter DocumentTypeViewPresenter
	 */
	public void setPresenter(DocumentTypeViewPresenter presenter) {
		this.presenter = presenter;
	}

	/**
	 * To get Document Type Detail View.
	 * 
	 * @return DocumentTypeDetailView
	 */
	public DocumentTypeDetailView getDocumentTypeDetailView() {
		return documentTypeDetailView;
	}

	/**
	 * To get Page Type List View.
	 * 
	 * @return ListView
	 */
	public ListView getPageTypeListView() {
		return pageTypeListView.listView;
	}

	/**
	 * To get Field Type View.
	 * 
	 * @return ListView
	 */
	public ListView getFieldTypeView() {
		return fieldTypeListView.getFieldsListView();
	}

	/**
	 * To get Function Key List View.
	 * 
	 * @return ListView
	 */
	public ListView getFunctionKeyListView() {
		return functionKeyListView.listView;
	}

	/**
	 * To get Edit Document Type View.
	 * 
	 * @return EditDocumentTypeView
	 */
	public EditDocumentTypeView getEditDocumentTypeView() {
		return editDocumentTypeView;
	}

	/**
	 * This class provides functionality to show page type list view.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 */
	public class PageTypeListView {

		/**
		 * blank HeaderColumn.
		 */
		private final HeaderColumn blank = new HeaderColumn(1, "", 5);

		/**
		 * name HeaderColumn.
		 */
		private final HeaderColumn name = new HeaderColumn(1, "Name", 30);

		/**
		 * description HeaderColumn.
		 */
		private final HeaderColumn description = new HeaderColumn(2, "Description", 65);

		/**
		 * listView ListView.
		 */
		private final ListView listView = new ListView();

		/**
		 * Constructor.
		 */
		public PageTypeListView() {
			listView.addHeaderColumns(blank, name, description);
		}
	}

	/**
	 * To get Document Type Configuration Vertical Panel.
	 * 
	 * @return VerticalPanel
	 */
	public VerticalPanel getDocumentTypeConfigVerticalPanel() {
		return documentTypeConfigVerticalPanel;
	}

	/**
	 * To get Document Type Vertical Panel.
	 * 
	 * @return VerticalPanel
	 */
	public VerticalPanel getDocumentTypeVerticalPanel() {
		return documentTypeVerticalPanel;
	}

	/**
	 * To get Add Field Button.
	 * 
	 * @return Button
	 */
	public Button getAddFieldButtonButton() {
		return addFieldButton;
	}

	/**
	 * To perform operations on add Field Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("addFieldButton")
	public void onAddFieldButtonClick(ClickEvent clickEvent) {
		presenter.onAddFieldButtonClicked();
	}

	/**
	 * To perform operations on add function key Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("addFunctionKeyButton")
	public void onAddFunctionKeyButtonClick(ClickEvent clickEvent) {
		presenter.onAddFunctionKeyButtonClicked();
	}

	/**
	 * To perform operations on edit function key Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("editFunctionKeyButton")
	public void onEditFunctionKeyButtonClick(ClickEvent clickEvent) {
		functionKeyListPresenter.onEditButtonClicked();
	}

	/**
	 * To perform operations on delete function key Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("deleteFunctionKeyButton")
	public void onDeleteFunctionKeyButtonClick(ClickEvent clickEvent) {
		final String identifier = functionKeyListView.listView.getSelectedRowIndex();
		int rowCount = functionKeyListView.listView.getTableRecordCount();
		if (identifier == null || identifier.isEmpty()) {
			if (rowCount == 0) {
				final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get()
						.getMessageValue(BatchClassManagementMessages.NO_RECORD_TO_DELETE), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.DELETE_FUNCTION_KEY_TITLE), Boolean.TRUE);

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

			} else {
				final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get()
						.getMessageValue(BatchClassManagementMessages.NONE_SELECTED_WARNING), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.DELETE_FUNCTION_KEY_TITLE), Boolean.TRUE);

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

			}
			return;
		}
		final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get()
				.getMessageValue(BatchClassManagementMessages.DELETE_FUNCTION_KEY_CONFORMATION), LocaleDictionary.get()
				.getConstantValue(BatchClassManagementConstants.DELETE_FUNCTION_KEY_TITLE), false);

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

	}

	/**
	 * To perform operations on edit document properties button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */

	public void onEditDocumentPropertiesButtonClick(ClickEvent clickEvent) {
		presenter.onEditDocumentPropertiesButtonClicked();
	}

	/**
	 * To perform operations on edit field button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("editFieldButton")
	public void onEditFieldButtonClicked(ClickEvent clickEvent) {
		fieldTypeListPresenter.onEditButtonClicked();
	}

	/**
	 * To perform operations on delete field button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
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
		final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get()
				.getMessageValue(BatchClassManagementMessages.DELETE_FIELD_TYPE_CONFORMATION), LocaleDictionary.get()
				.getConstantValue(BatchClassManagementConstants.DELETE_FIELD_TITLE), Boolean.FALSE);

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

	}

	/**
	 * To perform operations on add table info field button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("addTableInfoFieldButton")
	public void onAddTableInfoFieldButtonClick(ClickEvent clickEvent) {
		presenter.onAddTableInfoFieldButtonClicked();
	}

	/**
	 * To perform operations on edit table info field button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("editTableInfoFieldButton")
	public void onEditTableInfoFieldButtonClick(ClickEvent clickEvent) {
		tableInfoListPresenter.onEditButtonClicked();

	}

	/**
	 * To perform operations on delete table info field button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
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
		final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get()
				.getMessageValue(BatchClassManagementMessages.DELETE_TABLE_INFO_CONFORMATION), LocaleDictionary.get()
				.getConstantValue(BatchClassManagementConstants.DELETE_TABLE_TITLE), Boolean.FALSE);

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

	}

	/**
	 * To perform operations on test table button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
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

	/**
	 * To create Document Function Key List.
	 * 
	 * @param functionKeyInfo Collection<FunctionKeyDTO>
	 */
	public void createDocumentFunctionKeyList(Collection<FunctionKeyDTO> functionKeyInfo) {
		List<Record> recordList = setFunctionKeyList(functionKeyInfo);
		functionKeyListPresenter = new FunctionKeyListPresenter(presenter.getController(), functionKeyListView);
		functionKeyListView.listView.initTable(recordList.size(), null, null, recordList, true, false, functionKeyListPresenter, null,
				true);
	}

}
