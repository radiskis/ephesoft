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

import com.ephesoft.dcma.core.common.LocationType;
import com.google.gwt.user.client.rpc.IsSerializable;

public class KVPageProcessDTO implements IsSerializable {

	private static final long serialVersionUID = 1L;

	private BatchClassPluginConfigDTO batchClassPluginConfigDTO;

	private String keyPattern;

	private String valuePattern;

	private LocationType locationType;

	private long identifier;

	private boolean isDeleted;

	private boolean isNew;

	private Integer noOfWords;
	
	private String pageLevelFieldName;

	/*public KVPageProcessDTO() {
	}*/

	/**
	 * @return the batchClassPluginConfig
	 */
	public BatchClassPluginConfigDTO getBatchClassPluginConfigDTO() {
		return batchClassPluginConfigDTO;
	}

	/**
	 * @param batchClassPluginConfig the batchClassPluginConfig to set
	 */
	public void setBatchClassPluginConfigDTO(BatchClassPluginConfigDTO batchClassPluginConfigDTO) {
		this.batchClassPluginConfigDTO = batchClassPluginConfigDTO;
	}

	/**
	 * @return the keyPattern
	 */
	public String getKeyPattern() {
		return keyPattern;
	}

	/**
	 * @param keyPattern the keyPattern to set
	 */
	public void setKeyPattern(String keyPattern) {
		this.keyPattern = keyPattern;
	}

	/**
	 * @return the valuePattern
	 */
	public String getValuePattern() {
		return valuePattern;
	}

	/**
	 * @param valuePattern the valuePattern to set
	 */
	public void setValuePattern(String valuePattern) {
		this.valuePattern = valuePattern;
	}

	/**
	 * @return the locationType
	 */
	public LocationType getLocationType() {
		return locationType;
	}

	/**
	 * @param locationType the locationType to set
	 */
	public void setLocationType(LocationType locationType) {
		this.locationType = locationType;
	}

	public boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}

	public long getIdentifier() {
		return identifier;
	}

	public void setIdentifier(long identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the noOfWords
	 */
	public Integer getNoOfWords() {
		if(null == noOfWords){
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

	
	/**
	 * @return the description
	 */
	public String getPageLevelFieldName() {
		return pageLevelFieldName;
	}

	
	/**
	 * @param description the description to set
	 */
	public void setPageLevelFieldName(String pageLevelFieldName) {
		this.pageLevelFieldName = pageLevelFieldName;
	}
	
	

}
