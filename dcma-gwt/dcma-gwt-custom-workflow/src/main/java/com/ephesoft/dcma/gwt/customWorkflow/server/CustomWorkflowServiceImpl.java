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

package com.ephesoft.dcma.gwt.customworkflow.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.core.common.PluginJpdlCreationInfo;
import com.ephesoft.dcma.da.domain.BatchClassPlugin;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;
import com.ephesoft.dcma.da.domain.Dependency;
import com.ephesoft.dcma.da.domain.Plugin;
import com.ephesoft.dcma.da.domain.PluginConfig;
import com.ephesoft.dcma.da.domain.PluginConfigSampleValue;
import com.ephesoft.dcma.da.property.DependencyTypeProperty;
import com.ephesoft.dcma.da.service.BatchClassPluginConfigService;
import com.ephesoft.dcma.da.service.BatchClassPluginService;
import com.ephesoft.dcma.da.service.PluginConfigService;
import com.ephesoft.dcma.da.service.PluginService;
import com.ephesoft.dcma.gwt.core.server.BatchClassUtil;
import com.ephesoft.dcma.gwt.core.server.DCMARemoteServiceServlet;
import com.ephesoft.dcma.gwt.core.shared.BatchConstants;
import com.ephesoft.dcma.gwt.core.shared.PluginConfigXmlDTO;
import com.ephesoft.dcma.gwt.core.shared.PluginDependencyXmlDTO;
import com.ephesoft.dcma.gwt.core.shared.PluginDetailsDTO;
import com.ephesoft.dcma.gwt.core.shared.PluginXmlDTO;
import com.ephesoft.dcma.gwt.core.shared.exception.GWTException;
import com.ephesoft.dcma.gwt.customworkflow.client.CustomWorkflowService;
import com.ephesoft.dcma.gwt.customworkflow.shared.CustomWorkflowSharedConstants;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.XMLUtil;
import com.ephesoft.dcma.workflow.service.PluginJpdlCreationService;
import com.ephesoft.dcma.workflow.service.common.DeploymentService;

public class CustomWorkflowServiceImpl extends DCMARemoteServiceServlet implements CustomWorkflowService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomWorkflowServiceImpl.class);

	@Override
	public Boolean addNewPlugin(String newPluginName, String xmlSourcePath, String jarSourcePath) throws GWTException {

		LOGGER.info("Saving the new plugin with following details:\n Plugin Name:\t" + newPluginName + "\n Plugin Xml Path:\t"
				+ xmlSourcePath + "\n Plugin Jar path:\t" + jarSourcePath);
		boolean pluginAdded = false;
		// Parse the data from xmlSourcePath file
		XPathFactory xFactory = new org.apache.xpath.jaxp.XPathFactoryImpl();
		XPath xpath = xFactory.newXPath();
		org.w3c.dom.Document pluginXmlDoc = null;

		try {
			pluginXmlDoc = XMLUtil.createDocumentFrom(FileUtils.getInputStreamFromZip(newPluginName, xmlSourcePath));
		} catch (Exception e) {
			String errorMsg = "Invalid xml content. Please try again.";
			LOGGER.error(errorMsg + e.getMessage(), e);
			throw new GWTException(errorMsg, e);
		}

		if (pluginXmlDoc != null) {

			// correct syntax
			NodeList pluginNodeList = null;
			try {
				pluginNodeList = (NodeList) xpath.evaluate(CustomWorkflowSharedConstants.PLUGIN_EXPR, pluginXmlDoc,
						XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				String errorMsg = CustomWorkflowSharedConstants.INVALID_XML_CONTENT_MESSAGE;
				LOGGER.error(errorMsg + e.getMessage(), e);
				throw new GWTException(errorMsg, e);
			}
			pluginAdded = extractingXMLContents(newPluginName, jarSourcePath, pluginAdded, xpath, pluginNodeList);
		}

		return pluginAdded;
	}

	private boolean extractingXMLContents(String newPluginName, String jarSourcePath, boolean pluginAdded, XPath xpath,
			NodeList pluginNodeList) throws GWTException {
		boolean pluginAddedTemp = pluginAdded;
		PluginXmlDTO pluginXmlDTO;
		if (pluginNodeList != null && pluginNodeList.getLength() == 1) {
			LOGGER.info("Reading the Xml contents");
			String backUpFileName = CustomWorkflowSharedConstants.EMPTY_STRING;
			String jarName = CustomWorkflowSharedConstants.EMPTY_STRING;
			String methodName = CustomWorkflowSharedConstants.EMPTY_STRING;
			String description = CustomWorkflowSharedConstants.EMPTY_STRING;
			String pluginName = CustomWorkflowSharedConstants.EMPTY_STRING;
			String workflowName = CustomWorkflowSharedConstants.EMPTY_STRING;
			String scriptFileName = CustomWorkflowSharedConstants.EMPTY_STRING;
			String serviceName = CustomWorkflowSharedConstants.EMPTY_STRING;
			String pluginApplicationContextPath = CustomWorkflowSharedConstants.EMPTY_STRING;
			boolean isScriptingPlugin = false;
			boolean overrideExisting = false;
			try {
				backUpFileName = (String) xpath.evaluate(CustomWorkflowSharedConstants.BACK_UP_FILE_NAME_EXPR, pluginNodeList
						.item(0), XPathConstants.STRING);
				jarName = (String) xpath.evaluate(CustomWorkflowSharedConstants.JAR_NAME_EXPR, pluginNodeList.item(0),
						XPathConstants.STRING);
				methodName = (String) xpath.evaluate(CustomWorkflowSharedConstants.METHOD_NAME_EXPR, pluginNodeList.item(0),
						XPathConstants.STRING);
				description = (String) xpath.evaluate(CustomWorkflowSharedConstants.PLUGIN_DESC_EXPR, pluginNodeList.item(0),
						XPathConstants.STRING);
				pluginName = (String) xpath.evaluate(CustomWorkflowSharedConstants.PLUGIN_NAME_EXPR, pluginNodeList.item(0),
						XPathConstants.STRING);
				workflowName = (String) xpath.evaluate(CustomWorkflowSharedConstants.PLUGIN_WORKFLOW_NAME_EXPR, pluginNodeList
						.item(0), XPathConstants.STRING);
				scriptFileName = (String) xpath.evaluate(CustomWorkflowSharedConstants.SCRIPT_FILE_NAME_EXPR, pluginNodeList
						.item(0), XPathConstants.STRING);
				serviceName = (String) xpath.evaluate(CustomWorkflowSharedConstants.SERVICE_NAME_EXPR, pluginNodeList.item(0),
						XPathConstants.STRING);
				isScriptingPlugin = Boolean.parseBoolean((String) xpath.evaluate(
						CustomWorkflowSharedConstants.IS_SCRIPT_PLUGIN_EXPR, pluginNodeList.item(0), XPathConstants.STRING));
				pluginApplicationContextPath = (String) xpath.evaluate(CustomWorkflowSharedConstants.APPLICATION_CONTEXT_PATH,
						pluginNodeList.item(0), XPathConstants.STRING);
				overrideExisting = Boolean.parseBoolean((String) xpath.evaluate(CustomWorkflowSharedConstants.OVERRIDE_EXISTING,
						pluginNodeList.item(0), XPathConstants.STRING));
			} catch (Exception e) {
				String errorMsg = "Error in xml content. A mandatory tag is missing or invalid.";
				LOGGER.error(errorMsg + e.getMessage(), e);
				throw new GWTException(errorMsg, e);
			}

			LOGGER.info("Back Up File Name: " + backUpFileName);
			LOGGER.info("Jar Name" + jarName);
			LOGGER.info("Method Name" + methodName);
			LOGGER.info("Description: " + description);
			LOGGER.info("Name: " + pluginName);
			LOGGER.info("Workflow Name" + workflowName);
			LOGGER.info("Script file Name" + scriptFileName);
			LOGGER.info("Service Name" + serviceName);
			LOGGER.info("Is scripting Plugin:" + isScriptingPlugin);
			LOGGER.info("Plugin application context path: " + pluginApplicationContextPath);
			if (!backUpFileName.isEmpty() && !jarName.isEmpty() && !methodName.isEmpty() && !description.isEmpty()
					&& !pluginName.isEmpty() && !workflowName.isEmpty() && !serviceName.isEmpty()
					&& !pluginApplicationContextPath.isEmpty()) {

				if (isScriptingPlugin && scriptFileName.isEmpty()) {
					String errorMsg = "Error in xml content. A mandatory field is missing.";
					LOGGER.error(errorMsg);
					throw new GWTException(errorMsg);
				}
				pluginXmlDTO = setPluginInfo(backUpFileName, jarName, methodName, description, pluginName, workflowName,
						scriptFileName, serviceName, pluginApplicationContextPath, isScriptingPlugin, overrideExisting);

				extractPluginConfigs(pluginXmlDTO, xpath, pluginNodeList);

				extractPluginDependenciesFromXml(pluginXmlDTO, xpath, pluginNodeList);

				boolean pluginAlreadyExists = !checkIfPluginExists(pluginName);

				saveOrUpdatePluginToDB(pluginXmlDTO);

				createAndDeployPluginJPDL(pluginXmlDTO);

				copyJarToLib(newPluginName, jarSourcePath);

				if (pluginAlreadyExists) {
					addPathToApplicationContext(pluginXmlDTO.getApplicationContextPath());
				}
				pluginAddedTemp = true;
				LOGGER.info("Plugin added successfully.");
			} else {
				String errorMsg = CustomWorkflowSharedConstants.INVALID_XML_CONTENT_MESSAGE;
				LOGGER.error(errorMsg);
				throw new GWTException(errorMsg);
			}
		} else {
			String errorMsg = "Invalid xml content. Number of plugins expected is one.";
			LOGGER.error(errorMsg);
			throw new GWTException(errorMsg);
		}
		return pluginAddedTemp;
	}

	/**
	 * @param pluginXmlDTO
	 * @param backUpFileName
	 * @param jarName
	 * @param methodName
	 * @param description
	 * @param pluginNAme
	 * @param workflowName
	 * @param scriptFileName
	 * @param serviceName
	 * @param pluginApplicationContextPath
	 * @param isScriptingPlugin
	 * @param overrideExisting
	 */
	private PluginXmlDTO setPluginInfo(String backUpFileName, String jarName, String methodName, String description,
			String pluginNAme, String workflowName, String scriptFileName, String serviceName, String pluginApplicationContextPath,
			boolean isScriptingPlugin, boolean overrideExisting) {
		PluginXmlDTO pluginXmlDTO = new PluginXmlDTO();
		pluginXmlDTO.setBackUpFileName(backUpFileName);
		pluginXmlDTO.setJarName(jarName);
		pluginXmlDTO.setMethodName(methodName);
		pluginXmlDTO.setPluginDesc(description);
		pluginXmlDTO.setPluginName(pluginNAme);
		pluginXmlDTO.setPluginWorkflowName(workflowName);
		if (isScriptingPlugin) {
			pluginXmlDTO.setScriptFileName(scriptFileName);
		}
		pluginXmlDTO.setServiceName(serviceName);
		pluginXmlDTO.setIsScriptPlugin(isScriptingPlugin);
		pluginXmlDTO.setApplicationContextPath(pluginApplicationContextPath);
		pluginXmlDTO.setOverrideExisting(overrideExisting);

		return pluginXmlDTO;
	}

	/**
	 * @param pluginXmlDTO
	 * @param xpath
	 * @param pluginNodeList
	 * @throws
	 */
	private void extractPluginDependenciesFromXml(PluginXmlDTO pluginXmlDTO, XPath xpath, NodeList pluginNodeList) throws GWTException {
		NodeList pluginDependenciesNode;
		try {
			pluginDependenciesNode = (NodeList) xpath.evaluate(CustomWorkflowSharedConstants.DEPENDENCIES_LIST_DEPENDENCY,
					pluginNodeList.item(0), XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			String errorMsg = "Invalid xml content. A mandatory field is missing.";
			LOGGER.error(errorMsg, e);
			throw new GWTException(errorMsg, e);
		}
		LOGGER.info("Extracting Dependencies from xml:");

		List<PluginDependencyXmlDTO> pluginDependencyXmlDTOs = new ArrayList<PluginDependencyXmlDTO>(0);
		int numberOfDependencies = pluginDependenciesNode.getLength();
		LOGGER.info(numberOfDependencies + " dependencies found");
		for (int index = 0; index < numberOfDependencies; index++) {
			PluginDependencyXmlDTO pluginDependencyXmlDTO = new PluginDependencyXmlDTO();
			LOGGER.info("Plugin Dependency " + index + ":");
			String dependencyType = CustomWorkflowSharedConstants.EMPTY_STRING;
			String dependencyValue = CustomWorkflowSharedConstants.EMPTY_STRING;
			String operation = CustomWorkflowSharedConstants.EMPTY_STRING;
			try {
				dependencyType = (String) xpath.evaluate(CustomWorkflowSharedConstants.PLUGIN_DEPENDENCY_TYPE, pluginDependenciesNode
						.item(index), XPathConstants.STRING);
				dependencyValue = (String) xpath.evaluate(CustomWorkflowSharedConstants.PLUGIN_DEPENDENCY_VALUE,
						pluginDependenciesNode.item(index), XPathConstants.STRING);
				operation = (String) xpath.evaluate(CustomWorkflowSharedConstants.OPERATION, pluginDependenciesNode.item(index),
						XPathConstants.STRING);
			} catch (XPathExpressionException e) {
				String errorMsg = "Error in xml content. A mandatory field is missing.";
				LOGGER.error(errorMsg, e);
				throw new GWTException(errorMsg, e);
			}

			if (!dependencyType.isEmpty() && !dependencyValue.isEmpty()) {
				LOGGER.info("Type: " + dependencyType);
				LOGGER.info("Value: " + dependencyValue);
				pluginDependencyXmlDTO.setPluginDependencyType(dependencyType);

				pluginDependencyXmlDTO.setPluginDependencyValue(dependencyValue);

				pluginDependencyXmlDTO.setOperation(operation);

				pluginDependencyXmlDTOs.add(pluginDependencyXmlDTO);
			}
		}
		pluginXmlDTO.setDependencyXmlDTOs(pluginDependencyXmlDTOs);
	}

	/**
	 * @param pluginXmlDTO
	 * @param xpath
	 * @param pluginPropertyNode
	 * @throws
	 */
	private void extractPluginConfigs(PluginXmlDTO pluginXmlDTO, XPath xpath, NodeList pluginNodeList) throws GWTException {
		try {
			NodeList pluginPropertyNode = (NodeList) xpath.evaluate(CustomWorkflowSharedConstants.PLUGIN_PROPERTY_EXPR, pluginNodeList
					.item(0), XPathConstants.NODESET);
			LOGGER.info("Extracting plugin Configs from the xml");
			List<PluginConfigXmlDTO> pluginConfigXmlDTOs = new ArrayList<PluginConfigXmlDTO>(0);
			int numberOfPluginConfigs = pluginPropertyNode.getLength();

			LOGGER.info(numberOfPluginConfigs + " plugin configs found: ");
			for (int index = 0; index < numberOfPluginConfigs; index++) {
				LOGGER.info("Plugin config " + index + ": ");
				boolean isMandetory;
				boolean isMultiValue;
				String configName = CustomWorkflowSharedConstants.EMPTY_STRING;
				String propertyType = CustomWorkflowSharedConstants.EMPTY_STRING;
				String propertyDescription = CustomWorkflowSharedConstants.EMPTY_STRING;
				String operation = CustomWorkflowSharedConstants.EMPTY_STRING;

				isMandetory = Boolean.parseBoolean((String) xpath.evaluate(
						CustomWorkflowSharedConstants.PLUGIN_PROPERTY_IS_MANDETORY_EXPR, pluginPropertyNode.item(index),
						XPathConstants.STRING));
				isMultiValue = Boolean.parseBoolean((String) xpath.evaluate(
						CustomWorkflowSharedConstants.PLUGIN_PROPERTY_IS_MULTI_VALUES_EXPR, pluginPropertyNode.item(index),
						XPathConstants.STRING));
				configName = (String) xpath.evaluate(CustomWorkflowSharedConstants.PLUGIN_PROPERTY_NAME_EXPR, pluginPropertyNode
						.item(index), XPathConstants.STRING);
				propertyType = (String) xpath.evaluate(CustomWorkflowSharedConstants.PLUGIN_PROPERTY_TYPE_EXPR, pluginPropertyNode
						.item(index), XPathConstants.STRING);
				propertyDescription = (String) xpath.evaluate(CustomWorkflowSharedConstants.PLUGIN_PROPERTY_DESC_EXPR,
						pluginPropertyNode.item(index), XPathConstants.STRING);
				operation = (String) xpath.evaluate(CustomWorkflowSharedConstants.OPERATION, pluginPropertyNode.item(index),
						XPathConstants.STRING);

				LOGGER.info("Extracting values for config: " + index);
				LOGGER.info("Is Mandatory" + isMandetory);
				LOGGER.info("Is Multivalue" + isMultiValue);
				LOGGER.info("Config Name" + configName);
				LOGGER.info("Property Type" + propertyType);
				LOGGER.info("Property Description" + propertyDescription);
				if (!configName.isEmpty() && !propertyType.isEmpty() && !propertyDescription.isEmpty()) {
					PluginConfigXmlDTO pluginConfigXmlDTO = new PluginConfigXmlDTO();
					pluginConfigXmlDTO.setPluginPropertyIsMandatory(isMandetory);
					pluginConfigXmlDTO.setPluginPropertyIsMultiValues(isMultiValue);
					pluginConfigXmlDTO.setPluginPropertyName(configName);
					pluginConfigXmlDTO.setPluginPropertyType(propertyType);
					pluginConfigXmlDTO.setPluginPropertyDesc(propertyDescription);
					pluginConfigXmlDTO.setOperation(operation);
					NodeList sampleValuesNode = (NodeList) xpath.evaluate(
							CustomWorkflowSharedConstants.PLUGIN_PROPERTY_SAMPLE_VALUES_EXPR, pluginPropertyNode.item(index),
							XPathConstants.NODESET);
					List<String> sampleValuesList = new ArrayList<String>(0);
					LOGGER.info("Extracting sample values: ");
					int numberOfSampleValues = sampleValuesNode.getLength();

					LOGGER.info(numberOfSampleValues + " sample values found");
					for (int sampleValueIndex = 0; sampleValueIndex < numberOfSampleValues; sampleValueIndex++) {

						String sampleValue = (String) xpath.evaluate(CustomWorkflowSharedConstants.PLUGIN_PROPERTY_SAMPLE_VALUE_EXPR,
								sampleValuesNode.item(sampleValueIndex), XPathConstants.STRING);
						LOGGER.info("Sample value " + sampleValueIndex + " :" + sampleValue);
						sampleValuesList.add(sampleValue);
					}
					pluginConfigXmlDTO.setPluginPropertySampleValues(sampleValuesList);
					pluginConfigXmlDTOs.add(pluginConfigXmlDTO);
				} else {
					String errorMsg = CustomWorkflowSharedConstants.INVALID_XML_CONTENT_MESSAGE;
					LOGGER.error(errorMsg);
					throw new GWTException(errorMsg);
				}
			}
			pluginXmlDTO.setConfigXmlDTOs(pluginConfigXmlDTOs);
		} catch (XPathExpressionException e) {
			String errorMsg = CustomWorkflowSharedConstants.INVALID_XML_CONTENT_MESSAGE;
			LOGGER.error(errorMsg, e);
			throw new GWTException(errorMsg, e);
		}
	}

	/**
	 * @param jarSourcePath
	 * @param jarSourcePath2
	 * @throws
	 */
	private void copyJarToLib(String pluginName, String jarSourcePath) throws GWTException {

		InputStream inputStream = null;
		// try {
		//
		// } catch (Exception e) {
		// String errorMsg = " Jar file is either not present or corrupted.";
		// LOGGER.error(errorMsg, e);
		// throw new GWTException(errorMsg, e);
		// }
		FileOutputStream fileOutputStream = null;

		LOGGER.info("JAR file source path: " + jarSourcePath);
		StringBuffer destFilePath = new StringBuffer(System.getenv(CustomWorkflowSharedConstants.DCMA_HOME));
		String destinationFilePathString = destFilePath.toString();
		LOGGER.info("DCMA HOME: " + destinationFilePathString);
		// File srcFile = new File(jarSourcePath);
		destFilePath.append(File.separator);
		destFilePath.append(CustomWorkflowSharedConstants.WEB_INF);
		destFilePath.append(File.separator);
		destFilePath.append(CustomWorkflowSharedConstants.LIB);
		destFilePath.append(File.separator);
		destFilePath.append(jarSourcePath);
		destinationFilePathString = destFilePath.toString();
		LOGGER.info("Copying the JAR File in source path: " + jarSourcePath + " to " + destinationFilePathString);
		File destFile = new File(destinationFilePathString);
		try {
			fileOutputStream = new FileOutputStream(destFile);
			// } catch (FileNotFoundException e) {
			// String errorMsg = " The jar file could not be copied.";
			// LOGGER.error(errorMsg, e);
			// throw new GWTException(errorMsg, e);
			// }
			//
			// try {
			inputStream = FileUtils.getInputStreamFromZip(pluginName, jarSourcePath);
			byte[] buf = new byte[1024];
			int len = inputStream.read(buf);
			while (len > 0) {
				fileOutputStream.write(buf, 0, len);
				len = inputStream.read(buf);
			}

		} catch (FileNotFoundException fnfe) {
			String errorMsg = fnfe.getMessage() + " File not found";
			LOGGER.error(errorMsg, fnfe);
			throw new GWTException(errorMsg, fnfe);
		} catch (IOException ioe) {
			String errorMsg = ioe.getMessage() + " Error copying file";
			LOGGER.error(errorMsg, ioe);
			throw new GWTException(errorMsg, ioe);
		} finally {

			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					LOGGER.error("Error closing input stream.");
				}
			}
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					LOGGER.error("Error closing file output stream.");
				}
			}
		}

	}

	/**
	 * @param pluginXmlDTO
	 * @throws IOException
	 */
	private void createAndDeployPluginJPDL(PluginXmlDTO pluginXmlDTO) throws GWTException {
		PluginJpdlCreationService pluginJpdlCreationService = this.getSingleBeanOfType(PluginJpdlCreationService.class);
		DeploymentService deploymentService = this.getSingleBeanOfType(DeploymentService.class);

		File workflowDirectory = null;

		String newWorkflowBasePath = CustomWorkflowSharedConstants.EMPTY_STRING;

		try {
			Properties allProperties = ApplicationConfigProperties.getApplicationConfigProperties().getAllProperties(
					CustomWorkflowSharedConstants.META_INF_DCMA_WORKFLOWS_PROPERTIES);
			newWorkflowBasePath = allProperties.getProperty(CustomWorkflowSharedConstants.NEW_WORKFLOWS_BASE_PATH);
		} catch (IOException e) {
			LOGGER.error("Error reading workflow base path from the properties file.");
		}

		workflowDirectory = new File(newWorkflowBasePath);

		LOGGER.info("Workflow directory: " + workflowDirectory.getAbsolutePath());
		if (!workflowDirectory.exists()) {

			workflowDirectory.mkdir();
		}
		File newPluginDirectory = new File(workflowDirectory.getAbsolutePath() + File.separator
				+ CustomWorkflowSharedConstants.PLUGINS + File.separator + pluginXmlDTO.getPluginWorkflowName());
		LOGGER.info("Creating the plugins JPDL directory + " + newPluginDirectory.getAbsolutePath() + " if not existing");
		newPluginDirectory.mkdirs();

		StringBuffer pluginJpdlPath = new StringBuffer();
		pluginJpdlPath.append(workflowDirectory.getPath());
		pluginJpdlPath.append(File.separator);
		pluginJpdlPath.append(CustomWorkflowSharedConstants.PLUGINS);
		pluginJpdlPath.append(File.separator);
		pluginJpdlPath.append(pluginXmlDTO.getPluginWorkflowName());

		List<PluginJpdlCreationInfo> pluginJpdlCreationInfos = new ArrayList<PluginJpdlCreationInfo>(0);

		LOGGER.info("Converting XML info into required form for creating JPDL");
		PluginJpdlCreationInfo pluginJpdlCreationInfo = new PluginJpdlCreationInfo(pluginXmlDTO.getPluginWorkflowName(), pluginXmlDTO
				.getIsScriptPlugin(), pluginXmlDTO.getServiceName(), pluginXmlDTO.getMethodName());

		pluginJpdlCreationInfos.add(pluginJpdlCreationInfo);

		LOGGER.info("Creating JPDL");
		String jpdlFilePath = pluginJpdlCreationService.writeJPDL(newPluginDirectory.getPath(), pluginXmlDTO.getPluginWorkflowName(),
				pluginJpdlCreationInfos);
		LOGGER.info("JPDL created at " + jpdlFilePath);
		pluginJpdlPath.append(jpdlFilePath);

		LOGGER.info("Deploying JPDL");
		try {
			deploymentService.deploy(pluginJpdlPath.toString());
		} catch (Exception exception) {
			String errorMsg = exception.getMessage() + " Error deploying plugin";
			LOGGER.error(errorMsg, exception);
			throw new GWTException(errorMsg, exception);
		}

	}

	private boolean checkIfPluginExists(String pluginName) {
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);
		Plugin pluginCheck = pluginService.getPluginPropertiesForPluginName(pluginName);

		boolean pluginExists = !(pluginCheck == null);

		return pluginExists;
	}

	private void saveOrUpdatePluginToDB(PluginXmlDTO pluginXmlDTO) throws GWTException {
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);
		PluginConfigService pluginConfigService = this.getSingleBeanOfType(PluginConfigService.class);

		String pluginName = pluginXmlDTO.getPluginName();

		boolean pluginExists = checkIfPluginExists(pluginName);

		if (!pluginExists) {
			// If plugin does not exists, create one always
			LOGGER.info(pluginName + " plugin does not exists, so creating one.");

			LOGGER.info("Preparing plugin object from the XMl content");
			Plugin newPlugin = new Plugin();
			try {
				newPlugin = preparePluginObject(pluginXmlDTO);
			} catch (Exception e) {
				String errorMsg = " Data in xml is invalid or corrupted";
				LOGGER.error(errorMsg, e);
				throw new GWTException(errorMsg, e);
			}
			LOGGER.info("Preparing plugin config object from the XMl content");
			List<PluginConfig> pluginConfigs = preparePluginConfigObjects(pluginXmlDTO);
			// Create new Plugin
			LOGGER.info("Creating new plugin: " + newPlugin.getPluginName());
			// Plugin does not exists and need to be created
			pluginService.createNewPlugin(newPlugin);
			for (PluginConfig newPluginConfig : pluginConfigs) {
				// Associate new plugin with new Plugin configs
				newPluginConfig.setPlugin(newPlugin);

				PluginConfig pluginConfigCheck = pluginConfigService.getPluginConfigByName(newPluginConfig.getName());
				if (pluginConfigCheck == null) {
					// save new Plugin configs
					LOGGER.info("Creating new plugin config: " + newPluginConfig.getName());
					pluginConfigService.createNewPluginConfig(newPluginConfig);
				} else {
					String errorMsg = "Error saving new plugin config. A plugin config with same name already exists.";
					LOGGER.error(errorMsg);
					throw new GWTException(errorMsg);
				}
			}
		} else {
			LOGGER.info(pluginName + " plugin already exists.");
			if (pluginXmlDTO.getOverrideExisting()) {

				LOGGER.info(CustomWorkflowSharedConstants.OVERRIDE_EXISTING + " tag is true, so Overriding the plugin.");
				Plugin pluginCheck = pluginService.getPluginPropertiesForPluginName(pluginName);
				if (pluginCheck != null) {
					updateExistingPlugin(pluginCheck, pluginXmlDTO);
				}
				LOGGER.info("Plugin contents merged, now updating in db.");
				pluginService.mergePlugin(pluginCheck);
			} else {
				LOGGER.info(CustomWorkflowSharedConstants.OVERRIDE_EXISTING + " tag is false, so not Overriding the plugin.");
				String errorMsg = " A plugin with same name already exists.";
				LOGGER.error(errorMsg);
				throw new GWTException(errorMsg);
			}
		}

	}

	private void updateExistingPlugin(Plugin pluginCheck, PluginXmlDTO pluginXmlDTO) throws GWTException {

		LOGGER.info("Updating plugin properties for plugin name: " + pluginCheck.getPluginName());

		String pluginDesc = pluginXmlDTO.getPluginDesc();
		LOGGER.info("Plugin description: " + pluginDesc);
		pluginCheck.setDescription(pluginDesc);

		String scriptFileName = pluginXmlDTO.getScriptFileName();
		LOGGER.info("Plugin Script Name: " + scriptFileName);
		pluginCheck.setScriptName(scriptFileName);

		updateRevisionNumber(pluginCheck);
		LOGGER.info("Plugin revision number: " + pluginCheck.getVersion());

		String pluginWorkflowName = pluginXmlDTO.getPluginWorkflowName();
		LOGGER.info("Plugin Workflow Name: " + pluginWorkflowName);
		pluginCheck.setWorkflowName(pluginWorkflowName);

		updatePluginConfig(pluginCheck, pluginXmlDTO);

		updatePluginDependencies(pluginCheck, pluginXmlDTO);

	}

	private void updatePluginDependencies(Plugin pluginCheck, PluginXmlDTO pluginXmlDTO) throws GWTException {
		try {
			List<Dependency> dependencyList = getDependencyFromXml(pluginXmlDTO, pluginCheck);
			pluginCheck.getDependencies().clear();

			LOGGER.info("Updating new Dependencies for plugin name: " + pluginCheck.getPluginName());
			pluginCheck.getDependencies().addAll(dependencyList);
		} catch (Exception e) {
			String errorMsg = "Error: " + "Invalid Dependencies name.";
			LOGGER.error(errorMsg, e);
			throw new GWTException(errorMsg, e);
		}
	}

	private void updatePluginConfig(Plugin pluginCheck, PluginXmlDTO pluginXmlDTO) {

		PluginConfigService pluginConfigService = this.getSingleBeanOfType(PluginConfigService.class);
		for (PluginConfigXmlDTO pluginConfigXmlDTO : pluginXmlDTO.getConfigXmlDTOs()) {

			String pluginConfigOperation = pluginConfigXmlDTO.getOperation();
			PluginConfig newPluginConfig = setPluginConfigObject(pluginConfigXmlDTO);

			PluginConfig existingPluginConfig = pluginConfigService.getPluginConfigByName(newPluginConfig.getName());

			boolean pluginConfigAlreadyExists = false;
			if (existingPluginConfig != null) {
				pluginConfigAlreadyExists = true;
			}

			if (pluginConfigOperation.equalsIgnoreCase(CustomWorkflowSharedConstants.ADD_OPERATION) || pluginConfigOperation.isEmpty()) {
				LOGGER.info("Adding new Plugin config with name: " + newPluginConfig.getName());
				if (pluginConfigAlreadyExists) {
					LOGGER.error("Plugin config you are trying to add, already exists. Please try different name.");
				} else {
					createNewPluginConfig(pluginCheck, newPluginConfig);
				}
			} else if (pluginConfigOperation.equalsIgnoreCase(CustomWorkflowSharedConstants.UPDATE_OPERATION)) {
				LOGGER.info("Updating Plugin config with name: " + newPluginConfig.getName());
				if (pluginConfigAlreadyExists) {
					// update existing
					updateExistingPluginConfig(pluginCheck, newPluginConfig, existingPluginConfig);
				} else {
					// Create new one
					LOGGER.info(newPluginConfig.getName() + " plugin does not exists, creating new one.");
					createNewPluginConfig(pluginCheck, newPluginConfig);
				}
			} else if (pluginConfigOperation.equalsIgnoreCase(CustomWorkflowSharedConstants.DELETE_OPERATION)) {
				LOGGER.info("Deleting Plugin config with name: " + newPluginConfig.getName());
				if (pluginConfigAlreadyExists) {
					// before removing the plugin config, update the references
					removePluginConfig(existingPluginConfig);

				} else {
					LOGGER
							.error("Plugin config you are trying to delete, does not exist. Please make sure you enter the correct name.");
				}
			}

		}
	}

	/**
	 * @param pluginCheck
	 * @param pluginConfigService
	 * @param newPluginConfig
	 * @param existingPluginConfig
	 */
	private void updateExistingPluginConfig(Plugin pluginCheck, PluginConfig newPluginConfig, PluginConfig existingPluginConfig) {
		PluginConfigService pluginConfigService = this.getSingleBeanOfType(PluginConfigService.class);
		LOGGER.info("Merge new properties for plugin config: " + newPluginConfig.getName());
		mergePluginConfig(existingPluginConfig, newPluginConfig);
		existingPluginConfig.setPlugin(pluginCheck);
		pluginConfigService.updatePluginConfig(existingPluginConfig);

		updateBatchClassPluginConfigs(existingPluginConfig);
	}

	/**
	 * @param batchClassPluginConfigService
	 * @param existingPluginConfig
	 */
	private void removePluginConfig(PluginConfig existingPluginConfig) {
		BatchClassPluginConfigService batchClassPluginConfigService = this.getSingleBeanOfType(BatchClassPluginConfigService.class);
		PluginConfigService pluginConfigService = this.getSingleBeanOfType(PluginConfigService.class);

		String pluginConfigName = existingPluginConfig.getName();
		LOGGER.info("Removing all the instances of batch class plugin configs for plugin config: " + pluginConfigName);
		List<BatchClassPluginConfig> batchClassPluginConfigs = batchClassPluginConfigService
				.getBatchClassPluginConfigForPluginConfigId(existingPluginConfig.getId());
		for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
			LOGGER.info("Removing batch class plugin config with id: " + batchClassPluginConfig.getId());
			batchClassPluginConfigService.removeBatchClassPluginConfig(batchClassPluginConfig);
		}
		LOGGER.info("Removing the plugin config with name: " + pluginConfigName);
		pluginConfigService.removePluginConfig(existingPluginConfig);
	}

	/**
	 * @param pluginCheck
	 * @param pluginConfigService
	 * @param newPluginConfig
	 * @return
	 */
	private void createNewPluginConfig(Plugin pluginCheck, PluginConfig newPluginConfig) {
		PluginConfigService pluginConfigService = this.getSingleBeanOfType(PluginConfigService.class);
		LOGGER.info("Creating new plugin config with name: " + newPluginConfig.getName());
		PluginConfig existingPluginConfig;
		existingPluginConfig = newPluginConfig;
		existingPluginConfig.setPlugin(pluginCheck);
		pluginConfigService.createNewPluginConfig(existingPluginConfig);
		addBatchClassPluginConfig(existingPluginConfig);
	}

	private void updateBatchClassPluginConfigs(PluginConfig existingPluginConfig) {
		BatchClassPluginConfigService batchClassPluginConfigService = this.getSingleBeanOfType(BatchClassPluginConfigService.class);
		String pluginConfigName = existingPluginConfig.getName();

		LOGGER.info("Updating all the instances of batch class plugin configs for plugin config: " + pluginConfigName);
		List<BatchClassPluginConfig> batchClassPluginConfigs = batchClassPluginConfigService
				.getBatchClassPluginConfigForPluginConfigId(existingPluginConfig.getId());

		for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
			batchClassPluginConfig.setPluginConfig(existingPluginConfig);
			setDefaultValueForNewConfig(batchClassPluginConfig);
		}
	}

	private void addBatchClassPluginConfig(PluginConfig existingPluginConfig) {
		BatchClassPluginService batchClassPluginService = this.getSingleBeanOfType(BatchClassPluginService.class);

		String pluginConfigName = existingPluginConfig.getName();

		long pluginId = existingPluginConfig.getPlugin().getId();
		LOGGER.info("Adding new instances of batch class plugin configs for plugin config: " + pluginConfigName);
		List<BatchClassPlugin> batchClassPlugins = batchClassPluginService.getBatchClassPluginForPluginId(pluginId);

		for (BatchClassPlugin batchClassPlugin : batchClassPlugins) {
			BatchClassPluginConfig batchClassPluginConfig = new BatchClassPluginConfig();
			LOGGER.info("Creating new Batch class plugin config for plugin id: " + pluginId + " in batch class plugin with id: "
					+ batchClassPlugin.getId());
			batchClassPluginConfig.setPluginConfig(existingPluginConfig);
			setDefaultValueForNewConfig(batchClassPluginConfig);
			batchClassPlugin.addBatchClassPluginConfig(batchClassPluginConfig);
			LOGGER.info("Updating Batch class plugin.");
			batchClassPluginService.updateBatchClassPlugin(batchClassPlugin);
		}

	}

	/**
	 * @param pluginConfig
	 * @param batchClassPluginConfig
	 */
	private void setDefaultValueForNewConfig(BatchClassPluginConfig batchClassPluginConfig) {
		LOGGER.info("Setting default value for batch class plugin configs.");
		DataType pluginConfigDataType = batchClassPluginConfig.getPluginConfig().getDataType();
		LOGGER.info("Plugin config is of type: " + pluginConfigDataType.name());
		if (pluginConfigDataType == DataType.BOOLEAN) {
			LOGGER.info("Plugin config is boolean data type, so default value will be " + BatchConstants.YES);
			batchClassPluginConfig.setValue(BatchConstants.YES);
		} else {
			boolean isMandatory = batchClassPluginConfig.getPluginConfig().isMandatory();
			if (pluginConfigDataType == DataType.STRING && isMandatory) {
				List<String> sampleValues = batchClassPluginConfig.getSampleValue();
				if (sampleValues == null || sampleValues.isEmpty()) {
					LOGGER.info("Plugin config is String data type with no sample values, so default value will be"
							+ BatchConstants.DEFAULT);
					batchClassPluginConfig.setValue(BatchConstants.DEFAULT);
				} else if (sampleValues.size() > 0) {
					String defaultSampleValue = sampleValues.get(0);
					LOGGER.info("Plugin config is String data type with sample values as " + sampleValues.toArray().toString()
							+ ", so default value will be" + defaultSampleValue);
					batchClassPluginConfig.setValue(defaultSampleValue);
				}
			} else if (pluginConfigDataType == DataType.INTEGER && isMandatory) {
				LOGGER.info("Plugin config is Integer data type, so default value will be" + BatchConstants.ZERO);
				batchClassPluginConfig.setValue(BatchConstants.ZERO);
			}
		}
	}

	private void mergePluginConfig(PluginConfig existingPluginConfig, PluginConfig newPluginConfig) {
		LOGGER.info("Merge plugin config with name: " + existingPluginConfig.getName());

		DataType dataType = newPluginConfig.getDataType();
		LOGGER.info("Plugin Config Data type: " + dataType);
		existingPluginConfig.setDataType(dataType);

		String description = newPluginConfig.getDescription();
		LOGGER.info("Plugin Config description: " + description);
		existingPluginConfig.setDescription(description);

		boolean mandatory = newPluginConfig.isMandatory();
		LOGGER.info("Plugin Config is Mandatory: " + mandatory);
		existingPluginConfig.setMandatory(mandatory);

		Boolean multiValue = newPluginConfig.isMultiValue();
		LOGGER.info("Plugin Config is Multi Value: " + multiValue);
		existingPluginConfig.setMultiValue(multiValue);

		LOGGER.info("Clearing existing sample values " + existingPluginConfig.getSampleValue().toArray().toString()
				+ " for existing plugin config");
		existingPluginConfig.getPluginConfigSampleValues().clear();

		for (PluginConfigSampleValue pluginConfigSampleValue : newPluginConfig.getPluginConfigSampleValues()) {
			LOGGER.info("Adding new sample values " + pluginConfigSampleValue.getSampleValue() + " for Plugin config: "
					+ existingPluginConfig.getName());
			pluginConfigSampleValue.setPluginConfig(null);
			existingPluginConfig.getPluginConfigSampleValues().add(pluginConfigSampleValue);
		}
	}

	public void updateRevisionNumber(Plugin pluginCheck) {
		LOGGER.info("Updating Plugin revision number for plugin : " + pluginCheck.getPluginName());

		String version = pluginCheck.getVersion();
		if (null != version) {

			String newVersion = version;
			if (version.contains(".")) {
				int lastIndex = version.lastIndexOf('.');
				String preFix = version.substring(0, lastIndex);
				String postFix = version.substring(lastIndex + 1);
				int revNumber = Integer.parseInt(postFix);
				revNumber++;
				newVersion = preFix + "." + revNumber;
			} else {
				int revNumber = Integer.parseInt(version);
				revNumber++;
				newVersion = String.valueOf(revNumber);
			}

			LOGGER.info("Updating Plugin revision number to " + newVersion + " for plugin : " + pluginCheck.getPluginName());
			pluginCheck.setVersion(newVersion);
		}
	}

	private List<PluginConfig> preparePluginConfigObjects(PluginXmlDTO pluginXmlDTO) {
		List<PluginConfig> pluginConfigs = new ArrayList<PluginConfig>(0);

		LOGGER.info("Preparing plugin config objects.");
		for (PluginConfigXmlDTO pluginConfigXmlDTO : pluginXmlDTO.getConfigXmlDTOs()) {
			PluginConfig pluginConfig = setPluginConfigObject(pluginConfigXmlDTO);
			pluginConfigs.add(pluginConfig);
		}
		return pluginConfigs;
	}

	/**
	 * @param pluginConfigXmlDTO
	 * @return
	 */
	private PluginConfig setPluginConfigObject(PluginConfigXmlDTO pluginConfigXmlDTO) {
		PluginConfig pluginConfig = new PluginConfig();
		pluginConfig.setDescription(pluginConfigXmlDTO.getPluginPropertyDesc());
		pluginConfig.setMandatory(pluginConfigXmlDTO.getPluginPropertyIsMandatory());
		pluginConfig.setMultiValue(pluginConfigXmlDTO.getPluginPropertyIsMultiValues());
		pluginConfig.setName(pluginConfigXmlDTO.getPluginPropertyName());
		pluginConfig.setDataType(DataType.getDataType(pluginConfigXmlDTO.getPluginPropertyType()));

		List<PluginConfigSampleValue> pluginConfigSampleValues = new ArrayList<PluginConfigSampleValue>();
		for (String sampleValue : pluginConfigXmlDTO.getPluginPropertySampleValues()) {
			PluginConfigSampleValue pluginConfigSampleValue = new PluginConfigSampleValue();
			pluginConfigSampleValue.setPluginConfig(pluginConfig);
			pluginConfigSampleValue.setSamplevalue(sampleValue);

			pluginConfigSampleValues.add(pluginConfigSampleValue);
		}
		pluginConfig.setPluginConfigSampleValues(pluginConfigSampleValues);
		return pluginConfig;
	}

	private Plugin preparePluginObject(PluginXmlDTO pluginXmlDTO) throws GWTException {
		Plugin plugin = new Plugin();

		plugin.setPluginName(pluginXmlDTO.getPluginName());
		plugin.setWorkflowName(pluginXmlDTO.getPluginWorkflowName());
		plugin.setDescription(pluginXmlDTO.getPluginDesc());
		if (pluginXmlDTO.getIsScriptPlugin()) {
			plugin.setScriptName(pluginXmlDTO.getScriptFileName());
		}
		plugin.setVersion(CustomWorkflowSharedConstants.VERSION);
		List<Dependency> dependenciesList = plugin.getDependencies();

		if (dependenciesList == null) {
			dependenciesList = new ArrayList<Dependency>();
		}

		dependenciesList = getDependencyFromXml(pluginXmlDTO, plugin);
		plugin.setDependencies(dependenciesList);
		return plugin;
	}

	/**
	 * @param pluginXmlDTO
	 * @param plugin
	 * @param dependenciesList
	 * @throws GWTException
	 */
	private List<Dependency> getDependencyFromXml(PluginXmlDTO pluginXmlDTO, Plugin plugin) throws GWTException {
		List<Dependency> dependenciesList = new ArrayList<Dependency>();
		for (PluginDependencyXmlDTO dependencyXmlDTO : pluginXmlDTO.getDependencyXmlDTOs()) {
			Dependency dependency = null;
			dependency = prepareDependency(plugin, dependencyXmlDTO);
			if (dependency != null) {
				dependenciesList.add(dependency);
			}
		}
		return dependenciesList;
	}

	/**
	 * @param plugin
	 * @param dependencyXmlDTO
	 * @param dependency
	 * @return
	 * @throws GWTException
	 */
	private Dependency prepareDependency(Plugin plugin, PluginDependencyXmlDTO dependencyXmlDTO) throws GWTException {
		Dependency dependency = new Dependency();
		if (validateDependenciesNameList(dependencyXmlDTO.getPluginDependencyValue())) {
			dependency = new Dependency();
			final DependencyTypeProperty dependencyType = BatchClassUtil.getDependencyTypePropertyFromValue(dependencyXmlDTO
					.getPluginDependencyType());
			if (dependencyType != null) {
				dependency.setDependencyType(dependencyType);
				dependency.setDependencies(getDependenciesIdentifierString(dependencyXmlDTO.getPluginDependencyValue()));
				dependency.setId(0);
				dependency.setPlugin(plugin);
			} else {
				String errorMsg = "Error: " + "Invalid Dependency type \"" + dependencyType + "\".";
				LOGGER.error(errorMsg);
				throw new GWTException(errorMsg);
			}

		} else {
			String errorMsg = "Error: " + "Invalid Dependencies name.";
			LOGGER.error(errorMsg);
			throw new GWTException(errorMsg);
		}
		return dependency;
	}

	private String getDependenciesIdentifierString(String dependencies) {
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);

		LOGGER.info("Converting dependencies name to ID's");
		String[] andDependencies = dependencies.split(CustomWorkflowSharedConstants.AND);
		StringBuffer andDependenciesNameAsString = new StringBuffer();

		for (String andDependency : andDependencies) {
			if (!andDependenciesNameAsString.toString().isEmpty()) {
				andDependenciesNameAsString.append(CustomWorkflowSharedConstants.AND);
			}
			String[] orDependencies = andDependency.split(CustomWorkflowSharedConstants.OR_CONSTANT);
			StringBuffer orDependenciesNameAsString = new StringBuffer();

			for (String dependencyName : orDependencies) {
				if (!orDependenciesNameAsString.toString().isEmpty()) {
					orDependenciesNameAsString.append(CustomWorkflowSharedConstants.OR_CONSTANT);
				}
				String dependencyId = String.valueOf(pluginService.getPluginPropertiesForPluginName(dependencyName).getId());
				LOGGER.info("Dependency Id for Dependency Name: " + dependencyName + " : " + dependencyId);
				orDependenciesNameAsString.append(dependencyId);

			}
			andDependenciesNameAsString.append(orDependenciesNameAsString);

			orDependenciesNameAsString = new StringBuffer();
		}

		return andDependenciesNameAsString.toString();
	}

	private boolean validateDependenciesNameList(String pluginDependencyValue) {
		boolean isValidated = false;
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);

		LOGGER.info("Validating dependencies name.");
		String[] andDependencies = pluginDependencyValue.split(CustomWorkflowSharedConstants.AND);

		for (String andDependency : andDependencies) {

			String[] orDependencies = andDependency.split(CustomWorkflowSharedConstants.OR_CONSTANT);

			for (String dependencyName : orDependencies) {
				if (pluginService.getPluginPropertiesForPluginName(dependencyName) == null) {
					isValidated = false;
					String errorMsg = "No dependency found with name " + dependencyName;
					LOGGER.error(errorMsg);
					break;
				} else {
					isValidated = true;
				}
			}

		}
		return isValidated;
	}

	@Override
	public List<PluginDetailsDTO> getAllPluginDetailDTOs() {
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);
		List<PluginDetailsDTO> pluginDetailsDTOs = new ArrayList<PluginDetailsDTO>(0);
		LOGGER.info("Getting all plugin details from DB");
		List<Plugin> allPluginsNames = pluginService.getAllPlugins();
		for (Plugin plugin : allPluginsNames) {
			LOGGER.info("Preparing plugin object for " + plugin.getPluginName() + " plugin");
			pluginDetailsDTOs.add(BatchClassUtil.createPluginDetailsDTO(plugin, pluginService));
		}
		return pluginDetailsDTOs;
	}

	private PluginDetailsDTO updatePlugin(PluginDetailsDTO pluginDetailsDTO) {
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);
		String pluginIdentifier = pluginDetailsDTO.getIdentifier();
		LOGGER.info("Updating the plugin object with id: " + pluginIdentifier);
		PluginDetailsDTO updatedPluginDetailsDTO = null;
		try {
			Long pluginId = Long.valueOf(pluginIdentifier);
			Plugin plugin = pluginService.getPluginPropertiesForPluginId(pluginId);
			LOGGER.info("Merging the changes in plugin.");
			BatchClassUtil.mergePluginFromDTO(plugin, pluginDetailsDTO, pluginService);
			LOGGER.info("updating plugin");
			pluginService.mergePlugin(plugin);
			updatedPluginDetailsDTO = BatchClassUtil.createPluginDetailsDTO(plugin, pluginService);
		} catch (NumberFormatException e) {
			LOGGER.error("Error converting number " + e.getMessage(), e);
		}
		return updatedPluginDetailsDTO;
	}

	@Override
	public List<PluginDetailsDTO> updateAllPluginDetailsDTOs(List<PluginDetailsDTO> allPluginDetailsDTO) {
		List<PluginDetailsDTO> allUpdatedPluginDetailsDTO = new ArrayList<PluginDetailsDTO>(0);
		LOGGER.info("Updating all plugins");
		for (PluginDetailsDTO pluginDetailsDTO : allPluginDetailsDTO) {
			if (pluginDetailsDTO.isDirty()) {
				LOGGER.info(pluginDetailsDTO.getPluginName() + " plugin will be updated");
				final PluginDetailsDTO updatedPluginDetailsDto = updatePlugin(pluginDetailsDTO);
				if (updatedPluginDetailsDto != null) {
					allUpdatedPluginDetailsDTO.add(updatedPluginDetailsDto);
				}
			} else {
				LOGGER.info(pluginDetailsDTO.getPluginName() + " plugin does not need update");
				allUpdatedPluginDetailsDTO.add(pluginDetailsDTO);
			}
		}
		return allUpdatedPluginDetailsDTO;
	}

	private void addPathToApplicationContext(String pluginApplicationContextPath) throws GWTException {
		StringBuffer applicationContextFilePathBuffer = new StringBuffer(System.getenv(CustomWorkflowSharedConstants.DCMA_HOME));
		applicationContextFilePathBuffer.append(File.separator);
		applicationContextFilePathBuffer.append(CustomWorkflowSharedConstants.APPLICATION_CONTEXT_PATH_XML);
		String applicationContextFilePath = applicationContextFilePathBuffer.toString();
		File applicationContextFile = new File(applicationContextFilePath);
		LOGGER.info("Making entry for " + pluginApplicationContextPath + " in the application context file at: "
				+ applicationContextFilePath);
		try {
			Document xmlDocument = XMLUtil.createDocumentFrom(applicationContextFile);

			NodeList beanTags = xmlDocument.getElementsByTagName(CustomWorkflowSharedConstants.BEANS_TAG);
			if (beanTags != null) {
				String searchTag = CustomWorkflowSharedConstants.BEANS_TAG;
				// Get the 1st bean node from
				Node beanNode = getFirstNodeOfType(beanTags, searchTag);

				Node importNodesClone = null;

				NodeList beanChildNodes = beanNode.getChildNodes();
				searchTag = CustomWorkflowSharedConstants.IMPORT_TAG;
				importNodesClone = getFirstNodeOfType(beanChildNodes, searchTag);
				Node cloneNode = importNodesClone.cloneNode(true);
				cloneNode.getAttributes().getNamedItem(CustomWorkflowSharedConstants.RESOURCE).setTextContent(
						CustomWorkflowSharedConstants.CLASSPATH_META_INF + pluginApplicationContextPath);
				beanNode.insertBefore(cloneNode, importNodesClone);

				Source source = new DOMSource(xmlDocument);

				File batchXmlFile = new File(applicationContextFilePath);

				Result result = new StreamResult(batchXmlFile);

				Transformer xformer = null;
				try {
					xformer = TransformerFactory.newInstance().newTransformer();
					xformer.setOutputProperty(OutputKeys.INDENT, CustomWorkflowSharedConstants.YES);
					xformer.setOutputProperty(CustomWorkflowSharedConstants.XML_INDENT_AMOUNT, String.valueOf(2));

				} catch (TransformerConfigurationException e) {
					String errorMsg = CustomWorkflowSharedConstants.APPLICATION_CONTEXT_ENTRY_ERROR_MESSAGE;
					LOGGER.error(errorMsg + e.getMessage(), e);
					throw new GWTException(errorMsg, e);
				} catch (TransformerFactoryConfigurationError e) {
					String errorMsg = CustomWorkflowSharedConstants.APPLICATION_CONTEXT_ENTRY_ERROR_MESSAGE;
					LOGGER.error(errorMsg + e.getMessage(), e);
					throw new GWTException(errorMsg, e);
				}
				try {
					xformer.transform(source, result);
				} catch (TransformerException e) {
					String errorMsg = CustomWorkflowSharedConstants.APPLICATION_CONTEXT_ENTRY_ERROR_MESSAGE;
					LOGGER.error(errorMsg + e.getMessage(), e);
					throw new GWTException(errorMsg, e);
				}
				LOGGER.info("Application Context Entry made successfully.");
			}
		} catch (Exception e) {
			String errorMsg = CustomWorkflowSharedConstants.APPLICATION_CONTEXT_ENTRY_ERROR_MESSAGE;
			LOGGER.error(errorMsg + e.getMessage(), e);
			throw new GWTException(errorMsg, e);
		}
	}

	/**
	 * 
	 * @param beanChildNodes
	 * @param searchTag
	 * @return
	 */
	private Node getFirstNodeOfType(NodeList beanChildNodes, String searchTag) {
		Node importNodesClone;
		int index = 0;
		importNodesClone = beanChildNodes.item(index);
		while (!importNodesClone.getNodeName().equalsIgnoreCase(searchTag)) {
			index++;
			importNodesClone = beanChildNodes.item(index);
		}
		return importNodesClone;
	}

	@Override
	public Boolean deletePlugin(PluginDetailsDTO pluginDetailsDTO) throws GWTException {
		Boolean isPluginDeleted = false;
		BatchClassPluginService batchClassPluginService = this.getSingleBeanOfType(BatchClassPluginService.class);
		final String pluginIdentifier = pluginDetailsDTO.getIdentifier();
		final String pluginName = pluginDetailsDTO.getPluginName();
		LOGGER.info("Attempting to Delete plugin with id : " + pluginIdentifier + " name: " + pluginName);
		final long pluginId = Long.parseLong(pluginIdentifier);
		final List<BatchClassPlugin> batchClassPlugins = batchClassPluginService.getBatchClassPluginForPluginId(pluginId);

		if (batchClassPlugins == null || batchClassPlugins.isEmpty()) {
			LOGGER.info("No Batch Class is using this plugin. So delete is allowed.");
			deletePluginFromDb(pluginId);
			isPluginDeleted = true;
		} else {
			String errorMessage = CustomWorkflowSharedConstants.PLUGIN_IN_USE_MESSAGE;
			LOGGER.error(errorMessage);
			throw new GWTException(errorMessage);
		}
		if (isPluginDeleted) {
			LOGGER.info(pluginName + " plugin deleted successfully.");

		} else {
			LOGGER.info(pluginName + " plugin NOT deleted.");
		}

		return isPluginDeleted;
	}

	private void deletePluginFromDb(long pluginId) {
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);
		Plugin plugin = pluginService.getPluginPropertiesForPluginId(pluginId);
		LOGGER.info("Deleting Plugin with id : " + pluginId);
		pluginService.removePluginAndReferences(plugin, true);

	}
}
