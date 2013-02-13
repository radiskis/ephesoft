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

package com.ephesoft.dcma.da.service;

import java.io.File;
import java.io.IOException;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.BatchAlreadyLockedException;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.common.DeleteBatchClassEvent;
import com.ephesoft.dcma.da.common.NewBatchClassEvent;
import com.ephesoft.dcma.da.constant.DataAccessConstant;
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
import com.ephesoft.dcma.da.domain.BatchClassScannerConfiguration;
import com.ephesoft.dcma.da.domain.Dependency;
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
import com.ephesoft.dcma.da.property.BatchClassProperty;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.FileUtils;

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
	
	/**
	 * batchClassDao {@link BatchClassDao}.
	 */
	@Autowired
	private BatchClassDao batchClassDao;

	/**
	 * applicationContext {@link ApplicationContext}.
	 */
	private ApplicationContext applicationContext;

	/**
	 * An object used for synchronization.
	 */
	private static Object object = new Object();

	/** 
	 * Set the ApplicationContext that this object runs in.
     *
	 * @param applicationContext the ApplicationContext object to be used by this object
	 * @throws BeansException if thrown by application context methods
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * An API to fetch BatchClass by id.
	 * 
	 * @param identifier {@link Serializable}
	 * @return {@link BatchClass}
	 */
	@Transactional(readOnly = true)
	@Override
	public BatchClass get(Serializable identifier) {
		return batchClassDao.getBatchClassByIdentifier((String) identifier);
	}

	/**
	 * An API to save or update the batch class.
	 * 
	 * @param batchClass {@link BatchClass}
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
	 * An API to fetch BatchClass by uncFolder name.
	 * 
	 * @param folderName {@link String}
	 * @return {@link BatchClass}
	 */
	@Transactional(readOnly = true)
	@Override
	public BatchClass getBatchClassbyUncFolder(String folderName) {
		BatchClass batchClass = null;
		if (null == folderName || DataAccessConstant.EMPTY.equals(folderName)) {
			LOGGER.info("folderName is null or empty.");
		} else {
			batchClass = batchClassDao.getBatchClassbyUncFolder(folderName);
		}
		return batchClass;
	}

	/**
	 * An API to fetch BatchClass by batch Class name.
	 * 
	 * @param batchClassName {@link String}
	 * @return {@link BatchClass}
	 */
	@Transactional(readOnly = true)
	@Override
	public BatchClass getBatchClassbyName(String batchClassName) {
		BatchClass batchClass = null;
		if (null == batchClassName || DataAccessConstant.EMPTY.equals(batchClassName)) {
			LOGGER.info("batchClassName is null or empty.");
		} else {
			batchClass = batchClassDao.getBatchClassbyName(batchClassName);
		}
		return batchClass;
	}

	/**
	 * An API to fetch BatchClass by batch Class processName.
	 * 
	 * @param processName {@link String}
	 * @return {@link BatchClass}
	 */
	@Transactional(readOnly = true)
	@Override
	public BatchClass getBatchClassbyProcessName(String processName) {
		BatchClass batchClass = null;
		if (null == processName || DataAccessConstant.EMPTY.equals(processName)) {
			LOGGER.info("processName is null or empty.");
		} else {
			batchClass = batchClassDao.getBatchClassbyName(processName);
		}
		return batchClass;
	}

	/**
	 * This API will fetch all the unlocked batch classes.
	 * 
	 * @return List<{@link BatchClass}>
	 */
	@Override
	public List<BatchClass> getAllUnlockedBatchClasses() {
		List<BatchClass> batchClassList = null;
		batchClassList = batchClassDao.getAllUnlockedBatchClasses();
		return batchClassList;
	}

	/**
	 * This API will fetch all the batch classes.
	 * 
	 * @return List<{@link BatchClass}>
	 */
	@Override
	public List<BatchClass> getAllBatchClasses() {
		List<BatchClass> batchClassList = null;
		batchClassList = batchClassDao.getAllBatchClasses();
		return batchClassList;
	}

	/**
	 * API to fetch a batch class by identifier.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @return {@link BatchClass}
	 */
	@Override
	@Transactional(readOnly = true)
	public BatchClass getBatchClassByIdentifier(String batchClassIdentifier) {
		BatchClass batchClass = null;
		batchClass = batchClassDao.getBatchClassByIdentifier(batchClassIdentifier);
		return batchClass;
	}

	/**
	 * This API will fetch the batch class (eagerly loaded) on the basis of identifier.
	 * 
	 * @param batchClassIdentifier String
	 * @return List<{@link BatchClass}>
	 */
	@Override
	@Transactional(readOnly = true)
	public BatchClass getLoadedBatchClassByIdentifier(String batchClassIdentifier) {
		BatchClass batchClass = null;
		batchClass = batchClassDao.getBatchClassByIdentifier(batchClassIdentifier);
		if (null != batchClass) {
			loadBatchClass(batchClass);
		}
		return batchClass;
	}

	/**
	 * API to get the batch class loaded by UNC folder path.
	 * 
	 * @param folderName {@link String}
	 * @return {@link BatchClass}
	 */
	@Override
	@Transactional(readOnly = true)
	public BatchClass getLoadedBatchClassByUNC(String folderName) {
		BatchClass batchClass = null;
		batchClass = batchClassDao.getBatchClassbyUncFolder(folderName);
		if (null != batchClass) {
			loadBatchClass(batchClass);
		}
		return batchClass;
	}

	/**
	 * API to acquire a batch on the basis of batchClassID and userName.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param currentUser {@link String}
	 * @return {@link BatchClass}
	 * @throws BatchAlreadyLockedException in case of error
	 */
	@Override
	@Transactional(readOnly = false)
	public BatchClass acquireBatchClass(String batchClassIdentifier, String currentUser)
			throws BatchAlreadyLockedException {
		BatchClass batchClass = null;
		synchronized (object) {
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
		}
		return batchClass;
	}

	/**
	 * Unlocks all the batches acquired by the user.
	 * 
	 * @param currentUser {@link String}
	 */
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

	/**
	 * Unlocks the currently acquired batch by the user (currentUser).
	 * 
	 * @param batchClassIdentifier {@link String}
	 */
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

	/**
	 * API to merge the batch class.
	 * 
	 * @param batchClass {@link BatchClass}
	 * @param isBatchClassDeleted boolean
	 * @return {@link BatchClass}
	 */ 
	@Transactional
	@Override
	public BatchClass merge(BatchClass batchClass, boolean isBatchClassDeleted) {
		if (isBatchClassDeleted) {
			applicationContext
					.publishEvent(new DeleteBatchClassEvent(applicationContext, new BatchClassID(batchClass.getIdentifier())));
		}
		return batchClassDao.merge(batchClass);
	}

	/**
	 * API to evict a batch class object.
	 * 
	 * @param batchClass {@link BatchClass}
	 */
	@Transactional
	@Override
	public void evict(BatchClass batchClass) {
		batchClassDao.evict(batchClass);
	}

	/**
	 * API to get the list of Batch Classes specifying start index, no of results and sorting if any.
	 * 
	 * @param firstResult int
	 * @param maxResults int
	 * @param order {@link Order}
	 * @param userRoles Set<{@link BatchClass}>
	 * @return List<{@link BatchClass}>
	 */
	@Override
	public List<BatchClass> getBatchClassList(int firstResult, int maxResults, final Order order, Set<String> userRoles) {
		List<BatchClass> batches = null;
		Order batchClassOrder = order;
		List<Order> orderList = new ArrayList<Order>();
		if (order == null) {
			batchClassOrder = new Order(BatchClassProperty.ID, true);
		}
		orderList.add(batchClassOrder);
		batches = batchClassDao.getBatchClassList(firstResult, maxResults, orderList, userRoles);
		return batches;
	}

	/**
	 * API to count all the batch classes. * @param userRoles
	 * 
	 * @param userRoles Set<String>
	 * @return count int
	 */
	@Override
	public int countAllBatchClassesExcludeDeleted(Set<String> userRoles) {
		return batchClassDao.getAllBatchClassCountExcludeDeleted(userRoles);
	}

	/**
	 * API to delete BatchClass by id.
	 * 
	 * @param identifier {@link Serializable}
	 */
	@Override
	@Transactional
	public void delete(Serializable identifier) {
		BatchClass object = batchClassDao.get(identifier);
		batchClassDao.remove(object);
	}

	/**
	 * API to create batch class.
	 * 
	 * @param batchClass {@link BatchClass}
	 * @return {@link BatchClass}
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public BatchClass createBatchClass(BatchClass batchClass) {
		BatchClass newBatchClass = null;
		newBatchClass = batchClassDao.merge(batchClass);
		applicationContext.publishEvent(new NewBatchClassEvent(applicationContext, new BatchClassID(newBatchClass.getIdentifier())));
		return newBatchClass;
	}

	/**
	 * This API will fetch all the batch classes on user role.
	 * 
	 * @param userRoles Set<String>
	 * @return List<{@link BatchClass}>
	 */
	@Override
	public List<BatchClass> getAllBatchClassesByUserRoles(Set<String> userRoles) {
		List<BatchClass> batchClassList = new ArrayList<BatchClass>();
		batchClassList = batchClassDao.getAllBatchClassesByUserRoles(userRoles);
		return batchClassList;
	}

	/**
	 * API to get all batch classes excluding the one's deleted.
	 * 
	 * @return List<{@link BatchClass}>
	 */
	@Override
	public List<BatchClass> getAllBatchClassesExcludeDeleted() {
		List<BatchClass> batchClassList = null;
		batchClassList = batchClassDao.getAllBatchClassExcludeDeleted();
		return batchClassList;
	}
	
	/**
	 * This API will fetch the batch class (eagerly loaded) list excluding the deleted batch class.
	 * 
	 * @return List<{@link BatchClass}>
	 */
	@Override
	@Transactional
	public List<BatchClass> getAllLoadedBatchClassExcludeDeleted() {
		List<BatchClass> batchClassList = null;
		batchClassList = batchClassDao.getAllLoadedBatchClassExcludeDeleted();
		return batchClassList;
	}

	/**
	 * API to merge a batch class.
	 * 
	 * @param batchClass {@link BatchClass}
	 * @return {@link BatchClass}
	 */
	@Override
	@Transactional
	public BatchClass merge(BatchClass batchClass) {
		return batchClassDao.merge(batchClass);
	}

	/**
	 * API to get all the associated UNC folders with a workflow.
	 * 
	 * @param workflowName {@link String}
	 * @return List<{@link String}>
	 */
	@Override
	@Transactional
	public List<String> getAssociatedUNCList(String workflowName) {
		List<String> uncFolderList = null;
		uncFolderList = batchClassDao.getAllAssociatedUNCFolders(workflowName);
		return uncFolderList;
	}

	/**
	 * API to get the batch class loaded with batch class modules configurations.
	 * 
	 * @param batchClassIdentifier(@link String)
	 * @return {@link BatchClass}
	 */
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
		List<BatchClassEmailConfiguration> emailConfigurations = batchClass.getEmailConfigurations();
		if (emailConfigurations != null && !emailConfigurations.isEmpty()) {
			for (BatchClassEmailConfiguration emails : emailConfigurations) {
				if (emails != null) {
					LOGGER.debug(emails.getFolderName());
				}
			}
		}

		List<BatchClassScannerConfiguration> scannerProfiles = batchClass.getBatchClassScannerConfiguration();
		if (scannerProfiles != null && !scannerProfiles.isEmpty()) {
			for (BatchClassScannerConfiguration scannerProfile : scannerProfiles) {
				if (scannerProfile != null) {
					LOGGER.debug(scannerProfile.getScannerMasterConfig().getName());
					LOGGER.debug(scannerProfile.getValue());
					List<BatchClassScannerConfiguration> childScannerProfiles = scannerProfile.getChildren();
					if (childScannerProfiles != null && !childScannerProfiles.isEmpty()) {
						for (BatchClassScannerConfiguration childScannerProfile : childScannerProfiles) {
							LOGGER.debug(childScannerProfile.getScannerMasterConfig().getName());
							LOGGER.debug(childScannerProfile.getValue());

						}
					}
				}
			}
		}

		List<BatchClassField> batchClassFields = batchClass.getBatchClassField();
		if (batchClassFields != null && !batchClassFields.isEmpty()) {
			for (BatchClassField batchClassField : batchClassFields) {
				if (batchClassField != null) {
					LOGGER.debug(batchClassField.getName());
				}
			}
		}

		loadDocumentType(batchClass);

		List<BatchClassGroups> assignedGroups = batchClass.getAssignedGroups();
		if (assignedGroups != null && !assignedGroups.isEmpty()) {
			for (BatchClassGroups roles : assignedGroups) {
				if (roles != null) {
					LOGGER.debug(roles.getGroupName());
				}
			}
		}

		loadBatchClassModules(batchClass);

	}

	private void loadBatchClassModules(BatchClass batchClass) {
		for (BatchClassModule mod : batchClass.getBatchClassModules()) {
			List<BatchClassModuleConfig> modConfigs = mod.getBatchClassModuleConfig();
			if (modConfigs != null && !modConfigs.isEmpty()) {
				for (BatchClassModuleConfig bcModConfig : modConfigs) {
					ModuleConfig moduleConfig = bcModConfig.getModuleConfig();
					if (moduleConfig != null) {
						LOGGER.debug(moduleConfig.getChildDisplayName());
					}
				}
			}

			List<BatchClassPlugin> plugins = mod.getBatchClassPlugins();
			if (plugins != null && !plugins.isEmpty()) {
				for (BatchClassPlugin plugin : plugins) {
					List<BatchClassPluginConfig> pluginConfigs = plugin.getBatchClassPluginConfigs();
					List<BatchClassDynamicPluginConfig> dynamicPluginConfigs = plugin.getBatchClassDynamicPluginConfigs();
					if (pluginConfigs != null && !pluginConfigs.isEmpty()) {
						for (BatchClassPluginConfig conf : pluginConfigs) {
							List<KVPageProcess> kvs = conf.getKvPageProcesses();
							for (KVPageProcess kv : kvs) {
								if (kv != null) {
									LOGGER.debug(kv.getKeyPattern());
								}
							}
						}
					}
					if (dynamicPluginConfigs != null && !dynamicPluginConfigs.isEmpty()) {
						for (BatchClassDynamicPluginConfig dyPluginConfig : dynamicPluginConfigs) {
							if (dyPluginConfig != null) {
								LOGGER.debug(dyPluginConfig.getName());
								List<BatchClassDynamicPluginConfig> children = dyPluginConfig.getChildren();
								if (children != null && !children.isEmpty()) {
									for (BatchClassDynamicPluginConfig child : children) {
										if (child != null) {
											LOGGER.debug(child.getName());
											LOGGER.debug(child.getValue());
										}
									}
								}
							}
						}
					}
					for (Dependency dependency : plugin.getPlugin().getDependencies()) {
						if (dependency != null) {
							LOGGER.debug(dependency.getDependencies());
						}
					}
				}
			}
		}
	}

	private void loadDocumentType(BatchClass batchClass) {
		List<DocumentType> documentTypes = batchClass.getDocumentTypes();
		if (documentTypes != null && !documentTypes.isEmpty()) {
			for (DocumentType documentType : batchClass.getDocumentTypes()) {
				if (documentType != null) {
					LOGGER.debug(documentType.getName());
					for (PageType pageType : documentType.getPages()) {
						if (pageType != null) {
							LOGGER.debug(pageType.getName());
						}
					}

					List<FieldType> fieldTypes = documentType.getFieldTypes();
					if (fieldTypes != null && !fieldTypes.isEmpty()) {
						for (FieldType fieldType : fieldTypes) {
							if (fieldType != null) {
								LOGGER.debug(fieldType.getName());
								for (KVExtraction kvExtraction : fieldType.getKvExtraction()) {
									if (kvExtraction != null) {
										LOGGER.debug(kvExtraction.getKeyPattern());
									}
								}
								for (RegexValidation regexValidation : fieldType.getRegexValidation()) {
									if (regexValidation != null) {
										LOGGER.debug(regexValidation.getPattern());
									}
								}
							}
						}
					}
					List<TableInfo> tableInfos = documentType.getTableInfos();
					if (tableInfos != null && !tableInfos.isEmpty()) {
						for (TableInfo tableInfo : tableInfos) {
							if (tableInfo != null) {
								LOGGER.debug(tableInfo.getName());
								for (TableColumnsInfo tableColumnsInfo : tableInfo.getTableColumnsInfo()) {
									if (tableColumnsInfo != null) {
										LOGGER.debug(tableColumnsInfo.getColumnName());
									}
								}
							}
						}

					}
					List<FunctionKey> functionKeys = documentType.getFunctionKeys();
					if (functionKeys != null && !functionKeys.isEmpty()) {
						for (FunctionKey functionKey : functionKeys) {
							if (functionKey != null) {
								LOGGER.debug(functionKey.getShortcutKeyname());
							}
						}
					}
				}
			}
		}
	}

	/**
	 * API to get the batch class loaded by UNC name.
	 * 
	 * @param batchClassName {@link String}
	 * @return {@link BatchClass}
	 */
	@Override
	public BatchClass getBatchClassByNameIncludingDeleted(String batchClassName) {
		BatchClass batchClass = batchClassDao.getBatchClassByNameIncludingDeleted(batchClassName);

		if (batchClass != null) {
			for (BatchClassModule mod : batchClass.getBatchClassModules()) {
				for (BatchClassModuleConfig importConfig : mod.getBatchClassModuleConfig()) {
					if (LOGGER.isDebugEnabled() && importConfig != null) {
						LOGGER.debug(importConfig.toString());
					}
				}
				List<BatchClassPlugin> plugins = mod.getBatchClassPlugins();
				for (BatchClassPlugin plugin : plugins) {
					if (LOGGER.isDebugEnabled() && plugin != null) {
						LOGGER.debug(plugin.toString());
					}

				}
			}
		}
		return batchClass;
	}

	/**
	 * API to create batch class without adding watch to it.
	 * 
	 * @param batchClass {@link BatchClass}
	 * @return {@link BatchClass}
	 */
	@Override
	@Transactional
	public BatchClass createBatchClassWithoutWatch(BatchClass batchClass) {
		BatchClass batchClass1 = null;
		batchClass1 = batchClassDao.merge(batchClass);
		return batchClass1;
	}

	/**
	 * API to get loaded batch class by workflow name.
	 * 
	 * @param batchClassName {@link String}
	 * @return {@link BatchClass}
	 */
	@Override
	@Transactional
	public BatchClass getLoadedBatchClassByNameIncludingDeleted(String batchClassName) {
		BatchClass batchClass = null;
		batchClass = batchClassDao.getBatchClassByNameIncludingDeleted(batchClassName);
		if (null != batchClass) {
			loadBatchClass(batchClass);
		}
		return batchClass;
	}

	/**
	 * API to get batch class identifier by UNC folder.
	 * 
	 * @param uncFolder {@link String}
	 * @return {@link String}
	 */
	@Override
	public String getBatchClassIdentifierByUNCfolder(String uncFolder) {
		String identifier = null;
		identifier = batchClassDao.getBatchClassIdentifierByUNCfolder(uncFolder);
		return identifier;
	}

	/**
	 * This API returns list of all batch class identifiers.
	 * 
	 * @return List<String>
	 */
	@Override
	public List<String> getAllBatchClassIdentifier() {
		List<String> allBatchClassIdentifiers = null;
		allBatchClassIdentifiers = batchClassDao.getAllBatchClassIdentifiers();
		return allBatchClassIdentifiers;
	}

	/**
	 * API to get all batch classes on the basis of excluding the deleted batch class and on the basis of ascending or desending order
	 * of specified property.
	 * 
	 * @param isExcludeDeleted boolean
	 * @param isAsc boolean
	 * @param propertyName String
	 * @return List<{@link BatchClass}>
	 */
	@Override
	public List<BatchClass> getAllBatchClasses(boolean isExcludeDeleted, boolean isAsc, String propertyName) {
		List<BatchClass> batchClassList = null;
		batchClassList = batchClassDao.getAllBatchClasses(isExcludeDeleted, isAsc, propertyName);
		return batchClassList;
	}

	/**
	 * API to get all the UNC folders on the basis of excluding the deleted batch class.
	 * 
	 * @param isExcludeDeleted boolean
	 * @return List<{@link String}>
	 *
	@Override
	public List<String> getAllUNCList(boolean isExcludeDeleted) {
		return batchClassDao.getAllUNCList(isExcludeDeleted);
	}

	/**
	 * API to copy the batch class with given id and create a new batch class with the details provided as arguments . This API also
	 * creates a new batch class group with given name (if not null) and assign that group to this newly created batch class
	 * 
	 * @param identifier{@link String}
	 * @param batchClassName{@link String}
	 * @param batchClassDesc{@link String}
	 * @param batchClassGroup{@link String}
	 * @param batchClassPriority{@link String}
	 * @param uncFolder{@link String}
	 * @param isConfigureExportFolder boolean
	 * @return {@link BatchClass}
	 * @throws DCMAApplicationException in case of error
	 */
	@Override
	@Transactional
	public BatchClass copyBatchClass(final String identifier, final String batchClassName, final String batchClassDesc,
			final String batchClassGroup, final String batchClassPriority, final String uncFolderName,
			final boolean isConfigureExportFolder) throws DCMAApplicationException {
		LOGGER.info("Inside Copy of batch class: " + identifier);
		boolean configureExportFolder = isConfigureExportFolder;
		String sharedFolderPath = null;
		// empty and null checks
		isStringEmptyOrNull("batch class name", batchClassName);
		isStringEmptyOrNull("batch class identifier", identifier);
		isStringEmptyOrNull("batch class group", batchClassGroup);
		isStringEmptyOrNull("batch class priority", batchClassPriority);
		isStringEmptyOrNull("UNC folder", uncFolderName);
		// checking batch class and unc folder existence
		LOGGER.info("Checking batch class and unc folder existence");
		checkBatchClassOrUncFolderExistence(batchClassName, uncFolderName);
		LOGGER.info("Getting the loaded batch class from database");
		BatchClass batchClass = getLoadedBatchClassByIdentifier(identifier);
		if (batchClass == null) {
			LOGGER.error("Batch class with id:" + identifier + " does not exists.");
			throw new DCMAApplicationException("Batch class with id:" + identifier + " does not exist.");
		} else {
			evict(batchClass);
			// getting the shared folder path from batch properties file
			try {
				sharedFolderPath = ApplicationConfigProperties.getApplicationConfigProperties().getAllProperties(DataAccessConstant.DCMA_BATCH)
						.getProperty(DataAccessConstant.BATCH_BASE_FOLDER);
			} catch (IOException ioException) {
				LOGGER.error("Unable to get shared folder path from properties file.", ioException);
				throw new DCMAApplicationException("Unable to get shared folder path from properties file.", ioException);
			}
			isStringEmptyOrNull("Shared folder path ", sharedFolderPath);
			String uncFolderPath = sharedFolderPath + File.separator + uncFolderName;
			// creating unc folder
			final File uncFolder = new File(uncFolderPath);
			uncFolder.mkdirs();
			// setting batch class info
			LOGGER.info("Setting batch class info.");
			setBatchClassInfo(batchClass, batchClassName, batchClassDesc, uncFolderPath, batchClassGroup, batchClassPriority);
			LOGGER.info("Start copy batch class modules.");
			copyModules(batchClass);
			LOGGER.info("Start copy batch class document types.");
			copyDocumentTypes(batchClass);
			LOGGER.info("Start copy batch class scanner configs.");
			copyScannerConfig(batchClass);
			LOGGER.info("Start copy batch class fields.");
			copyBatchClassField(batchClass);
			LOGGER.info("Creating batch class");
			batchClass = createBatchClass(batchClass);
			// copy batch class folder
			evict(batchClass);
			batchClass = get(batchClass.getIdentifier());
			final File originalFolder = new File(sharedFolderPath + File.separator + identifier);
			final File copiedFolder = new File(sharedFolderPath + File.separator + batchClass.getIdentifier());
			try {
				FileUtils.copyDirectoryWithContents(originalFolder, copiedFolder);
			} catch (IOException ioException) {
				LOGGER.error("Error in copy of the batch class folder.", ioException);
				throw new DCMAApplicationException("Error in copy of the batch class folder.", ioException);
			}
			LOGGER.info("Batch class folder has been created successfully");
			if (configureExportFolder) {
				// configure the export folder of newly created batch class
				LOGGER.info("Inside configure export folder");
				final String newExportFolder = sharedFolderPath + File.separator + batchClass.getBatchClassID() + File.separator
						+ DataAccessConstant.FINAL_DROP_FOLDER;
				final boolean isValueUpdated = setPluginConfigValue(batchClass, DataAccessConstant.EXPORT, DataAccessConstant.COPY_BATCH_XML, DataAccessConstant.BATCH_EXPORT_TO_FOLDER,
						newExportFolder);
				if (isValueUpdated) {
					LOGGER.info("Updating the batch class after configuring the export folder.");
					// updating the batch class
					batchClassDao.updateBatchClass(batchClass);
					// creating the final drop folder in the batch class folder for the new batch class
					LOGGER.info("Creating the export folder for the batch class");
					final File exportFolder = new File(newExportFolder);
					exportFolder.mkdirs();
					LOGGER.info("Final drop folder created successfully");
				} else {
					LOGGER.error("Error in configuring the export folder");
					throw new DCMAApplicationException("Error in configuring the export folder");
				}
			}

		}
		return batchClass;
	}

	/**
	 * API to check for null or empty strings.
	 * 
	 * @param property{@link String} : the name of the string
	 * @param data{@link String} : value of the string
	 * @throws DCMAApplicationException in case of error
	 */
	private void isStringEmptyOrNull(String property, String data) throws DCMAApplicationException {
		if (data == null) {
			LOGGER.error(property + " is null.");
			throw new DCMAApplicationException(property + " is null.");
		}
		if (data.isEmpty()) {
			LOGGER.error(property + " is empty.");
			throw new DCMAApplicationException(property + " is empty.");
		}
	}

	/**
	 * API that checks whether batch class with name or unc folder exists. Returns true if batch class with name or unc folder exists
	 * else returns false.
	 * 
	 * @param batchClassName {@link String}
     * @param uncFolder {@link String}
	 * @throws DCMAApplicationException in case of error
	 */
	private void checkBatchClassOrUncFolderExistence(String batchClassName, String uncFolder) throws DCMAApplicationException {
		LOGGER.info("Checking batch class existance");
		final List<BatchClass> batchClasses = getAllBatchClasses();
		final List<String> uncFolders = getAllUNCList(false);
		if (batchClasses != null && (!batchClasses.isEmpty())) {
			for (BatchClass batchClass : batchClasses) {
				if (batchClass.getName().equalsIgnoreCase(batchClassName)) {
					LOGGER.error("Batch class with name : " + batchClassName + " already exists");
					throw new DCMAApplicationException("Batch class with name : " + batchClassName + " already exists.");

				}
			}
		}
		if (uncFolders != null && !uncFolders.isEmpty()) {
			for (String uncFolderName : uncFolders) {
				if (uncFolderName.equalsIgnoreCase(uncFolder)) {
					LOGGER.error("UNC folder with name : " + uncFolder + " already exists");
					throw new DCMAApplicationException("UNC folder with name : " + uncFolder + " already exists");
				}

			}
		}
	}

	/**
	 * API sets the value of the given batch class plugin config with the new value value. Returns true if value is updated
	 * successfully.
	 * 
	 * @param batchClass{@link BatchClass}
	 * @param moduleName{@link String}
	 * @param pluginName{@link String}
	 * @param pluginConfigName{@link String}
	 * @param newpluginConfigValue{@link String}
	 * @return boolean
	 */
	private boolean setPluginConfigValue(BatchClass batchClass, final String moduleName, final String pluginName,
			final String pluginConfigName, final String newpluginConfigValue) {
		boolean isValueUpdated = false;
		BatchClassPlugin copyBatchXMLPlugin = null;
		final BatchClassModule exportModule = batchClass.getBatchClassModuleByName(moduleName);
		List<BatchClassPlugin> batchClassPlugins = null;
		if (exportModule != null) {
			batchClassPlugins = exportModule.getBatchClassPlugins();
		}
		if (batchClassPlugins != null) {
			for (BatchClassPlugin batchClassPlugin : batchClassPlugins) {
				if (batchClassPlugin.getPlugin().getPluginName().equals(pluginName)) {
					copyBatchXMLPlugin = batchClassPlugin;
					break;
				}
			}

			isValueUpdated = updatePluginInformation(copyBatchXMLPlugin, pluginConfigName, newpluginConfigValue);

		}
		return isValueUpdated;
	}

	/**
	 * API updates the value of the plugin config with the new value given as argument.
	 * 
	 * @param plugin{@link BatchClassPlugin}
	 * @param pluginConfigName{@link String}
	 * @param newPluginConfigValue{@link String}
	 * @return  boolean
	 */
	private boolean updatePluginInformation(BatchClassPlugin plugin, final String pluginConfigName, final String newPluginConfigValue) {
		LOGGER.info("Updating the plugin config value.");
		boolean isPluginConfigValueUpdated = false;
		if (plugin != null) {
			final List<BatchClassPluginConfig> batchClassPluginConfigs = plugin.getBatchClassPluginConfigs();
			if (batchClassPluginConfigs != null && (!batchClassPluginConfigs.isEmpty())) {
				for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
					if (batchClassPluginConfig.getName().equals(pluginConfigName)) {
						batchClassPluginConfig.setValue(newPluginConfigValue);
						isPluginConfigValueUpdated = true;
						break;
					}
				}

			}

		}
		return isPluginConfigValueUpdated;
	}

	/**
	 * API sets the batch class info with the given arguments.
	 * 
	 * @param batchClass{@link String}
	 * @param batchClassName{@link String}
	 * @param batchClassDesc{@link String}
	 * @param uncFolder{@link String}
	 * @param batchClassGroup{@link String}
	 * @param batchClassPriority{@link String}
	 */
	private void setBatchClassInfo(final BatchClass batchClass, final String batchClassName, final String batchClassDesc,
			final String uncFolder, final String batchClassGroup, final String batchClassPriority) {
		LOGGER.info("Setting batch class info.");
		batchClass.setDescription(batchClassDesc);
		int priority = DataAccessConstant.DEFAULT_PRIORITY;
		if (batchClassPriority != null) {
			try {
				int newpriority = Integer.valueOf(batchClassPriority);
				if (0 <= newpriority && newpriority <= DataAccessConstant.PRIORITY) {
					priority = newpriority;
				}
			} catch (NumberFormatException e) {
				LOGGER.error("Error converting priority: " + batchClassPriority + "for batch class:" + " ." + e.getMessage(), e);
			}
		}
		batchClass.setPriority(priority);
		batchClass.setUncFolder(uncFolder);
		batchClass.setName(batchClassName);
		batchClass.setId(0);
		batchClass.setIdentifier(null);
		batchClass.setCurrentUser(null);
		batchClass.setVersion(ICommonConstants.VERSION);
		batchClass.setEmailConfigurations(null);
		final List<BatchClassGroups> batchClassGroupsList = batchClass.getAssignedGroups();
		if (batchClassGroup == null) {
			// assign the old batch class groups to the new batch class
			for (BatchClassGroups batchClassGroups : batchClassGroupsList) {
				batchClassGroups.setId(0);
			}

		} else {// create a new user group and assign that group to new batch class
			batchClassGroupsList.clear();
			final BatchClassGroups batchClassGroupName = new BatchClassGroups();
			batchClassGroupName.setBatchClass(batchClass);
			batchClassGroupName.setGroupName(batchClassGroup);
			batchClassGroupsList.add(batchClassGroupName);
		}

		batchClass.setAssignedGroups(batchClassGroupsList);
		batchClass.setDeleted(false);
	}

	/**
	 * API to copy modules of batch class.
	 * 
	 * @param batchClass{@link BatchClass}
	 */
	private void copyModules(BatchClass batchClass) {
		final List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
		final List<BatchClassModule> newBatchClassModulesList = new ArrayList<BatchClassModule>();
		for (BatchClassModule batchClassModule : batchClassModules) {
			newBatchClassModulesList.add(batchClassModule);
			batchClassModule.setId(0);
			batchClassModule.setBatchClass(null);
			copyPlugins(batchClassModule);
			copyModuleConfig(batchClassModule);
		}
		batchClass.setBatchClassModules(newBatchClassModulesList);
	}

	/**
	 * API to copy Document Types of batch class.
	 * 
	 * @param batchClass{@link BatchClass}
	 */
	private void copyDocumentTypes(BatchClass batchClass) {
		final List<DocumentType> documentTypes = batchClass.getDocumentTypes();
		List<DocumentType> newDocumentType = new ArrayList<DocumentType>();
		for (DocumentType documentType : documentTypes) {
			newDocumentType.add(documentType);
			documentType.setId(0);
			documentType.setBatchClass(null);
			documentType.setIdentifier(null);
			copyPageTypes(documentType);
			copyFieldTypes(documentType);
			copyTableInfo(documentType);
			copyFunctionKeys(documentType);
		}
		batchClass.setDocumentTypes(newDocumentType);
	}

	/**
	 * API to copy ScannerConfig of batch class.
	 * 
	 * @param batchClass{@link BatchClass}
	 */
	private void copyScannerConfig(BatchClass batchClass) {
		List<BatchClassScannerConfiguration> configs = batchClass.getBatchClassScannerConfiguration();
		List<BatchClassScannerConfiguration> newConfigsList = new ArrayList<BatchClassScannerConfiguration>();
		for (BatchClassScannerConfiguration config : configs) {
			if (config.getParent() == null) {
				newConfigsList.add(config);
				config.setId(0);
				config.setBatchClass(null);

				List<BatchClassScannerConfiguration> children = config.getChildren();
				for (BatchClassScannerConfiguration child : children) {
					child.setId(0);
					newConfigsList.add(child);
					child.setBatchClass(null);
					child.setParent(config);
				}
			}
		}
		batchClass.setBatchClassScannerConfiguration(newConfigsList);
	}

	/**
	 * API to copy Batch Class Fields of batch class.
	 * 
	 * @param batchClass{@link BatchClass}
	 */
	private void copyBatchClassField(BatchClass batchClass) {
		final List<BatchClassField> batchClassField = batchClass.getBatchClassField();
		List<BatchClassField> newBatchClassField = new ArrayList<BatchClassField>();
		for (BatchClassField batchClassFieldTemp : batchClassField) {
			newBatchClassField.add(batchClassFieldTemp);
			batchClassFieldTemp.setId(0);
			batchClassFieldTemp.setBatchClass(null);
			batchClassFieldTemp.setIdentifier(null);
		}
		batchClass.setBatchClassField(newBatchClassField);
	}

	/**
	 * API to copy Page Types of batch class.
	 * 
	 * @param documentType{@link DocumentType}
	 */
	private void copyPageTypes(DocumentType documentType) {
		List<PageType> pages = documentType.getPages();
		List<PageType> newPageTypes = new ArrayList<PageType>();
		for (PageType pageType : pages) {
			newPageTypes.add(pageType);
			pageType.setId(0);
			pageType.setDocType(null);
			pageType.setIdentifier(null);
		}
		documentType.setPages(newPageTypes);
	}

	/**
	 * API to copy Field Types of batch class.
	 * 
	 * @param documentType{@link DocumentType}
	 */
	private void copyFieldTypes(DocumentType documentType) {
		List<FieldType> fieldTypes = documentType.getFieldTypes();
		List<FieldType> newFieldType = new ArrayList<FieldType>();
		for (FieldType fieldType : fieldTypes) {
			newFieldType.add(fieldType);
			fieldType.setId(0);
			fieldType.setDocType(null);
			fieldType.setIdentifier(null);
			copyKVExtractionFields(fieldType);
			copyRegex(fieldType);
		}
		documentType.setFieldTypes(newFieldType);
	}

	/**
	 * API to copy Table Info of batch class.
	 * 
	 * @param documentType{@link DocumentType}
	 */
	private void copyTableInfo(DocumentType documentType) {
		List<TableInfo> tableInfos = documentType.getTableInfos();
		List<TableInfo> newTableInfo = new ArrayList<TableInfo>();
		for (TableInfo tableInfo : tableInfos) {
			newTableInfo.add(tableInfo);
			tableInfo.setId(0);
			tableInfo.setDocType(null);
			copyTableColumnsInfo(tableInfo);
		}
		documentType.setTableInfos(newTableInfo);
	}

	/**
	 * API to copy Function Keys of batch class.
	 * 
	 * @param documentType{@link DocumentType}
	 */
	public void copyFunctionKeys(DocumentType documentType) {
		List<FunctionKey> functionKeys = documentType.getFunctionKeys();
		List<FunctionKey> newFunctionKeys = new ArrayList<FunctionKey>();
		for (FunctionKey functionKey : functionKeys) {
			newFunctionKeys.add(functionKey);
			functionKey.setId(0);
			functionKey.setDocType(null);
			functionKey.setIdentifier(null);
		}
		documentType.setFunctionKeys(newFunctionKeys);

	}

	/**
	 * API to copy Table Columns Info of batch class.
	 * 
	 * @param tableInfo{@link TableInfo}
	 */
	public void copyTableColumnsInfo(TableInfo tableInfo) {
		List<TableColumnsInfo> tableColumnsInfos = tableInfo.getTableColumnsInfo();
		List<TableColumnsInfo> newTableColumnsInfo = new ArrayList<TableColumnsInfo>();
		for (TableColumnsInfo tableColumnsInfo : tableColumnsInfos) {
			newTableColumnsInfo.add(tableColumnsInfo);
			tableColumnsInfo.setId(0);
		}
		tableInfo.setTableColumnsInfo(newTableColumnsInfo);
	}

	/**
	 * API to copy KVExtractionFields of batch class.
	 * 
	 * @param fieldType{@link FieldType}
	 */
	private void copyKVExtractionFields(FieldType fieldType) {
		List<KVExtraction> kvExtraction2 = fieldType.getKvExtraction();
		List<KVExtraction> newKvExtraction = new ArrayList<KVExtraction>();
		for (KVExtraction kvExtraction : kvExtraction2) {
			newKvExtraction.add(kvExtraction);
			kvExtraction.setId(0);
			kvExtraction.setFieldType(null);
		}
		fieldType.setKvExtraction(newKvExtraction);
	}

	/**
	 * API to copy Regex of batch class.
	 * 
	 * @param fieldType{@link FieldType}
	 */
	private void copyRegex(FieldType fieldType) {
		List<RegexValidation> regexValidations = fieldType.getRegexValidation();
		List<RegexValidation> regexValidations2 = new ArrayList<RegexValidation>();
		for (RegexValidation regexValidation : regexValidations) {
			regexValidations2.add(regexValidation);
			regexValidation.setId(0);
			regexValidation.setFieldType(null);
		}
		fieldType.setRegexValidation(regexValidations2);
	}

	/**
	 * API to copy Plugins of batch class.
	 * 
	 * @param batchClassModule{@link BatchClassModule}
	 */
	private void copyPlugins(BatchClassModule batchClassModule) {
		List<BatchClassPlugin> batchClassPlugins = batchClassModule.getBatchClassPlugins();
		List<BatchClassPlugin> newBatchClassPluginsList = new ArrayList<BatchClassPlugin>();
		for (BatchClassPlugin batchClassPlugin : batchClassPlugins) {
			newBatchClassPluginsList.add(batchClassPlugin);
			batchClassPlugin.setId(0);
			batchClassPlugin.setBatchClassModule(null);
			copyPluginConfigs(batchClassPlugin);
			copyDynamicPluginConfigs(batchClassPlugin);
		}
		batchClassModule.setBatchClassPlugins(newBatchClassPluginsList);
	}

	/**
	 * API to copy PluginConfigs of batch class.
	 * 
	 * @param batchClassPlugin{@link BatchClassPlugin}
	 */
	private void copyPluginConfigs(BatchClassPlugin batchClassPlugin) {
		List<BatchClassPluginConfig> batchClassPluginConfigs = batchClassPlugin.getBatchClassPluginConfigs();
		List<BatchClassPluginConfig> newBatchClassPluginConfigsList = new ArrayList<BatchClassPluginConfig>();
		for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
			newBatchClassPluginConfigsList.add(batchClassPluginConfig);
			batchClassPluginConfig.setId(0);
			batchClassPluginConfig.setBatchClassPlugin(null);
			// copyBatchClassPluginConfigsChild(batchClassPluginConfig);
			copyKVPageProcess(batchClassPluginConfig);
		}
		batchClassPlugin.setBatchClassPluginConfigs(newBatchClassPluginConfigsList);
	}

	/**
	 * API to copy DynamicPluginConfigs of batch class.
	 * 
	 * @param batchClassPlugin{@link BatchClassPlugin}
	 */
	private void copyDynamicPluginConfigs(BatchClassPlugin batchClassPlugin) {
		List<BatchClassDynamicPluginConfig> batchClassDynamicPluginConfigs = batchClassPlugin.getBatchClassDynamicPluginConfigs();
		List<BatchClassDynamicPluginConfig> newBatchClassDynamicPluginConfigsList = new ArrayList<BatchClassDynamicPluginConfig>();
		for (BatchClassDynamicPluginConfig batchClassDynamicPluginConfig : batchClassDynamicPluginConfigs) {
			newBatchClassDynamicPluginConfigsList.add(batchClassDynamicPluginConfig);
			batchClassDynamicPluginConfig.setId(0);
			batchClassDynamicPluginConfig.setBatchClassPlugin(null);
			copyBatchClassDynamicPluginConfigsChild(batchClassDynamicPluginConfig);
		}
		batchClassPlugin.setBatchClassDynamicPluginConfigs(newBatchClassDynamicPluginConfigsList);
	}

	/**
	 * API to copy KVPageProcess of batch class.
	 * 
	 * @param batchClassPluginConfig{@link BatchClassPluginConfig}
	 */
	private void copyKVPageProcess(BatchClassPluginConfig batchClassPluginConfig) {
		List<KVPageProcess> kvPageProcess = batchClassPluginConfig.getKvPageProcesses();
		List<KVPageProcess> newKvPageProcessList = new ArrayList<KVPageProcess>();
		for (KVPageProcess kVPageProcessChild : kvPageProcess) {
			kVPageProcessChild.setId(0);
			newKvPageProcessList.add(kVPageProcessChild);
			kVPageProcessChild.setBatchClassPluginConfig(null);
		}
		batchClassPluginConfig.setKvPageProcesses(newKvPageProcessList);
	}

	/**
	 * API to copy DynamicPluginConfigsChild.
	 * 
	 * @param batchClassDynamicPluginConfig {@link BatchClassDynamicPluginConfig}
	 */
	private void copyBatchClassDynamicPluginConfigsChild(BatchClassDynamicPluginConfig batchClassDynamicPluginConfig) {
		List<BatchClassDynamicPluginConfig> children = batchClassDynamicPluginConfig.getChildren();
		List<BatchClassDynamicPluginConfig> newChildrenList = new ArrayList<BatchClassDynamicPluginConfig>();
		if (children != null && !children.isEmpty()) {
			for (BatchClassDynamicPluginConfig child : children) {
				child.setId(0);
				newChildrenList.add(child);
				child.setBatchClassPlugin(null);
				child.setParent(null);
			}
			batchClassDynamicPluginConfig.setChildren(newChildrenList);
		}
	}

	/**
	 * API to copy ModuleConfig.
	 * 
	 * @param batchClassModule{@link BatchClassModule}
	 */
	private void copyModuleConfig(BatchClassModule batchClassModule) {
		List<BatchClassModuleConfig> batchClassModConfigs = batchClassModule.getBatchClassModuleConfig();
		List<BatchClassModuleConfig> newBatchClassModuleConfigList = new ArrayList<BatchClassModuleConfig>();
		for (BatchClassModuleConfig batchClassModConfig : batchClassModConfigs) {
			newBatchClassModuleConfigList.add(batchClassModConfig);
			batchClassModConfig.setId(0);
			batchClassModConfig.setBatchClassModule(null);
		}
		batchClassModule.setBatchClassModuleConfig(newBatchClassModuleConfigList);
	}

	/**
	 * API to fetch the system folder for the given batch class identifier.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @return {@link String} the system folder path.
	 */
	@Override
	public String getSystemFolderForBatchClassIdentifier(String batchClassIdentifier) {
		LOGGER.info("Retrieving system folder for batch class with identifier: " + batchClassIdentifier);
		return batchClassDao.getSystemFolderForBatchClassIdentifier(batchClassIdentifier);
	}
	
	/**
	 * This API will fetch the batch classes on user role if any.
	 * 
	 * @param userRoles Set<String>
	 * @param batchClassID String
	 * @return (@link BatchClass)
	 */
	@Override
	public BatchClass getBatchClassByUserRoles(Set<String> userRoles, String batchClassID) {
		return batchClassDao.getBatchClassByUserRoles(userRoles, batchClassID);
	}

	/**
	 * API to get all the UNC folders on the basis of excluding the deleted batch class.
	 * 
	 * @param isExcludeDeleted boolean
	 * @return List<{@link String}>
	 */
	@Override
	public List<String> getAllUNCList(boolean isExcludeDeleted) {
	return batchClassDao.getAllUNCList(isExcludeDeleted);
	}


}
