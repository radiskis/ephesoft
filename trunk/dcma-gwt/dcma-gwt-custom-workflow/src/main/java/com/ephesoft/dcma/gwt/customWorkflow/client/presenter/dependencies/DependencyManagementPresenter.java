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

package com.ephesoft.dcma.gwt.customworkflow.client.presenter.dependencies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ephesoft.dcma.da.property.DependencyTypeProperty;
import com.ephesoft.dcma.gwt.core.client.EphesoftAsyncCallback;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.DependencyDTO;
import com.ephesoft.dcma.gwt.core.shared.PluginDetailsDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.ephesoft.dcma.gwt.customworkflow.client.CustomWorkflowController;
import com.ephesoft.dcma.gwt.customworkflow.client.i18n.CustomWorkflowConstants;
import com.ephesoft.dcma.gwt.customworkflow.client.i18n.CustomWorkflowMessages;
import com.ephesoft.dcma.gwt.customworkflow.client.presenter.AbstractCustomWorkflowPresenter;
import com.ephesoft.dcma.gwt.customworkflow.client.presenter.CustomWorkflowBreadCrumbPresenter;
import com.ephesoft.dcma.gwt.customworkflow.client.view.dependencies.DependencyManagementView;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;

/**
 * The <code>DependencyManagementPresenter</code> class provides functionality for showing, adding, validating
 * dependencies for different plugins.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.customworkflow.client.presenter.AbstractCustomWorkflowPresenter
 * @see com.ephesoft.dcma.gwt.customworkflow.client.view.dependencies.DependencyManagementView
 *
 */
public class DependencyManagementPresenter extends AbstractCustomWorkflowPresenter<DependencyManagementView> {

	private static int newIdentifier = 1;

	private final DependencyPresenter dependencyPresenter;

	private final EditDependencyPresenter editDependencyPresenter;
	private final CustomWorkflowBreadCrumbPresenter breadCrumbPresenter;

	private final List<String> dependenciesListBuffer = new ArrayList<String>();

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

	public boolean isCyclic = false;

	public boolean isValidDependency = true;

	public String selectedDependencyName;

	public String conflictingDependencyName;

	public DependencyManagementPresenter(CustomWorkflowController controller, DependencyManagementView view) {
		super(controller, view);
		this.dependencyPresenter = new DependencyPresenter(controller, view.getDependencyView());
		this.editDependencyPresenter = new EditDependencyPresenter(controller, view.getEditDependencyView());
		this.breadCrumbPresenter = new CustomWorkflowBreadCrumbPresenter(controller, view.getWorkflowBreadCrumbView());
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		/**
		 * Inject your events here
		 */
	}

	@Override
	public void bind() {

		if (controller.getAllPlugins() != null) {
			dependencyPresenter.bind();
		}

	}

	public void onSaveClicked() {
		ScreenMaskUtility.maskScreen(LocaleDictionary.get().getMessageValue(CustomWorkflowMessages.UPDATING_PLUGINS_LIST));
		controller.getRpcService().updateAllPluginDetailsDTOs(controller.getAllPlugins(), new EphesoftAsyncCallback<List<PluginDetailsDTO>>() {

			@Override
			public void customFailure(Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						CustomWorkflowMessages.ERROR_UPDATING_PLUGINS));
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
		ScreenMaskUtility.maskScreen(LocaleDictionary.get().getMessageValue(CustomWorkflowMessages.UPDATING_PLUGINS_LIST));
		controller.getRpcService().updateAllPluginDetailsDTOs(controller.getAllPlugins(), new EphesoftAsyncCallback<List<PluginDetailsDTO>>() {

			@Override
			public void customFailure(Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						CustomWorkflowMessages.ERROR_UPDATING_PLUGINS));
				ScreenMaskUtility.unmaskScreen();
			}

			@Override
			public void onSuccess(List<PluginDetailsDTO> pluginDetailsDTOs) {
				controller.setAllPlugins(pluginDetailsDTOs);
				String selectedPlugin = getSelectedValueFromList(view.getDependencyView().getPluginNames());
				controller.setSelectedPlugin(selectedPlugin);
				showDependenciesView();
				populatePluginName(controller.getSelectedPlugin());
				ScreenMaskUtility.unmaskScreen();
				ConfirmationDialogUtil.showConfirmationDialogSuccess(LocaleDictionary.get().getMessageValue(
						CustomWorkflowMessages.DEPENDENCIES_LIST_UPDATED_SUCCESSFULLY));
			}
		});
	}

	public void onCancelClicked() {
		ScreenMaskUtility.maskScreen();
		controller.getRpcService().getAllPluginDetailDTOs(new EphesoftAsyncCallback<List<PluginDetailsDTO>>() {

			@Override
			public void customFailure(Throwable arg0) {
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
			controller.getRpcService().getAllPluginDetailDTOs(new EphesoftAsyncCallback<List<PluginDetailsDTO>>() {

				@Override
				public void customFailure(Throwable arg0) {
					ScreenMaskUtility.unmaskScreen();
				}

				@Override
				public void onSuccess(List<PluginDetailsDTO> pluginDetailsDTOs) {
					showDependenciesView(pluginDetailsDTOs);
				}

			});
		} else {
			showDependenciesView(controller.getAllPlugins());
			String selectedPlugin = controller.getSelectedPlugin();
			if (selectedPlugin != null && !selectedPlugin.isEmpty()) {
				populatePluginName(selectedPlugin);
			}
		}

	}

	/**
	 * @param pluginDetailsDTOs
	 */
	private void showDependenciesView(List<PluginDetailsDTO> pluginDetailsDTOs) {
		controller.setAllPlugins(pluginDetailsDTOs);
		breadCrumbPresenter.createBreadCrumbForDependenciesList();
		dependencyPresenter.bind();

		view.showDependencyView();
		breadCrumbPresenter.setBackButtonVisibility(true);
		ScreenMaskUtility.unmaskScreen();
	}

	private String getSelectedValueFromList(ListBox listBox) {
		String value = CustomWorkflowConstants.EMPTY_STRING;
		int selectedIndex = listBox.getSelectedIndex();
		if (selectedIndex != -1) {

			value = listBox.getItemText(selectedIndex);
		}
		return value;

	}

	public void updatePluginDependency() {
		reinitializeValidationParameters();
		boolean dependenciesValidated = validateCurrentDependencies();
		if (dependenciesValidated) {
			ScreenMaskUtility.maskScreen(LocaleDictionary.get().getMessageValue(CustomWorkflowMessages.UPDATING_DEPENDENCIES_LIST));
			PluginDetailsDTO pluginDetailsDTO = getPluginForName(controller.getAllPlugins(), getEditDependencyPresenter().getView()
					.getPluginNamesList().getText());
			pluginDetailsDTO.setDirty(true);
			DependencyDTO dependencyDTO = createDependencyDTO();
			if (dependencyDTO.getDependencyType().equals(DependencyTypeProperty.UNIQUE.getProperty())) {
				dependencyDTO.setDependencies(CustomWorkflowConstants.EMPTY_STRING);
			}
			if (pluginDetailsDTO.getDependencies() == null) {
				pluginDetailsDTO.setDependencies(new ArrayList<DependencyDTO>(0));
				pluginDetailsDTO.getDependencies().clear();
			}
			if (controller.getSelectedDependencyDTO() == null) {
				dependencyDTO.setIdentifier(CustomWorkflowConstants.NEW + newIdentifier++);
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
			populatePluginName(dependencyDTO.getPluginDTO().getPluginName());
		}

	}

	/**
	 * @param dependencyDTO
	 */
	private void populatePluginName(String selectedPluginName) {
		ListBox pluginNamesListBox = view.getDependencyView().getPluginNames();
		int index = getIndexForValue(pluginNamesListBox, selectedPluginName);
		pluginNamesListBox.setSelectedIndex(index);

		PluginDetailsDTO pluginDTO = getPluginForName(controller.getAllPlugins(), selectedPluginName);
		// Create the dependencies list for it
		view.createDependenciesList(pluginDTO.getDependencies());
	}

	public void updateAllPlugins(List<PluginDetailsDTO> allPluginDetailsDTOs) {

		controller.getRpcService().updateAllPluginDetailsDTOs(allPluginDetailsDTOs, new EphesoftAsyncCallback<List<PluginDetailsDTO>>() {

			@Override
			public void customFailure(Throwable arg0) {
				/**
				 * RPC call failure handling
				 */
			}

			@Override
			public void onSuccess(List<PluginDetailsDTO> pluginDetailsDTOs) {
				controller.setAllPlugins(pluginDetailsDTOs);
			}
		});
	}

	private DependencyDTO createDependencyDTO() {
		DependencyDTO dependencyDTO = new DependencyDTO();

		String dependencyType = getSelectedValueFromList(getEditDependencyPresenter().getView().getDependencyTypeList());
		dependencyDTO.setDependencyType(dependencyType);

		String dependencies = getEditDependencyPresenter().getView().getDependenciesTextArea().getText();
		dependencies = dependencies.replaceAll(CustomWorkflowConstants.NEXT_LINE, CustomWorkflowConstants.EMPTY_STRING).replaceAll(
				CustomWorkflowConstants.CARRIAGE_RETURN, CustomWorkflowConstants.EMPTY_STRING);
		dependencyDTO.setDependencies(dependencies);

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
		populateEditDependencyView();
		addValueForPluginNameForAdd();
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
			String dependenciesNameString = selectedDependencyDTO.getDependencies();
			dependenciesNameString = dependenciesNameString.replaceAll(CustomWorkflowConstants.AND_SEPERATOR, CustomWorkflowConstants.AND_SEPERATOR
					+ CustomWorkflowConstants.NEXT_LINE);
			dependenciesNameString = dependenciesNameString.replaceAll(CustomWorkflowConstants.OR_SEPERATOR,
					CustomWorkflowConstants.OR_SEPERATOR + CustomWorkflowConstants.NEXT_LINE);
			getEditDependencyPresenter().getView().getDependenciesTextArea().setText(dependenciesNameString);
			enableItemsOnDependencyTypeChange(true);
		} else {
			// Disable the items
			getEditDependencyPresenter().getView().getDependenciesTextArea().setText(CustomWorkflowConstants.EMPTY_STRING);
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
	private void addValueForPluginNameForAdd() {
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
			editDependencyPresenter.getView().getDependenciesTextArea().setText(CustomWorkflowConstants.EMPTY_STRING);
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

		boolean isDuplicate = false;
		// Get list of and dependencies
		List<String> uniqueAndDependency = getAndDependenciesAsList(newDependenciesString);

		for (DependencyDTO dependencyDTO : dependencyDTOs) {
			if (!dependencyDTO.isDeleted()
					&& (dependencyDTO.getDependencyType().equals(DependencyTypeProperty.ORDER_BEFORE.getProperty()))) {
				String existingDependencies = dependencyDTO.getDependencies();

				List<String> existingUniqueAndDependency = getAndDependenciesAsList(existingDependencies);
				for (String andDependency : uniqueAndDependency) {
					if (existingUniqueAndDependency.contains(andDependency.replaceAll(CustomWorkflowConstants.NEXT_LINE,
							CustomWorkflowConstants.EMPTY_STRING))) {
						isDuplicate = true;
						String dependencyViewName = addStringAround(andDependency, CustomWorkflowConstants.OR_SEPERATOR,
								CustomWorkflowConstants.SPACE);
						ConfirmationDialogUtil.showConfirmationDialogError(dependencyViewName
								+ LocaleDictionary.get().getMessageValue(CustomWorkflowMessages.DEPENDENCY_ALREADY_EXISTS_TRY_AGAIN));
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

		boolean isValidEntry = true;
		List<String> uniqueAndDependency = getAndDependenciesAsList(newDependenciesString);

		Set<String> uniqueDependencySet = new HashSet<String>();

		for (String dependency : uniqueAndDependency) {

			if (!uniqueDependencySet.add(dependency)) {
				// dependency is duplicate
				isValidEntry = false;
				String dependencyViewName = addStringAround(dependency, CustomWorkflowConstants.OR_SEPERATOR,
						CustomWorkflowConstants.SPACE);
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						CustomWorkflowMessages.DEPENDENCIES_CONTAINS_DUPLICATE_ENTRY)
						+ dependencyViewName + LocaleDictionary.get().getMessageValue(CustomWorkflowMessages.TRY_AGAIN));
				break;
			}

		}

		return isValidEntry;
	}

	private String sortOrDependencies(String orDependenciesString) {
		List<String> sortedDependenciesString = new ArrayList<String>();
		String sortedOrDependenciesString = null;

		String[] orDependencies = orDependenciesString.split(CustomWorkflowConstants.OR_SEPERATOR);

		for (String dependency : orDependencies) {
			sortedDependenciesString.add(dependency.trim());
		}
		Collections.sort(sortedDependenciesString);
		StringBuffer sortedDependenciesStringBuffer = new StringBuffer();
		// Prepare a new string for the sorted dependencies
		for (String dependency : sortedDependenciesString) {
			if (!sortedDependenciesStringBuffer.toString().isEmpty()) {
				sortedDependenciesStringBuffer.append(CustomWorkflowConstants.OR_SEPERATOR);
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
	private List<String> getAndDependenciesAsList(String newDependenciesString) {
		List<String> uniqueAndDependency = new ArrayList<String>(0);
		String[] andDependencies = newDependenciesString.split(CustomWorkflowConstants.AND_SEPERATOR);

		for (String andDependency : andDependencies) {
			String sortedOrDependencies = sortOrDependencies(andDependency);
			uniqueAndDependency.add(sortedOrDependencies.trim());
		}
		return uniqueAndDependency;
	}

	/**
	 * The validateCurrentDependencies method validates entered dependencies for 
	 * duplication, cycle etc.
	 * @return true/false.
	 */
	public boolean validateCurrentDependencies() {
		boolean dependenciesValidated = false;

		boolean isCyclic = false;

		// boolean isValidDependency = true;

		boolean isDuplicate = false;

		boolean isValidEntry = true;

		String newDependencies = getEditDependencyPresenter().getView().getDependenciesTextArea().getText();
		selectedDependencyName = getEditDependencyPresenter().getView().getPluginNamesList().getText();

		dependenciesListBuffer.clear();
		dependenciesListBuffer.add(selectedDependencyName);

		PluginDetailsDTO pluginDetailsDTO = getPluginDetailsDTOByPluginName(controller.getAllPlugins(), selectedDependencyName);

		List<DependencyDTO> dependenciesList = new ArrayList<DependencyDTO>(pluginDetailsDTO.getDependencies());
		DependencyDTO selectedDependencyDTO = controller.getSelectedDependencyDTO();
		if (selectedDependencyDTO != null) {
			dependenciesList.remove(selectedDependencyDTO);
		}
		if (getEditDependencyPresenter().getView().getDependenciesList().isEnabled() && !newDependencies.isEmpty()) {
			isCyclic = false;
			isValidDependency = true;
			newDependencies = newDependencies.replaceAll(CustomWorkflowConstants.NEXT_LINE, CustomWorkflowConstants.EMPTY_STRING)
					.replaceAll(CustomWorkflowConstants.CARRIAGE_RETURN, CustomWorkflowConstants.EMPTY_STRING);
			isDuplicate = checkForDuplicateDependenciesEntry(dependenciesList, newDependencies);
			if (!isDuplicate) {
				isValidEntry = checkInvalidDependencyEntry(newDependencies);
				if (isValidEntry) {
					isCyclic = checkCyclicDependencies(newDependencies.trim(), selectedDependencyName);
				}
			}
		} else if (!getEditDependencyPresenter().getView().getDependenciesList().isEnabled()) {
			isCyclic = false;
			isValidDependency = true;
			isValidEntry = true;

			isDuplicate = checkForDuplicateUniqueDependencyEntry(dependenciesList);

			if (isDuplicate) {
				String dependencyNameView = addStringAround(selectedDependencyName, CustomWorkflowConstants.OR_SEPERATOR,
						CustomWorkflowConstants.SPACE);
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						CustomWorkflowMessages.UNIQUE_DEPENDENCY_FOR)
						+ dependencyNameView
						+ LocaleDictionary.get().getMessageValue(CustomWorkflowMessages.ALREADY_EXISTS_CORRECT_AND_TRY_AGAIN));
			}
		} else {
			isValidDependency = false;
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					CustomWorkflowMessages.NO_DEPENDENCY_SELECTED));
		}
		dependenciesValidated = !isCyclic && isValidDependency && !isDuplicate && isValidEntry;
		return dependenciesValidated;
	}

	private boolean checkForDuplicateUniqueDependencyEntry(List<DependencyDTO> dependenciesList) {

		boolean isDuplicate = false;
		for (DependencyDTO dependencyDTO : dependenciesList) {

			if (dependencyDTO.getDependencyType().equals(DependencyTypeProperty.UNIQUE.getProperty())) {
				isDuplicate = true;
				break;
			}
		}
		return isDuplicate;
	}

	/**
	 * 
	 */
	public void toggleOkButtonEnable(boolean enable) {
		getEditDependencyPresenter().getView().getOkButton().setEnabled(enable);
	}

	private boolean checkCyclicDependencies(String newDependencies, String selectedDependency) {
		Set<String> uniqueDependency = new HashSet<String>(0);

		String trimmedNewDependencies = newDependencies.trim();
		String[] andDependencies = trimmedNewDependencies.split(CustomWorkflowConstants.AND_SEPERATOR);

		for (String andDependency : andDependencies) {

			String[] orDependencies = andDependency.split(CustomWorkflowConstants.OR_SEPERATOR);

			for (String dependencyName : orDependencies) {
				boolean validDependencyname = false;
				Collection<String> validDependenciesNamesList = controller.getPluginIndexToNameMap().values();
				validDependencyname = validDependenciesNamesList.contains(dependencyName);
				if (validDependencyname) {
					uniqueDependency.add(dependencyName);
					if (uniqueDependency.contains(selectedDependencyName)) {
						// cyclic
						if (isCyclic) {
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
							+ LocaleDictionary.get().getMessageValue(CustomWorkflowMessages.INVALID_DEPENDENCY_NAME) + dependencyName);
					break;
				}
			}
			if (isCyclic || !isValidDependency) {
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
					String dependencies = CustomWorkflowConstants.EMPTY_STRING;
					dependencies = dependencyDTO.getDependencies();
					if (!dependenciesBuffer.toString().isEmpty()) {
						dependenciesBuffer.append(CustomWorkflowConstants.AND_SEPERATOR);
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
			confirmationDialog.okButton.setText(LocaleDictionary.get().getConstantValue(CustomWorkflowConstants.YES));
			confirmationDialog.cancelButton.setText(LocaleDictionary.get().getConstantValue(CustomWorkflowConstants.NO_CONSTANT));
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
		String selectedDependency = getSelectedValueFromList(getEditDependencyPresenter().getView().getDependenciesList());
		if (selectedDependency != null && !selectedDependency.isEmpty()) {
			StringBuffer selectedDependencies = new StringBuffer(getEditDependencyPresenter().getView().getDependenciesTextArea()
					.getText());
			if (!selectedDependencies.toString().isEmpty()) {
				if (dependencyType.equals(CustomWorkflowConstants.AND_SEPERATOR)) {
					selectedDependencies.append(CustomWorkflowConstants.AND_SEPERATOR);
					selectedDependencies.append(CustomWorkflowConstants.NEXT_LINE);
				} else if (dependencyType.equals(CustomWorkflowConstants.OR_SEPERATOR)) {
					selectedDependencies.append(CustomWorkflowConstants.OR_SEPERATOR);
					selectedDependencies.append(CustomWorkflowConstants.NEXT_LINE);
				}
			}
			selectedDependencies.append(selectedDependency);
			TextArea dependenciesTextArea = editDependencyPresenter.getView().getDependenciesTextArea();
			dependenciesTextArea.setText(selectedDependencies.toString());
			dependenciesTextArea.setCursorPos(selectedDependencies.length());
		}
	}

	private String addStringAround(String originalString, String markedCharacter, String surrondingCharacter) {
		StringBuffer replacementStringBuffer = new StringBuffer();
		replacementStringBuffer.append(surrondingCharacter);
		replacementStringBuffer.append(markedCharacter);
		replacementStringBuffer.append(surrondingCharacter);

		return originalString.replaceAll(markedCharacter, replacementStringBuffer.toString());
	}
	
	/**
	 * The <code>reinitializeValidationParameters</code> is a method that re initialize 
	 * validation parameters used for validating dependency update.
	 */
	private void reinitializeValidationParameters() {
		isCyclic = false;
		isValidDependency = true;
	}
}
