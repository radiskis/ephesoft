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

package com.ephesoft.dcma.core.threadpool;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.core.constant.CoreConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;

import com.ephesoft.dcma.util.ApplicationConfigProperties;

/**
 * Class to execute process.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see java.io.InputStreamReader
 */
public class ProcessExecutor extends AbstractRunnable {

	/**
	 * Logger instance for logging using slf4j for logging information.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ProcessExecutor.class);

	/**
	 * Default number of retries by executor in case of failure(non-zero exit value)
	 */
	private static final int DEFAULT_NO_OF_RETRIES = 0;

	/**
	 * List of commands to be executed.
	 */
	private String[] cmds;

	/**
	 * The directory in which it needs to be executed.
	 */
	private File environment;

	/**
	 * The number of retries we want in case the executor exits with a non-zero exit value
	 */
	private int numberOfRetries;

	/**
	 * A variable to decide whether we want to retry in case the executor exits with a non-zero exit value
	 */
	private boolean retry;

	/**
	 * Constructor.
	 * 
	 * @param cmds the command that is to be executed.
	 */
	public ProcessExecutor(String[] cmds) {
		this(cmds, null);
	}

	/**
	 * Constructor.
	 * 
	 * @param cmds the command that is to be executed.
	 * @param environment the environment in which it needs to be executed.
	 */
	public ProcessExecutor(String[] cmds, File environment) {
		this(cmds, environment, false);
	}

	/**
	 * Constructor
	 * 
	 * @param cmds the command that is to be executed.
	 * @param environment the environment in which it needs to be executed.
	 * @param retry whether the command needs to be re-executed on non-zero exit command value
	 */
	public ProcessExecutor(String[] cmds, File environment, boolean retry) {
		super();
		if (cmds != null) {
			this.cmds = new String[cmds.length];
			System.arraycopy(cmds, 0, this.cmds, 0, cmds.length);
		}
		this.environment = environment;
		this.retry = retry;
	}

	/**
	 * The processing to be done when this thread runs. That is, execution of provided command in the provided environment.
	 */
	@Override
	public void run() {
		Process process = null;
		BufferedReader input = null;
		InputStreamReader inputStreamReader = null;
		StringBuffer commandStr = new StringBuffer();
		try {
			LOG.info("Starting execution of ");
			for (int ind = 0; ind < cmds.length; ind++) {
				if (cmds[ind] == null) {
					cmds[ind] = CoreConstants.EMPTY;
				}
				LOG.info(cmds[ind]);
				commandStr.append(cmds[ind]);
				commandStr.append(CoreConstants.SPACE);
			}
			LOG.info("in environment " + environment);
			process = Runtime.getRuntime().exec(cmds, null, environment);

			inputStreamReader = new InputStreamReader(process.getInputStream());
			input = new BufferedReader(inputStreamReader);
			String line = null;
			do {
				line = input.readLine();
				LOG.debug(line);
			} while (line != null);
			int exitValue = process.waitFor();
			LOG.info("Command");
			for (int ind = 0; ind < cmds.length; ind++) {
				LOG.info(cmds[ind]);
			}
			LOG.info("exited with error code no " + exitValue);
			if (exitValue != 0) {
				if (retry) {
					checkAndRetryExecution();
				} else {
					setNonZeroExitValueError();
				}
			}
		} catch (IOException e) {
			String errorMsg = "Problem in completing processing";
			LOG.error(errorMsg, e);
			setDcmaApplicationException(new DCMAApplicationException(errorMsg, e));
		} catch (Exception e) {
			String errorMsg = "Processing could not be completed";
			LOG.error(errorMsg, e);
			setDcmaApplicationException(new DCMAApplicationException(errorMsg, e));
		} finally {
			try {
				if (input != null) {
					input.close();
				}
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
			} catch (IOException e) {
				LOG.error("Problem in closing buffer reader. " + e.getMessage(), e);
			}
		}
	}

	/**
	 * A method to check whether we need to retry the execution of the failed command. If we do, we perform the operations that failed
	 * again, else we set an exception for non zero exit value.
	 */
	private void checkAndRetryExecution() {
		int maximumNumberOfRetries = DEFAULT_NO_OF_RETRIES;
		try {
			ApplicationConfigProperties applicationConfigProperties = ApplicationConfigProperties.getApplicationConfigProperties();
			String property = applicationConfigProperties.getProperty(CoreConstants.MAXIMUM_NUMBER_OF_RETRIES);
			maximumNumberOfRetries = Integer.parseInt(property);
		} catch (Exception e) {
			LOG.info("Could not fetch maximum number of retries from the property file. Hence it has the value: "
					+ maximumNumberOfRetries);
			// deliberately ignoring the exception so that the default number of retries can still work for an inappropriate value in
			// the property file
		}
		if (numberOfRetries++ < maximumNumberOfRetries) {
			LOG.info("Retrying executing the command.");
			run();
		} else {
			setNonZeroExitValueError();
		}
	}

	/**
	 * A method to set the non zero exit value being obtained in the command execution
	 */
	private void setNonZeroExitValueError() {
		setDcmaApplicationException(new DCMAApplicationException("Non-zero exit value for command found. So exiting the application"));
	}
}
