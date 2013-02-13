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

package com.ephesoft.dcma.docassembler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.service.DocumentTypeService;
import com.ephesoft.dcma.da.service.PageTypeService;
import com.ephesoft.dcma.docassembler.classification.DocumentClassification;
import com.ephesoft.dcma.docassembler.classification.barcode.BarcodeClassification;
import com.ephesoft.dcma.docassembler.classification.engine.SearchClassification;
import com.ephesoft.dcma.docassembler.classification.image.ImageClassification;
import com.ephesoft.dcma.docassembler.constant.DocumentAssemblerConstants;
import com.ephesoft.dcma.docassembler.factory.DocumentClassificationFactory;

/**
 * This class retrieves a proper classification (Barcode, Image and Search classification) based on factory classification specified in
 * resources.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.docassembler.service.DocumentAssemblerServiceImpl
 * @see com.ephesoft.dcma.docassembler.classification.DocumentClassification
 */
@Component
public class DocumentAssembler {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentAssembler.class);

	/**
	 * barcode Classification.
	 */
	private String barcodeClassification;

	/**
	 * lucene Classification.
	 */
	private String luceneClassification;

	/**
	 * image Classification.
	 */
	private String imageClassification;

	/**
	 * automatic Classification.
	 */
	private String automaticClassification;

	/**
	 * automatic Classification. inclusion list
	 */
	private String automaticIncludeList;

	/**
	 * check first page.
	 */
	private String checkFirstPage;

	/**
	 * check first page.
	 */
	private String checkMiddlePage;

	/**
	 * check first page.
	 */
	private String checkLastPage;

	/**
	 * Reference of BatchSchemaService.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Reference of PageTypeService.
	 */
	@Autowired
	private PageTypeService pageTypeService;

	/**
	 * Instance of PluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * Instance of PluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchClassPluginPropertiesService")
	private PluginPropertiesService bcPluginPropertiesService;

	/**
	 * docTypeService DocumentTypeService.
	 */
	@Autowired
	private DocumentTypeService docTypeService;

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
	 * To get Page Type Service.
	 * @return the pageTypeService
	 */
	public final PageTypeService getPageTypeService() {
		return pageTypeService;
	}

	/**
	 * To set Page Type Service.
	 * @param pageTypeService PageTypeService
	 */
	public final void setPageTypeService(final PageTypeService pageTypeService) {
		this.pageTypeService = pageTypeService;
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
	 * @param barcodeClassification String
	 */
	public final void setBarcodeClassification(final String barcodeClassification) {
		this.barcodeClassification = barcodeClassification;
	}

	/**
	 * To get Lucene Classification.
	 * @return the luceneClassification
	 */
	public final String getLuceneClassification() {
		return luceneClassification;
	}

	/**
	 * To set Lucene Classification.
	 * @param luceneClassification String
	 */
	public final void setLuceneClassification(final String luceneClassification) {
		this.luceneClassification = luceneClassification;
	}

	/**
	 * To get Image Classification.
	 * @return the imageClassification
	 */
	public final String getImageClassification() {
		return imageClassification;
	}

	/**
	 * To set Image Classification.
	 * @param imageClassification String
	 */
	public final void setImageClassification(final String imageClassification) {
		this.imageClassification = imageClassification;
	}

	/**
	 * To get Automatic Include List.
	 * @return the automaticClassification inclusion list
	 */
	public String getAutomaticIncludeList() {
		return automaticIncludeList;
	}

	/**
	 * To set Automatic Include List.
	 * @param automaticIncludeList String
	 */
	public void setAutomaticIncludeList(String automaticIncludeList) {
		this.automaticIncludeList = automaticIncludeList;
	}

	/**
	 * To get Automatic Classification.
	 * @return the automaticClassification
	 */
	public String getAutomaticClassification() {
		return automaticClassification;
	}

	/**
	 * To set Automatic Classification.
	 * @param automaticClassification String
	 */
	public void setAutomaticClassification(String automaticClassification) {
		this.automaticClassification = automaticClassification;
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
	 * To get Check First Page.
	 * @return the checkFirstPage
	 */
	public final String getCheckFirstPage() {
		return checkFirstPage;
	}

	/**
	 * To set Check First Page.
	 * @param checkFirstPage String
	 */
	public final void setCheckFirstPage(final String checkFirstPage) {
		this.checkFirstPage = checkFirstPage;
	}

	/**
	 * To get Check Middle Page.
	 * @return the checkMiddlePage
	 */
	public final String getCheckMiddlePage() {
		return checkMiddlePage;
	}

	/**
	 * To set Check Middle Page.
	 * @param checkMiddlePage String
	 */
	public final void setCheckMiddlePage(final String checkMiddlePage) {
		this.checkMiddlePage = checkMiddlePage;
	}

	/**
	 * To get Check last Page.
	 * @return the checkLastPage
	 */
	public final String getCheckLastPage() {
		return checkLastPage;
	}

	/**
	 * To set Check last Page.
	 * @param checkLastPage String
	 */
	public final void setCheckLastPage(final String checkLastPage) {
		this.checkLastPage = checkLastPage;
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
	 * @param pluginPropertiesService PluginPropertiesService
	 */
	public void setPluginPropertiesService(PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	/**
	 * This method will create new documents for all the pages of the document type Unknown.
	 * 
	 * @param batchInstanceID String
	 * @throws DCMAApplicationException Check for input parameters, read all pages of document and create document for every page.
	 */
	public final void createDocument(final String batchInstanceID) throws DCMAApplicationException {

		// Check all the input parameters and fields loaded from property file.
		checkInputParams(batchInstanceID);

		// TODO read this from work flow.
		String factoryClassification = pluginPropertiesService.getPropertyValue(batchInstanceID, DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN,
				DocumentAssemblerProperties.DA_FACTORY_CLASS);
		
		DocumentClassification documentClassification = DocumentClassificationFactory.getDocumentClassification(factoryClassification);

		// TODO read this from work flow.

		documentClassification.processUnclassifiedPages(this, batchInstanceID, pluginPropertiesService);

	}
	
	/**
	 * This method will create new documents for all the pages of the document type Unknown.
	 * 
	 * @param batchClassID {@link String}
	 * @param classType {@link DocumentClassificationFactory}
	 * @param docPageInfo {@link List<Page>}
	 * @return {@link List<Document>}
	 * @throws DCMAApplicationException {@link DCMAApplicationException} Check for input parameters, read all pages of document and create document for every page.
	 */
	public final List<Document> createDocumentAPI(final DocumentClassificationFactory classType, final String batchClassID, final List<Page> docPageInfo) throws DCMAApplicationException {
		List<Document> doc = null;
		DocumentClassification documentClassification = DocumentClassificationFactory.getDocumentClassification(classType.getNameClassification());
		if(classType.compareTo(DocumentClassificationFactory.IMAGE) == 0){
			ImageClassification imgClassification = (ImageClassification)documentClassification;
			doc = imgClassification.processUnclassifiedPagesAPI(docPageInfo, this, batchClassID, bcPluginPropertiesService);
		} else if(classType.compareTo(DocumentClassificationFactory.SEARCHCLASSIFICATION) == 0) {
			SearchClassification searchClassification = (SearchClassification)documentClassification;
			doc =  searchClassification.processUnclassifiedPagesAPI(docPageInfo, this, batchClassID, bcPluginPropertiesService);
		} else if(classType.compareTo(DocumentClassificationFactory.BARCODE) == 0) {
			BarcodeClassification barcodeClassification = (BarcodeClassification)documentClassification;
			doc =  barcodeClassification.processUnclassifiedPagesAPI(docPageInfo, this, batchClassID, bcPluginPropertiesService);
		}
		return doc;
	}

	/**
	 * This method will check all the input parameters for document assembly.
	 * 
	 * @param batchInstanceID {@link String}
	 * @throws DCMAApplicationException {@link DCMAApplicationException} Check input parameters.
	 */
	private void checkInputParams(final String batchInstanceID) throws DCMAApplicationException {

		String errorMsg = null;

		LOGGER.info("Checking all the fields loaded from peroperty files.");

		// check for batch instance ID.
		if (null == batchInstanceID || "".equals(batchInstanceID)) {
			errorMsg = "Invalid argument batchInstanceID.";
			LOGGER.error(errorMsg);
			throw new DCMAApplicationException(errorMsg);
		}

		LOGGER.info("Input batch instance ID for Document Assembler is " + batchInstanceID);

		// check for barcode Classification name.
		if (null == this.barcodeClassification || DocumentAssemblerConstants.EMPTY.equals(this.barcodeClassification)) {
			errorMsg = "Invalid initalization of barcodeClassification with properties file.";
			LOGGER.error(errorMsg);
			throw new DCMAApplicationException(errorMsg);
		}

		// check for lucene Classification name.
		if (null == this.luceneClassification || DocumentAssemblerConstants.EMPTY.equals(this.luceneClassification)) {
			errorMsg = "Invalid initalization of luceneClassification with properties file.";
			LOGGER.error(errorMsg);
			throw new DCMAApplicationException(errorMsg);
		}

		// check for image Classification name.
		if (null == this.imageClassification || DocumentAssemblerConstants.EMPTY.equals(this.imageClassification)) {
			errorMsg = "Invalid initalization of imageClassification with properties file.";
			LOGGER.error(errorMsg);
			throw new DCMAApplicationException(errorMsg);
		}

		// check for automatic Classification name.
		if (null == this.automaticClassification || DocumentAssemblerConstants.EMPTY.equals(this.automaticClassification)) {
			errorMsg = "Invalid initalization of automaticClassification with properties file.";
			LOGGER.error(errorMsg);
			throw new DCMAApplicationException(errorMsg);
		}
	}
}
