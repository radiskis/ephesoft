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

package com.ephesoft.dcma.gwt.login.client;

import java.util.Map;

import com.ephesoft.dcma.gwt.core.client.DCMAEntryPoint;
import com.ephesoft.dcma.gwt.core.client.DCMARemoteServiceAsync;
import com.ephesoft.dcma.gwt.core.client.EphesoftAsyncCallback;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleInfo;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.constants.CoreCommonConstants;
import com.ephesoft.dcma.gwt.login.client.i18n.LoginConstants;
import com.ephesoft.dcma.gwt.login.client.i18n.LoginMessages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SubmitButton;

public class LoginEntryPoint extends DCMAEntryPoint<DCMARemoteServiceAsync> {

	private static final String LICENSE_TEXT = "license-text";
	private static final String EXPIRY_MSG_LEBEL = "expiryMsgLabel";

	@Override
	public void onLoad() {
		Document.get().setTitle(LocaleDictionary.get().getConstantValue(LoginConstants.LOGIN_TITLE));
		Label userName = new Label(LocaleDictionary.get().getConstantValue(LoginConstants.LOGIN_USERNAME));
		userName.setStyleName("text_bold");
		Label password = new Label(LocaleDictionary.get().getConstantValue(LoginConstants.LOGIN_PASSWORD));
		password.setStyleName("text_bold");
		/*
		 * CheckBox remMe = new CheckBox(LocaleDictionary.get().getConstantValue( LoginConstants.login_remember_me));
		 * remMe.setEnabled(false); Label forgPwd = new Label(LocaleDictionary.get().getConstantValue(LoginConstants
		 * .login_forgot_pwd));
		 */
		Label version = new Label(LocaleDictionary.get().getConstantValue(LoginConstants.VERSION_TEXT));
		final Label versionNumber = new Label();
		RootPanel.get("userName").add(userName);
		RootPanel.get("password").add(password);
		/*
		 * RootPanel.get("remMe").add(remMe); RootPanel.get("forgPwd").add(forgPwd);
		 */
		RootPanel.get("version").add(version);
		((LoginRemoteServiceAsync) createRpcService()).getProductVersion(new EphesoftAsyncCallback<String>() {

			@Override
			public void onSuccess(String version) {
				versionNumber.setText(version);
			}

			@Override
			public void customFailure(Throwable arg0) {
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
						LoginConstants.UNABLE_TO_RETRIVE_VERSION_INFO));

			}
		});
		RootPanel.get("versionLabel").add(versionNumber);

		SubmitButton submitButton = new SubmitButton();
		submitButton.setFocus(true);
		submitButton.setText(LocaleDictionary.get().getConstantValue(LoginConstants.LOGIN_BUTTON_TEXT));
		submitButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				login();
			}
		});

		submitButton.setStyleName("btn_blue");
		RootPanel.get("buttonLogin").add(submitButton);
		final Label expiryMsg = new Label();

		((LoginRemoteServiceAsync) createRpcService()).getLicenseExpiryMsg(new EphesoftAsyncCallback<Void>() {

			@Override
			public void customFailure(Throwable arg0) {
				String alertMsg = arg0.getLocalizedMessage();
				int indexOfDelimiter = alertMsg.indexOf('@');
				String days = alertMsg.substring(0, indexOfDelimiter);
				int remainingDays = Integer.parseInt(days);
				String dateString = alertMsg.substring(indexOfDelimiter + 1);
				if (remainingDays != 0) {
					expiryMsg.setText(LocaleDictionary.get().getMessageValue(LoginMessages.LICENSE_EXPIRY_MSG, days, dateString));
				} else {
					expiryMsg.setText(LocaleDictionary.get().getMessageValue(LoginMessages.LICENSE_EXPIRY_MSG_TODAY));
				}
			}

			@Override
			public void onSuccess(Void arg0) {
				// Nothing to do .. on success of RPC call
			}
		});
		expiryMsg.setStyleName(LICENSE_TEXT);
		RootPanel.get(EXPIRY_MSG_LEBEL).add(expiryMsg);
		final HorizontalPanel horPanel = new HorizontalPanel();
		horPanel.setWidth("100%");
		RootPanel.get().add(horPanel);
		ScreenMaskUtility.maskScreen("Loading");
		rpcService.getFooterProperties(new EphesoftAsyncCallback<Map<String, String>>() {

			@Override
			public void onSuccess(Map<String, String> footerProperties) {
				String footerText = footerProperties.get(CoreCommonConstants.FOOTER_TEXT_KEY);
				String footerLink = footerProperties.get(CoreCommonConstants.FOOTER_LINK_KEY);
				final Anchor footerInfo = new Anchor(footerText, footerLink);
				horPanel.add(footerInfo);
				horPanel.setCellHorizontalAlignment(footerInfo, HasHorizontalAlignment.ALIGN_CENTER);
				ScreenMaskUtility.unmaskScreen();
			}

			@Override
			public void customFailure(Throwable arg0) {
				ScreenMaskUtility.unmaskScreen();
				ConfirmationDialogUtil.showConfirmationDialogError("Application could not be authorized. Please try again.");
			}
		});
	}

	private native void login() /*-{
								return $wnd.loginSubmit();
								}-*/;

	@Override
	public DCMARemoteServiceAsync createRpcService() {
		return GWT.create(LoginRemoteService.class);
	}

	@Override
	public String getHomePage() {
		return "Login.html";
	}

	@Override
	public LocaleInfo createLocaleInfo(String locale) {
		return new LocaleInfo(locale, "loginConstants", "loginMessages");
	}
}
