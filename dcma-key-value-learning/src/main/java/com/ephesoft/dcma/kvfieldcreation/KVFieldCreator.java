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

package com.ephesoft.dcma.kvfieldcreation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.HocrPages;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.common.KVFetchValue;
import com.ephesoft.dcma.core.common.LocationType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.FieldType;
import com.ephesoft.dcma.da.domain.KVExtraction;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.da.service.KVExtractionService;
import com.ephesoft.dcma.kvfieldcreation.constant.KVFieldCreatorConstants;
import com.ephesoft.dcma.kvfinder.LineDataCarrier;
import com.ephesoft.dcma.kvfinder.service.KVFinderService;

/**
 * This class creates the key value field in batch class for every document level field of each document in the batch.
 * 
 * @author Ephesoft
 * @version 1.0
 */
@Component
public class KVFieldCreator implements ICommonConstants {

	private static final Logger LOGGER = LoggerFactory.getLogger(KVFieldCreator.class);

	private static final String KV_FIELD_LEARNING_PLUGIN = "KEY_VALUE_LEARNING_PLUGIN";

	private String locationOrder;
	private String maxNumberRecordPerDlf;
	private String toleranceThreshold;
	private String[] locationArr;
	private int maxNumberRecordPerDlfInt;
	private int toleranceThresholdInt;
	private String multiplier;
	private String fetchValue;
	private String minKeyCharCount;
	private int gapBetweenKeys;

	public int getGapBetweenKeys() {
		return gapBetweenKeys;
	}

	public void setGapBetweenKeys(int gapBetweenKeys) {
		this.gapBetweenKeys = gapBetweenKeys;
	}

	public String getMinKeyCharCount() {
		return minKeyCharCount;
	}

	public void setMinKeyCharCount(String minKeyCharCount) {
		this.minKeyCharCount = minKeyCharCount;
	}

	public String getFetchValue() {
		return fetchValue;
	}

	public void setFetchValue(final String fetchValue) {
		this.fetchValue = fetchValue;
	}

	public String getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(final String multiplier) {
		this.multiplier = multiplier;
	}

	public int getMaxNumberRecordPerDlfInt() {
		return maxNumberRecordPerDlfInt;
	}

	public void setMaxNumberRecordPerDlfInt(final Integer maxNumberRecordPerDlfInt) {
		this.maxNumberRecordPerDlfInt = maxNumberRecordPerDlfInt;
	}

	public int getToleranceThresholdInt() {
		return toleranceThresholdInt;
	}

	public void setToleranceThresholdInt(final Integer toleranceThresholdInt) {
		this.toleranceThresholdInt = toleranceThresholdInt;
	}

	final public String[] getLocationArr() {
		return locationArr;
	}

	final public String getLocationOrder() {
		return locationOrder;
	}

	final public void setLocationOrder(final String locationOrder) {
		this.locationOrder = locationOrder;
		if (locationOrder == null || locationOrder.isEmpty()) {
			LOGGER.info("locationOrder is not defined in the specified property file ....");
			throw new DCMABusinessException("locationOrder is not definedin the specified property file ....");
		}
		this.locationArr = locationOrder.split(KVFieldCreatorConstants.SEMI_COLON);
	}

	final public String getMaxNumberRecordPerDlf() {
		return maxNumberRecordPerDlf;
	}

	final public void setMaxNumberRecordPerDlf(final String maxNumberRecordPerDlf) {
		this.maxNumberRecordPerDlf = maxNumberRecordPerDlf;
		Integer maxRecordsPerDlf = KVFieldCreatorConstants.DEFAULT_MAX_RECORD_PER_DLF;
		try {
			maxRecordsPerDlf = Integer.parseInt(maxNumberRecordPerDlf);
		} catch (NumberFormatException nfe) {
			LOGGER.info("cannot parse maxNumberRecordPerDlf from property file . maxNumberRecordPerDlf : " + maxNumberRecordPerDlf);
		}
		setMaxNumberRecordPerDlfInt(maxRecordsPerDlf);
	}

	final public String getToleranceThreshold() {
		return toleranceThreshold;
	}

	final public void setToleranceThreshold(final String toleranceThreshold) {
		this.toleranceThreshold = toleranceThreshold;
		Integer toleranceThresholdInt = KVFieldCreatorConstants.DEFAULT_TOLEANCE_THRESHOLD;
		try {
			toleranceThresholdInt = Integer.parseInt(toleranceThreshold);
		} catch (NumberFormatException nfe) {
			LOGGER.info("Cannot parse tolerance from properties field . ToleranceThershold : " + toleranceThreshold);
		}
		setToleranceThresholdInt(toleranceThresholdInt);
	}

	private List<String> keyRegexList = null;

	private List<String> valueRegexList = null;

	@Autowired
	private BatchSchemaService batchSchemaService;

	@Autowired
	private BatchClassService batchClassService;

	@Autowired
	private KVFinderService kvFinderService;

	@Autowired
	private BatchInstanceService batchInstanceService;

	@Autowired
	private KVExtractionService kvExtractionService;

	/**
	 * Instance of PluginPropertiesService.
	 **/
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
	 * This method creates key value field for each document level field of every document.
	 * 
	 * @param batchInstanceIdentifier
	 * @param pluginWorkflow
	 * @throws DCMAApplicationException
	 */
	public void createKeyValueFields(String batchInstanceIdentifier, String pluginWorkflow) throws DCMAApplicationException {
		String kvFieldCreatorSwitch = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, KV_FIELD_LEARNING_PLUGIN,
				KVFieldCreatorProperties.LEARNING_KEY_VALUE_SWTICH);
		LOGGER.info("KV creation plugin switch value  : " + kvFieldCreatorSwitch);
		if (KVFieldCreatorConstants.SWITCH_ON.equalsIgnoreCase(kvFieldCreatorSwitch)) {
			if (null == keyRegexList) {
				keyRegexList = new ArrayList<String>();
				readRegexPropertyFile(KVFieldCreatorConstants.KEY_REGEX_FILE_PATH, keyRegexList);
			}
			if (null == valueRegexList) {
				valueRegexList = new ArrayList<String>();
				readRegexPropertyFile(KVFieldCreatorConstants.VALUE_REGEX_FILE_PATH, valueRegexList);
			}
			Float multiplierFloat = getMultiplierFloat();
			KVFetchValue kvFetchValue = getKVFetchValue();
			int minKeyCharsInt = getminKeyCharsInt();
			Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);
			BatchClass batchClass = batchClassService.getLoadedBatchClassByIdentifier(batchInstanceService
					.getBatchClassIdentifier(batchInstanceIdentifier));

			List<Document> documentList = batch.getDocuments().getDocument();
			boolean isSuccess = false;
			for (Document document : documentList) {
				DocumentLevelFields documentLevelFields = document.getDocumentLevelFields();
				if (null == documentLevelFields) {
					LOGGER.info("documentLevelFields is null for document : " + document.getType());
					continue;
				}
				List<DocField> docLevelFields = documentLevelFields.getDocumentLevelField();
				for (DocField docLevelField : docLevelFields) {
					KVExtraction kvExtractionField = new KVExtraction();
					String pageID = getPageId(document, docLevelField.getPage());
					String docLevelFieldValue = docLevelField.getValue();
					Map<String, List<LineDataCarrier>> pageIdToLineDataCarrier = new HashMap<String, List<LineDataCarrier>>();
					List<LineDataCarrier> lineDataCarrierList = null;
					if (pageID != null && docLevelFieldValue != null && !docLevelFieldValue.trim().isEmpty()) {
						if (pageIdToLineDataCarrier != null && !pageIdToLineDataCarrier.containsKey(pageID)) {
							final HocrPages hocrPages = batchSchemaService.getHocrPages(batchInstanceIdentifier, pageID);
							if (null == hocrPages) {
								LOGGER.info("hocrPages is null. pageID = " + pageID + ". batchInstanceIdentifier = "
										+ batchInstanceIdentifier);
								continue;
							}
							HocrPage hocrPage = hocrPages.getHocrPage().get(0);
							Spans spans = hocrPage.getSpans();
							if (hocrPage != null && spans != null) {
								lineDataCarrierList = createLineDataCarrier(spans, hocrPage.getPageID());
							}
							if (lineDataCarrierList != null) {
								pageIdToLineDataCarrier.put(pageID, lineDataCarrierList);
							}
						} else {
							lineDataCarrierList = pageIdToLineDataCarrier.get(pageID);
						}
						isSuccess = createKeyValuePattern(kvExtractionField, lineDataCarrierList, docLevelField, minKeyCharsInt);
						if (!isSuccess) {
							LOGGER.info("Key value field not created for DLF " + docLevelField.getName() + " for document "
									+ document.getType());
							continue;
						} else {
							kvExtractionField.setMultiplier(multiplierFloat);
							kvExtractionField.setFetchValue(kvFetchValue);
							// kvExtractionField.setPageValue();
							addKVField(batchClass, document, docLevelField, kvExtractionField);
						}
					}
				}
			}
			batchClassService.merge(batchClass);
		}
	}
	
	private List<KVExtraction> createKeyValuePatternList(KVFetchValue kvFetchValue, Float multiplierFloat, final String value, final List<LineDataCarrier> lineDataCarrierList, final int minKeyCharsInt) throws DCMAApplicationException {
		List<KVExtraction> kvExtractionList = new ArrayList<KVExtraction>();
		boolean valueFound = false;
		boolean keyFound = false;
			if (null != lineDataCarrierList) {
				for (LineDataCarrier lineDataCarrier : lineDataCarrierList) {
					if (null != lineDataCarrier) {
						LOGGER.info("Searching for value: " + value + " in row == " + lineDataCarrier.getLineRowData());
						List<Span> spanList = lineDataCarrier.getSpanList();
						if (null != spanList) {
							for (Span span : spanList) {
								if (null != span) {
									String valueSpan = span.getValue();
									if (valueSpan != null && value.contains(valueSpan)) {
										KVExtraction kvExtractionField = new KVExtraction();
										String valuePattern = getRegexPattern(valueSpan, valueRegexList);
										kvExtractionField.setValuePattern(valuePattern);
										keyFound = searchKey(kvExtractionField, span.getCoordinates(), span, lineDataCarrier,
												lineDataCarrierList, minKeyCharsInt);
										kvExtractionField.setMultiplier(multiplierFloat);
										kvExtractionField.setFetchValue(kvFetchValue);
										kvExtractionList.add(kvExtractionField);
										LOGGER.info("Value Found=" + valueFound);
										LOGGER.info("Key Found=" + keyFound);
									}
								}
							}
						}
					}
				}
			}
		return kvExtractionList;
	}
	
	public List<KVExtraction> createKeyValueFieldsAPI(final String value, final HocrPage hocrPage) throws DCMAApplicationException {
		if (null == keyRegexList) {
			keyRegexList = new ArrayList<String>();
			readRegexPropertyFile(KVFieldCreatorConstants.KEY_REGEX_FILE_PATH, keyRegexList);
		}
		if (null == valueRegexList) {
			valueRegexList = new ArrayList<String>();
			readRegexPropertyFile(KVFieldCreatorConstants.VALUE_REGEX_FILE_PATH, valueRegexList);
		}
		Float multiplierFloat = getMultiplierFloat();
		KVFetchValue kvFetchValue = getKVFetchValue();
		int minKeyCharsInt = getminKeyCharsInt();

		List<KVExtraction> kvExtractionList = new ArrayList<KVExtraction>();
		List<LineDataCarrier> lineDataCarrierList = createLineDataCarrier(hocrPage.getSpans(), hocrPage.getPageID());
		kvExtractionList = createKeyValuePatternList(kvFetchValue, multiplierFloat, value, lineDataCarrierList, minKeyCharsInt);
		return kvExtractionList;
	}

	private int getminKeyCharsInt() {
		int minKeyCharsInt = KVFieldCreatorConstants.MIN_KEY_CHAR_COUNT;
		try {
			if (minKeyCharCount != null) {
				minKeyCharsInt = Integer.parseInt(minKeyCharCount);
			} else {
				LOGGER.error("No min_key_char_count specified, setting it to its default value '3'...");
			}
		} catch (NumberFormatException e) {
			LOGGER
					.error("Inavlid value of min_key_characters value specified in properties file..., setting it to its default value 3..");
			minKeyCharsInt = KVFieldCreatorConstants.MIN_KEY_CHAR_COUNT;
		}
		return minKeyCharsInt;
	}

	private Float getMultiplierFloat() {
		float multilierFloat = KVFieldCreatorConstants.DEFAULT_MULTIPLIER;
		try {
			multilierFloat = Float.parseFloat(multiplier);
		} catch (NumberFormatException nfe) {
			LOGGER.error("Couldn't parse multiplier value, setting it to its default value 1.", nfe);
		}
		return multilierFloat;
	}

	private KVFetchValue getKVFetchValue() {
		KVFetchValue kvFetchValue = KVFetchValue.ALL;
		try {
			if (fetchValue == null) {
				LOGGER.error("No fetch_value specified, setting it to its default value ALL.");
			} else {
				kvFetchValue = KVFetchValue.valueOf(fetchValue);
			}
		} catch (IllegalArgumentException illArgExcep) {
			LOGGER.error("Cannot parse value for fetch_value specified in properties file, setting its default value to ALL.",
					illArgExcep);
		}
		return kvFetchValue;
	}

	/**
	 * This method adds the key value field to the batch class.
	 * 
	 * @param batchClass
	 * @param document
	 * @param docLevelField
	 * @param kvExtractionField
	 */
	private void addKVField(final BatchClass batchClass, final Document document, final DocField docLevelField,
			final KVExtraction kvExtractionField) {
		boolean fieldAdded = false;
		boolean isDuplicateField = false;
		String docFieldName = docLevelField.getName();
		for (DocumentType documentType : batchClass.getDocumentTypes()) {
			if (documentType.getName().equals(document.getType())) {
				List<FieldType> fieldTypes = documentType.getFieldTypes();
				if (fieldTypes == null) {
					LOGGER.info("Field type is null for document type : " + documentType.getName());
					continue;
				}
				for (FieldType fieldType : fieldTypes) {
					String fieldName = fieldType.getName();
					if (fieldName != null && docFieldName != null && fieldName.equals(docFieldName)) {
						if (isDuplicateKVField(fieldType, kvExtractionField)) {
							LOGGER.info("KV field duplicate for field type : " + fieldName);
							isDuplicateField = true;
						} else {
							List<KVExtraction> kvExtractionList = fieldType.getKvExtraction();
							if (kvExtractionList == null) {
								fieldType.setKvExtraction(new ArrayList<KVExtraction>());
							}
							if (kvExtractionList.size() < getMaxNumberRecordPerDlfInt()) {
								LOGGER.info("Field added to field type " + fieldName + " of document type " + documentType.getName());
								fieldType.addKVExtraction(kvExtractionField);
								fieldAdded = true;
								break;
							}
						}
					}
				}
				if (fieldAdded || isDuplicateField) {
					break;
				}
			}
		}
	}

	/**
	 * This method checks if the new KV field already exists for the field type.
	 * 
	 * @param fieldType
	 * @param kvExtractionField
	 * @return
	 */
	private boolean isDuplicateKVField(FieldType fieldType, KVExtraction kvExtractionField) {
		LOGGER.info("checking for duplicate key value field ..");
		boolean isDuplicateField = false;
		List<KVExtraction> kvExtractionList = kvExtractionService.getDuplicateKVFields(fieldType, kvExtractionField);
		if (kvExtractionList != null && kvExtractionList.size() > 0) {
			LOGGER.info("KV field is duplicate.");
			isDuplicateField = true;
		} else {
			LOGGER.info("KV field is not duplicate.");
		}
		return isDuplicateField;
	}

	private void readRegexPropertyFile(String filePath, List<String> regexList) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(filePath)));
			String pattern = reader.readLine();
			while (pattern != null) {
				regexList.add(pattern);
				pattern = reader.readLine();
			}
		} catch (FileNotFoundException fnfe) {
			LOGGER.info("File not found at path speified.");
		} catch (IOException ioe) {
			LOGGER.info("Error reading from property file.");
		}
	}

	/**
	 * This method creates key value pattern for doc level field.
	 * 
	 * @param kvExtractionField
	 * @param lineDataCarrierList
	 * @param docLevelField
	 * @param minKeyCharsInt
	 * @return keyFound
	 * @throws DCMAApplicationException
	 */
	private boolean createKeyValuePattern(final KVExtraction kvExtractionField, final List<LineDataCarrier> lineDataCarrierList,
			final DocField docLevelField, final int minKeyCharsInt) throws DCMAApplicationException {
		CoordinatesList coordinates = docLevelField.getCoordinatesList();
		boolean valueFound = false;
		boolean keyFound = false;
		if (coordinates != null) {
			List<Coordinates> coordinatesList = coordinates.getCoordinates();
			Coordinates recCoordinates = getRectangleCoordinates(coordinatesList);
			String docLevelFieldValue = docLevelField.getValue();
			if (null != lineDataCarrierList) {
				for (LineDataCarrier lineDataCarrier : lineDataCarrierList) {
					if (null != lineDataCarrier) {
						LOGGER.info("Searching for value: " + docLevelFieldValue + " in row == " + lineDataCarrier.getLineRowData());
						List<Span> spanList = lineDataCarrier.getSpanList();
						if (null != spanList) {
							for (Span span : spanList) {
								if (null != span) {
									String value = span.getValue();
									if (value != null && docLevelFieldValue.contains(value) && matchCoordinates(span, recCoordinates)) {
										String valuePattern = getRegexPattern(value, valueRegexList);
										kvExtractionField.setValuePattern(valuePattern);
										keyFound = searchKey(kvExtractionField, recCoordinates, span, lineDataCarrier,
												lineDataCarrierList, minKeyCharsInt);
										valueFound = true;
										break;
									}
								}
							}
						}
						if (valueFound) {
							break;
						}
					}
				}
			}
		}
		LOGGER.info("Value Found=" + valueFound);
		LOGGER.info("Key Found=" + keyFound);
		return keyFound && valueFound;
	}

	/**
	 * This method look for key in directions as ordered in locationArr array.
	 * 
	 * @param kvExtractionField
	 * @param recCoordinates
	 * @param span
	 * @param lineDataCarrier
	 * @param lineDataCarrierList
	 * @param minKeyCharsInt
	 * @return {@link Boolean}
	 * @throws DCMAApplicationException
	 */
	private boolean searchKey(final KVExtraction kvExtractionField, final Coordinates recCoordinates, final Span span,
			LineDataCarrier lineDataCarrier, final List<LineDataCarrier> lineDataCarrierList, int minKeyCharsInt)
			throws DCMAApplicationException {
		boolean keyFound = false;
		try {
			Coordinates keyCoordinates = new Coordinates();
			if (locationOrder == null || locationOrder.isEmpty()) {
				LOGGER.info("locationOrder is not defined in the specified property file ....");
				throw new DCMAApplicationException("locationOrder is not definedin the specified property file ....");
			}

			for (String location : this.getLocationArr()) {
				LocationType locationType = LocationType.valueOf(location);
				switch (locationType) {
					case TOP:
					case TOP_LEFT:
					case TOP_RIGHT:
						LOGGER.info("Searching key at location : " + locationType);
						keyFound = createKeyTop(kvExtractionField, recCoordinates, lineDataCarrier, lineDataCarrierList, locationType,
								keyCoordinates, minKeyCharsInt);
						break;

					case RIGHT:
						LOGGER.info("Searching key at location : " + locationType);
						keyFound = createKeyRight(kvExtractionField, recCoordinates, span, lineDataCarrier, locationType,
								keyCoordinates, minKeyCharsInt);
						break;

					case LEFT:
						LOGGER.info("Searching key at location : " + locationType);
						keyFound = createKeyLeft(kvExtractionField, recCoordinates, span, lineDataCarrier, locationType,
								keyCoordinates, minKeyCharsInt);
						break;

					case BOTTOM:
					case BOTTOM_LEFT:
					case BOTTOM_RIGHT:
						LOGGER.info("Searching key at location : " + locationType);
						keyFound = createKeyBottom(kvExtractionField, recCoordinates, lineDataCarrier, lineDataCarrierList,
								locationType, keyCoordinates, minKeyCharsInt);
						break;

					default:
						LOGGER.info("***********  Default case found. In valid case.*************");
						break;
				}
				if (keyFound) {
					LOGGER.info("Key found at location " + location);
					findLocation(kvExtractionField, keyCoordinates, recCoordinates);
					break;
				} else {
					LOGGER.info("Key not found at location " + location);
				}
			}
		} catch (IllegalArgumentException illArgExcep) {
			LOGGER.error("Invalid location specified in the properties file....", illArgExcep);
			throw new DCMAApplicationException("Invalid location specified in the properties file....", illArgExcep);
		}
		return keyFound;
	}

	/**
	 * This method sets the length, width, x-offset and y-offset values of the new KV Field.
	 * 
	 * @param kvExtractionField {@link KVExtraction}
	 * @param locationType {@link LocationType}
	 * @param keyCoordinates {@link Coordinates}
	 * @param valueCoordinates {@link Coordinates}
	 */
	private void setValues(final KVExtraction kvExtractionField, final LocationType locationType, final Coordinates keyCoordinates,
			final Coordinates valueCoordinates) {
		long keyX0 = keyCoordinates.getX0().longValue();
		long keyY0 = keyCoordinates.getY0().longValue();
		long keyX1 = keyCoordinates.getX1().longValue();
		long keyY1 = keyCoordinates.getY1().longValue();

		long valueX0 = valueCoordinates.getX0().longValue();
		long valueY0 = valueCoordinates.getY0().longValue();
		long valueX1 = valueCoordinates.getX1().longValue();
		long valueY1 = valueCoordinates.getY1().longValue();

		int lengthOfBoxInInt = (int) Math.round(valueX1 - valueX0);
		int widthOfBoxInInt = (int) Math.round(valueY1 - valueY0);
		double xOffset = 0;
		double yOffset = 0;

		switch (locationType) {
			case TOP_LEFT:
				xOffset = keyX0 - valueCoordinates.getX1().longValue();
				yOffset = keyY0 - valueCoordinates.getY1().longValue();
				break;
			case TOP_RIGHT:
				xOffset = valueCoordinates.getX0().longValue() - keyX1;
				yOffset = keyY0 - valueCoordinates.getY1().longValue();
				break;
			case TOP:
				xOffset = 0;
				yOffset = keyY0 - valueCoordinates.getY1().longValue();
				break;
			case LEFT:
				yOffset = 0;
				xOffset = keyX0 - valueCoordinates.getX1().longValue();
				break;
			case RIGHT:
				yOffset = 0;
				xOffset = valueCoordinates.getX0().longValue() - keyX1;
				break;
			case BOTTOM_LEFT:
				xOffset = keyX0 - valueCoordinates.getX1().longValue();
				yOffset = valueCoordinates.getY0().longValue() - keyY1;
				break;
			case BOTTOM_RIGHT:
				xOffset = valueCoordinates.getX0().longValue() - keyX1;
				yOffset = valueCoordinates.getY0().longValue() - keyY1;
				break;
			case BOTTOM:
				xOffset = 0;
				yOffset = valueCoordinates.getY0().longValue() - keyY1;
				break;
			default:
				LOGGER.info("Invalid location = " + locationType);
				break;
		}

		int xOffsetInInt = (int) Math.round(xOffset);
		int yOffsetInInt = (int) Math.round(yOffset);

		lengthOfBoxInInt = lengthOfBoxInInt + (getToleranceThresholdInt() * lengthOfBoxInInt) / 100;
		widthOfBoxInInt = widthOfBoxInInt + (getToleranceThresholdInt() * widthOfBoxInInt) / 100;
		kvExtractionField.setXoffset(xOffsetInInt);
		kvExtractionField.setYoffset(yOffsetInInt);
		kvExtractionField.setLength(lengthOfBoxInInt);
		kvExtractionField.setWidth(widthOfBoxInInt);
		kvExtractionField.setLocationType(locationType);
	}

	/**
	 * This method creates key at TOP,TOP_LEFT and TOP_RIGHT of value.
	 * 
	 * @param kvExtractionField {@link KVExtraction}
	 * @param recCoordinates
	 * @param lineDataCarrier
	 * @param lineDataCarrierList
	 * @param location
	 * @param keyCoordinates
	 * @param minKeyCharsInt
	 * @return {@link Boolean}
	 */
	private boolean createKeyTop(final KVExtraction kvExtractionField, final Coordinates recCoordinates,
			final LineDataCarrier lineDataCarrier, final List<LineDataCarrier> lineDataCarrierList, final LocationType location,
			final Coordinates keyCoordinates, int minKeyCharsInt) {
		boolean keyFound = false;
		LOGGER.info("Creating key at location : " + location.toString());
		int index = lineDataCarrierList.indexOf(lineDataCarrier);
		Coordinates spanCoordinates = null;
		LineDataCarrier topLineDataCarrier = null;
		while (index > 0) {
			topLineDataCarrier = lineDataCarrierList.get(--index);
			List<Span> spanList = new ArrayList<Span>();
			spanList.addAll(topLineDataCarrier.getSpanList());
			if (LocationType.TOP_LEFT.equals(location)) {
				Collections.reverse(spanList);
			}
			for (Span span : spanList) {
				if (span == null) {
					continue;
				}
				spanCoordinates = span.getCoordinates();
				String key = span.getValue();
				if (key != null && !key.trim().isEmpty() && !KVFieldCreatorConstants.KEY_VALUE_SEPARATORS.contains(key)
						&& isValidKey(recCoordinates, spanCoordinates, location)) {
					if (LocationType.TOP_RIGHT.equals(location)) {
						keyFound = concatenateKeysRight(kvExtractionField, topLineDataCarrier, span, minKeyCharsInt, keyCoordinates);
					} else {
						keyFound = concatenateKeysLeft(kvExtractionField, topLineDataCarrier, span, minKeyCharsInt, keyCoordinates);
					}
					LOGGER.info("Key found at location: " + keyFound);
					break;
				}
			}
			if (keyFound) {
				break;
			}
		}
		return keyFound;
	}

	/**
	 * This method creates key at BOTTOM,BOTTOM_LEFT and BOTTOM_RIGHT of value.
	 * 
	 * @param kvExtractionField {@link KVExtraction}
	 * @param recCoordinates {@link Coordinates}
	 * @param lineDataCarrier {@link LineDataCarrier}
	 * @param lineDataCarrierList {@link List<LineDataCarrier>}
	 * @param location {@link LocationType}
	 * @param keyCoordinates
	 * @param keyCoordinates {@link Coordinates}
	 * @param minKeyCharsInt
	 * @return {@link Boolean}
	 */
	private boolean createKeyBottom(final KVExtraction kvExtractionField, final Coordinates recCoordinates,
			final LineDataCarrier lineDataCarrier, final List<LineDataCarrier> lineDataCarrierList, final LocationType location,
			final Coordinates keyCoordinates, final int minKeyCharsInt) {
		boolean keyFound = false;
		LOGGER.info("Creating key at location : " + location.toString());
		int index = lineDataCarrierList.indexOf(lineDataCarrier);
		int length = lineDataCarrierList.size();
		Coordinates spanCoordinates = null;
		LineDataCarrier bottomLineDataCarrier = null;
		while (index < length - 1) {
			bottomLineDataCarrier = lineDataCarrierList.get(++index);
			List<Span> spanList = new ArrayList<Span>();
			spanList.addAll(bottomLineDataCarrier.getSpanList());
			if (LocationType.BOTTOM_LEFT.equals(location)) {
				Collections.reverse(spanList);
			}
			for (Span span : spanList) {
				if (span == null) {
					continue;
				}
				spanCoordinates = span.getCoordinates();
				String key = span.getValue();
				if (key != null && !key.trim().isEmpty() && !KVFieldCreatorConstants.KEY_VALUE_SEPARATORS.contains(key)
						&& isValidKey(recCoordinates, spanCoordinates, location)) {
					if (LocationType.BOTTOM_RIGHT.equals(location)) {
						keyFound = concatenateKeysRight(kvExtractionField, bottomLineDataCarrier, span, minKeyCharsInt, keyCoordinates);
					} else {
						keyFound = concatenateKeysLeft(kvExtractionField, bottomLineDataCarrier, span, minKeyCharsInt, keyCoordinates);
					}
					LOGGER.info("Key found at location: " + keyFound);
					break;
				}
			}
			if (keyFound) {
				break;
			}
		}
		return keyFound;
	}

	/**
	 * This method searches key to left of value.
	 * 
	 * @param kvExtractionField {@link KVExtraction}
	 * @param recCoordinates
	 * @param span
	 * @param lineDataCarrier
	 * @param location
	 * @param keyCoordinates
	 * @param minKeyCharsInt
	 * @return {@link Boolean}
	 */
	private boolean createKeyLeft(KVExtraction kvExtractionField, Coordinates recCoordinates, Span span,
			LineDataCarrier lineDataCarrier, LocationType location, Coordinates keyCoordinates, int minKeyCharsInt) {
		LOGGER.info("Creating key at location : " + location.toString());
		int prevKeyX0 = 0;
		boolean keyFound = false;
		int gapBetweenWords = 0;
		StringBuffer keyString = new StringBuffer();
		List<Coordinates> keyCoordinatesList = new ArrayList<Coordinates>();
		Integer spanIndex = lineDataCarrier.getIndexOfSpan(span);
		if (spanIndex != null) {
			Span leftSpan = lineDataCarrier.getLeftSpan(spanIndex);
			while (leftSpan != null) {
				String key = leftSpan.getValue();
				if (keyFound && null != key && !key.trim().isEmpty()) {
					gapBetweenWords = prevKeyX0 - leftSpan.getCoordinates().getX1().intValue();
					if (Math.abs(gapBetweenWords) < gapBetweenKeys) {
						keyCoordinatesList.add(leftSpan.getCoordinates());
						keyString.insert(0, key.trim() + KVFieldCreatorConstants.SPACE);
						prevKeyX0 = leftSpan.getCoordinates().getX0().intValue();
						LOGGER.info("Key == " + keyString);
					} else {
						keyFound = setKeyPattern(kvExtractionField, keyCoordinates, minKeyCharsInt, keyString, keyCoordinatesList);
						if (keyFound) {
							break;
						}
					}
				} else if (key != null && !key.trim().isEmpty() && !KVFieldCreatorConstants.KEY_VALUE_SEPARATORS.contains(key)
						&& isValidKey(recCoordinates, leftSpan.getCoordinates(), location)) {
					keyCoordinatesList.add(leftSpan.getCoordinates());
					prevKeyX0 = leftSpan.getCoordinates().getX0().intValue();
					keyString.append(key.trim());
					LOGGER.info("Key == " + keyString);
					keyFound = true;
				}
				spanIndex = spanIndex - 1;
				leftSpan = lineDataCarrier.getLeftSpan(spanIndex);
			}
		}
		return keyFound;
	}

	/**
	 * This method searches key to right of value.
	 * 
	 * @param kvExtractionField {@link KVExtraction}
	 * @param recCoordinates
	 * @param span
	 * @param lineDataCarrier
	 * @param location
	 * @param keyCoordinates
	 * @param minKeyCharsInt
	 * @return {@link Boolean}
	 */
	private boolean createKeyRight(KVExtraction kvExtractionField, Coordinates recCoordinates, Span span,
			LineDataCarrier lineDataCarrier, LocationType location, Coordinates keyCoordinates, int minKeyCharsInt) {
		LOGGER.info("Creating key at location : " + location.toString());
		boolean keyFound = false;
		Integer spanIndex = lineDataCarrier.getIndexOfSpan(span);
		if (spanIndex != null) {
			Span rightSpan = lineDataCarrier.getRightSpan(spanIndex);
			spanIndex = spanIndex + 1;
			int prevKeyX1 = 0;
			int gapBetweenWords = 0;
			StringBuffer keyString = new StringBuffer();
			List<Coordinates> keyCoordinatesList = new ArrayList<Coordinates>();
			while (rightSpan != null) {
				String key = rightSpan.getValue();
				if (keyFound && null != key && !key.trim().isEmpty()) {
					gapBetweenWords = prevKeyX1 - rightSpan.getCoordinates().getX0().intValue();
					if (Math.abs(gapBetweenWords) < gapBetweenKeys) {
						keyCoordinatesList.add(rightSpan.getCoordinates());
						keyString.append(KVFieldCreatorConstants.SPACE + key.trim());
						prevKeyX1 = rightSpan.getCoordinates().getX1().intValue();
					} else {
						keyFound = setKeyPattern(kvExtractionField, keyCoordinates, minKeyCharsInt, keyString, keyCoordinatesList);
						if (keyFound) {
							break;
						}
					}
				} else if (key != null && !key.trim().isEmpty() && !KVFieldCreatorConstants.KEY_VALUE_SEPARATORS.contains(key)
						&& isValidKey(recCoordinates, rightSpan.getCoordinates(), location)) {
					keyCoordinatesList.add(rightSpan.getCoordinates());
					prevKeyX1 = rightSpan.getCoordinates().getX1().intValue();
					keyString.append(key.trim());
					keyFound = true;
				}
				spanIndex = spanIndex + 1;
				rightSpan = lineDataCarrier.getRightSpan(spanIndex);
			}
		}
		return keyFound;
	}

	private boolean setKeyPattern(KVExtraction kvExtractionField, Coordinates keyCoordinates, int minKeyCharsInt,
			StringBuffer keyString, List<Coordinates> keyCoordinatesList) {
		boolean keyFound;
		final String finalKeyString = keyString.toString().trim();
		if (finalKeyString.length() >= minKeyCharsInt) {
			LOGGER.info("Key found: " + finalKeyString);
			final String keyPattern = getRegexPattern(finalKeyString, keyRegexList);
			kvExtractionField.setKeyPattern(keyPattern);
			setKeyCoordinates(keyCoordinates, keyCoordinatesList);
			LOGGER.info("Key Pattern Created : " + keyPattern);
			keyFound = true;
		} else {
			LOGGER.info("Required no of characters in key= " + minKeyCharsInt + " , Chars found= "
					+ keyString.toString().trim().length());
			keyFound = false;
		}
		return keyFound;
	}

	/**
	 * This method checks if span is valid with respect to value coordinates.
	 * 
	 * @param recCoordinates
	 * @param spanCoordinates
	 * @param location
	 * @return {@link Boolean}
	 */
	private boolean isValidKey(Coordinates recCoordinates, Coordinates spanCoordinates, LocationType location) {
		boolean isValidKey = false;
		LOGGER.info("Creating key at location : " + location.toString());
		long rectangleX0 = recCoordinates.getX0().longValue();
		long rectangleY0 = recCoordinates.getY0().longValue();
		long rectangleX1 = recCoordinates.getX1().longValue();
		long rectangleY1 = recCoordinates.getY1().longValue();

		long spanX0 = spanCoordinates.getX0().longValue();
		long spanY0 = spanCoordinates.getY0().longValue();
		long spanX1 = spanCoordinates.getX1().longValue();
		long spanY1 = spanCoordinates.getY1().longValue();

		long diffX0 = Math.abs(spanX0 - rectangleX0);
		long diffX1 = Math.abs(spanX1 - rectangleX1);

		if ((spanX0 <= rectangleX0 && spanX1 <= rectangleX0) || (spanX0 >= rectangleX1 && spanX1 >= rectangleX1)
				|| (spanY0 <= rectangleY0 && spanY1 <= rectangleY0) || (spanY0 >= rectangleY1 && spanY1 >= rectangleY1)) {
			isValidKey = true;
		}

		switch (location) {
			case BOTTOM:
			case TOP:
				if (isValidKey
						&& ((spanX0 <= rectangleX0 && spanX1 >= rectangleX1) || (spanX0 >= rectangleX0 && spanX1 <= rectangleX1)
								|| ((spanX0 >= rectangleX0 && spanX0 <= rectangleX1) && (diffX0 >= diffX1)) || ((spanX1 >= rectangleX0 && spanX1 <= rectangleX1) && (diffX0 <= diffX1)))) {
					isValidKey = true;
				} else {
					isValidKey = false;
				}
				break;

			case BOTTOM_LEFT:
			case TOP_LEFT:
				if (isValidKey && (spanX1 <= rectangleX0 || (spanX0 <= rectangleX0) && (diffX0 >= diffX1))) {
					isValidKey = true;
				} else {
					isValidKey = false;
				}
				break;

			case BOTTOM_RIGHT:
			case TOP_RIGHT:
				if (isValidKey && (spanX0 >= rectangleX1 || (spanX1 >= rectangleX1) && (diffX0 <= diffX1))) {
					isValidKey = true;
				} else {
					isValidKey = false;
				}
				break;

			default:
				break;
		}

		return isValidKey;
	}

	/**
	 * This method gets the rectangle coordinates for the value.
	 * 
	 * @param coordinatesList
	 * @return Coordinates
	 */
	private Coordinates getRectangleCoordinates(List<Coordinates> coordinatesList) {
		long minX0 = 0;
		long minY0 = 0;
		long maxX1 = 0;
		long maxY1 = 0;
		Coordinates recCoord = new Coordinates();
		boolean isFirstCoord = true;
		for (Coordinates coordinate : coordinatesList) {
			if (isFirstCoord) {
				recCoord = coordinate;
				isFirstCoord = false;
			} else {
				minX0 = recCoord.getX0().longValue();
				minY0 = recCoord.getY0().longValue();
				maxX1 = recCoord.getX1().longValue();
				maxY1 = recCoord.getY1().longValue();
				long coordX0 = coordinate.getX0().longValue();
				long coordY0 = coordinate.getY0().longValue();
				long coordX1 = coordinate.getX1().longValue();
				long coordY1 = coordinate.getY1().longValue();
				if (coordX0 < minX0) {
					recCoord.setX0(coordinate.getX0());
				}
				if (coordY0 < minY0) {
					recCoord.setY0(coordinate.getY0());
				}
				if (coordX1 > maxX1) {
					recCoord.setX1(coordinate.getX1());
				}
				if (coordY1 > maxY1) {
					recCoord.setY1(coordinate.getY1());
				}
			}
		}
		return recCoord;
	}

	/**
	 * This method checks if span exists inside the value rectangle zone.
	 * 
	 * @param span {@link Span}
	 * @param recCoordinates {@link List<Coordinates}
	 * @return {@link Boolean}
	 */
	private boolean matchCoordinates(final Span span, final Coordinates recCoordinates) {
		boolean isValidCoor = false;
		Coordinates spanCoordinates = span.getCoordinates();
		long spanX0 = spanCoordinates.getX0().longValue();
		long spanY0 = spanCoordinates.getY0().longValue();
		long spanX1 = spanCoordinates.getX1().longValue();
		long spanY1 = spanCoordinates.getY1().longValue();
		long valueX0 = recCoordinates.getX0().longValue();
		long valueY0 = recCoordinates.getY0().longValue();
		long valueX1 = recCoordinates.getX1().longValue();
		long valueY1 = recCoordinates.getY1().longValue();
		// Verify whether span lie inside value rectangle.
		if ((spanX0 >= valueX0 && spanX0 <= valueX1) && (spanX1 >= valueX0 && spanX0 <= valueX1)
				&& (spanY0 >= valueY0 && spanY0 <= valueY1) && (spanY1 >= valueY0 && spanY1 <= valueY1)) {
			isValidCoor = true;
		}
		return isValidCoor;
	}

	/**
	 * Creates the line data carrier list.
	 * 
	 * @param spans {@link Spans}
	 * @param pageID {@link String}
	 * @return List<LineDataCarrier>
	 */
	private List<LineDataCarrier> createLineDataCarrier(final Spans spans, final String pageID) {

		final List<Span> mainSpanList = spans.getSpan();
		List<LineDataCarrier> lineDataCarrierList = new ArrayList<LineDataCarrier>();

		if (null != mainSpanList) {

			int defaultValue = 0;
			try {
				defaultValue = Integer.parseInt(kvFinderService.getWidthOfLine());
			} catch (NumberFormatException nfe) {
				LOGGER.error("Error in reading line width from properties file, setting iit to 20.", nfe);
				defaultValue = KVFieldCreatorConstants.DEFAULT_WIDTH_OF_LINE;
			}

			final int compareValue = defaultValue;

			final Set<Span> set = new TreeSet<Span>(new Comparator<Span>() {

				public int compare(final Span firstSpan, final Span secSpan) {
					BigInteger s1Y1 = firstSpan.getCoordinates().getY1();
					BigInteger s2Y1 = secSpan.getCoordinates().getY1();
					int returnValue = 0;
					int compare = s1Y1.intValue() - s2Y1.intValue();
					if (compare >= -compareValue && compare <= compareValue) {
						BigInteger s1X1 = firstSpan.getCoordinates().getX1();
						BigInteger s2X1 = secSpan.getCoordinates().getX1();
						returnValue = s1X1.compareTo(s2X1);
					} else {
						returnValue = s1Y1.compareTo(s2Y1);
					}
					return returnValue;
				}
			});

			set.addAll(mainSpanList);

			LineDataCarrier lineDataCarrier = new LineDataCarrier(pageID);
			lineDataCarrierList.add(lineDataCarrier);
			List<Span> spanList = lineDataCarrier.getSpanList();

			for (Span span : set) {
				if (spanList.isEmpty()) {
					spanList.add(span);
				} else {
					Span lastSpan = spanList.get(spanList.size() - 1);
					int compare = lastSpan.getCoordinates().getY1().intValue() - span.getCoordinates().getY1().intValue();
					if (compare >= -defaultValue && compare <= defaultValue) {
						spanList.add(span);
					} else {
						lineDataCarrier = new LineDataCarrier(pageID);
						lineDataCarrierList.add(lineDataCarrier);
						spanList = lineDataCarrier.getSpanList();
						spanList.add(span);
					}
				}
			}
		}
		return lineDataCarrierList;
	}

	private String getPageId(Document document, String pageIdentifier) {
		String pageID = null;
		Pages pages = document.getPages();
		if (null != pages) {
			for (Page page : pages.getPage()) {
				pageID = page.getIdentifier();
				if (pageID.equals(pageIdentifier)) {
					pageID = page.getIdentifier();
				}
			}
		}
		return pageID;
	}

	/**
	 * This method gets the matched regex pattern for input string.
	 * 
	 * @param inputString
	 * @param regexList
	 * @return String
	 */
	private String getRegexPattern(String inputString, List<String> regexList) {
		String matchedPattern = null;
		int confidenceInt = 100;
		Pattern pattern = null;
		Matcher matcher = null;
		float previousMatchedConfidence = 0;
		if (null == inputString || inputString.isEmpty()) {
			LOGGER.info("Input string is null or empty.");
		} else {
			String dlfValue = inputString.split(KVFieldCreatorConstants.SPACE)[0];
			for (String regex : regexList) {
				pattern = Pattern.compile(regex);
				matcher = pattern.matcher(dlfValue);
				while (matcher.find()) {
					for (int i = 0; i <= matcher.groupCount(); i++) {
						final String groupStr = matcher.group(i);
						if (null != groupStr) {
							final float confidence = (groupStr.length() * confidenceInt) / inputString.length();
							if (confidence > previousMatchedConfidence) {
								previousMatchedConfidence = confidence;
								matchedPattern = regex;
							}
						}
					}
				}
			}
			if (matchedPattern == null) {
				LOGGER.info("No regex pattern found, setting input string as the regex itself. Pattern == " + inputString);
				matchedPattern = inputString;
			}
		}
		return matchedPattern;
	}

	/**
	 * Concatenate keys found at left location.
	 * 
	 * @param kvExtractionField
	 * @param lineDataCarrier
	 * @param span
	 * @param minKeyCharsInt
	 * @param keyCoordinates
	 * @return
	 */
	private boolean concatenateKeysLeft(final KVExtraction kvExtractionField, final LineDataCarrier lineDataCarrier, final Span span,
			final int minKeyCharsInt, final Coordinates keyCoordinates) {
		LOGGER.info("Concatenating keys on left....");
		boolean keyFound = false;
		String key = span.getValue();
		StringBuffer keyString = null;
		List<Coordinates> keyCoordinatesList = new ArrayList<Coordinates>();
		if (key != null) {
			keyString = new StringBuffer(key);
			keyCoordinatesList.add(span.getCoordinates());
			int prevKeyX0 = span.getCoordinates().getX0().intValue();
			int gapBetweenWords = 0;
			keyString = new StringBuffer(key);
			Integer spanIndex = lineDataCarrier.getIndexOfSpan(span);
			if (spanIndex != null) {
				Span leftSpan = lineDataCarrier.getLeftSpan(spanIndex);
				while (leftSpan != null) {
					key = leftSpan.getValue();
					if (null != key && !key.trim().isEmpty()) {
						gapBetweenWords = prevKeyX0 - leftSpan.getCoordinates().getX1().intValue();
						if (Math.abs(gapBetweenWords) < gapBetweenKeys) {
							LOGGER.info(key.trim() + " Concatenated on left.");
							keyCoordinatesList.add(leftSpan.getCoordinates());
							keyString.insert(0, key.trim() + KVFieldCreatorConstants.SPACE);
							prevKeyX0 = leftSpan.getCoordinates().getX0().intValue();
						} else {
							LOGGER.info("gap between words > " + gapBetweenKeys);
							break;
						}
					}
					spanIndex = spanIndex - 1;
					leftSpan = lineDataCarrier.getLeftSpan(spanIndex);
				}
			}
		}
		keyFound = setKeyPattern(kvExtractionField, keyCoordinates, minKeyCharsInt, keyString, keyCoordinatesList);
		return keyFound;
	}

	/**
	 * Concatenate keys found at right location.
	 * 
	 * @param kvExtractionField
	 * @param lineDataCarrier
	 * @param span
	 * @param minKeyCharsInt
	 * @param keyCoordinates
	 * @return
	 */
	private boolean concatenateKeysRight(final KVExtraction kvExtractionField, final LineDataCarrier lineDataCarrier, final Span span,
			final int minKeyCharsInt, final Coordinates keyCoordinates) {
		LOGGER.info("Concatenating keys on right....");
		boolean keyFound = false;
		String key = span.getValue();
		StringBuffer keyString = null;
		List<Coordinates> keyCoordinatesList = new ArrayList<Coordinates>();
		if (key != null) {
			keyString = new StringBuffer(key);
			keyCoordinatesList.add(span.getCoordinates());
			int prevKeyX1 = span.getCoordinates().getX1().intValue();
			int gapBetweenWords = 0;
			keyString = new StringBuffer(key);
			Integer spanIndex = lineDataCarrier.getIndexOfSpan(span);
			if (spanIndex != null) {
				Span rightSpan = lineDataCarrier.getLeftSpan(spanIndex);
				while (rightSpan != null) {
					key = rightSpan.getValue();
					if (null != key && !key.trim().isEmpty()) {
						gapBetweenWords = prevKeyX1 - rightSpan.getCoordinates().getX0().intValue();
						if (Math.abs(gapBetweenWords) < gapBetweenKeys) {
							LOGGER.info(key.trim() + " Concatenated on right.");
							keyCoordinatesList.add(rightSpan.getCoordinates());
							keyString.insert(0, key.trim() + KVFieldCreatorConstants.SPACE);
							prevKeyX1 = rightSpan.getCoordinates().getX0().intValue();
						} else {
							LOGGER.info("gap between words >= " + gapBetweenKeys);
							break;
						}
					}
					spanIndex = spanIndex - 1;
					rightSpan = lineDataCarrier.getRightSpan(spanIndex);
				}
			}
		}
		keyFound = setKeyPattern(kvExtractionField, keyCoordinates, minKeyCharsInt, keyString, keyCoordinatesList);
		return keyFound;
	}

	private void setKeyCoordinates(final Coordinates keyCoordinates, final List<Coordinates> keyCoordinatesList) {
		Coordinates recCoordinates = getRectangleCoordinates(keyCoordinatesList);
		if (null != recCoordinates) {
			keyCoordinates.setX0(recCoordinates.getX0());
			keyCoordinates.setY0(recCoordinates.getY0());
			keyCoordinates.setX1(recCoordinates.getX1());
			keyCoordinates.setY1(recCoordinates.getY1());
		}
	}

	public void findLocation(KVExtraction kvExtractionField, Coordinates keyCoordinates, Coordinates valueCoordinates) {

		if (keyCoordinates.getY0().intValue() >= valueCoordinates.getY1().intValue()) {
			// it is in top of key
			if (keyCoordinates.getX0().intValue() >= valueCoordinates.getX1().intValue()) {
				// it is in top left
				setValues(kvExtractionField, LocationType.TOP_LEFT, keyCoordinates, valueCoordinates);
			} else if (keyCoordinates.getX1().intValue() <= valueCoordinates.getX0().intValue()) {
				// it is in top right
				setValues(kvExtractionField, LocationType.TOP_RIGHT, keyCoordinates, valueCoordinates);
			} else if (keyCoordinates.getX0().intValue() <= valueCoordinates.getX0().intValue()
					&& keyCoordinates.getX1().intValue() >= valueCoordinates.getX1().intValue()) {
				// it is in top
				setValues(kvExtractionField, LocationType.TOP, keyCoordinates, valueCoordinates);
			} else if (keyCoordinates.getX0().intValue() >= valueCoordinates.getX0().intValue()
					&& keyCoordinates.getX1().intValue() >= valueCoordinates.getX1().intValue()) {
				// it is in top left
				setValues(kvExtractionField, LocationType.TOP_LEFT, keyCoordinates, valueCoordinates);
			} else if (keyCoordinates.getX0().intValue() <= valueCoordinates.getX0().intValue()
					&& keyCoordinates.getX1().intValue() <= valueCoordinates.getX1().intValue()) {
				// it is top right
				setValues(kvExtractionField, LocationType.TOP_RIGHT, keyCoordinates, valueCoordinates);
			} else {
				// it spans all three quadrants
				double x0diff = valueCoordinates.getX0().intValue() - keyCoordinates.getX0().intValue();
				double x1diff = valueCoordinates.getX1().intValue() - keyCoordinates.getX1().intValue();
				if (x0diff < x1diff) {
					setValues(kvExtractionField, LocationType.TOP_RIGHT, keyCoordinates, valueCoordinates);
				} else {
					setValues(kvExtractionField, LocationType.TOP_LEFT, keyCoordinates, valueCoordinates);
				}
			}
		} else if (keyCoordinates.getY1().intValue() <= valueCoordinates.getY0().intValue()) {
			// it is in bottom of key
			if (keyCoordinates.getX0().intValue() >= valueCoordinates.getX1().intValue()) {
				// it is in bottom left
				setValues(kvExtractionField, LocationType.BOTTOM_LEFT, keyCoordinates, valueCoordinates);
			} else if (keyCoordinates.getX1().intValue() <= valueCoordinates.getX0().intValue()) {
				// it is in bottom right
				setValues(kvExtractionField, LocationType.BOTTOM_RIGHT, keyCoordinates, valueCoordinates);
			} else if (keyCoordinates.getX0().intValue() <= valueCoordinates.getX0().intValue()
					&& keyCoordinates.getX1().intValue() >= valueCoordinates.getX1().intValue()) {
				// it is in bottom
				setValues(kvExtractionField, LocationType.BOTTOM, keyCoordinates, valueCoordinates);
			} else if (keyCoordinates.getX0().intValue() >= valueCoordinates.getX0().intValue()
					&& keyCoordinates.getX1().intValue() >= valueCoordinates.getX1().intValue()) {
				// it is in bottom left
				setValues(kvExtractionField, LocationType.BOTTOM_LEFT, keyCoordinates, valueCoordinates);
			} else if (keyCoordinates.getX0().intValue() <= valueCoordinates.getX0().intValue()
					&& keyCoordinates.getX1().intValue() <= valueCoordinates.getX1().intValue()) {
				// it is bottom right
				setValues(kvExtractionField, LocationType.BOTTOM_RIGHT, keyCoordinates, valueCoordinates);
			} else {
				// it spans all three quadrants
				double x0diff = valueCoordinates.getX0().intValue() - keyCoordinates.getX0().intValue();
				double x1diff = valueCoordinates.getX1().intValue() - keyCoordinates.getX1().intValue();
				if (x0diff < x1diff) {
					setValues(kvExtractionField, LocationType.BOTTOM_RIGHT, keyCoordinates, valueCoordinates);
				} else {
					setValues(kvExtractionField, LocationType.BOTTOM_LEFT, keyCoordinates, valueCoordinates);
				}
			}
		} else if (keyCoordinates.getY0().intValue() >= valueCoordinates.getY0().intValue()
				&& keyCoordinates.getY1().intValue() >= valueCoordinates.getY1().intValue()) {
			// it is in line with key
			if (keyCoordinates.getX0().intValue() >= valueCoordinates.getX1().intValue()) {
				// it is in left
				setValues(kvExtractionField, LocationType.LEFT, keyCoordinates, valueCoordinates);
			} else if (keyCoordinates.getX1().intValue() <= valueCoordinates.getX0().intValue()) {
				// it is in right
				setValues(kvExtractionField, LocationType.RIGHT, keyCoordinates, valueCoordinates);
			} else if (keyCoordinates.getX0().intValue() >= valueCoordinates.getX0().intValue()
					&& keyCoordinates.getX1().intValue() >= valueCoordinates.getX1().intValue()) {
				// it is in left
				setValues(kvExtractionField, LocationType.LEFT, keyCoordinates, valueCoordinates);
			} else if (keyCoordinates.getX0().intValue() <= valueCoordinates.getX0().intValue()
					&& keyCoordinates.getX1().intValue() <= valueCoordinates.getX1().intValue()) {
				// it is right
				setValues(kvExtractionField, LocationType.RIGHT, keyCoordinates, valueCoordinates);
			} else {
				// it spans all three quadrants
				double x0diff = valueCoordinates.getX0().intValue() - keyCoordinates.getX0().intValue();
				double x1diff = valueCoordinates.getX1().intValue() - keyCoordinates.getX1().intValue();
				if (x0diff < x1diff) {
					setValues(kvExtractionField, LocationType.TOP_RIGHT, keyCoordinates, valueCoordinates);
				} else {
					setValues(kvExtractionField, LocationType.TOP_LEFT, keyCoordinates, valueCoordinates);
				}
			}
		} else {
			// it spans more than two quadrants
			double y0diff = valueCoordinates.getY0().intValue() - keyCoordinates.getY0().intValue();
			double y1diff = valueCoordinates.getY1().intValue() - keyCoordinates.getY1().intValue();
			if (y0diff < y1diff) {
				// it is in bottom
				double x0diff = valueCoordinates.getX0().intValue() - keyCoordinates.getX0().intValue();
				double x1diff = valueCoordinates.getX1().intValue() - keyCoordinates.getX1().intValue();
				if (x0diff < x1diff) {
					setValues(kvExtractionField, LocationType.BOTTOM_RIGHT, keyCoordinates, valueCoordinates);
				} else {
					setValues(kvExtractionField, LocationType.BOTTOM_LEFT, keyCoordinates, valueCoordinates);
				}
			} else {
				// it is in top
				double x0diff = valueCoordinates.getX0().intValue() - keyCoordinates.getX0().intValue();
				double x1diff = valueCoordinates.getX1().intValue() - keyCoordinates.getX1().intValue();
				if (x0diff < x1diff) {
					setValues(kvExtractionField, LocationType.TOP_RIGHT, keyCoordinates, valueCoordinates);
				} else {
					setValues(kvExtractionField, LocationType.TOP_LEFT, keyCoordinates, valueCoordinates);
				}
			}
		}
	}

}
