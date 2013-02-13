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
import java.util.List;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.da.property.DependencyTypeProperty;
import com.ephesoft.dcma.da.property.PluginProperty;
import com.ephesoft.dcma.gwt.core.client.EphesoftAsyncCallback;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.DoubleClickListner;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.PaginationListner;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.DependencyDTO;
import com.ephesoft.dcma.gwt.core.shared.PluginDetailsDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.ephesoft.dcma.gwt.core.shared.comparator.PluginComparator;
import com.ephesoft.dcma.gwt.customworkflow.client.CustomWorkflowController;
import com.ephesoft.dcma.gwt.customworkflow.client.i18n.CustomWorkflowConstants;
import com.ephesoft.dcma.gwt.customworkflow.client.i18n.CustomWorkflowMessages;
import com.ephesoft.dcma.gwt.customworkflow.client.view.ViewAndAddPluginsView;
import com.google.gwt.event.shared.HandlerManager;

public class ViewAndAddPluginsPresenter extends AbstractCustomWorkflowPresenter<ViewAndAddPluginsView> implements PaginationListner,
		DoubleClickListner {

	private final AllPluginsListPresenter allPluginsListPresenter;

	public ViewAndAddPluginsPresenter(CustomWorkflowController customWorkflowController, ViewAndAddPluginsView viewAndAddPluginsView) {
		super(customWorkflowController, viewAndAddPluginsView);

		this.allPluginsListPresenter = new AllPluginsListPresenter(controller, view.getAllPluginsListView());
		view.getAllPluginsListView().listView.setTableRowCount(CustomWorkflowConstants.TABLE_ROW_COUNT);

	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		/**
		 * Inject your events here
		 */
	}

	@Override
	public void bind() {
		if (controller.getAllPluginsNameToDescriptionMap() != null) {
			view.createPluginsList(controller.getAllPlugins());
		}
	}

	public void showEntryView() {

		controller.getCustomWorkflowManagementPresenter().getView().getCustomWorkflowEntryView().setVisible(true);

	}

	public void addNewPlugin() {
		controller.getCustomWorkflowManagementPresenter().showImportPluginView();
	}

	/**
	 * @return the allPluginsListPresenter
	 */
	public AllPluginsListPresenter getAllPluginsListPresenter() {
		return allPluginsListPresenter;
	}

	public String createHelpContent() {

		String pluginAddingStepsLocaleMessage = LocaleDictionary.get().getMessageValue(CustomWorkflowMessages.PLUGIN_ADDING_STEPS);
		String makeLocaleMessage = LocaleDictionary.get().getMessageValue(CustomWorkflowMessages.MAKE);
		String pluginTwoFilesLocaleMessage = LocaleDictionary.get().getMessageValue(CustomWorkflowMessages.PLUGIN_HAVING_TWO_FILES);
		String jarDescriptionLocaleMessage = LocaleDictionary.get().getMessageValue(
				CustomWorkflowMessages.JAR_FOR_THE_PLUGIN_TO_BE_ADDED);
		String xmlDescriptionLocaleMessage = LocaleDictionary.get().getMessageValue(
				CustomWorkflowMessages.FILE_INFORMATION_ABOUT_THE_PLUGIN);
		String zipContentLocaleMessage = LocaleDictionary.get().getMessageValue(CustomWorkflowMessages.FILE_CONTAIN_TWO_FILES);
		String fileKeywordLocaleMessage = LocaleDictionary.get().getMessageValue(CustomWorkflowMessages.FILE_AND);
		String sameNameLocaleMessage = LocaleDictionary.get().getMessageValue(CustomWorkflowMessages.FILE_SAME_NAME);
		String jarContentWarningLocaleMessage = LocaleDictionary.get().getMessageValue(
				CustomWorkflowMessages.FILE_CANNOT_BE_VERIFIED_WARNING);
		String jarRequiredContentLocaleMessage = LocaleDictionary.get().getMessageValue(CustomWorkflowMessages.REQUIRED_CONTENT);
		String tomcatRestartWarning = LocaleDictionary.get().getMessageValue(CustomWorkflowMessages.RESTART_TOMCAT_WARNING);

		String keyBoardShortcuts = "<p><b>" + pluginAddingStepsLocaleMessage + "</b></p>" + "<ol>" + CustomWorkflowConstants.HTML_LI
				+ makeLocaleMessage + CustomWorkflowConstants.BREAK_TAG_START + CustomWorkflowConstants.ZIP_FILE
				+ CustomWorkflowConstants.BREAK_TAG_END + pluginTwoFilesLocaleMessage + CustomWorkflowConstants.HTML_LI_END + "<ol>"
				+ " <li type=\"a\">" + CustomWorkflowConstants.BREAK_TAG_START + CustomWorkflowConstants.JAR_FILE
				+ CustomWorkflowConstants.BREAK_TAG_END + jarDescriptionLocaleMessage + CustomWorkflowConstants.HTML_LI_END
				+ " <li type=\"a\">" + CustomWorkflowConstants.BREAK_TAG_START + CustomWorkflowConstants.XML_FILE
				+ CustomWorkflowConstants.BREAK_TAG_END + xmlDescriptionLocaleMessage + CustomWorkflowConstants.HTML_LI_END + "</ol>"
				+ CustomWorkflowConstants.HTML_LI + CustomWorkflowConstants.BREAK_TAG_START + CustomWorkflowConstants.ZIP_FILE
				+ CustomWorkflowConstants.BREAK_TAG_END + zipContentLocaleMessage + CustomWorkflowConstants.HTML_LI_END
				+ CustomWorkflowConstants.HTML_LI + CustomWorkflowConstants.BREAK_TAG_START + CustomWorkflowConstants.ZIP_FILE
				+ CustomWorkflowConstants.BREAK_TAG_END + fileKeywordLocaleMessage + CustomWorkflowConstants.BREAK_TAG_START
				+ CustomWorkflowConstants.JAR_FILE + CustomWorkflowConstants.BREAK_TAG_END + sameNameLocaleMessage
				+ CustomWorkflowConstants.HTML_LI_END + CustomWorkflowConstants.HTML_LI + CustomWorkflowConstants.BREAK_TAG_START
				+ CustomWorkflowConstants.JAR_FILE + CustomWorkflowConstants.BREAK_TAG_END + jarContentWarningLocaleMessage
				+ jarRequiredContentLocaleMessage + CustomWorkflowConstants.HTML_LI_END + CustomWorkflowConstants.HTML_LI
				+ tomcatRestartWarning + CustomWorkflowConstants.HTML_LI_END + "</ol>";

		return keyBoardShortcuts;
	}

	public void onAddNewPluginHelp() {

		String helpContent = createHelpContent();
		final ConfirmationDialog confirmationDialog = new ConfirmationDialog();
		confirmationDialog.setMessage(helpContent);
		confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(CustomWorkflowConstants.HELP_BUTTON));
		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onCancelClick() {
				confirmationDialog.hide();
			}

			@Override
			public void onOkClick() {
				confirmationDialog.hide();
			}

		});
		confirmationDialog.setPerformCancelOnEscape(true);
		confirmationDialog.show();
		confirmationDialog.center();
		confirmationDialog.okButton.setFocus(true);
		confirmationDialog.cancelButton.setVisible(false);
		confirmationDialog.getPanel().getElementById(CustomWorkflowConstants.TEXT_PANEL).addClassName(
				CustomWorkflowConstants.HELP_CONTENT);

	}

	public void showDependenciesView() {
		controller.getCustomWorkflowManagementPresenter().getView().showDependencyManagementView();
		final String selectedPluginIndex = view.getAllPluginsListView().getAllPluginsListView().getSelectedRowIndex();

		final PluginDetailsDTO selectedPluginDto = controller.getCustomWorkflowManagementPresenter().getPluginDtoForIdentifier(
				selectedPluginIndex);

		if (selectedPluginDto != null) {
			controller.setSelectedPlugin(selectedPluginDto.getPluginName());
		}
		controller.getCustomWorkflowManagementPresenter().getDependencyManagementPresenter().showDependenciesView();
	}

	@Override
	public void onPagination(int startIndex, int maxResult, Order order) {
		Order finalOrder;
		if (order == null) {
			finalOrder = new Order(PluginProperty.NAME, true);
		} else {
			finalOrder = order;
		}
		PluginComparator pluginComparator = new PluginComparator(finalOrder);
		List<PluginDetailsDTO> allPluginsList = controller.getAllPlugins();

		Collections.sort(allPluginsList, pluginComparator);
		int totalSize = allPluginsList.size();
		int lastIndex = startIndex + maxResult;
		view.updatePluginsList(allPluginsList, totalSize, startIndex, Math.min(totalSize, lastIndex));
	}

	public void deletePlugin() {
		final String selectedPluginIndex = view.getAllPluginsListView().getAllPluginsListView().getSelectedRowIndex();

		final PluginDetailsDTO selectedPluginDTO = controller.getCustomWorkflowManagementPresenter().getPluginDtoForIdentifier(
				selectedPluginIndex);

		if (selectedPluginDTO != null) {
			ScreenMaskUtility.maskScreen(LocaleDictionary.get().getMessageValue(CustomWorkflowConstants.DELETING_PLUGIN_MESSAGE));
			controller.getRpcService().deletePlugin(selectedPluginDTO, new EphesoftAsyncCallback<Boolean>() {

				@Override
				public void onSuccess(Boolean isPluginDeleted) {
					if (isPluginDeleted) {
						updateAllPluginDependencies(controller.getAllPlugins(), selectedPluginDTO.getPluginName());
						controller.getRpcService().updateAllPluginDetailsDTOs(controller.getAllPlugins(),
								new EphesoftAsyncCallback<List<PluginDetailsDTO>>() {

									@Override
									public void onSuccess(List<PluginDetailsDTO> allPluginDetailsDTOs) {
										ScreenMaskUtility.unmaskScreen();
										controller.setAllPlugins(allPluginDetailsDTOs);
										ConfirmationDialogUtil.showConfirmationDialogSuccess(LocaleDictionary.get().getMessageValue(
												CustomWorkflowConstants.PLUGIN_DELETED_SUCCESSFULLY));
										getController().getCustomWorkflowManagementPresenter().getCustomWorkflowEntryPresenter()
												.getViewAndAddPluginsPresenter().bind();

										getController().getCustomWorkflowManagementPresenter().init();
									}

									@Override
									public void customFailure(Throwable arg0) {
										ScreenMaskUtility.unmaskScreen();
									}
								});

					}
				}

				@Override
				public void customFailure(Throwable failureMessage) {
					ScreenMaskUtility.unmaskScreen();
					final String message = failureMessage.getLocalizedMessage();
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(message));
				}
			});
		}
	}

	/**
	 * This method is used to remove the pluginName string from the dependencies string.
	 * 
	 * @param allPlugins
	 * @param pluginName
	 */
	private void updateAllPluginDependencies(List<PluginDetailsDTO> allPlugins, String pluginName) {

		StringBuilder orRegexBuilder = new StringBuilder();

		orRegexBuilder.append(CustomWorkflowConstants.BOUNDARY);
		orRegexBuilder.append(pluginName);
		orRegexBuilder.append(CustomWorkflowConstants.OR_REPLACE_REGEX);
		orRegexBuilder.append(pluginName);
		orRegexBuilder.append(CustomWorkflowConstants.BOUNDARY);

		String orRegex = orRegexBuilder.toString();

		StringBuilder andRegexBuilder = new StringBuilder();

		andRegexBuilder.append(CustomWorkflowConstants.BOUNDARY);
		andRegexBuilder.append(pluginName);
		andRegexBuilder.append(CustomWorkflowConstants.AND_REPLACE_REGEX);
		andRegexBuilder.append(pluginName);
		andRegexBuilder.append(CustomWorkflowConstants.BOUNDARY);

		String andRegex = andRegexBuilder.toString();

		for (PluginDetailsDTO pluginDetailsDTO : allPlugins) {

			for (DependencyDTO dependencyDTO : pluginDetailsDTO.getDependencies()) {
				String originalDependenciesString = dependencyDTO.getDependencies();

				if (dependencyDTO.getDependencyType().equals(DependencyTypeProperty.ORDER_BEFORE.getProperty())) {

					if (!originalDependenciesString.equals(pluginName)) {
						String newDependenciesString = originalDependenciesString.replaceAll(orRegex,
								CustomWorkflowConstants.EMPTY_STRING).replaceAll(andRegex, CustomWorkflowConstants.EMPTY_STRING);
						dependencyDTO.setDependencies(newDependenciesString);
						if (newDependenciesString.isEmpty()) {
							pluginDetailsDTO.setDirty(true);
							dependencyDTO.setDeleted(true);
						}
						if (!originalDependenciesString.equals(newDependenciesString)) {
							pluginDetailsDTO.setDirty(true);
							dependencyDTO.setDirty(true);
						}
					} else {
						pluginDetailsDTO.setDirty(true);
						dependencyDTO.setDependencies(CustomWorkflowConstants.EMPTY_STRING);
						dependencyDTO.setDeleted(true);
					}
				}
			}
		}
	}

	@Override
	public void onDoubleClickTable() {
		showDependenciesView();
	}
}
