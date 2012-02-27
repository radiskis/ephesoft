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
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassField;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.gwt.core.server.DCMARemoteServiceServlet;
import com.ephesoft.dcma.gwt.core.shared.BatchClassDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassFieldDTO;
import com.ephesoft.dcma.gwt.core.shared.exception.GWTException;
import com.ephesoft.dcma.gwt.uploadbatch.client.UploadBatchService;
import com.ephesoft.dcma.util.CustomFileFilter;

public class UploadBatchServiceImpl extends DCMARemoteServiceServlet implements UploadBatchService {

	private static final long serialVersionUID = 1L;
	private static final String SERIALIZATION_EXT = FileType.SER.getExtensionWithDot();
	private static final String BCF_SER_FILE_NAME = "BCF_ASSO";

	@Override
	public int getRowsCount() {
		List<BatchInstanceStatus> statusList = new ArrayList<BatchInstanceStatus>();
		statusList.add(BatchInstanceStatus.READY_FOR_REVIEW);
		statusList.add(BatchInstanceStatus.READY_FOR_VALIDATION);
		int rowCount = 0;
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		rowCount = batchInstanceService.getCount(statusList, null, getUserRoles(), getUserName());
		return rowCount;
	}

	@Override
	public String finishBatch(String currentBatchUploadFolder, String batchClassID) throws GWTException {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		try {
			CustomFileFilter fileFilter = new CustomFileFilter(true, FileType.TIF.getExtensionWithDot(), FileType.TIFF
					.getExtensionWithDot(), FileType.SER.getExtensionWithDot(), FileType.PDF.getExtensionWithDot());
			batchSchemaService.copyFolderWithFileFilter(batchSchemaService.getUploadBatchFolder(), currentBatchUploadFolder,
					batchClassID, fileFilter);
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
			// List<BatchClass> batchClassList = batchClassService.getAllBatchClasses();
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
		}
		return list;
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
			log.info("Error occurred while creating the serializable file." + e, e);
			throw new GWTException(e.getMessage());
		} finally {
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}

			} catch (Exception e) {
				if (serializedExportFile != null)
					log.error("Problem closing stream for file :" + serializedExportFile.getName());
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
				log.info("Error while cleaning up last upload batch folder. Folder name:" + folderPath);
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
					log.info("Error while deleting " + fileName + "file from the Folder:" + folderPath);
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
		String folderName = getUserName() + (new GregorianCalendar().getTimeInMillis());
		return folderName;
	}

}
