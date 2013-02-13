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

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.naming.NamingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ephesoft.dcma.batch.schema.UserInformation;
import com.ephesoft.dcma.user.connectivity.UserConnectivity;
import com.ephesoft.dcma.user.connectivity.constant.UserConnectivityConstant;

/**
 * This class connect to the Tomcat server and fetching the result from the Tomcat server.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.user.connectivity.UserConnectivity
 * 
 */
public class TomcatConnectivity implements UserConnectivity {

	/**
	 * OPERATION_NOT_SUPPORTED_IN_TOMCAT_CONNECTIVITY String.
	 */
	private static final String OPERATION_NOT_SUPPORTED_IN_TOMCAT_CONNECTIVITY = "Operation not supported in Tomcat connectivity.";

	/**
	 * Used for handling logs.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(TomcatConnectivity.class);

	/**
	 * tomcatUserXmlPath String.
	 */
	private String tomcatUserXmlPath;

	/**
	 * To get Tomcat User Xml Path.
	 * @return the tomcatUserXmlPath
	 */
	public String getTomcatUserXmlPath() {
		return tomcatUserXmlPath;
	}

	/**
	 * To set Tomcat User Xml Path.
	 * @param tomcatUserXmlPath String
	 */
	public void setTomcatUserXmlPath(String tomcatUserXmlPath) {
		this.tomcatUserXmlPath = tomcatUserXmlPath;
	}

	/**
	 * This method returns DOM document from the XML file loaded for tomcatUserXmlPath.
	 * 
	 * @param tomcatUserXmlPath String
	 * @return {@link Document}
	 */
	private Document getDocument(String tomcatUserXmlPath) {
		Document document = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			LOG.error(e.getMessage(), e);
		}
		try {
			if (builder != null) {
				document = builder.parse(tomcatUserXmlPath);
			}
		} catch (SAXException e) {
			LOG.error(e.getMessage(), e);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		return document;
	}

	/**
	 * This method is used to connect the Tomcat Server and fetching the list of group object from the Tomcat Server.
	 * 
	 * @return Set<String> if connected and group is found else return null
	 */
	@Override
	public Set<String> getAllGroups() {
		Set<String> allGroups = new HashSet<String>();
		Document document = getDocument(tomcatUserXmlPath);
		NodeList roleList = document.getElementsByTagName(UserConnectivityConstant.TOMCAT_ROLE);
		if (roleList != null & roleList.getLength() > UserConnectivityConstant.ZERO) {
			for (int index = UserConnectivityConstant.ZERO; index < roleList.getLength(); index++) {
				Element role = (Element) roleList.item(index);
				String roleAttribute = role.getAttribute(UserConnectivityConstant.TOMCAT_ROLENAME);
				setUserGroupsSet(allGroups, roleAttribute);
			}
		}
		return allGroups;
	}

	/**
	 * This method is used to connect the Tomcat Server and fetching the list of user object from the Tomcat Server.
	 * 
	 * @return Set<String> if connected and user is found else return null
	 */
	@Override
	public Set<String> getAllUser() {
		Set<String> allUsers = new HashSet<String>();
		Document document = getDocument(tomcatUserXmlPath);
		NodeList roleList = document.getElementsByTagName(UserConnectivityConstant.TOMCAT_USER);
		if (roleList != null & roleList.getLength() > UserConnectivityConstant.ZERO) {
			for (int index = UserConnectivityConstant.ZERO; index < roleList.getLength(); index++) {
				Element role = (Element) roleList.item(index);
				String userAttribute = role.getAttribute(UserConnectivityConstant.TOMCAT_USERNAME);
				setUserGroupsSet(allUsers, userAttribute);
			}
		}
		return allUsers;
	}

	/**
	 * This method is used to connect the Tomcat Server and fetching the list of user group object from the Tomcat Server.
	 * @param userName String
	 * @return Set<String> if connected and user is found else return null
	 */
	@Override
	public Set<String> getUserGroups(String userName) {
		Set<String> userGroupsSet = new HashSet<String>();
		Document document = getDocument(tomcatUserXmlPath);
		NodeList roleList = document.getElementsByTagName(UserConnectivityConstant.TOMCAT_USER);
		if (roleList != null & roleList.getLength() > UserConnectivityConstant.ZERO) {
			for (int index = UserConnectivityConstant.ZERO; index < roleList.getLength(); index++) {
				Element user = (Element) roleList.item(index);
				String userAttribute = user.getAttribute(UserConnectivityConstant.TOMCAT_USERNAME);
				if (userAttribute.equals(userName)) {
					String userRoles = user.getAttribute(UserConnectivityConstant.ROLES);
					String[] userRolesArray = userRoles.split(UserConnectivityConstant.COMMA_SYMBOL);
					for (String userRole : userRolesArray) {
						setUserGroupsSet(userGroupsSet, userRole);
					}
				}
			}
		}
		return userGroupsSet;
	}

	private void setUserGroupsSet(Set<String> userGroupsSet, String userRole) {
		if (userRole != null && !userRole.isEmpty()) {
			userGroupsSet.add(userRole);
		}
	}

	/**
	 * To add group.
	 * @param userInformation UserInformation
	 */
	@Override
	public void addGroup(UserInformation userInformation) {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED_IN_TOMCAT_CONNECTIVITY);
	}

	/**
	 * To add user.
	 * @param userInformation UserInformation
	 */
	@Override
	public void addUser(UserInformation userInformation) {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED_IN_TOMCAT_CONNECTIVITY);
	}

	/**
	 * To check existence of user.
	 * @param userName String
	 * @return boolean
	 */
	@Override
	public boolean checkUserExistence(String userName) {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED_IN_TOMCAT_CONNECTIVITY);
	}

	/**
	 * To delete group.
	 * @param groupName String
	 */
	@Override
	public void deleteGroup(String groupName) {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED_IN_TOMCAT_CONNECTIVITY);
	}

	/**
	 * To delete user.
	 * @param userName String
	 */
	@Override
	public void deleteUser(String userName) {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED_IN_TOMCAT_CONNECTIVITY);
	}

	/**
	 * To modify user password.
	 * @param userName String
	 * @param newPassword String
	 * @throws NamingException if error occurs
	 */
	@Override
	public void modifyUserPassword(String userName, String newPassword) throws NamingException {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED_IN_TOMCAT_CONNECTIVITY);
	}

	/**
	 * To verify and modify user password.
	 * @param userName String
	 * @param oldPassword String
	 * @param newPassword String
	 * @throws NamingException if error occurs
	 */
	@Override
	public void verifyandmodifyUserPassword(String userName, String oldPassword, String newPassword) throws NamingException {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED_IN_TOMCAT_CONNECTIVITY);
	}
}
