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

package com.ephesoft.dcma.barcode;

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
public class BarcodeReader implements ICommonConstants {

	private static final char DOT_DELIMITER = '.';

	private static final String SEMICOLON_DELIMITER = ";";

	private static final String EMPTY_STRING = "";

	private static final int BARCODE_FIELD_SUFFIX = 1;

	private static final String BARCODE_READER_PLUGIN = "BARCODE_READER";

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
	private static final Logger LOGGER = LoggerFactory.getLogger(BarcodeReader.class);

	private static final String SWITCH_ON = "ON";

	/**
	 * The prefix for name to be stored in batch xml.
	 */
	private transient String barcodeName;

	/**
	 * Reference firstPage name.
	 */
	private transient String firstPage;

	/**
	 * @param barcodeName
	 */
	public void setBarcodeName(final String barcodeName) {
		this.barcodeName = barcodeName;
	}

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
	 * @param fileName String
	 * @param barCodeResults BarcodeResult[]
	 * @param batch Batch
	 * @throws DCMAApplicationException
	 */
	public void updateBatchXML(final String fileName, final BarcodeResult[] barCodeResults, final Batch batch,
			final String maxConfidence, final String minConfidence) throws DCMAApplicationException {

		List<Document> xmlDocuments = batch.getDocuments().getDocument();
		for (int i = 0; i < xmlDocuments.size(); i++) {
			Document document = xmlDocuments.get(i);
			List<Page> listOfPages = document.getPages().getPage();
			for (int j = 0; j < listOfPages.size(); j++) {
				Page page = listOfPages.get(j);
				String sImageFile = page.getNewFileName();
				PageLevelFields pageLevelFields = page.getPageLevelFields();
				if (pageLevelFields == null) {
					pageLevelFields = new PageLevelFields();
				}
				if (fileName.equalsIgnoreCase(sImageFile)) {
					if (barCodeResults != null && barCodeResults.length > 0) {
						DocField docFieldType = new DocField();
						AlternateValues alternateValues = docFieldType.getAlternateValues();
						for (int k = 0; k < barCodeResults.length; k++) {
							if (k == 0) {
								docFieldType.setName(barcodeName + (k + 1));
								String value = barCodeResults[k].getTexts();

								if (null != value && !value.isEmpty()) {
									value = value + getFirstPage();
								}

								docFieldType.setValue(value);
								docFieldType.setType(barCodeResults[k].getBarcodeType().name());
								if (!value.isEmpty()) {
									docFieldType.setConfidence(Integer.valueOf(maxConfidence));
								} else {
									docFieldType.setConfidence(Integer.valueOf(minConfidence));
								}
								Coordinates coordinates = new Coordinates();
								coordinates.setX0(new BigInteger(String.valueOf((int) barCodeResults[k].getX0Cord())));
								coordinates.setX1(new BigInteger(String.valueOf((int) barCodeResults[k].getX1Cord())));
								coordinates.setY0(new BigInteger(String.valueOf((int) barCodeResults[k].getY0Cord())));
								coordinates.setY1(new BigInteger(String.valueOf((int) barCodeResults[k].getY1Cord())));
								CoordinatesList coordinatesList = new CoordinatesList();
								coordinatesList.getCoordinates().add(coordinates);
								docFieldType.setCoordinatesList(coordinatesList);
								pageLevelFields.getPageLevelField().add(docFieldType);
							} else {
								if (alternateValues == null) {
									alternateValues = new AlternateValues();
								}
								Field fieldType = new Field();
								fieldType.setName(barcodeName + (k + 1));
								String value = barCodeResults[k].getTexts();
								fieldType.setValue(value);
								fieldType.setType(barCodeResults[k].getBarcodeType().name());
								if (!value.isEmpty()) {
									fieldType.setConfidence(Integer.valueOf(maxConfidence));
								} else {
									fieldType.setConfidence(Integer.valueOf(minConfidence));
								}
								Coordinates coordinates = new Coordinates();
								coordinates.setX0(new BigInteger(String.valueOf((int) barCodeResults[k].getX0Cord())));
								coordinates.setX1(new BigInteger(String.valueOf((int) barCodeResults[k].getX1Cord())));
								coordinates.setY0(new BigInteger(String.valueOf((int) barCodeResults[k].getY0Cord())));
								coordinates.setY1(new BigInteger(String.valueOf((int) barCodeResults[k].getY1Cord())));
								CoordinatesList coordinatesList = new CoordinatesList();
								coordinatesList.getCoordinates().add(coordinates);
								fieldType.setCoordinatesList(coordinatesList);
								alternateValues.getAlternateValue().add(fieldType);
							}
						}
						docFieldType.setAlternateValues(alternateValues);
						page.setPageLevelFields(pageLevelFields);
					} else {
						DocField docFieldType = new DocField();
						docFieldType.setName(barcodeName + BARCODE_FIELD_SUFFIX);
						docFieldType.setValue(EMPTY_STRING);
						docFieldType.setType(EMPTY_STRING);
						docFieldType.setConfidence(Integer.valueOf(minConfidence));
						Coordinates coordinates = new Coordinates();
						coordinates.setX0(new BigInteger(DEFAULT_COORDINATES_VALUE));
						coordinates.setX1(new BigInteger(DEFAULT_COORDINATES_VALUE));
						coordinates.setY0(new BigInteger(DEFAULT_COORDINATES_VALUE));
						coordinates.setY1(new BigInteger(DEFAULT_COORDINATES_VALUE));
						CoordinatesList coordinatesList = new CoordinatesList();
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
		String switchValue = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, BARCODE_READER_PLUGIN,
				BarcodeProperties.BARCODE_SWITCH);
		if (switchValue.equalsIgnoreCase(SWITCH_ON)) {
			LOGGER.info("Started Processing image at " + new Date());
			// Initialize properties
			LOGGER.info("Initializing properties...");
			String validExt = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, BARCODE_READER_PLUGIN,
					BarcodeProperties.BARCODE_VALID_EXTNS);
			String readerTypes = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, BARCODE_READER_PLUGIN,
					BarcodeProperties.BARCODE_READER_TYPES);
			String maxConfidence = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, BARCODE_READER_PLUGIN,
					BarcodeProperties.MAX_CONFIDENCE);
			String minConfidence = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, BARCODE_READER_PLUGIN,
					BarcodeProperties.MIN_CONFIDENCE);
			LOGGER.info("Properties Initialized Successfully");
			String[] validExtensions = validExt.split(SEMICOLON_DELIMITER);
			String[] appReaderTypes = readerTypes.split(SEMICOLON_DELIMITER);
			String actualFolderLocation = batchSchemaService.getLocalFolderLocation() + File.separator + batchInstanceIdentifier;
			List<String> allPages = null;
			Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);
			try {
				allPages = findAllPagesFromXML(batch);
			} catch (DCMAApplicationException e1) {
				LOGGER.error("Exception while reading from XML" + e1.getMessage());
				throw new DCMAApplicationException(e1.getMessage(), e1);
			}
			BatchInstanceThread batchInstanceThread = new BatchInstanceThread(batchInstanceIdentifier);

			List<BarcodeExecutor> barcodeExecutorList = new ArrayList<BarcodeExecutor>();
			if (!allPages.isEmpty()) {
				for (int i = 0; i < allPages.size(); i++) {
					String eachPage = allPages.get(i);
					eachPage = eachPage.trim();
					boolean isFileValid = false;
					if (validExtensions != null && validExtensions.length > 0) {
						for (int l = 0; l < validExtensions.length; l++) {
							if (eachPage.substring(eachPage.indexOf(DOT_DELIMITER) + BARCODE_FIELD_SUFFIX).equalsIgnoreCase(
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
						BarcodeExecutor barcodeExecutor = new BarcodeExecutor(actualFolderLocation + File.separator + eachPage,
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
				} catch (DCMAApplicationException dcmae) {
					LOGGER.error("Error in generating thumbnails");
					batchInstanceThread.remove();
					// Throw the exception to set the batch status to Error by Application aspect
					LOGGER.error("Error in generating thumbnails" + dcmae.getMessage(), dcmae);
					throw new DCMAApplicationException(dcmae.getMessage(), dcmae);
				}
				LOGGER.info("Done execution through thread pool");
				for (BarcodeExecutor barcodeExecutor : barcodeExecutorList) {
					LOGGER.info("updating XML for image :" + barcodeExecutor.getFileName());
					updateBatchXML(barcodeExecutor.getFileName(), barcodeExecutor.getBarCodeResults(), batch, maxConfidence,
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
	 * Enum for Barcode Reader types possible values are CODE39, QR, DATAMATRIX.
	 */
	public static enum BarcodeReaderTypes {
		CODE39, QR, DATAMATRIX;

		public static List<BarcodeReaderTypes> valuesAsList() {
			return Arrays.asList(values());
		}
	}

}
