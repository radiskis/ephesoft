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

package com.ephesoft.dcma.da.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ephesoft.dcma.core.service.DataAccessService;
import com.ephesoft.dcma.da.dao.DocumentTypeDao;
import com.ephesoft.dcma.da.domain.DocumentType;

/**
 * This is a database service to read data required by Document Type Service .
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.da.service.DocumentTypeService
 */
@Service
public class DocumentTypeServiceImpl extends DataAccessService implements DocumentTypeService {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentTypeServiceImpl.class);

	@Autowired
	private DocumentTypeDao documentTypeDao;

	/**
	 * An api to fetch all DocumentType by document type name.
	 * 
	 * @param docTypeName String
	 * @return List<DocumentType>
	 */
	@Transactional(readOnly = true)
	@Override
	public List<DocumentType> getDocTypeByDocTypeName(String docTypeName) {
		List<DocumentType> documentType = null;
		if (null == docTypeName || "".equals(docTypeName)) {
			LOGGER.info("Input docTypeName is null or empty.");
		} else {
			documentType = documentTypeDao.getDocTypeByDocTypeName(docTypeName);
		}
		return documentType;
	}

	/**
	 * An api to insert the documentType object.
	 * 
	 * @param documentType DocumentType
	 */
	@Transactional
	@Override
	public void insertDocumentType(DocumentType documentType) {
		if (null == documentType) {
			LOGGER.info("Document object is null.");
		} else {
			documentTypeDao.insertDocumentType(documentType);
		}
	}

	/**
	 * An api to update the documentType object.
	 * 
	 * @param documentType DocumentType
	 */
	@Transactional
	@Override
	public void updateDocumentType(DocumentType documentType) {
		if (null == documentType) {
			LOGGER.info("Document object is null.");
		} else {
			documentTypeDao.updateDocumentType(documentType);
		}
	}

	/**
	 * An api to remove the documentType object.
	 * 
	 * @param documentType DocumentType
	 */
	@Transactional
	@Override
	public void removeDocumentType(DocumentType documentType) {
		if (null == documentType) {
			LOGGER.info("Document object is null.");
		} else {
			documentTypeDao.removeDocumentType(documentType);
		}
	}

	/**
	 * An api to fetch all DocumentType by batch instance id.
	 * 
	 * @param batchInstanceID Long
	 * @return List<DocumentType>
	 */
	@Transactional
	@Override
	public List<DocumentType> getDocTypeByBatchInstanceIdentifier(String batchInstanceIdentifier) {
		List<DocumentType> documentType = null;
		if (null == batchInstanceIdentifier) {
			LOGGER.info("Input batchInstanceIdentifier is null.");
		} else {
			documentType = documentTypeDao.getDocTypeByBatchInstanceIdentifier(batchInstanceIdentifier);
		}
		return documentType;
	}

	/**
	 * An api to fetch all DocumentType by batch class id.
	 * 
	 * @param batchClassID Long
	 * @param firstIndex int
	 * @param maxResults int
	 * @return List<DocumentType>
	 */
	@Transactional
	@Override
	public List<DocumentType> getDocTypeByBatchClassIdentifier(final String batchClassIdentifier, final int firstIndex,
			final int maxResults) {
		List<DocumentType> documentType = null;
		if (null == batchClassIdentifier) {
			LOGGER.info("Input batchClassID is null.");
		} else {
			documentType = documentTypeDao.getDocTypeByBatchClassIdentifier(batchClassIdentifier, firstIndex, maxResults);
		}
		return documentType;
	}

	@Override
	public DocumentType getDocTypeByIdentifier(String identifier) {
		DocumentType returnVal = null;
		if (identifier == null || identifier.isEmpty()) {
			LOGGER.warn("Empty identifier for document type");
		} else {
			DocumentType documentType = null;
			documentType = documentTypeDao.getDocTypeByIdentifier(identifier);
			returnVal = documentType;
		}
		return returnVal;
	}

	@Override
	public void evict(DocumentType documentType) {
		documentTypeDao.evict(documentType);
	}
}
