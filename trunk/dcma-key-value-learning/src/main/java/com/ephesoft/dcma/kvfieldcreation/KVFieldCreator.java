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

package com.ephesoft.dcma.kvfieldcreation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.ephesoft.dcma.common.HocrUtil;
import com.ephesoft.dcma.common.LineDataCarrier;
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
import com.ephesoft.dcma.kvfinder.service.KVFinderService;

/**
 * This class creates the key value field in batch class for every document level field of each document in the batch.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.kvfieldcreation.service.KVFieldCreatorService
 */
@Component
public class KVFieldCreator implements ICommonConstants {

	/**
	 * An instance of Logger for proper logging in this file using slf4j.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(KVFieldCreator.class);
	/**
	 * Location order to be followed.
	 */
	private String locationOrder;
	/**
	 * Maximum number of records per dlf.
	 */
	private String maxNumberRecordPerDlf;
	/**
	 * Tolerance threshold.
	 */
	private String toleranceThreshold;
	/**
	 * Array of location of type String.
	 */
	private String[] locationArr;
	/**
	 * Maximum number of records per dlf in integer.
	 */
	private int maxNumberRecordPerDlfInt;
	/**
	 * Tolerance threshold in integer.
	 */
	private int toleranceThresholdInt;
	/**
	 * To store the value of multiplier.
	 */
	private String multiplier;
	/**
	 * The fetch value.
	 */
	private String fetchValue;
	/**
	 * Minimum key character count.
	 */
	private String minKeyCharCount;
	/**
	 * The gap between the keys.
	 */
	private int gapBetweenKeys;
	
	/**
	 * getter for gapBetweenKeys.
	 * @return int
	 */
	public int getGapBetweenKeys() {
		return gapBetweenKeys;
	}
	/**
	 * setter for gapBetweenKeys.
	 * @param gapBetweenKeys int
	 */
	public void setGapBetweenKeys(int gapBetweenKeys) {
		this.gapBetweenKeys = gapBetweenKeys;
	}
	/**
	 * getter for minKeyCharCount.
	 * @return {@link String}
	 */
	public String getMinKeyCharCount() {
		return minKeyCharCount;
	}
	/**
	 * setter for minKeyCharCount.
	 * @param minKeyCharCount {@link String}
	 */
	public void setMinKeyCharCount(String minKeyCharCount) {
		this.minKeyCharCount = minKeyCharCount;
	}
	/**
	 * getter for fetchValue.
	 * @return {@link String}
	 */
	public String getFetchValue() {
		return fetchValue;
	}
	/**
	 * setter for fetchValue.
	 * @param fetchValue {@link String}
	 */
	public void setFetchValue(final String fetchValue) {
		this.fetchValue = fetchValue;
	}
	/**
	 * getter for multiplier.
	 * @return {@link String}
	 */
	public String getMultiplier() {
		return multiplier;
	}
	/**
	 * setter for multiplier.
	 * @param multiplier {@link String}
	 */
	public void setMultiplier(final String multiplier) {
		this.multiplier = multiplier;
	}
	/**
	 * setter for maxNumberRecordPerDlfInt.
	 * @param maxNumberRecordPerDlfInt {@link Integer}
	 */
	public void setMaxNumberRecordPerDlfInt(final Integer maxNumberRecordPerDlfInt) {
		this.maxNumberRecordPerDlfInt = maxNumberRecordPerDlfInt;
	}
	/**
	 * setter for toleranceThresholdInt.
	 * @param toleranceThresholdInt {@link String}
	 */
	public void setToleranceThresholdInt(final Integer toleranceThresholdInt) {
		this.toleranceThresholdInt = toleranceThresholdInt;
	}
	/**
	 * setter for locationOrder.
	 * @param locationOrder {@link String}
	 */
	final public void setLocationOrder(final String locationOrder) {
		this.locationOrder = locationOrder;
		if (locationOrder == null || locationOrder.isEmpty()) {
			LOGGER.info("locationOrder is not defined in the specified property file ....");
			throw new DCMABusinessException("locationOrder is not definedin the specified property file ....");
		}
		this.locationArr = locationOrder.split(KVFieldCreatorConstants.SEMI_COLON);
	}
	/**
	 * getter for maxNumberRecordPerDlf.
	 * @return {@link String}
	 */
	final public String getMaxNumberRecordPerDlf() {
		return maxNumberRecordPerDlf;
	}
	/**
	 * This method is used to set the maximum number of records per dlf.
	 * @param maxNumberRecordPerDlf {@link String}
	 */
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
	
	/**
	 * Getter for toleranceThreshold.
	 * @return {@link String}
	 */
	final public String getToleranceThreshold() {
		return toleranceThreshold;
	}
	/**
	 * This method is used to set the tolerance threshold value.
	 * @param toleranceThreshold {@link String}
	 */
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

	/**
	 * A list to store the patterns or strings mentioned in key regex list.
	 */
	private List<String> keyRegexList = null;

	/**
	 * A list to store the patterns or strings mentioned in value regex list.
	 */
	private List<String> valueRegexList = null;

	/**
	 * A list to store the patterns or strings mentioned in exclusion list.
	 */
	private List<String> exclusionRegexList = null;

	/**
	 * An instance of {@link BatchSchemaService}.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * An instance of {@link BatchClassService}.
	 */
	@Autowired
	private BatchClassService batchClassService;

	/**
	 * An instance of {@link KVFinderService}.
	 */
	@Autowired
	private KVFinderService kvFinderService;

	/**
	 * An instance of {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * An instance of {@link KVExtractionService}.
	 */
	@Autowired
	private KVExtractionService kvExtractionService;

	/**
	 * Instance of {@link PluginPropertiesService}.
	 **/
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * This method creates key value field for each document level field of every document.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @param pluginWorkflow {@link String}
	 * @throws DCMAApplicationException
	 */
	public void createKeyValueFields(String batchInstanceIdentifier, String pluginWorkflow) throws DCMAApplicationException {
		String kvFieldCreatorSwitch = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, KVFieldCreatorConstants.KV_FIELD_LEARNING_PLUGIN,
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
			// get the exclusion list.
			if (null == exclusionRegexList) {
				exclusionRegexList = new ArrayList<String>();
				readRegexPropertyFile(KVFieldCreatorConstants.EXCLUSION_REGEX_FILE_PATH, exclusionRegexList);
			}
			Float multiplierFloat = getMultiplierFloat();
			KVFetchValue kvFetchValue = getKVFetchValue();
			int minKeyCharsInt = getminKeyCharsInt();
			Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);
			BatchClass batchClass = batchClassService.getLoadedBatchClassByIdentifier(batchInstanceService
					.getBatchClassIdentifier(batchInstanceIdentifier));

			List<Document> documentList = batch.getDocuments().getDocument();
			Map<String, List<LineDataCarrier>> pageIdToLineDataCarrier = new HashMap<String, List<LineDataCarrier>>();
			boolean isSuccess = false;
			int widthOfLine = getWidthOfLineInt();
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
					List<LineDataCarrier> lineDataCarrierList = null;
					if (pageID == null || docLevelFieldValue == null || docLevelFieldValue.trim().isEmpty()) {
						continue;
					}
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
							lineDataCarrierList = HocrUtil.getLineDataCarrierList(spans, hocrPage.getPageID(), widthOfLine);
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
			batchClassService.merge(batchClass);
		}
	}

	private int getWidthOfLineInt() {
		int widthOfLineInt = KVFieldCreatorConstants.DEFAULT_WIDTH_OF_LINE;
		try {
			widthOfLineInt = Integer.parseInt(kvFinderService.getWidthOfLine());
		} catch (NumberFormatException nfe) {
			LOGGER.error("Error in reading line width from properties file, setting iit to 20.", nfe);
		}
		return widthOfLineInt;
	}

	private List<KVExtraction> createKeyValuePatternList(KVFetchValue kvFetchValue, Float multiplierFloat, final String value,
			final List<LineDataCarrier> lineDataCarrierList, final int minKeyCharsInt) throws DCMAApplicationException {
		List<KVExtraction> kvExtractionList = new ArrayList<KVExtraction>();
		boolean valueFound = false;
		boolean keyFound = false;
		if (null != lineDataCarrierList) {
			for (LineDataCarrier lineDataCarrier : lineDataCarrierList) {
				if (null != lineDataCarrier) {
					LOGGER.info("Searching for value: " + value + " in row == " + lineDataCarrier.getLineRowData());
					List<Span> spanList = lineDataCarrier.getSpanList();
					if (null == spanList) {
						continue;
					}
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
		return kvExtractionList;
	}

	/**
	 * This api is used to create the key value fields.
	 * @param value {@link String}
	 * @param hocrPage {@link com.ephesoft.dcma.batch.schema.HocrPages.HocrPage}
	 * @return {@link List<KVExtraction>}
	 * @throws DCMAApplicationException
	 */
	public List<KVExtraction> createKeyValueFieldsAPI(final String value, final HocrPage hocrPage) throws DCMAApplicationException {
		if (null == keyRegexList) {
			keyRegexList = new ArrayList<String>();
			readRegexPropertyFile(KVFieldCreatorConstants.KEY_REGEX_FILE_PATH, keyRegexList);
		}
		if (null == valueRegexList) {
			valueRegexList = new ArrayList<String>();
			readRegexPropertyFile(KVFieldCreatorConstants.VALUE_REGEX_FILE_PATH, valueRegexList);
		}
		int widthOfLine = KVFieldCreatorConstants.DEFAULT_WIDTH_OF_LINE;
		try {
			widthOfLine = Integer.parseInt(kvFinderService.getWidthOfLine());
		} catch (NumberFormatException nfe) {
			LOGGER.error("Error in reading line width from properties file, setting iit to 20.", nfe);
		}
		Float multiplierFloat = getMultiplierFloat();
		KVFetchValue kvFetchValue = getKVFetchValue();
		int minKeyCharsInt = getminKeyCharsInt();

		List<KVExtraction> kvExtractionList = new ArrayList<KVExtraction>();
		List<LineDataCarrier> lineDataCarrierList = HocrUtil.getLineDataCarrierList(hocrPage.getSpans(), hocrPage.getPageID(),
				widthOfLine);
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
							if (kvExtractionList.size() < this.maxNumberRecordPerDlfInt) {
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

	private boolean isDuplicateKVField(final FieldType fieldType, final KVExtraction kvExtractionField) {
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

	private boolean createKeyValuePattern(final KVExtraction kvExtractionField, final List<LineDataCarrier> lineDataCarrierList,
			final DocField docLevelField, final int minKeyCharsInt) throws DCMAApplicationException {
		CoordinatesList coordinates = docLevelField.getCoordinatesList();
		boolean valueFound = false;
		boolean keyFound = false;
		if (coordinates != null) {
			List<Coordinates> coordinatesList = coordinates.getCoordinates();
			Coordinates recCoordinates = HocrUtil.getRectangleCoordinates(coordinatesList);
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

	private boolean searchKey(final KVExtraction kvExtractionField, final Coordinates recCoordinates, final Span span,
			LineDataCarrier lineDataCarrier, final List<LineDataCarrier> lineDataCarrierList, int minKeyCharsInt)
			throws DCMAApplicationException {
		boolean keyFound = false;
		try {
			Coordinates keyCoordinates = new Coordinates();
			if (locationOrder == null || locationOrder.isEmpty()) {
				LOGGER.error("locationOrder is not defined in the specified property file ....");
				throw new DCMAApplicationException("locationOrder is not defined in the specified property file ....");
			}

			for (String location : this.locationArr) {
				LocationType locationType = LocationType.valueOf(location);
				LOGGER.info("Searching key at location : " + locationType);
				switch (locationType) {
					case TOP:
					case TOP_LEFT:
					case TOP_RIGHT:
						keyFound = createKeyTop(kvExtractionField, recCoordinates, lineDataCarrier, lineDataCarrierList, locationType,
								keyCoordinates, minKeyCharsInt);
						break;

					case RIGHT:
						keyFound = createKeyRight(kvExtractionField, recCoordinates, span, lineDataCarrier, locationType,
								keyCoordinates, minKeyCharsInt);
						break;

					case LEFT:
						keyFound = createKeyLeft(kvExtractionField, recCoordinates, span, lineDataCarrier, locationType,
								keyCoordinates, minKeyCharsInt);
						break;

					case BOTTOM:
					case BOTTOM_LEFT:
					case BOTTOM_RIGHT:
						keyFound = createKeyBottom(kvExtractionField, recCoordinates, lineDataCarrier, lineDataCarrierList,
								locationType, keyCoordinates, minKeyCharsInt);
						break;

					default:
						LOGGER.info("***********  Default case found. In valid case.*************");
						break;
				}
				LOGGER.info("Key found = " + keyFound);
				if (keyFound) {
					setValues(kvExtractionField, keyCoordinates, recCoordinates);
					break;
				}
			}
		} catch (IllegalArgumentException illArgExcep) {
			LOGGER.error("Invalid location specified in the properties file....", illArgExcep);
			throw new DCMAApplicationException("Invalid location specified in the properties file....", illArgExcep);
		}
		return keyFound;
	}

	private void setValues(final KVExtraction kvExtractionField, final Coordinates keyCoordinates, final Coordinates valueCoordinates) {
		if (isValidCoordinate(keyCoordinates) && isValidCoordinate(valueCoordinates)) {
			long keyX1 = keyCoordinates.getX1().longValue();
			long keyY1 = keyCoordinates.getY1().longValue();

			long valueX0 = valueCoordinates.getX0().longValue();
			long valueY0 = valueCoordinates.getY0().longValue();
			long valueX1 = valueCoordinates.getX1().longValue();
			long valueY1 = valueCoordinates.getY1().longValue();

			int xOffsetInInt = (int) Math.round(valueX0 - keyX1);
			int yOffsetInInt = (int) Math.round(valueY0 - keyY1);
			LOGGER.debug("X offset : " + xOffsetInInt);
			LOGGER.debug("Y offset : " + yOffsetInInt);

			int lengthOfBoxInInt = (int) Math.round(valueX1 - valueX0);
			int widthOfBoxInInt = (int) Math.round(valueY1 - valueY0);
			lengthOfBoxInInt = lengthOfBoxInInt + (this.toleranceThresholdInt * lengthOfBoxInInt) / KVFieldCreatorConstants.CONSTANT_VALUE_100;
			widthOfBoxInInt = widthOfBoxInInt + (this.toleranceThresholdInt * widthOfBoxInInt) / KVFieldCreatorConstants.CONSTANT_VALUE_100;
			LOGGER.debug("value zone length : " + lengthOfBoxInInt);
			LOGGER.debug("value zone width : " + widthOfBoxInInt);

			kvExtractionField.setXoffset(xOffsetInInt);
			kvExtractionField.setYoffset(yOffsetInInt);
			kvExtractionField.setLength(lengthOfBoxInInt);
			kvExtractionField.setWidth(widthOfBoxInInt);
			// kvExtractionField.setLocationType(locationType);
		}
	}

	private boolean createKeyTop(final KVExtraction kvExtractionField, final Coordinates recCoordinates,
			final LineDataCarrier lineDataCarrier, final List<LineDataCarrier> lineDataCarrierList, final LocationType location,
			final Coordinates keyCoordinates, int minKeyCharsInt) {
		boolean keyFound = false;
		LOGGER.info(KVFieldCreatorConstants.MSG_KEY_CREATION + location.toString());
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

	private boolean createKeyBottom(final KVExtraction kvExtractionField, final Coordinates recCoordinates,
			final LineDataCarrier lineDataCarrier, final List<LineDataCarrier> lineDataCarrierList, final LocationType location,
			final Coordinates keyCoordinates, final int minKeyCharsInt) {
		boolean keyFound = false;
		LOGGER.info(KVFieldCreatorConstants.MSG_KEY_CREATION + location.toString());
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

	private boolean createKeyLeft(KVExtraction kvExtractionField, Coordinates recCoordinates, Span span,
			LineDataCarrier lineDataCarrier, LocationType location, Coordinates keyCoordinates, int minKeyCharsInt) {
		LOGGER.info(KVFieldCreatorConstants.MSG_KEY_CREATION + location.toString());
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
						break;
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
			if (keyFound) {
				keyFound = setKeyPattern(kvExtractionField, keyCoordinates, minKeyCharsInt, keyString, keyCoordinatesList);
			}
		}
		return keyFound;
	}

	private boolean checkExclusionList(String keyString) {
		boolean isValidkey = true;
		if (!exclusionRegexList.isEmpty()) {
			for (String exclusionString : exclusionRegexList) {
				if (keyString.matches(exclusionString)) {
					isValidkey = false;
					break;
				}
			}
		}
		return isValidkey;
	}

	private boolean createKeyRight(KVExtraction kvExtractionField, Coordinates recCoordinates, Span span,
			LineDataCarrier lineDataCarrier, LocationType location, Coordinates keyCoordinates, int minKeyCharsInt) {
		LOGGER.info(KVFieldCreatorConstants.MSG_KEY_CREATION + location.toString());
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
						keyString.append(KVFieldCreatorConstants.SPACE);
						keyString.append(key.trim());
						prevKeyX1 = rightSpan.getCoordinates().getX1().intValue();
					} else {
						break;
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
			if (keyFound) {
				keyFound = setKeyPattern(kvExtractionField, keyCoordinates, minKeyCharsInt, keyString, keyCoordinatesList);
			}
		}
		return keyFound;
	}

	private boolean setKeyPattern(KVExtraction kvExtractionField, Coordinates keyCoordinates, int minKeyCharsInt,
			StringBuffer keyString, List<Coordinates> keyCoordinatesList) {
		boolean keyFound;
		final String finalKeyString = keyString.toString().trim();

		// check if the found key is present in exclusion list or not.
		if (finalKeyString.length() >= minKeyCharsInt && checkExclusionList(finalKeyString)) {
			LOGGER.info("Key found: " + finalKeyString);
			final String keyPattern = getRegexPattern(finalKeyString, keyRegexList);

			kvExtractionField.setKeyPattern(keyPattern);
			setKeyCoordinates(keyCoordinates, keyCoordinatesList);
			LOGGER.info("Key Pattern Created : " + keyPattern);
			keyFound = true;
		} else {
			LOGGER.info("Required no of characters in key= " + minKeyCharsInt + " , Chars found= " + finalKeyString.length());
			keyFound = false;
		}
		return keyFound;
	}

	private boolean isValidKey(Coordinates recCoordinates, Coordinates spanCoordinates, LocationType location) {
		boolean isValidKey = false;
		LOGGER.info(KVFieldCreatorConstants.MSG_KEY_CREATION + location.toString());
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

	private boolean matchCoordinates(final Span span, final Coordinates recCoordinates) {
		boolean isValidCoor = false;
		Coordinates spanCoordinates = span.getCoordinates();
		long spanX0 = spanCoordinates.getX0().longValue();
		long spanY0 = spanCoordinates.getY0().longValue();
		long spanX1 = spanCoordinates.getX1().longValue();
		long spanY1 = spanCoordinates.getY1().longValue();
		long x0Coordinate = recCoordinates.getX0().longValue();
		long y0Coordinate = recCoordinates.getY0().longValue();
		long x1Coordinate = recCoordinates.getX1().longValue();
		long y1Coordinate = recCoordinates.getY1().longValue();
		// Verify whether span lie inside value rectangle.
		if (((spanX1 >= x0Coordinate && spanX1 <= x1Coordinate) || (spanX0 >= x0Coordinate && spanX0 <= x1Coordinate))
				&& ((spanY1 <= y1Coordinate && spanY1 >= y0Coordinate) || (spanY0 <= y1Coordinate && spanY0 >= y0Coordinate))) {
			isValidCoor = true;
		} else if (((x0Coordinate <= spanX0 && x1Coordinate >= spanX0) || (x0Coordinate >= spanX1 && x1Coordinate <= spanX1))
				&& ((y0Coordinate >= spanY0 && y0Coordinate <= spanY1) || (y1Coordinate >= spanY0 && y1Coordinate <= spanY1))
				|| ((y0Coordinate <= spanY0 && y1Coordinate >= spanY0) || (y0Coordinate >= spanY1 && y1Coordinate <= spanY1))
				&& ((x0Coordinate >= spanX0 && x0Coordinate <= spanX1) || (x1Coordinate >= spanX0 && x1Coordinate <= spanX1))) {
			isValidCoor = true;
		} else {
			if (((x0Coordinate > spanX0 && x0Coordinate < spanX1) || (x1Coordinate > spanX0 && x1Coordinate < spanX1))
					&& ((y0Coordinate > spanY0 && y0Coordinate < spanY1) || (y1Coordinate > spanY0 && y1Coordinate < spanY1))) {
				isValidCoor = true;
			}
		}

		return isValidCoor;
	}

	private String getPageId(final Document document, final String pageIdentifier) {
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

	private String getRegexPattern(String inputString, List<String> regexList) {
		String matchedPattern = null;
		int confidenceInt = KVFieldCreatorConstants.CONSTANT_VALUE_100;
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
		Coordinates recCoordinates = HocrUtil.getRectangleCoordinates(keyCoordinatesList);
		if (null != recCoordinates) {
			keyCoordinates.setX0(recCoordinates.getX0());
			keyCoordinates.setY0(recCoordinates.getY0());
			keyCoordinates.setX1(recCoordinates.getX1());
			keyCoordinates.setY1(recCoordinates.getY1());
		}
	}

	private boolean isValidCoordinate(final Coordinates coordinate) {
		boolean validCoordinate = false;
		BigInteger x0Coord = coordinate.getX0();
		BigInteger y0Coord = coordinate.getY0();
		BigInteger x1Coord = coordinate.getX1();
		BigInteger y1Coord = coordinate.getY1();
		LOGGER.debug("X0:\t" + x0Coord);
		LOGGER.debug("Y0:\t" + y0Coord);
		LOGGER.debug("X1:\t" + x1Coord);
		LOGGER.debug("Y1:\t" + y1Coord);
		if (x0Coord != null || y0Coord != null || x1Coord != null || y1Coord != null) {
			validCoordinate = true;
		}
		LOGGER.debug("Is valid coordinates = " + validCoordinate);
		return validCoordinate;
	}

}
