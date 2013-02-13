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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.swfupload.client.File;
import org.swfupload.client.SWFUpload;
import org.swfupload.client.UploadBuilder;
import org.swfupload.client.SWFUpload.ButtonAction;
import org.swfupload.client.SWFUpload.ButtonCursor;
import org.swfupload.client.event.FileQueuedHandler;
import org.swfupload.client.event.UploadCompleteHandler;
import org.swfupload.client.event.UploadStartHandler;

import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.foldermanager.client.event.FolderTreeRefreshEvent;
import com.ephesoft.dcma.gwt.foldermanager.client.event.PathRefreshEvent;
import com.ephesoft.dcma.gwt.foldermanager.client.event.PathRefreshEventHandler;
import com.ephesoft.dcma.gwt.foldermanager.client.event.RemoveAttachmentEvent;
import com.ephesoft.dcma.gwt.foldermanager.client.event.RemoveAttachmentEventHandler;
import com.ephesoft.dcma.gwt.foldermanager.shared.constant.FolderManagementConstants;
import com.ephesoft.dcma.gwt.foldermanager.shared.constant.FolderManagementMessages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FolderUploadWidget extends Composite {

	private final SWFUpload swfUpload;
	private final HandlerManager eventBus;
	@UiField
	protected HorizontalPanel buttonPanel;

	@UiField
	protected HorizontalPanel labelPanel;

	@UiField
	protected Button viewAttachedFiles;

	@UiField
	protected Label uploadHere;

	@UiField
	protected Label uploadFilePlaceholder;

	@UiField
	protected Button upload;

	protected String folderPath;

	private final UploadBuilder uploadBuilder = new UploadBuilder();

	private final Map<Integer, Boolean> fileIndexVsToUploadMap = new HashMap<Integer, Boolean>();

	interface Binder extends UiBinder<VerticalPanel, FolderUploadWidget> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);

	public FolderUploadWidget(final HandlerManager eventBus) {
		super();
		initWidget(BINDER.createAndBindUi(this));
		this.eventBus = eventBus;
		this.eventBus.addHandler(PathRefreshEvent.type, new PathRefreshEventHandler() {

			@Override
			public void onFolderPathChange(PathRefreshEvent pathRefreshEvent) {
				folderPath = pathRefreshEvent.getFolderPath();
			}
		});

		this.eventBus.addHandler(RemoveAttachmentEvent.type, new RemoveAttachmentEventHandler() {

			@Override
			public void onRemoveAttachment(RemoveAttachmentEvent removeAttachedFolderEvent) {
				int fileIndex = removeAttachedFolderEvent.getFileIndex();
				fileIndexVsToUploadMap.put(fileIndex, false);
			}
		});

		uploadBuilder.requeueOnError(false);
		labelPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		buttonPanel.setSpacing(2);
		HTMLPanel panel = new HTMLPanel(FolderManagementConstants.DIV_ID_SWFUPLOAD_DIV);
		uploadFilePlaceholder.getElement().appendChild(panel.getElement());
		uploadFilePlaceholder.setHeight(FolderManagementConstants._25PX);
		RootPanel.get().add(this);
		viewAttachedFiles.setText(LocaleDictionary.get().getConstantValue(FolderManagementConstants.VIEW_ATTACHED_FILES));
		viewAttachedFiles.setTitle(LocaleDictionary.get().getMessageValue(FolderManagementMessages.VIEW_ATTACHED_FILES));

		upload.setText(LocaleDictionary.get().getConstantValue(FolderManagementConstants.UPLOAD));
		uploadHere.setText(LocaleDictionary.get().getConstantValue(FolderManagementConstants.UPLOAD_HERE));

		uploadBuilder.setButtonAction(ButtonAction.SELECT_FILES);
		uploadBuilder.setButtonCursor(ButtonCursor.HAND);
		uploadBuilder.setButtonImageURL(FolderManagementConstants.IMAGES_BROWSE_PNG);
		uploadBuilder.setButtonHeight(25);
		uploadBuilder.setButtonWidth(68);

		uploadBuilder.setButtonPlaceholderID(FolderManagementConstants.SWFUPLOAD);
		uploadBuilder.setFileTypes(FolderManagementConstants.FILE_TYPES);
		uploadBuilder.setFileTypesDescription(FolderManagementConstants.ALL_FILES);

		uploadBuilder.setUploadStartHandler(new UploadStartHandler() {

			@Override
			public void onUploadStart(UploadStartEvent event) {
				File file = event.getFile();
				fileIndexVsToUploadMap.put(file.getIndex(), false);
			}
		});

		uploadBuilder.setFileQueuedHandler(new FileQueuedHandler() {

			@Override
			public void onFileQueued(FileQueuedEvent event) {
				File file = event.getFile();
				fileIndexVsToUploadMap.put(file.getIndex(), true);
			}
		});

		uploadBuilder.setUploadCompleteHandler(new UploadCompleteHandler() {

			@Override
			public void onUploadComplete(UploadCompleteEvent event) {
				int fileLength = swfUpload.getStats().getFilesQueued();
				if (fileLength > 0) {
					eventBus.fireEvent(new FolderTreeRefreshEvent());
					swfUpload.startUpload();
				} else {
					ScreenMaskUtility.unmaskScreen();
					eventBus.fireEvent(new FolderTreeRefreshEvent());
				}
			}
		});
		uploadBuilder.requeueOnError(true);
		uploadBuilder.preventSWFCaching(true);
		swfUpload = uploadBuilder.build();
	}

	protected void setAndStartUpload(String uploadFormAction, String absolutePath) {
		StringBuffer urlBuffer = new StringBuffer(uploadFormAction);
		if (Window.Location.getHref().contains(FolderManagementConstants.QUESTION_MARK)) {
			urlBuffer.append(FolderManagementConstants.AMPERSAND);
		} else {
			urlBuffer.append(FolderManagementConstants.QUESTION_MARK);
		}
		urlBuffer.append(FolderManagementConstants.CURRENT_UPLOAD_FOLDER_NAME);
		urlBuffer.append(FolderManagementConstants.EQUALS);
		urlBuffer.append(URL.encode(absolutePath));
		swfUpload.setUploadURL(urlBuffer.toString());
		swfUpload.startUpload();
	}

	@UiHandler(value = {"upload"})
	public void onUploadClick(ClickEvent clickEvent) {
		Set<Entry<Integer, Boolean>> entrySet = fileIndexVsToUploadMap.entrySet();
		for (Entry<Integer, Boolean> entry : entrySet) {
			Boolean toUpload = entry.getValue();
			int index = entry.getKey();
			if (!toUpload) {
				File file = swfUpload.getFile(index);
				swfUpload.cancelUpload(file.getId(), false);
			}
		}
		fileIndexVsToUploadMap.clear();
		int fileLength = swfUpload.getStats().getFilesQueued();
		if (fileLength == 0) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					FolderManagementMessages.NO_FILES_ATTACHED_FOR_UPLOAD), true);
		} else {
			ScreenMaskUtility.maskScreen(LocaleDictionary.get().getMessageValue(FolderManagementMessages.UPLOADING_SELECTED_FILES));
			setAndStartUpload(FolderManagementConstants.UPLOAD_DOWNLOAD_SERVLET_PATH, folderPath);
		}

	}

	@UiHandler(value = {"viewAttachedFiles"})
	public void onViewAttachedFilesClick(ClickEvent clickEvent) {
		int index = 0;
		Map<Integer, String> fileIndexVsNameMap = new HashMap<Integer, String>();
		while (swfUpload.getFile(index) != null) {
			File file = swfUpload.getFile(index);
			String name = file.getName();
			Boolean uploadEnabled = fileIndexVsToUploadMap.get(index);
			if (uploadEnabled != null && uploadEnabled) {
				fileIndexVsNameMap.put(index, name);
			}
			index++;
		}
		DisplayDialogBox customDialogBox = new DisplayDialogBox(fileIndexVsNameMap, eventBus);
		customDialogBox.setText(LocaleDictionary.get().getMessageValue(FolderManagementMessages.LIST_OF_FILES_ATTACHED));
	}

	/**
	 * This method set the enable attribute for upload button.
	 * 
	 * @param enable to enable/disable
	 */
	public void setEnableUploadButton(boolean enable) {
		upload.setEnabled(enable);
	}
}
