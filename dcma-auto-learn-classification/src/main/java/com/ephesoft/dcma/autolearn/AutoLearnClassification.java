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

package com.ephesoft.dcma.autolearn;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.autolearn.constant.AutoLearnConstants;
import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Batch.Documents;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.lucene.LuceneEngine;
import com.ephesoft.dcma.util.FileUtils;

/**
 * This class is used to learn files automatically for each page of the batch class. Learned files will be used for search
 * classification.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.batch.service.BatchSchemaService
 * @see com.ephesoft.dcma.batch.service.PluginPropertiesService
 */
@Component
public class AutoLearnClassification {

	/**
	 * DEFAULT_AUTO_LEARN_FILE_SIZE double.
	 */
	private static final double DEFAULT_AUTO_LEARN_FILE_SIZE = 5.0;

	/**
	 * Instance of {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * Instance of {@link BatchSchemaService}.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Instance of {@link PluginPropertiesService}.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * Instance of {@link LuceneEngine}.
	 */
	@Autowired
	private LuceneEngine luceneEngine;

	/**
	 * Logger reference.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AutoLearnClassification.class);

	/**
	 * Method to learn files for a batch instance. Learned files will be used for Search Classification.
	 * 
	 * @param batchInstanceIdentifier String
	 * @throws DCMAException if error occurs
	 */
	public void autoLearnFiles(final String batchInstanceIdentifier) throws DCMAException {
		LOGGER.info("Entering method autoLearnFiles....");
		String systemFolderPath = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceIdentifier);
		LOGGER.debug("Batch instance identifier :: " + batchInstanceIdentifier);
		String autoLearnClassificationSwitch = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				AutoLearnConstants.AUTO_LEARN_CLASSIFICATION_PLUGIN,
				AutoLearnClassificationProperties.AUTO_LEARN_CLASSIFICATION_SWITCH);
		LOGGER.debug("Switch for Auto Learn Classification Plugin :: " + autoLearnClassificationSwitch);
		if (AutoLearnConstants.SWITCH_ON.equals(autoLearnClassificationSwitch)) {
			String batchInstanceFolderPath = systemFolderPath + File.separator + batchInstanceIdentifier;
			File batchInstanceFolder = new File(batchInstanceFolderPath);
			try {
				if (batchInstanceFolder.exists()) {
					processDocuments(batchInstanceIdentifier, batchInstanceFolderPath);
				} else {
					LOGGER.error("Learning cannot be done. Batch instance folder doesnot exist :: " + batchInstanceFolderPath);
				}
			} catch (Exception exception) {
				LOGGER.error("Error while performing auto learning for search classification for batch instance :: "
						+ batchInstanceIdentifier, exception);
				throw new DCMAException("Error while performing auto learning for search classification for batch instance :: "
						+ batchInstanceIdentifier, exception);
			}
		}
		LOGGER.info("Exiting method autoLearnFiles....");
	}

	private boolean processDocuments(final String batchInstanceIdentifier, String batchInstanceFolderPath)
			throws DCMAApplicationException, IOException {
		Batch batchInstance = batchSchemaService.getBatch(batchInstanceIdentifier);
		Documents documents = batchInstance.getDocuments();
		String maxFileSize = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				AutoLearnConstants.AUTO_LEARN_CLASSIFICATION_PLUGIN, AutoLearnClassificationProperties.MAX_AUTO_LEARN_FILE_SIZE);
		boolean learningRequired = false;
		if (documents != null) {
			String batchClassIdentifier = batchInstance.getBatchClassIdentifier();
			String searchFolderPath = batchSchemaService.getSearchClassSamplePath(batchClassIdentifier, true);
			String indexFolderPath = batchSchemaService.getSearchClassIndexFolder(batchClassIdentifier, true);
			List<Document> documentList = documents.getDocument();
			Set<File> learnFileList = new HashSet<File>();
			double maxFileSizeDouble = DEFAULT_AUTO_LEARN_FILE_SIZE;
			try {
				maxFileSizeDouble = Double.parseDouble(maxFileSize);
			} catch (NumberFormatException nfe) {
				LOGGER.error("Invalid value for max file size specified. Setting it to its default value 5 MB");
			}
			for (final Document document : documentList) {
				boolean isDocTypeChanged = document.isDocumentTypeChanged();
				LOGGER.debug("Is auto learning required :: " + isDocTypeChanged + " for document :: " + document.getIdentifier());
				if (isDocTypeChanged) {
					LOGGER.debug("Auto learning files for document :: " + document.getIdentifier());
					learningRequired = processPages(document, batchInstanceFolderPath, maxFileSizeDouble, searchFolderPath,
							learnFileList);
				}
			}
			if (learningRequired) {
				for (File learnFile : learnFileList) {
					LOGGER.debug("Creating indexes for learn file :: " + learnFile.getAbsolutePath());
					luceneEngine.learnFiles(learnFile.getAbsolutePath(), indexFolderPath, true);
				}
			}
		}
		return learningRequired;
	}

	private boolean processPages(final Document document, final String batchInstanceFolderPath, final double maxFileSize,
			final String searchFolderPath, final Set<File> learnFileList) throws DCMAApplicationException, IOException {
		boolean learningRequired = false;
		String docType = document.getType();
		if (docType != null && !docType.isEmpty()) {
			Pages pages = document.getPages();
			if (pages != null) {
				List<Page> docPageList = pages.getPage();
				String learnFilePath = searchFolderPath;
				int docPageSize = docPageList.size();
				File autoLearnFile = null;
				boolean isMiddlePage = false;
				for (int index = 0; index < docPageSize; index = index + 1) {
					Page pageType = docPageList.get(index);
					LOGGER.debug("Auto learning files for page :: " + pageType.getIdentifier());
					if (index == 0) {
						learnFilePath = searchFolderPath + File.separatorChar + docType + File.separatorChar + docType
								+ AutoLearnConstants.FIRST_PAGE;
						autoLearnFile = new File(learnFilePath + File.separatorChar + AutoLearnConstants.AUTOLEARN_FILE_NAME
								+ FileType.HTML.getExtensionWithDot());
					} else if (index == docPageSize - 1) {
						learnFilePath = searchFolderPath + File.separatorChar + docType + File.separatorChar + docType
								+ AutoLearnConstants.LAST_PAGE;
						autoLearnFile = new File(learnFilePath + File.separatorChar + AutoLearnConstants.AUTOLEARN_FILE_NAME
								+ FileType.HTML.getExtensionWithDot());
					} else {
						learnFilePath = searchFolderPath + File.separatorChar + docType + File.separatorChar + docType
								+ AutoLearnConstants.MIDDLE_PAGE;
						autoLearnFile = new File(learnFilePath + File.separatorChar + AutoLearnConstants.AUTOLEARN_FILE_NAME
								+ FileType.HTML.getExtensionWithDot());
						isMiddlePage = true;
					}
					String pageID = pageType.getIdentifier();
					if (autoLearnFile.exists()) {
						double fileSize = FileUtils.getSizeInMB(autoLearnFile);
						if (fileSize >= maxFileSize) {
							LOGGER.debug("Size of file :: " + autoLearnFile.getAbsolutePath() + " greater than max file size :: "
									+ maxFileSize + ". Cannot learn file for page id :: " + pageID);
							if (isMiddlePage) {
								// if is middle page and size increased than max size, skip pages and move to the last page.
								index = docPageSize - 2;
							}
							continue;
						}
					} else {
						autoLearnFile.getParentFile().mkdirs();
					}
					String hocrFileName = pageType.getHocrFileName();
					File hocrFile = new File(batchInstanceFolderPath + File.separator + hocrFileName);
					if (hocrFile.exists()) {
						learnFileList.add(autoLearnFile);
						copyHocrDataToLearnFile(hocrFile, autoLearnFile);
						learningRequired = true;
					}
				}
			}
		} else {
			LOGGER.error("Doc type null or empty for :: " + document.getIdentifier());
		}
		return learningRequired;
	}

	private void copyHocrDataToLearnFile(final File hocrFile, final File autoLearnFile) throws IOException {
		if (hocrFile != null && hocrFile.exists() && autoLearnFile != null && autoLearnFile.getParentFile().exists()) {
			FileUtils.appendFile(hocrFile, autoLearnFile);
		}
	}
}
