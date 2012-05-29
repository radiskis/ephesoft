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

package com.ephesoft.dcma.kvfinder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
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
	}

	/**
	 * Method is responsible for finding the patterns and returned the found data List.
	 * 
	 * @param span Span
	 * @param patternStr String
	 * @return OutputDataCarrier
	 * @throws DCMAApplicationException Check for all the input parameters and find the pattern.
	 */
	public final OutputDataCarrier findPattern(final Span span, final String patternStr) throws DCMAApplicationException {

		String errMsg = null;
		OutputDataCarrier dataCarrier = null;
		final CharSequence inputStr = span.getValue();
		if (null == inputStr || KVFinderConstants.EMPTY.equals(inputStr)) {

			errMsg = "Invalid input character sequence.";
			// throw new DCMAApplicationException(errMsg);
			LOGGER.info(errMsg);

		} else {

			if (null == patternStr || KVFinderConstants.EMPTY.equals(patternStr)) {
				errMsg = "Invalid input pattern sequence.";
				throw new DCMAApplicationException(errMsg);
			}

			// Compile and use regular
			// expression
			final Pattern pattern = Pattern.compile(patternStr);
			final Matcher matcher = pattern.matcher(inputStr);
			// boolean matchFound = matcher.find();
			while (matcher.find()) {
				// Get all groups for this match
				for (int i = 0; i <= matcher.groupCount(); i++) {
					final String groupStr = matcher.group(i);

					if (groupStr != null) {
						int confidenceInt = 100;
						try {
							confidenceInt = Integer.parseInt(getConfidenceScore());
						} catch (NumberFormatException nfe) {
							LOGGER.error(nfe.getMessage(), nfe);
						}
						final float confidence = (groupStr.length() * confidenceInt) / inputStr.length();
						dataCarrier = new OutputDataCarrier(span, confidence, groupStr);
						LOGGER.info(groupStr);
					}
				}
			}
		}

		return dataCarrier;
	}

	/**
	 * Method is responsible for finding the patterns and returned the found data List.
	 * 
	 * @param inputData String
	 * @param patternStr String
	 * @param spanList
	 * @return List<OutputDataCarrier>
	 * @throws DCMAApplicationException Check for all the input parameters and find the pattern.
	 */
	public final List<OutputDataCarrier> findPattern(final String inputData, final String patternStr, List<Span> spanList)
			throws DCMAApplicationException {

		String errMsg = null;
		List<OutputDataCarrier> dataCarrierList = new ArrayList<OutputDataCarrier>();
		final CharSequence inputStr = inputData;
		if (null == inputStr || KVFinderConstants.EMPTY.equals(inputStr)) {

			errMsg = "Invalid input character sequence.";
			// throw new DCMAApplicationException(errMsg);
			LOGGER.info(errMsg);

		} else {

			if (null == patternStr || KVFinderConstants.EMPTY.equals(patternStr)) {
				errMsg = "Invalid input pattern sequence.";
				throw new DCMAApplicationException(errMsg);
			}

			// Compile and use regular
			// expression
			final Pattern pattern = Pattern.compile(patternStr);
			final Matcher matcher = pattern.matcher(inputStr);
			// boolean matchFound = matcher.find();
			while (matcher.find()) {
				// Get all groups for this match
				for (int i = 0; i <= matcher.groupCount(); i++) {

					final String groupStr = matcher.group(i);
					final int startIndex = matcher.start();
					final Span matchedSpan = getMatchedSpan(spanList, startIndex);
					if (groupStr != null) {
						int confidenceInt = 100;
						try {
							confidenceInt = Integer.parseInt(getConfidenceScore());
						} catch (NumberFormatException nfe) {
							LOGGER.error(nfe.getMessage(), nfe);
						}
						final float confidence = (groupStr.length() * confidenceInt) / inputStr.length();
						OutputDataCarrier dataCarrier = new OutputDataCarrier(matchedSpan, confidence, groupStr);
						dataCarrierList.add(dataCarrier);
						LOGGER.info(groupStr);
					}
				}
			}
		}

		return dataCarrierList;
	}

	private Span getMatchedSpan(final List<Span> spanList, final int startIndex) {
		int spanIndex = 0;
		boolean isFirstSpan = Boolean.TRUE;
		Span matchedSpan = null;
		for (Span span : spanList) {
			if (null != span && null != span.getValue()) {
				spanIndex = spanIndex + span.getValue().length();
				if (!isFirstSpan) {
					spanIndex = spanIndex + 1;
				}
				if (spanIndex > startIndex) {
					matchedSpan = span;
					break;
				}
			}
			isFirstSpan = Boolean.FALSE;
		}
		return matchedSpan;
	}

	/**
	 * Right location finder. Method will search to the right of the key word.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List<Span>
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 */
	public final void rightLocation(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {

		if (currentLineIndex < 0 || currentLineIndex >= lineOutputDataCarrierList.size()) {
			return;
		}

		LineDataCarrier lineOutputDataCarrier = lineOutputDataCarrierList.get(currentLineIndex);
		if (null == lineOutputDataCarrier) {
			return;
		}

		List<Span> spanList = lineOutputDataCarrier.getSpanList();
		if (null == spanList) {
			return;
		}

		Coordinates keyCoordinates = keyCoordinate;

		final String valuePattern = kVExtraction.getValuePattern();

		final List<OutputDataCarrier> afterValueExtractionList = new ArrayList<OutputDataCarrier>();
		for (Span span : spanList) {
			final OutputDataCarrier dataCarrier = findPattern(span, valuePattern);
			if (null != dataCarrier) {
				afterValueExtractionList.add(dataCarrier);
			}
		}

		final BigInteger keyX1 = keyCoordinates.getX1();
		BigInteger minValue = null;
		BigInteger tempValue = null;
		OutputDataCarrier valueOutputDataCarrier = null;
		for (OutputDataCarrier dataCarrier : afterValueExtractionList) {
			final Span span = dataCarrier.getSpan();
			final Coordinates valueCoordinates = span.getCoordinates();
			final BigInteger valueX0 = valueCoordinates.getX0();
			if (keyX1.longValue() < valueX0.longValue()) {
				tempValue = valueX0.subtract(keyX1);
				if (tempValue.longValue() < 0) {
					tempValue = tempValue.negate();
				}
				if (null == minValue) {
					minValue = tempValue;
					valueOutputDataCarrier = dataCarrier;
				} else {
					if (tempValue.subtract(minValue).longValue() < 0) {
						minValue = tempValue;
						valueOutputDataCarrier = dataCarrier;
					}
				}
			}
		}

		if (null != valueOutputDataCarrier) {

			Integer noOfWords = kVExtraction.getNoOfWords();
			int numberOfWords = 0;
			if (null != noOfWords) {
				numberOfWords = noOfWords;
			}

			if (numberOfWords > 0) {
				addValues(lineOutputDataCarrier, valueOutputDataCarrier, numberOfWords);
			}
			valueFoundData.add(valueOutputDataCarrier);
		}

	}

	private void addValues(LineDataCarrier lineOutputDataCarrier, OutputDataCarrier valueOutputDataCarrier, int numberOfWords) {
		StringBuffer finalValue = new StringBuffer(valueOutputDataCarrier.getValue());
		Span valueSpan = valueOutputDataCarrier.getSpan();
		if (null != valueSpan) {
			Integer spanIndex = lineOutputDataCarrier.getIndexOfSpan(valueSpan);
			if (spanIndex != null) {
				Span rightSpan = null;
				Coordinates coordinates = new Coordinates();
				if (valueSpan.getCoordinates() != null) {
					coordinates.setX0(valueSpan.getCoordinates().getX0());
					coordinates.setY0(valueSpan.getCoordinates().getY0());
					coordinates.setX1(valueSpan.getCoordinates().getX1());
					coordinates.setY1(valueSpan.getCoordinates().getY1());
				}
				for (int index = 0; index < numberOfWords; index++) {
					rightSpan = lineOutputDataCarrier.getRightSpan(spanIndex);
					if (null != rightSpan && rightSpan.getValue() != null) {
						finalValue.append(KVFinderConstants.SPACE);
						finalValue.append(rightSpan.getValue());
						setValueCoordinates(coordinates, rightSpan);
					}
					spanIndex = spanIndex + 1;
				}
				valueSpan.setCoordinates(coordinates);
			}
			valueOutputDataCarrier.setValue(finalValue.toString());
		}
	}

	private void setValueCoordinates(Coordinates coordinates, Span span) {
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
			if (spanX0.compareTo(coordX0) == -1) {
				coordinates.setX0(spanX0);
			}
			if (spanY0.compareTo(coordY0) == -1) {
				coordinates.setY0(spanY0);
			}
			if (spanX1.compareTo(coordX1) == 1) {
				coordinates.setX1(spanX1);
			}
			if (spanY1.compareTo(coordY1) == 1) {
				coordinates.setY1(spanY1);
			}
		}
	}

	/**
	 * Left location finder. Method will search to the left of the key word.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List<Span>
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 */
	public final void leftLocation(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {

		if (currentLineIndex < 0 || currentLineIndex >= lineOutputDataCarrierList.size()) {
			return;
		}

		LineDataCarrier lineOutputDataCarrier = lineOutputDataCarrierList.get(currentLineIndex);
		if (null == lineOutputDataCarrier) {
			return;
		}

		List<Span> spanList = lineOutputDataCarrier.getSpanList();
		if (null == spanList) {
			return;
		}

		Coordinates keyCoordinates = keyCoordinate;

		final String valuePattern = kVExtraction.getValuePattern();

		final List<OutputDataCarrier> afterValueExtractionList = new ArrayList<OutputDataCarrier>();
		for (Span span : spanList) {
			final OutputDataCarrier dataCarrier = findPattern(span, valuePattern);
			if (null != dataCarrier) {
				afterValueExtractionList.add(dataCarrier);
			}
		}

		final BigInteger keyX0 = keyCoordinates.getX0();
		BigInteger minValue = null;
		BigInteger tempValue = null;
		OutputDataCarrier valueOutputDataCarrier = null;
		for (OutputDataCarrier dataCarrier : afterValueExtractionList) {
			final Span span = dataCarrier.getSpan();
			final Coordinates valueCoordinates = span.getCoordinates();
			final BigInteger valueX1 = valueCoordinates.getX1();
			if (valueX1.longValue() < keyX0.longValue()) {
				tempValue = keyX0.subtract(valueX1);
				if (tempValue.longValue() < 0) {
					tempValue = tempValue.negate();
				}
				if (null == minValue) {
					minValue = tempValue;
					valueOutputDataCarrier = dataCarrier;
				} else {
					if (tempValue.subtract(minValue).longValue() < 0) {
						minValue = tempValue;
						valueOutputDataCarrier = dataCarrier;
					}
				}
			}
		}

		if (null != valueOutputDataCarrier) {

			Integer noOfWords = kVExtraction.getNoOfWords();
			int numberOfWords = 0;
			if (null != noOfWords) {
				numberOfWords = noOfWords;
			}

			if (numberOfWords > 0) {
				addValues(lineOutputDataCarrier, valueOutputDataCarrier, numberOfWords);
			}
			valueFoundData.add(valueOutputDataCarrier);
		}

	}

	/**
	 * Bottom Right location finder. Method will search to the bottom right of the key word.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List<Span>
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 */
	public final void bottomRightLocation(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {

		if (currentLineIndex < 0 || currentLineIndex >= lineOutputDataCarrierList.size()) {
			return;
		}

		if (currentLineIndex + 1 >= lineOutputDataCarrierList.size()) {
			return;
		}

		LineDataCarrier topLineDataCarrier = lineOutputDataCarrierList.get(currentLineIndex + 1);
		if (null == topLineDataCarrier) {
			return;
		}

		List<Span> spanList = topLineDataCarrier.getSpanList();
		if (null == spanList) {
			return;
		}

		Coordinates keyCoordinates = keyCoordinate;

		final String valuePattern = kVExtraction.getValuePattern();

		final List<OutputDataCarrier> afterValueExtractionList = new ArrayList<OutputDataCarrier>();
		for (Span span : spanList) {
			OutputDataCarrier dataCarrier = findPattern(span, valuePattern);
			if (null != dataCarrier) {
				afterValueExtractionList.add(dataCarrier);
			}
		}

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
				if (tempValue.longValue() < 0) {
					tempValue = tempValue.negate();
				}
				if (null == minValue) {
					minValue = tempValue;
					valueOutputDataCarrier = dataCarrier;
				} else {
					if (tempValue.subtract(minValue).longValue() < 0) {
						minValue = tempValue;
						valueOutputDataCarrier = dataCarrier;
					}
				}
			}
		}

		if (null != valueOutputDataCarrier) {

			Integer noOfWords = kVExtraction.getNoOfWords();
			int numberOfWords = 0;
			if (null != noOfWords) {
				numberOfWords = noOfWords;
			}

			if (numberOfWords > 0) {
				addValues(topLineDataCarrier, valueOutputDataCarrier, numberOfWords);
			}
			valueFoundData.add(valueOutputDataCarrier);
		}

	}

	/**
	 * Bottom left location finder. Method will search to the bottom left of the key word.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List<Span>
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 */
	public final void bottomLeftLocation(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {

		if (currentLineIndex < 0 || currentLineIndex >= lineOutputDataCarrierList.size()) {
			return;
		}

		if (currentLineIndex + 1 >= lineOutputDataCarrierList.size()) {
			return;
		}

		LineDataCarrier topLineDataCarrier = lineOutputDataCarrierList.get(currentLineIndex + 1);
		if (null == topLineDataCarrier) {
			return;
		}

		List<Span> spanList = topLineDataCarrier.getSpanList();
		if (null == spanList) {
			return;
		}

		Coordinates keyCoordinates = keyCoordinate;

		String valuePattern = kVExtraction.getValuePattern();

		List<OutputDataCarrier> afterValueExtractionList = new ArrayList<OutputDataCarrier>();
		for (Span span : spanList) {
			OutputDataCarrier dataCarrier = findPattern(span, valuePattern);
			if (null != dataCarrier) {
				afterValueExtractionList.add(dataCarrier);
			}
		}

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
				if (tempValue.longValue() < 0) {
					tempValue = tempValue.negate();
				}
				if (null == minValue) {
					minValue = tempValue;
					valueOutputDataCarrier = dataCarrier;
				} else {
					if (tempValue.subtract(minValue).longValue() < 0) {
						minValue = tempValue;
						valueOutputDataCarrier = dataCarrier;
					}
				}
			}
		}

		if (null != valueOutputDataCarrier) {

			Integer noOfWords = kVExtraction.getNoOfWords();
			int numberOfWords = 0;
			if (null != noOfWords) {
				numberOfWords = noOfWords;
			}

			if (numberOfWords > 0) {
				addValues(topLineDataCarrier, valueOutputDataCarrier, numberOfWords);
			}
			valueFoundData.add(valueOutputDataCarrier);
		}

	}

	/**
	 * Bottom location finder. Method will search to the bottom of the key word.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List<Span>
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 */
	public final void bottomLocation(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {

		if (currentLineIndex < 0 || currentLineIndex >= lineOutputDataCarrierList.size()) {
			return;
		}

		if (currentLineIndex + 1 >= lineOutputDataCarrierList.size()) {
			return;
		}

		LineDataCarrier topLineDataCarrier = lineOutputDataCarrierList.get(currentLineIndex + 1);
		if (null == topLineDataCarrier) {
			return;
		}

		List<Span> spanList = topLineDataCarrier.getSpanList();
		if (null == spanList) {
			return;
		}

		Coordinates keyCoordinates = keyCoordinate;

		String valuePattern = kVExtraction.getValuePattern();

		List<OutputDataCarrier> afterValueExtractionList = new ArrayList<OutputDataCarrier>();
		for (Span span : spanList) {
			OutputDataCarrier dataCarrier = findPattern(span, valuePattern);
			if (null != dataCarrier) {
				afterValueExtractionList.add(dataCarrier);
			}
		}

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
				if (tempValue.longValue() < 0) {
					tempValue = tempValue.negate();
				}
				if (null == minValue) {
					minValue = tempValue;
					valueOutputDataCarrier = dataCarrier;
				} else {
					if (tempValue.subtract(minValue).longValue() < 0) {
						minValue = tempValue;
						valueOutputDataCarrier = dataCarrier;
					}
				}
			}
		}

		if (null != valueOutputDataCarrier) {

			Integer noOfWords = kVExtraction.getNoOfWords();
			int numberOfWords = 0;
			if (null != noOfWords) {
				numberOfWords = noOfWords;
			}

			if (numberOfWords > 0) {
				addValues(topLineDataCarrier, valueOutputDataCarrier, numberOfWords);
			}
			valueFoundData.add(valueOutputDataCarrier);
		}

	}

	/**
	 * Top right location finder. Method will search to the top right of the key word.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List<Span>
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 */
	public final void topRightLocation(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {

		if (currentLineIndex < 0 || currentLineIndex >= lineOutputDataCarrierList.size()) {
			return;
		}

		if (currentLineIndex - 1 < 0) {
			return;
		}

		LineDataCarrier topLineDataCarrier = lineOutputDataCarrierList.get(currentLineIndex - 1);
		if (null == topLineDataCarrier) {
			return;
		}

		List<Span> spanList = topLineDataCarrier.getSpanList();
		if (null == spanList) {
			return;
		}

		Coordinates keyCoordinates = keyCoordinate;
		String valuePattern = kVExtraction.getValuePattern();

		List<OutputDataCarrier> afterValueExtractionList = new ArrayList<OutputDataCarrier>();
		for (Span span : spanList) {
			OutputDataCarrier dataCarrier = findPattern(span, valuePattern);
			if (null != dataCarrier) {
				afterValueExtractionList.add(dataCarrier);
			}
		}

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
				if (tempValue.longValue() < 0) {
					tempValue = tempValue.negate();
				}
				if (null == minValue) {
					minValue = tempValue;
					valueOutputDataCarrier = dataCarrier;
				} else {
					if (tempValue.subtract(minValue).longValue() < 0) {
						minValue = tempValue;
						valueOutputDataCarrier = dataCarrier;
					}
				}
			}
		}

		if (null != valueOutputDataCarrier) {

			Integer noOfWords = kVExtraction.getNoOfWords();
			int numberOfWords = 0;
			if (null != noOfWords) {
				numberOfWords = noOfWords;
			}

			if (numberOfWords > 0) {
				addValues(topLineDataCarrier, valueOutputDataCarrier, numberOfWords);
			}
			valueFoundData.add(valueOutputDataCarrier);
		}

	}

	/**
	 * Top left location finder. Method will search to the top left of the key word.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List<Span>
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 */
	public final void topLeftLocation(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {

		if (currentLineIndex < 0 || currentLineIndex >= lineOutputDataCarrierList.size()) {
			return;
		}

		if (currentLineIndex - 1 < 0) {
			return;
		}

		LineDataCarrier topLineDataCarrier = lineOutputDataCarrierList.get(currentLineIndex - 1);
		if (null == topLineDataCarrier) {
			return;
		}

		List<Span> spanList = topLineDataCarrier.getSpanList();
		if (null == spanList) {
			return;
		}

		Coordinates keyCoordinates = keyCoordinate;

		String valuePattern = kVExtraction.getValuePattern();

		List<OutputDataCarrier> afterValueExtractionList = new ArrayList<OutputDataCarrier>();
		for (Span span : spanList) {
			OutputDataCarrier dataCarrier = findPattern(span, valuePattern);
			if (null != dataCarrier) {
				afterValueExtractionList.add(dataCarrier);
			}
		}

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
				if (tempValue.longValue() < 0) {
					tempValue = tempValue.negate();
				}
				if (null == minValue) {
					minValue = tempValue;
					valueOutputDataCarrier = dataCarrier;
				} else {
					if (tempValue.subtract(minValue).longValue() < 0) {
						minValue = tempValue;
						valueOutputDataCarrier = dataCarrier;
					}
				}
			}
		}

		if (null != valueOutputDataCarrier) {

			Integer noOfWords = kVExtraction.getNoOfWords();
			int numberOfWords = 0;
			if (null != noOfWords) {
				numberOfWords = noOfWords;
			}

			if (numberOfWords > 0) {
				addValues(topLineDataCarrier, valueOutputDataCarrier, numberOfWords);
			}
			valueFoundData.add(valueOutputDataCarrier);
		}

	}

	/**
	 * Top location finder. Method will search to the top of the key word.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List<Span>
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 */
	public final void topLocation(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {

		if (currentLineIndex < 0 || currentLineIndex >= lineOutputDataCarrierList.size()) {
			return;
		}

		if (currentLineIndex - 1 < 0) {
			return;
		}

		LineDataCarrier topLineDataCarrier = lineOutputDataCarrierList.get(currentLineIndex - 1);
		if (null == topLineDataCarrier) {
			return;
		}

		List<Span> spanList = topLineDataCarrier.getSpanList();
		if (null == spanList) {
			return;
		}

		Coordinates keyCoordinates = keyCoordinate;

		String valuePattern = kVExtraction.getValuePattern();

		List<OutputDataCarrier> afterValueExtractionList = new ArrayList<OutputDataCarrier>();
		for (Span span : spanList) {
			OutputDataCarrier dataCarrier = findPattern(span, valuePattern);
			if (null != dataCarrier) {
				afterValueExtractionList.add(dataCarrier);
			}
		}

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
				if (tempValue.longValue() < 0) {
					tempValue = tempValue.negate();
				}
				if (null == minValue) {
					minValue = tempValue;
					valueOutputDataCarrier = dataCarrier;
				} else {
					if (tempValue.subtract(minValue).longValue() < 0) {
						minValue = tempValue;
						valueOutputDataCarrier = dataCarrier;
					}
				}
			}
		}

		if (null != valueOutputDataCarrier) {

			Integer noOfWords = kVExtraction.getNoOfWords();
			int numberOfWords = 0;
			if (null != noOfWords) {
				numberOfWords = noOfWords;
			}

			if (numberOfWords > 0) {
				addValues(topLineDataCarrier, valueOutputDataCarrier, numberOfWords);
			}
			valueFoundData.add(valueOutputDataCarrier);
		}

	}

	/**
	 * Bottom Zonal Extraction.Method to locate all the words in a bottom location Rectangle.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List<Span>
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 * 
	 */
	public final void bottomLocationRectangle(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {

		if (currentLineIndex < 0 || currentLineIndex >= lineOutputDataCarrierList.size()) {
			return;
		}

		if (currentLineIndex + 1 >= lineOutputDataCarrierList.size()) {
			return;
		}

		Coordinates keyCoordinates = keyCoordinate;

		BigInteger keyCenterX = keyCoordinates.getX1().subtract(keyCoordinates.getX0()).divide(BigInteger.valueOf(2));

		Coordinates zoneCoordinates = new Coordinates();
		BigInteger tempX0 = keyCoordinates.getX0().add(keyCenterX).subtract(BigInteger.valueOf(kVExtraction.getLength() / 2));
		BigInteger tempX1 = tempX0.add(BigInteger.valueOf(kVExtraction.getLength()));

		zoneCoordinates.setX0(tempX0.longValue() < 0 ? BigInteger.ZERO : tempX0);
		zoneCoordinates.setY0(keyCoordinates.getY1().add(BigInteger.valueOf(kVExtraction.getYoffset())));

		zoneCoordinates.setX1(tempX1.longValue() < 0 ? BigInteger.ZERO : tempX1);
		zoneCoordinates.setY1(zoneCoordinates.getY0().add(BigInteger.valueOf(kVExtraction.getWidth())));
		if (isValidZone(zoneCoordinates)) {
			// finding the lines in the rectangle // traverse downwards in increasing order
			List<LineDataCarrier> zonalLineList = new ArrayList<LineDataCarrier>();
			boolean firstLineFound = false;
			for (int index = currentLineIndex; index < lineOutputDataCarrierList.size(); index++) {
				LineDataCarrier bottomLineDataCarrier = lineOutputDataCarrierList.get(index);
				if (null == bottomLineDataCarrier) {
					continue;
				}
				Coordinates rowCoordinates = bottomLineDataCarrier.getRowCoordinates();
				// Compare the line Y - coordinates that touch the border or lie in that rectangle
				if (rowCoordinates.getY0().longValue() >= zoneCoordinates.getY0().longValue()
						&& rowCoordinates.getY1().longValue() <= zoneCoordinates.getY1().longValue()
						|| (rowCoordinates.getY1().longValue() >= zoneCoordinates.getY0().longValue() && rowCoordinates.getY0()
								.longValue() <= zoneCoordinates.getY0().longValue())
						|| (rowCoordinates.getY0().longValue() <= zoneCoordinates.getY1().longValue() && rowCoordinates.getY1()
								.longValue() >= zoneCoordinates.getY1().longValue())) {
					firstLineFound = true;
					zonalLineList.add(bottomLineDataCarrier);
				} else {
					if (firstLineFound) {
						break;
					}
				}
			}

			createOutputDataCarrierList(zonalLineList, kVExtraction, valueFoundData, zoneCoordinates);
		}
	}

	/**
	 * Bottom Right Zonal Extraction.Method to locate all the words in a bottom-right location Rectangle.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List<Span>
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 * 
	 */

	public final void bottomRightLocationRectangle(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {

		if (currentLineIndex < 0 || currentLineIndex >= lineOutputDataCarrierList.size()) {
			return;
		}

		if (currentLineIndex + 1 >= lineOutputDataCarrierList.size()) {
			return;
		}

		Coordinates keyCoordinates = keyCoordinate;
		BigInteger keyX1 = keyCoordinates.getX1();
		BigInteger keyY1 = keyCoordinates.getY1();

		Coordinates zoneCoordinates = new Coordinates();
		zoneCoordinates.setX0(keyX1.add(BigInteger.valueOf(kVExtraction.getXoffset())));
		zoneCoordinates.setY0(keyY1.add(BigInteger.valueOf(kVExtraction.getYoffset())));

		zoneCoordinates.setX1(zoneCoordinates.getX0().add(BigInteger.valueOf(kVExtraction.getLength())));
		zoneCoordinates.setY1(zoneCoordinates.getY0().add(BigInteger.valueOf(kVExtraction.getWidth())));
		if (isValidZone(zoneCoordinates)) {
			// finding the lines in the rectangle // traverse downwards in increasing order
			List<LineDataCarrier> zonalLineList = new ArrayList<LineDataCarrier>();
			boolean firstLineFound = false;
			for (int index = currentLineIndex; index < lineOutputDataCarrierList.size(); index++) {
				LineDataCarrier bottomLineDataCarrier = lineOutputDataCarrierList.get(index);
				if (null == bottomLineDataCarrier) {
					continue;
				}
				Coordinates rowCoordinates = bottomLineDataCarrier.getRowCoordinates();
				// Compare the line Y - coordinates that touch the border or lie in that rectangle
				if (rowCoordinates.getY0().longValue() >= zoneCoordinates.getY0().longValue()
						&& rowCoordinates.getY1().longValue() <= zoneCoordinates.getY1().longValue()
						|| (rowCoordinates.getY1().longValue() >= zoneCoordinates.getY0().longValue() && rowCoordinates.getY0()
								.longValue() <= zoneCoordinates.getY0().longValue())
						|| (rowCoordinates.getY0().longValue() <= zoneCoordinates.getY1().longValue() && rowCoordinates.getY1()
								.longValue() >= zoneCoordinates.getY1().longValue())) {
					firstLineFound = true;
					zonalLineList.add(bottomLineDataCarrier);
				} else {
					if (firstLineFound) {
						break;
					}
				}
			}

			createOutputDataCarrierList(zonalLineList, kVExtraction, valueFoundData, zoneCoordinates);
		}
	}

	/**
	 * Bottom Left Zonal Extraction.Method to locate all the words in a bottom-left location Rectangle.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List<Span>
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 * 
	 */
	public final void bottomLeftLocationRectangle(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {

		if (currentLineIndex < 0 || currentLineIndex >= lineOutputDataCarrierList.size()) {
			return;
		}

		if (currentLineIndex + 1 >= lineOutputDataCarrierList.size()) {
			return;
		}

		Coordinates keyCoordinates = keyCoordinate;
		BigInteger keyX0 = keyCoordinates.getX0();
		BigInteger keyY1 = keyCoordinates.getY1();

		Coordinates zoneCoordinates = new Coordinates();
		BigInteger tempX = keyX0.subtract(BigInteger.valueOf(kVExtraction.getXoffset()));
		BigInteger tempY = keyY1.add(BigInteger.valueOf(kVExtraction.getYoffset()));
		BigInteger tempX0 = tempX.subtract(BigInteger.valueOf(kVExtraction.getLength()));

		zoneCoordinates.setX0(tempX0.longValue() < 0 ? BigInteger.ZERO : tempX0);
		zoneCoordinates.setY0(tempY);

		zoneCoordinates.setX1(tempX.longValue() < 0 ? BigInteger.ZERO : tempX);
		zoneCoordinates.setY1(tempY.add(BigInteger.valueOf(kVExtraction.getWidth())));
		if (isValidZone(zoneCoordinates)) {
			// finding the lines in the rectangle // traverse downwards in increasing order
			List<LineDataCarrier> zonalLineList = new ArrayList<LineDataCarrier>();
			boolean firstLineFound = false;
			for (int index = currentLineIndex; index < lineOutputDataCarrierList.size(); index++) {
				LineDataCarrier bottomLineDataCarrier = lineOutputDataCarrierList.get(index);
				if (null == bottomLineDataCarrier) {
					continue;
				}
				Coordinates rowCoordinates = bottomLineDataCarrier.getRowCoordinates();
				// Compare the line Y - coordinates that touch the border or lie in that rectangle
				if (rowCoordinates.getY0().longValue() >= zoneCoordinates.getY0().longValue()
						&& rowCoordinates.getY1().longValue() <= zoneCoordinates.getY1().longValue()
						|| (rowCoordinates.getY1().longValue() >= zoneCoordinates.getY0().longValue() && rowCoordinates.getY0()
								.longValue() <= zoneCoordinates.getY0().longValue())
						|| (rowCoordinates.getY0().longValue() <= zoneCoordinates.getY1().longValue() && rowCoordinates.getY1()
								.longValue() >= zoneCoordinates.getY1().longValue())) {
					firstLineFound = true;
					zonalLineList.add(bottomLineDataCarrier);
				} else {
					if (firstLineFound) {
						break;
					}
				}
			}

			createOutputDataCarrierList(zonalLineList, kVExtraction, valueFoundData, zoneCoordinates);
		}
	}

	/**
	 * Top Zonal Extraction.Method to locate all the words in a top location Rectangle.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List<Span>
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 * 
	 */

	public final void topLocationRectangle(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {

		if (currentLineIndex < 0 || currentLineIndex >= lineOutputDataCarrierList.size()) {
			return;
		}

		if (currentLineIndex - 1 < 0) {
			return;
		}

		Coordinates keyCoordinates = keyCoordinate;

		BigInteger keyCenterX = keyCoordinates.getX1().subtract(keyCoordinates.getX0()).divide(BigInteger.valueOf(2));

		Coordinates zoneCoordinates = new Coordinates();

		BigInteger tempX0 = keyCoordinates.getX0().add(keyCenterX).subtract(BigInteger.valueOf(kVExtraction.getLength() / 2));
		BigInteger tempY0 = keyCoordinates.getY0().subtract(BigInteger.valueOf(kVExtraction.getYoffset() + kVExtraction.getWidth()));

		zoneCoordinates.setX0(tempX0.longValue() < 0 ? BigInteger.ZERO : tempX0);
		zoneCoordinates.setY0(tempY0.longValue() < 0 ? BigInteger.ZERO : tempY0);

		zoneCoordinates.setX1(tempX0.add(BigInteger.valueOf(kVExtraction.getLength())));
		zoneCoordinates.setY1(tempY0.add(BigInteger.valueOf(kVExtraction.getWidth())));
		if (isValidZone(zoneCoordinates)) {
			// finding the lines in the rectangle // traverse downwards in increasing order
			List<LineDataCarrier> zonalLineList = new ArrayList<LineDataCarrier>();
			boolean firstLineFound = false;
			for (int index = currentLineIndex; index >= 0; index--) {
				LineDataCarrier topLineDataCarrier = lineOutputDataCarrierList.get(index);
				if (null == topLineDataCarrier) {
					continue;
				}
				Coordinates rowCoordinates = topLineDataCarrier.getRowCoordinates();
				// Compare the line Y - coordinates that touch the border or lie in that rectangle
				if (rowCoordinates.getY0().longValue() >= zoneCoordinates.getY0().longValue()
						&& rowCoordinates.getY1().longValue() <= zoneCoordinates.getY1().longValue()
						|| (rowCoordinates.getY1().longValue() >= zoneCoordinates.getY0().longValue() && rowCoordinates.getY0()
								.longValue() <= zoneCoordinates.getY0().longValue())
						|| (rowCoordinates.getY0().longValue() <= zoneCoordinates.getY1().longValue() && rowCoordinates.getY1()
								.longValue() >= zoneCoordinates.getY1().longValue())) {
					firstLineFound = true;
					zonalLineList.add(0, topLineDataCarrier);
				} else {
					if (firstLineFound) {
						break;
					}
				}
			}

			createOutputDataCarrierList(zonalLineList, kVExtraction, valueFoundData, zoneCoordinates);
		}
	}

	/**
	 * Top Left Zonal Extraction.Method to locate all the words in a bottom-left location Rectangle.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List<Span>
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 * 
	 */

	public final void topLeftLocationRectangle(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {

		if (currentLineIndex < 0 || currentLineIndex >= lineOutputDataCarrierList.size()) {
			return;
		}

		if (currentLineIndex - 1 < 0) {
			return;
		}

		Coordinates keyCoordinates = keyCoordinate;
		BigInteger keyX0 = keyCoordinates.getX0();
		BigInteger keyY0 = keyCoordinates.getY0();

		Coordinates zoneCoordinates = new Coordinates();

		BigInteger tempX1 = keyX0.subtract(BigInteger.valueOf(kVExtraction.getXoffset()));
		BigInteger tempY1 = keyY0.subtract(BigInteger.valueOf(kVExtraction.getYoffset()));

		BigInteger tempX0 = tempX1.subtract(BigInteger.valueOf(kVExtraction.getLength()));
		BigInteger tempY0 = tempY1.subtract(BigInteger.valueOf(kVExtraction.getWidth()));

		zoneCoordinates.setX1(tempX1.longValue() < 0 ? BigInteger.ZERO : tempX1);
		zoneCoordinates.setY1(tempY1.longValue() < 0 ? BigInteger.ZERO : tempY1);

		zoneCoordinates.setX0(tempX0.longValue() < 0 ? BigInteger.ZERO : tempX0);
		zoneCoordinates.setY0(tempY0.longValue() < 0 ? BigInteger.ZERO : tempY0);
		if (isValidZone(zoneCoordinates)) {
			// finding the lines in the rectangle // traverse downwards in increasing order
			List<LineDataCarrier> zonalLineList = new ArrayList<LineDataCarrier>();
			boolean firstLineFound = false;
			for (int index = currentLineIndex; index >= 0; index--) {
				LineDataCarrier topLineDataCarrier = lineOutputDataCarrierList.get(index);
				if (null == topLineDataCarrier) {
					continue;
				}
				Coordinates rowCoordinates = topLineDataCarrier.getRowCoordinates();
				// Compare the line Y - coordinates that touch the border or lie in that rectangle
				if (rowCoordinates.getY0().longValue() >= zoneCoordinates.getY0().longValue()
						&& rowCoordinates.getY1().longValue() <= zoneCoordinates.getY1().longValue()
						|| (rowCoordinates.getY1().longValue() >= zoneCoordinates.getY0().longValue() && rowCoordinates.getY0()
								.longValue() <= zoneCoordinates.getY0().longValue())
						|| (rowCoordinates.getY0().longValue() <= zoneCoordinates.getY1().longValue() && rowCoordinates.getY1()
								.longValue() >= zoneCoordinates.getY1().longValue())) {
					firstLineFound = true;
					zonalLineList.add(0, topLineDataCarrier);
				} else {
					if (firstLineFound) {
						break;
					}
				}
			}
			createOutputDataCarrierList(zonalLineList, kVExtraction, valueFoundData, zoneCoordinates);
		}
	}

	/**
	 * Top Right Zonal Extraction.Method to locate all the words in a top-right location Rectangle.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List<Span>
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 * 
	 */
	public final void topRightLocationRectangle(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {

		if (currentLineIndex < 0 || currentLineIndex >= lineOutputDataCarrierList.size()) {
			return;
		}

		if (currentLineIndex - 1 < 0) {
			return;
		}

		Coordinates keyCoordinates = keyCoordinate;
		BigInteger keyX1 = keyCoordinates.getX1();
		BigInteger keyY0 = keyCoordinates.getY0();

		BigInteger tempY0 = keyY0.subtract(BigInteger.valueOf(kVExtraction.getYoffset() + kVExtraction.getWidth()));

		Coordinates zoneCoordinates = new Coordinates();
		zoneCoordinates.setX0(keyX1.add(BigInteger.valueOf(kVExtraction.getXoffset())));
		zoneCoordinates.setY0(tempY0.longValue() < 0 ? BigInteger.ZERO : tempY0);

		zoneCoordinates.setX1(zoneCoordinates.getX0().add(BigInteger.valueOf(kVExtraction.getLength())));
		zoneCoordinates.setY1(tempY0.add(BigInteger.valueOf(kVExtraction.getWidth())));
		if (isValidZone(zoneCoordinates)) {
			// finding the lines in the rectangle // traverse downwards in increasing order
			List<LineDataCarrier> zonalLineList = new ArrayList<LineDataCarrier>();
			boolean firstLineFound = false;
			for (int index = currentLineIndex; index >= 0; index--) {
				LineDataCarrier topLineDataCarrier = lineOutputDataCarrierList.get(index);
				if (null == topLineDataCarrier) {
					continue;
				}
				Coordinates rowCoordinates = topLineDataCarrier.getRowCoordinates();
				// Compare the line Y - coordinates that touch the border or lie in that rectangle
				if (rowCoordinates.getY0().longValue() >= zoneCoordinates.getY0().longValue()
						&& rowCoordinates.getY1().longValue() <= zoneCoordinates.getY1().longValue()
						|| (rowCoordinates.getY1().longValue() >= zoneCoordinates.getY0().longValue() && rowCoordinates.getY0()
								.longValue() <= zoneCoordinates.getY0().longValue())
						|| (rowCoordinates.getY0().longValue() <= zoneCoordinates.getY1().longValue() && rowCoordinates.getY1()
								.longValue() >= zoneCoordinates.getY1().longValue())) {
					firstLineFound = true;
					zonalLineList.add(0, topLineDataCarrier);
				} else {
					if (firstLineFound) {
						break;
					}
				}
			}
			createOutputDataCarrierList(zonalLineList, kVExtraction, valueFoundData, zoneCoordinates);
		}
	}

	/**
	 * Left Zonal Extraction.Method to locate all the words in a left location Rectangle.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List<Span>
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 * 
	 */
	public final void leftLocationRectangle(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {

		if (currentLineIndex < 0 || currentLineIndex >= lineOutputDataCarrierList.size()) {
			return;
		}

		Coordinates keyCoordinates = keyCoordinate;

		BigInteger keyCenterY = keyCoordinates.getY1().subtract(keyCoordinates.getY0()).divide(BigInteger.valueOf(2));
		Coordinates zoneCoordinates = new Coordinates();

		BigInteger tempX0 = keyCoordinates.getX0().subtract(BigInteger.valueOf(kVExtraction.getXoffset() + kVExtraction.getLength()));
		BigInteger tempY0 = keyCoordinates.getY0().add(keyCenterY).subtract(BigInteger.valueOf(kVExtraction.getWidth() / 2));

		zoneCoordinates.setX0(tempX0.longValue() < 0 ? BigInteger.ZERO : tempX0);
		zoneCoordinates.setY0(tempY0.longValue() < 0 ? BigInteger.ZERO : tempY0);

		zoneCoordinates.setX1(tempX0.add(BigInteger.valueOf(kVExtraction.getLength())));
		zoneCoordinates.setY1(tempY0.add(BigInteger.valueOf(kVExtraction.getWidth())));
		if (isValidZone(zoneCoordinates)) {
			// finding the lines in the rectangle // traverse upwards in decreasing order
			List<LineDataCarrier> zonalLineList = new ArrayList<LineDataCarrier>();
			boolean firstUpperLineFound = false;
			for (int index = currentLineIndex; index >= 0; index--) {

				LineDataCarrier topLineDataCarrier = lineOutputDataCarrierList.get(index);
				if (null == topLineDataCarrier) {
					continue;
				}
				Coordinates rowCoordinates = topLineDataCarrier.getRowCoordinates();
				// Compare the line Y - coordinates that touch the border or lie in that rectangle
				if (rowCoordinates.getY0().longValue() >= zoneCoordinates.getY0().longValue()
						&& rowCoordinates.getY1().longValue() <= zoneCoordinates.getY1().longValue()
						|| (rowCoordinates.getY1().longValue() >= zoneCoordinates.getY0().longValue() && rowCoordinates.getY0()
								.longValue() <= zoneCoordinates.getY0().longValue())
						|| (rowCoordinates.getY0().longValue() <= zoneCoordinates.getY1().longValue() && rowCoordinates.getY1()
								.longValue() >= zoneCoordinates.getY1().longValue())) {
					firstUpperLineFound = true;
					zonalLineList.add(0, topLineDataCarrier);
				} else {
					if (firstUpperLineFound) {
						break;
					}
				}
			}
			// finding the lines in the rectangle // traverse downwards in increasing order
			boolean firstBottomLineFound = false;
			for (int index = currentLineIndex + 1; index < lineOutputDataCarrierList.size(); index++) {

				LineDataCarrier bottomLineDataCarrier = lineOutputDataCarrierList.get(index);
				if (null == bottomLineDataCarrier) {
					continue;
				}
				Coordinates rowCoordinates = bottomLineDataCarrier.getRowCoordinates();
				// Compare the line Y - coordinates that touch the border or lie in that rectangle
				if (rowCoordinates.getY0().longValue() >= zoneCoordinates.getY0().longValue()
						&& rowCoordinates.getY1().longValue() <= zoneCoordinates.getY1().longValue()
						|| (rowCoordinates.getY1().longValue() >= zoneCoordinates.getY0().longValue() && rowCoordinates.getY0()
								.longValue() <= zoneCoordinates.getY0().longValue())
						|| (rowCoordinates.getY0().longValue() <= zoneCoordinates.getY1().longValue() && rowCoordinates.getY1()
								.longValue() >= zoneCoordinates.getY1().longValue())) {
					firstBottomLineFound = true;
					zonalLineList.add(bottomLineDataCarrier);
				} else {
					if (firstBottomLineFound) {
						break;
					}
				}
			}

			createOutputDataCarrierList(zonalLineList, kVExtraction, valueFoundData, zoneCoordinates);
		}
	}

	/**
	 * Right Zonal Extraction.Method to locate all the words in a right location Rectangle.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List<Span>
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 * 
	 */
	public final void rightLocationRectangle(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {

		if (currentLineIndex < 0 || currentLineIndex >= lineOutputDataCarrierList.size()) {
			return;
		}

		Coordinates keyCoordinates = keyCoordinate;

		BigInteger keyCenterY = keyCoordinates.getY1().subtract(keyCoordinates.getY0()).divide(BigInteger.valueOf(2));
		Coordinates zoneCoordinates = new Coordinates();

		BigInteger tempY0 = keyCoordinates.getY0().add(keyCenterY).subtract(BigInteger.valueOf(kVExtraction.getWidth() / 2));

		zoneCoordinates.setX0(keyCoordinates.getX1().add(BigInteger.valueOf(kVExtraction.getXoffset())));
		zoneCoordinates.setY0(tempY0.longValue() < 0 ? BigInteger.ZERO : tempY0);

		zoneCoordinates.setX1(zoneCoordinates.getX0().add(BigInteger.valueOf(kVExtraction.getLength())));
		zoneCoordinates.setY1(tempY0.add(BigInteger.valueOf(kVExtraction.getWidth())));

		if (isValidZone(zoneCoordinates)) {

			// finding the lines in the rectangle // traverse upwards in decreasing order
			List<LineDataCarrier> zonalLineList = new ArrayList<LineDataCarrier>();
			boolean firstUpperLineFound = false;
			for (int index = currentLineIndex; index >= 0; index--) {
				LineDataCarrier topLineDataCarrier = lineOutputDataCarrierList.get(index);
				if (null == topLineDataCarrier) {
					continue;
				}
				Coordinates rowCoordinates = topLineDataCarrier.getRowCoordinates();
				// Compare the line Y - coordinates that touch the border or lie in that rectangle
				if (rowCoordinates.getY0().longValue() >= zoneCoordinates.getY0().longValue()
						&& rowCoordinates.getY1().longValue() <= zoneCoordinates.getY1().longValue()
						|| (rowCoordinates.getY1().longValue() >= zoneCoordinates.getY0().longValue() && rowCoordinates.getY0()
								.longValue() <= zoneCoordinates.getY0().longValue())
						|| (rowCoordinates.getY0().longValue() <= zoneCoordinates.getY1().longValue() && rowCoordinates.getY1()
								.longValue() >= zoneCoordinates.getY1().longValue())) {
					firstUpperLineFound = true;
					zonalLineList.add(0, topLineDataCarrier);
				} else {
					if (firstUpperLineFound) {
						break;
					}
				}
			}

			// finding the lines in the rectangle // traverse downwards in increasing order
			boolean firstBottomLineFound = false;
			for (int index = currentLineIndex + 1; index < lineOutputDataCarrierList.size(); index++) {
				LineDataCarrier bottomLineDataCarrier = lineOutputDataCarrierList.get(index);
				if (null == bottomLineDataCarrier) {
					continue;
				}
				Coordinates rowCoordinates = bottomLineDataCarrier.getRowCoordinates();
				// Compare the line Y - coordinates that touch the border or lie in that rectangle
				if (rowCoordinates.getY0().longValue() >= zoneCoordinates.getY0().longValue()
						&& rowCoordinates.getY1().longValue() <= zoneCoordinates.getY1().longValue()
						|| rowCoordinates.getY1().longValue() >= zoneCoordinates.getY0().longValue()
						|| rowCoordinates.getY0().longValue() <= zoneCoordinates.getY0().longValue()) {
					firstBottomLineFound = true;
					zonalLineList.add(bottomLineDataCarrier);
				} else {
					if (firstBottomLineFound) {
						break;
					}
				}
			}

			createOutputDataCarrierList(zonalLineList, kVExtraction, valueFoundData, zoneCoordinates);
		}
	}

	/**
	 * Method to locate the values in a zone on the basis of fetch Value
	 * 
	 * @param zonalLineList
	 * @param kvExtraction
	 * @param valueFoundData
	 * @param zoneCoordinates
	 */
	private void createOutputDataCarrierList(List<LineDataCarrier> zonalLineList, final InputDataCarrier kvExtraction,
			CustomList valueFoundData, final Coordinates zoneCoordinates) throws DCMAApplicationException {
		OutputDataCarrier lastDataCarrier = null;
		OutputDataCarrier dataCarrier = null;
		for (Iterator<LineDataCarrier> iterator = zonalLineList.iterator(); iterator.hasNext();) {
			LineDataCarrier lineDataCarrier = (LineDataCarrier) iterator.next();
			List<Span> eachLineSpan = lineDataCarrier.getSpanList();
			if (null == eachLineSpan) {
				continue;
			}
			for (Span span : eachLineSpan) {

				// Basis of fetchValue : ALL , FIRST , LAST
				switch (kvExtraction.getFetchValue()) {
					case ALL:
						dataCarrier = getOutputDataCarrierSpan(kvExtraction, valueFoundData, zoneCoordinates, span);
						break;
					case LAST:
						OutputDataCarrier tempLastDataCarrier = getOutputDataCarrierSpan(kvExtraction, valueFoundData,
								zoneCoordinates, span);
						if (tempLastDataCarrier != null) {
							lastDataCarrier = tempLastDataCarrier;
						}
						break;
					case FIRST:
						if (valueFoundData.isEmpty()) {
							dataCarrier = getOutputDataCarrierSpan(kvExtraction, valueFoundData, zoneCoordinates, span);
						}
						break;
					default:
						break;
				}
				if (dataCarrier != null) {
					valueFoundData.add(dataCarrier);
				}
			}
		}
		if (lastDataCarrier != null) {
			valueFoundData.add(lastDataCarrier);
		}
		if (valueFoundData != null && !valueFoundData.isEmpty() && valueFoundData.size() > 1) {
			List<OutputDataCarrier> sortedList = getSortedList(valueFoundData);
			concatenateList(valueFoundData, sortedList);
		}
	}

	private void concatenateList(CustomList valueFoundData, List<OutputDataCarrier> outputDataCarriers) {
		StringBuffer valueList = new StringBuffer();
		Coordinates coordinates = new Coordinates();
		float confidence = 0;
		for (int index = 0; index < outputDataCarriers.size(); index++) {
			OutputDataCarrier outputDataCarrier = outputDataCarriers.get(index);
			Span span = outputDataCarrier.getSpan();
			if (null != span && span.getCoordinates() != null) {
				if (index == 0) {
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
	 * Method to determine whether the zone is valid with correct co-ordinates
	 * 
	 * @param zoneCoordinates
	 * @return
	 */
	private boolean isValidZone(Coordinates zoneCoordinates) {
		if (zoneCoordinates.getX0().longValue() <= 0 && zoneCoordinates.getX1().longValue() <= 0
				&& zoneCoordinates.getY0().longValue() <= 0 && zoneCoordinates.getY1().longValue() <= 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @param kVExtraction
	 * @param valueFoundData
	 * @param zoneCoordinates
	 * @param span
	 * @return
	 */
	private OutputDataCarrier getOutputDataCarrierSpan(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final Coordinates zoneCoordinates, final Span span) throws DCMAApplicationException {

		Coordinates valueCoordinates = span.getCoordinates();
		OutputDataCarrier dataCarrier = null;
		// Compare each value X - coordinates that touch the border or lie in that rectangle
		if (valueCoordinates.getX0().longValue() >= zoneCoordinates.getX0().longValue()
				&& valueCoordinates.getX1().longValue() <= zoneCoordinates.getX1().longValue()
				|| (valueCoordinates.getX1().longValue() >= zoneCoordinates.getX0().longValue() && valueCoordinates.getX0()
						.longValue() <= zoneCoordinates.getX0().longValue())
				|| (valueCoordinates.getX0().longValue() <= zoneCoordinates.getX1().longValue() && valueCoordinates.getX1()
						.longValue() >= zoneCoordinates.getX1().longValue())) {
			// apply the value pattern
			dataCarrier = findPattern(span, kVExtraction.getValuePattern());
			if (dataCarrier != null) {
				if (kVExtraction.getMultiplier() != null && kVExtraction.getMultiplier() > 0) {
					dataCarrier.setConfidence(dataCarrier.getConfidence() * kVExtraction.getMultiplier());
				}
			}
		}
		return dataCarrier;
	}

	private List<OutputDataCarrier> getSortedList(CustomList valueFoundData) {

		List<OutputDataCarrier> list = valueFoundData.getList();
		Collections.sort(list, new Comparator<OutputDataCarrier>() {

			public int compare(final OutputDataCarrier o1, final OutputDataCarrier o2) {
				Span firstSpan = o1.getSpan();
				Span secSpan = o2.getSpan();
				BigInteger s1Y1 = firstSpan.getCoordinates().getY1();
				BigInteger s2Y1 = secSpan.getCoordinates().getY1();
				int returnValue = 0;
				int compare = s1Y1.intValue() - s2Y1.intValue();
				int defaultValue = 20;
				try {
					defaultValue = Integer.parseInt(getWidthOfLine());
				} catch (NumberFormatException nfe) {
					LOGGER.error(nfe.getMessage(), nfe);
					defaultValue = 20;
				}
				if (compare >= -defaultValue && compare <= defaultValue) {
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
}
