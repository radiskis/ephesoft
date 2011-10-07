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

package com.ephesoft.dcma.lucene;

import com.ephesoft.dcma.core.common.PluginProperty;

/**
 * Enumeration representing properties to be fetched from Database.
 * 
 * @author Ephesoft
 * @version 1.0
 * 
 */
public enum LuceneProperties implements PluginProperty {

	/**
	 * valid extensions for Luecene.
	 */
	LUCENE_VALID_EXTNS("lucene.valid_extensions"),
	/**
	 * Minimum term frequency for MoreLiikeThis.
	 */
	LUCENE_MIN_TERM_FREQ("lucene.min_term_freq"),
	/**
	 * Minimum doc frequency for MoreLiikeThis.
	 */
	LUCENE_MIN_DOC_FREQ("lucene.min_doc_freq"),
	/**
	 * Minimum word length for MoreLiikeThis.
	 */
	LUCENE_MIN_WORD_LENGTH("lucene.min_word_length"),
	/**
	 * Maximum query terms for MoreLiikeThis.
	 */
	LUCENE_MAX_QUERY_TERMS("lucene.max_query_terms"),
	/**
	 * Top level field for MoreLiikeThis.
	 */
	LUCENE_TOP_LEVEL_FIELD("lucene.top_level_field"),
	/**
	 * No of pages for MoreLiikeThis.
	 */
	LUCENE_NO_OF_PAGES("lucene.no_of_pages"),
	/**
	 * Index level fields for MoreLiikeThis.
	 */
	LUCENE_INDEX_FIELDS("lucene.index_fields"),
	/**
	 * Project File.
	 */
	RECOSTAR_PROJECT_FILE("recostar.project_file"),
	/**
	 * Stop words for MoreLiikeThis.
	 */
	LUCENE_STOP_WORDS("lucene.stop_words"),

	/**
	 * On/Off switch for lucene image classification.
	 */
	LUCENE_SWITCH("lucene.switch"),

	/**
	 * Maximum number of results
	 */
	LUCENE_MAX_RESULT_COUNT("lucene.max_result_count"),
	/**
	 * first page confidence score weightage factor
	 */
	LUCENE_FIRST_PAGE_CONF_WEIGHTAGE("lucene.first_page_conf_weightage"),
	/**
	 * middle page confidence score weightage factor
	 */
	LUCENE_MIDDLE_PAGE_CONF_WEIGHTAGE("lucene.middle_page_conf_weightage"),
	/**
	 * last page confidence score weightage factor
	 */
	LUCENE_LAST_PAGE_CONF_WEIGHTAGE("lucene.last_page_conf_weightage");

	/**
	 * Property key.
	 */
	private String key;

	LuceneProperties(final String key) {
		this.key = key;
	}

	@Override
	public String getPropertyKey() {
		return key;
	}
}
