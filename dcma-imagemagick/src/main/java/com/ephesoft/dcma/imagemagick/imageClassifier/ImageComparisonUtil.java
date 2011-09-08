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

package com.ephesoft.dcma.imagemagick.imageClassifier;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.imagemagick.IImageMagickCommonConstants;
import com.ephesoft.dcma.imagemagick.ImageMagicProperties;
import com.ephesoft.dcma.imagemagick.constant.ImageMagicKConstants;

/**
 * This class contains methods to compare two images.The underlying engine to compare images is Image Magick.One of the method uses
 * IM4Java to talk to Image Magick. The other method does this using a normal Runtime.exec call.
 * 
 * @author Ephesoft
 * 
 */
public class ImageComparisonUtil {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Instance of PluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * @return the pluginPropertiesService
	 */
	public PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	/**
	 * @param pluginPropertiesService the pluginPropertiesService to set
	 */
	public void setPluginPropertiesService(PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	/**
	 * This Constructor is called by the Spring framework.
	 * 
	 * @param imMetric
	 * @param imFuzz
	 */
	public ImageComparisonUtil() {
	}

	/**
	 * The Default Constructor.
	 */
	public ImageComparisonUtil(boolean isDefault) {
		
	}

	/**
	 * Calls the Image Magick command using IM4Java interface. The command line command created by this method is compare -metric
	 * <imMetric> -fuzz <imFuzz> <path1> <path2> null
	 * 
	 * @param path1
	 * @param path2
	 * @return Similarity Percentage 100 for totally similar images.
	 * @throws DCMAApplicationException
	 */
	/*public double compareImagesIM4J(final String path1, final String path2) throws DCMAApplicationException {
		File image1 = new File(path1);
		File image2 = new File(path2);
		if (!image1.exists() || !image2.exists()) {
			throw new DCMABusinessException("One of the images not found images =" + image1 + " ," + image2);
		}
		IMOperation all = new IMOperation();
		all.metric(imMetric);
		all.fuzz(Double.parseDouble(imFuzz));
		all.addImage();
		all.addImage();
		all.addImage("null");
		ArrayListErrorConsumer error = new ArrayListErrorConsumer();
		ImageCommand compare = new ImageCommand();
		compare.setErrorConsumer(error);
		compare.setCommand("compare");
		try {
			compare.run(all, path1, path2);
		} catch (IOException e) {
			throw new DCMAApplicationException("Unable to compare images", e);
		} catch (InterruptedException e) {
			throw new DCMAApplicationException("Unable to compare images", e);
		} catch (IM4JavaException e) {
			throw new DCMAApplicationException("Unable to compare images", e);
		}

		ArrayList<String> cmdError = error.getOutput();
		String output = cmdError.get(0);
		int openingBraces = output.indexOf('(');
		int closingBraces = output.indexOf(')');

		String strDisimilarity = output.substring(openingBraces + 1, closingBraces);
		double disimilarity = Double.parseDouble(strDisimilarity);

		double similarity = (1 - disimilarity) * 100;
		return similarity;
	}*/

	/**
	 * Calls the Image Magick compare command using Runtime.execute. sample command is compare -dissimilarity-threshold 100% -metric
	 * <imMetric> -fuzz <imFuzz>% <path1> <path2> null
	 * 
	 * @param path1 image file 1
	 * @param path2 image file 2
	 * @return Similarity Percentage 100 for totally similar images.
	 */
	public double compareImagesRuntime(final String path1, final String path2, final String batchInstanceID) {
		double similarity = 0;
		
		// Initialize properties
		logger.info("Initializing properties...");
		String imMetric = pluginPropertiesService.getPropertyValue(batchInstanceID, ImageMagicKConstants.CLASSIFY_IMAGES_PLUGIN,
				ImageMagicProperties.CLASSIFY_IMAGES_COMP_METRIC);
		String imFuzz = pluginPropertiesService.getPropertyValue(batchInstanceID, ImageMagicKConstants.CLASSIFY_IMAGES_PLUGIN,
				ImageMagicProperties.CLASSIFY_IMAGES_FUZZ_PERCNT);
		logger.info("Properties Initialized Successfully");
		
		if(!(imMetric != null && imMetric.length()>0)){
			imMetric = IImageMagickCommonConstants.DEFAULT_IM_COMP_METRIC;
		}
		if(!(imFuzz != null && imFuzz.length()>0)){
			imFuzz = IImageMagickCommonConstants.DEFAULT_IM_COMP_FUZZ;
		}
		
		try {
			Runtime runtime = Runtime.getRuntime();
			StringBuffer comapreCommand = new StringBuffer(120);
			comapreCommand.append("cmd /c");
			comapreCommand.append(" ");
			comapreCommand.append("compare");
			comapreCommand.append(' ');
			comapreCommand.append("-dissimilarity-threshold");
			comapreCommand.append(" ");
			comapreCommand.append("100%");
			comapreCommand.append(" ");
			comapreCommand.append("-metric");
			comapreCommand.append(" ");
			comapreCommand.append(imMetric);
			comapreCommand.append(" ");
			comapreCommand.append("-fuzz");
			comapreCommand.append(" ");
			comapreCommand.append(imFuzz);
			comapreCommand.append("%");
			comapreCommand.append(" ");
			comapreCommand.append("\"" + path1 + "\"");
			comapreCommand.append(" ");
			comapreCommand.append("\"" + path2 + "\"");
			comapreCommand.append(" ");
			comapreCommand.append("null");
			logger.debug("Compare command=" + comapreCommand.toString());
			Process pr = runtime.exec(comapreCommand.toString());

			BufferedReader sysErr = new BufferedReader(new InputStreamReader(pr.getErrorStream()));

			String cmdOutput;
			cmdOutput = sysErr.readLine();
			int openingBraces = cmdOutput.indexOf('(');
			int closingBraces = cmdOutput.indexOf(')');

			String strDisimilarity = cmdOutput.substring(openingBraces + 1, closingBraces);
			double disimilarity = Double.parseDouble(strDisimilarity);

			similarity = (1 - disimilarity) * 100;
			logger.debug("Similarity=" + similarity);

		} catch (Exception e) {

			throw new DCMABusinessException("Unable to Compare Images ", e);
		}
		return similarity;
	}

}
