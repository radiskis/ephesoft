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

import com.ephesoft.dcma.gwt.core.shared.importtree.Node;
import com.google.gwt.user.client.rpc.IsSerializable;

public class ImportBatchClassUserOptionDTO implements IsSerializable {

	private String zipFileLocation;
	private String uncFolder;
	private boolean importExisting;
	private boolean workflowDeployed;
	private boolean workflowExistsInBatchClass;
	private boolean workflowEqual;
	private String name;
	private String description;
	private String priority;
	private boolean useSource;
	private Node uiConfigRoot;
	private String systemFolder;

	public ImportBatchClassUserOptionDTO() {
		super();
		this.zipFileLocation = "";
		this.name = "";
		this.description = "";
		this.priority = "";
		this.uiConfigRoot = new Node();
	}

	public ImportBatchClassUserOptionDTO(String zipFileName, boolean isImportExisting, String name, String description,
			String priority, boolean isUseSource, Node uiConfigRoot, String systemFolder) {
		super();
		this.zipFileLocation = zipFileName;
		this.importExisting = isImportExisting;
		this.name = name;
		this.description = description;
		this.priority = priority;
		this.useSource = isUseSource;
		this.uiConfigRoot = uiConfigRoot;
		this.systemFolder = systemFolder;
	}

	public Node getUiConfigRoot() {
		return uiConfigRoot;
	}

	public void setUiConfigRoot(Node uiConfigRoot) {
		this.uiConfigRoot = uiConfigRoot;
	}

	public String getZipFileName() {
		return zipFileLocation;
	}

	public void setZipFileName(String zipFileName) {
		this.zipFileLocation = zipFileName;
	}

	public boolean isImportExisting() {
		return importExisting;
	}

	public void setImportExisting(boolean importExisting) {
		this.importExisting = importExisting;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public boolean isUseSource() {
		return useSource;
	}

	public void setUseSource(boolean useSource) {
		this.useSource = useSource;
	}

	public String getUncFolder() {
		return uncFolder;
	}

	public void setUncFolder(String uncFolder) {
		this.uncFolder = uncFolder;
	}
	
	
	public void setWorkflowDeployed(boolean workflowDeployed) {
		this.workflowDeployed = workflowDeployed;
	}
	
	public boolean isWorkflowDeployed() {
		return workflowDeployed;
	}
	
	public void setWorkflowExistsInBatchClass(boolean workflowExistsInBatchClass) {
		this.workflowExistsInBatchClass = workflowExistsInBatchClass;
	}
	
	public boolean isWorkflowExistsInBatchClass() {
		return workflowExistsInBatchClass;
	}
	
	
	public void setWorkflowEqual(boolean workflowEqual) {
		this.workflowEqual = workflowEqual;
	}
	
	public boolean isWorkflowEqual() {
		return workflowEqual;
	}

	
	/**
	 * @return the systemFolder
	 */
	public String getSystemFolder() {
		return systemFolder;
	}

	
	/**
	 * @param systemFolder the systemFolder to set
	 */
	public void setSystemFolder(String systemFolder) {
		this.systemFolder = systemFolder;
	}
}
