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

package com.ephesoft.dcma.gwt.admin.bm.client.view.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.module.ModuleViewPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin.PluginListPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.plugin.PluginListView;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView;
import com.ephesoft.dcma.gwt.core.client.ui.table.Record;
import com.ephesoft.dcma.gwt.core.client.ui.table.Table;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginDTO;
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
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class provides functionality to edit module.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class ModuleView extends View<ModuleViewPresenter> {

	/**
	 * HEIGHT String.
	 */
	private static final String HEIGHT = "20px";

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<DockLayoutPanel, ModuleView> {
	}

	/**
	 * moduleDetailView ModuleDetailView.
	 */
	@UiField
	protected ModuleDetailView moduleDetailView;

	/**
	 * editModuleView EditModuleView.
	 */
	@UiField
	protected EditModuleView editModuleView;

	/**
	 * pluginListPanel LayoutPanel.
	 */
	@UiField
	protected LayoutPanel pluginListPanel;

	/**
	 * pluginListView PluginListView.
	 */
	private final PluginListView pluginListView;

	/**
	 * pluginListPresenter PluginListPresenter.
	 */
	private PluginListPresenter pluginListPresenter;

	/**
	 * moduleConfigurationCaptionPanel CaptionPanel.
	 */
	@UiField
	protected CaptionPanel moduleConfigurationCaptionPanel;

	/**
	 * editPlugin Button.
	 */
	@UiField
	protected Button editPlugin;

	/**
	 * addPlugin Button.
	 */
	@UiField
	protected Button addPlugin;

	/**
	 * buttonPanel HorizontalPanel.
	 */
	@UiField
	protected HorizontalPanel buttonPanel;

	/**
	 * moduleViewPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel moduleViewPanel;

	/**
	 * moduleDetailViewPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel moduleDetailViewPanel;

	/**
	 * editModuleViewPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel editModuleViewPanel;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Constructor.
	 */
	public ModuleView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		pluginListView = new PluginListView();
		pluginListPresenter = null;
		moduleConfigurationCaptionPanel.setCaptionHTML(AdminConstants.MODULE_CONFIGURATION);
		pluginListPanel.add(pluginListView.listView);
		editPlugin.setText(AdminConstants.EDIT_BUTTON);
		editPlugin.setHeight(HEIGHT);
		addPlugin.setText(AdminConstants.EDIT_LIST_BUTTON);
		addPlugin.setHeight(HEIGHT);
		buttonPanel.setStyleName("topPadd");

	}

	/**
	 * To create Plugin List.
	 * 
	 * @param plugins Collection<BatchClassPluginDTO>
	 */
	public void createPluginList(Collection<BatchClassPluginDTO> plugins) {
		int maxResult = 0;
		List<BatchClassPluginDTO> pluginsList = new ArrayList<BatchClassPluginDTO>(plugins);

		presenter.getController().getMainPresenter().sortPluginList(pluginsList);
		List<Record> recordList = setPluginList(pluginsList);

		pluginListPresenter = new PluginListPresenter(presenter.getController(), pluginListView);
		pluginListPresenter.setPluginDetailDTO(plugins);
		maxResult = Math.min(recordList.size(), Table.visibleRecodrCount);
		pluginListView.listView.initTable(recordList.size(), pluginListPresenter, recordList.subList(0, maxResult), true, false,
				pluginListPresenter, null, false);
	}

	/**
	 * To set Plugin List.
	 * 
	 * @param plugins Collection<BatchClassPluginDTO>
	 * @return List<Record>
	 */
	public List<Record> setPluginList(Collection<BatchClassPluginDTO> plugins) {
		List<Record> recordList = new LinkedList<Record>();
		if (null == plugins) {
			recordList = null;
		} else {
			for (final BatchClassPluginDTO pluginDTO : plugins) {
				if (!pluginDTO.isDeleted()) {
					Record record = new Record(pluginDTO.getIdentifier());
					record.addWidget(pluginListView.name, new Label(pluginDTO.getPlugin().getPluginName()));
					record.addWidget(pluginListView.description, new Label(pluginDTO.getPlugin().getPluginDescription()));
					record.addWidget(pluginListView.information, new Label(pluginDTO.getPlugin().getPluginInformation()));
					recordList.add(record);
				}
			}
		}
		return recordList;
	}

	/**
	 * To get Module Detail View.
	 * 
	 * @return ModuleDetailView
	 */
	public ModuleDetailView getModuleDetailView() {
		return moduleDetailView;
	}

	/**
	 * To get Module List View.
	 * 
	 * @return ListView
	 */
	public ListView getModuleListView() {
		return pluginListView.listView;
	}

	/**
	 * To get Plugin List View.
	 * 
	 * @return ListView
	 */
	public ListView getPluginListView() {
		return pluginListView.listView;
	}

	/**
	 * To perform operations on Edit Plugin Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("editPlugin")
	public void onEditPluginClicked(ClickEvent clickEvent) {
		pluginListPresenter.onEditButtonClicked();
	}

	/**
	 * To perform operations on add Plugin Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("addPlugin")
	public void onAddPluginClicked(ClickEvent clickEvent) {

		// New Code

		String moduleIdentifier = presenter.getController().getSelectedModule().getIdentifier();

		Map<String, String> pluginIdentifierToNameMap = new LinkedHashMap<String, String>(0);
		List<BatchClassPluginDTO> batchClassPlugins = new ArrayList<BatchClassPluginDTO>(presenter.getController().getBatchClass()
				.getModuleByIdentifier(moduleIdentifier).getBatchClassPlugins());
		presenter.getController().getMainPresenter().sortPluginList(batchClassPlugins);
		for (BatchClassPluginDTO batchClassPluginDTO : batchClassPlugins) {
			if (!batchClassPluginDTO.isDeleted()) {
				pluginIdentifierToNameMap.put(batchClassPluginDTO.getIdentifier(), batchClassPluginDTO.getPlugin().getPluginName());
			}
		}
		presenter.getController().setPluginIdentifierToNameMap(pluginIdentifierToNameMap);
		presenter.getController().getMainPresenter().showAddPluginView();
	}

	/**
	 * To get Module View Panel.
	 * 
	 * @return VerticalPanel
	 */
	public VerticalPanel getModuleViewPanel() {
		return moduleViewPanel;
	}

	/**
	 * To get Module Detail View Panel.
	 * 
	 * @return VerticalPanel
	 */
	public VerticalPanel getModuleDetailViewPanel() {
		return moduleDetailViewPanel;
	}

	/**
	 * To get Edit Module View Panel.
	 * 
	 * @return VerticalPanel
	 */
	public VerticalPanel getEditModuleViewPanel() {
		return editModuleViewPanel;
	}

	/**
	 * To get Edit Module View.
	 * 
	 * @return EditModuleView
	 */
	public EditModuleView getEditModuleView() {
		return editModuleView;
	}

	/**
	 * To get Add Plugin.
	 * 
	 * @return the addPlugin
	 */
	public Button getAddPlugin() {
		return addPlugin;
	}

	/**
	 * To set Add Plugin.
	 * 
	 * @param addPlugin Button
	 */
	public void setAddPlugin(Button addPlugin) {
		this.addPlugin = addPlugin;
	}

	/**
	 * To get Edit Plugin.
	 * 
	 * @return the editPlugin
	 */
	public Button getEditPlugin() {
		return editPlugin;
	}
}
