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

/**
 * This class contains manual steps data.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public class ManualStepsData {

	/**
	 * identifier long.
	 */
	private long identifier;

	/**
	 * userName String.
	 */
	private String userName;

	/**
	 * batchInstanceId String.
	 */
	private String batchInstanceId;

	/**
	 * batchStatus String.
	 */
	private String batchStatus;

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
	 * reportData ReportData.
	 */
	private ReportData reportData;

	/**
	 * To set id.
	 * @param identifier 
	 */
	public void setId(long identifier) {
		this.identifier = identifier;
	}

	/**
	 * To get id.
	 * @return the id
	 */
	public long getId() {
		return identifier;
	}

	/**
	 * To set user name.
	 * @param userName 
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

	/**
	 * To set Batch Instance Id.
	 * @param batchInstanceId 
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
	 * To set batch status. 
	 * @param batchStatus
	 */
	public void setBatchStatus(String batchStatus) {
		this.batchStatus = batchStatus;
	}

	/**
	 * To get batch status. 
	 * @return the batchStatus
	 */
	public String getBatchStatus() {
		return batchStatus;
	}

	/**
	 * To set start time.
	 * @param startTime 
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * To get start time.
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * To set end time.
	 * @param endTime
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * To get end time.
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * To set duration.
	 * @param duration 
	 */
	public void setDuration(BigInteger duration) {
		this.duration = duration;
	}

	/**
	 * To get duration.
	 * @return the duration
	 */
	public BigInteger getDuration() {
		return duration;
	}

	/**
	 * To set the report data.
	 * @param reportData 
	 */
	public void setReportData(ReportData reportData) {
		this.reportData = reportData;
	}

	/**
	 * To get report data.
	 * @return the reportData
	 */
	public ReportData getReportData() {
		return reportData;
	}

}
