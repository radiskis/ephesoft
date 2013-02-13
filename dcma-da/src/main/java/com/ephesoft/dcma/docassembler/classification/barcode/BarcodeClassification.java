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

package com.ephesoft.dcma.docassembler.classification.barcode;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.docassembler.DocumentAssembler;
import com.ephesoft.dcma.docassembler.DocumentAssemblerProperties;
import com.ephesoft.dcma.docassembler.classification.DocumentClassification;
import com.ephesoft.dcma.docassembler.classification.barcode.process.BarcodePageProcess;
import com.ephesoft.dcma.docassembler.constant.DocumentAssemblerConstants;

/**
 * This class does the actual processing on the basis of Barcode classification for document assembly. It will read all the pages of
 * the document type Unknown and then create new documents for all these pages found.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.docassembler.classification.DocumentClassification
 */
public class BarcodeClassification implements DocumentClassification {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BarcodeClassification.class);

	/**
	 * This method will process all the unclassified pages present at document type Unknown. Process every page one by one and create
	 * new documents.
	 * 
	 * @param documentAssembler {@link DocumentAssembler}
	 * @param batchInstanceIdentifier {@link Long}
	 * @param pluginPropertiesService {@link PluginPropertiesService}
	 * @throws DCMAApplicationException {@link DCMAApplicationException} If any invalid parameter found.
	 */
	@Override
	public final void processUnclassifiedPages(final DocumentAssembler documentAssembler, final String batchInstanceIdentifier,
			final PluginPropertiesService pluginPropertiesService) throws DCMAApplicationException {

		LOGGER.info("Setting all the fields for BarcodePageProcess.");

		BarcodePageProcess barcodePageProcess = new BarcodePageProcess();

		barcodePageProcess.setDocTypeService(documentAssembler.getDocTypeService());
		barcodePageProcess.setPluginPropertiesService(documentAssembler.getPluginPropertiesService());
		barcodePageProcess.setBatchInstanceIdentifier(batchInstanceIdentifier);
		barcodePageProcess.setBatchSchemaService(documentAssembler.getBatchSchemaService());
		barcodePageProcess.setBarcodeClassification(documentAssembler.getBarcodeClassification());

		LOGGER.info("Initializing properties...");
		String barcodeConfidence = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN,
				DocumentAssemblerProperties.DA_BARCODE_CONFIDENCE);
		String factoryClassification = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN,
				DocumentAssemblerProperties.DA_FACTORY_CLASS);
		LOGGER.info("Properties Initialized Successfully");

		barcodePageProcess.setBarcodeConfidence(barcodeConfidence);
		barcodePageProcess.setFactoryClassification(factoryClassification);

		// read all the pages of the document for document type
		// Unknown.
		List<Page> docPageInfo = barcodePageProcess.readAllPages();

		if (null != docPageInfo) {
			// create new document for pages that was found in the
			// batch.xml file for Unknown type document.
			List<Document> insertAllDocument = new ArrayList<Document>();
			barcodePageProcess.createDocForPages(insertAllDocument,docPageInfo,false);
		}

	}
	
	/**
	 * This method will process all the unclassified pages for web service API.
	 * 
	 * @param documentAssembler  {@link DocumentAssembler}
	 * @param docPageInfo {@link List<Page>}
	 * @param batchClassID {@link String}
	 * @param pluginPropertiesService {@link PluginPropertiesService}
	 * @return {@link List<Document>} 
	 * @throws DCMAApplicationException {@link DCMAApplicationException} If any invalid parameter found.
	 */

	public final List<Document> processUnclassifiedPagesAPI(List<Page> docPageInfo, final DocumentAssembler documentAssembler, final String batchClassID,
			final PluginPropertiesService pluginPropertiesService) throws DCMAApplicationException {

		LOGGER.info("Setting all the fields for BarcodePageProcess.");

		BarcodePageProcess barcodePageProcess = new BarcodePageProcess();

		barcodePageProcess.setDocTypeService(documentAssembler.getDocTypeService());
		barcodePageProcess.setPluginPropertiesService(documentAssembler.getPluginPropertiesService());
		barcodePageProcess.setBatchClassIdentifier(batchClassID);
		barcodePageProcess.setBatchSchemaService(documentAssembler.getBatchSchemaService());
		barcodePageProcess.setBarcodeClassification(documentAssembler.getBarcodeClassification());

		LOGGER.info("Initializing properties...");
		String barcodeConfidence = pluginPropertiesService.getPropertyValue(batchClassID, DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN,
				DocumentAssemblerProperties.DA_BARCODE_CONFIDENCE);
		String factoryClassification = pluginPropertiesService.getPropertyValue(batchClassID, DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN,
				DocumentAssemblerProperties.DA_FACTORY_CLASS);
		LOGGER.info("Properties Initialized Successfully");

		barcodePageProcess.setBarcodeConfidence(barcodeConfidence);
		barcodePageProcess.setFactoryClassification(factoryClassification);

		List<Document> insertAllDocument = new ArrayList<Document>();
		barcodePageProcess.createDocForPages(insertAllDocument,docPageInfo, true);
		return insertAllDocument;
	}
}
