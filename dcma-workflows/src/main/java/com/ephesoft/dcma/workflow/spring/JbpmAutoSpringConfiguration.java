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

package com.ephesoft.dcma.workflow.spring;

import org.jbpm.api.ExecutionService;
import org.jbpm.api.HistoryService;
import org.jbpm.api.IdentityService;
import org.jbpm.api.ManagementService;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.TaskService;
import org.jbpm.pvm.internal.cfg.ConfigurationImpl;
import org.jbpm.pvm.internal.processengine.ProcessEngineImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.ephesoft.dcma.core.common.ImageType;
import com.ephesoft.dcma.workflow.common.EnvironmentObjectGetCmd;

/**
 * Factory bean which create the JBPM Process Engine.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see org.jbpm.pvm.internal.processengine.ProcessEngineImpl
 */
@Configuration
public class JbpmAutoSpringConfiguration {

	/**
	 * jbpmCfg String.
	 */
	protected String jbpmCfg = "META-INF/dcma-workflows/jbpm.cfg.xml";

	/**
	 * To set JbpmCfg.
	 * 
	 * @param jbpmCfg String
	 */
	public void setJbpmCfg(String jbpmCfg) {
		this.jbpmCfg = jbpmCfg;
	}

	/**
	 * applicationContext {@link ApplicationContext}.
	 */
	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * To configure JBPM.
	 * 
	 * @return org.jbpm.api.Configuration
	 */
	@Bean(name = "jbpmConfiguration")
	@Scope(BeanDefinition.SCOPE_SINGLETON)
	public org.jbpm.api.Configuration jbpmConfiguration() {
		ConfigurationImpl configuration = new ConfigurationImpl();
		configuration.springInitiated(applicationContext).setResource(jbpmCfg);
		return configuration;
	}

	/**
	 * To process Engine.
	 * 
	 * @return ProcessEngine
	 */
	@Bean(name = "jbpmProcessEngine")
	@Scope(BeanDefinition.SCOPE_SINGLETON)
	public ProcessEngine processEngine() {
		ProcessEngine processEngine = jbpmConfiguration().buildProcessEngine();
		((ProcessEngineImpl) processEngine).set("PNG_IMAGE", ImageType.PNG);
		((ProcessEngineImpl) processEngine).set("TIFF_IMAGE", ImageType.TIFF);
		return processEngine;
	}

	/**
	 * Provides the {@link ExecutionService}. Qualifier name is 'jbpmExecutionService'.
	 * 
	 * @return {@link ExecutionService}
	 */
	@Bean(name = "jbpmExecutionService")
	public ExecutionService executionService() {
		return (ExecutionService) processEngine().execute(new EnvironmentObjectGetCmd(ExecutionService.class));
	}

	/**
	 * Provides the {@link HistoryService}. Qualifier name is 'jbpmHistoryService'.
	 * 
	 * @return {@link HistoryService}
	 */
	@Bean
	@Qualifier("jbpmHistoryService")
	public HistoryService historyService() {
		return (HistoryService) processEngine().execute(new EnvironmentObjectGetCmd(HistoryService.class));
	}

	/**
	 * Provides the {@link IdentityService}. Qualifier name is 'jbpmIdentityService'.
	 * 
	 * @return {@link IdentityService}
	 */
	@Bean
	@Qualifier("jbpmIdentityService")
	public IdentityService identityService() {
		return (IdentityService) processEngine().execute(new EnvironmentObjectGetCmd(IdentityService.class));
	}

	/**
	 * Provides the {@link ManagementService}. Qualifier name is 'jbpmManagementService'.
	 * 
	 * @return {@link ManagementService}
	 */
	@Bean
	@Qualifier("jbpmManagementService")
	public ManagementService managementService() {
		return (ManagementService) processEngine().execute(new EnvironmentObjectGetCmd(ManagementService.class));
	}

	/**
	 * Provides the {@link RepositoryService}. Qualifier name is 'jbpmRepositoryService'.
	 * 
	 * @return {@link RepositoryService}
	 */
	@Bean
	@Qualifier("jbpmRepositoryService")
	public RepositoryService repositoryService() {
		return (RepositoryService) processEngine().execute(new EnvironmentObjectGetCmd(RepositoryService.class));
	}

	/**
	 * Provides the {@link TaskService}. Qualifier name is 'jbpmTaskService'.
	 * 
	 * @return {@link TaskService}
	 */
	@Bean
	@Qualifier("jbpmTaskService")
	public TaskService taskService() {
		return (TaskService) processEngine().execute(new EnvironmentObjectGetCmd(TaskService.class));
	}

}
