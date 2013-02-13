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

package com.ephesoft.dcma.gwt.core.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Class that stores a record's information on client side.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.google.gwt.user.client.rpc.IsSerializable
 */

public class BatchInstanceDTO implements IsSerializable {

	/**
	 * The priority of this batch.
	 */
	private int priority;

	/**
	 * The unique identifier associated with each batch.
	 */
	private String batchIdentifier;

	/**
	 * Name of the batch.
	 */
	private String batchName;

	/**
	 * Name of the batch class to which this batch belongs to.
	 */
	private String batchClassName;

	/**
	 * Date and time when this batch was uploaded.
	 */
	private String uploadedOn;

	/**
	 * Date and time when this batch was created.
	 */
	private String createdOn;

	/**
	 * No of documents present in this batch.
	 */
	private String noOfDocuments;

	/**
	 * Review status of the batch.
	 */
	private String reviewStatus;

	/**
	 * Validation status of this batch.
	 */
	private String validationStatus;

	/**
	 * Total number of pages in the batch.
	 */
	private String noOfPages;

	/**
	 * Status of the batch. Running, new, ready for review etc.
	 */
	private String status;

	/**
	 * Current user who has locked the batch.
	 */
	private String currentUser;

	/**
	 * Variable for isRemote.
	 */
	private boolean remote;

	/**
	 * Variable for executedModules.
	 */
	private String executedModules;

	/**
	 * To get Status.
	 * 
	 * @return String
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * To set Status.
	 * 
	 * @param status String
	 */
	public void setStatus(final String status) {
		this.status = status;
	}

	/**
	 * To get Priority.
	 * 
	 * @return int
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * To set Priority.
	 * 
	 * @param priority int
	 */
	public void setPriority(final int priority) {
		this.priority = priority;
	}

	/**
	 * To get Batch Identifier.
	 * 
	 * @return String
	 */
	public String getBatchIdentifier() {
		return batchIdentifier;
	}

	/**
	 * To set Batch Identifier.
	 * 
	 * @param batchIdentifier String
	 */
	public void setBatchIdentifier(final String batchIdentifier) {
		this.batchIdentifier = batchIdentifier;
	}

	/**
	 * To get Batch Name.
	 * 
	 * @return String
	 */
	public String getBatchName() {
		return batchName;
	}

	/**
	 * To set Batch Name.
	 * 
	 * @param batchName String
	 */
	public void setBatchName(final String batchName) {
		this.batchName = batchName;
	}

	/**
	 * To get Uploaded On.
	 * 
	 * @return String
	 */
	public String getUploadedOn() {
		return uploadedOn;
	}

	/**
	 * To set Uploaded On.
	 * 
	 * @param uploadedOn String
	 */
	public void setUploadedOn(final String uploadedOn) {
		this.uploadedOn = uploadedOn;
	}

	/**
	 * To get No Of Documents.
	 * 
	 * @return String
	 */
	public String getNoOfDocuments() {
		return noOfDocuments;
	}

	/**
	 * To set No Of Documents.
	 * 
	 * @param noOfDocuments String
	 */
	public void setNoOfDocuments(final String noOfDocuments) {
		this.noOfDocuments = noOfDocuments;
	}

	/**
	 * To get Review Status.
	 * 
	 * @return String
	 */
	public String getReviewStatus() {
		return reviewStatus;
	}

	/**
	 * To set Review Status.
	 * 
	 * @param reviewStatus String
	 */
	public void setReviewStatus(final String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	/**
	 * To get Validation Status.
	 * 
	 * @return String
	 */
	public String getValidationStatus() {
		return validationStatus;
	}

	/**
	 * To set Validation Status.
	 * 
	 * @param validationStatus String
	 */
	public void setValidationStatus(final String validationStatus) {
		this.validationStatus = validationStatus;
	}

	/**
	 * To get No Of Pages.
	 * 
	 * @return String
	 */
	public String getNoOfPages() {
		return noOfPages;
	}

	/**
	 * To set No Of Pages.
	 * 
	 * @param noOfPages String
	 */
	public void setNoOfPages(final String noOfPages) {
		this.noOfPages = noOfPages;
	}

	/**
	 * To get Batch Class Name.
	 * 
	 * @return String
	 */
	public String getBatchClassName() {
		return batchClassName;
	}

	/**
	 * To set Batch Class Name.
	 * 
	 * @param batchClassName String
	 */
	public void setBatchClassName(final String batchClassName) {
		this.batchClassName = batchClassName;
	}

	/**
	 * To check whether remote.
	 * 
	 * @return boolean
	 */
	public boolean isRemote() {
		return remote;
	}

	/**
	 * To set remote.
	 * 
	 * @param isRemote boolean
	 */
	public void setRemote(final boolean remote) {
		this.remote = remote;
	}

	/**
	 * remoteBatchInstanceDTO RemoteBatchInstanceDTO.
	 */
	private RemoteBatchInstanceDTO remoteBatchInstanceDTO;

	/**
	 * To get Remote Batch Instance DTO.
	 * 
	 * @return RemoteBatchInstanceDTO
	 */
	public RemoteBatchInstanceDTO getRemoteBatchInstanceDTO() {
		return remoteBatchInstanceDTO;
	}

	/**
	 * To set Remote Batch Instance DTO.
	 * 
	 * @param remoteBatchInstanceDTO RemoteBatchInstanceDTO
	 */
	public void setRemoteBatchInstanceDTO(RemoteBatchInstanceDTO remoteBatchInstanceDTO) {
		this.remoteBatchInstanceDTO = remoteBatchInstanceDTO;
	}

	/**
	 * To get Executed Modules.
	 * 
	 * @param String
	 */
	public String getExecutedModules() {
		return executedModules;
	}

	/**
	 * To set Executed Modules.
	 * 
	 * @param executedModules String
	 */
	public void setExecutedModules(String executedModules) {
		this.executedModules = executedModules;
	}

	/**
	 * To get Current User.
	 * 
	 * @return String
	 */
	public String getCurrentUser() {
		return currentUser;
	}

	/**
	 * To get Current User.
	 * 
	 * @param currentUser String
	 */
	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * To get Created On.
	 * 
	 * @return String
	 */
	public String getCreatedOn() {
		return createdOn;
	}

	/**
	 * To set Created On.
	 * 
	 * @param createdOn String
	 */
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

}
