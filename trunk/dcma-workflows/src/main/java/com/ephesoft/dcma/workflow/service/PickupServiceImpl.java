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

import java.util.List;

import org.hibernate.exception.LockAcquisitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.workflow.service.common.WorkflowService;

public class PickupServiceImpl implements PickupService {

	private int maxCapacity;
	private int pickCapacity;

	private static final Logger log = LoggerFactory.getLogger(PickupServiceImpl.class);

	@Autowired BatchInstanceService batchInstanceService;
	@Autowired WorkflowService workFlowService;

	@Override
	public void pickupBatchInstance() {
		List<BatchInstance> newBatches = batchInstanceService.getBatchInstByStatus(BatchInstanceStatus.NEW);
		
		for (BatchInstance newBatchInstance : newBatches) {
			try {
				batchInstanceService.lockBatch(newBatchInstance.getId());
				workFlowService.startWorkflow(newBatchInstance.getBatchInstanceID());
			} catch (LockAcquisitionException e) {
				log.trace("Batch instance -" + newBatchInstance.getIdentifier() + " is already locked.");
			}
		}
	}

	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	public void setPickCapacity(int pickCapacity) {
		this.pickCapacity = pickCapacity;
	}

	@Override
	public int getMaxCapacity() {
		return maxCapacity;
	}
}
