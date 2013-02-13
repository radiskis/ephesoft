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

import java.util.List;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.MessageConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.batch.ImportBatchClassPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.client.validator.EmptyStringValidator;
import com.ephesoft.dcma.gwt.core.client.validator.ValidatableWidget;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.ImportBatchClassUserOptionDTO;
import com.ephesoft.dcma.gwt.core.shared.StringUtil;
import com.ephesoft.dcma.gwt.core.shared.UNCFolderConfig;
import com.ephesoft.dcma.gwt.core.shared.importtree.Node;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;

/**
 * This class provides functionality to import batch class view.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class ImportBatchClassView extends View<ImportBatchClassPresenter> {

	/**
	 * ATTACH_FORM_ACTION String.
	 */
	private static final String ATTACH_FORM_ACTION = "dcma-gwt-admin/importBatchClassUpload?";

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, ImportBatchClassView> {
	}

	/**
	 * priority TextBox.
	 */
	@UiField
	protected TextBox priority;

	/**
	 * description TextBox.
	 */
	@UiField
	protected TextBox description;

	/**
	 * name TextBox.
	 */
	@UiField
	protected TextBox name;

	/**
	 * importFile FileUpload.
	 */
	@UiField
	protected FileUpload importFile;

	/**
	 * useSource CheckBox.
	 */
	@UiField
	protected CheckBox useSource;

	/**
	 * useExisting CheckBox.
	 */
	@UiField
	protected CheckBox useExisting;

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
	 * attachZipFilePanel FormPanel.
	 */
	@UiField
	protected FormPanel attachZipFilePanel;

	/**
	 * attachButton Button.
	 */
	@UiField
	protected Button attachButton;

	/**
	 * importPanel DockLayoutPanel.
	 */
	@UiField
	protected DockLayoutPanel importPanel;

	/**
	 * priorityLabel Label.
	 */
	@UiField
	protected Label priorityLabel;

	/**
	 * descLabel Label.
	 */
	@UiField
	protected Label descLabel;

	/**
	 * nameLabel Label.
	 */
	@UiField
	protected Label nameLabel;

	/**
	 * uncLabel Label.
	 */
	@UiField
	protected Label uncLabel;

	/**
	 * importLabel Label.
	 */
	@UiField
	protected Label importLabel;

	/**
	 * errorMessage Label.
	 */
	@UiField
	protected Label errorMessage;

	/**
	 * priorityStar Label.
	 */
	@UiField
	protected Label priorityStar;

	/**
	 * uncStar Label.
	 */
	@UiField
	protected Label uncStar;

	/**
	 * importStar Label.
	 */
	@UiField
	protected Label importStar;

	/**
	 * importBatchPanel HorizontalPanel.
	 */
	@UiField
	protected HorizontalPanel importBatchPanel;

	/**
	 * descStar Label.
	 */
	@UiField
	protected Label descStar;

	/**
	 * nameStar Label.
	 */
	@UiField
	protected Label nameStar;

	/**
	 * importFolderListViewPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel importFolderListViewPanel;

	/**
	 * uncPanel HorizontalPanel.
	 */
	@UiField
	protected HorizontalPanel uncPanel;

	/**
	 * batchClassFolderView Tree.
	 */
	@UiField
	protected Tree batchClassFolderView;

	/**
	 * uncFolderList ListBox.
	 */
	private final ListBox uncFolderList;

	/**
	 * uncFolder TextBox.
	 */
	private final TextBox uncFolder;

	/**
	 * validateTextBox ValidatableWidget<TextBox>.
	 */
	private final ValidatableWidget<TextBox> validateTextBox;

	/**
	 * validateDescTextBox ValidatableWidget<TextBox>.
	 */
	private final ValidatableWidget<TextBox> validateDescTextBox;

	/**
	 * validateUNCTextBox ValidatableWidget<TextBox>.
	 */
	private final ValidatableWidget<TextBox> validateUNCTextBox;

	/**
	 * validateNameTextBox ValidatableWidget<TextBox>.
	 */
	private final ValidatableWidget<TextBox> validateNameTextBox;

	/**
	 * importBatchClassUserOptionDTO ImportBatchClassUserOptionDTO.
	 */
	private final ImportBatchClassUserOptionDTO importBatchClassUserOptionDTO;

	/**
	 * dialogBox DialogBox.
	 */
	private DialogBox dialogBox;

	/**
	 * zipImported boolean.
	 */
	private boolean zipImported = false;

	/**
	 * zipWorkflowName String.
	 */
	private String zipWorkflowName = "";

	/**
	 * importExisting Hidden.
	 */
	private Hidden importExisting = new Hidden("importExisting", "false");

	/**
	 * To set Import Existing.
	 * 
	 * @param importExisting Hidden
	 */
	public void setImportExisting(Hidden importExisting) {
		this.importExisting = importExisting;
	}

	/**
	 * importBatchClassViewPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel importBatchClassViewPanel;

	/**
	 * errorText String.
	 */
	private String errorText = AdminConstants.EMPTY_STRING;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * TRUE String.
	 */
	private static final String TRUE = "true";

	/**
	 * FALSE String.
	 */
	private static final String FALSE = "false";

	/**
	 * systemFolder TextBox.
	 */
	@UiField
	protected TextBox systemFolder;

	/**
	 * systemFolderStar Label.
	 */
	@UiField
	protected Label systemFolderStar;

	/**
	 * systemFolderLabel Label.
	 */
	@UiField
	protected Label systemFolderLabel;

	/**
	 * validateSystemFolderTextBox ValidatableWidget<TextBox>.
	 */
	private final ValidatableWidget<TextBox> validateSystemFolderTextBox;

	/**
	 * Constructor.
	 */
	public ImportBatchClassView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		saveButton.setText(AdminConstants.SAVE_BUTTON);
		cancelButton.setText(AdminConstants.CANCEL_BUTTON);
		attachButton.setText(AdminConstants.ATTACH_BUTTON);
		attachButton.setStyleName("attach-button");
		attachButton.addStyleName("disabled");
		attachButton.removeStyleName("enabled");
		attachButton.setEnabled(Boolean.FALSE);
		errorMessage.addStyleName("error_style");

		uncFolder = new TextBox();
		uncFolder.setName("uncFolder");
		uncFolderList = new ListBox();
		uncFolderList.setName("uncFolderList");
		uncFolderList.setWidth("145px");
		uncFolder.setWidth("140px");
		importBatchClassUserOptionDTO = new ImportBatchClassUserOptionDTO();
		importBatchClassUserOptionDTO.setUseSource(Boolean.TRUE);
		importBatchClassUserOptionDTO.setImportExisting(Boolean.FALSE);

		validateTextBox = new ValidatableWidget<TextBox>(priority);
		validateTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateTextBox.toggleValidDateBox();
				importBatchClassUserOptionDTO.setPriority(priority.getText());
			}
		});
		validateTextBox.addValidator(new EmptyStringValidator(priority));

		validateDescTextBox = new ValidatableWidget<TextBox>(description);
		validateDescTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateDescTextBox.toggleValidDateBox();
				importBatchClassUserOptionDTO.setDescription(description.getText());
			}
		});
		validateDescTextBox.addValidator(new EmptyStringValidator(description));

		validateUNCTextBox = new ValidatableWidget<TextBox>(uncFolder);
		validateUNCTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> arg0) {
				validateUNCTextBox.toggleValidDateBox();
				importBatchClassUserOptionDTO.setUncFolder(uncFolder.getText().trim());
			}
		});
		validateUNCTextBox.addValidator(new EmptyStringValidator(uncFolder));

		uncFolderList.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent arg0) {
				String selectedUNCFolder = uncFolderList.getValue(uncFolderList.getSelectedIndex());
				importBatchClassUserOptionDTO.setUncFolder(selectedUNCFolder);
				final String selectedBatchName = presenter.getSelectedBatchName(selectedUNCFolder);
				name.setText(selectedBatchName);
				systemFolder.setText(presenter.getSelectedBatchClassSystemFolder(selectedBatchName));
			}
		});

		validateNameTextBox = new ValidatableWidget<TextBox>(name);
		validateNameTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> arg0) {
				validateNameTextBox.toggleValidDateBox();

			}
		});
		validateNameTextBox.addValidator(new EmptyStringValidator(name));

		importBatchClassViewPanel.setSpacing(BatchClassManagementConstants.FIVE);

		nameLabel.setText(AdminConstants.NAME);
		priorityLabel.setText(AdminConstants.PRIORITY);
		descLabel.setText(AdminConstants.DESCRIPTION);
		uncLabel.setText(AdminConstants.UNC_FOLDER);
		importLabel.setText(AdminConstants.IMPORT_FILE);

		uncStar.setText(AdminConstants.STAR);
		importStar.setText(AdminConstants.STAR);
		nameStar.setText(AdminConstants.STAR);

		nameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		priorityLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		descLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		uncLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		importLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);

		priorityStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		descStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		uncStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		importStar.setStyleName(AdminConstants.FONT_RED_STYLE);
		nameStar.setStyleName(AdminConstants.FONT_RED_STYLE);

		importBatchPanel.setSpacing(BatchClassManagementConstants.TEN);

		attachZipFilePanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		attachZipFilePanel.setMethod(FormPanel.METHOD_POST);
		attachZipFilePanel.setAction(ATTACH_FORM_ACTION);

		importFolderListViewPanel.add(importExisting);
		importPanel.setVisible(Boolean.FALSE);

		useExisting.setValue(Boolean.FALSE);
		useSource.setVisible(false);

		systemFolderLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.SYSTEM_FOLDER)
				+ AdminConstants.COLON);
		systemFolderLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		systemFolderStar.setText(AdminConstants.STAR);
		systemFolderStar.setStyleName(AdminConstants.FONT_RED_STYLE);

		validateSystemFolderTextBox = new ValidatableWidget<TextBox>(systemFolder);
		validateSystemFolderTextBox.getWidget().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				validateSystemFolderTextBox.toggleValidDateBox();
				importBatchClassUserOptionDTO.setSystemFolder(systemFolder.getText().trim());
			}
		});

		if (!importBatchClassUserOptionDTO.isImportExisting()) {
			name.setEnabled(Boolean.TRUE);
			name.setText(importBatchClassUserOptionDTO.getName());
			systemFolder.setText(importBatchClassUserOptionDTO.getSystemFolder());
		}
		description.setEnabled(true);
		priority.setEnabled(true);
		descStar.setText(AdminConstants.STAR);
		priorityStar.setText(AdminConstants.STAR);
		nameStar.setText(AdminConstants.STAR);

		uncPanel.add(uncFolder);

		importFile.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent arg0) {
				attachButton.setStyleName("attach-button");
				attachButton.setEnabled(Boolean.TRUE);
			}
		});

		useExisting.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				boolean checked = event.getValue();
				importBatchClassUserOptionDTO.setImportExisting(Boolean.valueOf(checked));
				uncPanel.clear();
				if (checked) {
					name.setEnabled(Boolean.TRUE);
					uncPanel.add(uncFolderList);
					importExisting.setValue(TRUE);
					final String selectedBatchName = presenter.getSelectedBatchName(uncFolderList.getValue(0));
					name.setText(selectedBatchName);
					systemFolder.setText(presenter.getSelectedBatchClassSystemFolder(selectedBatchName));
					errorMessage.setText(AdminConstants.EMPTY_STRING);
				} else {
					if (!importBatchClassUserOptionDTO.isUseSource()) {
						name.setEnabled(Boolean.TRUE);
						name.setText(importBatchClassUserOptionDTO.getName());
					}
					name.setText(importBatchClassUserOptionDTO.getName());
					uncPanel.add(uncFolder);
					uncFolder.setText(AdminConstants.EMPTY_STRING);
					importExisting.setValue(FALSE);
					final String tempSystemFolder = importBatchClassUserOptionDTO.getSystemFolder();
					if (tempSystemFolder == null) {
						systemFolder.setText(BatchClassManagementConstants.EMPTY_STRING);
					} else {
						systemFolder.setText(tempSystemFolder);
					}
				}
				presenter.setFolderList();
			}
		});

		useSource.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				boolean checked = ((CheckBox) event.getSource()).getValue();
				importBatchClassUserOptionDTO.setUseSource(Boolean.valueOf(checked));
				if (checked) {
					name.setEnabled(Boolean.FALSE);
					description.setEnabled(false);
					description.setText(BatchClassManagementConstants.EMPTY_STRING);
					name.setText(importBatchClassUserOptionDTO.getName());
					priority.setEnabled(false);
					priority.setText(BatchClassManagementConstants.EMPTY_STRING);
					descStar.setText(BatchClassManagementConstants.EMPTY_STRING);
					priorityStar.setText(BatchClassManagementConstants.EMPTY_STRING);
					nameStar.setText(BatchClassManagementConstants.EMPTY_STRING);
				} else {
					if (!importBatchClassUserOptionDTO.isImportExisting()) {
						name.setEnabled(Boolean.TRUE);
						name.setText(importBatchClassUserOptionDTO.getName());
					}
					description.setEnabled(true);
					priority.setEnabled(true);
					descStar.setText(AdminConstants.STAR);
					priorityStar.setText(AdminConstants.STAR);
					nameStar.setText(AdminConstants.STAR);
				}
			}
		});

		attachZipFilePanel.addSubmitHandler(new SubmitHandler() {

			@Override
			public void onSubmit(SubmitEvent event) {
				String fileName = importFile.getFilename();
				String fileExt = fileName.substring(fileName.lastIndexOf(BatchClassManagementConstants.DOT) + 1);
				if (!fileExt.equalsIgnoreCase("zip")) {
					ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.IMPORT_FILE_INVALID_TYPE);
					ScreenMaskUtility.unmaskScreen();
					event.cancel();
				} else {
					String lastAttachedZipSourcePath = "lastAttachedZipSourcePath=" + importBatchClassUserOptionDTO.getZipFileName();
					attachZipFilePanel.setAction(ATTACH_FORM_ACTION + lastAttachedZipSourcePath);
				}
			}
		});

		attachZipFilePanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				ScreenMaskUtility.unmaskScreen();
				String result = event.getResults();
				if (event.getResults().toLowerCase().indexOf(AdminConstants.ERROR_CODE_TEXT) > -1) {
					ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.IMPORT_UNSUCCESSFUL);
					getDialogBox().hide(true);
					return;
				}
				String keyWorkFlowName = "workFlowName:";
				String keyZipFolderPath = "filePath:";
				String keyWorkflowDeployed = "workflowDeployed:";
				String keyWorkflowEqual = "workflowEqual:";
				String keyWorkflowExistInBatchClass = "workflowExistInBatchClass:";
				String keySystemFolderPath = "systemFolderPath:";

				String workFlowName = result.substring(result.indexOf(keyWorkFlowName) + keyWorkFlowName.length(), result.indexOf('|',
						result.indexOf(keyWorkFlowName)));
				String zipSourcePath = result.substring(result.indexOf(keyZipFolderPath) + keyZipFolderPath.length(), result.indexOf(
						'|', result.indexOf(keyZipFolderPath)));
				String workflowDeployed = result.substring(result.indexOf(keyWorkflowDeployed) + keyWorkflowDeployed.length(), result
						.indexOf('|', result.indexOf(keyWorkflowDeployed)));
				String workflowEqual = result.substring(result.indexOf(keyWorkflowEqual) + keyWorkflowEqual.length(), result.indexOf(
						'|', result.indexOf(keyWorkflowEqual)));
				String workflowExistInBatchClass = result.substring(result.indexOf(keyWorkflowExistInBatchClass)
						+ keyWorkflowExistInBatchClass.length(), result.indexOf('|', result.indexOf(keyWorkflowExistInBatchClass)));
				String systemFolderPath = result.substring(result.indexOf(keySystemFolderPath) + keySystemFolderPath.length(), result
						.indexOf('|', result.indexOf(keySystemFolderPath)));
				name.setText(workFlowName);
				zipWorkflowName = workFlowName;
				importBatchClassUserOptionDTO.setName(workFlowName);
				importBatchClassUserOptionDTO.setZipFileName(zipSourcePath);
				importBatchClassUserOptionDTO.setWorkflowDeployed(Boolean.valueOf(workflowDeployed));
				importBatchClassUserOptionDTO.setWorkflowEqual(Boolean.valueOf(workflowEqual));
				importBatchClassUserOptionDTO.setWorkflowExistsInBatchClass(Boolean.valueOf(workflowExistInBatchClass));
				importBatchClassUserOptionDTO.setSystemFolder(systemFolderPath);
				uncPanel.clear();
				uncPanel.add(uncFolder);
				presenter.onAttachComplete(workFlowName, zipSourcePath);
				importPanel.setVisible(Boolean.TRUE);
				zipImported = true;
				dialogBox.center();
			}
		});
	}

	/**
	 * To get Priority.
	 * 
	 * @return String
	 */
	public String getPriority() {
		return priority.getValue();
	}

	/**
	 * To get Import Existing.
	 * 
	 * @return Hidden
	 */
	public Hidden getImportExisting() {
		return importExisting;
	}

	/**
	 * To set Priority.
	 * 
	 * @param priority
	 */
	public void setPriority(String priority) {
		this.priority.setValue(priority);
	}

	/**
	 * To get Unc Folder.
	 * 
	 * @return String
	 */
	public String getUncFolder() {
		String returnVal;
		if (importExisting.equals(TRUE)) {
			returnVal = uncFolderList.getValue(uncFolderList.getSelectedIndex()).trim();
		} else {
			returnVal = uncFolder.getText().trim();
		}
		return returnVal;
	}

	/**
	 * To set Unc Folder.
	 * 
	 * @param uncFolder String
	 */
	public void setUncFolder(String uncFolder) {
		this.uncFolder.setText(uncFolder);
	}

	/**
	 * To get Description.
	 * 
	 * @return String
	 */
	public String getDescription() {
		return description.getValue();
	}

	/**
	 * To set Description.
	 * 
	 * @param description String
	 */
	public void setDescription(String description) {
		this.description.setValue(description);
	}

	/**
	 * To get Validate TextBox.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateTextBox() {
		return validateTextBox;
	}

	/**
	 * To get Validate Desc TextBox.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateDescTextBox() {
		return validateDescTextBox;
	}

	/**
	 * To get Validate UNC TextBox.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateUNCTextBox() {
		return validateUNCTextBox;
	}

	/**
	 * To get Validate Name TextBox.
	 * 
	 * @return ValidatableWidget<TextBox>
	 */
	public ValidatableWidget<TextBox> getValidateNameTextBox() {
		return validateNameTextBox;
	}

	/**
	 * To get Priority TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getPriorityTextBox() {
		return priority;
	}

	/**
	 * To get Description TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getDescriptionTextBox() {
		return description;
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
	 * To get Cancel Button.
	 * 
	 * @return Button
	 */
	public Button getCancelButton() {
		return cancelButton;
	}

	/**
	 * To get Name TextBox.
	 * 
	 * @return TextBox
	 */
	public TextBox getNameTextBox() {
		return name;
	}

	/**
	 * To get Name.
	 * 
	 * @return String
	 */
	public String getName() {
		return name.getValue();
	}

	/**
	 * To set Name.
	 * 
	 * @param name String
	 */
	public void setName(String name) {
		this.name.setValue(name);
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
	 * To set Import File.
	 * 
	 * @param importFile FileUpload
	 */
	public void setImportFile(FileUpload importFile) {
		this.importFile = importFile;
	}

	/**
	 * To get Import File.
	 * 
	 * @return FileUpload
	 */
	public FileUpload getImportFile() {
		return importFile;
	}

	/**
	 * To get Import Batch Class User Option DTO.
	 * 
	 * @return ImportBatchClassUserOptionDTO
	 */
	public ImportBatchClassUserOptionDTO getImportBatchClassUserOptionDTO() {
		return importBatchClassUserOptionDTO;
	}

	/**
	 * To perform operations on OK click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("saveButton")
	public void onOkClick(ClickEvent clickEvent) {
		if (!zipImported) {
			ScreenMaskUtility.unmaskScreen();
			ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.NO_ZIP_ATTACHED);
		} else {
			if (isConfigApplied(importBatchClassUserOptionDTO.getUiConfigRoot())) {
				ScreenMaskUtility.maskScreen();
				boolean isUseSource = useSource.getValue();
				presenter.onOkClicked(isUseSource, importBatchClassUserOptionDTO.isImportExisting());
			} else {
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.NO_CONFIG_SELEDCTED);
			}
		}
	}

	private boolean isConfigApplied(Node rootNode) {
		boolean isAnyConfigApplied = false;
		for (Node node : rootNode.getChildren()) {
			isAnyConfigApplied = node.getLabel().isMandatory();
			if (!isAnyConfigApplied) {
				isAnyConfigApplied = isConfigApplied(node);
			}
			if (isAnyConfigApplied) {
				break;
			}
		}
		return isAnyConfigApplied;
	}

	/**
	 * To perform operations on cancel click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("cancelButton")
	public void onCancelClick(ClickEvent clickEvent) {
		presenter.deleteAttachedFolders(importBatchClassUserOptionDTO.getZipFileName());
		dialogBox.hide(true);
	}

	/**
	 * To perform operations on attach click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("attachButton")
	public void onAttachClick(ClickEvent clickEvent) {
		ScreenMaskUtility.maskScreen();
		attachZipFilePanel.submit();
		attachButton.addStyleName("disabled");
		attachButton.removeStyleName("enabled");
		attachButton.setEnabled(Boolean.FALSE);
	}

	/**
	 * This method creates tree view of batch class configuration for selected batch class.
	 * 
	 * @param uiConfigList List<Node>
	 */
	public void getbatchFolderListView(List<Node> uiConfigList) {
		batchClassFolderView.removeItems();
		if (uncFolderList.getItemCount() > 0) {
			importBatchClassUserOptionDTO.setUncFolder(uncFolderList.getValue(0));
		}
		if (uiConfigList != null && !uiConfigList.isEmpty()) {
			importBatchClassUserOptionDTO.getUiConfigRoot().getChildren().clear();

			for (Node rootNode : uiConfigList) {
				Node node = new Node();
				node.getLabel().setDisplayName(rootNode.getLabel().getDisplayName());
				node.getLabel().setKey(rootNode.getLabel().getKey());
				node.setParent(rootNode.getParent());
				CheckBox checkBox = new CheckBox();
				checkBox.setText(rootNode.getLabel().getDisplayName().trim());
				TreeItem treeItem = new TreeItem(checkBox);
				createUI(rootNode.getLabel().isMandatory(), checkBox, node, treeItem);
				node.setParent(importBatchClassUserOptionDTO.getUiConfigRoot());
				importBatchClassUserOptionDTO.getUiConfigRoot().getChildren().add(node);
				if (rootNode.getChildren() != null && !rootNode.getChildren().isEmpty()) {
					setImportFolderUI(treeItem, rootNode, node);
				}
				batchClassFolderView.addItem(treeItem);
				treeItem.setState(Boolean.TRUE);
			}

		}
	}

	private void setImportFolderUI(final TreeItem treeItem, final Node rootNode, final Node newRootNode) {
		for (Node childNode : rootNode.getChildren()) {
			if (rootNode != null && rootNode.getLabel() != null && rootNode.getLabel().getDisplayName() != null
					&& !rootNode.getLabel().getDisplayName().trim().isEmpty()) {
				Node node = new Node();
				node.getLabel().setDisplayName(childNode.getLabel().getDisplayName());
				node.getLabel().setKey(childNode.getLabel().getKey());
				node.setParent(childNode.getParent());
				CheckBox checkBox = new CheckBox();
				checkBox.setText(childNode.getLabel().getDisplayName().trim());
				TreeItem childTree = new TreeItem(checkBox);
				treeItem.addItem(childTree);
				createUI(childNode.getLabel().isMandatory(), checkBox, node, childTree);
				node.setParent(newRootNode);
				newRootNode.getChildren().add(node);
				if (childNode.getChildren() != null && !childNode.getChildren().isEmpty()) {
					setImportFolderUI(childTree, childNode, node);
				}
			}
		}
	}

	private void createUI(final boolean isMandatory, final CheckBox checkBox, final Node newNode, final TreeItem childTree) {
		if (newNode.getParent().getLabel().getKey().equals("BatchClassModules")
				|| newNode.getLabel().getKey().equals("BatchClassModules")) {
			checkBox.setEnabled(Boolean.FALSE);
			checkBox.setValue(Boolean.TRUE);
			newNode.getLabel().setMandatory(Boolean.TRUE);
		} else {
			if (isMandatory) {
				checkBox.setEnabled(Boolean.TRUE);
				checkBox.setValue(Boolean.FALSE);
			} else {
				if (importExisting.getValue().equalsIgnoreCase(TRUE)) {
					checkBox.setEnabled(Boolean.TRUE);
					checkBox.setValue(Boolean.FALSE);
					newNode.getLabel().setMandatory(Boolean.FALSE);
				} else if (importExisting.getValue().equalsIgnoreCase(FALSE)) {
					checkBox.setEnabled(Boolean.FALSE);
					checkBox.setValue(Boolean.TRUE);
					newNode.getLabel().setMandatory(Boolean.TRUE);
				}
			}

			if (checkBox != null && checkBox.isEnabled()) {
				checkBox.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						boolean checked = ((CheckBox) event.getSource()).getValue();
						newNode.getLabel().setMandatory(checked);
						setParentItemsUI(childTree.getParentItem(), checked, newNode);
						setChildItemsUI(childTree, checked, newNode);
					}

				});
			}
		}

	}

	private void setParentItemsUI(TreeItem parentItem, boolean checked, Node childNode) {
		Node parentNode = childNode.getParent();
		if (!checked && parentNode.getParent() != null) {
			CheckBox checkBox = (CheckBox) parentItem.getWidget();
			if (checkBox.isEnabled()) {
				checkBox.setValue(checked);
				parentNode.getLabel().setMandatory(checked);
			}
			setParentItemsUI(parentItem.getParentItem(), checked, parentNode);
		}
	}

	private void setChildItemsUI(final TreeItem childTree, final boolean checked, final Node parentNode) {
		List<Node> childList = parentNode.getChildren();
		if (childTree.getChildCount() > 0) {
			for (int index = 0; index < childTree.getChildCount(); index++) {
				CheckBox checkBox = (CheckBox) childTree.getChild(index).getWidget();
				if (checkBox.isEnabled()) {
					checkBox.setValue(checked);
					childList.get(index).getLabel().setMandatory(checked);
				}
				setChildItemsUI(childTree.getChild(index), checked, childList.get(index));
			}
		}
	}

	/**
	 * This method sets the UNC folder list of the selected batch class.
	 * 
	 * @param uncFolderConfigList
	 */
	public void setUNCFolderList(List<UNCFolderConfig> uncFolderConfigList) {
		uncFolderList.clear();
		if (uncFolderConfigList != null && !uncFolderConfigList.isEmpty()) {
			for (UNCFolderConfig uncFolderConfig : uncFolderConfigList) {
				String batchClassID = uncFolderConfig.getBatchClassId();
				String uncFolder = uncFolderConfig.getUncFolder();
				if (uncFolder != null && !uncFolder.isEmpty()) {
					String uncFolderName = uncFolder.substring(uncFolder.lastIndexOf('\\') + 1);
					if (uncFolderName.length() > AdminConstants.MAX_TEXT_LENGTH) {
						uncFolderName = StringUtil.getTrimmedText(uncFolderName, AdminConstants.MAX_TEXT_LENGTH,
								BatchClassManagementConstants.FOUR, Boolean.TRUE);
					}
					uncFolderList.addItem(batchClassID + '-' + uncFolderName, uncFolder);
					((Element) uncFolderList.getElement().getLastChild()).setAttribute("title", uncFolder);
				}
			}
		}
	}

	/**
	 * To toggle the existing state.
	 * 
	 * @param isEnabled boolean
	 */
	public void toggleUseExistingState(boolean isEnabled) {
		useExisting.setEnabled(isEnabled);
	}

	/**
	 * To get Zip Workflow Name.
	 * 
	 * @return String
	 */
	public String getZipWorkflowName() {
		return zipWorkflowName;
	}

	/**
	 * To set Error Text.
	 * 
	 * @param errorText String
	 */
	public void setErrorText(final String errorText) {
		this.errorText = errorText;
	}

	/**
	 * To get Error Text.
	 * 
	 * @return String
	 */
	public String getErrorText() {
		return errorText;
	}

	/**
	 * To get Validate System Folder TextBox.
	 * 
	 * @return the validateSystemFolderTextBox
	 */
	public ValidatableWidget<TextBox> getValidateSystemFolderTextBox() {
		return validateSystemFolderTextBox;
	}

	/**
	 * To set name.
	 * 
	 * @param name TextBox
	 */
	public void setName(TextBox name) {
		this.name = name;
	}
}
