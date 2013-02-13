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

package com.ephesoft.dcma.user.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ephesoft.dcma.batch.schema.UserInformation;
import com.ephesoft.dcma.user.connectivity.UserConnectivity;
import com.ephesoft.dcma.user.connectivity.constant.UserConnectivityConstant;
import com.ephesoft.dcma.user.connectivity.factory.UserConnectivityFactory;
import com.ephesoft.dcma.util.ApplicationConfigProperties;

/**
 * This class Implements the methood of the {@link UserConnectivityService}.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.user.connectivity.UserConnectivity
 */
@Service
public class UserConnectivityServiceImpl implements UserConnectivityService {

	/**
	 * USER_CONNECTIVITY_FACTORY_IS_NULL_HENCE_RETURNING String.
	 */
	private static final String USER_CONNECTIVITY_FACTORY_IS_NULL_HENCE_RETURNING = "User Connectivity Factory is null. Hence returning";

	/**
	 * USER_SUPER_ADMIN String.
	 */
	private static final String USER_SUPER_ADMIN = "user.super_admin";

	/**
	 * Used for handling logs.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(UserConnectivityServiceImpl.class);

	/**
	 * Instance of UserConnectivityFactory.
	 */
	@Autowired
	private UserConnectivityFactory factory;

	/**
	 * This method is used to return set of string of all the groups.
	 * 
	 * @return Set<{@link String}> if result is found else return null
	 */
	@Override
	public Set<String> getAllGroups() {
		LOG.info("Inside get all groups.");
		boolean isValid = true;
		Set<String> allGroups = null;
		Set<String> resultAllgroups = null;
		if (factory == null) {
			LOG.error(USER_CONNECTIVITY_FACTORY_IS_NULL_HENCE_RETURNING);
			isValid = false;
		}
		if (isValid) {
			UserConnectivity userConnectivity = factory.getImplementation();
			if (userConnectivity != null) {
				allGroups = userConnectivity.getAllGroups();
				resultAllgroups = performAddtionToResultSet(allGroups);
			}
		}
		LOG.info("Successfully returning groups.");
		return resultAllgroups;
	}

	private Set<String> performAddtionToResultSet(Set<String> dataSet) {
		Set<String> resultSet = new TreeSet<String>();
		if (dataSet != null) {
			for (String data : dataSet) {
				resultSet.add(data);
			}
		}
		return resultSet;
	}

	/**
	 * This method is used to return set of string of all the users.
	 * 
	 * @return Set<{@link String}> if result is found else return null
	 */
	@Override
	public Set<String> getAllUser() {
		LOG.info("Inside get all users.");
		Set<String> allUser = null;
		Set<String> resultAllUser = null;
		boolean isValid = true;
		if (factory == null) {
			LOG.error(USER_CONNECTIVITY_FACTORY_IS_NULL_HENCE_RETURNING);
			isValid = false;
		}
		if (isValid) {
			UserConnectivity userConnectivity = factory.getImplementation();
			if (userConnectivity != null) {
				allUser = userConnectivity.getAllUser();
				resultAllUser = performAddtionToResultSet(allUser);
			}
		}
		LOG.info("Successfully returning users.");
		return resultAllUser;
	}

	/**
	 * This method is used to return set of string of all the groups of a user.
	 * 
	 * @param userName String
	 * @return Set<{@link String}> if result is found else return null
	 */
	@Override
	public Set<String> getUserGroups(String userName) {
		LOG.info("Inside get all groups.");
		boolean isValid = true;
		Set<String> userGroups = null;
		Set<String> resultAllgroups = null;
		if (factory == null) {
			LOG.error(USER_CONNECTIVITY_FACTORY_IS_NULL_HENCE_RETURNING);
			isValid = false;
		}
		if (isValid) {
			UserConnectivity userConnectivity = factory.getImplementation();
			if (userConnectivity != null) {
				userGroups = userConnectivity.getUserGroups(userName);
				resultAllgroups = performAddtionToResultSet(userGroups);
			}
		}
		LOG.info("Successfully returning groups.");
		return resultAllgroups;
	}

	/**
	 * This method is used to return set of string of all super admin groups.
	 * 
	 * @return Set<{@link String}> if result is found else return null
	 */
	@Override
	public Set<String> getAllSuperAdminGroups() {
		LOG.info("Inside get all super admin groups.");
		Set<String> allSuperAdminGroups = null;
		Set<String> resultAllSuperAdminGroups = null;
		allSuperAdminGroups = getAllSuperAdminRoles();
		resultAllSuperAdminGroups = performAddtionToResultSet(allSuperAdminGroups);
		LOG.info("Successfully returning super admin groups.");
		return resultAllSuperAdminGroups;

	}

	/**
	 * This method is used to return set of string of all super admin roles.
	 * 
	 * @return Set<{@link String}> if result is found else return null
	 */
	public Set<String> getAllSuperAdminRoles() {
		String superAdminGroups = null;
		Set<String> allSuperAdminRoles = null;
		try {
			ApplicationConfigProperties applicationConfigProperties = ApplicationConfigProperties.getApplicationConfigProperties();
			superAdminGroups = applicationConfigProperties.getProperty(USER_SUPER_ADMIN);
			allSuperAdminRoles = getAllSuperAdminRoles(superAdminGroups);
		} catch (IOException e) {
			LOG.error("Error in fetching roles for super admin");
		}
		return allSuperAdminRoles;
	}

	/**
	 * This method is used to return set of string of all super admin roles.
	 * @param superAdminGroups String
	 * @return Set<{@link String}> if result is found else return null
	 */
	public Set<String> getAllSuperAdminRoles(String superAdminGroups) {
		Set<String> superAdminGroupSet = null;
		if (superAdminGroups != null && !superAdminGroups.isEmpty()) {
			String delimiter = UserConnectivityConstant.DOUBLE_SEMICOLON_DELIMITER;
			String[] superAdmins = superAdminGroups.split(delimiter);
			if (superAdmins != null) {
				superAdminGroupSet = new HashSet<String>();
				for (String superAdmin : superAdmins) {
					setSuperAdminGroupsSet(superAdminGroupSet, superAdmin);
				}
			}
		}
		return superAdminGroupSet;
	}

	private void setSuperAdminGroupsSet(Set<String> superAdminGroupSet, String superAdmin) {
		if (superAdmin != null && !superAdmin.isEmpty()) {
			superAdminGroupSet.add(superAdmin);
		}
	}

	/**
	 * This method is used to return add groups for user.
	 * 
	 * @param userInformation UserInformation
	 * @throws NamingException if error occurs
	 */
	@Override
	public void addGroup(UserInformation userInformation) throws NamingException {
		boolean isValid = true;
		if (factory == null) {
			LOG.error(USER_CONNECTIVITY_FACTORY_IS_NULL_HENCE_RETURNING);
			isValid = false;
		}
		if (isValid) {
			UserConnectivity userConnectivity = factory.getImplementation();
			if (userConnectivity != null) {
				userConnectivity.addGroup(userInformation);
			}
		}
	}

	/**
	 * This method is used to return set of string of all superAdmin groups.
	 * 
	 * @param userInformation UserInformation
	 * @throws NamingException if error occurs
	 */
	@Override
	public void addUser(UserInformation userInformation) throws NamingException {
		boolean isValid = true;
		if (factory == null) {
			LOG.error(USER_CONNECTIVITY_FACTORY_IS_NULL_HENCE_RETURNING);
			isValid = false;
		}
		if (isValid) {
			UserConnectivity userConnectivity = factory.getImplementation();
			if (userConnectivity != null) {
				userConnectivity.addUser(userInformation);
			}
		}
	}

	/**
	 * API check the user exist in the LDAP.
	 * 
	 * @param userName {@link String}
	 * @return boolean
	 */
	@Override
	public boolean checkUserExistence(String userName) {
		boolean isUserExist = false;
		boolean isValid = true;
		if (factory == null) {
			LOG.error(USER_CONNECTIVITY_FACTORY_IS_NULL_HENCE_RETURNING);
			isValid = false;
		}
		if (isValid) {
			UserConnectivity userConnectivity = factory.getImplementation();
			if (userConnectivity != null) {
				isUserExist = userConnectivity.checkUserExistence(userName);
			}
		}
		return isUserExist;
	}

	/**
	 * API delete the group name in the LDAP.
	 * 
	 * @param groupName {@link String}
	 */
	@Override
	public void deleteGroup(String groupName) {
		boolean isValid = true;
		if (factory == null) {
			LOG.error(USER_CONNECTIVITY_FACTORY_IS_NULL_HENCE_RETURNING);
			isValid = false;
		}
		if (isValid) {
			UserConnectivity userConnectivity = factory.getImplementation();
			if (userConnectivity != null) {
				userConnectivity.deleteGroup(groupName);
			}
		}
	}

	/**
	 * API delete the user name in the LDAP.
	 * 
	 * @param userName {@link String}
	 */
	@Override
	public void deleteUser(String userName) {
		boolean isValid = true;
		if (factory == null) {
			LOG.error(USER_CONNECTIVITY_FACTORY_IS_NULL_HENCE_RETURNING);
			isValid = false;
		}
		if (isValid) {
			UserConnectivity userConnectivity = factory.getImplementation();
			if (userConnectivity != null) {
				userConnectivity.deleteUser(userName);
			}
		}
	}

	/**
	 * API for updates the user password.
	 * 
	 * @param userName {@link String}
	 * @param newPassword {@link String}
	 */
	@Override
	public void modifyUserPassword(String userName, String newPassword) throws NamingException {
		boolean isValid = true;
		if (factory == null) {
			LOG.error(USER_CONNECTIVITY_FACTORY_IS_NULL_HENCE_RETURNING);
			isValid = false;
		}
		if (isValid) {
			UserConnectivity userConnectivity = factory.getImplementation();
			if (userConnectivity != null) {
				userConnectivity.modifyUserPassword(userName, newPassword);
			}
		}
	}

	/**
	 * API for verifying the user authentication and updates the user with new password.
	 * 
	 * @param userName {@link String}
	 * @param oldPassword {@link String}
	 * @param newPassword {@link String}
	 */
	@Override
	public void verifyandmodifyUserPassword(String userName, String oldPassword, String newPassword) throws NamingException {
		boolean isValid = true;
		if (factory == null) {
			LOG.error(USER_CONNECTIVITY_FACTORY_IS_NULL_HENCE_RETURNING);
			isValid = false;
		}
		if (isValid) {
			UserConnectivity userConnectivity = factory.getImplementation();
			if (userConnectivity != null) {
				userConnectivity.verifyandmodifyUserPassword(userName, oldPassword, newPassword);
			}
		}

	}
}
