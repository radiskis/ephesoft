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

package com.ephesoft.dcma.gwt.home.client.i18n;

import com.ephesoft.dcma.gwt.core.client.i18n.LocaleCommonConstants;

/**
 * The interface is used to define all the constants used in the Batch List page and support internationalization.
 * 
 * @author Ephesoft
 * 
 */
public interface BatchListConstants extends LocaleCommonConstants {

	/**
	 * The title of the batch list page.
	 */
	String BATCH_LIST_TITLE = "batchList_title";

	/**
	 * The title of the tab which points to home i.e to batch list page.
	 */
	String TAB_LABEL_HOME = "tabLabel_home";

	/**
	 * The title of the tab which points to batch detail.
	 */

	String TAB_LABEL_BATCH_DETAIL = "tabLabel_batch_detail";

	/**
	 * The title of the tab which points to web scanner page.
	 */

	String TAB_LABEL_WEB_SCANNER = "tabLabel_web_scanner";

	/**
	 * The title of the tab which points to upload batch page.
	 */

	String TAB_LABEL_UPLOAD_BATCH = "tabLabel_upload_batch";
	
	/**
	 * Used in the labels for batch alerts.
	 */
	String LABEL_BATCH_ALERTS = "label_batch_alerts";

	/**
	 * Used in the the labels for batches that are in review state.
	 */
	String LABEL_PEND_FOR_REVIEW = "label_pend_for_review";

	/**
	 * Used in the the labels for batches that are in validate state.
	 */
	String LABEL_PEND_FOR_VALIDATION = "label_pend_for_validation";

	/**
	 * Used in the the labels for all batches.
	 */
	String LABEL_TOTAL_BATCHES = "label_total_batches";

	/**
	 * Used in the label for start review and start validate buttons.
	 */
	String LABEL_BUTTON_START_REV_VALIDATION = "label_button_start_rev_validation";

	/**
	 * Used in the label for table for a batches.
	 */
	String LABEL_TABLE_BATCHES = "label_table_batches";

	/**
	 * Used in the label for table for all batches.
	 */
	String LABEL_TABLE_ALL = "label_table_all";

	/**
	 * Used in the label for table column priority.
	 */
	String LABEL_TABLE_COLUMN_PRIORITY = "label_table_column_priority";

	/**
	 * Used in the label for table column priority whose value is urgent.
	 */
	String LABEL_TABLE_PRIORITY_VALUE_URGERNT = "label_table_priority_value_urgent";

	/**
	 * Used in the label for table column priority whose value is high.
	 */
	String LABEL_TABLE_PRIORITY_VALUE_HIGH = "label_table_priority_value_high";

	/**
	 * Used in the label for table column priority whose value is medium.
	 */
	String LABEL_TABLE_PRIORITY_VALUE_MEDIUM = "label_table_priority_value_medium";

	/**
	 * Used in the label for table column priority whose value is low.
	 */
	String LABEL_TABLE_PRIORITY_VALUE_LOW = "label_table_priority_value_low";

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

	/**
	 * Used in the label for table column batch status.
	 */
	String LABEL_TABLE_COLUMN_BATCHSTATUS = "label_table_column_batchStatus";

	/**
	 * Used in the label for table column batch status whose value is review.
	 */
	String LABEL_TABLE_REVIEW = "label_table_review";

	/**
	 * Used in the label for table column batch status whose value is validation.
	 */
	String LABEL_TABLE_VALIDATION = "label_table_validation";

	/**
	 * Value to be shown when a batch has priority urgent.
	 */
	String BATCH_PRIORITY_URGENT = "batch_priority_urgent";

	/**
	 * Value to be shown when a batch has priority high.
	 */
	String BATCH_PRIORITY_HIGH = "batch_priority_high";

	/**
	 * Value to be shown when a batch has priority medium.
	 */
	String BATCH_PRIORITY_MEDIUM = "batch_priority_medium";

	/**
	 * Value to be shown when a batch has priority low.
	 */
	String BATCH_PRIORITY_LOW = "batch_priority_low";

	/**
	 * Type of css to be applied on a button.
	 */
	String BUTTON_STYLE = "button-style";

	/**
	 * Text of the button used to start review process.
	 */
	String START_REVIEW_BUTTON = "Start Review";

	/**
	 * Text of the button used to start validate process.
	 */
	String START_VALIDATE_BUTTON = "Start Validate";

	/**
	 * Text of the list box that shows batch list priorities.
	 */
	String LABEL_PRIORITY_LISTBOX = "label_priority_listbox";
	
	String REVIEW_TAB_SHORTCUT = "review_tab_shortcut";
	
	String VALIDATE_TAB_SHORTCUT = "validate_tab_shortcut";

	/**
	 * Text of the refresh button displayed on batch list screen.
	 */
	String LABEL_REFRESH_BUTTON = "label_refresh_button";

	/**
	 * Label of the search button displayed.
	 */
	String BUTTON_SEARCH_BATCH = "search_batch_button";

	/**
	 * Text displayed in front of search button.
	 */
	String LABEL_SEARCH_BATCH = "search_batch_label";

	String COLON = ":";

}
