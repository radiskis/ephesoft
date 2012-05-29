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

package com.ephesoft.dcma.ibm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.EphesoftContext;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.ibm.constant.IBMCMConstant;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.XMLUtil;

/**
 * This Class is used to export the XML , DAT and CTL file in IBM content management accepted format.
 * 
 * @author Ephesoft
 * @version 1.0
 */
@Component
public class IBMCMExporter implements ICommonConstants {

	/**
	 * Variable for offset.
	 */
	private static final String OFFSET = "Offset";

	/**
	 * Variable for docID.
	 */
	private static final String DOC_ID = "DocId";

	/**
	 * Variable for document.
	 */
	private static final String DOCUMENT = "Document";

	/**
	 * String for ON switch.
	 */
	private static final String ON_STRING = "ON";

	/**
	 * Instance of Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(IBMCMExporter.class);

	/**
	 * Instance of BatchSchemaService.
	 **/
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Instance of BatchInstanceService.
	 **/
	@Autowired
	private BatchInstanceService batchInstanceService;

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

	/**
	 * @param xslResource the xslResource to set
	 */
	public void setXslResource(final ClassPathResource xslResource) {
		this.xslResource = xslResource;
	}

	/**
	 * Variable for cmodAppGroup.
	 */
	private String cmodAppGroup;

	/**
	 * Variable for cmodApp.
	 */
	private String cmodApp;

	/**
	 * Variable for userName.
	 */
	private String userName;

	/**
	 * Variable for email.
	 */
	private String email;

	/**
	 * Varibale for supplyingSystem.
	 */
	private String supplyingSystem;

	/**
	 * Getter for supplyingSystem.
	 * 
	 * @return {@link String}
	 */

	public final String getSupplyingSystem() {
		return supplyingSystem;
	}

	/**
	 * Setter for supplyingSystem.
	 * 
	 * @param supplyingSystem
	 */
	public final void setSupplyingSystem(final String supplyingSystem) {
		this.supplyingSystem = supplyingSystem;
	}

	/**
	 * @param cmodAppGroup the cmodAppGroup to set
	 */
	public void setCmodAppGroup(final String cmodAppGroup) {
		this.cmodAppGroup = cmodAppGroup;
	}

	/**
	 * @param cmodApp the cmodApp to set
	 */
	public void setCmodApp(final String cmodApp) {
		this.cmodApp = cmodApp;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(final String userName) {
		this.userName = userName;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(final String email) {
		this.email = email;
	}

	/**
	 * This API update the batch.xml.
	 * 
	 * @param batchInstanceID {@link String}
	 */
	private void updateBatch(final String batchInstanceID) {
		LOGGER.info("Update document size of each document in batch.xml");
		Batch batch = batchSchemaService.getBatch(batchInstanceID);
		if (batch != null && batch.getDocuments() != null) {
			List<Document> documents = batch.getDocuments().getDocument();
			for (Document document : documents) {
				if (document != null) {
					// fetching file and update document size
					LOGGER.info("Updated document size for document :" + document.getIdentifier());
					updateDocumentSize(batchInstanceID, batch, document);
				}
			}
			batchSchemaService.updateBatch(batch);
			LOGGER.info("Updated document size in batch.xml");
		}
	}

	/**
	 * This API retrieving the file location, calculating the file size and update it to batch.xml.
	 * 
	 * @param batchInstanceID {@link String}
	 * @param batch {@link Batch}
	 * @param document {@link Document}
	 */
	private void updateDocumentSize(final String batchInstanceID, final Batch batch, final Document document) {
		LOGGER.info("Retrieving document size for each document");
		String multipagePdf = document.getMultiPagePdfFile();
		if (multipagePdf != null && !multipagePdf.isEmpty()) {
			StringBuffer multipagePdfPath = new StringBuffer(batch.getBatchLocalPath());
			multipagePdfPath.append(File.separator);
			multipagePdfPath.append(batchInstanceID);
			multipagePdfPath.append(File.separator);
			multipagePdfPath.append(multipagePdf);
			File file = new File(multipagePdfPath.toString());
			LOGGER.info("Retrieving file is :" + multipagePdf);
			if (file.exists()) {
				long length = file.length();
				document.setSize(String.valueOf(length));
				LOGGER.info("Updated file size for document" + multipagePdf + "is :" + length);
			} else {
				LOGGER.error("File does not exits... " + multipagePdf);
			}
		}
	}

	/**
	 * This API calculating the document size of mutipage pdf and returns the sum of all multipage pdf document.
	 * 
	 * @param batchInstanceID {@link String}
	 * @return totalDocSize
	 */
	private int getTotalDocumentSize(final String batchInstanceID) {
		LOGGER.info("Retrieving total size of document");
		int totalDocSize = 0;
		Batch batch = batchSchemaService.getBatch(batchInstanceID);
		if (batch != null && batch.getDocuments() != null) {
			List<Document> documentList = batch.getDocuments().getDocument();
			for (Document document : documentList) {
				if (document != null && document.getSize() != null) {
					try {
						totalDocSize += Integer.valueOf(document.getSize());
						LOGGER.info("Updated total size for document in bytes :" + totalDocSize);
					} catch (NumberFormatException nfe) {
						LOGGER.error("Exception in converting string to integer :" + document.getSize());
					}
				}
			}
		}
		return totalDocSize;
	}

	/**
	 * This method transforms the batch.xml to another XML acceptable by IBM Content Management.
	 * 
	 * @param batchInstanceID {@link String}
	 * @throws JAXBException
	 * @throws DCMAApplicationException
	 */
	public void exportFiles(final String batchInstanceID) throws JAXBException, DCMAApplicationException {
		LOGGER.info("IBM Content Management plugin.");
		LOGGER.info("Initializing properties...");

		String ibmCMSwitch = pluginPropertiesService.getPropertyValue(batchInstanceID, IBMCMConstant.IBM_CM_PLUGIN.getId(),
				IBMCMProperties.IBM_CM_SWITCH);

		if (ON_STRING.equalsIgnoreCase(ibmCMSwitch)) {
			// update document size in batch.xml
			updateBatch(batchInstanceID);

			String exportFolder = pluginPropertiesService.getPropertyValue(batchInstanceID, IBMCMConstant.IBM_CM_PLUGIN.getId(),
					IBMCMProperties.IBM_CM_EXPORT_FOLDER);

			// return total document size
			int totalDocSize = getTotalDocumentSize(batchInstanceID);

			if (null == exportFolder || "".equalsIgnoreCase(exportFolder)) {
				LOGGER.error("Export folder is null or empty");
				throw new DCMAApplicationException(
						"IBM Content Management Export Folder value is null/empty from the database. Invalid initializing of properties.");
			}

			LOGGER.info("Properties Initialized Successfully");
			
			boolean isZipSwitchOn = batchSchemaService.isZipSwitchOn();
			LOGGER.info("Zipped Batch XML switch is:" + isZipSwitchOn);

			// check whether export folder to be created or not
			String exportToFolder = batchSchemaService.getLocalFolderLocation();
			String baseDocsFolder = exportToFolder + File.separator + batchInstanceID;
			boolean isExportFolderCreated = folderCreation(exportFolder);

			String batchXmlName = batchInstanceID + ICommonConstants.UNDERSCORE_BATCH_XML;
			String sourceXMLPath = baseDocsFolder + File.separator + batchInstanceID + ICommonConstants.UNDERSCORE_BATCH_XML;
			Batch batch = batchSchemaService.getBatch(batchInstanceID);
			String targetXmlPath = null;
			String batchName = "";
			Map<String, String> subPoenaLoanMap = getSubPoenaLoanNumber(batch.getDocuments().getDocument());
			if (subPoenaLoanMap.size() == 2) {
				batchName = subPoenaLoanMap.get(IBMCMConstant.SUBPOENA.getId()) + IBMCMConstant.UNDERSCORE.getId()
						+ subPoenaLoanMap.get(IBMCMConstant.LOAN_NUMBER.getId());
			}
			if ((batch != null && batch.getBatchName() != null && !batch.getBatchName().isEmpty())
					|| (batchName != null && !batchName.isEmpty())) {
				targetXmlPath = getTargetFilePath(batchInstanceID, exportFolder, batch, IBMCMConstant.XML_EXTENSION.getId(), batchName);
			}

			InputStream xslStream = null;
			try {
				LOGGER.info("xslResource = " + xslResource.toString());
				xslStream = xslResource.getInputStream();
			} catch (IOException e) {
				LOGGER.error("Could not find xsl file in the classpath resource", e.getMessage(), e);
				throw new DCMAApplicationException("Could not find xsl file in the classpath resource", e);
			}
			InputStream in = null;
			LOGGER.debug("Transforming XML " + sourceXMLPath + " to " + targetXmlPath);
			if (xslStream != null && targetXmlPath != null && isExportFolderCreated) {
				try {
					TransformerFactory tFactory = TransformerFactory.newInstance();
					Transformer transformer = null;

					try {
						transformer = tFactory.newTransformer(new StreamSource(xslStream));
					} finally {
						if (xslStream != null) {
							try {
								xslStream.close();
							} catch (IOException e) {
								LOGGER.info("Error closing input stream for :" + xslResource.toString());
							}
						}
					}
					if (transformer != null) {
						String prefixSubpoenaValue = "";
						List<Document> listDocuments = batch.getDocuments().getDocument();
						outer: for (Document doc : listDocuments) {
							DocumentLevelFields documentLevelFields = doc.getDocumentLevelFields();
							if (null == documentLevelFields) {
								LOGGER.info("DocumentLevelFields is null.");
							} else {
								List<DocField> documentLevelField = documentLevelFields.getDocumentLevelField();
								for (DocField docField : documentLevelField) {
									String name = docField.getName();
									if (null != name && name.equalsIgnoreCase("DRCi_Instance")) {
										prefixSubpoenaValue = docField.getValue();
										break outer;
									}
								}
							}
						}

						String cmodAppGroupLocal = "";
						String cmodAppLocal = "";

						if (prefixSubpoenaValue.equalsIgnoreCase("FRE")) {
							cmodAppLocal = "FreddieMac_Loans_PDF";
							cmodAppGroupLocal = "FreddieMac_Loans";
						} else if (prefixSubpoenaValue.equalsIgnoreCase("FNM")) {
							cmodAppLocal = "FannieMae_Loans_PDF";
							cmodAppGroupLocal = "FannieMae_Loans";
						} else {
							cmodAppGroupLocal = this.cmodAppGroup;
							cmodAppLocal = this.cmodApp;
						}
						
						// Parsing parameter to XML
						parsingParamterToXML(batchInstanceID, transformer, cmodAppGroupLocal, cmodAppLocal);
						// Setting parameter for total document size for updating its value in XML
						transformer.setParameter(IBMCMConstant.TOTAL_DOCUMENT_SIZE.getId(), totalDocSize);
						String datFile = getTargetFilePath(batchInstanceID, exportFolder, batch, IBMCMConstant.DAT_EXTENSION.getId(),
								batchName);
						// Setting parameter for DAT file name for updating its value in XML.
						transformer.setParameter(IBMCMConstant.DAT_FILE_NAME.getId(), new File(datFile).getName());
						transformer.setParameter(IBMCMConstant.SUPLLYING_SYSTEM.getId(), this.getSupplyingSystem());
						
						if (isZipSwitchOn) {
							if (FileUtils.isZipFileExists(sourceXMLPath)) {
								in = FileUtils.getInputStreamFromZip(sourceXMLPath, batchXmlName);
								transformer.transform(new StreamSource(in), new StreamResult(new FileOutputStream(targetXmlPath)));
							} else {
								transformer.transform(new StreamSource(new File(sourceXMLPath)), new StreamResult(new FileOutputStream(
										targetXmlPath)));
							}
						} else {
							File srcXML = new File(sourceXMLPath);
							if (srcXML.exists()) {
								transformer.transform(new StreamSource(srcXML), new StreamResult(new FileOutputStream(
										targetXmlPath)));
							} else {
								in = FileUtils.getInputStreamFromZip(sourceXMLPath, batchXmlName);
								transformer.transform(new StreamSource(in), new StreamResult(new FileOutputStream(targetXmlPath)));
							}
						}									
						
						updateDocumentOffsetValue(batchInstanceID, targetXmlPath);
					} else {
						LOGGER.error("Transformer is null due to Invalid xsl file.");
					}
				} catch (FileNotFoundException e) {
					LOGGER.error("ibmCMTransform.xsl is not found" + e.getMessage(), e);
					throw new DCMAApplicationException("Could not find nsiTransform.xsl file : " + e.getMessage(), e);
				} catch (TransformerException e1) {
					LOGGER.error("Problem occurred in transforming " + sourceXMLPath + " to " + targetXmlPath + e1.getMessage(), e1);
					throw new DCMAApplicationException("Could not transform ibmCMTransform.xsl file : " + e1.getMessage(), e1);
				} catch (IOException ioe) {
					LOGGER.error("Problem occurred in transforming " + sourceXMLPath + " to " + targetXmlPath + ioe.getMessage(), ioe);
					throw new DCMAApplicationException("Could not transform ibmCMTransform.xsl file : " + ioe.getMessage(), ioe);
				} finally {
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
							LOGGER.info("Error closing input stream for :" + sourceXMLPath);
						}
					}
				}
			} else {
				LOGGER.error("Invalid input stream for :" + xslResource.toString());
				throw new DCMAApplicationException("Could not find ibmCMTransform.xsl file");
			}

			// generating DAT file
			generateDATFile(batchInstanceID, exportFolder, batch);
			// generating CTL file
			generateCTLFile(batchInstanceID, exportFolder, batch);
		} else {
			LOGGER.info("IBM Content Management Exporter is switched off or null");
		}
	}

	/**
	 * This API is used to parsing parameter to ibmCMTransform.xsl file.
	 * 
	 * @param batchInstanceID {@link String}
	 * @param transformer {@link Transformer}
	 */
	private void parsingParamterToXML(final String batchInstanceID, final Transformer transformer, final String cmodAppGroupLocal,
			final String cmodAppLocal) {
		LOGGER.info("Parsing infomation to transformer");
		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceID);
		if (batchInstance != null && batchInstance.getCreationDate() != null) {
			String datepattern = IBMCMConstant.DATE_FORMAT.getId();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datepattern, Locale.getDefault());
			String batchCreationDate = simpleDateFormat.format(batchInstance.getCreationDate());
			// Setting parameter for batch creation dates for updating its value in xml
			LOGGER.info("Date parsing to transfomer is :" + batchCreationDate);
			transformer.setParameter(IBMCMConstant.BATCH_CREATION_DATE.getId(), batchCreationDate);
			String timepattern = IBMCMConstant.TIME_FORMAT.getId();
			SimpleDateFormat simpleTimeFormat = new SimpleDateFormat(timepattern, Locale.getDefault());
			String batchCreationTime = simpleTimeFormat.format(batchInstance.getCreationDate());
			// Setting parameter batch creation time for updating its value in xml
			LOGGER.info("Time parsing to transfomer is :" + batchCreationTime);
			transformer.setParameter(IBMCMConstant.BATCH_CREATION_TIME.getId(), batchCreationTime);
			// Setting parameter cmod app group for updating its value in XML.
			transformer.setParameter(IBMCMConstant.CMOD_APP_GROUP.getId(), cmodAppGroupLocal);

			// Setting parameter cmod app for updating its value in XML.
			transformer.setParameter(IBMCMConstant.CMOD_APP.getId(), cmodAppLocal);

			// Setting parameter user name for updating its value in XML.
			transformer.setParameter(IBMCMConstant.USER_NAME.getId(), this.userName);
			// Setting parameter email for updating its value in XML.
			transformer.setParameter(IBMCMConstant.EMAIL.getId(), this.email);
			// Setting parameter batch creation station id for updating its value in XML.
			String batchCreationStationID = batchInstance.getServerIP();
			if (batchCreationStationID == null) {
				transformer.setParameter(IBMCMConstant.BATCH_CREATION_STATION_ID.getId(), "");
			} else {
				transformer.setParameter(IBMCMConstant.BATCH_CREATION_STATION_ID.getId(), batchCreationStationID);
			}
			// Setting parameter station id for updating its value in XML.
			transformer.setParameter(IBMCMConstant.STATION_ID.getId(), EphesoftContext.getHostServerRegistry().getIpAddress());
		}
	}

	/**
	 * This API created the export folder if it not exists else it will created the export folder.
	 * 
	 * @param exportFolder {@link String}
	 * @return
	 */
	private boolean folderCreation(final String exportFolder) {
		LOGGER.info("Checking the export folder is exist or not");
		File exportFolderFile = new File(exportFolder);
		boolean isExportFolderCreated = false;
		if (!exportFolderFile.exists()) {
			isExportFolderCreated = exportFolderFile.mkdirs();
		} else {
			isExportFolderCreated = true;
		}
		return isExportFolderCreated;
	}

	/**
	 * This API provides the target file path to be created.
	 * 
	 * @param batchInstanceID {@link String}
	 * @param exportFolder {@link String}
	 * @param batch {@link Batch}
	 * @param fileExtension {@link String}
	 * @return targetXmlPath {@link String}
	 */
	private String getTargetFilePath(final String batchInstanceID, final String exportFolder, final Batch batch,
			final String fileExtension, String batchName) {
		LOGGER.info("Generating file path to be exported");
		String targetFilePath = null;
		String filePath = "";
		if (batchName == null || batchName.isEmpty()) {
			batchName = batch.getBatchName();
		}
		if (batchName.length() > 3) {
			String subpoenaPrefix = batchName.substring(0, 3);
			filePath = exportFolder + File.separator + subpoenaPrefix + File.separator + batchInstanceID;
		} else {
			filePath = exportFolder + File.separator + batchInstanceID;
		}
		boolean isFolderCreated = false;
		File file = new File(filePath);
		if (!file.exists()) {
			isFolderCreated = file.mkdirs();
		} else {
			isFolderCreated = true;
		}
		if (isFolderCreated) {
			targetFilePath = filePath + File.separator + batchName + IBMCMConstant.UNDERSCORE.getId() + batchInstanceID
					+ fileExtension.toLowerCase(Locale.getDefault());
			LOGGER.info("File path to be exported is :" + targetFilePath);
		}
		return targetFilePath;
	}

	/**
	 * This API fetching the document list of mutipage pdf.
	 * 
	 * @param batchInstanceID {@link String}
	 * @return totalDocSize
	 */
	private List<String> getDocumentFileList(final String batchInstanceID) throws DCMAApplicationException {
		LOGGER.info("Retrieving file list of multipage document");
		List<String> multiPageDocument = new ArrayList<String>();
		Batch batch = batchSchemaService.getBatch(batchInstanceID);
		if (batch != null && batch.getDocuments() != null) {
			StringBuffer multipagePdfPath = new StringBuffer(batch.getBatchLocalPath());
			multipagePdfPath.append(File.separator);
			multipagePdfPath.append(batchInstanceID);
			multipagePdfPath.append(File.separator);

			List<Document> documentList = batch.getDocuments().getDocument();
			for (Document document : documentList) {
				String multiPagePdfFile = document.getMultiPagePdfFile();
				if (multiPagePdfFile == null) {
					throw new DCMAApplicationException("Multi Page PDF file name is null for " + document.getIdentifier());
				}
				multiPageDocument.add(multipagePdfPath.toString() + File.separator + multiPagePdfFile);
			}
		}
		return multiPageDocument;
	}

	/**
	 * This API for generating DAT file.
	 * 
	 * @param batchInstanceID {@link String}
	 * @param exportFolder {@link String}
	 * @param batch {@link Batch}
	 */
	private void generateDATFile(final String batchInstanceID, final String exportFolder, final Batch batch)
			throws DCMAApplicationException {
		LOGGER.info("Generating DAT file");
		List<String> documentFileList = getDocumentFileList(batchInstanceID);
		String batchName = "";
		Map<String, String> subPoenaLoanMap = getSubPoenaLoanNumber(batch.getDocuments().getDocument());
		if (subPoenaLoanMap.size() == 2) {
			batchName = subPoenaLoanMap.get(IBMCMConstant.SUBPOENA.getId()) + IBMCMConstant.UNDERSCORE.getId()
					+ subPoenaLoanMap.get(IBMCMConstant.LOAN_NUMBER.getId());
		}
		// Output DAT file path
		String outputFilePath = getTargetFilePath(batchInstanceID, exportFolder, batch, IBMCMConstant.DAT_EXTENSION.getId(), batchName);

		if (null == outputFilePath || outputFilePath.isEmpty()) {
			String msg = "Unable to create batch instance folder inside exportFolder. outputFilePath is null or empty.";
			LOGGER.error(msg);
			throw new DCMAApplicationException(msg);
		}
		boolean isDeleted = true;
		File file = new File(outputFilePath);
		if (file.exists()) {
			isDeleted = file.delete();
			if (isDeleted) {
				LOGGER.info(outputFilePath + " is deleted successfully.");
			} else {
				LOGGER.error(outputFilePath + " is unable delete.");
				isDeleted = file.delete();
				if (isDeleted) {
					LOGGER.info(outputFilePath + " is deleted successfully.");
				} else {
					LOGGER.error(outputFilePath + " is unable delete.");
				}
			}
		} else {
			LOGGER.info(outputFilePath);
		}

		if (isDeleted) {
			LOGGER.info("Output DAT file path :" + outputFilePath);
			try {
				FileUtils.mergeFilesIntoSingleFile(documentFileList, outputFilePath);
			} catch (Exception e) {
				LOGGER.error("Error in merging file" + e.getMessage(), e);
				throw new DCMAApplicationException("Error in merging file" + e.getMessage(), e);
			}
			LOGGER.info("Successfully generated DAT file");
		}
	}

	/**
	 * This API for generating CTL file.
	 * 
	 * @param batchInstanceID {@link String}
	 * @param exportFolder {@link String}
	 * @param batch {@link Batch}
	 */
	private void generateCTLFile(final String batchInstanceID, final String exportFolder, final Batch batch)
			throws DCMAApplicationException {
		LOGGER.info("Generating CTL file");
		String batchName = "";
		Map<String, String> subPoenaLoanMap = getSubPoenaLoanNumber(batch.getDocuments().getDocument());
		if (subPoenaLoanMap.size() == 2) {
			batchName = subPoenaLoanMap.get(IBMCMConstant.SUBPOENA.getId()) + IBMCMConstant.UNDERSCORE.getId()
					+ subPoenaLoanMap.get(IBMCMConstant.LOAN_NUMBER.getId());
		}
		// Output CTL file path
		String outputCTLFilePath = getTargetFilePath(batchInstanceID, exportFolder, batch, IBMCMConstant.CTL_EXTENSION.getId(),
				batchName);
		File file = new File(outputCTLFilePath);
		boolean isFileCreated = false;
		try {
			isFileCreated = file.createNewFile();
		} catch (IOException e) {
			LOGGER.error("Exception in creating file" + e.getMessage(), e);
		}
		if (isFileCreated) {
			LOGGER.info("Successfully generated CTL file");
		}
	}

	/**
	 * This API is used to update the offset value in XML file generated for IBM Content Management.
	 * 
	 * @param batchInstanceID {@link String}
	 * @param filePath {@link String}
	 * @throws DCMAApplicationException if any exception or error occur
	 */
	private void updateDocumentOffsetValue(final String batchInstanceID, final String filePath) throws DCMAApplicationException {
		try {
			org.w3c.dom.Document document = XMLUtil.createDocumentFrom(new File(filePath));
			NodeList documentList = document.getElementsByTagName(DOCUMENT);
			for (int documentIndex = 0; documentIndex < documentList.getLength(); documentIndex++) {
				Element documentTag = (Element) documentList.item(documentIndex);
				Element docTag = (Element) documentTag.getElementsByTagName(DOC_ID).item(0);
				Element offset = (Element) documentTag.getElementsByTagName(OFFSET).item(0);
				String docIdentifier = docTag.getTextContent();
				if (docIdentifier != null & !docIdentifier.isEmpty()) {
					offset.setTextContent(String.valueOf(getOffsetValue(batchInstanceID, docIdentifier)));
				}
			}
			Source source = new DOMSource(document);
			File batchXmlFile = new File(filePath);
			OutputStream outputStream = new FileOutputStream(batchXmlFile);
			Result result = new StreamResult(outputStream);
			Transformer xformer = null;
			try {
				xformer = TransformerFactory.newInstance().newTransformer();
			} catch (TransformerConfigurationException e) {
				String msg = "Exception in transforming configuration file";
				LOGGER.error(msg + e.getMessage(), e);
				throw new DCMAApplicationException(msg + e.getMessage(), e);
			} catch (TransformerFactoryConfigurationError e) {
				String msg = "Exception in transforming factory file";
				LOGGER.error(msg + e.getMessage(), e);
				throw new DCMAApplicationException(msg + e.getMessage(), e);
			}
			try {
				xformer.transform(source, result);
			} catch (TransformerException e) {
				String msg = "Exception in transforming file";
				LOGGER.error(msg + e.getMessage(), e);
				throw new DCMAApplicationException(msg + e.getMessage(), e);
			}
		} catch (Exception e) {
			String msg = "Exception in updating offset into file";
			LOGGER.error(msg + e.getMessage(), e);
			throw new DCMAApplicationException(msg + e.getMessage(), e);
		}
	}

	/**
	 * This API is used to get the offset value for the specific document identifier.
	 * 
	 * @param batchInstanceID {@link String}
	 * @param docID {@link String}
	 * @return offset
	 * @throws DCMAApplicationException if any exception or error occur
	 */
	private int getOffsetValue(final String batchInstanceID, final String docID) throws DCMAApplicationException {
		int offset = 0;
		Batch batch = batchSchemaService.getBatch(batchInstanceID);
		List<Document> documents = batch.getDocuments().getDocument();
		try {
			for (Document document : documents) {
				if (document.getIdentifier().equals(docID)) {
					break;
				} else {
					offset = offset + Integer.valueOf(document.getSize());
				}
			}
		} catch (NumberFormatException e) {
			String msg = "Error in converting offset value into integer";
			LOGGER.error(msg + e.getMessage(), e);
			throw new DCMAApplicationException(msg + e.getMessage(), e);
		}
		return offset;
	}
	
	/**
	 * API for getting subpoena loan number on the basis of document list.
	 * @param documentList
	 * @return
	 */
	private Map<String, String> getSubPoenaLoanNumber(List<Document> documentList) {
		Map<String, String> resultMap = new HashMap<String, String>();
		for (Document document : documentList) {
			if (resultMap.size() == 2) {
				break;
			}
			DocumentLevelFields docFields = document.getDocumentLevelFields();
			if (docFields == null) {
				continue;
			}
			List<DocField> docFieldList = docFields.getDocumentLevelField();
			for (DocField docField : docFieldList) {
				if ((docField.getName().equals(IBMCMConstant.SUBPOENA.getId()))
						|| (docField.getName().equals(IBMCMConstant.LOAN_NUMBER.getId()))) {
					String value = docField.getValue();
					if (null != value && !value.isEmpty()) {
						resultMap.put(docField.getName(), value);
					}
				}
			}
		}
		return resultMap;
	}
}
