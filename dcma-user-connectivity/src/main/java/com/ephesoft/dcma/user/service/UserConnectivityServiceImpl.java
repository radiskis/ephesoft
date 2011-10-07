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

package com.ephesoft.dcma.user.service;

import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ephesoft.dcma.user.connectivity.UserConnectivity;
import com.ephesoft.dcma.user.connectivity.factory.UserConnectivityFactory;

/**
 * This class Implements the methood of the {@link UserConnectivityService}.
 * 
 * @author Ephesoft
 * @version 1.0
 */
@Service
public class UserConnectivityServiceImpl implements UserConnectivityService {

	/**
	 * Used for handling logs.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(UserConnectivityServiceImpl.class);

	/**
	 * Instance of UserConnectivityFactory.
	 */
	@Autowired
	private UserConnectivityFactory factory;

	@Override
	public Set<String> getAllGroups() {
		LOG.info("Inside get all groups.");
		boolean isValid = true;
		Set<String> allGroups = null;
		Set<String> resultAllgroups = null;
		if (factory == null) {
			LOG.error("User Connectivity Factory is null. Hence returning");
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

	@Override
	public Set<String> getAllUser() {
		LOG.info("Inside get all users.");
		Set<String> allUser = null;
		Set<String> resultAllUser = null;
		boolean isValid = true;
		if (factory == null) {
			LOG.error("User Connectivity Factory is null. Hence returning");
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

}
