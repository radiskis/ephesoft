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

package com.ephesoft.dcma.user.connectivity;

import java.util.Set;

import javax.naming.NamingException;
import com.ephesoft.dcma.batch.schema.UserInformation;

/**
 * This class connect to the user authentication based server and fetching the Set of groups or User from that server.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.batch.schema.UserInformation
 */

public interface UserConnectivity {

	/**
	 * This method is used to return set of string of all the groups.
	 * 
	 * @return Set<String> if result is found else return null
	 */
	Set<String> getAllGroups();

	/**
	 * This method is used to return set of string of all the users.
	 * 
	 * @return Set<String> if result is found else return null
	 */
	Set<String> getAllUser();

	/**
	 * This method is used to return set of string of all superAdmin groups.
	 * 
	 * @param userName String
	 * @return Set<String> if result is found else return null
	 */

	Set<String> getUserGroups(String userName);

	/**
	 * This method is used to return set of string of all superAdmin groups.
	 * 
	 * @param userInformation UserInformation
	 * @return Set<String> if result is found else return null
	 * @throws NamingException
	 */

	void addUser(UserInformation userInformation) throws NamingException;

	/**
	 * This method is used to return add groups for user.
	 *
	 * @param userInformation UserInformation
	 * @return Set<String> if result is found else return null
	 * @throws NamingException
	 */

	void addGroup(UserInformation userInformation) throws NamingException;

	/**
	 * API check the user exist in the LDAP.
	 * 
	 * @param userName {@link String}
	 * @return boolean
	 */
	boolean checkUserExistence(String userName);

	/**
	 * API delete the group name in the LDAP.
	 * 
	 * @param groupName {@link String}
	 */
	void deleteGroup(String groupName);

	/**
	 * API delete the user name in the LDAP.
	 * 
	 * @param userName {@link String}
	 */
	void deleteUser(String userName);

	/**
	 * API for verifying the user authentication and updates the user with new password.
	 * 
	 * @param userName {@link String}
	 * @param oldPassword {@link String}
	 * @param newPassword {@link String}
	 */
	void verifyandmodifyUserPassword(String userName, String oldPassword, String newPassword) throws NamingException;

	/**
	 * API for updates the user password.
	 * 
	 * @param userName {@link String}
	 * @param newPassword {@link String}
	 */
	void modifyUserPassword(String userName, String newPassword) throws NamingException;
}
