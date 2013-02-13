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
import com.ephesoft.dcma.core.common.KVFetchValue;
import com.ephesoft.dcma.core.common.LocationType;

/**
 * This data structure is used to carry input data key, value and location type.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.kvfinder.service.KVFinderServiceImpl
 * @see com.ephesoft.dcma.kvfinder.LocationFinder
 */
public class InputDataCarrier {

	/**
	 * Location type.
	 */
	private LocationType locationType;

	/**
	 * Key pattern.
	 */
	private String keyPattern;

	/**
	 * Value pattern.
	 */
	private String valuePattern;

	/**
	 * noOfWords.
	 */
	private Integer noOfWords;

	/**
	 * distance.
	 */
	private String distance;

	/**
	 * multiplier.
	 */
	private Float multiplier;

	/**
	 * fetchValue.
	 */
	private KVFetchValue fetchValue;

	/**
	 * length.
	 */
	private Integer length;

	/**
	 * width.
	 */
	private Integer width;

	/**
	 * x-offset.
	 */
	private Integer xoffset;

	/**
	 * y-offset.
	 */
	private Integer yoffset;

	/**
	 * use existing key.
	 */
	private boolean useExistingField;

	/**
	 * Coordinates of key rectangle drawn by user at UI.
	 */
	private Coordinates keyRectangleCoordinates;

	/**
	 * Coordinates of value rectangle drawn by user at UI.
	 */
	private Coordinates valueRectangleCoordinates;

	/**
	 * Constructor.
	 * 
	 * @param locationType {@link LocationType}
	 * @param keyPattern String
	 * @param valuePattern String
	 * @param noOfWords Integer
	 */
	public InputDataCarrier(LocationType locationType, String keyPattern, String valuePattern, Integer noOfWords) {
		super();
		this.locationType = locationType;
		this.keyPattern = keyPattern;
		this.valuePattern = valuePattern;
		this.noOfWords = noOfWords;
	}

	/**
	 * Constructor.
	 * 
	 * @param locationType {@link LocationType}
	 * @param keyPattern String
	 * @param valuePattern String 
	 * @param noOfWords Integer
	 * @param multiplier Float
	 * @param fetchValue {@link KVFetchValue}
	 * @param length Integer
	 * @param width Integer
	 * @param xoffset Integer
	 * @param yoffset Integer
	 * @param useExistingField boolean
	 * @param keyRectangleCoordinates Coordinates
	 * @param valueRectangleCoordinates Coordinates
	 */
	public InputDataCarrier(LocationType locationType, String keyPattern, String valuePattern, Integer noOfWords, Float multiplier,
			KVFetchValue fetchValue, Integer length, Integer width, Integer xoffset, Integer yoffset, boolean useExistingField,
			Coordinates keyRectangleCoordinates, Coordinates valueRectangleCoordinates) {
		super();
		this.locationType = locationType;
		this.keyPattern = keyPattern;
		this.valuePattern = valuePattern;
		this.noOfWords = noOfWords;

		this.multiplier = multiplier;
		this.fetchValue = fetchValue;
		this.length = length;
		this.width = width;
		this.xoffset = xoffset;
		this.yoffset = yoffset;
		this.useExistingField = useExistingField;

		this.keyRectangleCoordinates = keyRectangleCoordinates;
		this.valueRectangleCoordinates = valueRectangleCoordinates;

	}

	/**
	 * To get location type.
	 * @return the locationType
	 */
	public LocationType getLocationType() {
		return locationType;
	}

	/**
	 * To set location type.
	 * @param locationType {@link LocationType}
	 */
	public void setLocationType(LocationType locationType) {
		this.locationType = locationType;
	}

	/**
	 * To get Key Pattern.
	 * @return the keyPattern
	 */
	public String getKeyPattern() {
		return keyPattern;
	}

	/**
	 *  To set Key Pattern.
	 * @param keyPattern String
	 */
	public void setKeyPattern(String keyPattern) {
		this.keyPattern = keyPattern;
	}

	/**
	 * To get Value Pattern.
	 * @return the valuePattern
	 */
	public String getValuePattern() {
		return valuePattern;
	}

	/**
	 * To set Value Pattern.
	 * @param valuePattern String
	 */
	public void setValuePattern(String valuePattern) {
		this.valuePattern = valuePattern;
	}

	/**
	 * To get no. of words.
	 * @return the noOfWords
	 */
	public Integer getNoOfWords() {
		return noOfWords;
	}

	/**
	 * To set no. of words.
	 * @param noOfWords Integer
	 */
	public void setNoOfWords(final Integer noOfWords) {
		this.noOfWords = noOfWords;
	}

	/**
	 * To get Distance.
	 * @return the distance
	 */
	public String getDistance() {
		return distance;
	}

	/**
	 * To set Distance.
	 * @param distance String
	 */
	public void setDistance(final String distance) {
		this.distance = distance;
	}

	/**
	 * To get Multiplier.
	 * @return the multiplier
	 */
	public Float getMultiplier() {
		return multiplier;
	}

	/**
	 * To set Multiplier.
	 * @param multiplier Float
	 */
	public void setMultiplier(final Float multiplier) {
		this.multiplier = multiplier;
	}

	/**
	 * To get Fetch Value.
	 * @return KVFetchValue
	 */
	public KVFetchValue getFetchValue() {
		return fetchValue;
	}

	/**
	 * To set Fetch Value.
	 * @param fetchValue KVFetchValue
	 */
	public void setFetchValue(final KVFetchValue fetchValue) {
		this.fetchValue = fetchValue;
	}

	/**
	 * To get width.
	 * @return Integer
	 */
	public Integer getWidth() {
		return width;
	}

	/**
	 * To set width.
	 * @param width Integer
	 */
	public void setWidth(final Integer width) {
		this.width = width;
	}

	/**
	 * To get Length.
	 * @return Integer
	 */
	public Integer getLength() {
		return length;
	}

	/**
	 * To set Length.
	 * @param length Integer
	 */
	public void setLength(final Integer length) {
		this.length = length;
	}

	/**
	 * To get Xoffset.
	 * @return Integer
	 */
	public Integer getXoffset() {
		return xoffset;
	}

	/**
	 * To set Xoffset.
	 * @param xoffset Integer
	 */
	public void setXoffset(final Integer xoffset) {
		this.xoffset = xoffset;
	}

	/**
	 * To get Yoffset.
	 * @return Integer
	 */
	public Integer getYoffset() {
		return yoffset;
	}

	/**
	 * To set Yoffset.
	 * @param yoffset Integer
	 */
	public void setYoffset(final Integer yoffset) {
		this.yoffset = yoffset;
	}

	/**
	 * To check whether use field exists or not.
	 * @return boolean
	 */
	public boolean isUseExistingField() {
		return useExistingField;
	}

	/**
	 * To set use existing field.
	 * @param useExistingField boolean
	 */
	public void setUseExistingField(final boolean useExistingField) {
		this.useExistingField = useExistingField;
	}

	/**
	 * To get Key Rectangle Coordinates.
	 * @return Coordinates
	 */
	public Coordinates getKeyRectangleCoordinates() {
		return keyRectangleCoordinates;
	}

	/**
	 * To set Key Rectangle Coordinates.
	 * @param keyRectangleCoordinates Coordinates
	 */
	public void setKeyRectangleCoordinates(final Coordinates keyRectangleCoordinates) {
		this.keyRectangleCoordinates = keyRectangleCoordinates;
	}

	/**
	 * To get Value Rectangle Coordinates.
	 * @return Coordinates
	 */
	public Coordinates getValueRectangleCoordinates() {
		return valueRectangleCoordinates;
	}

	/**
	 * To set Value Rectangle Coordinates.
	 * @param valueRectangleCoordinates Coordinates
	 */
	public void setValueRectangleCoordinates(final Coordinates valueRectangleCoordinates) {
		this.valueRectangleCoordinates = valueRectangleCoordinates;
	}

}
