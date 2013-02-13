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

package com.ephesoft.dcma.gwt.foldermanager.client.view.widget;

import java.util.Map;
import java.util.Map.Entry;

import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.foldermanager.client.event.RemoveAttachmentEvent;
import com.ephesoft.dcma.gwt.foldermanager.shared.constant.FolderManagementConstants;
import com.ephesoft.dcma.gwt.foldermanager.shared.constant.FolderManagementMessages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class DisplayDialogBox extends DialogBox {

	private static final String _100PX = "100px";
	private static final String _200PX = "200px";

	interface Binder extends UiBinder<Widget, DisplayDialogBox> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);

	protected HandlerManager eventBus;

	@UiField
	protected VerticalPanel verticalPanel;

	private int totalCounter;

	public DisplayDialogBox(Map<Integer, String> fileIndexVsNameMap, final HandlerManager eventBus) {
		super();
		setWidget(BINDER.createAndBindUi(this));
		this.eventBus = eventBus;
		final VerticalPanel labelPanel = new VerticalPanel();
		if (fileIndexVsNameMap != null && !fileIndexVsNameMap.isEmpty()) {
			for (final Entry<Integer, String> labelString : fileIndexVsNameMap.entrySet()) {
				totalCounter++;
				final HorizontalPanel horizontalPanel = new HorizontalPanel();
				HTML html = new HTML(FolderManagementConstants.REMOVE_ICON_FOLDER_GIF);
				Label label = new Label(labelString.getValue());
				horizontalPanel.add(html);
				horizontalPanel.add(label);
				horizontalPanel.setSpacing(3);
				labelPanel.add(horizontalPanel);
				html.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent arg0) {
						labelPanel.remove(horizontalPanel);
						if (--totalCounter == 0) {
							addNoFilesLabel(labelPanel);
						}
						eventBus.fireEvent(new RemoveAttachmentEvent(labelString.getKey()));
					}

				});

			}
		} else {
			addNoFilesLabel(labelPanel);
		}
		addStyleName(FolderManagementConstants.CSS_CONFIGURABLE_DIALOG_BOX);
		Button closeButton = new Button(LocaleDictionary.get().getConstantValue(FolderManagementConstants.CLOSE), new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				hide();
			}
		});
		HorizontalPanel horizontalButtonPanel = new HorizontalPanel();
		horizontalButtonPanel.add(closeButton);
		horizontalButtonPanel.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_CENTER);
		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.add(labelPanel);
		scrollPanel.setWidth(_200PX);
		scrollPanel.setHeight(_200PX);
		scrollPanel.addStyleName(FolderManagementConstants.BORDER);
		setHeight(_100PX);
		verticalPanel.add(scrollPanel);
		verticalPanel.add(horizontalButtonPanel);
		verticalPanel.setCellHorizontalAlignment(horizontalButtonPanel, HasHorizontalAlignment.ALIGN_CENTER);
		setModal(true);
		setVisible(true);
		show();
		center();
		closeButton.setFocus(true);
	}

	private void addNoFilesLabel(final VerticalPanel labelPanel) {
		labelPanel.add(new Label(LocaleDictionary.get().getMessageValue(FolderManagementMessages.NO_FILES_ATTACHED)));
	}

	@Override
	protected void onPreviewNativeEvent(NativePreviewEvent preview) {
		super.onPreviewNativeEvent(preview);

		NativeEvent evt = preview.getNativeEvent();
		if (evt.getType().equals("keydown") && evt.getKeyCode() == KeyCodes.KEY_ESCAPE) {
			hide();
		}
	}
}
