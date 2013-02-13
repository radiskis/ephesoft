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

package com.ephesoft.dcma.gwt.customworkflow.client.presenter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.da.property.PluginProperty;
import com.ephesoft.dcma.gwt.core.client.EphesoftAsyncCallback;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.PluginDetailsDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.ephesoft.dcma.gwt.core.shared.comparator.PluginComparator;
import com.ephesoft.dcma.gwt.customworkflow.client.CustomWorkflowController;
import com.ephesoft.dcma.gwt.customworkflow.client.i18n.CustomWorkflowConstants;
import com.ephesoft.dcma.gwt.customworkflow.client.i18n.CustomWorkflowMessages;
import com.ephesoft.dcma.gwt.customworkflow.client.presenter.dependencies.DependencyManagementPresenter;
import com.ephesoft.dcma.gwt.customworkflow.client.presenter.dependencies.DependencyPresenter;
import com.ephesoft.dcma.gwt.customworkflow.client.view.CustomWorkflowManagementView;
import com.ephesoft.dcma.gwt.customworkflow.client.view.ImportPluginView;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;

public class CustomWorkflowManagementPresenter extends AbstractCustomWorkflowPresenter<CustomWorkflowManagementView> {

	private final CustomWorkflowEntryPresenter customWorkflowEntryPresenter;

	private final DependencyPresenter dependencyPresenter;

	private final DependencyManagementPresenter dependencyManagementPresenter;

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

	public final void init() {

		ScreenMaskUtility.maskScreen(LocaleDictionary.get().getMessageValue(CustomWorkflowMessages.LOADING_PLUGINS));
		controller.getRpcService().getAllPluginDetailDTOs(new EphesoftAsyncCallback<List<PluginDetailsDTO>>() {

			@Override
			public void onSuccess(List<PluginDetailsDTO> pluginsDTOList) {
				Order order = new Order(PluginProperty.NAME, true);
				PluginComparator pluginComparator = new PluginComparator(order);
				Collections.sort(pluginsDTOList, pluginComparator);
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
			public void customFailure(Throwable arg0) {
				ScreenMaskUtility.unmaskScreen();
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						CustomWorkflowMessages.UNABLE_TO_GET_ALL_PLUGINS_LIST));

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
		/**
		 * Inject your events here
		 */
	}

	@Override
	public void bind() {
		/**
		 * Bind your view with the values.
		 */
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

	public void checkForDirtyPlugin() {
		List<PluginDetailsDTO> pluginDetailsDTOs = controller.getAllPlugins();
		boolean dirtyPlugin = false;
		for (PluginDetailsDTO pluginDetailsDTO : pluginDetailsDTOs) {
			if (pluginDetailsDTO.isDirty()) {
				dirtyPlugin = true;
				final ConfirmationDialog confirmationDialog = new ConfirmationDialog();

				confirmationDialog.setText(LocaleDictionary.get().getMessageValue(CustomWorkflowMessages.WARNING));
				confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(
						CustomWorkflowMessages.UNSAVED_CHANGES_LOST_WARNING));
				confirmationDialog.cancelButton
						.setText(LocaleDictionary.get().getConstantValue(CustomWorkflowConstants.CANCEL_BUTTON));
				confirmationDialog.okButton.setText(LocaleDictionary.get().getConstantValue(CustomWorkflowConstants.OK_CONSTANT));
				confirmationDialog.center();
				confirmationDialog.show();
				confirmationDialog.okButton.setFocus(true);
				confirmationDialog.addDialogListener(new DialogListener() {

					@Override
					public void onOkClick() {
						Window.Location.reload();
					}

					@Override
					public void onCancelClick() {
						confirmationDialog.hide();
						dependencyManagementPresenter.getBreadCrumbPresenter().createBreadCrumbForDependenciesList();
					}
				});

				break;
			}

		}
		if (!dirtyPlugin) {
			Window.Location.reload();
		}
	}

	public PluginDetailsDTO getPluginDtoForName(String pluginName) {
		PluginDetailsDTO pluginDetailsDTO = null;

		final List<PluginDetailsDTO> allPlugins = controller.getAllPlugins();
		if (allPlugins != null) {
			for (PluginDetailsDTO pluginDto : allPlugins) {
				if (pluginDto.getPluginName().equals(pluginName)) {
					pluginDetailsDTO = pluginDto;
					break;
				}
			}
		}
		return pluginDetailsDTO;

	}

	public PluginDetailsDTO getPluginDtoForIdentifier(String pluginIdentifier) {
		PluginDetailsDTO pluginDetailsDTO = null;
		final List<PluginDetailsDTO> allPlugins = controller.getAllPlugins();
		if (allPlugins != null) {
			for (PluginDetailsDTO pluginDto : controller.getAllPlugins()) {
				if (pluginDto.getIdentifier().equals(pluginIdentifier)) {
					pluginDetailsDTO = pluginDto;
					break;
				}
			}
		}
		return pluginDetailsDTO;

	}

}
