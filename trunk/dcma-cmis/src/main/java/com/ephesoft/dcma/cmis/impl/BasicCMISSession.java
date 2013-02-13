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

package com.ephesoft.dcma.cmis.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisUnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.cmis.CMISProperties;
import com.ephesoft.dcma.cmis.CMISSession;
import com.ephesoft.dcma.cmis.constant.CMISExportConstant;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;

/**
 * This class for making connection using basic authentication in CMIS.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.cmis.CMISSession
 */
@Component
public class BasicCMISSession implements CMISSession {

	/**
	 * Instance of PluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService batchInstancePluginPropertiesService;
	
	/**
	 * Instance of serverURL {@link String}.
	 */
	String serverURL;
	
	/**
	 * Instance of serverUserName {@link String}.
	 */
	String serverUserName;
	
	/**
	 * Instance of serverPassword {@link String}.
	 */
	String serverPassword;
	
	/**
	 * Instance of repositoryID {@link String}.
	 */
	String repositoryID;
	
	/**
	 * Instance of alfrescoAspectSwitch {@link String}.
	 */
	String alfrescoAspectSwitch;

	/**
	 * setter for server URL.
	 * 
	 * @param serverURL
	 */
	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}

	/**
	 * setter for server password.
	 * 
	 * @param serverPassword
	 */
	public void setServerPassword(String serverPassword) {
		this.serverPassword = serverPassword;
	}

	/**
	 * setter for server username.
	 * 
	 * @param serverUserName
	 */
	public void setServerUserName(String serverUserName) {
		this.serverUserName = serverUserName;
	}

	/**
	 * setter for repository ID.
	 * 
	 * @param repositoryID
	 */
	public void setRepositoryID(String repositoryID) {
		this.repositoryID = repositoryID;
	}

	/**
	 * setter for alfresco aspect switch.
	 * 
	 * @param alfrescoAspectSwitch
	 */
	public void setAlfrescoAspectSwitch(String alfrescoAspectSwitch) {
		this.alfrescoAspectSwitch = alfrescoAspectSwitch;
	}

	public String getServerPassword() {
		return serverPassword;
	}

	public String getServerURL() {
		return serverURL;
	}

	public String getServerUserName() {
		return serverUserName;
	}

	public String getAlfrescoAspectSwitch() {
		return alfrescoAspectSwitch;
	}

	public String getRepositoryID() {
		return repositoryID;
	}

	@Override
	public Session getSession(String batchInstanceIdentifier) throws DCMAApplicationException {
		SessionFactory sessionFactory = SessionFactoryImpl.newInstance();

		Map<String, String> sessionParameters = new HashMap<String, String>();

		serverURL = batchInstancePluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				CMISExportConstant.CMIS_EXPORT_PLUGIN, CMISProperties.CMIS_SERVER_URL);
		validateServerUrl(serverURL);
		serverUserName = batchInstancePluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				CMISExportConstant.CMIS_EXPORT_PLUGIN, CMISProperties.CMIS_SERVER_USER_NAME);
		validateServerUserName(serverUserName);
		serverPassword = batchInstancePluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				CMISExportConstant.CMIS_EXPORT_PLUGIN, CMISProperties.CMIS_SERVER_PASSWORD);
		validateServerPassword(serverPassword);
		repositoryID = batchInstancePluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				CMISExportConstant.CMIS_EXPORT_PLUGIN, CMISProperties.CMIS_REPOSITORY_ID);
		alfrescoAspectSwitch = batchInstancePluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				CMISExportConstant.CMIS_EXPORT_PLUGIN, CMISProperties.CMIS_ASPECTS_SWITCH);

		sessionParameters.put(SessionParameter.USER, serverUserName);
		sessionParameters.put(SessionParameter.PASSWORD, serverPassword);
		sessionParameters.put(SessionParameter.ATOMPUB_URL, serverURL);
		sessionParameters.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
		if (null != repositoryID && !repositoryID.isEmpty()) {
			sessionParameters.put(SessionParameter.REPOSITORY_ID, repositoryID);
		}
		if (null != alfrescoAspectSwitch && alfrescoAspectSwitch.equals(CMISExportConstant.ON_STRING)) {
			sessionParameters.put(SessionParameter.OBJECT_FACTORY_CLASS, "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl");
		}

		return sessionFactory.createSession(sessionParameters);
	}

	@Override
	public Map<String, String> getSession() throws DCMAApplicationException {
		SessionFactory sessionFactory = null;
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> sessionParameters = new HashMap<String, String>();
		try {
			sessionFactory = SessionFactoryImpl.newInstance();

			sessionParameters.put(SessionParameter.USER, getServerUserName());
			sessionParameters.put(SessionParameter.PASSWORD, getServerPassword());
			sessionParameters.put(SessionParameter.ATOMPUB_URL, getServerURL());
			sessionParameters.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
			if (null != getRepositoryID() && !getRepositoryID().isEmpty()) {
				sessionParameters.put(SessionParameter.REPOSITORY_ID, getRepositoryID());
			}
			if (null != getRepositoryID() && getRepositoryID().equals(CMISExportConstant.ON_STRING)) {
				sessionParameters
						.put(SessionParameter.OBJECT_FACTORY_CLASS, "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl");
			}
		} catch (CmisUnauthorizedException e) {
			throw new DCMAApplicationException(CMISExportConstant.CMIS_AUTHENTICATION_FAIL, e);
		} catch (CmisObjectNotFoundException e) {
			throw new DCMAApplicationException(CMISExportConstant.CMIS_REPOSITORY_NOT_FOUND, e);
		} catch (CmisBaseException e) {
			throw new DCMAApplicationException(CMISExportConstant.CMIS_CONNECTION_FAIL, e);
		}
		sessionFactory.createSession(sessionParameters);

		return map;
	}

	private void validateServerPassword(String serverPassword) throws DCMAApplicationException {
		if (null == serverPassword || "".equals(serverPassword)) {
			throw new DCMAApplicationException(
					"Server User Password is null/empty from the data base. Invalid initializing of properties.");
		}
	}

	private void validateServerUserName(String serverUserName) throws DCMAApplicationException {
		if (null == serverUserName || "".equals(serverUserName)) {
			throw new DCMAApplicationException(
					"Server User Name is null/empty from the data base. Invalid initializing of properties.");
		}
	}

	private void validateServerUrl(String serverURL) throws DCMAApplicationException {
		if (null == serverURL || "".equals(serverURL)) {
			throw new DCMAApplicationException("Server Url is null/empty from the data base. Invalid initializing of properties.");
		}
	}
}
