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

package com.ephesoft.dcma.script;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.service.BatchClassPluginConfigService;
import com.ephesoft.dcma.script.compiler.DynamicCodeCompiler;
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
	 * Reference of BatchSchemaService.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Instance of BatchClassPluginConfigService.
	 */
	@Autowired
	private BatchClassPluginConfigService batchClassPluginConfigService;

	/**
	 * @return the batchClassPluginConfigService
	 */
	public BatchClassPluginConfigService getBatchClassPluginConfigService() {
		return batchClassPluginConfigService;
	}

	/**
	 * @param batchClassPluginConfigService the batchClassPluginConfigService to set
	 */
	public void setBatchClassPluginConfigService(final BatchClassPluginConfigService batchClassPluginConfigService) {
		this.batchClassPluginConfigService = batchClassPluginConfigService;
	}

	/**
	 * This method will compile and execute all the scripts for input plug-in name placed at some pre defined location.
	 * 
	 * @param batchInstanceID String
	 * @param pluginScriptName String
	 * @throws DCMAApplicationException Check for all the input parameters.
	 */
	public void extractFields(final String batchInstanceId, final String pluginScriptName, final String docIdentifier,
			final String methodName) throws DCMAApplicationException {

		String errMsg = null;
		if (null == batchInstanceId || null == pluginScriptName) {
			errMsg = "Invalid input parameter. batchInstanceId or nameOfPluginScript is null.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		LOGGER.info(" batchInstanceId : " + batchInstanceId + "  nameOfPluginScript : " + pluginScriptName);

		final Batch batch = batchSchemaService.getBatch(batchInstanceId);

		if (null == batch) {
			errMsg = "Invalid batch.";
			LOGGER.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		try {
			final String localFolderPath = batchSchemaService.getLocalFolderLocation();

			if (null == localFolderPath) {
				errMsg = "localFolderPath is null.";
				LOGGER.error(errMsg);
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
			final IScripts iExecutor = (IScripts) dynacode.newProxyInstance(IScripts.class, pluginScriptName);
			if (null == iExecutor) {
				LOGGER.info("IScripts was returned as null.");
			} else {
				final File batchXmlFile = new File(localFolderPath + File.separator + batchInstanceId + File.separator
						+ batchInstanceId + "_batch.xml");
				final Document document = XMLUtil.createDocumentFrom(batchXmlFile);
				iExecutor.execute(document, methodName, docIdentifier);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}
