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

package com.ephesoft.dcma.gwt.admin.bm.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ephesoft.dcma.gwt.admin.bm.client.presenter.BatchClassManagementPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.BatchClassManagementView;
import com.ephesoft.dcma.gwt.core.client.AbstractController;
import com.ephesoft.dcma.gwt.core.client.event.BindEvent;
import com.ephesoft.dcma.gwt.core.shared.BatchClassDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassDynamicPluginConfigDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassFieldDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassModuleDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginConfigDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginDTO;
import com.ephesoft.dcma.gwt.core.shared.CmisConfigurationDTO;
import com.ephesoft.dcma.gwt.core.shared.DataChecker;
import com.ephesoft.dcma.gwt.core.shared.DocumentTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.EmailConfigurationDTO;
import com.ephesoft.dcma.gwt.core.shared.FieldTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.FunctionKeyDTO;
import com.ephesoft.dcma.gwt.core.shared.KVExtractionDTO;
import com.ephesoft.dcma.gwt.core.shared.KVPageProcessDTO;
import com.ephesoft.dcma.gwt.core.shared.ModuleDTO;
import com.ephesoft.dcma.gwt.core.shared.PluginDetailsDTO;
import com.ephesoft.dcma.gwt.core.shared.RegexDTO;
import com.ephesoft.dcma.gwt.core.shared.RoleDTO;
import com.ephesoft.dcma.gwt.core.shared.TableColumnInfoDTO;
import com.ephesoft.dcma.gwt.core.shared.TableInfoDTO;
import com.ephesoft.dcma.gwt.core.shared.WebScannerConfigurationDTO;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Composite;

/**
 * This class provides functionality to control batch class view, save, update etc.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.AbstractController
 */
public class BatchClassManagementController extends AbstractController {

	/**
	 * batchClass {@link BatchClassDTO}.
	 */
	private BatchClassDTO batchClass;

	/**
	 * superAdmin {@link Boolean}.
	 */
	private Boolean superAdmin;

	/**
	 * batchClassList List<BatchClassDTO>.
	 */
	private List<BatchClassDTO> batchClassList;

	/**
	 * selectedModule {@link BatchClassModuleDTO}.
	 */
	private BatchClassModuleDTO selectedModule;

	/**
	 * selectedPlugin {@link BatchClassPluginDTO}.
	 */
	private BatchClassPluginDTO selectedPlugin;

	/**
	 * selectedDocument {@link DocumentTypeDTO}.
	 */
	private DocumentTypeDTO selectedDocument;

	/**
	 * tables Map<String, List<String>>.
	 */
	private Map<String, List<String>> tables;

	/**
	 * connectionDetails Map<String, String>.
	 */
	private Map<String, String> connectionDetails;

	/**
	 * selectedTable String.
	 */
	private String selectedTable;

	/**
	 * columnsVsDataTypeMap Map<String, String>.
	 */
	private Map<String, String> columnsVsDataTypeMap;

	/**
	 * docFieldsVsDataTypeMap Map<String, String>.
	 */
	private Map<String, String> docFieldsVsDataTypeMap;

	/**
	 * pluginConfigDTO {@link BatchClassPluginConfigDTO}.
	 */
	private BatchClassPluginConfigDTO pluginConfigDTO;

	/**
	 * dataCheckers List<DataChecker>.
	 */
	private final List<DataChecker> dataCheckers;

	/**
	 * selectedFunctionKeyDTO {@link FunctionKeyDTO}.
	 */
	private FunctionKeyDTO selectedFunctionKeyDTO;

	/**
	 * selectedField {@link FieldTypeDTO}.
	 */
	private FieldTypeDTO selectedField;

	/**
	 * kvExtractionDTO {@link KVExtractionDTO}.
	 */
	private KVExtractionDTO kvExtractionDTO;

	/**
	 * tableInfoSelectedField {@link TableInfoDTO}.
	 */
	private TableInfoDTO tableInfoSelectedField;

	/**
	 * selectedTableColumnInfoField {@link TableColumnInfoDTO}.
	 */
	private TableColumnInfoDTO selectedTableColumnInfoField;

	/**
	 * regexDTO {@link RegexDTO}.
	 */
	private RegexDTO regexDTO;

	/**
	 * add {@link Boolean}.
	 */
	private boolean add;

	/**
	 * selectedEmailConfiguration {@link EmailConfigurationDTO}.
	 */
	private EmailConfigurationDTO selectedEmailConfiguration;

	/**
	 * selectedCmisConfiguration {@link CmisConfigurationDTO}.
	 */
	private CmisConfigurationDTO selectedCmisConfiguration;

	/**
	 * batchClassDynamicPluginConfigDTO {@link BatchClassDynamicPluginConfigDTO}.
	 */
	private BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO;

	/**
	 * selectedTableType {@link String}.
	 */
	private String selectedTableType;

	/**
	 * selectedBatchClassField {@link BatchClassFieldDTO}.
	 */
	private BatchClassFieldDTO selectedBatchClassField;

	/**
	 * batchClassManagementPresenter {@link BatchClassManagementPresenter}.
	 */
	private BatchClassManagementPresenter batchClassManagementPresenter;

	/**
	 * view {@link BatchClassManagementView}.
	 */
	private BatchClassManagementView view;

	/**
	 * kvPageProcessDTO {@link KVPageProcessDTO}.
	 */
	private KVPageProcessDTO kvPageProcessDTO;

	/**
	 * roleDTOs List<RoleDTO>.
	 */
	private List<RoleDTO> roleDTOs;

	/**
	 * userRoles Set<String>.
	 */
	private Set<String> userRoles;

	/**
	 * modulesList List<String>.
	 */
	private List<String> modulesList;

	/**
	 * allModules List<String>.
	 */
	private List<String> allModules;

	/**
	 * allPluginDetailsDTOs List<PluginDetailsDTO>.
	 */
	private List<PluginDetailsDTO> allPluginDetailsDTOs;

	/**
	 * pluginIdentifierToNameMap Map<String, String>.
	 */
	private Map<String, String> pluginIdentifierToNameMap;

	/**
	 * selectedWebScannerConfiguration {@link WebScannerConfigurationDTO}.
	 */
	private WebScannerConfigurationDTO selectedWebScannerConfiguration;

	/**
	 * moduleNameToDtoMap Map<String, ModuleDTO>.
	 */
	private Map<String, ModuleDTO> moduleNameToDtoMap;

	/**
	 * allPluginNames List<String>.
	 */
	private List<String> allPluginNames;

	/**
	 * Constructor.
	 * 
	 * @param eventBus {@link HandlerManager}.
	 * @param rpcService {@link BatchClassManagementServiceAsync}
	 */
	public BatchClassManagementController(HandlerManager eventBus, BatchClassManagementServiceAsync rpcService) {
		super(eventBus, rpcService);
		this.dataCheckers = new ArrayList<DataChecker>();
	}

	/**
	 * To craete view.
	 * 
	 * @return Composite
	 */
	@Override
	public Composite createView() {
		this.view = new BatchClassManagementView(this.eventBus);
		this.batchClassManagementPresenter = new BatchClassManagementPresenter(this, view);
		return this.view;
	}

	/**
	 * To handle events.
	 * 
	 * @param eventBus HandlerManager
	 */
	@Override
	public void injectEvents(HandlerManager eventBus) {
		// To be used to handle events
	}

	/**
	 * To get Rpc Service.
	 * 
	 * @return {@link BatchClassManagementServiceAsync}
	 */
	@Override
	public BatchClassManagementServiceAsync getRpcService() {
		return (BatchClassManagementServiceAsync) super.getRpcService();
	}

	/**
	 * To get Batch Class.
	 * 
	 * @return BatchClassDTO
	 */
	public BatchClassDTO getBatchClass() {
		return batchClass;
	}

	/**
	 * To get Batch Class List.
	 * 
	 * @return List<BatchClassDTO>
	 */
	public List<BatchClassDTO> getBatchClassList() {
		return batchClassList;
	}

	/**
	 * To set Batch Class List.
	 * 
	 * @param batchClassList List<BatchClassDTO>
	 */
	public void setBatchClassList(List<BatchClassDTO> batchClassList) {
		this.batchClassList = batchClassList;
	}

	/**
	 * To set Batch Class.
	 * 
	 * @param batchClass BatchClassDTO
	 */
	public void setBatchClass(BatchClassDTO batchClass) {
		this.batchClass = batchClass;
	}

	/**
	 * To get Main Presenter.
	 * 
	 * @return BatchClassManagementPresenter
	 */
	public BatchClassManagementPresenter getMainPresenter() {
		return batchClassManagementPresenter;
	}

	/**
	 * To get Selected Module.
	 * 
	 * @return BatchClassModuleDTO
	 */
	public BatchClassModuleDTO getSelectedModule() {
		return selectedModule;
	}

	/**
	 * To set Selected Module.
	 * 
	 * @param selectedModule BatchClassModuleDTO
	 */
	public void setSelectedModule(BatchClassModuleDTO selectedModule) {
		this.selectedModule = selectedModule;
	}

	/**
	 * To get Selected Plugin.
	 * 
	 * @return BatchClassPluginDTO
	 */
	public BatchClassPluginDTO getSelectedPlugin() {
		return selectedPlugin;
	}

	/**
	 * To set Selected Plugin.
	 * 
	 * @param selectedPlugin BatchClassPluginDTO
	 */
	public void setSelectedPlugin(BatchClassPluginDTO selectedPlugin) {
		this.selectedPlugin = selectedPlugin;
	}

	/**
	 * To get Selected Document.
	 * 
	 * @return DocumentTypeDTO
	 */
	public DocumentTypeDTO getSelectedDocument() {
		return selectedDocument;
	}

	/**
	 * To set Selected Document.
	 * 
	 * @param selectedDocument DocumentTypeDTO
	 */
	public void setSelectedDocument(DocumentTypeDTO selectedDocument) {
		this.selectedDocument = selectedDocument;
	}

	/**
	 * To get Selected Web Scanner Configuration.
	 * 
	 * @return WebScannerConfigurationDTO
	 */
	public WebScannerConfigurationDTO getSelectedWebScannerConfiguration() {
		return selectedWebScannerConfiguration;
	}

	/**
	 * To set Selected Web Scanner Configuration.
	 * 
	 * @param selectedWebScannerConfigurationDTO WebScannerConfigurationDTO
	 */
	public void setSelectedWebScannerConfiguration(WebScannerConfigurationDTO selectedWebScannerConfigurationDTO) {
		this.selectedWebScannerConfiguration = selectedWebScannerConfigurationDTO;
	}

	/**
	 * To get Selected Email Configuration.
	 * 
	 * @return {@link EmailConfigurationDTO}
	 */
	public EmailConfigurationDTO getSelectedEmailConfiguration() {
		return selectedEmailConfiguration;
	}

	/**
	 * To set Selected Email Configuration.
	 * 
	 * @param selectedEmailConfiguration EmailConfigurationDTO
	 */
	public void setSelectedEmailConfiguration(EmailConfigurationDTO selectedEmailConfiguration) {
		this.selectedEmailConfiguration = selectedEmailConfiguration;
	}

	/**
	 * To get Tables.
	 * 
	 * @return Map<String, List<String>>
	 */
	public Map<String, List<String>> getTables() {
		return tables;
	}

	/**
	 * To get Selected Regex.
	 * 
	 * @return RegexDTO
	 */
	public RegexDTO getSelectedRegex() {
		return regexDTO;
	}

	/**
	 * To set Selected Regex.
	 * 
	 * @param regexDTO RegexDTO
	 */
	public void setSelectedRegex(RegexDTO regexDTO) {
		this.regexDTO = regexDTO;
	}

	/**
	 * To set tables.
	 * 
	 * @param tables Map<String, List<String>>
	 */
	public void setTables(Map<String, List<String>> tables) {
		this.tables = tables;
	}

	/**
	 * To get Connection Details.
	 * 
	 * @return Map<String, String>
	 */
	public Map<String, String> getConnectionDetails() {
		return connectionDetails;
	}

	/**
	 * To set Connection Details.
	 * 
	 * @param connectionDetails Map<String, String>
	 */
	public void setConnectionDetails(Map<String, String> connectionDetails) {
		this.connectionDetails = connectionDetails;
	}

	/**
	 * To get Selected Table.
	 * 
	 * @return String
	 */
	public String getSelectedTable() {
		return selectedTable;
	}

	/**
	 * To set Selected Table.
	 * 
	 * @param selectedTable String
	 */
	public void setSelectedTable(String selectedTable) {
		this.selectedTable = selectedTable;
	}

	/**
	 * To get Columns Map.
	 * 
	 * @return Map<String, String>
	 */
	public Map<String, String> getColumnsMap() {
		return columnsVsDataTypeMap;
	}

	/**
	 * To set Columns Map.
	 * 
	 * @param columns Map<String, String>
	 */
	public void setColumnsMap(Map<String, String> columns) {
		this.columnsVsDataTypeMap = columns;
	}

	/**
	 * To get Plugin Config DTO.
	 * 
	 * @return BatchClassPluginConfigDTO
	 */
	public BatchClassPluginConfigDTO getPluginConfigDTO() {
		return pluginConfigDTO;
	}

	/**
	 * To set Plugin Config DTO.
	 * 
	 * @param pluginConfigDTO BatchClassPluginConfigDTO
	 */
	public void setPluginConfigDTO(BatchClassPluginConfigDTO pluginConfigDTO) {
		this.pluginConfigDTO = pluginConfigDTO;
	}

	/**
	 * To get Doc Fields Vs Data TypeMap.
	 * 
	 * @return Map<String, String>
	 */
	public Map<String, String> getDocFieldsVsDataTypeMap() {
		return docFieldsVsDataTypeMap;
	}

	/**
	 * To set Doc Fields Vs Data Type Map.
	 * 
	 * @param docFieldsVsDataTypeMap Map<String, String>
	 */
	public void setDocFieldsVsDataTypeMap(Map<String, String> docFieldsVsDataTypeMap) {
		this.docFieldsVsDataTypeMap = docFieldsVsDataTypeMap;
	}

	/**
	 * To get Data Checkers.
	 * 
	 * @return List<DataChecker>
	 */
	public List<DataChecker> getDataCheckers() {
		return dataCheckers;
	}

	/**
	 * To get Selected Document Level Field.
	 * 
	 * @return FieldTypeDTO
	 */
	public FieldTypeDTO getSelectedDocumentLevelField() {
		return selectedField;
	}

	/**
	 * To set Selected Document Level Field.
	 * 
	 * @param selectedField FieldTypeDTO
	 */
	public void setSelectedDocumentLevelField(FieldTypeDTO selectedField) {
		this.selectedField = selectedField;
	}

	/**
	 * To set Selected KV Extraction.
	 * 
	 * @param kvExtractionDTO KVExtractionDTO
	 */
	public void setSelectedKVExtraction(KVExtractionDTO kvExtractionDTO) {
		this.kvExtractionDTO = kvExtractionDTO;
	}

	/**
	 * To get Selected Table Info Field.
	 * 
	 * @return TableInfoDTO
	 */
	public TableInfoDTO getSelectedTableInfoField() {
		return tableInfoSelectedField;
	}

	/**
	 * To set Table Info Selected Field.
	 * 
	 * @param tableInfoSelectedField TableInfoDTO
	 */
	public void setTableInfoSelectedField(TableInfoDTO tableInfoSelectedField) {
		this.tableInfoSelectedField = tableInfoSelectedField;
	}

	/**
	 * To get Selected KV Extraction.
	 * 
	 * @return KVExtractionDTO
	 */
	public KVExtractionDTO getSelectedKVExtraction() {
		return this.kvExtractionDTO;
	}

	/**
	 * To get Selected Table Column Info Field.
	 * 
	 * @return TableColumnInfoDTO
	 */
	public TableColumnInfoDTO getSelectedTableColumnInfoField() {
		return selectedTableColumnInfoField;
	}

	/**
	 * To set Selected Table Column Info Field.
	 * 
	 * @param selectedTableColumnInfoField TableColumnInfoDTO
	 */
	public void setSelectedTableColumnInfoField(TableColumnInfoDTO selectedTableColumnInfoField) {
		this.selectedTableColumnInfoField = selectedTableColumnInfoField;
	}

	/**
	 * T return add.
	 * 
	 * @return boolean
	 */
	public boolean isAdd() {
		return add;
	}

	/**
	 * To set Add.
	 * 
	 * @param add boolean
	 */
	public void setAdd(boolean add) {
		this.add = add;
	}

	/**
	 * To get view.
	 * 
	 * @return BatchClassManagementView
	 */
	public BatchClassManagementView getView() {
		return view;
	}

	/**
	 * To get Selected Kv Page Process DTO.
	 * 
	 * @return KVPageProcessDTO
	 */
	public KVPageProcessDTO getSelectedKvPageProcessDTO() {
		return kvPageProcessDTO;
	}

	/**
	 * To get Batch Class By Identifier.
	 * 
	 * @param identifier String
	 * @return BatchClassDTO
	 */
	public BatchClassDTO getBatchClassByIdentifier(String identifier) {
		BatchClassDTO dto = null;
		for (BatchClassDTO batchClassDTO : batchClassList) {
			if (batchClassDTO.getIdentifier().equals(identifier)) {
				dto = batchClassDTO;
				break;
			}
		}
		return dto;
	}

	/**
	 * To get All Plugin Details DTOs.
	 * 
	 * @return the allPluginDetailsDTOs
	 */
	public List<PluginDetailsDTO> getAllPluginDetailsDTOs() {
		return allPluginDetailsDTOs;
	}

	/**
	 * To set All Plugin Details DTOs.
	 * 
	 * @param allPluginDetailsDTOs List<PluginDetailsDTO>
	 */
	public void setAllPluginDetailsDTOs(List<PluginDetailsDTO> allPluginDetailsDTOs) {
		this.allPluginDetailsDTOs = allPluginDetailsDTOs;
	}

	/**
	 * To refresh all the modules.
	 */
	@Override
	public void refresh() {
		if (selectedModule != null) {
			selectedModule = batchClass.getModuleByWorkflowName(selectedModule.getWorkflowName());
		}
		if (selectedPlugin != null) {
			selectedPlugin = selectedModule.getPluginByName(selectedPlugin.getPlugin().getPluginName());
		}

		if (pluginConfigDTO != null) {
			if (pluginConfigDTO.getQualifier() != null) {
				pluginConfigDTO = selectedPlugin.getBatchClassPluginConfigDTOByQualifier(pluginConfigDTO.getQualifier());
			} else {
				pluginConfigDTO = selectedPlugin.getBatchClassPluginConfigDTOByName(pluginConfigDTO.getName());
			}
		}

		if (selectedDocument != null) {
			selectedDocument = batchClass.getDocTypeByName(selectedDocument.getName());
		}

		if (selectedWebScannerConfiguration != null) {
			selectedWebScannerConfiguration = batchClass.getScannerConfigurationByProfileName(selectedWebScannerConfiguration
					.getValue());
		}

		if (selectedEmailConfiguration != null) {
			selectedEmailConfiguration = batchClass.getEmailConfigurationByFields(selectedEmailConfiguration.getUserName(),
					selectedEmailConfiguration.getPassword(), selectedEmailConfiguration.getServerName(), selectedEmailConfiguration
							.getServerType(), selectedEmailConfiguration.getFolderName());
		}

		if (selectedField != null && selectedDocument != null) {
			selectedField = selectedDocument.getFieldTypeByName(selectedField.getName());
		}

		if (tableInfoSelectedField != null && selectedDocument != null) {
			tableInfoSelectedField = selectedDocument.getTableInfoByName(tableInfoSelectedField.getName());
		}

		if (selectedFunctionKeyDTO != null && selectedDocument != null) {
			selectedFunctionKeyDTO = selectedDocument.getFunctionKeyDTOByShorcutKeyName(selectedFunctionKeyDTO.getShortcutKeyName());
		}

		if (selectedTableColumnInfoField != null && tableInfoSelectedField != null) {
			selectedTableColumnInfoField = tableInfoSelectedField.getTCInfoDTOByNameAndPattern(selectedTableColumnInfoField
					.getColumnName(), selectedTableColumnInfoField.getColumnPattern());
		}

		if (kvExtractionDTO != null && kvExtractionDTO.isSimpleKVExtraction() && selectedField != null) {
			kvExtractionDTO = selectedField.getKVExtractionByKeyAndDataTypeAndLocation(kvExtractionDTO.getKeyPattern(),
					kvExtractionDTO.getValuePattern(), kvExtractionDTO.getLocationType());
		}

		if (regexDTO != null && selectedField != null) {
			regexDTO = selectedField.getRegexDTOByPattern(regexDTO.getPattern());
		}

		if (kvPageProcessDTO != null && pluginConfigDTO != null) {
			kvPageProcessDTO = pluginConfigDTO.getKVPageProcessByKeyAndDataTypeAndLocation(kvPageProcessDTO.getKeyPattern(),
					kvPageProcessDTO.getValuePattern(), kvPageProcessDTO.getLocationType());
		}

		if (batchClassDynamicPluginConfigDTO != null && selectedPlugin != null) {
			batchClassDynamicPluginConfigDTO = selectedPlugin
					.getBatchClassPluginConfigDTOByDescription(batchClassDynamicPluginConfigDTO.getDescription());
		}
		if (selectedBatchClassField != null) {
			selectedBatchClassField = batchClass.getBatchClassFieldByName(selectedBatchClassField.getName());
		}

		eventBus.fireEvent(new BindEvent());
	}

	/**
	 * To set Kv Page Process DTO.
	 * 
	 * @param kvPageProcessDTO KVPageProcessDTO
	 */
	public void setKvPageProcessDTO(KVPageProcessDTO kvPageProcessDTO) {
		this.kvPageProcessDTO = kvPageProcessDTO;
	}

	/**
	 * To set Role DTOs.
	 * 
	 * @param roleDTOs List<RoleDTO>
	 */
	public void setRoleDTOs(List<RoleDTO> roleDTOs) {
		this.roleDTOs = roleDTOs;
	}

	/**
	 * To get All Roles.
	 * 
	 * @return List<RoleDTO>
	 */
	public List<RoleDTO> getAllRoles() {
		return roleDTOs;
	}

	/**
	 * To get Selected Batch Class Field.
	 * 
	 * @return BatchClassFieldDTO
	 */
	public BatchClassFieldDTO getSelectedBatchClassField() {
		return selectedBatchClassField;
	}

	/**
	 * To set Selected Batch Class Field.
	 * 
	 * @param selectedBatchClassField BatchClassFieldDTO
	 */
	public void setSelectedBatchClassField(BatchClassFieldDTO selectedBatchClassField) {
		this.selectedBatchClassField = selectedBatchClassField;
	}

	/**
	 * To get Document By Identifier.
	 * 
	 * @param identifier String
	 * @return DocumentTypeDTO
	 */
	public DocumentTypeDTO getDocumentByIdentifier(String identifier) {
		DocumentTypeDTO dto = null;
		for (DocumentTypeDTO documentTypeDTO : batchClass.getDocuments()) {
			if (documentTypeDTO.getIdentifier().equals(identifier)) {
				dto = documentTypeDTO;
				break;
			}
		}
		return dto;
	}

	/**
	 * To get Module Name To Dto Map.
	 * 
	 * @return the moduleNameToDtoMap
	 */
	public Map<String, ModuleDTO> getModuleNameToDtoMap() {
		return moduleNameToDtoMap;
	}

	/**
	 * To set Module Name To Dto Map.
	 * 
	 * @param moduleNameToDtoMap Map<String, ModuleDTO>
	 */
	public void setModuleNameToDtoMap(Map<String, ModuleDTO> moduleNameToDtoMap) {
		this.moduleNameToDtoMap = moduleNameToDtoMap;
	}

	/**
	 * To get Batch Class Dynamic Plugin Config DTO.
	 * 
	 * @return BatchClassDynamicPluginConfigDTO
	 */
	public BatchClassDynamicPluginConfigDTO getBatchClassDynamicPluginConfigDTO() {
		return batchClassDynamicPluginConfigDTO;
	}

	/**
	 * To set Batch Class Dynamic Plugin Config DTO.
	 * 
	 * @param batchClassDynamicPluginConfigDTO BatchClassDynamicPluginConfigDTO
	 */
	public void setBatchClassDynamicPluginConfigDTO(BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO) {
		this.batchClassDynamicPluginConfigDTO = batchClassDynamicPluginConfigDTO;
	}

	/**
	 * To set Selected Function Key DTO.
	 * 
	 * @param selectedFunctionKeyDTO FunctionKeyDTO
	 */
	public void setSelectedFunctionKeyDTO(FunctionKeyDTO selectedFunctionKeyDTO) {
		this.selectedFunctionKeyDTO = selectedFunctionKeyDTO;
	}

	/**
	 * To get Selected Function Key DTO.
	 * 
	 * @return the selectedFunctionKeyDTO
	 */
	public FunctionKeyDTO getSelectedFunctionKeyDTO() {
		return selectedFunctionKeyDTO;
	}

	/**
	 * To get Selected Table Type.
	 * 
	 * @return String
	 */
	public String getSelectedTableType() {
		return selectedTableType;
	}

	/**
	 * To set Selected Table Type.
	 * 
	 * @param selectedTableType String
	 */
	public void setSelectedTableType(String selectedTableType) {
		this.selectedTableType = selectedTableType;
	}

	/**
	 * To get Modules List.
	 * 
	 * @return the modulesList
	 */
	public List<String> getModulesList() {
		return modulesList;
	}

	/**
	 * To set Modules List.
	 * 
	 * @param modulesList the modulesList
	 */
	public void setModulesList(List<String> modulesList) {
		this.modulesList = modulesList;
	}

	/**
	 * To get all Modules.
	 * 
	 * @return the allModules
	 */
	public List<String> getAllModules() {
		return allModules;
	}

	/**
	 * To set all Modules.
	 * 
	 * @param allModules List<String>
	 */
	public void setAllModules(List<String> allModules) {
		this.allModules = allModules;
	}

	/**
	 * To get Batch Class Management Presenter.
	 * 
	 * @return the batchClassManagementPresenter
	 */
	public BatchClassManagementPresenter getBatchClassManagementPresenter() {
		return batchClassManagementPresenter;
	}

	/**
	 * To get Plugin Identifier To Name Map.
	 * 
	 * @return the pluginIdentifierToNameMap
	 */
	public Map<String, String> getPluginIdentifierToNameMap() {
		return pluginIdentifierToNameMap;
	}

	/**
	 * To set Plugin Identifier To Name Map.
	 * 
	 * @param pluginIdentifierToNameMap Map<String, String>
	 */
	public void setPluginIdentifierToNameMap(Map<String, String> pluginIdentifierToNameMap) {
		this.pluginIdentifierToNameMap = pluginIdentifierToNameMap;
	}

	/**
	 * To set User Roles.
	 * 
	 * @param userRoles Set<String>
	 */
	public void setUserRoles(Set<String> userRoles) {
		this.userRoles = userRoles;
	}

	/**
	 * To get User Roles.
	 * 
	 * @return the userRoles
	 */
	public Set<String> getUserRoles() {
		return userRoles;
	}

	/**
	 * To set Super Admin.
	 * 
	 * @param isSuperAdmin Boolean
	 */
	public void setSuperAdmin(Boolean isSuperAdmin) {
		this.superAdmin = isSuperAdmin;
	}

	/**
	 * To check whether super admin.
	 * 
	 * @return the isSuperAdmin
	 */
	public Boolean isSuperAdmin() {
		if (superAdmin == null) {
			superAdmin = Boolean.FALSE;
		}
		return superAdmin;
	}

	/**
	 * To get Selected Cmis Configuration.
	 * 
	 * @return CmisConfigurationDTO
	 */
	public CmisConfigurationDTO getSelectedCmisConfiguration() {
		return selectedCmisConfiguration;
	}

	/**
	 * Getter for KVPageProcessDTO {@link KVPageProcessDTO}.
	 * 
	 * @return the kvPageProcessDTO.
	 */
	public KVPageProcessDTO getKvPageProcessDTO() {
		return kvPageProcessDTO;
	}

	/**
	 * To set Selected Cmis Configuration.
	 * 
	 * @param selectedCmisConfiguration CmisConfigurationDTO
	 */
	public void setSelectedCmisConfiguration(CmisConfigurationDTO selectedCmisConfiguration) {
		this.selectedCmisConfiguration = selectedCmisConfiguration;
	}

	/**
	 * To get all Plugin Names.
	 * 
	 * @return the allPluginNames
	 */
	public List<String> getAllPluginNames() {
		return allPluginNames;
	}

	/**
	 * To set all Plugin Names.
	 * 
	 * @param allPluginNames List<String>
	 */
	public void setAllPluginNames(List<String> allPluginNames) {
		this.allPluginNames = allPluginNames;
	}
}
