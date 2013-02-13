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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.cmis.CMISProperties;
import com.ephesoft.dcma.cmis.CMISSession;
import com.ephesoft.dcma.cmis.URLService;
import com.ephesoft.dcma.cmis.constant.CMISExportConstant;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;

/**
 * This class for making connection using Web Service authentication in CMIS.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.cmis.CMISSession
 */
@Component
public class WebServiceCMISSession implements CMISSession {

	/**
	 * Instance of PluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService batchInstancePluginPropertiesService;

	/**
	 * Logger instance for logging.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(WebServiceCMISSession.class);
	
	/**
	 * Instance of aclServiceURL {@link String}.
	 */
	String aclServiceURL = null;
	
	/**
	 * Instance of discoverServiceURL {@link String}.
	 */
	String discoverServiceURL = null;
	
	/**
	 * Instance of multifilingServiceURL {@link String}.
	 */
	String multifilingServiceURL = null;
	
	/**
	 * Instance of navigationServiceURL {@link String}.
	 */
	String navigationServiceURL = null;
	
	/**
	 * Instance of objectServiceURL {@link String}.
	 */
	String objectServiceURL = null;
	
	/**
	 * Instance of policyServiceURL {@link String}.
	 */
	String policyServiceURL = null;
	
	/**
	 * Instance of relationshipServiceURL {@link String}.
	 */
	String relationshipServiceURL = null;
	
	/**
	 * Instance of repositoryServiceURL {@link String}.
	 */
	String repositoryServiceURL = null;
	
	/**
	 * Instance of versioningServiceURL {@link String}.
	 */
	String versioningServiceURL = null;
	
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
	 * Setter for serverURL.
	 * @param serverURL {@link String}
	 */
	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}
	
	/**
	 * Setter for serverPassword.
	 * @param serverPassword {@link String}
	 */
	public void setServerPassword(String serverPassword) {
		this.serverPassword = serverPassword;
	}

	/**
	 * Setter for serverUserName.
	 * @param serverUserName {@link String}
	 */
	public void setServerUserName(String serverUserName) {
		this.serverUserName = serverUserName;
	}
	
	/**
	 * Setter for repositoryID.
	 * @param repositoryID {@link String}
	 */
	public void setRepositoryID(String repositoryID) {
		this.repositoryID = repositoryID;
	}
	
	/**
	 * Setter for alfrescoAspectSwitch. 
	 * @param alfrescoAspectSwitch {@link String}
	 */
	public void setAlfrescoAspectSwitch(String alfrescoAspectSwitch) {
		this.alfrescoAspectSwitch = alfrescoAspectSwitch;
	}
	
	/**
	 * An instance of {@link URLService}.
	 */
	@Autowired
	private URLService urlService;

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

		try {
			StringBuffer errorMsg = new StringBuffer();

			// Get the URL's for each of the services.
			aclServiceURL = urlService.getUrlAclService(); // bundle.getString("cmis.url.acl_service");
			aclServiceURL = urlService.createACLServiceURL(serverURL, aclServiceURL, errorMsg);

			discoverServiceURL = urlService.getUrlDiscoveryService(); // bundle.getString("cmis.url.discovery_service");
			discoverServiceURL = urlService.createDiscoverServiceURL(serverURL, discoverServiceURL, errorMsg);

			multifilingServiceURL = urlService.getUrlMultifilingService(); // bundle.getString("cmis.url.multifiling_service");
			multifilingServiceURL = urlService.createMultiFilingServiceURL(serverURL, multifilingServiceURL, errorMsg);

			navigationServiceURL = urlService.getUrlNavigationService(); // bundle.getString("cmis.url.navigation_service");
			navigationServiceURL = urlService.createNavigationServiceURL(serverURL, navigationServiceURL, errorMsg);

			objectServiceURL = urlService.getUrlObjectService(); // bundle.getString("cmis.url.object_service");
			objectServiceURL = urlService.createObjectServiceURL(serverURL, objectServiceURL, errorMsg);

			policyServiceURL = urlService.getUrlPolicyService(); // bundle.getString("cmis.url.policy_service");
			policyServiceURL = urlService.createPolicyServiceURL(serverURL, policyServiceURL, errorMsg);

			relationshipServiceURL = urlService.getUrlRelationshipService(); // bundle.getString("cmis.url.relationship_service");
			relationshipServiceURL = urlService.createRelationshipServiceURL(serverURL, relationshipServiceURL, errorMsg);

			repositoryServiceURL = urlService.getUrlRepositoryService(); // bundle.getString("cmis.url.repository_service");
			repositoryServiceURL = urlService.createRepositoryServiceURL(serverURL, repositoryServiceURL, errorMsg);

			versioningServiceURL = urlService.getUrlVersioningService();// bundle.getString("cmis.url.versioning_service");
			versioningServiceURL = urlService.createVersioningServiceURL(serverURL, versioningServiceURL, errorMsg);
		} catch (Exception configEx) {
			LOGGER.error("CMIS CONFIGURATION ERROR: An error occurred while attempting to obtain configuration properties from the "
					+ "configuraiton file. The error was: " + configEx.getMessage());
		}

		// Configure Open Chemistry for WS-Security.
		sessionParameters.put(SessionParameter.BINDING_TYPE, BindingType.WEBSERVICES.value());
		sessionParameters.put(SessionParameter.AUTH_SOAP_USERNAMETOKEN, "true");
		sessionParameters.put(SessionParameter.AUTH_HTTP_BASIC, CMISExportConstant.FALSE);

		sessionParameters.put(SessionParameter.WEBSERVICES_ACL_SERVICE, aclServiceURL);
		sessionParameters.put(SessionParameter.WEBSERVICES_DISCOVERY_SERVICE, discoverServiceURL);
		sessionParameters.put(SessionParameter.WEBSERVICES_MULTIFILING_SERVICE, multifilingServiceURL);
		sessionParameters.put(SessionParameter.WEBSERVICES_NAVIGATION_SERVICE, navigationServiceURL);
		sessionParameters.put(SessionParameter.WEBSERVICES_OBJECT_SERVICE, objectServiceURL);
		sessionParameters.put(SessionParameter.WEBSERVICES_POLICY_SERVICE, policyServiceURL);
		sessionParameters.put(SessionParameter.WEBSERVICES_RELATIONSHIP_SERVICE, relationshipServiceURL);
		sessionParameters.put(SessionParameter.WEBSERVICES_REPOSITORY_SERVICE, repositoryServiceURL);
		sessionParameters.put(SessionParameter.WEBSERVICES_VERSIONING_SERVICE, versioningServiceURL);

		if (null != repositoryID && !repositoryID.isEmpty()) {
			sessionParameters.put(SessionParameter.REPOSITORY_ID, repositoryID);
		}
		if (null != alfrescoAspectSwitch && alfrescoAspectSwitch.equals(CMISExportConstant.ON_STRING)) {
			sessionParameters.put(SessionParameter.OBJECT_FACTORY_CLASS, "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl");
		}

		return sessionFactory.createSession(sessionParameters);
	}

	@Override
	public Map<String, String> getSession() {
		Map<String, String> map = new HashMap<String, String>();
		SessionFactory sessionFactory = SessionFactoryImpl.newInstance();

		Map<String, String> sessionParameters = new HashMap<String, String>();

		sessionParameters.put(SessionParameter.USER, serverUserName);
		sessionParameters.put(SessionParameter.PASSWORD, serverPassword);

		try {
			StringBuffer errorMsg = new StringBuffer();

			// Get the URL's for each of the services.
			aclServiceURL = urlService.getUrlAclService(); // bundle.getString("cmis.url.acl_service");
			aclServiceURL = urlService.createACLServiceURL(serverURL, aclServiceURL, errorMsg);

			discoverServiceURL = urlService.getUrlDiscoveryService(); // bundle.getString("cmis.url.discovery_service");
			discoverServiceURL = urlService.createDiscoverServiceURL(serverURL, discoverServiceURL, errorMsg);

			multifilingServiceURL = urlService.getUrlMultifilingService(); // bundle.getString("cmis.url.multifiling_service");
			multifilingServiceURL = urlService.createMultiFilingServiceURL(serverURL, multifilingServiceURL, errorMsg);

			navigationServiceURL = urlService.getUrlNavigationService(); // bundle.getString("cmis.url.navigation_service");
			navigationServiceURL = urlService.createNavigationServiceURL(serverURL, navigationServiceURL, errorMsg);

			objectServiceURL = urlService.getUrlObjectService(); // bundle.getString("cmis.url.object_service");
			objectServiceURL = urlService.createObjectServiceURL(serverURL, objectServiceURL, errorMsg);

			policyServiceURL = urlService.getUrlPolicyService(); // bundle.getString("cmis.url.policy_service");
			policyServiceURL = urlService.createPolicyServiceURL(serverURL, policyServiceURL, errorMsg);

			relationshipServiceURL = urlService.getUrlRelationshipService(); // bundle.getString("cmis.url.relationship_service");
			relationshipServiceURL = urlService.createRelationshipServiceURL(serverURL, relationshipServiceURL, errorMsg);

			repositoryServiceURL = urlService.getUrlRepositoryService(); // bundle.getString("cmis.url.repository_service");
			repositoryServiceURL = urlService.createRepositoryServiceURL(serverURL, repositoryServiceURL, errorMsg);

			versioningServiceURL = urlService.getUrlVersioningService();// bundle.getString("cmis.url.versioning_service");
			versioningServiceURL = urlService.createVersioningServiceURL(serverURL, versioningServiceURL, errorMsg);
		} catch (Exception configEx) {
			LOGGER.error("CMIS CONFIGURATION ERROR: An error occurred while attempting to obtain configuration properties from the "
					+ "configuraiton file. The error was: " + configEx.getMessage());
		}

		// Configure Open Chemistry for WS-Security.
		sessionParameters.put(SessionParameter.BINDING_TYPE, BindingType.WEBSERVICES.value());
		sessionParameters.put(SessionParameter.AUTH_SOAP_USERNAMETOKEN, "true");
		sessionParameters.put(SessionParameter.AUTH_HTTP_BASIC, CMISExportConstant.FALSE);

		sessionParameters.put(SessionParameter.WEBSERVICES_ACL_SERVICE, aclServiceURL);
		sessionParameters.put(SessionParameter.WEBSERVICES_DISCOVERY_SERVICE, discoverServiceURL);
		sessionParameters.put(SessionParameter.WEBSERVICES_MULTIFILING_SERVICE, multifilingServiceURL);
		sessionParameters.put(SessionParameter.WEBSERVICES_NAVIGATION_SERVICE, navigationServiceURL);
		sessionParameters.put(SessionParameter.WEBSERVICES_OBJECT_SERVICE, objectServiceURL);
		sessionParameters.put(SessionParameter.WEBSERVICES_POLICY_SERVICE, policyServiceURL);
		sessionParameters.put(SessionParameter.WEBSERVICES_RELATIONSHIP_SERVICE, relationshipServiceURL);
		sessionParameters.put(SessionParameter.WEBSERVICES_REPOSITORY_SERVICE, repositoryServiceURL);
		sessionParameters.put(SessionParameter.WEBSERVICES_VERSIONING_SERVICE, versioningServiceURL);

		if (null != repositoryID && !repositoryID.isEmpty()) {
			sessionParameters.put(SessionParameter.REPOSITORY_ID, repositoryID);
		}
		if (null != alfrescoAspectSwitch && alfrescoAspectSwitch.equals(CMISExportConstant.ON_STRING)) {
			sessionParameters.put(SessionParameter.OBJECT_FACTORY_CLASS, "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl");
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
