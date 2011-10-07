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
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin.PluginViewPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PluginView extends View<PluginViewPresenter> {

	interface Binder extends UiBinder<DockLayoutPanel, PluginView> {
	}

	@UiField
   LayoutPanel viewEditPluginPanel;

	@UiField
	protected PluginDetailView pluginDetailView;
	@UiField
	protected EditPluginView editPluginView;
	@UiField
	protected PluginData pluginData;

	@UiField
	protected VerticalPanel pluginViewVerticalPanel;

	public VerticalPanel getPluginViewVerticalPanel() {
		return pluginViewVerticalPanel;
	}

	public VerticalPanel getPluginConfigVerticalPanel() {
		return pluginConfigVerticalPanel;
	}

	@UiField
	protected VerticalPanel pluginConfigVerticalPanel;
	private final Button edit;
	private final Label noResuleLabel;

	@UiField
	protected CaptionPanel pluginDetailsCaptionPanel;
	@UiField
	protected CaptionPanel pluginConfigurationCaptionPanel;

	@UiField
	protected VerticalPanel editButtonPanel;

	private static final Binder BINDER = GWT.create(Binder.class);

	public PluginView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		edit = new Button();
		noResuleLabel = new Label();
		noResuleLabel.setText(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.NO_RECORD_FOUND));
		noResuleLabel.setVisible(false);
		edit.setText("Edit");
		pluginConfigurationCaptionPanel.setCaptionHTML(AdminConstants.PLUGIN_CONFIGURATION_HTML);
		pluginDetailsCaptionPanel.setCaptionHTML(AdminConstants.PLUGIN_DETAILS_HTML);
		editButtonPanel.setStyleName(AdminConstants.PADDING_STYLE);
		editButtonPanel.add(edit);
		pluginViewVerticalPanel.add(noResuleLabel);
	}

	public PluginDetailView getPluginDetailView() {
		return pluginDetailView;
	}

	public EditPluginView getEditPluginView() {
		return editPluginView;
	}

	public PluginData getPluginDataView() {
		return pluginData;
	}

	public Button getEditButton() {
		return edit;
	}

	public Label getNoResuleLabel() {
		return noResuleLabel;
	}
}
