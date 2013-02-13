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

package com.ephesoft.dcma.docassembler.classification.barcode.process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Batch.DocumentClassificationTypes;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.schema.Page.PageLevelFields;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.EphesoftProperty;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.service.DocumentTypeService;
import com.ephesoft.dcma.docassembler.constant.DocumentAssemblerConstants;

/**
 * This class will process every page present at document type Unknown. This will read all the pages one by one and basis of the
 * barcode it will create new documents and delete the current page from the document type Unknown.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.docassembler.DocumentAssembler
 */
public class BarcodePageProcess {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BarcodePageProcess.class);

	/**
	 * docTypeService DocumentTypeService.
	 */
	private DocumentTypeService docTypeService;

	/**
	 * Instance of PluginPropertiesService.
	 */
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * xmlDocuments List<DocumentType>.
	 */
	private List<Document> xmlDocuments;

	/**
	 * barcode Classification.
	 */
	private String barcodeClassification;

	/**
	 * barcode Confidence score.
	 */
	private String barcodeConfidence;

	/**
	 * Batch instance ID.
	 */
	private String batchInstanceIdentifier;

	/**
	 * Batch Class ID.
	 */
	private String batchClassIdentifier;
	/**
	 * Reference of BatchSchemaService.
	 */
	private BatchSchemaService batchSchemaService;

	/**
	 * Name of the factory classification.
	 */
	private String factoryClassification;

	/**
	 * To get Batch Schema Service.
	 * @return the batchSchemaService
	 */
	public final BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * To set Batch Schema Service.
	 * @param batchSchemaService BatchSchemaService
	 */
	public final void setBatchSchemaService(final BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * To get Doc Type Service.
	 * @return the docTypeService
	 */
	public final DocumentTypeService getDocTypeService() {
		return docTypeService;
	}

	/**
	 * To set Doc Type Service.
	 * @param docTypeService DocumentTypeService
	 */
	public final void setDocTypeService(final DocumentTypeService docTypeService) {
		this.docTypeService = docTypeService;
	}

	/**
	 * To get Xml Documents.
	 * @return List<DocumentType>
	 */
	public final List<Document> getXmlDocuments() {
		return xmlDocuments;
	}

	/**
	 * To set Xml Documents.
	 * @param xmlDocuments List<DocumentType>
	 */
	public final void setXmlDocuments(final List<Document> xmlDocuments) {
		this.xmlDocuments = xmlDocuments;
	}

	/**
	 * To get Batch Instance Identifier.
	 * @return batchInstanceID
	 */
	public final String getBatchInstanceIdentifier() {
		return this.batchInstanceIdentifier;
	}

	/**
	 * To set Batch Instance Identifier.
	 * @param batchInstanceIdentifier {@link String}
	 */
	public final void setBatchInstanceIdentifier(String batchInstanceIdentifier) {
		this.batchInstanceIdentifier = batchInstanceIdentifier;
	}

	/**
	 * To set Batch Class Identifier.
	 * @param batchClassIdentifier {@link String}
	 */
	public void setBatchClassIdentifier(String batchClassIdentifier) {
		this.batchClassIdentifier = batchClassIdentifier;
	}

	/**
	 * To get Batch Class Identifier.
	 * @return batch class identifier.
	 */
	public String getBatchClassIdentifier() {
		return batchClassIdentifier;
	}

	/**
	 * To get Barcode Classification.
	 * @return the barcodeClassification
	 */
	public final String getBarcodeClassification() {
		return barcodeClassification;
	}

	/**
	 * To set Barcode Classification.
	 * @param barcodeClassification
	 */
	public final void setBarcodeClassification(final String barcodeClassification) {
		this.barcodeClassification = barcodeClassification;
	}

	/**
	 * To get Barcode Confidence.
	 * @return the barcodeConfidence
	 */
	public final String getBarcodeConfidence() {
		return barcodeConfidence;
	}

	/**
	 * To set Barcode Confidence.
	 * @param barcodeConfidence 
	 */
	public final void setBarcodeConfidence(final String barcodeConfidence) {
		this.barcodeConfidence = barcodeConfidence;
	}

	/**
	 * To get Factory Classification.
	 * @return the factoryClassification
	 */
	public String getFactoryClassification() {
		return factoryClassification;
	}

	/**
	 * To set Factory Classification.
	 * @param factoryClassification
	 */
	public void setFactoryClassification(String factoryClassification) {
		this.factoryClassification = factoryClassification;
	}

	/**
	 * To get Plugin Properties Service.
	 * @return the pluginPropertiesService
	 */
	public PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	/**
	 * To set Plugin Properties Service.
	 * @param pluginPropertiesService
	 */
	public void setPluginPropertiesService(PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	/**
	 * This method return the document type name for the page type name input..
	 * 
	 * @param pageTypeName {@link String}
	 * @return {@link String} docTypeName which is present in data base table document_type for the corresponding page type name.
	 */
	public final String getDocTypeName(final String pageTypeName) {

		String docTypeName = null;

		List<com.ephesoft.dcma.da.domain.DocumentType> docTypeList = pluginPropertiesService.getDocTypeByPageTypeName(
				batchInstanceIdentifier, pageTypeName);

		if (null == docTypeList || docTypeList.isEmpty()) {
			LOGGER.error("No document type name found for the page type name : " + pageTypeName);
		} else {
			com.ephesoft.dcma.da.domain.DocumentType docType = docTypeList.get(0);
			docTypeName = docType.getName();
		}

		return docTypeName;
	}

	/**
	 * This method return the document type name for the page type name input from the batch class id.
	 * 
	 * @param pageTypeName {@link String}
	 * @param batchClassID {@link String}
	 * @return String docTypeName which is present in data base table document_type for the corresponding page type name.
	 */
	public final String getDocTypeNameAPI(final String batchClassID, final String pageTypeName) {

		String docTypeName = null;

		List<com.ephesoft.dcma.da.domain.DocumentType> docTypeList = docTypeService.getDocTypeByBatchClassIdentifier(batchClassID, -1,
				-1);

		String docTypeNameTemp = "";
		if (null == docTypeList || docTypeList.isEmpty()) {
			LOGGER.error("No document type name found for the page type name : " + pageTypeName);
		} else {

			final Iterator<com.ephesoft.dcma.da.domain.DocumentType> itr = docTypeList.iterator();
			while (itr.hasNext()) {
				final com.ephesoft.dcma.da.domain.DocumentType docTypeDB = itr.next();
				docTypeNameTemp = docTypeDB.getName();
				if (pageTypeName.contains(docTypeNameTemp)) {
					docTypeName = docTypeNameTemp;
					LOGGER.debug("DocumentType name : " + docTypeName);
					break;
				}
			}
		}
		return docTypeName;
	}

	/**
	 * This method will create new document for pages that was found in the batch.xml file for Unknown type document.
	 * 
	 * @param insertAllDocument	{@link List<Document>}  
	 * @param docPageInfo {@link List<Page>}
	 * @param isFromWebService boolean 
	 * @throws DCMAApplicationException {@link DCMAApplicationException} Check for input parameters, create new documents for page found in document type Unknown.
	 */
	public final void createDocForPages(List<Document> insertAllDocument, final List<Page> docPageInfo, final boolean isFromWebService)
			throws DCMAApplicationException {

		String errMsg = null;

		if (!isFromWebService && null == this.xmlDocuments) {
			throw new DCMAApplicationException("Unable to write pages for the document.");
		}

		int confidenceValue = 0;

		try {
			confidenceValue = Integer.parseInt(getBarcodeConfidence());
		} catch (NumberFormatException nfe) {
			errMsg = "Invalid integer for barcode confidence score in properties file." + nfe.getMessage();
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg, nfe);

		}

		try {
			String previousValue = DocumentAssemblerConstants.EMPTY;

			Document document = null;
			Long idGenerator = 0L;
			List<Integer> removeIndexList = new ArrayList<Integer>();

			for (int index = 0; index < docPageInfo.size(); index++) {

				Page pgType = docPageInfo.get(index);
				String value = getPgLevelFdValue(pgType);

				if (null == value) {
					errMsg = "Invalid format of page level fields. Value found for " + getBarcodeClassification()
							+ " classification is null.";
					throw new DCMAApplicationException(errMsg);
				}

				if (value.equals(DocumentAssemblerConstants.EMPTY)) {
					// This if is to check the first continuous empty names for
					// all the pages. donot do any thing if the first name is
					// empty.
					if (previousValue.equals(DocumentAssemblerConstants.EMPTY)) {
						continue;
					} else {
						document.getPages().getPage().add(pgType);
						removeIndexList.add(index);
					}
				} else {
					previousValue = value;
					String docTypeName = null;
					if (isFromWebService) {
						docTypeName = getDocTypeNameAPI(batchClassIdentifier, previousValue);
					} else {
						docTypeName = getDocTypeName(previousValue);
					}
					if (docTypeName == null || docTypeName.isEmpty()) {
						errMsg = "DocumentType name is not found in the data base for the page type name : " + previousValue;
						LOGGER.info(errMsg);
						throw new DCMAApplicationException(errMsg);
					} else {
						document = new Document();
						document.setType(docTypeName);
						float minConfThreshold = getMinConfThreshold(previousValue);
						document.setConfidenceThreshold(minConfThreshold);
						Pages pages = new Pages();
						List<Page> listOfPages = pages.getPage();
						listOfPages.add(pgType);
						idGenerator++;
						document.setIdentifier(EphesoftProperty.DOCUMENT.getProperty() + idGenerator);
						document.setDocumentDisplayInfo(BatchConstants.EMPTY);
						LOGGER.info("Page confidence value is : " + confidenceValue);
						document.setConfidence(confidenceValue);
						document.setPages(pages);
						insertAllDocument.add(document);
						removeIndexList.add(index);
					}
				}
			}

			if (isFromWebService) {
				updateBatchXMLAPI(insertAllDocument);
			} else {
				// update the xml file.
				updateBatchXML(insertAllDocument, removeIndexList);
			}

		} catch (Exception e) {
			errMsg = "Unable to write pages for the document. " + e.getMessage();
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg, e);
		}
	}

	/**
	 * This method return the document minimum confidence threshold for the page type name input.
	 * 
	 * @param pageTypeName {@link String}
	 * @return float minConfThreshold which is present in data base table document_type for the corresponding page type name.
	 */
	private float getMinConfThreshold(final String pageTypeName) {
		float minConfThreshold = 0;

		List<com.ephesoft.dcma.da.domain.DocumentType> docTypeList = pluginPropertiesService.getDocTypeByPageTypeName(
				batchInstanceIdentifier, pageTypeName);

		if (null == docTypeList || docTypeList.isEmpty()) {
			LOGGER.error("No document type name found for the page type name : " + pageTypeName);
		} else {
			com.ephesoft.dcma.da.domain.DocumentType docType = docTypeList.get(0);
			minConfThreshold = docType.getMinConfidenceThreshold();
		}

		return minConfThreshold;
	}

	/**
	 * Update Batch XML file.
	 * 
	 * @param insertAllDocument {@link List<DocumentType>}
	 * @param removeIndexList {@link List<Integer>}
	 * @throws DCMAApplicationException {@link DCMAApplicationException} Check for input parameters, update the batch xml.
	 */
	private void updateBatchXML(final List<Document> insertAllDocument, final List<Integer> removeIndexList)
			throws DCMAApplicationException {

		String errMsg = null;

		Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);

		List<Document> xmlDocuments = batch.getDocuments().getDocument();
		if (null != xmlDocuments && null != insertAllDocument && !insertAllDocument.isEmpty()) {
			xmlDocuments.addAll(insertAllDocument);
		}

		List<Page> docPageInfo = null;

		int indexDocType = -1;

		for (int i = 0; i < xmlDocuments.size(); i++) {
			final Document document = xmlDocuments.get(i);
			String docType = document.getType();
			if (null != docType && docType.equals(EphesoftProperty.UNKNOWN.getProperty())) {
				docPageInfo = document.getPages().getPage();
				indexDocType = i;
				break;
			}
		}

		if (!removeIndexList.isEmpty()) {
			for (int index = removeIndexList.size() - 1; index >= 0; index--) {
				int rIndex = removeIndexList.get(index);
				if (rIndex < docPageInfo.size()) {
					docPageInfo.remove(rIndex);
				} else {
					errMsg = "Invalid argument for removal of pages for document type : " + EphesoftProperty.UNKNOWN.getProperty();
					LOGGER.error(errMsg);
					throw new DCMAApplicationException(errMsg);
				}
			}
		}

		// Delete the document type "Unknown" if no more pages are present.
		if (indexDocType != -1 && null != docPageInfo && docPageInfo.isEmpty()) {
			xmlDocuments.remove(indexDocType);
		}

		// setting the batch level documentClassificationTypes.
		DocumentClassificationTypes documentClassificationTypes = new DocumentClassificationTypes();
		List<String> documentClassificationType = documentClassificationTypes.getDocumentClassificationType();
		documentClassificationType.add(getFactoryClassification());
		batch.setDocumentClassificationTypes(documentClassificationTypes);

		// Set the error message explicitly to blank to display the node in batch xml
		for (int i = 0; i < xmlDocuments.size(); i++) {
			final Document document = xmlDocuments.get(i);
			document.setErrorMessage("");
		}
		// now write the state of the object to the xml file.
		batchSchemaService.updateBatch(batch);

		LOGGER.info("updateBatchXML done.");

	}

	/**
	 * Update Batch XML file.
	 * 
	 * @param insertAllDocument List<DocumentType>
	 * @throws DCMAApplicationException {@link DCMAApplicationException} Check for input parameters, update the batch xml.
	 */
	private void updateBatchXMLAPI(final List<Document> insertAllDocument) throws DCMAApplicationException {
		// setting the batch level documentClassificationTypes.
		DocumentClassificationTypes documentClassificationTypes = new DocumentClassificationTypes();
		List<String> documentClassificationType = documentClassificationTypes.getDocumentClassificationType();
		documentClassificationType.add(getFactoryClassification());

		// Set the error message explicitly to blank to display the node in batch xml
		for (int i = 0; i < insertAllDocument.size(); i++) {
			final Document document = insertAllDocument.get(i);
			document.setErrorMessage("");
		}
		LOGGER.info("updateBatchXML for web services done.");
	}

	/**
	 * This method will retrieve the page level field type value for any input page type for current classification name.
	 * 
	 * @param pgType {@link PageType}
	 * @return value {@link String}
	 */
	private String getPgLevelFdValue(final Page pgType) {

		String value = null;
		String name = null;
		if (null != pgType) {
			PageLevelFields pageLevelFields = pgType.getPageLevelFields();
			if (null != pageLevelFields) {
				List<DocField> pageLevelField = pageLevelFields.getPageLevelField();
				if (null != pageLevelFields) {
					for (DocField docFdType : pageLevelField) {
						if (null != docFdType) {
							name = docFdType.getName();
							if (null != name && name.contains(getBarcodeClassification())) {
								value = docFdType.getValue();
							}
						}
					}
				}
			}
		}

		return value;

	}

	/**
	 * This method will read all the pages of the document for document type Unknown.
	 * 
	 * @return docPageInfo {@link List<PageType>}
	 * @throws DCMAApplicationException {@link DCMAApplicationException} Check for input parameters and read all pages of the document.
	 */
	public final List<Page> readAllPages() throws DCMAApplicationException {
		LOGGER.info("Reading the document for Document Assembler.");
		List<Page> docPageInfo = null;

		final Batch pasrsedXMLFile = batchSchemaService.getBatch(this.batchInstanceIdentifier);

		this.xmlDocuments = pasrsedXMLFile.getDocuments().getDocument();

		for (int i = 0; i < this.xmlDocuments.size(); i++) {
			final Document document = this.xmlDocuments.get(i);
			String docType = document.getType();
			if (null != docType && docType.equals(EphesoftProperty.UNKNOWN.getProperty())) {
				docPageInfo = document.getPages().getPage();
				break;
			}
		}

		return docPageInfo;
	}

}
