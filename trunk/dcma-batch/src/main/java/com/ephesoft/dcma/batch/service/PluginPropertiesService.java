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

import java.util.List;

import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchDynamicPluginConfiguration;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPlugin;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPluginConfiguration;
import com.ephesoft.dcma.core.common.PluginProperty;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.FieldType;
import com.ephesoft.dcma.da.domain.FunctionKey;
import com.ephesoft.dcma.da.domain.PageType;

/**
 * 
 * This service provides APIs for retrieving plug-in properties , documents, fields and pages for a batch.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer
 * @see com.ephesoft.dcma.core.common.PluginProperty
 */
public interface PluginPropertiesService {

	/**
	 * This method returns the plug-in properties of a batch.
	 * 
	 * @param batchIdentifier {@link String}
	 * @return {@link BatchPluginPropertyContainer}
	 */
	BatchPluginPropertyContainer getPluginProperties(String batchIdentifier);

	/**
	 * This method returns the plug-in properties of a batch for a particular plugin.
	 * 
	 * @param batchIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @return {@link BatchPlugin}
	 */
	BatchPlugin getPluginProperties(String batchIdentifier, String pluginName);

	/**
	 * This method returns the plug-in property configuration of a batch for a particular plug-in property.
	 * 
	 * @param batchIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @param pluginProperty {@link PluginProperty}
	 * @return {@link BatchPluginConfiguration}
	 */
	BatchPluginConfiguration[] getPluginProperties(String batchIdentifier, String pluginName, PluginProperty pluginProperty);

	/**
	 * This method returns the dynamic plug-in properties of a batch for a particular plug-in.
	 * 
	 * @param batchIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @param pluginProperty {@link PluginProperty}
	 * @return {@link BatchDynamicPluginConfiguration}
	 */
	BatchDynamicPluginConfiguration[] getDynamicPluginProperties(String batchIdentifier, String pluginName,
			PluginProperty pluginProperty);

	/**
	 * This method returns the plug-in property value of a batch for a particular plug-in property.
	 * 
	 * @param batchIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @param pluginProperty {@link PluginProperty}
	 * @return {@link String}
	 */
	String getPropertyValue(String batchIdentifier, String pluginName, PluginProperty pluginProperty);

	/**
	 * This method clears the cache for a particular batch.
	 * 
	 * @param batchIdentifier {@link String}
	 */
	void clearCache(String batchIdentifier);

	/**
	 * This method returns the document types for a particular batch.
	 * 
	 * @param batchIdentifier {@link String}
	 * @return {@link DocumentType}
	 */
	List<DocumentType> getDocumentTypes(String batchIdentifier);

	/**
	 * This method returns the document types by name for a particular batch.
	 * 
	 * @param batchIdentifier {@link String}
	 * @param docTypeName {@link String}
	 * @return {@link DocumentType}
	 */
	List<DocumentType> getDocumentTypeByName(String batchIdentifier, String docTypeName);

	/**
	 * This method returns the page types for a particular batch.
	 * 
	 * @param batchIdentifier {@link String}
	 * @return {@link PageType}
	 */
	List<PageType> getPageTypes(String batchIdentifier);

	/**
	 * This method returns the document types by page type name for a particular batch.
	 * 
	 * @param batchIdentifier {@link String}
	 * @param pageTypeName {@link String}
	 * @return {@link DocumentType}
	 */
	List<DocumentType> getDocTypeByPageTypeName(String batchIdentifier, String pageTypeName);

	/**
	 * This method returns the field types of a batch for a document.
	 * 
	 * @param batchIdentifier {@link String}
	 * @param docTypeName {@link String}
	 * @return {@link FieldType}
	 */
	List<FieldType> getFieldTypes(String batchIdentifier, String docTypeName);

	/**
	 * This method returns the field types with KVExtraction of a batch for a document.
	 * 
	 * @param batchIdentifier {@link String}
	 * @param docTypeName {@link String}
	 * @return {@link FieldType}
	 */
	List<FieldType> getFieldTypeAndKVExtractions(String batchIdentifier, String docTypeName);

	/**
	 * This method returns the function keys of a batch for a document.
	 * 
	 * @param batchIdentifier {@link String}
	 * @param docTypeName {@link String}
	 * @return {@link FunctionKey}
	 */
	List<FunctionKey> getFunctionKeys(String batchIdentifier, String docTypeName);

}
