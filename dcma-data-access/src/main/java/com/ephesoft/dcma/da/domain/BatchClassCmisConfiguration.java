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
 * This class is a Entity class for batch_class_cmis_configuration table.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.model.common.AbstractChangeableEntity
 */
@Entity
@Table(name = "batch_class_cmis_configuration")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class BatchClassCmisConfiguration extends AbstractChangeableEntity implements Serializable {

	/**
	 * Reference for SerialVersionUID.
	 */
	private static final long serialVersionUID = -8194075842000186880L;
	
	/**
	 * Reference for BatchClass.
	 */
	@ManyToOne
	@JoinColumn(name = "batch_class_id")
	private BatchClass batchClass;
	
	/**
	 * Reference for serverURL.
	 */
	@Column(name = "server_url")
	private String serverURL;
	
	/**
	 * Reference for userName.
	 */
	@Column(name = "username")
	private String userName;
	
	/**
	 * Reference for password.
	 */
	@Column(name = "password")
	private String password;
	
	/**
	 * Reference for repositoryID.
	 */
	@Column(name = "repository_id")
	private String repositoryID;
	
	/**
	 * Reference for fileExtension.
	 */
	@Column(name = "file_extension")
	private String fileExtension;
	
	/**
	 * Reference for folderName.
	 */
	@Column(name = "folder_name")
	private String folderName;
	
	/**
	 * Reference for cmisProperty.
	 */
	@Column(name = "cmis_property")
	private String cmisProperty;
	
	/**
	 * Reference for value.
	 */
	@Column(name = "value")
	private String value;
	
	/**
	 * Reference for value.
	 */
	@Column(name = "value_to_update")
	private String valueToUpdate;

	/**
	 * To get Batch Class.
	 * @return BatchClass
	 */
	public BatchClass getBatchClass() {
		return batchClass;
	}

	/**
	 * To set Batch Class.
	 * @param batchClass BatchClass
	 */
	public void setBatchClass(final BatchClass batchClass) {
		this.batchClass = batchClass;
	}

	/**
	 * To get Server URL.
	 * @return String
	 */
	public String getServerURL() {
		return serverURL;
	}

	/**
	 * To set Server URL.
	 * @param serverURL String
	 */
	public void setServerURL(final String serverURL) {
		this.serverURL = serverURL;
	}

	/**
	 * To get User Name.
	 * @return String
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * To set User Name.
	 * @param userName String
	 */
	public void setUserName(final String userName) {
		this.userName = userName;
	}

	/**
	 * To get Password.
	 * @return String
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * To set Password.
	 * @param password String
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * To get Repository ID.
	 * @return String
	 */
	public String getRepositoryID() {
		return repositoryID;
	}

	/**
	 * To set Repository ID.
	 * @param repositoryID String
	 */
	public void setRepositoryID(final String repositoryID) {
		this.repositoryID = repositoryID;
	}

	/**
	 * To get File Extension.
	 * @return String
	 */
	public String getFileExtension() {
		return fileExtension;
	}

	/**
	 * To set File Extension.
	 * @param fileExtension String
	 */
	public void setFileExtension(final String fileExtension) {
		this.fileExtension = fileExtension;
	}

	/**
	 * To get Folder Name.
	 * @return String
	 */
	public String getFolderName() {
		return folderName;
	}

	/**
	 * To set Folder Name.
	 * @param folderName String
	 */
	public void setFolderName(final String folderName) {
		this.folderName = folderName;
	}

	/**
	 * To get Cmis Property.
	 * @return String
	 */
	public String getCmisProperty() {
		return cmisProperty;
	}

	/**
	 * To set Cmis Property. 
	 * @param cmisProperty String
	 */
	public void setCmisProperty(final String cmisProperty) {
		this.cmisProperty = cmisProperty;
	}

	/**
	 * To get value.
	 * @return String
	 */
	public String getValue() {
		return value;
	}

	/**
	 * To set value. 
	 * @param value String
	 */
	public void setValue(final String value) {
		this.value = value;
	}
	
	/**
	 * To get Value to Update.
	 * @return String
	 */
	public String getValueToUpdate() {
		return valueToUpdate;
	}
	
	/**
	 * To set Value to Update. 
	 * @param valueToUpdate String
	 */
	public void setValueToUpdate(String valueToUpdate) {
		this.valueToUpdate = valueToUpdate;
	}
}
