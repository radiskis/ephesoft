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

package com.ephesoft.dcma.imagemagick;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Field;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.DocField.AlternateValues;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.util.FileNameFormatter;

public class ImageOverlayCreator implements ICommonConstants {

	private static final String COMMA = ",";
	private static final String SPACE = " ";
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Simple constructor checks weather IMAGEMAGICK_ENV_VARIABLE is set.
	 */
	public ImageOverlayCreator() {
		String envVariable = System.getenv(IImageMagickCommonConstants.IMAGEMAGICK_ENV_VARIABLE);
		logger.debug("checking if the Image Magick Enviornment variable IM4JAVA_TOOLPATH is set");
		if (envVariable == null || envVariable.isEmpty()) {
			logger.error("Enviornment Variable IM4JAVA_TOOLPATH not set");
			throw new DCMABusinessException("Enviornment Variable IM4JAVA_TOOLPATH not set.");
		}
		logger.debug("Enviornment variable IM4JAVA_TOOLPATH is set value=" + envVariable);
	}

	/**
	 * This method takes bath folder path, batch id as Input and generates overlayed images
	 * 
	 * @param sBatchFolder
	 * @param batchInstanceIdentifier
	 * @throws JAXBException
	 * @throws DCMAApplicationException
	 */
	public void generateOverlayImages(String sBatchFolder, String batchInstanceIdentifier, BatchSchemaService batchSchemaService)
			throws JAXBException, DCMAApplicationException {

		Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);

		File fBatchFolder = new File(sBatchFolder);

		if (!fBatchFolder.exists() || !fBatchFolder.isDirectory()) {
			throw new DCMABusinessException("Improper Folder Specified folder name->" + sBatchFolder);
		}
		logger.info("Finding xml file for Overlyed Images generation in the folder--> " + fBatchFolder);

		logger.info("xml file parsed sucsessfully");
		List<OverlayDetails> listOfOverlayDetails = getListOfOverlayedFiles(fBatchFolder, sBatchFolder, batch, batchInstanceIdentifier);
		StringBuffer sbParameter = new StringBuffer();

		for (int index = 0; index < listOfOverlayDetails.size(); index++) {
			OverlayDetails overlayDetails = null;
			try {
				overlayDetails = listOfOverlayDetails.get(index);
				if (overlayDetails.getFieldValue() == null || overlayDetails.getFieldValue().isEmpty()
						|| overlayDetails.getCoordinatesList().getCoordinates().isEmpty()) {
					continue;
				}
				List<Coordinates> coordinatesList = overlayDetails.getCoordinatesList().getCoordinates();
				for (Coordinates coordinates : coordinatesList) {
					if (coordinates.getX0() == null || coordinates.getX1() == null || coordinates.getY0() == null
							|| coordinates.getY1() == null) {
						continue;
					}
					String ocrFilePath = overlayDetails.getOcrFilePath();
					String overlayFilePath = overlayDetails.getOverlayedFilePath();
					Object[] listOfFiles = {ocrFilePath, overlayFilePath};
					sbParameter.append("rectangle");
					sbParameter.append(SPACE);
					sbParameter.append(coordinates.getX0());
					sbParameter.append(COMMA);
					sbParameter.append(coordinates.getY0());
					sbParameter.append(COMMA);
					sbParameter.append(coordinates.getX1());
					sbParameter.append(COMMA);
					sbParameter.append(coordinates.getY1());
					ConvertCmd convertcmd = new ConvertCmd();
					IMOperation operation = new IMOperation();
					operation.addImage();
					operation.fill("#0AFA");
					String paramerter = sbParameter.toString();
					sbParameter.delete(0, sbParameter.length());
					logger.info("Overlaying image " + ocrFilePath + " Overlay coordinates=" + paramerter + "Overlayed Image="
							+ overlayFilePath);
					operation.draw(paramerter);
					operation.addImage();
					convertcmd.run(operation, listOfFiles);
					addFileToBatchSchema(batch, overlayDetails);
				}
			} catch (Exception ex) {
				logger.error("Problem generating Overlayed Image ");
				// parsedXmlFile.setBatchStatus(BatchStatusType.ERROR);
				// xmlReader.writeXML(BATCH_XSD_SCHEMA_PACKAGE, parsedXmlFile,
				// xmlFile);

			}

		}
		logger.info("Overlayed Image generation complete for batch folder" + sBatchFolder + " persisitng info to xml file");

		batchSchemaService.updateBatch(batch);

	}

	/**
	 * This methods updates the UnMarshelled XML object with the newly created overlay. For efficiency this method does not marshal the
	 * info.
	 * 
	 * @param parsedXmlFile
	 * @param overlayDetails
	 */
	private void addFileToBatchSchema(Batch parsedXmlFile, OverlayDetails overlayDetails) {
		logger.debug("Adding newly generated overlayed image to  parsed batch.xml file overlayed file name="
				+ overlayDetails.getOverlayedFileName());
		int documentIndex = overlayDetails.getDocumentIndex();
		int fieldIndex = overlayDetails.getFieldIndex();
		boolean alternateValueIndicator = overlayDetails.isAlternateValue();
		int alternateValueIndex = overlayDetails.getAlternateValueIndex();
		String overlayedFileName = overlayDetails.getOverlayedFileName();
		List<Document> listOfDocuments = parsedXmlFile.getDocuments().getDocument();
		Document document = listOfDocuments.get(documentIndex);
		if (alternateValueIndicator) {
			Field alternatevalueField = document.getDocumentLevelFields().getDocumentLevelField().get(fieldIndex).getAlternateValues()
					.getAlternateValue().get(alternateValueIndex);
			alternatevalueField.setOverlayedImageFileName(overlayedFileName);
		} else {
			Field documentLevelField = document.getDocumentLevelFields().getDocumentLevelField().get(fieldIndex);
			documentLevelField.setOverlayedImageFileName(overlayedFileName);
		}
	}

	/**
	 * This method creates the list of overlayed files which have to be created. It fetches this info form the UnMarsheled xml file.
	 * 
	 * @param fBatchFolder
	 * @param sBatchFolder
	 * @param parsedXmlFile
	 * @param batchInstanceIdentifier
	 * @return
	 * @throws DCMAApplicationException
	 */
	private List<OverlayDetails> getListOfOverlayedFiles(File fBatchFolder, String sBatchFolder, Batch parsedXmlFile,
			String batchInstanceIdentifier) throws DCMAApplicationException {
		List<OverlayDetails> listOfOverlayDetails = new LinkedList<OverlayDetails>();
		List<Document> listOfDocuments = parsedXmlFile.getDocuments().getDocument();

		for (int documentIndex = 0; documentIndex < listOfDocuments.size(); documentIndex++) {
			Document document = listOfDocuments.get(documentIndex);

			DocumentLevelFields documentLevelFields = document.getDocumentLevelFields();
			if (documentLevelFields == null) {
				continue;
			}
			List<DocField> listOfFields = documentLevelFields.getDocumentLevelField();
			for (int fieldIndex = 0; fieldIndex < listOfFields.size(); fieldIndex++) {
				OverlayDetails overlayDetails = new OverlayDetails();
				DocField documentField = listOfFields.get(fieldIndex);
				String documentId = document.getIdentifier();
				String pageid = documentField.getPage();
				String fieldName = documentField.getName();
				String fieldValue = documentField.getValue();
				CoordinatesList coordinatesList = documentField.getCoordinatesList();
				overlayDetails.setDocumentId(documentId);
				overlayDetails.setFieldName(fieldName);
				overlayDetails.setDocumentIndex(documentIndex);
				overlayDetails.setFieldIndex(fieldIndex);
				overlayDetails.setPageID(pageid);
				overlayDetails.setAlternateValue(false);
				overlayDetails.setCoordinatesList(coordinatesList);
				overlayDetails.setFieldValue(fieldValue);
				listOfOverlayDetails.add(overlayDetails);
				AlternateValues alternateValues = documentField.getAlternateValues();
				if (alternateValues == null) {
					continue;
				}
				List<Field> listOfAlternateValues = alternateValues.getAlternateValue();
				for (int alternateValueIndex = 0; alternateValueIndex < listOfAlternateValues.size(); alternateValueIndex++) {
					Field alternateValueField = listOfAlternateValues.get(alternateValueIndex);
					OverlayDetails alternateValueoverlayDetails = new OverlayDetails();
					alternateValueoverlayDetails.setDocumentId(documentId);
					alternateValueoverlayDetails.setFieldName(alternateValueField.getName());
					alternateValueoverlayDetails.setDocumentIndex(documentIndex);
					alternateValueoverlayDetails.setFieldIndex(fieldIndex);
					alternateValueoverlayDetails.setAlternateValueIndex(alternateValueIndex);
					alternateValueoverlayDetails.setPageID(alternateValueField.getPage());
					alternateValueoverlayDetails.setCoordinatesList(alternateValueField.getCoordinatesList());
					alternateValueoverlayDetails.setAlternateValue(true);
					listOfOverlayDetails.add(alternateValueoverlayDetails);

				}

			}

		}
		try {
			updateFileNames(listOfOverlayDetails, parsedXmlFile, sBatchFolder, batchInstanceIdentifier);
		} catch (Exception e) {
			throw new DCMAApplicationException("Unable to get the file name", e);
		}
		return listOfOverlayDetails;
	}

	/**
	 * This method updtaes the names of files of the Objects found in the list.
	 * 
	 * @param listOfOverlayDetails
	 * @param parsedXmlFile
	 * @param sBatchFolder
	 * @param batchInstanceIdentifier
	 * @throws Exception
	 */
	private void updateFileNames(List<OverlayDetails> listOfOverlayDetails, Batch parsedXmlFile, String sBatchFolder,
			String batchInstanceIdentifier) throws Exception {
		FileNameFormatter formater = new FileNameFormatter();
		for (int index = 0; index < listOfOverlayDetails.size(); index++) {
			OverlayDetails overlayDetails = listOfOverlayDetails.get(index);
			String ocrFileName = getOcrFileName(overlayDetails.getPageID(), overlayDetails.getDocumentIndex(), parsedXmlFile);
			String oldFileName = getOldFileName(overlayDetails.getPageID(), overlayDetails.getDocumentIndex(), parsedXmlFile);
			String ocrFilePath = sBatchFolder + File.separator + ocrFileName;
			if (ocrFileName == null || oldFileName == null) {
				continue;
			}
			String overlayedFileName = formater.getOverlayedFileName(batchInstanceIdentifier, overlayDetails.getDocumentId(),
					ocrFileName, overlayDetails.getFieldName(), overlayDetails.getAlternateValueIndex(), overlayDetails
							.isAlternateValue(), FileType.PNG.getExtensionWithDot());

			String overlayedFilePath = sBatchFolder + File.separator + overlayedFileName;
			overlayDetails.setOcrFileName(ocrFileName);
			overlayDetails.setOcrFilePath(ocrFilePath);
			overlayDetails.setOverlayedFileName(overlayedFileName);
			overlayDetails.setOverlayedFilePath(overlayedFilePath);

		}

	}

	/**
	 * This method gets the name of OCR file for a given pageId.
	 * 
	 * @param pageId
	 * @param documentIndex
	 * @param parsedXMLFile
	 * @return nameOfOCRedFile
	 */
	private String getOcrFileName(String pageId, int documentIndex, Batch parsedXMLFile) {
		Document document = parsedXMLFile.getDocuments().getDocument().get(documentIndex);
		List<Page> listOfPages = document.getPages().getPage();
		String nameOfOCRedFile = null;
		for (int i = 0; i < listOfPages.size(); i++) {
			if (listOfPages.get(i).getIdentifier().equals(pageId)) {
				nameOfOCRedFile = listOfPages.get(i).getNewFileName();
				break;
			}
		}
		return nameOfOCRedFile;

	}

	/**
	 * This method returns the OldFile Name for a given Page Id.
	 * 
	 * @param pageId
	 * @param documentIndex
	 * @param parsedXMLFile
	 * @return nameOriginalFile
	 */
	private String getOldFileName(String pageId, int documentIndex, Batch parsedXMLFile) {
		Document document = parsedXMLFile.getDocuments().getDocument().get(documentIndex);
		List<Page> listOfPages = document.getPages().getPage();
		String nameOriginalFile = null;
		for (int i = 0; i < listOfPages.size(); i++) {
			if (listOfPages.get(i).getIdentifier().equals(pageId)) {
				nameOriginalFile = listOfPages.get(i).getOldFileName();
				break;
			}
		}
		return nameOriginalFile;

	}

}
