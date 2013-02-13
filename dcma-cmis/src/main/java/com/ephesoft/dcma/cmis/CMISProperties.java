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

package com.ephesoft.dcma.cmis;

import com.ephesoft.dcma.core.common.PluginProperty;

/**
 * This enum is responsible for loading property fields with data base.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.cmis.CMISExporter
 */
public enum CMISProperties implements PluginProperty {
	
	/**
	 * cmis root folder.
	 */
	CMIS_ROOT_FOLDER("cmis.root_folder"), 
	/**
	 * cmis upload file type extension.
	 */
	CMIS_UPLOAD_FILE_EXT("cmis.upload_file_type_ext"), 
	/**
	 * cmis server url.
	 */
	CMIS_SERVER_URL("cmis.server_URL"), 
	/**
	 * cmis server user name.
	 */
	CMIS_SERVER_USER_NAME("cmis.server_username"), 
	/**
	 * cmis server password.
	 */
	CMIS_SERVER_PASSWORD("cmis.server_password"), 
	/**
	 * cmis repository id.
	 */
	CMIS_REPOSITORY_ID("cmis.repository_id"), 
	/**
	 * cmis switch.
	 */
	CMIS_SWITCH("cmis.switch"),
	/**
	 * cmis aspects switch.
	 */
	CMIS_ASPECTS_SWITCH("cmis.aspects_switch"),
	/**
	 * cmis file name.
	 */
	CMIS_FILE_NAME("cmis.file_name"),
	/**
	 * cmis client key.
	 */
	CMIS_CLIENT_KEY("cmis.client_key"),
	/**
	 * cmis secret key.
	 */
	CMIS_SECRET_KEY("cmis.secret_key"),
	/**
	 * cmis refresh token.
	 */
	CMIS_REFRESH_TOKEN("cmis.refresh_token"),
	/**
	 * cmis network.
	 */
	CMIS_NETWROK("cmis.network"),
	/**
	 * cmis redirect url.
	 */
	CMIS_REDIRECT_URL("cmis.redirect_url");
	
	/**
	 * A string to store key.
	 */
	String key;
	
	CMISProperties(String key) {
		this.key = key;
	}

	@Override
	public String getPropertyKey() {
		return key;
	}
}
