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

package com.ephesoft.dcma.gwt.foldermanager.client.view;

import java.util.Map;

import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.foldermanager.client.FolderManagementController;
import com.ephesoft.dcma.gwt.foldermanager.client.presenter.FolderManagementPresenter;
import com.ephesoft.dcma.gwt.foldermanager.client.presenter.FolderSystemTreePresenter;
import com.ephesoft.dcma.gwt.foldermanager.client.presenter.FolderTablePresenter;
import com.ephesoft.dcma.gwt.foldermanager.client.view.widget.FolderSelectionWidget;
import com.ephesoft.dcma.gwt.foldermanager.shared.constant.FolderManagementConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;

public class FolderManagementView extends View<FolderManagementPresenter> {

	@UiField
	protected DockLayoutPanel mainPanel;

	interface Binder extends UiBinder<DockLayoutPanel, FolderManagementView> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);

	private FolderTableView folderTableView;
	private FolderSystemTreeView folderSystemTreeView;
	private FolderSystemTreePresenter folderSystemTreePresenter;
	private FolderTablePresenter folderTablePresenter;

	public FolderManagementView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		mainPanel.addStyleName(FolderManagementConstants.MAIN_BODY_BOX);
	}

	public void setInitialFolderManagementView(FolderManagementController controller, String parentFolderPath, String baseFolderUrl,
			Map<String, String> batchClassesMap) {
		Label footer = new Label();
		folderTableView = new FolderTableView(footer);

		folderTablePresenter = new FolderTablePresenter(controller, folderTableView, parentFolderPath, baseFolderUrl);

		folderSystemTreeView = new FolderSystemTreeView();

		folderSystemTreePresenter = new FolderSystemTreePresenter(controller, folderSystemTreeView, parentFolderPath, footer);

		SplitLayoutPanel mainContentPanel = new SplitLayoutPanel();
		DockLayoutPanel leftLayoutPanel = new DockLayoutPanel(Unit.PCT);
		ScrollPanel treeScrollPanel = new ScrollPanel();
		treeScrollPanel.add(folderSystemTreeView);
		FolderSelectionWidget folderSelectionWidget = new FolderSelectionWidget(batchClassesMap, controller.getEventBus());
		leftLayoutPanel.addNorth(folderSelectionWidget, 10);
		leftLayoutPanel.add(treeScrollPanel);
		mainContentPanel.addWest(leftLayoutPanel, 200);
		DockLayoutPanel contentMainPanel = new DockLayoutPanel(Unit.PCT);
		contentMainPanel.add(folderTableView);
		contentMainPanel.addStyleName(FolderManagementConstants.WHITE_BACKGROUND);
		mainContentPanel.add(contentMainPanel);
		mainPanel.add(mainContentPanel);
		ScreenMaskUtility.unmaskScreen();
	}

	public void refreshTableItems(String path) {
		folderTablePresenter.refreshContent(path);
	}

	public FolderTableView getFolderTableView() {
		return folderTableView;
	}

	public void setFolderTableView(FolderTableView folderTableView) {
		this.folderTableView = folderTableView;
	}

	public FolderSystemTreeView getFolderSystemTreeView() {
		return folderSystemTreeView;
	}

	public void setFolderSystemTreeView(FolderSystemTreeView folderSystemTreeView) {
		this.folderSystemTreeView = folderSystemTreeView;
	}

	public void setFolderSystemTreePresenter(FolderSystemTreePresenter folderSystemTreePresenter) {
		this.folderSystemTreePresenter = folderSystemTreePresenter;
	}

	public FolderSystemTreePresenter getFolderSystemTreePresenter() {
		return folderSystemTreePresenter;
	}
}
