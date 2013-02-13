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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ephesoft.dcma.core.EphesoftProperty;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;
import com.ephesoft.dcma.da.constant.DataAccessConstant;
import com.ephesoft.dcma.da.id.BatchInstanceID;

/**
 * Entity class for batch_instance.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.model.common.AbstractChangeableEntity
 */
@Entity
@Table(name = "batch_instance")
public class BatchInstance extends AbstractChangeableEntity {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = 4777280763161216105L;

	/**
	 * batchInstanceID BatchInstanceID.
	 */
	private transient BatchInstanceID batchInstanceID;

	/**
	 * batchClass BatchClass.
	 */
	@ManyToOne
	@JoinColumn(name = "batch_class_id")
	private BatchClass batchClass;

	/**
	 * reviewUserName String.
	 */
	@Column(name = "review_operator_user_name")
	private String reviewUserName;

	/**
	 * validationUserName String.
	 */
	@Column(name = "validation_operator_user_name")
	private String validationUserName;

	/**
	 * priority int.
	 */
	@Column(name = "batch_priority")
	private int priority;

	/**
	 * identifier String.
	 */
	@Column(name = "identifier")
	private String identifier;

	/**
	 * status BatchInstanceStatus.
	 */ 
	@Enumerated(EnumType.STRING)
	@Column(name = "batch_status")
	private BatchInstanceStatus status;

	/**
	 * uncSubfolder String.
	 */
	@Column(name = "unc_subfolder")
	private String uncSubfolder;

	/**
	 * localFolder String.
	 */
	@Column(name = "local_folder")
	private String localFolder;

	/**
	 * currentUser String.
	 */
	@Column(name = "curr_user")
	private String currentUser;

	/**
	 * batchName String.
	 */  
	@Column(name = "batch_name")
	private String batchName;

	/**
	 * remote boolean.
	 */
	@Column(name = "is_remote", columnDefinition = "bit default 0")
	private boolean remote;

	/**
	 * executedModules String.
	 */
	@Column(name = "executed_modules")
	private String executedModules;
	
	/**
	 * serverIP String.
	 */
	@Column(name = "server_ip")
	private String serverIP;
	
	/**
	 * executingServer String.
	 */
	@Column(name = "executing_server")
	private String executingServer;

	/**
	 * lockOwner ServerRegistry.
	 */
	@ManyToOne
	@JoinColumn(name = "lock_owner")
	private ServerRegistry lockOwner;

	/**
	 * remoteBatchInstance RemoteBatchInstance.
	 */
	@OneToOne
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "remote_batch_instance_id")
	private RemoteBatchInstance remoteBatchInstance;

	/**
	 * To get Priority.
	 * @return int
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * To add variables other than in pojo.
	 */
	public void postPersist() {
		super.postPersist();
		this.identifier = EphesoftProperty.BATCH_INSTANCE.getProperty() + Long.toHexString(this.getId()).toUpperCase();
	}

	/**
	 * To set Priority.
	 * @param priority int
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * To get Batch Class.
	 * @return BatchClass
	 */
	public BatchClass getBatchClass() {
		return batchClass;
	}

	/**
	 * To set Batch Class. 
	 * @param batchClass BatchClass
	 */  
	public void setBatchClass(BatchClass batchClass) {
		this.batchClass = batchClass;
	}

	/**
	 * To get Review User Name.
	 * @return String
	 */
	public String getReviewUserName() {
		return reviewUserName;
	}

	/**
	 * To set Review User Name.
	 * @param reviewUserName String
	 */
	public void setReviewUserName(String reviewUserName) {
		this.reviewUserName = reviewUserName;
	}

	/**
	 * To get Validation User Name.
	 * @return String
	 */
	public String getValidationUserName() {
		return validationUserName;
	}

	/**
	 * To set Validation User Name.
	 * @param validationUserName String
	 */
	public void setValidationUserName(String validationUserName) {
		this.validationUserName = validationUserName;
	}

	/**
	 * To get Status.
	 * @return BatchInstanceStatus
	 */
	public BatchInstanceStatus getStatus() {
		return status;
	}

	/**
	 * To set Status.
	 * @param status BatchInstanceStatus
	 */
	public void setStatus(BatchInstanceStatus status) {
		this.status = status;
	}

	/**
	 * To get Unc Sub-folder.
	 * @return String
	 */
	public String getUncSubfolder() {
		return uncSubfolder;
	}

	/**
	 * To set Unc Sub-folder.
	 * @param uncSubfolder String
	 */
	public void setUncSubfolder(String uncSubfolder) {
		this.uncSubfolder = uncSubfolder;
	}

	/**
	 * To get Local Folder.
	 * @return String
	 */
	public String getLocalFolder() {
		return localFolder;
	}

	/**
	 * To set Local Folder.
	 * @param localFolder String
	 */
	public void setLocalFolder(String localFolder) {
		this.localFolder = localFolder;
	}

	/**
	 * To get Current User.
	 * @return String
	 */
	public String getCurrentUser() {
		return currentUser;
	}

	/**
	 * To set Current User.
	 * @param currentUser String
	 */
	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * To get Batch Name.
	 * @return String
	 */
	public String getBatchName() {
		return batchName;
	}

	/**
	 * To set Batch Name.
	 * @param batchName String
	 */
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	/**
	 * To get Identifier.
	 * @return String
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * To set Identifier.
	 * @param identifier String
	 */ 
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * To get Process Instance Key.
	 * @return String
	 */
	@Transient
	public String getProcessInstanceKey() {
		return this.getBatchClass().getName() + DataAccessConstant.DOT + this.getIdentifier();
	}

	/**
	 * To get Lock Owner.
	 * @return ServerRegistry
	 */
	public ServerRegistry getLockOwner() {
		return lockOwner;
	}

	/**
	 * To set Lock Owner.
	 * @param lockOwner ServerRegistry
	 */
	public void setLockOwner(ServerRegistry lockOwner) {
		this.lockOwner = lockOwner;
	}

	/**
	 * To get Batch Instance ID.
	 * @return BatchInstanceID
	 */ 
	@Transient
	public BatchInstanceID getBatchInstanceID() {
		if (getId() != 0 && batchInstanceID == null) {
			batchInstanceID = new BatchInstanceID(identifier);
		}
		return batchInstanceID;
	}

	/**
	 * To check whether remote or not.
	 * @return boolean
	 */
	public boolean isRemote() {
		return remote;
	}

	/**
	 * To set remote.
	 * @param isRemote boolean
	 */
	public void setRemote(boolean isRemote) {
		this.remote = isRemote;
	}

	/**
	 * To get Remote Batch Instance.
	 * @return RemoteBatchInstance
	 */
	public RemoteBatchInstance getRemoteBatchInstance() {
		return remoteBatchInstance;
	}

	/**
	 * To set Remote Batch Instance.
	 * @param remoteBatchInstance RemoteBatchInstance
	 */
	public void setRemoteBatchInstance(RemoteBatchInstance remoteBatchInstance) {
		this.remoteBatchInstance = remoteBatchInstance;
	}

	/**
	 * To get Executed Modules.
	 * @return String
	 */
	public String getExecutedModules() {
		return executedModules;
	}

	/**
	 * To set Executed Modules.
	 * @param executedModules String
	 */
	public void setExecutedModules(String executedModules) {
		this.executedModules = executedModules;
	}
	
	/**
	 * To get Server IP.
	 * @return String
	 */
	public String getServerIP() {
		return serverIP;
	}
	
	/**
	 * To set Server IP. 
	 * @param serverIP String
	 */
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	
	/**
	 * To get Executing Server.
	 * @return String
	 */
	public String getExecutingServer() {
		return executingServer;
	}
	
	/**
	 * To set Executing Server.
	 * @param executingServer String
	 */
	public void setExecutingServer(String executingServer) {
		this.executingServer = executingServer;
	}
}
