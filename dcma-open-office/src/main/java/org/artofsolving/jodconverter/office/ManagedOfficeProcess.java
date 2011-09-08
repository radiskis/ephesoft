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

//
// JODConverter - Java OpenDocument Converter
// Copyright 2009 Art of Solving Ltd
// Copyright 2004-2009 Mirko Nasato
//
// JODConverter is free software: you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public License
// as published by the Free Software Foundation, either version 3 of
// the License, or (at your option) any later version.
//
// JODConverter is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General
// Public License along with JODConverter.  If not, see
// <http://www.gnu.org/licenses/>.
//
package org.artofsolving.jodconverter.office;

import java.net.ConnectException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import com.sun.star.frame.XDesktop;
import com.sun.star.lang.DisposedException;

class ManagedOfficeProcess {

	private final ManagedOfficeProcessSettings settings;

	private final OfficeProcess process;
	private final InternalOfficeConnection connection;

	private ExecutorService executor = Executors.newSingleThreadExecutor(new NamedThreadFactory("OfficeProcessThread"));

	private final Logger logger = Logger.getLogger(getClass().getName());

	public ManagedOfficeProcess(ManagedOfficeProcessSettings settings) throws OfficeException {
		this.settings = settings;
		process = new OfficeProcess(settings.getOfficeHome(), settings.getUnoUrl(), settings.getTemplateProfileDir(), settings
				.getProcessManager());
		connection = new InternalOfficeConnection(settings.getUnoUrl());
	}

	public OfficeConnection getConnection() {
		return connection;
	}

	public void startAndWait() throws OfficeException {
		Future<?> future = executor.submit(new Runnable() {

			public void run() {
				doStartProcessAndConnect();
			}
		});
		try {
			future.get();
		} catch (Exception exception) {
			throw new OfficeException("failed to start and connect", exception);
		}
	}

	public void stopAndWait() throws OfficeException {
		Future<?> future = executor.submit(new Runnable() {

			public void run() {
				doStopProcess();
			}
		});
		try {
			future.get();
		} catch (Exception exception) {
			throw new OfficeException("failed to start and connect", exception);
		}
	}

	public void restartAndWait() {
		Future<?> future = executor.submit(new Runnable() {

			public void run() {
				doStopProcess();
				doStartProcessAndConnect();
			}
		});
		try {
			future.get();
		} catch (Exception exception) {
			throw new OfficeException("failed to restart", exception);
		}
	}

	public void restartDueToTaskTimeout() {
		executor.execute(new Runnable() {

			public void run() {
				doTerminateProcess();
				// will cause unexpected disconnection and subsequent restart
			}
		});
	}

	public void restartDueToLostConnection() {
		executor.execute(new Runnable() {

			public void run() {
				doEnsureProcessExited();
				doStartProcessAndConnect();
			}
		});
	}

	private void doStartProcessAndConnect() throws OfficeException {
		try {
			process.start();
			new Retryable() {

				protected void attempt() throws TemporaryException, Exception {
					try {
						connection.connect();
					} catch (ConnectException connectException) {
						throw new TemporaryException(connectException);
					}
				}
			}.execute(settings.getRetryInterval(), settings.getRetryTimeout());
		} catch (Exception exception) {
			throw new OfficeException("could not establish connection", exception);
		}
	}

	private void doStopProcess() {
		try {
			XDesktop desktop = OfficeUtils.cast(XDesktop.class, connection.getService(OfficeUtils.SERVICE_DESKTOP));
			desktop.terminate();
		} catch (DisposedException disposedException) {
			// expected
		} catch (Exception exception) {
			// in case we can't get hold of the desktop
			doTerminateProcess();
		}
		doEnsureProcessExited();
	}

	private void doEnsureProcessExited() throws OfficeException {
		try {
			int exitCode = process.getExitCode(settings.getRetryInterval(), settings.getRetryTimeout());
			logger.info("process exited with code " + exitCode);
		} catch (RetryTimeoutException retryTimeoutException) {
			doTerminateProcess();
		}
		process.deleteProfileDir();
	}

	private void doTerminateProcess() {
		try {
			int exitCode = process.forciblyTerminate(settings.getRetryInterval(), settings.getRetryTimeout());
			logger.info("process forcibly terminated with code " + exitCode);
		} catch (Exception exception) {
			throw new OfficeException("could not terminate process", exception);
		}
	}

}
