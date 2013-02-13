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

package com.ephesoft.dcma.mail.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.common.CustomMessage;
import com.ephesoft.dcma.core.common.MailMetaData;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.BatchClassEmailConfiguration;
import com.ephesoft.dcma.mail.constants.MailConstants;
import com.ephesoft.dcma.util.FileUtils;
import com.sun.mail.imap.IMAPSSLStore;
import com.sun.mail.pop3.POP3SSLStore;

/**
 * This class handles the mails recieved and processing of them.
 * 
 * @author Ephesoft
 *
 */
public class MailReceiverServiceImpl implements MailReceiverService {

	/**
	 * Size of the buffer byte array.
	 */
	private static final int BUFFER_SIZE_2048 = 2048;
	/**
	 * Constant value of "46".
	 */
	private static final int EXTENSION_VALUE_46 = 46;
	/**
	 * An instance of Logger for proper logging.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MailReceiverServiceImpl.class);
	/**
	 * An instance of {@link BatchSchemaService}.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;
	/**
	 * To store the requested connection.
	 */
	private Store store = null;
	/**
	 * To store the session.
	 */
	private Session session = null;

	@Override
	public List<CustomMessage> readMails(BatchClassEmailConfiguration configuration) throws DCMAApplicationException {
		String defaultFolderLocation = batchSchemaService.getEmailFolderPath();
		Folder folder = null;
		Message[] messages = null;
		boolean isUsingImapConf = false;
		Boolean isSSL = false;
		String serverType = null;
		List<CustomMessage> customMessageList = null;
		String errorMsg = configuration.getUserName() + MailConstants.SPACE + configuration.getServerName() + MailConstants.SPACE
				+ configuration.getServerType() + MailConstants.SPACE + configuration.getFolderName();
		try {
			isSSL = configuration.isSSL();
			serverType = configuration.getServerType();
			if (isSSL != null && isSSL) {
				if (serverType.equalsIgnoreCase("pop3")) {
					store = connectWithPOP3SSL(configuration);
				} else if (serverType.equalsIgnoreCase("imap")) {
					store = connectWithIMAPSSL(configuration);
					isUsingImapConf = true;
				} else {
					LOGGER.error("Error in Server Type Configuration, only imap/pop3 is allowed. Configuration used : " + errorMsg);
					throw new DCMAApplicationException(
							"Error in Server Type Configuration, only imap/pop3 is allowed. Configuration used : " + errorMsg);
				}

			} else {
				store = connectWithNonSSL(configuration);
			}
			if (store == null || !store.isConnected()) {
				LOGGER.error("Not able to establish connection with " + errorMsg);
				throw new DCMAApplicationException("Not able to establish connection with " + errorMsg);
			} else {
				folder = store.getDefaultFolder();
				folder = folder.getFolder(configuration.getFolderName());
				folder.open(Folder.READ_WRITE);
				messages = folder.getMessages();
				customMessageList = new ArrayList<CustomMessage>();
				// Populating the message object. This is done to cater lazy loading of message objects.
				for (int messageNumber = 0; messageNumber < messages.length; messageNumber++) {

					if (messages[messageNumber] != null && messages[messageNumber].getContent() != null) {
						if (isUsingImapConf && messages[messageNumber].isSet(Flags.Flag.SEEN)) {
							LOGGER.info("Message " + messageNumber + " already proceesed.");
							continue;
						}
						Object messagecontentObject = messages[messageNumber].getContent();
						if (messagecontentObject instanceof Multipart) {
							Multipart multipart = (Multipart) messages[messageNumber].getContent();
							CustomMessage customMsg = new CustomMessage();
							MailMetaData mmd = new MailMetaData();
							customMsg.setMessage(messages[messageNumber]);
							customMsg.setContent(getText((Part) multipart.getBodyPart(0)));
							mmd = createMailMetaData(customMsg.getMessage(), customMsg.getContent());
							customMsg.setMailMetaData(mmd);
							String folderName = String.valueOf(System.nanoTime()) + messageNumber;
							File uniqueFolderForMail = new File(defaultFolderLocation + File.separator + folderName);
							StringBuffer folderPath = new StringBuffer(uniqueFolderForMail.getPath());
							customMsg.setFolderPath(folderPath.toString());
							customMsg.setFolderName(folderName);
							customMessageList.add(customMsg);
							folderPath.append(File.separator);
							folderPath.append(ICommonConstants.DOWNLOAD_FOLDER_NAME);
							// downloading all attachments....
							for (int i = 0; i < multipart.getCount(); i++) {
								Part part = (Part) multipart.getBodyPart(i);
								downloadAttachment(part, folderPath.toString());
							}
						}
					}
					messages[messageNumber].setFlag(Flags.Flag.SEEN, true);
					if (!isUsingImapConf) {
						messages[messageNumber].setFlag(Flags.Flag.DELETED, true);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Not able to process the mail reading from configuration : " + errorMsg, e);
			throw new DCMAApplicationException("Not able to process the mail reading from configuration : " + errorMsg
					+ e.getMessage(), e);
		} finally {
			try {
				if (folder != null) {
					folder.close(true);
				}
				if (store != null) {
					store.close();
				}
			} catch (Exception e) {
				LOGGER.error("Could not close the Connection.", e);
			}
		}
		return customMessageList;
	}

	public Store connectWithPOP3SSL(BatchClassEmailConfiguration configuration) throws MessagingException {
		Properties pop3Props = new Properties();

		pop3Props.setProperty("mail.pop3.socketFactory.class", MailConstants.SSL_FACTORY);
		pop3Props.setProperty("mail.pop3.socketFactory.fallback", "false");
		Integer portNumber = configuration.getPortNumber();

		if (portNumber == null) {
			LOGGER.error("Could not find port number. Trying with default value of 995");
			portNumber = MailConstants.DEFAULT_PORT_NUMBER_POP3;
		}

		URLName url = new URLName(configuration.getServerType(), configuration.getServerName(), portNumber, "", configuration
				.getUserName(), configuration.getPassword());

		session = Session.getInstance(pop3Props, null);
		store = new POP3SSLStore(session, url);
		store.connect();
		return store;

	}

	public Store connectWithIMAPSSL(BatchClassEmailConfiguration configuration) throws MessagingException {

		Properties imapProps = new Properties();
		imapProps.setProperty("mail.imap.socketFactory.class", MailConstants.SSL_FACTORY);
		imapProps.setProperty("mail.imap.socketFactory.fallback", "false");
		imapProps.setProperty("mail.imap.partialfetch", "false");
		Integer portNumber = configuration.getPortNumber();

		if (portNumber == null) {
			LOGGER.error("Could not find port number. Trying with default value of 993");
			portNumber = MailConstants.DEFAULT_PORT_NUMBER_IMAP;
		}

		URLName url = new URLName(configuration.getServerType(), configuration.getServerName(), portNumber, "", configuration
				.getUserName(), configuration.getPassword());

		session = Session.getInstance(imapProps, null);
		store = new IMAPSSLStore(session, url);
		store.connect();
		return store;

	}

	public Store connectWithNonSSL(BatchClassEmailConfiguration configuration) throws MessagingException {
		session = Session.getDefaultInstance(System.getProperties(), null);
		store = session.getStore(configuration.getServerType());
		store.connect(configuration.getServerName(), configuration.getUserName(), configuration.getPassword());
		return store;
	}

	private MailMetaData createMailMetaData(Message message, String messageBody) {
		MailMetaData mailMetaData = new MailMetaData();
		try {
			mailMetaData.setBody(messageBody);
			List<String> toAddresses = new ArrayList<String>();
			List<String> ccAddresses = new ArrayList<String>();
			List<String> bccAddresses = new ArrayList<String>();
			String subject = message.getSubject();

			Address[] toAddressArray = message.getRecipients(RecipientType.TO);
			Address[] ccAddressArray = message.getRecipients(RecipientType.CC);
			Address[] bccAddressArray = message.getRecipients(RecipientType.BCC);

			StringBuffer sender = new StringBuffer();
			if (toAddressArray != null && toAddressArray.length > 0) {
				for (int i = 0; i < toAddressArray.length; i++) {
					String toAddress = toAddressArray[i].toString();
					toAddress = getFormattedAddress(toAddress);
					toAddresses.add(toAddress);
				}
			}
			if (ccAddressArray != null && ccAddressArray.length > 0) {
				for (int i = 0; i < ccAddressArray.length; i++) {
					String address = ccAddressArray[i].toString();
					address = getFormattedAddress(address);
					ccAddresses.add(address);
				}
			}

			if (bccAddressArray != null && bccAddressArray.length > 0) {
				for (int i = 0; i < bccAddressArray.length; i++) {
					String address = bccAddressArray[i].toString();
					address = getFormattedAddress(address);
					bccAddresses.add(address);
				}
			}

			String senderName = ((InternetAddress) message.getFrom()[0]).getPersonal();
			String senderAddress = ((InternetAddress) message.getFrom()[0]).getAddress();
			if (senderName == null) {
				sender.append(senderAddress);
				LOGGER.info("sender in NULL. Printing Address:" + sender);
			} else {
				sender.append(MailConstants.QUOTES);
				sender.append(senderName);
				sender.append(MailConstants.QUOTES);
				sender.append(MailConstants.SPACE);
				sender.append(MailConstants.OPENING_BRACKET);
				sender.append(senderAddress);
				sender.append(MailConstants.CLOSING_BRACKET);
			}
			mailMetaData.setToAddresses(toAddresses);
			if (null != bccAddresses && bccAddresses.size() > 0) {
				mailMetaData.setBccAddresses(bccAddresses);
			}
			if (null != ccAddresses && ccAddresses.size() > 0) {
				mailMetaData.setCcAddresses(ccAddresses);
			}
			if (null != subject) {
				mailMetaData.setSubject(subject);
			} else {
				mailMetaData.setSubject("");
			}
			mailMetaData.setFromAddress(sender.toString());
		} catch (MessagingException e) {
			LOGGER.error("Unable to retreive sender information.");
		}
		return mailMetaData;

	}

	private String getFormattedAddress(final String address) {
		String localAddress = address;
		localAddress = address.replace(MailConstants.OPENING_ANGULAR_BRACKET, MailConstants.OPENING_BRACKET);
		localAddress = address.replace(MailConstants.CLOSING_ANGULAR_BRACKET, MailConstants.CLOSING_BRACKET);
		return localAddress;
	}

	public String getText(final Part part) throws MessagingException, IOException {
		// String text = null;
		String text = null;
		if (part.isMimeType(MailConstants.MIME_TYPE_TEXT)) {
			text = (String) part.getContent();
			// return content;
		}
		Multipart multiPart = (Multipart) part.getContent();

		if (part.isMimeType(MailConstants.MIME_TYPE_ALTERNATIVE)) {
			for (int i = 0; i < multiPart.getCount(); i++) {
				Part bodyPart = multiPart.getBodyPart(i);
				if (bodyPart.isMimeType(MailConstants.MIME_TYPE_PLAIN_TEXT)) {
					if (text == null) {
						text = getText(bodyPart);
					} else {
						continue;
					}
				} else if (bodyPart.isMimeType(MailConstants.MIME_TYPE_HTML_TEXT)) {
					text = getText(bodyPart);
					if (text != null) {
						break;
						// return text;
					}
				} else {
					text = getText(bodyPart);
					break;
					// return getText(bodyPart);
				}
			}
			// return text;
		} else if (part.isMimeType("multipart/*")) {
			// Multipart multiPart = (Multipart) part.getContent();
			for (int i = 0; i < multiPart.getCount(); i++) {
				text = getText(multiPart.getBodyPart(i));
				if (text != null) {
					break;
					// return text;
				}
			}
		}

		return text;
	}

	private void downloadAttachment(Part part, String folderPath) throws MessagingException, IOException {
		String disPosition = part.getDisposition();
		String fileName = part.getFileName();
		String decodedAttachmentName = null;

		if (fileName != null) {
			LOGGER.info("Attached File Name :: " + fileName);
			decodedAttachmentName = MimeUtility.decodeText(fileName);
			LOGGER.info("Decoded string :: " + decodedAttachmentName);
			decodedAttachmentName = Normalizer.normalize(decodedAttachmentName, Normalizer.Form.NFC);
			LOGGER.info("Normalized string :: " + decodedAttachmentName);
			int extensionIndex = decodedAttachmentName.indexOf(EXTENSION_VALUE_46);
			extensionIndex = extensionIndex == -1 ? decodedAttachmentName.length() : extensionIndex;
			File parentFile = new File(folderPath);
			LOGGER.info("Updating file name if any file with the same name exists. File : " + decodedAttachmentName);
			decodedAttachmentName = FileUtils.getUpdatedFileNameForDuplicateFile(decodedAttachmentName.substring(0, extensionIndex), parentFile, -1)
					+ decodedAttachmentName.substring(extensionIndex);

			LOGGER.info("Updated file name : " + decodedAttachmentName);
		}
		if (disPosition != null && disPosition.equalsIgnoreCase(Part.ATTACHMENT)) {
			File file = new File(folderPath + File.separator + decodedAttachmentName);
			file.getParentFile().mkdirs();
			saveEmailAttachment(file, part);
		}
	}

	private int saveEmailAttachment(File saveFile, Part part) throws IOException, MessagingException {

		BufferedOutputStream bos = null;
		InputStream inputStream = null;
		int ret = 0, count = 0;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(saveFile));

			byte[] buff = new byte[BUFFER_SIZE_2048];
			inputStream = part.getInputStream();
			ret = inputStream.read(buff);
			while (ret > 0) {
				bos.write(buff, 0, ret);
				count += ret;
				ret = inputStream.read(buff);
			}
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException ioe) {
				LOGGER.error("Error while closing the stream.", ioe);
			}
		}
		return count;
	}

}
