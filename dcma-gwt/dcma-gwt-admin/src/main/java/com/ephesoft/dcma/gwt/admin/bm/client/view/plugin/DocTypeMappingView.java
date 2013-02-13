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

package com.ephesoft.dcma.gwt.admin.bm.client.view.plugin;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin.DocTypeMappingPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin.DocTypeMappingPresenter.CustomWidget;
import com.ephesoft.dcma.gwt.core.client.View;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

/**
 * This class provides functionality to edit document type mapping.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class DocTypeMappingView extends View<DocTypeMappingPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<DockLayoutPanel, DocTypeMappingView> {
	}

	/**
	 * flexEditTable FlexTable.
	 */
	@UiField
	protected FlexTable flexEditTable;

	/**
	 * detailsTable FlexTable.
	 */
	private final FlexTable detailsTable;

	/**
	 * rowCounter int.
	 */
	private int rowCounter = 0;

	/**
	 * pluginData PluginData.
	 */
	@UiField
	protected PluginData pluginData;

	/**
	 * dbMappingPanel CaptionPanel.
	 */
	@UiField
	protected CaptionPanel dbMappingPanel;

	/**
	 * pluginDetailsPanel CaptionPanel.
	 */
	@UiField
	protected CaptionPanel pluginDetailsPanel;

	/**
	 * driverName String.
	 */
	private String driverName;

	/**
	 * url String.
	 */
	private String url;

	/**
	 * userName String.
	 */
	private String userName;

	/**
	 * password String.
	 */
	private String password;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Constructor.
	 */
	public DocTypeMappingView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		dbMappingPanel.setCaptionHTML(AdminConstants.DATABASE_MAPPING_HTML);
		pluginDetailsPanel.setCaptionHTML(AdminConstants.PLUGIN_DETAILS_HTML);

		detailsTable = new FlexTable();
		detailsTable.setWidth("100%");
		flexEditTable.setWidget(0, 0, detailsTable);
		flexEditTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);

	}

	/**
	 * To add row.
	 * 
	 * @param widget CustomWidget
	 */
	public void addRow(CustomWidget widget) {
		detailsTable.setWidget(rowCounter, 0, widget);
		rowCounter++;
	}

	/**
	 * To clear Details Table.
	 */
	public void clearDetailsTable() {
		detailsTable.clear();
		rowCounter = 0;
	}

	/**
	 * To get Driver Name.
	 * 
	 * @return String
	 */
	public String getDriverName() {
		return driverName;
	}

	/**
	 * To set Driver Name.
	 * 
	 * @param driverName String
	 */
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	/**
	 * To get URL.
	 * 
	 * @return String
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * To set URL.
	 * 
	 * @param url String
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * To get User Name.
	 * 
	 * @return String
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * To set User Name.
	 * 
	 * @param userName String
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * To get Password.
	 * 
	 * @return String
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * To set Password.
	 * 
	 * @param password String
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * To get Plugin Data View.
	 * 
	 * @return PluginData
	 */
	public PluginData getPluginDataView() {
		return pluginData;
	}

}
