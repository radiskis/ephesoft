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

package com.ephesoft.dcma.tablefinder.data;

import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.tablefinder.constants.TableExtractionConstants;

/**
 * This class is used to carry the span, confidence and value objects for extraction.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.kvextraction.KeyValueExtraction
 */
public class DataCarrier implements Comparable<DataCarrier> {

	/**
	 * Span tag information.
	 */
	private Span span;

	/**
	 * Confidence information.
	 */
	private float confidence;

	/**
	 * Value information.
	 */
	private String value;

	/**
	 * Constructor.
	 * 
	 * @param span {@link Span}
	 * @param confidence {@link float}
	 * @param value {@link String}
	 */
	public DataCarrier(final Span span, final float confidence, final String value) {
		super();
		this.span = span;
		this.confidence = confidence;
		this.value = value;
	}

	/**
	 * Compare a given DataCarrier with this object. If confidence of this object is greater than the received object, then this object
	 * is greater than the other. As we have to finder larger confidence score value we will return -1 for this case.
	 * 
	 * @param dataCarrier {@link DataCarrier}
	 * @return int
	 */
	public int compareTo(final DataCarrier dataCarrier) {

		int returnValue = TableExtractionConstants.ZERO;

		final float diffConfidence = this.getConfidence() - dataCarrier.getConfidence();

		if (diffConfidence > TableExtractionConstants.ZERO) {
			returnValue = -TableExtractionConstants.ONE;
		}
		if (diffConfidence < TableExtractionConstants.ZERO) {
			returnValue = TableExtractionConstants.ONE;
		}

		return returnValue;
	}

	/**
	 * To get span.
	 * @return Span
	 */
	public final Span getSpan() {
		return span;
	}

	/**
	 * To get confidence.
	 * @return float the confidence
	 */
	public final float getConfidence() {
		return confidence;
	}

	/**
	 * To get value.
	 * @return String the value
	 */
	public final String getValue() {
		return value;
	}

	/**
	 * To set span.
	 * @param span {@link Span} 
	 */
	public final void setSpan(final Span span) {
		this.span = span;
	}

	/**
	 * To set confidence.
	 * @param confidence float 
	 */
	public final void setConfidence(final float confidence) {
		this.confidence = confidence;
	}

	/**
	 * To set value.
	 * @param value {@link String} 
	 */
	public final void setValue(final String value) {
		this.value = value;
	}

}
