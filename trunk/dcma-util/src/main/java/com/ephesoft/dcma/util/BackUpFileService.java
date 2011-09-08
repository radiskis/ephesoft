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

package com.ephesoft.dcma.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

/**
 * This file is used to maintain the back up file at every plugin level.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public class BackUpFileService {

	/**
	 * 
	 */
	private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(BackUpFileService.class);

	private static final String BACKUP_PROPERTY_FILE = "META-INF" + File.separator + "dcma-util" + File.separator
			+ "dcma-backup-service.properties";
	
	private static final String APPLICATION_PROPERTY_FILE = "META-INF" + File.separator + "application.properties";

	private static final String PRE_BATCH_XML = "PRE_STATE_";

	private static final String BASE_FOL_LOC = "backup.local_folder";

	private static final String INPUT_BATCH_XML = "backup.input_batch_xml";
	
	private static final String REPORT_BASE_FOL_LOC = "backup.report_folder";
	
	private static final String ENABLE_REPORTING = "enable.reporting";

	private static final String OUTPUT_BATCH_XML = "backup.output_batch_xml";

	private static Properties _backUpFileConfig = new Properties();
	
	private static Properties _applicationConfig = new Properties();

	static {
		fetchConfig();
		fetchApplicationConfig();
	}
	
	/**
	 * Open application.properties containing backup configuration parameters, and populate a corresponding Properties object.
	 **/
	private static void fetchApplicationConfig() {

		ClassPathResource classPathResource = new ClassPathResource(APPLICATION_PROPERTY_FILE);

		InputStream input = null;
		try {
			input = classPathResource.getInputStream();
			_applicationConfig.load(input);
		} catch (IOException ex) {
			log.error("Cannot open and load application properties file.", ex);
			// System.exit(1);
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (IOException ex) {
				log.error("Cannot close application properties file.", ex);
			}
		}
	}


	/**
	 * Open backUpService.properties containing backup configuration parameters, and populate a corresponding Properties object.
	 **/
	private static void fetchConfig() {

		ClassPathResource classPathResource = new ClassPathResource(BACKUP_PROPERTY_FILE);

		InputStream input = null;
		try {
			input = classPathResource.getInputStream();
			_backUpFileConfig.load(input);
		} catch (IOException ex) {
			log.error("Cannot open and load backUpService properties file.", ex);
			// System.exit(1);
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (IOException ex) {
				log.error("Cannot close backUpService properties file.", ex);
			}
		}
	}

	/**
	 * This method will called before service method call to generate the back up file.
	 * 
	 * @param batchInstanceIdentifier
	 */
	public static void backUpBatch(final String batchInstanceIdentifier) {
		boolean preserveFileDate = false;

		String baseFolLoc = com.ephesoft.dcma.util.FileUtils.getAbsoluteFilePath(_backUpFileConfig.getProperty(BASE_FOL_LOC));

		String inputBatchXml = _backUpFileConfig.getProperty(INPUT_BATCH_XML);

		String baseFolPath = baseFolLoc + File.separator + batchInstanceIdentifier + File.separator;

		String inputFileName = batchInstanceIdentifier + inputBatchXml;

		String outputFileName = PRE_BATCH_XML + inputFileName;

		log.info("batchInstanceIdentifierIdentifier : " + batchInstanceIdentifier + "  inputFileName : " + inputFileName
				+ "  outputFileName : " + outputFileName);

		// The source file name to be copied.
		File srcFile = new File(baseFolPath + inputFileName);

		// The target file name to which the source file will be copied.
		File destFile = new File(baseFolPath + outputFileName);

		try {
			FileUtils.copyFile(srcFile, destFile, preserveFileDate);
			log.info("Successfully copy of the file for batch Instance identifier : " + batchInstanceIdentifier);
		} catch (IOException e) {
			log.trace("Unable to copy the file for batch Instance identifier : " + batchInstanceIdentifier);
			log.trace(e.getMessage());
		}
	}

	/**
	 * This method will called with resume service method to generate the back up file.
	 * 
	 * @param batchInstanceIdentifier
	 */
	public static void restoreBatch(final String batchInstanceIdentifier) {
		boolean preserveFileDate = false;

		String baseFolLoc = _backUpFileConfig.getProperty(BASE_FOL_LOC);

		String inputBatchXml = _backUpFileConfig.getProperty(INPUT_BATCH_XML);

		String baseFolPath = baseFolLoc + File.separator + batchInstanceIdentifier + File.separator;

		String inputFileName = batchInstanceIdentifier + inputBatchXml;

		String outputFileName = PRE_BATCH_XML + inputFileName;

		log.info("batchInstanceIdentifierIdentifier : " + batchInstanceIdentifier + "  inputFileName : " + inputFileName
				+ "  outputFileName : " + outputFileName);

		// The source file name to be copied.
		File srcFile = new File(baseFolPath + outputFileName);

		// The target file name to which the source file will be copied.
		File destFile = new File(baseFolPath + inputFileName);

		try {
			FileUtils.copyFile(srcFile, destFile, preserveFileDate);
			log.info("Successfully copy of the file for batch Instance identifier : " + batchInstanceIdentifier);
		} catch (IOException e) {
			log.error("Unable to copy the file for batch Instance identifier : " + batchInstanceIdentifier);
			log.error(e.getMessage());
		}
	}

	/**
	 * This method will called after service method call to generate the back up file.
	 * 
	 * @param batchInstanceIdentifier
	 * @param pluginName
	 */
	public static void backUpBatch(final String batchInstanceIdentifier, final String pluginName) {
		boolean preserveFileDate = false;

		String baseFolLoc = _backUpFileConfig.getProperty(BASE_FOL_LOC);

		String inputBatchXml = _backUpFileConfig.getProperty(INPUT_BATCH_XML);

		String outputBatchXml = _backUpFileConfig.getProperty(OUTPUT_BATCH_XML);

		String baseFolPath = baseFolLoc + File.separator + batchInstanceIdentifier + File.separator;
		
		String reportBaseFolPath = _backUpFileConfig.getProperty(REPORT_BASE_FOL_LOC) + File.separator + batchInstanceIdentifier + File.separator;
		
		String inputFileName = batchInstanceIdentifier + inputBatchXml;

		String outputFileName = batchInstanceIdentifier + "_" + pluginName + outputBatchXml;

		log.info("batchInstanceIdentifierIdentifier : " + batchInstanceIdentifier + "  inputFileName : " + inputFileName
				+ "  outputFileName : " + outputFileName);

		// The source file name to be copied.
		File srcFile = new File(baseFolPath + inputFileName);
		if (null != srcFile && null != pluginName && srcFile.exists() && !srcFile.isDirectory()) {
			// The target file name to which the source file will be copied.
			File destFile = new File(baseFolPath + outputFileName);

			try {
				FileUtils.copyFile(srcFile, destFile, preserveFileDate);
				log.info("Successfully copy of the file for the plugin : " + pluginName + " and batch Instance Id : "
						+ batchInstanceIdentifier);
			} catch (IOException e) {
				log.error("Unable to copy the file for the plugin : " + pluginName + " and batch Instance Id : "
						+ batchInstanceIdentifier);
				log.error(e.getMessage());
			}
			if(Boolean.parseBoolean(_applicationConfig.getProperty(ENABLE_REPORTING))){
				destFile = new File(reportBaseFolPath + outputFileName);
				try
				{
					FileUtils.copyFile(srcFile, destFile, preserveFileDate);
					log.info("Report : Successfully copy of the file for the plugin : " + pluginName + " and batch Instance Id : " + batchInstanceIdentifier);
				}
				catch(IOException e)
				{
					log.error("Report : Unable to copy the file for the plugin : " + pluginName + " and batch Instance Id : " + batchInstanceIdentifier);
					log.error(e.getMessage());
				}
			} else {
				log.info("Reporting Sevice is disabled.");
			}
		}
	}
}
