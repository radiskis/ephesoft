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

package com.ephesoft.dcma.cleanup;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.util.FileUtils;

public class CleanupComponent {

	/**
	 * Logger reference.
	 */
	private final static Logger LOGGER = LoggerFactory.getLogger(CleanupComponent.class);

	/**
	 * This method simply deletes the contents of the folder to be cleaned up. And after that it deletes the folder itself.
	 * 
	 * @param sFolderToBeCleaned {@link String}
	 * @throws DCMAApplicationException If the folder does not exits.
	 * @throws IOException If it is unable to delete the folder.
	 */
	public void execute(final String sFolderToBeCleaned) throws DCMAApplicationException, IOException {

		if (null == sFolderToBeCleaned) {
			throw new DCMAApplicationException("Invalid input parameter. Folder name is null.");
		}

		final File fFolderToBeCleaned = new File(sFolderToBeCleaned);
		if (!fFolderToBeCleaned.exists()) {
			LOGGER.info("Folder does not exist. Folder name : " + fFolderToBeCleaned);
			// throw new DCMAApplicationException("Folder does not exist. Folder name : " + fFolderToBeCleaned);
		}
		if (!fFolderToBeCleaned.isDirectory()) {
			LOGGER.info("The path specified does not point to a directory path : " + fFolderToBeCleaned);
			// throw new DCMAApplicationException("The path specified does not point to a directory path : " + fFolderToBeCleaned);
		}

		boolean deleteFiles = FileUtils.deleteDirectoryAndContents(fFolderToBeCleaned);
		if (deleteFiles) {
			LOGGER.info("Folder delete successfully. Folder name : " + fFolderToBeCleaned);
		} else {
			deleteFiles = FileUtils.deleteDirectoryAndContents(fFolderToBeCleaned);
			if (!fFolderToBeCleaned.exists()) {
				LOGGER.info("Folder delete successfully. Folder name : " + fFolderToBeCleaned);
			} else {
				throw new IOException("Unable to delete Folder : " + fFolderToBeCleaned);
			}
		}

	}

	/**
	 * This method deletes the specified file.
	 * 
	 * @param file {@link String}
	 * @throws DCMAApplicationException If the file does not exits.
	 * @throws IOException If it is unable to delete the file.
	 */
	public void deleteFile(final String file) throws DCMAApplicationException, IOException {

		if (null == file) {
			throw new DCMAApplicationException("Invalid input parameter. File name is null.");
		}

		final File fileToBeDeleted = new File(file);
		boolean deleteSuccess = true;
		if (fileToBeDeleted.exists()) {
			deleteSuccess = fileToBeDeleted.delete();
			if (deleteSuccess) {
				LOGGER.info("File delete successfully. File name : " + file);
			} else {
				deleteSuccess = fileToBeDeleted.delete();
				if (!fileToBeDeleted.exists()) {
					LOGGER.info("File delete successfully. File name : " + file);
				} else {
					throw new IOException("Unable to delete the file. File name : " + file);
				}
			}
		} else {
			LOGGER.info("File does not exist file name : " + file);
			//throw new DCMAApplicationException("File does not exist file name : " + file);
		}
	}

}
