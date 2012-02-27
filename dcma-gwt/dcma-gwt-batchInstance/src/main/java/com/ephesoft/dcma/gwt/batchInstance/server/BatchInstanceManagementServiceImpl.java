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

package com.ephesoft.dcma.gwt.batchInstance.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import org.springframework.core.io.ClassPathResource;

import com.ephesoft.dcma.batch.service.BatchInstancePluginPropertiesService;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.core.threadpool.ThreadPool;
import com.ephesoft.dcma.da.dao.hibernate.BatchClassGroupsDaoImpl;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchClassPlugin;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.RemoteBatchInstance;
import com.ephesoft.dcma.da.property.BatchInstanceFilter;
import com.ephesoft.dcma.da.property.BatchInstanceProperty;
import com.ephesoft.dcma.da.property.BatchPriority;
import com.ephesoft.dcma.da.service.BatchClassModuleService;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.gwt.batchInstance.client.BatchInstanceManagementService;
import com.ephesoft.dcma.gwt.batchInstance.client.i18n.BatchInstanceConstants;
import com.ephesoft.dcma.gwt.batchInstance.client.presenter.BatchInstancePresenter.Results;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.server.DCMARemoteServiceServlet;
import com.ephesoft.dcma.gwt.core.shared.BatchInstanceDTO;
import com.ephesoft.dcma.gwt.core.shared.DataFilter;
import com.ephesoft.dcma.gwt.core.shared.RemoteBatchInstanceDTO;
import com.ephesoft.dcma.gwt.core.shared.exception.GWTException;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.workflow.service.JbpmService;
import com.ephesoft.dcma.workflow.service.common.WorkflowService;

public class BatchInstanceManagementServiceImpl extends DCMARemoteServiceServlet implements BatchInstanceManagementService {

	private static final long serialVersionUID = 1L;
	private static final String INPUT_BATCH_XML = "backup.input_batch_xml";
	private static final String OUTPUT_BATCH_XML = "backup.output_batch_xml";
	private static final String INPUT_BATCH_XML_ZIP = "backup.input_batch_xml_zip";

	private static final String BACKUP_PROPERTY_FILE = "META-INF" + File.separator + "dcma-util" + File.separator
			+ "dcma-backup-service.properties";
	private static final String SCRIPTING_PLUGIN = "Scripting_Plugin";

	@Override
	public List<BatchInstanceDTO> getBatchInstanceDTOs(final int startRow, final int rowsCount, final List<DataFilter> filters,
			final Order order) {
		List<Order> orderList = null;
		orderList = new ArrayList<Order>();
		if (order != null) {
			orderList = new ArrayList<Order>();
			orderList.add(order);
		} else {
			Order defaultOrder = new Order(BatchInstanceProperty.ID, false);
			orderList.add(defaultOrder);
		}
		List<BatchInstanceFilter> filterClauseList = null;
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		List<BatchInstance> batchInstanceList = null;
		List<BatchPriority> batchPriorities = getPriorityList(filters);
		List<BatchInstanceStatus> statusList = getStatusList(filters);
		Set<String> allBatchClassByUserRoles = getAllBatchClassByUserRoles();
		batchInstanceList = batchInstanceService.getBatchInstances(statusList, startRow, rowsCount, orderList, filterClauseList,
				batchPriorities, getUserName(), allBatchClassByUserRoles);

		BatchInstanceDTO batchInstanceDTO = null;
		ArrayList<BatchInstanceDTO> batches = new ArrayList<BatchInstanceDTO>();

		for (BatchInstance instance : batchInstanceList) {
			batchInstanceDTO = convertBatchInstanceToBatchInstanceDTO(instance);
			batches.add(batchInstanceDTO);
		}
		return batches;
	}

	private BatchInstanceDTO convertBatchInstanceToBatchInstanceDTO(BatchInstance instance) {
		BatchClass batchClass = instance.getBatchClass();
		Date date = instance.getLastModified();
		SimpleDateFormat sdf = new SimpleDateFormat(BatchInstanceConstants.DATE_FORMAT, Locale.getDefault());
		BatchInstanceDTO batchInstanceDTO = new BatchInstanceDTO();
		batchInstanceDTO.setPriority(instance.getPriority());
		batchInstanceDTO.setBatchIdentifier(instance.getIdentifier());
		batchInstanceDTO.setBatchName(instance.getBatchName());
		batchInstanceDTO.setBatchClassName(batchClass.getDescription());
		batchInstanceDTO.setUploadedOn(sdf.format(date));
		batchInstanceDTO.setNoOfDocuments(null);
		batchInstanceDTO.setExecutedModules(instance.getExecutedModules());
		batchInstanceDTO.setReviewStatus(null);
		batchInstanceDTO.setValidationStatus(null);
		batchInstanceDTO.setNoOfPages(null);
		batchInstanceDTO.setStatus(instance.getStatus().name());
		batchInstanceDTO.setCurrentUser(instance.getCurrentUser() != null ? instance.getCurrentUser() : "");
		batchInstanceDTO.setRemote(instance.isRemote());
		RemoteBatchInstanceDTO remoteBatchInstanceDTO = null;
		if (instance.getRemoteBatchInstance() != null) {
			remoteBatchInstanceDTO = new RemoteBatchInstanceDTO();
			RemoteBatchInstance remoteBatchInstance = instance.getRemoteBatchInstance();
			remoteBatchInstanceDTO.setRemoteBatchInstanceIdentifier(remoteBatchInstance.getRemoteBatchInstanceIdentifier());
			remoteBatchInstanceDTO.setRemoteURL(remoteBatchInstance.getRemoteURL());
			remoteBatchInstanceDTO.setPreviousRemoteBatchInstanceIdentifier(remoteBatchInstance
					.getPreviousRemoteBatchInstanceIdentifier());
			remoteBatchInstanceDTO.setPreviousRemoteURL(remoteBatchInstance.getPreviousRemoteURL());
			remoteBatchInstanceDTO.setSourceModule(instance.getRemoteBatchInstance().getSourceModule());
		}
		batchInstanceDTO.setRemoteBatchInstanceDTO(remoteBatchInstanceDTO);
		return batchInstanceDTO;
	}

	private List<BatchInstanceStatus> getStatusList(final List<DataFilter> filters) {

		List<BatchInstanceStatus> statusList = new ArrayList<BatchInstanceStatus>();
		statusList.clear();
		boolean found = false;
		if (filters.isEmpty()) {
			statusList.addAll(getStatusFilter("-2"));
		} else {
			for (DataFilter filter : filters) {
				if ((BatchInstanceConstants.STATUS).equals(filter.getColumn())) {
					String value = filter.getValue();
					List<BatchInstanceStatus> statusFilter = getStatusFilter(value);
					statusList.addAll(statusFilter);
					found = true;
				}
			}
			if (!found) {
				statusList.addAll(getStatusFilter("-2"));
			}
		}
		return statusList;
	}

	private List<BatchPriority> getPriorityList(final List<DataFilter> filters) {

		List<BatchPriority> batchPriorities = new ArrayList<BatchPriority>();
		batchPriorities.clear();
		if (!filters.isEmpty()) {
			for (DataFilter filter : filters) {
				if ((BatchInstanceConstants.PRIORITY).equals(filter.getColumn())) {
					batchPriorities.add(getPriorityValue(filter));
				}
			}
		}
		return batchPriorities;
	}

	private List<BatchInstanceStatus> getStatusFilter(final String value) {

		List<BatchInstanceStatus> batchInstanceStatus = new ArrayList<BatchInstanceStatus>();
		if (value != null) {
			int statusInt = -2;
			try {
				statusInt = Integer.parseInt(value);
			} catch (NumberFormatException e) {
			}
			BatchInstanceStatus[] statusArray = BatchInstanceStatus.values();
			if (statusInt == -2) {
				for (BatchInstanceStatus status : statusArray) {
					if (!(status.equals(BatchInstanceStatus.FINISHED) || status.equals(BatchInstanceStatus.DELETED))) {
						batchInstanceStatus.add(status);
					}
				}
			} else if (statusInt == -1) {
				for (BatchInstanceStatus status : statusArray) {
					batchInstanceStatus.add(status);
				}
			} else {
				for (BatchInstanceStatus status : statusArray) {
					if (status.getId().intValue() == statusInt) {
						batchInstanceStatus.add(status);
					}
				}
			}
		}
		return batchInstanceStatus;
	}

	private BatchPriority getPriorityValue(final DataFilter filter) {
		BatchPriority priorityValue = null;
		if (filter != null) {
			int priorityInt = Integer.parseInt(filter.getValue());
			BatchPriority[] priorities = BatchPriority.values();

			for (BatchPriority priority : priorities) {
				if (priority.getLowerLimit() != null && priority.getLowerLimit().intValue() == priorityInt) {
					priorityValue = priority;
				}
			}
		}
		return priorityValue;
	}

	@Override
	public Integer getRowCount(List<DataFilter> dataFilters) {
		List<BatchInstanceStatus> statusList = getStatusList(dataFilters);
		List<BatchPriority> batchPriorities = getPriorityList(dataFilters);
		int rowCount = 0;
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		Set<String> allBatchClassByUserRoles = getAllBatchClassByUserRoles();
		rowCount = batchInstanceService.getCount(statusList, batchPriorities, allBatchClassByUserRoles, getUserName());
		return rowCount;
	}

	@Override
	public Results deleteBatchInstance(String identifier) throws GWTException {
		Results deleteResult = Results.SUCCESSFUL;
		PluginPropertiesService pluginPropertiesService = this.getBeanByName("batchInstancePluginPropertiesService",
				BatchInstancePluginPropertiesService.class);
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(identifier);
		pluginPropertiesService.clearCache(identifier);
		try {
			JbpmService jbpmService = this.getSingleBeanOfType(JbpmService.class);
			jbpmService.deleteProcessInstance(batchInstance.getProcessInstanceKey());
			batchInstance.setStatus(BatchInstanceStatus.DELETED);
			batchInstanceService.updateBatchInstance(batchInstance);
			removeUncFolder(batchInstance);
		} catch (Exception e) {
			deleteResult = Results.FAILURE;
			throw new GWTException(e.getMessage());
		}
		return deleteResult;
	}

	private void removeUncFolder(BatchInstance batchInstance) {
		File uncFile = new File(batchInstance.getUncSubfolder());
		if (null != uncFile) {
			FileUtils.deleteDirectoryAndContentsRecursive(uncFile);
		}
	}

	private boolean removeFolders(BatchInstance batchInstance) throws IOException, DCMAApplicationException {
		File systemFolderFile = new File(batchInstance.getLocalFolder() + File.separator + batchInstance.getIdentifier());
		File propertiesFile = new File(batchInstance.getLocalFolder() + File.separator + BatchInstanceConstants.PROPERTIES_DIRECTORY
				+ File.separator + batchInstance.getIdentifier() + FileType.SER.getExtensionWithDot());
		boolean deleted = true;

		if (null != systemFolderFile) {
			deleted &= FileUtils.deleteDirectoryAndContents(systemFolderFile);
		}
		if (null != propertiesFile) {
			deleted &= propertiesFile.delete();
		}
		return deleted;
	}

	@Override
	public Results updateBatchInstanceStatus(String identifier, BatchInstanceStatus biStatus) throws GWTException {
		Results result = Results.FAILURE;
		try {
			if (identifier != null) {
				BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
				batchInstanceService.updateBatchInstanceStatusByIdentifier(identifier, BatchInstanceStatus.RESTART_IN_PROGRESS);
			}
		} catch (Exception e) {
			result = Results.FAILURE;
			log.error("Error in updating batch instance status" + e.getMessage(), e);
			throw new GWTException(e.getMessage());
		}

		return result;
	}

	@Override
	public Results restartBatchInstance(String identifier, String moduleName) throws GWTException {
		JbpmService jbpmService = null;
		Results result = Results.FAILURE;
		if (identifier != null) {
			BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
			BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
			BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			BatchClassModuleService batchClassModuleService = this.getSingleBeanOfType(BatchClassModuleService.class);
			BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(identifier);
			if (batchInstance != null && batchClassService != null && batchSchemaService != null && batchClassModuleService != null
					&& batchInstanceService != null) {
				result = processRestartingBatch(identifier, moduleName, jbpmService, batchInstanceService, batchClassService,
						batchSchemaService, batchClassModuleService, batchInstance, true);
			}
		}
		return result;
	}

	private Results processRestartingBatch(String identifier, String moduleName, JbpmService jbpmService,
			BatchInstanceService batchInstanceService, BatchClassService batchClassService, BatchSchemaService batchSchemaService,
			BatchClassModuleService batchClassModuleService, BatchInstance batchInstance, boolean throwException) throws GWTException {
		Results result;
		boolean isZipSwitchOn = batchSchemaService.isZipSwitchOn();
		log.info("Zipped Batch XML switch is:" + isZipSwitchOn);

		result = Results.SUCCESSFUL;
		try {
			jbpmService = this.getSingleBeanOfType(JbpmService.class);
			jbpmService.deleteProcessInstance(batchInstance.getProcessInstanceKey());
			BatchInstanceThread batchInstanceThread = ThreadPool.getBatchInstanceThreadList(identifier);
			if (batchInstanceThread != null) {
				batchInstanceThread.remove();
			}
			if (moduleName == null) {
				if (batchInstance.getRemoteBatchInstance() != null) {
					moduleName = batchInstance.getRemoteBatchInstance().getSourceModule();
				}
			}

			if (moduleName != null) {
				updateBatchFolders(batchInstance, moduleName, isZipSwitchOn);
			} else {
				deleteBatchFolder(batchInstance);
				moduleName = BatchInstanceConstants.FOLDER_IMPORT_MODULE;
			}

			WorkflowService workflowService = this.getSingleBeanOfType(WorkflowService.class);
			String activeModule = workflowService.getActiveModule(batchInstance);
			String executedModules = "";
			if (moduleName != null) {
				executedModules = batchInstance.getExecutedModules();
				if (executedModules != null) {
					String batchClassIdentifier = batchInstanceService.getBatchClassIdentifier(identifier);
					if (batchClassIdentifier != null) {
						BatchClassModule batchClassModuleItem = batchClassModuleService.getBatchClassModuleByWorkflowName(
								batchClassIdentifier, moduleName);
						BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdentifier);
						List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
						if (null != batchClassModules) {
							for (BatchClassModule batchClassModule : batchClassModules) {
								if (batchClassModule != null && batchClassModule.getModule() != null) {
									if (batchClassModule.getOrderNumber() >= batchClassModuleItem.getOrderNumber()) {
										String replaceText = batchClassModule.getModule().getId() + BatchInstanceConstants.SEMICOLON;
										executedModules = executedModules.replace(replaceText, BatchInstanceConstants.EMPTY_VALUE);
									}
								}
							}
						}
					}
				}
			} else {
				executedModules = "";
			}

			batchInstance = batchInstanceService.getBatchInstanceByIdentifier(identifier);
			batchInstance.setExecutedModules(executedModules);
			batchInstance = batchInstanceService.merge(batchInstance);

			if (activeModule != null && activeModule.contains(BatchInstanceConstants.WORKFLOW_CONTINUE_CHECK)) {
				log.error("Error in restarting batch instance : " + identifier);
				if (throwException) {
					throw new GWTException(LocaleDictionary.get().getConstantValue(BatchInstanceConstants.RESTART_ERROR_MESSAGE));
				}
			}
			workflowService.startWorkflow(batchInstance.getBatchInstanceID(), moduleName);
			result = Results.SUCCESSFUL;
		} catch (Exception e) {
			result = Results.FAILURE;
			log.error("Error in restarting batch instance " + identifier + e.getMessage(), e);

			// update the batch instance to ERROR state.
			if (jbpmService != null) {
				jbpmService.deleteProcessInstance(batchInstance.getProcessInstanceKey());
			}
			batchInstanceService.updateBatchInstanceStatusByIdentifier(batchInstance.getIdentifier(), BatchInstanceStatus.ERROR);
			if (throwException) {
				throw new GWTException(e.getMessage());
			}
		}
		return result;
	}

	private void deleteBatchFolder(BatchInstance batchInstance) {
		File batchInstanceFolder = new File(batchInstance.getLocalFolder() + File.separator + batchInstance.getIdentifier());
		if (batchInstanceFolder.exists()) {
			FileUtils.deleteDirectoryAndContents(batchInstanceFolder);
		}
	}

	private Properties fetchConfig() {

		ClassPathResource classPathResource = new ClassPathResource(BACKUP_PROPERTY_FILE);
		Properties properties = new Properties();

		InputStream input = null;
		try {
			input = classPathResource.getInputStream();
			properties.load(input);
		} catch (IOException ex) {
			log.error("Cannot open and load backUpService properties file.", ex);
			// System.exit(1);
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (IOException ex) {
				log.error("Cannot close backUpService properties file.", ex);
			}
		}
		return properties;
	}

	private void updateBatchFolders(BatchInstance batchInstance, String moduleName, boolean isZipSwitchOn) throws GWTException {
		Properties properties = fetchConfig();
		String batchXmlExtension = ICommonConstants.UNDERSCORE_BATCH_XML;
		if (properties != null) {
			batchXmlExtension = properties.getProperty(INPUT_BATCH_XML);
		}
		File batchInstanceFolder = new File(batchInstance.getLocalFolder() + File.separator + batchInstance.getIdentifier());
		String batchXmlPath = batchInstanceFolder.getAbsolutePath() + File.separator + batchInstance.getIdentifier()
				+ batchXmlExtension;

		File batchZipFile = null;
		File batchXmlFile = null;
		boolean isZip = false;
		if (isZipSwitchOn) {
			if (FileUtils.isZipFileExists(batchXmlPath)) {
				isZip = true;
			}
		} else {
			batchXmlFile = new File(batchXmlPath);
			if (batchXmlFile.exists()) {
				isZip = false;

			} else {
				isZip = true;
			}
		}

		log.info("isZip in restarting batch is : " + isZip);
		if (isZip) {
			batchXmlExtension = ICommonConstants.UNDERSCORE_BATCH_XML_ZIP;
			if (properties != null) {
				batchXmlExtension = properties.getProperty(INPUT_BATCH_XML_ZIP);
			}
			String batchZipPath = batchInstanceFolder.getAbsolutePath() + File.separator + batchInstance.getIdentifier()
					+ batchXmlExtension;
			batchZipFile = new File(batchZipPath);
			File backupXmlZipFile = new File(batchInstanceFolder.getAbsolutePath() + File.separator + batchInstance.getIdentifier()
					+ ICommonConstants.UNDERSCORE_BAK_BATCH_XML_ZIP);
			try {
				FileUtils.copyFile(batchZipFile, backupXmlZipFile);
			} catch (Exception e) {
				log.error("Unable to create backup copy of batch file for batch instance : " + batchInstance.getIdentifier());
				throw new GWTException("Unable to create backup copy of batch file for batch instance : "
						+ batchInstance.getIdentifier());
			}
		} else {
			batchXmlFile = new File(batchXmlPath);
			File backupXmlFile = new File(batchInstanceFolder.getAbsolutePath() + File.separator + batchInstance.getIdentifier()
					+ ICommonConstants.UNDERSCORE_BAK_BATCH_XML);
			try {
				if (batchXmlFile.exists()) {
					FileUtils.copyFile(batchXmlFile, backupXmlFile);
				}
			} catch (Exception e) {
				log.error("Unable to create backup copy of batch file for batch instance : " + batchInstance.getIdentifier());
				throw new GWTException("Unable to create backup copy of batch file for batch instance : "
						+ batchInstance.getIdentifier());
			}

		}

		List<BatchClassModule> batchClassModuleList = batchInstance.getBatchClass().getBatchClassModules();
		BatchClassModule currentBatchClassModule = null;
		for (BatchClassModule batchClassModule : batchClassModuleList) {
			if (moduleName.equalsIgnoreCase(batchClassModule.getWorkflowName())) {
				currentBatchClassModule = batchClassModule;
				break;
			}
		}
		if (currentBatchClassModule != null) {
			BatchClassModule previousBatchClassModule = null;
			for (BatchClassModule batchClassModule : batchClassModuleList) {
				if (previousBatchClassModule == null && batchClassModule.getOrderNumber() < currentBatchClassModule.getOrderNumber()) {
					previousBatchClassModule = batchClassModule;
				} else {
					if (previousBatchClassModule != null
							&& batchClassModule.getOrderNumber() > previousBatchClassModule.getOrderNumber()
							&& batchClassModule.getOrderNumber() < currentBatchClassModule.getOrderNumber()) {
						previousBatchClassModule = batchClassModule;
					}
				}
			}

			String batchBakXml = ICommonConstants.UNDERSCORE_BATCH_BAK_XML;
			if (properties != null) {
				batchBakXml = properties.getProperty(OUTPUT_BATCH_XML);
			}

			if (previousBatchClassModule != null) {
				BatchClassPlugin prevBatchClassPlugin = getLastPluginFor(previousBatchClassModule);
				String prevPluginFilePath = batchInstanceFolder.getAbsolutePath() + File.separator + batchInstance.getIdentifier()
						+ "_" + prevBatchClassPlugin.getPlugin().getWorkflowName() + batchBakXml;
				File prevPluginBatchXml = new File(prevPluginFilePath);

				if (!prevPluginBatchXml.exists() && !FileUtils.isZipFileExists(prevPluginFilePath)) {
					String prevPath = prevPluginBatchXml.getAbsolutePath();
					prevPluginFilePath = batchInstanceFolder.getAbsolutePath() + File.separator + batchInstance.getIdentifier() + "_"
							+ previousBatchClassModule.getWorkflowName() + "_" + SCRIPTING_PLUGIN + batchBakXml;
					prevPluginBatchXml = new File(prevPluginFilePath);
					if (!prevPluginBatchXml.exists() && !FileUtils.isZipFileExists(prevPluginFilePath)) {
						log.error("Unable to find backup batch xml for batch instance : " + batchInstance.getIdentifier()
								+ "with file : " + prevPluginBatchXml.getAbsolutePath() + "or " + prevPath);
						throw new GWTException("Unable to update batch xml for batch instance : " + batchInstance.getIdentifier()
								+ "with file : " + prevPluginBatchXml.getAbsolutePath() + "or " + prevPath);
					}
				}

				try {
					if (batchZipFile != null && batchZipFile.exists()) {
						prevPluginFilePath = prevPluginBatchXml + FileType.ZIP.getExtensionWithDot();
						FileUtils.copyFile(new File(prevPluginFilePath), batchZipFile);
					} else {
						FileUtils.copyFile(prevPluginBatchXml, batchXmlFile);
					}
				} catch (Exception e) {
					log.error("Unable to update batch xml for batch instance : " + batchInstance.getIdentifier() + "with file : "
							+ prevPluginBatchXml.getAbsolutePath());
					throw new GWTException("Unable to update batch xml for batch instance : " + batchInstance.getIdentifier()
							+ "with file : " + prevPluginBatchXml.getAbsolutePath());
				}
			}
		} else {
			log.error("Could not find restart option for batch instance : " + batchInstance.getIdentifier() + "restart option "
					+ moduleName);
			throw new GWTException("Could not find restart option for batch instance : " + batchInstance.getIdentifier()
					+ "restart option " + moduleName);
		}
	}

	private BatchClassPlugin getLastPluginFor(BatchClassModule previousBatchClassModule) {
		List<BatchClassPlugin> batchClassPlugins = previousBatchClassModule.getBatchClassPlugins();
		BatchClassPlugin lastBatchClassPlugin = null;
		for (BatchClassPlugin batchClassPlugin : batchClassPlugins) {
			if (lastBatchClassPlugin == null) {
				lastBatchClassPlugin = batchClassPlugin;
			} else {
				if (batchClassPlugin.getOrderNumber() > lastBatchClassPlugin.getOrderNumber()) {
					lastBatchClassPlugin = batchClassPlugin;
				}
			}
		}
		return lastBatchClassPlugin;
	}

	@Override
	public Integer[] getIndividualRowCount() {
		Integer[] resultList = new Integer[3];
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		Set<String> allBatchClassByUserRoles = getAllBatchClassByUserRoles();
		resultList[0] = batchInstanceService.getAllCount(getUserName(), allBatchClassByUserRoles);
		List<BatchInstanceStatus> batchInstanceStatus = new ArrayList<BatchInstanceStatus>();
		batchInstanceStatus.add(BatchInstanceStatus.DELETED);
		resultList[1] = batchInstanceService.getCount(batchInstanceStatus, null, true, getUserName(), allBatchClassByUserRoles);
		batchInstanceStatus.clear();
		batchInstanceStatus.add(BatchInstanceStatus.RESTARTED);
		resultList[2] = batchInstanceService.getCount(batchInstanceStatus, null, true, getUserName(), allBatchClassByUserRoles);
		return resultList;
	}

	/* The following function will fetch the list of BatchInstanceDTOs by matching batch name */
	@Override
	public List<BatchInstanceDTO> getBatchInstanceDTOs(String batchName) throws GWTException {
		List<BatchInstanceDTO> batchInstanceDTOs = new ArrayList<BatchInstanceDTO>();
		if (batchName != null && !batchName.isEmpty()) {
			BatchClassGroupsDaoImpl batchClassGroupsDaoImpl = this.getSingleBeanOfType(BatchClassGroupsDaoImpl.class);
			Set<String> batchClassList = batchClassGroupsDaoImpl.getBatchClassIdentifierForUsers(getUserRoles());
			BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
			List<BatchInstance> batchInstances = batchInstanceService.getBatchInstanceByBatchName(batchName);
			if (batchInstances != null && !batchInstances.isEmpty()) {
				for (BatchInstance batchInstance : batchInstances) {
					if (batchInstance != null) {
						BatchClass batchClass = batchInstance.getBatchClass();
						if (batchClassList != null) {
							if (batchClassList.contains(batchClass.getIdentifier())) {
								batchInstanceDTOs.add(convertBatchInstanceToBatchInstanceDTO(batchInstance));
							}
						}
					}
				}
			}
		}
		return batchInstanceDTOs;
	}

	/* The following function will fetch the BatchInstanceDTO by Batch Id */
	@Override
	public BatchInstanceDTO getBatchInstanceDTO(String identifier) throws GWTException {
		BatchInstanceDTO batchInstanceDTO = null;
		if (identifier != null && !identifier.isEmpty()) {

			BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
			BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(identifier);
			if (batchInstance != null) {
				BatchClass batchClass = batchInstance.getBatchClass();
				BatchClassGroupsDaoImpl batchClassGroupsDaoImpl = this.getSingleBeanOfType(BatchClassGroupsDaoImpl.class);
				Set<String> batchClassList = batchClassGroupsDaoImpl.getBatchClassIdentifierForUsers(getUserRoles());
				if (batchClassList != null) {
					if (batchClassList.contains(batchClass.getIdentifier())) {
						batchInstanceDTO = convertBatchInstanceToBatchInstanceDTO(batchInstance);
					}
				}
			}
		}
		return batchInstanceDTO;
	}

	@Override
	public Map<String, String> getRestartOptions(String batchInstanceIdentifier) {
		Map<String, String> moduleList = null;
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdentifier);
		WorkflowService workflowService = this.getSingleBeanOfType(WorkflowService.class);
		String activemodule = workflowService.getActiveModule(batchInstance);
		List<BatchClassModule> batchClassModuleList = batchInstance.getBatchClass().getBatchClassModules();
		BatchClassModule currentBatchClassModule = null;
		moduleList = new LinkedHashMap<String, String>();
		if (activemodule != null && !activemodule.contains(BatchInstanceConstants.FOLDER_IMPORT_MODULE)
				&& batchInstance.getExecutedModules() == null && batchInstance.getRemoteBatchInstance() == null) {
			for (BatchClassModule batchClassModule : batchClassModuleList) {
				if (activemodule.contains(batchClassModule.getWorkflowName())) {
					currentBatchClassModule = batchClassModule;
					break;
				}
			}
			for (BatchClassModule batchClassModule : batchClassModuleList) {
				if (currentBatchClassModule.getOrderNumber() > batchClassModule.getOrderNumber()) {
					moduleList.put(batchClassModule.getWorkflowName(), batchClassModule.getModule().getDescription());
				}
			}

		} else {
			String executedModuleIds = batchInstance.getExecutedModules();
			if (executedModuleIds != null) {
				StringTokenizer tokenizer = new StringTokenizer(executedModuleIds, BatchInstanceConstants.SEMICOLON);
				while (tokenizer.hasMoreTokens()) {
					String moduleId = tokenizer.nextToken();
					for (BatchClassModule batchClassModule : batchClassModuleList) {
						if (batchClassModule.getModule().getId() == Long.valueOf(moduleId).longValue()) {
							moduleList.put(batchClassModule.getWorkflowName(), batchClassModule.getModule().getDescription());
						}
					}
				}
			}
		}
		return moduleList;
	}

	@Override
	public Results deleteBatchFolders(String batchInstanceIdentifier) throws GWTException {
		Results deleteResult = Results.SUCCESSFUL;
		BatchInstanceThread batchInstanceThread = ThreadPool.getBatchInstanceThreadList(batchInstanceIdentifier);
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdentifier);
		try {
			if (batchInstanceThread != null) {
				batchInstanceThread.remove();
			}
			removeFolders(batchInstance);
		} catch (Exception e) {
			deleteResult = Results.FAILURE;
			throw new GWTException("Error while deleting the batch instance folders.");
		}
		return deleteResult;
	}

	@Override
	public void clearCurrentUser(String batchInstanceIdentifier) {
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		batchInstanceService.clearCurrentUser(batchInstanceIdentifier);
	}

	public void restartAllBatchInstances() {
		log.info("Restarting all batch instances of READY_FOR_REVIEW or READY_FOR_VALIDATION status.");
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		JbpmService jbpmService = this.getSingleBeanOfType(JbpmService.class);
		WorkflowService workflowService = this.getSingleBeanOfType(WorkflowService.class);
		BatchClassModuleService batchClassModuleService = this.getSingleBeanOfType(BatchClassModuleService.class);

		List<BatchInstanceStatus> batchStatusList = new ArrayList<BatchInstanceStatus>();
		batchStatusList.add(BatchInstanceStatus.READY_FOR_REVIEW);
		batchStatusList.add(BatchInstanceStatus.READY_FOR_VALIDATION);
		List<BatchInstance> batchInstanceList = batchInstanceService.getBatchInstanceByStatusList(batchStatusList);

		for (BatchInstance batchInstance : batchInstanceList) {
			String batchInstanceIdentifier = batchInstance.getIdentifier();
			log.info("Restarting batch instance : " + batchInstanceIdentifier);
			String activityName = workflowService.getActiveModule(batchInstance);
			int indexOf = activityName.indexOf('.');
			indexOf = indexOf == -1 ? activityName.length() : indexOf;
			String moduleName = activityName.substring(0, indexOf);
			try {
				processRestartingBatch(batchInstanceIdentifier, moduleName, jbpmService, batchInstanceService, batchClassService,
						batchSchemaService, batchClassModuleService, batchInstance, false);
			} catch (GWTException e) {
				log.error("Error while restarting batch instance: " + batchInstanceIdentifier);
			}
		}
	}
}
