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

public interface AdminConstants {

	String DATABASE_TYPE_STRING = "STRING";
	String DATABASE_TYPE_DATE = "DATE";
	String DATABASE_TYPE_DOUBLE = "DOUBLE";
	String DATABASE_TYPE_LONG = "LONG";
	String DATABASE_TYPE_INTEGER = "INTEGER";
	String DATABASE_TYPE_FLOAT = "FLOAT";
	String DATABASE_TYPE_BIGDECIMAL = "BIGDECIMAL";

	String JAVA_TYPE_STRING = "String";
	String JAVA_TYPE_DATE = "Timestamp";
	String JAVA_TYPE_DOUBLE = "Double";
	String JAVA_TYPE_LONG = "Long";
	String JAVA_TYPE_INTEGER = "Integer";
	String JAVA_TYPE_FLOAT = "Float";
	String JAVA_TYPE_BIGDECIMAL = "BigDecimal";

	String DOCUMENT_TYPE = "document.type";
	String FIELD_TYPE = "field.type";
	String ROW_TYPE = "row.id";

	String ROWID = "RowId";

	String MAP_BUTTON = "Map";
	String EDIT_BUTTON = "Edit";
	String OK_BUTTON = "Ok";
	String CANCEL_BUTTON = "Cancel";
	String MAPPING_BUTTON = "Mapping";
	String BACK_BUTTON = "Back";
	String CLOSE_BUTTON = "Close";
	String SAVE_BUTTON = "Save";
	String APPLY_BUTTON = "Apply";
	String LEARN_FILES_BUTTON = "Learn Files";
	String LEARN_DB_BUTTON = "Learn DB";
	String GENERATE_FOLDERS_BUTTON = "Generate Folders";
	String DELETE_BUTTON = "Delete";
	String ADD_BUTTON = "Add";
	String BUTTON_STYLE = "gwt-Button";
	String VALIDATION_STYLE = "dateBoxFormatError";
	String DISABLED_BUTTON_STYLE = "disableButton";
	String FULL_WIDTH_STYLE = "fullWidth";
	String COPY_BUTTON = "Copy";
	String EXPORT_BUTTON = "Export";
	String IMPORT_BUTTON = "Import";
	String IMPORT_FILE = "Import File";
	String ERROR_CODE_TEXT = "error";
	String DEPLOY_BUTTON = "Deploy Workflow";
	String EDIT_LIST_BUTTON = "Configure";

	String DATABASE_DRIVER = "fuzzydb.database.driver";
	String DATABASE_URL = "fuzzydb.database.connectionURL";
	String DATABASE_USERNAME = "fuzzydb.database.userName";
	String DATABASE_PASSWORD = "fuzzydb.database.password";

	String FUZZY_DB_PLUGIN = "FuzzyDB";

	String DOCUMENT_TYPE_UNKNOWN = "unknown";

	String EMPTY_STRING = "";

	String NO = "No";
	String YES = "Yes";
	
	String AND = ",";
	String OR = "/";

	String PLUGIN_CONFIGURATION_HTML = "<b>Plugin Configuration</b>";
	String DATABASE_MAPPING_HTML = "<b>Database Mapping</b>";
	String PLUGIN_DETAILS_HTML = "<b>Plugin Details</b>";
	String DOCUMENT_DETAILS_HTML = "<b>Document Details</b>";
	String FIELD_DETAILS_HTML = "<b>Document Index Field Details</b>";
	String BATCH_CLASS_CONFIGURATION_HTML = "<b>Batch class configuration</b>";
	String KVEXTRACTION_DETAILS_HTML = "<b>KV Extraction Configuration</b>";
	String TABLE_INFO_DETAILS_HTML = "<b>Table Info Configuration</b>";
	String TCINFO_DETAILS_HTML = "<b>Table Column Info Configuration</b>";
	String DOCUMENT_CONFIGURATION_HTML = "<b>Document Type configuration</b>";
	String EMAIL_CONFIGURATION_HTML = "<b>Email configuration</b>";
	String REGEX_CONFIGURATION_HTML = "<b>Regular Expression configuration</b>";
	String MODULE_CONFIGURATION = "<b>Module configuration</b>";
	String DOCUMENT_TYPE_HTML = "<b>Document Type</b>";
	String FIELD_MAPPING_HTML = "<b>Field Mapping</b>";

	String DATABASE_MAPPING = "Database Mapping";
	String TABLE_MAPPING = "Table Mapping";

	String NAME = "Name:";
	String DESCRIPTION = "Description:";
	String MINCONF = "Minimum Confidence Threshold:";
	String PRIORITY = "Priority:";
	String UNC_FOLDER = "UNC Folder:";

	String REVIEW_DOCUMENT = "Review Document";
	String VALIDATE_DOCUMENT = "Validate Document";

	String EDIT_BUTTON_STYLE = "editButton";
	String VIEW_BUTTON_STYLE = "viewButton";
	String DELETE_BUTTON_STYLE = "deleteButton";
	String BLANK_IMAGE_STYLE = "blankImage";
	String BOLD_TEXT_STYLE = "bold_text";
	String FONT_RED_STYLE = "font_red";
	String PADDING_STYLE = "padd";
	String PADDING_STYLE_TOPPADD = "topPadd";

	String COLON = ":";
	String DOUBLE_ARROW = " >>";
	String STAR = "*";

	String VERSION = "1.0.0.0";

	String KV_EXTRACTION = "KV Extraction";
	String REGEX = "Regex";
	String NEW_DOCUMENT = "New Document";
	String NEW_FIELD_TYPE = "New Field Type";
	String NEW_TABLE_INFO_TYPE = "New Table Info Type";
	String NEW_TABLE_COLUMN_INFO_TYPE = "New Table Column Info Type";
	String NEW_KV_EXTRACTION = "New KV Extraction";
	String NEW_REGEX = "New Regex";
	String NEW_EMAIL = "New Email";
	String FOLDER_NAME = "inbox";
	String KV_PAGE_PLUGIN = "KV Page Process";
	String CONFIG_BUTTON = "Configure";
	String KV_PAGE_PLUGIN_DESCRIPTION = "KV Patterns for Page Process";

	/**
	 * Lower limit on priority value possible.
	 */
	int PRIORITY_LOWER_LIMIT = 1;

	/**
	 * Upper limit on priority value possible.
	 */
	int PRIORITY_UPPER_LIMIT = 100;

	String TEST_KV_BUTTON = "Test KV";

	String KV_PP_DETAILS_HTML = "<b>KV Page Process Configuration</b>";

	String TOOLTIP_TEST_KV_BUTTON = "Key Value based Testing on Sample Data";

	String KV_PAGE_PROCESS_KV_PATTERN = "kvpageprocess.key_value_patterns";

	String MIN_CONFIDENCE_THRESHOLD = "Min Confidence";

	String ADVANCE_OPTION_BUTTON = "Advance Options";

	String BORDER_STYLE = "border";

	String ADVANCED_KV_EXTRACTION = "Advanced KV Extraction";

	String ADD_ADVANCED_KV_EXTRACTION_BUTTON = "Adv.Add";

	String EDIT_ADVANCED_KV_EXTRACTION_BUTTON = "Adv.Edit";

	String TEST_TABLE_BUTTON = "Test Table";

	String BATCH_CLASS_FIELD_HTML = "<b>Batch Class Field</b>";

	String FUNCTION_KEY_HTML = "<b>Function Key</b>";

	String ATTACH_BUTTON = "Attach";

	String SAVE_BATCH_TITLE = "Save Batch";

	String BATCH_CLASS_FIELD_DATATYPE = "datatype";
	String BATCH_CLASS_FIELD_NAME = "name";
	String BATCH_CLASS_FIELD_ORDER_NUMBER = "fieldOrderNumber";
	String BATCH_CLASS_FIELD_DESCRIPTION = "description";

	String DOCUMENT_TYPE_NAME = "name";
	String DOCUMENT_TYPE_DESCRIPTION = "description";

	String EMAIL_USERNAME = "username";
	String EMAIL_PASSWORD = "password";
	String EMAIL_SERVER_NAME = "serverName";
	String EMAIL_SERVER_TYPE = "serverType";
	String EMAIL_FOLDER_NAME = "folderName";
	String EMAIL_PORTNUMBER = "portNumber";

	String MODULE_NAME = "name";
	String MODULE_DESCRIPTION = "description";

	String PLUGIN_NAME = "name";
	String PLUGIN_DESCRIPTION = "description";

	/**
	 * Maximum length of text that can be shown in listBox beyond which it will be trimmed.
	 */
	int MAX_TEXT_LENGTH = 26;

	int INITIAL_ORDER_NUMBER = 1;
	
	int ORDER_NUMBER_OFFSET = 10;
	
	String SWITCH_OFF = "OFF";
	String SWITCH_ON = "ON";
	
	String SAMPLE_REGEX_BUTTON="Sample Regex";
	String SAMPLE_REGEX_PATTRENS="Sample Regular Expression Patterns";
	String SAMPLE_REGEX_PATTERN_TITLE="Sample Regular Expressions";
}
