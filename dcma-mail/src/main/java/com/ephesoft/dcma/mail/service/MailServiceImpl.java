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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.ephesoft.dcma.core.common.MailMetaData;
import com.ephesoft.dcma.mail.MailContentModel;
import com.ephesoft.dcma.mail.SendMailException;

import freemarker.template.Configuration;

/**
 * Mail service implementation class.
 * 
 * @author Ephesoft
 *
 */
public class MailServiceImpl implements MailService {

	/**
	 * Logger instance for proper logging.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

	/**
	 * An instance of {@link JavaMailSender}.
	 */
	private JavaMailSender mailSender;
	/**
	 * An instance of {@link Configuration}.
	 */
	private Configuration freemarkerMailConfiguration;
	/**
	 * boolean value for Suppress mail.
	 */
	private Boolean suppressMail;

	public void setMailSender(final JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setFreemarkerMailConfiguration(final Configuration freemarkerMailConfiguration) {
		this.freemarkerMailConfiguration = freemarkerMailConfiguration;
	}

	public void setSuppressMail(final Boolean suppressMail) {
		this.suppressMail = suppressMail;
	}

	@Override
	public void sendTextMail(final MailMetaData mailMetaData, final String text) {
		if (suppressMail) {
			return;
		}
		final SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(new StringBuilder().append(mailMetaData.getFromName()).append(" <").append(mailMetaData.getFromAddress())
				.append(">").toString());
		mailMessage.setSubject(mailMetaData.getSubject());
		mailMessage.setReplyTo(mailMetaData.getReplyTo());
		if (mailMetaData.getCcAddresses() != null && mailMetaData.getCcAddresses().size() > 0) {
			mailMessage.setCc((String[]) mailMetaData.getCcAddresses().toArray(new String[mailMetaData.getCcAddresses().size()]));
		}
		if (mailMetaData.getBccAddresses() != null && mailMetaData.getBccAddresses().size() > 0) {
			mailMessage.setBcc((String[]) mailMetaData.getBccAddresses().toArray(new String[mailMetaData.getBccAddresses().size()]));
		}
		if (mailMetaData.getToAddresses() != null && mailMetaData.getToAddresses().size() > 0) {
			mailMessage.setTo((String[]) mailMetaData.getToAddresses().toArray(new String[mailMetaData.getToAddresses().size()]));
		}
		mailMessage.setText(text);
		try {
			mailSender.send(mailMessage);
		} catch (MailException e) {
			throw new SendMailException(new StringBuilder().append("Error sending mail: ").append(mailMetaData.toString()).toString(),
					e);
		}
	}

	@Override
	public void sendTextMailWithClasspathTemplate(final MailMetaData mailMetaData, final String templateLocation,
			final MailContentModel model) {
		LOGGER.info("Entering method sendTextMailWithClasspathTemplate..");
		model.add("mailMeta", mailMetaData);
		try {
			final String result = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerMailConfiguration
					.getTemplate(templateLocation), model.getModel());
			sendTextMail(mailMetaData, result);
		} catch (Exception e) {
			throw new SendMailException(new StringBuilder().append("Error sending mail: ").append(mailMetaData.toString()).toString(),
					e);
		}
		LOGGER.info("Exiting method sendTextMailWithClasspathTemplate..");
	}

}
