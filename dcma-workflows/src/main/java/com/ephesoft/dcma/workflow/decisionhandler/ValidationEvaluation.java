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

package com.ephesoft.dcma.workflow.decisionhandler;

import java.util.Map;

import org.jbpm.api.jpdl.DecisionHandler;
import org.jbpm.api.model.OpenExecution;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.util.BackUpFileService;
import com.ephesoft.dcma.workflow.constant.WorkFlowConstants;

/**
 * This class is used to validate evaluation.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see org.jbpm.api.jpdl.DecisionHandler
 */
public class ValidationEvaluation implements DecisionHandler {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = 936791060838469598L;

	/**
	 * Instance of {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ReviewEvaluation.class);

	/**
	 * To decide which transition will execute.
	 * 
	 * @param execution OpenExecution
	 * @return String
	 */
	public String decide(OpenExecution execution) {

		Map<String, Object> map = execution.getVariables();
		BatchInstanceID batchInstanceID = null;
		boolean isValidationRequired = true;

		if (null != map) {
			batchInstanceID = (BatchInstanceID) map.get(WorkFlowConstants.BATCH_INSTANCE_ID);
		}
		final String batchInstanceIdentifier = batchInstanceID.getID();
		try {
			BatchSchemaService batchSchemaService = EnvironmentImpl.getCurrent().get(BatchSchemaService.class);
			isValidationRequired = batchSchemaService.isValidationRequired(batchInstanceIdentifier);

		} catch (DCMAApplicationException e) {
			LOGGER.error(e.getMessage(), e);
		}

		String content = WorkFlowConstants.YES_STRING;
		if (isValidationRequired) {
			content = WorkFlowConstants.YES_STRING;
		} else {
			content = WorkFlowConstants.NO_STRING;

			// Explicitly copy the batch xml for "resume" batch functionality
			BackUpFileService.backUpBatch(batchInstanceIdentifier, WorkFlowConstants.VALIDATE_DOCUMENT_PLUGIN, batchInstanceService
					.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));
		}

		return content;
	}
}
