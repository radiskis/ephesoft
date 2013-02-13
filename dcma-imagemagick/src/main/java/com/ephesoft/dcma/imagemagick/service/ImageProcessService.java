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

package com.ephesoft.dcma.imagemagick.service;

import java.io.File;
import java.util.List;

import com.ephesoft.dcma.batch.schema.Page;
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
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 * @param imageType {@link ImageType}
	 * @throws DCMAException
	 */
	void createOcrInputImages(final BatchInstanceID batchInstanceID, final String pluginWorkflow, ImageType imageType)
			throws DCMAException;

	/**
	 * This method creates thumbnails of input image of a batch for a particular plugin workflow.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 * @throws DCMAException
	 */
	void createThumbnails(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException;

	/**
	 * This method creates multi page files of a batch for a particular plugin workflow.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 * @throws DCMAException
	 */
	void createMultiPageFiles(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException;

	/**
	 * This method creates display image of a batch for a particular plugin workflow.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 * @throws DCMAException
	 */
	void createDisplayImages(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException;

	/**
	 * This method classifies image of a batch for a particular plugin workflow.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 * @throws DCMAException
	 */
	void classifyImages(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException;

	/**
	 * This method rotates image of a batch for given document id and page id.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param documentId {@link String}
	 * @param pageId {@link String}
	 * @throws DCMAException
	 */
	void rotateImage(final BatchInstanceID batchInstanceID, String documentId, String pageId) throws DCMAException;

	/**
	 * This method rotates image at given path.
	 * 
	 * @param imagePath {@link String}
	 * @throws DCMAException
	 */
	void rotateImage(String imagePath) throws DCMAException;

	/**
	 * This method generates thumbnails and pngs for given image.
	 * 
	 * @param imagePath {@link File}
	 * @param thumbnailW {@link String}
	 * @param thumbnailH {@link String}
	 * @throws DCMAException
	 */
	void generateThumbnailsAndPNGsForImage(File imagePath, String thumbnailW, String thumbnailH) throws DCMAException;

	/**
	 * This method converts a given Multi page tiff into single page tiffs. Also depending upon the parameter allowPdfConversion it can
	 * convert a multi page pdf to single page tiffs too. The output files will be generated in the same folder.
	 * 
	 * @param batchClass {@link BatchClass}
	 * @param imagePath {@link File}
	 * @param outputFilePath (@link File}
	 * @param batchInstanceThread {@link BatchInstanceThread}
	 * @param allowPdfConversion {@link Boolean}
	 * @throws DCMAException
	 */
	void convertPdfOrMultiPageTiffToTiff(BatchClass batchClass, File imagePath, File outputFilePath,
			BatchInstanceThread batchInstanceThread, Boolean allowPdfConversion) throws DCMAException;

	/**
	 * Method used for web services.
	 * 
	 * @param inputParams {@link String}
	 * @param imagePath {@link File}
	 * @param outputParams {@link String}
	 * @param outputFilePath {@link File}
	 * @param thread {@link BatchInstanceThread}
	 * @throws DCMAException
	 */
	void convertPdfOrMultiPageTiffToTiffUsingIM(String inputParams, File imagePath, String outputParams, File outputFilePath,
			BatchInstanceThread thread) throws DCMAException;

	/**
	 * This method converts all the pdf's and tiff's placed inside folder to single page tiff's. In case of a multi-page pdf single
	 * tiff's for each page will be created. The output files will be generated in the same folder.
	 * 
	 * @param batchClassID {@link BatchClassID}
	 * @param folderPath {@link String}
	 * @param testImageFile {@link File}
	 * @param isTestAdvancedKV {@link Boolean}
	 * @param batchInstanceThread {@link BatchInstanceThread}
	 * @return List<{@link File}> - file paths of all original files.
	 * @throws DCMAException
	 */
	List<File> convertPdfOrMultiPageTiffToTiff(BatchClassID batchClassID, String folderPath, File testImageFile,
			Boolean isTestAdvancedKV, BatchInstanceThread batchInstanceThread) throws DCMAException;

	/**
	 * Method to generate the PNG for a tiff file at the same location as input file.
	 * 
	 * @param imagePath {@link File}
	 * @throws DCMAException
	 */
	void generatePNGForImage(final File imagePath) throws DCMAException;

	/**
	 * This method converts a given pdf into tiff. The output files will be generated in the same folder.
	 * 
	 * @param batchClass {@link BatchClass}
	 * @param imagePath {@link File}
	 * @param threadList {@link BatchInstanceThread}
	 * @throws DCMAException
	 */
	void convertPdfToSinglePageTiffs(BatchClass batchClass, File imagePath, BatchInstanceThread threadList) throws DCMAException;

	/**
	 * This method converts a given pdf into tiff. The output files will be generated in the output folder specified. Called from Web
	 * Service API.
	 * 
	 * @param inputParams {@link String}
	 * @param imagePath {@link File}
	 * @param outputParams {@link String}
	 * @param outputFilePath {@link File}
	 * @param thread {@link BatchInstanceThread}
	 * @throws DCMAException
	 */
	void convertPdfToSinglePageTiffsUsingGSAPI(String inputParams, File imagePath, String outputParams, File outputFilePath,
			BatchInstanceThread thread) throws DCMAException;

	/**
	 * This API creates the searchable pdf for the image files on the basis of color image and searchable image at the output folder
	 * location.
	 * 
	 * @param checkColorImage {@link String}
	 * @param checkSearchableImage {@link String}
	 * @param inputFolderLocation {@link String}
	 * @param imageFiles String[]
	 * @param batchInstanceThread {@link BatchInstanceThread}
	 * @param documentId {@link String}
	 */
	void createSearchablePDF(String checkColorImage, String checkSearchableImage, String inputFolderLocation, String[] imageFiles,
			BatchInstanceThread batchInstanceThread, String documentId) throws DCMAException;

	/**
	 * Method to create thumbnails for web service Image Classification API.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @param folderPath {@link String}
	 * @param sListOfTiffFiles {@link String [][]}
	 * @param outputImageParameters {@link String}
	 * @param compareThumbnailH {@link String}
	 * @param compareThumbnailW {@link String}
	 * @return {@link BatchInstanceThread}
	 * @throws DCMAException
	 */
	BatchInstanceThread createCompThumbForImage(String batchInstanceIdentifier, String folderPath, String[][] sListOfTiffFiles,
			String outputImageParameters, String compareThumbnailH, String compareThumbnailW) throws DCMAException;

	/**
	 * Method to perform image classification of PP module for web services.
	 * @param maxVal {@link String}
	 * @param imMetric {@link String}
	 * @param imFuzz {@link String}
	 * @param batchInstanceIdentifier {@link String}
	 * @param batchClassIdentifier {@link String}
	 * @param sBatchFolder {@link String}
	 * @param listOfPages {@link List<Page>}
	 * @throws DCMAException
	 */
	void classifyImagesAPI(String maxVal, String imMetric, String imFuzz, String batchInstanceIdentifier, String batchClassIdentifier,
			String sBatchFolder, List<Page> listOfPages) throws DCMAException;

	/**
	 * API for creating pdf from single tif file.
	 * 
	 * @param pdfGeneratorEngine {@link String}
	 * @param files {@link String[]}
	 * @param pdfBatchInstanceThread {@link BatchInstanceThread}
	 * @param inputParams {@link String}
	 * @param outputParams {@link String}
	 * @throws DCMAException if exception or error occur
	 */
	void createTifToPDF(String pdfGeneratorEngine, String[] files, BatchInstanceThread pdfBatchInstanceThread, String inputParams,
			String outputParams) throws DCMAException;

	/**
	 * API for create multipage files web service.
	 * @param ghostscriptPdfParameters {@link String}
	 * @param pdfOptimizationParams {@link String}
	 * @param multipageTifSwitch {@link String}
	 * @param toolName {@link String}
	 * @param pdfOptimizationSwitch {@link String}
	 * @param workingDir {@link String}
	 * @param outputDir {@link String}
	 * @param singlePageFiles {@link List<File>}
	 * @param batchInstanceIdentifier {@link String}
	 * @throws DCMAException
	 */
	void createMultiPageFilesAPI(String ghostscriptPdfParameters, String pdfOptimizationParams, String multipageTifSwitch,
			String toolName, String pdfOptimizationSwitch, String workingDir, String outputDir, List<File> singlePageFiles,
			String batchInstanceIdentifier) throws DCMAException;

}
