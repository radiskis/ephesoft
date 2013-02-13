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

package com.ephesoft.dcma.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.ephesoft.dcma.constant.UtilConstants;

/**
 * This file is used to maintain the back up file at every plugin level.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see org.springframework.core.io.ClassPathResource
 */
public class BackUpFileService {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BackUpFileService.class);

	/**
	 * Initializing BACKUP_PROPERTY_FILE.
	 */
	private static final String BACKUP_PROPERTY_FILE = "META-INF" + File.separator + "dcma-util" + File.separator
			+ "dcma-backup-service.properties";

	/**
	 * Initializing APPLICATION_PROPERTY_FILE.
	 */
	private static final String APPLICATION_PROPERTY_FILE = "META-INF" + File.separator + "application.properties";

	/**
	 * Initializing PRE_BATCH_XML.
	 */
	private static final String PRE_BATCH_XML = "PRE_STATE_";

	/**
	 * Initializing INPUT_BATCH_XML.
	 */
	private static final String INPUT_BATCH_XML = "backup.input_batch_xml";

	/**
	 * Initializing INPUT_BATCH_XML_ZIP.
	 */
	private static final String INPUT_BATCH_XML_ZIP = "backup.input_batch_xml_zip";

	/**
	 * Initializing REPORT_BASE_FOL_LOC.
	 */
	private static final String REPORT_BASE_FOL_LOC = "backup.report_folder";

	/**
	 * Initializing ENABLE_REPORTING.
	 */
	private static final String ENABLE_REPORTING = "enable.reporting";

	/**
	 * Initializing OUTPUT_BATCH_XML.
	 */
	private static final String OUTPUT_BATCH_XML = "backup.output_batch_xml";

	/**
	 * Initializing OUTPUT_BATCH_XML_ZIP.
	 */
	private static final String OUTPUT_BATCH_XML_ZIP = "backup.output_batch_xml_zip";

	/**
	 * Initializing ZIP_SWITCH.
	 */
	private static final String ZIP_SWITCH = "zip_switch";

	/**
	 * Initializing backUpFileConfig.
	 */
	private static Properties backUpFileConfig = new Properties();

	/**
	 * Initializing applicationConfig.
	 */
	private static Properties applicationConfig = new Properties();

	static {
		fetchConfig();
		fetchApplicationConfig();
	}

	/**
	 * Open application.properties containing backup configuration parameters, and populate a corresponding Properties object.
	 */
	private static void fetchApplicationConfig() {

		ClassPathResource classPathResource = new ClassPathResource(APPLICATION_PROPERTY_FILE);

		InputStream input = null;
		try {
			input = classPathResource.getInputStream();
			applicationConfig.load(input);
		} catch (IOException ex) {
			LOGGER.error("Cannot open and load application properties file.", ex);
			// System.exit(1);
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException ex) {
				LOGGER.error("Cannot close application properties file.", ex);
			}
		}
	}

	/**
	 * Open backUpService.properties containing backup configuration parameters, and populate a corresponding Properties object.
	 */
	private static void fetchConfig() {

		ClassPathResource classPathResource = new ClassPathResource(BACKUP_PROPERTY_FILE);

		InputStream input = null;
		try {
			input = classPathResource.getInputStream();
			backUpFileConfig.load(input);
		} catch (IOException ex) {
			LOGGER.error("Cannot open and load backUpService properties file.", ex);
	
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException ex) {
				LOGGER.error("Cannot close backUpService properties file.", ex);
			}
		}
	}

	/**
	 * This method will called before service method call to generate the back up file.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @param localFolder {@link String}
	 */
	public static void backUpBatch(final String batchInstanceIdentifier, final String localFolder) {
		LOGGER.info("Entering backUpBatch method.");
		boolean preserveFileDate = false;

		boolean isZipSwitchOn = Boolean.parseBoolean(applicationConfig.getProperty(ZIP_SWITCH));
		LOGGER.info("Zipped Batch XML switch is:" + isZipSwitchOn);

		String baseFolLoc = localFolder;

		String inputBatchXml = backUpFileConfig.getProperty(INPUT_BATCH_XML);

		String baseFolPath = baseFolLoc + File.separator + batchInstanceIdentifier + File.separator;

		String inputFileName = batchInstanceIdentifier + inputBatchXml;

		String outputFileName = PRE_BATCH_XML + inputFileName;

		LOGGER.info("batchInstanceIdentifierIdentifier : " + batchInstanceIdentifier + "  inputFileName : " + inputFileName
				+ "  outputFileName : " + outputFileName);

		String srcFilePath = baseFolPath + inputFileName;
		String destFilePath = baseFolPath + outputFileName;

		boolean isZip = false;
		if (isZipSwitchOn) {

			if (com.ephesoft.dcma.util.FileUtils.isZipFileExists(srcFilePath)) {
				LOGGER.info("zip file exists for the xml file whose File path is : " + srcFilePath);
				isZip = true;
			}
		} else {
			if (new File(srcFilePath).exists()) {
				LOGGER.info("xml file exists. File path is: " + srcFilePath);
				isZip = false;
			} else if (com.ephesoft.dcma.util.FileUtils.isZipFileExists(srcFilePath)) {
				LOGGER.info("zip file exists for the xml file whose File path is : " + srcFilePath);
				isZip = true;

			}
		}

		LOGGER.info("isZip in back service is : " + isZip);
		try {

			if (isZip) {
				inputBatchXml = backUpFileConfig.getProperty(INPUT_BATCH_XML_ZIP);
				inputFileName = batchInstanceIdentifier + inputBatchXml;
				outputFileName = PRE_BATCH_XML + inputFileName;
				srcFilePath = baseFolPath + inputFileName;
				destFilePath = baseFolPath + outputFileName;
			}
			// The source file name to be copied.
			File srcFile = new File(srcFilePath);

			// The target file name to which the source file will be copied.
			File destFile = new File(destFilePath);

			FileUtils.copyFile(srcFile, destFile, preserveFileDate);
			LOGGER.info("Successfully copy of the file for batch Instance identifier : " + batchInstanceIdentifier);
		} catch (IOException e) {
			LOGGER.trace("Unable to copy the file for batch Instance identifier : " + batchInstanceIdentifier);
			LOGGER.trace(e.getMessage());
		}
		LOGGER.info("Exiting backUpBatch method.");
	}

	/**
	 * This method will called with resume service method to generate the back up file.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @param localFolder {@link String}
	 */
	public static void restoreBatch(final String batchInstanceIdentifier, final String localFolder) {
		LOGGER.info("Entering restore batch method.");
		boolean preserveFileDate = false;

		boolean isZipSwitchOn = Boolean.parseBoolean(applicationConfig.getProperty(ZIP_SWITCH));
		LOGGER.info("Zipped Batch XML switch is:" + isZipSwitchOn);

		String baseFolLoc = localFolder;

		String inputBatchXml = backUpFileConfig.getProperty(INPUT_BATCH_XML);

		String baseFolPath = baseFolLoc + File.separator + batchInstanceIdentifier + File.separator;

		String inputFileName = batchInstanceIdentifier + inputBatchXml;

		String outputFileName = PRE_BATCH_XML + inputFileName;

		LOGGER.info("batchInstanceIdentifierIdentifier : " + batchInstanceIdentifier + "  inputFileName : " + inputFileName
				+ "  outputFileName : " + outputFileName);

		String srcFilePath = baseFolPath + outputFileName;
		String destFilePath = baseFolPath + inputFileName;

		boolean isZip = false;

		if (isZipSwitchOn) {
			if (com.ephesoft.dcma.util.FileUtils.isZipFileExists(srcFilePath)) {
				LOGGER.info("zip file exists for the xml file whose File path is : " + srcFilePath);
				isZip = true;
			}
		} else {
			if (new File(srcFilePath).exists()) {
				LOGGER.info("zip file exists. File path is: " + srcFilePath);
				isZip = false;
			} else if (com.ephesoft.dcma.util.FileUtils.isZipFileExists(srcFilePath)) {
				LOGGER.info("zip file exists for the xml file whose File path is: " + srcFilePath);
				isZip = true;

			}
		}
		if (isZip) {
			inputBatchXml = backUpFileConfig.getProperty(INPUT_BATCH_XML_ZIP);
			inputFileName = batchInstanceIdentifier + inputBatchXml;
			outputFileName = PRE_BATCH_XML + inputFileName;
			srcFilePath = baseFolPath + outputFileName;
			destFilePath = baseFolPath + inputFileName;
		}

		// The source file name to be copied.
		File srcFile = new File(srcFilePath);

		// The target file name to which the source file will be copied.
		File destFile = new File(destFilePath);

		try {
			FileUtils.copyFile(srcFile, destFile, preserveFileDate);
			LOGGER.info("Successfully copy of the file for batch Instance identifier : " + batchInstanceIdentifier);
		} catch (IOException e) {
			LOGGER.error("Unable to copy the file for batch Instance identifier : " + batchInstanceIdentifier);
			LOGGER.error(e.getMessage());
		}
		LOGGER.info("Exiting restore batch method.");
	}

	/**
	 * This method will called after service method call to generate the back up file.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @param localFolder {@link String}
	 */
	public static void backUpBatch(final String batchInstanceIdentifier, final String pluginName, final String localFolder) {
		LOGGER.info("Entering backUpBatch method.");
		boolean preserveFileDate = false;

		boolean isZipSwitchOn = Boolean.parseBoolean(applicationConfig.getProperty(ZIP_SWITCH));
		LOGGER.info("Zipped Batch XML switch is:" + isZipSwitchOn);

		String baseFolLoc = localFolder;

		String inputBatchXml = backUpFileConfig.getProperty(INPUT_BATCH_XML);

		String outputBatchXml = backUpFileConfig.getProperty(OUTPUT_BATCH_XML);

		String baseFolPath = baseFolLoc + File.separator + batchInstanceIdentifier + File.separator;

		String reportBaseFolPath = backUpFileConfig.getProperty(REPORT_BASE_FOL_LOC) + File.separator + batchInstanceIdentifier
				+ File.separator;

		String inputFileName = batchInstanceIdentifier + inputBatchXml;

		String outputFileName = batchInstanceIdentifier + UtilConstants.UNDERSCORE + pluginName + outputBatchXml;

		String xmlOutputFileName = outputFileName;

		LOGGER.info("batchInstanceIdentifierIdentifier : " + batchInstanceIdentifier + "  inputFileName : " + inputFileName
				+ "  outputFileName : " + outputFileName);

		String srcFilePath = baseFolPath + inputFileName;
		String destFilePath = baseFolPath + outputFileName;
		boolean isZip = false;

		if (isZipSwitchOn) {
			if (com.ephesoft.dcma.util.FileUtils.isZipFileExists(srcFilePath)) {
				LOGGER.info("zip file exists for the xml file whose File path is: " + srcFilePath);
				isZip = true;
			}
		} else {
			if (new File(srcFilePath).exists()) {
				LOGGER.info("xml file exists. File path is: " + srcFilePath);
				isZip = false;
			} else if (com.ephesoft.dcma.util.FileUtils.isZipFileExists(srcFilePath)) {
				LOGGER.info("zip file exists for the xml file whose File path is: " + srcFilePath);
				isZip = true;
			}
		}
		if (isZip) {
			inputBatchXml = backUpFileConfig.getProperty(INPUT_BATCH_XML_ZIP);
			outputBatchXml = backUpFileConfig.getProperty(OUTPUT_BATCH_XML_ZIP);
			inputFileName = batchInstanceIdentifier + inputBatchXml;
			outputFileName = batchInstanceIdentifier + UtilConstants.UNDERSCORE + pluginName + outputBatchXml;
			srcFilePath = baseFolPath + inputFileName;
			destFilePath = baseFolPath + outputFileName;
		}
		LOGGER.info("Source File in copying the back up is:" + srcFilePath);
		LOGGER.info("Destination File in copying the back up is:" + destFilePath);

		// The source file name to be copied.
		File srcFile = new File(srcFilePath);

		if (null != srcFile && null != pluginName && srcFile.exists() && !srcFile.isDirectory()) {
			// The target file name to which the source file will be copied.
			File destFile = new File(destFilePath);

			try {
				//com.ephesoft.dcma.util.FileUtils.copyFile(srcFile, destFile, preserveFileDate);
				FileUtils.copyFile(srcFile, destFile,preserveFileDate);
				LOGGER.info("Successfully copy of the file for the plugin : " + pluginName + " and batch Instance Id: "
						+ batchInstanceIdentifier);
			} catch (IOException e) {
				LOGGER.error("Unable to copy the file for the plugin : " + pluginName + " and batch Instance Id: "
						+ batchInstanceIdentifier);
				LOGGER.error(e.getMessage(),e);
			}
			if (Boolean.parseBoolean(applicationConfig.getProperty(ENABLE_REPORTING))) {
				destFile = new File(reportBaseFolPath + outputFileName);
				File xmlFile = new File(reportBaseFolPath + xmlOutputFileName);
				File zipXmlFile = new File(reportBaseFolPath + outputFileName);

				if (xmlOutputFileName != null && xmlFile.exists()) {
					FileUtils.deleteQuietly(xmlFile);
				}
				if (zipXmlFile != null && zipXmlFile.exists()) {
					FileUtils.deleteQuietly(xmlFile);
				}
				try {
					//com.ephesoft.dcma.util.FileUtils.copyFile(srcFile, destFile, preserveFileDate);
					FileUtils.copyFile(srcFile, destFile,preserveFileDate);
					LOGGER.info("Report : Successfully copy of the file for the plugin : " + pluginName + " and batch Instance Id : "
							+ batchInstanceIdentifier);
				} catch (IOException e) {
					LOGGER.error("Report : Unable to copy the file for the plugin : " + pluginName + " and batch Instance Id : "
							+ batchInstanceIdentifier);
					LOGGER.error(e.getMessage());
				}
			} else {
				LOGGER.info("Reporting Sevice is disabled.");
			}
		}
		LOGGER.info("Exiting backup batch method.");
	}
}
