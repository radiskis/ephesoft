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

package com.ephesoft.dcma.gwt.admin.bm.client.view.batch;

import java.util.Map;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.MessageConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.batch.ExportBatchClassPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.shared.BatchFolderListDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;

/**
 * This class provides functionality to export batch class view.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class ExportBatchClassView extends View<ExportBatchClassPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, ExportBatchClassView> {
	}

	/**
	 * saveButton Button.
	 */
	@UiField
	protected Button saveButton;

	/**
	 * cancelButton Button.
	 */
	@UiField
	protected Button cancelButton;

	/**
	 * exportBatchClassViewPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel exportBatchClassViewPanel;

	/**
	 * dialogBox DialogBox.
	 */
	private DialogBox dialogBox;

	/**
	 * exportBatchClassIdentifier String.
	 */
	private String exportBatchClassIdentifier;

	/**
	 * exportFormPanel FormPanel.
	 */
	@UiField
	protected FormPanel exportFormPanel;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Constructor.
	 */
	public ExportBatchClassView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		saveButton.setText(AdminConstants.SAVE_BUTTON);
		cancelButton.setText(AdminConstants.CANCEL_BUTTON);

		final Hidden exportIdentifierHidden = new Hidden("identifier");
		exportBatchClassViewPanel.add(exportIdentifierHidden);

		exportBatchClassViewPanel.setSpacing(BatchClassManagementConstants.TEN);

		exportFormPanel.setMethod(FormPanel.METHOD_POST);
		exportFormPanel.setAction("dcma-gwt-admin/exportBatchClassDownload");

		exportFormPanel.addSubmitHandler(new SubmitHandler() {

			@Override
			public void onSubmit(SubmitEvent event) {
				exportIdentifierHidden.setValue(getExportBatchClassIdentifier());

			}
		});

		exportFormPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				if (event.getResults().toLowerCase().indexOf(AdminConstants.ERROR_CODE_TEXT) > -1) {
					ConfirmationDialogUtil.showConfirmationDialogSuccess(MessageConstants.EXPORT_UNSUCCESSFUL);
					getDialogBox().hide(true);
					return;
				}
			}
		});

	}

	/**
	 * To get batch Folder List View.
	 * 
	 * @param propertyMap Map<String, String>
	 */
	public void getbatchFolderListView(Map<String, String> propertyMap) {
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.addStyleName("width100");

		CheckBox checkBox = new CheckBox(propertyMap.get(BatchFolderListDTO.FOLDER_NAME));
		checkBox.setName(propertyMap.get(BatchFolderListDTO.FOLDER_NAME));
		checkBox.setFormValue(propertyMap.get(BatchFolderListDTO.FOLDER_NAME));
		checkBox.setEnabled(Boolean.parseBoolean(propertyMap.get(BatchFolderListDTO.ENABLED)));
		checkBox.setChecked(Boolean.parseBoolean(propertyMap.get(BatchFolderListDTO.CHECKED)));
		horizontalPanel.add(checkBox);
		exportBatchClassViewPanel.add(horizontalPanel);
	}

	/**
	 * To get Save Button.
	 * 
	 * @return Button
	 */
	public Button getSaveButton() {
		return saveButton;
	}

	/**
	 * To set Save Button.
	 * 
	 * @param saveButton Button
	 */
	public void setSaveButton(Button saveButton) {
		this.saveButton = saveButton;
	}

	/**
	 * To get Cancel Button.
	 * 
	 * @return Button
	 */
	public Button getCancelButton() {
		return cancelButton;
	}

	/**
	 * To set Cancel Button.
	 * 
	 * @param cancelButton Button
	 */
	public void setCancelButton(Button cancelButton) {
		this.cancelButton = cancelButton;
	}

	/**
	 * To get Export Batch Class View Panel.
	 * 
	 * @return VerticalPanel
	 */
	public VerticalPanel getExportBatchClassViewPanel() {
		return exportBatchClassViewPanel;
	}

	/**
	 * To set Export Batch Class View Panel.
	 * 
	 * @param exportBatchClassViewPanel VerticalPanel
	 */
	public void setExportBatchClassViewPanel(VerticalPanel exportBatchClassViewPanel) {
		this.exportBatchClassViewPanel = exportBatchClassViewPanel;
	}

	/**
	 * To get Dialog Box.
	 * 
	 * @return DialogBox
	 */
	public DialogBox getDialogBox() {
		return dialogBox;
	}

	/**
	 * To set Dialog Box.
	 * 
	 * @param dialogBox DialogBox
	 */
	public void setDialogBox(DialogBox dialogBox) {
		this.dialogBox = dialogBox;
	}

	/**
	 * To get Export Form Panel.
	 * 
	 * @return FormPanel
	 */
	public FormPanel getExportFormPanel() {
		return exportFormPanel;
	}

	/**
	 * To set Export Form Panel.
	 * 
	 * @param exportFormPanel FormPanel
	 */
	public void setExportFormPanel(FormPanel exportFormPanel) {
		this.exportFormPanel = exportFormPanel;
	}

	/**
	 * To set Export Batch Class Identifier.
	 * 
	 * @param exportBatchClassIdentifier String
	 */
	public void setExportBatchClassIdentifier(String exportBatchClassIdentifier) {
		this.exportBatchClassIdentifier = exportBatchClassIdentifier;
	}

	/**
	 * To get Export Batch Class Identifier.
	 * 
	 * @return String
	 */
	public String getExportBatchClassIdentifier() {
		return exportBatchClassIdentifier;
	}

	/**
	 * To perform operations on OK click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("saveButton")
	public void onOkClick(ClickEvent clickEvent) {
		presenter.onOkClicked();

	}

	/**
	 * To perform operations on cancel click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("cancelButton")
	public void onCancelClick(ClickEvent clickEvent) {
		dialogBox.hide(true);
	}
}
