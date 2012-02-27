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
	 * When the number of threads is greater than the core, this is the maximum time that excess idle threads will wait for new tasks
	 * before terminating.
	 */
	private static final long KEEP_ALIVE_TIME = 15000L;

	private static final String META_INF = "META-INF";

	private static final String FILE_NAME = "dcma-core";

	private static final String THREAD_SIZE = "thread.pool_size";

	/**
	 * Logger instance for logging using slf4j for logging information.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ThreadPool.class);

	private static final String FOLDER_NAME = "dcma-core";

	/**
	 * Object of this class.
	 */
	private static ThreadPool threadPoolInstance;

	private static final Map<String, BatchInstanceThread> BATCH_INSTANCE_THREAD_MAP = new HashMap<String, BatchInstanceThread>();

	/**
	 * An object used for synchronization.
	 */
	private static Object object = new Object();

	/**
	 * Creating a singleton class.
	 */
	private ThreadPool() {
		super(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		String filePath = META_INF + File.separator + FOLDER_NAME + File.separator + FILE_NAME + ".properties";
		InputStream propertyInStream = null;
		try {
			propertyInStream = new ClassPathResource(filePath).getInputStream();
			Properties properties = new Properties();
			properties.load(propertyInStream);
			int corePoolSize = Integer.parseInt((String) properties.get(THREAD_SIZE));
			super.setCorePoolSize(corePoolSize);
		} catch (Exception e) {
			LOG.info("Could not set thread pool size. Using default value of 5");
        } finally {
			try {
				if (propertyInStream != null) {
					propertyInStream.close();
				}
			} catch (IOException ioe) {
				LOG.info("Could not close property input stream in threadpool.");
			}
		}

	}

	/**
	 * Adds a task to the thread pool.
	 * 
	 * @param r the runnable task to be added.
	 */
	public void addTask(Runnable runnable) throws RejectedExecutionException {
		super.execute(runnable);
	}

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
	 * @return the thread instance.
	 */
	public static synchronized ThreadPool getInstance() {
		if (threadPoolInstance == null) {
			synchronized (object) {
				if (threadPoolInstance == null) {
					threadPoolInstance = new ThreadPool();
				}
			}

		}
		return threadPoolInstance;
	}

	/**
	 * Generates the thread pool results.
	 */
	public void generateSystemReport() {
		LOG.info("------------------------------------------------------" + "Active count " + super.getActiveCount()
				+ "-----------------------------------------------");
		LOG.info("------------------------------------------------------" + "Completed count " + super.getCompletedTaskCount()
				+ "-----------------------------------------------");
		LOG.info("------------------------------------------------------" + "Task Count " + super.getTaskCount()
				+ "-----------------------------------------------\n");

	}

	/**
	 * Returns batch instance thread {@link BatchInstanceThread} for specific batch instance.
	 * 
	 * @return
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

	public void removeBatchInstanceThreadMap(String batchInstanceId) {
		if (batchInstanceId != null) {
			BATCH_INSTANCE_THREAD_MAP.remove(batchInstanceId);
		}
	}
}
