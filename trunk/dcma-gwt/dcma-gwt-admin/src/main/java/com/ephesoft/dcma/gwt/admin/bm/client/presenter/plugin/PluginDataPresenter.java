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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin;

import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.plugin.PluginData;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginDTO;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;

/**
 * This class provides functionality to handle plugin data presentation to user.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class PluginDataPresenter extends AbstractBatchClassPresenter<PluginData> {

	/**
	 * Integer constant for THREE.
	 */
	public static final int THREE = 3;

	/**
	 * viewTable FlexTable.
	 */
	private FlexTable viewTable;

	/**
	 * dataTable FlexTable.
	 */
	private FlexTable dataTable;

	/**
	 * pluginName Label.
	 */
	private final Label pluginName;

	/**
	 * pluginNameValue Label.
	 */
	private final Label pluginNameValue;

	/**
	 * pluginDescription Label.
	 */
	private final Label pluginDescription;

	/**
	 * pluginDescriptionValue Label.
	 */
	private final Label pluginDescriptionValue;

	/**
	 * pluginVersion Label.
	 */
	private final Label pluginVersion;

	/**
	 * pluginVersionValue Label.
	 */
	private final Label pluginVersionValue;

	/**
	 * pluginInformation Label.
	 */
	private final Label pluginInformation;

	/**
	 * pluginInformationValue Label.
	 */
	private final Label pluginInformationValue;

	/**
	 * Constructor.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view PluginData
	 */
	public PluginDataPresenter(BatchClassManagementController controller, PluginData view) {
		super(controller, view);
		pluginName = new Label(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.PLUGIN_NAME)
				+ BatchClassManagementConstants.COLON);
		pluginDescription = new Label(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.DESCRIPTION)
				+ BatchClassManagementConstants.COLON);
		pluginNameValue = new Label();
		pluginDescriptionValue = new Label();
		pluginVersion = new Label();
		pluginVersionValue = new Label();
		pluginInformation = new Label(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.PLUGIN_INFORMATION)
				+ BatchClassManagementConstants.COLON);
		pluginInformationValue = new Label();
	}

	/**
	 * To set properties.
	 */
	public void setProperties() {

		BatchClassPluginDTO selectedPlugin = controller.getSelectedPlugin();
		if (selectedPlugin != null) {

			viewTable.setWidget(0, 0, pluginName);
			pluginNameValue.setText(selectedPlugin.getPlugin().getPluginName());
			viewTable.setWidget(0, 2, pluginNameValue);

			viewTable.setWidget(1, 0, pluginDescription);
			pluginDescriptionValue.setText(selectedPlugin.getPlugin().getPluginDescription());
			viewTable.setWidget(1, 2, pluginDescriptionValue);

			viewTable.setWidget(2, 0, pluginInformation);
			pluginInformationValue.setText(selectedPlugin.getPlugin().getPluginInformation());
			viewTable.setWidget(2, 2, pluginInformationValue);

			for (int row = 0; row < THREE; row++) {
				viewTable.getFlexCellFormatter().setAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT,
						HasVerticalAlignment.ALIGN_MIDDLE);
				viewTable.getFlexCellFormatter().addStyleName(row, 0, "leftCell");
				viewTable.getFlexCellFormatter().addStyleName(row, 2, "rightCell");
				viewTable.getFlexCellFormatter().addStyleName(row, 0, "bold_text");
			}
		}
	}

	/**
	 * To show Plugin Data View.
	 * 
	 * @return PluginData
	 */
	public PluginData showPluginDataView() {
		return view;
	}

	/**
	 * To get Data Table.
	 * 
	 * @return FlexTable
	 */
	public FlexTable getDataTable() {
		return dataTable;
	}

	/**
	 * To get Plugin Version.
	 * 
	 * @return Label
	 */
	public Label getPluginVersion() {
		return pluginVersion;
	}

	/**
	 * To get Plugin Version Value.
	 * 
	 * @return Label
	 */
	public Label getPluginVersionValue() {
		return pluginVersionValue;
	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {
		if (controller.getSelectedPlugin() != null) {
			this.dataTable = view.getViewTable();
			viewTable = new FlexTable();
			viewTable.setWidth("100%");
			viewTable.setCellSpacing(0);
			viewTable.getColumnFormatter().setWidth(0, "10%");
			viewTable.getColumnFormatter().setWidth(1, "1%");
			viewTable.getColumnFormatter().setWidth(2, "70%");
			viewTable.getFlexCellFormatter().addStyleName(0, 0, "topBorder");
			viewTable.getFlexCellFormatter().addStyleName(0, 1, "topBorder");
			viewTable.getFlexCellFormatter().addStyleName(0, 2, "topBorder");
			dataTable.setWidget(0, 0, viewTable);
			dataTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
			setProperties();
		}
	}

	/**
	 * To handle events.
	 * 
	 * @param eventBus HandlerManager
	 */
	@Override
	public void injectEvents(HandlerManager eventBus) {
		// TODO Auto-generated method stub

	}
}
