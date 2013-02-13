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

package com.ephesoft.dcma.nsi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.nsi.constant.NSIExportConstant;
import com.ephesoft.dcma.util.FileUtils;

/**
 * This class exports batch instance files.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.batch.service.BatchSchemaService
 * @see com.ephesoft.dcma.batch.service.PluginPropertiesService
 */
@Component
public class NsiExporter implements ICommonConstants {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(NsiExporter.class);

	/**
	 * batchSchemaService BatchSchemaService.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Instance of PluginPropertiesService.
	 */
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
	 * Instance of {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * @param batchSchemaService the batchSchemaService to set
	 */
	public void setBatchSchemaService(BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * To get Plugin Properties Service.
	 * @return the pluginPropertiesService
	 */
	public PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	/**
	 * To set Plugin Properties Service.
	 * @param pluginPropertiesService the pluginPropertiesService to set
	 */
	public void setPluginPropertiesService(PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	/**
	 * To get Xsl Resource.
	 * @return the xslResource
	 */
	public ClassPathResource getXslResource() {
		return xslResource;
	}

	/**
	 * To set Xsl Resource.
	 * @param xslResource the xslResource to set
	 */
	public void setXslResource(ClassPathResource xslResource) {
		this.xslResource = xslResource;
	}

	/**
	 * This method transforms the batch.xml to another xml acceptable by NSI and zip it along with multipage tif and multipage pdf to
	 * NSI export location.
	 * 
	 * @param batchInstanceID String
	 * @throws JAXBException if error occurs
	 * @throws DCMAApplicationException if xsl file not find 
	 */
	public void exportFiles(String batchInstanceID) throws JAXBException, DCMAApplicationException {
		LOGGER.info("NSI export plugin.");
		LOGGER.info("Initializing properties...");
		String isNsiSwitchON = pluginPropertiesService.getPropertyValue(batchInstanceID, NSIExportConstant.NSI_PLUGIN_NAME,
				NsiExporterProperties.NSI_SWITCH);
		if ("ON".equalsIgnoreCase(isNsiSwitchON)) {
			String exportFolder = pluginPropertiesService.getPropertyValue(batchInstanceID, NSIExportConstant.NSI_PLUGIN_NAME,
					NsiExporterProperties.NSI_EXPORT_FOLDER);
			String xmlTagStyle = pluginPropertiesService.getPropertyValue(batchInstanceID, NSIExportConstant.NSI_PLUGIN_NAME,
					NsiExporterProperties.NSI_XML_NAME);
			if (null == exportFolder || NSIExportConstant.EMPTY.equalsIgnoreCase(exportFolder)) {
				throw new DCMAApplicationException(
						"NSI Export Folder value is null/empty from the database. Invalid initializing of properties.");
			}
			if (null == xmlTagStyle || NSIExportConstant.EMPTY.equalsIgnoreCase(xmlTagStyle)) {
				throw new DCMAApplicationException(
						"Nsi Xml file naming convention is null/empty from the data base. Invalid initializing of properties.");
			}
			LOGGER.info("Properties Initialized Successfully");

			boolean isZipSwitchOn = batchSchemaService.isZipSwitchOn();
			LOGGER.info("Zipped Batch XML switch is:" + isZipSwitchOn);

			String exportToFolder = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceID);
			String baseDocsFolder = exportToFolder + File.separator + batchInstanceID;

			InputStream xslStream = null;
			try {
				LOGGER.info("xslResource = " + xslResource.toString());
				xslStream = xslResource.getInputStream();
			} catch (IOException e) {
				LOGGER.error("Could not find xsl file in the classpath resource", e.getMessage(), e);
				throw new DCMAApplicationException("Could not find xsl file in the classpath resource", e);
			}
			if (xslStream != null) {
				transformXmlAndExportFiles(batchInstanceID, exportFolder, xmlTagStyle, isZipSwitchOn, baseDocsFolder, xslStream);
			} else {
				LOGGER.info("Invalid input stream for :" + xslResource.toString());
			}
		} else {
			LOGGER.info("NSI Exporter is switched off");
		}
	}

	private void transformXmlAndExportFiles(String batchInstanceID, String exportFolder, String xmlTagStyle, boolean isZipSwitchOn,
			String baseDocsFolder, InputStream xslStream) throws TransformerFactoryConfigurationError, DCMAApplicationException {
		String batchXmlName = batchInstanceID + ICommonConstants.UNDERSCORE_BATCH_XML;
		String sourceXMLPath = baseDocsFolder + File.separator + batchInstanceID + ICommonConstants.UNDERSCORE_BATCH_XML;
		String targetXmlPath = exportFolder + File.separator + batchInstanceID + xmlTagStyle;
		LOGGER.debug("Transforming XML " + sourceXMLPath + " to " + targetXmlPath);
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = null;
			try {
				// NOTE, this needs to be fixed to use the InputStream xslStream object, not a hardcoded path to the file.
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
				DateTimeZone zone = DateTimeZone.forID(NSIExportConstant.TIME_ZONE_ID);
				DateTime dateTime = new DateTime(zone);
				String date = Integer.toString(dateTime.getYear()) + NSIExportConstant.HYPEN
						+ Integer.toString(dateTime.getMonthOfYear()) + NSIExportConstant.HYPEN
						+ Integer.toString(dateTime.getDayOfMonth());
				String time = Integer.toString(dateTime.getHourOfDay()) + NSIExportConstant.COLON
						+ Integer.toString(dateTime.getMinuteOfHour()) + NSIExportConstant.COLON
						+ Integer.toString(dateTime.getSecondOfMinute());
				transformer.setParameter(NSIExportConstant.DATE, date);
				transformer.setParameter(NSIExportConstant.HOURS, time);
				transformer.setParameter(NSIExportConstant.BASE_DOC_FOLDER_PATH, baseDocsFolder + File.separator);
				transformer.setParameter(NSIExportConstant.EXPORT_FOLDER_PATH, exportFolder + File.separator);
				File file = new File(exportFolder);
				boolean isFileCreated = false;
				if (!file.exists()) {
					isFileCreated = file.mkdir();
				} else {
					isFileCreated = true;
				}
				if (isFileCreated) {
					String imageFolderPath = exportFolder + File.separator + NSIExportConstant.IMAGE_FOLDER_NAME;
					File imageFolder = new File(imageFolderPath);
					boolean isImageFolderCreated = false;
					if (!imageFolder.exists()) {
						isImageFolderCreated = imageFolder.mkdir();
					} else {
						isImageFolderCreated = true;
					}
					if (isImageFolderCreated) {
						LOGGER.info(exportFolder + " folder created");
						Batch batch = batchSchemaService.getBatch(batchInstanceID);
						List<Document> documentList = batch.getDocuments().getDocument();

						transformXML(isZipSwitchOn, batchXmlName, sourceXMLPath, targetXmlPath, transformer);

						File baseDocFolder = new File(baseDocsFolder);
						for (Document document : documentList) {
							if (document != null && document.getMultiPageTiffFile() != null
									&& !document.getMultiPageTiffFile().isEmpty()) {
								String multipageTiffName = document.getMultiPageTiffFile();
								String filePath = baseDocFolder.getAbsolutePath() + File.separator + multipageTiffName;
								String exportFileName = multipageTiffName.replace(NSIExportConstant.TIF_WITH_DOT_EXTENSION,
										NSIExportConstant.DAT_WITH_DOT_EXTENSION);
								String exportFilePath = imageFolderPath + File.separator + exportFileName;
								File oldFile = new File(filePath);
								File newFile = new File(exportFilePath);
								try {
									FileUtils.copyFile(oldFile, newFile);
								} catch (Exception e) {
									LOGGER.error("Error creating in file: " + newFile + "is" + e.getMessage(), e);
								}
							}
						}
					}
				} else {
					LOGGER.error("Access is denied for creating: " + file.getName());
				}
			} else {
				LOGGER.error("Transformer is null due to Invalid xsl file.");
			}
		} catch (FileNotFoundException e1) {
			LOGGER.error("Could not find NSITransform.xsl file : " + e1.getMessage(), e1);
			throw new DCMAApplicationException("Could not find nsiTransform.xsl file : " + e1.getMessage(), e1);
		} catch (TransformerException e1) {
			LOGGER.error("Problem occurred in transforming " + sourceXMLPath + " to " + targetXmlPath + e1.getMessage(), e1);
			throw new DCMAApplicationException("Could not find nsiTransform.xsl file : ", e1);
		} catch (IOException ioe) {
			LOGGER.error("Problem occurred in transforming " + sourceXMLPath + " to " + targetXmlPath + ioe.getMessage(), ioe);
			throw new DCMAApplicationException("Could not transform ibmCMTransform.xsl file : " + ioe.getMessage(), ioe);
		}
	}

	private InputStream transformXML(boolean isZipSwitchOn, String batchXmlName, String sourceXMLPath, String targetXmlPath,
			Transformer transformer) throws FileNotFoundException, IOException, TransformerException {
		InputStream input = null;
		try {
			if (isZipSwitchOn) {
				if (FileUtils.isZipFileExists(sourceXMLPath)) {
					input = FileUtils.getInputStreamFromZip(sourceXMLPath, batchXmlName);
					transformer.transform(new StreamSource(input), new StreamResult(new FileOutputStream(targetXmlPath)));
				} else {
					transformer.transform(new StreamSource(new File(sourceXMLPath)), new StreamResult(new FileOutputStream(
							targetXmlPath)));
				}
			} else {
				File srcXML = new File(sourceXMLPath);
				if (srcXML.exists()) {
					transformer.transform(new StreamSource(new File(sourceXMLPath)), new StreamResult(new FileOutputStream(
							targetXmlPath)));
				} else {
					input = FileUtils.getInputStreamFromZip(sourceXMLPath, batchXmlName);
					transformer.transform(new StreamSource(input), new StreamResult(new FileOutputStream(targetXmlPath)));
				}
			}
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					LOGGER.info("Error closing input stream for :" + sourceXMLPath);
				}
			}
		}
		return input;
	}
}
