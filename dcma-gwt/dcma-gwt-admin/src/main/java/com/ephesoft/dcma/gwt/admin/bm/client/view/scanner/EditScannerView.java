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

package com.ephesoft.dcma.gwt.admin.bm.client.view.scanner;

import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.scanner.EditScannerPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.validator.EmptyStringValidator;
import com.ephesoft.dcma.gwt.core.client.validator.ValidatableWidget;
import com.ephesoft.dcma.gwt.core.client.validator.Validator;
import com.ephesoft.dcma.gwt.core.client.validator.ValidatorFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class provides functionality to edit scanner view.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class EditScannerView extends View<EditScannerPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, EditScannerView> {
	}

	/**
	 * flexEditTable FlexTable.
	 */
	@UiField
	protected FlexTable flexEditTable;

	/**
	 * dataTable FlexTable.
	 */
	private FlexTable dataTable;

	/**
	 * saveButton Button.
	 */
	@UiField
	protected Button saveButton;

	/**
	 * cancelButton Button.
	 */
	@UiField
	protected Button cancelButton;

	/**
	 * scrollPanel ScrollPanel.
	 */
	@UiField
	protected ScrollPanel scrollPanel;

	/**
	 * editScannerViewPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel editScannerViewPanel;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * To add text box.
	 * 
	 * @param row int
	 * @param value String
	 * @param dataType String
	 * @param readOnly boolean
	 * @param isMandatory boolean
	 * @param htmlID String
	 * @param elementMap Map<String, Widget>
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> addTextBox(int row, String value, String dataType, boolean readOnly, boolean isMandatory,
			String htmlID, Map<String, Widget> elementMap) {
		TextBox fieldValue = new TextBox();
		elementMap.put(htmlID, fieldValue);
		fieldValue.setReadOnly(readOnly);
		fieldValue.setText(value);
		scrollPanel.setStyleName(AdminConstants.SCROLL_PANEL_HEIGHT);

		final ValidatableWidget<TextBox> validatableTextBox = new ValidatableWidget<TextBox>(fieldValue);
		if (!readOnly && dataType != null && DataType.getValuesAsListString().contains(dataType.toLowerCase())) {
			validatableTextBox.addValidator((Validator) ValidatorFactory.getValidator(DataType.getDataType(dataType.toUpperCase()),
					fieldValue));
			validatableTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					validatableTextBox.toggleValidDateBox();
				}
			});
			validatableTextBox.toggleValidDateBox();
		}
		if (isMandatory) {
			validatableTextBox.addValidator(new EmptyStringValidator(fieldValue));
			validatableTextBox.toggleValidDateBox();
		}

		return validatableTextBox;
	}

	/**
	 * To add Drop Down.
	 * 
	 * @param row int
	 * @param sampleValueList List<String>
	 * @param selectedValue String
	 * @param htmlID String
	 * @param elementMap Map<String, Widget>
	 * @return ListBox
	 */
	public ListBox addDropDown(int row, List<String> sampleValueList, String selectedValue, String htmlID,
			Map<String, Widget> elementMap) {
		ListBox fieldValue = new ListBox();
		elementMap.put(htmlID, fieldValue);
		fieldValue.getElement().setId(htmlID);
		fieldValue.setVisibleItemCount(1);
		for (String item : sampleValueList) {
			fieldValue.addItem(item);
		}
		if (selectedValue == null) {
			fieldValue.setItemSelected(0, true);
		} else {
			fieldValue.setItemSelected(sampleValueList.indexOf(selectedValue), true);
		}

		return fieldValue;
	}

	/**
	 * Constructor.
	 */
	public EditScannerView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
	}

	/**
	 * To set the view.
	 */
	public void setView() {
		dataTable = new FlexTable();
		dataTable.setWidth("100%");
		flexEditTable.setWidget(0, 0, dataTable);
		flexEditTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
		saveButton.setText(AdminConstants.OK_BUTTON);
		cancelButton.setText(AdminConstants.CANCEL_BUTTON);
		saveButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		cancelButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		editScannerViewPanel.setSpacing(BatchClassManagementConstants.FIVE);
	}

	/**
	 * To add Widget.
	 * 
	 * @param row int
	 * @param column int
	 * @param widget Widget
	 */
	public void addWidget(int row, int column, Widget widget) {
		dataTable.setWidget(row, column, widget);
	}

	/**
	 * To add Widget Star.
	 * 
	 * @param row int
	 * @param column int
	 */
	public void addWidgetStar(int row, int column) {
		Label star = new Label(AdminConstants.STAR);
		dataTable.setWidget(row, column, star);
		star.setStyleName(BatchClassManagementConstants.FONT_RED);
	}

	/**
	 * To perform operations on Save Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("saveButton")
	public void onSaveClicked(ClickEvent clickEvent) {
		presenter.onSave();
	}

	/**
	 * To perform operations on cancel Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("cancelButton")
	public void onCancelClicked(ClickEvent clickEvent) {
		presenter.onCancel();
	}

	/**
	 * To get data table.
	 * 
	 * @return FlexTable
	 */
	public FlexTable getDataTable() {
		return dataTable;
	}

	/**
	 * To set data table.
	 * 
	 * @param dataTable FlexTable
	 */
	public void setDataTable(FlexTable dataTable) {
		this.dataTable = dataTable;
	}

	/**
	 * To set flex Edit Table.
	 * 
	 * @param flexEditTable FlexTable
	 */
	public void setflexEditTable(FlexTable flexEditTable) {
		this.flexEditTable = flexEditTable;
	}

	/**
	 * To format Row.
	 * 
	 * @param row int
	 */
	public void formatRow(int row) {
		dataTable.getCellFormatter().setWidth(row, 0, "40%");
		dataTable.getFlexCellFormatter().setAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
		dataTable.getCellFormatter().setWidth(row, 1, "1%");
		dataTable.getCellFormatter().setWidth(row, 2, "70px");
		dataTable.getCellFormatter().setWidth(row, BatchClassManagementConstants.THREE, "58%");
		dataTable.getFlexCellFormatter().setAlignment(row, BatchClassManagementConstants.THREE, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_BOTTOM);
		dataTable.getFlexCellFormatter().addStyleName(row, BatchClassManagementConstants.THREE, "sampleData");
		dataTable.getFlexCellFormatter().addStyleName(row, 0, "bold_text");
	}

}
