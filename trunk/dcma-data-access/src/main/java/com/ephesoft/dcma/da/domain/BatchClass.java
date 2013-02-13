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
import javax.persistence.OneToMany;

import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NaturalId;

import com.ephesoft.dcma.core.EphesoftProperty;
import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;
import com.ephesoft.dcma.da.id.BatchClassID;

/**
 * Entity class for batch_class.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.model.common.AbstractChangeableEntity
 */
@Entity
@Table(name = "batch_class")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class BatchClass extends AbstractChangeableEntity implements Serializable {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * BATCH_CLASS_ID String.
	 */
	private static final String BATCH_CLASS_ID = "batch_class_id";

	/**
	 * batchClassID BatchClassID.
	 */
	private transient BatchClassID batchClassID;

	/**
	 * name String.
	 */
	@Column(name = "batch_class_name")
	private String name;

	/**
	 * identifier String.
	 */
	@Column(name = "identifier")
	private String identifier;

	/**
	 * priority int.
	 */
	@Column(name = "priority")
	private int priority;

	/**
	 * description String.
	 */
	@Column(name = "batch_class_description")
	private String description;

	/**
	 * version String.
	 */
	@Column(name = "batch_class_version")
	private String version;

	/**
	 * uncFolder String.
	 */
	@Column(name = "unc_folder")
	@NaturalId(mutable = true)
	private String uncFolder;

	/**
	 * processName String.
	 */
	@Column(name = "process_name")
	private String processName;

	/** 
	 * currentUser String.
	 */
	@Column(name = "curr_user")
	private String currentUser;

	/**
	 * lastModifiedBy String.
	 */
	@Column(name = "last_modified_by")
	private String lastModifiedBy;

	/**
	 * deleted Boolean.
	 */
	@Column(name = "is_deleted")
	private Boolean deleted;

	/**
	 * systemFolder String.
	 */
	@Column(name = "system_folder")
	private String systemFolder;

	/**
	 * batchClassModules List<BatchClassModule>.
	 */
	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = BATCH_CLASS_ID)
	@javax.persistence.OrderBy("orderNumber")
	private List<BatchClassModule> batchClassModules;

	/**
	 * documentTypes List<DocumentType>.
	 */
	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = BATCH_CLASS_ID)
	private List<DocumentType> documentTypes;

	/**
	 * emailConfigurations List<BatchClassEmailConfiguration>.
	 */
	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = BATCH_CLASS_ID)
	private List<BatchClassEmailConfiguration> emailConfigurations;

	/**
	 * batchClassField List<BatchClassField>.
	 */
	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = BATCH_CLASS_ID)
	private List<BatchClassField> batchClassField;

	/**
	 * batchClassScannerConfiguration List<BatchClassScannerConfiguration>.
	 */
	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = BATCH_CLASS_ID)
	private List<BatchClassScannerConfiguration> batchClassScannerConfiguration;

	/**
	 * assignedGroups List<BatchClassGroups>.
	 */
	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = BATCH_CLASS_ID)
	private List<BatchClassGroups> assignedGroups;

	/**
	 * cmisConfigurations List<BatchClassCmisConfiguration>.
	 */
	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = BATCH_CLASS_ID)
	private List<BatchClassCmisConfiguration> cmisConfigurations;

	/**
	 * To get name.
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * To set Name.
	 * @param name String
	 */
	public void setName(String name) {
		this.name = name;
	}

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
	 * To get Version.
	 * @return String
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * To set Version.
	 * @param  version String
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * To get Unc Folder.
	 * @return String
	 */ 
	public String getUncFolder() {
		return uncFolder;
	}

	/**
	 * To set Unc Folder.
	 * @param uncFolder String
	 */
	public void setUncFolder(String uncFolder) {
		this.uncFolder = uncFolder;
	}

	/**
	 * To get Process Name.
	 * @return String
	 */ 
	public String getProcessName() {
		return processName;
	}

	/**
	 * To set Process Name.
	 * @param processName String
	 */
	public void setProcessName(String processName) {
		this.processName = processName;
	}

	/**
	 * To get Batch Class Modules.
	 * @return List<BatchClassModule>
	 */
	public List<BatchClassModule> getBatchClassModules() {
		return batchClassModules;
	}

	/**
	 * To set Batch Class Modules.
	 * @param batchClassModules List<BatchClassModule>
	 */
	public void setBatchClassModules(List<BatchClassModule> batchClassModules) {
		this.batchClassModules = batchClassModules;
	}

	/**
	 * To get Document Types.
	 * @return List<DocumentType>
	 */
	public List<DocumentType> getDocumentTypes() {
		return documentTypes;
	}

	/**
	 * To set Document Types. 
	 * @param documentTypes List<DocumentType>
	 */
	public void setDocumentTypes(List<DocumentType> documentTypes) {
		this.documentTypes = documentTypes;
	}

	/**
	 * To get Batch Class Scanner Configuration.
	 * @return List<BatchClassScannerConfiguration>
	 */
	public List<BatchClassScannerConfiguration> getBatchClassScannerConfiguration() {
		return batchClassScannerConfiguration;
	}

	/**
	 * To get Batch Class Scanner Configuration.
	 * @param batchClassScannerConfiguration List<BatchClassScannerConfiguration>
	 */
	public void setBatchClassScannerConfiguration(List<BatchClassScannerConfiguration> batchClassScannerConfiguration) {
		this.batchClassScannerConfiguration = batchClassScannerConfiguration;
	}

	/**
	 * To get Current User.
	 * @return the currentUser
	 */
	public String getCurrentUser() {
		return currentUser;
	}

	/**
	 * To set Current User.
	 * @param currentUser String
	 */
	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * To get Last Modified By.
	 * @return the lastModifiedBy
	 */
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	/**
	 * To set Last Modified By.
	 * @param lastModifiedBy String
	 */
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
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
	 * To get Assigned Groups.
	 * @return List<BatchClassGroups>
	 */
	public List<BatchClassGroups> getAssignedGroups() {
		return assignedGroups;
	}

	/**
	 * To set Assigned Groups.
	 * @param assignedRoles List<BatchClassGroups>
	 */
	public void setAssignedGroups(List<BatchClassGroups> assignedRoles) {
		this.assignedGroups = assignedRoles;
	}

	/**
	 * To get Batch Class ID.
	 * @return BatchClassID
	 */
	@Transient
	public BatchClassID getBatchClassID() {
		if (getId() != 0 && batchClassID == null) {
			batchClassID = new BatchClassID(identifier);
		}
		return batchClassID;
	}

	/**
	 * Returns the Module based on ID.
	 * 
	 * @param identifier the id of the module required
	 * @return the module corresponding to the supplied id
	 */
	public BatchClassModule getBatchClassModuleById(long identifier) {
		BatchClassModule batchClassModule1 = null;
		if (!(this.batchClassModules == null || this.batchClassModules.isEmpty())) {
			for (BatchClassModule batchClassModule : this.batchClassModules) {
				if (batchClassModule.getId() == identifier) {
					batchClassModule1 = batchClassModule;
					break;
				}
			}
		}
		return batchClassModule1;
	}

	/**
	 * API to return the batch class module by name.
	 * 
	 * @param moduleName {@link String} the name of the module required
	 * @return {@link BatchClassModule} the module corresponding to the supplied name
	 */
	public BatchClassModule getBatchClassModuleByName(String moduleName) {
		BatchClassModule batchClassModule1 = null;
		if (!(this.batchClassModules == null || this.batchClassModules.isEmpty())) {
			for (BatchClassModule batchClassModule : this.batchClassModules) {
				if (batchClassModule.getModule().getName().equalsIgnoreCase(moduleName)) {
					batchClassModule1 = batchClassModule;
					break;
				}
			}
		}
		return batchClassModule1;
	}

	/**
	 * API to return the batch class module by workflow name.
	 * 
	 * @param workflowName {@link String} the workflowName of the module required
	 * @return {@link BatchClassModule} the module corresponding to the supplied name
	 */
	public BatchClassModule getBatchClassModuleByWorkflowName(String workflowName) {
		BatchClassModule batchClassModule1 = null;
		if (!(this.batchClassModules == null || this.batchClassModules.isEmpty())) {
			for (BatchClassModule batchClassModule : this.batchClassModules) {
				if (batchClassModule.getWorkflowName().equalsIgnoreCase(workflowName)) {
					batchClassModule1 = batchClassModule;
					break;
				}
			}
		}
		return batchClassModule1;
	}

	/**
	 * Removes the document type from this batch class based on the identifier Used to delete a document type.
	 * 
	 * @param identifier the identifier of the document type that is to be removed
	 * @return true if the document was found and removed else false.
	 */
	public boolean removeDocumentTypeByIdentifier(String identifier) {
		boolean isRemoved = false;
		List<DocumentType> documentTypesList = new ArrayList<DocumentType>();
		documentTypesList.addAll(this.documentTypes);
		for (DocumentType documentType : documentTypesList) {
			if (identifier.equals(documentType.getIdentifier())) {
				isRemoved = documentTypes.remove(documentType);
				break;
			}
		}
		return isRemoved;
	}

	/**
	 * Removes Scanner Configuration Identifier.
	 * 
	 * @param identifier String
	 * @return boolean
	 */
	public boolean removeScannerConfigIdentifier(String identifier) {
		boolean isRemoved = false;
		List<BatchClassScannerConfiguration> scannerList = new ArrayList<BatchClassScannerConfiguration>();
		scannerList.addAll(this.batchClassScannerConfiguration);
		for (BatchClassScannerConfiguration config : scannerList) {
			if (identifier.equals(String.valueOf(config.getId()))) {
				// remove the child config and parent config
				isRemoved = batchClassScannerConfiguration.remove(config);
				if (isRemoved) {
					for (BatchClassScannerConfiguration childConfig : config.getChildren()) {
						isRemoved = batchClassScannerConfiguration.remove(childConfig);
					}
					break;
				}
			}
		}
		return isRemoved;
	}

	/**
	 * Removes the email configuration from this batch class based on the identifier Used to delete a email configuration.
	 * 
	 * @param identifier the identifier of the email configuration that is to be removed
	 * @return true if the email configuration was found and removed else false.
	 */
	public boolean removeEmailConfigurationByIdentifier(String identifier) {
		boolean isRemoved = false;
		List<BatchClassEmailConfiguration> emailBatchClassEmailConfigurationsList = new ArrayList<BatchClassEmailConfiguration>();
		emailBatchClassEmailConfigurationsList.addAll(this.emailConfigurations);
		for (BatchClassEmailConfiguration batchClassEmailConfiguration : emailBatchClassEmailConfigurationsList) {
			if (identifier.equals(String.valueOf(batchClassEmailConfiguration.getId()))) {
				isRemoved = this.emailConfigurations.remove(batchClassEmailConfiguration);
				break;
			}
		}
		return isRemoved;
	}

	/**
	 * Removes the cmis configuration from this batch class based on the identifier Used to delete a cmis configuration.
	 * 
	 * @param identifier the identifier of the cmis configuration that is to be removed
	 * @return true if the cmis configuration was found and removed else false.
	 */
	public boolean removeCmisConfigurationByIdentifier(String identifier) {
		boolean isRemoved = false;
		List<BatchClassCmisConfiguration> batchClassCmisConfigurationsList = new ArrayList<BatchClassCmisConfiguration>();
		batchClassCmisConfigurationsList.addAll(this.cmisConfigurations);
		for (BatchClassCmisConfiguration batchClassCmisConfiguration : batchClassCmisConfigurationsList) {
			if (identifier.equals(String.valueOf(batchClassCmisConfiguration.getId()))) {
				isRemoved = this.cmisConfigurations.remove(batchClassCmisConfiguration);
				break;
			}
		}
		return isRemoved;
	}

	/**
	 * Removes the batch class field from this batch class based on the identifier Used to delete a batch class field.
	 * 
	 * @param identifier the identifier of the batch class field that is to be removed
	 * @return true if the batch class field was found and removed else false.
	 */
	public boolean removeBatchClassFieldByIdentifier(String identifier) {
		boolean isRemoved = false;
		List<BatchClassField> batchClassFieldList = new ArrayList<BatchClassField>();
		batchClassFieldList.addAll(this.batchClassField);
		for (BatchClassField batchClassField : batchClassFieldList) {
			if (identifier.equals(batchClassField.getIdentifier())) {
				isRemoved = this.batchClassField.remove(batchClassField);
				break;
			}
		}
		return isRemoved;
	}

	/**
	 * Returns the document type corresponding to the identifier passed.
	 * 
	 * @param identifier the identifier of the document that is needed
	 * @return document type if found else null
	 */
	public DocumentType getDocumentTypeByIdentifier(String identifier) {
		DocumentType documentType = null;
		if (!(this.documentTypes == null || this.documentTypes.isEmpty())) {
			for (DocumentType documentType2 : this.documentTypes) {
				if (documentType2.getIdentifier().equals(identifier)) {
					documentType = documentType2;
					break;
				}
			}
		}
		return documentType;
	}

	/**
	 * Returns the email configuration corresponding to the identifier passed.
	 * 
	 * @param identifier the identifier of the email configuration that is needed
	 * @return email configuration if found else null
	 */
	public BatchClassEmailConfiguration getEmailConfigurationByIdentifier(String identifier) {
		BatchClassEmailConfiguration batchClassEmailConfiguration1 = null;
		if (!(this.emailConfigurations == null || this.emailConfigurations.isEmpty())) {
			for (BatchClassEmailConfiguration batchClassEmailConfiguration : this.emailConfigurations) {
				if (identifier.equals(String.valueOf(batchClassEmailConfiguration.getId()))) {
					batchClassEmailConfiguration1 = batchClassEmailConfiguration;
					break;
				}
			}
		}
		return batchClassEmailConfiguration1;
	}

	/**
	 * Returns the cmis configuration corresponding to the identifier passed.
	 * 
	 * @param identifier the identifier of the cmis configuration that is needed
	 * @return BatchClassCmisConfiguration
	 */
	public BatchClassCmisConfiguration getCmisConfigurationByIdentifier(String identifier) {
		BatchClassCmisConfiguration batchClassCmisConfiguration1 = null;
		if (!(this.cmisConfigurations == null || this.cmisConfigurations.isEmpty())) {
			for (BatchClassCmisConfiguration batchClassCmisConfiguration : this.cmisConfigurations) {
				if (identifier.equals(String.valueOf(batchClassCmisConfiguration.getId()))) {
					batchClassCmisConfiguration1 = batchClassCmisConfiguration;
					break;
				}
			}
		}
		return batchClassCmisConfiguration1;
	}

	/**
	 * Returns the batch class field corresponding to the identifier passed.
	 * 
	 * @param identifier the identifier of the batch class field that is needed
	 * @return batch class field if found else null
	 */
	public BatchClassField getBatchClassFieldByIdentifier(String identifier) {
		BatchClassField batchClassField1 = null;
		if (!(this.batchClassField == null || this.batchClassField.isEmpty())) {
			for (BatchClassField batchClassField : this.batchClassField) {
				if (identifier.equals(batchClassField.getIdentifier())) {
					batchClassField1 = batchClassField;
					break;
				}
			}
		}
		return batchClassField1;
	}

	/**
	 * Adds a document type to this batch class.
	 * 
	 * @param documentType the document type that is to be added in this batch class
	 */
	public void addDocumentType(DocumentType documentType) {
		this.documentTypes.add(documentType);
	}

	/**
	 * Adds a email configuration to this batch class.
	 * 
	 * @param batchClassEmailConfiguration the email configuration that is to be added in this batch class
	 */
	public void addEmailConfiguration(BatchClassEmailConfiguration batchClassEmailConfiguration) {
		this.emailConfigurations.add(batchClassEmailConfiguration);
	}

	/**
	 * Adds a cmis configuration to this batch class.
	 * 
	 * @param batchClassCmisConfiguration the cmis configuration that is to be added in this batch class
	 */
	public void addCmisConfiguration(BatchClassCmisConfiguration batchClassCmisConfiguration) {
		this.cmisConfigurations.add(batchClassCmisConfiguration);
	}

	public void addScannerConfiguration(BatchClassScannerConfiguration batchClassscannerConfiguration) {
		this.batchClassScannerConfiguration.add(batchClassscannerConfiguration);
	}

	/**
	 * Adds a batch class field to this batch class.
	 * 
	 * @param batchClassField the batch class field that is to be added in this batch class
	 */
	public void addBatchClassField(BatchClassField batchClassField) {
		this.batchClassField.add(batchClassField);
	}

	/**
	 * To add variables other than in pojo.
	 */
	public void postPersist() {
		super.postPersist();
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

	/**
	 * To get Batch Class Field.
	 * @return List<BatchClassField>
	 */
	public List<BatchClassField> getBatchClassField() {
		return batchClassField;
	}

	/**
	 * To set Batch Class Field.
	 * @param batchClassField List<BatchClassField>
	 */
	public void setBatchClassField(List<BatchClassField> batchClassField) {
		this.batchClassField = batchClassField;
	}

	/**
	 * To get Email Configurations.
	 * @return List<BatchClassEmailConfiguration>
	 */
	public List<BatchClassEmailConfiguration> getEmailConfigurations() {
		return emailConfigurations;
	}

	/**
	 * To set Email Configurations.
	 * @param emailConfigurations List<BatchClassEmailConfiguration>
	 */
	public void setEmailConfigurations(List<BatchClassEmailConfiguration> emailConfigurations) {
		this.emailConfigurations = emailConfigurations;
	}

	/**
	 * To get Cmis Configurations.
	 * @return List<BatchClassCmisConfiguration>
	 */
	public List<BatchClassCmisConfiguration> getCmisConfigurations() {
		return cmisConfigurations;
	}

	/**
	 * To set Cmis Configurations.
	 * @param cmisConfigurations List<BatchClassCmisConfiguration>
	 */
	public void setCmisConfigurations(List<BatchClassCmisConfiguration> cmisConfigurations) {
		this.cmisConfigurations = cmisConfigurations;
	}

	/**
	 * To check whether deleted or not.
	 * @return the deleted
	 */
	public Boolean isDeleted() {
		if (this.deleted == null) {
			this.deleted = Boolean.FALSE;
		}
		return deleted;
	}

	/**
	 * To set deleted.
	 * @param deleted Boolean
	 */
	public void setDeleted(Boolean deleted) {
		Boolean isdeleted = deleted;
		if (deleted == null) {
			isdeleted = Boolean.FALSE;
		}
		this.deleted = isdeleted;
	}

	/**
	 * To remove Module by Identifier.
	 * @param moduleIdentifier String
	 */
	public void removeModuleByIdentifier(String moduleIdentifier) {
		List<BatchClassModule> batchClassModules = new ArrayList<BatchClassModule>(this.batchClassModules);
		for (BatchClassModule batchClassModule : batchClassModules) {
			if (moduleIdentifier.equals(String.valueOf(batchClassModule.getId()))) {
				this.batchClassModules.remove(batchClassModule);
				break;
			}
		}
	}

	/**
	 * To add new module.
	 * @param batchClassModule BatchClassModule
	 */
	public void addModule(BatchClassModule batchClassModule) {
		this.batchClassModules.add(batchClassModule);
	}

	/**
	 * To get Scanner Configuration by Identifier.
	 * @param identifier String
	 * @return BatchClassScannerConfiguration
	 */
	public BatchClassScannerConfiguration getScannerConfigByIdentifier(String identifier) {
		BatchClassScannerConfiguration config1 = null;
		if (!(this.batchClassScannerConfiguration == null || this.batchClassScannerConfiguration.isEmpty())) {
			for (BatchClassScannerConfiguration config : this.batchClassScannerConfiguration) {
				if (identifier.equals(String.valueOf(config.getId()))) {
					config1 = config;
					break;
				}
			}
		}
		return config1;
	}

	/**
	 * To get system folder.
	 * 
	 * @return the systemFolder {@link String}
	 */
	public String getSystemFolder() {
		return systemFolder;
	}

	/**
	 * To set the system folder.
	 * 
	 * @param {@link String} systemFolder 
	 */
	public void setSystemFolder(String systemFolder) {
		this.systemFolder = systemFolder;
	}
}
