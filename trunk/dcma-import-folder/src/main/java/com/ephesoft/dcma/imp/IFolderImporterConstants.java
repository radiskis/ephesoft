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

package com.ephesoft.dcma.imp;

import com.ephesoft.dcma.core.common.FileType;

/**
 * An interface keeping the string and numeric constants.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public interface IFolderImporterConstants {

	/**
	 * The value "100".
	 */
	int HUNDERED = 100;
	/**
	 * The value "1024".
	 */
	int KBYTE = 1024;
	/**
	 * The value "0".
	 */
	int ZERO = 0;
	/**
	 * The value "1".
	 */
	int ONE = 1;
	/**
	 * The value "true".
	 */
	String TRUE = "true";
	/**
	 * The value "YES".
	 */
	String YES = "YES";

	/**
	 * Message format for showing the expected tiff files count.
	 */
    String EXPECTED_TIF_FILES_COUNT = ", expectedTifFilesCount = ";

	/**
	 * A constant to store period delimiter.
	 */
	String DOT_DELIMITER = ".";

	/**
	 * A constant to store semicolon delimiter.
	 */
	String SEMICOLON_DELIMITER = ";";

	/**
	 * A constant to store underscore.
	 */
	String UNDERSCORE = "_";
	
	/**
	 * A constant to store empty.
	 */
	String EMPTY = "";

	/**
	 * A constant to store serialized file extension .
	 */
	String SER_EXTENSION = SEMICOLON_DELIMITER + FileType.SER.getExtension();

	/**
	 * Import multipage files plugin name.
	 */
	String IMPORT_MULTIPAGE_FILES_PLUGIN = "IMPORT_MULTIPAGE_FILES";

	/**
	 * Import batch folder plugin name.
	 */
	String IMPORT_BATCH_FOLDER_PLUGIN = "IMPORT_BATCH_FOLDER";

	/**
	 * A constant to store extension of serialized files with dot.
	 */
	String SERIALIZATION_EXT = FileType.SER.getExtensionWithDot();
	
	/**
	 * Constant to store the serialized file name.
	 */
	String BCF_SER_FILE_NAME = "BCF_ASSO";

}
