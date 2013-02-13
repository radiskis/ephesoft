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

package com.ephesoft.dcma.cmis.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.cmis.CMISSession;
import com.ephesoft.dcma.cmis.impl.BasicCMISSession;
import com.ephesoft.dcma.cmis.impl.OAuthCMISSession;
import com.ephesoft.dcma.cmis.impl.WebServiceCMISSession;

/**
 * This class for initializing cmis session on the basis of session type in CMIS.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public class CMISSessionFactory {
	
	/**
	 * Instance for basicCMISSession {@link BasicCMISSession}.
	 */
	@Autowired
	private BasicCMISSession basicCMISSession;
	
	/**
	 * Instance for webServiceCMISSession {@link WebServiceCMISSession}.
	 */
	@Autowired
	private WebServiceCMISSession webServiceCMISSession;
	
	/**
	 * Instance for oAuthCMISSession {@link OAuthCMISSession}.
	 */
	@Autowired
	private OAuthCMISSession oAuthCMISSession;

	/**
	 * Used for handling logs.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(CMISSessionFactory.class);
	
	/**
	 * Instance for sessionType {@link String}.
	 */
	String sessionType;

	/**
	 * Setting for sessionType.
	 * 
	 * @param sessionType
	 */
	public void setSessionType(String sessionType) {
		this.sessionType = sessionType;
	}

	/**
	 * This method is used to return the implementation of cmis session in accordance with the session type.
	 * 
	 * @return {@link CMISSession}
	 */
	public CMISSession getImplementation() {
		if (basicCMISSession == null) {
			LOG.error("Basic CMIS Session is null");
		}
		if (webServiceCMISSession == null) {
			LOG.error("WebServiceCMISSession Session is null");
		}
		if (oAuthCMISSession == null) {
			LOG.error("OAuth Cmis Session is null");
		}
		CMISSession cmisSession = null;
		if (("basic").equalsIgnoreCase(sessionType)) {
			cmisSession = basicCMISSession;
		} else if (("wssecurity").equalsIgnoreCase(sessionType)) {
			cmisSession = webServiceCMISSession;
		} else if (("oauth").equalsIgnoreCase(sessionType)) {
			cmisSession = oAuthCMISSession;
		} else {
			cmisSession = basicCMISSession;
		}
		return cmisSession;
	}
}
