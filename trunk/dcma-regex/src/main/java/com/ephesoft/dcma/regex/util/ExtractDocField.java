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

package com.ephesoft.dcma.regex.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.regex.constant.RegexConstants;

/**
 * This class is used to extract the document fields. This will parse the file, update the file. After inserting all the document level
 * fields this class will actually persists the whole data to the batch.xml file.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.regex.Extraction
 */
public class ExtractDocField {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ExtractDocField.class);

	/**
	 * Update Batch XML file.
	 * 
	 * @param batchInstanceId String
	 * @param updtDocList List<List<DocFieldType>>
	 * @param batch Batch
	 * @param batchSchemaService BatchSchemaService
     * @param isValidDataList List<Boolean>
	 * @throws DCMAApplicationException Check for input parameters and update the file having file name batch.xml.
	 */
	public void updateBatchXML(final String batchInstanceId, final List<List<DocField>> updtDocList, Batch batch,
			List<Boolean> isValidateDataList, BatchSchemaService batchSchemaService) throws DCMAApplicationException {

		String errMsg = null;

		if (null == batchInstanceId || RegexConstants.EMPTY.equals(batchInstanceId)) {
			errMsg = "Invalid argument batchInstanceId.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		if (null == isValidateDataList || isValidateDataList.isEmpty()) {
			errMsg = "Invalid argument isValidateDataList.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		if (null == updtDocList || updtDocList.isEmpty()) {
			errMsg = "Invalid argument DocFieldType.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		List<Document> xmlDocuments = batch.getDocuments().getDocument();

		boolean isWrite = false;
		if (null != xmlDocuments && xmlDocuments.size() == updtDocList.size()) {
			for (int i = RegexConstants.ZERO; i < xmlDocuments.size(); i++) {
				List<DocField> docFdTyLt = updtDocList.get(i);
				// Boolean isValidateData = isValidateDataList.get(i);
				if (null == docFdTyLt || docFdTyLt.isEmpty()) {
					continue;
				}
				Document document = xmlDocuments.get(i);
				DocumentLevelFields documentLevelFields = document.getDocumentLevelFields();
				if (null == documentLevelFields) {
					documentLevelFields = new DocumentLevelFields();
				}
				List<DocField> documentLevelField = documentLevelFields.getDocumentLevelField();

				// replace node only if exists other wise insert new one.
				// START
				if (documentLevelField.isEmpty()) {
					documentLevelField.addAll(docFdTyLt);
				} else {
					for (int orgIndex = RegexConstants.ZERO; orgIndex < documentLevelField.size(); orgIndex++) {
						DocField orgFieldType = documentLevelField.get(orgIndex);
						String orgName = orgFieldType.getName();
						for (DocField actFieldType : docFdTyLt) {
							String actName = actFieldType.getName();
							if (null != actName && null != orgName && orgName.equals(actName)) {
								String actValue = actFieldType.getValue();
								if (null == actValue || actValue.isEmpty()) {
									break;
								} else {
									documentLevelField.set(orgIndex, actFieldType);
								}
								break;
							}
						}
					}
				}
				// DONE.

				document.setDocumentLevelFields(documentLevelFields);
				// document.setValid(!isValidateData);
				isWrite = true;
			}
		}

		// now write the state of the object to the xml file.
		if (isWrite) {
			batchSchemaService.updateBatch(batch);
			LOGGER.info("Document level fields are added" + " to the batch xml file.");
		} else {
			LOGGER.info("Document level fields are not added" + " to the batch xml file.");
		}

		LOGGER.info("updateBatchXML done.");

	}
}
