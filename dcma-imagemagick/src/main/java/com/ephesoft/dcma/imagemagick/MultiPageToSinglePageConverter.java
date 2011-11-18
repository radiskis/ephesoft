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

	private static final String EMPTY_STRING = "";

	private static final String QUOTES = "\"";

	private static final String SPACE = " ";

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
	public void convertPdfOrMultiPageTiffToTiff(BatchClass batchClass, File imagePath, File outputFilePath, BatchInstanceThread thread)
			throws DCMAApplicationException {
		String repairFileUtilityPath = System.getenv(REPAIR_FILES_ENV_VARIABLE);
		String imageName = imagePath.getAbsolutePath();
		int indexOf = imageName.toLowerCase().indexOf(FileType.PDF.getExtensionWithDot());
		if (indexOf == -1) {
			indexOf = imageName.toLowerCase().indexOf(FileType.TIF.getExtensionWithDot());
			if (indexOf == -1) {
				indexOf = imageName.toLowerCase().indexOf(FileType.TIFF.getExtensionWithDot());
			}
		}
		if (indexOf == -1) {
			logger.error("Unsupported file format");
			return;
		}
		String outputImagePath = imageName.substring(0, indexOf);
		String fileExtension = "-%04d" + FileType.TIF.getExtensionWithDot();
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
				commandList.add("cmd ");
				commandList.add("/c ");
				commandList.add(repairFileUtilityPath + File.separator + "EphesoftExecutor.exe");
				createCommandforWindows(commandList, batchClass, QUOTES + System.getenv(IM4JAVA_TOOLPATH) + File.separator
						+ "convert\"", QUOTES + imageName + QUOTES, QUOTES + outputImagePath + fileExtension + QUOTES);
				// commandList.add(command);
			} else {
				String outputImageName = outputImagePath + fileExtension; // OSUtil.escapeSpacesForUnixLinux(outputImagePath +
				// fileExtension);
				commandList.add("convert");
				createCommandForLinux(commandList, batchClass, SPACE + imageName + SPACE, SPACE + outputImageName + SPACE);
				// commandList.add(command);
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

	private String createCommandforWindows(ArrayList<String> commandList, BatchClass batchClass, String environment,
			String inputImageName, String outputImageName) {
		if (environment != null) {
			commandList.add(environment);
		}
		StringBuffer command = new StringBuffer(EMPTY_STRING);
		BatchPluginConfiguration[] pluginConfiguration = pluginPropertiesService.getPluginProperties(batchClass.getIdentifier(),
				ImageMagicKConstants.IMPORT_MULTIPAGE_FILES_PLUGIN, ImageMagicProperties.IM_CONVERT_INPUT_IMAGE_PARAMETERS);
		String inputParams = EMPTY_STRING;
		if (pluginConfiguration != null && pluginConfiguration.length > 0 && pluginConfiguration[0].getValue() != null
				&& pluginConfiguration[0].getValue().length() > 0) {
			inputParams = pluginConfiguration[0].getValue();
		}
		commandList.add(QUOTES + inputParams.trim() + QUOTES);
		commandList.add(inputImageName.trim());
		pluginConfiguration = pluginPropertiesService.getPluginProperties(batchClass.getIdentifier(),
				ImageMagicKConstants.IMPORT_MULTIPAGE_FILES_PLUGIN, ImageMagicProperties.IM_CONVERT_OUTPUT_IMAGE_PARAMETERS);
		String outputParams = EMPTY_STRING;
		if (pluginConfiguration != null && pluginConfiguration.length > 0 && pluginConfiguration[0].getValue() != null
				&& pluginConfiguration[0].getValue().length() > 0) {
			outputParams = pluginConfiguration[0].getValue();
		}
		commandList.add(QUOTES + outputParams.trim() + QUOTES);
		commandList.add(outputImageName.trim());
		return command.toString();
	}

	private String createCommandForLinux(ArrayList<String> commandList, BatchClass batchClass, String inputImageName,
			String outputImageName) {
		StringBuffer command = new StringBuffer(EMPTY_STRING);
		BatchPluginConfiguration[] pluginConfiguration = pluginPropertiesService.getPluginProperties(batchClass.getIdentifier(),
				ImageMagicKConstants.IMPORT_MULTIPAGE_FILES_PLUGIN, ImageMagicProperties.IM_CONVERT_INPUT_IMAGE_PARAMETERS);
		if (pluginConfiguration != null && pluginConfiguration.length > 0 && pluginConfiguration[0].getValue() != null
				&& pluginConfiguration[0].getValue().length() > 0) {
			// command.append(pluginConfiguration[0].getValue() + " ");
			String inputParams = pluginConfiguration[0].getValue();
			String inputParamsArr[] = inputParams.split(SPACE);
			for (String string : inputParamsArr) {
				commandList.add(string.trim());
			}
		}
		commandList.add(inputImageName.trim());
		// command.append(inputImageName + " ");
		pluginConfiguration = pluginPropertiesService.getPluginProperties(batchClass.getIdentifier(),
				ImageMagicKConstants.IMPORT_MULTIPAGE_FILES_PLUGIN, ImageMagicProperties.IM_CONVERT_OUTPUT_IMAGE_PARAMETERS);
		if (pluginConfiguration != null && pluginConfiguration.length > 0 && pluginConfiguration[0].getValue() != null
				&& pluginConfiguration[0].getValue().length() > 0) {
			// command.append(pluginConfiguration[0].getValue() + " ");
			String outputParams = pluginConfiguration[0].getValue();
			String outputParamsArr[] = outputParams.split(SPACE);
			for (String string : outputParamsArr) {
				commandList.add(string.trim());
			}
		}
		commandList.add(outputImageName.trim());
		// command.append(outputImageName);
		return command.toString();
	}

}
