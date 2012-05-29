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

package com.ephesoft.dcma.workflow.service.webservices;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
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
import com.ephesoft.dcma.batch.schema.ObjectFactory;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.ReportingOptions;
import com.ephesoft.dcma.batch.schema.WebServiceParams;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.schema.ExtractKVParams.Params;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.schema.KVExtractionFieldPatterns.KVExtractionFieldPattern;
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
import com.ephesoft.dcma.recostarExtraction.service.RecostarExtractionService;
import com.ephesoft.dcma.tesseract.service.TesseractService;
import com.ephesoft.dcma.user.service.UserConnectivityService;
import com.ephesoft.dcma.util.CustomFileFilter;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.TIFFUtil;
import com.ephesoft.dcma.util.WebServiceUtil;
import com.ephesoft.dcma.util.XMLUtil;
import com.ephesoft.dcma.workflow.service.JbpmService;
import com.ephesoft.dcma.workflow.service.common.DeploymentService;
import com.ephesoft.dcma.workflow.service.common.WorkflowService;

/**
 * This Class provides the functionality of the Web services across the product. {@link EphesoftWebServiceAPI}.
 * 
 * @author Ephesoft
 * @version 1.0
 */
@Controller
public class EphesoftWebServiceAPI {

	/**
	 * LOGGER to print the logging information.
	 */
	private final Logger logger = LoggerFactory.getLogger(EphesoftWebServiceAPI.class);

	@Autowired
	private ImageProcessService imService;

	@Autowired
	private UserConnectivityService userConnectivityService;

	@Autowired
	private KVFieldCreatorService kvFieldService;

	@Autowired
	private SearchClassificationService scService;

	@Autowired
	private BarcodeService barcodeService;

	@Autowired
	private BatchSchemaService bsService;

	@Autowired
	private DocumentAssembler docAssembler;

	@Autowired
	@Qualifier("batchClassPluginPropertiesService")
	private PluginPropertiesService batchClassPPService;

	@Autowired
	private ReportDataService reportingService;

	@Autowired
	private BatchClassService bcService;

	@Autowired
	private BatchSchemaDao batchSchemaDao;

	@Autowired
	private ImportBatchService importBatchService;

	@Autowired
	private KVExtractionService kvService;

	@Autowired
	private DeploymentService deploymentService;

	@Autowired
	private BatchClassGroupsService batchClassGroupsService;

	@Autowired
	private BatchInstanceService biService;

	@Autowired
	private JbpmService jbpmService;

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private BatchClassModuleService bcModuleService;

	@Autowired
	private BatchInstanceGroupsService batchInstanceGroupsService;

	@Autowired
	private RecostarService recostarService;

	@Autowired
	private TesseractService tesseractService;

	@Autowired
	private RecostarExtractionService recostarExtractionService;

	@Autowired
	private FuzzyDBSearchService fuzzyDBSearchService;

	@Autowired
	private FieldTypeService fieldTypeService;

	/**
	 * Instance of PluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	@RequestMapping(value = "/splitMultipageFile", method = RequestMethod.POST)
	@ResponseBody
	public void splitMultipageFile(final HttpServletRequest req, final HttpServletResponse resp) {
		String respStr = WebServiceUtil.EMPTY_STRING;
		String workingDir = WebServiceUtil.EMPTY_STRING;
		try {
			if (req instanceof DefaultMultipartHttpServletRequest) {
				logger.info("Start spliting multipage file");
				final String webServiceFolderPath = bsService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
				final String outputDir = WebServiceUtil.createWebServiceOutputDir(workingDir);

				InputStream instream = null;
				OutputStream outStream = null;

				final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) req;

				final BatchInstanceThread threadList = new BatchInstanceThread(new File(workingDir).getName() + Math.random());
				String inputParams = WebServiceUtil.EMPTY_STRING, outputParams = WebServiceUtil.EMPTY_STRING;
				boolean isGSTool = false;
				for (final Enumeration<String> params = multiPartRequest.getParameterNames(); params.hasMoreElements();) {
					final String paramName = params.nextElement();
					if (paramName.equalsIgnoreCase("isGhostscript")) {
						isGSTool = Boolean.parseBoolean(multiPartRequest.getParameter(paramName));
						logger.info("Value for isGhostscript parameter is " + isGSTool);
						continue;
					}

					if (paramName.equalsIgnoreCase("inputParams")) {
						inputParams = multiPartRequest.getParameter(paramName);
						logger.info("Value for inputParams parameter is " + inputParams);
						continue;
					}

					if (paramName.equalsIgnoreCase("outputParams")) {
						outputParams = multiPartRequest.getParameter(paramName);
						logger.info("Value for outputParams parameter is " + outputParams);
						continue;
					}
				}
				final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();
				// perform validation on input fields
				String results = WebServiceUtil.validateSplitAPI(fileMap, isGSTool, outputParams, inputParams);
				if (!results.isEmpty()) {
					respStr = results;
				} else {

					for (final String fileName : fileMap.keySet()) {
						if (fileName.toLowerCase().indexOf(FileType.PDF.getExtension()) > -1
								|| fileName.toLowerCase().indexOf(FileType.TIF.getExtension()) > -1
								|| fileName.toLowerCase().indexOf(FileType.TIFF.getExtension()) > -1) {
							// only tiffs and RSP file is expected
							if (isGSTool
									&& (fileName.toLowerCase().indexOf(FileType.TIF.getExtension()) > -1 || fileName.toLowerCase()
											.indexOf(FileType.TIFF.getExtension()) > -1)) {
								respStr = "Only PDF files expected with GhostScript tool.";
								break;
							}
							final MultipartFile multipartFile = multiPartRequest.getFile(fileName);
							instream = multipartFile.getInputStream();
							final File file = new File(workingDir + File.separator + fileName);
							outStream = new FileOutputStream(file);
							final byte[] buf = new byte[WebServiceUtil.bufferSize];
							int len;
							while ((len = instream.read(buf)) > 0) {
								outStream.write(buf, 0, len);
							}
							if (instream != null) {
								instream.close();
							}

							if (outStream != null) {
								outStream.close();
							}
						} else {
							respStr = "Files other than tiff, tif and pdf formats are provided.";
							break;
						}
					}
					if (respStr.isEmpty()) {
						for (final String fileName : fileMap.keySet()) {
							final File file = new File(workingDir + File.separator + fileName);
							if (isGSTool) {
								logger.info("Start spliting multipage file using ghost script for file :" + fileName);
								imService.convertPdfToSinglePageTiffsUsingGSAPI(inputParams, file, outputParams, new File(outputDir
										+ File.separator + fileName), threadList);
							} else {
								logger.info("Start spliting multipage file using image magick for file :" + fileName);
								imService.convertPdfOrMultiPageTiffToTiffUsingIM(inputParams, file, outputParams, new File(outputDir
										+ File.separator + fileName), threadList);
							}
						}
						try {
							logger.info("Executing batch instance thread using thread pool");
							threadList.execute();
						} catch (final DCMAApplicationException e) {
							threadList.remove();
							FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
							throw new Exception(e.getMessage(), e);
						}

						ServletOutputStream out = null;
						ZipOutputStream zout = null;
						final String zipFileName = WebServiceUtil.serverOutputFolderName;
						resp.setContentType("application/x-zip\r\n");
						resp.setHeader("Content-Disposition", "attachment; filename=\"" + zipFileName
								+ FileType.ZIP.getExtensionWithDot() + "\"\r\n");
						try {
							out = resp.getOutputStream();
							zout = new ZipOutputStream(out);
							FileUtils.zipDirectory(outputDir, zout, zipFileName);
							resp.setStatus(HttpServletResponse.SC_OK);
						} catch (final IOException e) {
							respStr = "Unable to process web service request.Please check you ghostscipt or imagemagick configuration.";
						} finally {
							if (zout != null) {
								zout.close();
							}
							if (out != null) {
								out.flush();
							}
							FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
						}
					}
				}
			} else {
				respStr = "Improper input to server. Expected multipart request. Returning without processing the results.";
			}
		} catch (Exception e) {
			respStr = "Internal Server error.Please check logs for further details." + e;
		}
		if (!workingDir.isEmpty()) {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
			} catch (final IOException ioe) {

			}
		}
	}

	@RequestMapping(value = "/exportBatchClass", method = RequestMethod.POST)
	@ResponseBody
	public void exportBatchClass(final HttpServletRequest req, final HttpServletResponse resp) {
		String results = WebServiceUtil.EMPTY_STRING;
		try {
			String identifier = WebServiceUtil.EMPTY_STRING, imBaseFolderName = WebServiceUtil.EMPTY_STRING, searchSampleName = WebServiceUtil.EMPTY_STRING;
			for (final Enumeration<String> params = req.getParameterNames(); params.hasMoreElements();) {
				final String paramName = params.nextElement();
				if (paramName.equalsIgnoreCase("identifier")) {
					identifier = req.getParameter(paramName);
					logger.info("Value for identifier parameter is " + identifier);
					continue;
				}

				if (paramName.equalsIgnoreCase("image-classification-sample")) {
					imBaseFolderName = req.getParameter(paramName);
					logger.info("Value for imBaseFolderName parameter is " + imBaseFolderName);
					continue;
				}

				if (paramName.equalsIgnoreCase("lucene-search-classification-sample")) {
					searchSampleName = req.getParameter(paramName);
					logger.info("Value for searchSampleName parameter is " + searchSampleName);
					continue;
				}
			}
			results = WebServiceUtil.validateExportBatchClassAPI(imBaseFolderName, identifier, searchSampleName);
			if (results.isEmpty()) {
				req.getRequestDispatcher("/dcma-gwt-admin/exportBatchClassDownload").forward(req, resp);
				resp.setStatus(HttpServletResponse.SC_OK);
			}
		} catch (final Exception e) {
			results = "Internal Server error.Please check logs for further details." + e;
		}
		if (!results.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, results);
			} catch (final IOException ioe) {

			}
		}
	}

	@RequestMapping(value = "/extractKV", method = RequestMethod.POST)
	@ResponseBody
	public void extractKV(final HttpServletRequest req, final HttpServletResponse resp) {
		String respStr = WebServiceUtil.EMPTY_STRING;
		logger.info("Processing key value extraction using web service.");
		String workingDir = WebServiceUtil.EMPTY_STRING;

		if (req instanceof DefaultMultipartHttpServletRequest) {
			InputStream instream = null;
			OutputStream outStream = null;
			final String webServiceFolderPath = bsService.getWebServicesFolderPath();
			final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) req;
			final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();

			if (fileMap.size() == 2) {
				try {
					workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
					ExtractKVParams params = null;
					String filePath = WebServiceUtil.EMPTY_STRING;
					for (final String fileName : fileMap.keySet()) {
						final MultipartFile multipartFile = multiPartRequest.getFile(fileName);
						instream = multipartFile.getInputStream();
						if (fileName.toLowerCase().indexOf(FileType.XML.getExtension().toLowerCase()) > -1) {
							final Source source = XMLUtil.createSourceFromStream(instream);
							params = (ExtractKVParams) batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().unmarshal(source);
							continue;
						} else if (fileName.toLowerCase().indexOf(FileType.HTML.getExtension().toLowerCase()) > -1) {
							filePath = workingDir + File.separator + fileName;
							logger.info("HTML file for processing is " + filePath);
							final File file = new File(filePath);
							outStream = new FileOutputStream(file);
							final byte[] buf = new byte[WebServiceUtil.bufferSize];
							int len;
							while ((len = instream.read(buf)) > 0) {
								outStream.write(buf, 0, len);
							}
							continue;
						}
					}

					if (params != null && params.getParams().size() > 0 && !filePath.isEmpty()) {
						Params paramList = params.getParams().get(0);
						if (paramList.getLocationType() == null || paramList.getLocationType().isEmpty()) {
							respStr = "Please provide the location type. Accepted values are: TOP, RIGHT, LEFT, BOTTOM, TOP_RIGHT, TOP_LEFT, BOTTOM_LEFT, BOTTOM_RIGHT.";
						}
						if (respStr.isEmpty() && !LocationType.valuesAsStringList().contains(paramList.getLocationType())) {
							respStr = "Please provide the location type. Accepted values are: TOP, RIGHT, LEFT, BOTTOM, TOP_RIGHT, TOP_LEFT, BOTTOM_LEFT, BOTTOM_RIGHT.";
						}
						if (respStr.isEmpty()
								&& (paramList.getKeyPattern() == null || paramList.getKeyPattern().isEmpty()
										|| paramList.getValuePattern() == null || paramList.getValuePattern().isEmpty())) {
							respStr = "Please provide the key and value patterns.";
						}
						if (paramList.isAdvancedKV()) {
							if (respStr.isEmpty() && (paramList.getMultiplier() > 1 || paramList.getMultiplier() <= 0)) {
								respStr = "Please provide the multiplier for Advanced KV extraction. Range of values is between 0 to 1.";
							}
							if (respStr.isEmpty() && (paramList.getKVFetchValue() == null || paramList.getKVFetchValue().isEmpty())) {
								respStr = "Please provide the KVFetchValue for Advanced KV extraction. Expected values are:ALL, FIRST, LAST";
							}
							if (respStr.isEmpty() && !(KVFetchValue.valuesAsStringList().contains(paramList.getKVFetchValue()))) {
								respStr = "Please provide the KVFetchValue for Advanced KV extraction. Expected values are:ALL, FIRST, LAST";
							}
							if (respStr.isEmpty() && paramList.getLength() <= 0) {
								respStr = "Please provide the length value greater than zero with advanced KV extraction.";
							}
							if (respStr.isEmpty() && paramList.getWidth() <= 0) {
								respStr = "Please provide the width value greater than zero with advanced KV extraction.";
							}
						} else {
							if (respStr.isEmpty() && paramList.getNoOfWords() < 0) {
								respStr = "Please provide positive value for no of words with advanced KV extraction.";
							}
						}
					} else {
						respStr = "Please send an hocr file as input. Improper input to the server.Proceeding without processing";
					}
					if (respStr.isEmpty()) {
						// extract the hocr content from hocr files
						// generate hocr from html file
						HocrPages hocrPages = new HocrPages();
						List<HocrPage> hocrPageList = hocrPages.getHocrPage();
						HocrPage hocrPage = new HocrPage();
						String pageID = "PG0";
						hocrPage.setPageID(pageID);
						hocrPageList.add(hocrPage);
						bsService.hocrGenerationAPI(workingDir, "PG0", filePath, hocrPage);

						final List<DocField> updtDocList = new ArrayList<DocField>();
						final boolean isSuccess = kvService.extractKVDocumentFieldsFromHOCR(updtDocList, hocrPages, params);
						if (!isSuccess) {
							respStr = "Internal Server error.Please check logs for further details.";
						} else {
							logger.info("Generating document level fields for the output result");

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
								respStr = "Internal Server error.Please check logs for further details." + e.getMessage();
							}
						}
					}

				} catch (final XmlMappingException xmle) {
					respStr = "Error in mapping input XML or the hocr file in the desired format. Please send it in the specified format. Detailed exception is "
							+ xmle;
				} catch (final DCMAException dcmae) {
					respStr = "Error in processing request. Detailed exception is " + dcmae;
				} catch (final Exception e) {
					respStr = "Internal Server error.Please check logs for further details." + e;
				} finally {
					IOUtils.closeQuietly(instream);
					IOUtils.closeQuietly(outStream);
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			} else {
				respStr = "Improper input to server. Expected two files: hocr and xml parameter file. Returning without processing the results.";
			}
		} else {
			respStr = "Improper input to server. Expected multipart request. Returning without processing the results.";
		}

		if (!workingDir.isEmpty()) {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
			} catch (final IOException ioe) {

			}
		}
	}

	@RequestMapping(value = "/importBatchClass", method = RequestMethod.POST)
	@ResponseBody
	public void importBatchClass(final HttpServletRequest req, final HttpServletResponse resp) {
		String respStr = WebServiceUtil.EMPTY_STRING;
		logger.info("Start processing import batch class web service");
		String workingDir = WebServiceUtil.EMPTY_STRING;
		if (req instanceof DefaultMultipartHttpServletRequest) {
			InputStream instream = null;
			OutputStream outStream = null;
			final String webServiceFolderPath = bsService.getWebServicesFolderPath();
			final DefaultMultipartHttpServletRequest mPartReq = (DefaultMultipartHttpServletRequest) req;
			final MultiValueMap<String, MultipartFile> fileMap = mPartReq.getMultiFileMap();

			if (fileMap.size() == 2) {
				try {
					workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
					ImportBatchClassOptions option = null;
					String zipFilePath = WebServiceUtil.EMPTY_STRING;
					for (final String fileName : fileMap.keySet()) {
						final MultipartFile f = mPartReq.getFile(fileName);
						instream = f.getInputStream();
						if (fileName.toLowerCase().indexOf(FileType.XML.getExtension().toLowerCase()) > -1) {
							final Source source = XMLUtil.createSourceFromStream(instream);
							option = (ImportBatchClassOptions) batchSchemaDao.getJAXB2Template().getJaxb2Marshaller()
									.unmarshal(source);
							continue;
						} else if (fileName.toLowerCase().indexOf(FileType.ZIP.getExtension().toLowerCase()) > -1) {
							zipFilePath = workingDir + File.separator + fileName;
							logger.info("Zip file is using for importing batch class is " + zipFilePath);
							final File file = new File(zipFilePath);
							outStream = new FileOutputStream(file);
							final byte[] buf = new byte[WebServiceUtil.bufferSize];
							int len;
							while ((len = instream.read(buf)) > 0) {
								outStream.write(buf, 0, len);
							}
							continue;
						}
					}

					if (option != null && !zipFilePath.isEmpty()) {
						final Map<Boolean, String> results = importBatchService.validateInputXML(option);
						final String errorMessg = results.get(Boolean.FALSE);
						if (errorMessg != null && !errorMessg.isEmpty()) {
							respStr = errorMessg;
						} else {
							final File tempZipFile = new File(zipFilePath);
							final String tempOutputUnZipDir = tempZipFile.getParent() + File.separator
									+ tempZipFile.getName().substring(0, tempZipFile.getName().indexOf(WebServiceUtil.DOT));
							try {
								FileUtils.unzip(tempZipFile, tempOutputUnZipDir);
							} catch (final Exception e) {
								FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
								tempZipFile.delete();
								resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
										"Unable to unzip file. Returning without processing the results.");
							}

							option.setZipFilePath(tempOutputUnZipDir);
							logger.info("Importing batch class");
							final boolean isDeployed = deploymentService.isDeployed(option.getName());
							final Map<Boolean, String> resultsImport = importBatchService.importBatchClass(option, isDeployed, true,null);
							final String errorMessgImport = resultsImport.get(Boolean.FALSE);
							if (errorMessgImport != null && !errorMessgImport.isEmpty()) {
								respStr = errorMessgImport;
							}
						}
					} else {
						respStr = "Improper input to the server.Proceeding without processing";
					}

				} catch (final XmlMappingException xmle) {
					respStr = "Error in mapping input XML in the desired format. Please send it in the specified format. Detailed exception is "
							+ xmle;
				} catch (final Exception e) {
					respStr = "Internal Server error.Please check logs for further details." + e.getMessage();
				} finally {
					IOUtils.closeQuietly(instream);
					IOUtils.closeQuietly(outStream);

					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			} else {
				respStr = "Improper input to server. Expected two files: zip and xml file. Returning without processing the results.";
			}
		} else {
			respStr = "Improper input to server. Expected multipart request. Returning without processing the results.";
		}
		if (!workingDir.isEmpty()) {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
			} catch (final IOException ioe) {

			}
		}
	}

	@RequestMapping(value = "/runReporting", method = RequestMethod.POST)
	@ResponseBody
	public void runReporting(final HttpServletRequest req, final HttpServletResponse resp) {
		logger.info("Start processing the run reporting web service");
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
					if (installerPath == null || installerPath.isEmpty() || !installerPath.toLowerCase().contains("build.xml")) {
						respStr = "Improper input to server. Installer path not specified or it does not contain the build.xml path.";
					} else {
						logger.info("synchronizing the database");
						reportingService.syncDatabase(installerPath);
						break;
					}
				}

			} else {
				respStr = "Improper input to server. Expected multipart request. Returning without processing the results.";
			}
		} catch (final XmlMappingException xmle) {
			respStr = "Error in mapping input XML in the desired format. Please send it in the specified format. Detailed exception is "
					+ xmle;
		} catch (final Exception e) {
			respStr = "Internal Server error.Please check logs for further details." + e;
		}

		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
			} catch (final IOException ioe) {

			}
		}
	}

	@RequestMapping(value = "/getBatchInstanceList/{status}/", method = RequestMethod.GET)
	@ResponseBody
	public void getBatchInstanceList(@PathVariable("status") final String status, final HttpServletResponse resp,
			final HttpServletRequest req) {
		logger.info("Start processing get batch instance list from batch status web service");
		String respStr = WebServiceUtil.EMPTY_STRING;
		Set<String> batchInstancesId = null;
		if (!BatchInstanceStatus.valuesAsStringList().contains(status)) {
			respStr = "Invalid value for batch instance status. Please try again.";
		} else {
			final BatchInstanceStatus batchInstanceStatus = BatchInstanceStatus.valueOf(status);
			logger.info("Batch instance status is " + status);
			logger.info("Feteching batch instance list from the database");
			final List<BatchInstance> batchInstance = biService.getBatchInstByStatus(batchInstanceStatus);
			if (batchInstance != null && !batchInstance.isEmpty()) {
				String userName = req.getUserPrincipal().getName();
				Set<String> userRoles = userConnectivityService.getUserGroups(userName);

				// fetch the batch instances from batch instance groups
				batchInstancesId = batchInstanceGroupsService.getBatchInstanceIdentifierForUserRoles(userRoles);

				// fetch the list of batch instances from the batch instance table for batch classes having the given role.
				List<BatchClass> batchClasses = bcService.getAllBatchClassesByUserRoles(userRoles);
				for (BatchClass batchClass : batchClasses) {
					List<BatchInstance> eachBatchInstance = biService.getBatchInstByBatchClass(batchClass);
					for (BatchInstance bi : eachBatchInstance) {
						batchInstancesId.add(bi.getIdentifier());
					}
				}

				final BatchInstances batchinstances = new BatchInstances();

				final List<com.ephesoft.dcma.batch.schema.BatchInstances.BatchInstance> batchInstanceList = batchinstances
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
				StreamResult result;
				try {
					result = new StreamResult(resp.getOutputStream());
					resp.setStatus(HttpServletResponse.SC_OK);
					batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().marshal(batchinstances, result);
				} catch (final IOException e) {
					respStr = "Internal Server error.Please check logs for further details." + e;
				}
			} else {
				respStr = "No results found.";
			}
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
			} catch (final IOException ioe) {

			}
		}
	}

	@RequestMapping(value = "/getAllModulesWorkflowName/{batchClassIdentifier}", method = RequestMethod.GET)
	@ResponseBody
	public Set<String> getAllModulesWorkflowName(@PathVariable("batchClassIdentifier") final String identifier,
			final HttpServletResponse resp) {
		logger.info("Start processing web service for get all modules workflowName by batchClassId");
		Set<String> modulesWorkflowName = new HashSet<String>();
		String respStr = "";
		if (identifier != null && !identifier.isEmpty()) {
			final BatchClass batchClass = bcService.getBatchClassByIdentifier(identifier);
			if (batchClass != null) {
				List<BatchClassModule> modules = batchClass.getBatchClassModules();
				for (BatchClassModule bcm : modules) {
					modulesWorkflowName.add(bcm.getWorkflowName());
				}
				resp.setStatus(HttpServletResponse.SC_OK);
			} else {
				respStr = "Batch Class does not exist.";
			}

		} else {
			respStr = "Invalid input parameters.";

		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
			} catch (IOException e) {
			}
		}
		return modulesWorkflowName;
	}

	@RequestMapping(value = "/getBatchClassList", method = RequestMethod.GET)
	@ResponseBody
	public void getBatchClassList(final HttpServletResponse resp, final HttpServletRequest req) {
		logger.info("Start processing web service for get batch class list");
		logger.info("Fetching batch class from the database");
		String userName = req.getUserPrincipal().getName(); // ToDo fetch from authentication header.
		Set<String> userRoles = userConnectivityService.getUserGroups(userName);
		if (userRoles == null || userRoles.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "User not authorized to view this API.");
			} catch (final IOException ioe) {

			}
		} else {
			final List<BatchClass> batchClass = bcService.getAllBatchClassesByUserRoles(userRoles);
			final BatchClasses batchClasses = new BatchClasses();
			final List<com.ephesoft.dcma.batch.schema.BatchClasses.BatchClass> batchClassList = batchClasses.getBatchClass();
			logger.info("Total batch class found from the database is : " + batchClass.size());
			for (final BatchClass eachBatchClass : batchClass) {
				final com.ephesoft.dcma.batch.schema.BatchClasses.BatchClass batchClassLocal = new com.ephesoft.dcma.batch.schema.BatchClasses.BatchClass();
				batchClassLocal.setCurrentUser(eachBatchClass.getCurrentUser());
				batchClassLocal.setDescription(eachBatchClass.getDescription());
				batchClassLocal.setIdentifier(eachBatchClass.getIdentifier());
				batchClassLocal.setIsDeleted(eachBatchClass.isDeleted());
				batchClassLocal.setName(eachBatchClass.getName());
				batchClassLocal.setPriority(eachBatchClass.getPriority());
				batchClassLocal.setUncFolder(eachBatchClass.getUncFolder());
				batchClassLocal.setVersion(eachBatchClass.getVersion());
				batchClassList.add(batchClassLocal);

			}
			StreamResult result;
			try {
				result = new StreamResult(resp.getOutputStream());
				resp.setStatus(HttpServletResponse.SC_OK);
				batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().marshal(batchClasses, result);
			} catch (final IOException e) {
				try {
					resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Internal Server error.Please check logs for further details." + e.getMessage());
				} catch (final IOException ioe) {

				}
			}
		}
	}

	@RequestMapping(value = "/getRoles/{batchClassIdentifier}", method = RequestMethod.GET)
	@ResponseBody
	public Set<String> getRoles(@PathVariable("batchClassIdentifier") final String identifier, final HttpServletResponse resp) {
		logger.info("Start processing web service for get roles of batch class");
		Set<String> userGroups = null;
		String respStr = "";
		if (identifier != null && !identifier.isEmpty()) {
			final BatchClass batchClass = bcService.getBatchClassByIdentifier(identifier);
			if (batchClass != null) {
				logger.info("Fetching user roles for batch class identifier" + identifier);
				userGroups = batchClassGroupsService.getRolesForBatchClass(identifier);
				resp.setStatus(HttpServletResponse.SC_OK);
			} else {
				respStr = "Batch Class does not exist.";
			}

		} else {
			respStr = "Invalid input parameters.";

		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
			} catch (IOException e) {
			}
		}
		return userGroups;
	}

	@RequestMapping(value = "/getBatchClassForRole/{role}", method = RequestMethod.GET)
	@ResponseBody
	public Set<String> getBatchClassForRole(@PathVariable("role") final String role, final HttpServletResponse resp) {
		logger.info("Start processing web service for get batch classes for user role");
		Set<String> batchClasses = null;
		if (role != null && !role.isEmpty()) {
			final Set<String> userRoles = new HashSet<String>();
			userRoles.add(role);
			logger.info("Fetching batch classes for user role" + role);
			batchClasses = batchClassGroupsService.getBatchClassIdentifierForUserRoles(userRoles);
			resp.setStatus(HttpServletResponse.SC_OK);
		} else {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invalid input parameters.");
			} catch (IOException e) {
			}
		}
		return batchClasses;
	}

	@RequestMapping(value = "/getBatchInstanceForRole/{role}", method = RequestMethod.GET)
	@ResponseBody
	public Set<String> getBatchInstanceForRoles(@PathVariable("role") final String role, final HttpServletResponse resp) {
		logger.info("Start processing web service for get batch instances for user role");
		Set<String> batchInstances = null;
		if (role != null && !role.isEmpty()) {
			final Set<String> userRoles = new HashSet<String>();
			userRoles.add(role);
			logger.info("Fetching batch instances for user role" + role);
			// fetch the batch instances from batch instance groups
			batchInstances = batchInstanceGroupsService.getBatchInstanceIdentifierForUserRoles(userRoles);

			// fetch the list of batch instances from the batch instance table for batch classes having the given role.
			List<BatchClass> batchClasses = bcService.getAllBatchClassesByUserRoles(userRoles);
			for (BatchClass batchClass : batchClasses) {
				List<BatchInstance> eachBatchInstance = biService.getBatchInstByBatchClass(batchClass);
				for (BatchInstance batchInstance : eachBatchInstance) {
					batchInstances.add(batchInstance.getIdentifier());
				}
			}

			resp.setStatus(HttpServletResponse.SC_OK);
		} else {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invalid input parameters.");
			} catch (IOException e) {
			}
		}
		return batchInstances;
	}

	@RequestMapping(value = "/deleteBatchInstance/{identifier}", method = RequestMethod.GET)
	@ResponseBody
	public String deleteBatchInstance(@PathVariable("identifier") final String identifier, final HttpServletResponse resp,
			final HttpServletRequest req) {
		logger.info("Start processing web service for delete batch instance");
		String respStr = WebServiceUtil.EMPTY_STRING;
		String userName = req.getUserPrincipal().getName(); // ToDo fetch from authentication header.
		Set<String> userRoles = userConnectivityService.getUserGroups(userName);
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
					if (batchInstanceRoles.removeAll(userRoles)) {
						pluginPropertiesService.clearCache(identifier);
						jbpmService.deleteProcessInstance(batchInstance.getProcessInstanceKey());
						batchInstance.setStatus(BatchInstanceStatus.DELETED);
						biService.updateBatchInstance(batchInstance);
						final File uncFile = new File(batchInstance.getUncSubfolder());
						if (null != uncFile) {
							FileUtils.deleteDirectoryAndContentsRecursive(uncFile);
						}
						isSuccess = true;
					} else {
						respStr = "User is not authorized to perform operation on this batch instance." + identifier;
					}
				} else {
					respStr = "Either Batch instance does not exist with batch instance identifier " + identifier
							+ " or batch exists with incorrect status to be deleted. Batch instance should be of status:-"
							+ "ERROR, READY_FOR_REVIEW, READY_FOR_VALIDATION, RUNNING";
				}
			} catch (final Exception e) {
				respStr = "Error while deleting batch instance id:" + identifier + ".Please check logs for further details." + e;

			}
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
			} catch (final IOException ioe) {

			}
		}
		return (isSuccess ? "Batch deleted successfully." : "Failure while deleting batch instance.");
	}

	@RequestMapping(value = "/restartBatchInstance/{batchInstanceIdentifier}/{restartAtModuleName}", method = RequestMethod.GET)
	@ResponseBody
	public String restartBatchInstance(@PathVariable("batchInstanceIdentifier") final String identifier,
			@PathVariable("restartAtModuleName") String moduleName, final HttpServletResponse resp, final HttpServletRequest req) {
		logger.info("Start processing web service for restart batch instance");
		boolean isSuccess = false;
		String userName = req.getUserPrincipal().getName(); // ToDo fetch from authentication header.
		Set<String> userRoles = userConnectivityService.getUserGroups(userName);
		String respStr = WebServiceUtil.EMPTY_STRING;
		if (identifier != null && !identifier.isEmpty()) {
			BatchInstance batchInstance = biService.getBatchInstanceByIdentifier(identifier);
			// only batch instance with these status can be restarted
			if (batchInstance != null
					&& (batchInstance.getStatus().equals(BatchInstanceStatus.ERROR)
							|| batchInstance.getStatus().equals(BatchInstanceStatus.READY_FOR_REVIEW)
							|| batchInstance.getStatus().equals(BatchInstanceStatus.READY_FOR_VALIDATION) || batchInstance.getStatus()
							.equals(BatchInstanceStatus.RUNNING))) {

				Set<String> batchInstanceRoles = biService.getRolesForBatchInstance(batchInstance);
				if (batchInstanceRoles.removeAll(userRoles)) {
					String executedBatchInstanceModules = batchInstance.getExecutedModules();
					String[] executedModulesArray = executedBatchInstanceModules.split(";");
					final String batchClassIdentifier = biService.getBatchClassIdentifier(identifier);
					if (batchClassIdentifier != null) {
						final BatchClassModule batchClassModuleItem = bcModuleService.getBatchClassModuleByWorkflowName(
								batchClassIdentifier, moduleName);
						if (batchClassModuleItem != null) {
							for (String string : executedModulesArray) {
								if (string.equalsIgnoreCase(String.valueOf(batchClassModuleItem.getId()))) {
									isSuccess = true;
									break;
								}
							}
						}

					}
					final boolean isZipSwitchOn = bsService.isZipSwitchOn();
					logger.info("Zipped Batch XML switch is:" + isZipSwitchOn);

					final String activeModule = workflowService.getActiveModule(batchInstance);

					if (isSuccess) {
						respStr = processRestartingBatchInternal(identifier, moduleName, respStr, batchInstance, batchClassIdentifier,
								isZipSwitchOn, activeModule);
					} else {
						isSuccess = false;
						respStr = "Invalid parameter for restarting batch instance.";
					}
				} else {
					respStr = "User is not authorized to perform operation on this batch instance." + identifier;
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
			} catch (final IOException ioe) {
			}
		}
		return (isSuccess ? "Batch restarted successfully." : "Failure while restarting batch instance.");
	}

	private String processRestartingBatchInternal(final String identifier, String moduleName, String respStr,
			BatchInstance batchInstance, final String batchClassIdentifier, final boolean isZipSwitchOn, final String activeModule) {
		try {
			jbpmService.deleteProcessInstance(batchInstance.getProcessInstanceKey());
			final BatchInstanceThread batchInstanceThread = ThreadPool.getBatchInstanceThreadList(identifier);
			if (batchInstanceThread != null) {
				batchInstanceThread.remove();
			}
			String executedModules = WebServiceUtil.EMPTY_STRING;
			if (moduleName != null) {
				Properties properties = WebServiceUtil.fetchConfig();

				importBatchService.updateBatchFolders(properties, batchInstance, moduleName, isZipSwitchOn);
				executedModules = batchInstance.getExecutedModules();
				if (executedModules != null) {
					if (batchClassIdentifier != null) {
						final BatchClassModule batchClassModuleItem = bcModuleService.getBatchClassModuleByWorkflowName(
								batchClassIdentifier, moduleName);
						final BatchClass batchClass = bcService.getBatchClassByIdentifier(batchClassIdentifier);
						final List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
						if (null != batchClassModules) {
							for (final BatchClassModule batchClassModule : batchClassModules) {
								if (batchClassModule != null && batchClassModule.getModule() != null) {
									if (batchClassModule.getOrderNumber() >= batchClassModuleItem.getOrderNumber()) {
										final String replaceText = batchClassModule.getModule().getId() + ";";
										executedModules = executedModules.replace(replaceText, WebServiceUtil.EMPTY_STRING);
									}
								}
							}
						}
					}
				}
			} else {
				executedModules = WebServiceUtil.EMPTY_STRING;
			}

			batchInstance = biService.getBatchInstanceByIdentifier(identifier);
			batchInstance.setExecutedModules(executedModules);
			batchInstance = biService.merge(batchInstance);

			if (activeModule != null && activeModule.contains("Workflow_Continue_Check")) {
				logger.error("Error in restarting batch instance : " + identifier);
			}
			workflowService.startWorkflow(batchInstance.getBatchInstanceID(), moduleName);
		} catch (final IOException ex) {
			respStr = "Cannot open and load backUpService properties file." + ex;
			logger.error(respStr, ex);
		} catch (final DCMAApplicationException dcmae) {
			respStr = "Error while plugin processing." + dcmae;
			logger.error(respStr, dcmae);
		} catch (final Exception e) {
			respStr = "Error in restarting batch instance " + identifier + e;
			logger.error(respStr, e);

			// update the batch instance to ERROR state.
			if (jbpmService != null) {
				jbpmService.deleteProcessInstance(batchInstance.getProcessInstanceKey());
			}
			biService.updateBatchInstanceStatusByIdentifier(batchInstance.getIdentifier(), BatchInstanceStatus.ERROR);
		}
		return respStr;

	}

	@RequestMapping(value = "/addUserRolesToBatchInstanceIdentifier/{batchInstanceIdentifier}/{userRole}", method = RequestMethod.GET)
	@ResponseBody
	public String addUserRolesToBatchInstanceIdentifier(@PathVariable("batchInstanceIdentifier") final String identifier,
			@PathVariable("userRole") final String userRole, final HttpServletResponse resp) throws IOException {
		logger.info("Start processing web service for adding user roles to batch instance identifier");
		String isAdded = WebServiceUtil.EMPTY_STRING;
		if (userRole != null && userRole.isEmpty() && identifier != null && identifier.isEmpty()) {
			BatchInstance bi = biService.getBatchInstanceByIdentifier(identifier);
			if (bi == null) {
				isAdded = "Invalid batch instance id passed to web service. Please pass valid parameter.";
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, isAdded);
			} else {
				try {
					batchInstanceGroupsService.addUserRolesToBatchInstanceIdentifier(identifier, userRole);
					resp.setStatus(HttpServletResponse.SC_OK);
					isAdded = "User role is added successfully to batch instance.";
				} catch (Exception e) {
					isAdded = "Error in adding roles to batch instance identifier " + e.getMessage();
					resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, isAdded);
				}
			}
		} else {
			isAdded = "Invalid parameters passed to web service. Please pass valid parameter.";
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, isAdded);
		}
		return isAdded;
	}

	@RequestMapping(value = "/createSearchablePDF", method = RequestMethod.POST)
	@ResponseBody
	public void createSearchablePDF(final HttpServletRequest request, final HttpServletResponse response) {
		logger.info("Start processing web service for create searchable pdf");
		String respStr = WebServiceUtil.EMPTY_STRING;
		String workingDir = WebServiceUtil.EMPTY_STRING;
		if (request instanceof DefaultMultipartHttpServletRequest) {
			try {
				final String webServiceFolderPath = bsService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
				final String outputDir = WebServiceUtil.createWebServiceOutputDir(workingDir);

				InputStream instream = null;
				OutputStream outStream = null;
				final DefaultMultipartHttpServletRequest multipartRequest = (DefaultMultipartHttpServletRequest) request;
				final BatchInstanceThread batchInstanceThread = new BatchInstanceThread(new File(workingDir).getName() + Math.random());

				final String isColorImage = request.getParameter("isColorImage");
				final String isSearchableImage = request.getParameter("isSearchableImage");
				final String outputPDFFileName = request.getParameter("outputPDFFileName");
				final String projectFile = request.getParameter("projectFile");
				String results = WebServiceUtil.validateSearchableAPI(outputPDFFileName, projectFile, FileType.PDF
						.getExtensionWithDot(), isSearchableImage, isColorImage);
				if (!results.isEmpty()) {
					respStr = results;
				} else {
					logger.info("Value of isColorImage" + isColorImage);
					logger.info("Value of isSearchableImage" + isSearchableImage);
					logger.info("Value of outputPDFFileName" + outputPDFFileName);
					logger.info("Value of projectFile" + projectFile);

					final MultiValueMap<String, MultipartFile> fileMap = multipartRequest.getMultiFileMap();
					for (final String fileName : fileMap.keySet()) {
						if (fileName.toLowerCase().indexOf(WebServiceUtil.RSP_EXTENSION) > -1
								|| fileName.toLowerCase().indexOf(FileType.TIF.getExtension()) > -1
								|| fileName.toLowerCase().indexOf(FileType.TIFF.getExtension()) > -1) {
							// only tiffs and RSP file is expected
							final MultipartFile f = multipartRequest.getFile(fileName);
							instream = f.getInputStream();
							final File file = new File(workingDir + File.separator + fileName);
							outStream = new FileOutputStream(file);
							final byte[] buf = new byte[WebServiceUtil.bufferSize];
							int len;
							while ((len = instream.read(buf)) > 0) {
								outStream.write(buf, 0, len);
							}
							if (instream != null) {
								instream.close();
							}
							if (outStream != null) {
								outStream.close();
							}
						} else {
							respStr = "Only tiff, tif and rsp files expected.";
							break;
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

						String rspProjectFile = workingDir + File.separator + projectFile;

						File rspFile = new File(rspProjectFile);

						if (rspProjectFile == null || !rspFile.exists()) {
							respStr = "Invalid project file. Please verify the project file.";
						}

						if (respStr.isEmpty()) {
							final String[] pages = new String[imageFiles.length + 1];
							int index = 0;
							for (final String imageFileName : imageFiles) {
								pages[index] = workingDir + File.separator + imageFileName;
								index++;

								if (WebServiceUtil.TRUE.equalsIgnoreCase(isColorImage)) {
									try {
										logger.info("Generating png image files");
										imService.generatePNGForImage(new File(workingDir + File.separator + imageFileName));
										final String pngFileName = imageFileName.substring(0, imageFileName
												.lastIndexOf(WebServiceUtil.DOT))
												+ FileType.PNG.getExtensionWithDot();
										recostarService.createOCR(projectFile, workingDir, WebServiceUtil.ON_STRING, pngFileName,
												batchInstanceThread, workingDir);
									} catch (final DCMAException e) {
										FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
										respStr = "Error in generating plugin output." + imageFileName + ". " + e;
									}
								} else {
									try {
										recostarService.createOCR(projectFile, workingDir, WebServiceUtil.OFF_STRING, imageFileName,
												batchInstanceThread, workingDir);
									} catch (final DCMAException e) {
										FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
										respStr = "Error in generating plugin output." + imageFileName + ". " + e;
									}
								}
							}
							try {
								logger.info("Generating HOCR file for input images.");
								batchInstanceThread.execute();
								batchInstanceThread.remove();
								final String outputPDFFile = workingDir + File.separator + outputPDFFileName;
								pages[index] = outputPDFFile;
								imService.createSearchablePDF(isColorImage, isSearchableImage, workingDir, pages, batchInstanceThread,
										WebServiceUtil.DOCUMENTID);
								batchInstanceThread.execute();
								logger.info("Copying output searchable file");
								FileUtils.copyFile(new File(outputPDFFile), new File(outputDir + File.separator + outputPDFFileName));
							} catch (final DCMAApplicationException e) {
								batchInstanceThread.remove();
								FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
								respStr = "Error in generating searchable pdf." + e;
							}
							ServletOutputStream out = null;
							ZipOutputStream zout = null;
							final String zipFileName = WebServiceUtil.serverOutputFolderName;
							response.setContentType("application/x-zip\r\n");
							response.setHeader("Content-Disposition", "attachment; filename=\"" + zipFileName
									+ FileType.ZIP.getExtensionWithDot() + "\"\r\n");
							try {
								out = response.getOutputStream();
								zout = new ZipOutputStream(out);
								FileUtils.zipDirectory(outputDir, zout, zipFileName);
								response.setStatus(HttpServletResponse.SC_OK);
							} catch (final IOException e) {
								respStr = "Unable to process web service request.Please try again." + e;
							} finally {
								// clean up code
								if (zout != null) {
									zout.close();
								}
								if (out != null) {
									out.flush();
								}
								FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
							}
						}
					}

				}
			} catch (Exception e) {
				respStr = "Internal Server error.Please check logs for further details." + e;
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			}
		} else {
			respStr = "Improper input to server. Expected multipart request. Returning without processing the results.";
		}
		if (!workingDir.isEmpty()) {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
		}
		if (!respStr.isEmpty()) {
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
			} catch (final IOException ioe) {

			}
		}
	}

	@RequestMapping(value = "/createOCR", method = RequestMethod.POST)
	@ResponseBody
	public void createOCR(final HttpServletRequest req, final HttpServletResponse resp) {
		logger.info("Start processing web service for create OCRing");
		String respStr = WebServiceUtil.EMPTY_STRING;
		String workingDir = WebServiceUtil.EMPTY_STRING;
		if (req instanceof DefaultMultipartHttpServletRequest) {
			try {
				final String webServiceFolderPath = bsService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
				final String outputDir = WebServiceUtil.createWebServiceOutputDir(workingDir);

				InputStream instream = null;
				OutputStream outStream = null;

				final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) req;

				final BatchInstanceThread batchInstanceThread = new BatchInstanceThread(new File(workingDir).getName() + Math.random());

				final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();

				if (!(fileMap.size() >= 2 && fileMap.size() <= 3)) {
					respStr = "Invalid number of files. We are supposed only 3 files.";
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
						int len;
						while ((len = instream.read(buf)) > 0) {
							outStream.write(buf, 0, len);
						}
						if (instream != null) {
							instream.close();
						}

						if (outStream != null) {
							outStream.close();
						}
					}

					final File xmlFile = new File(workingDir + File.separator + xmlFileName);
					final FileInputStream inputStream = new FileInputStream(xmlFile);
					Source source = XMLUtil.createSourceFromStream(inputStream);
					final WebServiceParams webServiceParams = (WebServiceParams) batchSchemaDao.getJAXB2Template()
							.getJaxb2Marshaller().unmarshal(source);

					List<Param> paramList = webServiceParams.getParams().getParam();
					if (paramList == null || paramList.isEmpty()) {
						FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
						respStr = "Improper input to server. Parameter XML is incorrect. Returning without processing the results.";
					} else {
						String ocrEngine = WebServiceUtil.EMPTY_STRING;
						String colorSwitch = WebServiceUtil.EMPTY_STRING;
						String projectFile = WebServiceUtil.EMPTY_STRING;
						String tesseractVersion = WebServiceUtil.EMPTY_STRING;
						String cmdLanguage = WebServiceUtil.EMPTY_STRING;
						for (final Param param : paramList) {
							if (param.getName().equalsIgnoreCase("ocrEngine")) {
								ocrEngine = param.getValue();
								continue;
							}
							if (param.getName().equalsIgnoreCase("colorSwitch")) {
								colorSwitch = param.getValue();
								continue;
							}
							if (param.getName().equalsIgnoreCase("projectFile")) {
								projectFile = param.getValue();
								logger.info("Project file for recostar is :" + projectFile);
								continue;
							}
							if (param.getName().equalsIgnoreCase("tesseractVersion")) {
								tesseractVersion = param.getValue();
								logger.info("Tesseract version is: " + tesseractVersion);
								continue;
							}
							if (param.getName().equalsIgnoreCase("cmdLanguage")) {
								// supported values are "eng" and "tha" for now provided tesseract engine is learnt.
								cmdLanguage = param.getValue();
								logger.info("cmd langugage is :" + cmdLanguage);
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
								logger.info("Picking up the png file for processing.");
								fileNames = file.list(new CustomFileFilter(false, FileType.PNG.getExtensionWithDot()));
							} else {
								logger.info("Picking up the tif file for processing.");
								fileNames = file.list(new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(), FileType.TIFF
										.getExtensionWithDot()));
							}
							logger.info("Number of file is:" + fileNames.length);
							logger.info("OcrEngine used for generating ocr is :" + ocrEngine);
							if (ocrEngine.equalsIgnoreCase("recostar")) {
								if (fileNames != null && fileNames.length > 0) {
									for (final String fileName : fileNames) {
										try {
											logger.info("File processing for recostar is :" + fileName);
											recostarService.createOCR(projectFile, workingDir, colorSwitch, fileName,
													batchInstanceThread, outputDir);
										} catch (final DCMAException e) {
											respStr = "Error occuring while creating OCR file using recostar. Please try again." + e;
											break;
										}
									}
								} else {
									respStr = "Improper input to server. No tiff/png files provided.";
								}
							} else if (ocrEngine.equalsIgnoreCase("tesseract")) {
								if (fileNames != null && fileNames.length > 0) {
									for (final String fileName : fileNames) {
										try {
											logger.info("File processing for ocr with tesseract is :" + fileName);
											tesseractService.createOCR(workingDir, colorSwitch, fileName, batchInstanceThread,
													outputDir, cmdLanguage, tesseractVersion);
										} catch (final DCMAException e) {
											respStr = "Error occuring while creating OCR file using tesseract. Please try again." + e;
											break;
										}
									}
								} else {
									respStr = "Improper input to server. No tiff/png files provided.";

								}
							} else {
								respStr = "Please select valid tool for generating OCR file.";
							}
							if (respStr.isEmpty()) {
								try {
									batchInstanceThread.execute();
									if (ocrEngine.equalsIgnoreCase("recostar")) {
										for (final String fileName : fileNames) {
											final String recostarXMLFileName = fileName.substring(0, fileName
													.lastIndexOf(WebServiceUtil.DOT))
													+ FileType.XML.getExtensionWithDot();
											try {
												FileUtils.copyFile(new File(workingDir + File.separator + recostarXMLFileName),
														new File(outputDir + File.separator + recostarXMLFileName));
											} catch (final Exception e) {
												respStr = "Error while generating copying result file." + e;
												break;
											}
										}
									}
								} catch (final DCMAApplicationException e) {
									respStr = "Exception while generating ocr using threadpool" + e;
								}

								ServletOutputStream out = null;
								ZipOutputStream zout = null;
								final String zipFileName = WebServiceUtil.serverOutputFolderName;
								resp.setContentType("application/x-zip\r\n");
								resp.setHeader("Content-Disposition", "attachment; filename=\"" + zipFileName
										+ FileType.ZIP.getExtensionWithDot() + "\"\r\n");
								try {
									out = resp.getOutputStream();
									zout = new ZipOutputStream(out);
									FileUtils.zipDirectory(outputDir, zout, zipFileName);
									resp.setStatus(HttpServletResponse.SC_OK);
								} catch (final IOException e) {
									respStr = "Error in creating output zip file.Please try again." + e;
								} finally {
									if (zout != null) {
										zout.close();
									}
									if (out != null) {
										out.flush();
									}
									FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
								}
							}
						}
					}
				}
			} catch (final XmlMappingException xmle) {
				respStr = "Error in mapping input XML in the desired format. Please send it in the specified format. Detailed exception is "
						+ xmle;
			} catch (final DCMAException dcmae) {
				respStr = "Error in processing request. Detailed exception is " + dcmae;
			} catch (final Exception e) {
				respStr = "Internal Server error.Please check logs for further details." + e;
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			}
		} else {
			respStr = "Improper input to server. Expected multipart request. Returning without processing the results.";
		}
		if (!workingDir.isEmpty()) {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
			} catch (final IOException ioe) {

			}
		}
	}

	@RequestMapping(value = "/classifyImage", method = RequestMethod.POST)
	@ResponseBody
	public void classifyImage(final HttpServletRequest req, final HttpServletResponse resp) {
		logger.info("Start processing web service for classifyImage.");
		String respStr = WebServiceUtil.EMPTY_STRING;
		String workingDir = WebServiceUtil.EMPTY_STRING;

		if (req instanceof DefaultMultipartHttpServletRequest) {
			try {
				final String webServiceFolderPath = bsService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);

				InputStream instream = null;
				OutputStream outStream = null;

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
					BatchClass bc = bcService.getBatchClassByIdentifier(batchClassId);
					if (bc == null) {
						respStr = "Batch class with the specified identifier does not exist.";
					} else {
						BatchPlugin createThumbPlugin = batchClassPPService.getPluginProperties(batchClassId,
								ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN);
						BatchPlugin classifyImgPlugin = batchClassPPService.getPluginProperties(batchClassId,
								ImageMagicKConstants.CLASSIFY_IMAGES_PLUGIN);
						BatchPlugin docAssemblyPlugin = batchClassPPService.getPluginProperties(batchClassId,
								DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN);
						if (createThumbPlugin == null || classifyImgPlugin == null || docAssemblyPlugin == null) {
							respStr = "Either create Thumbnails plugin or Classify Image plugin or document assembly plugin does not exist for the specified batch id.";
						} else if (createThumbPlugin
								.getPluginConfigurations(ImageMagicProperties.CREATE_THUMBNAILS_OUTPUT_IMAGE_PARAMETERS) == null
								|| createThumbPlugin.getPluginConfigurations(ImageMagicProperties.CREATE_THUMBNAILS_COMP_THUMB_WIDTH) == null
								|| createThumbPlugin.getPluginConfigurations(ImageMagicProperties.CREATE_THUMBNAILS_COMP_THUMB_HEIGHT) == null) {
							respStr = "Create Thumbnails Height or width or output image parameters does not exist for the specified batch id.";
						} else if (classifyImgPlugin.getPluginConfigurations(ImageMagicProperties.CLASSIFY_IMAGES_COMP_METRIC) == null
								|| classifyImgPlugin.getPluginConfigurations(ImageMagicProperties.CLASSIFY_IMAGES_MAX_RESULTS) == null
								|| classifyImgPlugin.getPluginConfigurations(ImageMagicProperties.CLASSIFY_IMAGES_FUZZ_PERCNT) == null) {
							respStr = "Classify Images comp metric or fuzz percent or max results does not exist for the specified batch id.";
						} else if (docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP_MP_LP) == null
								|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP) == null
								|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_MP) == null
								|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_LP) == null
								|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP_LP) == null
								|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP_MP) == null
								|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_MP_LP) == null) {
							respStr = "Incomplete properties of the Document assembler plugin for the specified batch id.";
						}
					}
				}
				if (respStr.isEmpty()) {

					final String outputParams = batchClassPPService.getPropertyValue(batchClassId,
							ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN,
							ImageMagicProperties.CREATE_THUMBNAILS_OUTPUT_IMAGE_PARAMETERS);

					final MultiValueMap<String, MultipartFile> fileMap = multipartReq.getMultiFileMap();

					if (fileMap.size() == 1) {
						String[][] sListOfTiffFiles = new String[fileMap.size()][3];
						for (final String fileName : fileMap.keySet()) {
							// only single tiff file is expected as input
							if ((fileName.toLowerCase().indexOf(FileType.TIF.getExtension()) > -1 || fileName.toLowerCase().indexOf(
									FileType.TIFF.getExtension()) > -1)) {

								final MultipartFile f = multipartReq.getFile(fileName);
								instream = f.getInputStream();
								final File file = new File(workingDir + File.separator + fileName);
								outStream = new FileOutputStream(file);
								final byte buf[] = new byte[1024];
								int len;
								while ((len = instream.read(buf)) > 0) {
									outStream.write(buf, 0, len);
								}
								if (instream != null) {
									instream.close();
								}

								if (outStream != null) {
									outStream.close();
								}
								if (TIFFUtil.getTIFFPageCount(file.getAbsolutePath()) > 1) {
									respStr = "Improper input to server. Expected only one single page tiff file. Returning without processing the results.";
								}
								break;
							} else {
								respStr = "Improper input to server. Expected only one tiff file. Returning without processing the results.";
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
									respStr = "Internal Server error.Please check logs for further details." + e;
								}
							} catch (final DCMAApplicationException e) {
								threadList.remove();
								respStr = "Error while executing threadpool. Detailed exception is " + e;
							} catch (final DCMAException e) {
								threadList.remove();
								respStr = "Error while executing threadpool. Detailed exception is " + e;
							}
						}
					} else {
						respStr = "Improper input to server. Expected only one tiff file. Returning without processing the results.";
					}
				}
			} catch (final Exception e) {
				respStr = "Internal Server error.Please check logs for further details." + e;
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			}
		} else {
			respStr = "Improper input to server. Expected multipart request. Returning without processing the results.";
		}
		if (!workingDir.isEmpty()) {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
			} catch (final IOException ioe) {

			}
		}
	}

	@RequestMapping(value = "/classifyBarcodeImage", method = RequestMethod.POST)
	@ResponseBody
	public void classifyBarcodeImage(final HttpServletRequest req, final HttpServletResponse resp) {
		logger.info("Start processing web service for classifyBarcode.");
		String respStr = WebServiceUtil.EMPTY_STRING;
		String workingDir = WebServiceUtil.EMPTY_STRING;

		if (req instanceof DefaultMultipartHttpServletRequest) {
			try {
				final String webServiceFolderPath = bsService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);

				InputStream instream = null;
				OutputStream outStream = null;

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
					BatchClass bc = bcService.getBatchClassByIdentifier(batchClassId);
					if (bc == null) {
						respStr = "Batch class with the specified identifier does not exist.";
					} else {
						BatchPlugin barcodeReader = batchClassPPService.getPluginProperties(batchClassId,
								ICommonConstants.BARCODE_READER_PLUGIN);
						BatchPlugin docAssemblyPlugin = batchClassPPService.getPluginProperties(batchClassId,
								DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN);
						if (barcodeReader == null || docAssemblyPlugin == null) {
							respStr = "Either Barcode Reader plugin or document assembly plugin does not exist for the specified batch id.";
						} else if (docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_BARCODE_CONFIDENCE) == null) {
							respStr = "Incomplete properties of the Document assembler plugin for the specified batch id.";
						} else if (barcodeReader.getPluginConfigurations(BarcodeProperties.BARCODE_VALID_EXTNS) == null
								|| barcodeReader.getPluginConfigurations(BarcodeProperties.BARCODE_READER_TYPES) == null
								|| barcodeReader.getPluginConfigurations(BarcodeProperties.MAX_CONFIDENCE) == null
								|| barcodeReader.getPluginConfigurations(BarcodeProperties.MIN_CONFIDENCE) == null) {
							respStr = "Incomplete properties of the Barcode reader plugin for the specified batch id.";
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
							if ((fileName.toLowerCase().indexOf(FileType.TIF.getExtension()) > -1 || fileName.toLowerCase().indexOf(
									FileType.TIFF.getExtension()) > -1)) {

								final MultipartFile f = multipartReq.getFile(fileName);
								instream = f.getInputStream();
								final File file = new File(workingDir + File.separator + fileName);
								outStream = new FileOutputStream(file);
								final byte buf[] = new byte[1024];
								int len;
								while ((len = instream.read(buf)) > 0) {
									outStream.write(buf, 0, len);
								}
								if (instream != null) {
									instream.close();
								}

								if (outStream != null) {
									outStream.close();
								}
								if (TIFFUtil.getTIFFPageCount(file.getAbsolutePath()) > 1) {
									respStr = "Improper input to server. Expected only one single page tiff file. Returning without processing the results.";
								}
								tiffFileName = file.getName();
								break;
							} else {
								respStr = "Improper input to server. Expected only one tiff file. Returning without processing the results.";
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
									respStr = "Internal Server error.Please check logs for further details." + e;
								}
							} catch (final DCMAApplicationException e) {
								respStr = "Error while executing plugin. Detailed exception is " + e;
							}
						}
					} else {
						respStr = "Improper input to server. Expected only one html file. Returning without processing the results.";
					}
				}
			} catch (final XmlMappingException xmle) {
				respStr = "Error in mapping input XML in the desired format. Please send it in the specified format. Detailed exception is "
						+ xmle;
			} catch (final DCMAException dcmae) {
				respStr = "Error in processing request. Detailed exception is " + dcmae;
			} catch (final Exception e) {
				respStr = "Internal Server error.Please check logs for further details." + e;
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			}
		} else {
			respStr = "Improper input to server. Expected multipart request. Returing without processing the results.";
		}

		if (!workingDir.isEmpty()) {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
			} catch (final IOException ioe) {

			}
		}
	}

	@RequestMapping(value = "/classifyHocr", method = RequestMethod.POST)
	@ResponseBody
	public void classifyHocr(final HttpServletRequest req, final HttpServletResponse resp) {
		logger.info("Start processing web service for classifyHocr.");
		String respStr = WebServiceUtil.EMPTY_STRING;
		String workingDir = WebServiceUtil.EMPTY_STRING;

		if (req instanceof DefaultMultipartHttpServletRequest) {
			try {
				final String webServiceFolderPath = bsService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);

				InputStream instream = null;
				OutputStream outStream = null;

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
				} else {
					BatchClass bc = bcService.getBatchClassByIdentifier(batchClassId);
					if (bc == null) {
						respStr = "Batch class with the specified identifier does not exist.";
					} else {
						BatchPlugin searchClassPlugin = batchClassPPService.getPluginProperties(batchClassId,
								ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN);
						BatchPlugin docAssemblyPlugin = batchClassPPService.getPluginProperties(batchClassId,
								DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN);
						if (searchClassPlugin == null || docAssemblyPlugin == null) {
							respStr = "Either Search Classification plugin or document assembly plugin does not exist for the specified batch id.";
						} else if (docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP_MP_LP) == null
								|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP) == null
								|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_MP) == null
								|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_LP) == null
								|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP_LP) == null
								|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP_MP) == null
								|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_MP_LP) == null) {
							respStr = "Incomplete properties of the Document assembler plugin for the specified batch id.";
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
							respStr = "Incomplete properties of the Search Classification plugin for the specified batch id.";
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
							if (fileName.toLowerCase().indexOf(FileType.HTML.getExtension()) > -1) {
								// only HTML file is expected
								hocrFileName = fileName;
								final MultipartFile f = multipartReq.getFile(fileName);
								instream = f.getInputStream();
								final File file = new File(workingDir + File.separator + fileName);
								outStream = new FileOutputStream(file);
								final byte buf[] = new byte[1024];
								int len;
								while ((len = instream.read(buf)) > 0) {
									outStream.write(buf, 0, len);
								}
								if (instream != null) {
									instream.close();
								}

								if (outStream != null) {
									outStream.close();
								}
								break;
							} else {
								respStr = "Improper input to server. Expected only one html file. Returning without processing the results.";
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
							String pageID = "PG0";
							hocrPage.setPageID(pageID);
							hocrPageList.add(hocrPage);
							bsService.hocrGenerationAPI(workingDir, "PG0", fileName, hocrPage);

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
									respStr = "Internal Server error.Please check logs for further details." + e;
								}
							} catch (final DCMAApplicationException e) {
								respStr = "Error while executing plugin. Detailed exception is " + e;
							}
						}
					} else {
						respStr = "Improper input to server. Expected only one html file. Returning without processing the results.";
					}
				}
			} catch (final XmlMappingException xmle) {
				respStr = "Error in mapping input XML in the desired format. Please send it in the specified format. Detailed exception is "
						+ xmle;
			} catch (final DCMAException dcmae) {
				respStr = "Error in processing request. Detailed exception is " + dcmae;
			} catch (final Exception e) {
				respStr = "Internal Server error.Please check logs for further details." + e;
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			}
		} else {
			respStr = "Improper input to server. Expected multipart request. Returing without processing the results.";
		}

		if (!workingDir.isEmpty()) {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
			} catch (final IOException ioe) {

			}
		}
	}

	@RequestMapping(value = "/createMultiPageFile", method = RequestMethod.POST)
	@ResponseBody
	public void createMultiPageFile(final HttpServletRequest req, final HttpServletResponse resp) {
		logger.info("Start processing web service for createMultiPageFile.");
		String respStr = WebServiceUtil.EMPTY_STRING;
		String workingDir = WebServiceUtil.EMPTY_STRING;
		if (req instanceof DefaultMultipartHttpServletRequest) {
			try {
				final String webServiceFolderPath = bsService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
				final String outputDir = WebServiceUtil.createWebServiceOutputDir(workingDir);
				InputStream instream = null;
				OutputStream outStream = null;
				final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) req;
				final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();
				String xmlFileName = WebServiceUtil.EMPTY_STRING;
				List<File> fileList = new ArrayList<File>();
				for (final String fileName : fileMap.keySet()) {
					if (fileName.endsWith(FileType.XML.getExtensionWithDot()) || fileName.endsWith(FileType.TIF.getExtensionWithDot())
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
						int len;
						while ((len = instream.read(buf)) > 0) {
							outStream.write(buf, 0, len);
						}
						if (instream != null) {
							instream.close();
						}
						if (outStream != null) {
							outStream.close();
						}
					} else {
						respStr = "Expected only tif, tiff files.";
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
						respStr = "Improper input to server. Parameter XML is incorrect. Returning without processing the results.";
					} else {
						List<Param> paramList = webServiceParams.getParams().getParam();
						String imageProcessingAPI = WebServiceUtil.EMPTY_STRING;
						String pdfOptimizationParams = WebServiceUtil.EMPTY_STRING;
						String multipageTifSwitch = WebServiceUtil.EMPTY_STRING;
						String pdfOptimizationSwitch = WebServiceUtil.EMPTY_STRING, ghostscriptPdfParameters = WebServiceUtil.EMPTY_STRING;
						for (final Param param : paramList) {
							if (param.getName().equalsIgnoreCase("imageProcessingAPI")) {
								imageProcessingAPI = param.getValue();
								continue;
							}
							if (param.getName().equalsIgnoreCase("pdfOptimizationParams")) {
								pdfOptimizationParams = param.getValue();
								continue;
							}
							if (param.getName().equalsIgnoreCase("multipageTifSwitch")) {
								multipageTifSwitch = param.getValue();
								continue;
							}
							if (param.getName().equalsIgnoreCase("pdfOptimizationSwitch")) {
								pdfOptimizationSwitch = param.getValue();
								continue;
							}
							if (param.getName().equalsIgnoreCase("ghostscriptPdfParameters")) {
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
							final String zipFileName = WebServiceUtil.serverOutputFolderName;
							resp.setContentType("application/x-zip\r\n");
							resp.setHeader("Content-Disposition", "attachment; filename=\"" + zipFileName
									+ FileType.ZIP.getExtensionWithDot() + "\"\r\n");
							try {
								out = resp.getOutputStream();
								zout = new ZipOutputStream(out);
								FileUtils.zipDirectory(outputDir, zout, zipFileName);
								resp.setStatus(HttpServletResponse.SC_OK);
							} catch (final IOException e) {
								respStr = "Unable to process web service request.Please try again." + e;
							} finally {
								if (zout != null) {
									zout.close();
								}
								if (out != null) {
									out.flush();
								}
								FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
							}
						}
					}
				}
			} catch (final XmlMappingException xmle) {
				respStr = "Error in mapping input XML in the desired format. Please send it in the specified format. Detailed exception is "
						+ xmle;
			} catch (final DCMAException dcmae) {
				respStr = "Error in processing request. Detailed exception is " + dcmae;
			} catch (final Exception e) {
				respStr = "Internal Server error.Please check logs for further details." + e;
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			}
		} else {
			respStr = "Improper input to server. Expected multipart request. Returning without processing the results.";
		}
		if (!workingDir.isEmpty()) {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
			} catch (final IOException ioe) {
			}
		}
	}

	@RequestMapping(value = "/extractFieldFromHocr", method = RequestMethod.POST)
	@ResponseBody
	public void extractFieldFromHocr(final HttpServletRequest req, final HttpServletResponse resp) {
		logger.info("Start processing web service for extractFieldFromHocr.");
		String respStr = WebServiceUtil.EMPTY_STRING;
		String workingDir = WebServiceUtil.EMPTY_STRING;

		if (req instanceof DefaultMultipartHttpServletRequest) {
			try {
				final String webServiceFolderPath = bsService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);

				InputStream instream = null;
				OutputStream outStream = null;

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
				}
				if (respStr.isEmpty()) {
					final MultiValueMap<String, MultipartFile> fileMap = multipartReq.getMultiFileMap();

					if (fileMap.size() == 1) {
						String hocrFileName = "";
						for (final String fileName : fileMap.keySet()) {
							// only single html file is expected as input
							if (fileName.toLowerCase().indexOf(FileType.HTML.getExtension()) > -1) {
								// only HTML file is expected
								hocrFileName = fileName;
								final MultipartFile f = multipartReq.getFile(fileName);
								instream = f.getInputStream();
								final File file = new File(workingDir + File.separator + fileName);
								outStream = new FileOutputStream(file);
								final byte buf[] = new byte[1024];
								int len;
								while ((len = instream.read(buf)) > 0) {
									outStream.write(buf, 0, len);
								}
								if (instream != null) {
									instream.close();
								}

								if (outStream != null) {
									outStream.close();
								}
								break;
							} else {
								respStr = "Improper input to server. Expected only one html file. Returning without processing the results.";
							}
						}
						if (respStr.isEmpty()) {
							String fileName = workingDir + File.separator + hocrFileName;

							// generate hocr file from html file.
							HocrPages hocrPages = new HocrPages();
							List<HocrPage> hocrPageList = hocrPages.getHocrPage();
							HocrPage hocrPage = new HocrPage();
							String pageID = "PG0";
							hocrPage.setPageID(pageID);
							hocrPageList.add(hocrPage);
							bsService.hocrGenerationAPI(workingDir, "PG0", fileName, hocrPage);

							List<KVExtraction> kvExtractionList = kvFieldService.createKeyValueFieldAPI(fieldValue, hocrPage);

							final KVExtractionFieldPatterns patterns = new KVExtractionFieldPatterns();

							final List<KVExtractionFieldPattern> pattern = patterns.getKVExtractionFieldPattern();
							for (final KVExtraction eachKVExtraction : kvExtractionList) {
								final KVExtractionFieldPattern kvField = new KVExtractionFieldPattern();
								kvField.setDistance(eachKVExtraction.getDistance());
								kvField.setFetchValue(eachKVExtraction.getFetchValue().name());
								kvField.setKeyPattern(eachKVExtraction.getKeyPattern());
								kvField.setLength(eachKVExtraction.getLength());
								kvField.setLocation(eachKVExtraction.getLocationType().name());
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
									resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
											"Internal Server error.Please check logs for further details." + e.getMessage());
								} catch (final IOException ioe) {

								}
							}
						}
					} else {
						respStr = "Improper input to server. Expected only one html file. Returning without processing the results.";
					}
				}
			} catch (final DCMAException dcmae) {
				respStr = "Error in processing request. Detailed exception is " + dcmae;
			} catch (final Exception e) {
				respStr = "Internal Server error.Please check logs for further details." + e;
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			}
		} else {
			respStr = "Improper input to server. Expected multipart request. Returing without processing the results.";
		}

		if (!workingDir.isEmpty()) {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
			} catch (final IOException ioe) {

			}
		}
	}

	@RequestMapping(value = "/extractFixedForm", method = RequestMethod.POST)
	@ResponseBody
	public void extractFixedForm(final HttpServletRequest req, final HttpServletResponse resp) throws Exception {
		logger.info("Start processing web service for extract fixed form");
		String respStr = WebServiceUtil.EMPTY_STRING;
		String workingDir = WebServiceUtil.EMPTY_STRING;
		com.ephesoft.dcma.batch.schema.Documents documents = null;
		if (req instanceof DefaultMultipartHttpServletRequest) {
			final String webServiceFolderPath = bsService.getWebServicesFolderPath();
			workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
			final String outputDir = WebServiceUtil.createWebServiceOutputDir(workingDir);

			InputStream instream = null;
			OutputStream outStream = null;

			final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) req;

			final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();
			String xmlFileName = WebServiceUtil.EMPTY_STRING;

			if (fileMap.size() != 3) {
				respStr = "Invalid number of files. We are supposed only 3 files.";
			}

			if (respStr.isEmpty()) {
				for (final String fileName : fileMap.keySet()) {
					if (fileName.endsWith(FileType.XML.getExtensionWithDot())) {
						xmlFileName = fileName;
					}

					final MultipartFile multiPartFile = multiPartRequest.getFile(fileName);
					instream = multiPartFile.getInputStream();
					final File file = new File(workingDir + File.separator + fileName);
					outStream = new FileOutputStream(file);
					final byte[] buf = new byte[WebServiceUtil.bufferSize];
					int len;
					while ((len = instream.read(buf)) > 0) {
						outStream.write(buf, 0, len);
					}
					if (instream != null) {
						instream.close();
					}

					if (outStream != null) {
						outStream.close();
					}
				}

				if (xmlFileName.isEmpty()) {
					respStr = "XML file is not found. Returning without processing the results.";
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
							}

							if (respStr.isEmpty()) {
								String colorSwitch = WebServiceUtil.EMPTY_STRING;
								String projectFile = WebServiceUtil.EMPTY_STRING;
								if (paramList == null || paramList.size() <= 0) {
									respStr = "Improper input to server. Returning without processing the results.";
								} else {
									for (final Param param : paramList) {
										if (param.getName().equalsIgnoreCase("colorSwitch")) {
											colorSwitch = param.getValue();
											logger.info("Color Switch for recostar is :" + colorSwitch);
											continue;
										}
										if (param.getName().equalsIgnoreCase("projectFile")) {
											projectFile = param.getValue();
											logger.info("Project file for recostar is :" + projectFile);
											continue;
										}
									}
								}

								String[] fileNames = null;
								final File file = new File(workingDir);
								respStr = WebServiceUtil.validateExtractFixedFormAPI(workingDir, projectFile, colorSwitch);

								if (respStr.isEmpty()) {
									if (colorSwitch.equalsIgnoreCase(WebServiceUtil.ON_STRING)) {
										logger.info("Picking up the png file for processing.");
										fileNames = file.list(new CustomFileFilter(false, FileType.PNG.getExtensionWithDot()));
									} else {
										logger.info("Picking up the tif file for processing.");
										fileNames = file.list(new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(),
												FileType.TIFF.getExtensionWithDot()));
									}
									logger.info("Number of file is:" + fileNames.length);

									if (fileNames != null && fileNames.length > 0) {
										for (final String fileName : fileNames) {
											logger.info("File processing for recostar is :" + fileName);
											documents = recostarExtractionService.extractDocLevelFieldsForRspFile(projectFile,
													workingDir, colorSwitch, fileName, workingDir);
										}
									} else {
										if (colorSwitch.equalsIgnoreCase(WebServiceUtil.ON_STRING)) {
											respStr = "Image file of png type is not found for processing";
										} else {
											respStr = "Image file of tif type is not found for processing";
										}
									}

									if (respStr.isEmpty() && documents != null) {
										File outputxmlFile = new File(outputDir + File.separator + "OutputXML.xml");
										FileOutputStream stream = new FileOutputStream(outputxmlFile);
										StreamResult result = new StreamResult(stream);
										batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().marshal(documents, result);

										ServletOutputStream out = null;
										ZipOutputStream zout = null;
										final String zipFileName = WebServiceUtil.serverOutputFolderName;
										resp.setContentType("application/x-zip\r\n");
										resp.setHeader("Content-Disposition", "attachment; filename=\"" + zipFileName
												+ FileType.ZIP.getExtensionWithDot() + "\"\r\n");
										try {
											out = resp.getOutputStream();
											zout = new ZipOutputStream(out);
											FileUtils.zipDirectory(outputDir, zout, zipFileName);
											resp.setStatus(HttpServletResponse.SC_OK);
										} catch (final IOException e) {
											respStr = "Error in creating output zip file.Please try again." + e;
										} finally {
											if (zout != null) {
												zout.close();
											}
											if (out != null) {
												out.flush();
											}
										}
									}
								}
							}
						}
					} catch (final DCMAException e) {
						respStr = "Error occuring while creating OCR file using recostar. Please try later. " + e;
					} catch (final Exception e) {
						respStr = "Improper input to server. Returning without processing the results." + e;
					}
				}
			}
			if (!workingDir.isEmpty()) {
				FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
			}
		}
		if (!respStr.isEmpty()) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
			} catch (final IOException ioe) {

			}
		}
	}

	@RequestMapping(value = "/restartAllBatchInstance", method = RequestMethod.GET)
	@ResponseBody
	public String restartAllBatchInstance(final HttpServletResponse resp, final HttpServletRequest req) {
		String isSuccess = WebServiceUtil.EMPTY_STRING;
		String userName = req.getUserPrincipal().getName(); // ToDo fetch from authentication header.
		Set<String> userRoles = userConnectivityService.getUserGroups(userName);
		List<BatchInstanceStatus> batchStatusList = new ArrayList<BatchInstanceStatus>();
		batchStatusList.add(BatchInstanceStatus.READY_FOR_REVIEW);
		batchStatusList.add(BatchInstanceStatus.READY_FOR_VALIDATION);
		List<BatchInstance> batchInstanceList = biService.getBatchInstanceByStatusList(batchStatusList);
		final boolean isZipSwitchOn = bsService.isZipSwitchOn();
		if (batchInstanceList.size() > 0) {
			for (BatchInstance batchInstance : batchInstanceList) {
				Set<String> batchInstanceRoles = biService.getRolesForBatchInstance(batchInstance);
				String batchInstanceIdentifier = batchInstance.getIdentifier();
				logger.info("Restarting batch instance : " + batchInstanceIdentifier);
				if (batchInstanceRoles.removeAll(userRoles)) {
					String activityName = workflowService.getActiveModule(batchInstance);
					if (activityName != null) {
						int indexOf = activityName.indexOf('.');
						indexOf = indexOf == -1 ? activityName.length() : indexOf;
						String moduleName = activityName.substring(0, indexOf);
						try {
							String batchClassIdentifier = biService.getBatchClassIdentifier(batchInstanceIdentifier);
							isSuccess = processRestartingBatchInternal(batchInstanceIdentifier, moduleName, isSuccess.toString(),
									batchInstance, batchClassIdentifier, isZipSwitchOn, activityName);
						} catch (Exception e) {
							if (isSuccess.isEmpty()) {
								isSuccess += "Error in restarting following batch instance identifiers: ";
							} else {
								isSuccess += ", ";
							}
							isSuccess += batchInstanceIdentifier;
							logger.error("Error while restarting batch instance: " + batchInstanceIdentifier);
						}
					} else {
						if (isSuccess.isEmpty()) {
							isSuccess += "Error in restarting batch instance identifiers are : ";
						} else {
							isSuccess += ", ";
						}
						isSuccess += batchInstanceIdentifier;
					}
				} else {
					if (isSuccess.isEmpty()) {
						isSuccess += "Error in restarting following batch instance identifiers:";
					} else {
						isSuccess += ", ";
					}
					isSuccess += "User is not authorized for id:" + batchInstanceIdentifier;
					logger.error("Error while restarting batch instance: " + batchInstanceIdentifier);
				}
			}

		} else {
			isSuccess = "No results found.";
		}
		return isSuccess;
	}

	@RequestMapping(value = "/extractFuzzyDB", method = RequestMethod.POST)
	@ResponseBody
	public void extractFuzzyDB(final HttpServletRequest req, final HttpServletResponse resp) {
		logger.info("Start processing web service for extract fuzzy DB for given HOCR file");
		String respStr = "";
		String workingDir = "";
		Documents documents = null;
		if (req instanceof DefaultMultipartHttpServletRequest) {
			try {
				final String webServiceFolderPath = bsService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
				final String outputDir = WebServiceUtil.createWebServiceOutputDir(workingDir);

				InputStream instream = null;
				OutputStream outStream = null;

				final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) req;

				final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();
				String htmlFile = WebServiceUtil.EMPTY_STRING;
				if (fileMap.size() == 1) {
					for (final String fileName : fileMap.keySet()) {
						if (fileName.endsWith(FileType.HTML.getExtensionWithDot())) {
							htmlFile = fileName;
						} else {
							respStr = "Invalid file. Please passed the valid html file";
							break;
						}
						final MultipartFile multiPartFile = multiPartRequest.getFile(fileName);
						instream = multiPartFile.getInputStream();
						final File file = new File(workingDir + File.separator + fileName);
						outStream = new FileOutputStream(file);
						final byte[] buf = new byte[WebServiceUtil.bufferSize];
						int len;
						while ((len = instream.read(buf)) > 0) {
							outStream.write(buf, 0, len);
						}
						if (instream != null) {
							instream.close();
						}

						if (outStream != null) {
							outStream.close();
						}
					}
				} else {
					respStr = "Invalid number of files. We are supposed only one file";
				}

				String batchClassIdentifier = WebServiceUtil.EMPTY_STRING;
				String documentType = WebServiceUtil.EMPTY_STRING;
				String hocrFileName = WebServiceUtil.EMPTY_STRING;
				for (final Enumeration<String> params = multiPartRequest.getParameterNames(); params.hasMoreElements();) {
					final String paramName = params.nextElement();
					if (paramName.equalsIgnoreCase("documentType")) {
						documentType = multiPartRequest.getParameter(paramName);
						logger.info("Value for documentType parameter is " + documentType);
						continue;
					}
					if (paramName.equalsIgnoreCase("batchClassIdentifier")) {
						batchClassIdentifier = multiPartRequest.getParameter(paramName);
						logger.info("Value for batchClassIdentifier parameter is " + batchClassIdentifier);
						continue;
					}
					if (paramName.equalsIgnoreCase("hocrFile")) {
						hocrFileName = multiPartRequest.getParameter(paramName);
						logger.info("Value for hocrFile parameter is " + hocrFileName);
						continue;
					}
				}

				if (!hocrFileName.equalsIgnoreCase(htmlFile)) {
					respStr = "Please passed the valid hocr File";
				}

				String results = WebServiceUtil
						.validateExtractFuzzyDBAPI(workingDir, hocrFileName, batchClassIdentifier, documentType);

				BatchClass batchClass = bcService.getBatchClassByIdentifier(batchClassIdentifier);
				if (batchClass == null) {
					respStr = "Please enter valid batch class identifier";
				} else {
					BatchPlugin fuzzyDBPlugin = batchClassPPService.getPluginProperties(batchClassIdentifier, "FUZZYDB");
					if (fuzzyDBPlugin == null) {
						respStr = "Fuzzy DB plugin is not configured for batch class : " + batchClassIdentifier
								+ " . Please select proper batch class";
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
						respStr = "Incomplete properties of the Fuzzy DB plugin for the specified batch class id.";
					}

				}

				List<com.ephesoft.dcma.da.domain.FieldType> allFdTypes = fieldTypeService.getFdTypeByDocTypeNameForBatchClass(
						documentType, batchClassIdentifier);

				if (allFdTypes == null) {
					respStr = "Please enter valid document type";
				}

				if (!results.isEmpty()) {
					respStr = results;
				} else {
					try {
						HocrPages hocrPages = new HocrPages();
						List<HocrPage> hocrPageList = hocrPages.getHocrPage();
						HocrPage hocrPage = new HocrPage();
						String pageID = "PG0";
						hocrPage.setPageID(pageID);
						hocrPageList.add(hocrPage);
						bsService.hocrGenerationAPI(workingDir, pageID, workingDir + File.separator + hocrFileName, hocrPage);
						documents = fuzzyDBSearchService.extractDataBaseFields(batchClassIdentifier, documentType, hocrPages);
					} catch (final DCMAException e) {
						respStr = "Exception while extracting field using fuzzy db" + e;
					}
				}

				if (documents != null) {
					File outputxmlFile = new File(outputDir + File.separator + "OutputXML.xml");
					FileOutputStream stream = new FileOutputStream(outputxmlFile);
					StreamResult result = new StreamResult(stream);
					batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().marshal(documents, result);
					ServletOutputStream out = null;
					ZipOutputStream zout = null;
					final String zipFileName = WebServiceUtil.serverOutputFolderName;
					resp.setContentType("application/x-zip\r\n");
					resp.setHeader("Content-Disposition", "attachment; filename=\"" + zipFileName + FileType.ZIP.getExtensionWithDot()
							+ "\"\r\n");
					try {
						out = resp.getOutputStream();
						zout = new ZipOutputStream(out);
						FileUtils.zipDirectory(outputDir, zout, zipFileName);
						resp.setStatus(HttpServletResponse.SC_OK);
					} catch (final IOException e) {
						resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
								"Error in creating output zip file.Please try again." + e.getMessage());
					} finally {
						if (zout != null) {
							zout.close();
						}
						if (out != null) {
							out.flush();
						}
						FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
					}
				}
			} catch (final XmlMappingException xmle) {
				respStr = "Error in mapping input XML in the desired format. Please send it in the specified format. Detailed exception is "
						+ xmle;
			} catch (final DCMAException dcmae) {
				respStr = "Error in processing request. Detailed exception is " + dcmae;
			} catch (final Exception e) {
				respStr = "Internal Server error.Please check logs for further details." + e;
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			}
		} else {
			respStr = "Improper input to server. Expected multipart request. Returning without processing the results.";
		}
		if (!respStr.isEmpty()) {
			try {
				FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
			} catch (final IOException ioe) {

			}
		}
	}

	@RequestMapping(value = "/convertTiffToPdf", method = RequestMethod.POST)
	@ResponseBody
	public void convertTiffToPdf(final HttpServletRequest req, final HttpServletResponse resp) {
		logger.info("Start processing web service for extract fuzzy DB for given HOCR file");
		String respStr = "";
		String workingDir = "";
		if (req instanceof DefaultMultipartHttpServletRequest) {
			try {
				final String webServiceFolderPath = bsService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
				final String outputDir = WebServiceUtil.createWebServiceOutputDir(workingDir);

				InputStream instream = null;
				OutputStream outStream = null;

				final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) req;

				final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();
				if (!fileMap.keySet().isEmpty()) {
					for (final String fileName : fileMap.keySet()) {
						if (fileName.endsWith(FileType.TIF.getExtensionWithDot())
								|| fileName.endsWith(FileType.TIFF.getExtensionWithDot())) {
						} else {
							respStr = "Invalid file. Please passed the valid tif/tiff file";
							break;
						}
						final MultipartFile multiPartFile = multiPartRequest.getFile(fileName);
						instream = multiPartFile.getInputStream();
						final File file = new File(workingDir + File.separator + fileName);
						outStream = new FileOutputStream(file);
						final byte[] buf = new byte[WebServiceUtil.bufferSize];
						int len;
						while ((len = instream.read(buf)) > 0) {
							outStream.write(buf, 0, len);
						}
						if (instream != null) {
							instream.close();
						}

						if (outStream != null) {
							outStream.close();
						}
					}
				} else {
					respStr = "Please passed the input files for processing";
				}

				if (respStr.isEmpty()) {
					String inputParams = WebServiceUtil.EMPTY_STRING;
					String outputParams = WebServiceUtil.EMPTY_STRING;
					String pdfGeneratorEngine = WebServiceUtil.EMPTY_STRING;
					for (final Enumeration<String> params = multiPartRequest.getParameterNames(); params.hasMoreElements();) {
						final String paramName = params.nextElement();
						if (paramName.equalsIgnoreCase("inputParams")) {
							inputParams = multiPartRequest.getParameter(paramName);
							logger.info("Value for batchClassIdentifier parameter is " + inputParams);
							continue;
						}
						if (paramName.equalsIgnoreCase("outputParams")) {
							outputParams = multiPartRequest.getParameter(paramName);
							logger.info("Value for hocrFile parameter is " + outputParams);
							continue;
						}
						if (paramName.equalsIgnoreCase("pdfGeneratorEngine")) {
							pdfGeneratorEngine = multiPartRequest.getParameter(paramName);
							logger.info("Value for hocrFile parameter is " + pdfGeneratorEngine);
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
						final String zipFileName = WebServiceUtil.serverOutputFolderName;
						resp.setContentType("application/x-zip\r\n");
						resp.setHeader("Content-Disposition", "attachment; filename=\"" + zipFileName
								+ FileType.ZIP.getExtensionWithDot() + "\"\r\n");
						try {
							out = resp.getOutputStream();
							zout = new ZipOutputStream(out);
							FileUtils.zipDirectory(outputDir, zout, zipFileName);
							resp.setStatus(HttpServletResponse.SC_OK);
						} catch (final IOException e) {
							resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
									"Error in creating output zip file.Please try again." + e.getMessage());
						} finally {
							if (zout != null) {
								zout.close();
							}
							if (out != null) {
								out.flush();
							}
							FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
						}
					}
				}
			} catch (final XmlMappingException xmle) {
				respStr = "Error in mapping input XML in the desired format. Please send it in the specified format. Detailed exception is "
						+ xmle;
			} catch (final DCMAException dcmae) {
				respStr = "Error in processing request. Detailed exception is " + dcmae;
			} catch (final Exception e) {
				respStr = "Internal Server error.Please check logs for further details." + e;
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			}
		} else {
			respStr = "Improper input to server. Expected multipart request. Returning without processing the results.";
		}
		if (!respStr.isEmpty()) {
			try {
				FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, respStr);
			} catch (final IOException ioe) {

			}
		}
	}
}
