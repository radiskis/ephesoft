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

package com.ephesoft.dcma.heartbeat;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.da.domain.ServerRegistry;
import com.ephesoft.dcma.da.service.ServerRegistryService;

/**
 * This class is responsible for watching all the servers registered with the common database pool. If any of the server is down or not
 * active this service will mark that register entry as in active such that other servers can pick the entry corresponding to this shut
 * down server.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.heartbeat.service.HeartBeatServiceImpl
 * 
 */
public final class HeartBeat {

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HeartBeat.class);

	/**
	 * Name of the health file.
	 */
	private static final String HEALTH_FILE_NAME = "HealthStatus.html";
	/**
	 * Reference of ServerRegistryService.
	 */
	@Autowired
	private ServerRegistryService serverRegistryService;

	/**
	 * Number of ping's count.
	 */
	private String numberOfPings;

	/**
	 * Number of ping's count.
	 * 
	 * @return numberOfPings
	 */
	public String getNumberOfPings() {
		return numberOfPings;
	}

	/**
	 * Number of ping's count.
	 * 
	 * @param numberOfPings String
	 */
	public void setNumberOfPings(final String numberOfPings) {
		this.numberOfPings = numberOfPings;
	}

	/**
	 * This method will check the health of all the servers registered to the common data base pool.
	 */
	public void heartBeatHealth() {

		if (null == serverRegistryService) {
			throw new DCMABusinessException("Not able to find ServerRegistryService service of the system.");
		}

		List<ServerRegistry> serverRegistries = serverRegistryService.getAllServerRegistry();

		StringBuilder url = null;
		String pathUrl = null;
		boolean isActive = false;

		if (null == serverRegistries || serverRegistries.isEmpty()) {
			LOGGER.info("No server registry found.");
		} else {
			for (ServerRegistry serverRegistry : serverRegistries) {
				String ipAddress = serverRegistry.getIpAddress();
				String portNumber = serverRegistry.getPort();
				String context = serverRegistry.getAppContext();

				if (ipAddress == null || null == portNumber || null == context) {
					LOGGER.error("Problem in creating. Server Registry Info is Null.");
				} else {
					url = new StringBuilder("http://");
					url.append(ipAddress);
					url.append(":");
					url.append(portNumber);
					if (!context.contains("/")) {
						url.append("/");
					}
					url.append(context);
					url.append("/");
					url.append(HEALTH_FILE_NAME);
					pathUrl = url.toString();
					LOGGER.info(pathUrl);
					isActive = checkHealth(pathUrl);
					if (!isActive) {
						int noOfPings = 1;
						try {
							noOfPings = Integer.parseInt(getNumberOfPings());
						} catch (NumberFormatException nfe) {
							LOGGER.error(nfe.getMessage());
						}
						for (int i = 1; i < noOfPings; i++) {
							isActive = checkHealth(pathUrl);
							if (isActive) {
								break;
							}
						}
					}
					if (serverRegistry.isActive() != isActive) {
						serverRegistry.setActive(isActive);
						serverRegistryService.updateServerRegistry(serverRegistry);
					}
				}
			}
		}

	}

	/**
	 * This method will return true if the server is active other wise false.
	 * 
	 * @param url {@link String}
	 * @return boolean true if the serve is active other wise false.
	 */
	private boolean checkHealth(final String url) {

		boolean isActive = false;

		// Create an instance of HttpClient.
		HttpClient client = new HttpClient();

		// Create a method instance.
		GetMethod method = new GetMethod(url);

		try {
			// Execute the method.
			int statusCode = client.executeMethod(method);

			if (statusCode == HttpStatus.SC_OK) {
				isActive = true;
			} else {
				LOGGER.info("Method failed: " + method.getStatusLine());
			}

		} catch (HttpException e) {
			LOGGER.error("Fatal protocol violation: " + e.getMessage());
		} catch (IOException e) {
			LOGGER.error("Fatal transport error: " + e.getMessage());
		} finally {
			// Release the connection.
			if (method != null) {
				method.releaseConnection();
			}
		}

		return isActive;
	}

}
