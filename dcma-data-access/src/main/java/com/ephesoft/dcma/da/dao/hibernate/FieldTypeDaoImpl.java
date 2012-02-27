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

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinFragment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ephesoft.dcma.core.dao.hibernate.HibernateDao;
import com.ephesoft.dcma.da.dao.FieldTypeDao;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.FieldType;

/**
 * Implementation of a Dao representing field_type table in database.
 * 
 * @author Ephesoft
 * @version 1.0
 */
@Repository
public class FieldTypeDaoImpl extends HibernateDao<FieldType> implements FieldTypeDao {

	private static final String REGEX_VALIDATION = "regexValidation";
	private static final String NAME = "name";
	private static final String BATCH_CLASS1_IDENTIFIER = "batchClass1.identifier";
	private static final String BATCH_CLASS2_IDENTIFIER = "batchClass2.identifier";
	private static final String BATCH_CLASS2 = "batchClass2";
	private static final String BATCH_CLASS = "batchClass";
	private static final String IDENTIFIER = "identifier";
	private static final String BATCH_CLASS1 = "batchClass1";
	private static final String DOC_TYPE_BATCH_CLASS = "docType.batchClass";
	private static final String DOC_TYPE_NAME = "docType.name";
	private static final String DOC_TYPE = "docType";
	private static final Logger LOG = LoggerFactory.getLogger(FieldTypeDaoImpl.class);

	/**
	 * An api to fetch all Field types by document type name.
	 * 
	 * @param docTypeName
	 * @param batchInstanceID
	 * @return List<FieldType>
	 */
	@Override
	public List<FieldType> getFdTypeByDocTypeNameForBatchInstance(String docTypeName, String batchInstanceIdentifier) {

		LOG.info("batchInstanceID  : " + batchInstanceIdentifier);
		DetachedCriteria criteria = criteria();
		criteria.createAlias(DOC_TYPE, DOC_TYPE, JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq(DOC_TYPE_NAME, docTypeName));
		criteria.createAlias(DOC_TYPE_BATCH_CLASS, BATCH_CLASS1, JoinFragment.INNER_JOIN);
		DetachedCriteria subQuery = criteria(BatchInstance.class);
		subQuery.add(Restrictions.eq(IDENTIFIER, batchInstanceIdentifier));
		subQuery.createAlias(BATCH_CLASS, BATCH_CLASS2, JoinFragment.INNER_JOIN);
		subQuery.setProjection(Projections.property(BATCH_CLASS2_IDENTIFIER));
		criteria.add(Subqueries.propertyEq(BATCH_CLASS1_IDENTIFIER, subQuery));

		return find(criteria);

	}

	@Override
	public FieldType getFieldType(String fieldTypeName, String docTypeName, String batchInstanceIdentifier) {

		LOG.info("batchInstanceID  : " + batchInstanceIdentifier);
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(NAME, fieldTypeName));
		criteria.createAlias(DOC_TYPE, DOC_TYPE, JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq(DOC_TYPE_NAME, docTypeName));
		criteria.createAlias(DOC_TYPE_BATCH_CLASS, BATCH_CLASS1, JoinFragment.INNER_JOIN);
		DetachedCriteria subQuery = criteria(BatchInstance.class);
		subQuery.add(Restrictions.eq(IDENTIFIER, batchInstanceIdentifier));
		subQuery.createAlias(BATCH_CLASS, BATCH_CLASS2, JoinFragment.INNER_JOIN);
		subQuery.setProjection(Projections.property(BATCH_CLASS2_IDENTIFIER));
		criteria.add(Subqueries.propertyEq(BATCH_CLASS1_IDENTIFIER, subQuery));

		return findSingle(criteria);

	}

	/**
	 * An api to fetch all Field types by document type.
	 * 
	 * @param documentType DocumentType
	 * @return List<FieldType>
	 */
	@Override
	public List<FieldType> getFdTypeByDocumentType(DocumentType documentType) {
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(DOC_TYPE, documentType));
		return find(criteria);
	}

	/**
	 * An api to fetch all Field types by document type name.
	 * 
	 * @param docTypeName String
	 * @param batchInstanceID Long
	 * @param isKVExtraction boolean
	 * @return List<FieldType>
	 */
	@Override
	public List<FieldType> getFdTypeByDocumentTypeName(String docTypeName, String batchInstanceIdentifier, boolean isKVExtraction) {
		LOG.info("batchInstanceID ID  : " + batchInstanceIdentifier);
		DetachedCriteria criteria = criteria();
		criteria.createAlias(DOC_TYPE, DOC_TYPE, JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq(DOC_TYPE_NAME, docTypeName));
		criteria.createAlias(DOC_TYPE_BATCH_CLASS, BATCH_CLASS1, JoinFragment.INNER_JOIN);

		if (isKVExtraction) {
			criteria.setFetchMode("kvExtraction", FetchMode.JOIN);
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		}

		DetachedCriteria subQuery = criteria(BatchInstance.class);
		subQuery.add(Restrictions.eq(IDENTIFIER, batchInstanceIdentifier));
		subQuery.createAlias(BATCH_CLASS, BATCH_CLASS2, JoinFragment.INNER_JOIN);
		subQuery.setProjection(Projections.property(BATCH_CLASS2_IDENTIFIER));
		criteria.add(Subqueries.propertyEq(BATCH_CLASS1_IDENTIFIER, subQuery));
		return find(criteria);
	}

	/**
	 * An api to fetch all Field types by document type name.
	 * 
	 * @param docTypeName String
	 * @param batchInstanceID Long
	 * @return List<FieldType>
	 */
	@Override
	public List<FieldType> getFdTypeAndRegexValidationByDocTypeName(String docTypeName, String batchInstanceIdentifier) {

		LOG.info("batchInstanceID ID  : " + batchInstanceIdentifier);
		DetachedCriteria criteria = criteria();
		criteria.createAlias(DOC_TYPE, DOC_TYPE, JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq(DOC_TYPE_NAME, docTypeName));
		criteria.createAlias(DOC_TYPE_BATCH_CLASS, BATCH_CLASS1, JoinFragment.INNER_JOIN);

		criteria.setFetchMode(REGEX_VALIDATION, FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		DetachedCriteria subQuery = criteria(BatchInstance.class);
		subQuery.add(Restrictions.eq(IDENTIFIER, batchInstanceIdentifier));
		subQuery.createAlias(BATCH_CLASS, BATCH_CLASS2, JoinFragment.INNER_JOIN);
		subQuery.setProjection(Projections.property(BATCH_CLASS2_IDENTIFIER));
		criteria.add(Subqueries.propertyEq(BATCH_CLASS1_IDENTIFIER, subQuery));

		return find(criteria);

	}

	/**
	 * An api to insert the fieldType object.
	 * 
	 * @param fieldType FieldType
	 */
	public void insertFieldType(FieldType fieldType) {
		create(fieldType);
	}

	/**
	 * An api to update the fieldType object.
	 * 
	 * @param fieldType FieldType
	 */
	public void updateFieldType(FieldType fieldType) {
		saveOrUpdate(fieldType);
	}

	/**
	 * An api to remove the fieldType object.
	 * 
	 * @param fieldType FieldType
	 */
	public void removeFieldType(FieldType fieldType) {
		remove(fieldType);
	}

	@Override
	public List<FieldType> getFdTypeByDocumentTypeNameForBatchClass(String docTypeName, String batchClassIdentifier) {
		LOG.info("batchClassID ID  : " + batchClassIdentifier);
		DetachedCriteria criteria = criteria();
		criteria.createAlias(DOC_TYPE, DOC_TYPE, JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq(DOC_TYPE_NAME, docTypeName));
		criteria.createAlias(DOC_TYPE_BATCH_CLASS, BATCH_CLASS1, JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq(BATCH_CLASS1_IDENTIFIER, batchClassIdentifier));
		return find(criteria);
	}
}
