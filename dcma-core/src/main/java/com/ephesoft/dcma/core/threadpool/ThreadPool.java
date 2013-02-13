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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.ephesoft.dcma.core.constant.CoreConstants;

/**
 * This class used to creates the thread in the thread pool and maintains the information of the threads per batch instance.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see java.util.concurrent.ThreadPoolExecutor
 */
public final class ThreadPool extends ThreadPoolExecutor {

	/**
	 * Maximum number of threads that can be added to the pool.
	 */
	private static final int MAX_POOL_SIZE = Integer.MAX_VALUE;

	/**
	 * Maximum number of threads that can be executed simultaneously.
	 */
	private static final int CORE_POOL_SIZE = 5;

	/**
	 * Maximum number of threads that can be executed simultaneously when executed for ghost script.
	 */
	private static final int CORE_POOL_SIZE_FOR_GHOST_SCRIPT = 1;

	/**
	 * When the number of threads is greater than the core, this is the maximum time that excess idle threads will wait for new tasks
	 * before terminating.
	 */
	private static final long KEEP_ALIVE_TIME = 15000L;

	/**
	 * Constant for META-INF.
	 */
	private static final String META_INF = "META-INF";

	/**
	 * Constant for dcma core property file name.
	 */
	private static final String FILE_NAME = "dcma-core";

	/**
	 * Constant for thread.pool_size.
	 */
	private static final String THREAD_SIZE = "thread.pool_size";

	/**
	 * Constant for thread.pool_size_for_ghost_script.
	 */
	private static final String THREAD_SIZE_FOR_GHOST_SCRIPT = "thread.pool_size_for_ghost_script";

	/**
	 * Logger instance for logging using slf4j for logging information.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ThreadPool.class);

	/**
	 * Constant for dcma core folder name.
	 */
	private static final String FOLDER_NAME = "dcma-core";

	/**
	 * Object of this class.
	 */
	private static ThreadPool threadPoolInstance;

	/**
	 * Thread pool instance for ghost script.
	 */
	private static ThreadPool threadPoolInstanceForGhostScript;

	/**
	 * Map for storing the information of batch instance id vs batch instance thread object.
	 */
	private static final Map<String, BatchInstanceThread> BATCH_INSTANCE_THREAD_MAP = new HashMap<String, BatchInstanceThread>();

	/**
	 * An object used for synchronization.
	 */
	private static Object object = new Object();

	/**
	 * Creating a singleton class.
	 */
	private ThreadPool(boolean isUsingGhostScript) {
		super(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		int corePoolSize = getCorePoolSize(isUsingGhostScript);
		this.setCorePoolSize(corePoolSize);
	}

	/**
	 * API for getting the core pool size configured in the property file.
	 * 
	 * @param isUsingGhostScript boolean
	 * @return int 
	 */
	private int getCorePoolSize(boolean isUsingGhostScript) {
		String filePath = META_INF + File.separator + FOLDER_NAME + File.separator + FILE_NAME + ".properties";
		InputStream propertyInStream = null;
		Integer corePoolSize = null;
		int defaultCorePoolSize = CORE_POOL_SIZE;
		try {
			if (isUsingGhostScript) {
				defaultCorePoolSize = CORE_POOL_SIZE_FOR_GHOST_SCRIPT;
			}
			propertyInStream = new ClassPathResource(filePath).getInputStream();
			Properties properties = new Properties();
			properties.load(propertyInStream);
			try {
				if (isUsingGhostScript) {
					corePoolSize = Integer.parseInt((String) properties.get(THREAD_SIZE_FOR_GHOST_SCRIPT));
				} else {
					corePoolSize = Integer.parseInt((String) properties.get(THREAD_SIZE));
				}
			} catch (NumberFormatException nfe) {
				LOG.info("Invalid or no value for core pool size specified in properties file. Setting it its default value.");
				corePoolSize = defaultCorePoolSize;
			}
		} catch (Exception e) {
			LOG.info("Could not get thread pool size. Using default value.");
			corePoolSize = defaultCorePoolSize;
		} finally {
			try {
				if (propertyInStream != null) {
					propertyInStream.close();
				}
			} catch (IOException ioe) {
				LOG.info("Could not close property input stream in threadpool.");
			}
		}
		return corePoolSize;
	}

	/**
	 * Adds a task to the thread pool.
	 * 
	 * @param r the runnable task to be added.
	 */
	public void addTask(Runnable runnable) throws RejectedExecutionException {
		threadPoolInstance.execute(runnable);
	}
	
	/**
	 * Adds a task to the thread pool for ghost script.
	 * 
	 * @param runnable the runnable task to be added.
	 */
	public void addTaskForGhostScript(Runnable runnable) throws RejectedExecutionException {
		threadPoolInstanceForGhostScript.execute(runnable);
	}

	/**
	 * API for add information in the batch instance thread map.
	 * 
	 * @param batchInstanceId String
	 * @param batchInstanceThread BatchInstanceThread
	 */
	public void putBatchInstanceThreadMap(String batchInstanceId, BatchInstanceThread batchInstanceThread) {
		if (batchInstanceId != null && batchInstanceThread != null) {
			BATCH_INSTANCE_THREAD_MAP.put(batchInstanceId, batchInstanceThread);
		}
	}

	/**
	 * remove a task to the thread pool.
	 * 
	 * @param r the runnable task to be added.
	 */
	public boolean removeTask(Runnable runnable) {
		return super.remove(runnable);
	}

	/**
	 * Used to get the single element of this thread class.
	 * 
	 * @return threadPoolInstance.
	 */
	public static ThreadPool getInstance() {
		if (threadPoolInstance == null) {
			synchronized (object) {
				if (threadPoolInstance == null) {
					threadPoolInstance = new ThreadPool(false);
				}
			}

		}
		return threadPoolInstance;
	}

	/**
	 * Generates the thread pool results.
	 */
	public void generateSystemReport() {
		LOG.info(CoreConstants.HYPHENS + "Active count " + super.getActiveCount()
				+ CoreConstants.HYPHENS);
		LOG.info(CoreConstants.HYPHENS + "Completed count " + super.getCompletedTaskCount()
				+ CoreConstants.HYPHENS);
		LOG.info(CoreConstants.HYPHENS + "Task Count " + super.getTaskCount()
				+ CoreConstants.HYPHENS + "\n");

	}

	/**
	 * Returns batch instance thread {@link BatchInstanceThread} for specific batch instance.
	 * 
	 * @return BatchInstanceThread
	 */
	public static BatchInstanceThread getBatchInstanceThreadList(String batchInstanceId) {
		return BATCH_INSTANCE_THREAD_MAP.get(batchInstanceId);
	}

	/**
	 * Processing to be done after the thread has finished execution.
	 */
	@Override
	protected void afterExecute(Runnable runnable, Throwable throwable) {
		if (runnable instanceof AbstractRunnable) {
			AbstractRunnable abstractRunnable = (AbstractRunnable) runnable;
			abstractRunnable.setCompleted(true);
		}
		super.afterExecute(runnable, throwable);
	}

	@Override
	protected void beforeExecute(Thread thread, Runnable runnable) {
		if (runnable instanceof AbstractRunnable) {
			AbstractRunnable abstractRunnable = (AbstractRunnable) runnable;
			abstractRunnable.setStarted(true);
		}
		super.beforeExecute(thread, runnable);
	}

	/**
	 * API for deleting the information from the batch instance thread map.
	 * 
	 * @param batchInstanceId String
	 */ 
	public void removeBatchInstanceThreadMap(String batchInstanceId) {
		if (batchInstanceId != null) {
			BATCH_INSTANCE_THREAD_MAP.remove(batchInstanceId);
		}
	}

	/**
	 * Returns an instance of thread pool for ghostscript.
	 * 
	 * @return threadPoolInstanceForGhostScript
	 */
	public static ThreadPool getInstanceForGhostScript() {
		if (threadPoolInstanceForGhostScript == null) {
			synchronized (object) {
				if (threadPoolInstanceForGhostScript == null) {
					threadPoolInstanceForGhostScript = new ThreadPool(true);
				}
			}

		}
		return threadPoolInstanceForGhostScript;
	}
}
