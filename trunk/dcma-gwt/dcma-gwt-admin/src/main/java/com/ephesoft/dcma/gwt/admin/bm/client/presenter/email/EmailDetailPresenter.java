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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.email;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.email.EmailDetailView;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.EmailConfigurationDTO;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The presenter for view that shows the email details.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class EmailDetailPresenter extends AbstractBatchClassPresenter<EmailDetailView> {

	/**
	 * ERROR_VALIDATING_EMAIL String.
	 */
	protected static final String ERROR_VALIDATING_EMAIL = "error_validating_email";

	/**
	 * Constructor.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view EmailDetailView
	 */
	public EmailDetailPresenter(BatchClassManagementController controller, EmailDetailView view) {
		super(controller, view);

	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {
		if (controller.getSelectedEmailConfiguration() != null) {
			view.setUserName(controller.getSelectedEmailConfiguration().getUserName());
			view.setPassword(controller.getSelectedEmailConfiguration().getPassword());
			view.setServerName(controller.getSelectedEmailConfiguration().getServerName());
			view.setServerType(controller.getSelectedEmailConfiguration().getServerType());
			view.setFolderName(controller.getSelectedEmailConfiguration().getFolderName());
			if (controller.getSelectedEmailConfiguration().getIsSSL() != null) {
				view.setSsl(controller.getSelectedEmailConfiguration().getIsSSL().toString());
			} else {
				view.setSsl(Boolean.FALSE.toString());
			}
			if (controller.getSelectedEmailConfiguration().getPortNumber() != null) {
				view.setPortNumber(controller.getSelectedEmailConfiguration().getPortNumber().toString());
			} else {
				view.setPortNumber(AdminConstants.EMPTY_STRING);
			}
		}
	}

	/**
	 * To handle events.
	 * 
	 * @param eventBus HandlerManager
	 */
	@Override
	public void injectEvents(HandlerManager eventBus) {
		// TODO Auto-generated method stub

	}

	/**
	 * To perform operations on test email configuration button clicked.
	 * 
	 * @param userName String
	 * @param password String
	 * @param portNumber String
	 * @param folderName String
	 * @param isSSL String
	 * @param serverName String
	 * @param serverType String
	 */
	public void onTestEmailConfigButtonClick(final String userName, final String password, final String portNumber,
			final String folderName, final String isSSL, final String serverName, final String serverType) {
		EmailConfigurationDTO emailConfigDTO = createEmailConfigurationDTO(userName, password, serverName, serverType, folderName,
				isSSL, portNumber);
		ScreenMaskUtility.maskScreen("Validating Email ....");
		controller.getRpcService().validateEmailConfig(emailConfigDTO, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(final Throwable arg0) {
				ScreenMaskUtility.unmaskScreen();
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						BatchClassManagementConstants.EMAIL_CONFIG_VALIDATION_FAILURE));
			}

			@Override
			public void onSuccess(final Boolean isValid) {
				ScreenMaskUtility.unmaskScreen();
				if (isValid) {
					ConfirmationDialogUtil.showConfirmationDialogSuccess(LocaleDictionary.get().getMessageValue(
							BatchClassManagementConstants.EMAIL_CONFIG_VALIDATION_SUCCESS));
				} else {
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							BatchClassManagementConstants.EMAIL_CONFIG_VALIDATION_FAILURE));
				}
			}
		});

	}

	private EmailConfigurationDTO createEmailConfigurationDTO(final String userName, final String password, final String serverName,
			final String serverType, final String folderName, final String isSSL, final String portNumber) {
		EmailConfigurationDTO emailConfigDTO = new EmailConfigurationDTO();
		emailConfigDTO.setUserName(userName);
		emailConfigDTO.setPassword(password);
		emailConfigDTO.setServerName(serverName);
		emailConfigDTO.setServerType(serverType);
		emailConfigDTO.setIsSSL(Boolean.valueOf(isSSL));
		emailConfigDTO.setPortNumber(Integer.parseInt(portNumber));
		emailConfigDTO.setFolderName(folderName);
		return emailConfigDTO;
	}
}
