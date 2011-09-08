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

package com.ephesoft.dcma.gwt.core.client.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.ephesoft.dcma.gwt.core.client.event.SignoutEvent;
import com.ephesoft.dcma.gwt.core.client.event.TabSelectionEvent;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleCommonConstants;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabBar;

public class Header extends DCMAComplexPanel {

	private static final String DISCARD_BUTTON = "Discard";
	private static final String SAVE_BUTTON = "Save";

	interface Binder extends UiBinder<DockLayoutPanel, Header> {
	}

	public interface Images extends ClientBundle {

		ImageResource logo();

		ImageResource logo_tk();
	}

	@UiField
	Label userName;
	@UiField
	Anchor signOutLink;
	@UiField
	TabBar tabBar;

	@UiField
	Label logo;

	@UiField
	HorizontalPanel signoutPanel;

	private boolean dialogBoxOnTabClick;

	private String dialogMessage;

	private Map<Integer, String> tabs = new HashMap<Integer, String>();

	private static final Binder binder = GWT.create(Binder.class);

	public Header() {
		Images images = GWT.create(Images.class);
		initWidget(binder.createAndBindUi(this));
		signOutLink.setText(LocaleDictionary.get().getConstantValue(LocaleCommonConstants.header_label_signOut));
		String locale = LocaleDictionary.get().getLocaleInfo().getLocale();
		if (locale != null && locale.equalsIgnoreCase("tk")) {
			DOM.setInnerHTML(logo.getElement(), AbstractImagePrototype.create(images.logo_tk()).getHTML());
		} else {
			DOM.setInnerHTML(logo.getElement(), AbstractImagePrototype.create(images.logo()).getHTML());
		}
		tabBar.addStyleName("header_tabs");
		signoutPanel.addStyleName("logoPanel");
	}

	public void addTab(String display, final String htmlPattern, final boolean isFromBatchList) {
		Label htmlUrl = new Label(display);
		htmlUrl.setWordWrap(false);
		htmlUrl.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				if (showDialogBoxOnTabClick()) {
					final ConfirmationDialog confirmationDialog = new ConfirmationDialog();
					confirmationDialog.setMessage(dialogMessage);
					confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(LocaleCommonConstants.dialog_box_title));
					confirmationDialog.show();
					confirmationDialog.center();
					confirmationDialog.okButton.setFocus(true);
					confirmationDialog.okButton.setText(SAVE_BUTTON);
					confirmationDialog.cancelButton.setText(DISCARD_BUTTON);
					confirmationDialog.addDialogListener(new DialogListener() {

						@Override
						public void onOkClick() {
							confirmationDialog.hide();
							if (isFromBatchList) {
								eventBus.fireEvent(new TabSelectionEvent(htmlPattern));
							} else
								moveToTab(htmlPattern);
						}

						@Override
						public void onCancelClick() {
							confirmationDialog.hide();
							if (isFromBatchList) {
								moveToTab(htmlPattern);
							}
							else {
								selectTab();
							}
								
						}
					});

				} else {
					moveToTab(htmlPattern);
				}
			}
		});
		this.tabBar.addTab(htmlUrl);

		tabs.put(this.tabBar.getTabCount() - 1, htmlPattern);

		selectTab();
	}

	public TabBar getTabBar() {
		return this.tabBar;
	}

	public void selectTab() {
		String href = Window.Location.getHref();
		String url = href.substring(href.lastIndexOf('/') + 1);

		Set<Map.Entry<Integer, String>> tabSet = tabs.entrySet();
		for (Map.Entry<Integer, String> entry : tabSet) {
			if (entry.getValue().equals(url) && this.tabBar.getSelectedTab() != entry.getKey()) {
				this.tabBar.selectTab(entry.getKey());
				break;
			}
		}
	}

	public void setUserName(String name) {
		this.userName.setText(LocaleDictionary.get().getConstantValue(LocaleCommonConstants.header_label_hi) + " " + name);
	}

	@UiHandler("signOutLink")
	void onSignOutClicked(ClickEvent event) {
		this.eventBus.fireEvent(new SignoutEvent());
	}

	private void moveToTab(String htmlPattern) {
		String href = Window.Location.getHref();
		String baseUrl = href.substring(0, href.lastIndexOf("/"));
		Window.Location.assign(baseUrl + "/" + htmlPattern);
	}

	public boolean showDialogBoxOnTabClick() {
		return dialogBoxOnTabClick;
	}

	public void setShowDialogBoxOnTabClick(boolean show) {
		dialogBoxOnTabClick = show;
	}

	public void setDialogMessage(String dialogMessage) {
		this.dialogMessage = dialogMessage;
	}

}
