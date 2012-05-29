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

package com.ephesoft.dcma.gwt.customWorkflow.client.presenter;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.PluginDetailsDTO;
import com.ephesoft.dcma.gwt.customWorkflow.client.CustomWorkflowController;
import com.ephesoft.dcma.gwt.customWorkflow.client.i18n.CustomWorkflowMessages;
import com.ephesoft.dcma.gwt.customWorkflow.client.presenter.dependencies.DependencyManagementPresenter;
import com.ephesoft.dcma.gwt.customWorkflow.client.presenter.dependencies.DependencyPresenter;
import com.ephesoft.dcma.gwt.customWorkflow.client.view.CustomWorkflowManagementView;
import com.ephesoft.dcma.gwt.customWorkflow.client.view.ImportPluginView;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;

public class CustomWorkflowManagementPresenter extends AbstractCustomWorkflowPresenter<CustomWorkflowManagementView> {

	private final CustomWorkflowEntryPresenter customWorkflowEntryPresenter;

	private final DependencyPresenter dependencyPresenter;

	private final DependencyManagementPresenter dependencyManagementPresenter;

	public final static String AND = ",";

	public final static String OR = "/";

	public static boolean isCyclic = false;

	public String selectedDependencyName;

	public String conflictingDependencyName;

	public CustomWorkflowManagementPresenter(CustomWorkflowController controller, CustomWorkflowManagementView view) {
		super(controller, view);
		this.customWorkflowEntryPresenter = new CustomWorkflowEntryPresenter(controller, view.getCustomWorkflowEntryView());
		this.dependencyPresenter = new DependencyPresenter(controller, view.getDependencyView());
		this.dependencyManagementPresenter = new DependencyManagementPresenter(controller, view.getDependencyManagementView());
		init();
	}

	public void init() {

		ScreenMaskUtility.maskScreen(LocaleDictionary.get().getMessageValue(CustomWorkflowMessages.LOADING_PLUGINS));
		controller.getRpcService().getAllPluginDetailDTOs(new AsyncCallback<List<PluginDetailsDTO>>() {

			@Override
			public void onSuccess(List<PluginDetailsDTO> pluginsDTOList) {
				sortPluginList(pluginsDTOList, true);
				controller.setAllPlugins(pluginsDTOList);
				Map<String, String> allPluginsNameToDescriptionMap = new LinkedHashMap<String, String>();
				for (PluginDetailsDTO pluginDetailsDTO : pluginsDTOList) {
					allPluginsNameToDescriptionMap.put(pluginDetailsDTO.getPluginName(), pluginDetailsDTO.getPluginDescription());
				}

				controller.setAllPluginsNameToDescriptionMap(allPluginsNameToDescriptionMap);
				customWorkflowEntryPresenter.getViewAndAddPluginsPresenter().bind();
				ScreenMaskUtility.unmaskScreen();
			}

			@Override
			public void onFailure(Throwable arg0) {
				ScreenMaskUtility.unmaskScreen();
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						CustomWorkflowMessages.UNABLE_TO_GET_ALL_PLUGINS_LIST));

			}
		});
	}

	public void sortPluginList(List<PluginDetailsDTO> pluginsList, final boolean ascending) {
		Collections.sort(pluginsList, new Comparator<PluginDetailsDTO>() {

			@Override
			public int compare(PluginDetailsDTO PluginDTO1, PluginDetailsDTO PluginDTO2) {
				int result;
				String orderNumberOne = PluginDTO1.getPluginName();
				String orderNumberTwo = PluginDTO2.getPluginName();
				if (orderNumberOne != null && orderNumberTwo != null) {
					result = orderNumberOne.compareTo(orderNumberTwo);
				} else if (orderNumberOne == null && orderNumberTwo == null) {
					result = 0;
				} else if (orderNumberOne == null) {
					result = -1;
				} else {
					result = 1;
				}
				return result;
			}
		});
	}

	/**
	 * @return the customWorkflowEntryPresenter
	 */
	public CustomWorkflowEntryPresenter getCustomWorkflowEntryPresenter() {
		return customWorkflowEntryPresenter;
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {

	}

	@Override
	public void bind() {

	}

	public void showImportPluginView() {
		final DialogBox dialogBox = new DialogBox();
		final ImportPluginView importPluginView = new ImportPluginView();
		ImportPluginPresenter importPluginPresenter = new ImportPluginPresenter(controller, importPluginView);

		importPluginView.setDialogBox(dialogBox);
		importPluginPresenter.bind();

		importPluginPresenter.showPluginImportView();
		importPluginView.getSaveButton().setFocus(true);

	}

	public void addNewPlugin() {
		showImportPluginView();
	}

	public void showDependenciesView() {

		if (controller.getAllPlugins() == null) {
			ScreenMaskUtility.maskScreen(LocaleDictionary.get().getMessageValue(CustomWorkflowMessages.LOADING_DEPENDENCIES));
			controller.getRpcService().getAllPluginDetailDTOs(new AsyncCallback<List<PluginDetailsDTO>>() {

				@Override
				public void onFailure(Throwable arg0) {
					ScreenMaskUtility.unmaskScreen();
				}

				@Override
				public void onSuccess(List<PluginDetailsDTO> pluginDetailsDTOs) {
					showDependenciesView(pluginDetailsDTOs);
				}

			});
		} else {
			showDependenciesView(controller.getAllPlugins());

		}

	}

	/**
	 * @param pluginDetailsDTOs
	 */
	private void showDependenciesView(List<PluginDetailsDTO> pluginDetailsDTOs) {
		controller.setAllPlugins(pluginDetailsDTOs);
		dependencyManagementPresenter.getBreadCrumbPresenter().createBreadCrumbForDependenciesList();
		dependencyManagementPresenter.bind();
		view.showDependencyManagementView();
		dependencyManagementPresenter.getBreadCrumbPresenter().setbackButtonVisibility(true);
		ScreenMaskUtility.unmaskScreen();
	}

	/**
	 * @return the dependencyPresenter
	 */
	public DependencyPresenter getDependencyPresenter() {
		return dependencyPresenter;
	}

	public void showEntryView() {
		getView().showEntryView();
	}

	/**
	 * @return the dependencyManagementPresenter
	 */
	public DependencyManagementPresenter getDependencyManagementPresenter() {
		return dependencyManagementPresenter;
	}

}
