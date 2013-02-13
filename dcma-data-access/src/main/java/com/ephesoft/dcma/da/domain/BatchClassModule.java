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
 * Entity class for batch_class_module.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.model.common.AbstractChangeableEntity
 */
@Entity
@Table(name = "batch_class_module")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class BatchClassModule extends AbstractChangeableEntity {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = -5366469561526543577L;

	/**
	 * batchClass BatchClass.
	 */
	@OneToOne
	@JoinColumn(name = "batch_class_id")
	private BatchClass batchClass;

	/** 
	 * module Module.
	 */
	@OneToOne
	@JoinColumn(name = "module_id", nullable = false)
	private Module module;

	/**
	 * orderNumber int.
	 */
	@Column(name = "order_number")
	private int orderNumber;

	/**
	 * workflowName String.
	 */
	@Column(name = "workflow_name")
	private String workflowName;

	/**
	 * remoteURL String.
	 */
	@Column(name = "remote_url")
	private String remoteURL;

	/**
	 * remoteBatchClassIdentifier String.
	 */
	@Column(name = "remote_batch_class_id")
	private String remoteBatchClassIdentifier;

	/**
	 * batchClassPlugins List<BatchClassPlugin>.
	 */
	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "batch_class_module_id")
	@javax.persistence.OrderBy("orderNumber")
	private List<BatchClassPlugin> batchClassPlugins;

	/**
	 * batchClassModuleConfig List<BatchClassModuleConfig>.
	 */
	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "batch_class_module_id")
	private List<BatchClassModuleConfig> batchClassModuleConfig;

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
	 * To get Batch Class.
	 * @return BatchClass
	 */
	public BatchClass getBatchClass() {
		return batchClass;
	}

	/**
	 * To get Batch Class.
	 * @param batchClass
	 */
	public void setBatchClass(BatchClass batchClass) {
		this.batchClass = batchClass;
	}

	/**
	 * To get Module.
	 * @return Module
	 */
	public Module getModule() {
		return module;
	}

	/**
	 * To set Module.
	 * @param module Module
	 */
	public void setModule(Module module) {
		this.module = module;
	}

	/**
	 * To get Batch Class Plugins.
	 * @return List<BatchClassPlugin>
	 */
	public List<BatchClassPlugin> getBatchClassPlugins() {
		return batchClassPlugins;
	}

	/**
	 * To set Batch Class Plugins.
	 * @param batchClassPlugins List<BatchClassPlugin>
	 */
	public void setBatchClassPlugins(List<BatchClassPlugin> batchClassPlugins) {
		this.batchClassPlugins = batchClassPlugins;
	}

	/**
	 * To get Batch Class Plugin by Id.
	 * @param identifier long
	 * @return BatchClassPlugin
	 */
	public BatchClassPlugin getBatchClassPluginById(long identifier) {
		BatchClassPlugin plugin = null;
		if (getBatchClassPlugins() != null && getBatchClassPlugins().size() > 0) {
			for (BatchClassPlugin batchClassPlugin : getBatchClassPlugins()) {
				if (batchClassPlugin.getId() == identifier) {
					plugin = batchClassPlugin;
					break;
				}
			}
		}
		return plugin;
	}

	/**
	 * To remove Batch Class Plugin By Id.
	 * @param identifier long
	 */
	public void removeBatchClassPluginById(long identifier) {
		BatchClassPlugin plugin = getBatchClassPluginById(identifier);
		batchClassPlugins.remove(plugin);
	}

	/**
	 * To get Workflow Name.
	 * @return String
	 */
	public String getWorkflowName() {
		return workflowName;
	}

	/**
	 * To set Workflow Name.
	 * @param workflowName String
	 */
	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	/**
	 * To get Batch Class Module Configuration.
	 * @return List<BatchClassModuleConfig>
	 */
	public List<BatchClassModuleConfig> getBatchClassModuleConfig() {
		return batchClassModuleConfig;
	}

	/**
	 * To set Batch Class Module Configuration.
	 * @param batchClassModuleConfig List<BatchClassModuleConfig>
	 */
	public void setBatchClassModuleConfig(List<BatchClassModuleConfig> batchClassModuleConfig) {
		this.batchClassModuleConfig = batchClassModuleConfig;
	}

	/**
	 * To set Remote Batch Class Identifier.
	 * @param remoteBatchClassIdentifier String
	 */
	public void setRemoteBatchClassIdentifier(String remoteBatchClassIdentifier) {
		this.remoteBatchClassIdentifier = remoteBatchClassIdentifier;
	}

	/**
	 * To get Remote Batch Class Identifier.
	 * @return String
	 */
	public String getRemoteBatchClassIdentifier() {
		return remoteBatchClassIdentifier;
	}

	/**
	 * To set remote URL.
	 * @param remoteURL String
	 */
	public void setRemoteURL(String remoteURL) {
		this.remoteURL = remoteURL;
	}

	/**
	 * To get remote URL.
	 * @return String
	 */
	public String getRemoteURL() {
		return remoteURL;
	}
}
