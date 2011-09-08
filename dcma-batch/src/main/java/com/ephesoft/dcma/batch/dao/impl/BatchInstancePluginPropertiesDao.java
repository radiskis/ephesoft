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

import com.ephesoft.dcma.batch.dao.PluginPropertiesDao;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.da.dao.BatchClassDynamicPluginConfigDao;
import com.ephesoft.dcma.da.dao.BatchClassPluginConfigDao;
import com.ephesoft.dcma.da.domain.BatchClassDynamicPluginConfig;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.KVPageProcess;
import com.ephesoft.dcma.da.service.DocumentTypeService;
import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.KeyGenerator;
import com.googlecode.ehcache.annotations.Property;
import com.googlecode.ehcache.annotations.TriggersRemove;

@Repository("batchInstancePluginPropertiesDao")
public class BatchInstancePluginPropertiesDao implements PluginPropertiesDao {

	public static final String EXTN = "ser";
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private BatchSchemaService batchSchemaService;

	@Autowired
	private DocumentTypeService documentTypeService;

	@Autowired
	private BatchClassPluginConfigDao batchClassPluginConfigDao;

	@Autowired
	private BatchClassDynamicPluginConfigDao batchClassDynamicPluginConfigDao;

	@Cacheable(cacheName = "pluginPropertiesCache", keyGenerator = @KeyGenerator(name = "ListCacheKeyGenerator", properties = {
			@Property(name = "useReflection", value = "true"), @Property(name = "checkforCycles", value = "true"),
			@Property(name = "includeMethod", value = "false")}))
	public BatchPluginPropertyContainer getPluginProperties(String batchInstanceIdentifier) {
		BatchPluginPropertyContainer container = null;
		String localFolderLocation = batchSchemaService.getLocalFolderLocation();

		boolean isContainerCreated = false;
		String pathToPropertiesFolder = localFolderLocation + File.separator + "properties";
		File file = new File(pathToPropertiesFolder);
		if (!file.exists()) {
			file.mkdir();
			log.info(pathToPropertiesFolder + " folder created");
		}

		File serializedFile = new File(pathToPropertiesFolder + File.separator + batchInstanceIdentifier + '.' + EXTN);
		if (serializedFile.exists()) {
			FileInputStream fileInputStream = null;
			try {
				fileInputStream = new FileInputStream(serializedFile);
				container = (BatchPluginPropertyContainer) SerializationUtils.deserialize(fileInputStream);
				isContainerCreated = true;
			} catch (Exception e) {
				log.error("Error during de-serializing the properties for Batch instance: " + batchInstanceIdentifier);
				isContainerCreated = false;
			} finally {
				try {
					if (fileInputStream != null) {
						fileInputStream.close();
					}
				} catch (IOException e) {
					log.error("Problem closing stream for file :" + serializedFile.getName());
				}
			}
		}

		if (!isContainerCreated) {
			List<BatchClassPluginConfig> batchClassPluginConfigs = batchClassPluginConfigDao
					.getAllPluginPropertiesForBatchInstance(batchInstanceIdentifier);
			// to lazily load KVPageProcess objects
			for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
				for (KVPageProcess eachKvPageProcess : batchClassPluginConfig.getKvPageProcesses()) {
					if (log.isDebugEnabled() && eachKvPageProcess != null) {
						log.debug(eachKvPageProcess.getKeyPattern());
					}
				}
			}

			List<BatchClassDynamicPluginConfig> batchClassDynamicPluginConfigs = batchClassDynamicPluginConfigDao
					.getAllDynamicPluginPropertiesForBatchInstance(batchInstanceIdentifier);
			container = new BatchPluginPropertyContainer(String.valueOf(batchInstanceIdentifier));
			container.populate(batchClassPluginConfigs);
			container.populateDynamicPluginConfigs(batchClassDynamicPluginConfigs);
			List<DocumentType> documentTypes = documentTypeService.getDocTypeByBatchInstanceIdentifier(batchInstanceIdentifier);
			container.populateDocumentTypes(documentTypes, batchInstanceIdentifier);
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(serializedFile);
				SerializationUtils.serialize(container, fileOutputStream);
			} catch (Exception e) {
				log.error("Error during serializing the properties for Batch instance: " + batchInstanceIdentifier);
			} finally {
				try {
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
				} catch (IOException e) {
					log.error("Problem closing stream for file :" + serializedFile.getName());
				}
			}
		}
		return container;
	}

	@TriggersRemove(cacheName = "pluginPropertiesCache", keyGenerator = @KeyGenerator(name = "ListCacheKeyGenerator", properties = {
			@Property(name = "useReflection", value = "true"), @Property(name = "checkforCycles", value = "true"),
			@Property(name = "includeMethod", value = "false")}))
	public void clearPluginProperties(String batchInstanceIdentifier) {
		log.debug("Cleared the pluginPropertiesCache for batch Instance: " + batchInstanceIdentifier);
	}
}
