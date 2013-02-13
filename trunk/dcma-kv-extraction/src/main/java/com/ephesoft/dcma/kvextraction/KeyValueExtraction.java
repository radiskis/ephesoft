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

package com.ephesoft.dcma.kvextraction;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.ExtractKVParams;
import com.ephesoft.dcma.batch.schema.Field;
import com.ephesoft.dcma.batch.schema.HocrPages;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.DocField.AlternateValues;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.schema.ExtractKVParams.Params;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.common.KVFetchValue;
import com.ephesoft.dcma.core.common.KVPageValue;
import com.ephesoft.dcma.core.common.LocationType;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.AdvancedKVExtraction;
import com.ephesoft.dcma.da.domain.KVExtraction;
import com.ephesoft.dcma.da.service.FieldTypeService;
import com.ephesoft.dcma.kvextraction.constant.KVExtractionConstants;
import com.ephesoft.dcma.kvfinder.data.InputDataCarrier;
import com.ephesoft.dcma.kvfinder.data.KeyValueFieldCarrier;
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
	/**
	 * Plug in name for KV extraction.
	 */
	private static final String KEY_VALUE_EXTRACTION_PLUGIN = "KEY_VALUE_EXTRACTION";

	/**
	 * fieldService {@link FieldTypeService}.
	 */
	@Autowired
	private FieldTypeService fieldTypeService;

	/**
	 * Reference of {@link BatchSchemaService}.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Instance of {@link PluginPropertiesService}.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * Reference of {@link KVFinderService}.
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
	 * @param fieldTypeService {@link FieldTypeService}.
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
	 * @return fieldTypeService {@link FieldTypeService}
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
	 * @return the {@link BatchSchemaService}
	 */
	public final BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * @param batchSchemaService the {@link BatchSchemaService}
	 */
	public final void setBatchSchemaService(final BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * @return the {@link PluginPropertiesService}
	 */
	public final PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	/**
	 * @param pluginPropertiesService the {@link PluginPropertiesService}
	 */
	public final void setPluginPropertiesService(final PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	/**
	 * This method is used to extract the document level fields using key value based extraction. Update the extracted data to the
	 * batch.xml file.
	 * 
	 * @param batchInstanceId {@link String}
	 * @return isSuccessful boolean
	 * @throws DCMAApplicationException Check for all the input parameters.
	 */
	public final boolean extractFields(final String batchInstanceId) throws DCMAApplicationException {

		boolean isSuccessful = false;
		String errMsg = null;
		if (null == batchInstanceId) {
			errMsg = "Invalid batchInstanceId.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}
		String switchValue = pluginPropertiesService.getPropertyValue(batchInstanceId, KEY_VALUE_EXTRACTION_PLUGIN,
				KVExtractionProperties.KV_EXTRACTION_SWITCH);
		if ("ON".equalsIgnoreCase(switchValue)) {

			if (null == fieldTypeService) {
				errMsg = "Invalid intialization of FieldService.";
				LOGGER.error(errMsg);
				throw new DCMAApplicationException(errMsg);
			}

			LOGGER.info("batchInstanceId : " + batchInstanceId);

			final Batch batch = batchSchemaService.getBatch(batchInstanceId);

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
		} else {
			LOGGER.info("Skipping KV extraction. Switch set as off.");
		}
		return isSuccessful;
	}

	/**
	 * This api is used to extract the field values from the HOCR file.
	 * @param updtDocList {@link List<DocField>}
	 * @param hocrPages {@link HocrPages}
	 * @param params {@link ExtractKVParams}
	 * @return boolean
	 * @throws DCMAApplicationException
	 */
	public final boolean extractFieldsFromHOCR(final List<DocField> updtDocList, final HocrPages hocrPages,
			final ExtractKVParams params) throws DCMAApplicationException {

		boolean isSuccessful = false;
		try {
			isSuccessful = performKVExtractionAPI(updtDocList, hocrPages, params);
		} catch (DCMAApplicationException e) {
			LOGGER.error(e.getMessage());
			throw new DCMAApplicationException(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new DCMAApplicationException(e.getMessage(), e);
		}
		return isSuccessful;
	}

	private boolean performKVExtractionAPI(final List<DocField> updtDocFdTyList, final HocrPages hocrPages, ExtractKVParams params)
			throws DCMAApplicationException {
		boolean isSuccessful = false;
		final List<Boolean> isValidateDataList = new ArrayList<Boolean>();

		boolean isValidateData = false;
		// read the params from web service
		Params paramList = params.getParams().get(0);

		String pageID = null;
		String key = paramList.getKeyPattern();
		final int fieldOrderNumber = -1;
		String fdTypeName = null;

		final DocField updtDocFdType = new DocField();
		final AlternateValues alternateValues = new AlternateValues();

		setFirstFieldValue(true);

		try {
			InputDataCarrier inputDataCarrier = new InputDataCarrier(LocationType.valueOf(paramList.getLocationType()), paramList
					.getKeyPattern(), paramList.getValuePattern(), Integer.valueOf(paramList.getNoOfWords()), paramList
					.getMultiplier(), paramList.getKVFetchValue() == null ? null : KVFetchValue.valueOf(paramList.getKVFetchValue()),
					Integer.valueOf(paramList.getLength()), Integer.valueOf(paramList.getWidth()), Integer.valueOf(paramList
							.getXoffset()), Integer.valueOf(paramList.getYoffset()), false, null, null);

			performKVExtraction(updtDocFdType, alternateValues, fieldOrderNumber, key, fdTypeName, hocrPages, pageID,
					inputDataCarrier, null, null);

			sortDocFdWithConfidence(updtDocFdType);

			// populating the default values
			updtDocFdType.setFieldOrderNumber(0);
			updtDocFdType.setFieldValueOptionList(KVExtractionConstants.EMPTY);
			updtDocFdType.setOverlayedImageFileName(KVExtractionConstants.EMPTY);
			updtDocFdType.setPage(KVExtractionConstants.EMPTY);
			updtDocFdType.setType(KVExtractionConstants.EMPTY);

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
		isSuccessful = true;
		isValidateDataList.add(isValidateData);

		LOGGER.info("All files are written.");
		return isSuccessful;

	}

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
			String docTypeName = document.getType();
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
			Map<String, KeyValueFieldCarrier> fieldTypeKVMap = new HashMap<String, KeyValueFieldCarrier>(allFdTypes.size());

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
					KeyValueFieldCarrier keyValueFieldCarrier = new KeyValueFieldCarrier();
					fieldTypeKVMap.put(key, keyValueFieldCarrier);
					keyExtraction(updtDocFdType, alternateValues, pageList, fdType, fieldTypeKVMap, batchInstanceID,
							keyValueFieldCarrier);
					sortDocFdWithConfidence(updtDocFdType);
				} catch (DCMAApplicationException dcma) {
					LOGGER.error(dcma.getMessage());
				} catch (Exception e) {
					LOGGER.error(e.getMessage());
				}

				// Code commented as it could be a probable cause for blank document level fields.
				// if (!isFirstFieldValue()) {
				// updtDocFdTyList.add(updtDocFdType);
				// } else {
				updtDocFdType.setName(key);
				updtDocFdType.setType(fdTypeName);
				updtDocFdType.setFieldOrderNumber(fieldOrderNumber);
				updtDocFdTyList.add(updtDocFdType);
				// isValidateData = true;
				// }
			}

			isSuccessful = true;
			updtDocList.add(updtDocFdTyList);
			isValidateDataList.add(isValidateData);
		}

		updateBatchXML(batchInstanceID, updtDocList, batch, isValidateDataList);

		LOGGER.info("All files are written.");
		return isSuccessful;
	}

	private void keyExtraction(final DocField updtDocFdType, final AlternateValues alternateValues, final List<Page> pageList,
			final com.ephesoft.dcma.da.domain.FieldType fdType, final Map<String, KeyValueFieldCarrier> fieldTypeKVMap,
			final String batchInstanceID, final KeyValueFieldCarrier keyValueFieldCarrier) throws DCMAApplicationException {

		final List<KVExtraction> kvExtractionList = fdType.getKvExtraction();

		final String key = fdType.getName();

		if (null == kvExtractionList || pageList == null || pageList.isEmpty()) {
			final String errMsg = "No KVExtraction data found from data base for field type : " + key;
			LOGGER.info(errMsg);
			return;
		}

		for (KVExtraction kVExtraction : kvExtractionList) {
			if (null == kVExtraction) {
				continue;
			}

			KVPageValue kvPageValue = kVExtraction.getPageValue();
			if (kvPageValue == null) {
				LOGGER.info("Page Value null. Setting its value to ALL.");
				kvPageValue = KVPageValue.ALL;
			}
			LOGGER.info("Performing kv extraction for page value = " + kvPageValue);
			switch (kvPageValue) {
				case ALL:
					for (Page pageType : pageList) {
						LOGGER.info("Extract Key value data for page : " + pageType.getIdentifier());
						extractData(updtDocFdType, alternateValues, fdType, batchInstanceID, kVExtraction, pageType, fieldTypeKVMap,
								keyValueFieldCarrier);
					}
					break;
				case FIRST:
					if (pageList != null && pageList.size() > 0) {
						extractData(updtDocFdType, alternateValues, fdType, batchInstanceID, kVExtraction, pageList.get(0),
								fieldTypeKVMap, keyValueFieldCarrier);
					}
					break;
				case LAST:
					if (pageList != null && pageList.size() > 0) {
						extractData(updtDocFdType, alternateValues, fdType, batchInstanceID, kVExtraction, pageList.get(pageList
								.size() - 1), fieldTypeKVMap, keyValueFieldCarrier);
					}
					break;
				default:
					break;
			}
		}
	}

	private void extractData(final DocField updtDocFdType, final AlternateValues alternateValues,
			final com.ephesoft.dcma.da.domain.FieldType fdType, final String batchInstanceID, KVExtraction kVExtraction,
			Page pageType, final Map<String, KeyValueFieldCarrier> fieldTypeKVMap, final KeyValueFieldCarrier keyValueFieldCarrier)
			throws DCMAApplicationException {
		final String pageID = pageType.getIdentifier();
		String key = null;
		String fdTypeName = null;
		final HocrPages hocrPages = batchSchemaService.getHocrPages(batchInstanceID, pageID);
		if (null == hocrPages) {
			throw new DCMAApplicationException("In valid parameters. HocrPages is null for page id : " + pageID);
		}
		LOGGER.info("HocrPage page ID : " + pageID);
		AdvancedKVExtraction advancedKVExtraction = kVExtraction.getAdvancedKVExtraction();
		Coordinates keyRectangleCoordinates = null;
		Coordinates valueRectangleCoordinates = null;
		if (advancedKVExtraction != null) {
			keyRectangleCoordinates = new Coordinates();
			valueRectangleCoordinates = new Coordinates();
			keyRectangleCoordinates.setX0(BigInteger.valueOf(advancedKVExtraction.getKeyX0Coord()));
			keyRectangleCoordinates.setY0(BigInteger.valueOf(advancedKVExtraction.getKeyY0Coord()));
			keyRectangleCoordinates.setX1(BigInteger.valueOf(advancedKVExtraction.getKeyX1Coord()));
			keyRectangleCoordinates.setY1(BigInteger.valueOf(advancedKVExtraction.getKeyY1Coord()));
			valueRectangleCoordinates.setX0(BigInteger.valueOf(advancedKVExtraction.getValueX0Coord()));
			valueRectangleCoordinates.setY0(BigInteger.valueOf(advancedKVExtraction.getValueY0Coord()));
			valueRectangleCoordinates.setX1(BigInteger.valueOf(advancedKVExtraction.getValueX1Coord()));
			valueRectangleCoordinates.setY1(BigInteger.valueOf(advancedKVExtraction.getValueY1Coord()));
		}
		if (fdType != null) {
			key = fdType.getName();
			if (fdType.getDataType() != null) {
				fdTypeName = fdType.getDataType().name();
			}
		}
		InputDataCarrier inputDataCarrier = new InputDataCarrier(kVExtraction.getLocationType(), kVExtraction.getKeyPattern(),
				kVExtraction.getValuePattern(), kVExtraction.getNoOfWords(), kVExtraction.getMultiplier(), kVExtraction
						.getFetchValue(), kVExtraction.getLength(), kVExtraction.getWidth(), kVExtraction.getXoffset(), kVExtraction
						.getYoffset(), kVExtraction.isUseExistingKey(), keyRectangleCoordinates, valueRectangleCoordinates);
		performKVExtraction(updtDocFdType, alternateValues, fdType.getFieldOrderNumber(), key, fdTypeName, hocrPages, pageID,
				inputDataCarrier, fieldTypeKVMap, keyValueFieldCarrier);
	}

	private void performKVExtraction(final DocField updtDocFdType, final AlternateValues alternateValues, final int fieldOrderNumber,
			final String key, String fdTypeName, final HocrPages hocrPages, final String pageID,
			final InputDataCarrier inputDataCarrier, final Map<String, KeyValueFieldCarrier> fieldTypeKVMap,
			KeyValueFieldCarrier keyValueFieldCarrier) throws DCMAApplicationException {

		final List<HocrPage> hocrPageList = hocrPages.getHocrPage();

		HocrPage hocrPage = hocrPageList.get(0);

		List<OutputDataCarrier> valueFoundData = null;

		List<InputDataCarrier> inputDataCarrierList = new ArrayList<InputDataCarrier>();
		inputDataCarrierList.add(inputDataCarrier);

		try {
			valueFoundData = kvFinderService.findKeyValue(inputDataCarrierList, hocrPage, fieldTypeKVMap, keyValueFieldCarrier,
					Integer.MAX_VALUE);
		} catch (DCMAException e) {
			LOGGER.error(e.getMessage(), e);
		}

		if (valueFoundData != null && !valueFoundData.isEmpty()) {
			processPageType(updtDocFdType, valueFoundData, key, pageID, alternateValues, fdTypeName, fieldOrderNumber);
		}
	}

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

	/**
	 * This api is used to extract the values from HOCR file for batch class.
	 * @param updtDocList {@link List<DocField>}
	 * @param hocrPages {@link HocrPages}
	 * @param batchClassId {@link String}
	 * @param docTypeName {@link String}
	 * @return boolean
	 */
	public boolean extractFromHOCRForBatchClass(final List<DocField> updtDocList, final HocrPages hocrPages,
			final String batchClassId, final String docTypeName) {
		String errMsg = "";
		boolean isSuccessful = false;

		final List<com.ephesoft.dcma.da.domain.FieldType> allFdTypes = fieldTypeService.getFdTypeByDocTypeNameForBatchClass(
				docTypeName, batchClassId);

		if (null == allFdTypes || allFdTypes.isEmpty()) {
			errMsg = "No FieldType data found from data base for document type : " + docTypeName;
			LOGGER.error(errMsg);
			updtDocList.add(null);
		}

		LOGGER.info("FieldType data found from data base for document type : " + docTypeName);
		Map<String, KeyValueFieldCarrier> fieldTypeKVMap = new HashMap<String, KeyValueFieldCarrier>(allFdTypes.size());

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
				KeyValueFieldCarrier keyValueFieldCarrier = new KeyValueFieldCarrier();
				fieldTypeKVMap.put(key, keyValueFieldCarrier);

				List<KVExtraction> kvExtractionList = fdType.getKvExtraction();
				for (KVExtraction kvExtraction : kvExtractionList) {
					extractData(updtDocFdType, alternateValues, fdType, kvExtraction, fieldTypeKVMap, keyValueFieldCarrier, null, key,
							fdTypeName, hocrPages);
				}

				sortDocFdWithConfidence(updtDocFdType);
			} catch (DCMAApplicationException dcma) {
				LOGGER.error(dcma.getMessage());
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}

			if (!isFirstFieldValue()) {
				updtDocList.add(updtDocFdType);
			} else {
				updtDocFdType.setName(key);
				updtDocFdType.setType(fdTypeName);
				updtDocFdType.setFieldOrderNumber(fieldOrderNumber);
				updtDocList.add(updtDocFdType);
			}

		}
		isSuccessful = true;
		// updtDocList.add(updtDocFdTyList);
		// updtDocList.addAll(updtDocFdTyList);
		return isSuccessful;
	}

	private void extractData(final DocField updtDocFdType, final AlternateValues alternateValues,
			final com.ephesoft.dcma.da.domain.FieldType fdType, KVExtraction kVExtraction,
			final Map<String, KeyValueFieldCarrier> fieldTypeKVMap, final KeyValueFieldCarrier keyValueFieldCarrier,
			final String pageID, String key, String fdTypeName, final HocrPages hocrPages) throws DCMAApplicationException {
		AdvancedKVExtraction advancedKVExtraction = kVExtraction.getAdvancedKVExtraction();
		Coordinates keyRectangleCoordinates = null;
		Coordinates valueRectangleCoordinates = null;
		if (advancedKVExtraction != null) {
			keyRectangleCoordinates = new Coordinates();
			valueRectangleCoordinates = new Coordinates();
			keyRectangleCoordinates.setX0(BigInteger.valueOf(advancedKVExtraction.getKeyX0Coord()));
			keyRectangleCoordinates.setY0(BigInteger.valueOf(advancedKVExtraction.getKeyY0Coord()));
			keyRectangleCoordinates.setX1(BigInteger.valueOf(advancedKVExtraction.getKeyX1Coord()));
			keyRectangleCoordinates.setY1(BigInteger.valueOf(advancedKVExtraction.getKeyY1Coord()));
			valueRectangleCoordinates.setX0(BigInteger.valueOf(advancedKVExtraction.getValueX0Coord()));
			valueRectangleCoordinates.setY0(BigInteger.valueOf(advancedKVExtraction.getValueY0Coord()));
			valueRectangleCoordinates.setX1(BigInteger.valueOf(advancedKVExtraction.getValueX1Coord()));
			valueRectangleCoordinates.setY1(BigInteger.valueOf(advancedKVExtraction.getValueY1Coord()));
		}
		if (fdType != null) {
			key = fdType.getName();
			if (fdType.getDataType() != null) {
				fdTypeName = fdType.getDataType().name();
			}
		}
		InputDataCarrier inputDataCarrier = new InputDataCarrier(kVExtraction.getLocationType(), kVExtraction.getKeyPattern(),
				kVExtraction.getValuePattern(), kVExtraction.getNoOfWords(), kVExtraction.getMultiplier(), kVExtraction
						.getFetchValue(), kVExtraction.getLength(), kVExtraction.getWidth(), kVExtraction.getXoffset(), kVExtraction
						.getYoffset(), kVExtraction.isUseExistingKey(), keyRectangleCoordinates, valueRectangleCoordinates);
		performKVExtraction(updtDocFdType, alternateValues, fdType.getFieldOrderNumber(), key, fdTypeName, hocrPages, pageID,
				inputDataCarrier, fieldTypeKVMap, keyValueFieldCarrier);
	}
}
