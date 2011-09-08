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

package com.ephesoft.dcma.mail.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.common.CustomMessage;
import com.ephesoft.dcma.core.common.MailMetaData;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.BatchClassEmailConfiguration;
import com.sun.mail.imap.IMAPSSLStore;
import com.sun.mail.pop3.POP3SSLStore;

public class MailReceiverServiceImpl implements MailReceiverService {

	private static final Integer DEFAULT_PORT_NUMBER_POP3 = 995;
	private static final Integer DEFAULT_PORT_NUMBER_IMAP = 993;
	private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private BatchSchemaService batchSchemaService;
	private String defaultFolderLocation;
	private Store store = null;
	private Session session = null;
	String folderPath = null;

	@Override
	public List<CustomMessage> readMails(BatchClassEmailConfiguration configuration) throws DCMAApplicationException {
		defaultFolderLocation = batchSchemaService.getEmailFolderPath();
		Folder folder = null;
		Message[] messages = null;
		Boolean isSSL = false;
		String serverType = null;
		List<CustomMessage> customMessageList = null;

		try {
			isSSL = configuration.isSSL();
			serverType = configuration.getServerType();
			if (isSSL != null && isSSL) {
				if (serverType.equalsIgnoreCase("pop3")) {
					store = connectWithPOP3SSL(configuration);
				} else if (serverType.equalsIgnoreCase("imap")) {
					store = connectWithIMAPSSL(configuration);
				} else {
					logger.error("Error in Server Type Configuration, only imap/pop3 is allowed");
					throw (new DCMAApplicationException("Error in Server Type Configuration, only imap/pop3 is allowed"));
				}

			} else {
				store = connectWithNonSSL(configuration);
			}
			if (store == null || !store.isConnected()) {
				logger.error("Not able to establish connection");
				throw (new DCMAApplicationException("Not able to establish connection"));
			} else {
				folder = store.getDefaultFolder();
				folder = folder.getFolder(configuration.getFolderName());
				folder.open(Folder.READ_WRITE);
				messages = folder.getMessages();

				// Populating the message object. This is done to cater lazy loading of message objects.
				for (int messageNumber = 0; messageNumber < messages.length; messageNumber++) {

					if (messages[messageNumber] != null && messages[messageNumber].getContent() != null) {
						Object messagecontentObject = messages[messageNumber].getContent();
						if (messagecontentObject instanceof Multipart) {
							customMessageList = new ArrayList<CustomMessage>();
							Multipart multipart = (Multipart) messages[messageNumber].getContent();
							CustomMessage cm = new CustomMessage();
							MailMetaData mmd = new MailMetaData();
							cm.setMessage(messages[messageNumber]);
							cm.setContent(getText((Part) multipart.getBodyPart(0)));
							mmd = createMailMetaData(cm.getMessage(), cm.getContent());
							cm.setMailMetaData(mmd);
							String folderName = String.valueOf(new Date().getTime());
							File uniqueFolderForMail = new File(defaultFolderLocation + File.separator + folderName);
							String folderPath = uniqueFolderForMail.getPath().toString();
							cm.setFolderPath(folderPath);
							cm.setFolderName(folderName);
							customMessageList.add(cm);
							// downloading all attachments....
							for (int i = 0; i < multipart.getCount(); i++) {
								Part part = (Part) multipart.getBodyPart(i);
								downloadAttachment(part, folderPath);
							}
						}
					}

					messages[messageNumber].setFlag(Flags.Flag.DELETED, true);
				}
			}
		} catch (Exception e) {
			logger.error("Not able to process the mail reading.", e);
			throw new DCMAApplicationException(e.getMessage(), e);
		} finally {
			try {
				if (folder != null) {
					folder.close(true);
				}
				if (store != null) {
					store.close();
				}
			} catch (Exception e) {
				logger.error("Could not close the Connection.", e);
			}
		}
		return customMessageList;
	}

	public Store connectWithPOP3SSL(BatchClassEmailConfiguration configuration) throws MessagingException {
		Properties pop3Props = new Properties();

		pop3Props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
		pop3Props.setProperty("mail.pop3.socketFactory.fallback", "false");
		Integer portNumber = configuration.getPortNumber();

		if (portNumber == null) {
			logger.error("Could not find port number. Trying with default value of 995");
			portNumber = DEFAULT_PORT_NUMBER_POP3;
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
		imapProps.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
		imapProps.setProperty("mail.imap.socketFactory.fallback", "false");
		Integer portNumber = configuration.getPortNumber();

		if (portNumber == null) {
			logger.error("Could not find port number. Trying with default value of 993");
			portNumber = DEFAULT_PORT_NUMBER_IMAP;
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

			Address[] toAddressArray = message.getRecipients(RecipientType.TO);
			Address[] ccAddressArray = message.getRecipients(RecipientType.CC);
			Address[] bccAddressArray = message.getRecipients(RecipientType.BCC);

			String sender = "";
			if (toAddressArray != null && toAddressArray.length > 0) {
				for (int i = 0; i < toAddressArray.length; i++) {
					toAddresses.add(toAddressArray[i].toString());
				}
			}
			if (ccAddressArray != null && ccAddressArray.length > 0) {
				for (int i = 0; i < ccAddressArray.length; i++) {
					ccAddresses.add(ccAddressArray[i].toString());
				}
			}

			if (bccAddressArray != null && bccAddressArray.length > 0) {
				for (int i = 0; i < bccAddressArray.length; i++) {
					bccAddresses.add(bccAddressArray[i].toString());
				}
			}

			sender = ((InternetAddress) message.getFrom()[0]).getPersonal();
			if (sender == null) {
				sender = ((InternetAddress) message.getFrom()[0]).getAddress();
				logger.info("sender in NULL. Printing Address:" + sender);
			}
			mailMetaData.setToAddresses(toAddresses);
			mailMetaData.setBccAddresses(bccAddresses);
			mailMetaData.setCcAddresses(ccAddresses);
			mailMetaData.setSubject(message.getSubject().toString());
			mailMetaData.setFromAddress(sender);
		} catch (MessagingException e) {
			logger.error("Unable to retreive sender information.");
		}
		return mailMetaData;

	}

	public String getText(Part p) throws MessagingException, IOException {
		if (p.isMimeType("text/*")) {
			String s = (String) p.getContent();
			return s;
		}

		if (p.isMimeType("multipart/alternative")) {
			Multipart mp = (Multipart) p.getContent();
			String text = null;
			for (int i = 0; i < mp.getCount(); i++) {
				Part bp = mp.getBodyPart(i);
				if (bp.isMimeType("text/plain")) {
					if (text == null) {
						text = getText(bp);
						return text;
					}
					continue;
				} else if (bp.isMimeType("text/html")) {
					String s = getText(bp);
					if (s != null)
						return s;
				} else {
					return getText(bp);
				}
			}
			return text;
		} else if (p.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) p.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				String s = getText(mp.getBodyPart(i));
				if (s != null)
					return s;
			}
		}

		return null;
	}

	private void downloadAttachment(Part part, String folderPath) throws Exception {
		String disPosition = part.getDisposition();
		String fileName = null;
		fileName = part.getFileName();
		if (disPosition != null && disPosition.equalsIgnoreCase(Part.ATTACHMENT)) {
			File file = new File(folderPath + File.separator + fileName);
			file.getParentFile().mkdirs();
			saveEmailAttachment(file, part);
		}
	}

	protected int saveEmailAttachment(File saveFile, Part part) throws Exception {

		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(saveFile));

		byte[] buff = new byte[2048];
		InputStream is = part.getInputStream();
		int ret = 0, count = 0;
		while ((ret = is.read(buff)) > 0) {
			bos.write(buff, 0, ret);
			count += ret;
		}
		if (bos != null) {
			bos.close();
		}
		if (is != null) {
			is.close();
		}
		return count;
	}

}
