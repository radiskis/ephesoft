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
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.exceptions.CmisUnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.social.alfresco.api.Alfresco;
import org.springframework.social.alfresco.connect.AlfrescoConnectionFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.cmis.CMISProperties;
import com.ephesoft.dcma.cmis.CMISSession;
import com.ephesoft.dcma.cmis.constant.CMISExportConstant;
import com.ephesoft.dcma.core.constant.PluginNames;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchClassPlugin;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;

/**
 * This class for making connection using OAuth authentication in CMIS.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.cmis.CMISSession
 */
@Component
public class OAuthCMISSession implements CMISSession {

	/**
	 * Instance of PluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchClassPluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * Instance of BatchInstanceService.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;
	
	/**
	 * Instance of BatchClassService.
	 */
	@Autowired
	private BatchClassService batchClassService;
	
	/**
	 * Instance of clientKey {@link String}.
	 */
	String clientKey = null;
	
	/**
	 * Instance of secretKey {@link String}.
	 */
	String secretKey = null;
	
	/**
	 * Instance of refreshToken {@link String}.
	 */
	String refreshToken = null;
	
	/**
	 * Instance of network {@link String}.
	 */
	String network = null;
	
	/**
	 * Instance of batchClassIdentifier {@link String}.
	 */
	String batchClassIdentifier = null;
	
	/**
	 * Variable of object {@link Object} for synchronization.
	 */
	Object object = new Object();
	
	/**
	 * Setter for clientKey.
	 * @param clientKey {@link String}
	 */
	public void setClientKey(String clientKey) {
		this.clientKey = clientKey;
	}
	
	/**
	 * Setter for secretKey.
	 * @param secretKey {@link String}
	 */
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
	/**
	 * Setter for refreshToken.
	 * @param refreshToken {@link String}
	 */
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	/**
	 * Setter for network.
	 * @param network {@link String}
	 */
	public void setNetwork(String network) {
		this.network = network;
	}
	
	/**
	 * Setter for batchClassIdentifier.
	 * @param batchClassIdentifier {@link String}
	 */
	public void setBatchClassIdentifier(String batchClassIdentifier) {
		this.batchClassIdentifier = batchClassIdentifier;
	}

	@Override
	public Map<String, String> getSession() throws DCMAApplicationException {

		Map<String, String> map = new HashMap<String, String>();
		Alfresco alfresco = null;
		try {
			validateClientKey(clientKey);
			validateSecretKey(secretKey);
			validateRefreshToken(refreshToken);
			validateNetwork(network);
			AlfrescoConnectionFactory connectionFactory = new AlfrescoConnectionFactory(clientKey, secretKey);
			OAuth2Parameters parameters = new OAuth2Parameters();
			parameters.setScope(Alfresco.DEFAULT_SCOPE);
			AccessGrant accessGrant = connectionFactory.getOAuthOperations().refreshAccess(refreshToken, null, parameters);
			Connection<Alfresco> connection = connectionFactory.createConnection(accessGrant);
			alfresco = connection.getApi();
			map.put(CMISProperties.CMIS_REFRESH_TOKEN.getPropertyKey(), accessGrant.getRefreshToken());
			if (alfresco != null) {
				alfresco.getCMISSession(network);
			} else {
				throw new DCMAApplicationException("Unable to create alfresco instance");
			}

			// Get CMIS Session
		} catch (HttpClientErrorException httpClientErrorException) {
			throw new DCMAApplicationException(CMISExportConstant.CMIS_AUTHENTICATION_FAIL, httpClientErrorException);
		} catch (ResourceAccessException resourceAccessException) {
			throw new DCMAApplicationException(CMISExportConstant.CMIS_CONNECTION_FAIL, resourceAccessException);
		} catch (CmisUnauthorizedException cmisUnauthorizedException) {
			throw new DCMAApplicationException(CMISExportConstant.CMIS_UNAUTHORIZED_ACCESS, cmisUnauthorizedException);
		}
		return map;
	}

	@Override
	public Session getSession(String batchInstanceIdentifier) throws DCMAApplicationException {
		synchronized (object) {
			BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdentifier);
			BatchClass batchClass = batchInstance.getBatchClass();
			String batchClassIdentifier = batchClass.getIdentifier();

			refreshToken = pluginPropertiesService.getPropertyValue(batchClassIdentifier, CMISExportConstant.CMIS_EXPORT_PLUGIN,
					CMISProperties.CMIS_REFRESH_TOKEN);
			validateRefreshToken(refreshToken);
			clientKey = pluginPropertiesService.getPropertyValue(batchClassIdentifier, CMISExportConstant.CMIS_EXPORT_PLUGIN,
					CMISProperties.CMIS_SERVER_USER_NAME);
			validateClientKey(clientKey);
			secretKey = pluginPropertiesService.getPropertyValue(batchClassIdentifier, CMISExportConstant.CMIS_EXPORT_PLUGIN,
					CMISProperties.CMIS_SERVER_PASSWORD);
			validateSecretKey(secretKey);
			network = pluginPropertiesService.getPropertyValue(batchClassIdentifier, CMISExportConstant.CMIS_EXPORT_PLUGIN,
					CMISProperties.CMIS_ASPECTS_SWITCH);
			validateNetwork(network);

			AlfrescoConnectionFactory connectionFactory = new AlfrescoConnectionFactory(clientKey, secretKey);

			OAuth2Parameters parameters = new OAuth2Parameters();
			parameters.setScope(Alfresco.DEFAULT_SCOPE);

			AccessGrant accessGrant = connectionFactory.getOAuthOperations().refreshAccess(refreshToken, null, parameters);

			boolean isFound = false;
			List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
			for (BatchClassModule batchClassModule : batchClassModules) {
				List<BatchClassPlugin> batchClassPlugins = batchClassModule.getBatchClassPlugins();
				for (BatchClassPlugin batchClassPlugin : batchClassPlugins) {
					if (batchClassPlugin.getPlugin().getPluginName().equalsIgnoreCase(PluginNames.CMIS_EXPORT_PLUGIN)) {
						List<BatchClassPluginConfig> batchClassPluginConfigs = batchClassPlugin.getBatchClassPluginConfigs();
						for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
							if (batchClassPluginConfig.getPluginConfig().getName().equalsIgnoreCase(
									CMISProperties.CMIS_REFRESH_TOKEN.getPropertyKey())) {
								batchClassPluginConfig.setValue(accessGrant.getRefreshToken());
								isFound = true;
								break;
							}
						}
						if (isFound) {
							break;
						}
					}
				}
				if (isFound) {
					break;
				}
			}
			batchClassService.saveOrUpdate(batchClass);

			Connection<Alfresco> connection = connectionFactory.createConnection(accessGrant);
			Alfresco alfresco = connection.getApi();

			// Get CMIS Session
			Session session = alfresco.getCMISSession(network);
			return session;
		}
	}

	private void validateNetwork(String networkString) throws DCMAApplicationException {
		if (networkString == null || networkString.isEmpty()) {
			throw new DCMAApplicationException("Network value is null/empty");
		}
	}

	private void validateSecretKey(String secretKeyString) throws DCMAApplicationException {
		if (secretKeyString == null || secretKeyString.isEmpty()) {
			throw new DCMAApplicationException("Secret Key value is null/empty");
		}
	}

	private void validateClientKey(String clientKeyString) throws DCMAApplicationException {
		if (clientKeyString == null || clientKeyString.isEmpty()) {
			throw new DCMAApplicationException("ClientKey value is null/empty");
		}
	}

	private void validateRefreshToken(String refreshTokenString) throws DCMAApplicationException {
		if (refreshTokenString == null || refreshTokenString.isEmpty()) {
			throw new DCMAApplicationException("Refresh Token value is null/empty");
		}
	}
}
