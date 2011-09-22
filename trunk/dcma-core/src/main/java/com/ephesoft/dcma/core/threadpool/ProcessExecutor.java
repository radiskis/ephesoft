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

package com.ephesoft.dcma.core.threadpool;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.core.exception.DCMAApplicationException;

public class ProcessExecutor extends AbstractRunnable {

	/**
	 * List of commands to be executed.
	 */
	private String[] cmds;

	/**
	 * The directory in which it needs to be executed.
	 */
	private File environment;

	/**
	 * Constructor
	 * 
	 * @param cmds the command that is to be executed.
	 */
	public ProcessExecutor(String[] cmds) {
		this.cmds = cmds;
	}

	/**
	 * Logger instance for logging using slf4j for logging information.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ProcessExecutor.class);

	/**
	 * Constructor
	 * 
	 * @param cmds the command that is to be executed.
	 * @param environment the environment in which it needs to be executed.
	 */
	public ProcessExecutor(String[] cmds, File environment) {
		this.cmds = cmds;
		this.environment = environment;
	}

	/**
	 * The processing to be done when this thread runs. That is, execution of provided command in the provided environment.
	 */
	@Override
	public void run() {
		Process process = null;
		BufferedReader input  = null;
		InputStreamReader inputStreamReader = null;
		StringBuffer commandStr = new StringBuffer();
		try {
			logger.info("Starting execution of ");
			for (int ind=0;ind<cmds.length;ind++){
				if(cmds[ind] == null){
					cmds[ind] = "";
				}
				logger.info(cmds[ind]);
				commandStr.append(cmds[ind] + " ");
			}
			logger.info("in environment " + environment);
			process = Runtime.getRuntime().exec(cmds, null, environment);
			//process = Runtime.getRuntime().exec(commandStr.toString(), null, environment);
			inputStreamReader = new InputStreamReader(process.getInputStream());
			input = new BufferedReader(inputStreamReader);
			String line = null;
			do {
				line = input.readLine();
				logger.debug(line);
			} while (line != null);
			int exitValue = process.waitFor();
			logger.info("Command"); 
			for (int ind=0;ind<cmds.length;ind++){
				logger.info(cmds[ind]);
			}
			logger.info("exited with error code no " + exitValue);
			if (exitValue != 0) {
				logger.error("Non-zero("+ exitValue +") exit value for command found. So exiting the application");
				setDcmaApplicationException(new DCMAApplicationException(
						"Non-zero exit value for command found. So exiting the application"));
			}
		} catch (IOException e) {
			logger.error("Problem in completing processing", e);
			setDcmaApplicationException(new DCMAApplicationException("Problem in completing processing", e));
		} catch (Exception e) {
			logger.error("Processing could not be completed", e);
			setDcmaApplicationException(new DCMAApplicationException("Processing could not be completed", e));
		}
		finally {
			try {
				if (input != null) {
					input.close();
				}
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
			} catch (IOException e) {
				logger.error("Problem in closing buffer reader. " + e.getMessage(), e);
			}
		}
	}
}
