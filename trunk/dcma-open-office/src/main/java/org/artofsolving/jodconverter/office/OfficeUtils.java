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

//
// JODConverter - Java OpenDocument Converter
// Copyright 2009 Art of Solving Ltd
// Copyright 2004-2009 Mirko Nasato
//
// JODConverter is free software: you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public License
// as published by the Free Software Foundation, either version 3 of
// the License, or (at your option) any later version.
//
// JODConverter is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General
// Public License along with JODConverter.  If not, see
// <http://www.gnu.org/licenses/>.
//
package org.artofsolving.jodconverter.office;

import java.io.File;
import java.util.Map;

import org.artofsolving.jodconverter.util.PlatformUtils;

import com.sun.star.beans.PropertyValue;
import com.sun.star.uno.UnoRuntime;

/**
 * Utility class.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.sun.star.beans.PropertyValue
 */
public final class OfficeUtils {

	/**
	 * SERVICE_DESKTOP String.
	 */
	public static final String SERVICE_DESKTOP = "com.sun.star.frame.Desktop";

	private OfficeUtils() {
		throw new AssertionError("utility class must not be instantiated");
	}

	/**
	 * To cast.
	 * 
	 * @param <T>
	 * @param type Class<T>
	 * @param object Object
	 * @return <T> T
	 */
	@SuppressWarnings("unchecked")
	public static <T> T cast(Class<T> type, Object object) {
		return (T) UnoRuntime.queryInterface(type, object);
	}

	/**
	 * Property method.
	 * @param name String
	 * @param value Object
	 * @return PropertyValue
	 */
	public static PropertyValue property(String name, Object value) {
		PropertyValue propertyValue = new PropertyValue();
		propertyValue.Name = name;
		propertyValue.Value = value;
		return propertyValue;
	}

	/**
	 * To Uno Properties.
	 * @param properties Map<String, ?>
	 * @return PropertyValue[]
	 */
	public static PropertyValue[] toUnoProperties(Map<String, ?> properties) {
		PropertyValue[] propertyValues = new PropertyValue[properties.size()];
		int index = 0;
		for (Map.Entry<String, ?> entry : properties.entrySet()) {
			Object value = entry.getValue();
			if (value instanceof Map<?, ?>) {
				@SuppressWarnings("unchecked")
				Map<String, Object> subProperties = (Map<String, Object>) value;
				value = toUnoProperties(subProperties);
			}
			propertyValues[index++] = property((String) entry.getKey(), value);
		}
		return propertyValues;
	}

	/**
	 * To convert to URL.
	 * @param file File
	 * @return String
	 */
	public static String toUrl(File file) {
		String url = "file:///" + file.getPath();
		url = url.replace('\\', '/');
		if (url.endsWith("/")) {
			url = url.substring(0, url.length() - 1);
		}
		return url;
	}

	/**
	 * To get Default Office Home.
	 * @return File
	 */
	public static File getDefaultOfficeHome() {
		File officeHomeFile = null;
		if (System.getProperty("office.home") != null) {
			officeHomeFile = new File(System.getProperty("office.home"));
		} else if (PlatformUtils.isWindows()) {
			officeHomeFile = new File(System.getenv("ProgramFiles"), "OpenOffice.org 3");
		} else if (PlatformUtils.isMac()) {
			officeHomeFile = new File("/Applications/OpenOffice.org.app/Contents");
		} else {
			// Linux or Solaris
			officeHomeFile = new File("/opt/openoffice.org3");
		}
		return officeHomeFile;
	}

	/**
	 * To get Default Profile Directory.
	 * @return File
	 */
	public static File getDefaultProfileDir() {
		File officeProfileFile = null;
		if (System.getProperty("office.profile") != null) {
			officeProfileFile = new File(System.getProperty("office.profile"));
		} else if (PlatformUtils.isWindows()) {
			officeProfileFile = new File(System.getenv("APPDATA"), "OpenOffice.org/3");
		} else if (PlatformUtils.isMac()) {
			officeProfileFile = new File(System.getProperty("user.home"), "Library/Application Support/OpenOffice.org/3");
		} else {
			// Linux or Solaris
			officeProfileFile = new File(System.getProperty("user.home"), ".openoffice.org/3");
		}
		return officeProfileFile;
	}

	/**
	 * To get Office Executable.
	 * @param officeHome File
	 * @return File
	 */
	public static File getOfficeExecutable(File officeHome) {
		File file = null;
		if (PlatformUtils.isMac()) {
			file = new File(officeHome, "MacOS/soffice.bin");
		} else {
			file = new File(officeHome, "program/soffice.bin");
		}
		return file;
	}

}
