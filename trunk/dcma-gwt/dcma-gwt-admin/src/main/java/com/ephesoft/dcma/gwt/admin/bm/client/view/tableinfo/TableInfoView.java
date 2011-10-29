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

package com.ephesoft.dcma.gwt.admin.bm.client.view.tableinfo;

import java.util.LinkedList;
import java.util.List;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
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

public class TableInfoView extends View<TableInfoViewPresenter> {

	interface Binder extends UiBinder<DockLayoutPanel, TableInfoView> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);

	private final TableColumnInfoListView tableColumnInfoListView;

	@UiField
	protected LayoutPanel tableColumnInfoListPanel;

	@UiField
	protected TableInfoDetailView tableInfoDetailView;

	@UiField
	protected EditTableInfoView editTableInfoView;

	@UiField
	protected Button editTableInfoPropertiesButton;

	@UiField
	protected VerticalPanel tableInfoVerticalPanel;

	@UiField
	protected VerticalPanel tableInfoConfigVerticalPanel;

	@UiField
	protected CaptionPanel tableInfoCaptionPanel;

	@UiField
	protected Button addTCButton;

	@UiField
	protected Button editTCButton;

	@UiField
	protected Button deleteTCButton;

	@UiField
	protected HorizontalPanel tableColumnInfoButtonPanel;

	public TableInfoView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		tableColumnInfoListView = new TableColumnInfoListView();
		tableColumnInfoListPanel.add(tableColumnInfoListView.listView);

		addTCButton.setText(AdminConstants.ADD_BUTTON);
		editTCButton.setText(AdminConstants.EDIT_BUTTON);
		deleteTCButton.setText(AdminConstants.DELETE_BUTTON);

		editTableInfoPropertiesButton.setText(AdminConstants.EDIT_BUTTON);

		tableInfoCaptionPanel.setCaptionHTML(AdminConstants.TABLE_INFO_DETAILS_HTML);

		addTCButton.setHeight("20px");
		editTCButton.setHeight("20px");
		deleteTCButton.setHeight("20px");

		tableColumnInfoButtonPanel.addStyleName("topPadd");

		tableInfoVerticalPanel.add(editTableInfoPropertiesButton);

	}

	public TableInfoDetailView getTableInfoDetailView() {
		return tableInfoDetailView;
	}

	public TableColumnInfoListView getTableColumnInfoListView() {
		return tableColumnInfoListView;
	}

	public void createTableColumnInfoList(List<TableColumnInfoDTO> fields) {
		List<Record> recordList = setFieldsList(fields);

		tableColumnInfoListView.listView.initTable(recordList.size(), recordList, true);
	}

	private List<Record> setFieldsList(List<TableColumnInfoDTO> fields) {

		List<Record> recordList = new LinkedList<Record>();
		for (final TableColumnInfoDTO tcInfoDTO : fields) {
			Record record = new Record(tcInfoDTO.getIdentifier());
			CheckBox isRequired = new CheckBox();
			isRequired.setValue(tcInfoDTO.isRequired());
			isRequired.setEnabled(false);
			record.addWidget(tableColumnInfoListView.betweenLeft, new Label(tcInfoDTO.getBetweenLeft()));
			record.addWidget(tableColumnInfoListView.betweenRight, new Label(tcInfoDTO.getBetweenRight()));
			record.addWidget(tableColumnInfoListView.columnName, new Label(tcInfoDTO.getColumnName()));
			record.addWidget(tableColumnInfoListView.columnPattern, new Label(tcInfoDTO.getColumnPattern()));
			record.addWidget(tableColumnInfoListView.isRequired, isRequired);
			recordList.add(record);
		}

		return recordList;
	}

	public EditTableInfoView getEditTableInfoView() {
		return editTableInfoView;
	}

	public Button getEditTableInfoPropertiesButton() {
		return editTableInfoPropertiesButton;
	}

	public VerticalPanel getTableInfoVerticalPanel() {
		return tableInfoVerticalPanel;
	}

	public VerticalPanel getTableInfoConfigVerticalPanel() {
		return tableInfoConfigVerticalPanel;
	}

	public Button getAddTCButton() {
		return addTCButton;
	}

	@UiHandler("editTableInfoPropertiesButton")
	public void onEditTableInfoPropertiesButtonClick(ClickEvent clickEvent) {
		presenter.onEditTableInfoPropertiesButtonClicked();
	}

	@UiHandler("addTCButton")
	public void onAddTCButtonClick(ClickEvent clickEvent) {
		presenter.onAddTCButtonClicked();
	}

	@UiHandler("editTCButton")
	public void onEditTCButtonClicked(ClickEvent clickEvent) {
		String identifier = tableColumnInfoListView.listView.getSelectedRowIndex();
		int rowCount = tableColumnInfoListView.listView.getTableRecordCount();
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
		presenter.onEditTCButtonClicked(identifier);

	}

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
		final ConfirmationDialog confirmationDialog = new ConfirmationDialog();
		confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(
				BatchClassManagementMessages.DELETE_TABLE_COLUMN_INFO_CONFORMATION));
		confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(
				BatchClassManagementConstants.DELETE_TABLE_COLUMN_INFO_TITLE));
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
		confirmationDialog.center();
		confirmationDialog.show();
		confirmationDialog.okButton.setFocus(true);
	}

}
