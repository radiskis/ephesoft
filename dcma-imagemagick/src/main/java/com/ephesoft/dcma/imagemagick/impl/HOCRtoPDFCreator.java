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

package com.ephesoft.dcma.imagemagick.impl;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.imagemagick.ImageMagicProperties;
import com.ephesoft.dcma.imagemagick.MultiPageExecutor;
import com.ephesoft.dcma.imagemagick.constant.ImageMagicKConstants;
import com.ephesoft.dcma.util.OSUtil;

/**
 * This class is used to create the pdf file using HOCR to PDF.
 * 
 * @author Ephesoft
 */
public class HOCRtoPDFCreator {

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

	public static final String JAVA_LIBRARY_PATH = "java.library.path";
	public static final String JAVA_TEMP_DIR_PATH = "java.io.tmpdir";
	public static final String JAVA_OPTION_PREFIX = "-D";
	private static final String FALSE = "false";
	private static final String SEMI_COLON = ";";
	private static final String QUOTES = "\"";
	public static final String FILE_NAME = "fileNames.txt";

	/**
	 * Reference for batchSchemaService.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Reference for pluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * Variable for logging.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HOCRtoPDFCreator.class);

	/**
	 * @param batchSchemaService the batchSchemaService to set
	 */
	public void setBatchSchemaService(BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * @param pdfCmds the pdfCmds to set
	 */
	public void setPdfCmds(String pdfCmds) {
		this.pdfCmds = pdfCmds;
	}

	/**
	 * @param unixPdfCmds the unixPdfCmds to set
	 */
	public void setUnixPdfCmds(String unixPdfCmds) {
		this.unixPdfCmds = unixPdfCmds;
	}

	/**
	 * @param jarName the jarName to set
	 */
	public void setJarName(String jarName) {
		this.jarName = jarName;
	}

	/**
	 * API for getting PDF Command for windows/Linux.
	 * 
	 * @return the pdfCmds
	 */
	public String getPdfCmds() {
		String command = null;
		if (OSUtil.isWindows()) {
			command = pdfCmds;
		} else {
			command = unixPdfCmds;
		}
		return command;
	}

	/**
	 * API for creating pdf for HOCR file.
	 * 
	 * @param batchXML
	 * @param batchInstanceID
	 * @param localFolder
	 * @param pages
	 * @param batchInstanceThread
	 * @param multiPageExecutors
	 * @param documentIdInt
	 * @return
	 * @throws DCMAApplicationException
	 */
	public boolean createPDFFromHOCR(Batch batchXML, String batchInstanceID, String localFolder, String[] pages,
			BatchInstanceThread batchInstanceThread, List<MultiPageExecutor> multiPageExecutors, String documentIdInt)
			throws DCMAApplicationException {
		String checkColouredImage = FALSE;
		String checkSearchableImage = FALSE;
		String checkColouredPDF = pluginPropertiesService.getPropertyValue(batchInstanceID,
				ImageMagicKConstants.CREATEMULTIPAGE_FILES_PLUGIN, ImageMagicProperties.CHECK_COLOURED_PDF);
		String checkSearchablePDF = pluginPropertiesService.getPropertyValue(batchInstanceID,
				ImageMagicKConstants.CREATEMULTIPAGE_FILES_PLUGIN, ImageMagicProperties.CHECK_SEARCHABLE_PDF);
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
				String[] allPdfCmds = getPdfCmds().split(SEMI_COLON);
				int pdfCount = 0;
				if (allPdfCmds != null && allPdfCmds.length > 0) {
					for (String eachPdfCmd : allPdfCmds) {
						if (eachPdfCmd.contains("-jar")) {
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
					cmds[count] = QUOTES + batchInstanceFolder + QUOTES;
					count++;
					cmds[count] = QUOTES + pages[pages.length - 1] + QUOTES;
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
				throw new DCMAApplicationException("Exception while generating PDF" + e.getMessage(), e);
			}
		}
		return returnValue;
	}

	/**
	 * API for creating file and writing information into it.
	 * 
	 * @param batchXML
	 * @param pages
	 * @param batchInstanceFolder
	 * @param documentIdInt
	 * @throws DCMAApplicationException
	 */
	public void writePageNamesToFile(Batch batchXML, String[] pages, String batchInstanceFolder, String documentIdInt)
			throws DCMAApplicationException {
		FileOutputStream foOutputStream = null;
		DataOutputStream dataOutputStream = null;
		try {
			foOutputStream = new FileOutputStream(new File(batchInstanceFolder + File.separator + documentIdInt + FILE_NAME));
			if (foOutputStream != null) {
				dataOutputStream = new DataOutputStream(foOutputStream);
				if (dataOutputStream != null && pages != null && pages.length > 0) {
					StringBuffer writeContent = new StringBuffer();
					for (int i = 0; i < pages.length - 1; i++) {
						String htmlName = getHOCRFileNameForImage(batchXML, pages[i]);
						if (htmlName != null) {
							writeContent.append(htmlName);
							writeContent.append('|');
							writeContent.append(pages[i]);
							if (i < pages.length - 2) {
								writeContent.append(";;");
							}
						}
					}
					LOGGER.info("Content to be written in text file :" + writeContent);
					dataOutputStream.writeUTF(writeContent.toString());
				}
			}
		} catch (FileNotFoundException e) {
			LOGGER.error("Problem in creating file " + FILE_NAME + " at location :" + batchInstanceFolder, e);
			throw new DCMAApplicationException("Problem in creating file " + FILE_NAME + " at location :" + batchInstanceFolder
					+ e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.error("Problem in saving file " + FILE_NAME + " at location :" + batchInstanceFolder, e);
			throw new DCMAApplicationException("Problem in saving file " + FILE_NAME + " at location :" + batchInstanceFolder, e);
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

	/**
	 * API for getting HOCR file name for specified image.
	 * 
	 * @param batchXML
	 * @param imageName
	 * @return
	 */
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
	 * This method generates the PDF file on the basis of color image, searchable image. This methods reads the images from input
	 * folder and generates the pdf at the output folder location.
	 * 
	 * @param checkColorImage {@link String}
	 * @param checkSearchableImage {@link String}
	 * @param inputFolderLocation {@link String}
	 * @param imageFiles String[]
	 * @param batchInstanceThread {@link BatchInstanceThread}
	 * @param documentId {@link String}
	 * @return returnValue
	 * @throws DCMAApplicationException
	 */
	public boolean createPDFFromHOCR(String checkColorImage, String checkSearchableImage, String inputFolderLocation,
			String[] imageFiles, BatchInstanceThread batchInstanceThread, String documentId) throws DCMAApplicationException {
		List<MultiPageExecutor> multiPageExecutors = new ArrayList<MultiPageExecutor>();
		boolean returnValue = false;
		if (imageFiles != null && imageFiles.length > 0) {
			try {
				String[] cmds = new String[12];
				String[] allPdfCmds = getPdfCmds().split(SEMI_COLON);
				int pdfCount = 0;
				if (allPdfCmds != null && allPdfCmds.length > 0) {
					for (String eachPdfCmd : allPdfCmds) {
						if (eachPdfCmd.contains("-jar")) {
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
				writePageNamesToFile(imageFiles, inputFolderLocation, documentId);
				if (OSUtil.isWindows()) {
					cmds[count] = QUOTES + inputFolderLocation + QUOTES;
					count++;
					cmds[count] = QUOTES + imageFiles[imageFiles.length - 1] + QUOTES;
				} else {
					cmds[count] = inputFolderLocation;
					count++;
					cmds[count] = imageFiles[imageFiles.length - 1];
				}
				count++;
				cmds[count] = allPdfCmds[pdfCount - 1];
				count++;
				cmds[count] = documentId;
				count++;
				cmds[count] = checkColorImage;
				count++;
				cmds[count] = checkSearchableImage;
				LOGGER.info("command formed is :");
				for (int ind = 0; ind < cmds.length; ind++) {
					LOGGER.info(cmds[ind] + " ");
				}
				multiPageExecutors.add(new MultiPageExecutor(cmds, null, batchInstanceThread, imageFiles));
				returnValue = true;
			} catch (Exception e) {
				LOGGER.error("Exception while generating PDF", e);
				throw new DCMAApplicationException("Exception while generating PDF" + e.getMessage(), e);
			}
		}
		return returnValue;
	}

	private void writePageNamesToFile(String[] pages, String batchInstanceFolder, String documentIdInt)
			throws DCMAApplicationException {
		FileOutputStream foOutputStream = null;
		DataOutputStream dataOutputStream = null;
		try {
			foOutputStream = new FileOutputStream(new File(batchInstanceFolder + File.separator + documentIdInt + FILE_NAME));
			if (foOutputStream != null) {
				dataOutputStream = new DataOutputStream(foOutputStream);
				if (dataOutputStream != null && pages != null && pages.length > 0) {
					StringBuffer writeContent = new StringBuffer();
					for (int i = 0; i < pages.length - 1; i++) {
						String htmlName = getHOCRFileNameForImage(pages[i]);
						if (htmlName != null) {
							writeContent.append(htmlName);
							writeContent.append('|');
							writeContent.append(pages[i]);
							if (i < pages.length - 2) {
								writeContent.append(";;");
							}
						}
					}
					LOGGER.info("Content to be written in text file :" + writeContent);
					dataOutputStream.writeUTF(writeContent.toString());
				}
			}
		} catch (FileNotFoundException e) {
			LOGGER.error("Problem in creating file " + FILE_NAME + " at location :" + batchInstanceFolder, e);
			throw new DCMAApplicationException("Problem in creating file " + FILE_NAME + " at location :" + batchInstanceFolder
					+ e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.error("Problem in saving file " + FILE_NAME + " at location :" + batchInstanceFolder, e);
			throw new DCMAApplicationException("Problem in saving file " + FILE_NAME + " at location :" + batchInstanceFolder, e);
		} finally {
			try {
				if (dataOutputStream != null) {
					dataOutputStream.close();
				}
				if (foOutputStream != null) {
					foOutputStream.close();
				}
			} catch (IOException e) {
				LOGGER.info(
						"Problem in closing the streams for :" + batchInstanceFolder + File.separator + documentIdInt + FILE_NAME, e);
			}
		}
	}

	private String getHOCRFileNameForImage(String imageFile) {
		String returnValue = imageFile.substring(0, imageFile.lastIndexOf(".")) + ".html";
		return returnValue;
	}
}
