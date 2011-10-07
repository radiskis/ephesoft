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

package com.ephesoft.dcma.da.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinFragment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ephesoft.dcma.core.dao.hibernate.HibernateDao;
import com.ephesoft.dcma.da.dao.PageTypeDao;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.PageType;

/**
 * Implementation of a Dao representing page_type table in database
 * 
 * @author Ephesoft
 * 
 */
@Repository
public class PageTypeDaoImpl extends HibernateDao<PageType> implements PageTypeDao {

	private static final String DOC_TYPE_IDENTIFIER = "docType.identifier";
	private static final String NAME = "name";
	private static final String DOC_TYPE_NAME = "docType.name";
	private static final String BATCH_CLASS_IDENTIFIER = "batchClass.identifier";
	private static final String BATCH_CLASS1_IDENTIFIER = "batchClass1.identifier";
	private static final String BATCH_CLASS2_IDENTIFIER = "batchClass2.identifier";
	private static final String BATCH_CLASS2 = "batchClass2";
	private static final String BATCH_CLASS = "batchClass";
	private static final String IDENTIFIER = "identifier";
	private static final String BATCH_CLASS1 = "batchClass1";
	private static final String DOC_TYPE_BATCH_CLASS = "docType.batchClass";
	private static final String DOC_TYPE = "docType";
	private static final String DOCUMENT_TYPE = "documentType : ";
	private static final Logger LOGGER = LoggerFactory.getLogger(PageTypeDaoImpl.class);

	/**
	 * An api to fetch all page types by document type
	 * 
	 * @param documentType
	 * @return List<PageType>
	 */
	@Override
	public List<PageType> getPageTypesByDocumentType(DocumentType documentType) {
		LOGGER.info(DOCUMENT_TYPE + documentType);
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(DOC_TYPE, documentType));
		return find(criteria);
	}

	/**
	 * An api to fetch all Page types by batchInstanceID.
	 * 
	 * @param batchInstanceID Long
	 * @return List<PageType>
	 */
	@Override
	public List<PageType> getPageTypesByBatchInstanceID(String batchInstanceIdentifier) {
		DetachedCriteria criteria = criteria();
		criteria.createAlias(DOC_TYPE, DOC_TYPE, JoinFragment.INNER_JOIN);
		criteria.createAlias(DOC_TYPE_BATCH_CLASS, BATCH_CLASS1, JoinFragment.INNER_JOIN);
		DetachedCriteria subQuery = criteria(BatchInstance.class);
		subQuery.add(Restrictions.eq(IDENTIFIER, batchInstanceIdentifier));
		subQuery.createAlias(BATCH_CLASS, BATCH_CLASS2, JoinFragment.INNER_JOIN);
		subQuery.setProjection(Projections.property(BATCH_CLASS2_IDENTIFIER));

		criteria.add(Subqueries.propertyEq(BATCH_CLASS1_IDENTIFIER, subQuery));

		return find(criteria);
	}

	/**
	 * An api to fetch all batch class id, document type names and Page type names corresponding to each other.
	 * 
	 * @param batchClassIDList List<Long>
	 * @return List<Object[]>
	 */
	@Override
	public List<Object[]> getDocTypeNameAndPgTypeName(List<String> batchClassIdentifierList) {
		DetachedCriteria criteria = criteria();
		criteria.createAlias(DOC_TYPE, DOC_TYPE, JoinFragment.RIGHT_OUTER_JOIN);
		criteria.createAlias(DOC_TYPE_BATCH_CLASS, BATCH_CLASS, JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.in(BATCH_CLASS_IDENTIFIER, batchClassIdentifierList));
		criteria.addOrder(Order.asc(BATCH_CLASS_IDENTIFIER));
		criteria.addOrder(Order.asc(DOC_TYPE_NAME));
		criteria.setProjection(Projections.projectionList().add(Projections.property(BATCH_CLASS_IDENTIFIER)).add(
				Projections.property(DOC_TYPE_NAME)).add(Projections.property(NAME)));
		return find(criteria);
	}

	/**
	 * An api to fetch page type by page type name.
	 * 
	 * @param name String
	 * @param batchInstanceID Long
	 * @return List<PageType>
	 */
	@Override
	public List<PageType> getPageTypeByName(String name, String batchInstanceIdentifier) {
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(NAME, name));
		if (null != batchInstanceIdentifier) {
			criteria.createAlias(DOC_TYPE, DOC_TYPE, JoinFragment.INNER_JOIN);
			criteria.createAlias(DOC_TYPE_BATCH_CLASS, BATCH_CLASS1, JoinFragment.INNER_JOIN);
			DetachedCriteria subQuery = criteria(BatchInstance.class);
			subQuery.add(Restrictions.eq(IDENTIFIER, batchInstanceIdentifier));
			subQuery.createAlias(BATCH_CLASS, BATCH_CLASS2, JoinFragment.INNER_JOIN);
			subQuery.setProjection(Projections.property(BATCH_CLASS2_IDENTIFIER));
			criteria.add(Subqueries.propertyEq(BATCH_CLASS1_IDENTIFIER, subQuery));
		}
		return find(criteria);
	}

	/**
	 * An api to fetch all Page types by document type name.
	 * 
	 * @param docTypeName String
	 * @return List<PageType>
	 */
	@Override
	public List<PageType> getPageTypeByDocTypeName(String docTypeName) {
		DetachedCriteria criteria = criteria();
		criteria.createAlias(DOC_TYPE, DOC_TYPE, JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq(DOC_TYPE_NAME, docTypeName));
		return find(criteria);
	}

	/**
	 * An api to fetch all the page types for input document type ID.
	 * 
	 * @param documentTypeID Long
	 * @param startResult int
	 * @param maxResult int
	 * @return List<PageType>
	 */
	@Override
	public List<PageType> getPageTypes(String documentTypeIdentifier, int startResult, int maxResult) {
		DetachedCriteria criteria = criteria();
		criteria.createAlias(DOC_TYPE, DOC_TYPE, JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq(DOC_TYPE_IDENTIFIER, documentTypeIdentifier));
		return find(criteria, startResult, maxResult);
	}

	/**
	 * An api to insert the page type object.
	 * 
	 * @param pageType PageType
	 */
	@Override
	public void insertPageType(PageType pageType) {
		create(pageType);
	}

	/**
	 * An api to update the pageType object.
	 * 
	 * @param pageType PageType
	 */
	@Override
	public void updatePageType(PageType pageType) {
		saveOrUpdate(pageType);
	}

	/**
	 * An api to remove the pageType object.
	 * 
	 * @param pageType PageType
	 */
	@Override
	public void removePageType(PageType pageType) {
		remove(pageType);
	}
}
