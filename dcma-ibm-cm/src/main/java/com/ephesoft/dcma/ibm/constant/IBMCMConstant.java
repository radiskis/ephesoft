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

package com.ephesoft.dcma.ibm.constant;

import java.util.Arrays;
import java.util.List;

/**
 * This is a common constants file for IBM Content Management plug-in.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public enum IBMCMConstant {
	/**
	 * Total document size.
	 */
	TOTAL_DOCUMENT_SIZE("totalDocSize"),

	/**
	 * Plug-in name for IBM content management plug-in.
	 */
	IBM_CM_PLUGIN("IBM_CM_PLUGIN"),

	/**
	 * Extension for batch.xml.
	 */
	BATCH_XML("_batch.xml"),

	/**
	 * Extension for xml file.
	 */
	XML_EXTENSION(".xml"),

	/**
	 * Constant for batch creation date.
	 */
	BATCH_CREATION_DATE("batchCreationDate"),

	/**
	 * Constant for batch creation time.
	 */
	BATCH_CREATION_TIME("batchCreationTime"),

	/**
	 * Constant for underscore.
	 */
	UNDERSCORE("_"),

	/**
	 * Date format.
	 */
	DATE_FORMAT("MM/dd/yyyy"),

	/**
	 * Time format.
	 */
	TIME_FORMAT("HH:mm:ss"),

	/**
	 * DAT file extension.
	 */
	DAT_EXTENSION(".dat"),

	/**
	 * CTL file extension.
	 */
	CTL_EXTENSION(".ctl"),

	/**
	 * DAT file extension.
	 */
	DAT_FILE_NAME("datFileName"),

	/**
	 * Cmod app group for IBM xml file.
	 */
	CMOD_APP_GROUP("cmodAppGroup"),

	/**
	 * Cmod App for IBM xml file.
	 */
	CMOD_APP("cmodApp"),

	/**
	 * user name for IBM xml file.
	 */
	USER_NAME("userName"),

	/**
	 * email for IBM xml file.
	 */
	EMAIL("email"),

	/**
	 * Batch creation station id for IBM xml file.
	 */
	BATCH_CREATION_STATION_ID("batchCreationStationID"),

	/**
	 * Station id for IBM xml file.
	 */
	STATION_ID("stationID");

	/**
	 * Identifier for content.
	 */
	private String identifier;

	private IBMCMConstant(final String identifier) {
		this.identifier = identifier;
	}

	public static List<IBMCMConstant> valuesAsList() {
		return Arrays.asList(values());
	}

	public String getId() {
		return identifier;
	}
}
