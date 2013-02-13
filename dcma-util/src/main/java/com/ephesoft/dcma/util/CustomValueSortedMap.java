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

package com.ephesoft.dcma.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

import com.ephesoft.dcma.constant.UtilConstants;

/**
 * This is Custom Value Sorted Map Class to add new objects and reverse the map.
 *  
 * @author Ephesoft
 * @version 1.0
 * @see java.util.Comparator
 */
public class CustomValueSortedMap extends TreeSet<CustomMapClass> {

	/**
	 * serialVersionUID, constant long.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * maxValue int.
	 */
	private int maxValue;

	/**
	 * Constructor.
	 * 
	 * @param maxValue int
	 */
	public CustomValueSortedMap(int maxValue) {
		this(maxValue, new DoubleComparator());
	}

	/**
	 * Constructor.
	 * 
	 * @param maxValue int
	 * @param comparator Comparator<CustomMapClass> 
	 */
	public CustomValueSortedMap(int maxValue, Comparator<CustomMapClass> comparator) {
		super(comparator);
		this.maxValue = maxValue;
	}

	/**
	 * To add values.
	 * 
	 * @param clazz CustomMapClass
	 * @return boolean
	 */
	@Override
	public boolean add(CustomMapClass clazz) {
		boolean returnValue = true;
		if (maxValue == UtilConstants.ZERO) {
			returnValue = super.add(clazz);
		} else {
			if (super.size() == maxValue) {
				if (super.add(clazz)) {
					if (super.pollFirst() != null) {
						returnValue = true;
					} else {
						returnValue = false;
					}
				} else {
					Iterator<CustomMapClass> itr = super.iterator();
					while (itr.hasNext()) {
						CustomMapClass customMapClass = itr.next();
						if (customMapClass.equals(clazz) && clazz.getValue().compareTo(customMapClass.getValue()) > UtilConstants.ZERO) {
							super.remove(customMapClass);
							super.add(clazz);
							returnValue = true;
							break;
						}
					}
				}
			} else {
				returnValue = super.add(clazz);
			}
		}
		return returnValue;
	}

	/**
	 *  To add values.
	 * 
	 * @param key {@link String}
	 * @param value {@link Double}
	 * @return boolean
	 */
	public boolean add(String key, Double value) {
		CustomMapClass customMapClass = new CustomMapClass(key, value);
		return this.add(customMapClass);
	}

	/**
	 * To get values.
	 * 
	 * @return Map<String, Double> 
	 */
	public Map<String, Double> getMap() {
		Map<String, Double> linkedHashMap = new LinkedHashMap<String, Double>();
		Iterator<CustomMapClass> itr = super.iterator();
		while (itr.hasNext()) {
			CustomMapClass customMapClass = (CustomMapClass) itr.next();
			Double doubleValue = linkedHashMap.get(customMapClass.getKey());
			Double newValue = customMapClass.getValue();
			if (doubleValue == null) {
				linkedHashMap.put(customMapClass.getKey(), newValue);
			} else if (doubleValue.compareTo(newValue) < UtilConstants.ZERO) {
				linkedHashMap.put(customMapClass.getKey(), newValue);
			}
		}
		return linkedHashMap;
	}

	/**
	 * To get reverse sorted Map.
	 * 
	 * @return Map<String, Double>
	 */
	public Map<String, Double> getReverseSortedMap() {
		Map<String, Double> linkedHashMap = new LinkedHashMap<String, Double>();
		Iterator<CustomMapClass> itr = super.descendingIterator();
		while (itr.hasNext()) {
			CustomMapClass customMapClass = (CustomMapClass) itr.next();
			Double doubleValue = linkedHashMap.get(customMapClass.getKey());
			Double newValue = customMapClass.getValue();
			if (doubleValue == null) {
				linkedHashMap.put(customMapClass.getKey(), newValue);
			} else if (doubleValue.compareTo(newValue) < UtilConstants.ZERO) {
				linkedHashMap.put(customMapClass.getKey(), newValue);
			}
		}
		return linkedHashMap;
	}

	/**
	 * To get reverse sorted Map value in float.
	 * 
	 * @return Map<String, Float>
	 */
	public Map<String, Float> getReverseSortedMapValueInFloat() {
		Map<String, Float> linkedHashMap = new LinkedHashMap<String, Float>();
		Iterator<CustomMapClass> itr = super.descendingIterator();
		while (itr.hasNext()) {
			CustomMapClass customMapClass = (CustomMapClass) itr.next();
			Float doubleValue = linkedHashMap.get(customMapClass.getKey());
			Float newValue = (float) ((double) customMapClass.getValue());
			if (doubleValue == null) {
				linkedHashMap.put(customMapClass.getKey(), newValue);
			} else if (doubleValue.compareTo(newValue) < UtilConstants.ZERO) {
				linkedHashMap.put(customMapClass.getKey(), newValue);
			}
		}
		return linkedHashMap;
	}

}

/**
 * This class contains compare method.
 */
class DoubleComparator implements Comparator<CustomMapClass> {

	/**
	 * To compare 2 objects.
	 * 
	 * @param object1 CustomMapClass
	 * @param object CustomMapClass
	 * @return int
	 */
	@Override
	public int compare(CustomMapClass object1, CustomMapClass object) {
		int returnValue = object1.getValue().compareTo(object.getValue());
		if (returnValue == UtilConstants.ZERO) {
			returnValue = -UtilConstants.ONE;
		}
		return returnValue;
	}
}
