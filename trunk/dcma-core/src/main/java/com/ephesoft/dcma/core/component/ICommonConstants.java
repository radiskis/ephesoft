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

package com.ephesoft.dcma.core.component;

/**
 * This interface contains all the common constants.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public interface ICommonConstants {
	/**
	 * ENTER_METHOD String.
	 */
	String ENTER_METHOD = ">>>Entering Method";
	
	/**
	 * EXIT_METHOD String.
	 */
	String EXIT_METHOD = "<<<Exiting Method";
	
	/**
	 * CLASS_NAME String.
	 */
	String CLASS_NAME = " >Class Name=";
	
	/**
	 * EXTENSION_TIF String.
	 */
	String EXTENSION_TIF = ".tif";
	
	/**
	 * EXTENSION_PDF String.
	 */
	String EXTENSION_PDF = ".pdf";
	
	/**
	 * EXTENSION_PNG String.
	 */
	String EXTENSION_PNG = ".png";
	
	/**
	 * UNDERSCORE_BATCH_XML_ZIP String.
	 */
	String UNDERSCORE_BATCH_XML_ZIP = "_batch.xml.zip";
	
	/**
	 * UNDERSCORE_BATCH_BAK_XML_ZIP String.
	 */
	String UNDERSCORE_BATCH_BAK_XML_ZIP = "_batch_bak.xml.zip";
	
	/**
	 * UNDERSCORE_BAK_BATCH_XML_ZIP String.
	 */
	String UNDERSCORE_BAK_BATCH_XML_ZIP = "_bak_batch.xml.zip";

	/**
	 * UNDERSCORE_BATCH_XML String.
	 */
	String UNDERSCORE_BATCH_XML = "_batch.xml";
	
	/**
	 * UNDERSCORE_BATCH_BAK_XML String.
	 */
	String UNDERSCORE_BATCH_BAK_XML = "_batch_bak.xml";
	
	/**
	 * UNDERSCORE_BAK_BATCH_XML String.
	 */
	String UNDERSCORE_BAK_BATCH_XML = "_bak_batch.xml";
	
	/**
	 * ZIP_SWITCH String.
	 */
	String ZIP_SWITCH = "zip_switch";

	/**
	 * BATCH_XSD_SCHEMA_PACKAGE String.
	 */
	String BATCH_XSD_SCHEMA_PACKAGE = "com.ephesoft.dcma.batch.schema";
	
	/**
	 * PROPERTIES_FILE_BARCODE_READER String.
	 */
	String PROPERTIES_FILE_BARCODE_READER = "reader.properties";
	
	/**
	 * PROPERTY_BARCODE_READER String.
	 */
	String PROPERTY_BARCODE_READER = "image.base.location";
	
	/**
	 * FIRST_PAGE String.
	 */
	String FIRST_PAGE = "_First_Page";
	
	/**
	 * MIDDLE_PAGE String.
	 */
	String MIDDLE_PAGE = "_Middle_Page";
	
	/**
	 * LAST_PAGE String.
	 */
	String LAST_PAGE = "_Last_Page";

	/**
	 * SEARCH_CLASSIFICATION_PLUGIN String.
	 */
	String SEARCH_CLASSIFICATION_PLUGIN = "SEARCH_CLASSIFICATION";
	
	/**
	 * BARCODE_READER_PLUGIN String.
	 */
	String BARCODE_READER_PLUGIN = "BARCODE_READER";
	
	/**
	 * Folder Import Module Workflow Name.
	 */
	String FOLDER_IMPORT_MODULE = "Folder_Import_Module";
	
	/**
	 * DOWNLOAD_FOLDER_NAME String.
	 */
	String DOWNLOAD_FOLDER_NAME = "downloaded-email-attachments";
	
	/**
	 * BACK_UP_FOLDER_NAME String.
	 */
	String BACK_UP_FOLDER_NAME = "_backup";
	
	/**
	 * ON_CONSTANT String.
	 */
	String ON_CONSTANT = "ON";
	
	/**
	 * OFF_CONSTANT String.
	 */
	String OFF_CONSTANT = "OFF";
	
	/**
	 * CREATE_BATCH_INSTANCE_BACKUP String.
	 */
	String CREATE_BATCH_INSTANCE_BACKUP = "create_batch_instance_backup";
	
	/**
	 * SPACE String.
	 */
	String SPACE = " ";
	
	/**
	 * HYPHEN String.
	 */
	String HYPHEN = "-";
	
	/**
	 * UNC String.
	 */
	String UNC = "unc";
	
	/**
	 * VERSION String.
	 */
	String VERSION = "1.0.0.0";
	
	/**
	 * UNDERSCORE String.
	 */
	String UNDERSCORE = "_";
	/**
	 * A String constant to store colon.
	 */
	String COLON = ":";
	/**
	 * A String constant to store "localhost" for url.
	 */
	String LOCALHOST = "localhost";
	/**
	 * A String constant to store forward slash.
	 */
	String FORWARD_SLASH = "/";
	/**
	 * A String constant to store true.
	 */
	String TRUE = "true";
	
	/**
	 * The DEFAULT_BATCH_INSTANCE_LIMIT is a default constant for batch class instance limit.
	 */
	int DEFAULT_BATCH_INSTANCE_LIMIT = 10;
	
	/**
	 * The DEFAULT_PAGE_COUNT_LIMIT is a default constant for page count limit.
	 */
	int DEFAULT_PAGE_COUNT_LIMIT = 10;
	
	/**
	 * The DEFAULT_BATCH_INSTANCE_LIMIT is a default constant for number of days limit.
	 */
	int DEFAULT_NO_OF_DAYS_LIMIT = 1;
	
	/**
	 * TIME_IN_MILLISECOND float.
	 */
	float TIME_IN_MILLISECOND = 1 * 60 * 60 * 24 * 1000;

	/**
	 * xml file name for storing batch upload meta-data.
	 */
	String UPLOAD_BATCH_META_DATA_XML_FILE_NAME = "UploadBatchMetaData.xml";
	
	/**
	 * Constant for recostar license filename.
	 */
	String RECOSTAR_LICENSE_FILENAME = "recostar_license_filename";
	
	/**
	 * Constant for empty string.
	 */
	String EMPTY_STRING = "";

}
