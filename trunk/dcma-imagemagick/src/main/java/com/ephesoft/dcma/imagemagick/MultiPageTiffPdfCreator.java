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

package com.ephesoft.dcma.imagemagick;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Batch.Documents;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.EphesoftProperty;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.imagemagick.constant.ImageMagicKConstants;
import com.ephesoft.dcma.imagemagick.impl.GhostScriptPDFCreator;
import com.ephesoft.dcma.imagemagick.impl.HOCRtoPDFCreator;
import com.ephesoft.dcma.imagemagick.impl.ITextPDFCreator;
import com.ephesoft.dcma.imagemagick.impl.ImageMagicKPDFCreator;
import com.ephesoft.dcma.kvfinder.service.KVFinderService;
import com.ephesoft.dcma.util.FileNameFormatter;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.IUtilCommonConstants;
import com.ephesoft.dcma.util.OSUtil;
import com.ephesoft.dcma.util.PDFUtil;
import com.ephesoft.dcma.util.TIFFUtil;

/**
 * This class is contains methods which can detect the batch.xml file in a folder and export it to the specified folder.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.imagemagick.service.ImageProcessServiceImpl 
 */
@Component
public class MultiPageTiffPdfCreator implements ICommonConstants {

	/**
	 * Variable for full stop.
	 */
	private static final char FULL_STOP = '.';

	/**
	 * Reference for LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MultiPageTiffPdfCreator.class);

	/**
	 * Reference for {@link BatchSchemaService}.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;
	
	/**
	 * Instance of {@link KVFinderService}
	 */
	@Autowired
	private KVFinderService kvFinderService;

	/**
	 * List of commands for multipage tiff generation.
	 */
	private transient String tiffCmds;

	/**
	 * List of commands for unix for multipage tiff generation.
	 */
	private transient String unixTiffCmds;

	/**
	 * PDF compression to be set while creating multipage-pdf.
	 */
	private transient String tifCompression;

	/**
	 * generate the display png again or not.Default value: OFF.
	 */
	private transient String generateDisplayPng;

	/**
	 * Input image parameters for convert command.
	 */
	private transient String inputParameters;

	/**
	 * Output image parameters for convert command.
	 */
	private transient String outputParameters;

	/**
	 * Reference for {@link GhostScriptPDFCreator}.
	 */
	@Autowired
	private GhostScriptPDFCreator ghostScriptPDFCreator;

	/**
	 * Reference for {@link ImageMagicKPDFCreator}.
	 */
	@Autowired
	private ImageMagicKPDFCreator imageMagicKPDFCreator;

	/**
	 * Reference for {@link ITextPDFCreator}.
	 */
	@Autowired
	private ITextPDFCreator iTextPDFCreator;

	/**
	 * Reference for {@link HOCRtoPDFCreator}.
	 */
	@Autowired
	private HOCRtoPDFCreator hocrToPDFCreator;

	/**
	 * Validating the number of page in document with output pdf.
	 */
	private transient String validateDocumentPage;

	/**
	 * Maximum number of files to be processed by to be processed by one Image magick command for multi page tiff creation.
	 */
	private transient int maxFilesProcessedPerIMCmd;

	/**
	 * Instance of {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * @param batchSchemaService the {@link BatchSchemaService} to set
	 */
	public void setBatchSchemaService(BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * @param ghostScriptPDFCreator the {@link GhostScriptPDFCreator} to set
	 */
	public void setGhostScriptPDFCreator(GhostScriptPDFCreator ghostScriptPDFCreator) {
		this.ghostScriptPDFCreator = ghostScriptPDFCreator;
	}

	/**
	 * @param imageMagicKPDFCreator the {@link ImageMagicKPDFCreator} to set
	 */
	public void setImageMagicKPDFCreator(ImageMagicKPDFCreator imageMagicKPDFCreator) {
		this.imageMagicKPDFCreator = imageMagicKPDFCreator;
	}

	/**
	 * @param iTextPDFCreator the {@link ITextPDFCreator} to set
	 */
	public void setiTextPDFCreator(ITextPDFCreator iTextPDFCreator) {
		this.iTextPDFCreator = iTextPDFCreator;
	}

	/**
	 * @param hocrToPDFCreator the {@link HOCRtoPDFCreator} to set
	 */
	public void setHocrToPDFCreator(HOCRtoPDFCreator hocrToPDFCreator) {
		this.hocrToPDFCreator = hocrToPDFCreator;
	}

	/**
	 * @return {@link String}
	 */
	public String getValidateDocumentPage() {
		return validateDocumentPage;
	}

	/**
	 * @param validateDocumentPage {@link String}
	 */
	public void setValidateDocumentPage(String validateDocumentPage) {
		this.validateDocumentPage = validateDocumentPage;
	}

	/**
	 * @return {@link String}
	 */
	public String getUnixTiffCmds() {
		return unixTiffCmds;
	}

	/**
	 * @param unixTiffCmds {@link String}
	 */
	public void setUnixTiffCmds(String unixTiffCmds) {
		this.unixTiffCmds = unixTiffCmds;
	}

	/**
	 * @return {@link String}
	 */
	public String getTiffCmds() {
		String command = null;
		if (OSUtil.isWindows()) {
			command = tiffCmds;
		} else {
			command = unixTiffCmds;
		}
		return command;
	}

	/**
	 * @param tiffCmds {@link String}
	 */
	public void setTiffCmds(String tiffCmds) {
		this.tiffCmds = tiffCmds;
	}

	/**
	 * @return {@link String}
	 */
	public String getTifCompression() {
		return tifCompression;
	}

	/**
	 * @param tifCompression {@link String}
	 */
	public void setTifCompression(String tifCompression) {
		this.tifCompression = tifCompression;
	}

	/**
	 * @param generateDisplayPng {@link String}
	 */
	public void setGenerateDisplayPng(String generateDisplayPng) {
		this.generateDisplayPng = generateDisplayPng;
	}

	/**
	 * @return {@link String}
	 */
	public String getGenerateDisplayPng() {
		return generateDisplayPng;
	}

	/**
	 * @param inputParameters {@link String}
	 */
	public void setInputParameters(String inputParameters) {
		this.inputParameters = inputParameters;
	}

	/**
	 * @return {@link String}
	 */
	public String getInputParameters() {
		return inputParameters;
	}

	/**
	 * @param outputParameters {@link String}
	 */
	public void setOutputParameters(String outputParameters) {
		this.outputParameters = outputParameters;
	}

	/**
	 * @return {@link String}
	 */
	public String getOutputParameters() {
		return outputParameters;
	}

	/**
	 * Getter for max files processed by one IM command for multipage tiff creation.
	 * 
	 * @return int
	 */
	public int getMaxFilesProcessedPerIMCmd() {
		return maxFilesProcessedPerIMCmd;
	}

	/**
	 * Setter for max files processed by one IM command for multipage tiff creation.
	 * 
	 * @param maxFilesProcessedPerIMCmd int
	 */
	public void setMaxFilesProcessedPerIMCmd(int maxFilesProcessedPerIMCmd) {
		this.maxFilesProcessedPerIMCmd = maxFilesProcessedPerIMCmd;
	}

	/**
	 * This method takes the FolderToBeExported path and creates multi page tiff and PDF files based on the batch.xml file found in the
	 * folder.
	 * @param ghostscriptPdfParameters {@link String}
	 * @param batchInstanceIdentifier {@link String}
	 * @param pdfOptimizationParams {@link String}
	 * @param multipageTifSwitch {@link String}
	 * @param checkPDFExportProcess {@link String}
	 * @param pluginName {@link String}
	 * @param pdfOptimizationSwitch {@link String}
	 * @throws DCMAApplicationException for any exception during processing of documents
	 */
	public void createMultiPageFiles(String ghostscriptPdfParameters, String batchInstanceIdentifier, String pdfOptimizationParams,
			String multipageTifSwitch, String checkPDFExportProcess, String pluginName, String pdfOptimizationSwitch)
			throws DCMAApplicationException {
		LOGGER.info("Inside method createMultiPageFiles...");

		String sFolderToBeExported = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceIdentifier) + File.separator
				+ batchInstanceIdentifier;
		Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);
		File fFolderToBeExported = new File(sFolderToBeExported);

		if (!fFolderToBeExported.isDirectory()) {
			throw new DCMABusinessException(fFolderToBeExported.toString() + " is not a Directory.");
		}

		checkForUnknownDocument(batch);

		Map<String, List<File>> documentPageMap;
		try {
			documentPageMap = createDocumentPageMap(batch, batchInstanceIdentifier);
		} catch (Exception e) {
			LOGGER.error("Error in creating document page map. " + e.getMessage(), e);
			// updateXMLFileFailiure(batchSchemaService, batch);
			throw new DCMAApplicationException(e.getMessage(), e);
		}
		try {
			createMultiPageTiffAndPDF(ghostscriptPdfParameters, documentPageMap, fFolderToBeExported, batch, batchInstanceIdentifier,
					multipageTifSwitch, checkPDFExportProcess, pdfOptimizationParams, pdfOptimizationSwitch);
		} catch (Exception e) {
			LOGGER.error("Error in creating multi page tiff and pdf. " + e.getMessage(), e);
			throw new DCMAApplicationException(e.getMessage(), e);
		}
		batchSchemaService.updateBatch(batch);
	}

	/**
	 * This method takes the parameters to create multi page tiff and PDF files. This is used for web service API.
	 * @param ghostscriptPdfParameters {@link String}
	 * @param pdfOptimizationParams {@link String}
	 * @param multipageTifSwitch {@link String}
	 * @param toolName {@link String}
	 * @param pdfOptimizationSwitch {@link String}
	 * @param workingDir {@link String}
	 * @param outputDir {@link String}
	 * @param singlePageFiles {@link List<File>}
	 * @param batchInstanceIdentifier {@link String}
	 * @throws DCMAApplicationException
	 */
	public void createMultiPageFilesAPI(String ghostscriptPdfParameters, String pdfOptimizationParams, String multipageTifSwitch,
			String toolName, String pdfOptimizationSwitch, String workingDir, String outputDir, List<File> singlePageFiles,
			String batchInstanceIdentifier) throws DCMAApplicationException {
		LOGGER.info("Inside method createMultiPageFiles...");
		File fFolderToBeExported = new File(outputDir);

		if (!fFolderToBeExported.isDirectory()) {
			throw new DCMABusinessException(fFolderToBeExported.toString() + " is not a Directory.");
		}

		try {
			createMultiPageTiffAndPDFAPI(ghostscriptPdfParameters, singlePageFiles, fFolderToBeExported, multipageTifSwitch, toolName,
					pdfOptimizationParams, pdfOptimizationSwitch, batchInstanceIdentifier, workingDir);
		} catch (Exception e) {
			LOGGER.error("Error in creating multi page tiff and pdf. " + e.getMessage(), e);
			throw new DCMAApplicationException(e.getMessage(), e);
		}
	}

	private void createMultiPageTiffAndPDFAPI(String ghostscriptPdfParameters, List<File> singlePageFiles, File exportToFolder,
			String multipageTifSwitch, String pdfCreationTool, String pdfOptimizationParams, String pdfOptimizationSwitch,
			String batchInstanceIdentifier, String workingDir) throws DCMAApplicationException {
		String ghostScriptEnvVariable = System.getenv(IImageMagickCommonConstants.GHOSTSCRIPT_ENV_VARIABLE);
		validatingGhostScritEnv(ghostScriptEnvVariable);
		String documentIdInt = "0";
		String sTargetFileNameTif;
		File fTargetFileNameTif;
		String sTargetFileNamePdf = null;
		File fTargetFileNamePdf = null;
		String[] pages;
		String[] pdfPages;
		BatchInstanceThread tifToPdfThread = new BatchInstanceThread(batchInstanceIdentifier);
		BatchInstanceThread tifToTifThread = new BatchInstanceThread(batchInstanceIdentifier);
		BatchInstanceThread pdfBatchInstanceThread = new BatchInstanceThread(batchInstanceIdentifier);
		BatchInstanceThread tifBatchInstanceThread = new BatchInstanceThread(batchInstanceIdentifier);
		BatchInstanceThread pdfOptimizationThread = new BatchInstanceThread(batchInstanceIdentifier);
		String tempFileName = null;
		List<MultiPageExecutor> multiPageExecutorsTiff = new ArrayList<MultiPageExecutor>();
		List<MultiPageExecutor> multiPageExecutorsPdf = new ArrayList<MultiPageExecutor>();
		List<PdfOptimizer> pdfOptimizer = new ArrayList<PdfOptimizer>();
		LOGGER.info("pdf Optimization Switch value" + pdfOptimizationSwitch);
		String multiPageTiffFileName;
		String multiPagePdfFileName;
		try {
			multiPageTiffFileName = IUtilCommonConstants.DOCUMENT_ID + documentIdInt + FileType.TIF.getExtensionWithDot();
			multiPagePdfFileName = IUtilCommonConstants.DOCUMENT_ID + documentIdInt + FileType.PDF.getExtensionWithDot();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new DCMAApplicationException("Problem getting file name ", e);
		}
		sTargetFileNameTif = exportToFolder.getAbsolutePath() + File.separator + File.separator + multiPageTiffFileName;
		fTargetFileNameTif = new File(sTargetFileNameTif);
		sTargetFileNamePdf = exportToFolder.getAbsolutePath() + File.separator + File.separator + multiPagePdfFileName;
		fTargetFileNamePdf = new File(sTargetFileNamePdf);
		pages = new String[singlePageFiles.size() + 1];
		int index = 0;
		for (File page : singlePageFiles) {
			pages[index] = page.getAbsolutePath();
			index++;
		}
		addThreadToTifThread(tifToTifThread, pages);
		addThreadToPdfThread(tifToPdfThread, pages);
		pdfPages = new String[pages.length];
		pages[singlePageFiles.size()] = fTargetFileNameTif.getAbsolutePath();
		index = 0;
		index = addingPageIndexes(pages, index, pdfPages);
		if (multipageTifSwitch != null && multipageTifSwitch.equalsIgnoreCase(ImageMagicKConstants.ON_SWITCH)) {
			LOGGER.info("Adding command for multi page tiff execution");
			multiPageExecutorsTiff.add(new MultiPageExecutor(tifBatchInstanceThread, pages, tifCompression, maxFilesProcessedPerIMCmd,
					documentIdInt));
		}
		if (pdfCreationTool != null && !pdfCreationTool.equalsIgnoreCase(ImageMagicKConstants.IMAGE_MAGICK)) {
			tempFileName = getPagesForImageMagick(singlePageFiles, pdfOptimizationSwitch, workingDir, fTargetFileNamePdf, pages,
					pdfPages, tempFileName);
		} else {
			pages[singlePageFiles.size()] = fTargetFileNamePdf.getAbsolutePath();
			pdfPages[singlePageFiles.size()] = fTargetFileNamePdf.getAbsolutePath();
		}
		LOGGER.info("toolName : " + pdfCreationTool);
		LOGGER.info("Adding command for multi page pdf execution");
		// use this method to create pdf using itext API
		if (pdfCreationTool != null && pdfCreationTool.equalsIgnoreCase(ImageMagicKConstants.ITEXT)) {
			LOGGER.info("creating pdf using IText");
			iTextPDFCreator.createPDFUsingIText(pages, pdfBatchInstanceThread, multiPageExecutorsPdf, documentIdInt);
		}
		// use this method to create pdf using ghost script command
		else if (pdfCreationTool != null && pdfCreationTool.equalsIgnoreCase(ImageMagicKConstants.GHOST_SCRIPT)) {
			LOGGER.info("creating pdf using ghostscript command");
			ghostScriptPDFCreator.createPDFUsingGhostScript(ghostscriptPdfParameters, batchInstanceIdentifier, exportToFolder
					.getAbsolutePath(), pdfPages, pdfBatchInstanceThread, multiPageExecutorsPdf, documentIdInt);
		}
		// use this default method to create pdf using image-magick convert command
		else if (pdfCreationTool != null && pdfCreationTool.equalsIgnoreCase(ImageMagicKConstants.IMAGE_MAGICK)) {
			LOGGER.info("creating pdf using image-magick convert command");
			imageMagicKPDFCreator.createPDFUsingImageMagick(pages, pdfBatchInstanceThread, multiPageExecutorsPdf,
					pdfOptimizationSwitch);
		} else {
			LOGGER.info("creating pdf using Itext");
			iTextPDFCreator.createPDFUsingIText(pages, pdfBatchInstanceThread, multiPageExecutorsPdf, documentIdInt);
		}
		// Fix for issue : ghostscript unable to optimise pdf produced by IM
		if (pdfCreationTool != null && !pdfCreationTool.equalsIgnoreCase(ImageMagicKConstants.IMAGE_MAGICK)
				&& pdfOptimizationSwitch != null && pdfOptimizationSwitch.equalsIgnoreCase(ImageMagicKConstants.ON_SWITCH)) {
			ghostScriptPDFCreator.createOptimizedPdfAPI(exportToFolder.getAbsolutePath(), pdfOptimizationParams, fTargetFileNamePdf,
					pdfOptimizationThread, tempFileName, pdfOptimizer);
		}
		executeCommandAndOptimizePdf(pdfCreationTool, pdfOptimizationSwitch, tifToPdfThread, tifToTifThread, pdfBatchInstanceThread,
				tifBatchInstanceThread, pdfOptimizationThread);
		Boolean checkDocumentPage = Boolean.parseBoolean(validateDocumentPage);
		// Copy the pdf file from working Dir to output Dir in case of GHOSTSCRIPT
		copyPdfFile(pdfCreationTool, pdfOptimizationSwitch, workingDir, sTargetFileNamePdf, fTargetFileNamePdf, tempFileName);
		validatePageCount(singlePageFiles, sTargetFileNamePdf, checkDocumentPage);
	}

	private String getPagesForImageMagick(List<File> singlePageFiles, String pdfOptimizationSwitch, String workingDir,
			File fTargetFileNamePdf, String[] pages, String[] pdfPages, String tempFileName) {
		String fileNameTemp = tempFileName;
		if (pdfOptimizationSwitch != null && pdfOptimizationSwitch.equalsIgnoreCase(ImageMagicKConstants.ON_SWITCH)) {
			fileNameTemp = ImageMagicKConstants.TEMP_FILE_NAME + "_" + fTargetFileNamePdf.getName();
			pdfPages[singlePageFiles.size()] = workingDir + File.separator + fileNameTemp;
			pages[singlePageFiles.size()] = pdfPages[singlePageFiles.size()];
		} else {
			pages[singlePageFiles.size()] = fTargetFileNamePdf.getAbsolutePath();
			pdfPages[singlePageFiles.size()] = fTargetFileNamePdf.getAbsolutePath();
		}
		return fileNameTemp;
	}

	private void validatingGhostScritEnv(String ghostScriptEnvVariable) {
		if (ghostScriptEnvVariable == null || ghostScriptEnvVariable.isEmpty()) {
			LOGGER.info("ghostScriptEnvVariable is null or empty.");
			throw new DCMABusinessException("Enviornment Variable GHOSTSCRIPT_HOME not set.");
		}
	}

	private void copyPdfFile(String pdfCreationTool, String pdfOptimizationSwitch, String workingDir, String sTargetFileNamePdf,
			File fTargetFileNamePdf, String tempFileName) throws DCMAApplicationException {
		if (!fTargetFileNamePdf.exists()) {
			String inPdfFilePath = "";
			File outDirFile = new File(sTargetFileNamePdf);
			if (pdfCreationTool != null && !pdfCreationTool.equalsIgnoreCase(ImageMagicKConstants.IMAGE_MAGICK)) {
				if (pdfOptimizationSwitch != null && pdfOptimizationSwitch.equalsIgnoreCase(ImageMagicKConstants.ON_SWITCH)) {
					new File(workingDir + File.separator + tempFileName).renameTo(outDirFile);
				} else {
					new File(workingDir + File.separator + fTargetFileNamePdf.getName()).renameTo(outDirFile);
				}
			} else {
				inPdfFilePath = workingDir + File.separator + fTargetFileNamePdf.getName();
				File inDirFile = new File(inPdfFilePath);
				try {
					FileUtils.copyFile(inDirFile, outDirFile);
				} catch (Exception e) {
					throw new DCMAApplicationException("Unable to copy file while processing." + e);
				}
			}
		}
	}

	private void validatePageCount(List<File> singlePageFiles, String sTargetFileNamePdf, Boolean checkDocumentPage)
			throws DCMAApplicationException {
		if (sTargetFileNamePdf != null && checkDocumentPage) {
			int numberOfPDFPage = PDFUtil.getPDFPageCount(sTargetFileNamePdf);
			int numberOfTiffs = 0;
			for (File page : singlePageFiles) {
				numberOfTiffs = numberOfTiffs + TIFFUtil.getTIFFPageCount(page.getAbsolutePath());
			}
			if (numberOfPDFPage != numberOfTiffs) {
				throw new DCMAApplicationException("Number of pages mismatched in multipage PDF and list of input files.");
			}
		}
	}

	private void optimizePdfUsingGhostscript(String toolName, String pdfOptimizationSwitch, BatchInstanceThread pdfOptimizationThread)
			throws DCMAApplicationException {
		try {
			LOGGER.info("Executing commands for optimizing multi page pdf using thread pool.");
			if (toolName != null && !toolName.equalsIgnoreCase(ImageMagicKConstants.IMAGE_MAGICK) && pdfOptimizationSwitch != null
					&& pdfOptimizationSwitch.equalsIgnoreCase(ImageMagicKConstants.ON_SWITCH)) {
				pdfOptimizationThread.execute();
			}
		} catch (DCMAApplicationException dcmae) {
			LOGGER.error("Error in executing command for optimizing multi page pdf using thread pool" + dcmae.getMessage(), dcmae);
			if (toolName != null && !toolName.equalsIgnoreCase(ImageMagicKConstants.IMAGE_MAGICK) && pdfOptimizationSwitch != null
					&& pdfOptimizationSwitch.equalsIgnoreCase(ImageMagicKConstants.ON_SWITCH)) {
				pdfOptimizationThread.remove();
			}
			// Throw the exception to set the batch status to Error by Application aspect
			throw new DCMAApplicationException(dcmae.getMessage(), dcmae);
		}
	}

	private void checkForUnknownDocument(Batch pasrsedXMLFile) {
		Documents documents = pasrsedXMLFile.getDocuments();
		boolean valid = true;
		if (documents != null) {
			List<Document> listOfDocuments = documents.getDocument();
			if (listOfDocuments == null) {
				valid = false;
			} else {
				for (Document document : listOfDocuments) {
					if (document == null) {
						valid = false;
					}
					if (valid && document.getType().equalsIgnoreCase(EphesoftProperty.UNKNOWN.getProperty())) {
						Pages pages = document.getPages();
						if (pages == null) {
							valid = false;
						} else {
							List<Page> listOfPages = pages.getPage();
							if (listOfPages == null) {
								valid = false;
							}
							if (valid && listOfPages.isEmpty()) {
								valid = false;
							}
							throw new DCMABusinessException("Final xml document contains unknown documents. Cannot be exported.");
						}
					}
				}
			}
		}
	}

	private void createMultiPageTiffAndPDF(final String ghostscriptPdfParameters, final Map<String, List<File>> documentPageMap,
			final File exportToFolder, final Batch pasrsedXMLFile, final String batchInstanceIdentifier,
			final String multipageTifSwitch, final String checkPDFExportProcess, final String pdfOptimizationParams,
			final String pdfOptimizationSwitch) throws DCMAApplicationException {
		String ghostScriptEnvVariable = System.getenv(IImageMagickCommonConstants.GHOSTSCRIPT_ENV_VARIABLE);
		validatingGhostScritEnv(ghostScriptEnvVariable);
		Set<String> documentNames;
		documentNames = documentPageMap.keySet();
		Iterator<String> iterator = documentNames.iterator();
		BatchInstanceThread tifToPdfThread = new BatchInstanceThread(batchInstanceIdentifier);
		BatchInstanceThread tifToTifThread = new BatchInstanceThread(batchInstanceIdentifier);
		BatchInstanceThread pdfBatchInstanceThread = new BatchInstanceThread(batchInstanceIdentifier);
		BatchInstanceThread tifBatchInstanceThread = new BatchInstanceThread(batchInstanceIdentifier);
		BatchInstanceThread pdfOptimizationThread = new BatchInstanceThread(batchInstanceIdentifier);
		List<MultiPageExecutor> multiPageExecutorsTiff = new ArrayList<MultiPageExecutor>();
		List<MultiPageExecutor> multiPageExecutorsPdf = new ArrayList<MultiPageExecutor>();
		List<PdfOptimizer> pdfOptimizer = new ArrayList<PdfOptimizer>();
		LOGGER.info("pdf Optimization Switch value" + pdfOptimizationSwitch);
		int widthOfLineInt = getWidthOfLineInt();		
		while (iterator.hasNext()) {
			String documentIdInt = iterator.next();
			LOGGER.info("Started creating multipage Tif and PDF for the " + "document with document id=" + documentIdInt);
			List<File> listofPages = documentPageMap.get(documentIdInt);
			if (listofPages.size() == 0) {
				continue;
			}
			String multiPageTiffFileName;
			String multiPagePdfFileName;
			try {
				multiPageTiffFileName = new FileNameFormatter().getMuliPageFileName(documentIdInt, batchInstanceIdentifier,
						FileType.TIF.getExtensionWithDot());
				multiPagePdfFileName = new FileNameFormatter().getMuliPageFileName(documentIdInt, batchInstanceIdentifier,
						FileType.PDF.getExtensionWithDot());
			} catch (Exception e) {
				throw new DCMAApplicationException("Problem getting file name", e);
			}
			String multiPageTiffFilePath = exportToFolder.getAbsolutePath() + File.separator + File.separator + multiPageTiffFileName;
			File fTargetFileNameTif = new File(multiPageTiffFilePath);
			String multiPagePdfFilePath = exportToFolder.getAbsolutePath() + File.separator + File.separator + multiPagePdfFileName;
			File fTargetFileNamePdf = new File(multiPagePdfFilePath);
			String[] pages = new String[listofPages.size() + 1];
			int index = 0;
			for (File page : listofPages) {
				pages[index] = page.getAbsolutePath();
				index++;
			}
			addThreadToTifThread(tifToTifThread, pages);
			addThreadToPdfThread(tifToPdfThread, pages);
			String[] pdfPages = new String[pages.length];
			pages[listofPages.size()] = fTargetFileNameTif.getAbsolutePath();
			index = 0;
			index = addingPageIndexes(pages, index, pdfPages);
			if (multipageTifSwitch != null && multipageTifSwitch.equalsIgnoreCase(ImageMagicKConstants.ON_SWITCH)) {
				LOGGER.info("Adding command for multi page tiff execution");
				multiPageExecutorsTiff.add(new MultiPageExecutor(tifBatchInstanceThread, pages, tifCompression,
						maxFilesProcessedPerIMCmd, documentIdInt));
			}
			if (checkPDFExportProcess != null && !checkPDFExportProcess.equalsIgnoreCase(ImageMagicKConstants.IMAGE_MAGICK)) {
				createOptimizedPDF(batchInstanceIdentifier, pdfOptimizationParams, pdfOptimizationSwitch, pdfOptimizationThread,
						pdfOptimizer, listofPages, fTargetFileNamePdf, pages, pdfPages);
			} else {
				pages[listofPages.size()] = fTargetFileNamePdf.getAbsolutePath();
				pdfPages[listofPages.size()] = fTargetFileNamePdf.getAbsolutePath();
			}
			LOGGER.info("CheckPDFExportProcess : " + checkPDFExportProcess);
			LOGGER.info("Adding command for multi page pdf execution");
			// use this method to create pdf using itext API
			if (checkPDFExportProcess != null && checkPDFExportProcess.equalsIgnoreCase(ImageMagicKConstants.ITEXT)) {
				LOGGER.info("creating pdf using IText");
				iTextPDFCreator.createPDFUsingIText(pages, pdfBatchInstanceThread, multiPageExecutorsPdf, documentIdInt);
			}
			// use this method to create pdf using ghost script command
			else if (checkPDFExportProcess != null && checkPDFExportProcess.equalsIgnoreCase(ImageMagicKConstants.GHOST_SCRIPT)) {
				LOGGER.info("creating pdf using ghostscript command");
				ghostScriptPDFCreator.createPDFUsingGhostScript(ghostscriptPdfParameters, batchInstanceIdentifier, exportToFolder
						.getAbsolutePath(), pdfPages, pdfBatchInstanceThread, multiPageExecutorsPdf, documentIdInt);
			}
			// use this method to create pdf using hocrtopdf.jar
			else if (checkPDFExportProcess != null && checkPDFExportProcess.equalsIgnoreCase(ImageMagicKConstants.HOCR_TO_PDF)) {
				LOGGER.info("creating pdf using hOCRToPDF");
				hocrToPDFCreator.createPDFFromHOCR(pasrsedXMLFile, batchInstanceIdentifier, exportToFolder.getAbsolutePath(), pages,
						pdfBatchInstanceThread, multiPageExecutorsPdf, documentIdInt);
			}
			// use this default method to create pdf using image-magick convert command
			else if (checkPDFExportProcess != null && checkPDFExportProcess.equalsIgnoreCase(ImageMagicKConstants.IMAGE_MAGICK)) {
				LOGGER.info("creating pdf using image-magick convert command");
				imageMagicKPDFCreator.createPDFUsingImageMagick(pages, pdfBatchInstanceThread, multiPageExecutorsPdf,
						pdfOptimizationSwitch);
			} else if (checkPDFExportProcess != null && checkPDFExportProcess.equalsIgnoreCase(ImageMagicKConstants.ITEXT_SEARCHABLE)) {
				LOGGER.info("creating pdf using ITEXT_SEARCHABLE");
				iTextPDFCreator.createPDFUsingIText(pages, pdfBatchInstanceThread, multiPageExecutorsPdf, documentIdInt,
						pasrsedXMLFile, batchInstanceIdentifier, widthOfLineInt);
			} else {
				LOGGER.info("creating pdf using Itext");
				iTextPDFCreator.createPDFUsingIText(pages, pdfBatchInstanceThread, multiPageExecutorsPdf, documentIdInt);
			}
		}
		executeCommandAndOptimizePdf(checkPDFExportProcess, pdfOptimizationSwitch, tifToPdfThread, tifToTifThread,
				pdfBatchInstanceThread, tifBatchInstanceThread, pdfOptimizationThread);
		updateMultiPageTifToXml(documentPageMap, pasrsedXMLFile, multipageTifSwitch, documentNames, multiPageExecutorsTiff,
				multiPageExecutorsPdf);
		if (Boolean.parseBoolean(validateDocumentPage)) {
			validatingDocumentVsPdfPage(pasrsedXMLFile, batchInstanceIdentifier);
		}
	}

	private int addingPageIndexes(String[] pages, int index, String[] pdfPages) {
		int indexTemp = index;
		for (String page : pages) {
			if (page != null && !page.isEmpty()) {
				pdfPages[indexTemp++] = page.replace(page.substring(page.lastIndexOf(ImageMagicKConstants.DOT)), FileType.PDF.getExtensionWithDot());
			}
		}
		return indexTemp;
	}

	private void createOptimizedPDF(final String batchInstanceIdentifier, final String pdfOptimizationParams,
			final String pdfOptimizationSwitch, BatchInstanceThread pdfOptimizationThread, List<PdfOptimizer> pdfOptimizer,
			List<File> listofPages, File fTargetFileNamePdf, String[] pages, String[] pdfPages) throws DCMAApplicationException {
		if (pdfOptimizationSwitch != null && pdfOptimizationSwitch.equalsIgnoreCase(ImageMagicKConstants.ON_SWITCH)) {
			String tempFileName = ImageMagicKConstants.TEMP_FILE_NAME + "_" + fTargetFileNamePdf.getName();
			pdfPages[listofPages.size()] = fTargetFileNamePdf.getParent() + File.separator + tempFileName;
			pages[listofPages.size()] = pdfPages[listofPages.size()];
			ghostScriptPDFCreator.createOptimizedPdf(batchInstanceIdentifier, pdfOptimizationParams, fTargetFileNamePdf,
					pdfOptimizationThread, tempFileName, pdfOptimizer);
		} else {
			pages[listofPages.size()] = fTargetFileNamePdf.getAbsolutePath();
			pdfPages[listofPages.size()] = fTargetFileNamePdf.getAbsolutePath();
		}
	}

	private void addThreadToPdfThread(BatchInstanceThread tifToPdfThread, String[] pages) {
		for (int pageIndex = 0; pageIndex < pages.length - 1; pageIndex++) {
			File file = new File(pages[pageIndex]);
			TifToPDFCreator tifToPDFCreator = new TifToPDFCreator(file.getParent(), file.getName());
			tifToPdfThread.add(tifToPDFCreator);
		}
	}

	private void addThreadToTifThread(BatchInstanceThread tifToTifThread, String[] pages) {
		for (int pageIndex = 0; pageIndex < pages.length - 1; pageIndex++) {
			File file = new File(pages[pageIndex]);
			tifToTifThread.add(new TifToTifCreator(file.getParent(), file.getName()));
		}
	}

	private void executeCommandAndOptimizePdf(final String checkPDFExportProcess, final String pdfOptimizationSwitch,
			BatchInstanceThread tifToPdfThread, BatchInstanceThread tifToTifThread, BatchInstanceThread pdfBatchInstanceThread,
			BatchInstanceThread tifBatchInstanceThread, BatchInstanceThread pdfOptimizationThread) throws DCMAApplicationException {
		try {
			LOGGER.info("Executing commands for creation of muti page tiff and pdf using thread pool ..... ");
			tifToTifThread.execute();
			if (checkPDFExportProcess != null && !ImageMagicKConstants.ITEXT.equalsIgnoreCase(checkPDFExportProcess)) {
				tifToPdfThread.execute();
			}
			if (checkPDFExportProcess != null && checkPDFExportProcess.equalsIgnoreCase(ImageMagicKConstants.GHOST_SCRIPT)) {
				pdfBatchInstanceThread.setUsingGhostScript(Boolean.TRUE);
			}
			pdfBatchInstanceThread.execute();
			tifBatchInstanceThread.execute();
			LOGGER.info("Multipage tiff/pdf created .....");
		} catch (DCMAApplicationException dcmae) {
			LOGGER.error("Error in executing command for multi page tiff and pdf using thread pool " + dcmae.getMessage(), dcmae);
			tifToTifThread.remove();
			tifToPdfThread.remove();
			pdfBatchInstanceThread.remove();
			tifBatchInstanceThread.remove();
			// Throw the exception to set the batch status to Error by Application aspect
			throw new DCMAApplicationException(dcmae.getMessage(), dcmae);
		}
		optimizePdfUsingGhostscript(checkPDFExportProcess, pdfOptimizationSwitch, pdfOptimizationThread);
	}

	private void validatingDocumentVsPdfPage(Batch batch, String batchInstanceIdentifier) throws DCMAApplicationException {
		List<Document> documentList = batch.getDocuments().getDocument();
		String systemPath = batch.getBatchLocalPath() + File.separator + batch.getBatchInstanceIdentifier();
		if (documentList != null && documentList.size() > 0) {
			for (Document document : documentList) {
				List<Page> pageList = document.getPages().getPage();
				if (pageList != null) {
					int numberOfDocumentPage = pageList.size();
					String pdfFilePath = systemPath + File.separator + document.getMultiPagePdfFile();
					int numberOfPDFPage = PDFUtil.getPDFPageCount(pdfFilePath);
					if (numberOfDocumentPage != numberOfPDFPage) {
						throw new DCMAApplicationException("Number of pages mismatched in multipage PDF and batch xml for document "
								+ document.getIdentifier() + " for batch.Batch Instance ID:" + batchInstanceIdentifier);
					}
				}
			}
		}
	}

	private void updateMultiPageTifToXml(Map<String, List<File>> documentPageMap, Batch pasrsedXMLFile, String multipageTifSwitch,
			Set<String> documentNames, List<MultiPageExecutor> multiPageExecutorsTiff, List<MultiPageExecutor> multiPageExecutorsPdf) {
		String documentIdInt;
		if (multipageTifSwitch != null && multipageTifSwitch.equalsIgnoreCase(ImageMagicKConstants.ON_SWITCH)) {
			LOGGER.info("Updating files for multi page tiff");
			Iterator<String> iter = documentNames.iterator();
			for (MultiPageExecutor multiPageExecutor : multiPageExecutorsTiff) {
				String[] pageArray = multiPageExecutor.getPages();
				if (iter.hasNext()) {
					documentIdInt = iter.next();
					List<File> listofPages = documentPageMap.get(documentIdInt);
					String[] pages = new String[listofPages.size() + 1];
					int index = 0;
					for (File page : listofPages) {
						pages[index++] = page.getAbsolutePath();
					}
					pages[index] = pageArray[pageArray.length - 1];
					updateMultiPageTifToXmlObject(pages, pasrsedXMLFile);
				}
			}
		}
		LOGGER.info("Updating files for multi page pdf");
		for (MultiPageExecutor multiPageExecutor : multiPageExecutorsPdf) {
			updateMultiPageTifToXmlObject(multiPageExecutor.getPages(), pasrsedXMLFile);
		}
		LOGGER.info("Processing complete at " + new Date());
	}


	/**
	 * This method updates the Unmarsheled xml file with the information of multi page pdf or tif file.
	 * 
	 * @param pages
	 * @param pasrsedXMLFile
	 */
	private void updateMultiPageTifToXmlObject(String[] pages, Batch pasrsedXMLFile) {
		LOGGER.info("Started updating multi page tiff/pdf file in batch xml.");
		String newFileName = new File(pages[0]).getName();
		String multiPageFileName = new File(pages[pages.length - 1]).getName();
		String tempPrefix = ImageMagicKConstants.TEMP_FILE_NAME + "_";
		if (multiPageFileName.startsWith(tempPrefix)) {
			multiPageFileName = multiPageFileName.substring(tempPrefix.length());
		}
		Documents documents = pasrsedXMLFile.getDocuments();
		if (documents != null) {
			List<Document> listOfDocuments = documents.getDocument();
			if (listOfDocuments != null) {
				LOGGER.info("Number of documents in the batch." + listOfDocuments.size());
				for (Document document : listOfDocuments) {
					Pages pagesObj = document.getPages();
					if (pagesObj != null) {
						List<Page> listOfPages = pagesObj.getPage();
						if (listOfPages != null) {
							LOGGER.info("Number of pages in document " + document.getIdentifier() + " are - " + listOfDocuments.size()
									+ ".");
							for (Page page : listOfPages) {
								if (page != null) {
									String fileName = page.getNewFileName();
									if (fileName.equals(newFileName)
											|| fileName.substring(0, fileName.lastIndexOf('.')).equals(
													newFileName.substring(0, newFileName.lastIndexOf('.')))) {
										LOGGER.info("Multi page file name is " + multiPageFileName);
										if (multiPageFileName.endsWith(FileType.PDF.getExtensionWithDot())) {
											document.setMultiPagePdfFile(multiPageFileName);
										}
										if (multiPageFileName.endsWith(FileType.TIF.getExtensionWithDot())) {
											document.setMultiPageTiffFile(multiPageFileName);
										}
										break;
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * This method creates a Map containing the Document id as the key and the a list of pages as the value. This map is created on the
	 * basis of the batch.xml file information of which is received by this method as the pasrsedXMLFile object
	 * 
	 * @param pasrsedXMLFile OO representation of the xml file
	 * @param batchInstanceID
	 * @return Map
	 */
	private Map<String, List<File>> createDocumentPageMap(final Batch pasrsedXMLFile, String batchInstanceID) {

		List<Document> xmlDocuments = pasrsedXMLFile.getDocuments().getDocument();
		HashMap<String, List<File>> documentPageMap = new HashMap<String, List<File>>();

		for (Document document : xmlDocuments) {
			String documentId = document.getIdentifier();
			List<Page> listOfPages = document.getPages().getPage();
			List<File> listOfFiles = new LinkedList<File>();
			LOGGER.info("Document documentid =" + documentId + " contains the following pages:");
			for (Page page : listOfPages) {
				String sImageFile = page.getNewFileName();
				LOGGER.info("Page File Name:" + sImageFile);
				File fImageFile = batchSchemaService.getFile(batchInstanceID, sImageFile);
				if (fImageFile.exists()) {
					listOfFiles.add(fImageFile);
				} else {
					throw new DCMABusinessException("File does not exist File Name=" + fImageFile);
				}
			}
			documentPageMap.put(documentId, listOfFiles);
		}
		return documentPageMap;
	}

	private int getWidthOfLineInt() {
		int widthOfLineInt = ImageMagicKConstants.DEFAULT_WIDTH_OF_LINE;
		try {
			widthOfLineInt = Integer.parseInt(kvFinderService.getWidthOfLine());
		} catch (NumberFormatException nfe) {
			LOGGER.error("Invalid width of line specified. Setting it to its default value.");
		}
		return widthOfLineInt;
	}

}
