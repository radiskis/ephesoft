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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;

import com.ephesoft.dcma.constant.UtilConstants;

/**
 * This class handles Application Config Properties.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see org.springframework.core.io.ClassPathResource
 */
public final class ApplicationConfigProperties {

	/**
	 * serialVersionUID, constant long.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * properties Properties.
	 */
	private final Properties properties;

	/**
	 * applicationConfigurationProperties ApplicationConfigProperties.
	 */
	private static ApplicationConfigProperties applicationConfigurationProperties = null;

	/**
	 * To get properties.
	 * @return properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * Constructor.
	 * 
	 * @throws IOException {@link IOException} while reading from the file.
	 */
	private ApplicationConfigProperties() throws IOException {
		InputStream propertyInStream = null;
		this.properties = new Properties();
		try {
			String filePath = UtilConstants.META_INF + File.separator + UtilConstants.APPLICATION_PROPERTY_NAME + ".properties";
			propertyInStream = new ClassPathResource(filePath).getInputStream();
			this.properties.load(propertyInStream);
		} finally {
			if (propertyInStream != null) {
				propertyInStream.close();
			}
		}
	}

	/**
	 * This method gets all the properties.
	 * 
	 * @param propertyFileName {@link String}
	 * @return Properties
	 * @throws IOException while reading from the file.
	 */
	public Properties getAllProperties(String propertyFileName) throws IOException {
		InputStream propertyInStream = null;
		Properties properties = new Properties();
		try {
			String filePath = UtilConstants.META_INF + File.separator + propertyFileName + ".properties";
			propertyInStream = new ClassPathResource(filePath).getInputStream();
			properties.load(propertyInStream);
		} finally {
			if (propertyInStream != null) {
				propertyInStream.close();
			}
		}
		return properties;
	}

	/**
	 * This method gets Application Configuration Properties.
	 * 
	 * @return ApplicationConfigProperties
	 * @throws IOException {@link IOException}
	 */
	public static ApplicationConfigProperties getApplicationConfigProperties() throws IOException {
		if (applicationConfigurationProperties == null) {
			synchronized (ApplicationConfigProperties.class) {
				if (applicationConfigurationProperties == null) {
					applicationConfigurationProperties = new ApplicationConfigProperties();
				}
			}
		}
		return applicationConfigurationProperties;
	}

	/**
	 * This method gets the property.
	 * 
	 * @param propertyKey {@link String}
	 * @return {@link String}
	 * @throws IOException {@link IOException}
	 */
	public String getProperty(String propertyKey) throws IOException {
		return applicationConfigurationProperties.properties.getProperty(propertyKey);
	}

}
