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

package com.ephesoft.dcma.gwt.rv.client.view;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.batch.schema.Column;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Field;
import com.ephesoft.dcma.batch.schema.HeaderRow;
import com.ephesoft.dcma.batch.schema.Row;
import com.ephesoft.dcma.batch.schema.DataTable.Rows;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleCommonConstants;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.client.validator.RegExValidator;
import com.ephesoft.dcma.gwt.core.client.validator.ValidatableWidget;
import com.ephesoft.dcma.gwt.core.shared.BatchDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.PointCoordinate;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.ephesoft.dcma.gwt.rv.client.event.DocExpandEvent;
import com.ephesoft.dcma.gwt.rv.client.event.DocExpandEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyDownEvent;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyDownEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.TableViewDisplayEvent;
import com.ephesoft.dcma.gwt.rv.client.event.ValidationFieldChangeEvent;
import com.ephesoft.dcma.gwt.rv.client.i18n.ReviewValidateConstants;
import com.ephesoft.dcma.gwt.rv.client.i18n.ReviewValidateMessages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

public class TableExtractionView extends RVBasePanel {

	interface Binder extends UiBinder<DockLayoutPanel, TableExtractionView> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Reference of ScrollPanel.
	 */
	@UiField
	protected ScrollPanel scrollPanel;

	/**
	 * Reference of VerticalPanel.
	 */
	@UiField
	protected VerticalPanel verticalPanel;

	/**
	 * Reference of Button.
	 */
	@UiField
	protected Button addNewTableButton;

	/**
	 * Reference of Map<String, FlexTable>. Stores the FlexTable against the table name.
	 */
	private final Map<String, FlexTable> tableNameVsViewMap = new LinkedHashMap<String, FlexTable>();

	/**
	 * Reference of Map<Button, String>.
	 */
	private final Map<Button, String> tableNameVsButtonMap = new LinkedHashMap<Button, String>();

	/**
	 * Reference of Map<String, List<CheckBox>>. Stores list of CheckBoxes for header of table against the table name.
	 */
	private final Map<String, List<CheckBox>> tableNameVsCheckBoxMap = new LinkedHashMap<String, List<CheckBox>>();

	/**
	 * Reference of Map<Integer, Coordinates>. Stores Coordinates of drawn rectangle on image against column number for selected table.
	 */
	private final Map<Integer, Coordinates> columnVsCoordinates = new LinkedHashMap<Integer, Coordinates>();

	/**
	 * Reference of Map<String, List<String>>. Stores the list of column patterns against table name.
	 */
	private final Map<String, List<String>> tableNameVsColumnPattern = new LinkedHashMap<String, List<String>>();

	/**
	 * Reference of enum. Indicates the action selected by user.
	 */
	private enum Action {
		INSERT, DELETE, DELETE_ALL;
	};

	/**
	 * Variable to indicate Selected row number in table.
	 */
	private int selectedRowNumber;

	/**
	 * Variable to indicate the selected column number in table.
	 */
	private int selectedColumnNumber;

	/**
	 * Reference of int to indicate selected row number for manual extraction.
	 */
	private int selRowForManualExtraction;

	/**
	 * Reference of String to indicate name of selected data table.
	 */
	private String selectedDataTableName;

	/**
	 * Reference of DataTable to indicate selected DataTable.
	 */
	private DataTable selDataTable;

	/**
	 * Reference of List<String>.
	 */
	private List<String> columnPattern = null;

	/**
	 * Reference of Document.
	 */
	private Document selectedDocument;

	/**
	 * Reference of Button.
	 */
	private Button insertRowButton;

	/**
	 * Reference of Button.
	 */
	private Button deleteRowButton;

	/**
	 * Reference of Button.
	 */
	private Button deleteAllRowButton;

	/**
	 * Reference of Button.
	 */
	private Button manualExtractionButton;

	/**
	 * Reference of SuggestBox.
	 */
	private TextBox selectedTextBox;

	/**
	 * Reference of String. Label for start manual table extraction button.
	 */
	private final String startExtractionLabel;

	/**
	 * Reference of String. Label for manual table extraction button.
	 */
	private final String manualExtractionLabel;

	/**
	 * Reference of String. Indicates table name of table locked for manual extraction.
	 */
	private String lockedTableName;

	/**
	 * Reference of List<CheckBox>.
	 */
	private final List<CheckBox> activeCheckBoxes = new ArrayList<CheckBox>();

	/**
	 * Reference of Integer. Represents maximum number of rows with invalid data for a column.
	 */
	private final static int NUBER_OF_ROWS_WITH_INVALID_COLUMN = 3;
	private final static String ALTERNATE_STRING_VALUE = LocaleDictionary.get().getConstantValue(
			ReviewValidateConstants.alternate_value);
	private final static String SEPERATOR = ReviewValidateConstants.seperator;

	private final static String GAP_BETWEEN_BUTTONS = "5px";

	/**
	 * Constructor for this class to initialize resources.
	 */
	public TableExtractionView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		addNewTableButton.setText(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.ADD_NEW_TABLE_BUTTON));
		manualExtractionLabel = LocaleDictionary.get().getConstantValue(ReviewValidateConstants.MANUAL_EXTRACTION);
		startExtractionLabel = LocaleDictionary.get().getConstantValue(ReviewValidateConstants.START_EXTRACTION);
	}

	@Override
	public void initializeWidget() {
		// Used to Initialize UI Widgets.
	}

	@Override
	public void injectEvents(final HandlerManager eventBus) {
		eventBus.addHandler(DocExpandEvent.TYPE, new DocExpandEventHandler() {

			@Override
			public void onExpand(final DocExpandEvent event) {
				createTableView(event.getDocument());
			}
		});
		eventBus.addHandler(RVKeyDownEvent.TYPE, new RVKeyDownEventHandler() {

			@Override
			public void onKeyDown(final RVKeyDownEvent event) {
				Button selectedButton;
				if (event.getEvent().isControlKeyDown()) {
					switch (event.getEvent().getNativeEvent().getKeyCode()) {
						case 'i':
						case 'I':
							event.getEvent().getNativeEvent().preventDefault();
							selectedButton = getSelectedButton(insertRowButton.getText());
							if (selectedButton != null) {
								selectedButton.click();
							}
							break;
						case 'j':
						case 'J':
							event.getEvent().getNativeEvent().preventDefault();
							selectedButton = getSelectedButton(deleteRowButton.getText());
							if (selectedButton != null) {
								selectedButton.click();
							}
							break;
						case 'k':
						case 'K':
							event.getEvent().getNativeEvent().preventDefault();
							traverseDownTables();
							break;
						case 'u':
						case 'U':
							event.getEvent().getNativeEvent().preventDefault();
							selectedButton = getSelectedButton(deleteAllRowButton.getText());
							if (selectedButton != null) {
								selectedButton.click();
							}
							break;
						case '3':
							event.getEvent().getNativeEvent().preventDefault();
							addNewTableButton.click();
							break;
						case 'y':
						case 'Y':
							event.getEvent().getNativeEvent().preventDefault();
							selectedButton = getSelectedButton(manualExtractionLabel);
							if (selectedButton == null) {
								selectedButton = getSelectedButton(startExtractionLabel);
							}
							if (selectedButton != null) {
								selectedButton.click();
							}
							break;
						default:
							break;
					}
				}
			}
		});
	}

	protected void traverseDownTables() {
		boolean isNextTable = false;
		boolean isLastTable = true;
		Button firstKey = null;
		boolean isFirstTable = true;
		Button focussedKey = null;
		if (tableNameVsButtonMap != null && !tableNameVsButtonMap.isEmpty()) {
			if (tableNameVsButtonMap.size() == 1) {
				return;
			}
			for (Button key : tableNameVsButtonMap.keySet()) {
				if (isFirstTable) {
					firstKey = key;
					isFirstTable = false;
				}
				if (selectedDataTableName == null || selectedDataTableName.isEmpty()) {
					focussedKey = key;
					break;
				}
				if (selectedDataTableName.equalsIgnoreCase(tableNameVsButtonMap.get(key))) {
					isNextTable = true;
				}
				if (isNextTable && selectedDataTableName != tableNameVsButtonMap.get(key)) {
					isLastTable = false;
					focussedKey = key;
					break;
				}
			}
			if (isLastTable) {
				focussedKey = firstKey;
			}
			selectedDataTableName = tableNameVsButtonMap.get(focussedKey);
			selectedRowNumber = 1;
			setFocus(focussedKey);
		}
	}

	private void setFocus(final Button focussedButton) {
		if (focussedButton instanceof Button) {
			focussedButton.setFocus(Boolean.TRUE);
		}
	}

	public void setFocus() {
		FlexTable selectedView = tableNameVsViewMap.get(selectedDataTableName);
		SuggestBox suggestBox = (SuggestBox) selectedView.getWidget(selectedRowNumber, 0);
		suggestBox.setFocus(true);
	}

	private Button getSelectedButton(final String buttonLabel) {
		Button selectedButton = null;
		if (buttonLabel != null && !buttonLabel.isEmpty()) {
			for (Button button : tableNameVsButtonMap.keySet()) {
				if (button.getText().equalsIgnoreCase(buttonLabel)
						&& tableNameVsButtonMap.get(button).equalsIgnoreCase(selectedDataTableName)) {
					selectedButton = button;
					break;
				}
			}
		}
		return selectedButton;
	}

	private void setTableUI(final FlexTable flexTable, final DataTable dataTable) {
		ScrollPanel panel = new ScrollPanel();
		if (dataTable.getRows().getRow().size() == 0) {
			setEmptyTableView(flexTable, dataTable.getHeaderRow());
		}

		panel.add(flexTable);
		panel.setHeight("150px");
		panel.setWidth("400px");
		panel.setTitle(dataTable.getName());
		HorizontalPanel hPanel = new HorizontalPanel();
		insertRowButton = new Button();
		deleteRowButton = new Button();
		deleteAllRowButton = new Button();
		manualExtractionButton = new Button();
		setButtonsText();

		onInsertButtonClicked(insertRowButton, dataTable);
		onDeleteButtonClicked(deleteRowButton, dataTable);
		onDeleteAllButtonClicked(deleteAllRowButton, dataTable);
		onManualExtractionButtonClicked(manualExtractionButton, dataTable);

		final String tableName = dataTable.getName();
		tableNameVsButtonMap.put(insertRowButton, tableName);
		tableNameVsButtonMap.put(deleteRowButton, tableName);
		tableNameVsButtonMap.put(deleteAllRowButton, tableName);
		tableNameVsButtonMap.put(manualExtractionButton, tableName);
		Label label = new Label();
		label.setWidth(GAP_BETWEEN_BUTTONS);
		hPanel.add(insertRowButton);
		hPanel.add(label);
		label = new Label();
		label.setWidth(GAP_BETWEEN_BUTTONS);
		hPanel.add(deleteRowButton);
		hPanel.add(label);
		label = new Label();
		label.setWidth(GAP_BETWEEN_BUTTONS);
		hPanel.add(deleteAllRowButton);
		hPanel.add(label);
		hPanel.add(manualExtractionButton);
		Label emptyLabel = new Label();
		emptyLabel.setHeight("20px");
		verticalPanel.add(emptyLabel);
		emptyLabel = new Label();
		emptyLabel.setText(dataTable.getName());
		verticalPanel.add(emptyLabel);
		verticalPanel.add(hPanel);
		emptyLabel = new Label();
		emptyLabel.setHeight("10px");
		verticalPanel.add(emptyLabel);
		verticalPanel.add(panel);

	}

	private void onManualExtractionButtonClicked(Button manualExtractionButton, final DataTable dataTable) {

		manualExtractionButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(final ClickEvent clickEvent) {
				ScreenMaskUtility.maskScreen(LocaleDictionary.get().getConstantValue(LocaleCommonConstants.msg_mask_wait));
				Button manualExtractionButton = null;
				// boolean isValid = true;
				if (clickEvent.getSource() instanceof Button) {
					manualExtractionButton = (Button) clickEvent.getSource();
				}
				if (presenter.isManualTableExtraction()
						&& !tableNameVsButtonMap.get(manualExtractionButton).equalsIgnoreCase(lockedTableName)) {
					ScreenMaskUtility.unmaskScreen();
					exitManualExtractionConfirmation(LocaleDictionary.get().getMessageValue(
							ReviewValidateMessages.EXIT_MANUAL_EXTARCTION_CONFIRMATION, lockedTableName), LocaleDictionary.get()
							.getConstantValue(ReviewValidateConstants.MANUAL_EXTRACTION), manualExtractionButton);
				} else {
					selDataTable = dataTable;
					if (manualExtractionButton.getText().equals(manualExtractionLabel)) {
						if (dataTable.getRows().getRow() == null || dataTable.getRows().getRow().isEmpty()
								|| selectedDataTableName == null
								|| selectedDataTableName != tableNameVsButtonMap.get(manualExtractionButton)) {
							selectedRowNumber = 0;
						}
						selectedDataTableName = tableNameVsButtonMap.get(manualExtractionButton);
						tableNameVsViewMap.get(selectedDataTableName).getFlexCellFormatter().setColSpan(1, 0, 1);
						lockedTableName = selectedDataTableName;
						insertDeleteRow(Action.INSERT, 1, manualExtractionButton);
						selRowForManualExtraction = selectedRowNumber;
						getCheckBoxesForSelectedTable(selectedDataTableName);
						showCheckBoxes();
						presenter.setManualTableExtraction(Boolean.TRUE);
						manualExtractionButton.setText(startExtractionLabel);

					} else {
						if (columnVsCoordinates == null || columnVsCoordinates.isEmpty()) {
							showConfirmationDialog(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.NO_COLUMN_SELECTED),
									LocaleDictionary.get().getConstantValue(ReviewValidateConstants.MANUAL_EXTRACTION),
									manualExtractionButton);
						} else {
							presenter.getTableData(columnVsCoordinates, selectedDocument.getType(), selDataTable);
							ScreenMaskUtility.unmaskScreen();
							columnVsCoordinates.clear();
							manualExtractionButton.setText(manualExtractionLabel);
						}
					}
				}
				ScreenMaskUtility.unmaskScreen();
			}
		});
	}

	private void onDeleteAllButtonClicked(final Button deleteAllRowButton, final DataTable dataTable) {
		deleteAllRowButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(final ClickEvent clickEvent) {
				Button deleteAllButton = null;
				if (clickEvent.getSource() instanceof Button) {
					deleteAllButton = (Button) clickEvent.getSource();
				}
				doAction(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.DELETE_ALL_ROW_TITLE), deleteAllButton,
						dataTable);
			}
		});
	}

	private void onDeleteButtonClicked(final Button deleteRowButton, final DataTable dataTable) {
		deleteRowButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(final ClickEvent clickEvent) {
				Button deleteButton = null;
				if (clickEvent.getSource() instanceof Button) {
					deleteButton = (Button) clickEvent.getSource();
				}
				doAction(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.DELETE_ROW_TITLE), deleteButton, dataTable);
			}

		});
	}

	private void onInsertButtonClicked(final Button insertRowButton, final DataTable dataTable) {
		insertRowButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(final ClickEvent clickEvent) {
				Button insertButton = null;
				if (clickEvent.getSource() instanceof Button) {
					insertButton = (Button) clickEvent.getSource();
				}
				doAction(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.INSERT_ROW), insertButton, dataTable);
			}
		});
	}

	private void doAction(String title, Button button, DataTable dataTable) {
		if (presenter.isManualTableExtraction()) {
			exitManualExtractionConfirmation(LocaleDictionary.get().getMessageValue(
					ReviewValidateMessages.EXIT_MANUAL_EXTARCTION_CONFIRMATION, lockedTableName), title, button);
		} else {
			ScreenMaskUtility.maskScreen(LocaleDictionary.get().getConstantValue(LocaleCommonConstants.msg_mask_wait));
			if (tableNameVsButtonMap != null && tableNameVsButtonMap.size() != 0
					&& !tableNameVsButtonMap.get(button).equalsIgnoreCase(selectedDataTableName)
					|| dataTable.getRows().getRow().isEmpty()) {
				selectedRowNumber = 0;
			}
			selectedDataTableName = tableNameVsButtonMap.get(button);
			if (button.getText().equals(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.INSERT_ROW_BUTTON))) {
				insertNewRow(button);
			} else if (button.getText().equals(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.DELETE_ROW_BUTTON))) {
				deleteRowAction(button);
			} else if (button.getText().equals(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.DELETE_ALL_ROW_BUTTON))) {
				deleteAllRowAction(button);
			}
		}
		ScreenMaskUtility.unmaskScreen();
	}

	/**
	 * This method shows the confirmation dialog to exit the manual extraction of table.
	 * 
	 * @param message {@link String} message to be shown in confirmation dialog.
	 * @param title {@link String} title of the confirmation dialog.
	 * @param button {@link Button} clicked button. Focus is set on this button after exiting manual extraction.
	 */
	private void exitManualExtractionConfirmation(final String message, final String title, final Button button) {
		final ConfirmationDialog confirmationDialog = new ConfirmationDialog();
		confirmationDialog.setMessage(message);
		confirmationDialog.setDialogTitle(title);
		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				confirmationDialog.hide(true);
				selectedRowNumber = selRowForManualExtraction;
				selectedDataTableName = selDataTable.getName();
				insertDeleteRow(Action.DELETE, button);
				undoManualExtractionChanges();
				selectedDataTableName = tableNameVsButtonMap.get(button);
				button.setFocus(Boolean.TRUE);
			}

			@Override
			public void onCancelClick() {
				button.setFocus(Boolean.TRUE);
				confirmationDialog.hide(true);
			}
		});
		confirmationDialog.show();
		confirmationDialog.center();
		confirmationDialog.okButton.setFocus(true);
	}

	public void undoManualExtractionChanges() {
		presenter.setManualTableExtraction(Boolean.FALSE);
		hideCheckBoxes();
		for (Button button : tableNameVsButtonMap.keySet()) {
			if (button.getText().equalsIgnoreCase(startExtractionLabel)) {
				button.setText(manualExtractionLabel);
				break;
			}
		}
		columnVsCoordinates.clear();
	}

	/**
	 * This method adds the rows extracted from image table to the selected DataTable.
	 * 
	 * @param rowList {@link List<Row>} List of rows extracted from image table.
	 */
	public void mergeRows(final List<Row> rowList) {
		if (rowList == null || rowList.isEmpty()) {
			ScreenMaskUtility.unmaskScreen();
			showCheckBoxes();
			return;
		}
		FlexTable flexTable = tableNameVsViewMap.get(selDataTable.getName());
		columnPattern = tableNameVsColumnPattern.get(selDataTable.getName());
		selectedRowNumber = selRowForManualExtraction;
		Row selRow = selDataTable.getRows().getRow().get(selectedRowNumber - 1);
		selRow.setMannualExtraction(Boolean.TRUE);
		List<Integer> invalidRowsForColumns = new ArrayList<Integer>();
		for (int colNo = 0; colNo < selDataTable.getHeaderRow().getColumns().getColumn().size(); colNo++) {
			invalidRowsForColumns.add(0);
		}
		boolean endOfTable = false;
		int counter = 1;
		int index = 0;
		for (Row row : rowList) {
			endOfTable = false;
			index = 0;
			if (counter == 1) {
				selRow.setRowCoordinates(row.getRowCoordinates());
				counter++;
			} else {
				selDataTable.getRows().getRow().add(selectedRowNumber, row);
				selectedRowNumber++;
				selRow = row;
				selRow.setMannualExtraction(Boolean.TRUE);
			}
			for (Column column : selRow.getColumns().getColumn()) {
				column.setValidationRequired(Boolean.valueOf(activeCheckBoxes.get(index).getValue()));
				if (column.isValidationRequired()) {
					if (columnPattern != null && columnPattern.get(index) != null && !columnPattern.get(index).isEmpty()) {
						if (column.getValue() == null || !column.getValue().matches(columnPattern.get(index))) {
							invalidRowsForColumns.set(index, invalidRowsForColumns.get(index) + 1);
						} else {
							invalidRowsForColumns.set(index, 0);
						}
					} else {
						invalidRowsForColumns.set(index, 0);
					}
				} else {
					invalidRowsForColumns.set(index, 0);
				}
				column.setPage(presenter.page.getIdentifier());
				index++;
			}
			for (Integer invalidRowsForColumn : invalidRowsForColumns) {
				if (invalidRowsForColumn == NUBER_OF_ROWS_WITH_INVALID_COLUMN) {
					endOfTable = true;
				}
			}
			if (endOfTable) {
				break;
			}
		}
		addHeaderColumns(selDataTable.getHeaderRow().getColumns().getColumn(), flexTable);
		createTableList(flexTable, selDataTable.getRows().getRow(), selDataTable.getHeaderRow().getColumns().getColumn(),
				selDataTable, selectedDocument);
		hideCheckBoxes();
		presenter.setManualTableExtraction(Boolean.FALSE);
		ScreenMaskUtility.unmaskScreen();
	}

	public void showCheckBoxes() {
		for (CheckBox chkBox : activeCheckBoxes) {
			chkBox.setVisible(Boolean.TRUE);
		}
	}

	public void hideCheckBoxes() {
		for (CheckBox chkBox : activeCheckBoxes) {
			chkBox.setVisible(Boolean.FALSE);
		}
		activeCheckBoxes.clear();
	}

	public void getCheckBoxesForSelectedTable(final String selectedDataTableName) {
		if (selectedDataTableName == null || selectedDataTableName.isEmpty()) {
			return;
		}
		activeCheckBoxes.addAll(tableNameVsCheckBoxMap.get(selectedDataTableName));
	}

	private void addHeaderColumns(final List<Column> columnList, final FlexTable flexTable) {
		tableNameVsCheckBoxMap.get(selectedDataTableName).clear();
		int columnNumber = 0;
		for (Column headerColumn : columnList) {
			HorizontalPanel headerRowPanel = new HorizontalPanel();
			Label headerText = new Label(headerColumn.getName());
			headerRowPanel.add(headerText);
			headerRowPanel.setWidth("80px");
			CheckBox validationCheckBox = new CheckBox();
			validationCheckBox.setVisible(Boolean.FALSE);
			headerRowPanel.add(validationCheckBox);
			headerRowPanel.setCellVerticalAlignment(validationCheckBox, HasVerticalAlignment.ALIGN_MIDDLE);
			tableNameVsCheckBoxMap.get(selectedDataTableName).add(validationCheckBox);
			flexTable.setWidget(0, columnNumber, headerRowPanel);
			flexTable.getCellFormatter().addStyleName(0, columnNumber, "wordWrap");
			columnNumber++;
		}
	}

	public void createTableList(final FlexTable flexTable, final List<Row> rows, final List<Column> columnList,
			final DataTable dataTable, final Document document) {
		setTableList(flexTable, rows, columnList, dataTable, document);
	}

	private void setTableList(final FlexTable flexTable, final List<Row> rows, final List<Column> columnList,
			final DataTable inputDataTable, final Document inputDocument) {
		selectedDataTableName = inputDataTable.getName();
		int index = 1;
		for (Row row : rows) {
			final int presentRowNumber = index;
			if (row != null && row.getColumns() != null && row.getColumns().getColumn() != null
					&& !row.getColumns().getColumn().isEmpty()) {
				final List<Column> columns = row.getColumns().getColumn();
				int index1 = 0;
				final ArrayList<Coordinates> rowCoordinates = new ArrayList<Coordinates>();
				rowCoordinates.add(row.getRowCoordinates());
				for (final Column column : columnList) {
					final int presentColumnNumber = index1;
					final Column selectedColumn = columns.get(index1);
					columnPattern = tableNameVsColumnPattern.get(inputDataTable.getName());
					List<String> alternateValuesSet = getAlternateValueSet(selectedColumn);
					MultiWordSuggestOracle oracle = setMultiWordOracle(selectedColumn.getValue(), alternateValuesSet);
					final SuggestBox suggestBox = new SuggestBox(oracle);
					suggestBox.addStyleName("tableViewListBox");
					suggestBox.setWidth("100%");

					final ValidatableWidget<SuggestBox> validatableSuggestBox = new ValidatableWidget<SuggestBox>(suggestBox);

					if ((columnPattern != null && columnPattern.size() > index1)
							&& (!row.isMannualExtraction() || selectedColumn.isValidationRequired())) {
						validatableSuggestBox.addValidator(new RegExValidator(columnPattern.get(index1), suggestBox));
					}

					suggestBox.addValueChangeHandler(new ValueChangeHandler<String>() {

						@Override
						public void onValueChange(final ValueChangeEvent<String> event) {
							selectedColumn.setValue(event.getValue());
							selectedColumn.setValid(validatableSuggestBox.validate());
							validatableSuggestBox.toggleValidDateBox();
						}
					});

					suggestBox.getTextBox().addFocusHandler(new FocusHandler() {

						@Override
						public void onFocus(FocusEvent event) {
							TableExtractionView.this.fireEvent(new ValidationFieldChangeEvent(selectedColumn, rowCoordinates));
							if (event.getSource() instanceof TextBox) {
								selectedTextBox = (TextBox) event.getSource();
							}
							selectedRowNumber = presentRowNumber;
							selectedColumnNumber = presentColumnNumber;
							selectedDocument = inputDocument;
							if (selectedDataTableName != null) {
								clearSelectedRowStyle(tableNameVsViewMap.get(selectedDataTableName));
							}
							if (!presenter.isManualTableExtraction()) {
								selDataTable = inputDataTable;
							}
							selectedDataTableName = inputDataTable.getName();
							addSelectedRowStyle(flexTable, rows, columnList, selectedRowNumber);

						}
					});

					suggestBox.addSelectionHandler(new SelectionHandler<Suggestion>() {

						@Override
						public void onSelection(SelectionEvent<Suggestion> suggestion) {
							String inputString = suggestion.getSelectedItem().getReplacementString();
							setSuggestBoxEvents(selectedColumn, inputString, validatableSuggestBox);
							ValueChangeEvent.fire(suggestBox, inputString);
						}
					});

					suggestBox.setText(selectedColumn.getValue());
					if (!selectedColumn.isValid()) {
						validatableSuggestBox.getWidget().addStyleName("dateBoxFormatError");
					}
					if (row.isMannualExtraction()) {
						ValueChangeEvent.fire(suggestBox, selectedColumn.getValue());
					}
					flexTable.setWidget(index, index1, validatableSuggestBox.getWidget());
					index1++;
				}
				index++;
			}
		}
	}

	private void setSuggestBoxEvents(final Column column, final String originalString,
			final ValidatableWidget<SuggestBox> validatableSuggestBox) {
		int pos = originalString.lastIndexOf(SEPERATOR);
		int index = 0;
		String inputString = originalString;
		if (!(pos < 0)) {
			index = Integer.parseInt(inputString.substring(pos + ALTERNATE_STRING_VALUE.length() + SEPERATOR.length(), inputString
					.length()));
			inputString = inputString.substring(0, pos);
		}
		validatableSuggestBox.getWidget().setValue(inputString);
		validatableSuggestBox.toggleValidDateBox();
		CoordinatesList coordinatesList = column.getCoordinatesList();
		int count = 0;
		if (column.getAlternateValues() != null) {
			List<Field> alternativeFieldList = column.getAlternateValues().getAlternateValue();
			for (Field alternateField : alternativeFieldList) {
				if (pos < 0) {
					if (alternateField.getValue().equals(inputString)) {
						TableExtractionView.this.fireEvent(new ValidationFieldChangeEvent(alternateField));
						coordinatesList = alternateField.getCoordinatesList();
					}
				} else {
					if (alternateField.getValue().equals(inputString)) {
						if (count == index) {
							TableExtractionView.this.fireEvent(new ValidationFieldChangeEvent(alternateField));
							coordinatesList = alternateField.getCoordinatesList();
						}
						count++;
					}
				}

			}
		}
		if (column.getValue().equals(originalString)) {
			TableExtractionView.this.fireEvent(new ValidationFieldChangeEvent(column));
		}
		column.setCoordinatesList(coordinatesList);
	}

	private MultiWordSuggestOracle setMultiWordOracle(final String value, final List<String> alternateValues) {
		MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		String actualvalue = null == value ? "" : value;
		oracle.add(actualvalue);
		for (String altValue : alternateValues) {
			oracle.add(altValue);
		}
		return oracle;
	}

	private List<String> getAlternateValueSet(final Column field) {
		Map<String, Integer> alternateValues = new HashMap<String, Integer>();
		field.getName();
		if (field.getAlternateValues() != null) {
			for (Field alternateField : field.getAlternateValues().getAlternateValue()) {
				if (alternateValues.containsKey(alternateField.getValue())) {
					int nextVal = alternateValues.get(alternateField.getValue()).intValue();
					nextVal++;
					alternateValues.remove(alternateField.getValue());
					alternateValues.put(alternateField.getValue(), Integer.valueOf(nextVal));
				} else {
					alternateValues.put(alternateField.getValue(), 0);
				}
			}
		}
		Iterator<Map.Entry<String, Integer>> iterator = alternateValues.entrySet().iterator();
		List<String> alternateValuesSet = new ArrayList<String>();
		while (iterator.hasNext()) {
			Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>) iterator.next();
			if (pair.getValue().intValue() != 0) {
				for (int k = 0; k <= pair.getValue().intValue(); k++) {
					alternateValuesSet.add(pair.getKey() + SEPERATOR + ALTERNATE_STRING_VALUE + k);
				}
			} else {
				alternateValuesSet.add(pair.getKey());
			}
		}
		return alternateValuesSet;
	}

	private void addSelectedRowStyle(final FlexTable flexTable, final List<Row> rowList, final List<Column> columnList,
			final int selectedRowNumber) {
		int index = 1;
		for (Row row : rowList) {
			if (row != null && row.getColumns() != null && row.getColumns().getColumn() != null
					&& !row.getColumns().getColumn().isEmpty()) {
				if (selectedRowNumber != index) {
					for (int index1 = 0; index1 < columnList.size(); index1++) {
						flexTable.getWidget(index, index1).removeStyleName(ReviewValidateConstants.ROW_SELECTION_STYLE);
					}
				} else {
					for (int index1 = 0; index1 < columnList.size(); index1++) {
						flexTable.getWidget(index, index1).addStyleName(ReviewValidateConstants.ROW_SELECTION_STYLE);
					}
				}
			}
			index++;
		}
	}

	private void clearSelectedRowStyle(final FlexTable flexTable) {
		for (int i = 0; i < flexTable.getRowCount(); i++) {
			for (int j = 0; j < flexTable.getCellCount(i); j++) {
				if (flexTable.getWidget(i, j) != null) {
					flexTable.getWidget(i, j).removeStyleName(ReviewValidateConstants.ROW_SELECTION_STYLE);
				}
			}
		}
	}

	public void createRadioOption(final Button button) {
		List<String> radioButtonText = new ArrayList<String>();
		radioButtonText.add(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.INSERT_BEFORE));
		radioButtonText.add(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.INSERT_AFTER));
		final RadioButtonDialogView radioButtonDialogView = new RadioButtonDialogView(radioButtonText, LocaleDictionary.get()
				.getMessageValue(ReviewValidateMessages.INSERT_ROW));
		radioButtonDialogView.addDialogListener(new RadioButtonDialogView.DialogListener() {

			@Override
			public void onOkClick(final int radioButtonNumber) {
				radioButtonDialogView.hide();
				if (radioButtonNumber == -1) {
					showConfirmationDialog(LocaleDictionary.get().getMessageValue(
							ReviewValidateMessages.NO_RADIO_BUTTON_SELECTED_ERROR), LocaleDictionary.get().getMessageValue(
							ReviewValidateMessages.INSERT_ROW), button);
					return;
				}
				insertDeleteRow(Action.INSERT, radioButtonNumber, button);
			}

			@Override
			public void onCancelClick() {
				radioButtonDialogView.hide();
				setFocus();
			}
		});
		radioButtonDialogView.center();
		radioButtonDialogView.okButton.setFocus(true);
		radioButtonDialogView.show();
	}

	public void insertNewRow(final Button button) {
		if (selectedRowNumber == 0) {
			if (checkDataPresent()) {
				showConfirmationDialog(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.NO_ROW_SELECTED),
						LocaleDictionary.get().getMessageValue(ReviewValidateMessages.INSERT_ROW), button);
			} else {
				selectedRowNumber = 1;
				FlexTable flexTable = tableNameVsViewMap.get(selectedDataTableName);
				flexTable.getFlexCellFormatter().setColSpan(1, 0, 1);
				insertDeleteRow(Action.INSERT, button);
			}
		} else {
			createRadioOption(button);
		}
	}

	private void showConfirmationDialog(final String message, final String title, final Button button) {
		String confirmationTitle = title;
		if (title == null || title.isEmpty()) {
			confirmationTitle = "error";
		}
		final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.confirmationDialog(message, confirmationTitle);
		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				confirmationDialog.hide(true);
				setFocus(button);
			}

			@Override
			public void onCancelClick() {
				// TODO Auto-generated method stub
			}
		});

	}

	public void deleteRowAction(final Button button) {
		if (selectedRowNumber != 0) {
			final ConfirmationDialog confirmationDialog = new ConfirmationDialog();
			confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.DELETE_ROW_CONFIRMATION));
			confirmationDialog.setText(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.DELETE_ROW_TITLE));
			confirmationDialog.addDialogListener(new DialogListener() {

				@Override
				public void onOkClick() {
					confirmationDialog.hide();
					insertDeleteRow(Action.DELETE, button);
				}

				@Override
				public void onCancelClick() {
					confirmationDialog.hide();
					setFocus();
				}
			});
			confirmationDialog.center();
			confirmationDialog.show();
			confirmationDialog.okButton.setFocus(true);
		} else {
			if (!checkDataPresent()) {
				showConfirmationDialog(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.NO_DATA_TO_DELETE),
						LocaleDictionary.get().getMessageValue(ReviewValidateMessages.DELETE_ROW_TITLE), button);
			} else {
				showConfirmationDialog(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.NO_ROW_SELECTED),
						LocaleDictionary.get().getMessageValue(ReviewValidateMessages.DELETE_ROW_TITLE), button);
			}
		}
	}

	public void deleteAllRowAction(final Button button) {
		if (checkDataPresent()) {
			final ConfirmationDialog confirmationDialog = new ConfirmationDialog();
			confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.DELETE_ALL_ROW_CONFIRMATION));
			confirmationDialog.setText(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.DELETE_ALL_ROW_TITLE));
			confirmationDialog.addDialogListener(new DialogListener() {

				@Override
				public void onOkClick() {
					confirmationDialog.hide();
					insertDeleteRow(Action.DELETE_ALL, button);
				}

				@Override
				public void onCancelClick() {
					confirmationDialog.hide();
					setFocus(button);
				}
			});
			confirmationDialog.center();
			confirmationDialog.show();
			confirmationDialog.okButton.setFocus(true);
		} else {
			showConfirmationDialog(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.NO_DATA_TO_DELETE), LocaleDictionary
					.get().getMessageValue(ReviewValidateMessages.DELETE_ALL_ROW_TITLE), button);
		}
	}

	public boolean checkDataPresent() {
		boolean valid = false;
		List<DataTable> dataTablesList = selectedDocument.getDataTables().getDataTable();
		if (dataTablesList == null || dataTablesList.isEmpty()) {
			TableExtractionView.this.fireEvent(new TableViewDisplayEvent(0));
			valid = false;
		}
		for (final DataTable dataTable : dataTablesList) {
			if (dataTable.getName().equals(selectedDataTableName)) {
				if (dataTable.getRows().getRow().isEmpty()) {
					valid = false;
				} else {
					valid = true;
				}
			}
		}
		return valid;
	}

	public void insertDeleteRow(final Action action, final Button button) {
		insertDeleteRow(action, -1, button);
	}

	/**
	 * This method performs insert,delete and delete all operation on table.
	 * 
	 * @param action {@link Action} action selected by user
	 * @param radioButtonNumber {@link Integer} 0 for insert row before selected row, 1 for insert row after selected row
	 * @param button {@link Button} button where focus is set after action is performed
	 */
	public void insertDeleteRow(final Action action, final int radioButtonNumber, final Button button) {

		List<DataTable> dataTablesList = selectedDocument.getDataTables().getDataTable();
		if (dataTablesList == null || dataTablesList.isEmpty()) {
			TableExtractionView.this.fireEvent(new TableViewDisplayEvent(0));
			return;
		}
		for (final DataTable dataTable : dataTablesList) {
			int focusRow = 0;
			if (dataTable.getName().equals(selectedDataTableName)) {
				HeaderRow headerRow = dataTable.getHeaderRow();
				if (dataTable != null && headerRow != null && headerRow.getColumns() != null
						&& headerRow.getColumns().getColumn() != null && !headerRow.getColumns().getColumn().isEmpty()) {
					FlexTable selectedView = tableNameVsViewMap.get(selectedDataTableName);
					final List<Column> columnList = headerRow.getColumns().getColumn();
					selectedView.clear();
					if (action == Action.DELETE) {
						dataTable.getRows().getRow().remove(selectedRowNumber - 1);
						selectedView.removeRow(selectedRowNumber);
						focusRow = selectedRowNumber;
					} else if (action == Action.INSERT) {
						Row row = createNewRow(columnList);
						if (dataTable.getRows().getRow() == null || dataTable.getRows().getRow().isEmpty()) {
							selectedRowNumber = 1;
							dataTable.getRows().getRow().add(0, row);
							focusRow = 1;
						} else {
							if (radioButtonNumber == 0) {
								dataTable.getRows().getRow().add(selectedRowNumber - 1, row);
								focusRow = selectedRowNumber;
							} else if (radioButtonNumber == 1) {
								dataTable.getRows().getRow().add(selectedRowNumber, row);
								focusRow = selectedRowNumber + 1;
							}
						}
					} else if (action == Action.DELETE_ALL) {
						dataTable.getRows().getRow().clear();
						selectedView.removeAllRows();
					}
					if (dataTable.getRows().getRow().size() != 0) {
						addHeaderColumns(headerRow.getColumns().getColumn(), selectedView);
						createTableList(selectedView, dataTable.getRows().getRow(), columnList, dataTable, selectedDocument);
					} else {
						selectedRowNumber = 0;
						setEmptyTableView(selectedView, headerRow);
						if (button != null) {
							setFocus(button);
						}
					}
					if (dataTable.getRows().getRow().size() > selectedRowNumber - 1 && !dataTable.getRows().getRow().isEmpty()) {
						SuggestBox suggestBox = (SuggestBox) selectedView.getWidget(focusRow, 0);
						suggestBox.getTextBox().setFocus(true);
					} else if (!dataTable.getRows().getRow().isEmpty()) {
						SuggestBox suggestBox = (SuggestBox) selectedView.getWidget(selectedRowNumber - 1, 0);
						suggestBox.getTextBox().setFocus(true);
					}
					break;
				}
			}
		}

	}

	/**
	 * This method sets the view of the empty table.
	 * 
	 * @param flexTable {@link FlexTable}
	 * @param headerRow {@link HeaderRow}
	 */
	private void setEmptyTableView(final FlexTable flexTable, final HeaderRow headerRow) {
		Label label = new Label(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.NO_DATA_TO_DISPLAY));
		int columnCount = headerRow.getColumns().getColumn().size();
		addHeaderColumns(headerRow.getColumns().getColumn(), flexTable);
		flexTable.getRowFormatter().setStyleName(0, "header");
		flexTable.setWidget(1, 0, label);
		flexTable.getFlexCellFormatter().setColSpan(1, 0, columnCount);
	}

	/**
	 * This method creates a new row for selected DataTable.
	 * 
	 * @param columnList {@link List<Column>} list of columns for selected DataTable.
	 * @return {@link Row} returns the new row created.
	 */
	public Row createNewRow(final List<Column> columnList) {
		Row row = new Row();

		Row.Columns columnsRow = row.getColumns();
		if (null == columnsRow) {
			columnsRow = new Row.Columns();
			row.setColumns(columnsRow);
		}
		List<Column> columnRowList = columnsRow.getColumn();
		for (Column colHeader : columnList) {
			Column column = new Column();
			column.setName(null);
			column.setValue(null);
			column.setConfidence(0);
			column.setAlternateValues(new Column.AlternateValues());
			column.setCoordinatesList(new CoordinatesList());
			// column.setPage(null);
			column.setPage(presenter.page.getIdentifier());
			column.setValid(false);
			columnRowList.add(column);
		}
		return row;
	}

	@UiHandler("addNewTableButton")
	public void onAddNewTableButton(final ClickEvent clickEvent) {
		if (presenter.isManualTableExtraction()) {
			exitManualExtractionConfirmation(LocaleDictionary.get().getMessageValue(
					ReviewValidateMessages.EXIT_MANUAL_EXTARCTION_CONFIRMATION, lockedTableName), LocaleDictionary.get()
					.getConstantValue(ReviewValidateConstants.ADD_NEW_TABLE_TITLE), addNewTableButton);
		} else {
			ScreenMaskUtility.maskScreen(LocaleDictionary.get().getConstantValue(LocaleCommonConstants.msg_mask_wait));
			presenter.rpcService.executeAddNewTable(presenter.batchDTO.getBatch(), selectedDocument.getIdentifier(),
					new AsyncCallback<BatchDTO>() {

						@Override
						public void onSuccess(final BatchDTO batchDTO) {
							ScreenMaskUtility.unmaskScreen();
							selectedDocument = batchDTO.getDocumentById(selectedDocument.getIdentifier());
							createTableView(selectedDocument);
							presenter.batchDTO = batchDTO;
							presenter.document = selectedDocument;
							setFocusAfterAddNewTable();
							scrollPanel.setScrollPosition(0);
						}

						@Override
						public void onFailure(final Throwable arg0) {
							ScreenMaskUtility.unmaskScreen();
							ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getConstantValue(
									ReviewValidateConstants.ADD_NEW_TABLE_FAIL_TITLE), LocaleDictionary.get().getMessageValue(
									ReviewValidateMessages.ADD_NEW_TABLE_FAIL));
							setFocusAfterAddNewTable();
						}
					});
		}
	}

	private void setButtonsText() {
		insertRowButton.setText(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.INSERT_ROW_BUTTON));
		deleteRowButton.setText(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.DELETE_ROW_BUTTON));
		deleteAllRowButton.setText(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.DELETE_ALL_ROW_BUTTON));
		manualExtractionButton.setText(manualExtractionLabel);
	}

	public void setFocusAfterAddNewTable() {
		for (Button button : tableNameVsButtonMap.keySet()) {
			setFocus(button);
			break;
		}
	}

	private void createTableView(final Document document) {
		verticalPanel.clear();
		tableNameVsButtonMap.clear();
		List<DataTable> dataTablesList = null;
		selectedDocument = document;
		if (document.getDataTables() != null && document.getDataTables().getDataTable() != null) {
			dataTablesList = document.getDataTables().getDataTable();
		}

		if (dataTablesList == null || dataTablesList.isEmpty()) {
			TableExtractionView.this.fireEvent(new TableViewDisplayEvent(0));
		} else {
			TableExtractionView.this.fireEvent(new TableViewDisplayEvent(dataTablesList.size()));
			for (final DataTable dataTable : dataTablesList) {
				HeaderRow headerRow = dataTable.getHeaderRow();
				final Document doc = document;
				final Rows rows = dataTable.getRows();
				tableNameVsCheckBoxMap.put(dataTable.getName(), new ArrayList<CheckBox>());
				if (dataTable != null && headerRow != null && headerRow.getColumns() != null
						&& headerRow.getColumns().getColumn() != null && !headerRow.getColumns().getColumn().isEmpty()) {
					final List<Column> columnList = headerRow.getColumns().getColumn();
					final FlexTable flexTable = new FlexTable();
					selectedDataTableName = dataTable.getName();
					flexTable.setCellSpacing(0);
					flexTable.setBorderWidth(1);
					flexTable.setWidth("100%");
					addHeaderColumns(columnList, flexTable);
					flexTable.getRowFormatter().setStyleName(0, "header");
					setTableUI(flexTable, dataTable);
					// if (rows != null && rows.getRow() != null && !rows.getRow().isEmpty()) {
					if (rows != null && rows.getRow() != null) {
						presenter.rpcService.getColumnRegexPattern(selectedDocument.getType(), dataTable.getName(),
								new AsyncCallback<List<String>>() {

									@Override
									public void onSuccess(final List<String> listOfColumnPatterns) {
										columnPattern = listOfColumnPatterns;
										tableNameVsColumnPattern.put(dataTable.getName(), listOfColumnPatterns);
										createTableList(flexTable, rows.getRow(), columnList, dataTable, doc);
									}

									@Override
									public void onFailure(final Throwable arg0) {
										final ConfirmationDialog confirmationDialog = new ConfirmationDialog(true);
										confirmationDialog.setText(LocaleDictionary.get().getConstantValue(
												ReviewValidateConstants.REGEX_RETRIEVAL_FAIL));
										confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(
												ReviewValidateMessages.REGEX_RETRIEVAL_FAIL));
										confirmationDialog.addDialogListener(new DialogListener() {

											@Override
											public void onOkClick() {
												confirmationDialog.hide();
												createTableList(flexTable, rows.getRow(), columnList, dataTable, doc);
											}

											@Override
											public void onCancelClick() {
												confirmationDialog.hide();
												presenter.setFocus();
											}
										});

										confirmationDialog.center();
										confirmationDialog.show();
										confirmationDialog.okButton.setFocus(true);
									}
								});
					}
					tableNameVsViewMap.put(dataTable.getName(), flexTable);
				}
			}
		}
	}

	/**
	 * This method will set the selected value from image in the selected SuggestBox.
	 * 
	 * @param spanList {@link List}
	 * @param initailCoordinate {@link PointCoordinate}
	 * @param finalCoordinate {@link PointCoordinate}
	 */
	public void setSelectedValue(final List<Span> spanList, final PointCoordinate initailCoordinate,
			final PointCoordinate finalCoordinate) {
		if (spanList != null && !spanList.isEmpty() && presenter.isManualTableExtraction()) {

			Coordinates coordinates = new Coordinates();
			coordinates.setX0(new BigInteger(initailCoordinate.getxCoordinate().toString()));
			coordinates.setX1(new BigInteger(finalCoordinate.getxCoordinate().toString()));
			coordinates.setY0(new BigInteger(initailCoordinate.getyCoordinate().toString()));
			coordinates.setY1(new BigInteger(finalCoordinate.getyCoordinate().toString()));
			columnVsCoordinates.put(selectedColumnNumber, coordinates);
			insertDataInTable(spanList);
		}
	}

	public void insertDataInTable(final List<Span> spanList) {
		// Row row = selDataTable.getRows().getRow().get(selRowForManualExtraction - 1);
		Row row = selDataTable.getRows().getRow().get(selectedRowNumber - 1);
		Column column = row.getColumns().getColumn().get(selectedColumnNumber);
		if (column.getCoordinatesList() == null) {
			column.setCoordinatesList(new CoordinatesList());
		}
		column.getCoordinatesList().getCoordinates().clear();
		column.setPage(presenter.page.getIdentifier());
		StringBuffer value = new StringBuffer();
		int counter = 1;
		for (Span span : spanList) {
			if (counter++ != 1) {
				value.append(' ');
			}
			column.getCoordinatesList().getCoordinates().add(span.getCoordinates());
			value.append(span.getValue());
		}
		selectedTextBox.setText(value.toString());
		selectedTextBox.setFocus(Boolean.TRUE);
		ValueChangeEvent.fire(selectedTextBox, value.toString());
	}

	/**
	 * This method checks if the rectangle drawn in image is valid with respect to other rectangles drawn.
	 * 
	 * @param initialCoordinate {@link PointCoordinate}
	 * @param finalCoordinate {@link PointCoordinate}
	 * @return valid {@link Boolean}
	 */
	public boolean isValidCoordinate(final PointCoordinate initialCoordinate, final PointCoordinate finalCoordinate) {
		boolean valid = false;
		if (presenter.isManualTableExtraction()) {
			valid = true;
			int counter = 1;
			if (columnVsCoordinates == null || columnVsCoordinates.isEmpty()) {
				valid = true;
			}
			if (!isSameRowSelected()) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						ReviewValidateMessages.INVALID_ROW_SELECTED, selRowForManualExtraction, lockedTableName));
				valid = false;
			} else {
				for (Integer key : columnVsCoordinates.keySet()) {
					if (key == selectedColumnNumber) {
						continue;
					}
					Coordinates coord = columnVsCoordinates.get(key);

					Integer existingX0 = coord.getX0().intValue();
					Integer existingX1 = coord.getX1().intValue();
					Integer existingY0 = coord.getY0().intValue();
					Integer existingY1 = coord.getY1().intValue();
					Integer selectedX0 = initialCoordinate.getxCoordinate();
					Integer selectedX1 = finalCoordinate.getxCoordinate();
					Integer selectedY0 = initialCoordinate.getyCoordinate();
					Integer selectedY1 = finalCoordinate.getyCoordinate();
					if (!checkColumnOverlapping(existingX0, existingX1, selectedX0, selectedX1)) {
						ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.COLUMNS_OVERLAPPING_ERROR));
						valid = false;
						break;
					}

					if (counter++ == 1 && !checkSameRow(existingY0, existingY1, selectedY0, selectedY1)) {
						ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.DIFFERENT_ROW_ERROR));
						valid = false;
						break;
					}
				}
			}
		}
		return valid;
	}

	private boolean checkSameRow(final Integer existingY0, final Integer existingY1, final Integer selectedY0, final Integer selectedY1) {
		boolean valid = true;
		if (selectedY0 < (existingY0 - ReviewValidateConstants.ROW_ERROR_MARGIN)
				|| selectedY1 > (existingY1 + ReviewValidateConstants.ROW_ERROR_MARGIN)) {
			valid = false;
		}
		return valid;
	}

	private boolean isSameRowSelected() {
		boolean valid = false;
		if (selRowForManualExtraction == selectedRowNumber && lockedTableName.equalsIgnoreCase(selectedDataTableName)) {
			valid = true;
		}
		return valid;
	}

	private boolean checkColumnOverlapping(final Integer existingX0, final Integer existingX1, final Integer selectedX0,
			final Integer selectedX1) {
		boolean valid = true;
		if ((selectedX0 > existingX0 && selectedX0 < existingX1) || (selectedX1 > existingX0 && selectedX1 < existingX1)
				|| (selectedX0 <= existingX0 && selectedX1 >= existingX1)) {
			valid = false;
		}
		return valid;
	}

	public Map<Integer, Coordinates> getColumnVsCoordinates() {
		return columnVsCoordinates;
	}
}
