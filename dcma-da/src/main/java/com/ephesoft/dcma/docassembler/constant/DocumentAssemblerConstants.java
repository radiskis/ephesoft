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

package com.ephesoft.dcma.docassembler.constant;

/**
 * This is a common constants file for Document Assembler plugin.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public interface DocumentAssemblerConstants {
	/**
	 * String constant for space.
	 */
	String SPACE = " ";

	/**
	 * String constant for empty.
	 */
	String EMPTY = "";

	/**
	 * String constant for IMAGE_CLASSIFICATION.
	 */
	String IMAGE_CLASSIFICATION = "imageClassification";

	/**
	 * String constant for LUCENE_CLASSIFICATION.
	 */
	String LUCENE_CLASSIFICATION = "luceneClassification";

	/**
	 * String constant for CHECK_FIRST_PAGE.
	 */
	String CHECK_FIRST_PAGE = "checkFirstPage";

	/**
	 * String constant for CHECK_MIDDLE_PAGE.
	 */
	String CHECK_MIDDLE_PAGE = "checkMiddlePage";

	/**
	 * String constant for CHECK_LAST_PAGE.
	 */
	String CHECK_LAST_PAGE = "checkLastPage";

	/**
	 * String constant for FACTORY_CLASSIFICATION.
	 */
	String FACTORY_CLASSIFICATION = "factoryClassification";

	/**
	 * String constant for RULE_FML_PAGE.
	 */
	String RULE_FML_PAGE = "ruleFMLPage";

	/**
	 * String constant for RULE_FL_PAGE.
	 */
	String RULE_FL_PAGE = "ruleFLPage";

	/**
	 * String constant for RULE_FM_PAGE.
	 */
	String RULE_FM_PAGE = "ruleFMPage";

	/**
	 * String constant for RULE_F_PAGE.
	 */
	String RULE_F_PAGE = "ruleFPage";

	/**
	 * String constant for RULE_L_PAGE.
	 */
	String RULE_L_PAGE = "ruleLPage";

	/**
	 * String constant for RULE_M_PAGE.
	 */
	String RULE_M_PAGE = "ruleMPage";

	/**
	 * String constant for RULE_ML_PAGE.
	 */
	String RULE_ML_PAGE = "ruleMLPage";
	
	/**
	 * String constant for AUTOMATIC_CLASSIFICATION.
	 */
	String AUTOMATIC_CLASSIFICATION = "automaticClassification";
	
	/**
	 * String constant for AUTOMATIC_INCLUDE_LIST.
	 */
	String AUTOMATIC_INCLUDE_LIST = "automaticIncludeList";
	
	/**
	 * String constant for document merge switch "on" value .
	 */
	String DA_MERGE_SWITCH_ON = "ON";
	
	/**
	 * String constant for document merge switch "off" value.
	 */
	String DA_MERGE_SWITCH_OFF = "OFF";
	
	/**
	 * String constant for document assembler plugin name.
	 */
	String DOCUMENT_ASSEMBLER_PLUGIN = "DOCUMENT_ASSEMBLER";
}
