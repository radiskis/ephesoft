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

package com.ephesoft.dcma.mail.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.ephesoft.dcma.core.common.MailMetaData;
import com.ephesoft.dcma.core.model.common.AbstractEntity;

@Entity
@Table(name="email_data")
public class EmailData extends AbstractEntity implements Serializable, Cloneable {

    private static final long serialVersionUID = 8351775124202755008L;

    public EmailData() {
        super();
    }

    public EmailData(String sender, String senderName, String recipient, String subject, String content) {
        this.sender = sender;
        this.senderName = senderName;
        this.recipient = recipient;
        this.subject = subject;
        this.content = content;
    }

    /**
     * Constructs an <code>EmailData</code> using the first reciever als single receiver.
     */
    public EmailData(MailMetaData mailMetaData, String content) {
        this.sender = mailMetaData.getFromAddress();
        this.senderName = mailMetaData.getFromName();
        this.recipient = mailMetaData.getToAddresses().get(0);
        this.subject = mailMetaData.getSubject();
        this.content = content;
        this.isContentTypeHtml = mailMetaData.isContentTypeHtml();
    }

    @Lob
    @Column(length = 100000)
    private String content; // sync size with Message.content!

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false)
    private String sender;

    @Column
    private String senderName;

    @Column
    private String subject;
    
    @Column(name="isContentTypeHtml")
	private boolean isContentTypeHtml;
    
    @Column(length = 500)
	private String ccList;
	
	@Column(length = 500)
	private String bccList;

    public String getContent() {
        return content;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSender() {
        return sender;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSubject() {
        return subject;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

	public boolean isContentTypeHtml() {
		return isContentTypeHtml;
	}

	public void setContentTypeHtml(boolean isContentTypeHtml) {
		this.isContentTypeHtml = isContentTypeHtml;
	}

	public String getCcList() {
		return ccList;
	}

	public void setCcList(String ccList) {
		this.ccList = ccList;
	}

	public String getBccList() {
		return bccList;
	}

	public void setBccList(String bccList) {
		this.bccList = bccList;
	}
}
