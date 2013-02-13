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

package com.ephesoft.dcma.gwt.admin.bm.client.view.email;

import com.ephesoft.dcma.da.property.EmailProperty;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.email.EmailListPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView;
import com.ephesoft.dcma.gwt.core.client.ui.table.TableHeader.HeaderColumn;

/**
 * This class provides functionality to show cmis importer list view.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class EmailListView extends View<EmailListPresenter> {

	/**
	 * To get Email List View.
	 * 
	 * @return ListView
	 */
	public ListView getEmailListView() {
		return listView;
	}

	/**
	 * userName HeaderColumn.
	 */
	public HeaderColumn userName = new HeaderColumn(1,
			LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.USERNAME), BatchClassManagementConstants.FIFTEEN,
			true, EmailProperty.USERNAME);

	/**
	 * password HeaderColumn.
	 */
	public HeaderColumn password = new HeaderColumn(2,
			LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.PASSWORD), BatchClassManagementConstants.TEN, true,
			EmailProperty.PASSWORD);

	/**
	 * serverName HeaderColumn.
	 */
	public HeaderColumn serverName = new HeaderColumn(BatchClassManagementConstants.THREE, LocaleDictionary.get().getConstantValue(
			BatchClassManagementConstants.SERVER_NAME), BatchClassManagementConstants.TEN, true, EmailProperty.SERVERNAME);

	/**
	 * serverType HeaderColumn.
	 */
	public HeaderColumn serverType = new HeaderColumn(BatchClassManagementConstants.FOUR, LocaleDictionary.get().getConstantValue(
			BatchClassManagementConstants.SERVER_TYPE), BatchClassManagementConstants.TEN, true, EmailProperty.SERVERTYPE);

	/**
	 * folderName HeaderColumn.
	 */
	public HeaderColumn folderName = new HeaderColumn(BatchClassManagementConstants.FIVE, LocaleDictionary.get().getConstantValue(
			BatchClassManagementConstants.FOLDER_NAME), BatchClassManagementConstants.TEN, true, EmailProperty.FOLDERNAME);

	/**
	 * isSSL HeaderColumn.
	 */
	public HeaderColumn isSSL = new HeaderColumn(BatchClassManagementConstants.SIX, LocaleDictionary.get().getConstantValue(
			BatchClassManagementConstants.ISSSL), BatchClassManagementConstants.TEN);

	/**
	 * portNumbner HeaderColumn.
	 */
	public HeaderColumn portNumbner = new HeaderColumn(BatchClassManagementConstants.SEVEN, LocaleDictionary.get().getConstantValue(
			BatchClassManagementConstants.PORTNUMBER), BatchClassManagementConstants.TEN, true, EmailProperty.PORTNUMBER);

	/**
	 * listView ListView.
	 */
	public ListView listView = new ListView();

	/**
	 * Constructor.
	 */
	public EmailListView() {
		super();
		listView.addHeaderColumns(userName, password, serverName, serverType, folderName, isSSL, portNumbner);
	}
}
