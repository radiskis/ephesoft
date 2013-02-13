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

package com.ephesoft.dcma.workflow.listener;

import java.util.List;

import org.jbpm.api.listener.EventListener;
import org.jbpm.api.listener.EventListenerExecution;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.workflow.constant.WorkFlowConstants;

/**
 * This is listener class when the module execution starts.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see org.jbpm.api.listener.EventListenerExecution
 */
public class ModuleExecutionStartListener implements EventListener {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = -2076024339760462947L;

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ModuleExecutionStartListener.class);

	/**
	 * This method is invoked when an execution crosses the event on which this listener is registered.
	 * 
	 * @param execution EventListenerExecution
	 */
	@Override
	public void notify(EventListenerExecution execution) {
		LOGGER.info("Module Execution Start Event Fired.");
		BatchInstanceID batchInstanceID = (BatchInstanceID) execution.getVariables().get(WorkFlowConstants.BATCH_INSTANCE_ID);
		String moduleWorkflowName = ((ExecutionImpl) execution).getActivityName();
		LOGGER.info("Module Name:" + moduleWorkflowName);
		BatchInstanceService batchInstanceService = EnvironmentImpl.getCurrent().get(BatchInstanceService.class);
		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceID.getID());
		BatchClass batchClass = batchInstance.getBatchClass();
		List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
		String executedModuleIdString = null;
		for (BatchClassModule bcm : batchClassModules) {
			if (bcm.getWorkflowName().equalsIgnoreCase(moduleWorkflowName)) {
				executedModuleIdString = batchInstance.getExecutedModules();
				LOGGER.info("Executed Module List:" + executedModuleIdString);
				if (executedModuleIdString == null) {
					executedModuleIdString = WorkFlowConstants.EMPTY_STRING;
				}
				String moduleId = bcm.getModule().getId() + WorkFlowConstants.SEMICOLON;
				if (!executedModuleIdString.contains(moduleId)) {
					StringBuilder executedModuleBuilder = new StringBuilder();
					executedModuleBuilder.append(executedModuleIdString);
					executedModuleBuilder.append(moduleId);
					executedModuleIdString = executedModuleBuilder.toString();
				}
			}
		}
		LOGGER.info("Final Executed Module List:" + executedModuleIdString);
		batchInstance.setExecutedModules(executedModuleIdString);
		batchInstanceService.updateBatchInstance(batchInstance);
		LOGGER.info("Module Execution Start Event Ends.");
	}

}
