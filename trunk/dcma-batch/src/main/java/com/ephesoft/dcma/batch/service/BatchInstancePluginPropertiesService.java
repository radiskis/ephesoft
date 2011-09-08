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

package com.ephesoft.dcma.batch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ephesoft.dcma.batch.dao.PluginPropertiesDao;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchDynamicPluginConfiguration;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPlugin;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPluginConfiguration;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.DocumentType;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.PageType;
import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.core.common.LocationType;
import com.ephesoft.dcma.core.common.PluginProperty;
import com.ephesoft.dcma.da.domain.FieldType;
import com.ephesoft.dcma.da.domain.FunctionKey;
import com.ephesoft.dcma.da.domain.KVExtraction;
import com.ephesoft.dcma.da.domain.RegexValidation;

@Service("batchInstancePluginPropertiesService")
public class BatchInstancePluginPropertiesService implements PluginPropertiesService {

	@Autowired
	@Qualifier("batchInstancePluginPropertiesDao")
	private PluginPropertiesDao pluginPropertiesDao;

	@Transactional
	@Override
	@Deprecated
	public String getPropertyValue(String batchIdentifier, String pluginName, PluginProperty pluginProperty) {
		BatchPluginPropertyContainer container = pluginPropertiesDao.getPluginProperties(batchIdentifier);

		final List<BatchPluginConfiguration> pluginConfiguration = container.getPlginConfiguration(pluginName, pluginProperty);

		if (null == pluginConfiguration || pluginConfiguration.isEmpty()) {
			return null;
		}

		return pluginConfiguration.get(0).getValue();
	}

	@Override
	public void clearCache(String batchIdentifier) {
		pluginPropertiesDao.clearPluginProperties(batchIdentifier);
	}

	@Transactional
	@Override
	public BatchPluginPropertyContainer getPluginProperties(String batchIdentifier) {
		return pluginPropertiesDao.getPluginProperties(batchIdentifier);
	}

	@Transactional
	@Override
	public BatchPlugin getPluginProperties(String batchIdentifier, String pluginName) {
		return pluginPropertiesDao.getPluginProperties(batchIdentifier).getPlugin(pluginName);
	}

	@Transactional
	@Override
	public BatchPluginConfiguration[] getPluginProperties(String batchIdentifier, String pluginName, PluginProperty pluginProperty) {
		List<BatchPluginConfiguration> configurations = pluginPropertiesDao.getPluginProperties(batchIdentifier).getPlugin(pluginName)
				.getPluginConfigurations(pluginProperty);
		if (configurations == null) {
			configurations = new ArrayList<BatchPluginConfiguration>(0);
		}
		return configurations.toArray(new BatchPluginPropertyContainer.BatchPluginConfiguration[configurations.size()]);
	}

	@Transactional
	@Override
	public BatchDynamicPluginConfiguration[] getDynamicPluginProperties(String batchIdentifier, String pluginName,
			PluginProperty pluginProperty) {
		List<BatchDynamicPluginConfiguration> configurations = pluginPropertiesDao.getPluginProperties(batchIdentifier).getPlugin(
				pluginName).getDynamicPluginConfigurations(pluginProperty);
		if (configurations == null) {
			configurations = new ArrayList<BatchDynamicPluginConfiguration>(0);
		}
		return configurations.toArray(new BatchPluginPropertyContainer.BatchDynamicPluginConfiguration[configurations.size()]);
	}

	@Override
	public List<com.ephesoft.dcma.da.domain.DocumentType> getDocumentTypes(String batchIdentifier) {
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
					returnList.add(tempDocType);
				}
			}
		}
		return returnList;
	}

	@Override
	public List<com.ephesoft.dcma.da.domain.DocumentType> getDocumentTypeByName(String batchIdentifier, String docTypeName) {
		BatchPluginPropertyContainer container = getPluginProperties(batchIdentifier);
		List<com.ephesoft.dcma.da.domain.DocumentType> returnList = null;
		if (container != null) {
			Map<String, DocumentType> allDocTypes = container.getAllDocumentTypes();
			if (allDocTypes != null && !allDocTypes.isEmpty()) {
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
				returnList.add(tempDocType);
			}
		}
		return returnList;
	}

	@Override
	public List<com.ephesoft.dcma.da.domain.FieldType> getFieldTypes(String batchIdentifier, String docTypeName) {
		BatchPluginPropertyContainer container = getPluginProperties(batchIdentifier);
		List<com.ephesoft.dcma.da.domain.FieldType> returnList = null;
		if (container != null) {
			Map<String, DocumentType> allDocTypes = container.getAllDocumentTypes();
			if (allDocTypes != null && !allDocTypes.isEmpty()) {
				returnList = new ArrayList<com.ephesoft.dcma.da.domain.FieldType>();
				DocumentType serializedDocType = allDocTypes.get(docTypeName);
				if (serializedDocType != null) {
					Map<String, com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.FieldType> allFieldTypes = serializedDocType
							.getDocFieldTypes();
					if (allFieldTypes != null && !allFieldTypes.isEmpty()) {
						Set<String> tempFieldTypes = allFieldTypes.keySet();
						if (tempFieldTypes != null && !tempFieldTypes.isEmpty()) {
							for (String string : tempFieldTypes) {
								com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.FieldType eachFieldType = allFieldTypes
										.get(string);
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
									localFieldType.setSampleValue(eachFieldType.getSampleValue());
									List<RegexValidation> finalExtractionList = new ArrayList<RegexValidation>();
									Map<String, com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.RegexValidation> regexMap = eachFieldType
											.getRegexValidation();
									if (regexMap != null && !regexMap.isEmpty()) {
										Set<String> regexSet = regexMap.keySet();
										if (regexSet != null && !regexSet.isEmpty()) {
											for (String string2 : regexSet) {
												com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.RegexValidation localRegexVal = regexMap
														.get(string2);
												if (localRegexVal != null) {
													RegexValidation regexValidation = new RegexValidation();
													regexValidation.setId(localRegexVal.getId());
													regexValidation.setCreationDate(localRegexVal.getCreationDate());
													regexValidation.setPattern(localRegexVal.getPattern());
													regexValidation.setLastModified(localRegexVal.getLastModified());
													regexValidation.setFieldType(localRegexVal.getFieldType());
													finalExtractionList.add(regexValidation);
												}
											}
										}
									}
									localFieldType.setRegexValidation(finalExtractionList);
									returnList.add(localFieldType);
								}
							}
						}
					}
				}
			}
		}
		return returnList;
	}

	@Override
	public List<com.ephesoft.dcma.da.domain.FieldType> getFieldTypeAndKVExtractions(String batchIdentifier, String docTypeName) {
		BatchPluginPropertyContainer container = getPluginProperties(batchIdentifier);
		List<com.ephesoft.dcma.da.domain.FieldType> returnList = null;
		if (container != null) {
			Map<String, DocumentType> allDocTypes = container.getAllDocumentTypes();
			if (allDocTypes != null && !allDocTypes.isEmpty()) {
				returnList = new ArrayList<com.ephesoft.dcma.da.domain.FieldType>();
				DocumentType serializedDocType = allDocTypes.get(docTypeName);
				Map<String, com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.FieldType> allFieldTypes = serializedDocType
						.getDocFieldTypes();
				if (allFieldTypes != null && !allFieldTypes.isEmpty()) {
					Set<String> tempFieldTypes = allFieldTypes.keySet();
					if (tempFieldTypes != null && !tempFieldTypes.isEmpty()) {
						for (String string : tempFieldTypes) {
							com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.FieldType eachFieldType = allFieldTypes
									.get(string);
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
								// populating KV extraction
								List<KVExtraction> finalExtractionList = new ArrayList<KVExtraction>();
								Map<String, com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.KVExtraction> kvExtractionMap = eachFieldType
										.getFieldKVExtraction();
								if (kvExtractionMap != null && !kvExtractionMap.isEmpty()) {
									Set<String> kvSet = kvExtractionMap.keySet();
									if (kvSet != null && !kvSet.isEmpty()) {
										for (String string2 : kvSet) {
											com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.KVExtraction localKvExtrac = kvExtractionMap
													.get(string2);
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
									}
								}
								localFieldType.setKvExtraction(finalExtractionList);
								returnList.add(localFieldType);
							}
						}
					}
				}
			}
		}
		return returnList;
	}

	@Override
	public List<com.ephesoft.dcma.da.domain.PageType> getPageTypes(String batchIdentifier) {
		BatchPluginPropertyContainer container = getPluginProperties(batchIdentifier);
		List<com.ephesoft.dcma.da.domain.PageType> returnList = null;
		if (container != null) {
			Map<String, DocumentType> allDocTypes = container.getAllDocumentTypes();
			if (allDocTypes != null && !allDocTypes.isEmpty()) {
				returnList = new ArrayList<com.ephesoft.dcma.da.domain.PageType>();
				Set<String> allkeysSet = allDocTypes.keySet();
				if (allkeysSet != null && !allkeysSet.isEmpty()) {
					for (String eachKey : allkeysSet) {
						DocumentType docType = allDocTypes.get(eachKey);
						if (docType != null) {
							Map<String, PageType> allPageTypes = docType.getDocPageTypes();
							if (allPageTypes != null && !allPageTypes.isEmpty()) {
								Set<String> allTempKeySet = allPageTypes.keySet();
								if (allTempKeySet != null) {
									for (String eachTempKey : allTempKeySet) {
										PageType eachPageType = allPageTypes.get(eachTempKey);
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
									}
								}
							}
						}
					}
				}
			}
		}
		return returnList;
	}

	@Override
	public List<com.ephesoft.dcma.da.domain.DocumentType> getDocTypeByPageTypeName(String batchIdentifier, String pageTypeName) {
		BatchPluginPropertyContainer container = getPluginProperties(batchIdentifier);
		List<com.ephesoft.dcma.da.domain.DocumentType> returnList = null;
		if (container != null) {
			Map<String, DocumentType> allDocTypes = container.getAllDocumentTypes();
			if (allDocTypes != null && !allDocTypes.isEmpty()) {
				returnList = new ArrayList<com.ephesoft.dcma.da.domain.DocumentType>();
				Set<String> allkeysSet = allDocTypes.keySet();
				if (allkeysSet != null && !allkeysSet.isEmpty()) {
					for (String eachKey : allkeysSet) {
						DocumentType docType = allDocTypes.get(eachKey);
						if (docType != null) {
							Map<String, PageType> allPageTypes = docType.getDocPageTypes();
							if (allPageTypes != null && !allPageTypes.isEmpty()) {
								Set<String> allTempKeySet = allPageTypes.keySet();
								if (allTempKeySet != null && allTempKeySet.contains(pageTypeName)) {
									com.ephesoft.dcma.da.domain.DocumentType tempDocType = new com.ephesoft.dcma.da.domain.DocumentType();
									tempDocType.setId(docType.getId());
									tempDocType.setIdentifier(tempDocType.getIdentifier());
									tempDocType.setName(docType.getName());
									tempDocType.setDescription(docType.getDescription());
									tempDocType.setRspProjectFileName(docType.getRspProjectFileName());
									tempDocType.setBatchClass(docType.getBatchClass());
									tempDocType.setCreationDate(docType.getCreationDate());
									tempDocType.setLastModified(docType.getLastModified());
									tempDocType.setMinConfidenceThreshold(docType.getMinConfidenceThreshold());
									tempDocType.setPriority(Integer.valueOf(docType.getPriority()));
									returnList.add(tempDocType);
								}
							}
						}
					}
				}
			}
		}
		return returnList;
	}

	@Override
	public List<FunctionKey> getFunctionKeys(String batchIdentifier, String docTypeName) {
		BatchPluginPropertyContainer container = getPluginProperties(batchIdentifier);
		List<com.ephesoft.dcma.da.domain.FunctionKey> returnList = null;
		if (container != null) {
			Map<String, DocumentType> allDocTypes = container.getAllDocumentTypes();
			if (allDocTypes != null && !allDocTypes.isEmpty()) {
				returnList = new ArrayList<com.ephesoft.dcma.da.domain.FunctionKey>();
				DocumentType serializedDocType = allDocTypes.get(docTypeName);
				if (serializedDocType != null) {
					Map<String, com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.FunctionKey> allFunctionKeys = serializedDocType
							.getDocFunctionKeys();
					if (allFunctionKeys != null && !allFunctionKeys.isEmpty()) {
						Set<String> tempFunctionKeys = allFunctionKeys.keySet();
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
					}
				}
			}
		}

		return returnList;
	}

}
