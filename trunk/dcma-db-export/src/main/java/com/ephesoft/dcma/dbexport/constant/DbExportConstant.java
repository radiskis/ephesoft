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

package com.ephesoft.dcma.dbexport.constant;

import com.ephesoft.dcma.core.component.ICommonConstants;

/**
 * This is a common constants file for the Database Export plugin.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public interface DbExportConstant extends ICommonConstants {

	/**
	 * String constant for period.
	 */
	String PERIOD = ".";

	/**
	 * String constant for colon.
	 */
	String COLON = ":";

	/**
	 * String constant for hyphen.
	 */
	String HYPEN = "-";

	/**
	 * String constant for equality sign.
	 */
	String EQUALS_SIGN = "=";

	/**
	 * String constant for Documents keyword.
	 */
	String DOCUMENTS = "Documents";

	/**
	 * String constant for Document keyword.
	 */
	String DOCUMENT = "Document";

	/**
	 * String constant for DocumentLevelFields keyword.
	 */
	String DLFS = "DocumentLevelFields";

	/**
	 * String constant for DocumentLevelField keyword.
	 */
	String DLF = "DocumentLevelField";

	/**
	 * String constant for Name keyword.
	 */
	String NAME = "Name";

	/**
	 * String constant for Value keyword.
	 */
	String VALUE = "Value";

	/**
	 * String constant for DB EXPORT Plug in name.
	 */
	String DB_EXPORT_PLUGIN_NAME = "DB_EXPORT";

	/**
	 * String constant for ON keyword.
	 */
	String STATE = "ON";

	/**
	 * String constant for open bracket.
	 */
	String OPEN_BRACKET = "(";

	/**
	 * String constant for single quote.
	 */
	String SINGLE_QOUTE = "'";

	/**
	 * String constant for closed bracket.
	 */
	String CLOSING_BRACKET = ")";

	/**
	 * String constant values.
	 */
	String VALUES_CONSTANT = " VALUES ('";

	/**
	 * String constant quoted comma.
	 */
	String QOUTED_COMMA = "','";

	/**
	 * String constant comma.
	 */
	String COMMA = ",";

	/**
	 * String constant insert into.
	 */
	String INSERT_INTO = "INSERT INTO ";

	/**
	 * String constant batch class id column name.
	 */
	String BATCH_CLASS_ID = "batch_class_id";

	/**
	 * String constant batch instance id column name.
	 */
	String BATCH_INSTANCE_ID = "batch_instance_id";

	/**
	 * String constant document type column name.
	 */
	String DOCUMENT_TYPE = "document_type";

	/**
	 * String constant document level field column name.
	 */
	String DOCUMENT_LEVEL_FIELD_NAME = "document_level_field_name";

	/**
	 * Integer constant temporary position zero.
	 */
	int TEMP_ZERO_POSITION = 0;

	/**
	 * Integer constant temporary position one.
	 */
	int TEMP_ONE_POSITION = 1;

	/**
	 * Integer constant temporary position two.
	 */
	int TEMP_CONTENT_SIZE_2 = 2;

	/**
	 * Integer constant mapping file content size four.
	 */
	int MAPPING_CONTENT_SIZE_4 = 4;

	/**
	 * Integer constant for document type value position in mapping file.
	 */
	int DOC_TYPE_POSITION = 0;

	/**
	 * Integer constant for document field value position in mapping file.
	 */
	int DOC_FIELD_POSITION = 1;

	/**
	 * Integer constant for table name value position in mapping file.
	 */
	int TABLE_NAME_POSITION = 2;

	/**
	 * Integer constant for column name value position in mapping file.
	 */
	int COLUMN_NAME_POSITION = 3;

	/**
	 * String constant for indicating a comment line initiator in properties file.
	 */
	String PROPERTIES_COMMENT_PREFIX = "#";

}
