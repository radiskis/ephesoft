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

package com.ephesoft.dcma.gwt.admin.bm.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.core.model.common.DomainEntity.EntityState;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassDynamicPluginConfig;
import com.ephesoft.dcma.da.domain.BatchClassEmailConfiguration;
import com.ephesoft.dcma.da.domain.BatchClassField;
import com.ephesoft.dcma.da.domain.BatchClassGroups;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchClassModuleConfig;
import com.ephesoft.dcma.da.domain.BatchClassPlugin;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.FieldType;
import com.ephesoft.dcma.da.domain.FunctionKey;
import com.ephesoft.dcma.da.domain.KVExtraction;
import com.ephesoft.dcma.da.domain.KVPageProcess;
import com.ephesoft.dcma.da.domain.Module;
import com.ephesoft.dcma.da.domain.ModuleConfig;
import com.ephesoft.dcma.da.domain.PageType;
import com.ephesoft.dcma.da.domain.Plugin;
import com.ephesoft.dcma.da.domain.PluginConfig;
import com.ephesoft.dcma.da.domain.RegexValidation;
import com.ephesoft.dcma.da.domain.TableColumnsInfo;
import com.ephesoft.dcma.da.domain.TableInfo;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.ModuleConfigService;
import com.ephesoft.dcma.da.service.ModuleService;
import com.ephesoft.dcma.da.service.PluginConfigService;
import com.ephesoft.dcma.da.service.PluginService;
import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.core.shared.BatchClassDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassDynamicPluginConfigDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassFieldDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassModuleDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginConfigDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginDTO;
import com.ephesoft.dcma.gwt.core.shared.CoordinatesDTO;
import com.ephesoft.dcma.gwt.core.shared.DocumentTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.EmailConfigurationDTO;
import com.ephesoft.dcma.gwt.core.shared.FieldTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.FunctionKeyDTO;
import com.ephesoft.dcma.gwt.core.shared.KVExtractionDTO;
import com.ephesoft.dcma.gwt.core.shared.KVPageProcessDTO;
import com.ephesoft.dcma.gwt.core.shared.ModuleDTO;
import com.ephesoft.dcma.gwt.core.shared.OutputDataCarrierDTO;
import com.ephesoft.dcma.gwt.core.shared.PageTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.PluginConfigurationDTO;
import com.ephesoft.dcma.gwt.core.shared.PluginDetailsDTO;
import com.ephesoft.dcma.gwt.core.shared.RegexDTO;
import com.ephesoft.dcma.gwt.core.shared.RoleDTO;
import com.ephesoft.dcma.gwt.core.shared.TableColumnInfoDTO;
import com.ephesoft.dcma.gwt.core.shared.TableInfoDTO;
import com.ephesoft.dcma.gwt.core.shared.TestTableResultDTO;
import com.ephesoft.dcma.gwt.core.shared.importTree.Label;
import com.ephesoft.dcma.gwt.core.shared.importTree.Node;
import com.ephesoft.dcma.kvfinder.data.InputDataCarrier;
import com.ephesoft.dcma.kvfinder.data.OutputDataCarrier;
import com.ephesoft.dcma.util.FileUtils;

public class BatchClassUtil {

	private static final String SEMI_COLON = ";";
	private static final String ROLES = "Roles";
	private static final String EMPTY_STRING = "";
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchClassUtil.class);
	private static final String FIRST_PAGE = "_First_Page";
	private static final String MIDDLE_PAGE = "_Middle_Page";
	private static final String LAST_PAGE = "_Last_Page";

	/**
	 * 
	 * @param batchClass the batch class that is to be merged
	 * @param batchClassDTO the batchClassDTO which has to be replicated on the batch class
	 * @return a list of document type names that have been deleted.
	 */
	public static List<String> mergeBatchClassFromDTO(BatchClass batchClass, BatchClassDTO batchClassDTO, Set<String> groupNameSet) {

		List<String> docTypeNameList = null;
		int priority = Integer.parseInt(batchClassDTO.getPriority());
		batchClass.setPriority(priority);
		batchClass.setDescription(batchClassDTO.getDescription());
		batchClass.setLastModifiedBy(batchClass.getCurrentUser());
		updateRevisionNumber(batchClass);
		batchClassDTO.setVersion(batchClass.getVersion());
		batchClass.setDeleted(batchClassDTO.isDeleted());

		Collection<BatchClassModuleDTO> moduleDTOs = batchClassDTO.getModules();
		for (BatchClassModuleDTO moduleDTO : moduleDTOs) {
			BatchClassModule module = batchClass.getBatchClassModuleById(Long.valueOf(moduleDTO.getIdentifier()));
			mergeBatchClassModuleFromDTO(module, moduleDTO);
		}
		for (DocumentTypeDTO documentTypeDTO : batchClassDTO.getDocuments(true)) {
			if (documentTypeDTO.isDeleted()) {
				batchClass.removeDocumentTypeByIdentifier(documentTypeDTO.getIdentifier());
				if (null == docTypeNameList) {
					docTypeNameList = new ArrayList<String>();
					docTypeNameList.add(documentTypeDTO.getName());
				} else {
					docTypeNameList.add(documentTypeDTO.getName());
				}
			} else if (documentTypeDTO.isNew()) {
				DocumentType documentType = new DocumentType();
				documentTypeDTO.setNew(false);
				mergeBatchClassDocumentFromDTO(documentType, documentTypeDTO);
				addAutoGeneratedPageType(documentType);
				batchClass.addDocumentType(documentType);
			} else {
				DocumentType documentType = batchClass.getDocumentTypeByIdentifier(documentTypeDTO.getIdentifier());
				mergeBatchClassDocumentFromDTO(documentType, documentTypeDTO);
			}
		}
		for (EmailConfigurationDTO emailConfigurationDTO : batchClassDTO.getEmailConfiguration(true)) {
			if (emailConfigurationDTO.isDeleted()) {
				batchClass.removeEmailConfigurationByIdentifier(emailConfigurationDTO.getIdentifier());
			} else if (emailConfigurationDTO.isNew()) {
				BatchClassEmailConfiguration emailConfiguration = new BatchClassEmailConfiguration();
				emailConfigurationDTO.setNew(false);
				mergeBatchClassEmailFromDTO(emailConfiguration, emailConfigurationDTO);
				batchClass.addEmailConfiguration(emailConfiguration);
			} else {
				BatchClassEmailConfiguration batchClassEmailConfiguration = batchClass
						.getEmailConfigurationByIdentifier(emailConfigurationDTO.getIdentifier());
				mergeBatchClassEmailFromDTO(batchClassEmailConfiguration, emailConfigurationDTO);
			}
		}

		for (BatchClassFieldDTO batchClassFieldDTO : batchClassDTO.getBatchClassField(true)) {
			if (batchClassFieldDTO.isDeleted()) {
				batchClass.removeBatchClassFieldByIdentifier(batchClassFieldDTO.getIdentifier());
			} else if (batchClassFieldDTO.isNew()) {
				BatchClassField batchClassField = new BatchClassField();
				batchClassFieldDTO.setNew(false);
				mergeBatchClassFieldFromDTO(batchClassField, batchClassFieldDTO);
				batchClass.addBatchClassField(batchClassField);
			} else {
				BatchClassField batchClassField = batchClass.getBatchClassFieldByIdentifier(batchClassFieldDTO.getIdentifier());
				mergeBatchClassFieldFromDTO(batchClassField, batchClassFieldDTO);
			}
		}

		List<BatchClassGroups> assignedRoles = batchClass.getAssignedGroups();
		if (assignedRoles == null) {
			assignedRoles = new ArrayList<BatchClassGroups>();
		}
		assignedRoles.clear();
		if (null != groupNameSet && !groupNameSet.isEmpty()) {
			for (RoleDTO roleDTO : batchClassDTO.getAssignedRole()) {
				for (String group : groupNameSet) {
					if (roleDTO.getName().equals(group)) {
						BatchClassGroups batchClassGroups = new BatchClassGroups();
						batchClassGroups.setGroupName(group);
						batchClassGroups.setBatchClass(batchClass);
						assignedRoles.add(batchClassGroups);
					}
				}
			}
		}

		batchClass.setAssignedGroups(assignedRoles);

		return docTypeNameList;
	}

	public static void mergeBatchClassEmailFromDTO(BatchClassEmailConfiguration batchClassEmailConfiguration,
			EmailConfigurationDTO emailConfigurationDTO) {
		batchClassEmailConfiguration.setUserName(emailConfigurationDTO.getUserName());
		batchClassEmailConfiguration.setPassword(emailConfigurationDTO.getPassword());
		batchClassEmailConfiguration.setServerName(emailConfigurationDTO.getServerName());
		batchClassEmailConfiguration.setServerType(emailConfigurationDTO.getServerType());
		batchClassEmailConfiguration.setFolderName(emailConfigurationDTO.getFolderName());
		batchClassEmailConfiguration.setPortNumber(emailConfigurationDTO.getPortNumber());
		batchClassEmailConfiguration.setIsSSL(emailConfigurationDTO.getIsSSL());
	}

	public static void mergeBatchClassFieldFromDTO(BatchClassField batchClassField, BatchClassFieldDTO batchClassFieldDTO) {
		batchClassField.setDataType(batchClassFieldDTO.getDataType());
		batchClassField.setIdentifier(batchClassFieldDTO.getIdentifier());
		batchClassField.setName(batchClassFieldDTO.getName());
		batchClassField.setFieldOrderNumber(Integer.valueOf(batchClassFieldDTO.getFieldOrderNumber()));
		batchClassField.setDescription(batchClassFieldDTO.getDescription());
		batchClassField.setValidationPattern(batchClassFieldDTO.getValidationPattern());
		batchClassField.setSampleValue(batchClassFieldDTO.getSampleValue());
		batchClassField.setFieldOptionValueList(batchClassFieldDTO.getFieldOptionValueList());
	}

	public static void addAutoGeneratedPageType(DocumentType documentType) {
		PageType pageType = new PageType();
		pageType.setName(documentType.getName() + FIRST_PAGE);
		pageType.setDescription(documentType.getName() + "FIRST_PAGE");
		documentType.addPageType(pageType);
		pageType = new PageType();
		pageType.setName(documentType.getName() + MIDDLE_PAGE);
		pageType.setDescription(documentType.getName() + MIDDLE_PAGE);
		documentType.addPageType(pageType);
		pageType = new PageType();
		pageType.setName(documentType.getName() + LAST_PAGE);
		pageType.setDescription(documentType.getName() + LAST_PAGE);
		documentType.addPageType(pageType);
	}

	public static void mergeBatchClassDocumentFromDTO(DocumentType documentType, DocumentTypeDTO documentTypeDTO) {
		// merge the document fields edited by user.
		documentType.setName(documentTypeDTO.getName());
		documentType.setDescription(documentTypeDTO.getDescription());
		documentType.setRspProjectFileName(documentTypeDTO.getRspProjectFileName());
		documentType.setMinConfidenceThreshold(documentTypeDTO.getMinConfidenceThreshold());
		documentType.setPriority(documentTypeDTO.getPriority());
		documentType.setHidden(documentTypeDTO.isHidden());
		if (documentTypeDTO.getFields(true) != null && documentTypeDTO.getFields(true).size() != 0) {
			for (FieldTypeDTO fieldTypeDTO : documentTypeDTO.getFields(true)) {
				if (fieldTypeDTO.isDeleted()) {
					documentType.removeFieldTypeByIdentifier(fieldTypeDTO.getIdentifier());
				} else if (fieldTypeDTO.isNew()) {
					FieldType fieldType = new FieldType();
					fieldTypeDTO.setNew(false);
					mergeDocumentTypeFieldFromDTO(fieldType, fieldTypeDTO);
					documentType.addFieldType(fieldType);
				} else {
					FieldType fieldType = documentType.getFieldByIdentifier(fieldTypeDTO.getIdentifier());
					mergeDocumentTypeFieldFromDTO(fieldType, fieldTypeDTO);
				}
			}
		}
		if (documentTypeDTO.getFunctionKeys(true) != null && documentTypeDTO.getFunctionKeys(true).size() != 0) {
			for (FunctionKeyDTO functionKeyDTO : documentTypeDTO.getFunctionKeys(true)) {
				if (functionKeyDTO.isDeleted()) {
					documentType.removeFunctionKeyByIdentifier(functionKeyDTO.getIdentifier());
				} else if (functionKeyDTO.isNew()) {
					FunctionKey functionKey = new FunctionKey();
					functionKeyDTO.setNew(false);
					mergeDocumentTypeFieldFromDTO(functionKey, functionKeyDTO);
					documentType.addFunctionKey(functionKey);
				} else {
					FunctionKey functionKey = documentType.getFunctionKeyByIdentifier(functionKeyDTO.getIdentifier());
					mergeDocumentTypeFieldFromDTO(functionKey, functionKeyDTO);
				}
			}
		}

		if (documentTypeDTO.getTableInfos(true) != null && documentTypeDTO.getTableInfos(true).size() != 0) {
			for (TableInfoDTO tableInfoDTO : documentTypeDTO.getTableInfos(true)) {
				if (tableInfoDTO.isDeleted()) {
					documentType.removeTableInfoByIdentifier(tableInfoDTO.getIdentifier());
				} else if (tableInfoDTO.isNew()) {
					TableInfo tableInfo = new TableInfo();
					tableInfoDTO.setNew(false);

					mergeDocumentTypeFieldFromDTO(tableInfo, tableInfoDTO);
					documentType.addTableInfo(tableInfo);
				} else {
					TableInfo tableInfo = documentType.getTableInfoByIdentifier(tableInfoDTO.getIdentifier());
					mergeDocumentTypeFieldFromDTO(tableInfo, tableInfoDTO);
				}
			}
		}

		if (documentTypeDTO.getPages(true) != null && documentTypeDTO.getPages(true).size() != 0) {
			for (PageTypeDTO pageTypeDTO : documentTypeDTO.getPages(true)) {
				if (pageTypeDTO.isDeleted()) {
					documentType.removePageTypeByIdentifier(pageTypeDTO.getIdentifier());
				} else {
					PageType pageType = documentType.getPageByIdentifier(pageTypeDTO.getIdentifier());
					mergeDocumentTypePageFromDTO(pageType, pageTypeDTO);
				}
			}
		}
	}

	private static void mergeDocumentTypeFieldFromDTO(FunctionKey functionKey, FunctionKeyDTO functionKeyDTO) {
		functionKey.setIdentifier(functionKeyDTO.getIdentifier());
		functionKey.setMethodName(functionKeyDTO.getMethodName());
		functionKey.setUiLabel(functionKeyDTO.getMethodDescription());
		functionKey.setShortcutKeyname(functionKeyDTO.getShortcutKeyName());
	}

	public static void mergeDocumentTypeFieldFromDTO(TableInfo tableInfo, TableInfoDTO tableInfoDTO) {
		// merge the field types edited by user.
		tableInfo.setEndPattern(tableInfoDTO.getEndPattern());
		tableInfo.setStartPattern(tableInfoDTO.getStartPattern());
		tableInfo.setName(tableInfoDTO.getName());

		if (tableInfoDTO.getTableColumnInfoList(true) != null) {
			for (TableColumnInfoDTO tableColumnInfoDTO : tableInfoDTO.getTableColumnInfoList(true)) {
				if (tableColumnInfoDTO.isDeleted()) {
					tableInfo.removeTableColumnsInfoById(Long.valueOf(tableColumnInfoDTO.getIdentifier()));
				} else if (tableColumnInfoDTO.isNew()) {
					TableColumnsInfo tabColumnsInfo = new TableColumnsInfo();
					tableColumnInfoDTO.setNew(false);
					mergeTableColumnInfoFromDTO(tabColumnsInfo, tableColumnInfoDTO);
					tableInfo.addTableColumnInfo(tabColumnsInfo);
				} else {
					TableColumnsInfo tabColumnsInfo = tableInfo.getTableColumnInfobyIdentifier(tableColumnInfoDTO.getIdentifier());
					mergeTableColumnInfoFromDTO(tabColumnsInfo, tableColumnInfoDTO);
				}
			}
		}

	}

	public static void mergeDocumentTypeFieldFromDTO(FieldType fieldType, FieldTypeDTO fieldTypeDTO) {
		// merge the field types edited by user.
		fieldType.setName(fieldTypeDTO.getName());
		fieldType.setDescription(fieldTypeDTO.getDescription());
		fieldType.setDataType(fieldTypeDTO.getDataType());
		fieldType.setPattern(fieldTypeDTO.getPattern());
		fieldType.setSampleValue(fieldTypeDTO.getSampleValue());
		fieldType.setFieldOrderNumber(Integer.parseInt(fieldTypeDTO.getFieldOrderNumber()));
		fieldType.setBarcodeType(fieldTypeDTO.getBarcodeType());
		fieldType.setHidden(fieldTypeDTO.isHidden());
		fieldType.setFieldOptionValueList(fieldTypeDTO.getFieldOptionValueList());
		if (fieldTypeDTO.getKvExtractionList(true) != null) {
			for (KVExtractionDTO kvExtractionDTO : fieldTypeDTO.getKvExtractionList(true)) {
				if (kvExtractionDTO.isDeleted()) {
					fieldType.removeKvExtractionById(Long.valueOf(kvExtractionDTO.getIdentifier()));
				} else if (kvExtractionDTO.isNew()) {
					KVExtraction kvExtraction = new KVExtraction();
					kvExtractionDTO.setNew(false);
					mergeFieldTypeKVExtractionFromDTO(kvExtraction, kvExtractionDTO);
					fieldType.addKVExtraction(kvExtraction);
				} else {
					KVExtraction kvExtraction = fieldType.getKVExtractionbyIdentifier(kvExtractionDTO.getIdentifier());
					mergeFieldTypeKVExtractionFromDTO(kvExtraction, kvExtractionDTO);
				}
			}
		}
		if (fieldTypeDTO.getRegexList(true) != null) {
			for (RegexDTO regexDTO : fieldTypeDTO.getRegexList(true)) {
				if (regexDTO.isDeleted()) {
					fieldType.removeRegexValidationById(Long.valueOf(regexDTO.getIdentifier()));
				} else if (regexDTO.isNew()) {
					RegexValidation regexValidation = new RegexValidation();
					regexDTO.setNew(false);
					regexValidation.setPattern(regexDTO.getPattern());
					fieldType.addRegexValidation(regexValidation);
				} else {
					RegexValidation regexValidation = fieldType.getRegexValidationbyIdentifier(regexDTO.getIdentifier());
					regexValidation.setPattern(regexDTO.getPattern());
				}
			}
		}
	}

	public static void mergeFieldTypeKVExtractionFromDTO(KVExtraction kvExtraction, KVExtractionDTO kvExtractionDTO) {
		kvExtraction.setLocationType(kvExtractionDTO.getLocationType());
		kvExtraction.setKeyPattern(kvExtractionDTO.getKeyPattern());
		kvExtraction.setValuePattern(kvExtractionDTO.getValuePattern());
		kvExtraction.setNoOfWords(kvExtractionDTO.getNoOfWords());
		kvExtraction.setLength(kvExtractionDTO.getLength());
		kvExtraction.setWidth(kvExtractionDTO.getWidth());
		kvExtraction.setXoffset(kvExtractionDTO.getXoffset());
		kvExtraction.setYoffset(kvExtractionDTO.getYoffset());
		kvExtraction.setFetchValue(kvExtractionDTO.getFetchValue());
		kvExtraction.setMultiplier(kvExtractionDTO.getMultiplier());
	}

	public static void mergeTableColumnInfoFromDTO(TableColumnsInfo tabColumnsInfo, TableColumnInfoDTO tableColumnInfoDTO) {
		tabColumnsInfo.setBetweenLeft(tableColumnInfoDTO.getBetweenLeft());
		tabColumnsInfo.setBetweenRight(tableColumnInfoDTO.getBetweenRight());
		tabColumnsInfo.setColumnName(tableColumnInfoDTO.getColumnName());
		tabColumnsInfo.setColumnPattern(tableColumnInfoDTO.getColumnPattern());
		tabColumnsInfo.setRequired(tableColumnInfoDTO.isRequired());
	}

	public static void mergeDocumentTypePageFromDTO(PageType pageType, PageTypeDTO pageTypeDTO) {
		// will be used in edit case.
	}

	public static void mergeBatchClassModuleFromDTO(BatchClassModule module, BatchClassModuleDTO moduleDTO) {
		module.setRemoteURL(moduleDTO.getRemoteURL());
		module.setRemoteBatchClassIdentifier(moduleDTO.getRemoteBatchClassIdentifier());
		Collection<BatchClassPluginDTO> pluginDTOs = moduleDTO.getBatchClassPlugins();

		for (BatchClassPluginDTO pluginDTO : pluginDTOs) {
			BatchClassPlugin batchClassPlugin = module.getBatchClassPluginById(Long.valueOf(pluginDTO.getIdentifier()));
			mergeBatchClassPluginFromDTO(batchClassPlugin, pluginDTO);
		}
	}

	public static void mergeBatchClassPluginFromDTO(BatchClassPlugin batchClassPlugin, BatchClassPluginDTO pluginDTO) {
		Map<String, BatchClassPluginConfig> batchClassPluginConfigMap = new HashMap<String, BatchClassPluginConfig>();
		Map<String, BatchClassDynamicPluginConfig> batchClassDynamicPluginConfigMap = new HashMap<String, BatchClassDynamicPluginConfig>();

		Collection<BatchClassPluginConfigDTO> pluginConfigDTOs = pluginDTO.getBatchClassPluginConfigs();

		Collection<BatchClassDynamicPluginConfigDTO> dynamicPluginConfigDTOs = pluginDTO.getBatchClassDynamicPluginConfigs();

		for (BatchClassPluginConfigDTO batchClassPluginConfigDTO : pluginConfigDTOs) {
			BatchClassPluginConfig batchClassPluginConfig = batchClassPlugin.getBatchClassPluginConfigById(Long
					.valueOf(batchClassPluginConfigDTO.getIdentifier()));

			if (batchClassPluginConfig == null) {
				batchClassPluginConfig = new BatchClassPluginConfig();
				PluginConfig pluginConfig = new PluginConfig();
				pluginConfig.setMultiValue(false);
				pluginConfig.setMandatory(true);
				pluginConfig.setDataType(DataType.STRING);
				batchClassPluginConfig.setPluginConfig(pluginConfig);
			}
			refreshBatchClassPluginConfigFromDTO(batchClassPluginConfig, batchClassPluginConfigDTO);
			batchClassPlugin.addBatchClassPluginConfig(batchClassPluginConfig);

			if (batchClassPluginConfigDTO.getKvPageProcessDTOs(true) != null) {
				for (KVPageProcessDTO kvPageProcessDTO : batchClassPluginConfigDTO.getKvPageProcessDTOs(true)) {
					if (kvPageProcessDTO.getIsDeleted()) {
						batchClassPluginConfig.removeKvPageProcessById(kvPageProcessDTO.getIdentifier());
					} else if (kvPageProcessDTO.getIsNew()) {
						KVPageProcess kvPageProcess = new KVPageProcess();
						kvPageProcessDTO.setIsNew(false);
						kvPageProcessDTO.setIdentifier(0L);
						kvPageProcess.setBatchClassPluginConfig(batchClassPluginConfig);
						mergeKvPageProcessFromDTO(kvPageProcess, kvPageProcessDTO);
						batchClassPluginConfig.addKVPageProcess(kvPageProcess);
					} else {
						KVPageProcess kvPageProcess = batchClassPluginConfig.getKVPageProcessbyIdentifier(String
								.valueOf(kvPageProcessDTO.getIdentifier()));
						mergeKvPageProcessFromDTO(kvPageProcess, kvPageProcessDTO);
					}
				}
			}

			batchClassPluginConfigMap.put(batchClassPluginConfigDTO.getIdentifier(), batchClassPluginConfig);
		}

		for (BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO : dynamicPluginConfigDTOs) {
			BatchClassDynamicPluginConfig batchClassDynamicPluginConfig = batchClassPlugin.getBatchClassDynamicPluginConfigById(Long
					.valueOf(batchClassDynamicPluginConfigDTO.getIdentifier()));

			if (batchClassDynamicPluginConfig == null) {
				batchClassDynamicPluginConfig = new BatchClassDynamicPluginConfig();
				batchClassDynamicPluginConfig.setId(0);
			}
			refreshBatchClassDynamicPluginConfigFromDTO(batchClassDynamicPluginConfig, batchClassDynamicPluginConfigDTO);
			batchClassPlugin.addBatchClassDynamicPluginConfig(batchClassDynamicPluginConfig);
			batchClassDynamicPluginConfigMap.put(batchClassDynamicPluginConfigDTO.getIdentifier(), batchClassDynamicPluginConfig);

		}

		for (BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO : dynamicPluginConfigDTOs) {
			BatchClassDynamicPluginConfig config = batchClassDynamicPluginConfigMap.get(batchClassDynamicPluginConfigDTO
					.getIdentifier());
			if (batchClassDynamicPluginConfigDTO.getChildren() != null && !batchClassDynamicPluginConfigDTO.getChildren().isEmpty()) {
				Collection<BatchClassDynamicPluginConfigDTO> children = batchClassDynamicPluginConfigDTO.getChildren();
				for (BatchClassDynamicPluginConfigDTO child : children) {
					BatchClassDynamicPluginConfig childConfig = batchClassDynamicPluginConfigMap.get(child.getIdentifier());
					if (childConfig != null) {
						config.addChild(childConfig);
					}
				}
			}
		}

		for (BatchClassDynamicPluginConfigDTO parent : dynamicPluginConfigDTOs) {

			if (parent != null && parent.getChildren() != null) {
				BatchClassDynamicPluginConfig dynamicParentConfig = batchClassDynamicPluginConfigMap.get(parent.getIdentifier());
				mergeDeletedDynamicConfigs(dynamicParentConfig, pluginDTO);
			}
		}
		mergeDeletedDynamicConfigs(batchClassPlugin, pluginDTO);
	}

	/*
	 * private static void mergeDeletedConfigs(BatchClassPlugin batchClassPlugin, BatchClassPluginDTO pluginDTO) {
	 * Collection<BatchClassPluginConfig> batchClassPluginConfigs = batchClassPlugin.getBatchClassPluginConfigs();
	 * 
	 * if (batchClassPluginConfigs == null) return;
	 * 
	 * List<BatchClassPluginConfig> removedConfigs = new ArrayList<BatchClassPluginConfig>(); for (BatchClassPluginConfig
	 * batchClassPluginConfig : batchClassPluginConfigs) { if (batchClassPluginConfig.getId() == 0) continue; if
	 * (pluginDTO.getBatchClassPluginConfigDTOById(String.valueOf(batchClassPluginConfig.getId())) == null) {
	 * removedConfigs.add(batchClassPluginConfig); } }
	 * 
	 * for (BatchClassPluginConfig batchClassPluginConfig : removedConfigs) {
	 * batchClassPlugin.removeBatchClassPluginConfig(batchClassPluginConfig); } }
	 */
	private static void mergeDeletedDynamicConfigs(BatchClassPlugin batchClassPlugin, BatchClassPluginDTO pluginDTO) {
		if (batchClassPlugin.getPlugin().getPluginName().equalsIgnoreCase("FUZZYDB")) {
			Collection<BatchClassDynamicPluginConfig> batchClassDynamicPluginConfigs = batchClassPlugin
					.getBatchClassDynamicPluginConfigs();

			if (batchClassDynamicPluginConfigs == null) {
				return;
			}
			List<BatchClassDynamicPluginConfig> removedConfigs = new ArrayList<BatchClassDynamicPluginConfig>();
			for (BatchClassDynamicPluginConfig batchClassDynamicPluginConfig : batchClassDynamicPluginConfigs) {
				if (batchClassDynamicPluginConfig.getId() == 0) {
					continue;
				}
				if (pluginDTO.getBatchClassDynamicPluginConfigDTOById(String.valueOf(batchClassDynamicPluginConfig.getId())) == null) {
					removedConfigs.add(batchClassDynamicPluginConfig);
				}
			}

			for (BatchClassDynamicPluginConfig batchClassDynamicPluginConfig : removedConfigs) {
				batchClassPlugin.removeBatchClassDynamicPluginConfig(batchClassDynamicPluginConfig);
			}
		}
	}

	/*
	 * private static void mergeDeletedConfigs(BatchClassPluginConfig config, BatchClassPluginDTO pluginDTO) {
	 * Collection<BatchClassPluginConfig> batchClassPluginConfigs = config.getChildren();
	 * 
	 * if (batchClassPluginConfigs == null) return;
	 * 
	 * List<BatchClassPluginConfig> removedConfigs = new ArrayList<BatchClassPluginConfig>(); for (BatchClassPluginConfig
	 * batchClassPluginConfig : batchClassPluginConfigs) { if (batchClassPluginConfig.getId() == 0) continue; if
	 * (pluginDTO.getBatchClassPluginConfigDTOById(String.valueOf(batchClassPluginConfig.getId())) == null) {
	 * removedConfigs.add(batchClassPluginConfig); } }
	 * 
	 * for (BatchClassPluginConfig batchClassPluginConfig : removedConfigs) { config.removeChild(batchClassPluginConfig); } }
	 */

	private static void mergeDeletedDynamicConfigs(BatchClassDynamicPluginConfig config, BatchClassPluginDTO pluginDTO) {
		if (config != null) {
			Collection<BatchClassDynamicPluginConfig> batchClassDynamicPluginConfigs = config.getChildren();

			if (batchClassDynamicPluginConfigs == null) {
				return;
			}

			List<BatchClassDynamicPluginConfig> removedConfigs = new ArrayList<BatchClassDynamicPluginConfig>();
			for (BatchClassDynamicPluginConfig batchClassDynamicPluginConfig : batchClassDynamicPluginConfigs) {
				if (batchClassDynamicPluginConfig.getId() == 0) {
					continue;
				}
				if (pluginDTO.getBatchClassDynamicPluginConfigDTOById(String.valueOf(batchClassDynamicPluginConfig.getId())) == null) {
					removedConfigs.add(batchClassDynamicPluginConfig);
				}
			}

			for (BatchClassDynamicPluginConfig batchClassDynamicPluginConfig : removedConfigs) {
				config.removeChild(batchClassDynamicPluginConfig);
			}
		}
	}

	public static void refreshBatchClassPluginConfigFromDTO(BatchClassPluginConfig batchClassPluginConfig,
			BatchClassPluginConfigDTO batchClassPluginConfigDTO) {
		batchClassPluginConfig.setDescription(batchClassPluginConfigDTO.getDescription());
		batchClassPluginConfig.setEntityState(EntityState.NEW);
		batchClassPluginConfig.setName(batchClassPluginConfigDTO.getName());
		batchClassPluginConfig.setQualifier(batchClassPluginConfigDTO.getQualifier());
		batchClassPluginConfig.setValue(batchClassPluginConfigDTO.getValue());
	}

	public static void refreshBatchClassDynamicPluginConfigFromDTO(BatchClassDynamicPluginConfig batchClassDynamicPluginConfig,
			BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO) {
		batchClassDynamicPluginConfig.setDescription(batchClassDynamicPluginConfigDTO.getDescription());
		batchClassDynamicPluginConfig.setEntityState(EntityState.NEW);
		batchClassDynamicPluginConfig.setName(batchClassDynamicPluginConfigDTO.getName());
		batchClassDynamicPluginConfig.setValue(batchClassDynamicPluginConfigDTO.getValue());
	}

	public static BatchClassDTO createBatchClassDTO(BatchClass batchClass) {
		BatchClassDTO batchClassDTO = new BatchClassDTO();
		batchClassDTO.setIdentifier(batchClass.getIdentifier());
		batchClassDTO.setDescription(batchClass.getDescription());
		batchClassDTO.setName(batchClass.getName());
		batchClassDTO.setPriority(AdminConstants.EMPTY_STRING + batchClass.getPriority());
		batchClassDTO.setUncFolder(batchClass.getUncFolder());
		batchClassDTO.setVersion(batchClass.getVersion());
		batchClassDTO.setDeleted(batchClass.isDeleted());

		List<DocumentType> documentTypes = batchClass.getDocumentTypes();

		for (DocumentType documentType : documentTypes) {
			DocumentTypeDTO documentTypeDTO = createDocumentTypeDTO(batchClassDTO, documentType);
			batchClassDTO.addDocumentType(documentTypeDTO);
		}

		for (BatchClassModule batchClassModule : batchClass.getBatchClassModules()) {
			BatchClassModuleDTO batchClassModuleDTO = createBatchClassModuleDTO(batchClassDTO, batchClassModule);
			batchClassDTO.addModule(batchClassModuleDTO);
		}

		for (BatchClassEmailConfiguration batchClassEmailConfiguration : batchClass.getEmailConfigurations()) {
			EmailConfigurationDTO emailConfigurationDTO = createEmailDTO(batchClassDTO, batchClassEmailConfiguration);
			batchClassDTO.addEmailConfiguration(emailConfigurationDTO);
		}

		for (BatchClassField batchClassField : batchClass.getBatchClassField()) {
			BatchClassFieldDTO batchClassFieldDTO = createBatchClassFieldDTO(batchClassDTO, batchClassField);
			batchClassDTO.addBatchClassField(batchClassFieldDTO);
		}

		for (BatchClassGroups role : batchClass.getAssignedGroups()) {
			RoleDTO roleDTO = createRoleDTO(role);
			batchClassDTO.addAssignedRole(roleDTO);
		}
		batchClassDTO.setDirty(false);
		return batchClassDTO;
	}

	public static EmailConfigurationDTO createEmailDTO(BatchClassDTO batchClassDTO,
			BatchClassEmailConfiguration batchClassEmailConfiguration) {
		EmailConfigurationDTO emailConfigurationDTO = new EmailConfigurationDTO();
		emailConfigurationDTO.setBatchClass(batchClassDTO);
		emailConfigurationDTO.setUserName(batchClassEmailConfiguration.getUserName());
		emailConfigurationDTO.setPassword(batchClassEmailConfiguration.getPassword());
		emailConfigurationDTO.setServerName(batchClassEmailConfiguration.getServerName());
		emailConfigurationDTO.setServerType(batchClassEmailConfiguration.getServerType());
		emailConfigurationDTO.setFolderName(batchClassEmailConfiguration.getFolderName());
		emailConfigurationDTO.setIdentifier(String.valueOf(batchClassEmailConfiguration.getId()));
		emailConfigurationDTO.setPortNumber(batchClassEmailConfiguration.getPortNumber());
		emailConfigurationDTO.setIsSSL(batchClassEmailConfiguration.isSSL());
		return emailConfigurationDTO;
	}

	public static BatchClassFieldDTO createBatchClassFieldDTO(BatchClassDTO batchClassDTO, BatchClassField batchClassField) {
		BatchClassFieldDTO batchClassFieldDTO = new BatchClassFieldDTO();
		batchClassFieldDTO.setBatchClass(batchClassDTO);
		batchClassFieldDTO.setDataType(batchClassField.getDataType());
		batchClassFieldDTO.setIdentifier(batchClassField.getIdentifier());
		batchClassFieldDTO.setName(batchClassField.getName());
		batchClassFieldDTO.setFieldOrderNumber(String.valueOf(batchClassField.getFieldOrderNumber()));
		batchClassFieldDTO.setDescription(batchClassField.getDescription());
		batchClassFieldDTO.setValidationPattern(batchClassField.getValidationPattern());
		batchClassFieldDTO.setSampleValue(batchClassField.getSampleValue());
		batchClassFieldDTO.setFieldOptionValueList(batchClassField.getFieldOptionValueList());
		return batchClassFieldDTO;
	}

	public static DocumentTypeDTO createDocumentTypeDTO(BatchClassDTO batchClassDTO, DocumentType documentType) {
		DocumentTypeDTO documentTypeDTO = new DocumentTypeDTO();
		documentTypeDTO.setBatchClass(batchClassDTO);
		documentTypeDTO.setDescription(documentType.getDescription());
		documentTypeDTO.setRspProjectFileName(documentType.getRspProjectFileName());
		documentTypeDTO.setMinConfidenceThreshold(documentType.getMinConfidenceThreshold());
		documentTypeDTO.setName(documentType.getName());
		documentTypeDTO.setPriority(documentType.getPriority());
		documentTypeDTO.setIdentifier(documentType.getIdentifier());
		documentTypeDTO.setHidden(documentType.isHidden());
		List<PageType> pageTypesList = documentType.getPages();
		List<FieldType> fieldTypesList = documentType.getFieldTypes();
		List<FunctionKey> functionKeyList = documentType.getFunctionKeys();
		for (PageType pageType : pageTypesList) {
			PageTypeDTO pageTypeDTO = createPageTypeDTO(documentTypeDTO, pageType);
			documentTypeDTO.addPageType(pageTypeDTO);
		}
		for (FieldType fieldType : fieldTypesList) {
			FieldTypeDTO fieldTypeDTO = createFieldTypeDTO(documentTypeDTO, fieldType);
			documentTypeDTO.addFieldTypeDTO(fieldTypeDTO);
		}
		// TODO Create TableInfoDTO and TableColumsInfoDto to be used for table UI.
		// Use the following code for above.
		for (TableInfo tableInfo : documentType.getTableInfos()) {
			TableInfoDTO tableInfoDTO = createTableInfoDTO(documentTypeDTO, tableInfo);
			documentTypeDTO.addTableInfoDTO(tableInfoDTO);
		}

		for (FunctionKey functionKey : functionKeyList) {
			FunctionKeyDTO functionKeyDTO = createFunctionKeyDTO(documentTypeDTO, functionKey);
			documentTypeDTO.addFunctionKey(functionKeyDTO);
		}
		return documentTypeDTO;
	}

	public static TableInfoDTO createTableInfoDTO(DocumentTypeDTO documentTypeDTO, TableInfo tableInfo) {
		TableInfoDTO tableInfoDTO = new TableInfoDTO();

		tableInfoDTO.setDocTypeDTO(documentTypeDTO);
		tableInfoDTO.setEndPattern(tableInfo.getEndPattern());
		tableInfoDTO.setName(tableInfo.getName());
		tableInfoDTO.setIdentifier(String.valueOf(tableInfo.getId()));
		tableInfoDTO.setStartPattern(tableInfo.getStartPattern());

		for (TableColumnsInfo tableColumnsInfo : tableInfo.getTableColumnsInfo()) {
			TableColumnInfoDTO columnInfoDTO = createTableColumnInfoDTO(tableInfoDTO, tableColumnsInfo);
			tableInfoDTO.addColumnInfo(columnInfoDTO);
		}
		return tableInfoDTO;
	}

	public static PageTypeDTO createPageTypeDTO(DocumentTypeDTO documentTypeDTO, PageType pageType) {
		PageTypeDTO pageTypeDTO = new PageTypeDTO();
		pageTypeDTO.setDescription(pageType.getDescription());
		pageTypeDTO.setDocumentTypeDTO(documentTypeDTO);
		pageTypeDTO.setName(pageType.getName());
		pageTypeDTO.setIdentifier(pageType.getIdentifier());
		return pageTypeDTO;
	}

	public static FieldTypeDTO createFieldTypeDTO(DocumentTypeDTO documentTypeDTO, FieldType fieldType) {
		FieldTypeDTO fieldTypeDTO = new FieldTypeDTO();
		fieldTypeDTO.setIdentifier(fieldType.getIdentifier());
		fieldTypeDTO.setDataType(fieldType.getDataType());
		fieldTypeDTO.setDescription(fieldType.getDescription());
		fieldTypeDTO.setDocTypeDTO(documentTypeDTO);
		fieldTypeDTO.setName(fieldType.getName());
		fieldTypeDTO.setPattern(fieldType.getPattern());
		fieldTypeDTO.setSampleValue(fieldType.getSampleValue());
		fieldTypeDTO.setFieldOptionValueList(fieldType.getFieldOptionValueList());
		fieldTypeDTO.setFieldOrderNumber(String.valueOf(fieldType.getFieldOrderNumber()));
		fieldTypeDTO.setBarcodeType(fieldType.getBarcodeType());
		fieldTypeDTO.setHidden(Boolean.valueOf(fieldType.isHidden()));
		for (KVExtraction kvExtraction : fieldType.getKvExtraction()) {
			KVExtractionDTO kvExtractionDTO = createKVExtractionDTO(fieldTypeDTO, kvExtraction);
			fieldTypeDTO.addKvExtraction(kvExtractionDTO);
		}
		for (RegexValidation regexValidation : fieldType.getRegexValidation()) {
			RegexDTO regexDTO = createRegexDTO(fieldTypeDTO, regexValidation);
			fieldTypeDTO.addRegex(regexDTO);
		}
		return fieldTypeDTO;
	}

	public static KVExtractionDTO createKVExtractionDTO(FieldTypeDTO fieldTypeDTO, KVExtraction kvExtraction) {
		KVExtractionDTO kvExtractionDTO = new KVExtractionDTO();
		kvExtractionDTO.setFieldTypeDTO(fieldTypeDTO);
		kvExtractionDTO.setIdentifier(String.valueOf(kvExtraction.getId()));
		kvExtractionDTO.setKeyPattern(kvExtraction.getKeyPattern());
		kvExtractionDTO.setLocationType(kvExtraction.getLocationType());
		kvExtractionDTO.setValuePattern(kvExtraction.getValuePattern());
		kvExtractionDTO.setNoOfWords(kvExtraction.getNoOfWords());
		kvExtractionDTO.setLength(kvExtraction.getLength());
		kvExtractionDTO.setWidth(kvExtraction.getWidth());
		kvExtractionDTO.setXoffset(kvExtraction.getXoffset());
		kvExtractionDTO.setYoffset(kvExtraction.getYoffset());
		kvExtractionDTO.setFetchValue(kvExtraction.getFetchValue());
		kvExtractionDTO.setMultiplier(kvExtraction.getMultiplier());
		return kvExtractionDTO;
	}

	public static FunctionKeyDTO createFunctionKeyDTO(DocumentTypeDTO documentTypeDTO, FunctionKey functionKey) {
		FunctionKeyDTO functionKeyDTO = new FunctionKeyDTO();
		functionKeyDTO.setDocTypeDTO(documentTypeDTO);
		functionKeyDTO.setMethodName(functionKey.getMethodName());
		functionKeyDTO.setMethodDescription(functionKey.getUiLabel());
		functionKeyDTO.setShortcutKeyName(functionKey.getShortcutKeyname());
		functionKeyDTO.setIdentifier(functionKey.getIdentifier());
		return functionKeyDTO;
	}

	public static TableColumnInfoDTO createTableColumnInfoDTO(TableInfoDTO tableInfoDTO, TableColumnsInfo tableColumnsInfo) {
		TableColumnInfoDTO columnInfoDTO = new TableColumnInfoDTO();
		columnInfoDTO.setBetweenLeft(tableColumnsInfo.getBetweenLeft());
		columnInfoDTO.setBetweenRight(tableColumnsInfo.getBetweenRight());
		columnInfoDTO.setColumnName(tableColumnsInfo.getColumnName());
		columnInfoDTO.setColumnPattern(tableColumnsInfo.getColumnPattern());
		columnInfoDTO.setRequired(tableColumnsInfo.isRequired());
		columnInfoDTO.setIdentifier(String.valueOf(tableColumnsInfo.getId()));
		columnInfoDTO.setTableInfoDTO(tableInfoDTO);
		return columnInfoDTO;
	}

	public static RegexDTO createRegexDTO(FieldTypeDTO fieldTypeDTO, RegexValidation regexValidation) {
		RegexDTO regexDTO = new RegexDTO();
		regexDTO.setPattern(regexValidation.getPattern());
		regexDTO.setIdentifier(String.valueOf(regexValidation.getId()));
		regexDTO.setFieldTypeDTO(fieldTypeDTO);
		return regexDTO;

	}

	public static BatchClassModuleDTO createBatchClassModuleDTO(BatchClassDTO batchClassDTO, BatchClassModule batchClassModule) {

		BatchClassModuleDTO batchClassModuleDTO = new BatchClassModuleDTO();
		batchClassModuleDTO.setBatchClass(batchClassDTO);
		batchClassModuleDTO.setIdentifier(AdminConstants.EMPTY_STRING + batchClassModule.getId());
		batchClassModuleDTO.setOrderNumber(batchClassModule.getOrderNumber());
		batchClassModuleDTO.setRemoteURL(batchClassModule.getRemoteURL());
		batchClassModuleDTO.setRemoteBatchClassIdentifier(batchClassModule.getRemoteBatchClassIdentifier());

		batchClassModuleDTO.setModule(createModuleDTO(batchClassModule.getModule()));

		for (BatchClassPlugin batchClassPlugin : batchClassModule.getBatchClassPlugins()) {
			BatchClassPluginDTO batchClassPluginDTO = createBatchClassPluginDTO(batchClassModuleDTO, batchClassPlugin);
			batchClassModuleDTO.addBatchClassPlugin(batchClassPluginDTO);
		}
		return batchClassModuleDTO;
	}

	public static ModuleDTO createModuleDTO(Module module) {
		ModuleDTO moduleDTO = new ModuleDTO();
		moduleDTO.setDescription(module.getDescription());
		moduleDTO.setIdentifier(AdminConstants.EMPTY_STRING + module.getId());
		moduleDTO.setName(module.getName());
		moduleDTO.setVersion(module.getVersion());
		return moduleDTO;
	}

	public static BatchClassPluginDTO createBatchClassPluginDTO(BatchClassModuleDTO batchClassModuleDTO,
			BatchClassPlugin batchClassPlugin) {
		BatchClassPluginDTO batchClassPluginDTO = new BatchClassPluginDTO();
		batchClassPluginDTO.setBatchClassModule(batchClassModuleDTO);
		batchClassPluginDTO.setIdentifier(AdminConstants.EMPTY_STRING + batchClassPlugin.getId());
		batchClassPluginDTO.setOrderNumber(batchClassPlugin.getOrderNumber());

		batchClassPluginDTO.setPlugin(createPluginDetailsDTO(batchClassPlugin.getPlugin()));

		List<BatchClassPluginConfig> batchClassPluginConfigs = batchClassPlugin.getBatchClassPluginConfigs();

		for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
			BatchClassPluginConfigDTO batchClassPluginConfigDTO = createBatchClassPluginConfigDTO(batchClassPluginDTO,
					batchClassPluginConfig);
			batchClassPluginDTO.addBatchClassPluginConfig(batchClassPluginConfigDTO);

		}

		List<BatchClassDynamicPluginConfig> batchClassDynamicPluginConfigs = batchClassPlugin.getBatchClassDynamicPluginConfigs();

		for (BatchClassDynamicPluginConfig batchClassDynamicPluginConfig : batchClassDynamicPluginConfigs) {
			BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO = createBatchClassDynamicPluginConfigDTO(
					batchClassPluginDTO, batchClassDynamicPluginConfig);
			batchClassPluginDTO.addBatchClassDynamicPluginConfig(batchClassDynamicPluginConfigDTO);

		}

		return batchClassPluginDTO;
	}

	public static PluginDetailsDTO createPluginDetailsDTO(Plugin plugin) {
		PluginDetailsDTO pluginDetailsDTO = new PluginDetailsDTO();
		pluginDetailsDTO.setIdentifier(AdminConstants.EMPTY_STRING + plugin.getId());
		pluginDetailsDTO.setPluginDescription(plugin.getDescription());
		pluginDetailsDTO.setPluginName(plugin.getPluginName());
		return pluginDetailsDTO;
	}

	public static BatchClassPluginConfigDTO createBatchClassPluginConfigDTO(BatchClassPluginDTO batchClassPluginDTO,
			BatchClassPluginConfig batchClassPluginConfig) {

		BatchClassPluginConfigDTO batchClassPluginConfigDTO = new BatchClassPluginConfigDTO();
		batchClassPluginConfigDTO.setBatchClassPlugin(batchClassPluginDTO);
		batchClassPluginConfigDTO.setDescription(batchClassPluginConfig.getDescription());
		batchClassPluginConfigDTO.setIdentifier(AdminConstants.EMPTY_STRING + batchClassPluginConfig.getId());
		batchClassPluginConfigDTO.setName(batchClassPluginConfig.getName());
		batchClassPluginConfigDTO.setSampleValue(batchClassPluginConfig.getSampleValue());
		batchClassPluginConfigDTO.setQualifier(batchClassPluginConfig.getQualifier());

		if (batchClassPluginConfig.getKvPageProcesses() != null && !batchClassPluginConfig.getKvPageProcesses().isEmpty()) {
			List<KVPageProcess> kvPageProcesses = batchClassPluginConfig.getKvPageProcesses();
			List<KVPageProcessDTO> kvPageProcessDTOs = new ArrayList<KVPageProcessDTO>();
			for (KVPageProcess kvPageProcess : kvPageProcesses) {
				KVPageProcessDTO kvPageProcessDTO = new KVPageProcessDTO();
				kvPageProcessDTO.setKeyPattern(kvPageProcess.getKeyPattern());
				kvPageProcessDTO.setLocationType(kvPageProcess.getLocationType());
				kvPageProcessDTO.setValuePattern(kvPageProcess.getValuePattern());
				kvPageProcessDTO.setNoOfWords(kvPageProcess.getNoOfWords());
				kvPageProcessDTO.setIdentifier(kvPageProcess.getId());
				kvPageProcessDTO.setPageLevelFieldName(kvPageProcess.getPageLevelFieldName());
				kvPageProcessDTOs.add(kvPageProcessDTO);
			}
			batchClassPluginConfigDTO.setKvPageProcessDTOs(kvPageProcessDTOs);
		} else {
			batchClassPluginConfigDTO.setKvPageProcessDTOs(null);
		}

		PluginConfigurationDTO pluginConfigurationDTO = new PluginConfigurationDTO();

		if (batchClassPluginConfig.getPluginConfig() != null) {
			batchClassPluginConfigDTO.setMultivalue(batchClassPluginConfig.getPluginConfig().isMultiValue());
			batchClassPluginConfigDTO.setMandatory(batchClassPluginConfig.getPluginConfig().isMandatory());
			batchClassPluginConfigDTO.setDataType(batchClassPluginConfig.getPluginConfig().getDataType());
			pluginConfigurationDTO.setFieldName(batchClassPluginConfig.getPluginConfig().getName());
			pluginConfigurationDTO.setFieldValue(batchClassPluginConfig.getValue());
			pluginConfigurationDTO.setIdentifier(AdminConstants.EMPTY_STRING + batchClassPluginConfig.getPluginConfig().getId());
			pluginConfigurationDTO.setSampleValue(batchClassPluginConfig.getPluginConfig().getSampleValue());
		}
		batchClassPluginConfigDTO.setPluginConfig(pluginConfigurationDTO);
		batchClassPluginConfigDTO.setValue(batchClassPluginConfig.getValue());
		pluginConfigurationDTO.setDirty(false);

		// Setting the parent

		/*
		 * if (batchClassPluginConfig.getParent() != null) { BatchClassPluginConfig parentPluginConfig =
		 * batchClassPluginConfig.getParent(); BatchClassPluginConfigDTO parent = setBatchClassPluginConfigDTO(batchClassPluginDTO,
		 * parentPluginConfig); parent.setParent(null); PluginConfigurationDTO parentPluginConfigurationDTO = new
		 * PluginConfigurationDTO();
		 * 
		 * if (batchClassPluginConfig.getPluginConfig() != null) {
		 * parentPluginConfigurationDTO.setFieldName(parentPluginConfig.getPluginConfig().getName());
		 * parentPluginConfigurationDTO.setFieldValue(parentPluginConfig.getValue());
		 * parentPluginConfigurationDTO.setIdentifier(AdminConstants.EMPTY_STRING + parentPluginConfig.getPluginConfig().getId());
		 * parentPluginConfigurationDTO.setSampleValue(parentPluginConfig.getPluginConfig().getSampleValue()); }
		 * parent.setPluginConfig(parentPluginConfigurationDTO); parent.setValue(parentPluginConfig.getValue());
		 * parentPluginConfigurationDTO.setDirty(false);
		 * 
		 * batchClassPluginConfigDTO.setParent(parent); }
		 * 
		 * // setting the children
		 * 
		 * if (batchClassPluginConfig.getChildren() != null) { List<BatchClassPluginConfigDTO> children = new
		 * ArrayList<BatchClassPluginConfigDTO>(); for (BatchClassPluginConfig child : batchClassPluginConfig.getChildren()) {
		 * BatchClassPluginConfigDTO childBatchClassPluginConfigDTO = setBatchClassPluginConfigDTO(batchClassPluginDTO, child);
		 * childBatchClassPluginConfigDTO.setParent(batchClassPluginConfigDTO); childBatchClassPluginConfigDTO.setChildren(null);
		 * children.add(childBatchClassPluginConfigDTO); }
		 * 
		 * batchClassPluginConfigDTO.setChildren(children); }
		 */

		return batchClassPluginConfigDTO;
	}

	public static BatchClassDynamicPluginConfigDTO createBatchClassDynamicPluginConfigDTO(BatchClassPluginDTO batchClassPluginDTO,
			BatchClassDynamicPluginConfig batchClassDynamicPluginConfig) {

		BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO = new BatchClassDynamicPluginConfigDTO();
		batchClassDynamicPluginConfigDTO.setBatchClassPlugin(batchClassPluginDTO);
		batchClassDynamicPluginConfigDTO.setDescription(batchClassDynamicPluginConfig.getDescription());
		batchClassDynamicPluginConfigDTO.setIdentifier(AdminConstants.EMPTY_STRING + batchClassDynamicPluginConfig.getId());
		batchClassDynamicPluginConfigDTO.setName(batchClassDynamicPluginConfig.getName());
		batchClassDynamicPluginConfigDTO.setValue(batchClassDynamicPluginConfig.getValue());

		// Setting the parent

		if (batchClassDynamicPluginConfig.getParent() != null) {
			BatchClassDynamicPluginConfig parentPluginConfig = batchClassDynamicPluginConfig.getParent();
			BatchClassDynamicPluginConfigDTO parent = setBatchClassDynamicPluginConfigDTO(batchClassPluginDTO, parentPluginConfig);
			parent.setParent(null);
			batchClassDynamicPluginConfigDTO.setParent(parent);
		}

		// setting the children

		if (batchClassDynamicPluginConfig.getChildren() != null) {
			List<BatchClassDynamicPluginConfigDTO> children = new ArrayList<BatchClassDynamicPluginConfigDTO>();
			for (BatchClassDynamicPluginConfig child : batchClassDynamicPluginConfig.getChildren()) {
				BatchClassDynamicPluginConfigDTO childBatchClassDynamicPluginConfigDTO = setBatchClassDynamicPluginConfigDTO(
						batchClassPluginDTO, child);
				childBatchClassDynamicPluginConfigDTO.setParent(batchClassDynamicPluginConfigDTO);
				childBatchClassDynamicPluginConfigDTO.setChildren(null);
				children.add(childBatchClassDynamicPluginConfigDTO);
			}

			batchClassDynamicPluginConfigDTO.setChildren(children);
		}

		return batchClassDynamicPluginConfigDTO;
	}

	public static BatchClassPluginConfigDTO setBatchClassPluginConfigDTO(BatchClassPluginDTO batchClassPluginDTO,
			BatchClassPluginConfig batchClassPluginConfig) {
		BatchClassPluginConfigDTO batchClassPluginConfigDTO = new BatchClassPluginConfigDTO();
		batchClassPluginConfigDTO.setBatchClassPlugin(batchClassPluginDTO);
		batchClassPluginConfigDTO.setDescription(batchClassPluginConfig.getDescription());
		batchClassPluginConfigDTO.setIdentifier(AdminConstants.EMPTY_STRING + batchClassPluginConfig.getId());
		batchClassPluginConfigDTO.setName(batchClassPluginConfig.getName());
		batchClassPluginConfigDTO.setSampleValue(batchClassPluginConfig.getSampleValue());
		batchClassPluginConfigDTO.setQualifier(batchClassPluginConfig.getQualifier());
		batchClassPluginConfigDTO.setDataType(batchClassPluginConfig.getPluginConfig().getDataType());
		PluginConfigurationDTO pluginConfigurationDTO = new PluginConfigurationDTO();

		if (batchClassPluginConfig.getPluginConfig() != null) {
			pluginConfigurationDTO.setFieldName(batchClassPluginConfig.getName());
			pluginConfigurationDTO.setFieldValue(batchClassPluginConfig.getValue());
			pluginConfigurationDTO.setIdentifier(AdminConstants.EMPTY_STRING + batchClassPluginConfig.getId());
			pluginConfigurationDTO.setSampleValue(batchClassPluginConfig.getSampleValue());
		}
		batchClassPluginConfigDTO.setPluginConfig(pluginConfigurationDTO);
		batchClassPluginConfigDTO.setValue(batchClassPluginConfig.getValue());
		return batchClassPluginConfigDTO;
	}

	public static BatchClassDynamicPluginConfigDTO setBatchClassDynamicPluginConfigDTO(BatchClassPluginDTO batchClassPluginDTO,
			BatchClassDynamicPluginConfig batchClassDynamicPluginConfig) {
		BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO = new BatchClassDynamicPluginConfigDTO();
		batchClassDynamicPluginConfigDTO.setBatchClassPlugin(batchClassPluginDTO);
		batchClassDynamicPluginConfigDTO.setDescription(batchClassDynamicPluginConfig.getDescription());
		batchClassDynamicPluginConfigDTO.setIdentifier(AdminConstants.EMPTY_STRING + batchClassDynamicPluginConfig.getId());
		batchClassDynamicPluginConfigDTO.setName(batchClassDynamicPluginConfig.getName());
		batchClassDynamicPluginConfigDTO.setValue(batchClassDynamicPluginConfig.getValue());
		return batchClassDynamicPluginConfigDTO;
	}

	public static void updateRevisionNumber(BatchClass batchClass) {
		if (null == batchClass) {
			return;
		}
		String version = batchClass.getVersion();
		if (null == version) {
			return;
		}
		String newVersion = version;
		if (version.contains(".")) {
			int lastIndex = version.lastIndexOf('.');
			String preFix = version.substring(0, lastIndex);
			String postFix = version.substring(lastIndex + 1);
			int revNumber = Integer.parseInt(postFix);
			revNumber++;
			newVersion = preFix + "." + revNumber;
		} else {
			int revNumber = Integer.parseInt(version);
			revNumber++;
			newVersion = String.valueOf(revNumber);
		}
		batchClass.setVersion(newVersion);
	}

	public static void copyModules(BatchClass batchClass) {
		List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
		List<BatchClassModule> newBatchClassModulesList = new ArrayList<BatchClassModule>();
		for (BatchClassModule batchClassModule : batchClassModules) {
			newBatchClassModulesList.add(batchClassModule);
			batchClassModule.setId(0);
			batchClassModule.setBatchClass(null);
			copyPlugins(batchClassModule);
			copyModuleConfig(batchClassModule);
		}
		batchClass.setBatchClassModules(newBatchClassModulesList);
	}

	public static void copyModuleConfig(BatchClassModule batchClassModule) {
		List<BatchClassModuleConfig> batchClassModConfigs = batchClassModule.getBatchClassModuleConfig();
		List<BatchClassModuleConfig> newBatchClassModuleConfigList = new ArrayList<BatchClassModuleConfig>();
		for (BatchClassModuleConfig batchClassModConfig : batchClassModConfigs) {
			newBatchClassModuleConfigList.add(batchClassModConfig);
			batchClassModConfig.setId(0);
			batchClassModConfig.setBatchClassModule(null);
		}
		batchClassModule.setBatchClassModuleConfig(newBatchClassModuleConfigList);
	}

	public static void copyPlugins(BatchClassModule batchClassModule) {
		List<BatchClassPlugin> batchClassPlugins = batchClassModule.getBatchClassPlugins();
		List<BatchClassPlugin> newBatchClassPluginsList = new ArrayList<BatchClassPlugin>();
		for (BatchClassPlugin batchClassPlugin : batchClassPlugins) {
			newBatchClassPluginsList.add(batchClassPlugin);
			batchClassPlugin.setId(0);
			batchClassPlugin.setBatchClassModule(null);
			copyPluginConfigs(batchClassPlugin);
			copyDynamicPluginConfigs(batchClassPlugin);
		}
		batchClassModule.setBatchClassPlugins(newBatchClassPluginsList);
	}

	public static void copyPluginConfigs(BatchClassPlugin batchClassPlugin) {
		List<BatchClassPluginConfig> batchClassPluginConfigs = batchClassPlugin.getBatchClassPluginConfigs();
		List<BatchClassPluginConfig> newBatchClassPluginConfigsList = new ArrayList<BatchClassPluginConfig>();
		for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
			newBatchClassPluginConfigsList.add(batchClassPluginConfig);
			batchClassPluginConfig.setId(0);
			batchClassPluginConfig.setBatchClassPlugin(null);
			// copyBatchClassPluginConfigsChild(batchClassPluginConfig);
			copyKVPageProcess(batchClassPluginConfig);
		}
		batchClassPlugin.setBatchClassPluginConfigs(newBatchClassPluginConfigsList);
	}

	public static void copyDynamicPluginConfigs(BatchClassPlugin batchClassPlugin) {
		List<BatchClassDynamicPluginConfig> batchClassDynamicPluginConfigs = batchClassPlugin.getBatchClassDynamicPluginConfigs();
		List<BatchClassDynamicPluginConfig> newBatchClassDynamicPluginConfigsList = new ArrayList<BatchClassDynamicPluginConfig>();
		for (BatchClassDynamicPluginConfig batchClassDynamicPluginConfig : batchClassDynamicPluginConfigs) {
			newBatchClassDynamicPluginConfigsList.add(batchClassDynamicPluginConfig);
			batchClassDynamicPluginConfig.setId(0);
			batchClassDynamicPluginConfig.setBatchClassPlugin(null);
			copyBatchClassDynamicPluginConfigsChild(batchClassDynamicPluginConfig);
		}
		batchClassPlugin.setBatchClassDynamicPluginConfigs(newBatchClassDynamicPluginConfigsList);
	}

	private static void copyKVPageProcess(BatchClassPluginConfig batchClassPluginConfig) {
		List<KVPageProcess> kvPageProcess = batchClassPluginConfig.getKvPageProcesses();
		List<KVPageProcess> newKvPageProcessList = new ArrayList<KVPageProcess>();
		for (KVPageProcess kVPageProcessChild : kvPageProcess) {
			kVPageProcessChild.setId(0);
			newKvPageProcessList.add(kVPageProcessChild);
			kVPageProcessChild.setBatchClassPluginConfig(null);
		}
		batchClassPluginConfig.setKvPageProcesses(newKvPageProcessList);
	}

	/*private static void copyBatchClassPluginConfigsChild(BatchClassPluginConfig batchClassPluginConfig) {
		List<BatchClassPluginConfig> children = batchClassPluginConfig.getChildren();
		List<BatchClassPluginConfig> newChildrenList = new ArrayList<BatchClassPluginConfig>();
		for (BatchClassPluginConfig child : children) {
			child.setId(0);
			newChildrenList.add(child);
			child.setBatchClassPlugin(null);
			child.setParent(null);
		}
		batchClassPluginConfig.setChildren(newChildrenList);
	}*/

	private static void copyBatchClassDynamicPluginConfigsChild(BatchClassDynamicPluginConfig batchClassDynamicPluginConfig) {
		List<BatchClassDynamicPluginConfig> children = batchClassDynamicPluginConfig.getChildren();
		List<BatchClassDynamicPluginConfig> newChildrenList = new ArrayList<BatchClassDynamicPluginConfig>();
		for (BatchClassDynamicPluginConfig child : children) {
			child.setId(0);
			newChildrenList.add(child);
			child.setBatchClassPlugin(null);
			child.setParent(null);
		}
		batchClassDynamicPluginConfig.setChildren(newChildrenList);
	}

	public static void copyDocumentTypes(BatchClass batchClass) {
		List<DocumentType> documentTypes = batchClass.getDocumentTypes();
		List<DocumentType> newDocumentType = new ArrayList<DocumentType>();
		for (DocumentType documentType : documentTypes) {
			newDocumentType.add(documentType);
			documentType.setId(0);
			documentType.setBatchClass(null);
			documentType.setIdentifier(null);
			copyPageTypes(documentType);
			copyFieldTypes(documentType);
			copyTableInfo(documentType);
			copyFunctionKeys(documentType);
		}
		batchClass.setDocumentTypes(newDocumentType);
	}

	public static void copyFunctionKeys(DocumentType documentType) {
		List<FunctionKey> functionKeys = documentType.getFunctionKeys();
		List<FunctionKey> newFunctionKeys = new ArrayList<FunctionKey>();
		for (FunctionKey functionKey : functionKeys) {
			newFunctionKeys.add(functionKey);
		}
		documentType.setFunctionKeys(newFunctionKeys);

	}

	public static void copyBatchClassField(BatchClass batchClass) {
		List<BatchClassField> batchClassField = batchClass.getBatchClassField();
		List<BatchClassField> newBatchClassField = new ArrayList<BatchClassField>();
		for (BatchClassField batchClassFieldTemp : batchClassField) {
			newBatchClassField.add(batchClassFieldTemp);
			batchClassFieldTemp.setId(0);
			batchClassFieldTemp.setBatchClass(null);
			batchClassFieldTemp.setIdentifier(null);
		}
		batchClass.setBatchClassField(newBatchClassField);
	}

	public static void copyTableInfo(DocumentType documentType) {
		List<TableInfo> tableInfos = documentType.getTableInfos();
		List<TableInfo> newTableInfo = new ArrayList<TableInfo>();
		for (TableInfo tableInfo : tableInfos) {
			newTableInfo.add(tableInfo);
			tableInfo.setId(0);
			tableInfo.setDocType(null);
			copyTableColumnsInfo(tableInfo);
		}
		documentType.setTableInfos(newTableInfo);
	}

	public static void copyTableColumnsInfo(TableInfo tableInfo) {
		List<TableColumnsInfo> tableColumnsInfos = tableInfo.getTableColumnsInfo();
		List<TableColumnsInfo> newTableColumnsInfo = new ArrayList<TableColumnsInfo>();
		for (TableColumnsInfo tableColumnsInfo : tableColumnsInfos) {
			newTableColumnsInfo.add(tableColumnsInfo);
			tableColumnsInfo.setId(0);
		}
		tableInfo.setTableColumnsInfo(newTableColumnsInfo);
	}

	public static void copyFieldTypes(DocumentType documentType) {
		List<FieldType> fieldTypes = documentType.getFieldTypes();
		List<FieldType> newFieldType = new ArrayList<FieldType>();
		for (FieldType fieldType : fieldTypes) {
			newFieldType.add(fieldType);
			fieldType.setId(0);
			fieldType.setDocType(null);
			fieldType.setIdentifier(null);
			copyKVExtractionFields(fieldType);
			copyRegex(fieldType);
		}
		documentType.setFieldTypes(newFieldType);
	}

	public static void copyKVExtractionFields(FieldType fieldType) {
		List<KVExtraction> kvExtraction2 = fieldType.getKvExtraction();
		List<KVExtraction> newKvExtraction = new ArrayList<KVExtraction>();
		for (KVExtraction kvExtraction : kvExtraction2) {
			newKvExtraction.add(kvExtraction);
			kvExtraction.setId(0);
			kvExtraction.setFieldType(null);
		}
		fieldType.setKvExtraction(newKvExtraction);
	}

	public static void copyRegex(FieldType fieldType) {
		List<RegexValidation> regexValidations = fieldType.getRegexValidation();
		List<RegexValidation> regexValidations2 = new ArrayList<RegexValidation>();
		for (RegexValidation regexValidation : regexValidations) {
			regexValidations2.add(regexValidation);
			regexValidation.setId(0);
			regexValidation.setFieldType(null);
		}
		fieldType.setRegexValidation(regexValidations2);
	}

	public static void copyPageTypes(DocumentType documentType) {
		List<PageType> pages = documentType.getPages();
		List<PageType> newPageTypes = new ArrayList<PageType>();
		for (PageType pageType : pages) {
			newPageTypes.add(pageType);
			pageType.setId(0);
			pageType.setDocType(null);
			pageType.setIdentifier(null);
		}
		documentType.setPages(newPageTypes);
	}

	public static void changePageTypeForNewDocument(DocumentType documentType) {
		List<PageType> pages = documentType.getPages();
		List<PageType> newPageTypes = new ArrayList<PageType>();
		int iVar = 0;
		for (PageType pageType : pages) {
			pageType.setId(0);
			pageType.setDocType(null);
			pageType.setIdentifier(null);
			newPageTypes.add(pageType);
			switch (iVar) {
				case 0:
					pageType.setName(documentType.getName() + FIRST_PAGE);
					pageType.setDescription(documentType.getName() + FIRST_PAGE);
					break;
				case 1:
					pageType.setName(documentType.getName() + MIDDLE_PAGE);
					pageType.setDescription(documentType.getName() + MIDDLE_PAGE);
					break;
				case 2:
					pageType.setName(documentType.getName() + LAST_PAGE);
					pageType.setDescription(documentType.getName() + LAST_PAGE);
					break;
				default:
					break;
			}
			iVar++;
		}
		documentType.setPages(newPageTypes);
	}

	public static void mergeKvPageProcessFromDTO(KVPageProcess kvPageProcess, KVPageProcessDTO kvPageProcessDTO) {
		kvPageProcess.setKeyPattern(kvPageProcessDTO.getKeyPattern());
		kvPageProcess.setLocationType(kvPageProcessDTO.getLocationType());
		kvPageProcess.setValuePattern(kvPageProcessDTO.getValuePattern());
		kvPageProcess.setNoOfWords(kvPageProcessDTO.getNoOfWords());
		kvPageProcess.setPageLevelFieldName(kvPageProcessDTO.getPageLevelFieldName());
	}

	public static InputDataCarrier createInputDataCarrierFromKVExtDTO(KVExtractionDTO kvExtractionDTO) {
		InputDataCarrier inputDataCarrier = new InputDataCarrier(kvExtractionDTO.getLocationType(), kvExtractionDTO.getKeyPattern(),
				kvExtractionDTO.getValuePattern(), kvExtractionDTO.getNoOfWords(), kvExtractionDTO.getMultiplier(), kvExtractionDTO
						.getFetchValue(), kvExtractionDTO.getLength(), kvExtractionDTO.getWidth(), kvExtractionDTO.getXoffset(),
				kvExtractionDTO.getYoffset());
		return inputDataCarrier;
	}

	public static List<OutputDataCarrierDTO> createOutputDataDTOFromOutputDataCarrier(List<OutputDataCarrier> outputDataCarriers,
			List<OutputDataCarrierDTO> carrierDtos, String inputFileName) {
		if (outputDataCarriers != null && !outputDataCarriers.isEmpty()) {
			for (OutputDataCarrier outputDataCarrier : outputDataCarriers) {
				Coordinates hocr = outputDataCarrier.getSpan().getCoordinates();
				CoordinatesDTO coordinates = null;
				if (hocr != null) {
					coordinates = new CoordinatesDTO();
					coordinates.setX0(hocr.getX0());
					coordinates.setX1(hocr.getX1());
					coordinates.setY0(hocr.getY0());
					coordinates.setY1(hocr.getY1());
				}
				OutputDataCarrierDTO dto = new OutputDataCarrierDTO();
				dto.setConfidence(outputDataCarrier.getConfidence());
				dto.setCoordinates(coordinates);
				dto.setValue(outputDataCarrier.getValue());
				dto.setInputFileName(inputFileName);
				carrierDtos.add(dto);
			}
		}
		return carrierDtos;
	}

	public static RoleDTO createRoleDTO(BatchClassGroups role) {
		RoleDTO roleDTO = new RoleDTO();
		roleDTO.setName(role.getGroupName());
		return roleDTO;
	}

	public static void exportEmailConfiguration(BatchClass batchClass) {
		List<BatchClassEmailConfiguration> batchClassEmailConfigurations = batchClass.getEmailConfigurations();
		List<BatchClassEmailConfiguration> newBatchClassEmailConfigurationList = new ArrayList<BatchClassEmailConfiguration>();
		for (BatchClassEmailConfiguration batchClassEmailConfiguration : batchClassEmailConfigurations) {
			newBatchClassEmailConfigurationList.add(batchClassEmailConfiguration);
			batchClassEmailConfiguration.setId(0);
			batchClassEmailConfiguration.setBatchClass(null);
		}
		batchClass.setEmailConfigurations(newBatchClassEmailConfigurationList);
	}

	public static void exportBatchClassField(BatchClass batchClass) {
		List<BatchClassField> batchClassField = batchClass.getBatchClassField();
		List<BatchClassField> newBatchClassFieldList = new ArrayList<BatchClassField>();
		for (BatchClassField batchClassFields : batchClassField) {
			newBatchClassFieldList.add(batchClassFields);
			batchClassFields.setId(0);
			batchClassFields.setBatchClass(null);
		}
		batchClass.setBatchClassField(newBatchClassFieldList);
	}

	public static void exportUserGroups(BatchClass batchClass) {
		List<BatchClassGroups> batchClassGroups = batchClass.getAssignedGroups();
		List<BatchClassGroups> newBatchClassGroupsList = new ArrayList<BatchClassGroups>();
		for (BatchClassGroups batchClassGroup : batchClassGroups) {
			newBatchClassGroupsList.add(batchClassGroup);
			batchClassGroup.setId(0);
			batchClassGroup.setBatchClass(null);
		}
		batchClass.setAssignedGroups(newBatchClassGroupsList);
	}

	public static boolean matchBaseFolder(String uncFolderPath, String baseFolderPath) {
		uncFolderPath = uncFolderPath.replace("\\", "/");
		uncFolderPath = uncFolderPath.replace("\\\\", "/");
		uncFolderPath = uncFolderPath.replace("//", "/");

		baseFolderPath = baseFolderPath.replace("\\", "/");
		baseFolderPath = baseFolderPath.replace("\\\\", "/");
		baseFolderPath = baseFolderPath.replace("//", "/");

		String[] uncFolderPathToken = uncFolderPath.split("\\/");
		String[] baseFolderPathToken = baseFolderPath.split("\\/");

		boolean isUncPathStartsWithBaseFolder = true;
		for (int index = 0; index < baseFolderPathToken.length; index++) {
			String userVal = uncFolderPathToken[index];
			String baseVal = baseFolderPathToken[index];
			if (!userVal.equals(baseVal)) {
				isUncPathStartsWithBaseFolder = false;
				break;
			}
		}
		return isUncPathStartsWithBaseFolder;
	}

	/**
	 * Utility Method to synchronize the serializable file Batch class with the current System Batch Class. In case the exported batch
	 * class contains any additional plugins,modules,plugin Config or plugin config sample values, they are imported as well.
	 * 
	 * @param dbBatchClass
	 * @param moduleConfigService
	 * @param moduleService
	 * @param pluginService
	 * @param pluginConfigService
	 * @param batchClassService
	 * @param batchSchemaService
	 * @param serializedBatchClass
	 */
	public static void updateSerializableBatchClass(BatchClass dbBatchClass, ModuleConfigService moduleConfigService,
			ModuleService moduleService, PluginService pluginService, PluginConfigService pluginConfigService,
			BatchClassService batchClassService, BatchSchemaService batchSchemaService, BatchClass serializedBatchClass) {
		// fix for "Not Mapped yet." issue while importing old batch classes.
		List<DocumentType> documentTypes = serializedBatchClass.getDocumentTypes();
		if (documentTypes != null) {
			for (DocumentType documentType : documentTypes) {
				if (documentType.getRspProjectFileName() != null
						&& documentType.getRspProjectFileName().equalsIgnoreCase("Not Mapped yet.")) {
					documentType.setRspProjectFileName(null);
				}
			}
		}

		List<BatchClassModule> batchClassModules = serializedBatchClass.getBatchClassModules();
		for (BatchClassModule batchClassModule : batchClassModules) {
			Module currentModule = moduleService.getModuleByName(batchClassModule.getModule().getName());
			if (currentModule == null) {
				continue;
			} else {
				batchClassModule.setModule(currentModule);
			}
			List<BatchClassModuleConfig> batchClassModConfigs = batchClassModule.getBatchClassModuleConfig();
			boolean isClearBatchClassModuleConfig = false;
			if (batchClassModConfigs != null) {
				for (BatchClassModuleConfig batchClassModConfig : batchClassModConfigs) {
					if (batchClassModConfig != null) {
						if (batchClassModConfig.getModuleConfig() != null) {
							ModuleConfig currentModuleConfig = moduleConfigService.getModuleConfigByKeyAndMandatory(
									batchClassModConfig.getModuleConfig().getChildKey(), batchClassModConfig.getModuleConfig()
											.isMandatory());
							if (currentModuleConfig == null) {
								continue;
							} else {
								batchClassModConfig.setModuleConfig(currentModuleConfig);
							}
						} else if (!isClearBatchClassModuleConfig) {
							isClearBatchClassModuleConfig = true;
						}
					}
				}

				// backward compatibility support for 2.2 release exported batch classes
				if (isClearBatchClassModuleConfig) {
					batchClassModule.getBatchClassModuleConfig().clear();
					batchClassModule.setBatchClassModuleConfig(new ArrayList<BatchClassModuleConfig>());
				}
			}

			List<BatchClassPlugin> batchClassPlugins = batchClassModule.getBatchClassPlugins();
			for (BatchClassPlugin batchClassPlugin : batchClassPlugins) {
				Plugin currentPlugin = pluginService.getPluginPropertiesForPluginName(batchClassPlugin.getPlugin().getPluginName());
				if (currentPlugin == null) {
					continue;
				} else {
					batchClassPlugin.setPlugin(currentPlugin);
				}
				List<BatchClassPluginConfig> batchClassPluginConfigs = batchClassPlugin.getBatchClassPluginConfigs();
				Iterator<BatchClassPluginConfig> iter = batchClassPluginConfigs.iterator();
				while (iter.hasNext()) {
					BatchClassPluginConfig batchClassPluginConfig = iter.next();
					PluginConfig currentPluginConfig = pluginConfigService.getPluginConfigByName(batchClassPluginConfig
							.getPluginConfig().getName());

					if (currentPluginConfig == null) {
						continue;
					} else {
						batchClassPluginConfig.setPluginConfig(currentPluginConfig);
					}
				}
			}
		}
		updateBatchClassModules(pluginConfigService, batchSchemaService, dbBatchClass, batchClassModules);
	}

	/**
	 * This method synchronizes the zip batch class modules with the system batch class modules.
	 * 
	 * @param pluginConfigService
	 * @param batchSchemaService
	 * @param dbBatchClass
	 * @param zipBatchClassModules
	 */
	private static void updateBatchClassModules(PluginConfigService pluginConfigService, BatchSchemaService batchSchemaService,
			BatchClass dbBatchClass, List<BatchClassModule> zipBatchClassModules) {
		List<BatchClassModule> dbBatchClassModules = dbBatchClass.getBatchClassModules();
		List<BatchClassModule> validBatchClassModules = new ArrayList<BatchClassModule>();
		if (dbBatchClassModules != null) {
			BatchClassModule bcModule = null;
			for (BatchClassModule dbBatchClassModule : dbBatchClassModules) {
				boolean isPresent = false;
				for (BatchClassModule zipBatchClassModule : zipBatchClassModules) {
					if (zipBatchClassModule.getModule().getName().equalsIgnoreCase(dbBatchClassModule.getModule().getName())) {
						isPresent = true;
						validBatchClassModules.add(zipBatchClassModule);
						bcModule = zipBatchClassModule;
						break;
					}
				}
				if (isPresent) {
					updateBatchClassPlugins(pluginConfigService, batchSchemaService.getDetachedBatchClassModuleByName(dbBatchClass
							.getIdentifier(), bcModule.getModule().getName()), bcModule.getBatchClassPlugins());
				} else {
					validBatchClassModules.add(batchSchemaService.getDetachedBatchClassModuleByName(dbBatchClass.getIdentifier(),
							dbBatchClassModule.getModule().getName()));
				}
			}
		}
		if (validBatchClassModules != null) {
			zipBatchClassModules.clear();
			zipBatchClassModules.addAll(validBatchClassModules);
		}
	}

	/**
	 * This method synchronizes the zip batch class plugins of specific batch class module with batch class plugins of corresponding
	 * batch class module of system batch class.
	 * 
	 * @param pluginConfigService
	 * @param dbBatchClassModule
	 * @param zipBatchClassPlugins
	 */
	private static void updateBatchClassPlugins(PluginConfigService pluginConfigService, BatchClassModule dbBatchClassModule,
			List<BatchClassPlugin> zipBatchClassPlugins) {
		List<BatchClassPlugin> dbBatchClassPlugins = dbBatchClassModule.getBatchClassPlugins();
		List<BatchClassPlugin> validBatchClassPlugins = new ArrayList<BatchClassPlugin>();
		if (dbBatchClassPlugins != null) {
			BatchClassPlugin bcPlugin = null;
			for (BatchClassPlugin dbBatchClassPlugin : dbBatchClassPlugins) {
				boolean isPresent = false;
				for (BatchClassPlugin zipBatchClassPlugin : zipBatchClassPlugins) {
					if (zipBatchClassPlugin.getPlugin().getPluginName().equalsIgnoreCase(
							dbBatchClassPlugin.getPlugin().getPluginName())) {
						isPresent = true;
						validBatchClassPlugins.add(zipBatchClassPlugin);
						bcPlugin = zipBatchClassPlugin;
						break;
					}
				}
				if (isPresent) {
					updateBatchClassPluginConfigs(pluginConfigService, dbBatchClassPlugin, bcPlugin.getBatchClassPluginConfigs());
				} else {
					validBatchClassPlugins.add(dbBatchClassPlugin);
				}
			}
		}
		if (validBatchClassPlugins != null) {
			zipBatchClassPlugins.clear();
			zipBatchClassPlugins.addAll(validBatchClassPlugins);
		}
	}

	/**
	 * This method synchronizes the zip batch class plugin configs of specific batch class plugin with the corresponding batch class
	 * plugin config in system batch class.
	 * 
	 * @param pluginConfigService
	 * @param dbBatchClassPlugin
	 * @param zipBatchClassPluginConfigs
	 */
	private static void updateBatchClassPluginConfigs(PluginConfigService pluginConfigService, BatchClassPlugin dbBatchClassPlugin,
			List<BatchClassPluginConfig> zipBatchClassPluginConfigs) {
		if (dbBatchClassPlugin != null) {
			List<BatchClassPluginConfig> dbBatchClassPluginConfigs = dbBatchClassPlugin.getBatchClassPluginConfigs();
			List<BatchClassPluginConfig> validBCPluginConfigs = new ArrayList<BatchClassPluginConfig>();
			BatchClassPluginConfig bcPluginConfig = null;
			if (dbBatchClassPluginConfigs != null) {
				for (BatchClassPluginConfig dbBatchClassPluginConfig : dbBatchClassPluginConfigs) {
					boolean isPresent = false;
					for (BatchClassPluginConfig zipBatchClassPluginConfig : zipBatchClassPluginConfigs) {
						if (zipBatchClassPluginConfig.getPluginConfig().getName().equalsIgnoreCase(
								dbBatchClassPluginConfig.getPluginConfig().getName())) {
							isPresent = true;
							bcPluginConfig = zipBatchClassPluginConfig;
							validBCPluginConfigs.add(zipBatchClassPluginConfig);
							break;
						}
					}
					if (isPresent) {
						// update batch class plugin config value.
						updateBatchClassPluginConfigValue(pluginConfigService, bcPluginConfig, dbBatchClassPluginConfig);
					} else {
						// add new batch class plugin config to imported batch class.
						validBCPluginConfigs.add(dbBatchClassPluginConfig);
					}
				}
			}
			if (validBCPluginConfigs != null) {
				zipBatchClassPluginConfigs.clear();
				zipBatchClassPluginConfigs.addAll(validBCPluginConfigs);
			}
		}
	}

	/**
	 * This method sychronizes the zip batch class plugin config value with the value in plugin config sample value.
	 * 
	 * @param pluginConfigService
	 * @param zipBatchClassPluginConfig
	 * @param dbBatchClassPluginConfig
	 */
	private static void updateBatchClassPluginConfigValue(PluginConfigService pluginConfigService,
			BatchClassPluginConfig zipBatchClassPluginConfig, BatchClassPluginConfig dbBatchClassPluginConfig) {
		PluginConfig pluginConfig = dbBatchClassPluginConfig.getPluginConfig();
		if (pluginConfig != null) {
			pluginConfig = pluginConfigService.getPluginConfigByName(pluginConfig.getName());
			// get sample values for that plugin config.
			List<String> pcSampleValue = pluginConfig.getSampleValue();
			// get batch class plugin config value in zip.
			String bcpcValue = zipBatchClassPluginConfig.getValue();
			// Check if batch class plugin config value is valid or not.
			if (pcSampleValue != null && !pcSampleValue.isEmpty() && !isBcpcValueExistsInDB(pcSampleValue, bcpcValue)) {
				// update imported batch class plugin value with plugin config value for system batch class.
				zipBatchClassPluginConfig.setValue(dbBatchClassPluginConfig.getValue());
			}
		}
	}

	/**
	 * This method returns true or false depending on whether zip batch class plugin config value exists in its sample values in
	 * database.
	 * 
	 * @param dbBatchClassPluginConfig
	 * @param zipBcpcValue
	 * @return
	 */
	private static boolean isBcpcValueExistsInDB(List<String> dbBatchClassPluginConfig, String zipBcpcValue) {
		boolean isPresent = false;
		if (zipBcpcValue != null) {
			String[] valueArray = zipBcpcValue.split(SEMI_COLON);
			for (int index = 0; index < valueArray.length; index++) {
				String eachValue = valueArray[index];
				for (String sampleValue : dbBatchClassPluginConfig) {
					// Check if BCPC value exists in database.
					if (sampleValue.equalsIgnoreCase(eachValue)) {
						isPresent = true;
						break;
					}
				}
				if (!isPresent) {
					// Value not present in sample values. Return false.
					break;
				} else if (index == valueArray.length - 1) {
					// Reached end of array. Each BCPC value present. Return true.
					break;
				} else {
					// BCPC value present. Continue with next value in array.
					isPresent = false;
				}
			}
		}
		return isPresent;
	}

	public static List<TableInfo> mapTableInputFromDTO(TableInfoDTO tableInfoDTO) {
		List<TableInfo> tableInfoList = new ArrayList<TableInfo>();

		TableInfo tableInfo = new TableInfo();
		tableInfo.setEndPattern(tableInfoDTO.getEndPattern());
		tableInfo.setStartPattern(tableInfoDTO.getStartPattern());
		tableInfo.setName(tableInfoDTO.getName());

		for (TableColumnInfoDTO tableColumnsInfo : tableInfoDTO.getColumnInfoDTOs()) {
			TableColumnsInfo columnInfo = mapTableColumnFromDTO(tableInfoDTO, tableColumnsInfo);
			tableInfo.addTableColumnInfo(columnInfo);
		}
		tableInfoList.add(tableInfo);
		return tableInfoList;

	}

	public static TableColumnsInfo mapTableColumnFromDTO(TableInfoDTO tableInfoDTO, TableColumnInfoDTO tableColumnsInfo) {
		TableColumnsInfo columnInfo = new TableColumnsInfo();
		columnInfo.setBetweenLeft(tableColumnsInfo.getBetweenLeft());
		columnInfo.setBetweenRight(tableColumnsInfo.getBetweenRight());
		columnInfo.setColumnName(tableColumnsInfo.getColumnName());
		columnInfo.setColumnPattern(tableColumnsInfo.getColumnPattern());
		columnInfo.setRequired(tableColumnsInfo.isRequired());
		return columnInfo;
	}

	public static void mapTestTableResultsToDTO(List<TestTableResultDTO> results, DataTable dataTable, String inputFileName) {
		if (results != null && dataTable != null) {
			TestTableResultDTO dto = new TestTableResultDTO();
			dto.setDataTable(dataTable);
			dto.setInputFileName(inputFileName);
			results.add(dto);
		}
	}

	public static void populateUICOnfig(BatchClass importedBatchClass, Node uiConfig, BatchClassService batchClassService,
			String workflowName) {
		Label rootLabel = new Label("root", "root", false);
		uiConfig.setLabel(rootLabel);
		List<BatchClassModule> moduleList = importedBatchClass.getBatchClassModules();
		if (!(moduleList == null || moduleList.isEmpty())) {
			Node node = new Node();
			Label lRole = node.getLabel();
			lRole.setDisplayName(ROLES);
			lRole.setKey(BatchClassManagementServiceImpl.ROLES);
			lRole.setMandatory(true);
			uiConfig.getChildren().add(node);
			node.setParent(uiConfig);

			Node nodeBatchClassDef = new Node();
			Label lDef = nodeBatchClassDef.getLabel();
			lDef.setDisplayName("Batch Class Definition");
			lDef.setKey(BatchClassManagementServiceImpl.BATCH_CLASS_DEF);
			lDef.setMandatory(true);
			uiConfig.getChildren().add(nodeBatchClassDef);
			nodeBatchClassDef.setParent(uiConfig);

			for (BatchClassModule module : moduleList) {
				Node branch = new Node();
				List<BatchClassModuleConfig> dbConfigs = module.getBatchClassModuleConfig();
				BatchClass dbBatchClass = batchClassService.getBatchClassByNameIncludingDeleted(workflowName);
				module = dbBatchClass.getBatchClassModuleByName(module.getModule().getName());
				List<BatchClassModuleConfig> dbBatchClassConfigs = module.getBatchClassModuleConfig();
				if (module != null) {
					if (dbConfigs == null || dbConfigs.isEmpty()) {
						dbConfigs = dbBatchClassConfigs;
					}
					for (BatchClassModuleConfig dbConfig : dbConfigs) {
						if (dbConfig.getModuleConfig() == null) {
							dbConfigs = dbBatchClassConfigs;
							break;
						}
					}
				}

				if (!dbConfigs.isEmpty()) {
					for (BatchClassModuleConfig dbConfig : dbConfigs) {
						ModuleConfig moduleConfig = dbConfig.getModuleConfig();
						if (moduleConfig != null && (moduleConfig.getChildKey() == null || moduleConfig.getChildKey().isEmpty())) {
							Label labelBranch = branch.getLabel();
							labelBranch.setDisplayName(module.getModule().getDescription());
							labelBranch.setKey(module.getModule().getName());
							labelBranch.setMandatory(moduleConfig.isMandatory());
							branch.setLabel(labelBranch);
						} else {
							Node leaf = new Node();
							Label leafLabel = leaf.getLabel();
							leafLabel.setDisplayName(moduleConfig.getChildDisplayName());
							leafLabel.setKey(moduleConfig.getChildKey());
							leafLabel.setMandatory(moduleConfig.isMandatory());
							leaf.setLabel(leafLabel);
							leaf.setParent(branch);
							branch.getChildren().add(leaf);
						}
					}
					branch.setParent(nodeBatchClassDef);
					nodeBatchClassDef.getChildren().add(branch);
				}
			}
		}
	}

	public static boolean isChecked(Node node) {
		boolean checked = false;
		if (node.getLabel().isMandatory()) {
			checked = true;
		}
		return checked;
	}

	public static void updateRoles(BatchClass importBatchClass, BatchClass existingBatchClass, Boolean isFromDB) {
		List<BatchClassGroups> batchClassGroups = new ArrayList<BatchClassGroups>();
		if (isFromDB) {
			batchClassGroups = existingBatchClass.getAssignedGroups();
		} else {
			batchClassGroups = importBatchClass.getAssignedGroups();
		}
		List<BatchClassGroups> newBatchClassGroupsList = new ArrayList<BatchClassGroups>();
		for (BatchClassGroups batchClassGroup : batchClassGroups) {
			newBatchClassGroupsList.add(batchClassGroup);
			batchClassGroup.setId(0);
			batchClassGroup.setBatchClass(null);
		}
		if (isFromDB) {
			importBatchClass.getAssignedGroups().clear();
			importBatchClass.setAssignedGroups(newBatchClassGroupsList);
		} else {
			existingBatchClass.getAssignedGroups().clear();
			existingBatchClass.setAssignedGroups(newBatchClassGroupsList);
		}
	}

	public static void updateEmailConfigurations(BatchClass importBatchClass, BatchClass existingBatchClass, Boolean isFromDB) {
		List<BatchClassEmailConfiguration> batchClassEmailConfigurations = new ArrayList<BatchClassEmailConfiguration>();
		if (isFromDB) {
			batchClassEmailConfigurations = existingBatchClass.getEmailConfigurations();
		} else {
			batchClassEmailConfigurations = importBatchClass.getEmailConfigurations();
		}

		List<BatchClassEmailConfiguration> newBatchClassEmailConfigurationList = new ArrayList<BatchClassEmailConfiguration>();
		for (BatchClassEmailConfiguration batchClassEmailConfiguration : batchClassEmailConfigurations) {
			newBatchClassEmailConfigurationList.add(batchClassEmailConfiguration);
			batchClassEmailConfiguration.setId(0);
			batchClassEmailConfiguration.setBatchClass(null);
		}
		if (isFromDB) {
			importBatchClass.getEmailConfigurations().clear();
			importBatchClass.setEmailConfigurations(newBatchClassEmailConfigurationList);
		} else {
			existingBatchClass.getEmailConfigurations().clear();
			existingBatchClass.setEmailConfigurations(newBatchClassEmailConfigurationList);
		}
	}

	public static boolean overrideFromDB(String existingFolderName, String zipFolderName, String zipDir) {
		boolean isSuccess = true;
		// import the samples from database
		if (zipFolderName.isEmpty()) {
			// create a folder in the directory of zip file
			zipFolderName = new File(zipFolderName).getAbsolutePath();
		} else {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(zipFolderName), false);
		}
		if (zipFolderName != null && !zipFolderName.isEmpty()) {
			if (existingFolderName != null && !existingFolderName.isEmpty()) {
				boolean copiedSuccessfully = true;
				try {
					FileUtils.copyDirectoryWithContents(existingFolderName, zipFolderName);
				} catch (IOException ioe) {
					LOGGER.info("Unable to override folder" + existingFolderName + " for batch class." + ioe, ioe);
					copiedSuccessfully = false;
				}
				if (!copiedSuccessfully) {
					isSuccess = false;
				}
			}
		}
		return isSuccess;
	}

	public static void updateScriptsFiles(String tempOutputUnZipDir, BatchSchemaService batchSchemaService, List<String> scriptsList,
			Node node, String moduleName, BatchClass importBatchClass, BatchClass existingBatchClass) throws Exception {
		String scriptFileName = node.getLabel().getKey();
		String existingScriptFilePath = EMPTY_STRING;
		String zipScriptFilePath = EMPTY_STRING;
		String scriptsFolderName = batchSchemaService.getScriptFolderName();
		BatchClassModule existingBatchClassModule = batchSchemaService.getDetachedBatchClassModuleByName(existingBatchClass
				.getIdentifier(), moduleName);
		BatchClassModule importBatchClassModule = importBatchClass.getBatchClassModuleByName(moduleName);

		List<BatchClassModuleConfig> existingModConfigs = existingBatchClassModule.getBatchClassModuleConfig();
		if (existingModConfigs != null && !existingModConfigs.isEmpty()) {
			for (BatchClassModuleConfig existingBatchClassModuleConfig : existingModConfigs) {
				ModuleConfig existingModConfig = existingBatchClassModuleConfig.getModuleConfig();
				if (existingModConfig != null && existingModConfig.getChildKey() != null
						&& existingModConfig.getChildKey().equalsIgnoreCase(scriptFileName)) {
					existingScriptFilePath = FileUtils.getFileNameOfTypeFromFolder(batchSchemaService.getAbsolutePath(
							existingBatchClass.getIdentifier(), scriptsFolderName, false), scriptFileName);
					break;
				}
			}
		} else {
			existingScriptFilePath = FileUtils.getFileNameOfTypeFromFolder(batchSchemaService.getAbsolutePath(existingBatchClass
					.getIdentifier(), scriptsFolderName, false), scriptFileName);
		}

		List<BatchClassModuleConfig> importModConfigs = importBatchClassModule.getBatchClassModuleConfig();
		if (importModConfigs != null && !importModConfigs.isEmpty()) {
			for (BatchClassModuleConfig importBatchClassModConfig : importModConfigs) {
				ModuleConfig importModConfig = importBatchClassModConfig.getModuleConfig();
				if (importModConfig != null && importModConfig.getChildKey() != null
						&& importModConfig.getChildKey().equalsIgnoreCase(scriptFileName)) {
					zipScriptFilePath = FileUtils.getFileNameOfTypeFromFolder(tempOutputUnZipDir + File.separator
							+ scriptsFolderName, scriptFileName);
					break;
				}
			}
		} else {
			zipScriptFilePath = FileUtils.getFileNameOfTypeFromFolder(tempOutputUnZipDir + File.separator
					+ scriptsFolderName, scriptFileName);
		}

		File zipScriptFile = null;
		File existingScriptFile = null;
		if (zipScriptFilePath != null && !zipScriptFilePath.isEmpty()) {
			zipScriptFile = new File(zipScriptFilePath);
		}
		if (!isChecked(node)) {
			// import the script for the module from database

			if (zipScriptFile != null && zipScriptFile.exists()) {
				zipScriptFile.delete();
			}
			if (existingScriptFilePath != null && !existingScriptFilePath.isEmpty()) {
				existingScriptFile = new File(existingScriptFilePath);
			}
			if (existingScriptFile != null && existingScriptFile.exists()) {
				if (zipScriptFile == null) {
					zipScriptFile = new File(tempOutputUnZipDir + File.separator + scriptsFolderName
							+ File.separator + existingScriptFile.getName());
				}
				FileUtils.copyFile(existingScriptFile, zipScriptFile);
				scriptsList.add(zipScriptFile.getName());
			}
		} else {
			if (zipScriptFile != null) {
				scriptsList.add(zipScriptFile.getName());
			}
		}
	}
}
