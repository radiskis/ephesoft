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

package com.ephesoft.dcma.core.common;

import java.util.List;

/**
 * This class handles mailing details.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see java.util.List
 * 
 */
public class MailMetaData {

	/**
	 * Constant for EMPTY_STRING.
	 */
	private static final String EMPTY_STRING = "";

	/**
	 * Constant for SEMICOLON.
	 */
	private static final char SEMICOLON = ';';

	/**
	 * fromName String.
	 */
	private String fromName;

	/**
	 * fromAddress String.
	 */
	private String fromAddress;

	/**
	 * subject String.
	 */
	private String subject;

	/**
	 * replyTo String.
	 */
	private String replyTo;

	/**
	 * toAddresses List<String>.
	 */
	private List<String> toAddresses;

	/**
	 * ccAddresses List<String>.
	 */
	private List<String> ccAddresses;

	/**
	 * bccAddresses List<String>.
	 */
	private List<String> bccAddresses;

	/**
	 * CARRIAGE_RETUN_NEW_LINE String.
	 */
	private static final String CARRIAGE_RETUN_NEW_LINE = "\r\n";

	/**
	 * isContentHtml boolean.
	 */
	private boolean isContentHtml;

	/**
	 * body String.
	 */
	private String body;

	/**
	 * To get from-Name.
	 * 
	 * @return String
	 */
	public String getFromName() {
		return fromName;
	}

	/**
	 * To set from-Name.
	 * 
	 * @param fromName String
	 */
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	/**
	 * To get from-Address.
	 * 
	 * @return String
	 */
	public String getFromAddress() {
		return fromAddress;
	}

	/**
	 * To set from-Address.
	 * 
	 * @param fromAddress String
	 */
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	/**
	 * To get Subject.
	 * 
	 * @return String
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * To set Subject.
	 * 
	 * @param subject String
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * To set reply-to.
	 * 
	 * @param replyTo String
	 */
	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	/**
	 * To get reply-to.
	 * 
	 * @return String
	 */
	public String getReplyTo() {
		return replyTo;
	}

	/**
	 * To get to-Addresses as String.
	 * 
	 * @return String
	 */
	public String getToAddressesAsString() {
		StringBuffer toAddress = new StringBuffer();
		toAddress.append(EMPTY_STRING);

		if (toAddresses != null && toAddresses.size() > 0) {
			for (int i = 0; i < toAddresses.size(); i++) {
				toAddress.append(toAddresses.get(i));
				toAddress.append(SEMICOLON);

			}
		}
		return toAddress.toString();
	}

	/**
	 * To set to-addresses.
	 * 
	 * @return List<String>
	 */
	public List<String> getToAddresses() {
		return toAddresses;
	}

	/**
	 * To set to-addresses.
	 * 
	 * @param toAddresses List<String>
	 */
	public void setToAddresses(List<String> toAddresses) {
		this.toAddresses = toAddresses;
	}

	/**
	 * To get Cc Addresses as String.
	 * 
	 * @return String
	 */
	public String getCcAddressesAsString() {
		StringBuffer ccAddress = new StringBuffer();
		ccAddress.append(EMPTY_STRING);

		if (toAddresses != null && ccAddresses.size() > 0) {
			for (int i = 0; i < ccAddresses.size(); i++) {
				ccAddress.append(ccAddresses.get(i));
				ccAddress.append(SEMICOLON);

			}
		}
		return ccAddress.toString();
	}

	/**
	 * To get Cc Addresses.
	 * 
	 * @return List<String>
	 */
	public List<String> getCcAddresses() {
		return ccAddresses;
	}

	/**
	 * To set Cc Addresses.
	 * 
	 * @param ccAddresses List<String>
	 */
	public void setCcAddresses(List<String> ccAddresses) {
		this.ccAddresses = ccAddresses;
	}

	/**
	 * To get Bcc Addresses as String.
	 * 
	 * @return String
	 */
	public String getBccAddressesAsString() {
		StringBuffer bccAddress = new StringBuffer();
		if (bccAddresses != null && bccAddresses.size() > 0) {
			for (int i = 0; i < bccAddresses.size(); i++) {
				bccAddress.append(bccAddresses.get(i));
				bccAddress.append(SEMICOLON);
			}
		}
		return bccAddress.toString();
	}

	/**
	 * To get Bcc Addresses.
	 * 
	 * @return List<String>
	 */
	public List<String> getBccAddresses() {
		return bccAddresses;
	}

	/**
	 * To set Bcc Addresses.
	 * 
	 * @param bccAddresses List<String>
	 */
	public void setBccAddresses(List<String> bccAddresses) {
		this.bccAddresses = bccAddresses;
	}

	/**
	 * To get Content Type Html.
	 * 
	 * @return boolean
	 */
	public boolean isContentTypeHtml() {
		return isContentHtml;
	}

	/**
	 * To set Content Type Html.
	 * 
	 * @param isContentHtml boolean
	 */
	public void setContentTypeHtml(boolean isContentHtml) {
		this.isContentHtml = isContentHtml;
	}

	/**
	 * To get body.
	 * 
	 * @return String
	 */
	public String getBody() {
		return body;
	}

	/**
	 * To set body.
	 * 
	 * @param body String
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * To convert to string.
	 * 
	 * @return String
	 */
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("From: ");
		stringBuffer.append(fromAddress);
		stringBuffer.append(CARRIAGE_RETUN_NEW_LINE);
		if (toAddresses != null) {
			stringBuffer.append("To: ");
			stringBuffer.append(getToAddressesAsString());
			stringBuffer.append(CARRIAGE_RETUN_NEW_LINE);
		}
		if (ccAddresses != null) {
			stringBuffer.append("CC: ");
			stringBuffer.append(getCcAddressesAsString());
			stringBuffer.append(CARRIAGE_RETUN_NEW_LINE);
		}
		if (bccAddresses != null) {
			stringBuffer.append("BCC: ");
			stringBuffer.append(getBccAddressesAsString());
			stringBuffer.append(CARRIAGE_RETUN_NEW_LINE);
		}
		stringBuffer.append(CARRIAGE_RETUN_NEW_LINE);
		stringBuffer.append("Subject: ");
		stringBuffer.append(subject);
		stringBuffer.append(CARRIAGE_RETUN_NEW_LINE);
		stringBuffer.append(CARRIAGE_RETUN_NEW_LINE);
		stringBuffer.append(CARRIAGE_RETUN_NEW_LINE);
		stringBuffer.append(body);
		return stringBuffer.toString();
	}
}
