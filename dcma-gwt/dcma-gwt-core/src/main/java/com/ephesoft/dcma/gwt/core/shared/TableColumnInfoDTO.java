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

import com.google.gwt.user.client.rpc.IsSerializable;

public class TableColumnInfoDTO implements IsSerializable {

	private TableInfoDTO tableInfoDTO;

	private String betweenLeft;

	private String betweenRight;

	private String identifier;

	private String columnName;

	private String columnPattern;

	private boolean required;

	private boolean mandatory;

	private boolean newTableColumnInfo;

	private boolean deleted;

	private String columnHeaderPattern;

	private String columnStartCoordinate;

	private String columnEndCoordinate;

	private String columnCoordY0;

	private String columnCoordY1;

	public TableInfoDTO getTableInfoDTO() {
		return tableInfoDTO;
	}

	public void setTableInfoDTO(TableInfoDTO tableInfoDTO) {
		this.tableInfoDTO = tableInfoDTO;
	}

	public String getBetweenLeft() {
		return betweenLeft;
	}

	public void setBetweenLeft(String betweenLeft) {
		this.betweenLeft = betweenLeft;
	}

	public String getBetweenRight() {
		return betweenRight;
	}

	public void setBetweenRight(String betweenRight) {
		this.betweenRight = betweenRight;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnPattern() {
		return columnPattern;
	}

	public void setColumnPattern(String columnPattern) {
		this.columnPattern = columnPattern;
	}

	public void setNew(boolean newTableColumnInfo) {
		this.newTableColumnInfo = newTableColumnInfo;
	}

	public boolean isNew() {
		return newTableColumnInfo;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getColumnHeaderPattern() {
		return columnHeaderPattern;
	}

	public void setColumnHeaderPattern(String columnHeaderPattern) {
		this.columnHeaderPattern = columnHeaderPattern;
	}

	public String getColumnStartCoordinate() {
		return columnStartCoordinate;
	}

	public void setColumnStartCoordinate(String columnStartCoordinate) {
		this.columnStartCoordinate = columnStartCoordinate;
	}

	public String getColumnEndCoordinate() {
		return columnEndCoordinate;
	}

	public void setColumnEndCoordinate(String columnEndCoordinate) {
		this.columnEndCoordinate = columnEndCoordinate;
	}

	public String getColumnCoordY0() {
		return columnCoordY0;
	}

	public void setColumnCoordY0(String columnCoordY0) {
		this.columnCoordY0 = columnCoordY0;
	}

	public String getColumnCoordY1() {
		return columnCoordY1;
	}

	public void setColumnCoordY1(String columnCoordY1) {
		this.columnCoordY1 = columnCoordY1;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public boolean isNewTableColumnInfo() {
		return newTableColumnInfo;
	}

	public void setNewTableColumnInfo(boolean newTableColumnInfo) {
		this.newTableColumnInfo = newTableColumnInfo;
	}

}
