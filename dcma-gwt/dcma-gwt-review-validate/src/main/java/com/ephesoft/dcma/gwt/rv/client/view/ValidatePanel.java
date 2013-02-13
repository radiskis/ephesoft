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

package com.ephesoft.dcma.gwt.rv.client.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Column;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Field;
import com.ephesoft.dcma.batch.schema.Row;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.gwt.core.client.event.AnimationCompleteEvent;
import com.ephesoft.dcma.gwt.core.client.event.AnimationCompleteEventHandler;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.client.ui.SuggestionBox;
import com.ephesoft.dcma.gwt.core.client.util.GWTListBoxControl;
import com.ephesoft.dcma.gwt.core.client.util.GWTValidatableControl;
import com.ephesoft.dcma.gwt.core.client.util.ReverseIterable;
import com.ephesoft.dcma.gwt.core.client.validator.ValidatableWidget;
import com.ephesoft.dcma.gwt.core.shared.BatchDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.FieldTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.ephesoft.dcma.gwt.rv.client.event.DocExpandEvent;
import com.ephesoft.dcma.gwt.rv.client.event.DocExpandEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.DocTypeChangeEvent;
import com.ephesoft.dcma.gwt.rv.client.event.DocTypeChangeEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.DocumentRefreshEvent;
import com.ephesoft.dcma.gwt.rv.client.event.FuzzySearchEvent;
import com.ephesoft.dcma.gwt.rv.client.event.FuzzySearchEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyDownEvent;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyDownEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyUpEvent;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyUpEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.TableViewDisplayEvent;
import com.ephesoft.dcma.gwt.rv.client.event.TableViewDisplayEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.TreeRefreshEvent;
import com.ephesoft.dcma.gwt.rv.client.event.TreeRefreshEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.ValidationFieldChangeEvent;
import com.ephesoft.dcma.gwt.rv.client.i18n.ReviewValidateConstants;
import com.ephesoft.dcma.gwt.rv.client.i18n.ReviewValidateMessages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * The <code>ValidatePanel</code> class provides functionality for showing validate panel to user on validation screen.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.rv.client.view.RVBasePanel
 */
@SuppressWarnings("deprecation")
public class ValidatePanel extends RVBasePanel {

	private static final String ENABLE_SCRIPT_VALIDATION = "ON";

	interface Binder extends UiBinder<DockLayoutPanel, ValidatePanel> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);

	private final FocusPanel fuzzySearchTablefocusPanel = new FocusPanel();

	private final FocusPanel scrollValidationTableFocusPanel = new FocusPanel();

	private final Button fuzzySearchBtn;

	private final Button showTableViewBtn;

	private final TextBox fuzzySearchText;

	private final ValidatableWidget<TextBox> fuzzySearchTextBox;

	@UiField
	protected DockLayoutPanel validateDockLayoutPanel;

	private final FlexTable validationTable = new FlexTable();

	private final ScrollPanel scrollPanel = new ScrollPanel();

	private final FlexTable fuzzySearchTable = new FlexTable();

	private Boolean showErrorField = Boolean.TRUE;

	private Boolean isPrevious = Boolean.FALSE;

	private final static String ALTERNATE_STRING_VALUE = LocaleDictionary.get().getConstantValue(
			ReviewValidateConstants.ALTERNATE_VALUE);
	private final static String SEPERATOR = ReviewValidateConstants.SEPERATOR;

	private List<List<String>> fuzzyDataCarrier;

	private final String enableSwitchScriptValidation = ENABLE_SCRIPT_VALIDATION;

	private boolean fieldAlreadySelected = false;

	private boolean currentFieldSet = true;

	public ValidatePanel() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		scrollPanel.addStyleName(ReviewValidateConstants.OVERFLOW_SCROLL);
		scrollValidationTableFocusPanel.add(validationTable);
		scrollValidationTableFocusPanel.setHeight("98%");
		scrollValidationTableFocusPanel.setWidth("99%");
		scrollPanel.setHeight("100%");
		scrollPanel.add(scrollValidationTableFocusPanel);
		fuzzySearchBtn = new Button();
		showTableViewBtn = new Button();
		showTableViewBtn.setStyleName("tableViewButton");
		showTableViewBtn.setTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.TITLE_TABLE_VIEW_TOOLTIP));
		fuzzySearchText = new TextBox();
		fuzzySearchBtn.setText(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.FUZZY_SEARCH_GO_BTN));
		fuzzySearchBtn.setTitle(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.FUZZY_SEARCH_TOOLTIP));
		fuzzySearchBtn.setStyleName("fuzzySearchButton");

		fuzzySearchTextBox = new ValidatableWidget<TextBox>(fuzzySearchText);
		fuzzySearchTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				fuzzySearchTextBox.toggleValidDateBox();
				fuzzySearchTextBox.getWidget().addStyleName("validatePanelListBox");
			}
		});
		// fuzzySearchTextBox.addValidator(new EmptyStringValidator(fuzzySearchText));
		fuzzySearchTextBox.getWidget().addStyleName("validatePanelListBox");
		fuzzySearchText.addKeyboardListener(new KeyboardListenerAdapter() {

			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				if (keyCode == (char) KEY_ENTER) {
					fuzzySearchBtn.click();
				}
			}
		});

		showTableViewBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				presenter.setTableView(Boolean.TRUE);
				presenter.onTableViewButtonClicked();
			}
		});

		fuzzySearchBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent clickEvent) {
				ScreenMaskUtility.maskScreen();
				String value = fuzzySearchText.getValue();
				if (null == value || value.trim().isEmpty()) {
					final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get()
							.getMessageValue(ReviewValidateMessages.MSG_FUZZY_SEARCH_INVALID_ENTRY), LocaleDictionary.get()
							.getConstantValue(ReviewValidateConstants.FUZZY_SEARCH_TOOLTIP), Boolean.TRUE);
					confirmationDialog.addDialogListener(new DialogListener() {

						@Override
						public void onOkClick() {
							confirmationDialog.hide();
							ScreenMaskUtility.unmaskScreen();
							presenter.setFocus();
						}

						@Override
						public void onCancelClick() {
							ScreenMaskUtility.unmaskScreen();
							presenter.setFocus();
						}
					});

				} else {
					performFuzzySearch(value);
				}
				fuzzySearchText.setText("");
			}
		});

	}

	public void refreshPanel(final Document document) {
		refreshPanel(document, false);
	}

	public void refreshPanel(final Document document, final boolean isDocumentTypeChange) {
		if (presenter.batchDTO.getBatchInstanceStatus().equals(BatchInstanceStatus.READY_FOR_REVIEW)) {
			DocumentLevelFields documentLevelFields = document.getDocumentLevelFields();
			if (documentLevelFields != null) {
				List<DocField> docFieldList = documentLevelFields.getDocumentLevelField();
				if (docFieldList != null) {
					docFieldList.clear();
				}
			}
		}

		this.presenter.document = document;
		this.validationTable.clear();
		presenter.rpcService.getFieldTypeDTOs(document.getType(), presenter.batchDTO.getBatch().getBatchInstanceIdentifier(),
				new AsyncCallback<List<FieldTypeDTO>>() {

					@Override
					public void onFailure(Throwable arg0) {
						ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
								ReviewValidateMessages.ERROR_RET_BATCH));
						// return;
					}

					@Override
					public void onSuccess(List<FieldTypeDTO> arg0) {
						setView(arg0, isDocumentTypeChange);
						presenter.setFocus();
					}
				});

	}

	private void setFocus1() {
		// Boolean focusSet = Boolean.FALSE;
		boolean focusWasSet = false;
		if (fieldAlreadySelected) {
			setFieldAlreadySelected(false);
			setCurrentDocFieldWidget(presenter.getCurrentFieldName());
			DocFieldWidget currentDocFieldWidget = getCurrentDocFieldWidget();
			if (currentDocFieldWidget != null) {
				ValidatableWidget<SuggestionBox> vWidget = currentDocFieldWidget.widget;
				if (vWidget != null) {
					SuggestionBox sWidget = vWidget.getWidget();
					if (sWidget != null) {
						sWidget.setFocus(true);
						focusWasSet = true;
						currentDocFieldWidget.setCurrent(true);
						sWidget.getTextBox().selectAll();
					}
				} else if (currentDocFieldWidget.lWidget != null) {
					currentDocFieldWidget.lWidget.getWidget().setFocus(true);
					focusWasSet = true;
					currentDocFieldWidget.setCurrent(true);
				} else if (currentDocFieldWidget.textAreaWidget != null) {
					currentDocFieldWidget.textAreaWidget.getWidget().setFocus(true);
					focusWasSet = true;
					currentDocFieldWidget.setCurrent(true);
				}
			}
		}
		if (!focusWasSet) {
			if (isPrevious) {
				if (showErrorField) {
					focusWasSet = setPreviousDocFieldWidget();
				}
			} else {
				if (showErrorField) {
					focusWasSet = setDocFieldWidget();

				}
			}
		}
		if (!focusWasSet && docFieldWidgets != null && !docFieldWidgets.isEmpty() && docFieldWidgets.get(0) != null) {
			docFieldWidgets.get(0).setCurrent(true);
			if (docFieldWidgets.get(0).widget != null) {
				docFieldWidgets.get(0).widget.getWidget().setFocus(true);
			} else if (docFieldWidgets.get(0).lWidget != null) {
				docFieldWidgets.get(0).lWidget.getWidget().setFocus(true);
			} else {
				docFieldWidgets.get(0).textAreaWidget.getWidget().setFocus(true);
			}
		}
		DocFieldWidget currentDocFieldWidget = getCurrentDocFieldWidget();
		if (currentDocFieldWidget != null) {
			scrollPanel.ensureVisible(getCurrentDocFieldWidget().getFieldLabel());
			ValidatableWidget<SuggestionBox> vWidget = currentDocFieldWidget.widget;
			ValidatableWidget<ListBox> lWidget = currentDocFieldWidget.lWidget;
			ValidatableWidget<TextArea> textArea = currentDocFieldWidget.textAreaWidget;
			if (vWidget != null) {
				if (presenter.batchDTO.getSuggestionBoxSwitchState().equals("OFF")) {
					SuggestionBox suggestionBoxWidget = vWidget.getWidget();
					if (suggestionBoxWidget != null) {
						suggestionBoxWidget.hideSuggestionList();
					}

				}
				vWidget.getWidget().setFocus(true);
			} else if (lWidget != null) {
				lWidget.getWidget().setFocus(true);
			} else if (textArea != null) {
				textArea.getWidget().setFocus(true);
			}
		}
	}

	/**
	 * @param focusWasSet
	 * @return
	 */
	private boolean setDocFieldWidget() {
		boolean focusWasSet = false;
		for (DocFieldWidget docFieldWidget : docFieldWidgets) {
			if (docFieldWidget.widget != null && !docFieldWidget.widget.validate()) {
				docFieldWidget.widget.getWidget().setFocus(true);
				docFieldWidget.setCurrent(true);
				focusWasSet = true;
				// focusSet = Boolean.TRUE;
				break;
			} else if (docFieldWidget.lWidget != null && !validateListBoxSelection(docFieldWidget.lWidget)) {
				docFieldWidget.lWidget.getWidget().setFocus(true);
				docFieldWidget.setCurrent(true);
				focusWasSet = true;
				// focusSet = Boolean.TRUE;
				break;
			} else if (docFieldWidget.textAreaWidget != null) {
				docFieldWidget.textAreaWidget.getWidget().setFocus(true);
				focusWasSet = true;
				docFieldWidget.setCurrent(true);
				break;
			}
		}
		return focusWasSet;
	}

	/**
	 * @param focusWasSet
	 * @return
	 */
	private boolean setPreviousDocFieldWidget() {
		boolean focusWasSet = false;
		ReverseIterable<DocFieldWidget> iterator = new ReverseIterable<DocFieldWidget>(docFieldWidgets);
		for (DocFieldWidget docFieldWid : iterator) {
			if (docFieldWid.widget != null && !docFieldWid.widget.validate()) {
				docFieldWid.widget.getWidget().setFocus(true);
				focusWasSet = true;
				docFieldWid.setCurrent(true);
				// focusSet = Boolean.TRUE;
				break;
			} else if (docFieldWid.lWidget != null && !validateListBoxSelection(docFieldWid.lWidget)) {
				docFieldWid.lWidget.getWidget().setFocus(true);
				focusWasSet = true;
				docFieldWid.setCurrent(true);
				// focusSet = Boolean.TRUE;
				break;
			} else if (docFieldWid.textAreaWidget != null) {
				docFieldWid.textAreaWidget.getWidget().setFocus(true);
				focusWasSet = true;
				docFieldWid.setCurrent(true);
				break;
			}
		}
		return focusWasSet;
	}

	public boolean setFocusAfterConformationDialog() {
		boolean returnVal = false;
		if (docFieldWidgets != null && !docFieldWidgets.isEmpty()) {
			setFocus1();
			returnVal = true;
		}
		return returnVal;
	}

	public void clearPanel() {
		this.validationTable.clear();
		this.docFieldWidgets.clear();
		this.fuzzySearchTable.setVisible(false);
	}

	public void setView(List<FieldTypeDTO> fieldTypeDTOs, final boolean isDocumentTypeChange) {
		// This field is used to hide document level field on validate panel
		boolean isFieldHidden = false;
		clearPanel();
		if (!this.isVisible()) {
			return;
		}
		fuzzySearchTable.setVisible(true);
		Label fuzzyLabel = new Label("Fuzzy Search : ");
		fuzzyLabel.setWidth("105px");
		fuzzyLabel.addStyleName("bold_text");
		fuzzySearchTable.getCellFormatter().setWidth(0, 0, "115px");
		fuzzySearchTable.setWidget(0, 0, fuzzyLabel);
		fuzzySearchTable.setWidget(1, 0, fuzzySearchText);
		fuzzySearchTable.setWidget(1, 1, fuzzySearchBtn);
		fuzzySearchTable.setWidget(1, 2, showTableViewBtn);
		fuzzySearchTable.getCellFormatter().setWidth(1, 1, "20%");
		fuzzySearchTable.getCellFormatter().setWidth(1, 2, "80%");
		fuzzySearchTable.setWidth("99%");
		Label seperator = new Label(".");
		seperator.addStyleName("fuzzy-SEPERATOR");

		DocumentLevelFields documentLevelFields = presenter.document.getDocumentLevelFields();
		List<DocField> documentLevelFieldList = null;
		if (documentLevelFields != null) {
			documentLevelFieldList = documentLevelFields.getDocumentLevelField();
		}
		if (null != documentLevelFieldList && !documentLevelFieldList.isEmpty()) {
			setDocView(fieldTypeDTOs, isDocumentTypeChange, isFieldHidden, documentLevelFieldList);
		}
		validateDockLayoutPanel.clear();
		if (presenter.batchDTO.getFuzzySearchSwitchState().equals("ON")) {
			fuzzySearchTablefocusPanel.clear();
			fuzzySearchTablefocusPanel.add(fuzzySearchTable);
			validateDockLayoutPanel.addNorth(fuzzySearchTablefocusPanel, 52);
		} else {
			fuzzySearchTablefocusPanel.clear();
			fuzzySearchTable.clear();
			validateDockLayoutPanel.addNorth(showTableViewBtn, 20);
		}
		scrollPanel.clear();
		scrollPanel.add(scrollValidationTableFocusPanel);
		validateDockLayoutPanel.add(scrollPanel);
	}

	private void setDocView(List<FieldTypeDTO> fieldTypeDTOs, final boolean isDocumentTypeChange, boolean isFieldHidden,
			List<DocField> documentLevelFieldList) {
		int index = 1;
		boolean isFieldHiddenTemp = isFieldHidden;
		sortDocumentLevelFields(documentLevelFieldList);
		for (final DocField field : documentLevelFieldList) {
			Map<String, Integer> alternateValues = new HashMap<String, Integer>();
			if (field.getAlternateValues() != null) {
				for (Field alternateField : field.getAlternateValues().getAlternateValue()) {
					populateAlternateValues(alternateValues, alternateField);
				}
			}
			Iterator<Map.Entry<String, Integer>> iterator = alternateValues.entrySet().iterator();
			List<String> alternateValuesSet = new ArrayList<String>();
			while (iterator.hasNext()) {
				populateAlternateValuesSet(iterator, alternateValuesSet);
			}
			String tempFieldName = "";
			String sampleVString = "";
			String fieldTypeName = null;
			List<String> values = new ArrayList<String>();
			ValidatableWidget<SuggestionBox> tempVWidget = null;
			ValidatableWidget<TextArea> tempTextArea = null;
			boolean isShowListBox = false;
			boolean showTextArea = false;
			boolean isReadOnly = false;
			ValidatableWidget<ListBox> tempListBox = null;
			for (FieldTypeDTO fieldTypeDTO : fieldTypeDTOs) {
				if (!fieldTypeDTO.isHidden()) {
					if (fieldTypeDTO.getName().equals(field.getName())) {
						isFieldHiddenTemp = false;
						String fieldOptionValueList = null;
						List<String> regexPatternList = fieldTypeDTO.getRegexPatternList();
						if (isDocumentTypeChange) {
							fieldOptionValueList = fieldTypeDTO.getFieldOptionValueList();
						} else {
							fieldOptionValueList = field.getFieldValueOptionList();
						}
						if (fieldOptionValueList != null && !fieldOptionValueList.isEmpty()
								&& !fieldOptionValueList.matches("^[ ;]{1,}")) {
							isShowListBox = true;
							values = Arrays.asList(fieldOptionValueList.split(";"));
							String actualValue = field.getValue();
							if (null == actualValue) {
								actualValue = "";
							}
							tempListBox = GWTListBoxControl.createGWTListControl(field.getType(), actualValue, field.getName(),
									regexPatternList, values);
							tempFieldName = field.getName();
							sampleVString = fieldTypeDTO.getSampleValue();
							fieldTypeName = fieldTypeDTO.getName();
							// checking if the document level field is readonly and setting the readonly flag.
							if (fieldTypeDTO.getIsReadOnly()) {
								isReadOnly = true;
							}
							break;
						} else if (fieldTypeDTO.isMultiLine()) {
							TextArea textArea = new TextArea();
							tempTextArea = new ValidatableWidget<TextArea>(textArea);
							tempTextArea.getWidget().setValue(field.getValue());
							tempFieldName = field.getName();
							fieldTypeName = fieldTypeDTO.getName();
							showTextArea = true;
							// checking if the document level field is readonly and setting the readonly flag.
							if (fieldTypeDTO.getIsReadOnly()) {
								isReadOnly = true;
							}
							break;
						} else {
							tempVWidget = GWTValidatableControl.createGWTControl(field.getType(), field.getValue(), field.getName(),
									alternateValuesSet, regexPatternList, fieldTypeDTO.getSampleValue());
							tempFieldName = field.getName();
							sampleVString = fieldTypeDTO.getSampleValue();
							fieldTypeName = fieldTypeDTO.getName();
							// checking if the document level field is readonly and setting the readonly flag.
							if (fieldTypeDTO.getIsReadOnly()) {
								isReadOnly = true;
							}
							break;
						}
					}
				} else {
					isFieldHiddenTemp = true;
				}
			}
			final String fieldNameString = tempFieldName;
			final String sampleValueString = sampleVString;
			if (isShowListBox) {
				index = populateValidationTableWidgets(fieldTypeDTOs, index, isFieldHiddenTemp, field, fieldTypeName, values,
						isReadOnly, tempListBox, sampleValueString);
			} else if (showTextArea) {
				if (tempTextArea == null) {
					TextArea textArea = new TextArea();
					tempTextArea = new ValidatableWidget<TextArea>(textArea);
				}
				index = setTextAreaTypeFields(isFieldHiddenTemp, index, field, alternateValuesSet, fieldTypeName, tempTextArea,
						isReadOnly, fieldNameString, sampleValueString);
			} else {
				index = addValidatableTableWidgets(index, isFieldHiddenTemp, field, alternateValuesSet, fieldTypeName, tempVWidget,
						isReadOnly, fieldNameString, sampleValueString);
			}
		}
	}

	private int populateValidationTableWidgets(List<FieldTypeDTO> fieldTypeDTOs, int index, boolean isFieldHiddenTemp,
			final DocField field, String fieldTypeDescription, List<String> values, boolean isReadOnly,
			ValidatableWidget<ListBox> tempListBox, final String sampleValueString) {
		int indexTemp = index;
		List<String> regexPatternList = null;
		for (FieldTypeDTO fieldTypeDTO : fieldTypeDTOs) {
			if (!fieldTypeDTO.isHidden() && fieldTypeDTO.getName().equals(field.getName())) {
				regexPatternList = fieldTypeDTO.getRegexPatternList();
			}
		}
		if (!isFieldHiddenTemp) {
			indexTemp = setValidationTableWidgets(indexTemp, field, fieldTypeDescription, values, isReadOnly, tempListBox,
					sampleValueString, regexPatternList);
		}
		return indexTemp;
	}

	private void populateAlternateValuesSet(Iterator<Map.Entry<String, Integer>> iterator, List<String> alternateValuesSet) {
		Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>) iterator.next();
		if (pair.getValue().intValue() != 0) {
			for (int k = 0; k <= pair.getValue().intValue(); k++) {
				alternateValuesSet.add(pair.getKey() + SEPERATOR + ALTERNATE_STRING_VALUE + k);
			}
		} else {
			alternateValuesSet.add(pair.getKey());
		}
	}

	private void populateAlternateValues(Map<String, Integer> alternateValues, Field alternateField) {
		if (alternateValues.containsKey(alternateField.getValue())) {
			int nextVal = alternateValues.get(alternateField.getValue()).intValue();
			nextVal++;
			alternateValues.remove(alternateField.getValue());
			alternateValues.put(alternateField.getValue(), Integer.valueOf(nextVal));
		} else {
			alternateValues.put(alternateField.getValue(), 0);
		}
	}

	private int addValidatableTableWidgets(int index, boolean isFieldHiddenTemp, final DocField field,
			List<String> alternateValuesSet, String fieldTypeDescription, ValidatableWidget<SuggestionBox> tempVWidget,
			boolean isReadOnly, final String fieldNameString, final String sampleValueString) {
		int indexTemp = index;
		ValidatableWidget<SuggestionBox> vWidgetTemp = tempVWidget;
		if (!isFieldHiddenTemp) {
			if (vWidgetTemp == null) {
				vWidgetTemp = GWTValidatableControl.createGWTControl(field.getType(), field.getValue(), alternateValuesSet);
			}
			final ValidatableWidget<SuggestionBox> vWidget = vWidgetTemp;
			vWidget.getWidget().addStyleName("validatePanelListBox");
			for (int k = 0; k < alternateValuesSet.size(); k++) {
				vWidget.getWidget().setTitle(field.getName());
			}
			addVWidgetHandlers(field, fieldNameString, sampleValueString, vWidget);
			Label fieldLabel = null;
			if (fieldTypeDescription != null && !fieldTypeDescription.isEmpty()) {
				fieldLabel = new Label(fieldTypeDescription);
				validationTable.setWidget(indexTemp++, 0, fieldLabel);
			} else {
				fieldLabel = new Label(field.getName());
				validationTable.setWidget(indexTemp++, 0, fieldLabel);
			}
			validationTable.setWidget(indexTemp++, 0, vWidget.getWidget());
			addDocFieldWidget(presenter.document.getIdentifier(), fieldLabel, field, vWidget, null, null, isReadOnly);
		}
		return indexTemp;
	}

	private int setValidationTableWidgets(int index, final DocField field, String fieldTypeDescription, List<String> values,
			boolean isReadOnly, ValidatableWidget<ListBox> tempListBox, final String sampleValueString, List<String> regexPatternList) {
		int indexTemp = index;
		ValidatableWidget<ListBox> listBox = tempListBox;
		if (listBox == null) {
			listBox = GWTListBoxControl.createGWTListControl(field.getType(), field.getValue(), regexPatternList, values);
		}
		final ValidatableWidget<ListBox> listBoxWidget = listBox;
		listBoxWidget.getWidget().addStyleName(ReviewValidateConstants.DROPBOX_STYLE);
		final ListBox vWidget = listBoxWidget.getWidget();
		vWidget.setTitle(field.getName());
		final String isActualValueFound = vWidget.getElement().getAttribute("isActualValueFound");
		// decision to removeOverlay in case value not found in drop down
		vWidget.insertItem(LocaleDictionary.get().getMessageValue(ReviewValidateMessages.NONE_SELECTED_TEXT), 0);
		if (!Boolean.parseBoolean(isActualValueFound != null ? isActualValueFound : "")) {
			vWidget.setSelectedIndex(0);
		}
		validateListBoxSelection(listBoxWidget);
		addVWidgetHandlers(field, sampleValueString, listBoxWidget, vWidget);
		Label fieldName = null;
		if (fieldTypeDescription != null && !fieldTypeDescription.isEmpty()) {
			fieldName = new Label(fieldTypeDescription);
			validationTable.setWidget(indexTemp++, 0, fieldName);
		} else {
			fieldName = new Label(field.getName());
			validationTable.setWidget(indexTemp++, 0, fieldName);
		}
		validationTable.setWidget(indexTemp++, 0, vWidget);
		addDocFieldWidget(presenter.document.getIdentifier(), fieldName, field, null, listBox, null, isReadOnly);
		return indexTemp;
	}

	/**
	 * @param field
	 * @param fieldNameString
	 * @param sampleValueString
	 * @param vWidget
	 */
	private void addVWidgetHandlers(final DocField field, final String fieldNameString, final String sampleValueString,
			final ValidatableWidget<SuggestionBox> vWidget) {
		vWidget.getWidget().getTextBox().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent arg0) {
				if (presenter.batchDTO.getSuggestionBoxSwitchState().equals("OFF")) {
					vWidget.getWidget().hideSuggestionList();
				}
				if (arg0.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					arg0.preventDefault();
					vWidget.getWidget().hideSuggestionList();
				}
			}
		});
		vWidget.getWidget().getTextBox().addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent keyDownEvent) {
				if (keyDownEvent.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					keyDownEvent.preventDefault();
					vWidget.getWidget().hideSuggestionList();
				}
				switch (keyDownEvent.getNativeKeyCode()) {
					case 'b':
					case 'B':
						if (keyDownEvent.isControlKeyDown()) {
							keyDownEvent.preventDefault();
							boolean validateWidget = vWidget.isValidateWidget();
							vWidget.setValidateWidget(!validateWidget);
							ValidatePanel.this.fireEvent(new ValidationFieldChangeEvent(vWidget.validate(), sampleValueString, field
									.getName(), vWidget.validateThroughValidators()));
						}
						break;
					default:
						break;
				}
			}
		});
		vWidget.getWidget().getTextBox().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> arg0) {
				if (presenter.batchDTO.getFieldValueChangeScriptSwitchState().equalsIgnoreCase("ON")) {
					currentFieldSet = false;
					presenter.setScriptExecuted(true);
					presenter.setFieldValueChangeName(field.getName());
					setTimerToExecuteScript();
				}
			}

		});
		vWidget.getWidget().getTextBox().addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				if (!currentFieldSet) {
					currentFieldSet = true;
					presenter.setCurrentFieldName(field.getName());
				}

				presenter.setCurrentDocumentFieldName(field.getName());

				setCurrentDocFieldWidget(field.getName());
				if (presenter.batchDTO.getSuggestionBoxSwitchState().equals("OFF")) {
					vWidget.getWidget().hideSuggestionList();
				}
				ValidatePanel.this.fireEvent(new ValidationFieldChangeEvent(field));
				ValidatePanel.this.fireEvent(new ValidationFieldChangeEvent(vWidget.validate(), sampleValueString, fieldNameString,
						vWidget.validateThroughValidators()));

				if (presenter.isScriptExecuted()) {
					presenter.executeScriptOnFieldChange(presenter.getFieldValueChangeName());
				}
			}
		});

		vWidget.getWidget().addSelectionHandler(new SelectionHandler<Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> arg0) {
				String inputString = arg0.getSelectedItem().getReplacementString();
				setSuggestBoxEvents(field, inputString, vWidget);
			}
		});
	}

	/**
	 * @param isFieldHidden
	 * @param index
	 * @param field
	 * @param alternateValuesSet
	 * @param fieldTypeDescription
	 * @param tempTextArea
	 * @param isReadOnly
	 * @param fieldNameString
	 * @param sampleValueString
	 * @return
	 */
	private int setTextAreaTypeFields(boolean isFieldHidden, int index, final DocField field, List<String> alternateValuesSet,
			String fieldTypeDescription, ValidatableWidget<TextArea> tempTextArea, boolean isReadOnly, final String fieldNameString,
			final String sampleValueString) {
		int indexLocal = index;
		if (!isFieldHidden) {
			final ValidatableWidget<TextArea> textAreaWidget = tempTextArea;
			for (int k = 0; k < alternateValuesSet.size(); k++) {
				textAreaWidget.getWidget().setTitle(field.getName());
			}

			textAreaWidget.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> arg0) {
					if (presenter.batchDTO.getFieldValueChangeScriptSwitchState().equalsIgnoreCase("ON")) {
						currentFieldSet = false;
						presenter.setScriptExecuted(true);
						presenter.setFieldValueChangeName(field.getName());
						setTimerToExecuteScript();
					}
				}

			});
			textAreaWidget.getWidget().addFocusHandler(new FocusHandler() {

				@Override
				public void onFocus(FocusEvent event) {
					if (!currentFieldSet) {
						currentFieldSet = true;
						setFieldAlreadySelected(true);
						presenter.setCurrentFieldName(field.getName());
					}

					presenter.setCurrentDocumentFieldName(field.getName());

					setCurrentDocFieldWidget(field.getName());
					ValidatePanel.this.fireEvent(new ValidationFieldChangeEvent(field));
					ValidatePanel.this.fireEvent(new ValidationFieldChangeEvent(true, sampleValueString, fieldNameString, true));

					if (presenter.isScriptExecuted()) {
						presenter.executeScriptOnFieldChange(presenter.getFieldValueChangeName());
					}

				}
			});

			Label fieldLabel = null;
			if (fieldTypeDescription != null && !fieldTypeDescription.isEmpty()) {
				fieldLabel = new Label(fieldTypeDescription);
				validationTable.setWidget(indexLocal++, 0, fieldLabel);
			} else {
				fieldLabel = new Label(field.getName());
				validationTable.setWidget(indexLocal++, 0, fieldLabel);
			}
			validationTable.setWidget(indexLocal++, 0, textAreaWidget.getWidget());
			addDocFieldWidget(presenter.document.getIdentifier(), fieldLabel, field, null, null, textAreaWidget, isReadOnly);
		}
		return indexLocal;
	}

	/**
	 * @param field
	 * @param sampleValueString
	 * @param listBoxWidget
	 * @param vWidget
	 */
	private void addVWidgetHandlers(final DocField field, final String sampleValueString,
			final ValidatableWidget<ListBox> listBoxWidget, final ListBox vWidget) {
		vWidget.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				if (!currentFieldSet) {
					currentFieldSet = true;
					presenter.setCurrentFieldName(field.getName());
				}

				presenter.setCurrentDocumentFieldName(field.getName());

				setCurrentDocFieldWidget(field.getName());
				ValidatePanel.this.fireEvent(new ValidationFieldChangeEvent(field, false));

				// commented out this line to remove the overlay in case the actual value not found in drop down
				ValidatePanel.this.fireEvent(new ValidationFieldChangeEvent(listBoxWidget.validate(), null, null, listBoxWidget
						.validateThroughValidators()));

				if (presenter.isScriptExecuted()) {
					presenter.executeScriptOnFieldChange(presenter.getFieldValueChangeName());
				}
			}
		});

		vWidget.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent arg0) {
				validateListBoxSelection(listBoxWidget);
				updateDocument(null, field.getName());
				if (presenter.batchDTO.getFieldValueChangeScriptSwitchState().equalsIgnoreCase("ON")) {
					currentFieldSet = false;
					presenter.setScriptExecuted(true);
					presenter.setFieldValueChangeName(field.getName());
					setTimerToExecuteScript();
				}
			}
		});
		vWidget.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				switch (event.getNativeKeyCode()) {
					case 'b':
					case 'B':
						if (event.isControlKeyDown()) {
							event.preventDefault();
							boolean validateWidget = listBoxWidget.isValidateWidget();
							listBoxWidget.setValidateWidget(!validateWidget);
							ValidatePanel.this.fireEvent(new ValidationFieldChangeEvent(listBoxWidget.validate(), sampleValueString,
									field.getName(), listBoxWidget.validateThroughValidators()));
						}
						break;
					default:
						break;

				}
			}
		});
	}

	/**
	 * @param documentLevelFieldList
	 */
	private void sortDocumentLevelFields(List<DocField> documentLevelFieldList) {
		Collections.sort(documentLevelFieldList, new Comparator<DocField>() {

			public int compare(DocField fieldOne, DocField fieldSec) {
				int compare = 0;
				if (fieldOne != null && fieldSec != null) {
					int fieldOrderNumberOne = fieldOne.getFieldOrderNumber();
					int fieldOrderNumberSec = fieldSec.getFieldOrderNumber();
					if (fieldOrderNumberOne > fieldOrderNumberSec) {
						compare = 1;
					} else {
						if (fieldOrderNumberOne < fieldOrderNumberSec) {
							compare = -1;
						} else {
							compare = 0;
						}
					}
				}
				return compare;
			};
		});
	}

	public void updateDocument(CoordinatesList coordinatesList, String changedWidgetName) {
		if (!docFieldWidgets.isEmpty()) {
			if (docFieldWidgets.get(0).getParentDocumentIdentifier().equals(presenter.document.getIdentifier())) {
				String errorMessage = presenter.document.getErrorMessage();
				Boolean isValidDoc = Boolean.TRUE;
				if (errorMessage != null && !errorMessage.isEmpty()) {
					isValidDoc = Boolean.FALSE;
				}
				for (DocFieldWidget docFieldWidget : docFieldWidgets) {
					if (docFieldWidget.widget != null) {
						docFieldWidget.field.setValue(docFieldWidget.widget.getWidget().getValue());
						if (!docFieldWidget.widget.validate()) {
							isValidDoc = Boolean.FALSE;
						}
					} else if (docFieldWidget.textAreaWidget != null) {
						docFieldWidget.field.setValue(docFieldWidget.textAreaWidget.getWidget().getValue());

					} else {
						// Check for the drop down if they have none selected
						if (docFieldWidget.lWidget != null) {
							if (docFieldWidget.lWidget.getWidget().getSelectedIndex() == 0) {
								docFieldWidget.field.setValue("");
							} else {
								docFieldWidget.field.setValue(docFieldWidget.lWidget.getWidget().getItemText(
										docFieldWidget.lWidget.getWidget().getSelectedIndex()));
							}
							if (!docFieldWidget.lWidget.validate()) {
								isValidDoc = Boolean.FALSE;
							}
						}
					}
				}
				if (coordinatesList != null) {
					for (DocFieldWidget docFieldWidget : docFieldWidgets) {
						if (docFieldWidget.field.getName().equals(changedWidgetName)) {
							docFieldWidget.field.setCoordinatesList(coordinatesList);
						}
					}
				}
				if (isValidDoc) {
					isValidDoc = checkForInvalidDataTables(presenter.document);
				}
				presenter.document.setValid(isValidDoc);
				List<Document> documents = presenter.batchDTO.getBatch().getDocuments().getDocument();
				int docIndex = 0;
				for (Document doc : documents) {
					if (doc.getIdentifier().equals(presenter.document.getIdentifier())) {
						documents.set(docIndex, presenter.document);
						break;
					}
					docIndex++;
				}
			}
		} else {
			presenter.document.setValid(checkForInvalidDataTables(presenter.document));
		}
	}

	private boolean checkForInvalidDataTables(Document document) {
		boolean isValidDoc = true;
		if (document.getDataTables() != null) {
			List<DataTable> dataTableList = document.getDataTables().getDataTable();
			for (DataTable dataTable : dataTableList) {
				if (dataTable.getRows() != null) {
					isValidDoc = checkForInvalidRow(dataTable);
					if (!isValidDoc) {
						break;
					}
				}
			}
		}
		return isValidDoc;
	}

	public boolean checkForInvalidRow(final DataTable dataTable) {
		boolean isValidDoc = true;
		List<Row> rowList = dataTable.getRows().getRow();
		for (Row row : rowList) {
			if (row.getColumns() != null) {
				List<Column> columnList = row.getColumns().getColumn();
				for (Column column : columnList) {
					if (!column.isValid()) {
						isValidDoc = false;
						break;
					}
				}
				if (!isValidDoc) {
					break;
				}
			}
		}
		return isValidDoc;
	}

	@Override
	public void initializeWidget() {
		// no need to do anything while initializing widget
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		addDocExpandEventHandler(eventBus);

		addDocTypeChangeEventHandler(eventBus);

		addRVKeyDownEventHandler(eventBus);

		addRVKeyUpEventHandler(eventBus);

		addAnimationCompleteEventHandler(eventBus);

		addTreeFreshEventHandler(eventBus);

		addFuzzySearchEventHandler(eventBus);

		addTableViewDisplayEventHandler(eventBus);

	}

	/**
	 * @param eventBus
	 */
	private void addTableViewDisplayEventHandler(HandlerManager eventBus) {
		eventBus.addHandler(TableViewDisplayEvent.type, new TableViewDisplayEventHandler() {

			@Override
			public void onResult(TableViewDisplayEvent event) {
				showTableViewBtn.setVisible(false);
				if (event.getTablesCount() > 0) {
					showTableViewBtn.setVisible(true);
				}
			}
		});
	}

	/**
	 * @param eventBus
	 */
	private void addFuzzySearchEventHandler(HandlerManager eventBus) {
		// This event will handle the fuzzy search event.
		eventBus.addHandler(FuzzySearchEvent.type, new FuzzySearchEventHandler() {

			@Override
			public void onExpand(FuzzySearchEvent event) {
				int index = event.getIndex();
				boolean isError = false;
				if (null == fuzzyDataCarrier || fuzzyDataCarrier.isEmpty() || index >= fuzzyDataCarrier.size() || index == 0) {
					isError = true;
				} else {
					List<String> resultHeaderList = fuzzyDataCarrier.get(0);
					List<String> resultDataList = fuzzyDataCarrier.get(index);
					if (null == resultDataList || resultDataList.isEmpty() || null == resultHeaderList || resultHeaderList.isEmpty()) {
						isError = true;
					} else {
						int jIndex = 0;
						for (String headerName : resultHeaderList) {
							String data = resultDataList.get(jIndex);
							setFuzzySearchValue(headerName, data);
							jIndex++;
						}
					}
				}
				if (isError) {
					// Window.alert("Unsuccessful fuzzy search");
					final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get()
							.getMessageValue(ReviewValidateMessages.MSG_FUZZY_SEARCH_UNSUCCESSFUL), LocaleDictionary.get()
							.getConstantValue(ReviewValidateConstants.FUZZY_SEARCH_TOOLTIP), Boolean.TRUE);

					confirmationDialog.addDialogListener(new DialogListener() {

						@Override
						public void onOkClick() {
							confirmationDialog.hide();
							ScreenMaskUtility.unmaskScreen();
						}

						@Override
						public void onCancelClick() {
							ScreenMaskUtility.unmaskScreen();
							presenter.setFocus();
						}
					});

				}
			}
		});
	}

	/**
	 * @param eventBus
	 */
	private void addTreeFreshEventHandler(HandlerManager eventBus) {
		eventBus.addHandler(TreeRefreshEvent.type, new TreeRefreshEventHandler() {

			@Override
			public void refresh(TreeRefreshEvent treeRefreshEvent) {
				// If any of the following entities is null... this means there is no page or document left in the batch.
				// Set the validate panel visibility to false.
				Batch batch = treeRefreshEvent.getBatchDTO().getBatch();
				if (batch == null || batch.getDocuments() == null || batch.getDocuments().getDocument() == null
						|| batch.getDocuments().getDocument().size() < 1) {
					setVisible(Boolean.FALSE);
					return;
				}
			}
		});
	}

	/**
	 * @param eventBus
	 */
	private void addAnimationCompleteEventHandler(HandlerManager eventBus) {
		eventBus.addHandler(AnimationCompleteEvent.type, new AnimationCompleteEventHandler() {

			@Override
			public void onAnimationComplete(AnimationCompleteEvent event) {
				if (event.getIndex() == 1) {
					((Button) ((HorizontalPanel) ((DockLayoutPanel) event.getWidget()).getWidget(0)).getWidget(0)).setFocus(true);
				} else {
					presenter.setFocus();
				}

			}
		});
	}

	/**
	 * @param eventBus
	 */
	private void addRVKeyUpEventHandler(HandlerManager eventBus) {
		eventBus.addHandler(RVKeyUpEvent.type, new RVKeyUpEventHandler() {

			@Override
			public void onKeyUp(RVKeyUpEvent event) {

				DocFieldWidget docFieldWidget = null;
				updateDocument(null, null);

				if (event.getEvent().isControlKeyDown()) {
					switch (event.getEvent().getNativeKeyCode()) {
						// Tab
						case '\t':
							if (!presenter.isTableView()) {
								event.getEvent().getNativeEvent().preventDefault();
								if (event.getEvent().isShiftKeyDown()) {
									break;
								}
								showErrorField = Boolean.FALSE;
								docFieldWidget = getNextDocFieldWidget(showErrorField);
								if (docFieldWidget != null) {
									setCurrentFieldWidgetFocus(docFieldWidget);
								} else {
									openNextDocument();
								}
							}
							break;
						// CTRL + .
						case 190:
							if (!presenter.isTableView()) {
								event.getEvent().getNativeEvent().preventDefault();
								showErrorField = Boolean.TRUE;
								isPrevious = Boolean.FALSE;
								docFieldWidget = getNextDocFieldWidget(showErrorField);
								if (docFieldWidget != null) {
									if (docFieldWidget.widget != null) {
										docFieldWidget.widget.getWidget().setFocus(true);
									} else if (docFieldWidget.lWidget != null) {
										docFieldWidget.lWidget.getWidget().setFocus(true);
									} else {
										docFieldWidget.textAreaWidget.getWidget().setFocus(true);
									}
								} else {
									openNextDocument();
								}
							}
							break;
						// CTRL + ,
						case 188:
							if (!presenter.isTableView()) {
								event.getEvent().getNativeEvent().preventDefault();
								showErrorField = Boolean.TRUE;
								isPrevious = Boolean.TRUE;
								docFieldWidget = getPreviousDocFieldWidget(showErrorField);
								if (docFieldWidget != null) {
									if (docFieldWidget.widget != null) {
										docFieldWidget.widget.getWidget().setFocus(true);
									} else if (docFieldWidget.lWidget != null) {
										docFieldWidget.lWidget.getWidget().setFocus(true);
									} else {
										docFieldWidget.textAreaWidget.getWidget().setFocus(true);
									}
								} else {
									openPreviousDocument();
								}
							}
							break;
						// CTRL + z
						case 90:
							if (!presenter.isTableView()) {
								event.getEvent().getNativeEvent().preventDefault();
								fuzzySearchText.setFocus(true);
							}
							break;
						// CTRL + 5
						case 53:
						case 101:
							event.getEvent().getNativeEvent().preventDefault();
							if (showTableViewBtn.isVisible()) {
								presenter.setTableView(Boolean.TRUE);
								presenter.onTableViewButtonClicked();
							}
							break;
						// CTRL + 6
						case 54:
						case 102:
							event.getEvent().getNativeEvent().preventDefault();
							presenter.setTableView(Boolean.FALSE);
							if (showTableViewBtn.isVisible()) {
								presenter.onTableViewBackButtonClicked();
							}
							break;
						default:
							break;
					}
				}

			}
		});
	}

	/**
	 * @param eventBus
	 */
	private void addRVKeyDownEventHandler(HandlerManager eventBus) {
		eventBus.addHandler(RVKeyDownEvent.type, new RVKeyDownEventHandler() {

			@Override
			public void onKeyDown(RVKeyDownEvent event) {
				if (presenter.batchDTO.getBatchInstanceStatus().equals(BatchInstanceStatus.READY_FOR_VALIDATION)
						&& event.getEvent().isControlKeyDown()) {
					updateDocument(null, null);
				}
			}
		});
	}

	/**
	 * @param eventBus
	 */
	private void addDocTypeChangeEventHandler(HandlerManager eventBus) {
		eventBus.addHandler(DocTypeChangeEvent.type, new DocTypeChangeEventHandler() {

			@Override
			public void onDocumentTypeChange(DocTypeChangeEvent event) {
				Document documentType = event.getDocumentType();
				refreshPanel(documentType, true);
				documentType.setDocumentTypeChanged(true);
			}
		});
	}

	/**
	 * @param eventBus
	 */
	private void addDocExpandEventHandler(HandlerManager eventBus) {
		eventBus.addHandler(DocExpandEvent.type, new DocExpandEventHandler() {

			@Override
			public void onExpand(DocExpandEvent event) {
				validateDockLayoutPanel.clear();
				refreshPanel(event.getDocument());
			}
		});
	}

	private static class DocFieldWidget {

		protected String parentDocumentIdentifier;
		protected Field field;
		protected ValidatableWidget<SuggestionBox> widget;
		protected ValidatableWidget<ListBox> lWidget;
		protected ValidatableWidget<TextArea> textAreaWidget;
		protected boolean isReadOnly;
		protected boolean isCurrent = false;
		protected Label fieldLabel;

		public DocFieldWidget(String parentDocumentIdentifier, Label fieldLabel, Field field, ValidatableWidget<SuggestionBox> widget,
				ValidatableWidget<ListBox> lWidget, ValidatableWidget<TextArea> textAreaWidget, boolean isReadOnly) {
			this.parentDocumentIdentifier = parentDocumentIdentifier;
			this.fieldLabel = fieldLabel;
			this.field = field;
			this.textAreaWidget = textAreaWidget;
			this.widget = widget;
			this.lWidget = lWidget;
			this.isReadOnly = isReadOnly;
			// enabling or disabling the widgets based on 'readonly' flag
			if (lWidget != null) {
				this.lWidget.getWidget().setEnabled(!isReadOnly);
			}
			if (widget != null) {
				this.widget.getWidget().getTextBox().setEnabled(!isReadOnly);
			}
			if (textAreaWidget != null) {
				this.textAreaWidget.getWidget().setReadOnly(isReadOnly);
			}

			setForceReviewOfValidatableWidget();
		}

		private void setForceReviewOfValidatableWidget() {
			Field field = this.field;
			if (field != null) {
				boolean forceReview = field.isForceReview();
				if (forceReview) {
					if (this.widget != null) {
						this.widget.setForcedReviewDone(false);
					}
					if (this.lWidget != null) {
						this.lWidget.setForcedReviewDone(false);
					}
				}
			}
		}

		/*
		 * public DocFieldWidget(Field field, ListBox lWidget) { this.field = field; this.lWidget = lWidget; }
		 * 
		 * public DocFieldWidget(Label fieldLabel, Field field, ListBox lWidget) { this.fieldLabel = fieldLabel; this.field = field;
		 * this.lWidget = lWidget; }
		 */

		@Override
		public int hashCode() {
			return field.getName().hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return this.field.getName().equals(((DocFieldWidget) obj).field.getName());
		}

		public void setCurrent(boolean isCurrent) {
			this.isCurrent = isCurrent;
		}

		public Label getFieldLabel() {
			return fieldLabel;
		}

		public String getParentDocumentIdentifier() {
			return parentDocumentIdentifier;
		}
	}

	private final List<DocFieldWidget> docFieldWidgets = new LinkedList<DocFieldWidget>();

	private void addDocFieldWidget(String documentIdentifier, Label fieldLabel, Field field, ValidatableWidget<SuggestionBox> widget,
			ValidatableWidget<ListBox> lWidget, ValidatableWidget<TextArea> textArea, boolean isReadOnly) {
		docFieldWidgets.add(new DocFieldWidget(documentIdentifier, fieldLabel, field, widget, lWidget, textArea, isReadOnly));
	}

	/*
	 * private void addDocFieldWidget(Field field, ValidatableWidget<ListBox> lWidget) { docFieldWidgets.add(new DocFieldWidget(field,
	 * null, lWidget)); }
	 * 
	 * private void addDocFieldWidget(Label fieldLabel, Field field, ValidatableWidget<ListBox> lWidget) { docFieldWidgets.add(new
	 * DocFieldWidget(fieldLabel, field, null, lWidget)); }
	 */

	private DocFieldWidget getNextDocFieldWidget(boolean isError) {
		Boolean reachedToCurrent = Boolean.FALSE;
		DocFieldWidget nextDocFieldWidget = null;
		for (DocFieldWidget docFieldWidget : docFieldWidgets) {
			if (docFieldWidget.widget != null) {
				if (docFieldWidget.isCurrent) {
					docFieldWidget.widget.getWidget().hideSuggestionList();
					reachedToCurrent = Boolean.TRUE;
					continue;
				}

				if (reachedToCurrent && (!isError || !docFieldWidget.widget.validate())) {
					nextDocFieldWidget = docFieldWidget;
					break;
				}
			} else if (docFieldWidget.textAreaWidget != null) {
				if (docFieldWidget.isCurrent) {
					reachedToCurrent = Boolean.TRUE;
					continue;
				}

				if (reachedToCurrent && !isError) {
					nextDocFieldWidget = docFieldWidget;
					break;
				}

			} else {
				if (docFieldWidget.isCurrent) {
					reachedToCurrent = Boolean.TRUE;
					continue;
				}
				if (reachedToCurrent && (!isError || !validateListBoxSelection(docFieldWidget.lWidget))) {
					nextDocFieldWidget = docFieldWidget;
					break;
				}
			}
		}
		return nextDocFieldWidget;
	}

	public void setSelectedValues(String value, CoordinatesList coordinatesList, String pageid) {
		boolean isAdded = false;
		DocFieldWidget docFieldWidget = getCurrentDocFieldWidget();
		if (!docFieldWidget.isReadOnly) {

			// set the overlay values to the selected fields only if the document level field is not readonly
			if (docFieldWidget.widget != null) {
				// docFieldWidget.widget.getWidget().setText(value);
				// docFieldWidget.widget.getWidget().getTextBox().setValue(value,true);
				docFieldWidget.field.setCoordinatesList(coordinatesList);
				docFieldWidget.field.setPage(pageid);
				docFieldWidget.field.setValue(value);
				docFieldWidget.widget.getWidget().setValue(value, true);
				// ValueChangeEvent.fire(docFieldWidget.widget.getWidget(), value);
				docFieldWidget.widget.getWidget().setFocus(true);
			} else if (docFieldWidget.textAreaWidget != null) {
				docFieldWidget.field.setCoordinatesList(coordinatesList);
				docFieldWidget.field.setPage(pageid);
				docFieldWidget.field.setValue(value);
				docFieldWidget.textAreaWidget.getWidget().setValue(value, true);
				docFieldWidget.textAreaWidget.getWidget().setFocus(true);
			} else {
				ListBox widget = docFieldWidget.lWidget.getWidget();
				for (int i = 0; i < widget.getItemCount(); i++) {
					if (widget.getItemText(i).equalsIgnoreCase(value)) {
						widget.setSelectedIndex(i);
						isAdded = true;
					}
				}
				if (!isAdded) {
					widget.addItem(value);
					widget.setSelectedIndex(widget.getItemCount() - 1);
				}
			}
		}
	}

	private DocFieldWidget getPreviousDocFieldWidget(boolean isError) {
		Boolean reachedToCurrent = Boolean.FALSE;
		DocFieldWidget previousDocFieldWidget = null;
		for (DocFieldWidget docFieldWidget : new ReverseIterable<DocFieldWidget>(docFieldWidgets)) {
			if (docFieldWidget.widget != null) {
				if (docFieldWidget.isCurrent) {
					docFieldWidget.widget.getWidget().hideSuggestionList();
					reachedToCurrent = Boolean.TRUE;
					continue;
				}

				if (reachedToCurrent && (!isError || !docFieldWidget.widget.validate())) {
					previousDocFieldWidget = docFieldWidget;
					break;
				}
			} else if (docFieldWidget.textAreaWidget != null) {
				if (docFieldWidget.isCurrent) {
					reachedToCurrent = Boolean.TRUE;
					continue;
				}

				if (reachedToCurrent && !isError) {
					previousDocFieldWidget = docFieldWidget;
					break;
				}
			} else {
				if (docFieldWidget.isCurrent) {
					reachedToCurrent = Boolean.TRUE;
					continue;
				}
				if (reachedToCurrent && (!isError || !validateListBoxSelection(docFieldWidget.lWidget))) {
					previousDocFieldWidget = docFieldWidget;
					break;
				}
			}
		}
		return previousDocFieldWidget;
	}

	private DocFieldWidget getCurrentDocFieldWidget() {
		DocFieldWidget currentDocFieldWidget = null;
		for (DocFieldWidget docFieldWidget : new ReverseIterable<DocFieldWidget>(docFieldWidgets)) {
			if (docFieldWidget.widget != null) {
				if (docFieldWidget.isCurrent) {
					docFieldWidget.widget.getWidget().hideSuggestionList();
					currentDocFieldWidget = docFieldWidget;
					break;
				}

			} else if (docFieldWidget.textAreaWidget != null) {
				if (docFieldWidget.isCurrent) {
					currentDocFieldWidget = docFieldWidget;
					break;
				}
			} else {
				if (docFieldWidget.isCurrent) {
					currentDocFieldWidget = docFieldWidget;
					break;
				}
			}
		}
		return currentDocFieldWidget;
	}

	public String getCurrentDocFieldWidgetName() {
		String nameOfCurrentField = null;
		if (getCurrentDocFieldWidget() != null) {
			nameOfCurrentField = getCurrentDocFieldWidget().field.getName();
		}
		return nameOfCurrentField;
	}

	// private void setCurrentDocFieldWidget(DocFieldWidget dfWidget) {
	// setCurrentDocFieldWidget(dfWidget.field.getName());
	// }

	public void setForceReviewDoneForDocFieldWidgets() {
		DocFieldWidget currenDocFieldWidget = getCurrentDocFieldWidget();
		if (currenDocFieldWidget != null) {
			Field field = currenDocFieldWidget.field;
			if (field.isForceReview()) {
				field.setForceReview(false);
			}
			currenDocFieldWidget.setForceReviewOfValidatableWidget();
		}

	}

	private void setCurrentDocFieldWidget(String fieldName) {
		for (DocFieldWidget docFieldWidget : docFieldWidgets) {
			docFieldWidget.setCurrent(false);

			if (docFieldWidget.field.getName().equals(fieldName)) {
				docFieldWidget.setCurrent(true);
				// docFieldWidget.widget.getWidget().setFocus(true);
			}
		}
	}

	private void setCurrentFieldWidgetFocus(DocFieldWidget dfWidget) {
		dfWidget.setCurrent(true);
		if (dfWidget.widget != null) {
			dfWidget.widget.getWidget().setFocus(true);
		} else if (dfWidget.lWidget != null) {
			dfWidget.lWidget.getWidget().setFocus(true);
		} else if (dfWidget.textAreaWidget != null) {
			dfWidget.textAreaWidget.getWidget().setFocus(true);
		}
	}

	/**
	 * This method is used to set value of field name in the existing document level fields. This will set the value to the widget
	 * field value and validate it.
	 * 
	 * @param fieldName String
	 * @param fieldValue String
	 */
	private void setFuzzySearchValue(String fieldName, String fieldValue) {
		for (DocFieldWidget docFieldWidget : docFieldWidgets) {
			if (docFieldWidget.field.getName().equals(fieldName)) {
				if (docFieldWidget.widget != null) {
					docFieldWidget.widget.getWidget().setValue(fieldValue);
					docFieldWidget.field.setValue(fieldValue);
					docFieldWidget.widget.getWidget().getTextBox().setText(fieldValue);
					docFieldWidget.widget.toggleValidDateBox();
				} else {
					docFieldWidget.textAreaWidget.getWidget().setValue(fieldValue);
					docFieldWidget.field.setValue(fieldValue);
					docFieldWidget.textAreaWidget.getWidget().setText(fieldValue);
					docFieldWidget.textAreaWidget.toggleValidDateBox();
				}

			}
		}
	}

	private void setSuggestBoxEvents(DocField field, String input, ValidatableWidget<SuggestionBox> vWidget) {
		String inputString = input;
		int pos = inputString.lastIndexOf(SEPERATOR);
		int index = 0;
		String originalString = inputString;
		if (!(pos < 0)) {
			index = Integer.parseInt(inputString.substring(pos + ALTERNATE_STRING_VALUE.length() + SEPERATOR.length(), inputString
					.length()));
			inputString = inputString.substring(0, pos);
		}
		vWidget.getWidget().getTextBox().setText(inputString);
		vWidget.getWidget().setValue(inputString);
		vWidget.toggleValidDateBox();
		CoordinatesList coordinatesList = field.getCoordinatesList();
		int count = 0;
		if (field.getAlternateValues() != null) {
			for (Field alternateField : field.getAlternateValues().getAlternateValue()) {
				if (pos < 0) {
					if (alternateField.getValue().equals(inputString)) {
						ValidatePanel.this.fireEvent(new ValidationFieldChangeEvent(alternateField));
						coordinatesList = alternateField.getCoordinatesList();
					}
				} else {
					if (alternateField.getValue().equals(inputString)) {
						if (count == index) {
							ValidatePanel.this.fireEvent(new ValidationFieldChangeEvent(alternateField));
							coordinatesList = alternateField.getCoordinatesList();
						}
						count++;
					}
				}

			}
		}
		if (field.getValue().equals(originalString)) {
			ValidatePanel.this.fireEvent(new ValidationFieldChangeEvent(field));
		}
		updateDocument(coordinatesList, field.getName());
	}

	public void setTextAreaEvent(DocField field, String input, final ValidatableWidget<TextArea> textAreaWidget) {
		String inputString = input;
		int pos = inputString.lastIndexOf(SEPERATOR);
		int index = 0;
		String originalString = inputString;
		if (!(pos < 0)) {
			index = Integer.parseInt(inputString.substring(pos + ALTERNATE_STRING_VALUE.length() + SEPERATOR.length(), inputString
					.length()));
			inputString = inputString.substring(0, pos);
		}
		textAreaWidget.getWidget().setText(inputString);
		textAreaWidget.getWidget().setValue(inputString);
		// textAreaWidget.toggleValidDateBox();
		CoordinatesList coordinatesList = field.getCoordinatesList();
		int count = 0;
		if (field.getAlternateValues() != null) {
			for (Field alternateField : field.getAlternateValues().getAlternateValue()) {
				if (pos < 0) {
					if (alternateField.getValue().equals(inputString)) {
						ValidatePanel.this.fireEvent(new ValidationFieldChangeEvent(alternateField));
						coordinatesList = alternateField.getCoordinatesList();
					}
				} else {
					if (alternateField.getValue().equals(inputString)) {
						if (count == index) {
							ValidatePanel.this.fireEvent(new ValidationFieldChangeEvent(alternateField));
							coordinatesList = alternateField.getCoordinatesList();
						}
						count++;
					}
				}

			}
		}
		if (field.getValue().equals(originalString)) {
			ValidatePanel.this.fireEvent(new ValidationFieldChangeEvent(field));
		}
		updateDocument(coordinatesList, field.getName());
	}

	public Button getShowTableViewBtn() {
		return showTableViewBtn;
	}

	private boolean validateListBoxSelection(ValidatableWidget<ListBox> listboxWidget) {
		boolean isValid = true;
		listboxWidget.toggleValidDateBox();
		isValid = listboxWidget.validate();
		return isValid;
	}

	public void openNextDocument() {
		Document previousDoc = presenter.document;
		if (enableSwitchScriptValidation.equalsIgnoreCase(presenter.batchDTO.getIsValidationScriptEnabled())) {
			executeScript(previousDoc, false);
		} else {
			presenter.document = presenter.batchDTO.getNextDocumentTo(presenter.document, showErrorField);
			ValidatePanel.this.fireEvent(new TreeRefreshEvent(presenter.batchDTO, presenter.document, null));
		}
	}

	private void openPreviousDocument() {
		Document previousDoc = presenter.document;
		if (enableSwitchScriptValidation.equalsIgnoreCase(presenter.batchDTO.getIsValidationScriptEnabled())) {
			executeScript(previousDoc, true);
		} else {
			if (presenter.document == null) {
				presenter.document = previousDoc;
			}
			presenter.document = presenter.batchDTO.getPreviousDocumentTo(presenter.document, showErrorField);
			ValidatePanel.this.fireEvent(new TreeRefreshEvent(presenter.batchDTO, presenter.document, null));
		}
	}

	public void executeScript(final Document previousDocument, final boolean isPrevious) {
		final Batch batch = presenter.batchDTO.getBatch();
		ScreenMaskUtility.maskScreen("Executing Script....");

		presenter.rpcService.executeScript(batch, presenter.document, new AsyncCallback<BatchDTO>() {

			@Override
			public void onFailure(Throwable arg0) {
				ScreenMaskUtility.unmaskScreen();
				if (!presenter.displayErrorMessage(arg0)) {
					String title = LocaleDictionary.get().getConstantValue(ReviewValidateConstants.SCRIPT_EXECUTION);
					String message = LocaleDictionary.get().getMessageValue(ReviewValidateMessages.MSG_SCRIPT_EXECUTION_FAILED);
					showConfirmationDialog(title, message);
				}
			}

			@Override
			public void onSuccess(BatchDTO batchDTO) {
				presenter.batchDTO = batchDTO;
				Document doc = null;
				boolean showPopUp = false;
				List<Document> documents = batchDTO.getBatch().getDocuments().getDocument();
				Document selectedDocument = null;
				for (Document document : documents) {
					if (document.getIdentifier().equalsIgnoreCase(previousDocument.getIdentifier())) {
						if (!document.isValid()) {
							doc = document;
							selectedDocument = document;
							setFieldValues(previousDocument, selectedDocument);
							showPopUp = true;
						} else {
							if (isPrevious) {
								if (presenter.document == null) {
									presenter.document = previousDocument;
								}
								presenter.document = presenter.batchDTO.getPreviousDocumentTo(presenter.document, showErrorField);
							} else {
								presenter.document = presenter.batchDTO.getNextDocumentTo(presenter.document, showErrorField);
							}
							selectedDocument = presenter.document;
							ValidatePanel.this.fireEvent(new TreeRefreshEvent(presenter.batchDTO, presenter.document, null));
						}
						break;
					}
				}
				if (selectedDocument != null) {
					ValidatePanel.this.fireEvent(new DocumentRefreshEvent(selectedDocument));
				}
				ScreenMaskUtility.unmaskScreen();

				if (showPopUp) {
					ValidatePanel.this.fireEvent(new TreeRefreshEvent(presenter.batchDTO, doc, null));
				}

			}
		});
	}

	private void showConfirmationDialog(String title, String message) {
		final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(message, title, Boolean.TRUE);
		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				confirmationDialog.hide();
				ScreenMaskUtility.unmaskScreen();
			}

			@Override
			public void onCancelClick() {
				ScreenMaskUtility.unmaskScreen();
				presenter.setFocus();
			}
		});

	}

	/**
	 * The <code>setTimerToExecuteScript</code> method is for setting timer before executing script on value change.
	 */
	private void setTimerToExecuteScript() {
		Timer timer = new Timer() {

			@Override
			public void run() {
				if (!currentFieldSet) {
					presenter.executeScriptOnFieldChange(presenter.getFieldValueChangeName());
				}
			}
		};
		timer.schedule(300);
	}

	public void resetFocusToDocumentField(String fieldName) {
		if (null != fieldName) {
			for (DocFieldWidget docFieldWidget : docFieldWidgets) {
				if (docFieldWidget.field.getName().equals(fieldName)) {
					docFieldWidget.widget.getWidget().setFocus(true);
				}
			}
		}
	}

	private void setFieldValues(Document previousDocument, Document selectedDocument) {
		selectedDocument.setDocumentLevelFields(previousDocument.getDocumentLevelFields());
	}

	public boolean isFieldAlreadySelected() {
		return fieldAlreadySelected;
	}

	public void setFieldAlreadySelected(boolean fieldAlreadySelected) {
		this.fieldAlreadySelected = fieldAlreadySelected;
	}

	public void setCurrentFieldSet(boolean currentFieldSet) {
		this.currentFieldSet = currentFieldSet;
	}

	public boolean isCurrentFieldSet() {
		return currentFieldSet;
	}

	/**
	 * @param value
	 */
	private void performFuzzySearch(String value) {
		if (presenter.document != null) {
			presenter.rpcService.fuzzyTextSearch(presenter.batchDTO.getBatch(), presenter.document.getType(), value,
					new AsyncCallback<List<List<String>>>() {

						@Override
						public void onFailure(Throwable arg0) {
							final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(
									LocaleDictionary.get().getMessageValue(ReviewValidateMessages.MSG_FUZZY_SEARCH_UNSUCCESSFUL),
									LocaleDictionary.get().getConstantValue(ReviewValidateConstants.FUZZY_SEARCH_TOOLTIP),
									Boolean.TRUE);

							confirmationDialog.addDialogListener(new DialogListener() {

								@Override
								public void onOkClick() {
									confirmationDialog.hide();
									ScreenMaskUtility.unmaskScreen();
									presenter.setFocus();
								}

								@Override
								public void onCancelClick() {
									ScreenMaskUtility.unmaskScreen();
									presenter.setFocus();
								}
							});

						}

						@Override
						public void onSuccess(List<List<String>> arg0) {

							if (arg0 == null || arg0.isEmpty()) {
								final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(
										LocaleDictionary.get().getMessageValue(ReviewValidateMessages.MSG_FUZZY_SEARCH_NO_RESULT),
										LocaleDictionary.get().getConstantValue(ReviewValidateConstants.FUZZY_SEARCH_TOOLTIP),
										Boolean.TRUE);

								confirmationDialog.addDialogListener(new DialogListener() {

									@Override
									public void onOkClick() {
										confirmationDialog.hide();
										ScreenMaskUtility.unmaskScreen();
										presenter.setFocus();
									}

									@Override
									public void onCancelClick() {
										ScreenMaskUtility.unmaskScreen();
										presenter.setFocus();
									}

								});

							} else {
								fuzzyDataCarrier = arg0;

								final DialogBox dialogBox = new DialogBox();
								dialogBox.addStyleName(ReviewValidateConstants.CONFIGURED_DIMENSIONS_DIALOG);
								final FuzzySearchResultView fuzzySearchResultView = new FuzzySearchResultView(presenter,
										presenter.batchDTO.getFuzzySearchPopUpXDimension(), presenter.batchDTO
												.getFuzzySearchPopUpYDimension());
								fuzzySearchResultView.setEventBus(eventBus);
								dialogBox.setWidget(fuzzySearchResultView);
								fuzzySearchResultView.setDialogBox(dialogBox);
								fuzzySearchResultView.createBatchList(arg0);

								fuzzySearchResultView.setWidth(presenter.batchDTO.getFuzzySearchPopUpYDimension());
								dialogBox.center();
								dialogBox.setText(LocaleDictionary.get().getConstantValue(ReviewValidateConstants.FUZZY_SEARCH_TITLE));
								dialogBox.show();
								fuzzySearchResultView.getSelectBtn().setFocus(true);
							}
						}

					});
		}
	}
}
