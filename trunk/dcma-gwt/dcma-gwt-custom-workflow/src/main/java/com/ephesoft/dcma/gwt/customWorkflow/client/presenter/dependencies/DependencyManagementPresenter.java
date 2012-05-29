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

package com.ephesoft.dcma.gwt.customWorkflow.client.presenter.dependencies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ephesoft.dcma.da.property.DependencyTypeProperty;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.DependencyDTO;
import com.ephesoft.dcma.gwt.core.shared.PluginDetailsDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.ephesoft.dcma.gwt.customWorkflow.client.CustomWorkflowController;
import com.ephesoft.dcma.gwt.customWorkflow.client.i18n.CustomWorkflowMessages;
import com.ephesoft.dcma.gwt.customWorkflow.client.presenter.AbstractCustomWorkflowPresenter;
import com.ephesoft.dcma.gwt.customWorkflow.client.presenter.CustomWorkflowBreadCrumbPresenter;
import com.ephesoft.dcma.gwt.customWorkflow.client.view.dependencies.DependencyManagementView;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;

public class DependencyManagementPresenter extends AbstractCustomWorkflowPresenter<DependencyManagementView> {

	private static final String NO = "No";

	private static final String YES = "Yes";

	private final DependencyPresenter dependencyPresenter;

	private final EditDependencyPresenter editDependencyPresenter;
	private final CustomWorkflowBreadCrumbPresenter breadCrumbPresenter;

	List<String> dependenciesListBuffer = new ArrayList<String>();

	/**
	 * @return the dependencyPresenter
	 */
	public DependencyPresenter getDependencyPresenter() {
		return dependencyPresenter;
	}

	/**
	 * @return the breadCrumbPresenter
	 */
	public CustomWorkflowBreadCrumbPresenter getBreadCrumbPresenter() {
		return breadCrumbPresenter;
	}

	public final static String AND = ",";

	public final static String OR = "/";

	public boolean isCyclic = false;

	public boolean isValidDependency = true;

	boolean isDuplicate = false;

	boolean isValidEntry = true;

	public String selectedDependencyName;

	public String conflictingDependencyName;

	;

	public DependencyManagementPresenter(CustomWorkflowController controller, DependencyManagementView view) {
		super(controller, view);
		this.dependencyPresenter = new DependencyPresenter(controller, view.getDependencyView());
		this.editDependencyPresenter = new EditDependencyPresenter(controller, view.getEditDependencyView());
		this.breadCrumbPresenter = new CustomWorkflowBreadCrumbPresenter(controller, view.getWorkflowBreadCrumbView());
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {

	}

	@Override
	public void bind() {

		if (controller.getAllPlugins() != null) {
			Map<Integer, String> pluginIndexToNameMap = controller.getPluginIndexToNameMap();
			// populate plugin names
			if (pluginIndexToNameMap == null) {
				pluginIndexToNameMap = new HashMap<Integer, String>(0);
			} else {
				pluginIndexToNameMap.clear();
			}
			int index = 0;
			for (PluginDetailsDTO pluginDetailsDTO : controller.getAllPlugins()) {
				pluginIndexToNameMap.put(index++, pluginDetailsDTO.getPluginName());
			}
			controller.setPluginIndexToNameMap(pluginIndexToNameMap);
			view.populateListBoxWithValuesMap(view.getDependencyView().getPluginNames(), pluginIndexToNameMap);

			// populate dependencies of selected plugin
			getDependenciesList();
		}

	}

	/**
	 * 
	 */
	private void getDependenciesList() {
		int selectedIndex = Integer.parseInt(view.getDependencyView().getPluginNames().getValue(
				view.getDependencyView().getPluginNames().getSelectedIndex()));
		getView().createDependenciesList(controller.getAllPlugins().get(selectedIndex).getDependencies());
	}

	public void onSaveClicked() {
		ScreenMaskUtility.maskScreen("Updating plugins list");
		controller.getRpcService().updateAllPluginDetailsDTOs(controller.getAllPlugins(), new AsyncCallback<List<PluginDetailsDTO>>() {

			@Override
			public void onFailure(Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialogError("Error updating plugins");
				ScreenMaskUtility.unmaskScreen();
			}

			@Override
			public void onSuccess(List<PluginDetailsDTO> pluginDetailsDTOs) {
				controller.setAllPlugins(pluginDetailsDTOs);
				ConfirmationDialogUtil.showConfirmationDialogSuccess(LocaleDictionary.get().getMessageValue(
						CustomWorkflowMessages.PLUGINS_UPDATED_SUCCESSFULLY));
				ScreenMaskUtility.unmaskScreen();
				Window.Location.reload();
			}
		});
	}

	public void onApplyClicked() {
		ScreenMaskUtility.maskScreen("Updating plugins list");
		controller.getRpcService().updateAllPluginDetailsDTOs(controller.getAllPlugins(), new AsyncCallback<List<PluginDetailsDTO>>() {

			@Override
			public void onFailure(Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialogError("Error updating plugins");
				ScreenMaskUtility.unmaskScreen();
			}

			@Override
			public void onSuccess(List<PluginDetailsDTO> pluginDetailsDTOs) {
				controller.setAllPlugins(pluginDetailsDTOs);
				String selectedPlugin = getSelectedValueFromList(view.getDependencyView().getPluginNames());
				controller.setSelectedPlugin(selectedPlugin);
				showDependenciesView();
				ScreenMaskUtility.unmaskScreen();
				ConfirmationDialogUtil.showConfirmationDialogSuccess(LocaleDictionary.get().getMessageValue(
						CustomWorkflowMessages.PLUGINS_UPDATED_SUCCESSFULLY));
			}
		});
	}

	public void onCancelClicked() {
		ScreenMaskUtility.maskScreen();
		controller.getRpcService().getAllPluginDetailDTOs(new AsyncCallback<List<PluginDetailsDTO>>() {

			@Override
			public void onFailure(Throwable arg0) {
				ScreenMaskUtility.unmaskScreen();
			}

			@Override
			public void onSuccess(List<PluginDetailsDTO> pluginDetailsDTOs) {
				controller.setAllPlugins(pluginDetailsDTOs);
				Window.Location.reload();
				ScreenMaskUtility.unmaskScreen();
			}

		});
	}

	/**
	 * @return the editDependencyPresenter
	 */
	public EditDependencyPresenter getEditDependencyPresenter() {
		return editDependencyPresenter;
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
			populateThePluginName(controller.getSelectedPlugin());
		}

	}

	/**
	 * @param pluginDetailsDTOs
	 */
	private void showDependenciesView(List<PluginDetailsDTO> pluginDetailsDTOs) {
		controller.setAllPlugins(pluginDetailsDTOs);
		breadCrumbPresenter.createBreadCrumbForDependenciesList();
		dependencyPresenter.bind();

		// getController().getCustomWorkflowManagementPresenter().getView().sho
		view.showDependencyView();
		breadCrumbPresenter.setbackButtonVisibility(true);
		ScreenMaskUtility.unmaskScreen();
	}

	private String getSelectedValueFromList(ListBox listBox) {
		String value = "";
		int selectedIndex = listBox.getSelectedIndex();
		if (selectedIndex != -1) {

			value = listBox.getItemText(selectedIndex);
		}
		return value;

	}

	public void updatePluginDependency() {
		validateCurrentDependencies();
		if (!isCyclic && isValidDependency && !isDuplicate && isValidEntry) {
			ScreenMaskUtility.maskScreen(LocaleDictionary.get().getMessageValue(CustomWorkflowMessages.UPDATING_DEPENDENCIES_LIST));
			PluginDetailsDTO pluginDetailsDTO = getPluginForName(controller.getAllPlugins(), getEditDependencyPresenter().getView()
					.getPluginNamesList().getText());
			pluginDetailsDTO.setDirty(true);
			DependencyDTO dependencyDTO = createDependencyDTO();
			if (dependencyDTO.getDependencyType().equals(DependencyTypeProperty.UNIQUE.getProperty())) {
				dependencyDTO.setDependencies("");
			}
			if (pluginDetailsDTO.getDependencies() == null) {
				pluginDetailsDTO.setDependencies(new ArrayList<DependencyDTO>(0));
				pluginDetailsDTO.getDependencies().clear();
			}
			if (controller.getSelectedDependencyDTO() == null) {
				dependencyDTO.setIdentifier("0");
				dependencyDTO.setPluginDTO(pluginDetailsDTO);
				dependencyDTO.setNew(true);
				controller.setSelectedDependencyDTO(dependencyDTO);
				pluginDetailsDTO.getDependencies().add(controller.getSelectedDependencyDTO());
			} else {
				for (DependencyDTO dependency : pluginDetailsDTO.getDependencies()) {
					if (dependency.getIdentifier().equals(controller.getSelectedDependencyDTO().getIdentifier())) {
						dependency.setDependencyType(dependencyDTO.getDependencyType());
						dependency.setDependencies(dependencyDTO.getDependencies());
						dependency.setDirty(true);
						dependencyDTO = dependency;
						break;
					}
				}
			}
			ConfirmationDialogUtil.showConfirmationDialogSuccess(LocaleDictionary.get().getMessageValue(
					CustomWorkflowMessages.DEPENDENCIES_LIST_UPDATED_SUCCESSFULLY));
			ScreenMaskUtility.unmaskScreen();
			showDependenciesView(controller.getAllPlugins());
			populateThePluginName(dependencyDTO.getPluginDTO().getPluginName());
		}

	}

	/**
	 * @param dependencyDTO
	 */
	private void populateThePluginName(String selectedPluginName) {
		ListBox pluginNamesListBox = view.getDependencyView().getPluginNames();
		int index = getIndexForValue(pluginNamesListBox, selectedPluginName);
		pluginNamesListBox.setSelectedIndex(index);

		PluginDetailsDTO pluginDTO = getPluginForName(controller.getAllPlugins(), selectedPluginName);
		// Create the dependencies list for it
		getView().createDependenciesList(pluginDTO.getDependencies());
	}

	public void updateAllPlugins(List<PluginDetailsDTO> allPluginDetailsDTOs) {

		controller.getRpcService().updateAllPluginDetailsDTOs(allPluginDetailsDTOs, new AsyncCallback<List<PluginDetailsDTO>>() {

			@Override
			public void onFailure(Throwable arg0) {

			}

			@Override
			public void onSuccess(List<PluginDetailsDTO> pluginDetailsDTOs) {
				controller.setAllPlugins(pluginDetailsDTOs);
			}
		});
	}

	private DependencyDTO createDependencyDTO() {
		DependencyDTO dependencyDTO = new DependencyDTO();

		dependencyDTO.setDependencyType(getSelectedValueFromList(getEditDependencyPresenter().getView().getDependencyTypeList()));

		dependencyDTO.setDependencies((getEditDependencyPresenter().getView().getDependenciesTextArea().getText()));

		return dependencyDTO;
	}

	/**
	 * 
	 */
	private void populateEditDependencyView() {

		List<String> dependenciesType = new ArrayList<String>(0);

		for (DependencyTypeProperty dependencyTypeProperty : DependencyTypeProperty.valuesAsList()) {
			dependenciesType.add(dependencyTypeProperty.getProperty());
		}

		controller.setDependenciesType(dependenciesType);

		getEditDependencyPresenter().bind();
	}

	public void showAddDependenciesView() {

		breadCrumbPresenter.createBreadCrumbForDependenciesAddView(LocaleDictionary.get().getMessageValue(
				CustomWorkflowMessages.ADD_DEPENDENCY));

		controller.setSelectedDependencyDTO(null);
		// disableUneditableFields(true);
		populateEditDependencyView();
		addValueForPluginNameForAdd(controller.getSelectedDependencyDTO());
		getEditDependencyPresenter().setAvailableDependenciesList();
		view.showEditDependencyView();
	}

	public void showEditDependenciesView() {
		String identifier = dependencyPresenter.getDependencyListPresenter().getView().listView.getSelectedRowIndex();
		if (identifier != null) {
			breadCrumbPresenter.createBreadCrumbForDependenciesAddView(LocaleDictionary.get().getMessageValue(
					CustomWorkflowMessages.EDIT_DEPENDENCY));
			populateEditDependencyView();
			// Get selectedDependency
			setSelectedDependency();
			setSelectedValuesInEditView(controller.getSelectedDependencyDTO());
			// Disable the uneditable items
			view.showEditDependencyView();
		} else {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					CustomWorkflowMessages.NO_RECORD_TO_EDIT));
		}
	}

	/**
	 * 
	 */
	private void setSelectedDependency() {
		String dependencyIdentifier = dependencyPresenter.getDependencyListPresenter().getView().listView.getSelectedRowIndex();
		int selectedIndex = Integer.parseInt(dependencyPresenter.getView().getPluginNames().getValue(
				dependencyPresenter.getView().getPluginNames().getSelectedIndex()));

		PluginDetailsDTO pluginDetailsDTO = controller.getAllPlugins().get(selectedIndex);
		pluginDetailsDTO.getDependencyDTOByIdentifier(pluginDetailsDTO, dependencyIdentifier);

		controller.setSelectedDependencyDTO(pluginDetailsDTO.getDependencyDTOByIdentifier(pluginDetailsDTO, dependencyIdentifier));
	}

	public void setSelectedValuesInEditView(DependencyDTO selectedDependencyDTO) {
		// Populate Plugin name
		addValueForPluginName(selectedDependencyDTO);

		// Populate Dependency Type
		String dependencyType = selectedDependencyDTO.getDependencyType();
		int dependencyTypeIndex = getIndexForValue(getEditDependencyPresenter().getView().getDependencyTypeList(), dependencyType);
		getEditDependencyPresenter().getView().getDependencyTypeList().setSelectedIndex(dependencyTypeIndex);

		getEditDependencyPresenter().setAvailableDependenciesList();
		if (!dependencyType.equals(DependencyTypeProperty.UNIQUE.getProperty())) {
			// Get List of plugin names for dependencies from allPluginList
			String dependenciesNameString = (selectedDependencyDTO.getDependencies());
			getEditDependencyPresenter().getView().getDependenciesTextArea().setText(dependenciesNameString);
			enableItemsOnDependencyTypeChange(true);
		} else {
			// Disable the items
			getEditDependencyPresenter().getView().getDependenciesTextArea().setText("");
			enableItemsOnDependencyTypeChange(false);
		}
	}

	/**
	 * @param selectedDependencyDTO
	 */
	private void addValueForPluginName(DependencyDTO selectedDependencyDTO) {
		String pluginName = selectedDependencyDTO.getPluginDTO().getPluginName();
		controller.setSelectedPlugin(pluginName);
		getEditDependencyPresenter().getView().getPluginNamesList().setText(pluginName);
	}

	/**
	 * @param selectedDependencyDTO
	 */
	private void addValueForPluginNameForAdd(DependencyDTO selectedDependencyDTO) {
		ListBox pluginNamesList = dependencyPresenter.getView().getPluginNames();
		String pluginName = pluginNamesList.getItemText(pluginNamesList.getSelectedIndex());
		controller.setSelectedPlugin(pluginName);
		getEditDependencyPresenter().getView().getPluginNamesList().setText(pluginName);
	}

	private PluginDetailsDTO getPluginForName(List<PluginDetailsDTO> allPlugins, String dependencyName) {
		PluginDetailsDTO pluginDTO = new PluginDetailsDTO();
		for (PluginDetailsDTO pluginDetailsDTO : allPlugins) {
			if (pluginDetailsDTO.getPluginName().equals(dependencyName)) {
				pluginDTO = pluginDetailsDTO;
				break;
			}

		}

		return pluginDTO;
	}

	public void onDependencyTypeChange() {
		String dependencyType = getSelectedValueFromList(getEditDependencyPresenter().getView().getDependencyTypeList());
		if (dependencyType.equals(DependencyTypeProperty.UNIQUE.getProperty())) {
			enableItemsOnDependencyTypeChange(false);
			editDependencyPresenter.getView().getDependenciesTextArea().setText("");
			editDependencyPresenter.getView().getDependenciesList().setSelectedIndex(-1);
		} else {
			enableItemsOnDependencyTypeChange(true);
		}
	}

	/**
	 * 
	 */
	public void enableItemsOnDependencyTypeChange(boolean enable) {
		getEditDependencyPresenter().getView().getDependenciesList().setEnabled(enable);
		// dependencyPresenter.getEditDependencyPresenter().getView().getDependenciesTextArea().setEnabled(enable);
		getEditDependencyPresenter().getView().getAndButton().setEnabled(enable);
		getEditDependencyPresenter().getView().getOrButton().setEnabled(enable);
	}

	public PluginDetailsDTO getPluginDetailsDTOByPluginName(List<PluginDetailsDTO> pluginDetailsDTOs, String pluginName) {
		PluginDetailsDTO pluginDetailsDTO = null;

		for (PluginDetailsDTO pluginDetailsDTO2 : pluginDetailsDTOs) {

			if (pluginDetailsDTO2.getPluginName().equals(pluginName)) {
				pluginDetailsDTO = pluginDetailsDTO2;
				break;
			}
		}
		return pluginDetailsDTO;
	}

	private boolean checkForDuplicateDependenciesEntry(List<DependencyDTO> dependencyDTOs, String newDependenciesString) {

		isDuplicate = false;
		// Get list of and dependencies
		List<String> uniqueAndDependency = getAndDependenciesAsSet(newDependenciesString);

		for (DependencyDTO dependencyDTO : dependencyDTOs) {
			if (dependencyDTO.getDependencyType().equals(DependencyTypeProperty.ORDER_BEFORE.getProperty())) {
				String existingDependencies = dependencyDTO.getDependencies();

				List<String> existingUniqueAndDependency = getAndDependenciesAsSet(existingDependencies);
				for (String andDependency : uniqueAndDependency) {
					if (existingUniqueAndDependency.contains(andDependency)) {
						isDuplicate = true;
						ConfirmationDialogUtil.showConfirmationDialogError(andDependency
								+ " dependency already exists. Please try again.");
						break;
					}
				}
				if (isDuplicate) {
					break;
				}
			}

		}
		return isDuplicate;
	}

	private boolean checkInvalidDependencyEntry(String newDependenciesString) {
		isValidEntry = true;
		List<String> uniqueAndDependency = getAndDependenciesAsSet(newDependenciesString);

		Set<String> uniqueDependencySet = new HashSet<String>();

		for (String dependency : uniqueAndDependency) {

			if (!uniqueDependencySet.add(dependency)) {
				// dependency is duplicate
				isValidEntry = false;
				ConfirmationDialogUtil.showConfirmationDialogError("Selected dependencies contains duplicate entry for " + dependency
						+ " please try again.");
				break;
			}

		}

		return isValidEntry;
	}

	private String sortOrDependencies(String orDependenciesString) {
		List<String> sortedDependenciesString = new ArrayList<String>();
		String sortedOrDependenciesString = null;

		String[] orDependencies = orDependenciesString.split(OR);

		for (String dependency : orDependencies) {
			sortedDependenciesString.add(dependency);
		}
		Collections.sort(sortedDependenciesString);
		StringBuffer sortedDependenciesStringBuffer = new StringBuffer();
		// Prepare a new string for the sorted dependencies
		for (String dependency : sortedDependenciesString) {
			if (!sortedDependenciesStringBuffer.toString().isEmpty()) {
				sortedDependenciesStringBuffer.append(OR);
			}
			sortedDependenciesStringBuffer.append(dependency);
		}
		sortedOrDependenciesString = sortedDependenciesStringBuffer.toString();
		return sortedOrDependenciesString;
	}

	/**
	 * @param newDependenciesString
	 * @param uniqueAndDependency
	 */
	private List<String> getAndDependenciesAsSet(String newDependenciesString) {
		List<String> uniqueAndDependency = new ArrayList<String>(0);
		String[] andDependencies = newDependenciesString.split(AND);

		for (String andDependency : andDependencies) {
			String sortedOrDependencies = sortOrDependencies(andDependency);
			uniqueAndDependency.add(sortedOrDependencies);
		}
		return uniqueAndDependency;
	}

	public void validateCurrentDependencies() {
		String newDependencies = getEditDependencyPresenter().getView().getDependenciesTextArea().getText();
		selectedDependencyName = getEditDependencyPresenter().getView().getPluginNamesList().getText();

		dependenciesListBuffer.clear();
		dependenciesListBuffer.add(selectedDependencyName);
		if (getEditDependencyPresenter().getView().getDependenciesList().isEnabled() && !newDependencies.isEmpty()) {
			isCyclic = false;
			isValidDependency = true;
			// Check for duplicate entries
			// Get plugin DTO for selected plugin
			PluginDetailsDTO pluginDetailsDTO = getPluginDetailsDTOByPluginName(controller.getAllPlugins(), selectedDependencyName);

			// Check if this entry already exists in the PDDTO
			List<DependencyDTO> dependenciesList = new ArrayList<DependencyDTO>(pluginDetailsDTO.getDependencies());
			DependencyDTO selectedDependencyDTO = controller.getSelectedDependencyDTO();
			if (selectedDependencyDTO != null) {
				dependenciesList.remove(selectedDependencyDTO);
			}
			boolean isDuplicate = checkForDuplicateDependenciesEntry(dependenciesList, newDependencies);
			if (!isDuplicate) {
				boolean isValidEntry = checkInvalidDependencyEntry(newDependencies);
				if (isValidEntry) {
					checkCyclicDependencies(newDependencies, selectedDependencyName);
				}
			}
		} else if (!getEditDependencyPresenter().getView().getDependenciesList().isEnabled()) {
			isCyclic = false;
			isValidDependency = true;
			isDuplicate = false;
			isValidEntry = true;
//			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
//					CustomWorkflowMessages.DEPENDENCIES_SUCCESSFULLY_VALIDATED));
		} else {
			isValidDependency = false;
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					CustomWorkflowMessages.NO_DEPENDENCY_SELECTED));
		}

	}

	/**
	 * 
	 */
	public void toggleOkButtonEnable(boolean enable) {
		getEditDependencyPresenter().getView().getOkButton().setEnabled(enable);
	}

	private boolean checkCyclicDependencies(String newDependencies, String selectedDependency) {
		Set<String> uniqueDependency = new HashSet<String>(0);
		String[] andDependencies = newDependencies.split(AND);

		for (String andDependency : andDependencies) {

			String[] orDependencies = andDependency.split(OR);

			for (String dependencyName : orDependencies) {
				boolean validDependencyname = false;
				Collection<String> validDependenciesNamesList = controller.getPluginIndexToNameMap().values();
				validDependencyname = validDependenciesNamesList.contains(dependencyName);
				if (validDependencyname) {
					uniqueDependency.add(dependencyName);
					if (uniqueDependency.contains(selectedDependencyName)) {
						// cyclic
						if (isCyclic == true) {
							break;
						}
						ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
								CustomWorkflowMessages.DEPENDENCY_VIOLATED)
								+ selectedDependencyName
								+ LocaleDictionary.get().getMessageValue(CustomWorkflowMessages.PLUGIN_ALREADY_DEPENDENT)
								+ dependenciesListBuffer.get(1));
						isCyclic = true;
						break;
					} else {
						String nextDependency = getOrderBeforeDependenciesForPluginName(dependencyName);
						conflictingDependencyName = dependencyName;
						dependenciesListBuffer.add(conflictingDependencyName);
						if (!nextDependency.isEmpty()) {
							checkCyclicDependencies(nextDependency, dependencyName);
						} else {
							dependenciesListBuffer.remove(dependenciesListBuffer.size() - 1);
							continue;
						}
					}
				} else if (!isCyclic) {
					isValidDependency = false;
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							CustomWorkflowMessages.DEPENDENCY_VIOLATED)
							+ " Invalid dependency Name: " + dependencyName);
					break;
				}
			}
			if (isCyclic == true || !isValidDependency) {
				break;
			}

		}
		if (!(isCyclic || !isValidDependency) && (dependenciesListBuffer.contains(selectedDependency))) {
			dependenciesListBuffer.remove(selectedDependency);
		}
		return isCyclic || !isValidDependency;
	}

	private String getOrderBeforeDependenciesForPluginName(String pluginName) {

		StringBuffer dependenciesBuffer = new StringBuffer();
		List<DependencyDTO> dependencyDTOs = getPluginForName(controller.getAllPlugins(), pluginName).getDependencies();
		if (dependencyDTOs != null) {
			for (DependencyDTO dependencyDTO : dependencyDTOs) {
				if (dependencyDTO != null
						&& dependencyDTO.getDependencyType().equals(DependencyTypeProperty.ORDER_BEFORE.getProperty())) {
					String dependencies = "";
					dependencies = (dependencyDTO.getDependencies());
					if (!dependenciesBuffer.toString().isEmpty()) {
						dependenciesBuffer.append(AND);
					}
					dependenciesBuffer.append(dependencies);

				}
			}
		}
		return dependenciesBuffer.toString();
	}

	public void showDeleteDependenciesView() {

		String dependencyIdentifier = dependencyPresenter.getDependencyListPresenter().getView().listView.getSelectedRowIndex();

		if (dependencyIdentifier != null) {
			final ConfirmationDialog confirmationDialog = new ConfirmationDialog();
			confirmationDialog.setText(LocaleDictionary.get().getMessageValue(CustomWorkflowMessages.WARNING));
			confirmationDialog.setMessage(LocaleDictionary.get().getMessageValue(CustomWorkflowMessages.DELETE_DEPENDENCY));
			confirmationDialog.okButton.setText(YES);
			confirmationDialog.cancelButton.setText(NO);
			confirmationDialog.center();
			confirmationDialog.show();
			confirmationDialog.okButton.setFocus(true);
			confirmationDialog.addDialogListener(new DialogListener() {

				@Override
				public void onOkClick() {

					String dependencyIdentifier = dependencyPresenter.getDependencyListPresenter().getView().listView
							.getSelectedRowIndex();
					if (dependencyIdentifier != null) {
						int selectedIndex = Integer.parseInt(dependencyPresenter.getView().getPluginNames().getValue(
								dependencyPresenter.getView().getPluginNames().getSelectedIndex()));
						PluginDetailsDTO pluginDetailsDTO = controller.getAllPlugins().get(selectedIndex);
						DependencyDTO dependencyDTO = pluginDetailsDTO.getDependencyDTOByIdentifier(pluginDetailsDTO,
								dependencyIdentifier);
						pluginDetailsDTO.setDirty(true);
						dependencyDTO.setDeleted(true);

						// Update the view list
						getView().createDependenciesList(pluginDetailsDTO.getDependencies());
					}

				}

				@Override
				public void onCancelClick() {
					confirmationDialog.hide();
				}
			});
		} else {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					CustomWorkflowMessages.NO_RECORD_TO_DELETE));
		}

	}

	// private String getValueForSelectedIndex(ListBox fromList) {
	// String value = null;
	// if (fromList.getItemCount() > 0) {
	//
	// // populate all selected values from the list
	// for (int index = 0; index < fromList.getItemCount(); index++) {
	// if (fromList.isItemSelected(index)) {
	// value = fromList.getItemText(index);
	// break;
	// }
	// }
	// }
	//
	// return value;
	//
	// }

	private int getIndexForValue(ListBox fromList, String item) {
		int position = -1;
		if (fromList.getItemCount() > 0) {

			// populate all selected values from the list
			for (int index = 0; index < fromList.getItemCount(); index++) {
				if (fromList.getItemText(index).equals(item)) {
					position = index;
					break;
				}
			}
		}

		return position;
	}

	public void addToDependenciesList(String dependencyType) {
		StringBuffer selectedDependencies = new StringBuffer(getEditDependencyPresenter().getView().getDependenciesTextArea()
				.getText());

		if (!selectedDependencies.toString().isEmpty()) {
			if (dependencyType.equals(AND)) {
				selectedDependencies.append(AND);
			} else if (dependencyType.equals(OR)) {
				selectedDependencies.append(OR);
			}
		}
		selectedDependencies.append(getSelectedValueFromList(getEditDependencyPresenter().getView().getDependenciesList()));
		getEditDependencyPresenter().getView().getDependenciesTextArea().setText(selectedDependencies.toString());

		// toggleOkButtonEnable(false);
	}

}
