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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;

@Entity
@Table(name = "table_info")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class TableInfo extends AbstractChangeableEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "document_type_id")
	private DocumentType docType;

	@Column(name = "table_name")
	private String name;

	@Column(name = "start_pattern")
	private String startPattern;

	@Column(name = "end_pattern")
	private String endPattern;

	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "table_info_id")
	private List<TableColumnsInfo> tableColumnsInfo = new ArrayList<TableColumnsInfo>();

	public TableInfo() {
	}

	public DocumentType getDocType() {
		return docType;
	}

	public void setDocType(DocumentType docType) {
		this.docType = docType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStartPattern(String startPattern) {
		this.startPattern = startPattern;
	}

	public String getStartPattern() {
		return startPattern;
	}

	public void setEndPattern(String endPattern) {
		this.endPattern = endPattern;
	}

	public String getEndPattern() {
		return endPattern;
	}

	public void setTableColumnsInfo(List<TableColumnsInfo> tableColumnsInfo) {
		this.tableColumnsInfo = tableColumnsInfo;
	}

	public List<TableColumnsInfo> getTableColumnsInfo() {
		return tableColumnsInfo;
	}

	public boolean removeTableColumnsInfoById(Long id) {

		if (null == this.tableColumnsInfo) {
			return false;
		}

		int index = 0;
		TableColumnsInfo removalElement = null;
		for (TableColumnsInfo actualElement : this.tableColumnsInfo) {
			if (id == actualElement.getId()) {
				removalElement = this.tableColumnsInfo.get(index);
				break;
			}
			index++;
		}

		if (null == removalElement) {
			return false;
		}

		return this.tableColumnsInfo.remove(removalElement);

	}

	public void addTableColumnsInfo(TableColumnsInfo tableColumnsInfo) {

		if (null == tableColumnsInfo) {
			return;
		}

		this.tableColumnsInfo.add(tableColumnsInfo);
	}

	public TableColumnsInfo getTableColumnsInfobyIdOfColumn(Long tableColumnInfoId) {

		if (null == tableColumnInfoId || this.tableColumnsInfo == null || this.tableColumnsInfo.isEmpty()) {
			return null;
		}

		for (TableColumnsInfo tableColumn : this.tableColumnsInfo) {
			if (tableColumn.getId() == tableColumnInfoId) {
				return tableColumn;
			}
		}

		return null;
	}
	
	/**
	 * Adds a Table Column Info to this table
	 * 
	 * @param kvExtraction the KV Extraction to be added
	 */
	public void addTableColumnInfo(TableColumnsInfo tableColumnsInfo) {

		if (null == tableColumnsInfo) {
			return;
		}

		this.tableColumnsInfo.add(tableColumnsInfo);
	}
	

	/**
	 * Returns a Table Columns Info based on identifier
	 * 
	 * @param identifier the identifier corresponding to the Table Columns Info
	 * @return Table Columns Info if found. null otherwise
	 */
	public TableColumnsInfo getTableColumnInfobyIdentifier(String identifier) {

		if (null == identifier || this.tableColumnsInfo == null || this.tableColumnsInfo.isEmpty()) {
			return null;
		}

		for (TableColumnsInfo columnsInfo : this.tableColumnsInfo) {
			if (String.valueOf(columnsInfo.getId()).equals(identifier)) {
				return columnsInfo;
			}
		}

		return null;
	}


}
