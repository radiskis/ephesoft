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

package com.ephesoft.dcma.barcode;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.barcode.constant.BarcodeConstants;
import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Field;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.DocField.AlternateValues;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
import com.ephesoft.dcma.batch.schema.Page.PageLevelFields;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.da.service.BatchInstanceService;

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
public class BarcodeReader implements ICommonConstants {

	
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
	 * Instance of batchInstanceService.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;
	
	/**
	 * Logger instance for logging using slf4j for logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BarcodeReader.class);

	/**
	 * The prefix for name to be stored in batch xml.
	 */
	private transient String barcodeName;

	/**
	 * Reference firstPage name.
	 */
	private transient String firstPage;

	/**
	 * setter for barcode name.
	 * @param barcodeName {@link String}
	 */
	public void setBarcodeName(final String barcodeName) {
		this.barcodeName = barcodeName;
	}

	/**
	 * getter for batchSchemaService.
	 * @return {@link BatchSchemaService} the batchSchemaService
	 */
	public BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * First page name.
	 * 
	 * @return firstPage {@link String}
	 */
	public final String getFirstPage() {
		return firstPage;
	}

	/**
	 * First page name.
	 * 
	 * @param firstPage {@link String}
	 */
	public final void setFirstPage(final String firstPage) {
		this.firstPage = firstPage;
	}

	/**
	 * setter for BatchSchemaService.
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
	 * setter for PluginPropertiesService.
	 * @param pluginPropertiesService {@link PluginPropertiesService}
	 */
	public void setPluginPropertiesService(final PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	/**
	 * This method updates the the batch xml for each image file and inserts page level field with attributes name, value, coordinates
	 * and Confidence score.
	 * 
	 * @param fileName {@link String}
	 * @param barCodeResults {@link BarcodeResult}
	 * @param xmlDocuments {@link List <Document>}
	 * @param maxConfidence {@link String}
	 * @param minConfidence {@link String}
	 * @throws DCMAApplicationException {@link DCMAApplicationException} exception to be thrown
	 */
	public void updateBatchXML(final String fileName, final BarcodeResult[] barCodeResults, final List<Document> xmlDocuments,
			final String maxConfidence, final String minConfidence) throws DCMAApplicationException {

		for (int i = 0; i < xmlDocuments.size(); i++) {
			final Document document = xmlDocuments.get(i);
			final List<Page> listOfPages = document.getPages().getPage();
			for (int j = 0; j < listOfPages.size(); j++) {
				final Page page = listOfPages.get(j);
				final String sImageFile = page.getNewFileName();
				PageLevelFields pageLevelFields = page.getPageLevelFields();
				if (pageLevelFields == null) {
					pageLevelFields = new PageLevelFields();
				}
				if (fileName.equalsIgnoreCase(sImageFile)) {
					if (barCodeResults != null && barCodeResults.length > 0) {
						final DocField docFieldType = new DocField();
						AlternateValues alternateValues = docFieldType.getAlternateValues();
						for (int k = 0; k < barCodeResults.length; k++) {
							if (k == 0) {
								docFieldType.setName(barcodeName + (k + 1));
								String value = barCodeResults[k].getTexts();
								final StringBuffer tempValue = new StringBuffer();
								tempValue.append(value);
								if (null != tempValue.toString() && !tempValue.toString().isEmpty()) {
									tempValue.append(getFirstPage());
								}
								value = tempValue.toString();
								docFieldType.setValue(value);
								docFieldType.setType(barCodeResults[k].getBarcodeType().name());
								if (!value.isEmpty()) {
									docFieldType.setConfidence(Integer.valueOf(maxConfidence));
								} else {
									docFieldType.setConfidence(Integer.valueOf(minConfidence));
								}
								final Coordinates coordinates = new Coordinates();
								coordinates.setX0(new BigInteger(String.valueOf((int) barCodeResults[k].getX0Cord())));
								coordinates.setX1(new BigInteger(String.valueOf((int) barCodeResults[k].getX1Cord())));
								coordinates.setY0(new BigInteger(String.valueOf((int) barCodeResults[k].getY0Cord())));
								coordinates.setY1(new BigInteger(String.valueOf((int) barCodeResults[k].getY1Cord())));
								final CoordinatesList coordinatesList = new CoordinatesList();
								coordinatesList.getCoordinates().add(coordinates);
								docFieldType.setCoordinatesList(coordinatesList);
								pageLevelFields.getPageLevelField().add(docFieldType);
							} else {
								if (alternateValues == null) {
									alternateValues = new AlternateValues();
								}
								final Field fieldType = new Field();
								fieldType.setName(barcodeName + (k + 1));
								final String value = barCodeResults[k].getTexts();
								fieldType.setValue(value);
								fieldType.setType(barCodeResults[k].getBarcodeType().name());
								if (!value.isEmpty()) {
									fieldType.setConfidence(Integer.valueOf(maxConfidence));
								} else {
									fieldType.setConfidence(Integer.valueOf(minConfidence));
								}
								final Coordinates coordinates = new Coordinates();
								coordinates.setX0(new BigInteger(String.valueOf((int) barCodeResults[k].getX0Cord())));
								coordinates.setX1(new BigInteger(String.valueOf((int) barCodeResults[k].getX1Cord())));
								coordinates.setY0(new BigInteger(String.valueOf((int) barCodeResults[k].getY0Cord())));
								coordinates.setY1(new BigInteger(String.valueOf((int) barCodeResults[k].getY1Cord())));
								final CoordinatesList coordinatesList = new CoordinatesList();
								coordinatesList.getCoordinates().add(coordinates);
								fieldType.setCoordinatesList(coordinatesList);
								alternateValues.getAlternateValue().add(fieldType);
							}
						}
						docFieldType.setAlternateValues(alternateValues);
						page.setPageLevelFields(pageLevelFields);
					} else {
						final DocField docFieldType = new DocField();
						docFieldType.setName(barcodeName + BarcodeConstants.BARCODE_FIELD_SUFFIX);
						docFieldType.setValue(BarcodeConstants.EMPTY_STRING);
						docFieldType.setType(BarcodeConstants.EMPTY_STRING);
						docFieldType.setConfidence(Integer.valueOf(minConfidence));
						final Coordinates coordinates = new Coordinates();
						coordinates.setX0(new BigInteger(BarcodeConstants.DEFAULT_COORDINATES_VALUE));
						coordinates.setX1(new BigInteger(BarcodeConstants.DEFAULT_COORDINATES_VALUE));
						coordinates.setY0(new BigInteger(BarcodeConstants.DEFAULT_COORDINATES_VALUE));
						coordinates.setY1(new BigInteger(BarcodeConstants.DEFAULT_COORDINATES_VALUE));
						final CoordinatesList coordinatesList = new CoordinatesList();
						coordinatesList.getCoordinates().add(coordinates);
						docFieldType.setCoordinatesList(coordinatesList);
						pageLevelFields.getPageLevelField().add(docFieldType);
						page.setPageLevelFields(pageLevelFields);
					}
				}
			}
		}
	}

	/**
	 * @param batchInstanceIdentifier {@link String}
	 * @param xmlDocuments {@link List <Document>}
	 * @param workingDir {@link String}
	 * @param propertyMap {@link Map <BarcodeProperties, String>}
	 * @throws DCMAApplicationException {@link DCMAApplicationException} exception to be thrown
	 */
	public void readBarcodeAPI(final String batchInstanceIdentifier, final List<Document> xmlDocuments, final String workingDir,
			final Map<BarcodeProperties, String> propertyMap) throws DCMAApplicationException {
		LOGGER.info("Started Processing image at " + new Date());
		// Initialize properties
		LOGGER.info("Initializing properties...");
		final String validExt = propertyMap.get(BarcodeProperties.BARCODE_VALID_EXTNS);
		final String readerTypes = propertyMap.get(BarcodeProperties.BARCODE_READER_TYPES);
		final String maxConfidence = propertyMap.get(BarcodeProperties.MAX_CONFIDENCE);
		final String minConfidence = propertyMap.get(BarcodeProperties.MIN_CONFIDENCE);
		LOGGER.info("Properties Initialized Successfully");
		final String[] validExtensions = validExt.split(BarcodeConstants.SEMICOLON_DELIMITER);
		final String[] appReaderTypes = readerTypes.split(BarcodeConstants.SEMICOLON_DELIMITER);
		List<String> allPages = null;
		try {
			allPages = findAllPagesFromXML(xmlDocuments);
		} catch (final DCMAApplicationException e1) {
			LOGGER.error("Exception while reading from XML" + e1.getMessage());
			throw new DCMAApplicationException(e1.getMessage(), e1);
		}
		final BatchInstanceThread batchInstanceThread = new BatchInstanceThread(batchInstanceIdentifier);

		final List<BarcodeExecutor> barcodeExecutorList = new ArrayList<BarcodeExecutor>();
		if (!allPages.isEmpty()) {
			for (int i = 0; i < allPages.size(); i++) {
				String eachPage = allPages.get(i);
				eachPage = eachPage.trim();
				boolean isFileValid = false;
				if (validExtensions != null && validExtensions.length > 0) {
					for (int l = 0; l < validExtensions.length; l++) {
						if (eachPage.substring(eachPage.indexOf(BarcodeConstants.DOT_DELIMITER) + BarcodeConstants.BARCODE_FIELD_SUFFIX).equalsIgnoreCase(
								validExtensions[l])) {
							isFileValid = true;
							break;
						}
					}
				} else {
					LOGGER.error("No valid extension are specified in resources");
					throw new DCMAApplicationException("No valid extensions are specified in resources");
				}
				if (isFileValid) {
					LOGGER.info("Calling Zxing library for image :" + eachPage);
					final BarcodeExecutor barcodeExecutor = new BarcodeExecutor(workingDir + File.separator + eachPage, eachPage,
							appReaderTypes);
					barcodeExecutorList.add(barcodeExecutor);
					batchInstanceThread.add(barcodeExecutor);
					LOGGER.info("Done with Zxing library for image : " + eachPage);
				} else {
					LOGGER.error("File  " + eachPage + "  has invalid extension.");
					throw new DCMABusinessException("File " + eachPage + " has invalid extension.");
				}
			}
			LOGGER.info("Starting execution through thread pool");
			try {
				batchInstanceThread.execute();
			} catch (final DCMAApplicationException dcmae) {
				LOGGER.error("Error in generating barcode.");
				batchInstanceThread.remove();
				// Throw the exception to set the batch status to Error by Application aspect
				LOGGER.error("Error in generating barcode" + dcmae.getMessage(), dcmae);
				throw new DCMAApplicationException(dcmae.getMessage(), dcmae);
			}
			LOGGER.info("Done execution through thread pool");
			for (final BarcodeExecutor barcodeExecutor : barcodeExecutorList) {
				LOGGER.info("updating XML for image :" + barcodeExecutor.getFileName());
				updateBatchXML(barcodeExecutor.getFileName(), barcodeExecutor.getBarCodeResults(), xmlDocuments, maxConfidence,
						minConfidence);
			}
		} else {
			LOGGER.error(" No pages found in batch XML.");
			throw new DCMAApplicationException("No pages found in batch XML.");
		}
		LOGGER.info("Processing finished at " + new Date());
	}

	/**
	 * Main method to read barcode information from an image file.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @throws DCMAApplicationException {@link DCMAApplicationException} exception to be thrown
	 */
	public void readBarcode(final String batchInstanceIdentifier, final String pluginName) throws DCMAApplicationException {
		final String switchValue = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, BARCODE_READER_PLUGIN,
				BarcodeProperties.BARCODE_SWITCH);
		if (switchValue.equalsIgnoreCase(BarcodeConstants.SWITCH_ON)) {
			LOGGER.info("Started Processing image at " + new Date());
			// Initialize properties
			LOGGER.info("Initializing properties...");
			final String validExt = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, BARCODE_READER_PLUGIN,
					BarcodeProperties.BARCODE_VALID_EXTNS);
			final String readerTypes = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, BARCODE_READER_PLUGIN,
					BarcodeProperties.BARCODE_READER_TYPES);
			final String maxConfidence = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, BARCODE_READER_PLUGIN,
					BarcodeProperties.MAX_CONFIDENCE);
			final String minConfidence = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, BARCODE_READER_PLUGIN,
					BarcodeProperties.MIN_CONFIDENCE);
			LOGGER.info("Properties Initialized Successfully");
			final String[] validExtensions = validExt.split(BarcodeConstants.SEMICOLON_DELIMITER);
			final String[] appReaderTypes = readerTypes.split(BarcodeConstants.SEMICOLON_DELIMITER);
			final String actualFolderLocation = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceIdentifier)
					+ File.separator + batchInstanceIdentifier;
			List<String> allPages = null;
			final Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);
			try {
				final List<Document> xmlDocuments = batch.getDocuments().getDocument();
				allPages = findAllPagesFromXML(xmlDocuments);
			} catch (final DCMAApplicationException e1) {
				LOGGER.error("Exception while reading from XML" + e1.getMessage());
				throw new DCMAApplicationException(e1.getMessage(), e1);
			}
			final BatchInstanceThread batchInstanceThread = new BatchInstanceThread(batchInstanceIdentifier);

			final List<BarcodeExecutor> barcodeExecutorList = new ArrayList<BarcodeExecutor>();
			if (!allPages.isEmpty()) {
				for (int i = 0; i < allPages.size(); i++) {
					String eachPage = allPages.get(i);
					eachPage = eachPage.trim();
					boolean isFileValid = false;
					if (validExtensions != null && validExtensions.length > 0) {
						for (int l = 0; l < validExtensions.length; l++) {
							if (eachPage.substring(eachPage.indexOf(BarcodeConstants.DOT_DELIMITER) + BarcodeConstants.BARCODE_FIELD_SUFFIX).equalsIgnoreCase(
									validExtensions[l])) {
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
						final BarcodeExecutor barcodeExecutor = new BarcodeExecutor(actualFolderLocation + File.separator + eachPage,
								eachPage, appReaderTypes);
						barcodeExecutorList.add(barcodeExecutor);
						batchInstanceThread.add(barcodeExecutor);
						LOGGER.info("Done with Zxing library for image : " + eachPage);
					} else {
						LOGGER.error("File " + eachPage + " has invalid extension.");
						throw new DCMABusinessException("File " + eachPage + " has invalid extension.");
					}
				}
				LOGGER.info("Starting execution through thread pool");
				try {
					batchInstanceThread.execute();
				} catch (final DCMAApplicationException dcmae) {
					LOGGER.error("Error in generating thumbnails");
					batchInstanceThread.remove();
					// Throw the exception to set the batch status to Error by Application aspect
					LOGGER.error("Error in generating thumbnails" + dcmae.getMessage(), dcmae);
					throw new DCMAApplicationException(dcmae.getMessage(), dcmae);
				}
				LOGGER.info("Done execution through thread pool");
				for (final BarcodeExecutor barcodeExecutor : barcodeExecutorList) {
					LOGGER.info("updating XML for image :" + barcodeExecutor.getFileName());
					final List<Document> xmlDocuments = batch.getDocuments().getDocument();
					updateBatchXML(barcodeExecutor.getFileName(), barcodeExecutor.getBarCodeResults(), xmlDocuments, maxConfidence,
							minConfidence);
				}
			} else {
				LOGGER.error("No pages found in batch XML.");
				throw new DCMAApplicationException("No pages found in batch XML.");
			}
			LOGGER.info("Saving the data to disk");
			batchSchemaService.updateBatch(batch);
			LOGGER.info("Processing finished at " + new Date());
		} else {
			LOGGER.info("Skipping barcode classification. Switch set as off.");
		}
	}

	/**
	 * This method finds the name of all image files to be processed from batch xml.
	 * 
	 * @param xmlDocuments  {@link List <Document>}
	 * @return List<String>
	 * @throws DCMAApplicationException {@link DCMAApplicationException} exception to be thrown
	 */
	public List<String> findAllPagesFromXML(final List<Document> xmlDocuments) throws DCMAApplicationException {
		final List<String> allPages = new ArrayList<String>();

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
	 * Enum for Barcode Reader types possible values are CODE39, QR, DATAMATRIX.
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
		 * PDF417 barcode type.
		 */
		PDF417, 
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
		 * EAN13 barcode type.
		 */
		EAN13;

		/**
		 * The values as list.
		 * 
		 * @return {@link List<BarcodeReaderTypes>}
		 */
		public static List<BarcodeReaderTypes> valuesAsList() {
			return Arrays.asList(values());
		}
	}

}
