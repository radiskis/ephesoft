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

package com.ephesoft.dcma.da.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ephesoft.dcma.core.common.KVFetchValue;
import com.ephesoft.dcma.core.common.KVPageValue;
import com.ephesoft.dcma.core.common.LocationType;
import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;

/**
 * Entity class for kv_extraction.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.model.common.AbstractChangeableEntity
 */
@Entity
@Table(name = "kv_extraction")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class KVExtraction extends AbstractChangeableEntity implements Serializable {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * fieldType FieldType.
	 */
	@ManyToOne
	@JoinColumn(name = "field_type_id")
	private FieldType fieldType;

	/**
	 * useExistingKey boolean.
	 */
	@Column(name = "use_existing_key", columnDefinition = "bit default 0")
	private boolean useExistingKey;

	/**
	 * keyPattern String.
	 */
	@Column(name = "key_pattern", nullable = false)
	private String keyPattern;

	/**
	 * valuePattern String.
	 */
	@Column(name = "value_pattern")
	private String valuePattern;

	/**
	 * locationType LocationType.
	 */
	@Column(name = "location")
	@Enumerated(EnumType.STRING)
	private LocationType locationType;

	/**
	 * noOfWords Integer.
	 */
	@Column(name = "no_of_words")
	private Integer noOfWords;

	/**
	 * distance String.
	 */
	@Column(name = "distance")
	private String distance;

	/**
	 * multiplier Float.
	 */
	@Column(name = "multiplier")
	private Float multiplier;

	/**
	 * fetchValue KVFetchValue.
	 */
	@Column(name = "fetch_value")
	@Enumerated(EnumType.STRING)
	private KVFetchValue fetchValue;

	/**
	 * pageValue KVPageValue.
	 */
	@Column(name = "page_value")
	@Enumerated(EnumType.STRING)
	private KVPageValue pageValue;

	/**
	 * length Integer.
	 */
	@Column(name = "length")
	private Integer length;

	/**
	 * width Integer.
	 */
	@Column(name = "width")
	private Integer width;

	/**
	 * x-offset Integer.
	 */
	@Column(name = "x_offset")
	private Integer xoffset;

	/**
	 * y-offset Integer.
	 */
	@Column(name = "y_offset")
	private Integer yoffset;

	/**
	 * advancedKVExtraction AdvancedKVExtraction.
	 */
	@OneToOne
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "advanced_kv_extraction_id")
	private AdvancedKVExtraction advancedKVExtraction;

	/**
	 * To get Field Type.
	 * @return the fieldType
	 */
	public FieldType getFieldType() {
		return fieldType;
	}

	/**
	 * To set Field Type.
	 * @param fieldType FieldType
	 */
	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}

	/**
	 * To get Key Pattern.
	 * @return the keyPattern
	 */
	public String getKeyPattern() {
		return keyPattern;
	}

	/**
	 * To set Key Pattern.
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
	 * To get Location Type.
	 * @return the locationType
	 */
	public LocationType getLocationType() {
		return locationType;
	}

	/**
	 * To set Location Type.
	 * @param locationType LocationType
	 */
	public void setLocationType(LocationType locationType) {
		this.locationType = locationType;
	}

	/**
	 * To get No. of Words.
	 * @return the noOfWords
	 */
	public Integer getNoOfWords() {
		return noOfWords;
	}

	/**
	 * To set No. of Words.
	 * @param noOfWords Integer
	 */
	public void setNoOfWords(Integer noOfWords) {
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
	public void setDistance(String distance) {
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
	public void setMultiplier(Float multiplier) {
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
	public void setFetchValue(KVFetchValue fetchValue) {
		this.fetchValue = fetchValue;
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
	public void setLength(Integer length) {
		this.length = length;
	}

	/**
	 * To get Width.
	 * @return Integer
	 */
	public Integer getWidth() {
		return width;
	}

	/**
	 * To set Width.
	 * @param width Integer
	 */
	public void setWidth(Integer width) {
		this.width = width;
	}

	/**
	 * To get X-offset.
	 * @return Integer
	 */
	public Integer getXoffset() {
		return xoffset;
	}

	/**
	 * To set X-offset.
	 * @param xoffset Integer
	 */ 
	public void setXoffset(Integer xoffset) {
		this.xoffset = xoffset;
	}

	/**
	 * To get Y-offset.
	 * @return Integer
	 */
	public Integer getYoffset() {
		return yoffset;
	}

	/**
	 * To get Page Value.
	 * @return KVPageValue
	 */
	public KVPageValue getPageValue() {
		return pageValue;
	}

	/**
	 * To set Page Value.
	 * @param pageValue KVPageValue
	 */
	public void setPageValue(KVPageValue pageValue) {
		this.pageValue = pageValue;
	}

	/**
	 * To set Y-offset.
	 * @param yoffset Integer
	 */
	public void setYoffset(Integer yoffset) {
		this.yoffset = yoffset;
	}

	/**
	 * To get Advanced KV Extraction.
	 * @return AdvancedKVExtraction
	 */
	public AdvancedKVExtraction getAdvancedKVExtraction() {
		return advancedKVExtraction;
	}

	/**
	 * To set Advanced KV Extraction.
	 * @param advancedKVExtraction AdvancedKVExtraction
	 */
	public void setAdvancedKVExtraction(AdvancedKVExtraction advancedKVExtraction) {
		this.advancedKVExtraction = advancedKVExtraction;
	}

	/**
	 * Returns true or false depending on UseExistingKey.
	 * @return boolean
	 */
	public boolean isUseExistingKey() {
		return useExistingKey;
	}

	/**
	 * To set UseExistingKey.
	 * @param useExistingKey boolean
	 */
	public void setUseExistingKey(boolean useExistingKey) {
		this.useExistingKey = useExistingKey;
	}

}
