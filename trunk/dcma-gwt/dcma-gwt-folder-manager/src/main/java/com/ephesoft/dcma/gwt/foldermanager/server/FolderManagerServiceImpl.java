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

package com.ephesoft.dcma.gwt.foldermanager.server;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.common.FileWrapper;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.gwt.core.server.DCMARemoteServiceServlet;
import com.ephesoft.dcma.gwt.core.shared.exception.GWTException;
import com.ephesoft.dcma.gwt.foldermanager.client.FolderManagerService;
import com.ephesoft.dcma.gwt.foldermanager.shared.constant.FolderManagementConstants;
import com.ephesoft.dcma.gwt.foldermanager.shared.constant.FolderManagementMessages;

public class FolderManagerServiceImpl extends DCMARemoteServiceServlet
		implements FolderManagerService {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FolderManagerServiceImpl.class);
	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(
			FolderManagementConstants.DATE_FORMAT, Locale.getDefault());

	private static String dateFormat(long dateLong) {
		return DATE_FORMATTER.format(new Date(dateLong));
	}

	@Override
	public List<FileWrapper> getContents(FileWrapper file) throws GWTException {
		List<FileWrapper> fileWrapperList = null;
		File fsFile = new File(file.getPath());
		if (!fsFile.exists()) {
			LOGGER.info("File selected is not present in the system:"
					+ fsFile.getAbsolutePath());
			throw new GWTException(FolderManagementConstants.ERROR_TYPE_1);
		}
		if (fsFile.isDirectory()) {
			fileWrapperList = this.buildFilesList(fsFile.listFiles());
		}
		return fileWrapperList;
	}

	@Override
	public List<FileWrapper> getContents(String file) throws GWTException {
		return this.getContents(new FileWrapper(file));
	}

	private List<FileWrapper> buildFilesList(File[] files) {
		List<FileWrapper> result = new ArrayList<FileWrapper>();
		for (int i = 0; i < files.length; i++) {
			FileWrapper fileWrapper = new FileWrapper(files[i]
					.getAbsolutePath(), files[i].getName(), dateFormat(files[i]
					.lastModified()), files[i].lastModified());
			result.add(fileWrapper);
			if (files[i].isDirectory()) {
				fileWrapper.setIsDirectory();
				boolean isSubfolderContained = false;
				for (File file : files[i].listFiles()) {
					if (file.isDirectory()) {
						isSubfolderContained = true;
						break;
					}
				}
				fileWrapper.setSubFolderContained(isSubfolderContained);
			}
		}
		return result;
	}

	@Override
	public Boolean deleteFile(String absoluteName) {
		boolean deleteSuccessfull = true;
		File file = new File(absoluteName);
		try {
			if (file.isDirectory()) {
				FileUtils.deleteDirectory(file);
			} else {
				deleteSuccessfull = file.delete();
			}
		} catch (IOException e) {
			deleteSuccessfull = false;
		}

		return deleteSuccessfull;
	}

	@Override
	public Boolean renameFile(String oldName, String newName, String folderPath) {
		File orignalFile = new File(folderPath + File.separator + oldName);
		File renamedFile = new File(folderPath + File.separator + newName);
		return orignalFile.renameTo(renamedFile);
	}

	@Override
	public String deleteFiles(List<String> absolutePaths) {
		StringBuffer returnStringBuffer = new StringBuffer();
		for (String absolutePath : absolutePaths) {
			if (!deleteFile(absolutePath)) {
				returnStringBuffer.append(absolutePath);
				returnStringBuffer.append(FolderManagementConstants.SEMI_COLON);
			}
		}
		return returnStringBuffer.toString();
	}

	@Override
	public String getParentFolder() {
		BatchSchemaService batchSchemaService = this
				.getSingleBeanOfType(BatchSchemaService.class);
		return batchSchemaService.getBaseFolderLocation();
	}

	@Override
	public Boolean copyFiles(List<String> copyFilesList,
			String currentFolderPath) throws GWTException {
		for (String filePath : copyFilesList) {
			try {
				File srcFile = new File(filePath);
				if (srcFile.exists()) {
					srcFile.getName();
					String fileName = srcFile.getName();
					File copyFile = new File(currentFolderPath + File.separator
							+ fileName);
					if (copyFile.exists()) {
						if (copyFile.equals(srcFile)) {
							int counter = 0;
							while (copyFile.exists()) {
								String newFileName;
								if (counter == 0) {
									newFileName = FolderManagementConstants.COPY_OF
											+ fileName;
								} else {
									newFileName = FolderManagementConstants.COPY_OF
											+ FolderManagementConstants.OPENING_BRACKET
											+ counter
											+ FolderManagementConstants.CLOSING_BRACKET
											+ FolderManagementConstants.SPACE
											+ fileName;
								}
								counter++;
								copyFile = new File(currentFolderPath
										+ File.separator + newFileName);
							}
						} else {
							throw new GWTException(
									FolderManagementMessages.CANNOT_COMPLETE_COPY_PASTE_OPERATION_AS_THE_FILE_FOLDER
											+ fileName
											+ FolderManagementMessages.ALREADY_EXISTS);
						}
					}
					if (srcFile.isFile()) {
						FileUtils.copyFile(srcFile, copyFile, false);
					} else {
						FileUtils.forceMkdir(copyFile);
						FileUtils.copyDirectory(srcFile, copyFile, false);
					}
				} else {
					throw new GWTException(
							FolderManagementMessages.EXCEPTION_OCCURRED_WHILE_COPY_PASTE_OPERATION_FILE_NOT_FOUND
									+ FolderManagementConstants.QUOTES
									+ srcFile.getName()
									+ FolderManagementConstants.QUOTES);
				}
			} catch (IOException e) {
				LOGGER
						.error(FolderManagementMessages.EXCEPTION_OCCURRED_WHILE_COPY_PASTE_OPERATION_COULD_NOT_COMPLETE_OPERATION
								+ e.getMessage());
				throw new GWTException(
						FolderManagementMessages.EXCEPTION_OCCURRED_WHILE_COPY_PASTE_OPERATION_COULD_NOT_COMPLETE_OPERATION,
						e);
			}
		}
		return true;
	}

	@Override
	public Boolean cutFiles(List<String> cutFilesList, String currentFolderPath)
			throws GWTException {
		for (String filePath : cutFilesList) {
			File srcFile = new File(filePath);
			String fileName = srcFile.getName();
			if (srcFile.exists()) {
				try {
					String newPathName = currentFolderPath + File.separator
							+ srcFile.getName();
					File newFile = new File(newPathName);
					if (!newFile.exists()) {
						if (srcFile.isFile()) {
							FileUtils.moveFile(srcFile, newFile);
						} else {
							FileUtils.moveDirectory(srcFile, newFile);
						}
					} else {
						throw new GWTException(
								FolderManagementMessages.CANNOT_COMPLETE_CUT_PASTE_OPERATION_AS_THE_FILE_FOLDER
										+ fileName
										+ FolderManagementMessages.ALREADY_EXISTS);
					}
				} catch (IOException e) {
					LOGGER
							.error(FolderManagementMessages.EXCEPTION_OCCURRED_WHILE_CUT_PASTE_OPERATION_COULD_NOT_COMPLETE_OPERATION
									+ e.getMessage());
					throw new GWTException(
							FolderManagementMessages.EXCEPTION_OCCURRED_WHILE_CUT_PASTE_OPERATION_COULD_NOT_COMPLETE_OPERATION,
							e);
				}
			} else {
				throw new GWTException(
						FolderManagementMessages.EXCEPTION_OCCURRED_WHILE_CUT_PASTE_OPERATION_FILE_NOT_FOUND
								+ FolderManagementConstants.QUOTES
								+ srcFile.getName()
								+ FolderManagementConstants.QUOTES);
			}
		}
		return true;
	}

	@Override
	public String getBaseHttpURL() {
		BatchSchemaService batchSchemaService = this
				.getSingleBeanOfType(BatchSchemaService.class);
		return batchSchemaService.getBaseHttpURL();
	}

	@Override
	public String createNewFolder(String folderPath) throws GWTException {
		String folderName = FolderManagementConstants.NEW_FOLDER;
		File folder = new File(folderPath + File.separator + folderName);
		int counter = 0;
		while (folder.exists()) {
			String newFolderName = folderName
					+ FolderManagementConstants.OPENING_BRACKET + ++counter
					+ FolderManagementConstants.CLOSING_BRACKET;
			folder = new File(folderPath + File.separator + newFolderName);
		}
		String name = folder.getName();
		try {
			FileUtils.forceMkdir(folder);
		} catch (IOException e) {
			LOGGER
					.error(FolderManagementMessages.PROBLEM_OCCURRED_WHILE_CREATING_THE_FOLDER
							+ name);
			LOGGER.error(e.getMessage());
			throw new GWTException(
					FolderManagementMessages.PROBLEM_OCCURRED_WHILE_CREATING_THE_FOLDER
							+ name, e);
		}
		return name;
	}

	@Override
	public Map<String, String> getBatchClassNames() {
		Map<String, String> list = new LinkedHashMap<String, String>();
		BatchClassService batchClassService = this
				.getSingleBeanOfType(BatchClassService.class);
		Set<String> allGroups = getUserRoles();
		if (isSuperAdmin()) {
			String parentFolder = getParentFolder();
			File file = new File(parentFolder);
			String name = file.getName();
			list.put(name, null);
		}
		if (null != allGroups) {
			List<BatchClass> batchClassList = batchClassService
					.getAllBatchClassesByUserRoles(allGroups);
			if (null != batchClassList) {
				for (BatchClass batchClass : batchClassList) {
					String identifier = batchClass.getIdentifier();
					String description = batchClass.getDescription();
					if (description.length() > 30) {
						description = description.substring(0, 30);
					}
					list.put(identifier + " - " + description, identifier);
				}
			}
		}
		return list;
	}

}
