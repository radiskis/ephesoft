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

package com.ephesoft.dcma.tabbed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Batch.Documents;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.tabbed.constant.TabbedPdfConstant;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.OSUtil;

/**
 * This class creates Tabbed Pdf.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.da.service.BatchClassService
 */
@Component
public class TabbedPdfExporter implements ICommonConstants {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(TabbedPdfExporter.class);

	/**
	 * ghostScriptCommand String.
	 */
	private String ghostScriptCommand;

	/**
	 * unixGhostScriptCommand String.
	 */
	private String unixGhostScriptCommand;

	/**
	 * To get Ghost Script Command.
	 * @return String
	 */
	public String getGhostScriptCommand() {
		return ghostScriptCommand;
	}

	/**
	 * To set Ghost Script Command.
	 * @param ghostScriptCommand String
	 */
	public void setGhostScriptCommand(String ghostScriptCommand) {
		this.ghostScriptCommand = ghostScriptCommand;
	}

	/**
	 * To get Unix Ghost Script Command.
	 * @return String
	 */
	public String getUnixGhostScriptCommand() {
		return unixGhostScriptCommand;
	}

	/**
	 * To set Unix Ghost Script Command.
	 * @param unixGhostScriptCommand String
	 */
	public void setUnixGhostScriptCommand(String unixGhostScriptCommand) {
		this.unixGhostScriptCommand = unixGhostScriptCommand;
	}

	/**
	 * Instance of {@link BatchSchemaService}.
	 **/
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Instance of {@link BatchClassService}.
	 */
	@Autowired
	private BatchClassService batchClassService;

	/**
	 * Instance of {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * Instance of {@link PluginPropertiesService}.
	 **/
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * To get Batch Schema Service.
	 * @return the batchSchemaService
	 */
	public BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * To get Plugin Properties Service.
	 * @return the pluginPropertiesService
	 */
	public PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	/**
	 * This will create the tabbed pdf for the given batchInstance.
	 * 
	 * @param batchInstanceIdentifier(@link String)
	 * @throws DCMAApplicationException in case of error
	 */
	public void createTabbedPDF(String batchInstanceIdentifier) throws DCMAApplicationException {
		String tabbedPDFSwitch = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				TabbedPdfConstant.TABBED_PDF_PLUGIN, TabbedPdfProperties.TABBED_PDF_SWITCH);
		if (TabbedPdfConstant.SWITCH_ON.equalsIgnoreCase(tabbedPDFSwitch)) {
			String sFolderToBeExported = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
					TabbedPdfConstant.TABBED_PDF_PLUGIN, TabbedPdfProperties.TABBED_PDF_EXPORT_FOLDER);
			String placeHolder = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
					TabbedPdfConstant.TABBED_PDF_PLUGIN, TabbedPdfProperties.TABBED_PDF_PLACEHOLDER);
			String pdfCreationParam = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
					TabbedPdfConstant.TABBED_PDF_PLUGIN, TabbedPdfProperties.TABBED_PDF_CREATION_PARAMETERS);
			String pdfOptimizationParam = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
					TabbedPdfConstant.TABBED_PDF_PLUGIN, TabbedPdfProperties.TABBED_PDF_OPTIMIZATION_PARAMETERS);
			String propertyFile = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
					TabbedPdfConstant.TABBED_PDF_PLUGIN, TabbedPdfProperties.TABBED_PDF_PROPERTY_FILE);
			String pdfOptimizationSwitch = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
					TabbedPdfConstant.TABBED_PDF_PLUGIN, TabbedPdfProperties.TABBED_PDF_OPTIMIZATION_SWITCH);
			Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);
			boolean exportFolderExists = checkForExportFolderAndCreateIfNotExists(sFolderToBeExported);
			File fFolderToBeExported = new File(sFolderToBeExported);
			if (!exportFolderExists) {
				throw new DCMAApplicationException(fFolderToBeExported.toString() + " is not a Directory.");
			}
			// checkForUnknownDocument(batch);
			Map<String, List<String>> documentPDFMap;
			try {
				if (TabbedPdfConstant.YES.equalsIgnoreCase(placeHolder)) {
					documentPDFMap = createBatchClassDocumentPDFMap(batch, batchInstanceIdentifier, propertyFile);
				} else {
					documentPDFMap = createDocumentPDFMap(batch, batchInstanceIdentifier);
				}
			} catch (Exception e) {
				throw new DCMAApplicationException("Error in writing pdfMarks file." + e.getMessage(), e);
			}

			String envVariable = System.getenv(TabbedPdfConstant.GHOSTSCRIPT_HOME);
			if (envVariable == null || envVariable.isEmpty()) {
				throw new DCMAApplicationException("Enviornment Variable GHOSTSCRIPT_HOME not set.");
			}
			try {
				writePDFMarksFile(sFolderToBeExported, pdfCreationParam, batchInstanceIdentifier, pdfOptimizationParam,
						pdfOptimizationSwitch, documentPDFMap);
			} catch (Exception e) {
				LOGGER.error("Error in writing pdfMarks file" + e.getMessage(), e);
				throw new DCMAApplicationException("Error in writing pdfMarks file." + e.getMessage(), e);
			}

			LOGGER.info("Processing complete at " + new Date());
		} else {
			LOGGER.info("Skipping tabbed pdf plugin. Switch set as OFF");
		}
	}

	/**
	 * 
	 * This will create the PDFMarks file.
	 * 
	 * @param sFolderToBeExported String
	 * @param pdfCreationParam String
	 * @param pdfOptimizationParam String
	 * @param pdfOptimizationSwitch String
	 * @param batchInstanceIdentifier String
	 * @param documentPDFMap Map<String, List<String>>
	 * @throws DCMAApplicationException in case of error
	 * 
	 */
	private void writePDFMarksFile(String sFolderToBeExported, String pdfCreationParam, String batchInstanceIdentifier,
			String pdfOptimizationParam, String pdfOptimizationSwitch, Map<String, List<String>> documentPDFMap) throws IOException,
			DCMAApplicationException {
		String gsCommand = getGSCommand();
		if (gsCommand == null) {
			LOGGER.info(TabbedPdfConstant.GHOSTCRIPT_COMMAND_NOT_FOUND + gsCommand);
			throw new DCMAApplicationException(TabbedPdfConstant.GHOSTCRIPT_COMMAND_NOT_FOUND + gsCommand);
		}
		Set<String> documentNames;
		documentNames = documentPDFMap.keySet();
		Iterator<String> iterator = documentNames.iterator();
		List<String> documentPDFPaths = new ArrayList<String>();

		String documentId;
		Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);

		String pdfMarkTemplatePath = batchSchemaService.getAbsolutePath(batch.getBatchClassIdentifier(), batchSchemaService
				.getScriptConfigFolderName(), true);
		File pdfMarksSample = new File(pdfMarkTemplatePath + File.separator + TabbedPdfConstant.PDF_MARKS_FILE_NAME);
		if (!pdfMarksSample.exists()) {
			throw new DCMAApplicationException("Sample PdfMarks file not provided.");
		}
		File localPdfMarksSample = new File(batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceIdentifier)
				+ File.separator + batchInstanceIdentifier + File.separator + pdfMarksSample.getName());
		try {
			FileUtils.copyFile(pdfMarksSample, localPdfMarksSample);
		} catch (Exception e) {
			throw new DCMAApplicationException("Exception in copying the file \"" + localPdfMarksSample.getAbsolutePath() + "\" to \""
					+ pdfMarksSample.getAbsolutePath() + TabbedPdfConstant.QUOTES, e);

		}
		String pdfBookMarkTemplate = TabbedPdfConstant.PDF_MARKS_TEMPLATE;
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(localPdfMarksSample, true);
			String pdfBookMarkText = TabbedPdfConstant.EMPTY;
			while (iterator.hasNext()) {
				documentId = iterator.next();
				List<String> docDetails = documentPDFMap.get(documentId);
				pdfBookMarkText = pdfBookMarkTemplate.replace(TabbedPdfConstant.BOOKMARK_TITLE_PLACEHOLDER, docDetails.get(0));// page
				// bookmark
				pdfBookMarkText = pdfBookMarkText.replace(TabbedPdfConstant.BOOKMARK_PAGE_NUMBER_PLACEHOLDER, docDetails.get(1));// page
				// title
				fileWriter.write(pdfBookMarkText + System.getProperty(TabbedPdfConstant.LINE_SEPARATOR));
				documentPDFPaths.add(docDetails.get(2));
			}
		} catch (IOException ioException) {
			throw new DCMAApplicationException("Exception in getting the FileWriter.", ioException);

		} finally {
			try {
				if (fileWriter != null) {
					fileWriter.close();
				}
			} catch (IOException e) {
				LOGGER.info("Unable to close the file stream for file:\"" + localPdfMarksSample.getAbsolutePath() + "\"");
			}

		}
		BatchInstanceThread batchInstanceThread = new BatchInstanceThread(batchInstanceIdentifier);

		List<TabbedPDFExecutor> tabbedPDFExecutors = new ArrayList<TabbedPDFExecutor>();
		String batchName = TabbedPdfConstant.EMPTY;
		Map<String, String> subPoenaLoanMap = getSubPoenaLoanNumber(batch.getDocuments().getDocument());
		if (subPoenaLoanMap.size() == 2) {
			batchName = subPoenaLoanMap.get(TabbedPdfConstant.SUBPOENA) + TabbedPdfConstant.UNDERSCORE
					+ subPoenaLoanMap.get(TabbedPdfConstant.LOAN_NUMBER);
		}
		if (batchName == null || batchName.isEmpty()) {
			batchName = batch.getBatchName();
		}
		String tabbedPDFName = batchName + TabbedPdfConstant.UNDERSCORE + batchInstanceIdentifier + FileType.PDF.getExtensionWithDot();
		String tabbedPDFTempFolder = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceIdentifier) + File.separator
				+ batchInstanceIdentifier;
		String tabbedPDFLocalPath = tabbedPDFTempFolder + File.separator + tabbedPDFName;
		if (pdfOptimizationSwitch != null && pdfOptimizationSwitch.equalsIgnoreCase(TabbedPdfConstant.SWITCH_ON)) {
			tabbedPDFExecutors.add(new TabbedPDFExecutor(tabbedPDFName, tabbedPDFTempFolder, documentPDFPaths, localPdfMarksSample
					.getAbsolutePath(), batchInstanceThread, pdfCreationParam, gsCommand));
		} else {
			tabbedPDFExecutors.add(new TabbedPDFExecutor(tabbedPDFName, sFolderToBeExported, documentPDFPaths, localPdfMarksSample
					.getAbsolutePath(), batchInstanceThread, pdfCreationParam, gsCommand));
		}
		try {
			LOGGER.info("Executing commands for creation of tabbed pdf using thread pool.");
			batchInstanceThread.execute();
			LOGGER.info("Tabbed pdf creation ends.");
		} catch (DCMAApplicationException dcmae) {
			LOGGER.error("Error in executing command for tabbed pdf using thread pool" + dcmae.getMessage(), dcmae);
			batchInstanceThread.remove();
			// Throw the exception to set the batch status to Error by Application aspect
			throw new DCMAApplicationException(dcmae.getMessage(), dcmae);
		}
		checkForPdfOptimizationSwitch(sFolderToBeExported, batchInstanceIdentifier, pdfOptimizationParam, pdfOptimizationSwitch,
				tabbedPDFName, tabbedPDFTempFolder);

		copyMultipagePdfInEphesoftSystemFolder(sFolderToBeExported, tabbedPDFName, tabbedPDFLocalPath);
		mergeBatchXmlDocs(batchSchemaService, batch, documentNames, documentPDFMap, tabbedPDFName);
	}

	/**
	 * It will copy the multipage pdf file in ephesoft system folder.
	 * 
	 * @param sFolderToBeExported{@link String}
	 * @param tabbedPDFName{@link String}
	 * @param tabbedPDFLocalPath{@link String}
	 * @throws DCMAApplicationException in case of error
	 */
	private void copyMultipagePdfInEphesoftSystemFolder(String sFolderToBeExported, String tabbedPDFName, String tabbedPDFLocalPath)
			throws DCMAApplicationException {
		File destFile = new File(tabbedPDFLocalPath);
		if (destFile.exists()) {
			destFile.delete();
		}
		// copying file to ephesoft system folder
		LOGGER.info("Started copying multipage pdf file in ephesoft system folder.");
		File srcFile = new File(sFolderToBeExported + File.separator + tabbedPDFName);
		destFile = new File(tabbedPDFLocalPath);
		try {
			FileUtils.copyFile(srcFile, destFile);
		} catch (Exception e) {
			throw new DCMAApplicationException("Exception in copying the file  " + destFile.getAbsolutePath() + " to "
					+ srcFile.getAbsolutePath(), e);
		}
		LOGGER.info("Completed copying multipage pdf file in ephesoft system folder.");
	}

	/**
	 * This will check for the Pdf optimization switch and if set to "ON" ,adds the new object of TabbedPdfOptimizer to the Arraylist
	 * tabbedPdfOptimier.
	 * 
	 * @param sFolderToBeExported{@link String}
	 * @param batchInstanceIdentifier{@link String}
	 * @param pdfOptimizationParam{@link String}
	 * @param pdfOptimizationSwitch{@link String}
	 * @param tabbedPDFName{@link String}
	 * @param tabbedPDFTempFolder{@link String}
	 * @throws DCMAApplicationException in acse of error
	 */
	private void checkForPdfOptimizationSwitch(String sFolderToBeExported, String batchInstanceIdentifier,
			String pdfOptimizationParam, String pdfOptimizationSwitch, String tabbedPDFName, String tabbedPDFTempFolder)
			throws DCMAApplicationException {
		String gsCommand = getGSCommand();
		if (gsCommand == null) {
			LOGGER.info(TabbedPdfConstant.GHOSTCRIPT_COMMAND_NOT_FOUND + gsCommand);
			throw new DCMAApplicationException(TabbedPdfConstant.GHOSTCRIPT_COMMAND_NOT_FOUND + gsCommand);
		}
		BatchInstanceThread batchInstanceThread;
		if (pdfOptimizationSwitch != null && pdfOptimizationSwitch.equalsIgnoreCase(TabbedPdfConstant.SWITCH_ON)) {
			batchInstanceThread = new BatchInstanceThread(batchInstanceIdentifier);
			List<TabbedPdfOptimizer> tabbedPdfOptimier = new ArrayList<TabbedPdfOptimizer>();

			tabbedPdfOptimier.add(new TabbedPdfOptimizer(sFolderToBeExported, tabbedPDFTempFolder, batchInstanceThread, tabbedPDFName,
					pdfOptimizationParam, gsCommand));
			try {
				LOGGER.info("Executing commands for optimizing tabbed pdf using thread pool.");
				batchInstanceThread.execute();
				LOGGER.info("Tabbed pdf creation ends.");
			} catch (DCMAApplicationException dcmae) {
				LOGGER.error("Error in executing command for optimizing tabbed pdf using thread pool" + dcmae.getMessage(), dcmae);
				batchInstanceThread.remove();
				// Throw the exception to set the batch status to Error by Application aspect
				throw new DCMAApplicationException(dcmae.getMessage(), dcmae);
			}
		}
	}

	/**
	 * This will return the ghost script command for window or linux environment.
	 * 
	 * @return {@link String}
	 */
	private String getGSCommand() {
		String gsCommand = null;
		if (OSUtil.isWindows() && ghostScriptCommand != null) {
			gsCommand = ghostScriptCommand;
		} else if (OSUtil.isUnix() && unixGhostScriptCommand != null) {
			gsCommand = unixGhostScriptCommand;
		}
		return gsCommand;
	}

	/**
	 * This API merges all the documents as per priority list into first document. Also, updates the multi page pdf file name to tabbed
	 * pdf file name.
	 * 
	 * @param batchSchemaService{@link BatchSchemaService}
	 * @param batch{@link Batch}
	 * @param documentNames {@link Set<String>}
	 * @param documentPDFMap {@link LinkedHashMap<String, List<String>>}
	 * @param tabbedPDFName {@link String}
	 */
	private void mergeBatchXmlDocs(BatchSchemaService batchSchemaService, Batch batch, Set<String> documentNames,
			Map<String, List<String>> documentPDFMap, String tabbedPDFName) {
		boolean isFirstDocInXML = true;
		String documentIdInt;
		Documents documents = new Documents();
		List<Document> docList = new ArrayList<Document>();
		Iterator<String> iterator = documentNames.iterator();
		while (iterator.hasNext()) {
			documentIdInt = iterator.next();
			List<String> docDetails = documentPDFMap.get(documentIdInt);
			String documentTypeAfterSorting = docDetails.get(0);
			// fetch the document from batch xml and merge with first document as per priority.
			documents = batch.getDocuments();
			docList = documents.getDocument();
			Iterator<Document> docListIter = docList.iterator();
			while (docListIter.hasNext()) {
				Document document = (Document) docListIter.next();
				String docTypeFromXML = document.getType();
				if (docTypeFromXML.equalsIgnoreCase(documentTypeAfterSorting)) {
					if (isFirstDocInXML) {
						// put this document as first document
						docList.add(0, document);
						isFirstDocInXML = false;
					} else {
						// merge the document with first document
						Pages docPages = document.getPages();
						docList.get(0).getPages().getPage().addAll(docPages.getPage());
					}
					docList.remove(document);
					break;
				}
			}
		}
		// update the multipage pdf file name
		docList.get(0).setMultiPagePdfFile(tabbedPDFName);
		batchSchemaService.updateBatch(batch);
	}

	/**
	 * This API created the export folder if it not exists else it will created the export folder.
	 * 
	 * @param exportFolder {@link String}
	 * @return isExportFolderCreated {@link boolean}
	 */
	private boolean checkForExportFolderAndCreateIfNotExists(final String exportFolder) {
		LOGGER.info("Checking the export folder is already created or not");
		File exportFolderFile = new File(exportFolder);
		boolean isExportFolderCreated = false;
		if (!exportFolderFile.exists()) {
			isExportFolderCreated = exportFolderFile.mkdir();
		} else {
			isExportFolderCreated = true;
		}
		LOGGER.info("Value of variable \"isExportFolderCreated:\"" + isExportFolderCreated);
		LOGGER.info("Checking the export folder is already created or not");
		return isExportFolderCreated;
	}

	/**
	 * This will create the document pdf map for.
	 * 
	 * @param parsedXMLFile{@link Batch}
	 * @param batchInstanceID{@link String}
	 * @return documentPDFMap{@link LinkedHashMap<String, List<String>>}
	 * @throws DCMAApplicationException in case of error
	 */
	private Map<String, List<String>> createDocumentPDFMap(final Batch parsedXMLFile, String batchInstanceID)
			throws DCMAApplicationException {

		List<Document> xmlDocuments = parsedXMLFile.getDocuments().getDocument();
		LinkedHashMap<String, List<String>> documentPDFMap = new LinkedHashMap<String, List<String>>();
		int startPageNumber = 1;
		for (Document document : xmlDocuments) {
			List<String> detailsList = new LinkedList<String>();
			String documentId = document.getIdentifier();
			LOGGER.info("Document documentid =" + documentId + " contains the following info:");
			int numberOfPages = document.getPages().getPage().size();
			// adding details to the document
			addDetailsToDocTypeWithoutPlaceHolder(document, startPageNumber, detailsList, batchInstanceID);
			startPageNumber = startPageNumber + numberOfPages;
			documentPDFMap.put(documentId, detailsList);
		}
		return documentPDFMap;
	}

	/**
	 * This will create the document map on the basis of order and load the error pdf if the document is not present.
	 * 
	 * @param parsedXMLFile {@link Batch}
	 * @param batchInstanceID{@link String}
	 * @param propertyFile{@link String}
	 * @return documentPDFMap {@link LinkedHashMap<String, List<String>>}
	 * @throws DCMAApplicationException in case of error
	 */
	private Map<String, List<String>> createBatchClassDocumentPDFMap(final Batch parsedXMLFile, String batchInstanceID,
			String propertyFile) throws DCMAApplicationException {
		String errorPDFPath = batchSchemaService.getAbsolutePath(parsedXMLFile.getBatchClassIdentifier(), batchSchemaService
				.getScriptConfigFolderName(), true);
		List<String> batchDocumentTypeNameList = new ArrayList<String>();
		String batchClassId = parsedXMLFile.getBatchClassIdentifier();
		BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);
		List<DocumentType> batchDocumentList = batchClass.getDocumentTypes();
		for (DocumentType docType : batchDocumentList) {
			if (!docType.isHidden()) {
				batchDocumentTypeNameList.add(docType.getName());
			}
		}
		List<String> sortedDocumentList = new ArrayList<String>();
		Map<String, Integer> propMap = fetchDocNameMapping(propertyFile);
		List<String> mapKeys = new ArrayList<String>(propMap.keySet());
		List<Integer> mapValues = new ArrayList<Integer>(propMap.values());
		TreeSet<Integer> sortedSet = new TreeSet<Integer>(mapValues);
		if (sortedSet.size() != propMap.size()) {
			LOGGER.error("Same priority is defined for more than one document type. Invalid scenario.");
			throw new DCMAApplicationException("Property file for documents not valid");
		} else {
			Object[] sortedArray = sortedSet.toArray();
			int size = sortedArray.length;
			for (int i = 0; i < size; i++) {
				String documentType = (String) mapKeys.get(mapValues.indexOf(sortedArray[i]));
				for (int documentIndex = 0; documentIndex < batchDocumentTypeNameList.size(); documentIndex++) {
					if (documentType.equals(batchDocumentTypeNameList.get(documentIndex))) {
						sortedDocumentList.add(batchDocumentTypeNameList.get(documentIndex));
					}
				}
			}
			List<Document> xmlDocuments = parsedXMLFile.getDocuments().getDocument();
			// check if any document in batch xml is not present in export props then send the batch to error.
			checkIfAnyXmlDocIsNotInProps(sortedDocumentList, xmlDocuments);
			LinkedHashMap<String, List<String>> documentPDFMap = new LinkedHashMap<String, List<String>>();
			int startPageNumber = 1;
			int docIdentifier = 1;
			int batchDocumentIndex = 0;
			for (String document : sortedDocumentList) {
				List<String> detailsList = new LinkedList<String>();
				String documentId = TabbedPdfConstant.DOCUMENT_IDENTIFIER + docIdentifier;
				int numberOfPages;
				String documentType;
				String pdfFile = null;

				if (batchDocumentIndex + 1 <= xmlDocuments.size()) {
					Document xmlDocument = xmlDocuments.get(batchDocumentIndex);
					if (document.equals(xmlDocument.getType())) {
						List<Page> listOfPages = xmlDocuments.get(batchDocumentIndex).getPages().getPage();
						LOGGER.info("Document documentid =" + documentId + " contains the following info:");
						numberOfPages = listOfPages.size();
						documentType = xmlDocument.getType();
						pdfFile = xmlDocument.getMultiPagePdfFile();
						detailsList.add(documentType);
						detailsList.add(String.valueOf(startPageNumber));
						if (pdfFile != null && !pdfFile.isEmpty()) {
							File fPDFFile = batchSchemaService.getFile(batchInstanceID, pdfFile);
							if (fPDFFile.exists()) {
								LOGGER.info("PDF File Name:" + fPDFFile);
								detailsList.add(fPDFFile.getAbsolutePath());

							} else {
								throw new DCMAApplicationException("File does not exist. File Name=" + fPDFFile);
							}

							docIdentifier++;
							startPageNumber = startPageNumber + numberOfPages;
							documentPDFMap.put(documentId, detailsList);
						} else {
							throw new DCMAApplicationException("MultiPagePDF file does not exist in batch xml.");
						}
						batchDocumentIndex++;
					} else {
						startPageNumber = appendPlaceholder(errorPDFPath, xmlDocuments, documentPDFMap, startPageNumber, document,
								detailsList, documentId);
						docIdentifier++;
					}
				} else {
					startPageNumber = appendPlaceholder(errorPDFPath, xmlDocuments, documentPDFMap, startPageNumber, document,
							detailsList, documentId);
					docIdentifier++;
				}
			}
			return documentPDFMap;
		}
	}

	/**
	 * It checks if any document in batch xml is not present in export props then send the batch to error.
	 * 
	 * @param sortedDocumentList List<String>
	 * @param xmlDocuments List<Document>
	 * @throws DCMAApplicationException in case of error
	 */
	private void checkIfAnyXmlDocIsNotInProps(List<String> sortedDocumentList, List<Document> xmlDocuments)
			throws DCMAApplicationException {
		boolean ifAnyXmlDocIsNotInProps = false;
		String docTypeNotPresent = null;
		for (Document doc : xmlDocuments) {
			if (!sortedDocumentList.contains(doc.getType())) {
				ifAnyXmlDocIsNotInProps = true;
				docTypeNotPresent = doc.getType();
				break;
			}
		}
		if (ifAnyXmlDocIsNotInProps) {
			LOGGER.error("Document Type:" + docTypeNotPresent + " is not present in Export Properties.");
			throw new DCMAApplicationException("Document Type:" + docTypeNotPresent + " is not present in Export Properties.");
		}
	}

	/**
	 * This method appends placeholder PDF in case placeholder switch is "ON".
	 * 
	 * @param errorPDFPath{@link String}
	 * @param xmlDocuments{@link List<Document>}
	 * @param documentPDFMap{@link LinkedHashMap<String, List<String>>}
	 * @param startPageNumber{@link int}
	 * @param document{@link String}
	 * @param detailsList{@link List<String>}
	 * @param documentId{@link documentId}
	 * @return startPageNumber{@link int}
	 * @throws DCMAApplicationException in case of error
	 */
	private int appendPlaceholder(String errorPDFPath, List<Document> xmlDocuments, Map<String, List<String>> documentPDFMap,
			final int startPageNumber, String document, List<String> detailsList, String documentId) throws DCMAApplicationException {
		int startPage = startPageNumber;
		int numberOfPages;
		String documentType;
		String pdfFile;
		String xmlDoc = TabbedPdfConstant.EMPTY;
		boolean isDocExistInXML = false;
		for (Document doc : xmlDocuments) {
			if (doc.getType().equals(document)) {
				xmlDoc = document;
				isDocExistInXML = true;
				break;
			}
		}
		if (isDocExistInXML) {
			throw new DCMAApplicationException("Document type " + xmlDoc
					+ " is not present in batch xml at the specified order in properties file. Throwing workflow into error.");
		} else {
			// append error.pdf
			documentType = document;
			numberOfPages = 1;
			pdfFile = TabbedPdfConstant.SAMPLE_PDF_FILE_NAME;
			detailsList.add(documentType);
			detailsList.add(String.valueOf(startPage));
			if (pdfFile != null && !pdfFile.isEmpty()) {
				File fPDFFile = new File(errorPDFPath + File.separator + TabbedPdfConstant.SAMPLE_PDF_FILE_NAME);
				if (fPDFFile.exists()) {
					LOGGER.info("PDF File Name:" + fPDFFile);
					detailsList.add(fPDFFile.getAbsolutePath());
				} else {
					throw new DCMAApplicationException("File does not exist. File Name=" + fPDFFile);
				}

				startPage = startPage + numberOfPages;
				documentPDFMap.put(documentId, detailsList);
			} else {
				throw new DCMAApplicationException("MultiPagePDF file does not exist in batch xml.");
			}
		}
		return startPage;
	}

	/**
	 * This will add the details to the given document when the place-holder property is not present.
	 * 
	 * @param document {@link Document}
	 * @param startPageNumber {@link int}
	 * @param detailsList {@link List<String>}
	 * @param batchInstanceID {@link String}
	 * @throws DCMAApplicationException
	 */
	private void addDetailsToDocTypeWithoutPlaceHolder(Document document, int startPageNumber, List<String> detailsList,
			String batchInstanceID) throws DCMAApplicationException {
		String documentType = document.getType();
		String pdfFile = document.getMultiPagePdfFile();
		detailsList.add(documentType);
		detailsList.add(String.valueOf(startPageNumber));
		if (pdfFile != null && !pdfFile.isEmpty()) {
			File fPDFFile = batchSchemaService.getFile(batchInstanceID, pdfFile);
			if (fPDFFile.exists()) {
				LOGGER.info("PDF File Name:" + fPDFFile);
				detailsList.add(fPDFFile.getAbsolutePath());
			} else {
				throw new DCMAApplicationException("File does not exist. File Name=" + fPDFFile);
			}
		} else {
			throw new DCMAApplicationException("MultiPagePDF file does not exist in batch xml.");
		}

	}

	/**
	 * This will fetch the document from the property file.
	 * 
	 * @param propertyFilePath{@link String}
	 * @return Map<String, Integer>
	 * @throws DCMAApplicationException in case of error
	 */
	private Map<String, Integer> fetchDocNameMapping(String propertyFilePath) throws DCMAApplicationException {
		final String filePath = propertyFilePath;
		HashMap<String, Integer> propertyMap = new HashMap<String, Integer>();
		FileInputStream fileInputStream = null;
		BufferedReader bufferReader = null;
		try {
			fileInputStream = new FileInputStream(filePath);
			bufferReader = new BufferedReader(new InputStreamReader(fileInputStream));
			String eachLine = bufferReader.readLine();
			while (eachLine != null) {
				if (eachLine.length() > 0) {
					final String[] keyValue = eachLine.split(TabbedPdfConstant.MAPPING_SEPERATOR);
					if (keyValue != null && keyValue.length == 2) {
						propertyMap.put(keyValue[0], Integer.parseInt(keyValue[1]));
					}
				}
				eachLine = bufferReader.readLine();
			}
		} catch (IOException e) {
			throw new DCMAApplicationException("Property file not provided.Cannot be exported.", e);
		} finally {
			try {
				if (bufferReader != null) {
					bufferReader.close();
				}
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (IOException e) {
				LOGGER.info("Unable to close the fileInputStream or bufferReader for the file:" + filePath);
			}
		}
		return propertyMap;
	}

	/**
	 * This will fetch the SubPoenaLoanNumber.
	 * 
	 * @param documentList{@link List<Document>}
	 * @return resultMap {@link Map<String, String>}
	 */
	private Map<String, String> getSubPoenaLoanNumber(List<Document> documentList) {
		HashMap<String, String> resultMap = new HashMap<String, String>();
		for (Document document : documentList) {
			if (resultMap.size() == 2) {
				break;
			}
			DocumentLevelFields docFields = document.getDocumentLevelFields();
			List<DocField> docFieldList = docFields.getDocumentLevelField();
			for (DocField docField : docFieldList) {
				if ((docField.getName().equals(TabbedPdfConstant.SUBPOENA))
						|| (docField.getName().equals(TabbedPdfConstant.LOAN_NUMBER))) {
					resultMap.put(docField.getName(), docField.getValue());
				}
			}
		}
		return resultMap;
	}
}
