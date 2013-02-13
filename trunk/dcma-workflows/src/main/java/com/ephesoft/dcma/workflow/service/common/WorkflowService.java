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

package com.ephesoft.dcma.workflow.service.common;

import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.id.BatchInstanceID;

/**
 * This class is used to initializing the workflow services. It is used start/stop/resume/suspend workflow.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.workflow.service.common.WorkflowServiceImpl
 * 
 */
public interface WorkflowService {

	/**
	 * This method is used to start workflow for given batch instance.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 */
	void startWorkflow(final BatchInstanceID batchInstanceID);

	/**
	 * This method is used to start workflow for given batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 */
	void stopWorkflow(BatchInstance batchInstance);

	/**
	 * This method is used to suspend workflow for given batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 */
	void suspendWorkflow(BatchInstance batchInstance);

	/**
	 * This method is used to restart workflow for given batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 */
	void restartWorkflow(BatchInstance batchInstance);

	/**
	 * This method generates a mail in case of error.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 */
	void mailOnError(BatchInstance batchInstance);

	/**
	 * This method is used to get active module for a batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @return {@link String}
	 */
	String getActiveModule(BatchInstance batchInstance);

	/**
	 * This method is used to start workflow for given batch instance on a particular module.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param module {@link String}
	 */
	void startWorkflow(final BatchInstanceID batchInstanceID, final String module);

	/**
	 * This method is used to signal workflow for given batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 */
	void signalWorkflow(final BatchInstance batchInstance);

	/**
	 * This method is used to signal workflow for a batch with the specified batchId.
	 * 
	 * @param batchId {@link String}
	 * 
	 */
	void signalWorkflow(String batchId);

	/**
	 * This API is used to update Batch Instance Status For batches in Review And Validation phase.
	 * 
	 * @param identifier {@link BatchInstanceID}
	 * @param status {@link BatchInstanceStatus}
	 */
	void updateBatchInstanceStatusForReviewAndValidation(BatchInstanceID identifier, BatchInstanceStatus status);

}
