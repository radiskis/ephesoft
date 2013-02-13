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

/**
 * This class provides functionality to edit KV PP properties.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class KV_PP_PropertiesView extends View<KV_PP_PropertiesPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<DockLayoutPanel, KV_PP_PropertiesView> {
	}

	/**
	 * pluginData PluginData.
	 */
	@UiField
	protected PluginData pluginData;

	/**
	 * kvPPDetailView KV_PP_DetailView.
	 */
	@UiField
	protected KV_PP_DetailView kvPPDetailView;

	/**
	 * kvPPEditView KV_PP_EditView.
	 */
	@UiField
	protected KV_PP_EditView kvPPEditView;

	/**
	 * configView KV_PP_ConfigView.
	 */
	@UiField
	protected KV_PP_ConfigView configView;

	/**
	 * kvppPluginViewVerticalPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel kvppPluginViewVerticalPanel;

	/**
	 * kvppViewEditPluginPanel LayoutPanel.
	 */
	@UiField
	protected LayoutPanel kvppViewEditPluginPanel;

	/**
	 * pluginDetailsCaptionPanel CaptionPanel.
	 */
	@UiField
	protected CaptionPanel pluginDetailsCaptionPanel;

	/**
	 * kvppPluginConfigurationCaptionPanel CaptionPanel.
	 */
	@UiField
	protected CaptionPanel kvppPluginConfigurationCaptionPanel;

	/**
	 * kvppPluginConfigVerticalPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel kvppPluginConfigVerticalPanel;

	/**
	 * kvppEditButtonPanel HorizontalPanel.
	 */
	@UiField
	protected HorizontalPanel kvppEditButtonPanel;

	/**
	 * configButton Button.
	 */
	@UiField
	protected Button configButton;

	/**
	 * edit Button.
	 */
	private final Button edit;

	/**
	 * scrollPanel ScrollPanel.
	 */
	@UiField
	protected ScrollPanel scrollPanel;

	/**
	 * kvPPConfigView KV_PP_ConfigView.
	 */
	private KV_PP_ConfigView kvPPConfigView;

	/**
	 * configButtonPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel configButtonPanel;

	/**
	 * To get Edit Button.
	 * 
	 * @return Button
	 */
	public Button getEditButton() {
		return edit;
	}

	/**
	 * To get No Result Label.
	 * 
	 * @return Label
	 */
	public Label getNoResultLabel() {
		return noResultLabel;
	}

	/**
	 * noResultLabel Label.
	 */
	private final Label noResultLabel;

	/**
	 * To get Kv pp Plugin View Vertical Panel.
	 * 
	 * @return VerticalPanel
	 */
	public VerticalPanel getKvppPluginViewVerticalPanel() {
		return kvppPluginViewVerticalPanel;
	}

	/**
	 * To get Kv pp Plugin Config Vertical Panel.
	 * 
	 * @return VerticalPanel
	 */
	public VerticalPanel getKvppPluginConfigVerticalPanel() {
		return kvppPluginConfigVerticalPanel;
	}

	/**
	 * To get Kv PP Detail View.
	 * 
	 * @return KV_PP_DetailView
	 */
	public KV_PP_DetailView getKvPPDetailView() {
		return kvPPDetailView;
	}

	/**
	 * To get Kv PP Edit View.
	 * 
	 * @return KV_PP_EditView
	 */
	public KV_PP_EditView getKvPPEditView() {
		return kvPPEditView;
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
	 * To get Kv PP Config View.
	 * 
	 * @return KV_PP_ConfigView
	 */
	public KV_PP_ConfigView getKvPPConfigView() {
		return kvPPConfigView;
	}

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Constructor.
	 */
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

	/**
	 * To get Config Button.
	 * 
	 * @return Button
	 */
	public Button getConfigButton() {
		return configButton;
	}

	/**
	 * To perform operations on config button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("configButton")
	public void onconfigButtonClick(ClickEvent clickEvent) {
		presenter.onConfigButtonClicked();
	}

	/**
	 * To get Config Button Panel.
	 * 
	 * @return VerticalPanel
	 */
	public VerticalPanel getConfigButtonPanel() {
		return configButtonPanel;
	}

	/**
	 * To get Scroll Panel.
	 * 
	 * @return ScrollPanel
	 */
	public ScrollPanel getScrollPanel() {
		return scrollPanel;
	}

	/**
	 * To get Kv pp View Edit Plugin Panel.
	 * 
	 * @return LayoutPanel
	 */
	public LayoutPanel getKvppViewEditPluginPanel() {
		return kvppViewEditPluginPanel;
	}

	/**
	 * To get Config View.
	 * 
	 * @return KV_PP_ConfigView
	 */
	public KV_PP_ConfigView getConfigView() {
		return configView;
	}
}
