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

package com.ephesoft.dcma.gwt.admin.bm.client.view.tableinfo;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.tableinfo.TableInfoDetailPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class provides functionality to show individual table info detail.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class TableInfoDetailView extends View<TableInfoDetailPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, TableInfoDetailView> {
	}

	/**
	 * name Label.
	 */
	@UiField
	protected Label name;

	/**
	 * startPattern Label.
	 */
	@UiField
	protected Label startPattern;

	/**
	 * endPattern Label.
	 */
	@UiField
	protected Label endPattern;

	/**
	 * nameLabel Label.
	 */
	@UiField
	protected Label nameLabel;

	/**
	 * startPatternLabel Label.
	 */
	@UiField
	protected Label startPatternLabel;

	/**
	 * endPatternLabel Label.
	 */
	@UiField
	protected Label endPatternLabel;

	/**
	 * widthOfMultiLineLabel Label.
	 */
	@UiField
	protected Label widthOfMultiLineLabel;

	/**
	 * widthOfMultiLine Label.
	 */
	@UiField
	protected Label widthOfMultiLine;

	/**
	 * tableExtractionAPILabel Label.
	 */
	@UiField
	protected Label tableExtractionAPILabel;

	/**
	 * tableExtractionAPI Label.
	 */
	@UiField
	protected Label tableExtractionAPI;

	/**
	 * editTableInfoPropertiesButton Button.
	 */
	@UiField
	protected Button editTableInfoPropertiesButton;

	/**
	 * scrollPanel ScrollPanel.
	 */
	@UiField
	protected ScrollPanel scrollPanel;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Constructor.
	 */
	public TableInfoDetailView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		nameLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.NAME) + AdminConstants.COLON);
		startPatternLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.START_PATTERN)
				+ AdminConstants.COLON);
		endPatternLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.END_PATTERN)
				+ AdminConstants.COLON);
		tableExtractionAPILabel.setText(LocaleDictionary.get().getConstantValue(
				BatchClassManagementConstants.TABLE_EXTRACTION_TECHNIQUE)
				+ AdminConstants.COLON);
		widthOfMultiLineLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.WIDTH_OF_MULTILINE)
				+ AdminConstants.COLON);

		nameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		startPatternLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		endPatternLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		tableExtractionAPILabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		widthOfMultiLineLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		editTableInfoPropertiesButton.setText(AdminConstants.EDIT_BUTTON);
		scrollPanel.setStyleName(AdminConstants.SCROLL_PANEL_HEIGHT);
	}

	/**
	 * To set name.
	 * 
	 * @param name String
	 */
	public void setName(String name) {
		this.name.setText(name);
	}

	/**
	 * To set Start Pattern.
	 * 
	 * @param startPattern String
	 */
	public void setStartPattern(String startPattern) {
		this.startPattern.setText(startPattern);
	}

	/**
	 * To set End Pattern.
	 * 
	 * @param endPattern String
	 */
	public void setEndPattern(String endPattern) {
		this.endPattern.setText(endPattern);
	}

	/**
	 * To get Table Extraction API.
	 * 
	 * @return String
	 */
	public String getTableExtractionAPI() {
		return tableExtractionAPI.getText();
	}

	/**
	 * To set Table Extraction API.
	 * 
	 * @param tableExtractionAPI String
	 */
	public void setTableExtractionAPI(final String tableExtractionAPI) {
		this.tableExtractionAPI.setText(tableExtractionAPI);
	}

	/**
	 * To get Edit Table Info Properties Button.
	 * 
	 * @return Button
	 */
	public Button getEditTableInfoPropertiesButton() {
		return editTableInfoPropertiesButton;
	}

	/**
	 * To get Width Of MultiLine.
	 * 
	 * @return String
	 */
	public String getWidthOfMultiLine() {
		return widthOfMultiLine.getText();
	}

	/**
	 * To set Width Of MultiLine.
	 * 
	 * @param widthOfMultiLine String
	 */
	public void setWidthOfMultiLine(String widthOfMultiLine) {
		this.widthOfMultiLine.setText(widthOfMultiLine);
	}

	/**
	 * To get controller on Edit Table Info Properties Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("editTableInfoPropertiesButton")
	public void onEditTableInfoPropertiesButtonClick(ClickEvent clickEvent) {
		presenter.getController().getBatchClassManagementPresenter().getTableInfoViewPresenter()
				.onEditTableInfoPropertiesButtonClicked();
	}

}
