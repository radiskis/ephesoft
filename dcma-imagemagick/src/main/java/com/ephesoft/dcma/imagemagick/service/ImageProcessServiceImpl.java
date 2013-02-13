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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.annotation.PostProcess;
import com.ephesoft.dcma.core.annotation.PreProcess;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.ImageType;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.id.BatchClassID;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.imagemagick.IImageMagickCommonConstants;
import com.ephesoft.dcma.imagemagick.ImageMagicProperties;
import com.ephesoft.dcma.imagemagick.ImageRotator;
import com.ephesoft.dcma.imagemagick.MultiPageTiffPdfCreator;
import com.ephesoft.dcma.imagemagick.MultiPageToSinglePageConverter;
import com.ephesoft.dcma.imagemagick.ThumbnailPNGCreator;
import com.ephesoft.dcma.imagemagick.constant.ImageMagicKConstants;
import com.ephesoft.dcma.imagemagick.imageclassifier.ImageClassifier;
import com.ephesoft.dcma.imagemagick.impl.HOCRtoPDFCreator;
import com.ephesoft.dcma.imagemagick.impl.ITextPDFCreator;
import com.ephesoft.dcma.util.BackUpFileService;
import com.ephesoft.dcma.util.CustomFileFilter;
import com.ephesoft.dcma.util.FileUtils;

/**
 * This class performs the processing of images like creating OCR inputs, multipage files etc and extracting data from them.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.imagemagick.service.ImageProcessService 
 */
public class ImageProcessServiceImpl extends Thread implements ImageProcessService {
	/**
	 * An instance of Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageProcessServiceImpl.class);

	/**
	 * An instance of {@link BatchSchemaService}.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * An instance of {@link ImageRotator}.
	 */
	@Autowired
	private ImageRotator imageRotator;

	/**
	 * An instance of {@link ThumbnailPNGCreator}. 
	 */
	@Autowired
	private ThumbnailPNGCreator thumbnailPNGCreator;

	/**
	 * An instance of {@link MultiPageToSinglePageConverter}.
	 */
	@Autowired
	private MultiPageToSinglePageConverter multiPageToSinglePageConverter;

	/**
	 * An instance of {@link ImageClassifier}.
	 */
	@Autowired
	private ImageClassifier imageClassifier;

	/**
	 * An instance of {@link MultiPageTiffPdfCreator}.
	 */
	@Autowired
	private MultiPageTiffPdfCreator multipageTiffPdfCreator;

	/**
	 * An instance of {@link PluginPropertiesService}.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * An instance of {@link BatchClassService}.
	 */
	@Autowired
	private BatchClassService batchClassService;

	/**
	 * An instance of {@link HOCRtoPDFCreator}.
	 */
	@Autowired
	private HOCRtoPDFCreator hocrToPDFCreator;

	/**
	 * An instance of {@link ITextPDFCreator}.
	 */
	@Autowired
	private ITextPDFCreator iTextPDFCreator;

	/**
	 * Instance of {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * To get the xml file before processing.
	 * @param batchInstanceID
	 * @param pluginWorkflow
	 * @throws DCMAApplicationException
	 */
	@PreProcess
	public void preProcess(final BatchInstanceID batchInstanceID, String pluginWorkflow) throws DCMAApplicationException {
		Assert.notNull(batchInstanceID);
		final String batchInstanceIdentifier = batchInstanceID.getID();
		BackUpFileService.backUpBatch(batchInstanceIdentifier, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));
	}

	/**
	 * To get the xml file after processing.
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
	 * This method creates OCR of input image of a batch for a particular plugin workflow.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 * @param imageType {@link ImageType}
	 * @throws DCMAException
	 */
	@Override
	public void createOcrInputImages(final BatchInstanceID batchInstanceID, final String pluginWorkflow, ImageType imageType)
			throws DCMAException {
		try {
			String sBatchFolder = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceID.getID()) + File.separator
					+ batchInstanceID;
			String generateDisplayPng = multipageTiffPdfCreator.getGenerateDisplayPng();
			String inputParameters = multipageTiffPdfCreator.getInputParameters();
			String outputParameters = multipageTiffPdfCreator.getOutputParameters();
			thumbnailPNGCreator.generateFullFiles(sBatchFolder, batchInstanceID.getID(), batchSchemaService,
					IImageMagickCommonConstants.OCR_INPUT_FILE, ImageMagicKConstants.CREATE_OCR_INPUT_PLUGIN, pluginWorkflow,
					generateDisplayPng, inputParameters, outputParameters);
		} catch (Exception ex) {
			LOGGER.error(ImageMagicKConstants.ERROR_IN_PNG_FILE_GENERATION, ex);
			throw new DCMAException(ImageMagicKConstants.ERROR_IN_PNG_FILE_GENERATION, ex);
		}

	}

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
	@Override
	public BatchInstanceThread createCompThumbForImage(final String batchInstanceIdentifier, final String folderPath,
			final String[][] sListOfTiffFiles, final String outputImageParameters, final String compareThumbnailH,
			final String compareThumbnailW) throws DCMAException {
		try {
			BatchInstanceThread batchInstanceThread = thumbnailPNGCreator.generateThumbnailInternal(batchInstanceIdentifier,
					IImageMagickCommonConstants.THUMB_TYPE_COMP, compareThumbnailH, compareThumbnailW, outputImageParameters,
					sListOfTiffFiles);
			return batchInstanceThread;
		} catch (Exception ex) {
			LOGGER.error(ImageMagicKConstants.THUMBNALIS_CREATION_EROR_MSG + ex.getMessage(), ex);
			throw new DCMAException(ImageMagicKConstants.THUMBNALIS_CREATION_EROR_MSG + ex.getMessage(), ex);
		}
	}

	/**
	 * This method creates thumbnails of input image of a batch for a particular plugin workflow.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 * @throws DCMAException
	 */
	@Override
	public void createThumbnails(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException {
		try {
			String sBatchFolder = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceID.getID()) + File.separator
					+ batchInstanceID;
			thumbnailPNGCreator.generateThumbnail(sBatchFolder, batchInstanceID.getID(), batchSchemaService,
					IImageMagickCommonConstants.THUMB_TYPE_DISP, pluginWorkflow);
			if (pluginPropertiesService.getPropertyValue(batchInstanceID.getID(), ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN,
					ImageMagicProperties.CREATE_THUMBNAILS_SWITCH).equalsIgnoreCase(ImageMagicKConstants.ON_SWITCH)) {
				thumbnailPNGCreator.generateThumbnail(sBatchFolder, batchInstanceID.getID(), batchSchemaService,
						IImageMagickCommonConstants.THUMB_TYPE_COMP, pluginWorkflow);
			} else {
				LOGGER.info("Skipping creation of comparison thumbnails. Switch set as OFF");
			}
		} catch (Exception ex) {
			LOGGER.error(ImageMagicKConstants.THUMBNALIS_CREATION_EROR_MSG + ex.getMessage(), ex);
			throw new DCMAException(ImageMagicKConstants.THUMBNALIS_CREATION_EROR_MSG + ex.getMessage(), ex);
		}
	}

	/**
	 * This method creates multi page files of a batch for a particular plugin workflow.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 * @throws DCMAException
	 */
	@Override
	public void createMultiPageFiles(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException {

		String multipageTifSwitch = pluginPropertiesService.getPropertyValue(batchInstanceID.getID(),
				ImageMagicKConstants.CREATEMULTIPAGE_FILES_PLUGIN, ImageMagicProperties.CREATE_MULTIPAGE_TIFF_SWITCH);
		String checkPDFExportProcess = pluginPropertiesService.getPropertyValue(batchInstanceID.getID(),
				ImageMagicKConstants.CREATEMULTIPAGE_FILES_PLUGIN, ImageMagicProperties.CHECK_PDF_EXPORT_PROCESS);
		String pdfOptimizationParams = pluginPropertiesService.getPropertyValue(batchInstanceID.getID(),
				ImageMagicKConstants.CREATEMULTIPAGE_FILES_PLUGIN, ImageMagicProperties.PDF_OPTIMIZATION_PARAMETERS);
		String pdfOptimizationSwitch = pluginPropertiesService.getPropertyValue(batchInstanceID.getID(),
				ImageMagicKConstants.CREATEMULTIPAGE_FILES_PLUGIN, ImageMagicProperties.PDF_OPTIMIZATION_SWITCH);
		String ghostscriptPdfParameters = pluginPropertiesService.getPropertyValue(batchInstanceID.getID(),
				ImageMagicKConstants.CREATEMULTIPAGE_FILES_PLUGIN, ImageMagicProperties.GHOSTSCRIPT_COMMAND_PDF_PARAMETERS);

		try {
			multipageTiffPdfCreator.createMultiPageFiles(ghostscriptPdfParameters, batchInstanceID.getID(), pdfOptimizationParams,
					multipageTifSwitch, checkPDFExportProcess, pluginWorkflow, pdfOptimizationSwitch);
		} catch (Exception ex) {
			LOGGER.error("Problem generating overlayed Images exception->" + ex.getMessage());
			throw new DCMAException("Problem generating overlayed Images " + ex.getMessage(), ex);
		}
	}

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
	@Override
	public void createMultiPageFilesAPI(String ghostscriptPdfParameters, String pdfOptimizationParams, String multipageTifSwitch,
			String toolName, String pdfOptimizationSwitch, String workingDir, String outputDir, List<File> singlePageFiles,
			String batchInstanceIdentifier) throws DCMAException {
		try {
			multipageTiffPdfCreator.createMultiPageFilesAPI(ghostscriptPdfParameters, pdfOptimizationParams, multipageTifSwitch,
					toolName, pdfOptimizationSwitch, workingDir, outputDir, singlePageFiles, batchInstanceIdentifier);
		} catch (Exception ex) {
			LOGGER.error("Problem generating overlayed Images exception->" + ex.getMessage());
			throw new DCMAException("Problem generating overlayed Images " + ex.getMessage(), ex);
		}
	}

	/**
	 * This method creates display image of a batch for a particular plugin workflow.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 * @throws DCMAException
	 */
	@Override
	public void createDisplayImages(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException {
		try {
			String sBatchFolder = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceID.getID()) + File.separator
					+ batchInstanceID;
			String generateDisplayPng = multipageTiffPdfCreator.getGenerateDisplayPng();
			String inputParameters = multipageTiffPdfCreator.getInputParameters();
			String outputParameters = multipageTiffPdfCreator.getOutputParameters();
			thumbnailPNGCreator.generateFullFiles(sBatchFolder, batchInstanceID.getID(), batchSchemaService,
					IImageMagickCommonConstants.DISPLAY_IMAGE, ImageMagicKConstants.CREATE_DISPLAY_IMAGE_PLUGIN, pluginWorkflow,
					generateDisplayPng, inputParameters, outputParameters);
		} catch (Exception ex) {
			LOGGER.error("Problem in generating Display File", ex);
			throw new DCMAException("Problem in generating Display File", ex);
		}
	}

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
	@Override
	public void classifyImagesAPI(String maxVal, String imMetric, String imFuzz, String batchInstanceIdentifier,
			String batchClassIdentifier, String sBatchFolder, List<Page> listOfPages) throws DCMAException {
		try {
			imageClassifier.classifyAllImgsOfBatchInternal(maxVal, imMetric, imFuzz, batchInstanceIdentifier, batchClassIdentifier,
					sBatchFolder, listOfPages);
		} catch (Exception ex) {
			LOGGER.error(ImageMagicKConstants.IMAGE_CLASSIFICATION_ERROR_MSG + ex.getMessage(), ex);
			throw new DCMAException(ImageMagicKConstants.IMAGE_CLASSIFICATION_ERROR_MSG + ex.getMessage(), ex);
		}

	}

	/**
	 * This method classifies image of a batch for a particular plugin workflow.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 * @throws DCMAException
	 */
	@Override
	public void classifyImages(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException {
		try {

			if (ImageMagicKConstants.ON_SWITCH.equalsIgnoreCase(pluginPropertiesService.getPropertyValue(batchInstanceID.getID(),
					ImageMagicKConstants.CLASSIFY_IMAGES_PLUGIN, ImageMagicProperties.CLASSIFY_IMAGES_SWITCH))) {
				String sBatchFolder = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceID.getID()) + File.separator
						+ batchInstanceID;
				LOGGER.info("sBatchFolder = " + sBatchFolder);
				imageClassifier.classifyAllImgsOfBatch(batchInstanceID.getID(), sBatchFolder);
			} else {
				LOGGER.info("Skipping image magic classification. Switch set as OFF");
			}
		} catch (Exception ex) {
			LOGGER.error(ImageMagicKConstants.IMAGE_CLASSIFICATION_ERROR_MSG + ex.getMessage(), ex);
			throw new DCMAException(ImageMagicKConstants.IMAGE_CLASSIFICATION_ERROR_MSG + ex.getMessage(), ex);
		}
	}

	/**
	 * This method rotates image of a batch for given document id and page id.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param documentId {@link String}
	 * @param pageId {@link String}
	 * @throws DCMAException
	 */
	@Override
	public void rotateImage(final BatchInstanceID batchInstanceID, String documentId, String pageId) throws DCMAException {
		String displayImageFilePath = null;
		String thumbnailFilePath = null;

		try {
			displayImageFilePath = batchSchemaService.getDisplayImageFilePath(batchInstanceID.getID(), documentId, pageId);
			thumbnailFilePath = batchSchemaService.getThumbnailFilePath(batchInstanceID.getID(), documentId, pageId);
			imageRotator.rotateImage(displayImageFilePath);
			imageRotator.rotateImage(thumbnailFilePath);
		} catch (Exception e) {
			LOGGER.error(ImageMagicKConstants.PROBLEM_ROTATING_IMAGES);
			throw new DCMAException(ImageMagicKConstants.PROBLEM_ROTATING_IMAGES, e);
		}

	}

	/**
	 * This method generates thumbnails and pngs for given image.
	 * 
	 * @param imagePath {@link File}
	 * @param thumbnailW {@link String}
	 * @param thumbnailH {@link String}
	 * @throws DCMAException
	 */
	@Override
	public void generateThumbnailsAndPNGsForImage(final File imagePath, final String thumbnailW, final String thumbnailH)
			throws DCMAException {
		// Runnable uploadRunnable = new Runnable() {
		//
		// @Override
		// public void run() {
		try {
			thumbnailPNGCreator.generateThumbnailsAndPNGForImage(imagePath, thumbnailW, thumbnailH);
		} catch (Exception e) {
			LOGGER.error("Problem generateThumbnailsAndPNGsForImage", e);
		}
		// }
		// };
		// Thread thread = new Thread(uploadRunnable);
		// thread.start();
		// LOGGER.info("Thread started for tumbnails and PNG creation for " + imagePath.getName());
	}

	/**
	 * This method rotates image at given path.
	 * 
	 * @param imagePath {@link String}
	 * @throws DCMAException
	 */
	@Override
	public void rotateImage(String imagePath) throws DCMAException {
		try {
			imageRotator.rotateImage(imagePath);
		} catch (Exception e) {
			LOGGER.error(ImageMagicKConstants.PROBLEM_ROTATING_IMAGES);
			throw new DCMAException(ImageMagicKConstants.PROBLEM_ROTATING_IMAGES, e);
		}
	}

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
	@Override
	public void convertPdfOrMultiPageTiffToTiff(BatchClass batchClass, File imagePath, File outputFilePath,
			BatchInstanceThread thread, Boolean allowPdfConversion) throws DCMAException {
		try {
			multiPageToSinglePageConverter.convertPdfOrMultiPageTiffToTiff(batchClass, imagePath, outputFilePath, thread,
					allowPdfConversion);
		} catch (DCMAApplicationException e) {
			throw new DCMAException(e.getMessage(), e);
		}
	}

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
	@Override
	public void convertPdfOrMultiPageTiffToTiffUsingIM(String inputParams, File imagePath, String outputParams, File outputFilePath,
			BatchInstanceThread thread) throws DCMAException {
		try {
			String imageName = imagePath.getAbsolutePath();
			int indexOf = imageName.toLowerCase(Locale.getDefault()).lastIndexOf(FileType.TIF.getExtensionWithDot());
			if (indexOf == -1) {
				indexOf = imageName.toLowerCase(Locale.getDefault()).lastIndexOf(FileType.TIFF.getExtensionWithDot());
				if (indexOf == -1) {
					indexOf = imageName.toLowerCase(Locale.getDefault()).lastIndexOf(FileType.PDF.getExtensionWithDot());
				}
			}
			if (indexOf == -1) {
				throw new DCMAException("Unsupported file format");
			}
			multiPageToSinglePageConverter.convertPdfOrMultiPageTiffToTiffUsingIM(inputParams, imagePath, outputParams,
					outputFilePath, thread, indexOf);
		} catch (DCMAApplicationException e) {
			throw new DCMAException(e.getMessage(), e);
		}
	}

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
	@Override
	public List<File> convertPdfOrMultiPageTiffToTiff(BatchClassID batchClassID, String folderPath, File testImageFile,
			Boolean isTestAdvancedKV, BatchInstanceThread thread) throws DCMAException {
		File folder = new File(folderPath);
		BatchClass batchClass = batchClassService.get(batchClassID.getID());
		List<File> allImageFiles = new ArrayList<File>();
		if (isTestAdvancedKV) {
			if (testImageFile.exists()) {
				replaceSubStringInFile(testImageFile, "-%04d", "");
				File htmlFile = new File(folderPath + File.separator
						+ FileUtils.changeFileExtension(testImageFile.getName(), FileType.HTML.getExtension()));
				File xmlFile = new File(folderPath + File.separator
						+ FileUtils.changeFileExtension(testImageFile.getName(), FileType.HTML.getExtension()));
				if (!htmlFile.exists() || !xmlFile.exists()) {
					allImageFiles.add(testImageFile);
					convertPdfOrMultiPageTiffToTiff(batchClass, testImageFile, null, thread, true);
				}

			} else {
				LOGGER.info("File doesn't exist = " + testImageFile.getAbsolutePath());
			}
		} else {
			String[] folderList = folder.list(new CustomFileFilter(false, FileType.PDF.getExtension(), FileType.TIF.getExtension(),
					FileType.TIFF.getExtension(), FileType.HTML.getExtensionWithDot(), FileType.XML.getExtensionWithDot()));
			for (String string : folderList) {
				File file = new File(folder.getAbsolutePath() + File.separator + string);
				replaceSubStringInFile(file, "-%04d", "");
			}
			allImageFiles = getAllImagesPathInFolder(folderPath);
			for (File imageFile : allImageFiles) {
				convertPdfOrMultiPageTiffToTiff(batchClass, imageFile, null, thread, true);
			}
		}
		return allImageFiles;
	}

	private List<File> getAllImagesPathInFolder(String testKvExtractionFolderPath) {
		List<File> allImageFiles = new ArrayList<File>();
		File folder = new File(testKvExtractionFolderPath);
		String[] imageNames = folder.list(new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(), FileType.TIFF
				.getExtensionWithDot(), FileType.PDF.getExtensionWithDot()));
		String[] htmlFiles = folder.list(new CustomFileFilter(false, FileType.HTML.getExtensionWithDot()));
		String[] xmlFiles = folder.list(new CustomFileFilter(false, FileType.XML.getExtensionWithDot()));
		for (String fileName : imageNames) {
			isHtmlFilesGenerated(testKvExtractionFolderPath, allImageFiles, htmlFiles, xmlFiles, fileName);
		}
		return allImageFiles;
	}

	private void isHtmlFilesGenerated(String testKvExtractionFolderPath, List<File> allImageFiles, String[] htmlFiles,
			String[] xmlFiles, String fileName) {
		boolean htmlFilesGenerated = false;
		// boolean xmlFilesGenerated = false;
		for (String htmlFile : htmlFiles) {
			if (htmlFile.substring(0, htmlFile.lastIndexOf('.')).equalsIgnoreCase(fileName.substring(0, fileName.lastIndexOf('.')))) {
				htmlFilesGenerated = true;
				break;
			}
		}
		for (String xmlFile : xmlFiles) {
			if (xmlFile.substring(0, xmlFile.lastIndexOf('.')).equalsIgnoreCase(fileName.substring(0, fileName.lastIndexOf('.')))) {
				// xmlFilesGenerated = true;
				break;
			}
		}
		if (!(htmlFilesGenerated)) {
			File file = new File(testKvExtractionFolderPath + File.separator + fileName);
			allImageFiles.add(file);
		}
	}

	private void replaceSubStringInFile(File inputFile, String toBeReplaced, String newValue) {
		File newFile = inputFile;
		if (inputFile.getName().contains(toBeReplaced)) {
			String fileName = inputFile.getName();
			fileName = fileName.replaceAll(toBeReplaced, newValue);
			newFile = new File(inputFile.getParent() + File.separator + fileName);
			inputFile.renameTo(newFile);
		}
	}

	/**
	 * Method to generate the PNG for a tiff file at the same location as input file.
	 * 
	 * @param imagePath {@link File}
	 * @throws DCMAException
	 */
	@Override
	public void generatePNGForImage(final File imagePath) throws DCMAException {
		try {
			thumbnailPNGCreator.generatePNGForImage(imagePath);
		} catch (Exception ex) {
			LOGGER.error(ImageMagicKConstants.ERROR_IN_PNG_FILE_GENERATION, ex);
			throw new DCMAException(ImageMagicKConstants.ERROR_IN_PNG_FILE_GENERATION, ex);
		}
	}

	/**
	 * This method converts a given pdf into tiff. The output files will be generated in the same folder.
	 * 
	 * @param batchClass {@link BatchClass}
	 * @param imagePath {@link File}
	 * @param threadList {@link BatchInstanceThread}
	 * @throws DCMAException
	 */
	@Override
	public void convertPdfToSinglePageTiffs(BatchClass batchClass, File imagePath, BatchInstanceThread thread) throws DCMAException {
		try {
			multiPageToSinglePageConverter.convertPdfToSinglePageTiffs(batchClass, imagePath, null, thread);
		} catch (DCMAApplicationException e) {
			throw new DCMAException(e.getMessage(), e);
		}
	}

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
	@Override
	public void convertPdfToSinglePageTiffsUsingGSAPI(String inputParams, File imagePath, String outputParams, File outputFilePath,
			BatchInstanceThread thread) throws DCMAException {
		try {
			multiPageToSinglePageConverter.convertPdfToSinglePageTiffsUsingGSAPI(inputParams, imagePath, outputParams, outputFilePath,
					thread);
		} catch (DCMAApplicationException e) {
			throw new DCMAException(e.getMessage(), e);
		}
	}

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
	@Override
	public void createSearchablePDF(String checkColorImage, String checkSearchableImage, String batchInstanceFolder, String[] pages,
			BatchInstanceThread batchInstanceThread, String documentId) throws DCMAException {
		try {
			hocrToPDFCreator.createPDFFromHOCR(checkColorImage, checkSearchableImage, batchInstanceFolder, pages, batchInstanceThread,
					documentId);
		} catch (DCMAApplicationException e) {
			throw new DCMAException("Error in creating searchable pdf file" + e.getMessage(), e);
		}

	}

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
	@Override
	public void createTifToPDF(String pdfGeneratorEngine, String[] files, BatchInstanceThread pdfBatchInstanceThread,
			String inputParams, String outputParams) throws DCMAException {
		if (ImageMagicKConstants.IMAGE_MAGICK.equalsIgnoreCase(pdfGeneratorEngine)) {
			try {
				String inputFilePath = files[0];
				String outputFilePath = files[1];
				multiPageToSinglePageConverter.convertInputFileToOutputFileUsingIM(inputParams, inputFilePath, outputParams,
						outputFilePath, pdfBatchInstanceThread);
			} catch (DCMAApplicationException e) {
				throw new DCMAException("Error while generating output file", e);
			}
		} else if (ImageMagicKConstants.ITEXT.equalsIgnoreCase(pdfGeneratorEngine)) {
			iTextPDFCreator.createPDFUsingIText(files, pdfBatchInstanceThread);
		}

	}

}

