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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.batch.BatchClassViewPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.batchclassfield.BatchClassFieldListPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.cmisimporter.CmisImporterListPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.documenttype.CopyDocumentTypePresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.documenttype.DocumentTypeListPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.email.EmailListPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.module.ModuleListPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.scanner.ScannerListPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.batchclassfield.BatchClassFieldListView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.cmisimporter.CmisImporterListView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.documenttype.CopyDocumentView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.documenttype.DocumentTypeListView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.email.EmailListView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.module.ConfigureModuleView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.module.ModuleListView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.scanner.ScannerListView;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView;
import com.ephesoft.dcma.gwt.core.client.ui.table.Record;
import com.ephesoft.dcma.gwt.core.client.ui.table.Table;
import com.ephesoft.dcma.gwt.core.shared.BatchClassFieldDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassModuleDTO;
import com.ephesoft.dcma.gwt.core.shared.CmisConfigurationDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.DocumentTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.EmailConfigurationDTO;
import com.ephesoft.dcma.gwt.core.shared.WebScannerConfigurationDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class provides functionality to show individual batch class and it's child.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class BatchClassView extends View<BatchClassViewPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<DockLayoutPanel, BatchClassView> {
	}

	/**
	 * viewEditLayoutPanel LayoutPanel.
	 */
	@UiField
	protected LayoutPanel viewEditLayoutPanel;

	/**
	 * batchClassDetailView BatchClassDetailView.
	 */
	@UiField
	protected BatchClassDetailView batchClassDetailView;

	/**
	 * editBatchClassView EditBatchClassView.
	 */
	@UiField
	protected EditBatchClassView editBatchClassView;

	/**
	 * moduleListPanel LayoutPanel.
	 */
	@UiField
	protected LayoutPanel moduleListPanel;

	/**
	 * docTypeListPanel DockLayoutPanel.
	 */
	@UiField
	protected DockLayoutPanel docTypeListPanel;

	/**
	 * moduleTypeListPanel DockLayoutPanel.
	 */
	@UiField
	protected DockLayoutPanel moduleTypeListPanel;

	/**
	 * editBatchPropertiesButton Button.
	 */
	@UiField
	protected Button editBatchPropertiesButton;

	/**
	 * batchClassConfigurationCaptionPanel CaptionPanel.
	 */
	@UiField
	protected CaptionPanel batchClassConfigurationCaptionPanel;

	/**
	 * batchClassViewVerticalPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel batchClassViewVerticalPanel;

	/**
	 * batchClassConfigVerticalPanel VerticalPanel.
	 */
	@UiField
	protected VerticalPanel batchClassConfigVerticalPanel;

	/**
	 * moduleListView ModuleListView.
	 */
	private final ModuleListView moduleListView;

	/**
	 * docTypeListView DocumentTypeListView.
	 */
	private final DocumentTypeListView docTypeListView;

	/**
	 * emailListView EmailListView.
	 */
	private final EmailListView emailListView;

	/**
	 * scannerListView ScannerListView.
	 */
	private final ScannerListView scannerListView;

	/**
	 * addModuleView ConfigureModuleView.
	 */
	private final ConfigureModuleView addModuleView;

	/**
	 * docTypeLayoutPanel LayoutPanel.
	 */
	@UiField
	protected LayoutPanel docTypeLayoutPanel;

	/**
	 * emailLayoutPanel LayoutPanel.
	 */
	@UiField
	protected LayoutPanel emailLayoutPanel;

	/**
	 * scannerLayoutPanel LayoutPanel.
	 */
	@UiField
	protected LayoutPanel scannerLayoutPanel;

	/**
	 * addDocumentButton Button.
	 */
	@UiField
	protected Button addDocumentButton;

	/**
	 * editDocumentButton Button.
	 */
	@UiField
	protected Button editDocumentButton;

	/**
	 * copyDocumentButton Button.
	 */
	@UiField
	protected Button copyDocumentButton;

	/**
	 * deleteDocumentButton Button.
	 */
	@UiField
	protected Button deleteDocumentButton;

	/**
	 * editModuleButton Button.
	 */
	@UiField
	protected Button editModuleButton;

	/**
	 * addModuleButton Button.
	 */
	@UiField
	protected Button addModuleButton;

	/**
	 * buttonPanel HorizontalPanel.
	 */
	@UiField
	protected HorizontalPanel buttonPanel;

	/**
	 * documentButtonPanel HorizontalPanel.
	 */
	@UiField
	protected HorizontalPanel documentButtonPanel;

	/**
	 * emailListPanel DockLayoutPanel.
	 */
	@UiField
	protected DockLayoutPanel emailListPanel;

	/**
	 * emailButtonPanel HorizontalPanel.
	 */
	@UiField
	protected HorizontalPanel emailButtonPanel;

	/**
	 * scannerButtonPanel HorizontalPanel.
	 */
	@UiField
	protected HorizontalPanel scannerButtonPanel;

	/**
	 * addEmailButton Button.
	 */
	@UiField
	protected Button addEmailButton;

	/**
	 * editEmailButton Button.
	 */
	@UiField
	protected Button editEmailButton;

	/**
	 * addScannerButton Button.
	 */
	@UiField
	protected Button addScannerButton;

	/**
	 * editScannerButton Button.
	 */
	@UiField
	protected Button editScannerButton;

	/**
	 * deleteScannerButton Button.
	 */
	@UiField
	protected Button deleteScannerButton;

	/**
	 * deleteEmailButton Button.
	 */
	@UiField
	protected Button deleteEmailButton;

	/**
	 * batchClassFieldListView BatchClassFieldListView.
	 */
	private final BatchClassFieldListView batchClassFieldListView;

	/**
	 * batchClassFieldListPresenter BatchClassFieldListPresenter.
	 */
	private BatchClassFieldListPresenter batchClassFieldListPresenter;

	/**
	 * moduleListPresenter ModuleListPresenter.
	 */
	private ModuleListPresenter moduleListPresenter;

	/**
	 * emailListPresenter EmailListPresenter.
	 */
	private EmailListPresenter emailListPresenter;

	/**
	 * scannerListPresenter ScannerListPresenter.
	 */
	private ScannerListPresenter scannerListPresenter;

	/**
	 * batchClassFieldLayoutPanel LayoutPanel.
	 */
	@UiField
	protected LayoutPanel batchClassFieldLayoutPanel;

	/**
	 * batchClassFieldListPanel DockLayoutPanel.
	 */
	@UiField
	protected DockLayoutPanel batchClassFieldListPanel;

	/**
	 * batchClassFieldButtonPanel HorizontalPanel.
	 */
	@UiField
	protected HorizontalPanel batchClassFieldButtonPanel;

	/**
	 * addBatchClassFieldButton Button.
	 */
	@UiField
	protected Button addBatchClassFieldButton;

	/**
	 * editBatchClassFieldButton Button.
	 */
	@UiField
	protected Button editBatchClassFieldButton;

	/**
	 * deleteBatchClassFieldButton Button.
	 */
	@UiField
	protected Button deleteBatchClassFieldButton;

	/**
	 * cmisLayoutPanel LayoutPanel.
	 */
	@UiField
	protected LayoutPanel cmisLayoutPanel;

	/**
	 * cmisListPanel DockLayoutPanel.
	 */
	@UiField
	protected DockLayoutPanel cmisListPanel;

	/**
	 * cmisButtonPanel HorizontalPanel.
	 */
	@UiField
	protected HorizontalPanel cmisButtonPanel;

	/**
	 * addCmisButton Button.
	 */
	@UiField
	protected Button addCmisButton;

	/**
	 * editCmisButton Button.
	 */
	@UiField
	protected Button editCmisButton;

	/**
	 * deleteCmisButton Button.
	 */
	@UiField
	protected Button deleteCmisButton;

	/**
	 * cmisListView CmisImporterListView.
	 */
	private final CmisImporterListView cmisListView;

	/**
	 * cmisListPresenter CmisImporterListPresenter.
	 */
	private CmisImporterListPresenter cmisListPresenter;

	/**
	 * documentTypeListPresenter DocumentTypeListPresenter.
	 */
	private DocumentTypeListPresenter documentTypeListPresenter;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * TWENTY_PIXEL String.
	 */
	private static final String TWENTY_PIXEL = "20px";

	/**
	 * Constructor.
	 */
	public BatchClassView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		moduleListView = new ModuleListView();
		addDocumentButton.setText(AdminConstants.ADD_BUTTON);
		addScannerButton.setText(AdminConstants.ADD_BUTTON);
		addEmailButton.setText(AdminConstants.ADD_BUTTON);
		addCmisButton.setText(AdminConstants.ADD_BUTTON);
		editDocumentButton.setText(AdminConstants.EDIT_BUTTON);
		copyDocumentButton.setText(AdminConstants.COPY_BUTTON);
		editEmailButton.setText(AdminConstants.EDIT_BUTTON);
		editCmisButton.setText(AdminConstants.EDIT_BUTTON);
		editScannerButton.setText(AdminConstants.EDIT_BUTTON);

		deleteDocumentButton.setText(AdminConstants.DELETE_BUTTON);
		deleteEmailButton.setText(AdminConstants.DELETE_BUTTON);
		deleteCmisButton.setText(AdminConstants.DELETE_BUTTON);
		deleteScannerButton.setText(AdminConstants.DELETE_BUTTON);
		docTypeListView = new DocumentTypeListView();
		moduleListPanel.add(moduleListView.listView);
		docTypeLayoutPanel.add(docTypeListView.listView);
		emailListView = new EmailListView();
		cmisListView = new CmisImporterListView();
		scannerListView = new ScannerListView();
		emailLayoutPanel.add(emailListView.listView);
		cmisLayoutPanel.add(cmisListView.listView);
		scannerLayoutPanel.add(scannerListView.listView);
		batchClassConfigurationCaptionPanel.setCaptionHTML(AdminConstants.BATCH_CLASS_CONFIGURATION_HTML);
		editBatchPropertiesButton.setText(AdminConstants.EDIT_BUTTON);

		addDocumentButton.setHeight(TWENTY_PIXEL);
		addScannerButton.setHeight(TWENTY_PIXEL);
		addEmailButton.setHeight(TWENTY_PIXEL);
		addCmisButton.setHeight(TWENTY_PIXEL);
		editDocumentButton.setHeight(TWENTY_PIXEL);
		copyDocumentButton.setHeight(TWENTY_PIXEL);
		editEmailButton.setHeight(TWENTY_PIXEL);
		editCmisButton.setHeight(TWENTY_PIXEL);
		editScannerButton.setHeight(TWENTY_PIXEL);
		deleteDocumentButton.setHeight(TWENTY_PIXEL);
		deleteEmailButton.setHeight(TWENTY_PIXEL);
		deleteCmisButton.setHeight(TWENTY_PIXEL);
		deleteScannerButton.setHeight(TWENTY_PIXEL);

		addModuleView = new ConfigureModuleView();
		editModuleButton.setText(AdminConstants.EDIT_BUTTON);
		editModuleButton.setHeight(TWENTY_PIXEL);
		addModuleButton.setText(AdminConstants.EDIT_LIST_BUTTON);
		addModuleButton.setHeight(TWENTY_PIXEL);
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		buttonPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		documentButtonPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		emailButtonPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		scannerButtonPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		buttonPanel.addStyleName(AdminConstants.PADDING_STYLE_TOPPADD);
		documentButtonPanel.addStyleName(AdminConstants.PADDING_STYLE_TOPPADD);
		emailButtonPanel.addStyleName(AdminConstants.PADDING_STYLE_TOPPADD);
		cmisButtonPanel.addStyleName(AdminConstants.PADDING_STYLE_TOPPADD);
		scannerButtonPanel.addStyleName(AdminConstants.PADDING_STYLE_TOPPADD);

		addBatchClassFieldButton.setText(AdminConstants.ADD_BUTTON);
		editBatchClassFieldButton.setText(AdminConstants.EDIT_BUTTON);
		deleteBatchClassFieldButton.setText(AdminConstants.DELETE_BUTTON);
		addBatchClassFieldButton.setHeight(TWENTY_PIXEL);
		editBatchClassFieldButton.setHeight(TWENTY_PIXEL);
		deleteBatchClassFieldButton.setHeight(TWENTY_PIXEL);
		batchClassFieldButtonPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		batchClassFieldButtonPanel.addStyleName(AdminConstants.PADDING_STYLE_TOPPADD);
		batchClassFieldListView = new BatchClassFieldListView();
		batchClassFieldListPresenter = null;
		documentTypeListPresenter = null;
		moduleListPresenter = null;
		emailListPresenter = null;
		cmisListPresenter = null;
		scannerListPresenter = null;

		batchClassFieldLayoutPanel.add(batchClassFieldListView.listView);
	}

	/**
	 * To create module list.
	 * 
	 * @param modules Collection<BatchClassModuleDTO>
	 */
	public void createModuleList(Collection<BatchClassModuleDTO> modules) {
		int maxResult = 0;
		List<BatchClassModuleDTO> modulesList = new ArrayList<BatchClassModuleDTO>(modules);

		// Sort modules
		presenter.getController().getMainPresenter().sortModulesList(modulesList);
		List<Record> recordList = setModuleList(modulesList);
		moduleListPresenter = new ModuleListPresenter(presenter.getController(), moduleListView);
		moduleListPresenter.setModuleDTOList(modulesList);
		maxResult = Math.min(recordList.size(), Table.visibleRecodrCount);

		moduleListView.listView.initTable(recordList.size(), moduleListPresenter, recordList.subList(0, maxResult), true, false,
				moduleListPresenter, moduleListPresenter, true);
	}

	/**
	 * To set Module List.
	 * 
	 * @param modules Collection<BatchClassModuleDTO>
	 * @return List<Record>
	 */
	public List<Record> setModuleList(Collection<BatchClassModuleDTO> modules) {

		List<Record> recordList = new LinkedList<Record>();
		for (final BatchClassModuleDTO batchClassModuleDTO : modules) {
			if (!batchClassModuleDTO.isDeleted()) {
				Record record = new Record(batchClassModuleDTO.getIdentifier());
				record.addWidget(moduleListView.name, new Label(batchClassModuleDTO.getModule().getName()));
				record.addWidget(moduleListView.description, new Label(batchClassModuleDTO.getModule().getDescription()));
				recordList.add(record);
			}
		}
		return recordList;
	}

	/**
	 * To create Scanner List.
	 * 
	 * @param scannerConfigurationDTOs Collection<WebScannerConfigurationDTO>
	 */
	public void createScannerList(Collection<WebScannerConfigurationDTO> scannerConfigurationDTOs) {
		int maxResult = 0;
		List<Record> recordList = setScannerList(scannerConfigurationDTOs);
		scannerListPresenter = new ScannerListPresenter(presenter.getController(), scannerListView);
		scannerListPresenter.setScannerConfigurationDTOList(scannerConfigurationDTOs);
		maxResult = Math.min(recordList.size(), Table.visibleRecodrCount);
		scannerListView.listView.initTable(recordList.size(), scannerListPresenter, recordList.subList(0, maxResult), true, false,
				scannerListPresenter, null, false);
	}

	/**
	 * To set Scanner List.
	 * 
	 * @param scannerConfigurationDTOs Collection<WebScannerConfigurationDTO>
	 * @return List<Record>
	 */
	public List<Record> setScannerList(Collection<WebScannerConfigurationDTO> scannerConfigurationDTOs) {
		List<Record> recordList = new LinkedList<Record>();
		for (final WebScannerConfigurationDTO scannerConfigurationDTO : scannerConfigurationDTOs) {
			if (scannerConfigurationDTO.getParent() == null) {
				Record record = new Record(scannerConfigurationDTO.getIdentifier());
				record.addWidget(scannerListView.profileName, new Label(String.valueOf(scannerConfigurationDTO.getValue())));
				recordList.add(record);
			}
		}
		return recordList;
	}

	/**
	 * To create Cmis List.
	 * 
	 * @param cmisConfigurationDTOs Collection<CmisConfigurationDTO>
	 */
	public void createCmisList(Collection<CmisConfigurationDTO> cmisConfigurationDTOs) {
		int maxResult = 0;
		List<Record> recordList = setCmisList(cmisConfigurationDTOs);
		cmisListPresenter = new CmisImporterListPresenter(presenter.getController(), cmisListView);
		cmisListPresenter.setCmisConfigurationDTOList(cmisConfigurationDTOs);
		maxResult = Math.min(recordList.size(), Table.visibleRecodrCount);
		cmisListView.listView.initTable(recordList.size(), cmisListPresenter, recordList.subList(0, maxResult), true, false,
				cmisListPresenter, null, false);
	}

	/**
	 * To create Email List.
	 * 
	 * @param emailConfigurationDTOs Collection<EmailConfigurationDTO>
	 */
	public void createEmailList(Collection<EmailConfigurationDTO> emailConfigurationDTOs) {
		int maxResult = 0;
		List<Record> recordList = setEmailList(emailConfigurationDTOs);
		emailListPresenter = new EmailListPresenter(presenter.getController(), emailListView);
		emailListPresenter.setEmailConfigurationDTOList(emailConfigurationDTOs);
		maxResult = Math.min(recordList.size(), Table.visibleRecodrCount);
		emailListView.listView.initTable(recordList.size(), emailListPresenter, recordList.subList(0, maxResult), true, false,
				emailListPresenter, null, false);
	}

	/**
	 * To set Email List.
	 * 
	 * @param emailConfigurationDTOs Collection<EmailConfigurationDTO>
	 * @return List<Record>
	 */
	public List<Record> setEmailList(Collection<EmailConfigurationDTO> emailConfigurationDTOs) {
		List<Record> recordList = new LinkedList<Record>();
		for (final EmailConfigurationDTO emailConfigurationDTO : emailConfigurationDTOs) {
			Record record = new Record(emailConfigurationDTO.getIdentifier());
			record.addWidget(emailListView.userName, new Label(emailConfigurationDTO.getUserName()));
			record.addWidget(emailListView.password, new Label(emailConfigurationDTO.getPassword()));
			record.addWidget(emailListView.serverName, new Label(emailConfigurationDTO.getServerName()));
			record.addWidget(emailListView.serverType, new Label(emailConfigurationDTO.getServerType()));
			record.addWidget(emailListView.folderName, new Label(emailConfigurationDTO.getFolderName()));
			Integer portNumber = emailConfigurationDTO.getPortNumber();
			if (portNumber != null) {
				record.addWidget(emailListView.portNumbner, new Label(String.valueOf(portNumber)));
			} else {
				record.addWidget(emailListView.portNumbner, new Label(AdminConstants.EMPTY_STRING));
			}

			Boolean isSSL = emailConfigurationDTO.getIsSSL();
			if (isSSL != null) {
				record.addWidget(emailListView.isSSL, new Label(String.valueOf(isSSL)));
			} else {
				record.addWidget(emailListView.isSSL, new Label(Boolean.FALSE.toString()));
			}
			recordList.add(record);
		}
		return recordList;
	}

	/**
	 * To set Cmis List.
	 * 
	 * @param cmisImporterConfigurationDTOs
	 * @return List<Record>
	 */
	public List<Record> setCmisList(Collection<CmisConfigurationDTO> cmisImporterConfigurationDTOs) {

		List<Record> recordList = new LinkedList<Record>();
		for (final CmisConfigurationDTO cmisConfigurationDTO : cmisImporterConfigurationDTOs) {
			Record record = new Record(cmisConfigurationDTO.getIdentifier());
			record.addWidget(cmisListView.userName, new Label(cmisConfigurationDTO.getUserName()));
			record.addWidget(cmisListView.password, new Label(cmisConfigurationDTO.getPassword()));
			record.addWidget(cmisListView.serverURL, new Label(cmisConfigurationDTO.getServerURL()));
			record.addWidget(cmisListView.cmisProperty, new Label(cmisConfigurationDTO.getCmisProperty()));
			record.addWidget(cmisListView.folderName, new Label(cmisConfigurationDTO.getFolderName()));
			record.addWidget(cmisListView.fileExtn, new Label(cmisConfigurationDTO.getFileExtension()));
			record.addWidget(cmisListView.value, new Label(cmisConfigurationDTO.getValue()));
			record.addWidget(cmisListView.valueToUpdate, new Label(cmisConfigurationDTO.getValueToUpdate()));
			record.addWidget(cmisListView.repositoryID, new Label(cmisConfigurationDTO.getRepositoryID()));
			recordList.add(record);
		}
		return recordList;
	}

	/**
	 * To create Batch Class Field List.
	 * 
	 * @param batchClassFieldDTOs Collection<BatchClassFieldDTO>
	 */
	public void createBatchClassFieldList(Collection<BatchClassFieldDTO> batchClassFieldDTOs) {
		int maxResult = 0;
		List<Record> recordList = setBatchClassFieldList(batchClassFieldDTOs);
		batchClassFieldListPresenter = new BatchClassFieldListPresenter(presenter.getController(), batchClassFieldListView);
		batchClassFieldListPresenter.setBatchClassFieldDTOList(batchClassFieldDTOs);
		maxResult = Math.min(recordList.size(), Table.visibleRecodrCount);
		batchClassFieldListView.listView.initTable(recordList.size(), batchClassFieldListPresenter, recordList.subList(0, maxResult),
				true, false, batchClassFieldListPresenter, null, false);
	}

	/**
	 * To set Batch Class Field List.
	 * 
	 * @param batchClassFieldDTOs Collection<BatchClassFieldDTO>
	 * @return List<Record>
	 */
	public List<Record> setBatchClassFieldList(Collection<BatchClassFieldDTO> batchClassFieldDTOs) {

		List<Record> recordList = new LinkedList<Record>();
		for (final BatchClassFieldDTO batchClassFieldDTO : batchClassFieldDTOs) {
			Record record = new Record(batchClassFieldDTO.getIdentifier());
			record.addWidget(batchClassFieldListView.name, new Label(batchClassFieldDTO.getName()));
			record.addWidget(batchClassFieldListView.description, new Label(batchClassFieldDTO.getDescription()));
			record.addWidget(batchClassFieldListView.type, new Label(batchClassFieldDTO.getDataType().name()));
			record.addWidget(batchClassFieldListView.fdOrder, new Label(String.valueOf(batchClassFieldDTO.getFieldOrderNumber())));
			record.addWidget(batchClassFieldListView.sampleValue, new Label(String.valueOf(batchClassFieldDTO.getSampleValue())));
			record.addWidget(batchClassFieldListView.validationPattern, new Label(batchClassFieldDTO.getValidationPattern()));
			recordList.add(record);
		}
		return recordList;
	}

	/**
	 * To create Document Type List.
	 * 
	 * @param documentTypeDTOs Collection<DocumentTypeDTO>
	 */
	public void createDocumentTypeList(Collection<DocumentTypeDTO> documentTypeDTOs) {
		int maxResult = 0;
		List<Record> recordList = setDocumentTypeList(documentTypeDTOs);
		documentTypeListPresenter = new DocumentTypeListPresenter(presenter.getController(), docTypeListView);
		documentTypeListPresenter.setDocumentTypeDTOList(documentTypeDTOs);
		maxResult = Math.min(recordList.size(), Table.visibleRecodrCount);
		docTypeListView.listView.initTable(recordList.size(), documentTypeListPresenter, recordList.subList(0, maxResult), true,
				false, documentTypeListPresenter, null, false);
	}

	/**
	 * To set Document Type List.
	 * 
	 * @param documentTypeDTOs Collection<DocumentTypeDTO>
	 * @return List<Record>
	 */
	public List<Record> setDocumentTypeList(Collection<DocumentTypeDTO> documentTypeDTOs) {

		List<Record> recordList = new LinkedList<Record>();
		for (final DocumentTypeDTO documentTypeDTO : documentTypeDTOs) {
			if (!documentTypeDTO.getName().equalsIgnoreCase(AdminConstants.DOCUMENT_TYPE_UNKNOWN)) {
				CheckBox isHidden = new CheckBox();
				isHidden.setValue(documentTypeDTO.isHidden());
				isHidden.setEnabled(false);
				Record record = new Record(documentTypeDTO.getIdentifier());
				record.addWidget(docTypeListView.name, new Label(documentTypeDTO.getName()));
				record.addWidget(docTypeListView.description, new Label(documentTypeDTO.getDescription()));
				record.addWidget(docTypeListView.isHidden, isHidden);
				recordList.add(record);
			}
		}
		return recordList;
	}

	/**
	 * To get Batch Class Detail View.
	 * 
	 * @return BatchClassDetailView
	 */
	public BatchClassDetailView getBatchClassDetailView() {
		return batchClassDetailView;
	}

	/**
	 * To get Edit Batch Class View.
	 * 
	 * @return EditBatchClassView
	 */
	public EditBatchClassView getEditBatchClassView() {
		return editBatchClassView;
	}

	/**
	 * To get Module List View.
	 * 
	 * @return ListView
	 */
	public ListView getModuleListView() {
		return moduleListView.listView;
	}

	/**
	 * To get Doc Type List View.
	 * 
	 * @return ListView
	 */
	public ListView getDocTypeListView() {
		return docTypeListView.listView;
	}

	/**
	 * To get Batch Class View Vertical Panel.
	 * 
	 * @return VerticalPanel
	 */
	public VerticalPanel getBatchClassViewVerticalPanel() {
		return batchClassViewVerticalPanel;
	}

	/**
	 * To get edit Batch Properties Button.
	 * 
	 * @return Button
	 */
	public Button getEditBatchPropertiesButton() {
		return editBatchPropertiesButton;
	}

	/**
	 * To get add Document Button.
	 * 
	 * @return Button
	 */
	public Button getAddDocumentButton() {
		return addDocumentButton;
	}

	/**
	 * To get add Scanner Button.
	 * 
	 * @return Button
	 */
	public Button getAddScannerButton() {
		return addScannerButton;
	}

	/**
	 * To get add Email Button.
	 * 
	 * @return Button
	 */
	public Button getAddEmailButton() {
		return addEmailButton;
	}

	/**
	 * To get Batch Class Config Vertical Panel.
	 * 
	 * @return VerticalPanel
	 */
	public VerticalPanel getBatchClassConfigVerticalPanel() {
		return batchClassConfigVerticalPanel;
	}

	/**
	 * To perform operations on edit Batch Properties button click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("editBatchPropertiesButton")
	public void onEditBatchPropertiesButtonClick(ClickEvent clickEvent) {
		presenter.onEditBatchPropertiesButtonClicked();
	}

	/**
	 * To perform operations on add Document button click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("addDocumentButton")
	public void onAddDocumentButtonClick(ClickEvent clickEvent) {
		presenter.onAddDocumentButtonClicked();
	}

	/**
	 * To perform operations on add Scanner button click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("addScannerButton")
	public void onAddScannerButtonClick(ClickEvent clickEvent) {
		presenter.onAddScannerButtonClicked();
	}

	/**
	 * To perform operations on edit Document button clicked.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("editDocumentButton")
	public void onEditDocumentButtonClicked(ClickEvent clickEvent) {
		documentTypeListPresenter.onEditButtonClicked();
	}

	/**
	 * To perform operations on copy Document button clicked.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("copyDocumentButton")
	public void onCopyDocumentButtonClicked(ClickEvent clickEvent) {
		String rowIndex = docTypeListView.listView.getSelectedRowIndex();
		int rowCount = docTypeListView.listView.getTableRecordCount();
		if (rowIndex == null || rowIndex.isEmpty()) {
			if (rowCount == 0) {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NO_RECORD_TO_COPY), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.COPY_DOCUMENT_TITLE), Boolean.TRUE);
			} else {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NONE_SELECTED_WARNING), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.COPY_DOCUMENT_TITLE), Boolean.TRUE);
			}
			return;
		}
		final DialogBox dialogBox = new DialogBox();
		final CopyDocumentView copyDocumentView = new CopyDocumentView();
		CopyDocumentTypePresenter copyDocumentTypePresenter = new CopyDocumentTypePresenter(presenter.getController(),
				copyDocumentView);
		copyDocumentTypePresenter.setDocumentTypeDTO(presenter.getController().getDocumentByIdentifier(rowIndex));
		copyDocumentView.setDialogBox(dialogBox);
		copyDocumentTypePresenter.bind();
		copyDocumentTypePresenter.showDocumentCopyView();
		copyDocumentView.getSaveButton().setFocus(true);
	}

	/**
	 * To perform operations on Delete Document button clicked.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("deleteDocumentButton")
	public void onDeleteDocumentButtonClicked(ClickEvent clickEvent) {
		final String rowIndex = docTypeListView.listView.getSelectedRowIndex();
		int rowCount = docTypeListView.listView.getTableRecordCount();
		if (rowIndex == null || rowIndex.isEmpty()) {
			if (rowCount == 0) {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NO_RECORD_TO_DELETE), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.DELETE_DOCUMENT_TITLE), Boolean.TRUE);
			} else {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NONE_SELECTED_WARNING), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.DELETE_DOCUMENT_TITLE), Boolean.TRUE);
			}
			return;
		}
		final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get()
				.getMessageValue(BatchClassManagementMessages.DELETE_DOCUMENT_TYPE_CONFORMATION), LocaleDictionary.get()
				.getConstantValue(BatchClassManagementConstants.DELETE_DOCUMENT_TITLE), Boolean.FALSE);

		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				confirmationDialog.hide();
				presenter.onDeleteDocumentButtonClicked(rowIndex);
			}

			@Override
			public void onCancelClick() {
				confirmationDialog.hide();
			}
		});

	}

	/**
	 * To perform operations on edit Module button clicked.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("editModuleButton")
	public void onEditModuleButtonClicked(ClickEvent clickEvent) {
		moduleListPresenter.onEditButtonClicked();
	}

	/**
	 * To perform operations on add Module button clicked.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("addModuleButton")
	public void onAddModuleButtonClicked(ClickEvent clickEvent) {

		String identifier = presenter.getController().getBatchClass().getIdentifier();

		if (identifier == null || identifier.isEmpty()) {
			ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.NONE_SELECTED_WARNING), LocaleDictionary.get().getConstantValue(
					BatchClassManagementConstants.EDIT_MODULE_TITLE), true);
			return;
		}

		List<String> modulesList = new ArrayList<String>(0);
		List<BatchClassModuleDTO> batchClassModuleDTOs = new ArrayList<BatchClassModuleDTO>(presenter.getController().getBatchClass()
				.getModules());

		presenter.getController().getMainPresenter().sortModulesList(batchClassModuleDTOs);
		for (BatchClassModuleDTO batchClassModule : batchClassModuleDTOs) {
			modulesList.add(batchClassModule.getWorkflowName());

		}

		presenter.getController().getMainPresenter().showAddModuleView(modulesList);
	}

	/**
	 * To perform operations on add email button clicked.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("addEmailButton")
	public void onAddEmailButtonClicked(ClickEvent clickEvent) {
		presenter.onAddEmailButtonClicked();
	}

	/**
	 * To perform operations on edit Scanner button clicked.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("editScannerButton")
	public void onEditScannerButtonClicked(ClickEvent clickEvent) {
		scannerListPresenter.onEditButtonClicked();
	}

	/**
	 * To perform operations on edit email button clicked.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("editEmailButton")
	public void onEditEmailButtonClicked(ClickEvent clickEvent) {
		emailListPresenter.onEditButtonClicked();
	}

	/**
	 * To perform operations on delete email button clicked.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("deleteEmailButton")
	public void onDeleteEmailButtonClicked(ClickEvent clickEvent) {
		final String rowIndex = emailListView.listView.getSelectedRowIndex();
		int rowCount = emailListView.listView.getTableRecordCount();
		if (rowIndex == null || rowIndex.isEmpty()) {
			if (rowCount == 0) {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NO_RECORD_TO_DELETE), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.DELETE_EMAIL_CONFIGURATION_TITLE), Boolean.TRUE);
			} else {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NONE_SELECTED_WARNING), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.DELETE_EMAIL_CONFIGURATION_TITLE), Boolean.TRUE);
			}
			return;
		}
		final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get()
				.getMessageValue(BatchClassManagementMessages.DELETE_EMAIL_CONFIGURATION_CONFORMATION), LocaleDictionary.get()
				.getConstantValue(BatchClassManagementConstants.DELETE_EMAIL_CONFIGURATION_TITLE), Boolean.FALSE);

		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				confirmationDialog.hide();
				presenter.onDeleteEmailButtonClicked(rowIndex);
			}

			@Override
			public void onCancelClick() {
				confirmationDialog.hide();
			}
		});

	}

	/**
	 * To perform operations on delete scanner button clicked.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("deleteScannerButton")
	public void onDeleteScannerButtonClicked(ClickEvent clickEvent) {
		final String rowIndex = scannerListView.listView.getSelectedRowIndex();
		int rowCount = scannerListView.listView.getTableRecordCount();
		if (rowIndex == null || rowIndex.isEmpty()) {
			if (rowCount == 0) {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NO_RECORD_TO_DELETE), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.DELETE_SCANNER_CONFIGURATION_TITLE), Boolean.TRUE);
			} else {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NONE_SELECTED_WARNING), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.DELETE_SCANNER_CONFIGURATION_TITLE), Boolean.TRUE);
			}
			return;
		} else if (rowCount <= 1) {
			ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.NO_DELETE_LAST_RECORD), LocaleDictionary.get().getConstantValue(
					BatchClassManagementConstants.DELETE_SCANNER_CONFIGURATION_TITLE), Boolean.TRUE);
			return;
		}

		final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get()
				.getMessageValue(BatchClassManagementMessages.DELETE_SCANNER_CONFORMATION), LocaleDictionary.get().getConstantValue(
				BatchClassManagementConstants.DELETE_SCANNER_CONFIGURATION_TITLE), Boolean.FALSE);

		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				confirmationDialog.hide();
				presenter.onDeleteScannerButtonClicked(rowIndex);
			}

			@Override
			public void onCancelClick() {
				confirmationDialog.hide();
			}
		});
	}

	/**
	 * To perform operations on add Batch Class Field button clicked.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("addBatchClassFieldButton")
	public void onAddBatchClassFieldButtonClicked(ClickEvent clickEvent) {
		presenter.onAddBatchClassFieldButtonClicked();
	}

	/**
	 * To perform operations on edit Batch Class Field button clicked.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("editBatchClassFieldButton")
	public void onEditBatchClassFieldButtonClicked(ClickEvent clickEvent) {
		batchClassFieldListPresenter.onEditButtonClicked();
	}

	/**
	 * To perform operations on delete Batch Class Field button clicked.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("deleteBatchClassFieldButton")
	public void onDeleteBatchClassFieldButtonClicked(ClickEvent clickEvent) {
		final String rowIndex = batchClassFieldListView.listView.getSelectedRowIndex();
		int rowCount = batchClassFieldListView.listView.getTableRecordCount();
		if (rowIndex == null || rowIndex.isEmpty()) {
			if (rowCount == 0) {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NO_RECORD_TO_DELETE), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.DELETE_BATCH_CLASS_FIELD_TITLE), Boolean.TRUE);
			} else {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NONE_SELECTED_WARNING), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.DELETE_BATCH_CLASS_FIELD_TITLE), Boolean.TRUE);
			}
			return;
		}
		final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get()
				.getMessageValue(BatchClassManagementMessages.DELETE_BATCH_CLASS_FIELD_CONFORMATION), LocaleDictionary.get()
				.getConstantValue(BatchClassManagementConstants.DELETE_BATCH_CLASS_FIELD_TITLE), Boolean.FALSE);

		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				confirmationDialog.hide();
				presenter.onDeleteBatchClassFieldButtonClicked(rowIndex);
			}

			@Override
			public void onCancelClick() {
				confirmationDialog.hide();
			}
		});

	}

	/**
	 * To get View Edit Layout Panel.
	 * 
	 * @return LayoutPanel
	 */
	public LayoutPanel getViewEditLayoutPanel() {
		return viewEditLayoutPanel;
	}

	/**
	 * To get Doc Type List Panel.
	 * 
	 * @return DockLayoutPanel
	 */
	public DockLayoutPanel getDocTypeListPanel() {
		return docTypeListPanel;
	}

	/**
	 * To get Module Type List Panel.
	 * 
	 * @return DockLayoutPanel
	 */
	public DockLayoutPanel getModuleTypeListPanel() {
		return moduleTypeListPanel;
	}

	/**
	 * To get Email List Panel.
	 * 
	 * @return DockLayoutPanel
	 */
	public DockLayoutPanel getEmailListPanel() {
		return emailListPanel;
	}

	/**
	 * To get Batch Class Field List Panel.
	 * 
	 * @return DockLayoutPanel
	 */
	public DockLayoutPanel getBatchClassFieldListPanel() {
		return batchClassFieldListPanel;
	}

	/**
	 * To set Batch Class Field List Panel.
	 * 
	 * @param batchClassFieldListPanel DockLayoutPanel
	 */
	public void setBatchClassFieldListPanel(DockLayoutPanel batchClassFieldListPanel) {
		this.batchClassFieldListPanel = batchClassFieldListPanel;
	}

	/**
	 * To get Batch Class Field Layout Panel.
	 * 
	 * @return LayoutPanel
	 */
	public LayoutPanel getBatchClassFieldLayoutPanel() {
		return batchClassFieldLayoutPanel;
	}

	/**
	 * To set Batch Class Field Layout Panel.
	 * 
	 * @param batchClassFieldLayoutPanel LayoutPanel
	 */
	public void setBatchClassFieldLayoutPanel(LayoutPanel batchClassFieldLayoutPanel) {
		this.batchClassFieldLayoutPanel = batchClassFieldLayoutPanel;
	}

	/**
	 * To get Batch Class Field List View.
	 * 
	 * @return BatchClassFieldListView
	 */
	public BatchClassFieldListView getBatchClassFieldListView() {
		return batchClassFieldListView;
	}

	/**
	 * To get add Module View.
	 * 
	 * @return the addModuleView
	 */
	public ConfigureModuleView getAddModuleView() {
		return addModuleView;
	}

	/**
	 * To set add Module Button Visibility.
	 * 
	 * @param visibility boolean
	 */
	public void setAddModuleButtonVisibility(boolean visibility) {
	}

	/**
	 * To perform operations on add Cmis Button Clicked.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("addCmisButton")
	public void onAddCmisButtonClicked(ClickEvent clickEvent) {
		presenter.onAddCmisButtonClicked();
	}

	/**
	 * To perform operations on edit Cmis Button Clicked.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("editCmisButton")
	public void onEditCmisButtonClicked(ClickEvent clickEvent) {
		cmisListPresenter.onEditButtonClicked();
	}

	/**
	 * To perform operations on delete Cmis Button Clicked.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("deleteCmisButton")
	public void onDeleteCmisButtonClicked(ClickEvent clickEvent) {
		final String rowIndex = cmisListView.listView.getSelectedRowIndex();
		int rowCount = cmisListView.listView.getTableRecordCount();
		if (rowIndex == null || rowIndex.isEmpty()) {
			if (rowCount == 0) {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NO_RECORD_TO_DELETE), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.DELETE_EMAIL_CONFIGURATION_TITLE), Boolean.TRUE);
			} else {
				ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
						BatchClassManagementMessages.NONE_SELECTED_WARNING), LocaleDictionary.get().getConstantValue(
						BatchClassManagementConstants.DELETE_EMAIL_CONFIGURATION_TITLE), Boolean.TRUE);
			}
			return;
		}
		final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(LocaleDictionary.get()
				.getMessageValue(BatchClassManagementMessages.DELETE_CMIS_CONFIGURATION_CONFORMATION), LocaleDictionary.get()
				.getConstantValue(BatchClassManagementConstants.DELETE_CMIS_CONFIGURATION_TITLE), Boolean.FALSE);

		confirmationDialog.addDialogListener(new DialogListener() {

			@Override
			public void onOkClick() {
				confirmationDialog.hide();
				presenter.onDeleteCmisButtonClicked(rowIndex);
			}

			@Override
			public void onCancelClick() {
				confirmationDialog.hide();
			}
		});

	}
}
