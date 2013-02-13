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

package com.ephesoft.dcma.cleanup.service;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.cleanup.CleanupComponent;
import com.ephesoft.dcma.cleanup.CleanupProperties;
import com.ephesoft.dcma.cleanup.constant.CleanupConstant;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.annotation.PreProcess;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.RemoteBatchInstance;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.util.BackUpFileService;

/**
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.cleanup.service.CleanupServiceImpl
 *
 */
public class CleanupServiceImpl implements CleanupService, CleanupConstant {

	/**
	 * Logger reference.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CleanupServiceImpl.class);

	/**
	 * A instance of {@link PluginPropertiesService}.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * Instance of {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;
	
	
	/**
	 * To get the xml file before start of process.
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 */
	@PreProcess
	public void preProcess(final BatchInstanceID batchInstanceID, final String pluginWorkflow) {
		Assert.notNull(batchInstanceID);
		final String batchInstanceIdentifier = batchInstanceID.getID();
		BackUpFileService.backUpBatch(batchInstanceIdentifier, pluginWorkflow,  batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));
	}

	/**
	 * This method performs the functionality of clean up process.
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 * @throws DCMAException if any exception occurs while deleting the folder.
	 */
	@Override
	public void cleanup(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException {
		final String batchInstanceIdentifier = batchInstanceID.getID();
		final BatchInstance batchInstance = batchInstanceService.getBatchInstancesForIdentifier(batchInstanceIdentifier);
		final String deleteSystemInfo = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, CLEAN_UP_PLUGIN_NAME,
				CleanupProperties.DELETE_SYSTEM_INFORMATION_PROPERTY);
		boolean isRemoteBatchInstance = false;
		final String sBatchFolder = batchInstance.getLocalFolder() + File.separator + batchInstance.getIdentifier();

		final RemoteBatchInstance remoteBatchInstance = batchInstance.getRemoteBatchInstance();
		final String delUncFolder = batchInstance.getUncSubfolder();
		pluginPropertiesService.clearCache(batchInstanceIdentifier);
		if (remoteBatchInstance != null
				&& (remoteBatchInstance.getRemoteURL() != null || remoteBatchInstance.getPreviousRemoteURL() != null)
				&& delUncFolder == null) {
			isRemoteBatchInstance = true;
		}
		if (!isRemoteBatchInstance) {
			try {
				garbageCollector();
				CleanupComponent.execute(delUncFolder);
			} catch (IOException e) {
				LOGGER.error("Unable to delete folder : " + delUncFolder, e);
			} catch (DCMAApplicationException e) {
				LOGGER.error(COULD_NOT_DELETE_FOLDER + delUncFolder, e);
				throw new DCMAException(COULD_NOT_DELETE_FOLDER + delUncFolder, e);
			} catch (SecurityException e) {
				LOGGER.error(NOT_ENOUGH_PERMISSION_TO_DELETE_FOLDER + delUncFolder, e);
				throw new DCMAException(NOT_ENOUGH_PERMISSION_TO_DELETE_FOLDER + delUncFolder, e);
			} catch (Exception e) {
				LOGGER.error(EXCEPTION_WHILE_DELETING_FOLDER + delUncFolder, e);
				throw new DCMAException(EXCEPTION_WHILE_DELETING_FOLDER + delUncFolder, e);
			}
		}
		if (TRUE.equalsIgnoreCase(deleteSystemInfo)) {

			final String serializedFilePath = batchInstance.getLocalFolder() + File.separator + PROPERTIES_DIRECTORY + File.separator
					+ batchInstance.getIdentifier() + SER_EXTENSION;
			try {
				garbageCollector();
				CleanupComponent.deleteFile(serializedFilePath);
				LOGGER.info(serializedFilePath + " deleted successfully");
			} catch (IOException e) {
				LOGGER.error("Unable to delete file : " + serializedFilePath, e);
			} catch (DCMAApplicationException e) {
				LOGGER.error("Could not delete file : " + serializedFilePath, e);
				throw new DCMAException("Could not delete file : " + serializedFilePath, e);
			} catch (SecurityException e) {
				LOGGER.error("Not enough permission to delete file : " + serializedFilePath, e);
				throw new DCMAException("Not enough permission to delete file : " + serializedFilePath, e);
			} catch (Exception e) {
				LOGGER.error("Exception while deleting file : " + serializedFilePath, e);
				throw new DCMAException("Exception while deleting file : " + serializedFilePath, e);
			}

			try {
				garbageCollector();
				CleanupComponent.execute(sBatchFolder);
				LOGGER.info(sBatchFolder + " deleted successfully");
			} catch (IOException e) {
				LOGGER.error("Unable to delete folder : " + sBatchFolder, e);
			} catch (DCMAApplicationException e) {
				LOGGER.error(COULD_NOT_DELETE_FOLDER + sBatchFolder, e);
				throw new DCMAException(COULD_NOT_DELETE_FOLDER + sBatchFolder, e);
			} catch (SecurityException e) {
				LOGGER.error(NOT_ENOUGH_PERMISSION_TO_DELETE_FOLDER + sBatchFolder, e);
				throw new DCMAException(NOT_ENOUGH_PERMISSION_TO_DELETE_FOLDER + sBatchFolder, e);
			} catch (Exception e) {
				LOGGER.error(EXCEPTION_WHILE_DELETING_FOLDER + sBatchFolder, e);
				throw new DCMAException(EXCEPTION_WHILE_DELETING_FOLDER + sBatchFolder, e);
			}
		}
	}

	/**
	 * The <code> garbageCollector </code> is a method used to call systems
	 * garbage collections.
	 */
	private void garbageCollector() {
		System.gc();
	}

}
