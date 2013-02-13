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

package com.ephesoft.dcma.kvfieldcreation.constant;

/**
 * This is a common constants file for learning key value plugin.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public interface KVFieldCreatorConstants {

	/**
	 * String constant for checking value of switch as "ON".
	 */
	String SWITCH_ON = "ON";

	/**
	 * String constant for not space.
	 */
	String NOT_SPACE = "\\S";

	/**
	 * String constant for dot.
	 */
	String FULL_STOP = ".";

	/**
	 * String constant for space.
	 */
	String SPACE = " ";

	/**
	 * String constant for separator between key and value.
	 */
	String KEY_VALUE_SEPARATORS = ":=";

	/**
	 * String constant for semi colon.
	 */
	String SEMI_COLON = ";";

	/**
	 * Integer constant for default maximum records per document level field.
	 */
	Integer DEFAULT_MAX_RECORD_PER_DLF = 50;

	/**
	 * Path of value regex property file .
	 */
	String VALUE_REGEX_FILE_PATH = "/META-INF/dcma-key-value-learning/dcma-value-regex.properties";

	/**
	 * Integer constant for default threshold value.
	 */
	Integer DEFAULT_TOLEANCE_THRESHOLD = 10;

	/**
	 * Float constant for default multiplier value.
	 */
	float DEFAULT_MULTIPLIER = 1;

	/**
	 * Integer constant for Default width of line.
	 */
	int DEFAULT_WIDTH_OF_LINE = 20;

	/**
	 * Path of key regex property file .
	 */
	String KEY_REGEX_FILE_PATH = "/META-INF/dcma-key-value-learning/dcma-key-regex.properties";
	
	/**
	 * Minimum number of characters required in a key. 
	 */
	Integer MIN_KEY_CHAR_COUNT = 3;
	
	/**
	 * Default gap between words that is to be used while key pattern generation. 
	 * Value to be specified in pixels.
	 */
	Integer DEFAULT_GAP_BETWEEN_KEYS = 50;
	
	/**
	 * Path of exclusion regex property file .
	 */
	String EXCLUSION_REGEX_FILE_PATH  = "/META-INF/dcma-key-value-learning/key_exclusion_list.properties";
	/**
	 * Constant value of "100" used in calculations.
	 */
	int CONSTANT_VALUE_100 = 100;
	/**
	 * Message shown during process of key creation.
	 */
	String MSG_KEY_CREATION = "Creating key at location : ";
	/**
	 * KV field learning plug in name.
	 */
	String KV_FIELD_LEARNING_PLUGIN = "KEY_VALUE_LEARNING_PLUGIN";
}
