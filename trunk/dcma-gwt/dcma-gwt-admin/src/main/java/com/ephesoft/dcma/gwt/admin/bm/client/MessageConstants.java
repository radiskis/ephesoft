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

package com.ephesoft.dcma.gwt.admin.bm.client;

public interface MessageConstants {

	String MANDATORY_FIELDS_ERROR_MSG = "Mandatory fields cannot be blank.";
	String BATCH_CLASS_LIST_ERROR_MSG = "Error retrieving Batch Class List.";
	String BATCH_CLASS_ERROR_MSG = "Error retrieving batch class.";
	String SAMPLE_FOLDER_GENERATION_ERROR = "Cannot generate Sample folder.";
	String SAMPLE_FOLDER_GENERATION_SUCCESSFUL = "Sample folders generated successfully.";

	String PROBLEM_LEARNING_FILES = "Problem learning files.";
	String FILES_LEARNED_SUCCESSFULLY = "Files learned successfully.";
	String UNABLE_TO_GET_BATCH_CLASS = "Unable to get Batch Class from database.";
	String PERSISTANCE_ERROR = "Unable to persist information";
	String PROBLEM_LEARNING_DB = "DB configuration is not correct";
	String DB_LEARNED_SUCCESSFULLY = "Database learned successfully.";
	String PROBLEM_FETCHING_BATCH_CLASSES = "Problem in fetching Batch Classes.";

	String CONFIRMATION_MESSAGE = "Please save your changes first.";

	String UNABLE_TO_ACQUIRE_LOCK = "Unable to acquire lock on Batch Class for batch class id : ";
	String UNABLE_TO_RETRIEVE_FIELDS = "Could not retrieve doc level fields.";
	String UNABLE_TO_RETRIEVE_COLUMNS = "Could not retrieve columns.";
	String DATATYPE_COMPATIBILITY_CHECK = "Datatype of column selected and field should be compatible.";
	String NO_ROW_ID_SET = "No row Id set for document - ";
	String INVALID_DB_CONNECTION = "Invalid Database connection settings.";
	String ROWID_SET_INCORRECTLY = "RowId should be mapped to a Primary Key field";

	String UNC_PATH_NOT_UNIQUE = "UNC Path is either not unique or is empty.";
	String UNC_PATH_NOT_VERIFIED = "Unable to verify UNC Path.";
	String BATCH_CLASS_COPY_CREATED_SUCCESSFULLY = "Copy of Batch Class created successfully.";
	String UNABLE_TO_CREATE_COPY = "Unable to create the copy.";
	String BATCH_CLASS_COPY = "Copy Batch Class";
	String PRIORITY_SHOULD_BE_NUMERIC = "Priority should be numeric.";
	String CONFIDENCE_SHOULD_BE_NUMERIC = "Confidence should be numeric.";
	String PRIORITY_SHOULD_BE_BETWEEN_1_AND_100 = "Priority should be between 1 to 100.";
	String COPY_SUCCESSFUL = "Copy Successfull";

	String TITLE_TEST_SUCCESS = "Test Successful";
	String TITLE_TEST_FAILURE = "Test Failed";

	String MSG_TEST_KV_FAILURE = "Testing KV Extraction Failed. Please see logs for more details.";

	String MSG_NO_RESULTS_FOUND = "No Results Found.";

	String NOTHING_SELECTED = "Please select atleast one user group";

	String BATCH_CLASS_EXPORT_SUCCESSFULLY = "Batch class exported successfully.";
	String BATCH_CLASS_IMPORTED_SUCCESSFULLY = "Batch class imported successfully.";
	String UNABLE_TO_CREATE_UNC = "Unable to create UNC folder.";
	String UNABLE_TO_EXPORT = "Unable to export the batch Class.";
	String EXPORT_SUCCESSFUL = "Export successful";
	String IMPORT_SUCCESSFUL = "Import successful";
	String BATCH_CLASS_IMPORT = "Import Batch Class";
	String BATCH_CLASS_EXPORT = "Export Batch Class";
	String BATCH_NAME_INVALID = "Batch class name is either empty or no workflow exists for this batch class name.";
	String IMPORT_FILE_INVALID_TYPE = "Please select a zip batch file.";
	String IMPORT_UNSUCCESSFUL = "Error occurred while importing. Please try again";
	String EXPORT_UNSUCCESSFUL = "Error occurred while exporting. Please try again";
	String UNC_PATH_NOT_UNIQUE_CONFIRMATION = "UNC path is not unique. Please confirm do you wish to override the existing batch Class.";
	String UNC_PATH_NOT_BEGIN_WITH_BASEFOLDER = "UNC path should be in the context of base path.Please enter a valid folder location.";
	String DOCUMENT_TYPE_COPY = "Copy Document Type";
	String DOCUMENT_NAME_SAME_ERROR = "Cannot make a copy of the document. A document with same name already exists.";
	String DOCUMENT_DESCRIPTION_SAME_ERROR = "Cannot make a copy of the document. A document with same description already exists.";
	String COPY_FAILURE = "Copy Failed";
	String DOCUMENT_COPY_FAILURE = "Failed to create copy of document";
	String DOCUMENT_COPY_CREATED_SUCCESSFULLY = "Document Copy created successfully";

	String UPLOAD_IMAGE_INVALID_TYPE = "Supported formats are 'tiff' and 'tif'.Please select a valid file type.";
	String TEST_TABLE_RESULT_HEADER = "Test Table Result";
	String ERROR_FIELD_TYPE_MAPPING = "Atleast one field type should be mapped";
	String NO_CONFIG_SELEDCTED = "Atleast one configuration should be selected to be overriden from zip file.Please try again.";
	String NO_ZIP_ATTACHED = "Please attach a zip batch file first.";
}
