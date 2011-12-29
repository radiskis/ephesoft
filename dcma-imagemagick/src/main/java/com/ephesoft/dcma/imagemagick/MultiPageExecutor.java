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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.AbstractRunnable;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.core.threadpool.ProcessExecutor;
import com.ephesoft.dcma.imagemagick.constant.ImageMagicKConstants;
import com.ephesoft.dcma.util.OSUtil;

/**
 * This class is used for creating multipage tiff and pdf.
 * 
 * @author Ephesoft
 */
public class MultiPageExecutor implements ImageMagicKConstants {

	private String[] pages;

	/**
	 * Logger instance for logging using slf4j for logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MultiPageExecutor.class);

	public static String GS_PDF_EXECUTOR_PATH = "GS_PDF_EXECUTOR_PATH";

	public MultiPageExecutor(String[] cmds, File file, BatchInstanceThread batchInstanceThread, String[] pages) {
		this.pages = new String[pages.length];
		this.pages = pages.clone();
		batchInstanceThread.add(new ProcessExecutor(cmds, file));
	}

	/**
	 * This method creates multi page pdf using ghost script command.
	 * 
	 * @param batchInstanceThread
	 * @param pages11
	 * @param gsCmdParam
	 * @param ghostScriptCommand
	 * @param maxFilesProcessedPerLoop
	 */
	public MultiPageExecutor(BatchInstanceThread batchInstanceThread, final String[] pages11, final String gsCmdParam,
			final String ghostScriptCommand, final Integer maxFilesProcessedPerLoop) {
		if (pages11 != null && pages11.length > 0) {
			this.pages = new String[pages11.length];
			this.pages = pages11.clone();
			batchInstanceThread.add(new AbstractRunnable() {

				@Override
				public void run() {
					String ghostScriptPath = System.getenv(IImageMagickCommonConstants.GHOSTSCRIPT_ENV_VARIABLE);
					List<String> ghostScriptCommandList = new ArrayList<String>();
					File systemFileFolderPath = new File(pages[0]);
					String systemFolderPath = systemFileFolderPath.getParent();
					String outputFileName = "";
					String[] gsCmdParams = gsCmdParam.split(SPACE);
					int noOfPages = pages.length;
					int currPageNo = 0;
					int counter = 1;
					File fileToBeDeleted = null;
					String prevTempFilePath = null;
					String nextTempFilePath = null;
					String tempFilePath = pages[noOfPages - 1].substring(0, pages[noOfPages - 1].lastIndexOf('.') == -1
							? pages[noOfPages].length() : pages[noOfPages - 1].lastIndexOf('.'));
					boolean isLastPage = false;
					while (!isLastPage) {
						ghostScriptCommandList.clear();
						LOGGER.info("creating ghostscript command for multipage pdf creation.");
						int maxNoOfPages = counter * maxFilesProcessedPerLoop;
						for (String param : gsCmdParams) {
							ghostScriptCommandList.add(param);
						}
						if (maxNoOfPages >= noOfPages - 1) {
							if (OSUtil.isWindows()) {
								outputFileName = pages[noOfPages - 1].substring(pages[noOfPages - 1].lastIndexOf(File.separator) + 1);
								ghostScriptCommandList.add(GHOST_SCRIPT_COMMAND_OUTPUT_FILE_PARAM + outputFileName);
							} else if (OSUtil.isUnix()) {
								ghostScriptCommandList.add(GHOST_SCRIPT_COMMAND_OUTPUT_FILE_PARAM + pages[noOfPages - 1]);
							}

							isLastPage = true;
						} else {
							nextTempFilePath = tempFilePath + '_' + counter + FileType.PDF.getExtensionWithDot();
							outputFileName = nextTempFilePath.substring(nextTempFilePath.lastIndexOf(File.separator) + 1);
							if (OSUtil.isWindows()) {
								ghostScriptCommandList.add(DOUBLE_QUOTES + GHOST_SCRIPT_COMMAND_OUTPUT_FILE_PARAM + outputFileName
										+ DOUBLE_QUOTES);
							} else if (OSUtil.isUnix()) {
								ghostScriptCommandList.add(GHOST_SCRIPT_COMMAND_OUTPUT_FILE_PARAM + nextTempFilePath);
							}
						}
						if (prevTempFilePath != null) {
							ghostScriptCommandList.add(prevTempFilePath);
							fileToBeDeleted = new File(prevTempFilePath);
						}
						prevTempFilePath = nextTempFilePath;
						counter++;
						for (; currPageNo < noOfPages - 1 && currPageNo < maxNoOfPages; currPageNo++) {
							if (OSUtil.isWindows()) {
								ghostScriptCommandList.add(DOUBLE_QUOTES
										+ pages[currPageNo].substring(pages[currPageNo].lastIndexOf(File.separator) + 1)
										+ DOUBLE_QUOTES);

							} else if (OSUtil.isUnix()) {
								ghostScriptCommandList.add(pages[currPageNo]);
							}
						}
						List<String> commandList = new ArrayList<String>();
						if (OSUtil.isWindows()) {
							String absoluteFilePath = systemFolderPath + File.separator + outputFileName;
							String absoluteGhostScriptParametersFilePath = absoluteFilePath.substring(0, absoluteFilePath
									.lastIndexOf("."))
									+ FileType.SER.getExtensionWithDot();
							File ghostScriptCommandParametersFile = new File(absoluteGhostScriptParametersFilePath);
							writeGhosScriptParametersToFile(ghostScriptCommandParametersFile, ghostScriptCommandList);
							makeCommandForWindows(commandList, ghostScriptPath, systemFolderPath,
									absoluteGhostScriptParametersFilePath);
						} else if (OSUtil.isUnix()) {
							commandList.add(ghostScriptPath + File.separator + ghostScriptCommand);
							commandList.addAll(ghostScriptCommandList);
						}

						LOGGER.info("Command for multi page pdf creation : " + commandList);
						String[] cmds = (String[]) commandList.toArray(new String[commandList.size()]);
						try {
							Process process = Runtime.getRuntime().exec(cmds);
							InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
							BufferedReader input = new BufferedReader(inputStreamReader);
							String line = null;
							do {
								line = input.readLine();
								LOGGER.info(line);
							} while (line != null);
							int exitValue = process.waitFor();
							if (exitValue != 0) {
								LOGGER.error("Process exited with an invalid exit value : " + exitValue);
								setDcmaApplicationException(new DCMAApplicationException(
										"Error occured while running command for multipage pdf creation."));
							}
							if (fileToBeDeleted != null && fileToBeDeleted.exists()) {
								LOGGER.info("Deleting temporary file : " + fileToBeDeleted.getAbsolutePath());
								fileToBeDeleted.delete();
							}
						} catch (IOException e) {
							LOGGER.error("Error occured while running command for multipage pdf creation." + e.getMessage(), e);
							setDcmaApplicationException(new DCMAApplicationException(
									"Error occured while running command for multipage pdf creation." + e.getMessage(), e));
						} catch (InterruptedException e) {
							LOGGER.error("Error occured while running command for multipage pdf creation." + e.getMessage(), e);
							setDcmaApplicationException(new DCMAApplicationException(
									"Error occured while running command for multipage pdf creation." + e.getMessage(), e));
						} catch (SecurityException se) {
							LOGGER.error("Cannot delete the temporary file : " + fileToBeDeleted.getAbsolutePath() + se.getMessage(),
									se);
						}
					}
					if (fileToBeDeleted != null && fileToBeDeleted.exists()) {
						LOGGER.info("Deleting temporary file : " + fileToBeDeleted.getAbsolutePath());
						fileToBeDeleted.delete();
					}
				}

				private void writeGhosScriptParametersToFile(File file, List<String> ghostScriptCommandList) {
					LOGGER.info("Writing ghostscript parameters to :" + file.getAbsolutePath());
					StringBuffer ghostScriptParametersBuffer = new StringBuffer();
					for (String command : ghostScriptCommandList) {
						ghostScriptParametersBuffer.append(command + SPACE);
					}
					try {
						FileUtils.writeStringToFile(file, ghostScriptParametersBuffer.toString());
					} catch (IOException e) {
						LOGGER.error(e.getMessage(), e);
					}
				}

				private void makeCommandForWindows(List<String> commandList, String ghostScriptPath, String systemFolderPath,
						String ghostScriptParametersFileAbsolutePath) {
					LOGGER.info("Forming Command for Ghostscript Executor(Windows).");
					commandList.add(System.getenv(GS_PDF_EXECUTOR_PATH));
					commandList.add(DOUBLE_QUOTES + ghostScriptPath + File.separator + ghostScriptCommand + DOUBLE_QUOTES);
					commandList.add(DOUBLE_QUOTES + systemFolderPath + DOUBLE_QUOTES);
					commandList.add(DOUBLE_QUOTES + ghostScriptParametersFileAbsolutePath + DOUBLE_QUOTES);
				}
			});
		}
	}

	/**
	 * This method creates multipage tiff file from multipage pdf file using imagemagick.
	 * 
	 * @param batchInstanceThread {@link BatchInstanceThread}
	 * @param tifPages {@link String[]}
	 * @param tifCompression {@link String}
	 */
	public MultiPageExecutor(final BatchInstanceThread batchInstanceThread, final String[] tifPages, final String tifCompression) {
		if (tifPages != null && tifPages.length > 0) {
			this.pages = new String[tifPages.length];
			this.pages = tifPages.clone();
			batchInstanceThread.add(new AbstractRunnable() {

				@Override
				public void run() {
					LOGGER.info("Adding command for multipage tiff creation from multipage pdf using imagemagick.");
					ConvertCmd convert = new ConvertCmd();
					IMOperation oper = new IMOperation();
					oper.addImage(pages.length - 1);
					oper.adjoin();
					oper.compress(tifCompression);
					oper.addImage();
					try {
						LOGGER.info("Running command for multipage tiff creation from multipage pdf using imagemagick.");
						convert.run(oper, (Object[]) pages);
					} catch (Exception e) {
						LOGGER.error("Error occured while running command for multipage tiff creation.", e);
						setDcmaApplicationException(new DCMAApplicationException(
								"Error occured while running command for multipage tiff creation.", e));
					}
				}
			});
		}
	}

	/**
	 * This method creates multi page pdf using Image-Magick.
	 * 
	 * @param batchInstanceThread
	 * @param pages11
	 * @param pdfCompression
	 * @param pdfQuality
	 * @param coloredImage
	 */
	public MultiPageExecutor(BatchInstanceThread batchInstanceThread, final String[] pages11, final String pdfCompression,
			final String pdfQuality, final String coloredImage, final String pdfOptimizationSwitch) {
		this.pages = new String[pages11.length];
		this.pages = pages11.clone();
		batchInstanceThread.add(new AbstractRunnable() {

			@Override
			public void run() {
				LOGGER.info("Creating multipgae pdf using imagemagick....");
				IMOperation op = new IMOperation();
				op.addImage(pages.length - 1);
				if (pdfQuality != null) {
					LOGGER.info("Adding pdfQuality : " + pdfQuality);
					op.quality(new Double(pdfQuality));
				}

				if (coloredImage != null && ImageMagicKConstants.FALSE.equalsIgnoreCase(coloredImage)) {
					op.monochrome();
				}
				if (pdfCompression != null) {
					LOGGER.info("Adding pdfCompression : " + pdfCompression);
					op.compress(pdfCompression);
				}

				if (pdfOptimizationSwitch != null && pdfOptimizationSwitch.equalsIgnoreCase(ImageMagicKConstants.ON_SWITCH)) {
					LOGGER.info("Adding pdfOptimnisation.");
					op.type("optimize");
				}

				op.addImage();
				ConvertCmd convert = new ConvertCmd();
				try {
					convert.run(op, (Object[]) pages);
				} catch (Exception e) {
					LOGGER.error("Error occured while running command for multipage pdf creation." + e.getMessage(), e);
					setDcmaApplicationException(new DCMAApplicationException(
							"Error occured while running command for multipage pdf creation." + e.getMessage(), e));
				}
			}
		});
	}

	public String[] getPages() {
		return pages;
	}
}
