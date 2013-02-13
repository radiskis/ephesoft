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

package com.ephesoft.dcma.workflow.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jbpm.api.ExecutionService;
import org.jbpm.api.HistoryService;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.history.HistoryProcessInstance;
import org.jbpm.api.history.HistoryProcessInstanceQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.service.BatchInstanceService;

/**
 * This service will provide work flow state management. If the state of the process instance is given as a input to these api's. Api's
 * will return all the batch instance which are present at this state.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.workflow.service.WorkflowStateService
 */
@Service
public class WorkflowStateServiceImpl implements WorkflowStateService {

	/**
	 * Reference of HistoryService.
	 */
	@Autowired
	private HistoryService historyService;

	/**
	 * Reference of ExecutionService.
	 */
	@Autowired
	private ExecutionService executionService;

	/**
	 * Reference of BatchInstanceService.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * LOGGER to print the LOGGERging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowStateServiceImpl.class);

	/**
	 * An API to fetch all the batch instance id for input process instance state.
	 * 
	 * @param state String
	 * @return list of all the batch instance id for input of process instance state.
	 */
	public List<BatchInstance> getAllBatchInstanceForState(String state) {

		List<BatchInstance> batchInstanceList = null;

		String batchClassName = "TesseractInvoice";
		String reviewDocuments = "review-document";

		List<String> batchInstanceIDList = new ArrayList<String>();

		HistoryProcessInstanceQuery query = historyService.createHistoryProcessInstanceQuery().state(state);
		List<HistoryProcessInstance> instances = query.list();

		for (HistoryProcessInstance historyProcessInstance : instances) {
			String processInstanceId = historyProcessInstance.getProcessInstanceId();
			if (processInstanceId.contains(batchClassName)) {
				LOGGER.info("Process Instance ID contains TesseractInvoice.");
				String key = historyProcessInstance.getKey();
				LOGGER.info("processInstanceId : " + processInstanceId + " and key :" + key);
				ProcessInstance instance = executionService.findProcessInstanceById(processInstanceId);
				Set<String> activities = instance.findActiveActivityNames();
				if (activities.contains(reviewDocuments)) {
					try {
						batchInstanceIDList.add(key);
					} catch (NumberFormatException nfe) {
						LOGGER.error(nfe.getMessage());
					}
				}
			}
		}

		batchInstanceList = batchInstanceService.getBatchInstanceList(batchClassName, batchInstanceIDList);

		if (null != batchInstanceList) {
			LOGGER.info("For batchClassName '" + batchClassName + "' number of batchInstance  : " + batchInstanceList.size());
			for (BatchInstance batchInstance : batchInstanceList) {
				LOGGER.debug("BatchInstance id : " + batchInstance.getIdentifier());
			}
		}

		return batchInstanceList;
	}

}
