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

package com.ephesoft.dcma.core;

/**
 * Enum class for Ephesoft Property.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public enum EphesoftProperty {

	/**
	 * BATCH_CLASS.
	 */
	BATCH_CLASS("BC"),	
	/**
	 * BATCH_INSTANCE.
	 */
	BATCH_INSTANCE("BI"),
	/**
	 * DOCUMENT.
	 */
	DOCUMENT("DOC"),
	/**
	 * FIELD.
	 */
	FIELD("FD"),
	/**
	 * PAGE.
	 */
	PAGE("PG"),
	/**
	 * DOCUMENT_TYPE.
	 */
	DOCUMENT_TYPE("DT"),
	/**
	 * FIELD_TYPE.
	 */
	FIELD_TYPE("FT"),
	/**
	 * BATCH_CLASS_FIELD.
	 */
	BATCH_CLASS_FIELD("BCF"),
	/**
	 * PAGE_TYPE.
	 */
	PAGE_TYPE("PT"),
	/**
	 * EPHESOFT_APP_NAME.
	 */
	EPHESOFT_APP_NAME("EPHESOFT"),
	/**
	 * FUNCTION_KEY.
	 */
	FUNCTION_KEY("FK"),
	/**
	 * UNKNOWN.
	 */
	UNKNOWN("Unknown");
	
	/**
	 * property String.
	 */
	private String property;

	/**
	 * Constructor.
	 * @param property String
	 */
	EphesoftProperty(String property) {
		this.property = property;
	}

	/**
	 * To get property.
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}

}
