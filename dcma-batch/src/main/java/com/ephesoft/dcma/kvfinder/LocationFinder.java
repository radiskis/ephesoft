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

package com.ephesoft.dcma.kvfinder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.common.HocrUtil;
import com.ephesoft.dcma.common.LineDataCarrier;
import com.ephesoft.dcma.common.PatternMatcherUtil;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.kvfinder.data.CustomList;
import com.ephesoft.dcma.kvfinder.data.InputDataCarrier;
import com.ephesoft.dcma.kvfinder.data.OutputDataCarrier;

/**
 * This class is find the location of value extraction for a input of key extraction.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.kvextraction.KeyValueExtraction
 * @see com.ephesoft.dcma.kvextraction.util.OutputDataCarrier
 */
public class LocationFinder {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LocationFinder.class);

	/**
	 * Width of line.
	 */
	private String widthOfLine;

	/**
	 * Confidence score.
	 */
	private String confidenceScore;

	/**
	 * Confidence score.
	 */
	private float confidenceScoreFloat = KVFinderConstants.DEFAULT_CONFIDENCE_SCORE;

	/**
	 * @return the widthOfLine
	 */
	public final String getWidthOfLine() {
		return widthOfLine;
	}

	/**
	 * @param widthOfLine the widthOfLine to set
	 */
	public final void setWidthOfLine(final String widthOfLine) {
		this.widthOfLine = widthOfLine;
	}

	/**
	 * @return the confidenceScore
	 */
	public final String getConfidenceScore() {
		return confidenceScore;
	}

	/**
	 * @param confidenceScore the confidenceScore to set
	 */
	public final void setConfidenceScore(final String confidenceScore) {
		this.confidenceScore = confidenceScore;
		float confidenceFloat = KVFinderConstants.DEFAULT_CONFIDENCE_SCORE;
		try {
			confidenceFloat = Float.parseFloat(confidenceScore);
		} catch (NumberFormatException nfe) {
			LOGGER.error("Invalid value for confidence score specified. Setting it to its default value 100.");
		}
		this.confidenceScoreFloat = confidenceFloat;
	}

	/**
	 * Right location finder. Method will search to the right of the key word.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 */
	public final void rightLocation(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {
		boolean isValid = true;
		if (currentLineIndex < BatchConstants.ZERO || currentLineIndex >= lineOutputDataCarrierList.size()) {
			isValid = false;
		}
		LineDataCarrier lineOutputDataCarrier = lineOutputDataCarrierList.get(currentLineIndex);
		if (isValid && null == lineOutputDataCarrier) {
			isValid = false;
		}

		List<Span> spanList = lineOutputDataCarrier.getSpanList();
		if (isValid && null == spanList) {
			isValid = false;
		}
		if (isValid) {
			final String valuePattern = kVExtraction.getValuePattern();
			final List<OutputDataCarrier> afterValueExtractionList = PatternMatcherUtil.findPattern(lineOutputDataCarrier,
					valuePattern, getConfidenceScore());
			final BigInteger keyX1 = keyCoordinate.getX1();
			BigInteger minValue = null;
			BigInteger tempValue = null;
			OutputDataCarrier valueOutputDataCarrier = null;
			for (OutputDataCarrier dataCarrier : afterValueExtractionList) {
				final Span span = dataCarrier.getSpan();
				final Coordinates valueCoordinates = span.getCoordinates();
				final BigInteger valueX0 = valueCoordinates.getX0();
				if (keyX1.longValue() >= valueX0.longValue()) {
					continue;
				}
				tempValue = valueX0.subtract(keyX1);
				if (tempValue.longValue() < BatchConstants.ZERO) {
					tempValue = tempValue.negate();
				}
				if (null == minValue) {
					minValue = tempValue;
					valueOutputDataCarrier = dataCarrier;
				} else if (tempValue.subtract(minValue).longValue() < BatchConstants.ZERO) {
					minValue = tempValue;
					valueOutputDataCarrier = dataCarrier;
				}
			}
			// Append values to the right according to the number of words specified by admin.
			appendValues(kVExtraction, lineOutputDataCarrier, valueOutputDataCarrier);
			valueFoundData.add(valueOutputDataCarrier);
		}
	}

	private void addValues(LineDataCarrier lineDataCarrier, OutputDataCarrier valueOutputDataCarrier, int numberOfWords,
			Coordinates finalValueCoord) {
		StringBuffer finalValue = new StringBuffer(valueOutputDataCarrier.getValue());
		Span valueSpan = valueOutputDataCarrier.getSpan();

		if (null != valueSpan) {
			Integer spanIndex = lineDataCarrier.getIndexOfSpan(valueSpan);
			if (spanIndex != null) {
				Span rightSpan = null;
				for (int index = BatchConstants.ZERO; index < numberOfWords; index++) {
					rightSpan = lineDataCarrier.getRightSpan(spanIndex);
					if (null != rightSpan && rightSpan.getValue() != null) {
						finalValue.append(KVFinderConstants.SPACE);
						finalValue.append(rightSpan.getValue());
						setValueCoordinates(finalValueCoord, rightSpan);
					}
					spanIndex = spanIndex + BatchConstants.ONE;
				}
			}
			valueOutputDataCarrier.setValue(finalValue.toString());
		}
	}

	private void setValueCoordinates(final Coordinates coordinates, final Span span) {
		BigInteger coordX0 = coordinates.getX0();
		BigInteger coordY0 = coordinates.getY0();
		BigInteger coordX1 = coordinates.getX1();
		BigInteger coordY1 = coordinates.getY1();
		Coordinates spanCoordinates = span.getCoordinates();
		if (null != spanCoordinates) {
			BigInteger spanX0 = span.getCoordinates().getX0();
			BigInteger spanY0 = span.getCoordinates().getY0();
			BigInteger spanX1 = span.getCoordinates().getX1();
			BigInteger spanY1 = span.getCoordinates().getY1();
			if (spanX0.compareTo(coordX0) == -BatchConstants.ONE) {
				coordinates.setX0(spanX0);
			}
			if (spanY0.compareTo(coordY0) == -BatchConstants.ONE) {
				coordinates.setY0(spanY0);
			}
			if (spanX1.compareTo(coordX1) == BatchConstants.ONE) {
				coordinates.setX1(spanX1);
			}
			if (spanY1.compareTo(coordY1) == BatchConstants.ONE) {
				coordinates.setY1(spanY1);
			}
		}
	}

	/**
	 * Left location finder. Method will search to the left of the key word.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 */
	public final void leftLocation(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {
		boolean isValid = true;
		if (currentLineIndex < BatchConstants.ZERO || currentLineIndex >= lineOutputDataCarrierList.size()) {
			isValid = false;
		}
		LineDataCarrier lineOutputDataCarrier = lineOutputDataCarrierList.get(currentLineIndex);
		if (isValid && null == lineOutputDataCarrier) {
			isValid = false;
		}
		List<Span> spanList = lineOutputDataCarrier.getSpanList();
		if (isValid && null == spanList) {
			isValid = false;
		}
		if (isValid) {
			final String valuePattern = kVExtraction.getValuePattern();
			final List<OutputDataCarrier> afterValueExtractionList = PatternMatcherUtil.findPattern(lineOutputDataCarrier,
					valuePattern, getConfidenceScore());
			final BigInteger keyX0 = keyCoordinate.getX0();
			BigInteger minValue = null;
			BigInteger tempValue = null;
			OutputDataCarrier valueOutputDataCarrier = null;
			for (OutputDataCarrier dataCarrier : afterValueExtractionList) {
				final Span span = dataCarrier.getSpan();
				final Coordinates valueCoordinates = span.getCoordinates();
				final BigInteger valueX1 = valueCoordinates.getX1();
				if (valueX1.longValue() < keyX0.longValue()) {
					tempValue = keyX0.subtract(valueX1);
					if (tempValue.longValue() < BatchConstants.ZERO) {
						tempValue = tempValue.negate();
					}
					if (null == minValue) {
						minValue = tempValue;
						valueOutputDataCarrier = dataCarrier;
					} else if (tempValue.subtract(minValue).longValue() < BatchConstants.ZERO) {
						minValue = tempValue;
						valueOutputDataCarrier = dataCarrier;
					}
				}
			}
			// Append values to the right according to the number of words specified by admin.
			appendValues(kVExtraction, lineOutputDataCarrier, valueOutputDataCarrier);
			valueFoundData.add(valueOutputDataCarrier);
		}
	}

	/**
	 * Bottom Right location finder. Method will search to the bottom right of the key word.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 */
	public final void bottomRightLocation(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {

		boolean isValid = true;
		if (currentLineIndex < BatchConstants.ZERO || currentLineIndex >= lineOutputDataCarrierList.size()
				|| currentLineIndex + BatchConstants.ONE >= lineOutputDataCarrierList.size()) {
			isValid = false;
		}
		if (isValid) {
			LineDataCarrier topLineDataCarrier = lineOutputDataCarrierList.get(currentLineIndex + BatchConstants.ONE);
			if (isValid && null == topLineDataCarrier) {
				isValid = false;
			}

			List<Span> spanList = topLineDataCarrier.getSpanList();
			if (isValid && null == spanList) {
				isValid = false;
			}
			if (isValid) {
				Coordinates keyCoordinates = keyCoordinate;
				final String valuePattern = kVExtraction.getValuePattern();

				List<OutputDataCarrier> afterValueExtractionList = PatternMatcherUtil.findPattern(topLineDataCarrier, valuePattern,
						getConfidenceScore());

				BigInteger keyX1 = keyCoordinates.getX1();
				BigInteger minValue = null;
				BigInteger tempValue = null;
				OutputDataCarrier valueOutputDataCarrier = null;
				for (OutputDataCarrier dataCarrier : afterValueExtractionList) {
					Span span = dataCarrier.getSpan();
					Coordinates valueCoordinates = span.getCoordinates();
					BigInteger valueX0 = valueCoordinates.getX0();
					BigInteger valueX1 = valueCoordinates.getX1();
					if (valueX1.longValue() > keyX1.longValue()) {
						tempValue = valueX0.subtract(keyX1);
						if (tempValue.longValue() < BatchConstants.ZERO) {
							tempValue = tempValue.negate();
						}
						if (null == minValue) {
							minValue = tempValue;
							valueOutputDataCarrier = dataCarrier;
						} else if (tempValue.subtract(minValue).longValue() < BatchConstants.ZERO) {
							minValue = tempValue;
							valueOutputDataCarrier = dataCarrier;
						}
					}
				}
				// Append values to the right according to the number of words specified by admin.
				appendValues(kVExtraction, topLineDataCarrier, valueOutputDataCarrier);
				valueFoundData.add(valueOutputDataCarrier);
			}
		}
	}

	/**
	 * Bottom left location finder. Method will search to the bottom left of the key word.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 */
	public final void bottomLeftLocation(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {
		boolean isValid = true;
		if (currentLineIndex < BatchConstants.ZERO || currentLineIndex >= lineOutputDataCarrierList.size()
				|| currentLineIndex + BatchConstants.ONE >= lineOutputDataCarrierList.size()) {
			isValid = false;
		}
		if (isValid) {
			LineDataCarrier topLineDataCarrier = lineOutputDataCarrierList.get(currentLineIndex + BatchConstants.ONE);
			if (isValid && null == topLineDataCarrier) {
				isValid = false;
			}
			List<Span> spanList = topLineDataCarrier.getSpanList();
			if (isValid && null == spanList) {
				isValid = false;
			}
			if (isValid) {
				Coordinates keyCoordinates = keyCoordinate;
				String valuePattern = kVExtraction.getValuePattern();

				final List<OutputDataCarrier> afterValueExtractionList = PatternMatcherUtil.findPattern(topLineDataCarrier,
						valuePattern, getConfidenceScore());

				BigInteger keyX0 = keyCoordinates.getX0();
				BigInteger minValue = null;
				BigInteger tempValue = null;
				OutputDataCarrier valueOutputDataCarrier = null;
				for (OutputDataCarrier dataCarrier : afterValueExtractionList) {
					Span span = dataCarrier.getSpan();
					Coordinates valueCoordinates = span.getCoordinates();
					BigInteger valueX0 = valueCoordinates.getX0();
					if (valueX0.longValue() < keyX0.longValue()) {
						tempValue = valueX0.subtract(keyX0);
						if (tempValue.longValue() < BatchConstants.ZERO) {
							tempValue = tempValue.negate();
						}
						if (null == minValue) {
							minValue = tempValue;
							valueOutputDataCarrier = dataCarrier;
						} else if (tempValue.subtract(minValue).longValue() < BatchConstants.ZERO) {
							minValue = tempValue;
							valueOutputDataCarrier = dataCarrier;
						}
					}
				}
				// Append values to the right according to the number of words specified by admin.
				appendValues(kVExtraction, topLineDataCarrier, valueOutputDataCarrier);
				valueFoundData.add(valueOutputDataCarrier);
			}
		}
	}

	/**
	 * Bottom location finder. Method will search to the bottom of the key word.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 */
	public final void bottomLocation(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {
		boolean isValid = true;
		if (currentLineIndex < BatchConstants.ZERO || currentLineIndex >= lineOutputDataCarrierList.size()
				|| currentLineIndex + BatchConstants.ONE >= lineOutputDataCarrierList.size()) {
			isValid = false;
		}
		if (isValid) {
			LineDataCarrier topLineDataCarrier = lineOutputDataCarrierList.get(currentLineIndex + BatchConstants.ONE);
			if (isValid && null == topLineDataCarrier) {
				isValid = false;
			}
			if (isValid) {
				List<Span> spanList = topLineDataCarrier.getSpanList();
				if (!isValid && null == spanList) {
					isValid = false;
				}
				Coordinates keyCoordinates = keyCoordinate;
				String valuePattern = kVExtraction.getValuePattern();

				final List<OutputDataCarrier> afterValueExtractionList = PatternMatcherUtil.findPattern(topLineDataCarrier,
						valuePattern, getConfidenceScore());

				BigInteger keyX0 = keyCoordinates.getX0();
				BigInteger keyX1 = keyCoordinates.getX1();
				BigInteger minValue = null;
				BigInteger tempValue = null;
				OutputDataCarrier valueOutputDataCarrier = null;

				for (OutputDataCarrier dataCarrier : afterValueExtractionList) {
					Span span = dataCarrier.getSpan();
					Coordinates valueCoordinates = span.getCoordinates();
					BigInteger valueX0 = valueCoordinates.getX0();
					BigInteger valueX1 = valueCoordinates.getX1();
					if ((valueX0.longValue() < keyX1.longValue()) && (valueX1.longValue() >= keyX0.longValue())) {

						tempValue = valueX0.subtract(keyX0);
						if (tempValue.longValue() < BatchConstants.ZERO) {
							tempValue = tempValue.negate();
						}
						if (null == minValue) {
							minValue = tempValue;
							valueOutputDataCarrier = dataCarrier;
						} else if (tempValue.subtract(minValue).longValue() < BatchConstants.ZERO) {
							minValue = tempValue;
							valueOutputDataCarrier = dataCarrier;
						}
					}
				}
				// Append values to the right according to the number of words specified by admin.
				appendValues(kVExtraction, topLineDataCarrier, valueOutputDataCarrier);
				valueFoundData.add(valueOutputDataCarrier);
			}
		}
	}

	/**
	 * Top right location finder. Method will search to the top right of the key word.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 */
	public final void topRightLocation(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {
		boolean isValid = true;
		if (currentLineIndex < BatchConstants.ZERO || currentLineIndex >= lineOutputDataCarrierList.size() || currentLineIndex - BatchConstants.ONE < BatchConstants.ZERO) {
			isValid = false;
		}
		if (isValid) {
			LineDataCarrier topLineDataCarrier = lineOutputDataCarrierList.get(currentLineIndex - BatchConstants.ONE);
			if (null == topLineDataCarrier) {
				isValid = false;
			}
			if (isValid) {
				Coordinates keyCoordinates = keyCoordinate;
				String valuePattern = kVExtraction.getValuePattern();

				final List<OutputDataCarrier> afterValueExtractionList = PatternMatcherUtil.findPattern(topLineDataCarrier,
						valuePattern, getConfidenceScore());

				BigInteger keyX1 = keyCoordinates.getX1();
				BigInteger minValue = null;
				BigInteger tempValue = null;
				OutputDataCarrier valueOutputDataCarrier = null;
				for (OutputDataCarrier dataCarrier : afterValueExtractionList) {
					Span span = dataCarrier.getSpan();
					Coordinates valueCoordinates = span.getCoordinates();
					BigInteger valueX0 = valueCoordinates.getX0();
					BigInteger valueX1 = valueCoordinates.getX1();
					if (valueX1.longValue() <= keyX1.longValue()) {
						tempValue = valueX0.subtract(keyX1);
						if (tempValue.longValue() < BatchConstants.ZERO) {
							tempValue = tempValue.negate();
						}
						if (null == minValue) {
							minValue = tempValue;
							valueOutputDataCarrier = dataCarrier;
						} else if (tempValue.subtract(minValue).longValue() < BatchConstants.ZERO) {
							minValue = tempValue;
							valueOutputDataCarrier = dataCarrier;
						}
					}
				}
				// Append values to the right according to the number of words specified by admin.
				appendValues(kVExtraction, topLineDataCarrier, valueOutputDataCarrier);
				valueFoundData.add(valueOutputDataCarrier);
			}
		}
	}

	/**
	 * Top left location finder. Method will search to the top left of the key word.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 */
	public final void topLeftLocation(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {
		boolean isValid = true;
		if (currentLineIndex < BatchConstants.ZERO || currentLineIndex >= lineOutputDataCarrierList.size() || currentLineIndex - BatchConstants.ONE < BatchConstants.ZERO) {
			isValid = false;
		}
		if (isValid) {
			LineDataCarrier topLineDataCarrier = lineOutputDataCarrierList.get(currentLineIndex - BatchConstants.ONE);
			if (null == topLineDataCarrier) {
				isValid = false;
			}
			if (isValid) {
				Coordinates keyCoordinates = keyCoordinate;
				String valuePattern = kVExtraction.getValuePattern();

				List<OutputDataCarrier> afterValueExtractionList = PatternMatcherUtil.findPattern(topLineDataCarrier, valuePattern,
						getConfidenceScore());

				BigInteger keyX0 = keyCoordinates.getX0();
				BigInteger minValue = null;
				BigInteger tempValue = null;
				OutputDataCarrier valueOutputDataCarrier = null;
				for (OutputDataCarrier dataCarrier : afterValueExtractionList) {
					Span span = dataCarrier.getSpan();
					Coordinates valueCoordinates = span.getCoordinates();
					BigInteger valueX0 = valueCoordinates.getX0();
					if (valueX0.longValue() < keyX0.longValue()) {
						tempValue = valueX0.subtract(keyX0);
						if (tempValue.longValue() < BatchConstants.ZERO) {
							tempValue = tempValue.negate();
						}
						if (null == minValue) {
							minValue = tempValue;
							valueOutputDataCarrier = dataCarrier;
						} else if (tempValue.subtract(minValue).longValue() < BatchConstants.ZERO) {
							minValue = tempValue;
							valueOutputDataCarrier = dataCarrier;
						}
					}
				}
				// Append values to the right according to the number of words specified by admin.
				appendValues(kVExtraction, topLineDataCarrier, valueOutputDataCarrier);
				valueFoundData.add(valueOutputDataCarrier);
			}
		}
	}

	/**
	 * Top location finder. Method will search to the top of the key word.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 */
	public final void topLocation(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {
		boolean isValid = true;
		if (currentLineIndex < BatchConstants.ZERO || currentLineIndex >= lineOutputDataCarrierList.size() || currentLineIndex - BatchConstants.ONE < BatchConstants.ZERO) {
			isValid = false;
		}
		if (isValid) {
			LineDataCarrier topLineDataCarrier = lineOutputDataCarrierList.get(currentLineIndex - BatchConstants.ONE);
			if (null == topLineDataCarrier) {
				isValid = false;
			}

			List<Span> spanList = topLineDataCarrier.getSpanList();
			String rowData = topLineDataCarrier.getLineRowData();
			if (null == spanList || null == rowData || rowData.isEmpty()) {
				isValid = false;
			}
			if (isValid) {
				Coordinates keyCoordinates = keyCoordinate;
				String valuePattern = kVExtraction.getValuePattern();

				List<OutputDataCarrier> afterValueExtractionList = PatternMatcherUtil.findPattern(topLineDataCarrier, valuePattern,
						getConfidenceScore());

				BigInteger keyX0 = keyCoordinates.getX0();
				BigInteger keyX1 = keyCoordinates.getX1();
				BigInteger minValue = null;
				BigInteger tempValue = null;
				OutputDataCarrier valueOutputDataCarrier = null;

				for (OutputDataCarrier dataCarrier : afterValueExtractionList) {
					Span span = dataCarrier.getSpan();
					Coordinates valueCoordinates = span.getCoordinates();
					BigInteger valueX0 = valueCoordinates.getX0();
					BigInteger valueX1 = valueCoordinates.getX1();
					if ((valueX0.longValue() < keyX1.longValue()) && (valueX1.longValue() >= keyX0.longValue())) {
						tempValue = valueX0.subtract(keyX0);
						if (tempValue.longValue() < BatchConstants.ZERO) {
							tempValue = tempValue.negate();
						}
						if (null == minValue) {
							minValue = tempValue;
							valueOutputDataCarrier = dataCarrier;
						} else if (tempValue.subtract(minValue).longValue() < BatchConstants.ZERO) {
							minValue = tempValue;
							valueOutputDataCarrier = dataCarrier;
						}
					}
				}
				// Append values to the right according to the number of words specified by admin.
				appendValues(kVExtraction, topLineDataCarrier, valueOutputDataCarrier);
				valueFoundData.add(valueOutputDataCarrier);
			}
		}
	}

	private void appendValues(final InputDataCarrier kVExtraction, final LineDataCarrier lineDataCarrier,
			OutputDataCarrier valueOutputDataCarrier) {
		if (null != valueOutputDataCarrier) {
			String foundValue = valueOutputDataCarrier.getValue();
			Span span = valueOutputDataCarrier.getSpan();
			if (null != foundValue && !foundValue.isEmpty() && null != span) {
				float confidence = BatchConstants.ZERO;
				List<Coordinates> coordinatesList = new ArrayList<Coordinates>();
				coordinatesList.add(span.getCoordinates());
				String[] foundValArr = foundValue.split(KVFinderConstants.SPACE);
				Integer spanIndex = lineDataCarrier.getIndexOfSpan(span);
				Span rightSpan = null;
				Span lastSpan = span;
				int foundDataLength = foundValArr.length;
				if (foundDataLength > BatchConstants.ZERO && span.getValue() != null && !span.getValue().isEmpty()) {
					confidence = confidence + foundValArr[BatchConstants.ZERO].length() * this.confidenceScoreFloat / span.getValue().length();
				}
				if (null != spanIndex && null != foundValArr && foundDataLength > 1) {
					for (int count = 1; count < foundDataLength; count++) {
						rightSpan = lineDataCarrier.getRightSpan(spanIndex);
						if (null != rightSpan) {
							String currSpanValue = rightSpan.getValue();
							if (currSpanValue != null && !currSpanValue.isEmpty()) {
								confidence = confidence + foundValArr[count].length() * this.confidenceScoreFloat
										/ currSpanValue.length();
							}
							coordinatesList.add(rightSpan.getCoordinates());
							lastSpan = rightSpan;
						}
						spanIndex = spanIndex + BatchConstants.ONE;
					}
				}
				Coordinates valueCoordinates = HocrUtil.getRectangleCoordinates(coordinatesList);
				Integer noOfWords = kVExtraction.getNoOfWords();
				int numberOfWords = BatchConstants.ZERO;
				Span valueSpan = new Span();
				valueSpan.setCoordinates(valueCoordinates);
				if (null != noOfWords && noOfWords > BatchConstants.ZERO) {
					numberOfWords = noOfWords;
					valueOutputDataCarrier.setSpan(lastSpan);
					addValues(lineDataCarrier, valueOutputDataCarrier, numberOfWords, valueCoordinates);
				}
				valueOutputDataCarrier.setSpan(valueSpan);
				if (foundDataLength > BatchConstants.ZERO) {
					valueOutputDataCarrier.setConfidence(confidence / foundDataLength);
				}
				// valueFoundData.add(valueOutputDataCarrier);
			}
		}
	}

	/**
	 * Method to locate the values in a zone on the basis of fetch Value.
	 * 
	 * @param zonalLineList
	 * @param kvExtraction
	 * @param valueFoundData
	 * @param zoneCoordinates
	 * @throws DCMAApplicationException
	 */
	private void createOutputDataCarrierList(List<LineDataCarrier> zonalLineList, final InputDataCarrier kvExtraction,
			CustomList valueFoundData, final Coordinates zoneCoordinates) throws DCMAApplicationException {
		List<OutputDataCarrier> dataCarrierList = new ArrayList<OutputDataCarrier>();
		for (Iterator<LineDataCarrier> iterator = zonalLineList.iterator(); iterator.hasNext();) {
			LineDataCarrier lineDataCarrier = (LineDataCarrier) iterator.next();
			dataCarrierList.addAll(applyValuePattern(kvExtraction, zoneCoordinates, lineDataCarrier));
		}
		if (dataCarrierList != null && !dataCarrierList.isEmpty()) {
			// valueFoundData.addAll(dataCarrierList);
			// Basis of fetchValue : ALL , FIRST , LAST
			switch (kvExtraction.getFetchValue()) {
				case ALL:
					valueFoundData.addAll(dataCarrierList);
					break;
				case LAST:
					valueFoundData.add(dataCarrierList.get(dataCarrierList.size() - BatchConstants.ONE));
					break;
				case FIRST:
					valueFoundData.add(dataCarrierList.get(BatchConstants.ZERO));
					break;
				default:
					break;
			}
		}
		if (valueFoundData != null && !valueFoundData.isEmpty() && valueFoundData.size() > BatchConstants.ONE) {
			List<OutputDataCarrier> sortedList = getSortedList(valueFoundData);
			concatenateList(valueFoundData, sortedList);
		}
	}

	private void concatenateList(final CustomList valueFoundData, final List<OutputDataCarrier> outputDataCarriers) {
		StringBuffer valueList = new StringBuffer();
		Coordinates coordinates = new Coordinates();
		float confidence = BatchConstants.ZERO;
		for (int index = BatchConstants.ZERO; index < outputDataCarriers.size(); index++) {
			OutputDataCarrier outputDataCarrier = outputDataCarriers.get(index);
			Span span = outputDataCarrier.getSpan();
			if (null != span && span.getCoordinates() != null) {
				if (index == BatchConstants.ZERO) {
					coordinates.setX0(span.getCoordinates().getX0());
					coordinates.setY0(span.getCoordinates().getY0());
					coordinates.setX1(span.getCoordinates().getX1());
					coordinates.setY1(span.getCoordinates().getY1());
					confidence = outputDataCarrier.getConfidence();
				} else {
					setValueCoordinates(coordinates, span);
				}
			}
			valueList.append(KVFinderConstants.SPACE).append(outputDataCarrier.getValue());
		}

		Span span = new Span();
		span.setValue(valueList.toString());
		span.setCoordinates(coordinates);
		OutputDataCarrier finalConcatenatedoutput = new OutputDataCarrier(span, confidence, valueList.toString());
		valueFoundData.clear();
		valueFoundData.add(finalConcatenatedoutput);
	}

	/**
	 * Method to determine whether the zone is valid with correct co-ordinates.
	 * 
	 * @param zoneCoordinates Coordinates
	 * @return boolean
	 */
	private boolean isValidZone(final Coordinates zoneCoordinates) {
		boolean isValidZone = true;
		if (zoneCoordinates.getX0().longValue() <= BatchConstants.ZERO && zoneCoordinates.getX1().longValue() <= BatchConstants.ZERO
				&& zoneCoordinates.getY0().longValue() <= BatchConstants.ZERO && zoneCoordinates.getY1().longValue() <= BatchConstants.ZERO) {
			isValidZone = false;
		}
		return isValidZone;
	}

	/**
	 * @param kvExtraction
	 * @param zoneCoordinates
	 * @param lineDataCarrier
	 * @return List<OutputDataCarrier>
	 * @throws DCMAApplicationException
	 */
	private List<OutputDataCarrier> applyValuePattern(final InputDataCarrier kvExtraction, final Coordinates zoneCoordinates,
			final LineDataCarrier lineDataCarrier) throws DCMAApplicationException {
		final List<OutputDataCarrier> dataCarrierList = PatternMatcherUtil.findPattern(lineDataCarrier,
				kvExtraction.getValuePattern(), getConfidenceScore());
		float confidenceInt = KVFinderConstants.DEFAULT_CONFIDENCE_SCORE;
		try {
			confidenceInt = Float.parseFloat(getConfidenceScore());
		} catch (NumberFormatException nfe) {
			LOGGER.error("Invalid value for confidence score specified. Setting it to its default value 100.");
		}
		final List<OutputDataCarrier> finalDataCarrierList = new ArrayList<OutputDataCarrier>();
		finalDataCarrierList.addAll(dataCarrierList);
		boolean isValidData = true;
		List<Coordinates> coordinatesList = new ArrayList<Coordinates>();
		if (dataCarrierList != null && !dataCarrierList.isEmpty()) {
			for (OutputDataCarrier dataCarrier : dataCarrierList) {
				float confidence = BatchConstants.ZERO;
				isValidData = true;
				// Add coordinates for multiple word capture support.
				Span span = dataCarrier.getSpan();
				coordinatesList.add(span.getCoordinates());
				String foundValue = dataCarrier.getValue();
				String[] foundValArr = foundValue.split(KVFinderConstants.SPACE);
				Integer spanIndex = lineDataCarrier.getIndexOfSpan(span);
				Span rightSpan = null;
				int wordsCount = foundValArr.length;
				if (wordsCount > BatchConstants.ZERO && span.getValue() != null && !span.getValue().isEmpty()) {
					confidence = confidence + foundValArr[BatchConstants.ZERO].length() * confidenceInt / span.getValue().length();
				}
				if (!isValidCoordinatesForZone(span.getCoordinates(), zoneCoordinates)) {
					finalDataCarrierList.remove(dataCarrier);
					isValidData = false;
				} else if (null != spanIndex && null != foundValArr && foundValArr.length > BatchConstants.ONE) {
					for (int count = BatchConstants.ONE; count < wordsCount; count++) {
						rightSpan = lineDataCarrier.getRightSpan(spanIndex);
						if (null != rightSpan && !isValidCoordinatesForZone(rightSpan.getCoordinates(), zoneCoordinates)) {
							finalDataCarrierList.remove(dataCarrier);
							isValidData = false;
							break;
						} else if (null != rightSpan) {
							String currSpanValue = rightSpan.getValue();
							if (currSpanValue != null && !currSpanValue.isEmpty()) {
								confidence = confidence + foundValArr[count].length() * confidenceInt / currSpanValue.length();
							}
							coordinatesList.add(rightSpan.getCoordinates());
						}
						spanIndex = spanIndex + BatchConstants.ONE;
					}
				}
				if (isValidData) {
					Span valueSpan = new Span();
					if (wordsCount > BatchConstants.ONE) {
						confidence = confidence / wordsCount;
					}
					if (kvExtraction.getMultiplier() != null && kvExtraction.getMultiplier() > BatchConstants.ZERO) {
						confidence = confidence * kvExtraction.getMultiplier();
					}
					dataCarrier.setConfidence(confidence);
					valueSpan.setCoordinates(HocrUtil.getRectangleCoordinates(coordinatesList));
					dataCarrier.setSpan(valueSpan);
				}
				coordinatesList.clear();
			}
		}
		return finalDataCarrierList;
	}

	/**
	 * @param coordinate
	 * @param zoneCoordinates
	 * @return boolean
	 */
	private boolean isValidCoordinatesForZone(final Coordinates coordinate, final Coordinates zoneCoordinates) {
		boolean isValidCoordinate = false;
		if (coordinate != null && zoneCoordinates != null) {
			final int coordX0 = coordinate.getX0().intValue();
			final int zoneX0 = zoneCoordinates.getX0().intValue();
			final int coordX1 = coordinate.getX1().intValue();
			final int zoneX1 = zoneCoordinates.getX1().intValue();
			if ((coordX0 >= zoneX0 && coordX0 <= zoneX1) || (coordX1 >= zoneX0 && coordX1 <= zoneX1)) {
				isValidCoordinate = true;
			}
		}
		return isValidCoordinate;
	}

	private List<OutputDataCarrier> getSortedList(final CustomList valueFoundData) {

		List<OutputDataCarrier> list = valueFoundData.getAscendingList();
		int defaultValue = BatchConstants.TWENTY;
		try {
			defaultValue = Integer.parseInt(getWidthOfLine());
		} catch (NumberFormatException nfe) {
			LOGGER.error(nfe.getMessage(), nfe);
		}
		final int compareValue = defaultValue;
		Collections.sort(list, new Comparator<OutputDataCarrier>() {

			public int compare(final OutputDataCarrier outputCarrier1, final OutputDataCarrier outputCarrier2) {
				Span firstSpan = outputCarrier1.getSpan();
				Span secSpan = outputCarrier2.getSpan();
				BigInteger s1Y1 = firstSpan.getCoordinates().getY1();
				BigInteger s2Y1 = secSpan.getCoordinates().getY1();
				int returnValue = BatchConstants.ZERO;
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
		return list;
	}

	/**
	 * @param inputDataCarrier
	 * @param outputDataCarrierList
	 * @param lineDataCarrierList
	 * @param currentLineIndex
	 * @param keyCoordinate
	 * @throws DCMAApplicationException
	 */
	public void extractValueFromZone(final InputDataCarrier inputDataCarrier, final CustomList outputDataCarrierList,
			final List<LineDataCarrier> lineDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {
		Coordinates valueZone = createValueZone(keyCoordinate, inputDataCarrier.getXoffset(), inputDataCarrier.getYoffset(),
				inputDataCarrier.getLength(), inputDataCarrier.getWidth());
		if (isValidZone(valueZone)) {
			List<LineDataCarrier> zonalLineList = getZonalLineList(lineDataCarrierList, currentLineIndex, valueZone);
			createOutputDataCarrierList(zonalLineList, inputDataCarrier, outputDataCarrierList, valueZone);
		}
	}

	/**
	 * @param lineDataCarrierList
	 * @param currentLineIndex
	 * @param valueZone
	 * @return
	 */
	private List<LineDataCarrier> getZonalLineList(final List<LineDataCarrier> lineDataCarrierList, final int currentLineIndex,
			final Coordinates valueZone) {
		LOGGER.info("Entering method getZonalLineList....");
		// LineDataCarrier topLineDataCarrier = null;
		LineDataCarrier lineDataCarrier = null;
		List<LineDataCarrier> zonalLineList = new ArrayList<LineDataCarrier>();
		boolean firstValidRowFound = false;
		LineDataCarrier validLineDataCarrier = null;
		for (int index = currentLineIndex; index >= BatchConstants.ZERO; index--) {
			lineDataCarrier = lineDataCarrierList.get(index);
			if (null == lineDataCarrier) {
				continue;
			}
			Coordinates rowCoordinates = lineDataCarrier.getRowCoordinates();
			if (HocrUtil.isValidRowForZone(rowCoordinates, valueZone)) {
				getValidZonalData(valueZone, lineDataCarrier, zonalLineList);
				zonalLineList.add(BatchConstants.ZERO, validLineDataCarrier);
				firstValidRowFound = true;
			} else if (firstValidRowFound) {
				break;
			}
		}
		firstValidRowFound = false;
		for (int index = currentLineIndex + BatchConstants.ONE; index < lineDataCarrierList.size(); index++) {
			lineDataCarrier = lineDataCarrierList.get(index);
			if (null == lineDataCarrier) {
				continue;
			}
			Coordinates rowCoordinates = lineDataCarrier.getRowCoordinates();
			if (HocrUtil.isValidRowForZone(rowCoordinates, valueZone)) {
				getValidZonalData(valueZone, lineDataCarrier, zonalLineList);
				firstValidRowFound = true;
			} else if (firstValidRowFound) {
				break;
			}
		}
		LOGGER.info("Exiting method getZonalLineList....");
		return zonalLineList;
	}

	private void getValidZonalData(final Coordinates valueZone, LineDataCarrier lineDataCarrier, List<LineDataCarrier> zonalLineList) {
		List<Span> spanList;
		LineDataCarrier validLineDataCarrier;
		validLineDataCarrier = new LineDataCarrier(lineDataCarrier.getPageID());
		spanList = lineDataCarrier.getSpanList();
		if (spanList != null) {
			for (Span span : spanList) {
				if (span != null && HocrUtil.isInsideZone(span.getCoordinates(), valueZone)) {
					validLineDataCarrier.getSpanList().add(span);
				}
			}
		}
		zonalLineList.add(validLineDataCarrier);
	}

	/**
	 * This method creates a value zone relative to the key coordinates.
	 * 
	 * @param keyCoordinate
	 * @param xoffset
	 * @param yoffset
	 * @param length
	 * @param width
	 * @return
	 */
	private Coordinates createValueZone(final Coordinates keyCoordinate, final int xoffset, final int yoffset, final int length,
			final int width) {
		Coordinates valueZone = new Coordinates();
		if (keyCoordinate != null) {
			valueZone.setX0(BigInteger.valueOf(keyCoordinate.getX1().intValue() + xoffset));
			valueZone.setY0(BigInteger.valueOf(keyCoordinate.getY1().intValue() + yoffset));
			valueZone.setX1(valueZone.getX0().add(BigInteger.valueOf(length)));
			valueZone.setY1(valueZone.getY0().add(BigInteger.valueOf(width)));
		}
		return valueZone;
	}
}
