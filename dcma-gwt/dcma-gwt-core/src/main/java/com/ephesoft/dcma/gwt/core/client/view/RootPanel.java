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

import com.ephesoft.dcma.gwt.core.client.DCMARemoteServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Panel;

public class RootPanel extends Composite {

	interface Binder extends UiBinder<DockLayoutPanel, RootPanel> {
	}

	@UiField
	protected Header header;
	@UiField(provided = true)
	protected Panel body;
	@UiField
	protected Footer footer;

	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Only body argument constructor.
	 * 
	 * @param body
	 */
	public RootPanel(Panel body) {
		this(body, null);
	}

	/**
	 * @param body
	 */
	private void initializeView(Panel body) {
		this.body = body;
		initWidget(BINDER.createAndBindUi(this));
	}

	/**
	 * Body and Rpc Service constructor used to load footer from server side.
	 * 
	 * @param body
	 * @param rpcService
	 */
	public RootPanel(final Panel body, DCMARemoteServiceAsync rpcService) {
		this(body, rpcService, true);
	}

	/**
	 * Constructor used for enabling the use of root panel for Login page. Future use.
	 * 
	 * @param body
	 * @param rpcService
	 * @param showHeader
	 */
	public RootPanel(final Panel body, DCMARemoteServiceAsync rpcService, boolean showHeader) {
		super();
		initializeView(body);
		if (showHeader) {
			footer.setFooterContent(rpcService);
		}
	}

	public Header getHeader() {
		return header;
	}
}
