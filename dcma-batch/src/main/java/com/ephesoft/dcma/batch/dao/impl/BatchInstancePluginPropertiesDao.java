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

package com.ephesoft.dcma.batch.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.batch.dao.PluginPropertiesDao;
import com.ephesoft.dcma.da.dao.BatchClassDynamicPluginConfigDao;
import com.ephesoft.dcma.da.dao.BatchClassPluginConfigDao;
import com.ephesoft.dcma.da.dao.BatchInstanceDao;
import com.ephesoft.dcma.da.domain.BatchClassDynamicPluginConfig;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.KVPageProcess;
import com.ephesoft.dcma.da.service.DocumentTypeService;
import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.KeyGenerator;
import com.googlecode.ehcache.annotations.Property;
import com.googlecode.ehcache.annotations.TriggersRemove;

/**
 * This class is to get values of batch instance objects from serialized file.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.batch.dao.PluginPropertiesDao
 */
@Repository("batchInstancePluginPropertiesDao")
public class BatchInstancePluginPropertiesDao implements PluginPropertiesDao {

	/**
	 * EXTN String.
	 */
	public static final String EXTN = "ser";

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchInstancePluginPropertiesDao.class);

	/**
	 * boolean field to get if debug mode of logging is enabled.
	 */
	private static final boolean IS_DEBUG_ENABLE = LOGGER.isDebugEnabled();

	/**
	 * Instance of {@link DocumentTypeService}.
	 */
	@Autowired
	private DocumentTypeService documentTypeService;

	/**
	 * Instance of {@link BatchClassPluginConfigDao}.
	 */
	@Autowired
	private BatchClassPluginConfigDao batchClassPluginConfigDao;

	/**
	 * Instance of {@link BatchClassDynamicPluginConfigDao}.
	 */
	@Autowired
	private BatchClassDynamicPluginConfigDao batchClassDynamicPluginConfigDao;

	/**
	 * Instance of {@link batchInstanceDao}.
	 */
	@Autowired
	private BatchInstanceDao batchInstanceDao;

	/**
	 * This method is to get the plugin properties.
	 * 
	 * @param batchInstanceIdentifier String.
	 * @return {@link BatchPluginPropertyContainer}
	 */
	@Cacheable(cacheName = "pluginPropertiesCache", keyGenerator = @KeyGenerator(name = "StringCacheKeyGenerator", properties = @Property(name = "includeMethod", value = "false")))
	public BatchPluginPropertyContainer getPluginProperties(final String batchInstanceIdentifier) {
		String loggingPrefix = BatchConstants.LOG_AREA + batchInstanceIdentifier;
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix + " : Executing getPluginProperties(String) API of BatchInstancePluginPropertiesDao.");
		}
		BatchPluginPropertyContainer container = null;
		final String localFolderLocation = batchInstanceDao.getSystemFolderForBatchInstanceId(batchInstanceIdentifier);
		boolean isContainerCreated = false;
		final String pathToPropertiesFolder = localFolderLocation + File.separator + "properties";
		final File file = new File(pathToPropertiesFolder);
		if (!file.exists()) {
			file.mkdir();
			if (IS_DEBUG_ENABLE) {
				LOGGER.debug(loggingPrefix + " : folder created " + pathToPropertiesFolder);
			}
		}
		final File serializedFile = new File(pathToPropertiesFolder + File.separator + batchInstanceIdentifier + '.' + EXTN);
		if (serializedFile.exists()) {
			FileInputStream fileInputStream = null;
			try {
				fileInputStream = new FileInputStream(serializedFile);
				container = (BatchPluginPropertyContainer) SerializationUtils.deserialize(fileInputStream);
				isContainerCreated = true;
			} catch (Exception exception) {
				LOGGER.error(loggingPrefix + " : Error during de-serializing the properties for this Batch instance: ", exception);
				isContainerCreated = false;
			} finally {
				try {
					if (fileInputStream != null) {
						fileInputStream.close();
					}
				} catch (IOException exception) {
					LOGGER.error(loggingPrefix + " : Problem closing stream for file : " + serializedFile.getName(), exception);
				}
			}
		}
		if (!isContainerCreated) {
			final List<BatchClassPluginConfig> batchClassPluginConfigs = batchClassPluginConfigDao
					.getAllPluginPropertiesForBatchInstance(batchInstanceIdentifier);
			// to lazily load KVPageProcess objects
			for (final BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
				for (final KVPageProcess eachKvPageProcess : batchClassPluginConfig.getKvPageProcesses()) {
					if (IS_DEBUG_ENABLE && eachKvPageProcess != null) {
						LOGGER.debug(loggingPrefix + " : Key pattern is " + eachKvPageProcess.getKeyPattern());
					}
				}
			}

			final List<BatchClassDynamicPluginConfig> batchClassDynamicPluginConfigs = batchClassDynamicPluginConfigDao
					.getAllDynamicPluginPropertiesForBatchInstance(batchInstanceIdentifier);
			container = new BatchPluginPropertyContainer(String.valueOf(batchInstanceIdentifier));
			container.populate(batchClassPluginConfigs);
			container.populateDynamicPluginConfigs(batchClassDynamicPluginConfigs);
			final List<DocumentType> documentTypes = documentTypeService.getDocTypeByBatchInstanceIdentifier(batchInstanceIdentifier);
			container.populateDocumentTypes(documentTypes, batchInstanceIdentifier);
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(serializedFile);
				SerializationUtils.serialize(container, fileOutputStream);
			} catch (Exception exception) {
				LOGGER.error(loggingPrefix + " : Error during serializing properties for this Batch instance.", exception);
			} finally {
				try {
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
				} catch (IOException exception) {
					LOGGER.error(loggingPrefix + " : Problem closing stream for file " + serializedFile.getName(), exception);
				}
			}
		}
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix
					+ " : Executed getPluginProperties(String) API of BatchInstancePluginPropertiesDao successfully.");
		}
		return container;
	}

	/**
	 * This method is to clear the plugin properties.
	 * 
	 * @param batchInstanceIdentifier String.
	 */
	@TriggersRemove(cacheName = "pluginPropertiesCache", keyGenerator = @KeyGenerator(name = "StringCacheKeyGenerator", properties = @Property(name = "includeMethod", value = "false")))
	public void clearPluginProperties(final String batchInstanceIdentifier) {
		if (IS_DEBUG_ENABLE) {
			String loggingPrefix = BatchConstants.LOG_AREA + batchInstanceIdentifier;
			LOGGER.debug(loggingPrefix + " : Executing clearPluginProperties(String) API of BatchInstancePluginPropertiesDao.");
			LOGGER.debug(loggingPrefix + " : Cleared the pluginPropertiesCache for this batch Instance.");
			LOGGER.debug(loggingPrefix
					+ " : Executed clearPluginProperties(String) API of BatchInstancePluginPropertiesDao successfully.");
		}
	}
}
