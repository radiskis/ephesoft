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

package com.ephesoft.dcma.gwt.admin.bm.client.view.plugin;

import java.util.List;

import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin.KV_PP_EditPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.validator.ValidatableWidget;
import com.ephesoft.dcma.gwt.core.client.validator.Validator;
import com.ephesoft.dcma.gwt.core.client.validator.ValidatorFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class provides functionality to edit KV PP.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class KV_PP_EditView extends View<KV_PP_EditPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<FlexTable, KV_PP_EditView> {
	}

	/**
	 * flexEditTable FlexTable.
	 */
	@UiField
	protected FlexTable flexEditTable;

	/**
	 * editTable FlexTable.
	 */
	private FlexTable editTable;

	/**
	 * cancel Button.
	 */
	private Button cancel;

	/**
	 * save Button.
	 */
	private Button save;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Constructor.
	 */
	public KV_PP_EditView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
	}

	/**
	 * To get Flex Edit Table.
	 * 
	 * @return FlexTable
	 */
	public FlexTable getFlexEditTable() {
		return flexEditTable;
	}

	/**
	 * To set view.
	 */
	public void setView() {

		cancel = new Button();
		cancel.setText(AdminConstants.CANCEL_BUTTON);
		cancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				presenter.onCancel();
			}
		});
		save = new Button();
		save.setText(AdminConstants.OK_BUTTON);
		save.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				presenter.onSave();
			}
		});
		editTable = new FlexTable();
		editTable.setWidth("100%");
		flexEditTable.setWidget(0, 0, editTable);
		flexEditTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
	}

	/**
	 * To format the row.
	 * 
	 * @param row int
	 */
	public void formatRow(int row) {
		editTable.getCellFormatter().setWidth(row, 0, "60%");
		editTable.getFlexCellFormatter().setAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
		editTable.getCellFormatter().setWidth(row, 1, "1%");
		editTable.getCellFormatter().setWidth(row, 2, "70px");
		editTable.getCellFormatter().setWidth(row, BatchClassManagementConstants.THREE, "58%");
		editTable.getFlexCellFormatter().setAlignment(row, BatchClassManagementConstants.THREE, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_BOTTOM);
		editTable.getFlexCellFormatter().addStyleName(row, BatchClassManagementConstants.THREE, "sampleData");
		editTable.getFlexCellFormatter().addStyleName(row, 0, "bold_text");
	}

	/**
	 * To add widget.
	 * 
	 * @param row int
	 * @param column int
	 * @param widget Widget
	 */
	public void addWidget(int row, int column, Widget widget) {
		editTable.setWidget(row, column, widget);
	}

	/**
	 * To add Widget Star.
	 * 
	 * @param row int
	 * @param column int
	 */
	public void addWidgetStar(int row, int column) {
		Label star = new Label(AdminConstants.STAR);
		editTable.setWidget(row, column, star);
		star.setStyleName(BatchClassManagementConstants.FONT_RED);
	}

	/**
	 * To get save button.
	 * 
	 * @return Button
	 */
	public Button getSave() {
		return save;
	}

	/**
	 * To get cancel button.
	 * 
	 * @return Button
	 */
	public Button getCancel() {
		return cancel;
	}

	/**
	 * To add buttons.
	 * 
	 * @param row int
	 */
	public void addButtons(int row) {
		editTable.setWidget(row, 1, save);
		editTable.setWidget(row, 2, cancel);
		editTable.getFlexCellFormatter().setAlignment(row, 1, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
	}

	/**
	 * To add Text Box.
	 * 
	 * @param row int
	 * @param value String
	 * @param dataType DataType
	 * @param readOnly boolean
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> addTextBox(int row, String value, DataType dataType, boolean readOnly) {
		TextBox fieldValue = new TextBox();
		fieldValue.setReadOnly(readOnly);
		fieldValue.setText(value);
		final ValidatableWidget<TextBox> validatableTextBox = new ValidatableWidget<TextBox>(fieldValue);
		if (!readOnly) {
			validatableTextBox.addValidator((Validator) ValidatorFactory.getValidator(dataType, fieldValue));
			validatableTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					validatableTextBox.toggleValidDateBox();
				}
			});
			validatableTextBox.toggleValidDateBox();
		}

		return validatableTextBox;
	}

	/**
	 * To add Multiple Select List Box.
	 * 
	 * @param row int
	 * @param sampleValueList List<String>
	 * @param MAX_VISIBLE_ITEM_COUNT int
	 * @param value String
	 * @return ListBox
	 */
	public ListBox addMultipleSelectListBox(int row, List<String> sampleValueList, int MAX_VISIBLE_ITEM_COUNT, String value) {
		ListBox fieldValue = new ListBox(true);
		fieldValue.setVisibleItemCount(MAX_VISIBLE_ITEM_COUNT);
		for (String item : sampleValueList) {
			fieldValue.addItem(item);
		}
		String[] selectedValue = value.split(";");
		for (String string : selectedValue) {
			fieldValue.setItemSelected(sampleValueList.indexOf(string), true);
		}
		return fieldValue;
	}

	/**
	 * To add drop down.
	 * 
	 * @param row int
	 * @param sampleValueList List<String>
	 * @param selectedValue String
	 * @return ListBox
	 */
	public ListBox addDropDown(int row, List<String> sampleValueList, String selectedValue) {
		ListBox fieldValue = new ListBox();
		fieldValue.setVisibleItemCount(1);
		for (String item : sampleValueList) {
			fieldValue.addItem(item);
		}
		fieldValue.setItemSelected(sampleValueList.indexOf(selectedValue), true);
		return fieldValue;
	}

}
