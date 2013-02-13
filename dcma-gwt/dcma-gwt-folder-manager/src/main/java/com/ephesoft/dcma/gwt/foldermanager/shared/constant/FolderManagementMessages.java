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

package com.ephesoft.dcma.gwt.foldermanager.shared.constant;

public interface FolderManagementMessages {

	String UNABLE_TO_FETCH_BATCH_CLASSES_BY_ROLES_ASSIGNED_TO_USER = "unable_to_fetch_batch_classes_by_roles_assigned_to_user";
	String UNABLE_TO_FETCH_BASE_HTTP_URL = "unable_to_get_base_http_url";
	String UNABLE_TO_FETCH_BASE_FOLDER_PATH = "unable_to_get_base_folder_path";

	String UNABLE_TO_LOAD_FOLDER_ITEMS = "unable_to_load_folder_items";
	String LOADING = "loading";
	String RENAME_TO = "rename_to";

	String UNABLE_TO_DELETE_THE_FILE = "unable_to_delete_the_file";
	String SUCCESSFULLY_DELETED_THE_FILE = "successfully_deleted_the_file";

	String ARE_YOU_SURE_YOU_WANT_TO_DELETE_THE_FILE = "are_you_sure_you_want_to_delete_the_file";
	String FAILED_TO_RENAME_FILE = "failed_to_rename_file";
	String SUCCESSFULLY_RENAMED_FILE = "successfully_renamed_file";
	String COULD_NOT_OPEN_THE_FILE = "could_not_open_the_file";
	String UNABLE_TO_COMPLETE_CUT_PASTE_OPERATION = "unable_to_complete_cut_paste_operation";
	String UNABLE_TO_COMPLETE_COPY_PASTE_OPERATION = "unable_to_complete_copy_paste_operation";
	String PASTING = "pasting";

	String UNABLE_TO_PERFORM_DELETE_OPERATION = "unable_to_perform_delete_operation";
	String DELETE_SUCCESSFUL = "delete_success";
	String UNABLE_TO_DELETE_THE_FOLLOWING = "unable_to_delete_the_following";
	String PLEASE_WAIT_WHILE_THE_SELECTED_ITEMS_ARE_DELETED = "please_wait_while_deletion_in_progress";
	String CONFIRM_DELETE_OPERATION = "confirm_delete_operation";
	String ARE_YOU_SURE_YOU_WANT_TO_DELETE_ALL_THE_SELECTED_FILES = "confirm_deletion";
	String MULTI_SELECT_OPTIONS = "multi_select_options";
	String LIST_OF_FILES_ATTACHED = "list_of_files_attached";
	String NO_FILES_ATTACHED = "no_files_attached";
	String UPLOADING_SELECTED_FILES = "uploading_selected_files";

	String NO_FILES_FOLDERS_SELECTED_FOR_OPERATION = "no_folder_selected_for_operation";

	String SUCCESSFULLY_CREATED_THE_NEW_FOLDER = "new_folder_creation_success";
	String FAILED_TO_CREATE_A_NEW_FOLDER = "new_folder_creation_failure:";
	String PLEASE_WAIT_WHILE_A_NEW_FOLDER_IS_CREATED = "new_folder_creation_wait";
	String VIEW_ATTACHED_FILES = "view_attached_files";
	String NO_FILES_ATTACHED_FOR_UPLOAD = "no_files_attached_for_upload";
	String UNABLE_TO_CHECK_IF_USER_IS_SUPER_ADMIN = "problem_in_checking_if_user_is_super_admin";

	String CANNOT_COMPLETE_CUT_PASTE_OPERATION_AS_THE_FILE_FOLDER = "Cannot complete cut-paste operation as the file/folder: \"";
	String EXCEPTION_OCCURRED_WHILE_COPY_PASTE_OPERATION_COULD_NOT_COMPLETE_OPERATION = "Exception occurred while copy-paste operation. Could not complete operation.";
	String ALREADY_EXISTS = "\" already exists.";
	String CANNOT_COMPLETE_COPY_PASTE_OPERATION_AS_THE_FILE_FOLDER = "Cannot complete copy-paste operation as the file/folder: \"";
	String EXCEPTION_OCCURRED_WHILE_CUT_PASTE_OPERATION_COULD_NOT_COMPLETE_OPERATION = "Exception occurred while cut-paste operation. Could not complete operation.";
	String PROBLEM_OCCURRED_WHILE_CREATING_THE_FOLDER = "Problem occurred while creating the folder: ";
	String EXCEPTION_OCCURRED_WHILE_COPY_PASTE_OPERATION_FILE_NOT_FOUND = "Exception occurred while copy-paste operation. Could not find the file/folder : ";
	String EXCEPTION_OCCURRED_WHILE_CUT_PASTE_OPERATION_FILE_NOT_FOUND = "Exception occurred while cut-paste operation. Could not find the file/folder : ";
	String NO_BATCH_CLASSES_FOUND_FOR_USER = "no_batch_classes_found_for_user";
	String NO_PARENT_FOLDER = "no_parent_folder";
	
	/**
	 * String constant for message to be shown when no content is present for selected folder.
	 */
	String NO_CONTENT_MESSAGE = "no_content_message";
}
