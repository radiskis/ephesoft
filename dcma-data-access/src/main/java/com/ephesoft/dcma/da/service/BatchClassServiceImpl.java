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

package com.ephesoft.dcma.da.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.exception.BatchAlreadyLockedException;
import com.ephesoft.dcma.da.common.DeleteBatchClassEvent;
import com.ephesoft.dcma.da.common.NewBatchClassEvent;
import com.ephesoft.dcma.da.dao.BatchClassDao;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassDynamicPluginConfig;
import com.ephesoft.dcma.da.domain.BatchClassEmailConfiguration;
import com.ephesoft.dcma.da.domain.BatchClassField;
import com.ephesoft.dcma.da.domain.BatchClassGroups;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchClassModuleConfig;
import com.ephesoft.dcma.da.domain.BatchClassPlugin;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.FieldType;
import com.ephesoft.dcma.da.domain.FunctionKey;
import com.ephesoft.dcma.da.domain.KVExtraction;
import com.ephesoft.dcma.da.domain.KVPageProcess;
import com.ephesoft.dcma.da.domain.ModuleConfig;
import com.ephesoft.dcma.da.domain.PageType;
import com.ephesoft.dcma.da.domain.RegexValidation;
import com.ephesoft.dcma.da.domain.TableColumnsInfo;
import com.ephesoft.dcma.da.domain.TableInfo;
import com.ephesoft.dcma.da.id.BatchClassID;

/**
 * This is a database service to read data required by Batch Class Service.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.da.service.BatchClassService
 */
@Service
public class BatchClassServiceImpl implements BatchClassService, ApplicationContextAware {

	/**
	 * LOGGER to print the LOGGERging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchClassServiceImpl.class);

	@Autowired
	private BatchClassDao batchClassDao;

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * An api to fetch BatchClass by id.
	 * 
	 * @param identifier Serializable
	 * @return BatchClass
	 */
	@Transactional(readOnly = true)
	@Override
	public BatchClass get(Serializable identifier) {
		return batchClassDao.getBatchClassByIdentifier((String) identifier);
	}

	/**
	 * An api to update the batch class.
	 * 
	 * @param batchClass BatchClass
	 */
	@Transactional
	@Override
	public void saveOrUpdate(BatchClass batchClass) {
		if (null == batchClass) {
			LOGGER.info("batch class is null.");
		} else {
			batchClassDao.saveOrUpdate(batchClass);
		}
	}

	/**
	 * An api to get batch class by unc folder name.
	 * 
	 * @param folderName String
	 */
	@Transactional(readOnly = true)
	@Override
	public BatchClass getBatchClassbyUncFolder(String folderName) {
		BatchClass batchClass = null;
		if (null == folderName || "".equals(folderName)) {
			LOGGER.info("folderName is null or empty.");
		} else {
			batchClass = batchClassDao.getBatchClassbyUncFolder(folderName);
		}
		return batchClass;
	}

	/**
	 * An api to fetch BatchClass by batch Class name.
	 * 
	 * @param batchClassName String
	 * @return List<BatchClass>
	 */
	@Transactional(readOnly = true)
	@Override
	public List<BatchClass> getBatchClassbyName(String batchClassName) {
		List<BatchClass> batchClassList = null;
		if (null == batchClassName || "".equals(batchClassName)) {
			LOGGER.info("batchClassName is null or empty.");
		} else {
			batchClassList = batchClassDao.getBatchClassbyName(batchClassName);
		}
		return batchClassList;
	}

	/**
	 * An api to fetch BatchClass by batch Class processName.
	 * 
	 * @param processName String
	 * @return List<BatchClass>
	 */
	@Transactional(readOnly = true)
	@Override
	public List<BatchClass> getBatchClassbyProcessName(String processName) {
		List<BatchClass> batchClassList = null;
		if (null == processName || "".equals(processName)) {
			LOGGER.info("processName is null or empty.");
		} else {
			batchClassList = batchClassDao.getBatchClassbyName(processName);
		}
		return batchClassList;
	}

	@Override
	public List<BatchClass> getAllUnlockedBatchClasses() {
		List<BatchClass> batchClassList = null;
		batchClassList = batchClassDao.getAllUnlockedBatchClasses();
		return batchClassList;
	}

	@Override
	public List<BatchClass> getAllBatchClasses() {
		List<BatchClass> batchClassList = null;
		batchClassList = batchClassDao.getAllBatchClasses();
		return batchClassList;
	}

	@Override
	@Transactional(readOnly = true)
	public BatchClass getBatchClassByIdentifier(String batchClassIdentifier) {
		BatchClass batchClass = null;
		batchClass = batchClassDao.getBatchClassByIdentifier(batchClassIdentifier);
		return batchClass;
	}

	@Override
	@Transactional(readOnly = true)
	public BatchClass getLoadedBatchClassByIdentifier(String batchClassIdentifier) {
		BatchClass batchClass = null;
		batchClass = batchClassDao.getBatchClassByIdentifier(batchClassIdentifier);
		loadBatchClass(batchClass);
		return batchClass;
	}

	@Override
	@Transactional(readOnly = true)
	public BatchClass getLoadedBatchClassByUNC(String folderName) {
		BatchClass batchClass = null;
		batchClass = batchClassDao.getBatchClassbyUncFolder(folderName);
		loadBatchClass(batchClass);
		return batchClass;
	}

	@Override
	@Transactional(readOnly = false)
	public synchronized BatchClass acquireBatchClass(String batchClassIdentifier, String currentUser)
			throws BatchAlreadyLockedException {
		BatchClass batchClass = null;
		if (null != batchClassIdentifier) {
			batchClass = getBatchClassByIdentifier(batchClassIdentifier);
			if (!currentUser.equals(batchClass.getCurrentUser()) && batchClass.getCurrentUser() != null) {
				throw new BatchAlreadyLockedException("Batch Class " + batchClass + " is already locked by "
						+ batchClass.getCurrentUser());
			} else {
				// Updating the state of the batch to locked and saving in the
				// database.
				LOGGER.info(currentUser + " is getting lock on batch " + batchClassIdentifier);
				if (!currentUser.trim().isEmpty()) {
					batchClass.setCurrentUser(currentUser);
				}
				batchClassDao.updateBatchClass(batchClass);
			}
		} else {
			LOGGER.warn("batchClassID id is null.");
		}
		return batchClass;
	}

	@Transactional
	@Override
	public void unlockAllBatchClassesForCurrentUser(String currentUser) {
		if (currentUser != null && !currentUser.isEmpty()) {
			List<BatchClass> batchClassList = batchClassDao.getAllBatchClassesForCurrentUser(currentUser);
			if (batchClassList != null && !batchClassList.isEmpty()) {
				for (BatchClass batchClass : batchClassList) {
					LOGGER.info("Unlocking batches for " + currentUser);
					batchClass.setCurrentUser(null);
					batchClassDao.updateBatchClass(batchClass);
				}
			} else {
				LOGGER.warn("No batches exist for the username " + currentUser);
			}
		} else {
			LOGGER.warn("Username not specified or is Null. Returning.");
		}
	}

	@Transactional
	@Override
	public void unlockCurrentBatchClass(String batchClassIdentifier) {
		BatchClass batchClass = null;
		if (null == batchClassIdentifier) {
			LOGGER.info("batchClassIdentifier is null.");
			return;
		}
		batchClass = getBatchClassByIdentifier(batchClassIdentifier);
		batchClass.setCurrentUser(null);
		batchClassDao.updateBatchClass(batchClass);
	}

	@Transactional
	@Override
	public BatchClass merge(BatchClass batchClass, boolean isBatchClassDeleted) {
		if (isBatchClassDeleted) {
			applicationContext
					.publishEvent(new DeleteBatchClassEvent(applicationContext, new BatchClassID(batchClass.getIdentifier())));
		}
		return batchClassDao.merge(batchClass);
	}

	@Transactional
	@Override
	public void evict(BatchClass batchClass) {
		batchClassDao.evict(batchClass);
	}

	@Override
	public List<BatchClass> getBatchClassList(int firstResult, int maxResults, Order order) {
		List<BatchClass> batches = null;
		List<Order> orderList = new ArrayList<Order>();
		if (order != null) {
			orderList.add(order);
		}
		batches = batchClassDao.getBatchClassList(firstResult, maxResults, orderList);
		return batches;
	}

	@Override
	public int countAllBatchClassesExcludeDeleted() {
		return batchClassDao.getAllBatchClassCountExcludeDeleted();
	}

	@Override
	@Transactional
	public void delete(Serializable identifier) {
		BatchClass object = batchClassDao.get(identifier);
		batchClassDao.remove(object);
	}

	@Override
	@Transactional
	public BatchClass createBatchClass(BatchClass batchClass) {
		BatchClass batchClass1 = null;
		batchClass1 = batchClassDao.merge(batchClass);
		applicationContext.publishEvent(new NewBatchClassEvent(applicationContext, new BatchClassID(batchClass1.getIdentifier())));
		return batchClass1;
	}

	@Override
	public List<BatchClass> getAllBatchClassesByUserRoles(Set<String> userRoles) {
		List<BatchClass> batchClassList = new ArrayList<BatchClass>();
		batchClassList = batchClassDao.getAllBatchClassesByUserRoles(userRoles);
		return batchClassList;
	}

	@Override
	public List<BatchClass> getAllBatchClassesExcludeDeleted() {
		List<BatchClass> batchClassList = null;
		batchClassList = batchClassDao.getAllBatchClassExcludeDeleted();
		return batchClassList;
	}

	@Override
	@Transactional
	public List<BatchClass> getAllLoadedBatchClassExcludeDeleted() {
		List<BatchClass> batchClassList = null;
		batchClassList = batchClassDao.getAllLoadedBatchClassExcludeDeleted();
		return batchClassList;
	}

	@Override
	@Transactional
	public BatchClass merge(BatchClass batchClass) {
		return batchClassDao.merge(batchClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.dcma.da.service.BatchClassService#getAssociatedUNCList(java.lang.String)
	 */
	@Override
	@Transactional
	public List<String> getAssociatedUNCList(String workflowName) {
		List<String> uncFolderList = null;
		uncFolderList = batchClassDao.getAllAssociatedUNCFolders(workflowName);
		return uncFolderList;
	}

	@Override
	@Transactional(readOnly = true)
	public BatchClass getLoadedBatchClassForImport(String batchClassIdentifier) {
		BatchClass batchClass = null;
		batchClass = batchClassDao.getBatchClassByIdentifier(batchClassIdentifier);
		if (batchClass != null) {
			for (BatchClassModule mod : batchClass.getBatchClassModules()) {
				for (BatchClassModuleConfig importConfig : mod.getBatchClassModuleConfig()) {
					if (LOGGER.isDebugEnabled() && importConfig != null) {
						LOGGER.debug(importConfig.toString());
					}
				}
			}
		}

		return batchClass;
	}

	private void loadBatchClass(BatchClass batchClass) {
		for (BatchClassEmailConfiguration emails : batchClass.getEmailConfigurations()) {
			if (LOGGER.isDebugEnabled() && emails != null) {
				LOGGER.debug(emails.getFolderName());
			}
		}

		List<BatchClassField> batchClassFields = batchClass.getBatchClassField();
		for (BatchClassField batchClassField : batchClassFields) {
			if (LOGGER.isDebugEnabled() && batchClassField != null) {
				LOGGER.debug(batchClassField.getName());
			}
		}

		for (DocumentType documentType : batchClass.getDocumentTypes()) {
			if (LOGGER.isDebugEnabled() && documentType != null) {
				LOGGER.debug(documentType.getName());
				for (PageType pageType : documentType.getPages()) {
					if (LOGGER.isDebugEnabled() && pageType != null) {
						LOGGER.debug(pageType.getName());
					}
				}
				for (FieldType fieldType : documentType.getFieldTypes()) {
					if (LOGGER.isDebugEnabled() && fieldType != null) {
						LOGGER.debug(fieldType.getName());
						for (KVExtraction kvExtraction : fieldType.getKvExtraction()) {
							if (LOGGER.isDebugEnabled() && kvExtraction != null) {
								LOGGER.debug(kvExtraction.getKeyPattern());
							}
						}
						for (RegexValidation regexValidation : fieldType.getRegexValidation()) {
							if (LOGGER.isDebugEnabled() && regexValidation != null) {
								LOGGER.debug(regexValidation.getPattern());
							}
						}
					}
				}
				for (TableInfo tableInfo : documentType.getTableInfos()) {
					if (LOGGER.isDebugEnabled() && tableInfo != null) {
						LOGGER.debug(tableInfo.getName());
						for (TableColumnsInfo tableColumnsInfo : tableInfo.getTableColumnsInfo()) {
							if (LOGGER.isDebugEnabled() && tableColumnsInfo != null) {
								LOGGER.debug(tableColumnsInfo.getColumnName());
							}
						}
					}
				}
				for (FunctionKey functionKey : documentType.getFunctionKeys()) {
					if (LOGGER.isDebugEnabled() && functionKey != null) {
						LOGGER.debug(functionKey.getShortcutKeyname());
					}
				}
			}
		}

		for (BatchClassGroups roles : batchClass.getAssignedGroups()) {
			if (LOGGER.isDebugEnabled() && roles != null) {
				LOGGER.debug(roles.getGroupName());
			}
		}
		for (BatchClassModule mod : batchClass.getBatchClassModules()) {
			List<BatchClassModuleConfig> modConfigs = mod.getBatchClassModuleConfig();

			for (BatchClassModuleConfig BCModConfig : modConfigs) {
				ModuleConfig moduleConfig = BCModConfig.getModuleConfig();
				if (LOGGER.isDebugEnabled() && moduleConfig != null) {
					LOGGER.debug(moduleConfig.getChildDisplayName());
				}
			}

			List<BatchClassPlugin> plugins = mod.getBatchClassPlugins();
			for (BatchClassPlugin plugin : plugins) {
				List<BatchClassPluginConfig> pluginConfigs = plugin.getBatchClassPluginConfigs();
				List<BatchClassDynamicPluginConfig> dynamicPluginConfigs = plugin.getBatchClassDynamicPluginConfigs();

				for (BatchClassPluginConfig conf : pluginConfigs) {
					List<KVPageProcess> kvs = conf.getKvPageProcesses();
					for (KVPageProcess kv : kvs) {
						if (LOGGER.isDebugEnabled() && kv != null) {
							LOGGER.debug(kv.getKeyPattern());
						}
					}
				}
				for (BatchClassDynamicPluginConfig dyPluginConfig : dynamicPluginConfigs) {
					if (LOGGER.isDebugEnabled() && dyPluginConfig != null) {
						LOGGER.debug(dyPluginConfig.getName());
					}
				}
			}
		}
	}

	@Override
	public BatchClass getBatchClassByNameIncludingDeleted(String batchClassName) {
		return batchClassDao.getBatchClassByNameIncludingDeleted(batchClassName);
	}

	@Override
	@Transactional
	public BatchClass createBatchClassWithoutWatch(BatchClass batchClass) {
		BatchClass batchClass1 = null;
		batchClass1 = batchClassDao.merge(batchClass);
		return batchClass1;
	}

	@Override
	@Transactional
	public BatchClass getLoadedBatchClassByNameIncludingDeleted(String batchClassName) {
		BatchClass batchClass = null;
		batchClass = batchClassDao.getBatchClassByNameIncludingDeleted(batchClassName);
		loadBatchClass(batchClass);
		return batchClass;
	}
}
