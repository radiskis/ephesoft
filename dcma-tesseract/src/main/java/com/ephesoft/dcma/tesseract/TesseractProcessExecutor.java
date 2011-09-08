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

package com.ephesoft.dcma.tesseract;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.core.TesseractVersionProperty;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.core.threadpool.ProcessExecutor;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.FileNameFormatter;
import com.ephesoft.dcma.util.OSUtil;

public class TesseractProcessExecutor {

	private String fileName;
	private Batch batch;
	private String batchInstanceID;
	private List<String> cmdList;
	private String actualFolderLocation;
	private String cmdLanguage;
	private BatchInstanceThread thread;
	private String targetHOCR;
	private String tesseractVersion;
	private String colorSwitch;

	private static final Logger LOGGER = LoggerFactory.getLogger(TesseractProcessExecutor.class);

	public TesseractProcessExecutor(String fileName, Batch batch, String batchInstanceID, List<String> cmdList,
			String actualFolderLocation, String cmdLanguage, BatchInstanceThread thread, String tesseractVersion, String colorSwitch)
			throws DCMAApplicationException {
		this.fileName = fileName;
		this.batch = batch;
		this.batchInstanceID = batchInstanceID;
		this.cmdList = cmdList;
		this.actualFolderLocation = actualFolderLocation;
		this.cmdLanguage = cmdLanguage;
		this.thread = thread;
		this.tesseractVersion = tesseractVersion;
		this.colorSwitch = colorSwitch;
		run();
	}

	public void run() throws DCMAApplicationException {
		List<String> cmdListLocal = new ArrayList<String>();
		cmdListLocal.addAll(this.cmdList);
		if (fileName.contains(" ")) {
			LOGGER.error(" Space found in the name of image:" + fileName + ".So it acnnot be processed");
			throw new DCMAApplicationException(" Space found in the name of image:" + fileName + ".So it acnnot be processed");
		}

		String tesseractBasePath = "";
		try {
			ApplicationConfigProperties app = new ApplicationConfigProperties();
			tesseractBasePath = app.getProperty(tesseractVersion);
		} catch (IOException ioe) {
			LOGGER.error("Tesseract Base path not configured." + ioe, ioe);
			throw new DCMAApplicationException("Tesseract Base path not configured.", ioe);
		}

		if (tesseractBasePath == null) {
			LOGGER.error("Tesseract Base path not configured.");
			throw new DCMAApplicationException("Tesseract Base path not configured.");
		}

		// process each image file to generate HOCR files
		String targetDirectoryName = fileName.substring(0, fileName.indexOf('.'));
		String targetHOCR = "";
		String oldFileName = "";
		String ocrInputFileName = "";
		List<Document> xmlDocuments = batch.getDocuments().getDocument();
		String pageId = "";
		for (int i = 0; i < xmlDocuments.size(); i++) {
			Document document = (Document) xmlDocuments.get(i);
			List<Page> listOfPages = document.getPages().getPage();
			for (int j = 0; j < listOfPages.size(); j++) {
				Page page = listOfPages.get(j);
				String sImageFile;
				if ("ON".equals(colorSwitch)) {
					sImageFile = page.getOCRInputFileName();
				} else {
					sImageFile = page.getNewFileName();
				}

				if (fileName.equalsIgnoreCase(sImageFile)) {
					oldFileName = page.getOldFileName();
					ocrInputFileName = page.getOCRInputFileName();
					pageId = page.getIdentifier();
				}
			}
		}
		try {
			FileNameFormatter fileFormatter = new FileNameFormatter();
			if (tesseractVersion.equalsIgnoreCase(TesseractVersionProperty.TESSERACT_VERSION_3.getPropertyKey())) {
				/*
				 * # Changed due to Tesseract 3.0: # Sending an empty string in the extension part which Tesseract automatically does #
				 * to the name we give to the file
				 */
				targetHOCR = fileFormatter.getHocrFileName(String.valueOf(batchInstanceID), oldFileName, fileName, ocrInputFileName,
						"", pageId, false);
			} else {
				targetHOCR = fileFormatter.getHocrFileName(String.valueOf(batchInstanceID), oldFileName, fileName, ocrInputFileName,
						".html", pageId, false);
			}

		} catch (Exception e1) {
			LOGGER.error("Exception retrieving the name of HOCR file" + e1.getMessage(), e1);
			throw new DCMAApplicationException("Exception retrieving the name of HOCR file" + e1.getMessage(), e1);
		}
		LOGGER.info("image file name is: " + fileName);
		LOGGER.info("Target directory name is: " + targetDirectoryName);
		LOGGER.info("Target HOCR name is: " + targetHOCR);
		try {
			if (!cmdListLocal.isEmpty()) {
				if (tesseractVersion.equalsIgnoreCase(TesseractVersionProperty.TESSERACT_VERSION_3.getPropertyKey())) {
					if (OSUtil.isWindows()) {
						cmdListLocal.add("\"" + actualFolderLocation + File.separator + fileName + "\"");
						cmdListLocal.add("\"" + actualFolderLocation + File.separator + targetHOCR + "\"");
						cmdListLocal.add("-l");
						cmdListLocal.add(cmdLanguage);
						cmdListLocal.add("+hocr.txt");
					} else {
						cmdListLocal.add(actualFolderLocation + File.separator + fileName);
						cmdListLocal.add(actualFolderLocation + File.separator + targetHOCR);
						cmdListLocal.add("-l");
						cmdListLocal.add(cmdLanguage);
						cmdListLocal.add("+hocr.txt");
					}
				} else {
					cmdListLocal.add("\"" + actualFolderLocation + File.separator + fileName + "\"");
					cmdListLocal.add(cmdLanguage);
					cmdListLocal.add(">");
					cmdListLocal.add("\"" + actualFolderLocation + File.separator + targetHOCR + "\"");
				}

				String[] cmds = new String[cmdListLocal.size()];
				for (int i = 0; i < cmdListLocal.size(); i++) {
					if (cmdListLocal.get(i).contains("cmd")) {
						LOGGER.info("inside cmd");
						cmds[i] = cmdListLocal.get(i);
					} else if (cmdListLocal.get(i).contains("/c")) {
						LOGGER.info("inside /c");
						cmds[i] = cmdListLocal.get(i);
					} else {
						LOGGER.info("inside Tesseract");
						cmds[i] = cmdListLocal.get(i);
					}
				}

				LOGGER.info("command formed is :");
				for (int i = 0; i < cmds.length; i++) {
					LOGGER.info(cmds[i]);
				}
				LOGGER.info("command formed Ends.");

				thread.add(new ProcessExecutor(cmds, new File(tesseractBasePath)));
			}
		} catch (Exception e) {
			LOGGER.error("Exception while generating HOCR for image" + fileName + e.getMessage());
			throw new DCMAApplicationException("Exception while generating HOCR for image" + fileName + e.getMessage(), e);
		}
		if (tesseractVersion.equalsIgnoreCase(TesseractVersionProperty.TESSERACT_VERSION_3.getPropertyKey())) {
			// Appending the .html extension to the file(since Tesseract 3.0 appends it automatically)
			this.targetHOCR = targetHOCR + ".html";
		} else {
			this.targetHOCR = targetHOCR;
		}
	}

	public String getTargetHOCR() {
		return targetHOCR;
	}

	public String getFileName() {
		return fileName;
	}

	public String getActualFolderLocation() {
		return actualFolderLocation;
	}
}
