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

package com.ephesoft.dcma.core.threadpool;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.core.constant.CoreConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;

/**
 * This class used to add the threads in the thread pool.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see java.util.concurrent.RejectedExecutionException
 */
public class BatchInstanceThread {

	/**
	 * Time duration the thread sleeps before checking if all tasks have completed.
	 */
	private final long waitThreadSleepTime;

	/**
	 * String constant for batch instance identifier.
	 */
	private String batchInstanceId;

	/**
	 * List of all tasks associated with a batch instance.
	 */
	private final List<AbstractRunnable> taskList = new LinkedList<AbstractRunnable>();

	/**
	 * Boolean to check whether thread pool is used for executing ghost script commands.
	 */
	private boolean isUsingGhostScript;

	/**
	 * Logger instance for logging using slf4j for logging information.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(BatchInstanceThread.class);

	/**
	 * To get Batch Instance Id.
	 * @return the batchInstanceId
	 */
	public String getBatchInstanceId() {
		return batchInstanceId;
	}

	/**
	 * To set Batch Instance Id.
	 * @param batchInstanceId String
	 */
	public void setBatchInstanceId(String batchInstanceId) {
		this.batchInstanceId = batchInstanceId;
	}

	/**
	 * Default construtor for batch instance thread class.
	 */
	public BatchInstanceThread() {
		this(null, Boolean.FALSE, CoreConstants.THREAD_SLEEP_TIME);
	}

	/**
	 * Constructor for batch instance thread class.
	 * 
	 * @param batchInstanceId String
	 */
	public BatchInstanceThread(String batchInstanceId) {
		this(batchInstanceId, Boolean.FALSE, CoreConstants.THREAD_SLEEP_TIME);
	}

	/**
	 * Constructor for batch instance thread class.
	 * 
	 * @param waitTime long
	 */
	public BatchInstanceThread(long waitTime) {
		this(null, Boolean.FALSE, waitTime);
	}

	/**
	 * Constructor for batch instance thread class.
	 * 
	 * @param batchInstanceId String
	 * @param isUsingGhostScript boolean
	 * @param waitThreadSleepTime long
	 */
	public BatchInstanceThread(String batchInstanceId, boolean isUsingGhostScript, long waitThreadSleepTime) {
		this.batchInstanceId = batchInstanceId;
		this.isUsingGhostScript = isUsingGhostScript;
		this.waitThreadSleepTime = waitThreadSleepTime;
	}

	/**
	 * To set UsingGhostScript.
	 * @param isUsingGhostScript boolean
	 */
	public void setUsingGhostScript(boolean isUsingGhostScript) {
		this.isUsingGhostScript = isUsingGhostScript;
	}

	/**
	 * This method is used to add a task (command line) that needs execution.
	 * 
	 * @param runnable AbstractRunnable
	 */
	public void add(AbstractRunnable runnable) {
		synchronized (taskList) {
			this.taskList.add(runnable);
		}
	}

	/**
	 * This method is used to add list of task (command line) that needs execution.
	 * 
	 * @param runnableList List<AbstractRunnable>
	 */
	public void addAll(List<AbstractRunnable> runnableList) {
		synchronized (taskList) {
			this.taskList.addAll(runnableList);
		}
	}

	/**
	 * The method is used to execute the tasks provided. The method returns when all the tasks have stopped execution.
	 * 
	 * @throws DCMAApplicationException if more tasks cannot be added
	 */
	public void execute() throws DCMAApplicationException {
		synchronized (taskList) {
			ThreadPool threadPoolInstance = getThreadPoolInstance();
			if (batchInstanceId != null) {
				threadPoolInstance.putBatchInstanceThreadMap(batchInstanceId, this);
			}
			for (AbstractRunnable runnable : taskList) {
				try {
					if(isUsingGhostScript) {
						threadPoolInstance.addTaskForGhostScript(runnable);
					} else {
						threadPoolInstance.addTask(runnable);
					}
				} catch (RejectedExecutionException e) {
					LOG.error("Cannot add any more tasks. Some tasks for this batch instance may not have been added.");
					runnable.setDcmaApplicationException(new DCMAApplicationException(
							"Cannot add any more tasks. Thread pool has reached maximum size."));
				}
			}
			wait(taskList);
			if (batchInstanceId != null) {
				threadPoolInstance.removeBatchInstanceThreadMap(batchInstanceId);
			}
		}
	}

	/**
	 * API for the getting the thread pool instance object.
	 * 
	 * @return threadPoolInstance
	 */
	private ThreadPool getThreadPoolInstance() {
		ThreadPool threadPoolInstance;
		if (isUsingGhostScript) {
			threadPoolInstance = ThreadPool.getInstanceForGhostScript();
		} else {
			threadPoolInstance = ThreadPool.getInstance();
		}
		return threadPoolInstance;
	}

	/**
	 * API for removing the thread from the threadpool and batch instance thread map.
	 * 
	 */
	public void remove(){
		ThreadPool threadPoolInstance = getThreadPoolInstance();
		for (Iterator<AbstractRunnable> iterator = taskList.iterator(); iterator.hasNext();) {
			AbstractRunnable runnable = (AbstractRunnable) iterator.next();
			if (!runnable.isStarted() || runnable.isCompleted()) {
				threadPoolInstance.removeTask(runnable);
				iterator.remove();
			}
		}
		waitForCompletion(taskList);
		if (batchInstanceId != null) {
			threadPoolInstance.removeBatchInstanceThreadMap(batchInstanceId);
		}
	}

	/**
	 * Waits for all the threads in the provided list to complete execution.
	 * 
	 * @param threadClassList the list on which to wait.
	 * @throws DCMAApplicationException in case of error
	 */
	public void wait(List<AbstractRunnable> taskList) throws DCMAApplicationException {
		boolean completed = false;
		while (!completed) {
			completed = true;
			for (AbstractRunnable th : taskList) {
				completed &= th.isCompleted();
				if (th.isCompleted() && th.getDcmaApplicationException() != null) {
					throw th.getDcmaApplicationException();
				}
			}
			try {
				if (!completed) {
					Thread.sleep(waitThreadSleepTime);
				}
			} catch (InterruptedException e) {
				LOG.error(Thread.currentThread()
						+ " interrupted. Resuming execution. Some tasks of this batch instance may not have completed execution.");
			}
		}
	}

	/**
	 * Waits for all the threads in the provided list to complete execution.
	 * 
	 * @param threadClassList the list on which to wait.
	 */
	public void waitForCompletion(List<AbstractRunnable> taskList) {
		boolean completed = false;
		while (!completed) {
			completed = true;
			for (AbstractRunnable th : taskList) {
				completed &= th.isCompleted();
			}
			try {
				if (!completed) {
					Thread.sleep(waitThreadSleepTime);
				}
			} catch (InterruptedException e) {
				LOG.error(Thread.currentThread()
						+ " interrupted. Resuming execution. Some tasks of this batch instance may not have completed execution.");
			}
		}
	}

	/**
	 * To get Task List.
	 * @return List<AbstractRunnable>
	 */
	public List<AbstractRunnable> getTaskList() {
		return taskList;
	}

}
