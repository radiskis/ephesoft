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

package com.ephesoft.dcma.core.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.ephesoft.dcma.core.common.DataFilter;
import com.ephesoft.dcma.core.common.DomainProperty;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.dao.CacheableDao;
import com.ephesoft.dcma.core.dao.Dao;
import com.ephesoft.dcma.core.hibernate.EphesoftCriteria;
import com.ephesoft.dcma.core.model.common.AbstractEntity;
import com.ephesoft.dcma.util.ClassUtil;

public abstract class HibernateDao<T> implements Dao<T> {

	@Autowired
	@Qualifier("hibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	@Autowired
	@Qualifier("cacheableHibernateTemplate")
	private HibernateTemplate cacheableHibernateTemplate;

	@SuppressWarnings("unchecked")
	public HibernateTemplate getHibernateTemplate() {
		if (this instanceof CacheableDao) {
			return cacheableHibernateTemplate;
		}
		return hibernateTemplate;
	}

	private Class<? extends T> entityClass;

	protected Class<? extends T> getEntityClass() {
		if (this.entityClass == null) {
			this.entityClass = ClassUtil.getEntityClass(this);
		}
		return this.entityClass;
	}

	@Override
	public T get(Serializable id) {
		return this.hibernateTemplate.get(getEntityClass(), id);
	}

	public T get(Serializable id, LockMode lockMode) {
		return this.hibernateTemplate.get(getEntityClass(), id, lockMode);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getAll() {
		return this.getHibernateTemplate().findByCriteria(criteria());
	}

	@Override
	public void remove(T object) {
		this.getHibernateTemplate().delete(object);
	}

	@Override
	public void create(T object) {
		this.getHibernateTemplate().persist(object);
		if (object instanceof AbstractEntity) {
			((AbstractEntity) object).postPersist();
			this.getHibernateTemplate().saveOrUpdate(object);
		}
	}

	@Override
	public void saveOrUpdate(T object) {
		this.getHibernateTemplate().saveOrUpdate(object);
		if (object instanceof AbstractEntity) {
			((AbstractEntity) object).postPersist();
			this.getHibernateTemplate().saveOrUpdate(object);
		}
	}

	@Override
	public void evict(Object object) {
		this.getHibernateTemplate().evict(object);
	}

	@Override
	public T merge(T object) {
		object = this.getHibernateTemplate().merge(object);
		if (object instanceof AbstractEntity) {
			((AbstractEntity) object).postPersist();
			this.getHibernateTemplate().saveOrUpdate(object);
		}
		return get(((AbstractEntity) object).getId());
	}

	protected EphesoftCriteria criteria() {
		return this.criteria(this.getEntityClass());
	}

	protected EphesoftCriteria criteria(Class<?> clazz) {
		EphesoftCriteria criteria = EphesoftCriteria.forClass(clazz);
		return criteria;
	}

	protected <E> E findSingle(DetachedCriteria c) {
		List<E> find = this.find(c);
		return getSingleResult(find);
	}

	protected <E> List<E> find(DetachedCriteria c) {
		return this.find(c, -1, -1);
	}

	@SuppressWarnings("unchecked")
	protected <E> List<E> find(DetachedCriteria c, final int firstResult, final int maxResults) {
		return this.getHibernateTemplate().findByCriteria(c, firstResult, maxResults);

	}

	private void updateOrderCriteria(EphesoftCriteria criteria, Order... orders) {
		if (orders == null)
			return;
		for (Order order : orders) {
			String property = order.getSortProperty().getProperty();
			String alias = criteria.getAlias(property, true);
			if (alias == null) {
				criteria.createAlias(property, true);
				alias = criteria.getAlias(property, true);
			}
			if (order.isAscending()) {
				criteria.addOrder(org.hibernate.criterion.Order.asc(alias));
			} else {
				criteria.addOrder(org.hibernate.criterion.Order.desc(alias));
			}
		}
	}

	private void updateFilterCriteria(EphesoftCriteria criteria, DataFilter<? extends DomainProperty>... dataFilters) {
		if (dataFilters == null)
			return;
		for (DataFilter<? extends DomainProperty> dataFilter : dataFilters) {
			String property = dataFilter.getNameProperty().getProperty();
			String alias = criteria.getAlias(property, true);
			if (alias == null) {
				criteria.createAlias(property, true);
				alias = criteria.getAlias(property, true);
			}
			if (dataFilter.isLike()) {
				criteria.add(Restrictions.ilike(alias, dataFilter.getValueProperty()));
			} else {
				criteria.add(Restrictions.eq(alias, dataFilter.getValueProperty()));
			}
		}
	}

	protected <E> List<E> find(EphesoftCriteria criteria, Order... orders) {
		updateOrderCriteria(criteria, orders);
		return find(criteria);
	}

	protected <E> List<E> find(EphesoftCriteria criteria, DataFilter<?>... dataFilters) {
		updateFilterCriteria(criteria, dataFilters);
		return find(criteria);
	}

	protected <E> List<E> find(EphesoftCriteria criteria, final int firstResult, final int maxResults, Order... orders) {
		updateOrderCriteria(criteria, orders);
		return find(criteria, firstResult, maxResults);
	}

	protected <E> List<E> find(EphesoftCriteria criteria, final int firstResult, final int maxResult,
			DataFilter<? extends DomainProperty>[] dataFilters, Order... orders) {
		updateOrderCriteria(criteria, orders);
		updateFilterCriteria(criteria, dataFilters);
		return find(criteria, firstResult, maxResult);
	}

	public int count(DetachedCriteria criteria) {
		criteria.setProjection(Projections.rowCount());
		return (Integer) find(criteria).get(0);
	}

	public int countAll() {
		DetachedCriteria criteria = criteria();
		return count(criteria);
	}

	protected <E> E getSingleResult(List<E> list) throws DataAccessException {
		if (list == null || list.isEmpty()) {
			return null;
		}
		if (list.size() != 1) {
			throw new IncorrectResultSetColumnCountException(1, list.size());
		}
		return list.get(0);
	}

}
