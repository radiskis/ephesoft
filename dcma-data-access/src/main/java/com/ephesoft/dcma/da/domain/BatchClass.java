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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NaturalId;

import com.ephesoft.dcma.core.EphesoftProperty;
import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;
import com.ephesoft.dcma.da.id.BatchClassID;

@Entity
@Table(name = "batch_class")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class BatchClass extends AbstractChangeableEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private transient BatchClassID batchClassID;

	@Column(name = "batch_class_name")
	private String name;

	@Column(name = "identifier")
	private String identifier;

	@Column(name = "priority")
	private int priority;

	@Column(name = "batch_class_description")
	private String description;

	@Column(name = "batch_class_version")
	private String version;

	@Column(name = "unc_folder")
	@NaturalId(mutable=true)
	private String uncFolder;

	@Column(name = "process_name")
	private String processName;

	@Column(name = "curr_user")
	private String currentUser;

	@Column(name = "last_modified_by")
	private String lastModifiedBy;

	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "batch_class_id")
	@javax.persistence.OrderBy("orderNumber")
	private List<BatchClassModule> batchClassModules;

	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "batch_class_id")
	private List<DocumentType> documentTypes;

	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "batch_class_id")
	private List<BatchClassEmailConfiguration> emailConfigurations;
	
	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "batch_class_id")
	private List<BatchClassField> batchClassField;

	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "batch_class_id")
	private List<BatchClassGroups> assignedGroups;

	public BatchClass() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUncFolder() {
		return uncFolder;
	}

	public void setUncFolder(String uncFolder) {
		this.uncFolder = uncFolder;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public List<BatchClassModule> getBatchClassModules() {
		return batchClassModules;
	}

	public void setBatchClassModules(List<BatchClassModule> batchClassModules) {
		this.batchClassModules = batchClassModules;
	}

	public List<DocumentType> getDocumentTypes() {
		return documentTypes;
	}

	public void setDocumentTypes(List<DocumentType> documentTypes) {
		this.documentTypes = documentTypes;
	}

	/**
	 * @return the currentUser
	 */
	public String getCurrentUser() {
		return currentUser;
	}

	/**
	 * @param currentUser the currentUser to set
	 */
	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * @return the lastModifiedBy
	 */
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	/**
	 * @param lastModifiedBy the lastModifiedBy to set
	 */
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public List<BatchClassGroups> getAssignedGroups() {
		return assignedGroups;
	}

	public void setAssignedGroups(List<BatchClassGroups> assignedRoles) {
		this.assignedGroups = assignedRoles;
	}

	@Transient
	public BatchClassID getBatchClassID() {
		if (getId() != 0 && batchClassID == null) {
			batchClassID = new BatchClassID(identifier);
		}
		return batchClassID;
	}

	/**
	 * Returns the Module based on ID
	 * 
	 * @param id the id of the module required
	 * @return the module corresponding to the supplied id
	 */
	public BatchClassModule getBatchClassModuleById(long id) {
		if (!(this.batchClassModules == null || this.batchClassModules.isEmpty())) {
			for (BatchClassModule batchClassModule : this.batchClassModules) {
				if (batchClassModule.getId() == id) {
					return batchClassModule;
				}
			}
		}
		return null;
	}
	
	/**
	 * API to return the batch class module by name.
	 * 
	 * @param moduleName {@link String} the name of the module required
	 * @return {@link BatchClassModule} the module corresponding to the supplied name
	 */
	public BatchClassModule getBatchClassModuleByName(String moduleName) {
		if (!(this.batchClassModules == null || this.batchClassModules.isEmpty())) {
			for (BatchClassModule batchClassModule : this.batchClassModules) {
				if (batchClassModule.getModule().getName().equalsIgnoreCase(moduleName)) {
					return batchClassModule;
				}
			}
		}
		return null;
	}

	/**
	 * Removes the document type from this batch class based on the identifier Used to delete a document type
	 * 
	 * @param identifier the identifier of the document type that is to be removed
	 * @return true if the document was found and removed else false.
	 */
	public boolean removeDocumentTypeByIdentifier(String identifier) {
		List<DocumentType> documentTypesList = new ArrayList<DocumentType>();
		documentTypesList.addAll(this.documentTypes);
		for (DocumentType documentType : documentTypesList) {
			if (identifier.equals(documentType.getIdentifier())) {
				return documentTypes.remove(documentType);
			}
		}
		return false;
	}

	/**
	 * Removes the email configuration from this batch class based on the identifier Used to delete a email configuration
	 * 
	 * @param identifier the identifier of the email configuration that is to be removed
	 * @return true if the email configuration was found and removed else false.
	 */
	public boolean removeEmailConfigurationByIdentifier(String identifier) {
		List<BatchClassEmailConfiguration> emailBatchClassEmailConfigurationsList = new ArrayList<BatchClassEmailConfiguration>();
		emailBatchClassEmailConfigurationsList.addAll(this.emailConfigurations);
		for (BatchClassEmailConfiguration batchClassEmailConfiguration : emailBatchClassEmailConfigurationsList) {
			if (identifier.equals(String.valueOf(batchClassEmailConfiguration.getId()))) {
				return this.emailConfigurations.remove(batchClassEmailConfiguration);
			}
		}
		return false;
	}
	
	/**
	 * Removes the batch class field from this batch class based on the identifier Used to delete a batch class field
	 * 
	 * @param identifier the identifier of the batch class field that is to be removed
	 * @return true if the batch class field was found and removed else false.
	 */
	public boolean removeBatchClassFieldByIdentifier(String identifier) {
		List<BatchClassField> batchClassFieldList = new ArrayList<BatchClassField>();
		batchClassFieldList.addAll(this.batchClassField);
		for (BatchClassField batchClassField : batchClassFieldList) {
			if (identifier.equals(batchClassField.getIdentifier())) {
				return this.batchClassField.remove(batchClassField);
			}
		}
		return false;
	}

	/**
	 * Returns the document type corresponding to the identifier passed
	 * 
	 * @param identifier the identifier of the document that is needed
	 * @return document type if found else null
	 */
	public DocumentType getDocumentTypeByIdentifier(String identifier) {
		if (!(this.documentTypes == null || this.documentTypes.isEmpty())) {
			for (DocumentType documentType2 : this.documentTypes) {
				if (documentType2.getIdentifier().equals(identifier)) {
					return documentType2;
				}
			}
		}
		return null;
	}

	/**
	 * Returns the email configuration corresponding to the identifier passed
	 * 
	 * @param identifier the identifier of the email configuration that is needed
	 * @return email configuration if found else null
	 */
	public BatchClassEmailConfiguration getEmailConfigurationByIdentifier(String identifier) {
		if (!(this.emailConfigurations == null || this.emailConfigurations.isEmpty())) {
			for (BatchClassEmailConfiguration batchClassEmailConfiguration : this.emailConfigurations) {
				if (identifier.equals(String.valueOf(batchClassEmailConfiguration.getId()))) {
					return batchClassEmailConfiguration;
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns the batch class field corresponding to the identifier passed
	 * 
	 * @param identifier the identifier of the batch class field that is needed
	 * @return batch class field if found else null
	 */
	public BatchClassField getBatchClassFieldByIdentifier(String identifier) {
		if (!(this.batchClassField == null || this.batchClassField.isEmpty())) {
			for (BatchClassField batchClassField : this.batchClassField) {
				if (identifier.equals(batchClassField.getIdentifier())) {
					return batchClassField;
				}
			}
		}
		return null;
	}
	
	/**
	 * Adds a document type to this batch class
	 * 
	 * @param documentType the document type that is to be added in this batch class
	 */
	public void addDocumentType(DocumentType documentType) {
		this.documentTypes.add(documentType);
	}

	/**
	 * Adds a email configuration to this batch class
	 * 
	 * @param batchClassEmailConfiguration the email configuration that is to be added in this batch class
	 */
	public void addEmailConfiguration(BatchClassEmailConfiguration batchClassEmailConfiguration) {
		this.emailConfigurations.add(batchClassEmailConfiguration);
	}
	/**
	 * Adds a batch class field to this batch class
	 * 
	 * @param batchClassField the batch class field that is to be added in this batch class
	 */
	public void addBatchClassField(BatchClassField batchClassField) {
		this.batchClassField.add(batchClassField);
	}
	
	
	public void postPersist() {
		this.identifier = EphesoftProperty.BATCH_CLASS.getProperty() + Long.toHexString(this.getId()).toUpperCase();
		if (this.documentTypes != null && !this.documentTypes.isEmpty()) {
			for (DocumentType documentType : documentTypes) {
				documentType.postPersist();
			}
		}
		if (this.batchClassField != null && !this.batchClassField.isEmpty()) {
			for (BatchClassField batchClassField1 : batchClassField) {
				batchClassField1.postPersist();
			}
		}
	}

	public List<BatchClassField> getBatchClassField() {
		return batchClassField;
	}

	public void setBatchClassField(List<BatchClassField> batchClassField) {
		this.batchClassField = batchClassField;
	}

	public List<BatchClassEmailConfiguration> getEmailConfigurations() {
		return emailConfigurations;
	}

	public void setEmailConfigurations(List<BatchClassEmailConfiguration> emailConfigurations) {
		this.emailConfigurations = emailConfigurations;
	}

	/**
	 * @return the isDeleted
	 */
	public Boolean isDeleted() {
		if (this.isDeleted == null) {
			this.isDeleted = Boolean.FALSE;
		}
		return isDeleted;
	}

	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setDeleted(Boolean isDeleted) {
		if (isDeleted == null) {
			isDeleted = Boolean.FALSE;
		}
		this.isDeleted = isDeleted;
	}
}
