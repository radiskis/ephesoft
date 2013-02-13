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

package com.ephesoft.dcma.boxexport.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ephesoft.dcma.boxexport.BoxExporter;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.annotation.PostProcess;
import com.ephesoft.dcma.core.annotation.PreProcess;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.util.BackUpFileService;

/**
 * This class is responsible for uploading all the output files to the repository folder. This will reads the batch.xml file. It
 * finds the names of multipage tif and pdf files from the batch.xml. Then it upload these files to the repository main root folder. At
 * a time it will upload only pdf or tif files.
 * 
 * @author Ephesoft
 * 
 * @version 1.0
 * @see com.ephesoft.dcma.boxexport.BoxExporter
 */
public class BoxExportServiceImpl implements BoxExportService, ICommonConstants {

	private static final Logger LOGGER = LoggerFactory.getLogger(BoxExportServiceImpl.class);

	/**
	 * Instance of {@link BoxExporter}
	 */
	@Autowired
	private BoxExporter boxExporter;

	/**
	 * Instance of {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	@PreProcess
	public void preProcess(final BatchInstanceID batchInstanceID, final String pluginWorkflow) {
		Assert.notNull(batchInstanceID);
		final String batchInstanceIdentifier = batchInstanceID.getID();
		BackUpFileService.backUpBatch(batchInstanceIdentifier, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));
	}

	@PostProcess
	public void postProcess(final BatchInstanceID batchInstanceID, final String pluginWorkflow) {
		Assert.notNull(batchInstanceID);
		final String batchInstanceIdentifier = batchInstanceID.getID();
		BackUpFileService.backUpBatch(batchInstanceIdentifier, pluginWorkflow, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));
	}

	/* (non-Javadoc)
	 * @see com.ephesoft.dcma.boxexport.service.BoxExportService#boxExport(com.ephesoft.dcma.da.id.BatchInstanceID, java.lang.String)
	 */
	public void boxExport(BatchInstanceID batchInstanceID, String pluginWorkflow) throws DCMAException {
		String batchIdentifier = batchInstanceID.getID();
		try {
			LOGGER.info("Exporting files for batch instance :: " + batchIdentifier);
			boxExporter.exportFiles(batchIdentifier);
			LOGGER.info("Successfully completed exporting files for batch instance :: " + batchIdentifier);
		} catch (Exception e) {
			String errorMessage = "Error exporting files to box repository for batch instance :: " + batchIdentifier + e.getMessage();
			LOGGER.error(errorMessage, e);
			throw new DCMAException(errorMessage, e);
		}

	}

}
