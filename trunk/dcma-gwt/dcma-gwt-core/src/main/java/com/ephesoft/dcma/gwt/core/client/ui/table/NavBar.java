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

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleCommonConstants;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.PaginationListner;
import com.ephesoft.dcma.gwt.core.client.validator.NumberValidator;
import com.ephesoft.dcma.gwt.core.client.validator.ValidatableTextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

class NavBar extends Composite {

	@UiTemplate("NavBar.ui.xml")
	interface Binder extends UiBinder<Widget, NavBar> {
	}

	public interface Images extends ClientBundle {

		ImageResource nextPage();

		ImageResource previousPage();
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	Anchor newerButton;
	@UiField
	Anchor olderButton;

	enum Pagination {
		NONE, NEWER, OLDER;
	}

	private Order order;

	private PaginationListner listner;

	Pagination pagination = Pagination.NONE;

	private int startIndex;
	// private int endIndex;
	// private int count;

	private String countString;

	private Integer currentPageNumber;

	private Integer totalPageCount;

	private ValidatableTextBox searchPageTextBox;

	public NavBar() {
		initWidget(binder.createAndBindUi(this));
		Images images = GWT.create(Images.class);
		DOM.setInnerHTML(newerButton.getElement(), AbstractImagePrototype.create(images.previousPage()).getHTML());
		DOM.setInnerHTML(olderButton.getElement(), AbstractImagePrototype.create(images.nextPage()).getHTML());
		countString = new String();
		newerButton.setTitle(LocaleDictionary.get().getConstantValue(LocaleCommonConstants.title_previous));
		olderButton.setTitle(LocaleDictionary.get().getConstantValue(LocaleCommonConstants.title_next));
		searchPageTextBox = new ValidatableTextBox();
		searchPageTextBox.addValidator(new NumberValidator(searchPageTextBox, false, true));
		searchPageTextBox.setText("1");
		searchPageTextBox.setWidth("30px");
		searchPageTextBox.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				String pageNo = searchPageTextBox.getText();
				char keyCode = event.getCharCode();
				if (keyCode == KeyCodes.KEY_ENTER && checkTextEntered(pageNo) && searchPageTextBox.validate()) {
					moveToEnteredPage(pageNo);
				}
			}

		});
		searchPageTextBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> arg0) {
				searchPageTextBox.toggleValidDateBox();
			}
		});
	}

	private void moveToEnteredPage(String pageNo) {
		int currentPageNo = Integer.parseInt(pageNo) - 1;
		int totalPageCountLocal = totalPageCount > 0 ? (totalPageCount - 1) : totalPageCount;
		int startIndex = 0;
		if (currentPageNo < 0) {
			searchPageTextBox.setText("1");
		} else if (currentPageNo > totalPageCountLocal) {
			startIndex = totalPageCountLocal * Table.VISIBLE_RECORD_COUNT;
			searchPageTextBox.setText(totalPageCount.toString());
		} else {
			startIndex = currentPageNo * Table.VISIBLE_RECORD_COUNT;
		}
		listner.onPagination(startIndex, Table.VISIBLE_RECORD_COUNT, order);
	}

	private boolean checkTextEntered(String pageNo) {
		boolean textEntered = false;
		if (pageNo != null && !pageNo.isEmpty()) {
			textEntered = true;
		}
		return textEntered;
	}

	public void update(int startIndex, int count, int max) {
		this.startIndex = startIndex;
		this.currentPageNumber = getCurrentPageNumber(startIndex, Table.VISIBLE_RECORD_COUNT);
		this.totalPageCount = getTotalPageCount(count, Table.VISIBLE_RECORD_COUNT);
		if (totalPageCount == 0) {
			searchPageTextBox.setText(totalPageCount.toString());
		} else {
			searchPageTextBox.setText(currentPageNumber.toString());
		}
		// this.endIndex = max;
		// this.count = count;

		setVisibility(newerButton, startIndex != 0);
		setVisibility(olderButton, startIndex + Table.VISIBLE_RECORD_COUNT < count);
		String displayingText = LocaleDictionary.get().getConstantValue(LocaleCommonConstants.title_displaying);
		if (listner != null) {
			countString = displayingText + (startIndex + 1) + " - " + max + " of " + count;
		} else {
			countString = displayingText + (startIndex + 1) + " - " + count + " of " + count;
		}
		if (count == 0) {
			countString = displayingText + (startIndex) + " - " + count + " of " + count;
		}
		// countLabel.setInnerText("" + (startIndex + 1) + " - " + max + " of " + count);
	}

	private int getTotalPageCount(int count, int max) {
		Double returnVal = 0.0;
		double countDouble = count;
		if (max != 0) {
			returnVal = Math.ceil(countDouble / max);
		}
		return returnVal.intValue();
	}

	private Integer getCurrentPageNumber(int startIndex, int max) {
		Double returnVal = 0.0;
		double startIndexDouble = startIndex + 1;
		if (max != 0) {
			returnVal = Math.ceil(startIndexDouble / max);
		}
		return returnVal.intValue();
	}

	@UiHandler("newerButton")
	void onNewerClicked(ClickEvent event) {
		pagination = Pagination.NEWER;
		startIndex = startIndex - Table.VISIBLE_RECORD_COUNT;
		if (startIndex <= 0) {
			startIndex = 0;
		}
		searchPageTextBox.setText(getCurrentPageNumber(startIndex, Table.VISIBLE_RECORD_COUNT).toString());
		listner.onPagination(startIndex, Table.VISIBLE_RECORD_COUNT, order);
	}

	@UiHandler("olderButton")
	void onOlderClicked(ClickEvent event) {
		pagination = Pagination.OLDER;
		startIndex = startIndex + Table.VISIBLE_RECORD_COUNT;
		searchPageTextBox.setText(getCurrentPageNumber(startIndex, Table.VISIBLE_RECORD_COUNT).toString());
		listner.onPagination(startIndex, Table.VISIBLE_RECORD_COUNT, order);
	}

	private void setVisibility(Widget widget, boolean visible) {
		widget.getElement().getStyle().setVisibility(visible ? Visibility.VISIBLE : Visibility.HIDDEN);
	}

	public void setListner(PaginationListner paginationListner) {
		this.listner = paginationListner;
	}

	public String getCountString() {
		return countString;
	}

	public PaginationListner getListner() {
		return listner;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public int getCurrentPageNumber() {
		return currentPageNumber;
	}

	public void setCurrentPageNumber(int currentPageNumber) {
		this.currentPageNumber = currentPageNumber;
	}

	public int getTotalPageCount() {
		return totalPageCount;
	}

	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}

	public ValidatableTextBox getSearchPageTextBox() {
		return searchPageTextBox;
	}

	public void setSearchPageTextBox(ValidatableTextBox searchPageTextBox) {
		this.searchPageTextBox = searchPageTextBox;
	}
}
