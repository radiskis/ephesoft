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

package com.ephesoft.dcma.cleanup.service;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.cleanup.CleanupComponent;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.dao.BatchInstanceDao;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.RemoteBatchInstance;
import com.ephesoft.dcma.da.id.BatchInstanceID;

public class CleanupServiceImpl implements CleanupService, ICommonConstants {

	private static final String PROPERTIES_DIRECTORY = "properties";

	private static final String PROPERTIES_FILE_EXTENSION = ".ser";

	/**
	 * Logger reference.
	 */
	private Logger logger = LoggerFactory.getLogger(CleanupServiceImpl.class);

	@Autowired
	private BatchInstanceDao batchInstanceDao;

	@Override
	public void cleanup(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException {
		BatchInstance batchInstance = batchInstanceDao.getBatchInstancesForIdentifier(batchInstanceID.getID());
		boolean isRemoteBatchInstance = false;
		String delUncFolder = null;
		String sBatchFolder = batchInstance.getLocalFolder() + File.separator + batchInstance.getIdentifier();
		CleanupComponent cleanUpComponet = new CleanupComponent();
		RemoteBatchInstance remoteBatchInstance = batchInstance.getRemoteBatchInstance();
		delUncFolder = batchInstance.getUncSubfolder();
		if (remoteBatchInstance != null && (remoteBatchInstance.getRemoteURL() != null || remoteBatchInstance.getPreviousRemoteURL() != null) ) {
			if (delUncFolder == null) {
				isRemoteBatchInstance = true;
			}
		}
		if (!isRemoteBatchInstance) {
			try {
				cleanUpComponet.execute(delUncFolder);
			} catch (IOException e) {
				logger.error("Unable to delete folder : " + delUncFolder, e);
			} catch (DCMAApplicationException e) {
				logger.error("Could not delete folder : " + delUncFolder, e);
				throw new DCMAException("Could not delete folder : " + delUncFolder, e);
			} catch (SecurityException e) {
				logger.error("Not enough permission to delete folder : " + delUncFolder, e);
				throw new DCMAException("Not enough permission to delete folder : " + delUncFolder, e);
			} catch (Exception e) {
				logger.error("Exception while deleting folder : " + delUncFolder, e);
				throw new DCMAException("Exception while deleting folder : " + delUncFolder, e);
			}
		}

		String serializedFilePath = batchInstance.getLocalFolder() + File.separator + PROPERTIES_DIRECTORY + File.separator
				+ batchInstance.getIdentifier() + PROPERTIES_FILE_EXTENSION;
		try {
			cleanUpComponet.deleteFile(serializedFilePath);
			logger.info(serializedFilePath + " deleted successfully");
		} catch (IOException e) {
			logger.error("Unable to delete file : " + serializedFilePath, e);
		} catch (DCMAApplicationException e) {
			logger.error("Could not delete file : " + serializedFilePath, e);
			throw new DCMAException("Could not delete file : " + serializedFilePath, e);
		} catch (SecurityException e) {
			logger.error("Not enough permission to delete file : " + serializedFilePath, e);
			throw new DCMAException("Not enough permission to delete file : " + serializedFilePath, e);
		} catch (Exception e) {
			logger.error("Exception while deleting file : " + serializedFilePath, e);
			throw new DCMAException("Exception while deleting file : " + serializedFilePath, e);
		}

		try {
			cleanUpComponet.execute(sBatchFolder);
			logger.info(sBatchFolder + " deleted successfully");
		} catch (IOException e) {
			logger.error("Unable to delete folder : " + sBatchFolder, e);
		} catch (DCMAApplicationException e) {
			logger.error("Could not delete folder : " + sBatchFolder, e);
			throw new DCMAException("Could not delete folder : " + sBatchFolder, e);
		} catch (SecurityException e) {
			logger.error("Not enough permission to delete folder : " + sBatchFolder, e);
			throw new DCMAException("Not enough permission to delete folder : " + sBatchFolder, e);
		} catch (Exception e) {
			logger.error("Exception while deleting folder : " + sBatchFolder, e);
			throw new DCMAException("Exception while deleting folder : " + sBatchFolder, e);
		}

	}

}
