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

package com.ephesoft.dcma.workflow.service.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.jbpm.api.NewDeployment;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.common.ModuleJpdlPluginCreationInfo;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchClassPlugin;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.workflow.constant.WorkFlowConstants;
import com.ephesoft.dcma.workflow.service.GridWorkflowCreationService;
import com.ephesoft.dcma.workflow.service.WorkflowCreationService;

/**
 * This class is used to initializing the deployment services. It is used deploy/undeploy batch class.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.workflow.service.common.DeploymentService
 * 
 */
public class DeploymentServiceImpl implements DeploymentService {

	/**
	 * Logger object used to log various log messages.
	 */
	protected static final Logger LOGGER = LoggerFactory.getLogger(DeploymentServiceImpl.class);

	/**
	 * Repository service object: {@link RepositoryService}.
	 */
	@Autowired
	private RepositoryService repositoryService;

	/**
	 * Workflow creation service: {@link WorkflowCreationService}.
	 */
	@Autowired
	private WorkflowCreationService workflowCreationService;

	/**
	 * Grid workflow creation service for grid computing batch classes: {@link GridWorkflowCreationService}.
	 */
	@Autowired
	private GridWorkflowCreationService gridWorkflowCreationService;

	/**
	 * {@link List}<{@link String}> list of jpdl files location to be deployed initially.
	 */
	private List<String> workflowsDefinitionList;

	/**
	 * {@link Boolean} whether or not the workflow should be deployed again.
	 */
	private Boolean workflowDeploy;

	/**
	 * {@link String} common path for storing the Newly created JPDLs for various components.
	 */
	private String newWorkflowsBasePath;

	/**
	 * This method is used to deploy given batch class.
	 * 
	 * @param batchClass {@link BatchClass}
	 */
	@Override
	public void deploy(BatchClass batchClass) {
		throw new UnsupportedOperationException("Operation not supported.");
	}

	/**
	 * This method is used to undeploy given batch class.
	 * 
	 * @param batchClass {@link BatchClass}
	 */
	@Override
	public void undeploy(BatchClass batchClass) {
		throw new UnsupportedOperationException("Operation not supported.");
	}

	/**
	 * API to check if a workflow is deployed.
	 * 
	 * @param activityName
	 * @return
	 */
	@Override
	public boolean isDeployed(String activityName) {
		List<ProcessDefinition> deployment = repositoryService.createProcessDefinitionQuery().processDefinitionKey(activityName)
				.list();
		boolean isWorkFlowDeployed = false;
		if (deployment != null && deployment.size() > 0) {
			isWorkFlowDeployed = true;
		}
		return isWorkFlowDeployed;
	}

	/**
	 * This method is used to deploy all batch classes.
	 */
	@Override
	@PostConstruct
	public void deployAll() {

		if (!workflowDeploy) {
			return;
		}

		ClassPathResource workflowsClassPathResource = new ClassPathResource(WorkFlowConstants.META_INF_DCMA_WORKFLOWS);
		File workflowDirectory = null;
		File newWorkflowFolder = null;
		try {
			// Deploy new plugins here
			workflowDirectory = workflowsClassPathResource.getFile();
			newWorkflowFolder = new File(newWorkflowsBasePath);
			String subDirectoryName = WorkFlowConstants.PLUGINS_CONSTANT;
			LOGGER.info("Deploying new plugins.");
			deployFilesFromWorkflowSubdirectory(newWorkflowFolder, subDirectoryName);
			// workflowDirectory.
			for (String processDefinition : workflowsDefinitionList) {
				NewDeployment deployment = repositoryService.createDeployment();
				deployment.addResourceFromUrl(new ClassPathResource(processDefinition).getURL());

				deployment.deploy();
			}
			// Deploy new Modules followed by new workflows here

			subDirectoryName = WorkFlowConstants.MODULES_CONSTANT;
			LOGGER.info("Deploying new batch class modules.");
			deployFilesFromWorkflowSubdirectory(newWorkflowFolder, subDirectoryName);

			subDirectoryName = WorkFlowConstants.WORKFLOWS_CONSTANT;
			LOGGER.info("Deploying new batch classes.");
			deployFilesFromWorkflowSubdirectory(newWorkflowFolder, subDirectoryName);

		} catch (IOException e) {
			LOGGER.error("IOException occurred: " + ExceptionUtils.getFullStackTrace(e), e);
			throw new DCMABusinessException("An error occured while trying to deploy a process definition", e);
		}

		try {
			File propertyFile = new File(workflowDirectory.getAbsolutePath() + File.separator
					+ WorkFlowConstants.DCMA_WORKFLOWS_PROPERTIES);
			Map<String, String> propertyMap = new HashMap<String, String>();
			propertyMap.put(WorkFlowConstants.WORKFLOW_DEPLOY, Boolean.toString(false));

			String comments = "Workflow deploy property changed.";

			FileUtils.updateProperty(propertyFile, propertyMap, comments);

		} catch (IOException e) {
			LOGGER.error("IOException occurred: " + ExceptionUtils.getFullStackTrace(e), e);
			// Continuing without throwing exception.
		}

	}

	private void deployFilesFromWorkflowSubdirectory(File workflowDirectory, String subDirectoryName) {
		final String workflowDirectoryPath = workflowDirectory.getAbsolutePath();
		File workflowSubDirectory = new File(workflowDirectoryPath + File.separator + subDirectoryName);
		LOGGER.info("Deploying files from workflow subdirectory: " + workflowSubDirectory.getAbsolutePath());
		if (workflowSubDirectory.exists()) {
			List<String> jpdlFilePaths = new ArrayList<String>();
			try {
				jpdlFilePaths = FileUtils.listDirectory(workflowSubDirectory, false);
				for (String jpdlPath : jpdlFilePaths) {
					StringBuilder jpdlPathBuilder = new StringBuilder();
					jpdlPathBuilder.append(workflowDirectoryPath);
					jpdlPathBuilder.append(File.separator);
					jpdlPathBuilder.append(jpdlPath);
					jpdlPath = jpdlPathBuilder.toString();
					deploy(jpdlPath);
				}
			} catch (IOException e) {
				LOGGER.error("Error while deploying " + e.getMessage(), e);
			}
		}
	}

	/**
	 * API to deploy a JPDL existing on the given process definition path.
	 * 
	 * @param processDefinition {@link String}
	 */
	@Override
	public void deploy(String processDefinition) {
		LOGGER.info("Deploying process at " + processDefinition);
		NewDeployment deployment = repositoryService.createDeployment();
		try {

			LOGGER.info("Adding resource file to the new deployment");

			File file = new File(processDefinition);
			deployment.addResourceFromFile(file);

			LOGGER.info("Deploying process");
			deployment.deploy();
		} catch (Exception e) {
			LOGGER.error("Error while deploying " + e.getMessage(), e);
		}
	}

	/**
	 * To set Workflow Definition List.
	 * @param workflowsDefinitionList List<String>
	 */
	public void setWorkflowsDefinitionList(List<String> workflowsDefinitionList) {
		this.workflowsDefinitionList = workflowsDefinitionList;
	}

	/**
	 * To set Workflow Deploy.
	 * @param workflowDeploy Boolean
	 */
	public void setWorkflowDeploy(Boolean workflowDeploy) {
		this.workflowDeploy = workflowDeploy;
	}

	/**
	 * API to create and deploy workflow for a batch class.
	 * 
	 * @param batchClass {@link BatchClass}
	 */
	@Override
	public void createAndDeployBatchClassJpdl(BatchClass batchClass) {
		Map<String, List<String>> moduleToPluginMap = getModuleToPluginMap(batchClass);
		String workflowProcessDefinitionPath = WorkFlowConstants.EMPTY_STRING;
		List<String> moduleProcessDefinitionPaths = new ArrayList<String>(0);

		List<String> moduleNames = new ArrayList<String>(moduleToPluginMap.keySet());

		File workflowDirectory = null;
		try {
			workflowDirectory = new File(newWorkflowsBasePath);
			if (!workflowDirectory.exists()) {
				workflowDirectory.mkdir();
			}

			String workflowName = batchClass.getIdentifier();
			StringBuilder newWorkflowDirectoryPathBuilder = new StringBuilder();
			newWorkflowDirectoryPathBuilder.append(workflowDirectory.getAbsolutePath());
			newWorkflowDirectoryPathBuilder.append(File.separator);
			newWorkflowDirectoryPathBuilder.append(WorkFlowConstants.WORKFLOWS_CONSTANT);
			newWorkflowDirectoryPathBuilder.append(File.separator);
			newWorkflowDirectoryPathBuilder.append(workflowName);
			File newWorkflowDirectory = new File(newWorkflowDirectoryPathBuilder.toString());
			LOGGER.info("Creating New workflow directory " + newWorkflowDirectory.getPath() + " if it does not exist");
			newWorkflowDirectory.mkdirs();

			StringBuilder newModulesDirectoryPathBuilder = new StringBuilder();
			newModulesDirectoryPathBuilder.append(workflowDirectory.getAbsolutePath());
			newModulesDirectoryPathBuilder.append(File.separator);
			newModulesDirectoryPathBuilder.append(WorkFlowConstants.MODULES_CONSTANT);
			newModulesDirectoryPathBuilder.append(File.separator);
			newModulesDirectoryPathBuilder.append(workflowName);
			File newModulesDirectory = new File(newModulesDirectoryPathBuilder.toString());

			LOGGER.info("Creating New modules directory " + newModulesDirectory.getPath() + " if it does not exist");
			newModulesDirectory.mkdirs();

			// List<ModuleJpdlPluginCreationInfo> customJpdlCreationInfos = new ArrayList<ModuleJpdlPluginCreationInfo>();

			moduleProcessDefinitionPaths = createModuleJpdls(batchClass, moduleNames, workflowDirectory, workflowName,
					newModulesDirectory);
			LOGGER.info("Creating JPDL for Batch Class identifier: " + workflowName);
			final String jpdlRelativePath = createWorkflowJpdl(batchClass, moduleNames, newWorkflowDirectory);
			StringBuilder jpdlPathBuilder = new StringBuilder();
			jpdlPathBuilder.append(workflowDirectory.getPath());
			jpdlPathBuilder.append(File.separator);
			jpdlPathBuilder.append(WorkFlowConstants.WORKFLOWS_CONSTANT);
			jpdlPathBuilder.append(File.separator);
			jpdlPathBuilder.append(workflowName);
			jpdlPathBuilder.append(File.separator);
			jpdlPathBuilder.append(jpdlRelativePath);
			workflowProcessDefinitionPath = jpdlPathBuilder.toString();

			LOGGER.info("Adding workflow jpdl path to the list of jpdl paths.");
			moduleProcessDefinitionPaths.add(workflowProcessDefinitionPath);
			// Deploy Module JPDLs
			for (String path : moduleProcessDefinitionPaths) {
				LOGGER.info("Deploying the JPDL for " + path.substring(path.lastIndexOf(File.separator)) + " module/workflow");
				deploy(path);
			}

		} catch (Exception e) {
			LOGGER.error("Exception " + e.getMessage(), e);
		}
	}


	private String createWorkflowJpdl(BatchClass batchClass, List<String> moduleNames, File newWorkflowDirectory) {
		List<ModuleJpdlPluginCreationInfo> customJpdlCreationInfos = new ArrayList<ModuleJpdlPluginCreationInfo>();
		LOGGER.info("Adding " + WorkFlowConstants.WORKFLOW_STATUS_RUNNING + " to the begining of the workflow");
		moduleNames.add(0, WorkFlowConstants.WORKFLOW_STATUS_RUNNING);

		LOGGER.info("Adding " + WorkFlowConstants.WORKFLOW_STATUS_FINISHED + " to the end of the workflow");
		moduleNames.add(moduleNames.size(), WorkFlowConstants.WORKFLOW_STATUS_FINISHED);

		customJpdlCreationInfos.clear();
		for (String moduleName : moduleNames) {
			// For modules use only subprocess name constructor
			customJpdlCreationInfos.add(new ModuleJpdlPluginCreationInfo(moduleName));
		}

		ModuleJpdlPluginCreationInfo workflowJpdlCreationInfo = new ModuleJpdlPluginCreationInfo(batchClass.getName());
		/* Create JPDL for the workflow */
		final String jpdlRelativePath = gridWorkflowCreationService.writeJPDL(newWorkflowDirectory.getPath(),
				workflowJpdlCreationInfo, customJpdlCreationInfos, true);
		return jpdlRelativePath;
	}

	private List<String> createModuleJpdls(BatchClass batchClass, List<String> moduleNames, File workflowDirectory,
			String workflowName, File newModulesDirectory) {
		List<String> moduleProcessDefinitionPaths = new ArrayList<String>(0);
		List<ModuleJpdlPluginCreationInfo> customJpdlCreationInfos = new ArrayList<ModuleJpdlPluginCreationInfo>();
		/* Create JPDL for each module */
		for (String module : moduleNames) {

			// Create customJpdlCreationInfo objects for plug-ins
			customJpdlCreationInfos.clear();
			LOGGER.info("Converting " + module + "Batch class module object to the List CustomJpdlCreationInfo object for plugins");
			customJpdlCreationInfos = getCustomJpdlCreationInfos(batchClass, module);

			LOGGER.info("Creating JPDL for new module");
			ModuleJpdlPluginCreationInfo moduleJpdlCreationInfo = new ModuleJpdlPluginCreationInfo(module);
			String moduleProcessDefinitionPath = workflowCreationService.writeJPDL(newModulesDirectory.getPath(),
					moduleJpdlCreationInfo, customJpdlCreationInfos, false);
			if (!moduleProcessDefinitionPath.isEmpty()) {
				StringBuilder moduleJpdlPathBuilder = new StringBuilder();
				moduleJpdlPathBuilder.append(workflowDirectory.getPath());
				moduleJpdlPathBuilder.append(File.separator);
				moduleJpdlPathBuilder.append(WorkFlowConstants.MODULES_CONSTANT);
				moduleJpdlPathBuilder.append(File.separator);
				moduleJpdlPathBuilder.append(workflowName);
				moduleJpdlPathBuilder.append(File.separator);
				moduleJpdlPathBuilder.append(moduleProcessDefinitionPath);
				moduleProcessDefinitionPaths.add(moduleJpdlPathBuilder.toString());
			}
		}
		return moduleProcessDefinitionPaths;
	}

	private Map<String, List<String>> getModuleToPluginMap(BatchClass batchClass) {
		LOGGER.info("Getting module to plugin map in sorted order..for batch class id: " + batchClass.getIdentifier());
		Map<String, List<String>> moduleToPluginMap = new LinkedHashMap<String, List<String>>();

		try {
			List<BatchClassModule> batchClassModules = new ArrayList<BatchClassModule>(batchClass.getBatchClassModules());

			LOGGER.info("Sorting batch class modules for batch class id: " + batchClass.getIdentifier());

			sortBatchClassModulesListByOrderNumber(batchClassModules);
			// Sort modules
			for (BatchClassModule batchClassModule : batchClassModules) {
				List<String> pluginNames = new ArrayList<String>();
				List<BatchClassPlugin> batchClassPluginsList = batchClassModule.getBatchClassPlugins();
				List<BatchClassPlugin> batchClassPlugins = null;
				if (batchClassPluginsList == null) {
					LOGGER.info("Empty module added. " + batchClassModule.getWorkflowName());
					batchClassPlugins = new ArrayList<BatchClassPlugin>();
				} else {
					batchClassPlugins = new ArrayList<BatchClassPlugin>(batchClassPluginsList);
				}

				LOGGER.info("Sorting batch class plugins for for batch class module id: " + batchClassModule.getId());

				sortBatchClassPluginsListByOrderNumber(batchClassPlugins);
				// Sort plug-ins

				for (BatchClassPlugin batchClassPlugin : batchClassPlugins) {
					// populates the list of plug-ins for a module
					LOGGER.info("Populating the " + batchClassPlugin.getPlugin().getPluginName() + " plugin info into the map");
					pluginNames.add(batchClassPlugin.getPlugin().getWorkflowName());
				}
				// Entry for each module with its list of plug-ins
				moduleToPluginMap.put(batchClassModule.getWorkflowName(), pluginNames);

			}
		} catch (Exception e) {
			LOGGER.error("Exception: " + e.getMessage(), e);
		}
		return moduleToPluginMap;
	}

	private void sortBatchClassModulesListByOrderNumber(List<BatchClassModule> batchClassModulesList) {
		Collections.sort(batchClassModulesList, new Comparator<BatchClassModule>() {

			@Override
			public int compare(BatchClassModule batchClassModule1, BatchClassModule batchClassModule2) {
				int result;
				Integer orderNumberOne = batchClassModule1.getOrderNumber();
				Integer orderNumberTwo = batchClassModule2.getOrderNumber();
				if (orderNumberOne != null && orderNumberTwo != null) {
					result = orderNumberOne.compareTo(orderNumberTwo);
				} else if (orderNumberOne == null && orderNumberTwo == null) {
					result = 0;
				} else if (orderNumberOne == null) {
					result = -1;
				} else {
					result = 1;
				}
				return result;
			}
		});
	}

	private void sortBatchClassPluginsListByOrderNumber(List<BatchClassPlugin> batchClassPluginsList) {
		Collections.sort(batchClassPluginsList, new Comparator<BatchClassPlugin>() {

			@Override
			public int compare(BatchClassPlugin batchClassPlugin1, BatchClassPlugin batchClassPlugin2) {
				int result;
				Integer orderNumberOne = batchClassPlugin1.getOrderNumber();
				Integer orderNumberTwo = batchClassPlugin2.getOrderNumber();
				if (orderNumberOne != null && orderNumberTwo != null) {
					result = orderNumberOne.compareTo(orderNumberTwo);
				} else if (orderNumberOne == null && orderNumberTwo == null) {
					result = 0;
				} else if (orderNumberOne == null) {
					result = -1;
				} else {
					result = 1;
				}
				return result;
			}
		});
	}

	private List<ModuleJpdlPluginCreationInfo> getCustomJpdlCreationInfos(BatchClass batchClass, String moduleName) {
		List<ModuleJpdlPluginCreationInfo> customJpdlCreationInfos = new ArrayList<ModuleJpdlPluginCreationInfo>();

		for (BatchClassPlugin pluginDetails : batchClass.getBatchClassModuleByWorkflowName(moduleName).getBatchClassPlugins()) {

			String subProcessName = WorkFlowConstants.EMPTY_STRING;
			String scriptName = WorkFlowConstants.EMPTY_STRING;
			String backUpFileName = WorkFlowConstants.EMPTY_STRING;
			boolean isScriptingPlugin = false;
			String subProcessKey = WorkFlowConstants.EMPTY_STRING;

			subProcessKey = pluginDetails.getPlugin().getWorkflowName();
			LOGGER.info("Preparing JPDL creation info for " + subProcessKey + " plugin.");
			subProcessName = checkForPluginMultipleInstance(customJpdlCreationInfos, subProcessKey);
			String pluginScriptName = pluginDetails.getPlugin().getScriptName();
			if (pluginScriptName == null) {
				isScriptingPlugin = false;
				scriptName = WorkFlowConstants.EMPTY_STRING;
				backUpFileName = WorkFlowConstants.EMPTY_STRING;
				LOGGER.info(subProcessKey + " plugin is not a scripting plugin.");
			} else {
				final List<BatchClassPluginConfig> batchClassPluginConfigs = pluginDetails.getBatchClassPluginConfigs();
				for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
					if (batchClassPluginConfig.getPluginConfig().getName().equals("script.backup.name")) {
						backUpFileName = batchClassPluginConfig.getValue();
					}
					if (batchClassPluginConfig.getPluginConfig().getName().equals("script.file.name")) {
						scriptName = batchClassPluginConfig.getValue();
					}

				}
				isScriptingPlugin = true;
				scriptName = pluginScriptName;
				backUpFileName = subProcessKey;
				LOGGER.info(subProcessKey + " plugin is a scripting plugin.");
			}

			customJpdlCreationInfos.add(new ModuleJpdlPluginCreationInfo(subProcessName, isScriptingPlugin, scriptName,
					backUpFileName, subProcessKey));
		}

		return customJpdlCreationInfos;
	}

	private String checkForPluginMultipleInstance(List<ModuleJpdlPluginCreationInfo> customJpdlCreationInfos, String subProcessKey) {
		String subProcessName = subProcessKey;
		List<String> jpdlConfigSubProcessname = new ArrayList<String>();
		LOGGER.info("Creating list of already used sub process names.");
		for (ModuleJpdlPluginCreationInfo moduleJpdlPluginCreationInfo : customJpdlCreationInfos) {
			jpdlConfigSubProcessname.add(moduleJpdlPluginCreationInfo.getSubProcessName());
		}
		subProcessName = generateWorkflowName(subProcessName, jpdlConfigSubProcessname);
		LOGGER.info("Using the " + subProcessName + " sub process name for " + subProcessKey + " sub process key.");
		return subProcessName;
	}

	/**
	 * @param subProcessName
	 * @param jpdlConfigSubProcessname
	 * @return
	 */
	private String generateWorkflowName(String subProcessName, List<String> jpdlConfigSubProcessname) {
		boolean nameAlreadyInUse = false;
		int index = 0;
		nameAlreadyInUse = jpdlConfigSubProcessname.contains(subProcessName);

		while (nameAlreadyInUse) {
			index++;
			String tempSubProcessName = subProcessName + WorkFlowConstants.UNDERSCORE_SYMBOL + index;
			nameAlreadyInUse = jpdlConfigSubProcessname.contains(tempSubProcessName);
			LOGGER.info(tempSubProcessName + " sub process name already in use ");
		}
		return subProcessName;
	}

	/**
	 * To get New Workflow Base Path.
	 * @return the newWorkflowsBasePath
	 */
	public String getNewWorkflowsBasePath() {
		return newWorkflowsBasePath;
	}

	/**
	 * To set New Workflow Base Path.
	 * @param newWorkflowsBasePath String
	 */
	public void setNewWorkflowsBasePath(String newWorkflowsBasePath) {
		this.newWorkflowsBasePath = newWorkflowsBasePath;
	}
}
