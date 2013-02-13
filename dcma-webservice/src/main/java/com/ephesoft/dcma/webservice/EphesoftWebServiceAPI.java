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

package com.ephesoft.dcma.webservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.ephesoft.dcma.barcode.BarcodeProperties;
import com.ephesoft.dcma.barcode.service.BarcodeService;
import com.ephesoft.dcma.barcodeextraction.service.BarcodeExtractionService;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPlugin;
import com.ephesoft.dcma.batch.dao.xml.BatchSchemaDao;
import com.ephesoft.dcma.batch.schema.BatchClasses;
import com.ephesoft.dcma.batch.schema.BatchInstances;
import com.ephesoft.dcma.batch.schema.Direction;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Documents;
import com.ephesoft.dcma.batch.schema.ExtractKVParams;
import com.ephesoft.dcma.batch.schema.HocrPages;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions;
import com.ephesoft.dcma.batch.schema.KVExtractionFieldPatterns;
import com.ephesoft.dcma.batch.schema.Modules;
import com.ephesoft.dcma.batch.schema.ObjectFactory;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.ReportingOptions;
import com.ephesoft.dcma.batch.schema.WebServiceParams;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.schema.ExtractKVParams.Params;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.schema.KVExtractionFieldPatterns.KVExtractionFieldPattern;
import com.ephesoft.dcma.batch.schema.Modules.Module;
import com.ephesoft.dcma.batch.schema.WebServiceParams.Params.Param;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.ImportBatchService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.EphesoftProperty;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.KVFetchValue;
import com.ephesoft.dcma.core.common.LocationType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.service.ZipService;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.core.threadpool.ThreadPool;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.KVExtraction;
import com.ephesoft.dcma.da.service.BatchClassGroupsService;
import com.ephesoft.dcma.da.service.BatchClassModuleService;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceGroupsService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.da.service.FieldTypeService;
import com.ephesoft.dcma.docassembler.DocumentAssembler;
import com.ephesoft.dcma.docassembler.DocumentAssemblerProperties;
import com.ephesoft.dcma.docassembler.constant.DocumentAssemblerConstants;
import com.ephesoft.dcma.docassembler.factory.DocumentClassificationFactory;
import com.ephesoft.dcma.fuzzydb.FuzzyDBProperties;
import com.ephesoft.dcma.fuzzydb.service.FuzzyDBSearchService;
import com.ephesoft.dcma.imagemagick.ImageMagicProperties;
import com.ephesoft.dcma.imagemagick.constant.ImageMagicKConstants;
import com.ephesoft.dcma.imagemagick.service.ImageProcessService;
import com.ephesoft.dcma.kvextraction.service.KVExtractionService;
import com.ephesoft.dcma.kvfieldcreation.service.KVFieldCreatorService;
import com.ephesoft.dcma.lucene.LuceneProperties;
import com.ephesoft.dcma.lucene.service.SearchClassificationService;
import com.ephesoft.dcma.performance.reporting.service.ReportDataService;
import com.ephesoft.dcma.recostar.service.RecostarService;
import com.ephesoft.dcma.recostarextraction.service.RecostarExtractionService;
import com.ephesoft.dcma.regex.service.ExtractionService;
import com.ephesoft.dcma.tesseract.service.TesseractService;
import com.ephesoft.dcma.user.service.UserConnectivityService;
import com.ephesoft.dcma.util.CustomFileFilter;
import com.ephesoft.dcma.util.FieldExtractionTechnique;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.TIFFUtil;
import com.ephesoft.dcma.util.WebServiceUtil;
import com.ephesoft.dcma.util.XMLUtil;
import com.ephesoft.dcma.webservice.constants.WebserviceConstants;
import com.ephesoft.dcma.workflow.service.JbpmService;
import com.ephesoft.dcma.workflow.service.common.DeploymentService;
import com.ephesoft.dcma.workflow.service.common.WorkflowService;

/**
 * This Class provides the functionality of the Web services across the product. {@link EphesoftWebServiceAPI}.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.batch.service.BatchSchemaService
 */
@Controller
public class EphesoftWebServiceAPI {

	/**
	 * APPLICATION_ZIP String.
	 */
	private static final String APPLICATION_ZIP = "application/zip";

	/**
	 * IMPROPER_INPUT_ONLY_ONE_HTML_FILE_EXPECTED String.
	 */
	private static final String IMPROPER_INPUT_ONLY_ONE_HTML_FILE_EXPECTED = "Improper input to server. Expected only one html file. Returning without processing the results.";

	/**
	 * IMPROPER_XML_PARAMETER String.
	 */
	private static final String IMPROPER_XML_PARAMETER = "Improper input to server. Parameter XML is incorrect. Returning without processing the results.";

	/**
	 * ONLY_ONE_SINGLE_PAGE_TIFF_EXPECTED String.
	 */
	private static final String ONLY_ONE_SINGLE_PAGE_TIFF_EXPECTED = "Improper input to server. Expected only one single page tiff file. Returning without processing the results.";

	/**
	 * USER_NOT_AUTHORIZED_TO_VIEW_THE_BATCH_CLASS String.
	 */
	private static final String USER_NOT_AUTHORIZED_TO_VIEW_THE_BATCH_CLASS = "User is not authorized to view the batch class for given identifier:";

	/**
	 * ERROR_PROCESSING_REQUEST String.
	 */
	private static final String ERROR_PROCESSING_REQUEST = "Error in processing request. Detailed exception is ";

	/**
	 * ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT String.
	 */
	private static final String ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT = "Exception in sending the error code to client. Logged the exception for debugging:";

	/**
	 * NEXT_LINE_STRING String.
	 */
	private static final String NEXT_LINE_STRING = "\"\r\n";

	/**
	 * SERVER_ERROR_MSG String.
	 */
	private static final String SERVER_ERROR_MSG = "Error response at server:";

	/**
	 * ERROR_IN_MAPPING_INPUT String.
	 */
	private static final String ERROR_IN_MAPPING_INPUT = "Error in mapping input XML in the desired format. Please send it in the specified format. Detailed exception is ";

	/**
	 * INTERNAL_SERVER_ERROR String.
	 */
	private static final String INTERNAL_SERVER_ERROR = "Internal Server error.Please check logs for further details.";

	/**
	 * IMPROPER_INPUT_TO_SERVER String.
	 */
	private static final String IMPROPER_INPUT_TO_SERVER = "Improper input to server. Expected multipart request. Returning without processing the results.";

	/**
	 * CONSTANT_DOT String.
	 */
	private static final String CONSTANT_DOT = ".";

	/**
	 * RECOSTAR_EXTRACTION String.
	 */
	private static final String RECOSTAR_EXTRACTION = "recostar-extraction";

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(EphesoftWebServiceAPI.class);
	
	/**
	 * Constant for batchClassIdentifier.
	 */
	private static final String BATCH_CLASS_IDENTIFIER = "batchClassIdentifier";

	/**
	 * imService {@link ImageProcessService}.
	 */
	@Autowired
	private ImageProcessService imService;

	/**
	 * barcodeExtractionService {@link BarcodeExtractionService}.
	 */
	@Autowired
	private BarcodeExtractionService barcodeExtractionService;

	/**
	 * userConnectivityService {@link UserConnectivityService}.
	 */
	@Autowired
	private UserConnectivityService userConnectivityService;

	/**
	 * kvFieldService {@link KVFieldCreatorService}.
	 */
	@Autowired
	private KVFieldCreatorService kvFieldService;

	/**
	 * scService {@link SearchClassificationService}.
	 */
	@Autowired
	private SearchClassificationService scService;

	/**
	 * barcodeService {@link BarcodeService}.
	 */
	@Autowired
	private BarcodeService barcodeService;

	/**
	 * bsService {@link BatchSchemaService}.
	 */
	@Autowired
	private BatchSchemaService bsService;

	/**
	 * docAssembler {@link DocumentAssembler}.
	 */
	@Autowired
	private DocumentAssembler docAssembler;

	/**
	 * batchClassPPService {@link PluginPropertiesService}.
	 */
	@Autowired
	@Qualifier("batchClassPluginPropertiesService")
	private PluginPropertiesService batchClassPPService;

	/**
	 * reportingService {@link ReportDataService}.
	 */
	@Autowired
	private ReportDataService reportingService;

	/**
	 * bcService {@link BatchClassService}.
	 */
	@Autowired
	private BatchClassService bcService;

	/**
	 * batchSchemaDao {@link BatchSchemaDao}.
	 */
	@Autowired
	private BatchSchemaDao batchSchemaDao;

	/**
	 * importBatchService {@link ImportBatchService}.
	 */
	@Autowired
	private ImportBatchService importBatchService;

	/**
	 * kvService {@link KVExtractionService}.
	 */
	@Autowired
	private KVExtractionService kvService;

	/**
	 * deploymentService {@link DeploymentService}.
	 */
	@Autowired
	private DeploymentService deploymentService;

	/**
	 * batchClassGroupsService {@link BatchClassGroupsService}.
	 */
	@Autowired
	private BatchClassGroupsService batchClassGroupsService;

	/**
	 * biService {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService biService;

	/**
	 * jbpmService {@link JbpmService}.
	 */
	@Autowired
	private JbpmService jbpmService;

	/**
	 * workflowService {@link WorkflowService}.
	 */
	@Autowired
	private WorkflowService workflowService;

	/**
	 * bcModuleService {@link BatchClassModuleService}.
	 */
	@Autowired
	private BatchClassModuleService bcModuleService;

	/**
	 * batchInstanceGroupsService {@link BatchInstanceGroupsService}.
	 */
	@Autowired
	private BatchInstanceGroupsService batchInstanceGroupsService;

	/**
	 * recostarService {@link RecostarService}.
	 */
	@Autowired
	private RecostarService recostarService;

	/**
	 * tesseractService {@link TesseractService}.
	 */
	@Autowired
	private TesseractService tesseractService;

	/**
	 * recostarExtractionService {@link RecostarExtractionService}.
	 */
	@Autowired
	private RecostarExtractionService recostarExtractionService;

	/**
	 * fuzzyDBSearchService {@link FuzzyDBSearchService}.
	 */
	@Autowired
	private FuzzyDBSearchService fuzzyDBSearchService;

	/**
	 * fieldTypeService {@link FieldTypeService}.
	 */
	@Autowired
	private FieldTypeService fieldTypeService;

	/**
	 * extractionService {@link ExtractionService}.
	 */
	@Autowired
	private ExtractionService extractionService;

	/**
	 * Instance of PluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * zipService {@link ZipService}.
	 */
	@Autowired
	private ZipService zipService;

	/**
	 * PG_IDENTIFIER String.
	 */
	public static final String PG_IDENTIFIER = "PG";

	/**
	 * To split Multi page File.
	 * @param req {@link HttpServletRequest}
	 * @param resp {@link HttpServletResponse}
	 * 
	 */
	@RequestMapping(value = "/splitMultipageFile", method = RequestMethod.POST)
	@ResponseBody
	public void splitMultipageFile(final HttpServletRequest req, final HttpServletResponse resp) {
		String respStr = WebServiceUtil.EMPTY_STRING;
		String workingDir = WebServiceUtil.EMPTY_STRING;

		InputStream instream = null;
		OutputStream outStream = null;
		try {
			if (req instanceof DefaultMultipartHttpServletRequest) {
				LOGGER.info("Start spliting multipage file");
				final String webServiceFolderPath = bsService.getWebServicesFolderPath();
				LOGGER.info("Web Service Folder Path:" + webServiceFolderPath);
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
				LOGGER.info("web service workingDir:" + workingDir);
				final String outputDir = WebServiceUtil.createWebServiceOutputDir(workingDir);
				LOGGER.info("web service outputDir:" + outputDir);
				final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) req;

				final BatchInstanceThread threadList = new BatchInstanceThread(new File(workingDir).getName() + Math.random());
				String inputParams = WebServiceUtil.EMPTY_STRING;
				String outputParams = WebServiceUtil.EMPTY_STRING;
				boolean isGSTool = false;
				for (final Enumeration<String> params = multiPartRequest.getParameterNames(); params.hasMoreElements();) {
					final String paramName = params.nextElement();
					if (paramName.equalsIgnoreCase(WebServiceUtil.IS_GHOSTSCRIPT)) {
						isGSTool = Boolean.parseBoolean(multiPartRequest.getParameter(paramName));
						LOGGER.info("Value for isGhostscript parameter is " + isGSTool);
						continue;
					}

					if (paramName.equalsIgnoreCase(WebServiceUtil.INPUT_PARAMS)) {
						inputParams = multiPartRequest.getParameter(paramName);
						LOGGER.info("Value for inputParams parameter is " + inputParams);
						continue;
					}

					if (paramName.equalsIgnoreCase(WebServiceUtil.OUTPUT_PARAMS)) {
						outputParams = multiPartRequest.getParameter(paramName);
						LOGGER.info("Value for outputParams parameter is " + outputParams);
						continue;
					}
				}
				final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();
				// perform validation on input fields
				String results = WebServiceUtil.validateSplitAPI(fileMap, isGSTool, outputParams, inputParams);
				if (!results.isEmpty()) {
					respStr = results;
				} else {
					LOGGER.info("List of input file names:");
					for (final String fileName : fileMap.keySet()) {
						LOGGER.info(fileName + WebserviceConstants.COMMA);

					}
					for (final String fileName : fileMap.keySet()) {
						if (fileName.toLowerCase(Locale.getDefault()).indexOf(FileType.PDF.getExtension()) > -WebserviceConstants.ONE
								|| fileName.toLowerCase(Locale.getDefault()).indexOf(FileType.TIF.getExtension()) > -WebserviceConstants.ONE
								|| fileName.toLowerCase(Locale.getDefault()).indexOf(FileType.TIFF.getExtension()) > -WebserviceConstants.ONE) {
							// only tiffs and RSP file is expected
							if (isGSTool
									&& (fileName.toLowerCase(Locale.getDefault()).indexOf(FileType.TIF.getExtension()) > -WebserviceConstants.ONE || fileName
											.toLowerCase(Locale.getDefault()).indexOf(FileType.TIFF.getExtension()) > -WebserviceConstants.ONE)) {
								respStr = "Only PDF files expected with GhostScript tool.";
								LOGGER.error(SERVER_ERROR_MSG + respStr);
								break;
							}
							try {
								final MultipartFile multiPartFile = multiPartRequest.getFile(fileName);
								instream = multiPartFile.getInputStream();
								final File file = new File(workingDir + File.separator + fileName);
								outStream = new FileOutputStream(file);
								final byte[] buf = new byte[WebServiceUtil.bufferSize];
								int len = instream.read(buf);
								while (len > WebserviceConstants.ZERO) {
									outStream.write(buf, WebserviceConstants.ZERO, len);
									len = instream.read(buf);
								}
							} finally {
								IOUtils.closeQuietly(instream);
								IOUtils.closeQuietly(outStream);

							}
						} else {
							respStr = "Files other than tiff, tif and pdf formats are provided.";
							LOGGER.error(SERVER_ERROR_MSG + respStr);
							break;
						}
					}
					if (respStr.isEmpty()) {
						respStr = performSplitAPIInternal(resp, workingDir, outputDir, threadList, inputParams, outputParams,
								isGSTool, fileMap);
					}
				}
			} else {
				respStr = IMPROPER_INPUT_TO_SERVER;
			}
		} catch (Exception e) {
			respStr = INTERNAL_SERVER_ERROR + e;
			LOGGER.error(SERVER_ERROR_MSG + respStr);
		}
		if (!workingDir.isEmpty()) {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final IOException ioe) {
				LOGGER.info(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + ioe, ioe);
			}
		}
	}

	/**
	 * Method to perform split multipage file API after the validations.
	 * 
	 * @param resp {@link HttpServletResponse}
	 * @param workingDir {@link String}
	 * @param outputDir {@link String}
	 * @param threadList {@link BatchInstanceThread}
	 * @param inputParams {@link String}
	 * @param outputParams {@link String}
	 * @param isGSTool boolean
	 * @param fileMap MultiValueMap<String, MultipartFile>
	 * @throws DCMAException if error occurs
	 * @throws DCMAApplicationException if error occurs
	 * @throws IOException if error occurs
	 */ 
	private String performSplitAPIInternal(final HttpServletResponse resp, String workingDir, final String outputDir,
			final BatchInstanceThread threadList, String inputParams, String outputParams, boolean isGSTool,
			final MultiValueMap<String, MultipartFile> fileMap) throws DCMAException, DCMAApplicationException, IOException {
		String respStr = WebServiceUtil.EMPTY_STRING;
		for (final String fileName : fileMap.keySet()) {
			final File file = new File(workingDir + File.separator + fileName);
			if (isGSTool) {
				LOGGER.info("Start spliting multipage file using ghost script for file :" + fileName);
				imService.convertPdfToSinglePageTiffsUsingGSAPI(inputParams, file, outputParams, new File(outputDir + File.separator
						+ fileName), threadList);
			} else {
				LOGGER.info("Start spliting multipage file using image magick for file :" + fileName);
				imService.convertPdfOrMultiPageTiffToTiffUsingIM(inputParams, file, outputParams, new File(outputDir + File.separator
						+ fileName), threadList);
			}
		}
		try {
			LOGGER.info("Executing batch instance thread using thread pool");
			threadList.execute();
		} catch (final DCMAApplicationException dcmae) {
			threadList.remove();
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
			throw dcmae;
		}

		ServletOutputStream out = null;
		ZipOutputStream zout = null;
		final String zipFileName = WebServiceUtil.SERVEROUTPUTFOLDERNAME;
		resp.setContentType(WebServiceUtil.APPLICATION_X_ZIP);
		resp.setHeader(WebServiceUtil.CONTENT_DISPOSITION, WebServiceUtil.ATTACHMENT_FILENAME + zipFileName
				+ FileType.ZIP.getExtensionWithDot() + NEXT_LINE_STRING);
		try {
			out = resp.getOutputStream();
			zout = new ZipOutputStream(out);
			FileUtils.zipDirectory(outputDir, zout, zipFileName);
			LOGGER.info("zipFileName:" + zipFileName);
			LOGGER.info("outputDir:" + outputDir);
			resp.setStatus(HttpServletResponse.SC_OK);
		} catch (final IOException e) {
			respStr = "Unable to process web service request.Please check your ghostscipt or imagemagick configuration.";
			LOGGER.error(SERVER_ERROR_MSG + respStr);
		} finally {
			IOUtils.closeQuietly(zout);
			IOUtils.closeQuietly(out);
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
		}
		return respStr;
	}

	/**
	 * To export batch class.
	 * @param req {@link HttpServletRequest}
	 * @param resp {@link HttpServletResponse}
	 */
	@RequestMapping(value = "/exportBatchClass", method = RequestMethod.POST)
	@ResponseBody
	public void exportBatchClass(final HttpServletRequest req, final HttpServletResponse resp) {
		LOGGER.info("Inside exportBatchClass web service.");
		String results = WebServiceUtil.EMPTY_STRING;
		try {
			String identifier = WebServiceUtil.EMPTY_STRING;
			String imBaseFolderName = WebServiceUtil.EMPTY_STRING;
			String searchSampleName = WebServiceUtil.EMPTY_STRING;
			for (final Enumeration<String> params = req.getParameterNames(); params.hasMoreElements();) {
				final String paramName = params.nextElement();
				if (paramName.equalsIgnoreCase(WebServiceUtil.IDENTIFIER2)) {
					identifier = req.getParameter(paramName);
					LOGGER.info("Value for identifier parameter is " + identifier);
					continue;
				}

				if (paramName.equalsIgnoreCase(WebServiceUtil.IMAGE_CLASSIFICATION_SAMPLE)) {
					imBaseFolderName = req.getParameter(paramName);
					LOGGER.info("Value for imBaseFolderName parameter is " + imBaseFolderName);
					continue;
				}

				if (paramName.equalsIgnoreCase(WebServiceUtil.LUCENE_SEARCH_CLASSIFICATION_SAMPLE)) {
					searchSampleName = req.getParameter(paramName);
					LOGGER.info("Value for searchSampleName parameter is " + searchSampleName);
					continue;
				}
			}
			final BatchClass batchClass = bcService.getBatchClassByIdentifier(identifier);
			if (batchClass != null) {
				results = WebServiceUtil.validateExportBatchClassAPI(imBaseFolderName, identifier, searchSampleName);
				if (results.isEmpty()) {
					Set<String> loggedInUserRole = getUserRoles(req);
					if (loggedInUserRole == null || loggedInUserRole.isEmpty()) {
						results = "User not authorized to view this API.";
					} else {
						boolean isBatchClassViewableToUser = isBatchClassViewableToUser(identifier, loggedInUserRole,
								isSuperAdmin(req));
						if (isBatchClassViewableToUser) {
							req.getRequestDispatcher(WebServiceUtil.DCMA_GWT_ADMIN_EXPORT_BATCH_CLASS_DOWNLOAD).forward(req, resp);
							resp.setStatus(HttpServletResponse.SC_OK);
						} else {
							results = "User not authorized to view this batch class id:" + identifier;
						}
					}
				}
			} else {
				results = "Batch Class does not exist with batch class id:" + identifier;
				LOGGER.error(SERVER_ERROR_MSG + results);
			}
		} catch (final Exception e) {
			results = INTERNAL_SERVER_ERROR + e;
			LOGGER.error("Exception in exporting batch class :" + e.getMessage());
		}
		if (!results.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, results);
			} catch (final IOException ioe) {
				LOGGER.info(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + ioe, ioe);
			}
		}
	}

	/**
	 * To extract KV.
	 * @param req {@link HttpServletRequest}
	 * @param resp {@link HttpServletResponse}
	 */ 
	@RequestMapping(value = "/extractKV", method = RequestMethod.POST)
	@ResponseBody
	public void extractKV(final HttpServletRequest req, final HttpServletResponse resp) {
		processKVExtraction(req, resp);
	}

	private void processKVExtraction(final HttpServletRequest req, final HttpServletResponse resp) {
		String respStr = WebServiceUtil.EMPTY_STRING;
		LOGGER.info("Processing key value extraction using web service.");
		String workingDir = WebServiceUtil.EMPTY_STRING;

		if (req instanceof DefaultMultipartHttpServletRequest) {
			InputStream instream = null;
			OutputStream outStream = null;
			final String webServiceFolderPath = bsService.getWebServicesFolderPath();
			LOGGER.info("Web Service Folder Path:" + webServiceFolderPath);
			final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) req;
			final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();

			if (fileMap.size() == 2) {
				try {
					workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
					LOGGER.info("Created web service working directory:" + workingDir + "successfully");
					ExtractKVParams params = null;
					String filePath = WebServiceUtil.EMPTY_STRING;
					for (final String fileName : fileMap.keySet()) {
						try {
							final MultipartFile multipartFile = multiPartRequest.getFile(fileName);
							instream = multipartFile.getInputStream();
							if (fileName.toLowerCase(Locale.getDefault()).indexOf(FileType.XML.getExtension().toLowerCase()) > -1) {
								final Source source = XMLUtil.createSourceFromStream(instream);
								params = (ExtractKVParams) batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().unmarshal(source);
								continue;
							} else if (fileName.toLowerCase(Locale.getDefault()).indexOf(FileType.HTML.getExtension().toLowerCase()) > -1) {
								filePath = workingDir + File.separator + fileName;
								LOGGER.info("HTML file for processing is " + filePath);
								final File file = new File(filePath);
								outStream = new FileOutputStream(file);
								final byte[] buf = new byte[WebServiceUtil.bufferSize];
								int len = instream.read(buf);
								while (len > 0) {
									outStream.write(buf, 0, len);
									len = instream.read(buf);
								}
								continue;
							}
						} finally {
							IOUtils.closeQuietly(instream);
							IOUtils.closeQuietly(outStream);
						}
					}

					respStr = validateExtractKV(respStr, params, filePath);
					if (respStr.isEmpty()) {
						respStr = performExtractKVInternal(resp, respStr, workingDir, params, filePath);
					}

				} catch (final XmlMappingException xmle) {
					respStr = "Error in mapping input XML or the hocr file in the desired format. Please send it in the specified format. Detailed exception is "
							+ xmle;
					LOGGER.error(SERVER_ERROR_MSG + respStr);
				} catch (final DCMAException dcmae) {
					respStr = ERROR_PROCESSING_REQUEST + dcmae;
					LOGGER.error(SERVER_ERROR_MSG + respStr);
				} catch (final Exception e) {
					respStr = "Internal Server error.Please check logs for further details." + e;
					LOGGER.error(SERVER_ERROR_MSG + respStr);
				} finally {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			} else {
				respStr = "Improper input to server. Expected two files: hocr and xml parameter file. Returning without processing the results.";
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			}
		} else {
			respStr = IMPROPER_INPUT_TO_SERVER;
			LOGGER.error(SERVER_ERROR_MSG + respStr);
		}

		if (!workingDir.isEmpty()) {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final IOException ioe) {
				LOGGER.info(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + ioe, ioe);
			}
		}
	}


	private String performExtractKVInternal(final HttpServletResponse resp, final String respStr, final String workingDir,
			final ExtractKVParams params, final String filePath) throws FileNotFoundException, IOException, DCMAException {
		// extract the hocr content from hocr files
		// generate hocr from html file
		String respStrLocal = respStr;
		HocrPages hocrPages = new HocrPages();
		List<HocrPage> hocrPageList = hocrPages.getHocrPage();
		HocrPage hocrPage = new HocrPage();
		String pageID = WebServiceUtil.PG0;
		hocrPage.setPageID(pageID);
		hocrPageList.add(hocrPage);
		try {
			bsService.hocrGenerationAPI(workingDir, WebServiceUtil.PG0, filePath, hocrPage);
			LOGGER.info("Successfully generated hocr from html file.");
			final List<DocField> updtDocList = new ArrayList<DocField>();
			final boolean isSuccess = kvService.extractKVDocumentFieldsFromHOCR(updtDocList, hocrPages, params);
			if (!isSuccess) {
				respStrLocal = INTERNAL_SERVER_ERROR;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} else {
				LOGGER.info("Generating document level fields for the output result");

				final DocumentLevelFields dlfs = new DocumentLevelFields();
				dlfs.getDocumentLevelField().addAll(updtDocList);
				Documents docs = new Documents();
				Document doc = new Document();
				docs.getDocument().add(doc);
				doc.setDocumentLevelFields(dlfs);

				StreamResult result;
				try {
					result = new StreamResult(resp.getOutputStream());
					resp.setStatus(HttpServletResponse.SC_OK);
					batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().marshal(docs, result);
				} catch (final IOException e) {
					respStrLocal = INTERNAL_SERVER_ERROR + e.getMessage();
					LOGGER.error(SERVER_ERROR_MSG + respStrLocal);
				}
			}
		} catch (Exception exception) {
			LOGGER.error("Error occurred while kv extraction..", exception);
			throw new DCMAException("Error occurred while kv extraction..", exception);
		}
		return respStrLocal;
	}

	/**
	 * Method to validate the Extract KV parameters.
	 * 
	 * @param respStr {@link String}
	 * @param params {@link ExtractKVParams}
	 * @param filePath {@link String}
	 * @return {@link String}
	 */
	private String validateExtractKV(String respStr, ExtractKVParams params, String filePath) {
		String respStrLocal = respStr;
		if (params != null && params.getParams().size() > 0 && !filePath.isEmpty()) {
			Params paramList = params.getParams().get(0);
			if (paramList.getLocationType() == null || paramList.getLocationType().isEmpty()) {
				respStrLocal = "Please provide the location type. Accepted values are: TOP, RIGHT, LEFT, BOTTOM, TOP_RIGHT, TOP_LEFT, BOTTOM_LEFT, BOTTOM_RIGHT.";
				LOGGER.error(SERVER_ERROR_MSG + respStrLocal);
			}
			if (respStrLocal.isEmpty() && !LocationType.valuesAsStringList().contains(paramList.getLocationType())) {
				respStrLocal = "Please provide the location type. Accepted values are: TOP, RIGHT, LEFT, BOTTOM, TOP_RIGHT, TOP_LEFT, BOTTOM_LEFT, BOTTOM_RIGHT.";
				LOGGER.error(SERVER_ERROR_MSG + respStrLocal);
			}
			if (respStrLocal.isEmpty()
					&& (paramList.getKeyPattern() == null || paramList.getKeyPattern().isEmpty()
							|| paramList.getValuePattern() == null || paramList.getValuePattern().isEmpty())) {
				respStrLocal = "Please provide the key and value patterns.";
				LOGGER.error(SERVER_ERROR_MSG + respStrLocal);
			}
			if (paramList.isAdvancedKV()) {
				if (respStrLocal.isEmpty() && (paramList.getMultiplier() > 1 || paramList.getMultiplier() <= 0)) {
					respStrLocal = "Please provide the multiplier for Advanced KV extraction. Range of values is between 0 to 1.";
					LOGGER.error(SERVER_ERROR_MSG + respStrLocal);
				}
				if (respStrLocal.isEmpty() && (paramList.getKVFetchValue() == null || paramList.getKVFetchValue().isEmpty())) {
					respStrLocal = "Please provide the KVFetchValue for Advanced KV extraction. Expected values are:ALL, FIRST, LAST";
					LOGGER.error(SERVER_ERROR_MSG + respStrLocal);
				}
				if (respStrLocal.isEmpty() && !(KVFetchValue.valuesAsStringList().contains(paramList.getKVFetchValue()))) {
					respStrLocal = "Please provide the KVFetchValue for Advanced KV extraction. Expected values are:ALL, FIRST, LAST";
					LOGGER.error(SERVER_ERROR_MSG + respStrLocal);
				}
				if (respStrLocal.isEmpty() && paramList.getLength() <= 0) {
					respStrLocal = "Please provide the length value greater than zero with advanced KV extraction.";
					LOGGER.error(SERVER_ERROR_MSG + respStrLocal);
				}
				if (respStrLocal.isEmpty() && paramList.getWidth() <= 0) {
					respStrLocal = "Please provide the width value greater than zero with advanced KV extraction.";
					LOGGER.error(SERVER_ERROR_MSG + respStrLocal);
				}
			} else {
				if (respStrLocal.isEmpty() && paramList.getNoOfWords() < 0) {
					respStrLocal = "Please provide positive value for no of words with advanced KV extraction.";
					LOGGER.error(SERVER_ERROR_MSG + respStrLocal);
				}
			}
		} else {
			respStrLocal = "Please send an hocr file as input. Improper input to the server.Proceeding without processing";
			LOGGER.error(SERVER_ERROR_MSG + respStrLocal);
		}
		return respStrLocal;
	}

	/**
	 * To import Batch Class.
	 * @param req {@link HttpServletRequest}
	 * @param resp {@link HttpServletResponse}
	 */
	@RequestMapping(value = "/importBatchClass", method = RequestMethod.POST)
	@ResponseBody
	public void importBatchClass(final HttpServletRequest req, final HttpServletResponse resp) {
		String respStr = WebServiceUtil.EMPTY_STRING;
		LOGGER.info("Start processing import batch class web service");
		String workingDir = WebServiceUtil.EMPTY_STRING;
		if (req instanceof DefaultMultipartHttpServletRequest) {
			InputStream instream = null;
			OutputStream outStream = null;
			final String webServiceFolderPath = bsService.getWebServicesFolderPath();
			LOGGER.info("web Service Folder Path" + webServiceFolderPath);
			final DefaultMultipartHttpServletRequest mPartReq = (DefaultMultipartHttpServletRequest) req;
			final MultiValueMap<String, MultipartFile> fileMap = mPartReq.getMultiFileMap();

			if (fileMap.size() == 2) {
				try {
					workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
					LOGGER.info("Created the web service working directory successfully  :" + workingDir);
					ImportBatchClassOptions option = null;
					String zipFilePath = WebServiceUtil.EMPTY_STRING;
					for (final String fileName : fileMap.keySet()) {
						try {
							final MultipartFile multiPartFile = mPartReq.getFile(fileName);
							instream = multiPartFile.getInputStream();
							if (fileName.toLowerCase(Locale.getDefault()).indexOf(FileType.XML.getExtension().toLowerCase()) > -1) {
								final Source source = XMLUtil.createSourceFromStream(instream);
								option = (ImportBatchClassOptions) batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().unmarshal(
										source);
								continue;
							} else if (fileName.toLowerCase(Locale.getDefault()).indexOf(FileType.ZIP.getExtension().toLowerCase()) > -1) {
								zipFilePath = workingDir + File.separator + fileName;
								LOGGER.info("Zip file is using for importing batch class is " + zipFilePath);
								final File file = new File(zipFilePath);
								outStream = new FileOutputStream(file);
								final byte[] buf = new byte[WebServiceUtil.bufferSize];
								int len = instream.read(buf);
								while (len > 0) {
									outStream.write(buf, 0, len);
									len = instream.read(buf);
								}
								continue;
							}
						} finally {
							IOUtils.closeQuietly(instream);
							IOUtils.closeQuietly(outStream);
						}
					}

					respStr = importBatchClassInternal(resp, respStr, workingDir, option, zipFilePath);

				} catch (final XmlMappingException xmle) {
					respStr = ERROR_IN_MAPPING_INPUT + xmle;
				} catch (final Exception e) {
					respStr = INTERNAL_SERVER_ERROR + e.getMessage();
				} finally {

					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			} else {
				respStr = "Improper input to server. Expected two files: zip and xml file. Returning without processing the results.";
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			}
		} else {
			respStr = IMPROPER_INPUT_TO_SERVER;
			LOGGER.error(SERVER_ERROR_MSG + respStr);
		}
		if (!workingDir.isEmpty()) {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final IOException ioe) {
				LOGGER.error("Error while sending error reponse.");
			}
		}
	}

	private String importBatchClassInternal(final HttpServletResponse resp, String respStr, String workingDir,
			ImportBatchClassOptions option, String zipFilePath) throws IOException {
		String respStrLocal = respStr;
		if (option != null && !zipFilePath.isEmpty()) {
			final Map<Boolean, String> results = importBatchService.validateInputXML(option);
			final String errorMessg = results.get(Boolean.FALSE);
			if (errorMessg != null && !errorMessg.isEmpty()) {
				respStrLocal = errorMessg;
			} else {
				LOGGER.info("zip file path:" + zipFilePath);
				final File tempZipFile = new File(zipFilePath);
				final String tempOutputUnZipDir = tempZipFile.getParent() + File.separator
						+ tempZipFile.getName().substring(0, tempZipFile.getName().indexOf(WebServiceUtil.DOT));
				LOGGER.info("temporary Output UnZip Directory:" + tempOutputUnZipDir);
				try {
					FileUtils.unzip(tempZipFile, tempOutputUnZipDir);
				} catch (final Exception e) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
					tempZipFile.delete();
					resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Unable to unzip file. Returning without processing the results.");
					LOGGER.error("Unable to unzip file. Returning without processing the results.");
				}

				option.setZipFilePath(tempOutputUnZipDir);
				LOGGER.info("Importing batch class");
				final boolean isDeployed = deploymentService.isDeployed(option.getName());
				final Map<Boolean, String> resultsImport = importBatchService.importBatchClass(option, isDeployed, true, null);
				final String errorMessgImport = resultsImport.get(Boolean.FALSE);
				if (errorMessgImport != null && !errorMessgImport.isEmpty()) {
					respStrLocal = errorMessgImport;
				} else {
					final String batchClassId = resultsImport.get(Boolean.TRUE);
					BatchClass batchClass = bcService.getBatchClassByIdentifier(batchClassId);
					renameBatchClassModules(batchClass);
					bcService.evict(batchClass);
					bcService.merge(batchClass);
					deploymentService.createAndDeployBatchClassJpdl(batchClass);
					LOGGER.info("Batch class with identifier:" + batchClass.getIdentifier() + " imported successfully.");
				}
			}
		} else {
			respStrLocal = "Improper input to the server.Proceeding without processing";
			LOGGER.error(SERVER_ERROR_MSG + respStrLocal);
		}
		return respStrLocal;
	}

	private void renameBatchClassModules(BatchClass batchClass) {
		String existingBatchClassIdentifier = batchClass.getIdentifier();
		for (BatchClassModule batchClassModule : batchClass.getBatchClassModules()) {
			String existingModuleName = batchClassModule.getModule().getName();
			StringBuffer newWorkflowNameStringBuffer = new StringBuffer();
			newWorkflowNameStringBuffer.append(existingModuleName.replaceAll(" ", "_"));
			newWorkflowNameStringBuffer.append('_');
			newWorkflowNameStringBuffer.append(existingBatchClassIdentifier);
			batchClassModule.setWorkflowName(newWorkflowNameStringBuffer.toString());
		}
	}

	/**
	 * To run Reporting.
	 * @param req {@link HttpServletRequest}
	 * @param resp {@link HttpServletResponse}
	 */
	@RequestMapping(value = "/runReporting", method = RequestMethod.POST)
	@ResponseBody
	public void runReporting(final HttpServletRequest req, final HttpServletResponse resp) {
		LOGGER.info("Start processing the run reporting web service");
		String respStr = WebServiceUtil.EMPTY_STRING;
		try {
			if (req instanceof DefaultMultipartHttpServletRequest) {

				InputStream instream = null;
				final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) req;
				final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();
				for (final String fileName : fileMap.keySet()) {
					final MultipartFile multiPartFile = multiPartRequest.getFile(fileName);
					instream = multiPartFile.getInputStream();
					final Source source = XMLUtil.createSourceFromStream(instream);
					final ReportingOptions option = (ReportingOptions) batchSchemaDao.getJAXB2Template().getJaxb2Marshaller()
							.unmarshal(source);
					final String installerPath = option.getInstallerPath();
					if (installerPath == null || installerPath.isEmpty()
							|| !installerPath.toLowerCase(Locale.getDefault()).contains(WebServiceUtil.BUILD_XML)) {
						respStr = "Improper input to server. Installer path not specified or it does not contain the build.xml path.";
					} else {
						LOGGER.info("synchronizing the database");
						reportingService.syncDatabase(installerPath);
						break;
					}
				}

			} else {
				respStr = IMPROPER_INPUT_TO_SERVER;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			}
		} catch (final XmlMappingException xmle) {
			respStr = ERROR_IN_MAPPING_INPUT + xmle;
			LOGGER.error(SERVER_ERROR_MSG + respStr);
		} catch (final Exception e) {
			respStr = INTERNAL_SERVER_ERROR + e;
			LOGGER.error(SERVER_ERROR_MSG + respStr);
		}

		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final IOException ioe) {
				LOGGER.info(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + ioe, ioe);
			}
		}
	}

	/**
	 * To get Batch Instance List.
	 * @param status {@link String}
	 * @param resp {@link HttpServletResponse}
	 * @param req {@link HttpServletRequest}
	 */
	@RequestMapping(value = "/getBatchInstanceList/{status}", method = RequestMethod.GET)
	@ResponseBody
	public void getBatchInstanceList(@PathVariable("status") final String status, final HttpServletResponse resp,
			final HttpServletRequest req) {
		LOGGER.info("Start processing get batch instance list from batch status web service");
		String respStr = WebServiceUtil.EMPTY_STRING;
		Set<String> batchInstancesId = new TreeSet<String>();
		if (!BatchInstanceStatus.valuesAsStringList().contains(status)) {
			respStr = "Invalid value for batch instance status. Please try again.";
		} else {
			final BatchInstanceStatus batchInstanceStatus = BatchInstanceStatus.valueOf(status);
			LOGGER.info("Batch instance status is " + status);
			LOGGER.info("Fetching batch instance list from the database");
			final List<BatchInstance> batchInstance = biService.getBatchInstByStatus(batchInstanceStatus);
			if (batchInstance != null && !batchInstance.isEmpty()) {
				Set<String> loggedInUserRole = getUserRoles(req);
				if (!isSuperAdmin(req)) {
					// fetch the batch instances from batch instance groups
					Set<String> batchInstancesIdentifiers = batchInstanceGroupsService
							.getBatchInstanceIdentifierForUserRoles(loggedInUserRole);

					if (batchInstancesIdentifiers != null) {
						batchInstancesId.addAll(batchInstancesIdentifiers);
					}

					// fetch the list of batch instances from the batch instance table for batch classes having the given role.
					List<BatchClass> batchClasses = bcService.getAllBatchClassesByUserRoles(loggedInUserRole);
					for (BatchClass batchClass : batchClasses) {
						List<BatchInstance> eachBatchInstance = biService.getBatchInstByBatchClass(batchClass);
						for (BatchInstance bi : eachBatchInstance) {
							batchInstancesId.add(bi.getIdentifier());
						}
					}
				} else {
					for (BatchInstance bi : batchInstance) {
						batchInstancesId.add(bi.getIdentifier());
					}
				}

				LOGGER.info("Fetched list of batch instances from the batch instance table for batch classes having the given role:");
				for (String batchInstanceIdentifier : batchInstancesId) {
					LOGGER.info(batchInstanceIdentifier);
				}

				final BatchInstances batchInstances = new BatchInstances();

				final List<com.ephesoft.dcma.batch.schema.BatchInstances.BatchInstance> batchInstanceList = batchInstances
						.getBatchInstance();

				for (final BatchInstance eachBatchInstance : batchInstance) {
					if (batchInstancesId.contains(eachBatchInstance.getIdentifier())) {
						final com.ephesoft.dcma.batch.schema.BatchInstances.BatchInstance batchLocal = new com.ephesoft.dcma.batch.schema.BatchInstances.BatchInstance();
						batchLocal.setIdentifier(eachBatchInstance.getIdentifier());
						batchLocal.setBatchName(eachBatchInstance.getBatchName());
						batchLocal.setCurrentUser(eachBatchInstance.getCurrentUser());
						batchLocal.setExecutedModules(eachBatchInstance.getExecutedModules());
						batchLocal.setLocalFolder(eachBatchInstance.getLocalFolder());
						batchLocal.setRemoteBatchInstanceId(eachBatchInstance.getRemoteBatchInstance() != null ? eachBatchInstance
								.getRemoteBatchInstance().getRemoteBatchInstanceIdentifier() : null);
						batchLocal.setReviewOperatorName(eachBatchInstance.getReviewUserName());
						batchLocal.setServerIP(eachBatchInstance.getServerIP());
						batchLocal.setUncSubFolder(eachBatchInstance.getUncSubfolder());
						batchLocal.setValidateOperatorName(eachBatchInstance.getValidationUserName());
						batchInstanceList.add(batchLocal);
					}
				}
				StreamResult result = null;
				try {
					result = new StreamResult(resp.getOutputStream());
					resp.setStatus(HttpServletResponse.SC_OK);
					batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().marshal(batchInstances, result);
				} catch (final IOException e) {
					respStr = INTERNAL_SERVER_ERROR + e;
					LOGGER.error("Internal Server error.Exception occured:" + e.getMessage());
				} finally {
					if (result != null) {
						IOUtils.closeQuietly(result.getOutputStream());
					}
				}
			} else {
				respStr = "No results found.";
			}
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final IOException ioe) {
				LOGGER.info(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + ioe, ioe);
			}
		}
	}

	/**
	 * To get all Modules Workflow Name by Batch Class.
	 * @param identifier {@link String}
	 * @param resp {@link HttpServletResponse}
	 * @param req {@link HttpServletRequest}
	 */
	@RequestMapping(value = "/getAllModulesWorkflowNameByBatchClass/{batchClassIdentifier}", method = RequestMethod.GET)
	@ResponseBody
	public void getAllModulesWorkflowNameByBatchClass(@PathVariable("batchClassIdentifier") final String identifier,
			final HttpServletResponse resp, final HttpServletRequest req) {
		LOGGER.info("Start processing web service for get all modules workflowName by batchClassId");
		String respStr = "";
		Set<String> loggedInUserRole = getUserRoles(req);
		if (loggedInUserRole == null || loggedInUserRole.isEmpty()) {
			respStr = "User is not authorized to view this API.";
		} else {
			if (identifier != null && !identifier.isEmpty()) {
				final BatchClass batchClass = bcService.getBatchClassByIdentifier(identifier);
				if (batchClass != null) {
					boolean isBatchClassViewableToUser = isBatchClassViewableToUser(identifier, loggedInUserRole, isSuperAdmin(req));
					if (isBatchClassViewableToUser) {
						List<BatchClassModule> modules = batchClass.getBatchClassModules();
						Modules modulesSchema = new Modules();
						for (BatchClassModule bcm : modules) {
							Module module = new Module();
							module.setModuleName(bcm.getModule().getName());
							module.setWorkflowName(bcm.getWorkflowName());
							modulesSchema.getModule().add(module);
						}
						StreamResult result;
						try {
							result = new StreamResult(resp.getOutputStream());
							resp.setStatus(HttpServletResponse.SC_OK);
							batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().marshal(modulesSchema, result);
						} catch (final IOException e) {
							try {
								resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR + e.getMessage());
							} catch (final IOException ioe) {
								LOGGER.info(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + ioe, ioe);
							}
						}
					} else {
						respStr = "User not authorized to view this batch class id:" + identifier;
						LOGGER.error(SERVER_ERROR_MSG + respStr);
					}
				} else {
					respStr = "Batch Class does not exist with batch class id:" + identifier;
					LOGGER.error(SERVER_ERROR_MSG + respStr);
				}
			} else {
				respStr = "Invalid input parameters.";
				LOGGER.error(SERVER_ERROR_MSG + respStr);

			}
		}

		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (IOException ioe) {
				LOGGER.info(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + ioe, ioe);
			}
		}
	}

	/**
	 * Internal method to check if the batch class is viewable to the logged in user.
	 * 
	 * @param batchClassId {@link String}
	 * @param loggedInUserRole Set<String>
	 * @param isSuperAdmin boolean
	 * @return boolean
	 */
	private boolean isBatchClassViewableToUser(final String batchClassId, Set<String> loggedInUserRole, boolean isSuperAdmin) {
		LOGGER.info("Starting \"isBatchClassViewableToUser\" service for batch class id:" + batchClassId);
		List<BatchClass> batchClasses = null;
		if (isSuperAdmin) {
			LOGGER.info("Getting all batch classes.");
			batchClasses = bcService.getAllBatchClasses();
		} else {
			LOGGER.info("Getting batch classes by user roles.");
			batchClasses = bcService.getAllBatchClassesByUserRoles(loggedInUserRole);
		}
		boolean isBatchClassViewableToUser = false;
		for (BatchClass batchClass : batchClasses) {
			if (batchClass.getIdentifier().equalsIgnoreCase(batchClassId)) {
				isBatchClassViewableToUser = true;
				break;
			}
		}
		LOGGER.info("isBatchClassViewableToUser:" + isBatchClassViewableToUser + " Batch Class id:" + batchClassId
				+ " logged in user role:" + loggedInUserRole.toString());
		LOGGER.info("isSuperAdmin Flag value:" + isSuperAdmin);
		return isBatchClassViewableToUser;
	}

	/**
	 * To get Batch Class List.
	 * @param resp {@link HttpServletResponse}
	 * @param req {@link HttpServletRequest}
	 */
	@RequestMapping(value = "/getBatchClassList", method = RequestMethod.GET)
	@ResponseBody
	public void getBatchClassList(final HttpServletResponse resp, final HttpServletRequest req) {
		LOGGER.info("Start processing web service for get batch class list");
		LOGGER.info("Fetching batch class from the database");
		Set<String> loggedInUserRole = getUserRoles(req);
		if (loggedInUserRole == null || loggedInUserRole.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "User not authorized to view this API.");
			} catch (final IOException ioe) {
				LOGGER.error("Error while sending the error response.");
			}
		} else {
			List<BatchClass> batchClass = null;
			if (isSuperAdmin(req)) {
				batchClass = bcService.getAllBatchClassesExcludeDeleted();
			} else {
				batchClass = bcService.getAllBatchClassesByUserRoles(loggedInUserRole);
			}
			final BatchClasses batchClasses = new BatchClasses();
			final List<com.ephesoft.dcma.batch.schema.BatchClasses.BatchClass> batchClassList = batchClasses.getBatchClass();
			LOGGER.info("Total batch class found from the database is : " + batchClass.size());
			for (final BatchClass eachBatchClass : batchClass) {
				final com.ephesoft.dcma.batch.schema.BatchClasses.BatchClass batchClassLocal = createBatchClass(eachBatchClass);
				batchClassList.add(batchClassLocal);

			}
			StreamResult result;
			try {
				result = new StreamResult(resp.getOutputStream());
				resp.setStatus(HttpServletResponse.SC_OK);
				batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().marshal(batchClasses, result);
			} catch (final IOException e) {
				try {
					resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR + e.getMessage());
				} catch (final IOException ioe) {
					LOGGER.info(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + ioe, ioe);

				}
			}
		}
	}

	/**
	 * Method to perform conversion of batch class from database to xsd for web service.
	 * 
	 * @param eachBatchClass {@link BatchClass}
	 * @return com.ephesoft.dcma.batch.schema.BatchClasses.BatchClass
	 */
	private com.ephesoft.dcma.batch.schema.BatchClasses.BatchClass createBatchClass(final BatchClass eachBatchClass) {
		final com.ephesoft.dcma.batch.schema.BatchClasses.BatchClass batchClassLocal = new com.ephesoft.dcma.batch.schema.BatchClasses.BatchClass();
		batchClassLocal.setCurrentUser(eachBatchClass.getCurrentUser());
		batchClassLocal.setDescription(eachBatchClass.getDescription());
		batchClassLocal.setIdentifier(eachBatchClass.getIdentifier());
		batchClassLocal.setName(eachBatchClass.getName());
		batchClassLocal.setPriority(eachBatchClass.getPriority());
		batchClassLocal.setUncFolder(eachBatchClass.getUncFolder());
		batchClassLocal.setVersion(eachBatchClass.getVersion());
		return batchClassLocal;
	}

	/**
	 * To get roles.
	 * @param identifier {@link String}
	 * @param resp {@link HttpServletResponse}
	 * @param req {@link HttpServletRequest}
	 * @return Set<String>
	 */
	@RequestMapping(value = "/getRoles/{batchClassIdentifier}", method = RequestMethod.GET)
	@ResponseBody
	public Set<String> getRoles(@PathVariable("batchClassIdentifier") final String identifier, final HttpServletResponse resp,
			final HttpServletRequest req) {
		LOGGER.info("Start processing web service for get roles of batch class");

		Set<String> userGroups = null;
		String respStr = "";
		if (identifier != null && !identifier.isEmpty()) {
			Set<String> loggedInUserRole = getUserRoles(req);
			boolean isBatchClassViewableToUser = isBatchClassViewableToUser(identifier, loggedInUserRole, isSuperAdmin(req));
			if (isBatchClassViewableToUser) {
				final BatchClass batchClass = bcService.getBatchClassByIdentifier(identifier);
				if (batchClass != null) {
					LOGGER.info("Fetching user roles for batch class identifier" + identifier);
					userGroups = batchClassGroupsService.getRolesForBatchClass(identifier);
					resp.setStatus(HttpServletResponse.SC_OK);
				} else {
					respStr = "Batch Class does not exist.";
					LOGGER.error(SERVER_ERROR_MSG + respStr);
				}
			} else {
				respStr = "User is not authorized to view the batch class roles for given identifier:" + identifier;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			}
		} else {
			respStr = "Invalid input parameters.";
			LOGGER.error(SERVER_ERROR_MSG + respStr);
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (IOException e) {
				LOGGER.info(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + e, e);
			}
		}
		return userGroups;
	}

	/**
	 * To get Batch Class for Role.
	 * @param role String
	 * @param resp {@link HttpServletResponse}
	 * @return List<String>
	 */
	@RequestMapping(value = "/getBatchClassForRole/{role}", method = RequestMethod.GET)
	@ResponseBody
	public List<String> getBatchClassForRole(@PathVariable("role") final String role, final HttpServletResponse resp) {
		LOGGER.info("Start processing web service for get batch classes for user role:" + role);
		List<String> batchClassList = new ArrayList<String>();
		Set<String> batchClasses = new TreeSet<String>();
		String respStr = WebServiceUtil.EMPTY_STRING;
		if (role != null && !role.isEmpty()) {
			if (userConnectivityService.getAllGroups().contains(role)) {
				LOGGER.info("Given role : " + role + " is valid.");
				boolean isSuperAdmin = false;
				if (userConnectivityService.getAllSuperAdminGroups().contains(role)) {
					isSuperAdmin = true;
					LOGGER.info("Given role:" + role + " is super admin.");
				}
				if (isSuperAdmin) {
					List<String> results = bcService.getAllBatchClassIdentifier();
					for (String batchClassId : results) {
						batchClasses.add(batchClassId);
					}
				} else {
					final Set<String> userRoles = new HashSet<String>();
					userRoles.add(role);
					LOGGER.info("Fetching batch classes for user role" + role);

					Set<String> batchClassIdentifiers = batchClassGroupsService.getBatchClassIdentifierForUserRoles(userRoles, false);
					if (batchClassIdentifiers != null) {
						batchClasses.addAll(batchClassIdentifiers);
					}
				}
				resp.setStatus(HttpServletResponse.SC_OK);
			} else {
				respStr = "Invalid role provided. This is not amongst the list of valid roles.";
			}
		} else {
			respStr = "Invalid input parameters. Please provide a value for role.";
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (IOException e) {
				LOGGER.info(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + e, e);
			}
		}
		for (String batchClassIdentifier : batchClasses) {
			batchClassList.add(batchClassIdentifier);
		}
		return batchClassList;
	}

	/**
	 * To get Batch Instance for Roles.
	 * @param role {@link String}
	 * @param resp {@link HttpServletResponse}
	 * @return List<String>
	 */
	@RequestMapping(value = "/getBatchInstanceForRole/{role}", method = RequestMethod.GET)
	@ResponseBody
	public List<String> getBatchInstanceForRoles(@PathVariable("role") final String role, final HttpServletResponse resp) {
		LOGGER.info("Start processing web service for get batch instances for user role");
		List<String> batchInstancesList = new ArrayList<String>();
		Set<String> batchInstances = new TreeSet<String>();
		String respStr = WebServiceUtil.EMPTY_STRING;
		if (role != null && !role.isEmpty()) {
			if (userConnectivityService.getAllGroups().contains(role)) {
				final Set<String> userRoles = new HashSet<String>();
				userRoles.add(role);
				LOGGER.info("Given role:" + role + " is valid.");
				boolean isSuperAdmin = false;
				if (userConnectivityService.getAllSuperAdminGroups().contains(role)) {
					isSuperAdmin = true;
					LOGGER.info("Given role:" + role + " is super admin.");
				}
				if (isSuperAdmin) {
					List<BatchClass> batchClassList = bcService.getAllBatchClasses();
					if (batchClassList != null) {
						for (BatchClass batchClass : batchClassList) {
							List<BatchInstance> eachBatchInstance = biService.getBatchInstByBatchClass(batchClass);
							for (BatchInstance batchInstance : eachBatchInstance) {
								batchInstances.add(batchInstance.getIdentifier());
							}
						}
					}
				} else {
					List<BatchClass> batchClasses = bcService.getAllBatchClassesByUserRoles(userRoles);
					if (batchClasses != null) {
						for (BatchClass batchClass : batchClasses) {
							List<BatchInstance> eachBatchInstance = biService.getBatchInstByBatchClass(batchClass);
							for (BatchInstance batchInstance : eachBatchInstance) {
								batchInstances.add(batchInstance.getIdentifier());
							}
						}
					}
				}
				resp.setStatus(HttpServletResponse.SC_OK);
			} else {
				respStr = "Invalid role provided. This is not amongst the list of valid roles.";
			}
		} else {
			respStr = "Invalid input parameters. Please provide a value for role.";
		}

		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (IOException e) {
				LOGGER.info(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + e, e);
			}
		}
		for (String batchInstanceIdentifier : batchInstances) {
			batchInstancesList.add(batchInstanceIdentifier);
		}
		return batchInstancesList;
	}

	/**
	 * To delete Batch Instance.
	 * @param identifier {@link String}
	 * @param resp {@link HttpServletResponse}
	 * @param req {@link HttpServletRequest}
	 * @return
	 */
	@RequestMapping(value = "/deleteBatchInstance/{batchInstanceIdentifier}", method = RequestMethod.GET)
	@ResponseBody
	public String deleteBatchInstance(@PathVariable("batchInstanceIdentifier") final String identifier,
			final HttpServletResponse resp, final HttpServletRequest req) {
		LOGGER.info("Start processing web service for delete batch instance");
		String respStr = WebServiceUtil.EMPTY_STRING;
		boolean isSuccess = false;
		if (identifier != null && !identifier.isEmpty()) {
			final BatchInstance batchInstance = biService.getBatchInstanceByIdentifier(identifier);
			try {
				// Status for which a batch can be deleted:
				if (batchInstance != null
						&& (batchInstance.getStatus().equals(BatchInstanceStatus.ERROR)
								|| batchInstance.getStatus().equals(BatchInstanceStatus.READY_FOR_REVIEW)
								|| batchInstance.getStatus().equals(BatchInstanceStatus.READY_FOR_VALIDATION) || batchInstance
								.getStatus().equals(BatchInstanceStatus.RUNNING))) {

					Set<String> batchInstanceRoles = biService.getRolesForBatchInstance(batchInstance);
					Set<String> loggedInUserRole = getUserRoles(req);
					if (isSuperAdmin(req) || batchInstanceRoles.removeAll(loggedInUserRole)) {
						LOGGER.info("Deleting the batch instance:" + identifier);
						pluginPropertiesService.clearCache(identifier);
						jbpmService.deleteProcessInstance(batchInstance.getProcessInstanceKey());
						batchInstance.setStatus(BatchInstanceStatus.DELETED);
						biService.updateBatchInstance(batchInstance);
						final File uncFile = new File(batchInstance.getUncSubfolder());
						LOGGER.info("uncFile for the batch instance:" + uncFile);
						if (null != uncFile) {
							FileUtils.deleteDirectoryAndContentsRecursive(uncFile);
							LOGGER.info("Deleted the unc folders of batch instance:" + identifier + " successfully.");
						}
						isSuccess = true;
					} else {
						respStr = "User is not authorized to perform operation on this batch instance." + identifier;
						LOGGER.error(SERVER_ERROR_MSG + respStr);
					}
				} else {
					respStr = "Either Batch instance does not exist with batch instance identifier " + identifier
							+ " or batch exists with incorrect status to be deleted. Batch instance should be of status:-"
							+ "ERROR, READY_FOR_REVIEW, READY_FOR_VALIDATION, RUNNING";
					LOGGER.error(SERVER_ERROR_MSG + respStr);
				}
			} catch (final Exception e) {
				respStr = "Error while deleting batch instance id:" + identifier + ".Please check logs for further details." + e;
				LOGGER.error(SERVER_ERROR_MSG + respStr);

			}
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final IOException ioe) {
				LOGGER.info(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + ioe, ioe);

			}
		}
		return (isSuccess ? "Batch deleted successfully." : "Failure while deleting batch instance.");
	}

	/**
	 * To restart Batch Instance.
	 * @param identifier {@link String}
	 * @param moduleName {@link String}
	 * @param resp {@link HttpServletResponse}
	 * @param req {@link HttpServletRequest}
	 * @return {@link String}
	 */
	@RequestMapping(value = "/restartBatchInstance/{batchInstanceIdentifier}/{restartAtModuleName}", method = RequestMethod.GET)
	@ResponseBody
	public String restartBatchInstance(@PathVariable("batchInstanceIdentifier") final String identifier,
			@PathVariable("restartAtModuleName") String moduleName, final HttpServletResponse resp, final HttpServletRequest req) {
		LOGGER.info("Start processing web service for restart batch instance");
		boolean isSuccess = false;
		String moduleNameLocal = moduleName;
		Set<String> loggedInUserRole = getUserRoles(req);
		String respStr = WebServiceUtil.EMPTY_STRING;
		if (identifier != null && !identifier.isEmpty()) {
			LOGGER.info("Start processing of restarting batch for batch instance:" + identifier);
			BatchInstance batchInstance = biService.getBatchInstanceByIdentifier(identifier);
			// only batch instance with these status can be restarted
			if (batchInstance != null
					&& (batchInstance.getStatus().equals(BatchInstanceStatus.ERROR)
							|| batchInstance.getStatus().equals(BatchInstanceStatus.READY_FOR_REVIEW)
							|| batchInstance.getStatus().equals(BatchInstanceStatus.READY_FOR_VALIDATION) || batchInstance.getStatus()
							.equals(BatchInstanceStatus.RUNNING))) {
				LOGGER.info("Batch is in the valid state to restart.Restarting batch instance:" + batchInstance);

				Set<String> batchInstanceRoles = biService.getRolesForBatchInstance(batchInstance);
				if (isSuperAdmin(req) || batchInstanceRoles.removeAll(loggedInUserRole)) {
					LOGGER.info("User is authorized to perform operation on the batch instance:" + identifier);
					final String batchClassIdentifier = biService.getBatchClassIdentifier(identifier);
					String executedBatchInstanceModules = batchInstance.getExecutedModules();
					if (executedBatchInstanceModules != null) {
						String[] executedModulesArray = executedBatchInstanceModules.split(";");
						if (batchClassIdentifier != null) {
							LOGGER.info("Restarting the batch instance for the  batch class:" + batchClassIdentifier);
							final BatchClassModule batchClassModuleItem = bcModuleService.getBatchClassModuleByWorkflowName(
									batchClassIdentifier, moduleNameLocal);
							if (batchClassModuleItem != null) {
								for (String executedModule : executedModulesArray) {
									if (executedModule.equalsIgnoreCase(String.valueOf(batchClassModuleItem.getModule().getId()))) {
										isSuccess = true;
										break;
									}
								}
							}
						}
					} else {
						isSuccess = true;
						List<BatchClassModule> batchClassModuleList = batchInstance.getBatchClass().getBatchClassModules();
						moduleNameLocal = batchClassModuleList.get(0).getWorkflowName();
						LOGGER.info("Restarting the batch from first module." + moduleNameLocal
								+ " as the executed module list is empty.");
					}
					final boolean isZipSwitchOn = bsService.isZipSwitchOn();
					LOGGER.info("Zipped Batch XML switch is:" + isZipSwitchOn);

					final String activeModule = workflowService.getActiveModule(batchInstance);
					LOGGER.info("The activeModule of batch:" + activeModule);
					if (isSuccess) {
						LOGGER.info("All parameters for restarting the batch are valid.");
						respStr = processRestartingBatchInternal(identifier, moduleNameLocal, respStr, batchInstance,
								batchClassIdentifier, isZipSwitchOn, activeModule);
					} else {
						isSuccess = false;
						List<BatchClassModule> batchClassModules = bcModuleService
								.getAllBatchClassModulesByIdentifier(batchClassIdentifier);
						String[] executedModulesArray = executedBatchInstanceModules.split(";");
						Set<String> executedWorkflows = new HashSet<String>();
						for (String executedModuleId : executedModulesArray) {
							for (BatchClassModule batchClassModule : batchClassModules) {
								if (batchClassModule != null
										&& executedModuleId.equalsIgnoreCase(String.valueOf(batchClassModule.getModule().getId()))) {
									executedWorkflows.add(batchClassModule.getWorkflowName());
									break;
								}
							}
						}
						respStr = "Invalid parameter for restarting batch instance. Batch is being restarted from a module:"
								+ moduleNameLocal
								+ " that may not yet be executed or is non existent. Please select the valid module name for restart from the following :"
								+ executedWorkflows.toString();
						LOGGER.error(SERVER_ERROR_MSG + respStr);
					}
				} else {
					respStr = "User is not authorized to perform operation on this batch instance." + identifier;
					LOGGER.error(SERVER_ERROR_MSG + respStr);
				}
			} else {
				respStr = "Either Batch instance does not exist with batch instance identifier " + identifier
						+ " or batch exists with incorrect status to be restarted. Batch instance should be of status:-"
						+ "ERROR, READY_FOR_REVIEW, READY_FOR_VALIDATION, RUNNING";
				isSuccess = false;
			}
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final IOException ioe) {
				LOGGER.debug(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + ioe, ioe);
			}
		}
		return (isSuccess ? "Batch restarted successfully from module:" + moduleNameLocal : "Failure while restarting batch instance.");
	}

	private String processRestartingBatchInternal(final String identifier, final String moduleName, String respStr,
			final BatchInstance batchInstance, final String batchClassIdentifier, final boolean isZipSwitchOn,
			final String activeModule) {
		String respStrLocal = respStr;
		BatchInstance batchInstanceLocal = batchInstance;
		try {
			LOGGER.info("Inside processRestartingBatchInternal method.");
			jbpmService.deleteProcessInstance(batchInstanceLocal.getProcessInstanceKey());
			final BatchInstanceThread batchInstanceThread = ThreadPool.getBatchInstanceThreadList(identifier);
			if (batchInstanceThread != null) {
				batchInstanceThread.remove();
				LOGGER.info("Removing the batch instance thread done successsfully.");
			}
			String executedModules = WebServiceUtil.EMPTY_STRING;
			if (moduleName != null) {
				LOGGER.info("Restarting the batch instance from the module:" + moduleName);
				Properties properties = WebServiceUtil.fetchConfig();

				importBatchService.updateBatchFolders(properties, batchInstanceLocal, moduleName, isZipSwitchOn);

				LOGGER.info("Batch folders have been updated successfully.");
				executedModules = batchInstanceLocal.getExecutedModules();
				if (executedModules != null && (!executedModules.isEmpty())) {
					LOGGER.info("List of executed modules:" + executedModules);
					if (batchClassIdentifier != null) {
						final BatchClassModule batchClassModuleItem = bcModuleService.getBatchClassModuleByWorkflowName(
								batchClassIdentifier, moduleName);
						final BatchClass batchClass = bcService.getBatchClassByIdentifier(batchClassIdentifier);
						final List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
						if (null != batchClassModules) {
							for (final BatchClassModule batchClassModule : batchClassModules) {
								if (batchClassModule != null && batchClassModule.getModule() != null
										&& batchClassModule.getOrderNumber() >= batchClassModuleItem.getOrderNumber()) {
									final String replaceText = batchClassModule.getModule().getId() + ";";
									executedModules = executedModules.replace(replaceText, WebServiceUtil.EMPTY_STRING);
								}
							}
						}
					}
				}
				LOGGER.info("Resetted the executed modules list to :" + executedModules);
			} else {
				LOGGER.info("Restarting the batch instance from the begining.");
				importBatchService.removeFolders(batchInstanceLocal);
				executedModules = WebServiceUtil.EMPTY_STRING;
				LOGGER.info("Resetted the executed modules list to :" + executedModules);
			}

			batchInstanceLocal = biService.getBatchInstanceByIdentifier(identifier);
			batchInstanceLocal.setExecutedModules(executedModules);
			batchInstanceLocal = biService.merge(batchInstanceLocal);
			LOGGER.info("BatchInstance object updated in the database successfully.");

			if (activeModule != null && activeModule.contains("Workflow_Continue_Check")) {
				LOGGER.error("Error in restarting batch instance : " + identifier);
			}
			workflowService.startWorkflow(batchInstanceLocal.getBatchInstanceID(), moduleName);
		} catch (final IOException ex) {
			respStrLocal = "Cannot open and load backUpService properties file." + ex;
			LOGGER.error(respStrLocal, ex);
		} catch (final DCMAApplicationException dcmae) {
			respStrLocal = "Error while restarting batch instance " + identifier + dcmae;
			LOGGER.error(respStrLocal, dcmae);
		} catch (final Exception e) {
			respStrLocal = "Error in restarting batch instance " + identifier + e;
			LOGGER.error(respStrLocal, e);

			// update the batch instance to ERROR state.
			if (jbpmService != null) {
				jbpmService.deleteProcessInstance(batchInstanceLocal.getProcessInstanceKey());
			}
			biService.updateBatchInstanceStatusByIdentifier(batchInstanceLocal.getIdentifier(), BatchInstanceStatus.ERROR);
		}
		return respStrLocal;

	}

	/**
	 * To add User Roles to Batch Instance.
	 * @param identifier {@link String}
	 * @param userRole {@link String}
	 * @param resp {@link HttpServletResponse}
	 * @param req {@link HttpServletRequest}
	 * @return {@link String}
	 * @throws IOException in case of error
	 */
	@RequestMapping(value = "/addUserRolesToBatchInstance/{batchInstanceIdentifier}/{userRole}", method = RequestMethod.GET)
	@ResponseBody
	public String addUserRolesToBatchInstance(@PathVariable("batchInstanceIdentifier") final String identifier,
			@PathVariable("userRole") final String userRole, final HttpServletResponse resp, final HttpServletRequest req)
			throws IOException {
		LOGGER.info("Start processing web service for adding user roles to batch instance identifier");

		String respStr = WebServiceUtil.EMPTY_STRING;
		boolean isSuccess = false;
		if (identifier != null && !identifier.isEmpty()) {
			final BatchInstance batchInstance = biService.getBatchInstanceByIdentifier(identifier);
			Set<String> allRoles = userConnectivityService.getAllGroups();
			if (allRoles != null && allRoles.contains(userRole)) {
				try {
					if (batchInstance != null) {
						Set<String> batchInstanceRoles = biService.getRolesForBatchInstance(batchInstance);
						Set<String> loggedInUserRoles = getUserRoles(req);
						if (isSuperAdmin(req) || batchInstanceRoles.removeAll(loggedInUserRoles)) {
							batchInstanceGroupsService.addUserRolesToBatchInstanceIdentifier(identifier, userRole);
							resp.setStatus(HttpServletResponse.SC_OK);
							isSuccess = true;
						} else {
							respStr = "User is not authorized to perform operation on this batch instance." + identifier;
							LOGGER.error(SERVER_ERROR_MSG + respStr);
						}
					} else {
						respStr = "Batch instance does not exist with batch instance identifier " + identifier;
						LOGGER.error(SERVER_ERROR_MSG + respStr);
					}
				} catch (final Exception e) {
					respStr = "Error in adding roles to batch instance identifier: " + identifier + "." + e.getMessage();
					LOGGER.error(SERVER_ERROR_MSG + respStr);
				}
			} else {
				respStr = "Invalid role provided. This is not amongst the list of valid roles.";
			}
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final IOException ioe) {
				LOGGER.info(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + ioe, ioe);
			}
		}
		return (isSuccess ? "User role is added successfully to batch instance." : "Failure while adding roles to batch instance: "
				+ identifier + ".");
	}

	/**
	 * To create Searchable PDF.
	 * @param request {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 */
	@RequestMapping(value = "/createSearchablePDF", method = RequestMethod.POST)
	@ResponseBody
	public void createSearchablePDF(final HttpServletRequest request, final HttpServletResponse response) {
		LOGGER.info("Start processing web service for create searchable pdf");
		String respStr = WebServiceUtil.EMPTY_STRING;
		String workingDir = WebServiceUtil.EMPTY_STRING;
		InputStream instream = null;
		OutputStream outStream = null;
		if (request instanceof DefaultMultipartHttpServletRequest) {
			try {
				final String webServiceFolderPath = bsService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
				LOGGER.info("workingDir:" + workingDir);
				final String outputDir = WebServiceUtil.createWebServiceOutputDir(workingDir);
				LOGGER.info("outputDir:" + outputDir);
				final DefaultMultipartHttpServletRequest multipartRequest = (DefaultMultipartHttpServletRequest) request;
				final BatchInstanceThread batchInstanceThread = new BatchInstanceThread(new File(workingDir).getName() + Math.random());

				final String isColorImage = request.getParameter("isColorImage");
				final String isSearchableImage = request.getParameter("isSearchableImage");
				final String outputPDFFileName = request.getParameter("outputPDFFileName");
				final String projectFile = request.getParameter(WebServiceUtil.PROJECT_FILE);
				String results = WebServiceUtil.validateSearchableAPI(outputPDFFileName, projectFile, FileType.PDF
						.getExtensionWithDot(), isSearchableImage, isColorImage);
				if (!results.isEmpty()) {
					respStr = results;
				} else {
					LOGGER.info("Value of isColorImage" + isColorImage);
					LOGGER.info("Value of isSearchableImage" + isSearchableImage);
					LOGGER.info("Value of outputPDFFileName" + outputPDFFileName);
					LOGGER.info("Value of projectFile" + projectFile);

					final MultiValueMap<String, MultipartFile> fileMap = multipartRequest.getMultiFileMap();
					for (final String fileName : fileMap.keySet()) {
						try {
							if (fileName.toLowerCase(Locale.getDefault()).indexOf(WebServiceUtil.RSP_EXTENSION) > -1
									|| fileName.toLowerCase(Locale.getDefault()).indexOf(FileType.TIF.getExtension()) > -1
									|| fileName.toLowerCase(Locale.getDefault()).indexOf(FileType.TIFF.getExtension()) > -1) {
								// only tiffs and RSP file is expected
								final MultipartFile multiPartFile = multipartRequest.getFile(fileName);
								try {
									instream = multiPartFile.getInputStream();
									final File file = new File(workingDir + File.separator + fileName);
									outStream = new FileOutputStream(file);
									final byte[] buf = new byte[WebServiceUtil.bufferSize];
									int len = instream.read(buf);
									while (len > 0) {
										outStream.write(buf, 0, len);
										len = instream.read(buf);
									}

									if (fileName.endsWith(FileType.TIF.getExtensionWithDot())
											|| fileName.endsWith(FileType.TIFF.getExtensionWithDot())) {
										int pageCount = TIFFUtil.getTIFFPageCount(workingDir + File.separator + fileName);
										if (pageCount > 1) {
											respStr = ONLY_ONE_SINGLE_PAGE_TIFF_EXPECTED;
											break;
										}
									}
								} finally {
									if (instream != null) {
										instream.close();
									}
									if (outStream != null) {
										outStream.close();
									}
								}
							} else {
								respStr = "Only tiff, tif and rsp files expected.";
								LOGGER.error(SERVER_ERROR_MSG + respStr);
								break;
							}
						} finally {
							IOUtils.closeQuietly(instream);
							IOUtils.closeQuietly(outStream);
						}
					}
					if (respStr.isEmpty()) {
						String[] imageFiles = null;
						final File file = new File(workingDir);

						imageFiles = file.list(new CustomFileFilter(false, FileType.TIFF.getExtensionWithDot(), FileType.TIF
								.getExtensionWithDot()));

						if (imageFiles == null || imageFiles.length == 0) {
							respStr = "No tif/tiff file found for processing.";
						}
						// getting rsp project file name
						String rspProjectFile = workingDir + File.separator + projectFile;
						LOGGER.info("rspProjectFile:" + rspProjectFile);
						File rspFile = new File(rspProjectFile);

						if (rspProjectFile == null || !rspFile.exists()) {
							respStr = "Invalid project file. Please verify the project file.";
							LOGGER.error(SERVER_ERROR_MSG + respStr);
						}

						if (respStr.isEmpty()) {
							final String[] pages = new String[imageFiles.length + 1];
							int index = 0;
							for (final String imageFileName : imageFiles) {
								pages[index] = workingDir + File.separator + imageFileName;
								index++;

								if (WebServiceUtil.TRUE.equalsIgnoreCase(isColorImage)) {
									try {
										LOGGER.info("Generating png image files");
										imService.generatePNGForImage(new File(workingDir + File.separator + imageFileName));
										// getting png file name from image file name
										final String pngFileName = imageFileName.substring(0, imageFileName
												.lastIndexOf(WebServiceUtil.DOT))
												+ FileType.PNG.getExtensionWithDot();
										// creating OCR
										recostarService.createOCR(projectFile, workingDir, WebServiceUtil.ON_STRING, pngFileName,
												batchInstanceThread, workingDir);
									} catch (final DCMAException e) {
										// deleting directory
										FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
										respStr = "Error in generating plugin output for color switch \"ON\" for image:"
												+ imageFileName + ". " + e;
										LOGGER.error(SERVER_ERROR_MSG + respStr);
									}
								} else {
									try {
										// creating OCR
										recostarService.createOCR(projectFile, workingDir, WebServiceUtil.OFF_STRING, imageFileName,
												batchInstanceThread, workingDir);
									} catch (final DCMAException e) {
										FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
										respStr = "Error in generating plugin output for color switch \"OFF\" for image:"
												+ imageFileName + ". " + e;
										LOGGER.error(SERVER_ERROR_MSG + respStr);
									}
								}
							}
							if (respStr.isEmpty()) {
								try {
									LOGGER.info("Generating HOCR file for input images.");
									// executing the batch instance thread
									batchInstanceThread.execute();
									batchInstanceThread.remove();
									final String outputPDFFile = workingDir + File.separator + outputPDFFileName;
									pages[index] = outputPDFFile;
									// creating searchable PDF
									imService.createSearchablePDF(isColorImage, isSearchableImage, workingDir, pages,
											batchInstanceThread, WebServiceUtil.DOCUMENTID);
									batchInstanceThread.execute();
									LOGGER.info("Copying output searchable file");
									FileUtils.copyFile(new File(outputPDFFile), new File(outputDir + File.separator
											+ outputPDFFileName));
								} catch (final DCMAApplicationException e) {
									batchInstanceThread.remove();
									// deleting the directory contents
									FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
									respStr = "Error in generating searchable pdf." + e;
									LOGGER.error(SERVER_ERROR_MSG + respStr);
								}
								if (respStr.isEmpty()) {
									ServletOutputStream out = null;
									ZipOutputStream zout = null;
									final String zipFileName = WebServiceUtil.SERVEROUTPUTFOLDERNAME;
									// setting the content type
									response.setContentType(WebServiceUtil.ATTACHMENT_FILENAME);
									// setting the content header
									response.setHeader(WebServiceUtil.CONTENT_DISPOSITION, WebServiceUtil.ATTACHMENT_FILENAME
											+ zipFileName + FileType.ZIP.getExtensionWithDot() + NEXT_LINE_STRING);
									try {
										// zip the directory contents and set the response status
										out = response.getOutputStream();
										zout = new ZipOutputStream(out);
										FileUtils.zipDirectory(outputDir, zout, zipFileName);
										response.setStatus(HttpServletResponse.SC_OK);
									} catch (final IOException e) {
										respStr = "Unable to process web service request.Please try again." + e;
										LOGGER.error(SERVER_ERROR_MSG + respStr);
									} finally {
										// clean up code
										IOUtils.closeQuietly(zout);
										IOUtils.closeQuietly(out);
										FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
									}
								}
							}
						}
					}
				}
			} catch (Exception e) {
				respStr = INTERNAL_SERVER_ERROR + e;
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			}
		} else {
			respStr = IMPROPER_INPUT_TO_SERVER;
		}
		if (!workingDir.isEmpty()) {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
		}
		if (!respStr.isEmpty()) {
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
			} catch (final IOException ioe) {
				LOGGER.info(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + ioe, ioe);
			}
		}
	}

	/**
	 * To create OCR.
	 * @param req {@link HttpServletRequest}
	 * @param resp {@link HttpServletResponse}
	 */
	@RequestMapping(value = "/createOCR", method = RequestMethod.POST)
	@ResponseBody
	public void createOCR(final HttpServletRequest req, final HttpServletResponse resp) {
		LOGGER.info("Start processing web service for create OCRing");
		String respStr = WebServiceUtil.EMPTY_STRING;
		String workingDir = WebServiceUtil.EMPTY_STRING;
		InputStream instream = null;
		OutputStream outStream = null;
		if (req instanceof DefaultMultipartHttpServletRequest) {
			try {
				final String webServiceFolderPath = bsService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
				final String outputDir = WebServiceUtil.createWebServiceOutputDir(workingDir);

				final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) req;

				final BatchInstanceThread batchInstanceThread = new BatchInstanceThread(new File(workingDir).getName() + Math.random());

				final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();

				if (!(fileMap.size() >= 2 && fileMap.size() <= WebserviceConstants.THREE)) {
					respStr = "Invalid number of files. We are supposed only 3 files each of type:XML, Project RSP file(if recostar tool) and tif/tiff/png file.";
				}
				if (respStr.isEmpty()) {
					String xmlFileName = WebServiceUtil.EMPTY_STRING;
					for (final String fileName : fileMap.keySet()) {
						try {
							if (fileName.endsWith(FileType.XML.getExtensionWithDot())) {
								xmlFileName = fileName;
							}
							final MultipartFile multiPartFile = multiPartRequest.getFile(fileName);
							instream = multiPartFile.getInputStream();
							final File file = new File(workingDir + File.separator + fileName);
							outStream = new FileOutputStream(file);
							final byte[] buf = new byte[WebServiceUtil.bufferSize];
							int len = instream.read(buf);
							while (len > 0) {
								outStream.write(buf, 0, len);
								len = instream.read(buf);
							}
							if (fileName.endsWith(FileType.TIF.getExtensionWithDot())
									|| fileName.endsWith(FileType.TIFF.getExtensionWithDot())) {
								int pageCount = TIFFUtil.getTIFFPageCount(workingDir + File.separator + fileName);
								if (pageCount > 1) {
									respStr = ONLY_ONE_SINGLE_PAGE_TIFF_EXPECTED;
									break;
								}
							}
						} finally {
							IOUtils.closeQuietly(instream);
							IOUtils.closeQuietly(outStream);
						}

					}
					WebServiceParams webServiceParams = null;
					final File xmlFile = new File(workingDir + File.separator + xmlFileName);
					if (xmlFile.exists()) {
						final FileInputStream inputStream = new FileInputStream(xmlFile);
						Source source = XMLUtil.createSourceFromStream(inputStream);
						webServiceParams = (WebServiceParams) batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().unmarshal(source);
					}

					List<Param> paramList = webServiceParams.getParams().getParam();
					if (paramList == null || paramList.isEmpty()) {
						FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
						respStr = IMPROPER_XML_PARAMETER;
					} else {
						String ocrEngine = WebServiceUtil.EMPTY_STRING;
						String colorSwitch = WebServiceUtil.EMPTY_STRING;
						String projectFile = WebServiceUtil.EMPTY_STRING;
						String tesseractVersion = WebServiceUtil.EMPTY_STRING;
						String cmdLanguage = WebServiceUtil.EMPTY_STRING;
						for (final Param param : paramList) {
							if (param.getName().equalsIgnoreCase(WebServiceUtil.OCR_ENGINE)) {
								ocrEngine = param.getValue();
								continue;
							}
							if (param.getName().equalsIgnoreCase(WebServiceUtil.COLOR_SWITCH)) {
								colorSwitch = param.getValue();
								continue;
							}
							if (param.getName().equalsIgnoreCase(WebServiceUtil.PROJECT_FILE)) {
								projectFile = param.getValue();
								LOGGER.info("Project file for recostar is :" + projectFile);
								continue;
							}
							if (param.getName().equalsIgnoreCase(WebServiceUtil.TESSERACT_VERSION)) {
								tesseractVersion = param.getValue();
								LOGGER.info("Tesseract version is: " + tesseractVersion);
								continue;
							}
							if (param.getName().equalsIgnoreCase(WebServiceUtil.CMD_LANGUAGE)) {
								// supported values are "eng" and "tha" for now provided tesseract engine is learnt.
								cmdLanguage = param.getValue();
								LOGGER.info("cmd langugage is :" + cmdLanguage);
								continue;
							}
						}

						String results = WebServiceUtil.validateCreateOCRAPI(workingDir, ocrEngine, colorSwitch, projectFile,
								tesseractVersion, cmdLanguage);
						if (!results.isEmpty()) {
							respStr = results;
						} else {
							String[] fileNames = null;
							final File file = new File(workingDir);
							if (colorSwitch.equalsIgnoreCase(WebServiceUtil.ON_STRING)) {
								String[] tifFileNames = file.list(new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(),
										FileType.TIFF.getExtensionWithDot()));
								// generate png file for each tiff/tif file
								for (String tifFile : tifFileNames) {
									imService.generatePNGForImage(new File(workingDir + File.separator + tifFile));
								}
								LOGGER.info("Picking up the png file for processing.");
								fileNames = file.list(new CustomFileFilter(false, FileType.PNG.getExtensionWithDot()));
							} else {
								LOGGER.info("Picking up the tif file for processing.");
								fileNames = file.list(new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(), FileType.TIFF
										.getExtensionWithDot()));
							}
							LOGGER.info("Number of file is:" + fileNames.length);
							LOGGER.info("OcrEngine used for generating ocr is :" + ocrEngine);
							if (ocrEngine.equalsIgnoreCase(WebServiceUtil.RECOSTAR)) {
								if (fileNames != null && fileNames.length > 0) {
									for (final String fileName : fileNames) {
										try {
											LOGGER.info("File processing for recostar is :" + fileName);
											recostarService.createOCR(projectFile, workingDir, colorSwitch, fileName,
													batchInstanceThread, outputDir);
										} catch (final DCMAException e) {
											respStr = "Error occuring while creating OCR file using recostar. Please try again." + e;
											LOGGER.error(SERVER_ERROR_MSG + respStr);
											break;
										}
									}
								} else {
									if (colorSwitch.equalsIgnoreCase(WebServiceUtil.OFF_STRING)) {
										respStr = "Improper input to server. No tiff files provided.";
									} else {
										respStr = "Improper input to server. No tiff/png files provided.";
									}
								}
							} else if (ocrEngine.equalsIgnoreCase(WebServiceUtil.TESSERACT)) {
								if (fileNames != null && fileNames.length > 0) {
									for (final String fileName : fileNames) {
										try {
											LOGGER.info("File processing for ocr with tesseract is :" + fileName);
											tesseractService.createOCR(workingDir, colorSwitch, fileName, batchInstanceThread,
													outputDir, cmdLanguage, tesseractVersion);
										} catch (final DCMAException e) {
											respStr = "Error occuring while creating OCR file using tesseract. Please try again." + e;
											LOGGER.error(SERVER_ERROR_MSG + respStr);
											break;
										}
									}
								} else {
									respStr = "Improper input to server. No tiff/png files provided.";
									LOGGER.error(SERVER_ERROR_MSG + respStr);

								}
							} else {
								respStr = "Please select valid tool for generating OCR file.";
							}
							if (respStr.isEmpty()) {
								try {
									batchInstanceThread.execute();
									if (ocrEngine.equalsIgnoreCase(WebServiceUtil.RECOSTAR)) {
										for (final String fileName : fileNames) {
											final String recostarXMLFileName = fileName.substring(0, fileName
													.lastIndexOf(WebServiceUtil.DOT))
													+ FileType.XML.getExtensionWithDot();
											try {
												FileUtils.copyFile(new File(workingDir + File.separator + recostarXMLFileName),
														new File(outputDir + File.separator + recostarXMLFileName));
											} catch (final Exception e) {
												respStr = "Error while generating copying result file." + e;
												LOGGER.error(SERVER_ERROR_MSG + respStr);
												break;
											}
										}
									}
								} catch (final DCMAApplicationException e) {
									respStr = "Exception while generating ocr using threadpool" + e;
								}
								if (respStr.isEmpty()) {
									ServletOutputStream out = null;
									ZipOutputStream zout = null;
									final String zipFileName = WebServiceUtil.SERVEROUTPUTFOLDERNAME;
									resp.setContentType(WebServiceUtil.APPLICATION_X_ZIP);
									resp.setHeader(WebServiceUtil.CONTENT_DISPOSITION, WebServiceUtil.ATTACHMENT_FILENAME
											+ zipFileName + FileType.ZIP.getExtensionWithDot() + NEXT_LINE_STRING);
									try {
										out = resp.getOutputStream();
										zout = new ZipOutputStream(out);
										FileUtils.zipDirectory(outputDir, zout, zipFileName);
										resp.setStatus(HttpServletResponse.SC_OK);
									} catch (final IOException e) {
										respStr = "Error in creating output zip file.Please try again." + e;
										LOGGER.error(SERVER_ERROR_MSG + respStr);
									} finally {
										IOUtils.closeQuietly(zout);
										IOUtils.closeQuietly(out);

										FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
									}
								}
							}
						}
					}
				}
			} catch (final XmlMappingException xmle) {
				respStr = ERROR_IN_MAPPING_INPUT + xmle;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final DCMAException dcmae) {
				respStr = ERROR_PROCESSING_REQUEST + dcmae;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final Exception e) {
				respStr = INTERNAL_SERVER_ERROR + e;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			}
		} else {
			respStr = IMPROPER_INPUT_TO_SERVER;
			LOGGER.error(SERVER_ERROR_MSG + respStr);
		}
		if (!workingDir.isEmpty()) {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final IOException ioe) {
				LOGGER.info(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + ioe, ioe);
			}
		}
	}

	/**
	 * To classify Image.
	 * @param req {@link HttpServletRequest}
	 * @param resp {@link HttpServletResponse}
	 */
	@RequestMapping(value = "/classifyImage", method = RequestMethod.POST)
	@ResponseBody
	public void classifyImage(final HttpServletRequest req, final HttpServletResponse resp) {
		LOGGER.info("Start processing web service for classifyImage.");
		String respStr = WebServiceUtil.EMPTY_STRING;
		String workingDir = WebServiceUtil.EMPTY_STRING;
		InputStream instream = null;
		OutputStream outStream = null;
		if (req instanceof DefaultMultipartHttpServletRequest) {
			try {
				final String webServiceFolderPath = bsService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);

				final DefaultMultipartHttpServletRequest multipartReq = (DefaultMultipartHttpServletRequest) req;
				String batchClassId = WebServiceUtil.EMPTY_STRING;
				for (final Enumeration<String> params = multipartReq.getParameterNames(); params.hasMoreElements();) {
					final String paramName = params.nextElement();
					if (paramName.equalsIgnoreCase("batchClassId")) {
						batchClassId = multipartReq.getParameter(paramName);
						break;
					}
				}

				if (batchClassId == null || batchClassId.isEmpty()) {
					respStr = "Batch Class identifier not specified.";
				} else {

					BatchClass batchClass = bcService.getBatchClassByIdentifier(batchClassId);
					if (batchClass == null) {
						respStr = "Batch class with the specified identifier:" + batchClassId + " does not exist.";
						LOGGER.error(SERVER_ERROR_MSG + respStr);
					} else {
						Set<String> loggedInUserRole = getUserRoles(req);
						if (!isBatchClassViewableToUser(batchClassId, loggedInUserRole, isSuperAdmin(req))) {
							respStr = USER_NOT_AUTHORIZED_TO_VIEW_THE_BATCH_CLASS + batchClassId;
							LOGGER.error(SERVER_ERROR_MSG + respStr);
						} else {
							BatchPlugin createThumbPlugin = batchClassPPService.getPluginProperties(batchClassId,
									ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN);
							BatchPlugin classifyImgPlugin = batchClassPPService.getPluginProperties(batchClassId,
									ImageMagicKConstants.CLASSIFY_IMAGES_PLUGIN);
							BatchPlugin docAssemblyPlugin = batchClassPPService.getPluginProperties(batchClassId,
									DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN);
							if (createThumbPlugin == null || classifyImgPlugin == null || docAssemblyPlugin == null) {
								respStr = "Either create Thumbnails plugin or Classify Image plugin or document assembly plugin does not exist for the specified batch class id: "
										+ batchClassId;
								LOGGER.error(SERVER_ERROR_MSG + respStr);
							} else if (createThumbPlugin
									.getPluginConfigurations(ImageMagicProperties.CREATE_THUMBNAILS_OUTPUT_IMAGE_PARAMETERS) == null
									|| createThumbPlugin
											.getPluginConfigurations(ImageMagicProperties.CREATE_THUMBNAILS_COMP_THUMB_WIDTH) == null
									|| createThumbPlugin
											.getPluginConfigurations(ImageMagicProperties.CREATE_THUMBNAILS_COMP_THUMB_HEIGHT) == null) {
								respStr = "Create Thumbnails Height or width or output image parameters does not exist for the specified batch class id: "
										+ batchClassId;
								LOGGER.error(SERVER_ERROR_MSG + respStr);
							} else if (classifyImgPlugin.getPluginConfigurations(ImageMagicProperties.CLASSIFY_IMAGES_COMP_METRIC) == null
									|| classifyImgPlugin.getPluginConfigurations(ImageMagicProperties.CLASSIFY_IMAGES_MAX_RESULTS) == null
									|| classifyImgPlugin.getPluginConfigurations(ImageMagicProperties.CLASSIFY_IMAGES_FUZZ_PERCNT) == null) {
								respStr = "Classify Images comp metric or fuzz percent or max results does not exist for the specified batch class id: "
										+ batchClassId;
								LOGGER.error(SERVER_ERROR_MSG + respStr);
							} else if (docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP_MP_LP) == null
									|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP) == null
									|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_MP) == null
									|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_LP) == null
									|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP_LP) == null
									|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP_MP) == null
									|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_MP_LP) == null) {
								respStr = "Incomplete properties of the Document assembler plugin for the specified batch class id: "
										+ batchClassId;
								LOGGER.error(SERVER_ERROR_MSG + respStr);
							}
						}
					}
				}
				if (respStr.isEmpty()) {

					final String outputParams = batchClassPPService.getPropertyValue(batchClassId,
							ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN,
							ImageMagicProperties.CREATE_THUMBNAILS_OUTPUT_IMAGE_PARAMETERS);

					final MultiValueMap<String, MultipartFile> fileMap = multipartReq.getMultiFileMap();

					if (fileMap.size() == 1) {
						String[][] sListOfTiffFiles = new String[fileMap.size()][WebserviceConstants.THREE];
						for (final String fileName : fileMap.keySet()) {
							// only single tiff file is expected as input
							try {
								if ((fileName.toLowerCase(Locale.getDefault()).indexOf(FileType.TIF.getExtension()) > -1 || fileName
										.toLowerCase(Locale.getDefault()).indexOf(FileType.TIFF.getExtension()) > -1)) {

									final MultipartFile multipartFile = multipartReq.getFile(fileName);
									instream = multipartFile.getInputStream();
									final File file = new File(workingDir + File.separator + fileName);
									outStream = new FileOutputStream(file);
									final byte buf[] = new byte[WebserviceConstants.BUF];
									int len = instream.read(buf);
									while (len > WebserviceConstants.ZERO) {
										outStream.write(buf, 0, len);
										len = instream.read(buf);
									}

									if (TIFFUtil.getTIFFPageCount(file.getAbsolutePath()) > WebserviceConstants.ONE) {
										respStr = ONLY_ONE_SINGLE_PAGE_TIFF_EXPECTED;
									}
									break;
								} else {
									respStr = "Improper input to server. Expected only one tiff file. Returning without processing the results.";
								}
							} finally {
								IOUtils.closeQuietly(instream);
								IOUtils.closeQuietly(outStream);
							}
						}
						if (respStr.isEmpty()) {
							String compareThumbnailH = batchClassPPService.getPropertyValue(batchClassId,
									ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN,
									ImageMagicProperties.CREATE_THUMBNAILS_COMP_THUMB_HEIGHT);
							String compareThumbnailW = batchClassPPService.getPropertyValue(batchClassId,
									ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN,
									ImageMagicProperties.CREATE_THUMBNAILS_COMP_THUMB_WIDTH);

							String batchId = new File(workingDir).getName() + Math.random();
							ObjectFactory objectFactory = new ObjectFactory();
							Pages pages = new Pages();
							List<Page> listOfPages = pages.getPage();
							String[] imageFiles = new File(workingDir).list(new CustomFileFilter(false, FileType.TIFF
									.getExtensionWithDot(), FileType.TIF.getExtensionWithDot()));
							for (int i = 0; i < imageFiles.length; i++) {
								String fileName = workingDir + File.separator + imageFiles[i];
								String thumbFileName = "th" + FileType.TIF.getExtensionWithDot();
								String fileTiffPath = workingDir + File.separator + thumbFileName;
								sListOfTiffFiles[i][0] = fileName;
								sListOfTiffFiles[i][1] = fileTiffPath;
								sListOfTiffFiles[i][2] = Integer.toString(i);

								Page pageType = objectFactory.createPage();
								pageType.setIdentifier(EphesoftProperty.PAGE.getProperty() + i);
								pageType.setNewFileName(fileName);
								pageType.setOldFileName(fileName);
								pageType.setDirection(Direction.NORTH);
								pageType.setIsRotated(false);
								pageType.setComparisonThumbnailFileName(thumbFileName);
								listOfPages.add(pageType);
							}

							final BatchInstanceThread threadList = imService.createCompThumbForImage(batchId, workingDir,
									sListOfTiffFiles, outputParams, compareThumbnailH, compareThumbnailW);

							try {
								threadList.execute();
								// invoke the Classification Image plugin
								String imMetric = batchClassPPService.getPropertyValue(batchClassId,
										ImageMagicKConstants.CLASSIFY_IMAGES_PLUGIN, ImageMagicProperties.CLASSIFY_IMAGES_COMP_METRIC);
								String imFuzz = batchClassPPService.getPropertyValue(batchClassId,
										ImageMagicKConstants.CLASSIFY_IMAGES_PLUGIN, ImageMagicProperties.CLASSIFY_IMAGES_FUZZ_PERCNT);
								String maxVal = batchClassPPService.getPropertyValue(batchClassId,
										ImageMagicKConstants.CLASSIFY_IMAGES_PLUGIN, ImageMagicProperties.CLASSIFY_IMAGES_MAX_RESULTS);

								imService.classifyImagesAPI(maxVal, imMetric, imFuzz, batchId, batchClassId, workingDir, listOfPages);

								// invoke the document assembler plugin
								List<Document> doc = docAssembler.createDocumentAPI(DocumentClassificationFactory.IMAGE, batchClassId,
										listOfPages);
								Documents docs = new Documents();
								docs.getDocument().addAll(doc);
								StreamResult result;
								try {
									result = new StreamResult(resp.getOutputStream());
									batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().marshal(docs, result);
								} catch (final IOException e) {
									respStr = INTERNAL_SERVER_ERROR + e;
									LOGGER.error(SERVER_ERROR_MSG + respStr);
								}
							} catch (final DCMAApplicationException e) {
								threadList.remove();
								respStr = "Error while executing threadpool. Detailed exception is " + e;
								LOGGER.error(SERVER_ERROR_MSG + respStr);
							} catch (final DCMAException e) {
								threadList.remove();
								respStr = "Error while executing threadpool. Detailed exception is " + e;
								LOGGER.error(SERVER_ERROR_MSG + respStr);
							}
						}
					} else {
						respStr = "Improper input to server. Expected only one tiff file. Returning without processing the results.";
						LOGGER.error(SERVER_ERROR_MSG + respStr);
					}
				}
			} catch (final Exception e) {
				respStr = INTERNAL_SERVER_ERROR + e;
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			}
		} else {
			respStr = IMPROPER_INPUT_TO_SERVER;
		}
		if (!workingDir.isEmpty()) {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final IOException ioe) {
				LOGGER.info(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + ioe, ioe);
			}
		}
	}

	/**
	 * To classify Barcode Image.
	 * @param req {@link HttpServletRequest}
	 * @param resp {@link HttpServletResponse}
	 */
	@RequestMapping(value = "/classifyBarcodeImage", method = RequestMethod.POST)
	@ResponseBody
	public void classifyBarcodeImage(final HttpServletRequest req, final HttpServletResponse resp) {
		LOGGER.info("Start processing web service for classifyBarcode.");
		String respStr = WebServiceUtil.EMPTY_STRING;
		String workingDir = WebServiceUtil.EMPTY_STRING;
		InputStream instream = null;
		OutputStream outStream = null;
		if (req instanceof DefaultMultipartHttpServletRequest) {
			try {
				final String webServiceFolderPath = bsService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);

				final DefaultMultipartHttpServletRequest multipartReq = (DefaultMultipartHttpServletRequest) req;
				String batchClassId = WebServiceUtil.EMPTY_STRING;
				for (final Enumeration<String> params = multipartReq.getParameterNames(); params.hasMoreElements();) {
					final String paramName = params.nextElement();
					if (paramName.equalsIgnoreCase("batchClassId")) {
						batchClassId = multipartReq.getParameter(paramName);
						break;
					}
				}
				Map<BarcodeProperties, String> batchClassConfigMap = new HashMap<BarcodeProperties, String>();

				if (batchClassId == null || batchClassId.isEmpty()) {
					respStr = "Batch Class identifier not specified.";
				} else {
					BatchClass batchClass = bcService.getBatchClassByIdentifier(batchClassId);
					if (batchClass == null) {
						respStr = "Batch class with the specified identifier: " + batchClassId + " does not exist.";
						LOGGER.error(SERVER_ERROR_MSG + respStr);
					} else {
						Set<String> loggedInUserRole = getUserRoles(req);
						if (!isBatchClassViewableToUser(batchClassId, loggedInUserRole, isSuperAdmin(req))) {
							respStr = USER_NOT_AUTHORIZED_TO_VIEW_THE_BATCH_CLASS + batchClassId;
							LOGGER.error(SERVER_ERROR_MSG + respStr);
						} else {
							BatchPlugin barcodeReader = batchClassPPService.getPluginProperties(batchClassId,
									ICommonConstants.BARCODE_READER_PLUGIN);
							BatchPlugin docAssemblyPlugin = batchClassPPService.getPluginProperties(batchClassId,
									DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN);
							if (barcodeReader == null || docAssemblyPlugin == null) {
								respStr = "Either Barcode Reader plugin or document assembly plugin does not exist for the specified batch class id: "
										+ batchClassId;
							} else if (docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_BARCODE_CONFIDENCE) == null) {
								respStr = "Incomplete properties of the Document assembler plugin for the specified batch class id:"
										+ batchClassId;
								LOGGER.error(SERVER_ERROR_MSG + respStr);
							} else if (barcodeReader.getPluginConfigurations(BarcodeProperties.BARCODE_VALID_EXTNS) == null
									|| barcodeReader.getPluginConfigurations(BarcodeProperties.BARCODE_READER_TYPES) == null
									|| barcodeReader.getPluginConfigurations(BarcodeProperties.MAX_CONFIDENCE) == null
									|| barcodeReader.getPluginConfigurations(BarcodeProperties.MIN_CONFIDENCE) == null) {
								respStr = "Incomplete properties of the Barcode reader plugin for the specified batch class id: "
										+ batchClassId;
								LOGGER.error(SERVER_ERROR_MSG + respStr);
							}
						}
					}
				}
				if (respStr.isEmpty()) {
					batchClassConfigMap.put(BarcodeProperties.BARCODE_VALID_EXTNS, batchClassPPService.getPropertyValue(batchClassId,
							ICommonConstants.BARCODE_READER_PLUGIN, BarcodeProperties.BARCODE_VALID_EXTNS));
					batchClassConfigMap.put(BarcodeProperties.BARCODE_READER_TYPES, batchClassPPService.getPropertyValue(batchClassId,
							ICommonConstants.BARCODE_READER_PLUGIN, BarcodeProperties.BARCODE_READER_TYPES));
					batchClassConfigMap.put(BarcodeProperties.MAX_CONFIDENCE, batchClassPPService.getPropertyValue(batchClassId,
							ICommonConstants.BARCODE_READER_PLUGIN, BarcodeProperties.MAX_CONFIDENCE));
					batchClassConfigMap.put(BarcodeProperties.MIN_CONFIDENCE, batchClassPPService.getPropertyValue(batchClassId,
							ICommonConstants.BARCODE_READER_PLUGIN, BarcodeProperties.MIN_CONFIDENCE));

					final MultiValueMap<String, MultipartFile> fileMap = multipartReq.getMultiFileMap();

					if (fileMap.size() == 1) {
						String tiffFileName = WebServiceUtil.EMPTY_STRING;
						for (final String fileName : fileMap.keySet()) {
							// only single tiff/tif file is expected as input
							try {
								if ((fileName.toLowerCase(Locale.getDefault()).indexOf(FileType.TIF.getExtension()) > -1 || fileName
										.toLowerCase(Locale.getDefault()).indexOf(FileType.TIFF.getExtension()) > -1)) {

									final MultipartFile multiPartFile = multipartReq.getFile(fileName);
									instream = multiPartFile.getInputStream();
									final File file = new File(workingDir + File.separator + fileName);
									outStream = new FileOutputStream(file);
									final byte buf[] = new byte[WebserviceConstants.BUF];
									int len = instream.read(buf);
									while (len > 0) {
										outStream.write(buf, 0, len);
										len = instream.read(buf);
									}

									if (TIFFUtil.getTIFFPageCount(file.getAbsolutePath()) > 1) {
										respStr = ONLY_ONE_SINGLE_PAGE_TIFF_EXPECTED;
									}
									tiffFileName = file.getName();
									break;
								} else {
									respStr = "Improper input to server. Expected only one tiff file. Returning without processing the results.";
								}
							} finally {
								IOUtils.closeQuietly(instream);
								IOUtils.closeQuietly(outStream);
							}
						}
						if (respStr.isEmpty()) {
							ObjectFactory objectFactory = new ObjectFactory();

							Pages pages = new Pages();
							List<Page> listOfPages = pages.getPage();
							List<Document> xmlDocuments = new ArrayList<Document>();
							Document doc = objectFactory.createDocument();
							xmlDocuments.add(doc);
							doc.setPages(pages);

							Page pageType = objectFactory.createPage();
							pageType.setIdentifier(EphesoftProperty.PAGE.getProperty() + "0");
							pageType.setNewFileName(tiffFileName);
							listOfPages.add(pageType);
							String batchInstanceIdentifier = new File(workingDir).getName() + Math.random();
							barcodeService.extractPageBarCodeAPI(xmlDocuments, batchInstanceIdentifier, workingDir,
									batchClassConfigMap);

							try {
								// invoke the document assembler plugin
								xmlDocuments = docAssembler.createDocumentAPI(DocumentClassificationFactory.BARCODE, batchClassId,
										listOfPages);
								Documents docs = new Documents();
								docs.getDocument().addAll(xmlDocuments);
								StreamResult result;
								try {
									result = new StreamResult(resp.getOutputStream());
									resp.setStatus(HttpServletResponse.SC_OK);
									batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().marshal(docs, result);
								} catch (final IOException e) {
									respStr = INTERNAL_SERVER_ERROR + e;
									LOGGER.error(SERVER_ERROR_MSG + respStr);
								}
							} catch (final DCMAApplicationException e) {
								respStr = "Error while executing plugin. Detailed exception is " + e;
								LOGGER.error(SERVER_ERROR_MSG + respStr);
							}
						}
					} else {
						respStr = IMPROPER_INPUT_ONLY_ONE_HTML_FILE_EXPECTED;
						LOGGER.error(SERVER_ERROR_MSG + respStr);
					}
				}
			} catch (final XmlMappingException xmle) {
				respStr = ERROR_IN_MAPPING_INPUT + xmle;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final DCMAException dcmae) {
				respStr = ERROR_PROCESSING_REQUEST + dcmae;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final Exception e) {
				respStr = INTERNAL_SERVER_ERROR + e;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			}
		} else {
			respStr = "Improper input to server. Expected multipart request. Returing without processing the results.";
			LOGGER.error(SERVER_ERROR_MSG + respStr);
		}

		if (!workingDir.isEmpty()) {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final IOException ioe) {
				LOGGER.info(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + ioe, ioe);
			}
		}
	}

	/**
	 * To classify Hocr.
	 * @param req {@link HttpServletRequest}
	 * @param resp {@link HttpServletResponse}
	 */
	@RequestMapping(value = "/classifyHocr", method = RequestMethod.POST)
	@ResponseBody
	public void classifyHocr(final HttpServletRequest req, final HttpServletResponse resp) {
		LOGGER.info("Start processing web service for classifyHocr.");
		String respStr = WebServiceUtil.EMPTY_STRING;
		String workingDir = WebServiceUtil.EMPTY_STRING;
		InputStream instream = null;
		OutputStream outStream = null;
		if (req instanceof DefaultMultipartHttpServletRequest) {
			try {
				final String webServiceFolderPath = bsService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);

				final DefaultMultipartHttpServletRequest multipartReq = (DefaultMultipartHttpServletRequest) req;
				String batchClassId = WebServiceUtil.EMPTY_STRING;
				for (final Enumeration<String> params = multipartReq.getParameterNames(); params.hasMoreElements();) {
					final String paramName = params.nextElement();
					if (paramName.equalsIgnoreCase("batchClassId")) {
						batchClassId = multipartReq.getParameter(paramName);
						break;
					}
				}
				Map<LuceneProperties, String> batchClassConfigMap = new HashMap<LuceneProperties, String>();

				if (batchClassId == null || batchClassId.isEmpty()) {
					respStr = "Batch Class identifier not specified.";
					LOGGER.error(SERVER_ERROR_MSG + respStr);
				} else {
					BatchClass batchClass = bcService.getBatchClassByIdentifier(batchClassId);
					if (batchClass == null) {
						respStr = "Batch class with the specified identifier : " + batchClassId + " does not exist.";
						LOGGER.error(SERVER_ERROR_MSG + respStr);
					} else {
						Set<String> loggedInUserRole = getUserRoles(req);
						if (!isBatchClassViewableToUser(batchClassId, loggedInUserRole, isSuperAdmin(req))) {
							respStr = USER_NOT_AUTHORIZED_TO_VIEW_THE_BATCH_CLASS + batchClassId;
							LOGGER.error(SERVER_ERROR_MSG + respStr);
						} else {
							BatchPlugin searchClassPlugin = batchClassPPService.getPluginProperties(batchClassId,
									ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN);
							BatchPlugin docAssemblyPlugin = batchClassPPService.getPluginProperties(batchClassId,
									DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN);
							if (searchClassPlugin == null || docAssemblyPlugin == null) {
								respStr = "Either Search Classification plugin or document assembly plugin does not exist for the specified batch class id: "
										+ batchClassId;
							} else if (docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP_MP_LP) == null
									|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP) == null
									|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_MP) == null
									|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_LP) == null
									|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP_LP) == null
									|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP_MP) == null
									|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_MP_LP) == null) {
								respStr = "Incomplete properties of the Document assembler plugin for the specified batch class id :"
										+ batchClassId;
								LOGGER.error(SERVER_ERROR_MSG + respStr);
							} else if (searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_VALID_EXTNS) == null
									|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_INDEX_FIELDS) == null
									|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_STOP_WORDS) == null
									|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_MIN_TERM_FREQ) == null
									|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_MIN_DOC_FREQ) == null
									|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_MIN_WORD_LENGTH) == null
									|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_MAX_QUERY_TERMS) == null
									|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_TOP_LEVEL_FIELD) == null
									|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_NO_OF_PAGES) == null
									|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_MAX_RESULT_COUNT) == null
									|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_FIRST_PAGE_CONF_WEIGHTAGE) == null
									|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_MIDDLE_PAGE_CONF_WEIGHTAGE) == null
									|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_LAST_PAGE_CONF_WEIGHTAGE) == null) {
								respStr = "Incomplete properties of the Search Classification plugin for the specified batch class id :"
										+ batchClassId;
								LOGGER.error(SERVER_ERROR_MSG + respStr);
							}
						}
					}
				}
				if (respStr.isEmpty()) {
					batchClassConfigMap.put(LuceneProperties.LUCENE_VALID_EXTNS, batchClassPPService.getPropertyValue(batchClassId,
							ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_VALID_EXTNS));
					batchClassConfigMap.put(LuceneProperties.LUCENE_INDEX_FIELDS, batchClassPPService.getPropertyValue(batchClassId,
							ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_INDEX_FIELDS));
					batchClassConfigMap.put(LuceneProperties.LUCENE_STOP_WORDS, batchClassPPService.getPropertyValue(batchClassId,
							ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_STOP_WORDS));
					batchClassConfigMap.put(LuceneProperties.LUCENE_MIN_TERM_FREQ, batchClassPPService.getPropertyValue(batchClassId,
							ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_MIN_TERM_FREQ));
					batchClassConfigMap.put(LuceneProperties.LUCENE_MIN_DOC_FREQ, batchClassPPService.getPropertyValue(batchClassId,
							ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_MIN_DOC_FREQ));
					batchClassConfigMap.put(LuceneProperties.LUCENE_MIN_WORD_LENGTH, batchClassPPService.getPropertyValue(
							batchClassId, ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_MIN_WORD_LENGTH));
					batchClassConfigMap.put(LuceneProperties.LUCENE_MAX_QUERY_TERMS, batchClassPPService.getPropertyValue(
							batchClassId, ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_MAX_QUERY_TERMS));
					batchClassConfigMap.put(LuceneProperties.LUCENE_TOP_LEVEL_FIELD, batchClassPPService.getPropertyValue(
							batchClassId, ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_TOP_LEVEL_FIELD));
					batchClassConfigMap.put(LuceneProperties.LUCENE_NO_OF_PAGES, batchClassPPService.getPropertyValue(batchClassId,
							ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_NO_OF_PAGES));
					batchClassConfigMap.put(LuceneProperties.LUCENE_MAX_RESULT_COUNT, batchClassPPService.getPropertyValue(
							batchClassId, ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_MAX_RESULT_COUNT));
					batchClassConfigMap.put(LuceneProperties.LUCENE_FIRST_PAGE_CONF_WEIGHTAGE, batchClassPPService.getPropertyValue(
							batchClassId, ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN,
							LuceneProperties.LUCENE_FIRST_PAGE_CONF_WEIGHTAGE));
					batchClassConfigMap.put(LuceneProperties.LUCENE_MIDDLE_PAGE_CONF_WEIGHTAGE, batchClassPPService.getPropertyValue(
							batchClassId, ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN,
							LuceneProperties.LUCENE_MIDDLE_PAGE_CONF_WEIGHTAGE));
					batchClassConfigMap.put(LuceneProperties.LUCENE_LAST_PAGE_CONF_WEIGHTAGE, batchClassPPService.getPropertyValue(
							batchClassId, ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN,
							LuceneProperties.LUCENE_LAST_PAGE_CONF_WEIGHTAGE));

					final MultiValueMap<String, MultipartFile> fileMap = multipartReq.getMultiFileMap();

					if (fileMap.size() == 1) {
						String hocrFileName = "";
						for (final String fileName : fileMap.keySet()) {
							// only single html file is expected as input
							try {
								if (fileName.toLowerCase(Locale.getDefault()).indexOf(FileType.HTML.getExtension()) > -1) {
									// only HTML file is expected
									hocrFileName = fileName;
									final MultipartFile multiPartFile = multipartReq.getFile(fileName);
									instream = multiPartFile.getInputStream();
									final File file = new File(workingDir + File.separator + fileName);
									outStream = new FileOutputStream(file);
									final byte buf[] = new byte[WebServiceUtil.bufferSize];
									int len = instream.read(buf);
									while (len > 0) {
										outStream.write(buf, 0, len);
										len = instream.read(buf);
									}

									break;
								} else {
									respStr = IMPROPER_INPUT_ONLY_ONE_HTML_FILE_EXPECTED;
									LOGGER.error(SERVER_ERROR_MSG + respStr);
								}
							} finally {
								IOUtils.closeQuietly(instream);
								IOUtils.closeQuietly(outStream);
							}
						}
						if (respStr.isEmpty()) {
							ObjectFactory objectFactory = new ObjectFactory();

							Pages pages = new Pages();
							List<Page> listOfPages = pages.getPage();
							List<Document> xmlDocuments = new ArrayList<Document>();
							Document doc = objectFactory.createDocument();
							xmlDocuments.add(doc);
							doc.setPages(pages);
							String fileName = workingDir + File.separator + hocrFileName;

							// generate hocr file from html file.
							HocrPages hocrPages = new HocrPages();
							List<HocrPage> hocrPageList = hocrPages.getHocrPage();
							HocrPage hocrPage = new HocrPage();
							String pageID = WebServiceUtil.PG0;
							hocrPage.setPageID(pageID);
							hocrPageList.add(hocrPage);
							bsService.hocrGenerationAPI(workingDir, WebServiceUtil.PG0, fileName, hocrPage);

							Page pageType = objectFactory.createPage();
							pageType.setIdentifier(EphesoftProperty.PAGE.getProperty() + "0");
							pageType.setHocrFileName(hocrFileName);
							listOfPages.add(pageType);
							scService.generateConfidenceScoreAPI(xmlDocuments, hocrPages, workingDir, batchClassConfigMap,
									batchClassId);

							try {
								// invoke the document assembler plugin
								xmlDocuments = docAssembler.createDocumentAPI(DocumentClassificationFactory.SEARCHCLASSIFICATION,
										batchClassId, listOfPages);
								Documents docs = new Documents();
								docs.getDocument().addAll(xmlDocuments);
								StreamResult result;
								try {
									result = new StreamResult(resp.getOutputStream());
									resp.setStatus(HttpServletResponse.SC_OK);
									batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().marshal(docs, result);
								} catch (final IOException e) {
									respStr = INTERNAL_SERVER_ERROR + e;
									LOGGER.error(SERVER_ERROR_MSG + respStr);
								}
							} catch (final DCMAApplicationException e) {
								respStr = "Error while executing plugin. Detailed exception is " + e;
								LOGGER.error(SERVER_ERROR_MSG + respStr);
							}
						}
					} else {
						respStr = IMPROPER_INPUT_ONLY_ONE_HTML_FILE_EXPECTED;
						LOGGER.error(SERVER_ERROR_MSG + respStr);
					}
				}
			} catch (final XmlMappingException xmle) {
				respStr = ERROR_IN_MAPPING_INPUT + xmle;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final DCMAException dcmae) {
				respStr = ERROR_PROCESSING_REQUEST + dcmae;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final Exception e) {
				respStr = INTERNAL_SERVER_ERROR + e;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}

			}
		} else {
			respStr = "Improper input to server. Expected multipart request. Returing without processing the results.";
			LOGGER.error(SERVER_ERROR_MSG + respStr);
		}

		if (!workingDir.isEmpty()) {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final IOException ioe) {
				LOGGER.info(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + ioe, ioe);
			}
		}
	}

	/**
	 * To create Multi Page File.
	 * @param req {@link HttpServletRequest}
	 * @param resp {@link HttpServletResponse}
	 */
	@RequestMapping(value = "/createMultiPageFile", method = RequestMethod.POST)
	@ResponseBody
	public void createMultiPageFile(final HttpServletRequest req, final HttpServletResponse resp) {
		LOGGER.info("Start processing web service for createMultiPageFile.");
		String respStr = WebServiceUtil.EMPTY_STRING;
		String workingDir = WebServiceUtil.EMPTY_STRING;
		InputStream instream = null;
		OutputStream outStream = null;
		if (req instanceof DefaultMultipartHttpServletRequest) {
			try {
				final String webServiceFolderPath = bsService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
				final String outputDir = WebServiceUtil.createWebServiceOutputDir(workingDir);

				final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) req;
				final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();
				String xmlFileName = WebServiceUtil.EMPTY_STRING;
				List<File> fileList = new ArrayList<File>();
				for (final String fileName : fileMap.keySet()) {
					try {
						if (fileName.endsWith(FileType.XML.getExtensionWithDot())
								|| fileName.endsWith(FileType.TIF.getExtensionWithDot())
								|| fileName.endsWith(FileType.TIFF.getExtensionWithDot())) {
							final File file = new File(workingDir + File.separator + fileName);
							if (fileName.endsWith(FileType.XML.getExtensionWithDot())) {
								xmlFileName = fileName;
							} else {
								fileList.add(file);
							}
							final MultipartFile multiPartFile = multiPartRequest.getFile(fileName);
							instream = multiPartFile.getInputStream();
							outStream = new FileOutputStream(file);
							final byte[] buf = new byte[WebServiceUtil.bufferSize];
							int len = instream.read(buf);
							while (len > 0) {
								outStream.write(buf, 0, len);
								len = instream.read(buf);
							}

						} else {
							respStr = "Expected only tif, tiff files.";
							LOGGER.error(SERVER_ERROR_MSG + respStr);
						}
					} finally {
						IOUtils.closeQuietly(instream);
						IOUtils.closeQuietly(outStream);
					}
				}
				if (respStr.isEmpty()) {
					final File xmlFile = new File(workingDir + File.separator + xmlFileName);
					final FileInputStream inputStream = new FileInputStream(xmlFile);
					Source source = XMLUtil.createSourceFromStream(inputStream);
					final WebServiceParams webServiceParams = (WebServiceParams) batchSchemaDao.getJAXB2Template()
							.getJaxb2Marshaller().unmarshal(source);
					if (webServiceParams.getParams() == null || webServiceParams.getParams().getParam() == null
							|| webServiceParams.getParams().getParam().isEmpty()) {
						FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
						respStr = IMPROPER_XML_PARAMETER;
						LOGGER.error(SERVER_ERROR_MSG + respStr);
					} else {
						List<Param> paramList = webServiceParams.getParams().getParam();
						String imageProcessingAPI = WebServiceUtil.EMPTY_STRING;
						String pdfOptimizationParams = WebServiceUtil.EMPTY_STRING;
						String multipageTifSwitch = WebServiceUtil.EMPTY_STRING;
						String pdfOptimizationSwitch = WebServiceUtil.EMPTY_STRING, ghostscriptPdfParameters = WebServiceUtil.EMPTY_STRING;
						for (final Param param : paramList) {

							if (param.getName().equalsIgnoreCase(WebServiceUtil.IMAGE_PROCESSING_API)) {
								imageProcessingAPI = param.getValue();
								continue;
							}
							if (param.getName().equalsIgnoreCase(WebServiceUtil.PDF_OPTIMIZATION_PARAMS)) {
								pdfOptimizationParams = param.getValue();
								continue;
							}
							if (param.getName().equalsIgnoreCase(WebServiceUtil.MULTIPAGE_TIF_SWITCH)) {
								multipageTifSwitch = param.getValue();
								continue;
							}
							if (param.getName().equalsIgnoreCase(WebServiceUtil.PDF_OPTIMIZATION_SWITCH)) {
								pdfOptimizationSwitch = param.getValue();
								continue;
							}
							if (param.getName().equalsIgnoreCase(WebServiceUtil.GHOSTSCRIPT_PDF_PARAMETERS)) {
								ghostscriptPdfParameters = param.getValue();
								continue;
							}
						}
						String results = WebServiceUtil.validateCreateMultiPageFile(ghostscriptPdfParameters, imageProcessingAPI,
								pdfOptimizationSwitch, multipageTifSwitch, pdfOptimizationParams);
						if (!results.isEmpty()) {
							respStr = results;
						} else {
							imService.createMultiPageFilesAPI(ghostscriptPdfParameters, pdfOptimizationParams, multipageTifSwitch,
									imageProcessingAPI, pdfOptimizationSwitch, workingDir, outputDir, fileList, new File(workingDir)
											.getName()
											+ Math.random());
							ServletOutputStream out = null;
							ZipOutputStream zout = null;
							final String zipFileName = WebServiceUtil.SERVEROUTPUTFOLDERNAME;
							resp.setContentType(WebServiceUtil.APPLICATION_X_ZIP);
							resp.setHeader(WebServiceUtil.CONTENT_DISPOSITION, WebServiceUtil.ATTACHMENT_FILENAME + zipFileName
									+ FileType.ZIP.getExtensionWithDot() + NEXT_LINE_STRING);
							try {
								out = resp.getOutputStream();
								zout = new ZipOutputStream(out);
								FileUtils.zipDirectory(outputDir, zout, zipFileName);
								resp.setStatus(HttpServletResponse.SC_OK);
							} catch (final IOException e) {
								respStr = "Unable to process web service request.Please try again." + e;
							} finally {
								IOUtils.closeQuietly(zout);
								IOUtils.closeQuietly(out);
								FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
							}
						}
					}
				}
			} catch (final XmlMappingException xmle) {
				respStr = ERROR_IN_MAPPING_INPUT + xmle;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final DCMAException dcmae) {
				respStr = ERROR_PROCESSING_REQUEST + dcmae;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final Exception e) {
				respStr = INTERNAL_SERVER_ERROR + e;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			}
		} else {
			respStr = IMPROPER_INPUT_TO_SERVER;
			LOGGER.error(SERVER_ERROR_MSG + respStr);
		}
		if (!workingDir.isEmpty()) {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final IOException ioe) {
				LOGGER.info(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + ioe, ioe);
			}
		}
	}

	/**
	 * To extract Field from Hocr.
	 * @param req {@link HttpServletRequest}
	 * @param resp {@link HttpServletResponse}
	 */
	@RequestMapping(value = "/extractFieldFromHocr", method = RequestMethod.POST)
	@ResponseBody
	public void extractFieldFromHocr(final HttpServletRequest req, final HttpServletResponse resp) {
		LOGGER.info("Start processing web service for extractFieldFromHocr.");
		String respStr = WebServiceUtil.EMPTY_STRING;
		String workingDir = WebServiceUtil.EMPTY_STRING;
		InputStream instream = null;
		OutputStream outStream = null;
		if (req instanceof DefaultMultipartHttpServletRequest) {
			try {
				final String webServiceFolderPath = bsService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);

				final DefaultMultipartHttpServletRequest multipartReq = (DefaultMultipartHttpServletRequest) req;
				String fieldValue = WebServiceUtil.EMPTY_STRING;
				for (final Enumeration<String> params = multipartReq.getParameterNames(); params.hasMoreElements();) {
					final String paramName = params.nextElement();
					if (paramName.equalsIgnoreCase("fieldValue")) {
						fieldValue = multipartReq.getParameter(paramName);
						break;
					}
				}

				if (fieldValue == null || fieldValue.isEmpty()) {
					respStr = "Field Value not specified.";
					LOGGER.error(SERVER_ERROR_MSG + respStr);
				}
				if (respStr.isEmpty()) {
					final MultiValueMap<String, MultipartFile> fileMap = multipartReq.getMultiFileMap();

					if (fileMap.size() == 1) {
						String hocrFileName = "";
						for (final String fileName : fileMap.keySet()) {
							// only single html file is expected as input
							try {
								if (fileName.toLowerCase(Locale.getDefault()).indexOf(FileType.HTML.getExtension()) > -1) {
									// only HTML file is expected
									hocrFileName = fileName;
									final MultipartFile multiPartFile = multipartReq.getFile(fileName);
									instream = multiPartFile.getInputStream();
									final File file = new File(workingDir + File.separator + fileName);
									outStream = new FileOutputStream(file);
									final byte buf[] = new byte[WebserviceConstants.BUF];
									int len = instream.read(buf);
									while (len > 0) {
										outStream.write(buf, 0, len);
										len = instream.read(buf);
									}

									break;
								} else {
									respStr = IMPROPER_INPUT_ONLY_ONE_HTML_FILE_EXPECTED;
									LOGGER.error(SERVER_ERROR_MSG + respStr);
								}
							} finally {
								IOUtils.closeQuietly(instream);
								IOUtils.closeQuietly(outStream);
							}
						}
						if (respStr.isEmpty()) {
							String fileName = workingDir + File.separator + hocrFileName;

							// generate hocr file from html file.
							HocrPages hocrPages = new HocrPages();
							List<HocrPage> hocrPageList = hocrPages.getHocrPage();
							HocrPage hocrPage = new HocrPage();
							String pageID = WebServiceUtil.PG0;
							hocrPage.setPageID(pageID);
							hocrPageList.add(hocrPage);
							bsService.hocrGenerationAPI(workingDir, WebServiceUtil.PG0, fileName, hocrPage);

							List<KVExtraction> kvExtractionList = kvFieldService.createKeyValueFieldAPI(fieldValue, hocrPage);

							final KVExtractionFieldPatterns patterns = new KVExtractionFieldPatterns();

							final List<KVExtractionFieldPattern> pattern = patterns.getKVExtractionFieldPattern();
							for (final KVExtraction eachKVExtraction : kvExtractionList) {
								final KVExtractionFieldPattern kvField = new KVExtractionFieldPattern();
								kvField.setDistance(eachKVExtraction.getDistance());
								kvField.setFetchValue(eachKVExtraction.getFetchValue().name());
								kvField.setKeyPattern(eachKVExtraction.getKeyPattern());
								kvField.setLength(eachKVExtraction.getLength());
								if (eachKVExtraction.getLocationType() != null) {
									kvField.setLocation(eachKVExtraction.getLocationType().name());
								}
								kvField.setMultiplier(eachKVExtraction.getMultiplier());
								kvField.setNoOfWords(eachKVExtraction.getNoOfWords() == null ? 0 : eachKVExtraction.getNoOfWords());
								kvField.setValuePattern(eachKVExtraction.getValuePattern());
								kvField.setWidth(eachKVExtraction.getWidth());
								kvField.setXOffset(eachKVExtraction.getXoffset());
								kvField.setYOffset(eachKVExtraction.getYoffset());
								pattern.add(kvField);
							}
							StreamResult result;
							try {
								result = new StreamResult(resp.getOutputStream());
								resp.setStatus(HttpServletResponse.SC_OK);
								batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().marshal(patterns, result);
							} catch (final IOException e) {
								try {
									resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR
											+ e.getMessage());
								} catch (final IOException ioe) {
									LOGGER.info(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + ioe, ioe);
								}
							}
						}
					} else {
						respStr = IMPROPER_INPUT_ONLY_ONE_HTML_FILE_EXPECTED;
						LOGGER.error(SERVER_ERROR_MSG + respStr);
					}
				}
			} catch (final DCMAException dcmae) {
				respStr = ERROR_PROCESSING_REQUEST + dcmae;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final Exception e) {
				respStr = INTERNAL_SERVER_ERROR + e;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			}
		} else {
			respStr = "Improper input to server. Expected multipart request. Returing without processing the results.";
			LOGGER.error(SERVER_ERROR_MSG + respStr);
		}

		if (!workingDir.isEmpty()) {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final IOException ioe) {
				LOGGER.info(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + ioe, ioe);
			}
		}
	}

	/**
	 * To extract Fixed Form.
	 * @param req {@link HttpServletRequest}
	 * @param resp {@link HttpServletResponse}
	 */
	@RequestMapping(value = "/extractFixedForm", method = RequestMethod.POST)
	@ResponseBody
	public void extractFixedForm(final HttpServletRequest req, final HttpServletResponse resp) {
		processFixedFormExtraction(req, resp);
	}

	private void processFixedFormExtraction(final HttpServletRequest req, final HttpServletResponse resp) {
		LOGGER.info("Start processing web service for extract fixed form....");
		String respStr = WebServiceUtil.EMPTY_STRING;
		String workingDir = WebServiceUtil.EMPTY_STRING;
		com.ephesoft.dcma.batch.schema.Documents documents = null;
		InputStream instream = null;
		OutputStream outStream = null;

		if (req instanceof DefaultMultipartHttpServletRequest) {
			try {
				final String webServiceFolderPath = bsService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
				final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) req;

				final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();
				String xmlFileName = WebServiceUtil.EMPTY_STRING;

				if (fileMap.size() != WebserviceConstants.THREE) {
					respStr = "Invalid number of files. We are supposed only 3 files each of type: XML, RSP and tif/tiff/png.";
					LOGGER.error(SERVER_ERROR_MSG + respStr);
				}

				if (respStr.isEmpty()) {
					for (final String fileName : fileMap.keySet()) {
						try {
							if (fileName.endsWith(FileType.XML.getExtensionWithDot())) {
								xmlFileName = fileName;
							}

							final MultipartFile multiPartFile = multiPartRequest.getFile(fileName);
							instream = multiPartFile.getInputStream();
							final File file = new File(workingDir + File.separator + fileName);
							outStream = new FileOutputStream(file);
							final byte[] buf = new byte[WebServiceUtil.bufferSize];
							int len = instream.read(buf);
							while (len > 0) {
								outStream.write(buf, 0, len);
								len = instream.read(buf);
							}
							if (fileName.toLowerCase(Locale.getDefault()).endsWith(FileType.TIF.getExtensionWithDot())
									|| fileName.toLowerCase(Locale.getDefault()).endsWith(FileType.TIFF.getExtensionWithDot())) {
								int pageCount = TIFFUtil.getTIFFPageCount(workingDir + File.separator + fileName);
								if (pageCount > 1) {
									respStr = ONLY_ONE_SINGLE_PAGE_TIFF_EXPECTED;
									break;
								}
							}
						} finally {
							IOUtils.closeQuietly(instream);
							IOUtils.closeQuietly(outStream);
						}
					}

					if (xmlFileName.isEmpty()) {
						respStr = "XML file is not found. Returning without processing the results.";
						LOGGER.error(SERVER_ERROR_MSG + respStr);
					}

					if (respStr.isEmpty()) {
						final File xmlFile = new File(workingDir + File.separator + xmlFileName);
						final FileInputStream inputStream = new FileInputStream(xmlFile);

						Source source = null;
						try {
							source = XMLUtil.createSourceFromStream(inputStream);
							if (respStr.isEmpty()) {
								final WebServiceParams webServiceParams = (WebServiceParams) batchSchemaDao.getJAXB2Template()
										.getJaxb2Marshaller().unmarshal(source);

								List<Param> paramList = null;

								if (webServiceParams != null && webServiceParams.getParams() != null) {
									paramList = webServiceParams.getParams().getParam();
								} else {
									FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
									respStr = "Invalid xml file mapped. Returning without processing the results.";
									LOGGER.error(SERVER_ERROR_MSG + respStr);
								}

								if (respStr.isEmpty()) {
									String colorSwitch = WebServiceUtil.EMPTY_STRING;
									String projectFile = WebServiceUtil.EMPTY_STRING;
									if (paramList == null || paramList.size() <= 0) {
										respStr = "Improper input to server. Returning without processing the results.";
									} else {
										for (final Param param : paramList) {
											if (param.getName().equalsIgnoreCase(WebServiceUtil.COLOR_SWITCH)) {
												colorSwitch = param.getValue();
												LOGGER.info("Color Switch for recostar is :" + colorSwitch);
												continue;
											}
											if (param.getName().equalsIgnoreCase(WebServiceUtil.PROJECT_FILE)) {
												projectFile = param.getValue();
												LOGGER.info("Project file for recostar is :" + projectFile);
												continue;
											}
										}
									}

									String[] fileNames = null;
									final File file = new File(workingDir);
									respStr = WebServiceUtil.validateExtractFixedFormAPI(workingDir, projectFile, colorSwitch);

									if (respStr.isEmpty()) {
										if (colorSwitch.equalsIgnoreCase(WebServiceUtil.ON_STRING)) {
											String[] tifFileNames = file.list(new CustomFileFilter(false, FileType.TIF
													.getExtensionWithDot(), FileType.TIFF.getExtensionWithDot()));
											// generate png file for each tiff/tif file
											for (String tifFile : tifFileNames) {
												imService.generatePNGForImage(new File(workingDir + File.separator + tifFile));
											}
											LOGGER.info("Picking up the png file for processing.");
											fileNames = file.list(new CustomFileFilter(false, FileType.PNG.getExtensionWithDot()));
										} else {
											LOGGER.info("Picking up the tif file for processing.");
											fileNames = file.list(new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(),
													FileType.TIFF.getExtensionWithDot()));
										}
										LOGGER.info("Number of file is:" + fileNames.length);

										if (fileNames != null && fileNames.length > 0) {
											for (final String fileName : fileNames) {
												LOGGER.info("File processing for recostar is :" + fileName);
												documents = recostarExtractionService.extractDocLevelFieldsForRspFile(projectFile,
														workingDir, colorSwitch, fileName, workingDir);
											}
										} else {
											if (colorSwitch.equalsIgnoreCase(WebServiceUtil.ON_STRING)) {
												respStr = "Image file of png type is not found for processing";
												LOGGER.error(SERVER_ERROR_MSG + respStr);
											} else {
												respStr = "Image file of tif type is not found for processing";
												LOGGER.error(SERVER_ERROR_MSG + respStr);
											}
										}

										if (respStr.isEmpty() && documents != null) {
											StreamResult result = new StreamResult(resp.getOutputStream());
											batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().marshal(documents, result);
										}
									}
								}
							}
						} catch (final DCMAException e) {
							respStr = "Error occuring while creating OCR file using recostar. Please try later. " + e;
							LOGGER.error(SERVER_ERROR_MSG + respStr);
						} catch (final Exception e) {
							respStr = "Improper input to server. Returning without processing the results." + e;
							LOGGER.error(SERVER_ERROR_MSG + respStr);
						}
					}
				}
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			} catch (Exception e) {
				respStr = INTERNAL_SERVER_ERROR + e;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			}

		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final IOException ioe) {
				LOGGER.info(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + ioe, ioe);
			}
		}
	}

	/**
	 * To restart all Batch Instance.
	 * @param req {@link HttpServletRequest}
	 * @param resp {@link HttpServletResponse}
	 * @return {@link String}
	 */
	@RequestMapping(value = "/restartAllBatchInstance", method = RequestMethod.GET)
	@ResponseBody
	public String restartAllBatchInstance(final HttpServletResponse resp, final HttpServletRequest req) {
		StringBuffer isSuccess = new StringBuffer(WebServiceUtil.EMPTY_STRING);
		StringBuffer isFailure = new StringBuffer(WebServiceUtil.EMPTY_STRING);
		Set<String> loggedInUserRole = getUserRoles(req);
		List<BatchInstanceStatus> batchStatusList = new ArrayList<BatchInstanceStatus>();
		batchStatusList.add(BatchInstanceStatus.READY_FOR_REVIEW);
		batchStatusList.add(BatchInstanceStatus.READY_FOR_VALIDATION);
		boolean isSuperAdmin = isSuperAdmin(req);
		List<BatchInstance> batchInstanceList = null;
		if (isSuperAdmin) {
			batchInstanceList = biService.getBatchInstanceByStatusList(batchStatusList);
		} else {
			batchInstanceList = biService.getBatchInstancesForStatusPriority(batchStatusList, null, loggedInUserRole);
		}
		boolean successFlag = true;
		final boolean isZipSwitchOn = bsService.isZipSwitchOn();
		if (batchInstanceList != null && batchInstanceList.size() > 0) {
			for (BatchInstance batchInstance : batchInstanceList) {
				String batchInstanceIdentifier = batchInstance.getIdentifier();
				LOGGER.info("Restarting batch instance : " + batchInstanceIdentifier);
				String activityName = workflowService.getActiveModule(batchInstance);
				if (activityName != null) {
					int indexOf = activityName.indexOf('.');
					indexOf = indexOf == -1 ? activityName.length() : indexOf;
					String moduleName = activityName.substring(0, indexOf);
					try {
						String batchClassIdentifier = biService.getBatchClassIdentifier(batchInstanceIdentifier);
						isSuccess.append(processRestartingBatchInternal(batchInstanceIdentifier, moduleName, isSuccess.toString(),
								batchInstance, batchClassIdentifier, isZipSwitchOn, activityName));
						if (isSuccess.toString().isEmpty()) {
							isSuccess.append("Successfully restarted batch instance identifiers are: ");
						} else {
							isSuccess.append(", ");
						}
						isSuccess.append(batchInstanceIdentifier);
					} catch (Exception e) {
						successFlag = false;
						if (isFailure.toString().isEmpty()) {
							isFailure.append("Error in restarting following batch instance identifiers: ");
						} else {
							isFailure.append(", ");
						}
						isFailure.append(batchInstanceIdentifier);
						LOGGER.error("Error while restarting batch instance: " + batchInstanceIdentifier);
					}
				} else {
					successFlag = false;
					if (isFailure.toString().isEmpty()) {
						isFailure.append("Error in restarting batch instance identifiers are : ");
					} else {
						isFailure.append(", ");
					}
					isFailure.append(batchInstanceIdentifier);
				}
			}

		} else {
			isSuccess = new StringBuffer("No batch instance found for restarting.");
		}
		isSuccess.append('\n');
		isSuccess.append(isFailure);

		if (!successFlag) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, isSuccess.toString());
			} catch (final IOException ioe) {
				LOGGER.info(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + ioe, ioe);
			}
		}
		return isSuccess.toString();
	}

	/**
	 * To extract Fuzzy DB.
	 * @param req {@link HttpServletRequest}
	 * @param resp {@link HttpServletResponse}
	 */
	@RequestMapping(value = "/extractFuzzyDB", method = RequestMethod.POST)
	@ResponseBody
	public void extractFuzzyDB(final HttpServletRequest req, final HttpServletResponse resp) {
		processFuzzyDbExtraction(req, resp);
	}

	private void processFuzzyDbExtraction(final HttpServletRequest req, final HttpServletResponse resp) {
		LOGGER.info("Start processing web service for extract fuzzy DB for given HOCR file");
		String respStr = "";
		String workingDir = "";
		Documents documents = null;
		InputStream instream = null;
		OutputStream outStream = null;
		if (req instanceof DefaultMultipartHttpServletRequest) {
			try {
				final String webServiceFolderPath = bsService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);

				final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) req;

				final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();
				String htmlFile = WebServiceUtil.EMPTY_STRING;
				if (fileMap.size() == 1) {
					for (final String fileName : fileMap.keySet()) {
						try {
							if (fileName.endsWith(FileType.HTML.getExtensionWithDot())) {
								htmlFile = fileName;
							} else {
								respStr = "Invalid file. Please passed the valid html file";
								LOGGER.error(SERVER_ERROR_MSG + respStr);
								break;
							}
							final MultipartFile multiPartFile = multiPartRequest.getFile(fileName);
							instream = multiPartFile.getInputStream();
							final File file = new File(workingDir + File.separator + fileName);
							outStream = new FileOutputStream(file);
							final byte[] buf = new byte[WebServiceUtil.bufferSize];
							int len = instream.read(buf);
							while (len > 0) {
								outStream.write(buf, 0, len);
								len = instream.read(buf);
							}
						} finally {
							IOUtils.closeQuietly(instream);
							IOUtils.closeQuietly(outStream);
						}

					}
				} else {
					respStr = "Invalid number of files. We are supposed only one file";
					LOGGER.error(SERVER_ERROR_MSG + respStr);
				}

				String batchClassIdentifier = WebServiceUtil.EMPTY_STRING;
				String documentType = WebServiceUtil.EMPTY_STRING;
				String hocrFileName = WebServiceUtil.EMPTY_STRING;
				for (final Enumeration<String> params = multiPartRequest.getParameterNames(); params.hasMoreElements();) {
					final String paramName = params.nextElement();
					if (paramName.equalsIgnoreCase("documentType")) {
						documentType = multiPartRequest.getParameter(paramName);
						LOGGER.info("Value for documentType parameter is : " + documentType);
						continue;
					}
					if (paramName.equalsIgnoreCase("batchClassIdentifier")) {
						batchClassIdentifier = multiPartRequest.getParameter(paramName);
						LOGGER.info("Value for batchClassIdentifier parameter is : " + batchClassIdentifier);
						continue;
					}
					if (paramName.equalsIgnoreCase("hocrFile")) {
						hocrFileName = multiPartRequest.getParameter(paramName);
						LOGGER.info("Value for hocrFile parameter is : " + hocrFileName);
						continue;
					}
				}

				if (!hocrFileName.equalsIgnoreCase(htmlFile)) {
					respStr = "Please pass the valid hocr File.";
					LOGGER.error(SERVER_ERROR_MSG + respStr);
				}
				if (respStr.isEmpty()) {
					respStr = WebServiceUtil.validateExtractFuzzyDBAPI(workingDir, hocrFileName, batchClassIdentifier, documentType);
					if (respStr.isEmpty()) {
						BatchClass batchClass = bcService.getBatchClassByIdentifier(batchClassIdentifier);
						if (batchClass == null) {
							respStr = "Please enter valid batch class identifier";
						} else {
							Set<String> loggedInUserRole = getUserRoles(req);
							if (!isBatchClassViewableToUser(batchClassIdentifier, loggedInUserRole, isSuperAdmin(req))) {
								respStr = USER_NOT_AUTHORIZED_TO_VIEW_THE_BATCH_CLASS + batchClassIdentifier;
								LOGGER.error(SERVER_ERROR_MSG + respStr);
							} else {
								BatchPlugin fuzzyDBPlugin = batchClassPPService.getPluginProperties(batchClassIdentifier, "FUZZYDB");
								if (fuzzyDBPlugin == null || fuzzyDBPlugin.getPropertiesSize() == 0) {
									respStr = "Fuzzy DB plugin is not configured for batch class : " + batchClassIdentifier
											+ " . Please select proper batch class";
									LOGGER.error(SERVER_ERROR_MSG + respStr);
								} else if (fuzzyDBPlugin.getPluginConfigurations(FuzzyDBProperties.FUZZYDB_STOP_WORDS) == null
										|| fuzzyDBPlugin.getPluginConfigurations(FuzzyDBProperties.FUZZYDB_MIN_TERM_FREQ) == null
										|| fuzzyDBPlugin.getPluginConfigurations(FuzzyDBProperties.FUZZYDB_MIN_WORD_LENGTH) == null
										|| fuzzyDBPlugin.getPluginConfigurations(FuzzyDBProperties.FUZZYDB_MAX_QUERY_TERMS) == null
										|| fuzzyDBPlugin.getPluginConfigurations(FuzzyDBProperties.FUZZYDB_NO_OF_PAGES) == null
										|| fuzzyDBPlugin.getPluginConfigurations(FuzzyDBProperties.FUZZYDB_DB_DRIVER) == null
										|| fuzzyDBPlugin.getPluginConfigurations(FuzzyDBProperties.FUZZYDB_CONNECTION_URL) == null
										|| fuzzyDBPlugin.getPluginConfigurations(FuzzyDBProperties.FUZZYDB_DB_USER_NAME) == null
										|| fuzzyDBPlugin.getPluginConfigurations(FuzzyDBProperties.FUZZYDB_DB_PASSWORD) == null
										|| fuzzyDBPlugin.getPluginConfigurations(FuzzyDBProperties.FUZZYDB_THRESHOLD_VALUE) == null) {
									respStr = "Incomplete properties of the Fuzzy DB plugin for the specified batch class id: "
											+ batchClassIdentifier;
									LOGGER.error(SERVER_ERROR_MSG + respStr);
								}
							}
						}

						if (respStr.isEmpty()) {
							List<com.ephesoft.dcma.da.domain.FieldType> allFdTypes = fieldTypeService
									.getFdTypeByDocTypeNameForBatchClass(documentType, batchClassIdentifier);

							if (allFdTypes == null || allFdTypes.size() == 0) {
								respStr = "Document Level Fields doesn't exist for document type: " + documentType;
								LOGGER.error(SERVER_ERROR_MSG + respStr);
							}
						}

						if (respStr.isEmpty()) {
							try {
								HocrPages hocrPages = new HocrPages();
								List<HocrPage> hocrPageList = hocrPages.getHocrPage();
								HocrPage hocrPage = new HocrPage();
								String pageID = WebServiceUtil.PG0;
								hocrPage.setPageID(pageID);
								hocrPageList.add(hocrPage);
								bsService.hocrGenerationAPI(workingDir, pageID, workingDir + File.separator + hocrFileName, hocrPage);
								documents = fuzzyDBSearchService.extractDataBaseFields(batchClassIdentifier, documentType, hocrPages);
								if (documents != null) {
									StreamResult result = new StreamResult(resp.getOutputStream());
									batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().marshal(documents, result);
								}
							} catch (final DCMAException e) {
								respStr = "Exception while extracting field using fuzzy db" + e;
								LOGGER.error(SERVER_ERROR_MSG + respStr);
							}
						}
					}
				}
			} catch (final XmlMappingException xmle) {
				respStr = ERROR_IN_MAPPING_INPUT + xmle;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final Exception e) {
				respStr = INTERNAL_SERVER_ERROR + e;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			}
		} else {
			respStr = IMPROPER_INPUT_TO_SERVER;
			LOGGER.error(SERVER_ERROR_MSG + respStr);
		}
		if (!respStr.isEmpty()) {
			try {
				FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final IOException ioe) {
				LOGGER.info(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + ioe, ioe);
			}
		}
	}

	/**
	 * To convert Tiff to Pdf.
	 * @param req {@link HttpServletRequest}
	 * @param resp {@link HttpServletResponse}
	 */
	@RequestMapping(value = "/convertTiffToPdf", method = RequestMethod.POST)
	@ResponseBody
	public void convertTiffToPdf(final HttpServletRequest req, final HttpServletResponse resp) {
		LOGGER.info("Start processing web service for extract fuzzy DB for given HOCR file");
		String respStr = "";
		String workingDir = "";
		if (req instanceof DefaultMultipartHttpServletRequest) {
			InputStream instream = null;
			OutputStream outStream = null;
			try {
				final String webServiceFolderPath = bsService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
				final String outputDir = WebServiceUtil.createWebServiceOutputDir(workingDir);

				final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) req;

				final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();
				if (!fileMap.keySet().isEmpty()) {
					for (final String fileName : fileMap.keySet()) {
						try {
							if (!fileName.toLowerCase(Locale.getDefault()).endsWith(FileType.TIF.getExtensionWithDot())
									&& !fileName.toLowerCase(Locale.getDefault()).endsWith(FileType.TIFF.getExtensionWithDot())) {
								respStr = "Invalid file. Please pass the valid Tif/Tiff file";
								LOGGER.error(SERVER_ERROR_MSG + respStr);
								break;
							}
							final MultipartFile multiPartFile = multiPartRequest.getFile(fileName);
							instream = multiPartFile.getInputStream();
							final File file = new File(workingDir + File.separator + fileName);
							outStream = new FileOutputStream(file);
							final byte[] buf = new byte[WebServiceUtil.bufferSize];
							int len = instream.read(buf);
							while (len > 0) {
								outStream.write(buf, 0, len);
								len = instream.read(buf);
							}
						} finally {
							IOUtils.closeQuietly(instream);
							IOUtils.closeQuietly(outStream);
						}
					}
				} else {
					respStr = "Please passed the input files for processing";
					LOGGER.error(SERVER_ERROR_MSG + respStr);
				}

				if (respStr.isEmpty()) {
					String inputParams = WebServiceUtil.EMPTY_STRING;
					String outputParams = WebServiceUtil.EMPTY_STRING;
					String pdfGeneratorEngine = WebServiceUtil.EMPTY_STRING;
					for (final Enumeration<String> params = multiPartRequest.getParameterNames(); params.hasMoreElements();) {
						final String paramName = params.nextElement();
						if (paramName.equalsIgnoreCase(WebServiceUtil.INPUT_PARAMS)) {
							inputParams = multiPartRequest.getParameter(paramName);
							LOGGER.info("Value for batchClassIdentifier parameter is " + inputParams);
							continue;
						}
						if (paramName.equalsIgnoreCase(WebServiceUtil.OUTPUT_PARAMS)) {
							outputParams = multiPartRequest.getParameter(paramName);
							LOGGER.info("Value for hocrFile parameter is " + outputParams);
							continue;
						}
						if (paramName.equalsIgnoreCase(WebServiceUtil.PDF_GENERATOR_ENGINE)) {
							pdfGeneratorEngine = multiPartRequest.getParameter(paramName);
							LOGGER.info("Value for hocrFile parameter is " + pdfGeneratorEngine);
							continue;
						}
					}

					respStr = WebServiceUtil.validateConvertTiffToPdfAPI(pdfGeneratorEngine, inputParams, outputParams);

					if (respStr.isEmpty()) {
						Set<String> outputFileList = new HashSet<String>();
						File file = new File(workingDir);
						String[] fileList = file.list(new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(), FileType.TIFF
								.getExtensionWithDot()));

						BatchInstanceThread batchInstanceThread = new BatchInstanceThread(workingDir);

						for (String inputFile : fileList) {
							String[] fileArray = new String[2];
							String outputFile = inputFile.substring(0, inputFile.lastIndexOf(WebServiceUtil.DOT))
									+ FileType.PDF.getExtensionWithDot();
							fileArray[0] = workingDir + File.separator + inputFile;
							fileArray[1] = workingDir + File.separator + outputFile;
							outputFileList.add(outputFile);
							imService.createTifToPDF(pdfGeneratorEngine, fileArray, batchInstanceThread, inputParams, outputParams);
						}

						batchInstanceThread.execute();

						for (String outputFile : outputFileList) {
							FileUtils.copyFile(new File(workingDir + File.separator + outputFile), new File(outputDir + File.separator
									+ outputFile));
						}

						ServletOutputStream out = null;
						ZipOutputStream zout = null;
						final String zipFileName = WebServiceUtil.SERVEROUTPUTFOLDERNAME;
						resp.setContentType(WebServiceUtil.APPLICATION_X_ZIP);
						resp.setHeader(WebServiceUtil.CONTENT_DISPOSITION, WebServiceUtil.ATTACHMENT_FILENAME + zipFileName
								+ FileType.ZIP.getExtensionWithDot() + NEXT_LINE_STRING);
						try {
							out = resp.getOutputStream();
							zout = new ZipOutputStream(out);
							FileUtils.zipDirectory(outputDir, zout, zipFileName);
							resp.setStatus(HttpServletResponse.SC_OK);
						} catch (final IOException e) {
							resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
									"Error in creating output zip file.Please try again." + e.getMessage());
						} finally {
							IOUtils.closeQuietly(zout);
							IOUtils.closeQuietly(out);
							FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
						}
					}
				}
			} catch (final XmlMappingException xmle) {
				respStr = ERROR_IN_MAPPING_INPUT + xmle;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final DCMAException dcmae) {
				respStr = ERROR_PROCESSING_REQUEST + dcmae;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final Exception e) {
				respStr = INTERNAL_SERVER_ERROR + e;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			}
		} else {
			respStr = IMPROPER_INPUT_TO_SERVER;
		}
		if (!respStr.isEmpty()) {
			try {
				FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final IOException ioe) {
				LOGGER.info(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + ioe, ioe);
			}
		}
	}

	private Boolean isSuperAdmin(HttpServletRequest req) {
		Boolean isSuperAdmin = Boolean.FALSE;
		Set<String> allSuperAdminGroups = userConnectivityService.getAllSuperAdminGroups();
		for (String superAdminGroup : allSuperAdminGroups) {
			if (req.isUserInRole(superAdminGroup)) {
				isSuperAdmin = Boolean.TRUE;
				break;
			}
		}
		return isSuperAdmin;
	}

	private Set<String> getUserRoles(HttpServletRequest req) {
		LOGGER.info("========Getting the user roles=========");
		Set<String> userGroups = null;
		Set<String> allGroups = userConnectivityService.getAllGroups();
		if (null == allGroups || allGroups.isEmpty()) {
			LOGGER.error("No groups fetched from Authenticated User.....All groups is empty.Returning null");
		} else {
			userGroups = new HashSet<String>();
			for (String group : allGroups) {
				if (null != group && !group.isEmpty() && req.isUserInRole(group)) {
					LOGGER.info("Added group is: " + group);
					userGroups.add(group);
				}
			}

			LOGGER.info("List of fetched user roles:");
			for (String userRole : userGroups) {
				LOGGER.info(userRole + ",");
			}
			if (userGroups.isEmpty()) {
				String userName = WebServiceUtil.EMPTY_STRING;
				if (req.getUserPrincipal() != null) {
					userName = req.getUserPrincipal().getName();
				}
				LOGGER.error("No roles found in Authenticated User for " + userName);
				userGroups = null;
			}
		}
		return userGroups;
	}

	/**
	 * To extract barcode.
	 * @param req {@link HttpServletRequest}
	 * @param resp {@link HttpServletResponse}
	 */
	@RequestMapping(value = "/barcodeExtraction", method = RequestMethod.POST)
	@ResponseBody
	public void barcodeExtraction(final HttpServletRequest req, final HttpServletResponse resp) {
		processBarcodeExtarction(req, resp);
	}

	private void processBarcodeExtarction(final HttpServletRequest req, final HttpServletResponse resp) {
		LOGGER.info("Start processing web service for barcode extraction");
		String respStr = WebServiceUtil.EMPTY_STRING;
		String workingDir = WebServiceUtil.EMPTY_STRING;
		InputStream instream = null;
		OutputStream outStream = null;
		if (req instanceof DefaultMultipartHttpServletRequest) {
			try {
				final String webServiceFolderPath = bsService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
				final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) req;
				final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();

				if (fileMap.size() != 2) {
					respStr = "Invalid number of files. Expected input should be 1 tif file and 1 xml file.";
				}
				if (respStr.isEmpty()) {
					String xmlFileName = WebServiceUtil.EMPTY_STRING;
					for (final String fileName : fileMap.keySet()) {
						if (fileName.endsWith(FileType.XML.getExtensionWithDot())) {
							xmlFileName = fileName;
						}
						final MultipartFile multiPartFile = multiPartRequest.getFile(fileName);
						instream = multiPartFile.getInputStream();
						final File file = new File(workingDir + File.separator + fileName);
						outStream = new FileOutputStream(file);
						final byte[] buf = new byte[WebServiceUtil.bufferSize];
						int len = instream.read(buf);
						while (len > 0) {
							outStream.write(buf, 0, len);
							len = instream.read(buf);
						}
					}
					if (!xmlFileName.isEmpty()) {
						WebServiceParams webServiceParams = null;
						final File xmlFile = new File(workingDir + File.separator + xmlFileName);
						if (xmlFile.exists()) {
							final FileInputStream inputStream = new FileInputStream(xmlFile);
							Source source = XMLUtil.createSourceFromStream(inputStream);
							webServiceParams = (WebServiceParams) batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().unmarshal(
									source);
						}
						if (webServiceParams != null) {
							List<Param> paramList = webServiceParams.getParams().getParam();
							if (paramList == null || paramList.isEmpty()) {
								FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
								respStr = IMPROPER_XML_PARAMETER;
							} else {
								String batchClassIdentifier = WebServiceUtil.EMPTY_STRING;
								String imageName = WebServiceUtil.EMPTY_STRING;
								String docType = WebServiceUtil.EMPTY_STRING;
								for (final Param param : paramList) {
									LOGGER.error("Parameter: " + param.getName() + " Value: " + param.getValue());
									if (param.getName().equalsIgnoreCase(WebServiceUtil.BATCH_CLASS_IDENTIFIER)) {
										batchClassIdentifier = param.getValue();
										continue;
									}
									if (param.getName().equalsIgnoreCase(WebServiceUtil.IMAGE_NAME)) {
										imageName = param.getValue();
										continue;
									}
									if (param.getName().equalsIgnoreCase(WebServiceUtil.DOC_TYPE)) {
										docType = param.getValue();
										continue;
									}
								}
								String results = WebServiceUtil.validateBarcodeExtractionInput(batchClassIdentifier, imageName,
										docType);
								if (!results.isEmpty()) {
									respStr = results;
								} else {
									Set<String> loggedInUserRole = getUserRoles(req);
									if (loggedInUserRole == null || loggedInUserRole.isEmpty()) {
										respStr = "User not authorized to view this API.";
									} else {
										boolean isBatchClassViewableToUser = isBatchClassViewableToUser(batchClassIdentifier,
												loggedInUserRole, isSuperAdmin(req));
										if (isBatchClassViewableToUser) {
											BatchClass batchClass = bcService.getBatchClassByIdentifier(batchClassIdentifier);
											if (batchClass != null) {
												List<DocField> updtDocFdTyList = new ArrayList<DocField>();
												updtDocFdTyList = barcodeExtractionService.extractPageBarCodeAPI(batchClassIdentifier,
														workingDir, imageName, docType);
												LOGGER.info("Generating document level fields for the output result");
												final DocumentLevelFields dlfs = new DocumentLevelFields();
												dlfs.getDocumentLevelField().addAll(updtDocFdTyList);
												Documents docs = new Documents();
												Document doc = new Document();
												docs.getDocument().add(doc);
												doc.setDocumentLevelFields(dlfs);

												StreamResult result;
												try {
													result = new StreamResult(resp.getOutputStream());
													resp.setStatus(HttpServletResponse.SC_OK);
													batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().marshal(docs, result);
												} catch (final IOException e) {
													respStr = INTERNAL_SERVER_ERROR + e.getMessage();
													LOGGER.error(SERVER_ERROR_MSG + respStr);
												}
											}
										} else {
											respStr = "User not authorized to view this batch class id:" + batchClassIdentifier;
										}
									}

								}
							}
						}
					} else {
						respStr = "Invalid format specified in the input. Expected XML found " + xmlFileName;
					}
				}
			} catch (final XmlMappingException xmle) {
				respStr = ERROR_IN_MAPPING_INPUT + xmle;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final DCMAException dcmae) {
				respStr = ERROR_PROCESSING_REQUEST + dcmae;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final Exception e) {
				respStr = INTERNAL_SERVER_ERROR + e;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			} finally {
				IOUtils.closeQuietly(instream);
				IOUtils.closeQuietly(outStream);
			}

			if (!workingDir.isEmpty()) {
				FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
			}
			if (!respStr.isEmpty()) {
				try {
					resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
					LOGGER.error(SERVER_ERROR_MSG + respStr);
				} catch (final IOException ioe) {
					LOGGER.info(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + ioe, ioe);
				}
			}
		}
	}

	/**
	 * To extract Fields using Regex.
	 * @param req {@link HttpServletRequest}
	 * @param resp {@link HttpServletResponse}
	 */
	@RequestMapping(value = "/extractFieldsUsingRegex", method = RequestMethod.POST)
	@ResponseBody
	public void extractFieldsUsingRegex(final HttpServletRequest req, final HttpServletResponse resp) {
		processRegularRegexExtraction(req, resp);
	}

	private void processRegularRegexExtraction(final HttpServletRequest req, final HttpServletResponse resp) {
		LOGGER.info("Start processing web service for extracting fields using regex...");
		String respStr = WebServiceUtil.EMPTY_STRING;
		String workingDir = WebServiceUtil.EMPTY_STRING;
		InputStream instream = null;
		OutputStream outStream = null;
		if (req instanceof DefaultMultipartHttpServletRequest) {
			try {
				final String webServiceFolderPath = bsService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);

				final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) req;

				final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();

				if (respStr.isEmpty()) {
					String xmlFileName = WebServiceUtil.EMPTY_STRING;
					if (fileMap.size() == 2) {
						Set<String> fileNameSet = fileMap.keySet();
						for (final String fileName : fileNameSet) {
							try {
								if (fileName.endsWith(FileType.XML.getExtensionWithDot())) {
									xmlFileName = fileName;
								}
								if (!xmlFileName.isEmpty()) {
									final MultipartFile multiPartFile = multiPartRequest.getFile(fileName);
									instream = multiPartFile.getInputStream();
									final File file = new File(workingDir + File.separator + fileName);
									outStream = new FileOutputStream(file);
									final byte[] buf = new byte[WebServiceUtil.bufferSize];
									int len = instream.read(buf);
									while (len > 0) {
										outStream.write(buf, 0, len);
										len = instream.read(buf);
									}
									break;
								}
							} finally {
								IOUtils.closeQuietly(instream);
								IOUtils.closeQuietly(outStream);
							}
						}
						WebServiceParams webServiceParams = null;
						final File xmlFile = new File(workingDir + File.separator + xmlFileName);
						if (xmlFileName == null) {
							respStr = "No xml file passed...";
						} else if (xmlFile.exists()) {
							FileInputStream inputStream = null;
							try {
								inputStream = new FileInputStream(xmlFile);
								Source source = XMLUtil.createSourceFromStream(inputStream);
								webServiceParams = (WebServiceParams) batchSchemaDao.getJAXB2Template().getJaxb2Marshaller()
										.unmarshal(source);
							} finally {
								IOUtils.closeQuietly(inputStream);
							}

							List<Param> paramList = webServiceParams.getParams().getParam();
							if (paramList == null || paramList.isEmpty()) {
								FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
								respStr = IMPROPER_XML_PARAMETER;
							} else {
								String batchClassIdentifier = WebServiceUtil.EMPTY_STRING;
								String documentType = WebServiceUtil.EMPTY_STRING;
								String hocrFileName = WebServiceUtil.EMPTY_STRING;
								for (final Param param : paramList) {
									if (param.getName().equalsIgnoreCase(WebServiceUtil.DOCUMENT_TYPE)) {
										documentType = param.getValue();
										LOGGER.info("Value for documentType parameter is " + documentType);
										continue;
									}
									if (param.getName().equalsIgnoreCase(WebServiceUtil.BATCH_CLASS_IDENTIFIER)) {
										batchClassIdentifier = param.getValue();
										LOGGER.info("Value for batchClassIdentifier parameter is " + batchClassIdentifier);
										continue;
									}
									if (param.getName().equalsIgnoreCase(WebServiceUtil.HOCR_FILE)) {
										hocrFileName = param.getValue();
										LOGGER.info("Value for hocrFile parameter is " + hocrFileName);
										continue;
									}
								}
								respStr = WebServiceUtil.validateExtractRegexFieldsAPI(workingDir, batchClassIdentifier, documentType,
										hocrFileName);
								if (respStr.isEmpty()) {
									respStr = validateInputAndPerformExtraction(req, resp, respStr, workingDir, multiPartRequest,
											fileNameSet, batchClassIdentifier, documentType, hocrFileName);
								}
							}
						}
					} else {
						respStr = "Invalid number of files. We are supposed only 2 files each of type: XML and html file.";
					}
				}
			} catch (final XmlMappingException xmle) {
				respStr = ERROR_IN_MAPPING_INPUT + xmle;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final DCMAException dcmae) {
				respStr = ERROR_PROCESSING_REQUEST + dcmae;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final Exception e) {
				respStr = INTERNAL_SERVER_ERROR + e;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			}
		} else {
			respStr = IMPROPER_INPUT_TO_SERVER;
			LOGGER.error(SERVER_ERROR_MSG + respStr);
		}
		if (!workingDir.isEmpty()) {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final IOException ioe) {
				LOGGER.info(ERROR_WHILE_SENDING_ERROR_RESPONSE_TO_CLIENT + ioe, ioe);
			}
		}
	}

	private String validateInputAndPerformExtraction(final HttpServletRequest req, final HttpServletResponse resp,
			final String respStr, final String workingDir, final DefaultMultipartHttpServletRequest multiPartRequest,
			final Set<String> fileNameSet, final String batchClassIdentifier, final String documentType, final String hocrFileName)
			throws IOException, FileNotFoundException, DCMAException {
		InputStream instream;
		OutputStream outStream;
		String respStrLocal = respStr;
		try {
			BatchClass batchClass = bcService.getBatchClassByIdentifier(batchClassIdentifier);
			if (batchClass == null) {
				respStrLocal = "Please enter valid batch class identifier";
			} else {
				Set<String> loggedInUserRole = getUserRoles(req);
				if (!isBatchClassViewableToUser(batchClassIdentifier, loggedInUserRole, isSuperAdmin(req))) {
					respStrLocal = USER_NOT_AUTHORIZED_TO_VIEW_THE_BATCH_CLASS + batchClassIdentifier;
					LOGGER.error(SERVER_ERROR_MSG + respStrLocal);
				} else {
					BatchPlugin regularRegexPlugin = batchClassPPService.getPluginProperties(batchClassIdentifier,
							"REGULAR_REGEX_EXTRACTION");
					if (regularRegexPlugin == null || regularRegexPlugin.getPropertiesSize() == 0) {
						respStrLocal = "Fuzzy DB plugin is not configured for batch class : " + batchClassIdentifier
								+ " . Please select proper batch class";
						LOGGER.error(SERVER_ERROR_MSG + respStrLocal);
					}
					for (final String fileName : fileNameSet) {
						if (fileName.equalsIgnoreCase(hocrFileName)) {
							LOGGER.info("hocr file name found : " + hocrFileName);
							final MultipartFile multiPartFile = multiPartRequest.getFile(fileName);
							instream = multiPartFile.getInputStream();
							final File file = new File(workingDir + File.separator + fileName);
							outStream = new FileOutputStream(file);
							final byte[] buf = new byte[WebServiceUtil.bufferSize];
							int len = instream.read(buf);
							while (len > 0) {
								outStream.write(buf, 0, len);
								len = instream.read(buf);
							}
							break;
						}
					}
					final File hocrFile = new File(workingDir + File.separator + hocrFileName);
					if (hocrFile.exists()) {
						respStrLocal = performRegexExtraction(resp, workingDir, hocrFile.getAbsolutePath(), batchClassIdentifier,
								documentType);
					}
				}
			}
		} catch (Exception exception) {
			LOGGER.error("Error occurred while extracting fields using regular regex extarction.");
			throw new DCMAException(exception.getMessage(), exception);
		}
		return respStrLocal;
	}

	private String performRegexExtraction(final HttpServletResponse resp, final String workingDir, final String hocrFilePath,
			final String batchClassId, final String docTypeName) throws FileNotFoundException, IOException, DCMAException {
		final HocrPages hocrPages = new HocrPages();
		final List<HocrPage> hocrPageList = hocrPages.getHocrPage();
		final HocrPage hocrPage = new HocrPage();
		final String pageID = WebServiceUtil.PG0;
		hocrPage.setPageID(pageID);
		String respStrLocal = WebServiceUtil.EMPTY_STRING;
		hocrPageList.add(hocrPage);
		try {
			bsService.hocrGenerationAPI(workingDir, WebServiceUtil.PG0, hocrFilePath, hocrPage);
			LOGGER.info("Successfully generated hocr from html file.");
			final List<DocField> updtDocList = extractionService.extractDocumentFieldsFromHOCR(batchClassId, docTypeName, hocrPages);
			if (updtDocList == null || updtDocList.isEmpty()) {
				respStrLocal = "No dlf found for batch class : " + batchClassId + " , document type name : " + docTypeName;
				LOGGER.info(respStrLocal);
			} else {
				LOGGER.info("Generating document level fields for the output result");

				final DocumentLevelFields dlfs = new DocumentLevelFields();
				dlfs.getDocumentLevelField().addAll(updtDocList);
				Documents docs = new Documents();
				Document doc = new Document();
				docs.getDocument().add(doc);
				doc.setDocumentLevelFields(dlfs);

				StreamResult result;
				try {
					result = new StreamResult(resp.getOutputStream());
					resp.setStatus(HttpServletResponse.SC_OK);
					batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().marshal(docs, result);
				} catch (final IOException e) {
					respStrLocal = INTERNAL_SERVER_ERROR + e.getMessage();
					LOGGER.error(SERVER_ERROR_MSG + respStrLocal);
				}

			}
		} catch (Exception exception) {
			LOGGER.error("Error occurred while extracting fields using regular regex extarction.");
			throw new DCMAException(exception.getMessage(), exception);
		}
		return respStrLocal;
	}

	/**
	 * To extract Fields.
	 * @param req {@link HttpServletRequest}
	 * @param resp {@link HttpServletResponse}
	 */
	@RequestMapping(value = "/extractFields", method = RequestMethod.POST)
	@ResponseBody
	public void extractFields(final HttpServletRequest req, final HttpServletResponse resp) {
		LOGGER.info("Start processing web service for extracting fields ...");
		String respStr = WebServiceUtil.EMPTY_STRING;
		String extractionAPI = req.getHeader(WebServiceUtil.EXTRACTION_API);
		if (extractionAPI != null && !extractionAPI.isEmpty()) {
			LOGGER.debug("Extraction api to be used : " + extractionAPI);
			switch (FieldExtractionTechnique.valueOf(extractionAPI)) {
				case BARCODE_EXTARCTION:
					processBarcodeExtarction(req, resp);
					break;
				case RECOSTAR_EXTARCTION:
					processFixedFormExtraction(req, resp);
					break;
				case REGULAR_REGEX_EXTRACTION:
					processRegularRegexExtraction(req, resp);
					break;
				case KV_EXTRACTION:
					processKVExtraction(req, resp);
					break;
				case FUZZY_DB:
					processFuzzyDbExtraction(req, resp);
					break;
				default:
					respStr = "Invalid extraction api passed. Allowed API's are BARCODE_EXTARCTION,	RECOSTAR_EXTARCTION, REGULAR_REGEX_EXTRACTION, KV_EXTRACTION, FUZZY_DB";
					break;
			}
		} else {
			respStr = "No extraction api passed. Allowed API's are BARCODE_EXTARCTION,	RECOSTAR_EXTARCTION, REGULAR_REGEX_EXTRACTION, KV_EXTRACTION, FUZZY_DB";
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final IOException ioe) {
				LOGGER.info("Exception in sending the response to client. Logged the exception for debugging:" + ioe, ioe);
			}
		}
	}

	/**
	 * To extract From Image.
	 * @param req {@link HttpServletRequest}
	 * @param resp {@link HttpServletResponse}
	 */
	@RequestMapping(value = "/extractFromImage", method = RequestMethod.POST)
	@ResponseBody
	public void extractFromImage(final HttpServletRequest req, final HttpServletResponse resp) {
		LOGGER.info("Start processing web service for performExtractionForImage..");
		String respStr = WebServiceUtil.EMPTY_STRING;
		String workingDir = WebServiceUtil.EMPTY_STRING;
		InputStream instream = null;
		OutputStream outStream = null;
		if (req instanceof DefaultMultipartHttpServletRequest) {
			try {
				final String webServiceFolderPath = bsService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
				final String outputDir = WebServiceUtil.createWebServiceOutputDir(workingDir);

				final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) req;

				final BatchInstanceThread batchInstanceThread = new BatchInstanceThread();

				final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();
				final DefaultMultipartHttpServletRequest multipartReq = (DefaultMultipartHttpServletRequest) req;
				String batchClassId = WebServiceUtil.EMPTY_STRING;
				String ocrEngine = WebServiceUtil.EMPTY_STRING;
				String colorSwitch = WebServiceUtil.EMPTY_STRING;
				String projectFile = WebServiceUtil.EMPTY_STRING;
				String cmdLanguage = WebServiceUtil.EMPTY_STRING;
				String documentType = WebServiceUtil.EMPTY_STRING;
				String tifFileName = WebServiceUtil.EMPTY_STRING;
				for (final Enumeration<String> params = multipartReq.getParameterNames(); params.hasMoreElements();) {
					final String paramName = params.nextElement();
					if (paramName.equalsIgnoreCase(WebServiceUtil.BATCH_CLASS_IDENTIFIER)) {
						batchClassId = multipartReq.getParameter(paramName);
						continue;
					}
					if (paramName.equalsIgnoreCase(WebServiceUtil.OCR_ENGINE)) {
						ocrEngine = multipartReq.getParameter(paramName);
						continue;
					}
					if (paramName.equalsIgnoreCase(WebServiceUtil.COLOR_SWITCH)) {
						colorSwitch = multipartReq.getParameter(paramName);
						continue;
					}
					if (paramName.equalsIgnoreCase(WebServiceUtil.PROJECT_FILE)) {
						projectFile = multipartReq.getParameter(paramName);
						continue;
					}
					if (paramName.equalsIgnoreCase(WebServiceUtil.CMD_LANGUAGE)) {
						cmdLanguage = multipartReq.getParameter(paramName);
						continue;
					}
					if (paramName.equalsIgnoreCase(WebServiceUtil.DOCUMENT_TYPE)) {
						documentType = multipartReq.getParameter(paramName);
						continue;
					}
					
				}
				LOGGER.info("Parameters for the web service are: ");
				LOGGER.debug(WebServiceUtil.BATCH_CLASS_IDENTIFIER + " :" + batchClassId);
				LOGGER.debug(WebServiceUtil.OCR_ENGINE + " :" + ocrEngine);
				LOGGER.debug(WebServiceUtil.COLOR_SWITCH + " :" + colorSwitch);
				LOGGER.debug(WebServiceUtil.PROJECT_FILE + " :" + projectFile);
				// logger.debug("" + tesseractVersion);
				LOGGER.debug(WebServiceUtil.CMD_LANGUAGE + " :" + cmdLanguage);
				LOGGER.debug(WebServiceUtil.DOCUMENT_TYPE + " :" + documentType);

				final int attachedFileSize = fileMap.size();
				if (attachedFileSize != 1) {
					respStr = "Invalid number of files. Expected number of file(s): 1 of type: tif/tiff/png file. Recieved: "
							+ attachedFileSize + " file(s)";
				}
				if (respStr.isEmpty()) {
					for (final String fileName : fileMap.keySet()) {
						try {
							final MultipartFile multiPartFile = multiPartRequest.getFile(fileName);
							instream = multiPartFile.getInputStream();
							final File file = new File(workingDir + File.separator + fileName);
							outStream = new FileOutputStream(file);
							final byte[] buf = new byte[WebServiceUtil.bufferSize];
							int len = instream.read(buf);
							while (len > 0) {
								outStream.write(buf, 0, len);
								len = instream.read(buf);
							}
							if (fileName.endsWith(FileType.TIF.getExtensionWithDot())
									|| fileName.endsWith(FileType.TIFF.getExtensionWithDot())) {
								tifFileName = fileName;
								LOGGER.debug("Image Name :" + tifFileName);
						
							}
						} finally {
							IOUtils.closeQuietly(instream);
							IOUtils.closeQuietly(outStream);
						}

					}

					final StringBuilder projectFileBuffer = new StringBuilder();
					projectFileBuffer.append(bsService.getBaseFolderLocation());
					projectFileBuffer.append(File.separator);
					projectFileBuffer.append(batchClassId);
					projectFileBuffer.append(File.separator);
					projectFileBuffer.append(RECOSTAR_EXTRACTION);
					projectFileBuffer.append(File.separator);
					projectFileBuffer.append(projectFile);
					final String projectFilePath = projectFileBuffer.toString();
					final String results = WebServiceUtil.validateExtractFromImageAPI(workingDir, ocrEngine, colorSwitch,
							projectFilePath, cmdLanguage, tifFileName);
					if (!results.isEmpty()) {
						respStr = results;
					} else {
						String[] fileNames = null;
						final File file = new File(workingDir);
						fileNames = splitImagesAndCreatePNG(workingDir, colorSwitch, file);
						LOGGER.info("Number of file is:" + fileNames.length);
						LOGGER.info("OcrEngine used for generating ocr is :" + ocrEngine);
						if (ocrEngine.equalsIgnoreCase(WebServiceUtil.RECOSTAR)) {
							respStr = createHOCRViaRecostar(workingDir, outputDir, batchInstanceThread, colorSwitch, projectFilePath,
									fileNames);
						} else if (ocrEngine.contains(WebServiceUtil.TESSERACT)) {
							respStr = createHOCRViaTesseract(workingDir, outputDir, batchInstanceThread, colorSwitch, ocrEngine,
									cmdLanguage, fileNames);
						} else {
							respStr = "Please select valid tool for generating OCR file.";
						}
						if (respStr.isEmpty()) {
							respStr = executeBatchInstanceThread(workingDir, outputDir, batchInstanceThread, ocrEngine, fileNames);
						}

					}
				}
				if (respStr.isEmpty()) {
					final String hocrFilePath = outputDir + File.separator
							+ tifFileName.substring(0, tifFileName.lastIndexOf(CONSTANT_DOT) + 1) + FileType.HTML.getExtension();
					LOGGER.debug("Generated HOCR file located at " + hocrFilePath);
					final Set<String> loggedInUserRole = getUserRoles(req);
					if (!isBatchClassViewableToUser(batchClassId, loggedInUserRole, isSuperAdmin(req))) {
						respStr = "User is not authorized to view the batch class for given identifier:" + batchClassId;
						LOGGER.error("Error response at server:" + respStr);
					}

					LOGGER.info("Performing Extraction using KV mechanism.");
					respStr = extractKVFromHOCR(resp, respStr, outputDir, batchClassId, hocrFilePath, documentType);
				}
			

			} catch (final DCMAException dcmae) {
				respStr = "Error in processing request. Detailed exception is " + dcmae;
				LOGGER.error("Error response at server:" + respStr);
			} catch (final Exception e) {
				respStr = INTERNAL_SERVER_ERROR + e;
				LOGGER.error("Error response at server:" + respStr);
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			}
		} else {
			respStr = IMPROPER_INPUT_TO_SERVER;
			LOGGER.error("Error response at server:" + respStr);
		}
		if (!workingDir.isEmpty()) {
			LOGGER.info("Clearing the temporary files.");
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
				LOGGER.error("Error response at server:" + respStr);
			} catch (final IOException ioe) {
				LOGGER.info("Exception in sending the error code to client. Logged the exception for debugging:" + ioe, ioe);
			}
		}
	}


	private String createHOCRViaTesseract(final String workingDir, final String outputDir,
			final BatchInstanceThread batchInstanceThread, final String colorSwitch, final String tesseractVersion,
			final String cmdLanguage, final String[] fileNames) {
		String respStr = WebServiceUtil.EMPTY_STRING;
		if (fileNames != null && fileNames.length > 0) {
			for (final String fileName : fileNames) {
				try {
					LOGGER.info("File processing for ocr with tesseract is :" + fileName);
					tesseractService.createOCR(workingDir, colorSwitch, fileName, batchInstanceThread, outputDir, cmdLanguage,
							tesseractVersion);
				} catch (final DCMAException e) {
					respStr = "Error occuring while creating OCR file using tesseract. Please try again." + e;
					LOGGER.error("Error response at server:" + respStr);
					break;
				}
			}
		} else {
			respStr = "Improper input to server. No tiff/png files provided.";
			LOGGER.error("Error response at server:" + respStr);

		}
		return respStr;
	}


	private String createHOCRViaRecostar(final String workingDir, final String outputDir,
			final BatchInstanceThread batchInstanceThread, final String colorSwitch, final String projectFile, final String[] fileNames) {
		String respStr = WebServiceUtil.EMPTY_STRING;
		if (fileNames != null && fileNames.length > 0) {
			for (final String fileName : fileNames) {
				try {
					LOGGER.info("File processing for recostar is :" + fileName);
					recostarService.createOCRForProjectFile(projectFile, workingDir, colorSwitch, fileName, batchInstanceThread,
							outputDir);
				} catch (final DCMAException e) {
					respStr = "Error occuring while creating OCR file using recostar. Please try again." + e;
					LOGGER.error("Error response at server:" + respStr);
					break;
				}
			}
		} else {
			if (colorSwitch.equalsIgnoreCase(WebServiceUtil.OFF_STRING)) {
				respStr = "Improper input to server. No tiff files provided.";
			} else {
				respStr = "Improper input to server. No tiff/png files provided.";
			}
		}
		return respStr;
	}

	private String[] splitImagesAndCreatePNG(final String workingDir, final String colorSwitch, final File file) throws DCMAException {
		String[] fileNames;
		if (colorSwitch.equalsIgnoreCase(WebServiceUtil.ON_STRING)) {
			final String[] tifFileNames = file.list(new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(), FileType.TIFF
					.getExtensionWithDot()));
			// generate png file for each tiff/tif file
			for (final String tifFile : tifFileNames) {
				imService.generatePNGForImage(new File(workingDir + File.separator + tifFile));
			}
			LOGGER.info("Picking up the png file for processing.");
			fileNames = file.list(new CustomFileFilter(false, FileType.PNG.getExtensionWithDot()));
		} else {
			LOGGER.info("Picking up the tif file for processing.");
			fileNames = file
					.list(new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(), FileType.TIFF.getExtensionWithDot()));
		}
		return fileNames;
	}

	private String executeBatchInstanceThread(final String workingDir, final String outputDir,
			final BatchInstanceThread batchInstanceThread, final String ocrEngine, final String[] fileNames) {
		String respStr = WebServiceUtil.EMPTY_STRING;
		try {
			batchInstanceThread.execute();
			if (ocrEngine.equalsIgnoreCase(WebServiceUtil.RECOSTAR)) {
				for (final String fileName : fileNames) {
					final String recostarXMLFileName = fileName.substring(0, fileName.lastIndexOf(WebServiceUtil.DOT))
							+ FileType.XML.getExtensionWithDot();
					try {
						FileUtils.copyFile(new File(workingDir + File.separator + recostarXMLFileName), new File(outputDir
								+ File.separator + recostarXMLFileName));
					} catch (final Exception e) {
						respStr = "Error while generating copying result file." + e;
						LOGGER.error("Error response at server:" + respStr);
						break;
					}
				}
			}
		} catch (final DCMAApplicationException e) {
			respStr = "Exception while generating ocr using threadpool" + e;
		}
		return respStr;
	}

		
	private String extractKVFromHOCR(final HttpServletResponse resp, final String respStr, final String workingDir,
			final String batchClassIdentifier, final String filePath, final String documentType) throws FileNotFoundException,
			IOException, DCMAException {
		// extract the hocr content from hocr files
		// generate hocr from html file
		String respStrLocal = respStr;
		final HocrPages hocrPages = new HocrPages();
		final List<HocrPage> hocrPageList = hocrPages.getHocrPage();
		final HocrPage hocrPage = new HocrPage();
		final String pageID = WebServiceUtil.PG0;
		hocrPage.setPageID(pageID);
		hocrPageList.add(hocrPage);
		try {
			bsService.hocrGenerationAPI(workingDir, WebServiceUtil.PG0, filePath, hocrPage);
		} catch (final Exception e) {
			respStrLocal = INTERNAL_SERVER_ERROR + "Error generating HOCR for the file: " + filePath;
			LOGGER.error("Error response at server:" + respStr);
		}

		LOGGER.info("Successfully generated hocr from html file.");
		final List<DocField> updtDocList = new ArrayList<DocField>();
		final boolean isSuccess = kvService.extractKVFromHOCRForBatchClass(updtDocList, hocrPages, batchClassIdentifier, documentType);
		if (!isSuccess) {
			respStrLocal = INTERNAL_SERVER_ERROR;
			LOGGER.error("Error response at server:" + respStr);
		} else {
			LOGGER.info("Generating document level fields for the output result");

			final DocumentLevelFields dlfs = new DocumentLevelFields();
			dlfs.getDocumentLevelField().addAll(updtDocList);
			final Documents docs = new Documents();
			final Document doc = new Document();
			docs.getDocument().add(doc);
			doc.setDocumentLevelFields(dlfs);

			StreamResult result;
			try {
				result = new StreamResult(resp.getOutputStream());
				resp.setStatus(HttpServletResponse.SC_OK);
				batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().marshal(docs, result);
			} catch (final IOException e) {
				respStrLocal = INTERNAL_SERVER_ERROR + e.getMessage();
				LOGGER.error("Error response at server:" + respStrLocal);
			}
		}
		return respStrLocal;
	}

	/**
	 * To classify Multi Page Hocr.
	 * @param req {@link HttpServletRequest}
	 * @param resp {@link HttpServletResponse}
	 */
	@RequestMapping(value = "/classifyMultiPageHocr", method = RequestMethod.POST)
	@ResponseBody
	public void classifyMultiPageHocr(final HttpServletRequest req, final HttpServletResponse resp) {
		LOGGER.info("Start processing web service for classifyHocr.");
		String respStr = WebServiceUtil.EMPTY_STRING;
		String workingDir = WebServiceUtil.EMPTY_STRING;
		InputStream instream = null;
		OutputStream outStream = null;
		if (req instanceof DefaultMultipartHttpServletRequest) {
			try {
				final String webServiceFolderPath = bsService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);

				final DefaultMultipartHttpServletRequest multipartReq = (DefaultMultipartHttpServletRequest) req;
				String batchClassId = WebServiceUtil.EMPTY_STRING;
				for (final Enumeration<String> params = multipartReq.getParameterNames(); params.hasMoreElements();) {
					final String paramName = params.nextElement();
					if (paramName.equalsIgnoreCase("batchClassId")) {
						batchClassId = multipartReq.getParameter(paramName);
						break;
					}
				}
				Map<LuceneProperties, String> batchClassConfigMap = new HashMap<LuceneProperties, String>();

				if (batchClassId == null || batchClassId.isEmpty()) {
					respStr = "Batch Class identifier not specified.";
					LOGGER.error("Error response at server:" + respStr);
				} else {
					BatchClass bc = bcService.getBatchClassByIdentifier(batchClassId);
					if (bc == null) {
						respStr = "Batch class with the specified identifier : " + batchClassId + " does not exist.";
						LOGGER.error("Error response at server:" + respStr);
					} else {
						Set<String> loggedInUserRole = getUserRoles(req);
						if (!isBatchClassViewableToUser(batchClassId, loggedInUserRole, isSuperAdmin(req))) {
							respStr = "User is not authorized to view the batch class for given identifier:" + batchClassId;
							LOGGER.error("Error response at server:" + respStr);
						} else {
							BatchPlugin searchClassPlugin = batchClassPPService.getPluginProperties(batchClassId,
									ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN);
							BatchPlugin docAssemblyPlugin = batchClassPPService.getPluginProperties(batchClassId,
									DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN);
							if (searchClassPlugin == null || docAssemblyPlugin == null) {
								respStr = "Either Search Classification plugin or document assembly plugin does not exist for the specified batch class id: "
										+ batchClassId;
							} else if (docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP_MP_LP) == null
									|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP) == null
									|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_MP) == null
									|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_LP) == null
									|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP_LP) == null
									|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP_MP) == null
									|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_MP_LP) == null) {
								respStr = "Incomplete properties of the Document assembler plugin for the specified batch class id :"
										+ batchClassId;
								LOGGER.error("Error response at server:" + respStr);
							} else if (searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_VALID_EXTNS) == null
									|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_INDEX_FIELDS) == null
									|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_STOP_WORDS) == null
									|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_MIN_TERM_FREQ) == null
									|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_MIN_DOC_FREQ) == null
									|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_MIN_WORD_LENGTH) == null
									|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_MAX_QUERY_TERMS) == null
									|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_TOP_LEVEL_FIELD) == null
									|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_NO_OF_PAGES) == null
									|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_MAX_RESULT_COUNT) == null
									|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_FIRST_PAGE_CONF_WEIGHTAGE) == null
									|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_MIDDLE_PAGE_CONF_WEIGHTAGE) == null
									|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_LAST_PAGE_CONF_WEIGHTAGE) == null) {
								respStr = "Incomplete properties of the Search Classification plugin for the specified batch class id :"
										+ batchClassId;
								LOGGER.error("Error response at server:" + respStr);
							}
						}
					}
				}
				if (respStr.isEmpty()) {
					batchClassConfigMap.put(LuceneProperties.LUCENE_VALID_EXTNS, batchClassPPService.getPropertyValue(batchClassId,
							ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_VALID_EXTNS));
					batchClassConfigMap.put(LuceneProperties.LUCENE_INDEX_FIELDS, batchClassPPService.getPropertyValue(batchClassId,
							ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_INDEX_FIELDS));
					batchClassConfigMap.put(LuceneProperties.LUCENE_STOP_WORDS, batchClassPPService.getPropertyValue(batchClassId,
							ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_STOP_WORDS));
					batchClassConfigMap.put(LuceneProperties.LUCENE_MIN_TERM_FREQ, batchClassPPService.getPropertyValue(batchClassId,
							ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_MIN_TERM_FREQ));
					batchClassConfigMap.put(LuceneProperties.LUCENE_MIN_DOC_FREQ, batchClassPPService.getPropertyValue(batchClassId,
							ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_MIN_DOC_FREQ));
					batchClassConfigMap.put(LuceneProperties.LUCENE_MIN_WORD_LENGTH, batchClassPPService.getPropertyValue(
							batchClassId, ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_MIN_WORD_LENGTH));
					batchClassConfigMap.put(LuceneProperties.LUCENE_MAX_QUERY_TERMS, batchClassPPService.getPropertyValue(
							batchClassId, ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_MAX_QUERY_TERMS));
					batchClassConfigMap.put(LuceneProperties.LUCENE_TOP_LEVEL_FIELD, batchClassPPService.getPropertyValue(
							batchClassId, ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_TOP_LEVEL_FIELD));
					batchClassConfigMap.put(LuceneProperties.LUCENE_NO_OF_PAGES, batchClassPPService.getPropertyValue(batchClassId,
							ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_NO_OF_PAGES));
					batchClassConfigMap.put(LuceneProperties.LUCENE_MAX_RESULT_COUNT, batchClassPPService.getPropertyValue(
							batchClassId, ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_MAX_RESULT_COUNT));
					batchClassConfigMap.put(LuceneProperties.LUCENE_FIRST_PAGE_CONF_WEIGHTAGE, batchClassPPService.getPropertyValue(
							batchClassId, ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN,
							LuceneProperties.LUCENE_FIRST_PAGE_CONF_WEIGHTAGE));
					batchClassConfigMap.put(LuceneProperties.LUCENE_MIDDLE_PAGE_CONF_WEIGHTAGE, batchClassPPService.getPropertyValue(
							batchClassId, ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN,
							LuceneProperties.LUCENE_MIDDLE_PAGE_CONF_WEIGHTAGE));
					batchClassConfigMap.put(LuceneProperties.LUCENE_LAST_PAGE_CONF_WEIGHTAGE, batchClassPPService.getPropertyValue(
							batchClassId, ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN,
							LuceneProperties.LUCENE_LAST_PAGE_CONF_WEIGHTAGE));

					final MultiValueMap<String, MultipartFile> fileMap = multipartReq.getMultiFileMap();

					if (fileMap.size() == 1) {
						String path = "";
						for (final String fileName : fileMap.keySet()) {
							// only single zip file is expected as input containing multiple HTML files.
							try {
								boolean isZipContent = multipartReq.getFile(fileName).getContentType().toLowerCase().contains(
										APPLICATION_ZIP);
								if (fileName.toLowerCase().indexOf(FileType.ZIP.getExtension()) > -WebserviceConstants.ONE || isZipContent) {
									// only HTML file is expected
									final MultipartFile f = multipartReq.getFile(fileName);
									instream = f.getInputStream();
									path = workingDir + File.separator + fileName;
									if (isZipContent && !fileName.toLowerCase().endsWith(FileType.ZIP.getExtension())) {
										path = workingDir + File.separator + fileName + FileType.ZIP.getExtensionWithDot();
									}
									final File file = new File(path);
									outStream = new FileOutputStream(file);
									final byte buf[] = new byte[WebServiceUtil.bufferSize];
									int len;
									while ((len = instream.read(buf)) > WebserviceConstants.ZERO) {
										outStream.write(buf, 0, len);
									}
									break;
								} else {
									respStr = "Improper input to server. Expected only one zip file. Returning without processing the results.";
									LOGGER.error("Error response at server:" + respStr);
								}
							} finally {
								IOUtils.closeQuietly(instream);
								IOUtils.closeQuietly(outStream);
							}
						}
						if (respStr.isEmpty()) {
							zipService.unzipFiles(new File(path), workingDir);
							CustomFileFilter filter = new CustomFileFilter(false, FileType.HTML.getExtension());
							String htmlFileList[] = new File(workingDir).list(filter);
							if (htmlFileList != null && htmlFileList.length > WebserviceConstants.ZERO) {
								ObjectFactory objectFactory = new ObjectFactory();
								List<Document> xmlDocuments = new ArrayList<Document>();
								HocrPages hocrPages = new HocrPages();
								Pages pages = new Pages();
								List<Page> listOfPages = pages.getPage();
								Document doc = objectFactory.createDocument();
								xmlDocuments.add(doc);
								doc.setPages(pages);

								for (int index = 0; index < htmlFileList.length; index++) {
									// generate hocr file from html file.
									String htmlFile = htmlFileList[index];
									String htmlFilePath = workingDir + File.separator + htmlFile;
									HocrPage hocrPage = new HocrPage();
									List<HocrPage> hocrPageList = hocrPages.getHocrPage();
									String pageID = PG_IDENTIFIER + index;
									hocrPage.setPageID(pageID);
									hocrPageList.add(hocrPage);
									bsService.hocrGenerationAPI(workingDir, pageID, htmlFilePath, hocrPage);

									Page pageType = objectFactory.createPage();
									pageType.setIdentifier(EphesoftProperty.PAGE.getProperty() + index);
									pageType.setHocrFileName(htmlFile);
									listOfPages.add(pageType);
								}

								scService.generateConfidenceScoreAPI(xmlDocuments, hocrPages, workingDir, batchClassConfigMap,
										batchClassId);

								try {
									// invoke the document assembler plugin
									xmlDocuments = docAssembler.createDocumentAPI(DocumentClassificationFactory.SEARCHCLASSIFICATION,
											batchClassId, listOfPages);
									Documents docs = new Documents();
									docs.getDocument().addAll(xmlDocuments);
									StreamResult result;
									try {
										result = new StreamResult(resp.getOutputStream());
										resp.setStatus(HttpServletResponse.SC_OK);
										batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().marshal(docs, result);
									} catch (final IOException e) {
										respStr = INTERNAL_SERVER_ERROR + e;
										LOGGER.error("Error response at server:" + respStr);
									}
								} catch (final DCMAApplicationException e) {
									respStr = "Error while executing plugin. Detailed exception is " + e;
									LOGGER.error("Error response at server:" + respStr);
								}
							} else {
								respStr = "Improper input to server. Expected HTML file inside zip file.";
								LOGGER.error("Error response at server:" + respStr);
							}
						}
					} else {
						respStr = "Improper input to server. Expected only one zip file. Returning without processing the results.";
						LOGGER.error("Error response at server:" + respStr);
					}
				}
			} catch (final XmlMappingException xmle) {
				respStr = "Error in mapping input XML in the desired format. Please send it in the specified format. Detailed exception is "
						+ xmle;
				LOGGER.error("Error response at server:" + respStr);
			} catch (final DCMAException dcmae) {
				respStr = "Error in processing request. Detailed exception is " + dcmae;
				LOGGER.error("Error response at server:" + respStr);
			} catch (final Exception e) {
				respStr = INTERNAL_SERVER_ERROR + e;
				LOGGER.error("Error response at server:" + respStr);
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			}
		} else {
			respStr = "Improper input to server. Expected multipart request. Returing without processing the results.";
			LOGGER.error("Error response at server:" + respStr);
		}

		if (!workingDir.isEmpty()) {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
				LOGGER.error("Error response at server:" + respStr);
			} catch (final IOException ioe) {
				LOGGER.info("Exception in sending the error code to client. Logged the exception for debugging:" + ioe, ioe);
			}
		}
	}

	@RequestMapping(value = "/uploadBatch/{batchClassIdentifier}/{batchInstanceName}", method = RequestMethod.POST)
	@ResponseBody
	public void uploadBatch(@PathVariable(BATCH_CLASS_IDENTIFIER) final String batchClassIdentifier,
			@PathVariable("batchInstanceName") final String batchInstanceName, final HttpServletRequest req,
			final HttpServletResponse resp) {
		LOGGER.info("Start processing web service for uploadBatch.");
		String respStr = WebServiceUtil.EMPTY_STRING;
		String workingDir = WebServiceUtil.EMPTY_STRING;
		InputStream instream = null;
		OutputStream outStream = null;
		respStr = WebServiceUtil.validateUploadBatchParameters(batchClassIdentifier, batchInstanceName);
		if (respStr.isEmpty()) {
			try {
				if (req instanceof DefaultMultipartHttpServletRequest) {
					try {
						final String webServiceFolderPath = bsService.getWebServicesFolderPath();
						workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
						final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) req;
						final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();
						String uncFolderPath = WebServiceUtil.EMPTY_STRING;
						List<File> fileList = new ArrayList<File>();
						try {
							Set<String> roles = getUserRoles(req);
							if (roles != null) {
								BatchClass batchClass = bcService.getBatchClassByUserRoles(roles, batchClassIdentifier);
								if (batchClass == null) {
									respStr = "The user does not have the authentication to run the batch in the requested batch class with id: "
											+ batchClassIdentifier;
									LOGGER.error(SERVER_ERROR_MSG + respStr);
								} else {
									uncFolderPath = batchClass.getUncFolder();
								}
							} else {
								respStr = "There are no user roles for authenticated user.";
								LOGGER.error(SERVER_ERROR_MSG + respStr);
							}
						} finally {
							IOUtils.closeQuietly(instream);
							IOUtils.closeQuietly(outStream);
						}
						if (respStr.isEmpty()) {
							for (String srcFileName : fileMap.keySet()) {
								try {
									if (srcFileName.endsWith(FileType.TIF.getExtensionWithDot())
											|| srcFileName.endsWith(FileType.TIFF.getExtensionWithDot())
											|| srcFileName.endsWith(FileType.PDF.getExtensionWithDot())) {
										final File file = new File(workingDir + File.separator + srcFileName);
										fileList.add(file);
										final MultipartFile multiPartFile = multiPartRequest.getFile(srcFileName);
										instream = multiPartFile.getInputStream();
										outStream = new FileOutputStream(file);
										final byte[] buf = new byte[WebServiceUtil.bufferSize];
										int len = instream.read(buf);
										while (len > 0) {
											outStream.write(buf, 0, len);
											len = instream.read(buf);
										}
									} else {
										respStr = "Expected only tif, tiff and pdf files.";
										LOGGER.error(SERVER_ERROR_MSG + respStr);
									}
								} finally {
									IOUtils.closeQuietly(instream);
									IOUtils.closeQuietly(outStream);
								}
							}
						}
						if (respStr.isEmpty()) {
							String filePath = workingDir + File.separator + batchInstanceName;
							File file = new File(filePath);
							if (file.mkdir()) {
								for (File srcFileName : fileList) {
									File destFile = new File(filePath + File.separator + srcFileName.getName());
									try {
										FileUtils.copyFile(srcFileName, destFile);
									} catch (IOException e) {
										respStr = "Unable to copy the files to working directory";
										LOGGER.error(SERVER_ERROR_MSG + respStr);
										break;
									}
								}

								File destFile = new File(uncFolderPath + File.separator + batchInstanceName);
								try {
									if (!destFile.exists()) {
										FileUtils.copyDirectoryWithContents(file, destFile);
									} else {
										respStr = "The batch name already exists. Please change the name of the batch.";
										LOGGER.error(SERVER_ERROR_MSG + respStr);
									}
								} catch (IOException e) {
									respStr = "Unable to copy the files to the unc folder";
									LOGGER.error(SERVER_ERROR_MSG + respStr);
								}
							}
						}
					} finally {
						IOUtils.closeQuietly(instream);
						IOUtils.closeQuietly(outStream);
					}
				} else {
					respStr = IMPROPER_INPUT_TO_SERVER;
					LOGGER.error(SERVER_ERROR_MSG + respStr);
				}
			} catch (final XmlMappingException xmle) {
				respStr = "Error in mapping input XML in the desired format. Please send it in the specified format. Detailed exception is "
						+ xmle;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final Exception e) {
				respStr = INTERNAL_SERVER_ERROR + e;
				LOGGER.error(SERVER_ERROR_MSG + respStr);
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			}
		} else {
			LOGGER.error(SERVER_ERROR_MSG + respStr);
		}
		if (!workingDir.isEmpty()) {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
				LOGGER.error(SERVER_ERROR_MSG + respStr);
			} catch (final IOException ioe) {
				LOGGER.info("Exception in sending the error code to client. Logged the exception for debugging:" + ioe, ioe);
			}
		}

	}
}
