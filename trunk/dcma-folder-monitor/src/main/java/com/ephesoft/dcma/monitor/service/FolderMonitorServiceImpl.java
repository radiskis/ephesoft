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

package com.ephesoft.dcma.monitor.service;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.id.BatchClassID;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.monitor.EphesoftFolderListner;
import com.ephesoft.dcma.monitor.FolderDetail;
import com.ephesoft.dcma.monitor.FolderModificationEvent;

public class FolderMonitorServiceImpl implements FolderMonitorService, ApplicationListener<FolderModificationEvent> {

	// private static final String TIME_FORMAT_DELIMITER = ":";
	//
	// private static final String DIRECTORY_TOKEN = "<DIR>";
	//
	// private static final String TIMEFORMAT_PM = "PM";

	protected static final Logger LOGGER = LoggerFactory.getLogger(FolderMonitorServiceImpl.class);

	@Autowired
	private BatchClassService batchClassService;
	@Autowired
	private BatchInstanceService batchInstanceService;
	@Autowired
	private BatchSchemaService batchSchemaService;

	@Autowired
	private EphesoftFolderListner listner;

	// @Autowired
	// private ServerRegistryService registryService;

	final private Map<String, Integer> batchClassIdVsWatchIdMap = new HashMap<String, Integer>();
	private FileChannel fileChannel;
	private boolean running = false;

	private long waitTime;

	@Override
	public void start() {
		List<BatchClass> batchClasses = batchClassService.getAllBatchClassesExcludeDeleted();
		pickOldUnPickedBatches(batchClasses);
		lockLocalFolder();
		for (BatchClass batchClass : batchClasses) {
			addWatch(batchClass);
		}
		running = true;
	}

	private void pickOldUnPickedBatches(List<BatchClass> batchClasses) {
		for (BatchClass batchClass : batchClasses) {
			List<BatchInstance> batches = batchInstanceService.getAllUnFinishedBatchInstances(batchClass.getUncFolder());
			String uncFolderPath = batchClass.getUncFolder();
			File uncFolder = new File(uncFolderPath);
			String[] folders = uncFolder.list();
			if (batches != null && folders != null) {
				for (String folderName : folders) {
					if (folderName != null && !isFolderPresent(folderName, batches)) {
						createBatchInstance(batchClass, uncFolder, folderName);
					}
				}
			}
		}

	}

	private void createBatchInstance(BatchClass batchClass, File uncFolder, String batchFolderName) {
		String uncSubFolder = new File(uncFolder + File.separator + batchFolderName).getAbsolutePath();
		batchInstanceService.createBatchInstance(batchClass, uncSubFolder, batchSchemaService.getLocalFolderLocation(), batchClass
				.getPriority());
	}

	@Override
	public void stop() {
		removeWatch();
		unlockLocalFolder();
		running = false;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public boolean isAutoStartup() {
		return true;
	}

	@Override
	public void stop(Runnable callback) {
		callback.run();
	}

	@Override
	public int getPhase() {
		return 0;
	}

	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}

	@Override
	public void onApplicationEvent(final FolderModificationEvent event) {

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(waitTime);
				} catch (InterruptedException e) {
				}

				FolderDetail folderDetail = event.getFolderDetail();
				List<BatchClass> batchClasses = batchClassService.getAllBatchClassesExcludeDeleted();
				for (BatchClass batchClass : batchClasses) {
					if (batchClass.getUncFolder().equals(folderDetail.getParentPath())) {
						batchInstanceService.createBatchInstance(batchClass, folderDetail.getFullPath(), batchSchemaService
								.getLocalFolderLocation(), batchClass.getPriority());
						break;
					}
				}
			}
		});
		thread.start();
	}

	@Override
	public void monitorBatchClass(BatchClassID batchClassID) {
		BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassID.getID());
		addWatch(batchClass);
	}

	private void addWatch(BatchClass batchClass) throws DCMABusinessException {
		try {
			int watchID = JNotify.addWatch(batchClass.getUncFolder(), JNotify.FILE_CREATED, false, listner);
			this.batchClassIdVsWatchIdMap.put(batchClass.getIdentifier(), watchID);
		} catch (JNotifyException e) {
			LOGGER.error(e.getMessage(), e);
			throw new DCMABusinessException(e.getMessage(), e);
		}
	}

	private void removeWatch() throws DCMABusinessException {
		try {
			for (Integer watchID : batchClassIdVsWatchIdMap.values()) {
				JNotify.removeWatch(watchID);
			}
		} catch (JNotifyException e) {
			LOGGER.error(e.getMessage(), e);
			throw new DCMABusinessException(e.getMessage(), e);
		}
	}

	private void lockLocalFolder() {
		String lockFileName = batchSchemaService.getLocalFolderLocation() + File.separator + "_lock";

		try {
			File lockFile = new File(lockFileName);
			fileChannel = new RandomAccessFile(lockFile, "rw").getChannel();

			FileLock lock = fileChannel.lock();

			try {
				lock = fileChannel.tryLock();
			} catch (OverlappingFileLockException e) {
				LOGGER.trace("File is already locked in this thread or virtual machine");
			}
			if (lock == null) {
				LOGGER.trace("File is already locked in this thread or virtual machine");
			}

		} catch (Exception e) {
			LOGGER.error("Unable to aquire lock on file : " + lockFileName, e);
			throw new DCMABusinessException("Unable to aquire lock on file : " + lockFileName, e);
		}
	}

	private void unlockLocalFolder() {
		try {
			if (null != fileChannel) {
				FileLock lock = fileChannel.lock();
				if (null != lock) {
					// Remember to release the lock
					lock.release();
					// Close the file
					fileChannel.close();
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new DCMABusinessException(e.getMessage(), e);
		}
	}

	@Override
	public void removeWatchFromBatchClass(BatchClassID batchClassID) {
		Integer watchId = batchClassIdVsWatchIdMap.get(batchClassID.getID());
		if (watchId != null) {
			try {
				LOGGER.info("Removing watch on Batch Class:" + batchClassID.getID());
				JNotify.removeWatch(watchId);
				LOGGER.info("Watch successfully removed");
			} catch (JNotifyException e) {
				LOGGER.error("Unable to remove watch on batch class: " + batchClassID.getID(), e);
			}
		}
	}

	private boolean isFolderPresent(String folderName, List<BatchInstance> batches) {
		boolean isPresent = false;
		for (BatchInstance batchInstance : batches) {
			String uncSubFolder = batchInstance.getBatchName();
			if (uncSubFolder.equals(folderName)) {
				isPresent = true;
				break;
			}
		}
		return isPresent;
	}

}
