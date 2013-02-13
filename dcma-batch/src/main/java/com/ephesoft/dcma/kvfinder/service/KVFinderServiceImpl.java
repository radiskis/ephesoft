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

package com.ephesoft.dcma.kvfinder.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.common.HocrUtil;
import com.ephesoft.dcma.common.LineDataCarrier;
import com.ephesoft.dcma.common.PatternMatcherUtil;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.common.LocationType;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.kvfinder.KVFinderConstants;
import com.ephesoft.dcma.kvfinder.LocationFinder;
import com.ephesoft.dcma.kvfinder.data.CustomList;
import com.ephesoft.dcma.kvfinder.data.InputDataCarrier;
import com.ephesoft.dcma.kvfinder.data.KeyValueFieldCarrier;
import com.ephesoft.dcma.kvfinder.data.OutputDataCarrier;
import com.ephesoft.dcma.kvfinder.data.ZonalKeyParameters;
import com.ephesoft.dcma.kvfinder.data.KeyValueFieldCarrier.KeyValueProperties;

/**
 * This is a service to search for key value based extraction. Api will first search the key pattern and then search the value pattern
 * on the basis of location.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.kvfinder.service.KVFinderService
 */
public class KVFinderServiceImpl implements KVFinderService {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(KVFinderServiceImpl.class);

	/**
	 * Width of the line.
	 */
	private String widthOfLine;

	/**
	 * Confidence score.
	 */
	private String confidenceScore;

	/**
	 * Width of the line.
	 * 
	 * @return the widthOfLine
	 */
	public final String getWidthOfLine() {
		return widthOfLine;
	}

	/**
	 * Width of the line.
	 * 
	 * @param widthOfLine the widthOfLine to set
	 */
	public final void setWidthOfLine(final String widthOfLine) {
		this.widthOfLine = widthOfLine;
	}

	/**
	 * Confidence score.
	 * 
	 * @return the confidenceScore
	 */
	public final String getConfidenceScore() {
		return confidenceScore;
	}

	/**
	 * Setter for confidence score.
	 * 
	 * @param confidenceScore the confidenceScore to set
	 */
	public final void setConfidenceScore(final String confidenceScore) {
		this.confidenceScore = confidenceScore;
	}

	/**
	 * This api will search all the input key, value and location pattern for the input hocr page and return the output values which
	 * will satisfied for all the above patterns.
	 * 
	 * @param inputDataCarrier {@link List<{@link InputDataCarrier}>}
	 * @param hocrPage {@link HocrPage}
	 * @param fieldTypeKVMap {@link Map<{@link String}, {@link KeyValueFieldCarrier}>}
	 * @param keyValueFieldCarrier {@link KeyValueFieldCarrier}
	 * @param maxResults int
	 * @return List<{@link OutputDataCarrier}>
	 * @throws DCMAException if invalid value is specified
	 */
	public final List<OutputDataCarrier> findKeyValue(final List<InputDataCarrier> inputDataCarrierList, final HocrPage hocrPage,
			final Map<String, KeyValueFieldCarrier> fieldTypeKVMap, final KeyValueFieldCarrier keyValueFieldCarrier,
			final int maxResults) throws DCMAException {

		LOGGER.info("Key value search for hocr page.");

		if (inputDataCarrierList == null || null == hocrPage) {
			LOGGER.error("Invalid InputDataCarrier. inputDataCarrier is null.");
			throw new DCMAException("Invalid inputDataCarrier.");
		}
		int widthOfLineInt = BatchConstants.ZERO;
		final CustomList outputDataCarrierList = new CustomList(maxResults);
		final Spans spans = hocrPage.getSpans();
		if (null != spans) {
			final String pageID = hocrPage.getPageID();
			LOGGER.info("page id: " + pageID);
			final LocationFinder locationFinder = new LocationFinder();
			try {
				widthOfLineInt = Integer.parseInt(widthOfLine);
			} catch (NumberFormatException nfe) {
				LOGGER.error("Invalid value for widthOfLine specified. Setting it to its default value 15.");
			}
			final List<LineDataCarrier> lineDataCarrierList = HocrUtil.getLineDataCarrierList(spans, pageID, widthOfLineInt);

			locationFinder.setConfidenceScore(getConfidenceScore());
			locationFinder.setWidthOfLine(getWidthOfLine());

			for (InputDataCarrier inputDataCarrier : inputDataCarrierList) {
				boolean useExistingKey = inputDataCarrier.isUseExistingField();
				final String keyPattern = inputDataCarrier.getKeyPattern();
				LOGGER.info("Key pattern : " + keyPattern);
				LOGGER.info("Use Existing key : " + useExistingKey);

				if (LOGGER.isInfoEnabled()) {
					for (LineDataCarrier lineDataCarrier : lineDataCarrierList) {
						final String lineRowData = lineDataCarrier.getLineRowData();
						LOGGER.info(lineRowData);
					}
				}
				if (useExistingKey) {
					LOGGER.info("Extracting data using existing field as key. Field name : " + keyPattern);
					extractKVUsingExistingField(inputDataCarrier, locationFinder, outputDataCarrierList, fieldTypeKVMap, keyPattern,
							pageID, lineDataCarrierList, maxResults, keyValueFieldCarrier);
				} else {
					extractKey(keyValueFieldCarrier, maxResults, outputDataCarrierList, pageID, locationFinder, lineDataCarrierList,
							inputDataCarrier, keyPattern);
				}
			}
		}
		return outputDataCarrierList.getList();
	}

	private void extractKey(final KeyValueFieldCarrier keyValueFieldCarrier, final int maxResults,
			final CustomList outputDataCarrierList, final String pageID, final LocationFinder locationFinder,
			final List<LineDataCarrier> lineDataCarrierList, InputDataCarrier inputDataCarrier, final String keyPattern) {
		LOGGER.info("Inside method extractKey...");
		if (null != keyPattern) {
			boolean isZonalKV = isZonalKVExtraction(inputDataCarrier);
			List<ZonalKeyParameters> foundKeyList = new ArrayList<ZonalKeyParameters>();
			for (int currentLineIndex = BatchConstants.ZERO; currentLineIndex < lineDataCarrierList.size(); currentLineIndex++) {
				LineDataCarrier lineDataCarrier = lineDataCarrierList.get(currentLineIndex);
				try {
					final List<OutputDataCarrier> dataCarrierList = PatternMatcherUtil.findPattern(lineDataCarrier, keyPattern,
							getConfidenceScore());
					for (OutputDataCarrier dataCarrier : dataCarrierList) {
						String foundValue = dataCarrier.getValue();
						Span span = dataCarrier.getSpan();
						if (null == foundValue || foundValue.isEmpty() || null == span) {
							continue;
						}

						String[] foundValArr = foundValue.split(KVFinderConstants.SPACE);
						List<Coordinates> coordinatesList = new ArrayList<Coordinates>();
						coordinatesList.add(span.getCoordinates());
						Integer spanIndex = lineDataCarrier.getIndexOfSpan(span);
						Span rightSpan = null;
						if (null != spanIndex && null != foundValArr && foundValArr.length > BatchConstants.ONE) {
							for (int p = BatchConstants.ZERO; p < foundValArr.length - BatchConstants.ONE; p++) {
								rightSpan = lineDataCarrier.getRightSpan(spanIndex);
								if (null != rightSpan) {
									coordinatesList.add(rightSpan.getCoordinates());
								}
								spanIndex = spanIndex + BatchConstants.ONE;
							}
						}
						Coordinates keyCoordinate = HocrUtil.getRectangleCoordinates(coordinatesList);
						if (!isZonalKV) {
							extractValue(keyCoordinate, lineDataCarrierList, inputDataCarrier, keyValueFieldCarrier, maxResults,
									outputDataCarrierList, locationFinder, currentLineIndex, pageID);
						} else {
							ZonalKeyParameters zonalKeyParameters = new ZonalKeyParameters(currentLineIndex, keyCoordinate, keyPattern);
							double distanceFromZone = HocrUtil.calculateDistanceFromZone(keyCoordinate, inputDataCarrier
									.getKeyRectangleCoordinates());
							zonalKeyParameters.setDistanceFromZone(distanceFromZone);
							foundKeyList.add(zonalKeyParameters);
						}
					}
				} catch (Exception e) {
					LOGGER.error("Error while extracting key value data." + e.getMessage(), e);
				}
			}
			if (isZonalKV) {
				Collections.sort(foundKeyList, new CustomComparator());
				for (ZonalKeyParameters zonalKeyParameters : foundKeyList) {
					if (zonalKeyParameters != null) {
						Coordinates keyCoordinates = zonalKeyParameters.getKeyCoordinates();
						int currentLineIndex = zonalKeyParameters.getLineIndex();
					
						try {
							extractValue(keyCoordinates, lineDataCarrierList, inputDataCarrier, keyValueFieldCarrier, maxResults,
									outputDataCarrierList, locationFinder, currentLineIndex, pageID);
						} catch (DCMAApplicationException e) {
							LOGGER.error("Error while extracting key value data." + e.getMessage(), e);
						}
					}
				}
			}
		}
		LOGGER.info("Exiting method extractKey.....");
	}


	private void extractValue(final Coordinates keyCoordinate, final List<LineDataCarrier> lineDataCarrierList,
			final InputDataCarrier inputDataCarrier, final KeyValueFieldCarrier keyValueFieldCarrier, final int maxResults,
			final CustomList outputDataCarrierList, final LocationFinder locationFinder, final int currentLineIndex,
			final String pageID) throws DCMAApplicationException {
		performValueExtraction(maxResults, locationFinder, outputDataCarrierList, inputDataCarrier, lineDataCarrierList,
				currentLineIndex, keyCoordinate);
		List<OutputDataCarrier> outDataList = outputDataCarrierList.getList();
		List<Coordinates> coordinatesList = new ArrayList<Coordinates>();
		for (OutputDataCarrier outDataCarrier : outDataList) {
			coordinatesList.add(outDataCarrier.getSpan().getCoordinates());
		}
		Coordinates recCoord = HocrUtil.getRectangleCoordinates(coordinatesList);
		if (keyValueFieldCarrier != null) {
			KeyValueProperties keyValueProperties = keyValueFieldCarrier.new KeyValueProperties();
			keyValueProperties.setValueCoordinates(recCoord);
			keyValueFieldCarrier.addKeyValueDataToPage(pageID, keyValueProperties);
		}
	}

	private void extractKVUsingExistingField(final InputDataCarrier inputDataCarrier, final LocationFinder locationFinder,
			final CustomList outputDataCarrierList, final Map<String, KeyValueFieldCarrier> fieldTypeKVMap, String keyPattern,
			final String pageID, final List<LineDataCarrier> lineDataCarrierList, final int maxResults,
			final KeyValueFieldCarrier keyValueFieldCarrier) {
		LOGGER.info("Entering method extractKVUsingExistingField .....");
		try {
			if (keyPattern != null && !keyPattern.isEmpty() && fieldTypeKVMap != null && !fieldTypeKVMap.isEmpty()) {
				LOGGER.info("Getting KeyValueFieldCarrier for field type : " + keyPattern);
				KeyValueFieldCarrier keyPatternCarrier = fieldTypeKVMap.get(keyPattern);
				if (keyPatternCarrier != null && keyPatternCarrier.getKeyValuePropertiesForPage(pageID) != null) {
					LOGGER.info("Getting KeyValueProperties list for page id : " + pageID);
					List<KeyValueProperties> keyValuePropertiesList = keyPatternCarrier.getKeyValuePropertiesForPage(pageID);
					for (KeyValueProperties keyValueProperties : keyValuePropertiesList) {
						LOGGER.info("Getting KeyCoordinates for page id : " + pageID);
						Coordinates keyCoordinates = keyValueProperties.getValueCoordinates();
						for (int currentLineIndex = BatchConstants.ZERO; currentLineIndex < lineDataCarrierList.size(); currentLineIndex++) {
							LineDataCarrier lineDataCarrier = lineDataCarrierList.get(currentLineIndex);
							List<Span> spanList = lineDataCarrier.getSpanList();
							for (Span span : spanList) {
								if (span == null || span.getCoordinates() == null) {
									continue;
								}
								Coordinates spanCoord = span.getCoordinates();
								if (spanCoord.getX0().compareTo(keyCoordinates.getX0()) == BatchConstants.ZERO
										&& spanCoord.getX1().compareTo(keyCoordinates.getX1()) == BatchConstants.ZERO) {
									// Value Coordinates for the previous field is now the key coordinate for this field.
									performValueExtraction(maxResults, locationFinder, outputDataCarrierList, inputDataCarrier,
											lineDataCarrierList, currentLineIndex, keyCoordinates);
									List<OutputDataCarrier> outDataList = outputDataCarrierList.getList();
									List<Coordinates> coordinatesList = new ArrayList<Coordinates>();
									for (OutputDataCarrier outDataCarrier : outDataList) {
										coordinatesList.add(outDataCarrier.getSpan().getCoordinates());
									}
									Coordinates recCoord = HocrUtil.getRectangleCoordinates(coordinatesList);
									if (keyValueFieldCarrier != null) {
										KeyValueProperties newKeyValueProperties = keyValueFieldCarrier.new KeyValueProperties();
										newKeyValueProperties.setValueCoordinates(recCoord);
										keyValueFieldCarrier.addKeyValueDataToPage(pageID, newKeyValueProperties);
									}
								}
							}
						}
					}
				}
			}
		} catch (DCMAApplicationException dcmae) {
			LOGGER.error("Error while extracting key value field using existing field. Field : " + dcmae.getMessage(), dcmae);
		}
		LOGGER.info("Exiting method extractKVUsingExistingField .....");
	}

	private CustomList performValueExtraction(final int maxResults, final LocationFinder locationFinder,
			final CustomList outputDataCarrierList, InputDataCarrier inputDataCarrier,
			final List<LineDataCarrier> lineDataCarrierList, int currentLineIndex, Coordinates keyCoordinate)
			throws DCMAApplicationException {
		final CustomList outputDataCarrierListInner = new CustomList(maxResults);
		valueExtraction(inputDataCarrier, outputDataCarrierListInner, lineDataCarrierList, currentLineIndex, locationFinder,
				keyCoordinate);
		for (OutputDataCarrier outputDataCarrier : outputDataCarrierListInner) {
			if (outputDataCarrier != null) {
				outputDataCarrierList.add(outputDataCarrier);
			}
		}
		return outputDataCarrierListInner;
	}

	/**
	 * Value based extraction.
	 * 
	 * @param inputDataCarrier {@link InputDataCarrier}
	 * @param outputDataCarrierList CustomList
	 * @param lineDataCarrierList List
	 * @param currentLineIndex int
	 * @param locationFinder {@link LocationFinder}
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException
	 */
	private void valueExtraction(final InputDataCarrier inputDataCarrier, final CustomList outputDataCarrierList,
			final List<LineDataCarrier> lineDataCarrierList, final int currentLineIndex, final LocationFinder locationFinder,
			final Coordinates keyCoordinate) throws DCMAApplicationException {

		final LocationType locationType = inputDataCarrier.getLocationType();

		if (isZonalKVExtraction(inputDataCarrier)) {
			locationFinder.extractValueFromZone(inputDataCarrier, outputDataCarrierList, lineDataCarrierList, currentLineIndex,
					keyCoordinate);
		} else if (null != locationType) {

			switch (locationType) {

				case TOP:
					locationFinder.topLocation(inputDataCarrier, outputDataCarrierList, lineDataCarrierList, currentLineIndex,
							keyCoordinate);
					break;

				case TOP_LEFT:
					locationFinder.topLeftLocation(inputDataCarrier, outputDataCarrierList, lineDataCarrierList, currentLineIndex,
							keyCoordinate);

					break;

				case TOP_RIGHT:
					locationFinder.topRightLocation(inputDataCarrier, outputDataCarrierList, lineDataCarrierList, currentLineIndex,
							keyCoordinate);
					break;

				case RIGHT:
					locationFinder.rightLocation(inputDataCarrier, outputDataCarrierList, lineDataCarrierList, currentLineIndex,
							keyCoordinate);
					break;

				case LEFT:
					locationFinder.leftLocation(inputDataCarrier, outputDataCarrierList, lineDataCarrierList, currentLineIndex,
							keyCoordinate);
					break;

				case BOTTOM:
					locationFinder.bottomLocation(inputDataCarrier, outputDataCarrierList, lineDataCarrierList, currentLineIndex,
							keyCoordinate);

					break;

				case BOTTOM_LEFT:
					locationFinder.bottomLeftLocation(inputDataCarrier, outputDataCarrierList, lineDataCarrierList, currentLineIndex,
							keyCoordinate);
					break;

				case BOTTOM_RIGHT:
					locationFinder.bottomRightLocation(inputDataCarrier, outputDataCarrierList, lineDataCarrierList, currentLineIndex,
							keyCoordinate);
					break;

				default:
					LOGGER.info("***********  Default case found. In valid case.");
					break;
			}
		}

	}

	private boolean isZonalKVExtraction(InputDataCarrier inputDataCarrier) {
		boolean isZonalKV = false;
		if (inputDataCarrier.getLength() != null && inputDataCarrier.getWidth() != null
				&& (inputDataCarrier.getLength() > BatchConstants.ZERO || inputDataCarrier.getWidth() > BatchConstants.ZERO)) {
			isZonalKV = true;
		}
		return isZonalKV;
	}

	/**
	 * This is comparator class.
	 * @author Ephesoft
	 * @version 1.0
	 */
	class CustomComparator implements Comparator<ZonalKeyParameters> {

		/**
		 * This is override compare method to compare two objects.
		 * @param zonalKeyParameter1 {@link ZonalKeyParameters}
		 * @param zonalKeyParameter2 {@link ZonalKeyParameters}
		 * @return int
		 */
		@Override
		public int compare(final ZonalKeyParameters zonalKeyParameter1, final ZonalKeyParameters zonalKeyParameter2) {
			int compare = BatchConstants.ZERO;
			if (zonalKeyParameter1 != null && zonalKeyParameter2 != null) {
				double distanceFromZone1 = zonalKeyParameter1.getDistanceFromZone();
				double distanceFromZone2 = zonalKeyParameter2.getDistanceFromZone();
				if (distanceFromZone2 > distanceFromZone1) {
					compare = -BatchConstants.ONE;
				} else if (distanceFromZone2 < distanceFromZone1) {
					compare = BatchConstants.ONE;
				}
			}
			return compare;
		}
	}

}
