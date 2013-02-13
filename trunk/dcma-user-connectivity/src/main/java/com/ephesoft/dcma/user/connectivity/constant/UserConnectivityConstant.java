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

package com.ephesoft.dcma.user.connectivity.constant;

/**
 * This is a common constants file for User Connectivity plugin.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public interface UserConnectivityConstant {

	/**
	 * Variable to double semicolon delimiter.
	 */
	String DOUBLE_SEMICOLON_DELIMITER = ";;";

	/**
	 * Variable for tomcat role.
	 */
	String TOMCAT_ROLE = "role";

	/**
	 * Variable for tomcat rolename.
	 */
	String TOMCAT_ROLENAME = "rolename";

	/**
	 * Variable for tomcat user.
	 */
	String TOMCAT_USER = "user";

	/**
	 * Variable for tomcat username.
	 */
	String TOMCAT_USERNAME = "username";

	/**
	 * Variable for equal symbol.
	 */
	String EQUAL_SYMBOL = "=";

	/**
	 * Variable for colon symbol.
	 */
	String COLON_SYMBOL = ":";

	/**
	 * Variable for comma symbol.
	 */
	String COMMA_SYMBOL = ",";

	/**
	 * Variable for ldap people.
	 */
	String LDAP_PEOPLE = "people";

	/**
	 * Variable for ldap people.
	 */
	String LDAP_GROUPS = "groups";

	/**
	 * Variable for organizational unit.
	 */
	String ORGANIZATIONAL_UNIT = "ou";

	/**
	 * Variable for domain component.
	 */
	String DOMAIN_COMPONENT = "dc";

	/**
	 * Variable for common name.
	 */
	String COMMON_NAME = "cn";

	/**
	 * Variable for ms active directory group.
	 */
	String MSACTIVEDIRECTORY_GROUP = "group";

	/**
	 * Variable for ms active directory user.
	 */
	String MSACTIVEDIRECTORY_USER = "User";

	/**
	 * Variable for ldap filter.
	 */
	String LDAP_FILTER = "(objectClass=*)";
	
	/**
	 * Variable for ldap user filter.
	 */
	String LDAP_USER_FILTER = "(objectClass=Person)";
	
	/**
	 * Variable for ldap group filter.
	 */
	String LDAP_GROUP_FILTER = "(|((objectClass=organizationalRole)(objectClass=groupOfNames)(objectClass=groupOfUniqueNames)))";

	/**
	 * Variable for ms active directory start filter.
	 */
	String MSACTIVEDIRECTORY_START_FILTER = "(";
	
	/**
	 * Variable for object class name.
	 */
	String OBJECT_CLASS = "objectClass";
	
	/**
	 * Variable for ms active directory equal symbol.
	 */
	String MSACTIVEDIRECTORY_EQUAL_SYMBOL = "=";

	/**
	 * Variable for ms active directory end filter.
	 */
	String MSACTIVEDIRECTORY_END_FILTER = ")";

	/**
	 * Variable for ms active directory ampersand symbol.
	 */
	String MSACTIVEDIRECTORY_AMP_SYMBOL = "&";
	
	/**
	 * Variable for empty string.
	 */
	String EMPTY_STRING = "";
	
	/**
	 * Variable for tomcat roles.
	 */
	String ROLES = "roles";
	
	/**
	 * Variable for mail.
	 */
	String MAIL = "mail";
	
	/**
	 * Variable for user id.
	 */
	String UID = "uid";
	
	/**
	 * Variable for user password.
	 */
	String USER_PASSWORD = "userpassword";
	
	/**
	 * Variable for inetOrgPerson.
	 */
	String INET_ORG_PERSON = "inetOrgPerson";
	
	/**
	 * Variable for description.
	 */
	String DESCRIPTION = "description";
	
	/**
	 * Variable for uniqueMember.
	 */
	String UNIQUE_MEMBER = "uniqueMember";
	
	/**
	 * Variable for groupOfUniqueNames.
	 */
	String GROUP_OF_UNIQUE_NAMES = "groupOfUniqueNames";
	
	/**
	 * Variable for givenName.
	 */
	String GIVEN_NAME = "givenName";
	
	/**
	 * Variable for sn.
	 */
	String SUR_NAME = "sn";

	/**
	 * Constant for 'Person' LDAP object class name.
	 */
	String OBJECT_CLASS_PERSON = "Person";
	
	/**
	 * Constant for empty.
	 */
	String EMPTY = "";
	
	/**
	 * Constant for zero.
	 */
	int ZERO = 0;
	
	/**
	 * Constant for one.
	 */
	int ONE = 1;
	
	/**
	 * Constant for two.
	 */
	int TWO = 2;
}
