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

package com.ephesoft.dcma.batch.service;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBeanNotInitializedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.da.domain.ServerRegistry;
import com.ephesoft.dcma.da.service.ServerRegistryService;
import com.ephesoft.dcma.util.ApplicationContextUtil;

/**
 * This class is to get the context.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.util.ApplicationContextUtil
 */
@Component
public class EphesoftContext implements Context, ApplicationContextAware {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(EphesoftContext.class);

	/**
	 * Instance of ApplicationContext.
	 */
	private static ApplicationContext applicationContext;

	/**
	 * Instance of ServerRegistryService.
	 */
	@Autowired
	private ServerRegistryService registryService;

	/**
	 * identifier long.
	 */
	private long identifier = BatchConstants.ZERO;
	
	/**
	 * port int.
	 */
	private int port = BatchConstants.ZERO;
	
	/**
	 * context String.
	 */
	private String context = BatchConstants.EMPTY;
	
	/**
	 * hostName String.
	 */
	private final String hostName = getHostAddress();

	/**
	 * To set Application Context.
	 * @param appContext ApplicationContext
	 * @throws BeansException if case error occurs
	 */
	@Override
	public void setApplicationContext(final ApplicationContext appContext) throws BeansException {
		applicationContext = appContext;
	}

	/**
	 * Initial processing to be done.
	 */
	@PostConstruct
	public void init() {
		createRegistry();
	}

	private void createRegistry() {
		if (applicationContext instanceof WebApplicationContext) {
			final String portNumber = ((WebApplicationContext) applicationContext).getServletContext().getInitParameter("port");
			if (portNumber != null) {
				try {
					port = Integer.valueOf(portNumber);
				} catch (final NumberFormatException e) {
					LOGGER.error("No port number is defined in web.xml. Assigning port number 0");
				}
			}
			context = ((WebApplicationContext) applicationContext).getServletContext().getContextPath();
		}

		ServerRegistry registry = registryService.getServerRegistry(this.hostName, String.valueOf(this.port), this.context);
		if (registry == null) {
			registry = new ServerRegistry();
			registry.setIpAddress(this.hostName);
			registry.setPort(String.valueOf(this.port));
			registry.setAppContext(this.context);
			registry.setActive(true);
			registryService.createServerRegistry(registry);
		} else {
			registry.setActive(true);
			registryService.updateServerRegistry(registry);
		}
		identifier = registry.getId();
	}

	/**
	 * To get port.
	 * @return int
	 */
	public int getPort() {
		return port;
	}

	/**
	 * To get host name.
	 * @return String
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * To get Web Context Path.
	 * @return String
	 */
	public String getWebContextPath() {
		return context;
	}

	/**
	 * To get context.
	 * @return EphesoftContext
	 */
	@Override
	public EphesoftContext getContext() {
		return this;
	}

	/**
	 * To get id.
	 * @return long
	 */
	public long getId() {
		return identifier;
	}

	/**
	 * To get Heart beat Url.
	 * @return String
	 */
	public String getHeartbeatUrl() {
		final StringBuilder url = new StringBuilder("http://");
		String returnValue;
		try {
			url.append(this.getHostName());
			url.append(ICommonConstants.COLON);
			url.append(this.getPort());
			if (!context.contains(ICommonConstants.FORWARD_SLASH)) {
				url.append(ICommonConstants.FORWARD_SLASH);
			}
			url.append(this.getWebContextPath());
			url.append(ICommonConstants.FORWARD_SLASH);
			url.append("HealthStatus.html");
			returnValue = url.toString();
		} catch (final Exception e) {
			returnValue = "http://www.ephesoft.com/";
		}
		return returnValue;
	}

	private static String getHostAddress() {
		String returnValue;
		try {
			returnValue = InetAddress.getLocalHost().getHostAddress();
		} catch (final UnknownHostException e) {
			returnValue = ICommonConstants.LOCALHOST;
		}
		return returnValue;
	}

	/**
	 * To get single bean of the specified type.
	 * @param type Class<T>
	 * @param <T>
	 * @return <T>
	 */
	public static <T> T get(final Class<T> type) {
		if (applicationContext == null) {
			LOGGER.error("Ephesoft application Context yet not initialized.");
			throw new FactoryBeanNotInitializedException("Ephesoft application Context yet not initialized.");
		}
		return ApplicationContextUtil.getSingleBeanOfType(applicationContext, type);
	}

	/**
	 * To get current context.
	 * @return EphesoftContext
	 */
	public static EphesoftContext getCurrent() {
		return get(Context.class).getContext();
	}

	/**
	 * To get Host Server Registry.
	 * @return ServerRegistry
	 */
	public static ServerRegistry getHostServerRegistry() {
		final EphesoftContext context = get(Context.class).getContext();
		final ServerRegistryService registryService = get(ServerRegistryService.class);
		return registryService.getServerRegistry(context.getId());
	}

	/**
	 * To get Server Registry.
	 * @param ipAddress String
	 * @param portNumber String
	 * @param applicationContext String
	 * @return ServerRegistry
	 */
	public static ServerRegistry getServerRegistry(String ipAddress, String portNumber, String applicationContext) {
		ServerRegistryService registryService = get(ServerRegistryService.class);
		return registryService.getServerRegistry(ipAddress, portNumber, applicationContext);
	}
}
