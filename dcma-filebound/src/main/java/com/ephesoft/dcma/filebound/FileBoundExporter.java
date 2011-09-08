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
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.service.BatchInstanceService;

/**
 * This class is responsible for uploading all the output files to the repository folder. This will reads the batch.xml file. It finds
 * the names of multipage tif and pdf files from the batch.xml. Then it upload these files to the repository main root folder. At a
 * time it will upload only pdf or tif files.
 * 
 * @author Ephesoft
 * 
 * @version 1.0
 * @see com.ephesoft.dcma.fileboound.service.FileBoundServiceImpl
 */
@Component
public class FileBoundExporter implements ICommonConstants {

	private static final String FILEBOUND_EXPORT_PLUGIN = "FILEBOUND_EXPORT";

	private static final Logger LOGGER = LoggerFactory.getLogger(FileBoundExporter.class);

	/**
	 * The path where the recostar executable is placed.
	 */
	public static final String FILEBOUND_BASE_PATH = "FILEBOUND_PATH";

	public static List<String> skipDlfNames = null;

	public static final String DOCUMENT_NAME = "document_name";

	public static final String DIVISION_TYPE_NAME = "division_type_name";

	public static final String LOAN_NUMBER_NAME = "loan_number_value";

	public static final String SEPERATOR = "separator";

	private static final String PROPERTY_FILE_NAME = "filebound-mapping.properties";
	private static final String PARAMETERS_FILE_NAME = "filebound-parameters.properties";
	private static final String MAPPING_SEPERATOR = "===";

	private static final String META_INF = "META-INF" + File.separator + "dcma-filebound";
	private static final String FIELD_LOOKUP_PROPERTY_FILE = "filebound-field-lookup.properties";

	private static final String MAPPING_FOLDER_NAME = "filebound-plugin-mapping";

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
	public void setBatchSchemaService(BatchSchemaService batchSchemaService) {
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
	public void setBatchInstanceService(BatchInstanceService batchInstanceService) {
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
	public void setMandatorycmds(String mandatorycmds) {
		this.mandatorycmds = mandatorycmds;
	}

	/**
	 * This method populates the list of commands from ";" seperated String of commands.
	 * 
	 * @return List<String>
	 */
	public List<String> populateCommandsList() {
		List<String> cmdList = new ArrayList<String>();
		StringTokenizer token = new StringTokenizer(mandatorycmds, ";");
		while (token.hasMoreTokens()) {
			String eachCmd = token.nextToken();
			cmdList.add(eachCmd);
		}
		return cmdList;
	}

	/**
	 * This method reads the batch.xml file. It finds the names of multipage tif and pdf files from the batch.xml. Then it upload these
	 * files to the repository main root folder. At a time it will upload only pdf or tif files.
	 * 
	 * 
	 * @param batchInstanceIdentifier Long
	 * @throws JAXBException
	 * @throws DCMAApplicationException If not able to upload files to repository server. If invalid input parameters.
	 */
	public void exportFiles(String batchInstanceIdentifier) throws JAXBException, DCMAApplicationException {
		LOGGER.info("FileBound export plugin.");

		LOGGER.info("Initializing properties...");

		String isFileBoundON = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FILEBOUND_EXPORT_PLUGIN,
				FileBoundProperties.FILEBOUND_SWITCH);
		if (isFileBoundON == null || !isFileBoundON.equals("ON")) {
			LOGGER.info("Filebound configured not to be run.");
			return;
		}
		String connectionURL = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FILEBOUND_EXPORT_PLUGIN,
				FileBoundProperties.FILEBOUND_CONNECTION_URL);
		String userName = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FILEBOUND_EXPORT_PLUGIN,
				FileBoundProperties.FILEBOUND_USERNAME);
		String password = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FILEBOUND_EXPORT_PLUGIN,
				FileBoundProperties.FILEBOUND_PASSWORD);
		String projectName = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FILEBOUND_EXPORT_PLUGIN,
				FileBoundProperties.FILEBOUND_PROJECT_NAME);

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

		skipDlfNames = new ArrayList<String>();
		skipDlfNames.add(DOCUMENT_NAME);
		skipDlfNames.add(SEPERATOR);
		skipDlfNames.add(DIVISION_TYPE_NAME);

		List<String> cmdList = populateCommandsList();
		Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);
		List<Document> xmlDocuments = batch.getDocuments().getDocument();
		String docFieldValues = "";
		String loanNumber = "";
		Document document = xmlDocuments.get(0);
		DocumentLevelFields docLeveleFields = document.getDocumentLevelFields();
		if (docLeveleFields == null) {
			LOGGER.error("Document Level fields are null. So cannot upload documents of batch instance " + batchInstanceIdentifier
					+ " to filebound repository");
			throw new DCMAApplicationException("Document Level fields are null. So cannot upload documents of batch instance "
					+ batchInstanceIdentifier + " to filebound repository");
		}
		List<DocField> docLevelFields = docLeveleFields.getDocumentLevelField();
		String loanNumberName = "";
		if (docLevelFields != null && docLevelFields.size() > 0) {
			int count = 0;
			for (DocField docField : docLevelFields) {
				String dlfName = docField.getName();
				String dlfValue = docField.getValue();
				if (dlfName == null) {
					continue;
				}
				if (dlfName.equalsIgnoreCase(LOAN_NUMBER_NAME)) {
					loanNumber = dlfValue;
				}
				count++;
				if (!skipDlfNames.contains(docField.getName())) {
					String dlfNameFromLookup = getFieldNameFromLookup(dlfName);
					if (dlfNameFromLookup != null && !dlfNameFromLookup.isEmpty()) {
						if (dlfName.equalsIgnoreCase(LOAN_NUMBER_NAME)) {
							loanNumberName = dlfNameFromLookup;
						}
						dlfName = dlfNameFromLookup;
					}
					docFieldValues += dlfName + "=" + dlfValue;
					if (count < docLevelFields.size()) {
						docFieldValues += ";;";
					}
				}
			}
		}
		if (loanNumberName.isEmpty()) {
			loanNumberName = LOAN_NUMBER_NAME;
		}
		String divider = fetchDividerValues(xmlDocuments);
		String seperator = fetchSeperatorValues(xmlDocuments);
		// Over-riding the divider value from the value configured in filebound-mapping.properties
		String docName = document.getType();
		String dividerMapping = fetchDocNameMapping(docName, batch.getBatchClassIdentifier());
		if (dividerMapping != null && dividerMapping.length() > 0) {
			divider = dividerMapping;
		}

		if (loanNumber == null || loanNumber.length() <= 0) {
			LOGGER.error("Cannot upload to File bound repository as Loan number is null or empty");
			throw new DCMAApplicationException("Loan number is null, so cannot upload documents for batch instance :"
					+ batchInstanceIdentifier);
		}

		if (divider == null || divider.length() <= 0) {
			LOGGER.error("Cannot upload to File bound repository as Divider is null or empty");
			throw new DCMAApplicationException("Divider is null, so cannot upload documents for batch instance :"
					+ batchInstanceIdentifier);
		}

		if (seperator == null || seperator.length() <= 0) {
			LOGGER.error("Cannot upload to File bound repository as Seperator is null or empty");
			throw new DCMAApplicationException("Seperator is null, so cannot upload documents for batch instance :"
					+ batchInstanceIdentifier);
		}
		
		//----------------batch in error if all doclevel fields are null or empty as per discussion with IKE------------------------\\
		
		String[] docLevelFieldsList = docFieldValues.split(";;");
		boolean notValid = true; 
		for (String docField : docLevelFieldsList) {
			String docFieldName = docField.substring(0, docField.lastIndexOf("="));
			String docFieldVal = docField.substring(docField.lastIndexOf("=")+1, docField.length());
			if (!docFieldName.equals(loanNumberName)) {
				if (docFieldVal!=null && !docFieldVal.isEmpty()) {
					notValid = false;
					break;
				}
			}
		}
		if (notValid) {
			LOGGER.error("Cannot upload to File bound repository as all document level fields are null");
			throw new DCMAApplicationException("Cannot upload to File bound repository as all document level fields are null for batch instance :"
					+ batchInstanceIdentifier);
		}
		//----------------------end-----------------------\\
		
		String exportFolder = batchSchemaService.getLocalFolderLocation() + File.separator + batchInstanceIdentifier;
		String docNamePathValues = getDocNamePathValues(xmlDocuments, exportFolder, batchInstanceIdentifier);
		File fileboundParameterFile = new File(exportFolder + File.separator + PARAMETERS_FILE_NAME);
		writeParametersToFile(batch, fileboundParameterFile, divider, docNamePathValues, seperator, batchInstanceIdentifier);
		executeCommand(cmdList, connectionURL, userName, password, docFieldValues, loanNumber, projectName, loanNumberName,
				exportFolder + File.separator + PARAMETERS_FILE_NAME);
		LOGGER.info("File bound upload successful.");
		LOGGER.info("Cleaning up intermediate filebound parameters file.....");
		if(fileboundParameterFile.exists()){
			fileboundParameterFile.delete();
		}
	}

	public String getFieldNameFromLookup(String fieldName) {
		String returnValue = null;
		String filePath = META_INF + File.separator + FIELD_LOOKUP_PROPERTY_FILE;
		InputStream propertyInStream = null;
		try {
			propertyInStream = new ClassPathResource(filePath).getInputStream();
			Properties properties = new Properties();
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

	/**
	 * This method fetches the seperator values for all the document types seperated by ";;".
	 * 
	 * @param xmlDocuments
	 * @return
	 */
	private String fetchSeperatorValues(List<Document> xmlDocuments) {
		String returnValue = "";
		if (xmlDocuments != null) {
			int count = 0;
			for (Document document : xmlDocuments) {
				count++;
				List<DocField> docLevelFields = document.getDocumentLevelFields().getDocumentLevelField();
				if (docLevelFields != null && docLevelFields.size() > 0) {
					for (DocField docField : docLevelFields) {
						String dlfName = docField.getName();
						if (dlfName != null && dlfName.equalsIgnoreCase(SEPERATOR)) {
							returnValue += docField.getValue();
							if (count < xmlDocuments.size()) {
								returnValue += ";;";
							}
						}
					}
				}
			}
		}
		return returnValue;
	}

	/**
	 * This method fetches the divider values for all the document types seperated by ";;".
	 * 
	 * @param xmlDocuments
	 * @return
	 */
	private String fetchDividerValues(List<Document> xmlDocuments) {
		String returnValue = "";
		if (xmlDocuments != null) {
			int count = 0;
			for (Document document : xmlDocuments) {
				count++;
				List<DocField> docLevelFields = document.getDocumentLevelFields().getDocumentLevelField();
				if (docLevelFields != null && docLevelFields.size() > 0) {
					for (DocField docField : docLevelFields) {
						String dlfName = docField.getName();
						if (dlfName != null && dlfName.equalsIgnoreCase(DIVISION_TYPE_NAME)) {
							returnValue += docField.getValue();
							if (count < xmlDocuments.size()) {
								returnValue += ";;";
							}
						}
					}
				}
			}
		}
		return returnValue;
	}

	/**
	 * This method finds the document names and values for all documents seperated by ";;;;" while document name and values are
	 * seperated by ";;".
	 * 
	 * @param xmlDocuments
	 * @param exportFolder
	 * @return
	 */
	private String getDocNamePathValues(List<Document> xmlDocuments, String exportFolder, String batchInstanceID) {
		String returnValue = "";
		if (xmlDocuments != null) {
			int count = 0;
			for (Document document : xmlDocuments) {
				count++;
				String docName = document.getType();
				if (docName == null || docName.length() <= 0) {
					continue;
				}
				String docNameValue = "";
				List<DocumentType> docType = pluginPropertiesService.getDocumentTypeByName(batchInstanceID, docName);
				if (docType != null) {
					DocumentType doc = docType.get(0);
					if (doc != null) {
						docNameValue = doc.getDescription();
					}
				} else {
					docNameValue = docName;
				}
				returnValue += docNameValue + ";;" + exportFolder + File.separator + document.getMultiPagePdfFile();
				if (count < xmlDocuments.size()) {
					returnValue += ";;;;";
				}
			}
		}
		return returnValue;
	}

	@SuppressWarnings("deprecation")
	private String fetchDocNameMapping(String docName, String batchClassIdentifier) {
		String returnValue = "";
		String filePath = batchSchemaService.getBaseFolderLocation() + File.separator + batchClassIdentifier + File.separator
				+ MAPPING_FOLDER_NAME + File.separator + PROPERTY_FILE_NAME;
		DataInputStream dataInputStream = null;
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(filePath);
			dataInputStream = new DataInputStream(fileInputStream);
			String eachLine = null;
			while ((eachLine = dataInputStream.readLine()) != null) {
				if (eachLine.length() > 0) {
					String[] keyValue = eachLine.split(MAPPING_SEPERATOR);
					if (keyValue != null && keyValue.length > 0 && keyValue[0].equalsIgnoreCase(docName)) {
						returnValue = keyValue[1];
						break;
					}
				}
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
	 * 
	 * @param batchXML
	 * @param batchInstanceFolder
	 * @param dividers
	 * @param docNamePathValues
	 * @param seperators
	 * @param batchInstanceIdentifier
	 * @throws DCMAApplicationException
	 */
	public void writeParametersToFile(final Batch batchXML, final File fileboundParameterFile, final String dividers,
			final String docNamePathValues, final String seperators, final String batchInstanceIdentifier)
			throws DCMAApplicationException {

		String writeContent = "";
		if (dividers != null) {
			writeContent += dividers;
		} else {
			LOGGER.error("Cannot upload to File bound repository as Divider is null or empty");
			throw new DCMAApplicationException("Divider is null, so cannot upload documents for batch instance :"
					+ batchInstanceIdentifier);
		}
		if (docNamePathValues != null) {
			writeContent += ";;;;;;;;";
			writeContent += docNamePathValues;
		} else {
			LOGGER.error("Cannot upload to File bound repository as docNamePathValues is null or empty");
			throw new DCMAApplicationException("DocNamePathValues is null, so cannot upload documents for batch instance :"
					+ batchInstanceIdentifier);
		}
		if (seperators != null) {
			writeContent += ";;;;;;;;";
			writeContent += seperators;
		} else {
			LOGGER.error("Cannot upload to File bound repository as seperators is null or empty");
			throw new DCMAApplicationException("Seperators is null, so cannot upload documents for batch instance :"
					+ batchInstanceIdentifier);
		}
		try {
			FileUtils.writeStringToFile(fileboundParameterFile, writeContent);
		} catch (IOException e) {
			LOGGER.error("Cannot write contents to the file : " + fileboundParameterFile.getAbsolutePath(), e);
			throw new DCMAApplicationException("Cannot write contents to the file : " + fileboundParameterFile.getAbsolutePath(), e);
		}
		LOGGER.info("Content to be written in file bound parameter text file :" + writeContent);
	}

	/**
	 * This method executes the command which uploads the document to filebound repository.
	 * 
	 * @param cmdList
	 * @param connectionURL
	 * @param userName
	 * @param password
	 * @param docFieldValues
	 * @param divider
	 * @param docNamePathValues
	 * @param loanNumberValue
	 * @param seperatorValue
	 * @throws DCMAApplicationException
	 */
	private void executeCommand(final List<String> cmdList, final String connectionURL, final String userName, final String password,
			final String docFieldValues, final String loanNumberValue, final String projectName, final String loanNumberName,
			final String parameterFileName) throws DCMAApplicationException {
		InputStreamReader inputStreamReader = null;
		BufferedReader input = null;
		try {
			Runtime runtime = Runtime.getRuntime();
			if (!cmdList.isEmpty()) {
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
								+ "\" \"" + docFieldValues + "\" \"" + loanNumberName + "\" \"" + loanNumberValue + "\" \""
								+ parameterFileName + "\"";
					}
				}
				LOGGER.info("command formed is :" + cmds[cmdList.size() - 1]);
				Process process = runtime.exec(cmds, null, new File(System.getenv(FILEBOUND_BASE_PATH)));
				inputStreamReader = new InputStreamReader(process.getInputStream());
				input = new BufferedReader(inputStreamReader);
				String line = null;
				do {
					line = input.readLine();
					LOGGER.debug(line);
				} while (line != null);
				int exitValue = process.exitValue();
				LOGGER.info("Command exited with error code no " + exitValue);
				if (exitValue != 0) {
					LOGGER.error("Non-zero exit value for filebound command found. So exiting the application");
					throw new DCMAApplicationException("Non-zero exit value for filebound command found. So exiting the application");
				}
			} else {
				LOGGER.error("No proper commands configured in resources");
				throw new DCMAApplicationException("No proper commands configured in resources");
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
