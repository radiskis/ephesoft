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

package com.ephesoft.dcma.ibm.cm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.JAXBException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.ibm.cm.constant.IBMCMConstant;
import com.ephesoft.dcma.util.FileUtils;

/**
 * This Class is used to export the XML , DAT and CTL file in IBM content management accepted format.
 * 
 * @author Ephesoft
 * @version 1.0
 */
@Component
public class IBMCMExporter implements ICommonConstants {

	/**
	 * Character size for SHA1 encoder.
	 */
	private static final int SHA1_ENCODER_CHAR_SIZE = 40;

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
			StringBuffer multipagePdfPath = new StringBuffer();
			multipagePdfPath = new StringBuffer(batch.getBatchLocalPath());
			multipagePdfPath.append(File.separator);
			multipagePdfPath.append(batchInstanceID);
			multipagePdfPath.append(File.separator);
			multipagePdfPath.append(multipagePdf);
			File file = new File(multipagePdfPath.toString());
			LOGGER.info("Retrieving file is :" + multipagePdfPath.toString());
			if (file.exists()) {
				document.setSize(String.valueOf(file.length()));
				LOGGER.info("Updated file size for document" + multipagePdfPath.toString() + "is :" + file.length());
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
	 * This method transforms the batch.xml to another xml acceptable by IM Load.
	 * 
	 * @param batchInstanceID {@link String}
	 * @throws JAXBException
	 * @throws DCMAApplicationException
	 */
	public void exportFiles(final String batchInstanceID) throws JAXBException, DCMAApplicationException {
		LOGGER.info("IBM Content Management plugin.");
		LOGGER.info("Initializing properties...");

		String ibmCMSwitch = pluginPropertiesService.getPropertyValue(batchInstanceID, IBMCMConstant.IBM_CM_PLUGIN
				.getId(), IBMCMProperties.IBM_CM_SWITCH);

		if (ON_STRING.equalsIgnoreCase(ibmCMSwitch)) {
			// update document size in batch.xml
			updateBatch(batchInstanceID);

			String exportFolder = pluginPropertiesService.getPropertyValue(batchInstanceID,
					IBMCMConstant.IBM_CM_PLUGIN.getId(), IBMCMProperties.IBM_CM_EXPORT_FOLDER);

			// return total document size
			int totalDocSize = getTotalDocumentSize(batchInstanceID);

			if (null == exportFolder || "".equalsIgnoreCase(exportFolder)) {
				LOGGER.error("Export folder is null or empty");
				throw new DCMAApplicationException(
						"IBM Content Management Export Folder value is null/empty from the database. Invalid initializing of properties.");
			}

			LOGGER.info("Properties Initialized Successfully");

			// check whether export folder to be created or not
			boolean isExportFolderCreated = isExportFolderAlreadyCreated(exportFolder);
			String exportToFolder = batchSchemaService.getLocalFolderLocation();
			String baseDocsFolder = exportToFolder + File.separator + batchInstanceID;
			String sourceXMLPath = baseDocsFolder + File.separator + batchInstanceID + IBMCMConstant.BATCH_XML.getId();
			Batch batch = batchSchemaService.getBatch(batchInstanceID);
			String targetXmlPath = null;
			if (batch != null && batch.getBatchName() != null && !batch.getBatchName().isEmpty()) {
				targetXmlPath = getTargetFilePath(batchInstanceID, exportFolder, batch, IBMCMConstant.XML_EXTENSION.getId());
			}

			InputStream xslStream = null;
			try {
				LOGGER.info("xslResource = " + xslResource.toString());
				xslStream = xslResource.getInputStream();
			} catch (IOException e) {
				LOGGER.error("Could not find xsl file in the classpath resource", e.getMessage(), e);
				throw new DCMAApplicationException("Could not find xsl file in the classpath resource", e);
			}

			LOGGER.debug("Transforming XML " + sourceXMLPath + " to " + targetXmlPath);
			if (xslStream != null && targetXmlPath != null && isExportFolderCreated) {
				try {
					TransformerFactory tFactory = TransformerFactory.newInstance();
					Transformer transformer = tFactory.newTransformer(new StreamSource(xslStream));
					if (transformer != null) {
						// Parsing parameter to xml
						parsingParamterToXML(batchInstanceID, transformer);
						// Setting parameter for total document size for updating its value in xml
						transformer.setParameter(IBMCMConstant.TOTAL_DOCUMENT_SIZE.getId(), totalDocSize);
						transformer.transform(new StreamSource(new File(sourceXMLPath)), new StreamResult(new FileOutputStream(
								targetXmlPath)));
					} else {
						LOGGER.error("Transformer is null due to Invalid xsl file.");
					}
				} catch (FileNotFoundException e) {
					LOGGER.error("ibmCMTransform.xsl is not found" + e.getMessage(), e);
					throw new DCMAApplicationException("Could not find nsiTransform.xsl file : " + e.getMessage(), e);
				} catch (TransformerException e1) {
					LOGGER.error("Problem occurred in transforming " + sourceXMLPath + " to " + targetXmlPath + e1.getMessage(), e1);
					throw new DCMAApplicationException("Could not transform ibmCMTransform.xsl file : " + e1.getMessage(), e1);
				}
			} else {
				LOGGER.error("Invalid input stream for :" + xslResource.toString());
			}

			// generating DAT file
			generateDATFile(batchInstanceID, exportFolder, batch);
			// generating CTL file
			generateCTLFile(batchInstanceID, exportFolder, batch);
		} else {
			LOGGER.error("IM Load Exporter is switched off");
		}
	}

	/**
	 * This API is used to parsing parameter to ibmCMTransform.xsl file.
	 * 
	 * @param batchInstanceID {@link String}
	 * @param transformer {@link Transformer}
	 */
	private void parsingParamterToXML(final String batchInstanceID, final Transformer transformer) {
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
			// Setting parameter for batch creation time for updating its value in xml
			LOGGER.info("Time parsing to transfomer is :" + batchCreationTime);
			transformer.setParameter(IBMCMConstant.BATCH_CREATION_TIME.getId(), batchCreationTime);
		}
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
			final String fileExtension) {
		LOGGER.info("Generating file path to be exported");
		String targetFilePath = null;
		String filePath = exportFolder + File.separator + batch.getBatchName().toLowerCase();
		File file = new File(filePath + fileExtension);
		if (!file.exists()) {
			targetFilePath = filePath + fileExtension;
		} else {
			targetFilePath = filePath + IBMCMConstant.UNDERSCORE.getId() + batchInstanceID + fileExtension;
		}
		LOGGER.info("File path to be exported is :" + targetFilePath);
		return targetFilePath;
	}

	/**
	 * This API fetching the document list of mutipage pdf.
	 * 
	 * @param batchInstanceID {@link String}
	 * @return totalDocSize
	 */
	private List<String> getDocumentFileList(final String batchInstanceID) {
		LOGGER.info("Retrieving total size of document");
		List<String> multiPageDocument = new ArrayList<String>();
		Batch batch = batchSchemaService.getBatch(batchInstanceID);
		if (batch != null && batch.getDocuments() != null) {
			List<Document> documentList = batch.getDocuments().getDocument();
			for (Document document : documentList) {
				multiPageDocument.add(batchSchemaService.getLocalFolderLocation() + File.separator + batchInstanceID + File.separator
						+ document.getMultiPagePdfFile());
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
	private void generateDATFile(final String batchInstanceID, final String exportFolder, final Batch batch) {
		LOGGER.info("Generating DAT file");
		List<String> documentFileList = getDocumentFileList(batchInstanceID);
		// Output DAT file path
		String outputFilePath = getTargetFilePath(batchInstanceID, exportFolder, batch, IBMCMConstant.DAT_EXTENSION.getId());
		LOGGER.info("Output DAT file path :" + outputFilePath);
		FileUtils.mergeFilesIntoSingleFile(documentFileList, outputFilePath);
		LOGGER.info("Successfully generated DAT file");
	}

	/**
	 * This API convert data into Hexadecimal format.
	 * 
	 * @param data
	 * @return
	 */
	private String convertToHex(final byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9)) {
					buf.append((char) ('0' + halfbyte));
				} else {
					buf.append((char) ('a' + (halfbyte - 10)));
				}
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	/**
	 * This API gives the text into SHA1 encoding form.
	 * 
	 * @param text {@link String}
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws DCMAApplicationException
	 */
	public String sha1Encoder(final String text) throws NoSuchAlgorithmException, UnsupportedEncodingException,
			DCMAApplicationException {
		MessageDigest messageDigest;
		messageDigest = MessageDigest.getInstance("SHA-1");
		byte[] sha1hash = new byte[SHA1_ENCODER_CHAR_SIZE];
		messageDigest.update(text.getBytes("iso-8859-1"), 0, text.length());
		sha1hash = messageDigest.digest();
		return convertToHex(sha1hash);
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
		// Output CTL file path
		String outputCTLFilePath = getTargetFilePath(batchInstanceID, exportFolder, batch, IBMCMConstant.CTL_EXTENSION.getId());
		String fileNameWithoutExtension = new File(outputCTLFilePath).getName();
		LOGGER.info("Output CTL file path :" + outputCTLFilePath);
		String file1 = fileNameWithoutExtension.substring(0, fileNameWithoutExtension.indexOf('.'))
				+ IBMCMConstant.XML_EXTENSION.getId();
		String file2 = fileNameWithoutExtension.substring(0, fileNameWithoutExtension.indexOf('.'))
				+ IBMCMConstant.DAT_EXTENSION.getId();
		String sha1 = null;
		String sha2 = null;
		try {
			sha1 = sha1Encoder(file1);
			sha2 = sha1Encoder(file2);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("No algorithum is found for encoding data " + e.getMessage(), e);
			throw new DCMAApplicationException("No algorithum is found for encoding data " + e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Unsupported encoding in SHA1 algorithum" + e.getMessage(), e);
			throw new DCMAApplicationException("Unsupported encoding in SHA1 algorithum" + e.getMessage(), e);
		}
		String line1 = sha1 + "  " + file1;
		String line2 = sha2 + "  " + file2;
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(outputCTLFilePath, true));
			out.write(line1);
			out.newLine();
			out.write(line2);
			if (out != null) {
				out.close();
			}
		} catch (FileNotFoundException e1) {
			LOGGER.error("File not found exception :" + e1.getMessage(), e1);
			throw new DCMAApplicationException("File not found exception :" + e1.getMessage(), e1);
		} catch (IOException e) {
			LOGGER.error("Unable to read data from the system :" + e.getMessage(), e);
			throw new DCMAApplicationException("Unable to read data from the system :" + e.getMessage(), e);
		}
		LOGGER.info("Successfully generated CTL file");
	}
}
