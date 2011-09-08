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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.barcodeextraction.BarcodeExtractionReader.BarcodeReaderTypes;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPlugin;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.service.BatchClassPluginPropertiesService;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.exception.BatchAlreadyLockedException;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.hibernate.DynamicHibernateDao;
import com.ephesoft.dcma.core.hibernate.DynamicHibernateDao.ColumnDefinition;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassEmailConfiguration;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.FieldType;
import com.ephesoft.dcma.da.domain.TableInfo;
import com.ephesoft.dcma.da.id.BatchClassID;
import com.ephesoft.dcma.da.service.BatchClassEmailConfigService;
import com.ephesoft.dcma.da.service.BatchClassPluginConfigService;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.da.service.DocumentTypeService;
import com.ephesoft.dcma.da.service.FieldTypeService;
import com.ephesoft.dcma.da.service.ModuleConfigService;
import com.ephesoft.dcma.da.service.ModuleService;
import com.ephesoft.dcma.da.service.PageTypeService;
import com.ephesoft.dcma.da.service.PluginConfigService;
import com.ephesoft.dcma.da.service.PluginService;
import com.ephesoft.dcma.fuzzydb.service.FuzzyDBSearchService;
import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementService;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.PluginNameConstants;
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
import com.ephesoft.dcma.gwt.core.shared.RoleDTO;
import com.ephesoft.dcma.gwt.core.shared.TableInfoDTO;
import com.ephesoft.dcma.gwt.core.shared.TestTableResultDTO;
import com.ephesoft.dcma.gwt.core.shared.exception.GWTException;
import com.ephesoft.dcma.gwt.core.shared.importTree.Node;
import com.ephesoft.dcma.imagemagick.ImageMagicProperties;
import com.ephesoft.dcma.imagemagick.imageClassifier.SampleThumbnailGenerator;
import com.ephesoft.dcma.imagemagick.service.ImageProcessService;
import com.ephesoft.dcma.kvfinder.data.InputDataCarrier;
import com.ephesoft.dcma.kvfinder.data.OutputDataCarrier;
import com.ephesoft.dcma.kvfinder.service.KVFinderService;
import com.ephesoft.dcma.lucene.service.SearchClassificationService;
import com.ephesoft.dcma.tablefinder.service.TableFinderService;
import com.ephesoft.dcma.util.FileUtils;

public class BatchClassManagementServiceImpl extends DCMARemoteServiceServlet implements BatchClassManagementService {

	public static final String BATCH_CLASS_DEF = "BatchClassDefinition";
	public static final String ROLES = "Roles";

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
		BatchClass batchClass = null;
		boolean isBatchClassDirty = false;
		if (batchClassDTO.isDirty()) {
			isBatchClassDirty = true;
		}

		Collection<BatchClassModuleDTO> batchClassModules = batchClassDTO.getModules();
		for (BatchClassModuleDTO batchClassModule : batchClassModules) {
			Collection<BatchClassPluginDTO> batchClassPluginDTOs = batchClassModule.getBatchClassPlugins();
			for (BatchClassPluginDTO batchClassPlugin : batchClassPluginDTOs) {
				Collection<BatchClassPluginConfigDTO> batchClassPluginConfigDTOs = batchClassPlugin.getBatchClassPluginConfigs();
				for (BatchClassPluginConfigDTO batchClassPluginConfig : batchClassPluginConfigDTOs) {
					if (batchClassPluginConfig.getPluginConfig() != null && batchClassPluginConfig.getPluginConfig().isDirty()) {
						isBatchClassDirty = true;
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

		List<String> docTypeNameList = BatchClassUtil.mergeBatchClassFromDTO(batchClass, batchClassDTO, groupNameSet);

		if (null != docTypeNameList) {
			BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			try {
				batchSchemaService.deleteDocTypeFolder(docTypeNameList, batchClassIdentifier);
			} catch (DCMAApplicationException e) {
			}
		}
		batchClass = batchClassService.merge(batchClass, batchClass.isDeleted());
		batchClassDTO = BatchClassUtil.createBatchClassDTO(batchClass);
		return batchClassDTO;
	}

	@Override
	public BatchClassDTO getBatchClass(String batchClassIdentifier) {
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdentifier);

		return BatchClassUtil.createBatchClassDTO(batchClass);
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

		if (batchClassIdentifier == null || batchClassIdentifier.equals("0")) {
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
		BatchClass batchClass = batchClassService.getLoadedBatchClassByIdentifier(batchClassDTO.getIdentifier());
		BatchClassUtil.createBatchClassDTO(batchClass);
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
		setBatchClassInfo(batchClassDTO, batchClass);
		BatchClassUtil.copyModules(batchClass);
		BatchClassUtil.copyDocumentTypes(batchClass);
		BatchClassUtil.copyBatchClassField(batchClass);
		File originalFolder = new File(batchSchemaService.getBaseSampleFDLock() + File.separator + identifier);
		batchClass = batchClassService.createBatchClass(batchClass);
		File copiedFolder = new File(batchSchemaService.getBaseSampleFDLock() + File.separator + batchClass.getIdentifier());
		try {
			FileUtils.copyDirectoryWithContents(originalFolder, copiedFolder);
		} catch (IOException e) {
			throw new GWTException("Unable to create learning folders");
		}
	}

	private void setBatchClassInfo(BatchClassDTO batchClassDTO, BatchClass batchClass) {
		batchClass.setDescription(batchClassDTO.getDescription());
		batchClass.setPriority(Integer.valueOf(batchClassDTO.getPriority()));
		batchClass.setUncFolder(batchClassDTO.getUncFolder());
		batchClass.setId(0);
		batchClass.setIdentifier(null);
		batchClass.setCurrentUser(null);
		batchClass.setVersion(AdminConstants.VERSION);
		batchClass.setEmailConfigurations(null);
		batchClass.setAssignedGroups(null);
		batchClass.setDeleted(false);
	}

	@Override
	public List<BatchClassDTO> getBatchClasses(int firstResult, int maxResults, Order order) {
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		List<BatchClass> batchList = batchClassService.getBatchClassList(firstResult, maxResults, order);
		List<BatchClassDTO> batchClassDTOs = convertToBatchClassDTOs(batchList);
		return batchClassDTOs;
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
			batchClassDTOs.add(batchClassDTO);
		}
		return batchClassDTOs;
	}

	@Override
	public int countAllBatchClassesExcludeDeleted() {
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		return batchClassService.countAllBatchClassesExcludeDeleted();
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
	public List<OutputDataCarrierDTO> testKVExtraction(BatchClassDTO batchClassDTO, KVExtractionDTO kvExtractionDTO)
			throws GWTException {
		SearchClassificationService searchClassificationService = this.getSingleBeanOfType(SearchClassificationService.class);
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		KVFinderService kVFinderService = this.getSingleBeanOfType(KVFinderService.class);
		ImageProcessService imageProcessService = this.getSingleBeanOfType(ImageProcessService.class);

		BatchClassID batchClassID = new BatchClassID(batchClassDTO.getIdentifier());
		List<String> outputFilePaths = null;
		List<InputDataCarrier> inputDataCarrierList = new ArrayList<InputDataCarrier>();
		List<OutputDataCarrierDTO> carrierDTOs = new ArrayList<OutputDataCarrierDTO>();
		InputDataCarrier inputDataCarrier = BatchClassUtil.createInputDataCarrierFromKVExtDTO(kvExtractionDTO);
		inputDataCarrierList.add(inputDataCarrier);
		try {
			String testKVExtractionFolderPath = batchSchemaService.getTestKVExtractionFolderPath(batchClassID, true);
			String ocrEngineName = getOCRPluginNameForBatchClass(batchClassDTO.getIdentifier());
			BatchInstanceThread batchInstanceThread = new BatchInstanceThread();
			List<File> allImageFiles = imageProcessService.convertPdfOrMultiPageTiffToTiff(batchClassID, testKVExtractionFolderPath,
					batchInstanceThread);
			batchInstanceThread.execute();
			// delete the original files.
			for (File imageFile : allImageFiles) {
				imageFile.delete();
			}
			outputFilePaths = searchClassificationService.generateHOCRForKVExtractionTest(testKVExtractionFolderPath, ocrEngineName,
					batchClassDTO.getIdentifier());
			File file = new File(testKVExtractionFolderPath + File.separator + "tempfile");
			if (outputFilePaths != null && !outputFilePaths.isEmpty()) {
				for (String outputFilePath : outputFilePaths) {
					File inputFile = new File(outputFilePath);
					HocrPage hocrPage = batchSchemaService.generateHocrPage(inputFile.getName(), outputFilePath, file.getPath(),
							batchClassDTO.getIdentifier());
					List<OutputDataCarrier> outputDataCarrierList = kVFinderService.findKeyValue(inputDataCarrierList, hocrPage,
							Integer.MAX_VALUE);
					BatchClassUtil.createOutputDataDTOFromOutputDataCarrier(outputDataCarrierList, carrierDTOs, inputFile.getName());
				}
			}
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
		String ocrEngineName = null;
		BatchPlugin plugin = service.getPluginProperties(batchClassIdentifier, PluginNameConstants.RECOSTAR_HOCR_PLUGIN);
		if (plugin != null && plugin.getPropertiesSize() > 0) {
			ocrEngineName = PluginNameConstants.RECOSTAR_HOCR_PLUGIN;
		} else {
			plugin = service.getPluginProperties(batchClassIdentifier, PluginNameConstants.TESSERACT_HOCR_PLUGIN);
			if (plugin != null && plugin.getPropertiesSize() > 0) {
				ocrEngineName = PluginNameConstants.TESSERACT_HOCR_PLUGIN;
			}
		}
		return ocrEngineName;
	}

	@Override
	public List<RoleDTO> getAllRoles() {
		Set<String> allGroups = getAllGroups();
		List<RoleDTO> roleDTOs = new ArrayList<RoleDTO>();
		if (null != allGroups) {
			for (String group : allGroups) {
				RoleDTO roleDTO = new RoleDTO();
				roleDTO.setName(group);
				roleDTOs.add(roleDTO);
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
		BatchClass batchClass = null;
		String batchClassIdentifier = batchClassDTO.getIdentifier();
		batchClass = batchClassService.get(batchClassIdentifier);
		batchClass.setDeleted(batchClassDTO.isDeleted());
		batchClass = batchClassService.merge(batchClass, batchClassDTO.isDeleted());
		deleteEmailConfigForBatchClass(batchClass);
		batchClassDTO = BatchClassUtil.createBatchClassDTO(batchClass);
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
		documentType.setId(0);
	}

	@Override
	public String getAdvancedKVImageUploadPath(String batchClassId, String imageName) {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		return batchSchemaService.getBaseHttpURL() + "/" + batchClassId + "/" + batchSchemaService.getTempFolderName() + "/"
				+ imageName;
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
		try {
			String testTableFolderPath = batchSchemaService.getTestTableFolderPath(batchClassID, true);
			String ocrEngineName = getOCRPluginNameForBatchClass(batchClassDTO.getIdentifier());
			BatchInstanceThread batchInstanceThread = new BatchInstanceThread();
			List<File> allImageFiles = imageProcessService.convertPdfOrMultiPageTiffToTiff(batchClassID, testTableFolderPath,
					batchInstanceThread);
			batchInstanceThread.execute();
			// delete the original files.
			for (File imageFile : allImageFiles) {
				imageFile.delete();
			}
			outputFilePaths = searchClassificationService.generateHOCRForKVExtractionTest(testTableFolderPath, ocrEngineName,
					batchClassDTO.getIdentifier());
			File file = new File(testTableFolderPath + File.separator + "tempfile");
			if (outputFilePaths != null && !outputFilePaths.isEmpty()) {
				for (String outputFilePath : outputFilePaths) {
					File inputFile = new File(outputFilePath);
					HocrPage hocrPage = batchSchemaService.generateHocrPage(inputFile.getName(), outputFilePath, file.getPath(),
							batchClassDTO.getIdentifier());
					DataTable dataTable = tableFinderService.findTableData(inputDataCarrier, hocrPage, Integer.MAX_VALUE);
					BatchClassUtil.mapTestTableResultsToDTO(results, dataTable, inputFile.getName());
				}
			}
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
	public ImportBatchClassSuperConfig getImportBatchClassUIConfig(String workflowName, String zipSourcePath) {
		ImportBatchClassSuperConfig config = new ImportBatchClassSuperConfig();
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		config.setUncFolderList(batchClassService.getAssociatedUNCList(workflowName));
		InputStream serializableFileStream = null;
		String serializableFilePath = FileUtils.getFileNameOfTypeFromFolder(zipSourcePath, ".ser");

		try {
			serializableFileStream = new FileInputStream(serializableFilePath);
			// Import Into Database from the serialized file
			BatchClass importBatchClass = (BatchClass) SerializationUtils.deserialize(serializableFileStream);

			if (importBatchClass != null) {
				Node rootNode = config.getUiConfigRoot();
				BatchClassUtil.populateUICOnfig(importBatchClass, rootNode, batchClassService, workflowName);
			}
		} catch (Exception e) {
			LOGGER.error("Error while importing" + e, e);
			config = null;
		}
		return config;
	}

	@Override
	public boolean importBatchClass(ImportBatchClassUserOptionDTO userOptions) throws GWTException {

		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		ModuleService moduleService = this.getSingleBeanOfType(ModuleService.class);
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);
		PluginConfigService pluginConfigService = this.getSingleBeanOfType(PluginConfigService.class);
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		ModuleConfigService moduleConfigService = this.getSingleBeanOfType(ModuleConfigService.class);

		String tempOutputUnZipDir = userOptions.getZipFileName();
		File originalFolder = new File(tempOutputUnZipDir);
		String serializableFilePath = FileUtils.getFileNameOfTypeFromFolder(tempOutputUnZipDir, ".ser");

		InputStream serializableFileStream = null;
		boolean isSuccess = true;
		try {
			serializableFileStream = new FileInputStream(serializableFilePath);
			// Import Into Database from the serialized file
			BatchClass importBatchClass = (BatchClass) SerializationUtils.deserialize(serializableFileStream);

			// delete serialization file from folder and create test-extraction folder
			new File(serializableFilePath).delete();
			new File(tempOutputUnZipDir + File.separator + batchSchemaService.getTestKVExtractionFolderName()).mkdir();
			new File(tempOutputUnZipDir + File.separator + batchSchemaService.getTestTableFolderName()).mkdir();

			if (userOptions.isImportExisting()) {
				isSuccess = overrideExistingBatchClass(userOptions, batchClassService, batchSchemaService, moduleService,
						pluginService, pluginConfigService, moduleConfigService, batchInstanceService, tempOutputUnZipDir, originalFolder, isSuccess,
						importBatchClass);
			} else {
				isSuccess = importNewBatchClass(userOptions, batchClassService, batchSchemaService, moduleService, pluginService,
						pluginConfigService, moduleConfigService, tempOutputUnZipDir, originalFolder, serializableFileStream, importBatchClass, isSuccess);
			}
		} catch (Exception e) {
			LOGGER.error("Error while importing" + e, e);
			isSuccess = false;
		}
		return isSuccess;
	}

	private boolean importNewBatchClass(ImportBatchClassUserOptionDTO userOptions, BatchClassService batchClassService,
			BatchSchemaService batchSchemaService, ModuleService moduleService, PluginService pluginService,
			PluginConfigService pluginConfigService, ModuleConfigService moduleConfigService,String tempOutputUnZipDir, File originalFolder,
			InputStream serializableFileStream, BatchClass importBatchClass, boolean isSuccess) throws IOException {
		// create a new batch class from zip file
		try {
			importBatchClass.setId(0L);
			if (userOptions.getUiConfigRoot() != null) {
				List<Node> leafNodes = new ArrayList<Node>();
				leafNodes = userOptions.getUiConfigRoot().getLeafNodes(leafNodes);

				for (Node node : leafNodes) {
					if (node.getLabel() != null && node.getLabel().getKey().equalsIgnoreCase(BatchClassManagementServiceImpl.ROLES)) {
						if (!BatchClassUtil.isChecked(node)) {
							// do not import the roles
							importBatchClass.getAssignedGroups().clear();
						}
					} else {
						if (node != null && node.getLabel() != null) {
							if (node.getLabel().getKey().equalsIgnoreCase("ImageSample")) {
								if (!BatchClassUtil.isChecked(node)) {
									// do not import the image samples
									String imagemagickBaseFolder = FileUtils.getFileNameOfTypeFromFolder(tempOutputUnZipDir,
											batchSchemaService.getImagemagickBaseFolderName());
									FileUtils.deleteDirectoryAndContentsRecursive(new File(imagemagickBaseFolder));
								}
							}
							if (node.getLabel().getKey().equalsIgnoreCase("LuceneSample")) {
								if (!BatchClassUtil.isChecked(node)) {
									// do not import the lucene samples
									String searchSampleFolder = FileUtils.getFileNameOfTypeFromFolder(tempOutputUnZipDir,
											batchSchemaService.getSearchSampleName());
									FileUtils.deleteDirectoryAndContentsRecursive(new File(searchSampleFolder));
								}
							}
						}
					}
				}
			}

			if (!userOptions.isUseSource()) {
				importBatchClass.setDescription(userOptions.getDescription());
				importBatchClass.setName(userOptions.getName());
				importBatchClass.setPriority(Integer.parseInt(userOptions.getPriority()));
			}
			new File(userOptions.getUncFolder()).mkdirs();
			importBatchClass.setUncFolder(userOptions.getUncFolder());
			try {
				BatchClassUtil.updateSerializableBatchClass(moduleConfigService, moduleService, pluginService, pluginConfigService, importBatchClass);
				batchClassService.evict(importBatchClass);
				importBatchClass = batchClassService.createBatchClass(importBatchClass);
			} catch (Exception exception) {
				LOGGER.error("Unable to create/override batch Class." + exception, exception);
				isSuccess = false;
				new File(userOptions.getUncFolder()).delete();
			}

			File copiedFolder = new File(batchSchemaService.getBaseSampleFDLock() + File.separator + importBatchClass.getIdentifier());
			FileUtils.copyDirectoryWithContents(originalFolder, copiedFolder);

		} catch (FileNotFoundException e) {
			LOGGER.error("Serializable file not found." + e, e);
			isSuccess = false;
			new File(userOptions.getUncFolder()).delete();
		} catch (IOException e) {
			LOGGER.error("Unable to copy the learning folders." + e, e);
			isSuccess = false;
			new File(userOptions.getUncFolder()).delete();
		} finally {
			if (serializableFileStream != null) {
				serializableFileStream.close();
			}
			FileUtils.deleteDirectoryAndContentsRecursive(originalFolder);
			new File(userOptions.getZipFileName()).delete();
		}
		return isSuccess;
	}

	private boolean overrideExistingBatchClass(ImportBatchClassUserOptionDTO userOptions, BatchClassService batchClassService,
			BatchSchemaService batchSchemaService, ModuleService moduleService, PluginService pluginService,
			PluginConfigService pluginConfigService, ModuleConfigService moduleConfigService,  BatchInstanceService batchInstanceService, String tempOutputUnZipDir,
			File originalFolder, boolean isSuccess, BatchClass importBatchClass) throws Exception {
		// overriding a batch class
		BatchClass existingBatchClass = batchClassService.getLoadedBatchClassByUNC(userOptions.getUncFolder());
		batchClassService.evict(existingBatchClass);
		List<String> scriptsList = new ArrayList<String>();
		if (existingBatchClass != null) {
			String existingUncFolder = existingBatchClass.getUncFolder();

			// update the configurations of imported batch class from DB / Zip
			if (userOptions.getUiConfigRoot() != null) {
				List<Node> leafNodes = new ArrayList<Node>();
				leafNodes = userOptions.getUiConfigRoot().getLeafNodes(leafNodes);

				for (Node node : leafNodes) {
					// iterate over leaf nodes.
					if (node.getLabel() != null && node.getLabel().getKey().equalsIgnoreCase(BatchClassManagementServiceImpl.ROLES)) {
						if (!BatchClassUtil.isChecked(node)) {
							// import the roles from database
							BatchClassUtil.updateRoles(importBatchClass, existingBatchClass, Boolean.TRUE);
						}
						continue;
					} else {

						Node parent = node.getParent();
						String moduleName = parent.getLabel().getKey();

						if (node != null && node.getLabel() != null && node.getLabel().getKey() != null) {
							String nodeName = node.getLabel().getKey();
							if (node.getLabel().getKey().toLowerCase().indexOf("script") > -1) {
								BatchClassUtil.updateScriptsFiles(tempOutputUnZipDir, batchSchemaService, scriptsList, node,
										moduleName, importBatchClass, existingBatchClass);
								continue;
							}

							if (nodeName.equalsIgnoreCase("PluginConfiguration")) {
								if (!BatchClassUtil.isChecked(node)) {
									// import the plug-in configuration for the module from database
									BatchClassModule existingBatchClassModule = batchSchemaService.getDetachedBatchClassModuleByName(
											existingBatchClass.getIdentifier(), moduleName);
									BatchClassModule importBatchClassModule = importBatchClass.getBatchClassModuleByName(moduleName);
									List<BatchClassModule> importedBatchClassMod = importBatchClass.getBatchClassModules();
									importedBatchClassMod.set(importedBatchClassMod.indexOf(importBatchClassModule),
											existingBatchClassModule);
								}
								continue;
							}

							if (nodeName.equalsIgnoreCase("EmailAccounts")) {
								if (!BatchClassUtil.isChecked(node)) {
									// import the email accounts for the module from database
									BatchClassUtil.updateEmailConfigurations(importBatchClass, existingBatchClass, Boolean.TRUE);
								}
								continue;
							}
							if (nodeName.equalsIgnoreCase("Fuzzy-DBIndex")) {
								if (!BatchClassUtil.isChecked(node)) {
									// import the fuzzy DB samples from database
									String fuzzyDbBaseFolder = FileUtils.getFileNameOfTypeFromFolder(tempOutputUnZipDir,
											batchSchemaService.getFuzzyDBIndexFolderName());
									String existingFuzzyDBFolder = batchSchemaService.getFuzzyDBIndexFolder(existingBatchClass
											.getIdentifier(), false);
									isSuccess = BatchClassUtil.overrideFromDB(existingFuzzyDBFolder, fuzzyDbBaseFolder,
											tempOutputUnZipDir);
									if (!isSuccess) {
										break;
									}
								}
								continue;
							}

							if (nodeName.equalsIgnoreCase("CmisMapping")) {
								if (!BatchClassUtil.isChecked(node)) {
									// import the fuzzy DB samples from database
									String cmisBaseFolder = FileUtils.getFileNameOfTypeFromFolder(tempOutputUnZipDir,
											batchSchemaService.getCmisPluginMappingFolderName());
									String existingCmisBaseFolder = batchSchemaService.getAbsolutePath(existingBatchClass
											.getIdentifier(), batchSchemaService.getCmisPluginMappingFolderName(), false);
									isSuccess = BatchClassUtil.overrideFromDB(existingCmisBaseFolder, cmisBaseFolder,
											tempOutputUnZipDir);
									if (!isSuccess) {
										break;
									}
								}
								continue;
							}

							/*if (nodeName.equalsIgnoreCase("FileboundMapping")) {
								if (!BatchClassUtil.isChecked(node)) {
									// import the fuzzy DB samples from database
									String fileboundBaseFolder = FileUtils.getFileNameOfTypeFromFolder(tempOutputUnZipDir,
											batchSchemaService.getFileboundPluginMappingFolderName());

									String existingFileboundBaseFolder = batchSchemaService.getAbsolutePath(existingBatchClass
											.getIdentifier(), batchSchemaService.getFileboundPluginMappingFolderName(), false);
									isSuccess = BatchClassUtil.overrideFromDB(existingFileboundBaseFolder, fileboundBaseFolder,
											tempOutputUnZipDir);
									if (!isSuccess) {
										break;
									}
								}
								continue;
							}*/

							if (nodeName.equalsIgnoreCase("ImageSample")) {
								if (!BatchClassUtil.isChecked(node)) {
									// import the image samples from database
									String imagemagickBaseFolder = FileUtils.getFileNameOfTypeFromFolder(tempOutputUnZipDir,
											batchSchemaService.getImagemagickBaseFolderName());

									String existingImagemagickBaseFolder = batchSchemaService.getImageMagickBaseFolderPath(
											existingBatchClass.getIdentifier(), false);

									isSuccess = BatchClassUtil.overrideFromDB(existingImagemagickBaseFolder, imagemagickBaseFolder,
											tempOutputUnZipDir);
									if (!isSuccess) {
										break;
									}
								}
								continue;
							}
							if (nodeName.equalsIgnoreCase("LuceneSample")) {
								if (!BatchClassUtil.isChecked(node)) {
									// import the lucene samples from database
									String searchSampleFolder = FileUtils.getFileNameOfTypeFromFolder(tempOutputUnZipDir,
											batchSchemaService.getSearchSampleName());
									String existingSearchSampleFolder = batchSchemaService.getSearchClassSamplePath(existingBatchClass
											.getIdentifier(), false);

									isSuccess = BatchClassUtil.overrideFromDB(existingSearchSampleFolder, searchSampleFolder,
											tempOutputUnZipDir);
									if (!isSuccess) {
										break;
									}

								}
								continue;
							}
							if (nodeName.equalsIgnoreCase("RecostarExtraction")) {
								if (!BatchClassUtil.isChecked(node)) {
									// import the image samples from database
									String recostarBaseFolder = FileUtils.getFileNameOfTypeFromFolder(tempOutputUnZipDir,
											batchSchemaService.getProjectFileBaseFolder());
									String existingRecostarBaseFolder = batchSchemaService.getAbsolutePath(existingBatchClass
											.getIdentifier(), batchSchemaService.getProjectFileBaseFolder(), false);
									isSuccess = BatchClassUtil.overrideFromDB(existingRecostarBaseFolder, recostarBaseFolder,
											tempOutputUnZipDir);
									if (!isSuccess) {
										break;
									}

								}
								continue;
							}
							if (nodeName.equalsIgnoreCase("LearnIndex")) {
								if (!BatchClassUtil.isChecked(node)) {
									// import the image samples from database
									String learnIndexBaseFolder = FileUtils.getFileNameOfTypeFromFolder(tempOutputUnZipDir,
											batchSchemaService.getSearchIndexFolderName());
									String existingLearnIndexBaseFolder = batchSchemaService.getAbsolutePath(existingBatchClass
											.getIdentifier(), batchSchemaService.getSearchIndexFolderName(), false);

									isSuccess = BatchClassUtil.overrideFromDB(existingLearnIndexBaseFolder, learnIndexBaseFolder,
											tempOutputUnZipDir);
									if (!isSuccess) {
										break;
									}

								}
								continue;
							}

						}
					}
				}
			}
			if (isSuccess) {
				// override the existing batch class
				importBatchClass.setId(0L);
				importBatchClass.setUncFolder("dummyUncFolder" + System.currentTimeMillis());

				if (!userOptions.isUseSource()) {
					importBatchClass.setDescription(userOptions.getDescription());
					importBatchClass.setName(userOptions.getName());
					importBatchClass.setPriority(Integer.parseInt(userOptions.getPriority()));
				}

				BatchClassUtil.updateSerializableBatchClass(moduleConfigService, moduleService, pluginService, pluginConfigService, importBatchClass);
				importBatchClass = batchClassService.createBatchClassWithoutWatch(importBatchClass);

				try {
					String dummyUncFolder = existingUncFolder + "-" + existingBatchClass.getIdentifier() + "-deleted";
					existingBatchClass = batchClassService.getBatchClassByIdentifier(existingBatchClass.getIdentifier());
					existingBatchClass.setUncFolder(dummyUncFolder);
					deleteEmailConfigForBatchClass(existingBatchClass);
					existingBatchClass = batchClassService.merge(existingBatchClass);
					try {
						importBatchClass.setUncFolder(existingUncFolder);
						importBatchClass = batchClassService.createBatchClass(importBatchClass);
						try {
							FileUtils.deleteSelectedFilesFromDirectory(tempOutputUnZipDir + File.separator
									+ batchSchemaService.getScriptFolderName(), scriptsList);
							File copiedFolder = new File(batchSchemaService.getBaseSampleFDLock() + File.separator
									+ importBatchClass.getIdentifier());
							FileUtils.copyDirectoryWithContents(originalFolder, copiedFolder);

							List<BatchInstance> newBatchInstances = batchInstanceService.getBatchInstByBatchClass(existingBatchClass);
							for (BatchInstance batchInstance : newBatchInstances) {
								if (batchInstance.getStatus().equals(BatchInstanceStatus.NEW)) {
									batchInstance.setBatchClass(importBatchClass);
									batchInstance.setPriority(importBatchClass.getPriority());
									batchInstanceService.updateBatchInstance(batchInstance);
								}
							}
						} catch (Exception e) {
							log.info("Error while overriding, reverting the changes." + e, e);
							importBatchClass.setDeleted(true);
							importBatchClass = batchClassService.merge(importBatchClass, true);

							existingBatchClass.setUncFolder(existingUncFolder);
							existingBatchClass = batchClassService.merge(existingBatchClass);
						}
						existingBatchClass.setDeleted(true);
						existingBatchClass = batchClassService.merge(existingBatchClass, true);
						new File(dummyUncFolder).mkdirs();

					} catch (Exception e) {
						log.info("Error while overriding, reverting the changes" + e, e);
						existingBatchClass.setUncFolder(existingUncFolder);
						existingBatchClass = batchClassService.merge(existingBatchClass);
					}

				} catch (Exception e) {
					log.info("Error while overriding, reverting the changes" + e, e);
					importBatchClass.setDeleted(true);
					importBatchClass = batchClassService.merge(importBatchClass, true);
				}
			}
		}

		FileUtils.deleteDirectoryAndContentsRecursive(originalFolder);
		new File(userOptions.getZipFileName()).delete();

		return isSuccess;
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
}
