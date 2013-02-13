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

import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.mail.imap.IMAPSSLStore;
import com.sun.mail.pop3.POP3SSLStore;

/**
 * This is util class for email.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see javax.mail.AuthenticationFailedException
 */
public class EmailUtil {


	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailUtil.class);

	/**
	 * API to test the email configuration.
	 * 
	 * @param emailConfigData {@link EmailConfigurationData}
	 * @return true or false based on the connection made successfully with the email setting passed
	 * @throws MessagingException {@link MessagingException}
	 * @throws AuthenticationFailedException {@link AuthenticationFailedException}
	 */
	public static boolean testEmailConfiguration(final EmailConfigurationData emailConfigData) throws MessagingException,
			AuthenticationFailedException {
		LOGGER.info("Enter method testEmailConfiguration.");
		boolean isBValidConfig = true;
		Store store = null;
		if (emailConfigData != null) {
			String serverType = emailConfigData.getServerType();
			LOGGER.debug("Server type :: " + serverType);
			if (emailConfigData.getIsSSL()) {
				if (IUtilCommonConstants.POP3_CONFIG.equalsIgnoreCase(serverType)) {
					store = connectWithPOP3SSL(emailConfigData);
				} else if (IUtilCommonConstants.IMAP_CONFIG.equalsIgnoreCase(serverType)) {
					store = connectWithIMAPSSL(emailConfigData);
				} else {
					LOGGER.error("Error in Server Type Configuration, only imap/pop3 is allowed.");
					throw new MessagingException("Error in Server Type Configuration, only imap/pop3 is allowed.");
				}
			} else {
				store = connectWithNonSSL(emailConfigData);
			}
		}
		if (store == null || !store.isConnected()) {
			LOGGER.error("Not able to establish connection. Please check settings.");
			isBValidConfig = false;
		}
		LOGGER.info("Exiting method testEmailConfiguration.");
		return isBValidConfig;
	}

	/**
	 * API to connect to the email configuration with POP3 server type.
	 * 
	 * @param emailConfigData {@link EmailConfigurationData}
	 * @return
	 * @throws MessagingException {@link MessagingException}
	 * @throws AuthenticationFailedException {@link AuthenticationFailedException}
	 */
	public static Store connectWithPOP3SSL(final EmailConfigurationData emailConfigData) throws MessagingException,
			AuthenticationFailedException {
		LOGGER.info("Entering method connectWithPOP3SSL...");
		Properties pop3Props = new Properties();
		pop3Props.setProperty("mail.pop3.socketFactory.class", IUtilCommonConstants.SSL_FACTORY);
		pop3Props.setProperty("mail.pop3.socketFactory.fallback", "false");
		Integer portNumber = emailConfigData.getPortNumber();
		LOGGER.debug("Port Number :: " + portNumber);
		if (portNumber == null) {
			LOGGER.error("Could not find port number. Trying with default value of 995");
			throw new MessagingException("Could not connect to port number " + portNumber
					+ ". Try connecting with defualt port number " + IUtilCommonConstants.DEFAULT_PORT_NUMBER_POP3 + " for pop3 and "
					+ IUtilCommonConstants.DEFAULT_PORT_NUMBER_IMAP + " for IMAP ");
		}
		URLName url = new URLName(emailConfigData.getServerType(), emailConfigData.getServerName(), portNumber, "", emailConfigData
				.getUserName(), emailConfigData.getPassword());

		Session session = Session.getInstance(pop3Props, null);
		Store store = new POP3SSLStore(session, url);
		store.connect();
		LOGGER.info("Exiting method connectWithPOP3SSL...");
		return store;
	}

	/**
	 * API to connect to the email configuration with IMAP server type.
	 * 
	 * @param emailConfigData {@link EmailConfigurationData}
	 * @return
	 * @throws MessagingException {@link MessagingException}
	 * @throws AuthenticationFailedException {@link AuthenticationFailedException}
	 */
	public static Store connectWithIMAPSSL(final EmailConfigurationData emailConfigData) throws MessagingException,
			AuthenticationFailedException {
		LOGGER.info("Entering method connectWithIMAPSSL...");
		Properties imapProps = new Properties();
		imapProps.setProperty("mail.imap.socketFactory.class", IUtilCommonConstants.SSL_FACTORY);
		imapProps.setProperty("mail.imap.socketFactory.fallback", "false");
		imapProps.setProperty("mail.imap.partialfetch", "false");
		Integer portNumber = emailConfigData.getPortNumber();
		Session session = null;
		Store store = null;
		LOGGER.debug("Port Number :: " + portNumber);
		if (portNumber == null) {
			LOGGER.error("Could not find port number. Trying with default value of 993");
			portNumber = IUtilCommonConstants.DEFAULT_PORT_NUMBER_IMAP;
		}
		URLName url = new URLName(emailConfigData.getServerType(), emailConfigData.getServerName(), portNumber, "", emailConfigData
				.getUserName(), emailConfigData.getPassword());

		session = Session.getInstance(imapProps, null);
		store = new IMAPSSLStore(session, url);
		store.connect();
		LOGGER.info("Exiting method connectWithIMAPSSL...");
		return store;

	}

	/**
	 * API to connect to a non ssl email config. Returns the {@link Store} object which provides many common methods for naming stores,
	 * connecting to stores, and listening to connection events.
	 * 
	 * 
	 * @param emailConfigData {@link EmailConfigurationData}
	 * @return
	 * @throws MessagingException {@link MessagingException}
	 * @throws AuthenticationFailedException {@link AuthenticationFailedException}
	 */
	public static Store connectWithNonSSL(final EmailConfigurationData emailConfigData) throws MessagingException,
			AuthenticationFailedException {
		LOGGER.info("Entering method connectWithNonSSL...");
		Session session = Session.getDefaultInstance(System.getProperties(), null);
		Store store = session.getStore(emailConfigData.getServerType());
		store.connect(emailConfigData.getServerName(), emailConfigData.getUserName(), emailConfigData.getPassword());
		LOGGER.info("Exiting method connectWithNonSSL...");
		return store;
	}

}
