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

package com.ephesoft.dcma.gwt.core.client.ui.table;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.PaginationListner;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.RowSelectionListner;
import com.ephesoft.dcma.gwt.core.client.ui.table.TableHeader.HeaderColumn;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.Cell;

public class Table extends ResizeComposite {

	interface Binder extends UiBinder<Widget, Table> {
	}

	public interface Images extends ClientBundle {

		ImageResource sortUp();

		ImageResource sortDown();
	}

	interface SelectionStyle extends CssResource {

		String selectedRow();

		String oddRow();

		String evenRow();

		String header();

		String boldText();

		String rowHighlighted();
	}

	private static final Binder binder = GWT.create(Binder.class);
	public static int VISIBLE_RECORD_COUNT = 5;

	@UiField
	FlexTable table;

	@UiField
	FlexTable headerTable;

	@UiField
	SelectionStyle selectionStyle;

	@UiField
	FlexTable navBarTable;

	private TableData tableData;

	private int totalCount;
	private NavBar navBar;
	private boolean requireRadioButton;

	private boolean fireEventForFirstRow;

	private String selectedRowId;

	private RowSelectionListner listner;

	private Order order;

	private Map<Integer, RadioButtonContainer> radioButtons = new HashMap<Integer, RadioButtonContainer>();

	public Table(int totalCount, TableHeader header, boolean requireRadioButton, boolean fireEventForFirstRow) {
		initWidget(binder.createAndBindUi(this));
		this.totalCount = totalCount;
		this.fireEventForFirstRow = fireEventForFirstRow;
		tableData = new TableData();
		tableData.setHeader(header);
		this.requireRadioButton = requireRadioButton;
		navBar = new NavBar();
	}

	public void setPaginationListner(PaginationListner paginationListner) {
		this.navBar.setListner(paginationListner);
	}

	public void setRowSelectionListener(RowSelectionListner rowSelectionListner) {
		this.listner = rowSelectionListner;
	}

	@Override
	protected void onLoad() {
	}

	public void pushData(List<Record> recordList, int startIndex) {
		this.tableData.setRecordList(recordList);
		update(requireRadioButton, startIndex);
	}

	public void pushData(List<Record> recordList, int startIndex, int count) {
		this.tableData.setRecordList(recordList);
		this.totalCount = count;
		update(requireRadioButton, startIndex);
	}

	private void createTableHeader(boolean isRadioButton) {
		Images images = GWT.create(Images.class);
		final TableHeader header = tableData.getHeader();
		final LinkedList<HeaderColumn> columns = header.getHeaderColumns(isRadioButton);
		String width = null;
		int j = 0;
		for (final HeaderColumn column : columns) {
			width = String.valueOf(column.getWidth()) + "%";
			headerTable.getCellFormatter().setWidth(0, j, width);
			headerTable.getCellFormatter().addStyleName(0, j, "wordWrap");
			HorizontalPanel headerPanel = new HorizontalPanel();
			Label name = new Label(column.name);
			headerPanel.add(name);
			final Label sortImage = new Label();
			sortImage.setWidth("5px");
			sortImage.setStyleName("alignMiddle");
			if (order != null && column.getDomainProperty() != null
					&& order.getSortProperty().getProperty().equals(column.getDomainProperty().getProperty())) {
				if (column.isPrimaryAsc()) {
					DOM.setInnerHTML(sortImage.getElement(), AbstractImagePrototype.create(images.sortUp()).getHTML());
				} else {
					DOM.setInnerHTML(sortImage.getElement(), AbstractImagePrototype.create(images.sortDown()).getHTML());
				}
			}
			headerPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			headerPanel.add(sortImage);

			if (j == 0 && isRadioButton) {
				name.setText("");
			}
			headerTable.setWidget(0, j, headerPanel);
			if (column.isSortable) {
				name.addStyleName("cursorHand");
				name.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent arg0) {
						order = new Order(column.getDomainProperty(), !column.isPrimaryAsc());
						navBar.setOrder(order);
						column.setPrimaryAsc(!column.isPrimaryAsc());
						navBar.getListner().onPagination(navBar.getStartIndex(), VISIBLE_RECORD_COUNT, order);
					}
				});
			}
			headerTable.getFlexCellFormatter().setVerticalAlignment(0, j, HasVerticalAlignment.ALIGN_TOP);
			headerTable.getCellFormatter().setHorizontalAlignment(0, j, HasHorizontalAlignment.ALIGN_LEFT);
			j++;
		}
		headerTable.getRowFormatter().setStyleName(0, selectionStyle.header());
	}

	private void update(boolean isRadioButton, int startIndex) {
		selectedRowId = null;
		table.removeAllRows();
		navBarTable.removeAllRows();
		createTableHeader(isRadioButton);
		int count = totalCount;
		int max = startIndex + VISIBLE_RECORD_COUNT;
		if (max > count) {
			max = count;
		}
		navBar.update(startIndex, count, max);
		setNavigationBar();
		TableHeader header = tableData.getHeader();
		HeaderColumn[] columns = header.getHeaderColumns();
		String width = null;
		int i = 0;
		if (tableData.getRecordList() != null) {
			if (!tableData.getRecordList().isEmpty()) {
				for (final Record record : tableData.getRecordList()) {
					int j = 0;
					for (; j < columns.length; j++) {
						width = String.valueOf(columns[j].getWidth()) + "%";
						table.getCellFormatter().setWidth(i, j, width);
						table.setWidget(i, j, record.getWidget(columns[j]));
						table.getCellFormatter().setHorizontalAlignment(i, j, HasHorizontalAlignment.ALIGN_LEFT);
						table.getCellFormatter().setWordWrap(i, j, true);
						table.getCellFormatter().addStyleName(i, j, "wordWrap");
					}
					if (isRadioButton) {
						final RadioButton radioButton = new RadioButton(String.valueOf(new Date().getTime()));
						if (i == 0) {
							radioButton.setValue(true);
							selectedRowId = record.getIdentifier();
							if (null != listner && fireEventForFirstRow) {
								listner.onRowSelected(selectedRowId);
							}
						}
						radioButton.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent arg0) {
								clearRadioButtons();
								radioButton.setValue(true);
								selectedRowId = record.getIdentifier();
							}
						});
						table.setWidget(i, 0, radioButton);
						RadioButtonContainer radioButtonContainer = new RadioButtonContainer(radioButton, record.getIdentifier());
						radioButtons.put(new Integer(i), radioButtonContainer);
						table.getCellFormatter().setHorizontalAlignment(i, 0, HasHorizontalAlignment.ALIGN_CENTER);

						radioButton.addFocusHandler(new FocusHandler() {

							@Override
							public void onFocus(FocusEvent arg0) {
								removeSelectedRowStyleFromTable();
								for (Integer rowId : radioButtons.keySet()) {
									if (radioButtons.get(rowId).getRadioButton().equals(radioButton)) {
										table.getRowFormatter().addStyleName(rowId, selectionStyle.rowHighlighted());
									}
								}
							}
						});
					} else {
						RadioButtonContainer radioButtonContainer = new RadioButtonContainer(null, record.getIdentifier());
						radioButtons.put(new Integer(i), radioButtonContainer);
					}
					if (i % 2 == 0) {
						table.getRowFormatter().setStyleName(i, selectionStyle.oddRow());
					} else {
						table.getRowFormatter().setStyleName(i, selectionStyle.evenRow());
					}
					i++;
					table.getRowFormatter().addStyleName(0, selectionStyle.rowHighlighted());
				}
			} else {
				Label label = new Label();
				label.setWidth("100%");
				label.setText("No record found.");
				table.getCellFormatter().setWidth(1, 0, "100%");
				table.getFlexCellFormatter().setColSpan(1, 0, 3);
				// Record record = new Record("1");
				// tableData.getRecordList().add(record);
				table.setWidget(1, 0, label);
			}

		}
	}

	private void setNavigationBar() {
		navBarTable.getCellFormatter().setWidth(0, 1, "40%");
		navBarTable.getCellFormatter().setWidth(0, 2, "15%");
		navBarTable.getCellFormatter().setWidth(0, 3, "45%");
		Label displayText = new Label(navBar.getCountString());
		displayText.setStyleName(selectionStyle.boldText());
		if (this.navBar.getListner() != null) {
			navBarTable.setWidget(0, 2, navBar);
			navBarTable.getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_RIGHT);
		}
		navBarTable.setWidget(0, 3, displayText);
		navBarTable.getCellFormatter().setHorizontalAlignment(0, 3, HasHorizontalAlignment.ALIGN_RIGHT);
	}

	public String getSelectedRowId() {
		return selectedRowId;
	}

	public Order getOrder() {
		return order;
	}

	@UiHandler("table")
	void onTableClicked(ClickEvent event) {
		removeSelectedRowStyleFromTable();
		Cell cell = table.getCellForEvent(event);
		if (cell != null && totalCount != 0) {
			int row = cell.getRowIndex();
			table.getRowFormatter().addStyleName(row, selectionStyle.rowHighlighted());
			RadioButtonContainer radioButtonContainer = radioButtons.get(row);
			if (radioButtonContainer != null && radioButtonContainer.getRadioButton() != null) {
				clearRadioButtons();
				radioButtonContainer.getRadioButton().setValue(true);
				selectedRowId = radioButtonContainer.getIdentifier();
				if (listner != null) {
					listner.onRowSelected(selectedRowId);
				}
			}
			if (radioButtonContainer != null && radioButtonContainer.getRadioButton() == null
					&& radioButtonContainer.getIdentifier() != null) {
				selectedRowId = radioButtonContainer.getIdentifier();
			}
		}
	}

	private void clearRadioButtons() {
		for (RadioButtonContainer radioBtnContainer : radioButtons.values()) {
			radioBtnContainer.getRadioButton().setValue(false);
		}
	}

	private void removeSelectedRowStyleFromTable() {
		for (int rowId = 0; rowId < table.getRowCount(); rowId++) {
			table.getRowFormatter().removeStyleName(rowId, selectionStyle.rowHighlighted());
		}
	}

	private class RadioButtonContainer {

		private RadioButton radioButton;
		private String identifier;

		public RadioButtonContainer(RadioButton radioBtn, String identifier) {
			this.radioButton = radioBtn;
			this.identifier = identifier;
		}

		public String getIdentifier() {
			return identifier;
		}

		public RadioButton getRadioButton() {
			return radioButton;
		}
	}

	public int getTableRecordCount() {
		return tableData.getRecordList().size();
	}

}
