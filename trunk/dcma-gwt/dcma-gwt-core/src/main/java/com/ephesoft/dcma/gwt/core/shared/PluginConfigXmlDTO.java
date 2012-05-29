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

package com.ephesoft.dcma.gwt.core.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PluginConfigXmlDTO implements IsSerializable {

	private String pluginPropertyName;
	private String pluginPropertyType;
	private Boolean pluginPropertyIsMandetory;
	private Boolean pluginPropertyIsMultiValues;
	private String pluginPropertyDesc;
	private List<String> pluginPropertySampleValues;

	/**
	 * @return the pluginPropertyName
	 */
	public String getPluginPropertyName() {
		return pluginPropertyName;
	}

	/**
	 * @param pluginPropertyName the pluginPropertyName to set
	 */
	public void setPluginPropertyName(String pluginPropertyName) {
		this.pluginPropertyName = pluginPropertyName;
	}

	/**
	 * @return the pluginPropertyType
	 */
	public String getPluginPropertyType() {
		return pluginPropertyType;
	}

	/**
	 * @param pluginPropertyType the pluginPropertyType to set
	 */
	public void setPluginPropertyType(String pluginPropertyType) {
		this.pluginPropertyType = pluginPropertyType;
	}


	/**
	 * @return the pluginPropertyIsMandetory
	 */
	public Boolean getPluginPropertyIsMandetory() {
		return pluginPropertyIsMandetory;
	}

	/**
	 * @param pluginPropertyIsMandetory the pluginPropertyIsMandetory to set
	 */
	public void setPluginPropertyIsMandetory(Boolean pluginPropertyIsMandetory) {
		this.pluginPropertyIsMandetory = pluginPropertyIsMandetory;
	}

	/**
	 * @return the pluginPropertyIsMultiValues
	 */
	public Boolean getPluginPropertyIsMultiValues() {
		return pluginPropertyIsMultiValues;
	}

	/**
	 * @param pluginPropertyIsMultiValues the pluginPropertyIsMultiValues to set
	 */
	public void setPluginPropertyIsMultiValues(Boolean pluginPropertyIsMultiValues) {
		this.pluginPropertyIsMultiValues = pluginPropertyIsMultiValues;
	}

	
	/**
	 * @return the pluginPropertyDesc
	 */
	public String getPluginPropertyDesc() {
		return pluginPropertyDesc;
	}

	
	/**
	 * @param pluginPropertyDesc the pluginPropertyDesc to set
	 */
	public void setPluginPropertyDesc(String pluginPropertyDesc) {
		this.pluginPropertyDesc = pluginPropertyDesc;
	}

	
	/**
	 * @return the pluginPropertySampleValues
	 */
	public List<String> getPluginPropertySampleValues() {
		return pluginPropertySampleValues;
	}

	
	/**
	 * @param pluginPropertySampleValues the pluginPropertySampleValues to set
	 */
	public void setPluginPropertySampleValues(List<String> pluginPropertySampleValues) {
		this.pluginPropertySampleValues = pluginPropertySampleValues;
	}
}
