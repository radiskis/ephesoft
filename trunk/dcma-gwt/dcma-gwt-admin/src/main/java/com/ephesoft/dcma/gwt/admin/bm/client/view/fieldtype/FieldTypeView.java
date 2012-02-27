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

package com.ephesoft.dcma.gwt.admin.bm.client.view.fieldtype;

import java.util.LinkedList;
import java.util.List;

import com.ephesoft.dcma.core.common.KVFetchValue;
import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.fieldtype.FieldTypeViewPresenter;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FieldTypeView extends View<FieldTypeViewPresenter> {

	interface Binder extends UiBinder<DockLayoutPanel, FieldTypeView> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);

	private final KVFieldTypeListView kvFieldTypeListView;

	private final RegexListView regexListView;

	@UiField
	protected LayoutPanel kvFieldTypeListPanel;

	@UiField
	protected LayoutPanel regexListPanel;

	@UiField
	protected FieldTypeDetailView fieldTypeDetailView;

	@UiField
	protected EditFieldTypeView editFieldTypeView;

	@UiField
	protected Button editFieldPropertiesButton;

	@UiField
	protected VerticalPanel fieldTypeVerticalPanel;

	@UiField
	protected VerticalPanel fieldTypeConfigVerticalPanel;

	@UiField
	protected CaptionPanel fieldConfigurationCaptionPanel;

	@UiField
	DockLayoutPanel kvExtractionCompletePanel;

	@UiField
	protected Button addKVButton;

	@UiField
	protected Button testKVButton;

	@UiField
	protected Button editKVButton;

	@UiField
	protected Button deleteKVButton;

	@UiField
	protected Button addRegexBtn;

	@UiField
	protected Button editRegexBtn;

	@UiField
	protected Button deleteRegexBtn;

	@UiField
	protected HorizontalPanel kvButtonPanel;

	@UiField
	protected HorizontalPanel regexButtonPanel;

	@UiField
	protected Button addAdvancedKVAddButton;

	@UiField
	protected Button editAdvancedKVButton;

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
		editFieldPropertiesButton.setText(AdminConstants.EDIT_BUTTON);
		fieldConfigurationCaptionPanel.setCaptionHTML(AdminConstants.FIELD_DETAILS_HTML);
		addKVButton.setHeight("20px");
		editKVButton.setHeight("20px");
		deleteKVButton.setHeight("20px");
		addRegexBtn.setHeight("20px");
		editRegexBtn.setHeight("20px");
		deleteRegexBtn.setHeight("20px");
		testKVButton.setHeight("20px");
		kvButtonPanel.addStyleName("topPadd");
		regexButtonPanel.addStyleName("topPadd");

		fieldTypeVerticalPanel.add(editFieldPropertiesButton);

	}

	public FieldTypeDetailView getFieldTypeDetailView() {
		return fieldTypeDetailView;
	}

	public KVFieldTypeListView getKVFieldTypeListView() {
		return kvFieldTypeListView;
	}

	public void createKVFieldList(List<KVExtractionDTO> fields) {
		List<Record> recordList = setFieldsList(fields);

		kvFieldTypeListView.listView.initTable(recordList.size(), recordList, true);
	}

	private List<Record> setFieldsList(List<KVExtractionDTO> fields) {

		List<Record> recordList = new LinkedList<Record>();
		for (final KVExtractionDTO kvExtractionDTO : fields) {
			Record record = new Record(kvExtractionDTO.getIdentifier());
			record.addWidget(kvFieldTypeListView.keyPattern, new Label(kvExtractionDTO.getKeyPattern()));
			record.addWidget(kvFieldTypeListView.location, new Label(kvExtractionDTO.getLocationType().name()));
			record.addWidget(kvFieldTypeListView.valuePattern, new Label(kvExtractionDTO.getValuePattern()));
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
			Float multiplier = kvExtractionDTO.getMultiplier();
			if (multiplier != null) {
				float Rval = multiplier;
				int Rpl = 2;
				float pVarLocal = (float) Math.pow(10, Rpl);
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

	public void createRegexList(List<RegexDTO> regex) {
		List<Record> recordList = setRegexList(regex);

		regexListView.listView.initTable(recordList.size(), recordList, true);
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

	public EditFieldTypeView getEditFieldTypeView() {
		return editFieldTypeView;
	}

	public Button getEditFieldPropertiesButtonButton() {
		return editFieldPropertiesButton;
	}

	public VerticalPanel getFieldTypeVerticalPanel() {
		return fieldTypeVerticalPanel;
	}

	public VerticalPanel getFieldTypeConfigVerticalPanel() {
		return fieldTypeConfigVerticalPanel;
	}

	public Button getAddKVButtonButton() {
		return addKVButton;
	}

	@UiHandler("editFieldPropertiesButton")
	public void onEditFieldPropertiesButtonClick(ClickEvent clickEvent) {
		presenter.onEditFieldPropertiesButtonClicked();
	}

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

	@UiHandler("addKVButton")
	public void onAddKVButtonClick(ClickEvent clickEvent) {
		presenter.onAddKVButtonClicked();
	}

	@UiHandler("addAdvancedKVAddButton")
	public void onAddAdvancedKVAddButtonClicked(ClickEvent clickEvent) {
		presenter.onAdvancedKVAddButton();
	}

	@UiHandler("editAdvancedKVButton")
	public void onEditAdvancedKVButtonClicked(ClickEvent clickEvent) {
		String identifier = kvFieldTypeListView.listView.getSelectedRowIndex();
		int rowCount = kvFieldTypeListView.listView.getTableRecordCount();
		if (identifier == null || identifier.isEmpty()) {
			if (rowCount == 0) {
				Window.alert(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.NO_RECORD_TO_EDIT));
			} else {
				Window.alert(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.NONE_SELECTED_WARNING));
			}
			return;
		}
		presenter.onAdvancedEditKVButtonClicked(identifier);
	}

	@UiHandler("editKVButton")
	public void onEditKVButtonClicked(ClickEvent clickEvent) {
		String identifier = kvFieldTypeListView.listView.getSelectedRowIndex();
		int rowCount = kvFieldTypeListView.listView.getTableRecordCount();
		if (identifier == null || identifier.isEmpty()) {
			if (rowCount == 0) {
				Window.alert(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.NO_RECORD_TO_EDIT));
			} else {
				Window.alert(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.NONE_SELECTED_WARNING));
			}
			return;
		}
		presenter.onEditKVButtonClicked(identifier);

	}

	@UiHandler("deleteKVButton")
	public void onDeleteKVButtonClicked(ClickEvent clickEvent) {
		final String identifier = kvFieldTypeListView.listView.getSelectedRowIndex();
		int rowCount = kvFieldTypeListView.listView.getTableRecordCount();
		if (identifier == null || identifier.isEmpty()) {
			if (rowCount == 0) {
				Window.alert(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.NO_RECORD_TO_DELETE));
			} else {
				Window.alert(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.NONE_SELECTED_WARNING));
			}
			return;
		}
		final ConfirmationDialog confirmationDialog = new ConfirmationDialog();
		confirmationDialog
				.setMessage(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.DELETE_KV_TYPE_CONFORMATION));
		confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.DELETE_KV_TITLE));
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
		confirmationDialog.center();
		confirmationDialog.show();
		confirmationDialog.okButton.setFocus(true);
	}

	@UiHandler("editRegexBtn")
	public void onEditRegexBtnClicked(ClickEvent clickEvent) {
		String identifier = regexListView.listView.getSelectedRowIndex();
		int rowCount = regexListView.listView.getTableRecordCount();
		if (identifier == null || identifier.isEmpty()) {
			if (rowCount == 0) {
				Window.alert(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.NO_RECORD_TO_EDIT));
			} else {
				Window.alert(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.NONE_SELECTED_WARNING));
			}
			return;
		}
		presenter.onEditRegexButtonClicked(identifier);
	}

	@UiHandler("addRegexBtn")
	public void onAddRegexBtnClicked(ClickEvent clickEvent) {
		if (fieldTypeDetailView.getIsHidden().getValue() || editFieldTypeView.getIsHidden().getValue()) {
			final ConfirmationDialog confirmationDialog = new ConfirmationDialog(true);
			confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.ADD_REGEX_FAILURE));
			confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(
					BatchClassManagementConstants.ADD_REGEX_FAILURE_TITLE));
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
			confirmationDialog.center();
			confirmationDialog.show();
			confirmationDialog.okButton.setFocus(true);
		} else {
			presenter.onAddRegexBtnClicked();
		}
	}

	@UiHandler("deleteRegexBtn")
	public void onDeleteRegexButtonClicked(ClickEvent clickEvent) {
		final String identifier = regexListView.listView.getSelectedRowIndex();
		int rowCount = regexListView.listView.getTableRecordCount();
		if (identifier == null || identifier.isEmpty()) {
			if (rowCount == 0) {
				Window.alert(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.NO_RECORD_TO_DELETE));
			} else {
				Window.alert(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.NONE_SELECTED_WARNING));
			}
			return;
		}
		final ConfirmationDialog confirmationDialog = new ConfirmationDialog();
		confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(
				BatchClassManagementMessages.DELETE_REGEX_TYPE_CONFORMATION));
		confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.DELETE_REGEX_TITLE));
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
		confirmationDialog.center();
		confirmationDialog.show();
		confirmationDialog.okButton.setFocus(true);
	}

	public void getConformationForKVExtractionView(final boolean isAdvanced, final KVExtractionDTO kvExtractionDTO) {
		final ConfirmationDialog confirmationDialog = new ConfirmationDialog();
		confirmationDialog
				.setDialogTitle(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.CONTINUE_CONFORMATION));
		confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.DATA_LOSS));
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
		confirmationDialog.center();
		confirmationDialog.show();
		confirmationDialog.okButton.setFocus(true);
	}
}
