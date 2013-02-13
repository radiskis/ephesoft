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
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin.BoxExporterPluginPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class provides functionality to edit box exporter.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class BoxExporterPluginView extends View<BoxExporterPluginPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<DockLayoutPanel, BoxExporterPluginView> {
	}

	/**
	 * editBoxExporterView EditBoxExporterView.
	 */
	@UiField
	protected EditBoxExporterView editBoxExporterView;

	/**
	 * boxExporterDetailView BoxExporterDetailView.
	 */
	@UiField
	protected BoxExporterDetailView boxExporterDetailView;

	/**
	 * propertyDetailsPanel CaptionPanel.
	 */
	@UiField
	protected CaptionPanel propertyDetailsPanel;

	/**
	 * propertyDetailsViewPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel propertyDetailsViewPanel;

	/**
	 * editPropertyDetailPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel editPropertyDetailPanel;

	/**
	 * editButtonPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel editButtonPanel;

	/**
	 * pluginData PluginData.
	 */
	@UiField
	protected PluginData pluginData;

	/**
	 * pluginDetailsPanel CaptionPanel.
	 */
	@UiField
	CaptionPanel pluginDetailsPanel;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Constructor.
	 */
	public BoxExporterPluginView() {
		initWidget(BINDER.createAndBindUi(this));

		propertyDetailsPanel.setCaptionHTML(AdminConstants.PLUGIN_CONFIGURATION_HTML);
		pluginDetailsPanel.setCaptionHTML(AdminConstants.PLUGIN_DETAILS_HTML);

		editButtonPanel.setStyleName(AdminConstants.PADDING_STYLE);
	}

	/**
	 * To get Box Exporter Detail View.
	 * 
	 * @return BoxExporterDetailView
	 */
	public BoxExporterDetailView getBoxExporterDetailView() {
		return boxExporterDetailView;
	}

	/**
	 * To get Edit Box Exporter View.
	 * 
	 * @return EditBoxExporterView
	 */
	public EditBoxExporterView getEditBoxExporterView() {
		return editBoxExporterView;
	}

	/**
	 * To get Property Details View Panel.
	 * 
	 * @return VerticalPanel
	 */
	public VerticalPanel getPropertyDetailsViewPanel() {
		return propertyDetailsViewPanel;
	}

	/**
	 * To get Edit Property Detail Panel.
	 * 
	 * @return VerticalPanel
	 */
	public VerticalPanel getEditPropertyDetailPanel() {
		return editPropertyDetailPanel;
	}

	/**
	 * To get Edit Button Panel.
	 * 
	 * @return VerticalPanel
	 */
	public VerticalPanel getEditButtonPanel() {
		return editButtonPanel;
	}

	/**
	 * To show Box Exporter Detail View.
	 */
	public void showBoxExporterDetailView() {
		propertyDetailsViewPanel.setVisible(true);
		editPropertyDetailPanel.setVisible(false);
	}

	/**
	 * Class to get identifier.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 */
	public static class Id {

		/**
		 * ID_CONST long.
		 */
		private static final long ID_CONST = 1000L;

		/**
		 * id long.
		 */
		private static long id = -ID_CONST;

		/**
		 * To get Identifier.
		 * 
		 * @return long
		 */
		public static long getIdentifier() {
			id = id - 1;
			return id;
		}
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
