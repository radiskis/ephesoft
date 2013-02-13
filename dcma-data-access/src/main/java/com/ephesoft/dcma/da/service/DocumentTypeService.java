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

package com.ephesoft.dcma.da.service;

import java.util.List;

import com.ephesoft.dcma.da.domain.DocumentType;

/**
 * This is a database service to read data required by Document Type Service .
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.da.service.DocumentTypeServiceImpl
 */
public interface DocumentTypeService {

	/**
	 * An API to fetch all DocumentType by batch instance id.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @return List<{@link DocumentType}>
	 */
	List<DocumentType> getDocTypeByBatchInstanceIdentifier(String batchInstanceIdentifier);

	/**
	 * An API to fetch all DocumentType by batch class id.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param firstIndex int
	 * @param maxResults int
	 * @return List<{@link DocumentType}>
	 */
	List<DocumentType> getDocTypeByBatchClassIdentifier(final String batchClassIdentifier, final int firstIndex, final int maxResults);

	/**
	 * An API to fetch all DocumentType by document type name.
	 * 
	 * @param docTypeName {@link String}
	 * @return List<{@link DocumentType}>
	 */
	List<DocumentType> getDocTypeByDocTypeName(String docTypeName);

	/**
	 * An API to fetch the document type based on identifier.
	 * 
	 * @param identifier {@link String}, the document type identifier
	 * @return {@link DocumentType}
	 */
	DocumentType getDocTypeByIdentifier(String identifier);

	/**
	 * An API to insert the documentType object.
	 * 
	 * @param documentType {@link DocumentType}
	 */
	void insertDocumentType(DocumentType documentType);

	/**
	 * An API to update the documentType object.
	 * 
	 * @param documentType {@link DocumentType}
	 */
	void updateDocumentType(DocumentType documentType);

	/**
	 * An API to remove the documentType object.
	 * 
	 * @param documentType {@link DocumentType}
	 */
	void removeDocumentType(DocumentType documentType);

	/**
	 * API to evict a document type object.
	 * 
	 * @param documentType {@link DocumentType}
	 */
	void evict(DocumentType documentType);
	
	/**
	 * An API to fetch all DocumentType by batch class id.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @return List<{@link DocumentType}>
	 */
	List<DocumentType> getDocTypeByBatchClassIdentifier(final String batchClassIdentifier);
}
