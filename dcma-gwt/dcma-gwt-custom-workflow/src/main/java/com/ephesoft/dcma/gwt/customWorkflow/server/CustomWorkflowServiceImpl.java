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

package com.ephesoft.dcma.gwt.customWorkflow.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.NodeList;

import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.core.common.PluginJpdlCreationInfo;
import com.ephesoft.dcma.da.domain.Dependency;
import com.ephesoft.dcma.da.domain.Plugin;
import com.ephesoft.dcma.da.domain.PluginConfig;
import com.ephesoft.dcma.da.domain.PluginConfigSampleValue;
import com.ephesoft.dcma.da.service.PluginConfigSampleValueService;
import com.ephesoft.dcma.da.service.PluginConfigService;
import com.ephesoft.dcma.da.service.PluginService;
import com.ephesoft.dcma.gwt.core.server.BatchClassUtil;
import com.ephesoft.dcma.gwt.core.server.DCMARemoteServiceServlet;
import com.ephesoft.dcma.gwt.core.shared.PluginConfigXmlDTO;
import com.ephesoft.dcma.gwt.core.shared.PluginDependencyXmlDTO;
import com.ephesoft.dcma.gwt.core.shared.PluginDetailsDTO;
import com.ephesoft.dcma.gwt.core.shared.PluginXmlDTO;
import com.ephesoft.dcma.gwt.core.shared.exception.GWTException;
import com.ephesoft.dcma.gwt.customWorkflow.client.CustomWorkflowService;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.XMLUtil;
import com.ephesoft.dcma.workflow.service.PluginJpdlCreationService;
import com.ephesoft.dcma.workflow.service.WorflowDeploymentService;

public class CustomWorkflowServiceImpl extends DCMARemoteServiceServlet implements CustomWorkflowService {

	private static final String META_INF_DCMA_WORKFLOWS = "META-INF\\dcma-workflows";
	private static final String PLUGINS = "plugins";
	private static final String LIB = "lib";
	private static final String DCMA_HOME = "DCMA_HOME";
	private static final String WEB_INF = "WEB-INF";
	private static final String OR = "/";
	private static final String AND = ",";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String VERSION = "1.0.0.0";
	String pluginExpr = "/plugin";
	String jarNameExpr = "jar-name";
	String pluginDescExpr = "plugin-desc";
	String pluginWorkflowNameExpr = "plugin-workflow-name";
	String pluginNameExpr = "plugin-name";
	String serviceNameExpr = "plugin-service-instance";
	String methodNameExpr = "method-name";
	String isScriptPluginExpr = "is-scripting";
	String scriptFileNameExpr = "script-name";
	String backUpFileNameExpr = "back-up-file-name";
	String pluginPropertyExpr = "plugin-properties/plugin-property";
	String pluginPropertyNameExpr = "name";
	String pluginPropertyTypeExpr = "type";
	String pluginPropertyDescExpr = "description";
	String pluginPropertySampleValuesExpr = "sample-values";
	String pluginPropertySampleValueExpr = "sample-value";
	String pluginPropertyIsMandetoryExpr = "is-mendatory";
	String pluginPropertyIsMultiValuesExpr = "is-multivalue";
	String dependenciesListDependency = "dependencies/dependency";
	String pluginDependencyType = "type-of-dependency";
	String pluginDependencyValue = "dependency-value";
	String JAR_PATH = "com.ephesoft.dcma.core.service.DBScriptExecuter";

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomWorkflowServiceImpl.class);

	@Override
	public Boolean addNewPlugin(String pluginName, String xmlSourcePath, String jarSourcePath) throws GWTException {

		LOGGER.info("Saving the new plugin with following details:\n Plugin Name:\t" + pluginName + "\n Plugin Xml Path:\t"
				+ xmlSourcePath + "\n Plugin Jar path:\t" + jarSourcePath);
		PluginXmlDTO pluginXmlDTO = new PluginXmlDTO();
		boolean pluginAdded = false;
		// Parse the data from xmlSourcePath file
		XPathFactory xFactory = new org.apache.xpath.jaxp.XPathFactoryImpl();
		XPath xpath = xFactory.newXPath();
		try {

			org.w3c.dom.Document pluginXmlDoc = null;

			try {
				pluginXmlDoc = XMLUtil.createDocumentFrom(FileUtils.getInputStreamFromZip(pluginName, xmlSourcePath));
			} catch (Exception e) {
				String errorMsg = "Invalid xml content. Please try again.";
				LOGGER.error(errorMsg + e.getMessage());
				throw new GWTException(errorMsg);
			}

			if (pluginXmlDoc != null) {

				// correct syntax
				NodeList pluginNodeList = null;
				try {
					pluginNodeList = (NodeList) xpath.evaluate(pluginExpr, pluginXmlDoc, XPathConstants.NODESET);
				} catch (Exception e) {
					String errorMsg = "Invalid xml content. A mandatory tag is missing.";
					LOGGER.error(errorMsg + e.getMessage());
					throw new GWTException(errorMsg);
				}
				if (pluginNodeList.getLength() == 1) {
					// OK

					LOGGER.info("Reading the Xml contents");
					String backUpFileName = "";
					String jarName = "";
					String methodName = "";
					String description = "";
					String pluginNAme = "";
					String workflowName = "";
					String scriptFileName = "";
					String serviceName = "";
					boolean isScriptingPlugin = false;
					try {
						backUpFileName = (String) xpath.evaluate(backUpFileNameExpr, pluginNodeList.item(0), XPathConstants.STRING);
						jarName = (String) xpath.evaluate(jarNameExpr, pluginNodeList.item(0), XPathConstants.STRING);
						methodName = (String) xpath.evaluate(methodNameExpr, pluginNodeList.item(0), XPathConstants.STRING);
						description = (String) xpath.evaluate(pluginDescExpr, pluginNodeList.item(0), XPathConstants.STRING);
						pluginNAme = (String) xpath.evaluate(pluginNameExpr, pluginNodeList.item(0), XPathConstants.STRING);
						workflowName = (String) xpath.evaluate(pluginWorkflowNameExpr, pluginNodeList.item(0), XPathConstants.STRING);
						scriptFileName = (String) xpath.evaluate(scriptFileNameExpr, pluginNodeList.item(0), XPathConstants.STRING);
						serviceName = (String) xpath.evaluate(serviceNameExpr, pluginNodeList.item(0), XPathConstants.STRING);
						isScriptingPlugin = (Boolean) xpath.evaluate(isScriptPluginExpr, pluginNodeList.item(0),
								XPathConstants.BOOLEAN);
					} catch (Exception e) {
						String errorMsg = "Error in xml content. A mandatory tag is missing.";
						LOGGER.error(errorMsg + e.getMessage());
						throw new GWTException(errorMsg);
					}

					LOGGER.info("Back Up File Name: " + backUpFileName);
					LOGGER.info("Jar Name" + jarName);
					LOGGER.info("Method Name" + methodName);
					LOGGER.info("Description: " + description);
					LOGGER.info("Name: " + pluginNAme);
					LOGGER.info("Workflow Name" + workflowName);
					LOGGER.info("Script file Name" + scriptFileName);
					LOGGER.info("Service Name" + serviceName);
					LOGGER.info("Is scripting Plugin:" + isScriptingPlugin);
					if (!backUpFileName.isEmpty() && !jarName.isEmpty() && !methodName.isEmpty() && !description.isEmpty()
							&& !pluginName.isEmpty() && !workflowName.isEmpty() && !serviceName.isEmpty()) {

						if (isScriptingPlugin && scriptFileName.isEmpty()) {
							String errorMsg = "Error in xml content. A mandatory field is missing.";
							LOGGER.error(errorMsg);
							throw new GWTException(errorMsg);
						}
						pluginXmlDTO.setBackUpFileName(backUpFileName);
						pluginXmlDTO.setJarName(jarName);
						pluginXmlDTO.setMethodName(methodName);
						pluginXmlDTO.setPluginDesc(description);
						pluginXmlDTO.setPluginName(pluginNAme);
						pluginXmlDTO.setPluginWorkflowName(workflowName);
						pluginXmlDTO.setScriptFileName(scriptFileName);
						pluginXmlDTO.setServiceName(serviceName);
						pluginXmlDTO.setIsScriptPlugin(isScriptingPlugin);

						extractPluginConfigs(pluginXmlDTO, xpath, pluginNodeList);

						extractPluginDependenciesFromXml(pluginXmlDTO, xpath, pluginNodeList);

						saveNewPluginToDB(pluginXmlDTO);

						createAndDeployPluginJPDL(pluginXmlDTO);

						copyJarToLib(pluginName, jarSourcePath);
						pluginAdded = true;
						LOGGER.info("Plugin added successfully.");
					} else {
						String errorMsg = "Invalid xml content. A mandatory tag is missing.";
						LOGGER.error(errorMsg);
						throw new GWTException(errorMsg);
					}
				} else {
					String errorMsg = "Invalid xml content";
					LOGGER.error(errorMsg);
					throw new GWTException(errorMsg);
				}
			}

		} catch (GWTException e) {
			pluginAdded = false;
			String errorMsg = e.getMessage();
			LOGGER.error(errorMsg);
			throw new GWTException(errorMsg);
		}
		
		return pluginAdded;
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
			pluginDependenciesNode = (NodeList) xpath.evaluate(dependenciesListDependency, pluginNodeList.item(0),
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			String errorMsg = "Invalid xml content. A mandatory field is missing.";
			LOGGER.error(errorMsg);
			throw new GWTException(errorMsg);
		}
		LOGGER.info("Extracting Dependencies from xml:");

		List<PluginDependencyXmlDTO> pluginDependencyXmlDTOs = new ArrayList<PluginDependencyXmlDTO>(0);
		int numberOfDependencies = pluginDependenciesNode.getLength();
		LOGGER.info(numberOfDependencies + " dependencies found");
		for (int index = 0; index < numberOfDependencies; index++) {
			PluginDependencyXmlDTO pluginDependencyXmlDTO = new PluginDependencyXmlDTO();
			LOGGER.info("Plugin Dependency " + index + ":");
			String dependencyType;
			String dependencyValue;
			try {
				dependencyType = (String) xpath.evaluate(pluginDependencyType, pluginDependenciesNode.item(index),
						XPathConstants.STRING);
				dependencyValue = (String) xpath.evaluate(pluginDependencyValue, pluginDependenciesNode.item(index),
						XPathConstants.STRING);
			} catch (Exception e) {
				String errorMsg = "Error in xml content. A mandatory field is missing.";
				LOGGER.error(errorMsg);
				throw new GWTException(errorMsg);
			}

			if (!dependencyType.isEmpty() && !dependencyValue.isEmpty()) {
				LOGGER.info("Type: " + dependencyType);
				LOGGER.info("Value: " + dependencyValue);
				pluginDependencyXmlDTO.setPluginDependencyType(dependencyType);

				pluginDependencyXmlDTO.setPluginDependencyValue(dependencyValue);

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
			NodeList pluginPropertyNode = (NodeList) xpath
					.evaluate(pluginPropertyExpr, pluginNodeList.item(0), XPathConstants.NODESET);
			LOGGER.info("Extracting plugin Configs from the xml");
			List<PluginConfigXmlDTO> pluginConfigXmlDTOs = new ArrayList<PluginConfigXmlDTO>(0);
			int numberOfPluginConfigs = pluginPropertyNode.getLength();

			LOGGER.info(numberOfPluginConfigs + " plugin configs found: ");
			for (int index = 0; index < numberOfPluginConfigs; index++) {
				LOGGER.info("Plugin config " + index + ": ");
				boolean isMandetory;
				boolean isMultiValue;
				String configName = "";
				String propertyType = "";
				String propertyDescription = "";

				isMandetory = (Boolean) xpath.evaluate(pluginPropertyIsMandetoryExpr, pluginPropertyNode.item(index),
						XPathConstants.BOOLEAN);
				isMultiValue = (Boolean) xpath.evaluate(pluginPropertyIsMultiValuesExpr, pluginPropertyNode.item(index),
						XPathConstants.BOOLEAN);
				configName = (String) xpath.evaluate(pluginPropertyNameExpr, pluginPropertyNode.item(index), XPathConstants.STRING);
				propertyType = (String) xpath.evaluate(pluginPropertyTypeExpr, pluginPropertyNode.item(index), XPathConstants.STRING);
				propertyDescription = (String) xpath.evaluate(pluginPropertyDescExpr, pluginPropertyNode.item(index),
						XPathConstants.STRING);

				LOGGER.info("Extracting values for config: " + index);
				LOGGER.info("Is Mandetory" + isMandetory);
				LOGGER.info("Is Multivalue" + isMultiValue);
				LOGGER.info("Config Name" + configName);
				LOGGER.info("Property Type" + propertyType);
				LOGGER.info("Property Description" + propertyDescription);
				if (!configName.isEmpty() && !propertyType.isEmpty() && !propertyDescription.isEmpty()) {
					PluginConfigXmlDTO pluginConfigXmlDTO = new PluginConfigXmlDTO();
					pluginConfigXmlDTO.setPluginPropertyIsMandetory(isMandetory);
					pluginConfigXmlDTO.setPluginPropertyIsMultiValues(isMultiValue);
					pluginConfigXmlDTO.setPluginPropertyName(configName);
					pluginConfigXmlDTO.setPluginPropertyType(propertyType);
					pluginConfigXmlDTO.setPluginPropertyDesc(propertyDescription);
					NodeList sampleValuesNode = (NodeList) xpath.evaluate(pluginPropertySampleValuesExpr, pluginPropertyNode
							.item(index), XPathConstants.NODESET);
					List<String> sampleValuesList = new ArrayList<String>(0);
					LOGGER.info("Extracting sample values: ");
					int numberOfSampleValues = sampleValuesNode.getLength();

					LOGGER.info(numberOfSampleValues + " sample values found");
					for (int sampleValueIndex = 0; sampleValueIndex < numberOfSampleValues; sampleValueIndex++) {

						String sampleValue = (String) xpath.evaluate(pluginPropertySampleValueExpr, sampleValuesNode
								.item(sampleValueIndex), XPathConstants.STRING);
						LOGGER.info("Sample value " + sampleValueIndex + " :" + sampleValue);
						sampleValuesList.add(sampleValue);
					}
					pluginConfigXmlDTO.setPluginPropertySampleValues(sampleValuesList);
					pluginConfigXmlDTOs.add(pluginConfigXmlDTO);
				} else {
					String errorMsg = "Invalid xml content. A mandatory tag is missing.";
					LOGGER.error(errorMsg);
					throw new GWTException(errorMsg);
				}
			}
			pluginXmlDTO.setConfigXmlDTOs(pluginConfigXmlDTOs);
		} catch (XPathExpressionException e) {
			String errorMsg = "Invalid xml content. A mandatory tag is missing.";
			LOGGER.error(errorMsg);
			throw new GWTException(errorMsg);
		}
	}

	/**
	 * @param jarSourcePath
	 * @param jarSourcePath2
	 * @throws
	 */
	private void copyJarToLib(String pluginName, String jarSourcePath) throws GWTException {

		InputStream inputStream;
		try {
			inputStream = FileUtils.getInputStreamFromZip(pluginName, jarSourcePath);
		} catch (Exception e) {
			String errorMsg = " Jar file is either not present or corrupted.";
			LOGGER.error(errorMsg);
			throw new GWTException(errorMsg);
		}
		FileOutputStream fileOutputStream = null;

		LOGGER.info("JAR file source path: " + jarSourcePath);
		StringBuffer destFilePath = new StringBuffer(System.getenv(DCMA_HOME));
		String destinationFilePathString = destFilePath.toString();
		LOGGER.info("DCMA HOME: " + destinationFilePathString);
		// File srcFile = new File(jarSourcePath);
		destFilePath.append(File.separator);
		destFilePath.append(WEB_INF);
		destFilePath.append(File.separator);
		destFilePath.append(LIB);
		destFilePath.append(File.separator);
		destFilePath.append(jarSourcePath);
		destinationFilePathString = destFilePath.toString();
		LOGGER.info("Copying the JAR File in source path: " + jarSourcePath + " to " + destinationFilePathString);
		File destFile = new File(destinationFilePathString);
		try {
			fileOutputStream = new FileOutputStream(destFile);
		} catch (FileNotFoundException e) {
			String errorMsg = " The jar file could not be copied.";
			LOGGER.error(errorMsg);
			throw new GWTException(errorMsg);
		}

		try {
			byte[] buf = new byte[1024];
			int len;
			while ((len = inputStream.read(buf)) > 0) {
				fileOutputStream.write(buf, 0, len);
			}
			inputStream.close();
			fileOutputStream.close();
		} catch (FileNotFoundException fileNotFoundException) {
			String errorMsg = fileNotFoundException.getMessage() + " File not found";
			LOGGER.error(errorMsg);
			throw new GWTException(errorMsg);
		} catch (IOException ioException) {
			String errorMsg = ioException.getMessage() + " Error copying file";
			LOGGER.error(errorMsg);
			throw new GWTException(errorMsg);
		}

		// FileUtils.copyFile(srcFile, destFile);
		// LOGGER.info("Deleting the temporary source file from " + jarSourcePath);
		// FileUtils.deleteDirectoryAndContents(srcFile.getParent());
	}

	/**
	 * @param pluginXmlDTO
	 * @throws IOException
	 */
	private void createAndDeployPluginJPDL(PluginXmlDTO pluginXmlDTO) throws GWTException {
		PluginJpdlCreationService pluginJpdlCreationService = this.getSingleBeanOfType(PluginJpdlCreationService.class);
		WorflowDeploymentService worflowDeploymentService = this.getSingleBeanOfType(WorflowDeploymentService.class);

		ClassPathResource classPathResource = new ClassPathResource(META_INF_DCMA_WORKFLOWS);
		File workflowDirectory = null;
		try {
			workflowDirectory = classPathResource.getFile();
		} catch (IOException e) {
			String errorMsg = "Error saving new plugin.";
			LOGGER.error(errorMsg);
			throw new GWTException();
		}
		LOGGER.info("Workflow directory: " + workflowDirectory.getAbsolutePath());
		if (!workflowDirectory.exists()) {

			workflowDirectory.mkdir();
		}
		File newPluginDirectory = new File(workflowDirectory.getAbsolutePath() + File.separator + PLUGINS + File.separator
				+ pluginXmlDTO.getPluginWorkflowName());
		LOGGER.info("Creating the plugins JPDL directory + " + newPluginDirectory.getAbsolutePath() + " if not existing");
		newPluginDirectory.mkdirs();

		StringBuffer pluginJpdlPath = new StringBuffer();
		pluginJpdlPath.append(File.separator);
		pluginJpdlPath.append(classPathResource.getPath());
		pluginJpdlPath.append(File.separator);
		pluginJpdlPath.append(PLUGINS);
		pluginJpdlPath.append(File.separator);
		pluginJpdlPath.append(pluginXmlDTO.getPluginWorkflowName());
		// pluginJpdlPath.append(File.separator);

		List<PluginJpdlCreationInfo> pluginJpdlCreationInfos = new ArrayList<PluginJpdlCreationInfo>(0);

		LOGGER.info("Converting XML info into required form for creating JPDL");
		PluginJpdlCreationInfo pluginJpdlCreationInfo = new PluginJpdlCreationInfo(pluginXmlDTO.getPluginDesc(), pluginXmlDTO
				.getIsScriptPlugin(), pluginXmlDTO.getServiceName(), pluginXmlDTO.getMethodName());

		pluginJpdlCreationInfos.add(pluginJpdlCreationInfo);

		LOGGER.info("Creating JPDL");
		String jpdlFilePath = pluginJpdlCreationService.writeJPDL(newPluginDirectory.getPath(), pluginXmlDTO.getPluginWorkflowName(),
				pluginJpdlCreationInfos);
		LOGGER.info("JPDL created at " + jpdlFilePath);
		pluginJpdlPath.append(jpdlFilePath);

		LOGGER.info("Deploying JPDL");
		worflowDeploymentService.deploy(pluginJpdlPath.toString());
	}

	private void saveNewPluginToDB(PluginXmlDTO pluginXmlDTO) throws GWTException {
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);
		PluginConfigService pluginConfigService = this.getSingleBeanOfType(PluginConfigService.class);
		PluginConfigSampleValueService pluginConfigSampleValueService = this.getSingleBeanOfType(PluginConfigSampleValueService.class);

		LOGGER.info("Preparing plugin object from the XMl content");
		Plugin newPlugin = new Plugin();
		try {
			newPlugin = preparePluginObject(pluginXmlDTO);
		} catch (Exception e) {
			String errorMsg = " Data in xml is invalid or corrupted";
			LOGGER.error(errorMsg);
			throw new GWTException(errorMsg);
		}

		LOGGER.info("Preparing plugin config object from the XMl content");
		List<PluginConfig> pluginConfigs = preparePluginConfigObjects(pluginXmlDTO);
		List<PluginConfigSampleValue> pluginConfigSampleValues = new ArrayList<PluginConfigSampleValue>(0);

		// Create new Plugin
		LOGGER.info("Creating new plugin: " + newPlugin.getPluginName());
		Plugin pluginCheck = pluginService.getPluginPropertiesForPluginName(newPlugin.getPluginName());

		if (pluginCheck == null) {
			pluginService.createNewPlugin(newPlugin);
			for (PluginConfig newPluginConfig : pluginConfigs) {
				// Associate new plugin with new Plugin configs
				newPluginConfig.setPlugin(newPlugin);

				PluginConfig pluginConfigCheck = pluginConfigService.getPluginConfigByName(newPluginConfig.getName());
				if (pluginConfigCheck == null) {
					// save new Plugin configs
					LOGGER.info("Creating new plugin config: " + newPluginConfig.getName());
					pluginConfigService.createNewPluginConfig(newPluginConfig);
					// get PCSV for PCName
					LOGGER.info("Preparing plugin config sample value");
					pluginConfigSampleValues = getPluginConfigSampleValueForPluginConfigName(pluginXmlDTO, newPluginConfig.getName());
					for (PluginConfigSampleValue pluginConfigSampleValue : pluginConfigSampleValues) {
						pluginConfigSampleValue.setPluginConfig(newPluginConfig);
						LOGGER.info("Creating new plugin config sample value: " + pluginConfigSampleValue.getSampleValue());
						pluginConfigSampleValueService.createNewPluginConfigSampleValue(pluginConfigSampleValue);
					}
				} else {
					String errorMsg = "Error saving new plugin config. A plugin config with same name already exists.";
					LOGGER.error(errorMsg);
					throw new GWTException(errorMsg);
				}
			}
		} else {
			String errorMsg = " A plugin with same name already exists.";
			LOGGER.error(errorMsg);
			throw new GWTException(errorMsg);
		}

	}

	private List<PluginConfigSampleValue> getPluginConfigSampleValueForPluginConfigName(PluginXmlDTO pluginXmlDTO,
			String pluginConfigName) {
		List<PluginConfigSampleValue> pluginConfigSampleValues = new ArrayList<PluginConfigSampleValue>(0);

		for (PluginConfigXmlDTO pluginConfigXmlDTO : pluginXmlDTO.getConfigXmlDTOs()) {
			if (pluginConfigXmlDTO.getPluginPropertyName().equals(pluginConfigName)) {

				for (String SampleValues : pluginConfigXmlDTO.getPluginPropertySampleValues()) {
					PluginConfigSampleValue configSampleValue = new PluginConfigSampleValue();
					configSampleValue.setSamplevalue(SampleValues);
					pluginConfigSampleValues.add(configSampleValue);
				}

			}
		}
		return pluginConfigSampleValues;
	}

	private List<PluginConfig> preparePluginConfigObjects(PluginXmlDTO pluginXmlDTO) {
		List<PluginConfig> pluginConfigs = new ArrayList<PluginConfig>(0);

		for (PluginConfigXmlDTO pluginConfigXmlDTO : pluginXmlDTO.getConfigXmlDTOs()) {
			PluginConfig pluginConfig = new PluginConfig();
			pluginConfig.setDescription(pluginConfigXmlDTO.getPluginPropertyDesc());
			pluginConfig.setMandatory(pluginConfigXmlDTO.getPluginPropertyIsMandetory());
			pluginConfig.setMultiValue(pluginConfigXmlDTO.getPluginPropertyIsMultiValues());
			pluginConfig.setName(pluginConfigXmlDTO.getPluginPropertyName());
			pluginConfig.setDataType(DataType.getDataType(pluginConfigXmlDTO.getPluginPropertyType()));
			pluginConfigs.add(pluginConfig);
		}
		return pluginConfigs;
	}

	private Plugin preparePluginObject(PluginXmlDTO pluginXmlDTO) throws GWTException {
		Plugin plugin = new Plugin();

		plugin.setPluginName(pluginXmlDTO.getPluginName());
		plugin.setWorkflowName(pluginXmlDTO.getPluginWorkflowName());
		plugin.setDescription(pluginXmlDTO.getPluginDesc());
		if (pluginXmlDTO.getIsScriptPlugin()) {
			plugin.setScriptName(pluginXmlDTO.getScriptFileName());
		}
		plugin.setVersion(VERSION);
		List<Dependency> dependenciesList = plugin.getDependencies();

		if (dependenciesList == null) {
			dependenciesList = new ArrayList<Dependency>();
		}

		for (PluginDependencyXmlDTO dependencyXmlDTO : pluginXmlDTO.getDependencyXmlDTOs()) {
			if (validateDependenciesNameList(dependencyXmlDTO.getPluginDependencyValue())) {
				Dependency dependency = new Dependency();
				dependency.setDependencyType(BatchClassUtil.getDependencyTypePropertyFromValue(dependencyXmlDTO
						.getPluginDependencyType()));
				dependency.setDependencies(getDependenciesIdentifierString(dependencyXmlDTO.getPluginDependencyValue()));
				dependency.setId(0);
				dependency.setPlugin(plugin);
				dependenciesList.add(dependency);
			} else {
				String errorMsg = "Error: " + "Invalid Dependencies name.";
				LOGGER.error(errorMsg);
				throw new GWTException(errorMsg);
			}

		}
		plugin.setDependencies(dependenciesList);
		return plugin;
	}

	private String getDependenciesIdentifierString(String dependencies) {
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);

		LOGGER.info("Converting dependencies name to ID's");
		String[] andDependencies = dependencies.split(AND);
		StringBuffer andDependenciesNameAsString = new StringBuffer();

		for (String andDependency : andDependencies) {
			if (!andDependenciesNameAsString.toString().isEmpty()) {
				andDependenciesNameAsString.append(AND);
			}
			String[] orDependencies = andDependency.split(OR);
			StringBuffer orDependenciesNameAsString = new StringBuffer();

			for (String dependencyName : orDependencies) {
				if (!orDependenciesNameAsString.toString().isEmpty()) {
					orDependenciesNameAsString.append(OR);
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
		String[] andDependencies = pluginDependencyValue.split(AND);

		for (String andDependency : andDependencies) {

			String[] orDependencies = andDependency.split(OR);

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
		List<Plugin> allPluginsNames = pluginService.getAllPluginsNames();
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

		try {
			Long pluginId = Long.valueOf(pluginIdentifier);
			Plugin plugin = pluginService.getPluginPropertiesForPluginId(pluginId);
			LOGGER.info("Merging the changes in plugin.");
			BatchClassUtil.mergePluginFromDTO(plugin, pluginDetailsDTO, pluginService);
			LOGGER.info("updating plugin");
			pluginService.mergePlugin(plugin);
			pluginDetailsDTO = BatchClassUtil.createPluginDetailsDTO(plugin, pluginService);
		} catch (NumberFormatException e) {
			LOGGER.error("Error converting number " + e.getMessage());
		}
		return pluginDetailsDTO;
	}

	@Override
	public List<PluginDetailsDTO> updateAllPluginDetailsDTOs(List<PluginDetailsDTO> allPluginDetailsDTO) {
		List<PluginDetailsDTO> allUpdatedPluginDetailsDTO = new ArrayList<PluginDetailsDTO>(0);
		LOGGER.info("Updating all plugins");
		for (PluginDetailsDTO pluginDetailsDTO : allPluginDetailsDTO) {
			if (pluginDetailsDTO.isDirty()) {
				LOGGER.info(pluginDetailsDTO.getPluginName() + " plugin will be updated");
				allUpdatedPluginDetailsDTO.add(updatePlugin(pluginDetailsDTO));
			} else {
				LOGGER.info(pluginDetailsDTO.getPluginName() + " plugin does not need update");
				allUpdatedPluginDetailsDTO.add(pluginDetailsDTO);
			}
		}
		return allUpdatedPluginDetailsDTO;
	}
}
