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

package com.ephesoft.dcma.gwt.admin.bm.client.view.batch;

import java.util.List;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.MessageConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.batch.ImportBatchClassPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.client.validator.EmptyStringValidator;
import com.ephesoft.dcma.gwt.core.client.validator.ValidatableWidget;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.ImportBatchClassUserOptionDTO;
import com.ephesoft.dcma.gwt.core.shared.StringUtil;
import com.ephesoft.dcma.gwt.core.shared.importTree.Node;
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

public class ImportBatchClassView extends View<ImportBatchClassPresenter> {

	private static final String ATTACH_FORM_ACTION = "dcma-gwt-admin/importBatchClassUpload?";

	interface Binder extends UiBinder<VerticalPanel, ImportBatchClassView> {
	}

	@UiField
	protected TextBox priority;
	@UiField
	protected TextBox description;
	@UiField
	protected TextBox name;
	@UiField
	protected FileUpload importFile;

	@UiField
	protected CheckBox useSource;
	@UiField
	protected CheckBox useExisting;

	@UiField
	protected Button saveButton;
	@UiField
	protected Button cancelButton;

	@UiField
	protected FormPanel attachZipFilePanel;
	@UiField
	protected Button attachButton;

	@UiField
	protected DockLayoutPanel importPanel;

	@UiField
	protected Label priorityLabel;
	@UiField
	protected Label descLabel;
	@UiField
	protected Label nameLabel;
	@UiField
	protected Label uncLabel;
	@UiField
	protected Label importLabel;

	@UiField
	protected Label priorityStar;

	@UiField
	protected Label uncStar;

	@UiField
	protected Label importStar;

	@UiField
	protected HorizontalPanel importBatchPanel;

	@UiField
	protected Label descStar;

	@UiField
	protected Label nameStar;

	@UiField
	protected VerticalPanel importFolderListViewPanel;

	@UiField
	protected HorizontalPanel uncPanel;

	@UiField
	protected Tree batchClassFolderView;

	private final ListBox uncFolderList;
	private final TextBox uncFolder;

	private final ValidatableWidget<TextBox> validateTextBox;
	private final ValidatableWidget<TextBox> validateDescTextBox;
	private final ValidatableWidget<TextBox> validateUNCTextBox;
	private final ValidatableWidget<TextBox> validateNameTextBox;
	private final ImportBatchClassUserOptionDTO importBatchClassUserOptionDTO;

	private DialogBox dialogBox;
	private boolean zipImported = false;

	private Hidden importExisting = new Hidden("importExisting", "true");

	public void setImportExisting(Hidden importExisting) {
		this.importExisting = importExisting;
	}

	@UiField
	protected VerticalPanel importBatchClassViewPanel;

	private static final Binder BINDER = GWT.create(Binder.class);
	private static final String TRUE = "true";
	private static final String FALSE = "false";

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

		uncFolder = new TextBox();
		uncFolder.setName("uncFolder");
		uncFolderList = new ListBox();
		uncFolderList.setName("uncFolderList");
		uncFolderList.setWidth("145px");
		uncFolder.setWidth("140px");
		importBatchClassUserOptionDTO = new ImportBatchClassUserOptionDTO();
		importBatchClassUserOptionDTO.setUseSource(Boolean.TRUE);
		importBatchClassUserOptionDTO.setImportExisting(Boolean.TRUE);

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
				importBatchClassUserOptionDTO.setUncFolder(uncFolderList.getValue(uncFolderList.getSelectedIndex()));
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

		importBatchClassViewPanel.setSpacing(5);

		nameLabel.setText(AdminConstants.NAME);
		priorityLabel.setText(AdminConstants.PRIORITY);
		descLabel.setText(AdminConstants.DESCRIPTION);
		uncLabel.setText(AdminConstants.UNC_FOLDER);
		importLabel.setText(AdminConstants.IMPORT_FILE);

		uncStar.setText(AdminConstants.STAR);
		importStar.setText(AdminConstants.STAR);

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

		importBatchPanel.setSpacing(10);

		attachZipFilePanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		attachZipFilePanel.setMethod(FormPanel.METHOD_POST);
		attachZipFilePanel.setAction(ATTACH_FORM_ACTION);

		importFolderListViewPanel.add(importExisting);
		importPanel.setVisible(Boolean.FALSE);

		useExisting.setValue(Boolean.TRUE);
		useSource.setValue(Boolean.TRUE);
		name.setEnabled(Boolean.FALSE);
		description.setEnabled(Boolean.FALSE);
		priority.setEnabled(Boolean.FALSE);
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
					uncPanel.add(uncFolderList);
					importExisting.setValue(TRUE);
				} else {
					uncPanel.add(uncFolder);
					uncFolder.setText("");
					importExisting.setValue(FALSE);
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
					description.setEnabled(false);
					description.setText("");
					priority.setEnabled(false);
					priority.setText("");
					descStar.setText("");
					priorityStar.setText("");
				} else {
					description.setEnabled(true);
					priority.setEnabled(true);
					descStar.setText(AdminConstants.STAR);
					priorityStar.setText(AdminConstants.STAR);
				}
			}
		});

		attachZipFilePanel.addSubmitHandler(new SubmitHandler() {

			@Override
			public void onSubmit(SubmitEvent event) {
				String fileName = importFile.getFilename();
				String fileExt = fileName.substring(fileName.lastIndexOf('.') + 1);
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
				String workFlowName = result.substring(result.indexOf(keyWorkFlowName) + keyWorkFlowName.length(), result.indexOf('|',
						result.indexOf(keyWorkFlowName)));
				String zipSourcePath = result.substring(result.indexOf(keyZipFolderPath) + keyZipFolderPath.length(), result.indexOf(
						'|', result.indexOf(keyZipFolderPath)));
				name.setText(workFlowName);
				importBatchClassUserOptionDTO.setName(workFlowName);
				importBatchClassUserOptionDTO.setZipFileName(zipSourcePath);
				uncPanel.clear();
				uncPanel.add(uncFolderList);
				presenter.onAttachComplete(workFlowName, zipSourcePath);
				importPanel.setVisible(Boolean.TRUE);
				zipImported = true;
				dialogBox.center();
			}
		});
	}

	public String getPriority() {
		return priority.getValue();
	}

	public Hidden getImportExisting() {
		return importExisting;
	}

	public void setPriority(String priority) {
		this.priority.setValue(priority);
	}

	public String getUncFolder() {
		String returnVal;
		if (importExisting.equals(TRUE)) {
			returnVal = uncFolderList.getValue(uncFolderList.getSelectedIndex()).trim();
		} else {
			returnVal = uncFolder.getText().trim();
		}
		return returnVal;
	}

	public void setUncFolder(String uncFolder) {
		this.uncFolder.setText(uncFolder);
	}

	public String getDescription() {
		return description.getValue();
	}

	public void setDescription(String description) {
		this.description.setValue(description);
	}

	public ValidatableWidget<TextBox> getValidateTextBox() {
		return validateTextBox;
	}

	public ValidatableWidget<TextBox> getValidateDescTextBox() {
		return validateDescTextBox;
	}

	public ValidatableWidget<TextBox> getValidateUNCTextBox() {
		return validateUNCTextBox;
	}

	public ValidatableWidget<TextBox> getValidateNameTextBox() {
		return validateNameTextBox;
	}

	public TextBox getPriorityTextBox() {
		return priority;
	}

	public TextBox getDescriptionTextBox() {
		return description;
	}

	public Button getSaveButton() {
		return saveButton;
	}

	public Button getCancelButton() {
		return cancelButton;
	}

	public TextBox getNameTextBox() {
		return name;
	}

	public String getName() {
		return name.getValue();
	}

	public void setName(String name) {
		this.name.setValue(name);
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

	public ImportBatchClassUserOptionDTO getImportBatchClassUserOptionDTO() {
		return importBatchClassUserOptionDTO;
	}

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

	@UiHandler("cancelButton")
	public void onCancelClick(ClickEvent clickEvent) {
		presenter.deleteAttachedFolders(importBatchClassUserOptionDTO.getZipFileName());
		dialogBox.hide(true);
	}

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
	 * @param uiConfigList
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
	 * @param uncFolders
	 */
	public void setUNCFolderList(List<String> uncFolders) {
		uncFolderList.clear();
		if (uncFolders != null && !uncFolders.isEmpty()) {
			for (String uncFolderPath : uncFolders) {
				String uncFolderName = uncFolderPath.substring(uncFolderPath.lastIndexOf('\\') + 1);
				if (uncFolderName.length() > AdminConstants.MAX_TEXT_LENGTH) {
					uncFolderName = StringUtil.getTrimmedText(uncFolderName, AdminConstants.MAX_TEXT_LENGTH, 4, Boolean.TRUE);
				}
				uncFolderList.addItem(uncFolderName, uncFolderPath);
				((Element) uncFolderList.getElement().getLastChild()).setAttribute("title", uncFolderPath);
			}
		}
	}
}
