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

package com.ephesoft.dcma.core.component;

/**
 * This interface contains all the common constants.
 * 
 * @author Ephesoft
 * 
 */
public interface ICommonConstants {
	public final String ENTER_METHOD = ">>>Entering Method";
	public final String EXIT_METHOD = "<<<Exiting Method";
	public final String CLASS_NAME = " >Class Name=";
	public final String EXTENSION_TIF = ".tif";
	public final String EXTENSION_PDF = ".pdf";
	public final String EXTENSION_PNG = ".png";
	public final String UNDERSCORE_BATCH_XML_ZIP = "_batch.xml.zip";
	public final String UNDERSCORE_BATCH_BAK_XML_ZIP = "_batch_bak.xml.zip";
	public final String UNDERSCORE_BAK_BATCH_XML_ZIP = "_bak_batch.xml.zip";
	
	public final String UNDERSCORE_BATCH_XML = "_batch.xml";
	public final String UNDERSCORE_BATCH_BAK_XML = "_batch_bak.xml";
	public final String UNDERSCORE_BAK_BATCH_XML = "_bak_batch.xml";
	public static final String ZIP_SWITCH = "zip_switch";
	
	public final String BATCH_XSD_SCHEMA_PACKAGE = "com.ephesoft.dcma.batch.schema";
	public final String PROPERTIES_FILE_BARCODE_READER = "reader.properties";
	public final String PROPERTY_BARCODE_READER = "image.base.location";
	public final String FIRST_PAGE = "_First_Page";
	public final String MIDDLE_PAGE = "_Middle_Page";
	public final String LAST_PAGE = "_Last_Page";
	
	public final String SEARCH_CLASSIFICATION_PLUGIN = "SEARCH_CLASSIFICATION";
	public final String BARCODE_READER_PLUGIN = "BARCODE_READER";
}
