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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.exception.BatchAlreadyLockedException;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.service.BatchClassGroupsService;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceGroupsService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.gwt.core.client.DCMARemoteService;
import com.ephesoft.dcma.gwt.core.shared.exception.GWTException;
import com.ephesoft.dcma.user.service.UserConnectivityService;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.ApplicationContextUtil;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public abstract class DCMARemoteServiceServlet extends RemoteServiceServlet implements DCMARemoteService {

	private static final String ALL_GROUPS = "allGroups";

	private static final String ALL_SUPER_ADMIN_GROUPS = "allSuperAdminGroups";

	private static final String ALL_USERS = "allUsers";

	private static final String REVIEW_BATCH_LIST_PRIORITY_FILTER = "reviewBatchListPriorityFilter";

	private static final String VALIDATE_BATCH_LIST_PRIORITY_FILTER = "validateBatchListPriorityFilter";

	private static final String RESTART_ALL_STATUS = "restartAllStatus";

	private static final String COULD_NOT_CLEAR_CURRENT_USER_ERROR_MSG = "Could not clear current user for batch id: ";

	private static final long serialVersionUID = 1L;

	private static final String IS_SUPER_ADMIN = "isSuperAdmin";

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void setup() {

	}

	@Override
	public void setUpForLicenseExpiryAlert() {

	}

	@Override
	public void initRemoteService() throws Exception {
		this.getThreadLocalRequest().getSession().removeAttribute(ALL_GROUPS);
		this.getThreadLocalRequest().getSession().removeAttribute(ALL_USERS);
		try {
			setup();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GWTException(e.getCause().getMessage());
		}
	}

	@Override
	public void initRemoteServiceForLicenseAlert() throws Exception {
		try {
			setUpForLicenseExpiryAlert();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GWTException(e.getMessage());
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

	public Boolean isSuperAdmin() {
		Boolean isSuperAdmin = (Boolean) this.getThreadLocalRequest().getSession().getAttribute(IS_SUPER_ADMIN);
		if (isSuperAdmin == null) {
			Set<String> allSuperAdminGroups = getAllSuperAdminGroup();
			for (String superAdminGroup : allSuperAdminGroups) {
				if (this.getThreadLocalRequest().isUserInRole(superAdminGroup)) {
					isSuperAdmin = Boolean.TRUE;
					break;
				}
			}
			if (isSuperAdmin == null) {
				isSuperAdmin = Boolean.FALSE;
			}

			this.getThreadLocalRequest().getSession().setAttribute(IS_SUPER_ADMIN, isSuperAdmin);

		}
		return isSuperAdmin;

	}

	@SuppressWarnings("unchecked")
	public Set<String> getAllSuperAdminGroup() {
		Object allSuperAdminGroups = this.getThreadLocalRequest().getSession().getAttribute(ALL_SUPER_ADMIN_GROUPS);
		if (allSuperAdminGroups == null) {
			Set<String> allGroupsSet = null;
			UserConnectivityService userConnectivityService = this.getSingleBeanOfType(UserConnectivityService.class);
			allGroupsSet = userConnectivityService.getAllSuperAdminGroups();
			this.getThreadLocalRequest().getSession().setAttribute(ALL_SUPER_ADMIN_GROUPS, allGroupsSet);
			allSuperAdminGroups = allGroupsSet;
		}
		return (Set<String>) allSuperAdminGroups;
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
	public void acquireLock(String batchInstanceIdentifier) throws GWTException {
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		try {

			BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdentifier);
			String batchClassIdentifier = batchInstance.getBatchClass().getIdentifier();
			Set<String> batchClassIdentifiers = getAllBatchClassByUserRoles();
			Set<String> batchInstanceIdentifiers = getAllBatchInstanceByUserRoles();
			if (batchClassIdentifiers.contains(batchClassIdentifier)) {
				batchInstanceService.acquireBatchInstance(batchInstanceIdentifier, getUserName());
			} else if (batchInstanceIdentifiers.contains(batchInstanceIdentifier)) {
				batchInstanceService.acquireBatchInstance(batchInstanceIdentifier, getUserName());
			}
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
	public void cleanUpCurrentBatch(String batchIdentifier) {
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);

		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchIdentifier);
		if (batchInstance != null) {
			String currentUser = batchInstance.getCurrentUser();
			if (currentUser != null && currentUser.equals(getUserName())) {
				batchInstanceService.unlockCurrentBatchInstance(batchIdentifier);
			}
		} else {
			log.error(COULD_NOT_CLEAR_CURRENT_USER_ERROR_MSG + batchIdentifier);
		}
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
		boolean isReportingEnabled = false;
		try {
			ApplicationConfigProperties configProperties = ApplicationConfigProperties.getApplicationConfigProperties();
			isReportingEnabled = Boolean.parseBoolean(configProperties.getProperty("enable.reporting"));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			isReportingEnabled = false;
		}
		return isReportingEnabled;
	}

	@Override
	public Boolean isUploadBatchEnabled() {
		boolean isUploadBatchEnabled = false;
		try {
			ApplicationConfigProperties configProperties = ApplicationConfigProperties.getApplicationConfigProperties();
			isUploadBatchEnabled = Boolean.parseBoolean(configProperties.getProperty("enable.uploadBatch"));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			isUploadBatchEnabled = false;
		}
		return isUploadBatchEnabled;
	}

	@SuppressWarnings("unchecked")
	public Set<String> getAllGroups() {
		Object allGroups = this.getThreadLocalRequest().getSession().getAttribute(ALL_GROUPS);
		if (allGroups == null) {
			Set<String> allGroupsSet = null;
			UserConnectivityService userConnectivityService = this.getSingleBeanOfType(UserConnectivityService.class);
			allGroupsSet = userConnectivityService.getAllGroups();
			this.getThreadLocalRequest().getSession().setAttribute(ALL_GROUPS, allGroupsSet);
			allGroups = allGroupsSet;
		}
		return (Set<String>) allGroups;
	}

	@SuppressWarnings("unchecked")
	public Set<String> getAllUser() {
		Object allUsers = this.getThreadLocalRequest().getSession().getAttribute(ALL_USERS);
		if (allUsers == null) {
			Set<String> allUserSet = null;
			UserConnectivityService userConnectivityService = this.getSingleBeanOfType(UserConnectivityService.class);
			allUserSet = userConnectivityService.getAllUser();
			this.getThreadLocalRequest().getSession().setAttribute(ALL_USERS, allUserSet);
			allUsers = allUserSet;
		}
		return (Set<String>) allUsers;
	}

	public Set<String> getAllBatchClassByUserRoles() {
		BatchClassGroupsService batchClassGroupsService = this.getSingleBeanOfType(BatchClassGroupsService.class);
		boolean isSuperAdmin = isSuperAdmin().booleanValue();
		Set<String> userRoles = getUserRoles();
		Set<String> batchClassIdentifiersSet = new HashSet<String>();
		if (!isSuperAdmin) {
			Set<String> batchClassIdentifiers = batchClassGroupsService.getBatchClassIdentifierForUserRoles(userRoles);
			if (batchClassIdentifiers != null) {
				batchClassIdentifiersSet = batchClassIdentifiers;
			}
		} else {
			BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
			List<String> allBatchClassIdentifiers = batchClassService.getAllBatchClassIdentifier();
			if (allBatchClassIdentifiers != null && allBatchClassIdentifiers.size() > 0) {
				for (String batchClassIdentifier : allBatchClassIdentifiers) {
					batchClassIdentifiersSet.add(batchClassIdentifier);
				}
			}
		}
		return batchClassIdentifiersSet;
	}

	private Set<String> getAllBatchInstanceByUserRoles() {
		BatchInstanceGroupsService batchInstanceGroupsService = this.getSingleBeanOfType(BatchInstanceGroupsService.class);
		Set<String> userRoles = getUserRoles();
		return batchInstanceGroupsService.getBatchInstanceIdentifierForUserRoles(userRoles);
	}

	@Override
	public Map<BatchInstanceStatus, Integer> getBatchListPriorityFilter() {
		Map<BatchInstanceStatus, Integer> priorityFilter = new HashMap<BatchInstanceStatus, Integer>(2);
		Object reviewBatchListPriorityFilter = this.getThreadLocalRequest().getSession().getAttribute(
				REVIEW_BATCH_LIST_PRIORITY_FILTER);
		Object validateBatchListPriorityFilter = this.getThreadLocalRequest().getSession().getAttribute(
				VALIDATE_BATCH_LIST_PRIORITY_FILTER);
		if (reviewBatchListPriorityFilter != null) {
			priorityFilter.put(BatchInstanceStatus.READY_FOR_REVIEW, (Integer) reviewBatchListPriorityFilter);
		}
		if (validateBatchListPriorityFilter != null) {
			priorityFilter.put(BatchInstanceStatus.READY_FOR_VALIDATION, (Integer) validateBatchListPriorityFilter);
		}
		return priorityFilter;
	}

	@Override
	public void setBatchListPriorityFilter(Integer reviewBatchListPriority, Integer validateBatchListPriority) {
		this.getThreadLocalRequest().getSession().setAttribute(REVIEW_BATCH_LIST_PRIORITY_FILTER, reviewBatchListPriority);
		this.getThreadLocalRequest().getSession().setAttribute(VALIDATE_BATCH_LIST_PRIORITY_FILTER, validateBatchListPriority);
	}

	@Override
	public Boolean isRestartAllBatchEnabled() {
		boolean isRestartAllBatchEnabled = false;
		Object restartAllStatus = this.getThreadLocalRequest().getSession().getAttribute(RESTART_ALL_STATUS);
		try {
			if (null == restartAllStatus) {
				ApplicationConfigProperties configProperties = ApplicationConfigProperties.getApplicationConfigProperties();
				isRestartAllBatchEnabled = Boolean.parseBoolean(configProperties.getProperty("enable.restart_all_batch"));
				this.getThreadLocalRequest().getSession().setAttribute(RESTART_ALL_STATUS, isRestartAllBatchEnabled);
			} else {
				isRestartAllBatchEnabled = (Boolean) restartAllStatus;
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			isRestartAllBatchEnabled = false;
		}
		return isRestartAllBatchEnabled;
	}

	@Override
	public Integer getBatchListTableRowCount() {
		Integer rowCount = null;
		try {
			ApplicationConfigProperties configProperties = ApplicationConfigProperties.getApplicationConfigProperties();
			rowCount = Integer.parseInt(configProperties.getProperty("batchlist.table_row_count"));
		} catch (Exception e) {
			log.info("batchlist.table_row_count is invalid or not exist in application.properties.Using default value: 15");
		}
		return rowCount;

	}

	@Override
	public BatchInstanceStatus getBatchListScreenTab(String userName) {
		BatchInstanceStatus batchListScreenTab = (BatchInstanceStatus) this.getThreadLocalRequest().getSession()
				.getAttribute(userName);
		return batchListScreenTab;
	}

	@Override
	public void setBatchListScreenTab(String userName, BatchInstanceStatus batchDTOStatus) {
		this.getThreadLocalRequest().getSession().setAttribute(userName, batchDTOStatus);
	}

	@Override
	public void disableRestartAllButton() {
		this.getThreadLocalRequest().getSession().setAttribute(RESTART_ALL_STATUS, Boolean.FALSE);
	}

	@Override
	public String getCurrentUser(String batchInstanceIdentifier) {
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdentifier);
		return batchInstance.getCurrentUser();
	}
}
