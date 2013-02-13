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
import javax.persistence.Table;

import com.ephesoft.dcma.core.common.LocationType;
import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;
import com.ephesoft.dcma.da.constant.DataAccessConstant;

/**
 * Entity class for kv_page_process.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.model.common.AbstractChangeableEntity
 */
@Entity
@Table(name = "kv_page_process")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class KVPageProcess extends AbstractChangeableEntity implements Serializable {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = 1L;

	/** 
	 * batchClassPluginConfig {@link BatchClassPluginConfig}.
	 */ 
	@ManyToOne
	@JoinColumn(name = "batch_class_plugin_config_id")
	private BatchClassPluginConfig batchClassPluginConfig;

	/**
	 * keyPattern String.
	 */
	@Column(name = "key_pattern", nullable = false)
	private String keyPattern;

	/**
	 * valuePattern String.
	 */ 
	@Column(name = "value_pattern", nullable = false)
	private String valuePattern;

	/**
	 * locationType LocationType.
	 */
	@Column(name = "location", nullable = false)
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
	 * multiplier Integer.
	 */
	@Column(name = "multiplier")
	private Integer multiplier;
	
	/**
	 * pageLevelFieldName String.
	 */
	@Column(name = "page_level_field_name", nullable = false, columnDefinition = "varchar(100) default 'KV_Page_Process'")
	private String pageLevelFieldName;
	
	/**
	 * To get Batch Class Plugin Config.
	 * @return the batchClassPluginConfig
	 */
	public BatchClassPluginConfig getBatchClassPluginConfig() {
		return batchClassPluginConfig;
	}

	/**
	 * To set Batch Class Plugin Config.
	 * @param batchClassPluginConfig BatchClassPluginConfig
	 */
	public void setBatchClassPluginConfig(BatchClassPluginConfig batchClassPluginConfig) {
		this.batchClassPluginConfig = batchClassPluginConfig;
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
	 *  To set Value Pattern.
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
	 * To get distance.
	 * @return the distance
	 */
	public String getDistance() {
		return distance;
	}
	
	/**
	 * To set distance.
	 * @param distance String
	 */
	public void setDistance(String distance) {
		this.distance = distance;
	}
	
	/**
	 * To get Multiplier.
	 * @return the multiplier
	 */
	public Integer getMultiplier() {
		return multiplier;
	}
	
	/**
	 * To set Multiplier.
	 * @param multiplier Integer
	 */
	public void setMultiplier(Integer multiplier) {
		this.multiplier = multiplier;
	}
	
	/**
	 * To get Page Level Field Name.
	 * @return the description
	 */
	public String getPageLevelFieldName() {
		if(null == this.pageLevelFieldName){
			this.pageLevelFieldName = DataAccessConstant.KV_PAGE_PROCESS;
		}
		return pageLevelFieldName;
	}
	
	/**
	 * To set Page Level Field Name.
	 * @param description the description to set
	 */
	public void setPageLevelFieldName(String pageLevelFieldName) {
		if(null == pageLevelFieldName){
			this.pageLevelFieldName = DataAccessConstant.KV_PAGE_PROCESS;
		} else {
			this.pageLevelFieldName = pageLevelFieldName;
		}
	}
}
