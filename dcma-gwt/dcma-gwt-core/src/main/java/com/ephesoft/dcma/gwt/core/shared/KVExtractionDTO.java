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

package com.ephesoft.dcma.gwt.core.shared;

import com.ephesoft.dcma.core.common.KVFetchValue;
import com.ephesoft.dcma.core.common.KVPageValue;
import com.ephesoft.dcma.core.common.LocationType;
import com.google.gwt.user.client.rpc.IsSerializable;

public class KVExtractionDTO implements IsSerializable {

	private FieldTypeDTO fieldTypeDTO;

	private boolean useExistingKey;

	private String keyPattern;

	private String valuePattern;

	private LocationType locationType;

	private String identifier;

	private boolean deleted;

	private boolean newKV;

	private Integer noOfWords;

	private KVPageValue kvPageValue;

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
	 * Advanced Key Value extraction.
	 */
	private AdvancedKVExtractionDTO advancedKVExtractionDTO;

	public FieldTypeDTO getFieldTypeDTO() {
		return fieldTypeDTO;
	}

	public void setFieldTypeDTO(FieldTypeDTO fieldTypeDTO) {
		this.fieldTypeDTO = fieldTypeDTO;
	}

	public String getKeyPattern() {
		return keyPattern;
	}

	public void setKeyPattern(String keyPattern) {
		this.keyPattern = keyPattern;
	}

	public String getValuePattern() {
		return valuePattern;
	}

	public void setValuePattern(String valuePattern) {
		this.valuePattern = valuePattern;
	}

	public LocationType getLocationType() {
		return locationType;
	}

	public void setLocationType(LocationType locationType) {
		this.locationType = locationType;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public boolean isNew() {
		return newKV;
	}

	public void setNew(boolean newKV) {
		this.newKV = newKV;
	}

	/**
	 * @return the noOfWords
	 */
	public Integer getNoOfWords() {
		if (null == noOfWords) {
			noOfWords = 0;
		}
		return noOfWords;
	}

	/**
	 * @param noOfWords the noOfWords to set
	 */
	public void setNoOfWords(Integer noOfWords) {
		Integer noOFWords = noOfWords;
		if (null == noOfWords) {
			noOFWords = 0;
		}
		this.noOfWords = noOFWords;
	}

	public Float getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(Float multiplier) {
		this.multiplier = multiplier;
	}

	public KVFetchValue getFetchValue() {
		return fetchValue;
	}

	public void setFetchValue(KVFetchValue fetchValue) {
		this.fetchValue = fetchValue;
	}

	public Integer getLength() {
		if (null == length) {
			length = 0;
		}
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getWidth() {
		if (null == width) {
			width = 0;
		}
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getXoffset() {
		if (null == xoffset) {
			xoffset = 0;
		}
		return xoffset;
	}

	public void setXoffset(Integer xoffset) {
		this.xoffset = xoffset;
	}

	public Integer getYoffset() {
		if (null == yoffset) {
			yoffset = 0;
		}
		return yoffset;
	}

	public void setYoffset(Integer yoffset) {
		this.yoffset = yoffset;
	}

	public boolean isSimpleKVExtraction() {
		boolean returnVal = true;
		if (width != null && width != 0 && length != null && length != 0) {
			returnVal = false;
		}
		return returnVal;
	}

	public AdvancedKVExtractionDTO getAdvancedKVExtractionDTO() {
		return advancedKVExtractionDTO;
	}

	public void setAdvancedKVExtractionDTO(AdvancedKVExtractionDTO advancedKVExtractionDTO) {
		this.advancedKVExtractionDTO = advancedKVExtractionDTO;
	}

	public KVPageValue getKvPageValue() {
		return kvPageValue;
	}

	public void setKvPageValue(KVPageValue kvPageValue) {
		this.kvPageValue = kvPageValue;
	}

	public boolean isUseExistingKey() {
		return useExistingKey;
	}

	public void setUseExistingKey(boolean useExistingKey) {
		this.useExistingKey = useExistingKey;
	}
}
