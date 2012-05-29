/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2010-2011 Ephesoft Inc. 
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
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin.KV_PP_PropertiesPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class KV_PP_PropertiesView extends View<KV_PP_PropertiesPresenter> {

	interface Binder extends UiBinder<DockLayoutPanel, KV_PP_PropertiesView> {
	}

	@UiField
	protected PluginData pluginData;

	@UiField
	protected KV_PP_DetailView kvPPDetailView;

	@UiField
	protected KV_PP_EditView kvPPEditView;

	@UiField
	protected KV_PP_ConfigView configView;

	@UiField
	protected VerticalPanel kvppPluginViewVerticalPanel;

	@UiField
	protected LayoutPanel kvppViewEditPluginPanel;

	@UiField
	protected CaptionPanel pluginDetailsCaptionPanel;

	@UiField
	protected CaptionPanel kvppPluginConfigurationCaptionPanel;

	@UiField
	protected VerticalPanel kvppPluginConfigVerticalPanel;

	@UiField
	protected HorizontalPanel kvppEditButtonPanel;

	@UiField
	protected Button configButton;

	private final Button edit;

	@UiField
	protected ScrollPanel scrollPanel;

	private KV_PP_ConfigView kvPPConfigView;

	@UiField
	VerticalPanel configButtonPanel;

	public Button getEditButton() {
		return edit;
	}

	public Label getNoResultLabel() {
		return noResultLabel;
	}

	private final Label noResultLabel;

	public VerticalPanel getKvppPluginViewVerticalPanel() {
		return kvppPluginViewVerticalPanel;
	}

	public VerticalPanel getKvppPluginConfigVerticalPanel() {
		return kvppPluginConfigVerticalPanel;
	}

	public KV_PP_DetailView getKvPPDetailView() {
		return kvPPDetailView;
	}

	public KV_PP_EditView getKvPPEditView() {
		return kvPPEditView;
	}

	public PluginData getPluginDataView() {
		return pluginData;
	}

	public KV_PP_ConfigView getKvPPConfigView() {
		return kvPPConfigView;
	}

	private static final Binder BINDER = GWT.create(Binder.class);

	public KV_PP_PropertiesView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		edit = new Button();
		noResultLabel = new Label();
		noResultLabel.setText(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.NO_RECORD_FOUND));
		noResultLabel.setVisible(false);
		edit.setText(AdminConstants.EDIT_BUTTON);
		kvppPluginConfigurationCaptionPanel.setCaptionHTML(AdminConstants.PLUGIN_CONFIGURATION_HTML);
		pluginDetailsCaptionPanel.setCaptionHTML(AdminConstants.PLUGIN_DETAILS_HTML);
		kvppEditButtonPanel.setStyleName(AdminConstants.PADDING_STYLE);
		kvppEditButtonPanel.add(edit);
		kvppPluginViewVerticalPanel.add(noResultLabel);
		configButton.setText(AdminConstants.CONFIG_BUTTON);
	}

	public Button getConfigButton() {
		return configButton;
	}

	@UiHandler("configButton")
	public void onconfigButtonClick(ClickEvent clickEvent) {
		presenter.onConfigButtonClicked();
	}

	public VerticalPanel getConfigButtonPanel() {
		return configButtonPanel;
	}

	public ScrollPanel getScrollPanel() {
		return scrollPanel;
	}

	public LayoutPanel getKvppViewEditPluginPanel() {
		return kvppViewEditPluginPanel;
	}

	public KV_PP_ConfigView getConfigView() {
		return configView;
	}
}
