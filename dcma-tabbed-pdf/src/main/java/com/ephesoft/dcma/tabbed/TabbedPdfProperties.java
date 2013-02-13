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

package com.ephesoft.dcma.tabbed;

import com.ephesoft.dcma.core.common.PluginProperty;

/**
 * This class states the properties of Tabbed PDF plugin.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.common.PluginProperty
 */
public enum TabbedPdfProperties implements PluginProperty {

	/**
	 * TABBED_PDF_EXPORT_FOLDER.
	 */
	TABBED_PDF_EXPORT_FOLDER("tabbedPdf.final_export_folder"),
	/**
	 * TABBED_PDF_SWITCH.
	 */
	TABBED_PDF_SWITCH("tabbedPdf.switch"),
	/**
	 * TABBED_PDF_PLACEHOLDER.
	 */
	TABBED_PDF_PLACEHOLDER("tabbedPdf.placeholder"),
	/**
	 * TABBED_PDF_PROPERTY_FILE.
	 */
	TABBED_PDF_PROPERTY_FILE("tabbedPdf.property_file"),
	/**
	 * TABBED_PDF_CREATION_PARAMETERS.
	 */
	TABBED_PDF_CREATION_PARAMETERS("tabbedPdf.creation_parameters"),
	/**
	 * TABBED_PDF_OPTIMIZATION_PARAMETERS.	
	 */
	TABBED_PDF_OPTIMIZATION_PARAMETERS("tabbedPdf.optimization_parameters"), 
	/**
	 * TABBED_PDF_OPTIMIZATION_SWITCH.
	 */
	TABBED_PDF_OPTIMIZATION_SWITCH("tabbedPdf.pdf_optimization_switch");

	/**
	 * key String.
	 */
	String key;

	/**
	 * Constructor.
	 * @param key {@link String}
	 */
	TabbedPdfProperties(String key) {
		this.key = key;
	}

	/**
	 * To get property key.
	 * @return String
	 */
	@Override
	public String getPropertyKey() {
		return key;
	}
}
