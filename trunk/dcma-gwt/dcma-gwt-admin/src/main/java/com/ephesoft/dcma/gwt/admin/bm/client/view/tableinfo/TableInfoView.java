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

package com.ephesoft.dcma.gwt.admin.bm.client.view.tableinfo;

import java.util.LinkedList;
import java.util.List;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.tablecolumninfo.TableColumnInfoListPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.tableinfo.TableInfoViewPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.tablecolumninfo.TableColumnInfoListView;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.table.Record;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.TableColumnInfoDTO;
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
 * This class provides functionality to edit table info view.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class TableInfoView extends View<TableInfoViewPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<DockLayoutPanel, TableInfoView> {
	}

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * tableColumnInfoListView TableColumnInfoListView.
	 */
	private final TableColumnInfoListView tableColumnInfoListView;

	/**
	 * tableColumnInfoListPresenter TableColumnInfoListPresenter.
	 */
	private TableColumnInfoListPresenter tableColumnInfoListPresenter;

	/**
	 * tableColumnInfoListPanel LayoutPanel.
	 */
	@UiField
	protected LayoutPanel tableColumnInfoListPanel;

	/**
	 * tableInfoDetailView TableInfoDetailView.
	 */
	@UiField
	protected TableInfoDetailView tableInfoDetailView;

	/**
	 * editTableInfoView EditTableInfoView.
	 */
	@UiField
	protected EditTableInfoView editTableInfoView;

	/**
	 * tableInfoVerticalPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel tableInfoVerticalPanel;

	/**
	 * tableInfoConfigVerticalPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel tableInfoConfigVerticalPanel;

	/**
	 * tableInfoCaptionPanel CaptionPanel.
	 */
	@UiField
	protected CaptionPanel tableInfoCaptionPanel;

	/**
	 * addTCButton Button.
	 */
	@UiField
	protected Button addTCButton;

	/**
	 * editTCButton Button.
	 */
	@UiField
	protected Button editTCButton;

	/**
	 * advEditButton Button.
	 */
	@UiField
	protected Button advEditButton;

	/**
	 * deleteTCButton Button.
	 */
	@UiField
	protected Button deleteTCButton;

	/**
	 * testTableButton Button.
	 */
	@UiField
	protected Button testTableButton;

	/**
	 * tableColumnInfoButtonPanel HorizontalPanel.
	 */
	@UiField
	protected HorizontalPanel tableColumnInfoButtonPanel;

	/**
	 * Constructor.
	 */
	public TableInfoView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		tableColumnInfoListView = new TableColumnInfoListView();
		tableColumnInfoListPanel.add(tableColumnInfoListView.listView);

		addTCButton.setText(AdminConstants.ADD_BUTTON);
		editTCButton.setText(AdminConstants.EDIT_BUTTON);
		deleteTCButton.setText(AdminConstants.DELETE_BUTTON);
		advEditButton.setText(AdminConstants.ADV_EDIT);

		tableInfoCaptionPanel.setCaptionHTML(AdminConstants.TABLE_INFO_DETAILS_HTML);

		addTCButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		editTCButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		deleteTCButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		advEditButton.setHeight(AdminConstants.BUTTON_HEIGHT);

		testTableButton.setText(AdminConstants.TEST_TABLE_BUTTON);
		testTableButton.setHeight("20px");
		testTableButton.addStyleName("margin");

		tableColumnInfoButtonPanel.addStyleName("topPadd");

	}

	/**
	 * To perform operations on Test Table Button Clicked.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("testTableButton")
	public void onTestTableButtonClicked(ClickEvent clickEvent) {
		presenter.onTestTableButtonClicked();
	}

	/**
	 * To get Table Info Detail View.
	 * 
	 * @return TableInfoDetailView
	 */
	public TableInfoDetailView getTableInfoDetailView() {
		return tableInfoDetailView;
	}

	/**
	 * To get Table Column Info List View.
	 * 
	 * @return TableColumnInfoListView
	 */
	public TableColumnInfoListView getTableColumnInfoListView() {
		return tableColumnInfoListView;
	}

	/**
	 * To create Table Column Info List.
	 * 
	 * @param fields List<TableColumnInfoDTO>
	 */
	public void createTableColumnInfoList(List<TableColumnInfoDTO> fields) {
		List<Record> recordList = setFieldsList(fields);
		tableColumnInfoListPresenter = new TableColumnInfoListPresenter(presenter.getController(), tableColumnInfoListView);
		tableColumnInfoListView.listView.initTable(recordList.size(), null, null, recordList, true, false,
				tableColumnInfoListPresenter, null, true);
	}

	private List<Record> setFieldsList(List<TableColumnInfoDTO> fields) {

		List<Record> recordList = new LinkedList<Record>();
		for (final TableColumnInfoDTO tcInfoDTO : fields) {
			Record record = new Record(tcInfoDTO.getIdentifier());
			CheckBox isRequired = new CheckBox();
			CheckBox isMandatory = new CheckBox();
			isRequired.setValue(tcInfoDTO.isRequired());
			isRequired.setEnabled(false);
			isMandatory.setValue(tcInfoDTO.isMandatory());
			isMandatory.setEnabled(false);
			record.addWidget(tableColumnInfoListView.betweenLeft, new Label(tcInfoDTO.getBetweenLeft()));
			record.addWidget(tableColumnInfoListView.betweenRight, new Label(tcInfoDTO.getBetweenRight()));
			record.addWidget(tableColumnInfoListView.columnName, new Label(tcInfoDTO.getColumnName()));
			record.addWidget(tableColumnInfoListView.columnHeaderPattern, new Label(tcInfoDTO.getColumnHeaderPattern()));
			record.addWidget(tableColumnInfoListView.columnStartCoord, new Label(tcInfoDTO.getColumnStartCoordinate()));
			record.addWidget(tableColumnInfoListView.columnEndCoord, new Label(tcInfoDTO.getColumnEndCoordinate()));
			record.addWidget(tableColumnInfoListView.columnPattern, new Label(tcInfoDTO.getColumnPattern()));
			record.addWidget(tableColumnInfoListView.isRequired, isRequired);
			record.addWidget(tableColumnInfoListView.isMandatory, isMandatory);
			recordList.add(record);
		}

		return recordList;
	}

	/**
	 * To get Edit Table Info View.
	 * 
	 * @return EditTableInfoView
	 */
	public EditTableInfoView getEditTableInfoView() {
		return editTableInfoView;
	}

	/**
	 * To get Table Info Vertical Panel.
	 * 
	 * @return VerticalPanel
	 */
	public VerticalPanel getTableInfoVerticalPanel() {
		return tableInfoVerticalPanel;
	}

	/**
	 * To get Table Info Configuration Vertical Panel.
	 * 
	 * @return VerticalPanel
	 */
	public VerticalPanel getTableInfoConfigVerticalPanel() {
		return tableInfoConfigVerticalPanel;
	}

	/**
	 * To get Add TC Button.
	 * 
	 * @return Button
	 */
	public Button getAddTCButton() {
		return addTCButton;
	}

	/**
	 * To perform operations on Add TC Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("addTCButton")
	public void onAddTCButtonClick(ClickEvent clickEvent) {
		presenter.onAddTCButtonClicked();
	}

	/**
	 * To perform operations on edit TC Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("editTCButton")
	public void onEditTCButtonClicked(ClickEvent clickEvent) {
		tableColumnInfoListPresenter.onEditButtonClicked();
	}

	/**
	 * To perform operations on advanced edit Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("advEditButton")
	public void onAdvEditButtonClicked(ClickEvent clickEvent) {
		tableColumnInfoListPresenter.onAdvEditButtonClicked();
	}

	/**
	 * To delete the selected records.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("deleteTCButton")
	public void onDeleteTCButtonClicked(ClickEvent clickEvent) {
		final String identifier = tableColumnInfoListView.listView.getSelectedRowIndex();
		int rowCount = tableColumnInfoListView.listView.getTableRecordCount();
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
				.getMessageValue(BatchClassManagementMessages.DELETE_TABLE_COLUMN_INFO_CONFORMATION), LocaleDictionary.get()
				.getConstantValue(BatchClassManagementConstants.DELETE_TABLE_COLUMN_INFO_TITLE), Boolean.FALSE);

		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				confirmationDialog.hide();
				presenter.onDeleteTCButtonClicked(identifier);
			}

			@Override
			public void onCancelClick() {
				confirmationDialog.hide();
			}
		});

	}

}
