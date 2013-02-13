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
 * Entity class for hist_manual_steps_in_workflow.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.model.common.AbstractChangeableEntity
 */
@Entity
@Table(name = "hist_manual_steps_in_workflow")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class ManualStepHistoryInWorkflow extends AbstractChangeableEntity {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * userName String.
	 */
	@Column(name = "user_name")
	private String userName;

	/**
	 * batchInstanceId String.
	 */
	@Column(name = "batch_instance_id")
	private String batchInstanceId;
	
	/**
	 * batchInstanceStatus String.
	 */
	@Column(name = "batch_instance_status")
	private String batchInstanceStatus;
	
	/**
	 * startTime Date.
	 */
	@Column(name = "start_time")
	private Date startTime;

	/**
	 * endTime Date.
	 */
	@Column(name = "end_time")
	private Date endTime;

	/**
	 * duration long.
	 */
	@Column(name = "duration")
	private long duration;

	/**
	 * To set User Name.
	 * @param userName String
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * To get User Name.
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * To set Batch Instance Id.
	 * @param batchInstanceId String
	 */
	public void setBatchInstanceId(String batchInstanceId) {
		this.batchInstanceId = batchInstanceId;
	}

	/**
	 * To get Batch Instance Id.
	 * @return the batchInstanceId
	 */
	public String getBatchInstanceId() {
		return batchInstanceId;
	}

	/**
	 * To set Start Time.
	 * @param startTime Date
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * To get Start Time.
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * To set End Time.
	 * @param endTime Date
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * To get End Time.
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * To set batch Instance Status.
	 * @param batchInstanceStatus String
	 */
	public void setBatchInstanceStatus(String batchInstanceStatus) {
		this.batchInstanceStatus = batchInstanceStatus;
	}

	/**
	 * To get batch Instance Status.
	 * @return the batchInstanceStatus
	 */
	public String getBatchInstanceStatus() {
		return batchInstanceStatus;
	}

	/**
	 * To set Duration.
	 * @param duration long
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}

	/**
	 * To get Duration.
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}

}
