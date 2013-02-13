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
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ephesoft.dcma.core.EphesoftProperty;
import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;

/**
 * Entity class for field_type.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.model.common.AbstractChangeableEntity
 */
@Entity
@Table(name = "field_type")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class FieldType extends AbstractChangeableEntity implements Serializable {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * docType DocumentType.
	 */
	@ManyToOne
	@JoinColumn(name = "document_type_id")
	private DocumentType docType;

	/**
	 * dataType DataType.
	 */
	@Column(name = "field_data_type")
	@Enumerated(EnumType.STRING)
	private DataType dataType;

	/**
	 * identifier String.
	 */ 
	@Column(name = "identifier")
	private String identifier;

	/**
	 * name String.
	 */
	@Column(name = "field_type_name")
	private String name;

	/**
	 * fieldOrderNumber int.
	 */
	@Column(name = "field_order_number", nullable = false)
	private int fieldOrderNumber;

	/**
	 * description String.
	 */
	@Column(name = "field_type_description")
	private String description;

	/**
	 * pattern String.
	 */
	@Column(name = "pattern")
	private String pattern;

	/**
	 * sampleValue String.
	 */
	@Column(name = "sample_value")
	private String sampleValue;

	/**
	 * barcodeType String.
	 */
	@Column(name = "barcode_type")
	private String barcodeType;

	/**
	 * fieldOptionValueList String.
	 */
	@Column(name = "field_option_value_list")
	private String fieldOptionValueList;

	/**
	 * hidden boolean.
	 */
	@Column(name = "is_hidden", columnDefinition = "bit default 0")
	private boolean hidden;

	/**
	 * multiLine boolean.
	 */
	@Column(name = "is_multi_line", columnDefinition = "bit default 0")
	private boolean multiLine;

	/**
	 * readOnly boolean.
	 */
	@Column(name = "is_read_only", columnDefinition = "bit default 0")
	private boolean readOnly;

	/**
	 * kvExtraction List<KVExtraction>.
	 */
	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "field_type_id")
	private List<KVExtraction> kvExtraction = new ArrayList<KVExtraction>();;

	/**
	 * regexValidation List<RegexValidation>.
	 */
	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "field_type_id")
	private List<RegexValidation> regexValidation = new ArrayList<RegexValidation>();

	/**
	 * To get Doc Type.
	 * @return DocumentType
	 */
	public DocumentType getDocType() {
		return docType;
	}

	/**
	 * To set Doc Type.
	 * @param docType DocumentType
	 */
	public void setDocType(DocumentType docType) {
		this.docType = docType;
	}

	/**
	 * To get Data Type.
	 * @return DataType
	 */
	public DataType getDataType() {
		return dataType;
	}

	/**
	 * To set Data Type.
	 * @param dataType DataType
	 */
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	/**
	 * To get name.
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * To set name.
	 * @param name String
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * To get Description.
	 * @return String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * To set Description.
	 * @param description String
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * To get Pattern.
	 * @return String
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * To set Pattern.
	 * @param pattern String
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * To get Kv Extraction.
	 * @return List<KVExtraction>
	 */
	public List<KVExtraction> getKvExtraction() {
		return kvExtraction;
	}

	/**
	 * To set Kv Extraction.
	 * @param kvExtraction List<KVExtraction>
	 */
	public void setKvExtraction(List<KVExtraction> kvExtraction) {
		this.kvExtraction = kvExtraction;
	}

	/**
	 * To get Regex Validation.
	 * @return List<RegexValidation> 
	 */
	public List<RegexValidation> getRegexValidation() {
		return regexValidation;
	}

	/**
	 * To set Regex Validation.
	 * @param regexValidation List<RegexValidation>
	 */
	public void setRegexValidation(List<RegexValidation> regexValidation) {
		this.regexValidation = regexValidation;
	}

	/**
	 * To get Identifier.
	 * @return String
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * To set Identifier.
	 * @param identifier String
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * To get Field Order Number.
	 * @return int
	 */
	public int getFieldOrderNumber() {
		return fieldOrderNumber;
	}

	/**
	 * To set Field Order Number.
	 * @param fieldOrderNumber int
	 */
	public void setFieldOrderNumber(int fieldOrderNumber) {
		this.fieldOrderNumber = fieldOrderNumber;
	}

	/**
	 * To get Sample Value.
	 * @return String
	 */
	public String getSampleValue() {
		return sampleValue;
	}

	/**
	 * To set Sample Value.
	 * @param sampleValue String
	 */
	public void setSampleValue(String sampleValue) {
		this.sampleValue = sampleValue;
	}

	/**
	 * To get Barcode Type.
	 * @return String
	 */
	public String getBarcodeType() {
		return barcodeType;
	}

	/**
	 * To set Barcode Type.
	 * @param barcodeType String
	 */
	public void setBarcodeType(String barcodeType) {
		this.barcodeType = barcodeType;
	}

	/**
	 * To get Field Option Value List.
	 * @return String
	 */
	public String getFieldOptionValueList() {
		return fieldOptionValueList;
	}

	/**
	 * To set Field Option Value List.
	 * @param fieldOptionValueList String
	 */
	public void setFieldOptionValueList(String fieldOptionValueList) {
		this.fieldOptionValueList = fieldOptionValueList;
	}

	/**
	 * To check whether hidden or not.
	 * @return boolean
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * To set hidden.
	 * @param hidden boolean
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	/**
	 * Removes a KV Extraction from the field type based on id (Used in same respect as identifier).
	 * 
	 * @param identifier the id corresponding to the KV Extraction
	 * @return true if KV Extraction could be found and removed. False otherwise
	 */
	public boolean removeKvExtractionById(Long identifier) {
		boolean isRemoved = false;
		if (null != this.kvExtraction) {
			int index = 0;
			KVExtraction kvExt = null;
			for (KVExtraction kvExtn : this.kvExtraction) {
				if (identifier == kvExtn.getId()) {
					kvExt = this.kvExtraction.get(index);
					isRemoved = this.kvExtraction.remove(kvExt);
					break;
				}
				index++;
			}
		}
		return isRemoved;
	}

	/**
	 * Adds a KV Extraction to this field type.
	 * 
	 * @param kvExtraction the KV Extraction to be added
	 */
	public void addKVExtraction(KVExtraction kvExtraction) {

		if (null == kvExtraction) {
			return;
		}

		this.kvExtraction.add(kvExtraction);
	}

	public void postPersist() {
		super.postPersist();
		this.identifier = EphesoftProperty.FIELD_TYPE.getProperty() + Long.toHexString(this.getId()).toUpperCase();
	}

	/**
	 * Returns a KV Extraction based on identifier.
	 * 
	 * @param identifier the identifier corresponding to the KV Extraction
	 * @return KV Extraction if found. null otherwise
	 */
	public KVExtraction getKVExtractionbyIdentifier(String identifier) {
		KVExtraction kvExtraction1 = null;
		if (null != identifier && this.kvExtraction != null && !this.kvExtraction.isEmpty()) {
			for (KVExtraction kvExtraction : this.kvExtraction) {
				if (String.valueOf(kvExtraction.getId()).equals(identifier)) {
					kvExtraction1 = kvExtraction;
					break;
				}
			}
		}
		return kvExtraction1;
	}

	/**
	 * To remove Regex Validation by Id.
	 * @param identifier Long
	 * @return boolean
	 */
	public boolean removeRegexValidationById(Long identifier) {
		boolean isRemoved = false;
		if (null != this.regexValidation) {
			int index = 0;
			RegexValidation removalElement = null;
			for (RegexValidation actualElement : this.regexValidation) {
				if (identifier == actualElement.getId()) {
					removalElement = this.regexValidation.get(index);
					isRemoved = this.regexValidation.remove(removalElement);
					break;
				}
				index++;
			}
		}
		return isRemoved;
	}

	/**
	 * To add Regex Validation.
	 * @param regexValidation RegexValidation
	 */
	public void addRegexValidation(RegexValidation regexValidation) {

		if (null == regexValidation) {
			return;
		}

		this.regexValidation.add(regexValidation);
	}

	/**
	 * To get Regex Validation by Identifier.
	 * @param identifier String
	 * @return RegexValidation
	 */
	public RegexValidation getRegexValidationbyIdentifier(String identifier) {
		RegexValidation regexValidation1 = null;
		if (null != identifier && this.regexValidation != null && !this.regexValidation.isEmpty()) {
			for (RegexValidation regexVdn : this.regexValidation) {
				if (String.valueOf(regexVdn.getId()).equals(identifier)) {
					regexValidation1 = regexVdn;
					break;
				}
			}
		}

		return regexValidation1;
	}

	/**
	 * To set MultiLine.
	 * @param multiLine boolean
	 */
	public void setMultiLine(boolean multiLine) {
		this.multiLine = multiLine;
	}

	/**
	 * To check whether multiline or not.
	 * @return boolean
	 */
	public boolean isMultiLine() {
		return multiLine;
	}

	/**
	 * API to set the boolean flag 'readOnly'.
	 * 
	 * @param isReadOnly boolean
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	/**
	 * API to get the boolean flag 'readOnly'.
	 * 
	 * @return the isReadOnly
	 */
	public boolean getIsReadOnly() {
		return readOnly;
	}
}
