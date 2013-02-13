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

package com.ephesoft.dcma.kvfinder.data;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;

/**
 * This data structure is used to carry output data value, confidence and span tag of hocr pages.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.kvfinder.service.KVFinderServiceImpl
 * @see com.ephesoft.dcma.kvfinder.LocationFinder
 */
public class OutputDataCarrier {

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
	 * @param span {@link Span>}
	 * @param confidence float
	 * @param value {@link String>}
	 */
	public OutputDataCarrier(final Span span, final float confidence, final String value) {
		super();
		this.span = span;
		this.confidence = confidence;
		this.value = value;
	}

	/**
	 * Compare a given OutputDataCarrier with this object. If confidence of this object is greater than the received object, then this
	 * object is greater than the other. As we have to finder larger confidence score value we will return -1 for this case.
	 * 
	 * @param outputDataCarrier {@link OutputDataCarrier)
	 * @return int
	 */
	public int compareTo(final OutputDataCarrier outputDataCarrier) {

		int returnValue = BatchConstants.ZERO;
		final float diffConfidence = this.getConfidence() - outputDataCarrier.getConfidence();

		if (diffConfidence > BatchConstants.ZERO) {
			returnValue = - BatchConstants.ONE;
		}
		if (diffConfidence < BatchConstants.ZERO) {
			returnValue = BatchConstants.ONE;
		}

		return returnValue;
	}

	/**
	 * @return the span
	 */
	public final Span getSpan() {
		return span;
	}

	/**
	 * @return the confidence
	 */
	public final float getConfidence() {
		return confidence;
	}

	/**
	 * @return the value
	 */
	public final String getValue() {
		return value;
	}

	/**
	 * @param span the span to set
	 */
	public final void setSpan(final Span span) {
		this.span = span;
	}

	/**
	 * @param confidence the confidence to set
	 */
	public final void setConfidence(final float confidence) {
		this.confidence = confidence;
	}

	/**
	 * @param value the value to set
	 */
	public final void setValue(final String value) {
		this.value = value;
	}

}
