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

package com.ephesoft.dcma.cmis.service;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.alfresco.cmis.client.AlfrescoDocument;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Policy;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.Ace;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.cmis.CMISDocumentDetails;
import com.ephesoft.dcma.cmis.CMISExporter;
import com.ephesoft.dcma.cmis.constant.CMISExportConstant;
import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;

/**
 * Abstract class for Upload file.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.cmis.CMISExporter;
 *
 */
public abstract class AbstractUploadFile {

	/**
	 * Logger instance for logging.
	 */
	protected static final Logger LOGGER = LoggerFactory.getLogger(CMISExporter.class);

	/**
	 * An instance of {@link BatchSchemaService}.
	 */
	private static BatchSchemaService batchSchemaService;
	
	
	/**
	 * @param batchSchemaService {@link BatchSchemaService}
	 */
	public static void setBatchSchemaService(BatchSchemaService batchSchemaService) {
		AbstractUploadFile.batchSchemaService = batchSchemaService;
	}
	
	/**
	 * This method is used to upload the file.
	 * @param batchInstanceIdentifier {@link String}
	 * @param rootFolder {@link String}
	 * @param alfrescoAspectSwitch {@link String}
	 * @param session {@link Session}
	 * @param batchInstanceFolder {@link Folder}
	 * @param sFolderToBeExported {@link String}
	 * @param batchClassIdentifier {@link String}
	 * @param document {@link Document}
	 * @param updatedCmisFileName {@link String}
	 * @param documentDetails {@link CMISDocumentDetails}
	 * @throws DCMAApplicationException {@link DCMAApplicationException}
	 */
	public abstract void uploadFile(String batchInstanceIdentifier, String rootFolder, String alfrescoAspectSwitch, Session session,
			Folder batchInstanceFolder, String sFolderToBeExported, String batchClassIdentifier, Document document,
			String updatedCmisFileName, CMISDocumentDetails documentDetails) throws DCMAApplicationException;

	protected static org.apache.chemistry.opencmis.client.api.Document uploadDocument(
			org.apache.chemistry.opencmis.client.api.Document doc, Session session, Folder folder, File file, Document document,
			String batchClassIdentifier, boolean isPdfFile, String batchInstanceIdentifier, String newFileName,
			CMISDocumentDetails documentDetails) throws IOException, DCMAApplicationException {
		String mimeType = null;
		org.apache.chemistry.opencmis.client.api.Document newDoc;
		try {
			FileNameMap fileNameMap = URLConnection.getFileNameMap();
			mimeType = fileNameMap.getContentTypeFor(file.getAbsolutePath());
		} catch (Exception e) {
			LOGGER.error("Could not find mime type. Using other method", e);
		}
		if (mimeType == null
				|| !(mimeType.equalsIgnoreCase(CMISExportConstant.IMAGE_MIME_TYPE) || mimeType
						.equalsIgnoreCase(CMISExportConstant.PDF_MIME_TYPE))) {
			mimeType = CMISExportConstant.IMAGE_MIME_TYPE;
			if (isPdfFile) {
				mimeType = CMISExportConstant.PDF_MIME_TYPE;
			}
		}

		LOGGER.info("Content mime type is: " + mimeType);

		FileInputStream fis = new FileInputStream(file);
		DataInputStream dis = new DataInputStream(fis);
		byte[] bytes = new byte[(int) file.length()];
		dis.readFully(bytes);
		ContentStream contentStream = null;

		Map<String, Object> newDocProps = getPropertyMap(file, document, batchClassIdentifier, newFileName, documentDetails
				.getPluginMappingFileName(), documentDetails.getDateFormat());

		LOGGER.info(newDocProps.toString());

		List<Ace> addAces = new LinkedList<Ace>();
		List<Ace> removeAces = new LinkedList<Ace>();
		List<Policy> policies = new LinkedList<Policy>();

		try {

			// BigInteger bInteger = new BigInteger("10");
			contentStream = new ContentStreamImpl(file.getAbsolutePath(), null, mimeType, new ByteArrayInputStream(bytes));

			VersioningState versioningState;// default Versioning State of Document

			try {
				versioningState = VersioningState.valueOf(documentDetails.getDocumentVersioningState().trim());
			} catch (Exception e) {
				LOGGER.error("In valid parameter set in the property file " + e.getMessage(), e);
				versioningState = VersioningState.NONE;
			}
			if (null != doc) {
				LOGGER.info(CMISExportConstant.DELETING_ALL_THE_VERSIONS_OF_THE_DOCUMENT + doc.getName()
						+ CMISExportConstant.BECAUSE_IT_ALREADY_EXISTS_ON_THE_REPOSITORY);
				doc.deleteAllVersions();
			}
			newDoc = folder.createDocument(newDocProps, contentStream, versioningState, policies, removeAces, addAces, session
					.getDefaultContext());
			/*
			 * doc = folder.createDocument(newDocProps, contentStream, versioningState, policies, removeAces, addAces, session
			 * .getDefaultContext());
			 */
			if (null != newDoc) {
				LOGGER.info("*** Created Document : " + file.getAbsolutePath() + "  " + newDoc.getCheckinComment());
			}
			// Document created

		} catch (CmisBaseException e) {
			String errorContent = ((CmisBaseException) e).getErrorContent();
			if (null != errorContent) {
				String errorText = getTextFromHtmlString(errorContent);
				LOGGER.error(CMISExportConstant.CUSTOM_ERROR_MESSAGE_FROM_CMIS_PLUGIN + errorText);
			}
			throw new DCMAApplicationException(CMISExportConstant.UNABLE_TO_UPLOAD_THE_DOCUMENT + file.getName(), e);
		} catch (Exception e) {
			LOGGER.error(CMISExportConstant.ERROR_WHILE_UPLOADING_DOCUMENT_WITH_IDENTIFIER + document.getIdentifier()
					+ CMISExportConstant.FOR_BATCH_INSTANCE + batchInstanceIdentifier, e);
			throw new DCMAApplicationException(CMISExportConstant.UNABLE_TO_UPLOAD_THE_DOCUMENT + file.getName(), e);
		} finally {
			try {
				if (dis != null) {
					dis.close();
				}
				if (fis != null) {
					fis.close();
				}
				if (contentStream != null && contentStream.getStream() != null) {
					contentStream.getStream().close();
				}
			} catch (IOException io) {
				LOGGER.error(CMISExportConstant.UNABLE_TO_CLOSE_THE_INPUT_STREAM + io.getMessage());
			}
		}
		return newDoc;
	}

	protected static Map<String, Object> getPropertyMap(File file, Document document, String batchClassIdentifier, String newFileName,
			String pluginMappingFileName, String dateFormat) throws DCMAApplicationException {

		if (null == file) {
			throw new DCMAApplicationException("File can not be null.");
		}

		Map<String, Object> newDocProps = new HashMap<String, Object>();
		newDocProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
		if (newFileName == null) {
			newDocProps.put(PropertyIds.NAME, file.getName());
		} else {
			newDocProps.put(PropertyIds.NAME, newFileName);
		}

		Properties properties = new Properties();
		FileInputStream fis = null;
		try {

			String propertyFolderPath = getPropertyFolderPath(batchClassIdentifier);
			String propertyFilePath = propertyFolderPath + File.separator + pluginMappingFileName;

			File propertyFile = new File(propertyFilePath);
			fis = new FileInputStream(propertyFile);
			properties.load(fis);
		} catch (IOException io) {
			LOGGER.error("Unable to load the property file. " + io.getMessage());
			throw new DCMAApplicationException("Unable to load the property file.", io);
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException io) {
				LOGGER.error("Unable to close the input stream of the property file. " + io.getMessage());
			}
		}

		Set<Object> set = properties.keySet();
		Iterator<Object> itr = set.iterator();

		DocumentLevelFields documentLevelFields = document.getDocumentLevelFields();
		if (null == documentLevelFields) {
			throw new DCMAApplicationException("Document level fields can not be null.");
		}

		List<DocField> documentLevelFieldList = documentLevelFields.getDocumentLevelField();
		if (documentLevelFieldList.isEmpty()) {
			throw new DCMAApplicationException("Document level fields can not be empty.");
		}

		while (itr.hasNext()) {
			Object key = itr.next();
			String keyString = (String) key;
			if (null != keyString) {
				String type = document.getType();
				// type = type.replaceAll(SPACE, "");
				if (null != type && keyString.contains(type)) {
					String value = properties.getProperty(keyString);
					LOGGER.info("property - keyString : " + keyString + " , property-value : " + value);
					if (keyString.equals(type)) {
						newDocProps.put(PropertyIds.OBJECT_TYPE_ID, value);
					} else {
						for (DocField fdType : documentLevelFieldList) {
							String name = fdType.getName();
							if (null != name && keyString.contains(name)
									&& !(fdType.getValue() == null || fdType.getValue().trim().isEmpty())) {
								// String nameDLF = name.replaceAll(SPACE, "");
								Object valueDLF = convert(fdType.getValue(), fdType.getType(), name, dateFormat);
								newDocProps.put(value, valueDLF);
								break;
							}
						}
					}

				}
			}
		}

		return newDocProps;

	}

	protected static void addAspectsToDocument(org.apache.chemistry.opencmis.client.api.Document doc, Document document,
			String batchClassIdentifier, String batchInstanceIdentifier, CMISDocumentDetails documentDetails)
			throws DCMAApplicationException {
		if (doc != null) {
			String aspectMappingFileName = documentDetails.getAspectMappingFileName();
			if (aspectMappingFileName == null || aspectMappingFileName.isEmpty()) {
				throw new DCMAApplicationException("Name for aspect mapping file not specified in cmis properties file.");
			}
			String filePath = getPropertyFolderPath(batchClassIdentifier) + File.separator + aspectMappingFileName;
			Properties aspectMappingProperties = getAspectProperties(filePath, documentDetails.getAspectProperties());
			Map<String, Object> newDocProperties = new HashMap<String, Object>();
			String documentType = document.getType();

			String documentTypeAspectsToBeAdded = aspectMappingProperties.getProperty(documentType);
			List<DocField> documentLevelFieldList = document.getDocumentLevelFields().getDocumentLevelField();
			AlfrescoDocument alfrescoDocument = (AlfrescoDocument) doc;
			if (documentTypeAspectsToBeAdded != null && !documentTypeAspectsToBeAdded.isEmpty()) {
				String[] aspects = documentTypeAspectsToBeAdded.split(";");
				for (String aspect : aspects) {
					try {
						LOGGER.info(CMISExportConstant.ADDING_ASPECT + aspect + CMISExportConstant.FOR_DOCUMENT_TYPE + documentType
								+ CMISExportConstant.FOR_BATCH_INSTANCE + batchInstanceIdentifier);
						alfrescoDocument.addAspect(aspect);
					} catch (CmisBaseException e) {
						String errorContent = ((CmisBaseException) e).getErrorContent();
						if (null != errorContent) {
							String errorText = getTextFromHtmlString(errorContent);
							LOGGER.error(CMISExportConstant.CUSTOM_ERROR_MESSAGE_FROM_CMIS_PLUGIN + errorText);
						}
						String errorMsg = CMISExportConstant.UNABLE_TO_ADD_ASPECT + aspect
								+ CMISExportConstant.DEFINED_IN_THE_PROPERTY_MAPPING_FILE + filePath
								+ CMISExportConstant.FOR_BATCH_INSTANCE + batchInstanceIdentifier;
						throw new DCMAApplicationException(errorMsg, e);
					} catch (Exception e) {
						String errorMsg = CMISExportConstant.UNABLE_TO_ADD_ASPECT + aspect
								+ CMISExportConstant.DEFINED_IN_THE_PROPERTY_MAPPING_FILE + filePath
								+ CMISExportConstant.FOR_BATCH_INSTANCE + batchInstanceIdentifier;
						LOGGER.error(errorMsg, e);
						throw new DCMAApplicationException(errorMsg, e);
					}
				}
			}
			Set<Object> keySet = aspectMappingProperties.keySet();
			Iterator<Object> iterator = keySet.iterator();
			while (iterator.hasNext()) {
				applyAspect(document, batchInstanceIdentifier, filePath, aspectMappingProperties, newDocProperties, documentType,
						documentLevelFieldList, alfrescoDocument, iterator, documentDetails.getDateFormat());
			}
			if (!newDocProperties.isEmpty()) {
				String logMsg = CMISExportConstant.UPDATING_THE_ADDED_PROPERTIES_FOR + alfrescoDocument.getName();
				LOGGER.info(logMsg);
				try {
					alfrescoDocument.updateProperties(newDocProperties);
				} catch (CmisBaseException e) {
					String errorContent = ((CmisBaseException) e).getErrorContent();
					if (null != errorContent) {
						String errorText = getTextFromHtmlString(errorContent);
						LOGGER.error(CMISExportConstant.CUSTOM_ERROR_MESSAGE_FROM_CMIS_PLUGIN + errorText);
					}
					String errMsg = CMISExportConstant.COULD_NOT_UPDATE_PROPERTIES + CMISExportConstant.FOR_DOCUMENT_ID
							+ document.getIdentifier() + CMISExportConstant.FOR_BATCH_INSTANCE + batchInstanceIdentifier;
					throw new DCMAApplicationException(errMsg, e);

				} catch (Exception e) {
					String errMsg = CMISExportConstant.COULD_NOT_UPDATE_PROPERTIES + CMISExportConstant.FOR_DOCUMENT_ID
							+ document.getIdentifier() + CMISExportConstant.FOR_BATCH_INSTANCE + batchInstanceIdentifier;
					throw new DCMAApplicationException(errMsg, e);
				}
			}
		}
	}

	/**
	 * This method extracts the text from html string.
	 * @param htmlString {@link String}
	 * @return {@link String}
	 */
	public static String getTextFromHtmlString(String htmlString) {
		String errorText = "";
		CleanerProperties cleanerProps = new CleanerProperties();
		// set some properties to non-default values
		cleanerProps.setTransResCharsToNCR(true);
		cleanerProps.setTranslateSpecialEntities(true);
		cleanerProps.setOmitComments(true);
		cleanerProps.setOmitDoctypeDeclaration(true);
		cleanerProps.setOmitXmlDeclaration(true);
		cleanerProps.setUseEmptyElementTags(true);

		HtmlCleaner cleaner = new HtmlCleaner(cleanerProps);
		TagNode tagNode = cleaner.clean(htmlString);
		Object[] rootNode = null;
		try {
			rootNode = tagNode.evaluateXPath("//table");
			if (null != rootNode && rootNode.length > 0) {
				TagNode[] textNode = ((TagNode) rootNode[rootNode.length - 1]).getElementsByName("td", true);
				for (TagNode tag : textNode) {
					if (tag != null && tag.getText() != null) {
						StringBuilder errorTextString = new StringBuilder();
						errorTextString.append(errorText);
						if (tag.getText().toString().trim().equals("&nbsp;")) {
							errorTextString.append(" ");
							errorText = errorTextString.toString();
						} else {
							errorTextString.append(tag.getText());
							errorText = errorTextString.toString();
						}
					}
				}
			}
		} catch (XPatherException e) {
			LOGGER.error("Error extracting table node from html." + e.getMessage());
		}
		return errorText;
	}

	private static String getPropertyFolderPath(String batchClassIdentifier) throws DCMAApplicationException {
		String propertyFolderPath = batchSchemaService.getAbsolutePath(batchClassIdentifier, batchSchemaService
				.getCmisPluginMappingFolderName(), false);
		if (null == propertyFolderPath) {
			throw new DCMAApplicationException("In valid folder name in properties file.");
		}
		return propertyFolderPath;
	}

	/**
	 * This method converts the string to their respective format.
	 * @param value {@link String}
	 * @param type {@link String}
	 * @param property {@link String}
	 * @param dateFormat {@link String}
	 * @return {@link Object}
	 */
	public static Object convert(String value, String type, String property, String dateFormat) {
		Object returnValue = null;
		switch (DataType.getDataType(type)) {
			case DATE:
				try {
					GregorianCalendar calendar = new GregorianCalendar();
					DateFormat formatter = null;
					Date date;
					formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
					date = (Date) formatter.parse(value);
					calendar.setTime(date);
					returnValue = calendar;
					LOGGER.info(CMISExportConstant.CONVERTING + property + " to dataType date");
				} catch (ParseException e) {
					LOGGER.error("Could not parse date. Using string instead to upload document" + e);
					returnValue = (Object) value;
				} catch (Exception e) {
					LOGGER.error("Could not parse date. Using string instead to upload document" + e);
					returnValue = (Object) value;
				}
				break;
			case DOUBLE:
			case FLOAT:
				LOGGER.info(CMISExportConstant.CONVERTING + property + " to dataType BigDecimal");
				returnValue = new BigDecimal(value);
				break;
			case INTEGER:
				LOGGER.info(CMISExportConstant.CONVERTING + property + " to dataType Integer");
				returnValue = Integer.valueOf(value);
				break;
			case LONG:
				LOGGER.info(CMISExportConstant.CONVERTING + property + " to dataType Long");
				returnValue = Long.valueOf(value);
				break;
			case STRING:
				LOGGER.info(CMISExportConstant.CONVERTING + property + " to dataType String");
				returnValue = (Object) value;
				break;
			case BOOLEAN:
				LOGGER.info(CMISExportConstant.CONVERTING + property + " to dataType Boolean");
				String valueToConvert = value;

				if (value.equalsIgnoreCase("yes")) {
					valueToConvert = "true";
				} else {
					try {
						int valueInt = Integer.parseInt(valueToConvert);
						if (valueInt != 0) {
							valueToConvert = "true";
						} else {
							valueToConvert = CMISExportConstant.FALSE;
						}
					} catch (NumberFormatException nfe) {
						valueToConvert = CMISExportConstant.FALSE;
						LOGGER.info("Found non integer value in boolean field.");
					}
				}

				returnValue = Boolean.valueOf(valueToConvert);
				break;
			default:
				LOGGER.info(CMISExportConstant.CONVERTING + property + " to default value of String");
				returnValue = (Object) value;
				break;
		}
		return returnValue;
	}

	private static void applyAspect(Document document, String batchInstanceIdentifier, String filePath,
			Properties aspectMappingProperties, Map<String, Object> newDocProperties, String documentType,
			List<DocField> documentLevelFieldList, AlfrescoDocument alfrescoDocument, Iterator<Object> iterator, String dateFormat)
			throws DCMAApplicationException {
		Object key = iterator.next();
		String keyString = (String) key;
		if (keyString.contains(documentType + CMISExportConstant.DOT)) {
			String documentLevelFieldName = keyString.substring(documentType.length() + 1);
			if (documentLevelFieldName != null && !documentLevelFieldName.isEmpty()) {
				String aspectProperty = (String) aspectMappingProperties.get(key);
				LOGGER.info(CMISExportConstant.SEARCHING_FOR_EXISTENCE_OF_ASPECT_PROPERTY + aspectProperty
						+ CMISExportConstant.ON_CMIS_REPOSITORY);
				if (alfrescoDocument.findAspect(aspectProperty) != null) {
					if (aspectProperty != null && !aspectProperty.isEmpty()) {
						String docLevelFieldValue = null;
						String docFieldType = null;
						for (DocField docField : documentLevelFieldList) {
							if (docField != null && docField.getName() != null && !docField.getName().isEmpty()
									&& docField.getName().equals(documentLevelFieldName.trim())) {
								docLevelFieldValue = docField.getValue();
								docFieldType = docField.getType();
							}
						}
						if (docLevelFieldValue != null && docFieldType != null) {
							Object aspectPropertyValue = convert(docLevelFieldValue, docFieldType, documentLevelFieldName,
									dateFormat);
							newDocProperties.put(aspectProperty, aspectPropertyValue);
							String logMsg = CMISExportConstant.ADDED_ASPECT_PROPERTY + aspectProperty
									+ CMISExportConstant.FOR_DOCUMENT_LEVEL_FIELD + documentLevelFieldName
									+ CMISExportConstant.FOR_DOCUMENT_ID + document.getIdentifier()
									+ CMISExportConstant.HAVING_DOCUMENT_TYPE + documentType + CMISExportConstant.FOR_BATCH_INSTANCE
									+ batchInstanceIdentifier;
							LOGGER.info(logMsg);
						} else {
							String errorMsg = CMISExportConstant.IMPROPER_MAPPING_IN_PROPERTY_FILE + filePath
									+ CMISExportConstant.FOR_BATCH_INSTANCE + batchInstanceIdentifier
									+ CMISExportConstant.NO_SUCH_DOCUMENT_LEVEL_FIELD_EXISTS + documentLevelFieldName
									+ CMISExportConstant.FOR_DOCUMENT_TYPE + document.getType()
									+ CMISExportConstant.DEFINED_FOR_DOCUMENT_ID + document.getIdentifier()
									+ CMISExportConstant.SKIPPING_THE_ASPECT_PROPERTY + aspectProperty
									+ CMISExportConstant.AND_CONTINUING;

							LOGGER.error(errorMsg);
						}
					}
				} else {
					String errorMsg = CMISExportConstant.IMPROPER_MAPPING_IN_PROPERTY_FILE + filePath
							+ CMISExportConstant.FOR_BATCH_INSTANCE + batchInstanceIdentifier
							+ CMISExportConstant.NO_SUCH_ASPECT_PROPERTY_EXISTS + aspectProperty;
					LOGGER.error(errorMsg);
					throw new DCMAApplicationException(errorMsg);
				}
			}
		}
	}

	private static Properties getAspectProperties(String filePath, Properties paramAspectProperties) throws DCMAApplicationException {
		FileInputStream propertyInStream = null;
		Properties aspectProperties = paramAspectProperties;
		if (aspectProperties == null) {
			aspectProperties = new Properties();
			try {
				File propertyFile = new File(filePath);
				propertyInStream = new FileInputStream(propertyFile);
				aspectProperties.load(propertyInStream);
			} catch (IOException e) {
				String errorMsg = CMISExportConstant.UNABLE_TO_READ_ASPECT_MAPPING_FROM_PROPERTY_FILE + filePath;
				LOGGER.error(errorMsg, e);
				throw new DCMAApplicationException(errorMsg, e);
			} finally {
				try {
					if (propertyInStream != null) {
						propertyInStream.close();
					}
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}
		return aspectProperties;
	}
	
	/**
	 * This method resets all the application properties.
	 * @param documentDetails {@link CMISDocumentDetails}
	 */
	public static void resetAspectProperties(CMISDocumentDetails documentDetails) {
		documentDetails.setAspectProperties(null);
	}
}
