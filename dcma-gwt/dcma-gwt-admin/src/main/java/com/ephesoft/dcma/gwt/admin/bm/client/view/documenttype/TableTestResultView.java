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

import java.util.LinkedList;
import java.util.List;

import com.ephesoft.dcma.batch.schema.Column;
import com.ephesoft.dcma.batch.schema.HeaderRow;
import com.ephesoft.dcma.batch.schema.Row;
import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.MessageConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.documenttype.TableTestResultViewPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.table.Record;
import com.ephesoft.dcma.gwt.core.shared.TestTableResultDTO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;

public class TableTestResultView extends View<TableTestResultViewPresenter> {

	interface Binder extends UiBinder<ScrollPanel, TableTestResultView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	private FlexTable resultTable;

	@UiField
	FlowPanel tableListPanel;

	private HorizontalPanel buttonPanel;

	private Button backButton;

	private DialogBox dialogBox;
	
	@UiField ScrollPanel scrollPanel;

	public TableTestResultView() {
		initWidget(binder.createAndBindUi(this));
		
		scrollPanel.setSize("500px", "300px");
		
		resultTable = new FlexTable();
		resultTable.setWidth("100%");
		resultTable.setCellSpacing(0);
		resultTable.addStyleName("border-result-table");

		backButton = new Button();
		backButton.setText(AdminConstants.CLOSE_BUTTON);
		
		buttonPanel = new HorizontalPanel();
		buttonPanel.add(backButton);

		tableListPanel.add(buttonPanel);
		tableListPanel.add(resultTable);

		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				dialogBox.hide(true);
			}
		});
	}

	public void createTestTableList(List<TestTableResultDTO> outputDtos) {
		resultTable.removeAllRows();
		setResultList(outputDtos);
	}

	private List<Record> setResultList(List<TestTableResultDTO> outputDtos) {

		List<Record> recordList = new LinkedList<Record>();
		int index = 0;
		int rows = 0;
		boolean isHeaderRowAdded = false;
		if (outputDtos != null && outputDtos.size() > 0) {
			for (final TestTableResultDTO resultDTO : outputDtos) {
				if(!isHeaderRowAdded) {
					isHeaderRowAdded = createHeaderRow(resultDTO.getDataTable().getHeaderRow());
				}
				for (rows = 0; rows < resultDTO.getDataTable().getRows().getRow().size(); rows++) {
					resultTable.setWidget(index + rows + 1, 0, new Label(resultDTO.getInputFileName()));
					Row row = resultDTO.getDataTable().getRows().getRow().get(rows);
					int colIndex = 1;
					//row.getRowCoordinates();
					for(Column column : row.getColumns().getColumn()) {
						resultTable.setWidget(index + rows + 1, colIndex, new Label(column.getValue()));
						colIndex++;
					}
				}
				index = rows;
			}
		} else {
			resultTable.getFlexCellFormatter().setColSpan(1, 0, 4);
			resultTable.setWidget(index, 0, new Label(MessageConstants.MSG_NO_RESULTS_FOUND));
		}

		return recordList;
	}

	private boolean createHeaderRow(HeaderRow headerRow) {		
		resultTable.getCellFormatter().setWidth(0, 0, "25%");
		
		resultTable.setWidget(0, 0, new Label(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.PAGE_NAME)));
		int index = 1;
		for(Column column : headerRow.getColumns().getColumn()) {
			resultTable.getCellFormatter().setWidth(0, index, "20%");
			resultTable.setWidget(0, index, new Label(column.getName()));
			index++;
		}
		resultTable.getRowFormatter().setStylePrimaryName(0, "header");
		return true;
	}

	public DialogBox getDialogBox() {
		return dialogBox;
	}

	public void setDialogBox(DialogBox dialogBox) {
		this.dialogBox = dialogBox;
	}
	
	public Button getBackButton() {
		return backButton;
	}

}
