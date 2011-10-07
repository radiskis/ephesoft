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

package com.ephesoft.dcma.tabbed.pdf;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.BatchStatus;
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
import com.ephesoft.dcma.tabbed.pdf.constant.TabbedPdfConstant;
import com.ephesoft.dcma.util.FileUtils;

@Component
public class TabbedPdfExporter implements ICommonConstants {

	private static final Logger LOGGER = LoggerFactory.getLogger(TabbedPdfExporter.class);

	private static final String TABBED_PDF_PLUGIN = "TABBED_PDF";

	/**
	 * Instance of BatchSchemaService.
	 **/
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Instance of PluginPropertiesService.
	 **/
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * ClassPath Resource to xsl file.
	 */
	private ClassPathResource xslResource;

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

	/**
	 * @return the xslResource
	 */
	public ClassPathResource getXslResource() {
		return xslResource;
	}

	public void setXslResource(ClassPathResource xslResource) {
		this.xslResource = xslResource;
	}

	public void createTabbedPDF(String batchInstanceIdentifier, BatchSchemaService batchSchemaService, String pluginName)
			throws DCMAApplicationException {
		String tabbedPDFSwitch = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, TABBED_PDF_PLUGIN,
				TabbedPdfProperties.TABBED_PDF_SWITCH);
		if (("ON").equalsIgnoreCase(tabbedPDFSwitch)) {
			String sFolderToBeExported = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, TABBED_PDF_PLUGIN,
					TabbedPdfProperties.TABBED_PDF_EXPORT_FOLDER);
			this.batchSchemaService = batchSchemaService;
			Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);
			File fFolderToBeExported = new File(sFolderToBeExported);

			if (!fFolderToBeExported.isDirectory()) {
				throw new DCMAApplicationException(fFolderToBeExported.toString() + " is not a Directory.");
			}
			checkForUnknownDocument(batch);

			LinkedHashMap<String, List<String>> documentPDFMap;
			try {
				documentPDFMap = createDocumentPDFMap(batch, batchInstanceIdentifier);
			} catch (Exception e) {
				updateXMLFileFailiure(batchSchemaService, batch);
				throw new DCMAApplicationException(e.getMessage(), e);
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
				tabbedPDFExecutors.add(new TabbedPDFExecutor(batch.getBatchName(), sFolderToBeExported, documentPDFPaths,
						localPdfMarksSample.getAbsolutePath(), batchInstanceThread, batchInstanceIdentifier));

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
			} catch (Exception e) {
				updateXMLFileFailiure(batchSchemaService, batch);
				LOGGER.error("Error in writing pdfMarks file" + e.getMessage(), e);
				throw new DCMAApplicationException(e.getMessage(), e);
			}
			updateXMLFileSucess(batchSchemaService, batch);
			LOGGER.info("Processing complete at " + new Date());
		} else {
			LOGGER.info("Skipping tabbed pdf plgugin. Switch set as OFF");
		}
	}

	private void updateXMLFileFailiure(BatchSchemaService batchSchemaService, Batch batch) throws DCMAApplicationException {
		batch.setBatchStatus(BatchStatus.ERROR);
		batchSchemaService.updateBatch(batch);
	}

	private void updateXMLFileSucess(BatchSchemaService batchSchemaService, Batch batch) throws DCMAApplicationException {
		batch.setBatchStatus(BatchStatus.FINISHED);
		batchSchemaService.updateBatch(batch);
	}

	private LinkedHashMap<String, List<String>> createDocumentPDFMap(final Batch pasrsedXMLFile, String batchInstanceID) {

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
					throw new DCMABusinessException("File does not exist. File Name=" + fPDFFile);
				}
			}
			startPageNumber = startPageNumber + numberOfPages;
			documentPDFMap.put(documentId, detailsList);
		}

		return documentPDFMap;
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

}
