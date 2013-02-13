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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.api.Execution;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.ProcessInstance;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ephesoft.dcma.batch.service.EphesoftContext;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.MailMetaData;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.ServerRegistry;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.mail.MailContentModel;
import com.ephesoft.dcma.mail.service.MailService;
import com.ephesoft.dcma.workflow.constant.WorkFlowConstants;
import com.ephesoft.dcma.workflow.service.PickupService;

/**
 * This class is used to initializing the workflow services. It is used start/stop/resume/suspend workflow.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.workflow.service.common.WorkflowServiceImpl
 * 
 */
public class WorkflowServiceImpl implements WorkflowService {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowServiceImpl.class);

	/**
	 * Initializing pluginPropertiesService {@link PluginPropertiesService}.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;
	
	/**
	 * Instance of {@link ExecutionService}.
	 */
	@Autowired
	private ExecutionService executionService;
	
	/**
	 * Instance of {@link MailService}.
	 */
	@Autowired
	private MailService mailService;
	
	/**
	 * Instance of {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;
	
	/**
	 * Instance of {@link PickupService}.
	 */
	@Autowired
	private PickupService pickupService;

	/**
	 * fromMail String.
	 */
	private String fromMail;
	
	/**
	 * toMail String.
	 */
	private String toMail;
	
	/**
	 * subject String.
	 */
	private String subject;
	
	/**
	 * mailTemplatePath String.
	 */
	private String mailTemplatePath;

	/**
	 * This method is used to start workflow for given batch instance.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 */
	@Override
	public void startWorkflow(final BatchInstanceID batchInstanceID) {
		startWorkflow(batchInstanceID, null);
	}

	/**
	 * This method is used to start workflow for given batch instance on a particular module.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param moduleName {@link String}
	 */
	@Override
	public void startWorkflow(final BatchInstanceID batchInstanceID, String moduleName) {
		LOGGER.info("Start Workflow for batch instance id:" + batchInstanceID + " for module name:" + moduleName);
		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceID.getID());
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put(WorkFlowConstants.BATCH_INSTANCE_ID, batchInstanceID);
		vars.put(WorkFlowConstants.RESTART_WORKFLOW, moduleName);
		vars.put(WorkFlowConstants.IS_MODULE_REMOTE, WorkFlowConstants.NO_STRING);
		if (moduleName != null) {
			// batchInstance.setStatus(BatchInstanceStatus.RUNNING);
			batchInstanceService.updateBatchInstanceStatusByIdentifier(batchInstance.getIdentifier(), BatchInstanceStatus.RUNNING);
		}
		// batchInstanceService.merge(batchInstance);
		executionService.startProcessInstanceByKey(batchInstance.getBatchClass().getName(), vars, batchInstance.getIdentifier());

	}

	/**
	 * This method is used to restart workflow for given batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 */
	@Override
	public void restartWorkflow(BatchInstance batchInstance) {
		throw new UnsupportedOperationException("Operation not supported.");
	}

	/**
	 * This method is used to start workflow for given batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 */
	@Override
	public void stopWorkflow(BatchInstance batchInstance) {
		throw new UnsupportedOperationException("Operation not supported.");
	}

	/**
	 * This method is used to suspend workflow for given batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 */
	@Override
	public void suspendWorkflow(BatchInstance batchInstance) {
		throw new UnsupportedOperationException("Operation not supported.");
	}

	/**
	 * This method generates a mail in case of error.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 */
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

	/**
	 * This method is used to signal workflow for given batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void signalWorkflow(BatchInstance batchInstance) {
		LOGGER.info("Signal Workflow for batch instance id:" + batchInstance.getIdentifier());
		String processInstanceKey = batchInstance.getProcessInstanceKey();
		ProcessInstance processInstance = executionService.findProcessInstanceById(processInstanceKey);
		if (processInstance != null) {
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
		} else {
			startWorkflow(batchInstance.getBatchInstanceID());
		}
	}

	/**
	 * This method is used to get active module for a batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @return {@link String}
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String getActiveModule(BatchInstance batchInstance) {
		LOGGER.info("Get active module for batch instance id:" + batchInstance.getIdentifier());
		String returnValue = null;
		String processInstanceKey = batchInstance.getProcessInstanceKey();
		ProcessInstance processInstance = executionService.findProcessInstanceById(processInstanceKey);
		Execution moduleExecution = null;
		if (processInstance != null) {
			Execution execution = processInstance.findActiveExecutionIn(((ExecutionImpl) processInstance).getActivityName());
			if ((execution != null)) {
				moduleExecution = ((ExecutionImpl) execution).getSubProcessInstance();
			}
			if (moduleExecution != null) {
				execution = moduleExecution;
			}
			returnValue = execution.getId();
		}
		LOGGER.info("Active module for batch instance id:" + batchInstance.getIdentifier() + " is:" + returnValue);
		return returnValue;

	}

	/**
	 * To set From Mail.
	 * @param fromMail String
	 */
	public void setFromMail(String fromMail) {
		this.fromMail = fromMail;
	}

	/**
	 * To set To Mail.
	 * @param toMail String
	 */
	public void setToMail(String toMail) {
		this.toMail = toMail;
	}

	/**
	 * To set Subject.
	 * @param subject String
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * To set Mail Template Path.
	 * @param mailTemplatePath String
	 */
	public void setMailTemplatePath(String mailTemplatePath) {
		this.mailTemplatePath = mailTemplatePath;
	}

	/**
	 * This method is used to signal workflow for a batch with the specified batchId.
	 * 
	 * @param batchId {@link String}
	 * 
	 */
	@Override
	public void signalWorkflow(String batchId) {
		LOGGER.info("Signal workflow for batch instance id:" + batchId);
		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchId);
		boolean needToSignal = false;
		switch (batchInstance.getStatus()) {
			case READY_FOR_REVIEW:
				needToSignal = true;
				break;

			case READY_FOR_VALIDATION:
				needToSignal = true;
				break;

			default:
				break;
		}
		if (needToSignal) {
			int maxCapacity = pickupService.getMaxCapacity();
			ServerRegistry lockOwner = EphesoftContext.getHostServerRegistry();
			String serverContextPath = lockOwner.getIpAddress() + WorkFlowConstants.COLON + lockOwner.getPort()
					+ lockOwner.getAppContext();
			int runningJobCount = 0;
			List<BatchInstance> batchInstanceList = batchInstanceService.getExecutingJobByServerIP(serverContextPath);
			if (batchInstanceList != null) {
				runningJobCount = batchInstanceList.size();
			}
			if (runningJobCount < maxCapacity) {
				batchInstance.setStatus(BatchInstanceStatus.RUNNING);
				batchInstance.setExecutingServer(serverContextPath);
				batchInstance.setCurrentUser(null);
				batchInstanceService.updateBatchInstance(batchInstance);
				signalWorkflow(batchInstance);
			} else {
				batchInstance.setStatus(BatchInstanceStatus.READY);
				batchInstance.setCurrentUser(null);
				batchInstanceService.updateBatchInstance(batchInstance);
			}

		}
	}

	/**
	 * This API is used to update Batch Instance Status For batches in Review And Validation phase.
	 * 
	 * @param identifier {@link BatchInstanceID}
	 * @param status {@link BatchInstanceStatus}
	 */
	@Override
	public void updateBatchInstanceStatusForReviewAndValidation(BatchInstanceID identifier, BatchInstanceStatus status) {
		pluginPropertiesService.clearCache(identifier.getID());
		batchInstanceService.updateBatchInstanceStatus(identifier, status);
	}

}
