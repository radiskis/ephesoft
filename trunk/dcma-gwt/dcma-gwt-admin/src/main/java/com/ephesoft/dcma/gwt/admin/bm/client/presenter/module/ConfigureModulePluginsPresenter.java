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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.ephesoft.dcma.da.property.DependencyTypeProperty;
import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.MessageConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.module.ConfigureModulesPluginSelectView;
import com.ephesoft.dcma.gwt.core.client.EphesoftAsyncCallback;
import com.ephesoft.dcma.gwt.core.client.event.ItemsAddedEvent;
import com.ephesoft.dcma.gwt.core.client.event.ItemsAddedEventHandler;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.BatchClassDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassModuleDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.DependencyDTO;
import com.ephesoft.dcma.gwt.core.shared.PluginDetailsDTO;
import com.ephesoft.dcma.gwt.core.shared.listener.ThirdButtonListener;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.ListBox;

/**
 * Presenter for configuring module plugins.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class ConfigureModulePluginsPresenter extends AbstractBatchClassPresenter<ConfigureModulesPluginSelectView> {

	/**
	 * newIdentifier int.
	 */
	private static int newIdentifier = 1;

	/**
	 * selectedPluginDependenciesIndexSet Set<Integer>.
	 */
	private Set<Integer> selectedPluginDependenciesIndexSet;

	/**
	 * eventBus HandlerManager.
	 */
	private HandlerManager eventBus;

	/**
	 * Constructor.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view ConfigureModulesPluginSelectView
	 */
	public ConfigureModulePluginsPresenter(BatchClassManagementController controller, ConfigureModulesPluginSelectView view) {
		super(controller, view);
		this.eventBus = controller.getEventBus();
	}

	/**
	 * To handle events.
	 * 
	 * @param eventBus HandlerManager
	 */
	@Override
	public void injectEvents(HandlerManager eventBus) {
		// Inject all events here
		eventBus.addHandler(ItemsAddedEvent.type, new ItemsAddedEventHandler() {

			@Override
			public void onItemsAdded(ItemsAddedEvent event) {
				Set<Integer> dependenciesIndexList = getSelectedPluginDependenciesIndex();
				if (dependenciesIndexList != null) {
					setSelectedPluginDependenciesIndexSet(dependenciesIndexList);
					if (selectedPluginDependenciesIndexSet.size() > 0) {
						final ConfirmationDialog confirmationDialog = new ConfirmationDialog(true);
						confirmationDialog.setText(MessageConstants.WARNING);
						confirmationDialog.setMessage(MessageConstants.HIGHLIGHT_DEPENDENCY_ADD_MESSAGE);
						confirmationDialog.okButton.setText(AdminConstants.STRING_YES);
						confirmationDialog.cancelButton.setText(AdminConstants.STRING_NO);
						confirmationDialog.thirdButton.setText(AdminConstants.CANCEL_BUTTON);
						confirmationDialog.addDialogListener(new ThirdButtonListener() {

							@Override
							public void onOkClick() {
								// also shift the dependencies to right
								moveDependenciesAlongWithplugin();
								addSelectedPlugin();
								removeHighlightedDependencies(view.getMultipleSelectTwoSidedListBox().getLeftHandSideListBox(),
										selectedPluginDependenciesIndexSet);
								selectedPluginDependenciesIndexSet.clear();
							}

							@Override
							public void onCancelClick() {
								addSelectedPlugin();
							}

							@Override
							public void onThirdButtonClick() {
								confirmationDialog.hide();

							}
						});

						confirmationDialog.center();
						confirmationDialog.show();
						confirmationDialog.okButton.setFocus(true);

					} else {
						addSelectedPlugin();
					}
				}
			}
		});

	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {

		if (controller.getPluginIdentifierToNameMap() != null) {

			ScreenMaskUtility.maskScreen(MessageConstants.LOADING_PLUGINS);

			if (controller.getAllPluginDetailsDTOs() == null || controller.getAllPluginDetailsDTOs().isEmpty()) {
				getPluginDTOs();
			} else if (!controller.getBatchClass().isDirty()) {
				getPluginDTOs();
			} else {
				populateAndShowPluginView(controller.getAllPluginDetailsDTOs());
			}
		}
	}

	private void addSelectedPlugin() {
		int selectedIndex = view.getMultipleSelectTwoSidedListBox().getLeftHandSideListBox().getSelectedIndex();
		if (selectedIndex != -1) {
			String selectedValue = view.getMultipleSelectTwoSidedListBox().getLeftHandSideListBox().getItemText(selectedIndex);
			String selectedValueKey = BatchClassManagementConstants.EMPTY_STRING;
			ListBox toList = view.getMultipleSelectTwoSidedListBox().getRightHandSideListBox();
			Map<String, String> pluginIdentifierToNameMap = view.getMultipleSelectTwoSidedListBox().getAllValuesMapFromList(toList);
			do {

				selectedValueKey = BatchClassManagementConstants.NEW + newIdentifier++;
			} while (pluginIdentifierToNameMap.keySet().contains(selectedValueKey));
			toList.addItem(selectedValue, selectedValueKey);
			int newIndex = toList.getItemCount() - 1;
			toList.getElement().getElementsByTagName("option").getItem(newIndex).setTitle(selectedValue);
		}
	}

	private void getPluginDTOs() {
		controller.getRpcService().getAllPluginDetailDTOs(new EphesoftAsyncCallback<List<PluginDetailsDTO>>() {

			@Override
			public void customFailure(Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialogError("Unable to get the list of plugins. Check Logs for details.");
			}

			@Override
			public void onSuccess(List<PluginDetailsDTO> pluginDetailsDTOs) {
				getController().getMainPresenter().sortPluginDetailsDTOList(pluginDetailsDTOs, true);
				controller.setAllPluginDetailsDTOs(pluginDetailsDTOs);
				populateAndShowPluginView(pluginDetailsDTOs);
			}
		});
	}

	private void populateAndShowPluginView(List<PluginDetailsDTO> pluginDTOs) {
		controller.setAllPluginDetailsDTOs(pluginDTOs);
		List<String> pluginNames = new ArrayList<String>(0);
		for (PluginDetailsDTO pluginDetailsDTO : pluginDTOs) {
			pluginNames.add(pluginDetailsDTO.getPluginName());
		}
		view.getMultipleSelectTwoSidedListBox().getLeftHandSideListBox().clear();

		view.getMultipleSelectTwoSidedListBox().populateListBoxWithValues(
				view.getMultipleSelectTwoSidedListBox().getLeftHandSideListBox(), pluginNames);

		view.getMultipleSelectTwoSidedListBox().getRightHandSideListBox().clear();
		view.getMultipleSelectTwoSidedListBox().populateListBoxWithMap(
				view.getMultipleSelectTwoSidedListBox().getRightHandSideListBox(), controller.getPluginIdentifierToNameMap());
		ScreenMaskUtility.unmaskScreen();
	}

	/**
	 * To set Batch Class DTO Plugins List.
	 * 
	 * @param pluginIdentifierToNameMap Map<String, String>
	 */
	public void setBatchClassDTOPluginsList(Map<String, String> pluginIdentifierToNameMap) {
		// Clear current view dependencies list
		if (selectedPluginDependenciesIndexSet != null) {
			selectedPluginDependenciesIndexSet.clear();
		}
		ScreenMaskUtility.maskScreen(MessageConstants.UPDATING_PLUGINS_LIST);
		controller.setPluginIdentifierToNameMap(pluginIdentifierToNameMap);
		BatchClassDTO batchClass = controller.getBatchClass();
		final BatchClassDTO batchClassDTO = batchClass;
		batchClassDTO.setDirty(true);
		// Remove plug-ins
		BatchClassModuleDTO selectedModule = controller.getSelectedModule();
		String identifier = selectedModule.getIdentifier();
		BatchClassModuleDTO batchClassModuleDTO = batchClass.getModuleByIdentifier(identifier);
		Collection<BatchClassPluginDTO> batchClassPlugins = batchClassModuleDTO.getBatchClassPlugins();
		List<String> alreadyExistingPluginIdentifiers = new ArrayList<String>();
		for (BatchClassPluginDTO batchClassPluginDTO : batchClassPlugins) {

			String batchClassPluginIdentifier = batchClassPluginDTO.getIdentifier();
			if (!pluginIdentifierToNameMap.keySet().contains(batchClassPluginIdentifier)) {
				batchClassPluginDTO.setDeleted(true);
			} else {
				if (alreadyExistingPluginIdentifiers == null) {
					alreadyExistingPluginIdentifiers = new ArrayList<String>();
				}
				alreadyExistingPluginIdentifiers.add(batchClassPluginIdentifier);
			}
		}

		for (final Entry<String, String> pluginIdentifierToName : pluginIdentifierToNameMap.entrySet()) {
			String key = pluginIdentifierToName.getKey();
			if (!alreadyExistingPluginIdentifiers.contains(key)) {
				BatchClassPluginDTO batchClassPluginDTO = new BatchClassPluginDTO();
				for (PluginDetailsDTO pluginDetailsDTO : controller.getAllPluginDetailsDTOs()) {

					String value = pluginIdentifierToName.getValue();
					if (pluginDetailsDTO.getPluginName().equals(value)) {
						batchClassPluginDTO.setPlugin(pluginDetailsDTO);
						batchClassPluginDTO.setBatchClassModule(selectedModule);
						batchClassPluginDTO.setNew(true);
						batchClassPluginDTO.setIdentifier(key);
						batchClassModuleDTO.addBatchClassPlugin(batchClassPluginDTO);
						break;
					}
				}
			}

		}

		orderPluginsInBatchClass(new ArrayList<String>(pluginIdentifierToNameMap.keySet()));

	}

	private void orderPluginsInBatchClass(List<String> pluginList) {

		// Change OrderNumber of plugins

		String batchClassModuleIdentifier = controller.getSelectedModule().getIdentifier();
		Collection<BatchClassPluginDTO> batchClassPluginsList = controller.getBatchClass().getModuleByIdentifier(
				batchClassModuleIdentifier).getBatchClassPlugins();
		for (BatchClassPluginDTO batchClassPlugin : batchClassPluginsList) {
			if (!batchClassPlugin.isDeleted()) {
				int order = AdminConstants.INITIAL_ORDER_NUMBER;
				int index = pluginList.indexOf(batchClassPlugin.getIdentifier());
				order = (index * AdminConstants.ORDER_NUMBER_OFFSET) + AdminConstants.INITIAL_ORDER_NUMBER;
				batchClassPlugin.setOrderNumber(order);
			}

		}

		editAndShowModuleView();
	}

	private void editAndShowModuleView() {
		controller.getMainPresenter().showModuleView(
				controller.getBatchClass().getModuleByIdentifier(controller.getSelectedModule().getIdentifier()));
		ConfirmationDialogUtil.showConfirmationDialogSuccess(MessageConstants.PLUGINS_LIST_UPDATED_SUCCESSFULLY);
		ScreenMaskUtility.unmaskScreen();

	}

	private PluginDetailsDTO getPluginForPluginName(List<PluginDetailsDTO> allPluginDetailsDTOs, String pluginName) {
		PluginDetailsDTO pluginDetailsDTO = new PluginDetailsDTO();
		for (PluginDetailsDTO pluginDTO : allPluginDetailsDTOs) {
			if (pluginDTO.getPluginName().equals(pluginName)) {
				pluginDetailsDTO = pluginDTO;
				break;
			}
		}
		return pluginDetailsDTO;
	}

	/**
	 * To sort plugin list.
	 * 
	 * @param pluginsList List<BatchClassPluginDTO>
	 */
	public void sortPluginList(List<BatchClassPluginDTO> pluginsList) {
		Collections.sort(pluginsList, new Comparator<BatchClassPluginDTO>() {

			@Override
			public int compare(BatchClassPluginDTO batchClassPluginDTO1, BatchClassPluginDTO batchClassPluginDTO2) {
				int result;
				Integer orderNumberOne = batchClassPluginDTO1.getOrderNumber();
				Integer orderNumberTwo = batchClassPluginDTO2.getOrderNumber();
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
	 * To perform operations in case of plugin select.
	 */
	public void onPluginSelect() {

		// Find new Dependencies
		Set<Integer> dependenciesIndexList = getSelectedPluginDependenciesIndex();
		setSelectedPluginDependenciesIndexSet(dependenciesIndexList);
	}

	private Set<Integer> getSelectedPluginDependenciesIndex() {
		Set<Integer> dependenciesIndexList = null;
		// remove already highlighted dependencies
		removeHighlightedDependencies(view.getMultipleSelectTwoSidedListBox().getLeftHandSideListBox(),
				selectedPluginDependenciesIndexSet);

		// list.getElement().getElementsByTagName("option").getItem(itemNumber).setAttribute("background", "disabled");
		Map<String, String> orderedPluginNames = getPluginIndexToNameMap();

		int selectedIndex = view.getMultipleSelectTwoSidedListBox().getLeftHandSideListBox().getSelectedIndex();
		if (selectedIndex != -1) {
			String selectedPluginName = BatchClassManagementConstants.EMPTY_STRING;
			// make sure only this is the selected value
			view.getMultipleSelectTwoSidedListBox().getLeftHandSideListBox().setSelectedIndex(selectedIndex);
			// Get plugin name
			selectedPluginName = view.getMultipleSelectTwoSidedListBox().getLeftHandSideListBox().getItemText(selectedIndex);
			Set<String> dependenciesNameList = new HashSet<String>(0);
			// Get list of dependencies
			if (getPluginForPluginName(controller.getAllPluginDetailsDTOs(), selectedPluginName).getDependencies() != null) {
				for (DependencyDTO dependencyDTO : getPluginForPluginName(controller.getAllPluginDetailsDTOs(), selectedPluginName)
						.getDependencies()) {
					if (dependencyDTO.getDependencyType().equals(DependencyTypeProperty.ORDER_BEFORE.getProperty())) {
						dependenciesNameList.addAll(getDependenciesSet(dependencyDTO.getDependencies()));
					}
				}
			}
			// Get indexes of Dependencies if not in the above
			dependenciesIndexList = new HashSet<Integer>(0);
			for (String dependencyName : dependenciesNameList) {
				if (!orderedPluginNames.values().contains(dependencyName)) {
					dependenciesIndexList.add(getIndexForValueFromList(view.getMultipleSelectTwoSidedListBox()
							.getLeftHandSideListBox(), dependencyName));
				}
			}
		}
		return dependenciesIndexList;
	}

	private void removeHighlightedDependencies(ListBox listBox, Set<Integer> selectedPluginDependenciesIndex) {
		if (selectedPluginDependenciesIndex != null) {
			for (Integer index : selectedPluginDependenciesIndex) {
				listBox.getElement().getElementsByTagName(BatchClassManagementConstants.OPTION).getItem(index).removeClassName(
						BatchClassManagementConstants.DEPENDENCY_COLOR);
			}
		}
	}

	private Integer getIndexForValueFromList(ListBox listBox, String dependencyName) {
		int valueIndex = -1;

		for (int index = 0; index < listBox.getItemCount(); index++) {
			if (listBox.getItemText(index).equals(dependencyName)) {
				valueIndex = index;
				listBox.getElement().getElementsByTagName(BatchClassManagementConstants.OPTION).getItem(valueIndex).addClassName(
						BatchClassManagementConstants.DEPENDENCY_COLOR);
				break;
			}
		}
		return valueIndex;
	}

	private Set<String> getDependenciesSet(String dependencies) {
		Set<String> dependenciesSet = new HashSet<String>(0);
		String[] andDependencies = dependencies.split(AdminConstants.SEPERATOR);
		for (String andDependency : andDependencies) {
			String[] orDependencies = andDependency.split(AdminConstants.SLASH);
			for (String dependencyName : orDependencies) {
				dependenciesSet.add(dependencyName);
			}
		}
		return dependenciesSet;
	}

	private Map<String, String> getPluginIndexToNameMap() {
		List<String> selectedModulePluginNames = view.getMultipleSelectTwoSidedListBox().getAllValuesFromList(
				view.getMultipleSelectTwoSidedListBox().getRightHandSideListBox());
		Map<String, String> orderedPluginNames = new HashMap<String, String>(0);
		int index = 0;
		for (String pluginName : selectedModulePluginNames) {
			orderedPluginNames.put(String.valueOf(index++), pluginName);
		}

		return orderedPluginNames;
	}

	/**
	 * To get Event Bus.
	 * 
	 * @return the eventBus
	 */
	public HandlerManager getEventBus() {
		return eventBus;
	}

	/**
	 * To set Event Bus.
	 * 
	 * @param eventBus HandlerManager
	 */
	public void setEventBus(HandlerManager eventBus) {
		this.eventBus = eventBus;
	}

	/**
	 * To get Selected Plugin Dependencies IndexSet.
	 * 
	 * @return the selectedPluginDependenciesIndexSet
	 */
	public Set<Integer> getSelectedPluginDependenciesIndexSet() {
		return selectedPluginDependenciesIndexSet;
	}

	/**
	 * To set Selected Plugin Dependencies IndexSet.
	 * 
	 * @param selectedPluginDependenciesIndexSet Set<Integer>
	 */
	public void setSelectedPluginDependenciesIndexSet(Set<Integer> selectedPluginDependenciesIndexSet) {
		this.selectedPluginDependenciesIndexSet = selectedPluginDependenciesIndexSet;
	}

	private void moveDependenciesAlongWithplugin() {
		view.getMultipleSelectTwoSidedListBox().moveValuesOnIndexFromOneListToAnother(getSelectedPluginDependenciesIndexSet(),
				view.getMultipleSelectTwoSidedListBox().getLeftHandSideListBox(),
				view.getMultipleSelectTwoSidedListBox().getRightHandSideListBox());
	}

	/**
	 * To show module view on cancel button clicked.
	 */
	public void onCancelButtonClicked() {
		controller.getBatchClassManagementPresenter().showModuleView();
	}

}
