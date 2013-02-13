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

package com.ephesoft.dcma.gwt.foldermanager.client;

import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.core.common.FileWrapper;
import com.ephesoft.dcma.gwt.core.client.DCMARemoteService;
import com.ephesoft.dcma.gwt.core.shared.exception.GWTException;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("folderManagementService")
public interface FolderManagerService extends DCMARemoteService {

	/**
	 * API to fetch the contents of a file/folder
	 * 
	 * @param file
	 * @return
	 */
	List<FileWrapper> getContents(FileWrapper wrapper) throws GWTException;

	/**
	 * API to get the files/folders on a path
	 * 
	 * @param file
	 * @return
	 */
	List<FileWrapper> getContents(String path) throws GWTException;

	/**
	 * API to delete a file/folder according to the specified path
	 * 
	 * @param absoluteName
	 * @return
	 */
	Boolean deleteFile(String absolutePath);

	/**
	 * API to rename a file/folder
	 * 
	 * @param absoluteName
	 * @param newName
	 * @param folderPath
	 * @return
	 */
	Boolean renameFile(String absoluteName, String newName, String folderPath);

	/**
	 * API to delete all files/folders contained in the specified paths
	 * 
	 * @param absolutePaths
	 * @return
	 */
	String deleteFiles(List<String> absolutePaths);

	/**
	 * API to fetch the parent folder for Folder Manager
	 * 
	 * @return
	 */
	String getParentFolder();

	/**
	 * API to copy files to specified path
	 * 
	 * @param copyFilesList
	 * @param currentFolderPath
	 * @return
	 * @throws GWTException
	 */
	Boolean copyFiles(List<String> copyFilesList, String currentFolderPath) throws GWTException;

	/**
	 * API to cut files and paste them at the specified path
	 * 
	 * @param cutFilesList
	 * @param currentFolderPath
	 * @return
	 * @throws GWTException
	 */
	Boolean cutFiles(List<String> cutFilesList, String currentFolderPath) throws GWTException;

	/**
	 * API to get the base http url
	 * 
	 * @return
	 */
	String getBaseHttpURL();

	/**
	 * API to create a new folder at the specified path
	 * 
	 * @param folderPath
	 * @return
	 * @throws GWTException
	 */
	String createNewFolder(String folderPath) throws GWTException;

	/**
	 * API to fetch all the batch classes available to the current user
	 * 
	 * @return
	 */
	Map<String, String> getBatchClassNames();

}
