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

import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;

/**
 * Entity class for table_info.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.model.common.AbstractChangeableEntity
 */
@Entity
@Table(name = "table_info")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class TableInfo extends AbstractChangeableEntity implements Serializable {

	/**
	 * Serial version UID long.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * docType DocumentType.
	 */
	@ManyToOne
	@JoinColumn(name = "document_type_id")
	private DocumentType docType;

	/**
	 * name String.
	 */
	@Column(name = "table_name")
	private String name;

	/**
	 * startPattern String.
	 */
	@Column(name = "start_pattern")
	private String startPattern;

	/**
	 * endPattern String.
	 */
	@Column(name = "end_pattern")
	private String endPattern;

	/**
	 * tableExtractionAPI {@link String}.
	 */
	@Column(name = "table_extraction_api")
	private String tableExtractionAPI;

	/**
	 * widthOfMultiline {@link Integer}.
	 */
	@Column(name = "width_of_multiline", columnDefinition = "int default 0")
	private Integer widthOfMultiline;

	/**
	 * tableColumnsInfo List<TableColumnsInfo>.
	 */
	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "table_info_id")
	private List<TableColumnsInfo> tableColumnsInfo = new ArrayList<TableColumnsInfo>();

	/**
	 * displayImage String.
	 */
	@Column(name = "display_image")
	private String displayImage;

	/**
	 * To get Display Image.
	 * 
	 * @return String
	 */
	public String getDisplayImage() {
		return displayImage;
	}

	/**
	 * To set Display Image.
	 * 
	 * @param displayImage String
	 */
	public void setDisplayImage(String displayImage) {
		this.displayImage = displayImage;
	}

	/**
	 * To get Table Extraction API.
	 * 
	 * @return String
	 */
	public String getTableExtractionAPI() {
		return tableExtractionAPI;
	}

	/**
	 * To set Table Extraction API.
	 * 
	 * @param tableExtractionAPI String
	 */
	public void setTableExtractionAPI(String tableExtractionAPI) {
		this.tableExtractionAPI = tableExtractionAPI;
	}

	/**
	 * To get Doc Type.
	 * 
	 * @return DocumentType
	 */
	public DocumentType getDocType() {
		return docType;
	}

	/**
	 * To set Doc Type.
	 * 
	 * @param docType DocumentType
	 */
	public void setDocType(DocumentType docType) {
		this.docType = docType;
	}

	/**
	 * To get name.
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * To set name.
	 * 
	 * @param name String
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * To set Start Pattern.
	 * 
	 * @param startPattern String
	 */
	public void setStartPattern(String startPattern) {
		this.startPattern = startPattern;
	}

	/**
	 * To get Start Pattern.
	 * 
	 * @return String
	 */
	public String getStartPattern() {
		return startPattern;
	}

	/**
	 * To set End Pattern.
	 * 
	 * @param endPattern String
	 */
	public void setEndPattern(String endPattern) {
		this.endPattern = endPattern;
	}

	/**
	 * To get End Pattern.
	 * 
	 * @return String
	 */
	public String getEndPattern() {
		return endPattern;
	}

	/**
	 * To set Table Columns Info.
	 * 
	 * @param tableColumnsInfo List<TableColumnsInfo>
	 */
	public void setTableColumnsInfo(List<TableColumnsInfo> tableColumnsInfo) {
		this.tableColumnsInfo = tableColumnsInfo;
	}

	/**
	 * To get Table Columns Info.
	 * 
	 * @return List<TableColumnsInfo>
	 */
	public List<TableColumnsInfo> getTableColumnsInfo() {
		return tableColumnsInfo;
	}

	/**
	 * To get Width of multiline.
	 * 
	 * @return widthOfMultiline Integer
	 */
	public Integer getWidthOfMultiline() {
		return widthOfMultiline;
	}

	/**
	 * To set Width of multiline.
	 * 
	 * @param widthOfMultiline Integer
	 */
	public void setWidthOfMultiline(Integer widthOfMultiline) {
		this.widthOfMultiline = widthOfMultiline;
	}

	/**
	 * To remove Table Columns Info by Id.
	 * 
	 * @param identifier Long
	 * @return boolean
	 */
	public boolean removeTableColumnsInfoById(Long identifier) {
		boolean isRemoved = false;
		if (null != this.tableColumnsInfo) {
			int index = 0;
			TableColumnsInfo removalElement = null;
			for (TableColumnsInfo actualElement : this.tableColumnsInfo) {
				if (identifier == actualElement.getId()) {
					removalElement = this.tableColumnsInfo.get(index);
					isRemoved = this.tableColumnsInfo.remove(removalElement);
					break;
				}
				index++;
			}
		}

		return isRemoved;
	}

	/**
	 * To add Table Columns Info.
	 * 
	 * @param tableColumnsInfo TableColumnsInfo
	 */
	public void addTableColumnsInfo(TableColumnsInfo tableColumnsInfo) {

		if (null == tableColumnsInfo) {
			return;
		}

		this.tableColumnsInfo.add(tableColumnsInfo);
	}

	/**
	 * To get Table Columns Info by Id of Column.
	 * 
	 * @param tableColumnInfoId Long
	 * @return TableColumnsInfo
	 */
	public TableColumnsInfo getTableColumnsInfobyIdOfColumn(Long tableColumnInfoId) {
		TableColumnsInfo tableColumnsInfo1 = null;
		if (null != tableColumnInfoId && this.tableColumnsInfo != null && !this.tableColumnsInfo.isEmpty()) {
			for (TableColumnsInfo tableColumn : this.tableColumnsInfo) {
				if (tableColumn.getId() == tableColumnInfoId) {
					tableColumnsInfo1 = tableColumn;
					break;
				}
			}
		}
		return tableColumnsInfo1;
	}

	/**
	 * Adds a Table Column Info to this table.
	 * 
	 * @param tableColumnsInfo TableColumnsInfo
	 */
	public void addTableColumnInfo(TableColumnsInfo tableColumnsInfo) {

		if (null == tableColumnsInfo) {
			return;
		}

		this.tableColumnsInfo.add(tableColumnsInfo);
	}

	/**
	 * Returns a Table Columns Info based on identifier.
	 * 
	 * @param identifier the identifier corresponding to the Table Columns Info
	 * @return Table Columns Info if found. null otherwise
	 */
	public TableColumnsInfo getTableColumnInfobyIdentifier(String identifier) {
		TableColumnsInfo tableColumnsInfo = null;
		if (null != identifier && this.tableColumnsInfo != null && !this.tableColumnsInfo.isEmpty()) {
			for (TableColumnsInfo columnsInfo : this.tableColumnsInfo) {
				if (String.valueOf(columnsInfo.getId()).equals(identifier)) {
					tableColumnsInfo = columnsInfo;
					break;
				}
			}
		}
		return tableColumnsInfo;
	}

}
