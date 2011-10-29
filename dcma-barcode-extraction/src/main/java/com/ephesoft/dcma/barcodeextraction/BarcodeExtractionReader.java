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

package com.ephesoft.dcma.barcodeextraction;

import java.io.File;
import java.io.IOException;
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
import com.ephesoft.dcma.da.domain.FieldType;
import com.ephesoft.dcma.util.FileUtils;

/**
 * This class reads the bar-code on each image file fetched from batch.xml, processes images with Zxing library to find the barcode
 * information for each image and writes the extracted information to batch xml to be used by subsequent plugins.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.component.ICommonConstants
 * @see com.ephesoft.dcma.batch.service.BatchSchemaService
 * 
 */
@Component
public class BarcodeExtractionReader implements ICommonConstants {

	private static final String BARCODE_EXTRACTION_PLUGIN = "BARCODE_EXTRACTION";

	/**
	 * Instance of BatchSchemaService.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Instance of PluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * Default co-ordinates value.
	 */
	public static final String DEFAULT_COORDINATES_VALUE = "0";
	/**
	 * Logger instance for logging using slf4j for logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BarcodeExtractionReader.class);

	private static final String SWITCH_ON = "ON";

	/**
	 * The prefix for name to be stored in batch xml.
	 */
	private transient String barcodeName;

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
	 * @param barcodeName
	 */
	public void setBarcodeName(final String barcodeName) {
		this.barcodeName = barcodeName;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(this.barcodeName);
		}
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
	 * @param batchSchemaService the batchSchemaService to set
	 */
	public void setBatchSchemaService(final BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * @return the pluginPropertiesService
	 */
	public PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	/**
	 * @param pluginPropertiesService the pluginPropertiesService to set
	 */
	public void setPluginPropertiesService(final PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	/**
	 * This method updates the the batch xml for each image file and inserts page level field with attributes name, value, coordinates
	 * and Confidence score.
	 * 
	 * @throws DCMAApplicationException
	 */
	public List<DocField> updateBatchXML(final List<BarcodeExtractionExecutor> barcodeExecutorList, final Batch batch,
			final String maxConfidence, final String minConfidence, final String batchInstanceId) throws DCMAApplicationException {

		List<DocField> updtDocFdTyList = new ArrayList<DocField>();

		String errMsg = null;

		List<FieldType> allFdTypes = null;

		for (BarcodeExtractionExecutor barcodeExtractionExecutor : barcodeExecutorList) {
			Document document = barcodeExtractionExecutor.getDocument();
			String docTypeName = document.getType();
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

			BarcodeExtractionResult[] barCodeResults = barcodeExtractionExecutor.getBarCodeResults();
			if (barCodeResults != null && barCodeResults.length > 0) {

				for (BarcodeExtractionResult barCodeResult : barCodeResults) {

					if (barCodeResult != null) {
						populateDocumentLevelFields(maxConfidence, minConfidence, updtDocFdTyList, allFdTypes,
								barcodeExtractionExecutor, barCodeResult);
					}
				}

			}
		}
		for (FieldType fieldType : allFdTypes) {
			boolean isFieldTypeExists = false;
			for (DocField updatedDocField : updtDocFdTyList) {
				if (updatedDocField.getName().equalsIgnoreCase(fieldType.getName())) {
					isFieldTypeExists = true;
				}
			}
			if (!isFieldTypeExists) {
				DocField docFieldType = createDefaultDLF(minConfidence, fieldType);
				updtDocFdTyList.add(docFieldType);
			}
		}

		return updtDocFdTyList;
	}

	private DocField createDefaultDLF(final String minConfidence, FieldType fieldType) {
		DocField docFieldType = new DocField();
		docFieldType.setName(fieldType.getName());
		docFieldType.setValue("");
		docFieldType.setType(fieldType.getDataType().name());
		docFieldType.setConfidence(Integer.valueOf(minConfidence));
		Coordinates coordinates = new Coordinates();
		coordinates.setX0(new BigInteger(DEFAULT_COORDINATES_VALUE));
		coordinates.setX1(new BigInteger(DEFAULT_COORDINATES_VALUE));
		coordinates.setY0(new BigInteger(DEFAULT_COORDINATES_VALUE));
		coordinates.setY1(new BigInteger(DEFAULT_COORDINATES_VALUE));
		CoordinatesList coordinatesList = new CoordinatesList();
		coordinatesList.getCoordinates().add(coordinates);
		docFieldType.setCoordinatesList(coordinatesList);
		docFieldType.setFieldOrderNumber(fieldType.getFieldOrderNumber());
		return docFieldType;
	}

	private void populateDocumentLevelFields(final String maxConfidence, final String minConfidence, List<DocField> updtDocFdTyList,
			List<FieldType> allFdTypes, BarcodeExtractionExecutor barcodeExtractionExecutor, BarcodeExtractionResult barCodeResult) {
		for (FieldType fieldType : allFdTypes) {
			if (fieldType.getBarcodeType() != null
					&& fieldType.getBarcodeType().equalsIgnoreCase(barCodeResult.getBarcodeType().name())) {
				boolean isAlternateValueFound = false;
				for (DocField existingDocField : updtDocFdTyList) {
					if (fieldType.getName().equalsIgnoreCase(existingDocField.getName())) {
						DocField docField = populateDLFForBarcodeType(maxConfidence, minConfidence, barcodeExtractionExecutor,
								barCodeResult, fieldType);
						if (existingDocField.getAlternateValues() == null) {
							com.ephesoft.dcma.batch.schema.DocField.AlternateValues values = new com.ephesoft.dcma.batch.schema.DocField.AlternateValues();
							existingDocField.setAlternateValues(values);
						}
						existingDocField.getAlternateValues().getAlternateValue().add(docField);
						isAlternateValueFound = true;
						break;
					}
				}
				if (!isAlternateValueFound) {
					DocField docField = populateDLFForBarcodeType(maxConfidence, minConfidence, barcodeExtractionExecutor,
							barCodeResult, fieldType);
					updtDocFdTyList.add(docField);
				}
			}
		}
	}

	private DocField populateDLFForBarcodeType(final String maxConfidence, final String minConfidence,
			BarcodeExtractionExecutor barcodeExtractionExecutor, BarcodeExtractionResult barCodeResult, FieldType fieldType) {
		DocField docField = new DocField();
		docField.setValue(barCodeResult.getTexts());
		docField.setName(fieldType.getName());
		docField.setType(fieldType.getDataType().name());
		docField.setFieldOrderNumber(fieldType.getFieldOrderNumber());
		Coordinates coordinates = new Coordinates();
		coordinates.setX0(new BigInteger(String.valueOf((int) barCodeResult.getX0())));
		coordinates.setX1(new BigInteger(String.valueOf((int) barCodeResult.getX1Coordinate())));
		coordinates.setY0(new BigInteger(String.valueOf((int) barCodeResult.getY0Coordinate())));
		coordinates.setY1(new BigInteger(String.valueOf((int) barCodeResult.getY1Coordinate())));
		CoordinatesList coordinatesList = new CoordinatesList();
		coordinatesList.getCoordinates().add(coordinates);
		docField.setCoordinatesList(coordinatesList);
		if (docField != null && docField.getValue() != null && !docField.getValue().isEmpty()) {
			docField.setConfidence(Integer.valueOf(maxConfidence));
		} else {
			docField.setConfidence(Integer.valueOf(minConfidence));
		}
		docField.setPage(barcodeExtractionExecutor.getPage().getIdentifier());
		return docField;
	}

	/**
	 * Main method to read barcode information from an image file.
	 * 
	 * @param pluginName
	 * 
	 * @param batchInstanceID
	 * @return
	 * @throws DCMAApplicationException
	 * @throws DCMABusinessException
	 */
	public void readBarcode(final String batchInstanceIdentifier, String pluginName) throws DCMAApplicationException {
		String switchValue = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, BARCODE_EXTRACTION_PLUGIN,
				BarcodeExtractionProperties.BARCODE_SWITCH);
		if (switchValue.equalsIgnoreCase(SWITCH_ON)) {
			LOGGER.info("Started Processing image at " + new Date());

			// Initialize properties
			LOGGER.info("Initializing properties...");
			String validExt = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, BARCODE_EXTRACTION_PLUGIN,
					BarcodeExtractionProperties.BARCODE_VALID_EXTNS);
			String readerTypes = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, BARCODE_EXTRACTION_PLUGIN,
					BarcodeExtractionProperties.BARCODE_READER_TYPES);
			String maxConfidence = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, BARCODE_EXTRACTION_PLUGIN,
					BarcodeExtractionProperties.MAX_CONFIDENCE);
			String minConfidence = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, BARCODE_EXTRACTION_PLUGIN,
					BarcodeExtractionProperties.MIN_CONFIDENCE);
			LOGGER.info("Properties Initialized Successfully");

			String[] validExtensions = validExt.split(";");
			String[] appReaderTypes = readerTypes.split(";");
			String actualFolderLocation = batchSchemaService.getLocalFolderLocation() + File.separator + batchInstanceIdentifier;

			Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);

			BatchInstanceThread batchInstanceThread = new BatchInstanceThread();

			String threadPoolLockFolderPath = batchSchemaService.getLocalFolderLocation() + File.separator + batchInstanceIdentifier
					+ File.separator + batchSchemaService.getThreadpoolLockFolderName();
			try {
				FileUtils.createThreadPoolLockFile(batchInstanceIdentifier, threadPoolLockFolderPath, pluginName);
			} catch (IOException ioe) {
				LOGGER.error("Error in creating threadpool lock file" + ioe.getMessage(), ioe);
				throw new DCMABusinessException(ioe.getMessage(), ioe);
			}

			List<List<BarcodeExtractionExecutor>> barcodeDOCExecutorList = new ArrayList<List<BarcodeExtractionExecutor>>();
			List<Document> xmlDocuments = batch.getDocuments().getDocument();

			if (null == xmlDocuments || xmlDocuments.isEmpty()) {
				LOGGER.error("No pages found in batch XML.");
				throw new DCMAApplicationException("No pages found in batch XML.");
			}

			for (Document document : xmlDocuments) {
				List<BarcodeExtractionExecutor> barcodeExecutorList = new ArrayList<BarcodeExtractionExecutor>();
				barcodeDOCExecutorList.add(barcodeExecutorList);

				List<Page> listOfPages = document.getPages().getPage();
				if (null == listOfPages || listOfPages.isEmpty()) {
					LOGGER.error("No pages found in batch XML for this document type");
					continue;
				}

				for (Page page : listOfPages) {
					String sImageFile = page.getNewFileName();
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
						LOGGER.error("No valid extensions are specified in resources");
						throw new DCMAApplicationException("No valid extensions are specified in resources");
					}
					if (isFileValid) {
						LOGGER.info("Calling Zxing library for image :" + eachPage);
						BarcodeExtractionExecutor barcodeExecutor = new BarcodeExtractionExecutor(actualFolderLocation
								+ File.separator + eachPage, eachPage, appReaderTypes, page, document);
						barcodeExecutorList.add(barcodeExecutor);
						batchInstanceThread.add(barcodeExecutor);
						LOGGER.info("Done with Zxing library for image : " + eachPage);
					} else {
						LOGGER.error("File " + eachPage + " has invalid extension.");
						throw new DCMABusinessException("File " + eachPage + " has invalid extension.");
					}
				}
			}

			try {
				LOGGER.info("Starting execution through thread pool");
				batchInstanceThread.execute();
				LOGGER.info("Done execution through thread pool");
			} catch (DCMAApplicationException dcmae) {
				LOGGER.error("Error in barcode extraction threadpool" + dcmae.getMessage(), dcmae);
				batchInstanceThread.remove();
				// Throw the exception to set the batch status to Error by Application aspect
				throw new DCMAApplicationException(dcmae.getMessage(), dcmae);
			} finally {
				try {
					FileUtils.deleteThreadPoolLockFile(batchInstanceIdentifier, threadPoolLockFolderPath, pluginName);
				} catch (IOException ioe) {
					LOGGER.error("Error in deleting threadpool lock file" + ioe.getMessage(), ioe);
					throw new DCMABusinessException(ioe.getMessage(), ioe);
				}
			}

			List<List<DocField>> updtDocList = new ArrayList<List<DocField>>();

			for (List<BarcodeExtractionExecutor> barcodeExecutor : barcodeDOCExecutorList) {
				LOGGER.info("updating XML for document");
				List<DocField> updtDocFdTyList = updateBatchXML(barcodeExecutor, batch, maxConfidence, minConfidence,
						batchInstanceIdentifier);
				updtDocList.add(updtDocFdTyList);
			}

			documentLevelFieldsMapping(batch, xmlDocuments, updtDocList);
		} else {
			LOGGER.info("Skipping barcode classification. Switch set as off.");
		}
	}

	private void documentLevelFieldsMapping(Batch batch, List<Document> xmlDocuments, List<List<DocField>> updtDocList) {
		boolean isWrite = false;
		if (null != xmlDocuments && xmlDocuments.size() == updtDocList.size()) {
			for (int i = 0; i < xmlDocuments.size(); i++) {
				List<DocField> docFdTyLt = updtDocList.get(i);
				if (null == docFdTyLt || docFdTyLt.isEmpty()) {
					continue;
				}
				Document document = xmlDocuments.get(i);
				DocumentLevelFields documentLevelFields = document.getDocumentLevelFields();
				if (null == documentLevelFields) {
					documentLevelFields = new DocumentLevelFields();
				}
				List<DocField> documentLevelField = documentLevelFields.getDocumentLevelField();

				if (documentLevelField.isEmpty()) {
					documentLevelField.addAll(docFdTyLt);
				} else {
					for (int orgIndex = 0; orgIndex < documentLevelField.size(); orgIndex++) {
						DocField orgFieldType = documentLevelField.get(orgIndex);
						String orgName = orgFieldType.getName();
						for (DocField actFieldType : docFdTyLt) {
							String actName = actFieldType.getName();
							if (null != actName && null != orgName && orgName.equals(actName)) {
								String actValue = actFieldType.getValue();
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
	 * This method finds the name of all processable image files from batch xml.
	 * 
	 * @param batch Batch
	 * @return List<String>
	 * @throws DCMAApplicationException
	 */
	public List<String> findAllPagesFromXML(final Batch batch) throws DCMAApplicationException {
		List<String> allPages = new ArrayList<String>();

		List<Document> xmlDocuments = batch.getDocuments().getDocument();
		for (int i = 0; i < xmlDocuments.size(); i++) {
			Document document = xmlDocuments.get(i);
			List<Page> listOfPages = document.getPages().getPage();
			for (int j = 0; j < listOfPages.size(); j++) {
				Page page = listOfPages.get(j);
				String sImageFile = page.getNewFileName();
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
		CODE39, QR, DATAMATRIX, CODE128, CODE93, ITF, CODABAR, PDF417, EAN13;

		public static List<BarcodeReaderTypes> valuesAsList() {
			return Arrays.asList(values());
		}
	}

}
