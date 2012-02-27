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

package com.ephesoft.dcma.core.common;

import java.util.List;

public class MailMetaData {

	private String fromName;
	private String fromAddress;
	private String subject;
	private String replyTo;
	private List<String> toAddresses;
	private List<String> ccAddresses;
	private List<String> bccAddresses;
	private static final String CARRIAGE_RETUN_NEW_LINE = "\r\n";
	private boolean isContentHtml;
	private String body;

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	public String getReplyTo() {
		return replyTo;
	}

	public String getToAddressesAsString() {
		StringBuffer toAddress = new StringBuffer();
		toAddress.append("");

		if (toAddresses != null && toAddresses.size() > 0) {
			for (int i = 0; i < toAddresses.size(); i++) {
				toAddress.append(toAddresses.get(i));
				toAddress.append(';');

			}
		}
		return toAddress.toString();
	}

	public List<String> getToAddresses() {
		return toAddresses;
	}

	public void setToAddresses(List<String> toAddresses) {
		this.toAddresses = toAddresses;
	}

	public String getCcAddressesAsString() {
		StringBuffer ccAddress = new StringBuffer();
		ccAddress.append("");

		if (toAddresses != null && ccAddresses.size() > 0) {
			for (int i = 0; i < ccAddresses.size(); i++) {
				ccAddress.append(ccAddresses.get(i));
				ccAddress.append(';');

			}
		}
		return ccAddress.toString();
	}

	public List<String> getCcAddresses() {
		return ccAddresses;
	}

	public void setCcAddresses(List<String> ccAddresses) {
		this.ccAddresses = ccAddresses;
	}

	public String getBccAddressesAsString() {
		StringBuffer bccAddress = new StringBuffer();
		if (bccAddresses != null && bccAddresses.size() > 0) {
			for (int i = 0; i < bccAddresses.size(); i++) {
				bccAddress.append(bccAddresses.get(i));
				bccAddress.append(';');
				
				
			}
		}
		return bccAddress.toString();
	}

	public List<String> getBccAddresses() {
		return bccAddresses;
	}

	public void setBccAddresses(List<String> bccAddresses) {
		this.bccAddresses = bccAddresses;
	}

	public boolean isContentTypeHtml() {
		return isContentHtml;
	}

	public void setContentTypeHtml(boolean isContentHtml) {
		this.isContentHtml = isContentHtml;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("From:");
		stringBuffer.append(fromAddress);
		stringBuffer.append(CARRIAGE_RETUN_NEW_LINE);
		if (toAddresses != null) {
			stringBuffer.append("To:");
			stringBuffer.append(getToAddressesAsString());
			stringBuffer.append(CARRIAGE_RETUN_NEW_LINE);
		}
		if (ccAddresses != null) {
			stringBuffer.append("CC:");
			stringBuffer.append(getCcAddressesAsString());
			stringBuffer.append(CARRIAGE_RETUN_NEW_LINE);
		}
		if (bccAddresses != null) {
			stringBuffer.append("BCC:");
			stringBuffer.append(getBccAddressesAsString());
			stringBuffer.append(CARRIAGE_RETUN_NEW_LINE);
		}
		stringBuffer.append(CARRIAGE_RETUN_NEW_LINE);
		stringBuffer.append("Subject:");
		stringBuffer.append(subject);
		stringBuffer.append(CARRIAGE_RETUN_NEW_LINE);
		stringBuffer.append(CARRIAGE_RETUN_NEW_LINE);
		stringBuffer.append(CARRIAGE_RETUN_NEW_LINE);
		stringBuffer.append(body);
		return stringBuffer.toString();
	}
}
