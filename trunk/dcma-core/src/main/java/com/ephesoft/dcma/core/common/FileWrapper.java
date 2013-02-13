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

package com.ephesoft.dcma.core.common;

import java.io.Serializable;

/**
 * This is wrapper class for file to get the file extension and its type.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see java.io.Serializable
 */
public class FileWrapper implements Serializable {

	/**
	 * Constant for DOT_CHAR.
	 */
	private static final char DOT_CHAR = '.';

	/**
	 * Constant for EMPTY_STRING.
	 */
	private static final String EMPTY_STRING = "";

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = 1407235313247426769L;

	/**
	 * name String.
	 */
	private String name;

	/**
	 * path String.
	 */
	private String path;

	/**
	 * kind FileType.
	 */
	private FileType kind;

	/**
	 * modifiedAt String.
	 */
	private String modifiedAt;

	/**
	 * modifiedTimeInSeconds Long.
	 */
	private Long modifiedTimeInSeconds;

	/**
	 * subFolderContained boolean.
	 */
	private boolean subFolderContained = true;

	/**
	 * Constructor.
	 * 
	 * @param path String
	 * @param name String
	 * @param modifiedAt String
	 */
	public FileWrapper(String path, String name, String modifiedAt) {
		this.name = name;
		this.path = path;
		this.modifiedAt = modifiedAt;
		this.kind = this.getFileType(extractFileExtention(name));

	}

	/**
	 * Constructor.
	 * 
	 * @param path String
	 * @param name String
	 */
	public FileWrapper(String path, String name) {
		this.name = name;
		this.path = path;
		this.kind = this.getFileType(extractFileExtention(name));
	}

	/**
	 * Constructor.
	 * 
	 * @param path String
	 */
	public FileWrapper(String path) {
		this.name = EMPTY_STRING;
		this.path = path;
		this.kind = FileType.DIR;
	}

	/**
	 * Constructor.
	 * 
	 * @param path String
	 * @param name String
	 * @param modifiedAt String
	 * @param subFolderContained boolean
	 * @param modifiedTimeInSeconds Long
	 */
	public FileWrapper(String path, String name, String modifiedAt, boolean subFolderContained, Long modifiedTimeInSeconds) {
		this.name = name;
		this.path = path;
		this.modifiedAt = modifiedAt;
		this.kind = this.getFileType(extractFileExtention(name));
		this.subFolderContained = subFolderContained;
		this.modifiedTimeInSeconds = modifiedTimeInSeconds;
		// this.setModifiedTimeInSeconds(modifiedTimeInSeconds);
	}

	/**
	 * Empty constructor for the class.
	 */
	public FileWrapper() {
		// Empty Constructor
	}

	/**
	 * Constructor.
	 * 
	 * @param path String
	 * @param name String
	 * @param modifiedAt String
	 * @param modifiedTimeInSeconds Long
	 */
	public FileWrapper(String path, String name, String modifiedAt, long modifiedTimeInSeconds) {
		this.name = name;
		this.path = path;
		this.modifiedAt = modifiedAt;
		this.kind = this.getFileType(extractFileExtention(name));
		this.modifiedTimeInSeconds = modifiedTimeInSeconds;
		// setModifiedTimeInSeconds(modifiedTimeInSeconds);
	}

	/**
	 * To get name.
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * To get path.
	 * 
	 * @return String
	 */
	public String getPath() {
		return path;
	}

	/**
	 * To get modified values.
	 * 
	 * @return String
	 */
	public String getModified() {
		return this.modifiedAt;
	}

	/**
	 * To get kind of file type.
	 * 
	 * @return FileType
	 */
	public FileType getKind() {
		return kind;
	}

	/**
	 * To set as directory.
	 */
	public void setIsDirectory() {
		this.kind = FileType.DIR;
	}

	private static String extractFileExtention(String file) {
		int dot = file.lastIndexOf(DOT_CHAR);
		return file.substring(dot + 1).toLowerCase();
	}

	private FileType getFileType(String ext) {
		FileType resultFileType = FileType.OTHER;
		if (ext.equalsIgnoreCase("doc") || ext.equalsIgnoreCase("pdf") || ext.equalsIgnoreCase("ppt") || ext.equalsIgnoreCase("docx")) {
			resultFileType = FileType.DOC;
		}
		if (ext.equalsIgnoreCase("avi") || ext.equalsIgnoreCase("wnv") || ext.equalsIgnoreCase("mpeg") || ext.equalsIgnoreCase("mov")
				|| ext.equalsIgnoreCase("mp3")) {
			resultFileType = FileType.MM;
		}
		if (ext.equalsIgnoreCase("gif") || ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("tif")
				|| ext.equalsIgnoreCase("psd") || ext.equalsIgnoreCase("bmp") || ext.equalsIgnoreCase("jpeg")) {
			resultFileType = FileType.IMG;
		}
		return resultFileType;

	}

	/**
	 * To set Sub Folder Contained.
	 * 
	 * @param subFolderContained boolean
	 */
	public void setSubFolderContained(boolean subFolderContained) {
		this.subFolderContained = subFolderContained;
	}

	/**
	 * To get Sub Folder Contained.
	 * 
	 * @return boolean
	 */
	public boolean isSubFolderContained() {
		return subFolderContained;
	}

	/**
	 * To set Modified Time in Seconds.
	 * 
	 * @param modifiedTimeInSeconds Long
	 */
	public void setModifiedTimeInSeconds(Long modifiedTimeInSeconds) {
		this.modifiedTimeInSeconds = modifiedTimeInSeconds;
	}

	/**
	 * To get Modified Time in Seconds.
	 * 
	 * @return Long
	 */
	public Long getModifiedTimeInSeconds() {
		return modifiedTimeInSeconds;
	}

}
