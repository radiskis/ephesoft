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

package com.ephesoft.dcma.gwt.admin.bm.client.view.kvextraction;

import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.kvextraction.KVTypeListPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView;
import com.ephesoft.dcma.gwt.core.client.ui.table.TableHeader.HeaderColumn;

/**
 * This class provides functionality to show field type list view.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class KVFieldTypeListView extends View<KVTypeListPresenter> {

	/**
	 * To get KV Fields List View.
	 * 
	 * @return ListView
	 */
	public ListView getKVFieldsListView() {
		return listView;
	}

	/**
	 * keyPattern HeaderColumn.
	 */
	public HeaderColumn keyPattern = new HeaderColumn(0, LocaleDictionary.get().getConstantValue(
			BatchClassManagementConstants.KEY_PATTERN), BatchClassManagementConstants.TWENTY);

	/**
	 * location HeaderColumn.
	 */
	public HeaderColumn location = new HeaderColumn(1,
			LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.LOCATION), BatchClassManagementConstants.FIFTEEN);

	/**
	 * valuePattern HeaderColumn.
	 */
	public HeaderColumn valuePattern = new HeaderColumn(2, LocaleDictionary.get().getConstantValue(
			BatchClassManagementConstants.VALUE_PATTERN), BatchClassManagementConstants.TWENTY);

	/**
	 * noOfWords HeaderColumn.
	 */
	public HeaderColumn noOfWords = new HeaderColumn(BatchClassManagementConstants.THREE, LocaleDictionary.get().getConstantValue(
			BatchClassManagementConstants.NO_OF_WORDS), BatchClassManagementConstants.TEN);

	/**
	 * fetchValue HeaderColumn.
	 */
	public HeaderColumn fetchValue = new HeaderColumn(BatchClassManagementConstants.FOUR, LocaleDictionary.get().getConstantValue(
			BatchClassManagementConstants.FETCH_VALUE), BatchClassManagementConstants.TEN);

	/**
	 * kvPageValue HeaderColumn.
	 */
	public HeaderColumn kvPageValue = new HeaderColumn(BatchClassManagementConstants.FIVE, LocaleDictionary.get().getConstantValue(
			BatchClassManagementConstants.KV_PAGE_VALUE_LABEL), BatchClassManagementConstants.TEN);

	/**
	 * multiplier HeaderColumn.
	 */
	public HeaderColumn multiplier = new HeaderColumn(BatchClassManagementConstants.SIX, LocaleDictionary.get().getConstantValue(
			BatchClassManagementConstants.MULTIPLIER_LABEL), BatchClassManagementConstants.TEN);

	/**
	 * listView ListView.
	 */
	public ListView listView = new ListView();

	/**
	 * Constructor.
	 */
	public KVFieldTypeListView() {
		super();
		listView.addHeaderColumns(keyPattern, location, valuePattern, noOfWords, fetchValue, kvPageValue, multiplier);
	}
}
