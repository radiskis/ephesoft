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

package com.ephesoft.dcma.docassembler.classification.searchablepdf.process;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.DocumentType;

/**
 * This class will process the batch and modify the document type from UNKNWON to the first document type configured for batch class
 * configured for searchable pdf workflow.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.docassembler.DocumentAssembler
 */
public class SearchablePdfPageProcess {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchablePdfPageProcess.class);

	/**
	 * Instance of PluginPropertiesService.
	 */
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * Batch instance Identifier.
	 */
	private String batchInstanceID;

	/**
	 * Reference of BatchSchemaService.
	 */
	private BatchSchemaService batchSchemaService;

	/**
	 * To get the plugin Properties Service.
	 * @return the pluginPropertiesService
	 */
	public PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	/**
	 * To get Batch Instance ID.
	 * @return the batchInstanceID
	 */
	public String getBatchInstanceID() {
		return batchInstanceID;
	}

	/**
	 * To set Batch Instance ID.
	 * @param batchInstanceID String
	 */
	public void setBatchInstanceID(String batchInstanceID) {
		this.batchInstanceID = batchInstanceID;
	}

	/**
	 * To set the plugin Properties Service.
	 * @param pluginPropertiesService PluginPropertiesService
	 */
	public void setPluginPropertiesService(PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	/**
	 * To get Batch Schema Service.
	 * @return the batchSchemaService
	 */
	public BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * To set Batch Schema Service.
	 * @param batchSchemaService BatchSchemaService
	 */
	public void setBatchSchemaService(BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * This method modifies the document type from UNKNWON to the first document type configured for batch class configured for
	 * searchable pdf workflow.
	 * 
	 * @throws DCMAApplicationException {@link DCMAApplicationException} Check for input parameters, update the batch xml.
	 */
	public void modifyDocumentType() throws DCMAApplicationException {
		List<DocumentType> allDocTypes = pluginPropertiesService.getDocumentTypes(batchInstanceID);
		String docTypeName = null;
		if (allDocTypes != null && !allDocTypes.isEmpty()) {
			// Assumption : Only 1 document type exist for Searchable PDF workflow.
			DocumentType docType = allDocTypes.get(0);
			if (docType != null) {
				docTypeName = docType.getName();
			} else {
				LOGGER.error("Document type is NULL for batch instance : " + batchInstanceID);
				throw new DCMAApplicationException("Document type is NULL for batch instance : " + batchInstanceID);
			}
		} else {
			LOGGER.error("No Document type defined for batch instance : " + batchInstanceID);
			throw new DCMAApplicationException("No Document type defined for batch instance : " + batchInstanceID);
		}
		if (docTypeName == null) {
			LOGGER.error("Document type name is NULL for batch instance : " + batchInstanceID);
			throw new DCMAApplicationException("Document type name is NULL for batch instance : " + batchInstanceID);
		}
		Batch batch = batchSchemaService.getBatch(batchInstanceID);
		List<Document> xmlDocuments = batch.getDocuments().getDocument();
		for (Document document : xmlDocuments) {
			document.setType(docTypeName);
			
			// Set the error message explicitly to blank to display the node in batch xml
			document.setErrorMessage("");
		}
		
		batchSchemaService.updateBatch(batch);
	}

}
