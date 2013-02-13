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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NaturalId;

import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;

/**
 * Entity class for plugin_config.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.model.common.AbstractChangeableEntity
 */
@Entity
@Table(name = "plugin_config")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class PluginConfig extends AbstractChangeableEntity {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = -1959019321092627830L;

	/**
	 * plugin Plugin.
	 */
	@ManyToOne
	@JoinColumn(name = "plugin_id")
	private Plugin plugin;

	/**
	 * name String.
	 */
	@Column(name = "config_name")
	@NaturalId(mutable = true)
	private String name;

	/**
	 * description String.
	 */
	@Column(name = "config_desc")
	private String description;

	/**
	 * sampleValue List<PluginConfigSampleValue>.
	 */
	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "plugin_config_id")
	private List<PluginConfigSampleValue> sampleValue;

	/**
	 * dataType DataType.
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "config_datatype")
	private DataType dataType;

	/**
	 * multiValue Boolean.
	 */
	@Column(name = "config_multivalue")
	private Boolean multiValue;

	/**
	 * mandatory boolean.
	 */
	@Column(name = "is_mandatory", columnDefinition = "bit default 1")
	private boolean mandatory;

	/**
	 * orderNumber Integer.
	 */
	@Column(name = "order_number")
	private Integer orderNumber;

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
	 * To get name.
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
	 * To get Sample Value.
	 * @return List<String>
	 */
	public List<String> getSampleValue() {
		List<String> returnList = new ArrayList<String>();
		if (sampleValue != null && !sampleValue.isEmpty()) {
			for (PluginConfigSampleValue pluginConfigSampleValue : sampleValue) {
				returnList.add(pluginConfigSampleValue.getSampleValue());
			}
		}
		return returnList;
	}
	
	/**
	 * To get Plugin Config Sample Values.
	 * @return List<PluginConfigSampleValue>
	 */
	public List<PluginConfigSampleValue> getPluginConfigSampleValues(){
		return sampleValue;
	}

	/**
	 * To set Plugin Config Sample Values.
	 * @param sampleValue List<PluginConfigSampleValue>
	 */
	public void setPluginConfigSampleValues(List<PluginConfigSampleValue> sampleValue) {
		this.sampleValue = sampleValue;
	}

	/**
	 * To get Data Type.
	 * @return DataType
	 */
	public DataType getDataType() {
		return dataType;
	}

	/**
	 * To set Data Type.
	 * @param dataType DataType
	 */
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	/**
	 * To check whether multivalue.
	 * @return Boolean
	 */
	public Boolean isMultiValue() {
		return multiValue;
	}

	/**
	 * To set MultiValue.
	 * @param multiValue Boolean
	 */
	public void setMultiValue(Boolean multiValue) {
		this.multiValue = multiValue;
	}

	/**
	 * To check whether mandatory or not.
	 * @return boolean
	 */
	public boolean isMandatory() {
		return mandatory;
	}

	/**
	 * To set Mandatory.
	 * @param mandatory boolean
	 */
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	/**
	 * To set Order Number.
	 * @param orderNumber Integer
	 */ 
	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * To get Order Number.
	 * @return Integer
	 */
	public Integer getOrderNumber() {
		return orderNumber;
	}

}
