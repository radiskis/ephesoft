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

package com.ephesoft.dcma.workflow.jbpm.pvm.internal.id;

import org.jbpm.api.Execution;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.pvm.internal.id.IdComposer;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.workflow.constant.WorkFlowConstants;

/**
 * This classis to compose database Id.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see org.jbpm.pvm.internal.id.IdComposer
 */
public class EphesoftDatabaseIdComposer extends IdComposer {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(EphesoftDatabaseIdComposer.class);

	/**
	 * To create Id.
	 * 
	 * @param processDefinition ProcessDefinition
	 * @param parent Execution
	 * @param execution ExecutionImpl
	 * @return String
	 */
	public String createId(ProcessDefinition processDefinition, Execution parent, ExecutionImpl execution) {

		String base = null;
		if (parent != null) {
			base = parent.getId();
		} else if (processDefinition.getKey() != null) {
			base = processDefinition.getKey();
		} else {
			base = processDefinition.getId();
		}

		String executionPart = null;
		if ((parent == null) && (execution.getKey() != null)) {
			executionPart = execution.getKey();

		} else if (execution.getName() != null) {
			executionPart = execution.getName();

		} else if (execution.getSuperProcessExecution() != null) {
			StringBuilder executionPartStringBuilder = new StringBuilder();
			executionPart = execution.getSuperProcessExecution().getKey();
			if (executionPart != null) {
				executionPartStringBuilder.append(executionPart);
				executionPartStringBuilder.append(WorkFlowConstants.PERIOD);
				executionPartStringBuilder.append(execution.getDbid());
				executionPartStringBuilder.append(WorkFlowConstants.MINUS_M_KEYWORD);
				executionPart = executionPartStringBuilder.toString();
			} else {
				final String key = extractKeyFromId(execution.getSuperProcessExecution().getId());
				executionPartStringBuilder.append(key);
				executionPartStringBuilder.append(WorkFlowConstants.PERIOD);
				executionPartStringBuilder.append(execution.getDbid());
				executionPartStringBuilder.append(WorkFlowConstants.MINUS_P_KEYWORD);
				executionPart = executionPartStringBuilder.toString();
			}
		} else {

			executionPart = Long.toString(execution.getDbid());
		}

		String executionId = base + WorkFlowConstants.PERIOD + executionPart;

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("generated execution id " + executionId);
		}
		return executionId;
	}

	private String extractKeyFromId(String keyId) {
		String key = null;
		int index = keyId.indexOf(WorkFlowConstants.PERIOD);
		int lastIndex = keyId.lastIndexOf(WorkFlowConstants.PERIOD);
		if (lastIndex > -1) {
			key = keyId.substring(index + 1, lastIndex);
		} else {
			key = keyId.substring(index + 1);
		}
		return key;
	}
}
