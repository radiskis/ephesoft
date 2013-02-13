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

package com.ephesoft.dcma.core.hibernate;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.TreeBidiMap;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.impl.CriteriaImpl;
import org.springframework.util.StringUtils;

import com.ephesoft.dcma.core.constant.CoreConstants;

/**
 * Ephesoft criteria class to create alias.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see org.hibernate.criterion.DetachedCriteria
 */
public class EphesoftCriteria extends DetachedCriteria {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * aliasEntityMap BidiMap.
	 */
	private final BidiMap aliasEntityMap = new TreeBidiMap();

	/**
	 * Constructor.
	 * 
	 * @param impl CriteriaImpl
	 * @param criteria Criteria
	 */
	public EphesoftCriteria(CriteriaImpl impl, Criteria criteria) {
		super(impl, criteria);
	}

	/**
	 * Constructor.
	 * 
	 * @param entityName String
	 * @param alias String
	 */
	public EphesoftCriteria(String entityName, String alias) {
		super(entityName, alias);
	}

	/**
	 * 
	 * Constructor.
	 * 
	 * @param entityName String
	 */
	public EphesoftCriteria(String entityName) {
		super(entityName);
	}

	/**
	 * Creating alias for entity name.
	 * 
	 * @param entityName String
	 * @return EphesoftCriteria
	 */
	public static EphesoftCriteria forEntityName(String entityName) {
		return new EphesoftCriteria(entityName);
	}

	/**
	 * Creating alias for entity name.
	 * 
	 * @param entityName String
	 * @param alias String
	 * @return EphesoftCriteria
	 */
	public static EphesoftCriteria forEntityName(String entityName, String alias) {
		return new EphesoftCriteria(entityName, alias);
	}

	/**
	 * Creating alias for class.
	 * 
	 * @param clazz Class<?> 
	 * @return EphesoftCriteria
	 */
	public static EphesoftCriteria forClass(Class<?> clazz) {
		return new EphesoftCriteria(clazz.getName());
	}

	/**
	 * Creating alias for class.
	 * 
	 * @param clazz Class<?> 
	 * @param alias String
	 * @return EphesoftCriteria
	 */
	public static EphesoftCriteria forClass(Class<?> clazz, String alias) {
		return new EphesoftCriteria(clazz.getName(), alias);
	}

	/**
	 * To create alias.
	 * 
	 * @param associationPath String
	 * @param alias String
	 * @return DetachedCriteria
	 * @throws HibernateException in case of error
	 */
	public DetachedCriteria createAlias(String associationPath, String alias) throws HibernateException {
		super.createAlias(associationPath, alias);
		String[] involvedEntitiyVars = split(associationPath);
		aliasEntityMap.put(alias, involvedEntitiyVars[involvedEntitiyVars.length - 1]);
		return this;
	}

	/**
	 * To create alias.
	 * 
	 * @param associationPath String
	 * @param alias Stringfz
	 * @param joinType int
	 * @return DetachedCriteria
	 * @throws HibernateException in case of error
	 */
	public DetachedCriteria createAlias(String associationPath, String alias, int joinType) throws HibernateException {
		super.createAlias(associationPath, alias, joinType);
		String[] involvedEntitiyVars = split(associationPath);
		aliasEntityMap.put(alias, involvedEntitiyVars[involvedEntitiyVars.length - 1]);
		return this;
	}

	/**
	 * To create alias.
	 * 
	 * @param associationPath String
	 * @param containsProperty boolean
	 * @return DetachedCriteria
	 * @throws HibernateException in case of error
	 */
	public DetachedCriteria createAlias(String associationPath, boolean containsProperty) throws HibernateException {
		String[] associations = split(associationPath);

		if (associations.length < (containsProperty ? 2 : 1)) {
			throw new HibernateException("Incorrect Association Path...");
		}

		for (int i = 0; i < (containsProperty ? associations.length - 1 : associations.length); i++) {
			if (getAlias(associations[i]) == null) {
				this.createAlias(associations[i], associations[i]);
			}
		}
		return this;
	}

	/**
	 * To create alias.
	 * 
	 * @param associationPath String
	 * @return DetachedCriteria
	 * @throws HibernateException in case of error
	 */
	public DetachedCriteria createAlias(String associationPath) throws HibernateException {
		return createAlias(associationPath, false);
	}

	/**
	 * To get alias.
	 * 
	 * @param associationPath String
	 * @param containsProperty boolean
	 * @return String
	 */ 
	public String getAlias(String associationPath, boolean containsProperty) {
		String path = null;
		String[] involvedEntitiyVars = split(associationPath);

		if (involvedEntitiyVars.length == 1) {
			path = involvedEntitiyVars[0];
		} else {
			if (involvedEntitiyVars.length < (containsProperty ? 2 : 1)) {

				throw new HibernateException("Incorrect Association Path...");
			}

			String alias = (String) this.aliasEntityMap.getKey(involvedEntitiyVars[involvedEntitiyVars.length
					- (containsProperty ? 2 : 1)]);
			if (alias != null && containsProperty) {
				path = alias + CoreConstants.DOT + involvedEntitiyVars[involvedEntitiyVars.length - 1];
			}
		}
		return path;
	}

	/**
	 * To get alias.
	 * 
	 * @param associationPath String
	 * @return String
	 */ 
	public String getAlias(String associationPath) {
		return getAlias(associationPath, false);
	}

	private String[] split(String associationPath) {
		String[] involvedEntitiyVars = StringUtils.split(associationPath, CoreConstants.DOT);
		if (involvedEntitiyVars == null) {
			involvedEntitiyVars = new String[1];
			involvedEntitiyVars[0] = associationPath;
		}
		return involvedEntitiyVars;
	}
}
