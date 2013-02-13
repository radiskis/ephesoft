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

package com.ephesoft.dcma.gwt.core.client.ui.table;

import java.util.List;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.gwt.core.client.ui.table.TableHeader.HeaderColumn;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

public class ListView extends ResizeComposite {

	interface Binder extends UiBinder<Widget, ListView> {
	}

	public interface RowSelectionListner {

		void onRowSelected(String identifer);
	}

	public interface DoubleClickListner {

		void onDoubleClickTable();
	}

	public interface PaginationListner {

		void onPagination(int startIndex, int maxResult, Order order);
	}

	public interface OrderingListner {

		void onOrdering(int startIndex, int maxResult, String selectedRowId, int swapIndex, int selectedRecordIndex);
	}

	@UiField
	protected LayoutPanel layoutPanel;

	private Table table;

	private final TableHeader header = new TableHeader();

	private static final Binder BINDER = GWT.create(Binder.class);

	public ListView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
	}

	public void addHeaderColumns(HeaderColumn... columns) {
		for (int i = 0; i < columns.length; i++) {
			header.addHeaderColumn(columns[i]);
		}
	}

	public void initTable(int totalCount, List<Record> recordList, boolean requireRadioButton) {
		initTable(totalCount, null, recordList, requireRadioButton, false, null, false);

	}

	public void initTable(int totalCount, List<Record> recordList, boolean requireRadioButton, boolean fireEventForFirstRow) {
		initTable(totalCount, null, recordList, requireRadioButton, fireEventForFirstRow, null, false);

	}

	public void addHeaderColumns(List<HeaderColumn> headerColumns) {
		for (HeaderColumn headerColumn : headerColumns) {
			header.addHeaderColumn(headerColumn);
		}
	}

	public void initTable(int totalCount, PaginationListner paginationListner, List<Record> recordList, boolean requireRadioButton,
			boolean fireEventForFirstRow, OrderingListner orderingListner, boolean isOrderedEntity) {
		initTable(totalCount, paginationListner, recordList, requireRadioButton, fireEventForFirstRow, null, null, isOrderedEntity);
	}

	public void initTable(int totalCount, PaginationListner paginationListner, List<Record> recordList, boolean requireRadioButton,
			boolean fireEventForFirstRow, DoubleClickListner doubleClickListner, OrderingListner orderingListner,
			boolean isOrderedEntity) {
		initTable(totalCount, paginationListner, null, recordList, requireRadioButton, fireEventForFirstRow, doubleClickListner,
				orderingListner, isOrderedEntity);
	}

	public void initTable(int totalCount, PaginationListner paginationListner, RowSelectionListner rowSelectionListner,
			List<Record> recordList, boolean requireRadioButton, boolean fireEventForFirstRow, OrderingListner orderingListner,
			boolean isOrderedEntity) {
		initTable(totalCount, paginationListner, rowSelectionListner, recordList, requireRadioButton, fireEventForFirstRow, null,
				orderingListner, isOrderedEntity);
	}

	public void initTable(int totalCount, PaginationListner paginationListner, RowSelectionListner rowSelectionListner,
			List<Record> recordList, boolean requireRadioButton, boolean fireEventForFirstRow, DoubleClickListner doubleClickListner,
			OrderingListner orderingListner, boolean isOrderedEntity) {
		table = new Table(totalCount, header, requireRadioButton, fireEventForFirstRow, doubleClickListner);
		table.setPaginationListner(paginationListner);
		table.setRowSelectionListener(rowSelectionListner);
		table.setOrderingListner(orderingListner);
		table.setOrderedEntity(isOrderedEntity);
		table.pushData(recordList, 0);
		layoutPanel.clear();
		layoutPanel.add(table);

	}

	public void initTable(int totalCount, List<Record> recordList) {
		initTable(totalCount, recordList, false, false);
	}

	public void updateRecords(List<Record> recordList, int startIndex) {
		table.pushData(recordList, startIndex);
	}

	public void updateRecords(List<Record> recordList, int startIndex, int count) {
		table.pushData(recordList, startIndex, count);
	}

	public void updateRecords(List<Record> recordList, int startIndex, int count, int selectedIndex) {
		table.pushData(recordList, startIndex, count, selectedIndex);
	}

	public void updateUnlockRecords(List<Record> recordList, int startIndex, int count) {
		table.pushData(recordList, startIndex, count, table.getSelectedIndex());
	}

	public String getSelectedRowIndex() {
		return table.getSelectedRowId();
	}

	public TableHeader getHeader() {
		return header;
	}

	public int getTableRowCount() {
		return Table.visibleRecodrCount;
	}

	public void setTableRowCount(int count) {
		Table.visibleRecodrCount = count;
	}

	public int getTableRecordCount() {
		return table.getTableRecordCount();
	}

	public Order getTableOrder() {
		return table.getOrder();
	}

	public int getStartIndex() {
		return table.getStartIndex();
	}
}
