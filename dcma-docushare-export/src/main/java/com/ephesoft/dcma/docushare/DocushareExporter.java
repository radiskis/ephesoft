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

package com.ephesoft.dcma.docushare;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.util.FileUtils;

/**
 * This method transforms the batch.xml to another xml acceptable by docushare and zip it along with multi page tif and multi page
 * pdf to docushare export location.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.docushare.service.DocushareExportServiceImpl
 */
@Component
public class DocushareExporter implements ICommonConstants {

	/**
	 * An instance of Logger for logging.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DocushareExporter.class);

	/**
	 * An instance of {@link BatchSchemaService}.
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
	 * An instance of {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * Final Export Folder for Docushare.
	 */
	private transient String finalExportFolder;

	/**
	 * Final xml name for Docushare.
	 */
	private transient String finalXmlName;

	/**
	 * Final zip file name for Docushare.
	 */
	private transient String zipFileName;

	/**
	 * Docushare switch.
	 */
	private transient String docushareSwitch;

	/**
	 * ClassPath Resource to xsl file.
	 */
	private ClassPathResource xslResource;

	/**
	 * @return the {@link BatchSchemaService}
	 */
	public BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * @param batchSchemaService {@link BatchSchemaService}
	 */
	public void setBatchSchemaService(BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * @return the {@link PluginPropertiesService}
	 */
	public PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	/**
	 * @param pluginPropertiesService {@link PluginPropertiesService}
	 */
	public void setPluginPropertiesService(PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	/**
	 * @return {@link String}
	 */
	public String getFinalExportFolder() {
		return finalExportFolder;
	}

	/**
	 * @param finalExportFolder {@link String}
	 */
	public void setFinalExportFolder(String finalExportFolder) {
		this.finalExportFolder = finalExportFolder;
	}

	/**
	 * @return the finalXmlName
	 */
	public String getFinalXmlName() {
		return finalXmlName;
	}

	/**
	 * @param finalXmlName {@link String}
	 */
	public void setFinalXmlName(String finalXmlName) {
		this.finalXmlName = finalXmlName;
	}

	/**
	 * @return {@link String}
	 */
	public String getZipFileName() {
		return zipFileName;
	}

	/**
	 * @param zipFileName {@link String}
	 */
	public void setZipFileName(String zipFileName) {
		this.zipFileName = zipFileName;
	}

	/**
	 * @return {@link String}
	 */
	public String getDocushareSwitch() {
		return docushareSwitch;
	}

	/**
	 * @param docushareSwitch {@link String}
	 */
	public void setDocushareSwitch(String docushareSwitch) {
		this.docushareSwitch = docushareSwitch;
	}

	/**
	 * @return {@link ClassPathResource}
	 */
	public ClassPathResource getXslResource() {
		return xslResource;
	}

	/**
	 * @param xslResource {@link ClassPathResource}
	 */
	public void setXslResource(ClassPathResource xslResource) {
		this.xslResource = xslResource;
	}

	/**
	 * This method transforms the batch.xml to another xml acceptable by docushare and zip it along with multi page tif and multi page
	 * pdf to docushare export location.
	 * 
	 * @param batchInstanceID {@link String}
	 * @throws JAXBException {@link JAXBException} root exception class for all JAXB exceptions 
	 * @throws DCMAApplicationException {@link DCMAApplicationException} if any file is not found.
	 */
	public void exportFiles(String batchInstanceID) throws JAXBException, DCMAApplicationException {

		if (docushareSwitch != null && !docushareSwitch.equalsIgnoreCase("ON")) {
			LOGGER.info("Docushare Exporter is switched off");
			return;
		}
		LOGGER.info("Initializing properties...");
		String temp_var_to = " to ";
		String exportToFolder = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceID);
		LOGGER.info("Properties Initialized Successfully");

		boolean isZipSwitchOn = batchSchemaService.isZipSwitchOn();
		LOGGER.info("Zipped Batch XML switch is:" + isZipSwitchOn);

		String baseDocsFolder = exportToFolder + File.separator + batchInstanceID;
		String sourceXMLPath = baseDocsFolder + File.separator + batchInstanceID + ICommonConstants.UNDERSCORE_BATCH_XML;
		String targetXmlPath = baseDocsFolder + File.separator + batchInstanceID + finalXmlName;
		InputStream xslStream = null;
		InputStream xmlStream = null;
		try {
			xslStream = xslResource.getInputStream();
		} catch (IOException e2) {
			LOGGER.error("Could not find xsl file in the classpath resource", e2);
			throw new DCMAApplicationException("Could not find xsl file in the classpath resource", e2);
		}
		LOGGER.debug("Transforming XML " + sourceXMLPath + temp_var_to + targetXmlPath);
		try {
			if (isZipSwitchOn) {
				if (FileUtils.isZipFileExists(sourceXMLPath)) {
					xmlStream = FileUtils
							.getInputStreamFromZip(sourceXMLPath, batchInstanceID + ICommonConstants.UNDERSCORE_BATCH_XML);
					com.ephesoft.dcma.util.XMLUtil.transformXMLWithStream(xmlStream, targetXmlPath, xslStream);
				} else {
					com.ephesoft.dcma.util.XMLUtil.transformXML(sourceXMLPath, targetXmlPath, xslStream);
				}
			} else {
				if (new File(sourceXMLPath).exists()) {
					com.ephesoft.dcma.util.XMLUtil.transformXML(sourceXMLPath, targetXmlPath, xslStream);
				} else {
					xmlStream = FileUtils
							.getInputStreamFromZip(sourceXMLPath, batchInstanceID + ICommonConstants.UNDERSCORE_BATCH_XML);
					com.ephesoft.dcma.util.XMLUtil.transformXMLWithStream(xmlStream, targetXmlPath, xslStream);
				}
			}
		} catch (FileNotFoundException e1) {
			LOGGER.error("Could not find docushareTransform.xsl file : " + e1, e1);
			throw new DCMAApplicationException("Could not find docushareTransform.xsl file : " + e1, e1);
		} catch (TransformerException e1) {
			LOGGER.error("Problem occured in transforming " + sourceXMLPath + temp_var_to + targetXmlPath + e1);
			throw new DCMAApplicationException("Could not find docushareTransform.xsl file : " + e1, e1);
		} catch (Exception e1) {
			LOGGER.error("Problem occured in transforming " + sourceXMLPath + temp_var_to + targetXmlPath + e1);
			throw new DCMAApplicationException("Problem in transforming : " + e1, e1);
		} finally {
			if (xslStream != null) {
				try {
					xslStream.close();
				} catch (IOException e) {
					LOGGER.error("Problem in closing input stream. " + e.getMessage(), e);
				}
			}
			if (xmlStream != null) {
				try {
					xmlStream.close();
				} catch (IOException e) {
					LOGGER.error("Problem in closing input stream. " + e.getMessage(), e);
				}
			}
		}

		String pathToDocShareExportFolder = batchSchemaService.getBaseFolderLocation() + File.separator + finalExportFolder;
		File file = new File(pathToDocShareExportFolder);
		if (!file.exists()) {
			file.mkdir();
			LOGGER.info(pathToDocShareExportFolder + " folder created");
		}

		String finalZipFileName = pathToDocShareExportFolder + File.separator + batchInstanceID + zipFileName;
		LOGGER.debug("Exporting zip file " + finalZipFileName);
		String temp_var_zip = "Problem in zipping directory ";
		try {
			FileUtils.zipDirectory(baseDocsFolder, finalZipFileName, true);
		} catch (IllegalArgumentException e) {
			LOGGER.error(temp_var_zip + baseDocsFolder + temp_var_to + finalZipFileName, e);
			throw new DCMAApplicationException(temp_var_zip + baseDocsFolder + temp_var_to + finalZipFileName, e);
		} catch (IOException e) {
			LOGGER.error(temp_var_zip + baseDocsFolder + temp_var_to + finalZipFileName, e);
			throw new DCMAApplicationException(temp_var_zip + baseDocsFolder + temp_var_to + finalZipFileName, e);
		}
	}
}
