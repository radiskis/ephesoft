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

package com.ephesoft.dcma.imagemagick.service;

import java.io.File;
import java.util.List;

import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.common.ImageType;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.id.BatchClassID;
import com.ephesoft.dcma.da.id.BatchInstanceID;

/**
 * This service provides image processing APIs.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.imagemagick.service.ImageProcessServiceImpl
 * 
 */
public interface ImageProcessService {

	/**
	 * This method creates OCR of input image of a batch for a particular plugin workflow.
	 * 
	 * @param batchInstanceID
	 * @param pluginWorkflow
	 * @param imageType
	 * @throws DCMAException
	 */
	void createOcrInputImages(final BatchInstanceID batchInstanceID, final String pluginWorkflow, ImageType imageType)
			throws DCMAException;

	/**
	 * This method creates thumbnails of input image of a batch for a particular plugin workflow.
	 * 
	 * @param batchInstanceID
	 * @param pluginWorkflow
	 * @throws DCMAException
	 */
	void createThumbnails(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException;
	
	/**
	 * This method creates multi page files of a batch for a particular plugin workflow.
	 * 
	 * @param batchInstanceID
	 * @param pluginWorkflow
	 * @throws DCMAException
	 */
	void createMultiPageFiles(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException;

	/**
	 * This method creates display image of a batch for a particular plugin workflow.
	 * 
	 * @param batchInstanceID
	 * @param pluginWorkflow
	 * @throws DCMAException
	 */
	void createDisplayImages(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException;

	/**
	 * This method classifies image of a batch for a particular plugin workflow.
	 * 
	 * @param batchInstanceID
	 * @param pluginWorkflow
	 * @throws DCMAException
	 */
	void classifyImages(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException;

	/**
	 * This method rotates image of a batch for given document id and page id.
	 * 
	 * @param batchInstanceID
	 * @param documentId
	 * @param pageId
	 * @throws DCMAException
	 */
	void rotateImage(final BatchInstanceID batchInstanceID, String documentId, String pageId) throws DCMAException;

	/**
	 * This method rotates image at given path.
	 * 
	 * @param imagePath
	 * @throws DCMAException
	 */
	void rotateImage(String imagePath) throws DCMAException;

	/**
	 * This method generates thumbnails and pngs for given image.
	 * 
	 * @param imagePath
	 * @param thumbnailW
	 * @param thumbnailH
	 * @throws DCMAException
	 */
	void generateThumbnailsAndPNGsForImage(File imagePath, String thumbnailW, String thumbnailH) throws DCMAException;

	/**
	 * This method converts a given pdf into tiff. In case of a multi-page pdf single tiff's for each page will be created. The output
	 * files will be generated in the same folder.
	 * 
	 * @param batchClass
	 * @param imagePath
	 * @param batchInstanceThread
	 * @throws DCMAException
	 */
	void convertPdfOrMultiPageTiffToTiff(BatchClass batchClass, File imagePath, BatchInstanceThread batchInstanceThread)
			throws DCMAException;

	/**
	 * This method converts all the pdf's and tiff's placed inside folder to single page tiff's. In case of a multi-page pdf single
	 * tiff's for each page will be created. The output files will be generated in the same folder.
	 * 
	 * @param batchClassID
	 * @param folderPath
	 * @param batchInstanceThread
	 * @return List<File> - file paths of all original files.
	 * @throws DCMAException
	 */
	List<File> convertPdfOrMultiPageTiffToTiff(BatchClassID batchClassID, String folderPath, BatchInstanceThread batchInstanceThread)
			throws DCMAException;

	/**
	 * Method to generate the PNG for a tiff file at the same location as input file
	 * 
	 * @param imagePath
	 * @throws DCMAException
	 */
	void generatePNGForImage(final File imagePath) throws DCMAException;

}
