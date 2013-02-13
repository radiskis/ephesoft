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
	private boolean dirty;
	private boolean deleted;
	private boolean deployed;
	private String systemFolder;

	private Map<String, WebScannerConfigurationDTO> scannerMap = new LinkedHashMap<String, WebScannerConfigurationDTO>();;

	private Map<String, BatchClassModuleDTO> moduleMap = new LinkedHashMap<String, BatchClassModuleDTO>();

	private Map<String, DocumentTypeDTO> documentsMap = new LinkedHashMap<String, DocumentTypeDTO>();

	private Map<String, ScannerMasterDTO> scannerMasterMap = new LinkedHashMap<String, ScannerMasterDTO>();

	private Map<String, EmailConfigurationDTO> emailMap = new LinkedHashMap<String, EmailConfigurationDTO>();

	private Map<String, CmisConfigurationDTO> cmisMap = new LinkedHashMap<String, CmisConfigurationDTO>();

	private Map<String, BatchClassFieldDTO> batchClassFieldMap = new LinkedHashMap<String, BatchClassFieldDTO>();

	private List<RoleDTO> assignedRole = new ArrayList<RoleDTO>();

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(final String priority) {
		this.priority = priority;
		setDirty(true);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
		setDirty(true);
	}

	public String getUncFolder() {
		return uncFolder;
	}

	public void setUncFolder(final String uncFolder) {
		this.uncFolder = uncFolder;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(final String version) {
		this.version = version;
	}

	public Collection<BatchClassModuleDTO> getModules() {
		return getModules(false);
	}

	public Collection<BatchClassModuleDTO> getModules(final boolean includeDeleted) {
		Collection<BatchClassModuleDTO> values = moduleMap.values();
		if (!includeDeleted) {
			final Map<String, BatchClassModuleDTO> batchClassModulesMap = new LinkedHashMap<String, BatchClassModuleDTO>();

			for (final BatchClassModuleDTO batchClassModuleDTO : values) {
				if (!(batchClassModuleDTO.isDeleted())) {
					batchClassModulesMap.put(batchClassModuleDTO.getIdentifier(), batchClassModuleDTO);
				}
			}
			values = batchClassModulesMap.values();
		}
		return values;

	}

	public void addModule(final BatchClassModuleDTO batchClassModuleDTO) {
		this.moduleMap.put(batchClassModuleDTO.getIdentifier(), batchClassModuleDTO);
	}

	public void removeModule(final BatchClassModuleDTO batchClassModuleDTO) {
		this.moduleMap.remove(batchClassModuleDTO.getIdentifier());
	}

	/**
	 * 
	 * @return a collection of the documents that are present in the batchClass and have not been soft deleted.
	 */
	public Collection<DocumentTypeDTO> getDocuments() {
		final Map<String, DocumentTypeDTO> dMap = new LinkedHashMap<String, DocumentTypeDTO>();
		for (final DocumentTypeDTO documentTypeDTO : documentsMap.values()){
			if (!(documentTypeDTO.isDeleted())){
		
				dMap.put(documentTypeDTO.getIdentifier(), documentTypeDTO);
			}
		}
		return dMap.values();
	}

	/**
	 * 
	 * @param includeDeleted : includes the soft deleted documents if set true
	 * @return a collection of documents present in the batch class
	 */
	public Collection<DocumentTypeDTO> getDocuments(final boolean includeDeleted) {
		Collection<DocumentTypeDTO> documents;
		if (includeDeleted){
			documents = documentsMap.values();
		}
		else{
			documents = getDocuments();
		}		
		return documents;
	}

	/**
	 * Adds a new document type to this batch
	 * 
	 * @param documentTypeDTO the documentTypeDTO to be added to this batch class.
	 */
	public void addDocumentType(final DocumentTypeDTO documentTypeDTO) {
		documentsMap.put(documentTypeDTO.getIdentifier(), documentTypeDTO);
	}

	/**
	 * Remove a document type
	 * 
	 * @param documentTypeDTO the documentTypeDTO to be removed
	 */
	public void removeDocumentType(final DocumentTypeDTO documentTypeDTO) {
		documentsMap.remove(documentTypeDTO.getIdentifier());
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(final boolean isDirty) {
		this.dirty = isDirty;
	}

	/**
	 * Returns the document type based on name
	 * 
	 * @param name the name of the document
	 * @return document type DTO based on provided name
	 */
	public DocumentTypeDTO getDocTypeByName(final String name) {
		final Collection<DocumentTypeDTO> dtos = documentsMap.values();
		DocumentTypeDTO documentTypeDTO = null;
		if (dtos != null){
			for (final DocumentTypeDTO dto : dtos) {
		
				if (dto.getName().equals(name)) {
					documentTypeDTO = dto;
				}
			}
		}
		return documentTypeDTO;
	}

	/**
	 * Api to check if the name of document is same
	 * 
	 * @param name the name to check
	 * @return true if a document by the name exists. False otherwise
	 */
	public boolean checkDocumentTypeName(final String name) {
		boolean valid = false;
		if (getDocTypeByName(name) != null){
			valid = true;
		}
		return valid;
	}

	/**
	 * Api to return the module based on provided identifier
	 * 
	 * @param identifier the identifier of the module required
	 * @return BathcClassModuleDTO based on identifier
	 */
	public BatchClassModuleDTO getModuleByIdentifier(final String identifier) {
		BatchClassModuleDTO batchClassModuleDTO = null;
		if (this.moduleMap != null && !this.moduleMap.isEmpty()) {
			for (final BatchClassModuleDTO moduleDTO : this.moduleMap.values()) {
				if (moduleDTO.getIdentifier().equals(identifier)){
					batchClassModuleDTO = moduleDTO;
				}
			}
		}
		return batchClassModuleDTO;
	}

	public DocumentTypeDTO getDocTypeByIdentifier(final String identifier) {
		DocumentTypeDTO documentTypeDTO = null;
		if (this.documentsMap != null && !this.documentsMap.isEmpty()) {
			for (final DocumentTypeDTO dto : this.documentsMap.values()) {
				if (dto.getIdentifier().equals(identifier)){
					documentTypeDTO = dto;
				}
			}
		}
		return documentTypeDTO;
	}

	public BatchClassModuleDTO getModuleByName(final String name) {
		BatchClassModuleDTO batchClassModuleDTO = null;
		if (this.moduleMap != null && !this.moduleMap.isEmpty()) {
			for (final BatchClassModuleDTO moduleDTO : this.moduleMap.values()) {
				if (moduleDTO.getModule().getName().equals(name)){
					batchClassModuleDTO = moduleDTO;
				}
			}
		}
		return batchClassModuleDTO;
	}

	public BatchClassModuleDTO getModuleByWorkflowName(final String name) {
		BatchClassModuleDTO batchClassModuleDTO = null;
		if (this.moduleMap != null && !this.moduleMap.isEmpty()) {
			for (final BatchClassModuleDTO batchClassModuleDTOObject : this.moduleMap.values()) {
				if (batchClassModuleDTOObject.getWorkflowName().equals(name)) {
					batchClassModuleDTO = batchClassModuleDTOObject;
					break;
				}
			}
		}
		return batchClassModuleDTO;
	}

	public void addEmailConfiguration(final EmailConfigurationDTO emailConfigurationDTO) {
		this.emailMap.put(emailConfigurationDTO.getIdentifier(), emailConfigurationDTO);
	}

	public void addCmisConfiguration(final CmisConfigurationDTO cmisConfigurationDTO) {
		this.cmisMap.put(cmisConfigurationDTO.getIdentifier(), cmisConfigurationDTO);
	}

	public WebScannerConfigurationDTO getWebScannerConfigurationById(final String identifier) {
		WebScannerConfigurationDTO webScannerConfigurationDTO = null;
		if (this.scannerMap != null && !this.scannerMap.isEmpty()) {
			for (final WebScannerConfigurationDTO scannerConfigurationDTO : this.scannerMap.values()) {
				if (scannerConfigurationDTO.getIdentifier().equals(identifier)){
					webScannerConfigurationDTO = scannerConfigurationDTO;
				}
			}
		}
		return webScannerConfigurationDTO;
	}

	public WebScannerConfigurationDTO getScannerConfigurationByIdentifier(final String identifier) {
		WebScannerConfigurationDTO webScannerConfigurationDTO = null;
		if (this.scannerMap != null && !this.scannerMap.isEmpty()) {
			for (final WebScannerConfigurationDTO scannerConfigurationDTO : this.scannerMap.values()) {
				if (scannerConfigurationDTO.getIdentifier().equals(identifier)){
					webScannerConfigurationDTO = scannerConfigurationDTO;
				}
			}
		}
		return webScannerConfigurationDTO;
	}

	public WebScannerConfigurationDTO getScannerConfigurationByProfileName(final String profileName) {
		WebScannerConfigurationDTO webScannerConfigurationDTO = null;
		final Collection<WebScannerConfigurationDTO> dtos = scannerMap.values();
		if (dtos != null){
			for (final WebScannerConfigurationDTO scannerConfigurationDTO : dtos) {
				if (scannerConfigurationDTO.getValue().equals(profileName)) {
					webScannerConfigurationDTO = scannerConfigurationDTO;
				}
			}
		}
		return webScannerConfigurationDTO;
	}

	public EmailConfigurationDTO getEmailConfigurationDTOByIdentifier(final String identifier) {
		EmailConfigurationDTO emailConfigurationDTO = null;
		if (this.emailMap != null && !this.emailMap.isEmpty()) {
			for (final EmailConfigurationDTO configurationDTO : this.emailMap.values()) {
				if (configurationDTO.getIdentifier().equals(identifier)){
					emailConfigurationDTO = configurationDTO;
				}
			}
		}
		return emailConfigurationDTO;
	}

	public void removeEmailConfiguration(final EmailConfigurationDTO emailConfigurationDTO) {
		this.emailMap.remove(emailConfigurationDTO.getIdentifier());
	}

	public CmisConfigurationDTO getCmisConfigurationDTOByIdentifier(final String identifier) {
		CmisConfigurationDTO cmisConfigurationDTO = null;
		if (this.cmisMap != null && !this.cmisMap.isEmpty()) {
			for (final CmisConfigurationDTO configurationDTO : this.cmisMap.values()) {
				if (configurationDTO.getIdentifier().equals(identifier)){
					cmisConfigurationDTO = configurationDTO;
				}
			}
		}
		return cmisConfigurationDTO;
	}

	public void removeCmisConfiguration(final CmisConfigurationDTO cmisConfigurationDTO) {
		this.cmisMap.remove(cmisConfigurationDTO.getIdentifier());
	}

	/**
	 * 
	 * @return a collection of the documents that are present in the batchClass and have not been soft deleted.
	 */
	public Collection<EmailConfigurationDTO> getEmailConfiguration() {
		final Map<String, EmailConfigurationDTO> eMap = new LinkedHashMap<String, EmailConfigurationDTO>();
		for (final EmailConfigurationDTO emailConfigurationDTO : emailMap.values()){
			if (!(emailConfigurationDTO.isDeleted())){
				eMap.put(emailConfigurationDTO.getIdentifier(), emailConfigurationDTO);
			}
		}
		return eMap.values();
	}

	/**
	 * 
	 * @return a collection of the documents that are present in the batchClass and have not been soft deleted.
	 */
	public Collection<CmisConfigurationDTO> getCmisConfiguration() {
		final Map<String, CmisConfigurationDTO> eMap = new LinkedHashMap<String, CmisConfigurationDTO>();
		for (final CmisConfigurationDTO cmisConfigurationDTO : cmisMap.values()){
			if (!(cmisConfigurationDTO.isDeleted())){
				eMap.put(cmisConfigurationDTO.getIdentifier(), cmisConfigurationDTO);
			}
		}
		return eMap.values();
	}

	/**
	 * 
	 * @return a collection of the documents that are present in the batchClass and have not been soft deleted.
	 */
	public Collection<WebScannerConfigurationDTO> getScannerConfiguration() {
		final Map<String, WebScannerConfigurationDTO> sMap = new LinkedHashMap<String, WebScannerConfigurationDTO>();
		for (final WebScannerConfigurationDTO dto : scannerMap.values()){
			if (!(dto.isDeleted())){
				sMap.put(dto.getIdentifier(), dto);
			}
		}
		return sMap.values();
	}

	public Collection<WebScannerConfigurationDTO> getScannerConfiguration(final boolean includeDeleted) {
		Collection<WebScannerConfigurationDTO> scannerConfigurationDTO;
		if (includeDeleted) {
			scannerConfigurationDTO = scannerMap.values();
		}else{
			scannerConfigurationDTO = getScannerConfiguration();
		}
		return scannerConfigurationDTO;
	}

	/**
	 * 
	 * @param includeDeleted : includes the soft deleted documents if set true
	 * @return a collection of documents present in the batch class
	 */
	public Collection<CmisConfigurationDTO> getCmisConfiguration(final boolean includeDeleted) {
		Collection<CmisConfigurationDTO> cmisConfigurationDTO;
		if (includeDeleted) {
			cmisConfigurationDTO = cmisMap.values();
		}else{
			cmisConfigurationDTO = getCmisConfiguration();
		}
		return cmisConfigurationDTO;
	}

	public CmisConfigurationDTO getCmisConfigurationByFields(final String serverURL, final String userName, final String password, final String repositoryID,
			final String fileExtension, final String folderName, final String cmisProperty, final String value, final String valueToUpdate) {
		final Collection<CmisConfigurationDTO> dtos = cmisMap.values();
		CmisConfigurationDTO configurationDTO = null;
		if (dtos != null){
			for (final CmisConfigurationDTO cmisConfigurationDTO : dtos) {
				if (cmisConfigurationDTO.getUserName().equals(userName) && cmisConfigurationDTO.getPassword().equals(password)
						&& cmisConfigurationDTO.getServerURL().equals(serverURL)
						&& cmisConfigurationDTO.getRepositoryID().equals(repositoryID)
						&& cmisConfigurationDTO.getFolderName().equals(folderName)
						&& cmisConfigurationDTO.getFileExtension().equals(fileExtension)
						&& cmisConfigurationDTO.getValue().equals(value)
						&& cmisConfigurationDTO.getValueToUpdate().equals(valueToUpdate)) {
					configurationDTO = cmisConfigurationDTO;
				}
			}
		}
		return configurationDTO;
	}

	/**
	 * 
	 * @param includeDeleted : includes the soft deleted documents if set true
	 * @return a collection of documents present in the batch class
	 */
	public Collection<EmailConfigurationDTO> getEmailConfiguration(final boolean includeDeleted) {
		Collection<EmailConfigurationDTO> emailConfigurationDTOs;
		if (includeDeleted) {
			emailConfigurationDTOs = emailMap.values();
		}else{
			emailConfigurationDTOs = getEmailConfiguration();
		}
		return emailConfigurationDTOs;
	}

	public EmailConfigurationDTO getEmailConfigurationByFields(final String userName, final String password, final String serverName, final String serverType,
			final String folderName) {
		final Collection<EmailConfigurationDTO> dtos = emailMap.values();
		EmailConfigurationDTO configurationDTO = null;
		if (dtos != null){
			for (final EmailConfigurationDTO emailConfigurationDTO : dtos) {
				if (emailConfigurationDTO.getUserName().equals(userName) && emailConfigurationDTO.getPassword().equals(password)
						&& emailConfigurationDTO.getServerName().equals(serverName)
						&& emailConfigurationDTO.getServerType().equals(serverType)
						&& emailConfigurationDTO.getFolderName().equals(folderName)) {
					configurationDTO = emailConfigurationDTO;
				}
			}
		}
		return configurationDTO;
	}

	public boolean checkEmailConfiguration(final String username, final String password, final String serverName, final String serverType, final String folderName) {
		boolean returnValue = false;
		if (getEmailConfigurationByFields(username, password, serverName, serverType, folderName) != null) {
			returnValue = true;
		}
		return returnValue;
	}

	public boolean checkCmisConfiguration(final String serverURL, final String userName, final String password, final String repositoryID,
			final String fileExtension, final String folderName, final String cmisProperty, final String value, final String valueToUpdate) {
		boolean returnValue = false;
		if (getCmisConfigurationByFields(serverURL, userName, password, repositoryID, fileExtension, folderName, cmisProperty, value,
				valueToUpdate) != null) {
			returnValue = true;
		}
		return returnValue;
	}

	public void addScannerConfiguration(final WebScannerConfigurationDTO dto) {
		this.scannerMap.put(dto.getIdentifier(), dto);
	}

	public void addBatchClassField(final BatchClassFieldDTO batchClassFieldDTO) {
		this.batchClassFieldMap.put(batchClassFieldDTO.getIdentifier(), batchClassFieldDTO);
	}

	public BatchClassFieldDTO getBatchClassFieldDTOByIdentifier(final String identifier) {
		BatchClassFieldDTO batchClassFieldDTO = null;
		if (this.batchClassFieldMap != null && !this.batchClassFieldMap.isEmpty()) {
			batchClassFieldDTO = batchClassFieldMap.get(identifier);
		}
		return batchClassFieldDTO;
	}

	public void removeBatchClassField(final BatchClassFieldDTO batchClassFieldDTO) {
		this.batchClassFieldMap.remove(batchClassFieldDTO.getIdentifier());
	}

	/**
	 * 
	 * @return a collection of the batch class field that are present in the batch class and have not been soft deleted.
	 */
	public Collection<BatchClassFieldDTO> getBatchClassField() {
		final Map<String, BatchClassFieldDTO> bCFMap = new LinkedHashMap<String, BatchClassFieldDTO>();
		for (final BatchClassFieldDTO batchClassFieldDTO : batchClassFieldMap.values()) {
			if (!(batchClassFieldDTO.isDeleted())){
				bCFMap.put(batchClassFieldDTO.getIdentifier(), batchClassFieldDTO);
			}
		}

		return bCFMap.values();

	}

	/**
	 * Returns the batch class field based on name excluding the given batchClassFieldId
	 * 
	 * @param name the name of the document
	 * @return batch class field DTO based on provided name
	 */
	public BatchClassFieldDTO getBatchClassFieldByName(final String name, final String batchClassFieldId) {
		final Collection<BatchClassFieldDTO> batchClassFields = batchClassFieldMap.values();
		BatchClassFieldDTO batchClassFieldDto = null;
		if (batchClassFields != null &&!batchClassFields.isEmpty()&&batchClassFieldId != null) {
			for (final BatchClassFieldDTO batchClassFieldDTO : batchClassFields) {
				if (!batchClassFieldDTO.getIdentifier().equals(batchClassFieldId) && batchClassFieldDTO.getName().equalsIgnoreCase(name)) {
					batchClassFieldDto = batchClassFieldDTO;
					break;
				}
			}
		}
		return batchClassFieldDto;
	}

	/**
	 * Returns the batch class field based on name
	 * 
	 * @param name the name of the document
	 * @return batch class field DTO based on provided name
	 */
	public BatchClassFieldDTO getBatchClassFieldByName(final String name) {
		final Collection<BatchClassFieldDTO> batchClassFields = batchClassFieldMap.values();
		BatchClassFieldDTO batchClassFieldDto = null;
		if (batchClassFields != null) {
			for (final BatchClassFieldDTO batchClassFieldDTO : batchClassFields) {
				if (batchClassFieldDTO.getName().equalsIgnoreCase(name)) {
					batchClassFieldDto = batchClassFieldDTO;
					break;
				}
			}
		}
		return batchClassFieldDto;
	}

	/**
	 * Api to check if the batch class field with given name exists excluding the given batchClassFieldId
	 * 
	 * @param name the name to check
	 * @return true if a batch class field by the name exists. False otherwise
	 */
	public boolean checkBatchClassFieldName(final String name, final String batchClassFieldId) {
		BatchClassFieldDTO batchClassFieldDTO = null;
		boolean isExists = false;
		if (batchClassFieldId == null) {
			batchClassFieldDTO = getBatchClassFieldByName(name);
		} else {
			batchClassFieldDTO = getBatchClassFieldByName(name, batchClassFieldId);
		}
		if (batchClassFieldDTO != null) {
			isExists = true;
		}
		return isExists;
	}

	/**
	 * 
	 * @param includeDeleted : includes the soft deleted batch class field if set true
	 * @return a collection of batch class field present in the batch class
	 */
	public Collection<BatchClassFieldDTO> getBatchClassField(final boolean includeDeleted) {
		Collection<BatchClassFieldDTO> batchClassFieldDTO;
		if (includeDeleted) {
			batchClassFieldDTO = batchClassFieldMap.values();
		}else{
			batchClassFieldDTO = getBatchClassField();
		}
		return batchClassFieldDTO;
	}

	public List<RoleDTO> getAssignedRole() {
		return assignedRole;
	}

	public void addAssignedRole(final RoleDTO roleDTO) {
		this.assignedRole.add(roleDTO);
	}

	public void setAssignedRole(final List<RoleDTO> assignedRole) {
		this.assignedRole = assignedRole;
	}

	/**
	 * @return the isDeleted
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setDeleted(final boolean isDeleted) {
		this.deleted = isDeleted;
	}

	/**
	 * @return the isDeployed
	 */
	public boolean isDeployed() {
		return deployed;
	}

	/**
	 * @param isDeployed the isDeployed to set
	 */
	public void setDeployed(final boolean isDeployed) {
		this.deployed = isDeployed;
	}

	public void addScannerMaster(final ScannerMasterDTO dto) {
		scannerMasterMap.put(dto.getName(), dto);
	}

	public Map<String, ScannerMasterDTO> getScannerMasterMap() {
		return scannerMasterMap;
	}

	
	/**
	 * Setter for system folder.
	 * 
	 * @return the systemFolder {@link String}
	 */
	public String getSystemFolder() {
		return systemFolder;
	}

	/**
	 * Getter for the system folder.
	 * 
	 * @param {@link String} systemFolder the systemFolder to set
	 */
	public void setSystemFolder(final String systemFolder) {
		this.systemFolder = systemFolder;
	}
	
}
