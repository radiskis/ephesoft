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

package com.ephesoft.dcma.kvfinder.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.schema.Coordinates;

/**
 * This is class to get key value properties for page.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.batch.schema.Coordinates
 */
public class KeyValueFieldCarrier {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(KeyValueFieldCarrier.class);

	/**
	 * pageIdToKVDataMap Map<String, List<KeyValueProperties>>.
	 */
	private Map<String, List<KeyValueProperties>> pageIdToKVDataMap;

	/**
	 * Constructor. 
	 */
	public KeyValueFieldCarrier() {
		this.pageIdToKVDataMap = new HashMap<String, List<KeyValueProperties>>();
	}

	/**
	 * To get Key Value Properties For Page.
	 * @param pageID String
	 * @return List<KeyValueProperties>
	 */
	public List<KeyValueProperties> getKeyValuePropertiesForPage(final String pageID) {
		LOGGER.info("Entering method getKeyValuePropertiesForPage........");
		List<KeyValueProperties> keyValuePropertieList = null;
		if (pageID != null && !pageID.isEmpty()) {
			LOGGER.info("Getting Key value propertes for page id: " + pageID);
			keyValuePropertieList = pageIdToKVDataMap.get(pageID);
		}
		LOGGER.info("Exiting method getKeyValuePropertiesForPage.........");
		return keyValuePropertieList;
	}

	/**
	 * To add Key Value Data To Page.
	 * @param pageID String
	 * @param keyValueProperty KeyValueProperties
	 */
	public void addKeyValueDataToPage(final String pageID, final KeyValueProperties keyValueProperty) {
		LOGGER.info("Entering method addKeyValueDataToPage........");
		if (this.pageIdToKVDataMap == null) {
			this.pageIdToKVDataMap = new HashMap<String, List<KeyValueProperties>>();
		}
		List<KeyValueProperties> keyValuePropertiesList = this.pageIdToKVDataMap.get(pageID);
		if (keyValuePropertiesList == null) {
			keyValuePropertiesList = new ArrayList<KeyValueProperties>();
			this.pageIdToKVDataMap.put(pageID, keyValuePropertiesList);
		}
		if (keyValueProperty != null) {
			keyValuePropertiesList.add(keyValueProperty);
			LOGGER.info("KeyValueProperty added to page id: " + pageID);
		}
		LOGGER.info("Exiting method addKeyValueDataToPage.........");
	}

	/**
	 * This is properties class for key value.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 */
	public class KeyValueProperties {

		/**
		 * keyCoordinates Coordinates.
		 */
		private Coordinates keyCoordinates;

		/**
		 * valueCoordinates Coordinates.
		 */
		private Coordinates valueCoordinates;

		/**
		 * To get Value Coordinates.
		 * @return Coordinates
		 */
		public Coordinates getValueCoordinates() {
			return valueCoordinates;
		}

		/**
		 * To set Value Coordinates.
		 * @param valueCoordinates Coordinates
		 */
		public void setValueCoordinates(Coordinates valueCoordinates) {
			this.valueCoordinates = valueCoordinates;
		}

		/**
		 * To get Key Coordinates.
		 * @return Coordinates
		 */
		public Coordinates getKeyCoordinates() {
			return keyCoordinates;
		}

		/**
		 * To set Key Coordinates.
		 * @param keyCoordinates Coordinates
		 */
		public void setKeyCoordinates(Coordinates keyCoordinates) {
			this.keyCoordinates = keyCoordinates;
		}

	}

}
