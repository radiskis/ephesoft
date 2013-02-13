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

package com.ephesoft.dcma.batch.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.batch.dao.PluginPropertiesDao;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchDynamicPluginConfiguration;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPlugin;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPluginConfiguration;
import com.ephesoft.dcma.core.common.PluginProperty;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.FieldType;
import com.ephesoft.dcma.da.domain.FunctionKey;

/**
 * This is service class to get the pugin properties and to clear them.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer
 * @see com.ephesoft.dcma.batch.dao.PluginPropertiesDao
 */
@Service("batchClassPluginPropertiesService")
public class BatchClassPluginPropertiesService implements PluginPropertiesService {

	/**
	 * A constant String to store message for message.
	 */
	private static final String UNSUPPORTED_METHOD = "This method is not supported for BatchClassPluginPropertiesService";
	
	/**
	 * pluginPropertiesDao {@link PluginPropertiesDao}.
	 */
	@Autowired
	@Qualifier("batchClassPluginPropertiesDao")
	private PluginPropertiesDao pluginPropertiesDao;

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchClassPluginPropertiesService.class);

	/**
	 * boolean field to get if debug mode of logging is enabled.
	 */
	private static final boolean IS_DEBUG_ENABLE = LOGGER.isDebugEnabled();

	/**
	 * This method returns plugin property value.
	 * 
	 * @param batchIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @param pluginProperty {@link PluginProperty}
	 * @return {@link String}
	 */
	@Transactional
	@Override
	@Deprecated
	public String getPropertyValue(final String batchIdentifier, final String pluginName, final PluginProperty pluginProperty) {
		String loggingPrefix = null;
		if (IS_DEBUG_ENABLE) {
			loggingPrefix = BatchConstants.LOG_AREA + batchIdentifier;
			LOGGER.debug(loggingPrefix + " : Executing getPropertyValue API.");
			StringBuilder logMsg = new StringBuilder(loggingPrefix);
			logMsg.append(BatchConstants.PLUGIN_NAME);
			logMsg.append(pluginName);
			logMsg.append(" and pluginProperty key is : ");
			logMsg.append(pluginProperty.getPropertyKey());
			LOGGER.debug(logMsg.toString());
		}
		final BatchPluginPropertyContainer container = pluginPropertiesDao.getPluginProperties(batchIdentifier);
		String propertyValue = container.getPlginConfiguration(pluginName, pluginProperty).get(BatchConstants.ZERO).getValue();
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix + " : Returning from getPropertyValue. Property value is " + propertyValue);
		}
		return propertyValue;
	}

	/**
	 * This method clears the plugin properties of a batch.
	 * @param batchIdentifier {@link String}
	 */
	@Override
	public void clearCache(final String batchIdentifier) {
		String loggingPrefix = null;
		if (IS_DEBUG_ENABLE) {
			loggingPrefix = BatchConstants.LOG_AREA + batchIdentifier;
			LOGGER.debug(loggingPrefix + " : Executing clearCache API.");
		}
		pluginPropertiesDao.clearPluginProperties(batchIdentifier);
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix + " : Cleared plugin properties. Executed clearCache API successfully.");
		}
	}

	/**
	 * This method returns plugin property container for a batch.
	 * @param batchIdentifier {@link String}
	 * @return {@link BatchPluginPropertyContainer}
	 */
	@Transactional
	@Override
	public BatchPluginPropertyContainer getPluginProperties(final String batchIdentifier) {
		String loggingPrefix = null;
		if (IS_DEBUG_ENABLE) {
			loggingPrefix = BatchConstants.LOG_AREA + batchIdentifier;
			LOGGER.debug(loggingPrefix + " : Executing getPluginProperties(String batchIdentifier) API.");
		}
		BatchPluginPropertyContainer batchPluginPropertyContainer = pluginPropertiesDao.getPluginProperties(batchIdentifier);
		if (IS_DEBUG_ENABLE) {
			LOGGER
					.debug(loggingPrefix
							+ " : Executed getPluginProperties(String batchIdentifier) API. Returning plugin properties container. Number of document types in container is "
							+ batchPluginPropertyContainer.getAllDocumentTypes().size());
		}
		return batchPluginPropertyContainer;
	}

	/**
	 * This method returns plugin property container for a batch.
	 * @param batchIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @return {@link BatchPlugin}
	 */
	@Transactional
	@Override
	public BatchPlugin getPluginProperties(final String batchIdentifier, final String pluginName) {
		String loggingPrefix = null;
		if (IS_DEBUG_ENABLE) {
			loggingPrefix = BatchConstants.LOG_AREA + batchIdentifier;
			LOGGER.debug(loggingPrefix
					+ " : Executing getPluginProperties(String batchIdentifier, String pluginName) API. Plugin name is " + pluginName);
		}
		BatchPlugin batchPlugin = pluginPropertiesDao.getPluginProperties(batchIdentifier).getPlugin(pluginName);
		if (IS_DEBUG_ENABLE) {
			LOGGER
					.debug(loggingPrefix
							+ " : Executed getPluginProperties(String batchIdentifier, String pluginName) API. Returning plugin. Plugin property size is "
							+ batchPlugin.getPropertiesSize());
		}
		return batchPlugin;
	}

	/**
	 * This method returns plugin property container for a batch.
	 * @param batchIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @param pluginProperty {@link PluginProperty}
	 * @return {@link BatchPluginConfiguration[]}
	 */
	@Transactional
	@Override
	public BatchPluginConfiguration[] getPluginProperties(final String batchIdentifier, final String pluginName,
			final PluginProperty pluginProperty) {
		String loggingPrefix = null;
		if (IS_DEBUG_ENABLE) {
			loggingPrefix = BatchConstants.LOG_AREA + batchIdentifier;
			LOGGER
					.debug(loggingPrefix
							+ " : Executing getPluginProperties(String batchIdentifier, String pluginName, PluginProperty pluginProperty) API.");
			StringBuilder logMsg = new StringBuilder(loggingPrefix);
			logMsg.append(BatchConstants.PLUGIN_NAME);
			logMsg.append(pluginName);
			logMsg.append(" and pluginProperty key is : ");
			logMsg.append(pluginProperty.getPropertyKey());
			LOGGER.debug(logMsg.toString());
		}
		List<BatchPluginConfiguration> configurations = pluginPropertiesDao.getPluginProperties(batchIdentifier).getPlugin(pluginName)
				.getPluginConfigurations(pluginProperty);
		if (configurations == null) {
			if (IS_DEBUG_ENABLE) {
				LOGGER.debug(loggingPrefix + " : plugin cofiguration not found for this plugin. Creating new configurations.");
			}
			configurations = new ArrayList<BatchPluginConfiguration>(0);
		}
		BatchPluginConfiguration[] batchPluginConfiguration = configurations
				.toArray(new BatchPluginPropertyContainer.BatchPluginConfiguration[configurations.size()]);
		if (IS_DEBUG_ENABLE) {
			LOGGER
					.debug(loggingPrefix
							+ " : Executed getPluginProperties(String batchIdentifier, String pluginName, PluginProperty pluginProperty) API successfully. Returning Batch Plugin Configurations with size"
							+ batchPluginConfiguration.length);
		}
		return batchPluginConfiguration;
	}

	/**
	 * To get Document Type.
	 * @param batchIdentifier String
	 * @return List<com.ephesoft.dcma.da.domain.DocumentType>
	 * @throws UnsupportedOperationException when method is not supported.
	 */
	@Override
	public List<com.ephesoft.dcma.da.domain.DocumentType> getDocumentTypes(final String batchIdentifier) {
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(BatchConstants.LOG_AREA + batchIdentifier
					+ " : Executing getDocumentTypes API. Throwing unsupported exception.");

		}
		throw new UnsupportedOperationException(UNSUPPORTED_METHOD);
	}

	/**
	 * To get Field Type.
	 * @param batchIdentifier String
	 * @param docTypeName String
	 * @return List<FieldType>
	 * @throws UnsupportedOperationException when method is not supported.
	 */
	@Override
	public List<FieldType> getFieldTypes(final String batchIdentifier, final String docTypeName) {
		if (IS_DEBUG_ENABLE) {
			LOGGER
					.debug(BatchConstants.LOG_AREA + batchIdentifier
							+ " : Executing getFieldTypes API. Throwing unsupported exception.");
		}
		throw new UnsupportedOperationException(UNSUPPORTED_METHOD);
	}

	/**
	 * To get Field Type and KV Extractions.
	 * @param batchIdentifier String
	 * @param docTypeName String
	 * @return List<com.ephesoft.dcma.da.domain.FieldType>
	 * @throws UnsupportedOperationException when method is not supported.
	 */
	@Override
	public List<com.ephesoft.dcma.da.domain.FieldType> getFieldTypeAndKVExtractions(final String batchIdentifier,
			final String docTypeName) {
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(BatchConstants.LOG_AREA + batchIdentifier
					+ " : Executing getFieldTypeAndKVExtractions API. Throwing unsupported exception.");
		}
		throw new UnsupportedOperationException(UNSUPPORTED_METHOD);
	}

	/**
	 * To get Page Type.
	 * @param batchIdentifier String
	 * @return List<com.ephesoft.dcma.da.domain.PageType>
	 * @throws UnsupportedOperationException when method is not supported.
	 */
	@Override
	public List<com.ephesoft.dcma.da.domain.PageType> getPageTypes(final String batchIdentifier) {
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(BatchConstants.LOG_AREA + batchIdentifier + " : Executing getPageTypes API. Throwing unsupported exception.");
		}
		throw new UnsupportedOperationException(UNSUPPORTED_METHOD);
	}

	/**
	 * To get Document Type By Name.
	 * @param batchIdentifier String
	 * @param docTypeName String
	 * @return List<com.ephesoft.dcma.da.domain.DocumentType>
	 * @throws UnsupportedOperationException when method is not supported.
	 */
	@Override
	public List<com.ephesoft.dcma.da.domain.DocumentType> getDocumentTypeByName(final String batchIdentifier, final String docTypeName) {
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(BatchConstants.LOG_AREA + batchIdentifier
					+ " : Executing getDocumentTypeByName API. Throwing unsupported exception.");
		}
		throw new UnsupportedOperationException(UNSUPPORTED_METHOD);
	}

	/**
	 * To get Doc Type By Page Type Name.
	 * @param batchIdentifier String
	 * @param pageTypeName String
	 * @return List<DocumentType>
	 * @throws UnsupportedOperationException when method is not supported.
	 */
	@Override
	public List<DocumentType> getDocTypeByPageTypeName(final String batchIdentifier, final String pageTypeName) {
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(BatchConstants.LOG_AREA + batchIdentifier
					+ " : Executing getDocTypeByPageTypeName API. Throwing unsupported exception.");
		}
		throw new UnsupportedOperationException(UNSUPPORTED_METHOD);
	}

	/**
	 * This method is to get the dynamic plugin properties for the batch.
	 * @param batchIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @param pluginProperty {@link PluginProperty}
	 * @return {@link BatchDynamicPluginConfiguration[]}
	 */
	@Transactional
	@Override
	public BatchDynamicPluginConfiguration[] getDynamicPluginProperties(final String batchIdentifier, final String pluginName,
			final PluginProperty pluginProperty) {
		String loggingPrefix = null;
		if (IS_DEBUG_ENABLE) {
			loggingPrefix = BatchConstants.LOG_AREA + batchIdentifier;
			StringBuilder logMsg = new StringBuilder(loggingPrefix);
			logMsg.append(" : Executing getDynamicPluginProperties API. Plugin name and pluginProperty key are ");
			logMsg.append(pluginName);
			logMsg.append(", ");
			logMsg.append(pluginProperty.getPropertyKey());
			LOGGER.debug(logMsg.toString());
		}
		List<BatchDynamicPluginConfiguration> configurations = pluginPropertiesDao.getPluginProperties(batchIdentifier).getPlugin(
				pluginName).getDynamicPluginConfigurations(pluginProperty);
		if (configurations == null) {
			if (IS_DEBUG_ENABLE) {
				LOGGER.debug(loggingPrefix + " : plugin configuration not found for this plugin. Creating new configurations.");
			}
			configurations = new ArrayList<BatchDynamicPluginConfiguration>(0);
		}
		BatchDynamicPluginConfiguration[] batchDynamicPluginConfiguration = configurations
				.toArray(new BatchPluginPropertyContainer.BatchDynamicPluginConfiguration[configurations.size()]);
		if (IS_DEBUG_ENABLE) {
			LOGGER
					.debug(loggingPrefix
							+ " : Executed getDynamicPluginProperties API successfully. Returning Batch dynamic plugin configurations with size "
							+ batchDynamicPluginConfiguration.length);
		}
		return batchDynamicPluginConfiguration;
	}

	/**
	 * To get the function keys.
	 * @param batchIdentifier {@link String}
	 * @param docTypeName {@link String}
	 * @return List<FunctionKey>
	 * @throws UnsupportedOperationException when method is not supported.
	 */
	@Override
	public List<FunctionKey> getFunctionKeys(final String batchIdentifier, final String docTypeName) {
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(BatchConstants.LOG_AREA + batchIdentifier
					+ " : Executing getFunctionKeys API. Throwing unsupported exception.");
		}
		throw new UnsupportedOperationException("This method is not supported for BatchClassPluginPropertiesService");

	}
}
