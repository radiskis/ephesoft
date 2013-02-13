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

package com.ephesoft.dcma.docassembler;

import com.ephesoft.dcma.core.common.PluginProperty;

/**
 * Document Assembler Property.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.common.PluginProperty
 */
public enum DocumentAssemblerProperties implements PluginProperty {
	
	/**
	 * DA_BARCODE_CONFIDENCE.
	 */
	DA_BARCODE_CONFIDENCE("da.barcode_confidence"), 
	/**
	 * DA_RULE_FP_MP_LP.
	 */
	DA_RULE_FP_MP_LP("da.rule_fp_mp_lp"), 
	/**
	 * DA_RULE_FP.
	 */
	DA_RULE_FP("da.rule_fp"), 
	/**
	 * DA_RULE_MP.
	 */
	DA_RULE_MP("da.rule_mp"), 
	/**
	 * DA_RULE_LP.
	 */
	DA_RULE_LP("da.rule_lp"), 
	/**
	 * DA_RULE_FP_LP.
	 */
	DA_RULE_FP_LP("da.rule_fp_lp"), 
	/**
	 * DA_RULE_FP_MP.
	 */
	DA_RULE_FP_MP("da.rule_fp_mp"), 
	/**
	 * DA_RULE_MP_LP.
	 */
	DA_RULE_MP_LP("da.rule_mp_lp"), 
	/**
	 * DA_FACTORY_CLASS.
	 */
	DA_FACTORY_CLASS("da.factory_classification"),
	/**
	 * DA_MERGE_UNKNOWN_DOCUMENT_SWITCH.
	 */
	DA_MERGE_UNKNOWN_DOCUMENT_SWITCH("da.merge_unknown_document_switch");
	
	/**
	 * Key String.
	 */
	String key;
	
	/**
	 * Constructor.
	 * @param key String
	 */
	DocumentAssemblerProperties(String key) {
		this.key = key;
	}
	
	/**
	 * To get property key.
	 * @return the key
	 */
    @Override
	public String getPropertyKey() {
		return key;
	}
}
