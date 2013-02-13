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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleCommonConstants;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.DoubleClickListner;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.OrderingListner;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.PaginationListner;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.RowSelectionListner;
import com.ephesoft.dcma.gwt.core.client.ui.table.TableHeader.HeaderColumn;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.Cell;

public class Table extends ResizeComposite implements HasDoubleClickHandlers {

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

	private static final Binder BINDER = GWT.create(Binder.class);
	public static int visibleRecodrCount = 5;

	@UiField
	protected FocusPanel focusPanel;

	@UiField
	protected FlexTable headerTable;

	@UiField
	protected SelectionStyle selectionStyle;

	@UiField
	protected FlexTable navBarTable;

	@UiField
	public ScrollPanel scrollPanel;

	@UiField
	protected FlexTable flexTable;

	private TableData tableData;

	private int totalCount;
	private NavBar navBar;
	private boolean requireRadioButton;

	private boolean orderedEntity;

	private boolean fireEventForFirstRow;

	private String selectedRowId;

	private RowSelectionListner listner;

	private Order order;

	private boolean mouseOn;

	private int selectedIndex;

	private static final int FIRST_COLUMN_INDEX = 1;

	private final Map<Integer, RadioButtonContainer> radioButtons = new HashMap<Integer, RadioButtonContainer>();

	public Table(int totalCount, TableHeader header, boolean requireRadioButton, boolean fireEventForFirstRow,
			final DoubleClickListner doubleClickListner) {
		super();
		initWidget(BINDER.createAndBindUi(this));
		this.totalCount = totalCount;
		this.fireEventForFirstRow = fireEventForFirstRow;
		tableData = new TableData();
		tableData.setHeader(header);
		this.requireRadioButton = requireRadioButton;
		navBar = new NavBar(this);
		mouseOn = false;
		if (doubleClickListner != null && totalCount != 0) {
			focusPanel.addMouseOutHandler(new MouseOutHandler() {

				@Override
				public void onMouseOut(MouseOutEvent arg0) {
					mouseOn = false;
				}
			});

			focusPanel.addMouseOverHandler(new MouseOverHandler() {

				@Override
				public void onMouseOver(MouseOverEvent arg0) {
					mouseOn = true;
				}
			});

			addDoubleClickHandler(new DoubleClickHandler() {

				@Override
				public void onDoubleClick(DoubleClickEvent arg0) {
					if (mouseOn) {
						doubleClickListner.onDoubleClickTable();
					}
				}
			});
		}
	}

	public Table(int totalCount, TableHeader header, boolean requireRadioButton, boolean fireEventForFirstRow) {
		this(totalCount, header, requireRadioButton, fireEventForFirstRow, null);
	}

	public void setPaginationListner(PaginationListner paginationListner) {
		this.navBar.setPaginationListner(paginationListner);
	}

	public void setOrderingListner(OrderingListner orderingListner) {
		this.navBar.setOrderingListner(orderingListner);
	}

	public void setRowSelectionListener(RowSelectionListner rowSelectionListner) {
		this.listner = rowSelectionListner;
	}

	/*
	 * @Override protected void onLoad() { }
	 */

	public void pushData(List<Record> recordList, int startIndex) {
		this.tableData.setRecordList(recordList);
		update(requireRadioButton, startIndex);
	}

	public void pushData(List<Record> recordList, int startIndex, int count) {
		this.tableData.setRecordList(recordList);
		this.totalCount = count;
		update(requireRadioButton, startIndex);
	}

	public void pushData(List<Record> recordList, int startIndex, int count, int selectedIndex) {
		this.tableData.setRecordList(recordList);
		this.totalCount = count;
		update(requireRadioButton, startIndex, selectedIndex);
	}

	private void createTableHeader(boolean isRadioButton) {
		Images images = GWT.create(Images.class);
		final TableHeader header = tableData.getHeader();
		final List<HeaderColumn> columns = header.getHeaderColumns(isRadioButton);
		String width = null;
		int counter = 0;
		for (final HeaderColumn column : columns) {
			width = column.getWidth() + "%";
			headerTable.getCellFormatter().setWidth(0, counter, width);
			headerTable.getCellFormatter().addStyleName(0, counter, "wordWrap");
			HorizontalPanel headerPanel = new HorizontalPanel();
			Label name = new Label(column.getName());
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

			if (counter == 0 && isRadioButton) {
				name.setText("");
			}
			headerTable.setWidget(0, counter, headerPanel);
			if (column.isSortable()) {
				name.addStyleName("cursorHand");
				name.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent arg0) {
						order = new Order(column.getDomainProperty(), !column.isPrimaryAsc());
						navBar.setOrder(order);
						column.setPrimaryAsc(!column.isPrimaryAsc());
						navBar.getListner().onPagination(navBar.getStartIndex(), visibleRecodrCount, order);
					}
				});
			}
			headerTable.getFlexCellFormatter().setVerticalAlignment(0, counter, HasVerticalAlignment.ALIGN_TOP);
			headerTable.getCellFormatter().setHorizontalAlignment(0, counter, HasHorizontalAlignment.ALIGN_LEFT);
			counter++;
		}
		headerTable.getRowFormatter().setStyleName(0, selectionStyle.header());
	}

	private native void scrollIntoView(Element element) /*-{
														element.scrollIntoView(true);
														}-*/;

	private void update(boolean isRadioButton, int startIndex, int selectedIndexlocal) {
		selectedRowId = null;
		flexTable.removeAllRows();
		navBarTable.removeAllRows();
		createTableHeader(isRadioButton);
		int count = totalCount;
		int max = startIndex + visibleRecodrCount;
		if (max > count) {
			max = count;
		}
		navBar.update(startIndex, count, max);
		setNavigationBar();
		TableHeader header = tableData.getHeader();
		HeaderColumn[] columns = header.getHeaderColumns();
		String width = null;
		int rowCounter = 0;
		String radioName = String.valueOf(new Date().getTime());
		final List<Record> recordList = tableData.getRecordList();
		if (recordList != null) {
			if (!recordList.isEmpty()) {
				for (final Record record : recordList) {
					int colCounter = 0;
					for (; colCounter < columns.length; colCounter++) {
						width = columns[colCounter].getWidth() + "%";
						flexTable.getCellFormatter().setWidth(rowCounter, colCounter, width);
						flexTable.setWidget(rowCounter, colCounter, record.getWidget(columns[colCounter]));
						flexTable.getCellFormatter().setHorizontalAlignment(rowCounter, colCounter, HasHorizontalAlignment.ALIGN_LEFT);
						flexTable.getCellFormatter().setWordWrap(rowCounter, colCounter, true);
						flexTable.getCellFormatter().addStyleName(rowCounter, colCounter, "wordWrap");
					}
					if (isRadioButton) {
						final RadioButton radioButton = new RadioButton(radioName);
						if (rowCounter == selectedIndexlocal) {
							radioButton.setValue(true);
							selectedRowId = record.getIdentifier();
							selectedIndex = rowCounter;
							if (null != listner && fireEventForFirstRow) {
								listner.onRowSelected(selectedRowId);
							}

							scrollIntoView(flexTable.getWidget(selectedIndexlocal, FIRST_COLUMN_INDEX).getElement());
						}
						radioButton.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent arg0) {
								clearRadioButtons();
								radioButton.setValue(true);
								selectedRowId = record.getIdentifier();
								selectedIndex = recordList.indexOf(record);
							}
						});
						flexTable.setWidget(rowCounter, 0, radioButton);
						RadioButtonContainer radioButtonContainer = new RadioButtonContainer(radioButton, record.getIdentifier());
						radioButtons.put(rowCounter, radioButtonContainer);
						flexTable.getCellFormatter().setHorizontalAlignment(rowCounter, 0, HasHorizontalAlignment.ALIGN_CENTER);

						radioButton.addFocusHandler(new FocusHandler() {

							@Override
							public void onFocus(FocusEvent arg0) {
								removeSelectedRowStyleFromTable();
								for (Integer rowId : radioButtons.keySet()) {
									if (radioButtons.get(rowId).getRadioButton().equals(radioButton)) {
										selectedIndex = recordList.indexOf(record);
										flexTable.getRowFormatter().addStyleName(rowId, selectionStyle.rowHighlighted());
									}
								}
							}
						});
					} else {
						RadioButtonContainer radioButtonContainer = new RadioButtonContainer(null, record.getIdentifier());
						radioButtons.put(rowCounter, radioButtonContainer);
					}
					if (rowCounter % 2 == 0) {
						flexTable.getRowFormatter().setStyleName(rowCounter, selectionStyle.oddRow());
					} else {
						flexTable.getRowFormatter().setStyleName(rowCounter, selectionStyle.evenRow());
					}
					rowCounter++;
					flexTable.getRowFormatter().addStyleName(selectedIndexlocal, selectionStyle.rowHighlighted());

				}
			} else {
				Label label = new Label();
				label.setWidth("100%");
				label.setText("No record found.");
				flexTable.getCellFormatter().setWidth(1, 0, "100%");
				flexTable.getFlexCellFormatter().setColSpan(1, 0, 3);
				// Record record = new Record("1");
				// tableData.getRecordList().add(record);
				flexTable.setWidget(1, 0, label);
			}

		}

	}

	private void update(boolean isRadioButton, int startIndex) {
		update(isRadioButton, startIndex, 0);
	}

	private void setNavigationBar() {
		navBarTable.getCellFormatter().setWidth(0, 1, "30%");
		navBarTable.getCellFormatter().setWidth(0, 2, "10%");
		navBarTable.getCellFormatter().setWidth(0, 3, "10%");
		navBarTable.getCellFormatter().setWidth(0, 4, "22%");
		navBarTable.getCellFormatter().setWidth(0, 5, "23%");
		Label displayText = new Label(navBar.getCountString());
		Label searchPageText = new Label(LocaleDictionary.get().getConstantValue(LocaleCommonConstants.TITLE_GO_TO_PAGE));
		Label totalPageCountText = new Label("  /" + navBar.getTotalPageCount());
		HorizontalPanel searchPagePanel = new HorizontalPanel();
		displayText.setStyleName(selectionStyle.boldText());
		searchPageText.setStyleName(selectionStyle.boldText());
		totalPageCountText.setStyleName(selectionStyle.boldText());
		searchPagePanel.add(searchPageText);
		searchPagePanel.add(navBar.getSearchPageTextBox());
		searchPagePanel.add(totalPageCountText);
		searchPagePanel.setCellHorizontalAlignment(searchPageText, HasHorizontalAlignment.ALIGN_RIGHT);
		searchPagePanel.setCellHorizontalAlignment(navBar.getSearchPageTextBox(), HasHorizontalAlignment.ALIGN_RIGHT);
		searchPagePanel.setCellHorizontalAlignment(totalPageCountText, HasHorizontalAlignment.ALIGN_RIGHT);
		if (this.navBar.getOrderingListner() != null) {
			navBarTable.setWidget(0, 2, navBar.getOrderingPanel());
			navBarTable.getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_RIGHT);
		}
		if (this.navBar.getListner() != null) {
			navBarTable.setWidget(0, 3, navBar.getPaginationPanel());
			navBarTable.getCellFormatter().setHorizontalAlignment(0, 3, HasHorizontalAlignment.ALIGN_RIGHT);
			navBarTable.setWidget(0, 4, searchPagePanel);
		}
		navBarTable.setWidget(0, 5, displayText);
		navBarTable.getCellFormatter().setHorizontalAlignment(0, 4, HasHorizontalAlignment.ALIGN_RIGHT);
		navBarTable.getCellFormatter().setHorizontalAlignment(0, 5, HasHorizontalAlignment.ALIGN_RIGHT);
	}

	public String getSelectedRowId() {
		return selectedRowId;
	}

	public Order getOrder() {
		return order;
	}

	@UiHandler("flexTable")
	protected void onFlexTableClicked(ClickEvent event) {
		removeSelectedRowStyleFromTable();
		Cell cell = flexTable.getCellForEvent(event);
		if (cell != null && totalCount != 0) {
			int row = cell.getRowIndex();
			selectedIndex = row;
			flexTable.getRowFormatter().addStyleName(row, selectionStyle.rowHighlighted());
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
		for (int rowId = 0; rowId < flexTable.getRowCount(); rowId++) {
			flexTable.getRowFormatter().removeStyleName(rowId, selectionStyle.rowHighlighted());
		}
	}

	private class RadioButtonContainer {

		private final RadioButton radioButton;
		private final String identifier;

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

	@Override
	public final HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		return addDomHandler(handler, DoubleClickEvent.getType());
	}

	/**
	 * @return the isOrderedEntity
	 */
	public boolean isOrderedEntity() {
		return orderedEntity;
	}

	/**
	 * @param isOrderedEntity the isOrderedEntity to set
	 */
	public void setOrderedEntity(boolean isOrderedEntity) {
		this.orderedEntity = isOrderedEntity;
	}

	public int getStartIndex() {
		return navBar.getStartIndex();
	}

	/**
	 * @return the selectedIndex
	 */
	public int getSelectedIndex() {
		return selectedIndex;
	}

	/**
	 * @param selectedIndex the selectedIndex to set
	 */
	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

}
