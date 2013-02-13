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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.core.common.PluginProperty;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassDynamicPluginConfig;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;
import com.ephesoft.dcma.da.domain.KVPageProcess;

/**
 * This class is to read plugin properties.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.common.PluginProperty
 * @see com.ephesoft.dcma.da.domain.BatchClass
 */
public class BatchPluginPropertyContainer implements Serializable {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * batchIdentifier String.
	 */
	private final String batchIdentifier;

	/**
	 * plugins Map<String, BatchPlugin>.
	 */
	private final Map<String, BatchPlugin> plugins = new HashMap<String, BatchPlugin>();

	/**
	 * allBatchConfiguration Map<Long, BatchPluginConfiguration>.
	 */
	private final Map<Long, BatchPluginConfiguration> allBatchConfiguration = new HashMap<Long, BatchPluginConfiguration>();

	/**
	 * allDynamicBatchConfiguration Map<Long, BatchDynamicPluginConfiguration>.
	 */
	private final Map<Long, BatchDynamicPluginConfiguration> allDynamicBatchConfiguration = new HashMap<Long, BatchDynamicPluginConfiguration>();

	/**
	 * allDocumentTypes Map<String, DocumentType>.
	 */
	private Map<String, DocumentType> allDocumentTypes = new HashMap<String, DocumentType>();
	
	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchPluginPropertyContainer.class);
	
	/**
	 * boolean field to get if debug mode of logging is enabled.
	 */
	private static final boolean IS_DEBUG_ENABLE = LOGGER.isDebugEnabled();
	
	/**
	 * prefix of each log statement.
	 */
	private final String loggingPrefix;

	/**
	 * Constructor.
	 * 
	 * @param batchIdentifier String
	 */
	public BatchPluginPropertyContainer(final String batchIdentifier) {
		this.batchIdentifier = batchIdentifier;
		this.loggingPrefix = batchIdentifier + BatchConstants.LOG_AREA;
	}

	/**
	 * To get all the documents.
	 * 
	 * @return the allDocumentTypes
	 */
	public Map<String, DocumentType> getAllDocumentTypes() {
		return allDocumentTypes;
	}

	/**
	 * To set all the documents.
	 * 
	 * @param allDocumentTypes {@link Map<{@link String}, {@link DocumentType}>} the allDocumentTypes to set
	 */
	public void setAllDocumentTypes(final Map<String, DocumentType> allDocumentTypes) {
		this.allDocumentTypes = allDocumentTypes;
	}

	/**
	 * API to get batch plugins.
	 * 
	 * @param pluginName {@link String}
	 * @return BatchPlugin {@link BatchPlugin}
	 */
	public BatchPlugin getPlugin(final String pluginName) {
		if (plugins.get(pluginName) == null) {
			plugins.put(pluginName, new BatchPlugin(pluginName));
		}
		return plugins.get(pluginName);
	}

	/**
	 * API to add a plugin property.
	 * 
	 * @param pluginName {@link String}
	 * @param configuration {@link BatchPluginConfiguration}
	 */
	private void addPluginProperty(final String pluginName, final BatchPluginConfiguration configuration) {
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix
					+ "Executing addPluginProperty(String,BatchPluginConfiguration) API of BatchPluginPropertyContainer.");
		}
		final BatchPlugin plugin = getPlugin(pluginName);
		plugin.addProperty(new PluginProperty() {

			@Override
			public String getPropertyKey() {
				return configuration.getKey();
			}
		}, configuration);
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix + "Plugin property added successfully. Plugin name is : " + pluginName);
		}
	}

	/**
	 * API to add a plugin property according to dynamic plugin configuration.
	 * 
	 * @param pluginName {@link String}
	 * @param configuration {@link BatchDynamicPluginConfiguration}
	 */
	private void addPluginProperty(final String pluginName, final BatchDynamicPluginConfiguration configuration) {
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix
					+ "Executing addPluginProperty(String,BatchDynamicPluginConfiguration) API of BatchPluginPropertyContainer.");
		}
		final BatchPlugin plugin = getPlugin(pluginName);
		plugin.addProperty(new PluginProperty() {

			@Override
			public String getPropertyKey() {
				return configuration.getKey();
			}
		}, configuration);
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix + "Plugin property added successfully. Plugin name is : " + pluginName);
		}
	}

	/**
	 * API to get plugin configuration.
	 * 
	 * @param pluginName {@link String}
	 * @param pluginProperty {@link PluginProperty}
	 * @return List<{@link BatchPluginConfiguration}>
	 */
	public List<BatchPluginConfiguration> getPlginConfiguration(final String pluginName, final PluginProperty pluginProperty) {
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix + "Executing getPlginConfiguration(String,PluginProperty) API of BatchPluginPropertyContainer.");
			LOGGER.debug(loggingPrefix + "plugin name is : " + pluginName + "Plugin propert is : " + pluginProperty.getPropertyKey());
		}
		List<BatchPluginConfiguration> returnList = null;
		final BatchPlugin plugin = getPlugin(pluginName);
		if (plugin != null) {
			returnList = plugin.getPluginConfigurations(pluginProperty);
		}
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix
					+ "Executed getPlginConfiguration(String,PluginProperty) API of BatchPluginPropertyContainer successfully.");
		}
		return returnList;
	}

	/**
	 * API to populate batch class plugin configurations.
	 * 
	 * @param batchClassPluginConfigs {@link List<{@link BatchClassPluginConfig}>}
	 */
	public void populate(final List<BatchClassPluginConfig> batchClassPluginConfigs) {
		for (final BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
			final String pluginName = batchClassPluginConfig.getBatchClassPlugin().getPlugin().getPluginName();
			BatchPluginConfiguration configuration = allBatchConfiguration.get(batchClassPluginConfig.getId());
			if (configuration == null) {
				configuration = new BatchPluginConfiguration(batchClassPluginConfig);
				addPluginProperty(pluginName, configuration);
			}
		}
	}

	/**
	 * API to populate dynamic batch class plugin configurations.
	 * 
	 * @param batchClassDynamicPluginConfigs {@link List<{@link BatchClassDynamicPluginConfig}>}
	 */
	public void populateDynamicPluginConfigs(final List<BatchClassDynamicPluginConfig> batchClassDynamicPluginConfigs) {
		for (final BatchClassDynamicPluginConfig batchClassDynamicPluginConfig : batchClassDynamicPluginConfigs) {
			final String pluginName = batchClassDynamicPluginConfig.getBatchClassPlugin().getPlugin().getPluginName();
			BatchDynamicPluginConfiguration configuration = allDynamicBatchConfiguration.get(batchClassDynamicPluginConfig.getId());
			if (configuration == null) {
				configuration = new BatchDynamicPluginConfiguration(batchClassDynamicPluginConfig);
				addPluginProperty(pluginName, configuration);
			}
		}
	}

	/**
	 * API to populate document types.
	 * 
	 * @param documentTypes {@link List< {@link com.ephesoft.dcma.da.domain.DocumentType}>}
	 * @param batchInstanceIdentifierIdentifier {@link String}
	 */
	public void populateDocumentTypes(final List<com.ephesoft.dcma.da.domain.DocumentType> documentTypes,
			final String batchInstanceIdentifierIdentifier) {
		if (documentTypes != null && !documentTypes.isEmpty()) {
			for (final com.ephesoft.dcma.da.domain.DocumentType documentType : documentTypes) {
				final DocumentType localDocumentType = new DocumentType();
				localDocumentType.setDescription(documentType.getDescription());
				localDocumentType.setRspProjectFileName(documentType.getRspProjectFileName());
				localDocumentType.setMinConfidenceThreshold(documentType.getMinConfidenceThreshold());
				localDocumentType.setName(documentType.getName());
				localDocumentType.setPriority(String.valueOf(documentType.getPriority()));
				localDocumentType.setId(documentType.getId());
				localDocumentType.setIdentifier(documentType.getIdentifier());
				localDocumentType.setBatchClass(documentType.getBatchClass());
				localDocumentType.setCreationDate(documentType.getCreationDate());
				localDocumentType.setLastModified(documentType.getLastModified());
				localDocumentType.setHidden(documentType.isHidden());
				// populating page types
				final List<com.ephesoft.dcma.da.domain.PageType> pageTypesList = documentType.getPages();
				final Map<String, PageType> tempPageTypes = new HashMap<String, PageType>();
				for (final com.ephesoft.dcma.da.domain.PageType pageType : pageTypesList) {
					final PageType localPageType = new PageType();
					localPageType.setId(pageType.getId());
					localPageType.setIdentifier(pageType.getIdentifier());
					localPageType.setCreationDate(pageType.getCreationDate());
					localPageType.setLastModified(pageType.getLastModified());
					localPageType.setDocType(pageType.getDocType());
					localPageType.setDescription(pageType.getDescription());
					localPageType.setName(pageType.getName());
					tempPageTypes.put(pageType.getName(), localPageType);
				}
				localDocumentType.setDocPageTypes(tempPageTypes);
				final List<com.ephesoft.dcma.da.domain.FunctionKey> functionKeyList = documentType.getFunctionKeys();
				final Map<String, FunctionKey> tempFunctionKeys = new HashMap<String, FunctionKey>();
				for (final com.ephesoft.dcma.da.domain.FunctionKey functionKey : functionKeyList) {
					final FunctionKey localFunctionKey = new FunctionKey();
					localFunctionKey.setId(functionKey.getId());
					localFunctionKey.setIdentifier(functionKey.getIdentifier());
					localFunctionKey.setDocType(functionKey.getDocType());
					localFunctionKey.setMethodName(functionKey.getMethodName());
					localFunctionKey.setShortcutKeyname(functionKey.getShortcutKeyname());
					localFunctionKey.setUiLabel(functionKey.getUiLabel());
					tempFunctionKeys.put(functionKey.getIdentifier(), localFunctionKey);
				}
				localDocumentType.setDocFunctionKeys(tempFunctionKeys);
				// populating field types
				final List<com.ephesoft.dcma.da.domain.FieldType> allFieldTypes = documentType.getFieldTypes();
				final Map<String, FieldType> tempFieldTypes = new HashMap<String, FieldType>();
				if (allFieldTypes != null && !allFieldTypes.isEmpty()) {
					setAllFields(allFieldTypes, tempFieldTypes);
				}
				localDocumentType.setDocFieldTypes(tempFieldTypes);
				allDocumentTypes.put(documentType.getName(), localDocumentType);
			}
		}
	}

	/**
	 * API to set all the fields.
	 * 
	 * @param allFieldTypes {@link List< {@link com.ephesoft.dcma.da.domain.FieldType}>}
	 * @param tempFieldTypes {@link Map<{@link String}, {@link FieldType}>}
	 */
	private void setAllFields(final List<com.ephesoft.dcma.da.domain.FieldType> allFieldTypes,
			final Map<String, FieldType> tempFieldTypes) {
		for (final com.ephesoft.dcma.da.domain.FieldType tempField : allFieldTypes) {
			final FieldType fieldType = new FieldType();
			fieldType.setId(tempField.getId());
			fieldType.setIdentifier(tempField.getIdentifier());
			fieldType.setFieldOrderNumber(tempField.getFieldOrderNumber());
			fieldType.setCreationDate(tempField.getCreationDate());
			fieldType.setLastModified(tempField.getLastModified());
			fieldType.setDocType(tempField.getDocType());
			fieldType.setDataType(tempField.getDataType().name());
			fieldType.setDescription(tempField.getDescription());
			fieldType.setName(tempField.getName());
			fieldType.setPattern(tempField.getPattern());
			fieldType.setBarcodeType(tempField.getBarcodeType());
			fieldType.setSampleValue(tempField.getSampleValue());
			fieldType.setFieldOptionValueList(tempField.getFieldOptionValueList());
			fieldType.setHidden(tempField.isHidden());
			fieldType.setMultiLine(tempField.isMultiLine());
			fieldType.setReadOnly(tempField.getIsReadOnly());
			final Map<String, KVExtraction> tempKVExtraction = new HashMap<String, KVExtraction>();
			final List<com.ephesoft.dcma.da.domain.KVExtraction> tempList = tempField.getKvExtraction();
			if (tempList != null && !tempList.isEmpty()) {
				for (final com.ephesoft.dcma.da.domain.KVExtraction kvExtraction : tempList) {
					final KVExtraction var = new KVExtraction();
					var.setId(kvExtraction.getId());
					var.setCreationDate(kvExtraction.getCreationDate());
					var.setLastModified(kvExtraction.getLastModified());
					var.setFieldType(kvExtraction.getFieldType());
					var.setKeyPattern(kvExtraction.getKeyPattern());
					if (kvExtraction.getLocationType() != null) {
						var.setLocation(kvExtraction.getLocationType().name());
					}
					var.setValuePattern(kvExtraction.getValuePattern());
					tempKVExtraction.put(String.valueOf(kvExtraction.getId()), var);
				}
			}
			fieldType.setFieldKVExtraction(tempKVExtraction);
			tempFieldTypes.put(fieldType.getName(), fieldType);
			final Map<String, RegexValidation> tempRegexValidation = new HashMap<String, RegexValidation>();
			final List<com.ephesoft.dcma.da.domain.RegexValidation> tempList2 = tempField.getRegexValidation();
			if (tempList2 != null && !tempList2.isEmpty()) {
				for (final com.ephesoft.dcma.da.domain.RegexValidation regexValidation : tempList2) {
					final RegexValidation var = new RegexValidation();
					var.setId(regexValidation.getId());
					var.setCreationDate(regexValidation.getCreationDate());
					var.setLastModified(regexValidation.getLastModified());
					var.setFieldType(regexValidation.getFieldType());
					var.setPattern(regexValidation.getPattern());
					tempRegexValidation.put(String.valueOf(regexValidation.getId()), var);
				}
			}
			fieldType.setRegexValidation(tempRegexValidation);
			tempFieldTypes.put(fieldType.getName(), fieldType);
		}
	}

	/**
	 * This is batch plugin class.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 * 
	 */
	public class BatchPlugin implements Serializable {

		/**
		 * serialVersionUID long.
		 */
		private static final long serialVersionUID = 2L;

		/**
		 * pluginName String.
		 */
		private final String pluginName;

		/**
		 * pluginProperties Map<String, List<BatchPluginConfiguration>>.
		 */
		private final Map<String, List<BatchPluginConfiguration>> pluginProperties = new HashMap<String, List<BatchPluginConfiguration>>();

		/**
		 * dynamicPluginProperties Map<String, List<BatchDynamicPluginConfiguration>>.
		 */
		private final Map<String, List<BatchDynamicPluginConfiguration>> dynamicPluginProperties = new HashMap<String, List<BatchDynamicPluginConfiguration>>();

		public BatchPlugin(final String pluginName) {
			this.pluginName = pluginName;
		}

		/**
		 * API to add property to plugin.
		 * 
		 * @param pluginProperty {@link PluginProperty}
		 * @param configuration {@link BatchPluginConfiguration}
		 */
		public void addProperty(final PluginProperty pluginProperty, final BatchPluginConfiguration configuration) {
			List<BatchPluginConfiguration> configurations = pluginProperties.get(pluginProperty.getPropertyKey());
			if (configurations == null) {
				configurations = new LinkedList<BatchPluginConfiguration>();
				pluginProperties.put(pluginProperty.getPropertyKey(), configurations);
			}
			configurations.add(configuration);
		}

		/**
		 * API to add property to plugin for dynamic batch plugin configuration.
		 * 
		 * @param pluginProperty {@link PluginProperty}
		 * @param configuration {@link BatchDynamicPluginConfiguration}
		 */
		public void addProperty(final PluginProperty pluginProperty, final BatchDynamicPluginConfiguration configuration) {
			List<BatchDynamicPluginConfiguration> configurations = dynamicPluginProperties.get(pluginProperty.getPropertyKey());
			if (configurations == null) {
				configurations = new LinkedList<BatchDynamicPluginConfiguration>();
				dynamicPluginProperties.put(pluginProperty.getPropertyKey(), configurations);
			}
			configurations.add(configuration);
		}

		/**
		 * API to get the list of plugin configurations.
		 * 
		 * @param pluginProperty {@link PluginProperty}
		 * @return List<BatchPluginConfiguration>
		 */
		public List<BatchPluginConfiguration> getPluginConfigurations(final PluginProperty pluginProperty) {
			return pluginProperties.get(pluginProperty.getPropertyKey());
		}

		/**
		 * API to get list of dynamic plugin configurations.
		 * 
		 * @param pluginProperty {@link PluginProperty}
		 * @return List<BatchDynamicPluginConfiguration>
		 */
		public List<BatchDynamicPluginConfiguration> getDynamicPluginConfigurations(final PluginProperty pluginProperty) {
			return dynamicPluginProperties.get(pluginProperty.getPropertyKey());
		}

		/**
		 * To get the property size.
		 * 
		 * @return int.
		 */
		public int getPropertiesSize() {
			return pluginProperties.size();
		}

		/**
		 * This is override method to generate hashcode.
		 * 
		 * @return int
		 */
		@Override
		public int hashCode() {
			final int prime = BatchConstants.PRIME_CONST;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((pluginName == null) ? BatchConstants.ZERO : pluginName.hashCode());
			return result;
		}

		/**
		 * This is override method for checking equality of two objects.
		 * 
		 * @return boolean
		 */
		@Override
		public boolean equals(final Object obj) {
			boolean returnValue = true;
			if (this == obj) {
				returnValue = true;
			} else if (obj == null) {
				returnValue = false;
			} else if (getClass() != obj.getClass()) {
				returnValue = false;
			} else {
				final BatchPlugin other = (BatchPlugin) obj;

				if (pluginName == null) {
					if (other.pluginName != null) {
						returnValue = false;
					}
				} else if (!pluginName.equals(other.pluginName)) {
					returnValue = false;
				}
			}
			return returnValue;
		}

		private BatchPluginPropertyContainer getOuterType() {
			return BatchPluginPropertyContainer.this;
		}
	}

	/**
	 * This is override method to generate hashcode.
	 * 
	 * @return int
	 */
	@Override
	public int hashCode() {
		final int prime = BatchConstants.PRIME_CONST;
		int result = 1;
		result = prime * result + ((batchIdentifier == null) ? BatchConstants.ZERO : batchIdentifier.hashCode());
		return result;
	}

	/**
	 * This is override method for checking equality of two objects.
	 * 
	 * @return boolean
	 */
	@Override
	public boolean equals(final Object obj) {
		boolean returnValue = true;
		if (this == obj) {
			returnValue = true;
		} else if (obj == null) {
			returnValue = false;
		} else if (getClass() != obj.getClass()) {
			returnValue = false;
		} else {
			final BatchPluginPropertyContainer other = (BatchPluginPropertyContainer) obj;
			if (batchIdentifier == null) {
				if (other.batchIdentifier != null) {
					returnValue = false;
				}
			} else if (!batchIdentifier.equals(other.batchIdentifier)) {
				returnValue = false;
			}
		}
		return returnValue;
	}

	/**
	 * This is class for batch plugin configuration.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 */
	public class BatchPluginConfiguration implements Serializable {

		/**
		 * serialVersionUID long.
		 */
		private static final long serialVersionUID = 3L;

		/**
		 * identifier Long.
		 */
		private final Long identifier;

		/**
		 * key String.
		 */
		private final String key;

		/**
		 * qualifier String.
		 */
		private String qualifier;

		/**
		 * value String.
		 */
		private final String value;

		/**
		 * kvPageProcesses List<KVPageProcess>.
		 */
		private List<KVPageProcess> kvPageProcesses = new ArrayList<KVPageProcess>();

		/**
		 * Constructor.
		 * 
		 * @param identifier long
		 * @param key String
		 * @param value String
		 */
		public BatchPluginConfiguration(final Long identifier, final String key, final String value) {
			this.identifier = identifier;
			this.key = key;
			this.value = value;
			allBatchConfiguration.put(identifier, this);
		}

		/**
		 * Constructor.
		 * 
		 * @param identifier
		 * @param key
		 * @param value
		 * @param qualifier
		 */
		public BatchPluginConfiguration(final Long identifier, final String key, final String value, final String qualifier) {
			this(identifier, key, value);
			this.qualifier = qualifier;
		}

		/**
		 * Constructor.
		 * 
		 * @param batchClassPluginConfig {@link BatchClassPluginConfig}
		 */
		public BatchPluginConfiguration(final BatchClassPluginConfig batchClassPluginConfig) {
			this(batchClassPluginConfig.getId(), batchClassPluginConfig.getName(), batchClassPluginConfig.getValue(),
					batchClassPluginConfig.getQualifier());

			if (batchClassPluginConfig.getKvPageProcesses() != null) {
				this.setKvPageProcesses(batchClassPluginConfig.getKvPageProcesses());
			}
		}

		/**
		 * To get identifier.
		 * 
		 * @return Long
		 */
		public Long getId() {
			return identifier;
		}

		/**
		 * To get key.
		 * 
		 * @return String
		 */
		public String getKey() {
			return key;
		}

		/**
		 * To get qualifier.
		 * 
		 * @return String
		 */
		public String getQualifier() {
			return qualifier;
		}

		/**
		 * To get value.
		 * 
		 * @return String
		 */
		public String getValue() {
			return value;
		}

		/**
		 * To get KV Page Processes.
		 * 
		 * @return List<KVPageProcess>
		 */
		public List<KVPageProcess> getKvPageProcesses() {
			return kvPageProcesses;
		}

		/**
		 * To set KV Page Processes.
		 * 
		 * @param kvPageProcesses List<KVPageProcess>
		 */
		public final void setKvPageProcesses(final List<KVPageProcess> kvPageProcesses) {
			this.kvPageProcesses = kvPageProcesses;
		}

		/**
		 * This is override method to generate hashcode.
		 * 
		 * @return int
		 */
		@Override
		public int hashCode() {
			final int prime = BatchConstants.PRIME_CONST;
			int result = BatchConstants.ONE;

			result = prime * result + ((identifier == null) ? BatchConstants.ZERO : identifier.hashCode());
			return result;
		}

		/**
		 * This is override method for checking equality of two objects.
		 * 
		 * @return boolean
		 */
		@Override
		public boolean equals(final Object obj) {
			boolean returnValue = true;
			if (this == obj) {
				returnValue = true;
			} else if (obj == null) {
				returnValue = false;
			} else if (getClass() != obj.getClass()) {
				returnValue = false;
			} else {
				final BatchPluginConfiguration other = (BatchPluginConfiguration) obj;

				if (identifier == null) {
					if (other.identifier != null) {
						returnValue = false;
					}
				} else if (!identifier.equals(other.identifier)) {
					returnValue = false;
				}
			}
			return returnValue;
		}

	}

	/**
	 * This is class for function key.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 * 
	 */
	public class FunctionKey implements Serializable {

		/**
		 * serialVersionUID long.
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * idLong long.
		 */
		private long idLong;

		/**
		 * docType com.ephesoft.dcma.da.domain.DocumentType.
		 */
		private com.ephesoft.dcma.da.domain.DocumentType docType;

		/**
		 * shortcutKeyname String.
		 */
		private String shortcutKeyname;

		/**
		 * methodName String.
		 */
		private String methodName;

		/**
		 * uiLabel String.
		 */
		private String uiLabel;

		/**
		 * identifier String.
		 */
		private String identifier;

		/**
		 * To get id.
		 * 
		 * @return long
		 */
		public long getId() {
			return idLong;
		}

		/**
		 * To set id.
		 * 
		 * @param idLong long
		 */
		public void setId(final long idLong) {
			this.idLong = idLong;
		}

		/**
		 * To get doc type.
		 * 
		 * @return com.ephesoft.dcma.da.domain.DocumentType
		 */
		public com.ephesoft.dcma.da.domain.DocumentType getDocType() {
			return docType;
		}

		/**
		 * To set doc type.
		 * 
		 * @param docType com.ephesoft.dcma.da.domain.DocumentType
		 */
		public void setDocType(final com.ephesoft.dcma.da.domain.DocumentType docType) {
			this.docType = docType;
		}

		/**
		 * To get shortcut key name.
		 * 
		 * @return String
		 */
		public String getShortcutKeyname() {
			return shortcutKeyname;
		}

		/**
		 * To set shortcut key name.
		 * 
		 * @param shortcutKeyname String
		 */
		public void setShortcutKeyname(final String shortcutKeyname) {
			this.shortcutKeyname = shortcutKeyname;
		}

		/**
		 * To get method name.
		 * 
		 * @return String
		 */
		public String getMethodName() {
			return methodName;
		}

		/**
		 * To set method name.
		 * 
		 * @param methodName String
		 */
		public void setMethodName(final String methodName) {
			this.methodName = methodName;
		}

		/**
		 * To get Ui Label.
		 * 
		 * @return String
		 */
		public String getUiLabel() {
			return uiLabel;
		}

		/**
		 * To set Ui Label.
		 * 
		 * @param uiLabel String
		 */
		public void setUiLabel(final String uiLabel) {
			this.uiLabel = uiLabel;
		}

		/**
		 * To get identifier.
		 * 
		 * @return String
		 */
		public String getIdentifier() {
			return identifier;
		}

		/**
		 * To set identifier.
		 * 
		 * @param identifier String
		 */
		public void setIdentifier(final String identifier) {
			this.identifier = identifier;
		}
	}

	/**
	 * This is class for Batch Dynamic Plugin Configuration.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 */
	public class BatchDynamicPluginConfiguration implements Serializable {

		/**
		 * serialVersionUID long.
		 */
		private static final long serialVersionUID = 3L;

		/**
		 * identifier Long.
		 */
		private final Long identifier;

		/**
		 * key String.
		 */
		private final String key;

		/**
		 * value String.
		 */
		private final String value;

		/**
		 * description String.
		 */
		private final String description;

		/**
		 * parent {@link BatchDynamicPluginConfiguration}.
		 */
		private BatchDynamicPluginConfiguration parent;

		/**
		 * children Set<BatchDynamicPluginConfiguration>.
		 */
		private final Set<BatchDynamicPluginConfiguration> children = new HashSet<BatchDynamicPluginConfiguration>();

		/**
		 * Constructor.
		 * 
		 * @param identifier Long
		 * @param key String
		 * @param value String
		 * @param description String
		 */
		public BatchDynamicPluginConfiguration(final Long identifier, final String key, final String value, final String description) {
			this.identifier = identifier;
			this.key = key;
			this.value = value;
			this.description = description;
			allDynamicBatchConfiguration.put(identifier, this);
		}

		/**
		 * Constructor.
		 * 
		 * @param identifier Long
		 * @param key String
		 * @param value String
		 * @param description String
		 * @param parent {@link BatchClassDynamicPluginConfig}
		 */
		public BatchDynamicPluginConfiguration(final Long identifier, final String key, final String value, final String description,
				final BatchClassDynamicPluginConfig parent) {
			this(identifier, key, value, description);

			if (parent != null) {
				this.parent = allDynamicBatchConfiguration.get(parent.getId());
				if (this.parent == null) {
					this.parent = new BatchDynamicPluginConfiguration(identifier, parent.getName(), parent.getValue(), parent
							.getDescription(), parent.getParent());
				}
				this.parent.addChild(this);
			}
		}

		/**
		 * Constructor.
		 * 
		 * @param batchClassDynamicPluginConfig {@link BatchClassDynamicPluginConfig}
		 */
		public BatchDynamicPluginConfiguration(final BatchClassDynamicPluginConfig batchClassDynamicPluginConfig) {
			this(batchClassDynamicPluginConfig.getId(), batchClassDynamicPluginConfig.getName(), batchClassDynamicPluginConfig
					.getValue(), batchClassDynamicPluginConfig.getDescription());

			if (batchClassDynamicPluginConfig.getChildren() != null) {
				for (final BatchClassDynamicPluginConfig child : batchClassDynamicPluginConfig.getChildren()) {
					BatchDynamicPluginConfiguration pChild = allDynamicBatchConfiguration.get(child.getId());
					if (pChild == null) {
						pChild = new BatchDynamicPluginConfiguration(child);
						addPluginProperty(batchClassDynamicPluginConfig.getBatchClassPlugin().getPlugin().getPluginName(), pChild);
					}
					this.addChild(pChild);
				}
			}

			if (batchClassDynamicPluginConfig.getParent() != null) {
				this.parent = allDynamicBatchConfiguration.get(batchClassDynamicPluginConfig.getParent().getId());
				if (this.parent == null) {
					this.parent = new BatchDynamicPluginConfiguration(batchClassDynamicPluginConfig.getParent());
					addPluginProperty(batchClassDynamicPluginConfig.getBatchClassPlugin().getPlugin().getPluginName(), this.parent);
				}
			}

		}

		/**
		 * To get id.
		 * 
		 * @return Long
		 */
		public Long getId() {
			return identifier;
		}

		/**
		 * To get children.
		 * 
		 * @return Set<BatchDynamicPluginConfiguration>
		 */
		public Set<BatchDynamicPluginConfiguration> getChildren() {
			return children;
		}

		/**
		 * To add new child.
		 * 
		 * @param configuration {@link BatchDynamicPluginConfiguration}
		 */
		public final void addChild(final BatchDynamicPluginConfiguration configuration) {
			children.add(configuration);
		}

		/**
		 * To get key.
		 * 
		 * @return String
		 */
		public String getKey() {
			return key;
		}

		/**
		 * To get value.
		 * 
		 * @return String
		 */
		public String getValue() {
			return value;
		}

		/**
		 * To get parent.
		 * 
		 * @return {@link BatchDynamicPluginConfiguration}
		 */
		public BatchDynamicPluginConfiguration getParent() {
			return parent;
		}

		/**
		 * To get description.
		 * 
		 * @return String
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * This is override method to generate hashcode.
		 * 
		 * @return int
		 */
		@Override
		public int hashCode() {
			final int prime = BatchConstants.PRIME_CONST;
			int result = BatchConstants.ONE;

			result = prime * result + ((identifier == null) ? BatchConstants.ZERO : identifier.hashCode());
			return result;
		}

		/**
		 * This is override method for checking equality of two objects.
		 * 
		 * @return boolean
		 */
		@Override
		public boolean equals(final Object obj) {
			boolean returnValue = true;
			if (this == obj) {
				returnValue = true;
			} else if (obj == null) {
				returnValue = false;
			} else if (getClass() != obj.getClass()) {
				returnValue = false;
			} else {
				final BatchDynamicPluginConfiguration other = (BatchDynamicPluginConfiguration) obj;

				if (identifier == null) {
					if (other.identifier != null) {
						returnValue = false;
					}
				} else if (!identifier.equals(other.identifier)) {
					returnValue = false;
				}
			}
			return returnValue;
		}

	}

	/**
	 * This is class for document type.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 * 
	 */
	public class DocumentType implements Serializable {

		/**
		 * serialVersionUID long.
		 */
		private static final long serialVersionUID = 4L;

		/**
		 * idLong long.
		 */
		private long idLong;

		/**
		 * identifier String.
		 */
		private String identifier;

		/**
		 * creationDate Date.
		 */
		private Date creationDate;

		/**
		 * lastModified Date.
		 */
		private Date lastModified;

		/**
		 * name String.
		 */
		private String name;

		/**
		 * description String.
		 */
		private String description;

		/**
		 * rspProjectFileName String.
		 */
		private String rspProjectFileName;

		/**
		 * minConfidenceThreshold float.
		 */
		private float minConfidenceThreshold;

		/**
		 * priority String.
		 */
		private String priority;

		/**
		 * hidden boolean.
		 */
		private boolean hidden;

		/**
		 * batchClass BatchClass.
		 */
		private BatchClass batchClass;

		/**
		 * docPageTypes Map<String, PageType>.
		 */
		private Map<String, PageType> docPageTypes = new HashMap<String, PageType>();

		/**
		 * docFieldTypes Map<String, FieldType>.
		 */
		private Map<String, FieldType> docFieldTypes = new HashMap<String, FieldType>();

		/**
		 * docFunctionKeys Map<String, FunctionKey>.
		 */
		private Map<String, FunctionKey> docFunctionKeys = new HashMap<String, FunctionKey>();

		/**
		 * To get name.
		 * 
		 * @return String
		 */
		public String getName() {
			return name;
		}

		/**
		 * To set name.
		 * 
		 * @param name String
		 */
		public void setName(final String name) {
			this.name = name;
		}

		/**
		 * To get description.
		 * 
		 * @return String
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * To set description.
		 * 
		 * @param description String
		 */
		public void setDescription(final String description) {
			this.description = description;
		}

		/**
		 * To get min confidence threshold.
		 * 
		 * @return float
		 */
		public float getMinConfidenceThreshold() {
			return minConfidenceThreshold;
		}

		/**
		 * To set min confidence threshold.
		 * 
		 * @param minConfidenceThreshold float
		 */
		public void setMinConfidenceThreshold(final float minConfidenceThreshold) {
			this.minConfidenceThreshold = minConfidenceThreshold;
		}

		/**
		 * To get priority.
		 * 
		 * @return String
		 */
		public String getPriority() {
			return priority;
		}

		/**
		 * To set priority.
		 * 
		 * @param priority String
		 */
		public void setPriority(final String priority) {
			this.priority = priority;
		}

		/**
		 * To get Doc Page Types.
		 * 
		 * @return Map<String, PageType>
		 */
		public Map<String, PageType> getDocPageTypes() {
			return docPageTypes;
		}

		/**
		 * To set Doc Page Types.
		 * 
		 * @param docPageTypes Map<String, PageType>
		 */
		public void setDocPageTypes(final Map<String, PageType> docPageTypes) {
			this.docPageTypes = docPageTypes;
		}

		/**
		 * To get Doc Function Keys.
		 * 
		 * @return Map<String, FunctionKey>
		 */
		public Map<String, FunctionKey> getDocFunctionKeys() {
			return docFunctionKeys;
		}

		/**
		 * To set Doc Function Keys.
		 * 
		 * @param Map<String, FunctionKey>
		 */
		public void setDocFunctionKeys(final Map<String, FunctionKey> docFunctionKeys) {
			this.docFunctionKeys = docFunctionKeys;
		}

		/**
		 * To get Doc Field Types.
		 * 
		 * @return Map<String, FieldType>
		 */
		public Map<String, FieldType> getDocFieldTypes() {
			return docFieldTypes;
		}

		/**
		 * To set Doc Field Types.
		 * 
		 * @param docFieldTypes Map<String, FieldType>
		 */
		public void setDocFieldTypes(final Map<String, FieldType> docFieldTypes) {
			this.docFieldTypes = docFieldTypes;
		}

		/**
		 * To get id.
		 * 
		 * @return long
		 */
		public long getId() {
			return idLong;
		}

		/**
		 * To set id.
		 * 
		 * @param idLong long
		 */
		public void setId(final long idLong) {
			this.idLong = idLong;
		}

		/**
		 * To get creation date.
		 * 
		 * @return Date
		 */
		public Date getCreationDate() {
			return creationDate;
		}

		/**
		 * To set creation date.
		 * 
		 * @param creationDate Date
		 */
		public void setCreationDate(final Date creationDate) {
			this.creationDate = creationDate;
		}

		/**
		 * To get lastModified date.
		 * 
		 * @return Date
		 */
		public Date getLastModified() {
			return lastModified;
		}

		/**
		 * To set lastModified date.
		 * 
		 * @param lastModified Date
		 */
		public void setLastModified(final Date lastModified) {
			this.lastModified = lastModified;
		}

		/**
		 * To check whether hidden is true or not.
		 * 
		 * @return boolean
		 */
		public boolean isHidden() {
			return hidden;
		}

		/**
		 * To set hidden.
		 * 
		 * @param hidden boolean
		 */
		public void setHidden(final boolean hidden) {
			this.hidden = hidden;
		}

		/**
		 * To get batch class.
		 * 
		 * @return BatchClass
		 */
		public BatchClass getBatchClass() {
			return batchClass;
		}

		/**
		 * To set batch class.
		 * 
		 * @param batchClass BatchClass
		 */
		public void setBatchClass(final BatchClass batchClass) {
			this.batchClass = batchClass;
		}

		/**
		 * To get identifier.
		 * 
		 * @return String
		 */
		public String getIdentifier() {
			return identifier;
		}

		/**
		 * To set identifier.
		 * 
		 * @param identifier String
		 */
		public void setIdentifier(final String identifier) {
			this.identifier = identifier;
		}

		/**
		 * To get Rsp Project File Name.
		 * 
		 * @return String
		 */
		public String getRspProjectFileName() {
			return rspProjectFileName;
		}

		/**
		 * To set Rsp Project File Name.
		 * 
		 * @param rspProjectFileName String
		 */
		public void setRspProjectFileName(final String rspProjectFileName) {
			this.rspProjectFileName = rspProjectFileName;
		}

	}

	/**
	 * This is class for page type.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 * 
	 */
	public class PageType implements Serializable {

		/**
		 * serialVersionUID long.
		 */
		private static final long serialVersionUID = 5L;

		/**
		 * idLong long.
		 */
		private long idLong;

		/**
		 * identifier String.
		 */
		private String identifier;

		/**
		 * creationDate Date.
		 */
		private Date creationDate;

		/**
		 * lastModified Date.
		 */
		private Date lastModified;

		/**
		 * name String.
		 */
		private String name;

		/**
		 * description String.
		 */
		private String description;

		/**
		 * docType com.ephesoft.dcma.da.domain.DocumentType.
		 */
		private com.ephesoft.dcma.da.domain.DocumentType docType;

		/**
		 * To get name.
		 * 
		 * @return String
		 */
		public String getName() {
			return name;
		}

		/**
		 * To set name.
		 * 
		 * @param name String
		 */
		public void setName(final String name) {
			this.name = name;
		}

		/**
		 * To get description.
		 * 
		 * @return String
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * To set description.
		 * 
		 * @param description String
		 */
		public void setDescription(final String description) {
			this.description = description;
		}

		/**
		 * To get id.
		 * 
		 * @return long
		 */
		public long getId() {
			return idLong;
		}

		/**
		 * To set id.
		 * 
		 * @param idLong long
		 */
		public void setId(final long idLong) {
			this.idLong = idLong;
		}

		/**
		 * To get creation date.
		 * 
		 * @return Date
		 */
		public Date getCreationDate() {
			return creationDate;
		}

		/**
		 * To set creation date.
		 * 
		 * @param creationDate Date
		 */
		public void setCreationDate(final Date creationDate) {
			this.creationDate = creationDate;
		}

		/**
		 * To get last modified date.
		 * 
		 * @return Date
		 */
		public Date getLastModified() {
			return lastModified;
		}

		/**
		 * To set last modified date.
		 * 
		 * @param lastModified Date
		 */
		public void setLastModified(final Date lastModified) {
			this.lastModified = lastModified;
		}

		/**
		 * To get doc type.
		 * 
		 * @return com.ephesoft.dcma.da.domain.DocumentType
		 */
		public com.ephesoft.dcma.da.domain.DocumentType getDocType() {
			return docType;
		}

		/**
		 * To set doc type.
		 * 
		 * @param docType com.ephesoft.dcma.da.domain.DocumentType
		 */
		public void setDocType(final com.ephesoft.dcma.da.domain.DocumentType docType) {
			this.docType = docType;
		}

		/**
		 * To get identifier.
		 * 
		 * @return String
		 */
		public String getIdentifier() {
			return identifier;
		}

		/**
		 * To set identifier.
		 * 
		 * @return String
		 */
		public void setIdentifier(final String identifier) {
			this.identifier = identifier;
		}

	}

	/**
	 * This is class for Field Type.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 */
	public class FieldType implements Serializable {

		/**
		 * serialVersionUID long.
		 */
		private static final long serialVersionUID = 6L;

		/**
		 * identifier String.
		 */
		private String identifier;

		/**
		 * creationDate Date.
		 */
		private Date creationDate;

		/**
		 * lastModified Date.
		 */
		private Date lastModified;

		/**
		 * idLong long.
		 */
		private long idLong;

		/**
		 * name String.
		 */
		private String name;

		/**
		 * fieldOrderNumber int.
		 */
		private int fieldOrderNumber;

		/**
		 * description String.
		 */
		private String description;

		/**
		 * dataType String.
		 */
		private String dataType;

		/**
		 * pattern String.
		 */
		private String pattern;

		/**
		 * barcodeType String.
		 */
		private String barcodeType;

		/**
		 * fieldOptionValueList String.
		 */
		private String fieldOptionValueList;

		/**
		 * hidden boolean.
		 */
		private boolean hidden;

		/**
		 * multiLine boolean.
		 */
		private boolean multiLine;

		/**
		 * isReadOnly boolean.
		 */
		private boolean isReadOnly;

		/**
		 * sampleValue String.
		 */
		private String sampleValue;

		/**
		 * regexValidation Map<String, RegexValidation>.
		 */
		private Map<String, RegexValidation> regexValidation = new HashMap<String, RegexValidation>();

		/**
		 * docType com.ephesoft.dcma.da.domain.DocumentType.
		 */
		private com.ephesoft.dcma.da.domain.DocumentType docType;

		/**
		 * fieldKVExtraction Map<String, KVExtraction>.
		 */
		private Map<String, KVExtraction> fieldKVExtraction = new HashMap<String, KVExtraction>();

		/**
		 * To get name.
		 * 
		 * @return String
		 */
		public String getName() {
			return name;
		}

		/**
		 * To set name.
		 * 
		 * @param name String
		 */
		public void setName(final String name) {
			this.name = name;
		}

		/**
		 * To get description.
		 * 
		 * @return String
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * To set description.
		 * 
		 * @param description String
		 */
		public void setDescription(final String description) {
			this.description = description;
		}

		/**
		 * To get data type.
		 * 
		 * @return String
		 */
		public String getDataType() {
			return dataType;
		}

		/**
		 * To set data type.
		 * 
		 * @param dataType String
		 */
		public void setDataType(final String dataType) {
			this.dataType = dataType;
		}

		/**
		 * To get pattern.
		 * 
		 * @return String
		 */
		public String getPattern() {
			return pattern;
		}

		/**
		 * To set pattern.
		 * 
		 * @param pattern String
		 */
		public void setPattern(final String pattern) {
			this.pattern = pattern;
		}

		/**
		 * To get field KV Extraction.
		 * 
		 * @return Map<String, KVExtraction>
		 */
		public Map<String, KVExtraction> getFieldKVExtraction() {
			return fieldKVExtraction;
		}

		/**
		 * To set field KV Extraction.
		 * 
		 * @param fieldKVExtraction Map<String, KVExtraction>
		 */
		public void setFieldKVExtraction(final Map<String, KVExtraction> fieldKVExtraction) {
			this.fieldKVExtraction = fieldKVExtraction;
		}

		/**
		 * To get id.
		 * 
		 * @return long
		 */
		public long getId() {
			return idLong;
		}

		/**
		 * To set id.
		 * 
		 * @param idLong long
		 */
		public void setId(final long idLong) {
			this.idLong = idLong;
		}

		/**
		 * To get creation date.
		 * 
		 * @return Date
		 */
		public Date getCreationDate() {
			return creationDate;
		}

		/**
		 * To set creation date.
		 * 
		 * @param creationDate Date
		 */
		public void setCreationDate(final Date creationDate) {
			this.creationDate = creationDate;
		}

		/**
		 * To get last modified date.
		 * 
		 * @return Date
		 */
		public Date getLastModified() {
			return lastModified;
		}

		/**
		 * To set last modified date.
		 * 
		 * @param lastModified Date
		 */
		public void setLastModified(final Date lastModified) {
			this.lastModified = lastModified;
		}

		/**
		 * To get doc type.
		 * 
		 * @return com.ephesoft.dcma.da.domain.DocumentType
		 */
		public com.ephesoft.dcma.da.domain.DocumentType getDocType() {
			return docType;
		}

		/**
		 * To set doc type.
		 * 
		 * @param docType com.ephesoft.dcma.da.domain.DocumentType
		 */
		public void setDocType(final com.ephesoft.dcma.da.domain.DocumentType docType) {
			this.docType = docType;
		}

		/**
		 * To get identifier.
		 * 
		 * @return String
		 */
		public String getIdentifier() {
			return identifier;
		}

		/**
		 * To set identifier.
		 * 
		 * @param identifier String
		 */
		public void setIdentifier(final String identifier) {
			this.identifier = identifier;
		}

		/**
		 * To get field Order Number.
		 * 
		 * @return int
		 */
		public int getFieldOrderNumber() {
			return fieldOrderNumber;
		}

		/**
		 * To set field Order Number.
		 * 
		 * @param fieldOrderNumber int
		 */
		public void setFieldOrderNumber(final int fieldOrderNumber) {
			this.fieldOrderNumber = fieldOrderNumber;
		}

		/**
		 * To get Barcode Type.
		 * 
		 * @return String
		 */
		public String getBarcodeType() {
			return barcodeType;
		}

		/**
		 * To set Barcode Type.
		 * 
		 * @param barcodeType String
		 */
		public void setBarcodeType(final String barcodeType) {
			this.barcodeType = barcodeType;
		}

		/**
		 * To get field Option Value List.
		 * 
		 * @return String
		 */
		public String getFieldOptionValueList() {
			return fieldOptionValueList;
		}

		/**
		 * To set field Option Value List.
		 * 
		 * @param fieldOptionValueList String
		 */
		public void setFieldOptionValueList(final String fieldOptionValueList) {
			this.fieldOptionValueList = fieldOptionValueList;
		}

		/**
		 * To get hidden value.
		 * 
		 * @return boolean
		 */
		public boolean isHidden() {
			return hidden;
		}

		/**
		 * To set MultiLine.
		 * 
		 * @param isMultiLine boolean
		 */
		public void setMultiLine(final boolean isMultiLine) {
			this.multiLine = isMultiLine;
		}

		/**
		 * To get MultiLine.
		 * 
		 * @return boolean
		 */
		public boolean isMultiLine() {
			return multiLine;
		}

		/**
		 * To set ready only.
		 * 
		 * @param isReadOnly boolean
		 */
		public void setReadOnly(final boolean isReadOnly) {
			this.isReadOnly = isReadOnly;
		}

		/**
		 * To get ready only.
		 * 
		 * @return boolean
		 */
		public boolean getIsReadOnly() {
			return isReadOnly;
		}

		/**
		 * To set hidden.
		 * 
		 * @param isHidden boolean
		 */
		public void setHidden(final boolean isHidden) {
			this.hidden = isHidden;
		}

		/**
		 * To get Regex Validation.
		 * 
		 * @return Map<String, RegexValidation>
		 */
		public Map<String, RegexValidation> getRegexValidation() {
			return regexValidation;
		}

		/**
		 * To set Regex Validation.
		 * 
		 * @param regexValidation Map<String, RegexValidation>
		 */
		public void setRegexValidation(final Map<String, RegexValidation> regexValidation) {
			this.regexValidation = regexValidation;
		}

		/**
		 * To get sample value.
		 * 
		 * @return String
		 */
		public String getSampleValue() {
			return sampleValue;
		}

		/**
		 * To set sample value.
		 * 
		 * @param sampleValue String
		 */
		public void setSampleValue(final String sampleValue) {
			this.sampleValue = sampleValue;
		}

	}

	/**
	 * This is class for KV Extraction.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 */
	public class KVExtraction implements Serializable {

		/**
		 * serialVersionUID long.
		 */
		private static final long serialVersionUID = 7L;

		/**
		 * identifier long.
		 */
		private long identifier;

		/**
		 * creationDate Date.
		 */
		private Date creationDate;

		/**
		 * lastModified Date.
		 */
		private Date lastModified;

		/**
		 * keyPattern String.
		 */
		private String keyPattern;

		/**
		 * location String.
		 */
		private String location;

		/**
		 * valuePattern String.
		 */
		private String valuePattern;

		/**
		 * fieldType com.ephesoft.dcma.da.domain.FieldType.
		 */
		private com.ephesoft.dcma.da.domain.FieldType fieldType;

		/**
		 * To get key Pattern.
		 * 
		 * @return String
		 */
		public String getKeyPattern() {
			return keyPattern;
		}

		/**
		 * To set key Pattern.
		 * 
		 * @param keyPattern String
		 */
		public void setKeyPattern(final String keyPattern) {
			this.keyPattern = keyPattern;
		}

		/**
		 * To get location.
		 * 
		 * @return String
		 */
		public String getLocation() {
			return location;
		}

		/**
		 * To set location.
		 * 
		 * @param location String
		 */
		public void setLocation(final String location) {
			this.location = location;
		}

		/**
		 * To get value Pattern.
		 * 
		 * @return String
		 */
		public String getValuePattern() {
			return valuePattern;
		}

		/**
		 * To set value pattern.
		 * 
		 * @param valuePattern String
		 */
		public void setValuePattern(final String valuePattern) {
			this.valuePattern = valuePattern;
		}

		/**
		 * To get id.
		 * 
		 * @return long
		 */
		public long getId() {
			return identifier;
		}

		/**
		 * To set id.
		 * 
		 * @param identifier long
		 */
		public void setId(final long identifier) {
			this.identifier = identifier;
		}

		/**
		 * To get creation date.
		 * 
		 * @return Date
		 */
		public Date getCreationDate() {
			return creationDate;
		}

		/**
		 * To set creation date.
		 * 
		 * @param creationDate Date
		 */
		public void setCreationDate(final Date creationDate) {
			this.creationDate = creationDate;
		}

		/**
		 * To get last modified date.
		 * 
		 * @return Date
		 */
		public Date getLastModified() {
			return lastModified;
		}

		/**
		 * To set last modified date.
		 * 
		 * @param lastModified Date
		 */
		public void setLastModified(final Date lastModified) {
			this.lastModified = lastModified;
		}

		/**
		 * To get Field Type.
		 * 
		 * @return com.ephesoft.dcma.da.domain.FieldType
		 */
		public com.ephesoft.dcma.da.domain.FieldType getFieldType() {
			return fieldType;
		}

		/**
		 * To set Field Type.
		 * 
		 * @param fieldType com.ephesoft.dcma.da.domain.FieldType
		 */
		public void setFieldType(final com.ephesoft.dcma.da.domain.FieldType fieldType) {
			this.fieldType = fieldType;
		}
	}

	/**
	 * This is class for Regex Validation.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 * 
	 */
	public class RegexValidation implements Serializable {

		/**
		 * serialVersionUID long.
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * identifier long.
		 */
		private long identifier;

		/**
		 * creationDate Date.
		 */
		private Date creationDate;

		/**
		 * lastModified Date.
		 */
		private Date lastModified;

		/**
		 * pattern String.
		 */
		private String pattern;

		/**
		 * fieldType com.ephesoft.dcma.da.domain.FieldType.
		 */
		private com.ephesoft.dcma.da.domain.FieldType fieldType;

		/**
		 * To get the id.
		 * 
		 * @return identifier long
		 */
		public long getId() {
			return identifier;
		}

		/**
		 * To set the id.
		 * 
		 * @param identifier long
		 */
		public void setId(final long identifier) {
			this.identifier = identifier;
		}

		/**
		 * To get creation date.
		 * 
		 * @return creationDate Date
		 */
		public Date getCreationDate() {
			return creationDate;
		}

		/**
		 * To set creation date.
		 * 
		 * @param creationDate Date
		 */
		public void setCreationDate(final Date creationDate) {
			this.creationDate = creationDate;
		}

		/**
		 * To get last modified date.
		 * 
		 * @return lastModified Date
		 */
		public Date getLastModified() {
			return lastModified;
		}

		/**
		 * To set last modified date.
		 * 
		 * @param lastModified Date
		 */
		public void setLastModified(final Date lastModified) {
			this.lastModified = lastModified;
		}

		/**
		 * To get pattern.
		 * 
		 * @return pattern String
		 */
		public String getPattern() {
			return pattern;
		}

		/**
		 * To set pattern.
		 * 
		 * @param pattern String
		 */
		public void setPattern(final String pattern) {
			this.pattern = pattern;
		}

		/**
		 * To get Field Type.
		 * 
		 * @return fieldType com.ephesoft.dcma.da.domain.FieldType
		 */
		public com.ephesoft.dcma.da.domain.FieldType getFieldType() {
			return fieldType;
		}

		/**
		 * To set Field Type.
		 * 
		 * @param fieldType com.ephesoft.dcma.da.domain.FieldType
		 */
		public void setFieldType(final com.ephesoft.dcma.da.domain.FieldType fieldType) {
			this.fieldType = fieldType;
		}

	}
}
