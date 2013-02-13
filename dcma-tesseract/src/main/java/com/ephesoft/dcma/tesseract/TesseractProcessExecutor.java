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

package com.ephesoft.dcma.tesseract;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.core.TesseractVersionProperty;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.core.threadpool.ProcessExecutor;
import com.ephesoft.dcma.tesseract.constant.TesseractConstants;
import com.ephesoft.dcma.util.CustomFileFilter;
import com.ephesoft.dcma.util.FileNameFormatter;
import com.ephesoft.dcma.util.OSUtil;

/**
 * This is executor class for tesseract plugin.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.TesseractVersionProperty
 */
public class TesseractProcessExecutor {

	/**
	 * TESSERACT_BASE_PATH_NOT_CONFIGURED String.
	 */
	final private static String TESSERACT_BASE_PATH_NOT_CONFIGURED = "Tesseract Base path not configured.";

	/**
	 * TESSERACT_EXECUTOR_PATH String.
	 */
	private final static String TESSERACT_EXECUTOR_PATH = "TESSERACT_EXECUTOR_PATH";

	/**
	 *  Constant for environment variable TESSERACT_HOME which contains the path to tesseract ocr engine
	 */
	private final static String TESSERACT_OCR_PATH = "TESSERACT_HOME";
	
	/**
	 * The name of the file.
	 */
	final private String fileName;

	/**
	 * batch Batch.
	 */
	final private Batch batch;

	/**
	 * batchInstanceID String.
	 */
	final private String batchInstanceID;

	/**
	 * cmdList List<String>.
	 */
	final private List<String> cmdList;

	/**
	 * actualFolderLocation String.
	 */
	final private String actualFolderLocation;

	/**
	 * cmdLanguage String.
	 */
	final private String cmdLanguage;

	/**
	 * thread BatchInstanceThread.
	 */
	final private BatchInstanceThread thread;

	/**
	 * targetHOCR String.
	 */
	private String targetHOCR;

	/**
	 * tesseractVersion String.
	 */
	final private String tesseractVersion;

	/**
	 * colorSwitch String.
	 */
	final private String colorSwitch;

	/**
	 * unixCmd String.
	 */
	final private String unixCmd;

	/**
	 * windowsCmd String.
	 */
	final private String windowsCmd;

	/**
	 * overwriteHOCR String.
	 */
	final private String overwriteHOCR;

	/**
	 * cmdParams String.
	 */
	final private String cmdParams;

	/**
	 * outputFolderLocation String.
	 */
	final private String outputFolderLocation;

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(TesseractProcessExecutor.class);

	/**
	 * Constructor.
	 * 
	 * @param fileName String
	 * @param batch Batch
	 * @param batchInstanceID String
	 * @param actualFolderLocation String
	 * @param cmdLanguage String
	 * @param thread BatchInstanceThread
	 * @param tesseractVersion String
	 * @param colorSwitch String
	 * @param windowsCmd String
	 * @param unixCmd String
	 * @throws DCMAApplicationException in case of error
	 */
	public TesseractProcessExecutor(String fileName, Batch batch, String batchInstanceID, String actualFolderLocation,
			String cmdLanguage, BatchInstanceThread thread, String tesseractVersion, String colorSwitch, String windowsCmd,
			String unixCmd) throws DCMAApplicationException {
		this.fileName = fileName;
		this.batch = batch;
		this.batchInstanceID = batchInstanceID;
		this.cmdList = new ArrayList<String>();
		this.actualFolderLocation = actualFolderLocation;
		this.cmdLanguage = cmdLanguage;
		this.thread = thread;
		this.tesseractVersion = tesseractVersion;
		this.colorSwitch = colorSwitch;
		this.windowsCmd = windowsCmd;
		this.unixCmd = unixCmd;
		this.overwriteHOCR = TesseractConstants.TRUE;
		this.cmdParams = TesseractConstants.EMPTY;
		this.outputFolderLocation = actualFolderLocation;
		run();
	}

	/**
	 * Constructor.
	 * 
	 * @param fileName String
	 * @param batch Batch
	 * @param batchInstanceID String
	 * @param actualFolderLocation String
	 * @param cmdLanguage String
	 * @param thread BatchInstanceThread
	 * @param tesseractVersion String
	 * @param colorSwitch String
	 * @param windowsCmd String
	 * @param unixCmd String
	 * @param overwriteHOCR String
	 * @param cmdParams String
	 * @throws DCMAApplicationException in case of error
	 */
	public TesseractProcessExecutor(String fileName, Batch batch, String batchInstanceID, String actualFolderLocation,
			String cmdLanguage, BatchInstanceThread thread, String tesseractVersion, String colorSwitch, String windowsCmd,
			String unixCmd, String overwriteHOCR, String cmdParams) throws DCMAApplicationException {
		this.fileName = fileName;
		this.batch = batch;
		this.batchInstanceID = batchInstanceID;
		this.cmdList = new ArrayList<String>();
		this.actualFolderLocation = actualFolderLocation;
		this.cmdLanguage = cmdLanguage;
		this.thread = thread;
		this.tesseractVersion = tesseractVersion;
		this.colorSwitch = colorSwitch;
		this.windowsCmd = windowsCmd;
		this.unixCmd = unixCmd;
		this.overwriteHOCR = overwriteHOCR;
		this.cmdParams = cmdParams;
		this.outputFolderLocation = actualFolderLocation;
		run();
	}

	/**
	 * Constructor.
	 * 
	 * @param fileName String
	 * @param actualFolderLocation String
	 * @param cmdLanguage String
	 * @param thread BatchInstanceThread
	 * @param tesseractVersion String
	 * @param colorSwitch String
	 * @param windowsCmd String
	 * @param unixCmd String
	 * @param cmdParams String
	 * @param outputFolderLocation String
	 * @throws DCMAApplicationException in case of error
	 */
	public TesseractProcessExecutor(String fileName, String actualFolderLocation, String cmdLanguage, BatchInstanceThread thread,
			String tesseractVersion, String colorSwitch, String windowsCmd, String unixCmd, String cmdParams,
			String outputFolderLocation) throws DCMAApplicationException {
		this.fileName = fileName;
		this.batch = null;
		this.batchInstanceID = null;
		this.cmdList = new ArrayList<String>();
		this.actualFolderLocation = actualFolderLocation;
		this.cmdLanguage = cmdLanguage;
		this.thread = thread;
		this.tesseractVersion = tesseractVersion;
		this.colorSwitch = colorSwitch;
		this.windowsCmd = windowsCmd;
		this.unixCmd = unixCmd;
		this.overwriteHOCR = TesseractConstants.TRUE;
		this.cmdParams = cmdParams;
		this.outputFolderLocation = outputFolderLocation;
		createOCR();
	}

	/**
	 * Run method.
	 * 
	 * @throws DCMAApplicationException in case of error
	 */
	public final void run() throws DCMAApplicationException {
		List<String> cmdListLocal = new ArrayList<String>();
		cmdListLocal.addAll(this.cmdList);
		if (fileName.contains(TesseractConstants.SPACE)) {
			LOGGER.error(" Space found in the name of image :" + fileName + ". So it acnnot be processed");
			throw new DCMAApplicationException(" Space found in the name of image:" + fileName + ".So it acnnot be processed");
		}
		String tesseractBasePath = getTesseractBasePath();
		if (tesseractBasePath == null) {
			LOGGER.error(TESSERACT_BASE_PATH_NOT_CONFIGURED);
			throw new DCMAApplicationException(TESSERACT_BASE_PATH_NOT_CONFIGURED);
		}

		// process each image file to generate HOCR files
		String targetDirectoryName = fileName.substring(0, fileName.indexOf('.'));
		String targetHOCR = TesseractConstants.EMPTY;
		String oldFileName = TesseractConstants.EMPTY;
		String ocrInputFileName = TesseractConstants.EMPTY;
		List<Document> xmlDocuments = batch.getDocuments().getDocument();
		String pageId = TesseractConstants.EMPTY;
		for (int i = 0; i < xmlDocuments.size(); i++) {
			Document document = (Document) xmlDocuments.get(i);
			List<Page> listOfPages = document.getPages().getPage();
			for (int j = 0; j < listOfPages.size(); j++) {
				Page page = listOfPages.get(j);
				String sImageFile;
				if (TesseractConstants.ON.equals(colorSwitch)) {
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
						TesseractConstants.EMPTY, pageId, false);
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

		boolean isHOCRExists = false;

		LOGGER.info("Overwrite HOCR is :" + this.overwriteHOCR);

		if (!"true".equalsIgnoreCase(this.overwriteHOCR)) {
			File fPageFolder = new File(actualFolderLocation);
			String[] listOfHtmlFiles = fPageFolder.list(new CustomFileFilter(false, FileType.HTML.getExtensionWithDot()));
			isHOCRExists = checkHocrExists(targetHOCR, listOfHtmlFiles);
		}

		LOGGER.info("Is HOCR existing for page" + pageId + "is" + isHOCRExists);

		if (!isHOCRExists) {
			validateHOCR(cmdListLocal, tesseractBasePath, targetHOCR);
		}
		if (tesseractVersion.equalsIgnoreCase(TesseractVersionProperty.TESSERACT_VERSION_3.getPropertyKey())) {
			// Appending the .html extension to the file(since Tesseract 3.0 appends it automatically)
			this.targetHOCR = targetHOCR + ".html";
		} else {
			this.targetHOCR = targetHOCR;
		}

	}

	private void validateHOCR(List<String> cmdListLocal, String tesseractBasePath, String targetHOCR) throws DCMAApplicationException {
		try {
			if (tesseractVersion.equalsIgnoreCase(TesseractVersionProperty.TESSERACT_VERSION_3.getPropertyKey())) {
				creatingTesseractVersion3Command(cmdListLocal, targetHOCR, cmdParams);
			} else {
				creatingTesseractVersion2Command(cmdListLocal, targetHOCR);
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
			if (OSUtil.isUnix()) {
				thread.add(new ProcessExecutor(cmds, new File(tesseractBasePath)));
			} else if (OSUtil.isWindows()) {
				thread.add(new ProcessExecutor(cmds, null));
			}
		} catch (Exception e) {
			LOGGER.error("Exception while generating HOCR for image" + fileName + e.getMessage());
			throw new DCMAApplicationException("Exception while generating HOCR for image" + fileName + e.getMessage(), e);
		}
	}

	/**
	 * This method generates the HOCR files.
	 * 
	 * @throws DCMAApplicationException in case of error
	 */
	public final void createOCR() throws DCMAApplicationException {
		List<String> cmdListLocal = new ArrayList<String>();
		cmdListLocal.addAll(this.cmdList);
		if (fileName.contains(TesseractConstants.SPACE)) {
			LOGGER.error(" Space found in the name of image:" + fileName + ".So it acnnot be processed");
			throw new DCMAApplicationException(" Space found in the name of image:" + fileName + ".So it acnnot be processed");
		}
		String tesseractBasePath = getTesseractBasePath();
		if (tesseractBasePath == null) {
			LOGGER.error(TESSERACT_BASE_PATH_NOT_CONFIGURED);
			throw new DCMAApplicationException(TESSERACT_BASE_PATH_NOT_CONFIGURED);
		}

		String targetHOCR = TesseractConstants.EMPTY;
		targetHOCR = fileName.substring(0, fileName.lastIndexOf(TesseractConstants.DOT));

		LOGGER.info("image file name is: " + fileName);
		LOGGER.info("Target HOCR name is: " + targetHOCR);

		LOGGER.info("Overwrite HOCR is :" + this.overwriteHOCR);

		validateHOCR(cmdListLocal, tesseractBasePath, targetHOCR);

		if (tesseractVersion.equalsIgnoreCase(TesseractVersionProperty.TESSERACT_VERSION_3.getPropertyKey())) {
			// Appending the .html extension to the file(since Tesseract 3.0 appends it automatically)
			this.targetHOCR = targetHOCR + FileType.HTML.getExtensionWithDot();
		} else {
			this.targetHOCR = targetHOCR;
		}

	}

	private void creatingTesseractVersion2Command(List<String> cmdListLocal, String targetHOCR) throws DCMAApplicationException {
		cmdListLocal.add(TesseractConstants.QUOTE + System.getenv(TESSERACT_EXECUTOR_PATH) + File.separator + "TesseractExecutor.exe"
				+ TesseractConstants.QUOTE);
		cmdListLocal.add(TesseractConstants.QUOTE + getTesseractBasePath() + File.separator + windowsCmd + TesseractConstants.QUOTE);
		cmdListLocal.add(TesseractConstants.QUOTE + actualFolderLocation + File.separator + fileName + TesseractConstants.QUOTE);
		cmdListLocal.add(cmdLanguage);
		cmdListLocal.add(TesseractConstants.GREATER_THAN_SIGN);
		cmdListLocal.add(TesseractConstants.QUOTE + outputFolderLocation + File.separator + targetHOCR + TesseractConstants.QUOTE);
	}

	private void creatingTesseractVersion3Command(List<String> cmdListLocal, String targetHOCR, String cmdParams)
			throws DCMAApplicationException {
		if (OSUtil.isWindows()) {
			cmdListLocal.add(TesseractConstants.QUOTE + System.getenv(TESSERACT_EXECUTOR_PATH) + File.separator
					+ "TesseractExecutor.exe" + TesseractConstants.QUOTE);
			cmdListLocal.add(TesseractConstants.QUOTE + getTesseractBasePath() + File.separator + windowsCmd
					+ TesseractConstants.QUOTE);
			cmdListLocal.add(TesseractConstants.QUOTE + actualFolderLocation + File.separator + fileName + TesseractConstants.QUOTE);
			cmdListLocal.add(TesseractConstants.QUOTE + outputFolderLocation + File.separator + targetHOCR + TesseractConstants.QUOTE);
			cmdListLocal.add("\"-l");
			cmdListLocal.add(cmdLanguage);
			if (cmdParams != null && !cmdParams.trim().isEmpty()) {
				String cmdParamsArray[] = cmdParams.split(TesseractConstants.SPACE);
				for (String param : cmdParamsArray) {
					cmdListLocal.add(param);
				}
			}
			cmdListLocal.add(TesseractConstants.QUOTE);
			cmdListLocal.add(TesseractConstants.PLUS + TesseractConstants.QUOTE + getTesseractBasePath() + File.separator
					+ "hocr.txt\"");
		} else {
			cmdListLocal.add(unixCmd);
			cmdListLocal.add(actualFolderLocation + File.separator + fileName);
			cmdListLocal.add(outputFolderLocation + File.separator + targetHOCR);
			cmdListLocal.add("-l");
			cmdListLocal.add(cmdLanguage);
			if (cmdParams != null && !cmdParams.trim().isEmpty()) {
				String cmdParamsArray[] = cmdParams.split(TesseractConstants.SPACE);
				for (String param : cmdParamsArray) {
					cmdListLocal.add(param);
				}
			}
			cmdListLocal.add("+hocr.txt");
		}
	}

	private String getTesseractBasePath() throws DCMAApplicationException {

		// Getting the tesseract ocr directory from environment variables.
		String tesseractBasePath = System.getenv(TESSERACT_OCR_PATH);
		if (tesseractBasePath == null) {
			LOGGER.error(TESSERACT_BASE_PATH_NOT_CONFIGURED);
			throw new DCMAApplicationException(TESSERACT_BASE_PATH_NOT_CONFIGURED);
		}
		return tesseractBasePath;
	}

	/**
	 * To get Target HOCR.
	 * 
	 * @return String
	 */
	public String getTargetHOCR() {
		return targetHOCR;
	}

	/**
	 * To get File Name.
	 * 
	 * @return String
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * To get Actual Folder Location.
	 * 
	 * @return String
	 */
	public String getActualFolderLocation() {
		return actualFolderLocation;
	}

	private boolean checkHocrExists(String hocrName, final String[] listOfFiles) {
		boolean returnValue = false;
		String localHOCRFileName = hocrName + FileType.HTML.getExtensionWithDot();
		if (listOfFiles != null && listOfFiles.length > 0 && localHOCRFileName != null) {
			for (String eachFile : listOfFiles) {
				if (eachFile.equalsIgnoreCase(localHOCRFileName)) {
					returnValue = true;
				}
			}
		}
		return returnValue;
	}
}
