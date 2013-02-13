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

import org.apache.commons.io.IOUtils;
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
 * @see com.ephesoft.dcma.ibm.service.IBMCMService
 */
@Component
public class IBMCMExporter implements ICommonConstants {

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
	 * @throws JAXBException root exception class for all JAXB exceptions
	 * @throws DCMAApplicationException if any exception occurs.
	 */
	public void exportFiles(final String batchInstanceID) throws JAXBException, DCMAApplicationException {
		LOGGER.info("IBM Content Management plugin.");
		LOGGER.info("Initializing properties...");
		String ibmCMSwitch = pluginPropertiesService.getPropertyValue(batchInstanceID, IBMCMConstant.IBM_CM_PLUGIN.getId(),
				IBMCMProperties.IBM_CM_SWITCH);
		if (IBMCMConstant.ON_STRING.equalsIgnoreCase(ibmCMSwitch)) {
			updateBatch(batchInstanceID);
			String exportFolder = pluginPropertiesService.getPropertyValue(batchInstanceID, IBMCMConstant.IBM_CM_PLUGIN.getId(),
					IBMCMProperties.IBM_CM_EXPORT_FOLDER);
			int totalDocSize = getTotalDocumentSize(batchInstanceID);
			if (null == exportFolder || "".equalsIgnoreCase(exportFolder)) {
				validateExportFolder();
			}
			LOGGER.info("Properties Initialized Successfully");
			boolean isZipSwitchOn = batchSchemaService.isZipSwitchOn();
			LOGGER.info("Zipped Batch XML switch is:" + isZipSwitchOn);
			String exportToFolder = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceID);
			String baseDocsFolder = exportToFolder + File.separator + batchInstanceID;
			boolean isExportFolderCreated = folderCreation(exportFolder);
			String batchXmlName = batchInstanceID + ICommonConstants.UNDERSCORE_BATCH_XML;
			String sourceXMLPath = baseDocsFolder + File.separator + batchInstanceID + ICommonConstants.UNDERSCORE_BATCH_XML;
			Batch batch = batchSchemaService.getBatch(batchInstanceID);
			String targetXmlPath = null;
			String batchName = "";
			Map<String, String> subPoenaLoanMap = getSubPoenaLoanNumber(batch.getDocuments().getDocument());
			if (subPoenaLoanMap.size() == 2) {
				batchName = validateSubPoenaLoanMap(subPoenaLoanMap);
			}
			if ((batch != null && batch.getBatchName() != null && !batch.getBatchName().isEmpty())
					|| (batchName != null && !batchName.isEmpty())) {
				targetXmlPath = getTargetFilePath(batchInstanceID, exportFolder, batch, IBMCMConstant.XML_EXTENSION.getId(), batchName);
			}
			InputStream xslStream = null;
			try {
				xslStream = xslResource.getInputStream();
			} catch (IOException e) {
				throw new DCMAApplicationException("Could not find xsl file in the classpath resource", e);
			}
			InputStream inputStream = null;
			tracingLogs(isExportFolderCreated, sourceXMLPath, targetXmlPath, xslStream);
			if (targetXmlPath == null) {
				validateTargetXmlPath(exportFolder);
			}
			if (xslStream == null) {
				validateXslStream();
			}
			if (!isExportFolderCreated) {
				throw new DCMAApplicationException("Unable to create directory in " + exportFolder + ". Please verify the permission");
			}
			try {
				TransformerFactory tFactory = TransformerFactory.newInstance();
				Transformer transformer = null;
				try {
					transformer = tFactory.newTransformer(new StreamSource(xslStream));
				} finally {
					IOUtils.closeQuietly(xslStream);
				}
				if (transformer != null) {
					String prefixSubpoenaValue = "";
					prefixSubpoenaValue = getPrefixSubpoenaValue(prefixSubpoenaValue, batch);
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
					parsingParamterToXML(batchInstanceID, transformer, cmodAppGroupLocal, cmodAppLocal);
					transformer.setParameter(IBMCMConstant.TOTAL_DOCUMENT_SIZE.getId(), totalDocSize);
					String datFile = getTargetFilePath(batchInstanceID, exportFolder, batch, IBMCMConstant.DAT_EXTENSION.getId(),
							batchName);
					transformer.setParameter(IBMCMConstant.DAT_FILE_NAME.getId(), new File(datFile).getName());
					transformer.setParameter(IBMCMConstant.SUPLLYING_SYSTEM.getId(), this.getSupplyingSystem());
					inputStream = transformIntoXML(isZipSwitchOn, batchXmlName, sourceXMLPath, targetXmlPath, inputStream, transformer);
					updateDocumentOffsetValue(batchInstanceID, targetXmlPath);
				} else {
					LOGGER.error("Transformer is null due to Invalid xsl file.");
				}
			} catch (FileNotFoundException e) {
				throw new DCMAApplicationException("Error in creating output xml file :" + targetXmlPath + " " + e.getMessage(), e);
			} catch (TransformerException e1) {
				throw new DCMAApplicationException("Could not transform ibmCMTransform.xsl file : " + e1.getMessage(), e1);
			} catch (IOException ioe) {
				throw new DCMAApplicationException("Could not transform ibmCMTransform.xsl file : " + ioe.getMessage(), ioe);
			} finally {
				IOUtils.closeQuietly(inputStream);
			}
			generateDATFile(batchInstanceID, exportFolder, batch);
			generateCTLFile(batchInstanceID, exportFolder, batch);
		} else {
			LOGGER.info("IBM Content Management Exporter is switched off or null");
		}
	}

	private void tracingLogs(boolean isExportFolderCreated, String sourceXMLPath, String targetXmlPath, InputStream xslStream) {
		LOGGER.info("Transforming XML " + sourceXMLPath + " to " + targetXmlPath);
		LOGGER.info("targetXMLPath value is :" + targetXmlPath);
		LOGGER.info("xslStream value is :" + xslStream);
		LOGGER.info("isExportFolderCreated value is :" + isExportFolderCreated);
	}

	private InputStream transformIntoXML(boolean isZipSwitchOn, String batchXmlName, String sourceXMLPath, String targetXmlPath,
			InputStream inputStream, Transformer transformer) throws FileNotFoundException, IOException, TransformerException {
		InputStream localInputStream = inputStream;
		if (isZipSwitchOn) {
			if (FileUtils.isZipFileExists(sourceXMLPath)) {
				localInputStream = FileUtils.getInputStreamFromZip(sourceXMLPath, batchXmlName);
				transformer.transform(new StreamSource(localInputStream), new StreamResult(new FileOutputStream(targetXmlPath)));
			} else {
				transformer
						.transform(new StreamSource(new File(sourceXMLPath)), new StreamResult(new FileOutputStream(targetXmlPath)));
			}
		} else {
			File srcXML = new File(sourceXMLPath);
			if (srcXML.exists()) {
				transformer.transform(new StreamSource(srcXML), new StreamResult(new FileOutputStream(targetXmlPath)));
			} else {
				localInputStream = FileUtils.getInputStreamFromZip(sourceXMLPath, batchXmlName);
				transformer.transform(new StreamSource(localInputStream), new StreamResult(new FileOutputStream(targetXmlPath)));
			}
		}
		return localInputStream;
	}

	private String getPrefixSubpoenaValue(String prefixSubpoenaValue, Batch batch) {
		String localPrefixSubpoenaValue = prefixSubpoenaValue;
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
						localPrefixSubpoenaValue = docField.getValue();
						break outer;
					}
				}
			}
		}
		return localPrefixSubpoenaValue;
	}

	private void validateXslStream() throws DCMAApplicationException {
		LOGGER.error("xslStream is null. Unable to generate xsl stream from " + xslResource.toString());
		throw new DCMAApplicationException("xslStream is null. Unable to generate xsl stream from " + xslResource.toString());
	}

	private void validateTargetXmlPath(String exportFolder) throws DCMAApplicationException {
		LOGGER.error("targetXMLPath is null. Unable to create directory in " + exportFolder + ". Please verify permission");
		throw new DCMAApplicationException("targetXMLPath is null. Unable to create directory in " + exportFolder
				+ ". Please verify the permission");
	}

	private String validateSubPoenaLoanMap(Map<String, String> subPoenaLoanMap) {
		String batchName;
		batchName = subPoenaLoanMap.get(IBMCMConstant.SUBPOENA.getId()) + IBMCMConstant.UNDERSCORE.getId()
				+ subPoenaLoanMap.get(IBMCMConstant.LOAN_NUMBER.getId());
		return batchName;
	}

	private void validateExportFolder() throws DCMAApplicationException {
		LOGGER.error("Export folder is null or empty");
		throw new DCMAApplicationException(
				"IBM Content Management Export Folder value is null/empty from the database. Invalid initializing of properties.");
	}

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

	private String getTargetFilePath(final String batchInstanceID, final String exportFolder, final Batch batch,
			final String fileExtension, String batchName) {
		LOGGER.info("Generating file path to be exported");
		String targetFilePath = null;
		String filePath = "";
		String newBatchName = batchName;
		if (newBatchName == null || newBatchName.isEmpty()) {
			newBatchName = batch.getBatchName();
		}
		if (newBatchName.length() > IBMCMConstant.CONSTANT_3) {
			String subpoenaPrefix = newBatchName.substring(0, IBMCMConstant.CONSTANT_3);
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
			targetFilePath = filePath + File.separator + newBatchName + IBMCMConstant.UNDERSCORE.getId() + batchInstanceID
					+ fileExtension.toLowerCase(Locale.getDefault());
			LOGGER.info("File path to be exported is :" + targetFilePath);
		}
		return targetFilePath;
	}

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

	private void generateDATFile(final String batchInstanceID, final String exportFolder, final Batch batch)
			throws DCMAApplicationException {
		LOGGER.info("Generating DAT file");
		List<String> documentFileList = getDocumentFileList(batchInstanceID);
		String batchName = "";
		Map<String, String> subPoenaLoanMap = getSubPoenaLoanNumber(batch.getDocuments().getDocument());
		if (subPoenaLoanMap.size() == 2) {
			batchName = validateSubPoenaLoanMap(subPoenaLoanMap);
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

	private void generateCTLFile(final String batchInstanceID, final String exportFolder, final Batch batch)
			throws DCMAApplicationException {
		LOGGER.info("Generating CTL file");
		String batchName = "";
		Map<String, String> subPoenaLoanMap = getSubPoenaLoanNumber(batch.getDocuments().getDocument());
		if (subPoenaLoanMap.size() == 2) {
			batchName = validateSubPoenaLoanMap(subPoenaLoanMap);
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

	private void updateDocumentOffsetValue(final String batchInstanceID, final String filePath) throws DCMAApplicationException {
		try {
			org.w3c.dom.Document document = XMLUtil.createDocumentFrom(new File(filePath));
			NodeList documentList = document.getElementsByTagName(IBMCMConstant.DOCUMENT);
			for (int documentIndex = 0; documentIndex < documentList.getLength(); documentIndex++) {
				Element documentTag = (Element) documentList.item(documentIndex);
				Element docTag = (Element) documentTag.getElementsByTagName(IBMCMConstant.DOC_ID).item(0);
				Element offset = (Element) documentTag.getElementsByTagName(IBMCMConstant.OFFSET).item(0);
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
