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

package com.ephesoft.dcma.gwt.admin.bm.client.view.tablecolumninfo;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.tablecolumninfo.TableColumnInfoDetailPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class provides functionality to show table column info detail.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class TableColumnInfoDetailView extends View<TableColumnInfoDetailPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, TableColumnInfoDetailView> {
	}

	/**
	 * betweenLeftLabel Label.
	 */
	@UiField
	protected Label betweenLeftLabel;

	/**
	 * betweenLeft Label.
	 */
	@UiField
	protected Label betweenLeft;

	/**
	 * betweenRightLabel Label.
	 */
	@UiField
	protected Label betweenRightLabel;

	/**
	 * betweenRight Label.
	 */
	@UiField
	protected Label betweenRight;

	/**
	 * columnNameLabel Label.
	 */
	@UiField
	protected Label columnNameLabel;

	/**
	 * columnName Label.
	 */
	@UiField
	protected Label columnName;

	/**
	 * columnHeaderPatternLabel Label.
	 */
	@UiField
	protected Label columnHeaderPatternLabel;

	/**
	 * columnHeaderPattern Label.
	 */
	@UiField
	protected Label columnHeaderPattern;

	/**
	 * columnStartCoordinateLabel Label.
	 */
	@UiField
	protected Label columnStartCoordinateLabel;

	/**
	 * columnStartCoordinate Label.
	 */
	@UiField
	protected Label columnStartCoordinate;

	/**
	 * columnEndCoordinateLabel Label.
	 */
	@UiField
	protected Label columnEndCoordinateLabel;

	/**
	 * columnEndCoordinate Label.
	 */
	@UiField
	protected Label columnEndCoordinate;

	/**
	 * columnPatternLabel Label.
	 */
	@UiField
	protected Label columnPatternLabel;

	/**
	 * columnPattern Label.
	 */
	@UiField
	protected Label columnPattern;

	/**
	 * isRequiredLabel Label.
	 */
	@UiField
	protected Label isRequiredLabel;

	/**
	 * isRequired CheckBox.
	 */
	@UiField
	protected CheckBox isRequired;

	/**
	 * isMandatoryLabel Label.
	 */
	@UiField
	protected Label isMandatoryLabel;

	/**
	 * mandatory CheckBox.
	 */
	@UiField
	protected CheckBox mandatory;

	/**
	 * editTCInfoPropertiesButton Button.
	 */
	@UiField
	protected Button editTCInfoPropertiesButton;

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
	public TableColumnInfoDetailView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		betweenLeftLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.BETWEEN_LEFT)
				+ AdminConstants.COLON);
		betweenRightLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.BETWEEN_RIGHT)
				+ AdminConstants.COLON);
		columnNameLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.COLUMN_NAME)
				+ AdminConstants.COLON);
		columnHeaderPatternLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.COLUMN_HEADER_PATTERN)
				+ AdminConstants.COLON);
		columnStartCoordinateLabel.setText(LocaleDictionary.get().getConstantValue(
				BatchClassManagementConstants.COLUMN_START_COORDINATE_LABEL)
				+ AdminConstants.COLON);
		columnEndCoordinateLabel.setText(LocaleDictionary.get().getConstantValue(
				BatchClassManagementConstants.COLUMN_END_COORDINATE_LABEL)
				+ AdminConstants.COLON);
		columnPatternLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.COLUMN_PATTERN)
				+ AdminConstants.COLON);
		isRequiredLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.IS_REQUIRED)
				+ AdminConstants.COLON);
		isMandatoryLabel.setText(BatchClassManagementConstants.IS_MANDATORY.toString() + AdminConstants.COLON);

		betweenLeftLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		betweenRightLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		columnNameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		columnHeaderPatternLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		columnPatternLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		columnStartCoordinateLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		columnEndCoordinateLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		isRequiredLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		isMandatoryLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		isRequired.setEnabled(false);
		mandatory.setEnabled(false);
		scrollPanel.setStyleName(AdminConstants.SCROLL_PANEL_HEIGHT);
		editTCInfoPropertiesButton.setText(AdminConstants.EDIT_BUTTON);
	}

	/**
	 * To set Column Name.
	 * 
	 * @param columnName String
	 */
	public void setColumnName(String columnName) {
		this.columnName.setText(columnName);
	}

	/**
	 * To set Between Left field.
	 * 
	 * @param betweenLeft String
	 */
	public void setBetweenLeft(String betweenLeft) {
		this.betweenLeft.setText(betweenLeft);
	}

	/**
	 * To set Between right field.
	 * 
	 * @param betweenRight String
	 */
	public void setBetweenRight(String betweenRight) {
		this.betweenRight.setText(betweenRight);
	}

	/**
	 * To set Column Pattern.
	 * 
	 * @param columnPattern String
	 */
	public void setColumnPattern(String columnPattern) {
		this.columnPattern.setText(columnPattern);
	}

	/**
	 * To set Column Header Pattern.
	 * 
	 * @param columnHeaderPattern String
	 */
	public void setColumnHeaderPattern(String columnHeaderPattern) {
		this.columnHeaderPattern.setText(columnHeaderPattern);
	}

	/**
	 * To set Required.
	 * 
	 * @param isRequired boolean
	 */
	public void setRequired(boolean isRequired) {
		this.isRequired.setValue(isRequired);
	}

	/**
	 * To set Mandatory.
	 * 
	 * @param mandatory boolean
	 */
	public void setMandatory(boolean mandatory) {
		this.mandatory.setValue(mandatory);
	}

	/**
	 * To set Column Start Coordinate.
	 * 
	 * @param columnStartCoordinate String
	 */
	public void setColumnStartCoordinate(String columnStartCoordinate) {
		this.columnStartCoordinate.setText(columnStartCoordinate);
	}

	/**
	 * To set Column End Coordinate.
	 * 
	 * @param columnEndCoordinate String
	 */
	public void setColumnEndCoordinate(String columnEndCoordinate) {
		this.columnEndCoordinate.setText(columnEndCoordinate);
	}

	/**
	 * To get Edit Table Column Info Properties Button.
	 * 
	 * @return Button
	 */
	public Button getEditTCInfoPropertiesButton() {
		return editTCInfoPropertiesButton;
	}

	/**
	 * To get controller on click of Edit Table Column Info Properties Button.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("editTCInfoPropertiesButton")
	public void onEditTCInfoPropertiesButtonClick(ClickEvent clickEvent) {
		presenter.getController().getBatchClassManagementPresenter().getTableColumnInfoPresenter()
				.onEditTCInfoPropertiesButtonClicked();
	}
}
