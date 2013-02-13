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

package com.ephesoft.dcma.barcodeextraction;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.barcodeextraction.constant.BarcodeExtractionConstant;
import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.FieldType;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.da.service.FieldTypeService;

/**
 * This class reads the bar-code on each image file fetched from batch.xml, processes images with Zxing library to find the barcode
 * information for each image and writes the extracted information to batch xml to be used by subsequent plug ins.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.component.ICommonConstants
 * @see com.ephesoft.dcma.batch.service.BatchSchemaService
 * 
 */
@Component
public class BarcodeExtractionReader implements ICommonConstants {

	/**
	 * Instance of {@link BatchSchemaService}.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * An instance of {@link FieldTypeService}.
	 */
	@Autowired
	@Qualifier("fieldTypeService")
	private FieldTypeService fieldTypeService;

	/**
	 * Instance of {@link BatchInstanceService} .
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * Instance of {@link PluginPropertiesService}.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * Instance of {@link PluginPropertiesService}.
	 */
	@Autowired
	@Qualifier("batchClassPluginPropertiesService")
	private PluginPropertiesService batchClassPluginPropertiesService;

	/**
	 * Instance of {@link BatchClassService}.
	 */
	@Autowired
	private BatchClassService batchClassService;

	
	/**
	 * Logger instance for logging using slf4j for logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BarcodeExtractionReader.class);

	/**
	 * /** Reference firstPage name.
	 */
	private transient String firstPage;

	/**
	 * @return the batchSchemaService
	 */
	public BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * First page name.
	 * 
	 * @return firstPage
	 */
	public final String getFirstPage() {
		return firstPage;
	}

	/**
	 * First page name.
	 * 
	 * @param firstPage String.
	 */
	public final void setFirstPage(final String firstPage) {
		this.firstPage = firstPage;
	}

	/**
	 * setter for BatchSchemaService.
	 * 
	 * @param batchSchemaService {@link BatchSchemaService}
	 */
	public void setBatchSchemaService(final BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * getter for PluginPropertiesService.
	 * @return {@link PluginPropertiesService} 
	 */
	public PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	/**
	 * setter for pluginPropertiesService.
	 * @param pluginPropertiesService {@link PluginPropertiesService}
	 */
	public void setPluginPropertiesService(final PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	/**
	 * This method updates the the batch xml for each image file and inserts page level field with attributes name, value, coordinates
	 * and Confidence score.
	 * @param barcodeExecutorList {@link List <BarcodeExtractionExecutor>}
	 * @param batch {@link Batch}
	 * @param maxConfidence {@link String}
	 * @param minConfidence {@link String}
	 * @param batchInstanceId {@link String}
	 * @return List<DocField>
	 * @throws DCMAApplicationException {@link DCMAApplicationException}exception to be thrown
	 */
	public List<DocField> updateBatchXML(final List<BarcodeExtractionExecutor> barcodeExecutorList, final Batch batch,
			final String maxConfidence, final String minConfidence, final String batchInstanceId) throws DCMAApplicationException {
		final List<DocField> updtDocFdTyList = new ArrayList<DocField>();
		String errMsg = null;
		List<FieldType> allFdTypes = null;
		for (final BarcodeExtractionExecutor barcodeExtractionExecutor : barcodeExecutorList) {
			final Document document = barcodeExtractionExecutor.getDocument();
			final String docTypeName = document.getType();
			allFdTypes = pluginPropertiesService.getFieldTypes(batchInstanceId, docTypeName);
			if (docTypeName == null || docTypeName.isEmpty()) {
				errMsg = "Document type is null or empty";
				LOGGER.error(errMsg);
				continue;
			}
			if (allFdTypes == null || allFdTypes.isEmpty()) {
				errMsg = "Field type List is null or empty";
				LOGGER.error(errMsg);
				continue;
			}
			final BarcodeExtractionResult[] barCodeResults = barcodeExtractionExecutor.getBarCodeResults();
			if (barCodeResults != null && barCodeResults.length > 0) {
				for (final BarcodeExtractionResult barCodeResult : barCodeResults) {
					if (barCodeResult != null) {
						populateDocumentLevelFields(maxConfidence, minConfidence, updtDocFdTyList, allFdTypes,
								barcodeExtractionExecutor, barCodeResult);
					}
				}

			}
		}
		for (final FieldType fieldType : allFdTypes) {
			boolean isFieldTypeExists = false;
			for (final DocField updatedDocField : updtDocFdTyList) {
				if (updatedDocField.getName().equalsIgnoreCase(fieldType.getName())) {
					isFieldTypeExists = true;
				}
			}
			if (!isFieldTypeExists) {
				final DocField docFieldType = createDefaultDLF(minConfidence, fieldType);
				updtDocFdTyList.add(docFieldType);
			}
		}

		return updtDocFdTyList;
	}

	private DocField createDefaultDLF(final String minConfidence, final FieldType fieldType) {
		final DocField docFieldType = new DocField();
		docFieldType.setName(fieldType.getName());
		docFieldType.setValue("");
		docFieldType.setType(fieldType.getDataType().name());
		docFieldType.setConfidence(Integer.valueOf(minConfidence));
		final Coordinates coordinates = new Coordinates();
		coordinates.setX0(new BigInteger(BarcodeExtractionConstant.DEFAULT_COORDINATES_VALUE));
		coordinates.setX1(new BigInteger(BarcodeExtractionConstant.DEFAULT_COORDINATES_VALUE));
		coordinates.setY0(new BigInteger(BarcodeExtractionConstant.DEFAULT_COORDINATES_VALUE));
		coordinates.setY1(new BigInteger(BarcodeExtractionConstant.DEFAULT_COORDINATES_VALUE));
		final CoordinatesList coordinatesList = new CoordinatesList();
		coordinatesList.getCoordinates().add(coordinates);
		docFieldType.setCoordinatesList(coordinatesList);
		docFieldType.setFieldOrderNumber(fieldType.getFieldOrderNumber());
		return docFieldType;
	}

	private void populateDocumentLevelFields(final String maxConfidence, final String minConfidence, final List<DocField> updtDocFdTyList,
			final List<FieldType> allFdTypes, final BarcodeExtractionExecutor barcodeExtractionExecutor, final BarcodeExtractionResult barCodeResult) {
		for (final FieldType fieldType : allFdTypes) {
			if (fieldType.getBarcodeType() != null
					&& fieldType.getBarcodeType().equalsIgnoreCase(barCodeResult.getBarcodeType().name())) {
				validateDocFdTyList(maxConfidence, minConfidence, updtDocFdTyList, barcodeExtractionExecutor, barCodeResult, fieldType);
			}
		}
	}

	private void validateDocFdTyList(final String maxConfidence, final String minConfidence, final List<DocField> updtDocFdTyList,
			final BarcodeExtractionExecutor barcodeExtractionExecutor, final BarcodeExtractionResult barCodeResult, final FieldType fieldType) {
		boolean isAlternateValueFound = false;
		for (final DocField existingDocField : updtDocFdTyList) {
			if (fieldType.getName().equalsIgnoreCase(existingDocField.getName())) {
				final DocField docField = populateDLFForBarcodeType(maxConfidence, minConfidence, barcodeExtractionExecutor, barCodeResult,
						fieldType);
				if (existingDocField.getAlternateValues() == null) {
					final com.ephesoft.dcma.batch.schema.DocField.AlternateValues values = new com.ephesoft.dcma.batch.schema.DocField.AlternateValues();
					existingDocField.setAlternateValues(values);
				}
				existingDocField.getAlternateValues().getAlternateValue().add(docField);
				isAlternateValueFound = true;
				break;
			}
		}
		if (!isAlternateValueFound) {
			final DocField docField = populateDLFForBarcodeType(maxConfidence, minConfidence, barcodeExtractionExecutor, barCodeResult,
					fieldType);
			updtDocFdTyList.add(docField);
		}
	}

	private DocField populateDLFForBarcodeType(final String maxConfidence, final String minConfidence,
			final BarcodeExtractionExecutor barcodeExtractionExecutor, final BarcodeExtractionResult barCodeResult, final FieldType fieldType) {
		final DocField docField = new DocField();
		docField.setValue(barCodeResult.getTexts());
		docField.setName(fieldType.getName());
		docField.setType(fieldType.getDataType().name());
		docField.setFieldOrderNumber(fieldType.getFieldOrderNumber());
		final Coordinates coordinates = new Coordinates();
		coordinates.setX0(new BigInteger(String.valueOf((int) barCodeResult.getX0())));
		coordinates.setX1(new BigInteger(String.valueOf((int) barCodeResult.getX1Coordinate())));
		coordinates.setY0(new BigInteger(String.valueOf((int) barCodeResult.getY0Coordinate())));
		coordinates.setY1(new BigInteger(String.valueOf((int) barCodeResult.getY1Coordinate())));
		final CoordinatesList coordinatesList = new CoordinatesList();
		coordinatesList.getCoordinates().add(coordinates);
		docField.setCoordinatesList(coordinatesList);
		if (docField != null && docField.getValue() != null && !docField.getValue().isEmpty()) {
			docField.setConfidence(Integer.valueOf(maxConfidence));
		} else {
			docField.setConfidence(Integer.valueOf(minConfidence));
		}
		if (barcodeExtractionExecutor.getPage() != null) {
			docField.setPage(barcodeExtractionExecutor.getPage().getIdentifier());
		}
		return docField;
	}

	/**
	 * Main method to read barcode information from an image file.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @throws DCMAApplicationException {@link DCMAApplicationException}exception to be thrown
	 */
	public void readBarcode(final String batchInstanceIdentifier, final String pluginName) throws DCMAApplicationException {
		final String switchValue = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, BarcodeExtractionConstant.BARCODE_EXTRACTION_PLUGIN,
				BarcodeExtractionProperties.BARCODE_SWITCH);
		if (switchValue.equalsIgnoreCase(BarcodeExtractionConstant.SWITCH_ON)) {
			LOGGER.info("Started Processing image at " + new Date());

			// Initialize properties
			LOGGER.info("Initializing properties...");
			final String validExt = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, BarcodeExtractionConstant.BARCODE_EXTRACTION_PLUGIN,
					BarcodeExtractionProperties.BARCODE_VALID_EXTNS);
			final String readerTypes = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, BarcodeExtractionConstant.BARCODE_EXTRACTION_PLUGIN,
					BarcodeExtractionProperties.BARCODE_READER_TYPES);
			final String maxConfidence = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, BarcodeExtractionConstant.BARCODE_EXTRACTION_PLUGIN,
					BarcodeExtractionProperties.MAX_CONFIDENCE);
			final String minConfidence = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, BarcodeExtractionConstant.BARCODE_EXTRACTION_PLUGIN,
					BarcodeExtractionProperties.MIN_CONFIDENCE);
			LOGGER.info("Properties Initialized Successfully");

			final String[] validExtensions = validExt.split(";");
			final String[] appReaderTypes = readerTypes.split(";");
			final String batchInstanceLocalFolderPath = batchInstanceService
					.getSystemFolderForBatchInstanceId(batchInstanceIdentifier);
			LOGGER.info("Local folder for batch instance: " + batchInstanceIdentifier + " is :" + batchInstanceLocalFolderPath);
			final String actualFolderLocation = batchInstanceLocalFolderPath + File.separator + batchInstanceIdentifier;

			final Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);

			final BatchInstanceThread batchInstanceThread = new BatchInstanceThread(batchInstanceIdentifier);

			final List<List<BarcodeExtractionExecutor>> barcodeDOCExecutorList = new ArrayList<List<BarcodeExtractionExecutor>>();
			final List<Document> xmlDocuments = batch.getDocuments().getDocument();

			if (null == xmlDocuments || xmlDocuments.isEmpty()) {
				LOGGER.error("No pages found in batch XML.");
				throw new DCMAApplicationException("No pages found in batch XML.");
			}

			processDocuments(validExtensions, appReaderTypes, actualFolderLocation, batchInstanceThread, barcodeDOCExecutorList,
					xmlDocuments);
			try {
				LOGGER.info("Starting execution through thread pool");
				batchInstanceThread.execute();
				LOGGER.info("Done execution through thread pool");
			} catch (final DCMAApplicationException dcmae) {
				LOGGER.error("Error in barcode extraction threadpool" + dcmae.getMessage(), dcmae);
				batchInstanceThread.remove();
				// Throw the exception to set the batch status to Error by Application aspect
				throw new DCMAApplicationException(dcmae.getMessage(), dcmae);
			}

			final List<List<DocField>> updtDocList = new ArrayList<List<DocField>>();

			for (final List<BarcodeExtractionExecutor> barcodeExecutor : barcodeDOCExecutorList) {
				LOGGER.info("updating XML for document");
				final List<DocField> updtDocFdTyList = updateBatchXML(barcodeExecutor, batch, maxConfidence, minConfidence,
						batchInstanceIdentifier);
				updtDocList.add(updtDocFdTyList);
			}

			documentLevelFieldsMapping(batch, xmlDocuments, updtDocList);
		} else {
			LOGGER.info("Skipping barcode classification. Switch set as off.");
		}
	}

	private void processDocuments(final String[] validExtensions, final String[] appReaderTypes, final String actualFolderLocation,
			final BatchInstanceThread batchInstanceThread, final List<List<BarcodeExtractionExecutor>> barcodeDOCExecutorList,
			final List<Document> xmlDocuments) throws DCMAApplicationException {
		for (final Document document : xmlDocuments) {
			final List<BarcodeExtractionExecutor> barcodeExecutorList = new ArrayList<BarcodeExtractionExecutor>();
			barcodeDOCExecutorList.add(barcodeExecutorList);

			final List<Page> listOfPages = document.getPages().getPage();
			if (null == listOfPages || listOfPages.isEmpty()) {
				LOGGER.error("No pages found in batch XML for this document type");
				continue;
			}
			for (final Page page : listOfPages) {
				final String sImageFile = page.getNewFileName();
				if (sImageFile == null || sImageFile.isEmpty()) {
					continue;
				}
				String eachPage = sImageFile;
				eachPage = eachPage.trim();
				boolean isFileValid = false;
				if (validExtensions != null && validExtensions.length > 0) {
					for (int l = 0; l < validExtensions.length; l++) {
						if (eachPage.substring(eachPage.indexOf('.') + 1).equalsIgnoreCase(validExtensions[l])) {
							isFileValid = true;
							break;
						}
					}
				} else {
					LOGGER.error(BarcodeExtractionConstant.INVALID_EXTENSIONS);
					throw new DCMAApplicationException(BarcodeExtractionConstant.INVALID_EXTENSIONS);
				}
				if (isFileValid) {
					LOGGER.info("Calling Zxing library for image :" + eachPage);
					final BarcodeExtractionExecutor barcodeExecutor = new BarcodeExtractionExecutor(actualFolderLocation
							+ File.separator + eachPage, eachPage, appReaderTypes, page, document);
					barcodeExecutorList.add(barcodeExecutor);
					batchInstanceThread.add(barcodeExecutor);
					LOGGER.info("Done with Zxing library for image : " + eachPage);
				} else {
					LOGGER.error(BarcodeExtractionConstant.FILE + eachPage + BarcodeExtractionConstant.HAS_INVALID_EXTENSION);
					throw new DCMABusinessException(BarcodeExtractionConstant.FILE + eachPage + BarcodeExtractionConstant.HAS_INVALID_EXTENSION);
				}
			}
		}
	}

	/**
	 * This method performs the functionality of reading barcode for the web service.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param folderLocation {@link String}
	 * @param paramImageName {@link String}
	 * @param docType {@link String}
	 * @return List<DocField>
	 * @throws DCMAApplicationException {@link DCMAApplicationException}exception to be thrown
	 */
	public List<DocField> readBarcodeForWebService(final String batchClassIdentifier, final String folderLocation, final String paramImageName,
			final String docType) throws DCMAApplicationException {
		LOGGER.info("Started Processing image at " + new Date());
		final BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdentifier);
		final List<DocumentType> validDocTypeList = batchClass.getDocumentTypes();
		boolean isValidDocType = false;
		for (final DocumentType documentType : validDocTypeList) {
			final String docTypeName = documentType.getName();
			if (docTypeName.equals(docType)) {
				isValidDocType = true;
				break;
			}
		}
		if (!isValidDocType) {
			throw new DCMAApplicationException("Document type is not valid");
		}
		// Initialize properties
		LOGGER.info("Initializing properties...");
		final String validExt = batchClassPluginPropertiesService.getPropertyValue(batchClassIdentifier, BarcodeExtractionConstant.BARCODE_EXTRACTION_PLUGIN,
				BarcodeExtractionProperties.BARCODE_VALID_EXTNS);
		final String readerTypes = batchClassPluginPropertiesService.getPropertyValue(batchClassIdentifier, BarcodeExtractionConstant.BARCODE_EXTRACTION_PLUGIN,
				BarcodeExtractionProperties.BARCODE_READER_TYPES);
		final String maxConfidence = batchClassPluginPropertiesService.getPropertyValue(batchClassIdentifier, BarcodeExtractionConstant.BARCODE_EXTRACTION_PLUGIN,
				BarcodeExtractionProperties.MAX_CONFIDENCE);
		final String minConfidence = batchClassPluginPropertiesService.getPropertyValue(batchClassIdentifier, BarcodeExtractionConstant.BARCODE_EXTRACTION_PLUGIN,
				BarcodeExtractionProperties.MIN_CONFIDENCE);

		LOGGER.info("Properties Initialized Successfully");
		final String[] validExtensions = validExt.split(";");
		final String[] appReaderTypes = readerTypes.split(";");
		final String imageName = paramImageName.trim();
		boolean isFileValid = false;
		if (validExtensions != null && validExtensions.length > 0) {
			for (int l = 0; l < validExtensions.length; l++) {
				if (imageName.substring(imageName.lastIndexOf('.') + 1).equalsIgnoreCase(validExtensions[l])) {
					isFileValid = true;
					break;
				}
			}
		} else {
			LOGGER.error(BarcodeExtractionConstant.INVALID_EXTENSIONS);
			throw new DCMAApplicationException(BarcodeExtractionConstant.INVALID_EXTENSIONS);
		}

		final List<BarcodeExtractionExecutor> barcodeExecutorList = new ArrayList<BarcodeExtractionExecutor>();
		if (isFileValid) {
			LOGGER.info("Calling Zxing library for image :" + imageName);
			LOGGER.error("*************folder location is " + folderLocation);
			final BarcodeExtractionExecutor barcodeExecutor = new BarcodeExtractionExecutor(folderLocation + File.separator + imageName,
					imageName, appReaderTypes, null, null);
			barcodeExecutorList.add(barcodeExecutor);
			LOGGER.info("Done with Zxing library for image : " + imageName);
		} else {
			LOGGER.error(BarcodeExtractionConstant.FILE + imageName + BarcodeExtractionConstant.HAS_INVALID_EXTENSION);
			throw new DCMABusinessException(BarcodeExtractionConstant.FILE + imageName + BarcodeExtractionConstant.HAS_INVALID_EXTENSION);
		}
		final List<DocField> updtDocFdTyList = new ArrayList<DocField>();
		final List<FieldType> fieldTypeList = fieldTypeService.getFdTypeByDocTypeNameForBatchClass(docType, batchClassIdentifier);
		if (fieldTypeList == null || fieldTypeList.isEmpty()) {
			LOGGER.error("Field type List is null or empty");
			throw new DCMAApplicationException("Document type does not contain valid fields.");
		}
		for (final BarcodeExtractionExecutor barcodeExtractionExecutor : barcodeExecutorList) {

			LOGGER.info("updating XML for image file");
			barcodeExtractionExecutor.run();
			final BarcodeExtractionResult[] barCodeResults = barcodeExtractionExecutor.getBarCodeResults();
			if (barCodeResults != null && barCodeResults.length > 0) {

				for (final BarcodeExtractionResult barCodeResult : barCodeResults) {

					if (barCodeResult != null) {
						populateDocumentLevelFields(maxConfidence, minConfidence, updtDocFdTyList, fieldTypeList,
								barcodeExtractionExecutor, barCodeResult);
					}
				}
			}

			for (final FieldType fieldType : fieldTypeList) {
				boolean isFieldTypeExists = false;
				for (final DocField updatedDocField : updtDocFdTyList) {
					if (updatedDocField.getName().equalsIgnoreCase(fieldType.getName())) {
						isFieldTypeExists = true;
					}
				}
				if (!isFieldTypeExists) {
					final DocField docFieldType = createDefaultDLF(minConfidence, fieldType);
					updtDocFdTyList.add(docFieldType);
				}
			}
		}
		return updtDocFdTyList;
	}

	private void documentLevelFieldsMapping(final Batch batch, final List<Document> xmlDocuments, final List<List<DocField>> updtDocList) {
		boolean isWrite = false;
		if (null != xmlDocuments && xmlDocuments.size() == updtDocList.size()) {
			for (int i = 0; i < xmlDocuments.size(); i++) {
				final List<DocField> docFdTyLt = updtDocList.get(i);
				if (null == docFdTyLt || docFdTyLt.isEmpty()) {
					continue;
				}
				final Document document = xmlDocuments.get(i);
				DocumentLevelFields documentLevelFields = document.getDocumentLevelFields();
				if (null == documentLevelFields) {
					documentLevelFields = new DocumentLevelFields();
				}
				final List<DocField> documentLevelField = documentLevelFields.getDocumentLevelField();

				if (documentLevelField.isEmpty()) {
					documentLevelField.addAll(docFdTyLt);
				} else {
					for (int orgIndex = 0; orgIndex < documentLevelField.size(); orgIndex++) {
						final DocField orgFieldType = documentLevelField.get(orgIndex);
						final String orgName = orgFieldType.getName();
						for (final DocField actFieldType : docFdTyLt) {
							final String actName = actFieldType.getName();
							if (null != actName && null != orgName && orgName.equals(actName)) {
								final String actValue = actFieldType.getValue();
								if (null == actValue || actValue.isEmpty()) {
									break;
								} else {
									documentLevelField.set(orgIndex, actFieldType);
								}
								break;
							}
						}
					}
				}
				document.setDocumentLevelFields(documentLevelFields);
				isWrite = true;
			}
		} else {
			LOGGER.info("Inavlid number of Document level fields are extracted. Will not proceed.");
		}

		if (isWrite) {
			batchSchemaService.updateBatch(batch);
			LOGGER.info("Document level fields are added to the batch xml file.");
		} else {
			LOGGER.info("Document level fields are not added to the batch xml file.");
		}

		LOGGER.info("Processing finished at " + new Date());
	}

	/**
	 * This method finds the name of all image files that could be processed from batch xml.
	 * 
	 * @param batch {@link Batch}
	 * @return List<String>
	 * @throws DCMAApplicationException {@link DCMAApplicationException}exception to be thrown
	 */
	public List<String> findAllPagesFromXML(final Batch batch) throws DCMAApplicationException {
		final List<String> allPages = new ArrayList<String>();

		final List<Document> xmlDocuments = batch.getDocuments().getDocument();
		for (int i = 0; i < xmlDocuments.size(); i++) {
			final Document document = xmlDocuments.get(i);
			final List<Page> listOfPages = document.getPages().getPage();
			for (int j = 0; j < listOfPages.size(); j++) {
				final Page page = listOfPages.get(j);
				final String sImageFile = page.getNewFileName();
				if (sImageFile != null && sImageFile.length() > 0) {
					allPages.add(sImageFile);
				}
			}
		}
		return allPages;
	}

	/**
	 * Enum for Barcode types possible values.
	 */
	public static enum BarcodeReaderTypes {
		/**
		 * CODE39 barcode type.
		 */
		CODE39, 
		/**
		 * QR barcode type.
		 */
		QR, 
		/**
		 * DATAMATRIX barcode type.
		 */
		DATAMATRIX, 
		/**
		 * CODE128 barcode type.
		 */
		CODE128, 
		/**
		 * CODE93 barcode type.
		 */
		CODE93, 
		/**
		 * ITF barcode type.
		 */
		ITF, 
		/**
		 * CODABAR barcode type.
		 */
		CODABAR, 
		/**
		 * PDF417 barcode type.
		 */
		PDF417, 
		/**
		 * EAN13 barcode type.
		 */
		EAN13;

		/**
		 * @return List<BarcodeReaderTypes>
		 */
		public static List<BarcodeReaderTypes> valuesAsList() {
			return Arrays.asList(values());
		}
	}

}
