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

@Entity
@Table(name = "field_type")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class FieldType extends AbstractChangeableEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "document_type_id")
	private DocumentType docType;

	@Column(name = "field_data_type")
	@Enumerated(EnumType.STRING)
	private DataType dataType;

	@Column(name = "identifier")
	private String identifier;

	@Column(name = "field_type_name")
	private String name;

	@Column(name = "field_order_number", nullable = false)
	private int fieldOrderNumber;

	@Column(name = "field_type_description")
	private String description;

	@Column(name = "pattern")
	private String pattern;

	@Column(name = "sample_value")
	private String sampleValue;

	@Column(name = "barcode_type")
	private String barcodeType;

	@Column(name = "field_option_value_list")
	private String fieldOptionValueList;

	@Column(name = "is_hidden", columnDefinition = "bit default 0")
	private boolean isHidden;

	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "field_type_id")
	private List<KVExtraction> kvExtraction = new ArrayList<KVExtraction>();;

	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "field_type_id")
	private List<RegexValidation> regexValidation = new ArrayList<RegexValidation>();

	public DocumentType getDocType() {
		return docType;
	}

	public void setDocType(DocumentType docType) {
		this.docType = docType;
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

	public List<KVExtraction> getKvExtraction() {
		return kvExtraction;
	}

	public void setKvExtraction(List<KVExtraction> kvExtraction) {
		this.kvExtraction = kvExtraction;
	}

	public List<RegexValidation> getRegexValidation() {
		return regexValidation;
	}

	public void setRegexValidation(List<RegexValidation> regexValidation) {
		this.regexValidation = regexValidation;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public int getFieldOrderNumber() {
		return fieldOrderNumber;
	}

	public void setFieldOrderNumber(int fieldOrderNumber) {
		this.fieldOrderNumber = fieldOrderNumber;
	}

	public String getSampleValue() {
		return sampleValue;
	}

	public void setSampleValue(String sampleValue) {
		this.sampleValue = sampleValue;
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
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	/**
	 * Removes a KV Extraction from the field type based on id (Used in same respect as identifier)
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
	 * Adds a KV Extraction to this field type
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
	 * Returns a KV Extraction based on identifier
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

	public void addRegexValidation(RegexValidation regexValidation) {

		if (null == regexValidation) {
			return;
		}

		this.regexValidation.add(regexValidation);
	}

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
}
