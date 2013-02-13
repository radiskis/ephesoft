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
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin.PluginDetailPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class provides functionality to show plugin detail.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class PluginDetailView extends View<PluginDetailPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, PluginDetailView> {
	}

	/**
	 * viewTable FlexTable.
	 */
	@UiField
	protected FlexTable viewTable;

	/**
	 * edit Button.
	 */
	@UiField
	protected Button edit;

	/**
	 * cmisInfo Button.
	 */
	@UiField
	protected Button cmisInfo;

	/**
	 * custom button for cmis export plugin to test connection to repository server.
	 */
	@UiField
	protected Button testCMIS;

	/**
	 * To get Test CMIS.
	 * 
	 * @return Button
	 */
	public Button getTestCMIS() {
		return testCMIS;
	}

	/**
	 * To get Cmis Info.
	 * 
	 * @return Button
	 */
	public Button getCmisInfo() {
		return cmisInfo;
	}

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
	public PluginDetailView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		scrollPanel.setStyleName(AdminConstants.SCROLL_PANEL_PLUGIN);
		edit.setText(AdminConstants.EDIT_BUTTON);
		testCMIS.setText(AdminConstants.TEST_CMIS_BUTTON);
		testCMIS.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				presenter.onTestCmisButtonClicked();
			}
		});

		cmisInfo.setText(AdminConstants.GET_TOKEN);
		cmisInfo.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				presenter.onGetTokenButtonClicked();
			}
		});
	}

	/**
	 * To get View Table.
	 * 
	 * @return FlexTable
	 */
	public FlexTable getViewTable() {
		return viewTable;
	}

	/**
	 * To get Edit Button.
	 * 
	 * @return Button
	 */
	public Button getEditButton() {
		return edit;
	}
}
