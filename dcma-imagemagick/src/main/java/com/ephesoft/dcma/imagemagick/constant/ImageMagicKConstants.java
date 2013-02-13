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

package com.ephesoft.dcma.imagemagick.constant;

/**
 * This interface is used for all the constants used across this plug-in.
 * 
 * @author Ephesoft
 *
 */
public interface ImageMagicKConstants {

	/**
	 * Image magic plug in name.
	 */
	String CLASSIFY_IMAGES_PLUGIN = "CLASSIFY_IMAGES";

	/**
	 * Create thumbnails plug in name.
	 */
	String CREATE_THUMBNAILS_PLUGIN = "CREATE_THUMBNAILS";

	/**
	 * Create OCR input plug in name.
	 */
	String CREATE_OCR_INPUT_PLUGIN = "CREATE_OCR_INPUT";

	/**
	 * Create display image plug in name.
	 */
	String CREATE_DISPLAY_IMAGE_PLUGIN = "CREATE_DISPLAY_IMAGE";

	/**
	 * Create multipage files plug in name.
	 */
	String CREATEMULTIPAGE_FILES_PLUGIN = "CREATEMULTIPAGE_FILES";

	/**
	 * Import multipage plug in name.
	 */
	String IMPORT_MULTIPAGE_FILES_PLUGIN = "IMPORT_MULTIPAGE_FILES";

	/**
	 * Image magick plug in.
	 */
	String IMAGE_MAGICK = "IMAGE_MAGICK";

	/**
	 * HOCR to PDF convert plug in. 
	 */
	String HOCR_TO_PDF = "HOCRtoPDF";

	/**
	 * Ghost script command parameters.
	 */
	String GHOST_SCRIPT_COMMAND_PARAMETERS = "-dQUIET -dNOPAUSE -r300 -sDEVICE=pdfwrite";

	/**
	 * Ghost script command output parameters.
	 */
	String GHOST_SCRIPT_COMMAND_OUTPUT_PARAMETERS = "-dBATCH";
	
	/**
	 * Ghost script command output file parameters.
	 */
	String GHOST_SCRIPT_COMMAND_OUTPUT_FILE_PARAM = "-sOutputFile=";

	/**
	 * Ghostscript conversion.
	 */
	String GHOST_SCRIPT = "GHOSTSCRIPT";

	/**
	 * Default tiff device parameters.
	 */
	String DEFAULT_TIF_DEVICE = "tiffg4";

	/**
	 * PDF write option.
	 */
	String PDF_DEVICE = "pdfwrite";

	/**
	 * Plug in switch on status.
	 */
	String ON_SWITCH = "ON";

	/**
	 * Temporary file name constant.
	 */
	String TEMP_FILE_NAME = "tempfile";

	/**
	 * Constant for double quotes.
	 */
	String DOUBLE_QUOTES = "\"";

	/**
	 * Constant for space.
	 */
	String SPACE = " ";

	/**
	 * Ghostscript home.
	 */
	String GHOSTSCRIPT_HOME = "GHOSTSCRIPT_HOME";

	/**
	 * Ghostscript command to execute.
	 */
	String GHOSTSCRIPT_COMMAND = "gswin32c.exe";

	/**
	 * String constant for false option. 
	 */
	String FALSE = "false";

	/**
	 * Limit of max files per ghostscript command.
	 */
	int MAX_FILES_PER_GS_COMMAND = 75;

	/**
	 * ITEXT conversion parameter.
	 */
	String ITEXT = "ITEXT";

	/**
	 * Default value for PDF page height.
	 */
	int DEFAULT_PDF_PAGE_HEIGHT = 792;
	
	/**
	 * Default value for PDF page width.
	 */
	int DEFAULT_PDF_PAGE_WIDTH = 612;

	/**
	 * A constant for underscore.
	 */
	char UNDERSCORE = '_';
	
	/**
	 * Constant char for storing period.
	 */
	char DOT = '.';
	/**
	 * A constant value for storing size of array.
	 */
	int CONSTANT_VALUE_3 = 3;

	/**
	 * String to store command output format.
	 */
	String COMMAND_OUTPUT = "cmdOutput = ";

	/**
	 * A constant to store convert command.
	 */
	String CONVERT_COMMAND = "convert";

	/**
	 * Error message for invalid height of scanned image.
	 */
	String INVALID_THUMBAIL_HEIGHT_SPECIFIED = "Invalid thumbail height for scanned images specified in imagemagick property file.";

	/**
	 * Error message for invalid width of scanned image.
	 */
	String INVALID_THUMBAIL_WIDTH_SPECIFIED = "Invalid thumbail width for scanned images specified in imagemagick property file.";

	/**
	 * Error message for invalid png width specified.
	 */
	String INVALID_PNG_WIDTH_SPECIFIED = "Invalid PNG width for scanned images specified in imagemagick property file.";

	/**
	 * Error message for invalid png height specified.
	 */
	String INVALID_PNG_HEIGHT_SPECIFIED = "Invalid PNG height for scanned images specified in imagemagick property file.";

	/**
	 * Message stating value being set to default.
	 */
	String SETTING_TO_DEFAULT_VALUE = "Setting to default value:";

	/**
	 * Constant for empty string.
	 */
	String EMPTY_STRING = "";

	/**
	 * Error message stating problems in generating thumbnails.
	 */
	String PROBLEM_GENERATING_THUMBNAILS = "Problem generating thumbnails";

	/**
	 * Constant for color gray.
	 */
	String GRAY_COLOR = "gray";

	/**
	 * Constant for double quotes.
	 */
	String QUOTES = "\"";

	/**
	 * Constant to store default thumbnail width.
	 */
	Integer DEFAULT_THUMBNAIL_WIDTH = 200;

	/**
	 * Constant to store default thumbnail height.
	 */
	Integer DEFAULT_THUMBNAIL_HEIGHT = 150;

	/**
	 * Constant to store default png width.
	 */
	Integer DEFAULT_PNG_WIDTH = 800;

	/**
	 * Constant to store default png height.
	 */
	Integer DEFAULT_PNG_HEIGHT = 600;

	/**
	 * Error message stating problem generating tiff files.
	 */
	String PROBLEM_GENERATING_TIFF = "Problem generating tiffs";

	/**
	 * Image magick ghostscript executable command.
	 */
	String IMAGEMAGICK_GHOSTSCRIPT_EXE = "imagemagick.ghostscript_command";

	/**
	 * Image magick property file name.
	 */
	String IMAGEMAGICK_PROPERTY_FILE_NAME = "imagemagick";

	/**
	 * Image magick properties folder name.
	 */
	String IMAGEMAGICK_FOLDER = "dcma-imagemagick";

	/**
	 * Multi page convert command parameter.
	 */
	String MULTIPAGE_COMMAND_PARAMETER = "-%04d";
	/**
	 * Ghostscript executor command.
	 */
	String GHOSTSCRIPT_EXECUTOR = "EphesoftExecutor.exe";
	
	/**
	 * Image magick executor command.
	 */
	String IMAGEMAGICK_EXECUTOR = "EphesoftImageMagickExecutor.exe";
	/**
	 * IM$JAVA tool path name.
	 */
	String IM4JAVA_TOOLPATH = "IM4JAVA_TOOLPATH";
	/**
	 * Ghostscript pdf executor path.
	 */
	String GS_PDF_EXECUTOR_PATH = "GS_PDF_EXECUTOR_PATH";

	/**
	 * Error message for multi page pdf creation message.
	 */
	String MULTIPAGE_PDF_CREATION_ERROR_MSG = "Error occured while running command for multipage pdf creation.";
	/**
	 * Default rotating angle.
	 */
	double DEFAULT_DEGREE_OF_ROTATION = 90.0;
	/**
	 * A constant to store comma.
	 */
	String COMMA = ",";
	/**
	 * Problem generating thumbnails.
	 */
	String THUMBNALIS_CREATION_EROR_MSG = "Problem generating thumbnalis exception ";

	/**
	 * Image classification error message.
	 */
	String IMAGE_CLASSIFICATION_ERROR_MSG = "Problem in Image Classification";

	/**
	 * Error message stating problem in rotating images.
	 */
	String PROBLEM_ROTATING_IMAGES = "Problem rotating images";

	/**
	 * Error message stating error in PNG file creation.
	 */
	String ERROR_IN_PNG_FILE_GENERATION = "Problem in generating PNG File";
	/**
	 * The java library path.
	 */
	String JAVA_LIBRARY_PATH = "java.library.path";
	/**
	 * The java temporary directory path.
	 */
	String JAVA_TEMP_DIR_PATH = "java.io.tmpdir";
	/**
	 * Java option prefix.
	 */
	String JAVA_OPTION_PREFIX = "-D";
	/**
	 * Constant for storing semicolon.
	 */
	String SEMI_COLON = ";";
	/**
	 * Constant for storing file name with extension type.
	 */
	String FILE_NAME = "fileNames.txt";
	/**
	 * Constant value of 100 used for calculating similarity.
	 */
	int CONSTANT_VALUE_100 = 100;

	/**
	 * Maximum size of compare command as string buffer.
	 */
	int COMPARE_COMMAND_MAX_SIZE_120 = 120;
	/**
	 * Default max value of 10.
	 */
	int DEFAULT_MAX_VALUE_10 = 10;

		/**
	 * Default width of line.
	 */
	int DEFAULT_WIDTH_OF_LINE = 15;

	/**
	 * String constant for xpath expression for a TextLine in an hocr xml file.
	 */
	String TEXTLINE_XPATH_EXPRESSION = "Documents//Document//ReadAreas//ReadArea//Blocks//Block//TextLines//TextLine";

	String ITEXT_SEARCHABLE = "ITEXT-SEARCHABLE";

	/**
	 * The COMPRESSION_TYPE {@link String} is a constant for compression type parameter used while converting image using image magic.
	 */
	String COMPRESSION_TYPE = "LZW";

	/**
	 * The DOTS {@link String} is a constant for character '.'.
	 */
	String DOTS = ".";

	/**
	 * The NEW {@link String} is a constant for new.
	 */
	String NEW = "_new";

	/**
	 * The TIF_EXTENSION {@link String} is a constant for tif file extension.
	 */
	String TIF_EXTENSION = ".tif";

		/**
	 * The PDF_RESOLUTION is a constant for pdf resolution set for searchable pdf.
	 */
	float PDF_RESOLUTION = 72.0f;

	/**
	 * The IMAGE_QUALITY is a constant for image quality parameter required for image compression using image magic.
	 */
	double IMAGE_QUALITY = 100.0;

	/**
	 * The OPERATION_TYPE {@link String} is a constant for operation type parameter used while converting image using image magic.
	 */
	String OPERATION_TYPE = "optimize";
}
