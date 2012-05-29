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

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jbpm.pvm.internal.hibernate;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.metadata.ClassMetadata;
import org.jbpm.api.Execution;
import org.jbpm.api.JbpmException;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.history.HistoryComment;
import org.jbpm.api.history.HistoryProcessInstance;
import org.jbpm.api.task.Task;
import org.jbpm.internal.log.Log;
import org.jbpm.pvm.internal.client.ClientExecution;
import org.jbpm.pvm.internal.client.ClientProcessDefinition;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.history.model.HistoryCommentImpl;
import org.jbpm.pvm.internal.history.model.HistoryProcessInstanceImpl;
import org.jbpm.pvm.internal.id.DbidGenerator;
import org.jbpm.pvm.internal.job.JobImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.query.DeploymentQueryImpl;
import org.jbpm.pvm.internal.query.HistoryActivityInstanceQueryImpl;
import org.jbpm.pvm.internal.query.HistoryDetailQueryImpl;
import org.jbpm.pvm.internal.query.HistoryProcessInstanceQueryImpl;
import org.jbpm.pvm.internal.query.JobQueryImpl;
import org.jbpm.pvm.internal.query.ProcessInstanceQueryImpl;
import org.jbpm.pvm.internal.query.TaskQueryImpl;
import org.jbpm.pvm.internal.session.DbSession;
import org.jbpm.pvm.internal.task.TaskImpl;
import org.jbpm.pvm.internal.util.Clock;

/**
 * @author Tom Baeyens
 */
public class DbSessionImpl implements DbSession {

	private static Log log = Log.getLog(DbSessionImpl.class.getName());

	protected Session session;

	public void close() {
		session.close();
	}

	public <T> T get(Class<T> entityClass, Object primaryKey) {
		return entityClass.cast(session.get(entityClass, (Serializable) primaryKey));
	}

	public void flush() {
		session.flush();
	}

	public void forceVersionUpdate(Object entity) {
		session.lock(entity, LockMode.FORCE);
	}

	public void lockPessimistically(Object entity) {
		session.lock(entity, LockMode.UPGRADE);
	}

	public void save(Object entity) {
		session.save(entity);
	}

	public void update(Object entity) {
		session.update(entity);
	}

	public void merge(Object entity) {
		session.merge(entity);
	}

	public void delete(Object entity) {
		session.delete(entity);
	}

	public Session getSession() {
		return session;
	}

	@SuppressWarnings("deprecation")
	public void setSession(Session session) {
		this.session = session;
		// explicitly set the transactional level.
		try {
			if (this.session != null) {
				this.session.connection().setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			}
		} catch (HibernateException hbe) {
			log.info("Unable to set the transactional level.This is a warning message.");
		} catch (SQLException sqle) {
			log.info("Unable to set the transactional level.This is a warning message.");
		}
	}

	public List<String> findProcessDefinitionKeys() {
		return session.getNamedQuery("findProcessDefinitionKeys").list();
	}

	public ClientProcessDefinition findLatestProcessDefinitionByKey(String processDefinitionKey) {
		Query query = session.getNamedQuery("findProcessDefinitionsByKey");
		query.setString("key", processDefinitionKey);
		query.setMaxResults(1);		
		ClientProcessDefinition processDefinition = (ClientProcessDefinition) query.uniqueResult();
		return processDefinition;
	}

	public List<ClientProcessDefinition> findProcessDefinitionsByKey(String processDefinitionKey) {
		Query query = session.getNamedQuery("findProcessDefinitionsByKey");
		query.setString("key", processDefinitionKey);
		return query.list();
	}

	public ClientProcessDefinition findProcessDefinitionById(String processDefinitionId) {
		Query query = session.getNamedQuery("findProcessDefinitionById");
		query.setString("id", processDefinitionId);
		query.setMaxResults(1);
		ClientProcessDefinition processDefinition = (ClientProcessDefinition) query.uniqueResult();
		return processDefinition;
	}

	public void deleteProcessDefinition(String processDefinitionId, boolean deleteProcessInstances, boolean deleteHistory) {
		List<String> processInstanceIds = findProcessInstanceIds(processDefinitionId);

		if (deleteHistory
		// and if hibernate knows about the history class
				&& (isHistoryEnabled())) {
			List<HistoryProcessInstance> historyProcessInstances = createHistoryProcessInstanceQuery().processDefinitionId(
					processDefinitionId).list();

			for (HistoryProcessInstance historyProcessInstance : historyProcessInstances) {
				session.delete(historyProcessInstance);
			}
		}

		if (deleteProcessInstances) {
			for (String processInstanceId : processInstanceIds) {
				deleteProcessInstance(processInstanceId, deleteHistory);
			}
		} else {
			if (processInstanceIds.size() > 0) {
				throw new JbpmException("still " + processInstanceIds.size() + " process instances for process definition "
						+ processDefinitionId);
			}
		}

		ProcessDefinition processDefinition = findProcessDefinitionById(processDefinitionId);
		session.delete(processDefinition);
	}

	public void deleteProcessDefinitionHistory(String processDefinitionId) {
		List<HistoryProcessInstanceImpl> historyProcessInstances = session.createQuery(
				"select hpi " + "from " + HistoryProcessInstanceImpl.class.getName() + " hpi "
						+ "where hpi.processDefinitionId = :processDefinitionId ").setString("processDefinitionId",
				processDefinitionId).list();

		for (HistoryProcessInstanceImpl hpi : historyProcessInstances) {
			session.delete(hpi);
		}
	}

	public boolean isHistoryEnabled() {
		ClassMetadata historyHibernateMetadata = session.getSessionFactory().getClassMetadata(HistoryProcessInstanceImpl.class);
		return historyHibernateMetadata != null;
	}

	// process execution queries ////////////////////////////////////////////////

	public ClientExecution findExecutionById(String executionId) {
		// query definition can be found at the bottom of resource jbpm.pvm.execution.hbm.xml
		Query query = session.getNamedQuery("findExecutionById");
		query.setString("id", executionId);
		query.setMaxResults(1);
		//query.setLockMode("execution", LockMode.READ);
		query.setLockMode("execution", LockMode.NONE);
		return (ClientExecution) query.uniqueResult();
	}

	public ClientExecution findProcessInstanceById(String processInstanceId) {
		// query definition can be found at the bottom of resource jbpm.pvm.execution.hbm.xml
		Query query = session.getNamedQuery("findProcessInstanceById");
		query.setString("processInstanceId", processInstanceId);
		//query.setLockMode("processInstance", LockMode.READ);	
		query.setLockMode("processInstance", LockMode.NONE);
		query.setMaxResults(1);
		return (ClientExecution) query.uniqueResult();
	}

	public ClientExecution findProcessInstanceByIdIgnoreSuspended(String processInstanceId) {
		// query definition can be found at the bottom of resource jbpm.pvm.execution.hbm.xml
		Query query = session.getNamedQuery("findProcessInstanceByIdIgnoreSuspended");
		query.setString("processInstanceId", processInstanceId);
		//query.setLockMode("processInstance", LockMode.READ);
		query.setLockMode("processInstance", LockMode.NONE);
		query.setMaxResults(1);
		return (ClientExecution) query.uniqueResult();
	}

	public List<String> findProcessInstanceIds(String processDefinitionId) {
		// query definition can be found at the bottom of resource jbpm.pvm.job.hbm.xml
		Query query = session.createQuery("select processInstance.id "
				+ "from org.jbpm.pvm.internal.model.ExecutionImpl as processInstance "
				+ "where processInstance.processDefinitionId = :processDefinitionId " + "  and processInstance.parent is null");
		query.setString("processDefinitionId", processDefinitionId);
		//query.setLockMode("processInstance", LockMode.READ);
		query.setLockMode("processInstance", LockMode.NONE);
		return query.list();
	}

	public void deleteProcessInstance(String processInstanceId) {
		deleteProcessInstance(processInstanceId, true);
	}

	public void deleteProcessInstance(String processInstanceId, boolean deleteHistory) {
		if (processInstanceId == null) {
			throw new JbpmException("processInstanceId is null");
		}

		// if history should be deleted
		if (deleteHistory && (isHistoryEnabled())) {
			// try to get the history
			HistoryProcessInstanceImpl historyProcessInstance = findHistoryProcessInstanceById(processInstanceId);

			// if there is a history process instance in the db
			if (historyProcessInstance != null) {
				if (log.isDebugEnabled()) {
					log.debug("deleting history process instance " + processInstanceId);
				}
				session.delete(historyProcessInstance);
			}
		}

		ExecutionImpl processInstance = (ExecutionImpl) findProcessInstanceByIdIgnoreSuspended(processInstanceId);
		if (processInstance != null) {
			deleteSubProcesses(processInstance, deleteHistory);

			// delete remaining tasks for this process instance
			List<TaskImpl> tasks = findTasks(processInstanceId);
			for (TaskImpl task : tasks) {
				session.delete(task);
			}

			// delete remaining jobs for this process instance
			JobImpl currentJob = EnvironmentImpl.getFromCurrent(JobImpl.class, false);
			List<JobImpl> jobs = findJobs(processInstanceId);
			for (JobImpl job : jobs) {
				if (job != currentJob) {
					session.delete(job);
				}
			}

			if (log.isDebugEnabled()) {
				log.debug("deleting process instance " + processInstanceId);
			}

			session.delete(processInstance);
		}
	}

	private void deleteSubProcesses(ExecutionImpl execution, boolean deleteHistory) {
		ExecutionImpl subProcessInstance = execution.getSubProcessInstance();
		if (subProcessInstance != null) {
			subProcessInstance.setSuperProcessExecution(null);
			execution.setSubProcessInstance(null);
			deleteProcessInstance(subProcessInstance.getId(), deleteHistory);
		}
		Collection<ExecutionImpl> childExecutions = execution.getExecutions();
		if (childExecutions != null) {
			for (ExecutionImpl childExecution : childExecutions) {
				deleteSubProcesses(childExecution, deleteHistory);
			}
		}
	}

	public HistoryProcessInstanceImpl findHistoryProcessInstanceById(String processInstanceId) {
		return (HistoryProcessInstanceImpl) session.createQuery(
				"select hpi " + "from " + HistoryProcessInstance.class.getName() + " as hpi " + "where hpi.processInstanceId = '"
						+ processInstanceId + "'").uniqueResult();
	}

	List<TaskImpl> findTasks(String processInstanceId) {
		Query query = session.createQuery("select task " + "from " + TaskImpl.class.getName() + " as task "
				+ "where task.processInstance.id = :processInstanceId");
		//query.setLockMode("task", LockMode.READ);
		query.setLockMode("task", LockMode.NONE);
		query.setString("processInstanceId", processInstanceId);
		return query.list();
	}

	List<JobImpl> findJobs(String processInstanceId) {
		Query query = session.createQuery("select job " + "from " + JobImpl.class.getName() + " as job "
				+ "where job.processInstance.id = :processInstanceId");
		query.setString("processInstanceId", processInstanceId);
		//query.setLockMode("job", LockMode.READ);
		query.setLockMode("job", LockMode.NONE);
		return query.list();
	}

	public void cascadeExecutionSuspend(ExecutionImpl execution) {
		// cascade suspend to jobs
		Query query = session.createQuery("select job " + "from " + JobImpl.class.getName() + " as job "
				+ "where job.execution = :execution " + "  and job.state != '" + JobImpl.STATE_SUSPENDED + "' ");
		query.setEntity("execution", execution);
		query.setLockMode("job", LockMode.NONE);
		
		List<JobImpl> jobs = query.list();
		for (JobImpl job : jobs) {
			job.suspend();
		}

		// cascade suspend to tasks
		query = session.createQuery("select task " + "from " + TaskImpl.class.getName() + " as task "
				+ "where task.execution = :execution " + "  and task.state != '" + Task.STATE_SUSPENDED + "' ");
		query.setEntity("execution", execution);
		query.setLockMode("task", LockMode.NONE);
		
		List<TaskImpl> tasks = query.list();
		for (TaskImpl task : tasks) {
			task.suspend();
		}
	}

	public void cascadeExecutionResume(ExecutionImpl execution) {
		// cascade suspend to jobs
		Query query = session.createQuery("select job " + "from " + JobImpl.class.getName() + " as job "
				+ "where job.execution = :execution " + "  and job.state = '" + Task.STATE_SUSPENDED + "' ");
		query.setEntity("execution", execution);
		query.setLockMode("job", LockMode.NONE);
		
		List<JobImpl> jobs = query.list();
		for (JobImpl job : jobs) {
			job.resume();
		}

		// cascade suspend to tasks
		query = session.createQuery("select task " + "from " + TaskImpl.class.getName() + " as task "
				+ "where task.execution = :execution " + "  and task.state = '" + Task.STATE_SUSPENDED + "' ");
		query.setEntity("execution", execution);
		query.setLockMode("task", LockMode.NONE);
		
		List<TaskImpl> tasks = query.list();
		for (TaskImpl task : tasks) {
			task.resume();
		}
	}

	public TaskImpl createTask() {
		TaskImpl task = newTask();
		task.setCreateTime(Clock.getCurrentTime());
		return task;
	}

	protected TaskImpl newTask() {
		TaskImpl task = new TaskImpl();
		long dbid = EnvironmentImpl.getFromCurrent(DbidGenerator.class).getNextId();
		task.setDbid(dbid);
		task.setNew(true);
		return task;
	}

	public TaskImpl findTaskByDbid(long taskDbid) {
		return (TaskImpl) session.get(TaskImpl.class, taskDbid);
	}

	public TaskImpl findTaskByExecution(Execution execution) {
		Query query = session.createQuery("select task " + "from " + TaskImpl.class.getName() + " as task "
				+ "where task.execution = :execution");
		query.setEntity("execution", execution);
		query.setLockMode("task", LockMode.NONE);
		
		return (TaskImpl) query.uniqueResult();
	}

	public JobImpl<?> findFirstAcquirableJob() {
		Query query = session.getNamedQuery("findFirstAcquirableJob");
		query.setTimestamp("now", Clock.getCurrentTime());
		query.setMaxResults(1);
		//query.setLockMode("job", LockMode.READ);
		query.setLockMode("job", LockMode.NONE);
		return (JobImpl<?>) query.uniqueResult();
	}

	public List<JobImpl<?>> findExclusiveJobs(Execution processInstance) {
		Query query = session.getNamedQuery("findExclusiveJobs");
		query.setTimestamp("now", Clock.getCurrentTime());
		query.setLockMode("job",LockMode.NONE);
		
		query.setEntity("processInstance", processInstance);
		return query.list();
	}

	public JobImpl<?> findFirstDueJob() {
		Query query = session.getNamedQuery("findFirstDueJob");
		query.setMaxResults(1);
		//query.setLockMode("job", LockMode.READ);
		query.setLockMode("job", LockMode.NONE);
		return (JobImpl<?>) query.uniqueResult();
	}

	public ProcessInstanceQueryImpl createProcessInstanceQuery() {
		return new ProcessInstanceQueryImpl();
	}

	public TaskQueryImpl createTaskQuery() {
		return new TaskQueryImpl();
	}

	public HistoryProcessInstanceQueryImpl createHistoryProcessInstanceQuery() {
		return new HistoryProcessInstanceQueryImpl();
	}

	public HistoryActivityInstanceQueryImpl createHistoryActivityInstanceQuery() {
		return new HistoryActivityInstanceQueryImpl();
	}

	public HistoryDetailQueryImpl createHistoryDetailQuery() {
		return new HistoryDetailQueryImpl();
	}

	public JobQueryImpl createJobQuery() {
		return new JobQueryImpl();
	}

	public DeploymentQueryImpl createDeploymentQuery() {
		return new DeploymentQueryImpl();
	}

	public List<HistoryComment> findCommentsByTaskId(String taskId) {
		Long taskDbid = null;
		try {
			taskDbid = Long.parseLong(taskId);
		} catch (Exception e) {
			throw new JbpmException("invalid taskId: " + taskId);
		}
		return session.createQuery(
				"select hc " + "from " + HistoryCommentImpl.class.getName() + " as hc " + "where hc.historyTask.dbid = :taskDbid "
						+ "order by hc.historyTaskIndex asc ").setLong("taskDbid", taskDbid).list();
	}
}
