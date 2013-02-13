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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.DocumentType;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.PageType;
import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.LocationType;
import com.ephesoft.dcma.core.common.PluginProperty;
import com.ephesoft.dcma.da.domain.FieldType;
import com.ephesoft.dcma.da.domain.FunctionKey;
import com.ephesoft.dcma.da.domain.KVExtraction;
import com.ephesoft.dcma.da.domain.RegexValidation;
import com.ephesoft.dcma.da.service.BatchInstanceService;

/**
 * This is service for batch instance plugin properties.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.batch.dao.PluginPropertiesDao
 * @see com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer
 */
@Service("batchInstancePluginPropertiesService")
public class BatchInstancePluginPropertiesService implements PluginPropertiesService {

	/**
	 * Instance of PluginPropertiesDao.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesDao")
	private PluginPropertiesDao pluginPropertiesDao;

	/**
	 * Instance of batchInstanceService.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchInstancePluginPropertiesService.class);

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
	public String getPropertyValue(String batchIdentifier, String pluginName, PluginProperty pluginProperty) {
		String loggingPrefix = null;
		if (IS_DEBUG_ENABLE) {
			loggingPrefix = BatchConstants.LOG_AREA + batchIdentifier;
			LOGGER.debug(loggingPrefix + " : Executing getPropertyValue API.");
			StringBuilder logMsg = new StringBuilder(loggingPrefix);
			logMsg.append(BatchConstants.PLUGIN_NAME);
			logMsg.append(pluginName);
			logMsg.append(" and pluginProperty key is ");
			logMsg.append(pluginProperty.getPropertyKey());
			LOGGER.debug(logMsg.toString());

		}
		String returnValue = null;
		String localFolderLocation = batchInstanceService.getSystemFolderForBatchInstanceId(batchIdentifier);
		String pathToPropertiesFolder = localFolderLocation + File.separator + "properties";
		File file = new File(pathToPropertiesFolder + File.separator + batchIdentifier + FileType.SER.getExtensionWithDot());
		if (!file.exists()) {
			if (IS_DEBUG_ENABLE) {
				LOGGER.debug(loggingPrefix + " : File does not exists. Path of file is " + file.getAbsolutePath());
			}
			pluginPropertiesDao.clearPluginProperties(batchIdentifier);
		}
		BatchPluginPropertyContainer container = pluginPropertiesDao.getPluginProperties(batchIdentifier);

		final List<BatchPluginConfiguration> pluginConfiguration = container.getPlginConfiguration(pluginName, pluginProperty);
		if (null != pluginConfiguration && !pluginConfiguration.isEmpty()) {
			returnValue = pluginConfiguration.get(BatchConstants.ZERO).getValue();
		}
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix + " : Executed getPropertyValue API successfully. Returning Property value, " + returnValue);
		}
		return returnValue;
	}

	/**
	 * This method clears the plugin properties of a batch.
	 * @param batchIdentifier {@link String}
	 */
	@Override
	public void clearCache(String batchIdentifier) {
		String loggingPrefix = null;
		if (IS_DEBUG_ENABLE) {
			loggingPrefix = BatchConstants.LOG_AREA + batchIdentifier;
			LOGGER.debug(loggingPrefix + " : Executing clearCache API. Clearing plugin properties of this batch.");
		}
		pluginPropertiesDao.clearPluginProperties(batchIdentifier);
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix + " : Executed clearCache API successfully.");
		}
	}

	/**
	 * This method returns plugin property container for a batch.
	 * @param batchIdentifier {@link String}
	 * @return {@link BatchPluginPropertyContainer}
	 */
	@Transactional
	@Override
	public BatchPluginPropertyContainer getPluginProperties(String batchIdentifier) {
		String loggingPrefix = null;
		if (IS_DEBUG_ENABLE) {
			loggingPrefix = BatchConstants.LOG_AREA + batchIdentifier;
			LOGGER.debug(loggingPrefix
					+ " : Executing getPluginProperties(String) API. Getting plugin property container for this batch.");
		}
		BatchPluginPropertyContainer batchPluginPropertyContainer = pluginPropertiesDao.getPluginProperties(batchIdentifier);
		if (IS_DEBUG_ENABLE) {
			if (batchPluginPropertyContainer != null && batchPluginPropertyContainer.getAllDocumentTypes() != null) {
				LOGGER
						.debug(loggingPrefix
								+ " : Executed getPluginProperties(String) API successfully. Returning batch plugin property container. Number of document type is "
								+ batchPluginPropertyContainer.getAllDocumentTypes().size());
			} else {
				LOGGER
						.debug(loggingPrefix
								+ " : Executed getPluginProperties(String) API successfully. Returning batch plugin property container. Either container is null or container has document types as null.");
			}
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
	public BatchPlugin getPluginProperties(String batchIdentifier, String pluginName) {
		String loggingPrefix = null;
		if (IS_DEBUG_ENABLE) {
			loggingPrefix = BatchConstants.LOG_AREA + batchIdentifier;
			LOGGER.debug(loggingPrefix + " : Executing getPluginProperties(String, String) API. plugin name is " + pluginName);
		}
		BatchPlugin batchPlugin = pluginPropertiesDao.getPluginProperties(batchIdentifier).getPlugin(pluginName);

		if (IS_DEBUG_ENABLE) {
			if (batchPlugin != null) {
				LOGGER
						.debug(loggingPrefix
								+ " : Executed getPluginProperties(String, String) API successfully. Returning batch plugin with properties size, "
								+ batchPlugin.getPropertiesSize());
			} else {
				LOGGER.debug(loggingPrefix
						+ " : Executed getPluginProperties(String, String) API successfully. Returning null batch plugin.");
			}

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
	public BatchPluginConfiguration[] getPluginProperties(String batchIdentifier, String pluginName, PluginProperty pluginProperty) {
		String loggingPrefix = null;
		if (IS_DEBUG_ENABLE) {
			loggingPrefix = BatchConstants.LOG_AREA + batchIdentifier;
			LOGGER.debug(loggingPrefix + " : Executing getPluginProperties(String, String, PluginProperty) API.");
			StringBuilder logMsg = new StringBuilder(loggingPrefix);
			logMsg.append(BatchConstants.PLUGIN_NAME);
			logMsg.append(pluginName);
			logMsg.append(" and pluginProperty key is ");
			logMsg.append(pluginProperty.getPropertyKey());
			LOGGER.debug(logMsg.toString());
		}
		List<BatchPluginConfiguration> configurations = pluginPropertiesDao.getPluginProperties(batchIdentifier).getPlugin(pluginName)
				.getPluginConfigurations(pluginProperty);
		if (configurations == null) {
			if (IS_DEBUG_ENABLE) {
				LOGGER.debug(loggingPrefix + " : plugin cofiguration not found for this plugin. Creating new configurations.");
			}
			configurations = new ArrayList<BatchPluginConfiguration>(BatchConstants.ZERO);
		}
		BatchPluginConfiguration[] batchPluginConfiguration = configurations
				.toArray(new BatchPluginPropertyContainer.BatchPluginConfiguration[configurations.size()]);
		if (IS_DEBUG_ENABLE) {

			if (batchPluginConfiguration != null && batchPluginConfiguration[0] != null) {
				LOGGER
						.debug(loggingPrefix
								+ " : Executed getPluginProperties(String, String, PluginProperty) API successfully. Returning Batch Plugin Configuration value, "
								+ batchPluginConfiguration[0].getValue());
			} else {
				LOGGER
						.debug(loggingPrefix
								+ " : Executed getPluginProperties(String) API successfully. Returning batch plugin property container. Either container is null or container has no configuration.");
			}
		}
		return batchPluginConfiguration;
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
	public BatchDynamicPluginConfiguration[] getDynamicPluginProperties(String batchIdentifier, String pluginName,
			PluginProperty pluginProperty) {
		String loggingPrefix = null;
		if (IS_DEBUG_ENABLE) {
			loggingPrefix = BatchConstants.LOG_AREA + batchIdentifier;
			LOGGER.debug(loggingPrefix + " : Executing getDynamicPluginProperties API.");
			StringBuilder logMsg = new StringBuilder(loggingPrefix);
			logMsg.append(BatchConstants.PLUGIN_NAME);
			logMsg.append(pluginName);
			logMsg.append(" and pluginProperty key is ");
			logMsg.append(pluginProperty.getPropertyKey());
			LOGGER.debug(logMsg.toString());
		}
		List<BatchDynamicPluginConfiguration> configurations = pluginPropertiesDao.getPluginProperties(batchIdentifier).getPlugin(
				pluginName).getDynamicPluginConfigurations(pluginProperty);
		if (configurations == null) {
			if (IS_DEBUG_ENABLE) {
				LOGGER.debug(loggingPrefix + " : plugin cofiguration not found for this plugin. Creating new configurations.");
			}
			configurations = new ArrayList<BatchDynamicPluginConfiguration>(BatchConstants.ZERO);
		}
		BatchDynamicPluginConfiguration[] batchDynamicPluginConfiguration = configurations
				.toArray(new BatchPluginPropertyContainer.BatchDynamicPluginConfiguration[configurations.size()]);
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix + " : Executed getDynamicPluginProperties API successfully.");
		}
		if (IS_DEBUG_ENABLE) {

			if (batchDynamicPluginConfiguration != null && batchDynamicPluginConfiguration[0] != null) {
				LOGGER
						.debug(loggingPrefix
								+ " : Executed getDynamicPluginProperties API successfully. Returning dynamic batch plugin configuration value, "
								+ batchDynamicPluginConfiguration[0].getValue());
			} else {
				LOGGER
						.debug(loggingPrefix
								+ " : Executed getDynamicPluginProperties API successfully. Returning dynamic batch plugin property container. Either container is null or container has no configuration.");
			}
		}
		return batchDynamicPluginConfiguration;
	}

	/**
	 * API to get Document Type for a batch.
	 * 
	 * @param batchIdentifier String
	 * @return List<com.ephesoft.dcma.da.domain.DocumentType>
	 */
	@Override
	public List<com.ephesoft.dcma.da.domain.DocumentType> getDocumentTypes(String batchIdentifier) {
		String loggingPrefix = null;
		if (IS_DEBUG_ENABLE) {
			loggingPrefix = BatchConstants.LOG_AREA + batchIdentifier;
			LOGGER.debug(loggingPrefix + " : Executing getDocumentTypes API.");
		}
		BatchPluginPropertyContainer container = getPluginProperties(batchIdentifier);
		List<com.ephesoft.dcma.da.domain.DocumentType> returnList = null;
		if (container != null) {
			Map<String, DocumentType> allDocTypes = container.getAllDocumentTypes();
			if (allDocTypes != null && !allDocTypes.isEmpty()) {
				Set<String> docTypesNames = allDocTypes.keySet();
				returnList = new ArrayList<com.ephesoft.dcma.da.domain.DocumentType>();
				for (String string : docTypesNames) {
					DocumentType serializedDocType = allDocTypes.get(string);
					com.ephesoft.dcma.da.domain.DocumentType tempDocType = new com.ephesoft.dcma.da.domain.DocumentType();
					tempDocType.setId(serializedDocType.getId());
					tempDocType.setIdentifier(serializedDocType.getIdentifier());
					tempDocType.setName(serializedDocType.getName());
					tempDocType.setDescription(serializedDocType.getDescription());
					tempDocType.setRspProjectFileName(serializedDocType.getRspProjectFileName());
					tempDocType.setBatchClass(serializedDocType.getBatchClass());
					tempDocType.setCreationDate(serializedDocType.getCreationDate());
					tempDocType.setLastModified(serializedDocType.getLastModified());
					tempDocType.setMinConfidenceThreshold(serializedDocType.getMinConfidenceThreshold());
					tempDocType.setPriority(Integer.valueOf(serializedDocType.getPriority()));
					tempDocType.setHidden(serializedDocType.isHidden());
					returnList.add(tempDocType);
					if (IS_DEBUG_ENABLE) {
						LOGGER.debug(loggingPrefix + " : Document type added to list successfully. DocType name is " + string);
					}
				}
			}
		}
		if (IS_DEBUG_ENABLE) {

			if (returnList != null) {
				LOGGER.debug(loggingPrefix
						+ " : Executed getDocumentTypes(String) API successfully. Returning list of document types with size, "
						+ returnList.size());
			} else {
				LOGGER.debug(loggingPrefix
						+ " : Executed getDocumentTypes(String) API successfully. Returning null as no document type is found.");
			}

		}
		return returnList;
	}

	/**
	 * API To get Document Type By Name.
	 * @param batchIdentifier String
	 * @param docTypeName String
	 * @return List<com.ephesoft.dcma.da.domain.DocumentType>
	 */
	@Override
	public List<com.ephesoft.dcma.da.domain.DocumentType> getDocumentTypeByName(String batchIdentifier, String docTypeName) {
		String loggingPrefix = null;
		if (IS_DEBUG_ENABLE) {
			loggingPrefix = BatchConstants.LOG_AREA + batchIdentifier;
			LOGGER.debug(loggingPrefix + " : Executing getDocumentTypeByName API. Document type name is " + docTypeName);
		}
		BatchPluginPropertyContainer container = getPluginProperties(batchIdentifier);
		List<com.ephesoft.dcma.da.domain.DocumentType> returnList = null;
		if (container != null) {
			if (IS_DEBUG_ENABLE) {
				LOGGER.debug(loggingPrefix + BatchConstants.PLUGIN_PROPERTIES_RETREIVED);
			}
			Map<String, DocumentType> allDocTypes = container.getAllDocumentTypes();
			if (allDocTypes != null && !allDocTypes.isEmpty()) {
				if (IS_DEBUG_ENABLE) {
					LOGGER.debug(loggingPrefix + " : all document types retreived successfully.");
				}
				returnList = new ArrayList<com.ephesoft.dcma.da.domain.DocumentType>();
				DocumentType serializedDocType = allDocTypes.get(docTypeName);
				com.ephesoft.dcma.da.domain.DocumentType tempDocType = new com.ephesoft.dcma.da.domain.DocumentType();
				tempDocType.setId(serializedDocType.getId());
				tempDocType.setIdentifier(serializedDocType.getIdentifier());
				tempDocType.setName(serializedDocType.getName());
				tempDocType.setDescription(serializedDocType.getDescription());
				tempDocType.setRspProjectFileName(serializedDocType.getRspProjectFileName());
				tempDocType.setBatchClass(serializedDocType.getBatchClass());
				tempDocType.setCreationDate(serializedDocType.getCreationDate());
				tempDocType.setLastModified(serializedDocType.getLastModified());
				tempDocType.setMinConfidenceThreshold(serializedDocType.getMinConfidenceThreshold());
				tempDocType.setPriority(Integer.valueOf(serializedDocType.getPriority()));
				tempDocType.setHidden(serializedDocType.isHidden());
				returnList.add(tempDocType);
				if (IS_DEBUG_ENABLE) {
					LOGGER.debug(loggingPrefix + " : Document type added to list successfully.");
				}
			}
		}
		if (IS_DEBUG_ENABLE) {
			if (returnList != null) {
				LOGGER.debug(loggingPrefix
						+ " : Executed getDocumentTypeByName API successfully. Returning list of document types with size, "
						+ returnList.size());
			} else {
				LOGGER.debug(loggingPrefix
						+ " : Executed getDocumentTypeByName API successfully. Returning null as no configuration is found.");
			}
		}
		return returnList;
	}

	/**
	 * API To get Field Type.
	 * @param batchIdentifier String
	 * @param docTypeName String
	 * @return List<com.ephesoft.dcma.da.domain.FieldType>
	 */
	@Override
	public List<com.ephesoft.dcma.da.domain.FieldType> getFieldTypes(String batchIdentifier, String docTypeName) {
		String loggingPrefix = null;
		if (IS_DEBUG_ENABLE) {
			loggingPrefix = BatchConstants.LOG_AREA + batchIdentifier;
			LOGGER.debug(loggingPrefix + " : Executing getFieldTypes API. Document type name is " + docTypeName);
		}
		BatchPluginPropertyContainer container = getPluginProperties(batchIdentifier);
		List<com.ephesoft.dcma.da.domain.FieldType> returnList = null;
		if (container != null) {
			if (IS_DEBUG_ENABLE) {
				LOGGER.debug(loggingPrefix + BatchConstants.PLUGIN_PROPERTIES_RETREIVED);
			}
			Map<String, DocumentType> allDocTypes = container.getAllDocumentTypes();
			if (allDocTypes != null && !allDocTypes.isEmpty()) {
				if (IS_DEBUG_ENABLE) {
					LOGGER.debug(loggingPrefix + " : all document types retreived successfully.");
				}
				returnList = new ArrayList<com.ephesoft.dcma.da.domain.FieldType>();
				DocumentType serializedDocType = allDocTypes.get(docTypeName);
				serializedDocType(returnList, serializedDocType, loggingPrefix);
				if (IS_DEBUG_ENABLE) {
					LOGGER.debug(loggingPrefix + " : list of field type is populated.");
				}
			}
		}
		if (IS_DEBUG_ENABLE) {
			if (returnList != null) {
				LOGGER.debug(loggingPrefix + " : Executed getFieldTypes API successfully. Returning list of field types with size, "
						+ returnList.size());
			} else {
				LOGGER.debug(loggingPrefix
						+ " : Executed getFieldTypes API successfully. Returning null as no configuration is found.");
			}
		}
		return returnList;
	}

	private void serializedDocType(List<com.ephesoft.dcma.da.domain.FieldType> returnList, DocumentType serializedDocType,
			final String loggingPrefix) {
		if (IS_DEBUG_ENABLE) {
			StringBuilder logMsg = new StringBuilder(loggingPrefix);
			logMsg.append(" : Executing serializedDocType API. Return list size is ");
			logMsg.append(returnList.size());
			logMsg.append(" and serializedDocType is ");
			String name = null;
			if (serializedDocType != null) {
				name = serializedDocType.getName();
			}
			logMsg.append(name);
			LOGGER.debug(logMsg.toString());

		}
		if (serializedDocType != null) {
			Map<String, com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.FieldType> allFieldTypes = serializedDocType
					.getDocFieldTypes();
			if (allFieldTypes != null && !allFieldTypes.isEmpty()) {
				Set<String> tempFieldTypes = allFieldTypes.keySet();
				LOGGER.debug(loggingPrefix + " : All document field types populated successfully.");
				serializedAllFieldType(returnList, allFieldTypes, tempFieldTypes, loggingPrefix);
			}
		}
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix + " : Executed serializedDocType API successfully.");
		}
	}

	private void serializedAllFieldType(List<com.ephesoft.dcma.da.domain.FieldType> returnList,
			Map<String, com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.FieldType> allFieldTypes,
			Set<String> tempFieldTypes, final String loggingPrefix) {
		if (IS_DEBUG_ENABLE) {
			StringBuilder logMsg = new StringBuilder(loggingPrefix);
			logMsg.append(" : Executing serializedAllFieldType API. Return list size is ");
			logMsg.append(returnList.size());
			logMsg.append(" size of allFieldTypes is ");
			logMsg.append(allFieldTypes.size());
			logMsg.append(" size of tempFieldTypes is ");
			int size = 0;
			if (tempFieldTypes != null) {
				size = tempFieldTypes.size();
			}
			logMsg.append(size);
			LOGGER.debug(logMsg.toString());
		}
		if (tempFieldTypes != null && !tempFieldTypes.isEmpty()) {
			for (String string : tempFieldTypes) {
				com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.FieldType eachFieldType = allFieldTypes.get(string);
				if (eachFieldType != null) {
					FieldType localFieldType = new FieldType();
					localFieldType.setId(eachFieldType.getId());
					localFieldType.setIdentifier(eachFieldType.getIdentifier());
					localFieldType.setCreationDate(eachFieldType.getCreationDate());
					localFieldType.setDataType(DataType.valueOf(eachFieldType.getDataType()));
					localFieldType.setDescription(eachFieldType.getDescription());
					localFieldType.setDocType(eachFieldType.getDocType());
					localFieldType.setLastModified(eachFieldType.getLastModified());
					localFieldType.setName(eachFieldType.getName());
					localFieldType.setPattern(eachFieldType.getPattern());
					localFieldType.setFieldOrderNumber(eachFieldType.getFieldOrderNumber());
					localFieldType.setBarcodeType(eachFieldType.getBarcodeType());
					localFieldType.setFieldOptionValueList(eachFieldType.getFieldOptionValueList());
					localFieldType.setHidden(eachFieldType.isHidden());
					localFieldType.setMultiLine(eachFieldType.isMultiLine());
					localFieldType.setReadOnly(eachFieldType.getIsReadOnly());
					localFieldType.setSampleValue(eachFieldType.getSampleValue());
					List<RegexValidation> finalExtractionList = new ArrayList<RegexValidation>();
					Map<String, com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.RegexValidation> regexMap = eachFieldType
							.getRegexValidation();
					setRegexInfo(finalExtractionList, regexMap, loggingPrefix);
					localFieldType.setRegexValidation(finalExtractionList);
					returnList.add(localFieldType);
				}
			}
		}
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix + " : Executed serializedAllFieldType API successfully.");
		}
	}

	private void setRegexInfo(List<RegexValidation> finalExtractionList,
			Map<String, com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.RegexValidation> regexMap,
			final String loggingPrefix) {
		if (IS_DEBUG_ENABLE) {
			StringBuilder logMsg = new StringBuilder(loggingPrefix);
			logMsg.append(" : Executing setRegexInfo API. finalExtractionList size is ");
			logMsg.append(finalExtractionList.size());
			logMsg.append(" size of regexMap is ");
			int size = 0;
			if (regexMap != null) {
				size = regexMap.size();
			}
			logMsg.append(size);
			LOGGER.debug(logMsg.toString());
		}
		if (regexMap != null && !regexMap.isEmpty()) {
			Set<String> regexSet = regexMap.keySet();
			if (regexSet != null && !regexSet.isEmpty()) {
				for (String string2 : regexSet) {
					com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.RegexValidation localRegexVal = regexMap
							.get(string2);
					setRegexInformation(finalExtractionList, localRegexVal, loggingPrefix);
				}
			}
		}
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix + " : Executed setRegexInfo API successfully.");
		}
	}

	private void setRegexInformation(List<RegexValidation> finalExtractionList,
			com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.RegexValidation localRegexVal, final String loggingPrefix) {
		if (IS_DEBUG_ENABLE) {
			StringBuilder logMsg = new StringBuilder(loggingPrefix);
			logMsg.append(" : Executing setRegexInformation API. finalExtractionList size is ");
			logMsg.append(finalExtractionList.size());
			logMsg.append(" localRegexVal pattern is ");
			String pattern = null;
			if (localRegexVal != null) {
				pattern = localRegexVal.getPattern();
			}
			logMsg.append(pattern);
			LOGGER.debug(logMsg.toString());
		}
		if (localRegexVal != null) {
			RegexValidation regexValidation = new RegexValidation();
			regexValidation.setId(localRegexVal.getId());
			regexValidation.setCreationDate(localRegexVal.getCreationDate());
			regexValidation.setPattern(localRegexVal.getPattern());
			regexValidation.setLastModified(localRegexVal.getLastModified());
			regexValidation.setFieldType(localRegexVal.getFieldType());
			finalExtractionList.add(regexValidation);
		}
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix + " : Executed setRegexInformation API successfully.");
		}
	}

	/**
	 * To get Field Type and KV Extractions.
	 * @param batchIdentifier String
	 * @param docTypeName String
	 * @return List<com.ephesoft.dcma.da.domain.FieldType>
	 */
	@Override
	public List<com.ephesoft.dcma.da.domain.FieldType> getFieldTypeAndKVExtractions(String batchIdentifier, String docTypeName) {
		String loggingPrefix = null;
		if (IS_DEBUG_ENABLE) {
			loggingPrefix = BatchConstants.LOG_AREA + batchIdentifier;
			LOGGER.debug(loggingPrefix + " : Executing getFieldTypeAndKVExtractions API. Document type name is " + docTypeName);
		}
		BatchPluginPropertyContainer container = getPluginProperties(batchIdentifier);
		List<com.ephesoft.dcma.da.domain.FieldType> returnList = null;
		if (container != null) {
			if (IS_DEBUG_ENABLE) {
				LOGGER.debug(loggingPrefix + BatchConstants.PLUGIN_PROPERTIES_RETREIVED);
			}
			Map<String, DocumentType> allDocTypes = container.getAllDocumentTypes();
			if (allDocTypes != null && !allDocTypes.isEmpty()) {
				returnList = serializedDocType(docTypeName, allDocTypes, loggingPrefix);
			}
		}
		if (IS_DEBUG_ENABLE) {
			if (returnList != null) {
				LOGGER.debug(loggingPrefix
						+ " : Executed getFieldTypeAndKVExtractions API successfully. Returning list of field types with size, "
						+ returnList.size());
			} else {
				LOGGER.debug(loggingPrefix
						+ " : Executed getFieldTypeAndKVExtractions API successfully. Returning null as no configuration is found.");
			}
		}
		return returnList;
	}

	private List<com.ephesoft.dcma.da.domain.FieldType> serializedDocType(String docTypeName, Map<String, DocumentType> allDocTypes,
			final String loggingPrefix) {
		if (IS_DEBUG_ENABLE) {
			StringBuilder logMsg = new StringBuilder(loggingPrefix);
			logMsg.append(" : Executing serializedDocType API. docTypeName is ");
			logMsg.append(docTypeName);
			logMsg.append(" allDocTypes size is ");
			logMsg.append(allDocTypes.size());
			LOGGER.debug(logMsg.toString());
		}
		List<com.ephesoft.dcma.da.domain.FieldType> returnList;
		returnList = new ArrayList<com.ephesoft.dcma.da.domain.FieldType>();
		DocumentType serializedDocType = allDocTypes.get(docTypeName);
		Map<String, com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.FieldType> allFieldTypes = serializedDocType
				.getDocFieldTypes();
		if (allFieldTypes != null && !allFieldTypes.isEmpty()) {
			Set<String> tempFieldTypes = allFieldTypes.keySet();
			if (tempFieldTypes != null && !tempFieldTypes.isEmpty()) {
				serializedFieldType(returnList, allFieldTypes, tempFieldTypes, loggingPrefix);
			}
		}
		if (IS_DEBUG_ENABLE) {
			if (returnList != null) {
				LOGGER.debug(loggingPrefix
						+ " : Executed serializedDocType API successfully. Returning list of field types with size, "
						+ returnList.size());
			} else {
				LOGGER.debug(loggingPrefix + " : Executed serializedDocType API successfully. allFieldTypes list is empty or null.");
			}
		}
		return returnList;
	}

	private void serializedFieldType(List<com.ephesoft.dcma.da.domain.FieldType> returnList,
			Map<String, com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.FieldType> allFieldTypes,
			Set<String> tempFieldTypes, final String loggingPrefix) {
		if (IS_DEBUG_ENABLE) {
			StringBuilder logMsg = new StringBuilder(loggingPrefix);
			logMsg.append(" : Executing serializedFieldType API. returnList size is ");
			logMsg.append(returnList.size());
			logMsg.append(", allFieldTypes size is ");
			logMsg.append(allFieldTypes.size());
			logMsg.append(" and tempFieldTypes size is ");
			logMsg.append(tempFieldTypes.size());
			LOGGER.debug(logMsg.toString());
		}
		for (String string : tempFieldTypes) {
			com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.FieldType eachFieldType = allFieldTypes.get(string);
			if (eachFieldType != null) {
				FieldType localFieldType = new FieldType();
				localFieldType.setId(eachFieldType.getId());
				localFieldType.setIdentifier(eachFieldType.getIdentifier());
				localFieldType.setCreationDate(eachFieldType.getCreationDate());
				localFieldType.setDataType(DataType.valueOf(eachFieldType.getDataType()));
				localFieldType.setDescription(eachFieldType.getDescription());
				localFieldType.setDocType(eachFieldType.getDocType());
				localFieldType.setLastModified(eachFieldType.getLastModified());
				localFieldType.setName(eachFieldType.getName());
				localFieldType.setPattern(eachFieldType.getPattern());
				localFieldType.setMultiLine(eachFieldType.isMultiLine());
				localFieldType.setReadOnly(eachFieldType.getIsReadOnly());
				// populating KV extraction
				List<KVExtraction> finalExtractionList = new ArrayList<KVExtraction>();
				Map<String, com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.KVExtraction> kvExtractionMap = eachFieldType
						.getFieldKVExtraction();
				setKVExtractionInformation(finalExtractionList, kvExtractionMap, loggingPrefix);
				localFieldType.setKvExtraction(finalExtractionList);
				returnList.add(localFieldType);
			}
		}
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix + " : Executed serializedFieldType API successfully.");
		}
	}

	private void setKVExtractionInformation(List<KVExtraction> finalExtractionList,
			Map<String, com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.KVExtraction> kvExtractionMap,
			String loggingPrefix) {
		if (IS_DEBUG_ENABLE) {
			StringBuilder logMsg = new StringBuilder(loggingPrefix);
			logMsg.append(" : Executing setKVExtractionInformation API. finalExtractionList size is ");
			logMsg.append(finalExtractionList.size());
			logMsg.append(" and kvExtractionMap size is ");
			logMsg.append(kvExtractionMap.size());
			LOGGER.debug(logMsg.toString());
		}
		if (kvExtractionMap != null && !kvExtractionMap.isEmpty()) {
			Set<String> kvSet = kvExtractionMap.keySet();
			if (kvSet != null && !kvSet.isEmpty()) {
				serializedKV(finalExtractionList, kvExtractionMap, kvSet, loggingPrefix);
			}
		}
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix + " : Executed setKVExtractionInformation API successfully.");
		}
	}

	private void serializedKV(List<KVExtraction> finalExtractionList,
			Map<String, com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.KVExtraction> kvExtractionMap,
			Set<String> kvSet, final String loggingPrefix) {
		if (IS_DEBUG_ENABLE) {
			StringBuilder logMsg = new StringBuilder(loggingPrefix);
			logMsg.append(" : Executing serializedKV API. finalExtractionList size is ");
			logMsg.append(finalExtractionList.size());
			logMsg.append(", kvExtractionMap size is ");
			logMsg.append(kvExtractionMap.size());
			logMsg.append(" and kvSet size is ");
			logMsg.append(kvSet.size());
			LOGGER.debug(logMsg.toString());
		}
		for (String string2 : kvSet) {
			com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.KVExtraction localKvExtrac = kvExtractionMap.get(string2);
			if (localKvExtrac != null) {
				KVExtraction kvExtraction = new KVExtraction();
				kvExtraction.setId(localKvExtrac.getId());
				kvExtraction.setCreationDate(localKvExtrac.getCreationDate());
				kvExtraction.setKeyPattern(localKvExtrac.getKeyPattern());
				kvExtraction.setLastModified(localKvExtrac.getLastModified());
				kvExtraction.setLocationType(LocationType.valueOf(localKvExtrac.getLocation()));
				kvExtraction.setValuePattern(localKvExtrac.getValuePattern());
				kvExtraction.setFieldType(localKvExtrac.getFieldType());
				finalExtractionList.add(kvExtraction);
			}
		}
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix + " : Executed serializedKV API successfully.");
		}
	}

	/**
	 * To get Page Types.
	 * @param batchIdentifier String
	 * @return List<com.ephesoft.dcma.da.domain.PageType>
	 */
	@Override
	public List<com.ephesoft.dcma.da.domain.PageType> getPageTypes(String batchIdentifier) {
		String loggingPrefix = null;
		if (IS_DEBUG_ENABLE) {
			loggingPrefix = BatchConstants.LOG_AREA + batchIdentifier;
			LOGGER.debug(loggingPrefix + " : Executing getPageTypes API.");
		}
		BatchPluginPropertyContainer container = getPluginProperties(batchIdentifier);
		List<com.ephesoft.dcma.da.domain.PageType> returnList = null;
		if (container != null) {
			if (IS_DEBUG_ENABLE) {
				LOGGER.debug(loggingPrefix + BatchConstants.PLUGIN_PROPERTIES_RETREIVED);
			}
			Map<String, DocumentType> allDocTypes = container.getAllDocumentTypes();
			if (allDocTypes != null && !allDocTypes.isEmpty()) {
				returnList = new ArrayList<com.ephesoft.dcma.da.domain.PageType>();
				Set<String> allkeysSet = allDocTypes.keySet();
				getPageInformation(returnList, allDocTypes, allkeysSet, loggingPrefix);
			}
		}
		if (IS_DEBUG_ENABLE) {
			if (returnList != null) {
				LOGGER.debug(loggingPrefix + " : Executed getPageTypes API successfully. Returning list of PageType with size, "
						+ returnList.size());
			} else {
				LOGGER.debug(loggingPrefix + " : Executed getPageTypes API successfully. PageType list is empty or null.");
			}
		}
		return returnList;
	}

	private void getPageInformation(List<com.ephesoft.dcma.da.domain.PageType> returnList, Map<String, DocumentType> allDocTypes,
			Set<String> allkeysSet, final String loggingPrefix) {
		if (IS_DEBUG_ENABLE) {
			StringBuilder logMsg = new StringBuilder(loggingPrefix);
			logMsg.append(" : Executing getPageInformation API. returnList size is ");
			logMsg.append(returnList.size());
			logMsg.append(", allDocTypes size is ");
			logMsg.append(allDocTypes.size());
			logMsg.append(" and allkeysSet size is ");
			int size = 0;
			if (allkeysSet != null) {
				size = allkeysSet.size();
			}
			logMsg.append(size);
			LOGGER.debug(logMsg.toString());
		}
		if (allkeysSet != null && !allkeysSet.isEmpty()) {
			for (String eachKey : allkeysSet) {
				DocumentType docType = allDocTypes.get(eachKey);
				if (docType != null) {
					Map<String, PageType> allPageTypes = docType.getDocPageTypes();
					addPageInformation(returnList, allPageTypes, loggingPrefix);
				}
			}
		}
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix + " : Executed getPageInformation API successfully.");
		}
	}

	private void addPageInformation(List<com.ephesoft.dcma.da.domain.PageType> returnList, Map<String, PageType> allPageTypes,
			final String loggingPrefix) {
		if (IS_DEBUG_ENABLE) {
			StringBuilder logMsg = new StringBuilder(loggingPrefix);
			logMsg.append(" : Executing addPageInformation API. returnList size is ");
			logMsg.append(returnList.size());
			logMsg.append(" and allPageTypes size is ");
			int size = 0;
			if (allPageTypes != null) {
				size = allPageTypes.size();
			}
			logMsg.append(size);
			LOGGER.debug(logMsg.toString());
		}
		if (allPageTypes != null && !allPageTypes.isEmpty()) {
			Set<String> allTempKeySet = allPageTypes.keySet();
			if (allTempKeySet != null) {
				for (String eachTempKey : allTempKeySet) {
					PageType eachPageType = allPageTypes.get(eachTempKey);
					setPageTypeInfo(returnList, eachPageType, loggingPrefix);
				}
			}
		}
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix + " : Executed addPageInformation API successfully.");
		}
	}

	private void setPageTypeInfo(List<com.ephesoft.dcma.da.domain.PageType> returnList, PageType eachPageType,
			final String loggingPrefix) {
		if (IS_DEBUG_ENABLE) {
			StringBuilder logMsg = new StringBuilder(loggingPrefix);
			logMsg.append(" : Executing setPageTypeInfo API. returnList size is ");
			logMsg.append(returnList.size());
			logMsg.append(" and eachPageType name is ");
			String name = null;
			if (eachPageType != null) {
				name = eachPageType.getName();
			}
			logMsg.append(name);
			LOGGER.debug(logMsg.toString());
		}
		if (eachPageType != null) {
			com.ephesoft.dcma.da.domain.PageType localPageType = new com.ephesoft.dcma.da.domain.PageType();
			localPageType.setId(eachPageType.getId());
			localPageType.setIdentifier(localPageType.getIdentifier());
			localPageType.setCreationDate(eachPageType.getCreationDate());
			localPageType.setDescription(eachPageType.getDescription());
			localPageType.setDocType(eachPageType.getDocType());
			localPageType.setLastModified(eachPageType.getLastModified());
			localPageType.setName(eachPageType.getName());
			returnList.add(localPageType);
		}
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix + " : Executed setPageTypeInfo API successfully.");
		}
	}

	/**
	 * To get Doc Type By Page Type Name.
	 * @param batchIdentifier String
	 * @param pageTypeName String
	 * @return List<com.ephesoft.dcma.da.domain.DocumentType>
	 */
	@Override
	public List<com.ephesoft.dcma.da.domain.DocumentType> getDocTypeByPageTypeName(String batchIdentifier, String pageTypeName) {
		String loggingPrefix = null;
		if (IS_DEBUG_ENABLE) {
			loggingPrefix = BatchConstants.LOG_AREA + batchIdentifier;
			LOGGER.debug(loggingPrefix + " : Executing getDocTypeByPageTypeName API. pageTypeName is " + pageTypeName);
		}
		BatchPluginPropertyContainer container = getPluginProperties(batchIdentifier);
		List<com.ephesoft.dcma.da.domain.DocumentType> returnList = null;
		if (container != null) {
			if (IS_DEBUG_ENABLE) {
				LOGGER.debug(loggingPrefix + BatchConstants.PLUGIN_PROPERTIES_RETREIVED);
			}
			Map<String, DocumentType> allDocTypes = container.getAllDocumentTypes();
			if (allDocTypes != null && !allDocTypes.isEmpty()) {
				returnList = new ArrayList<com.ephesoft.dcma.da.domain.DocumentType>();
				Set<String> allkeysSet = allDocTypes.keySet();
				getDocumentInformation(pageTypeName, returnList, allDocTypes, allkeysSet, loggingPrefix);
			}
		}
		if (IS_DEBUG_ENABLE) {
			if (returnList != null) {
				LOGGER.debug(loggingPrefix
						+ " : Executed getDocTypeByPageTypeName API successfully. Returning list of DocumentType with size, "
						+ returnList.size());
			} else {
				LOGGER.debug(loggingPrefix + " : Executed getDocTypeByPageTypeName API successfully. DocumentType is empty or null.");
			}
		}
		return returnList;
	}

	private void getDocumentInformation(String pageTypeName, List<com.ephesoft.dcma.da.domain.DocumentType> returnList,
			Map<String, DocumentType> allDocTypes, Set<String> allkeysSet, final String loggingPrefix) {
		if (IS_DEBUG_ENABLE) {
			StringBuilder logMsg = new StringBuilder(loggingPrefix);
			logMsg.append(" : Executing getDocumentInformation API. pageTypeName is ");
			logMsg.append(pageTypeName);
			logMsg.append(", returnList size is ");
			logMsg.append(returnList.size());
			logMsg.append(", allDocTypes size is ");
			logMsg.append(allDocTypes.size());
			logMsg.append(" and allkeysSet size is ");
			int size = 0;
			if (allkeysSet != null) {
				size = allkeysSet.size();
			}
			logMsg.append(size);
			LOGGER.debug(logMsg.toString());
		}
		if (allkeysSet != null && !allkeysSet.isEmpty()) {
			for (String eachKey : allkeysSet) {
				DocumentType docType = allDocTypes.get(eachKey);
				if (docType != null) {
					Map<String, PageType> allPageTypes = docType.getDocPageTypes();
					addDocTypeInformation(pageTypeName, returnList, docType, allPageTypes, loggingPrefix);
				}
			}
		}
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix + " : Executed getDocumentInformation API successfully.");
		}
	}

	private void addDocTypeInformation(String pageTypeName, List<com.ephesoft.dcma.da.domain.DocumentType> returnList,
			DocumentType docType, Map<String, PageType> allPageTypes, String loggingPrefix) {
		if (IS_DEBUG_ENABLE) {
			StringBuilder logMsg = new StringBuilder(loggingPrefix);
			logMsg.append(" : Executing addDocTypeInformation API. pageTypeName is ");
			logMsg.append(pageTypeName);
			logMsg.append(", returnList size is ");
			logMsg.append(returnList.size());
			logMsg.append(", docType name is ");
			logMsg.append(docType.getName());
			logMsg.append(" and allPageTypes size is ");
			int size = 0;
			if (allPageTypes != null) {
				size = allPageTypes.size();
			}
			logMsg.append(size);
			LOGGER.debug(logMsg.toString());
		}
		if (allPageTypes != null && !allPageTypes.isEmpty()) {
			Set<String> allTempKeySet = allPageTypes.keySet();
			if (allTempKeySet != null && allTempKeySet.contains(pageTypeName)) {
				com.ephesoft.dcma.da.domain.DocumentType tempDocType = new com.ephesoft.dcma.da.domain.DocumentType();
				tempDocType.setId(docType.getId());
				tempDocType.setIdentifier(tempDocType.getIdentifier());
				tempDocType.setName(docType.getName());
				tempDocType.setDescription(docType.getDescription());
				tempDocType.setRspProjectFileName(docType.getRspProjectFileName());
				tempDocType.setHidden(docType.isHidden());
				tempDocType.setBatchClass(docType.getBatchClass());
				tempDocType.setCreationDate(docType.getCreationDate());
				tempDocType.setLastModified(docType.getLastModified());
				tempDocType.setMinConfidenceThreshold(docType.getMinConfidenceThreshold());
				tempDocType.setPriority(Integer.valueOf(docType.getPriority()));
				returnList.add(tempDocType);
			}
		}
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix + " : Executed addDocTypeInformation API successfully.");
		}
	}

	/**
	 * To get Function Keys.
	 * @param batchIdentifier String
	 * @param docTypeName String
	 * @return List<FunctionKey>
	 */
	@Override
	public List<FunctionKey> getFunctionKeys(String batchIdentifier, String docTypeName) {
		String loggingPrefix = null;
		if (IS_DEBUG_ENABLE) {
			loggingPrefix = BatchConstants.LOG_AREA + batchIdentifier;
			LOGGER.debug(loggingPrefix + " : Executing getFunctionKeys(String, String) API. docTypeName is " + docTypeName);
		}
		BatchPluginPropertyContainer container = getPluginProperties(batchIdentifier);
		List<com.ephesoft.dcma.da.domain.FunctionKey> returnList = null;
		if (container != null) {
			if (IS_DEBUG_ENABLE) {
				LOGGER.debug(loggingPrefix + BatchConstants.PLUGIN_PROPERTIES_RETREIVED);
			}
			Map<String, DocumentType> allDocTypes = container.getAllDocumentTypes();
			if (allDocTypes != null && !allDocTypes.isEmpty()) {
				returnList = new ArrayList<com.ephesoft.dcma.da.domain.FunctionKey>();
				DocumentType serializedDocType = allDocTypes.get(docTypeName);
				getFunctionKey(returnList, serializedDocType, loggingPrefix);
			}
		}
		if (IS_DEBUG_ENABLE) {
			if (returnList != null) {
				LOGGER.debug(loggingPrefix
						+ " : Executed getFunctionKeys(String, String) API successfully. Returning list of FunctionKey with size, "
						+ returnList.size());
			} else {
				LOGGER
						.debug(loggingPrefix
								+ " : Executed getFunctionKeys(String, String) API successfully. Returning list as null. plugin properties not found for this batch.");
			}
		}
		return returnList;
	}

	private void getFunctionKey(List<com.ephesoft.dcma.da.domain.FunctionKey> returnList, DocumentType serializedDocType,
			final String loggingPrefix) {
		if (IS_DEBUG_ENABLE) {
			StringBuilder logMsg = new StringBuilder(loggingPrefix);
			logMsg.append(" : Executing getFunctionKey(List<FunctionKey>, DocumentType, String) API. returnList size is ");
			logMsg.append(returnList.size());
			logMsg.append(" and serializedDocType name is ");
			String name = null;
			if (serializedDocType != null) {
				name = serializedDocType.getName();
			}
			logMsg.append(name);
			LOGGER.debug(logMsg.toString());
		}
		if (serializedDocType != null) {
			Map<String, com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.FunctionKey> allFunctionKeys = serializedDocType
					.getDocFunctionKeys();
			if (allFunctionKeys != null && !allFunctionKeys.isEmpty()) {
				Set<String> tempFunctionKeys = allFunctionKeys.keySet();
				addFunctionKeyInfo(returnList, allFunctionKeys, tempFunctionKeys, loggingPrefix);
			}
		}
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix + " : Executed getFunctionKey(List<FunctionKey>, DocumentType, String) API successfully.");
		}
	}

	private void addFunctionKeyInfo(List<com.ephesoft.dcma.da.domain.FunctionKey> returnList,
			Map<String, com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.FunctionKey> allFunctionKeys,
			Set<String> tempFunctionKeys, final String loggingPrefix) {
		if (IS_DEBUG_ENABLE) {
			StringBuilder logMsg = new StringBuilder(loggingPrefix);
			logMsg.append(" : Executing addFunctionKeyInfo API. returnList size is ");
			logMsg.append(returnList.size());
			logMsg.append(" allFunctionKeys size is ");
			logMsg.append(allFunctionKeys.size());
			logMsg.append(" and tempFunctionKeys size is ");
			logMsg.append(allFunctionKeys.size());
			int size = 0;
			if (tempFunctionKeys != null) {
				size = tempFunctionKeys.size();
			}
			logMsg.append(size);
			LOGGER.debug(logMsg.toString());
		}
		if (tempFunctionKeys != null && !tempFunctionKeys.isEmpty()) {
			for (String string : tempFunctionKeys) {
				com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.FunctionKey eachFunctionKey = allFunctionKeys
						.get(string);
				if (eachFunctionKey != null) {
					FunctionKey localFunctionKey = new FunctionKey();
					localFunctionKey.setId(eachFunctionKey.getId());
					localFunctionKey.setIdentifier(eachFunctionKey.getIdentifier());
					localFunctionKey.setDocType(eachFunctionKey.getDocType());
					localFunctionKey.setMethodName(eachFunctionKey.getMethodName());
					localFunctionKey.setShortcutKeyname(eachFunctionKey.getShortcutKeyname());
					localFunctionKey.setUiLabel(eachFunctionKey.getUiLabel());
					returnList.add(localFunctionKey);
				}
			}
		}
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix + " : Executed addFunctionKeyInfo API successfully.");
		}
	}

}
