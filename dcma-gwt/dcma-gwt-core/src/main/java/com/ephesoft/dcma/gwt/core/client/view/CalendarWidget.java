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

package com.ephesoft.dcma.gwt.core.client.view;

import java.util.Date;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.Widget;

public class CalendarWidget extends Composite implements ClickListener, SourcesChangeEvents {

	private class NavBar extends Composite implements ClickListener {

		public final DockPanel bar = new DockPanel();
		public final Button prevMonth = new Button("&lt;", this);
		public final Button prevYear = new Button("&lt;&lt;", this);
		public final Button nextYear = new Button("&gt;&gt;", this);
		public final Button nextMonth = new Button("&gt;", this);
		public final HTML title = new HTML();

		private final CalendarWidget calendar;

		public NavBar(CalendarWidget calendar) {
			super();
			this.calendar = calendar;

			setWidget(bar);
			bar.setStyleName("navbar");
			title.setStyleName("header");

			HorizontalPanel prevButtons = new HorizontalPanel();
			prevButtons.add(prevMonth);
			prevButtons.add(prevYear);

			HorizontalPanel nextButtons = new HorizontalPanel();
			nextButtons.add(nextYear);
			nextButtons.add(nextMonth);

			bar.add(prevButtons, DockPanel.WEST);
			bar.setCellHorizontalAlignment(prevButtons, DockPanel.ALIGN_LEFT);
			bar.add(nextButtons, DockPanel.EAST);
			bar.setCellHorizontalAlignment(nextButtons, DockPanel.ALIGN_RIGHT);
			bar.add(title, DockPanel.CENTER);
			bar.setVerticalAlignment(DockPanel.ALIGN_MIDDLE);
			bar.setCellHorizontalAlignment(title, HasAlignment.ALIGN_CENTER);
			bar.setCellVerticalAlignment(title, HasAlignment.ALIGN_MIDDLE);
			bar.setCellWidth(title, "100%");
		}

		public void onClick(Widget sender) {
			if (sender.equals(prevMonth)) {
				calendar.prevMonth();
			} else if (sender.equals(prevYear)) {
				calendar.prevYear();
			} else if (sender.equals(nextYear)) {
				calendar.nextYear();
			} else if (sender.equals(nextMonth)) {
				calendar.nextMonth();
			}
		}
	}

	private static class CellHTML extends HTML {

		private final int day;

		public CellHTML(String text, int day) {
			super(text);
			this.day = day;
		}

		public int getDay() {
			return day;
		}
	}

	private final NavBar navbar = new NavBar(this);
	private final DockPanel outer = new DockPanel();

	private final Grid grid = new Grid(6, 7) {

		public boolean clearCell(int row, int column) {
			boolean retValue = super.clearCell(row, column);

			Element tableColumn = getCellFormatter().getElement(row, column);
			DOM.setInnerHTML(tableColumn, "");
			return retValue;
		}
	};

	private Date date = new Date();

	private ChangeListenerCollection changeListeners;

	private final String[] days = new String[] {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

	private final String[] months = new String[] {"January", "February", "March", "April", "May", "June", "July", "August", "September",
			"October", "November", "December"};

	public CalendarWidget() {
		super();
		setWidget(outer);
		grid.setStyleName("table");
		grid.setCellSpacing(0);
		outer.add(navbar, DockPanel.NORTH);
		outer.add(grid, DockPanel.CENTER);
		drawCalendar();
		setStyleName("CalendarWidget");
	}

	private void drawCalendar() {
		int year = getYear();
		int month = getMonth();
		// int day = getDay();
		setHeaderText(year, month);
		grid.getRowFormatter().setStyleName(0, "weekheader");
		for (int i = 0; i < days.length; i++) {
			grid.getCellFormatter().setStyleName(0, i, "days");
			grid.setText(0, i, days[i].substring(0, 3));
		}

		Date now = new Date();
		int sameDay = now.getDate();
		int today = (now.getMonth() == month && now.getYear() + 1900 == year) ? sameDay : 0;

		int firstDay = new Date(year - 1900, month, 1).getDay();
		int numOfDays = getDaysInMonth(year, month);

		int colCount = 0;
		for (int rowCount = 1; rowCount < 6; rowCount++) {
			for (int colIterator = 0; colIterator < 7; colIterator++, colCount++) {
				int displayNum = (colCount - firstDay + 1);
				if (colCount < firstDay || displayNum > numOfDays) {
					grid.getCellFormatter().setStyleName(rowCount, colIterator, "empty");
					grid.setHTML(rowCount, colIterator, "&nbsp;");
				} else {
					HTML html = new CellHTML("<span>" + displayNum + "</span>", displayNum);
					html.addClickListener(this);
					grid.getCellFormatter().setStyleName(rowCount, colIterator, "cell");
					if (displayNum == today) {
						grid.getCellFormatter().addStyleName(rowCount, colIterator, "today");
					} else if (displayNum == sameDay) {
						grid.getCellFormatter().addStyleName(rowCount, colIterator, "day");
					}
					grid.setWidget(rowCount, colIterator, html);
				}
			}
		}
	}

	protected final void setHeaderText(int year, int month) {
		navbar.title.setText(months[month] + ", " + year);
	}

	private int getDaysInMonth(int year, int month) {
		int noOfDays;
		switch (month) {
			case 1:
				if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0){
					noOfDays = 29; // leap year
				}
				else{
					noOfDays = 28;
				}
				break;
			case 3:
				noOfDays = 30;
				break;
			case 5:
				noOfDays = 30;
				break;
			case 8:
				noOfDays = 30;
				break;
			case 10:
				noOfDays = 30;
				break;
			default:
				noOfDays = 31;
		}
		return noOfDays;
	}

	public void prevMonth() {
		int month = getMonth() - 1;
		if (month < 0) {
			setDate(getYear() - 1, 11, getDay());
		} else {
			setMonth(month);
		}
		drawCalendar();
	}

	public void nextMonth() {
		int month = getMonth() + 1;
		if (month > 11) {
			setDate(getYear() + 1, 0, getDay());
		} else {
			setMonth(month);
		}
		drawCalendar();
	}

	public void prevYear() {
		setYear(getYear() - 1);
		drawCalendar();
	}

	public void nextYear() {
		setYear(getYear() + 1);
		drawCalendar();
	}

	private void setDate(int year, int month, int day) {
		date = new Date(year - 1900, month, day);
	}

	private void setYear(int year) {
		date.setYear(year - 1900);
	}

	private void setMonth(int month) {
		date.setMonth(month);
	}

	public final int getYear() {
		return 1900 + date.getYear();
	}

	public final int getMonth() {
		return date.getMonth();
	}

	public final int getDay() {
		return date.getDate();
	}

	public Date getDate() {
		return date;
	}

	public void onClick(Widget sender) {
		CellHTML cell = (CellHTML) sender;
		setDate(getYear(), getMonth(), cell.getDay());
		drawCalendar();
		if (changeListeners != null) {
			changeListeners.fireChange(this);
		}
	}

	public void addChangeListener(ChangeListener listener) {
		if (changeListeners == null){
			changeListeners = new ChangeListenerCollection();
		}
		changeListeners.add(listener);
	}

	public void removeChangeListener(ChangeListener listener) {
		if (changeListeners != null){
			changeListeners.remove(listener);
		}
	}

	public void hide() {
		outer.setVisible(false);
	}
}
