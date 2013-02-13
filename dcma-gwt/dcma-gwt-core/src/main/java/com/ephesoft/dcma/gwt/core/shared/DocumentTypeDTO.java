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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DocumentTypeDTO implements IsSerializable {

	private BatchClassDTO batchClass;

	private int priority;

	private float minConfidenceThreshold;

	private String name;

	private String description;

	private String rspProjectFileName;

	private String identifier;

	private boolean deleted;

	private boolean newDocument;

	private Map<String, PageTypeDTO> pagesMap = new LinkedHashMap<String, PageTypeDTO>();

	private Map<String, FieldTypeDTO> fieldsMap = new LinkedHashMap<String, FieldTypeDTO>();

	private Map<String, TableInfoDTO> tableInfoMap = new LinkedHashMap<String, TableInfoDTO>();

	private Map<String, FunctionKeyDTO> functionKeyMap = new LinkedHashMap<String, FunctionKeyDTO>();
	
	private boolean hidden;

	public BatchClassDTO getBatchClass() {
		return batchClass;
	}

	public void setBatchClass(final BatchClassDTO batchClass) {
		this.batchClass = batchClass;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(final int priority) {
		this.priority = priority;
	}

	public float getMinConfidenceThreshold() {
		return minConfidenceThreshold;
	}

	public void setMinConfidenceThreshold(final float minConfidenceThreshold) {
		this.minConfidenceThreshold = minConfidenceThreshold;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getIdentifier() {
		return identifier;
	}

	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * @return the rspProjectFileName
	 */
	public String getRspProjectFileName() {
		return rspProjectFileName;
	}

	/**
	 * @param rspProjectFileName the rspProjectFileName to set
	 */
	public void setRspProjectFileName(final String rspProjectFileName) {
		this.rspProjectFileName = rspProjectFileName;
	}

	/**
	 * true for soft delete
	 * 
	 * @param isDeleted
	 */
	public void setDeleted(final boolean deleted) {
		this.deleted = deleted;
	}

	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}

	public boolean isNew() {
		return newDocument;
	}

	public void setNew(final boolean newDocument) {
		this.newDocument = newDocument;
	}

	public void addFieldType(final FieldTypeDTO fieldTypeDTO) {
		fieldsMap.put(fieldTypeDTO.getIdentifier().toString(), fieldTypeDTO);
	}

	public void addPageType(final PageTypeDTO pageTypeDTO) {
		pagesMap.put(pageTypeDTO.getIdentifier().toString(), pageTypeDTO);
	}

	public void addFunctionKey(final FunctionKeyDTO functionKeyDTO) {
		functionKeyMap.put(functionKeyDTO.getIdentifier().toString(), functionKeyDTO);
	}

	public Collection<FieldTypeDTO> getFields(final boolean includeDeleted) {
		Collection<FieldTypeDTO> fieldTypeDTO;
		if (includeDeleted){
			fieldTypeDTO = fieldsMap.values();
		}else{
			fieldTypeDTO = getFields();
		}
		return fieldTypeDTO;
	}

	public Collection<FieldTypeDTO> getFields() {
		final Map<String, FieldTypeDTO> fieldTypeDTOs = new LinkedHashMap<String, FieldTypeDTO>();
		for (final FieldTypeDTO fieldTypeDTO : fieldsMap.values()) {
			if (!(fieldTypeDTO.isDeleted())){
				fieldTypeDTOs.put(fieldTypeDTO.getIdentifier(), fieldTypeDTO);
			}
		}
		return fieldTypeDTOs.values();
	}

	public Collection<TableInfoDTO> getTableInfos(final boolean includeDeleted) {
		Collection<TableInfoDTO> tableInfoDTO;
		if (includeDeleted){
			tableInfoDTO = tableInfoMap.values();
		}else{
			tableInfoDTO = getTableInfos();
		}
		return tableInfoDTO;
	}

	public Collection<TableInfoDTO> getTableInfos() {
		final Map<String, TableInfoDTO> tableInfoDTOs = new LinkedHashMap<String, TableInfoDTO>();
		for (final TableInfoDTO tableInfoDTO : tableInfoMap.values()) {
			if (!(tableInfoDTO.isDeleted())){
				tableInfoDTOs.put(tableInfoDTO.getIdentifier(), tableInfoDTO);
			}
		}
		return tableInfoDTOs.values();
	}

	public void addFieldTypeDTO(final FieldTypeDTO fieldTypeDTO) {
		fieldsMap.put(fieldTypeDTO.getIdentifier(), fieldTypeDTO);
	}

	public void addTableInfoDTO(final TableInfoDTO tableInfoDTO) {
		tableInfoMap.put(tableInfoDTO.getIdentifier(), tableInfoDTO);
	}

	public Collection<PageTypeDTO> getPages(final boolean includeDeleted) {
		Collection<PageTypeDTO> pageTypeDTO;
		if (includeDeleted){
			pageTypeDTO = pagesMap.values();
		}else{
			pageTypeDTO = getPages();
		}
		return pageTypeDTO;
	}

	public Collection<PageTypeDTO> getPages() {
		final Map<String, PageTypeDTO> pageTypeDTOs = new LinkedHashMap<String, PageTypeDTO>();
		for (final PageTypeDTO pageTypeDTO : pagesMap.values()) {
			if (!(pageTypeDTO.isDeleted())) {
				pageTypeDTOs.put(pageTypeDTO.getIdentifier(), pageTypeDTO);
			}
		}
		return pageTypeDTOs.values();
	}

	public void removeFieldTypeDTO(final FieldTypeDTO fieldTypeDTO) {
		fieldsMap.remove(fieldTypeDTO.getIdentifier());
	}

	public void removeTableInfoDTO(final TableInfoDTO tableInfoDTO) {
		tableInfoMap.remove(tableInfoDTO.getIdentifier());
	}

	public FieldTypeDTO getFieldTypeByName(final String name) {
		FieldTypeDTO fieldTypeDTO = null;
		final Collection<FieldTypeDTO> dtos = fieldsMap.values();
		if (dtos != null){
			for (final FieldTypeDTO dto : dtos) {
				if (dto.getName().equals(name)) {
					fieldTypeDTO = dto;
				}
			}
		}
		return fieldTypeDTO;
	}

	public FieldTypeDTO getFieldTypeByIdentifier(final String identifier) {
		FieldTypeDTO fieldTypeDTO = null;
		final Collection<FieldTypeDTO> dtos = fieldsMap.values();
		if (dtos != null){
			for (final FieldTypeDTO dto : dtos) {
				if (dto.getIdentifier().equals(identifier)) {
					fieldTypeDTO = dto;
				}
			}
		}
		return fieldTypeDTO;
	}

	public boolean checkFieldTypeName(final String name) {
		boolean validName = false;
		if (getFieldTypeByName(name) != null){
			validName = true;
		}
		return validName;
	}

	public TableInfoDTO getTableInfoByIdentifier(final String identifier) {
		TableInfoDTO tableInfoDTO = null;
		final Collection<TableInfoDTO> dtos = tableInfoMap.values();
		if (dtos != null){
			for (final TableInfoDTO dto : dtos) {
				if (dto.getIdentifier().equals(identifier)) {
					tableInfoDTO = dto;
				}
			}
		}
		return tableInfoDTO;
	}

	public TableInfoDTO getTableInfoByName(final String name) {
		TableInfoDTO tableInfoDTO = null;
		final Collection<TableInfoDTO> dtos = tableInfoMap.values();
		if (dtos != null){
			for (final TableInfoDTO dto : dtos) {
				if (dto.getName().equals(name)) {
					tableInfoDTO = dto;
				}
			}
		}
		return tableInfoDTO;
	}

	public Collection<FunctionKeyDTO> getFunctionKeys() {
		final Map<String, FunctionKeyDTO> functionKeyDTOs = new LinkedHashMap<String, FunctionKeyDTO>();
		for (final FunctionKeyDTO functionKeyDTO : functionKeyMap.values()) {
			if (!functionKeyDTO.isDeleted()) {
				functionKeyDTOs.put(functionKeyDTO.getIdentifier(), functionKeyDTO);
			}
		}
		return functionKeyDTOs.values();
	}

	public Collection<FunctionKeyDTO> getFunctionKeys(final boolean includeDeleted) {
		Collection<FunctionKeyDTO> functionKeyDTOs;
		if (includeDeleted){
			functionKeyDTOs = functionKeyMap.values();
		}else{
			functionKeyDTOs= getFunctionKeys();
		}
		return functionKeyDTOs;
	}

	public FunctionKeyDTO getFunctionKeyByIdentifier(final String identifier) {
		FunctionKeyDTO functionKeyDTO = null;
		final Collection<FunctionKeyDTO> dtos = functionKeyMap.values();
		if (dtos != null){
			for (final FunctionKeyDTO dto : dtos) {
				if (dto.getIdentifier().equals(identifier)) {
					functionKeyDTO = dto;
				}
			}
		}
		return functionKeyDTO;
	}

	public FunctionKeyDTO getFunctionKeyDTOByShorcutKeyName(final String shortcutKeyName) {
		FunctionKeyDTO functionKeyDTO = null;
		final Collection<FunctionKeyDTO> dtos = functionKeyMap.values();
		if (dtos != null){
			for (final FunctionKeyDTO dto : dtos) {
				if (dto.getShortcutKeyName().equals(shortcutKeyName)) {
					functionKeyDTO = dto;
				}
			}
		}
		return functionKeyDTO;
	}
	
	public boolean isHidden() {
		return hidden;
	}

	
	public void setHidden(final boolean hidden) {
		this.hidden = hidden;
	}

}
