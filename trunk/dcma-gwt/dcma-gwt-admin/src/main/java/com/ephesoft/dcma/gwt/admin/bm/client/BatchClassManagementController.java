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

package com.ephesoft.dcma.gwt.admin.bm.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ephesoft.dcma.gwt.admin.bm.client.presenter.BatchClassManagementPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.BatchClassManagementView;
import com.ephesoft.dcma.gwt.core.client.Controller;
import com.ephesoft.dcma.gwt.core.client.event.BindEvent;
import com.ephesoft.dcma.gwt.core.shared.BatchClassDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassDynamicPluginConfigDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassFieldDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassModuleDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginConfigDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginDTO;
import com.ephesoft.dcma.gwt.core.shared.DataChecker;
import com.ephesoft.dcma.gwt.core.shared.DocumentTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.EmailConfigurationDTO;
import com.ephesoft.dcma.gwt.core.shared.FieldTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.FunctionKeyDTO;
import com.ephesoft.dcma.gwt.core.shared.KVExtractionDTO;
import com.ephesoft.dcma.gwt.core.shared.KVPageProcessDTO;
import com.ephesoft.dcma.gwt.core.shared.PluginDetailsDTO;
import com.ephesoft.dcma.gwt.core.shared.RegexDTO;
import com.ephesoft.dcma.gwt.core.shared.RoleDTO;
import com.ephesoft.dcma.gwt.core.shared.TableColumnInfoDTO;
import com.ephesoft.dcma.gwt.core.shared.TableInfoDTO;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Composite;

public class BatchClassManagementController extends Controller {

	private BatchClassDTO batchClass;
	private Boolean superAdmin;
	private List<BatchClassDTO> batchClassList;
	private BatchClassModuleDTO selectedModule;
	private BatchClassPluginDTO selectedPlugin;
	private DocumentTypeDTO selectedDocument;
	private Map<String, List<String>> tables;
	private Map<String, String> connectionDetails;
	private String selectedTable;
	private Map<String, String> columnsVsDataTypeMap;
	private Map<String, String> docFieldsVsDataTypeMap;
	private BatchClassPluginConfigDTO pluginConfigDTO;
	private final List<DataChecker> dataCheckers;
	private FunctionKeyDTO selectedFunctionKeyDTO;
	private FieldTypeDTO selectedField;
	private KVExtractionDTO kvExtractionDTO;
	private TableInfoDTO tableInfoSelectedField;
	private TableColumnInfoDTO selectedTableColumnInfoField;
	private RegexDTO regexDTO;
	private boolean add;
	private EmailConfigurationDTO selectedEmailConfiguration;
	private BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO;
	private String selectedTableType;

	private BatchClassFieldDTO selectedBatchClassField;

	private BatchClassManagementPresenter batchClassManagementPresenter;

	private BatchClassManagementView view;

	private KVPageProcessDTO kvPageProcessDTO;

	private List<RoleDTO> roleDTOs;

	private Set<String> userRoles;

	private List<String> modulesList;

	private List<String> allModules;

	private List<PluginDetailsDTO> allPluginDetailsDTOs;

	private Map<String, String> pluginIdentifierToNameMap;

	public BatchClassManagementController(HandlerManager eventBus, BatchClassManagementServiceAsync rpcService) {
		super(eventBus, rpcService);
		this.dataCheckers = new ArrayList<DataChecker>();
	}

	@Override
	public Composite createView() {
		this.view = new BatchClassManagementView(this.eventBus);
		this.batchClassManagementPresenter = new BatchClassManagementPresenter(this, view);
		return this.view;
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		// To be used to handle events
	}

	@Override
	public BatchClassManagementServiceAsync getRpcService() {
		return (BatchClassManagementServiceAsync) super.getRpcService();
	}

	public BatchClassDTO getBatchClass() {
		return batchClass;
	}

	public List<BatchClassDTO> getBatchClassList() {
		return batchClassList;
	}

	public void setBatchClassList(List<BatchClassDTO> batchClassList) {
		this.batchClassList = batchClassList;
	}

	public void setBatchClass(BatchClassDTO batchClass) {
		this.batchClass = batchClass;
	}

	public BatchClassManagementPresenter getMainPresenter() {
		return batchClassManagementPresenter;
	}

	public BatchClassModuleDTO getSelectedModule() {
		return selectedModule;
	}

	public void setSelectedModule(BatchClassModuleDTO selectedModule) {
		this.selectedModule = selectedModule;
	}

	public BatchClassPluginDTO getSelectedPlugin() {
		return selectedPlugin;
	}

	public void setSelectedPlugin(BatchClassPluginDTO selectedPlugin) {
		this.selectedPlugin = selectedPlugin;
	}

	public DocumentTypeDTO getSelectedDocument() {
		return selectedDocument;
	}

	public void setSelectedDocument(DocumentTypeDTO selectedDocument) {
		this.selectedDocument = selectedDocument;
	}

	public EmailConfigurationDTO getSelectedEmailConfiguration() {
		return selectedEmailConfiguration;
	}

	public void setSelectedEmailConfiguration(EmailConfigurationDTO selectedEmailConfiguration) {
		this.selectedEmailConfiguration = selectedEmailConfiguration;
	}

	public Map<String, List<String>> getTables() {
		return tables;
	}

	public RegexDTO getSelectedRegex() {
		return regexDTO;
	}

	public void setSelectedRegex(RegexDTO regexDTO) {
		this.regexDTO = regexDTO;
	}

	public void setTables(Map<String, List<String>> tables) {
		this.tables = tables;
	}

	public Map<String, String> getConnectionDetails() {
		return connectionDetails;
	}

	public void setConnectionDetails(Map<String, String> connectionDetails) {
		this.connectionDetails = connectionDetails;
	}

	public String getSelectedTable() {
		return selectedTable;
	}

	public void setSelectedTable(String selectedTable) {
		this.selectedTable = selectedTable;
	}

	public Map<String, String> getColumnsMap() {
		return columnsVsDataTypeMap;
	}

	public void setColumnsMap(Map<String, String> columns) {
		this.columnsVsDataTypeMap = columns;
	}

	public BatchClassPluginConfigDTO getPluginConfigDTO() {
		return pluginConfigDTO;
	}

	public void setPluginConfigDTO(BatchClassPluginConfigDTO pluginConfigDTO) {
		this.pluginConfigDTO = pluginConfigDTO;
	}

	public Map<String, String> getDocFieldsVsDataTypeMap() {
		return docFieldsVsDataTypeMap;
	}

	public void setDocFieldsVsDataTypeMap(Map<String, String> docFieldsVsDataTypeMap) {
		this.docFieldsVsDataTypeMap = docFieldsVsDataTypeMap;
	}

	public List<DataChecker> getDataCheckers() {
		return dataCheckers;
	}

	public FieldTypeDTO getSelectedDocumentLevelField() {
		return selectedField;
	}

	public void setSelectedDocumentLevelField(FieldTypeDTO selectedField) {
		this.selectedField = selectedField;
	}

	public void setSelectedKVExtraction(KVExtractionDTO kvExtractionDTO) {
		this.kvExtractionDTO = kvExtractionDTO;
	}

	public TableInfoDTO getSelectedTableInfoField() {
		return tableInfoSelectedField;
	}

	public void setTableInfoSelectedField(TableInfoDTO tableInfoSelectedField) {
		this.tableInfoSelectedField = tableInfoSelectedField;
	}

	public KVExtractionDTO getSelectedKVExtraction() {
		return this.kvExtractionDTO;
	}

	public TableColumnInfoDTO getSelectedTableColumnInfoField() {
		return selectedTableColumnInfoField;
	}

	public void setSelectedTableColumnInfoField(TableColumnInfoDTO selectedTableColumnInfoField) {
		this.selectedTableColumnInfoField = selectedTableColumnInfoField;
	}

	public boolean isAdd() {
		return add;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	public BatchClassManagementView getView() {
		return view;
	}

	public KVPageProcessDTO getSelectedKvPageProcessDTO() {
		return kvPageProcessDTO;
	}

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
	 * @return the allPluginDetailsDTOs
	 */
	public List<PluginDetailsDTO> getAllPluginDetailsDTOs() {
		return allPluginDetailsDTOs;
	}

	/**
	 * @param allPluginDetailsDTOs the allPluginDetailsDTOs to set
	 */
	public void setAllPluginDetailsDTOs(List<PluginDetailsDTO> allPluginDetailsDTOs) {
		this.allPluginDetailsDTOs = allPluginDetailsDTOs;
	}

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

		eventBus.fireEvent(new BindEvent());
	}

	public void setKvPageProcessDTO(KVPageProcessDTO kvPageProcessDTO) {
		this.kvPageProcessDTO = kvPageProcessDTO;
	}

	public void setRoleDTOs(List<RoleDTO> roleDTOs) {
		this.roleDTOs = roleDTOs;
	}

	public List<RoleDTO> getAllRoles() {
		return roleDTOs;
	}

	public BatchClassFieldDTO getSelectedBatchClassField() {
		return selectedBatchClassField;
	}

	public void setSelectedBatchClassField(BatchClassFieldDTO selectedBatchClassField) {
		this.selectedBatchClassField = selectedBatchClassField;
	}

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

	public BatchClassDynamicPluginConfigDTO getBatchClassDynamicPluginConfigDTO() {
		return batchClassDynamicPluginConfigDTO;
	}

	public void setBatchClassDynamicPluginConfigDTO(BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO) {
		this.batchClassDynamicPluginConfigDTO = batchClassDynamicPluginConfigDTO;
	}

	/**
	 * @param selectedFunctionKeyDTO the selectedFunctionKeyDTO to set
	 */
	public void setSelectedFunctionKeyDTO(FunctionKeyDTO selectedFunctionKeyDTO) {
		this.selectedFunctionKeyDTO = selectedFunctionKeyDTO;
	}

	/**
	 * @return the selectedFunctionKeyDTO
	 */
	public FunctionKeyDTO getSelectedFunctionKeyDTO() {
		return selectedFunctionKeyDTO;
	}

	public String getSelectedTableType() {
		return selectedTableType;
	}

	public void setSelectedTableType(String selectedTableType) {
		this.selectedTableType = selectedTableType;
	}

	/**
	 * @return the modulesList
	 */
	public List<String> getModulesList() {
		return modulesList;
	}

	/**
	 * @param modulesList the modulesList to set
	 */
	public void setModulesList(List<String> modulesList) {
		this.modulesList = modulesList;
	}

	/**
	 * @return the allModules
	 */
	public List<String> getAllModules() {
		return allModules;
	}

	/**
	 * @param allModules the allModules to set
	 */
	public void setAllModules(List<String> allModules) {
		this.allModules = allModules;
	}

	/**
	 * @return the batchClassManagementPresenter
	 */
	public BatchClassManagementPresenter getBatchClassManagementPresenter() {
		return batchClassManagementPresenter;
	}

	/**
	 * @return the pluginIdentifierToNameMap
	 */
	public Map<String, String> getPluginIdentifierToNameMap() {
		return pluginIdentifierToNameMap;
	}

	/**
	 * @param pluginIdentifierToNameMap the pluginIdentifierToNameMap to set
	 */
	public void setPluginIdentifierToNameMap(Map<String, String> pluginIdentifierToNameMap) {
		this.pluginIdentifierToNameMap = pluginIdentifierToNameMap;
	}

	/**
	 * @param userRoles the userRoles to set
	 */
	public void setUserRoles(Set<String> userRoles) {
		this.userRoles = userRoles;
	}

	/**
	 * @return the userRoles
	 */
	public Set<String> getUserRoles() {
		return userRoles;
	}

	/**
	 * @param isSuperAdmin the isSuperAdmin to set
	 */
	public void setSuperAdmin(Boolean isSuperAdmin) {
		this.superAdmin = isSuperAdmin;
	}

	/**
	 * @return the isSuperAdmin
	 */
	public Boolean IsSuperAdmin() {
		if (superAdmin == null) {
			superAdmin = Boolean.FALSE;
		}
		return superAdmin;
	}
}
