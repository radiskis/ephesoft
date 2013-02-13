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

package com.ephesoft.dcma.cmis.constant;
/**
 * Constants class for all the constants used in the CMIS Export plugin.
 * 
 * @author Ephesoft
 * @version 1.0
 *
 */
public class CMISExportConstant {

	/**
	 * Enum for file extension.
	 * 
	 */
	public static enum UPLOADEXT {
		/**
		 * Upload extension type pdf.
		 */
		PDF("pdf"), 
		/**
		 * Upload extension type tif.
		 */
		TIF("tif");

		/**
		 * Private string to store upload file extension.
		 */
		private String uploadFileExt;

		UPLOADEXT(String uploadFileExt) {
			this.uploadFileExt = uploadFileExt;
		}

		/**
		 * @return {@link String}
		 */
		public String getUploadFileExt() {
			return this.uploadFileExt;
		}
	}
	
	/**
	 * Constant for stating "Could not update properties".
	 */
	public static final String COULD_NOT_UPDATE_PROPERTIES = "Could not update properties";
	
	/**
	 * Constant for stating "Unable to close the input stream ".
	 */
	public static final String UNABLE_TO_CLOSE_THE_INPUT_STREAM = "Unable to close the input stream ";

	/**
	 * Constant for stating "Custom error message from CMIS plug in : ".
	 */
	public static final String CUSTOM_ERROR_MESSAGE_FROM_CMIS_PLUGIN = "Custom error message from CMIS plugin : ";

	/**
	 * Constant for stating "Error while uploading document with identifier: ".
	 */
	public static final String ERROR_WHILE_UPLOADING_DOCUMENT_WITH_IDENTIFIER = "Error while uploading document with identifier: ";

	/**
	 * Constant for stating " and continuing.".
	 */
	public static final String AND_CONTINUING = " and continuing.";

	/**
	 * Constant for stating ". Skipping the aspect property: ".
	 */
	public static final String SKIPPING_THE_ASPECT_PROPERTY = ". Skipping the aspect property: ";

	/**
	 * Constant for stating " because it already exists on the repository".
	 */
	public static final String BECAUSE_IT_ALREADY_EXISTS_ON_THE_REPOSITORY = " because it already exists on the repository";

	/**
	 * Constant for stating "Deleting all the versions of the document: ".
	 */
	public static final String DELETING_ALL_THE_VERSIONS_OF_THE_DOCUMENT = "Deleting all the versions of the document: ";

	/**
	 * Constant for stating "Updating the added properties for :".
	 */
	public static final String UPDATING_THE_ADDED_PROPERTIES_FOR = "Updating the added properties for :";

	/**
	 * Constant for stating " having document type:".
	 */
	public static final String HAVING_DOCUMENT_TYPE = " having document type:";

	/**
	 * Constant for stating " for document id:".
	 */
	public static final String FOR_DOCUMENT_ID = " for document id:";

	/**
	 * Constant for stating " for document level field: ".
	 */
	public static final String FOR_DOCUMENT_LEVEL_FIELD = " for document level field: ";

	/**
	 * Constant for stating "Added aspect property: ".
	 */
	public static final String ADDED_ASPECT_PROPERTY = "Added aspect property: ";

	/**
	 * Constant for stating "Searching for existence of aspect property: ".
	 */
	public static final String SEARCHING_FOR_EXISTENCE_OF_ASPECT_PROPERTY = "Searching for existence of aspect property: ";

	/**
	 * Constant for stating " on CMIS repository".
	 */
	public static final String ON_CMIS_REPOSITORY = " on CMIS repository";

	/**
	 * Constant for stating ".".
	 */
	public static final String DOT = ".";

	/**
	 * Constant for stating "Adding aspect: ".
	 */
	public static final String ADDING_ASPECT = "Adding aspect: ";

	/**
	 * Constant for stating "/".
	 */
	public static final String FOLDER_SEPARATOR = "/";

	/**
	 * Constant for stating ". No such document level field exists:".
	 */
	public static final String NO_SUCH_DOCUMENT_LEVEL_FIELD_EXISTS = ". No such document level field exists:";

	/**
	 * Constant for stating " for document type: ".
	 */
	public static final String FOR_DOCUMENT_TYPE = " for document type: ";

	/**
	 * Constant for stating " defined for document id: ".
	 */
	public static final String DEFINED_FOR_DOCUMENT_ID = " defined for document id: ";

	/**
	 * Constant for stating "Unable to read aspect mapping from property file: ".
	 */
	public static final String UNABLE_TO_READ_ASPECT_MAPPING_FROM_PROPERTY_FILE = "Unable to read aspect mapping from property file: ";

	/**
	 * Constant for stating "Improper mapping in property file: ".
	 */
	public static final String IMPROPER_MAPPING_IN_PROPERTY_FILE = "Improper mapping in property file: ";

	/**
	 * Constant for stating ". No such aspect property exists: ".
	 */
	public static final String NO_SUCH_ASPECT_PROPERTY_EXISTS = ". No such aspect property exists: ";

	/**
	 * Constant for stating " defined in the property mapping file: ".
	 */
	public static final String DEFINED_IN_THE_PROPERTY_MAPPING_FILE = " defined in the property mapping file: ";

	/**
	 * Constant for stating "Unable to add aspect: ".
	 */
	public static final String UNABLE_TO_ADD_ASPECT = "Unable to add aspect: ";

	/**
	 * Constant for stating " for batch instance: ".
	 */
	public static final String FOR_BATCH_INSTANCE = " for batch instance: ";

	/**
	 * Constant for stating "ON".
	 */
	public static final String ON_STRING = "ON";
	
	/**
	 * Constant for stating "ON".
	 */
	public static final String STATE = "ON";

	/**
	 * Constant for stating "Converting".
	 */
	public static final String CONVERTING = "Converting";

	/**
	 * Constant for stating "CMIS_EXPORT".
	 */
	public static final String CMIS_EXPORT_PLUGIN = "CMIS_EXPORT";

	/**
	 * Constant for image mime type "image/tiff".
	 */
	public static final String IMAGE_MIME_TYPE = "image/tiff";

	/**
	 * Constant for pdf mime type "application/pdf".
	 */
	public static final String PDF_MIME_TYPE = "application/pdf";

	/**
	 * Constant for stating "The WSDL URL of the CMIS Navigation Service must be specified as the value of the \"dcma-cmis.properties\"configuration file property \" ".
	 */
	public static final String EXCEPTION_MESSAGE_CONSTANT_1 = "The WSDL URL of the CMIS Navigation Service must be specified as the value of the \"dcma-cmis.properties\"configuration file property \" ";
	
	/**
	 * Constant for stating " when WS-Security is specified as the CMIS security mode.HTTP Basic Authentication will be used by default.".
	 */
	public static final String EXCEPTION_MESSAGE_CONSTANT_2 = " when WS-Security is specified as the CMIS security mode.HTTP Basic Authentication will be used by default.";
	
	/**
	 * Constant for server url .
	 */
	public static final String SERVER_URL = "{serverURL}";
	
	/**
	 * Constant for "false".
	 */
	public static final String FALSE = "false";
	
	/**
	 * Constant for stating problem during uploading pdf file.
	 */
	public static final String PROBLEM_UPLOADING_PDF_FILE = "Problem uploading PDF file : ";

	/**
	 * Constant for stating statement "Unable to upload the document : ".
	 */
	public static final String UNABLE_TO_UPLOAD_THE_DOCUMENT = "Unable to upload the document : ";

	
	/**
	 * Parameter start delimiter.
	 */
	public static final String PARAM_START_DELIMETER = "$";

	/**
	 * Constant for batch id.
	 */
	public static final String EPHESOFT_BATCH_ID = "EphesoftBatchID";

	/**
	 * Constant for document id in file name.
	 */
	public static final String EPHESOFT_DOCUMENT_ID = "EphesoftDOCID";

	/**
	 * Separator between the parameters specified by admin.
	 */
	public static final String FILE_FORMAT_SEPARATOR = "&&";
	
	/**
	 * Error message for authentication failure.
	 */
	public static final String CMIS_AUTHENTICATION_FAIL = "cmis_authentication_fail";

	/**
	 * Error message when repository is not found.
	 */
	public static final String CMIS_REPOSITORY_NOT_FOUND = "cmis_repository_not_found";
	
	/**
	 * Error message for connection establishment failure with cmis.
	 */
	public static final String CMIS_CONNECTION_FAIL = "cmis_connection_fail";

	/**
	 * Error message for empty cmis connection properties.
	 */
	public static final String CMIS_CONNECTION_EMPTY_PROPERTIES = "cmis_connection_empty_properties";
	
	/**
	 * Error message for cmis unauthorized access.
	 */
	public static final String CMIS_UNAUTHORIZED_ACCESS = "cmis_unauthorized_access";

}
