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

package com.ephesoft.dcma.core.component;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.ephesoft.dcma.util.FileUtils;

/**
 * @author Ephesoft
 * @version 1.0
 * 
 */
public class JAXB2Template {

	/**
	 * jaxb2Marshaller Jaxb2Marshaller.
	 */
	private final Jaxb2Marshaller jaxb2Marshaller;
	
	/**
	 * baseFolderLocation String.
	 */
	private String baseFolderLocation;
	
	/**
	 * localFolderLocation String.
	 */
	private String localFolderLocation;
	
	/**
	 * exportFolderLocation String.
	 */
	private String exportFolderLocation;
	
	/**
	 * baseHttpURL String.
	 */
	private String baseHttpURL;
	
	/**
	 * baseSampleFdLoc String.
	 */
	private String baseSampleFdLoc;
	
	/**
	 * sampleFolders String.
	 */
	private String sampleFolders;
	
	/**
	 * searchSampleName String.
	 */
	private String searchSampleName;
	
	/**
	 * searchIndexFolderName String.
	 */
	private String searchIndexFolderName;
	
	/**
	 * imageMagickBaseFolderName String.
	 */
	private String imageMagickBaseFolderName;
	
	/**
	 * fuzzyDBIndexFolderName String.
	 */
	private String fuzzyDBIndexFolderName;
	
	/**
	 * webScannerScannedImagesFolderPath String.
	 */
	private String webScannerScannedImagesFolderPath;
	
	/**
	 * emailFolderName String.
	 */
	private String emailFolderName;
	
	/**
	 * projectFilesBaseFolder String.
	 */
	private String projectFilesBaseFolder;
	
	/**
	 * testKVExtractionFolderName String.
	 */
	private String testKVExtractionFolderName;
	
	/**
	 * scriptFolderName String.
	 */
	private String scriptFolderName;
	
	/**
	 * cmisPluginMappingFolderName String.
	 */
	private String cmisPluginMappingFolderName;
	
	/**
	 * batchExportFolder String.
	 */
	private String batchExportFolder;
	
	/**
	 * batchClassSerializableFile String.
	 */
	private String batchClassSerializableFile;
	
	/**
	 * fileboundPluginMappingFolderName String.
	 */
	private String fileboundPluginMappingFolderName;
	
	/**
	 * validationScriptName String.
	 */
	private String validationScriptName;
	
	/**
	 * tempFolder String.
	 */
	private String tempFolder;
	
	/**
	 * testTableFolderName String.
	 */
	private String testTableFolderName;
	
	/**
	 * threadpoolLockFolder String.
	 */
	private String threadpoolLockFolder;
	
	/**
	 * addNewTableScriptName String.
	 */
	private String addNewTableScriptName;
	
	/**
	 * testFolderLocation String.
	 */
	private String testFolderLocation;
	
	/**
	 * scriptConfigFolderName String.
	 */
	private String scriptConfigFolderName;
	
	/**
	 * uploadBatchFolder String.
	 */
	private String uploadBatchFolder;
	
	/**
	 * testAdvancedKvExtractionFolder String.
	 */
	private String testAdvancedKvExtractionFolder;
	
	/**
	 * webServicesFolderPath String.
	 */
	private String webServicesFolderPath;
	
	/**
	 * advancedTestTableFolder String.
	 */
	private String advancedTestTableFolder;
	
	/**
	 * dbExportPluginMappingFolderName String.
	 */
	private String dbExportPluginMappingFolderName;

	/**
	 * Constructor.
	 * @param jaxb2Marshaller Jaxb2Marshaller
	 * @param baseFolderLocation String
	 * @param localFolderLocation String
	 * @param exportFolderLocation String
	 * @param baseHttpURL String
	 * @param baseSampleFdLoc String
	 * @param sampleFolders String
	 * @param searchSampleName String
	 * @param searchIndexFolderName String
	 * @param imageMagickBaseFolderName String
	 * @param fuzzyDBIndexFolderName String
	 * @param webScannerScannedImagesFolder StringPath
	 * @param emailFolderName String
	 * @param projectFilesBaseFolder String
	 * @param testKVExtFolder String
	 * @param scriptFolderName String
	 * @param cmisPluginMappingFolderName String
	 * @param batchExportFolder String
	 * @param batchClassSerializableFile String
	 * @param fileboundPluginMappingFolderName String
	 * @param validationScriptName String
	 * @param tempFolder String
	 * @param testTableFolderName String
	 * @param threadpoolLockFolder String
	 * @param testFolderLocation String
	 * @param scriptConfigFolderName String
	 * @param uploadBatchFolder String
	 * @param testAdvancedKvExtractionFolder String
	 * @param webServicesFolderPath String
	 * @param samplePatternFolderName String
	 * @param samplePatternFileName String
	 */
	public JAXB2Template(Jaxb2Marshaller jaxb2Marshaller, String baseFolderLocation, String localFolderLocation,
			String exportFolderLocation, String baseHttpURL, String baseSampleFdLoc, String sampleFolders, String searchSampleName,
			String searchIndexFolderName, String imageMagickBaseFolderName, String fuzzyDBIndexFolderName,
			String webScannerScannedImagesFolderPath, String emailFolderName, String projectFilesBaseFolder, String testKVExtFolder,
			String scriptFolderName, String cmisPluginMappingFolderName, String batchExportFolder, String batchClassSerializableFile,
			String fileboundPluginMappingFolderName, String validationScriptName, String tempFolder, String testTableFolderName,
			String threadpoolLockFolder, String addNewTableScriptName, String testFolderLocation, String scriptConfigFolderName,
			String uploadBatchFolder, String testAdvancedKvExtractionFolder, String webServicesFolderPath,
			String advancedTestTableFolder, String dbExportPluginMappingFolderName) {
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
		this.uploadBatchFolder = uploadBatchFolder;
		this.testAdvancedKvExtractionFolder = testAdvancedKvExtractionFolder;
		this.webServicesFolderPath = webServicesFolderPath;
		this.advancedTestTableFolder = advancedTestTableFolder;
		this.dbExportPluginMappingFolderName = dbExportPluginMappingFolderName;
	}

	/**
	 * To set Base Sample Fd Loc.
	 * @param baseSampleFdLoc String
	 */
	public void setBaseSampleFdLoc(String baseSampleFdLoc) {
		this.baseSampleFdLoc = baseSampleFdLoc;
	}

	/**
	 * Constructor.
	 * @param jaxb2Marshaller Jaxb2Marshaller
	 * @param localFolderLocation String
	 */
	public JAXB2Template(Jaxb2Marshaller jaxb2Marshaller, String localFolderLocation) {
		super();
		this.jaxb2Marshaller = jaxb2Marshaller;
		this.localFolderLocation = FileUtils.getAbsoluteFilePath(localFolderLocation);
	}

	/**
	 * To get Jaxb2 Marshaller.
	 * @return the jaxb2Marshaller
	 */
	public Jaxb2Marshaller getJaxb2Marshaller() {
		return jaxb2Marshaller;
	}

	/**
	 * To get Local Folder Location.
	 * @return the localFolderLocation
	 */
	public String getLocalFolderLocation() {
		return localFolderLocation;
	}

	/**
	 * To get Export Folder Location.
	 * @return the exportFolderLocation
	 */
	public String getExportFolderLocation() {
		return exportFolderLocation;
	}

	/**
	 * To get Base Http URL.
	 * @return the baseHttpURL
	 */
	public String getBaseHttpURL() {
		return baseHttpURL;
	}

	/**
	 * To get Base Sample Fd Loc.
	 * @return the baseSampleFdLoc
	 */
	public String getBaseSampleFdLoc() {
		return baseSampleFdLoc;
	}

	/**
	 * To get Sample Folders.
	 * @return the sampleFolders
	 */
	public String getSampleFolders() {
		return sampleFolders;
	}

	/**
	 * To get Search Sample Name.
	 * @return the searchSampleName
	 */
	public String getSearchSampleName() {
		return searchSampleName;
	}

	/**
	 * To get Search Index Folder Name.
	 * @return the searchIndexFolderName
	 */
	public String getSearchIndexFolderName() {
		return searchIndexFolderName;
	}

	/**
	 * To get Image Magick Base Folder Name.
	 * @return the imageMagickBaseFolderName
	 */
	public String getImageMagickBaseFolderName() {
		return imageMagickBaseFolderName;
	}

	/**
	 * To get Fuzzy DB Index Folder Name.
	 * @return the fuzzyDBIndexFolderName
	 */
	public String getFuzzyDBIndexFolderName() {
		return fuzzyDBIndexFolderName;
	}

	/**
	 * Api to return the folder path of the scanned images.
	 * 
	 * @return the folder path
	 */
	public String getWebScannerScannedImagesFolderPath() {
		return webScannerScannedImagesFolderPath;
	}

	/**
	 * Api to return the base folder location.
	 * 
	 * @return the location of base folder
	 */
	public String getBaseFolderLocation() {
		return baseFolderLocation;
	}

	/**
	 * To get Project Files Base Folder.
	 * @return the projectFilesBaseFolder
	 */
	public String getProjectFilesBaseFolder() {
		return projectFilesBaseFolder;
	}

	/**
	 * To set Project Files Base Folder.
	 * @param projectFilesBaseFolder the projectFilesBaseFolder to set
	 */
	public void setProjectFilesBaseFolder(String projectFilesBaseFolder) {
		this.projectFilesBaseFolder = projectFilesBaseFolder;
	}

	/**
	 * Api to return the email folder location.
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
	 * To get Script Folder Name.
	 * @return the scriptFolderName
	 */
	public String getScriptFolderName() {
		return scriptFolderName;
	}

	/**
	 * To get Cmis Plugin Mapping Folder Name.
	 * @return the cmisPluginMappingFolderName
	 */
	public String getCmisPluginMappingFolderName() {
		return cmisPluginMappingFolderName;
	}

	/**
	 * Api to return the batch export folder location.
	 * 
	 * @return location of batch Export Folder
	 */
	public String getBatchExportFolder() {
		return batchExportFolder;
	}

	/**
	 * Api to return the batch class serializable file name.
	 * 
	 * @return name of batch Class Serializable Description as appearing on the UI
	 */
	public String getBatchClassSerializableFile() {
		return batchClassSerializableFile;
	}

	/**
	 * Api to return the filebound plugin mapping folder name.
	 * 
	 * @return Name of filebound plugin mapping folder
	 */

	public String getFileboundPluginMappingFolderName() {
		return fileboundPluginMappingFolderName;
	}

	/**
	 * API to return the Validation Script name.
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
	 * API to get the KV Extraction test folder name.
	 * 
	 * @return name of the folder
	 */
	public String getTestTableFolderName() {
		return testTableFolderName;
	}

	/**
	 * API to return the thread pool lock folder. This folder signifies if any thread is currently running for this batch instance at
	 * any point of time.
	 * 
	 * @return String
	 */
	public String getThreadpoolLockFolder() {
		return threadpoolLockFolder;
	}

	/**
	 * API to get the thread pool lock folder. This folder signifies if any thread is currently running for this batch instance at any
	 * point of time.
	 * 
	 * @return String
	 */
	public String getAddNewTableScriptName() {
		return addNewTableScriptName;
	}

	/**
	 * API to set the threadpool lock folder. This folder signifies if any thread is currently running for this batch instance at any
	 * point of time.
	 * 
	 * @param threadpoolLockFolder String
	 */
	public void setThreadpoolLockFolder(String threadpoolLockFolder) {
		this.threadpoolLockFolder = threadpoolLockFolder;
	}

	/**
	 * API to set the local folder location.
	 * 
	 * @param localFolderLocation String
	 */
	public void setLocalFolderLocation(String localFolderLocation) {
		this.localFolderLocation = localFolderLocation;
	}

	/**
	 * API to return test data folder.
	 * 
	 * @return String
	 */
	public String getTestFolderLocation() {
		return testFolderLocation;
	}

	/**
	 * API to set test data folder.
	 *  
	 * @param testFolderLocation String
	 */
	public void setTestFolderLocation(String testFolderLocation) {
		this.testFolderLocation = testFolderLocation;
	}

	/**
	 * API to set base folder.
	 * 
	 * @param baseFolderLocation String
	 */
	public void setBaseFolderLocation(String baseFolderLocation) {
		this.baseFolderLocation = baseFolderLocation;
	}

	/**
	 * To get Script Config Folder Name.
	 * @return String
	 */
	public String getScriptConfigFolderName() {
		return scriptConfigFolderName;
	}

	/**
	 * To set Script Config Folder Name.
	 * @param scriptConfigFolderName String
	 */
	public void setScriptConfigFolderName(String scriptConfigFolderName) {
		this.scriptConfigFolderName = scriptConfigFolderName;
	}

	/**
	 * To get Upload Batch Folder.
	 * @return String
	 */
	public String getUploadBatchFolder() {
		return uploadBatchFolder;
	}

	/**
	 * To set Upload Batch Folder.
	 * @param uploadBatchFolder String
	 */
	public void setUploadBatchFolder(String uploadBatchFolder) {
		this.uploadBatchFolder = uploadBatchFolder;
	}

	/**
	 * To get Web Services Folder Path.
	 * @return String
	 */ 
	public String getWebServicesFolderPath() {
		return webServicesFolderPath;
	}

	/**
	 * To set Web Services Folder Path.
	 * @param webServicesFolderPath String
	 */
	public void setWebServicesFolderPath(String webServicesFolderPath) {
		this.webServicesFolderPath = webServicesFolderPath;
	}

	/**
	 * To get Test Advanced Kv Extraction Folder Name.
	 * @return String
	 */
	public String getTestAdvancedKvExtractionFolderName() {
		return testAdvancedKvExtractionFolder;
	}

	/**
	 * To get Advanced Test Table Folder.
	 * @return String
	 */
	public String getAdvancedTestTableFolder() {
		return advancedTestTableFolder;
	}

	/**
	 * To set Advanced Test Table Folder.
	 * @param advancedTestTableFolder String
	 */
	public void setAdvancedTestTableFolder(String advancedTestTableFolder) {
		this.advancedTestTableFolder = advancedTestTableFolder;
	}

	/**
	 * To get Db Export Plugin Mapping Folder Name.
	 * @return the dbExportPluginMappingFolderName
	 */
	public String getDbExportPluginMappingFolderName() {
		return dbExportPluginMappingFolderName;
	}
}
