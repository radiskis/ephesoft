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

package com.ephesoft.dcma.gwt.admin.bm.client.i18n;

import com.ephesoft.dcma.gwt.core.client.i18n.LocaleCommonMessages;

/**
 * 
 * @author Ephesoft
 *
 */
/**
 * The interface is used to define all the messages used in the Batch Class page and support internationalization.
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

	String DELETE_EMAIL_CONFIGURATION_CONFORMATION = "delete_email_configuration_conformation";

	String DELETE_BATCH_CLASS_FIELD_CONFORMATION = "delete_batch_class_field_conformation";

	String ALREADY_EXISTS_ERROR = "already_exists_error";

	String APPLY_SUCCESSFUL = "apply_successful";

	String SAVE_SUCCESSFUL = "save_sucessful";

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

	String DELETE_BATCH_CLASS_CONFORMATION = "delete_batch_class_conformation";

	String DELETE_SUCCESS_TITLE = "delete_success_title";

	String DELETE_SUCCESS_MESSAGE = "delete_success_message";

	String ERROR_RETRIEVING_PATH = "error_retrieving_path";

	String MOUSE_NOT_CLICK_ERROR = "mouse_not_click_error";

	String KEY_NOT_FINAL_ERROR = "key_not_final_error";

	String MULTIPLIER_ERROR = "multiplier_error";

	String DATA_LOSS = "data_loss";

	String ADVANCED_KV_ERROR = "advanced_kv_error";

	String NO_RECORD_TO_COPY = "no_record_to_copy";

	String ERROR_UPLOAD_IMAGE = "error_upload_image";
	
	String DELETE_REGEX_VALIDATION_CONFIRMATION = "delete_regex_validation_confirmation";
	
	String ADD_REGEX_FAILURE = "add_regex_failure";

	String NO_TABLE_TO_TEST = "no_table_to_test";

}
