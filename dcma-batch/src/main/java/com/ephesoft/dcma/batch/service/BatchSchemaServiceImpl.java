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

package com.ephesoft.dcma.batch.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPluginConfiguration;
import com.ephesoft.dcma.batch.dao.xml.BatchSchemaDao;
import com.ephesoft.dcma.batch.dao.xml.HocrSchemaDao;
import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Field;
import com.ephesoft.dcma.batch.schema.HocrPages;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.DocField.AlternateValues;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.batch.schema.Page.PageLevelFields;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.EphesoftProperty;
import com.ephesoft.dcma.core.TesseractVersionProperty;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.UserType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.AbstractRunnable;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassCloudConfig;
import com.ephesoft.dcma.da.domain.BatchClassDynamicPluginConfig;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchClassModuleConfig;
import com.ephesoft.dcma.da.domain.BatchClassPlugin;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;
import com.ephesoft.dcma.da.domain.KVPageProcess;
import com.ephesoft.dcma.da.id.BatchClassID;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchClassCloudConfigService;
import com.ephesoft.dcma.da.service.BatchClassModuleService;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.BackUpFileService;
import com.ephesoft.dcma.util.CustomFileFilter;
import com.ephesoft.dcma.util.FileFormatException;
import com.ephesoft.dcma.util.FileNameFormatter;
import com.ephesoft.dcma.util.IUtilCommonConstants;
import com.ephesoft.dcma.util.OCREngineUtil;
import com.ephesoft.dcma.util.PDFUtil;
import com.ephesoft.dcma.util.TIFFUtil;
import com.ephesoft.dcma.util.XMLUtil;
import com.itextpdf.text.DocumentException;

/**
 * This is a service to read and write data required by Batch Schema. Api's are present to move, rearrange, delete, create, swap the
 * document and pages from one to each other. On the basis of batch instance id and base folder location will read the batch.xml file
 * and rearrange the attribute of the xml file.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.batch.service.BatchSchemaService
 * @see com.ephesoft.dcma.batch.dao.xml.BatchSchemaDao
 */
public class BatchSchemaServiceImpl implements BatchSchemaService {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchSchemaServiceImpl.class);

	/**
	 * Name of TESSERACT HOCR Plugin.
	 */
	private static final String TESSERACT_HOCR_PLUGIN = "TESSERACT_HOCR";

	/**
	 * Name of HOCR file name.
	 */
	public static final String HOCR_FILE_NAME = "_HOCR.xml";

	/**
	 * The USER_TYPE {@link String} is a constant for user type property key.
	 */
	private static final String USER_TYPE = "user_type";

	/**
	 * Reference of BatchSchemaDao.
	 */
	@Autowired
	private BatchSchemaDao batchSchemaDao;

	/**
	 * Reference of ScannerSchemaDao.
	 */
	@Autowired
	private HocrSchemaDao hocrSchemaDao;

	/**
	 * Reference of BatchClassService.
	 */
	@Autowired
	private BatchClassService batchClassService;

	/**
	 * The batchClassCloudService {@link BatchClassCloudConfigService} is used for using batch class cloud service.
	 */
	@Autowired
	private BatchClassCloudConfigService batchClassCloudService;

	/**
	 * Instance of BatchClassPluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchClassPluginPropertiesService")
	private PluginPropertiesService batchClassPluginPropertiesService;

	/**
	 * Instance of BatchClassModuleService.
	 */
	@Autowired
	private BatchClassModuleService batchClassModuleService;

	/**
	 * Instance of BatchInstanceService.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * An API to return the complete local folder location path.
	 * 
	 * @return String localFolderLocation
	 */
	@Override
	public boolean isZipSwitchOn() {
		boolean isZipSwitchOn = true;
		try {
			final ApplicationConfigProperties prop = ApplicationConfigProperties.getApplicationConfigProperties();
			isZipSwitchOn = Boolean.parseBoolean(prop.getProperty(ICommonConstants.ZIP_SWITCH));
		} catch (final IOException ioe) {
			LOGGER.error("Unable to read the zip switch value. Taking default value as false.Exception thrown is:" + ioe.getMessage(),
					ioe);
		}
		return isZipSwitchOn;
	}

	/**
	 * An API to return the complete local folder location path.
	 * 
	 * @return String localFolderLocation
	 */
	@Override
	public String getLocalFolderLocation() {
		return batchSchemaDao.getJAXB2Template().getLocalFolderLocation();
	}

	/**
	 * To set the local folder location.
	 * 
	 * @param localFolderLocation String
	 */
	@Override
	public void setLocalFolderLocation(final String localFolderLocation) {
		if (null != localFolderLocation && !localFolderLocation.isEmpty()) {
			batchSchemaDao.getJAXB2Template().setLocalFolderLocation(localFolderLocation);
			hocrSchemaDao.getJAXB2Template().setLocalFolderLocation(localFolderLocation);
		}
	}

	/**
	 * To get Base Sample FD Lock.
	 * 
	 * @return String
	 */
	@Override
	public String getBaseSampleFDLock() {
		return batchSchemaDao.getJAXB2Template().getBaseSampleFdLoc();
	}

	/**
	 * To get Script Folder Name.
	 * 
	 * @return String
	 */
	@Override
	public String getScriptFolderName() {
		return batchSchemaDao.getJAXB2Template().getScriptFolderName();
	}

	/**
	 * To get Cmis Plugin Mapping Folder Name.
	 * 
	 * @return String
	 */
	@Override
	public String getCmisPluginMappingFolderName() {
		return batchSchemaDao.getJAXB2Template().getCmisPluginMappingFolderName();
	}

	/**
	 * An API to return the sample path for search classification.
	 * 
	 * @param batchClassIdentifier String
	 * @param createDir boolean
	 * @return String searchClassSamplePath
	 */
	@Override
	public String getSearchClassSamplePath(final String batchClassIdentifier, final boolean createDir) {
		return getAbsolutePath(batchClassIdentifier, batchSchemaDao.getJAXB2Template().getSearchSampleName(), createDir);
	}

	/**
	 * An API to return the index folder for search classification.
	 * 
	 * @param batchClassIdentifier String
	 * @param createDir boolean
	 * @return String searchClassSamplePath
	 */
	@Override
	public String getSearchClassIndexFolder(final String batchClassIdentifier, final boolean createDir) {
		return getAbsolutePath(batchClassIdentifier, batchSchemaDao.getJAXB2Template().getSearchIndexFolderName(), createDir);
	}

	/**
	 * An API to return the base folder path for imagemagick.
	 * 
	 * @param batchClassIdentifier String
	 * @param createDir boolean
	 * @return String searchClassSamplePath
	 */
	@Override
	public String getImageMagickBaseFolderPath(final String batchClassIdentifier, final boolean createDir) {
		return getAbsolutePath(batchClassIdentifier, batchSchemaDao.getJAXB2Template().getImageMagickBaseFolderName(), createDir);
	}

	/**
	 * An API to return the Index folder for fuzzy db.
	 * 
	 * @param batchClassIdentifier String
	 * @param createDir boolean
	 * @return String searchClassSamplePath
	 */
	@Override
	public String getFuzzyDBIndexFolder(final String batchClassIdentifier, final boolean createDir) {
		return getAbsolutePath(batchClassIdentifier, batchSchemaDao.getJAXB2Template().getFuzzyDBIndexFolderName(), createDir);
	}

	/**
	 * An API to return the complete export folder location path.
	 * 
	 * @return String exportFolderLocation.
	 */
	@Override
	public String getExportFolderLocation() {
		return batchSchemaDao.getJAXB2Template().getExportFolderLocation();
	}

	/**
	 * An API to return the base http url path.
	 * 
	 * @return String base http url path.
	 */
	@Override
	public String getBaseHttpURL() {
		return batchSchemaDao.getJAXB2Template().getBaseHttpURL();
	}

	/**
	 * An API to get the absolute path of the directory. It will create the directory if it is not exits and createDir boolean is true
	 * otherwise not.
	 * 
	 * @param batchClassIdentifier String
	 * @param directoryName String
	 * @param createDir boolean
	 * @return absolute path of the directory.
	 */
	@Override
	public String getAbsolutePath(final String batchClassIdentifier, final String directoryName, final boolean createDir) {

		String absolutePath = null;
		if (null == batchClassIdentifier || null == directoryName || "".equals(batchClassIdentifier) || "".equals(directoryName)) {
			LOGGER.error("batchClassIdentifier/directoryName is null or empty.");
		} else {
			absolutePath = batchSchemaDao.getJAXB2Template().getBaseSampleFdLoc() + File.separator + batchClassIdentifier
					+ File.separator + directoryName;
			if (createDir) {
				try {
					// Create multiple directories
					final boolean success = new File(absolutePath).mkdirs();
					if (success) {
						LOGGER.info(BatchConstants.DIRECTORIES + absolutePath + BatchConstants.CREATED);
					} else {
						LOGGER.info(BatchConstants.DIRECTORIES + absolutePath + BatchConstants.NOT_CREATED);
					}
				} catch (final Exception e) {
					LOGGER.error(BatchConstants.DIRECTORIES + absolutePath + BatchConstants.NOT_CREATED);
					LOGGER.error(e.getMessage());
				}
			}
		}

		return absolutePath;
	}

	/**
	 * An API to create the Batch object.
	 * 
	 * @param batch Batch
	 */
	@Override
	public void createBatch(final Batch batch) {
		if (null == batch) {
			LOGGER.info(BatchConstants.BATCH_CLASS_NULL);
		} else {
			this.batchSchemaDao.create(batch, batch.getBatchInstanceIdentifier(), null, ICommonConstants.UNDERSCORE_BATCH_XML, false,
					batch.getBatchLocalPath());
		}
	}

	/**
	 * An API to create the hocrPages object.
	 * 
	 * @param batch {@link Batch}
	 * @param batchInstanceIdentifier {@link String}
	 * @param pageId {@link String}
	 */
	@Override
	public void createHocr(final HocrPages hocrPages, final String batchInstanceIdentifier, final String pageId) {
		if (null == hocrPages) {
			LOGGER.info(BatchConstants.HOCR_PAGES_NULL);
		} else {
			this.hocrSchemaDao.create(hocrPages, batchInstanceIdentifier, pageId, HOCR_FILE_NAME, true, batchInstanceService
					.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));
		}
	}

	/**
	 * An API to get the HocrPages object for an input of HOCR.XML.
	 * 
	 * @param batchInstanceIdentifier Serializable
	 * @param pageId {@link String}
	 * @return {@link HocrPages}
	 */
	@Override
	public HocrPages getHocrPages(final Serializable batchInstanceIdentifier, final String pageId) {
		HocrPages hocrPages = null;
		if (null == batchInstanceIdentifier) {
			LOGGER.info(BatchConstants.BATCH_INSTANCE_ID_NULL);
		} else {
			hocrPages = this.hocrSchemaDao.get(batchInstanceIdentifier, pageId, HOCR_FILE_NAME, batchInstanceService
					.getSystemFolderForBatchInstanceId(batchInstanceIdentifier.toString()));
		}
		return hocrPages;
	}

	/**
	 * API to update the batch object.
	 * 
	 * @param hocrPages {@link HocrPages}
	 * @param batchInstanceIdentifier Serializable
	 * @param pageId {@link String}
	 */
	public void update(final HocrPages hocrPages, final Serializable batchInstanceIdentifier, final String pageId) {
		if (null == batchInstanceIdentifier) {
			LOGGER.info(BatchConstants.BATCH_INSTANCE_ID_NULL);
		} else {
			this.hocrSchemaDao.update(hocrPages, batchInstanceIdentifier, HOCR_FILE_NAME, pageId, false, batchInstanceService
					.getSystemFolderForBatchInstanceId(batchInstanceIdentifier.toString()));
		}
	}

	/**
	 * An API to get the Batch object for an input of batchInstanceIdentifier.
	 * 
	 * @param batchInstanceIdentifier Serializable
	 * @return Batch
	 */
	@Override
	public Batch getBatch(final Serializable batchInstanceIdentifier) {
		Batch batch = null;
		if (null == batchInstanceIdentifier) {
			LOGGER.info(BatchConstants.BATCH_INSTANCE_ID_NULL);
		} else {
			batch = this.batchSchemaDao.get(batchInstanceIdentifier, batchInstanceService
					.getSystemFolderForBatchInstanceId(batchInstanceIdentifier.toString()));
		}
		return batch;
	}

	/**
	 * An API to update the Batch object.
	 * 
	 * @param batch Batch
	 */
	@Override
	public void updateBatch(final Batch batch) {
		if (null == batch) {
			LOGGER.info(BatchConstants.BATCH_CLASS_NULL);
		} else {
			this.batchSchemaDao.update(batch, batch.getBatchInstanceIdentifier(), false, batch.getBatchLocalPath());
		}
	}

	/**
	 * An API to update the Batch object.
	 * 
	 * @param batch Batch
	 * @param isFirstTimeUpdate
	 * 
	 */
	@Override
	public void updateBatch(final Batch batch, final boolean isFirstTimeUpdate) {
		if (null == batch) {
			LOGGER.info(BatchConstants.BATCH_CLASS_NULL);
		} else {
			this.batchSchemaDao.update(batch, batch.getBatchInstanceIdentifier(), isFirstTimeUpdate, batch.getBatchLocalPath());
		}
	}

	/**
	 * An API to store all files to base folder location.
	 * 
	 * @param identifier Serializable
	 * @param files File[]
	 */
	public void storeFiles(final Serializable identifier, final File[] files) {

		String errMsg = null;

		if (null == identifier || null == files) {
			errMsg = "Input parameters id/files are null.";
			LOGGER.error(errMsg);
			throw new DCMABusinessException(errMsg);
		}

		boolean preserveFileDate = false;
		String newPath = null;
		final String localFolderLocation = batchInstanceService.getSystemFolderForBatchInstanceId(identifier.toString());

		for (final File srcFile : files) {

			final String path = srcFile.getPath();

			newPath = localFolderLocation + File.separator + identifier + BatchConstants.UNDER_SCORE + path;
			// The target file name to which the source file will be copied.
			final File destFile = new File(newPath);

			try {
				FileUtils.copyFile(srcFile, destFile, preserveFileDate);
				errMsg = "Successfully copy of the file for the batch Instance Id : " + identifier;
				LOGGER.info(errMsg);
			} catch (final IOException e) {
				errMsg = "Unable to copy the file for the batch Instance Id : " + identifier;
				LOGGER.error(errMsg);
				LOGGER.error(e.getMessage());
			}
		}
	}

	/**
	 * An API to create backup of all files to input folder location.
	 * 
	 * @param identifier Serializable
	 * @param files File[]
	 */
	public void backUpFiles(final Serializable identifier, final File[] files) {

		String errMsg = null;

		if (null == identifier || null == files) {
			errMsg = "Input parameters id/files are null.";
			LOGGER.error(errMsg);
			throw new DCMABusinessException(errMsg);
		}

		boolean preserveFileDate = false;
		String newAbsPath = null;

		for (final File srcFile : files) {

			final String absPath = srcFile.getAbsolutePath();
			final String path = srcFile.getPath();

			final String[] arr = absPath.split(path);
			if (null != arr && arr.length >= BatchConstants.ZERO) {
				newAbsPath = arr[BatchConstants.ZERO] + BatchConstants.UNDER_SCORE + identifier + "_backUp_" + path;
			}

			if (null == newAbsPath) {
				continue;
			}

			// The target file name to which the source file will be copied.
			final File destFile = new File(newAbsPath);

			try {
				FileUtils.copyFile(srcFile, destFile, preserveFileDate);
				errMsg = "Successfully copy of the file for the batch Instance Id : " + identifier;
				LOGGER.info(errMsg);
			} catch (final IOException e) {
				errMsg = "Unable to copy the file for the batch Instance Id : " + identifier;
				LOGGER.error(errMsg);
				LOGGER.error(e.getMessage());
			}
		}
	}

	/**
	 * An API to get the URL object for the specified file name.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param fileName String
	 * @return URL object.
	 */
	@Override
	public URL getURL(final String batchInstanceIdentifier, final String fileName) {

		String errMsg = null;

		if (null == fileName || "".equals(fileName)) {
			errMsg = "File name is null or empty.";
			LOGGER.error(errMsg);
			throw new DCMABusinessException(errMsg);
		}

		URL url = null;
		final String pathName = getBatchFolderURL(batchInstanceIdentifier) + BatchConstants.SLASH + batchInstanceIdentifier
				+ BatchConstants.SLASH + fileName;

		try {
			url = new URL(pathName);
		} catch (final MalformedURLException e) {
			errMsg = "File name does not exists at the specified path location.";
			LOGGER.error(errMsg);
			throw new DCMABusinessException(errMsg, e);
		}

		return url;
	}

	/**
	 * An API to get the File object for the specified file name.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param fileName String
	 * @return File object.
	 */
	@Override
	public File getFile(final String batchInstanceIdentifier, final String fileName) {

		String errMsg = null;

		if (null == fileName || "".equals(fileName)) {
			errMsg = "File name is null or empty.";
			LOGGER.error(errMsg);
			throw new DCMABusinessException(errMsg);
		}

		File file = null;
		final String pathName = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceIdentifier) + File.separator
				+ batchInstanceIdentifier + File.separator + fileName;
		file = new File(pathName);
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
		} catch (final FileNotFoundException e) {
			errMsg = "File name does not exists at the specified path location. file : " + file.getName() + " , filepath : "
					+ file.getAbsolutePath();
			LOGGER.error(errMsg);
			throw new DCMABusinessException(errMsg + e.getMessage(), e);
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (final IOException e) {
					LOGGER.error("could not cloose the fileInputStream . file + " + file.getAbsolutePath() + e.getMessage(), e);
				}
			}
		}

		return file;
	}

	/**
	 * An API to get the input stream object for the specified file name.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param fileName String
	 * @return InputStream object.
	 */
	@Override
	public InputStream getInputStream(final String batchInstanceIdentifier, final String fileName) {

		String errMsg = null;

		InputStream inputStream = null;
		final String pathName = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceIdentifier) + File.separator
				+ batchInstanceIdentifier + File.separator + fileName;

		// Open the file that is the first
		// command line parameter
		try {
			inputStream = new FileInputStream(pathName);
		} catch (final FileNotFoundException e) {
			errMsg = e.getMessage();
			LOGGER.error(errMsg);
			throw new DCMABusinessException(errMsg, e);
		} catch (final Exception e) {
			errMsg = e.getMessage();
			LOGGER.error(errMsg);
			throw new DCMABusinessException(errMsg, e);
		}

		return inputStream;
	}

	/**
	 * An API to move the page of one document to to another document. Position of the page in new document will depend on the to page
	 * ID. If the isAfterToPageID boolean is true then from page ID is added after the to page ID other wise before the to page ID.
	 * After the movement API will delete the from page ID from the from doc ID.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param fromDocID String
	 * @param fromPageID String
	 * @param toDocID String
	 * @param toPageID String from page ID position relative to this page ID.
	 * @param isAfterToPageID boolean true means add after the to page ID. false means add before the to page ID.
	 * @throws DCMAApplicationException If not able to find fromDocID, fromPageID or toDocID. If not able to find fromPageID within
	 *             fromDocID.
	 */
	@Override
	public void movePageOfDocument(final String batchInstanceIdentifier, final String fromDocID, final String fromPageID,
			final String toDocID, final String toPageID, final boolean isAfterToPageID) throws DCMAApplicationException {

		String errMsg = null;

		if (null == batchInstanceIdentifier || null == fromDocID || null == fromPageID || null == toDocID || null == toPageID) {
			errMsg = "Input parameters batchInstanceIdentifier/fromDocID/fromPageID/toDocID/toPageID are null.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		final Batch batch = batchSchemaDao.get(batchInstanceIdentifier, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));

		final List<Document> docTypesList = batch.getDocuments().getDocument();

		checkDocTypeList(docTypesList);

		final int fromDocIndex = getDocumentTypeIndex(docTypesList, fromDocID);

		final int fromPageIndex = getPageTypeIndex(docTypesList, fromDocIndex, fromPageID);

		final Document fromDocType = docTypesList.get(fromDocIndex);
		final Pages fromPages = fromDocType.getPages();
		final List<Page> fromPageTypeList = fromPages.getPage();
		final Page fromPgType = fromPageTypeList.get(fromPageIndex);

		final int toDocIndex = getDocumentTypeIndex(docTypesList, toDocID);
		final int toPageIndex = getPageTypeIndex(docTypesList, toDocIndex, toPageID);
		int newPageIndex = -BatchConstants.ONE;
		if (isAfterToPageID) {
			newPageIndex = toPageIndex + BatchConstants.ONE;
		} else {
			newPageIndex = toPageIndex;
		}
		final Document toDocType = docTypesList.get(toDocIndex);
		final Pages toPages = toDocType.getPages();
		final List<Page> toPageTypeList = toPages.getPage();

		if (newPageIndex < BatchConstants.ZERO) {
			errMsg = "newPageIndex is not valid for toDocID : " + toDocID + " and toPageID" + toPageID;
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		// remove the page from document type.
		fromPageTypeList.remove(fromPageIndex);

		if (newPageIndex > toPageTypeList.size()) {
			newPageIndex = toPageTypeList.size();
		}

		toPageTypeList.add(newPageIndex, fromPgType);

		// Add a check for single page document. If a document have single page
		// and we are moving that page means delete the whole document.

		if (fromPageTypeList.isEmpty()) {
			docTypesList.remove(fromDocIndex);
		}

		batchSchemaDao.update(batch, batchInstanceIdentifier);

	}

	/**
	 * An API to remove the page of document. This will remove all the information available to page. This will delete all the files
	 * present at page level fields. If a document have single page and we are deleting that page means delete the whole document.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param docID String
	 * @param removePageID String
	 * @throws DCMAApplicationException If not able to find docID, removePageID. If not able to find removePageID within docID.
	 */
	@Override
	public void removePageOfDocument(final String batchInstanceIdentifier, final String docID, final String removePageID)
			throws DCMAApplicationException {
		LOGGER.info("inside removePageOfDocument method.");
		String errMsg = null;

		if (null == batchInstanceIdentifier || null == docID || null == removePageID) {
			errMsg = "Input parameters batchInstanceIdentifier/docID/removePageID are null.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		final Batch batch = batchSchemaDao.get(batchInstanceIdentifier, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));

		final DocPageCarrier docPageCarrier = getDocPageCarrier(batch, docID, removePageID);

		final int pageIndex = docPageCarrier.getPageIndex();
		final int docIndex = docPageCarrier.getDocIndex();
		final List<Document> docTypesList = docPageCarrier.getDocTypesList();

		final List<Page> pageTypeList = docTypesList.get(docIndex).getPages().getPage();

		// Remove the page from the document.
		pageTypeList.remove(pageIndex);

		// Add a check for single page document. If a document have single page
		// and we are deleting that page means delete the whole document.

		if (pageTypeList.isEmpty()) {
			docTypesList.remove(docIndex);
		}
		LOGGER.info("updating batch xml.");
		batchSchemaDao.update(batch, batchInstanceIdentifier);
		LOGGER.info("Exiting method removePageOfDocument.");
	}

	/**
	 * An API to create duplicate page of document. This will create duplicate for all the information available to page. This will
	 * create duplicate for all the files present at page level fields.
	 * 
	 * @param batchInstanceId String
	 * @param docID String
	 * @param duplicatePageID String
	 * @throws DCMAApplicationException If not able to find docID, duplicatePageID. If not able to find removePageID within
	 *             duplicatePageID.
	 */
	@Override
	public void duplicatePageOfDocument(final String batchInstanceId, final String docID, final String duplicatePageID)
			throws DCMAApplicationException {

		String errMsg = null;

		if (null == batchInstanceId || null == docID || null == duplicatePageID) {
			errMsg = "Input parameters batchInstanceIdentifier/docID/duplicatePageID are null.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		final Batch batch = batchSchemaDao.get(batchInstanceId, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceId));

		final DocPageCarrier docPageCarrier = getDocPageCarrier(batch, docID, duplicatePageID);

		final String localFolLoc = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceId);
		final String batchInstanceFolderPath = localFolLoc + File.separator + batchInstanceId;

		final int pageIndex = docPageCarrier.getPageIndex();
		LOGGER.info("pageIndex : " + pageIndex);
		final int docIndex = docPageCarrier.getDocIndex();
		final List<Document> docTypesList = docPageCarrier.getDocTypesList();

		checkDocTypeList(docTypesList);

		final Page duplicatePageType = docTypesList.get(docIndex).getPages().getPage().get(pageIndex);

		final String newPageTypeID = getNewPageTypeID(batchInstanceFolderPath);

		final Page duplicateCopyPage = duplicatePageFiles(duplicatePageType, batchInstanceId, newPageTypeID, batchInstanceFolderPath);

		// Add the duplicate page type to the end of the document.
		docTypesList.get(docIndex).getPages().getPage().add(duplicateCopyPage);

		batchSchemaDao.update(batch, batchInstanceId);

	}

	/**
	 * An API to re order pages of document. This will re order the pages for the input document id.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param docID String
	 * @param reOrderOfPageIDs String
	 * @throws DCMAApplicationException If not able to find docID, reOrderOfPageIDs. If not able to find all reOrderOfPageIDs within
	 *             duplicatePageID.
	 */
	@Override
	public void reOrderPagesOfDocument(final String batchInstanceIdentifier, final String docID, final List<String> reOrderOfPageIDs)
			throws DCMAApplicationException {

		String errMsg = null;

		if (null == batchInstanceIdentifier || null == docID || null == reOrderOfPageIDs) {
			errMsg = "Input parameters batchInstanceIdentifier/docID/reOrderOfPageIDs are null.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		final Batch batch = batchSchemaDao.get(batchInstanceIdentifier, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));

		final List<Document> docTypesList = validateParams(batch);

		final int docIndex = getDocumentTypeIndex(docTypesList, docID);
		LOGGER.info("Document type index : " + docIndex);

		final Document docType = docTypesList.get(docIndex);
		final Pages pages = docType.getPages();
		if (null == pages) {
			errMsg = "There are zero pages for document ID : " + docID;
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		final List<Page> pageTypeList = pages.getPage();

		if (reOrderOfPageIDs.size() != pageTypeList.size()) {
			errMsg = "ReOrderOfPageID's list are not valid for document ID : " + docID;
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		final Pages reOrdPages = new Pages();
		final List<Page> reOrderPageTypeList = reOrdPages.getPage();

		for (final String reOrderPgID : reOrderOfPageIDs) {
			for (final Page pgLt : pageTypeList) {
				final String pgID = pgLt.getIdentifier();
				if (null != pgID && null != reOrderPgID && pgID.equals(reOrderPgID)) {
					reOrderPageTypeList.add(pgLt);
					break;
				}
			}
		}

		docType.setPages(reOrdPages);

		batchSchemaDao.update(batch, batchInstanceIdentifier);
	}

	/**
	 * An API to swap pages of document. This will swap the pages for the input document id.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param docID String
	 * @param swapPageIDOne String
	 * @param swapPageIDTwo String
	 * @throws DCMAApplicationException If not able to find docID, swapPageIDOne or swapPageIDTwo. If not able to find all
	 *             swapPageIDOne and swapPageIDTwo within docID.
	 */
	@Override
	public void swapPageOfDocument(final String batchInstanceIdentifier, final String docID, final String swapPageIDOne,
			final String swapPageIDTwo) throws DCMAApplicationException {

		String errMsg = null;

		if (null == batchInstanceIdentifier || null == docID || null == swapPageIDOne || null == swapPageIDTwo) {
			errMsg = "Input parameters batchInstanceIdentifier/docID/pageID are null.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		final Batch batch = batchSchemaDao.get(batchInstanceIdentifier, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));

		final List<Document> docTypesList = batch.getDocuments().getDocument();

		checkDocTypeList(docTypesList);

		final int docIndex = getDocumentTypeIndex(docTypesList, docID);

		final int swapPageIndexOne = getPageTypeIndex(docTypesList, docIndex, swapPageIDOne);

		final int swapPageIndexTwo = getPageTypeIndex(docTypesList, docIndex, swapPageIDTwo);

		final Document docType = docTypesList.get(docIndex);
		final Pages pages = docType.getPages();
		final List<Page> pageTypeList = pages.getPage();
		final Page swapPageTypeOne = pageTypeList.get(swapPageIndexOne);
		final Page swapPageTypeTwo = pageTypeList.get(swapPageIndexTwo);

		pageTypeList.set(swapPageIndexOne, swapPageTypeTwo);
		pageTypeList.set(swapPageIndexTwo, swapPageTypeOne);

		batchSchemaDao.update(batch, batchInstanceIdentifier);
	}

	/**
	 * An API to swap one page of one document to second page of other document.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param swapDocIDOne String
	 * @param swapPageIDOne String
	 * @param swapDocIDTwo String
	 * @param swapPageIDTwo String
	 * @throws DCMAApplicationException If not able to find swapDocIDOne, swapPageIDOne, swapDocIDTwo or swapPageIDTwo. If not able to
	 *             find all swapPageIDOne and swapPageIDTwo within swapDocIDOne and swapDocIDTwo respectively.
	 */
	@Override
	public void swapPageOfDocuments(final String batchInstanceIdentifier, final String swapDocIDOne, final String swapPageIDOne,
			final String swapDocIDTwo, final String swapPageIDTwo) throws DCMAApplicationException {

		String errMsg = null;

		if (null == batchInstanceIdentifier || null == swapDocIDOne || null == swapPageIDOne || null == swapDocIDTwo
				|| null == swapPageIDTwo) {
			errMsg = "Input parameters batchInstanceIdentifier/swapDocIDOne/swapPageIDOne/" + "swapDocIDTwo/swapPageIDTwo are null.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		final Batch batch = batchSchemaDao.get(batchInstanceIdentifier, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));

		final List<Document> docTypesList = batch.getDocuments().getDocument();

		checkDocTypeList(docTypesList);

		final int swapDocIndexOne = getDocumentTypeIndex(docTypesList, swapDocIDOne);

		final int swapPageIndexOne = getPageTypeIndex(docTypesList, swapDocIndexOne, swapPageIDOne);

		final int swapDocIndexTwo = getDocumentTypeIndex(docTypesList, swapDocIDTwo);

		final int swapPageIndexTwo = getPageTypeIndex(docTypesList, swapDocIndexTwo, swapPageIDTwo);

		final Document docTypeOne = docTypesList.get(swapDocIndexOne);
		final Pages pagesOne = docTypeOne.getPages();
		final List<Page> pageTypeListOne = pagesOne.getPage();
		final Page swapPageTypeOne = pageTypeListOne.get(swapPageIndexOne);

		final Document docTypeTwo = docTypesList.get(swapDocIndexTwo);
		final Pages pagesTwo = docTypeTwo.getPages();
		final List<Page> pageTypeListTwo = pagesTwo.getPage();
		final Page swapPageTypeTwo = pageTypeListTwo.get(swapPageIndexTwo);

		pageTypeListOne.set(swapPageIndexOne, swapPageTypeTwo);
		pageTypeListTwo.set(swapPageIndexTwo, swapPageTypeOne);

		batchSchemaDao.update(batch, batchInstanceIdentifier);
	}

	/**
	 * An API to merge two different documents to a single document. This will merge all the pages of second document to first
	 * document.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param docIDOne String
	 * @param mergeDocID String
	 * @throws DCMAApplicationException If any of the input parameter is null.
	 */
	@Override
	public Batch mergeDocuments(final String batchInstanceIdentifier, final String docIDOne, final String mergeDocID)
			throws DCMAApplicationException {

		String errMsg = null;

		if (null == batchInstanceIdentifier || null == docIDOne || null == mergeDocID) {
			errMsg = "Input parameters batchInstanceIdentifier/docIDOne/mergeDocID" + " are null.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		final Batch batch = batchSchemaDao.get(batchInstanceIdentifier, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));

		final List<Document> docTypesList = batch.getDocuments().getDocument();

		checkDocTypeList(docTypesList);

		final int mergeDocIndexOne = getDocumentTypeIndex(docTypesList, docIDOne);

		final int mergeDocIndexTwo = getDocumentTypeIndex(docTypesList, mergeDocID);

		final Document mergeDocTypeOne = docTypesList.get(mergeDocIndexOne);
		final Pages mergePagesOne = mergeDocTypeOne.getPages();
		final List<Page> mergePageTypeListOne = mergePagesOne.getPage();

		final Document mergeDocTypeTwo = docTypesList.get(mergeDocIndexTwo);
		final Pages mergePagesTwo = mergeDocTypeTwo.getPages();
		final List<Page> pageTypeListTwo = mergePagesTwo.getPage();

		mergePageTypeListOne.addAll(pageTypeListTwo);
		docTypesList.remove(mergeDocTypeTwo);

		batchSchemaDao.update(batch, batchInstanceIdentifier);
		return batchSchemaDao.get(batchInstanceIdentifier, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));
	}

	/**
	 * An API to split the document for given input page id. This will create a new document starting from input page id to the last
	 * page id.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param docID String
	 * @param pageID String
	 * @throws DCMAApplicationException If any one of the parameter is null or pageID is not present in docID.
	 */
	@Override
	public void splitDocument(final String batchInstanceIdentifier, final String docID, final String pageID)
			throws DCMAApplicationException {
		validatingArgumentsInfo(batchInstanceIdentifier, docID, pageID);

		final Batch batch = batchSchemaDao.get(batchInstanceIdentifier);
		final List<Document> docTypesList = batch.getDocuments().getDocument();

		checkDocTypeList(docTypesList);

		final int docIndex = getDocumentTypeIndex(docTypesList, docID);
		final int pageIndex = getPageTypeIndex(docTypesList, docIndex, pageID);
		final Document docType = docTypesList.get(docIndex);
		final Pages pages = docType.getPages();
		final List<Page> pageTypeList = pages.getPage();

		// create new document.
		final String newDocID = getNewDocumentTypeID(docTypesList);
		final Document newDocType = createNewDocument(docType, newDocID);

		final Pages newPages = new Pages();
		final List<Page> listOfNewPages = newPages.getPage();
		final List<String> listOfNewPageIdentifiers = new ArrayList<String>();
		// create the new document for split pages.
		for (Page pgType : pageTypeList) {
			listOfNewPages.add(pgType);
			listOfNewPageIdentifiers.add(pgType.getIdentifier());
		}

		newDocType.setPages(newPages);

		// remove the pages from the older document.
		for (int index = pageTypeList.size() - 1; index >= pageIndex; index--) {
			pageTypeList.remove(index);
		}

		final DocumentLevelFields documentLevelFields = docType.getDocumentLevelFields();
		if (documentLevelFields != null) {
			final List<DocField> docFields = documentLevelFields.getDocumentLevelField();
			final DocumentLevelFields documentLevelFieldsForNewDoc = new DocumentLevelFields();
			final List<DocField> docFieldsForNewDoc = documentLevelFieldsForNewDoc.getDocumentLevelField();
			setDocLevelFieldsInfo(pageTypeList, listOfNewPages, listOfNewPageIdentifiers, docFields, docFieldsForNewDoc);
			newDocType.setDocumentLevelFields(documentLevelFieldsForNewDoc);
		} else {
			newDocType.setDocumentLevelFields(null);
		}
		docTypesList.add(docIndex + 1, newDocType);
		batchSchemaDao.update(batch, batchInstanceIdentifier);
	}

	private void setDocLevelFieldsInfo(final List<Page> pageTypeList, final List<Page> listOfNewPages,
			final List<String> listOfNewPageIdentifiers, final List<DocField> docFields, final List<DocField> docFieldsForNewDoc) {
		if (docFields != null) {
			for (final DocField docField : docFields) {
				final DocField docFieldForNewDoc = new DocField();
				docFieldForNewDoc.setConfidence(docField.getConfidence());
				docFieldForNewDoc.setFieldOrderNumber(docField.getFieldOrderNumber());
				docFieldForNewDoc.setFieldValueOptionList(docField.getFieldValueOptionList());
				docFieldForNewDoc.setName(docField.getName());
				docFieldForNewDoc.setType(docField.getType());
				docFieldForNewDoc.setValue(docField.getValue());
				if (listOfNewPageIdentifiers.contains(docField.getPage())) {
					docFieldForNewDoc.setCoordinatesList(docField.getCoordinatesList());
					docFieldForNewDoc.setOverlayedImageFileName(docField.getOverlayedImageFileName());
					docFieldForNewDoc.setPage(docField.getPage());
					docField.setPage(pageTypeList.get(BatchConstants.ZERO).getIdentifier());
					docField.setCoordinatesList(null);
					docField.setOverlayedImageFileName(null);
				} else {
					docFieldForNewDoc.setCoordinatesList(null);
					docFieldForNewDoc.setOverlayedImageFileName(null);
					if (docField.getPage() != null && !docField.getPage().isEmpty()) {
						docFieldForNewDoc.setPage(listOfNewPages.get(BatchConstants.ZERO).getIdentifier());
					} else {
						docFieldForNewDoc.setPage(docField.getPage());
					}
				}
				final AlternateValues alternateValuesForNewDoc = new AlternateValues();
				final List<Field> fieldsForNewDoc = alternateValuesForNewDoc.getAlternateValue();
				final AlternateValues alternateValues = docField.getAlternateValues();
				if (alternateValues != null) {
					final List<Field> alternateValueFields = alternateValues.getAlternateValue();
					if (alternateValueFields != null) {
						for (final Field field : alternateValueFields) {
							final Field fieldForNewDoc = new Field();
							fieldForNewDoc.setConfidence(field.getConfidence());
							fieldForNewDoc.setFieldOrderNumber(field.getFieldOrderNumber());
							fieldForNewDoc.setFieldValueOptionList(field.getFieldValueOptionList());
							fieldForNewDoc.setName(field.getName());
							fieldForNewDoc.setType(field.getType());
							fieldForNewDoc.setValue(field.getValue());
							if (listOfNewPageIdentifiers.contains(field.getPage())) {
								fieldForNewDoc.setCoordinatesList(field.getCoordinatesList());
								fieldForNewDoc.setOverlayedImageFileName(field.getOverlayedImageFileName());
								fieldForNewDoc.setPage(field.getPage());
								field.setCoordinatesList(null);
								field.setOverlayedImageFileName(null);
								field.setPage(pageTypeList.get(0).getIdentifier());
							} else {
								fieldForNewDoc.setCoordinatesList(null);
								fieldForNewDoc.setOverlayedImageFileName(null);
								if (field.getPage() != null && !field.getPage().isEmpty()) {
									fieldForNewDoc.setPage(listOfNewPages.get(BatchConstants.ZERO).getIdentifier());
								} else {
									fieldForNewDoc.setPage(field.getPage());
								}
							}
							fieldsForNewDoc.add(fieldForNewDoc);
						}
					}
				}
				docFieldForNewDoc.setAlternateValues(alternateValuesForNewDoc);
				docFieldsForNewDoc.add(docFieldForNewDoc);
			}
		}
	}

	private void validatingArgumentsInfo(final String batchInstanceIdentifier, final String docID, final String pageID)
			throws DCMAApplicationException {
		String errMsg = null;
		if (null == batchInstanceIdentifier || null == docID || null == pageID) {
			errMsg = "Input parameters batchInstanceIdentifier/docID/pageID are null.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}
	}

	private void checkDocTypeList(final List<Document> docTypesList) throws DCMAApplicationException {
		String errMsg;
		if (null == docTypesList) {
			errMsg = BatchConstants.DOC_TYPE_LIST_NULL;
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}
	}

	private Document createNewDocument(final Document docType, final String newDocID) {
		final Document newDocType = new Document();
		newDocType.setIdentifier(EphesoftProperty.DOCUMENT.getProperty() + newDocID);
		newDocType.setType(docType.getType());
		newDocType.setConfidence(docType.getConfidence());
		newDocType.setMultiPagePdfFile(docType.getMultiPagePdfFile());
		newDocType.setMultiPageTiffFile(docType.getMultiPageTiffFile());
		newDocType.setValid(docType.isValid());
		newDocType.setReviewed(docType.isReviewed());
		newDocType.setErrorMessage(BatchConstants.EMPTY);
		newDocType.setDocumentDisplayInfo(BatchConstants.EMPTY);
		return newDocType;
	}

	/**
	 * An API to update the document type name and document level fields for the input doc type ID.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param docID String
	 * @param docTypeName String
	 * @param documentLevelFields DocumentLevelFields
	 * @throws DCMAApplicationException If any one of the parameter is null or docID is not present in batch.
	 */
	@Override
	public void updateDocTypeName(final String batchInstanceIdentifier, final String docID, final String docTypeName,
			final DocumentLevelFields documentLevelFields) throws DCMAApplicationException {

		String errMsg = null;

		if (null == batchInstanceIdentifier || null == docID || null == docTypeName || null == documentLevelFields) {
			errMsg = "Input parameters batchInstanceIdentifier/docID/docTypeName/" + "documentLevelFields are null.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		final Batch batch = batchSchemaDao.get(batchInstanceIdentifier, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));

		final List<Document> docTypesList = batch.getDocuments().getDocument();

		checkDocTypeList(docTypesList);

		final int docIndex = getDocumentTypeIndex(docTypesList, docID);

		final Document docType = docTypesList.get(docIndex);
		docType.setType(docTypeName);
		docType.setDocumentLevelFields(documentLevelFields);

		batchSchemaDao.update(batch, batchInstanceIdentifier);
	}

	/**
	 * @param batch Batch
	 * @param docID String
	 * @param reOrderOfPageIDs List<String>
	 * @return List<DocumentType>
	 * @throws DCMAApplicationException If any one of the parameter is null or empty.
	 */
	private List<Document> validateParams(final Batch batch) throws DCMAApplicationException {
		final List<Document> docTypesList = batch.getDocuments().getDocument();
		checkDocTypeList(docTypesList);
		return docTypesList;
	}

	/**
	 * @param batch Batch
	 * @param docID String
	 * @param pageID String
	 * @param xmlUtil XMLUtil
	 * @return DocPageCarrier
	 * @throws DCMAApplicationException If any one of the parameter is null or empty.
	 */
	private DocPageCarrier getDocPageCarrier(final Batch batch, final String docID, final String pageID)
			throws DCMAApplicationException {
		final List<Document> docTypesList = batch.getDocuments().getDocument();
		checkDocTypeList(docTypesList);
		final int docIndex = getDocumentTypeIndex(docTypesList, docID);

		final int pageIndex = getPageTypeIndex(docTypesList, docIndex, pageID);

		final DocPageCarrier docPageCarrier = new DocPageCarrier();
		docPageCarrier.setDocIndex(docIndex);
		docPageCarrier.setPageIndex(pageIndex);
		docPageCarrier.setDocTypesList(docTypesList);

		return docPageCarrier;
	}

	/**
	 * @param docTypesList List<DocumentType>
	 * @param docIndex int
	 * @param pageID String
	 * @return int page type index.
	 * @throws DCMAApplicationException If any one of the parameter is null or empty.
	 */
	private int getPageTypeIndex(final List<Document> docTypesList, final int docIndex, final String pageID)
			throws DCMAApplicationException {

		String errMsg = null;
		final Pages pages = docTypesList.get(docIndex).getPages();

		if (null == pages) {
			errMsg = "Page id not found for the input document id.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		final List<Page> pageTypeList = pages.getPage();

		int pageIndex = 0;
		// PageType removePageType = null;
		for (final Page pageType : pageTypeList) {
			final String curPageID = pageType.getIdentifier();
			if (null == curPageID) {
				errMsg = "Page ID is null.";
				LOGGER.error(errMsg);
				throw new DCMAApplicationException(errMsg);
			}
			if (curPageID.equals(pageID)) {
				// removePageType = pageType;
				break;
			}
			pageIndex++;
		}

		if (pageIndex >= pageTypeList.size()) {
			errMsg = "Page id is not found for the input page id : " + pageID;
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		return pageIndex;
	}

	/**
	 * @param docTypesList List<DocumentType>
	 * @param docID String
	 * @return int document type index.
	 * @throws DCMAApplicationException If any one of the parameter is null or empty.
	 */
	private int getDocumentTypeIndex(final List<Document> docTypesList, final String docID) throws DCMAApplicationException {
		String errMsg = null;
		int docIndex = 0;
		for (final Document docType : docTypesList) {
			final String curDocID = docType.getIdentifier();
			if (null == curDocID) {
				errMsg = "Document ID is null.";
				LOGGER.error(errMsg);
				throw new DCMAApplicationException(errMsg);
			}
			if (curDocID.equals(docID)) {
				break;
			}
			docIndex++;
		}

		if (docIndex >= docTypesList.size()) {
			errMsg = "Document id is not found for the input document id : " + docID;
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		return docIndex;
	}

	/**
	 * An internal method to get unique page id.
	 * 
	 * @param batchInstanceFolderPath String
	 * @return String next page type id.
	 * @throws DCMAApplicationException
	 */
	private String getNewPageTypeID(final String batchInstanceFolderPath) throws DCMAApplicationException {
		String errMsg = null;
		String maxPageTypeID = null;
		if (batchInstanceFolderPath != null) {
			final File folderToCheck = new File(batchInstanceFolderPath);
			if (folderToCheck != null && folderToCheck.exists() && folderToCheck.isDirectory()) {
				final String[] listOfHtmlFiles = folderToCheck.list(new CustomFileFilter(false, BatchConstants.HTML_EXTENSION));
				maxPageTypeID = String.valueOf(listOfHtmlFiles.length);
			} else {
				errMsg = BatchConstants.UNABLE_TO_GET_UNIQUE_PAGE_ID;
				LOGGER.error(errMsg);
				throw new DCMAApplicationException(errMsg);
			}
		}
		return maxPageTypeID;
	}

	/**
	 * @param docTypesList List<DocumentType>
	 * @return String next document type id.
	 * @throws DCMAApplicationException If any one of the parameter is null or empty.
	 */
	private String getNewDocumentTypeID(final List<Document> docTypesList) throws DCMAApplicationException {

		String errMsg = null;

		checkDocTypeList(docTypesList);

		Long maxDocumentTypeID = 1L;

		for (final Document docType : docTypesList) {
			String curDocID = docType.getIdentifier();
			if (null == curDocID) {
				errMsg = "Document ID is null.";
				LOGGER.error(errMsg);
				throw new DCMAApplicationException(errMsg);
			}

			curDocID = curDocID.replaceAll(EphesoftProperty.DOCUMENT.getProperty(), "");
			final Long curPageIDLg = Long.parseLong(curDocID, BatchConstants.RADIX_BASE);

			if (curPageIDLg > maxDocumentTypeID) {
				maxDocumentTypeID = curPageIDLg;
			}
		}

		final String maxDocStr = Long.toString(maxDocumentTypeID + 1L);

		return maxDocStr;
	}

	/**
	 * An internal method to create duplicate pages and its physical contents.
	 * 
	 * @param duplicatePageType PageType
	 * @param batchInstanceIdentifier String
	 * @param newPageTypeID String
	 * @param batchInstanceFolderPath String
	 * @return PageType
	 * @throws DCMAApplicationException
	 */
	private Page duplicatePageFiles(final Page duplicatePageType, final String batchInstanceIdentifier, final String newPageTypeID,
			final String batchInstanceFolderPath) throws DCMAApplicationException {
		// Create duplicatePageType for all the images present at this page
		// type.
		String errMsg = null;
		final Page duplicateCopyPageType = new Page();
		duplicateCopyPageType.setIdentifier(EphesoftProperty.PAGE.getProperty() + newPageTypeID);
		duplicateCopyPageType.setOldFileName(newPageTypeID + BatchConstants.UNDER_SCORE + duplicatePageType.getOldFileName());
		duplicateCopyPageType.setDirection(duplicatePageType.getDirection());
		duplicateCopyPageType.setIsRotated(duplicatePageType.isIsRotated());
		final PageLevelFields pPageLevelFields = duplicatePageType.getPageLevelFields();
		final PageLevelFields copyPageLevelFields = new PageLevelFields();
		final List<DocField> pageLevelField = pPageLevelFields.getPageLevelField();
		final List<DocField> copyPageLevelField = copyPageLevelFields.getPageLevelField();
		copyPageLevelField.addAll(pageLevelField);
		duplicateCopyPageType.setPageLevelFields(copyPageLevelFields);

		final String newFileName = duplicatePageType.getNewFileName();
		String displayFileName = duplicatePageType.getDisplayFileName();
		final String hocrFileName = duplicatePageType.getHocrFileName();
		final String thumbnailFileName = duplicatePageType.getThumbnailFileName();
		final String oCRInputFileName = duplicatePageType.getOCRInputFileName();
		String originalXmlFile = batchInstanceFolderPath + File.separator + batchInstanceIdentifier + BatchConstants.UNDER_SCORE
				+ duplicatePageType.getIdentifier() + HOCR_FILE_NAME;

		FileNameFormatter fileNameFormatter = null;
		try {
			fileNameFormatter = new FileNameFormatter();
		} catch (IOException e) {
			errMsg = BatchConstants.FILE_NAME_FORMATTER_NOT_CREATED;
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg, e);
		}
		setDuplicateNewFileName(batchInstanceFolderPath, duplicateCopyPageType, newFileName, fileNameFormatter,
				batchInstanceIdentifier, newPageTypeID);

		setDuplicateDisplayFileName(batchInstanceFolderPath, duplicateCopyPageType, displayFileName, fileNameFormatter,
				batchInstanceIdentifier, newPageTypeID);

		setDuplicateHocrFileName(batchInstanceFolderPath, duplicateCopyPageType, hocrFileName, fileNameFormatter,
				batchInstanceIdentifier, newPageTypeID);

		setDupliateThumbnailFileName(batchInstanceFolderPath, duplicateCopyPageType, thumbnailFileName, fileNameFormatter,
				batchInstanceIdentifier, newPageTypeID);

		setDuplicateOCRFileName(batchInstanceFolderPath, duplicateCopyPageType, oCRInputFileName, fileNameFormatter,
				batchInstanceIdentifier, newPageTypeID);

		if (null != originalXmlFile) {
			try {
				StringBuilder xmlFileName = new StringBuilder(batchInstanceFolderPath + File.separator + batchInstanceIdentifier
						+ BatchConstants.UNDER_SCORE + duplicateCopyPageType.getIdentifier() + HOCR_FILE_NAME);
				final String zipFileName = originalXmlFile + FileType.ZIP.getExtensionWithDot();
				File srcFileHocrFileName = new File(zipFileName);
				if (srcFileHocrFileName.exists()) {
					originalXmlFile = zipFileName;
					xmlFileName.append(FileType.ZIP.getExtensionWithDot());
				} else {
					srcFileHocrFileName = new File(originalXmlFile);
				}
				final File destFileHocrFileName = new File(xmlFileName.toString());
				LOGGER.info("srcFileHocrFileName : " + srcFileHocrFileName);
				LOGGER.info("destFileHocrFileName : " + destFileHocrFileName);
				FileUtils.copyFile(srcFileHocrFileName, destFileHocrFileName, false);
			} catch (final IOException e) {
				errMsg = "Not able to create duplicate the xml file  " + originalXmlFile + BatchConstants.SPACE + e.getMessage();
				LOGGER.error(errMsg);
			}
			try {
				final HocrPages hocrPages = getHocrPages(batchInstanceIdentifier, duplicateCopyPageType.getIdentifier());
				if (null != hocrPages) {
					final HocrPage pages = hocrPages.getHocrPage().get(BatchConstants.ZERO);
					if (pages != null) {
						pages.setPageID(duplicateCopyPageType.getIdentifier());
						update(hocrPages, batchInstanceIdentifier, duplicateCopyPageType.getIdentifier());
					}
				}
			} catch (final Exception e) {
				LOGGER.error("Error in updating the xml file for new page type " + e.getMessage());
			}
		}

		return duplicateCopyPageType;
	}

	/**
	 * API to set Duplicate OCR file name.
	 * 
	 * @param batchInstanceFolderPath {@link String}
	 * @param duplicateCopyPage {@link Page}
	 * @param oCRInputFileName {@link String}
	 * @param fileNameFormatter {@link FileNameFormatter}
	 * @param batchInstanceId {@link String}
	 * @param newPageTypeID {@link String}
	 */
	private void setDuplicateOCRFileName(final String batchInstanceFolderPath, final Page duplicateCopyPage,
			final String oCRInputFileName, final FileNameFormatter fileNameFormatter, final String batchInstanceId,
			final String newPageTypeID) {
		String errMsg;
		if (null != oCRInputFileName) {
			final String localOCRInputFileName = oCRInputFileName.trim();

			try {
				final String oCRInputFileNameFormatter = fileNameFormatter.getOCRInputFileName(String.valueOf(batchInstanceId), null,
						null, BatchConstants.PNG_EXTENSION, String.valueOf(newPageTypeID));
				duplicateCopyPage.setOCRInputFileName(oCRInputFileNameFormatter);
			} catch (final FileFormatException e) {
				LOGGER.error(e.getMessage());
				duplicateCopyPage.setOCRInputFileName(newPageTypeID + BatchConstants.UNDER_SCORE + localOCRInputFileName);
			}
			try {
				final File srcFileOCRInputFileName = new File(batchInstanceFolderPath + File.separator + localOCRInputFileName);
				final File destFileOCRInputFileName = new File(batchInstanceFolderPath + File.separator
						+ duplicateCopyPage.getOCRInputFileName());
				LOGGER.info("srcFileOCRInputFileName : " + srcFileOCRInputFileName);
				LOGGER.info("destFileOCRInputFileName : " + destFileOCRInputFileName);
				FileUtils.copyFile(srcFileOCRInputFileName, destFileOCRInputFileName, false);
			} catch (final IOException e) {
				errMsg = "Not able to create duplicate the file oCRInputFileName: " + localOCRInputFileName + " " + e.getMessage();
				LOGGER.error(errMsg);
			}
		}
	}

	/**
	 * API to set Duplicate Thumbnail file name.
	 * 
	 * @param batchInstanceFolderPath {@link String}
	 * @param duplicateCopyPageType {@link Page}
	 * @param thumbnailFileName {@link String}
	 * @param fileNameFormatter {@link FileNameFormatter}
	 * @param batchInstanceIdentifier {@link String}
	 * @param newPageTypeID {@link String}
	 */
	private void setDupliateThumbnailFileName(final String batchInstanceFolderPath, final Page duplicateCopyPageType,
			final String thumbnailFileName, final FileNameFormatter fileNameFormatter, final String batchInstanceIdentifier,
			final String newPageTypeID) {
		String errMsg;
		if (null != thumbnailFileName) {
			final String localThumbnailFileName = thumbnailFileName.trim();
			try {
				final String thumbnailFileNameFormatter = fileNameFormatter.getDisplayThumbnailFileName(String
						.valueOf(batchInstanceIdentifier), null, null, null, BatchConstants.PNG_EXTENSION, String
						.valueOf(newPageTypeID));
				duplicateCopyPageType.setThumbnailFileName(thumbnailFileNameFormatter);
			} catch (final FileFormatException e) {
				LOGGER.error(e.getMessage());
				duplicateCopyPageType.setThumbnailFileName(newPageTypeID + BatchConstants.UNDER_SCORE + localThumbnailFileName);
			}
			try {
				final File srcFileThumbnailFileName = new File(batchInstanceFolderPath + File.separator + localThumbnailFileName);
				final File destFileThumbnailFileName = new File(batchInstanceFolderPath + File.separator
						+ duplicateCopyPageType.getThumbnailFileName());
				LOGGER.info("srcFileThumbnailFileName : " + srcFileThumbnailFileName);
				LOGGER.info("destFileThumbnailFileName : " + destFileThumbnailFileName);
				FileUtils.copyFile(srcFileThumbnailFileName, destFileThumbnailFileName, false);
				if (duplicateCopyPageType.isIsRotated()) {
					final File destFileThumbnailDirectionFileName = new File(batchInstanceFolderPath + File.separator
							+ duplicateCopyPageType.getDirection().toString() + File.separator
							+ duplicateCopyPageType.getThumbnailFileName());
					FileUtils.copyFile(srcFileThumbnailFileName, destFileThumbnailDirectionFileName, false);
				}

			} catch (final IOException e) {
				errMsg = "Not able to create duplicate the file thumbnailFileName: " + localThumbnailFileName + BatchConstants.SPACE
						+ e.getMessage();
				LOGGER.error(errMsg);
			}
		}
	}

	/**
	 * API to set Duplicate HOCR file name.
	 * 
	 * @param batchInstanceFolderPath {@link String}
	 * @param duplicateCopyPageType {@link Page}
	 * @param hocrFileName {@link String}
	 * @param fileNameFormatter {@link FileNameFormatter}
	 * @param batchInstanceIdentifier {@link String}
	 * @param newPageTypeID {@link String}
	 */
	private void setDuplicateHocrFileName(final String batchInstanceFolderPath, final Page duplicateCopyPageType,
			final String hocrFileName, final FileNameFormatter fileNameFormatter, final String batchInstanceIdentifier,
			final String newPageTypeID) {
		String errMsg;
		if (null != hocrFileName) {
			final String localHocrFileName = hocrFileName.trim();
			try {
				final String hocrFileNameFormatter = fileNameFormatter.getHocrFileName(String.valueOf(batchInstanceIdentifier), null,
						null, null, BatchConstants.HTML_EXTENSION, String.valueOf(newPageTypeID), false);
				duplicateCopyPageType.setHocrFileName(hocrFileNameFormatter);
			} catch (final FileFormatException e) {
				LOGGER.error(e.getMessage());
				duplicateCopyPageType.setHocrFileName(newPageTypeID + BatchConstants.UNDER_SCORE + localHocrFileName);
			}
			// create the copy of each file.
			try {
				final File srcFileHocrFileName = new File(batchInstanceFolderPath + File.separator + localHocrFileName);
				final File destFileHocrFileName = new File(batchInstanceFolderPath + File.separator
						+ duplicateCopyPageType.getHocrFileName());
				LOGGER.info("srcFileHocrFileName : " + srcFileHocrFileName);
				LOGGER.info("destFileHocrFileName : " + destFileHocrFileName);
				FileUtils.copyFile(srcFileHocrFileName, destFileHocrFileName, false);
			} catch (final IOException e) {
				errMsg = "Not able to create duplicate the file hocrFileName: " + localHocrFileName + " " + e.getMessage();
				LOGGER.error(errMsg);
			}
		}
	}

	/**
	 * API to set Duplicate Display file name.
	 * 
	 * @param batchInstanceFolderPath {@link String}
	 * @param duplicateCopyPageType {@link Page}
	 * @param displayFileName {@link String}
	 * @param fileNameFormatter {@link FileNameFormatter}
	 * @param batchInstanceIdentifier {@link String}
	 * @param newPageTypeID {@link String}
	 */
	private void setDuplicateDisplayFileName(final String batchInstanceFolderPath, final Page duplicateCopyPageType,
			final String displayFileName, final FileNameFormatter fileNameFormatter, final String batchInstanceIdentifier,
			final String newPageTypeID) {
		String errMsg;
		if (null != displayFileName) {
			final String localDisplayFileName = displayFileName.trim();
			try {
				final String displayFileNameFormatter = fileNameFormatter.getOCRInputFileName(String.valueOf(batchInstanceIdentifier),
						null, null, BatchConstants.PNG_EXTENSION, String.valueOf(newPageTypeID));
				duplicateCopyPageType.setDisplayFileName(displayFileNameFormatter);
			} catch (final FileFormatException e) {
				LOGGER.error(e.getMessage());
				duplicateCopyPageType.setDisplayFileName(newPageTypeID + BatchConstants.UNDER_SCORE + localDisplayFileName);
			}
			// create the copy of each file.
			try {
				final File srcFileDisplayFileName = new File(batchInstanceFolderPath + File.separator + localDisplayFileName);
				final File destFileDisplayFileName = new File(batchInstanceFolderPath + File.separator
						+ duplicateCopyPageType.getDisplayFileName());
				LOGGER.info("srcFileDisplayFileName : " + srcFileDisplayFileName);
				LOGGER.info("destFileDisplayFileName : " + destFileDisplayFileName);
				FileUtils.copyFile(srcFileDisplayFileName, destFileDisplayFileName, false);
				if (duplicateCopyPageType.isIsRotated()) {
					final File destFileDisplayDirectionFileName = new File(batchInstanceFolderPath + File.separator
							+ duplicateCopyPageType.getDirection().toString() + File.separator
							+ duplicateCopyPageType.getDisplayFileName());
					FileUtils.copyFile(srcFileDisplayFileName, destFileDisplayDirectionFileName, false);
				}
			} catch (final IOException e) {
				errMsg = "Not able to create duplicate the file displayFileName: " + localDisplayFileName + " " + e.getMessage();
				LOGGER.error(errMsg);
			}
		}
	}

	/**
	 * API to set Duplicate new file name.
	 * 
	 * @param batchInstanceFolderPath {@link String}
	 * @param duplicateCopyPageType {@link Page}
	 * @param newFileName {@link String}
	 * @param fileNameFormatter {@link FileNameFormatter}
	 * @param batchInstanceIdentifier {@link String}
	 * @param newPageTypeID {@link String}
	 */
	private void setDuplicateNewFileName(final String batchInstanceFolderPath, final Page duplicateCopyPageType,
			final String newFileName, final FileNameFormatter fileNameFormatter, final String batchInstanceIdentifier,
			final String newPageTypeID) {
		String errMsg;
		if (null != newFileName) {
			final String localNewFileName = newFileName.trim();
			try {
				final String newFileNameFormatter = fileNameFormatter.getNewFileName(String.valueOf(batchInstanceIdentifier), null,
						String.valueOf(newPageTypeID), BatchConstants.TIF_EXTENSION);
				duplicateCopyPageType.setNewFileName(newFileNameFormatter);
			} catch (final FileFormatException e) {
				LOGGER.error(e.getMessage());
				duplicateCopyPageType.setNewFileName(newPageTypeID + BatchConstants.UNDER_SCORE + localNewFileName);
			}
			// create the copy of each file.
			try {
				final File srcFileNewFileName = new File(batchInstanceFolderPath + File.separator + localNewFileName);
				final File destFileNewFileName = new File(batchInstanceFolderPath + File.separator
						+ duplicateCopyPageType.getNewFileName());
				LOGGER.info("srcFileNewFileName : " + srcFileNewFileName);
				LOGGER.info("destFileNewFileName : " + destFileNewFileName);
				FileUtils.copyFile(srcFileNewFileName, destFileNewFileName, false);
			} catch (final IOException e) {
				errMsg = "Not able to create duplicate the file newFileName: " + localNewFileName + BatchConstants.SPACE
						+ e.getMessage();
				LOGGER.error(errMsg);
			}
		}
	}

	/**
	 * Data carrier.
	 * 
	 * @author Ephesoft.
	 * 
	 */
	private class DocPageCarrier {

		/**
		 * pageIndex int.
		 */
		private int pageIndex;
		/**
		 * docIndex int.
		 */
		private int docIndex;
		/**
		 * docTypesList List<Document>.
		 */
		private List<Document> docTypesList;

		/**
		 * To get page index.
		 * 
		 * @return int
		 */
		public int getPageIndex() {
			return pageIndex;
		}

		/**
		 * To set page index.
		 * 
		 * @param pageIndex int
		 */
		public void setPageIndex(final int pageIndex) {
			this.pageIndex = pageIndex;
		}

		/**
		 * To get doc index.
		 * 
		 * @return int
		 */
		public int getDocIndex() {
			return docIndex;
		}

		/**
		 * To set doc index.
		 * 
		 * @param docIndex int
		 */
		public void setDocIndex(final int docIndex) {
			this.docIndex = docIndex;
		}

		/**
		 * To get Doc Types List.
		 * 
		 * @return List<Document>
		 */
		public List<Document> getDocTypesList() {
			return docTypesList;
		}

		/**
		 * To set Doc Types List.
		 * 
		 * @param docTypesList List<Document>
		 */
		public void setDocTypesList(final List<Document> docTypesList) {
			this.docTypesList = docTypesList;
		}

	}

	/**
	 * An API to get the URL object for the batch ID.
	 * 
	 * @param batchInstanceIdentifier String
	 * @return URL object.
	 */
	@Override
	public URL getBatchContextURL(final String batchInstanceIdentifier) {
		try {
			return new URL(this.getBatchFolderURL(batchInstanceIdentifier) + ICommonConstants.FORWARD_SLASH + batchInstanceIdentifier);
		} catch (final MalformedURLException e) {
			throw new DCMABusinessException(e.getMessage(), e);
		}
	}

	/**
	 * An API which will return true if the review is required other wise false.
	 * 
	 * @param batchInstanceIdentifier String
	 * @return boolean true if isReviewRequired other wise false.
	 * @throws DCMAApplicationException If any of the parameter is not valid.
	 */
	@Override
	public boolean isReviewRequired(final String batchInstanceIdentifier, final boolean checkReviewFlag)
			throws DCMAApplicationException {
		return isReviewRequired(batchInstanceIdentifier, checkReviewFlag, false);

	}

	private boolean isReviewRequired(final String batchInstanceIdentifier, final boolean checkReviewFlag,
			final boolean setDocumentReviewStatus) throws DCMAApplicationException {

		boolean isReviewRequired = false;

		String errMsg = null;

		if (null == batchInstanceIdentifier) {
			errMsg = "Input parameter batchInstanceIdentifier is null.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		final Batch batch = batchSchemaDao.get(batchInstanceIdentifier, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));

		final List<Document> docTypesList = batch.getDocuments().getDocument();

		checkDocTypeList(docTypesList);
		boolean isBatchReviewed = true;
		for (final Document document : docTypesList) {
			if (!document.isReviewed()) {
				isBatchReviewed = false;
			}
		}

		if (!isBatchReviewed) {
			if (!setDocumentReviewStatus) {
				for (final Document document : docTypesList) {
					final String docType = document.getType();
					if (null != docType && docType.equals(EphesoftProperty.UNKNOWN.getProperty())) {
						isReviewRequired = true;
						break;
					}
					if (checkReviewFlag) {
						isReviewRequired = !document.isReviewed();
						if (isReviewRequired) {
							break;
						}
					} else {
						final float confidence = document.getConfidence();
						final float confidenceThreshold = document.getConfidenceThreshold();
						if (confidenceThreshold >= confidence) {
							isReviewRequired = true;
							break;
						}
					}
				}
			} else {
				for (final Document document : docTypesList) {
					final String docType = document.getType();
					if (null != docType && docType.equals(EphesoftProperty.UNKNOWN.getProperty())) {
						document.setReviewed(false);
						isReviewRequired = true;
					} else {
						final float confidence = document.getConfidence();
						final float confidenceThreshold = document.getConfidenceThreshold();
						if (confidenceThreshold >= confidence) {
							document.setReviewed(false);
							isReviewRequired = true;
						} else {
							document.setReviewed(true);
						}
					}
				}
				updateBatch(batch);
			}
		}
		return isReviewRequired;
	}

	/**
	 * An API which will return true if the review is required other wise false.
	 * 
	 * @param batchInstanceIdentifier String
	 * @return boolean true if isReviewRequired other wise false.
	 * @throws DCMAApplicationException If any of the parameter is not valid.
	 */
	@Override
	public boolean isReviewRequired(final String batchInstanceIdentifier) throws DCMAApplicationException {
		return isReviewRequired(batchInstanceIdentifier, false, true);
	}

	/**
	 * An API which will return true if the validation is required for the document level fields other wise false.
	 * 
	 * @param batchInstanceIdentifier String
	 * @return boolean true if isValidationRequired other wise false.
	 * @throws DCMAApplicationException If any of the parameter is not valid.
	 */
	@Override
	public boolean isValidationRequired(final String batchInstanceIdentifier) throws DCMAApplicationException {

		boolean isValidationRequired = false;

		String errMsg = null;

		if (null == batchInstanceIdentifier) {
			errMsg = "Input parameter batchInstanceIdentifier is null.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		final Batch batch = batchSchemaDao.get(batchInstanceIdentifier, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));

		final List<Document> docTypesList = batch.getDocuments().getDocument();

		checkDocTypeList(docTypesList);

		for (final Document document : docTypesList) {
			// Add a check to insure that no document have document type "Unknown"
			// We can remove this check if isReviewRequired is handled properly.
			final String docType = document.getType();
			if (null != docType && docType.equals(EphesoftProperty.UNKNOWN.getProperty())) {
				isValidationRequired = true;
				break;
			}
			// End of check.
			final boolean boolIsValid = document.isValid();
			if (!boolIsValid) {
				isValidationRequired = true;
				break;
			}
		}

		return isValidationRequired;
	}

	/**
	 * An API to generate all the folder structure for samples.
	 * 
	 * @param batchClassIDList List<List<String>>
	 * @throws DCMAApplicationException If any of the parameter is not valid.
	 */
	@Override
	public void sampleGeneration(final List<List<String>> batchIdDocPgNameList) throws DCMAApplicationException {
		String errMsg = null;
		final String baseSampleFdLoc = batchSchemaDao.getJAXB2Template().getBaseSampleFdLoc();
		final String sampleFolders = batchSchemaDao.getJAXB2Template().getSampleFolders();

		if (null == batchIdDocPgNameList || batchIdDocPgNameList.isEmpty() || null == baseSampleFdLoc || null == sampleFolders) {
			errMsg = "Input parameter batchClassIDList are null or empty.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		final String[] nameOfFoldersArr = sampleFolders.split(BatchConstants.SEMI_COLON);
		boolean isFodlerNameAdded = true;
		for (final String fdName : nameOfFoldersArr) {
			final String dirPath = baseSampleFdLoc;
			for (final List<String> batchIdDocPgName : batchIdDocPgNameList) {
				final StringBuilder subDirPath = new StringBuilder();
				subDirPath.append(dirPath);
				isFodlerNameAdded = true;
				for (final String dirName : batchIdDocPgName) {
					if (null == dirName) {
						break;
					}
					subDirPath.append(File.separator);
					subDirPath.append(dirName);
					if (isFodlerNameAdded) {
						subDirPath.append(File.separator);
						subDirPath.append(fdName);
						isFodlerNameAdded = false;
					}
				}
				final String strManyDirectories = subDirPath.toString();
				try {
					// Create multiple directories
					boolean success = new File(strManyDirectories).mkdirs();
					if (success) {
						LOGGER.info("Directories: " + strManyDirectories + " created.");
					} else {
						LOGGER.info("Directories: " + strManyDirectories + " not created.");
					}
				} catch (final Exception e) {
					LOGGER.info("Directories: " + strManyDirectories + " not created.");
					LOGGER.error(e.getMessage());
				}
			}
		}
	}

	/**
	 * To delete the document type folder.
	 * 
	 * @param docTypeNameList List<String>
	 * @param batchClassIdentifier String
	 * @throws DCMAApplicationException if input parameter is null or empty
	 */
	@Override
	public void deleteDocTypeFolder(final List<String> docTypeNameList, final String batchClassIdentifier)
			throws DCMAApplicationException {
		String errMsg = null;
		final String baseSampleFdLoc = batchSchemaDao.getJAXB2Template().getBaseSampleFdLoc();
		final String sampleFolders = batchSchemaDao.getJAXB2Template().getSampleFolders();

		if (null == docTypeNameList || docTypeNameList.isEmpty() || null == baseSampleFdLoc || null == sampleFolders) {
			errMsg = "Input parameter docTypeNameList are null or empty.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		final String[] nameOfFoldersArr = sampleFolders.split(BatchConstants.SEMI_COLON);
		final StringBuilder docTypeDirPath = new StringBuilder(baseSampleFdLoc);
		docTypeDirPath.append(File.separator);
		docTypeDirPath.append(batchClassIdentifier);

		final String batchClassFolderPath = docTypeDirPath.toString();
		String docTypePath = null;
		String baseFolPath = null;
		for (final String fdName : nameOfFoldersArr) {
			baseFolPath = batchClassFolderPath + File.separator + fdName;
			for (final String docTypeName : docTypeNameList) {
				docTypePath = baseFolPath + File.separator + docTypeName;
				docTypePath = docTypePath.trim();
				try {
					final File docTypeFileName = new File(docTypePath);
					LOGGER.info("docTypeFileName : " + docTypeFileName);
					FileUtils.forceDelete(docTypeFileName);
				} catch (final IOException e) {
					errMsg = "Not able to delete the file docTypeFilePath: " + docTypePath + BatchConstants.SPACE + e.getMessage();
					LOGGER.info(errMsg);
				}
			}

		}

	}

	/**
	 * To get Thumb nail File Path.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param documentId String
	 * @param pageId String
	 * @return String
	 * @throws DCMAApplicationException if input parameters are null
	 */
	@Override
	public String getThumbnailFilePath(final String batchInstanceIdentifier, final String documentId, final String pageId)
			throws DCMAApplicationException {

		String errMsg = null;

		if (null == batchInstanceIdentifier || null == documentId || null == pageId) {
			errMsg = "Input parameters batchInstanceIdentifier/documentId/pageId are null.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		final Batch batch = batchSchemaDao.get(batchInstanceIdentifier, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));

		final List<Document> docTypesList = batch.getDocuments().getDocument();

		checkDocTypeList(docTypesList);

		final int docIndex = getDocumentTypeIndex(docTypesList, documentId);

		final int pageIndex = getPageTypeIndex(docTypesList, docIndex, pageId);

		final Document docType = docTypesList.get(docIndex);
		final Pages pages = docType.getPages();
		final List<Page> pageTypeList = pages.getPage();
		final Page page = pageTypeList.get(pageIndex);
		final String thumbnailFileName = page.getThumbnailFileName();

		final String thumbnailPath = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceIdentifier) + File.separator
				+ batchInstanceIdentifier + File.separator + thumbnailFileName;

		return thumbnailPath;
	}

	/**
	 * To get displayed image file path.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param documentId String
	 * @param pageId String
	 * @return String
	 * @throws DCMAApplicationException if input parameters are null
	 */
	@Override
	public String getDisplayImageFilePath(final String batchInstanceIdentifier, final String documentId, final String pageId)
			throws DCMAApplicationException {

		String errMsg = null;

		if (null == batchInstanceIdentifier || null == documentId || null == pageId) {
			errMsg = "Input parameters batchInstanceIdentifier/documentId/pageId are null.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		final Batch batch = batchSchemaDao.get(batchInstanceIdentifier, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));

		final List<Document> docTypesList = batch.getDocuments().getDocument();

		checkDocTypeList(docTypesList);

		final int docIndex = getDocumentTypeIndex(docTypesList, documentId);

		final int pageIndex = getPageTypeIndex(docTypesList, docIndex, pageId);

		final Document docType = docTypesList.get(docIndex);
		final Pages pages = docType.getPages();
		final List<Page> pageTypeList = pages.getPage();
		final Page page = pageTypeList.get(pageIndex);
		final String displayFileName = page.getDisplayFileName();

		final String displayFilePath = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceIdentifier)
				+ File.separator + batchInstanceIdentifier + File.separator + displayFileName;

		return displayFilePath;
	}

	/**
	 * An API to generate the xml file for all the htmls generated by image process plug-ins.
	 * 
	 * @return batchInstanceIdentifier String
	 * @throws DCMAException If any error occurs.
	 */
	public void htmlToXmlGeneration(final String batchInstanceIdentifier) throws DCMAException {
		try {
			htmlOutputGeneration(batchInstanceIdentifier);
		} catch (final DCMABusinessException e) {
			LOGGER.error(e.getMessage());
			throw new DCMAException(e.getMessage(), e);
		} catch (final DCMAApplicationException e) {
			LOGGER.error(e.getMessage());
			throw new DCMAException(e.getMessage(), e);
		}
	}

	private void htmlOutputGeneration(final String batchInstanceIdentifier) throws DCMAApplicationException, DCMABusinessException {
		final String actualFolderLocation = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceIdentifier)
				+ File.separator + batchInstanceIdentifier + File.separator;

		final Batch batch = getBatch(batchInstanceIdentifier);
		String outputFileName = "tempFile";
		String outputFilePath = null;

		final List<Document> xmlDocuments = batch.getDocuments().getDocument();
		final BatchInstanceThread batchInstanceThread = new BatchInstanceThread(batchInstanceIdentifier);
		outputFilePath = actualFolderLocation + outputFileName;
		String outputFilePathLocal = null;
		if (null != xmlDocuments) {
			for (final Document document : xmlDocuments) {
				final List<Page> listOfPages = document.getPages().getPage();
				if (null != listOfPages) {
					for (final Page page : listOfPages) {
						outputFilePathLocal = outputFilePath + page.getIdentifier();
						hOCRGenerationUsingThreadpool(batchInstanceThread, batchInstanceIdentifier, actualFolderLocation,
								outputFilePathLocal, page);
					}
				}
			}
			try {
				LOGGER.info("Creating HOCR for all pages using threadpool.");
				batchInstanceThread.execute();
				LOGGER.info("HOCRgenerated succussfully for all pages.");
			} catch (final DCMAApplicationException dcmae) {
				LOGGER.error("Error while genmerating hOCR from html using threadpool");
				batchInstanceThread.remove();
				throw new DCMAApplicationException("Error while genmerating hOCR from html using threadpool ." + dcmae.getMessage(),
						dcmae);
			}
		}

	}

	private void hOCRGenerationUsingThreadpool(final BatchInstanceThread batchInstanceThread, final String batchInstanceIdentifier,
			final String actualFolderLocation, final String outputFilePath, final Page page) {
		batchInstanceThread.add(new AbstractRunnable() {

			@Override
			public void run() {
				String pageID;
				String hocrFileName;
				String pathOfHOCRFile;
				FileInputStream inputStream = null;
				final HocrPages hocrPages = new HocrPages();
				final List<HocrPage> hocrPageList = hocrPages.getHocrPage();

				final HocrPage hocrPage = new HocrPage();
				pageID = page.getIdentifier();
				hocrPage.setPageID(pageID);
				hocrPageList.add(hocrPage);
				hocrFileName = page.getHocrFileName();
				pathOfHOCRFile = actualFolderLocation + hocrFileName;
				LOGGER.info("Creating hOCR for page : " + pageID);
				try {
					inputStream = hocrGenerationInternal(actualFolderLocation, outputFilePath, pageID, pathOfHOCRFile, hocrPage);
				} catch (final IOException e) {
					LOGGER.error(e.getMessage(), e);
				} catch (final Exception e) {
					LOGGER.error(e.getMessage(), e);
				} finally {
					try {
						if (inputStream != null) {
							inputStream.close();
						}
					} catch (final IOException e) {
						LOGGER.error(e.getMessage(), e);
					}
				}
				createHocr(hocrPages, batchInstanceIdentifier, pageID);
				try {
					LOGGER.info("Deleting temp file : " + outputFilePath);
					FileUtils.forceDelete(new File(outputFilePath));
				} catch (final IOException e) {
					LOGGER.info("Deleting the temp file." + e.getMessage());
				}
			}

		});
	}

	/**
	 * API to generate HOCR.
	 * 
	 * @param workingDir String
	 * @param pageID String
	 * @param pathOfHOCRFile String
	 * @param hocrPage HocrPage
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws XPathExpressionException
	 * @throws TransformerException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	@Override
	public void hocrGenerationAPI(final String workingDir, final String pageID, final String pathOfHOCRFile, final HocrPage hocrPage)
			throws FileNotFoundException, IOException, XPathExpressionException, TransformerException, ParserConfigurationException,
			SAXException {
		final String outputFilePath = workingDir + File.separator + "tempFile" + pageID;
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = hocrGenerationInternal(workingDir, outputFilePath, pageID, pathOfHOCRFile, hocrPage);
		} finally {
			if (fileInputStream != null) {
				fileInputStream.close();
			}
		}
	}

	/**
	 * Method extracted to be reused for Ephesoft Web Services.
	 * 
	 * @param actualFolderLocation
	 * @param outputFilePath
	 * @param pageID
	 * @param pathOfHOCRFile
	 * @param hocrPage
	 * @return FileInputStream
	 * @throws IOException
	 * @throws TransformerException
	 * @throws XPathExpressionException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	private FileInputStream hocrGenerationInternal(final String actualFolderLocation, final String outputFilePath,
			final String pageID, final String pathOfHOCRFile, final HocrPage hocrPage) throws XPathExpressionException,
			TransformerException, IOException, ParserConfigurationException, SAXException {

		XMLUtil.htmlOutputStream(pathOfHOCRFile, outputFilePath);
		OCREngineUtil.formatHOCRForTesseract(outputFilePath, actualFolderLocation, pageID);

		final FileInputStream inputStream = new FileInputStream(outputFilePath);
		final org.w3c.dom.Document doc = XMLUtil.createDocumentFrom(inputStream);
		final NodeList titleNodeList = doc.getElementsByTagName(BatchConstants.TITLE);
		if (null != titleNodeList) {
			for (int index = 0; index < titleNodeList.getLength(); index++) {
				final Node node = titleNodeList.item(index);
				final NodeList childNodeList = node.getChildNodes();
				final Node nodeChild = childNodeList.item(BatchConstants.ZERO);
				if (null != nodeChild) {
					final String value = nodeChild.getNodeValue();
					if (value != null) {
						hocrPage.setTitle(value);
						break;
					}
				}
			}
		}

		final NodeList spanNodeList = doc.getElementsByTagName("span");
		final Spans spans = new Spans();
		hocrPage.setSpans(spans);
		final List<Span> spanList = spans.getSpan();
		if (null != spanNodeList) {
			final StringBuilder hocrContent = new StringBuilder();
			for (int index = BatchConstants.ZERO; index < spanNodeList.getLength(); index++) {
				final Node node = spanNodeList.item(index);
				final NodeList childNodeList = node.getChildNodes();
				final Node nodeChild = childNodeList.item(BatchConstants.ZERO);
				final Span span = new Span();
				if (null != nodeChild) {
					final String value = nodeChild.getNodeValue();
					span.setValue(value);
					hocrContent.append(value);
					hocrContent.append(BatchConstants.SPACE);
				}
				spanList.add(span);
				final NamedNodeMap map = node.getAttributes();
				final Node nMap = map.getNamedItem(BatchConstants.TITLE);
				Coordinates hocrCoordinates = null;
				hocrCoordinates = getHOCRCoordinates(nMap, hocrCoordinates);
				if (null == hocrCoordinates) {
					hocrCoordinates = new Coordinates();
					hocrCoordinates.setX0(BigInteger.ZERO);
					hocrCoordinates.setX1(BigInteger.ZERO);
					hocrCoordinates.setY0(BigInteger.ZERO);
					hocrCoordinates.setY1(BigInteger.ZERO);
				}
				span.setCoordinates(hocrCoordinates);
			}
			hocrPage.setHocrContent(hocrContent.toString());
		}
		return inputStream;
	}

	/**
	 * This API is used to generate the HocrPage object for input hocr file.
	 * 
	 * @param pageName {@link String}
	 * @param pathOfHOCRFile {@link String}
	 * @param outputFilePath {@link String}
	 * @param batchClassIdentifier {@link String}
	 * @param ocrEngineName {@link String}
	 * @return {@link HocrPage}
	 */
	@Override
	public HocrPage generateHocrPage(final String pageName, final String pathOfHOCRFile, final String outputFilePath,
			final String batchClassIdentifier, final String ocrEngineName) {

		if (null == pathOfHOCRFile || null == outputFilePath) {
			return null;
		}
		final BatchPluginConfiguration[] pluginConfiguration = batchClassPluginPropertiesService.getPluginProperties(
				batchClassIdentifier, TESSERACT_HOCR_PLUGIN, TesseractVersionProperty.TESSERACT_VERSIONS);
		String tesseractVersion = BatchConstants.EMPTY;
		if (pluginConfiguration != null && pluginConfiguration.length > BatchConstants.ZERO
				&& pluginConfiguration[BatchConstants.ZERO].getValue() != null
				&& pluginConfiguration[BatchConstants.ZERO].getValue().length() > BatchConstants.ZERO) {
			tesseractVersion = pluginConfiguration[BatchConstants.ZERO].getValue();
		}
		final HocrPage hocrPage = new HocrPage();
		hocrPage.setPageID(pageName);
		FileInputStream inputStream = null;
		try {
			if (ocrEngineName.equalsIgnoreCase(IUtilCommonConstants.TESSERACT_HOCR_PLUGIN)
					&& tesseractVersion.equalsIgnoreCase(TesseractVersionProperty.TESSERACT_VERSION_3.getPropertyKey())) {
				XMLUtil.htmlOutputStream(pathOfHOCRFile, outputFilePath);
				final String actualFolderLocation = new File(outputFilePath).getParent();
				OCREngineUtil.formatHOCRForTesseract(outputFilePath, actualFolderLocation, pageName);
			} else {
				XMLUtil.htmlOutputStream(pathOfHOCRFile, outputFilePath);
			}
			inputStream = new FileInputStream(outputFilePath);
			final org.w3c.dom.Document doc = XMLUtil.createDocumentFrom(inputStream);
			final NodeList titleNodeList = doc.getElementsByTagName(BatchConstants.TITLE);
			if (null != titleNodeList) {
				for (int index = BatchConstants.ZERO; index < titleNodeList.getLength(); index++) {
					final Node node = titleNodeList.item(index);
					final NodeList childNodeList = node.getChildNodes();
					final Node nodeChild = childNodeList.item(BatchConstants.ZERO);
					if (null != nodeChild) {
						final String value = nodeChild.getNodeValue();
						if (value != null) {
							hocrPage.setTitle(value);
							break;
						}
					}
				}
			}

			final NodeList spanNodeList = doc.getElementsByTagName("span");
			final Spans spans = new Spans();
			hocrPage.setSpans(spans);
			final List<Span> spanList = spans.getSpan();
			if (null != spanNodeList) {
				final StringBuilder hocrContent = new StringBuilder();
				for (int index = 0; index < spanNodeList.getLength(); index++) {
					final Node node = spanNodeList.item(index);
					final NodeList childNodeList = node.getChildNodes();
					final Node nodeChild = childNodeList.item(BatchConstants.ZERO);
					final Span span = new Span();
					if (null != nodeChild) {
						final String value = nodeChild.getNodeValue();
						span.setValue(value);
						hocrContent.append(value);
						hocrContent.append(BatchConstants.SPACE);
					}
					spanList.add(span);
					final NamedNodeMap map = node.getAttributes();
					final Node nMap = map.getNamedItem(BatchConstants.TITLE);
					Coordinates hocrCoordinates = null;
					hocrCoordinates = getHOCRCoordinates(nMap, hocrCoordinates);
					if (null == hocrCoordinates) {
						hocrCoordinates = new Coordinates();
						hocrCoordinates.setX0(BigInteger.ZERO);
						hocrCoordinates.setX1(BigInteger.ZERO);
						hocrCoordinates.setY0(BigInteger.ZERO);
						hocrCoordinates.setY1(BigInteger.ZERO);
					}
					span.setCoordinates(hocrCoordinates);
				}
				hocrPage.setHocrContent(hocrContent.toString());
			}
		} catch (final IOException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
		return hocrPage;
	}

	private Coordinates getHOCRCoordinates(final Node nMap, Coordinates hocrCoordinates) {
		Coordinates localHocrCoordinates = hocrCoordinates;
		try {
			final String coordinates = nMap.getNodeValue();
			final String[] arr = coordinates.split(BatchConstants.SPACE);
			if (null != arr && arr.length >= 4) {
				localHocrCoordinates = new Coordinates();
				localHocrCoordinates.setX0(new BigInteger(arr[1]));
				localHocrCoordinates.setX1(new BigInteger(arr[3]));
				localHocrCoordinates.setY0(new BigInteger(arr[2]));
				localHocrCoordinates.setY1(new BigInteger(arr[4]));
			}
		} catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return localHocrCoordinates;
	}

	/**
	 * An API to fetch the image URL's excluding the imagename.
	 * 
	 * @return the URL to be appended to the image name.
	 */
	@Override
	public String getWebScannerScannedImagesURL() {
		return getBaseHttpURL() + BatchConstants.SLASH + batchSchemaDao.getJAXB2Template().getWebScannerScannedImagesFolderPath();
	}

	/**
	 * An API to fetch the folder in which the scanned images are being kept.
	 * 
	 * @return the scanned images folder path.
	 */
	@Override
	public String getWebScannerScannedImagesFolderPath() {
		return getBaseFolderLocation() + File.separator + batchSchemaDao.getJAXB2Template().getWebScannerScannedImagesFolderPath();
	}

	/**
	 * API to create a temp folder for scanned images for the given name.
	 * 
	 * @param folderName {@link String}
	 * @throws DCMAApplicationException if file not created
	 */
	@Override
	public void createWebScannerFolder(final String folderName) {
		String errMsg = null;
		final File newDirectory = new File(getWebScannerScannedImagesFolderPath() + File.separator + folderName);
		try {
			FileUtils.forceMkdir(newDirectory);
		} catch (final IOException e) {
			errMsg = "Not able to create the file " + folderName + " " + e.getMessage();
			LOGGER.error(errMsg);
		}
	}

	/**
	 * API to copy all the .tiff and .tif files from the sourcePath+folderName to the batchPath+folderName.
	 * 
	 * @param sourcePath {@link String} the path containing the folder to be copied
	 * @param folderName {@link String} the folder to be copied.
	 * @param batchClassID {@link String} the identifier of batch to which the batch is to be copied
	 * @throws DCMAApplicationException if error occurs
	 */
	@Override
	public void copyFolder(final String sourcePath, final String folderName, final String batchClassID)
			throws DCMAApplicationException {
		final String scannedImagesPath = sourcePath + File.separator + folderName;
		final File scannedImagesDirectory = new File(scannedImagesPath);
		final CustomFileFilter validFilesFilter = new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(), FileType.TIFF
				.getExtensionWithDot());
		List<File> validFilesToBeCopied = null;
		try {
			validFilesToBeCopied = getFiles(scannedImagesPath, validFilesFilter);
		} catch (final IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		if (validFilesToBeCopied == null || validFilesToBeCopied.isEmpty()) {
			throw new DCMAApplicationException("Error: No files scanned.");
		}

		final CustomFileFilter filesToBeDeletedFilter = new CustomFileFilter(true, FileType.TIF.getExtensionWithDot(), FileType.TIFF
				.getExtensionWithDot(), FileType.SER.getExtensionWithDot(), FileType.XML.getExtensionWithDot());
		final BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassID);
		final File uncFolder = new File(batchClass.getUncFolder() + File.separator + folderName);
		try {
			for (final File file : getFiles(scannedImagesPath, filesToBeDeletedFilter)) {
				try {
					FileUtils.forceDelete(file);
					LOGGER.info("Deleting File : " + file.getAbsolutePath());
				} catch (final Exception e) {
					LOGGER.error("Unable to delete file : " + file.getAbsolutePath());
				}
			}
			FileUtils.copyDirectory(scannedImagesDirectory, uncFolder);
			LOGGER.info("Copying directory " + scannedImagesDirectory.getAbsolutePath() + "to " + uncFolder.getAbsolutePath());
			FileUtils.forceDelete(scannedImagesDirectory);
			LOGGER.info("Deleting directory :" + scannedImagesDirectory.getAbsolutePath());
		} catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new DCMAApplicationException(e.getMessage(), e);
		}
	}

	/**
	 * API to copy all the files from the sourcePath+folderName to the batchPath+folderName. Valid file types are specified by the
	 * filter.
	 * 
	 * @param sourcePath {@link String} the path containing the folder to be copied
	 * @param folderName {@link String} the folder to be copied.
	 * @param batchClassID {@link String} the identifier of batch to which the batch is to be copied
	 * @param fileFilter {@link CustomFileFilter} the file filter specifying the valid file types to copy
	 * @throws DCMAApplicationException if error occurs in deleting
	 */
	@Override
	public void copyFolderWithFileFilter(final String sourcePath, final String folderName, final String batchClassID,
			final CustomFileFilter fileFilter) throws DCMAApplicationException {
		final String scannedImagesPath = sourcePath + File.separator + folderName;
		final File scannedImagesDirectory = new File(scannedImagesPath);
		final BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassID);
		final File uncFolder = new File(batchClass.getUncFolder() + File.separator + folderName);
		try {
			for (final File file : getFiles(scannedImagesPath, fileFilter)) {
				try {
					FileUtils.forceDelete(file);
					LOGGER.info("Deleting File : " + file.getAbsolutePath());
				} catch (final Exception e) {
					LOGGER.error("Unable to delete file : " + file.getAbsolutePath());
				}
			}
			FileUtils.copyDirectory(scannedImagesDirectory, uncFolder);
			LOGGER.info("Copying directory " + scannedImagesDirectory.getAbsolutePath() + "to " + uncFolder.getAbsolutePath());
			FileUtils.forceDelete(scannedImagesDirectory);
			LOGGER.info("Deleting directory :" + scannedImagesDirectory.getAbsolutePath());
		} catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new DCMAApplicationException(e.getMessage(), e);
		}
	}

	private List<File> getFiles(final String srcPath, final CustomFileFilter fileFilter) throws IOException {
		final File srcDir = new File(srcPath);
		final List<File> fileList = new ArrayList<File>();
		if (srcDir != null && srcDir.list(fileFilter) != null) {
			for (final String imagename : srcDir.list(fileFilter)) {
				fileList.add(new File(srcPath + File.separator + imagename));
			}
		}
		return fileList;
	}

	/**
	 * API to return base folder location.
	 * 
	 * @return String
	 */
	@Override
	public String getBaseFolderLocation() {
		return batchSchemaDao.getJAXB2Template().getBaseFolderLocation();
	}

	/**
	 * An API to generate the xml file for all the htmls generated by image process plug-ins.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @throws DCMAException If any error occurs.
	 */
	@Override
	public void htmlToXmlGeneration(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException {
		final String batchInstanceIdentifier = batchInstanceID.getID();
		try {
			this.htmlOutputGeneration(batchInstanceIdentifier);
		} catch (final Exception e) {
			throw new DCMAException(e.getMessage(), e);
		}
		LOGGER.info("Copying File : " + batchInstanceID);
		BackUpFileService.backUpBatch(batchInstanceIdentifier, pluginWorkflow, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));
	}

	/**
	 * An API to fetch project File Base Folder location.
	 * 
	 * @return String
	 */
	@Override
	public String getProjectFileBaseFolder() {
		return batchSchemaDao.getJAXB2Template().getProjectFilesBaseFolder();
	}

	/**
	 * An API to get the project files for the given document type and batch class identifier.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param documentType {@link String}
	 * @return List<String>
	 * @throws DCMAException if error occurs
	 */
	@Override
	public List<String> getProjectFilesForDocumentType(final String batchClassIdentifier, final String documentType)
			throws DCMAException {
		final List<String> returnList = new ArrayList<String>();
		if (batchClassIdentifier != null && batchClassIdentifier.length() > BatchConstants.ZERO) {
			final String projectFileBaseFolder = batchSchemaDao.getJAXB2Template().getProjectFilesBaseFolder();
			final File projectFilesFolder = new File(getBaseFolderLocation() + File.separator + batchClassIdentifier + File.separator
					+ projectFileBaseFolder);
			if (projectFilesFolder.exists()) {
				final String[] allProjectFiles = projectFilesFolder.list();
				if (allProjectFiles != null && allProjectFiles.length > BatchConstants.ZERO) {
					for (final String string : allProjectFiles) {
						if (string.endsWith(".rsp")) {
							returnList.add(string);
						}
					}
				}
			}
		}
		return returnList;
	}

	/**
	 * An API to fetch Email Base Folder location using Http protocol.
	 * 
	 * @return String
	 */
	@Override
	public String getHttpEmailFolderPath() {
		return getBaseHttpURL() + ICommonConstants.FORWARD_SLASH + batchSchemaDao.getJAXB2Template().getEmailFolderName();
	}

	/**
	 * An API to fetch Batches Folder location.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @return String
	 */
	@Override
	public String getBatchFolderURL(String batchInstanceIdentifier) {
		final File file = new File(batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));
		return getBaseHttpURL() + ICommonConstants.FORWARD_SLASH + file.getName();
	}

	/**
	 * An API to fetch Email Base Folder location.
	 * 
	 * @return String
	 */
	@Override
	public String getEmailFolderPath() {
		return getBaseSampleFDLock() + File.separator + batchSchemaDao.getJAXB2Template().getEmailFolderName();
	}

	/**
	 * API to get test kv extraction folder path.
	 * 
	 * @param batchClassID {@link BatchClassID}
	 * @param createDirectory boolean
	 * @return String absolute folder location
	 */
	@Override
	public String getTestKVExtractionFolderPath(final BatchClassID batchClassID, final boolean createDirectory) {
		return getAbsolutePath(batchClassID.getID(), batchSchemaDao.getJAXB2Template().getTestKVExtractionFolderName(),
				createDirectory);
	}

	/**
	 * An API to return the complete export batch class folder location path.
	 * 
	 * @return String Batch Export Folder Location
	 */
	@Override
	public String getBatchExportFolderLocation() {
		return batchSchemaDao.getJAXB2Template().getBatchExportFolder();
	}

	/**
	 * An API to return the Index folder name for fuzzy db.
	 * 
	 * @return String fuzzyDb Index Folder Path
	 */
	@Override
	public String getFuzzyDBIndexFolderName() {
		return batchSchemaDao.getJAXB2Template().getFuzzyDBIndexFolderName();
	}

	/**
	 * An API to return the Index folder name for fuzzy db.
	 * 
	 * @return String fuzzyDb Index Folder Path
	 */
	@Override
	public String getImagemagickBaseFolderName() {
		return batchSchemaDao.getJAXB2Template().getImageMagickBaseFolderName();
	}

	/**
	 * An API to return the Index folder name for fuzzy db.
	 * 
	 * @return String fuzzyDb Index Folder Path
	 */
	@Override
	public String getSearchIndexFolderName() {
		return batchSchemaDao.getJAXB2Template().getSearchIndexFolderName();
	}

	/**
	 * An API to return the Index folder name for fuzzy db.
	 * 
	 * @return String fuzzyDb Index Folder Path
	 */
	@Override
	public String getSearchSampleName() {
		return batchSchemaDao.getJAXB2Template().getSearchSampleName();
	}

	/**
	 * An API to return the test kv_extraction sample folder name.
	 * 
	 * @return String test kv_extraction sample folder name
	 */
	@Override
	public String getTestKVExtractionFolderName() {
		return batchSchemaDao.getJAXB2Template().getTestKVExtractionFolderName();
	}

	/**
	 * An API to return the Batch Class Serializable File.
	 * 
	 * @return String name of batch Class Serializable Description as appearing on the UI
	 */
	@Override
	public String getBatchClassSerializableFile() {
		return batchSchemaDao.getJAXB2Template().getBatchClassSerializableFile();
	}

	/**
	 * API to get File bound Plugin Mapping Folder Name.
	 * 
	 * @return String Name of filebound plugin mapping folder
	 */
	@Override
	public String getFileboundPluginMappingFolderName() {
		return batchSchemaDao.getJAXB2Template().getFileboundPluginMappingFolderName();
	}

	/**
	 * API to get the temp folder location for a batch class.
	 * 
	 * @return {@link String}
	 */
	@Override
	public String getTempFolderName() {
		return batchSchemaDao.getJAXB2Template().getTempFolder();
	}

	/**
	 * API to copy all the files from the Email download folder to the UNC Folder.
	 * 
	 * @param sourcePath {@link String} the path containing the folder to be copied
	 * @param folderName {@link String} the folder to be copied.
	 * @param batchClassID {@link String} the identifier of batch to which the batch is to be copied
	 * @throws DCMAApplicationException if error occurs.
	 */
	@Override
	public void copyEmailFolderToUNC(final String sourcePath, final String folderName, final String batchClassID)
			throws DCMAApplicationException {
		// Getting Ephesoft cloud user type
		final Integer userType = getUserType();
		final String scannedImagesPath = sourcePath + File.separator + folderName;
		final File scannedImagesDirectory = new File(scannedImagesPath);
		// Discard file after limit is crossed
		discardOutOfLimitFile(scannedImagesDirectory, batchClassID, userType);
		final BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassID);
		final File uncFolder = new File(batchClass.getUncFolder() + File.separator + folderName);
		try {
			FileUtils.copyDirectory(scannedImagesDirectory, uncFolder);
			LOGGER.info("Copying directory " + scannedImagesDirectory.getAbsolutePath() + "to " + uncFolder.getAbsolutePath());
			FileUtils.forceDelete(scannedImagesDirectory);
			LOGGER.info("Deleting directory :" + scannedImagesDirectory.getAbsolutePath());
		} catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new DCMAApplicationException(e.getMessage(), e);
		}
	}

	/**
	 * API to get test table folder path.
	 * 
	 * @param batchClassID {@link BatchClassID}
	 * @param createDirectory {@link boolean}
	 * @return {@link String} absolute folder location
	 */
	@Override
	public String getTestTableFolderPath(final BatchClassID batchClassID, final boolean createDirectory) {
		return getAbsolutePath(batchClassID.getID(), batchSchemaDao.getJAXB2Template().getTestTableFolderName(), createDirectory);
	}

	/**
	 * API to create the batch XML at the end of the specified plugin.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 */
	@Override
	public void backUpBatchXML(final BatchInstanceID batchInstanceID, final String pluginWorkflow) {
		final String batchInstanceIdentifier = batchInstanceID.getID();
		BackUpFileService.backUpBatch(batchInstanceIdentifier, pluginWorkflow, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));
	}

	/**
	 * An API to return the test table sample folder name.
	 * 
	 * @return {@link String} test table sample folder name
	 */

	@Override
	public String getTestTableFolderName() {
		return batchSchemaDao.getJAXB2Template().getTestTableFolderName();
	}

	/**
	 * API to return the ValidationScriptName.
	 * 
	 * @return {@link String} ValidationScriptName
	 */
	@Override
	public String getValidationScriptName() {
		return batchSchemaDao.getJAXB2Template().getValidationScriptName();
	}

	/**
	 * API to return the threadpool lock folder. This folder signifies if any thread is currently running for this batch instance at
	 * any point of time.
	 * 
	 * @return {@link String} thread pool lock folder name
	 */
	@Override
	public String getThreadpoolLockFolderName() {
		return batchSchemaDao.getJAXB2Template().getThreadpoolLockFolder();
	}

	/**
	 * API to add new table.
	 * 
	 * @return {@link String} AddNewTableScriptName
	 */
	@Override
	public String getAddNewTableScriptName() {
		return batchSchemaDao.getJAXB2Template().getAddNewTableScriptName();
	}

	/**
	 * API to set local folder.
	 * 
	 * @param testFolderLocation {@link String}
	 */
	@Override
	public void setTestFolderLocation(final String testFolderLocation) {
		if (null != testFolderLocation && !testFolderLocation.isEmpty()) {
			batchSchemaDao.getJAXB2Template().setTestFolderLocation(testFolderLocation);
		}
	}

	/**
	 * API to return test folder.
	 * 
	 * @return {@link String}
	 */
	@Override
	public String getTestFolderLocation() {
		return batchSchemaDao.getJAXB2Template().getTestFolderLocation();
	}

	/**
	 * API to get Script Config Folder Name.
	 * 
	 * @return {@link String}
	 */
	@Override
	public String getScriptConfigFolderName() {
		return batchSchemaDao.getJAXB2Template().getScriptConfigFolderName();
	}

	/**
	 * API to set base folder of the project.
	 * 
	 * @param baseFolderLocation {@link String}
	 */
	@Override
	public void setBaseFolderLocation(final String baseFolderLocation) {
		if (null != baseFolderLocation && !baseFolderLocation.isEmpty()) {
			batchSchemaDao.getJAXB2Template().setBaseFolderLocation(baseFolderLocation);
			batchSchemaDao.getJAXB2Template().setBaseSampleFdLoc(baseFolderLocation);
		}

	}

	/**
	 * API to get the detached batch class module object for a batch class.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param moduleName {@link String}
	 * @return {@link BatchClassModule}
	 */
	@Override
	public BatchClassModule getDetachedBatchClassModuleByName(final String batchClassIdentifier, final String workflowName) {
		final BatchClassModule batchClassModule = batchClassModuleService.getBatchClassModuleByWorkflowName(batchClassIdentifier,
				workflowName);
		BatchClassModule detachedBatchClassModule = null;
		if (batchClassModule == null) {
			LOGGER.info("No module found with workflowName:" + workflowName + " in batch class id:" + batchClassIdentifier
					+ ". Skipping the updates for this module.");
		} else {
			batchClassModuleService.evict(batchClassModule);
			detachedBatchClassModule = new BatchClassModule();

			detachedBatchClassModule.setId(BatchConstants.ZERO);
			detachedBatchClassModule.setBatchClass(null);

			detachedBatchClassModule.setModule(batchClassModule.getModule());
			detachedBatchClassModule.setWorkflowName(batchClassModule.getWorkflowName());
			detachedBatchClassModule.setOrderNumber(batchClassModule.getOrderNumber());
			detachedBatchClassModule.setRemoteBatchClassIdentifier(batchClassModule.getRemoteBatchClassIdentifier());
			detachedBatchClassModule.setRemoteURL(batchClassModule.getRemoteURL());

			final List<BatchClassModuleConfig> moduleConfigs = batchClassModule.getBatchClassModuleConfig();
			final List<BatchClassModuleConfig> newModuleConfigList = new ArrayList<BatchClassModuleConfig>();
			for (final BatchClassModuleConfig moduleConfig : moduleConfigs) {
				newModuleConfigList.add(moduleConfig);
				moduleConfig.setId(BatchConstants.ZERO);
				moduleConfig.setBatchClassModule(null);
			}
			detachedBatchClassModule.setBatchClassModuleConfig(newModuleConfigList);

			final List<BatchClassPlugin> batchClassPlugins = batchClassModule.getBatchClassPlugins();
			final List<BatchClassPlugin> newBatchClassPluginsList = new ArrayList<BatchClassPlugin>();
			for (final BatchClassPlugin batchClassPlugin : batchClassPlugins) {
				newBatchClassPluginsList.add(batchClassPlugin);
				batchClassPlugin.setId(BatchConstants.ZERO);
				batchClassPlugin.setBatchClassModule(null);

				final List<BatchClassPluginConfig> batchClassPluginConfigs = batchClassPlugin.getBatchClassPluginConfigs();
				final List<BatchClassPluginConfig> newBatchClassPluginConfigsList = new ArrayList<BatchClassPluginConfig>();
				for (final BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
					newBatchClassPluginConfigsList.add(batchClassPluginConfig);
					batchClassPluginConfig.setId(BatchConstants.ZERO);
					batchClassPluginConfig.setBatchClassPlugin(null);

					final List<KVPageProcess> kvPageProcess = batchClassPluginConfig.getKvPageProcesses();
					final List<KVPageProcess> newKvPageProcessList = new ArrayList<KVPageProcess>();
					for (final KVPageProcess kVPageProcessChild : kvPageProcess) {
						kVPageProcessChild.setId(BatchConstants.ZERO);
						newKvPageProcessList.add(kVPageProcessChild);
						kVPageProcessChild.setBatchClassPluginConfig(null);
					}
					batchClassPluginConfig.setKvPageProcesses(newKvPageProcessList);
				}
				batchClassPlugin.setBatchClassPluginConfigs(newBatchClassPluginConfigsList);

				final List<BatchClassDynamicPluginConfig> batchClassDynamicPluginConfigs = batchClassPlugin
						.getBatchClassDynamicPluginConfigs();
				final List<BatchClassDynamicPluginConfig> newBatchClassDynamicPluginConfigsList = new ArrayList<BatchClassDynamicPluginConfig>();
				for (final BatchClassDynamicPluginConfig batchClassDynamicPluginConfig : batchClassDynamicPluginConfigs) {
					newBatchClassDynamicPluginConfigsList.add(batchClassDynamicPluginConfig);
					batchClassDynamicPluginConfig.setId(BatchConstants.ZERO);
					batchClassDynamicPluginConfig.setBatchClassPlugin(null);

					final List<BatchClassDynamicPluginConfig> children = batchClassDynamicPluginConfig.getChildren();
					final List<BatchClassDynamicPluginConfig> newChildrenList = new ArrayList<BatchClassDynamicPluginConfig>();
					for (final BatchClassDynamicPluginConfig child : children) {
						child.setId(BatchConstants.ZERO);
						newChildrenList.add(child);
						child.setBatchClassPlugin(null);
						child.setParent(null);
					}
					batchClassDynamicPluginConfig.setChildren(newChildrenList);
				}
				batchClassPlugin.setBatchClassDynamicPluginConfigs(newBatchClassDynamicPluginConfigsList);
			}
			detachedBatchClassModule.setBatchClassPlugins(newBatchClassPluginsList);

		}
		return detachedBatchClassModule;
	}

	/**
	 * API to get the upload batch folder path.
	 * 
	 * @return {@link String}
	 */
	@Override
	public String getUploadBatchFolder() {
		return getBaseFolderLocation() + File.separator + batchSchemaDao.getJAXB2Template().getUploadBatchFolder();
	}

	/**
	 * API to get the Web services folder name.
	 * 
	 * @return {@link String}
	 */
	@Override
	public String getWebServicesFolderPath() {
		return getBaseFolderLocation() + File.separator + batchSchemaDao.getJAXB2Template().getWebServicesFolderPath();
	}

	/**
	 * API to get advanced test extraction folder path.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param createDirectory {@link boolean}
	 * @return {@link String}
	 */
	@Override
	public String getTestAdvancedKvExtractionFolderPath(final String batchClassIdentifier, final boolean createDirectory) {
		return getAbsolutePath(batchClassIdentifier, batchSchemaDao.getJAXB2Template().getTestAdvancedKvExtractionFolderName(),
				createDirectory);
	}

	/**
	 * API to get advanced test extraction folder name.
	 * 
	 * @return {@link String}
	 */
	@Override
	public String getTestAdvancedKVExtractionFolderName() {
		return batchSchemaDao.getJAXB2Template().getTestAdvancedKvExtractionFolderName();
	}

	/**
	 * API to update the batch object to the file path specified.
	 * 
	 * @param batch {@link Batch}
	 * @param filePath {@link String}
	 */
	@Override
	public void update(final Batch batch, final String filePath) {
		if (null == batch) {
			LOGGER.info("batch class is null.");
		} else if (null == filePath || filePath.isEmpty()) {
			LOGGER.info("File path is either null or empty.");
		} else {
			this.batchSchemaDao.update(batch, filePath);
		}
	}

	/**
	 * API to get the HOCR Pages using the file path specified.
	 * 
	 * @param xmlFilePath {@link String}
	 * @return {@link HocrPages}
	 */
	@Override
	public HocrPages getHOCR(final String xmlFilePath) {
		HocrPages hocrPages = null;
		hocrPages = this.hocrSchemaDao.getObjectFromFilePath(xmlFilePath);
		return hocrPages;
	}

	/**
	 * API to get the advanced test table folder path.
	 * 
	 * @param batchClassId {@link String}
	 * @param createDir {@link boolean}
	 * @return String
	 */
	@Override
	public String getAdvancedTestTableFolderPath(final String batchClassId, final boolean createDir) {
		return getAbsolutePath(batchClassId, batchSchemaDao.getJAXB2Template().getAdvancedTestTableFolder(), createDir);
	}

	/**
	 * API to get advanced test table folder name.
	 * 
	 * @return {@link String}
	 */
	@Override
	public String getAdvancedTestTableFolderName() {
		return this.batchSchemaDao.getJAXB2Template().getAdvancedTestTableFolder();
	}

	/**
	 * DB Export plugin mapping folder name.
	 * 
	 * @return {@link String} DB Export plugin mapping folder name.
	 */
	@Override
	public String getDbExportMappingFolderName() {
		return batchSchemaDao.getJAXB2Template().getDbExportPluginMappingFolderName();
	}

	/**
	 * The <code>getUserType</code> method is used to retrieve user type from properties file.
	 * 
	 * @return {@link Integer} Ephesoft Cloud user type
	 */
	private Integer getUserType() {
		Integer userType = null;
		try {
			userType = Integer.parseInt(ApplicationConfigProperties.getApplicationConfigProperties().getProperty(USER_TYPE));
		} catch (NumberFormatException numberFormatException) {
			userType = UserType.OTHERS.getUserType();
			LOGGER.error("user type property is in wrong format in property file");
		} catch (IOException ioException) {
			userType = UserType.OTHERS.getUserType();
			LOGGER.error("user type property is missing from property file");
		}
		return userType;
	}

	/**
	 * The <code>getFileType</code> method is used to get the file type.
	 * 
	 * @param file {@link File} file
	 * @return {@link String} file type
	 */
	private String getFileType(File file) {
		String fileType = null;
		if (null != file) {
			String fileName = file.getName().toLowerCase(Locale.getDefault());
			if (fileName.endsWith(FileType.PDF.getExtensionWithDot())) {
				fileType = FileType.PDF.getExtension();
			} else if (fileName.endsWith(FileType.TIF.getExtensionWithDot())) {
				fileType = FileType.TIF.getExtension();
			} else if (fileName.endsWith(FileType.TIFF.getExtensionWithDot())) {
				fileType = FileType.TIFF.getExtension();
			}
		}
		return fileType;
	}

	/**
	 * The <code>discardOutOfLimitFile</code> method is used to discard file after limit is crossed.
	 * 
	 * @param scannedImagesDirectory {@link File} email download directory file
	 * @param batchClassID {@link String} batch class identifier
	 * @param userType {@link Integer} Ephesoft Cloud user type
	 */
	private void discardOutOfLimitFile(File scannedImagesDirectory, String batchClassID, Integer userType) {
		if (userType.intValue() == UserType.LIMITED.getUserType() && null != batchClassID && null != scannedImagesDirectory
				&& scannedImagesDirectory.exists()) {
			BatchClassCloudConfig batchClassCloudConfig = batchClassCloudService
					.getBatchClassCloudConfigByBatchClassIdentifier(batchClassID);
			Integer pageLimit = null;
			if (null != batchClassCloudConfig) {
				pageLimit = batchClassCloudConfig.getPageCount();
			}
			if (null != pageLimit) {
				int currentCounter = BatchConstants.ZERO;
				for (File file : scannedImagesDirectory.listFiles()) {
					String fileType = getFileType(file);
					if (null != fileType && currentCounter < pageLimit) {
						String filePath = file.getAbsolutePath();
						int pageCount = fileType.equals(FileType.PDF.getExtension()) ? PDFUtil.getPDFPageCount(filePath) : TIFFUtil
								.getTIFFPageCount(filePath);
						if ((currentCounter + pageCount) > pageLimit) {
							int selectPage = pageLimit - currentCounter;
							currentCounter = pageLimit;
							if (fileType.equals(FileType.PDF.getExtension())) {
								try {
									PDFUtil.getSelectedPdfFile(file, selectPage);
								} catch (IOException e) {
									LOGGER.error("Error occured in reading file: " + filePath);
								} catch (DocumentException de) {
									LOGGER.error("Error occurred in creating within limit file");
								}
							} else {
								try {
									TIFFUtil.getSelectedTiffFile(file, selectPage);
								} catch (IOException e) {
									LOGGER.error("Error occured in reading file: " + filePath);
								}
							}
						} else {
							currentCounter += pageCount;
						}
					} else {
						if (null != fileType) {
							file.delete();
						}
					}

				}
			}
		}
	}
}
