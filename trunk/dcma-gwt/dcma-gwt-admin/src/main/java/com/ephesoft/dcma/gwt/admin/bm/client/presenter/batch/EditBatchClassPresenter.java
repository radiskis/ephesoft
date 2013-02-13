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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.MessageConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.batch.EditBatchClassView;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.validator.EmptyStringValidator;
import com.ephesoft.dcma.gwt.core.shared.BatchClassDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.RoleDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Presenter for editing details of a batch class.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class EditBatchClassPresenter extends AbstractBatchClassPresenter<EditBatchClassView> {

	/**
	 * Constructor.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view EditBatchClassView
	 */
	public EditBatchClassPresenter(final BatchClassManagementController controller, final EditBatchClassView view) {
		super(controller, view);
	}

	/**
	 * On cancel click.
	 */
	public void onCancel() {
		controller.getMainPresenter().getBatchClassViewPresenter().showBatchClassDetailView();
	}

	/**
	 * On save click.
	 */
	public void onSave() {

		if (!view.getValidateTextBox().validate() || !view.getValidateDescTextBox().validate()) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.MANDATORY_FIELDS_BLANK));
		} else if (!view.getValidateSystemFolderTextBox().validate()) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.INVALID_SYSTEM_FOLDER_PATH)
					+ LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.CHANGE_AND_TRY_AGAIN));
		} else {
			boolean validCheck = true;
			boolean isPriorityNumber = isNumber(view.getPriority());
			if (validCheck && !isPriorityNumber) {
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.PRIORITY_SHOULD_BE_NUMERIC);
				validCheck = false;
			}
			if (validCheck
					&& (Integer.parseInt(view.getPriority()) < AdminConstants.PRIORITY_LOWER_LIMIT || Integer.parseInt(view
							.getPriority()) > AdminConstants.PRIORITY_UPPER_LIMIT)) {
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.PRIORITY_SHOULD_BE_BETWEEN_1_AND_100);
				validCheck = false;
			}

			if (validCheck && !controller.isSuperAdmin()) {
				String roleName = checkForValidRoleToUnmap();
				if (roleName != null) {
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							BatchClassManagementMessages.CANT_DELETE_OWN_ROLE, roleName));
					validCheck = false;
				}
			}

			if (validCheck) {
				checkSystemFolderAndProceed();

			}
		}
	}

	private void checkSystemFolderAndProceed() {
		final BatchClassDTO batchClass = controller.getBatchClass();
		final String newSystemFolder = view.getSystemFolder();
		final String existingSystemFolder = batchClass.getSystemFolder();
		if (existingSystemFolder == null || !existingSystemFolder.equals(newSystemFolder)) {
			controller.getRpcService().getAllUnFinishedBatchInstancesCount(batchClass.getIdentifier(), new AsyncCallback<Integer>() {

				@Override
				public void onSuccess(Integer unfinishedBatchCount) {
					if (unfinishedBatchCount > 0) {
						ConfirmationDialog confirmationDialog = new ConfirmationDialog();
						confirmationDialog.okButton.setText(BatchClassManagementConstants.YES);
						confirmationDialog.cancelButton.setText(BatchClassManagementConstants.NO);
						confirmationDialog
								.setMessage("This batch class has "
										+ unfinishedBatchCount
										+ " batches under processing. Changing the system folder now will make them loose the restart functionality. Do you still want to continue ?");

						confirmationDialog.addDialogListener(new DialogListener() {

							@Override
							public void onOkClick() {
								batchClass.setSystemFolder(newSystemFolder);
								updateBatchClassDto(batchClass);
								showBatchClassView();
							}

							@Override
							public void onCancelClick() {
								updateBatchClassDto(batchClass);
								showBatchClassView();
							}
						});
						confirmationDialog.okButton.setFocus(true);
						confirmationDialog.show();

					} else {
						batchClass.setSystemFolder(newSystemFolder);
						updateBatchClassDto(batchClass);
						showBatchClassView();
					}
				}

				@Override
				public void onFailure(Throwable arg0) {
					ConfirmationDialogUtil.showConfirmationDialogError("Cannot update the system folder.");
				}
			});
		} else {
			updateBatchClassDto(batchClass);
			showBatchClassView();
		}
	}

	private void showBatchClassView() {
		controller.getMainPresenter().getBatchClassViewPresenter().bind();
		controller.getMainPresenter().getBatchClassViewPresenter().showBatchClassDetailView();
	}

	private void updateBatchClassDto(final BatchClassDTO batchClass) {
		batchClass.setDescription(view.getDescription());
		batchClass.setPriority(view.getPriority());
		batchClass.setUncFolder(view.getUncFolder());
		batchClass.setVersion(view.getVersion());
		batchClass.setAssignedRole(view.getRoleList());
	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {
		final BatchClassDTO batchClass = controller.getBatchClass();
		if (batchClass != null) {
			view.setName(batchClass.getName());
			view.setDescription(batchClass.getDescription());
			view.setPriority(batchClass.getPriority());
			view.setUncFolder(batchClass.getUncFolder());
			view.setVersion(batchClass.getVersion());
			view.getValidateTextBox().addValidator(new EmptyStringValidator(view.getPriorityTextBox()));
			view.getValidateTextBox().toggleValidDateBox();
			view.getValidateDescTextBox().addValidator(new EmptyStringValidator(view.getDescriptionTextBox()));
			view.getValidateDescTextBox().toggleValidDateBox();
			view.setRole(batchClass.getAssignedRole(), controller.getAllRoles());
			view.getDescriptionTextBox().setFocus(true);
			view.getValidateSystemFolderTextBox().toggleValidDateBox();
			view.setSystemFolder(batchClass.getSystemFolder());
		}
	}

	/**
	 * To handle events.
	 * 
	 * @param eventBus HandlerManager
	 */
	@Override
	public void injectEvents(final HandlerManager eventBus) {
		// event handling to be done here.
	}

	private boolean isNumber(final String priority) {
		boolean returnValue = true;
		try {
			Integer.parseInt(priority);
		} catch (NumberFormatException e) {
			returnValue = false;
		}
		return returnValue;
	}

	/**
	 * To check for valid role to unmap.
	 * 
	 * @return String
	 */
	public String checkForValidRoleToUnmap() {
		String roleName = null;
		List<RoleDTO> assignedRoles = controller.getBatchClass().getAssignedRole();
		Set<String> userRoles = controller.getUserRoles();
		List<RoleDTO> assignedUserRoles = getAssignedUserRoles(assignedRoles, userRoles);
		List<RoleDTO> selectedRolesList = view.getRoleList();
		for (RoleDTO roleDTO : assignedUserRoles) {
			if (!selectedRolesList.contains(roleDTO)) {
				roleName = roleDTO.getName();
				break;
			}
		}
		if (roleName != null) {
			view.setRole(assignedRoles, controller.getAllRoles());
		}
		return roleName;
	}

	private List<RoleDTO> getAssignedUserRoles(List<RoleDTO> assignedRoles, Set<String> userRoles) {
		List<RoleDTO> assignedUserRoles = new ArrayList<RoleDTO>();
		for (RoleDTO roleDTO : assignedRoles) {
			if (userRoles.contains(roleDTO.getName())) {
				assignedUserRoles.add(roleDTO);
			}
		}
		return assignedUserRoles;
	}
}
