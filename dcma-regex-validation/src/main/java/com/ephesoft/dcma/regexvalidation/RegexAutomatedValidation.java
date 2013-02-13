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

package com.ephesoft.dcma.regexvalidation;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Column;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Row;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.FieldType;
import com.ephesoft.dcma.da.domain.RegexValidation;
import com.ephesoft.dcma.da.service.FieldTypeService;
import com.ephesoft.dcma.regexvalidation.constant.RegexValidationConstants;

/**
 * This class is used to validate the document level fields of document for whole batch. It will validate the DLF's on the basis of
 * regex patterns available in database. If the document level field is valid with data type and regex pattern then only document is
 * called as valid document otherwise it is taken as invalid document.
 * 
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.regexvalidation.service.RegexValidationServiceImpl
 */
@Component
public class RegexAutomatedValidation {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RegexAutomatedValidation.class);

	/**
	 * fieldService FieldService.
	 */
	@Autowired
	private FieldTypeService fieldTypeService;

	/**
	 * Reference of BatchSchemaService.
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
	 * The <code>validateDLFields</code> method is used to validate the document level fields of document for whole batch. It will
	 * fetch all the record corresponding to document level fields from the database and on the basis of that pattern it will validate
	 * the document level field. If all the patterns satisfied with the document level field value then only that document will marked
	 * as valid otherwise it will marked as invalid document.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @throws DCMAApplicationException If any invalid state occur during the regex based validation process.
	 */
	public boolean validateDLFields(final String batchInstanceIdentifier) throws DCMAApplicationException {

		String errMsg = null;
		if (null == batchInstanceIdentifier) {
			errMsg = "Invalid batchInstanceId.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		if (null == fieldTypeService) {
			errMsg = "Invalid intialization of FieldService.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		LOGGER.info("batchInstanceIdentifier : " + batchInstanceIdentifier);

		final Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);

		boolean isSuccessful = false;
		try {

			final List<Document> docTypeList = batch.getDocuments().getDocument();

			if (null == docTypeList) {
				LOGGER.info("In valid batch documents.");
			} else {
				validateDLFields(docTypeList, batchInstanceIdentifier);

				// START.. add field option list if any....
				setFieldValueOptionList(docTypeList, batchInstanceIdentifier);
				// END...

				// now write the state of the object to the xml file.
				batchSchemaService.updateBatch(batch);
				LOGGER.info("Updated the batch xml file with valid/invalid document state.");

				isSuccessful = true;
			}

		} catch (DCMAApplicationException e) {
			LOGGER.error(e.getMessage(), e);
			throw new DCMAApplicationException(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new DCMAApplicationException(e.getMessage(), e);
		}

		return isSuccessful;
	}

	/**
	 * This method is responsible for setting the field option list for document level fields.
	 * 
	 * @param docTypeList List<Document>
	 * @param batchInstanceIdentifier {@link String}
	 */
	private void setFieldValueOptionList(final List<Document> docTypeList, final String batchInstanceIdentifier) {
		for (Document document : docTypeList) {
			if (null != document) {
				DocumentLevelFields documentLevelFields = document.getDocumentLevelFields();
				if (documentLevelFields != null) {
					List<DocField> docFieldList = documentLevelFields.getDocumentLevelField();
					List<FieldType> fieldTypeList = pluginPropertiesService.getFieldTypes(batchInstanceIdentifier, document.getType());
					if (fieldTypeList != null) {
						for (DocField docField : docFieldList) {
							for (FieldType fieldType : fieldTypeList) {
								validateFieldType(docField, fieldType);
							}
						}
					}
				}
			}
		}
	}

	private void validateFieldType(DocField docField, FieldType fieldType) {
		if (docField != null && fieldType != null) {
			String docFieldName = docField.getName();
			String fieldTypeName = fieldType.getName();
			if (null != docFieldName && docFieldName.equals(fieldTypeName)) {
				String fieldOptionValueList = fieldType.getFieldOptionValueList();
				if (fieldOptionValueList != null) {
					docField.setFieldValueOptionList(fieldOptionValueList);
				}
			}
		}
	}

	/**
	 * The <code>validateDLFields</code> method is used to validate the document level fields. If all the patterns satisfied with the
	 * document level field value then only that document will marked as valid otherwise it will marked as invalid document.
	 * 
	 * @param xmlDocuments List<Document>
	 * @param batchInstanceIdentifier {@link String}
	 * @param batch {@link Batch}
	 * @throws DCMAApplicationException If any invalid state occur during the regex based validation process.
	 */
	private void validateDLFields(final List<Document> xmlDocuments, final String batchInstanceIdentifier)
			throws DCMAApplicationException {
		String errMsg = null;
		List<DocField> documentLevelField = null;
		documentFor: for (Document document : xmlDocuments) {
			final String docTypeName = document.getType();
			LOGGER.info("docTypeName : " + docTypeName);

			DocumentLevelFields documentLevelFields = document.getDocumentLevelFields();
			if (null == documentLevelFields) {
				document.setValid(true);
				// continue;
			} else {
				documentLevelField = documentLevelFields.getDocumentLevelField();
				if (null == documentLevelField || documentLevelField.isEmpty()) {
					LOGGER.info("Document level field is null or empty.");
					document.setValid(true);
					// continue;
				} else {
					final List<com.ephesoft.dcma.da.domain.FieldType> allFdTypes = fieldTypeService
							.getFdTypeAndRegexValidationByDocTypeName(docTypeName, batchInstanceIdentifier);

					if (null == allFdTypes || allFdTypes.isEmpty()) {
						errMsg = "No FieldType data found from data base for document type : " + docTypeName;
						LOGGER.info(errMsg);
						document.setValid(true);
						// continue;
					} else {
						LOGGER.info("FieldType data found from data base for document type : " + docTypeName);
						for (DocField docField : documentLevelField) {
							final String value = docField.getValue();
							final String name = docField.getName();

							if (name == null) {
								LOGGER.info("Name is null for document level field.");
								continue;
							}

							boolean isCorrect = false;
							for (com.ephesoft.dcma.da.domain.FieldType fdType : allFdTypes) {

								if (null == fdType) {
									LOGGER.info("field is null for database field type.");
									continue;
								}

								final String dbFdName = fdType.getName();

								if (null == dbFdName) {
									LOGGER.info("field name is null for database field type.");
									continue;
								}

								if (dbFdName.equals(name)) {
									final List<RegexValidation> regexValidationList = fdType.getRegexValidation();

									if (null == regexValidationList || regexValidationList.isEmpty()) {
										LOGGER.info("Regex validation list is empty.");
										document.setValid(true);
										break;
									}
									for (RegexValidation regexValidation : regexValidationList) {
										if (null == regexValidation) {
											LOGGER.info("Regex validation is null.");
											document.setValid(true);
											continue;
										}
										String pattern = regexValidation.getPattern();

										isCorrect = findPattern(value, pattern);
										if (isCorrect) {
											LOGGER.info("Setting document type as valid document. Document type : " + docTypeName);
											document.setValid(true);
										} else {
											LOGGER.info("Setting document type as in valid document. Document type : " + docTypeName);
											document.setValid(false);
											continue documentFor;
										}
									}
									break;
								}
							}
						}
					}
				}
			}
			document.setValid(checkForInvalidDataTables(document));
		}
	}

	private boolean checkForInvalidDataTables(Document document) {
		LOGGER.info("Checking for invalid datatables.");
		boolean isValidDoc = true;
		if (document.getDataTables() != null) {
			List<DataTable> dataTableList = document.getDataTables().getDataTable();
			for (DataTable dataTable : dataTableList) {
				LOGGER.debug("Datatable name : " + dataTable.getName());
				if (dataTable.getRows() != null) {
					isValidDoc = checkForInvalidRow(dataTable);
					if (!isValidDoc) {
						break;
					}
				}
			}
		}
		LOGGER.debug("Is datatable valid : " + isValidDoc);
		return isValidDoc;
	}

	public boolean checkForInvalidRow(final DataTable dataTable) {
		LOGGER.info("Checking for invalid rows in table.");
		boolean isValidDoc = true;
		List<Row> rowList = dataTable.getRows().getRow();
		for (Row row : rowList) {
			if (row.getColumns() != null) {
				List<Column> columnList = row.getColumns().getColumn();
				for (Column column : columnList) {
					LOGGER.debug("Column data : " + column.getValue());
					if (!column.isValid()) {
						isValidDoc = false;
						LOGGER.debug("Column not valid.");
						break;
					}
				}
				if (!isValidDoc) {
					break;
				}
			}
		}
		return isValidDoc;
	}

	
	/**
	 * The <code>findPattern</code> method will test the pattern on the input character sequence and return true if and only if it
	 * passes the test.
	 * 
	 * @param inputCharSequence {@link String}
	 * @param patternStr {@link String}
	 * @return boolean if it pass the pattern matching test.
	 * @throws DCMAApplicationException if any invalid state occur during pattern test process.
	 */
	private boolean findPattern(String inputCharSequence, String patternStr) throws DCMAApplicationException {

		String errMsg = null;
		CharSequence inputStr = inputCharSequence;
		boolean isFound = false;
		if (null == inputStr /* || RegexValidationConstants.EMPTY.equals(inputStr) */) {
			errMsg = "Invalid input character sequence.";
			LOGGER.info(errMsg);
		} else {

			if (null == patternStr || RegexValidationConstants.EMPTY.equals(patternStr)) {
				errMsg = "Invalid input pattern sequence.";
				throw new DCMAApplicationException(errMsg);
			}

			// Compile and use regular
			// expression
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(inputStr);
			// boolean matchFound = matcher.find();
			whileLoop: while (matcher.find()) {
				// Get all groups for this match
				for (int i = RegexValidationConstants.ZERO; i <= matcher.groupCount();) {
					String groupStr = matcher.group(i);
					if (groupStr != null && groupStr.equals(inputStr)) {
						isFound = true;
						break whileLoop;
					} else {
						break whileLoop;
					}
				}
			}
		}

		return isFound;
	}
}
