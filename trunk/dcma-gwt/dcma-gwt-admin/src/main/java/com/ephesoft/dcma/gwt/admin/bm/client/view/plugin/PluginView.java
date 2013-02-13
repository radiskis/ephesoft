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
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin.PluginViewPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class provides functionality to edit plugin.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class PluginView extends View<PluginViewPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<DockLayoutPanel, PluginView> {
	}

	/**
	 * viewEditPluginPanel LayoutPanel.
	 */
	@UiField
	protected LayoutPanel viewEditPluginPanel;

	/**
	 * pluginDetailView PluginDetailView.
	 */
	@UiField
	protected PluginDetailView pluginDetailView;

	/**
	 * editPluginView EditPluginView.
	 */
	@UiField
	protected EditPluginView editPluginView;

	/**
	 * pluginData PluginData.
	 */
	@UiField
	protected PluginData pluginData;

	/**
	 * pluginViewVerticalPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel pluginViewVerticalPanel;

	/**
	 * To get Plugin View Vertical Panel.
	 * 
	 * @return VerticalPanel
	 */
	public VerticalPanel getPluginViewVerticalPanel() {
		return pluginViewVerticalPanel;
	}

	/**
	 * To get Plugin Config Vertical Panel.
	 * 
	 * @return VerticalPanel
	 */
	public VerticalPanel getPluginConfigVerticalPanel() {
		return pluginConfigVerticalPanel;
	}

	/**
	 * pluginConfigVerticalPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel pluginConfigVerticalPanel;

	/**
	 * noResultLabel Label.
	 */
	@UiField
	protected Label noResultLabel;

	/**
	 * pluginDetailsCaptionPanel CaptionPanel.
	 */
	@UiField
	protected CaptionPanel pluginDetailsCaptionPanel;

	/**
	 * pluginConfigurationCaptionPanel CaptionPanel.
	 */
	@UiField
	protected CaptionPanel pluginConfigurationCaptionPanel;

	/**
	 * editButtonPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel editButtonPanel;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Constructor.
	 */
	public PluginView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		noResultLabel.setText(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.NO_RECORD_FOUND));
		noResultLabel.setVisible(false);

		pluginConfigurationCaptionPanel.setCaptionHTML(AdminConstants.PLUGIN_CONFIGURATION_HTML);
		pluginDetailsCaptionPanel.setCaptionHTML(AdminConstants.PLUGIN_DETAILS_HTML);
		editButtonPanel.setStyleName(AdminConstants.PADDING_STYLE);

	}

	/**
	 * To get Plugin Detail View.
	 * 
	 * @return PluginDetailView
	 */
	public PluginDetailView getPluginDetailView() {
		return pluginDetailView;
	}

	/**
	 * To get Edit Plugin View.
	 * 
	 * @return EditPluginView
	 */
	public EditPluginView getEditPluginView() {
		return editPluginView;
	}

	/**
	 * To get Plugin Data View.
	 * 
	 * @return PluginData
	 */
	public PluginData getPluginDataView() {
		return pluginData;
	}

	/**
	 * To get No Result Label.
	 * 
	 * @return Label
	 */
	public Label getNoResuleLabel() {
		return noResultLabel;
	}
}
