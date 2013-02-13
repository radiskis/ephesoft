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

package com.ephesoft.dcma.kvfieldcreation.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.annotation.PostProcess;
import com.ephesoft.dcma.core.annotation.PreProcess;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.da.domain.KVExtraction;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.kvfieldcreation.KVFieldCreator;
import com.ephesoft.dcma.util.BackUpFileService;

/**
 * This class is used to create key value field for every DLF of document.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.kvfieldcreation.service.KVFieldCreatorService
 */
public class KVFieldCreatorServiceImpl implements KVFieldCreatorService, ICommonConstants {

	/**
	 * An instance of Logger for proper logging in the class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(KVFieldCreatorServiceImpl.class);

	/**
	 * An instance of {@link KVFieldCreator}.
	 */
	@Autowired
	private KVFieldCreator kvFieldCreator;

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
	public void preProcess(final BatchInstanceID batchInstanceID, String pluginWorkflow) {
		Assert.notNull(batchInstanceID);
		final String batchInstanceIdentifier = batchInstanceID.getID();
		BackUpFileService.backUpBatch(batchInstanceIdentifier, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));
	}
	/**
	 * To get the xml file after processing.
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 */
	@PostProcess
	public void postProcess(final BatchInstanceID batchInstanceID, String pluginWorkflow) {
		Assert.notNull(batchInstanceID);
		final String batchInstanceIdentifier = batchInstanceID.getID();
		BackUpFileService.backUpBatch(batchInstanceIdentifier, pluginWorkflow, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));
	}

	/**
	 * This method create kv fields for each document level field of each document.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 * @throws DCMAException if any error or exception occurs
	 */
	@Override
	public void createKeyValueField(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException {
		try {
			LOGGER.info("Start execution of key value learning .");
			kvFieldCreator.createKeyValueFields(batchInstanceID.getID(), pluginWorkflow);
			LOGGER.info("End execution of key value learning .");
		} catch (Exception e) {
			throw new DCMAException(e.getMessage(), e);
		}
	}

	/**
	 * Method to give the list of possible KV Field patterns from the given HOCR and the Value.
	 * 
	 * @param value {@link String}
	 * @param hocrPage {@link com.ephesoft.dcma.batch.schema.HocrPages.HocrPage}
	 * @return {@link List<KVExtraction>}
	 * @throws DCMAException
	 */
	@Override
	public List<KVExtraction> createKeyValueFieldAPI(String value, HocrPage hocrPage) throws DCMAException {
		try {
			LOGGER.info("Start execution of key value learning for value:" + value);
			return kvFieldCreator.createKeyValueFieldsAPI(value, hocrPage);
		} catch (Exception e) {
			throw new DCMAException(e.getMessage(), e);
		}
	}
}
