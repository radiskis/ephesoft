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

package com.ephesoft.dcma.core.dao.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;

import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.component.JAXB2Template;
import com.ephesoft.dcma.core.dao.Dao;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.XMLUtil;

/**
 * This is generic class for XML dao.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.dao.Dao
 * @param <T>
 */
public abstract class XmlDao<T> implements Dao<T> {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(XmlDao.class);
	
	/** 
	 * OPERATION_NOT_SUPPORTED String.
	 */
	private static final String OPERATION_NOT_SUPPORTED = "Operation not supported.";

	/**
	 * To create new objects.
	 * @param object T
	 * @param identifier Serializable
	 */
	public void create(T object, Serializable identifier) {
		create(object, identifier, null, ICommonConstants.UNDERSCORE_BATCH_XML, false);
	}

	/**
	 * To create new objects.
	 * @param object T
	 * @param identifier Serializable
	 * @param pageId String
	 * @param fileName String 
	 * @param isFirstTimeUpdate boolean
	 */
	public void create(T object, Serializable identifier, String pageId, String fileName, boolean isFirstTimeUpdate) {
		create(object, identifier, null, ICommonConstants.UNDERSCORE_BATCH_XML, false, getJAXB2Template().getLocalFolderLocation());
	}

	/**
	 * To create new objects.
	 * @param object T
	 * @param identifier Serializable
	 * @param pageId String
	 * @param fileName String 
	 * @param isFirstTimeUpdate boolean
	 * @param localFolderPath String
	 */
	public void create(T object, Serializable identifier, String pageId, String fileName, boolean isFirstTimeUpdate,
			String localFolderPath) {
		LOGGER.info("Entering create method.");
		OutputStream stream = null;
		try {
			String filePath = null;
			if (null == pageId) {
				filePath = localFolderPath + File.separator + identifier + File.separator + identifier + fileName;
			} else {
				filePath = localFolderPath + File.separator + identifier + File.separator + identifier + "_" + pageId + fileName;
			}
			File xmlFile = new File(filePath);
			boolean isZip = false;
			boolean isZipSwitchOn = isZipSwitchOn();
			if (isZipSwitchOn) {
				if (isFirstTimeUpdate && !xmlFile.exists()) {
					isZip = true;
				} else if (FileUtils.isZipFileExists(filePath)) {
					isZip = true;
				}
			} else if (isFirstTimeUpdate && FileUtils.isZipFileExists(filePath)) {
				isZip = true;
			}

			if (isZip) {
				stream = FileUtils.getOutputStreamFromZip(filePath, identifier + fileName);
			} else {
				stream = new FileOutputStream(xmlFile);
			}

			StreamResult result = new StreamResult(stream);
			getJAXB2Template().getJaxb2Marshaller().marshal(object, result);
		} catch (FileNotFoundException e) {
			throw new DCMABusinessException(e.getMessage(), e);
		} catch (Exception e) {
			throw new DCMABusinessException(e.getMessage(), e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					LOGGER.error("Error closing steam. " + e.getMessage(), e);
				}
			}
		}
		LOGGER.info("Exiting create method.");

	}

	/**
	 * To get the object.
	 * @param identifier Serializable
	 */
	@Override
	public T get(Serializable identifier) {
		return get(identifier, null, ICommonConstants.UNDERSCORE_BATCH_XML, getJAXB2Template().getLocalFolderLocation());
	}

	/**
	 * To get the object.
	 * @param identifier Serializable
	 * @param localFolder String
	 */
	public T get(Serializable identifier, String localFolder) {
		return get(identifier, null, ICommonConstants.UNDERSCORE_BATCH_XML, localFolder);
	}

	/**
	 * To get the object.
	 * @param identifier Serializable
	 * @param localFolder String
	 * @param pageId String
	 * @param fileName String
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	public T get(Serializable identifier, String pageId, String fileName, String localFolder) {
		LOGGER.info("Entering get method.");
		boolean isZipSwitchOn = true;
		isZipSwitchOn = isZipSwitchOn();

		InputStream inputStream = null;
		String filePath = null;
		try {

			if (null == pageId) {
				filePath = localFolder + File.separator + identifier + File.separator + identifier + fileName;
			} else {
				filePath = localFolder + File.separator + identifier + File.separator + identifier + ICommonConstants.UNDERSCORE + pageId + fileName;
			}
			File xmlFile = new File(filePath);
			LOGGER.info("FilePath in get batch object is : " + filePath);
			LOGGER.info("Zip switch in get batch object is : " + isZipSwitchOn);

			boolean isZip = false;

			if (isZipSwitchOn) {
				if (FileUtils.isZipFileExists(filePath)) {
					isZip = true;
				}
			} else {
				if (xmlFile.exists()) {
					isZip = false;
				} else if (FileUtils.isZipFileExists(filePath)) {
					isZip = true;
				}
			}

			if (isZip) {
				inputStream = FileUtils.getInputStreamFromZip(filePath, identifier + fileName);
			} else {
				inputStream = new FileInputStream(xmlFile);
			}

			Source source = XMLUtil.createSourceFromStream(inputStream);
			return (T) getJAXB2Template().getJaxb2Marshaller().unmarshal(source);
		} catch (Exception e) {
			throw new DCMABusinessException(e.getMessage(), e);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException ioe) {
				LOGGER.info("Exception in closing inputstream in xml dao. Filename is:" + filePath);
			}
			LOGGER.info("Exiting get method.");
		}
	}

	/**
	 * To check whether zip switch is on or not.
	 * @return boolean
	 */
	private boolean isZipSwitchOn() {
		boolean isZipSwitchOn = false;
		try {
			ApplicationConfigProperties prop = ApplicationConfigProperties.getApplicationConfigProperties();
			isZipSwitchOn = Boolean.parseBoolean(prop.getProperty(ICommonConstants.ZIP_SWITCH));
		} catch (IOException ioe) {
			LOGGER.error("Unable to read the zip switch value. Taking default value as true.Exception thrown is:" + ioe.getMessage(),
					ioe);
		}
		return isZipSwitchOn;
	}

	/**
	 * API for getting the Object providing the filePath.
	 * 
	 * @param filePath String
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	public T getObjectFromFilePath(String filePath) {
		LOGGER.info("Entering get method.");
		InputStream inputStream = null;
		try {

			File xmlFile = new File(filePath);
			LOGGER.info("FilePath in get batch object is : " + filePath);

			inputStream = new FileInputStream(xmlFile);

			Source source = XMLUtil.createSourceFromStream(inputStream);
			return (T) getJAXB2Template().getJaxb2Marshaller().unmarshal(source);
		} catch (Exception e) {
			throw new DCMABusinessException(e.getMessage(), e);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException ioe) {
				LOGGER.info("Exception in closing inputstream in xml dao. Filename is:" + filePath);
			}
			LOGGER.info("Exiting get method.");
		}
	}

	/**
	 * To get all objects satisfying criteria.
	 * @return List<T>
	 */
	@Override
	public List<T> getAll() {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED);
	}

	/**
	 * To merge objects.
	 * @param object T
	 * @return T
	 */
	@Override
	public T merge(T object) {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED);
	}

	/**
	 * To remove objects from the list.
	 * @param object T
	 */
	@Override
	public void remove(T object) {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED);
	}

	/**
	 * To save or update an object.
	 * @param object T
	 */
	@Override
	public void saveOrUpdate(T object) {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED);
	}

	/**
	 * To create new objects.
	 * @param object T
	 */
	@Override
	public void create(T object) {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED);
	}

	/**
	 * To evict an object.
	 * @param object object
	 */
	@Override
	public void evict(Object object) {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED);
	}

	/**
	 * To count.
	 * @return int
	 */
	@Override
	public int countAll() {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED);
	}

	/**
	 * To count.
	 * @param criteria DetachedCriteria
	 * @return int
	 */
	@Override
	public int count(DetachedCriteria criteria) {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED);
	}

	/**
	 * To update.
	 * @param object T
	 * @param identifier Serializable
	 */
	public void update(T object, Serializable identifier) {
		update(object, identifier, ICommonConstants.UNDERSCORE_BATCH_XML);
	}

	/**
	 * To update.
	 * @param object T
	 * @param identifier Serializable
	 * @param fileName String
	 */
	public void update(T object, Serializable identifier, String fileName) {
		update(object, identifier, fileName, false, getJAXB2Template().getLocalFolderLocation());
	}

	/**
	 * To update.
	 * @param object T
	 * @param identifier Serializable
	 * @param isFirstTimeUpdate boolean
	 * @param localFolder String
	 */
	public void update(T object, Serializable identifier, boolean isFirstTimeUpdate, String localFolder) {
		update(object, identifier, ICommonConstants.UNDERSCORE_BATCH_XML, null, isFirstTimeUpdate, localFolder);
	}

	/**
	 * To update.
	 * @param object T
	 * @param identifier Serializable
	 * @param fileName String
	 * @param isFirstTimeUpdate boolean
	 * @param localFolder String
	 */
	public void update(T object, Serializable identifier, String fileName, boolean isFirstTimeUpdate, String localFolder) {
		update(object, identifier, fileName, null, isFirstTimeUpdate, localFolder);
	}

	/**
	 * To update.
	 * @param object T
	 * @param identifier Serializable
	 * @param fileName String
	 * @param isFirstTimeUpdate boolean
	 * @param pageId String
	 */
	public void update(T object, Serializable identifier, String fileName, String pageId, boolean isFirstTimeUpdate) {
		update(object, identifier, fileName, null, isFirstTimeUpdate, getJAXB2Template().getLocalFolderLocation());
	}

	/**
	 * To update.
	 * @param object T
	 * @param identifier Serializable
	 * @param fileName String
	 * @param isFirstTimeUpdate boolean
	 * @param pageId String
	 * @param localFolder String
	 */
	public void update(T object, Serializable identifier, String fileName, String pageId, boolean isFirstTimeUpdate, String localFolder) {
		LOGGER.info("Entering update method.");
		OutputStream stream = null;
		try {
			String filePath = null;
			if (pageId == null) {
				filePath = localFolder + File.separator + identifier + File.separator + identifier + fileName;
			} else {
				filePath = localFolder + File.separator + identifier + File.separator + identifier + "_" + pageId + fileName;
			}
			File xmlFile = new File(filePath);
			boolean isZip = false;
			boolean isZipSwitchOn = isZipSwitchOn();
			if (isZipSwitchOn) {
				if (isFirstTimeUpdate && !xmlFile.exists()) {
					isZip = true;
				} else if (FileUtils.isZipFileExists(filePath)) {
					isZip = true;
				}
			} else {
				if (isFirstTimeUpdate && !FileUtils.isZipFileExists(filePath)) {
					isZip = false;
				} else if (xmlFile.exists()) {
					isZip = false;
				} else {
					isZip = true;
				}
			}

			if (isZip) {
				stream = FileUtils.getOutputStreamFromZip(filePath, identifier + fileName);
			} else {
				stream = new FileOutputStream(xmlFile);
			}

			StreamResult result = new StreamResult(stream);
			getJAXB2Template().getJaxb2Marshaller().marshal(object, result);
		} catch (FileNotFoundException e) {
			throw new DCMABusinessException(e.getMessage(), e);
		} catch (Exception e) {
			throw new DCMABusinessException(e.getMessage(), e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					LOGGER.info("Exception in closing outputstream in xml dao. Filename is:" + fileName);
				}
			}
		}
		LOGGER.info("Exiting update method.");
	}

	/**
	 * To update.
	 * @param object T
	 * @param filePath String
	 */
	public void update(final T object, final String filePath) {
		LOGGER.info("Entering update method.");
		if (filePath == null || filePath.isEmpty()) {
			LOGGER.info("File path is either null or empty.");
		} else {
			LOGGER.info("Updating file: " + filePath);
			OutputStream stream = null;
			try {
				File xmlFile = new File(filePath);
				stream = new FileOutputStream(xmlFile);
				StreamResult result = new StreamResult(stream);
				getJAXB2Template().getJaxb2Marshaller().marshal(object, result);
			} catch (FileNotFoundException e) {
				throw new DCMABusinessException(e.getMessage(), e);
			} catch (Exception e) {
				throw new DCMABusinessException(e.getMessage(), e);
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						LOGGER.info("Exception in closing outputstream in xml dao. File: " + filePath);
					}
				}
			}
		}
		LOGGER.info("Exiting update method.");
	}

	/**
	 * To get JAXB2 Template.
	 * @return JAXB2Template
	 */
	public abstract JAXB2Template getJAXB2Template();

}
