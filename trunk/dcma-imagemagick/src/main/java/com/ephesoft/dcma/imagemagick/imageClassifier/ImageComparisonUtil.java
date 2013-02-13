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

package com.ephesoft.dcma.imagemagick.imageclassifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.imagemagick.IImageMagickCommonConstants;
import com.ephesoft.dcma.imagemagick.constant.ImageMagicKConstants;
import com.ephesoft.dcma.util.OSUtil;

/**
 * This class contains methods to compare two images.The underlying engine to compare images is Image Magick.One of the method uses
 * IM4Java to talk to Image Magick. The other method does this using a normal Runtime.exec call.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.imagemagick.service.ImageProcessServiceImpl
 * 
 */
public class ImageComparisonUtil {

	/**
	 * An instance of Logger for logging in this file.
	 */
	protected final static Logger LOGGER = LoggerFactory.getLogger(ImageComparisonUtil.class);

	/**
	 * Instance of {@link PluginPropertiesService}.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * @return the {@link PluginPropertiesService}
	 */
	public PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	/**
	 * @param pluginPropertiesService the {@link PluginPropertiesService} to set
	 */
	public void setPluginPropertiesService(PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	/**
	 * This Constructor is called by the Spring framework.
	 */
	public ImageComparisonUtil() {
		LOGGER.info("Inside constructor..");
	}

	/**
	 * The parameterized constructor.
	 * @param isDefault boolean
	 */
	public ImageComparisonUtil(boolean isDefault) {
		LOGGER.info("Inside constructor..");
		LOGGER.debug("is default : " + isDefault);
	}

	/**
	 * Calls the Image Magick compare command using Runtime.execute. sample command is compare -dissimilarity-threshold 100% -metric
	 * <imMetric> -fuzz <imFuzz>% <path1> <path2> null
	 * @param path1 {@link String}
	 * @param path2 {@link String}
	 * @param imMetric {@link String}
	 * @param imFuzz {@link String}
	 * @return double
	 */
	public double compareImagesRuntime(final String path1, final String path2, final String imMetric, final String imFuzz) {
		double similarity = 0;
		String imMetricLocal = imFuzz;
		String imFuzzLocal = imFuzz;
		LOGGER.info("imMetric = " + imMetricLocal);
		LOGGER.info("imFuzz = " + imFuzzLocal);

		if (imMetricLocal == null || imMetricLocal.isEmpty()) {
			imMetricLocal = IImageMagickCommonConstants.DEFAULT_IM_COMP_METRIC;
		}
		if (imFuzzLocal == null || imFuzzLocal.isEmpty()) {
			imFuzzLocal = IImageMagickCommonConstants.DEFAULT_IM_COMP_FUZZ;
		}

		try {
			Runtime runtime = Runtime.getRuntime();
			StringBuffer compareCommand = new StringBuffer(ImageMagicKConstants.COMPARE_COMMAND_MAX_SIZE_120);
			if (OSUtil.isWindows()) {
				compareCommand.append("cmd /c");
				compareCommand.append(ImageMagicKConstants.SPACE);
			}
			compareCommand.append("compare");
			compareCommand.append(ImageMagicKConstants.SPACE);
			compareCommand.append("-dissimilarity-threshold");
			compareCommand.append(ImageMagicKConstants.SPACE);
			compareCommand.append("100%");
			compareCommand.append(ImageMagicKConstants.SPACE);
			compareCommand.append("-metric");
			compareCommand.append(ImageMagicKConstants.SPACE);
			compareCommand.append(imMetricLocal);
			compareCommand.append(ImageMagicKConstants.SPACE);
			compareCommand.append("-fuzz");
			compareCommand.append(ImageMagicKConstants.SPACE);
			compareCommand.append(imFuzzLocal);
			compareCommand.append('%');
			compareCommand.append(ImageMagicKConstants.SPACE);
			compareCommand.append(ImageMagicKConstants.QUOTES + path1 + ImageMagicKConstants.QUOTES);
			compareCommand.append(ImageMagicKConstants.SPACE);
			compareCommand.append(ImageMagicKConstants.QUOTES + path2 + ImageMagicKConstants.QUOTES);
			compareCommand.append(ImageMagicKConstants.SPACE);
			compareCommand.append("null");
			String compareString = compareCommand.toString();
			LOGGER.info("Compare command = " + compareString);
			Process proc = runtime.exec(compareString, null, new File(System
					.getenv(IImageMagickCommonConstants.IMAGEMAGICK_ENV_VARIABLE)));

			String cmdOutput = "";
			InputStreamReader inputStreamReader = null;
			BufferedReader sysErr = null;
			try {
				inputStreamReader = new InputStreamReader(proc.getErrorStream());
				sysErr = new BufferedReader(inputStreamReader);
				cmdOutput = sysErr.readLine();
				LOGGER.info("cmdOutput = " + cmdOutput);
			} catch (IOException ioe) {
				LOGGER.error("Exception while reading the buffer : " + ioe.getMessage(), ioe);
			} finally {
				if (null != sysErr) {
					sysErr.close();
				}
				if (null != inputStreamReader) {
					inputStreamReader.close();
				}
			}
			LOGGER.info("cmdOutput = " + cmdOutput);
			int openingBraces = cmdOutput.indexOf('(');
			int closingBraces = cmdOutput.indexOf(')');

			LOGGER.info("openingBraces = " + openingBraces);
			LOGGER.info("closingBraces = " + closingBraces);

			if (openingBraces != -1 && closingBraces != -1) {
				String strDisimilarity = cmdOutput.substring(openingBraces + 1, closingBraces);
				LOGGER.info("strDisimilarity = " + strDisimilarity);

				double disimilarity = 0;
				try {
					disimilarity = Double.parseDouble(strDisimilarity);
				} catch (NumberFormatException nfe) {
					LOGGER.error("Exception while conversion from string to double : " + nfe.getMessage(), nfe);
				}
				LOGGER.info("disimilarity = " + disimilarity);

				similarity = (1 - disimilarity) * ImageMagicKConstants.CONSTANT_VALUE_100;
				LOGGER.info("Similarity = " + similarity);
			}
		} catch (Exception e) {
			LOGGER.error("Exception while executing the compare command : " + e.getMessage(), e);
			throw new DCMABusinessException("Unable to Compare Images ", e);
		}
		return similarity;
	}

}
