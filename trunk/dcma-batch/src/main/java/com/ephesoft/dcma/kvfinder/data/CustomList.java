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

import java.math.BigInteger;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;

/**
 * This class is to add and get OutputDataCarrier objects to and from the list.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public class CustomList extends TreeSet<OutputDataCarrier> {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * maxResult int.
	 */
	private final int maxResult;

	/**
	 * Constructor.
	 * 
	 * @param maxResult int
	 */
	public CustomList(final int maxResult) {
		super(new CustomComparator());
		this.maxResult = maxResult;
	}

	/**
	 * To add new objects.
	 * 
	 * @param opDataCarrier {@link OutputDataCarrier}
	 * @return boolean
	 */
	@Override
	public boolean add(final OutputDataCarrier opDataCarrier) {
		boolean returnValue = true;
		if (maxResult == BatchConstants.ZERO) {
			returnValue = super.add(opDataCarrier);
		} else {
			if (super.size() == maxResult) {
				if (super.add(opDataCarrier)) {
					if (super.pollFirst() != null) {
						returnValue = true;
					} else {
						returnValue = false;
					}
				} else {
					final Iterator<OutputDataCarrier> itr = super.iterator();
					while (itr.hasNext()) {
						final OutputDataCarrier outputDataCarrier = itr.next();

						if (outputDataCarrier.equals(opDataCarrier)
								&& opDataCarrier.getValue().compareTo(outputDataCarrier.getValue()) > BatchConstants.ZERO) {
							super.remove(outputDataCarrier);
							super.add(opDataCarrier);
							returnValue = true;
							break;
						}
					}
				}
			} else {
				returnValue = super.add(opDataCarrier);
			}
		}
		return returnValue;
	}

	/**
	 * Method to get output data carrier list in ascending order.
	 * 
	 * @return List<OutputDataCarrier>
	 */
	public List<OutputDataCarrier> getAscendingList() {
		List<OutputDataCarrier> list = new LinkedList<OutputDataCarrier>(this);
		return list;
	}

	/**
	 * To get descending order list.
	 * 
	 * @return List<OutputDataCarrier>
	 */
	public List<OutputDataCarrier> getList() {
		final List<OutputDataCarrier> list = new LinkedList<OutputDataCarrier>();
		list.addAll(super.descendingSet());
		return list;
	}
}

/**
 * This is custom comparator class.
 * 
 * @author Ephesoft
 * @version 1.0
 */
final class CustomComparator implements Comparator<OutputDataCarrier> {

	/**
	 * This is override compare method to compare two objects.
	 * 
	 * @param outputDataCarrier1 {@link OutputDataCarrier}
	 * @param outputDataCarrier2 {@link OutputDataCarrier}
	 * @return int
	 */
	@Override
	public int compare(final OutputDataCarrier outputDataCarrier1, final OutputDataCarrier outputDataCarrier2) {
		int returnVal = BatchConstants.ZERO;
		if (outputDataCarrier1 != null && outputDataCarrier2 != null) {
			returnVal = Float.valueOf(outputDataCarrier1.getConfidence()).compareTo(Float.valueOf(outputDataCarrier2.getConfidence()));
			if (returnVal == BatchConstants.ZERO) {
				returnVal = outputDataCarrier1.getValue().compareTo(outputDataCarrier2.getValue());
				if (returnVal == BatchConstants.ZERO) {
					final Span firstSpan = outputDataCarrier1.getSpan();
					final Span secSpan = outputDataCarrier2.getSpan();
					if (firstSpan != null && secSpan != null && firstSpan.getCoordinates() != null && secSpan.getCoordinates() != null) {
						final BigInteger s1Y1 = firstSpan.getCoordinates().getY1();
						final BigInteger s2Y1 = secSpan.getCoordinates().getY1();
						if (s1Y1 != null && s2Y1 != null) {
							returnVal = s1Y1.compareTo(s2Y1);
							if (returnVal == BatchConstants.ZERO) {
								final BigInteger s1X1 = firstSpan.getCoordinates().getX1();
								final BigInteger s2X1 = secSpan.getCoordinates().getX1();
								returnVal = compareCoordinateInfo(returnVal, firstSpan, secSpan, s1X1, s2X1);
							}
						}
					}
				}
			}
		}
		return returnVal;
	}

	private int compareCoordinateInfo(int returnVal, final Span firstSpan, final Span secSpan, final BigInteger s1X1,
			final BigInteger s2X1) {
		int localReturnVal = returnVal;
		if (s1X1 != null && s2X1 != null) {
			localReturnVal = s1X1.compareTo(s2X1);
			if (localReturnVal == BatchConstants.ZERO) {
				final BigInteger s1Y0 = firstSpan.getCoordinates().getY0();
				final BigInteger s2Y0 = secSpan.getCoordinates().getY0();
				if (s1Y0 != null && s2Y0 != null) {
					localReturnVal = s1Y0.compareTo(s2Y0);
					if (localReturnVal == BatchConstants.ZERO) {
						final BigInteger s1X0 = firstSpan.getCoordinates().getX0();
						final BigInteger s2X0 = secSpan.getCoordinates().getX0();
						if (s1X0 != null && s2X0 != null) {
							localReturnVal = s1X0.compareTo(s2X0);
						}
					}
				}
			}
		}
		return localReturnVal;
	}
}
