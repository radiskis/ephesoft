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

package com.ephesoft.dcma.fuzzydb;

import com.ephesoft.dcma.core.common.PluginProperty;

/**
 * This class contains all the properties of Fuzzy DB plugin.
 * 
 * @author Ephesoft
 * @version 1.0
 *
 */
public enum FuzzyDBProperties implements PluginProperty {
	
	/**
	 * fuzzy db date format property.
	 */
	FUZZYDB_DATE_FORMAT("fuzzydb.dateFormat"),
	/**
	 * fuzzy db threshold value property.
	 */
	FUZZYDB_THRESHOLD_VALUE("fuzzydb.thresholdValue"),
	/**
	 * fuzzy db connection url property.
	 */
	FUZZYDB_CONNECTION_URL("fuzzydb.database.connectionURL"),
	/**
	 * fuzzy db database driver property.
	 */
	FUZZYDB_DB_DRIVER("fuzzydb.database.driver"),
	/**
	 * fuzzy db database username property.
	 */
	FUZZYDB_DB_USER_NAME("fuzzydb.database.userName"),
	/**
	 * fuzzy db database password property.
	 */
	FUZZYDB_DB_PASSWORD("fuzzydb.database.password"),
	/**
	 * fuzzy db minimum term frequency property.
	 */
	FUZZYDB_MIN_TERM_FREQ("fuzzydb.min_term_freq"),
	/**
	 * fuzzy db minimum doc frequency property.
	 */
	FUZZYDB_MIN_DOC_FREQ("fuzzydb.min_doc_freq"),
	/**
	 * fuzzy db minimum word length property.
	 */
	FUZZYDB_MIN_WORD_LENGTH("fuzzydb.min_word_length"),
	/**
	 * fuzzy db maximum query terms property.
	 */
	FUZZYDB_MAX_QUERY_TERMS("fuzzydb.max_query_terms"),
	/**
	 * fuzzy db no of pages property.
	 */
	FUZZYDB_NO_OF_PAGES("fuzzydb.no_of_pages"),
	/**
	 * fuzzy db include pages property.
	 */
	FUZZYDB_INCLUDE_PAGES("fuzzydb.include_pages"),
	/**
	 * fuzzy db document type property.
	 */
	FUZZYDB_DOCUMENT_TYPE("document.type"),
	/**
	 * fuzzy db field type property.
	 */
	FUZZYDB_FIELD_TYPE("field.type"),
	/**
	 * fuzzy db stop words property.
	 */
	FUZZYDB_STOP_WORDS("fuzzydb.stop_words"),
	/**
	 * fuzzy db query delimiters property.
	 */
	FUZZYDB_QUERY_DELIMITERS("fuzzydb.query_delimiters"),
	/**
	 * fuzzy db switch.
	 */
	FUZZYDB_SWITCH("fuzzydb.switch"),
	/**
	 * fuzzy db search switch.
	 */
	FUZZYDB_HOCR_SWITCH("fuzzydb.search.switch"),
	/**
	 * fuzzy db search column name.
	 */
	FUZZYDB_SEARCH_COLUMNS("fuzzydb.search.columnName");
	/**
	 * String type key.
	 */
	String key;
	
	FuzzyDBProperties(String key) {
		this.key = key;
	}

	@Override
	public String getPropertyKey() {
		return key;
	}
}
