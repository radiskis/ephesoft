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

package com.ephesoft.dcma.filebound;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.filebound.constants.FileBoundConstants;

/**
 * This class is responsible for uploading all the output files to the repository folder. This will reads the batch.xml file. It finds
 * the names of multi page tif and pdf files from the batch.xml. Then it upload these files to the repository main root folder. At a
 * time it will upload only pdf or tif files.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.fileboound.service.FileBoundServiceImpl
 */
@Component
public class FileBoundExporter implements ICommonConstants {

	/**
	 * An instance of Logger used for proper logging.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(FileBoundExporter.class);

	/**
	 * A List of type String for storing dlf names to skip.
	 */
	public static List<String> skipDlfNames = null;

	/**
	 * Instance of BatchInstanceService.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * Instance of BatchSchemaService.
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
	 * List of all filebound commands Separated by ";".
	 */
	private transient String mandatorycmds;

	/**
	 * @return the batchSchemaService
	 */
	public BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * @param batchSchemaService the batchSchemaService to set
	 */
	public void setBatchSchemaService(final BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * @return the batchInstanceService
	 */
	public BatchInstanceService getBatchInstanceService() {
		return batchInstanceService;
	}

	/**
	 * @param batchInstanceService the batchInstanceService to set
	 */
	public void setBatchInstanceService(final BatchInstanceService batchInstanceService) {
		this.batchInstanceService = batchInstanceService;
	}

	/**
	 * @return the mandatorycmds
	 */
	public String getMandatorycmds() {
		return mandatorycmds;
	}

	/**
	 * @param mandatorycmds the mandatorycmds to set
	 */
	public void setMandatorycmds(final String mandatorycmds) {
		this.mandatorycmds = mandatorycmds;
	}

	/**
	 * This method populates the list of commands from ";" separated String of commands.
	 * 
	 * @return List<String>
	 */
	public List<String> populateCommandsList() {
		final List<String> cmdList = new ArrayList<String>();
		final StringTokenizer token = new StringTokenizer(mandatorycmds, ";");
		while (token.hasMoreTokens()) {
			final String eachCmd = token.nextToken();
			cmdList.add(eachCmd);
		}
		return cmdList;
	}

	/**
	 * This method reads the batch.xml file. It finds the names of multi page tif and pdf files from the batch.xml. Then it upload these
	 * files to the repository main root folder. At a time it will upload only pdf or tif files.
	 * 
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @throws JAXBException root exception class for all JAXB exceptions
	 * @throws DCMAApplicationException If not able to upload files to repository server. If invalid input parameters.
	 */
	public void exportFiles(final String batchInstanceIdentifier) throws JAXBException, DCMAApplicationException {
		LOGGER.info("FileBound export plugin.");
		LOGGER.info("Initializing properties...");
		final String isFileBoundON = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FileBoundConstants.FILEBOUND_EXPORT_PLUGIN,
				FileBoundProperties.FILEBOUND_SWITCH);
		if (FileBoundConstants.ON_STRING.equalsIgnoreCase(isFileBoundON)) {
			final String connectionURL = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FileBoundConstants.FILEBOUND_EXPORT_PLUGIN,
					FileBoundProperties.FILEBOUND_CONNECTION_URL);
			final String indexField = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FileBoundConstants.FILEBOUND_EXPORT_PLUGIN,
					FileBoundProperties.FILEBOUND_INDEX_FIELD);
			final String division = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FileBoundConstants.FILEBOUND_EXPORT_PLUGIN,
					FileBoundProperties.FILEBOUND_DIVISION);
			final String seperator = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FileBoundConstants.FILEBOUND_EXPORT_PLUGIN,
					FileBoundProperties.FILEBOUND_SEPERATOR);
			final String userName = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FileBoundConstants.FILEBOUND_EXPORT_PLUGIN,
					FileBoundProperties.FILEBOUND_USERNAME);
			final String password = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FileBoundConstants.FILEBOUND_EXPORT_PLUGIN,
					FileBoundProperties.FILEBOUND_PASSWORD);
			final String projectName = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FileBoundConstants.FILEBOUND_EXPORT_PLUGIN,
					FileBoundProperties.FILEBOUND_PROJECT_NAME);
			final String exportFormat = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FileBoundConstants.FILEBOUND_EXPORT_PLUGIN,
					FileBoundProperties.FILEBOUND_EXPORT_FORMAT);

			LOGGER.info("Filebound properties initialised successfully...");
			boolean divisionPresent = false;
			boolean seperatorPresent = false;
			checkInformation(connectionURL, userName, password, projectName, indexField, division, seperator);
			skipDlfNames = new ArrayList<String>();
			skipDlfNames.add(FileBoundConstants.DOCUMENT_NAME);
			if ((null != seperator) && !(seperator.equals(""))) {
				seperatorPresent = true;
				skipDlfNames.add(seperator);
			}
			if ((null != division) && !(division.equals(""))) {
				divisionPresent = true;
				skipDlfNames.add(division);
			}

			final List<String> cmdList = populateCommandsList();
			final Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);
			final List<Document> xmlDocuments = batch.getDocuments().getDocument();
			final StringBuffer docFieldValues = new StringBuffer();
			String indexFieldVal = "";
			final Document document = xmlDocuments.get(0);
			final DocumentLevelFields docLeveleFields = document.getDocumentLevelFields();
			if (docLeveleFields == null) {
				LOGGER.error("Document Level fields are null. So cannot upload documents of batch instance " + batchInstanceIdentifier
						+ " to filebound repository");
				throw new DCMAApplicationException("Document Level fields are null. So cannot upload documents of batch instance "
						+ batchInstanceIdentifier + " to filebound repository");
			}
			final List<DocField> docLevelFields = docLeveleFields.getDocumentLevelField();
			String indexFieldName = "";
			if (docLevelFields != null && !docLevelFields.isEmpty()) {
				int count = 0;
				for (DocField docField : docLevelFields) {
					String dlfName = docField.getName();
					final String dlfValue = docField.getValue();
					if (dlfName == null) {
						continue;
					}
					if (dlfName.equalsIgnoreCase(indexField)) {
						indexFieldVal = dlfValue;
						continue;
					}
					count++;
					if (!skipDlfNames.contains(docField.getName())) {
						indexFieldName = updateDocLevelFied(indexField, docFieldValues, docLevelFields, indexFieldName, count,
								dlfName, dlfValue);
					}
				}
			}
			if (indexFieldName.isEmpty()) {
				indexFieldName = indexField;
			}
			docFieldValues.insert(0, indexFieldName + "=" + indexFieldVal + ";;");
			String divider = "";
			divider = fetchDividerValues(xmlDocuments, division, divisionPresent);
			String seperatorVal = "";
			seperatorVal = fetchSeperatorValues(xmlDocuments, seperator, seperatorPresent);

			// Over-riding the divider value from the value configured in filebound-mapping.properties
			final String docName = document.getType();
			final String dividerMapping = fetchDocNameMapping(docName, batch.getBatchClassIdentifier());
			if (dividerMapping != null && dividerMapping.length() > 0) {
				divider = dividerMapping;
			}
			validateInformationForExporting(batchInstanceIdentifier, indexFieldVal, divider, divisionPresent, seperatorVal,
					seperatorPresent);
			// ----------------batch in error if all doclevel fields are null or empty as per discussion with
			// IKE------------------------\\
			validateDocLevelFields(batchInstanceIdentifier, docFieldValues);
			// ----------------------end-----------------------\\

			final String exportFolder = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceIdentifier)
					+ File.separator + batchInstanceIdentifier;
			final String docNamePathValues = getDocNamePathValues(xmlDocuments, exportFolder, batchInstanceIdentifier, exportFormat);
			final File fileboundParameterFile = new File(exportFolder + File.separator + FileBoundConstants.PARAMETERS_FILE_NAME);
			writeParametersToFile(batch, fileboundParameterFile, divider, docNamePathValues, seperatorVal, batchInstanceIdentifier);
			executeCommand(cmdList, connectionURL, userName, password, docFieldValues.toString(), indexFieldVal, projectName,
					indexFieldName, exportFolder + File.separator + FileBoundConstants.PARAMETERS_FILE_NAME);
			LOGGER.info("File bound upload successful.");
			LOGGER.info("Cleaning up intermediate filebound parameters file.....");
			if (fileboundParameterFile.exists()) {
				fileboundParameterFile.delete();
			}
		} else {
			LOGGER.info("Filebound configured not to be run.");
		}
	}

	private String updateDocLevelFied(final String indexField, final StringBuffer docFieldValues, final List<DocField> docLevelFields,
			String indexFieldName, int count, String dlfName, final String dlfValue) {
		String localDlfName = dlfName;
		String localIndexFieldName = indexFieldName;
		final String dlfNameFromLookup = getFieldNameFromLookup(localDlfName);
		if (dlfNameFromLookup != null && !dlfNameFromLookup.isEmpty()) {
			if (localDlfName.equalsIgnoreCase(indexField)) {
				localIndexFieldName = dlfNameFromLookup;
			}
			localDlfName = dlfNameFromLookup;
		}
		docFieldValues.append(localDlfName);
		docFieldValues.append('=');
		docFieldValues.append(dlfValue);
		if (count < docLevelFields.size()) {
			docFieldValues.append(";;");
		}
		return localIndexFieldName;
	}

	private void validateDocLevelFields(final String batchInstanceIdentifier, final StringBuffer docFieldValues)
			throws DCMAApplicationException {
		final String[] docLevelFieldsList = docFieldValues.toString().split(";;");
		boolean notValid = true;
		for (String docField : docLevelFieldsList) {
			// final String docFieldName = docField.substring(0, docField.lastIndexOf("="));
			final String docFieldVal = docField.substring(docField.lastIndexOf("=") + 1, docField.length());
			if (docFieldVal != null && !docFieldVal.isEmpty()) {
				notValid = false;
				break;
			}
		}
		if (notValid) {
			LOGGER.error("Cannot upload to File bound repository as all document level fields are null");
			throw new DCMAApplicationException(
					"Cannot upload to File bound repository as all document level fields are null for batch instance :"
							+ batchInstanceIdentifier);
		}
	}

	private void validateInformationForExporting(final String batchInstanceIdentifier, final String loanNumber, final String divider,
			boolean dividerPresent, final String seperatorVal, boolean seperatorPresent) throws DCMAApplicationException {
		if (loanNumber == null || loanNumber.length() <= 0) {
			LOGGER.error("Cannot upload to File bound repository as Index field is null or empty");
			throw new DCMAApplicationException("Index Field is null, so cannot upload documents for batch instance :"
					+ batchInstanceIdentifier);
		}

		if ((dividerPresent) && (divider == null || divider.length() <= 0)) {
			LOGGER.error("Cannot upload to File bound repository as Divider is null or empty");
			throw new DCMAApplicationException("Divider is null, so cannot upload documents for batch instance :"
					+ batchInstanceIdentifier);
		}

		if ((seperatorPresent) && (seperatorVal == null || seperatorVal.length() <= 0)) {
			LOGGER.error("Cannot upload to File bound repository as Seperator is null or empty");
			throw new DCMAApplicationException("Seperator is null, so cannot upload documents for batch instance :"
					+ batchInstanceIdentifier);
		}
	}

	private void checkInformation(final String connectionURL, final String userName, final String password, final String projectName,
			final String indexField, final String division, final String seperator) throws DCMAApplicationException {
		if (projectName == null || projectName.length() <= 0) {
			LOGGER.error("Project Name must not be null");
			throw new DCMAApplicationException("Project Name must not be null");
		}

		if (connectionURL == null || connectionURL.length() <= 0) {
			LOGGER.error("Connection URL must not be null");
			throw new DCMAApplicationException("Connection URL must not be null");
		}

		if (userName == null || userName.length() <= 0) {
			LOGGER.error("Username must not be null");
			throw new DCMAApplicationException("Username must not be null");
		}

		if (password == null || password.length() <= 0) {
			LOGGER.error("Password must not be null");
			throw new DCMAApplicationException("Password must not be null");
		}

		if (indexField == null || indexField.length() <= 0) {
			LOGGER.error("Index field must not be null");
			throw new DCMAApplicationException("Index field must not be null");
		}

		if (division == null) {
			LOGGER.error("Division must not be null");
			throw new DCMAApplicationException("Division must not be null");
		}
		if (seperator == null) {
			LOGGER.error("Seperator must not be null");
			throw new DCMAApplicationException("Seperator must not be null");
		}
	}
	/**
	 * This method is used to fetch the field name according to the lookup.
	 * @param fieldName {@link String}
	 * @return {@link String}
	 */
	public String getFieldNameFromLookup(final String fieldName) {
		String returnValue = null;
		final String filePath = FileBoundConstants.META_INF + File.separator + FileBoundConstants.FIELD_LOOKUP_PROPERTY_FILE;
		InputStream propertyInStream = null;
		try {
			propertyInStream = new ClassPathResource(filePath).getInputStream();
			final Properties properties = new Properties();
			properties.load(propertyInStream);
			returnValue = (String) properties.get(fieldName);
			if (returnValue != null) {
				returnValue = returnValue.trim();
			}
		} catch (Exception e) {
			LOGGER.error("Problem occured while fetching field name for Filebound");
		} finally {
			if (propertyInStream != null) {
				try {
					propertyInStream.close();
				} catch (IOException e) {
					LOGGER.error("Problem in closing stream");
				}
			}
		}
		return returnValue;
	}

	private String fetchSeperatorValues(final List<Document> xmlDocuments, String seperator, boolean seperatorPresent) {
		final StringBuffer returnValue = new StringBuffer();
		if (xmlDocuments != null) {
			int count = 0;
			for (Document document : xmlDocuments) {
				count++;
				if (seperatorPresent) {
					final List<DocField> docLevelFields = document.getDocumentLevelFields().getDocumentLevelField();
					if (docLevelFields != null && !docLevelFields.isEmpty()) {
						updateReturnValue(xmlDocuments, seperator, returnValue, count, docLevelFields);
					}
				} else {
					returnValue.append(' ');
					if (count < xmlDocuments.size()) {
						returnValue.append(";;");
					}
				}
			}
		}
		return returnValue.toString();
	}

	private void updateReturnValue(final List<Document> xmlDocuments, String seperator, final StringBuffer returnValue, int count,
			final List<DocField> docLevelFields) {
		for (DocField docField : docLevelFields) {
			final String dlfName = docField.getName();
			if (dlfName != null && dlfName.equalsIgnoreCase(seperator)) {
				returnValue.append(docField.getValue());
				if (count < xmlDocuments.size()) {
					returnValue.append(";;");
				}
			}
		}
	}

	private String fetchDividerValues(final List<Document> xmlDocuments, String division, boolean divisionPresent) {
		final StringBuffer returnValue = new StringBuffer();
		if (xmlDocuments != null) {
			int count = 0;
			for (Document document : xmlDocuments) {
				count++;
				if (divisionPresent) {
					final List<DocField> docLevelFields = document.getDocumentLevelFields().getDocumentLevelField();
					if (docLevelFields != null && !docLevelFields.isEmpty()) {
						updateReturnValue(xmlDocuments, division, returnValue, count, docLevelFields);
					}
				} else {
					returnValue.append(' ');
					if (count < xmlDocuments.size()) {
						returnValue.append(";;");
					}
				}
			}
		}
		return returnValue.toString();
	}

	private String getDocNamePathValues(final List<Document> xmlDocuments, final String exportFolder, final String batchInstanceID,
			final String exportFormat) throws DCMAApplicationException {
		final StringBuffer returnValue = new StringBuffer();
		if (xmlDocuments != null) {
			int count = 0;

			for (Document document : xmlDocuments) {
				count++;
				final String docName = document.getType();
				if (docName == null || docName.length() <= 0) {
					continue;
				}
				String docNameValue = "";
				final List<DocumentType> docType = pluginPropertiesService.getDocumentTypeByName(batchInstanceID, docName);
				if (docType == null) {
					docNameValue = docName;
				} else {
					final DocumentType doc = docType.get(0);
					if (doc != null) {
						docNameValue = doc.getDescription();
					}
				}
				returnValue.append(docNameValue);
				returnValue.append(";;");
				returnValue.append(exportFolder);
				returnValue.append(File.separator);
				String exportFileName = "";
				if (exportFormat.endsWith(FileType.PDF.getExtension())) {
					if (document.getMultiPagePdfFile() != null) {
						exportFileName = document.getMultiPagePdfFile();
					} else {
						LOGGER
								.error("Cannot upload to File bound repository as export format specified is 'pdf' and multipagepdffile attribute is not set.");
						throw new DCMAApplicationException("multipagepdffile is null, so cannot upload documents for batch instance :"
								+ batchInstanceID);
					}
				} else if (exportFormat.endsWith(FileType.TIF.getExtension())) {
					if (document.getMultiPageTiffFile() != null) {
						exportFileName = document.getMultiPageTiffFile();
					} else {
						LOGGER
								.error("Cannot upload to File bound repository as export format specified is 'tif' and multipagetiffile attribute is not set.");
						throw new DCMAApplicationException("multipagetiffile is null, so cannot upload documents for batch instance :"
								+ batchInstanceID);
					}
				}

				returnValue.append(exportFileName);
				if (count < xmlDocuments.size()) {
					returnValue.append(";;;;");
				}
			}
		}
		return returnValue.toString();
	}

	@SuppressWarnings("deprecation")
	private String fetchDocNameMapping(final String docName, final String batchClassIdentifier) {
		String returnValue = "";
		final String filePath = batchSchemaService.getBaseFolderLocation() + File.separator + batchClassIdentifier + File.separator
				+ FileBoundConstants.MAPPING_FOLDER_NAME + File.separator + FileBoundConstants.PROPERTY_FILE_NAME;
		DataInputStream dataInputStream = null;
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(filePath);
			dataInputStream = new DataInputStream(fileInputStream);
			String eachLine = dataInputStream.readLine();
			while (eachLine != null) {
				if (eachLine.length() > 0) {
					final String[] keyValue = eachLine.split(FileBoundConstants.MAPPING_SEPERATOR);
					if (keyValue != null && keyValue.length > 0 && keyValue[0].equalsIgnoreCase(docName)) {
						returnValue = keyValue[1];
						break;
					}
				}
				eachLine = dataInputStream.readLine();
			}
		} catch (IOException e) {
			LOGGER.error("Error occured in reading from properties file.");
		} finally {
			try {
				if (dataInputStream != null) {
					dataInputStream.close();
				}
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (IOException e) {
				LOGGER.debug("DataInputStream cannot be closed.");
			}
		}
		return returnValue;
	}

	/**
	 * This method writes the contents (divider, document name and path values, seperators) of parameters to be used by filebound
	 * native code.
	 * @param batchXML {@link Batch}
	 * @param fileboundParameterFile {@link File}
	 * @param dividers {@link String}
	 * @param docNamePathValues {@link String}
	 * @param seperators {@link String}
	 * @param batchInstanceIdentifier {@link String}
	 * @throws DCMAApplicationException if any exception occurs.
	 */
	public void writeParametersToFile(final Batch batchXML, final File fileboundParameterFile, final String dividers,
			final String docNamePathValues, final String seperators, final String batchInstanceIdentifier)
			throws DCMAApplicationException {

		final StringBuffer writeContent = new StringBuffer();
		if (dividers == null) {
			LOGGER.error("Cannot upload to File bound repository as Divider is null or empty");
			throw new DCMAApplicationException("Divider is null, so cannot upload documents for batch instance :"
					+ batchInstanceIdentifier);
		} else {
			writeContent.append(dividers);
		}
		if (docNamePathValues == null) {
			LOGGER.error("Cannot upload to File bound repository as docNamePathValues is null or empty");
			throw new DCMAApplicationException("DocNamePathValues is null, so cannot upload documents for batch instance :"
					+ batchInstanceIdentifier);
		} else {
			writeContent.append(";;;;;;;;");
			writeContent.append(docNamePathValues);
		}
		if (seperators == null) {
			LOGGER.error("Cannot upload to File bound repository as seperators is null or empty");
			throw new DCMAApplicationException("Seperators is null, so cannot upload documents for batch instance :"
					+ batchInstanceIdentifier);
		} else {
			writeContent.append(";;;;;;;;");
			writeContent.append(seperators);
		}
		try {
			FileUtils.writeStringToFile(fileboundParameterFile, writeContent.toString());
		} catch (IOException e) {
			LOGGER.error("Cannot write contents to the file : " + fileboundParameterFile.getAbsolutePath(), e);
			throw new DCMAApplicationException("Cannot write contents to the file : " + fileboundParameterFile.getAbsolutePath(), e);
		}
		LOGGER.info("Content to be written in file bound parameter text file :" + writeContent);
	}

	private void executeCommand(final List<String> cmdList, final String connectionURL, final String userName, final String password,
			final String docFieldValues, final String loanNumberValue, final String projectName, final String loanNumberName,
			final String parameterFileName) throws DCMAApplicationException {
		InputStreamReader inputStreamReader = null;
		BufferedReader input = null;
		try {
			final Runtime runtime = Runtime.getRuntime();
			if (cmdList.isEmpty()) {
				LOGGER.error("No proper commands configured in resources");
				throw new DCMAApplicationException("No proper commands configured in resources");
			} else {
				String[] cmds = new String[cmdList.size()];
				for (int i = 0; i < cmdList.size(); i++) {
					if (cmdList.get(i).contains("cmd")) {
						LOGGER.info("inside cmd");
						cmds[i] = cmdList.get(i);
					} else if (cmdList.get(i).contains("/c")) {
						LOGGER.info("inside /c");
						cmds[i] = cmdList.get(i);
					} else if (cmdList.get(i).contains("FileBoundExport")) {
						LOGGER.info("inside FileBoundExport");
						cmds[i] = cmdList.get(i) + " \"" + connectionURL + "\" " + userName + " " + password + " \"" + projectName
								+ FileBoundConstants.ESCAPED_SPACE + docFieldValues + FileBoundConstants.ESCAPED_SPACE + loanNumberName + FileBoundConstants.ESCAPED_SPACE + loanNumberValue
								+ FileBoundConstants.ESCAPED_SPACE + parameterFileName + "\"";
					}
				}
				LOGGER.info("command formed is :" + cmds[cmdList.size() - 1]);
				final Process process = runtime.exec(cmds, null, new File(System.getenv(FileBoundConstants.FILEBOUND_BASE_PATH)));
				inputStreamReader = new InputStreamReader(process.getInputStream());
				input = new BufferedReader(inputStreamReader);
				String line = null;
				do {
					line = input.readLine();
					LOGGER.debug(line);
				} while (line != null);
				final int exitValue = process.exitValue();
				LOGGER.info("Command exited with error code no " + exitValue);
				if (exitValue != 0) {
					LOGGER.error("Non-zero exit value for filebound command found. So exiting the application");
					throw new DCMAApplicationException("Non-zero exit value for filebound command found. So exiting the application");
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception while expoting ");
			throw new DCMAApplicationException("Exception while expoting ", e);
		} finally {
			try {
				if (input != null) {
					input.close();
				}
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
			} catch (IOException e) {
				LOGGER.error("Problem in closing stream");
			}
		}
	}

}
