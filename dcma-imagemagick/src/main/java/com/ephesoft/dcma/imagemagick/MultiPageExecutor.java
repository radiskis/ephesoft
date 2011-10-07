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

	public MultiPageExecutor(String[] cmds, File file, BatchInstanceThread batchInstanceThread, String[] pages) {
		this.pages = new String[pages.length];
		this.pages = pages.clone();
		batchInstanceThread.add(new ProcessExecutor(cmds, file));
	}

	/**
	 * This method creates multi page pdf using ghost script command.
	 * 
	 * @param batchInstanceThread {@BatchInstanceThread}
	 * @param pages11 {@link String[]}
	 * @param pdfCompression {@link String}
	 * @param pdfQuality {@link String}
	 * @param coloredImage {@link String}
	 * @param convertToTif (@link Boolean}
	 */
	public MultiPageExecutor(BatchInstanceThread batchInstanceThread, final String[] pages11, final String pdfCompression,
			final String pdfQuality, final String multipageTifDevice, final boolean convertToTif) {
		if (pages11 != null && pages11.length > 0) {
			this.pages = new String[pages11.length];
			this.pages = pages11.clone();
			batchInstanceThread.add(new AbstractRunnable() {

				@Override
				public void run() {
					String ghostScriptPath = System.getenv(IImageMagickCommonConstants.GHOSTSCRIPT_ENV_VARIABLE);
					int noOfPages = pages.length;
					String string = null;
					int ITERATOR_COUNT = 500;
					int index = 0;
					int counter = 1;
					boolean isFirst = true;
					int lastIndexOf = pages[0].lastIndexOf(File.separator);
					String srcDir = null;
					if (lastIndexOf > 0) {
						srcDir = pages[0].substring(0, lastIndexOf);
						String tempResult = null;
						String tempFile = null;
						boolean isLastPage = false;
						while (!isLastPage) {
							LOGGER.info("creating ghostscript command for multipage pdf creation.");
							StringBuffer buffer = new StringBuffer();
							int maxNoOfPages = counter * ITERATOR_COUNT;
							string = DOUBLE_QUOTES + ghostScriptPath + File.separator + GHOSTSCRIPT_COMMAND + DOUBLE_QUOTES + SPACE;
							buffer.append(string);
							buffer.append(GHOST_SCRIPT_COMMAND_PARAMETERS);
							string = PDF_DEVICE + SPACE;
							buffer.append(string);
							buffer.append(GHOST_SCRIPT_COMMAND_OUTPUT_PARAMETERS);
							if (maxNoOfPages >= noOfPages - 1) {
								string = DOUBLE_QUOTES + pages[noOfPages - 1] + DOUBLE_QUOTES + SPACE;
								buffer.append(string);
								isLastPage = true;
							} else {
								tempFile = srcDir + File.separator + TEMP_FILE_NAME + counter + FileType.PDF.getExtensionWithDot();
								string = DOUBLE_QUOTES + tempFile + DOUBLE_QUOTES + SPACE;
								buffer.append(string);
							}
							if (!isFirst) {
								string = DOUBLE_QUOTES + tempResult + DOUBLE_QUOTES + SPACE;
								buffer.append(string);
							}
							isFirst = false;
							if (tempFile != null) {
								tempResult = tempFile;
							}
							counter++;
							for (; index < noOfPages - 1 && index < maxNoOfPages; index++) {
								string = DOUBLE_QUOTES + pages[index] + DOUBLE_QUOTES + SPACE;
								buffer.append(string);
							}
							String command = buffer.toString();
							LOGGER.info("Command for multi page pdf creation : " + command);
							try {
								Process process = Runtime.getRuntime().exec(command, null, new File(ghostScriptPath));
								int exitValue = process.waitFor();
								if (exitValue != 0) {
									LOGGER.error("Process exited with an invalid exit value : " + exitValue);
									setDcmaApplicationException(new DCMAApplicationException(
											"Error occured while running command for multipage tiff creation."));
								}
							} catch (IOException e) {
								LOGGER.error("Error occured while running command for multipage tiff creation.", e);
								setDcmaApplicationException(new DCMAApplicationException(
										"Error occured while running command for multipage tiff creation."));
							} catch (InterruptedException e) {
								LOGGER.error("Error occured while running command for multipage tiff creation.", e);
								setDcmaApplicationException(new DCMAApplicationException(
										"Error occured while running command for multipage tiff creation.", e));
							}
						}
					} else {
						LOGGER.error("Error occured while creating command for multipage tiff creation.");
						setDcmaApplicationException(new DCMAApplicationException(
								"Error occured while creating command for multipage tiff creation."));
					}
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

	public String[] getPages() {
		return pages;
	}
}
