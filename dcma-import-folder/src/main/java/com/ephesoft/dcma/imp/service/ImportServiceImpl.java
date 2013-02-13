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

package com.ephesoft.dcma.imp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.annotation.PostProcess;
import com.ephesoft.dcma.core.annotation.PreProcess;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.da.dao.BatchInstanceDao;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.imp.FolderImporter;
import com.ephesoft.dcma.util.BackUpFileService;

/**
 * This class is used to import the batch folder wiqth its contents.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.imp.service.ImportService
 */
public class ImportServiceImpl implements ImportService, ICommonConstants {

	/**
	 * An instance of Logger for proper logging using slf4j.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ImportServiceImpl.class);

	/**
	 * An instance of {@link BatchInstanceDao}.
	 */
	@Autowired
	private BatchInstanceDao batchInstanceDao;

	/**
	 * An instance of {@link FolderImporter}.
	 */
	@Autowired
	@Qualifier("folderImporter")
	private FolderImporter folderImporter;

	/**
	 * Instance of {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * To get the xml file before processing.
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 */
	@PreProcess
	public void preProcess(final BatchInstanceID batchInstanceID, final String pluginWorkflow) {
		Assert.notNull(batchInstanceID);
		final String batchInstanceIdentifier = batchInstanceID.getID();
		BackUpFileService.backUpBatch(batchInstanceIdentifier, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));
	}
	
	/**
	 * To get the xml after processing.
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 */
	@PostProcess
	public void postProcess(final BatchInstanceID batchInstanceID, final String pluginWorkflow) {
		Assert.notNull(batchInstanceID);
		final String batchInstanceIdentifier = batchInstanceID.getID();
		BackUpFileService.backUpBatch(batchInstanceIdentifier, pluginWorkflow, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));
	}

	/**
	 * This method is used to import all the images from the unc folder to batch instance identifier folder.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 * @throws DCMAException If any error or exception occurs while importing.
	 */
	@Override
	public void importBatchFolder(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException {
		try {
			final BatchInstance batchInstance = batchInstanceDao.getBatchInstancesForIdentifier(batchInstanceID.getID());
			final String batchInstanceIdentifier = batchInstance.getIdentifier();
			final String folderToBeMoved = batchInstance.getUncSubfolder();
			folderImporter.copyAndMove(batchInstance, folderToBeMoved, batchInstanceIdentifier);
		} catch (final Exception ex) {
			final String message = ex.getMessage();
			LOGGER.error(message, ex);
			throw new DCMAException("Exception in import. " + message, ex);
		}

	}

	/**
	 * This method is used to convert all the unc sub folder images in single tif files if they are multi page tif files.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 * @throws DCMAException If any error or exception occurs while importing.
	 */
	@Override
	public void importMultiPage(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException {
		final BatchInstance batchInstance = batchInstanceDao.getBatchInstancesForIdentifier(batchInstanceID.getID());
		try {
			final String folderPath = batchInstance.getUncSubfolder();
			folderImporter.createMultiPageTiff(batchInstance, folderPath);
		} catch (final Exception ex) {
			final String message = ex.getMessage();
			LOGGER.error(message, ex);
			throw new DCMAException("Exception in breaking multi page tiff file. " + message, ex);
		}

	}
}
