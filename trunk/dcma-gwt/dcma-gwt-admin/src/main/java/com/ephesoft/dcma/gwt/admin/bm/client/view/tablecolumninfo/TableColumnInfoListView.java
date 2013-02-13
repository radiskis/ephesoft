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

import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.tablecolumninfo.TableColumnInfoListPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView;
import com.ephesoft.dcma.gwt.core.client.ui.table.TableHeader.HeaderColumn;

/**
 * This class provides functionality to show table column info list view.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class TableColumnInfoListView extends View<TableColumnInfoListPresenter> {

	/**
	 * To get Table Column Info List View.
	 * 
	 * @return ListView
	 */
	public ListView getTableColumnInfoListView() {
		return listView;
	}

	/**
	 * betweenLeft HeaderColumn.
	 */
	public HeaderColumn betweenLeft = new HeaderColumn(0, LocaleDictionary.get().getConstantValue(
			BatchClassManagementConstants.BETWEEN_LEFT), BatchClassManagementConstants.TEN);

	/**
	 * betweenRight HeaderColumn.
	 */
	public HeaderColumn betweenRight = new HeaderColumn(1, LocaleDictionary.get().getConstantValue(
			BatchClassManagementConstants.BETWEEN_RIGHT), BatchClassManagementConstants.ELEVEN);

	/**
	 * columnName HeaderColumn.
	 */
	public HeaderColumn columnName = new HeaderColumn(2, LocaleDictionary.get().getConstantValue(
			BatchClassManagementConstants.COLUMN_NAME), BatchClassManagementConstants.ELEVEN);

	/**
	 * columnHeaderPattern HeaderColumn.
	 */
	public HeaderColumn columnHeaderPattern = new HeaderColumn(BatchClassManagementConstants.THREE, LocaleDictionary.get()
			.getConstantValue(BatchClassManagementConstants.COLUMN_HEADER_PATTERN), BatchClassManagementConstants.ELEVEN);

	/**
	 * columnPattern HeaderColumn.
	 */
	public HeaderColumn columnPattern = new HeaderColumn(BatchClassManagementConstants.FOUR, LocaleDictionary.get().getConstantValue(
			BatchClassManagementConstants.COLUMN_PATTERN), BatchClassManagementConstants.TWELVE);
	/**
	 * columnStartCoord HeaderColumn.
	 */
	public HeaderColumn columnStartCoord = new HeaderColumn(BatchClassManagementConstants.FIVE, LocaleDictionary.get()
			.getConstantValue(BatchClassManagementConstants.COLUMN_START_COORDINATE_LABEL), BatchClassManagementConstants.TWELVE);

	/**
	 * columnEndCoord HeaderColumn.
	 */
	public HeaderColumn columnEndCoord = new HeaderColumn(BatchClassManagementConstants.SIX, LocaleDictionary.get().getConstantValue(
			BatchClassManagementConstants.COLUMN_END_COORDINATE_LABEL), BatchClassManagementConstants.ELEVEN);

	/**
	 * isRequired HeaderColumn.
	 */
	public HeaderColumn isRequired = new HeaderColumn(BatchClassManagementConstants.SEVEN, LocaleDictionary.get().getConstantValue(
			BatchClassManagementConstants.IS_REQUIRED), BatchClassManagementConstants.SEVEN);

	/**
	 * isMandatory HeaderColumn.
	 */
	public HeaderColumn isMandatory = new HeaderColumn(BatchClassManagementConstants.EIGHT, LocaleDictionary.get().getConstantValue(
			BatchClassManagementConstants.IS_MANDATORY), BatchClassManagementConstants.SEVEN);

	/**
	 * listView ListView.
	 */
	public ListView listView = new ListView();

	/**
	 * Constructor.
	 */
	public TableColumnInfoListView() {
		super();
		listView.addHeaderColumns(betweenLeft, betweenRight, columnName, columnPattern, columnHeaderPattern, columnStartCoord,
				columnEndCoord, isRequired, isMandatory);
	}
}
