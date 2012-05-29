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

package com.ephesoft.dcma.tablefinder.data;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;

public class LineDataCarrier {

	private List<Span> spanList;

	private String pageID;

	/**
	 * Constructor.
	 * 
	 */
	public LineDataCarrier(final List<Span> spanList, final String pageID) {
		super();
		this.spanList = spanList;
		this.pageID = pageID;
	}

	/**
	 * Constructor.
	 * 
	 */
	public LineDataCarrier(final String pageID) {
		super();
		this.spanList = new ArrayList<Span>();
		this.pageID = pageID;
	}

	public List<Span> getSpanList() {
		return spanList;
	}

	public void setSpanList(List<Span> spanList) {
		this.spanList = spanList;
	}

	public String getPageID() {
		return pageID;
	}

	public void setPageID(String pageID) {
		this.pageID = pageID;
	}

	/**
	 * API to get the row data.
	 * 
	 * @return
	 */
	public String getLineRowData() {
		StringBuilder rowData = new StringBuilder();
		if (this.spanList != null && !this.spanList.isEmpty()) {
			for (Span span : this.spanList) {
				if (null != span) {
					String value = span.getValue();
					if (null != value && !value.isEmpty()) {
						rowData.append(value);
						rowData.append(" ");
					}
				}
			}
		}

		return rowData.toString();
	}

	/**
	 * API to the index list of all searchValues found in the current row.
	 * 
	 * @param searchValue
	 * @return
	 */
	public List<Integer> getIndexOfSpan(String searchValue) {
		List<Integer> indexList = null;
		if (searchValue != null) {
			if (this.spanList != null && !this.spanList.isEmpty()) {
				int local = 0;
				for (Span span : this.spanList) {
					if (null != span) {
						String value = span.getValue();
						if (null != value && !value.isEmpty()) {
							if (value.contains(searchValue)) {
								if (null == indexList) {
									indexList = new ArrayList<Integer>();
								}
								indexList.add(local);
							}
						}
					}
					local++;
				}
			}

		}

		return indexList;
	}

	/**
	 * API to get the span at passed index in the current row.
	 * 
	 * @param currentIndex
	 * @return
	 */
	public Span getCurrentSpan(int currentIndex) {
		Span span = null;
		if (null != this.spanList && !this.spanList.isEmpty()) {
			int length = this.spanList.size();
			if (currentIndex >= 0 && currentIndex < length) {
				span = this.spanList.get(currentIndex);
			}
		}
		return span;
	}

	/**
	 * API to get the span at left of passed index in the current row.
	 * 
	 * @param currentIndex
	 * @return
	 */
	public Span getLeftSpan(int currentIndex) {
		Span span = null;
		if (null != this.spanList && !this.spanList.isEmpty()) {
			int length = this.spanList.size();
			if (currentIndex - 1 >= 0 && currentIndex - 1 < length) {
				span = this.spanList.get(currentIndex - 1);
			}
		}
		return span;
	}

	/**
	 * API to get the span at right of passed index in the current row.
	 * 
	 * @param currentIndex
	 * @return
	 */
	public Span getRightSpan(int currentIndex) {
		Span span = null;
		if (null != this.spanList && !this.spanList.isEmpty()) {
			int length = this.spanList.size();
			if (currentIndex + 1 >= 0 && currentIndex + 1 < length) {
				span = this.spanList.get(currentIndex + 1);
			}
		}
		return span;
	}

	/**
	 * API to get the index of passed {@link Span} in the current row.
	 * 
	 * @param span
	 * @return
	 */
	public int getIndexOfSpan(final Span span) {
		Integer indexOf = null;
		if (this.spanList != null) {
			for (Span currentSpan : this.spanList) {
				if (currentSpan != null) {
					if (compareSpans(currentSpan, span)) {
						indexOf = spanList.indexOf(currentSpan);
						break;
					}
				}
			}
		}
		return indexOf;
	}

	/**
	 * API to return the span at right of the passed span. Returns null if span doesn't exists at right.
	 * 
	 * @param span
	 * @return
	 */
	public Span getRightSpan(final Span span) {
		Span rightSpan = null;
		if (this.spanList != null) {
			for (Span currentSpan : this.spanList) {
				if (currentSpan != null) {
					int indexOf = spanList.indexOf(currentSpan);
					if (indexOf < spanList.size() - 1 && compareSpans(currentSpan, span)) {
						rightSpan = spanList.get(indexOf + 1);
						break;
					}
				}
			}
		}
		return rightSpan;
	}

	/**
	 * API to return the span at left of the passed span. Returns null if span doesn't exists at left.
	 * 
	 * @param span
	 * @return
	 */
	public Span getLeftSpan(final Span span) {
		Span leftSpan = null;
		if (this.spanList != null) {
			for (Span currentSpan : this.spanList) {
				if (currentSpan != null) {
					int indexOf = spanList.indexOf(currentSpan);
					if (indexOf > 0 && compareSpans(currentSpan, span)) {
						leftSpan = spanList.get(indexOf - 1);
						break;
					}
				}
			}
		}
		return leftSpan;
	}

	/**
	 * API to match two spans. Returns true if they match otherwise false.
	 * 
	 * @param currentSpan {@link Span}
	 * @param span {@link Span}
	 * @return
	 */
	public boolean compareSpans(Span currentSpan, Span span) {
		boolean spanMatched = false;

		BigInteger currSpanX0 = currentSpan.getCoordinates().getX0();
		BigInteger currSpanX1 = currentSpan.getCoordinates().getX1();
		BigInteger currSpanY0 = currentSpan.getCoordinates().getY0();
		BigInteger currSpanY1 = currentSpan.getCoordinates().getY0();

		BigInteger spanX0 = span.getCoordinates().getX0();
		BigInteger spanX1 = span.getCoordinates().getX1();
		BigInteger spanY0 = span.getCoordinates().getY0();
		BigInteger spanY1 = span.getCoordinates().getY0();

		if (spanX0.compareTo(currSpanX0) == 0 && spanX1.compareTo(currSpanX1) == 0 && spanY0.compareTo(currSpanY0) == 0
				&& spanY1.compareTo(currSpanY1) == 0) {
			spanMatched = true;
		}
		return spanMatched;
	}

	/**
	 * API to get the row coordinates of current row.
	 * 
	 * @return
	 */
	public Coordinates getRowCoordinates() {

		Coordinates coordinates = new Coordinates();
		BigInteger minX0 = BigInteger.ZERO;
		BigInteger minY0 = BigInteger.ZERO;
		BigInteger maxX1 = BigInteger.ZERO;
		BigInteger maxY1 = BigInteger.ZERO;

		boolean isFirst = true;

		if (this.spanList != null && !this.spanList.isEmpty()) {
			for (Span span : this.spanList) {
				if (null != span) {
					Coordinates hocrCoordinates = span.getCoordinates();
					BigInteger hocrX0 = hocrCoordinates.getX0();
					BigInteger hocrY0 = hocrCoordinates.getY0();
					BigInteger hocrX1 = hocrCoordinates.getX1();
					BigInteger hocrY1 = hocrCoordinates.getY1();
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
			}
		}

		coordinates.setX0(minX0);
		coordinates.setX1(maxX1);
		coordinates.setY0(minY0);
		coordinates.setY1(maxY1);

		return coordinates;
	}

}
