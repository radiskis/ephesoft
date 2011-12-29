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

package com.ephesoft.dcma.gwt.core.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BatchClassDTO implements IsSerializable {

	private String identifier;
	private String name;
	private String priority;
	private String description;
	private String uncFolder;
	private String version;
	private boolean isDirty;
	private boolean isDeleted;

	private Map<String, BatchClassModuleDTO> moduleMap = new LinkedHashMap<String, BatchClassModuleDTO>();

	private Map<String, DocumentTypeDTO> documentsMap = new LinkedHashMap<String, DocumentTypeDTO>();

	private Map<String, EmailConfigurationDTO> emailMap = new LinkedHashMap<String, EmailConfigurationDTO>();

	private Map<String, BatchClassFieldDTO> batchClassFieldMap = new LinkedHashMap<String, BatchClassFieldDTO>();

	private List<RoleDTO> assignedRole = new ArrayList<RoleDTO>();

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
		setDirty(true);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		setDirty(true);
	}

	public String getUncFolder() {
		return uncFolder;
	}

	public void setUncFolder(String uncFolder) {
		this.uncFolder = uncFolder;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Collection<BatchClassModuleDTO> getModules() {
		return this.moduleMap.values();
	}

	public void addModule(BatchClassModuleDTO batchClassModuleDTO) {
		this.moduleMap.put(batchClassModuleDTO.getIdentifier(), batchClassModuleDTO);
	}

	/**
	 * 
	 * @return a collection of the documents that are present in the batchClass and have not been soft deleted.
	 */
	public Collection<DocumentTypeDTO> getDocuments() {
		Map<String, DocumentTypeDTO> dMap = new LinkedHashMap<String, DocumentTypeDTO>();
		for (DocumentTypeDTO documentTypeDTO : documentsMap.values())
			if (!(documentTypeDTO.isDeleted()))
				dMap.put(documentTypeDTO.getIdentifier(), documentTypeDTO);
		return dMap.values();
	}

	/**
	 * 
	 * @param includeDeleted : includes the soft deleted documents if set true
	 * @return a collection of documents present in the batch class
	 */
	public Collection<DocumentTypeDTO> getDocuments(boolean includeDeleted) {
		if (includeDeleted)
			return documentsMap.values();
		return getDocuments();
	}

	/**
	 * Adds a new document type to this batch
	 * 
	 * @param documentTypeDTO the documentTypeDTO to be added to this batch class.
	 */
	public void addDocumentType(DocumentTypeDTO documentTypeDTO) {
		documentsMap.put(documentTypeDTO.getIdentifier(), documentTypeDTO);
	}

	/**
	 * Remove a document type
	 * 
	 * @param documentTypeDTO the documentTypeDTO to be removed
	 */
	public void removeDocumentType(DocumentTypeDTO documentTypeDTO) {
		documentsMap.remove(documentTypeDTO.getIdentifier());
	}

	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}

	/**
	 * Returns the document type based on name
	 * 
	 * @param name the name of the document
	 * @return document type DTO based on provided name
	 */
	public DocumentTypeDTO getDocTypeByName(String name) {
		Collection<DocumentTypeDTO> dtos = documentsMap.values();
		if (dtos != null)
			for (DocumentTypeDTO documentTypeDTO : dtos) {
				if (documentTypeDTO.getName().equals(name)) {
					return documentTypeDTO;
				}
			}
		return null;
	}

	/**
	 * Api to check if the name of document is same
	 * 
	 * @param name the name to check
	 * @return true if a document by the name exists. False otherwise
	 */
	public boolean checkDocumentTypeName(String name) {
		if (getDocTypeByName(name) != null)
			return true;
		return false;
	}

	/**
	 * Api to return the module based on provided identifier
	 * 
	 * @param identifier the identifier of the module required
	 * @return BathcClassModuleDTO based on identifier
	 */
	public BatchClassModuleDTO getModuleByIdentifier(String identifier) {
		if (this.moduleMap != null && !this.moduleMap.isEmpty()) {
			for (BatchClassModuleDTO batchClassModuleDTO : this.moduleMap.values()) {
				if (batchClassModuleDTO.getIdentifier().equals(identifier))
					return batchClassModuleDTO;
			}
		}
		return null;
	}

	public DocumentTypeDTO getDocTypeByIdentifier(String identifier) {
		if (this.documentsMap != null && !this.documentsMap.isEmpty()) {
			for (DocumentTypeDTO documentTypeDTO : this.documentsMap.values()) {
				if (documentTypeDTO.getIdentifier().equals(identifier))
					return documentTypeDTO;
			}
		}
		return null;
	}

	public BatchClassModuleDTO getModuleByName(String name) {
		if (this.moduleMap != null && !this.moduleMap.isEmpty()) {
			for (BatchClassModuleDTO batchClassModuleDTO : this.moduleMap.values()) {
				if (batchClassModuleDTO.getModule().getName().equals(name))
					return batchClassModuleDTO;
			}
		}
		return null;
	}

	public void addEmailConfiguration(EmailConfigurationDTO emailConfigurationDTO) {
		this.emailMap.put(emailConfigurationDTO.getIdentifier(), emailConfigurationDTO);
	}

	public EmailConfigurationDTO getEmailConfigurationDTOByIdentifier(String identifier) {
		if (this.emailMap != null && !this.emailMap.isEmpty()) {
			for (EmailConfigurationDTO emailConfigurationDTO : this.emailMap.values()) {
				if (emailConfigurationDTO.getIdentifier().equals(identifier))
					return emailConfigurationDTO;
			}
		}
		return null;
	}

	public void removeEmailConfiguration(EmailConfigurationDTO emailConfigurationDTO) {
		this.emailMap.remove(emailConfigurationDTO.getIdentifier());
	}

	/**
	 * 
	 * @return a collection of the documents that are present in the batchClass and have not been soft deleted.
	 */
	public Collection<EmailConfigurationDTO> getEmailConfiguration() {
		Map<String, EmailConfigurationDTO> eMap = new LinkedHashMap<String, EmailConfigurationDTO>();
		for (EmailConfigurationDTO emailConfigurationDTO : emailMap.values())
			if (!(emailConfigurationDTO.isDeleted()))
				eMap.put(emailConfigurationDTO.getIdentifier(), emailConfigurationDTO);
		return eMap.values();
	}

	/**
	 * 
	 * @param includeDeleted : includes the soft deleted documents if set true
	 * @return a collection of documents present in the batch class
	 */
	public Collection<EmailConfigurationDTO> getEmailConfiguration(boolean includeDeleted) {
		if (includeDeleted) {
			return emailMap.values();
		}
		return getEmailConfiguration();
	}

	public EmailConfigurationDTO getEmailConfigurationByFields(String userName, String password, String serverName, String serverType,
			String folderName) {
		Collection<EmailConfigurationDTO> dtos = emailMap.values();
		if (dtos != null)
			for (EmailConfigurationDTO emailConfigurationDTO : dtos) {
				if (emailConfigurationDTO.getUserName().equals(userName) && emailConfigurationDTO.getPassword().equals(password)
						&& emailConfigurationDTO.getServerName().equals(serverName)
						&& emailConfigurationDTO.getServerType().equals(serverType)
						&& emailConfigurationDTO.getFolderName().equals(folderName)) {
					return emailConfigurationDTO;
				}
			}
		return null;
	}

	public boolean checkEmailConfiguration(String username, String password, String serverName, String serverType, String folderName) {
		boolean returnValue = false;
		if (getEmailConfigurationByFields(username, password, serverName, serverType, folderName) != null) {
			returnValue = true;
		}
		return returnValue;
	}

	public void addBatchClassField(BatchClassFieldDTO batchClassFieldDTO) {
		this.batchClassFieldMap.put(batchClassFieldDTO.getIdentifier(), batchClassFieldDTO);
	}

	public BatchClassFieldDTO getBatchClassFieldDTOByIdentifier(String identifier) {
		BatchClassFieldDTO batchClassFieldDTO = null;
		if (this.batchClassFieldMap != null && !this.batchClassFieldMap.isEmpty()) {
			batchClassFieldDTO = batchClassFieldMap.get(identifier);
		}
		return batchClassFieldDTO;
	}

	public void removeBatchClassField(BatchClassFieldDTO batchClassFieldDTO) {
		this.batchClassFieldMap.remove(batchClassFieldDTO.getIdentifier());
	}

	/**
	 * 
	 * @return a collection of the batch class field that are present in the batch class and have not been soft deleted.
	 */
	public Collection<BatchClassFieldDTO> getBatchClassField() {
		Map<String, BatchClassFieldDTO> bCFMap = new LinkedHashMap<String, BatchClassFieldDTO>();
		for (BatchClassFieldDTO batchClassFieldDTO : batchClassFieldMap.values()) {
			if (!(batchClassFieldDTO.isDeleted()))
				bCFMap.put(batchClassFieldDTO.getIdentifier(), batchClassFieldDTO);
		}

		return bCFMap.values();

	}

	public boolean checkFieldTypeName(String name) {
		if (getBatchClassFieldByName(name) != null)
			return true;
		return false;
	}

	public BatchClassFieldDTO getBatchClassFieldByName(String name) {
		Collection<BatchClassFieldDTO> bcfDtos = batchClassFieldMap.values();
		if (bcfDtos != null)
			for (BatchClassFieldDTO batchClassFieldDTO : bcfDtos) {
				if (batchClassFieldDTO.getName().equals(name)) {
					return batchClassFieldDTO;
				}
			}
		return null;
	}

	/**
	 * 
	 * @param includeDeleted : includes the soft deleted batch class field if set true
	 * @return a collection of batch class field present in the batch class
	 */
	public Collection<BatchClassFieldDTO> getBatchClassField(boolean includeDeleted) {
		if (includeDeleted) {
			return batchClassFieldMap.values();
		}
		return getBatchClassField();
	}

	public List<RoleDTO> getAssignedRole() {
		return assignedRole;
	}

	public void addAssignedRole(RoleDTO roleDTO) {
		this.assignedRole.add(roleDTO);
	}

	public void setAssignedRole(List<RoleDTO> assignedRole) {
		this.assignedRole = assignedRole;
	}

	/**
	 * @return the isDeleted
	 */
	public boolean isDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}
