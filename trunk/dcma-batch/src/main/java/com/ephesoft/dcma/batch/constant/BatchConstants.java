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

package com.ephesoft.dcma.batch.constant;

/**
 * This is a common constants file for Batch.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public interface BatchConstants {

	/**
	 * String constant for space.
	 */
	String SPACE = " ";
	/**
	 * String constant for hyphen.
	 */
	String HYPHEN = "-";
	/**
	 * String constant for slash.
	 */
	String SLASH = "/";

	/**
	 * String constant for empty string.
	 */
	String EMPTY = "";

	/**
	 * String constant for under score string.
	 */
	String UNDER_SCORE = "_";

	/**
	 * String constant for semi colon string.
	 */
	String SEMI_COLON = ";";
	
	/**
	 * String constant for properties directory.
	 */
	String PROPERTIES_DIRECTORY = "properties";
	/**
	 * String constant for directories.
	 */
	String DIRECTORIES = "Directories: ";
	/**
	 * String constant for title.
	 */
	String TITLE = "title";
	/**
	 * String constant for document type list null statement.
	 */
	String DOC_TYPE_LIST_NULL = "docTypesList is null.";
	/**
	 * String constant for string "Unable to create FileNameFormatter.".
	 */
	String FILE_NAME_FORMATTER_NOT_CREATED = "Unable to create FileNameFormatter.";
	/**
	 * String constant for string "Unable to get unique page id for duplicate page.".
	 */
	String UNABLE_TO_GET_UNIQUE_PAGE_ID = "Unable to get unique page id for duplicate page.";
	/**
	 * String constant for batch class null statement.
	 */
	String BATCH_CLASS_NULL = "batch class is null.";
	/**
	 * String constant for batch class null statement.
	 */
	String HOCR_PAGES_NULL = "hocrPages is null.";
	/**
	 * String constant for stating "not created.".
	 */
	String NOT_CREATED = " not created.";
	/**
	 * String constant for stating " created.".
	 */
	String CREATED = " created.";
	/**
	 * String constant for stating "backup batch file not created.".
	 */
	String BACKUP_BATCH_FILE_NOT_CREATED = "Unable to create backup copy of batch file for batch instance : ";
	/**
	 * String constant for stating "batchInstanceIdentifier is null.".
	 */
	String BATCH_INSTANCE_ID_NULL = "batchInstanceIdentifier is null.";
	/**
	 * String constant for png extension.
	 */
	String PNG_EXTENSION = ".png";
	/**
	 * String constant for html extension.
	 */
	String HTML_EXTENSION = ".html";
	/**
	 * String constant for tif extension.
	 */
	String TIF_EXTENSION = ".tif";
	/**
	 * int constant for prime constant.
	 */
	int PRIME_CONST = 31;
	/**
	 * int constant for zero.
	 */
	int ZERO = 0;
	/**
	 * int constant for one.
	 */
	int ONE = 1;
	/**
	 * int constant for two.
	 */
	int TWO = 2;
	/**
	 * int constant for three.
	 */
	int THREE = 3;
	/**
	 * int constant for four.
	 */
	int FOUR = 4;
	/**
	 * int constant for twenty.
	 */
	int TWENTY = 20;
	/**
	 * int constant for hundred.
	 */
	int HUNDRED = 100;
	/**
	 * Radix base.
	 */
	int RADIX_BASE = 10;

	/**
	 * String constant for defining logging area.
	 */
	String LOG_AREA = "BATCH_LOGS : ";

	/**
	 * String constant for "docTypeName is : " literal.
	 */
	String DOC_TYPE_NAME = " docTypeName is ";

	/**
	 * String constant for "pluginName is : " literal.
	 */
	String PLUGIN_NAME = " pluginName is ";
	
	/**
	 * String constant for plugin property retrived message.
	 */
	String PLUGIN_PROPERTIES_RETREIVED = " : plugin properties successfully retreived.";

}
