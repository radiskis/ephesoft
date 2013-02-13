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

package com.ephesoft.dcma.docassembler.classification.automatic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.docassembler.DocumentAssembler;
import com.ephesoft.dcma.docassembler.DocumentAssemblerProperties;
import com.ephesoft.dcma.docassembler.classification.DocumentClassification;
import com.ephesoft.dcma.docassembler.classification.automatic.process.AutomaticPageProcess;
import com.ephesoft.dcma.docassembler.constant.DocumentAssemblerConstants;

/**
 * This class does the actual processing on the basis of Automatic classification for document assembly. It will read all the pages of the
 * document type Unknown and then create new documents for all these pages found.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.docassembler.classification.DocumentClassification
 */
public class AutomaticClassification implements DocumentClassification {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AutomaticClassification.class);

	/**
	 * This method will process all the unclassified pages present at document type Unknown. Process every page one by one and create
	 * new documents.
	 * 
	 * @param documentAssembler {@link DocumentAssembler}
	 * @param batchInstanceID {@link String}
	 * @param pluginPropertiesService {@link PluginPropertiesService}
	 * @throws DCMAApplicationException {@link DCMAApplicationException} If any invalid parameter found.
	 */
	@Override
	public final void processUnclassifiedPages(final DocumentAssembler documentAssembler, final String batchInstanceID,
			final PluginPropertiesService pluginPropertiesService) throws DCMAApplicationException {

		LOGGER.info("Setting all the fields for AutomaticPageProcess.");

		AutomaticPageProcess automaticPageProcess = new AutomaticPageProcess();
		
		String barcodeConfidence = pluginPropertiesService.getPropertyValue(batchInstanceID, DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN,
				DocumentAssemblerProperties.DA_BARCODE_CONFIDENCE);

		automaticPageProcess.setPageTypeService(documentAssembler.getPageTypeService());
		automaticPageProcess.setPluginPropertiesService(documentAssembler.getPluginPropertiesService());
		automaticPageProcess.setBatchInstanceID(batchInstanceID);
		automaticPageProcess.setBatchSchemaService(documentAssembler.getBatchSchemaService());

		Map<String, String> propertyMap = new HashMap<String, String>();
		propertyMap.put(DocumentAssemblerConstants.AUTOMATIC_CLASSIFICATION, documentAssembler.getAutomaticClassification());
		propertyMap.put(DocumentAssemblerConstants.CHECK_FIRST_PAGE, documentAssembler.getCheckFirstPage());
		propertyMap.put(DocumentAssemblerConstants.CHECK_MIDDLE_PAGE, documentAssembler.getCheckMiddlePage());
		propertyMap.put(DocumentAssemblerConstants.CHECK_LAST_PAGE, documentAssembler.getCheckLastPage());
		propertyMap.put(DocumentAssemblerConstants.AUTOMATIC_INCLUDE_LIST, documentAssembler.getAutomaticIncludeList());

		LOGGER.info("Initializing properties...");
		String factoryClassification = pluginPropertiesService.getPropertyValue(batchInstanceID, DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN,
				DocumentAssemblerProperties.DA_FACTORY_CLASS);
		String ruleFMLPage = pluginPropertiesService.getPropertyValue(batchInstanceID, DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN,
				DocumentAssemblerProperties.DA_RULE_FP_MP_LP);
		String ruleFPage = pluginPropertiesService.getPropertyValue(batchInstanceID, DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN,
				DocumentAssemblerProperties.DA_RULE_FP);
		String ruleMPage = pluginPropertiesService.getPropertyValue(batchInstanceID, DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN,
				DocumentAssemblerProperties.DA_RULE_MP);
		String ruleLPage = pluginPropertiesService.getPropertyValue(batchInstanceID, DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN,
				DocumentAssemblerProperties.DA_RULE_LP);
		String ruleFLPage = pluginPropertiesService.getPropertyValue(batchInstanceID, DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN,
				DocumentAssemblerProperties.DA_RULE_FP_LP);
		String ruleMLPage = pluginPropertiesService.getPropertyValue(batchInstanceID, DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN,
				DocumentAssemblerProperties.DA_RULE_MP_LP);
		String ruleFMPage = pluginPropertiesService.getPropertyValue(batchInstanceID, DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN,
				DocumentAssemblerProperties.DA_RULE_FP_MP);
		LOGGER.info("Properties Initialized Successfully");

		propertyMap.put(DocumentAssemblerConstants.FACTORY_CLASSIFICATION, factoryClassification);
		propertyMap.put(DocumentAssemblerConstants.RULE_FL_PAGE, ruleFLPage);
		propertyMap.put(DocumentAssemblerConstants.RULE_FML_PAGE, ruleFMLPage);
		propertyMap.put(DocumentAssemblerConstants.RULE_FM_PAGE, ruleFMPage);
		propertyMap.put(DocumentAssemblerConstants.RULE_F_PAGE, ruleFPage);
		propertyMap.put(DocumentAssemblerConstants.RULE_L_PAGE, ruleLPage);
		propertyMap.put(DocumentAssemblerConstants.RULE_ML_PAGE, ruleMLPage);
		propertyMap.put(DocumentAssemblerConstants.RULE_M_PAGE, ruleMPage);

		automaticPageProcess.setPropertyMap(propertyMap);
		automaticPageProcess.setBarcodeConfidence(barcodeConfidence);
		automaticPageProcess.setBarcodeClassification(documentAssembler.getBarcodeClassification());
		// read all the pages of the document for document type
		// Unknown.
		List<Page> docPageInfo = automaticPageProcess.readAllPages();

		if (null != docPageInfo) {
			// create new document for pages that was found in the
			// batch.xml file for Unknown type document.
			automaticPageProcess.createDocForPages(docPageInfo);
		}

	}

}
