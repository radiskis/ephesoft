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

package com.ephesoft.dcma.imagemagick;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.AbstractRunnable;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.imagemagick.constant.ImageMagicKConstants;
import com.ephesoft.dcma.util.FileNameFormatter;
import com.ephesoft.dcma.util.FileUtils;

/**
 * This class has methods using which we can generate thumbnails and PNG for a given tif file.
 * 
 * @author Ephesoft
 * 
 */
public class ThumbnailPNGCreator implements ICommonConstants, IImageMagickCommonConstants {

	private static final String EMPTY_STRING = "";

	private static final char DOT = '.';

	private static final String PROBLEM_GENERATING_THUMBNAILS = "Problem generating thumbnails";

	protected static final Logger LOGGER = LoggerFactory.getLogger(ThumbnailPNGCreator.class);

	private static final String GRAY_COLOR = "gray";

	/**
	 * Instance of PluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * @return the pluginPropertiesService
	 */
	public PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	/**
	 * @param pluginPropertiesService the pluginPropertiesService to set
	 */
	public void setPluginPropertiesService(PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	/**
	 * This constructor checks weather the IM4JAVA_TOOLPATH enviornment variable is set.
	 */
	public ThumbnailPNGCreator() {
		final String envVariable = System.getenv(IMAGEMAGICK_ENV_VARIABLE);
		LOGGER.debug("checking if the Image Magick Enviornment variable IM4JAVA_TOOLPATH is set");
		if (envVariable == null || envVariable.isEmpty()) {
			LOGGER.error("Enviornment Variable IM4JAVA_TOOLPATH not set");
			throw new DCMABusinessException("Enviornment Variable IM4JAVA_TOOLPATH not set.");
		}
		LOGGER.debug("Enviornment variable IM4JAVA_TOOLPATH is set value=" + envVariable);
	}

	public void generateThumbnailsAndPNGForImage(File imagePath, String thumbnailW, String thumbnailH) throws DCMAApplicationException {
		ConvertCmd convertcmd = new ConvertCmd();
		IMOperation operation = new IMOperation();
		operation.addImage();
		operation.colorspace(GRAY_COLOR);
		operation.density(300);
		operation.thumbnail(Integer.parseInt(thumbnailH), Integer.parseInt(thumbnailW));
		operation.addImage();

		String imageName = imagePath.getAbsolutePath();
		// TODO remove hard coding _thumb.png, .png and .tif
		String pngPath = imageName.substring(0, imageName.indexOf(FileType.TIF.getExtensionWithDot()));

		try {
			String[] listOfFiles = {imageName, pngPath + SUFFIX_THUMBNAIL_SAMPLE_PNG};
			convertcmd.run(operation, (Object[]) listOfFiles);
		} catch (Exception ex) {
			LOGGER.error(PROBLEM_GENERATING_THUMBNAILS);
			throw new DCMAApplicationException(PROBLEM_GENERATING_THUMBNAILS, ex);
		}

		IMOperation operationPNG = new IMOperation();
		operationPNG.addImage();
		operation.density(300);
		operation.colorspace(GRAY_COLOR);
		operationPNG.addImage();

		try {
			String[] listOfFiles = {imageName, pngPath + FileType.PNG.getExtensionWithDot()};
			convertcmd.run(operationPNG, (Object[]) listOfFiles);
		} catch (Exception ex) {
			LOGGER.error("Problem generating pngs");
			throw new DCMAApplicationException("Problem generating pngs", ex);
		}
	}

	/**
	 * This method generates the thumbnails for all the tif files in the batch folder.
	 * 
	 * @param sBatchFolder
	 * @param batchInstanceIdentifier
	 * @param pluginWorkflowName
	 * @throws DCMAApplicationException
	 * @throws JAXBException
	 */
	public void generateThumbnail(final String sBatchFolder, final String batchInstanceIdentifier,
			BatchSchemaService batchSchemaService, String thumbnailType, String pluginWorkflowName) throws DCMAApplicationException,
			JAXBException {

		final File fBatchFolder = new File(sBatchFolder);
		String thumbnailH = EMPTY_STRING;
		String thumbnailW = EMPTY_STRING;

		// Initialize properties
		LOGGER.info("Initializing properties...");
		String displayThumbnailH = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN, ImageMagicProperties.CREATE_THUMBNAILS_DISP_THUMB_HEIGHT);
		String displayThumbnailW = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN, ImageMagicProperties.CREATE_THUMBNAILS_DISP_THUMB_WIDTH);
		String compareThumbnailH = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN, ImageMagicProperties.CREATE_THUMBNAILS_COMP_THUMB_HEIGHT);
		String compareThumbnailW = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN, ImageMagicProperties.CREATE_THUMBNAILS_COMP_THUMB_WIDTH);
		LOGGER.info("Properties Initialized Successfully");

		if (thumbnailType.equals(IImageMagickCommonConstants.THUMB_TYPE_COMP)) {
			thumbnailH = compareThumbnailH;
			thumbnailW = compareThumbnailW;
		}

		if (thumbnailType.equals(IImageMagickCommonConstants.THUMB_TYPE_DISP)) {
			thumbnailH = displayThumbnailH;
			thumbnailW = displayThumbnailW;
		}
		String[][] sListOfTiffFiles;
		if (!fBatchFolder.exists() || !fBatchFolder.isDirectory()) {
			throw new DCMABusinessException("Improper Folder Specified folder name->" + sBatchFolder);
		}
		LOGGER.info("Finding xml file for thumbnail generation in the folder--> " + fBatchFolder);

		Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);

		try {
			sListOfTiffFiles = getListOfTiffFiles(fBatchFolder, batch, batchInstanceIdentifier, true, thumbnailType,
					batchSchemaService, ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN);
		} catch (Exception e) {
			throw new DCMAApplicationException("Problem generating list of files", e);

		}
		LOGGER.info("Generating thumbnais");
		BatchInstanceThread batchInstanceThread = new BatchInstanceThread();

		String threadPoolLockFolderPath = batchSchemaService.getLocalFolderLocation() + File.separator + batchInstanceIdentifier
				+ File.separator + batchSchemaService.getThreadpoolLockFolderName();
		try {
			FileUtils.createThreadPoolLockFile(batchInstanceIdentifier, threadPoolLockFolderPath, pluginWorkflowName);
		} catch (IOException ioe) {
			LOGGER.error("Error in creating threadpool lock file" + ioe.getMessage(), ioe);
			throw new DCMABusinessException(ioe.getMessage(), ioe);
		}
		final String thumbNailH = thumbnailH;
		final String thumbNailW = thumbnailW;
		final String thumbnailT =  thumbnailType;
		
		
		for (int i = 0; i < sListOfTiffFiles.length; i++) {
			final String[] files = sListOfTiffFiles[i];
			batchInstanceThread.add(new AbstractRunnable() {

				@Override
				public void run() {
						LOGGER.info("Generating thumbnail for file" + files[0]);
						ConvertCmd convertcmd = new ConvertCmd();
						IMOperation operation = new IMOperation();
						operation.addImage();
						
						// added for color pdf handling.
						// Generate the display thumbnails as gray and compare thumbnails as per the color of input tiff.
						if (thumbnailT.equals(IImageMagickCommonConstants.THUMB_TYPE_DISP)) {
							operation.colorspace(GRAY_COLOR);
						}
						
						operation.thumbnail(Integer.parseInt(thumbNailH), Integer.parseInt(thumbNailW));
						operation.addImage();

					try {
						String[] listOfFiles = {files[0], files[1]};
						convertcmd.run(operation, (Object[]) listOfFiles);
					} catch (Exception ex) {
						LOGGER.error(PROBLEM_GENERATING_THUMBNAILS);
						setDcmaApplicationException(new DCMAApplicationException(PROBLEM_GENERATING_THUMBNAILS, ex));
					}
				}
			});
		}
		try {
			LOGGER.info("Executing thumbnail generation by thread pool for " + batchInstanceIdentifier);
			batchInstanceThread.execute();
			LOGGER.info("Finished thumbnail generation by thread pool for " + batchInstanceIdentifier);
		} catch (DCMAApplicationException dcmae) {
			LOGGER.error("Error in generating thumbnails");
			batchInstanceThread.remove();
			batchSchemaService.updateBatch(batch);

			// Throw the exception to set the batch status to Error by Application aspect
			LOGGER.error("Error in generating thumbnails" + dcmae.getMessage(), dcmae);
			throw new DCMAApplicationException(dcmae.getMessage(), dcmae);
		} finally {
			try {
				FileUtils.deleteThreadPoolLockFile(batchInstanceIdentifier, threadPoolLockFolderPath, pluginWorkflowName);
			} catch (IOException ioe) {
				LOGGER.error("Error in deleting threadpool lock file" + ioe.getMessage(), ioe);
				throw new DCMABusinessException(ioe.getMessage(), ioe);
			}
		}

		for (int i = 0; i < sListOfTiffFiles.length; i++) {
			addFileToPageSchema(batch, sListOfTiffFiles[i][2], sListOfTiffFiles[i][1], thumbnailType);
			LOGGER.debug("thumbnail " + sListOfTiffFiles[i][1] + " sucsesfully generated");
		}
		LOGGER.info("Persisting all thumbnail info to batch.xml file");
		batchSchemaService.updateBatch(batch);
		LOGGER.info(sListOfTiffFiles.length + " thumbnails succesfully generated");

	}

	/**
	 * This method generates the png files which will be used for OCR.
	 * 
	 * @param sBatchFolder
	 * @param batchInstanceIdentifier
	 * @param pluginWorkflowName
	 * @throws DCMAApplicationException
	 * @throws JAXBException
	 */
	public void generateFullFiles(final String sBatchFolder, final String batchInstanceIdentifier,
			BatchSchemaService batchSchemaService, String fileType, String pluginName, String pluginWorkflowName)
			throws DCMAApplicationException, JAXBException {

		File fBatchFolder = new File(sBatchFolder);
		String[][] sListOfTiffFiles;
		if (!fBatchFolder.exists() || !fBatchFolder.isDirectory()) {
			throw new DCMABusinessException("Improper Folder Specified folder name->" + sBatchFolder);
		}
		LOGGER.info("Finding xml file for PNG generation in the folder--> " + fBatchFolder);
		Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);

		try {
			sListOfTiffFiles = getListOfTiffFiles(fBatchFolder, batch, batchInstanceIdentifier, false, EMPTY_STRING, batchSchemaService,
					pluginName);
		} catch (Exception e) {
			throw new DCMAApplicationException("Problem generating list of files", e);
		}
		LOGGER.info("Generating PNG Files");
		BatchInstanceThread batchInstanceThread = new BatchInstanceThread();
		String threadPoolLockFolderPath = batchSchemaService.getLocalFolderLocation() + File.separator + batchInstanceIdentifier
				+ File.separator + batchSchemaService.getThreadpoolLockFolderName();
		try {
			FileUtils.createThreadPoolLockFile(batchInstanceIdentifier, threadPoolLockFolderPath, pluginWorkflowName);
		} catch (IOException ioe) {
			LOGGER.error("Error in creating threadpool lock file" + ioe.getMessage(), ioe);
			throw new DCMABusinessException(ioe.getMessage(), ioe);
		}

		for (int i = 0; i < sListOfTiffFiles.length; i++) {
			final String[] files = sListOfTiffFiles[i];
			batchInstanceThread.add(new AbstractRunnable() {

				@Override
				public void run() {
					LOGGER.info("Generating PNG for file" + files[0]);
					ConvertCmd convertcmd = new ConvertCmd();
					IMOperation operation = new IMOperation();
					operation.addImage();
					operation.density(300);
					operation.colorspace(GRAY_COLOR);
					operation.addImage();
					try {
						String[] listOfFiles = {files[0], files[1]};
						convertcmd.run(operation, (Object[]) listOfFiles);
					} catch (Exception ex) {
						setDcmaApplicationException(new DCMAApplicationException("Problem generating PNG", ex));
					}
				}
			});

		}
		try {
			LOGGER.info("Executing PNG generation by thread pool for " + batchInstanceIdentifier);
			batchInstanceThread.execute();
			LOGGER.info("Finished PNG generation by thread pool for " + batchInstanceIdentifier);
		} catch (DCMAApplicationException dcmae) {
			LOGGER.error("Problem generating thumbnails. Setting batch Status to error state." + dcmae.getMessage(), dcmae);
			batchInstanceThread.remove();
			batchSchemaService.updateBatch(batch);

			// Throw the exception to set the batch status to Error by Application aspect
			throw new DCMAApplicationException(dcmae.getMessage(), dcmae);
		} finally {
			try {
				FileUtils.deleteThreadPoolLockFile(batchInstanceIdentifier, threadPoolLockFolderPath, pluginWorkflowName);
			} catch (IOException ioe) {
				LOGGER.error("Error in deleting threadpool lock file" + ioe.getMessage(), ioe);
				throw new DCMABusinessException(ioe.getMessage(), ioe);
			}
		}
		for (int i = 0; i < sListOfTiffFiles.length; i++) {
			if (fileType.equals(OCR_INPUT_FILE)) {
				addFileToPageSchema(batch, sListOfTiffFiles[i][2], sListOfTiffFiles[i][1], OCR_INPUT_FILE);
			}
			if (fileType.equals(DISPLAY_IMAGE)) {
				addFileToPageSchema(batch, sListOfTiffFiles[i][2], sListOfTiffFiles[i][1], DISPLAY_IMAGE);
			}
			LOGGER.debug("PNG file " + sListOfTiffFiles[i][1] + " sucsesfully generated");
		}
		LOGGER.info("Persisting all PNG info to batch.xml file ");
		batchSchemaService.updateBatch(batch);
		LOGGER.info(sListOfTiffFiles.length + " PNG files succesfully generated");

	}

	/**
	 * This method adds the newly created file to the object of the batch.xml file. It should be noted that after the execution of this
	 * method the new information added is in the memory and has not yet been persisted to the xml file.
	 * 
	 * @param parsedXmlFile
	 * @param index
	 * @param filePath
	 * @param fileType
	 */
	private void addFileToPageSchema(final Batch parsedXmlFile, final String index, final String filePath, final String fileType) {
		String fileName = new File(filePath).getName();
		LOGGER.debug("Updating xml Object for file" + fileName);
		List<Page> listOfPages = parsedXmlFile.getDocuments().getDocument().get(0).getPages().getPage();

		Page page = listOfPages.get(Integer.parseInt(index));
		if (fileType.equalsIgnoreCase(THUMB_TYPE_DISP)) {
			page.setThumbnailFileName(fileName);
		}
		if (fileType.equalsIgnoreCase(THUMB_TYPE_COMP)) {
			page.setComparisonThumbnailFileName(fileName);
		}
		if (fileType.equalsIgnoreCase(OCR_INPUT_FILE)) {
			page.setOCRInputFileName(fileName);
		}
		if (fileType.equalsIgnoreCase(DISPLAY_IMAGE)) {
			page.setDisplayFileName(fileName);
		}

		LOGGER.debug("List Of Pages=" + listOfPages);

	}

	/**
	 * Returns an array containing the list of thumbnail and PNG files. which have to be created.
	 * 
	 * @param fBatchFolder
	 * @param parsedXmlFile
	 * @param batchInstanceIdentifier
	 * @param thumbNailGeneration
	 * @return
	 * @throws DCMAApplicationException
	 */
	private String[][] getListOfTiffFiles(File fBatchFolder, Batch parsedXmlFile, String batchInstanceIdentifier,
			boolean thumbNailGeneration, String thumbnailType, BatchSchemaService batchSchemaService, String pluginName)
			throws DCMAApplicationException {
		LOGGER.info("Getting the list of tif files from the xml for batchInstanceIdentifier=" + batchInstanceIdentifier);
		FileNameFormatter formatter = null;

		// Initialize properties
		String displayThumbnailType = null;
		String compareThumbnailType = null;
		LOGGER.info("Initializing properties...");
		if (pluginName.equals(ImageMagicKConstants.CREATE_OCR_INPUT_PLUGIN)) {
			displayThumbnailType = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
					ImageMagicKConstants.CREATE_OCR_INPUT_PLUGIN, ImageMagicProperties.CREATE_OCR_INPUT_DISP_THUMB_TYPE);
			compareThumbnailType = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
					ImageMagicKConstants.CREATE_OCR_INPUT_PLUGIN, ImageMagicProperties.CREATE_OCR_INPUT_COMP_THUMB_TYPE);
		} else if (pluginName.equals(ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN)) {
			displayThumbnailType = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
					ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN, ImageMagicProperties.CREATE_THUMBNAILS_DISP_THUMB_TYPE);
			compareThumbnailType = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
					ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN, ImageMagicProperties.CREATE_THUMBNAILS_COMP_THUMB_TYPE);
		} else {
			displayThumbnailType = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
					ImageMagicKConstants.CREATE_DISPLAY_IMAGE_PLUGIN, ImageMagicProperties.CREATE_DISPLAY_IMAGE_DISP_THUMB_TYPE);
			compareThumbnailType = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
					ImageMagicKConstants.CREATE_DISPLAY_IMAGE_PLUGIN, ImageMagicProperties.CREATE_DISPLAY_IMAGE_COMP_THUMB_TYPE);
		}

		LOGGER.info("Properties Initialized Successfully");

		try {
			formatter = new FileNameFormatter();
		} catch (Exception e) {
			throw new DCMAApplicationException("Problem Initiating formatter", e);
		}
		String[][] pageTiffFileArr;

		List<Document> listOfDocuments = parsedXmlFile.getDocuments().getDocument();
		Document document = listOfDocuments.get(0);
		List<Page> pages = document.getPages().getPage();
		pageTiffFileArr = new String[pages.size()][3];

		StringBuffer sbThumbNailFileName = new StringBuffer();
		StringBuffer sbPNGFileName = new StringBuffer();
		for (int i = 0; i < pages.size(); i++) {
			// Put the New File name int the array

			File pageTiffFile = batchSchemaService.getFile(batchInstanceIdentifier, pages.get(i).getNewFileName());
			String pageTiffFilePath = pageTiffFile.getAbsolutePath();
			pageTiffFileArr[i][0] = pageTiffFilePath;

			if (thumbNailGeneration) {
				LOGGER.info("Generating the list thumbnail files for batchInstanceIdentifier = " + batchInstanceIdentifier);
				sbThumbNailFileName.append(fBatchFolder.getAbsolutePath());
				sbThumbNailFileName.append(File.separator);
				String thumbNailFileName = EMPTY_STRING;
				try {
					if (thumbnailType.equals(IImageMagickCommonConstants.THUMB_TYPE_DISP)) {
						thumbNailFileName = formatter.getDisplayThumbnailFileName(batchInstanceIdentifier, pages.get(i)
								.getOldFileName(), pages.get(i).getNewFileName(), pages.get(i).getOCRInputFileName(),
								displayThumbnailType, pages.get(i).getIdentifier());
					}
					if (thumbnailType.equals(IImageMagickCommonConstants.THUMB_TYPE_COMP)) {
						thumbNailFileName = formatter.getCompareThumbnailFileName(batchInstanceIdentifier, pages.get(i)
								.getOldFileName(), pages.get(i).getNewFileName(), pages.get(i).getOCRInputFileName(),
								compareThumbnailType, pages.get(i).getIdentifier());
					}
				} catch (Exception ex) {
					LOGGER.error("Problem Generating Thumbnail File Name");
					throw new DCMABusinessException("Problem Generating Thumbnail File Name", ex);
				}

				sbThumbNailFileName.append(thumbNailFileName);
				String thumbnailFilePath = sbThumbNailFileName.toString();
				pageTiffFileArr[i][1] = thumbnailFilePath;
				sbThumbNailFileName.delete(0, sbThumbNailFileName.length());
			} else {
				LOGGER.info("Generating the list PNG files for batchInstanceIdentifier = " + batchInstanceIdentifier);
				sbPNGFileName.append(fBatchFolder.getAbsolutePath());
				sbPNGFileName.append(File.separator);
				String pngFileName = null;
				try {
					pngFileName = formatter.getOCRInputFileName(batchInstanceIdentifier, pages.get(i).getOldFileName(), pages.get(i)
							.getNewFileName(), FileType.PNG.getExtensionWithDot(), pages.get(i).getIdentifier());
				} catch (Exception e) {
					throw new DCMAApplicationException("Problem Getting Name from formatter", e);
				}
				sbPNGFileName.append(pngFileName);
				String pngFilePath = sbPNGFileName.toString();
				pageTiffFileArr[i][1] = pngFilePath;
				sbPNGFileName.delete(0, sbPNGFileName.length());
			}
			pageTiffFileArr[i][2] = Integer.toString(i);

		}

		return pageTiffFileArr;

	}

	public void generatePNGForImage(File imagePath) throws DCMAApplicationException {

		ConvertCmd convertcmd = new ConvertCmd();
		IMOperation operationPNG = new IMOperation();
		operationPNG.addImage();
		operationPNG.addImage();

		String imageName = imagePath.getAbsolutePath();
		// TODO remove hard coding _thumb.png, .png and .tif
		String pngPath = imageName.substring(0, imageName.lastIndexOf(DOT));
		try {
			String[] listOfFiles = {imageName, pngPath + FileType.PNG.getExtensionWithDot()};
			convertcmd.run(operationPNG, (Object[]) listOfFiles);
		} catch (Exception ex) {
			LOGGER.error("Problem generating png.");
			throw new DCMAApplicationException("Problem generating png.", ex);
		}
	}
}
