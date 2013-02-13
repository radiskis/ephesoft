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

package com.ephesoft.dcma.script;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.service.BatchClassPluginConfigService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.script.compiler.DynamicCodeCompiler;
import com.ephesoft.dcma.script.constant.ScriptConstants;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.XMLUtil;

/**
 * This class is used to call the scripts on the basis of plug-in name. Scripts are placed at some pre-defined location and this
 * plug-in will invoke the scripts. This plug-in will compile the scripts at run time and execute it. This service plug-in can be used
 * after any plug-in.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.script.service.ScriptServiceImpl
 */
@Component
public class ScriptExecutor {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ScriptExecutor.class);

	/**
	 * Variable for parserType.
	 */
	private String parserType;

	/**
	 * Variable for scriptSwitch.
	 */
	private String scriptSwitch;

	/**
	 * Variable for updateBatchInstFromBatch.
	 */
	private String updateBatchInstFromBatch;

	/**
	 * Getter for updateBatchInstFromBatch.
	 * 
	 * @return String
	 */
	public String getUpdateBatchInstFromBatch() {
		return updateBatchInstFromBatch;
	}

	/**
	 * Setter for updateBatchInstFromBatch.
	 * 
	 * @param updateBatchInstFromBatch String
	 */
	public void setUpdateBatchInstFromBatch(String updateBatchInstFromBatch) {
		this.updateBatchInstFromBatch = updateBatchInstFromBatch;
	}

	/**
	 * Getter for parserType.
	 * 
	 * @return String
	 */
	public String getParserType() {
		return parserType;
	}

	/**
	 * Setter for parserType.
	 * 
	 * @param parserType String
	 */
	public void setParserType(final String parserType) {
		this.parserType = parserType;
	}

	/**
	 * Getter for scriptSwitch.
	 * 
	 * @return String
	 */
	public String getScriptSwitch() {
		return scriptSwitch;
	}

	/**
	 * Setter for scriptSwitch.
	 * 
	 * @param scriptSwitch String
	 */
	public void setScriptSwitch(String scriptSwitch) {
		this.scriptSwitch = scriptSwitch;
	}

	/**
	 * Reference of BatchSchemaService.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Reference of batchInstanceService.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * Getter for batchInstanceService.
	 * 
	 * @return the batchInstanceService
	 */
	public BatchInstanceService getBatchInstanceService() {
		return batchInstanceService;
	}

	/**
	 * Setter for batchInstanceService.
	 * 
	 * @param batchInstanceService BatchInstanceService
	 */
	public void setBatchInstanceService(BatchInstanceService batchInstanceService) {
		this.batchInstanceService = batchInstanceService;
	}

	/**
	 * Instance of BatchClassPluginConfigService.
	 */
	@Autowired
	private BatchClassPluginConfigService batchClassPluginConfigService;

	/**
	 * Getter for batchClassPluginConfigService.
	 * 
	 * @return the batchClassPluginConfigService
	 */
	public BatchClassPluginConfigService getBatchClassPluginConfigService() {
		return batchClassPluginConfigService;
	}

	/**
	 * Setter for batchClassPluginConfigService.
	 * 
	 * @param batchClassPluginConfigService BatchClassPluginConfigService
	 */
	public void setBatchClassPluginConfigService(final BatchClassPluginConfigService batchClassPluginConfigService) {
		this.batchClassPluginConfigService = batchClassPluginConfigService;
	}

	/**
	 * This method will compile and execute all the scripts for input plug-in name placed at some pre defined location.
	 * 
	 * @param batchInstanceID String
	 * @param pluginScriptName String
	 * @param docIdentifier String
	 * @param scriptVariableName String
	 * @throws DCMAApplicationException Check for all the input parameters.
	 */
	public void extractFields(final String batchInstanceId, final String pluginScriptName, final String docIdentifier,
			final String scriptVariableName) throws DCMAApplicationException {
		if (!ICommonConstants.ON_CONSTANT.equalsIgnoreCase(scriptSwitch)) {
			LOGGER.info("Scripting plugin switch is OFF");
			return;
		}
		String errMsg = null;
		if (null == batchInstanceId || null == pluginScriptName) {
			errMsg = "Invalid input parameter. batchInstanceId or nameOfPluginScript is null.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}
		boolean isZipSwitchOn = batchSchemaService.isZipSwitchOn();
		LOGGER.info(" batchInstanceId : " + batchInstanceId + "  nameOfPluginScript : " + pluginScriptName);
		LOGGER.info("Zipped Batch XML switch is:" + isZipSwitchOn);
		final Batch batch = batchSchemaService.getBatch(batchInstanceId);

		if (null == batch) {
			errMsg = "Invalid batch.";
			throw new DCMAApplicationException(errMsg);
		}

		try {
			final String localFolderPath = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceId);
			if (null == localFolderPath) {
				errMsg = "localFolderPath is null.";
				throw new DCMAApplicationException(errMsg);
			}

			String mainFolderPath = null;
			final int index = localFolderPath.lastIndexOf('\\');
			final int srcIndex = localFolderPath.lastIndexOf('/');

			if (index == -1 && srcIndex == -1) {
				throw new DCMAApplicationException("In valid value of local folder.");
			}
			if (srcIndex > index) {
				mainFolderPath = localFolderPath.substring(0, srcIndex);
			} else {
				mainFolderPath = localFolderPath.substring(0, index);
			}

			if (null == mainFolderPath) {
				errMsg = "mainFolderPath is null.";
				LOGGER.error(errMsg);
				throw new DCMAApplicationException(errMsg);
			}

			final String pathToComplile = mainFolderPath + File.separator + batch.getBatchClassIdentifier() + File.separator
					+ batchSchemaService.getScriptFolderName();
			final DynamicCodeCompiler dynacode = new DynamicCodeCompiler();
			dynacode.addSourceDir(new File(pathToComplile));

			if (ScriptConstants.JDOM_PARSER_TYPE.equalsIgnoreCase(getParserType())) {
				scriptExecutionUsingIJomScript(batchInstanceId, pluginScriptName, docIdentifier, scriptVariableName, isZipSwitchOn,
						localFolderPath, dynacode);
			} else {
				scriptExecutionUsingIScript(batchInstanceId, pluginScriptName, docIdentifier, scriptVariableName, isZipSwitchOn,
						localFolderPath, dynacode);
			}
			if (ScriptConstants.ON_STRING.equalsIgnoreCase(getUpdateBatchInstFromBatch())) {
				Batch updatedBatch = batchSchemaService.getBatch(batchInstanceId);
				BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceId);
				String batchInstanceBatchName = batchInstance.getBatchName();
				String batchName = updatedBatch.getBatchName();
				if (batchName != null && batchInstanceBatchName != null && !batchInstanceBatchName.equals(batchName)) {
					File srcPath = new File(batchInstance.getUncSubfolder());
					String batchLocalFolder = batchInstanceService.getSystemFolderForBatchInstanceId(batchInstanceId);
					String batchInstancePath = batchLocalFolder + File.separator + batchInstanceId + File.separator
							+ batchInstanceBatchName;
					File destPath = new File(batchInstancePath);
					try {
						copyingFileInfomation(srcPath, destPath);
					} catch (IOException ioException) {
						LOGGER.error("Unable to copy the content of UNC folder to batch instance folder.Need to copy it manually.");
					}
					boolean isDeleted = FileUtils.deleteDirectoryAndContents(srcPath);
					if (!isDeleted) {
						LOGGER.error("Unable to delete UNC folder.Trying to delete again....");
						FileUtils.deleteDirectoryAndContents(srcPath);
					}
				}
				if (batchName != null) {
					batchInstance.setBatchName(batchName);
				}
				String batchPriority = updatedBatch.getBatchPriority();
				settingBatchPriority(batchInstance, batchPriority);
				batchInstanceService.updateBatchInstance(batchInstance);
			}
		} catch (DCMAApplicationException dcmae) {
			throw new DCMAApplicationException("Script errored out.Throwing workflow in error.Exception thrown is:"
					+ dcmae.getMessage(), dcmae);
		} catch (IllegalArgumentException illegalArgumentException) {
			throw new DCMAApplicationException("Script having invalid parser type or invalid agruments.Throwing workflow in error",
					illegalArgumentException);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Throwable throwable) {
			throw new DCMAApplicationException("Script errored out.Throwing workflow in error.Exception thrown is:"
					+ throwable.getMessage(), throwable);
		}
	}

	private void settingBatchPriority(BatchInstance batchInstance, String batchPriority) {
		if (batchPriority != null) {
			try {
				Integer batchPriorityInt = Integer.parseInt(batchPriority);
				if (batchPriorityInt != null) {
					batchInstance.setPriority(batchPriorityInt);
				}
			} catch (NumberFormatException numberFormatException) {
				LOGGER.error("Batch priority unable to parse.");
			}
		}
	}

	private void copyingFileInfomation(File srcPath, File destPath) throws IOException {
		try {
			FileUtils.copyDirectoryWithContents(srcPath, destPath);
		} catch (IOException ioException) {
			LOGGER.error("Unable to copy the content of UNC folder to batch instance folder.Trying to copy it again.");
			FileUtils.copyDirectoryWithContents(srcPath, destPath);
		}
	}

	private void scriptExecutionUsingIJomScript(final String batchInstanceId, final String pluginScriptName,
			final String docIdentifier, final String scriptVariableName, boolean isZipSwitchOn, final String localFolderPath,
			final DynamicCodeCompiler dynacode) throws DCMAApplicationException, JDOMException, IOException, FileNotFoundException {
		final IJDomScript iJDomExecutor = (IJDomScript) dynacode.newProxyInstance(IJDomScript.class, pluginScriptName);
		if (null == iJDomExecutor) {
			LOGGER.info("IJDomScript was returned as null.");
		} else {
			String batchXmlFileName = batchInstanceId + ICommonConstants.UNDERSCORE_BATCH_XML;
			String srcXmlPath = localFolderPath + File.separator + batchInstanceId + File.separator + batchXmlFileName;
			org.jdom.Document document = null;

			if (isZipSwitchOn) {
				if (FileUtils.isZipFileExists(srcXmlPath)) {
					document = XMLUtil
							.createJDOMDocumentFromInputStream(FileUtils.getInputStreamFromZip(srcXmlPath, batchXmlFileName));
				} else {
					final File batchXmlFile = new File(srcXmlPath);
					document = XMLUtil.createJDOMDocumentFromFile(batchXmlFile);
				}
			} else {
				final File batchXmlFile = new File(srcXmlPath);
				if (batchXmlFile.exists()) {
					document = XMLUtil.createJDOMDocumentFromFile(batchXmlFile);
				} else {
					document = XMLUtil
							.createJDOMDocumentFromInputStream(FileUtils.getInputStreamFromZip(srcXmlPath, batchXmlFileName));
				}
			}

			Object obj = iJDomExecutor.execute(document, scriptVariableName, docIdentifier);
			if (obj instanceof Exception) {
				Exception exception = (Exception) obj;
				throw new DCMAApplicationException("Script errored out. Throwing workflow in error.Exception thrown is:"
						+ exception.getMessage(), exception);
			}
		}
	}

	private void scriptExecutionUsingIScript(final String batchInstanceId, final String pluginScriptName, final String docIdentifier,
			final String scriptVariableName, boolean isZipSwitchOn, final String localFolderPath, final DynamicCodeCompiler dynacode)
			throws DCMAApplicationException, ParserConfigurationException, SAXException, IOException, FileNotFoundException {
		final IScripts iExecutor = (IScripts) dynacode.newProxyInstance(IScripts.class, pluginScriptName);
		if (null == iExecutor) {
			LOGGER.info("IScripts was returned as null.");
		} else {
			String batchXmlFileName = batchInstanceId + ICommonConstants.UNDERSCORE_BATCH_XML;
			String srcXmlPath = localFolderPath + File.separator + batchInstanceId + File.separator + batchXmlFileName;
			Document document = null;

			if (isZipSwitchOn) {
				if (FileUtils.isZipFileExists(srcXmlPath)) {
					document = XMLUtil.createDocumentFrom(FileUtils.getInputStreamFromZip(srcXmlPath, batchXmlFileName));
				} else {
					final File batchXmlFile = new File(srcXmlPath);
					document = XMLUtil.createDocumentFrom(batchXmlFile);
				}
			} else {
				final File batchXmlFile = new File(srcXmlPath);
				if (batchXmlFile.exists()) {
					document = XMLUtil.createDocumentFrom(batchXmlFile);
				} else {
					document = XMLUtil.createDocumentFrom(FileUtils.getInputStreamFromZip(srcXmlPath, batchXmlFileName));
				}
			}

			Object obj = iExecutor.execute(document, scriptVariableName, docIdentifier);
			if (obj instanceof Exception) {
				Exception exception = (Exception) obj;
				throw new DCMAApplicationException("Script errored out. Throwing workflow in error.Exception thrown is:"
						+ exception.getMessage(), exception);
			}
		}
	}
}
