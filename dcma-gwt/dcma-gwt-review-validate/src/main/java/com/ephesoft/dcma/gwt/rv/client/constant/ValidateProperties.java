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

package com.ephesoft.dcma.gwt.rv.client.constant;

import com.ephesoft.dcma.core.common.PluginProperty;


public enum ValidateProperties implements PluginProperty {
	/**
	 * Switch for validation document scripting script.
	 */
	VAILDATE_DOCUMENT_SCRIPTING_SWITCH("validation.validationScriptSwitch"),
	EXTERNAL_APP_URL1("validation.url(Ctrl+4)"),
	EXTERNAL_APP_URL2("validation.url(Ctrl+7)"),
	EXTERNAL_APP_URL3("validation.url(Ctrl+8)"),
	EXTERNAL_APP_URL4("validation.url(Ctrl+9)"), 
	EXTERNAL_APP_X_DIMENSION("validation.x_dimension"),
	EXTERNAL_APP_Y_DIMENSION("validation.y_dimension"), 
	EXTERNAL_APP_SWITCH("validation.external_app_switch");
	
	/**
	 * Property key.
	 */
	private String key;
	
	ValidateProperties(final String key) {
		this.key = key;
	}

	@Override
	public String getPropertyKey() {
		return key;
	}

}
