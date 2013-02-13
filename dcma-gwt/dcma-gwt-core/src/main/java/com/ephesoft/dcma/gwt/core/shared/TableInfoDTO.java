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
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TableInfoDTO implements IsSerializable {

	private DocumentTypeDTO docTypeDTO;

	private String name;

	private String identifier;

	private String startPattern;

	private String endPattern;

	private boolean newTableInfo;

	private boolean deleted;

	private String tableExtractionAPI;

	private String widthOfMultiline;

	private List<TableColumnInfoDTO> columnInfoDTOs = new ArrayList<TableColumnInfoDTO>();

	private String displayImage;

	public String getDisplayImage() {
		return displayImage;
	}

	public void setDisplayImage(String displayImage) {
		this.displayImage = displayImage;
	}

	public DocumentTypeDTO getDocTypeDTO() {
		return docTypeDTO;
	}

	public void setDocTypeDTO(DocumentTypeDTO docTypeDTO) {
		this.docTypeDTO = docTypeDTO;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartPattern() {
		return startPattern;
	}

	public void setStartPattern(String startPattern) {
		this.startPattern = startPattern;
	}

	public String getEndPattern() {
		return endPattern;
	}

	public void setEndPattern(String endPattern) {
		this.endPattern = endPattern;
	}

	public List<TableColumnInfoDTO> getColumnInfoDTOs(boolean includeDeleted) {
		List<TableColumnInfoDTO> tableColumnInfoDTOs = getColumnInfoDTOs();
		if (includeDeleted) {
			tableColumnInfoDTOs = columnInfoDTOs;
		}
		return tableColumnInfoDTOs;
	}

	public List<TableColumnInfoDTO> getColumnInfoDTOs() {
		List<TableColumnInfoDTO> tableColumnInfoDTOs = new LinkedList<TableColumnInfoDTO>();
		for (TableColumnInfoDTO columnInfoDTO : columnInfoDTOs) {
			if (!(columnInfoDTO.isDeleted())) {
				tableColumnInfoDTOs.add(columnInfoDTO);
			}
		}
		return tableColumnInfoDTOs;
	}

	public void setColumnInfoDTOs(List<TableColumnInfoDTO> columnInfoDTOs) {
		this.columnInfoDTOs = columnInfoDTOs;
	}

	public void addColumnInfo(TableColumnInfoDTO columnInfoDTO) {
		columnInfoDTOs.add(columnInfoDTO);
	}

	public List<TableColumnInfoDTO> getTableColumnInfoList(boolean includeDeleted) {
		List<TableColumnInfoDTO> tableColumnInfoDTOs = getTableColumnInfoList();
		if (includeDeleted) {
			tableColumnInfoDTOs = columnInfoDTOs;
		}
		return tableColumnInfoDTOs;
	}

	public List<TableColumnInfoDTO> getTableColumnInfoList() {
		List<TableColumnInfoDTO> tableColumnInfoDTOs = new LinkedList<TableColumnInfoDTO>();
		for (TableColumnInfoDTO columnInfoDTO : columnInfoDTOs) {
			if (!(columnInfoDTO.isDeleted())) {
				tableColumnInfoDTOs.add(columnInfoDTO);
			}
		}
		return tableColumnInfoDTOs;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public boolean isNew() {
		return newTableInfo;
	}

	public void setNew(boolean newTableInfo) {
		this.newTableInfo = newTableInfo;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public TableColumnInfoDTO getTCInfoDTOByIdentifier(String identifier) {
		TableColumnInfoDTO tableColumnInfoDTO = null;
		for (TableColumnInfoDTO columnInfoDTO : columnInfoDTOs) {
			if (columnInfoDTO.getIdentifier().equals(identifier)) {
				tableColumnInfoDTO = columnInfoDTO;
			}
		}
		return tableColumnInfoDTO;
	}

	public TableColumnInfoDTO getTCInfoDTOByNameAndPattern(String name, String pattern) {
		TableColumnInfoDTO tableColumnInfoDTO = null;
		for (TableColumnInfoDTO columnInfoDTO : columnInfoDTOs) {
			if (columnInfoDTO.getColumnName().equals(name) && columnInfoDTO.getColumnPattern().equals(pattern)) {
				tableColumnInfoDTO = columnInfoDTO;
			}
		}
		return tableColumnInfoDTO;
	}

	public String getTableExtractionAPI() {
		return tableExtractionAPI;
	}

	public void setTableExtractionAPI(String tableExtractionAPI) {
		this.tableExtractionAPI = tableExtractionAPI;
	}

	public String getWidthOfMultiline() {
		return widthOfMultiline;
	}

	public void setWidthOfMultiline(String widthOfMultiline) {
		this.widthOfMultiline = widthOfMultiline;
	}
}
