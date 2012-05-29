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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.ephesoft.dcma.barcodeextraction.BarcodeExtractionReader.BarcodeReaderTypes;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPlugin;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPluginConfiguration;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.Folders;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.Scripts;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.Folders.Folder;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.Scripts.Script;
import com.ephesoft.dcma.batch.service.BatchClassPluginPropertiesService;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.ImportBatchService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.ModuleJpdlPluginCreationInfo;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.common.PluginProperty;
import com.ephesoft.dcma.core.exception.BatchAlreadyLockedException;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.hibernate.DynamicHibernateDao;
import com.ephesoft.dcma.core.hibernate.DynamicHibernateDao.ColumnDefinition;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassEmailConfiguration;
import com.ephesoft.dcma.da.domain.BatchClassGroups;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchClassPlugin;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.FieldType;
import com.ephesoft.dcma.da.domain.Plugin;
import com.ephesoft.dcma.da.domain.TableInfo;
import com.ephesoft.dcma.da.id.BatchClassID;
import com.ephesoft.dcma.da.property.BatchClassProperty;
import com.ephesoft.dcma.da.property.ModuleProperty;
import com.ephesoft.dcma.da.service.BatchClassEmailConfigService;
import com.ephesoft.dcma.da.service.BatchClassModuleService;
import com.ephesoft.dcma.da.service.BatchClassPluginConfigService;
import com.ephesoft.dcma.da.service.BatchClassPluginService;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.DocumentTypeService;
import com.ephesoft.dcma.da.service.FieldTypeService;
import com.ephesoft.dcma.da.service.PageTypeService;
import com.ephesoft.dcma.da.service.PluginConfigService;
import com.ephesoft.dcma.da.service.PluginService;
import com.ephesoft.dcma.fuzzydb.service.FuzzyDBSearchService;
import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementService;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.PluginNameConstants;
import com.ephesoft.dcma.gwt.core.server.BatchClassUtil;
import com.ephesoft.dcma.gwt.core.server.DCMARemoteServiceServlet;
import com.ephesoft.dcma.gwt.core.shared.BatchClassDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassModuleDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginConfigDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchFolderListDTO;
import com.ephesoft.dcma.gwt.core.shared.DocumentTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.ImportBatchClassSuperConfig;
import com.ephesoft.dcma.gwt.core.shared.ImportBatchClassUserOptionDTO;
import com.ephesoft.dcma.gwt.core.shared.KVExtractionDTO;
import com.ephesoft.dcma.gwt.core.shared.OutputDataCarrierDTO;
import com.ephesoft.dcma.gwt.core.shared.PluginDetailsDTO;
import com.ephesoft.dcma.gwt.core.shared.RoleDTO;
import com.ephesoft.dcma.gwt.core.shared.SamplePatternDTO;
import com.ephesoft.dcma.gwt.core.shared.TableInfoDTO;
import com.ephesoft.dcma.gwt.core.shared.TestTableResultDTO;
import com.ephesoft.dcma.gwt.core.shared.comparator.BatchClassPluginComparator;
import com.ephesoft.dcma.gwt.core.shared.comparator.ModuleComparator;
import com.ephesoft.dcma.gwt.core.shared.exception.GWTException;
import com.ephesoft.dcma.gwt.core.shared.importTree.Node;
import com.ephesoft.dcma.imagemagick.ImageMagicProperties;
import com.ephesoft.dcma.imagemagick.imageClassifier.SampleThumbnailGenerator;
import com.ephesoft.dcma.imagemagick.service.ImageProcessService;
import com.ephesoft.dcma.kvfinder.data.InputDataCarrier;
import com.ephesoft.dcma.kvfinder.data.OutputDataCarrier;
import com.ephesoft.dcma.kvfinder.service.KVFinderService;
import com.ephesoft.dcma.lucene.service.SearchClassificationService;
import com.ephesoft.dcma.recostar.RecostarProperties;
import com.ephesoft.dcma.tablefinder.service.TableFinderService;
import com.ephesoft.dcma.tesseract.TesseractProperties;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.IUtilCommonConstants;
import com.ephesoft.dcma.workflow.service.WorflowDeploymentService;
import com.ephesoft.dcma.workflow.service.WorkflowCreationService;
import com.ephesoft.dcma.workflow.service.common.DeploymentService;

public class BatchClassManagementServiceImpl extends DCMARemoteServiceServlet implements BatchClassManagementService {

	private static final String ZERO = "0";
	private static final String WORKFLOW_STATUS_RUNNING = "Workflow_Status_Running";
	private static final String WORKFLOW_STATUS_FINISHED = "Workflow_Status_Finished";
	private static final String SERIALIZATION_EXT = FileType.SER.getExtensionWithDot();
	public static final String EMAIL_ACCOUNTS = "EmailAccounts";
	public static final String BATCH_CLASS_DEF = "BatchClassDefinition";
	public static final String SCRIPTS = "Scripts";
	public static final String ROLES = "Roles";
	private static final String PROPERTY_FILE_NAME = "application.properties";
	private static final String META_INF = "META-INF";
	private static final String ROW_COUNT = "row_count";

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(BatchClassManagementServiceImpl.class);

	@Override
	public List<BatchClassDTO> getAllBatchClasses() {
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		List<BatchClass> batchList = batchClassService.getAllUnlockedBatchClasses();
		List<BatchClassDTO> batchClassDTOs = convertToBatchClassDTOs(batchList);
		return batchClassDTOs;
	}

	@Override
	public BatchClassDTO updateBatchClass(BatchClassDTO batchClassDTO) {
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		BatchClassPluginService batchClassPluginService = this.getSingleBeanOfType(BatchClassPluginService.class);
		BatchClassPluginConfigService batchClassPluginConfigService = this.getSingleBeanOfType(BatchClassPluginConfigService.class);
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);
		PluginConfigService pluginConfigService = this.getSingleBeanOfType(PluginConfigService.class);
		BatchClass batchClass = null;
		boolean isBatchClassDirty = false;
		if (batchClassDTO.isDirty()) {
			isBatchClassDirty = true;
		}

		if (!isBatchClassDirty) {
			Collection<BatchClassModuleDTO> batchClassModules = batchClassDTO.getModules();
			for (BatchClassModuleDTO batchClassModule : batchClassModules) {
				Collection<BatchClassPluginDTO> batchClassPluginDTOs = batchClassModule.getBatchClassPlugins();
				for (BatchClassPluginDTO batchClassPlugin : batchClassPluginDTOs) {
					Collection<BatchClassPluginConfigDTO> batchClassPluginConfigDTOs = batchClassPlugin.getBatchClassPluginConfigs();
					for (BatchClassPluginConfigDTO batchClassPluginConfig : batchClassPluginConfigDTOs) {
						if (batchClassPluginConfig.getPluginConfig() != null && batchClassPluginConfig.getPluginConfig().isDirty()) {
							isBatchClassDirty = true;
							break;
						}
					}
				}
			}
		}

		if (!isBatchClassDirty) {
			return batchClassDTO;
		}

		String batchClassIdentifier = batchClassDTO.getIdentifier();

		batchClass = batchClassService.get(batchClassIdentifier);

		Set<String> groupNameSet = getAllGroups();

		List<String> docTypeNameList = BatchClassUtil.mergeBatchClassFromDTO(batchClass, batchClassDTO, groupNameSet,
				batchClassPluginConfigService, batchClassPluginService, pluginConfigService);

		if (null != docTypeNameList) {
			BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			try {
				batchSchemaService.deleteDocTypeFolder(docTypeNameList, batchClassIdentifier);
			} catch (DCMAApplicationException e) {
			}
		}
		batchClass = batchClassService.merge(batchClass, batchClass.isDeleted());
		batchClassDTO = BatchClassUtil.createBatchClassDTO(batchClass, pluginService);
		batchClassDTO.setDeployed(isBatchClassDeployed(batchClassDTO));
		return batchClassDTO;
	}

	@Override
	public BatchClassDTO getBatchClass(String batchClassIdentifier) {
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);
		BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdentifier);
		BatchClassDTO batchClassDTO = BatchClassUtil.createBatchClassDTO(batchClass, pluginService);
		Boolean isBatchClassDeployed = isBatchClassDeployed(batchClassDTO);
		batchClassDTO.setDeployed(isBatchClassDeployed);

		if (!isBatchClassDeployed) {
			batchClassDTO.setDirty(true);
		}
		return batchClassDTO;
	}

	@Override
	public void sampleGeneration(List<String> batchClassIDList) {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		PageTypeService pageTypeService = this.getSingleBeanOfType(PageTypeService.class);

		List<List<String>> batchIdDocPgNameList = pageTypeService.getDocTypeNameAndPgTypeName(batchClassIDList);
		try {
			batchSchemaService.sampleGeneration(batchIdDocPgNameList);
		} catch (DCMAApplicationException e) {

		}
	}

	private void sampleThumbnailGenerator(String batchClassIdentifier) throws DCMAApplicationException {

		String sampleBaseFolderPath = null;
		String thumbnailType = null;
		String thumbnailH = null;
		String thumbnailW = null;

		if (batchClassIdentifier == null || batchClassIdentifier.equals(ZERO)) {
			throw new DCMAApplicationException("In valid Batch Class ID");
		} else {
			BatchClassPluginConfigService batchClassPluginConfigService = this
					.getSingleBeanOfType(BatchClassPluginConfigService.class);
			BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			Map<String, String> properties = batchClassPluginConfigService.getPluginPropertiesForBatchClass(batchClassIdentifier,
					PluginNameConstants.CREATE_THUMBNAILS_PLUGIN, null);
			if (properties != null && properties.size() > 0) {
				sampleBaseFolderPath = batchSchemaService.getImageMagickBaseFolderPath(batchClassIdentifier, false);
				if (!(sampleBaseFolderPath != null && sampleBaseFolderPath.length() > 0)) {
					LOGGER.error("Error retreiving sampleBaseFolderPath");
					throw new DCMAApplicationException("Error retreiving sampleBaseFolderPath");
				}
				thumbnailH = properties.get(ImageMagicProperties.CREATE_THUMBNAILS_COMP_THUMB_HEIGHT.getPropertyKey());
				if (!(thumbnailH != null && thumbnailH.length() > 0)) {
					LOGGER.error("Error retreiving thumbnailH");
					throw new DCMAApplicationException("Error retreiving thumbnailH");
				}
				thumbnailType = properties.get(ImageMagicProperties.CREATE_THUMBNAILS_COMP_THUMB_TYPE.getPropertyKey());
				if (!(thumbnailType != null && thumbnailType.length() > 0)) {
					LOGGER.error("Error retreiving thumbnailType");
					throw new DCMAApplicationException("Error retreiving thumbnailType");
				}
				thumbnailW = properties.get(ImageMagicProperties.CREATE_THUMBNAILS_COMP_THUMB_WIDTH.getPropertyKey());
				if (!(thumbnailW != null && thumbnailW.length() > 0)) {
					LOGGER.error("Error retreiving thumbnailW");
					throw new DCMAApplicationException("Error retreiving thumbnailW");
				}
			} else {
				LOGGER.error("No Image Magic Properties found in DB. Could not generate sample Thumbnails.");
				return;
			}
		}

		SampleThumbnailGenerator sampleThumbnailGenerator = new SampleThumbnailGenerator(sampleBaseFolderPath, thumbnailType,
				thumbnailH, thumbnailW);
		sampleThumbnailGenerator.generateAllThumbnails(batchClassIdentifier);

	}

	@Override
	public void learnFileForBatchClass(String batchClassID) throws Exception {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		File batchClassFolder = new File(batchSchemaService.getBaseFolderLocation() + File.separator + batchClassID);
		if (!batchClassFolder.exists()) {
			return;
		}
		SearchClassificationService searchClassificationService = this.getSingleBeanOfType(SearchClassificationService.class);
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassID);
		String ocrPluginName = getOCRPluginNameForBatchClass(batchClassID);
		if (null != ocrPluginName) {
			if (ocrPluginName.equalsIgnoreCase(PluginNameConstants.TESSERACT_HOCR_PLUGIN)) {
				searchClassificationService.learnSampleHOCRForTesseract(batchClass.getBatchClassID(), true);
			} else if (ocrPluginName.equalsIgnoreCase(PluginNameConstants.RECOSTAR_HOCR_PLUGIN)) {
				searchClassificationService.learnSampleHOCR(batchClass.getBatchClassID(), true);
			} else {
				searchClassificationService.learnSampleHOCRForTesseract(batchClass.getBatchClassID(), true);
			}
		}
		sampleThumbnailGenerator(batchClassID);
	}

	@Override
	public void acquireLock(String batchClassIdentifier) throws GWTException {
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		try {
			batchClassService.acquireBatchClass(batchClassIdentifier, getUserName());
		} catch (BatchAlreadyLockedException e) {
			throw new GWTException(e.getMessage());
		}
	}

	@Override
	public void cleanup() {
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		batchClassService.unlockAllBatchClassesForCurrentUser(getUserName());
	}

	@Override
	public Map<String, String> getAllColumnsForTable(String driverName, String url, String userName, String password, String tableName)
			throws Exception {
		DynamicHibernateDao dao = new DynamicHibernateDao(userName, password, driverName, url);
		List<ColumnDefinition> columnNames = dao.getAllColumnsForTable(tableName);
		Map<String, String> map = new HashMap<String, String>();
		for (ColumnDefinition columnDefinition : columnNames) {
			map.put(columnDefinition.getColumnName(), columnDefinition.getType().getName());
		}
		dao.closeSession();
		return map;
	}

	@Override
	public Map<String, String> getDocumentLevelFields(String documentName, String batchClassId) {
		FieldTypeService fieldTypeService = this.getSingleBeanOfType(FieldTypeService.class);
		List<FieldType> fieldTypes = fieldTypeService.getFdTypeByDocTypeNameForBatchClass(documentName, batchClassId);
		Map<String, String> docFieldMap = new HashMap<String, String>();
		for (FieldType fieldType : fieldTypes) {
			docFieldMap.put(fieldType.getName(), fieldType.getDataType().toString());
		}
		return docFieldMap;
	}

	@Override
	public void learnDataBase(String batchClassIdentifier, boolean createIndex) throws Exception {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		File batchClassFolder = new File(batchSchemaService.getBaseFolderLocation() + File.separator + batchClassIdentifier);
		if (!batchClassFolder.exists()) {
			return;
		}
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdentifier);
		FuzzyDBSearchService fuzzyDBSearchService = this.getSingleBeanOfType(FuzzyDBSearchService.class);
		fuzzyDBSearchService.learnDataBase(batchClass.getBatchClassID(), createIndex);
	}

	@Override
	public Map<String, List<String>> getAllTables(String driverName, String url, String userName, String password) throws Exception {
		Map<String, List<String>> tableNames = null;
		DynamicHibernateDao dao = new DynamicHibernateDao(userName, password, driverName, url);
		tableNames = dao.getAllTableNames();
		dao.closeSession();
		return tableNames;
	}

	@Override
	public void copyBatchClass(BatchClassDTO batchClassDTO) throws Exception {
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);
		BatchClass batchClass = batchClassService.getLoadedBatchClassByIdentifier(batchClassDTO.getIdentifier());
		BatchClassUtil.createBatchClassDTO(batchClass, pluginService);
		batchClassService.evict(batchClass);
		copyAndSaveBatchClass(batchClassDTO, batchClassService, batchClass);
	}

	@Override
	public BatchFolderListDTO getBatchFolderList() {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		BatchFolderListDTO batchFolderListDTO = new BatchFolderListDTO();

		batchFolderListDTO.setCmisPluginMapping(batchSchemaService.getCmisPluginMappingFolderName());
		batchFolderListDTO.setFuzzyDBIndexFolderName(batchSchemaService.getFuzzyDBIndexFolderName());
		batchFolderListDTO.setImageMagickBaseFolderName(batchSchemaService.getImagemagickBaseFolderName());
		batchFolderListDTO.setProjectFilesBaseFolder(batchSchemaService.getProjectFileBaseFolder());
		batchFolderListDTO.setScripts(batchSchemaService.getScriptFolderName());
		batchFolderListDTO.setSearchIndexFolderName(batchSchemaService.getSearchIndexFolderName());
		batchFolderListDTO.setSearchSampleName(batchSchemaService.getSearchSampleName());
		batchFolderListDTO.setBatchClassSerializableFile(batchSchemaService.getBatchClassSerializableFile());
		batchFolderListDTO.setFileboundPluginMappingFolderName(batchSchemaService.getFileboundPluginMappingFolderName());
		batchFolderListDTO.initFolderList();

		return batchFolderListDTO;
	}

	private void copyAndSaveBatchClass(BatchClassDTO batchClassDTO, BatchClassService batchClassService, BatchClass batchClass)
			throws GWTException {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		String identifier = batchClass.getIdentifier();
		String existingBatchClassIdentifier = batchClass.getIdentifier();
		setBatchClassInfo(batchClassDTO, batchClass);
		BatchClassUtil.copyModules(batchClass);
		BatchClassUtil.copyDocumentTypes(batchClass);
		BatchClassUtil.copyBatchClassField(batchClass);
		if (!isBatchClassDeployed(batchClassDTO)) {
			renameBatchClassModules(batchClass, existingBatchClassIdentifier);
		}
		File originalFolder = new File(batchSchemaService.getBaseSampleFDLock() + File.separator + identifier);
		batchClass = batchClassService.createBatchClass(batchClass);
		File copiedFolder = new File(batchSchemaService.getBaseSampleFDLock() + File.separator + batchClass.getIdentifier());
		try {
			FileUtils.copyDirectoryWithContents(originalFolder, copiedFolder);
		} catch (IOException e) {
			throw new GWTException("Unable to create learning folders");
		}
	}

	private void renameBatchClassModules(BatchClass batchClass, String existingBatchClassIdentifier) {

		for (BatchClassModule batchClassModule : batchClass.getBatchClassModules()) {

			String existingWorkflowName = batchClassModule.getWorkflowName();

			batchClassModule.setWorkflowName(existingWorkflowName + "_" + existingBatchClassIdentifier);
		}
	}

	private void setBatchClassInfo(BatchClassDTO batchClassDTO, BatchClass batchClass) {
		batchClass.setDescription(batchClassDTO.getDescription());

		String batchClassPriority = batchClassDTO.getPriority();
		try {
			int priority = Integer.valueOf(batchClassPriority);
			batchClass.setPriority(priority);
		} catch (NumberFormatException e) {
			LOGGER.error("Error converting priority: " + batchClassPriority + "for batch class:" + batchClassDTO.getName() + " ."
					+ e.getMessage());
		}
		batchClass.setUncFolder(batchClassDTO.getUncFolder());
		batchClass.setName(batchClassDTO.getName());
		batchClass.setId(0);
		batchClass.setIdentifier(null);
		batchClass.setCurrentUser(null);
		batchClass.setVersion(AdminConstants.VERSION);
		batchClass.setEmailConfigurations(null);
		List<BatchClassGroups> batchClassGroupsList = batchClass.getAssignedGroups();
		for (BatchClassGroups batchClassGroups : batchClassGroupsList) {
			batchClassGroups.setId(0);
		}
		batchClass.setAssignedGroups(batchClassGroupsList);
		batchClass.setDeleted(false);
	}

	@Override
	public List<BatchClassDTO> getBatchClasses(int firstResult, int maxResults, Order order) {
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		List<BatchClass> batchList = batchClassService.getBatchClassList(firstResult, maxResults, order);
		if (!isSuperAdmin()) {
			batchList = removeUnallowedBatchClasses(batchList);
		}
		List<BatchClassDTO> batchClassDTOs = convertToBatchClassDTOs(batchList);
		return batchClassDTOs;
	}

	private List<BatchClass> removeUnallowedBatchClasses(List<BatchClass> batchClassList) {
		List<BatchClass> newBatchClassList = new ArrayList<BatchClass>();
		Set<String> rolesAssignedToUser = getUserRoles();
		for (BatchClass batchClass : batchClassList) {
			List<BatchClassGroups> batchClassGroups = batchClass.getAssignedGroups();
			for (BatchClassGroups batchClassGroup : batchClassGroups) {
				String groupName = batchClassGroup.getGroupName();
				if (rolesAssignedToUser.contains(groupName)) {
					newBatchClassList.add(batchClass);
					break;
				}
			}
		}
		return newBatchClassList;
	}

	private List<BatchClassDTO> convertToBatchClassDTOs(List<BatchClass> batchList) {
		List<BatchClassDTO> batchClassDTOs = new ArrayList<BatchClassDTO>();
		for (BatchClass batchClass : batchList) {
			BatchClassDTO batchClassDTO = new BatchClassDTO();
			batchClassDTO.setIdentifier(batchClass.getIdentifier());
			batchClassDTO.setDescription(batchClass.getDescription());
			batchClassDTO.setName(batchClass.getName());
			batchClassDTO.setPriority(String.valueOf(batchClass.getPriority()));
			batchClassDTO.setUncFolder(batchClass.getUncFolder());
			batchClassDTO.setVersion(batchClass.getVersion());
			batchClassDTO.setDeployed(isBatchClassDeployed(batchClassDTO));
			batchClassDTOs.add(batchClassDTO);
		}
		return batchClassDTOs;
	}

	@Override
	public int countAllBatchClassesExcludeDeleted() {
		Set<String> userRoles = null;
		if (!isSuperAdmin()) {
			userRoles = getUserRoles();
		}
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		return batchClassService.countAllBatchClassesExcludeDeleted(userRoles);
	}

	@Override
	public void createUncFolder(String path) throws GWTException {
		File file = new File(path);
		if (!file.exists()) {
			boolean success = file.mkdirs();
			if (!success) {
				throw new GWTException("Unable to create directory.");
			}
		}
	}

	@Override
	public List<String> getProjectFilesForDocumentType(String batchClassIdentifier, String documentTypeName) throws GWTException {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		List<String> rspFileName = null;
		try {
			rspFileName = batchSchemaService.getProjectFilesForDocumentType(batchClassIdentifier, documentTypeName);
		} catch (Exception e) {
			throw new GWTException(e.getMessage());
		}
		return rspFileName;
	}

	@Override
	public List<OutputDataCarrierDTO> testKVExtraction(BatchClassDTO batchClassDTO, KVExtractionDTO kvExtractionDTO,
			String testImageFilePath, boolean isTestAdvancedKV) throws GWTException {
		SearchClassificationService searchClassificationService = this.getSingleBeanOfType(SearchClassificationService.class);
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		KVFinderService kVFinderService = this.getSingleBeanOfType(KVFinderService.class);
		ImageProcessService imageProcessService = this.getSingleBeanOfType(ImageProcessService.class);

		BatchClassID batchClassID = new BatchClassID(batchClassDTO.getIdentifier());
		List<String> outputFilePaths = null;
		List<InputDataCarrier> inputDataCarrierList = new ArrayList<InputDataCarrier>();
		List<OutputDataCarrierDTO> carrierDTOs = new ArrayList<OutputDataCarrierDTO>();
		List<OutputDataCarrierDTO> carrierDTOsNoResult = new ArrayList<OutputDataCarrierDTO>();
		InputDataCarrier inputDataCarrier = BatchClassUtil.createInputDataCarrierFromKVExtDTO(kvExtractionDTO);
		inputDataCarrierList.add(inputDataCarrier);
		try {
			String testExtractionFolderPath = batchSchemaService.getTestKVExtractionFolderPath(batchClassID, true);
			String ocrEngineName = getOCRPluginNameForBatchClass(batchClassDTO.getIdentifier());
			BatchInstanceThread batchInstanceThread = new BatchInstanceThread();
			List<File> allImageFiles = null;
			File testImageFile = null;
			String finalImageName = null;
			if (isTestAdvancedKV) {
				if (testImageFilePath == null || testImageFilePath.isEmpty()) {
					LOGGER.info("File Image Path is either null or empty");
					throw new GWTException("Exception occuerred while performing Test Advanced KV");
				}
				testImageFile = new File(testImageFilePath);
				testExtractionFolderPath = testImageFile.getParent();
				finalImageName = testImageFile.getName();
			}
			allImageFiles = imageProcessService.convertPdfOrMultiPageTiffToTiff(batchClassID, testExtractionFolderPath, testImageFile,
					isTestAdvancedKV, batchInstanceThread);
			batchInstanceThread.execute();

			if (isTestAdvancedKV && allImageFiles.size() > 0) {
				int indexOf = finalImageName.lastIndexOf('.');
				String fileExtension = finalImageName.substring(indexOf, finalImageName.length());
				finalImageName = finalImageName.substring(0, indexOf) + BatchClassManagementConstants.FILE_CONVERSION_SUFFIX
						+ fileExtension;
				testImageFile = new File(testExtractionFolderPath + File.separator + finalImageName);
			} else {
				// delete the original files.
				for (File imageFile : allImageFiles) {
					imageFile.delete();
				}
			}
			outputFilePaths = searchClassificationService.generateHOCRForKVExtractionTest(testExtractionFolderPath, ocrEngineName,
					batchClassDTO.getIdentifier(), testImageFile, isTestAdvancedKV);
			File file = new File(testExtractionFolderPath + File.separator + "tempfile");
			if (outputFilePaths != null && !outputFilePaths.isEmpty()) {
				for (String outputFilePath : outputFilePaths) {
					File inputFile = new File(outputFilePath);
					HocrPage hocrPage = batchSchemaService.generateHocrPage(inputFile.getName(), outputFilePath, file.getPath(),
							batchClassDTO.getIdentifier(), ocrEngineName);
					List<OutputDataCarrier> outputDataCarrierList = kVFinderService.findKeyValue(inputDataCarrierList, hocrPage,
							Integer.MAX_VALUE);
					BatchClassUtil.createOutputDataDTOFromOutputDataCarrier(outputDataCarrierList, carrierDTOs, carrierDTOsNoResult,
							inputFile.getName());
				}
			}
			carrierDTOs.addAll(carrierDTOsNoResult);
			boolean isTempFileDeleted = file.delete();
			if (!isTempFileDeleted) {
				file.delete();
			}

		} catch (Exception e) {
			throw new GWTException(e.getMessage());
		}

		return carrierDTOs;
	}

	private String getOCRPluginNameForBatchClass(String batchClassIdentifier) {
		PluginPropertiesService service = this.getBeanByName("batchClassPluginPropertiesService",
				BatchClassPluginPropertiesService.class);
		LOGGER.info("Fetching the ocr engine to be used for learning.");
		String defaultOcrEngineName = getDefaultHOCRPlugin(batchClassIdentifier);
		String ocrEngineName = defaultOcrEngineName;
		PluginProperty ocrEngineSwitch = null;
		String ocrEngineSwitchValue = null;
		if (defaultOcrEngineName.equals(PluginNameConstants.RECOSTAR_HOCR_PLUGIN)) {
			ocrEngineSwitch = RecostarProperties.RECOSTAR_SWITCH;
		} else {
			ocrEngineSwitch = TesseractProperties.TESSERACT_SWITCH;
		}
		ocrEngineSwitchValue = getOcrEngineSwitchValue(batchClassIdentifier, service, ocrEngineName, ocrEngineSwitch);

		if (!AdminConstants.SWITCH_ON.equalsIgnoreCase(ocrEngineSwitchValue)) {
			if (defaultOcrEngineName.equals(PluginNameConstants.RECOSTAR_HOCR_PLUGIN)) {
				ocrEngineSwitch = TesseractProperties.TESSERACT_SWITCH;
				ocrEngineName = PluginNameConstants.TESSERACT_HOCR_PLUGIN;
			} else {
				ocrEngineSwitch = RecostarProperties.RECOSTAR_SWITCH;
				ocrEngineName = PluginNameConstants.RECOSTAR_HOCR_PLUGIN;
			}
			ocrEngineSwitchValue = getOcrEngineSwitchValue(batchClassIdentifier, service, ocrEngineName, ocrEngineSwitch);
			if (!AdminConstants.SWITCH_ON.equalsIgnoreCase(ocrEngineSwitchValue)) {
				ocrEngineName = defaultOcrEngineName;
			}
		}
		LOGGER.info("OCR Engine used for learning = " + ocrEngineName);
		return ocrEngineName;
	}

	private String getOcrEngineSwitchValue(String batchClassIdentifier, PluginPropertiesService service, String ocrEngineName,
			PluginProperty ocrEngineSwitch) {
		String ocrEngineSwitchValue = null;
		BatchPlugin plugin = service.getPluginProperties(batchClassIdentifier, ocrEngineName);
		if (plugin != null && plugin.getPropertiesSize() > 0) {
			List<BatchPluginConfiguration> pluginConfigurations = plugin.getPluginConfigurations(ocrEngineSwitch);
			if (pluginConfigurations != null && !pluginConfigurations.isEmpty()) {
				ocrEngineSwitchValue = pluginConfigurations.get(0).getValue();
				LOGGER.info(ocrEngineName + " switch = " + ocrEngineSwitchValue);
			}
		}
		return ocrEngineSwitchValue;
	}

	private String getDefaultHOCRPlugin(final String batchClassIdentifier) {
		boolean isTesseractBatch = false;
		String ocrEngine = PluginNameConstants.RECOSTAR_HOCR_PLUGIN;
		BatchClassModuleService batchClassModuleService = this.getSingleBeanOfType(BatchClassModuleService.class);
		if (batchClassIdentifier != null && !batchClassIdentifier.isEmpty()) {
			BatchClassModule batchClassModule = batchClassModuleService.getBatchClassModuleByName(batchClassIdentifier,
					IUtilCommonConstants.PAGE_PROCESS_MODULE_NAME);
			List<BatchClassPlugin> batchClassPlugins = batchClassModule.getBatchClassPlugins();
			if (batchClassPlugins != null) {
				String pluginName = null;
				for (BatchClassPlugin batchClassPlugin : batchClassPlugins) {
					if (batchClassPlugin != null) {
						pluginName = batchClassPlugin.getPlugin().getPluginName();
					}
					if (IUtilCommonConstants.TESSERACT_HOCR_PLUGIN.equals(pluginName)) {
						isTesseractBatch = true;
						break;
					} else if (IUtilCommonConstants.RECOSTAR_HOCR_PLUGIN.equals(pluginName)) {
						isTesseractBatch = false;
						break;
					}
				}
			}
		}
		if (isTesseractBatch) {
			ocrEngine = PluginNameConstants.TESSERACT_HOCR_PLUGIN;
		}
		LOGGER.info("Default ocr plugin to ber used in cvase both ");
		return ocrEngine;
	}

	@Override
	public List<RoleDTO> getAllRoles() {
		Set<String> allGroups = getAllGroups();
		Set<String> superAdminGroups = getAllSuperAdminGroup();
		List<RoleDTO> roleDTOs = new ArrayList<RoleDTO>();
		if (null != allGroups) {
			for (String group : allGroups) {
				if (!superAdminGroups.contains(group)) {
					RoleDTO roleDTO = new RoleDTO();
					roleDTO.setName(group);
					roleDTOs.add(roleDTO);
				}
			}
		}
		return roleDTOs;
	}

	@Override
	public List<String> getAllBarcodeTypes() {
		List<BarcodeReaderTypes> barcodeTypes = BarcodeReaderTypes.valuesAsList();
		List<String> barcodesList = new ArrayList<String>();
		if (barcodeTypes != null && !barcodeTypes.isEmpty()) {
			for (BarcodeReaderTypes barcodeReaderTypes : barcodeTypes) {
				barcodesList.add(barcodeReaderTypes.name());
			}
		}
		return barcodesList;
	}

	@Override
	public BatchClassDTO deleteBatchClass(BatchClassDTO batchClassDTO) {
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);
		BatchClass batchClass = null;
		String batchClassIdentifier = batchClassDTO.getIdentifier();
		batchClass = batchClassService.get(batchClassIdentifier);
		batchClass.setDeleted(batchClassDTO.isDeleted());
		batchClass = batchClassService.merge(batchClass, batchClassDTO.isDeleted());
		deleteEmailConfigForBatchClass(batchClass);
		batchClassDTO = BatchClassUtil.createBatchClassDTO(batchClass, pluginService);
		return batchClassDTO;
	}

	private void deleteEmailConfigForBatchClass(final BatchClass batchClass) {
		BatchClassEmailConfigService batchClassEmailConfigService = this.getSingleBeanOfType(BatchClassEmailConfigService.class);
		List<BatchClassEmailConfiguration> emailConfigList = batchClassEmailConfigService
				.getEmailConfigByBatchClassIdentifier(batchClass.getIdentifier());
		for (BatchClassEmailConfiguration batchClassEmailConfiguration : emailConfigList) {
			batchClassEmailConfigService.removeEmailConfiguration(batchClassEmailConfiguration);
		}
	}

	@Override
	public String matchBaseFolder(String uncFolder) {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		return String.valueOf(BatchClassUtil.matchBaseFolder(uncFolder, batchSchemaService.getBaseFolderLocation()));
	}

	@Override
	public void copyDocument(DocumentTypeDTO documentTypeDTO) throws Exception {
		DocumentTypeService documentTypeService = this.getSingleBeanOfType(DocumentTypeService.class);
		DocumentType documentType = documentTypeService.getDocTypeByIdentifier(documentTypeDTO.getIdentifier());
		BatchClassUtil.createDocumentTypeDTO(documentTypeDTO.getBatchClass(), documentType);
		documentTypeService.evict(documentType);
		copyAndSaveDocumentType(documentTypeDTO, documentTypeService, documentType);
	}

	private void copyAndSaveDocumentType(DocumentTypeDTO documentTypeDTO, DocumentTypeService documentTypeService,
			DocumentType documentType) throws GWTException {
		setDocumentTypeInfo(documentTypeDTO, documentType);
		BatchClassUtil.changePageTypeForNewDocument(documentType);
		BatchClassUtil.copyFieldTypes(documentType);
		BatchClassUtil.copyTableInfo(documentType);
		BatchClassUtil.copyFunctionKeys(documentType);
		documentTypeService.insertDocumentType(documentType);
	}

	private void setDocumentTypeInfo(DocumentTypeDTO documentTypeDTO, DocumentType documentType) {
		documentType.setName(documentTypeDTO.getName());
		documentType.setDescription(documentTypeDTO.getDescription());
		documentType.setMinConfidenceThreshold(documentTypeDTO.getMinConfidenceThreshold());
		documentType.setHidden(documentTypeDTO.isHidden());
		documentType.setId(0);
	}

	@Override
	public String getAdvancedKVImageUploadPath(String batchClassId, String docName, String imageName) {
		LOGGER.info("Getting image url for image = " + imageName);
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		String testExtractionFolderPath = batchSchemaService.getTestAdvancedKvExtractionFolderPath(batchClassId, true);
		File imageFile = new File(testExtractionFolderPath + File.separator + docName + File.separator + imageName);
		String imageUrl = null;
		if (imageFile.exists()) {
			imageUrl = batchSchemaService.getBaseHttpURL() + "/" + batchClassId + "/"
					+ batchSchemaService.getTestAdvancedKVExtractionFolderName() + "/" + docName + "/" + imageName;
		}
		return imageUrl;
	}

	@Override
	public List<TestTableResultDTO> testTablePattern(BatchClassDTO batchClassDTO, TableInfoDTO tableInfoDTO) throws GWTException {
		SearchClassificationService searchClassificationService = this.getSingleBeanOfType(SearchClassificationService.class);
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		TableFinderService tableFinderService = this.getSingleBeanOfType(TableFinderService.class);
		ImageProcessService imageProcessService = this.getSingleBeanOfType(ImageProcessService.class);

		BatchClassID batchClassID = new BatchClassID(batchClassDTO.getIdentifier());
		List<String> outputFilePaths = null;
		List<TableInfo> inputDataCarrier = BatchClassUtil.mapTableInputFromDTO(tableInfoDTO);
		// List<DataTable> dataTableDTOs = new ArrayList<DataTable>();
		List<TestTableResultDTO> results = new ArrayList<TestTableResultDTO>();
		List<TestTableResultDTO> noResultsDTOs = new ArrayList<TestTableResultDTO>();
		try {
			String testTableFolderPath = batchSchemaService.getTestTableFolderPath(batchClassID, true);
			String ocrEngineName = getOCRPluginNameForBatchClass(batchClassDTO.getIdentifier());
			BatchInstanceThread batchInstanceThread = new BatchInstanceThread();
			List<File> allImageFiles = imageProcessService.convertPdfOrMultiPageTiffToTiff(batchClassID, testTableFolderPath, null,
					false, batchInstanceThread);
			batchInstanceThread.execute();
			// delete the original files.
			for (File imageFile : allImageFiles) {
				imageFile.delete();
			}
			outputFilePaths = searchClassificationService.generateHOCRForKVExtractionTest(testTableFolderPath, ocrEngineName,
					batchClassDTO.getIdentifier(), null, false);
			File file = new File(testTableFolderPath + File.separator + "tempfile");
			if (outputFilePaths != null && !outputFilePaths.isEmpty()) {
				for (String outputFilePath : outputFilePaths) {
					File inputFile = new File(outputFilePath);
					HocrPage hocrPage = batchSchemaService.generateHocrPage(inputFile.getName(), outputFilePath, file.getPath(),
							batchClassDTO.getIdentifier(), ocrEngineName);
					DataTable dataTable = tableFinderService.findTableData(inputDataCarrier, hocrPage, Integer.MAX_VALUE);
					BatchClassUtil.mapTestTableResultsToDTO(results, noResultsDTOs, dataTable, inputFile.getName());
				}
			}
			results.addAll(noResultsDTOs);
			boolean isTempFileDeleted = file.delete();
			if (!isTempFileDeleted) {
				file.delete();
			}
		} catch (Exception e) {
			throw new GWTException(e.getMessage());
		}
		return results;
	}

	@Override
	public Map<String, Boolean> isWorkflowContentEqual(ImportBatchClassUserOptionDTO userOptions, String userInputWorkflowName) {
		ImportBatchService imService = this.getSingleBeanOfType(ImportBatchService.class);
		DeploymentService deployService = this.getSingleBeanOfType(DeploymentService.class);
		Map<String, Boolean> results = new HashMap<String, Boolean>();
		boolean isEqual = false;
		boolean isDeployed = deployService.isDeployed(userInputWorkflowName);
		InputStream serializableFileStream = null;
		String serializableFilePath = FileUtils.getFileNameOfTypeFromFolder(userOptions.getZipFileName(), SERIALIZATION_EXT);
		BatchClass importBatchClass = null;
		try {
			serializableFileStream = new FileInputStream(serializableFilePath);
			try {
				// Import Into Database from the serialized file
				importBatchClass = (BatchClass) SerializationUtils.deserialize(serializableFileStream);
			} finally {
				try {
					if (serializableFileStream != null) {
						serializableFileStream.close();
					}
				} catch (IOException ioe) {
					LOGGER.info("Error while closing file input stream.File name:" + serializableFilePath, ioe);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error while importing" + e, e);
		}
		if (importBatchClass != null) {
			isEqual = imService.isImportWorkflowEqualDeployedWorkflow(importBatchClass, userInputWorkflowName);
		}
		results.put("isEqual", isEqual);
		results.put("isDepoyed", isDeployed);
		return results;
	}

	@Override
	public ImportBatchClassSuperConfig getImportBatchClassUIConfig(String workflowName, String zipSourcePath) {
		ImportBatchClassSuperConfig config = new ImportBatchClassSuperConfig();
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);

		List<String> uncFolders = batchClassService.getAssociatedUNCList(workflowName);
		Map<String, String> uncFolderList = new HashMap<String, String>();
		String identifier;
		Set<String> batchClassesByUserRoles = getAllBatchClassByUserRoles();
		for (String uncFolder : uncFolders) {
			identifier = batchClassService.getBatchClassIdentifierByUNCfolder(uncFolder);
			if (identifier != null && batchClassesByUserRoles.contains(identifier)) {
				uncFolderList.put(identifier, uncFolder);
			}
		}
		config.setUncFolderList(uncFolderList);
		InputStream serializableFileStream = null;
		String serializableFilePath = FileUtils.getFileNameOfTypeFromFolder(zipSourcePath, SERIALIZATION_EXT);

		try {
			serializableFileStream = new FileInputStream(serializableFilePath);
			BatchClass importBatchClass = null;
			try {
				// Import Into Database from the serialized file
				importBatchClass = (BatchClass) SerializationUtils.deserialize(serializableFileStream);
			} finally {
				try {
					if (serializableFileStream != null) {
						serializableFileStream.close();
					}
				} catch (IOException ioe) {
					LOGGER.info("Error while closing file input stream.File name:" + serializableFilePath, ioe);
				}
			}
			if (importBatchClass != null) {
				Node rootNode = config.getUiConfigRoot();
				BatchClassUtil.populateUICOnfig(importBatchClass, rootNode, batchClassService, workflowName, zipSourcePath,
						batchSchemaService);
			}
		} catch (Exception e) {
			LOGGER.error("Error while importing" + e, e);
			config = null;
		}
		return config;
	}

	@Override
	public boolean importBatchClass(ImportBatchClassUserOptionDTO userOptions) throws GWTException {
		boolean isSuccess = true;
		Map<Boolean, String> importResults = new HashMap<Boolean, String>();
		try {
			ImportBatchService importService = this.getSingleBeanOfType(ImportBatchService.class);
			ImportBatchClassOptions optionXML = new ImportBatchClassOptions();
			convertDTOtoXMLForImport(userOptions, optionXML);
			String firstRoleOfUser = null;
			Set<String> userRoles = getUserRoles();
			if (userRoles != null) {
				for (String userRole : userRoles) {
					firstRoleOfUser = userRole;
					break;
				}
			}
			importResults = importService.importBatchClass(optionXML, userOptions.isWorkflowDeployed(), false, firstRoleOfUser);
		} catch (Exception e) {
			String errorMessg = "Error while importing." + e.getMessage();
			importResults.put(false, errorMessg);
			LOGGER.error(errorMessg, e);
		}
		isSuccess = !importResults.containsKey(false);
		return isSuccess;
	}

	private void convertDTOtoXMLForImport(ImportBatchClassUserOptionDTO userOptions, ImportBatchClassOptions optionXML) {
		optionXML.setZipFilePath(userOptions.getZipFileName());
		optionXML.setUncFolder(userOptions.getUncFolder());
		optionXML.setUseExisting(userOptions.isImportExisting());
		optionXML.setUseSource(userOptions.isUseSource());
		if (!userOptions.isUseSource()) {
			optionXML.setName(userOptions.getName());
			optionXML.setDescription(userOptions.getDescription());
			optionXML.setPriority(Integer.parseInt(userOptions.getPriority()));
		}

		// update the configurations of imported batch class from DB / Zip
		if (userOptions.getUiConfigRoot() != null) {
			List<Node> leafNodes = new ArrayList<Node>();
			leafNodes = userOptions.getUiConfigRoot().getLeafNodes(leafNodes);

			List<ImportBatchClassOptions.BatchClassDefinition> bcdList = optionXML.getBatchClassDefinition();
			BatchClassDefinition bcd = new BatchClassDefinition();
			bcdList.add(bcd);

			Scripts scripts = new Scripts();
			bcd.setScripts(scripts);
			List<Script> bcdScriptList = scripts.getScript();

			Folders folders = new Folders();
			bcd.setFolders(folders);
			List<Folder> bcdFolderList = folders.getFolder();

			com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.BatchClassModules modules = new com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.BatchClassModules();
			bcd.setBatchClassModules(modules);
			List<com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.BatchClassModules.BatchClassModule> bcdModuleList = modules
					.getBatchClassModule();

			for (Node node : leafNodes) {
				// iterate over leaf nodes.
				if (node.getLabel() != null && node.getLabel().getKey().equalsIgnoreCase(BatchClassManagementServiceImpl.ROLES)) {
					optionXML.setRolesImported(BatchClassUtil.isChecked(node));
					continue;
				} else if (node.getLabel() != null
						&& node.getLabel().getKey().equalsIgnoreCase(BatchClassManagementServiceImpl.EMAIL_ACCOUNTS)) {
					optionXML.setEmailAccounts(BatchClassUtil.isChecked(node));
					continue;
				} else {
					Node parent = node.getParent();
					String parentName = parent.getLabel().getKey();
					if (node != null && node.getLabel() != null && node.getLabel().getKey() != null) {
						if (parentName.toLowerCase().indexOf("script") > -1) {
							Script script = new Script();
							script.setFileName(node.getLabel().getKey());
							script.setSelected(BatchClassUtil.isChecked(node));
							bcdScriptList.add(script);
						}
						if (parentName.toLowerCase().indexOf("module") > -1) {
							com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.BatchClassModules.BatchClassModule bcm = new com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.BatchClassModules.BatchClassModule();
							bcm.setModuleName(node.getLabel().getKey());
							bcm.setPluginConfiguration(BatchClassUtil.isChecked(node));
							bcdModuleList.add(bcm);
						}
						if (parentName.toLowerCase().indexOf("folder") > -1) {
							Folder folder = new Folder();
							folder.setFileName(node.getLabel().getKey());
							folder.setSelected(BatchClassUtil.isChecked(node));
							bcdFolderList.add(folder);
						}
					}
				}
			}
		}

	}

	@Override
	public List<BatchClassDTO> getAllBatchClassesIncludingDeleted() {
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		List<BatchClass> batchList = batchClassService.getAllBatchClasses();
		List<BatchClassDTO> batchClassDTOs = convertToBatchClassDTOs(batchList);
		return batchClassDTOs;
	}

	@Override
	public void deleteAttachedFolders(String zipFileName) {
		if (zipFileName != null && !zipFileName.isEmpty()) {
			if (new File(zipFileName).exists()) {
				FileUtils.deleteDirectoryAndContentsRecursive(new File(zipFileName));
			}
		}
	}

	@Override
	public List<String> getAllPrimaryKeysForTable(String driverName, String url, String userName, String password, String table,
			String tableType) throws Exception {
		List<String> keyList = null;
		DynamicHibernateDao dao = new DynamicHibernateDao(userName, password, driverName, url);
		keyList = dao.getPrimaryKeysForTable(table, tableType);
		dao.closeSession();
		return keyList;
	}

	@Override
	public String getBatchClassRowCount() throws GWTException {
		String filePath = META_INF + File.separator + PROPERTY_FILE_NAME;
		String rowCount = null;
		try {
			InputStream propertyInStream = new ClassPathResource(filePath).getInputStream();
			Properties properties = new Properties();
			properties.load(propertyInStream);
			rowCount = properties.getProperty(ROW_COUNT);
		} catch (IOException e) {
			rowCount = null;
		}
		return rowCount;
	}

	@Override
	public List<PluginDetailsDTO> getAllPluginDetailDTOs() {
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);
		List<PluginDetailsDTO> allPluginDetailsDTO = new ArrayList<PluginDetailsDTO>();
		for (Plugin plugin : pluginService.getAllPluginsNames()) {
			PluginDetailsDTO pluginDetailsDTO = BatchClassUtil.createPluginDetailsDTO(plugin, pluginService);
			allPluginDetailsDTO.add(pluginDetailsDTO);

		}

		return allPluginDetailsDTO;
	}

	@Override
	public BatchClassDTO createAndDeployWorkflowJPDL(String workflowName, BatchClassDTO batchClassDTO) {

		LOGGER.info("Saving Batch Class before deploying");
		updateBatchClass(batchClassDTO);

		WorkflowCreationService workflowCreationService = this.getSingleBeanOfType(WorkflowCreationService.class);
		WorflowDeploymentService worflowDeploymentService = this.getSingleBeanOfType(WorflowDeploymentService.class);

		Map<String, List<String>> moduleToPluginMap = getModuleToPluginMap(batchClassDTO);
		String workflowProcessDefinitionPath = "";
		List<String> moduleProcessDefinitionPaths = new ArrayList<String>(0);

		List<String> moduleNames = new ArrayList<String>(moduleToPluginMap.keySet());

		ClassPathResource classPathResource = new ClassPathResource("META-INF\\dcma-workflows");
		File workflowDirectory = null;
		try {
			workflowDirectory = classPathResource.getFile();
			if (!workflowDirectory.exists()) {
				workflowDirectory.mkdir();
			}
			// classPathResource.getPath()

			File newWorkflowDirectory = new File(workflowDirectory.getAbsolutePath() + File.separator + "workflows" + File.separator
					+ workflowName);
			LOGGER.info("Creating New workflow directory " + newWorkflowDirectory.getPath() + " if it does not exist");
			newWorkflowDirectory.mkdirs();

			File newModulesDirectory = new File(workflowDirectory.getAbsolutePath() + File.separator + "modules" + File.separator
					+ workflowName);

			LOGGER.info("Creating New modules directory " + newModulesDirectory.getPath() + " if it does not exist");
			newModulesDirectory.mkdirs();

			List<ModuleJpdlPluginCreationInfo> customJpdlCreationInfos = new ArrayList<ModuleJpdlPluginCreationInfo>();

			/* Create JPDL for each module */
			for (String module : moduleNames) {

				// Create customJpdlCreationInfo objects for plug-ins
				customJpdlCreationInfos.clear();
				LOGGER
						.info("Converting " + module
								+ "Batch class module object to the List CustomJpdlCreationInfo object for plugins");
				customJpdlCreationInfos = BatchClassUtil.getCustomJpdlCreationInfos(batchClassDTO, module);

				LOGGER.info("Creating JPDL for new module");
				String moduleProcessDefinitionPath = workflowCreationService.writeJPDL(newModulesDirectory.getPath(), module,
						customJpdlCreationInfos);
				if (!moduleProcessDefinitionPath.isEmpty()) {
					moduleProcessDefinitionPaths.add(File.separator + classPathResource.getPath() + File.separator + "modules"
							+ File.separator + workflowName + File.separator + moduleProcessDefinitionPath);
				}
			}
			LOGGER.info("Adding " + WORKFLOW_STATUS_RUNNING + " to the begining of the workflow");
			moduleNames.add(0, WORKFLOW_STATUS_RUNNING);

			LOGGER.info("Adding " + WORKFLOW_STATUS_FINISHED + " to the end of the workflow");
			moduleNames.add(moduleNames.size(), WORKFLOW_STATUS_FINISHED);

			customJpdlCreationInfos.clear();
			for (String moduleName : moduleNames) {
				// For modules use only subprocess name constructor
				customJpdlCreationInfos.add(new ModuleJpdlPluginCreationInfo(moduleName));
			}

			LOGGER.info("Creating JPDL for Batch Class id: " + batchClassDTO.getIdentifier());
			/* Create JPDL for the workflow */
			workflowProcessDefinitionPath = File.separator
					+ classPathResource.getPath()
					+ File.separator
					+ "workflows"
					+ File.separator
					+ workflowName
					+ File.separator
					+ workflowCreationService.writeJPDL(newWorkflowDirectory.getPath(), batchClassDTO.getName(),
							customJpdlCreationInfos);

			// Deploy Module JPDLs
			for (String path : moduleProcessDefinitionPaths) {
				LOGGER.info("Deploying the JPDL for " + path.substring(path.lastIndexOf(File.separator)) + " module");
				worflowDeploymentService.deploy(path);
			}

			// Deploy Workflow JPDL

			LOGGER
					.info("Deploying the JPDL for "
							+ workflowProcessDefinitionPath.substring(workflowProcessDefinitionPath.lastIndexOf(File.separator))
							+ " Workflow");
			worflowDeploymentService.deploy(workflowProcessDefinitionPath);
		} catch (Exception e) {
			LOGGER.error("Exception " + e.getMessage());
		}

		return batchClassDTO;
	}

	private Map<String, List<String>> getModuleToPluginMap(BatchClassDTO batchClassDTO) {
		LOGGER.info("Getting module to plugin map in sorted order..for batch class id: " + batchClassDTO.getIdentifier());
		Map<String, List<String>> moduleToPluginMap = new LinkedHashMap<String, List<String>>();

		try {
			List<BatchClassModuleDTO> batchClassModuleDTOs = new ArrayList<BatchClassModuleDTO>(batchClassDTO.getModules());

			LOGGER.info("Sorting batch class modules for batch class id: " + batchClassDTO.getIdentifier());

			Order moduleOrder = new Order(ModuleProperty.ORDER, true);
			ModuleComparator moduleComparator = new ModuleComparator(moduleOrder);
			Collections.sort(batchClassModuleDTOs, moduleComparator);
			// Sort modules
			for (BatchClassModuleDTO module : batchClassModuleDTOs) {
				List<String> pluginNames = new ArrayList<String>();
				List<BatchClassPluginDTO> batchClassPluginDTOs = new ArrayList<BatchClassPluginDTO>(module.getBatchClassPlugins());
				LOGGER.info("Sorting batch class plugins for for batch class module id: " + module.getIdentifier());

				Order pluginOrder = new Order(com.ephesoft.dcma.da.property.PluginProperty.ORDER, true);
				BatchClassPluginComparator batchClassPluginComparator = new BatchClassPluginComparator(pluginOrder);

				Collections.sort(batchClassPluginDTOs, batchClassPluginComparator);
				// Sort plug-ins

				for (BatchClassPluginDTO plugin : batchClassPluginDTOs) {
					// populates the list of plug-ins for a module
					LOGGER.info("Populating the " + plugin.getPlugin().getPluginName() + " plugin info into the map");
					pluginNames.add(plugin.getPlugin().getPluginWorkflowName());
				}
				// Entry for each module with its list of plug-ins
				moduleToPluginMap.put(module.getWorkflowName(), pluginNames);

			}
		} catch (Exception e) {
			LOGGER.error("Exception: " + e.getMessage());
		}
		return moduleToPluginMap;
	}

	private Boolean isBatchClassDeployed(BatchClassDTO batchClassDTO) {
		Boolean isBatchClassDeployed = false;
		DeploymentService deploymentService = this.getSingleBeanOfType(DeploymentService.class);
		LOGGER.info("Checking if the batch class with identifier as :" + batchClassDTO.getIdentifier() + " is deployed or not");
		isBatchClassDeployed = deploymentService.isDeployed(batchClassDTO.getName());
		LOGGER.info("Batch class with identifier  is deployed :" + batchClassDTO.getIdentifier());
		return isBatchClassDeployed;

	}

	@Override
	public List<String> getAllBatchClassModulesWorkflowName() throws Exception {
		List<String> moduleNameDisplayStringList = new ArrayList<String>(0);
		BatchClassModuleService batchClassModuleService = this.getSingleBeanOfType(BatchClassModuleService.class);
		Order order = new Order(BatchClassProperty.WORKFLOW_NAME, true);
		List<BatchClassModule> allBatchClassModulesInOrderByWorkflowName = batchClassModuleService.getAllBatchClassModules(order);
		for (BatchClassModule batchClassModule : allBatchClassModulesInOrderByWorkflowName) {
			long batchClassModuleId = batchClassModule.getId();
			String workflowName = batchClassModule.getWorkflowName();
			StringBuffer moduleNameDisplayStringBuffer = new StringBuffer();
			if (workflowName != null) {
				moduleNameDisplayStringBuffer.append(workflowName);
				moduleNameDisplayStringBuffer.append('.');
				BatchClass batchClass = batchClassModule.getBatchClass();
				if (batchClass != null) {
					String identifier = batchClass.getIdentifier();
					if (identifier != null) {
						moduleNameDisplayStringBuffer.append(identifier);
					} else {
						long id = batchClass.getId();
						String errorMsg = "No batch class identifier found for batch class with id:" + id;
						LOGGER.error(errorMsg);
						throw new Exception(errorMsg);
					}
				} else {
					String errorMsg = "No batch class found for the present batch class module with batch class module id:"
							+ batchClassModuleId + " and workflow name: " + workflowName;
					LOGGER.error(errorMsg);
					throw new Exception(errorMsg);
				}
			} else {
				String errorMsg = "No workflow name found for batch class module id: " + batchClassModuleId;
				LOGGER.error(errorMsg);
				throw new Exception(errorMsg);
			}
			moduleNameDisplayStringList.add(moduleNameDisplayStringBuffer.toString());
		}
		return moduleNameDisplayStringList;
	}

	@Override
	public BatchClassDTO setBatchClassDTOModulesList(BatchClassDTO batchClassDTO, List<String> moduleNames) throws Exception {
		BatchClassModuleService batchClassModuleService = this.getSingleBeanOfType(BatchClassModuleService.class);
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);

		List<String> newModuleNameStringList = new ArrayList<String>(0);

		LOGGER.info("Updating the batch class module DTO list for batch class id: " + batchClassDTO.getIdentifier());
		// Remove modules
		for (BatchClassModuleDTO batchClassModuleDTO : batchClassDTO.getModules()) {
			if (!moduleNames.contains(batchClassModuleDTO.getWorkflowName())) {
				LOGGER.info("Removing the deselected batch class module with workflow name: " + batchClassModuleDTO.getWorkflowName());
				batchClassModuleDTO.setDeleted(true);
			}
		}

		// Add modules

		for (final String moduleName : moduleNames) {
			if (moduleName.lastIndexOf('.') != -1) {
				int temp = 1;
				LOGGER.info("Creating new batch class module DTO object");

				String moduleNameSubString = moduleName.substring(0, moduleName.indexOf('.'));
				LOGGER.info("New batch class module workflow name : " + moduleNameSubString);

				String batchClassIdentifierSubString = moduleName.substring(moduleName.indexOf('.') + 1);
				LOGGER.info("New batch class module's batch class identifier : " + batchClassIdentifierSubString);

				if (moduleNameSubString != null && !moduleNameSubString.isEmpty()) {
					if (batchClassIdentifierSubString != null && !batchClassIdentifierSubString.isEmpty()) {

						LOGGER.info("Get the existing batch class module for the workflow name: " + moduleNameSubString);
						BatchClassModule batchClassModule = batchClassModuleService.getBatchClassModuleByWorkflowName(
								batchClassIdentifierSubString, moduleNameSubString);

						LOGGER.info("Convert batch class module object to its DTO");
						BatchClassModuleDTO batchClassModuleDTO = BatchClassUtil.createBatchClassModuleDTO(batchClassDTO,
								batchClassModule, pluginService);

						batchClassModuleService.evict(batchClassModule);
						String workflowName = batchClassModuleDTO.getWorkflowName();

						LOGGER.info("Check for the available workflow name for the batch class module");
						String newWorkflowName = workflowName + "_" + batchClassIdentifierSubString + "_" + (temp++);
						boolean alreadyInList = false;
						do {
							if (newModuleNameStringList.contains(newWorkflowName)) {
								newWorkflowName = workflowName + "_" + batchClassIdentifierSubString + "_" + (temp++);
								LOGGER.info(newWorkflowName + " already exists");
								alreadyInList = true;
							} else {
								alreadyInList = false;
							}
						} while (alreadyInList);
						LOGGER.info("New workflow name for the batch class module is: " + newWorkflowName);
						newModuleNameStringList.add(newWorkflowName);

						LOGGER.info("Making changes to DTO object");
						batchClassModuleDTO.setWorkflowName(newWorkflowName);
						batchClassModuleDTO.setIdentifier(newWorkflowName);
						batchClassModuleDTO.setNew(true);
						batchClassModuleDTO.setBatchClass(batchClassDTO);
						batchClassDTO.addModule(batchClassModuleDTO);
					} else {
						String errorMsg = "Batch class identifier is either null or empty";
						LOGGER.error(errorMsg);
						throw new Exception(errorMsg);
					}
				} else {
					String errorMsg = "Module workflow name is either null or empty";
					LOGGER.error(errorMsg);
					throw new Exception(errorMsg);
				}
			} else {
				newModuleNameStringList.add(moduleName);
			}

		}
		orderModulesInBatchClass(batchClassDTO, newModuleNameStringList);

		return batchClassDTO;
	}

	private void orderModulesInBatchClass(BatchClassDTO batchClassDTO, List<String> modulesList) {

		// Change OrderNumber of modules
		String batchClassIdentifier = batchClassDTO.getIdentifier();
		LOGGER.info("Ordering the modules according to their positions on UI");
		for (BatchClassModuleDTO batchClassModuleDTO : batchClassDTO.getModules()) {
			if (!batchClassModuleDTO.isDeleted()) {
				int order = AdminConstants.INITIAL_ORDER_NUMBER;
				String workflowName = batchClassModuleDTO.getWorkflowName();
				int index = modulesList.indexOf(workflowName);

				order = (index * AdminConstants.ORDER_NUMBER_OFFSET) + AdminConstants.INITIAL_ORDER_NUMBER;

				LOGGER.info("Order number for batch class module with workflow name: " + workflowName + " form batch class "
						+ batchClassIdentifier + " is " + order);
				batchClassModuleDTO.setOrderNumber(order);
			}

		}

	}

	@Override
	public List<OutputDataCarrierDTO> testAdvancedKVExtraction(final BatchClassDTO batchClassDTO,
			final KVExtractionDTO kvExtractionDTO, final String docName, final String imageName) throws GWTException {
		LOGGER.info("Test Advanced KV Extraction for image : " + imageName);
		List<OutputDataCarrierDTO> outputDataCarrierDTOs = null;
		try {
			BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			// Get path upto test-advanced-extraction folder.
			String testKVFolderPath = batchSchemaService.getTestAdvancedKvExtractionFolderPath(batchClassDTO.getIdentifier(), true);
			File destinationImageFile = new File(testKVFolderPath + File.separator + docName + File.separator + imageName);
			if (!destinationImageFile.exists()) {
				LOGGER.error("Image doesn' exist = " + destinationImageFile.getAbsolutePath() + ". Cannot continue ocring...");
				throw new GWTException("Image Not Found : " + destinationImageFile.getAbsolutePath());
			} else {
				// hOCR file and get the list of html file names.
				outputDataCarrierDTOs = testKVExtraction(batchClassDTO, kvExtractionDTO, destinationImageFile.getAbsolutePath(), true);
			}
		} catch (Exception gwte) {
			throw new GWTException(gwte.getMessage());
		}
		return outputDataCarrierDTOs;
	}

	@Override
	public String getUpdatedTestFileName(final String batchClassIdentifier, final String docName, final String fileName) {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		String updatedFileName = fileName;
		// Get path upto test-advanced-extraction folder.
		String testKVFolderPath = batchSchemaService.getTestAdvancedKvExtractionFolderPath(batchClassIdentifier, true);
		testKVFolderPath += File.separator + docName;
		LOGGER.info("Test folder path = " + testKVFolderPath);
		File testImageFile = new File(testKVFolderPath + File.separator + fileName);

		File htmlFile = new File(testKVFolderPath + File.separator
				+ FileUtils.changeFileExtension(testImageFile.getName(), FileType.HTML.getExtension()));
		File xmlFile = new File(testKVFolderPath + File.separator
				+ FileUtils.changeFileExtension(testImageFile.getName(), FileType.HTML.getExtension()));
		if (!testImageFile.exists() || !htmlFile.exists() || !xmlFile.exists()) {
			int indexOf = fileName.lastIndexOf('.');
			String fileExtension = fileName.substring(indexOf, fileName.length());
			updatedFileName = fileName.substring(0, indexOf) + BatchClassManagementConstants.FILE_CONVERSION_SUFFIX + fileExtension;
		}
		return updatedFileName;
	}

	@Override
	public SamplePatternDTO getSamplePatterns() throws GWTException {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		String filePath = batchSchemaService.getSamplePatternFilePath();
		FileInputStream fileInputStream = null;
		SamplePatternDTO samplePattern = new SamplePatternDTO();
		Properties properties = new Properties();
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				LOGGER.error("Sample pattern property file does not exist at file path:" + filePath);
				throw new GWTException("Sample pattern property file does not exist at file path:" + filePath);
			} else {
				fileInputStream = new FileInputStream(new File(filePath));
				properties.load(fileInputStream);
			}
		} catch (IOException e) {
			LOGGER.error("Unable to read sample regular expressions file.", e);
			throw new GWTException("Unable to read the sample regular expressions file.");
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (IOException ioe) {
				LOGGER.info("Unable to close file input stream.", ioe);
			}
		}
		if (!properties.isEmpty()) {
			BatchClassUtil.createSamplePatternDTO(samplePattern, properties);
		}
		return samplePattern;
	}

	@Override
	public Boolean isUserSuperAdmin() {
		return isSuperAdmin();
	}

	@Override
	public HashSet<String> getAllRolesOfUser() {
		return (HashSet<String>) getUserRoles();
	}
}
