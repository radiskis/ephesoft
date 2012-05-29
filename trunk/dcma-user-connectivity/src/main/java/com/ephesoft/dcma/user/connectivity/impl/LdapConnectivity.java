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

package com.ephesoft.dcma.user.connectivity.impl;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.user.connectivity.UserConnectivity;
import com.ephesoft.dcma.user.connectivity.constant.UserConnectivityConstant;

/**
 * This class connect to the LDAP server and fetching the result from the LDAP directory server.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.user.connectivity.UserConnectivity
 * 
 */
public class LdapConnectivity implements UserConnectivity {

	/**
	 * Used for handling logs.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(LdapConnectivity.class);

	/**
	 * This value get from the user.ldap_url in user-connectivity.properties file.
	 */
	private String ldapURL;

	/**
	 * This value get from the user.ldap_config in user-connectivity.properties file.
	 */
	private String ldapConfig;

	/**
	 * This value get from the user.ldap_domain_component_name in user-connectivity.properties file.
	 */
	private String ldapDomainName;

	/**
	 * This value get from the user.ldap_domain_component_organization in user-connectivity.properties file.
	 */
	private String ldapDomainOrganization;

	/**
	 * @return the ldapURL
	 */
	public String getLdapURL() {
		return ldapURL;
	}

	/**
	 * @param ldapURL the ldapURL to set
	 */
	public void setLdapURL(final String ldapURL) {
		this.ldapURL = ldapURL;
	}

	/**
	 * @return the ldapConfig
	 */
	public String getLdapConfig() {
		return ldapConfig;
	}

	/**
	 * @param ldapConfig the ldapConfig to set
	 */
	public void setLdapConfig(final String ldapConfig) {
		this.ldapConfig = ldapConfig;
	}

	/**
	 * @return the ldapDomainName
	 */
	public String getLdapDomainName() {
		return ldapDomainName;
	}

	/**
	 * @param ldapDomainName the ldapDomainName to set
	 */
	public void setLdapDomainName(final String ldapDomainName) {
		this.ldapDomainName = ldapDomainName;
	}

	/**
	 * @return the ldapDomainOrganization
	 */
	public String getLdapDomainOrganization() {
		return ldapDomainOrganization;
	}

	/**
	 * @param ldapDomainOrganization the ldapDomainOrganization to set
	 */
	public void setLdapDomainOrganization(final String ldapDomainOrganization) {
		this.ldapDomainOrganization = ldapDomainOrganization;
	}

	/**
	 * This method is used to connect the LDAP Directory and fetching the list of groups object from the LDAP.
	 * 
	 * @return Set<String> if connected and groups is found else return null
	 */
	@Override
	public Set<String> getAllGroups() {
		Set<String> allGroups = null;
		try {
			LOG.info("======Inside LDAPUserConnectivity======");
			LOG.info("Fetching all available groups from the LDAP");
			allGroups = this.fetchLDAPList(UserConnectivityConstant.LDAP_GROUPS);
			LOG.info("Ending fetching list from LDAP");
		} catch (Exception e) {
			LOG.error("Error in fetching all groups " + e.getMessage(), e);
		}
		return allGroups;
	}

	/**
	 * This method is used to connect to the LDAP directory and used to return the Set of string of result fetch in accordance of the
	 * argument passed to it.
	 * 
	 * @param {@link String}
	 * @return Set<String> if connected and result is found else return null
	 */
	private Set<String> fetchLDAPList(final String name) {

		Set<String> resultList = null;
		boolean isValid = true;

		isValid = isValidData();

		if (isValid) {

			Hashtable<Object, Object> env = new Hashtable<Object, Object>(); // NOPMD. Required to work with Hashtable for LDAP.
			env.put(Context.INITIAL_CONTEXT_FACTORY, ldapConfig);
			env.put(Context.PROVIDER_URL, ldapURL);

			DirContext dctx = null;
			NamingEnumeration<?> results = null;
			try {
				dctx = new InitialDirContext(env);
			} catch (NamingException ne) {
				LOG.error(ne.getMessage(), ne);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}

			if (dctx != null) {
				LOG.info("Start Fetching result set from Active Directory");
				results = getResultSet(name, dctx);

				if (results != null) {
					resultList = new HashSet<String>();
					resultSetValues(resultList, results);
				} else {
					LOG.error("Results found from LDAP is  null or empty.");
				}

				try {
					if (dctx != null) {
						LOG.info("Closing directory context of LDAP");
						dctx.close();
					}
					if (results != null) {
						LOG.info("Closing result set of LDAP");
						results.close();
					}
				} catch (NamingException ne) {
					LOG.error(ne.getMessage(), ne);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			} else {
				LOG.error("Invalid directory context of LDAP.");
			}
		}
		return resultList;
	}

	/**
	 * This method is used to get the result from the Ldap directory
	 * 
	 * @param name
	 * @param dctx
	 * @return
	 */
	private NamingEnumeration<?> getResultSet(final String name, DirContext dctx) {
		SearchControls searchControl = new SearchControls();
		String[] attributeFilter = {UserConnectivityConstant.COMMON_NAME};
		searchControl.setReturningAttributes(attributeFilter);
		searchControl.setSearchScope(SearchControls.SUBTREE_SCOPE);

		String filter = UserConnectivityConstant.LDAP_FILTER;
		LOG.info("Added filter to LDAP :" + filter);
		NamingEnumeration<?> results = null;
		try {
			String paramName = UserConnectivityConstant.ORGANIZATIONAL_UNIT + UserConnectivityConstant.EQUAL_SYMBOL + name
					+ UserConnectivityConstant.COMMA_SYMBOL + UserConnectivityConstant.DOMAIN_COMPONENT
					+ UserConnectivityConstant.EQUAL_SYMBOL + ldapDomainName + UserConnectivityConstant.COMMA_SYMBOL
					+ UserConnectivityConstant.DOMAIN_COMPONENT + UserConnectivityConstant.EQUAL_SYMBOL + ldapDomainOrganization;
			LOG.info("Context Path for LDAP :" + paramName);
			results = dctx.search(paramName, filter, searchControl);
		} catch (NamingException ne) {
			LOG.error(ne.getMessage(), ne);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return results;
	}

	/**
	 * This method manipulates the result into string to be added in the resultset
	 * 
	 * @param resultList
	 * @param results
	 */
	private void resultSetValues(Set<String> resultList, NamingEnumeration<?> results) {
		try {
			while (results.hasMore()) {
				SearchResult searchResult = null;
				try {
					searchResult = (SearchResult) results.next();
				} catch (NamingException e) {
					LOG.error(e.getMessage(), e);
				}

				if (null != searchResult) {
					LOG.info("Group found of LDAP is :" + searchResult);
					String result = searchResult.toString().split(UserConnectivityConstant.COLON_SYMBOL)[0];
					if (null != result && !result.isEmpty()) {
						String[] cnConnectionNameArr = result.split(UserConnectivityConstant.COLON_SYMBOL);
						if (cnConnectionNameArr != null && cnConnectionNameArr.length >= 1) {
							String userName = cnConnectionNameArr[0];
							String[] userNameArr = userName.split(UserConnectivityConstant.EQUAL_SYMBOL);
							if (userNameArr != null && userNameArr.length >= 2) {
								resultList.add(userNameArr[1]);
							}
						}
					}
				} else {
					LOG.error("No groups found in the LDAP.");
				}
			}
		} catch (NamingException ne) {
			LOG.error(ne.getMessage(), ne);
		}
	}

	/**
	 * Check the user-connectivity.properties are valid or not
	 * 
	 * @param isValid
	 * @return true if valid else false
	 */
	private boolean isValidData() {
		boolean check = true; 
		if (null == ldapConfig || ldapConfig.isEmpty()) {
			LOG.error("ldapConfig not found.");
			if(check){
				check = false;
			}
		}

		if (null == ldapURL || ldapURL.isEmpty()) {
			LOG.error("ldapUrl not found.");
			if(check){
				check = false;
			}
		}

		if (null == ldapDomainName || ldapDomainName.isEmpty()) {
			LOG.error("ldapDomainName is null or empty.");
			if(check){
				check = false;
			}
		}

		if (null == ldapDomainOrganization || ldapDomainOrganization.isEmpty()) {
			LOG.error("ldapDomainOrganization is null or empty.");
			if(check){
				check = false;
			}
		}
		return check;
	}

	/**
	 * This method is used to connect the LDAP Directory and fetching the list of users object from the LDAP.
	 * 
	 * @return Set<String> if connected and users is found else return null
	 */
	@Override
	public Set<String> getAllUser() {
		Set<String> allUser = null;
		try {
			allUser = fetchLDAPList(UserConnectivityConstant.LDAP_PEOPLE);
		} catch (Exception e) {
			LOG.error("Error in fetching all users " + e.getMessage(), e);
		}
		return allUser;
	}

	@Override
	public Set<String> getUserGroups(String userName) {
		// TODO Auto-generated method stub
		return null;
	}

}
