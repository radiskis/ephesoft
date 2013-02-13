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

package com.ephesoft.dcma.core.dao.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;

import com.ephesoft.dcma.core.dao.Dao;
import com.ephesoft.dcma.util.ClassUtil;

/**
 * This is JPA dao class.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.dao.Dao
 * @param <T>
 */
public class JpaDao<T> implements Dao<T> {

	/**
	 * OPERATION_NOT_SUPPORTED String.
	 */ 
	private static final String OPERATION_NOT_SUPPORTED = "Operation not supported.";
	
	/**
	 * entityManager EntityManager.
	 */
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * entityClass Class<? extends T>.
	 */
	private Class<? extends T> entityClass;

	protected Class<? extends T> getEntityClass() {
		if (this.entityClass == null) {
			this.entityClass = ClassUtil.getEntityClass(this);
		}
		return this.entityClass;
	}

	/**
	 * To get objects.
	 * @param objectId Serializable
	 */
	public T get(Serializable objectId) {
		return (T) entityManager.find(getEntityClass(), objectId);
	}

	/**
	 * To save objects.
	 * @param object T
	 */
	public void save(T object) {
		((Session) entityManager.getDelegate()).saveOrUpdate(object);
	}

	/**
	 * To remove objects.
	 * @param object T
	 */
	public void remove(T object) {
		entityManager.remove(object);
	}

	/**
	 * To get all the names.
	 * @return List<T>
	 */
	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		return entityManager.createQuery("select obj from " + getEntityClass().getName() + " obj").getResultList();
	}

	/**
	 * To count.
	 * @param criteria DetachedCriteria
	 * @return int
	 * @throws UnsupportedOperationException in case of error
	 */
	@Override
	public int count(DetachedCriteria criteria) {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED);
	}

	/**
	 * To count.
	 * @return int
	 * @throws UnsupportedOperationException in case of error
	 */
	@Override
	public int countAll() {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED);
	}

	/**
	 * To create new object.
	 * @param object T
	 */
	@Override
	public void create(T object) {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED);
	}

	/**
	 * To evict an object.
	 * @param object object
	 */
	@Override
	public void evict(Object object) {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED);
	}

	/**
	 * To merge objects.
	 * @param object T
	 */
	@Override
	public T merge(T object) {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED);
	}

	/**
	 * To save or update an object.
	 * @param object T
	 */
	@Override
	public void saveOrUpdate(T object) {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED);
	}
}
