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
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BatchClassPluginDTO implements IsSerializable {

	private BatchClassModuleDTO batchClassModule;

	private PluginDetailsDTO plugin;

	private String identifier;

	private int orderNumber;
	
	private boolean deleted;
	
	private boolean newPlugin;
 
	private List<BatchClassPluginConfigDTO> batchClassPluginConfigsList = new LinkedList<BatchClassPluginConfigDTO>();

	private Map<String, BatchClassDynamicPluginConfigDTO> batchClassDynamicPluginConfigsMap = new LinkedHashMap<String, BatchClassDynamicPluginConfigDTO>();

	// getting the sorted list of plugin configs sorted by order number of
	// plugin_configs
	public Collection<BatchClassPluginConfigDTO> getBatchClassPluginConfigs() {
		return batchClassPluginConfigsList;
	}

	public void sortBatchClassPluginConfigList() {

		Collections.sort(batchClassPluginConfigsList, new Comparator<BatchClassPluginConfigDTO>() {

			@Override
			public int compare(final BatchClassPluginConfigDTO batchClassPluginConfigDTOOne,
					final BatchClassPluginConfigDTO batchClassPluginConfigDTOTwo) {
				int result;
				final Integer orderNumberOne = batchClassPluginConfigDTOOne.getOrderNumber();
				final Integer orderNumberTwo = batchClassPluginConfigDTOTwo.getOrderNumber();
				if (orderNumberOne != null && orderNumberTwo != null) {
					result = orderNumberOne.compareTo(orderNumberTwo);
				} else if (orderNumberOne == null && orderNumberTwo == null) {
					result = 0;
				} else if (orderNumberOne == null) {
					result = -1;
				} else {
					result = 1;
				}
				return result;
			}

		});
	}

	public void addBatchClassPluginConfig(final BatchClassPluginConfigDTO batchClassPluginConfigDTO) {
		this.batchClassPluginConfigsList.add(batchClassPluginConfigDTO);
	}

	public BatchClassPluginConfigDTO getBatchClassPluginConfigDTOByName(final String name) {
		BatchClassPluginConfigDTO batchClassPluginConfigDTO = null;
		for (final BatchClassPluginConfigDTO dto : batchClassPluginConfigsList) {
			if (dto != null && dto.getName() != null && dto.getName().equals(name)) {
				batchClassPluginConfigDTO = dto;
			}
		}
		return batchClassPluginConfigDTO;
	}

	public BatchClassPluginConfigDTO getBatchClassPluginConfigDTOByQualifier(final String qualifier) {
		BatchClassPluginConfigDTO batchClassPluginConfigDTO = null;
		for (final BatchClassPluginConfigDTO dto : batchClassPluginConfigsList) {
			if (dto.getQualifier() != null && dto.getQualifier().equals(qualifier)) {
				batchClassPluginConfigDTO = dto;
			}
		}
		return batchClassPluginConfigDTO;
	}

	public BatchClassDynamicPluginConfigDTO getBatchClassPluginConfigDTOByDescription(final String description) {
		BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO = null;
		for (final BatchClassDynamicPluginConfigDTO dto : batchClassDynamicPluginConfigsMap.values()) {
			if (dto.getDescription() != null && dto.getDescription().equals(description)) {
				batchClassDynamicPluginConfigDTO = dto;
			}
		}
		return batchClassDynamicPluginConfigDTO;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(final int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public BatchClassModuleDTO getBatchClassModule() {
		return batchClassModule;
	}

	public void setBatchClassModule(final BatchClassModuleDTO batchClassModule) {
		this.batchClassModule = batchClassModule;
	}

	public PluginDetailsDTO getPlugin() {
		return plugin;
	}

	public void setPlugin(final PluginDetailsDTO plugin) {
		this.plugin = plugin;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}

	public Collection<BatchClassDynamicPluginConfigDTO> getBatchClassDynamicPluginConfigs() {
		return batchClassDynamicPluginConfigsMap.values();
	}

	public void addBatchClassDynamicPluginConfig(final BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO) {
		this.batchClassDynamicPluginConfigsMap.put(batchClassDynamicPluginConfigDTO.getIdentifier(), batchClassDynamicPluginConfigDTO);
	}

	public void addBatchClassDynamicPluginConfig(final String identifier, final BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO) {
		this.batchClassDynamicPluginConfigsMap.put(identifier, batchClassDynamicPluginConfigDTO);
	}

	public BatchClassDynamicPluginConfigDTO getBatchClassDynamicPluginConfigDTOById(final String identifier) {
		return batchClassDynamicPluginConfigsMap.get(identifier);
	}

	public boolean isDeleted() {
		return deleted;
	}
	
	
	public void setDeleted(final boolean deleted) {
		this.deleted = deleted;
	}
	
	/**
	 * @return the isNew
	 */
	public boolean isNew() {
		return newPlugin;
	}

	
	/**
	 * @param isNew the isNew to set
	 */
	public void setNew(final boolean isNew) {
		this.newPlugin = isNew;
	}

}
