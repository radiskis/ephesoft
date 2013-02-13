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
 * Entity class for batch_class_dynamic_plugin_config.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.model.common.AbstractChangeableEntity
 */
@Entity
@Table(name = "batch_class_dynamic_plugin_config")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class BatchClassDynamicPluginConfig extends AbstractChangeableEntity implements Cloneable {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = -3994727578168859843L;

	/**
	 * name String.
	 */
	@Column(name = "config_name")
	private String name;

	/**
	 * description String.
	 */
	@Column(name = "config_desc")
	private String description;

	/** 
	 * batchClassPlugin BatchClassPlugin.
	 */
	@OneToOne
	@JoinColumn(name = "batch_class_plugin_id")
	private BatchClassPlugin batchClassPlugin;

	/**
	 * parent BatchClassDynamicPluginConfig.
	 */  
	@OneToOne
	@JoinColumn(name = "parent_id")
	private BatchClassDynamicPluginConfig parent;

	/**
	 * children List<BatchClassDynamicPluginConfig>.
	 */
	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "parent_id")
	private List<BatchClassDynamicPluginConfig> children;

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
	 * To get Parent.
	 * @return BatchClassDynamicPluginConfig
	 */
	public BatchClassDynamicPluginConfig getParent() {
		return parent;
	}

	/**
	 * To set Parent.
	 * @param parent BatchClassDynamicPluginConfig
	 */
	public void setParent(BatchClassDynamicPluginConfig parent) {
		this.parent = parent;
	}

	/**
	 * To get Children.
	 * @return List<BatchClassDynamicPluginConfig>
	 */
	public List<BatchClassDynamicPluginConfig> getChildren() {
		return children;
	}

	/**
	 * To set Children.
	 * @param children List<BatchClassDynamicPluginConfig>
	 */
	public void setChildren(List<BatchClassDynamicPluginConfig> children) {
		this.children = children;
	}

	/**
	 * To get Name.
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * To set name.
	 * @param name String
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * To get Value.
	 * @return String
	 */
	public String getValue() {
		return value;
	}

	/**
	 * To set Value.
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
	 * To add child.
	 * @param config BatchClassDynamicPluginConfig
	 */
	public void addChild(BatchClassDynamicPluginConfig config) {
		if (this.getChildren() == null) {
			setChildren(new ArrayList<BatchClassDynamicPluginConfig>());
		}
		if (config.getId() == 0) {
			this.getChildren().add(config);
		} else {
			removeChild(config);
			this.getChildren().add(config);
		}
	}

	/**
	 * To remove child.
	 * @param config BatchClassDynamicPluginConfig
	 */
	public void removeChild(BatchClassDynamicPluginConfig config) {
		int index = -1;
		int cnt = 0;
		for (BatchClassDynamicPluginConfig batchClassDynamicPluginConfig : getChildren()) {
			if (batchClassDynamicPluginConfig.getId() == config.getId()) {
				index = cnt;
				break;
			}
			cnt++;
		}
		this.getChildren().remove(index);
	}

	/**
	 * Clone method.
	 * @return Object
	 * @throws CloneNotSupportedException
	 */
	public Object clone() throws CloneNotSupportedException {
		Object obj = null;
		obj = super.clone();
		return obj;
	}
}
