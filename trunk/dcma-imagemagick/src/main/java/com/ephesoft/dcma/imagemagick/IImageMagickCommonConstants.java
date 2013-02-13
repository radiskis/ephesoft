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

package com.ephesoft.dcma.imagemagick;

/**
 * This interface stores all the common constants used for ImageMagick conversions and processing.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.imagemagick.service.ImageProcessServiceImpl 
 */
public interface IImageMagickCommonConstants {

	/**
	 * Thumbnail length.
	 */
	int THUMBNAIL_LENGTH = 200;
	/**
	 * Thumbnail breadth.
	 */
	int THUMBNAIL_BREDTH = 150;
	/**
	 * Constant for thumbnail.
	 */
	String THUMBNAIL = "thumbnail";
	/**
	 * Constant for thumbs.
	 */
	String THUMBS = "thumbs";
	/**
	 * Constant fot png type.
	 */
	String PNG = "png";
	/**
	 * Constant for tif type.
	 */
	String TIF = "tif";
	/**
	 * Constant for png extension.
	 */
	String EXT_PNG = ".png";
	/**
	 * Constant for tif extension.
	 */
	String EXT_TIF = ".tif";
	/**
	 * Suffix sample for thumbnail of png type.
	 */
	String SUFFIX_THUMBNAIL_SAMPLE_PNG = "_thumb.png";
	/**
	 * Suffix sample for thumbnail of tif type.
	 */
	String SUFFIX_THUMBNAIL_SAMPLE_TIF = "_thumb.tif";
	/**
	 * Imagemagick environment variable.
	 */
	String IMAGEMAGICK_ENV_VARIABLE = "IM4JAVA_TOOLPATH";
	/**
	 * Image magick compare classification.
	 */
	String IMAGE_COMPARE_CLASSIFICATION = "Image_Compare_Classification";
	/**
	 * Default IM compare metric.
	 */
	String DEFAULT_IM_COMP_METRIC = "RMSE";
	/**
	 * Default IM compare fuzz.
	 */
	String DEFAULT_IM_COMP_FUZZ = "10";
	/**
	 * Thumbnail type display.
	 */
	String THUMB_TYPE_DISP = "displayThumbnail";
	/**
	 * compare thumbnail type.
	 */
	String THUMB_TYPE_COMP = "compareThumbnail";
	/**
	 * OCR input file type.
	 */
	String OCR_INPUT_FILE = "ocrInputFile";
	/**
	 * Constant to store display image.
	 */
	String DISPLAY_IMAGE = "displayImage";
	/**
	 * Repair files utility through ghostscript.
	 */
	String REPAIR_FILES_THROUGH_GHOSTSCIPT_ENV_VARIABLE = "REPAIR_FILES_UTILITY_PATH";
	/**
	 * Ghostscript home environment variable.
	 */
	String GHOSTSCRIPT_ENV_VARIABLE = "GHOSTSCRIPT_HOME";
	/**
	 * Imagemagick repair files utility.
	 */
	String REPAIR_IMAGE_MAGICK_FILES_ENV_VARIABLE = "REPAIR_IMAGE_MAGICK_UTILITY_PATH";
	/**
	 * Constant for double quotes.
	 */
	String QUOTES = "\"";
	/**
	 * Constant for space.
	 */
	String SPACE = " ";
	/**
	 * Constant for ghostscript executor.
	 */
	String GHOSTSCRIPT_EXECUTOR = "EphesoftExecutor.exe";
	/**
	 * Constant for imagemagick executor.
	 */
	String IMAGEMAGICK_EXECUTOR = "EphesoftImageMagickExecutor.exe";
	/**
	 * Constant for imagemagick environment variable home.
	 */
	String IM4JAVA_TOOLPATH = "IM4JAVA_TOOLPATH";
}
