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

package com.ephesoft.dcma.monitor.service.foldermonitorconstants;

/**
 * This class contains constants.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.monitor.service.FolderMonitorService
 */
public interface FolderMoniterConstants {

	/**
	 * String to show state "ON".
	 */
	String STATE_ON = "ON";

	/**
	 * String to show state "OFF".
	 */
	String STATE_OFF = "OFF";

	/**
	 * String to store dot operator.
	 */
	char DOT = '.';

	/**
	 * String to store hyphen character.
	 */
	char HYPHEN_CHAR = '-';
	
	/**
	 * A int type constant for hashcode().
	 */
	int HASH_CODE_CONSTANT_31 = 31;
	
	/**
	 * A constant to save temporary folder name.
	 */
	String TEMPORARY_FOLDER_NAME = "temp";

	/**
	 * A constant string for batch copy timeout.
	 */
	String BATCH_COPY_TIMEOUT = "batch_copy_timeout";
	
	/**
	 * Constant to show folder name format.
	 */
	String FOLDER_NAME = "Folder name :: ";
	
	/**
	 * A constant of type int to store the sleep time.
	 */
	int SLEEP_TIME_5000 = 5000;
	
	/**
	 * A constant of type long to store the sleep time.
	 */
	long SLEEP_TIME_3600000L = 3600000L;

	/**
	 * A constant of type long to store the sleep time.
	 */
	long SLEEP_TIME_1000L = 1000L;

	/**
	 * A constant of type int to store the sleep time.
	 */
	int SLEEP_TIME_1000 = 1000;
	
	/**
	 * A constant of type int to store the sleep time.
	 */
	int SLEEP_TIME_30000 = 30000;


}
