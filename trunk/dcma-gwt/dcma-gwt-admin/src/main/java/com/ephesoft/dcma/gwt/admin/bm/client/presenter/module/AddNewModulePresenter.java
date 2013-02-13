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

import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.MessageConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.module.AddNewModuleView;
import com.ephesoft.dcma.gwt.core.client.EphesoftAsyncCallback;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.ModuleDTO;
import com.ephesoft.dcma.gwt.core.shared.StringUtil;
import com.google.gwt.event.shared.HandlerManager;

/**
 * Presenter for adding a new module.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class AddNewModulePresenter extends AbstractBatchClassPresenter<AddNewModuleView> {

	/**
	 * newModuleDTO ModuleDTO.
	 */
	private ModuleDTO newModuleDTO;

	/**
	 * To get Module DTO.
	 * 
	 * @return the moduleDTO
	 */
	public ModuleDTO getModuleDTO() {
		return newModuleDTO;
	}

	/**
	 * To set Module DTO.
	 * 
	 * @param moduleDTO ModuleDTO
	 */
	public void setModuleDTO(ModuleDTO moduleDTO) {
		this.newModuleDTO = moduleDTO;
	}

	/**
	 * To add new module presenter.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view AddNewModuleView
	 */
	public AddNewModulePresenter(final BatchClassManagementController controller, final AddNewModuleView view) {
		super(controller, view);
	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {

	}

	/**
	 * To show add new module view.
	 */
	public void showAddNewModuleView() {
		view.getSaveButton().setFocus(true);
		view.getDialogBox().setWidth("100%");
		view.getDialogBox().center();
		view.getDialogBox().add(view);
		view.getDialogBox().show();
		view.getDialogBox().setText(MessageConstants.ADD_NEW_MODULE);
	}

	/**
	 * To handle events.
	 * 
	 * @param eventBus HandlerManager
	 */
	@Override
	public void injectEvents(final HandlerManager eventBus) {
		// Event handling to be done here.
	}

	/**
	 * To perform operations on OK click.
	 */
	public void onOkClicked() {
		String moduleName = view.getName();
		String moduleDescription = view.getDescription();
		newModuleDTO = new ModuleDTO();
		newModuleDTO.setName(moduleName);
		newModuleDTO.setDescription(moduleDescription);
		boolean validCheck = true;
		boolean nameValidated = view.getValidateNameTextBox().validate();
		boolean descriptionValidated = view.getValidateDescTextBox().validate();
		if (!nameValidated || !descriptionValidated) {
			ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.MANDATORY_FIELDS_ERROR_MSG);
			validCheck = false;
		}
		boolean containsSpecialCharacter = StringUtil.checkForSpecialCharacter(moduleName);
		if (validCheck && containsSpecialCharacter) {
			ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.SPECIAL_CHARACTER_NOT_ALLOWED);
			validCheck = false;
		}
		if (validCheck) {
			ScreenMaskUtility.maskScreen("Adding new Module.");
			controller.getRpcService().createNewModule(newModuleDTO, new EphesoftAsyncCallback<ModuleDTO>() {

				@Override
				public void onSuccess(ModuleDTO moduleDTO) {
					view.getDialogBox().hide();
					ScreenMaskUtility.unmaskScreen();
					ConfirmationDialogUtil.showConfirmationDialogSuccess("Module Added Succesfully.");
					String newModuleName = moduleDTO.getName();
					controller.getAllModules().add(newModuleName);
					controller.getModuleNameToDtoMap().put(newModuleName, moduleDTO);
					controller.getBatchClassManagementPresenter().getConfigureModulesPresenter().populateAndShowAddModuleView();
				}

				@Override
				public void customFailure(Throwable throwable) {
					ScreenMaskUtility.unmaskScreen();
					String errorMessage = throwable.getLocalizedMessage();
					ConfirmationDialogUtil.showConfirmationDialogError("Error adding new Module. " + errorMessage);
				}
			});
		}

	}

}
