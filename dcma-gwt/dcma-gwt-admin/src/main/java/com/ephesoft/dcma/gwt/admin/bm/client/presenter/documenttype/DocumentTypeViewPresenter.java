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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.documenttype;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.MessageConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.documenttype.DocumentTypeView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.documenttype.TableTestResultView;
import com.ephesoft.dcma.gwt.core.client.EphesoftAsyncCallback;
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
import com.google.gwt.user.client.ui.DialogBox;

/**
 * The presenter for view that shows the document type view details.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class DocumentTypeViewPresenter extends AbstractBatchClassPresenter<DocumentTypeView> {

	/**
	 * documentTypeDetailPresenter DocumentTypeDetailPresenter.
	 */
	private final DocumentTypeDetailPresenter documentTypeDetailPresenter;

	/**
	 * editDocumentTypePresenter EditDocumentTypePresenter.
	 */
	private final EditDocumentTypePresenter editDocumentTypePresenter;

	/**
	 * tableTestResultView TableTestResultView.
	 */
	private final TableTestResultView tableTestResultView = new TableTestResultView();

	/**
	 * Constructor.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view DocumentTypeView
	 */
	public DocumentTypeViewPresenter(BatchClassManagementController controller, DocumentTypeView view) {

		super(controller, view);
		this.documentTypeDetailPresenter = new DocumentTypeDetailPresenter(controller, view.getDocumentTypeDetailView());
		this.editDocumentTypePresenter = new EditDocumentTypePresenter(controller, view.getEditDocumentTypeView());
	}

	/**
	 * To show Document Type View.
	 */
	public void showDocumentTypeView() {
		view.getDocumentTypeVerticalPanel().setVisible(Boolean.TRUE);
		view.getDocumentTypeConfigVerticalPanel().setVisible(Boolean.FALSE);
	}

	/**
	 * To show edit Document Type View.
	 */
	public void showEditDocumentTypeView() {
		view.getDocumentTypeVerticalPanel().setVisible(Boolean.FALSE);
		view.getDocumentTypeConfigVerticalPanel().setVisible(Boolean.TRUE);
	}

	/**
	 * In case of Detail View Clicked.
	 */
	public void onDetailViewClicked() {
		documentTypeDetailPresenter.bind();
	}

	/**
	 * To get Document Type Detail Presenter.
	 * 
	 * @return DocumentTypeDetailPresenter
	 */
	public DocumentTypeDetailPresenter getDocumentTypeDetailPresenter() {
		return documentTypeDetailPresenter;
	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {
		this.documentTypeDetailPresenter.bind();
		if (controller.getSelectedDocument() != null) {
			view.createDocumentFunctionKeyList(controller.getSelectedDocument().getFunctionKeys());
			view.createPageTypeList(controller.getSelectedDocument().getPages());
			view.createFieldTypeList(controller.getSelectedDocument().getFields());
			view.createTableInfoList(controller.getSelectedDocument().getTableInfos());
		}
		this.editDocumentTypePresenter.bind();
	}

	/**
	 * To perform operations in case of add Field Button Clicked.
	 */
	public void onAddFieldButtonClicked() {
		if (controller.isAdd()) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.ADD_DOCUMENT_TYPE));
			return;
		}
		FieldTypeDTO fieldTypeDTO = createFieldTypeDTOObject();

		controller.setAdd(true);
		controller.setSelectedDocumentLevelField(fieldTypeDTO);
		controller.getMainPresenter().showFieldTypeView(true);
	}

	/**
	 * To create Field Type DTO Object.
	 * 
	 * @return FieldTypeDTO
	 */
	public FieldTypeDTO createFieldTypeDTOObject() {
		FieldTypeDTO fieldTypeDTO = new FieldTypeDTO();
		fieldTypeDTO.setNew(true);
		fieldTypeDTO.setDocTypeDTO(controller.getSelectedDocument());
		fieldTypeDTO.setName(BatchClassManagementConstants.EMPTY_STRING);
		fieldTypeDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		// setting field order number
		if (controller.getSelectedDocument() != null) {
			Collection<FieldTypeDTO> dlfList = controller.getSelectedDocument().getFields();
			if (null != dlfList && !dlfList.isEmpty()) {
				ArrayList<Integer> fieldOrderList = new ArrayList<Integer>();
				for (FieldTypeDTO fieldTypeDto : dlfList) {
					fieldOrderList.add(Integer.parseInt(fieldTypeDto.getFieldOrderNumber()));
				}
				Integer maxFieldOrder = Collections.max(fieldOrderList);
				if (maxFieldOrder > (Integer.MAX_VALUE - BatchClassManagementConstants.FIELD_ORDER_DIFFERENCE)) {
					// setting the field order to be empty if generated field order is not in integer range
					fieldTypeDTO.setFieldOrderNumber(BatchClassManagementConstants.EMPTY_STRING);
				} else {
					Integer newFieldOrder = Collections.max(fieldOrderList) + BatchClassManagementConstants.FIELD_ORDER_DIFFERENCE;
					fieldTypeDTO.setFieldOrderNumber(newFieldOrder.toString());
				}
			} else {
				fieldTypeDTO.setFieldOrderNumber(BatchClassManagementConstants.INITIAL_FIELD_ORDER_NUMBER);
			}
		}
		return fieldTypeDTO;
	}

	/**
	 * To perform operations in case of edit document properties button clicked.
	 */
	public void onEditDocumentPropertiesButtonClicked() {
		controller.setAdd(false);

		showEditDocumentTypeView();
		controller.getBatchClass().setDirty(Boolean.TRUE);
		editDocumentTypePresenter.bind();
	}

	/**
	 * To perform operations in case of delete Field button clicked.
	 * 
	 * @param identifier String
	 */
	public void onDeleteFieldButtonClicked(String identifier) {
		controller.getSelectedDocument().getFieldTypeByIdentifier(identifier).setDeleted(true);
		controller.getBatchClass().setDirty(true);
		controller.getMainPresenter().showDocumentTypeView(false);
	}

	/**
	 * To perform operations in case of delete Field button clicked.
	 * 
	 * @param identifier String
	 */
	public void onEditFieldButtonClicked(String identifier) {
		controller.setSelectedDocumentLevelField(controller.getSelectedDocument().getFieldTypeByIdentifier(identifier));
		controller.setAdd(false);
		controller.getMainPresenter().showFieldTypeView(false);
	}

	/**
	 * To handle events.
	 * 
	 * @param eventBus HandlerManager
	 */
	@Override
	public void injectEvents(HandlerManager eventBus) {
		// Event handling to be done here.
	}

	/**
	 * To perform operations in case of add table info field button clicked.
	 */
	public void onAddTableInfoFieldButtonClicked() {
		if (controller.isAdd()) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.ADD_DOCUMENT_TYPE));
			return;
		}
		TableInfoDTO tableInfoDTO = createTableInfoDTOObject();
		controller.setAdd(true);
		controller.setTableInfoSelectedField(tableInfoDTO);
		controller.getMainPresenter().showTableInfoView(true);

	}

	/**
	 * To create Table Info DTO Object.
	 * 
	 * @return TableInfoDTO
	 */
	public TableInfoDTO createTableInfoDTOObject() {
		TableInfoDTO tableInfoDTO = new TableInfoDTO();
		tableInfoDTO.setNew(true);
		tableInfoDTO.setDocTypeDTO(controller.getSelectedDocument());
		tableInfoDTO.setName(BatchClassManagementConstants.EMPTY_STRING);
		tableInfoDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		return tableInfoDTO;
	}

	/**
	 * To perform operations in case of edit table info field button clicked.
	 * 
	 * @param identifier String
	 */
	public void onEditTableInfoFieldButtonClicked(String identifier) {
		controller.setTableInfoSelectedField(controller.getSelectedDocument().getTableInfoByIdentifier(identifier));
		controller.setAdd(false);
		controller.getMainPresenter().showTableInfoView(false);
	}

	/**
	 * To perform operations in case of delete table info button clicked.
	 * 
	 * @param identifier String
	 */
	public void onDeleteTableInfoButtonClicked(String identifier) {
		controller.getSelectedDocument().getTableInfoByIdentifier(identifier).setDeleted(true);
		controller.getBatchClass().setDirty(true);
		controller.getMainPresenter().showDocumentTypeView(false);
	}

	/**
	 * To perform operations in case of test table button clicked.
	 * 
	 * @param identifier String
	 */
	public void onTestTableButtonClicked(String identifier) {
		ScreenMaskUtility.maskScreen();
		controller.setTableInfoSelectedField(controller.getSelectedDocument().getTableInfoByIdentifier(identifier));

		controller.getRpcService().testTablePattern(controller.getBatchClass(), controller.getSelectedTableInfoField(),
				new EphesoftAsyncCallback<List<TestTableResultDTO>>() {

					@Override
					public void customFailure(Throwable throwable) {
						ScreenMaskUtility.unmaskScreen();
						final ConfirmationDialog dialog = ConfirmationDialogUtil.showConfirmationDialog(throwable.getMessage(),
								MessageConstants.TITLE_TEST_FAILURE, Boolean.TRUE, Boolean.TRUE);
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

						dialog.okButton.setStyleName(AdminConstants.BUTTON_STYLE);

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

	/**
	 * To perform operations in case of edit function key button clicked.
	 * 
	 * @param identifier String
	 */
	public void onEditFunctionKeyButtonClicked(String identifier) {
		controller.setSelectedFunctionKeyDTO(controller.getSelectedDocument().getFunctionKeyByIdentifier(identifier));
		controller.setAdd(false);
		controller.getMainPresenter().showFunctionKeyView(false);

	}

	/**
	 * To perform operations in case of add function key button clicked.
	 */
	public void onAddFunctionKeyButtonClicked() {
		if (controller.isAdd()) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.ADD_DOCUMENT_TYPE));
			return;
		}
		controller.getMainPresenter().getFunctionKeyViewPresenter().getEditFunctionKeyPresenter().clearFields();
		FunctionKeyDTO functionKeyDTO = createFunctionKeyDTOObject();
		controller.setAdd(true);
		controller.setSelectedFunctionKeyDTO(functionKeyDTO);
		controller.getMainPresenter().showFunctionKeyView(true);
	}

	/**
	 * To create Function Key DTO Object.
	 * 
	 * @return FunctionKeyDTO
	 */
	public FunctionKeyDTO createFunctionKeyDTOObject() {
		FunctionKeyDTO functionKeyDTO = new FunctionKeyDTO();
		functionKeyDTO.setDocTypeDTO(controller.getSelectedDocument());
		functionKeyDTO.setNew(true);
		functionKeyDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		return functionKeyDTO;
	}

	/**
	 * To perform operations in case of delete function key button clicked.
	 * 
	 * @param identifier String
	 */
	public void onDeleteFunctionKeyButtonClicked(String identifier) {
		controller.getSelectedDocument().getFunctionKeyByIdentifier(identifier).setDeleted(true);
		controller.getBatchClass().setDirty(true);
		controller.getMainPresenter().showDocumentTypeView(false);
	}

}
