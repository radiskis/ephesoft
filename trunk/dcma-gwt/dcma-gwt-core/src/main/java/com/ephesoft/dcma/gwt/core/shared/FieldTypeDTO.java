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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.core.common.LocationType;
import com.google.gwt.user.client.rpc.IsSerializable;

public class FieldTypeDTO implements IsSerializable {

	private DocumentTypeDTO docTypeDTO;

	private DataType dataType;

	private String identifier;

	private String name;

	private String description;

	private String pattern;

	private boolean deleted;

	private boolean newField;

	private String fieldOrderNumber;

	private String sampleValue;

	private String barcodeType;

	private String fieldOptionValueList;

	private List<String> regexPatternList;

	private boolean multiLine;

	private List<KVExtractionDTO> kvExtractionList = new ArrayList<KVExtractionDTO>();

	private List<RegexDTO> regexList = new ArrayList<RegexDTO>();
	
	private boolean hidden;
	/**
	 * Field is set to true if document level field is readonly.
	 */
	private boolean isReadOnly;

	public DocumentTypeDTO getDocTypeDTO() {
		return docTypeDTO;
	}

	public void setDocTypeDTO(DocumentTypeDTO docTypeDTO) {
		this.docTypeDTO = docTypeDTO;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getSampleValue() {
		return sampleValue;
	}

	public void setSampleValue(String sampleValue) {
		this.sampleValue = sampleValue;
	}

	public List<String> getRegexPatternList() {
		return regexPatternList;
	}

	public void setRegexPatternList(List<String> regexPatternList) {
		this.regexPatternList = regexPatternList;
	}

	public void setRegexList(List<RegexDTO> regexList) {
		this.regexList = regexList;
	}

	public List<KVExtractionDTO> getKvExtractionList(boolean includeDeleted) {
		List<KVExtractionDTO> kvExtractionDTOs;
		if (includeDeleted){
			kvExtractionDTOs = kvExtractionList;
		}else{
			kvExtractionDTOs = getKvExtractionList();
		}
		return kvExtractionDTOs;
	}

	public List<KVExtractionDTO> getKvExtractionList() {
		List<KVExtractionDTO> kvExtractionDTOs = new LinkedList<KVExtractionDTO>();
		for (KVExtractionDTO kvExtractionDTO : kvExtractionList) {
			if (!(kvExtractionDTO.isDeleted())) {
				kvExtractionDTOs.add(kvExtractionDTO);
			}
		}
		return kvExtractionDTOs;
	}

	public List<RegexDTO> getRegexList(boolean includeDeleted) {
		List<RegexDTO> regexDTOs;
		if (includeDeleted){
			regexDTOs = regexList;
		}else{
			regexDTOs = getRegexList();
		}
		return regexDTOs;
	}

	public List<RegexDTO> getRegexList() {
		List<RegexDTO> regexDTOs = new LinkedList<RegexDTO>();
		for (RegexDTO regexDTO : regexList) {
			if (!(regexDTO.isDeleted())) {
				regexDTOs.add(regexDTO);
			}
		}
		return regexDTOs;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public boolean isNew() {
		return newField;
	}

	public void setNew(boolean newField) {
		this.newField = newField;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public void addKvExtraction(KVExtractionDTO kvExtractionDTO) {
		kvExtractionList.add(kvExtractionDTO);
	}

	public void removeKvExtraction(KVExtractionDTO kvExtractionDTO) {
		this.kvExtractionList.remove(kvExtractionDTO);
	}

	public void setFieldOrderNumber(String fieldOrderNumber) {
		this.fieldOrderNumber = fieldOrderNumber;
	}

	public String getFieldOrderNumber() {
		return fieldOrderNumber;
	}

	public KVExtractionDTO getKVExtractionByKeyAndDataTypeAndLocation(String keyPattern, String valuePattern, LocationType locationType) {
		KVExtractionDTO kvExtractionDTO = null;
		Collection<KVExtractionDTO> dtos = kvExtractionList;
		for (KVExtractionDTO dto : dtos) {
			if (dto.getKeyPattern() != null && dto.getKeyPattern().equals(keyPattern)
					&& (dto.getValuePattern() != null && dto.getValuePattern().equals(valuePattern))
					&& (dto.getLocationType() != null && dto.getLocationType().name().equals(locationType.name()))) {

				kvExtractionDTO = dto;
			}
		}
		return kvExtractionDTO;
	}

	public boolean checkKVExtractionDetails(String keyPattern, String valuePattern, LocationType locationType) {
		boolean valid = false;
		if (getKVExtractionByKeyAndDataTypeAndLocation(keyPattern, valuePattern, locationType) != null){
			valid = true;
		}
		return valid;
	}

	public KVExtractionDTO getKVExtractionDTOByIdentifier(String identifier) {
		KVExtractionDTO kvExtractionDTO = null;
		for (KVExtractionDTO extractionDTO : kvExtractionList) {
			if (extractionDTO.getIdentifier().equals(identifier)){
				kvExtractionDTO= extractionDTO;
			}
		}
		return kvExtractionDTO;
	}

	public boolean checkRegex(String pattern) {
		boolean valid = false;
		for (RegexDTO dto : regexList) {
			if (dto.getPattern().equals(pattern)){
				valid = true;
			}
		}
		return valid;
	}

	public void addRegex(RegexDTO regexDTO) {
		regexList.add(regexDTO);
	}

	public RegexDTO getRegexDTOByIdentifier(String identifier) {
		RegexDTO regexDTO = null;
		for (RegexDTO dto : regexList) {
			if (dto.getIdentifier().equals(identifier)){
				regexDTO = dto;
			}
		}
		return regexDTO;
	}

	public RegexDTO getRegexDTOByPattern(String pattern) {
		RegexDTO regexDTO = null;
		for (RegexDTO dto : regexList) {
			if (dto.getPattern().equals(pattern)){
				regexDTO = dto;
			}
		}
		return regexDTO;
	}

	public String getBarcodeType() {
		return barcodeType;
	}

	public void setBarcodeType(String barcodeType) {
		this.barcodeType = barcodeType;
	}

	public String getFieldOptionValueList() {
		return fieldOptionValueList;
	}

	public void setFieldOptionValueList(String fieldOptionValueList) {
		this.fieldOptionValueList = fieldOptionValueList;
	}

	
	public boolean isHidden() {
		return hidden;
	}

	
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public void setMultiLine(boolean multiLine) {
		this.multiLine = multiLine;
	}

	public boolean isMultiLine() {
		return multiLine;
	}

	/**
	 * API to set the 'isReadOnly' field
	 * 
	 * @param isReadOnly the isReadOnly to set
	 */
	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	/**
	 * API to get the 'isReadOnly' field
	 * 
	 * @return the isReadOnly
	 */
	public boolean getIsReadOnly() {
		return isReadOnly;
	}

}
