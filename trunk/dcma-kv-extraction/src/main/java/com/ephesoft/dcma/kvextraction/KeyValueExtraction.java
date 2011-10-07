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

package com.ephesoft.dcma.kvextraction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Field;
import com.ephesoft.dcma.batch.schema.HocrPages;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.DocField.AlternateValues;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.KVExtraction;
import com.ephesoft.dcma.da.service.FieldTypeService;
import com.ephesoft.dcma.kvextraction.constant.KVExtractionConstants;
import com.ephesoft.dcma.kvfinder.data.InputDataCarrier;
import com.ephesoft.dcma.kvfinder.data.OutputDataCarrier;
import com.ephesoft.dcma.kvfinder.service.KVFinderService;

/**
 * This class is used to extract the document level fields using key value extraction. Update the extracted data as a document level
 * fields to the batch.xml file.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.kvextraction.util.LocationFinder
 * @see com.ephesoft.dcma.kvextraction.util.DataCarrier
 */
@Component
public class KeyValueExtraction {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(KeyValueExtraction.class);
	private static final String KEY_VALUE_EXTRACTION_PLUGIN = "KEY_VALUE_EXTRACTION";

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
	 * Reference of KVFinderService.
	 */
	@Autowired
	private KVFinderService kvFinderService;

	/**
	 * Default name for first page.
	 */
	private boolean firstFieldValue;

	/**
	 * Reference of FieldTypeService.
	 * 
	 * @param fieldTypeService FieldService
	 */
	public final void setFieldService(final FieldTypeService fieldTypeService) {
		this.fieldTypeService = fieldTypeService;
	}

	/**
	 * Keep the first field value information.
	 * 
	 * @return isFirstFieldValue boolean
	 */
	public final boolean isFirstFieldValue() {
		return firstFieldValue;
	}

	/**
	 * Keep the first field value information.
	 * 
	 * @param isFirstFieldValue boolean
	 */

	public final void setFirstFieldValue(final boolean isFirstFieldValue) {
		this.firstFieldValue = isFirstFieldValue;
	}

	/**
	 * Reference fieldTypeService.
	 * 
	 * @return fieldTypeService
	 */
	public final FieldTypeService getFieldTypeService() {
		return fieldTypeService;
	}

	/**
	 * Reference fieldTypeService.
	 * 
	 * @param fieldTypeService FieldTypeService
	 */
	public final void setFieldTypeService(final FieldTypeService fieldTypeService) {
		this.fieldTypeService = fieldTypeService;
	}

	/**
	 * @return the batchSchemaService
	 */
	public final BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * @param batchSchemaService the batchSchemaService to set
	 */
	public final void setBatchSchemaService(final BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * @return the pluginPropertiesService
	 */
	public final PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	/**
	 * @param pluginPropertiesService the pluginPropertiesService to set
	 */
	public final void setPluginPropertiesService(final PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	/**
	 * This method is used to extract the document level fields using key value based extraction. Update the extracted data to the
	 * batch.xml file.
	 * 
	 * @param batchInstanceId String
	 * @return isSuccessful boolean
	 * @throws DCMAApplicationException Check for all the input parameters.
	 */
	public final boolean extractFields(final String batchInstanceId) throws DCMAApplicationException {

		String errMsg = null;
		if (null == batchInstanceId) {
			errMsg = "Invalid batchInstanceId.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}
		String switchValue = pluginPropertiesService.getPropertyValue(batchInstanceId, KEY_VALUE_EXTRACTION_PLUGIN,
				KVExtractionProperties.KV_EXTRACTION_SWITCH);
		if (("ON".equalsIgnoreCase(switchValue))) {

			if (null == fieldTypeService) {
				errMsg = "Invalid intialization of FieldService.";
				LOGGER.error(errMsg);
				throw new DCMAApplicationException(errMsg);
			}

			LOGGER.info("batchInstanceId : " + batchInstanceId);

			final Batch batch = batchSchemaService.getBatch(batchInstanceId);

			boolean isSuccessful = false;
			try {

				final List<Document> docTypeList = batch.getDocuments().getDocument();

				if (null == docTypeList) {
					LOGGER.info("In valid batch documents.");
				} else {
					isSuccessful = processDocPage(docTypeList, batchInstanceId, batch);
				}

			} catch (DCMAApplicationException e) {
				LOGGER.error(e.getMessage());
				throw new DCMAApplicationException(e.getMessage(), e);
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
				throw new DCMAApplicationException(e.getMessage(), e);
			}
			return isSuccessful;
		} else {
			LOGGER.info("Skipping KV extraction. Switch set as off.");
			return true;
		}

	}

	/**
	 * This method will process for each page for each document.
	 * 
	 * @param xmlDocuments List<DocumentType>
	 * @param batchInstanceID {@link String}
	 * @param batch {@link Batch}
	 * @return isSuccessful
	 * @throws DCMAApplicationException Check for all the input parameters.
	 */
	private boolean processDocPage(final List<Document> xmlDocuments, final String batchInstanceID, final Batch batch)
			throws DCMAApplicationException {

		boolean isSuccessful = false;
		String errMsg = null;
		if (null == xmlDocuments || xmlDocuments.isEmpty()) {
			throw new DCMAApplicationException("In valid parameters.");
		}

		final List<List<DocField>> updtDocList = new ArrayList<List<DocField>>();
		final List<Boolean> isValidateDataList = new ArrayList<Boolean>();

		for (Document document : xmlDocuments) {
			final List<Page> pageList = document.getPages().getPage();
			final String docTypeName = document.getType();
			LOGGER.info("docTypeName : " + docTypeName);
			// read all the extraction fields one by one to all the
			// pages document.

			boolean isValidateData = false;

			final List<com.ephesoft.dcma.da.domain.FieldType> allFdTypes = fieldTypeService.getFdTypeAndKVExtractionByDocTypeName(
					docTypeName, batchInstanceID, true);

			if (null == allFdTypes || allFdTypes.isEmpty()) {
				errMsg = "No FieldType data found from data base for document type : " + docTypeName;
				LOGGER.error(errMsg);
				updtDocList.add(null);
				isValidateDataList.add(null);
				continue;
			}

			final List<DocField> updtDocFdTyList = new ArrayList<DocField>();

			LOGGER.info("FieldType data found from data base for document type : " + docTypeName);

			for (com.ephesoft.dcma.da.domain.FieldType fdType : allFdTypes) {
				final String key = fdType.getName();
				final int fieldOrderNumber = fdType.getFieldOrderNumber();
				String fdTypeName = null;

				if (fdType.getDataType() != null) {
					fdTypeName = fdType.getDataType().name();
				}

				final DocField updtDocFdType = new DocField();
				final AlternateValues alternateValues = new AlternateValues();

				setFirstFieldValue(true);

				try {
					keyExtraction(updtDocFdType, alternateValues, pageList, fdType, batchInstanceID);
					sortDocFdWithConfidence(updtDocFdType);
				} catch (DCMAApplicationException dcma) {
					LOGGER.error(dcma.getMessage());
				} catch (Exception e) {
					LOGGER.error(e.getMessage());
				}

				if (!isFirstFieldValue()) {
					updtDocFdTyList.add(updtDocFdType);
				} else {
					updtDocFdType.setName(key);
					updtDocFdType.setType(fdTypeName);
					updtDocFdType.setFieldOrderNumber(fieldOrderNumber);
					updtDocFdTyList.add(updtDocFdType);
					isValidateData = true;
				}

			}

			isSuccessful = true;
			updtDocList.add(updtDocFdTyList);
			isValidateDataList.add(isValidateData);
		}

		updateBatchXML(batchInstanceID, updtDocList, batch, isValidateDataList);

		LOGGER.info("All files are written.");
		return isSuccessful;
	}

	/**
	 * Key based extraction.
	 * 
	 * @param updtDocFdType {@link DocField}
	 * @param alternateValues {@link AlternateValues}
	 * @param pageList List<PageType>
	 * @param fdType {@link com.ephesoft.dcma.da.domain.FieldType}
	 * @param locationFinder LocationFinder
	 * @param batchInstanceID {@link String}
	 * @throws DCMAApplicationException Check for input parameters.
	 */
	private void keyExtraction(final DocField updtDocFdType, final AlternateValues alternateValues, final List<Page> pageList,
			final com.ephesoft.dcma.da.domain.FieldType fdType, final String batchInstanceID) throws DCMAApplicationException {

		final List<KVExtraction> kvExtractionList = fdType.getKvExtraction();

		final String key = fdType.getName();

		if (null == kvExtractionList) {
			final String errMsg = "No KVExtraction data found from data base for field type : " + key;
			LOGGER.info(errMsg);
			return;
		}

		String fdTypeName = null;

		if (fdType.getDataType() != null) {
			fdTypeName = fdType.getDataType().name();
		}

		for (KVExtraction kVExtraction : kvExtractionList) {
			if (null == kVExtraction) {
				continue;
			}

			for (Page pageType : pageList) {
				final String pageID = pageType.getIdentifier();
				final HocrPages hocrPages = batchSchemaService.getHocrPages(batchInstanceID, pageID);
				if (null == hocrPages) {
					throw new DCMAApplicationException("In valid parameters. HocrPages is null for page id : " + pageID);
				}

				final List<HocrPage> hocrPageList = hocrPages.getHocrPage();

				HocrPage hocrPage = hocrPageList.get(0);

				LOGGER.info("HocrPage page ID : " + pageID);

				List<OutputDataCarrier> valueFoundData = null;

				InputDataCarrier inputDataCarrier = new InputDataCarrier(kVExtraction.getLocationType(), kVExtraction.getKeyPattern(),
						kVExtraction.getValuePattern(), kVExtraction.getNoOfWords(), kVExtraction.getMultiplier(), kVExtraction
								.getFetchValue(), kVExtraction.getLength(), kVExtraction.getWidth(), kVExtraction.getXoffset(),
						kVExtraction.getYoffset());

				List<InputDataCarrier> inputDataCarrierList = new ArrayList<InputDataCarrier>();
				inputDataCarrierList.add(inputDataCarrier);

				try {
					valueFoundData = kvFinderService.findKeyValue(inputDataCarrierList, hocrPage, Integer.MAX_VALUE);
				} catch (DCMAException e) {
					LOGGER.error(e.getMessage(), e);
				}

				if (valueFoundData != null && !valueFoundData.isEmpty()) {

					processPageType(updtDocFdType, valueFoundData, key, pageID, alternateValues, fdTypeName, fdType
							.getFieldOrderNumber());

				}
			}
		}
	}

	/**
	 * Sort the document level fields on the basis of confidence score.
	 * 
	 * @param updtDocFdType DocFieldType
	 */
	private void sortDocFdWithConfidence(final DocField updtDocFdType) {

		if (null != updtDocFdType) {

			final AlternateValues alternateValues = updtDocFdType.getAlternateValues();

			if (null != alternateValues) {

				final List<Field> alternateValue = alternateValues.getAlternateValue();

				if (!alternateValue.isEmpty()) {

					final Field localFdType = new Field();
					localFdType.setName(updtDocFdType.getName());
					localFdType.setValue(updtDocFdType.getValue());
					localFdType.setConfidence(updtDocFdType.getConfidence());
					localFdType.setCoordinatesList(updtDocFdType.getCoordinatesList());
					localFdType.setPage(updtDocFdType.getPage());
					localFdType.setType(updtDocFdType.getType());
					localFdType.setFieldOrderNumber(updtDocFdType.getFieldOrderNumber());

					alternateValue.add(0, localFdType);

					// sort the field type on the basis of confidence score.
					Collections.sort(alternateValue, new Comparator<Field>() {

						public int compare(final Field fdType1, final Field fdType2) {

							final float diffConfidence = fdType1.getConfidence() - fdType2.getConfidence();

							if (diffConfidence > 0) {
								return -1;
							}
							if (diffConfidence < 0) {
								return 1;
							}

							return 0;
						}
					});

					// get the maximum confidence value.
					final Field fdType = alternateValue.get(0);

					updtDocFdType.setConfidence(fdType.getConfidence());
					updtDocFdType.setCoordinatesList(fdType.getCoordinatesList());
					updtDocFdType.setName(fdType.getName());
					updtDocFdType.setPage(fdType.getPage());
					updtDocFdType.setType(fdType.getType());
					updtDocFdType.setValue(fdType.getValue());
					updtDocFdType.setFieldOrderNumber(fdType.getFieldOrderNumber());

					alternateValue.remove(0);
				}
			}
		}
	}

	/**
	 * This method will process each page of one document.
	 * 
	 * @param updtDocFdType DocFieldType
	 * @param foundData List<DataCarrier>
	 * @param key String
	 * @param pageID String
	 * @param alternateValues AlternateValues
	 * @param fdTypeName String
	 * @param fieldOrderNumber int
	 * @throws DCMAApplicationException Check for input parameters and process the document page.
	 */
	private void processPageType(final DocField updtDocFdType, final List<OutputDataCarrier> foundData, final String key,
			final String pageID, final AlternateValues alternateValues, final String fdTypeName, final int fieldOrderNumber)
			throws DCMAApplicationException {

		for (OutputDataCarrier dataCarrier : foundData) {
			final Span span = dataCarrier.getSpan();
			final String value = dataCarrier.getValue().trim();
			if (isFirstFieldValue()) {
				updtDocFdType.setName(key);
				updtDocFdType.setFieldOrderNumber(fieldOrderNumber);
				updtDocFdType.setPage(pageID);
				updtDocFdType.setType(fdTypeName);
				updtDocFdType.setConfidence(dataCarrier.getConfidence());
				updtDocFdType.setValue(value);

				final Coordinates coordinates = new Coordinates();
				final Coordinates hocrCoordinates = span.getCoordinates();
				coordinates.setX0(hocrCoordinates.getX0());
				coordinates.setX1(hocrCoordinates.getX1());
				coordinates.setY0(hocrCoordinates.getY0());
				coordinates.setY1(hocrCoordinates.getY1());

				CoordinatesList coordinatesList = new CoordinatesList();
				coordinatesList.getCoordinates().add(coordinates);
				updtDocFdType.setCoordinatesList(coordinatesList);

				setFirstFieldValue(false);
			} else {

				final List<Field> alternateValue = alternateValues.getAlternateValue();

				final Field fieldType = new Field();
				fieldType.setName(key);
				fieldType.setFieldOrderNumber(fieldOrderNumber);
				fieldType.setValue(value);
				fieldType.setType(fdTypeName);
				fieldType.setConfidence(dataCarrier.getConfidence());

				fieldType.setPage(pageID);

				final Coordinates coordinates = new Coordinates();
				final Coordinates hocrCoordinates = span.getCoordinates();
				coordinates.setX0(hocrCoordinates.getX0());
				coordinates.setX1(hocrCoordinates.getX1());
				coordinates.setY0(hocrCoordinates.getY0());
				coordinates.setY1(hocrCoordinates.getY1());

				CoordinatesList coordinatesList = new CoordinatesList();
				coordinatesList.getCoordinates().add(coordinates);
				fieldType.setCoordinatesList(coordinatesList);

				alternateValue.add(fieldType);
				updtDocFdType.setAlternateValues(alternateValues);
			}
		}
	}

	/**
	 * Update Batch XML file.
	 * 
	 * @param batchInstanceId String
	 * @param updtDocList List<List<DocFieldType>>
	 * @param batch Batch
	 * @param isValidateDataList List<Boolean>
	 * @throws DCMAApplicationException Check for input parameters and update the file having file name batch.xml.
	 */
	private void updateBatchXML(final String batchInstanceId, final List<List<DocField>> updtDocList, final Batch batch,
			final List<Boolean> isValidateDataList) throws DCMAApplicationException {

		String errMsg = null;

		if (null == batchInstanceId || KVExtractionConstants.EMPTY.equals(batchInstanceId)) {
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

		final List<Document> xmlDocuments = batch.getDocuments().getDocument();

		boolean isWrite = false;
		if (null != xmlDocuments && xmlDocuments.size() == updtDocList.size()) {
			for (int i = 0; i < xmlDocuments.size(); i++) {
				final List<DocField> docFdTyLt = updtDocList.get(i);
				// final Boolean isValidateData = isValidateDataList.get(i);
				if (null == docFdTyLt || docFdTyLt.isEmpty()) {
					continue;
				}
				final Document document = xmlDocuments.get(i);
				DocumentLevelFields documentLevelFields = document.getDocumentLevelFields();
				if (null == documentLevelFields) {
					documentLevelFields = new DocumentLevelFields();
				}
				final List<DocField> documentLevelField = documentLevelFields.getDocumentLevelField();

				// replace node only if exists other wise insert new one.
				// START
				if (documentLevelField.isEmpty()) {
					documentLevelField.addAll(docFdTyLt);
				} else {
					for (int orgIndex = 0; orgIndex < documentLevelField.size(); orgIndex++) {
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
