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

package com.ephesoft.dcma.user.connectivity.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.user.connectivity.UserConnectivity;

/**
 * This class reads the value provided in user.connection in the user-connectivity.properties file it will return the implemention of
 * the user connectivity e.g. ldap or active directory in accordance of the user choice.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.user.connectivity.impl.LdapConnectivity
 * @see com.ephesoft.dcma.user.connectivity.impl.MSActiveDirectoryConnectivity
 * @see com.ephesoft.dcma.user.connectivity.impl.TomcatConnectivity
 */
@Component
public class UserConnectivityFactory {

	/**
	 * Used for handling logs.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(UserConnectivityFactory.class);

	/**
	 * Instance of LdapConnectivity.
	 */
	@Autowired
	@Qualifier("ldapConnectivity")
	private UserConnectivity ldapConnectivity;

	/**
	 * Instance of MSActiveDirectoryConnectivity.
	 */
	@Autowired
	@Qualifier("msActiveDirectoryConnectivity")
	private UserConnectivity msActiveDirectoryConnectivity;

	/**
	 * Instance of MSActiveDirectoryConnectivity.
	 */
	@Autowired
	@Qualifier("tomcatConnectivity")
	private UserConnectivity tomcatConnectivity;

	/**
	 * This value get from the user.connection in user-connectivity.properties file.
	 */
	private int choice;

	/**
	 * @param choice the choice to set
	 */
	public void setChoice(final int choice) {
		this.choice = choice;
	}

	/**
	 * This method is used to return the implementation of user connectivity in accordance of the choice provided in the
	 * user.connection in user-connectivity.properties file. It return ldapConnectivity for choice 0 and msActiveConnectivity for
	 * choice 1.
	 * 
	 * @return ldapConnectivity if choice is 0, msActiveDirectoryConnectivity if choice is 1, else return null
	 */
	public UserConnectivity getImplementation() {
		if (ldapConnectivity == null) {
			LOG.error("Ldap Connectivity is null");
		}
		if (msActiveDirectoryConnectivity == null) {
			LOG.error("MsActiveDirectory Connectivity is null");
		}
		if (tomcatConnectivity == null) {
			LOG.error("Tomcat Connectivity is null");
		}
		UserConnectivity connectivity = null;
		if (choice == 0) {
			connectivity = ldapConnectivity;
		} else if (choice == 1) {
			connectivity = msActiveDirectoryConnectivity;
		} else if (choice == 2) {
			connectivity = tomcatConnectivity;
		}
		return connectivity;
	}
}
