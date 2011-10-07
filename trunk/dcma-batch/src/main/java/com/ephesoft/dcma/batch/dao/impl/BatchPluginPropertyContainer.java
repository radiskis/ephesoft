/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2010-2011 Ephesoft Inc. 
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

/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2010-2011 Ephesoft Inc. 
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

import com.ephesoft.dcma.core.common.PluginProperty;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassDynamicPluginConfig;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;
import com.ephesoft.dcma.da.domain.KVPageProcess;

public class BatchPluginPropertyContainer implements Serializable {

	private static final long serialVersionUID = 1L;

	private String batchIdentifier;
	private Map<String, BatchPlugin> plugins = new HashMap<String, BatchPlugin>();
	private Map<Long, BatchPluginConfiguration> allBatchConfiguration = new HashMap<Long, BatchPluginConfiguration>();
	private Map<Long, BatchDynamicPluginConfiguration> allDynamicBatchConfiguration = new HashMap<Long, BatchDynamicPluginConfiguration>();
	private Map<String, DocumentType> allDocumentTypes = new HashMap<String, DocumentType>();

	public BatchPluginPropertyContainer(String batchIdentifier) {
		this.batchIdentifier = batchIdentifier;
	}

	/**
	 * @return the allDocumentTypes
	 */
	public Map<String, DocumentType> getAllDocumentTypes() {
		return allDocumentTypes;
	}

	/**
	 * @param allDocumentTypes the allDocumentTypes to set
	 */
	public void setAllDocumentTypes(Map<String, DocumentType> allDocumentTypes) {
		this.allDocumentTypes = allDocumentTypes;
	}

	public BatchPlugin getPlugin(String pluginName) {
		if (plugins.get(pluginName) == null)
			plugins.put(pluginName, new BatchPlugin(pluginName));
		return plugins.get(pluginName);
	}

	private void addPluginProperty(String pluginName, final BatchPluginConfiguration configuration) {
		BatchPlugin plugin = getPlugin(pluginName);
		plugin.addProperty(new PluginProperty() {

			@Override
			public String getPropertyKey() {
				return configuration.getKey();
			}
		}, configuration);
	}

	private void addPluginProperty(String pluginName, final BatchDynamicPluginConfiguration configuration) {
		BatchPlugin plugin = getPlugin(pluginName);
		plugin.addProperty(new PluginProperty() {

			@Override
			public String getPropertyKey() {
				return configuration.getKey();
			}
		}, configuration);
	}

	public List<BatchPluginConfiguration> getPlginConfiguration(String pluginName, PluginProperty pluginProperty) {
		BatchPlugin plugin = getPlugin(pluginName);
		if (plugin == null)
			return null;
		return plugin.getPluginConfigurations(pluginProperty);
	}

	public void populate(List<BatchClassPluginConfig> batchClassPluginConfigs) {
		for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
			String pluginName = batchClassPluginConfig.getBatchClassPlugin().getPlugin().getPluginName();

			BatchPluginConfiguration configuration = allBatchConfiguration.get(batchClassPluginConfig.getId());
			if (configuration == null) {
				/*
				 * configuration = new BatchPluginConfiguration(batchClassPluginConfig.getId(), batchClassPluginConfig.getName(),
				 * batchClassPluginConfig.getValue(), batchClassPluginConfig.getQualifier(), batchClassPluginConfig.getParent());
				 */
				configuration = new BatchPluginConfiguration(batchClassPluginConfig);

				addPluginProperty(pluginName, configuration);
			}
		}
	}

	public void populateDynamicPluginConfigs(List<BatchClassDynamicPluginConfig> batchClassDynamicPluginConfigs) {
		for (BatchClassDynamicPluginConfig batchClassDynamicPluginConfig : batchClassDynamicPluginConfigs) {
			String pluginName = batchClassDynamicPluginConfig.getBatchClassPlugin().getPlugin().getPluginName();

			BatchDynamicPluginConfiguration configuration = allDynamicBatchConfiguration.get(batchClassDynamicPluginConfig.getId());
			if (configuration == null) {
				/*
				 * configuration = new BatchPluginConfiguration(batchClassPluginConfig.getId(), batchClassPluginConfig.getName(),
				 * batchClassPluginConfig.getValue(), batchClassPluginConfig.getQualifier(), batchClassPluginConfig.getParent());
				 */
				configuration = new BatchDynamicPluginConfiguration(batchClassDynamicPluginConfig);

				addPluginProperty(pluginName, configuration);
			}
		}
	}

	public void populateDocumentTypes(List<com.ephesoft.dcma.da.domain.DocumentType> documentTypes,
			String batchInstanceIdentifierIdentifier) {
		if (documentTypes != null && !documentTypes.isEmpty()) {
			for (com.ephesoft.dcma.da.domain.DocumentType documentType : documentTypes) {
				DocumentType localDocumentType = new DocumentType();
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
				List<com.ephesoft.dcma.da.domain.PageType> pageTypesList = documentType.getPages();
				Map<String, PageType> tempPageTypes = new HashMap<String, PageType>();
				for (com.ephesoft.dcma.da.domain.PageType pageType : pageTypesList) {
					PageType localPageType = new PageType();
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
				List<com.ephesoft.dcma.da.domain.FunctionKey> functionKeyList = documentType.getFunctionKeys();
				Map<String, FunctionKey> tempFunctionKeys = new HashMap<String, FunctionKey>();
				for (com.ephesoft.dcma.da.domain.FunctionKey functionKey : functionKeyList) {
					FunctionKey localFunctionKey = new FunctionKey();
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
				List<com.ephesoft.dcma.da.domain.FieldType> allFieldTypes = documentType.getFieldTypes();
				Map<String, FieldType> tempFieldTypes = new HashMap<String, FieldType>();
				if (allFieldTypes != null && !allFieldTypes.isEmpty()) {
					for (com.ephesoft.dcma.da.domain.FieldType tempField : allFieldTypes) {
						FieldType fieldType = new FieldType();
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
						Map<String, KVExtraction> tempKVExtraction = new HashMap<String, KVExtraction>();
						List<com.ephesoft.dcma.da.domain.KVExtraction> tempList = tempField.getKvExtraction();
						if (tempList != null && !tempList.isEmpty()) {
							for (com.ephesoft.dcma.da.domain.KVExtraction kvExtraction : tempList) {
								KVExtraction var = new KVExtraction();
								var.setId(kvExtraction.getId());
								var.setCreationDate(kvExtraction.getCreationDate());
								var.setLastModified(kvExtraction.getLastModified());
								var.setFieldType(kvExtraction.getFieldType());
								var.setKeyPattern(kvExtraction.getKeyPattern());
								var.setLocation(kvExtraction.getLocationType().name());
								var.setValuePattern(kvExtraction.getValuePattern());
								tempKVExtraction.put(String.valueOf(kvExtraction.getId()), var);
							}
						}
						fieldType.setFieldKVExtraction(tempKVExtraction);
						tempFieldTypes.put(fieldType.getName(), fieldType);
						Map<String, RegexValidation> tempRegexValidation = new HashMap<String, RegexValidation>();
						List<com.ephesoft.dcma.da.domain.RegexValidation> tempList2 = tempField.getRegexValidation();
						if (tempList2 != null && !tempList2.isEmpty()) {
							for (com.ephesoft.dcma.da.domain.RegexValidation regexValidation : tempList2) {
								RegexValidation var = new RegexValidation();
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
				localDocumentType.setDocFieldTypes(tempFieldTypes);
				allDocumentTypes.put(documentType.getName(), localDocumentType);
			}
		}
	}

	public class BatchPlugin implements Serializable {

		private static final long serialVersionUID = 2L;
		private String pluginName;
		private Map<String, List<BatchPluginConfiguration>> pluginProperties = new HashMap<String, List<BatchPluginConfiguration>>();
		private Map<String, List<BatchDynamicPluginConfiguration>> dynamicPluginProperties = new HashMap<String, List<BatchDynamicPluginConfiguration>>();

		public BatchPlugin(String pluginName) {
			this.pluginName = pluginName;
		}

		public void addProperty(PluginProperty pluginProperty, BatchPluginConfiguration configuration) {
			List<BatchPluginConfiguration> configurations = pluginProperties.get(pluginProperty.getPropertyKey());
			if (configurations == null) {
				configurations = new LinkedList<BatchPluginConfiguration>();
				pluginProperties.put(pluginProperty.getPropertyKey(), configurations);
			}
			configurations.add(configuration);
		}

		public void addProperty(PluginProperty pluginProperty, BatchDynamicPluginConfiguration configuration) {
			List<BatchDynamicPluginConfiguration> configurations = dynamicPluginProperties.get(pluginProperty.getPropertyKey());
			if (configurations == null) {
				configurations = new LinkedList<BatchDynamicPluginConfiguration>();
				dynamicPluginProperties.put(pluginProperty.getPropertyKey(), configurations);
			}
			configurations.add(configuration);
		}

		public List<BatchPluginConfiguration> getPluginConfigurations(PluginProperty pluginProperty) {
			return pluginProperties.get(pluginProperty.getPropertyKey());
		}

		public List<BatchDynamicPluginConfiguration> getDynamicPluginConfigurations(PluginProperty pluginProperty) {
			return dynamicPluginProperties.get(pluginProperty.getPropertyKey());
		}

		public int getPropertiesSize() {
			return pluginProperties.size();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((pluginName == null) ? 0 : pluginName.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			BatchPlugin other = (BatchPlugin) obj;
			/*
			 * if (!getOuterType().equals(other.getOuterType())) return false;
			 */
			if (pluginName == null) {
				if (other.pluginName != null)
					return false;
			} else if (!pluginName.equals(other.pluginName))
				return false;
			return true;
		}

		private BatchPluginPropertyContainer getOuterType() {
			return BatchPluginPropertyContainer.this;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((batchIdentifier == null) ? 0 : batchIdentifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BatchPluginPropertyContainer other = (BatchPluginPropertyContainer) obj;
		if (batchIdentifier == null) {
			if (other.batchIdentifier != null)
				return false;
		} else if (!batchIdentifier.equals(other.batchIdentifier))
			return false;
		return true;
	}

	public class BatchPluginConfiguration implements Serializable {

		private static final long serialVersionUID = 3L;
		private Long id;
		private String key;
		private String qualifier;
		private String value;

		private List<KVPageProcess> kvPageProcesses = new ArrayList<KVPageProcess>();

		public BatchPluginConfiguration(Long id, String key, String value) {
			this.id = id;
			this.key = key;
			this.value = value;
			allBatchConfiguration.put(id, this);
		}

		public BatchPluginConfiguration(Long id, String key, String value, String qualifier) {
			this(id, key, value);
			this.qualifier = qualifier;
		}

		public BatchPluginConfiguration(Long id, String key, String value, String qualifier, BatchClassPluginConfig parent) {
			this(id, key, value, qualifier);
		}

		public BatchPluginConfiguration(BatchClassPluginConfig batchClassPluginConfig) {
			this(batchClassPluginConfig.getId(), batchClassPluginConfig.getName(), batchClassPluginConfig.getValue(),
					batchClassPluginConfig.getQualifier());

			if (batchClassPluginConfig.getKvPageProcesses() != null) {
				this.setKvPageProcesses(batchClassPluginConfig.getKvPageProcesses());
			}
		}

		public Long getId() {
			return id;
		}

		public String getKey() {
			return key;
		}

		public String getQualifier() {
			return qualifier;
		}

		public String getValue() {
			return value;
		}

		public List<KVPageProcess> getKvPageProcesses() {
			return kvPageProcesses;
		}

		public void setKvPageProcesses(List<KVPageProcess> kvPageProcesses) {
			this.kvPageProcesses = kvPageProcesses;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			// result = prime * result + getOuterType().hashCode();
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			BatchPluginConfiguration other = (BatchPluginConfiguration) obj;
			/*
			 * if (!getOuterType().equals(other.getOuterType())) return false;
			 */
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}

		/*
		 * private BatchPluginPropertyContainer getOuterType() { return BatchPluginPropertyContainer.this; }
		 */
	}

	public class FunctionKey implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private long id;
		private com.ephesoft.dcma.da.domain.DocumentType docType;
		private String shortcutKeyname;
		private String methodName;
		private String uiLabel;
		private String identifier;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public com.ephesoft.dcma.da.domain.DocumentType getDocType() {
			return docType;
		}

		public void setDocType(com.ephesoft.dcma.da.domain.DocumentType docType) {
			this.docType = docType;
		}

		public String getShortcutKeyname() {
			return shortcutKeyname;
		}

		public void setShortcutKeyname(String shortcutKeyname) {
			this.shortcutKeyname = shortcutKeyname;
		}

		public String getMethodName() {
			return methodName;
		}

		public void setMethodName(String methodName) {
			this.methodName = methodName;
		}

		public String getUiLabel() {
			return uiLabel;
		}

		public void setUiLabel(String uiLabel) {
			this.uiLabel = uiLabel;
		}

		public String getIdentifier() {
			return identifier;
		}

		public void setIdentifier(String identifier) {
			this.identifier = identifier;
		}
	}

	public class BatchDynamicPluginConfiguration implements Serializable {

		private static final long serialVersionUID = 3L;
		private Long id;
		private String key;
		private String value;
		private String description;
		private BatchDynamicPluginConfiguration parent;

		private Set<BatchDynamicPluginConfiguration> children = new HashSet<BatchDynamicPluginConfiguration>();

		public BatchDynamicPluginConfiguration(Long id, String key, String value, String description) {
			this.id = id;
			this.key = key;
			this.value = value;
			this.description = description;
			allDynamicBatchConfiguration.put(id, this);
		}

		public BatchDynamicPluginConfiguration(Long id, String key, String value, String description,
				BatchClassDynamicPluginConfig parent) {
			this(id, key, value, description);

			if (parent != null) {
				this.parent = allDynamicBatchConfiguration.get(parent.getId());
				if (this.parent == null) {
					this.parent = new BatchDynamicPluginConfiguration(id, parent.getName(), parent.getValue(),
							parent.getDescription(), parent.getParent());
				}
				this.parent.addChild(this);
			}
		}

		public BatchDynamicPluginConfiguration(BatchClassDynamicPluginConfig batchClassDynamicPluginConfig) {
			this(batchClassDynamicPluginConfig.getId(), batchClassDynamicPluginConfig.getName(), batchClassDynamicPluginConfig
					.getValue(), batchClassDynamicPluginConfig.getDescription());

			if (batchClassDynamicPluginConfig.getChildren() != null) {
				for (BatchClassDynamicPluginConfig child : batchClassDynamicPluginConfig.getChildren()) {
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

		public Long getId() {
			return id;
		}

		public Set<BatchDynamicPluginConfiguration> getChildren() {
			return children;
		}

		public void addChild(BatchDynamicPluginConfiguration configuration) {
			children.add(configuration);
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

		public BatchDynamicPluginConfiguration getParent() {
			return parent;
		}

		public String getDescription() {
			return description;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			// result = prime * result + getOuterType().hashCode();
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			BatchDynamicPluginConfiguration other = (BatchDynamicPluginConfiguration) obj;
			/*
			 * if (!getOuterType().equals(other.getOuterType())) return false;
			 */
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}

		/*
		 * private BatchPluginPropertyContainer getOuterType() { return BatchPluginPropertyContainer.this; }
		 */
	}

	public class DocumentType implements Serializable {

		private static final long serialVersionUID = 4L;
		private long id;
		private String identifier;
		private Date creationDate;
		private Date lastModified;
		private String name;
		private String description;
		private String rspProjectFileName;
		private float minConfidenceThreshold;
		private String priority;
		private boolean isHidden;
		private BatchClass batchClass;
		private Map<String, PageType> docPageTypes = new HashMap<String, PageType>();
		private Map<String, FieldType> docFieldTypes = new HashMap<String, FieldType>();
		private Map<String, FunctionKey> docFunctionKeys = new HashMap<String, FunctionKey>();

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * @param description the description to set
		 */
		public void setDescription(String description) {
			this.description = description;
		}

		/**
		 * @return the minConfidenceThreshold
		 */
		public float getMinConfidenceThreshold() {
			return minConfidenceThreshold;
		}

		/**
		 * @param minConfidenceThreshold the minConfidenceThreshold to set
		 */
		public void setMinConfidenceThreshold(float minConfidenceThreshold) {
			this.minConfidenceThreshold = minConfidenceThreshold;
		}

		/**
		 * @return the priority
		 */
		public String getPriority() {
			return priority;
		}

		/**
		 * @param priority the priority to set
		 */
		public void setPriority(String priority) {
			this.priority = priority;
		}

		/**
		 * @return the docPageTypes
		 */
		public Map<String, PageType> getDocPageTypes() {
			return docPageTypes;
		}

		/**
		 * @param docPageTypes the docPageTypes to set
		 */
		public void setDocPageTypes(Map<String, PageType> docPageTypes) {
			this.docPageTypes = docPageTypes;
		}

		public Map<String, FunctionKey> getDocFunctionKeys() {
			return docFunctionKeys;
		}

		public void setDocFunctionKeys(Map<String, FunctionKey> docFunctionKeys) {
			this.docFunctionKeys = docFunctionKeys;
		}

		/**
		 * @return the docFieldTypes
		 */
		public Map<String, FieldType> getDocFieldTypes() {
			return docFieldTypes;
		}

		/**
		 * @param docFieldTypes the docFieldTypes to set
		 */
		public void setDocFieldTypes(Map<String, FieldType> docFieldTypes) {
			this.docFieldTypes = docFieldTypes;
		}

		/**
		 * @return the id
		 */
		public long getId() {
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(long id) {
			this.id = id;
		}

		/**
		 * @return the creationDate
		 */
		public Date getCreationDate() {
			return creationDate;
		}

		/**
		 * @param creationDate the creationDate to set
		 */
		public void setCreationDate(Date creationDate) {
			this.creationDate = creationDate;
		}

		/**
		 * @return the lastModified
		 */
		public Date getLastModified() {
			return lastModified;
		}

		/**
		 * @param lastModified the lastModified to set
		 */
		public void setLastModified(Date lastModified) {
			this.lastModified = lastModified;
		}
		
		/**
		 * 
		 * @return isHidden
		 */
		public boolean isHidden() {
			return isHidden;
		}

		/**
		 * 
		 * @param isHidden
		 */
		public void setHidden(boolean isHidden) {
			this.isHidden = isHidden;
		}


		/**
		 * @return the batchClass
		 */
		public BatchClass getBatchClass() {
			return batchClass;
		}

		/**
		 * @param batchClass the batchClass to set
		 */
		public void setBatchClass(BatchClass batchClass) {
			this.batchClass = batchClass;
		}

		/**
		 * @return the identifier
		 */
		public String getIdentifier() {
			return identifier;
		}

		/**
		 * @param identifier the identifier to set
		 */
		public void setIdentifier(String identifier) {
			this.identifier = identifier;
		}

		/**
		 * @return the rspProjectFileName
		 */
		public String getRspProjectFileName() {
			return rspProjectFileName;
		}

		/**
		 * @param rspProjectFileName the rspProjectFileName to set
		 */
		public void setRspProjectFileName(String rspProjectFileName) {
			this.rspProjectFileName = rspProjectFileName;
		}

	}

	public class PageType implements Serializable {

		private static final long serialVersionUID = 5L;
		private long id;
		private String identifier;
		private Date creationDate;
		private Date lastModified;
		private String name;
		private String description;
		private com.ephesoft.dcma.da.domain.DocumentType docType;

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * @param description the description to set
		 */
		public void setDescription(String description) {
			this.description = description;
		}

		/**
		 * @return the id
		 */
		public long getId() {
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(long id) {
			this.id = id;
		}

		/**
		 * @return the creationDate
		 */
		public Date getCreationDate() {
			return creationDate;
		}

		/**
		 * @param creationDate the creationDate to set
		 */
		public void setCreationDate(Date creationDate) {
			this.creationDate = creationDate;
		}

		/**
		 * @return the lastModified
		 */
		public Date getLastModified() {
			return lastModified;
		}

		/**
		 * @param lastModified the lastModified to set
		 */
		public void setLastModified(Date lastModified) {
			this.lastModified = lastModified;
		}

		/**
		 * @return the docType
		 */
		public com.ephesoft.dcma.da.domain.DocumentType getDocType() {
			return docType;
		}

		/**
		 * @param docType the docType to set
		 */
		public void setDocType(com.ephesoft.dcma.da.domain.DocumentType docType) {
			this.docType = docType;
		}

		/**
		 * @return the identifier
		 */
		public String getIdentifier() {
			return identifier;
		}

		/**
		 * @param identifier the identifier to set
		 */
		public void setIdentifier(String identifier) {
			this.identifier = identifier;
		}

	}

	public class FieldType implements Serializable {

		private static final long serialVersionUID = 6L;
		private long id;
		private String identifier;
		private Date creationDate;
		private Date lastModified;
		private String name;
		private int fieldOrderNumber;
		private String description;
		private String dataType;
		private String pattern;
		private String barcodeType;
		private String fieldOptionValueList;
		private boolean isHidden;
		private String sampleValue;
		private Map<String, RegexValidation> regexValidation = new HashMap<String, RegexValidation>();
		private com.ephesoft.dcma.da.domain.DocumentType docType;
		private Map<String, KVExtraction> fieldKVExtraction = new HashMap<String, KVExtraction>();

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * @param description the description to set
		 */
		public void setDescription(String description) {
			this.description = description;
		}

		/**
		 * @return the dataType
		 */
		public String getDataType() {
			return dataType;
		}

		/**
		 * @param dataType the dataType to set
		 */
		public void setDataType(String dataType) {
			this.dataType = dataType;
		}

		/**
		 * @return the pattern
		 */
		public String getPattern() {
			return pattern;
		}

		/**
		 * @param pattern the pattern to set
		 */
		public void setPattern(String pattern) {
			this.pattern = pattern;
		}

		/**
		 * @return the fieldKVExtraction
		 */
		public Map<String, KVExtraction> getFieldKVExtraction() {
			return fieldKVExtraction;
		}

		/**
		 * @param fieldKVExtraction the fieldKVExtraction to set
		 */
		public void setFieldKVExtraction(Map<String, KVExtraction> fieldKVExtraction) {
			this.fieldKVExtraction = fieldKVExtraction;
		}

		/**
		 * @return the id
		 */
		public long getId() {
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(long id) {
			this.id = id;
		}

		/**
		 * @return the creationDate
		 */
		public Date getCreationDate() {
			return creationDate;
		}

		/**
		 * @param creationDate the creationDate to set
		 */
		public void setCreationDate(Date creationDate) {
			this.creationDate = creationDate;
		}

		/**
		 * @return the lastModified
		 */
		public Date getLastModified() {
			return lastModified;
		}

		/**
		 * @param lastModified the lastModified to set
		 */
		public void setLastModified(Date lastModified) {
			this.lastModified = lastModified;
		}

		/**
		 * @return the docType
		 */
		public com.ephesoft.dcma.da.domain.DocumentType getDocType() {
			return docType;
		}

		/**
		 * @param docType the docType to set
		 */
		public void setDocType(com.ephesoft.dcma.da.domain.DocumentType docType) {
			this.docType = docType;
		}

		/**
		 * @return the identifier
		 */
		public String getIdentifier() {
			return identifier;
		}

		/**
		 * @param identifier the identifier to set
		 */
		public void setIdentifier(String identifier) {
			this.identifier = identifier;
		}

		/**
		 * @return the fieldOrderNumber
		 */
		public int getFieldOrderNumber() {
			return fieldOrderNumber;
		}

		/**
		 * @param fieldOrderNumber the fieldOrderNumber to set
		 */
		public void setFieldOrderNumber(int fieldOrderNumber) {
			this.fieldOrderNumber = fieldOrderNumber;
		}

		public String getBarcodeType() {
			return barcodeType;
		}

		public void setBarcodeType(String barcodeType) {
			this.barcodeType = barcodeType;
		}

		/**
		 * @return fieldOptionValueList
		 */
		public String getFieldOptionValueList() {
			return fieldOptionValueList;
		}

		/**
		 * @param fieldOptionValueList field Option Value List to set
		 */
		public void setFieldOptionValueList(String fieldOptionValueList) {
			this.fieldOptionValueList = fieldOptionValueList;
		}

		/**
		 * 
		 * @return isHidden
		 */
		public boolean isHidden() {
			return isHidden;
		}

		/**
		 * 
		 * @param isHidden
		 */
		public void setHidden(boolean isHidden) {
			this.isHidden = isHidden;
		}

		/**
		 * 
		 * @return regexValidation
		 */
		public Map<String, RegexValidation> getRegexValidation() {
			return regexValidation;
		}

		/**
		 * 
		 * @param regexValidation
		 */
		public void setRegexValidation(Map<String, RegexValidation> regexValidation) {
			this.regexValidation = regexValidation;
		}

		public String getSampleValue() {
			return sampleValue;
		}

		public void setSampleValue(String sampleValue) {
			this.sampleValue = sampleValue;
		}

	}

	public class KVExtraction implements Serializable {

		private static final long serialVersionUID = 7L;
		private long id;
		private Date creationDate;
		private Date lastModified;
		private String keyPattern;
		private String location;
		private String valuePattern;
		private com.ephesoft.dcma.da.domain.FieldType fieldType;

		/**
		 * @return the keyPattern
		 */
		public String getKeyPattern() {
			return keyPattern;
		}

		/**
		 * @param keyPattern the keyPattern to set
		 */
		public void setKeyPattern(String keyPattern) {
			this.keyPattern = keyPattern;
		}

		/**
		 * @return the location
		 */
		public String getLocation() {
			return location;
		}

		/**
		 * @param location the location to set
		 */
		public void setLocation(String location) {
			this.location = location;
		}

		/**
		 * @return the valuePattern
		 */
		public String getValuePattern() {
			return valuePattern;
		}

		/**
		 * @param valuePattern the valuePattern to set
		 */
		public void setValuePattern(String valuePattern) {
			this.valuePattern = valuePattern;
		}

		/**
		 * @return the id
		 */
		public long getId() {
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(long id) {
			this.id = id;
		}

		/**
		 * @return the creationDate
		 */
		public Date getCreationDate() {
			return creationDate;
		}

		/**
		 * @param creationDate the creationDate to set
		 */
		public void setCreationDate(Date creationDate) {
			this.creationDate = creationDate;
		}

		/**
		 * @return the lastModified
		 */
		public Date getLastModified() {
			return lastModified;
		}

		/**
		 * @param lastModified the lastModified to set
		 */
		public void setLastModified(Date lastModified) {
			this.lastModified = lastModified;
		}

		/**
		 * @return the fieldType
		 */
		public com.ephesoft.dcma.da.domain.FieldType getFieldType() {
			return fieldType;
		}

		/**
		 * @param fieldType the fieldType to set
		 */
		public void setFieldType(com.ephesoft.dcma.da.domain.FieldType fieldType) {
			this.fieldType = fieldType;
		}
	}

	public class RegexValidation implements Serializable {

		private static final long serialVersionUID = 1L;
		private long id;
		private Date creationDate;
		private Date lastModified;
		private String pattern;
		private com.ephesoft.dcma.da.domain.FieldType fieldType;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public Date getCreationDate() {
			return creationDate;
		}

		public void setCreationDate(Date creationDate) {
			this.creationDate = creationDate;
		}

		public Date getLastModified() {
			return lastModified;
		}

		public void setLastModified(Date lastModified) {
			this.lastModified = lastModified;
		}

		public String getPattern() {
			return pattern;
		}

		public void setPattern(String pattern) {
			this.pattern = pattern;
		}

		public com.ephesoft.dcma.da.domain.FieldType getFieldType() {
			return fieldType;
		}

		public void setFieldType(com.ephesoft.dcma.da.domain.FieldType fieldType) {
			this.fieldType = fieldType;
		}

	}
}
