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

package com.ephesoft.dcma.boxexport.constants;

import com.ephesoft.dcma.core.component.ICommonConstants;

/**
 * Constants interface for box export plug in.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.boxexport.service.BoxExportServiceImpl;
 * 
 */
public interface BoxExportConstant extends ICommonConstants {

	/**
	 * String constant for authentication token. 
	 */
	String AUTH_TOKEN = "&auth_token=";
	
	/**
	 * String constant for box api key.
	 */
	String BOX_AUTH_API_KEY = "BoxAuth api_key=";
	
	/**
	 * String constant for Box export plugin.
	 */
	String BOX_EXPORT_PLUGIN = "BOX_EXPORT_PLUGIN";
	
	/**
	 * String constant for Authorization.
	 */
	String AUTHORIZATION = "Authorization";
	
	/**
	 * String constant for uploading files to box repository. 
	 */
	String HTTPS_WWW_BOX_COM_API_1_0_UPLOAD_URL = "https://upload.box.net/api/1.0/upload/{auth_token}/{folder_id}";
	
	/**
	 * String constant for multipage pdf.
	 */
	String MULTIPAGE_PDF = "Multipage PDF";
	
	/**
	 * String constant for multipage tiff.
	 */
	String MULTIPAGE_TIF = "Multipage TIF";
	
	/**
	 * String constant for switch ON.
	 */
	String SWITCH_ON = "ON";
	
	/**
	 * String constant for authentication token in upload url.
	 */
	String AUTH_TOKEN_CONSTANT = "{auth_token}";
	
	/**
	 * String constant for folder id.
	 */
	String FOLDER_ID_CONSTANT = "{folder_id}";
	
	/**
	 * String constant for file description.
	 */
	String DESCRIPTION = "description";
	
	/**
	 * String constant for response status upload_ok.
	 */
	String UPLOAD_OK = "upload_ok";
	
	/**
	 * String constant for response//status.
	 */
	String RESPONSE_STATUS = "response//status";
	
	/**
	 * String constant for semi-colon.
	 */
	String SEMI_COLON_SEPARATOR = "; ";
	
	/**
	 * String constant for equals operator.
	 */
	String EQUALS_OPERATOR = "=";

}
