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
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Batch.Documents;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.EphesoftProperty;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.tabbed.constant.TabbedPdfConstant;
import com.ephesoft.dcma.util.FileUtils;

@Component
public class TabbedPdfExporter implements ICommonConstants {

	private static final Logger LOGGER = LoggerFactory.getLogger(TabbedPdfExporter.class);

	private static final String TABBED_PDF_PLUGIN = "TABBED_PDF";
	private static final String MAPPING_SEPERATOR = "===";
	private static final String YES = "YES";
	private static final String DOCUMENT_IDENTIFIER = "DOC ";

	/**
	 * Instance of BatchSchemaService.
	 **/
	@Autowired
	private BatchSchemaService batchSchemaService;

	@Autowired
	private BatchClassService batchClassService;

	/**
	 * Instance of PluginPropertiesService.
	 **/
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * @return the batchSchemaService
	 */
	public BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * @return the pluginPropertiesService
	 */
	public PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	public void createTabbedPDF(String batchInstanceIdentifier, BatchSchemaService batchSchemaService, String pluginName)
			throws DCMAApplicationException {
		String tabbedPDFSwitch = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, TABBED_PDF_PLUGIN,
				TabbedPdfProperties.TABBED_PDF_SWITCH);
		if ("ON".equalsIgnoreCase(tabbedPDFSwitch)) {
			String sFolderToBeExported = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, TABBED_PDF_PLUGIN,
					TabbedPdfProperties.TABBED_PDF_EXPORT_FOLDER);
			String placeHolder = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, TABBED_PDF_PLUGIN,
					TabbedPdfProperties.TABBED_PDF_PLACEHOLDER);

			String propertyFile = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, TABBED_PDF_PLUGIN,
					TabbedPdfProperties.TABBED_PDF_PROPERTY_FILE);
			this.batchSchemaService = batchSchemaService;
			Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);
			boolean exportFolderExistes = isExportFolderAlreadyCreated(sFolderToBeExported);
			File fFolderToBeExported = new File(sFolderToBeExported);

			if (!exportFolderExistes) {
				throw new DCMAApplicationException(fFolderToBeExported.toString() + " is not a Directory.");
			}
			checkForUnknownDocument(batch);

			LinkedHashMap<String, List<String>> documentPDFMap;
			try {
				if (YES.equalsIgnoreCase(placeHolder)) {
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
			String pdfMarkTemplatePath = batchSchemaService.getAbsolutePath(batch.getBatchClassIdentifier(), batchSchemaService
					.getScriptConfigFolderName(), true);
			File pdfMarksSample = new File(pdfMarkTemplatePath + File.separator + TabbedPdfConstant.PDF_MARKS_FILE_NAME);
			if (!pdfMarksSample.exists()) {
				throw new DCMAApplicationException("Sample PdfMarks file not provided.");
			}
			Set<String> documentNames;
			documentNames = documentPDFMap.keySet();
			Iterator<String> iterator = documentNames.iterator();
			List<String> documentPDFPaths = new ArrayList<String>();

			String documentIdInt;
			try {
				File localPdfMarksSample = new File(batchSchemaService.getLocalFolderLocation() + File.separator
						+ batchInstanceIdentifier + File.separator + pdfMarksSample.getName());
				FileUtils.copyFile(pdfMarksSample, localPdfMarksSample);
				String pdfBookMarkTemplate = TabbedPdfConstant.PDF_MARKS_TEMPLATE;

				FileWriter fileWriter = new FileWriter(localPdfMarksSample, true);
				String pdfBookMarkText = "";
				while (iterator.hasNext()) {
					documentIdInt = iterator.next();
					List<String> docDetails = documentPDFMap.get(documentIdInt);
					pdfBookMarkText = pdfBookMarkTemplate.replace("**", docDetails.get(0)); // document type i.e bookmark title
					pdfBookMarkText = pdfBookMarkText.replace("##", docDetails.get(1)); // bookmark page number
					fileWriter.write(pdfBookMarkText + "\n");
					documentPDFPaths.add(docDetails.get(2));
				}

				fileWriter.close();

				BatchInstanceThread batchInstanceThread = new BatchInstanceThread();

				String threadPoolLockFolderPath = batchSchemaService.getLocalFolderLocation() + File.separator
						+ batchInstanceIdentifier + File.separator + batchSchemaService.getThreadpoolLockFolderName();
				try {
					FileUtils.createThreadPoolLockFile(batchInstanceIdentifier, threadPoolLockFolderPath, pluginName);
				} catch (IOException ioe) {
					LOGGER.error("Error in creating threadpool lock file" + ioe.getMessage(), ioe);
					throw new DCMABusinessException(ioe.getMessage(), ioe);
				}

				List<TabbedPDFExecutor> tabbedPDFExecutors = new ArrayList<TabbedPDFExecutor>();

				String tabbedPDFName = batch.getBatchName() + "_" + batchInstanceIdentifier + ".pdf";
				String tabbedPDFLocalPath = batchSchemaService.getLocalFolderLocation() + File.separator + batchInstanceIdentifier
						+ File.separator + tabbedPDFName;

				tabbedPDFExecutors.add(new TabbedPDFExecutor(tabbedPDFName, sFolderToBeExported, documentPDFPaths, localPdfMarksSample
						.getAbsolutePath(), batchInstanceThread));

				try {
					LOGGER.info("Executing commands for creation of tabbed pdf using thread pool.");
					batchInstanceThread.execute();
					LOGGER.info("Tabebd pdf creation ends.");
				} catch (DCMAApplicationException dcmae) {
					LOGGER.error("Error in executing command for tabbed pdf using thread pool" + dcmae.getMessage(), dcmae);
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
				
				// copying file to ephesoft system folder
				LOGGER.info("Started copying multipage pdf file in ephesoft system folder.");
				File srcFile = new File(sFolderToBeExported + File.separator + tabbedPDFName);
				File destFile = new File(tabbedPDFLocalPath);
				FileUtils.copyFile(srcFile, destFile);
				LOGGER.info("Completed copying multipage pdf file in ephesoft system folder.");

				mergeBatchXmlDocs(batchSchemaService, batch, documentNames, documentPDFMap, tabbedPDFName, sFolderToBeExported);
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
	 * This API merges all the documents as per prioritylist into first document. Also, updates the multipage pdf file name to tabbed
	 * pdf file name.
	 * 
	 * @param batchSchemaService
	 * @param batch
	 * @param documentNames
	 * @param documentPDFMap
	 * @param tabbedPDFName
	 * @param exportFolderPath
	 */
	private void mergeBatchXmlDocs(BatchSchemaService batchSchemaService, Batch batch, Set<String> documentNames,
			LinkedHashMap<String, List<String>> documentPDFMap, String tabbedPDFName, String exportFolderPath) {
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
	 * @return
	 */
	private boolean isExportFolderAlreadyCreated(final String exportFolder) {
		LOGGER.info("Checking the export folder is already created or not");
		File exportFolderFile = new File(exportFolder);
		boolean isExportFolderCreated = false;
		if (!exportFolderFile.exists()) {
			isExportFolderCreated = exportFolderFile.mkdir();
		} else {
			isExportFolderCreated = true;
		}
		return isExportFolderCreated;
	}

	private LinkedHashMap<String, List<String>> createDocumentPDFMap(final Batch pasrsedXMLFile, String batchInstanceID)
			throws DCMAApplicationException {

		List<Document> xmlDocuments = pasrsedXMLFile.getDocuments().getDocument();
		LinkedHashMap<String, List<String>> documentPDFMap = new LinkedHashMap<String, List<String>>();

		int startPageNumber = 1;

		for (Document document : xmlDocuments) {
			List<String> detailsList = new LinkedList<String>();
			List<Page> listOfPages = document.getPages().getPage();

			String documentId = document.getIdentifier();
			LOGGER.info("Document documentid =" + documentId + " contains the following info:");
			int numberOfPages = listOfPages.size();
			String documentType = document.getType();

			detailsList.add(documentType);
			detailsList.add(String.valueOf(startPageNumber));

			String pdfFile = document.getMultiPagePdfFile();
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
			startPageNumber = startPageNumber + numberOfPages;
			documentPDFMap.put(documentId, detailsList);
		}

		return documentPDFMap;
	}

	private LinkedHashMap<String, List<String>> createBatchClassDocumentPDFMap(final Batch pasrsedXMLFile, String batchInstanceID,
			String propertyFile) throws Exception {
		String errorPDFPath = batchSchemaService.getAbsolutePath(pasrsedXMLFile.getBatchClassIdentifier(), batchSchemaService
				.getScriptConfigFolderName(), true);
		// File samplePDF = new File(errorPDFPath + File.separator + TabbedPdfConstant.SAMPLE_PDF_FILE_NAME);
		// if (!samplePDF.exists()) {
		// throw new DCMABusinessException("Sample Pdf file not provided.");
		// }
		// File localSamplePDF = new File(batchSchemaService.getLocalFolderLocation() + File.separator
		// + pasrsedXMLFile.getBatchInstanceIdentifier() + File.separator + samplePDF.getName());
		// FileUtils.copyFile(samplePDF, localSamplePDF);
		List<String> batchDocumentTypeNameList = new ArrayList<String>();
		String batchClassId = pasrsedXMLFile.getBatchClassIdentifier();
		BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);
		List<DocumentType> batchDocumentList = batchClass.getDocumentTypes();
		for (DocumentType docType : batchDocumentList) {
			if (!docType.isHidden()) {
				batchDocumentTypeNameList.add(docType.getName());
			}
		}
		List<String> sortedDocumentList = new ArrayList<String>();
		Map<String, String> propMap = fetchDocNameMapping(propertyFile);
		List<String> mapKeys = new ArrayList<String>(propMap.keySet());
		List<String> mapValues = new ArrayList<String>(propMap.values());
		TreeSet<String> sortedSet = new TreeSet<String>(mapValues);
		if (sortedSet.size() != propMap.size()) {
			LOGGER.error("Same priority is defined for more than one document type. Invalid scenario.");
			throw new DCMAApplicationException("Property file for documents not valid");
		} else {
			Object[] sortedArray = sortedSet.toArray();
			int size = sortedArray.length;
			for (int i = size - 1; (i > 0 || i == 0); i--) {
				String documentType = (String) mapKeys.get(mapValues.indexOf(sortedArray[i]));
				for (int documentIndex = 0; documentIndex < batchDocumentTypeNameList.size(); documentIndex++) {
					if (documentType.equals(batchDocumentTypeNameList.get(documentIndex))) {
						sortedDocumentList.add(batchDocumentTypeNameList.get(documentIndex));
					}
				}
			}
			List<Document> xmlDocuments = pasrsedXMLFile.getDocuments().getDocument();
			LinkedHashMap<String, List<String>> documentPDFMap = new LinkedHashMap<String, List<String>>();

			int startPageNumber = 1;
			int docIdentifier = 1;
			int batchDocumentIndex = 0;
			for (String document : sortedDocumentList) {
				List<String> detailsList = new LinkedList<String>();
				String documentId = DOCUMENT_IDENTIFIER + docIdentifier;
				int numberOfPages;
				String documentType;
				String pdfFile = null;
				if (batchDocumentIndex < xmlDocuments.size()) {
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

						documentType = document;
						numberOfPages = 1;
						pdfFile = TabbedPdfConstant.SAMPLE_PDF_FILE_NAME;
						detailsList.add(documentType);
						detailsList.add(String.valueOf(startPageNumber));
						if (pdfFile != null && !pdfFile.isEmpty()) {
							File fPDFFile = new File(errorPDFPath + File.separator + TabbedPdfConstant.SAMPLE_PDF_FILE_NAME);
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
					}
				} else {
					documentType = document;
					numberOfPages = 1;
					pdfFile = TabbedPdfConstant.SAMPLE_PDF_FILE_NAME;
					detailsList.add(documentType);
					detailsList.add(String.valueOf(startPageNumber));
					if (pdfFile != null && !pdfFile.isEmpty()) {
						File fPDFFile = new File(errorPDFPath + File.separator + TabbedPdfConstant.SAMPLE_PDF_FILE_NAME);
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
				}

			}
			return documentPDFMap;
		}
	}

	private void checkForUnknownDocument(Batch pasrsedXMLFile) {
		Documents documents = pasrsedXMLFile.getDocuments();
		if (documents != null) {
			List<Document> listOfDocuments = documents.getDocument();
			if (listOfDocuments == null) {
				return;
			}
			for (Document document : listOfDocuments) {

				if (document == null) {
					return;
				}
				if (document.getType().equalsIgnoreCase(EphesoftProperty.UNKNOWN.getProperty())) {
					Pages pages = document.getPages();
					if (pages == null) {
						return;
					}
					List<Page> listOfPages = pages.getPage();
					if (listOfPages == null) {
						return;
					}
					if (listOfPages.isEmpty()) {
						return;
					}
					throw new DCMABusinessException("Final xml document contains unknown documents.Cannot be exported.");
				}
			}

		}

	}

	private Map<String, String> fetchDocNameMapping(String propertyFilePath) throws DCMAApplicationException {
		final String filePath = propertyFilePath;
		HashMap<String, String> propertyMap = new HashMap<String, String>();
		FileInputStream fileInputStream = null;
		BufferedReader bufferReader = null;
		try {
			fileInputStream = new FileInputStream(filePath);
			bufferReader = new BufferedReader(new InputStreamReader(fileInputStream));
			String eachLine = bufferReader.readLine();
			while (eachLine != null) {
				if (eachLine.length() > 0) {
					final String[] keyValue = eachLine.split(MAPPING_SEPERATOR);
					if (keyValue != null && keyValue.length == 2) {
						propertyMap.put(keyValue[0], keyValue[1]);
					}
				}
				eachLine = bufferReader.readLine();
			}
		} catch (IOException e) {
			throw new DCMAApplicationException("Property file not provided.Cannot be exported.");
		} finally {
			try {
				if (bufferReader != null) {
					bufferReader.close();
				}
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (IOException e) {
				throw new DCMAApplicationException("Property file not provided.Cannot be exported.");
			}
		}
		return propertyMap;
	}
}
