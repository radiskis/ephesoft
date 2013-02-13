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

package com.ephesoft.dcma.user.connectivity.impl;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.schema.UserInformation;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.user.connectivity.UserConnectivity;
import com.ephesoft.dcma.user.connectivity.constant.UserConnectivityConstant;
import com.ephesoft.dcma.user.connectivity.exception.InvalidCredentials;

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
	 * ERROR_IN_CLOSING_LDAP_CONNECTION String.
	 */
	private static final String ERROR_IN_CLOSING_LDAP_CONNECTION = "Error in closing LDAP connection";

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
	 * This value get from the user.ldap_user_name in user-connectivity.properties file.
	 */
	private String ldapUserName;

	/**
	 * This value get from the user.ldap_password in user-connectivity.properties file.
	 */
	private String ldapPassword;

	/**
	 * This value get from the user.ldap_user_base in user-connectivity.properties file.
	 */
	private String userBasePath;

	/**
	 * This value get from the user.ldap_group_base in user-connectivity.properties file.
	 */
	private String groupBasePath;

	/**
	 * Object containing the LDAP Environment parameters.
	 */
	private Hashtable<Object, Object> env;// NO PMD. Required to be in this form only.

	/**
	 * To get Ldap URL.
	 * @return the ldapURL
	 */
	public String getLdapURL() {
		return ldapURL;
	}

	/**
	 * To set Ldap URL.
	 * @param ldapURL the ldapURL to set
	 */
	public void setLdapURL(final String ldapURL) {
		this.ldapURL = ldapURL;
	}

	/**
	 * To get Ldap Config.
	 * @return the ldapConfig
	 */
	public String getLdapConfig() {
		return ldapConfig;
	}

	/**
	 * To set Ldap Config.
	 * @param ldapConfig the ldapConfig to set
	 */
	public void setLdapConfig(final String ldapConfig) {
		this.ldapConfig = ldapConfig;
	}

	/**
	 * To get Ldap Domain Name.
	 * @return the ldapDomainName
	 */
	public String getLdapDomainName() {
		return ldapDomainName;
	}

	/**
	 * To set Ldap Domain Name.
	 * @param ldapDomainName the ldapDomainName to set
	 */
	public void setLdapDomainName(final String ldapDomainName) {
		this.ldapDomainName = ldapDomainName;
	}

	/**
	 * To get Ldap Domain Organization.
	 * @return the ldapDomainOrganization
	 */
	public String getLdapDomainOrganization() {
		return ldapDomainOrganization;
	}

	/**
	 * To set Ldap Domain Organization.
	 * @param ldapDomainOrganization the ldapDomainOrganization to set
	 */
	public void setLdapDomainOrganization(final String ldapDomainOrganization) {
		this.ldapDomainOrganization = ldapDomainOrganization;
	}

	/**
	 * To get Ldap password.
	 * @return the ldapPassword
	 */
	public String getLdapPassword() {
		return ldapPassword;
	}

	/**
	 * To get Ldap user name.
	 * @return the ldapUserName
	 */
	public String getLdapUserName() {
		return ldapUserName;
	}

	/**
	 * To set Ldap password.
	 * @param ldapPassword the ldapPassword to set
	 */
	public void setLdapPassword(final String ldapPassword) {
		this.ldapPassword = ldapPassword;
	}

	/**
	 * To set Ldap user name.
	 * @param ldapUserName the ldapUserName to set
	 */
	public void setLdapUserName(final String ldapUserName) {
		this.ldapUserName = ldapUserName;
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
			allGroups = this.fetchLDAPList(groupBasePath, UserConnectivityConstant.LDAP_GROUP_FILTER);
			LOG.info("Ending fetching list from LDAP");
		} catch (final Exception e) {
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

	private Set<String> fetchLDAPList(final String name, final String filterValue) {

		Set<String> resultList = null;
		boolean isValid = true;

		isValid = isValidData();

		if (isValid) {

			final Hashtable<Object, Object> env = getLDAPEnvironment();
			DirContext dctx = null;
			NamingEnumeration<?> results = null;
			try {
				dctx = new InitialDirContext(env);
			} catch (final NamingException ne) {
				LOG.error(ne.getMessage(), ne);
			} catch (final Exception e) {
				LOG.error(e.getMessage(), e);
			}

			if (null != dctx) {
				LOG.info("Start Fetching result set from LDAP Directory");
				results = getResultSet(name, dctx, filterValue);

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
				} catch (final NamingException ne) {
					LOG.error(ERROR_IN_CLOSING_LDAP_CONNECTION);
				} catch (final Exception e) {
					LOG.error(ERROR_IN_CLOSING_LDAP_CONNECTION);
				}
			} else {
				LOG.error("Invalid directory context of LDAP.");
			}
		}
		return resultList;
	}

	/**
	 * This method is used to get the result from the Ldap directory.
	 * 
	 * @param name the search directory name
	 * @param dctx directory context object
	 * @param filterValue filter value for fetching specific type of objects
	 * @return NamingEnumeration<?>
	 */
	private NamingEnumeration<?> getResultSet(final String name, final DirContext dctx, final String filterValue) {
		final SearchControls searchControl = new SearchControls();

		final String[] attributeFilter = {UserConnectivityConstant.COMMON_NAME};
		searchControl.setReturningAttributes(attributeFilter);
		searchControl.setSearchScope(SearchControls.SUBTREE_SCOPE);

		LOG.info("Added filter to LDAP :" + filterValue);
		NamingEnumeration<?> results = null;
		try {
			final String paramName = name + UserConnectivityConstant.COMMA_SYMBOL + UserConnectivityConstant.DOMAIN_COMPONENT
					+ UserConnectivityConstant.EQUAL_SYMBOL + ldapDomainName + UserConnectivityConstant.COMMA_SYMBOL
					+ UserConnectivityConstant.DOMAIN_COMPONENT + UserConnectivityConstant.EQUAL_SYMBOL + ldapDomainOrganization;
			LOG.info("Context Path for LDAP :" + paramName);
			results = dctx.search(paramName, filterValue, searchControl);
		} catch (final NamingException ne) {
			LOG.error(ne.getMessage(), ne);
		} catch (final Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return results;
	}

	/**
	 * This method manipulates the result into string to be added in the result set.
	 * 
	 * @param resultList Set<String>
	 * @param results NamingEnumeration<?>
	 */
	private void resultSetValues(final Set<String> resultList, final NamingEnumeration<?> results) {
		try {
			while (results.hasMore()) {
				SearchResult searchResult = null;
				try {
					searchResult = (SearchResult) results.next();
				} catch (final NamingException e) {
					LOG.error(e.getMessage(), e);
				}

				if (null != searchResult) {
					LOG.info("Group found of LDAP is :" + searchResult);
					final String searchResultString = searchResult.toString();
					final StringTokenizer stringTokenizer = new StringTokenizer(searchResultString,
							UserConnectivityConstant.COMMA_SYMBOL + UserConnectivityConstant.COLON_SYMBOL
									+ UserConnectivityConstant.EQUAL_SYMBOL);
					int entryIndex = UserConnectivityConstant.ZERO;
					while (stringTokenizer.hasMoreTokens()) {
						entryIndex++;
						final String nextToken = stringTokenizer.nextToken();
						if (entryIndex == UserConnectivityConstant.TWO && nextToken != null) {
							resultList.add(nextToken);
							break;
						}
					}
				} else {
					LOG.error("No groups found in the LDAP.");
				}
			}
		} catch (final NamingException ne) {
			LOG.error(ne.getMessage(), ne);
		}
	}

	/**
	 * Check the user-connectivity.properties are valid or not.
	 * 
	 * @return true if valid else false
	 */
	private boolean isValidData() {
		boolean check = true;
		if (null == ldapConfig || ldapConfig.isEmpty()) {
			LOG.error("ldapConfig not found.");
			if (check) {
				check = false;
			}
		}

		if (null == ldapURL || ldapURL.isEmpty()) {
			LOG.error("ldapUrl not found.");
			if (check) {
				check = false;
			}
		}

		if (null == ldapDomainName || ldapDomainName.isEmpty()) {
			LOG.error("ldapDomainName is null or empty.");
			if (check) {
				check = false;
			}
		}

		if (null == ldapDomainOrganization || ldapDomainOrganization.isEmpty()) {
			LOG.error("ldapDomainOrganization is null or empty.");
			if (check) {
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
			allUser = fetchLDAPList(userBasePath, UserConnectivityConstant.LDAP_USER_FILTER);
		} catch (final Exception e) {
			LOG.error("Error in fetching all users " + e.getMessage(), e);
		}
		return allUser;
	}

	/**
	 * This method is used to connect the LDAP Directory and fetching the list of user gropus object from the LDAP.
	 * 
	 * @return Set<String> if connected and users is found else return null
	 */
	@Override
	public Set<String> getUserGroups(final String userName) {
		throw new UnsupportedOperationException("Operation not supported in LDAP.");
	}

	/**
	 * This method is used to connect the LDAP Directory and agg group object to the LDAP.
	 * 
	 * @param userInfo {@link UserInformation}
	 * @throws NamingException if error occurs
	 */
	@Override
	public void addGroup(final UserInformation userInfo) throws NamingException {
		final Hashtable<Object, Object> env = getLDAPEnvironment();

		DirContext dctx = null;
		try {
			dctx = new InitialDirContext(env);

			// Create a container set of attributes
			final Attributes container = new BasicAttributes();

			// Create the objectclass to add
			final Attribute objClasses = new BasicAttribute(UserConnectivityConstant.OBJECT_CLASS);
			objClasses.add(UserConnectivityConstant.GROUP_OF_UNIQUE_NAMES);

			final String commonNameValue = userInfo.getCompanyName() + ICommonConstants.UNDERSCORE + userInfo.getEmail();

			// Assign the name and description to the group
			final Attribute commonName = new BasicAttribute(UserConnectivityConstant.COMMON_NAME, commonNameValue);
			final Attribute desc = new BasicAttribute(UserConnectivityConstant.DESCRIPTION, userInfo.getCompanyName());
			final Attribute uniqueMember = new BasicAttribute(UserConnectivityConstant.UNIQUE_MEMBER, getUserDN(userInfo.getEmail()));

			// Add these to the container
			container.put(objClasses);
			container.put(commonName);
			container.put(desc);
			container.put(uniqueMember);

			// Create the entry
			dctx.createSubcontext(getGroupDN(commonNameValue), container);
		} finally {
			if (null != dctx) {
				try {
					dctx.close();
				} catch (final NamingException e) {
					LOG.error(ERROR_IN_CLOSING_LDAP_CONNECTION);
				}
			}
		}
	}

	/**
	 * This method is used to connect the LDAP Directory and agg user object to the LDAP.
	 * 
	 * @param userInfo {@link UserInformation}
	 * @throws NamingException if error occurs
	 */
	@Override
	public void addUser(final UserInformation userInformation) throws NamingException {
		final Hashtable<Object, Object> env = getLDAPEnvironment();
		DirContext dctx = null;
		try {
			dctx = new InitialDirContext(env);
			// Create a container set of attributes
			final Attributes container = new BasicAttributes();

			// Create the objectclass to add
			final Attribute objClasses = new BasicAttribute(UserConnectivityConstant.OBJECT_CLASS);
			objClasses.add(UserConnectivityConstant.INET_ORG_PERSON);
			objClasses.add(UserConnectivityConstant.OBJECT_CLASS_PERSON);

			// Assign the username, first name, and last name
			final Attribute commonName = new BasicAttribute(UserConnectivityConstant.COMMON_NAME, userInformation.getEmail());
			final Attribute email = new BasicAttribute(UserConnectivityConstant.MAIL, userInformation.getEmail());
			final Attribute givenName = new BasicAttribute(UserConnectivityConstant.GIVEN_NAME, userInformation.getFirstName());
			final Attribute uid = new BasicAttribute(UserConnectivityConstant.UID, userInformation.getEmail());
			final Attribute surName = new BasicAttribute(UserConnectivityConstant.SUR_NAME, userInformation.getLastName());

			// Add password
			final Attribute userPassword = new BasicAttribute(UserConnectivityConstant.USER_PASSWORD, userInformation.getPassword());

			// Add these to the container
			container.put(objClasses);
			container.put(commonName);
			container.put(givenName);
			container.put(email);
			container.put(uid);
			container.put(surName);
			container.put(userPassword);

			// Create the entry
			dctx.createSubcontext(getUserDN(userInformation.getEmail()), container);
		} finally {
			if (null != dctx) {
				try {
					dctx.close();
				} catch (final NamingException e) {
					LOG.error(ERROR_IN_CLOSING_LDAP_CONNECTION);
				}
			}
		}
	}

	private String getUserDN(final String userName) {
		return new StringBuffer().append(UserConnectivityConstant.COMMON_NAME).append(UserConnectivityConstant.EQUAL_SYMBOL).append(
				userName).append(UserConnectivityConstant.COMMA_SYMBOL).append(userBasePath).append(
				UserConnectivityConstant.COMMA_SYMBOL).append(UserConnectivityConstant.DOMAIN_COMPONENT).append(
				UserConnectivityConstant.EQUAL_SYMBOL).append(ldapDomainName).append(UserConnectivityConstant.COMMA_SYMBOL).append(
				UserConnectivityConstant.DOMAIN_COMPONENT).append(UserConnectivityConstant.EQUAL_SYMBOL)
				.append(ldapDomainOrganization).toString();
	}

	private String getGroupDN(final String userName) {
		return new StringBuffer().append(UserConnectivityConstant.COMMON_NAME).append(UserConnectivityConstant.EQUAL_SYMBOL).append(
				userName).append(UserConnectivityConstant.COMMA_SYMBOL).append(groupBasePath).append(
				UserConnectivityConstant.COMMA_SYMBOL).append(UserConnectivityConstant.DOMAIN_COMPONENT).append(
				UserConnectivityConstant.EQUAL_SYMBOL).append(ldapDomainName).append(UserConnectivityConstant.COMMA_SYMBOL).append(
				UserConnectivityConstant.DOMAIN_COMPONENT).append(UserConnectivityConstant.EQUAL_SYMBOL)
				.append(ldapDomainOrganization).toString();
	}

	/**
	 * To check existence of user.
	 * @param userName String
	 * @return boolean
	 */
	@Override
	public boolean checkUserExistence(final String userName) {
		boolean isUserExist = false;
		final Hashtable<Object, Object> env = getLDAPEnvironment();

		DirContext dctx = null;
		try {
			dctx = new InitialDirContext(env);
			final NamingEnumeration<SearchResult> namingEnumeration = dctx.search(getUserDN(userName), null);
			if (namingEnumeration != null) {
				isUserExist = true;
			}
		} catch (final NamingException e) {
			LOG.info("User is not found in ldap for :" + userName);
		} finally {
			if (null != dctx) {
				try {
					dctx.close();
				} catch (final NamingException e) {
					LOG.error(ERROR_IN_CLOSING_LDAP_CONNECTION);
				}
			}
		}
		return isUserExist;
	}

	/**
	 * To delete group.
	 * @param groupName String
	 */
	@Override
	public void deleteGroup(final String groupName) {
		final Hashtable<Object, Object> env = getLDAPEnvironment();

		DirContext dctx = null;
		try {
			dctx = new InitialDirContext(env);
			dctx.destroySubcontext(getGroupDN(groupName));
		} catch (final NameNotFoundException e) {
			LOG.error("User doesn't exist in LDAP for deletion");
		} catch (final NamingException e) {
			LOG.error("Unable to create connection with LDAP :" + e.getMessage(), e);
		} finally {
			if (null != dctx) {
				try {
					dctx.close();
				} catch (final NamingException e) {
					LOG.error(ERROR_IN_CLOSING_LDAP_CONNECTION);
				}
			}
		}
	}

	/**
	 * To delete user.
	 * @param userName String
	 */
	@Override
	public void deleteUser(final String userName) {
		final Hashtable<Object, Object> env = getLDAPEnvironment();

		DirContext dctx = null;
		try {
			dctx = new InitialDirContext(env);
			dctx.destroySubcontext(getUserDN(userName));
		} catch (final NameNotFoundException e) {
			LOG.error("User doesn't exist in LDAP for deletion");
		} catch (final NamingException e) {
			LOG.error("Unable to create connection with LDAP :" + e.getMessage(), e);
		} finally {
			if (null != dctx) {
				try {
					dctx.close();
				} catch (final NamingException e) {
					LOG.error(ERROR_IN_CLOSING_LDAP_CONNECTION);
				}
			}
		}
	}

	/**
	 * To verify and modify user password.
	 * @param userName String
	 * @param oldPassword String
	 * @param newPassword String
	 * @throws NamingException if error occurs
	 */
	@Override
	public void verifyandmodifyUserPassword(final String userName, final String oldPassword, final String newPassword)
			throws NamingException {
		final Hashtable<Object, Object> env = getLDAPEnvironment();

		DirContext dctx = null;
		try {
			dctx = new InitialDirContext(env);

			final Hashtable<Object, Object> env1 = new Hashtable<Object, Object>(); // NOPMD. Required to work with Hashtable for LDAP.
			env1.put(Context.INITIAL_CONTEXT_FACTORY, ldapConfig);
			env1.put(Context.PROVIDER_URL, ldapURL);
			env1.put(Context.SECURITY_PRINCIPAL, getUserDN(userName));
			env1.put(Context.SECURITY_CREDENTIALS, ((oldPassword == null) ? UserConnectivityConstant.EMPTY : oldPassword));
			boolean isExist = true;
			DirContext localDctx = null;
			try {
				localDctx = new InitialDirContext(env1);
			} catch (final NamingException e) {
				isExist = false;
				throw new InvalidCredentials("Invalid username and password provided for verification", e);
			} finally {
				if (null != localDctx) {
					try {
						localDctx.close();
					} catch (final NamingException e) {
						LOG.error(ERROR_IN_CLOSING_LDAP_CONNECTION);
					}
				}
			}
			if (isExist) {
				final ModificationItem[] modificationItem = new ModificationItem[1];
				final Attribute attribute = new BasicAttribute(UserConnectivityConstant.USER_PASSWORD, newPassword);

				modificationItem[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attribute);

				dctx.modifyAttributes(getUserDN(userName), modificationItem);
			}
		} finally {
			if (null != dctx) {
				try {
					dctx.close();
				} catch (final NamingException e) {
					LOG.error(ERROR_IN_CLOSING_LDAP_CONNECTION);
				}
			}
		}
	}

	/**
	 * To modify user password.
	 * @param userName String
	 * @param newPassword String
	 * @throws NamingException if error occurs
	 */
	@Override
	public void modifyUserPassword(final String userName, final String newPassword) throws NamingException {
		final Hashtable<Object, Object> env = getLDAPEnvironment();

		DirContext dctx = null;
		try {
			dctx = new InitialDirContext(env);

			final ModificationItem[] modificationItem = new ModificationItem[1];
			final Attribute attribute = new BasicAttribute(UserConnectivityConstant.USER_PASSWORD, newPassword);

			modificationItem[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attribute);

			dctx.modifyAttributes(getUserDN(userName), modificationItem);
		} finally {
			if (null != dctx) {
				try {
					dctx.close();
				} catch (final NamingException e) {
					LOG.error(ERROR_IN_CLOSING_LDAP_CONNECTION);
				}
			}
		}
	}

	/**
	 * To get User Base Path.
	 * @return the userBasePath
	 */
	public String getUserBasePath() {
		return userBasePath;
	}

	/**
	 * To set User Base Path.
	 * @param userBasePath 
	 */
	public void setUserBasePath(final String userBasePath) {
		this.userBasePath = userBasePath;
	}

	/**
	 * To get Group Base Path.
	 * @return the groupBasePath
	 */
	public String getGroupBasePath() {
		return groupBasePath;
	}

	/**
	 * To set Group Base Path.
	 * @param groupBasePath 
	 */
	public void setGroupBasePath(final String groupBasePath) {
		this.groupBasePath = groupBasePath;
	}

	/**
	 * API to fetch the configured LDAP environment parameters.
	 * 
	 * @return {@link Hashtable}<{@link Object}, {@link Object}>
	 */
	private Hashtable<Object, Object> getLDAPEnvironment() {
		if (env == null) {
			env = new Hashtable<Object, Object>();
			prepareLDAPEnvironment();
		}
		return env;
	}

	/**
	 * API to load the configured LDAP environment parameters into the environment object.
	 * 
	 * @return {@link Hashtable}<{@link Object}, {@link Object}>
	 */
	private void prepareLDAPEnvironment() {
		env.put(Context.INITIAL_CONTEXT_FACTORY, ldapConfig);
		env.put(Context.PROVIDER_URL, ldapURL);
		env.put(Context.SECURITY_PRINCIPAL, ldapUserName);
		env.put(Context.SECURITY_CREDENTIALS, ((ldapPassword == null) ? "" : ldapPassword));
	}
}
