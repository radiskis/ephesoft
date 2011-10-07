/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2010-2011 Ephesoft Inc. 
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.api.Execution;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.ProcessInstance;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.BatchStatus;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.MailMetaData;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.mail.MailContentModel;
import com.ephesoft.dcma.mail.service.MailService;
import com.ephesoft.dcma.workflow.constant.WorkFlowConstants;

/**
 * This class is used to initializing the workflow services. It is used start/stop/resume/suspend workflow.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.workflow.service.common.WorkflowServiceImpl
 * 
 */
public class WorkflowServiceImpl implements WorkflowService {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowServiceImpl.class);

	@Autowired
	private ExecutionService executionService;
	@Autowired
	private MailService mailService;
	@Autowired
	private BatchInstanceService batchInstanceService;
	@Autowired
	private BatchSchemaService batchSchemaService;

	private String fromMail;
	private String toMail;
	private String subject;
	private String mailTemplatePath;

	@Override
	public void startWorkflow(final BatchInstanceID batchInstanceID) {
		startWorkflow(batchInstanceID, null);
	}

	@Override
	public void startWorkflow(final BatchInstanceID batchInstanceID, String moduleName) {
		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceID.getID());
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put(JBPMVariables.BATCH_INSTANCE_ID, batchInstanceID);
		vars.put(JBPMVariables.RESTART_WORKFLOW, moduleName);
		vars.put(JBPMVariables.IS_MODULE_REMOTE, WorkFlowConstants.NO_STRING);
		if (moduleName == null) {
			//batchInstance.setStatus(BatchInstanceStatus.READY);
			batchInstanceService.updateBatchInstanceStatusByIdentifier(batchInstance.getIdentifier(), BatchInstanceStatus.READY);
		} else {
			//batchInstance.setStatus(BatchInstanceStatus.RUNNING);
			batchInstanceService.updateBatchInstanceStatusByIdentifier(batchInstance.getIdentifier(), BatchInstanceStatus.RUNNING);
		}
		//batchInstanceService.merge(batchInstance);
		executionService.startProcessInstanceByKey(batchInstance.getBatchClass().getName(), vars, batchInstance.getIdentifier());

	}

	@Override
	public void restartWorkflow(BatchInstance batchInstance) {
		throw new UnsupportedOperationException("Operation not supported.");
	}

	@Override
	public void stopWorkflow(BatchInstance batchInstance) {
		throw new UnsupportedOperationException("Operation not supported.");
	}

	@Override
	public void suspendWorkflow(BatchInstance batchInstance) {
		throw new UnsupportedOperationException("Operation not supported.");
	}

	@Override
	public void mailOnError(BatchInstance batchInstances) {
		MailMetaData metaData = new MailMetaData();
		metaData.setFromAddress(this.fromMail);
		metaData.setFromName(this.fromMail);
		metaData.setSubject(this.subject);
		metaData.setToAddresses(new ArrayList<String>(StringUtils.commaDelimitedListToSet(toMail)));

		MailContentModel model = new MailContentModel();
		model.add("workflow", batchInstances.getBatchClass().getName());
		model.add("batchInstance", batchInstances.getId());

		mailService.sendTextMailWithClasspathTemplate(metaData, mailTemplatePath, model);
	}

	public BatchStatus updateBatchAndSignalWorkflow(final Batch batch, String userName) {
		return updateBatchAndSignalWorkflow(batch, userName, true);
	}

	private BatchStatus updateBatchAndSignalWorkflow(final Batch batch, String userName, boolean signalWorkflow) {

		// update the batch.
		try {
			batchSchemaService.updateBatch(batch);
		} catch (Exception e) {
			LOGGER.error("Error saving batch info", e);
		}

		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batch.getBatchInstanceIdentifier());
		BatchInstanceStatus status = batchInstance.getStatus();

		BatchStatus batchStatus = batch.getBatchStatus();
		boolean needToSignal = false;

		switch (status) {
			case READY_FOR_REVIEW:
				batchStatus = BatchStatus.REVIEWED;
				try {
					needToSignal = !batchSchemaService.isReviewRequired(batch.getBatchInstanceIdentifier(), true);
					batchInstance.setReviewUserName(userName);
				} catch (DCMAApplicationException e) {
					LOGGER.error(e.getMessage());
				}
				break;

			case READY_FOR_VALIDATION:
				batchStatus = BatchStatus.VALIDATED;
				try {
					needToSignal = !batchSchemaService.isValidationRequired(batch.getBatchInstanceIdentifier());
					batchInstance.setValidationUserName(userName);
				} catch (DCMAApplicationException e) {
					LOGGER.error(e.getMessage());
				}
				break;

			default:
				break;
		}

		if (needToSignal) {
			if (signalWorkflow) {
				batchInstance.setStatus(BatchInstanceStatus.RUNNING);
				batchInstanceService.updateBatchInstance(batchInstance);
				signalWorkflow(batchInstance);
			}
			return batchStatus;
		}
		return batch.getBatchStatus();
	}

	public void signalWorkflow(BatchInstance batchInstance) {
		String processInstanceKey = batchInstance.getProcessInstanceKey();
		ProcessInstance processInstance = executionService.findProcessInstanceById(processInstanceKey);
		Execution execution = processInstance.findActiveExecutionIn(((ExecutionImpl) processInstance).getActivityName());
		Execution moduleExecution = ((ExecutionImpl) execution).getSubProcessInstance();
		if (moduleExecution != null) {
			execution = moduleExecution;
			Execution pluginExecution = ((ExecutionImpl) execution).getSubProcessInstance();
			if (pluginExecution != null) {
				execution = pluginExecution;
			}
		}
		if (execution != null) {
			executionService.signalExecutionById(execution.getId());
		}
	}

	@Override
	public String getActiveModule(BatchInstance batchInstance) {
		String returnValue = null;
		String processInstanceKey = batchInstance.getProcessInstanceKey();
		ProcessInstance processInstance = executionService.findProcessInstanceById(processInstanceKey);
		if (processInstance != null) {
			Execution execution = processInstance.findActiveExecutionIn(((ExecutionImpl) processInstance).getActivityName());
			if (execution != null) {
				Execution moduleExecution = ((ExecutionImpl) execution).getSubProcessInstance();
				if (moduleExecution != null) {
					execution = moduleExecution;
				}
				returnValue = execution.getId();
			}
		}
		return returnValue;

	}

	public void setFromMail(String fromMail) {
		this.fromMail = fromMail;
	}

	public void setToMail(String toMail) {
		this.toMail = toMail;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setMailTemplatePath(String mailTemplatePath) {
		this.mailTemplatePath = mailTemplatePath;
	}

	@Override
	public BatchStatus updateBatch(Batch batch, String userName) {
		return updateBatchAndSignalWorkflow(batch, userName, false);
	}

}
