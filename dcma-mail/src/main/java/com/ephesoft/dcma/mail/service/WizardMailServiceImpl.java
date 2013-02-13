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

import com.ephesoft.dcma.batch.schema.UserInformation;
import com.ephesoft.dcma.core.common.MailMetaData;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.da.domain.User;
import com.ephesoft.dcma.mail.MailContentModel;
import com.ephesoft.dcma.mail.WizardMailException;
import com.ephesoft.dcma.mail.constant.MailConstants;
import com.ephesoft.dcma.util.EphesoftStringUtil;

import freemarker.template.Configuration;

/**
 * This class is implementation for mail service.
 * 
 * @author Ephesoft
 *
 */
public class WizardMailServiceImpl implements WizardMailService {

	/**
	 * An instance of Logger for proper logging using slf4j.
	 */
	private final static Logger LOGGER = LoggerFactory.getLogger(WizardMailServiceImpl.class);

	/**
	 * wizardMailSender is used to send mail.
	 */
	private JavaMailSender wizardMailSender;

	/**
	 * freemarkerMailConfiguration is used to use predefine templates to send a mail.
	 */
	private Configuration freemarkerMailConfiguration;

	/**
	 * Error mail receiver name.
	 */
	private String errorMailReceiverName;

	/**
	 * Error mail receiver's email id.
	 */
	private String errorMailReceiverId;

	/**
	 * Subject of error mail.
	 */
	private String errorMailSubject;

	/**
	 * Template location of error mail.
	 */
	private String errorMailTemplateLocation;

	/**
	 * Subject of mail.
	 */
	private String subject;

	/**
	 * Sender name to specify in mail.
	 */
	private String senderName;

	/**
	 * Email Id for cc.
	 */
	private String ccList;

	/**
	 * Email template location.
	 */
	private String templateLocation;
	
	/**
	 * The resetPasswordTemplateLocation {@link String} is template location for 
	 * reset password mail.
	 */
	private String resetPasswordTemplateLocation;

	/**
	 * The resetPasswordSubject {@link String} is subject for 
	 * reset password mail.
	 */
	private String resetPasswordSubject;
	
	/**
	 * The changePasswordTemplateLocation {@link String} is template location for 
	 * change password mail.
	 */
	private String changePasswordTemplateLocation;

	/**
	 * The changePasswordSubject {@link String} is subject for 
	 * change password mail.
	 */
	private String changePasswordSubject;

	/**
	 * @return the templateLocation
	 */
	public String getTemplateLocation() {
		return templateLocation;
	}

	/**
	 * @param templateLocation the templateLocation to set
	 */
	public void setTemplateLocation(final String templateLocation) {
		this.templateLocation = templateLocation;
	}

	/**
	 * @return the ccList
	 */
	public String getCcList() {
		return ccList;
	}

	/**
	 * @param ccList the ccList to set
	 */
	public void setCcList(final String ccList) {
		this.ccList = ccList;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(final String subject) {
		this.subject = subject;
	}

	/**
	 * @return the senderName
	 */
	public String getSenderName() {
		return senderName;
	}

	/**
	 * @param senderName the senderName to set
	 */
	public void setSenderName(final String senderName) {
		this.senderName = senderName;
	}

	/**
	 * @return the errorMailSubject
	 */
	public String getErrorMailSubject() {
		return errorMailSubject;
	}

	/**
	 * @param errorMailSubject the errorMailSubject to set
	 */
	public void setErrorMailSubject(final String errorMailSubject) {
		this.errorMailSubject = errorMailSubject;
	}

	/**
	 * @return the errorMailTemplateLocation
	 */
	public String getErrorMailTemplateLocation() {
		return errorMailTemplateLocation;
	}

	/**
	 * @param errorMailTemplateLocation the errorMailTemplateLocation to set
	 */
	public void setErrorMailTemplateLocation(final String errorMailTemplateLocation) {
		this.errorMailTemplateLocation = errorMailTemplateLocation;
	}

	/**
	 * @return the errorMailReceiverName
	 */
	public String getErrorMailReceiverName() {
		return errorMailReceiverName;
	}

	/**
	 * @param errorMailReceiverName the errorMailReceiverName to set
	 */
	public void setErrorMailReceiverName(String errorMailReceiverName) {
		this.errorMailReceiverName = errorMailReceiverName;
	}

	/**
	 * @return the errorMailReceiverId
	 */
	public String getErrorMailReceiverId() {
		return errorMailReceiverId;
	}

	/**
	 * @param errorMailReceiverId the errorMailReceiverId to set
	 */
	public void setErrorMailReceiverId(String errorMailReceiverId) {
		this.errorMailReceiverId = errorMailReceiverId;
	}

	/**
	 * @return the wizardMailSender
	 */
	public JavaMailSender getWizardMailSender() {
		return wizardMailSender;
	}

	/**
	 * @param wizardMailSender the wizardMailSender to set
	 */
	public void setWizardMailSender(JavaMailSender wizardMailSender) {
		this.wizardMailSender = wizardMailSender;
	}

	/**
	 * @return the freemarkerMailConfiguration
	 */
	public Configuration getFreemarkerMailConfiguration() {
		return freemarkerMailConfiguration;
	}

	/**
	 * @param freemarkerMailConfiguration the freemarkerMailConfiguration to set
	 */
	public void setFreemarkerMailConfiguration(Configuration freemarkerMailConfiguration) {
		this.freemarkerMailConfiguration = freemarkerMailConfiguration;
	}
	
	
	/**
	 * The <code>getResetPasswordTemplateLocation</code> method is for getting
	 * template location.
	 * 
	 * @return {@link String} reset password template location
	 */
	public String getResetPasswordTemplateLocation() {
		return resetPasswordTemplateLocation;
	}

	/**
	 * The <code>setResetPasswordTemplateLocation</code> method is for setting
	 * reset password template location.
	 * 
	 * @param resetPasswordTemplateLocation {@link String} reset password template location
	 */
	public void setResetPasswordTemplateLocation(
			String resetPasswordTemplateLocation) {
		this.resetPasswordTemplateLocation = resetPasswordTemplateLocation;
	}
	
	/**
	 * The <code>getResetPasswordSubject</code> method is for getting
	 * template mail subject.
	 * 
	 * @return {@link String} reset password mail subject
	 */
	public String getResetPasswordSubject() {
		return resetPasswordSubject;
	}

	/**
	 * The <code>setResetPasswordSubject</code> method is for setting
	 * reset password subject.
	 * 
	 * @param resetPasswordSubject {@link String} reset password mail subject
	 */
	public void setResetPasswordSubject(String resetPasswordSubject) {
		this.resetPasswordSubject = resetPasswordSubject;
	}
	
	/**
	 * The <code>getChangePasswordTemplateLocation</code> method is for getting
	 * template location.
	 * 
	 * @return {@link String} change password template location
	 */
	public String getChangePasswordTemplateLocation() {
		return changePasswordTemplateLocation;
	}
	
	/**
	 * The <code>setChangePasswordTemplateLocation</code> method is for setting
	 * change password template location.
	 * 
	 * @param resetPasswordTemplateLocation {@link String} change password template location
	 */
	public void setChangePasswordTemplateLocation(
			String changePasswordTemplateLocation) {
		this.changePasswordTemplateLocation = changePasswordTemplateLocation;
	}
	
	/**
	 * The <code>getChangePasswordSubject</code> method is for getting
	 * template mail subject.
	 * 
	 * @return {@link String} change password mail subject
	 */
	public String getChangePasswordSubject() {
		return changePasswordSubject;
	}
	
	/**
	 * The <code>setChangePasswordSubject</code> method is for setting
	 * change password subject.
	 * 
	 * @param resetPasswordSubject {@link String} reset password mail subject
	 */
	public void setChangePasswordSubject(String changePasswordSubject) {
		this.changePasswordSubject = changePasswordSubject;
	}

	@Override
	public void sendConfirmationMail(final UserInformation userInformation, final boolean exceptionOccured,
			final String exceptionStackTrace) throws WizardMailException {
		LOGGER.info("Entering method sendConfirmationMail....");
		String templateLocation = null;
		final MailMetaData metaData = new MailMetaData();
		final MailContentModel model = new MailContentModel();
		metaData.setContentTypeHtml(true);
		metaData.setFromName(getSenderName());
		metaData.setCcAddresses(EphesoftStringUtil.convertDelimitedStringToList(getCcList()));
		final String recevierEmailId = userInformation.getEmail();
		final String receiverName = userInformation.getFirstName() + MailConstants.SPACE + userInformation.getLastName();
		final String password = userInformation.getPassword();
		final String companyName = userInformation.getCompanyName();
		final String phoneNumber = userInformation.getPhoneNumber();
		final String userInfo = "Receiver name = " + receiverName + " ; Receiver email id = " + recevierEmailId + " ; User name = "
				+ recevierEmailId + " ; Password = " + password + " ; exceptionOccured = " + exceptionOccured + " ; Company name = "
				+ companyName + " ; Phone number is = " + phoneNumber;
		if (receiverName != null && recevierEmailId != null && password != null && companyName != null && phoneNumber != null) {
			LOGGER.debug(userInfo);
			LOGGER.debug("Exception stack trace = " + exceptionStackTrace);
			model.add(MailConstants.COMPANY_NAME, companyName);
			model.add(MailConstants.CONTACT_NUMBER, phoneNumber);
			if (!exceptionOccured) {
				templateLocation = getTemplateLocation();
				metaData.setSubject(getSubject());
				metaData.setToAddresses(EphesoftStringUtil.convertDelimitedStringToList(recevierEmailId));
				model.add(MailConstants.RECEIVER_NAME, receiverName);
				model.add(MailConstants.SENDER_NAME, getSenderName());
				model.add(MailConstants.USER_NAME, recevierEmailId);
				model.add(MailConstants.PASSWORD, password);
			} else {
				templateLocation = getErrorMailTemplateLocation();
				metaData.setSubject(getErrorMailSubject());
				metaData.setToAddresses(EphesoftStringUtil.convertDelimitedStringToList(getErrorMailReceiverId()));
				model.add(MailConstants.RECEIVER_NAME, getErrorMailReceiverName());
				model.add(MailConstants.USER_NAME, receiverName);
				model.add(MailConstants.USER_EMAIL_ID, recevierEmailId);
				model.add(MailConstants.SENDER_NAME, getSenderName());
				if (null != exceptionStackTrace && !exceptionStackTrace.isEmpty()) {
					model.add(MailConstants.EXCEPTION_STACKTRACE, exceptionStackTrace);
				} else {
					model.add(MailConstants.EXCEPTION_STACKTRACE, MailConstants.EMPTY_STRING);
				}
			}
			sendTextMailWithClasspathTemplate(metaData, templateLocation, model);
			LOGGER.info("Mail sent successfully for email id = " + recevierEmailId);
		} else {
			LOGGER.error("Mail cannot be send. Invalid parameters.");
			LOGGER.error(userInfo);
			throw new WizardMailException(new StringBuilder().append("Error sending mail: Invalid user information").append(userInfo)
					.toString());
		}
		LOGGER.info("Exiting method sendConfirmationMail....");
	}

	/**
	 * API to get template location and model.
	 * 
	 * @param mailMetaData
	 * @param templateLocation
	 * @param model
	 * @throws WizardMailException
	 */
	private void sendTextMailWithClasspathTemplate(final MailMetaData mailMetaData, final String templateLocation,
			final MailContentModel model) throws WizardMailException {
		LOGGER.info("Entering method sendTextMailWithClasspathTemplate....");
		model.add("mailMeta", mailMetaData);
		try {
			final String result = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerMailConfiguration
					.getTemplate(templateLocation), model.getModel());
			LOGGER.info("Calling method sendTextMail....");
			sendTextMail(mailMetaData, result);
		} catch (Exception e) {
			LOGGER.error("Exception occured during sending Email.Email meta deta is " + mailMetaData.toString() + ".Excption is "
					+ e.getMessage(), e);
			throw new WizardMailException(new StringBuilder().append("Error sending mail: ").append(mailMetaData.toString())
					.toString(), e);
		}
		LOGGER.info("Mail sent successfully.Exiting method sendTextMailWithClasspathTemplate....");
	}

	/**
	 * API to send mail using javaMailSender.
	 * 
	 * @param mailMetaData
	 * @param text
	 * @throws WizardMailException
	 */
	private void sendTextMail(final MailMetaData mailMetaData, final String text) throws WizardMailException {
		LOGGER.info("Entering method sendTextMail....");
		final SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(new StringBuilder().append(mailMetaData.getFromName()).append(MailConstants.SPACE).append(
				MailConstants.LESS_SYMBOL).append(mailMetaData.getFromAddress()).append(MailConstants.GREATER_SYMBOL).toString());
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
			LOGGER.info("Sending mail using JavaMailSender....");
			wizardMailSender.send(mailMessage);
		} catch (MailException e) {
			LOGGER.error("Exception occured during sending Email.Email meta deta is " + mailMetaData.toString() + ".Excption is "
					+ e.getMessage(), e);
			throw new WizardMailException(new StringBuilder().append("Error sending mail: ").append(mailMetaData.toString())
					.toString(), e);
		}
		LOGGER.info("Mail sent successfully.Exiting method sendTextMail....");
	}

	/* (non-Javadoc)
	 * @see com.ephesoft.dcma.mail.service.WizardMailService#sendResetPasswordMail(java.lang.String, java.lang.String)
	 */
	@Override
	public void sendResetPasswordMail(final User user)
			throws WizardMailException {
		LOGGER.info("Entering method sendResetPasswordMail....");
		String templateLocation = null;
		
		final MailMetaData metaData = new MailMetaData();
		final MailContentModel model = new MailContentModel();
		metaData.setContentTypeHtml(true);
		metaData.setFromName(getSenderName());
		metaData.setCcAddresses(EphesoftStringUtil.convertDelimitedStringToList(getCcList()));
		final String recieverName = user.getFirstName() + ICommonConstants.SPACE + user.getLastName();
		final String username = user.getEmail();
		final String password = user.getPassword();
		
		final String userInfo = "Reciever name = " + recieverName + " ; Password is = " 
										+ password + "; User Name=" + username;
		if (null != username && null != password && null != recieverName) {
			LOGGER.debug(userInfo);
			model.add(MailConstants.USER_NAME, username);
			model.add(MailConstants.PASSWORD, password);
			model.add(MailConstants.RECEIVER_NAME, recieverName);
			templateLocation = getResetPasswordTemplateLocation();
			metaData.setSubject(getResetPasswordSubject());
			metaData.setToAddresses(EphesoftStringUtil.convertDelimitedStringToList(username));
			model.add(MailConstants.SENDER_NAME, getSenderName());
			sendTextMailWithClasspathTemplate(metaData, templateLocation, model);
			LOGGER.info("Mail sent successfully for email id = " + username);
		} else {
			LOGGER.error("Mail cannot be send. Invalid parameters.");
			LOGGER.error(userInfo);
			throw new WizardMailException(new StringBuilder().append("Error sending mail: Invalid user information").append(userInfo)
					.toString());
		}
		LOGGER.info("Exiting method sendResetPasswordMail....");
	}
	
	/* (non-Javadoc)
	 * @see com.ephesoft.dcma.mail.service.WizardMailService#sendResetPasswordMail(java.lang.String, java.lang.String)
	 */
	@Override
	public void sendChangePasswordMail(final User user)
			throws WizardMailException {
		LOGGER.info("Entering method sendChangePasswordMail....");
		String templateLocation = null;
		
		final MailMetaData metaData = new MailMetaData();
		final MailContentModel model = new MailContentModel();
		metaData.setContentTypeHtml(true);
		metaData.setFromName(getSenderName());
		metaData.setCcAddresses(EphesoftStringUtil.convertDelimitedStringToList(getCcList()));
		final String recieverName = user.getFirstName() + ICommonConstants.SPACE + user.getLastName();
		final String username = user.getEmail();
		final String password = user.getPassword();
		
		final String userInfo = "Reciever name = " + recieverName + " ; Password is = " 
										+ password + "; User Name=" + username;
		if (null != username && null != password && null != recieverName) {
			LOGGER.debug(userInfo);
			model.add(MailConstants.USER_NAME, username);
			model.add(MailConstants.PASSWORD, password);
			model.add(MailConstants.RECEIVER_NAME, recieverName);
			templateLocation = getChangePasswordTemplateLocation();
			metaData.setSubject(getChangePasswordSubject());
			metaData.setToAddresses(EphesoftStringUtil.convertDelimitedStringToList(username));
			model.add(MailConstants.SENDER_NAME, getSenderName());
			sendTextMailWithClasspathTemplate(metaData, templateLocation, model);
			LOGGER.info("Mail sent successfully for email id = " + username);
		} else {
			LOGGER.error("Mail cannot be send. Invalid parameters.");
			LOGGER.error(userInfo);
			throw new WizardMailException(new StringBuilder().append("Error sending mail: Invalid user information").append(userInfo)
					.toString());
		}
		LOGGER.info("Exiting method sendChangePasswordMail....");
	}
}
