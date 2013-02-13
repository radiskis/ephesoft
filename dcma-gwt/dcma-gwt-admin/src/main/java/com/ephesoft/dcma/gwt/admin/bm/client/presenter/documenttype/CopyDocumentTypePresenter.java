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

import java.util.Collection;
import java.util.List;

import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.MessageConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.documenttype.CopyDocumentView;
import com.ephesoft.dcma.gwt.core.client.EphesoftAsyncCallback;
import com.ephesoft.dcma.gwt.core.client.RandomIdGenerator;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.DocumentTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.FieldTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.FunctionKeyDTO;
import com.ephesoft.dcma.gwt.core.shared.KVExtractionDTO;
import com.ephesoft.dcma.gwt.core.shared.RegexDTO;
import com.ephesoft.dcma.gwt.core.shared.TableColumnInfoDTO;
import com.ephesoft.dcma.gwt.core.shared.TableInfoDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.google.gwt.event.shared.HandlerManager;

/**
 * This class handles functionality to copy document type and show end result to user.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class CopyDocumentTypePresenter extends AbstractBatchClassPresenter<CopyDocumentView> {

	/**
	 * The batch class that is to being copied.
	 */
	private DocumentTypeDTO documentTypeDTO;

	/**
	 * Constructor.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view {@link CopyDocumentView}
	 */
	public CopyDocumentTypePresenter(final BatchClassManagementController controller, final CopyDocumentView view) {
		super(controller, view);
	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {
		if (documentTypeDTO != null) {
			view.setName(documentTypeDTO.getName());
			view.setDescription(documentTypeDTO.getDescription());
			view.setMinConfidenceThreshold(String.valueOf(documentTypeDTO.getMinConfidenceThreshold()));
		}
	}

	/**
	 * To show Document Copy View.
	 */
	public void showDocumentCopyView() {
		view.getDialogBox().setWidth("100%");
		view.getDialogBox().center();
		view.getDialogBox().add(view);
		view.getDialogBox().show();
		view.getDialogBox().setText(MessageConstants.DOCUMENT_TYPE_COPY);
	}

	/**
	 * To handle events.
	 * 
	 * @param eventBus HandlerManager
	 */
	@Override
	public void injectEvents(final HandlerManager eventBus) {
		// Event handling to be done here.
	}

	/**
	 * To get Document Type DTO.
	 * 
	 * @return DocumentTypeDTO
	 */
	public DocumentTypeDTO getDocumentTypeDTO() {
		return documentTypeDTO;
	}

	/**
	 * To set Document Type DTO.
	 * 
	 * @param documentTypeDTO DocumentTypeDTO
	 */
	public void setDocumentTypeDTO(DocumentTypeDTO documentTypeDTO) {
		this.documentTypeDTO = documentTypeDTO;
	}

	/**
	 * To perform operations on OK clicked.
	 */
	public void onOkClicked() {
		ScreenMaskUtility.maskScreen();
		boolean validCheck = true;
		if (validCheck && (!view.getValidateNameTextBox().validate() || !view.getValidateDescriptionTextBox().validate())) {
			ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.MANDATORY_FIELDS_ERROR_MSG);
			validCheck = false;
			ScreenMaskUtility.unmaskScreen();

		}
		if (validCheck && !view.getValidatePriorityTextBox().validate()) {
			if (view.getMinConfidenceThreshold() != null && !view.getMinConfidenceThreshold().isEmpty()) {
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.CONFIDENCE_SHOULD_BE_NUMERIC);
				ScreenMaskUtility.unmaskScreen();
			} else {
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.MANDATORY_FIELDS_ERROR_MSG);
				ScreenMaskUtility.unmaskScreen();
			}
			validCheck = false;
		}
		if (validCheck) {
			for (DocumentTypeDTO documentTypeDTO : controller.getBatchClass().getDocuments()) {
				if (view.getName().equalsIgnoreCase(documentTypeDTO.getName())) {
					ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.DOCUMENT_NAME_SAME_ERROR);
					validCheck = false;
					ScreenMaskUtility.unmaskScreen();
					break;
				}
				if (view.getDescription().equalsIgnoreCase(documentTypeDTO.getDescription())) {
					ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.DOCUMENT_DESCRIPTION_SAME_ERROR);
					validCheck = false;
					ScreenMaskUtility.unmaskScreen();
					break;
				}
			}

		}

		if (validCheck) {

			controller.getRpcService().copyDocument(documentTypeDTO, new EphesoftAsyncCallback<DocumentTypeDTO>() {

				@Override
				public void customFailure(Throwable arg0) {
					ScreenMaskUtility.unmaskScreen();
					ConfirmationDialogUtil.showConfirmationDialog(MessageConstants.DOCUMENT_COPY_FAILURE + arg0.getMessage(),
							MessageConstants.COPY_FAILURE, Boolean.TRUE);
				}

				@Override
				public void onSuccess(final DocumentTypeDTO newDocumentTypeDTO) {
					ScreenMaskUtility.unmaskScreen();
					newDocumentTypeDTO.setName(view.getName());
					newDocumentTypeDTO.setDescription(view.getDescription());
					newDocumentTypeDTO.setMinConfidenceThreshold(Integer.parseInt(view.getMinConfidenceThreshold()));
					newDocumentTypeDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
					newDocumentTypeDTO.setNew(true);

					// Set new attribute for child DTOs
					setNewAttributeOfFeildDTO(newDocumentTypeDTO.getFields(true));
					setNewAttributeOfFunctionKeys(newDocumentTypeDTO.getFunctionKeys(true));
					setNewAttributeOfTableInfoDTO(newDocumentTypeDTO.getTableInfos(true));
					controller.getBatchClass().addDocumentType(newDocumentTypeDTO);

					// To update batch class DTO changes
					controller.getBatchClass().setDirty(true);
					ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(
							MessageConstants.DOCUMENT_COPY_CREATED_SUCCESSFULLY, MessageConstants.COPY_SUCCESSFUL, Boolean.TRUE);

					confirmationDialog.addDialogListener(new DialogListener() {

						@Override
						public void onOkClick() {
							controller.refresh();
							view.getDialogBox().hide(true);
						}

						@Override
						public void onCancelClick() {
							// To do nothing
						}
					});

				}
			});
		}

	}

	/**
	 * This method set new attribute of TableInfoDTO.
	 * 
	 * @param tableInfos collection of copied TableInfoDTO
	 */
	protected void setNewAttributeOfTableInfoDTO(Collection<TableInfoDTO> tableInfos) {
		if (null != tableInfos) {
			for (TableInfoDTO tableInfoDTO : tableInfos) {
				tableInfoDTO.setNew(true);
				setNewAttributeTableColumnInfoDTO(tableInfoDTO.getColumnInfoDTOs(true));
			}
		}

	}

	/**
	 * This method set new attribute of TableColumnInfoDTO.
	 * 
	 * @param columnInfoDTOs collection of copied TableColumnInfoDTO
	 */
	private void setNewAttributeTableColumnInfoDTO(List<TableColumnInfoDTO> columnInfoDTOs) {
		if (null != columnInfoDTOs) {
			for (TableColumnInfoDTO tableColumnInfoDTO : columnInfoDTOs) {
				tableColumnInfoDTO.setNew(true);
			}
		}

	}

	/**
	 * This method set new attribute of FunctionKeyDTO.
	 * 
	 * @param functionKeys collection of copied FunctionKeyDTO
	 */
	protected void setNewAttributeOfFunctionKeys(Collection<FunctionKeyDTO> functionKeys) {
		if (null != functionKeys) {
			for (FunctionKeyDTO functionKeyDTO : functionKeys) {
				functionKeyDTO.setNew(true);
			}
		}

	}

	/**
	 * This method set new attribute of FieldTypeDTO.
	 * 
	 * @param fields collection of copied FieldTypeDTO
	 */
	protected void setNewAttributeOfFeildDTO(Collection<FieldTypeDTO> fields) {
		if (null != fields) {
			for (FieldTypeDTO fieldTypeDTO : fields) {
				fieldTypeDTO.setNew(true);
				setNewAttributeOfKvExtractionDTO(fieldTypeDTO.getKvExtractionList(true));
				setNewAttributeOfRegexDTO(fieldTypeDTO.getRegexList(true));
			}
		}

	}

	/**
	 * This method set new attribute of RegexDTO.
	 * 
	 * @param regexList list of copied RegexDTO
	 */
	private void setNewAttributeOfRegexDTO(List<RegexDTO> regexList) {
		if (null != regexList) {
			for (RegexDTO regexDTO : regexList) {
				regexDTO.setNew(true);
			}
		}

	}

	/**
	 * This method set new attribute of KVExtractionDTO.
	 * 
	 * @param kvExtractionList list of copied KVExtractionDTO
	 */
	private void setNewAttributeOfKvExtractionDTO(List<KVExtractionDTO> kvExtractionList) {
		if (null != kvExtractionList) {
			for (KVExtractionDTO kvExtractionDTO : kvExtractionList) {
				kvExtractionDTO.setNew(true);
			}
		}

	}
}
