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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;

/**
 * Util class to perform any operation on HOCR content.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans
 */
public class HocrUtil {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HocrUtil.class);

	/**
	 * API to get the line data carrier.
	 * 
	 * @param spans {@link Spans}
	 * @param pageID {@link String}
	 * @param widthOfLine int
	 * @return List<LineDataCarrier>
	 */
	public static List<LineDataCarrier> getLineDataCarrierList(final Spans spans, final String pageID, final int widthOfLine) {
		LOGGER.info("Entering method getLineDataCarrierList...");
		List<Span> mainSpanList = null;
		final List<LineDataCarrier> lineDataCarriers = new ArrayList<LineDataCarrier>();
		if (spans != null) {
			mainSpanList = spans.getSpan();
			if (null != mainSpanList && !mainSpanList.isEmpty()) {
				LOGGER.debug("width of line = " + widthOfLine);

				final Set<Span> set = new TreeSet<Span>(new Comparator<Span>() {

					public int compare(final Span firstSpan, final Span secSpan) {
						final BigInteger s1Y1 = firstSpan.getCoordinates().getY1();
						final BigInteger s2Y1 = secSpan.getCoordinates().getY1();
						int returnValue = BatchConstants.ZERO;
						final int compare = s1Y1.intValue() - s2Y1.intValue();
						if (compare >= -widthOfLine && compare <= widthOfLine) {
							final BigInteger s1X1 = firstSpan.getCoordinates().getX1();
							final BigInteger s2X1 = secSpan.getCoordinates().getX1();
							returnValue = s1X1.compareTo(s2X1);
						} else {
							returnValue = s1Y1.compareTo(s2Y1);
						}
						return returnValue;
					}
				});

				set.addAll(mainSpanList);

				LineDataCarrier lineDataCarrier = new LineDataCarrier(pageID);
				lineDataCarriers.add(lineDataCarrier);
				List<Span> spanList = lineDataCarrier.getSpanList();

				for (final Span span : set) {
					if (spanList.isEmpty()) {
						spanList.add(span);
					} else {
						final Span lastSpan = spanList.get(spanList.size() - BatchConstants.ONE);
						final int compare = lastSpan.getCoordinates().getY1().intValue() - span.getCoordinates().getY1().intValue();
						if (compare >= -widthOfLine && compare <= widthOfLine) {
							spanList.add(span);
						} else {
							lineDataCarrier = new LineDataCarrier(pageID);
							lineDataCarriers.add(lineDataCarrier);
							spanList = lineDataCarrier.getSpanList();
							spanList.add(span);
						}
					}
				}
			}
		}
		LOGGER.info("Exiting method getLineDataCarrierList...");
		return lineDataCarriers;
	}

	/**
	 * API to get valid rows from rows list with respect to the rectangular zone coordinates.
	 * 
	 * @param lineDataCarrierList {@link List<{@link LineDataCarrier}>}
	 * @param zoneCoordinates {@link Coordinates}
	 * @return List<LineDataCarrier>
	 */
	public static List<LineDataCarrier> getValidRowsForZone(final List<LineDataCarrier> lineDataCarrierList,
			final Coordinates zoneCoordinates) {
		final List<LineDataCarrier> validLineDataCarrierList = new ArrayList<LineDataCarrier>();
		if (zoneCoordinates != null && lineDataCarrierList != null && !lineDataCarrierList.isEmpty()) {
			for (final LineDataCarrier lineDataCarrier : lineDataCarrierList) {
				final Coordinates rowCoordinates = lineDataCarrier.getRowCoordinates();
				if (rowCoordinates != null && isValidRowForZone(rowCoordinates, zoneCoordinates)) {
					validLineDataCarrierList.add(lineDataCarrier);
				}
			}
		}
		return validLineDataCarrierList;
	}


	/**
	 * API to check whether row is valid with respect to the rectangular zone coordinates.
	 * 
	 * @param rowCoordinate {@link Coordinates}
	 * @param zoneCoordinates {@link Coordinates}
	 * @return boolean
	 */
	public static boolean isValidRowForZone(final Coordinates rowCoordinate, final Coordinates zoneCoordinate) {
		boolean isValidRow = false;
		if (rowCoordinate != null && zoneCoordinate != null) {
			final int rowY0 = rowCoordinate.getY0().intValue();
			final int zoneY0 = zoneCoordinate.getY0().intValue();
			final int rowY1 = rowCoordinate.getY1().intValue();
			final int zoneY1 = zoneCoordinate.getY1().intValue();
			if ((rowY0 >= zoneY0 && rowY0 <= zoneY1) || (rowY1 >= zoneY0 && rowY1 <= zoneY1) || (rowY0 <= zoneY0 && rowY1 >= zoneY1)) {
				isValidRow = true;
			}
		}
		return isValidRow;
	}

	/**
	 * API to get the rectangular coordinates for the coordinates list passed.
	 * 
	 * @param coordinatesList {@link List<{@link Coordinates}>}
	 * @return Coordinates
	 */
	public static Coordinates getRectangleCoordinates(final List<Coordinates> coordinatesList) {
		final Coordinates rectCoordinate = new Coordinates();
		BigInteger minX0 = BigInteger.ZERO;
		BigInteger minY0 = BigInteger.ZERO;
		BigInteger maxX1 = BigInteger.ZERO;
		BigInteger maxY1 = BigInteger.ZERO;
		boolean isFirst = true;

		for (final Coordinates coordinates : coordinatesList) {
			final BigInteger hocrX0 = coordinates.getX0();
			final BigInteger hocrY0 = coordinates.getY0();
			final BigInteger hocrX1 = coordinates.getX1();
			final BigInteger hocrY1 = coordinates.getY1();
			if (isFirst) {
				minX0 = hocrX0;
				minY0 = hocrY0;
				maxX1 = hocrX1;
				maxY1 = hocrY1;
				isFirst = false;
			} else {
				if (hocrX0.compareTo(minX0) < BatchConstants.ZERO) {
					minX0 = hocrX0;
				}
				if (hocrY0.compareTo(minY0) < BatchConstants.ZERO) {
					minY0 = hocrY0;
				}
				if (hocrX1.compareTo(maxX1) > BatchConstants.ZERO) {
					maxX1 = hocrX1;
				}
				if (hocrY1.compareTo(maxY1) > BatchConstants.ZERO) {
					maxY1 = hocrY1;
				}
			}
		}

		rectCoordinate.setX0(minX0);
		rectCoordinate.setX1(maxX1);
		rectCoordinate.setY0(minY0);
		rectCoordinate.setY1(maxY1);
		return rectCoordinate;
	}

	/**
	 * Api to calculate the distance from zone.
	 * 
	 * @param coordinate {@link Coordinates}
	 * @param zoneCoordinates {@link Coordinates}
	 * @return double
	 */
	public static double calculateDistanceFromZone(final Coordinates coordinate, final Coordinates zoneCoordinates) {
		double distance = 0.0;
		LOGGER.info("Entering method calculateDistanceFromZone........");
		if (coordinate != null && zoneCoordinates != null) {
			BigInteger coordX0 = coordinate.getX0();
			BigInteger coordY0 = coordinate.getY0();
			BigInteger zoneX0 = zoneCoordinates.getX0();
			BigInteger zoneY0 = zoneCoordinates.getY0();
			if (coordX0 != null && coordY0 != null && zoneX0 != null && zoneY0 != null) {
				double xDistance = coordX0.subtract(zoneX0).doubleValue();
				double yDistance = coordY0.subtract(zoneY0).doubleValue();
				distance = Math.sqrt(Math.pow(xDistance, BatchConstants.TWO) + Math.pow(yDistance, BatchConstants.TWO));
			}
			if (isInsideZone(coordinate, zoneCoordinates)) {
				distance = -distance;
			}
		}
		LOGGER.info("Exiting method calculateDistanceFromZone........");
		return distance;
	}

	/**
	 * An api to check if pointer is inside zone.
	 * @param coordinate {@link Coordinates}
	 * @param zoneCoordinates {@link Coordinates}
	 * @return boolean
	 */
	public static boolean isInsideZone(final Coordinates coordinate, final Coordinates zoneCoordinates) {
		boolean isInsideZone = false;
		if (coordinate != null && zoneCoordinates != null) {
			long coordX0 = coordinate.getX0().longValue();
			long coordY0 = coordinate.getY0().longValue();
			long coordX1 = coordinate.getX1().longValue();
			long coordY1 = coordinate.getY1().longValue();
			long zoneX0 = zoneCoordinates.getX0().longValue();
			long zoneY0 = zoneCoordinates.getY0().longValue();
			long zoneX1 = zoneCoordinates.getX1().longValue();
			long zoneY1 = zoneCoordinates.getY1().longValue();
			if (((coordX1 >= zoneX0 && coordX1 <= zoneX1) || (coordX0 >= zoneX0 && coordX0 <= zoneX1))
					&& ((coordY1 <= zoneY1 && coordY1 >= zoneY0) || (coordY0 <= zoneY1 && coordY0 >= zoneY0))) {
				isInsideZone = true;
			} else if (((zoneX0 <= coordX0 && zoneX1 >= coordX0) || (zoneX0 >= coordX1 && zoneX1 <= coordX1))
					&& ((zoneY0 >= coordY0 && zoneY0 <= coordY1) || (zoneY1 >= coordY0 && zoneY1 <= coordY1))
					|| ((zoneY0 <= coordY0 && zoneY1 >= coordY0) || (zoneY0 >= coordY1 && zoneY1 <= coordY1))
					&& ((zoneX0 >= coordX0 && zoneX0 <= coordX1) || (zoneX1 >= coordX0 && zoneX1 <= coordX1))) {
				isInsideZone = true;
			} else if (((zoneX0 > coordX0 && zoneX0 < coordX1) || (zoneX1 > coordX0 && zoneX1 < coordX1))
					&& ((zoneY0 > coordY0 && zoneY0 < coordY1) || (zoneY1 > coordY0 && zoneY1 < coordY1))) {
				isInsideZone = true;
			}
		}
		return isInsideZone;
	}
}
