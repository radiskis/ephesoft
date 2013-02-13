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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.sun.star.beans.XPropertySet;
import com.sun.star.bridge.XUnoUrlResolver;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.connection.NoConnectException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

/**
 * Connection details are managed here.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.sun.star.beans.XPropertySet
 */
@SuppressWarnings("PMD")
class OfficeConnection implements OfficeContext {

	/**
	 * unoUrl UnoUrl.
	 */
	private final UnoUrl unoUrl;

	/**
	 * serviceManager XMultiComponentFactory.
	 */
	private XMultiComponentFactory serviceManager;
	
	/**
	 * componentContext XComponentContext.
	 */
	private XComponentContext componentContext;

	/**
	 * connectionEventListeners List<OfficeConnectionEventListener>.
	 */
	private final List<OfficeConnectionEventListener> connectionEventListeners = new ArrayList<OfficeConnectionEventListener>();

	/**
	 * connected boolean.
	 */
	private volatile boolean connected = false;

	/**
	 * LOGGER to print the logging information.
	 */
	private final Logger LOGGER = Logger.getLogger(getClass().getName());

	/**
	 * Office Connection.
	 * @param unoUrl UnoUrl
	 */
	public OfficeConnection(UnoUrl unoUrl) {
		this.unoUrl = unoUrl;
	}

	/**
	 * To add Connection Event Listener.
	 * @param connectionEventListener OfficeConnectionEventListener
	 */
	public void addConnectionEventListener(OfficeConnectionEventListener connectionEventListener) {
		connectionEventListeners.add(connectionEventListener);
	}

	/**
	 * To set up connection.
	 * @throws ConnectException if failure occurs
	 */
	public void connect() throws ConnectException {
		LOGGER.fine(String.format("connecting with connectString '%s'", unoUrl));
		try {

			XComponentContext localContext = Bootstrap.createInitialComponentContext(null);
			XMultiComponentFactory localServiceManager = localContext.getServiceManager();

			Object urlResolver = localServiceManager.createInstanceWithContext("com.sun.star.bridge.UnoUrlResolver", localContext);
			XUnoUrlResolver unoUrlResolver = (XUnoUrlResolver) UnoRuntime.queryInterface(XUnoUrlResolver.class, urlResolver);
			Object initialObject = unoUrlResolver.resolve(unoUrl.getConnectString());
			XPropertySet properties = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, initialObject);

			componentContext = OfficeUtils.cast(XComponentContext.class, properties.getPropertyValue("DefaultContext"));
			serviceManager = componentContext.getServiceManager();
			connected = true;
			LOGGER.info(String.format("connected: '%s'", unoUrl));
			OfficeConnectionEvent connectionEvent = new OfficeConnectionEvent(this);
			for (OfficeConnectionEventListener listener : connectionEventListeners) {
				listener.connected(connectionEvent);
			}
		} catch (NoConnectException connectException) {
			throw new ConnectException(String.format("connection failed: '%s'; %s", unoUrl, connectException.getMessage()));
		} catch (Exception exception) {
			throw new OfficeException("connection failed: " + unoUrl, exception);
		}
	}

	/**
	 * To check whether connected or not.
	 * @return boolean
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * To disconnect.
	 */
	public synchronized void disconnect() {
		LOGGER.fine(String.format("disconnecting: '%s'", unoUrl));
		((XComponent) componentContext).dispose();
	}

	/**
	 * To get service
	 * @param serviceName String
	 */
	public Object getService(String serviceName) {
		try {
			return serviceManager.createInstanceWithContext(serviceName, componentContext);
		} catch (Exception exception) {
			throw new OfficeException(String.format("failed to obtain service '%s'", serviceName), exception);
		}
	}

}
