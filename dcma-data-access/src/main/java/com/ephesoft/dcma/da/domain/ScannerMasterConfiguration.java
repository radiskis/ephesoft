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
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;

/**
 * Entity class for scanner_master_configuration.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.model.common.AbstractChangeableEntity
 */
@Entity
@Table(name = "scanner_master_configuration")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class ScannerMasterConfiguration extends AbstractChangeableEntity implements Serializable {

	/**
	 * Serial version UID long.
	 */
	private static final long serialVersionUID = 6869598804288921738L;

	/**
	 * sampleValue List<ScannerMasterConfigSampleValue>.
	 */
	@OneToMany
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "master_config_id")
	private List<ScannerMasterConfigSampleValue> sampleValue;

	/**
	 * name String.
	 */
	@Column(name = "config_name", unique = true)
	private String name;

	/**
	 * type String.
	 */
	@Column(name = "config_data_type")
	private String type;

	/**
	 * mandatory Boolean.
	 */
	@Column(name = "is_mandatory")
	private Boolean mandatory;

	/**
	 * description String.
	 */
	@Column(name = "description")
	private String description;

	/**
	 * multiValue Boolean.
	 */
	@Column(name = "config_multivalue")
	private Boolean multiValue;

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
	 * To get Type.
	 * @return String
	 */
	public String getType() {
		return type;
	}

	/**
	 * To set Type.
	 * @param type String
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * To set Mandatory.
	 * @param mandatory Boolean
	 */
	public void setMandatory(Boolean mandatory) {
		if (mandatory == null) {
			this.mandatory = Boolean.FALSE;
		} else {
			this.mandatory = mandatory;
		}
	}

	/**
	 * To check whether mandatory or not.
	 * @return boolean
	 */
	public boolean isMandatory() {
		if(this.mandatory == null) {
			return false;
		} else {
			return this.mandatory;
		}
	}

	/**
	 * To set Description.
	 * @param description String
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * To get Description.
	 * @return String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * To get Sample Value.
	 * @return List<String>
	 */
	public List<String> getSampleValue() {
		List<String> returnList = new ArrayList<String>();
		if (sampleValue != null && !sampleValue.isEmpty()) {
			for (ScannerMasterConfigSampleValue scannerMasterConfigSampleValue : sampleValue) {
				returnList.add(scannerMasterConfigSampleValue.getSampleValue());
			}
		}
		return returnList;
	}

	/**
	 * To check whether multivalue or not.
	 * @return boolean
	 */
	public boolean isMultiValue() {
		if(this.multiValue == null) {
			return false;
		} else {
			return this.multiValue;
		}
	}

	/**
	 * To set MultiValue.
	 * @param multiValue Boolean
	 */
	public void setMultiValue(Boolean multiValue) {
		if(multiValue == null) {
			this.multiValue = Boolean.FALSE;
		} else {
			this.multiValue = multiValue;
		}
		
	}

}
