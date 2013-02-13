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

package com.ephesoft.dcma.gwt.admin.bm.client.view.batchclassfield;

import com.ephesoft.dcma.da.property.BatchClassFieldProperty;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.batchclassfield.BatchClassFieldListPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView;
import com.ephesoft.dcma.gwt.core.client.ui.table.TableHeader.HeaderColumn;

/**
 * This class provides functionality to show batch class field list view.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class BatchClassFieldListView extends View<BatchClassFieldListPresenter> {

	/**
	 * To get Batch Class Field List View.
	 * 
	 * @return ListView
	 */
	public ListView getBatchClassFieldListView() {
		return listView;
	}

	/**
	 * name HeaderColumn.
	 */
	public HeaderColumn name = new HeaderColumn(0, LocaleDictionary.get().getConstantValue(
			BatchClassManagementConstants.BATCH_CLASS_FIELD_NAME), BatchClassManagementConstants.TWENTY, true,
			BatchClassFieldProperty.NAME);

	/**
	 * description HeaderColumn.
	 */
	public HeaderColumn description = new HeaderColumn(1, LocaleDictionary.get().getConstantValue(
			BatchClassManagementConstants.BATCH_CLASS_FIELD_DESCRIPTION), BatchClassManagementConstants.TWENTY, true,
			BatchClassFieldProperty.DESCRIPTION);

	/**
	 * type HeaderColumn.
	 */
	public HeaderColumn type = new HeaderColumn(2, LocaleDictionary.get().getConstantValue(
			BatchClassManagementConstants.BATCH_CLASS_FIELD_TYPE), BatchClassManagementConstants.FIFTEEN, true,
			BatchClassFieldProperty.TYPE);

	/**
	 * fdOrder HeaderColumn.
	 */
	public HeaderColumn fdOrder = new HeaderColumn(BatchClassManagementConstants.THREE, LocaleDictionary.get().getConstantValue(
			BatchClassManagementConstants.BATCH_CLASS_FIELD_ORDER), BatchClassManagementConstants.TEN, true,
			BatchClassFieldProperty.FIELDORDERNUMBER);

	/**
	 * sampleValue HeaderColumn.
	 */
	public HeaderColumn sampleValue = new HeaderColumn(BatchClassManagementConstants.FOUR, LocaleDictionary.get().getConstantValue(
			BatchClassManagementConstants.BATCH_CLASS_FIELD_SAMPLE), BatchClassManagementConstants.FIFTEEN);

	/**
	 * validationPattern HeaderColumn.
	 */
	public HeaderColumn validationPattern = new HeaderColumn(BatchClassManagementConstants.FIVE, LocaleDictionary.get()
			.getConstantValue(BatchClassManagementConstants.BATCH_CLASS_FIELD_PATTERN), BatchClassManagementConstants.FIFTEEN);

	/**
	 * listView ListView.
	 */
	public ListView listView = new ListView();

	/**
	 * Constructor.
	 */
	public BatchClassFieldListView() {
		super();
		listView.addHeaderColumns(name, description, type, fdOrder, sampleValue, validationPattern);

	}

}
