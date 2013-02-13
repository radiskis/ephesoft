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

package com.ephesoft.dcma.gwt.foldermanager.client.presenter;

import java.util.List;

import com.ephesoft.dcma.core.common.FileWrapper;
import com.ephesoft.dcma.gwt.core.client.EphesoftAsyncCallback;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.foldermanager.client.FolderManagementController;
import com.ephesoft.dcma.gwt.foldermanager.client.event.BatchClassChangeEvent;
import com.ephesoft.dcma.gwt.foldermanager.client.event.BatchClassChangeEventHandler;
import com.ephesoft.dcma.gwt.foldermanager.client.event.FolderTableRefreshEvent;
import com.ephesoft.dcma.gwt.foldermanager.client.event.FolderTreeRefreshEvent;
import com.ephesoft.dcma.gwt.foldermanager.client.event.FolderTreeRefreshEventHandler;
import com.ephesoft.dcma.gwt.foldermanager.client.event.FolderUpEvent;
import com.ephesoft.dcma.gwt.foldermanager.client.event.FolderUpEventHandler;
import com.ephesoft.dcma.gwt.foldermanager.client.view.FolderSystemTreeView;
import com.ephesoft.dcma.gwt.foldermanager.shared.constant.FolderManagementConstants;
import com.ephesoft.dcma.gwt.foldermanager.shared.constant.FolderManagementMessages;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TreeItem;

public class FolderSystemTreePresenter extends AbstractFolderManagementPresenter<FolderSystemTreeView> {

	private final String parentFolderPath;
	private String selectedBatchClassID = "";
	private String parentFolderName;

	private TreeItem currentTreeItem;
	private final Label footer;

	// This is to show error dialog only once to user
	private boolean isErrorDialogShown = false;

	public FolderSystemTreePresenter(FolderManagementController controller, FolderSystemTreeView view, String parentFolderPath,
			Label footer) {
		super(controller, view);
		this.parentFolderPath = parentFolderPath;
		this.footer = footer;
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		eventBus.addHandler(FolderUpEvent.type, new FolderUpEventHandler() {

			@Override
			public void onFolderUp(FolderUpEvent tableRefreshEvent) {
				TreeItem parentItem = currentTreeItem.getParentItem();
				if (parentItem != null) {
					currentTreeItem = parentItem;
					view.getTree().setSelectedItem(currentTreeItem);
				} else {
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							FolderManagementMessages.NO_PARENT_FOLDER), true);
				}
			}
		});

		eventBus.addHandler(FolderTreeRefreshEvent.type, new FolderTreeRefreshEventHandler() {

			@Override
			public void refreshTree(FolderTreeRefreshEvent treeRefreshEvent) {
				if (view.getTree().getItemCount() == 0) {
					createTree();
				}
				boolean state = currentTreeItem.getState();
				currentTreeItem.setState(state, false);
				String fileName = treeRefreshEvent.getFileName();
				if (fileName != null) {
					for (int i = 0; i < currentTreeItem.getChildCount(); i++) {
						TreeItem child = currentTreeItem.getChild(i);
						if (child != null && child.getText().equals(fileName)) {
							currentTreeItem = child;
							break;
						}
					}
				}
				setFooterPath(treeRefreshEvent.getFolderPath());
				TreeItem parentItem = currentTreeItem.getParentItem();
				while (parentItem != null) {
					parentItem.setState(true, false);
					parentItem = parentItem.getParentItem();
				}
				view.getTree().setSelectedItem(currentTreeItem);
			}

		});

		eventBus.addHandler(BatchClassChangeEvent.type, new BatchClassChangeEventHandler() {

			@Override
			public void onBatchClassChange(BatchClassChangeEvent batchClassChangeEvent) {
				selectedBatchClassID = batchClassChangeEvent.getBatchClassID();
				parentFolderName = batchClassChangeEvent.getBatchClassName();
				createTree();
			}

		});
	}

	private void createTree() {
		if (selectedBatchClassID == null) {
			if (parentFolderName != null) {
				view.createFolderSystemTree(parentFolderName);
			}
		} else {
			view.createFolderSystemTree(selectedBatchClassID);
		}
	}

	public void refreshTableItems(String path) {
		controller.getEventBus().fireEvent(new FolderTableRefreshEvent(path));

	}

	@Override
	public void bind() {
		// no need to implement bind for this presenter.
	}

	public void fetchTreeItems(final TreeItem father, String path) {

		controller.getRpcService().getContents(path, new EphesoftAsyncCallback<List<FileWrapper>>() {

			@Override
			public void customFailure(Throwable throwable) {
				String errorMessage = throwable.getLocalizedMessage();
				if (!isErrorDialogShown) {
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							FolderManagementMessages.UNABLE_TO_LOAD_FOLDER_ITEMS), true);
					isErrorDialogShown = true;
				}
				if (null != father && errorMessage.contains(FolderManagementConstants.ERROR_TYPE_1)) {
					TreeItem parentItem = father.getParentItem();
					if (null != parentItem) {
						view.getTree().setSelectedItem(parentItem, true);
					}
					view.getTree().removeItem(father);
				}
			}

			@Override
			public void onSuccess(List<FileWrapper> results) {
				isErrorDialogShown = false;
				view.expandTreeItem(father, results);
			}
		});

	}

	public String getRelativePath(String path) {
		String relativePath = null;
		if (selectedBatchClassID != null) {
			relativePath = FolderManagementConstants.DOT + path.substring(parentFolderPath.length());
		} else {
			relativePath = FolderManagementConstants.DOT + path.substring(parentFolderPath.length() - parentFolderName.length() - 1);
		}
		return relativePath;
	}

	public String findPath(TreeItem item) {
		TreeItem parent = item.getParentItem();
		String path = null;
		if (parent == null) {
			path = parentFolderPath;
			if (selectedBatchClassID != null) {
				StringBuilder stringBuilder = new StringBuilder(path);
				stringBuilder.append(FolderManagementConstants.URL_SEPARATOR);
				stringBuilder.append(selectedBatchClassID);
				path = stringBuilder.toString();
			}
		} else {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(findPath(parent));
			stringBuilder.append(FolderManagementConstants.URL_SEPARATOR);
			stringBuilder.append(item.getText());
			path = stringBuilder.toString();
		}
		return path;
	}

	public String getSelectedBatchClassID() {
		return selectedBatchClassID;
	}

	public void setSelectedBatchClassID(String selectedBatchClassID) {
		this.selectedBatchClassID = selectedBatchClassID;
	}

	public void setCurrentTreeItem(TreeItem currentTreeItem) {
		this.currentTreeItem = currentTreeItem;
	}

	public TreeItem getCurrentTreeItem() {
		return currentTreeItem;
	}

	public void setFooterPath(String path) {
		if (path != null) {
			String pathString = "Current path : " + getRelativePath(path);
			footer.setText(pathString);
			footer.setTitle(pathString);
		}
	}
}
