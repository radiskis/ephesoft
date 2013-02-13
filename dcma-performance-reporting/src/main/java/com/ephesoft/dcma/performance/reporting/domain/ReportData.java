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

package com.ephesoft.dcma.performance.reporting.domain;

import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

import com.ephesoft.dcma.core.common.WorkflowType;

/**
 * This class contains the report data.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.common.WorkflowType
 */
public class ReportData {

	/**
	 * identifier long.
	 */
	private long identifier;

	/**
	 * processId String.
	 */
	private String processId;

	/**
	 * dbId BigInteger.
	 */
	private BigInteger dbId;

	/**
	 * workflowId String.
	 */
	private String workflowId;

	/**
	 * processKey String.
	 */
	private String processKey;

	/**
	 * startTime Date.
	 */
	private Date startTime;

	/**
	 * endTime Date.
	 */
	private Date endTime;

	/**
	 * duration BigInteger.
	 */
	private BigInteger duration;

	/**
	 * totalNumberPages BigInteger.
	 */
	private BigInteger totalNumberPages;

	/**
	 * totalNumberDocuments BigInteger.
	 */
	private BigInteger totalNumberDocuments;

	/**
	 * users Set<ManualStepsData>.
	 */
	private Set<ManualStepsData> users;

	/**
	 * workflowName String.
	 */
	private String workflowName;

	/**
	 * batchInstanceId String.
	 */
	private String batchInstanceId;

	/**
	 * workflowType Enum<WorkflowType>.
	 */
	private Enum<WorkflowType> workflowType;
	
	/**
	 * userName String.
	 */
	private String userName;

	/**
	 * To get Batch Instance Id.
	 * @return String
	 */
	public String getBatchInstanceId() {
		return batchInstanceId;
	}

	/**
	 * To set Batch Instance Id. 
	 * @param batchInstanceId String
	 */
	public void setBatchInstanceId(String batchInstanceId) {
		this.batchInstanceId = batchInstanceId;
	}

	/**
	 * To get Id.
	 * @return long
	 */
	public long getId() {
		return identifier;
	}

	/**
	 * To set Id.
	 * @param identifier long
	 */
	public void setId(long identifier) {
		this.identifier = identifier;
	}

	/**
	 * To get Process Id.
	 * @return String
	 */
	public String getProcessId() {
		return processId;
	}

	/**
	 * To set Process Id.
	 * @param processId String
	 */
	public void setProcessId(String processId) {
		this.processId = processId;
	}

	/**
	 * To get Db Id.
	 * @return BigInteger
	 */
	public BigInteger getDbId() {
		return dbId;
	}

	/**
	 * To set Db Id.
	 * @param dbId BigInteger
	 */
	public void setDbId(BigInteger dbId) {
		this.dbId = dbId;
	}

	/**
	 * To get Workflow Id.
	 * @return String
	 */
	public String getWorkflowId() {
		return workflowId;
	}

	/**
	 * To set Workflow Id.
	 * @param workflowId String
	 */
	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}

	/**
	 * To get Process Key.
	 * @return String
	 */
	public String getProcessKey() {
		return processKey;
	}

	/**
	 * To set Process Key.
	 * @param processKey String
	 */
	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}

	/**
	 * To get start time.
	 * @return Date
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * To set start time.
	 * @param startTime Date
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * To get end time.
	 * @return Date
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * To set end time.
	 * @param endTime Date
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * To get duration.
	 * @return BigInteger
	 */
	public BigInteger getDuration() {
		return duration;
	}

	/**
	 * To set duration.
	 * @param duration BigInteger
	 */
	public void setDuration(BigInteger duration) {
		this.duration = duration;
	}

	/**
	 * To get Total Number of pages.
	 * @return BigInteger
	 */
	public BigInteger getTotalNumberPages() {
		return totalNumberPages;
	}

	/**
	 * To set Total Number of pages.
	 * @param totalNumberPages BigInteger
	 */
	public void setTotalNumberPages(BigInteger totalNumberPages) {
		this.totalNumberPages = totalNumberPages;
	}

	/**
	 * To get Total Number of Documents.
	 * @return BigInteger
	 */
	public BigInteger getTotalNumberDocuments() {
		return totalNumberDocuments;
	}

	/**
	 * To set Total Number of Documents.
	 * @param totalNumberDocuments BigInteger
	 */
	public void setTotalNumberDocuments(BigInteger totalNumberDocuments) {
		this.totalNumberDocuments = totalNumberDocuments;
	}
	
    /**
     * To get Work flow name.
     * @return String
     */
	public String getWorkflowName() {
		return workflowName;
	}

	/**
	 * To set Work flow name.
	 * @param workflowName String
	 */
	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	/**
	 * To get Work flow Type.
	 * @return Enum<WorkflowType>
	 */
	public Enum<WorkflowType> getWorkflowType() {
		return workflowType;
	}

	/**
	 * To set Work flow Type.
	 * @param workflowType Enum<WorkflowType>
	 */
	public void setWorkflowType(Enum<WorkflowType> workflowType) {
		this.workflowType = workflowType;
	}

	/**
	 * To set user.
	 * @param users Set<ManualStepsData>
	 */
	public void setUsers(Set<ManualStepsData> users) {
		this.users = users;
	}

	/**
	 * To get users.
	 * @return the users
	 */
	public Set<ManualStepsData> getUsers() {
		return users;
	}

	/**
	 * To set user name.
	 * @param userName String
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * To get user name.
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
}
