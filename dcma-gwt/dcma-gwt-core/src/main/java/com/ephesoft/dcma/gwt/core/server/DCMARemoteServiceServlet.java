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

package com.ephesoft.dcma.gwt.core.server;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ephesoft.dcma.core.exception.BatchAlreadyLockedException;
import com.ephesoft.dcma.da.dao.BatchClassGroupsDao;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.gwt.core.client.DCMARemoteService;
import com.ephesoft.dcma.gwt.core.shared.exception.GWTException;
import com.ephesoft.dcma.user.service.UserConnectivityService;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.ApplicationContextUtil;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public abstract class DCMARemoteServiceServlet extends RemoteServiceServlet implements DCMARemoteService {

	private static final long serialVersionUID = 1L;

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void setup() {

	}

	@Override
	public void initRemoteService() throws Exception {
		try {
			setup();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GWTException(e.getCause().getMessage());
		}
	}

	protected <T> T getSingleBeanOfType(Class<T> type) throws NoSuchBeanDefinitionException {
		WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
		return ApplicationContextUtil.getSingleBeanOfType(ctx, type);
	}

	protected <T> T getBeanByName(String name, Class<T> type) throws NoSuchBeanDefinitionException {
		WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
		return ApplicationContextUtil.getBeanFromContext(ctx, name, type);
	}

	@Override
	public String getUserName() {
		if (this.getThreadLocalRequest().getUserPrincipal() == null)
			return StringUtils.EMPTY;

		return this.getThreadLocalRequest().getUserPrincipal().getName();
	}

	@Override
	public Set<String> getUserRoles() {
		log.info("========Getting the user roles=========");
		Set<String> allGroups = getAllGroups();
		if (null == allGroups || allGroups.isEmpty()) {
			log.error("No groups fetched from Authenticated User.....All groups is empty.Returning null");
			return null;
		}

		Set<String> userGroups = new HashSet<String>();
		for (String group : allGroups) {
			if (null != group && !group.isEmpty()) {
				if (this.getThreadLocalRequest().isUserInRole(group)) {
					log.info("Added group is: " + group);
					userGroups.add(group);
				}
			}
		}
		if (userGroups.isEmpty()) {
			log.error("No roles found in Authenticated User for " + getUserName());
			userGroups = null;
		}
		return userGroups;
	}

	protected boolean isUserAdmin() {
		return this.getThreadLocalRequest().isUserInRole("ADMIN");
	}

	protected void invalidateSession() {
		cleanup();
		this.getThreadLocalRequest().getSession().invalidate();
	}

	@Override
	public final void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			processPost(request, response);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} catch (ServletException e) {
			log.error(e.getMessage(), e);
		} catch (SerializationException e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public void acquireLock(String batchClassIdentifier) throws GWTException {
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		try {
			batchInstanceService.acquireBatchInstance(batchClassIdentifier, getUserName());
		} catch (BatchAlreadyLockedException e) {
			throw new GWTException(e.getMessage());
		}
	}

	@Override
	public void cleanup() {
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		batchInstanceService.unlockAllBatchInstancesForCurrentUser(getUserName());
	}

	@Override
	public void logout() {
		cleanup();
		this.getThreadLocalRequest().getSession().invalidate();
	}

	@Override
	public String getLocale() {
		String locale = (String) this.getServletContext().getInitParameter("locale");
		if (locale.equalsIgnoreCase("default"))
			locale = "";
		return locale;
	}

	@Override
	public Boolean isReportingEnabled() {
		ApplicationConfigProperties configProperties = new ApplicationConfigProperties();
		boolean isReportingEnabled = false;
		try {
			isReportingEnabled = Boolean.parseBoolean(configProperties.getProperty("enable.reporting"));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			isReportingEnabled = false;
		}
		return isReportingEnabled;
	}

	@SuppressWarnings("unchecked")
	public Set<String> getAllGroups() {
		Object allGroups = this.getThreadLocalRequest().getSession().getAttribute("allGroups");
		if (allGroups == null) {
			Set<String> allGroupsSet = null;
			UserConnectivityService userConnectivityService = this.getSingleBeanOfType(UserConnectivityService.class);
			allGroupsSet = userConnectivityService.getAllGroups();
			this.getThreadLocalRequest().getSession().setAttribute("allGroups", allGroupsSet);
			allGroups = allGroupsSet;
		}
		return (Set<String>) allGroups;
	}

	@SuppressWarnings("unchecked")
	public Set<String> getAllUser() {
		Object allUsers = this.getThreadLocalRequest().getSession().getAttribute("allUsers");
		if (allUsers == null) {
			Set<String> allUserSet = null;
			UserConnectivityService userConnectivityService = this.getSingleBeanOfType(UserConnectivityService.class);
			allUserSet = userConnectivityService.getAllUser();
			this.getThreadLocalRequest().getSession().setAttribute("allUsers", allUserSet);
			allUsers = allUserSet;
		}
		return (Set<String>) allUsers;
	}

	public Set<String> getAllBatchClassByUserRoles() {
		BatchClassGroupsDao batchClassGroupsDao = this.getSingleBeanOfType(BatchClassGroupsDao.class);
		Set<String> userRoles = getUserRoles();
		return batchClassGroupsDao.getBatchClassIdentifierForUsers(userRoles);
	}
}
