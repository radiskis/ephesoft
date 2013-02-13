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

package com.ephesoft.dcma.gwt.core.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ExternalAppDialogBox extends DialogBox {

	private static final String CSS_CONFIGURABLE_DIALOG_BOX = "configurable-DialogBox";

	interface Binder extends UiBinder<Widget, ExternalAppDialogBox> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);

	private DialogBoxListener listener;

	public interface DialogBoxListener {

		public void onOkClick();

		public void onCloseClick();
	}

	@UiField
	VerticalPanel verticalPanel;
	
	private Frame frame;

	private Button okButton;

	private Button closeButton;
	
	public ExternalAppDialogBox(String url, int xDimension, int yDimension) {
		this(url, xDimension, yDimension, false, false, false, true);
	}
	
	
	public ExternalAppDialogBox(String url, int xDimension, int yDimension, boolean showOkButton, boolean showCloseButton,
			boolean center) {
		this(url, xDimension, yDimension, showOkButton, showCloseButton, center, true);
	}

	public ExternalAppDialogBox(String url, int xDimension, int yDimension, boolean showOkButton, boolean showCloseButton,
			boolean center, boolean isVisible) {
		setWidget(BINDER.createAndBindUi(this));
		addStyleName(CSS_CONFIGURABLE_DIALOG_BOX);
		setWidth(xDimension + "px");
		setHeight(yDimension + "px");
		frame = new Frame(url);
		frame.setWidth(xDimension + "px");
		frame.setHeight(yDimension + "px");
		okButton = new Button("Ok", new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				listener.onOkClick();
				hide();
			}
		});
		okButton.setTitle("Ctrl+a");
		closeButton = new Button("Close", new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				listener.onCloseClick();
				hide();
			}
		});
		closeButton.setTitle("Ctrl+z");
		okButton.getElement().setAttribute("id", "okButtonElement");
		closeButton.getElement().setAttribute("id", "closeButtonElement");
		okButton.setVisible(showOkButton);
		closeButton.setVisible(showCloseButton);
		HorizontalPanel horizontalButtonPanel = new HorizontalPanel();
		horizontalButtonPanel.setSpacing(5);
		if (showOkButton) {
			horizontalButtonPanel.add(okButton);
			horizontalButtonPanel.setCellHorizontalAlignment(okButton, HasHorizontalAlignment.ALIGN_CENTER);
		}
		if (showCloseButton) {
			horizontalButtonPanel.add(closeButton);
			horizontalButtonPanel.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_CENTER);
		}
		verticalPanel.add(frame);
		verticalPanel.setCellHorizontalAlignment(frame, HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.setCellHorizontalAlignment(horizontalButtonPanel, HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.add(horizontalButtonPanel);
		setModal(true);
		setVisible(true);
		if (center) {
			center();
		}
		if(isVisible) {
			show();
		}
	}

	@Override
	protected void onPreviewNativeEvent(NativePreviewEvent preview) {
		super.onPreviewNativeEvent(preview);

		NativeEvent evt = preview.getNativeEvent();

		if (evt.getCtrlKey()) {
			switch (evt.getKeyCode()) {
				case 'a':
				case 'A':
					evt.preventDefault();
					okButton.click();
				case 'z':
				case 'Z':
					evt.preventDefault();
					closeButton.click();
					break;
			}
		}

	}

	public void addDialogBoxListener(DialogBoxListener listener) {
		this.listener = listener;
	}

	public void setDialogTitle(String caption) {
		setText(caption);
	}
	
	public Frame getFrame() {
		return frame;
	}
}
