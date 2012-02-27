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

package com.ephesoft.dcma.gwt.batchInstance.client.i18n;

import com.ephesoft.dcma.gwt.core.client.i18n.LocaleCommonConstants;

public interface BatchInstanceConstants extends LocaleCommonConstants {

	String BUTTON_RESTART_BATCH = "restart_batch_button";

	String BUTTON_DELETE_BATCH = "delete_batch_button";

	String STYLE_BUTTON = "gwt-Button";

	String LABEL_TABLE_ALL = "label_table_all";

	String TEXT_BATCH_PRIORITY_URGENT = "batch_priority_urgent";

	/**
	 * Value to be shown when a batch has priority high.
	 */
	String TEXT_BATCH_PRIORITY_HIGH = "batch_priority_high";

	/**
	 * Value to be shown when a batch has priority medium.
	 */
	String TEXT_BATCH_PRIORITY_MEDIUM = "batch_priority_medium";

	/**
	 * Value to be shown when a batch has priority low.
	 */
	String TEXT_BATCH_PRIORITY_LOW = "batch_priority_low";

	String TEXT_BATCH_STATUS_NEW = "batch_status_new";

	String TEXT_BATCH_STATUS_LOCKED = "batch_status_locked";

	String TEXT_BATCH_STATUS_SUSPEND = "batch_status_suspend";

	String TEXT_BATCH_STATUS_READY = "batch_status_ready";

	String TEXT_BATCH_STATUS_ERROR = "batch_status_error";

	String TEXT_BATCH_STATUS_FINISHED = "batch_status_finished";

	String TEXT_BATCH_STATUS_ASSIGNED = "batch_status_assigned";

	String TEXT_BATCH_STATUS_OPEN = "batch_status_open";

	String TEXT_BATCH_STATUS_RUNNING = "batch_status_running";

	String TEXT_BATCH_STATUS_READY_FOR_REVIEW = "batch_status_ready_for_review";

	String TEXT_BATCH_STATUS_READY_FOR_VALIDATION = "batch_status_ready_for_validation";

	String TEXT_BATCH_STATUS_RESTARTED = "batch_status_restarted";

	String TEXT_BATCH_STATUS_DELETED = "batch_status_deleted";

	String LABEL_TABLE_COLUMN_PRIORITY = "label_table_column_priority";

	/**
	 * Used in the label for table column batchId.
	 */
	String LABEL_TABLE_COLUMN_BATCHID = "label_table_column_batchId";

	/**
	 * Used in the label for table column batch name.
	 */
	String LABEL_TABLE_COLUMN_BATCHNAME = "label_table_column_batchName";

	/**
	 * Used in the label for table column batch class name.
	 */
	String LABEL_TABLE_COLUMN_BATCHCLASSNAME = "label_table_column_batchClassName";

	/**
	 * Used in the label for table column batch update date.
	 */
	String LABEL_TABLE_COLUMN_BATCHUPDATEDON = "label_table_column_batchUpdatedOn";

	String LABEL_TABLE_COLUMN_STATUS = "label_table_column_status";

	String TOTAL_BATCHES = "total_batches";

	String DELETED_BATCHES = "deleted_batches";

	String RESTARTED_BATCHES = "restarted_batches";

	String BATCH_ALERTS = "batch_alerts";

	String COLON = ":";

	String DATE_FORMAT = "dd-MMM-yyyy HH:mm:ss";

	String PROPERTIES_DIRECTORY = "properties";

	String SEPERATOR = "-";

	String BUTTON_SEARCH_BATCH = "search_batch_button";

	String LABEL_SEARCH_BATCH = "search_batch_label";

	String LABEL_TABLE_DEFAULT = "label_table_default";

	String BUTTON_REFRESH = "refresh_button";

	String DISABLED_BUTTON_STYLE = "disableButton";

	String SELECT = "select";

	String FAILURE = "failure";

	String RESTART_TITLE = "restart_title";

	String PRIORITY = "priority";

	String STATUS = "status";

	String LABEL_TABLE_COLUMN_IS_REMOTE = "label_table_column_is_remote";

	String TEXT_BATCH_STATUS_TRANSFERRED = "batch_status_transferred";

	String SEARCH_CRITERIA = "search_criteria";

	/**
	 * Folder Import Module Workflow Name
	 */
	String FOLDER_IMPORT_MODULE = "Folder_Import_Module";

	String SEMICOLON = ";";

	String WORKFLOW_CONTINUE_CHECK = "Workflow_Continue_Check";

	String RESTART_ERROR_MESSAGE = "restart_error_message";

	String EMPTY_VALUE = "";

	String TEXT_RESTART_IN_PROGRESS = "restart_in_progress";
	
	/**
	 * Used in the label for current user for a batch on batch instance management screen.
	 */
	String LABEL_CURRENT_USER = "label_current_user";
	
	String BUTTON_RESTART_ALL = "restart_all_button";
	
	String BUTTON_UNLOCK = "unlock_button";
	
	String NEXT_TEXT = "next_text";
	
	String PREVIOUS_TEXT = "previous_text";

}
