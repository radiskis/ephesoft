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

package com.ephesoft.dcma.csv;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.csv.constant.CSVFileCreationConstant;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;

/**
 * This class is used to creating and exporting into path specified on the export folder CSV file.
 * 
 * @author Ephesoft
 * @version 1.0
 */
@Component
public class CSVFileCreationExporter implements ICommonConstants {

	/**
	 * Variable for ON.
	 */
	private static final String ON_STRING = "ON";

	private static final String PDF_EXT = ".pdf";

	/**
	 * Instance of Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CSVFileCreationExporter.class);

	/**
	 * Instance of BatchSchemaService.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Instance of BatchClassService.
	 */
	@Autowired
	private BatchClassService batchClassService;

	/**
	 * Instance of BatchInstanceService.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * Instance of PluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * @return the batchSchemaService
	 */
	public BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * @return the pluginPropertiesService
	 */
	public PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	/**
	 * This API adding header columns to the CSV file.
	 */
	private void addHeaderColumns(final List<String> headerColumns) {
		LOGGER.info("Start adding header column type in csv");
		headerColumns.add(CSVFileCreationConstant.FILE_PROCESS_NAME.getId());
		LOGGER.info("Add 1st header column type in csv :" + CSVFileCreationConstant.FILE_PROCESS_NAME.getId());
		headerColumns.add(CSVFileCreationConstant.SUBPOENA.getId());
		LOGGER.info("Add 2nd header column type in csv :" + CSVFileCreationConstant.SUBPOENA.getId());
		headerColumns.add(CSVFileCreationConstant.LOAN_NUMBER.getId());
		LOGGER.info("Add 3rd header column type in csv :" + CSVFileCreationConstant.LOAN_NUMBER.getId());
		headerColumns.add(CSVFileCreationConstant.PROCESS_DATE.getId());
		LOGGER.info("Add 4th header column type in csv :" + CSVFileCreationConstant.PROCESS_DATE.getId());
		headerColumns.add(CSVFileCreationConstant.TAB_NAME.getId());
		LOGGER.info("Add 5th header column type in csv :" + CSVFileCreationConstant.TAB_NAME.getId());
		headerColumns.add(CSVFileCreationConstant.CREATE_TAB.getId());
		LOGGER.info("Add 6th header column type in csv :" + CSVFileCreationConstant.CREATE_TAB.getId());
		headerColumns.add(CSVFileCreationConstant.PLACEHOLDER.getId());
		LOGGER.info("Add 7th header column type in csv :" + CSVFileCreationConstant.PLACEHOLDER.getId());
		headerColumns.add(CSVFileCreationConstant.NUMBER_OF_IMAGES.getId());
		LOGGER.info("Add 8th header column type in csv :" + CSVFileCreationConstant.NUMBER_OF_IMAGES.getId());
		LOGGER.info("Finished adding header column type in csv");
	}

	/**
	 * This API is used to create export folder if not exists and export the CSV into it.
	 * 
	 * @param batchInstanceID {@link String}
	 * @throws JAXBException if any error in reading batch.xml
	 * @throws DCMAApplicationException if any error or exception occur
	 */
	public void exportCSVFiles(final String batchInstanceID) throws JAXBException, DCMAApplicationException {
		LOGGER.info("CSV File creation export plugin.");
		LOGGER.info("Initializing properties...");

		String isCSVFileCreationSwitchON = this.pluginPropertiesService.getPropertyValue(batchInstanceID,
				CSVFileCreationConstant.CSV_FILE_CREATION_PLUGIN.getId(), CSVFileCreationProperties.CSV_FILE_CREATION_SWITCH);

		if (ON_STRING.equalsIgnoreCase(isCSVFileCreationSwitchON)) {
			String exportFolder = this.pluginPropertiesService.getPropertyValue(batchInstanceID,
					CSVFileCreationConstant.CSV_FILE_CREATION_PLUGIN.getId(),
					CSVFileCreationProperties.CSV_FILE_CREATION_EXPORT_FOLDER);

			if ((null == exportFolder) || ("".equalsIgnoreCase(exportFolder))) {
				throw new DCMAApplicationException(
						"CSV File Creation export folder value is null/empty from the database. Invalid initializing of properties.");
			}

			LOGGER.info("Properties Initialized Successfully");

			List<String> headerColumns = new LinkedList<String>();

			List<List<String>> dataList = new ArrayList<List<String>>();

			// Created export folder
			boolean isExportFoderCreated = createExportFolder(exportFolder);

			BatchInstance batchInstance = this.batchInstanceService.getBatchInstanceByIdentifier(batchInstanceID);

			Batch batch = this.batchSchemaService.getBatch(batchInstanceID);

			List<Document> documentList = batch.getDocuments().getDocument();

			if (batchClassService == null) {
				LOGGER.error("batchClassService is null.");
				throw new DCMAApplicationException("batchClassService is null.");
			}

			String batchClassId = batch.getBatchClassIdentifier();
			BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);
			List<DocumentType> batchDocumentList = batchClass.getDocumentTypes();
			List<DocumentType> batchClassMajorDocumentType = new ArrayList<DocumentType>();
			
			if (batchDocumentList == null || batchDocumentList.isEmpty()) {
				LOGGER.error("batchDocumentList is null or empty.");
				throw new DCMAApplicationException("batchDocumentList is null or empty.");
			}
			for(DocumentType docType: batchDocumentList) {
				if(!docType.isHidden()&& !docType.getName().equals("Unknown")){
					batchClassMajorDocumentType.add(docType);
				}
			}
			
			for (DocumentType document : batchClassMajorDocumentType) {
				List<String> addDataToList = addDataToList(batch, document, batchInstance, documentList);
				dataList.add(addDataToList);
			
			}

			// Getting export CSV file path
			String fileName = getTargetFilePath(batchInstanceID, exportFolder, batch, CSVFileCreationConstant.CSV_EXTENSION.getId());

			if (isExportFoderCreated && fileName != null && batch != null && batchInstance != null) {
				this.addHeaderColumns(headerColumns);
				try {
					new WriteToCSVFile(fileName, headerColumns, dataList);
				} catch (IOException e) {
					LOGGER.error("Error in creating file" + e.getMessage(), e);
					throw new DCMAApplicationException("Error in creating file " + e.getMessage(), e);
				}
			}
		} else {
			LOGGER.info("Switch is off or null for CSV file creation plugin.");
		}
	}


	/**
	 * This API added data into list.
	 * 
	 * @param batch {@link Batch}
	 * @param document {@link Document}
	 * @param docField {@link DocField}
	 * @param batchInstance {@link BatchInstance}
	 * 
	 */
	private List<String> addDataToList(final Batch batch, final DocumentType document, final BatchInstance batchInstance,
			final List<Document> batchDocumentList) {
		Document batchDocumtent = null;
		boolean placeholderSet = false;
		List<String> list = new ArrayList<String>();
		String subpoenaNumberValue = "";
		String batchName = batch.getBatchName();
		String[] split = batchName.split(CSVFileCreationConstant.UNDERSCORE.getId());
		String loanNumber = split[split.length - 1];
		int endIndex = batchName.length() - loanNumber.length() - 1;
		if (endIndex > 3) {
			subpoenaNumberValue = batchName.substring(0, endIndex);
		}
		String file_process_name = batch.getBatchName() + CSVFileCreationConstant.UNDERSCORE.getId()
				+ batch.getBatchInstanceIdentifier() + PDF_EXT;
		list.add(file_process_name);
		list.add(subpoenaNumberValue);
		list.add(loanNumber);
		String datePattern = CSVFileCreationConstant.MM_DD_YYYY.getId();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern, Locale.getDefault());
		String batchCreationDate = simpleDateFormat.format(new Date());
		list.add(batchCreationDate);
		// Adding information for BookmarkValue column
		list.add(document.getName());

		// Adding information for Place holder column
		for (Document docType : batchDocumentList) {
			if (docType != null) {
				String name = docType.getType();
				if (document.getName().equals(name)) {
					// Adding information for BookmarkCreated column
					list.add(CSVFileCreationConstant.YES.getId());
					list.add(CSVFileCreationConstant.NO_STRING.getId());
					placeholderSet = true;
					batchDocumtent =  docType;
					break;
				}
			}
		}
		if(!placeholderSet) {
			// Adding information for BookmarkCreated column
			list.add(CSVFileCreationConstant.NO_STRING.getId());
			list.add(CSVFileCreationConstant.YES.getId());
			// Adding information for PgCount column
			list.add("0");
		} else {
			// Adding information for PgCount column
			list.add(String.valueOf(batchDocumtent.getPages().getPage().size()));
		}
		
		
		return list;
	}

	/**
	 * This API is used to create the export folder.
	 * 
	 * @param exportFolder {@link String}
	 * @return isExportFoderCreated
	 */
	private boolean createExportFolder(final String exportFolder) {
		File exportFolderFile = new File(exportFolder);
		boolean isExportFolderCreated = false;
		if (!exportFolderFile.exists()) {
			isExportFolderCreated = exportFolderFile.mkdirs();
		} else {
			isExportFolderCreated = true;
		}
		return isExportFolderCreated;
	}

	/**
	 * This API returns the exported CSV file path.
	 * 
	 * @param batchInstanceID {@link String}
	 * @param exportFolder {@link String}
	 * @param batch {@link Batch}
	 * @param fileExtension {@link String}
	 * @return targetFilePath
	 */
	private String getTargetFilePath(final String batchInstanceID, final String exportFolder, final Batch batch,
			final String fileExtension) {
		LOGGER.info("Generating file path to be exported");
		String targetFilePath = exportFolder + File.separator + batch.getBatchName() + CSVFileCreationConstant.UNDERSCORE.getId()
				+ batchInstanceID + fileExtension.toLowerCase(Locale.getDefault());
		LOGGER.info("File path to be exported is :" + targetFilePath);
		return targetFilePath;
	}
	

}
