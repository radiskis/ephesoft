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

import java.util.List;

import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.FileWrapper;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.foldermanager.client.presenter.FolderSystemTreePresenter;
import com.ephesoft.dcma.gwt.foldermanager.shared.constant.FolderManagementMessages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class FolderSystemTreeView extends View<FolderSystemTreePresenter> {

	@UiField
	protected Tree tree;

	interface Binder extends UiBinder<Tree, FolderSystemTreeView> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);

	public FolderSystemTreeView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
	}

	public void expandTreeItem(TreeItem father, List<FileWrapper> files) {
		father.removeItems();
		for (FileWrapper file : files) {
			if (file.getKind() == FileType.DIR) {
				TreeItem newItem = new TreeItem(file.getName());
				father.addItem(newItem);
				if (file.isSubFolderContained()) {
					newItem.addItem(LocaleDictionary.get().getMessageValue(FolderManagementMessages.LOADING));
				}
			}
		}
	}

	public void createFolderSystemTree(final String parentFolderName) {
		tree.clear();
		tree.setAnimationEnabled(true);

		TreeItem tItem = new TreeItem(parentFolderName);
		tItem.addItem(LocaleDictionary.get().getMessageValue(FolderManagementMessages.LOADING));
		tree.addOpenHandler(new OpenHandler<TreeItem>() {

			@Override
			public void onOpen(OpenEvent<TreeItem> openEvent) {
				TreeItem targetItem = (TreeItem) openEvent.getTarget();
				String path = findPath(targetItem);
				fetchTreeItems(targetItem, path);
				presenter.setCurrentTreeItem(targetItem);
			}
		});
		tree.addSelectionHandler(new SelectionHandler<TreeItem>() {

			@Override
			public void onSelection(SelectionEvent<TreeItem> selectionEvent) {
				TreeItem selectedItem = (TreeItem) selectionEvent.getSelectedItem();
				String path = findPath(selectedItem);
				presenter.refreshTableItems(path);
				fetchTreeItems(selectedItem, path);
				presenter.setFooterPath(path);
				presenter.setCurrentTreeItem(selectedItem);
			}
		});
		tree.addItem(tItem);
		tree.setSelectedItem(tItem, true);
	}

	public void fetchTreeItems(final TreeItem father, String path) {
		presenter.fetchTreeItems(father, path);
	}

	public String findPath(TreeItem item) {
		return presenter.findPath(item);
	}

	public void setTree(Tree tree) {
		this.tree = tree;
	}

	public Tree getTree() {
		return tree;
	}

}
