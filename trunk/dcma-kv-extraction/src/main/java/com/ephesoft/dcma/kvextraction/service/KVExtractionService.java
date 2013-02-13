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

package com.ephesoft.dcma.kvextraction.service;

import java.util.List;

import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.ExtractKVParams;
import com.ephesoft.dcma.batch.schema.HocrPages;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.da.id.BatchInstanceID;

/**
 * This service is used to extract the data from scanned pages using key value extraction and insert the received data as a document
 * level fields to the batch.xml file. Data will be added to the document level fields. If the more than one data is found then values
 * are added to the alternate value of document level fields.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.kvextraction.service.KVExtractionServiceImpl
 */
public interface KVExtractionService {

	/**
	 * This method is used to extract the data from scanned pages using key value extraction and insert the received data to the
	 * batch.xml file.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 * @throws DCMAException If not able to extract Key Value document level fields.
	 */
	void extractKVDocumentFields(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException;

	/**
	 * This API is called from web services to perform KV extraction on the specified HOCR.
	 * @param updtDocList {@link List<DocField>}
	 * @param hocrPages {@link HocrPages}
	 * @param params {@link ExtractKVParams}
	 * @return boolean
	 * @throws DCMAException
	 */
	boolean extractKVDocumentFieldsFromHOCR(List<DocField> updtDocList, HocrPages hocrPages, ExtractKVParams params)
			throws DCMAException;
	
	/**
	 * This API is called from web services to perform KV extraction on the specified HOCR.
	 * @param updtDocList {@link List<DocField>}
	 * @param hocrPages {@link HocrPages}
	 * @param params {@link ExtractKVParams}
	 * @return bolean
	 * @throws DCMAException
	 */
	boolean extractKVFromHOCRForBatchClass(List<DocField> updtDocList, HocrPages hocrPages, String batchClassIdentifier, String documentType)
			throws DCMAException;

}
