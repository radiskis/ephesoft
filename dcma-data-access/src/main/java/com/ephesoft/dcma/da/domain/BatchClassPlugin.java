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
 * Entity class for batch_class_plugin.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.model.common.AbstractChangeableEntity
 */
@Entity
@Table(name = "batch_class_plugin")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class BatchClassPlugin extends AbstractChangeableEntity implements Cloneable {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = -1758841380371327824L;

	/**
	 * batchClassModule BatchClassModule.
	 */
	@OneToOne
	@JoinColumn(name = "batch_class_module_id")
	private BatchClassModule batchClassModule;

	/**
	 * plugin Plugin.
	 */
	@OneToOne
	@JoinColumn(name = "plugin_id", nullable = false)
	private Plugin plugin;

	/**
	 * orderNumber int.
	 */
	@Column(name = "order_number")
	private int orderNumber;

	/**
	 * batchClassPluginConfigs List<BatchClassPluginConfig>.
	 */
	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "batch_class_plugin_id")
	@javax.persistence.OrderBy("id")
	private List<BatchClassPluginConfig> batchClassPluginConfigs;

	/**
	 * batchClassDynamicPluginConfigs List<BatchClassDynamicPluginConfig>.
	 */
	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "batch_class_plugin_id")
	@javax.persistence.OrderBy("id")
	private List<BatchClassDynamicPluginConfig> batchClassDynamicPluginConfigs;

	/**
	 * To get Order Number.
	 * @return int
	 */
	public int getOrderNumber() {
		return orderNumber;
	}

	/**
	 * To set Order Number.
	 * @param orderNumber int
	 */
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * To get Batch Class Module.
	 * @return BatchClassModule
	 */
	public BatchClassModule getBatchClassModule() {
		return batchClassModule;
	}

	/**
	 * To set Batch Class Module.
	 * @param batchClassModule BatchClassModule
	 */
	public void setBatchClassModule(BatchClassModule batchClassModule) {
		this.batchClassModule = batchClassModule;
	}

	/**
	 * To get Plugin.
	 * @return Plugin
	 */
	public Plugin getPlugin() {
		return plugin;
	}

	/**
	 * To set Plugin.
	 * @param plugin Plugin
	 */
	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * To get Batch Class Plugin Configs.
	 * @return List<BatchClassPluginConfig>
	 */
	public List<BatchClassPluginConfig> getBatchClassPluginConfigs() {
		return batchClassPluginConfigs;
	}

	/**
	 * To set Batch Class Plugin Configs.
	 * @param batchClassPluginConfigs List<BatchClassPluginConfig>
	 */
	public void setBatchClassPluginConfigs(List<BatchClassPluginConfig> batchClassPluginConfigs) {
		this.batchClassPluginConfigs = batchClassPluginConfigs;
	}

	/**
	 * To get Batch Class Plugin Configuration by Id.
	 * @param identifier long
	 * @return BatchClassPluginConfig
	 */
	public BatchClassPluginConfig getBatchClassPluginConfigById(long identifier) {
		BatchClassPluginConfig pluginConfig = null;
		if (getBatchClassPluginConfigs() != null && getBatchClassPluginConfigs().size() > 0) {
			for (BatchClassPluginConfig batchClassPluginConfig : getBatchClassPluginConfigs()) {
				if (batchClassPluginConfig.getId() == identifier) {
					pluginConfig = batchClassPluginConfig;
					break;
				}
			}
		}
		return pluginConfig;
	}

	/**
	 * To add Batch Class Plugin Configuration.
	 * @param config BatchClassPluginConfig
	 */
	public void addBatchClassPluginConfig(BatchClassPluginConfig config) {
		if (this.getBatchClassPluginConfigs() == null) {
			setBatchClassPluginConfigs(new ArrayList<BatchClassPluginConfig>());
		}
		if (config.getId() == 0) {
			this.getBatchClassPluginConfigs().add(config);
		} else {
			removeBatchClassPluginConfig(config);
			getBatchClassPluginConfigs().add(config);
		}
	}

	/**
	 * To remove Batch Class Plugin Configuartion.
	 * @param config BatchClassPluginConfig
	 */
	public void removeBatchClassPluginConfig(BatchClassPluginConfig config) {
		int index = -1;
		int cnt = 0;
		for (BatchClassPluginConfig batchClassPluginConfig : getBatchClassPluginConfigs()) {
			if (batchClassPluginConfig.getId() == config.getId()) {
				index = cnt;
				break;
			}
			cnt++;
		}
		getBatchClassPluginConfigs().remove(index);
	}

	/**
	 * To get Batch Class Dynamic Plugin Configs.
	 * @return List<BatchClassDynamicPluginConfig>
	 */
	public List<BatchClassDynamicPluginConfig> getBatchClassDynamicPluginConfigs() {
		return batchClassDynamicPluginConfigs;
	}

	/**
	 *  To set Batch Class Dynamic Plugin Configs.
	 * @param batchClassDynamicPluginConfigs List<BatchClassDynamicPluginConfig>
	 */
	public void setBatchClassDynamicPluginConfigs(List<BatchClassDynamicPluginConfig> batchClassDynamicPluginConfigs) {
		this.batchClassDynamicPluginConfigs = batchClassDynamicPluginConfigs;
	}

	/**
	 * To get Batch Class Dynamic Plugin Configuration by Id.
	 * @param identifier long
	 * @return BatchClassDynamicPluginConfig
	 */
	public BatchClassDynamicPluginConfig getBatchClassDynamicPluginConfigById(long identifier) {
		BatchClassDynamicPluginConfig dynamicPluginConfig = null;
		if (getBatchClassDynamicPluginConfigs() != null && getBatchClassDynamicPluginConfigs().size() > 0) {
			for (BatchClassDynamicPluginConfig batchClassDynamicPluginConfig : getBatchClassDynamicPluginConfigs()) {
				if (batchClassDynamicPluginConfig.getId() == identifier) {
					dynamicPluginConfig = batchClassDynamicPluginConfig;
					break;
				}
			}
		}
		return dynamicPluginConfig;
	}

	/**
	 * To add Batch Class Dynamic Plugin Configuration.
	 * @param config BatchClassDynamicPluginConfig
	 */
	public void addBatchClassDynamicPluginConfig(BatchClassDynamicPluginConfig config) {
		if (this.getBatchClassDynamicPluginConfigs() == null) {
			setBatchClassDynamicPluginConfigs(new ArrayList<BatchClassDynamicPluginConfig>());
		}
		if (config.getId() == 0) {
			this.getBatchClassDynamicPluginConfigs().add(config);
		} else {
			removeBatchClassDynamicPluginConfig(config);
			getBatchClassDynamicPluginConfigs().add(config);
		}
	}

	/**
	 * To remove Batch Class Dynamic Plugin Configuration.
	 * @param config BatchClassDynamicPluginConfig
	 */ 
	public void removeBatchClassDynamicPluginConfig(BatchClassDynamicPluginConfig config) {
		int index = -1;
		int cnt = 0;
		for (BatchClassDynamicPluginConfig batchClassDynamicPluginConfig : getBatchClassDynamicPluginConfigs()) {
			if (batchClassDynamicPluginConfig.getId() == config.getId()) {
				index = cnt;
				break;
			}
			cnt++;
		}
		getBatchClassDynamicPluginConfigs().remove(index);
	}

	/**
	 * Cloning method.
	 * @return Object
	 * @throws CloneNotSupportedException
	 */
	public Object clone() throws CloneNotSupportedException {
		Object obj = null;
		obj = super.clone();
		return obj;
	}
}
