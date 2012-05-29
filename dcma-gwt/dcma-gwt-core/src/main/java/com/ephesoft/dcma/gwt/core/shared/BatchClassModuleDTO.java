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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BatchClassModuleDTO implements IsSerializable {

	private BatchClassDTO batchClass;

	private String identifier;

	private ModuleDTO module;

	private int orderNumber;

	private String remoteURL;

	private String remoteBatchClassIdentifier;

	private String workflowName;

	private boolean isDeleted;

	private boolean isNew;

	private Map<String, BatchClassPluginDTO> batchClassPluginsMap = new LinkedHashMap<String, BatchClassPluginDTO>();

	public Collection<BatchClassPluginDTO> getBatchClassPlugins() {
		return getBatchClassPlugins(false);
	}

	public Collection<BatchClassPluginDTO> getBatchClassPlugins(boolean includingDeleted) {
		Collection<BatchClassPluginDTO> values = batchClassPluginsMap.values();
		if (!includingDeleted) {
			Map<String, BatchClassPluginDTO> pluginsMap = new LinkedHashMap<String, BatchClassPluginDTO>();
			for (BatchClassPluginDTO batchClassPluginDTO : values) {
				if (!batchClassPluginDTO.isDeleted()) {
					pluginsMap.put(batchClassPluginDTO.getIdentifier(), batchClassPluginDTO);
				}
			}
			values = pluginsMap.values();
		}
		return values;
	}

	public void addBatchClassPlugin(BatchClassPluginDTO batchClassPluginDTO) {
		this.batchClassPluginsMap.put(batchClassPluginDTO.getIdentifier(), batchClassPluginDTO);
	}

	public void removeBatchClassPlugin(BatchClassPluginDTO batchClassPluginDTO) {
		this.batchClassPluginsMap.remove(batchClassPluginDTO.getIdentifier());
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public BatchClassDTO getBatchClass() {
		return batchClass;
	}

	public void setBatchClass(BatchClassDTO batchClass) {
		this.batchClass = batchClass;
	}

	public ModuleDTO getModule() {
		return module;
	}

	public void setModule(ModuleDTO module) {
		this.module = module;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public BatchClassPluginDTO getPluginByIdentifier(String identifier) {
		for (BatchClassPluginDTO batchClassPluginDTO : batchClassPluginsMap.values()) {
			if (batchClassPluginDTO.getIdentifier().equals(identifier))
				return batchClassPluginDTO;
		}
		return null;
	}

	public BatchClassPluginDTO getPluginByName(String name) {
		for (BatchClassPluginDTO batchClassPluginDTO : batchClassPluginsMap.values()) {
			if (batchClassPluginDTO.getPlugin().getPluginName().equals(name))
				return batchClassPluginDTO;
		}
		return null;
	}

	public String getRemoteBatchClassIdentifier() {
		return remoteBatchClassIdentifier;
	}

	public void setRemoteBatchClassIdentifier(String remoteBatchClassIdentifier) {
		this.remoteBatchClassIdentifier = remoteBatchClassIdentifier;
	}

	public String getRemoteURL() {
		return remoteURL;
	}

	public void setRemoteURL(String remoteURL) {
		this.remoteURL = remoteURL;
	}

	/**
	 * @return the workflowName
	 */
	public String getWorkflowName() {
		return workflowName;
	}

	/**
	 * @param workflowName the workflowName to set
	 */
	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	/**
	 * @return the batchClassPluginsMap
	 */
	public Map<String, BatchClassPluginDTO> getBatchClassPluginsMap() {
		return batchClassPluginsMap;
	}

	/**
	 * @param batchClassPluginsMap the batchClassPluginsMap to set
	 */
	public void setBatchClassPluginsMap(Map<String, BatchClassPluginDTO> batchClassPluginsMap) {
		this.batchClassPluginsMap = batchClassPluginsMap;
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

	/**
	 * @return the isNew
	 */
	public boolean isNew() {
		return isNew;
	}

	/**
	 * @param isNew the isNew to set
	 */
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
}
