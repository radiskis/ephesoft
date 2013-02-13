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

package com.ephesoft.dcma.cmis;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.alfresco.api.Alfresco;
import org.springframework.social.alfresco.connect.AlfrescoConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Parameters;

import com.ephesoft.dcma.cmis.oauth.LocalServerReceiver;
import com.ephesoft.dcma.cmis.oauth.VerificationCodeReceiver;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;

/**
 * This class is used to making connection with Alfresco OAuth Server.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.cmis.service.CMISExportService
 */
public class AlfrescoCMISOAuth {

	/**
	 * Logger instance for logging.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AlfrescoCMISOAuth.class);

	/**
	 * API for getting the Authentication URL for Alfresco CMIS Server using Oauth.
	 * 
	 * @param clientKey {@link String}
	 * @param secretKey {@link String}
	 * @param redirectURL {@link String}
	 * @return authenticationURL.
	 */
	public String getAuthenticationURL(String clientKey, String secretKey, String redirectURL) {
		LOGGER.info("Inside get Authentication URL method");
		AlfrescoConnectionFactory connectionFactory = new AlfrescoConnectionFactory(clientKey, secretKey);

		OAuth2Parameters parameters = new OAuth2Parameters();
		parameters.setRedirectUri(redirectURL);
		parameters.setScope(Alfresco.DEFAULT_SCOPE);
		
		return connectionFactory.getOAuthOperations().buildAuthenticateUrl(GrantType.AUTHORIZATION_CODE, parameters);
	}

	/**
	 * API for getting the token map generated after successful authentication on the Alfresco CMIS Server.
	 * 
	 * @param clientKey {@link String}
	 * @param secretKey {@link String}
	 * @param redirectURL {@link String}
	 * @return tokenMap
	 * @throws DCMAApplicationException if any error or exception occurs.
	 */
	public Map<String, String> getTokenMap(String clientKey, String secretKey, String redirectURL) throws DCMAApplicationException {
		LOGGER.info("Inside get token map method");
		Map<String, String> map = new HashMap<String, String>();

		String host = ICommonConstants.EMPTY_STRING;
		String portString = ICommonConstants.EMPTY_STRING;
		String callbackPath = ICommonConstants.EMPTY_STRING;

		String[] splittedRedirectURL = redirectURL.split(ICommonConstants.FORWARD_SLASH + ICommonConstants.FORWARD_SLASH);
		if (splittedRedirectURL.length > 1) {
			String string = redirectURL.split(ICommonConstants.FORWARD_SLASH + ICommonConstants.FORWARD_SLASH)[1];
			String[] splittedString = string.split(ICommonConstants.COLON);
			if (splittedString.length > 1) {
				host = splittedString[0];
				String restURL = splittedString[1];
				String[] splittedRestURL = restURL.split(ICommonConstants.FORWARD_SLASH);
				if (splittedRestURL.length > 1) {
					portString = splittedRestURL[0];
					callbackPath = splittedRestURL[1];
				}
			}
		}

		LOGGER.info("Host address:" + host);
		LOGGER.info("Port address:" + portString);
		LOGGER.info("CallbackPath:" + callbackPath);

		if (host == null || host.isEmpty() || portString == null || portString.isEmpty() || callbackPath == null
				|| callbackPath.isEmpty()) {
			throw new DCMAApplicationException("Invalid redirect URL provided for processing.");
		}

		int port = 8080;
		try {
			port = Integer.valueOf(portString);
		} catch (NumberFormatException numberFormatException) {
			LOGGER.error("Invalid port specified in the redirect URL.");
			throw new DCMAApplicationException("Invalid port specified in redirect URL.");
		}

		VerificationCodeReceiver receiver = new LocalServerReceiver(host, port, callbackPath);
		try {
			receiver.getRedirectUri();
			AlfrescoConnectionFactory connectionFactory = new AlfrescoConnectionFactory(clientKey, secretKey);

			String code = receiver.waitForCode();
			
			OAuth2Parameters parameters = new OAuth2Parameters();
			parameters.setRedirectUri(redirectURL);
			parameters.setScope(Alfresco.DEFAULT_SCOPE);

			AccessGrant accessGrant = connectionFactory.getOAuthOperations().exchangeForAccess(code, redirectURL, parameters);
			map.put(CMISProperties.CMIS_REFRESH_TOKEN.getPropertyKey(), accessGrant.getRefreshToken());
		} catch (Exception exception) {
			LOGGER.error("Error occur while starting up the jetty server.");
			throw new DCMAApplicationException("Error occur while setting the jetty server", exception);
		} finally {
			try {
				receiver.stop();
			} catch (Exception e) {
				LOGGER.info("Error in stopping jetty server");
			}
		}
		return map;
	}
}
