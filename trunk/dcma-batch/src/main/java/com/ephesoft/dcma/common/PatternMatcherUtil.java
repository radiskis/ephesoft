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

package com.ephesoft.dcma.common;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.kvfinder.KVFinderConstants;
import com.ephesoft.dcma.kvfinder.data.OutputDataCarrier;

/**
 * This is util class for pattern matching.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span
 */
public class PatternMatcherUtil {

	/**
	 * DEFAULT_CONFIDENCE_SCORE int.
	 */
	private static final int DEFAULT_CONFIDENCE_SCORE = 100;
	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PatternMatcherUtil.class);

	/**
	 * API is responsible for finding the patterns and returned the found data List.
	 * 
	 * @param lineDataCarrier {@link LineDataCarrier>}
	 * @param patternStr {@link String>}
	 * @param confidenceScore {@link String>}
	 * @return List<OutputDataCarrier>
	 * @throws DCMAApplicationException if input pattern sequence is invalid
	 */
	public static final List<OutputDataCarrier> findPattern(final LineDataCarrier lineDataCarrier, final String patternStr,
			final String confidenceScore) throws DCMAApplicationException {

		String errMsg = null;
		List<OutputDataCarrier> dataCarrierList = new ArrayList<OutputDataCarrier>(0);
		if (lineDataCarrier != null) {
			final CharSequence inputStr = lineDataCarrier.getLineRowData();
			final List<Span> spanList = lineDataCarrier.getSpanList();
			if (null == inputStr || KVFinderConstants.EMPTY.equals(inputStr)) {
				LOGGER.error(errMsg);
			} else {
				if (null == patternStr || KVFinderConstants.EMPTY.equals(patternStr)) {
					LOGGER.error("Invalid input pattern sequence.");
					// throw new DCMAApplicationException(errMsg);
				} else {
					int confidenceInt = DEFAULT_CONFIDENCE_SCORE;
					try {
						confidenceInt = Integer.parseInt(confidenceScore);
					} catch (NumberFormatException nfe) {
						LOGGER.error(nfe.getMessage(), nfe);
					}
					// Compile and use regular expression
					final Pattern pattern = Pattern.compile(patternStr);
					final Matcher matcher = pattern.matcher(inputStr);
					while (matcher.find()) {
						// Get all groups for this match
						for (int i = BatchConstants.ZERO; i <= matcher.groupCount(); i++) {
							final String groupStr = matcher.group(i);
							final int startIndex = matcher.start();
							final Span matchedSpan = getMatchedSpan(spanList, startIndex);
							if (groupStr != null && matchedSpan != null) {
								final float confidence = (groupStr.length() * confidenceInt) / inputStr.length();
								OutputDataCarrier dataCarrier = new OutputDataCarrier(matchedSpan, confidence, groupStr);
								dataCarrierList.add(dataCarrier);
								LOGGER.info(groupStr);
							}
						}
					}
				}
			}
		}
		return dataCarrierList;
	}

	/**
	 * Method is responsible for finding the patterns and returned the found data List.
	 * 
	 * @param span {@link Span>}
	 * @param patternStr {@link String>}
	 * @param confidenceScore String
	 * @return OutputDataCarrier
	 * @throws DCMAApplicationException Check for all the input parameters and find the pattern.
	 */
	public final OutputDataCarrier findPattern(final Span span, final String patternStr, final String confidenceScore)
			throws DCMAApplicationException {

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
				// throw new DCMAApplicationException(errMsg);
			} else {
				int confidenceInt = DEFAULT_CONFIDENCE_SCORE;
				try {
					confidenceInt = Integer.parseInt(confidenceScore);
				} catch (NumberFormatException nfe) {
					LOGGER.error(nfe.getMessage(), nfe);
				}
				// Compile and use regular expression
				final Pattern pattern = Pattern.compile(patternStr);
				final Matcher matcher = pattern.matcher(inputStr);
				// boolean matchFound = matcher.find();
				while (matcher.find()) {
					// Get all groups for this match
					for (int i = BatchConstants.ZERO; i <= matcher.groupCount(); i++) {
						final String groupStr = matcher.group(i);

						if (groupStr != null) {
							final float confidence = (groupStr.length() * confidenceInt) / inputStr.length();
							dataCarrier = new OutputDataCarrier(span, confidence, groupStr);
							LOGGER.info(groupStr);
						}
					}
				}
			}
		}

		return dataCarrier;
	}

	/**
	 * Api to return the first occourance of matched span.
	 *  
	 * @param spanList {@link List<{@link Span>}>}
	 * @param startIndex int
	 * @return Span
	 */
	private static Span getMatchedSpan(final List<Span> spanList, final int startIndex) {
		int spanIndex = BatchConstants.ZERO;
		boolean isFirstSpan = Boolean.TRUE;
		Span matchedSpan = null;
		for (Span span : spanList) {
			if (null != span && null != span.getValue()) {
				spanIndex = spanIndex + span.getValue().length();
				if (!isFirstSpan) {
					spanIndex = spanIndex + BatchConstants.ONE;
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
}
