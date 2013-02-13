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

package com.ephesoft.dcma.boxexport;

import java.io.File;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Batch.Documents;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.boxexport.constants.BoxExportConstant;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.util.XMLUtil;

/**
 * This class exports the multipage pdf/multipage tiff files for each document of a batch to box repository specified by admin.
 * 
 * @author Ephesoft
 * @Version 1.0
 * @see com.ephesoft.dcma.batch.service.BatchSchemaService
 * @See com.ephesoft.dcma.batch.service.BatchSchemaService
 */
public class BoxExporter {

	private static final String AUTHENTICATION_TOKEN_NOT_SPECIFIED_BATCH_INSTANCE = "Authentication token not specified. Batch instance : ";

	private static final String BOX_UPLOAD_FILE_TYPE_NOT_SPECIFIED_THROWING_BATCH_TO_ERROR_BATCH_INSTANCE = "Box Upload file type not specified. Throwing batch to error. Batch instance : ";

	private static final String DEFAULT_FOLDER_ID = "0";

	private static final String BATCH_INSTANCE_ID = " Batch instance : ";

	private static final String ERROR_EXPORTING_FILE = "Error occurred while exporting file : ";

	private static final Logger LOGGER = LoggerFactory.getLogger(BoxExporter.class);

	/**
	 * Instance of BatchInstanceService.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * Instance of BatchSchemaService.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

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
	 * @param batchSchemaService the batchSchemaService to set
	 */
	public void setBatchSchemaService(final BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * @return the batchInstanceService
	 */
	public BatchInstanceService getBatchInstanceService() {
		return batchInstanceService;
	}

	/**
	 * @param batchInstanceService the batchInstanceService to set
	 */
	public void setBatchInstanceService(final BatchInstanceService batchInstanceService) {
		this.batchInstanceService = batchInstanceService;
	}

	/**
	 * Method to export batch instance files to box repository.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @throws DCMAApplicationException {@link DCMAApplicationException}
	 */
	public void exportFiles(final String batchInstanceIdentifier) throws DCMAApplicationException {
		LOGGER.info("Inside box export plugin for batch : " + batchInstanceIdentifier);

		LOGGER.info("Initializing properties...");

		String isBoxON = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, BoxExportConstant.BOX_EXPORT_PLUGIN,
				BoxProperties.BOX_SWITCH);

		if (isBoxON == null || !isBoxON.equals(BoxExportConstant.SWITCH_ON)) {
			return;
		}
		// String folderID = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, BOX_EXPORT_PLUGIN,
		// BoxProperties.BOX_FOLDER_ID);

		// Hard coding the folder id to upload the documents to the root folder only. Read from UI in future.
		String folderID = DEFAULT_FOLDER_ID;
		String authToken = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, BoxExportConstant.BOX_EXPORT_PLUGIN,
				BoxProperties.BOX_AUTHENTICATION_TOKEN);
		if (authToken == null || authToken.isEmpty()) {
			LOGGER.error(AUTHENTICATION_TOKEN_NOT_SPECIFIED_BATCH_INSTANCE + batchInstanceIdentifier);
			throw new DCMAApplicationException(AUTHENTICATION_TOKEN_NOT_SPECIFIED_BATCH_INSTANCE + batchInstanceIdentifier);
		}
		String boxUploadFileType = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				BoxExportConstant.BOX_EXPORT_PLUGIN, BoxProperties.BOX_UPLOAD_FILE_TYPE);
		if (boxUploadFileType == null) {
			String errorString = BOX_UPLOAD_FILE_TYPE_NOT_SPECIFIED_THROWING_BATCH_TO_ERROR_BATCH_INSTANCE + batchInstanceIdentifier;
			LOGGER.error(errorString);
			throw new DCMAApplicationException(errorString);
		}
		StringBuilder folderNameStringBuilder = new StringBuilder();
		folderNameStringBuilder.append(batchSchemaService.getLocalFolderLocation());
		folderNameStringBuilder.append(File.separator);
		folderNameStringBuilder.append(batchInstanceIdentifier);
		String folderToBeExported = folderNameStringBuilder.toString();

		Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);

		Documents documents = batch.getDocuments();
		if (documents == null) {
			throw new DCMAApplicationException("Documents cannot be null for batch id:" + batch.getBatchInstanceIdentifier());
		}
		List<Document> listOfDocuments = documents.getDocument();
		if (listOfDocuments == null) {
			throw new DCMAApplicationException("Documents list cannot be null for batch id:" + batch.getBatchInstanceIdentifier());
		}

		processDocuments(batchInstanceIdentifier, folderID, authToken, boxUploadFileType, folderToBeExported, listOfDocuments);
		LOGGER.info("Exiting box export plugin for batch : " + batchInstanceIdentifier);
	}

	/**
	 * Method to process each document of the batch instance and export corresponding files to the box repository.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @param folderID {@link String}
	 * @param authToken {@link String}
	 * @param boxUploadFileType {@link String}
	 * @param sFolderToBeExported {@link String}
	 * @param listOfDocuments {@link List<Document>}
	 * @throws DCMAApplicationException {@link DCMAApplicationException}
	 */
	private void processDocuments(final String batchInstanceIdentifier, final String folderID, final String authToken,
			final String boxUploadFileType, final String sFolderToBeExported, final List<Document> listOfDocuments)
			throws DCMAApplicationException {
		String exportMultiPageFileName = null;
		String url = BoxExportConstant.HTTPS_WWW_BOX_COM_API_1_0_UPLOAD_URL.replace(BoxExportConstant.AUTH_TOKEN_CONSTANT, authToken)
				.replace(BoxExportConstant.FOLDER_ID_CONSTANT, folderID);
		HttpClient client = new HttpClient();
		PostMethod mPost = new PostMethod(url);
		try {
			for (Document document : listOfDocuments) {
				LOGGER.info("Processing document for box export : " + document.getIdentifier());
				if (boxUploadFileType.equalsIgnoreCase(BoxExportConstant.MULTIPAGE_PDF)) {
					exportMultiPageFileName = document.getMultiPagePdfFile();
				} else {
					exportMultiPageFileName = document.getMultiPageTiffFile();
				}

				LOGGER.info("The file to be exported is : " + exportMultiPageFileName);
				File exportFile = new File(sFolderToBeExported + File.separator + exportMultiPageFileName);

				// Creating the data to be exported to Box
				Part[] parts = new Part[2];
				parts[0] = new FilePart(exportMultiPageFileName, exportFile);
				String docLevelFieldData = getDocLevelFieldsData(document);
				LOGGER.info("Description for uploading file : " + docLevelFieldData);
				parts[1] = new StringPart(BoxExportConstant.DESCRIPTION, docLevelFieldData);

				MultipartRequestEntity entity = new MultipartRequestEntity(parts, mPost.getParams());
				mPost.setRequestEntity(entity);

				// Sending the data to Box
				int statusCode = client.executeMethod(mPost);
				LOGGER.info("Status Code of method execution: " + statusCode);
				String responseBody = mPost.getResponseBodyAsString();
				LOGGER.info("responseBody :: " + responseBody);

				// Getting and analyzing the response for the sent data
				org.w3c.dom.Document doc = XMLUtil.createDocumentFrom(mPost.getResponseBodyAsStream());
				String statusOfResponse = XMLUtil.getValueFromXML(doc, BoxExportConstant.RESPONSE_STATUS);
				LOGGER.info("The status of response for upload is :" + statusOfResponse);
				if (null == statusOfResponse || !statusOfResponse.equals(BoxExportConstant.UPLOAD_OK)) {
					LOGGER.error("Error uploading files to Box. Exiting with error code : " + statusCode);
					throw new DCMAApplicationException(ERROR_EXPORTING_FILE + exportMultiPageFileName + BATCH_INSTANCE_ID
							+ batchInstanceIdentifier);
				}
			}
		} catch (Exception e) {
			LOGGER.error(ERROR_EXPORTING_FILE + exportMultiPageFileName + BATCH_INSTANCE_ID + batchInstanceIdentifier, e);
			throw new DCMAApplicationException(ERROR_EXPORTING_FILE + exportMultiPageFileName + BATCH_INSTANCE_ID
					+ batchInstanceIdentifier, e);
		} finally {
			mPost.releaseConnection();
		}
	}

	/**
	 * This method gets the name value pair for document level fields of a document.
	 * 
	 * @param document {@link Document} Document whose document leel fields data is to be extracted
	 * @return {@link String} returns document level fields data as a string
	 */
	private String getDocLevelFieldsData(final Document document) {
		LOGGER.info("Entering getDocLevelFieldsData ... ");
		StringBuffer docLevelFieldData = new StringBuffer();
		DocumentLevelFields documentLevelFields = document.getDocumentLevelFields();
		if (documentLevelFields != null) {
			List<DocField> docFieldList = documentLevelFields.getDocumentLevelField();
			for (DocField docField : docFieldList) {
				String docFieldName = docField.getName();
				String docFieldValue = docField.getValue();
				if (docFieldName == null || docFieldName.isEmpty()) {
					continue;
				}
				LOGGER.info("DocLevelField : " + docFieldName + " DocFieldValue : " + docFieldValue);
				docLevelFieldData.append(docFieldName);
				docLevelFieldData.append(BoxExportConstant.EQUALS_OPERATOR);
				docLevelFieldData.append(docFieldValue);
				docLevelFieldData.append(BoxExportConstant.SEMI_COLON_SEPARATOR);
			}
		}
		LOGGER.info("Exiting getDocLevelFieldsData ... ");
		return docLevelFieldData.toString();
	}
}
