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

package com.ephesoft.dcma.csv.constant;

import java.util.Arrays;
import java.util.List;

/**
 * This enum is used as constant for CSV File Creation plug in.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public enum CSVFileCreationConstant {
	/**
	 * Value for comma.
	 */
	COMMA(","),

	/**
	 * Value for 1.
	 */
	ONE_VALUE("1"),

	/**
	 * Value for N.
	 */
	NO_STRING("N"),

	/**
	 * Value for Y.
	 */
	YES("Y"),

	/**
	 * Value for empty string.
	 */
	EMPTY_STRING(""),

	/**
	 * Time format.
	 */
	HH_MM_SS("HH:mm:ss"),

	/**
	 * Date format.
	 */
	MM_DD_YYYY("MM/dd/yyyy"),
	
	/**
	 * Date Time format.
	 */
	MM_DD_YYYY_HH_MM_SS("MM/dd/yyyy HH:mm:ss"),

	/**
	 * CSV header for BatchFields.
	 */
	BATCH_FIELDS("BatchFields"),

	/**
	 * CSV header for Placeholder.
	 */
	PLACEHOLDER("Placeholder"),

	/**
	 * CSV header for BookmarkCreated.
	 */
	CREATE_TAB("Create Tab"),

	/**
	 * CSV header for PgCount.
	 */
	NUMBER_OF_IMAGES("Num_Images"),

	/**
	 * CSV header for Suffix.
	 */
	SUFFIX("Suffix"),

	/**
	 * CSV header for BookmarkValue.
	 */
	TAB_NAME("TABNAME"),

	/**
	 * CSV header for Prefix.
	 */
	PREFIX("Prefix"),

	/**
	 * CSV header for BookmarkLevel.
	 */
	BOOKMARK_LEVEL("BookmarkLevel"),

	/**
	 * CSV header for TriggerType.
	 */
	TRIGGER_TYPE("TriggerType"),

	/**
	 * CSV header for Trigger.
	 */
	TRIGGER("Trigger"),

	/**
	 * CSV header for DocID.
	 */
	DOC_ID("DocID"),

	/**
	 * CSV header for CurrentTime.
	 */
	CURRENT_TIME("CurrentTime"),

	/**
	 * CSV header for CurrentDate.
	 */
	PROCESS_DATE("Process_Date"),

	/**
	 * CSV header for BatchCreationDateTime.
	 */
	BATCH_CREATION_DATE_TIME("BatchCreationDateTime"),

	/**
	 * CSV header for BatchID.
	 */
	BATCH_ID("BatchID"),

	/**
	 * Value for CSV sheet name.
	 */
	EPHESOFT("ephesoft"),

	/**
	 * CSV File creation plug in name.
	 */
	CSV_FILE_CREATION_PLUGIN("CSV_FILE_CREATION_PLUGIN"),

	/**
	 * Underscore symbol.
	 */
	UNDERSCORE("_"),

	/**
	 * CSV extension.
	 */
	CSV_EXTENSION(".csv"),

	/**
	 * Value for fillPattern.
	 */
	REC_ID("RecID"),
	
	/**
	 * Value for BatchName.
	 */
	BATCH_NAME("BatchName"),
	
	/**
	 * Value for BatchClassName.
	 */
	BATCH_CLASS_NAME("BatchClassName"),
	
	/**
	 * Value for file process name.
	 */
	FILE_PROCESS_NAME("File_Process_Name"),
	
	/**
	 * Value for Subpoena.
	 */
	SUBPOENA("Subpoena"),
	
	/**
	 * value for LoanPerf.
	 */
	LOAN_NUMBER("LoanPerf");

	/**
	 * Used for returning as a string data.
	 */
	private String identifier;
	
	/**
	 * Variable for comma character.
	 */
	public static final char COMMA_STRING = ',';

	/**
	 * Variable for quotes character.
	 */
	public static final String QUOTES = "\"";
	
	/**
	 * Constant for ON.
	 */
	public static final String ON_STRING = "ON";

	/**
	 * Constant for pdf extension.
	 */
	public static final String PDF_EXT = ".pdf";
	
	/**
	 * The end index value for loop.
	 */
	public static final int END_INDEX_VALUE = 3;


	private CSVFileCreationConstant(final String statusId) {
		this.identifier = statusId;
	}

	/**
	 * @return List< {@link CSVFileCreationConstant}>
	 */
	public static List<CSVFileCreationConstant> valuesAsList() {
		return Arrays.asList(values());
	}

	/**
	 * @return {@link String}
	 */
	public String getId() {
		return this.identifier;
	}
}
