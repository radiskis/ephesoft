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

package com.ephesoft.dcma.core.component;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.ephesoft.dcma.util.FileUtils;

/**
 * @author Ephesoft
 * @version 1.0
 * 
 */
public class JAXB2Template {

	private Jaxb2Marshaller jaxb2Marshaller;
	private String baseFolderLocation;
	private String localFolderLocation;
	private String exportFolderLocation;
	private String baseHttpURL;
	private String baseSampleFdLoc;
	private String sampleFolders;
	private String searchSampleName;
	private String searchIndexFolderName;
	private String imageMagickBaseFolderName;
	private String fuzzyDBIndexFolderName;
	private String webScannerScannedImagesFolderPath;
	private String emailFolderName;
	private String projectFilesBaseFolder;
	private String testKVExtractionFolderName;
	private String scriptFolderName;
	private String cmisPluginMappingFolderName;
	private String batchExportFolder;
	private String batchClassSerializableFile;
	private String fileboundPluginMappingFolderName;
	private String validationScriptName;
	private String tempFolder;
	private String testTableFolderName;
	private String threadpoolLockFolder;
	private String addNewTableScriptName;
	private String testFolderLocation;
	private String scriptConfigFolderName;	
	
	

	/**
	 * @param jaxb2Marshaller
	 * @param baseFolderLocation
	 * @param localFolderLocation
	 * @param exportFolderLocation
	 * @param baseHttpURL
	 * @param baseSampleFdLoc
	 * @param sampleFolders
	 * @param searchSampleName
	 * @param searchIndexFolderName
	 * @param imageMagickBaseFolderName
	 * @param fuzzyDBIndexFolderName
	 * @param webScannerScannedImagesFolderPath
	 * @param emailFolderName
	 * @param projectFilesBaseFolder
	 * @param testKVExtFolder
	 * @param scriptFolderName
	 * @param cmisPluginMappingFolderName
	 * @param batchExportFolder
	 * @param batchClassSerializableFile
	 * @param fileboundPluginMappingFolderName
	 * @param validationScriptName
	 * @param tempFolder
	 * @param testTableFolderName
	 * @param threadpoolLockFolder
	 * @param testFolderLocation
	 * @param scriptConfigFolderName
	 */
	public JAXB2Template(Jaxb2Marshaller jaxb2Marshaller, String baseFolderLocation, String localFolderLocation,
			String exportFolderLocation, String baseHttpURL, String baseSampleFdLoc, String sampleFolders, String searchSampleName,
			String searchIndexFolderName, String imageMagickBaseFolderName, String fuzzyDBIndexFolderName,
			String webScannerScannedImagesFolderPath, String emailFolderName, String projectFilesBaseFolder, String testKVExtFolder,
			String scriptFolderName, String cmisPluginMappingFolderName, String batchExportFolder, String batchClassSerializableFile,
			String fileboundPluginMappingFolderName, String validationScriptName, String tempFolder, String testTableFolderName,
			String threadpoolLockFolder,String addNewTableScriptName, String testFolderLocation,
			String scriptConfigFolderName) {
		super();
		this.jaxb2Marshaller = jaxb2Marshaller;
		this.baseFolderLocation = baseFolderLocation;
		this.localFolderLocation = FileUtils.getAbsoluteFilePath(localFolderLocation);
		this.exportFolderLocation = FileUtils.getAbsoluteFilePath(exportFolderLocation);
		this.baseHttpURL = baseHttpURL;
		this.baseSampleFdLoc = FileUtils.getAbsoluteFilePath(baseSampleFdLoc);
		this.sampleFolders = sampleFolders;
		this.searchSampleName = searchSampleName;
		this.searchIndexFolderName = searchIndexFolderName;
		this.imageMagickBaseFolderName = imageMagickBaseFolderName;
		this.fuzzyDBIndexFolderName = fuzzyDBIndexFolderName;
		this.webScannerScannedImagesFolderPath = webScannerScannedImagesFolderPath;
		this.emailFolderName = emailFolderName;
		this.projectFilesBaseFolder = projectFilesBaseFolder;
		this.testKVExtractionFolderName = testKVExtFolder;
		this.scriptFolderName = scriptFolderName;
		this.cmisPluginMappingFolderName = cmisPluginMappingFolderName;
		this.batchExportFolder = batchExportFolder;
		this.batchClassSerializableFile = batchClassSerializableFile;
		this.fileboundPluginMappingFolderName = fileboundPluginMappingFolderName;
		this.validationScriptName = validationScriptName;
		this.tempFolder = tempFolder;
		this.testTableFolderName = testTableFolderName;
		this.threadpoolLockFolder = threadpoolLockFolder;
		this.addNewTableScriptName = addNewTableScriptName;
		this.testFolderLocation = FileUtils.getAbsoluteFilePath(testFolderLocation);
		this.scriptConfigFolderName = scriptConfigFolderName;
	}

	
	public void setBaseSampleFdLoc(String baseSampleFdLoc) {
		this.baseSampleFdLoc = baseSampleFdLoc;
	}

	/**
	 * @param jaxb2Marshaller Jaxb2Marshaller
	 * @param localFolderLocation String
	 */
	public JAXB2Template(Jaxb2Marshaller jaxb2Marshaller, String localFolderLocation) {
		super();
		this.jaxb2Marshaller = jaxb2Marshaller;
		this.localFolderLocation = FileUtils.getAbsoluteFilePath(localFolderLocation);
	}

	/**
	 * @return the jaxb2Marshaller
	 */
	public Jaxb2Marshaller getJaxb2Marshaller() {
		return jaxb2Marshaller;
	}

	/**
	 * @return the localFolderLocation
	 */
	public String getLocalFolderLocation() {
		return localFolderLocation;
	}

	/**
	 * @return the exportFolderLocation
	 */
	public String getExportFolderLocation() {
		return exportFolderLocation;
	}

	/**
	 * @return the baseHttpURL
	 */
	public String getBaseHttpURL() {
		return baseHttpURL;
	}

	/**
	 * @return the baseSampleFdLoc
	 */
	public String getBaseSampleFdLoc() {
		return baseSampleFdLoc;
	}

	/**
	 * @return the sampleFolders
	 */
	public String getSampleFolders() {
		return sampleFolders;
	}

	/**
	 * @return the searchSampleName
	 */
	public String getSearchSampleName() {
		return searchSampleName;
	}

	/**
	 * @return the searchIndexFolderName
	 */
	public String getSearchIndexFolderName() {
		return searchIndexFolderName;
	}

	/**
	 * @return the imageMagickBaseFolderName
	 */
	public String getImageMagickBaseFolderName() {
		return imageMagickBaseFolderName;
	}

	/**
	 * @return the fuzzyDBIndexFolderName
	 */
	public String getFuzzyDBIndexFolderName() {
		return fuzzyDBIndexFolderName;
	}

	/**
	 * Api to return the folder path of the scanned images
	 * 
	 * @return the folder path
	 */
	public String getWebScannerScannedImagesFolderPath() {
		return webScannerScannedImagesFolderPath;
	}

	/**
	 * Api to return the base folder location
	 * 
	 * @return the location of base folder
	 */
	public String getBaseFolderLocation() {
		return baseFolderLocation;
	}

	/**
	 * @return the projectFilesBaseFolder
	 */
	public String getProjectFilesBaseFolder() {
		return projectFilesBaseFolder;
	}

	/**
	 * @param projectFilesBaseFolder the projectFilesBaseFolder to set
	 */
	public void setProjectFilesBaseFolder(String projectFilesBaseFolder) {
		this.projectFilesBaseFolder = projectFilesBaseFolder;
	}

	/**
	 * Api to return the email folder location
	 * 
	 * @return name of the folder
	 */
	public String getEmailFolderName() {
		return emailFolderName;
	}

	/**
	 * Api to get the KV Extraction test folder name.
	 * 
	 * @return name of the folder
	 */
	public String getTestKVExtractionFolderName() {
		return testKVExtractionFolderName;
	}

	/**
	 * @return the scriptFolderName
	 */
	public String getScriptFolderName() {
		return scriptFolderName;
	}

	/**
	 * @return the cmisPluginMappingFolderName
	 */
	public String getCmisPluginMappingFolderName() {
		return cmisPluginMappingFolderName;
	}

	/**
	 * Api to return the batch export folder location
	 * 
	 * @return location of batch Export Folder
	 */
	public String getBatchExportFolder() {
		return batchExportFolder;
	}

	/**
	 * Api to return the batch class serializable file name
	 * 
	 * @return name of batch Class Serializable Description as appearing on the UI
	 */
	public String getBatchClassSerializableFile() {
		return batchClassSerializableFile;
	}

	/**
	 * Api to return the filebound plugin mapping folder name
	 * 
	 * @return Name of filebound plugin mapping folder
	 */

	public String getFileboundPluginMappingFolderName() {
		return fileboundPluginMappingFolderName;
	}

	/**
	 * Api to return the Validation Script name
	 * 
	 * @return Name of Script File
	 */
	public String getValidationScriptName() {
		return validationScriptName;
	}

	public String getTempFolder() {
		return tempFolder;
	}

	public void setTempFolder(String tempFolder) {
		this.tempFolder = tempFolder;
	}

	/**
	 * Api to get the KV Extraction test folder name.
	 * 
	 * @return name of the folder
	 */
	public String getTestTableFolderName() {
		return testTableFolderName;
	}

	/**
	 * API to return the threadpool lock folder. This folder signifies if any thread is currently running for this batch instance at
	 * any point of time.
	 * 
	 * @return
	 */
	public String getThreadpoolLockFolder() {
		return threadpoolLockFolder;
	}
	
	/** API to set the threadpool lock folder.
	 * This folder signifies if any thread is currently running for this batch instance at any point of time.
	 * 
	 * @param threadpoolLockFolder
	 */
	
	public String getAddNewTableScriptName() {
		return addNewTableScriptName;
	}
	
	

	/**
	 * API to set the threadpool lock folder. This folder signifies if any thread is currently running for this batch instance at any
	 * point of time.
	 * 
	 * @param threadpoolLockFolder
	 */
	public void setThreadpoolLockFolder(String threadpoolLockFolder) {
		this.threadpoolLockFolder = threadpoolLockFolder;
	}

	/**
	 * API to set the local folder location.
	 * 
	 * @param localFolderLocation
	 */
	public void setLocalFolderLocation(String localFolderLocation) {
		this.localFolderLocation = localFolderLocation;
	}

	/**
	 * API to return test data folder.
	 * 
	 * @return
	 */
	public String getTestFolderLocation() {
		return testFolderLocation;
	}

	/**
	 * API to set test data folder.
	 * 
	 * @param testFolderLocation
	 */
	public void setTestFolderLocation(String testFolderLocation) {
		this.testFolderLocation = testFolderLocation;
	}

	/**
	 * API to set base folder.
	 * 
	 * @param baseFolderLocation
	 */
	public void setBaseFolderLocation(String baseFolderLocation)
	{
		this.baseFolderLocation = baseFolderLocation;
	}
	
	
	public String getScriptConfigFolderName() {
		return scriptConfigFolderName;
	}
	
	public void setScriptConfigFolderName(String scriptConfigFolderName) {
		this.scriptConfigFolderName = scriptConfigFolderName;
	}
	
}
