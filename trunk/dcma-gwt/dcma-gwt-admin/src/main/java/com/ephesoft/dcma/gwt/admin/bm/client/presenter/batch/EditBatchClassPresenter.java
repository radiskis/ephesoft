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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.MessageConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.batch.EditBatchClassView;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.validator.EmptyStringValidator;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.RoleDTO;
import com.google.gwt.event.shared.HandlerManager;

/**
 * 
 * @author Ephesoft
 *
 */
/**
 * Presenter for editing details of a batch class.
 */
public class EditBatchClassPresenter extends AbstractBatchClassPresenter<EditBatchClassView> {

	public EditBatchClassPresenter(final BatchClassManagementController controller, final EditBatchClassView view) {
		super(controller, view);
	}

	public void onCancel() {
		controller.getMainPresenter().getBatchClassViewPresenter().showBatchClassDetailView();
	}

	public void onSave() {

		if (!view.getValidateTextBox().validate() || !view.getValidateDescTextBox().validate()) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.MANDATORY_FIELDS_BLANK));
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

			if (validCheck && view.getRole().isEmpty()) {
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.NOTHING_SELECTED);
				validCheck = false;
			}

			if (validCheck && !controller.IsSuperAdmin()) {
				String roleName = checkForValidRoleToUnmap();
				if(roleName!=null){
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.CANT_DELETE_OWN_ROLE,roleName));
				validCheck = false;
				}
			}

			if (validCheck) {
				controller.getBatchClass().setDescription(view.getDescription());
				controller.getBatchClass().setPriority(view.getPriority());
				controller.getBatchClass().setUncFolder(view.getUncFolder());
				controller.getBatchClass().setVersion(view.getVersion());
				controller.getBatchClass().setAssignedRole(view.getRoleList());

				controller.getMainPresenter().getBatchClassViewPresenter().bind();
				controller.getMainPresenter().getBatchClassViewPresenter().showBatchClassDetailView();
			}
		}
	}

	@Override
	public void bind() {
		if (controller.getBatchClass() != null) {
			view.setName(controller.getBatchClass().getName());
			view.setDescription(controller.getBatchClass().getDescription());
			view.setPriority(controller.getBatchClass().getPriority());
			view.setUncFolder(controller.getBatchClass().getUncFolder());
			view.setVersion(controller.getBatchClass().getVersion());
			view.getValidateTextBox().addValidator(new EmptyStringValidator(view.getPriorityTextBox()));
			view.getValidateTextBox().toggleValidDateBox();
			view.getValidateDescTextBox().addValidator(new EmptyStringValidator(view.getDescriptionTextBox()));
			view.getValidateDescTextBox().toggleValidDateBox();
			view.setRole(controller.getBatchClass().getAssignedRole(), controller.getAllRoles());
		}
	}

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
