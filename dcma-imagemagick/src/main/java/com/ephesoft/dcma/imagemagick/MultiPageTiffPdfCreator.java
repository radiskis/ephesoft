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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.BatchStatus;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Batch.Documents;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.EphesoftProperty;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.imagemagick.constant.ImageMagicKConstants;
import com.ephesoft.dcma.util.FileNameFormatter;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.OSUtil;

/**
 * This class is contains methods which can detect the batch.xml file in a folder and export it to the specified folder.
 * 
 * @author Ephesoft
 * 
 */
@Component
public class MultiPageTiffPdfCreator implements ICommonConstants {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private BatchSchemaService batchSchemaService;
	public static final String IM4JAVA_TOOLPATH = "IM4JAVA_TOOLPATH";
	public static final String JAVA_LIBRARY_PATH = "java.library.path";
	public static final String JAVA_TEMP_DIR_PATH = "java.io.tmpdir";
	public static final String JAVA_OPTION_PREFIX = "-D";
	public static final String FILE_NAME = "fileNames.txt";

	/**
	 * List of commands for multipage tiff generation.
	 */
	private transient String tiffCmds;

	/**
	 * List of commands for unix for multipage tiff generation.
	 */
	private transient String unixTiffCmds;
	/**
	 * List of commands for multipage pdf generation.
	 */
	private transient String pdfCmds;

	/**
	 * List of commands for unix for multipage pdf generation.
	 */
	private transient String unixPdfCmds;

	/**
	 * Name of the jar to covert hocr into pdf.
	 */
	private transient String jarName;

	/**
	 * PDF compression to be set while creating multipage-pdf.
	 */
	private transient String pdfCompression;

	/**
	 * PDF quality to be set while creating multipage-pdf.
	 */
	private transient String pdfQuality;

	/**
	 * colored/monochrome to be set while creating multipage-pdf.
	 */
	private transient String coloredImage;

	/**
	 * @return unix compatible pdf commands
	 */
	public String getUnixPdfCmds() {
		return unixPdfCmds;
	}

	/**
	 * @param unixPdfCmds unix compatible pdf commands
	 */
	public void setUnixPdfCmds(String unixPdfCmds) {
		this.unixPdfCmds = unixPdfCmds;
	}

	/**
	 * @return unix compatible tiff commands
	 */
	public String getUnixTiffCmds() {
		return unixTiffCmds;
	}

	/**
	 * @param unixTiffCmds unix compatible tiff commands
	 */
	public void setUnixTiffCmds(String unixTiffCmds) {
		this.unixTiffCmds = unixTiffCmds;
	}

	/**
	 * @return the tiffCmds
	 */
	public String getTiffCmds() {
		if (OSUtil.isWindows()) {
			return tiffCmds;
		} else {
			return unixTiffCmds;
		}
	}

	/**
	 * @param tiffCmds the tiffCmds to set
	 */
	public void setTiffCmds(String tiffCmds) {
		this.tiffCmds = tiffCmds;
	}

	/**
	 * @return the pdfCmds
	 */
	public String getPdfCmds() {
		if (OSUtil.isWindows()) {
			return pdfCmds;
		} else {
			return unixPdfCmds;
		}
	}

	/**
	 * @param pdfCmds the pdfCmds to set
	 */
	public void setPdfCmds(String pdfCmds) {
		this.pdfCmds = pdfCmds;
	}

	/**
	 * @return the jarName
	 */
	public String getJarName() {
		return jarName;
	}

	/**
	 * @param jarName the jarName to set
	 */
	public void setJarName(String jarName) {
		this.jarName = jarName;
	}

	/**
	 * @return the pdfCompression
	 */
	public String getPdfCompression() {
		return pdfCompression;
	}

	/**
	 * @param pdfCompression the pdfCompression to set
	 */
	public void setPdfCompression(String pdfCompression) {
		this.pdfCompression = pdfCompression;
	}

	/**
	 * @return the pdfQuality
	 */
	public String getPdfQuality() {
		return pdfQuality;
	}

	/**
	 * @param pdfQuality the pdfQuality to set
	 */
	public void setPdfQuality(String pdfQuality) {
		this.pdfQuality = pdfQuality;
	}

	/**
	 * @return the coloredImage
	 */
	public String getColoredImage() {
		return coloredImage;
	}

	/**
	 * @param coloredImage the coloredImage to set
	 */
	public void setColoredImage(String coloredImage) {
		this.coloredImage = coloredImage;
	}

	/**
	 * This method takes the FolderToBeExported path and creates multi page tiff and PDF files based on the batch.xml file found in the
	 * folder.
	 * 
	 * @return File
	 * @param sFolderToBeExported
	 * @param batchInstanceIdentifier
	 * @param pluginName
	 * @throws DCMAApplicationException
	 */
	public void createMultiPageFiles(String sFolderToBeExported, String batchInstanceIdentifier,
			BatchSchemaService batchSchemaService, String multipageTifSwitch, String checkPDFExportProcess, String checkColouredPDF,
			String checkSearchablePDF, String pluginName) throws DCMAApplicationException {
		this.batchSchemaService = batchSchemaService;
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
			updateXMLFileFailiure(batchSchemaService, batch);
			throw new DCMAApplicationException(e.getMessage(), e);
		}
		try {
			createMultiPageTiffAndPDF(documentPageMap, fFolderToBeExported, batch, batchInstanceIdentifier, multipageTifSwitch,
					checkPDFExportProcess, checkColouredPDF, checkSearchablePDF, pluginName);
		} catch (Exception e) {
			updateXMLFileFailiure(batchSchemaService, batch);
			throw new DCMAApplicationException(e.getMessage(), e);
		}

		updateXMLFileSucess(batchSchemaService, batch);

	}

	/**
	 * Sanity check.Checks if there are still any unknown document in the batch.xml file. If any unknown document is found an exception
	 * is thrown.
	 * 
	 * @param pasrsedXMLFile
	 */
	private void checkForUnknownDocument(Batch pasrsedXMLFile) {
		Documents documents = pasrsedXMLFile.getDocuments();
		if (documents != null) {
			List<Document> listOfDocuments = documents.getDocument();
			if (listOfDocuments == null) {
				return;
			}
			for (Document document : listOfDocuments) {

				if (document == null) {
					return;
				}
				if (document.getType().equalsIgnoreCase(EphesoftProperty.UNKNOWN.getProperty())) {
					Pages pages = document.getPages();
					if (pages == null) {
						return;
					}
					List<Page> listOfPages = pages.getPage();
					if (listOfPages == null) {
						return;
					}
					if (listOfPages.isEmpty()) {
						return;
					}
					throw new DCMABusinessException("Final xml document contains unknown documents.Cannot be exported.");
				}
			}

		}

	}

	/**
	 * This method fetched the batch.xml file from the exported folder and updates the batch.xml file as successful.
	 * 
	 * @param batchSchemaService BatchSchemaService
	 * @param batch OO representation of the xml file
	 * @throws DCMAApplicationException
	 */
	private void updateXMLFileSucess(BatchSchemaService batchSchemaService, Batch batch) throws DCMAApplicationException {
		batch.setBatchStatus(BatchStatus.FINISHED);
		batchSchemaService.updateBatch(batch);
	}

	/**
	 * This method updates the batch.xml file state as error.
	 * 
	 * @param xmlFile The xml file to be updates
	 * @param pasrsedXMLFile OO representation of the xml file
	 * @throws DCMAApplicationException
	 */
	private void updateXMLFileFailiure(BatchSchemaService batchSchemaService, Batch batch) throws DCMAApplicationException {
		batch.setBatchStatus(BatchStatus.ERROR);
		batchSchemaService.updateBatch(batch);
	}

	/**
	 * This method generates the multi page tiff and pdf files using Image Magick(Wrapped using im4java). The document classification
	 * is based on the documentPageMap.
	 * 
	 * @param documentPageMap
	 * @param exportToFolder
	 * @param batchInstanceIdentifier
	 * @param pasrsedXMLFile
	 * @param pluginName
	 * @throws DCMAApplicationException
	 */
	private void createMultiPageTiffAndPDF(Map<String, List<File>> documentPageMap, File exportToFolder, Batch pasrsedXMLFile,
			String batchInstanceIdentifier, String multipageTifSwitch, String checkPDFExportProcess, String checkColouredPDF,
			String checkSearchablePDF, String pluginName) throws DCMAApplicationException {

		String envVariable = System.getenv(IImageMagickCommonConstants.IMAGEMAGICK_ENV_VARIABLE);
		if (envVariable == null || envVariable.isEmpty()) {
			throw new DCMABusinessException("Enviornment Variable IM4JAVA_TOOLPATH not set.");
		}
		Set<String> documentNames;
		documentNames = documentPageMap.keySet();
		Iterator<String> iterator = documentNames.iterator();
		String documentIdInt;
		String documentName;
		String sTargetFileNameTif;
		File fTargetFileNameTif;
		String sTargetFileNamePdf;
		File fTargetFileNamePdf;
		String[] pages;
		BatchInstanceThread batchInstanceThread = new BatchInstanceThread();

		String threadPoolLockFolderPath = batchSchemaService.getLocalFolderLocation() + File.separator + batchInstanceIdentifier
				+ File.separator + batchSchemaService.getThreadpoolLockFolderName();
		try {
			FileUtils.createThreadPoolLockFile(batchInstanceIdentifier, threadPoolLockFolderPath, pluginName);
		} catch (IOException ioe) {
			LOGGER.error("Error in creating threadpool lock file" + ioe.getMessage(), ioe);
			throw new DCMABusinessException(ioe.getMessage(), ioe);
		}

		List<MultiPageExecutor> multiPageExecutorsTiff = new ArrayList<MultiPageExecutor>();
		List<MultiPageExecutor> multiPageExecutorsPdf = new ArrayList<MultiPageExecutor>();
		while (iterator.hasNext()) {
			documentIdInt = iterator.next();
			LOGGER.info("Started creating multipage Tif and PDF for the " + "document with document id=" + documentIdInt);
			List<File> listofPages = documentPageMap.get(documentIdInt);
			if (listofPages.size() == 0) {
				continue;
			}

			try {
				documentName = new FileNameFormatter().getMuliPageFileName(documentIdInt, batchInstanceIdentifier, EXTENSION_TIF);
			} catch (Exception e1) {
				throw new DCMAApplicationException("Problem getting file name", e1);

			}
			sTargetFileNameTif = exportToFolder.getAbsolutePath() + File.separator + File.separator + documentName;
			fTargetFileNameTif = new File(sTargetFileNameTif);
			try {
				documentName = new FileNameFormatter().getMuliPageFileName(documentIdInt, batchInstanceIdentifier, EXTENSION_PDF);
			} catch (Exception e1) {
				throw new DCMAApplicationException("Problem getting file name", e1);
			}
			sTargetFileNamePdf = exportToFolder.getAbsolutePath() + File.separator + File.separator + documentName;
			fTargetFileNamePdf = new File(sTargetFileNamePdf);
			pages = new String[listofPages.size() + 1];
			int i = 0;
			for (File page : listofPages) {
				pages[i] = page.getAbsolutePath();
				i++;
			}

			ConvertCmd convert = new ConvertCmd();
			IMOperation op = new IMOperation();
			op.addImage();
			op.addImage();
			for (int pageIndex = 0; pageIndex < pages.length - 1; pageIndex++) {
				try {
					convert.run(op, pages[pageIndex], pages[pageIndex]);
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			convert = null;
			op = null;

			pages[listofPages.size()] = fTargetFileNameTif.getAbsolutePath();
			if (multipageTifSwitch != null && multipageTifSwitch.equalsIgnoreCase("ON")) {
				LOGGER.info("Adding command for multi page tiff execution");
				multiPageExecutorsTiff.add(new MultiPageExecutor(batchInstanceThread, pages));
			}
			LOGGER.info("Adding command for multi page pdf execution");
			pages[listofPages.size()] = fTargetFileNamePdf.getAbsolutePath();
			// use this method to create pdf using image-magick convert command
			if (checkPDFExportProcess != null
					&& checkPDFExportProcess.equalsIgnoreCase(ImageMagicKConstants.IMAGE_MAGIC_PLUGIN_NAME_CONSTANT)) {
				createPDFUsingImagemagick(pasrsedXMLFile, batchInstanceIdentifier, exportToFolder.getAbsolutePath(), pages,
						batchInstanceThread, multiPageExecutorsPdf, documentIdInt);
			}

			// use this method to create pdf using hocrtopdf.jar
			else if (checkPDFExportProcess != null
					&& checkPDFExportProcess.equalsIgnoreCase(ImageMagicKConstants.HOCR_TO_PDF_PLUGIN_NAME_CONSTANT)) {
				createPDFFromHOCR(pasrsedXMLFile, batchInstanceIdentifier, exportToFolder.getAbsolutePath(), pages,
						batchInstanceThread, multiPageExecutorsPdf, documentIdInt, checkColouredPDF, checkSearchablePDF);
			}
			// use this default method to create pdf using image-magick convert command
			else {
				createPDFUsingImagemagick(pasrsedXMLFile, batchInstanceIdentifier, exportToFolder.getAbsolutePath(), pages,
						batchInstanceThread, multiPageExecutorsPdf, documentIdInt);
			}
		}

		try {
			LOGGER.info("Executing commands for creation of muti page tiff and pdf using thread pool");
			batchInstanceThread.execute();
			LOGGER.info("Multipage tiff/pdf creation ends");
		} catch (DCMAApplicationException dcmae) {
			LOGGER.error("Error in executing command for multi page tiff and pdf using thread pool" + dcmae.getMessage(), dcmae);
			batchInstanceThread.remove();
			// Throw the exception to set the batch status to Error by Application aspect
			throw new DCMAApplicationException(dcmae.getMessage(), dcmae);
		} finally {
			try {
				FileUtils.deleteThreadPoolLockFile(batchInstanceIdentifier, threadPoolLockFolderPath, pluginName);
			} catch (IOException ioe) {
				LOGGER.error("Error in deleting threadpool lock file" + ioe.getMessage(), ioe);
				throw new DCMABusinessException(ioe.getMessage(), ioe);
			}
		}

		if (multipageTifSwitch != null && multipageTifSwitch.equalsIgnoreCase("ON")) {
			LOGGER.info("Updating files for multi page tiff");
			for (MultiPageExecutor multiPageExecutor : multiPageExecutorsTiff) {
				updateMultiPageTifToXmlObject(multiPageExecutor.getPages(), pasrsedXMLFile);
			}
		}
		LOGGER.info("Updating files for multi page pdf");
		for (MultiPageExecutor multiPageExecutor : multiPageExecutorsPdf) {
			updateMultiPageTifToXmlObject(multiPageExecutor.getPages(), pasrsedXMLFile);
		}
		LOGGER.info("Processing complete at " + new Date());
	}

	public void createPDFUsingImagemagick(Batch batchXML, String batchInstanceID, String localFolder, String[] pages,
			BatchInstanceThread batchInstanceThread, List<MultiPageExecutor> multiPageExecutors, String documentIdInt)
			throws DCMAApplicationException {
		LOGGER.info("Adding command for multi page pdf execution");
		multiPageExecutors.add(new MultiPageExecutor(batchInstanceThread, pages, pdfCompression, pdfQuality, coloredImage));
	}

	public boolean createPDFFromHOCR(Batch batchXML, String batchInstanceID, String localFolder, String[] pages,
			BatchInstanceThread batchInstanceThread, List<MultiPageExecutor> multiPageExecutors, String documentIdInt,
			String checkColouredPDF, String checkSearchablePDF) throws DCMAApplicationException {
		String checkColouredImage = "false";
		String checkSearchableImage = "false";
		if (checkColouredPDF != null && !checkColouredPDF.isEmpty()) {
			checkColouredImage = checkColouredPDF;
		}
		if (checkSearchablePDF != null && !checkSearchablePDF.isEmpty()) {
			checkSearchableImage = checkSearchablePDF;
		}
		boolean returnValue = false;
		if (pages != null && pages.length > 0) {
			try {
				String[] cmds = new String[12];
				String[] allPdfCmds = getPdfCmds().split(";");
				int pdfCount = 0;
				if (allPdfCmds != null && allPdfCmds.length > 0) {
					for (String eachPdfCmd : allPdfCmds) {
						if(eachPdfCmd.contains("-jar")) {
							break; 
						} else {	
							cmds[pdfCount] = eachPdfCmd;
							pdfCount++;
						}
					}
				}
				String tempDirPath = System.getProperty(JAVA_TEMP_DIR_PATH);
				if (tempDirPath != null && !tempDirPath.isEmpty()) {
					cmds[pdfCount] = JAVA_OPTION_PREFIX + JAVA_TEMP_DIR_PATH + "=" + tempDirPath;
				}
				pdfCount++;
				cmds[pdfCount] = allPdfCmds[pdfCount - 1];
				pdfCount++;
				cmds[pdfCount] = System.getProperty(JAVA_LIBRARY_PATH) + File.separator + jarName;
				int count = pdfCount + 1;
				String batchInstanceFolder = batchSchemaService.getLocalFolderLocation() + File.separator + batchInstanceID;
				writePageNamesToFile(batchXML, pages, batchInstanceFolder, documentIdInt);
				if (OSUtil.isWindows()) {
					cmds[count] = "\"" + batchInstanceFolder + "\"";
					count++;
					cmds[count] = "\"" + pages[pages.length - 1] + "\"";
				} else {
					cmds[count] = batchInstanceFolder;
					count++;
					cmds[count] = pages[pages.length - 1];
				}
				count++;
				cmds[count] = allPdfCmds[pdfCount - 1];
				count++;
				cmds[count] = documentIdInt;
				count++;
				cmds[count] = checkColouredImage;
				count++;
				cmds[count] = checkSearchableImage;
				LOGGER.info("command formed is :");
				for (int ind = 0; ind < cmds.length; ind++) {
					LOGGER.info(cmds[ind] + " ");
				}
				multiPageExecutors.add(new MultiPageExecutor(cmds, null, batchInstanceThread, pages));
				returnValue = true;
			} catch (Exception e) {
				LOGGER.error("Exception while generating PDF", e);
				throw new DCMAApplicationException("Exception while generating PDF", e);
			}
		}
		return returnValue;
	}

	public void writePageNamesToFile(Batch batchXML, String[] pages, String batchInstanceFolder, String documentIdInt)
			throws DCMAApplicationException {
		FileOutputStream foOutputStream = null;
		DataOutputStream dataOutputStream = null;
		try {
			foOutputStream = new FileOutputStream(new File(batchInstanceFolder + File.separator + documentIdInt + FILE_NAME));
			if (foOutputStream != null) {
				dataOutputStream = new DataOutputStream(foOutputStream);
				if (dataOutputStream != null && pages != null && pages.length > 0) {
					String writeContent = "";
					for (int i = 0; i < pages.length - 1; i++) {
						String htmlName = getHOCRFileNameForImage(batchXML, pages[i]);
						if (htmlName != null) {
							writeContent += htmlName;
							if (i < pages.length - 2) {
								writeContent += ";;";
							}
						}
					}
					LOGGER.info("Content to be written in text file :" + writeContent);
					dataOutputStream.writeUTF(writeContent);
				}
			}
		} catch (FileNotFoundException e) {
			LOGGER.error("Problem in creating file " + FILE_NAME + " at location :" + batchInstanceFolder);
			throw new DCMAApplicationException("Problem in creating file " + FILE_NAME + " at location :" + batchInstanceFolder);
		} catch (IOException e) {
			LOGGER.error("Problem in saving file " + FILE_NAME + " at location :" + batchInstanceFolder);
			throw new DCMAApplicationException("Problem in saving file " + FILE_NAME + " at location :" + batchInstanceFolder);
		} finally {
			try {
				if (dataOutputStream != null) {
					dataOutputStream.close();
				}
				if (foOutputStream != null) {
					foOutputStream.close();
				}
			} catch (IOException e) {
				LOGGER.error(
						"Problem in closing the streams for :" + batchInstanceFolder + File.separator + documentIdInt + FILE_NAME, e);
			}
		}
	}

	private String getHOCRFileNameForImage(Batch batchXML, String imageName) {
		String returnValue = null;
		List<Document> xmlDocuments = batchXML.getDocuments().getDocument();
		for (Document document : xmlDocuments) {
			List<Page> listOfPages = document.getPages().getPage();
			for (Page page : listOfPages) {
				String fileName = imageName.substring(imageName.lastIndexOf(File.separator) + 1, imageName.length());
				if (fileName != null && fileName.equalsIgnoreCase(page.getNewFileName())) {
					returnValue = imageName.substring(0, imageName.lastIndexOf(File.separator)) + File.separator
							+ page.getHocrFileName();
				}
			}
		}
		return returnValue;
	}

	/**
	 * This method updates the Unmarsheled xml file with the information of multi page pdf or tif file.
	 * 
	 * @param pages
	 * @param pasrsedXMLFile
	 */
	private void updateMultiPageTifToXmlObject(String[] pages, Batch pasrsedXMLFile) {
		String newFileName = new File(pages[0]).getName();
		String multiPageFileName = new File(pages[pages.length - 1]).getName();
		Documents documents = pasrsedXMLFile.getDocuments();
		if (documents == null) {
			return;
		}
		List<Document> listOfDocuments = documents.getDocument();
		if (listOfDocuments == null) {
			return;
		}
		for (Document document : listOfDocuments) {

			Pages pagesObj = document.getPages();
			if (pagesObj == null) {
				return;
			}
			List<Page> listOfPages = pagesObj.getPage();
			if (listOfPages == null) {
				return;
			}
			for (Page page : listOfPages) {

				if (page == null) {
					return;
				}
				if (page.getNewFileName().equals(newFileName)) {

					if (multiPageFileName.endsWith("pdf")) {
						document.setMultiPagePdfFile(multiPageFileName);
					}
					if (multiPageFileName.endsWith("tif")) {
						document.setMultiPageTiffFile(multiPageFileName);
					}
					break;
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

}
