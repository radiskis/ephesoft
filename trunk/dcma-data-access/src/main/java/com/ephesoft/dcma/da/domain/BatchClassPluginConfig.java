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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;

/**
 * Entity class for batch_class_plugin_config.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.model.common.AbstractChangeableEntity
 */
@Entity
@Table(name = "batch_class_plugin_config")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class BatchClassPluginConfig extends AbstractChangeableEntity implements Cloneable {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = -3994727578168859843L;

	/**
	 * batchClassPlugin BatchClassPlugin.
	 */
	@OneToOne
	@JoinColumn(name = "batch_class_plugin_id")
	private BatchClassPlugin batchClassPlugin;

	/**
	 * pluginConfig PluginConfig.
	 */
	@OneToOne
	@JoinColumn(name = "plugin_config_id")
	private PluginConfig pluginConfig;

	/**
	 * kvPageProcesses List<KVPageProcess>.
	 */
	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "batch_class_plugin_config_id")
	private List<KVPageProcess> kvPageProcesses = new ArrayList<KVPageProcess>();

	/**
	 * qualifier String.
	 */
	@Column(name = "qualifier")
	private String qualifier;

	/**
	 * value String.
	 */
	@Column(name = "plugin_config_value")
	private String value;

	/**
	 * To get Batch Class Plugin.
	 * @return BatchClassPlugin
	 */
	public BatchClassPlugin getBatchClassPlugin() {
		return batchClassPlugin;
	}

	/**
	 * To set Batch Class Plugin.
	 * @param batchClassPlugin BatchClassPlugin
	 */
	public void setBatchClassPlugin(BatchClassPlugin batchClassPlugin) {
		this.batchClassPlugin = batchClassPlugin;
	}

	/**
	 * To get Kv Page Processes.
	 * @return List<KVPageProcess>
	 */
	public List<KVPageProcess> getKvPageProcesses() {
		return kvPageProcesses;
	}

	/**
	 * To set Kv Page Processes. 
	 * @param kvPageProcesses List<KVPageProcess>
	 */
	public void setKvPageProcesses(List<KVPageProcess> kvPageProcesses) {
		this.kvPageProcesses = kvPageProcesses;
	}

	/**
	 * To get Plugin Config.
	 * @return PluginConfig
	 */
	public PluginConfig getPluginConfig() {
		return pluginConfig;
	}

	/**
	 * To set Plugin Config.
	 * @param pluginConfig PluginConfig
	 */
	public void setPluginConfig(PluginConfig pluginConfig) {
		this.pluginConfig = pluginConfig;
	}

	/**
	 * To get Qualifier.
	 * @return String
	 */
	public String getQualifier() {
		return qualifier;
	}

	/**
	 * To set Qualifier.
	 * @param qualifier String
	 */
	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}

	/**
	 * To get name.
	 * @return String
	 */
	public String getName() {
		return pluginConfig.getName();
	}

	/**
	 * To set name.
	 * @param name String
	 */
	public void setName(String name) {
		this.pluginConfig.setName(name);
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
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * To get Description.
	 * @return String
	 */
	public String getDescription() {
		return pluginConfig.getDescription();
	}

	/**
	 * To set Description.
	 * @param description String
	 */
	public void setDescription(String description) {
		this.pluginConfig.setDescription(description);
	}

	/**
	 * To get Sample Value.
	 * @return List<String> 
	 */
	public List<String> getSampleValue() {
		return pluginConfig.getSampleValue();
	}

	/**
	 * To add KV Page Process.
	 * @param kvPageProcess KVPageProcess
	 */ 
	public void addKVPageProcess(KVPageProcess kvPageProcess) {
		if (null == kvPageProcess) {
			return;
		}
		this.kvPageProcesses.add(kvPageProcess);
	}

	/**
	 * To remove Kv Page Process by Id.
	 * @param identifier long.
	 * @return boolean
	 */
	public boolean removeKvPageProcessById(long identifier) {
		boolean isRemoved = false;
		if (null != this.kvPageProcesses) {
			int index = 0;
			KVPageProcess kvPage = null;
			for (KVPageProcess kvPageProcess : this.kvPageProcesses) {
				if (identifier == kvPageProcess.getId()) {
					kvPage = this.kvPageProcesses.get(index);
					isRemoved = this.kvPageProcesses.remove(kvPage);
					break;
				}
				index++;
			}
		}
		return isRemoved;
	}

	/**
	 * To get KV Page Process by Identifier.
	 * @param identifier String
	 * @return KVPageProcess
	 */
	public KVPageProcess getKVPageProcessbyIdentifier(String identifier) {
		KVPageProcess kvPageProcess1 = null;
		if (null != identifier && this.kvPageProcesses != null && !this.kvPageProcesses.isEmpty()) {
			for (KVPageProcess kvPageProcess : this.kvPageProcesses) {
				if (String.valueOf(kvPageProcess.getId()).equals(identifier)) {
					kvPageProcess1 = kvPageProcess;
				}
			}
		}
		return kvPageProcess1;
	}

	/**
	 * Cloning method.
	 * @return Object
	 * @throws CloneNotSupportedException in case of error
	 */
	public Object clone() throws CloneNotSupportedException {
		Object obj = null;
		obj = super.clone();
		return obj;
	}
}
