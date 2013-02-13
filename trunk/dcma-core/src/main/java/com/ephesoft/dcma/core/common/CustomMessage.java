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

import javax.mail.Message;

/**
 * This class sets and return custom messages.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see javax.mail.Message
 *
 */
public class CustomMessage {

	/**
	 * message Message.
	 */
	private Message message;

	/**
	 * content String.
	 */
	private String content;

	/**
	 * mailMetaData MailMetaData.
	 */
	private MailMetaData mailMetaData;

	/**
	 * folderPath String.
	 */
	private String folderPath;

	/**
	 * folderName String.
	 */
	private String folderName;

	/**
	 * To get message.
	 * @return Message
	 */
	public Message getMessage() {
		return message;
	}

	/**
	 * To set message.
	 * @param message Message
	 */
	public void setMessage(Message message) {
		this.message = message;
	}

	/**
	 * To get Content.
	 * @return String
	 */
	public String getContent() {
		return content;
	}

	/**
	 * To set Content.
	 * @param content String
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * To get Mail Meta Data.
	 * @return MailMetaData
	 */
	public MailMetaData getMailMetaData() {
		return mailMetaData;
	}

	/**
	 * To set Mail Meta Data.
	 * @param mailMetaData MailMetaData
	 */
	public void setMailMetaData(MailMetaData mailMetaData) {
		this.mailMetaData = mailMetaData;
	}

	/**
	 * To get Folder Path.
	 * @return String
	 */
	public String getFolderPath() {
		return folderPath;
	}

	/**
	 * To set Folder Path.
	 * @param folderPath String
	 */
	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	/**
	 * To get Folder Name.
	 * @return String
	 */
	public String getFolderName() {
		return folderName;
	}

	/**
	 * To set Folder Name.
	 * @param folderName String
	 */
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

}
