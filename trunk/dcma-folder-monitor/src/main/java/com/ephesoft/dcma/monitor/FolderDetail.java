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

package com.ephesoft.dcma.monitor;

import java.io.File;

import com.ephesoft.dcma.monitor.service.foldermonitorconstants.FolderMoniterConstants;

/**
 * This class handles the folder's details.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.monitor.service.FolderMonitorService
 */
public class FolderDetail implements Comparable<FolderDetail> {

	/**
	 * A String variable to store the parent path.
	 */
	final private String parentPath;
	/**
	 * A String variable to store folder name.
	 */
	final private String folderName;
	/**
	 * A variable to store creation time.
	 */
	final private long creationTime;
	/**
	 * Parameterized constructor.
	 * @param parentPath {@link String}
	 * @param folderName {@link String}
	 */
	public FolderDetail(final String parentPath, final String folderName) {
		this.parentPath = parentPath;
		this.folderName = folderName;
		this.creationTime = System.currentTimeMillis();
	}
	/**
	 * 
	 * @return {@link String}
	 */
	public String getParentPath() {
		return parentPath;
	}
	/**
	 * 
	 * @return {@link String}
	 */
	public String getFolderName() {
		return folderName;
	}
	/**
	 * 
	 * @return long
	 */
	public long getCreationTime() {
		return creationTime;
	}
	/**
	 * 
	 * @return {@link String}
	 */
	public String getFullPath() {
		return parentPath + File.separator + folderName;
	}
	/**
	 * Implementation of hashcode method.
	 * @return int
	 */
	@Override
	public int hashCode() {
		int prime = FolderMoniterConstants.HASH_CODE_CONSTANT_31;
		int result = 1;
		result = prime * result + ((folderName == null) ? 0 : folderName.hashCode());
		result = prime * result + ((parentPath == null) ? 0 : parentPath.hashCode());
		return result;
	}
	/**
	 * Implementation of equals method.
	 * @param obj {@link Object}
	 * @return boolean
	 */
	@Override
	public boolean equals(Object obj) {
		boolean isEqual = false;
		if (this == obj) {
			isEqual = true;
		} else if (obj == null) {
			isEqual = false;
		} else if (getClass() != obj.getClass()) {
			isEqual =  false;
		} else {
			FolderDetail other = (FolderDetail) obj;
			if (folderName == null) {
				if (other.folderName != null) {
					isEqual = false;
				}
			} else if (!folderName.equals(other.folderName)) {
				isEqual = false;
			} else if (parentPath == null) {
				if (other.parentPath != null) {
					isEqual = false;
				}
			} else if (!parentPath.equals(other.parentPath)) {
				isEqual = false;
			}
			isEqual = true;
		}
		return isEqual;
	}
	/**
	 * Implementation of compareTo method.
	 * @param folderDetails {@link FolderDetail}
	 * @return int
	 */
	@Override
	public int compareTo(final FolderDetail folderDetails) {
		int isEqual;
		if (this.equals(folderDetails)) {
			isEqual = 0;
		}
		if (this.getCreationTime() < folderDetails.getCreationTime()) {
			isEqual = -1;
		}
		isEqual = +1;
		
		return isEqual;
	}
}
