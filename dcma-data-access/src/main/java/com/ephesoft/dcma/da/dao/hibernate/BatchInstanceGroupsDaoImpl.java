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

package com.ephesoft.dcma.da.dao.hibernate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ephesoft.dcma.core.dao.hibernate.HibernateDao;
import com.ephesoft.dcma.da.dao.BatchInstanceGroupsDao;
import com.ephesoft.dcma.da.domain.BatchInstanceGroups;

/**
 * This class is responsible to fetch data of batch instance group table from data base.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.da.dao.BatchInstanceGroupsDao
 */
@Repository
public class BatchInstanceGroupsDaoImpl extends HibernateDao<BatchInstanceGroups> implements BatchInstanceGroupsDao {
	
	/**
	 * Constant for group name.
	 */
	private static final String GROUP_NAME = "groupName";

	/**
	 * Constant for batch class id.
	 */
	private static final String BATCH_INSTANCE_ID = "batchInstanceIdentifier";

	/**
	 * API for getting the batch instance identifiers having the user roles.
	 * @param userRoles Set<String>
	 * @return Set<String>
	 */
	@Override
	public Set<String> getBatchInstanceIdentifierForUserRoles(final Set<String> userRoles) {
		boolean isValid = true;
		if (userRoles == null || userRoles.size() == 0) {
			isValid = false;
		}
		Set<String> batchInstanceIdentifiers = null;

		if (isValid) {
			batchInstanceIdentifiers = new HashSet<String>();
			final DetachedCriteria criteria = criteria();
			final Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.in(GROUP_NAME, userRoles));
			criteria.add(disjunction);
			final List<BatchInstanceGroups> batchInstanceGroups = find(criteria);
			for (final BatchInstanceGroups batchInstanceGroup : batchInstanceGroups) {
				batchInstanceIdentifiers.add(batchInstanceGroup.getBatchInstanceIdentifier());
			}
		}
		return batchInstanceIdentifiers;
	}
	
	/**
	 * API for getting roles for the specified batch instance.
	 * @param batchInstanceId String
	 * @return Set<String>
	 */
	@Override
	public Set<String> getRolesForBatchInstance(String batchInstanceId) {
		Set<String> grps = new HashSet<String>();
		final DetachedCriteria criteria = criteria();
		final Disjunction disjunction = Restrictions.disjunction();
		disjunction.add(Restrictions.eq(BATCH_INSTANCE_ID, batchInstanceId));
		criteria.add(disjunction);
		final List<BatchInstanceGroups> batchClassGroups = find(criteria);
		for (final BatchInstanceGroups batchInstanceGroup : batchClassGroups) {
			grps.add(batchInstanceGroup.getGroupName());
		}
		return grps;
	}
	
	/**
	 * API for adding user role to batch instance.
	 * @param batchInstanceIdentifier {@link String}
	 * @param userRole {@link String}
	 */
	@Transactional
	@Override
	public void addUserRolesToBatchInstanceIdentifier(String batchInstanceIdentifier, String userRole) {
		BatchInstanceGroups batchInstanceGroups = new BatchInstanceGroups();
		batchInstanceGroups.setBatchInstanceIdentifier(batchInstanceIdentifier);
		batchInstanceGroups.setGroupName(userRole);
		create(batchInstanceGroups);
	}
	
	/**
	 * API for getting the batch instance identifiers except provided user roles.
	 * 
	 * @param userRoles Set<String>
	 * @return Set<String>
	 */
	@Override
	public Set<String> getBatchInstanceIdentifiersExceptUserRoles(final Set<String> userRoles) {
		Set<String> batchInstanceIdentifiers = new HashSet<String>();
		final DetachedCriteria criteria = criteria();
		final Disjunction disjunction = Restrictions.disjunction();
		disjunction.add(Restrictions.not(Restrictions.in(GROUP_NAME, userRoles)));
		criteria.add(disjunction);
		final List<BatchInstanceGroups> batchInstanceGroups = find(criteria);
		for (final BatchInstanceGroups batchInstanceGroup : batchInstanceGroups) {
			batchInstanceIdentifiers.add(batchInstanceGroup.getBatchInstanceIdentifier());
		}
		return batchInstanceIdentifiers;
	}
}
