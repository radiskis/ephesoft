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

import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.da.id.BatchInstanceID;

/**
 * This service is used to generate HOCR files(html files with HOCR text) from image files. The service reads the image files from
 * batch xml and generates HOCR file for each image file.It also updates the batch xml with the name of newly created HOCR file.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.tesseract.service.TesseractServiceImpl
 */
public interface TesseractService {

	/**
	 * This method generates the HOCR files for each image file read from batch xml and updates the same batch XML with the name of
	 * HOCR file.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 * @throws DCMAException
	 */
	void generateHOCRFiles(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException;
	
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
	 * @throws DCMAException
	 */
	void createOCR(final String actualFolderLocation, String colorSwitch, String imageFile,
			BatchInstanceThread batchInstanceThread, String outputFolderLocation, String cmdLanguage, String tesseractVersion)
			throws DCMAException;
}
