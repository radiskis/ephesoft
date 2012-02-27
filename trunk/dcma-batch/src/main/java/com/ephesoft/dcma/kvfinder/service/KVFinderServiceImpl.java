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

package com.ephesoft.dcma.kvfinder.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.common.LocationType;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.kvfinder.KVFinderConstants;
import com.ephesoft.dcma.kvfinder.LineDataCarrier;
import com.ephesoft.dcma.kvfinder.LocationFinder;
import com.ephesoft.dcma.kvfinder.data.CustomList;
import com.ephesoft.dcma.kvfinder.data.InputDataCarrier;
import com.ephesoft.dcma.kvfinder.data.OutputDataCarrier;

public class KVFinderServiceImpl implements KVFinderService {

	/**
	 * LOGGER to print the logging information.
	 */
	private Logger logger = LoggerFactory.getLogger(KVFinderServiceImpl.class);

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
	 * Confidence score.
	 * 
	 * @param confidenceScore the confidenceScore to set
	 */
	public final void setConfidenceScore(final String confidenceScore) {
		this.confidenceScore = confidenceScore;
	}

	public final List<OutputDataCarrier> findKeyValue(final List<InputDataCarrier> inputDataCarrierList, final HocrPage hocrPage,
			final int maxResults) throws DCMAException {

		logger.info("Key value search for hocr page.");

		if (inputDataCarrierList == null || null == hocrPage) {
			logger.error("Invalid InputDataCarrier. inputDataCarrier is null.");
			throw new DCMAException("Invalid inputDataCarrier.");
		}

		final LocationFinder locationFinder = new LocationFinder();

		locationFinder.setConfidenceScore(getConfidenceScore());
		locationFinder.setWidthOfLine(getWidthOfLine());

		final CustomList outputDataCarrierList = new CustomList(maxResults);
		for (InputDataCarrier inputDataCarrier : inputDataCarrierList) {
			String keyPattern = inputDataCarrier.getKeyPattern();
			final Spans spans = hocrPage.getSpans();
			final String pageID = hocrPage.getPageID();
			if (null != spans) {

				final List<LineDataCarrier> lineDataCarrierList = createLineDataCarrier(spans, pageID);

				if (logger.isInfoEnabled()) {
					for (LineDataCarrier lineDataCarrier : lineDataCarrierList) {
						final String lineRowData = lineDataCarrier.getLineRowData();
						logger.info(lineRowData);
					}
				}

				for (int currentLineIndex = 0; currentLineIndex < lineDataCarrierList.size(); currentLineIndex++) {
					LineDataCarrier lineDataCarrier = lineDataCarrierList.get(currentLineIndex);
					final String lineRowData = lineDataCarrier.getLineRowData();
					try {
						final String pattern = keyPattern;
						if (null == pattern) {
							continue;
						}

						final List<OutputDataCarrier> dataCarrierList = locationFinder.findPattern(lineRowData, pattern);
						// TODO check for all possible return value and get the span
						// from the original list and then get the xox1 values for span
						// write method to get the span coordinates for this.
						if (null != dataCarrierList) {
							for (OutputDataCarrier dataCarrier : dataCarrierList) {
								String foundValue = dataCarrier.getValue();
								if (null == foundValue || foundValue.isEmpty()) {
									continue;
								}

								String[] foundValArr = foundValue.split(KVFinderConstants.SPACE);
								List<Coordinates> keyCoordinates = new ArrayList<Coordinates>();
								if (null != foundValArr && foundValArr.length >= 0) {
									String firstKeyValue = foundValArr[0];
									List<Integer> indexList = lineDataCarrier.getIndexOfSpan(firstKeyValue);
									if (null != indexList) {
										List<Span> spanList = lineDataCarrier.getSpanList();
										for (Integer integer : indexList) {
											List<Coordinates> local = new ArrayList<Coordinates>();
											for (int p = 0; p < foundValArr.length; p++) {
												if (integer < spanList.size()) {
													Span span = spanList.get(integer);
													if (null == span) {
														continue;
													}
													String value = span.getValue();
													if (null == value) {
														continue;
													}
													integer++;
													String fdVal = foundValArr[p];
													if (value.contains(fdVal)) {
														local.add(span.getCoordinates());
													}
												}
											}
											if (local.size() == foundValArr.length) {
												keyCoordinates.addAll(local);
											}
										}
									}
								}

								Coordinates keyCoordinate = new Coordinates();
								BigInteger minX0 = BigInteger.ZERO;
								BigInteger minY0 = BigInteger.ZERO;
								BigInteger maxX1 = BigInteger.ZERO;
								BigInteger maxY1 = BigInteger.ZERO;
								for (Coordinates coordinates1 : keyCoordinates) {

									boolean isFirst = true;

									BigInteger hocrX0 = coordinates1.getX0();
									BigInteger hocrY0 = coordinates1.getY0();
									BigInteger hocrX1 = coordinates1.getX1();
									BigInteger hocrY1 = coordinates1.getY1();
									if (isFirst) {
										minX0 = hocrX0;
										minY0 = hocrY0;
										maxX1 = hocrX1;
										maxY1 = hocrY1;
										isFirst = false;
									} else {
										if (hocrX0.compareTo(minX0) < 0) {
											minX0 = hocrX0;
										}
										if (hocrY0.compareTo(minY0) < 0) {
											minY0 = hocrY0;
										}
										if (hocrX1.compareTo(maxX1) > 0) {
											maxX1 = hocrX1;
										}
										if (hocrY1.compareTo(maxY1) > 0) {
											maxY1 = hocrY1;
										}
									}
								}

								keyCoordinate.setX0(minX0);
								keyCoordinate.setX1(maxX1);
								keyCoordinate.setY0(minY0);
								keyCoordinate.setY1(maxY1);
								final CustomList outputDataCarrierListInner = new CustomList(maxResults);
								valueExtraction(inputDataCarrier, outputDataCarrierListInner, lineDataCarrierList, currentLineIndex,
										locationFinder, keyCoordinate);
								for (OutputDataCarrier outputDataCarrier : outputDataCarrierListInner) {
									outputDataCarrierList.add(outputDataCarrier);
								}
							}
						}
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		}

		return outputDataCarrierList.getList();
	}

	/**
	 * Create the line data carrier.
	 * 
	 * @param spans {@link Spans}
	 * @param pageID {@link String}
	 * @return List<LineDataCarrier>
	 */
	private List<LineDataCarrier> createLineDataCarrier(final Spans spans, final String pageID) {

		final List<Span> mainSpanList = spans.getSpan();
		List<LineDataCarrier> lineDataCarrierList = new ArrayList<LineDataCarrier>();

		if (null == mainSpanList) {
			return lineDataCarrierList;
		}

		int defaultValue = 20;
		try {
			defaultValue = Integer.parseInt(getWidthOfLine());
		} catch (NumberFormatException nfe) {
			logger.error(nfe.getMessage(), nfe);
			defaultValue = 20;
		}

		final int compareValue = defaultValue;
		// TODO optimize the set creation for document level fields.
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

		return lineDataCarrierList;
	}

	/**
	 * Value based extraction.
	 * 
	 * @param inputDataCarrier {@link InputDataCarrier}
	 * @param outputDataCarrierList CustomList
	 * @param lineDataCarrierList List<Span>
	 * @param currentLineIndex int
	 * @param locationFinder {@link LocationFinder}
	 * @throws DCMAApplicationException
	 */
	private void valueExtraction(final InputDataCarrier inputDataCarrier, final CustomList outputDataCarrierList,
			final List<LineDataCarrier> lineDataCarrierList, final int currentLineIndex, final LocationFinder locationFinder,
			final Coordinates keyCoordinate) throws DCMAApplicationException {

		final LocationType locationType = inputDataCarrier.getLocationType();

		if (null == locationType) {
			return;
		}

		switch (locationType) {

			case TOP:
				if (isZonalKVExtraction(inputDataCarrier)) {
					locationFinder.topLocationRectangle(inputDataCarrier, outputDataCarrierList, lineDataCarrierList,
							currentLineIndex, keyCoordinate);
				} else {
					locationFinder.topLocation(inputDataCarrier, outputDataCarrierList, lineDataCarrierList, currentLineIndex,
							keyCoordinate);
				}
				break;

			case TOP_LEFT:
				if (isZonalKVExtraction(inputDataCarrier)) {
					locationFinder.topLeftLocationRectangle(inputDataCarrier, outputDataCarrierList, lineDataCarrierList,
							currentLineIndex, keyCoordinate);
				} else {
					locationFinder.topLeftLocation(inputDataCarrier, outputDataCarrierList, lineDataCarrierList, currentLineIndex,
							keyCoordinate);
				}

				break;

			case TOP_RIGHT:
				if (isZonalKVExtraction(inputDataCarrier)) {
					locationFinder.topRightLocationRectangle(inputDataCarrier, outputDataCarrierList, lineDataCarrierList,
							currentLineIndex, keyCoordinate);
				} else {
					locationFinder.topRightLocation(inputDataCarrier, outputDataCarrierList, lineDataCarrierList, currentLineIndex,
							keyCoordinate);
				}
				break;

			case RIGHT:
				if (isZonalKVExtraction(inputDataCarrier)) {
					locationFinder.rightLocationRectangle(inputDataCarrier, outputDataCarrierList, lineDataCarrierList,
							currentLineIndex, keyCoordinate);
				} else {
					locationFinder.rightLocation(inputDataCarrier, outputDataCarrierList, lineDataCarrierList, currentLineIndex,
							keyCoordinate);
				}
				break;

			case LEFT:
				if (isZonalKVExtraction(inputDataCarrier)) {
					locationFinder.leftLocationRectangle(inputDataCarrier, outputDataCarrierList, lineDataCarrierList,
							currentLineIndex, keyCoordinate);
				} else {
					locationFinder.leftLocation(inputDataCarrier, outputDataCarrierList, lineDataCarrierList, currentLineIndex,
							keyCoordinate);
				}
				break;

			case BOTTOM:
				if (isZonalKVExtraction(inputDataCarrier)) {
					locationFinder.bottomLocationRectangle(inputDataCarrier, outputDataCarrierList, lineDataCarrierList,
							currentLineIndex, keyCoordinate);
				} else {
					locationFinder.bottomLocation(inputDataCarrier, outputDataCarrierList, lineDataCarrierList, currentLineIndex,
							keyCoordinate);
				}

				break;

			case BOTTOM_LEFT:
				if (isZonalKVExtraction(inputDataCarrier)) {
					locationFinder.bottomLeftLocationRectangle(inputDataCarrier, outputDataCarrierList, lineDataCarrierList,
							currentLineIndex, keyCoordinate);
				} else {
					locationFinder.bottomLeftLocation(inputDataCarrier, outputDataCarrierList, lineDataCarrierList, currentLineIndex,
							keyCoordinate);
				}
				break;

			case BOTTOM_RIGHT:
				if (isZonalKVExtraction(inputDataCarrier)) {
					locationFinder.bottomRightLocationRectangle(inputDataCarrier, outputDataCarrierList, lineDataCarrierList,
							currentLineIndex, keyCoordinate);
				} else {
					locationFinder.bottomRightLocation(inputDataCarrier, outputDataCarrierList, lineDataCarrierList, currentLineIndex,
							keyCoordinate);
				}

				break;

			default:
				logger.info("***********  Default case found. In valid case.");
				break;
		}

	}

	private boolean isZonalKVExtraction(InputDataCarrier inputDataCarrier) {
		if (inputDataCarrier.getLength() != null && inputDataCarrier.getWidth() != null
				&& (inputDataCarrier.getLength() > 0 || inputDataCarrier.getWidth() > 0)) {
			return true;
		} else {
			return false;
		}
	}

}
