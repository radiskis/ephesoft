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

/**
 * 
 */
package com.ephesoft.dcma.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.mail.imap.IMAPSSLStore;
import com.sun.mail.pop3.POP3SSLStore;

/**
 * This class is a utility file consisting of various APIs related to different functions that can be performed with an Email.
 * 
 * @author Ephesoft
 * @version 1.0
 * 
 */
public class MailUtil {

	private final static Logger LOGGER = LoggerFactory.getLogger(MailUtil.class);

	/**
	 * This API returns an email store for the provided set of email configuration values like server type, server name, username,
	 * password, port, SSL.
	 * 
	 * @param serverType {@link String}
	 * @param serverName {@link String}
	 * @param userName {@link String}
	 * @param password {@link String}
	 * @param portNumber {@link Integer}
	 * @param isSSL {@link Boolean}
	 * @return email store object {@link Store}
	 * @throws MessagingException
	 */
	public static Store getMailStore(final String serverType, final String serverName, final String userName, final String password,
			final Integer portNumber, final Boolean isSSL) throws MessagingException {
		Store store = null;
		final StringBuilder logMessage = new StringBuilder();
		logMessage.append("Creating an email store object with configuration:\nServer type: ");
		logMessage.append(serverType);
		logMessage.append("\n Server Name: ");
		logMessage.append(serverName);
		logMessage.append("\nUser Name: ");
		logMessage.append(userName);
		logMessage.append("\nPort Number: ");
		logMessage.append(portNumber);
		LOGGER.info(logMessage.toString());
		if (isSSL != null && isSSL) {
			if (serverType.equalsIgnoreCase(IUtilCommonConstants.SERVER_TYPE_POP3)) {
				store = connectWithPOP3SSL(serverType, serverName, userName, password, portNumber);
			} else if (serverType.equalsIgnoreCase(IUtilCommonConstants.SERVER_TYPE_IMAP)) {
				store = connectWithIMAPSSL(serverType, serverName, userName, password, portNumber);
			} else {
				LOGGER.error("Error in Server Type Configuration, only imap/pop3 is allowed. Configuration used : ");
				throw new MessagingException("Error in Server Type Configuration, only imap/pop3 is allowed. Configuration used : ");
			}

		} else {
			store = connectWithNonSSL(serverType, serverName, userName, password, portNumber);
		}
		return store;
	}

	/**
	 * This API connects to a defined POP3 server for the given email configuration settings which include server name, username,
	 * password, port.
	 * 
	 * @param serverType {@link String}
	 * @param serverName {@link String}
	 * @param userName {@link String}
	 * @param password {@link String}
	 * @param portNumber {@link Integer}
	 * @return email store object {@link Store}
	 * @throws MessagingException
	 */
	private static Store connectWithPOP3SSL(final String serverType, final String serverName, final String userName,
			final String password, final Integer portNumber) throws MessagingException {
		Integer finalPortNumber = portNumber;
		final Properties pop3Props = new Properties();
		Store store = null;

		Session session = null;

		pop3Props.setProperty(IUtilCommonConstants.POP3_SOCKET_FACTORY_CLASS_PARAM, IUtilCommonConstants.SSL_FACTORY);
		pop3Props.setProperty(IUtilCommonConstants.POP3_SOCKET_FACTORY_FALLBACK_PARAM, IUtilCommonConstants.FALSE_PARAM_VALUE);

		if (finalPortNumber == null) {
			LOGGER.error("Could not find port number. Trying with default value of 995");
			finalPortNumber = IUtilCommonConstants.POP3_DEFAULT_PORT_NUMBER;
		}

		final URLName url = new URLName(serverType, serverName, finalPortNumber, "", userName, password);

		session = Session.getInstance(pop3Props, null);
		store = new POP3SSLStore(session, url);
		store.connect();
		return store;

	}

	/**
	 * This API connects to a defined IMAP server for the given email configuration settings which include server name, username,
	 * password, port.
	 * 
	 * @param serverType {@link String}
	 * @param serverName {@link String}
	 * @param userName {@link String}
	 * @param password {@link String}
	 * @param portNumber {@link Integer}
	 * @return email store object {@link Store}
	 * @throws MessagingException
	 */
	private static Store connectWithIMAPSSL(final String serverType, final String serverName, final String userName,
			final String password, final Integer portNumber) throws MessagingException {
		Integer finalPortNumber = portNumber;
		final Properties imapProps = new Properties();
		Store store = null;

		Session session = null;

		imapProps.setProperty(IUtilCommonConstants.IMAP_SOCKET_FACTORY_CLASS_PARAM, IUtilCommonConstants.SSL_FACTORY);
		imapProps.setProperty(IUtilCommonConstants.IMAP_SOCKET_FACTORY_FALLBACK_PARAM, IUtilCommonConstants.FALSE_PARAM_VALUE);
		imapProps.setProperty(IUtilCommonConstants.IMAP_PARTIAL_FETCH_PARAM, IUtilCommonConstants.FALSE_PARAM_VALUE);

		if (finalPortNumber == null) {
			LOGGER.error("Could not find port number. Trying with default value of 993");
			finalPortNumber = IUtilCommonConstants.IMAP_DEFAULT_PORT_NUMBER;
		}

		final URLName url = new URLName(serverType, serverName, finalPortNumber, "", userName, password);

		session = Session.getInstance(imapProps, null);
		store = new IMAPSSLStore(session, url);
		store.connect();
		return store;

	}

	/**
	 * This API connects to a defined a NON SSL server for the given email configuration settings which include server name, username,
	 * password and port.
	 * 
	 * @param serverType {@link String}
	 * @param serverName {@link String}
	 * @param userName {@link String}
	 * @param password {@link String}
	 * @param portNumber {@link Integer}
	 * @return email store object {@link Store}
	 * @throws MessagingException
	 */
	private static Store connectWithNonSSL(final String serverType, final String serverName, final String userName,
			final String password, final Integer port) throws MessagingException {
		Store store = null;

		Session session = null;

		session = Session.getInstance(System.getProperties(), null);
		store = session.getStore(serverType.toLowerCase());
		store.connect(serverName, port, userName, password);
		return store;
	}

	public static void downloadAttachment(final Part part, final String folderPath) throws FileNotFoundException, IOException,
			MessagingException {
		if (part != null) {
			final String disPosition = part.getDisposition();
			final String fileName = part.getFileName();
			String decodedText = fileName;
			final StringBuilder fileInfoBuilder = new StringBuilder();
			fileInfoBuilder.append("Disposition type :: ");
			fileInfoBuilder.append(disPosition);
			fileInfoBuilder.append("Attached File Name :: ");
			fileInfoBuilder.append(fileName);
			if (fileName != null) {
				try {
					decodedText = MimeUtility.decodeText(fileName);
				} catch (final UnsupportedEncodingException e) {
					LOGGER.error("Error while decoding text " + decodedText);
				}
				decodedText = Normalizer.normalize(decodedText, Normalizer.Form.NFC);
				final File parentFile = new File(folderPath);
				decodedText = FileUtils.getUpdatedFileNameForDuplicateFile(decodedText, parentFile);
				fileInfoBuilder.append("Decoded file name :: ");
				fileInfoBuilder.append(decodedText);
				fileInfoBuilder.append("Normalized file name :: ");
				fileInfoBuilder.append(decodedText);
				fileInfoBuilder.append("Updated file name : " + decodedText);
				fileInfoBuilder.append(decodedText);
			}
			if (disPosition != null && disPosition.equalsIgnoreCase(Part.ATTACHMENT)) {
				fileInfoBuilder.append("DisPosition is ATTACHMENT type.");
				final File file = new File(folderPath + File.separator + decodedText);
				file.getParentFile().mkdirs();
				saveEmailAttachment(file, part);
			} else if (fileName != null) {
				fileInfoBuilder.append("DisPosition is Null type but file name is valid. Possibly inline attchment");
				final File file = new File(folderPath + File.separator + decodedText);
				file.getParentFile().mkdirs();
				saveEmailAttachment(file, part);
			}
			LOGGER.info("Attachment info\n " + fileInfoBuilder.toString());
		}
	}

	public static void saveEmailAttachment(final File saveFile, final Part part) throws FileNotFoundException, IOException,
			MessagingException {
		InputStream inputStream = null;
		try {
			if (part != null) {
				inputStream = part.getInputStream();
				if (inputStream != null) {
					FileUtils.writeFileViaStream(saveFile, inputStream);
				}
			}
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (final IOException e) {
				LOGGER.error("Error while closing the input stream.", e);
			}
		}
	}

	public static String getMailPartAsText(final Part part) throws MessagingException, IOException {
		String partString = "";
		if (part.isMimeType("text/*")) {
			partString = (String) part.getContent();
		} else if (part.isMimeType("image/*")) {
			final String contentType = part.getContentType();
			final String disposition = part.getDisposition();
			final String fileName = part.getFileName();
			final StringBuilder imageTextString = new StringBuilder();
			imageTextString.append("[Image]");
			imageTextString.append("[Inline]");
			imageTextString.append(contentType);
			imageTextString.append(fileName);
			partString = imageTextString.toString();
			final StringBuilder imageInfoLogString = new StringBuilder();
			imageInfoLogString.append("Image in body.. with name:: ");
			imageInfoLogString.append(fileName);
			imageInfoLogString.append(" disposition:: ");
			imageInfoLogString.append(disposition);
			imageInfoLogString.append("HTML file tag:: ");
			imageInfoLogString.append(partString);
			LOGGER.info(imageInfoLogString.toString());
		} else if (part.isMimeType("multipart/*")) {
			final Multipart multiPart = (Multipart) part.getContent();
			for (int i = 0; i < multiPart.getCount(); i++) {
				partString = getMailPartAsText(multiPart.getBodyPart(i));
			}
		}

		return partString;
	}

	/**
	 * API to set message read flag to true.
	 * 
	 * @param isUsingImapConf
	 * @param currentMessage
	 * @throws MessagingException
	 */
	public static void setMessageProcessedFlag(final boolean isUsingImapConf, final Message currentMessage) throws MessagingException {
		LOGGER.info("Changing the seen flag for message with subject " + currentMessage.getSubject());
		currentMessage.setFlag(Flags.Flag.SEEN, true);
		
		if (!isUsingImapConf) {
			currentMessage.setFlag(Flags.Flag.DELETED, true);
			currentMessage.isSet(Flags.Flag.SEEN);
		}
	}

	public static String getBodyFromMultipartMessage(final Message message) throws MessagingException, IOException {
		String mailContent = "";
		Object messageContentObject;
		messageContentObject = message.getContent();
		if (messageContentObject != null) {
			if (messageContentObject instanceof Multipart) {
				final Multipart multipart = (Multipart) messageContentObject;

				// customMsg.setContent(MailUtil.getMailPartAsText((Part) multipart.getBodyPart(0)));
				mailContent = getMailPartAsText((Part) multipart.getBodyPart(0));
			} else if (messageContentObject instanceof String) {
				mailContent = messageContentObject.toString();
			}
		}
		return mailContent;
	}
}
