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

package com.ephesoft.dcma.gwt.admin.bm.client.i18n;

import com.ephesoft.dcma.gwt.core.client.i18n.LocaleCommonMessages;

/**
 * The interface is used to define all the messages used in the Batch Class page and support internationalization.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.i18n.LocaleCommonMessages
 */
public interface BatchClassManagementMessages extends LocaleCommonMessages {

	/**
	 * The message to show for confirmation of document type deletion.
	 */
	String DELETE_DOCUMENT_TYPE_CONFORMATION = "delete_document_type_conformation";

	/**
	 * The message to show for confirmation of field type deletion.
	 */
	String DELETE_FIELD_TYPE_CONFORMATION = "delete_field_type_conformation";

	/**
	 * The message to show for confirmation of scanner deletion.
	 */
	String DELETE_SCANNER_CONFORMATION = "delete_scanner_configuration_conformation";

	/**
	 * The message to show for confirmation of field type deletion.
	 */
	String DELETE_FUNCTION_KEY_CONFORMATION = "delete_function_key_conformation";

	/**
	 * The message to show for confirmation of page type deletion.
	 */
	String DELETE_PAGE_TYPE_CONFORMATION = "delete_page_type_conformation";

	/**
	 * The message to show for confirmation of key value type deletion.
	 */
	String DELETE_KV_TYPE_CONFORMATION = "delete_kv_type_conformation";

	/**
	 * The message to show for confirmation of regex type deletion.
	 */
	String DELETE_REGEX_TYPE_CONFORMATION = "delete_regex_type_conformation";

	/**
	 * Error message to show when user enters nothing in a mandatory field.
	 */
	String BLANK_ERROR = "blank_error";

	/**
	 * Error message shown when a non-number is entered in a field that expects number.
	 */
	String NUMBER_ERROR = "number_error";

	/**
	 * FIELD_ORDER_DUPLICATE_ERROR String.
	 */
	String FIELD_ORDER_DUPLICATE_ERROR = "field_order_duplicate_error";

	/**
	 * Error message shown when a non-number is entered in a field that expects float.
	 */
	String FLOAT_ERROR = "float_error";

	/**
	 * Error message shown when a non-number is entered in a field that expects integer.
	 */
	String INTEGER_ERROR = "integer_error";

	/**
	 * Error message shown when duplicate name is entered.
	 */
	String NAME_COMMON_ERROR = "name_common_error";
	/**
	 * Error message shown when duplicate name is entered for batch class field.
	 */
	String BATCH_CLASS_FIELD_NAME_COMMON_ERROR = "batch_class_field_name_common_error";

	/**
	 * Message shown when field type addition is expected.
	 */
	String ADD_FIELD_TYPE = "add_field_type";

	/**
	 * Message shown when document type addition is expected.
	 */
	String ADD_DOCUMENT_TYPE = "add_document_type";

	/**
	 * Message shown when no record is found in database.
	 */
	String NO_RECORD_FOUND = "no_record_found";

	/**
	 * Message shown when some field is expected to be selected.
	 */
	String NONE_SELECTED_WARNING = "none_selected_warning";

	/**
	 * Message shown when a field cannot be edited.
	 */
	String NOT_EDITABLE_WARNING = "not_editable_warning";

	/**
	 * Message shown when a field is not mapped yet.
	 */
	String NOT_MAPPED = "not_mapped";

	/**
	 * Message shown when there is no record to delete.
	 */
	String NO_RECORD_TO_DELETE = "no_record_to_delete";

	/**
	 * Message shown when there is no record to edit.
	 */
	String NO_RECORD_TO_EDIT = "no_record_to_edit";

	/**
	 * DELETE_EMAIL_CONFIGURATION_CONFORMATION String.
	 */
	String DELETE_EMAIL_CONFIGURATION_CONFORMATION = "delete_email_configuration_conformation";

	/**
	 * DELETE_BATCH_CLASS_FIELD_CONFORMATION String.
	 */
	String DELETE_BATCH_CLASS_FIELD_CONFORMATION = "delete_batch_class_field_conformation";

	/**
	 * ALREADY_EXISTS_ERROR.
	 */
	String ALREADY_EXISTS_ERROR = "already_exists_error";

	/**
	 * APPLY_SUCCESSFUL String.
	 */
	String APPLY_SUCCESSFUL = "apply_successful";

	/**
	 * SAVE_SUCCESSFUL String.
	 */
	String SAVE_SUCCESSFUL = "save_sucessful";

	/**
	 * SUCCESS String.
	 */
	String SUCCESS = "success";

	/**
	 * The message to show for confirmation of table Info deletion.
	 */
	String DELETE_TABLE_INFO_CONFORMATION = "delete_table_info_conformation";

	/**
	 * Message shown when field type addition is expected.
	 */
	String ADD_TABLE_INFO = "add_table_info";

	/**
	 * The message to show for confirmation of table column info deletion.
	 */
	String DELETE_TABLE_COLUMN_INFO_CONFORMATION = "delete_table_column_info_conformation";

	/**
	 * The message to show for confirmation of batch class deletion.
	 */
	String DELETE_BATCH_CLASS_CONFORMATION = "delete_batch_class_conformation";

	/**
	 * The message to show for deletion of success title.
	 */
	String DELETE_SUCCESS_TITLE = "delete_success_title";

	/**
	 * The message to show for deletion of success message.
	 */
	String DELETE_SUCCESS_MESSAGE = "delete_success_message";

	/**
	 * The message to show for error in retrieving path.
	 */
	String ERROR_RETRIEVING_PATH = "error_retrieving_path";

	/**
	 * The message to show for error in case of mouse not clicked.
	 */
	String MOUSE_NOT_CLICK_ERROR = "mouse_not_click_error";

	/**
	 * The message to show for error in case of key not final.
	 */
	String KEY_NOT_FINAL_ERROR = "key_not_final_error";

	/**
	 * The message to show for error in case of multiplier.
	 */
	String MULTIPLIER_ERROR = "multiplier_error";

	/**
	 * The message for data loss.
	 */
	String DATA_LOSS = "data_loss";

	/**
	 * The message to show for error in case of advanced KV.
	 */
	String ADVANCED_KV_ERROR = "advanced_kv_error";

	/**
	 * The message to show in case of there is no record to copy.
	 */
	String NO_RECORD_TO_COPY = "no_record_to_copy";

	/**
	 * The message to show in case of there is no record to export.
	 */
	String NO_RECORD_TO_EXPORT = "no_record_to_export";

	/**
	 * The message to show in case of error in uploading image.
	 */
	String ERROR_UPLOAD_IMAGE = "error_upload_image";

	/**
	 * The message to show for confirmation of regex validation deletion.
	 */
	String DELETE_REGEX_VALIDATION_CONFIRMATION = "delete_regex_validation_confirmation";

	/**
	 * The message to show for failure in adding regex.
	 */
	String ADD_REGEX_FAILURE = "add_regex_failure";

	/**
	 * The message to show in case of there is no table to test.
	 */
	String NO_TABLE_TO_TEST = "no_table_to_test";

	/**
	 * The message to show in case of mandatory fields are blank.
	 */
	String MANDATORY_FIELDS_BLANK = "mandatory_fields_cannot_be_blank";

	/**
	 * The message to show in case of remote batch class identifier is empty.
	 */
	String REMOTE_BATCH_CLASSIDENTIFIER_CANT_EMPTY = "remote_batch_class_identifier_cannot_be_empty";

	/**
	 * The message to show in case of error while retaining the batch list priority.
	 */
	String ERROR_WHILE_RETAINING_THE_BATCH_LIST_PRIORITY = "error_while_retaining_the_batch_list_priority";

	/**
	 * The message to show in case of remote url is empty.
	 */
	String REMOTE_URL_CANT_BE_EMPTY = "remote_url_cannot_be_empty";

	/**
	 * The message to show that all details are necessary.
	 */
	String ALL_DETAILS_NECESSARY = "all_details_are_necessary";

	/**
	 * The message to show that all details are incomplete.
	 */
	String INCOMPLETE_DETAILS = "incomplete_details";

	/**
	 * The message to show that key is not allowed.
	 */
	String KEY_NOT_ALLOWED = "key_is_not_allowed";

	/**
	 * The message for unable to determine location.
	 */
	String UNABLE_TO_DETERMINE_LOCATION = "unable_to_determine_location";

	/**
	 * The message for value overlapping with key.
	 */
	String VALUE_OVERLAPS_WITH_KEY = "value_overlaps_with_key";

	/**
	 * The message to show that last record cannot be deleted.
	 */
	String NO_DELETE_LAST_RECORD = "cannot_delete_last_record";

	/**
	 * The message to show that own role cannot be deleted.
	 */
	String CANT_DELETE_OWN_ROLE = "cant_delete_own_role";

	/**
	 * The message to show that no UNC folder exists.
	 */
	String NO_UNC_FOLDER_EXISTS = "no_unc_folder_exists";

	/**
	 * The message to show that unsaved data will be lost.
	 */
	String UNSAVED_DATA_WILL_LOST = "unsaved_data_will_lost";

	/**
	 * The message to show that mandatory fields are missing.
	 */
	String MISSING_MANDATORY_FIELDS = "missing_mandatory_fields";

	/**
	 * The message to show that no key field selected.
	 */
	String NO_KEY_FIELD_SELECTED = "no_key_field_selected";

	/**
	 * The message to show that no extraction API selected.
	 */
	String NO_EXTRACTION_API_SELECTED = "no_extraction_api_selected";

	/**
	 * The message to show that there is no column to edit.
	 */
	String NO_COLUMN_TO_EDIT = "no_column_to_edit";

	/**
	 * The message to show that unable to read sample pattern file.
	 */
	String UNABLE_TO_READ_SAMPLE_PATTERN_FILE = "unable_to_read_sample_pattern_file";

	/**
	 * String constant to show delete CMIS account configuration warning message.
	 */
	String DELETE_CMIS_CONFIGURATION_CONFORMATION = "delete_cmis_configuration_conformation";

	/**
	 * VALIDATE_THE_REGEX_PATTERN String.
	 */
	String VALIDATE_THE_REGEX_PATTERN = "validate_the_regex_pattern";

	/**
	 * CHANGE_AND_TRY_AGAIN String.
	 */
	String CHANGE_AND_TRY_AGAIN = "change_and_try_again";

	/**
	 * INVALID_DOCUMENT_NAME_MESSAGE String.
	 */
	String INVALID_DOCUMENT_NAME_MESSAGE = "invalid_document_name";

	/**
	 * String constant to error message on entering negative field order value.
	 */
	String NEGATIVE_FIELD_ORDER_ERROR = "negative_field_order";

	/**
	 * INVALID_SYSTEM_FOLDER_PATH String.
	 */
	String INVALID_SYSTEM_FOLDER_PATH = "invalid_system_folder_path";

	/**
	 * String constant to show success message on cmis connection test.
	 */
	String CMIS_CONNECTION_SUCCESSFUL = "cmis_connection_successful";

}
