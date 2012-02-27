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

package com.ephesoft.dcma.gwt.core.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BatchFolderListDTO implements IsSerializable {
	public static final String FOLDER_NAME = "folderName";
	public static final String CHECKED = "checked";
	public static final String ENABLED = "enabled";
	
	private String searchSampleName; //enabled
	private String searchIndexFolderName; 
	private String imageMagickBaseFolderName; // enabled
	private String fuzzyDBIndexFolderName;
	private String projectFilesBaseFolder;
	private String cmisPluginMapping;
	private String scripts;
	private String batchClassSerializableFile;
	private String fileboundPluginMappingFolderName;
	
	List<HashMap<String, String>> folderList = new ArrayList<HashMap<String,String>>();
	
	public String getSearchSampleName() {
		return searchSampleName;
	}
	
	public void setSearchSampleName(String searchSampleName) {
		this.searchSampleName = searchSampleName;
	}
	
	public String getSearchIndexFolderName() {
		return searchIndexFolderName;
	}
	
	public void setSearchIndexFolderName(String searchIndexFolderName) {
		this.searchIndexFolderName = searchIndexFolderName;
	}
	
	public String getImageMagickBaseFolderName() {
		return imageMagickBaseFolderName;
	}
	
	public void setImageMagickBaseFolderName(String imageMagickBaseFolderName) {
		this.imageMagickBaseFolderName = imageMagickBaseFolderName;
	}
	
	public String getFuzzyDBIndexFolderName() {
		return fuzzyDBIndexFolderName;
	}
	
	public void setFuzzyDBIndexFolderName(String fuzzyDBIndexFolderName) {
		this.fuzzyDBIndexFolderName = fuzzyDBIndexFolderName;
	}
	
	public String getProjectFilesBaseFolder() {
		return projectFilesBaseFolder;
	}
	
	public void setProjectFilesBaseFolder(String projectFilesBaseFolder) {
		this.projectFilesBaseFolder = projectFilesBaseFolder;
	}
	
	public String getCmisPluginMapping() {
		return cmisPluginMapping;
	}
	
	public void setCmisPluginMapping(String cmisPluginMapping) {
		this.cmisPluginMapping = cmisPluginMapping;
	}
	
	public String getScripts() {
		return scripts;
	}
	
	public void setScripts(String scripts) {
		this.scripts = scripts;
	}
	
	public void setFolderList(List<HashMap<String, String>> folderList) {
		this.folderList = folderList;
	}
	
	
	public String getBatchClassSerializableFile() {
		return batchClassSerializableFile;
	}
	
	public void setBatchClassSerializableFile(String batchClassSerializableFile) {
		this.batchClassSerializableFile = batchClassSerializableFile;
	}
	
	
	public String getFileboundPluginMappingFolderName() {
		return fileboundPluginMappingFolderName;
	}
	
	public void setFileboundPluginMappingFolderName(String fileboundPluginMappingFolderName) {
		this.fileboundPluginMappingFolderName = fileboundPluginMappingFolderName;
	}
	
	public void initFolderList() {
		HashMap<String,String> batchClassSerializableFilePropertyMap = new HashMap<String,String>();		
		batchClassSerializableFilePropertyMap.put(FOLDER_NAME, batchClassSerializableFile);
		batchClassSerializableFilePropertyMap.put(CHECKED, "true");
		batchClassSerializableFilePropertyMap.put(ENABLED, "false");
		folderList.add(batchClassSerializableFilePropertyMap);
		
		HashMap<String,String> searchIndexPropertyMap = new HashMap<String,String>();		
		searchIndexPropertyMap.put(FOLDER_NAME, searchIndexFolderName);
		searchIndexPropertyMap.put(CHECKED, "true");
		searchIndexPropertyMap.put(ENABLED, "false");
		folderList.add(searchIndexPropertyMap);

		HashMap<String,String> fuzzyDBPropertyMap = new HashMap<String,String>();		
		fuzzyDBPropertyMap.put(FOLDER_NAME, fuzzyDBIndexFolderName);
		fuzzyDBPropertyMap.put(CHECKED, "true");
		fuzzyDBPropertyMap.put(ENABLED, "false");
		folderList.add(fuzzyDBPropertyMap);

		HashMap<String,String> projectFilesPropertyMap = new HashMap<String,String>();		
		projectFilesPropertyMap.put(FOLDER_NAME, projectFilesBaseFolder);
		projectFilesPropertyMap.put(CHECKED, "true");
		projectFilesPropertyMap.put(ENABLED, "false");
		folderList.add(projectFilesPropertyMap);

		HashMap<String,String> cmisPropertyMap = new HashMap<String,String>();		
		cmisPropertyMap.put(FOLDER_NAME, cmisPluginMapping);
		cmisPropertyMap.put(CHECKED, "true");
		cmisPropertyMap.put(ENABLED, "false");
		folderList.add(cmisPropertyMap);

		HashMap<String,String> scriptsPropertyMap = new HashMap<String,String>();		
		scriptsPropertyMap.put(FOLDER_NAME, scripts);
		scriptsPropertyMap.put(CHECKED, "true");
		scriptsPropertyMap.put(ENABLED, "false");
		folderList.add(scriptsPropertyMap);

		/*HashMap<String,String> fileboundPluginPropertyMap = new HashMap<String,String>();		
		fileboundPluginPropertyMap.put(FOLDER_NAME, fileboundPluginMappingFolderName);
		fileboundPluginPropertyMap.put(CHECKED, "true");
		fileboundPluginPropertyMap.put(ENABLED, "false");
		folderList.add(fileboundPluginPropertyMap);*/
		
		HashMap<String,String> imageMagickPropertyMap = new HashMap<String,String>();		
		imageMagickPropertyMap.put(FOLDER_NAME, imageMagickBaseFolderName);
		imageMagickPropertyMap.put(CHECKED, "false");
		imageMagickPropertyMap.put(ENABLED, "true");
		folderList.add(imageMagickPropertyMap);
		
		HashMap<String,String> searchSamplePropertyMap = new HashMap<String,String>();		
		searchSamplePropertyMap.put(FOLDER_NAME, searchSampleName);
		searchSamplePropertyMap.put(CHECKED, "false");
		searchSamplePropertyMap.put(ENABLED, "true");
		folderList.add(searchSamplePropertyMap);
	}
	
	public List<HashMap<String, String>> getFolderList() {
		return folderList;
	}
}
