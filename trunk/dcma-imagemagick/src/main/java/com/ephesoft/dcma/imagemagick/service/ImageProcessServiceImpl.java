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
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

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
import com.ephesoft.dcma.imagemagick.IImageMagickCommonConstants;
import com.ephesoft.dcma.imagemagick.ImageMagicProperties;
import com.ephesoft.dcma.imagemagick.ImageRotator;
import com.ephesoft.dcma.imagemagick.MultiPageTiffPdfCreator;
import com.ephesoft.dcma.imagemagick.MultiPageToSinglePageConverter;
import com.ephesoft.dcma.imagemagick.ThumbnailPNGCreator;
import com.ephesoft.dcma.imagemagick.constant.ImageMagicKConstants;
import com.ephesoft.dcma.imagemagick.imageClassifier.ImageClassifier;
import com.ephesoft.dcma.util.BackUpFileService;
import com.ephesoft.dcma.util.CustomFileFilter;

public class ImageProcessServiceImpl implements ImageProcessService {

	private static final String PROBLEM_ROTATING_IMAGES = "Problem rotating images";

	private static final String ERROR_IN_PNG_FILE_GENERATION = "Problem in generating PNG File";

	private static final String ON = "ON";

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageProcessServiceImpl.class);

	@Autowired
	private BatchSchemaService batchSchemaService;

	@Autowired
	private ImageRotator imageRotator;

	@Autowired
	private ThumbnailPNGCreator thumbnailPNGCreator;

	@Autowired
	private MultiPageToSinglePageConverter multiPageToSinglePageConverter;

	@Autowired
	private ImageClassifier imageClassifier;

	@Autowired
	private MultiPageTiffPdfCreator multipageTiffPdfCreator;

	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	@Autowired
	private BatchClassService batchClassService;

	@PreProcess
	public void preProcess(final BatchInstanceID batchInstanceID, String pluginWorkflow) throws DCMAApplicationException {
		Assert.notNull(batchInstanceID);
		BackUpFileService.backUpBatch(batchInstanceID.getID());
	}

	@PostProcess
	public void postProcess(final BatchInstanceID batchInstanceID, String pluginWorkflow) {
		Assert.notNull(batchInstanceID);
		BackUpFileService.backUpBatch(batchInstanceID.getID(), pluginWorkflow);
	}

	@Override
	public void createOcrInputImages(final BatchInstanceID batchInstanceID, final String pluginWorkflow, ImageType imageType)
			throws DCMAException {
		try {
			String sBatchFolder = batchSchemaService.getLocalFolderLocation() + File.separator + batchInstanceID;
			thumbnailPNGCreator.generateFullFiles(sBatchFolder, batchInstanceID.getID(), batchSchemaService,
					IImageMagickCommonConstants.OCR_INPUT_FILE, ImageMagicKConstants.CREATE_OCR_INPUT_PLUGIN, pluginWorkflow);
		} catch (Exception ex) {
			LOGGER.error(ERROR_IN_PNG_FILE_GENERATION, ex);
			throw new DCMAException(ERROR_IN_PNG_FILE_GENERATION, ex);
		}

	}

	@Override
	public void createThumbnails(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException {
		try {
			String sBatchFolder = batchSchemaService.getLocalFolderLocation() + File.separator + batchInstanceID;
			thumbnailPNGCreator.generateThumbnail(sBatchFolder, batchInstanceID.getID(), batchSchemaService,
					IImageMagickCommonConstants.THUMB_TYPE_DISP, pluginWorkflow);
			if (pluginPropertiesService.getPropertyValue(batchInstanceID.getID(), ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN,
					ImageMagicProperties.CREATE_THUMBNAILS_SWITCH).equalsIgnoreCase(ON)) {
				thumbnailPNGCreator.generateThumbnail(sBatchFolder, batchInstanceID.getID(), batchSchemaService,
						IImageMagickCommonConstants.THUMB_TYPE_COMP, pluginWorkflow);
			} else {
				LOGGER.info("Skipping creation of comparison thumbnails. Switch set as OFF");
			}
		} catch (Exception ex) {
			LOGGER.error("Problem generating thumbnalis exception->" + ex.getMessage(), ex);
			throw new DCMAException("Problem generating thumbnalis exception->" + ex.getMessage(), ex);
		}
	}

	@Override
	public void createMultiPageFiles(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException {
		String multipageTifSwitch = pluginPropertiesService.getPropertyValue(batchInstanceID.getID(),
				ImageMagicKConstants.CREATEMULTIPAGE_FILES_PLUGIN, ImageMagicProperties.CREATE_MULTIPAGE_TIFF_SWITCH);
		String checkPDFExportProcess = pluginPropertiesService.getPropertyValue(batchInstanceID.getID(),
				ImageMagicKConstants.CREATEMULTIPAGE_FILES_PLUGIN, ImageMagicProperties.CHECK_PDF_EXPORT_PROCESS);
		String checkColouredPDF = pluginPropertiesService.getPropertyValue(batchInstanceID.getID(),
				ImageMagicKConstants.CREATEMULTIPAGE_FILES_PLUGIN, ImageMagicProperties.CHECK_COLOURED_PDF);
		String checkSearchablePDF = pluginPropertiesService.getPropertyValue(batchInstanceID.getID(),
				ImageMagicKConstants.CREATEMULTIPAGE_FILES_PLUGIN, ImageMagicProperties.CHECK_SEARCHABLE_PDF);
		try {
			String sBatchFolder = batchSchemaService.getLocalFolderLocation() + File.separator + batchInstanceID;
			multipageTiffPdfCreator.createMultiPageFiles(sBatchFolder, batchInstanceID.getID(), batchSchemaService,
					multipageTifSwitch, checkPDFExportProcess, checkColouredPDF, checkSearchablePDF, pluginWorkflow);

		} catch (Exception ex) {
			LOGGER.error("Problem generating overlayed Images exception->" + ex.getMessage());
			throw new DCMAException("Problem generating overlayed Images " + ex.getMessage(), ex);
		}

	}

	@Override
	public void createDisplayImages(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException {
		try {
			String sBatchFolder = batchSchemaService.getLocalFolderLocation() + File.separator + batchInstanceID;
			thumbnailPNGCreator.generateFullFiles(sBatchFolder, batchInstanceID.getID(), batchSchemaService,
					IImageMagickCommonConstants.DISPLAY_IMAGE, ImageMagicKConstants.CREATE_DISPLAY_IMAGE_PLUGIN, pluginWorkflow);
		} catch (Exception ex) {
			LOGGER.error("Problem in generating Display File");
			throw new DCMAException("Problem in generating Display File", ex);
		}
	}

	@Override
	public void classifyImages(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException {
		if (pluginPropertiesService.getPropertyValue(batchInstanceID.getID(), ImageMagicKConstants.CLASSIFY_IMAGES_PLUGIN,
				ImageMagicProperties.CLASSIFY_IMAGES_SWITCH).equalsIgnoreCase(ON)) {
			try {
				String sBatchFolder = batchSchemaService.getLocalFolderLocation() + File.separator + batchInstanceID;
				LOGGER.info("sBatchFolder = " + sBatchFolder);
				imageClassifier.classifyAllImgsOfBatch(batchInstanceID.getID(), sBatchFolder);
			} catch (Exception ex) {
				LOGGER.error("Problem in Image Classification");
				throw new DCMAException("Problem in Image Classification", ex);
			}
		} else {
			LOGGER.info("Skipping image magic classification. Switch set as OFF");
		}
	}

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
			LOGGER.error(PROBLEM_ROTATING_IMAGES);
			throw new DCMAException(PROBLEM_ROTATING_IMAGES, e);
		}

	}

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

	@Override
	public void rotateImage(String imagePath) throws DCMAException {
		try {
			imageRotator.rotateImage(imagePath);
		} catch (Exception e) {
			LOGGER.error(PROBLEM_ROTATING_IMAGES);
			throw new DCMAException(PROBLEM_ROTATING_IMAGES, e);
		}
	}

	@Override
	public void convertPdfOrMultiPageTiffToTiff(BatchClass batchClass, File imagePath, BatchInstanceThread thread)
			throws DCMAException {
		try {
			multiPageToSinglePageConverter.convertPdfOrMultiPageTiffToTiff(batchClass, imagePath, null, thread);
		} catch (DCMAApplicationException e) {
			throw new DCMAException(e.getMessage());
		}
	}

	@Override
	public List<File> convertPdfOrMultiPageTiffToTiff(BatchClassID batchClassID, String folderPath, BatchInstanceThread thread)
			throws DCMAException {
		File folder = new File(folderPath);
		String[] folderList = folder.list(new CustomFileFilter(false, FileType.PDF.getExtension(), FileType.TIF.getExtension(),
				FileType.TIFF.getExtension(), FileType.HTML.getExtensionWithDot(), FileType.XML.getExtensionWithDot()));
		for (String string : folderList) {
			File file = new File(folder.getAbsolutePath() + File.separator + string);
			replaceSubStringInFile(file, "-%04d", "");
		}
		BatchClass batchClass = batchClassService.get(batchClassID.getID());
		List<File> allImageFiles = getAllImagesPathInFolder(folderPath);
		for (File imageFile : allImageFiles) {
			convertPdfOrMultiPageTiffToTiff(batchClass, imageFile, thread);
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
			boolean htmlFilesGenerated = false;
			boolean xmlFilesGenerated = false;
			for (String htmlFile : htmlFiles) {
				if (htmlFile.substring(0, htmlFile.lastIndexOf("."))
						.equalsIgnoreCase(fileName.substring(0, fileName.lastIndexOf(".")))) {
					htmlFilesGenerated = true;
					break;
				}
			}
			for (String xmlFile : xmlFiles) {
				if (xmlFile.substring(0, xmlFile.lastIndexOf(".")).equalsIgnoreCase(fileName.substring(0, fileName.lastIndexOf(".")))) {
					xmlFilesGenerated = true;
					break;
				}
			}
			if (!(htmlFilesGenerated && xmlFilesGenerated)) {
				File file = new File(testKvExtractionFolderPath + File.separator + fileName);
				allImageFiles.add(file);
			}
		}
		return allImageFiles;
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

	@Override
	public void generatePNGForImage(final File imagePath) throws DCMAException {
		try {
			thumbnailPNGCreator.generatePNGForImage(imagePath);
		} catch (Exception ex) {
			LOGGER.error(ERROR_IN_PNG_FILE_GENERATION, ex);
			throw new DCMAException(ERROR_IN_PNG_FILE_GENERATION, ex);
		}
	}

}
