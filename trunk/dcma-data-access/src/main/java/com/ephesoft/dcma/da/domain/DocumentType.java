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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ephesoft.dcma.core.EphesoftProperty;
import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;

/**
 * Entity class for plugin_dependency.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.model.common.AbstractChangeableEntity
 */
@Entity
@Table(name = "document_type")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class DocumentType extends AbstractChangeableEntity implements Serializable {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * DOCUMENT_TYPE_ID String.
	 */
	private static final String DOCUMENT_TYPE_ID = "document_type_id";

	/**
	 * batchClass BatchClass.
	 */
	@ManyToOne
	@JoinColumn(name = "batch_class_id")
	private BatchClass batchClass;

	/**
	 * identifier String.
	 */
	@Column(name = "identifier")
	private String identifier;

	/**
	 * priority int.
	 */
	@Column
	private int priority;

	/**
	 * minConfidenceThreshold float.
	 */
	@Column(name = "min_confidence_threshold")
	private float minConfidenceThreshold;

	/**
	 * name String.
	 */
	@Column(name = "document_type_name")
	private String name;

	/**
	 * description String.
	 */
	@Column(name = "document_type_description")
	private String description;

	/**
	 * rspProjectFileName String.
	 */
	@Column(name = "rsp_project_file_name")
	private String rspProjectFileName;

	/**
	 * hidden boolean.
	 */
	@Column(name = "is_hidden", columnDefinition = "bit default 0")
	private boolean hidden;

	/**
	 * pages List<PageType>.
	 */
	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = DOCUMENT_TYPE_ID)
	private List<PageType> pages = new ArrayList<PageType>();

	/**
	 * fieldTypes List<FieldType>.
	 */
	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = DOCUMENT_TYPE_ID)
	@javax.persistence.OrderBy("fieldOrderNumber")
	private List<FieldType> fieldTypes = new ArrayList<FieldType>();

	/**
	 * tableInfos List<TableInfo>.
	 */
	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = DOCUMENT_TYPE_ID)
	private List<TableInfo> tableInfos = new ArrayList<TableInfo>();

	/**
	 * functionKeys List<FunctionKey>.
	 */
	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = DOCUMENT_TYPE_ID)
	private List<FunctionKey> functionKeys = new ArrayList<FunctionKey>();

	/**
	 * To get Priority.
	 * @return int
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * To set Priority.
	 * @param priority int 
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * To get Min Confidence Threshold.
	 * @return float
	 */
	public float getMinConfidenceThreshold() {
		return minConfidenceThreshold;
	}

	/**
	 * To set Min Confidence Threshold.
	 * @param minConfidenceThreshold float
	 */
	public void setMinConfidenceThreshold(float minConfidenceThreshold) {
		this.minConfidenceThreshold = minConfidenceThreshold;
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
	 * To get Batch Class.
	 * @return BatchClass
	 */
	public BatchClass getBatchClass() {
		return batchClass;
	}

	/**
	 * To set Batch Class.
	 * @param batchClass BatchClass
	 */
	public void setBatchClass(BatchClass batchClass) {
		this.batchClass = batchClass;
	}

	/**
	 * To get pages.
	 * @return List<PageType>
	 */
	public List<PageType> getPages() {
		return pages;
	}

	/**
	 * To set pages.
	 * @param pages List<PageType>
	 */
	public void setPages(List<PageType> pages) {
		this.pages = pages;
	}

	/**
	 * To get Field Types.
	 * @return List<FieldType>
	 */
	public List<FieldType> getFieldTypes() {
		return fieldTypes;
	}

	/**
	 * To set Field Types.
	 * @param fieldTypes List<FieldType>
	 */
	public void setFieldTypes(List<FieldType> fieldTypes) {
		this.fieldTypes = fieldTypes;
	}

	/**
	 * To set Table Infos.
	 * @param tableInfos List<TableInfo>
	 */
	public void setTableInfos(List<TableInfo> tableInfos) {
		this.tableInfos = tableInfos;
	}

	/**
	 * To get Table Infos.
	 * @return List<TableInfo>
	 */
	public List<TableInfo> getTableInfos() {
		return tableInfos;
	}

	/**
	 * To get Function Keys.
	 * @return List<FunctionKey>
	 */
	public List<FunctionKey> getFunctionKeys() {
		return functionKeys;
	}

	/**
	 * To set Function Keys.
	 * @param functionKeyList List<FunctionKey>
	 */
	public void setFunctionKeys(List<FunctionKey> functionKeyList) {
		functionKeys = functionKeyList;
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
	 * To get Rsp Project File Name.
	 * @return String
	 */
	public String getRspProjectFileName() {
		return rspProjectFileName;
	}

	/**
	 * To set Rsp Project File Name.
	 * @param rspProjectFileName String
	 */
	public void setRspProjectFileName(String rspProjectFileName) {
		this.rspProjectFileName = rspProjectFileName;
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
	 * @param isHidden boolean
	 */
	public void setHidden(boolean isHidden) {
		this.hidden = isHidden;
	}

	/**
	 * To add variables other than in pojo.
	 */
	public void postPersist() {
		super.postPersist();
		this.identifier = EphesoftProperty.DOCUMENT_TYPE.getProperty() + Long.toHexString(this.getId()).toUpperCase();
		if (pages != null && !pages.isEmpty()) {
			for (PageType pageType : pages) {
				pageType.postPersist();
			}
		}
		if (fieldTypes != null && !fieldTypes.isEmpty()) {
			for (FieldType fieldType : fieldTypes) {
				fieldType.postPersist();
			}
		}
		if (functionKeys != null && !functionKeys.isEmpty()) {
			for (FunctionKey functionKey : functionKeys) {
				functionKey.postPersist();
			}
		}
	}

	/**
	 * Removes a field type from the document based on identifier. Used to delete the field type
	 * 
	 * @param identifier the identifier of the field type that is to be removed
	 * @return true if field type was found and removed. false otherwise.
	 */
	public boolean removeFieldTypeByIdentifier(String identifier) {
		List<FieldType> fieldTypesList = new ArrayList<FieldType>();
		fieldTypesList.addAll(this.fieldTypes);
		for (FieldType fieldType : fieldTypesList) {
			if (identifier.equals(fieldType.getIdentifier())) {
				return fieldTypes.remove(fieldType);
			}
		}
		return false;
	}

	/**
	 * Returns a field type based on identifier.
	 * 
	 * @param identifier the identifier corresponding to which field type is required
	 * @return field type if found. null otherwise
	 */
	public FieldType getFieldByIdentifier(String identifier) {
		if (!(this.fieldTypes == null || this.fieldTypes.isEmpty())) {
			for (FieldType fieldType : this.fieldTypes) {
				if (fieldType.getIdentifier().equals(identifier)) {
					return fieldType;
				}
			}
		}
		return null;
	}

	/**
	 * Removes page type based on identifier. Used to delete a page type
	 * 
	 * @param identifier the identifier of the page type
	 * @return true if page type found. false otherwise
	 */
	public boolean removePageTypeByIdentifier(String identifier) {
		List<PageType> pageTypes = new ArrayList<PageType>();
		pageTypes.addAll(this.pages);
		for (PageType pageType : pageTypes) {
			if (identifier.equals(pageType.getIdentifier())) {
				return pages.remove(pageType);
			}
		}
		return false;
	}

	/**
	 * Returns page type based on identifier.
	 * 
	 * @param identifier the identifier corresponding to which page type is required
	 * @return page type if found. null otherwise
	 */
	public PageType getPageByIdentifier(String identifier) {
		if (!(this.pages == null || this.pages.isEmpty())) {
			for (PageType pageType : this.pages) {
				if (pageType.getIdentifier().equals(identifier)) {
					return pageType;
				}
			}
		}
		return null;
	}

	/**
	 * Adds a field type to this document type.
	 * 
	 * @param fieldType the field type to be added
	 */
	public void addFieldType(FieldType fieldType) {
		this.fieldTypes.add(fieldType);
	}

	/**
	 * Adds a page type to this document.
	 * 
	 * @param pageType the page type to be added
	 */
	public void addPageType(PageType pageType) {
		this.pages.add(pageType);
	}

	/**
	 * Removes a table info from the document based on identifier. Used to delete the table info.
	 * 
	 * @param identifier the identifier of the table info that is to be removed
	 * @return true if table info was found and removed. false otherwise.
	 */
	public boolean removeTableInfoByIdentifier(String identifier) {
		List<TableInfo> tableInfosList = new ArrayList<TableInfo>();
		tableInfosList.addAll(this.tableInfos);
		for (TableInfo tableInfo : tableInfosList) {
			if (identifier.equals(String.valueOf(tableInfo.getId()))) {
				return tableInfos.remove(tableInfo);
			}
		}
		return false;
	}

	/**
	 * Adds a table Info to this document type.
	 * 
	 * @param tableInfo the table info to be added
	 */
	public void addTableInfo(TableInfo tableInfo) {
		this.tableInfos.add(tableInfo);
	}

	/**
	 * Returns a table info based on identifier.
	 * 
	 * @param identifier the identifier corresponding to which table info is required
	 * @return tableInfo if found. null otherwise
	 */
	public TableInfo getTableInfoByIdentifier(String identifier) {
		if (!(this.tableInfos == null || this.tableInfos.isEmpty())) {
			for (TableInfo tableInfo : this.tableInfos) {
				if (String.valueOf(tableInfo.getId()).equals(identifier)) {
					return tableInfo;
				}
			}
		}
		return null;
	}

	/**
	 * To add Function Key.
	 * @param functionKey FunctionKey
	 */
	public void addFunctionKey(FunctionKey functionKey) {
		this.functionKeys.add(functionKey);
	}

	/**
	 * To remove Function Key of the specified Identifier.
	 * @param identifier String
	 * @return boolean
	 */
	public boolean removeFunctionKeyByIdentifier(String identifier) {
		List<FunctionKey> functionKeyList = new ArrayList<FunctionKey>();
		functionKeyList.addAll(this.functionKeys);
		for (FunctionKey functionKey : functionKeyList) {
			if (identifier.equals(functionKey.getIdentifier())) {
				return functionKeys.remove(functionKey);
			}
		}
		return false;
	}

	/**
	 * Returns a field type based on identifier.
	 * 
	 * @param identifier the identifier corresponding to which field type is required
	 * @return field type if found. null otherwise
	 */
	public FunctionKey getFunctionKeyByIdentifier(String identifier) {
		if (!(this.functionKeys == null || this.functionKeys.isEmpty())) {
			for (FunctionKey functionKey : this.functionKeys) {
				if (functionKey.getIdentifier().equals(identifier)) {
					return functionKey;
				}
			}
		}
		return null;
	}

}
