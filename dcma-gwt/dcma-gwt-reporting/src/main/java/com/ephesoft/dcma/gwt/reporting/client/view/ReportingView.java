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

package com.ephesoft.dcma.gwt.reporting.client.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.table.Record;
import com.ephesoft.dcma.gwt.core.client.validator.RegExValidatableWidget;
import com.ephesoft.dcma.gwt.core.shared.ReportDTO;
import com.ephesoft.dcma.gwt.reporting.client.i18n.ReportingConstants;
import com.ephesoft.dcma.gwt.reporting.client.presenter.ReportingPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Class that is used for the view presented to user on landing on batch list page.
 * 
 * @author Ephesoft
 * 
 */
public class ReportingView extends View<ReportingPresenter> {

	@UiField
	protected Label completeInfo;
	@UiField
	protected Label totalBatchesLabel;
	@UiField
	protected Label totalBatches;
	@UiField
	protected Label totalDocumentsLabel;
	@UiField
	protected Label totalDocuments;
	@UiField
	protected Label totalPagesLabel;
	@UiField
	protected Label totalPages;
	@UiField
	protected Button syncButton;

	@UiField
	protected RadioButton moduleSelection;
	@UiField
	protected RadioButton pluginSelection;
	@UiField
	protected RadioButton userSelection;

	@UiField
	protected ListBox userList;
	@UiField
	protected ListBox intervalListBox;
	@UiField
	protected ListBox batchClassList;

	@UiField
	protected Button goButton;

	@UiField
	protected Button commonButton;

	@UiField
	protected LayoutPanel reportTablePanel;

	/*
	 * @UiField CalendarWidget startDateCalendar;
	 */

	@UiField
	protected TextBox startDateText;

	@UiField
	protected TextBox endDateText;

	@UiField
	protected VerticalPanel labelPanel;

	@UiField
	protected DockLayoutPanel reportPanel;

	@UiField
	protected HorizontalPanel datePanel;

	@UiField
	protected HorizontalPanel batchClassPanel;

	@UiField
	protected Label startDateFormat;

	@UiField
	protected Label endDateFormat;

	protected final ReportingListView reportingListView;

	private List<ReportDTO> reportList;

	private final RegExValidatableWidget<TextBox> startDateWidget;
	private final RegExValidatableWidget<TextBox> endDateWidget;

	interface Binder extends UiBinder<DockLayoutPanel, ReportingView> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);

	public ReportingView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		completeInfo.setText(LocaleDictionary.get().getConstantValue(ReportingConstants.SYSTEM_STATISTICS_LABEL));
		completeInfo.addStyleName(ReportingConstants.BOLD_TEXT_CSS);
		completeInfo.addStyleName(ReportingConstants.SYSTEM_STATISTICS_CSS);

		totalBatchesLabel.setText(LocaleDictionary.get().getConstantValue(ReportingConstants.TOTAL_BATCHES_PROCESSED_LABEL));
		totalBatchesLabel.addStyleName(ReportingConstants.BOLD_TEXT_CSS);
		totalBatchesLabel.addStyleName(ReportingConstants.SYSTEM_STATISTICS_INNER_CSS);

		totalDocumentsLabel.setText(LocaleDictionary.get().getConstantValue(ReportingConstants.TOTAL_DOCUMENTS_PROCESSED_LABEL));
		totalDocumentsLabel.addStyleName(ReportingConstants.BOLD_TEXT_CSS);

		totalPagesLabel.setText(LocaleDictionary.get().getConstantValue(ReportingConstants.TOTAL_PAGES_PROCESSED));
		totalPagesLabel.addStyleName(ReportingConstants.BOLD_TEXT_CSS);

		syncButton.setText(LocaleDictionary.get().getConstantValue(ReportingConstants.SYNC_DB_BUTTON_TEXT));
		syncButton.addStyleName(ReportingConstants.SYNC_BD_BUTTON_CSS);

		commonButton.setText(LocaleDictionary.get().getConstantValue(ReportingConstants.COMMON_BUTTON_TEXT));
		commonButton.addStyleName(ReportingConstants.COMMON_BUTTON_CSS);

		startDateFormat.setText(ReportingConstants.OPENING_BRACKET + ReportingConstants.DATE_FORMAT_SAMPLE_VALUE
				+ ReportingConstants.CLOSING_BRACKET);
		startDateFormat.addStyleName(ReportingConstants.SAMPLE_FORMAT_CSS);

		endDateFormat.setText(ReportingConstants.OPENING_BRACKET + ReportingConstants.DATE_FORMAT_SAMPLE_VALUE
				+ ReportingConstants.CLOSING_BRACKET);
		endDateFormat.addStyleName(ReportingConstants.SAMPLE_FORMAT_CSS);

		addSelectionHandler(moduleSelection);
		addSelectionHandler(pluginSelection);
		addSelectionHandler(userSelection);

		goButton.setText(LocaleDictionary.get().getConstantValue(ReportingConstants.GO_BUTTON_TEXT));

		batchClassPanel.setSpacing(15);
		batchClassList.setMultipleSelect(true);
		batchClassList.setVisibleItemCount(3);
		batchClassList.setSelectedIndex(1);

		datePanel.setSpacing(15);

		populateIntervalListBox();

		reportingListView = new ReportingListView();

		reportingListView.listView.initTable(0, presenter, null, new ArrayList<Record>(), false, false, null, false);

		reportTablePanel.add(reportingListView.listView);

		addCalendarListener();

		moduleSelection.setValue(true);
		userList.setEnabled(false);

		labelPanel.addStyleName(ReportingConstants.SYSTEM_STATISTICS_BOX_CSS);

		Date startDate = new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000));

		Date endDate = new Date(System.currentTimeMillis());

		endDateText.setText(getDate(endDate));

		startDateText.setText(getDate(startDate));

		reportPanel.addStyleName(ReportingConstants.SYSTEM_STATISTICS_BOX_CSS);

		addIntervalChangeHandler();

		startDateWidget = new RegExValidatableWidget<TextBox>(startDateText);

		startDateWidget.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				startDateWidget.toggleValidDateBox();
			}
		});

		endDateWidget = new RegExValidatableWidget<TextBox>(endDateText);

		endDateWidget.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				endDateWidget.toggleValidDateBox();
			}
		});
	}

	private String getDate(Date date) {
		return DateTimeFormat.getFormat(ReportingConstants.DATE_FORMAT_SPECIFIED).format(date);
	}

	private void addCalendarListener() {
		// add calender listener
	}

	private void addSelectionHandler(final RadioButton radioButton) {
		radioButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				userList.setEnabled(false);
				moduleSelection.setValue(false);
				pluginSelection.setValue(false);
				userSelection.setValue(false);
				radioButton.setValue(true);
				if (userSelection.getValue()) {
					userList.setEnabled(true);
				}
			}
		});
	}

	public void populateUserListBox(List<String> users) {
		userList.clear();
		for (String user : users) {
			userList.addItem(user);
		}
	}

	public void populateBatchClassListBox(Map<String, String> batchClasses) {
		batchClassList.clear();
		for (Iterator<String> iterator = batchClasses.keySet().iterator(); iterator.hasNext();) {
			String optionValue = (String) iterator.next();
			batchClassList.addItem(batchClasses.get(optionValue), optionValue);
		}
		batchClassList.setSelectedIndex(0);
	}

	public final void populateIntervalListBox() {
		intervalListBox.addItem(LocaleDictionary.get().getConstantValue(ReportingConstants.INTERVAL_LISTBOX_TEXT_SECONDS),
				ReportingConstants.INTERVAL_LISTBOX_VALUE_SECONDS);
		intervalListBox.addItem(LocaleDictionary.get().getConstantValue(ReportingConstants.INTERVAL_LISTBOX_TEXT_MINUTES),
				ReportingConstants.INTERVAL_LISTBOX_VALUE_MINUTES);
		intervalListBox.addItem(LocaleDictionary.get().getConstantValue(ReportingConstants.INTERVAL_LISTBOX_TEXT_HOURS),
				ReportingConstants.INTERVAL_LISTBOX_VALUE_HOURS);
	}

	@UiHandler("goButton")
	public void onGoClicked(ClickEvent event) {
		presenter.onGoClicked();
	}

	public final String getSelectedRadioValue() {
		String value = null;
		if (moduleSelection.getValue()) {
			value = ReportingConstants.MODULE;
		} else if (pluginSelection.getValue()) {
			value = ReportingConstants.PLUGIN;
		} else if (userSelection.getValue()) {
			value = userList.getValue(userList.getSelectedIndex());
		}
		return value;
	}

	public Date getStartDate() {
		return new Date(startDateText.getValue());
	}

	public Date getEndDate() {
		return new Date(endDateText.getValue());
	}

	public void setTotalInfo(List<Integer> counts) {

		totalBatches.setText(counts.get(0).toString());
		totalDocuments.setText(counts.get(1).toString());
		totalPages.setText(counts.get(2).toString());

	}

	@UiHandler("syncButton")
	public void onSyncDBClicked(ClickEvent event) {
		presenter.onSyncDBClicked();
	}

	@UiHandler("commonButton")
	public void onCommonButtonClicked(ClickEvent event) {

		presenter.onCustomReportButtonClicked();
	}

	public ListBox getIntervalListBox() {
		return intervalListBox;
	}

	public List<String> getSelectedBatchClassIds() {
		List<String> batchClassIds = new ArrayList<String>();
		for (int i = 0; i < batchClassList.getItemCount(); i++) {
			if (batchClassList.getSelectedIndex() == -1
					|| batchClassList.getItemText(batchClassList.getSelectedIndex()).equalsIgnoreCase("ALL")) {
				batchClassIds.add(batchClassList.getValue(i));
			} else {
				if (batchClassList.isItemSelected(i)) {
					batchClassIds.add(batchClassList.getValue(i));
				}
			}
		}
		return batchClassIds;
	}

	public LayoutPanel getReportTablePanel() {
		return reportTablePanel;
	}

	private final void addIntervalChangeHandler() {
		intervalListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				String divisor = intervalListBox.getValue(intervalListBox.getSelectedIndex());
				String headerName = getSelectedRadioValue();
				if (!(headerName.equalsIgnoreCase(ReportingConstants.MODULE) || headerName.equalsIgnoreCase(ReportingConstants.PLUGIN))) {
					headerName = ReportingConstants.MODULE;
				}
				List<Record> recordList = createTableData(reportList, Integer.parseInt(divisor));
				reportingListView.updateHeaders(headerName, intervalListBox.getItemText(intervalListBox.getSelectedIndex()));
				reportingListView.listView.updateRecords(recordList, presenter.getStartIndex(), presenter.getTotalCount());
			}
		});
	}

	public final List<Record> createTableData(List<ReportDTO> reports, int divisor) {
		reportList = reports;
		List<Record> recordList = new ArrayList<Record>();
		if (reports != null && !reports.isEmpty()) {
			for (ReportDTO report : reports) {
				Record record = new Record(new Date().toString());
				record.addWidget(reportingListView.name, new Label(report.getEntityName()));
				record.addWidget(reportingListView.batches, new Label(String
						.valueOf(Math.round(report.getBatch() * divisor * 100.0) / 100.0)));
				record.addWidget(reportingListView.documents, new Label(String
						.valueOf(Math.round(report.getDocs() * divisor * 100.0) / 100.0)));
				record.addWidget(reportingListView.pages, new Label(String
						.valueOf(Math.round(report.getPages() * divisor * 100.0) / 100.0)));
				recordList.add(record);
			}
		}
		return recordList;
	}

	public ReportingListView getReportingListView() {
		return reportingListView;
	}

	public RegExValidatableWidget<TextBox> getStartDateWidget() {
		return startDateWidget;
	}

	public RegExValidatableWidget<TextBox> getEndDateWidget() {
		return endDateWidget;
	}

	public TextBox getStartDateTextBox() {
		return startDateText;
	}

	public TextBox getEndDateTextBox() {
		return endDateText;
	}

}
