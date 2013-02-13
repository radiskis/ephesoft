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

package com.ephesoft.dcma.tesseract.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.annotation.PostProcess;
import com.ephesoft.dcma.core.annotation.PreProcess;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.tesseract.TesseractReader;
import com.ephesoft.dcma.util.BackUpFileService;

/**
 * This service is used to generate HOCR files(html files with HOCR text) from image files. The service reads the image files from
 * batch xml and generates HOCR file for each image file.It also updates the batch xml with the name of newly created HOCR file.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.tesseract.service.TesseractService
 */
public class TesseractServiceImpl implements TesseractService {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(TesseractServiceImpl.class);

	/**
	 * tesseractReader {@link TesseractReader}.
	 */
	@Autowired
	private transient TesseractReader tesseractReader;

	/**
	 * Instance of {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * Pre-processing method.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 */
	@PreProcess
	public void preProcess(final BatchInstanceID batchInstanceID, String pluginWorkflow) {
		Assert.notNull(batchInstanceID);
		final String batchInstanceIdentifier = batchInstanceID.getID();
		BackUpFileService.backUpBatch(batchInstanceIdentifier, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));
	}

	/**
	 * Post-processing method.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 */
	@PostProcess
	public void postProcess(final BatchInstanceID batchInstanceID, String pluginWorkflow) {
		Assert.notNull(batchInstanceID);
		final String batchInstanceIdentifier = batchInstanceID.getID();
		BackUpFileService.backUpBatch(batchInstanceIdentifier, pluginWorkflow, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));
	}

	/**
	 * This method generates the HOCR files for each image file read from batch xml and updates the same batch XML with the name of
	 * HOCR file.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 * @throws DCMAException in case of error
	 */
	@Override
	public void generateHOCRFiles(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException {
		try {
			tesseractReader.readOCR(batchInstanceID.getID(), pluginWorkflow);
		} catch (Exception e) {
			LOGGER.error("Uncaught Exception in readOCR method " + e.getMessage(), e);
			throw new DCMAException(e.getMessage(), e);
		}
	}

	/**
	 * This method generates the HOCR files for image file and generates the output hocr file at the output folder location.
	 * 
	 * @param actualFolderLocation {@link String}
	 * @param colorSwitch {@link String}
	 * @param imageFile {@link String}
	 * @param batchInstanceThread {@link BatchInstanceThread}
	 * @param outputFolderLocation {@link String}
	 * @param cmdLanguage {@link String}
	 * @param tesseractVersion {@link String}
	 * @throws DCMAException in case of error
	 */
	@Override
	public void createOCR(String actualFolderLocation, String colorSwitch, String imageName, BatchInstanceThread batchInstanceThread,
			String outputFolderLocation, String cmdLanguage, String tesseractVersion) throws DCMAException {
		try {
			tesseractReader.createOCR(actualFolderLocation, colorSwitch, imageName, batchInstanceThread, outputFolderLocation,
					cmdLanguage, tesseractVersion);
		} catch (Exception e) {
			LOGGER.error("Exception occurs in creating OCR file " + e.getMessage(), e);
			throw new DCMAException(e.getMessage(), e);
		}
	}

}
