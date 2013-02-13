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

package com.ephesoft.dcma.fuzzydb.constants;

import com.ephesoft.dcma.core.component.ICommonConstants;

/**
 * An interface class used for constants used in fuzzy db plugin.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.fuzzydb.service.FuzzyDBSearchServiceImpl
 * 
 */
public interface FuzzyDBSearchConstants extends ICommonConstants {

	/**
	 * A constant for star.
	 */
	String STAR = "*";
	/**
	 * A constant to store semicolon.
	 */
	String SPLIT_CONSTANT = ";";
	/**
	 * A constant to store the plugin name.
	 */
	String FUZZYDB_PLUGIN = "FUZZYDB";
	/**
	 * Error message constant for showing error in retrieving column names.
	 */
	String COLUMN_NAME_ERROR_MSG = "Unable to fetch column names from DB";
	/**
	 * Constant for error in generating confidence score. 
	 */
	String CONFIDENCE_ERROR_MSG = "Problem generating confidence score for Document :  ";
	/**
	 * A constant for space delimiter.
	 */
	String SPACE_DELIMITER = " ";
	/**
	 * Query string delimiter.
	 */
	String QUERY_STRING_DELIMITER = ":";
	/**
	 * Constant for timestamp.
	 */
	String TIMESTAMP_NAME = "Timestamp";
	/**
	 * Constant for "Date".
	 */
	String DATE_NAME = "Date";
	/**
	 * Constant for "ALLPAGES".
	 */
	String ALLPAGES = "ALLPAGES";
	/**
	 * Constant String for "FIRSTPAGE".
	 */
	String FIRSTPAGE = "FIRSTPAGE";
	/**
	 * Index field row data.
	 */
	String INDEX_FIELD = "rowData";
	/**
	 * Constant for comma.
	 */
	String COMMA = ",";
	/**
	 * Constant for double quotes.
	 */
	String DOUBLE_QUOTES = "\"";
	/**
	 * Constant for single quotes.
	 */
	String SINGLE_QUOTES = "`";
	/**
	 * Mysql database.
	 */
	String MYSQL_DATABASE = "mysql";
	/**
	 * Constant for denoting empty string.
	 */
	String EMPTY_STRING = "";
	/**
	 * Constant used for checking word boundary.
	 */
	String WORD_BOUNDRY_REGEX = "\\b";
	/**
	 * String constant is used to check equals operator in strings. 
	 */
	String EQUALS = " = ";
	/**
	 * Constant for dollar delimiter.
	 */
	String DOLLAR_DELIMITER = "$";
	/**
	 * Constant for storing mysql driver.
	 */
	String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	/**
	 * Constant for storing mssql driver.
	 */
	String MSSQL_DRIVER = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
	/**
	 * Constant for storing jtds driver.
	 */
	String JTDS_DRIVER = "net.sourceforge.jtds.jdbc.Driver";
	/**
	 * Constant for database url separator.
	 */
	char DATABASE_URL_SEPERATOR = '/';
	/**
	 * Constant used for finding database.
	 */
	String DATABASE_NAME_CONSTANT = "databaseName";
	/**
	 * Constant for searching for database.
	 */
	String DATABASE_CONSTANT = "database";
	/**
	 * Constant for stating switch on for this plug-in.
	 */
	String SWITCH_ON = "ON";
	/**
	 * Constant for equals delimiter.
	 */
	String EQUALS_DELIM = "=";
	/**
	 * A constant to state error for generating query for document.
	 */
	String PROBLEM_GENERATING_QUERY_MESSAGE = "Problem generating query for Document :  ";
	/**
	 * A constant used for stating an empty generated query.
	 */
	String EMPTY_QUERY_MESSAGE = "Empty query generated for Document : ";
	/**
	 * Extracted data message.
	 */
	String EXTRACTED_DATA_MESSAGE = "Extracted data is : ";
	/**
	 * Constant to state return map message.
	 */
	String RETURN_MAP_MESSAGE = "Return Map is : ";
	/**
	 * Constant to show the processing of confidence score.
	 */
	String GENERATING_CONFIDENCE_MESSAGE = "Generating confidence score for Document: ";
	/**
	 * Constant to show non existence of fuzzy db index folder.
	 */
	String FUZZY_DB_INDEX_FOLDER_DOES_NOT_EXIST_MESSAGE = "The base fuzzy db index folder does not exist. So cannot extract database fields.";
	/**
	 * Constant to state wrong database name.
	 */
	String WRONG_DB_NAME_MESSAGE = "Wrong DB name found";
}
