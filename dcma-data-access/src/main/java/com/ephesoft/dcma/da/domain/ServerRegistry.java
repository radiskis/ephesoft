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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;

/**
 * Entity class for server_registry.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.model.common.AbstractChangeableEntity
 */
@Entity
@Table(name = "server_registry")
public class ServerRegistry extends AbstractChangeableEntity {

	/**
	 * Serial version UID long.
	 */
	private static final long serialVersionUID = 6213724039823490819L;

	/**
	 * ipAddress String.
	 */
	@Column(name = "ip_address")
	private String ipAddress;

	/**
	 * port String.
	 */
	@Column(name = "port_number")
	private String port;

	/**
	 * appContext String.
	 */
	@Column(name = "app_context")
	private String appContext;

	/**
	 * active boolean.
	 */
	@Column(name = "is_active")
	private boolean active;

	/**
	 * lastUpdatedBatchTime Date.
	 */
	@Column(name = "last_updated_batch_time")
	private Date lastUpdatedBatchTime;

	/**
	 * To get IP Address.
	 * @return String
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * To set IP Address.
	 * @param ipAddress String
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * To get port.
	 * @return String
	 */
	public String getPort() {
		return port;
	}

	/**
	 * To set port.
	 * @param port String
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * To get Application Context.
	 * @return String
	 */
	public String getAppContext() {
		return appContext;
	}

	/**
	 * To set Application Context.
	 * @param appContext String
	 */
	public void setAppContext(String appContext) {
		this.appContext = appContext;
	}

	/**
	 * To check whether active or not.
	 * @return boolean
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * To set active.
	 * @param active boolean
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * To get Last Updated Batch Time.
	 * @return Date
	 */
	public Date getLastUpdatedBatchTime() {
		return lastUpdatedBatchTime;
	}

	/**
	 * To set Last Updated Batch Time.
	 * @param lastUpdatedBatchTime Date
	 */
	public void setLastUpdatedBatchTime(Date lastUpdatedBatchTime) {
		this.lastUpdatedBatchTime = lastUpdatedBatchTime;
	}

}
