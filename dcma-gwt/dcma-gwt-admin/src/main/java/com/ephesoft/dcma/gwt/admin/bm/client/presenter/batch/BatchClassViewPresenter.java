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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.batch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.batch.BatchClassView;
import com.ephesoft.dcma.gwt.core.client.RandomIdGenerator;
import com.ephesoft.dcma.gwt.core.shared.BatchClassFieldDTO;
import com.ephesoft.dcma.gwt.core.shared.CmisConfigurationDTO;
import com.ephesoft.dcma.gwt.core.shared.DocumentTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.EmailConfigurationDTO;
import com.ephesoft.dcma.gwt.core.shared.ScannerMasterDTO;
import com.ephesoft.dcma.gwt.core.shared.WebScannerConfigurationDTO;
import com.google.gwt.event.shared.HandlerManager;

/**
 * Presenter for view that is used to show the batch details in editable mode and in detail view.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class BatchClassViewPresenter extends AbstractBatchClassPresenter<BatchClassView> {

	/**
	 * Presenter for batch class detail view.
	 */
	private final BatchClassDetailPresenter batchClassDetailPresenter;

	/**
	 * Presenter for batch class view in editable mode.
	 */
	private final EditBatchClassPresenter editBatchClassPresenter;

	/**
	 * Constructor.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view BatchClassView
	 */
	public BatchClassViewPresenter(final BatchClassManagementController controller, final BatchClassView view) {
		super(controller, view);
		this.batchClassDetailPresenter = new BatchClassDetailPresenter(controller, view.getBatchClassDetailView());
		this.editBatchClassPresenter = new EditBatchClassPresenter(controller, view.getEditBatchClassView());
	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	public void bind() {
		this.batchClassDetailPresenter.bind();
		if (controller.getBatchClass().getModules() != null) {
			view.createModuleList(controller.getBatchClass().getModules());
		}

		if (controller.getBatchClass().getDocuments() != null) {
			view.createDocumentTypeList(controller.getBatchClass().getDocuments());
		}
		if (controller.getBatchClass().getEmailConfiguration() != null) {
			view.createEmailList(controller.getBatchClass().getEmailConfiguration());
		}
		if (controller.getBatchClass().getBatchClassField() != null) {
			view.createBatchClassFieldList(controller.getBatchClass().getBatchClassField());
		}

		if (controller.getBatchClass().getScannerConfiguration() != null) {
			view.createScannerList(controller.getBatchClass().getScannerConfiguration());
		}
		if (controller.getBatchClass().getCmisConfiguration() != null) {
			view.createCmisList(controller.getBatchClass().getCmisConfiguration());
		}
	}

	/**
	 * To get Batch Class Detail Presenter.
	 * 
	 * @return BatchClassDetailPresenter
	 */
	public BatchClassDetailPresenter getBatchClassDetailPresenter() {
		return batchClassDetailPresenter;
	}

	/**
	 * To get Edit Batch Class Presenter.
	 * 
	 * @return EditBatchClassPresenter
	 */
	public EditBatchClassPresenter getEditBatchClassPresenter() {
		return editBatchClassPresenter;
	}

	/**
	 * To show Batch Class Detail View.
	 */
	public void showBatchClassDetailView() {
		view.getBatchClassViewVerticalPanel().setVisible(Boolean.TRUE);
		view.getBatchClassConfigVerticalPanel().setVisible(Boolean.FALSE);
	}

	/**
	 * This method works when edit Batch properties button is clicked.
	 */
	public void onEditBatchPropertiesButtonClicked() {
		controller.setAdd(false);
		view.getBatchClassViewVerticalPanel().setVisible(Boolean.FALSE);
		view.getBatchClassConfigVerticalPanel().setVisible(Boolean.TRUE);
		controller.getBatchClass().setDirty(Boolean.TRUE);
		editBatchClassPresenter.bind();
	}

	/**
	 * This method works when add document button is clicked.
	 */
	public void onAddDocumentButtonClicked() {
		DocumentTypeDTO documentTypeDTO = new DocumentTypeDTO();
		documentTypeDTO.setNew(true);
		documentTypeDTO.setName(BatchClassManagementConstants.EMPTY_STRING);
		documentTypeDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		documentTypeDTO.setBatchClass(controller.getBatchClass());
		controller.setAdd(true);
		controller.setSelectedDocument(documentTypeDTO);
		controller.getMainPresenter().showDocumentTypeView(true);
	}

	/**
	 * This method works when edit document button is clicked.
	 * 
	 * @param identifier String
	 */
	public void onEditDocumentButtonClicked(final String identifier) {
		controller.setSelectedDocument(controller.getBatchClass().getDocTypeByIdentifier(identifier));
		controller.setAdd(false);
		controller.getMainPresenter().showDocumentTypeView(false);
	}

	/**
	 * This method works when delete document button is clicked.
	 * 
	 * @param identifier String
	 */
	public void onDeleteDocumentButtonClicked(final String identifier) {
		controller.getBatchClass().getDocTypeByIdentifier(identifier).setDeleted(true);
		controller.getBatchClass().setDirty(true);
		controller.getMainPresenter().showBatchClassView();
	}

	/**
	 * This method works when add email button is clicked.
	 */
	public void onAddEmailButtonClicked() {
		EmailConfigurationDTO emailConfigurationDTO = createEmailConfigurationDTOObject();
		controller.setAdd(true);
		controller.setSelectedEmailConfiguration(emailConfigurationDTO);
		controller.getMainPresenter().showEmailView(true);
	}

	/**
	 * This method works when add cmis button is clicked.
	 */
	public void onAddCmisButtonClicked() {
		CmisConfigurationDTO cmisConfigurationDTO = createCmisConfigurationDTOObject();
		controller.setAdd(true);
		controller.setSelectedCmisConfiguration(cmisConfigurationDTO);
		controller.getMainPresenter().showCmisImporterView(true);
	}

	/**
	 * To create Email configuration DTO Object.
	 * 
	 * @return EmailConfigurationDTO
	 */
	public EmailConfigurationDTO createEmailConfigurationDTOObject() {
		EmailConfigurationDTO emailConfigurationDTO = new EmailConfigurationDTO();
		emailConfigurationDTO.setNew(true);
		emailConfigurationDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		emailConfigurationDTO.setUserName(BatchClassManagementConstants.EMPTY_STRING);
		emailConfigurationDTO.setBatchClass(controller.getBatchClass());
		return emailConfigurationDTO;
	}

	/**
	 * To create cmis configuration DTO Object.
	 * 
	 * @return CmisConfigurationDTO
	 */
	public CmisConfigurationDTO createCmisConfigurationDTOObject() {
		CmisConfigurationDTO cmisConfigurationDTO = new CmisConfigurationDTO();
		cmisConfigurationDTO.setNew(true);
		cmisConfigurationDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		cmisConfigurationDTO.setUserName(BatchClassManagementConstants.EMPTY_STRING);
		cmisConfigurationDTO.setBatchClass(controller.getBatchClass());
		return cmisConfigurationDTO;
	}

	/**
	 * To create scanner configuration DTO Object.
	 * 
	 * @return WebScannerConfigurationDTO
	 */
	public WebScannerConfigurationDTO createScannerConfigurationDTOObject() {
		WebScannerConfigurationDTO sConfigurationDTO = new WebScannerConfigurationDTO();
		sConfigurationDTO.setNew(true);
		sConfigurationDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		// create the parent DTO
		sConfigurationDTO.setParent(null);

		// set the children

		Map<String, ScannerMasterDTO> masterConfig = controller.getBatchClass().getScannerMasterMap();
		Collection<WebScannerConfigurationDTO> children = new ArrayList<WebScannerConfigurationDTO>();
		for (ScannerMasterDTO dto : masterConfig.values()) {
			if (dto.getName().equalsIgnoreCase(AdminConstants.WEB_SCANNER_PROFILE_TEXT_CONST)) {
				// set as parent
				sConfigurationDTO.setName(dto.getName());
				sConfigurationDTO.setDescription(dto.getDescription());
				sConfigurationDTO.setMandatory(dto.isMandatory());
				sConfigurationDTO.setMultiValue(dto.isMultiValue());
				sConfigurationDTO.setDataType(dto.getType());
				sConfigurationDTO.setBatchClass(controller.getBatchClass());

			} else {
				// set as child node
				WebScannerConfigurationDTO sChildDTO = new WebScannerConfigurationDTO();
				sChildDTO.setName(dto.getName());
				sChildDTO.setDescription(dto.getDescription());
				sChildDTO.setMandatory(dto.isMandatory());
				sChildDTO.setMultiValue(dto.isMultiValue());
				sChildDTO.setSampleValue(dto.getSampleValue());
				sChildDTO.setDataType(dto.getType());
				sChildDTO.setBatchClass(controller.getBatchClass());
				sChildDTO.setParent(sConfigurationDTO);
				children.add(sChildDTO);
			}
		}
		sConfigurationDTO.setChildren(children);
		sConfigurationDTO.setBatchClass(controller.getBatchClass());
		return sConfigurationDTO;
	}

	/**
	 * This method works when add scanner button is clicked.
	 */
	public void onAddScannerButtonClicked() {
		WebScannerConfigurationDTO scannerConfigurationDTO = createScannerConfigurationDTOObject();
		controller.setAdd(true);
		controller.setSelectedWebScannerConfiguration(scannerConfigurationDTO);
		controller.getMainPresenter().showScannerView(true);
	}

	/**
	 * This method works when edit scanner button is clicked.
	 * 
	 * @param identifier String
	 */
	public void onEditScannerButtonClicked(final String identifier) {
		controller.setSelectedWebScannerConfiguration(controller.getBatchClass().getWebScannerConfigurationById(identifier));
		controller.setAdd(false);
		controller.getMainPresenter().showScannerView(false);
	}

	/**
	 * This method works when edit email button is clicked.
	 * 
	 * @param identifier String
	 */
	public void onEditEmailButtonClicked(final String identifier) {
		controller.setSelectedEmailConfiguration(controller.getBatchClass().getEmailConfigurationDTOByIdentifier(identifier));
		controller.setAdd(false);
		controller.getMainPresenter().showEmailView(false);
	}

	/**
	 * This method works when edit cmis button is clicked.
	 * 
	 * @param identifier String
	 */
	public void onEditCmisButtonClicked(final String identifier) {
		controller.setSelectedCmisConfiguration(controller.getBatchClass().getCmisConfigurationDTOByIdentifier(identifier));
		controller.setAdd(false);
		controller.getMainPresenter().showCmisImporterView(false);
	}

	/**
	 * This method works when delete scanner button is clicked.
	 * 
	 * @param identifier String
	 */
	public void onDeleteScannerButtonClicked(final String identifier) {
		controller.getBatchClass().getScannerConfigurationByIdentifier(identifier).setDeleted(true);
		controller.getBatchClass().setDirty(true);
		controller.getMainPresenter().showBatchClassView();
	}

	/**
	 * This method works when delete email button is clicked.
	 * 
	 * @param identifier String
	 */
	public void onDeleteEmailButtonClicked(final String identifier) {
		controller.getBatchClass().getEmailConfigurationDTOByIdentifier(identifier).setDeleted(true);
		controller.getBatchClass().setDirty(true);
		controller.getMainPresenter().showBatchClassView();
	}

	/**
	 * This method works when delete cmis button is clicked.
	 * 
	 * @param identifier String
	 */
	public void onDeleteCmisButtonClicked(final String identifier) {
		controller.getBatchClass().getCmisConfigurationDTOByIdentifier(identifier).setDeleted(true);
		controller.getBatchClass().setDirty(true);
		controller.getMainPresenter().showBatchClassView();
	}

	/**
	 * This method works when add batch class button is clicked.
	 */
	public void onAddBatchClassFieldButtonClicked() {
		BatchClassFieldDTO batchClassFieldDTO = createBatchClassFieldDTOObject();
		controller.setAdd(true);
		controller.setSelectedBatchClassField(batchClassFieldDTO);
		controller.getMainPresenter().showBatchClassFieldView(true);
	}

	/**
	 * To create Batch Class Field DTO Object.
	 * 
	 * @return BatchClassFieldDTO
	 */
	public BatchClassFieldDTO createBatchClassFieldDTOObject() {
		BatchClassFieldDTO batchClassFieldDTO = new BatchClassFieldDTO();
		batchClassFieldDTO.setNew(true);
		batchClassFieldDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		batchClassFieldDTO.setBatchClass(controller.getBatchClass());
		if (controller.getBatchClass() != null) {
			// setting the field order
			Collection<BatchClassFieldDTO> bcflist = controller.getBatchClass().getBatchClassField();
			if (null != bcflist && !bcflist.isEmpty()) {
				ArrayList<Integer> batchClassFieldOrderList = new ArrayList<Integer>();
				for (BatchClassFieldDTO batchClassFieldDto : bcflist) {
					batchClassFieldOrderList.add(Integer.parseInt(batchClassFieldDto.getFieldOrderNumber()));
				}
				Integer maxFieldOrder = Collections.max(batchClassFieldOrderList);
				if (maxFieldOrder > (Integer.MAX_VALUE - BatchClassManagementConstants.FIELD_ORDER_DIFFERENCE)) {
					// setting the field order to be empty if generated field
					// order is not in integer range
					batchClassFieldDTO.setFieldOrderNumber(BatchClassManagementConstants.EMPTY_STRING);
				} else {
					Integer newFieldOrder = Collections.max(batchClassFieldOrderList)
							+ BatchClassManagementConstants.FIELD_ORDER_DIFFERENCE;
					batchClassFieldDTO.setFieldOrderNumber(newFieldOrder.toString());
				}
			} else {
				batchClassFieldDTO.setFieldOrderNumber(BatchClassManagementConstants.INITIAL_FIELD_ORDER_NUMBER);
			}
		}
		return batchClassFieldDTO;
	}

	/**
	 * This method works when edit batch field class button is clicked.
	 * 
	 * @param identifier String
	 */
	public void onEditBatchClassFieldButtonClicked(final String identifier) {
		controller.setSelectedBatchClassField(controller.getBatchClass().getBatchClassFieldDTOByIdentifier(identifier));
		controller.setAdd(false);
		controller.getMainPresenter().showBatchClassFieldView(false);
	}

	/**
	 * This method works when delete batch field class button is clicked.
	 * 
	 * @param identifier String
	 */
	public void onDeleteBatchClassFieldButtonClicked(final String identifier) {
		controller.getBatchClass().getBatchClassFieldDTOByIdentifier(identifier).setDeleted(true);
		controller.getBatchClass().setDirty(true);
		controller.getMainPresenter().showBatchClassView();
	}

	/**
	 * For event handling.
	 * 
	 * @param eventBus HandlerManager
	 */
	@Override
	public void injectEvents(final HandlerManager eventBus) {
		// event handling is done here.
	}
}
