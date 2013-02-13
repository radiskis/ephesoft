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

import java.util.Properties;


/**
 * This class contains all the document details for CMIS.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.cmis.service.CMISExportServiceImpl
 */
public class CMISDocumentDetails {
	
	/**
	 * Private string to store date format.
	 */
	private String dateFormat;

	/**
	 * An instance of {@link Properties}.
	 */
	private Properties aspectProperties;
	/**
	 * plugin mapping file name.
	 */
	private String pluginMappingFileName;
	/**
	 * aspect mapping file name.
	 */
	private String aspectMappingFileName;

	/**
	 * document versioning state name.
	 */
	private String documentVersioningState;

	
	/**
	 * getter for dateFormat.
	 * @return {@link String}
	 */
	public String getDateFormat() {
		return dateFormat;
	}

	
	/**
	 * setter for dateFormat.
	 * @param dateFormat {@link String}
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	
	/**
	 * getter for aspectProperties.
	 * @return {@link Properties}
	 */
	public Properties getAspectProperties() {
		return aspectProperties;
	}

	
	/**
	 * setter for aspectProperties.
	 * @param aspectProperties {@link Properties}
	 */
	public void setAspectProperties(Properties aspectProperties) {
		this.aspectProperties = aspectProperties;
	}

	
	/**
	 * getter for pluginMappingFileName.
	 * @return {@link String}
	 */
	public String getPluginMappingFileName() {
		return pluginMappingFileName;
	}

	
	/**
	 * setter for pluginMappingFileName.
	 * @param pluginMappingFileName {@link String}
	 */
	public void setPluginMappingFileName(String pluginMappingFileName) {
		this.pluginMappingFileName = pluginMappingFileName;
	}

	
	/**
	 * getter for aspectMappingFileName.
	 * @return {@link String}
	 */
	public String getAspectMappingFileName() {
		return aspectMappingFileName;
	}

	
	/**
	 * setter for aspectMappingFileName.
	 * @param aspectMappingFileName {@link String}
	 */
	public void setAspectMappingFileName(String aspectMappingFileName) {
		this.aspectMappingFileName = aspectMappingFileName;
	}

	
	/**
	 * getter for documentVersioningState.
	 * @return {@link String}
	 */
	public String getDocumentVersioningState() {
		return documentVersioningState;
	}

	
	/**
	 * setter for documentVersioningState.
	 * @param documentVersioningState {@link String}
	 */
	public void setDocumentVersioningState(String documentVersioningState) {
		this.documentVersioningState = documentVersioningState;
	}
	
	
}
