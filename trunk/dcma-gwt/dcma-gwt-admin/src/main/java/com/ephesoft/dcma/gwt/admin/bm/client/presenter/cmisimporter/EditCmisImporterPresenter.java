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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.cmisimporter;

import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.cmisimporter.EditCmisImporterView;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.validator.EmptyStringValidator;
import com.ephesoft.dcma.gwt.core.shared.CmisConfigurationDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.google.gwt.event.shared.HandlerManager;

/**
 * The presenter for view that shows the edit cmis importer details.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class EditCmisImporterPresenter extends AbstractBatchClassPresenter<EditCmisImporterView> {

	/**
	 * Constructor.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view EditCmisImporterView
	 */
	public EditCmisImporterPresenter(BatchClassManagementController controller, EditCmisImporterView view) {
		super(controller, view);
	}

	/**
	 * In case of cancel click.
	 */
	public void onCancel() {
		if (controller.isAdd()) {
			controller.getMainPresenter().showBatchClassView(controller.getBatchClass());
			controller.setAdd(false);
		} else {
			controller.getMainPresenter().getCmisImporterViewPresenter().showCmisImporterView();
		}
	}

	/**
	 * In case of save click.
	 */
	public void onSave() {
		boolean validFlag = true;
		if (controller.isAdd()) {
			if (validFlag
					&& (!view.getValidateUserNameTextBox().validate() || !view.getValidatePasswordTextBox().validate()
							|| !view.getValidateServerURLTextBox().validate() || !view.getValidateCmisPropertyTextBox().validate()
							|| !view.getValidateFolderNameTextBox().validate() || !view.getValidateFileExtensionTextBox().validate()
							|| !view.getValidateValueTextBox().validate() || !view.getValidateValueToUpdateTextBox().validate())) {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.BLANK_ERROR), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.ADD_CMIS_CONFIGURATION), Boolean.TRUE);
				validFlag = false;
			}

			if (validFlag
					&& (controller.getBatchClass().checkCmisConfiguration(view.getServerUrl(), view.getUserName(), view.getPassword(),
							view.getRepositoryID(), view.getFileExtension(), view.getFolderName(), view.getCmisProperty(), view
									.getValue(), view.getValueToUpdate()))) {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.ALREADY_EXISTS_ERROR), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.ADD_CMIS_CONFIGURATION), Boolean.TRUE);
				validFlag = false;
			}
		} else {
			if (validFlag
					&& (!view.getValidateUserNameTextBox().validate() || !view.getValidatePasswordTextBox().validate()
							|| !view.getValidateServerURLTextBox().validate() || !view.getValidateCmisPropertyTextBox().validate()
							|| !view.getValidateFolderNameTextBox().validate() || !view.getValidateFileExtensionTextBox().validate()
							|| !view.getValidateValueTextBox().validate() || !view.getValidateValueToUpdateTextBox().validate())) {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.BLANK_ERROR), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.EDIT_CMIS_CONFIGURATION_TITLE), Boolean.TRUE);
				validFlag = false;
			}
		}
		if (validFlag) {
			if (controller.isAdd()) {
				controller.getBatchClass().addCmisConfiguration(controller.getSelectedCmisConfiguration());
				controller.setAdd(false);
			}
			controller.getSelectedCmisConfiguration().setUserName(view.getUserName());
			controller.getSelectedCmisConfiguration().setPassword(view.getPassword());
			controller.getSelectedCmisConfiguration().setServerURL(view.getServerUrl());
			controller.getSelectedCmisConfiguration().setCmisProperty(view.getCmisProperty());
			controller.getSelectedCmisConfiguration().setFolderName(view.getFolderName());
			controller.getSelectedCmisConfiguration().setFileExtension(view.getFileExtension());
			controller.getSelectedCmisConfiguration().setValue(view.getValue());
			controller.getSelectedCmisConfiguration().setValueToUpdate(view.getValueToUpdate());
			controller.getSelectedCmisConfiguration().setRepositoryID(view.getRepositoryID());
			controller.getMainPresenter().getCmisImporterViewPresenter().bind();
			controller.getMainPresenter().getCmisImporterViewPresenter().showCmisImporterView();
			if (controller.getSelectedCmisConfiguration().isNew()) {
				controller.getMainPresenter().getBatchClassBreadCrumbPresenter().createBreadCrumb(
						controller.getSelectedCmisConfiguration());
			}
		}
	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {
		if (controller.getSelectedCmisConfiguration() != null) {
			view.setUserName(controller.getSelectedCmisConfiguration().getUserName());
			view.setPassword(controller.getSelectedCmisConfiguration().getPassword());
			view.setServerUrl(controller.getSelectedCmisConfiguration().getServerURL());
			view.setCmisProperty(controller.getSelectedCmisConfiguration().getCmisProperty());
			view.setFolderName(controller.getSelectedCmisConfiguration().getFolderName());
			// view.setFileExtension(controller.getSelectedCmisConfiguration().getFileExtension());
			view.setValue(controller.getSelectedCmisConfiguration().getValue());
			view.setValueToUpdate(controller.getSelectedCmisConfiguration().getValueToUpdate());
			view.setRepositoryID(controller.getSelectedCmisConfiguration().getRepositoryID());
		} else {
			CmisConfigurationDTO cmisConfigurationDTO = controller.getMainPresenter().getBatchClassViewPresenter()
					.createCmisConfigurationDTOObject();

			cmisConfigurationDTO.setUserName(view.getUserName());
			cmisConfigurationDTO.setPassword(view.getPassword());
			cmisConfigurationDTO.setServerURL(view.getServerUrl());
			cmisConfigurationDTO.setCmisProperty(view.getCmisProperty());
			cmisConfigurationDTO.setFolderName(view.getFolderName());
			cmisConfigurationDTO.setFileExtension(view.getFileExtension());
			cmisConfigurationDTO.setValue(view.getValue());
			cmisConfigurationDTO.setValueToUpdate(view.getValueToUpdate());
			cmisConfigurationDTO.setRepositoryID(view.getRepositoryID());

			controller.setSelectedCmisConfiguration(cmisConfigurationDTO);
		}
		view.getValidateCmisPropertyTextBox().addValidator(new EmptyStringValidator(view.getCmisPropertyTextBox()));
		view.getValidatePasswordTextBox().addValidator(new EmptyStringValidator(view.getPasswordTextBox()));
		view.getValidateServerURLTextBox().addValidator(new EmptyStringValidator(view.getServerUrlTextBox()));
		view.getValidateFileExtensionTextBox().addValidator(new EmptyStringValidator(view.getFileExtensionTextBox()));
		view.getValidateFolderNameTextBox().addValidator(new EmptyStringValidator(view.getFolderNameTextBox()));
		view.getValidateRepositoryIDTextBox().addValidator(new EmptyStringValidator(view.getRepositoryIDTextBox()));
		view.getValidateValueTextBox().addValidator(new EmptyStringValidator(view.getValueTextBox()));
		view.getValidateValueToUpdateTextBox().addValidator(new EmptyStringValidator(view.getValueToUpdateTextBox()));
		view.getValidateUserNameTextBox().addValidator(new EmptyStringValidator(view.getUserNameTextBox()));

		view.getValidateCmisPropertyTextBox().toggleValidDateBox();
		view.getValidatePasswordTextBox().toggleValidDateBox();
		view.getValidateServerURLTextBox().toggleValidDateBox();
		view.getValidateFileExtensionTextBox().toggleValidDateBox();
		view.getValidateFolderNameTextBox().toggleValidDateBox();
		view.getValidateRepositoryIDTextBox().toggleValidDateBox();
		view.getValidateValueTextBox().toggleValidDateBox();
		view.getValidateValueToUpdateTextBox().toggleValidDateBox();
		view.getValidateUserNameTextBox().toggleValidDateBox();
		view.getServerUrlTextBox().setFocus(true);

	}

	/**
	 * To handle events.
	 * 
	 * @param eventBus HandlerManager
	 */
	@Override
	public void injectEvents(HandlerManager eventBus) {
		// Event handling is done here.
	}

}
