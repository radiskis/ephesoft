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

package com.ephesoft.dcma.workflow.common;

import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.jbpm.api.ExecutionService;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;


/**
 * Spring bean instance of a JBPM4 process.<br>
 * This bean automatically connects to a {@link ProcessEngine}.
 * The process is automatically deployed when the bean is initialized and it undeploys the process before it is destroyed.
 * <p>
 * It provides convinience methods to JBPM4 resources like {@link ProcessEngine}, {@link ExecutionService} and {@link RepositoryService}.
 * <p>
 * It provides convinience methods to start an instances of the process. Multiple process instances can be running at the same time.
 * This bean does <b>not</b> maintain a list or state of started processes. That is the responsibility of JBPM. 
 * This bean does hold the process deployment id so it is not necessary to provide this when starting an process instance.
 * It is the responsibility of the calling code to do something with the {@link ProcessInstance} if it's necessary.
 * If needed a {@link ProcessInstance} can be looked by querying JBPM via a service.
 * 
 *
 */

public class JbpmProcessBean {

	private static final Logger logger = LoggerFactory.getLogger(JbpmProcessBean.class);
	
	// name of jpdl.xml file
	private String resourceName;
	
	// optional key for process as defined in jpdl.xml
	private String processKey;
	
	// deployment id of process
	private String deploymentId;
	
	// id of process definition of deployed process. Has format {key}-{version}.
	private String processDefinitionId;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private ExecutionService executionService;
	
	
	/**
	 * Deploy process when bean is initialized.
	 */
	@PostConstruct
	@Transactional
	public void initialize() {
		// deploy process
		logger.info(String.format("Deploying process with key %s ...",processKey));
		deploymentId = repositoryService.createDeployment()
				.addResourceFromClasspath(resourceName).deploy();

		// lookup processDefinitionId
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).uniqueResult();
		logger.debug(String.format("Deployed process with ID:%s, Name:%s, Key:%s, Version:%s", pd.getId(), pd.getName(), pd.getKey(), pd.getVersion()));
		processDefinitionId = pd.getId();
	}
	
	/**
	 * Unregister process when bean is destroyed.
	 */
	@PreDestroy
	public void destroy() {
		// delete deployed process
		logger.info(String.format("Deleting deployed process with key %s ...",processKey));
		repositoryService.deleteDeploymentCascade(deploymentId);
		logger.info("Process undeployed.");
	}
	
	/* 
	 * STARTING A PROCESS
	 */
	
	/**
	 * Start a new process.<br>
	 * If a processKey was provided in the Spring configuration, the key will be used to create a process. 
	 * Otherwise the deployment id is used.
	 * <p>
	 * @return Started {@link ProcessInstance}
	 */
	public ProcessInstance startProcessInstance() {
		return startProcessInstance(null, null);
	}
	
	/**
	 * Start a new process instance using a business key.<br>
	 * If a processKey was provided in the Spring configuration, the key will be used to create a process. 
	 * Otherwise the deployment id is used.
	 * <p>
	 * @param businessKey Custom key by which this process can be tracked
	 * @return Started {@link ProcessInstance}
	 */
	public ProcessInstance startProcessInstanceWithKey(String businessKey) {
		return startProcessInstance(null, businessKey);
	}
	
	/**
	 * Start a new process instance using variables.<br>
	 * If a processKey was provided in the Spring configuration, the key will be used to create a process. 
	 * Otherwise the deployment id is used.<br>
	 * @see See also {@link #startProcessInstanceWithKeyAndVariables(String, Map)}
	 * <p>
	 * @param variables Custom variables necessary for the process.
	 * @return Started {@link ProcessInstance}
	 */
	public ProcessInstance startProcessInstanceWithVariables(Map<String,Object> variables) {
		return startProcessInstance(variables, null);
	}
	
	/**
	 * Start a new process instance using a business key and variables.<br>
	 * If a processKey was provided in the Spring configuration, the key will be used to create a process. 
	 * Otherwise the deployment id is used.
	 * <p>
	 * NOTE: Because the process is running in a Spring environment, the process is able to lookup Spring managed beans.
	 * It is therefore not necessary to pass Spring components as variables to the process. It can be done however to override
	 * a Spring component since the variables Map is searched before the Spring environement.
	 * <p>
	 * @param businessKey Custom key by which this process can be tracked
	 * @param variables Custom variables necessary for the process.
	 * @return Started {@link ProcessInstance}
	 */
	public ProcessInstance startProcessInstanceWithKeyAndVariables(String businessKey, Map<String,Object> variables) {
		return startProcessInstance(variables, businessKey);
	}
	
	/**
	 * Start a process instance.<br>
	 * If a process key is provided for this bean, that process key is ALWAYS used to created a process instance.
	 * Otherwise just the deployment id (== processDefinitionKey) is used to create an instance.
	 * <p>
	 * If variables are provided, the variables are used when creating a process instance.<br>
	 * If a business key is provided, the business key is used when creating a process instance.<br>
	 * <p>
	 * @param variables
	 * @param businessKey
	 * @return
	 */
	private ProcessInstance startProcessInstance(Map<String,Object> variables, String businessKey) {
		
		boolean withVariables = variables != null;
		boolean withBusinessKey = businessKey != null;
		
		if(processKey != null) {
			// START WITH PROCESS KEY
			if(withBusinessKey && withVariables) { // start with business key AND variables 
				logger.info(String.format("Starting process using process key %s and business key %s and variables ...", processKey, businessKey));
				return executionService.startProcessInstanceByKey(processKey, variables, businessKey);
			} else if(withBusinessKey) { // start with business key AND NO variables
				logger.info(String.format("Starting process using process key %s and business key %s ...", processKey, businessKey));
				return executionService.startProcessInstanceByKey(processKey, businessKey);
			} else if(withVariables) { // start WITHOUT business key BUT WITH variables
				logger.info(String.format("Starting process using process key %s and variables ...", processKey));
				return executionService.startProcessInstanceByKey(processKey, variables);
			} else { // start WITHOUT business key AND WITHOUT variables
				logger.info(String.format("Starting process using process key %s ...", processKey));
				return executionService.startProcessInstanceByKey(processKey);
			}
		} else {
			// START WITH DEPLOYMENT ID
			if(withBusinessKey && withVariables) { // start with business key AND variables 
				logger.info(String.format("Starting process using deployment id %s and business key %s and variables ...", processDefinitionId, businessKey));
				return executionService.startProcessInstanceById(processDefinitionId, variables, businessKey);
			} else if(withBusinessKey) { // start with business key AND NO variables
				logger.info(String.format("Starting process using deployment id %s and business key %s ...", deploymentId, businessKey));
				return executionService.startProcessInstanceById(processDefinitionId, businessKey);
			} else if(withVariables) { // start WITHOUT business key BUT WITH variables
				logger.info(String.format("Starting process using deployment id %s and variables ...", deploymentId));
				return executionService.startProcessInstanceById(processDefinitionId, variables);
			} else { // start WITHOUT business key AND WITHOUT variables
				logger.info(String.format("Starting process using deployment id %s ...", deploymentId));
				return executionService.startProcessInstanceById(processDefinitionId);
			}
		}
	}
	
	/*
	 *  VARIABLES 
	 */
	
	/**
	 * @see ExecutionService#getVariable(String, String)
	 * 
	 * @param processExecutionId Execution id of a process
	 * @param variableName name of variable for which to lookup the value
	 * @return Variable value
	 */
	public Object getVariable(String processExecutionId, String variableName) {
		Object varValue = executionService.getVariable(processExecutionId, variableName);
		logger.debug(String.format("Value of variable name '%s': %s", variableName, varValue.toString()));
		return varValue;
	}
	
	/**
	 * @see ExecutionService#getVariableNames(String)
	 * 
	 * @param processExecutionId
	 * @return Variable names
	 */
	public Set<String> getVariableNames(String processExecutionId) {
		Set<String> names = executionService.getVariableNames(processExecutionId);
		logger.debug(String.format("Found %d variable names for process %s", names.size(), processExecutionId));
		return names;
	}
	
	/**
	 * @see ExecutionService#getVariables(String, Set)
	 * 
	 * @param processExecutionId
	 * @param variableNames
	 * @return
	 */
	public Map<String,Object> getVariables(String processExecutionId, Set<String> variableNames) {
		Map<String,Object> vars = executionService.getVariables(processExecutionId, variableNames);
		logger.debug(String.format("Found %d variables for process %s", vars.size(), processExecutionId));
		return vars;
	}

	/* 
	 * Spring getters/setters 
	 */
	public String getResourceName() {
		return resourceName;
	}

	@Required
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getProcessKey() {
		return processKey;
	}

	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}

	public String getDeploymentId() {
		return deploymentId;
	}
}
