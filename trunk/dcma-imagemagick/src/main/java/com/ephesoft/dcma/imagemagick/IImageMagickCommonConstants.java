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

public interface IImageMagickCommonConstants {

	public static int THUMBNAIL_LENGTH = 200;
	public static int THUMBNAIL_BREDTH = 150;
	public static String THUMBNAIL = "thumbnail";
	public static String THUMBS = "thumbs";
	public static String PNG = "png";
	public static String TIF = "tif";
	public static String EXT_PNG = ".png";
	public static String EXT_TIF = ".tif";
	public static String SUFFIX_THUMBNAIL_SAMPLE_PNG = "_thumb.png";
	public static String SUFFIX_THUMBNAIL_SAMPLE_TIF = "_thumb.tif";
	public static String IMAGEMAGICK_ENV_VARIABLE = "IM4JAVA_TOOLPATH";
	public static String IMAGE_COMPARE_CLASSIFICATION = "Image_Compare_Classification";
	public static String DEFAULT_IM_COMP_METRIC = "RMSE";
	public static String DEFAULT_IM_COMP_FUZZ = "10";
	public static String THUMB_TYPE_DISP = "displayThumbnail";
	public static String THUMB_TYPE_COMP = "compareThumbnail";
	public static String OCR_INPUT_FILE = "ocrInputFile";
	public static String DISPLAY_IMAGE = "displayImage";
	public static String REPAIR_FILES_THROUGH_GHOSTSCIPT_ENV_VARIABLE = "REPAIR_FILES_UTILITY_PATH";
	public static String GHOSTSCRIPT_ENV_VARIABLE = "GHOSTSCRIPT_HOME";
	public static String REPAIR_IMAGE_MAGICK_FILES_ENV_VARIABLE="REPAIR_IMAGE_MAGICK_UTILITY_PATH";
	
}
