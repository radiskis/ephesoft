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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.MessageConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.module.ConfigureModuleView;
import com.ephesoft.dcma.gwt.core.client.event.ModuleItemsAddedEvent;
import com.ephesoft.dcma.gwt.core.client.event.ModuleItemsAddedEventHandler;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.BatchClassDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassModuleDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.ModuleDTO;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.ListBox;

/**
 * Presenter for configuring module.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class ConfigureModulePresenter extends AbstractBatchClassPresenter<ConfigureModuleView> {

	/**
	 * newIdentifier int.
	 */
	private static int newIdentifier = 1;

	/**
	 * eventBus HandlerManager.
	 */
	private HandlerManager eventBus;

	/**
	 * Constructor.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view ConfigureModuleView
	 */
	public ConfigureModulePresenter(BatchClassManagementController controller, ConfigureModuleView view) {
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
		eventBus.addHandler(ModuleItemsAddedEvent.type, new ModuleItemsAddedEventHandler() {

			@Override
			public void onItemsAdded(ModuleItemsAddedEvent event) {
				addSelectedModule();
			}
		});

	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {

		if (controller.getAllModules() != null) {

			populateAndShowAddModuleView();
		}
	}

	private void addSelectedModule() {
		ListBox fromList = view.getMultipleSelectTwoSidedListBox().getLeftHandSideListBox();
		List<Integer> selectedIndexes = view.getMultipleSelectTwoSidedListBox().getAllSelectedIndexes(fromList);

		for (Integer selectedIndex : selectedIndexes) {
			if (selectedIndex != -1) {
				String selectedValue = fromList.getItemText(selectedIndex);
				String selectedValueKey = BatchClassManagementConstants.EMPTY_STRING;
				ListBox toList = view.getMultipleSelectTwoSidedListBox().getRightHandSideListBox();
				Map<String, String> moduleIdentifierToNameMap = view.getMultipleSelectTwoSidedListBox()
						.getAllValuesMapFromList(toList);
				do {
					selectedValueKey = BatchClassManagementConstants.NEW + newIdentifier++;
				} while (moduleIdentifierToNameMap.containsKey(selectedValueKey));
				toList.addItem(selectedValue, selectedValueKey);
				int newIndex = toList.getItemCount() - 1;
				toList.getElement().getElementsByTagName("option").getItem(newIndex).setTitle(selectedValue);
			}
		}
	}

	/**
	 * To populate and show add module view.
	 */
	public void populateAndShowAddModuleView() {
		view.getMultipleSelectTwoSidedListBox().getLeftHandSideListBox().clear();
		view.getMultipleSelectTwoSidedListBox().populateListBoxWithValues(
				view.getMultipleSelectTwoSidedListBox().getLeftHandSideListBox(), controller.getAllModules());

		view.getMultipleSelectTwoSidedListBox().populateListBoxWithValues(
				view.getMultipleSelectTwoSidedListBox().getRightHandSideListBox(), controller.getModulesList());

		ScreenMaskUtility.unmaskScreen();
	}

	/**
	 * To set Batch Class DTO Modules List.
	 * 
	 * @param batchClassModuleDTOs Map<String, String>
	 */
	public void setBatchClassDTOModulesList(Map<String, String> moduleIdentifierToNameMap) {

		ScreenMaskUtility.maskScreen("Saving modules");

		List<String> moduleNames = new ArrayList<String>(moduleIdentifierToNameMap.values());
		List<String> moduleIdentifier = new ArrayList<String>(moduleIdentifierToNameMap.keySet());
		controller.setModulesList(moduleNames);
		final BatchClassDTO batchClassDTO = controller.getBatchClass();
		batchClassDTO.setDirty(true);

		for (BatchClassModuleDTO batchClassModuleDTO : batchClassDTO.getModules()) {
			if (!moduleNames.contains(batchClassModuleDTO.getWorkflowName())) {
				batchClassModuleDTO.setDeleted(true);
			}
		}

		for (final Entry<String, String> moduleIdentifierToName : moduleIdentifierToNameMap.entrySet()) {
			String moduleIdentifer = moduleIdentifierToName.getKey();
			if (moduleIdentifer.contains(BatchClassManagementConstants.NEW)) {
				String moduleName = moduleIdentifierToName.getValue();
				ModuleDTO moduleDTO = controller.getModuleNameToDtoMap().get(moduleName);
				BatchClassModuleDTO newBatchClassModuleDTO = new BatchClassModuleDTO();
				newBatchClassModuleDTO.setModule(moduleDTO);
				newBatchClassModuleDTO.setNew(true);
				newBatchClassModuleDTO.setBatchClass(batchClassDTO);
				HashMap<String, BatchClassPluginDTO> pluginMap = new HashMap<String, BatchClassPluginDTO>();
				newBatchClassModuleDTO.setBatchClassPluginsMap(pluginMap);
				newBatchClassModuleDTO.setWorkflowName(moduleIdentifer);
				newBatchClassModuleDTO.setIdentifier(moduleIdentifer);
				batchClassDTO.addModule(newBatchClassModuleDTO);

			}
		}

		orderModulesInBatchClass(batchClassDTO, moduleIdentifier);

		controller.getMainPresenter().showBatchClassView(controller.getBatchClass());
		ConfirmationDialogUtil.showConfirmationDialogSuccess(MessageConstants.MODULES_LIST_UPDATED_SUCCESSFULLY);
		getController().getMainPresenter().getView().toggleDeployButtonEnable(false);
		ScreenMaskUtility.unmaskScreen();

	}

	private void orderModulesInBatchClass(BatchClassDTO batchClassDTO, List<String> modulesList) {

		// Change OrderNumber of modules
		List<String> newModulesWorflowNameList = new ArrayList<String>();
		for (BatchClassModuleDTO batchClassModuleDTO : batchClassDTO.getModules()) {
			if (!batchClassModuleDTO.isDeleted()) {
				int order = AdminConstants.INITIAL_ORDER_NUMBER;
				String workflowName = batchClassModuleDTO.getWorkflowName();
				int index = modulesList.indexOf(workflowName);

				order = (index * AdminConstants.ORDER_NUMBER_OFFSET) + AdminConstants.INITIAL_ORDER_NUMBER;
				if (batchClassModuleDTO.isNew() && workflowName.contains(BatchClassManagementConstants.NEW)) {
					validateBatchClassModuleDTO(batchClassDTO, modulesList, newModulesWorflowNameList, batchClassModuleDTO);
				}
				batchClassModuleDTO.setOrderNumber(order);
			}

		}

	}

	private void validateBatchClassModuleDTO(BatchClassDTO batchClassDTO, List<String> modulesList,
			List<String> newModulesWorflowNameList, BatchClassModuleDTO batchClassModuleDTO) {
		String batchClassIdentifier = batchClassDTO.getIdentifier();
		StringBuffer workflowNameStringBuffer = new StringBuffer();
		String moduleName = batchClassModuleDTO.getModule().getName();
		workflowNameStringBuffer.append(moduleName.replaceAll(BatchClassManagementConstants.SPACE,
				BatchClassManagementConstants.UNDERSCORE));
		workflowNameStringBuffer.append(BatchClassManagementConstants.UNDERSCORE);
		workflowNameStringBuffer.append(batchClassIdentifier);
		int instance = 0;
		String newWorkflowName = workflowNameStringBuffer.toString();
		if (modulesList.contains(newWorkflowName) || newModulesWorflowNameList.contains(newWorkflowName)) {
			do {
				instance++;
			} while (modulesList.contains(newWorkflowName + BatchClassManagementConstants.UNDERSCORE + instance));
			workflowNameStringBuffer.append(BatchClassManagementConstants.UNDERSCORE);
			workflowNameStringBuffer.append(instance);
		}
		newWorkflowName = workflowNameStringBuffer.toString();
		newModulesWorflowNameList.add(newWorkflowName);
		batchClassModuleDTO.setWorkflowName(newWorkflowName);
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
	 * To add new module.
	 */
	public void addNewModule() {
		controller.getBatchClassManagementPresenter().showAddNewModuleView();
	}

	/**
	 * To perform operations on cancel button click.
	 */
	public void onCancelButtonClicked() {
		controller.getBatchClassManagementPresenter().showBatchClassView();
	}

}
