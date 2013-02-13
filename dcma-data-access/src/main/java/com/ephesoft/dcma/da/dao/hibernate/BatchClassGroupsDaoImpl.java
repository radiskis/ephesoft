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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.ephesoft.dcma.core.dao.hibernate.HibernateDao;
import com.ephesoft.dcma.da.dao.BatchClassGroupsDao;
import com.ephesoft.dcma.da.domain.BatchClassGroups;

/**
 * This class is responsible to fetch data of batch instance group table from data base.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.da.dao.BatchClassGroupsDao
 */
@Repository
public class BatchClassGroupsDaoImpl extends HibernateDao<BatchClassGroups> implements BatchClassGroupsDao {

	/**
	 * GROUP_NAME String.
	 */
	private static final String GROUP_NAME = "groupName";
	
	/**
	 * BATCH_CLASS String.
	 */
	private static final String BATCH_CLASS = "batchClass";
	
	/**
	 * BATCH_CLASS_IDENTIFIER String.
	 */
	private static final String BATCH_CLASS_IDENTIFIER = "batchClass.identifier";

	/**
	 * API for getting the batch class identifiers having the user roles.
	 * @param userRoles Set<String>
	 * @return Set<String>
	 */
	@Override
	public Set<String> getBatchClassIdentifierForUserRoles(Set<String> userRoles) {
		//Set<String> batchClassIdentifiers = getBatchClassIdentifierForUserRoles(userRoles, true);
		return getBatchClassIdentifierForUserRoles(userRoles, true);
	}
	
	/**
	 * API for getting the batch class identifiers having the user roles.
	 * @param userRoles Set<String>
	 * @param includeDeleted boolean
	 * @return Set<String>
	 */
	@Override
	public Set<String> getBatchClassIdentifierForUserRoles(Set<String> userRoles, boolean includeDeleted) {
		boolean isValid = true;
		if (userRoles == null || userRoles.size() == 0) {
			isValid = false;
		}
		Set<String> batchClassIdentifiers = null;

		// New version of fetching.
		if (isValid) {
			batchClassIdentifiers = new LinkedHashSet<String>();
			DetachedCriteria criteria = criteria();
			Disjunction disjunction = Restrictions.disjunction();
			for (String userRole : userRoles) {
				disjunction.add(Restrictions.eq(GROUP_NAME, userRole));
			}
			criteria.add(disjunction);
			criteria.addOrder(Order.asc(BATCH_CLASS));
			List<BatchClassGroups> batchClassGroups = find(criteria);
			for (BatchClassGroups batchClassGroup : batchClassGroups) {
				if(includeDeleted) {
					batchClassIdentifiers.add(batchClassGroup.getBatchClass().getIdentifier());	
				} else {
					if(!batchClassGroup.getBatchClass().isDeleted()) {
						batchClassIdentifiers.add(batchClassGroup.getBatchClass().getIdentifier());
					}
				}
			}
		}
		return batchClassIdentifiers;
	}
	
	/**
	 * API for getting the user roles for a batch class.
	 * @param userRoles Set<String>
	 * @param batchClassIdentifier
	 * @return Set<String>
	 */
	@Override
	public Set<String> getRolesForBatchClass(String batchClassIdentifier) {
		Set<String> userGroups = new HashSet<String>();
		DetachedCriteria criteria = criteria();
		criteria.createAlias(BATCH_CLASS, BATCH_CLASS);
		criteria.add(Restrictions.eq(BATCH_CLASS_IDENTIFIER, batchClassIdentifier));
		List<BatchClassGroups> batchClassGroups = find(criteria);
		for (BatchClassGroups batchClassGroup : batchClassGroups) {
			userGroups.add(batchClassGroup.getGroupName());
		}
		return userGroups;
	}
}
