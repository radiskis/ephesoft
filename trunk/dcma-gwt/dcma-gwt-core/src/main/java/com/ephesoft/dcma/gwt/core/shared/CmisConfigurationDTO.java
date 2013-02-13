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

public class CmisConfigurationDTO implements IsSerializable {

	private BatchClassDTO batchClass;

	private String serverURL;

	private String userName;

	private String password;

	private String repositoryID;

	private String fileExtension;

	private String folderName;

	private String cmisProperty;

	private String value;

	private String valueToUpdate;

	private String identifier;
	
	private boolean deleted;
	
	private boolean newConfiguration;

	public BatchClassDTO getBatchClass() {
		return batchClass;
	}

	public void setBatchClass(BatchClassDTO batchClass) {
		this.batchClass = batchClass;
	}

	public String getServerURL() {
		return serverURL;
	}

	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepositoryID() {
		return repositoryID;
	}

	public void setRepositoryID(String repositoryID) {
		this.repositoryID = repositoryID;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getCmisProperty() {
		return cmisProperty;
	}

	public void setCmisProperty(String cmisProperty) {
		this.cmisProperty = cmisProperty;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueToUpdate() {
		return valueToUpdate;
	}

	public void setValueToUpdate(String valueToUpdate) {
		this.valueToUpdate = valueToUpdate;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public boolean isNew() {
		return newConfiguration;
	}
	
	public void setNew(boolean newConfiguration) {
		this.newConfiguration = newConfiguration;
	}
	
	public boolean isDeleted() {
		return deleted;
	}
	
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
