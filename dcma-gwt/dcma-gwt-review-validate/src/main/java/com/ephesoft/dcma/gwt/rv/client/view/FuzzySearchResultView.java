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

package com.ephesoft.dcma.gwt.rv.client.view;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.rv.client.event.FuzzySearchEvent;
import com.ephesoft.dcma.gwt.rv.client.i18n.ReviewValidateConstants;
import com.ephesoft.dcma.gwt.rv.client.presenter.ReviewValidatePresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;

public class FuzzySearchResultView extends RVBasePanel {

	interface Binder extends UiBinder<FlowPanel, FuzzySearchResultView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	private List<String> tbHeaderNameList;

	private List<String> tbValueList;

	private DialogBox dialogBox;

	private HorizontalPanel buttonHorizontalPanel;

	private Button cancelBtn;

	private Button selectBtn;

	// FuzzySearchListView fuzzySearchListView;
	FlexTable fuzzyTable;

	Map<RadioButton, Integer> radioButtonVsRowIdMap = new HashMap<RadioButton, Integer>();

	@UiField
	FlowPanel fuzzySearchListPanel;
	/*
	 * @UiField ScrollPanel fuzzySearchScrollPanel;
	 */

	String selectedRowId = null;

	public FuzzySearchResultView(List<String> arg0, final ReviewValidatePresenter reviewValidatePresenter, String width, String height) {
		initWidget(binder.createAndBindUi(this));
		// fuzzySearchScrollPanel.setSize("100%", "100%");

		fuzzyTable = new FlexTable();
		fuzzyTable.setWidth("100%");

		buttonHorizontalPanel = new HorizontalPanel();

		cancelBtn = new Button();
		selectBtn = new Button();
		buttonHorizontalPanel.add(selectBtn);
		buttonHorizontalPanel.add(cancelBtn);
		buttonHorizontalPanel.setSpacing(5);
		buttonHorizontalPanel.setVerticalAlignment(HasAlignment.ALIGN_TOP);
		addStyleName("overflow_scroll");
		fuzzySearchListPanel.add(buttonHorizontalPanel);
		setWidth(width);
		setHeight(height);
		fuzzySearchListPanel.add(fuzzyTable);
		fuzzySearchListPanel.addStyleName("border");

		// cancelBtn.setText("Cancel");
		// selectBtn.setText("Select");

		cancelBtn.setText(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.fuzzy_search_cancel_btn));
		selectBtn.setText(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.fuzzy_search_select_btn));

		selectBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {

				// TODO remove this hard coding and get it from hash map.
				int returnIndex = getSelectedRowId();
				FuzzySearchResultView.this.fireEvent(new FuzzySearchEvent(returnIndex));
				dialogBox.hide(true);
				ScreenMaskUtility.unmaskScreen();
				reviewValidatePresenter.setFocus();
			}

		});

		cancelBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				dialogBox.hide(true);
				ScreenMaskUtility.unmaskScreen();
				reviewValidatePresenter.setFocus();
			}

		});

	}

	public Button getSelectBtn() {
		return selectBtn;
	}

	public FlowPanel getFuzzySearchListPanel() {
		return fuzzySearchListPanel;
	}

	@Override
	public void initializeWidget() {

	}

	@Override
	public void injectEvents(HandlerManager eventBus) {

	}

	public List<String> getTbHeaderNameList() {
		return tbHeaderNameList;
	}

	public List<String> getTbValueList() {
		return tbValueList;
	}

	public void setTbHeaderNameList(List<String> tbHeaderNameList) {
		this.tbHeaderNameList = tbHeaderNameList;
	}

	public void setTbValueList(List<String> tbValueList) {
		this.tbValueList = tbValueList;
	}

	public DialogBox getDialogBox() {
		return dialogBox;
	}

	public void setDialogBox(DialogBox dialogBox) {
		this.dialogBox = dialogBox;
	}

	public void createBatchList(List<List<String>> result) {
		createHeaderColumns(result.get(0));
		setBatchList(result);
	}

	private void setBatchList(List<List<String>> data) {
		int rowIndex = 0;
		for (List<String> rowData : data) {
			if (rowIndex > 0) {
				final RadioButton radioButton = new RadioButton(new Date().toString());
				radioButton.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent arg0) {
						for (RadioButton radio : radioButtonVsRowIdMap.keySet()) {
							radio.setValue(false);
						}
						radioButton.setValue(true);
						selectedRowId = radioButtonVsRowIdMap.get(radioButton).toString();
					}
				});
				radioButtonVsRowIdMap.put(radioButton, rowIndex);
				if (rowIndex == 1) {
					radioButton.setValue(true);
					selectedRowId = "1";
				}
				fuzzyTable.setWidget(rowIndex, 0, radioButton);

				if (rowIndex % 2 == 0) {
					fuzzyTable.getRowFormatter().setStyleName(rowIndex, "oddRow");
				} else {
					fuzzyTable.getRowFormatter().setStyleName(rowIndex, "evenRow");
				}

				int columnIndex = 1;
				for (String columnData : rowData) {
					fuzzyTable.setWidget(rowIndex, columnIndex, new Label(columnData));
					columnIndex++;
				}
			}
			rowIndex++;
		}

		fuzzyTable.getRowFormatter().addStyleName(0, "rowHighlighted");
	}

	private void createHeaderColumns(List<String> arg0) {

		int widthOfHeaderCol = 94 / arg0.size();
		fuzzyTable.getCellFormatter().setWidth(0, 0, String.valueOf(6));
		fuzzyTable.setWidget(0, 0, new Label(""));
		int index = 1;
		for (String str : arg0) {
			fuzzyTable.getCellFormatter().setWidth(0, index, String.valueOf(widthOfHeaderCol));
			fuzzyTable.setWidget(0, index, new Label(str));
			index++;
		}
		fuzzyTable.getRowFormatter().setStylePrimaryName(0, "header");

	}

	public int getSelectedRowId() {
		return Integer.parseInt(selectedRowId);
	}

}
