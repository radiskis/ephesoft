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

package com.ephesoft.dcma.monitor.service;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.ephesoft.dcma.monitor.service.foldermonitorconstants.FolderMoniterConstants;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.FileUtils;

/**
 * This class is impl class for folder monitor service.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.monitor.service.FolderMonitorService
 */
public class FolderMonitorServiceImpl implements FolderMonitorService, ApplicationListener<FolderModificationEvent> {

	/**
	 * An instance of Logger for proper logging.
	 */
	protected static final Logger LOGGER = LoggerFactory.getLogger(FolderMonitorServiceImpl.class);

	/**
	 * An instance of {@link BatchClassService}.
	 */
	@Autowired
	private BatchClassService batchClassService;
	/**
	 * An instance of {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;
	/**
	 * An instance of {@link BatchSchemaService}.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * An instance of {@link EphesoftFolderListner}.
	 */
	@Autowired
	private EphesoftFolderListner listner;

	/**
	 * A Map to store the mapping between batch class id and watch id.
	 */
	final private Map<String, Integer> batchClassIdVsWatchIdMap = new HashMap<String, Integer>();
	/**
	 * Private FileChannel type.
	 */
	private FileChannel fileChannel;
	/**
	 * A boolean type for putting check on running states.
	 */
	private boolean running = false;

	/**
	 * A variable to store the wait time.
	 */
	private long waitTime;
	
	@Override
	public void start() {
		List<BatchClass> batchClasses = batchClassService.getAllBatchClassesExcludeDeleted();
		pickOldUnPickedBatches(batchClasses);
		for (BatchClass batchClass : batchClasses) {
			addWatch(batchClass);
			lockLocalFolder(batchClass);
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
					if (folderName != null && !isFolderPresent(folderName, batches)
							&& !FileUtils.checkForFile(uncFolderPath + File.separator + folderName)) {
						createBatchInstance(batchClass, uncFolder, folderName);
					}
				}
			}
		}

	}

	private void createBatchInstance(BatchClass batchClass, File uncFolder, String batchFolderName) {
		String uncSubFolder = new File(uncFolder + File.separator + batchFolderName).getAbsolutePath();
		final String batchClassIdentifier = batchClass.getIdentifier();
		batchInstanceService.createBatchInstance(batchClass, uncSubFolder, batchClassService
				.getSystemFolderForBatchClassIdentifier(batchClassIdentifier), batchClass.getPriority());

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
	/**
	 * setter for waitTime.
	 * @param waitTime long
	 */
	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}
	/**
	 * getter for waitTime.
	 * @return long
	 */
	public long getWaitTime() {
		return waitTime;
	}
	/**
	 * Handle an application event.
	 * @param event {@link FolderModificationEvent}
	 */
	@Override
	public void onApplicationEvent(final FolderModificationEvent event) {

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {

				FolderDetail folderDetail = event.getFolderDetail();
				File folderToCheck = null;
				boolean isFile = FileUtils.checkForFile(folderDetail.getFullPath());

				// Move the file to newly created batch folder.
				if (isFile) {
					performFileCopyOperation(folderDetail);
				}
				LOGGER.debug(FolderMoniterConstants.FOLDER_NAME + folderDetail.getFullPath() + " is File :: " + isFile);
				if (folderDetail != null && !isFile) {
					String folderPath = folderDetail.getFullPath();
					Set<String> inspectedFiles = new HashSet<String>();
					folderToCheck = new File(folderPath);
					if (folderToCheck != null && folderToCheck.exists()) {
						String[] fileList = folderToCheck.list();
						fileList = waitingFileListUpdate(folderToCheck, fileList);
						LOGGER.debug(FolderMoniterConstants.FOLDER_NAME + folderDetail.getFullPath() + " file length :: " + fileList.length);
						boolean fileCopiedSuccessfully = false;
						while (!fileCopiedSuccessfully) {
							boolean isFirstTime = true;
							isFirstTime = waitingForFileListUpdation(inspectedFiles, fileList, isFirstTime);
							fileCopiedSuccessfully = checkFileCopyOperation(inspectedFiles, fileList, fileCopiedSuccessfully,
									isFirstTime);
							for (String filePath : fileList) {
								LOGGER.debug(FolderMoniterConstants.FOLDER_NAME + folderDetail.getFullPath() + "File name :: " + filePath);
								checkFilePresent(folderDetail, folderPath, inspectedFiles, filePath);
							}
							fileList = folderToCheck.list();
						}
					}
				}
				LOGGER.info("Folder :: " + folderDetail.getFullPath() + " isFile_2 :: " + isFile);
				if (!isFile) {
					LOGGER.info("Creating batch instance.");
					List<BatchClass> batchClasses = batchClassService.getAllBatchClassesExcludeDeleted();
					for (BatchClass batchClass : batchClasses) {
						if (batchClass.getUncFolder().equals(folderDetail.getParentPath())) {
							final String batchClassIdentifier = batchClass.getIdentifier();
							batchInstanceService.createBatchInstance(batchClass, folderDetail.getFullPath(), batchClassService
									.getSystemFolderForBatchClassIdentifier(batchClassIdentifier), batchClass.getPriority());
							break;
						}
					}
				}
			}


		});
		thread.start();
	}
	/**
	 * API to monitor the batch class given it's BatchClassID.
	 * @param batchClassID {@link BatchClassID}
	 */
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

	private void lockLocalFolder(BatchClass batchClass) {
		String lockFileName = batchClass.getSystemFolder() + File.separator + "_lock";

		try {
			File lockFile = new File(lockFileName);
			fileChannel = new RandomAccessFile(lockFile, "rw").getChannel();

			FileLock lock = null;

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

	private void checkFilePresent(FolderDetail folderDetail, String folderPath, Set<String> inspectedFiles, String filePath) {
		if (!inspectedFiles.contains(filePath)) {
			String inputFilePath = folderPath + File.separator + filePath;
			File fileToCheck = new File(inputFilePath);
			if (fileToCheck.exists()) {
				checkingDirectoryAndCopyData(folderDetail, inspectedFiles, filePath, inputFilePath, fileToCheck);
			}
		}
	}

	private void checkingDirectoryAndCopyData(FolderDetail folderDetail, Set<String> inspectedFiles, String filePath,
			String inputFilePath, File fileToCheck) {
		if (fileToCheck.isDirectory()) {
			String[] fileListOfFolder = fileToCheck.list();
			Set<String> inspectedFilesInside = new HashSet<String>();
			while (fileListOfFolder.length != inspectedFilesInside.size()) {
				for (String filePathInside : fileListOfFolder) {
					if (!inspectedFilesInside.contains(filePathInside)) {
						String inputFilePathFolder = fileToCheck + File.separator + filePathInside;
						File fileToCheckInside = new File(inputFilePathFolder);
						if (fileToCheckInside.exists()) {

							RandomAccessFile randomAccessFileInside = null;
							try {
								randomAccessFileInside = new RandomAccessFile(fileToCheckInside, "rw");
								inspectedFilesInside.add(filePathInside);
							} catch (Exception ex) {
								LOGGER.info("Error in opening file " + inputFilePathFolder);
							} finally {
								closingFile(randomAccessFileInside);
							}

						}
					}
				}
				fileListOfFolder = fileToCheck.list();

			}
			inspectedFiles.add(filePath);
		} else {
			RandomAccessFile randomAccessFile = null;
			try {
				LOGGER.debug(FolderMoniterConstants.FOLDER_NAME + folderDetail.getFullPath() + " Adding to inspected file ::" + filePath);
				randomAccessFile = new RandomAccessFile(fileToCheck, "rw");
				inspectedFiles.add(filePath);
			} catch (Exception ex) {
				LOGGER.info("Error in opening file " + inputFilePath);
			} finally {
				closingFile(randomAccessFile);
			}
		}
	}

	private void closingFile(RandomAccessFile randomAccessFile) {
		if (randomAccessFile != null) {
			try {
				randomAccessFile.close();
			} catch (java.io.IOException e) {
				LOGGER.info("Unable to close file: " + e.getMessage());
			}
		} else {
			try {
				Thread.sleep(FolderMoniterConstants.SLEEP_TIME_5000);
			} catch (InterruptedException ex) {
				LOGGER.info(ex.getMessage());
			}
		}
	}

	private boolean checkFileCopyOperation(Set<String> inspectedFiles, String[] fileList, boolean fileCopiedSuccessfully,
			boolean isFirstTime) {
		boolean localFileCopiedSuccessfully = fileCopiedSuccessfully;
		if (!isFirstTime && (fileList.length == inspectedFiles.size())) {
			localFileCopiedSuccessfully = true;
		}
		return localFileCopiedSuccessfully;
	}

	private boolean waitingForFileListUpdation(Set<String> inspectedFiles, String[] fileList, boolean isFirstTime) {
		boolean localFirstTime = isFirstTime;
		if (localFirstTime && (fileList.length == inspectedFiles.size())) {
			localFirstTime = false;
			try {
				Thread.sleep(FolderMoniterConstants.SLEEP_TIME_30000);
			} catch (InterruptedException e) {
			}
		}
		return localFirstTime;
	}

	private String[] waitingFileListUpdate(File folderToCheck, String[] fileList) {
		String[] localFileList = fileList;
		if (localFileList == null) {
			try {
				Thread.sleep(FolderMoniterConstants.SLEEP_TIME_1000);
				localFileList = folderToCheck.list();
			} catch (InterruptedException e) {
			}
		}
		return localFileList;
	}
	/**
	 * This method is used to remove the watch form batch class with given id.
	 * @param batchClassID {@link BatchClassID}
	 */
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

	private void performFileCopyOperation(FolderDetail folderDetail) {
		LOGGER.info("Copying file into batch folder of current timestamp" + folderDetail.getFolderName());
		File file = new File(folderDetail.getFullPath());
		try {
			long startTimeWaitCopyOperation = System.currentTimeMillis();
			String timeOutString = ApplicationConfigProperties.getApplicationConfigProperties().getProperty(FolderMoniterConstants.BATCH_COPY_TIMEOUT);
			long timeoutLimit = 0L;
			try {
				timeoutLimit = Long.valueOf(timeOutString) * FolderMoniterConstants.SLEEP_TIME_1000L;
			} catch (Exception exception) {
				timeoutLimit = FolderMoniterConstants.SLEEP_TIME_3600000L;
			}

			// Wait for file to be copied completely
			while (true) {
				if (!checkForFileCopied(file) && (System.currentTimeMillis() - startTimeWaitCopyOperation) < timeoutLimit) {
					continue;
				} else {
					break;
				}
			}
			// while (!checkForFileCopied(file) && (System.currentTimeMillis() - startTimeWaitCopyOperation) < timeoutLimit);
			String folderName = folderDetail.getFolderName();
			long creationTime = System.currentTimeMillis();
			int indexOf = folderName.lastIndexOf(FolderMoniterConstants.DOT);
			if (indexOf != -1) {
				folderName = folderName.replace(folderName.charAt(indexOf), FolderMoniterConstants.HYPHEN_CHAR);
			}
			String newFolderName = folderName + FolderMoniterConstants.HYPHEN_CHAR + creationTime;
			String baseFolderLocation = batchSchemaService.getBaseFolderLocation();
			String parentPath = folderDetail.getParentPath();

			boolean success = FileUtils.moveFile(folderDetail.getFullPath(), baseFolderLocation + File.separator
					+ FolderMoniterConstants.TEMPORARY_FOLDER_NAME + File.separator + newFolderName + File.separator + folderDetail.getFolderName());
			if (success) {
				FileUtils.moveFile(baseFolderLocation + File.separator + FolderMoniterConstants.TEMPORARY_FOLDER_NAME + File.separator + newFolderName,
						parentPath + File.separator + newFolderName);
			} else {
				LOGGER.info("Unable to copy file to current folder timestamp");
			}
		} catch (java.io.IOException ioException) {
			LOGGER.error("Error occurred while reading timeout limit from properties file");
		} catch (Exception exception) {
			LOGGER.error("Error occurred while copying file from watch folder to temp folder");
		}

	}

	private boolean checkForFileCopied(File fileToCheckInside) {
		boolean isFileCopied = false;
		if (fileToCheckInside.exists()) {

			RandomAccessFile randomAccessFileInside = null;
			try {
				randomAccessFileInside = new RandomAccessFile(fileToCheckInside, "rw");
			} catch (Exception ex) {
				LOGGER.info("Error in opening file " + fileToCheckInside.getAbsolutePath());
			} finally {
				isFileCopied = closingFileOperation(isFileCopied, randomAccessFileInside);
			}

		}
		return isFileCopied;
	}

	private boolean closingFileOperation(boolean isFileCopied, RandomAccessFile randomAccessFileInside) {
		boolean fileCopied = isFileCopied;
		if (randomAccessFileInside != null) {
			try {
				randomAccessFileInside.close();
				fileCopied = true;
			} catch (java.io.IOException e) {
				LOGGER.info("Unable to close file: " + e.getMessage());
			}
		} else {
			try {
				Thread.sleep(FolderMoniterConstants.SLEEP_TIME_5000);
			} catch (InterruptedException ex) {
				LOGGER.info(ex.getMessage());
			}
		}
		return fileCopied;
	}

}
