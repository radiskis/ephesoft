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

import com.ephesoft.dcma.batch.schema.Coordinates;

/**
 * This class is used to carry key properties for zonal key extraction.
 * 
 * @author Epheosft
 * @version 1.0
 * @see com.ephesoft.dcma.batch.schema.Coordinates
 */
public class ZonalKeyParameters {

	/**
	 * Line index where key is found.
	 */
	private int lineIndex;

	/**
	 * Coordinates of the Key found.
	 */
	private Coordinates keyCoordinates;

	/**
	 * Key pattern.
	 */
	private String keyPattern;

	/**
	 * Distance of the key extracted from the zone drawn by user.
	 */
	private double distanceFromZone;

	/**
	 * Constructor.
	 * 
	 * @param lineIndex int
	 * @param keyCoordinate {@link Coordinates}
	 * @param keyPattern {@link String}
	 */
	public ZonalKeyParameters(final int lineIndex, final Coordinates keyCoordinates, final String keyPattern) {
		this.lineIndex = lineIndex;
		this.keyCoordinates = keyCoordinates;
		this.keyPattern = keyPattern;
	}

	/**
	 * To get Line Index.
	 * @return int
	 */
	public int getLineIndex() {
		return lineIndex;
	}

	/**
	 * To set Line Index.
	 * @param lineIndex int
	 */
	public void setLineIndex(final int lineIndex) {
		this.lineIndex = lineIndex;
	}

	/**
	 * To get Key Coordinates.
	 * @return Coordinates
	 */
	public Coordinates getKeyCoordinates() {
		return keyCoordinates;
	}

	/**
	 * To set Key Coordinates.
	 * @param keyCoordinates Coordinates
	 */ 
	public void setKeyCoordinates(final Coordinates keyCoordinates) {
		this.keyCoordinates = keyCoordinates;
	}

	/**
	 * To get Key Pattern.
	 * @return String
	 */
	public String getKeyPattern() {
		return keyPattern;
	}

	/**
	 * To set Key Pattern.
	 * @param keyPattern String
	 */
	public void setKeyPattern(final String keyPattern) {
		this.keyPattern = keyPattern;
	}

	/**
	 * To get Distance from Zone.
	 * @return double
	 */
	public double getDistanceFromZone() {
		return distanceFromZone;
	}

	/**
	 * To set Distance from Zone.
	 * @param distanceFromZone2 double
	 */
	public void setDistanceFromZone(final double distanceFromZone2) {
		this.distanceFromZone = distanceFromZone2;
	}

}
