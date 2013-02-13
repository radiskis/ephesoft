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

package com.ephesoft.dcma.gwt.uploadbatch.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SerializationUtils;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.EphesoftUser;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.UserType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassCloudConfig;
import com.ephesoft.dcma.da.domain.BatchClassField;
import com.ephesoft.dcma.da.service.BatchClassCloudConfigService;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.gwt.core.server.DCMARemoteServiceServlet;
import com.ephesoft.dcma.gwt.core.shared.BatchClassCloudConfigDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassFieldDTO;
import com.ephesoft.dcma.gwt.core.shared.exception.GWTException;
import com.ephesoft.dcma.gwt.uploadbatch.client.UploadBatchService;
import com.ephesoft.dcma.gwt.uploadbatch.client.i18n.UploadBatchConstants;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.CustomFileFilter;
import com.ephesoft.dcma.util.IUtilCommonConstants;
import com.ephesoft.dcma.util.PDFUtil;
import com.ephesoft.dcma.util.TIFFUtil;

public class UploadBatchServiceImpl extends DCMARemoteServiceServlet implements UploadBatchService {

	private static final long serialVersionUID = 1L;
	private static final String SERIALIZATION_EXT = FileType.SER.getExtensionWithDot();
	private static final String BCF_SER_FILE_NAME = "BCF_ASSO";
	
	
	/**
	 * The UPLOAD_BATCH_LIMIT {@link String} is a constant for 'upload_batch_limit'
	 * defined in application-config properties file.
	 */
	private static final String UPLOAD_BATCH_LIMIT = "upload_batch_limit";
	
	/**
	 * The DEFAULT_FILE_SIZE_LIMIT is the default file size limit for upload
	 * batch for Freemium User type.
	 * 
	 * @return default file size limit
	 */
	private static final long DEFAULT_FILE_SIZE_LIMIT = 1024L;

	@Override
	public int getRowsCount() {
		List<BatchInstanceStatus> statusList = new ArrayList<BatchInstanceStatus>();
		statusList.add(BatchInstanceStatus.READY_FOR_REVIEW);
		statusList.add(BatchInstanceStatus.READY_FOR_VALIDATION);

		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		return batchInstanceService.getCount(statusList, null, getUserRoles(), getUserName(), EphesoftUser.NORMAL_USER);
	}

	@Override
	public String finishBatch(String currentBatchUploadFolder, String batchClassID) throws GWTException {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		final Integer userType = getUserType();

		// Path to upload batch folder in ephesoft shared folder
		StringBuilder uploadBatchFolderPath = new StringBuilder();
		uploadBatchFolderPath.append(batchSchemaService.getUploadBatchFolder());
		uploadBatchFolderPath.append(File.separator);
		uploadBatchFolderPath.append(currentBatchUploadFolder);
		uploadBatchFolderPath.append(File.separator);
		uploadBatchFolderPath.append(ICommonConstants.UPLOAD_BATCH_META_DATA_XML_FILE_NAME);

		// Creating the xml storing the upload batch info
		String uploadBatchFolder=uploadBatchFolderPath.toString();
		LOG.info("Path to create the upload batch meta data xml file for uploaded batches:"+uploadBatchFolder);
		createXmlForUploadBatchInfo(uploadBatchFolder);

		String errorMessage = null;
		try {
			
			// Check for batch class limit for the current user group
			errorMessage = checkForBatchClassLimit(userType, batchClassID, currentBatchUploadFolder, batchSchemaService.getUploadBatchFolder());
			if (null == errorMessage || errorMessage.isEmpty()) {
				CustomFileFilter fileFilter = new CustomFileFilter(true, FileType.TIF.getExtensionWithDot(), FileType.TIFF
						.getExtensionWithDot(), FileType.SER.getExtensionWithDot(), FileType.PDF.getExtensionWithDot(), FileType.XML
						.getExtensionWithDot());
				batchSchemaService.copyFolderWithFileFilter(batchSchemaService.getUploadBatchFolder(), currentBatchUploadFolder,
						batchClassID, fileFilter);
				updateBatchClassCounter(userType, batchClassID);
			} else {
				
				// Throw batch class limit exception
				throw new GWTException(errorMessage);
			}
		} catch (Exception e) {
			throw new GWTException(e.getMessage());
		}
		return currentBatchUploadFolder;
	}

	@Override
	public Map<String, String> getBatchClassName() {
		Map<String, String> list = new LinkedHashMap<String, String>();
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		Set<String> allGroups = getUserRoles();
		if (null != allGroups) {
			List<BatchClass> batchClassList = batchClassService.getAllBatchClassesByUserRoles(allGroups);
			list = getBatchClassName(batchClassList);
			// List<BatchClass> batchClassList = batchClassService.getAllBatchClasses();
		}
		return list;
	}

	private Map<String, String> getBatchClassName(List<BatchClass> batchClassList) {
		Map<String, String> list = new LinkedHashMap<String, String>();
		if (null != batchClassList) {
			for (BatchClass batchClass : batchClassList) {
				String identifier = batchClass.getIdentifier();
				String description = batchClass.getDescription();
				if (description.length() > 30) {
					description = description.substring(0, 30);
				}
				list.put(identifier + " - " + description, identifier);
			}
		}
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.ephesoft.dcma.gwt.uploadbatch.client.UploadBatchService#getBatchClassImageLimit()
	 */
	@Override
	public Map<String, BatchClassCloudConfigDTO> getBatchClassImageLimit() {
		Map<String, BatchClassCloudConfigDTO> map = new LinkedHashMap<String, BatchClassCloudConfigDTO>();
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		BatchClassCloudConfigService batchClassCloudService = this.getSingleBeanOfType(BatchClassCloudConfigService.class);
		Set<String> allGroups = getUserRoles();
		Integer userType = getUserType();
		if (null != allGroups) {
			List<BatchClass> batchClassList = batchClassService.getAllBatchClassesByUserRoles(allGroups);
			
			if (null != batchClassList) {
				for (BatchClass batchClass : batchClassList) {
					String identifier = batchClass.getIdentifier();
					BatchClassCloudConfigDTO batchClassConfigDTO = null;
					BatchClassCloudConfig batchClassCloudConfig = batchClassCloudService
																		.getBatchClassCloudConfigByBatchClassIdentifier(identifier);
					if (null != batchClassCloudConfig && userType.intValue() == UserType.LIMITED.getUserType()) {
						batchClassConfigDTO = new BatchClassCloudConfigDTO();
						batchClassConfigDTO.setBatchInstanceCounter(batchClassCloudConfig.getCurrentCounter());
						batchClassConfigDTO.setBatchInstanceImageLimit(batchClassCloudConfig.getPageCount());
						batchClassConfigDTO.setBatchInstanceLimit(batchClassCloudConfig.getBatchInstanceLimit());
					}
					map.put(identifier, batchClassConfigDTO);
				}
			}
		}
		return map;
	}

	/**
	 * This method is used to fetch the BatchClassFieldDTO on the basis of BatchClassIdentifier
	 * 
	 * @param identifier
	 * @return ArrayList<BatchClassFieldDTO>
	 */

	@Override
	public List<BatchClassFieldDTO> getBatchClassFieldDTOByBatchClassIdentifier(String identifier) {
		BatchClass batchClass = null;
		ArrayList<BatchClassFieldDTO> arrayList = new ArrayList<BatchClassFieldDTO>();
		if (identifier != null) {
			BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
			batchClass = batchClassService.getBatchClassByIdentifier(identifier);
			BatchClassDTO batchClassDTO = new BatchClassDTO();
			batchClassDTO.setIdentifier(batchClass.getIdentifier());
			batchClassDTO.setDescription(batchClass.getDescription());
			batchClassDTO.setName(batchClass.getName());
			batchClassDTO.setPriority(String.valueOf(batchClass.getPriority()));
			batchClassDTO.setUncFolder(batchClass.getUncFolder());
			batchClassDTO.setVersion(batchClass.getVersion());
			batchClassDTO.setDeleted(batchClass.isDeleted());
			for (BatchClassField batchClassField : batchClass.getBatchClassField()) {
				BatchClassFieldDTO batchClassFieldDTO = createBatchClassFieldDTO(batchClassDTO, batchClassField);
				batchClassDTO.addBatchClassField(batchClassFieldDTO);
			}
			for (BatchClassFieldDTO batchClassFieldDTO : batchClassDTO.getBatchClassField()) {
				arrayList.add(batchClassFieldDTO);
			}
		}
		return arrayList;
	}

	/**
	 * This method is used to create the BatchClassFieldDTO on the basis of BatchClassDTO and BatchClassField
	 * 
	 * @param batchClassDTO
	 * @param batchClassField
	 * @return BatchClassFieldDTO
	 */
	private BatchClassFieldDTO createBatchClassFieldDTO(BatchClassDTO batchClassDTO, BatchClassField batchClassField) {
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
		batchClassFieldDTO.setValue(batchClassField.getValue());
		return batchClassFieldDTO;
	}

	/**
	 * This method is used to serialize the BatchClassField that is to be used in Web scanner module
	 * 
	 * @param folderName
	 * @param values
	 */

	@Override
	public void serializeBatchClassField(String folderName, List<BatchClassFieldDTO> values) throws GWTException {
		FileOutputStream fileOutputStream = null;
		File serializedExportFile = null;
		ArrayList<BatchClassField> batchClassFieldList = new ArrayList<BatchClassField>();
		for (BatchClassFieldDTO batchClassFieldDTO : values) {
			BatchClassField batchClassField = new BatchClassField();
			batchClassField.setBatchClass(null);
			batchClassField.setDataType(batchClassFieldDTO.getDataType());
			batchClassField.setIdentifier(batchClassFieldDTO.getIdentifier());
			batchClassField.setName(batchClassFieldDTO.getName());
			batchClassField.setFieldOrderNumber(Integer.parseInt(batchClassFieldDTO.getFieldOrderNumber()));
			batchClassField.setDescription(batchClassFieldDTO.getDescription());
			batchClassField.setValidationPattern(batchClassFieldDTO.getValidationPattern());
			batchClassField.setSampleValue(batchClassFieldDTO.getSampleValue());
			batchClassField.setFieldOptionValueList(batchClassFieldDTO.getFieldOptionValueList());
			batchClassField.setValue(batchClassFieldDTO.getValue());
			batchClassFieldList.add(batchClassField);
		}
		try {
			BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			String folderPath = batchSchemaService.getUploadBatchFolder() + File.separator + folderName;
			File currentBatchUploadFolder = new File(folderPath);
			if (!currentBatchUploadFolder.exists()) {
				currentBatchUploadFolder.mkdirs();
			}
			serializedExportFile = new File(folderPath + File.separator + BCF_SER_FILE_NAME + SERIALIZATION_EXT);
			fileOutputStream = new FileOutputStream(serializedExportFile);
			SerializationUtils.serialize(batchClassFieldList, fileOutputStream);
		} catch (FileNotFoundException e) {
			// Unable to read serializable file
			LOG.info("Error occurred while creating the serializable file." + e, e);
			throw new GWTException(e.getMessage());
		} finally {
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}

			} catch (Exception e) {
				if (serializedExportFile != null) {
					LOG.error("Problem closing stream for file :" + serializedExportFile.getName());
				}
			}
		}

	}

	@Override
	public void resetCurrentBatchUploadFolder(String folderName) {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		String folderPath = batchSchemaService.getUploadBatchFolder() + File.separator + folderName;
		File currentBatchUploadFolder = new File(folderPath);
		if (currentBatchUploadFolder.exists()) {
			try {
				FileUtils.deleteDirectory(currentBatchUploadFolder);
				if (currentBatchUploadFolder.exists()) {
					FileUtils.deleteDirectory(currentBatchUploadFolder);
				}
			} catch (IOException e) {
				LOG.info("Error while cleaning up last upload batch folder. Folder name:" + folderPath);
			}
		}
	}

	@Override
	public List<String> deleteFilesByName(String folderName, List<String> fileNames) {
		List<String> filesNotDeleted = new ArrayList<String>();
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		String folderPath = batchSchemaService.getUploadBatchFolder() + File.separator + folderName;
		for (String fileName : fileNames) {
			File currentFile = new File(folderPath + File.separator + fileName);
			if (currentFile.exists()) {
				try {
					currentFile.delete();
					if (currentFile.exists()) {
						currentFile.delete();
					}
				} catch (Exception e) {
					filesNotDeleted.add(fileName);
					LOG.info("Error while deleting " + fileName + "file from the Folder:" + folderPath);
				}
			}
			if (currentFile.exists()) {
				// add to the list if file not deleted
				filesNotDeleted.add(fileName);
			}
		}
		return filesNotDeleted;
	}

	@Override
	public String getCurrentBatchFolderName() {
		return getUserName() + (new GregorianCalendar().getTimeInMillis());
	}
	
	/* (non-Javadoc)
	 * @see com.ephesoft.dcma.gwt.uploadbatch.client.UploadBatchService#getFileSizeLimit()
	 */
	public Long getFileSizeLimit() {
		Long fileSizeLimit = null;
		try {
			fileSizeLimit =  Long.parseLong(ApplicationConfigProperties.
										getApplicationConfigProperties().getProperty(UPLOAD_BATCH_LIMIT));
		} catch (NumberFormatException e) {
			fileSizeLimit = DEFAULT_FILE_SIZE_LIMIT;
			LOG.error("Format of property file_size_limit is wrong.");
		} catch (IOException e) {
			fileSizeLimit = DEFAULT_FILE_SIZE_LIMIT;
			LOG.error("Property file_size_limit is missing from property file.");
		}
		return fileSizeLimit;
	}
	
	/**
	 * The <code>updateBatchClassCounter</code> method is used for updating the 
	 * batch class instance counter.
	 *  
	 * @param userType {@link Integer} Ephesoft Cloud user type
	 * @param batchClassID {@link String} current selected batch class identifier
	 */
	private void updateBatchClassCounter(final Integer userType, final String batchClassID) {
		
		// Check for FREEMIUM user type
		if (userType.intValue() == UserType.LIMITED.getUserType()) {
			final BatchClassCloudConfigService batchClassCloudService = this.getSingleBeanOfType(BatchClassCloudConfigService.class);
			final BatchClassCloudConfig batchClassCloudConfig = batchClassCloudService.
			getBatchClassCloudConfigByBatchClassIdentifier(batchClassID);
			if (null != batchClassCloudConfig) {
				Integer batchInstanceCount = batchClassCloudConfig.getBatchInstanceLimit();
				Integer currentCounter = batchClassCloudConfig.getCurrentCounter();
				
				// Check for batch class limit and update the counter
				if (null != currentCounter && null != batchInstanceCount && ++currentCounter <= batchInstanceCount) {
					batchClassCloudConfig.setCurrentCounter(currentCounter);
					batchClassCloudService.updateBatchClassCloudConfig(batchClassCloudConfig);
				}
			}
		}
	}

	/**
	 * The <code>checkForBatchClassLimit</code> method is used for checking batch 
	 * class instance limit for a given user.
	 * 
	 * @param userType {@link Integer} Ephesoft Cloud user type
	 * @param batchClassID {@link String} current selected batch class identifier
	 * @param currentBatchUploadFolder {@link String} temporary upload folder path
	 * @param sourcePath {@link String} batch class source path
	 * @return {@link String} error message
	 */
	private String checkForBatchClassLimit(final Integer userType,
			final String batchClassID, final String currentBatchUploadFolder, final String sourcePath) {
		String errorMessage = null;
		
		// Check for FREEMIUM user type
		if (userType.intValue() == UserType.LIMITED.getUserType()) {
			String finalPath = sourcePath + File.separator + currentBatchUploadFolder;
			BatchClassCloudConfigService batchClassCloudService = this.getSingleBeanOfType(BatchClassCloudConfigService.class);
			BatchClassCloudConfig batchClassCloudConfig = batchClassCloudService.
																getBatchClassCloudConfigByBatchClassIdentifier(batchClassID);
			if (null != batchClassCloudConfig) {
				Integer pageCount = batchClassCloudConfig.getPageCount();
				Integer batchInstanceLimit = batchClassCloudConfig.getBatchInstanceLimit();
				Integer currentCounter = batchClassCloudConfig.getCurrentCounter();
				
				// Check for batch class instance limit
				boolean isBatchLimit = (null != batchInstanceLimit && null != currentCounter && ++currentCounter <= batchInstanceLimit);
				
				// If batch instance limit is not reached
				if (isBatchLimit) {
					
					// Check for batch instance page count limit
					boolean isPageLimit = checkForPageLimit(finalPath, pageCount);
					if (isPageLimit) {
						errorMessage = UploadBatchConstants.IMAGE_ERROR;
					}
				} else {
					errorMessage = UploadBatchConstants.INSTANCE_ERROR;
					com.ephesoft.dcma.util.FileUtils
						.deleteDirectoryAndContentsRecursive(new File(finalPath), true);
					
				}
			}
		}
		return errorMessage;
	}

	/**
	 * The <code>checkForPageLimit</code> method is used to check the page 
	 * count limit for a batch class instance.
	 * 
	 * @param currentBatchUploadFolder {@link String} current folder name of upload batch
	 * @param pageCount {@link Integer} current page count limit
	 * @return true/false indicating page limit crossed or not
	 */
	private boolean checkForPageLimit(final String currentBatchUploadFolder,
			final Integer pageCount) {
		boolean isPageError = false;
		if (null != pageCount && null != currentBatchUploadFolder) {
			File uploadDir = new File(currentBatchUploadFolder);
			int currentCount = 0;
			
			// Iterate through upload files stored in temporary upload folder
			for (File file: uploadDir.listFiles()) {
				String fileName = file.getName();
				String filePath = file.getAbsolutePath();
				
				// Get page count for multi-page tiff or pdf file.
				int pageCountTemp = fileName.endsWith(IUtilCommonConstants.EXTENSION_PDF) ? PDFUtil.getPDFPageCount(filePath) :
											TIFFUtil.getTIFFPageCount(filePath);
				currentCount += pageCountTemp;
				
				// If page count limit is crossed 
				if (currentCount > pageCount) {
					isPageError = true;
					break;
				}
			}
		}
		return isPageError;
	}


}
