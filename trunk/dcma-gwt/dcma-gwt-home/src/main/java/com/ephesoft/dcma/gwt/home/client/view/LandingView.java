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

package com.ephesoft.dcma.gwt.home.client.view;

import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.shared.BatchPriority;
import com.ephesoft.dcma.gwt.home.client.event.BatchListKeyDownEvent;
import com.ephesoft.dcma.gwt.home.client.i18n.BatchListConstants;
import com.ephesoft.dcma.gwt.home.client.presenter.LandingPresenter;
import com.ephesoft.dcma.gwt.home.client.view.reviewtable.ReviewValidateTable;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Class that is used for the view presented to user on landing on batch list page.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class LandingView extends View<LandingPresenter> {

	/**
	 * The review table.
	 */
	private ReviewValidateTable reviewTable;

	/**
	 * The validate table.
	 */
	private ReviewValidateTable validateTable;

	/**
	 * Label used for batches in review state.
	 */
	private Label pendingForReviewLabelValue;

	/**
	 * Label used for batches in validation state.
	 */
	private Label pendingForValidationLabelValue;

	/**
	 * Label to show total batches present.
	 */
	private Label totalBatchValue;

	/**
	 * The outer panel in the view.
	 */
	private DockLayoutPanel flowpanel;

	/**
	 * The tab layout panel containing the review and validate tabs.
	 */
	private TabLayoutPanel reviewValidateTabLayoutPanel;

	/**
	 * Used to set height/width at hundred percent.
	 */
	private static final String HUNDRED_PERCENT = "100%";
	/**
	 * focusPanel FocusPanel.
	 */
	private FocusPanel focusPanel;

	/**
	 * To build the Landing Page.
	 * 
	 * @return FocusPanel
	 */
	public FocusPanel buildLandingPage() {
		focusPanel = new FocusPanel();
		flowpanel = new DockLayoutPanel(Unit.PCT);
		flowpanel.setHeight(HUNDRED_PERCENT);
		flowpanel.addStyleName(CoreCommonConstants.MAIN_CONTAINER_CSS);
		HorizontalPanel horCapPanel = new HorizontalPanel();
		VerticalPanel innerVerPanel = new VerticalPanel();
		innerVerPanel.setWidth(CoreCommonConstants._100_PERCENTAGE);
		VerticalPanel capPanel = new VerticalPanel();
		capPanel.setStyleName(CoreCommonConstants.TOP_PANEL_CSS);
		HorizontalPanel title = new HorizontalPanel();
		title.setBorderWidth(0);
		title.setSize(HUNDRED_PERCENT, HUNDRED_PERCENT);
		Label titleLabel = new Label(LocaleDictionary.get().getConstantValue(BatchListConstants.LABEL_BATCH_ALERTS));
		title.add(titleLabel);
		titleLabel.addStyleName(CoreCommonConstants.HEADER_BOLD_TEXT_CSS);
		capPanel.add(titleLabel);
		HorizontalPanel simPanel = new HorizontalPanel();

		simPanel.setSize(HUNDRED_PERCENT, HUNDRED_PERCENT);

		Label pendingForReviewLabel = new Label(LocaleDictionary.get().getConstantValue(BatchListConstants.LABEL_PEND_FOR_REVIEW)
				+ ": ");
		pendingForReviewLabel.addStyleName(CoreCommonConstants.BOLD_TEXT_CSS);
		pendingForReviewLabelValue = new Label("");
		Label pendingForValidationLabel = new Label(LocaleDictionary.get().getConstantValue(
				BatchListConstants.LABEL_PEND_FOR_VALIDATION)
				+ ": ");
		pendingForValidationLabel.addStyleName(CoreCommonConstants.BOLD_TEXT_CSS);
		pendingForValidationLabelValue = new Label("");
		Label totalBatchCount = new Label(LocaleDictionary.get().getConstantValue(BatchListConstants.LABEL_TOTAL_BATCHES) + ": ");
		totalBatchCount.addStyleName(CoreCommonConstants.BOLD_TEXT_CSS);
		totalBatchValue = new Label(BatchListConstants.EMPTY_STRING);
		Label pipe = new Label(BatchListConstants.EMPTY_STRING);
		pipe.setStyleName("pipe");

		simPanel.add(totalBatchCount);
		simPanel.add(totalBatchValue);
		simPanel.add(pipe);
		simPanel.add(pendingForReviewLabel);
		simPanel.add(pendingForReviewLabelValue);
		simPanel.add(pendingForValidationLabel);
		simPanel.add(pendingForValidationLabelValue);

		simPanel.setCellWidth(pendingForReviewLabel, "7%");
		simPanel.setCellWidth(pendingForReviewLabelValue, "2%");
		simPanel.setCellWidth(pendingForValidationLabel, "7%");
		simPanel.setCellWidth(pendingForValidationLabelValue, "20%");
		simPanel.setCellWidth(totalBatchCount, "5%");
		simPanel.setCellWidth(totalBatchValue, "2%");
		simPanel.setCellWidth(pipe, "1%");

		capPanel.add(simPanel);
		capPanel.setWidth(HUNDRED_PERCENT);
		innerVerPanel.add(capPanel);
		Label dummyLabel = new Label();
		dummyLabel.setHeight("40px");
		innerVerPanel.add(dummyLabel);
		HorizontalPanel buttonTextPanel = new HorizontalPanel();
		buttonTextPanel.add(dummyLabel);
		buttonTextPanel.setCellWidth(dummyLabel, "20%");
		innerVerPanel.add(buttonTextPanel);
		horCapPanel.add(innerVerPanel);
		horCapPanel.setCellHorizontalAlignment(innerVerPanel, HasAlignment.ALIGN_LEFT);
		horCapPanel.setWidth(HUNDRED_PERCENT);
		flowpanel.addNorth(horCapPanel, BatchListConstants.FIFTEEN);
		reviewTable = new ReviewValidateTable(true);
		reviewValidateTabLayoutPanel = new TabLayoutPanel(BatchListConstants.FIVE, Unit.PCT);
		reviewValidateTabLayoutPanel.setWidth(CoreCommonConstants._100_PERCENTAGE);
		reviewValidateTabLayoutPanel.add(reviewTable.getMainPanel());
		validateTable = new ReviewValidateTable(false);
		reviewValidateTabLayoutPanel.add(validateTable.getMainPanel(), "");
		flowpanel.add(reviewValidateTabLayoutPanel);
		focusPanel.add(flowpanel);
		focusPanel.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent arg0) {
				presenter.getController().getEventBus().fireEvent(new BatchListKeyDownEvent(arg0));
			}
		});

		return focusPanel;
	}

	/**
	 * To get Review Table.
	 * 
	 * @return ReviewValidateTable
	 */
	public ReviewValidateTable getReviewTable() {
		return reviewTable;
	}

	/**
	 * To get Validate Table.
	 * 
	 * @return ReviewValidateTable
	 */
	public ReviewValidateTable getValidateTable() {
		return validateTable;
	}

	/**
	 * To set Review Table Selected.
	 */
	public void setReviewTableSelected() {
		this.reviewValidateTabLayoutPanel.selectTab(reviewTable.getMainPanel());
	}

	/**
	 * To set Validate Table Seleted.
	 */
	public void setValidateTableSeleted() {
		this.reviewValidateTabLayoutPanel.selectTab(validateTable.getMainPanel());
	}

	/**
	 * To get Review Label.
	 * 
	 * @return Label
	 */
	public Label getReviewLabel() {
		return pendingForReviewLabelValue;
	}

	/**
	 * To get Validation Label.
	 * 
	 * @return Label
	 */
	public Label getValidationLabel() {
		return pendingForValidationLabelValue;
	}

	/**
	 * To get Total Batch Label.
	 * 
	 * @return Label
	 */
	public Label getTotalBatchLabel() {
		return totalBatchValue;
	}

	/**
	 * To get Priority List Box.
	 * 
	 * @return ListBox
	 */
	public ListBox getPriorityListBox() {
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

	/**
	 * To go to Review and Validate Page.
	 * 
	 * @param batchInstanceIdentifier String
	 */
	public void gotoReviewAndValidatePage(final String batchInstanceIdentifier) {
		String href = Window.Location.getHref();
		String baseUrl = href.substring(0, href.lastIndexOf('/'));
		StringBuffer newUrl = new StringBuffer();
		newUrl.append(baseUrl).append(BatchListConstants.REVIEW_VALIDATE_HTML);
		if (!(BatchListConstants.ZERO.equals(batchInstanceIdentifier))) {
			newUrl.append(BatchListConstants.BATCH_ID).append(batchInstanceIdentifier);
		}
		Window.Location.assign(newUrl.toString());
	}

	/**
	 * To get Outer.
	 * 
	 * @return Panel
	 */
	public Panel getOuter() {
		return focusPanel;
	}

	/**
	 * To get Review Validate TabLayout Panel.
	 * 
	 * @return TabLayoutPanel
	 */
	public TabLayoutPanel getReviewValidateTabLayoutPanel() {
		return reviewValidateTabLayoutPanel;
	}

}
