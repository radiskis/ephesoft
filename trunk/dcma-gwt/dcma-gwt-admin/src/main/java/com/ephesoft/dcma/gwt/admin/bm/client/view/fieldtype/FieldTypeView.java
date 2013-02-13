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

package com.ephesoft.dcma.gwt.admin.bm.client.view.fieldtype;

import java.util.LinkedList;
import java.util.List;

import com.ephesoft.dcma.core.common.KVFetchValue;
import com.ephesoft.dcma.core.common.KVPageValue;
import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.fieldtype.FieldTypeViewPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.kvextraction.KVTypeListPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.regex.RegexListPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.kvextraction.KVFieldTypeListView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.regex.RegexListView;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.table.Record;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.KVExtractionDTO;
import com.ephesoft.dcma.gwt.core.shared.RegexDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class provides functionality to edit fieldtype.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class FieldTypeView extends View<FieldTypeViewPresenter> {

	/**
	 * TOP_PADD String.
	 */
	private static final String TOP_PADD = "topPadd";

	/**
	 * HEIGHT String.
	 */
	private static final String HEIGHT = "20px";

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<DockLayoutPanel, FieldTypeView> {
	}

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * kvFieldTypeListViewv KVFieldTypeListView.
	 */
	private final KVFieldTypeListView kvFieldTypeListView;

	/**
	 * regexListView RegexListView.
	 */
	private final RegexListView regexListView;

	/**
	 * kvFieldTypeListPresenter KVTypeListPresenter.
	 */
	private KVTypeListPresenter kvFieldTypeListPresenter;

	/**
	 * regexListPresenter RegexListPresenter.
	 */
	private RegexListPresenter regexListPresenter;

	/**
	 * kvFieldTypeListPanel LayoutPanel.
	 */
	@UiField
	protected LayoutPanel kvFieldTypeListPanel;

	/**
	 * regexListPanel LayoutPanel.
	 */
	@UiField
	protected LayoutPanel regexListPanel;

	/**
	 * fieldTypeDetailView FieldTypeDetailView.
	 */
	@UiField
	protected FieldTypeDetailView fieldTypeDetailView;

	/**
	 * editFieldTypeView EditFieldTypeView.
	 */
	@UiField
	protected EditFieldTypeView editFieldTypeView;

	/**
	 * fieldTypeVerticalPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel fieldTypeVerticalPanel;

	/**
	 * fieldTypeConfigVerticalPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel fieldTypeConfigVerticalPanel;

	/**
	 * fieldConfigurationCaptionPanel CaptionPanel.
	 */
	@UiField
	protected CaptionPanel fieldConfigurationCaptionPanel;

	/**
	 * kvExtractionCompletePanel DockLayoutPanel.
	 */
	@UiField
	protected DockLayoutPanel kvExtractionCompletePanel;

	/**
	 * addKVButton Button.
	 */
	@UiField
	protected Button addKVButton;

	/**
	 * testKVButton Button.
	 */
	@UiField
	protected Button testKVButton;

	/**
	 * editKVButton Button.
	 */
	@UiField
	protected Button editKVButton;

	/**
	 * deleteKVButton Button.
	 */
	@UiField
	protected Button deleteKVButton;

	/**
	 * addRegexBtn Button.
	 */
	@UiField
	protected Button addRegexBtn;

	/**
	 * editRegexBtn Button.
	 */
	@UiField
	protected Button editRegexBtn;

	/**
	 * deleteRegexBtn Button.
	 */
	@UiField
	protected Button deleteRegexBtn;

	/**
	 * kvButtonPanel HorizontalPanel.
	 */
	@UiField
	protected HorizontalPanel kvButtonPanel;

	/**
	 * regexButtonPanel HorizontalPanel.
	 */
	@UiField
	protected HorizontalPanel regexButtonPanel;

	/**
	 * addAdvancedKVAddButton Button.
	 */
	@UiField
	protected Button addAdvancedKVAddButton;

	/**
	 * editAdvancedKVButton Button.
	 */
	@UiField
	protected Button editAdvancedKVButton;

	/**
	 * Constructor.
	 */
	public FieldTypeView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		kvFieldTypeListView = new KVFieldTypeListView();
		kvFieldTypeListPanel.add(kvFieldTypeListView.listView);
		regexListView = new RegexListView();
		regexListPanel.add(regexListView.listView);
		addKVButton.setText(AdminConstants.ADD_BUTTON);
		editKVButton.setText(AdminConstants.EDIT_BUTTON);
		deleteKVButton.setText(AdminConstants.DELETE_BUTTON);
		testKVButton.setText(AdminConstants.TEST_KV_BUTTON);
		testKVButton.setTitle(AdminConstants.TOOLTIP_TEST_KV_BUTTON);
		addRegexBtn.setText(AdminConstants.ADD_BUTTON);
		deleteRegexBtn.setText(AdminConstants.DELETE_BUTTON);
		editRegexBtn.setText(AdminConstants.EDIT_BUTTON);
		addAdvancedKVAddButton.setText(AdminConstants.ADD_ADVANCED_KV_EXTRACTION_BUTTON);
		editAdvancedKVButton.setText(AdminConstants.EDIT_ADVANCED_KV_EXTRACTION_BUTTON);

		fieldConfigurationCaptionPanel.setCaptionHTML(AdminConstants.FIELD_DETAILS_HTML);
		addKVButton.setHeight(HEIGHT);
		editKVButton.setHeight(HEIGHT);
		deleteKVButton.setHeight(HEIGHT);
		addAdvancedKVAddButton.setHeight(HEIGHT);
		editAdvancedKVButton.setHeight(HEIGHT);
		addRegexBtn.setHeight(HEIGHT);
		editRegexBtn.setHeight(HEIGHT);
		deleteRegexBtn.setHeight(HEIGHT);
		testKVButton.setHeight(HEIGHT);
		kvButtonPanel.addStyleName(TOP_PADD);
		regexButtonPanel.addStyleName(TOP_PADD);

	}

	/**
	 * To get Field Type Detail View.
	 * 
	 * @return FieldTypeDetailView
	 */
	public FieldTypeDetailView getFieldTypeDetailView() {
		return fieldTypeDetailView;
	}

	/**
	 * To get KV Field Type List View.
	 * 
	 * @return KVFieldTypeListView
	 */
	public KVFieldTypeListView getKVFieldTypeListView() {
		return kvFieldTypeListView;
	}

	/**
	 * To create KV Field List.
	 * 
	 * @param fields List<KVExtractionDTO>
	 */
	public void createKVFieldList(List<KVExtractionDTO> fields) {
		List<Record> recordList = setFieldsList(fields);

		kvFieldTypeListPresenter = new KVTypeListPresenter(presenter.getController(), kvFieldTypeListView);
		kvFieldTypeListView.listView.initTable(recordList.size(), null, null, recordList, true, false, kvFieldTypeListPresenter, null,
				true);
	}

	private List<Record> setFieldsList(List<KVExtractionDTO> fields) {

		List<Record> recordList = new LinkedList<Record>();
		for (final KVExtractionDTO kvExtractionDTO : fields) {
			Record record = new Record(kvExtractionDTO.getIdentifier());
			record.addWidget(kvFieldTypeListView.keyPattern, new Label(kvExtractionDTO.getKeyPattern()));
			record.addWidget(kvFieldTypeListView.valuePattern, new Label(kvExtractionDTO.getValuePattern()));
			if (kvExtractionDTO.getLocationType() != null) {
				record.addWidget(kvFieldTypeListView.location, new Label(kvExtractionDTO.getLocationType().name()));
			}
			String noOfWords = String.valueOf(kvExtractionDTO.getNoOfWords());
			if (noOfWords != null && noOfWords.length() != 0) {
				record.addWidget(kvFieldTypeListView.noOfWords, new Label(noOfWords));
			} else {
				record.addWidget(kvFieldTypeListView.noOfWords, new Label(AdminConstants.EMPTY_STRING));
			}
			KVFetchValue fetchValue = kvExtractionDTO.getFetchValue();
			if (fetchValue != null) {
				record.addWidget(kvFieldTypeListView.fetchValue, new Label(kvExtractionDTO.getFetchValue().name()));
			} else {
				record.addWidget(kvFieldTypeListView.fetchValue, new Label(AdminConstants.EMPTY_STRING));
			}
			KVPageValue kvPageValue = kvExtractionDTO.getKvPageValue();
			if (kvPageValue != null) {
				record.addWidget(kvFieldTypeListView.kvPageValue, new Label(kvExtractionDTO.getKvPageValue().name()));
			} else {
				record.addWidget(kvFieldTypeListView.kvPageValue, new Label(AdminConstants.EMPTY_STRING));
			}
			Float multiplier = kvExtractionDTO.getMultiplier();
			if (multiplier != null) {
				float Rval = multiplier;
				int Rpl = 2;
				float pVarLocal = (float) Math.pow(BatchClassManagementConstants.TEN, Rpl);
				Rval = Rval * pVarLocal;
				float tmp = Math.round(Rval);
				Float fVarLocal = (float) tmp / pVarLocal;
				record.addWidget(kvFieldTypeListView.multiplier, new Label(String.valueOf(fVarLocal.toString())));
			} else {
				record.addWidget(kvFieldTypeListView.multiplier, new Label(AdminConstants.EMPTY_STRING));
			}
			recordList.add(record);
		}

		return recordList;
	}

	/**
	 * To create Regex List.
	 * 
	 * @param regex List<RegexDTO>
	 */
	public void createRegexList(List<RegexDTO> regex) {
		List<Record> recordList = setRegexList(regex);
		regexListPresenter = new RegexListPresenter(presenter.getController(), regexListView);
		regexListView.listView.initTable(recordList.size(), null, null, recordList, true, false, regexListPresenter, null, true);
	}

	private List<Record> setRegexList(List<RegexDTO> regexDTOs) {
		List<Record> records = new LinkedList<Record>();
		for (final RegexDTO regexDTO : regexDTOs) {
			Record record = new Record(regexDTO.getIdentifier());
			record.addWidget(regexListView.pattern, new Label(regexDTO.getPattern()));
			records.add(record);
		}
		return records;
	}

	/**
	 * To get Edit Field Type View.
	 * 
	 * @return EditFieldTypeView
	 */
	public EditFieldTypeView getEditFieldTypeView() {
		return editFieldTypeView;
	}

	/**
	 * To get Field Type Vertical Panel.
	 * 
	 * @return VerticalPanel
	 */
	public VerticalPanel getFieldTypeVerticalPanel() {
		return fieldTypeVerticalPanel;
	}

	/**
	 * To get Field Type Config Vertical Panel.
	 * 
	 * @return VerticalPanel
	 */
	public VerticalPanel getFieldTypeConfigVerticalPanel() {
		return fieldTypeConfigVerticalPanel;
	}

	/**
	 * To get Add KV Button.
	 * 
	 * @return Button
	 */
	public Button getAddKVButtonButton() {
		return addKVButton;
	}

	/**
	 * To perform operations on Test KV Extraction Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("testKVButton")
	public void onTestKVExtractionButtonClick(ClickEvent clickEvent) {
		String identifier = kvFieldTypeListView.listView.getSelectedRowIndex();
		int rowCount = kvFieldTypeListView.listView.getTableRecordCount();
		if (identifier == null || identifier.isEmpty()) {
			if (rowCount == 0) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NONE_SELECTED_WARNING));
			} else {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NONE_SELECTED_WARNING));
			}
			return;
		}
		presenter.onTestKVButtonClicked(identifier);
	}

	/**
	 * To perform operations on add KV Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("addKVButton")
	public void onAddKVButtonClick(ClickEvent clickEvent) {
		presenter.onAddKVButtonClicked();
	}

	/**
	 * To perform operations on add advanced KV add Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("addAdvancedKVAddButton")
	public void onAddAdvancedKVAddButtonClicked(ClickEvent clickEvent) {
		presenter.onAdvancedKVAddButton();
	}

	/**
	 * To perform operations on edit advanced KV Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("editAdvancedKVButton")
	public void onEditAdvancedKVButtonClicked(ClickEvent clickEvent) {
		String identifier = kvFieldTypeListView.listView.getSelectedRowIndex();
		int rowCount = kvFieldTypeListView.listView.getTableRecordCount();
		if (identifier == null || identifier.isEmpty()) {
			if (rowCount == 0) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NO_RECORD_TO_EDIT));
			} else {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NONE_SELECTED_WARNING));
			}
			return;
		}
		presenter.onAdvancedEditKVButtonClicked(identifier);
	}

	/**
	 * To perform operations on edit KV Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("editKVButton")
	public void onEditKVButtonClicked(ClickEvent clickEvent) {
		kvFieldTypeListPresenter.onEditButtonClicked();
	}

	/**
	 * To perform operations on delete KV Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("deleteKVButton")
	public void onDeleteKVButtonClicked(ClickEvent clickEvent) {
		final String identifier = kvFieldTypeListView.listView.getSelectedRowIndex();
		int rowCount = kvFieldTypeListView.listView.getTableRecordCount();
		if (identifier == null || identifier.isEmpty()) {
			if (rowCount == 0) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NO_RECORD_TO_DELETE));
			} else {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NONE_SELECTED_WARNING));
			}
			return;
		}
		final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get()
				.getMessageValue(BatchClassManagementMessages.DELETE_KV_TYPE_CONFORMATION), LocaleDictionary.get().getConstantValue(
				BatchClassManagementConstants.DELETE_KV_TITLE), Boolean.FALSE);

		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				confirmationDialog.hide();
				presenter.onDeleteKVButtonClicked(identifier);
			}

			@Override
			public void onCancelClick() {
				confirmationDialog.hide();
			}
		});

	}

	/**
	 * To perform operations on edit regex Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("editRegexBtn")
	public void onEditRegexBtnClicked(ClickEvent clickEvent) {
		regexListPresenter.onEditButtonClicked();
	}

	/**
	 * To perform operations on add regex Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("addRegexBtn")
	public void onAddRegexBtnClicked(ClickEvent clickEvent) {
		if (((fieldTypeDetailView.getIsHidden().getValue()) || (fieldTypeDetailView.getIsMultiLine().getValue()) || (fieldTypeDetailView
				.getIsReadOnly().getValue()))
				|| ((editFieldTypeView.getIsHidden().getValue()) || (editFieldTypeView.getIsMultiLine().getValue()) || (editFieldTypeView
						.getIsReadonlyValue()))) {
			final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get()
					.getMessageValue(BatchClassManagementMessages.ADD_REGEX_FAILURE), LocaleDictionary.get().getConstantValue(
					BatchClassManagementConstants.ADD_REGEX_FAILURE_TITLE), Boolean.TRUE);

			confirmationDialog.addDialogListener(new DialogListener() {

				@Override
				public void onOkClick() {
					confirmationDialog.hide();
				}

				@Override
				public void onCancelClick() {
					confirmationDialog.hide();
				}
			});

		} else {
			presenter.onAddRegexBtnClicked();
		}
	}

	/**
	 * To perform operations on delete regex Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("deleteRegexBtn")
	public void onDeleteRegexButtonClicked(ClickEvent clickEvent) {
		final String identifier = regexListView.listView.getSelectedRowIndex();
		int rowCount = regexListView.listView.getTableRecordCount();
		if (identifier == null || identifier.isEmpty()) {
			if (rowCount == 0) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NO_RECORD_TO_DELETE));
			} else {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NONE_SELECTED_WARNING));
			}
			return;
		}
		final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get()
				.getMessageValue(BatchClassManagementMessages.DELETE_REGEX_TYPE_CONFORMATION), LocaleDictionary.get()
				.getConstantValue(BatchClassManagementConstants.DELETE_REGEX_TITLE), Boolean.FALSE);

		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				confirmationDialog.hide();
				presenter.onDeleteRegexBtnClicked(identifier);
			}

			@Override
			public void onCancelClick() {
				confirmationDialog.hide();
			}
		});

	}

	/**
	 * To get Conformation for KV Extraction View.
	 * 
	 * @param isAdvanced boolean
	 * @param kvExtractionDTO KVExtractionDTO
	 */
	public void getConformationForKVExtractionView(final boolean isAdvanced, final KVExtractionDTO kvExtractionDTO) {
		final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get()
				.getMessageValue(BatchClassManagementMessages.DATA_LOSS), LocaleDictionary.get().getConstantValue(
				BatchClassManagementConstants.CONTINUE_CONFORMATION), Boolean.FALSE);

		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				confirmationDialog.hide();
				if (isAdvanced) {
					presenter.showAdvancedKVExtractionView(kvExtractionDTO.getIdentifier());
				} else {
					presenter.showSimpleKVExtractionView(kvExtractionDTO);
				}
			}

			@Override
			public void onCancelClick() {
				confirmationDialog.hide();
			}
		});

	}
}
