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

import com.ephesoft.dcma.cmis.constant.CMISExportConstant;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;

/**
 * This class handles all the url services.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.exception.DCMAApplicationException
 */
public class URLService {

	/**
	 * A private string for url acl services.
	 */
	private String urlAclService;
	/**
	 * A private string for url discovery services.
	 */
	private String urlDiscoveryService;
	/**
	 * A private string for url multi filling services.
	 */
	private String urlMultifilingService;
	/**
	 * A private string for url navigation services.
	 */
	private String urlNavigationService;
	/**
	 * A private string for url object services.
	 */
	private String urlObjectService;
	/**
	 * A private string for url policy services.
	 */
	private String urlPolicyService;
	/**
	 * A private string for url relationship services.
	 */
	private String urlRelationshipService;
	/**
	 * A private string for url repository services.
	 */
	private String urlRepositoryService;
	/**
	 * A private string for url versioning services.
	 */
	private String urlVersioningService;

	/**
	 * getter for urlAclService.
	 * @return {@link String}
	 */
	public String getUrlAclService() {
		return urlAclService;
	}

	/**
	 * setter for urlAclService.
	 * @param urlAclService {@link String}
	 */
	public void setUrlAclService(String urlAclService) {
		this.urlAclService = urlAclService;
	}

	/**
	 * getter for urlDiscoveryService.
	 * @return {@link String}
	 */
	public String getUrlDiscoveryService() {
		return urlDiscoveryService;
	}

	/**
	 * setter for urlDiscoveryService.
	 * @param urlDiscoveryService {@link String}
	 */
	public void setUrlDiscoveryService(String urlDiscoveryService) {
		this.urlDiscoveryService = urlDiscoveryService;
	}

	/**
	 * getter for urlMultifilingService.
	 * @return {@link String}
	 */
	public String getUrlMultifilingService() {
		return urlMultifilingService;
	}

	/**
	 * setter for urlMultifilingService.
	 * @param urlMultifilingService {@link String}
	 */
	public void setUrlMultifilingService(String urlMultifilingService) {
		this.urlMultifilingService = urlMultifilingService;
	}

	/**
	 * getter for urlNavigationService.
	 * @return {@link String}
	 */
	public String getUrlNavigationService() {
		return urlNavigationService;
	}

	/**
	 * setter for urlNavigationService.
	 * @param urlNavigationService {@link String}
	 */
	public void setUrlNavigationService(String urlNavigationService) {
		this.urlNavigationService = urlNavigationService;
	}

	/**
	 * getter for urlObjectService.
	 * @return {@link String}
	 */
	public String getUrlObjectService() {
		return urlObjectService;
	}

	/**
	 * setter for urlObjectService.
	 * @param urlObjectService {@link String}
	 */
	public void setUrlObjectService(String urlObjectService) {
		this.urlObjectService = urlObjectService;
	}

	/**
	 * getter for urlPolicyService.
	 * @return {@link String}
	 */
	public String getUrlPolicyService() {
		return urlPolicyService;
	}

	/**
	 * setter for urlPolicyService.
	 * @param urlPolicyService {@link String}
	 */
	public void setUrlPolicyService(String urlPolicyService) {
		this.urlPolicyService = urlPolicyService;
	}

	/**
	 * getter for urlRelationshipService.
	 * @return {@link String}
	 */
	public String getUrlRelationshipService() {
		return urlRelationshipService;
	}

	/**
	 * setter for urlRelationshipService.
	 * @param urlRelationshipService {@link String}
	 */
	public void setUrlRelationshipService(String urlRelationshipService) {
		this.urlRelationshipService = urlRelationshipService;
	}

	/**
	 * getter for urlRepositoryService.
	 * @return {@link String}
	 */
	public String getUrlRepositoryService() {
		return urlRepositoryService;
	}

	/**
	 * setter for urlRepositoryService.
	 * @param urlRepositoryService {@link String}
	 */
	public void setUrlRepositoryService(String urlRepositoryService) {
		this.urlRepositoryService = urlRepositoryService;
	}

	/**
	 * getter for urlVersioningService.
	 * @return {@link String}
	 */
	public String getUrlVersioningService() {
		return urlVersioningService;
	}

	/**
	 * setter for urlVersioningService.
	 * @param urlVersioningService {@link String}
	 */
	public void setUrlVersioningService(String urlVersioningService) {
		this.urlVersioningService = urlVersioningService;
	}

	/**
	 * This method creates the ACL service url.
	 * @param serverURL {@link String}
	 * @param aclServiceURL {@link String}
	 * @param errorMsg {@link StringBuffer}
	 * @return {@link String}
	 * @throws DCMAApplicationException if an exception occurs.
	 */
	public String createACLServiceURL(String serverURL, String aclServiceURL, StringBuffer errorMsg) throws DCMAApplicationException {
		String aclURL;
		if ((aclServiceURL == null) || (aclServiceURL.trim().length() <= 0)) {

			errorMsg.append(CMISExportConstant.EXCEPTION_MESSAGE_CONSTANT_1);
			errorMsg.append("cmis.url.acl_service");
			errorMsg.append(CMISExportConstant.EXCEPTION_MESSAGE_CONSTANT_2);
			throw new DCMAApplicationException(errorMsg.toString());
		} else {
			// Make sure that it's tidy.
			aclURL = aclServiceURL.trim();

			// Determine if a part of the specified URL needs to be replaced with the server URL
			// configured in the batch classe.
			if (aclURL.contains(CMISExportConstant.SERVER_URL)) {
				aclURL = aclURL.replace(CMISExportConstant.SERVER_URL, serverURL);
			}
		}
		return aclServiceURL;
	}

	/**
	 * This method is used to discover the service url.
	 * @param serverURL {@link String}
	 * @param discoverServiceURL {@link String}
	 * @param errorMsg {@link StringBuffer}
	 * @return {@link String}
	 * @throws DCMAApplicationException if an exception occurs.
	 */
	public String createDiscoverServiceURL(String serverURL, String discoverServiceURL, StringBuffer errorMsg)
			throws DCMAApplicationException {
		String discoverURL;
		if ((discoverServiceURL == null) || (discoverServiceURL.trim().length() <= 0)) {
			errorMsg.setLength(0);
			errorMsg.append(CMISExportConstant.EXCEPTION_MESSAGE_CONSTANT_1);
			errorMsg.append("cmis.url.discovery_service");
			errorMsg.append(CMISExportConstant.EXCEPTION_MESSAGE_CONSTANT_2);
			throw new DCMAApplicationException(errorMsg.toString());
		} else {
			// Make sure that it's tidy.
			discoverURL = discoverServiceURL.trim();

			// Determine if a part of the specified URL needs to be replaced with the server URL
			// configured in the batch classe.
			if (discoverURL.contains(CMISExportConstant.SERVER_URL)) {
				discoverURL = discoverURL.replace(CMISExportConstant.SERVER_URL, serverURL);
			}
		}
		return discoverURL;
	}

	/**
	 * This method performs the function of creating the multi filling service url.
	 * @param serverURL {@link String}
	 * @param multifilingServiceURL {@link String}
	 * @param errorMsg {@link StringBuffer}
	 * @return {@link String}
	 * @throws DCMAApplicationException if an exception occurs.
	 */
	public String createMultiFilingServiceURL(String serverURL, String multifilingServiceURL, StringBuffer errorMsg)
			throws DCMAApplicationException {
		String multifilingURL;
		if ((multifilingServiceURL == null) || (multifilingServiceURL.trim().length() <= 0)) {
			errorMsg.setLength(0);
			errorMsg.append(CMISExportConstant.EXCEPTION_MESSAGE_CONSTANT_1);
			errorMsg.append("cmis.url.multifiling_service");
			errorMsg.append(CMISExportConstant.EXCEPTION_MESSAGE_CONSTANT_2);
			throw new DCMAApplicationException(errorMsg.toString());
		} else {
			// Make sure that it's tidy.
			multifilingURL = multifilingServiceURL.trim();

			// Determine if a part of the specified URL needs to be replaced with the server URL
			// configured in the batch classe.
			if (multifilingURL.contains(CMISExportConstant.SERVER_URL)) {
				multifilingURL = multifilingURL.replace(CMISExportConstant.SERVER_URL, serverURL);
			}
		}
		return multifilingURL;
	}

	/**
	 * This method is used to create the navigation url.
	 * @param serverURL {@link String}
	 * @param navigationServiceURL {@link String}
	 * @param errorMsg {@link StringBuffer}
	 * @return {@link String}
	 * @throws DCMAApplicationException if any exception occurs.
	 */
	public String createNavigationServiceURL(String serverURL, String navigationServiceURL, StringBuffer errorMsg)
			throws DCMAApplicationException {
		String navigationURL;
		if ((navigationServiceURL == null) || (navigationServiceURL.trim().length() <= 0)) {
			errorMsg.setLength(0);
			errorMsg.append(CMISExportConstant.EXCEPTION_MESSAGE_CONSTANT_1);
			errorMsg.append("cmis.url.navigation_service");
			errorMsg.append(CMISExportConstant.EXCEPTION_MESSAGE_CONSTANT_2);
			throw new DCMAApplicationException(errorMsg.toString()

			);
		} else {
			// Make sure that it's tidy.
			navigationURL = navigationServiceURL.trim();

			// Determine if a part of the specified URL needs to be replaced with the server URL
			// configured in the batch classe.
			if (navigationURL.contains(CMISExportConstant.SERVER_URL)) {
				navigationURL = navigationURL.replace(CMISExportConstant.SERVER_URL, serverURL);
			}
		}
		return navigationURL;
	}

	/**
	 * This method is used to create object service url.
	 * @param serverURL {@link String}
	 * @param objectServiceURL {@link String}
	 * @param errorMsg {@link StringBuffer}
	 * @return {@link String}
	 * @throws DCMAApplicationException if any exception occurs.
	 */
	public String createObjectServiceURL(String serverURL, String objectServiceURL, StringBuffer errorMsg)
			throws DCMAApplicationException {
		String objectURL;
		if ((objectServiceURL == null) || (objectServiceURL.trim().length() <= 0)) {
			errorMsg.setLength(0);
			errorMsg.append(CMISExportConstant.EXCEPTION_MESSAGE_CONSTANT_1);
			errorMsg.append("cmis.url.object_service");
			errorMsg.append(CMISExportConstant.EXCEPTION_MESSAGE_CONSTANT_2);
			throw new DCMAApplicationException(errorMsg.toString());
		} else {
			// Make sure that it's tidy.
			objectURL = objectServiceURL.trim();

			// Determine if a part of the specified URL needs to be replaced with the server URL
			// configured in the batch classe.
			if (objectURL.contains(CMISExportConstant.SERVER_URL)) {
				objectURL = objectURL.replace(CMISExportConstant.SERVER_URL, serverURL);
			}
		}
		return objectServiceURL;
	}

	/**
	 * This method is used to create policy service url.
	 * @param serverURL {@link String}
	 * @param policyServiceURL {@link String}
	 * @param errorMsg {@link StringBuffer}
	 * @return {@link String}
	 * @throws DCMAApplicationException if any exception occurs.
	 */
	public String createPolicyServiceURL(String serverURL, String policyServiceURL, StringBuffer errorMsg)
			throws DCMAApplicationException {
		String policyURL;
		if ((policyServiceURL == null) || (policyServiceURL.trim().length() <= 0)) {
			errorMsg.setLength(0);
			errorMsg.append(CMISExportConstant.EXCEPTION_MESSAGE_CONSTANT_1);
			errorMsg.append("cmis.url.policy_service");
			errorMsg.append(CMISExportConstant.EXCEPTION_MESSAGE_CONSTANT_2);
			throw new DCMAApplicationException(errorMsg.toString());
		} else {
			// Make sure that it's tidy.
			policyURL = policyServiceURL.trim();

			// Determine if a part of the specified URL needs to be replaced with the server URL
			// configured in the batch classe.
			if (policyURL.contains(CMISExportConstant.SERVER_URL)) {
				policyURL = policyURL.replace(CMISExportConstant.SERVER_URL, serverURL);
			}
		}
		return policyURL;
	}

	/**
	 * This method is used to create the relationship service url.
	 * @param serverURL {@link String}
	 * @param relationshipServiceURL {@link String}
	 * @param errorMsg {@link StringBuffer}
	 * @return {@link String}
	 * @throws DCMAApplicationException if any exception occurs.
	 */
	public String createRelationshipServiceURL(String serverURL, String relationshipServiceURL, StringBuffer errorMsg)
			throws DCMAApplicationException {
		String relationshipURL;
		if ((relationshipServiceURL == null) || (relationshipServiceURL.trim().length() <= 0)) {
			errorMsg.setLength(0);
			errorMsg.append(CMISExportConstant.EXCEPTION_MESSAGE_CONSTANT_1);
			errorMsg.append("cmis.url.relationship_service");
			errorMsg.append(CMISExportConstant.EXCEPTION_MESSAGE_CONSTANT_2);
			throw new DCMAApplicationException(errorMsg.toString());
		} else {
			// Make sure that it's tidy.
			relationshipURL = relationshipServiceURL.trim();

			// Determine if a part of the specified URL needs to be replaced with the server URL
			// configured in the batch classe.
			if (relationshipURL.contains(CMISExportConstant.SERVER_URL)) {
				relationshipURL = relationshipURL.replace(CMISExportConstant.SERVER_URL, serverURL);
			}
		}
		return relationshipURL;
	}

	/**
	 * This method is used to create the repository service url.
	 * @param serverURL {@link String}
	 * @param repositoryServiceURL {@link String}
	 * @param errorMsg {@link StringBuffer}
	 * @return {@link String}
	 * @throws DCMAApplicationException if any exception occurs.
	 */
	public String createRepositoryServiceURL(String serverURL, String repositoryServiceURL, StringBuffer errorMsg)
			throws DCMAApplicationException {
		String repositoryURL;
		if ((repositoryServiceURL == null) || (repositoryServiceURL.trim().length() <= 0)) {
			errorMsg.setLength(0);
			errorMsg.append(CMISExportConstant.EXCEPTION_MESSAGE_CONSTANT_1);
			errorMsg.append("cmis.url.repository_service");
			errorMsg.append(CMISExportConstant.EXCEPTION_MESSAGE_CONSTANT_2);
			throw new DCMAApplicationException(errorMsg.toString());
		} else {
			// Make sure that it's tidy.
			repositoryURL = repositoryServiceURL.trim();

			// Determine if a part of the specified URL needs to be replaced with the server URL
			// configured in the batch classe.
			if (repositoryURL.contains(CMISExportConstant.SERVER_URL)) {
				repositoryURL = repositoryURL.replace(CMISExportConstant.SERVER_URL, serverURL);
			}
		}
		return repositoryURL;
	}

	/**
	 * This method is used to create version service url.
	 * @param serverURL {@link String}
	 * @param serviceURL {@link String}
	 * @param errorMsg {@link StringBuffer}
	 * @return {@link String}
	 * @throws DCMAApplicationException if any exception occurs.
	 */
	public String createVersioningServiceURL(String serverURL, String serviceURL, StringBuffer errorMsg)
			throws DCMAApplicationException {
		String versioningServiceURL;
		if ((serviceURL == null) || (serviceURL.trim().length() <= 0)) {
			errorMsg.setLength(0);
			errorMsg.append(CMISExportConstant.EXCEPTION_MESSAGE_CONSTANT_1);
			errorMsg.append("cmis.url.versioning_service");
			errorMsg.append(CMISExportConstant.EXCEPTION_MESSAGE_CONSTANT_2);
			throw new DCMAApplicationException(errorMsg.toString());
		} else {
			// Make sure that it's tidy.
			versioningServiceURL = serviceURL.trim();

			// Determine if a part of the specified URL needs to be replaced with the server URL
			// configured in the batch classe.
			if (versioningServiceURL.contains(CMISExportConstant.SERVER_URL)) {
				versioningServiceURL = versioningServiceURL.replace(CMISExportConstant.SERVER_URL, serverURL);
			}
		}
		return versioningServiceURL;
	}
}
