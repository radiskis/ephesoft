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

package com.ephesoft.dcma.gwt.admin.bm.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.ephesoft.dcma.barcodeextraction.BarcodeExtractionReader.BarcodeReaderTypes;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPlugin;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPluginConfiguration;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.Folders;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.Scripts;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.Folders.Folder;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.Scripts.Script;
import com.ephesoft.dcma.batch.service.BatchClassPluginPropertiesService;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.ImportBatchService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.cmis.service.CMISExportService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.common.FileType;
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
import com.ephesoft.dcma.da.domain.Module;
import com.ephesoft.dcma.da.domain.Plugin;
import com.ephesoft.dcma.da.domain.ScannerMasterConfiguration;
import com.ephesoft.dcma.da.domain.TableInfo;
import com.ephesoft.dcma.da.id.BatchClassID;
import com.ephesoft.dcma.da.service.BatchClassEmailConfigService;
import com.ephesoft.dcma.da.service.BatchClassPluginConfigService;
import com.ephesoft.dcma.da.service.BatchClassPluginService;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.da.service.DocumentTypeService;
import com.ephesoft.dcma.da.service.FieldTypeService;
import com.ephesoft.dcma.da.service.MasterScannerService;
import com.ephesoft.dcma.da.service.ModuleService;
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
import com.ephesoft.dcma.gwt.core.shared.EmailConfigurationDTO;
import com.ephesoft.dcma.gwt.core.shared.FieldTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.ImportBatchClassSuperConfig;
import com.ephesoft.dcma.gwt.core.shared.ImportBatchClassUserOptionDTO;
import com.ephesoft.dcma.gwt.core.shared.KVExtractionDTO;
import com.ephesoft.dcma.gwt.core.shared.ModuleDTO;
import com.ephesoft.dcma.gwt.core.shared.OutputDataCarrierDTO;
import com.ephesoft.dcma.gwt.core.shared.PluginDetailsDTO;
import com.ephesoft.dcma.gwt.core.shared.RoleDTO;
import com.ephesoft.dcma.gwt.core.shared.SamplePatternDTO;
import com.ephesoft.dcma.gwt.core.shared.TableInfoDTO;
import com.ephesoft.dcma.gwt.core.shared.TestTableResultDTO;
import com.ephesoft.dcma.gwt.core.shared.exception.GWTException;
import com.ephesoft.dcma.gwt.core.shared.importtree.Node;
import com.ephesoft.dcma.imagemagick.ImageMagicProperties;
import com.ephesoft.dcma.imagemagick.imageclassifier.SampleThumbnailGenerator;
import com.ephesoft.dcma.imagemagick.service.ImageProcessService;
import com.ephesoft.dcma.kvfinder.data.InputDataCarrier;
import com.ephesoft.dcma.kvfinder.data.KeyValueFieldCarrier;
import com.ephesoft.dcma.kvfinder.data.OutputDataCarrier;
import com.ephesoft.dcma.kvfinder.service.KVFinderService;
import com.ephesoft.dcma.lucene.service.SearchClassificationService;
import com.ephesoft.dcma.recostar.RecostarProperties;
import com.ephesoft.dcma.tablefinder.service.TableFinderService;
import com.ephesoft.dcma.tesseract.TesseractProperties;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.EmailConfigurationData;
import com.ephesoft.dcma.util.EmailUtil;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.IUtilCommonConstants;
import com.ephesoft.dcma.util.XMLUtil;
import com.ephesoft.dcma.workflow.service.common.DeploymentService;

/**
 * This interface lists the service methods.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.DCMARemoteServiceServlet
 * @see com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementService
 */
public class BatchClassManagementServiceImpl extends DCMARemoteServiceServlet implements BatchClassManagementService {

	/**
	 * URL_SEPARATOR String.
	 */
	private static final String URL_SEPARATOR = "/";

	/**
	 * UNDERSCORE String.
	 */
	private static final String UNDERSCORE = "_";

	/**
	 * SPACE String.
	 */
	private static final String SPACE = " ";

	/**
	 * ZERO String.
	 */
	private static final String ZERO = "0";

	/**
	 * SERIALIZATION_EXT String.
	 */
	private static final String SERIALIZATION_EXT = FileType.SER.getExtensionWithDot();

	/**
	 * EMAIL_ACCOUNTS String.
	 */
	public static final String EMAIL_ACCOUNTS = "EmailAccounts";

	/**
	 * BATCH_CLASS_DEF String.
	 */
	public static final String BATCH_CLASS_DEF = "BatchClassDefinition";

	/**
	 * SCRIPTS String.
	 */
	public static final String SCRIPTS = "Scripts";

	/**
	 * ROLES String.
	 */
	public static final String ROLES = "Roles";

	/**
	 * PROPERTY_FILE_NAME String.
	 */
	private static final String PROPERTY_FILE_NAME = "application.properties";

	/**
	 * META_INF String.
	 */
	private static final String META_INF = "META-INF";

	/**
	 * ROW_COUNT String.
	 */
	private static final String ROW_COUNT = "row_count";

	/**
	 * AUTHENTICATION_URL String.
	 */
	private static final String AUTHENTICATION_URL = "AUTHENTICATION_URL";

	/**
	 * TICKET String.
	 */
	private static final String TICKET = "{ticket}";

	/**
	 * API_KEY String.
	 */
	private static final String API_KEY = "{key}";

	/**
	 * TEMP_TICKET_URL String.
	 */
	private static final String TEMP_TICKET_URL = "https://www.box.com/api/1.0/rest?action=get_ticket&api_key=" + API_KEY;

	/**
	 * RESPONSE_TICKET String.
	 */
	private static final String RESPONSE_TICKET = "response//ticket";

	/**
	 * RESPONSE_STATUS String.
	 */
	private static final String RESPONSE_STATUS = "response//status";

	/**
	 * RESPONSE_AUTH_TOKEN String.
	 */
	private static final String RESPONSE_AUTH_TOKEN = "response//auth_token";

	/**
	 * GET_TICKET_OK String.
	 */
	private static final String GET_TICKET_OK = "get_ticket_ok";

	/**
	 * GET_AUTH_TOKEN_OK String.
	 */
	private static final String GET_AUTH_TOKEN_OK = "get_auth_token_ok";

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * LOGGER to log info.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchClassManagementServiceImpl.class);

	/**
	 * SAMPLE_PATTERN_PROPERTY_FILE_NAME String.
	 */
	private static final String SAMPLE_PATTERN_PROPERTY_FILE_NAME = "sample-patterns";

	/**
	 * API to get All Batch Classes.
	 * 
	 * @return List<{@link BatchClassDTO}>
	 */
	@Override
	public List<BatchClassDTO> getAllBatchClasses() {
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		List<BatchClass> batchList = batchClassService.getAllUnlockedBatchClasses();
		return convertToBatchClassDTOs(batchList);
	}

	/**
	 * API to update Batch Class given BatchClassDTO.
	 * 
	 * @param batchClassDTO {@link BatchClassDTO}
	 * @return {@link BatchClassDTO}
	 * @throws GWTException
	 */
	@Override
	public BatchClassDTO updateBatchClass(BatchClassDTO batchDTO) throws GWTException {
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		BatchClassPluginService batchClassPluginService = this.getSingleBeanOfType(BatchClassPluginService.class);
		BatchClassPluginConfigService batchClassPluginConfigService = this.getSingleBeanOfType(BatchClassPluginConfigService.class);
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);
		PluginConfigService pluginConfigService = this.getSingleBeanOfType(PluginConfigService.class);
		MasterScannerService scannerMasterService = this.getSingleBeanOfType(MasterScannerService.class);

		BatchClass batchClass = null;
		boolean isBatchClassDirty = false;
		BatchClassDTO batchClassDTO;
		if (batchDTO.isDirty()) {
			isBatchClassDirty = true;
		}

		if (!isBatchClassDirty) {
			Collection<BatchClassModuleDTO> batchClassModules = batchDTO.getModules();
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
			batchClassDTO = batchDTO;
		} else {

			String batchClassIdentifier = batchDTO.getIdentifier();

			batchClass = batchClassService.get(batchClassIdentifier);

			Set<String> groupNameSet = getAllGroups();
			Set<String> superAdminGroupsSet = getAllSuperAdminGroup();
			List<ScannerMasterConfiguration> scannerMasterConfigs = scannerMasterService.getMasterConfigurations();

			checkAndCreateNewSystemFolder(batchClass, batchDTO);

			List<String> docTypeNameList = BatchClassUtil.mergeBatchClassFromDTO(batchClass, batchDTO, groupNameSet,
					batchClassPluginConfigService, batchClassPluginService, pluginConfigService, scannerMasterConfigs,
					superAdminGroupsSet);

			if (null != docTypeNameList) {
				BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
				try {
					batchSchemaService.deleteDocTypeFolder(docTypeNameList, batchClassIdentifier);
				} catch (DCMAApplicationException e) {
					LOGGER.error(e.getMessage());
				}
			}
			batchClass = batchClassService.merge(batchClass, batchClass.isDeleted());
			MasterScannerService masterScannerService = this.getSingleBeanOfType(MasterScannerService.class);
			List<ScannerMasterConfiguration> masterScannerConfig = masterScannerService.getMasterConfigurations();
			batchClassDTO = BatchClassUtil.createBatchClassDTO(masterScannerConfig, batchClass, pluginService);
			batchClassDTO.setDeployed(isBatchClassDeployed(batchClassDTO));
			batchClassDTO.setDirty(false);
		}
		return batchClassDTO;
	}

	private void checkAndCreateNewSystemFolder(BatchClass batchClass, BatchClassDTO batchClassDTO) throws GWTException {
		final String existingSystemFolderPath = batchClass.getSystemFolder();
		final String newSystemFolderPath = batchClassDTO.getSystemFolder();
		File newSystemFolder = new File(newSystemFolderPath);
		if (existingSystemFolderPath == null || (!existingSystemFolderPath.equals(newSystemFolderPath) && !newSystemFolder.exists())) {
			newSystemFolder.mkdirs();
			final boolean isLockAcquired = FileUtils.lockFolder(newSystemFolder.getPath());
			if (!isLockAcquired) {
				String errorMessage = "Could not acquire lock on the system folder.";
				LOGGER.error(errorMessage);
				throw new GWTException(errorMessage);
			}
		}
	}

	/**
	 * API to get Batch Class given batchClassIdentifier.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @return {@link BatchClassDTO}
	 */
	@Override
	public BatchClassDTO getBatchClass(String batchClassIdentifier) {
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);
		BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdentifier);
		MasterScannerService masterScannerService = this.getSingleBeanOfType(MasterScannerService.class);
		List<ScannerMasterConfiguration> masterScannerConfig = masterScannerService.getMasterConfigurations();

		BatchClassDTO batchClassDTO = BatchClassUtil.createBatchClassDTO(masterScannerConfig, batchClass, pluginService);
		Boolean isBatchClassDeployed = isBatchClassDeployed(batchClassDTO);
		batchClassDTO.setDeployed(isBatchClassDeployed);

		if (!isBatchClassDeployed) {
			batchClassDTO.setDirty(true);
		}
		return batchClassDTO;
	}

	/**
	 * API for sample Generation given the batch Class ID's List.
	 * 
	 * @param batchClassIDList List<{@link String}>
	 */
	@Override
	public void sampleGeneration(List<String> batchClassIDList) {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		PageTypeService pageTypeService = this.getSingleBeanOfType(PageTypeService.class);

		List<List<String>> batchIdDocPgNameList = pageTypeService.getDocTypeNameAndPgTypeName(batchClassIDList);
		try {
			batchSchemaService.sampleGeneration(batchIdDocPgNameList);
		} catch (DCMAApplicationException e) {
			LOGGER.error(e.getMessage());
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

	/**
	 * API to learn File For Batch Class given batchClassID.
	 * 
	 * @param batchClassID {@link String}
	 * @throws GWTException
	 */
	@Override
	public void learnFileForBatchClass(String batchClassID) throws GWTException {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		File batchClassFolder = new File(batchSchemaService.getBaseFolderLocation() + File.separator + batchClassID);
		if (!batchClassFolder.exists()) {
			return;
		}
		SearchClassificationService searchClassificationService = this.getSingleBeanOfType(SearchClassificationService.class);
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassID);
		String ocrPluginName = getOCRPluginNameForBatchClass(batchClassID);
		try {
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
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new GWTException(e.getMessage(), e);
		}

	}

	/**
	 * To acquire lock on the specified batch class.
	 * 
	 * @param batchClassIdentifier String
	 * @throws GWTException
	 */
	@Override
	public void acquireLock(String batchClassIdentifier) throws GWTException {
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		try {
			batchClassService.acquireBatchClass(batchClassIdentifier, getUserName());
		} catch (BatchAlreadyLockedException e) {
			LOGGER.error(e.getMessage());
			throw new GWTException(e.getMessage(), e);
		}
	}

	/**
	 * Clean up module.
	 */
	@Override
	public void cleanup() {
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		batchClassService.unlockAllBatchClassesForCurrentUser(getUserName());
	}

	/**
	 * API to get All columns of a Table given the DB configuration.
	 * 
	 * @param driverName {@link String}
	 * @param url {@link String}
	 * @param userName {@link String}
	 * @param password {@link String}
	 * @param tableName {@link String}
	 * @return Map<{@link String}, {@link String}>
	 * @throws GWTException
	 */
	@Override
	public Map<String, String> getAllColumnsForTable(String driverName, String url, String userName, String password, String tableName)
			throws GWTException {
		Map<String, String> map = new HashMap<String, String>();
		try {
			DynamicHibernateDao dao = new DynamicHibernateDao(userName, password, driverName, url);
			List<ColumnDefinition> columnNames = dao.getAllColumnsForTable(tableName);
			for (ColumnDefinition columnDefinition : columnNames) {
				map.put(columnDefinition.getColumnName(), columnDefinition.getType().getName());
			}
			dao.closeSession();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new GWTException(e.getMessage(), e);
		}
		return map;
	}

	/**
	 * API to get Document Level Fields given the document Name and batch Class Id.
	 * 
	 * @param documentName {@link String}
	 * @param batchClassId {@link String}
	 * @return Map<{@link String}, {@link String}>
	 */
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

	/**
	 * API to learn DataBase for a given batch class id depending on create Index.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param createIndex boolean
	 * @throws GWTException
	 */
	@Override
	public void learnDataBase(String batchClassIdentifier, boolean createIndex) throws GWTException {
		try {
			BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			File batchClassFolder = new File(batchSchemaService.getBaseFolderLocation() + File.separator + batchClassIdentifier);
			if (!batchClassFolder.exists()) {
				return;
			}
			BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
			BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdentifier);
			FuzzyDBSearchService fuzzyDBSearchService = this.getSingleBeanOfType(FuzzyDBSearchService.class);
			fuzzyDBSearchService.learnDataBase(batchClass.getBatchClassID(), createIndex);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new GWTException(e.getMessage(), e);
		}
	}

	/**
	 * API to get All Tables given the DB configuration.
	 * 
	 * @param driverName {@link String}
	 * @param url {@link String}
	 * @param userName {@link String}
	 * @param password {@link String}
	 * @return Map<{@link String}, List<{@link String}>>
	 * @throws GWTException
	 */
	@Override
	public Map<String, List<String>> getAllTables(String driverName, String url, String userName, String password) throws GWTException {
		Map<String, List<String>> tableNames = null;
		try {
			DynamicHibernateDao dao = new DynamicHibernateDao(userName, password, driverName, url);
			tableNames = dao.getAllTableNames();
			dao.closeSession();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new GWTException(e.getMessage(), e);
		}
		return tableNames;
	}

	/**
	 * API to copy Batch Class given a BatchClassDTO.
	 * 
	 * @param batchClassDTO {@link BatchClassDTO}
	 * @throws GWTException
	 */
	@Override
	public void copyBatchClass(BatchClassDTO batchClassDTO) throws GWTException {
		try {
			BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
			PluginService pluginService = this.getSingleBeanOfType(PluginService.class);
			MasterScannerService masterScannerService = this.getSingleBeanOfType(MasterScannerService.class);
			BatchClass batchClass = batchClassService.getLoadedBatchClassByIdentifier(batchClassDTO.getIdentifier());
			List<ScannerMasterConfiguration> masterScannerConfig = masterScannerService.getMasterConfigurations();
			BatchClassUtil.createBatchClassDTO(masterScannerConfig, batchClass, pluginService);
			batchClassService.evict(batchClass);
			checkAndCreateNewSystemFolder(batchClass, batchClassDTO);
			copyAndSaveBatchClass(batchClassDTO, batchClassService, batchClass);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new GWTException(e.getMessage());
		}
	}

	/**
	 * API to get Batch Folder List.
	 * 
	 * @return {@link BatchClassDTO}
	 */
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

	private void copyAndSaveBatchClass(BatchClassDTO batchClassDTO, BatchClassService batchClassService, BatchClass paramBatchClass)
			throws GWTException {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		String identifier = paramBatchClass.getIdentifier();
		setBatchClassInfo(batchClassDTO, paramBatchClass);
		BatchClassUtil.copyModules(paramBatchClass);
		BatchClassUtil.copyDocumentTypes(paramBatchClass);
		BatchClassUtil.copyScannerConfig(paramBatchClass);
		BatchClassUtil.copyBatchClassField(paramBatchClass);

		File originalFolder = new File(batchSchemaService.getBaseSampleFDLock() + File.separator + identifier);
		BatchClass batchClass = batchClassService.createBatchClass(paramBatchClass);
		batchClassService.evict(batchClass);
		File copiedFolder = new File(batchSchemaService.getBaseSampleFDLock() + File.separator + batchClass.getIdentifier());
		try {
			FileUtils.copyDirectoryWithContents(originalFolder, copiedFolder);
		} catch (IOException e) {
			throw new GWTException("Unable to create learning folders");
		}
		// Deploy Workflow
		deployNewBatchClass(batchClass.getIdentifier());
	}

	private void deployNewBatchClass(String identifier) {
		LOGGER.info("Deploying the newly copied batch class");
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		DeploymentService deploymentService = this.getSingleBeanOfType(DeploymentService.class);
		BatchClass batchClass = batchClassService.getLoadedBatchClassByIdentifier(identifier);
		renameBatchClassModules(batchClass);
		batchClass = batchClassService.merge(batchClass);
		deploymentService.createAndDeployBatchClassJpdl(batchClass);
	}

	/**
	 * To update batch class.
	 * 
	 * @param batchClass BatchClass
	 */
	public void updatingBatchClass(BatchClass batchClass) {
		List<DocumentType> documentTypes = batchClass.getDocumentTypes();

		for (DocumentType doctype : documentTypes) {
			doctype.setBatchClass(batchClass);
		}

	}

	/**
	 * API to rename the modules of given batch class (appending module names with batch class identifier).
	 * 
	 * @param batchClass BatchClass
	 */
	private void renameBatchClassModules(BatchClass batchClass) {
		String existingBatchClassIdentifier = batchClass.getIdentifier();
		for (BatchClassModule batchClassModule : batchClass.getBatchClassModules()) {
			String existingModuleName = batchClassModule.getModule().getName();
			StringBuffer newWorkflowNameStringBuffer = new StringBuffer();
			newWorkflowNameStringBuffer.append(existingModuleName.replaceAll(SPACE, UNDERSCORE));
			newWorkflowNameStringBuffer.append(UNDERSCORE);
			newWorkflowNameStringBuffer.append(existingBatchClassIdentifier);
			batchClassModule.setWorkflowName(newWorkflowNameStringBuffer.toString());
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
					+ e.getMessage(), e);
		}
		batchClass.setUncFolder(batchClassDTO.getUncFolder());
		batchClass.setName(batchClassDTO.getName());
		batchClass.setId(0);
		batchClass.setIdentifier(null);
		batchClass.setCurrentUser(null);
		batchClass.setVersion(AdminConstants.VERSION);
		batchClass.setEmailConfigurations(null);
		batchClass.setSystemFolder(batchClassDTO.getSystemFolder());
		List<BatchClassGroups> batchClassGroupsList = batchClass.getAssignedGroups();
		for (BatchClassGroups batchClassGroups : batchClassGroupsList) {
			batchClassGroups.setId(0);
		}
		batchClass.setAssignedGroups(batchClassGroupsList);
		batchClass.setDeleted(false);
	}

	/**
	 * To get Batch Classes.
	 * 
	 * @param firstResult int
	 * @param maxResults int
	 * @param order Order
	 * @return List<BatchClassDTO>
	 */
	@Override
	public List<BatchClassDTO> getBatchClasses(int firstResult, int maxResults, Order order) {
		Set<String> userRoles = getUserRoles();
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		List<BatchClass> batchList = batchClassService.getBatchClassList(firstResult, maxResults, order, userRoles);
		return convertToBatchClassDTOs(batchList);
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
			batchClassDTO.setSystemFolder(batchClass.getSystemFolder());
			batchClassDTOs.add(batchClassDTO);
		}
		return batchClassDTOs;
	}

	/**
	 * API to count All Batch Classes Excluding the one's Deleted.
	 * 
	 * @return int
	 */
	@Override
	public int countAllBatchClassesExcludeDeleted() {
		Set<String> userRoles = null;
		if (!isSuperAdmin()) {
			userRoles = getUserRoles();
		}
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		return batchClassService.countAllBatchClassesExcludeDeleted(userRoles);
	}

	/**
	 * API to create Unc Folder given the path.
	 * 
	 * @param path {@link String}
	 * @throws GWTException
	 */
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

	/**
	 * API to get Project Files For Document Type given the batch Class Identifier and document Type Name.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param documentTypeName {@link String}
	 * @return List<{@link String}>
	 * @throws GWTException
	 */
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

	/**
	 * API to test Key Value Extraction. Based on the flag either KV or Advanced Key Value is invoked.
	 * 
	 * @param batchClassDTO {@link BatchClassDTO}
	 * @param kvExtractionDTO {@link KVExtractionDTO}
	 * @param isTestAdvancedKV
	 * @param testImageName {@link String} Path of image file. Used only if isTestAdvancedKV flag is set to true.
	 * @return list<{@link OutputDataCarrierDTO}>
	 * @throws GWTException
	 */
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
				Map<String, KeyValueFieldCarrier> fieldTypeKVMap = new HashMap<String, KeyValueFieldCarrier>();
				extractKeyValueRecursively(batchClassDTO, kvExtractionDTO, batchSchemaService, kVFinderService, outputFilePaths,
						inputDataCarrierList, carrierDTOs, carrierDTOsNoResult, ocrEngineName, file, fieldTypeKVMap);
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

	private void extractKeyValueRecursively(BatchClassDTO batchClassDTO, KVExtractionDTO kvExtractionDTO,
			BatchSchemaService batchSchemaService, KVFinderService kVFinderService, List<String> outputFilePaths,
			List<InputDataCarrier> inputDataCarrierList, List<OutputDataCarrierDTO> carrierDTOs,
			List<OutputDataCarrierDTO> carrierDTOsNoResult, String ocrEngineName, File file,
			Map<String, KeyValueFieldCarrier> fieldTypeKVMap) throws DCMAException {
		LOGGER.info("Entering method extractKeyValueRecursively....");
		String fieldType = kvExtractionDTO.getFieldTypeDTO().getName();
		KeyValueFieldCarrier keyValueFieldCarrier = fieldTypeKVMap.get(fieldType);
		if (keyValueFieldCarrier == null) {
			keyValueFieldCarrier = new KeyValueFieldCarrier();
			fieldTypeKVMap.put(fieldType, keyValueFieldCarrier);
		}
		boolean useExisitingKey = kvExtractionDTO.isUseExistingKey();
		LOGGER.info("Use existing key : " + useExisitingKey);
		if (useExisitingKey) {
			FieldTypeDTO fieldTypeDTO = kvExtractionDTO.getFieldTypeDTO().getDocTypeDTO().getFieldTypeByName(
					kvExtractionDTO.getKeyPattern());
			List<KVExtractionDTO> kvExtractionDTOList = fieldTypeDTO.getKvExtractionList();
			for (KVExtractionDTO keyValueExtractionDTO : kvExtractionDTOList) {
				List<InputDataCarrier> localInputDataCarrierList = new ArrayList<InputDataCarrier>();
				InputDataCarrier inputDataCarrier = BatchClassUtil.createInputDataCarrierFromKVExtDTO(keyValueExtractionDTO);
				localInputDataCarrierList.add(inputDataCarrier);
				extractKeyValueRecursively(batchClassDTO, keyValueExtractionDTO, batchSchemaService, kVFinderService, outputFilePaths,
						localInputDataCarrierList, carrierDTOs, carrierDTOsNoResult, ocrEngineName, file, fieldTypeKVMap);
			}
		}
		carrierDTOsNoResult.clear();
		carrierDTOs.clear();
		for (String outputFilePath : outputFilePaths) {
			File inputFile = new File(outputFilePath);
			HocrPage hocrPage = batchSchemaService.generateHocrPage(inputFile.getName(), outputFilePath, file.getPath(), batchClassDTO
					.getIdentifier(), ocrEngineName);
			List<OutputDataCarrier> outputDataCarrierList = kVFinderService.findKeyValue(inputDataCarrierList, hocrPage,
					fieldTypeKVMap, keyValueFieldCarrier, Integer.MAX_VALUE);
			BatchClassUtil.createOutputDataDTOFromOutputDataCarrier(outputDataCarrierList, carrierDTOs, carrierDTOsNoResult, inputFile
					.getName());
		}
		LOGGER.info("Exiting method extractKeyValueRecursively....");
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
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		final BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdentifier);
		if (batchClassIdentifier != null && !batchClassIdentifier.isEmpty()) {
			List<BatchClassPlugin> batchClassPlugins = getOrderedListOfPlugins(batchClass);
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
		LOGGER.info("Default ocr plugin to be used in case both ");
		return ocrEngine;
	}

	private List<BatchClassPlugin> getOrderedListOfPlugins(BatchClass batchClass) {
		List<BatchClassPlugin> allBatchClassPlugins = null;

		if (batchClass != null) {
			final List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
			if (batchClassModules != null) {

				if (allBatchClassPlugins == null) {
					allBatchClassPlugins = new ArrayList<BatchClassPlugin>();
				}
				for (BatchClassModule batchClassModule : batchClassModules) {
					List<BatchClassPlugin> batchClassPlugins = batchClassModule.getBatchClassPlugins();
					allBatchClassPlugins.addAll(batchClassPlugins);
				}
			}
		}
		return allBatchClassPlugins;
	}

	/**
	 * API to get All Roles available.
	 * 
	 * @return List<{@link RoleDTO}>
	 */
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

	/**
	 * API to get All Barcode Types available.
	 * 
	 * @return List<{@link String}>
	 */
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

	/**
	 * 
	 * API to delete Batch Class given the BatchClassDTO.
	 * 
	 * @param batchClassDTO {@link BatchClassDTO}
	 * @return {@link BatchClassDTO}
	 */
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
		MasterScannerService masterScannerService = this.getSingleBeanOfType(MasterScannerService.class);
		List<ScannerMasterConfiguration> masterScannerConfig = masterScannerService.getMasterConfigurations();
		return BatchClassUtil.createBatchClassDTO(masterScannerConfig, batchClass, pluginService);
	}

	private void deleteEmailConfigForBatchClass(final BatchClass batchClass) {
		BatchClassEmailConfigService batchClassEmailConfigService = this.getSingleBeanOfType(BatchClassEmailConfigService.class);
		List<BatchClassEmailConfiguration> emailConfigList = batchClassEmailConfigService
				.getEmailConfigByBatchClassIdentifier(batchClass.getIdentifier());
		for (BatchClassEmailConfiguration batchClassEmailConfiguration : emailConfigList) {
			batchClassEmailConfigService.removeEmailConfiguration(batchClassEmailConfiguration);
		}
	}

	/**
	 * API to match Base Folder to given unc folder path.
	 * 
	 * @param uncFolder {@link String}
	 * @return {@link String}
	 */
	@Override
	public String matchBaseFolder(String uncFolder) {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		return String.valueOf(BatchClassUtil.matchBaseFolder(uncFolder, batchSchemaService.getBaseFolderLocation()));
	}

	/**
	 * API to copy the Document given the documentTypeDTO.
	 * 
	 * @param documentTypeDTO {@link DocumentTypeDTO}
	 * @throws Exception
	 */
	@Override
	public DocumentTypeDTO copyDocument(DocumentTypeDTO documentTypeDTO) throws GWTException {
		DocumentTypeService documentTypeService = this.getSingleBeanOfType(DocumentTypeService.class);
		DocumentType documentType = documentTypeService.getDocTypeByIdentifier(documentTypeDTO.getIdentifier());
		BatchClassUtil.createDocumentTypeDTO(documentTypeDTO.getBatchClass(), documentType);
		documentTypeService.evict(documentType);
		copyAndSaveDocumentType(documentTypeDTO, documentType);
		return BatchClassUtil.createDocumentTypeDTO(documentTypeDTO.getBatchClass(), documentType);
	}

	/**
	 * This method copy document type data to the passed document type.
	 * 
	 * @param documentTypeDTO DTO for copied document type
	 * @param documentTypeService instance of DocumentTypeService
	 * @param documentType document type for and to be copied into
	 * @throws GWTException on error
	 */
	private void copyAndSaveDocumentType(DocumentTypeDTO documentTypeDTO, DocumentType documentType) throws GWTException {
		setDocumentTypeInfo(documentTypeDTO, documentType);
		BatchClassUtil.changePageTypeForNewDocument(documentType);
		BatchClassUtil.copyFieldTypesForCopyDocument(documentType);
		BatchClassUtil.copyTableInfo(documentType);
		BatchClassUtil.copyFunctionKeysForCopyDocument(documentType);
	}

	private void setDocumentTypeInfo(DocumentTypeDTO documentTypeDTO, DocumentType documentType) {
		documentType.setName(documentTypeDTO.getName());
		documentType.setDescription(documentTypeDTO.getDescription());
		documentType.setMinConfidenceThreshold(documentTypeDTO.getMinConfidenceThreshold());
		documentType.setHidden(documentTypeDTO.isHidden());
		documentType.setId(0);
	}

	/**
	 * API to get Advanced Key Value Image Upload Path given the image name for a particular batch class id.
	 * 
	 * @param batchClassId {link String}
	 * @param docName {link String}
	 * @param dlfName {link String}
	 * @param kvIdentifier {link String}
	 * @param imageName {link String}
	 * @return String
	 */
	@Override
	public String getAdvancedKVImageUploadPath(String batchClassId, String docName, String imageName) {

		LOGGER.info("Getting image url for image = " + imageName);
		String imageUrl = null;
		if (batchClassId != null && docName != null) {
			String localBatchClassId = batchClassId.trim();
			String localDocName = docName.trim();
			BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			String testExtractionFolderPath = batchSchemaService.getTestAdvancedKvExtractionFolderPath(localBatchClassId, true);
			File imageFile = new File(testExtractionFolderPath + File.separator + localDocName + File.separator + imageName);
			if (imageFile.exists()) {
				imageUrl = batchSchemaService.getBaseHttpURL() + URL_SEPARATOR + localBatchClassId + URL_SEPARATOR
						+ batchSchemaService.getTestAdvancedKVExtractionFolderName() + URL_SEPARATOR + localDocName + URL_SEPARATOR
						+ imageName;
			}
		}
		return imageUrl;

	}

	/**
	 * To test Table Pattern.
	 * 
	 * @param batchClassDTO BatchClassDTO
	 * @param tableInfoDTO TableInfoDTO
	 * @return List<TestTableResultDTO>
	 * @throws GWTException
	 */
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

	/**
	 * API to match the work flow content for equality with same module(s) and their sequence and also same plugin(s) and sequence.
	 * 
	 * @param userOptions ImportBatchClassUserOptionDTO
	 * @param userInputWorkflowName String
	 * @return Map<String, Boolean>
	 */
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

	/**
	 * API to get import BatchClass UI Configuration given the workflow Name and zip Source Path.
	 * 
	 * @param workflowName {@link String}
	 * @param zipSourcePath {@link String}
	 * @return {@link ImportBatchClassSuperConfig}
	 */
	@Override
	public ImportBatchClassSuperConfig getImportBatchClassUIConfig(String workflowName, String zipSourcePath) {
		ImportBatchClassSuperConfig config = new ImportBatchClassSuperConfig();
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);

		List<String> uncFolders = batchClassService.getAllUNCList(true);
		BatchClass dbBatchClass;
		Set<String> batchClassesByUserRoles = getAllBatchClassByUserRoles();
		for (String uncFolder : uncFolders) {
			dbBatchClass = batchClassService.getBatchClassbyUncFolder(uncFolder);
			if (dbBatchClass != null && dbBatchClass.getIdentifier() != null
					&& batchClassesByUserRoles.contains(dbBatchClass.getIdentifier())) {
				config.addUncFolderConfig(dbBatchClass.getIdentifier(), dbBatchClass.getName(), uncFolder);
			}
		}
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

	/**
	 * API to get import BatchClass UI Configuration given the workflow Name and zip Source Path.
	 * 
	 * @param workflowName {@link String}
	 * @param zipSourcePath {@link String}
	 * @return {@link ImportBatchClassSuperConfig}
	 */
	@Override
	public boolean importBatchClass(ImportBatchClassUserOptionDTO userOptions) throws GWTException {
		boolean isSuccess = true;
		Map<Boolean, String> importResults = new HashMap<Boolean, String>();
		try {
			ImportBatchService importService = this.getSingleBeanOfType(ImportBatchService.class);
			ImportBatchClassOptions optionXML = new ImportBatchClassOptions();
			convertDTOtoXMLForImport(userOptions, optionXML);
			String firstRoleOfUser = null;
			Set<String> groupsToAssign = getAllSuperAdminGroup();
			if (!isSuperAdmin()) {
				Set<String> userRoles = getUserRoles();
				if (userRoles != null) {
					for (String userRole : userRoles) {
						firstRoleOfUser = userRole;
						groupsToAssign.add(firstRoleOfUser);
						break;
					}
				}
			}
			importResults = importService.importBatchClass(optionXML, userOptions.isWorkflowDeployed(), false, groupsToAssign);
		} catch (Exception e) {
			String errorMessg = "Error while importing." + e.getMessage();
			importResults.put(false, errorMessg);
			LOGGER.error(errorMessg, e);
		}
		isSuccess = !importResults.containsKey(false);
		if (isSuccess) {
			final String batchClassId = importResults.get(Boolean.TRUE);
			BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
			batchClassService.evict(batchClassService.getLoadedBatchClassByIdentifier(batchClassId));
			deployNewBatchClass(batchClassId);
		}
		return isSuccess;
	}

	private void convertDTOtoXMLForImport(ImportBatchClassUserOptionDTO userOptions, ImportBatchClassOptions optionXML) {
		optionXML.setZipFilePath(userOptions.getZipFileName());
		optionXML.setUncFolder(userOptions.getUncFolder());
		optionXML.setUseExisting(userOptions.isImportExisting());
		optionXML.setUseSource(false);
		optionXML.setName(userOptions.getName());
		optionXML.setDescription(userOptions.getDescription());
		optionXML.setPriority(Integer.parseInt(userOptions.getPriority()));

		optionXML.setSystemFolder(userOptions.getSystemFolder());

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
						if (parentName.toLowerCase(Locale.getDefault()).indexOf("script") > -1) {
							Script script = new Script();
							script.setFileName(node.getLabel().getKey());
							script.setSelected(BatchClassUtil.isChecked(node));
							bcdScriptList.add(script);
						}
						if (parentName.toLowerCase(Locale.getDefault()).indexOf("module") > -1) {
							com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.BatchClassModules.BatchClassModule bcm = new com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.BatchClassModules.BatchClassModule();
							bcm.setModuleName(node.getLabel().getKey());
							bcm.setPluginConfiguration(BatchClassUtil.isChecked(node));
							bcdModuleList.add(bcm);
						}
						if (parentName.toLowerCase(Locale.getDefault()).indexOf("folder") > -1) {
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

	/**
	 * API to get All BatchClasses Including the one's Deleted.
	 * 
	 * @return List<{@link BatchClassDTO}>
	 */
	@Override
	public List<BatchClassDTO> getAllBatchClassesIncludingDeleted() {
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		List<BatchClass> batchList = batchClassService.getAllBatchClasses();
		return convertToBatchClassDTOs(batchList);
	}

	/**
	 * API to delete Attached Folders given zip File Name.
	 * 
	 * @param zipFileName {@link String}
	 */
	@Override
	public void deleteAttachedFolders(String zipFileName) {
		if (zipFileName != null && !zipFileName.isEmpty() && new File(zipFileName).exists()) {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(zipFileName));
		}
	}

	/**
	 * API to get All Primary Keys For Table given DB config and table name.
	 * 
	 * @param driverName {@link String}
	 * @param url {@link String}
	 * @param userName {@link String}
	 * @param password {@link String}
	 * @param table {@link String}
	 * @param tableType {@link String}
	 * @return List<{@link String}>
	 * @throws GWTException
	 */
	@Override
	public List<String> getAllPrimaryKeysForTable(String driverName, String url, String userName, String password, String table,
			String tableType) throws GWTException {
		List<String> keyList = null;
		try {
			LOGGER.info("Getting primary keys for the table : " + table + " of type " + tableType);
			LOGGER.info("Connecting to the database using the following settings: ");
			LOGGER.info("Driver name: " + driverName);
			LOGGER.info("URL: " + url);
			LOGGER.info("User Name: " + userName);
			LOGGER.info("Password: " + password);
			DynamicHibernateDao dao = new DynamicHibernateDao(userName, password, driverName, url);
			keyList = dao.getPrimaryKeysForTable(table, tableType);
			LOGGER.info("Primary keys found are: " + keyList.toString());
			dao.closeSession();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new GWTException(e.getMessage());
		}
		return keyList;
	}

	/**
	 * API to get BatchClass Row Count.
	 * 
	 * @return {@link String}
	 * @throws GWTException
	 */
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
			LOGGER.error(e.getMessage());
		}
		return rowCount;
	}

	/**
	 * API to get list of all plug-ins.
	 * 
	 * @return {@link List} <{@link PluginDetailsDTO}>
	 */
	@Override
	public List<PluginDetailsDTO> getAllPluginDetailDTOs() {
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);
		List<PluginDetailsDTO> allPluginDetailsDTO = new ArrayList<PluginDetailsDTO>();
		for (Plugin plugin : pluginService.getAllPlugins()) {
			PluginDetailsDTO pluginDetailsDTO = BatchClassUtil.createPluginDetailsDTO(plugin, pluginService);
			allPluginDetailsDTO.add(pluginDetailsDTO);

		}

		return allPluginDetailsDTO;
	}

	/**
	 * API to create and deploy the JPDL's for the new workflow created.
	 * 
	 * @param workflowName {@link String}
	 * @param batchClassDTO {@link BatchClassDTO}
	 */
	@Override
	public BatchClassDTO createAndDeployWorkflowJPDL(String workflowName, BatchClassDTO paramBatchClassDTO) throws GWTException {

		LOGGER.info("Saving Batch Class before deploying");
		BatchClassDTO batchClassDTO = updateBatchClass(paramBatchClassDTO);

		DeploymentService deploymentService = this.getSingleBeanOfType(DeploymentService.class);
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);

		BatchClass batchClass = new BatchClass();
		String batchClassIdentifier = batchClassDTO.getIdentifier();
		batchClass = batchClassService.get(batchClassIdentifier);

		deploymentService.createAndDeployBatchClassJpdl(batchClass);

		batchClassDTO.setDirty(false);
		return batchClassDTO;
	}

	private Boolean isBatchClassDeployed(BatchClassDTO batchClassDTO) {
		Boolean isBatchClassDeployed = false;
		DeploymentService deploymentService = this.getSingleBeanOfType(DeploymentService.class);
		LOGGER.info("Checking if the batch class with identifier as :" + batchClassDTO.getIdentifier() + " is deployed or not");
		isBatchClassDeployed = deploymentService.isDeployed(batchClassDTO.getName());
		LOGGER.info("Batch class with identifier  is deployed :" + batchClassDTO.getIdentifier());
		return isBatchClassDeployed;

	}

	/**
	 * API to get the test the advanced kv extraction results.
	 * 
	 * @param batchClassDTO {@link BatchClassDTO}
	 * @param kvExtractionDTO {@link KVExtractionDTO}
	 * @param docName {@link String}
	 * @param imageName {@link String}
	 * @return List<OutputDataCarrierDTO>
	 * @throws GWTException
	 */
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

	/**
	 * This API gets the updated file name in test-advanced-extraction folder.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param docName {@link String}
	 * @param fileName {@link String}
	 * @return {@link String}
	 */
	@Override
	public String getUpdatedTestFileName(final String batchClassIdentifier, final String docName, final String fileName) {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		String updatedFileName = fileName;
		// Get path upto test-advanced-extraction folder.
		String testKVFolderPath = batchSchemaService.getTestAdvancedKvExtractionFolderPath(batchClassIdentifier, true);
		StringBuilder testKVFolderPathWithDocName = new StringBuilder();
		testKVFolderPathWithDocName.append(testKVFolderPath);
		testKVFolderPathWithDocName.append(File.separator);
		testKVFolderPathWithDocName.append(docName);
		testKVFolderPath = testKVFolderPathWithDocName.toString();
		LOGGER.info("Test folder path = " + testKVFolderPath);
		File testImageFile = new File(testKVFolderPath + File.separator + fileName);

		File htmlFile = new File(testKVFolderPath + File.separator
				+ FileUtils.changeFileExtension(testImageFile.getName(), FileType.HTML.getExtension()));
		File xmlFile = new File(testKVFolderPath + File.separator
				+ FileUtils.changeFileExtension(testImageFile.getName(), FileType.HTML.getExtension()));
		if (!testImageFile.exists() || !htmlFile.exists() || !xmlFile.exists()) {
			int indexOf = fileName.lastIndexOf(BatchClassManagementConstants.DOT);
			String fileExtension = fileName.substring(indexOf, fileName.length());
			updatedFileName = fileName.substring(0, indexOf) + BatchClassManagementConstants.FILE_CONVERSION_SUFFIX + fileExtension;
		}
		return updatedFileName;
	}

	/**
	 * API to get the sample regular expressions list defined in a properties file.
	 * 
	 * @return List of sample patterns
	 * @throws GWTException
	 */
	@Override
	public SamplePatternDTO getSamplePatterns() throws GWTException {
		Properties samplePatternProperties = null;
		try {
			samplePatternProperties = ApplicationConfigProperties.getApplicationConfigProperties().getAllProperties(
					SAMPLE_PATTERN_PROPERTY_FILE_NAME);
		} catch (IOException ioException) {
			LOGGER.error("Unable to read sample pattern properties file.", ioException);
			throw new GWTException("Unable to read sample pattern properties file.");
		}
		SamplePatternDTO samplePattern = new SamplePatternDTO();
		if (samplePatternProperties != null && (!samplePatternProperties.isEmpty())) {
			BatchClassUtil.createSamplePatternDTO(samplePattern, samplePatternProperties);
		}
		return samplePattern;
	}

	/**
	 * API to get super admin.
	 * 
	 * @return {@link Boolean}
	 */
	@Override
	public Boolean isUserSuperAdmin() {
		return isSuperAdmin();
	}

	/**
	 * API to get all roles of a user.
	 * 
	 * @return HashSet<{@link String}>
	 */
	@Override
	public Set<String> getAllRolesOfUser() {
		return getUserRoles();
	}

	/**
	 * API to get the span list for the provided .
	 * 
	 * @param batchClassId {@link String}
	 * @param docName {@link String}
	 * @param hocrFileName {@link String}
	 * @return {@link List} <{@link Span}>
	 * @throws GWTException if any exception or error occur
	 */
	@Override
	public List<Span> getSpanList(String batchClassId, String docName, String hocrFileName) {
		List<Span> spanList = null;
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		String testExtractionFolderPath = batchSchemaService.getTestAdvancedKvExtractionFolderPath(batchClassId, true);
		String hocrFilePath = testExtractionFolderPath + File.separator + docName + File.separator + hocrFileName;
		File file = new File(testExtractionFolderPath + File.separator + "tempFile");
		String ocrEngineName = getOCRPluginNameForBatchClass(batchClassId);
		File hocrFile = new File(hocrFilePath);
		if (hocrFile.exists()) {
			HocrPage hocrPage = batchSchemaService.generateHocrPage(hocrFileName, hocrFilePath, file.getPath(), batchClassId,
					ocrEngineName);
			if (hocrPage != null) {
				spanList = hocrPage.getSpans().getSpan();
			}
		}
		return spanList;
	}

	/**
	 * API to get Advanced test table upload path given the image name for a particular batch class id.
	 * 
	 * @param batchClassId {@link String}
	 * @param docName {@link String}
	 * @param dlfName {@link String}
	 * @param kvIdentifier {@link String}
	 * @param imageName {@link String}
	 * @return {@link String}
	 */
	@Override
	public String getAdvancedTEImageUploadPath(String batchClassId, String docName, String imageName) {
		LOGGER.info("Entering method getAdvancedTEImageUploadPath....");
		String imageUrl = null;
		String localBatchClassId = batchClassId;
		String localDocName = docName;
		if (localBatchClassId != null && localDocName != null && imageName != null) {
			localBatchClassId = localBatchClassId.trim();
			localDocName = localDocName.trim();
			LOGGER.debug("Getting image url for image = " + imageName);
			BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			imageUrl = batchSchemaService.getBaseHttpURL() + URL_SEPARATOR + localBatchClassId + URL_SEPARATOR
					+ batchSchemaService.getAdvancedTestTableFolderName() + URL_SEPARATOR + localDocName + URL_SEPARATOR + imageName;
			LOGGER.debug("Image url path : " + imageUrl);
		}
		LOGGER.info("Exiting method getAdvancedTEImageUploadPath....");
		return imageUrl;

	}

	/**
	 * API to get the names of available modules.
	 * 
	 * @return {@link List}< {@link String}>
	 */
	@Override
	public List<ModuleDTO> getAllModules() {
		LOGGER.info("Getting list of modules.");
		ModuleService moduleService = this.getSingleBeanOfType(ModuleService.class);
		List<Module> modulesList = moduleService.getAllModules();
		List<ModuleDTO> moduleDTOsList = new ArrayList<ModuleDTO>();
		for (Module module : modulesList) {
			LOGGER.info("Adding " + module.getName() + " module to the list.");
			moduleDTOsList.add(BatchClassUtil.createModuleDTO(module));
		}
		return moduleDTOsList;
	}

	/**
	 * API to create a new module.
	 * 
	 * @param moduleDTO {@link ModuleDTO}
	 * @throws GWTException
	 */
	@Override
	public ModuleDTO createNewModule(ModuleDTO moduleDTO) throws GWTException {
		LOGGER.info("Creating new Module with name: " + moduleDTO);
		ModuleService moduleService = this.getSingleBeanOfType(ModuleService.class);
		Module module = new Module();
		String moduleName = moduleDTO.getName();
		Module checkModule = moduleService.getModuleByName(moduleName);
		if (checkModule == null) {
			BatchClassUtil.mergeModuleFromDTO(module, moduleDTO);
			moduleService.createNewModule(module);
		} else {
			throw new GWTException("A module with same name already exists. Please try another name.");
		}
		return BatchClassUtil.createModuleDTO(module);
	}

	/**
	 * API to retrieve names of all the plugins.
	 * 
	 * @return {@link List}< {@link String}>
	 */
	@Override
	public List<String> getAllPluginsNames() {
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);
		return pluginService.getAllPluginsNames();
	}

	/**
	 * API to validate email configuration.
	 * 
	 * @param emailConfigDTO {@link EmailConfigurationDTO} Email Configuration to be verified.
	 * @return {@link Boolean} returns true if valid email configuration otherwise false.
	 * @throws {@link GWTException}
	 */
	@Override
	public Boolean validateEmailConfig(final EmailConfigurationDTO emailConfigDTO) throws GWTException {
		LOGGER.info("Entering method validateEmailConfig.");
		EmailConfigurationData emailConfigData = createEmailConfigData(emailConfigDTO);
		boolean isValid = false;
		try {
			isValid = EmailUtil.testEmailConfiguration(emailConfigData);
			if (!isValid) {
				throw new GWTException("Unable to connect to email configuration. See server logs for details.");
			}
		} catch (AuthenticationFailedException authExcep) {
			throw new GWTException(authExcep.getMessage());
		} catch (MessagingException messageException) {
			throw new GWTException(messageException.getMessage());
		}
		LOGGER.info("Exiting method validateEmailConfig.");
		return isValid;
	}

	private EmailConfigurationData createEmailConfigData(final EmailConfigurationDTO emailConfigDTO) {
		EmailConfigurationData emailConfigData = new EmailConfigurationData();
		if (emailConfigDTO != null) {
			emailConfigData.setUserName(emailConfigDTO.getUserName());
			emailConfigData.setPassword(emailConfigDTO.getPassword());
			emailConfigData.setFolderName(emailConfigDTO.getFolderName());
			emailConfigData.setServerName(emailConfigDTO.getServerName());
			emailConfigData.setServerType(emailConfigDTO.getServerType());
			emailConfigData.setIsSSL(emailConfigDTO.getIsSSL());
			emailConfigData.setPortNumber(emailConfigDTO.getPortNumber());
		}
		return emailConfigData;
	}

	/**
	 * API to fetch whether the batch class has any of its batches under processing i.e. not finished.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @return {@link Integer}, count of batch instances
	 */
	@Override
	public Integer getAllUnFinishedBatchInstancesCount(String batchClassIdentifier) {
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		LOGGER.info("Retrieving Number of batch instances for batch class: " + batchClassIdentifier);
		final int unfinishedBatchCount = batchInstanceService.getAllUnFinishedBatchInstancesCount(batchClassIdentifier);
		LOGGER.info(unfinishedBatchCount + " number of batches are in running state for the batch class: " + batchClassIdentifier);
		return unfinishedBatchCount;
	}

	/**
	 * API to test cmis connection to the repository server.
	 * 
	 * @param pluginConfigDTOValues {@link Collection<BatchClassPluginConfigDTO>} plugin configurations of the batch class.
	 * @return {@link Map<String,String>}
	 * @throws GWTException {@link GWTException} If not able to connect to repository server.
	 */
	@Override
	public Map<String, String> checkCmisConnection(final Collection<BatchClassPluginConfigDTO> pluginConfigDTOValues)
			throws GWTException {
		Map<String, String> map = new HashMap<String, String>();
		try {
			CMISExportService cmisExportservice = this.getSingleBeanOfType(CMISExportService.class);
			LOGGER.info("Checking CMIS connection for the batch..");
			Map<String, String> pluginPropertyValues = new HashMap<String, String>();
			if (pluginConfigDTOValues != null) {
				for (BatchClassPluginConfigDTO pluginConfigDTOValue : pluginConfigDTOValues) {
					pluginPropertyValues.put(pluginConfigDTOValue.getName(), pluginConfigDTOValue.getValue());
				}
				map = cmisExportservice.cmisConnectionTest(pluginPropertyValues);
			}
		} catch (DCMAException e) {
			LOGGER.error("Error occurred while testing cmis repository connection." + e.getMessage(), e);
			throw new GWTException(e.getMessage());
		}
		return map;
	}

	/**
	 * API for getting the CMIS configuration.
	 * 
	 * @param pluginConfigDTOValues {@link Collection<BatchClassPluginConfigDTO>} plugin configurations of the batch class.
	 * @return {@link Map<String,String>}
	 * @throws GWTException {@link GWTException} If not able to connect to repository server.
	 */
	@Override
	public Map<String, String> getCmisConfiguration(Collection<BatchClassPluginConfigDTO> pluginConfigDTOValues) throws GWTException {
		Map<String, String> tokenMap = null;
		try {
			CMISExportService cmisExportservice = this.getSingleBeanOfType(CMISExportService.class);
			LOGGER.info("Checking CMIS connection for the batch..");
			Map<String, String> pluginPropertyValues = new HashMap<String, String>();
			if (pluginConfigDTOValues != null) {
				for (BatchClassPluginConfigDTO pluginConfigDTOValue : pluginConfigDTOValues) {
					pluginPropertyValues.put(pluginConfigDTOValue.getName(), pluginConfigDTOValue.getValue());
				}
				tokenMap = cmisExportservice.getTokensMap(pluginPropertyValues);
			}
		} catch (DCMAException e) {
			LOGGER.error("Error occurred while testing cmis repository connection." + e.getMessage(), e);
			throw new GWTException(e.getMessage());
		}
		return tokenMap;

	}

	/**
	 * API for getting the authentication URL.
	 * 
	 * @param pluginConfigDTOValues {@link Collection<BatchClassPluginConfigDTO>} plugin configurations of the batch class.
	 * @return {@link Map<String,String>}
	 * @throws GWTException {@link GWTException} If not able to connect to repository server.
	 */
	@Override
	public String getAuthenticationURL(Collection<BatchClassPluginConfigDTO> pluginConfigDTOValues) throws GWTException {
		String authenticationURL = null;
		CMISExportService cmisExportservice = this.getSingleBeanOfType(CMISExportService.class);
		LOGGER.info("Checking CMIS connection for the batch..");
		Map<String, String> pluginPropertyValues = new HashMap<String, String>();
		if (pluginConfigDTOValues != null) {
			for (BatchClassPluginConfigDTO pluginConfigDTOValue : pluginConfigDTOValues) {
				pluginPropertyValues.put(pluginConfigDTOValue.getName(), pluginConfigDTOValue.getValue());
			}
			authenticationURL = cmisExportservice.getAuthenticationURL(pluginPropertyValues);
		}
		return authenticationURL;
	}

	/**
	 * API to authenticate box. Returns true if authenticated otherwise false.
	 * 
	 * @return boolean
	 */
	@Override
	public Boolean authenticateBox() {
		return true;
	}

	/**
	 * API to get authentication token for box repository.
	 * 
	 * @param APIKey {@link String} API key for the box repository
	 * @param ticket {@link String} Ticket generated for the box repository
	 * @return {@link String}
	 * @throws {@link GWTException}
	 */
	@Override
	public String getAuthenticationToken(String APIKey, String ticket) throws GWTException {
		String authTokenURL = "https://www.box.com/api/1.0/rest?action=get_auth_token&api_key=" + API_KEY + "&ticket=" + TICKET;
		String keyURL = authTokenURL.replace(API_KEY, APIKey);
		String finalURL = keyURL.replace(TICKET, ticket);
		HttpClient client = new HttpClient();
		GetMethod tokenGetMethod = new GetMethod(finalURL);
		String token = null;
		try {
			client.executeMethod(tokenGetMethod);
			InputStream responseBodyAsStream = tokenGetMethod.getResponseBodyAsStream();
			Document doc = XMLUtil.createDocumentFrom(responseBodyAsStream);
			String statusOfResponse = getValueFromXML(doc, RESPONSE_STATUS);
			if (null != statusOfResponse && statusOfResponse.equals(GET_AUTH_TOKEN_OK)) {
				token = getValueFromXML(doc, RESPONSE_AUTH_TOKEN);
			}
			LOGGER.info("Status of token response is:" + statusOfResponse);
		} catch (Exception e) {
			throw new GWTException("Unable to get new token");
		}
		LOGGER.info("Token is:" + token);
		return token;
	}

	/**
	 * Method to fetch the box properties.
	 * 
	 * @return {@link Map}
	 */
	@Override
	public Map<String, String> getBoxProperties() {
		Map<String, String> propertyMap = new HashMap<String, String>();
		propertyMap.put(AUTHENTICATION_URL, "https://www.box.com/api/1.0/auth/" + TICKET);
		return propertyMap;
	}

	/**
	 * API to get new ticket for box repository.
	 * 
	 * @param APIKey {@link String} API key for the box repository
	 * @return {@link String} the new generated ticket
	 * @throws GWTException
	 */
	@Override
	public String getNewTicket(String APIKey) throws GWTException {
		String ticket = null;
		String tempTicketURL = TEMP_TICKET_URL.replace(API_KEY, APIKey);
		GetMethod ticketGetMethod = new GetMethod(tempTicketURL);
		HttpClient client = new HttpClient();
		try {
			client.executeMethod(ticketGetMethod);
			InputStream responseBodyAsStream = ticketGetMethod.getResponseBodyAsStream();
			org.w3c.dom.Document doc = XMLUtil.createDocumentFrom(responseBodyAsStream);
			String statusOfResponse = getValueFromXML(doc, RESPONSE_STATUS);
			LOGGER.info("Status of ticket response is:" + statusOfResponse);
			if (null != statusOfResponse && statusOfResponse.equals(GET_TICKET_OK)) {
				ticket = getValueFromXML(doc, RESPONSE_TICKET);
			}
		} catch (Exception e) {
			throw new GWTException("Unable to get new ticket");
		}
		LOGGER.info("Ticket is:" + ticket);
		return ticket;
	}

	private String getValueFromXML(Document doc, String xPathExpression) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		String requiredValue = BatchClassManagementConstants.EMPTY_STRING;
		try {
			XPathExpression expr = xpath.compile(xPathExpression);
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			requiredValue = nodes.item(0).getFirstChild().getNodeValue();
		} catch (Exception e) {
		}
		return requiredValue;
	}
}
