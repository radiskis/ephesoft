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

package com.ephesoft.dcma.cmis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.cmis.client.AlfrescoDocument;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConnectionException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisRuntimeException;
import org.apache.commons.io.IOUtils;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.cmis.constants.CmisImporterConstants;
import com.ephesoft.dcma.da.domain.BatchClassCmisConfiguration;
import com.ephesoft.dcma.da.service.BatchClassCmisConfigService;

/**
 * This Class provides APIs for downloading and importing files from CMIS compliant repositiories.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.cmis.constants.CmisImporterConstants
 * 
 */
public class CmisImport {
	
	
	/**
	 * Reference for batchClassCmisConfigService.
	 */
	@Autowired
	private BatchClassCmisConfigService batchClassCmisConfigService;

	/**
	 * Reference for LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CmisImport.class);

	/**
	 * Reference for sessionFactory.
	 */
	private final SessionFactory sessionFactory = SessionFactoryImpl.newInstance();
	
	/**
	 * This api is used to import files.
	 */
	public void importFile() {
		final List<BatchClassCmisConfiguration> batchClassCmisConfigurations = batchClassCmisConfigService.getAllCmisConfigs();
		for (final BatchClassCmisConfiguration batchClassCmisConfiguration : batchClassCmisConfigurations) {
			if (batchClassCmisConfiguration != null) {
				final Session session = getRepoSession(batchClassCmisConfiguration);
				if (session != null) {
					final CmisObject cmisObject = getTargetFolder(session, batchClassCmisConfiguration);
					monitorTargetFolder(cmisObject, batchClassCmisConfiguration);
				} else {
					LOGGER.error("Unable to connect to the server:" + batchClassCmisConfiguration.getServerURL());
				}
			}
		}
	}

	private Session getRepoSession(final BatchClassCmisConfiguration batchClassCmisConfiguration) {
		final Map<String, String> parameter = new HashMap<String, String>();

		// User credentials.
		parameter.put(SessionParameter.USER, batchClassCmisConfiguration.getUserName());
		parameter.put(SessionParameter.PASSWORD, batchClassCmisConfiguration.getPassword());

		// Connection settings.
		parameter.put(SessionParameter.ATOMPUB_URL, batchClassCmisConfiguration.getServerURL()); // URL to your CMIS server.
		parameter.put(SessionParameter.REPOSITORY_ID, batchClassCmisConfiguration.getRepositoryID()); // Only necessary if there is
		// more than one repository.
		parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());

		// Set the alfresco object factory
		parameter.put(SessionParameter.OBJECT_FACTORY_CLASS, "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl");

		// Create session.
		Session session = null;
		try {
			// This supposes only one repository is available at the URL.
			final Repository soleRepository = sessionFactory.getRepositories(parameter).get(0);
			session = soleRepository.createSession();
		} catch (final CmisConnectionException e) {
			LOGGER.error("Error in connecting to cmis server " + batchClassCmisConfiguration.getServerURL() + " " + e.getMessage(), e);
		} catch (final CmisRuntimeException e) {
			LOGGER.error("Error in validation to cmis server " + batchClassCmisConfiguration.getServerURL() + " " + e.getMessage(), e);
		}
		return session;
	}

	private CmisObject getTargetFolder(final Session session, final BatchClassCmisConfiguration batchClassCmisConfiguration) {
		CmisObject targetFolder = null;
		LOGGER.info("Acquiring Root Folder.");
		final Folder root = session.getRootFolder();
		final ItemIterable<CmisObject> children = root.getChildren();
		LOGGER.info("Looping through Root Folder children.");
		for (final CmisObject cmisObject : children) {
			if (cmisObject != null && cmisObject.getName() != null
					&& cmisObject.getName().equalsIgnoreCase(batchClassCmisConfiguration.getFolderName())
					&& isObjectFolder(cmisObject)) {
				LOGGER.info("Verified that the target is of type cmis:folder.");
				LOGGER.info("Target folder, " + batchClassCmisConfiguration.getFolderName() + ", found.");
				targetFolder = cmisObject;
				break;
			}
		}
		return targetFolder;
	}

	private boolean isObjectFolder(final CmisObject cmisObject) {
		boolean isObjectFolder = false;
		if (cmisObject.getType() != null
				&& cmisObject.getType().getId().toString().equalsIgnoreCase(CmisImporterConstants.FOLDER_TYPE)) {
			isObjectFolder = true;
		}
		return isObjectFolder;
	}

	private void monitorTargetFolder(final CmisObject cmisFolder, final BatchClassCmisConfiguration batchClassCmisConfiguration) {
		final Folder targetFolder = (Folder) cmisFolder;
		final ItemIterable<CmisObject> folderChildren = targetFolder.getChildren();

		LOGGER.info("ImportFolder has children count of = " + folderChildren.getTotalNumItems());
		LOGGER.info("Looping through " + batchClassCmisConfiguration.getFolderName() + " children...");

		for (final CmisObject cmisObject : folderChildren) {
			LOGGER.info("Name = " + cmisObject.getName() + "\nId = " + cmisObject.getId());
			if (cmisObject instanceof Folder) {
				LOGGER.info("Found sub folder :" + cmisObject.getName());
				LOGGER.info("Skipping sub folder for processing");
				// Uncomment below line for supporting the subfolder.
				// monitorTargetFolder(cmisFolder);
			} else if (eligibleForImport(cmisObject, batchClassCmisConfiguration)) {
				LOGGER.info("Name = " + cmisObject.getName() + " HAS NOT BEEN IMPORTED TO EPHESOFT YET!");
				// GETTING OBJECT CONTENT
				String fileName = cmisObject.getName();
				final String destinationFolder = batchClassCmisConfiguration.getBatchClass().getUncFolder() + File.separator
						+ fileName.substring(0, fileName.indexOf('.')) + "_" + System.currentTimeMillis();
				final String destinationFile = destinationFolder + File.separator + fileName;
				copyObjectToLocation(getObjectContent(cmisObject), destinationFolder, destinationFile);

				outputToXml(generatePropertiesXml(cmisObject), destinationFolder + File.separator + fileName + "_PROPERTIES" + ".xml");
				setPropertyImportFlag(cmisObject, batchClassCmisConfiguration);
			}
		}
		LOGGER.info("***** Target Folder Monitor Service Scan END *****");
	}

	private boolean eligibleForImport(final CmisObject cmisObject, final BatchClassCmisConfiguration batchClassCmisConfiguration) {
		boolean eligibleForImport = false;
		final List<Property<?>> propList = cmisObject.getProperties();
		Property<Object> mimeType = cmisObject.getProperty(PropertyIds.CONTENT_STREAM_MIME_TYPE);
		if (mimeType != null) {
			final String mimeTypeValue = cmisObject.getProperty(PropertyIds.CONTENT_STREAM_MIME_TYPE).getValue();
			if (CmisImporterConstants.PDF_MIME_TYPE.equalsIgnoreCase(mimeTypeValue)
					|| CmisImporterConstants.TIFF_MIME_TYPE.equalsIgnoreCase(mimeTypeValue)) {
				boolean targetFound = false;
				for (int i = 0; i < propList.size() && !targetFound; i++) {
					final String propVal = propList.get(i).getValueAsString();
					if (propVal != null && propList.get(i).getId().equalsIgnoreCase(batchClassCmisConfiguration.getCmisProperty())
							&& propVal.equalsIgnoreCase(batchClassCmisConfiguration.getValue())) {
						eligibleForImport = true;
						break;
					}
				}
			}
		} else {
			eligibleForImport = false;
		}
		return eligibleForImport;
	}

	/**
	 * This api is used to send output to xml file.
	 * @param document {@link org.jdom.Document}
	 * @param destFile {@link String}
	 */
	public void outputToXml(final org.jdom.Document document, final String destFile) {
		final XMLOutputter outputter = new XMLOutputter();
		FileWriter writer = null;
		try {
			writer = new FileWriter(destFile);
			outputter.output(document, writer);
		} catch (final IOException e) {
			LOGGER.error("Error while generating cmis properties xml" + e.getMessage(), e);
		} finally {
			if (writer != null) {
				try {
					writer.flush();
					writer.close();
				} catch (final IOException e) {
					LOGGER.info("Error in closing output stream", e);
				}
			}
		}
	}

	private InputStream getObjectContent(final CmisObject cmisObject) {
		final Document document = (Document) cmisObject;
		return document.getContentStream().getStream();
	}

	private org.jdom.Document generatePropertiesXml(final CmisObject cmisObject) {
		final Element root = new Element(CmisImporterConstants.CMIS_IMPORT);
		final Element rChild = new Element(CmisImporterConstants.PROPERTIES);

		final List<Property<?>> propList = cmisObject.getProperties();
		for (int i = 0; i < propList.size(); i++) {
			rChild.addContent(createProperty(propList.get(i).getDisplayName(), propList.get(i).getValueAsString()));
		}
		root.addContent(rChild);
		return new org.jdom.Document(root);
	}

	private Element createProperty(final String name, final String value) {
		final Element property = new Element(CmisImporterConstants.PROPERTY);
		final Element propName = new Element(CmisImporterConstants.NAME);
		propName.setText(name);
		final Element propVal = new Element(CmisImporterConstants.VALUE);
		propVal.setText(value);
		property.addContent(propName);
		property.addContent(propVal);
		return property;
	}

	private void setPropertyImportFlag(final CmisObject cmisObject, final BatchClassCmisConfiguration batchClassCmisConfiguration) {
		final AlfrescoDocument alfrescoDocument = (AlfrescoDocument) cmisObject;
		final Map<String, Object> updateproperties = new HashMap<String, Object>();
		updateproperties.put(batchClassCmisConfiguration.getCmisProperty(), (Object) batchClassCmisConfiguration.getValueToUpdate());
		alfrescoDocument.updateProperties(updateproperties);
	}

	private void copyObjectToLocation(final InputStream inputStream, final String destPath, final String fileName) {
		OutputStream out = null;
		try {
			final File batchFolder = new File(destPath);
			if (!batchFolder.exists()) {
				batchFolder.mkdir();
			}
			out = new FileOutputStream(new File(fileName));
			int read = 0;
			final byte[] bytes = new byte[CmisImporterConstants.BYTE_ARRAY_SIZE];
			read = inputStream.read(bytes);
			while (read != -1) {
				out.write(bytes, 0, read);
				read = inputStream.read(bytes);
			}
			LOGGER.info("New file created @ " + fileName);
		} catch (final IOException e) {
			LOGGER.error("Error while copying file" + e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(inputStream);
		}
	}
}
