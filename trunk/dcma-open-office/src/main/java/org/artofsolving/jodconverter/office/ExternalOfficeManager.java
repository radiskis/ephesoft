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

package org.artofsolving.jodconverter.office;

import java.net.ConnectException;

/**
 * Implementation that connects to an external Office process.
 * 
 * @author Ephesoft
 * @version 1.0
 * 
 */
@SuppressWarnings("PMD")
public class ExternalOfficeManager implements OfficeManager {

	/**
	 * THREAD_SLEEP long.
	 */
	private static final long THREAD_SLEEP = 5000L;

	/**
	 * connection OfficeConnection.
	 */
	private final OfficeConnection connection;
	
	/**
	 * connectOnStart boolean.
	 */
	private final boolean connectOnStart;

	/**
	 * Constructor.
	 * 
	 * @param unoUrl UnoUrl
	 * @param connectOnStart should a connection be attempted on {@link #start()}? Default is <em>true</em>. If <em>false</em>, a
	 *            connection will only be attempted the first time an {@link OfficeTask} is executed.
	 */
	public ExternalOfficeManager(UnoUrl unoUrl, boolean connectOnStart) {
		connection = new OfficeConnection(unoUrl);
		this.connectOnStart = connectOnStart;
	}

	/**
	 * Start method.
	 */
	public void start() throws OfficeException {
		if (connectOnStart) {
			synchronized (connection) {
				connect();
			}
		}
	}

	/**
	 * Stop method.
	 */
	public void stop() {
		synchronized (connection) {
			if (connection.isConnected()) {
				connection.disconnect();
			}
		}
	}

	/**
	 * Execute method.
	 * @param task OfficeTask
	 * @throws OfficeException if error occurs
	 */
	public void execute(OfficeTask task) throws OfficeException {
		synchronized (connection) {
			if (!connection.isConnected()) {
				connect();
			}
			task.execute(connection);
		}
	}

	private void connect() {
		try {
			connection.connect();
		} catch (ConnectException connectException) {
			try {
				Thread.sleep(THREAD_SLEEP);
			} catch (InterruptedException e) {
			}
			try {
				connection.connect();
			} catch (ConnectException connectException1) {
				try {
					Thread.sleep(THREAD_SLEEP);
				} catch (InterruptedException e) {
				}
				try {
					connection.connect();
				} catch (ConnectException connectException2) {
					throw new OfficeException("could not connect to external office process", connectException);
				}
			}
		}
	}

}
