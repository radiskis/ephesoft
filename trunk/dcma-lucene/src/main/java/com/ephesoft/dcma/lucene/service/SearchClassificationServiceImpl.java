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

package com.ephesoft.dcma.lucene.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.HocrPages;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.annotation.PostProcess;
import com.ephesoft.dcma.core.annotation.PreProcess;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.id.BatchClassID;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.lucene.LuceneEngine;
import com.ephesoft.dcma.lucene.LuceneProperties;
import com.ephesoft.dcma.util.BackUpFileService;

/**
 * This class is used for generating the confidence score for the learning done.
 * 
 * @author Ephesoft
 *
 */
public class SearchClassificationServiceImpl implements SearchClassificationService {

	/**
	 * An instance of Logger for proper logging in this class using slf4j. 
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchClassificationServiceImpl.class);

	/**
	 * An instance of {@link LuceneEngine}.
	 */
	@Autowired
	private transient LuceneEngine luceneEngine;

	/**
	 * Instance of {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	@PreProcess
	public void preProcess(final BatchInstanceID batchInstanceID, String pluginWorkflow) throws DCMAApplicationException {
		Assert.notNull(batchInstanceID);
		final String batchInstanceIdentifier = batchInstanceID.getID();
		BackUpFileService.backUpBatch(batchInstanceIdentifier, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));
	}

	@PostProcess
	public void postProcess(final BatchInstanceID batchInstanceID, String pluginWorkflow) {
		Assert.notNull(batchInstanceID);
		final String batchInstanceIdentifier = batchInstanceID.getID();
		BackUpFileService.backUpBatch(batchInstanceIdentifier, pluginWorkflow, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));
	}

	@Override
	public void generateConfidenceScore(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException {
		try {
			luceneEngine.generateConfidence(batchInstanceID.getID());
		} catch (Exception e) {
			LOGGER.error("Uncaught Exception in generateConfidence method " + e.getMessage(), e);
			throw new DCMAException(e.getMessage(), e);
		}
	}

	@Override
	public void generateConfidenceScoreAPI(final List<Document> xmlDocuments, final HocrPages hocrPages, final String workingDir,
			final Map<LuceneProperties, String> propertyMap, final String batchClassIdentifier) throws DCMAException {
		try {
			luceneEngine.generateConfidenceAPI(xmlDocuments, hocrPages, workingDir, propertyMap, batchClassIdentifier);
		} catch (Exception e) {
			LOGGER.error("Uncaught Exception in generateConfidence method " + e.getMessage(), e);
			throw new DCMAException(e.getMessage(), e);
		}
	}

	@Override
	public void learnSampleHOCR(final BatchClassID batchClassID, final boolean createIndex) throws DCMAException {
		try {
			luceneEngine.learnSampleHocrFiles(batchClassID.getID(), createIndex);
		} catch (Exception e) {
			LOGGER.error("Uncaught Exception in learnSampleHOCR method " + e.getMessage(), e);
			throw new DCMAException(e.getMessage(), e);
		}
	}

	@Override
	public void learnSampleHOCRForTesseract(final BatchClassID batchClassID, final boolean createIndex) throws DCMAException {
		try {
			luceneEngine.learnSampleHocrFilesForTesseract(batchClassID.getID(), createIndex);
		} catch (Exception e) {
			LOGGER.error("Uncaught Exception in learnSampleHOCR method " + e.getMessage(), e);
			throw new DCMAException(e.getMessage(), e);
		}
	}

	@Override
	public List<String> generateHOCRForKVExtractionTest(String imageFolder, String ocrEngineName, String batchClassIdentifer,
			File testImageFile, boolean isAdvancedKVExtraction) throws DCMAException {
		List<String> outputFilePaths = null;
		try {
			outputFilePaths = luceneEngine.generateHOCRForKVExtractionTest(imageFolder, ocrEngineName, batchClassIdentifer,
					testImageFile, isAdvancedKVExtraction);
			luceneEngine.cleanUpIntrmediatePngs(imageFolder);
		} catch (Exception e) {
			LOGGER.error("Uncaught Exception in generateHOCRForKVExtractionTest method " + e.getMessage(), e);
			throw new DCMAException(e.getMessage(), e);
		}

		return outputFilePaths;
	}

}
