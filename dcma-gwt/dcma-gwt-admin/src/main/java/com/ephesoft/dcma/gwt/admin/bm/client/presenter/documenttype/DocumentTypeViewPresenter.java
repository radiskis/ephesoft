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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.documenttype;

import java.util.List;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.MessageConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.documenttype.DocumentTypeView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.documenttype.TableTestResultView;
import com.ephesoft.dcma.gwt.core.client.RandomIdGenerator;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.FieldTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.FunctionKeyDTO;
import com.ephesoft.dcma.gwt.core.shared.TableInfoDTO;
import com.ephesoft.dcma.gwt.core.shared.TestTableResultDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;

public class DocumentTypeViewPresenter extends AbstractBatchClassPresenter<DocumentTypeView> {

	private final DocumentTypeDetailPresenter documentTypeDetailPresenter;

	private final EditDocumentTypePresenter editDocumentTypePresenter;

	private final TableTestResultView tableTestResultView = new TableTestResultView();

	public DocumentTypeViewPresenter(BatchClassManagementController controller, DocumentTypeView view) {

		super(controller, view);
		this.documentTypeDetailPresenter = new DocumentTypeDetailPresenter(controller, view.getDocumentTypeDetailView());
		this.editDocumentTypePresenter = new EditDocumentTypePresenter(controller, view.getEditDocumentTypeView());
	}

	public void showDocumentTypeView() {
		view.getDocumentTypeVerticalPanel().setVisible(Boolean.TRUE);
		view.getDocumentTypeConfigVerticalPanel().setVisible(Boolean.FALSE);
	}

	public void showEditDocumentTypeView() {
		view.getDocumentTypeVerticalPanel().setVisible(Boolean.FALSE);
		view.getDocumentTypeConfigVerticalPanel().setVisible(Boolean.TRUE);
	}

	public void onDetailViewClicked() {
		documentTypeDetailPresenter.bind();
	}

	public DocumentTypeDetailPresenter getDocumentTypeDetailPresenter() {
		return documentTypeDetailPresenter;
	}

	@Override
	public void bind() {
		this.editDocumentTypePresenter.bind();
		this.documentTypeDetailPresenter.bind();
		if (controller.getSelectedDocument() != null) {
			view.createDocumentFunctionKeyList(controller.getSelectedDocument().getFunctionKeys());
			view.createPageTypeList(controller.getSelectedDocument().getPages());
			view.createFieldTypeList(controller.getSelectedDocument().getFields());
			view.createTableInfoList(controller.getSelectedDocument().getTableInfos());
		}
	}

	public void onAddFieldButtonClicked() {
		if (controller.isAdd()) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.ADD_DOCUMENT_TYPE)); 
			return;
		}
		FieldTypeDTO fieldTypeDTO = new FieldTypeDTO();
		fieldTypeDTO.setNew(true);
		fieldTypeDTO.setDocTypeDTO(controller.getSelectedDocument());
		fieldTypeDTO.setName("");
		fieldTypeDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		controller.setAdd(true);
		controller.setSelectedDocumentLevelField(fieldTypeDTO);
		controller.getMainPresenter().showFieldTypeView(true);

	}

	public void onEditDocumentPropertiesButtonClicked() {
		controller.setAdd(false);
		editDocumentTypePresenter.bind();
		showEditDocumentTypeView();
		controller.getBatchClass().setDirty(Boolean.TRUE);
	}

	public void onDeleteFieldButtonClicked(String identifier) {
		controller.getSelectedDocument().getFieldTypeByIdentifier(identifier).setDeleted(true);
		controller.getBatchClass().setDirty(true);
		controller.getMainPresenter().showDocumentTypeView(false);
	}

	public void onEditFieldButtonClicked(String identifier) {
		controller.setSelectedDocumentLevelField(controller.getSelectedDocument().getFieldTypeByIdentifier(identifier));
		controller.setAdd(false);
		controller.getMainPresenter().showFieldTypeView(true);
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		// Event handling to be done here.
	}

	public void onAddTableInfoFieldButtonClicked() {
		if (controller.isAdd()) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.ADD_DOCUMENT_TYPE));
			return;
		}
		TableInfoDTO tableInfoDTO = new TableInfoDTO();
		tableInfoDTO.setNew(true);
		tableInfoDTO.setDocTypeDTO(controller.getSelectedDocument());
		tableInfoDTO.setName("");
		tableInfoDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		controller.setAdd(true);
		controller.setTableInfoSelectedField(tableInfoDTO);
		controller.getMainPresenter().showTableInfoView(true);

	}

	public void onEditTableInfoFieldButtonClicked(String identifier) {
		controller.setTableInfoSelectedField(controller.getSelectedDocument().getTableInfoByIdentifier(identifier));
		controller.setAdd(false);
		controller.getMainPresenter().showTableInfoView(true);
	}

	public void onDeleteTableInfoButtonClicked(String identifier) {
		controller.getSelectedDocument().getTableInfoByIdentifier(identifier).setDeleted(true);
		controller.getBatchClass().setDirty(true);
		controller.getMainPresenter().showDocumentTypeView(false);
	}

	public void onTestTableButtonClicked(String identifier) {
		ScreenMaskUtility.maskScreen();
		controller.setTableInfoSelectedField(controller.getSelectedDocument().getTableInfoByIdentifier(identifier));

		controller.getRpcService().testTablePattern(controller.getBatchClass(), controller.getSelectedTableInfoField(),
				new AsyncCallback<List<TestTableResultDTO>>() {

					@Override
					public void onFailure(Throwable throwable) {
						ScreenMaskUtility.unmaskScreen();
						final ConfirmationDialog dialog = new ConfirmationDialog(true);
						dialog.addDialogListener(new DialogListener() {

							@Override
							public void onOkClick() {
								dialog.hide(true);
							}

							@Override
							public void onCancelClick() {
								// TODO Auto-generated method stub
							}
						});
						dialog.setText(MessageConstants.TITLE_TEST_FAILURE);
						dialog.setMessage(throwable.getMessage());
						dialog.okButton.setStyleName(AdminConstants.BUTTON_STYLE);
						dialog.center();
						dialog.show();
						dialog.okButton.setFocus(true);
					}

					@Override
					public void onSuccess(List<TestTableResultDTO> outputDtos) {
						ScreenMaskUtility.unmaskScreen();
						DialogBox dialogBox = new DialogBox();
						dialogBox.addStyleName("width500px");
						dialogBox.setHeight("200px");
						tableTestResultView.createTestTableList(outputDtos);
						tableTestResultView.setDialogBox(dialogBox);
						dialogBox.setText(MessageConstants.TEST_TABLE_RESULT_HEADER);
						dialogBox.setWidget(tableTestResultView);
						dialogBox.center();
						tableTestResultView.getBackButton().setFocus(true);
						dialogBox.show();
					}
				});
	}

	public void onEditFunctionKeyButtonClicked(String identifier) {
		controller.setSelectedFunctionKeyDTO(controller.getSelectedDocument().getFunctionKeyByIdentifier(identifier));
		controller.setAdd(false);
		controller.getMainPresenter().showFunctionKeyView(true);

	}

	public void onAddFunctionKeyButtonClicked() {
		if (controller.isAdd()) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.ADD_DOCUMENT_TYPE));
			return;
		}
		controller.getMainPresenter().getFunctionKeyViewPresenter().getEditFunctionKeyPresenter().clearFields();
		FunctionKeyDTO functionKeyDTO = new FunctionKeyDTO();
		functionKeyDTO.setDocTypeDTO(controller.getSelectedDocument());
		functionKeyDTO.setNew(true);
		functionKeyDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		controller.setAdd(true);
		controller.setSelectedFunctionKeyDTO(functionKeyDTO);
		controller.getMainPresenter().showFunctionKeyView(true);
	}

	public void onDeleteFunctionKeyButtonClicked(String identifier) {
		controller.getSelectedDocument().getFunctionKeyByIdentifier(identifier).setDeleted(true);
		controller.getBatchClass().setDirty(true);
		controller.getMainPresenter().showDocumentTypeView(false);
	}

}
