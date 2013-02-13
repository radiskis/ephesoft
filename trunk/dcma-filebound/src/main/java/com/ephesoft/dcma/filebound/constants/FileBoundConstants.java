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

package com.ephesoft.dcma.filebound.constants;

import java.io.File;

/**
 * The constants class for FileBound plugin.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public class FileBoundConstants {
	/**
	 * A constant for storing "ON".
	 */
	public static final String ON_STRING = "ON";

	/**
	 * A constant for storing escaped space.
	 */
	public static final String ESCAPED_SPACE = "\" \"";

	/**
	 * A constant for storing filebound export plug in.
	 */
	public static final String FILEBOUND_EXPORT_PLUGIN = "FILEBOUND_EXPORT";

	/**
	 * The path where the recostar executable is placed.
	 */
	public static final String FILEBOUND_BASE_PATH = "FILEBOUND_PATH";

	
	/**
	 * A constant for storing "document_name".
	 */
	public static final String DOCUMENT_NAME = "document_name";

	/**
	 * A constant for storing property file name.
	 */
	public static final String PROPERTY_FILE_NAME = "filebound-mapping.properties";
	
	/**
	 * A constant for storing parameters file name.
	 */
	public static final String PARAMETERS_FILE_NAME = "filebound-parameters.properties";
	
	/**
	 * A constant for storing mapping separator.
	 */
	public static final String MAPPING_SEPERATOR = "===";

	/**
	 * A constant for storing folder path.
	 */
	public static final String META_INF = "META-INF" + File.separator + "dcma-filebound";
	
	/**
	 * A constant for storing field lookup property.
	 */
	public static final String FIELD_LOOKUP_PROPERTY_FILE = "filebound-field-lookup.properties";

	/**
	 * A constant for storing mapping folder name.
	 */
	public static final String MAPPING_FOLDER_NAME = "filebound-plugin-mapping";

}
