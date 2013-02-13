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

package com.ephesoft.dcma.openoffice.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.document.DefaultDocumentFormatRegistry;
import org.artofsolving.jodconverter.office.ExternalOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;

import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.service.FileFormatConvertor;
import com.ephesoft.dcma.util.FileUtils;

/**
 * This is open office convertor class.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public class OpenOfficeConvertor implements SmartLifecycle, FileFormatConvertor {

	/**
	 * serverType int.
	 */
	private int serverType;
	
	/**
	 * autoStartup boolean.
	 */
	private boolean autoStartup;
	
	/**
	 * server String.
	 */
	private String server;
	
	/**
	 * homePath String.
	 */
	private String homePath;
	
	/**
	 * profilePath String.
	 */
	private String profilePath;
	
	/**
	 * serverPort int.
	 */
	private int serverPort;
	
	/**
	 * maxTasksPerProcess int.
	 */
	private int maxTasksPerProcess;
	
	/**
	 * taskExecutionTimeout int.
	 */
	private int taskExecutionTimeout;
	
	/**
	 * sharedLocation String.
	 */
	private String sharedLocation;

	/**
	 * absoluteLocation String.
	 */
	private String absoluteLocation;
	
	/**
	 * _LOCK_FILE String.
	 */
	private static final String _LOCK_FILE = "_lock";

	/**
	 * jodOOManager OfficeManager.
	 */
	private OfficeManager jodOOManager;
	
	/**
	 * jodConverter OfficeDocumentConverter.
	 */
	private OfficeDocumentConverter jodConverter;

	/**
	 * LOGGER to print the logging information.
	 */
	protected static final Logger LOGGER = LoggerFactory.getLogger(OpenOfficeConvertor.class);

	/**
	 * Returns auto startup is true or not.
	 * @return boolean
	 */
	@Override
	public boolean isAutoStartup() {
		return autoStartup;
	}

	/**
	 * To stop.
	 * @param callback {@link Runnable}
	 */
	@Override
	public void stop(Runnable callback) {
		callback.run();
	}

	/**
	 * Returns running is true or not.
	 * @return boolean
	 */
	@Override
	public boolean isRunning() {
		return false;
	}

	/**
	 * init method.
	 */
	public void init() {
		ExternalOfficeManagerConfiguration externalProcessOfficeManager = new ExternalOfficeManagerConfiguration();
		externalProcessOfficeManager.setPortNumber(serverPort);
		externalProcessOfficeManager.setConnectOnStart(true);
		this.jodOOManager = externalProcessOfficeManager.buildOfficeManager(server);
		this.jodConverter = new OfficeDocumentConverter(this.jodOOManager);
	}

	/**
	 * Start method.
	 */
	@Override
	public void start() {
		setSharedLocation(findSharedLocation());
		if (getSharedLocation() == null || server == null) {
			LOGGER.warn("Profile path is not shared or Server URL is not provided. Cannot connect to Open Office Service.");
			return;
		}
		try {
			this.jodOOManager.start();
		} catch (Exception e) {
			if (serverType == 0) {
				LOGGER.error("Problem in starting the open office. Reason may be open office is already running.", e);
			} else {
				LOGGER.error("Problem in connecting to external open office. Reason may be open office is not running on configured url/port.",	e);
			}
		}
	}

	/**
	 * Stop method.
	 */
	@Override
	public void stop() {
		this.jodOOManager.stop();
	}

	/**
	 * To get phase.
	 * @return int
	 */
	@Override
	public int getPhase() {
		return 0;
	}

	/**
	 * To convert input file URI to output file URI.
	 * @param inputFileURI URI
	 * @param outputFileURI URI
	 * @param outputFileType FileType
	 */
	@Override
	public void convert(URI inputFileURI, URI outputFileURI, FileType outputFileType) {
		jodConverter.convert(new File(inputFileURI), new File(outputFileURI), new DefaultDocumentFormatRegistry()
				.getFormatByExtension(outputFileType.getExtension()));
	}

	/**
	 * To convert input file path to output file path.
	 * @param inputFilePath String
	 * @param encodedFileName String
	 * @param outputFileType FileType
	 *  
	 */
	@Override
	public void convert(String inputFilePath, String outputFilePath, String encodedFileName, FileType outputFileType) {
		LOGGER.info("Converting file format using open office ...");
		LOGGER.info("Input File path : " + inputFilePath);
		String outputFile = outputFilePath;
		File outFile = new File(outputFile);
		boolean isCreated = outFile.getParentFile().mkdirs();
		LOGGER.info("Directory created : " + isCreated);
		File tempFile = new File(getSharedLocation() + File.separator + outFile.getParentFile().getName() + File.separator
				+ outFile.getName());
		if (encodedFileName != null && !encodedFileName.isEmpty()) {
			outputFile = new File(getAbsoluteLocation() + File.separator + outFile.getParentFile().getName() + File.separator
					+ encodedFileName).getPath();
		} else {
			outputFile = new File(getAbsoluteLocation() + File.separator + outFile.getParentFile().getName() + File.separator
					+ outFile.getName()).getPath();
		}
		outputFile = FileUtils.changeFileExtension(outputFile, FileType.PDF.getExtension());
		LOGGER.info("Ouput file path : " + outputFile);
		jodConverter.convert(inputFilePath, outputFile, new DefaultDocumentFormatRegistry().getFormatByExtension(outputFileType
				.getExtension()));
		try {
			LOGGER.info("Copying " + tempFile.getAbsolutePath() + " to " + outFile.getAbsolutePath());
			FileUtils.copyFile(tempFile, outFile);
		} catch (Exception e) {
			LOGGER.error("Unable to copy file.", e);
			throw new DCMABusinessException(e.getMessage(), e);
		}
		LOGGER.info("Delting directory : " + tempFile.getParent());
		FileUtils.deleteDirectoryAndContents(tempFile.getParentFile());
	}

	/**
	 * To set server type.
	 * @param serverType int
	 */
	public void setServerType(int serverType) {
		this.serverType = serverType;
	}

	/**
	 * To set auto startup.
	 * @param autoStartup boolean
	 */
	public void setAutoStartup(boolean autoStartup) {
		this.autoStartup = autoStartup;
	}

	/**
	 * To set Home Path.
	 * @param homePath String
	 */
	public void setHomePath(String homePath) {
		this.homePath = homePath;
		LOGGER.debug(this.homePath);
	}

	/**
	 * To set Profile Path.
	 * @param profilePath String
	 */
	public void setProfilePath(String profilePath) {
		this.profilePath = profilePath;
	}

	/**
	 * To set Server Port.
	 * @param serverPort int
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * To set Max Tasks Per Process.
	 * @param maxTasksPerProcess int
	 */
	public void setMaxTasksPerProcess(int maxTasksPerProcess) {
		this.maxTasksPerProcess = maxTasksPerProcess;
		LOGGER.debug(Integer.toString(this.maxTasksPerProcess));
	}

	/**
	 * To set Task Execution Timeout.
	 * @param taskExecutionTimeout int
	 */
	public void setTaskExecutionTimeout(int taskExecutionTimeout) {
		this.taskExecutionTimeout = taskExecutionTimeout;
		LOGGER.debug(Integer.toString(this.taskExecutionTimeout));
	}

	/**
	 * To get server.
	 * @return String
	 */
	public String getServer() {
		return server;
	}

	/**
	 * To set server.
	 * @param server String
	 */
	public void setServer(String server) {
		this.server = server;
	}

	/**
	 * To get Shared Location. 
	 * @return String
	 */
	public String getSharedLocation() {
		return sharedLocation;
	}

	/**
	 * To set Shared Location.
	 * @param sharedLocation String
	 */
	public void setSharedLocation(String sharedLocation) {
		this.sharedLocation = sharedLocation;
	}

	private String findSharedLocation() {
		String sharedLocation = null;
		File sharedProfile = new File(profilePath);

		boolean fileCreated = false;
		do {
			FileWriter fileWriter = null;
			File file = null;
			try {
				file = new File("\\\\" + server + File.separator + sharedProfile.getName() + File.separator + _LOCK_FILE);
				fileWriter = new FileWriter(file);
				fileWriter.write("locked");
				fileCreated = true;
				absoluteLocation = sharedProfile.getPath();
				sharedLocation = file.getParent();
			} catch (Exception e) {
				sharedProfile = sharedProfile.getParentFile();
			} finally {
				if (fileWriter != null) {
					try {
						fileWriter.close();
						file.delete();
					} catch (IOException e) {
						LOGGER.error(e.getMessage(), e);
					}
				}
			}
		} while (!fileCreated && sharedProfile.getParentFile() != null);

		return sharedLocation;

	}

	/**
	 * To get Absolute Location.
	 * @return String
	 */
	public String getAbsoluteLocation() {
		return absoluteLocation;
	}

	/**
	 * To get home path.
	 * @return String
	 */
	public String getHomePath() {
		return homePath;
	}

	/**
	 * To get Max Tasks Per Process.
	 * @return int
	 */
	public int getMaxTasksPerProcess() {
		return maxTasksPerProcess;
	}

	/**
	 * To get Task Execution Timeout.
	 * @return int
	 */
	public int getTaskExecutionTimeout() {
		return taskExecutionTimeout;
	}

}
