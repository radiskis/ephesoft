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

package com.ephesoft.dcma.gwt.home.client.view.reviewtable;

import com.ephesoft.dcma.da.property.BatchInstanceProperty;
import com.ephesoft.dcma.gwt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView;
import com.ephesoft.dcma.gwt.core.client.ui.table.TableHeader.HeaderColumn;
import com.ephesoft.dcma.gwt.core.shared.BatchPriority;
import com.ephesoft.dcma.gwt.home.client.i18n.BatchListConstants;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.TextBox;

/**
 * The table that is used to show all the batches in review state and in validation state. A common table is used for both views.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.google.gwt.user.client.ui.ResizeComposite
 */
public class ReviewValidateTable extends ResizeComposite {

	/**
	 * List view of the table.
	 */
	private ListView listView;

	/**
	 * List box used for sorting on basis of batch priority.
	 */
	private ListBox priorityListBox;

	/**
	 * Button used to refresh the batch list.
	 */
	private Button refreshButton;

	/**
	 * The main panel of the view.
	 */
	private DockLayoutPanel mainPanel;

	/**
	 * filterAndSearchPanel HorizontalPanel.
	 */
	private HorizontalPanel filterAndSearchPanel;

	/**
	 * searchPanel HorizontalPanel.
	 */
	private HorizontalPanel searchPanel;

	/**
	 * searchAndFilterContanier FlowPanel.
	 */
	private FlowPanel searchAndFilterContanier;

	/**
	 * searchBatchButton Button.
	 */
	private Button searchBatchButton;

	/**
	 * searchBatchLabel Label.
	 */
	private Label searchBatchLabel;

	/**
	 * searchBatchTextBox TextBox.
	 */
	private TextBox searchBatchTextBox;

	/**
	 * Used to distinguish if client is on table with batches in review state or client is on table with batches in validation state.
	 */
	private boolean review;

	/**
	 * Header column for priority.
	 */
	public HeaderColumn priority = new HeaderColumn(0, LocaleDictionary.get().getConstantValue(
			BatchListConstants.LABEL_TABLE_COLUMN_PRIORITY), BatchListConstants.TEN);

	/**
	 * Header column for batchId.
	 */
	public HeaderColumn batchId = new HeaderColumn(1, LocaleDictionary.get().getConstantValue(
			BatchListConstants.LABEL_TABLE_COLUMN_BATCHID), BatchListConstants.TEN, true, BatchInstanceProperty.ID);

	/**
	 * Header column for batch class name.
	 */
	public HeaderColumn batchClassName = new HeaderColumn(2, LocaleDictionary.get().getConstantValue(
			BatchListConstants.LABEL_TABLE_COLUMN_BATCHCLASSNAME), BatchListConstants.TWENTY, true,
			BatchInstanceProperty.BATCHCLASSNAME);

	/**
	 * Header column for batch name.
	 */
	public HeaderColumn batchName = new HeaderColumn(BatchListConstants.THREE, LocaleDictionary.get().getConstantValue(
			BatchListConstants.LABEL_TABLE_COLUMN_BATCHNAME), BatchListConstants.FIFTEEN, true, BatchInstanceProperty.BATCHNAME);

	/**
	 * Header column for batch update date.
	 */
	public HeaderColumn batchUpdatedOn = new HeaderColumn(BatchListConstants.FIVE, LocaleDictionary.get().getConstantValue(
			BatchListConstants.LABEL_TABLE_COLUMN_BATCHUPDATEDON), BatchListConstants.EIGHTEEN, true,
			BatchInstanceProperty.LASTMODIFIED);

	/**
	 * Header column for batch creation date.
	 */
	public HeaderColumn batchCreatedOn = new HeaderColumn(BatchListConstants.FOUR, LocaleDictionary.get().getConstantValue(
			BatchListConstants.LABEL_TABLE_COLUMN_BATCHCREATEDON), BatchListConstants.EIGHTEEN, true,
			BatchInstanceProperty.CREATIONDATE);

	/**
	 * Constructor.
	 */
	public ReviewValidateTable() {
		super();
		mainPanel = new DockLayoutPanel(Unit.PCT);
		searchAndFilterContanier = new FlowPanel();
		filterAndSearchPanel = new HorizontalPanel();
		searchAndFilterContanier.addStyleName(CoreCommonConstants.OPTIONS_PANEL);
		filterAndSearchPanel.setWidth(CoreCommonConstants._100_PERCENTAGE);
		priorityListBox = createPriorityListBox();
		searchPanel = new HorizontalPanel();
		listView = new ListView();
		listView.setTableRowCount(BatchListConstants.FIFTEEN);
		addHeaders();
		HorizontalPanel listBoxPanel;
		listBoxPanel = new HorizontalPanel();

		searchBatchButton = new Button();
		searchBatchLabel = new Label();
		searchBatchTextBox = new TextBox();

		searchPanel.add(searchBatchLabel);
		searchPanel.add(searchBatchTextBox);
		searchPanel.add(searchBatchButton);
		searchPanel.setCellVerticalAlignment(searchBatchLabel, HasVerticalAlignment.ALIGN_MIDDLE);
		searchPanel.setCellVerticalAlignment(searchBatchTextBox, HasVerticalAlignment.ALIGN_MIDDLE);
		searchPanel.setCellVerticalAlignment(searchBatchButton, HasVerticalAlignment.ALIGN_MIDDLE);

		searchPanel.addStyleName(CoreCommonConstants.LAST_GRP_PANEL_NORMAL_CSS);
		searchBatchLabel.setText(LocaleDictionary.get().getConstantValue(BatchListConstants.LABEL_SEARCH_BATCH)
				+ BatchListConstants.COLON);
		searchBatchButton.setText(LocaleDictionary.get().getConstantValue(BatchListConstants.BUTTON_SEARCH_BATCH));

		listBoxPanel.addStyleName(CoreCommonConstants.GRP_PANEL_NORMAL_CSS);
		searchAndFilterContanier.add(filterAndSearchPanel);
		mainPanel.addNorth(searchAndFilterContanier, BatchListConstants.FIFTEEN);
		mainPanel.addStyleName("padding0");
		mainPanel.add(listView);
		Label priorityLabel = new Label(LocaleDictionary.get().getConstantValue(BatchListConstants.LABEL_PRIORITY_LISTBOX));
		refreshButton = new Button(LocaleDictionary.get().getConstantValue(BatchListConstants.LABEL_REFRESH_BUTTON));

		listBoxPanel.add(priorityLabel);
		listBoxPanel.add(priorityListBox);
		listBoxPanel.add(refreshButton);
		listBoxPanel.setCellHorizontalAlignment(priorityLabel, HasHorizontalAlignment.ALIGN_RIGHT);
		listBoxPanel.setCellHorizontalAlignment(refreshButton, HasHorizontalAlignment.ALIGN_LEFT);
		listBoxPanel.setWidth(CoreCommonConstants._100_PERCENTAGE);

		filterAndSearchPanel.add(listBoxPanel);
		filterAndSearchPanel.add(searchPanel);
		filterAndSearchPanel.setCellHorizontalAlignment(listBoxPanel, HasHorizontalAlignment.ALIGN_LEFT);
		filterAndSearchPanel.setCellWidth(listBoxPanel, CoreCommonConstants._20_PERCENTAGE);
		filterAndSearchPanel.setCellHorizontalAlignment(searchPanel, HasHorizontalAlignment.ALIGN_LEFT);
		searchBatchTextBox.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					searchBatchButton.click();
				}
			}
		});
		searchBatchTextBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				searchBatchTextBox.setText(BatchListConstants.EMPTY_STRING);
			}
		});
	}

	/**
	 * Constructor.
	 * 
	 * @param review boolean
	 */
	public ReviewValidateTable(boolean review) {
		this();
		this.review = review;
	}

	/**
	 * To get Search Batch Button.
	 * 
	 * @return Button
	 */
	public Button getSearchBatchButton() {
		return searchBatchButton;
	}

	/**
	 * To set Search Batch Button.
	 * 
	 * @param searchBatchButton Button
	 */
	public void setSearchBatchButton(Button searchBatchButton) {
		this.searchBatchButton = searchBatchButton;
	}

	/**
	 * To get Search Batch Text Box.
	 * 
	 * @return TextBox
	 */
	public TextBox getSearchBatchTextBox() {
		return searchBatchTextBox;
	}

	/**
	 * To set Search Batch Text Box.
	 * 
	 * @param searchBatchTextBox TextBox
	 */
	public void setSearchBatchTextBox(TextBox searchBatchTextBox) {
		this.searchBatchTextBox = searchBatchTextBox;
	}

	private ListBox createPriorityListBox() {
		ListBox priorityListBox = new ListBox();
		priorityListBox.addItem(LocaleDictionary.get().getConstantValue(BatchListConstants.LABEL_TABLE_ALL), "0");
		priorityListBox.addItem(LocaleDictionary.get().getConstantValue(BatchListConstants.BATCH_PRIORITY_URGENT),
				BatchPriority.URGENT.getLowerLimit().toString());
		priorityListBox.addItem(LocaleDictionary.get().getConstantValue(BatchListConstants.BATCH_PRIORITY_HIGH), BatchPriority.HIGH
				.getLowerLimit().toString());
		priorityListBox.addItem(LocaleDictionary.get().getConstantValue(BatchListConstants.BATCH_PRIORITY_MEDIUM),
				BatchPriority.MEDIUM.getLowerLimit().toString());
		priorityListBox.addItem(LocaleDictionary.get().getConstantValue(BatchListConstants.BATCH_PRIORITY_LOW), BatchPriority.LOW
				.getLowerLimit().toString());
		return priorityListBox;
	}

	private void addHeaders() {
		listView.addHeaderColumns(priority, batchId, batchClassName, batchName, batchCreatedOn, batchUpdatedOn);
	}

	/**
	 * To get List View.
	 * 
	 * @return ListView
	 */
	public ListView getListView() {
		return listView;
	}

	/**
	 * To get Priority List Box.
	 * 
	 * @return ListBox
	 */
	public ListBox getPriorityListBox() {
		return priorityListBox;
	}

	/**
	 * To get Main Panel.
	 * 
	 * @return DockLayoutPanel
	 */
	public DockLayoutPanel getMainPanel() {
		return mainPanel;
	}

	/**
	 * To get value of field is review.
	 * 
	 * @return boolean
	 */
	public boolean isReview() {
		return review;
	}

	/**
	 * To set Review.
	 * 
	 * @param review boolean
	 */
	public void setReview(boolean review) {
		this.review = review;
	}

	/**
	 * To get Refresh Button.
	 * 
	 * @return Button
	 */
	public Button getRefreshButton() {
		return refreshButton;
	}

}
