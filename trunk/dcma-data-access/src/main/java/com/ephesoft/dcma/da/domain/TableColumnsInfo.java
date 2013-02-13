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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;

/**
 * Entity class for table_columns_info.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.model.common.AbstractChangeableEntity
 */
@Entity
@Table(name = "table_columns_info")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class TableColumnsInfo extends AbstractChangeableEntity implements Serializable {

	/**
	 * Serial version UID long.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * tableInfo TableInfo.
	 */
	@ManyToOne
	@JoinColumn(name = "table_info_id")
	private TableInfo tableInfo;

	/**
	 * columnName String.
	 */
	@Column(name = "column_name")
	private String columnName;

	/**
	 * columnHeaderPattern String.
	 */
	@Column(name = "column_header_pattern")
	private String columnHeaderPattern;

	/**
	 * columnStartCoordinate Integer.
	 */
	@Column(name = "column_start_coordinate")
	private Integer columnStartCoordinate;

	/**
	 * columnEndCoordinate Integer.
	 */
	@Column(name = "column_end_coordinate")
	private Integer columnEndCoordinate;

	/**
	 * columnCoordinateY0 Integer.
	 */
	@Column(name = "column_coordinate_y0")
	private Integer columnCoordinateY0;

	/**
	 * columnCoordinateY1 Integer.
	 */
	@Column(name = "column_coordinate_y1")
	private Integer columnCoordinateY1;

	/**
	 * columnPattern String.
	 */
	@Column(name = "column_pattern")
	private String columnPattern;

	/**
	 * betweenLeft String.
	 */
	@Column(name = "between_left")
	private String betweenLeft;

	/**
	 * betweenRight String.
	 */
	@Column(name = "between_right")
	private String betweenRight;

	/**
	 * required boolean.
	 */
	@Column(name = "is_required", columnDefinition = "bit default 0")
	private boolean required;

	/**
	 * mandatory boolean.
	 */
	@Column(name = "is_mandatory", columnDefinition = "bit default 0")
	private boolean mandatory;

	/**
	 * To get Table Info.
	 * 
	 * @return TableInfo
	 */
	public TableInfo getTableInfo() {
		return tableInfo;
	}

	/**
	 * To check whether is mandatory.
	 * 
	 * @return boolean
	 */
	public boolean isMandatory() {
		return mandatory;
	}

	/**
	 * To set mandatory.
	 * 
	 * @param mandatory boolean
	 */
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	/**
	 * To set Table Info.
	 * 
	 * @param tableInfo TableInfo
	 */
	public void setTableInfo(TableInfo tableInfo) {
		this.tableInfo = tableInfo;
	}

	/**
	 * To get Column Name.
	 * 
	 * @return String
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * To set Column Name.
	 * 
	 * @param columnName
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * To get Column Pattern.
	 * 
	 * @return String
	 */
	public String getColumnPattern() {
		return columnPattern;
	}

	/**
	 * To set Column Pattern.
	 * 
	 * @param columnPattern String
	 */
	public void setColumnPattern(String columnPattern) {
		this.columnPattern = columnPattern;
	}

	/**
	 * To get Between Left.
	 * 
	 * @return String
	 */
	public String getBetweenLeft() {
		return betweenLeft;
	}

	/**
	 * To set Between Left.
	 * 
	 * @param betweenLeft String
	 */
	public void setBetweenLeft(String betweenLeft) {
		this.betweenLeft = betweenLeft;
	}

	/**
	 * To get Between Right.
	 * 
	 * @return String
	 */
	public String getBetweenRight() {
		return betweenRight;
	}

	/**
	 * To get Between Right.
	 * 
	 * @param betweenRight String
	 */
	public void setBetweenRight(String betweenRight) {
		this.betweenRight = betweenRight;
	}

	/**
	 * To check whether is required.
	 * 
	 * @return boolean
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * To set required.
	 * 
	 * @param required boolean
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
	 * To get Column Header Pattern.
	 * 
	 * @return String
	 */
	public String getColumnHeaderPattern() {
		return columnHeaderPattern;
	}

	/**
	 * To set Column Header Pattern.
	 * 
	 * @param columnHeaderPattern String
	 */
	public void setColumnHeaderPattern(String columnHeaderPattern) {
		this.columnHeaderPattern = columnHeaderPattern;
	}

	/**
	 * To get Column Start Coordinate.
	 * 
	 * @return Integer
	 */
	public Integer getColumnStartCoordinate() {
		return columnStartCoordinate;
	}

	/**
	 * To set Column Start Coordinate.
	 * 
	 * @param columnStartCoordinate Integer
	 */
	public void setColumnStartCoordinate(Integer columnStartCoordinate) {
		this.columnStartCoordinate = columnStartCoordinate;
	}

	/**
	 * To get Column End Coordinate.
	 * 
	 * @return Integer
	 */
	public Integer getColumnEndCoordinate() {
		return columnEndCoordinate;
	}

	/**
	 * To set Column End Coordinate.
	 * 
	 * @param columnEndCoordinate Integer
	 */
	public void setColumnEndCoordinate(Integer columnEndCoordinate) {
		this.columnEndCoordinate = columnEndCoordinate;
	}

	/**
	 * To get Column Coordinate Y0.
	 * 
	 * @return Integer
	 */
	public Integer getColumnCoordinateY0() {
		return columnCoordinateY0;
	}

	/**
	 * To set Column Coordinate Y0.
	 * 
	 * @param columnCoordinateY0 Integer
	 */
	public void setColumnCoordinateY0(Integer columnCoordinateY0) {
		this.columnCoordinateY0 = columnCoordinateY0;
	}

	/**
	 * To get Column Coordinate Y1.
	 * 
	 * @return Integer
	 */
	public Integer getColumnCoordinateY1() {
		return columnCoordinateY1;
	}

	/**
	 * To set Column Coordinate Y1.
	 * 
	 * @param columnCoordinateY1 Integer
	 */
	public void setColumnCoordinateY1(Integer columnCoordinateY1) {
		this.columnCoordinateY1 = columnCoordinateY1;
	}
}
