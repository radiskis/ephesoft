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

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinFragment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ephesoft.dcma.core.dao.hibernate.HibernateDao;
import com.ephesoft.dcma.da.constant.DataAccessConstant;
import com.ephesoft.dcma.da.dao.DocumentTypeDao;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.DocumentType;

/**
 * Implementation of a Dao representing document_type table in database.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.da.dao.DocumentTypeDao
 */
@Repository
public class DocumentTypeDaoImpl extends HibernateDao<DocumentType> implements DocumentTypeDao {

	/**
	 * LOG to print the logging information.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(DocumentTypeDaoImpl.class);

	/**
	 * An API to fetch all DocumentType by batch instance id.
	 * 
	 * @param batchInstanceIdentifier String
	 * @return List<DocumentType>
	 */
	@Override
	public List<DocumentType> getDocTypeByBatchInstanceIdentifier(String batchInstanceIdentifier) {
		LOG.info("batchInstanceID : " + batchInstanceIdentifier);
		DetachedCriteria criteria = criteria();
		DetachedCriteria subQuery = criteria(BatchInstance.class);
		subQuery.add(Restrictions.eq(DataAccessConstant.IDENTIFIER, batchInstanceIdentifier));
		subQuery.setProjection(Projections.property(DataAccessConstant.BATCH_CLASS_ID));
		criteria.add(Subqueries.propertyIn(DataAccessConstant.BATCH_CLASS_ID, subQuery));
		return find(criteria);
	}

	/**
	 * An API to fetch all DocumentType by batch class id.
	 * 
	 * @param batchClassIdentifier String
	 * @param firstIndex int
	 * @param maxResults int
	 * @return List<DocumentType>
	 */
	@Override
	public List<DocumentType> getDocTypeByBatchClassIdentifier(final String batchClassIdentifier, final int firstIndex,
			final int maxResults) {
		DetachedCriteria criteria = criteria();
		criteria.createAlias(DataAccessConstant.BATCHCLASS, DataAccessConstant.BATCHCLASS, JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq(DataAccessConstant.BATCH_CLASS_IDENTIFIER, batchClassIdentifier));
		return find(criteria, firstIndex, maxResults);
	}

	/**
	 * An API to fetch all DocumentType by document type name.
	 * 
	 * @param docTypeName String
	 * @return List<DocumentType>
	 */
	@Override
	public List<DocumentType> getDocTypeByDocTypeName(String docTypeName) {
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(DataAccessConstant.NAME, docTypeName));
		return find(criteria);
	}

	/**
	 * An API to insert the documentType object.
	 * 
	 * @param documentType DocumentType
	 */
	@Override
	public void insertDocumentType(DocumentType documentType) {
		create(documentType);
	}

	/**
	 * An API to update the documentType object.
	 * 
	 * @param documentType DocumentType
	 */
	@Override
	public void updateDocumentType(DocumentType documentType) {
		saveOrUpdate(documentType);
	}

	/**
	 * An API to remove the documentType object.
	 * 
	 * @param documentType DocumentType
	 */
	@Override
	public void removeDocumentType(DocumentType documentType) {
		remove(documentType);
	}

	/**
	 * An API to get document type based on document type identifier.
	 * 
	 * @param identifier String
	 * @return DocumentType
	 */
	@Override
	public DocumentType getDocTypeByIdentifier(String identifier) {
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(DataAccessConstant.IDENTIFIER, identifier));
		return this.findSingle(criteria);
	}
	
	/**
	 * An API to fetch all DocumentType by batch class id.
	 * 
	 * @param batchClassIdentifier String
	 * @return List<DocumentType>
	 */
	@Override
	public List<DocumentType> getDocTypeByBatchClassIdentifier(final String batchClassIdentifier) {
		DetachedCriteria criteria = criteria();
		criteria.createAlias(DataAccessConstant.BATCHCLASS, DataAccessConstant.BATCHCLASS, JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq(DataAccessConstant.BATCH_CLASS_IDENTIFIER, batchClassIdentifier));
		return find(criteria);
	}
}
