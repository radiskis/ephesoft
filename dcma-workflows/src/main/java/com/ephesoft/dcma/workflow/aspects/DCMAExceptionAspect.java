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

package com.ephesoft.dcma.workflow.aspects;

import java.io.File;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.BatchStatus;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.workflow.service.common.WorkflowService;

/**
 * This class throws exception in case of error in executing the batch.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.batch.service.BatchSchemaService
 * @see com.ephesoft.dcma.da.service.BatchInstanceService
 */
@Aspect
public class DCMAExceptionAspect {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DCMAExceptionAspect.class);

	/**
	 * Instance of {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * Instance of {@link BatchSchemaService}.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Instance of {@link WorkflowService}.
	 */
	@Autowired
	private WorkflowService workflowService;

	/**
	 * To run the thread and throw exception in case of any error.
	 * 
	 * @param joinPoint JoinPoint
	 * @param dcmaException DCMAException
	 * @throws Throwable
	 */
	@AfterThrowing(pointcut = "execution(* com.ephesoft.dcma.*.service.*.*(..))", throwing = "dcmaException")
	public void afterThrowing(JoinPoint joinPoint, DCMAException dcmaException) throws Throwable {
		Object[] args = joinPoint.getArgs();
		if (args[0] instanceof BatchInstanceID) {
			String batchInstanceIdentifier = args[0].toString();
			final BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdentifier);
			if (batchInstance == null) {
				throw new Exception("Batch Instance can not be null to handle this aspect.");
			}

			if (batchInstance.getStatus() != BatchInstanceStatus.ERROR) {

				Runnable runnable = new Runnable() {

					@Override
					public void run() {

						try {
							// update the batch instance status to ERROR in batch xml file
							Batch batch = batchSchemaService.getBatch(batchInstance.getIdentifier());
							batch.setBatchStatus(BatchStatus.ERROR);
							batchSchemaService.updateBatch(batch);
						} catch (Exception e) {
							LOGGER.error("Error while updating the batch xml status to ERROR status. " + e.getMessage(), e);
						}
						batchInstanceService.updateBatchInstanceStatusByIdentifier(batchInstance.getIdentifier(),
								BatchInstanceStatus.ERROR);
					}
				};

				Thread thread = new Thread(runnable);
				thread.start();
				releaseLockFromBatchInstanceFolder(batchInstanceIdentifier, batchInstance);
			}
		}
	}

	private void releaseLockFromBatchInstanceFolder(String batchInstanceIdentifier, final BatchInstance batchInstance) {
		try {
			// RESTART BATCH functionality changes.
			// Explicitly delete the lock file in case the batch goes in ERROR so that the batch is not blocked from RESTARTING
			LOGGER
					.info("Explicitly delete the lock file in case the batch goes in ERROR so that the batch is not blocked from RESTARTING.");
			String threadPoolLockFolderPath = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceIdentifier)
					+ File.separator + batchInstance.getIdentifier() + File.separator
					+ batchSchemaService.getThreadpoolLockFolderName();
			File threadPoolLockFolder = new File(threadPoolLockFolderPath);
			if (threadPoolLockFolder != null && threadPoolLockFolder.isDirectory()) {
				boolean deleteSrcDir = false;
				FileUtils.deleteDirectoryAndContentsRecursive(threadPoolLockFolder, deleteSrcDir);
				String[] fileList = threadPoolLockFolder.list();
				if (fileList.length == 0) {
					LOGGER
							.info("Successfully deleted all the locked file from the system path location : "
									+ threadPoolLockFolderPath);
				} else {
					LOGGER.info("No able to deleted all the locked file from the system path location : " + threadPoolLockFolderPath);
				}
			}
			workflowService.mailOnError(batchInstance);
		} catch (Exception mailExp) {
			LOGGER.error("Some internal error occured during sending the mail. Check the mail configuration.", mailExp);
		}
	}
}
