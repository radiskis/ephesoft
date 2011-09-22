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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.AbstractRunnable;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.core.threadpool.ProcessExecutor;
import com.ephesoft.dcma.imagemagick.constant.ImageMagicKConstants;

/**
 * This class is used for creating multi page tifs and pdfs.
 * 
 * @author Ephesoft
 */
public class MultiPageExecutor {

	String[] pages;

	public static final String GHOSTSCRIPT_HOME = "GHOSTSCRIPT_HOME";

	public static final String GHOSTSCRIPT_COMMAND = "gswin32.exe";
	/**
	 * Logger instance for logging using slf4j for logging information.
	 */
	private static final Logger logger = LoggerFactory.getLogger(MultiPageExecutor.class);

	public MultiPageExecutor(String[] cmds, File file, BatchInstanceThread batchInstanceThread, String[] pages) {
		this.pages = new String[pages.length];
		this.pages = pages.clone();
		batchInstanceThread.add(new ProcessExecutor(cmds, file));
	}

	/**
	 * This method creates multi page tifs and pdfs using ghost script command.
	 * 
	 * @param batchInstanceThread {@BatchInstanceThread}
	 * @param pages11 {@link String[]}
	 * @param pdfCompression {@link String}
	 * @param pdfQuality {@link String}
	 * @param coloredImage {@link String}
	 * @param convertToTif (@link Boolean}
	 */
	public MultiPageExecutor(BatchInstanceThread batchInstanceThread, final String[] pages11, final String pdfCompression,
			final String pdfQuality, final String coloredImage, final boolean convertToTif) {
		if (pages11 != null && pages11.length > 0) {
			this.pages = new String[pages11.length];
			this.pages = pages11.clone();
			batchInstanceThread.add(new AbstractRunnable() {

				@Override
				public void run() {
					int index = 0;
					int counter = 1;
					String ghostScriptPath = System.getenv(GHOSTSCRIPT_HOME);
					int noOfPages = pages.length;
					String srcDir = pages[0].substring(0, pages[0].lastIndexOf(File.separator));
					String tempResult = null;
					String tempFile = null;
					boolean isFirst = true;
					while (index < noOfPages - 1) {
						StringBuffer buffer = new StringBuffer();
						int maxNoOfImages = counter * 100;
						buffer.append("\"" + ghostScriptPath + File.separator + GHOSTSCRIPT_COMMAND + "\" ");
						buffer.append(ImageMagicKConstants.GHOST_SCRIPT_COMMAND_PARAMETERS);
						if (convertToTif && maxNoOfImages >= noOfPages - 2) {
							buffer.append(ImageMagicKConstants.TIF_DEVICE + " ");
						} else {
							buffer.append(ImageMagicKConstants.PDF_DEVICE + " ");
						}
						buffer.append(ImageMagicKConstants.GHOST_SCRIPT_COMMAND_OUTPUT_PARAMETERS);
						if (maxNoOfImages >= noOfPages - 2) {
							buffer.append("\"" + pages[noOfPages - 1] + "\" ");
						} else {
							tempFile = srcDir + File.separator + ImageMagicKConstants.TEMP_FILE_NAME + counter
									+ ICommonConstants.EXTENSION_PDF;
							buffer.append("\"" + tempFile + "\" ");
						}
						if (!isFirst) {
							buffer.append("\"" + tempResult + "\" ");
						}
						isFirst = false;
						if (tempFile != null) {
							tempResult = tempFile;
						}
						for (; index < noOfPages - 1 && index <= maxNoOfImages; index++) {
							buffer.append("\"" + pages[index] + "\"" + " ");
						}
						counter++;
						String command = buffer.toString();

						try {
							Process process = Runtime.getRuntime().exec(command, null, new File(ghostScriptPath));
							int exitValue = process.waitFor();
							if (exitValue != 0) {
								logger.error("Process exited with an invalid exit value-" + exitValue);
							}
						} catch (Exception e) {
							logger.error("Error occured while running command for multipage tiff creation.", e);
							setDcmaApplicationException(new DCMAApplicationException(
									"Error occured while running command for multipage tiff creation."));
						}
					}
				}
			});
		}
	}

	public String[] getPages() {
		return pages;
	}
}
