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
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPluginConfiguration;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.core.threadpool.ProcessExecutor;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.imagemagick.constant.ImageMagicKConstants;
import com.ephesoft.dcma.util.OSUtil;

public class MultiPageToSinglePageConverter implements ICommonConstants, IImageMagickCommonConstants {

	private static final String MULTIPAGE_COMMAND_PARAMETER = "-%04d";

	private static final String EMPTY_STRING = "";

	private static final String QUOTES = "\"";

	private static final String SPACE = " ";
	private static final String GHOSTSCRIPT_EXECUTOR = "EphesoftExecutor.exe";
	private static final String IMAGEMAGICK_EXECUTOR = "EphesoftImageMagickExecutor.exe";

	@Autowired
	@Qualifier("batchClassPluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	public static final String IM4JAVA_TOOLPATH = "IM4JAVA_TOOLPATH";

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * This method converts a given tiff/pdf into tiff. In case of a multi-page pdf/tiff single tiff's for each page will be created.
	 * 
	 * @param imagePath
	 */
	public void convertPdfOrMultiPageTiffToTiff(BatchClass batchClass, File imagePath, File outputFilePath,
			BatchInstanceThread thread, Boolean allowPdfConversion) throws DCMAApplicationException {
		String inputParams = getInputPluginConfigForIM(batchClass);
		String outputParams = getOutputPluginConfigForIM(batchClass);
		
		String imageName = imagePath.getAbsolutePath();
		int indexOf = imageName.toLowerCase().indexOf(FileType.TIF.getExtensionWithDot());
		if (indexOf == -1) {
			indexOf = imageName.toLowerCase().indexOf(FileType.TIFF.getExtensionWithDot());
			if (indexOf == -1)
				if (allowPdfConversion) {
					indexOf = imageName.toLowerCase().indexOf(FileType.PDF.getExtensionWithDot());
				}
		}
		if (indexOf == -1) {
			logger.error("Unsupported file format");
			return;
		}
		
		convertPdfOrMultiPageTiffToTiffUsingIM(inputParams, imagePath, outputParams, outputFilePath, thread, indexOf);
	}
	
	public void convertPdfOrMultiPageTiffToTiffUsingIM(String inputParams,File imagePath, String outputParams, File outputFilePath, BatchInstanceThread thread, int indexOf)
	throws DCMAApplicationException {
		String repairImageMagickFileUtilityPath = System.getenv(REPAIR_IMAGE_MAGICK_FILES_ENV_VARIABLE);
		String imageName = imagePath.getAbsolutePath();
		
		String outputImagePath = imageName.substring(0, indexOf);
		String fileExtension = MULTIPAGE_COMMAND_PARAMETER + FileType.TIF.getExtensionWithDot();
		if (outputFilePath != null) {
			outputImagePath = outputFilePath.getAbsolutePath();
			if (outputImagePath.endsWith("\\") || outputImagePath.endsWith("/")) {
				fileExtension = "image" + fileExtension;
			}
		}
		try {
			String command = EMPTY_STRING;
			ArrayList<String> commandList = new ArrayList<String>();
			if (OSUtil.isWindows()) {
				commandList.add(repairImageMagickFileUtilityPath + File.separator + IMAGEMAGICK_EXECUTOR);
				createImageMagickCommandforWindows(commandList, inputParams, QUOTES + System.getenv(IM4JAVA_TOOLPATH) + File.separator
						+ "convert\"", QUOTES + imageName + QUOTES, outputParams, QUOTES + outputImagePath + fileExtension + QUOTES);

			} else {
				String outputImageName = outputImagePath + fileExtension; 
				commandList.add("convert");
				createCommandForLinux(commandList, inputParams, SPACE + imageName + SPACE, outputParams, SPACE + outputImageName + SPACE);
			}
			String[] cmds = (String[]) commandList.toArray(new String[commandList.size()]);

			if (thread != null) {
				logger.info("Adding generated command to thread pool. Command is : ");
				for (int ind = 0; ind < cmds.length; ind++) {
					logger.info(cmds[ind] + SPACE);
				}
				if (OSUtil.isWindows()) {
					thread.add(new ProcessExecutor(cmds, null));
				} else {
					thread.add(new ProcessExecutor(cmds, new File(System.getenv(IM4JAVA_TOOLPATH))));
				}
			} else {
				logger.error("Command " + command + " cannot be run");
				throw new DCMAApplicationException("Command " + command + " cannot be run");
			}

		} catch (Exception ex) {
			logger.error("Problem generating tiffs");
			throw new DCMAApplicationException("Problem generating tiffs", ex);
		}
	
	}

	private String createImageMagickCommandforWindows(ArrayList<String> commandList, String inputParams, String environment,
			String inputImageName, String outputParams, String outputImageName) {
		if (environment != null) {
			commandList.add(environment);
		}
		StringBuffer command = new StringBuffer(EMPTY_STRING);
		
		commandList.add(QUOTES + inputParams.trim() + QUOTES);
		commandList.add(inputImageName.trim());
		
		commandList.add(QUOTES + outputParams.trim() + QUOTES);
		commandList.add(outputImageName.trim());
		return command.toString();
	}

	private String createGhostScriptCommandforWindows(ArrayList<String> commandList, final String imageParams, final String environment,
			final String inputImageName, final String outputImageName) {

		StringBuffer command = new StringBuffer(EMPTY_STRING);

		String splitParams[] = imageParams.split(" ");
		StringBuffer inputParameterBuffer = new StringBuffer();
		for (int i = 0; i < splitParams.length; i++) {
			if (i == 0) {
				if (environment != null) {
					commandList.add(QUOTES + environment + splitParams[0] + QUOTES);
				}
			} else {
				inputParameterBuffer.append(splitParams[i] + " ");
			}
		}
		commandList.add(inputParameterBuffer.toString().trim());
		commandList.add("-sOutputFile=");
		commandList.add(outputImageName);
		commandList.add(inputImageName);
		return command.toString();
	}

	private String createCommandForLinux(ArrayList<String> commandList, final String inputParams, final String inputImageName,
			final String outputParams, final String outputImageName) {
		StringBuffer command = new StringBuffer(EMPTY_STRING);
		if(!inputParams.isEmpty()){
			String inputParamsArr[] = inputParams.split(SPACE);
			for (String string : inputParamsArr) {
				commandList.add(string.trim());
			}
		
		}
		commandList.add(inputImageName.trim());
		
		if(!outputParams.isEmpty()){
			String outputParamsArr[] = outputParams.split(SPACE);
			for (String string : outputParamsArr) {
				commandList.add(string.trim());
			}
		}
		commandList.add(outputImageName.trim());
		return command.toString();
	}

	public void convertPdfToSinglePageTiffs(BatchClass batchClass, File imagePath, File outputFilePath, BatchInstanceThread thread)
			throws DCMAApplicationException {
		
		String outputParams = getOutputPluginConfigForIM(batchClass);
		String gsParams = getPluginConfigForGS(batchClass);
		if (!OSUtil.isWindows()) {
			gsParams = getInputPluginConfigForIM(batchClass);
		}
		convertPdfToSinglePageTiffsUsingGSAPI(gsParams, imagePath, outputParams, outputFilePath, thread);
	}

	public void convertPdfToSinglePageTiffsUsingGSAPI(String inputParams, File imagePath, String outputParams, File outputFilePath, BatchInstanceThread thread)
			throws DCMAApplicationException {

		String repairGhostScriptFileUtilityPath = System.getenv(REPAIR_FILES_THROUGH_GHOSTSCIPT_ENV_VARIABLE);
		String imageName = imagePath.getAbsolutePath();
		int indexOf = imageName.toLowerCase().indexOf(FileType.PDF.getExtensionWithDot());
		if (indexOf == -1) {
			logger.info("No Pdf file format found");
			return;
		}

		String outputImagePath = imageName.substring(0, indexOf);
		String fileExtension = MULTIPAGE_COMMAND_PARAMETER + FileType.TIF.getExtensionWithDot();
		if (outputFilePath != null) {
			outputImagePath = outputFilePath.getAbsolutePath();
			if (outputImagePath.endsWith("\\") || outputImagePath.endsWith("/")) {
				fileExtension = "image" + fileExtension;
			}
		}
		try {
			String command = EMPTY_STRING;
			ArrayList<String> commandList = new ArrayList<String>();
			if (OSUtil.isWindows()) {
				commandList.add(repairGhostScriptFileUtilityPath + File.separator + GHOSTSCRIPT_EXECUTOR);
				createGhostScriptCommandforWindows(commandList, inputParams, System
						.getenv(GHOSTSCRIPT_ENV_VARIABLE)
						+ File.separator, QUOTES + imageName + QUOTES, QUOTES + outputImagePath + fileExtension + QUOTES);

			} else {
				String outputImageName = outputImagePath + fileExtension;
				commandList.add("convert");
				createCommandForLinux(commandList, inputParams, SPACE + imageName + SPACE, outputParams, SPACE + outputImageName + SPACE);
			}
			String[] cmds = (String[]) commandList.toArray(new String[commandList.size()]);

			if (thread != null) {
				logger.info("Adding generated command to thread pool. Command is : ");
				for (int ind = 0; ind < cmds.length; ind++) {
					logger.info(cmds[ind] + SPACE);
				}
				if (OSUtil.isWindows()) {
					thread.add(new ProcessExecutor(cmds, null));
				} else {
					thread.add(new ProcessExecutor(cmds, new File(System.getenv(IM4JAVA_TOOLPATH))));
				}
			} else {
				logger.error("Command " + command + " cannot be run");
				throw new DCMAApplicationException("Command " + command + " cannot be run");
			}

		} catch (Exception ex) {
			logger.error("Problem generating tiffs from Pdfs");
			throw new DCMAApplicationException("Problem generating tiffs from Pdfs", ex);
		}

	}
	
	private String getOutputPluginConfigForIM(BatchClass batchClass){
		String outputParams = "";
		BatchPluginConfiguration[] pluginConfiguration = pluginPropertiesService.getPluginProperties(batchClass.getIdentifier(),
				ImageMagicKConstants.IMPORT_MULTIPAGE_FILES_PLUGIN, ImageMagicProperties.IM_CONVERT_OUTPUT_IMAGE_PARAMETERS);
		if (pluginConfiguration != null && pluginConfiguration.length > 0 && pluginConfiguration[0].getValue() != null
				&& pluginConfiguration[0].getValue().length() > 0) {
			outputParams = pluginConfiguration[0].getValue();
		}
		return outputParams;
	}
	private String getInputPluginConfigForIM(BatchClass batchClass) {
		String inputParams = "";
		BatchPluginConfiguration[] pluginConfiguration = pluginPropertiesService.getPluginProperties(batchClass.getIdentifier(),
				ImageMagicKConstants.IMPORT_MULTIPAGE_FILES_PLUGIN, ImageMagicProperties.IM_CONVERT_INPUT_IMAGE_PARAMETERS);
		if (pluginConfiguration != null && pluginConfiguration.length > 0 && pluginConfiguration[0].getValue() != null
				&& pluginConfiguration[0].getValue().length() > 0) {
			inputParams = pluginConfiguration[0].getValue();
		}
		return inputParams;
	}
	private String getPluginConfigForGS(BatchClass batchClass) {
		BatchPluginConfiguration[] pluginConfiguration = pluginPropertiesService.getPluginProperties(batchClass.getIdentifier(),
				ImageMagicKConstants.IMPORT_MULTIPAGE_FILES_PLUGIN, ImageMagicProperties.GS_IMAGE_PARAMETERS);
		String imageParams = EMPTY_STRING;
		if (pluginConfiguration != null && pluginConfiguration.length > 0 && pluginConfiguration[0].getValue() != null
				&& pluginConfiguration[0].getValue().length() > 0) {
			imageParams = pluginConfiguration[0].getValue();
			if (imageParams != null) {
				imageParams = imageParams.trim();
			}
		}
		return imageParams;
	}
	
	
	/**
	 * This API will convert the input file to output file using image magic on the basis of input params and output params provided.
	 * 
	 * @param inputParams {@link String}
	 * @param inputImagePath {@link String} absoulte path of input image.
	 * @param outputParams {@link String}
	 * @param outputImagePath {@link String} absoulte path of output image.
	 * @param batchInstanceThread
	 * @throws DCMAApplicationException
	 */
	public void convertInputFileToOutputFileUsingIM(String inputParams, String inputImagePath, String outputParams,
			String outputImagePath, BatchInstanceThread batchInstanceThread) throws DCMAApplicationException {
		String repairImageMagickFileUtilityPath = System.getenv(REPAIR_IMAGE_MAGICK_FILES_ENV_VARIABLE);
		try {
			String command = EMPTY_STRING;
			ArrayList<String> commandList = new ArrayList<String>();
			if (OSUtil.isWindows()) {
				commandList.add(repairImageMagickFileUtilityPath + File.separator + IMAGEMAGICK_EXECUTOR);
				createImageMagickCommandforWindows(commandList, inputParams, QUOTES + System.getenv(IM4JAVA_TOOLPATH) + File.separator
						+ "convert\"", QUOTES + inputImagePath + QUOTES, outputParams, QUOTES + outputImagePath + QUOTES);

			} else {
				commandList.add("convert");
				createCommandForLinux(commandList, inputParams, SPACE + inputImagePath + SPACE, outputParams, SPACE + outputImagePath
						+ SPACE);
			}
			String[] cmds = (String[]) commandList.toArray(new String[commandList.size()]);

			if (batchInstanceThread != null) {
				logger.info("Adding generated command to thread pool. Command is : ");
				for (int ind = 0; ind < cmds.length; ind++) {
					logger.info(cmds[ind] + SPACE);
				}
				if (OSUtil.isWindows()) {
					batchInstanceThread.add(new ProcessExecutor(cmds, null));
				} else {
					batchInstanceThread.add(new ProcessExecutor(cmds, new File(System.getenv(IM4JAVA_TOOLPATH))));
				}
			} else {
				logger.error("Command " + command + " cannot be run");
				throw new DCMAApplicationException("Command " + command + " cannot be run");
			}

		} catch (Exception ex) {
			logger.error("Problem generating tiffs");
			throw new DCMAApplicationException("Problem generating tiffs", ex);
		}
	}
}
