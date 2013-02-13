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

package com.ephesoft.dcma.da.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ephesoft.dcma.da.dao.UserDao;
import com.ephesoft.dcma.da.domain.User;

/**
 * This service deals with User Information.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.da.service.UserService
 */
@Service
public class UserServiceImpl implements UserService {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	/**
	 * userDao {@link UserDao}.
	 */
	@Autowired
	private UserDao userDao;

	/**
	 * API to create user into database.
	 * 
	 * @param user {@link User}
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void createUser(User user) {
		LOGGER.info("Inside user creation method");
		userDao.create(user);
		LOGGER.info("Exiting user creation method");
	}

	/**
	 * API to delete user from the database.
	 * 
	 * @param user {@link User}
	 */
	@Override
	@Transactional
	public void deleteUser(User user) {
		LOGGER.info("Inside user deletion method");
		userDao.remove(user);
		LOGGER.info("Exiting user deletion method");
	}

	/**
	 * API to update user into the database.
	 * 
	 * @param user {@link User}
	 */
	@Override
	@Transactional
	public User updateUser(User user) {
		User updatedUser = null;
		LOGGER.info("Inside user updation method");
		updatedUser = userDao.merge(user);
		return updatedUser;
	}

	/**
	 * API to get User Information from user email ID.
	 * 
	 * @param emailId {@link String}
	 */
	@Override
	public User getUser(String emailId) {
		LOGGER.info("Fetching user for emailId : " + emailId);
		User user = null;
		if (emailId != null) {
			user = userDao.getUser(emailId);
		}
		return user;
	}

	/**
	 * API to get User Information from batch class ID.
	 * 
	 * @param batchClassId {@link String}
	 */
	@Override
	public User getUserByBatchClassId(String batchClassId) {
		LOGGER.info("Fetching user for emailId : " + batchClassId);
		User user = null;
		if (batchClassId != null) {
			user = userDao.getUserByBatchClassId(batchClassId);
		}
		return user;
	}

}
