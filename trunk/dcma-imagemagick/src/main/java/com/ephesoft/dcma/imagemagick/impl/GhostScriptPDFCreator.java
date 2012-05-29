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

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.imagemagick.MultiPageExecutor;
import com.ephesoft.dcma.imagemagick.PdfOptimizer;
import com.ephesoft.dcma.imagemagick.constant.ImageMagicKConstants;
import com.ephesoft.dcma.util.OSUtil;

/**
 * This class is used to create the pdf file using Ghost Script.
 * 
 * @author Ephesoft
 */
public class GhostScriptPDFCreator {

	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * GhostScript command for windows.
	 */
	private transient String ghostScriptCommand;

	/**
	 * GhostScript command for unix.
	 */
	private transient String unixGhostScriptCommand;

	/**
	 * Maximum number of files to be processed by to be processed by one ghost script command.
	 */
	private transient String maxFilesProcessedPerGSCmd;
	
	/**
	 * Variable for logging.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(GhostScriptPDFCreator.class);
	
	/**
	 * Reference for batchSchemaService.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;
	

	/**
	 * @return the ghostScriptCommand
	 */
	public String getGhostScriptCommand() {
		return ghostScriptCommand;
	}

	/**
	 * @param ghostScriptCommand the ghostScriptCommand to set
	 */
	public void setGhostScriptCommand(String ghostScriptCommand) {
		this.ghostScriptCommand = ghostScriptCommand;
	}

	/**
	 * @return the unixGhostScriptCommand
	 */
	public String getUnixGhostScriptCommand() {
		return unixGhostScriptCommand;
	}

	/**
	 * @param unixGhostScriptCommand the unixGhostScriptCommand to set
	 */
	public void setUnixGhostScriptCommand(String unixGhostScriptCommand) {
		this.unixGhostScriptCommand = unixGhostScriptCommand;
	}

	/**
	 * @return the maxFilesProcessedPerGSCmd
	 */
	public String getMaxFilesProcessedPerGSCmd() {
		return maxFilesProcessedPerGSCmd;
	}

	/**
	 * @param maxFilesProcessedPerGSCmd the maxFilesProcessedPerGSCmd to set
	 */
	public void setMaxFilesProcessedPerGSCmd(String maxFilesProcessedPerGSCmd) {
		this.maxFilesProcessedPerGSCmd = maxFilesProcessedPerGSCmd;
	}
	
	/**
	 * Setting Plugin property service.
	 * @param pluginPropertiesService
	 */
	public void setPluginPropertiesService(PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}
	
	/**
	 * Setting batchSchemaService.
	 * @param batchSchemaService
	 */
	public void setBatchSchemaService(BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}
	
	/**
	 * API for getting the Ghost Script command for windows/linux.
	 * @return gsCommand
	 */
	private String getGSCommand() {
		String gsCommand = null;
		if (OSUtil.isWindows() && ghostScriptCommand != null) {
			gsCommand = ghostScriptCommand;
		} else if (OSUtil.isUnix() && unixGhostScriptCommand != null) {
			gsCommand = unixGhostScriptCommand;
		}
		return gsCommand;
	}
	
	/**
	 * API for getting the maximum file process at Ghost Script Command.
	 * @return maxFilesProcessedPerLoopInt
	 */
	private int getMaxFilesProcessedPerGScmdInt() {
		int maxFilesProcessedPerLoopInt = ImageMagicKConstants.MAX_FILES_PER_GS_COMMAND;
		if (maxFilesProcessedPerGSCmd != null) {
			try {
				maxFilesProcessedPerLoopInt = Integer.parseInt(maxFilesProcessedPerGSCmd);
			} catch (NumberFormatException nfe) {
				LOGGER
						.info("Incorrect value of max files to be processed by ghostscript specifeid in properties file.. setting it to its default value 75..");
				maxFilesProcessedPerLoopInt = ImageMagicKConstants.MAX_FILES_PER_GS_COMMAND;
			}
		}
		return maxFilesProcessedPerLoopInt;
	}
	
	/**
	 * API for creating the PDF file using ghost script.
	 * 
	 * @param batchXML
	 * @param batchInstanceID
	 * @param localFolder
	 * @param pages
	 * @param batchInstanceThread
	 * @param multiPageExecutors
	 * @param documentIdInt
	 * @throws DCMAApplicationException
	 */
	public void createPDFUsingGhostScript(String ghostscriptPdfParameters, String batchInstanceID, String localFolder, String[] pages,
			BatchInstanceThread batchInstanceThread, List<MultiPageExecutor> multiPageExecutors, String documentIdInt)
			throws DCMAApplicationException {

		String gsCommand = getGSCommand();
		if (gsCommand == null) {
			LOGGER.error("GhostScript command null.");
			throw new DCMABusinessException("GhostScript command null. Not specified in properties file.");
		}

		int maxFilesProcessedPerGSCmdInt = getMaxFilesProcessedPerGScmdInt();
		LOGGER.info("Adding command for multi page pdf execution");
		multiPageExecutors.add(new MultiPageExecutor(batchInstanceThread, pages, ghostscriptPdfParameters, gsCommand,
				maxFilesProcessedPerGSCmdInt, documentIdInt));
	}
	
	/**
	 * API for creating the optimized pdf.
	 * 
	 * @param batchInstanceIdentifier
	 * @param pdfOptimizationParams
	 * @param fTargetFileNamePdf
	 * @param pdfOptimizationThread
	 * @param tempFileName
	 * @param pdfOptimizer
	 * @throws DCMAApplicationException
	 */
	public void createOptimizedPdf(String batchInstanceIdentifier, String pdfOptimizationParams, File fTargetFileNamePdf,
			BatchInstanceThread pdfOptimizationThread, String tempFileName, List<PdfOptimizer> pdfOptimizer)
			throws DCMAApplicationException {
		String batchInstanceFolder = batchSchemaService.getLocalFolderLocation() + File.separator + batchInstanceIdentifier;
		if (null != fTargetFileNamePdf && null != tempFileName) {
			pdfOptimizer.add(new PdfOptimizer(batchInstanceFolder, tempFileName, pdfOptimizationThread, fTargetFileNamePdf.getName(),
					pdfOptimizationParams, getGhostScriptCommand()));
		} else {
			LOGGER.info("Cannot create command for pdf optimization. fTargetFileNamePdf=" + fTargetFileNamePdf + " tempFileName="
					+ tempFileName);
			throw new DCMAApplicationException("Cannot create command for pdf optimization. fTargetFileNamePdf=" + fTargetFileNamePdf
					+ " tempFileName=" + tempFileName);
		}
	}
	
	/**
	 * API for creating the optimized pdf.
	 * 
	 * @param batchInstanceIdentifier
	 * @param pdfOptimizationParams
	 * @param fTargetFileNamePdf
	 * @param pdfOptimizationThread
	 * @param tempFileName
	 * @param pdfOptimizer
	 * @throws DCMAApplicationException
	 */
	public void createOptimizedPdfAPI(String outputDir, String pdfOptimizationParams, File fTargetFileNamePdf,
			BatchInstanceThread pdfOptimizationThread, String tempFileName, List<PdfOptimizer> pdfOptimizer)
			throws DCMAApplicationException {
		if (null != fTargetFileNamePdf && null != tempFileName) {
			pdfOptimizer.add(new PdfOptimizer(outputDir, tempFileName, pdfOptimizationThread, fTargetFileNamePdf.getName(),
					pdfOptimizationParams, getGhostScriptCommand()));
		} else {
			LOGGER.info("Cannot create command for pdf optimization. fTargetFileNamePdf=" + fTargetFileNamePdf + " tempFileName="
					+ tempFileName);
			throw new DCMAApplicationException("Cannot create command for pdf optimization. fTargetFileNamePdf=" + fTargetFileNamePdf
					+ " tempFileName=" + tempFileName);
		}
	}

}
