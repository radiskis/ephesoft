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

package com.ephesoft.dcma.gwt.customWorkflow.client.view;

import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.customWorkflow.client.i18n.CustomWorkflowConstants;
import com.ephesoft.dcma.gwt.customWorkflow.client.i18n.CustomWorkflowMessages;
import com.ephesoft.dcma.gwt.customWorkflow.client.presenter.ImportPluginPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;

public class ImportPluginView extends View<ImportPluginPresenter> {

	private static final char SERVLET_RESULT_SEPERATOR = '|';

	private static final String LAST_ATTACHED_ZIP_SOURCE_PATH = "lastAttachedZipSourcePath=";

	private static final String ZIP = "zip";

	private static final String ATTACH_FORM_ACTION = "dcma-gwt-custom-workflow/importPluginUploadServlet?";

	interface Binder extends UiBinder<VerticalPanel, ImportPluginView> {
	}

	@UiField
	protected FileUpload importFile;

	@UiField
	protected Button saveButton;
	@UiField
	protected Button cancelButton;

	@UiField
	protected FormPanel attachZipFilePanel;
	@UiField
	protected Label importLabel;

	@UiField
	protected Label importStar;

	@UiField
	protected HorizontalPanel importBatchPanel;

	private DialogBox dialogBox;

	private Hidden zipFileName = new Hidden("zipFileName", "");

	private Hidden jarFileName = new Hidden("jarFileName", "");

	private Hidden xmlFileName = new Hidden("xmlFileName", "");

	@UiField
	protected VerticalPanel importBatchClassViewPanel;

	private static final Binder BINDER = GWT.create(Binder.class);

	public ImportPluginView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		saveButton.setText(CustomWorkflowConstants.SAVE_BUTTON);
		cancelButton.setText(CustomWorkflowConstants.CANCEL_BUTTON);

		importLabel.setText(CustomWorkflowConstants.IMPORT_FILE);

		importStar.setText(CustomWorkflowConstants.STAR);

		importLabel.setStyleName(CustomWorkflowConstants.BOLD_TEXT_STYLE);

		importStar.setStyleName(CustomWorkflowConstants.FONT_RED_STYLE);

		importBatchPanel.setSpacing(10);

		attachZipFilePanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		attachZipFilePanel.setMethod(FormPanel.METHOD_POST);
		attachZipFilePanel.setAction(ATTACH_FORM_ACTION);

		attachZipFilePanel.addSubmitHandler(new SubmitHandler() {

			@Override
			public void onSubmit(SubmitEvent event) {
				String fileName = importFile.getFilename();
				String fileExt = fileName.substring(fileName.lastIndexOf('.') + 1);
				if (!fileExt.equalsIgnoreCase(ZIP)) {
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
							CustomWorkflowMessages.IMPORT_FILE_INVALID_TYPE));
					ScreenMaskUtility.unmaskScreen();
					event.cancel();
				} else {
					String lastAttachedZipSourcePath = LAST_ATTACHED_ZIP_SOURCE_PATH + importFile.getFilename();
					attachZipFilePanel.setAction(ATTACH_FORM_ACTION + lastAttachedZipSourcePath);
				}
			}
		});

		attachZipFilePanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {

				String result = event.getResults();
				if (result.toLowerCase().indexOf(CustomWorkflowConstants.ERROR_CODE_TEXT) > -1) {
					ConfirmationDialogUtil.showConfirmationDialogError((result.substring(result.indexOf(CustomWorkflowConstants.CAUSE)
							+ CustomWorkflowConstants.CAUSE.length())));
					// getDialogBox().hide(true);
					ScreenMaskUtility.unmaskScreen();
					return;
				}
				String keyPluginName = CustomWorkflowConstants.PLUGIN_NAME;
				String keyXmlFolderPath = CustomWorkflowConstants.XML_FILE_PATH;
				String keyJarFolderPath = CustomWorkflowConstants.JAR_FILE_PATH;
				String pluginName = result.substring(result.indexOf(keyPluginName) + keyPluginName.length(), result.indexOf(
						SERVLET_RESULT_SEPERATOR, result.indexOf(keyPluginName)));
				String xmlSourcePath = result.substring(result.indexOf(keyXmlFolderPath) + keyXmlFolderPath.length(), result.indexOf(
						SERVLET_RESULT_SEPERATOR, result.indexOf(keyXmlFolderPath)));
				String jarSourcePath = result.substring(result.indexOf(keyJarFolderPath) + keyXmlFolderPath.length(), result.indexOf(
						SERVLET_RESULT_SEPERATOR, result.indexOf(keyJarFolderPath)));

				zipFileName.setValue(pluginName);
				jarFileName.setValue(jarSourcePath);
				xmlFileName.setValue(xmlSourcePath);

				dialogBox.center();

				presenter.onSaveClicked(zipFileName.getValue(), xmlFileName.getValue(), jarFileName.getValue());

			}
		});
	}

	public Button getSaveButton() {
		return saveButton;
	}

	public Button getCancelButton() {
		return cancelButton;
	}

	public DialogBox getDialogBox() {
		return dialogBox;
	}

	public void setDialogBox(DialogBox dialogBox) {
		this.dialogBox = dialogBox;
	}

	public void setImportFile(FileUpload importFile) {
		this.importFile = importFile;
	}

	public FileUpload getImportFile() {
		return importFile;
	}

	@UiHandler("saveButton")
	public void onOkClick(ClickEvent clickEvent) {
		attachFile();

	}

	@UiHandler("cancelButton")
	public void onCancelClick(ClickEvent clickEvent) {
		dialogBox.hide(true);
	}

	private void attachFile() {
		ScreenMaskUtility.maskScreen();
		attachZipFilePanel.submit();
	}

}
