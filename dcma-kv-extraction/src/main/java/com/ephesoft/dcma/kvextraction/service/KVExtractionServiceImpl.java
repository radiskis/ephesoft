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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.ExtractKVParams;
import com.ephesoft.dcma.batch.schema.HocrPages;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.annotation.PostProcess;
import com.ephesoft.dcma.core.annotation.PreProcess;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.kvextraction.KeyValueExtraction;
import com.ephesoft.dcma.util.BackUpFileService;

/**
 * This class performs the functions of extraction kv values from document, ocr files etc.
 * 
 * @author Ephesoft
 *
 */
public class KVExtractionServiceImpl implements KVExtractionService {

	/**
	 * An instance of Logger for proper logging using slf4j.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(KVExtractionServiceImpl.class);

	/**
	 * An instance of {@link KeyValueExtraction}.
	 */
	@Autowired
	private KeyValueExtraction keyValueExtraction;

	/**
	 * Instance of {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * To get the xml file before processing.
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 */
	@PreProcess
	public final void preProcess(final BatchInstanceID batchInstanceID, String pluginWorkflow) {
		Assert.notNull(batchInstanceID);
		final String batchInstanceIdentifier = batchInstanceID.getID();
		BackUpFileService.backUpBatch(batchInstanceIdentifier, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));
	}

	/**
	 * To get the xml file after the processing.
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 */
	@PostProcess
	public final void postProcess(final BatchInstanceID batchInstanceID, String pluginWorkflow) {
		Assert.notNull(batchInstanceID);
		final String batchInstanceIdentifier = batchInstanceID.getID();
		BackUpFileService.backUpBatch(batchInstanceIdentifier, pluginWorkflow, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));
	}

	/**
	 * This method is used to extract the data from scanned pages using key value extraction and insert the received data to the
	 * batch.xml file.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 * @throws DCMAException If not able to extract Key Value document level fields.
	 */
	@Override
	public void extractKVDocumentFields(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException {
		try {
			keyValueExtraction.extractFields(batchInstanceID.getID());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new DCMAException(e.getMessage(), e);
		}
	}

	/**
	 * This API is called from web services to perform KV extraction on the specified HOCR.
	 * @param updtDocList {@link List<DocField>}
	 * @param hocrPages {@link HocrPages}
	 * @param params {@link ExtractKVParams}
	 * @return boolean
	 * @throws DCMAException
	 */
	@Override
	public boolean extractKVDocumentFieldsFromHOCR(final List<DocField> updtDocList, final HocrPages hocrPages,
			final ExtractKVParams params) throws DCMAException {
		try {
			return keyValueExtraction.extractFieldsFromHOCR(updtDocList, hocrPages, params);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new DCMAException(e.getMessage(), e);
		}
	}

	/**
	 * This API is called from web services to perform KV extraction on the specified HOCR.
	 * @param updtDocList {@link List<DocField>}
	 * @param hocrPages {@link HocrPages}
	 * @param params {@link ExtractKVParams}
	 * @return bolean
	 * @throws DCMAException
	 */
	@Override
	public boolean extractKVFromHOCRForBatchClass(List<DocField> updtDocList, HocrPages hocrPages, String batchClassIdentifier,
			String documentType) throws DCMAException {
		try {
			return keyValueExtraction.extractFromHOCRForBatchClass(updtDocList, hocrPages, batchClassIdentifier, documentType);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new DCMAException(e.getMessage(), e);
		}
	}

}
