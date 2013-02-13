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

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ephesoft.dcma.cmis.AlfrescoCMISOAuth;
import com.ephesoft.dcma.cmis.CMISExporter;
import com.ephesoft.dcma.cmis.CMISProperties;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.annotation.PostProcess;
import com.ephesoft.dcma.core.annotation.PreProcess;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.util.BackUpFileService;

/**
 * This service is responsible for uploading all the output files to the repository folder. This will reads the batch.xml file. It
 * finds the names of multi page tif and pdf files from the batch.xml.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.cmis.service.CMISExportService
 */
public class CMISExportServiceImpl implements CMISExportService, ICommonConstants {

	/**
	 * Logger instance for logging.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CMISExportServiceImpl.class);

	/**
	 * An instance of {@link CMISExporter}.
	 */
	@Autowired
	private CMISExporter cmisExporter;

	/**
	 * Instance of {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * To get the xml file before start of processing.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 */
	@PreProcess
	public void preProcess(final BatchInstanceID batchInstanceID, String pluginWorkflow) {
		Assert.notNull(batchInstanceID);
		final String batchInstanceIdentifier = batchInstanceID.getID();
		BackUpFileService.backUpBatch(batchInstanceIdentifier, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));
	}

	/**
	 * To get the xml file after processing has finished.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 */
	@PostProcess
	public void postProcess(final BatchInstanceID batchInstanceID, String pluginWorkflow) {
		final String batchInstanceIdentifier = batchInstanceID.getID();
		BackUpFileService.backUpBatch(batchInstanceIdentifier, pluginWorkflow, batchInstanceService
				.getSystemFolderForBatchInstanceId(batchInstanceIdentifier));
	}

	/**
	 * This method performs the function of exporting the contents.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 * @throws DCMAException if any exception occurs during export.
	 */
	@Override
	public void exportContent(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException {
		try {
			cmisExporter.exportFiles(batchInstanceID.getID());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new DCMAException(e.getMessage(), e);
		}
	}

	/**
	 * This method is used to delete the document.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 * @throws DCMAException if any exception occurs during deletion.
	 */
	@Override
	public void deleteDocument(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException {
		try {
			cmisExporter.deleteDocFromRepository(batchInstanceID.getID());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new DCMAException(e.getMessage(), e);
		}
	}

	/**
	 * This method tests the batch connection to the repository server.
	 * 
	 * @param pluginPropertyValues {@link Map<String, String>}
	 * @return map {@link Map<String, String>}
	 * @throws DCMAException {@link DCMAException}
	 */
	@Override
	public Map<String, String> cmisConnectionTest(final Map<String, String> pluginPropertyValues) throws DCMAException {
		try {
			return cmisExporter.cmisConnectionTest(pluginPropertyValues);
		} catch (DCMAApplicationException e) {
			LOGGER.error("Error occured while establishing cmis repository connection. " + e.getMessage(), e);
			throw new DCMAException(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error("Error occured while establishing cmis repository connection. " + e.getMessage(), e);
			throw new DCMAException(e.getMessage(), e);
		}
	}

	/**
	 * This method to get the CMIS authentication URL.
	 * 
	 * @param pluginPropertyValues {@link Map<String, String>} plugin properties of the batch class used for connection.
	 */
	@Override
	public String getAuthenticationURL(final Map<String, String> pluginPropertyValues) {
		AlfrescoCMISOAuth alfrescoCMISOAuth = new AlfrescoCMISOAuth();
		String clientKey = pluginPropertyValues.get(CMISProperties.CMIS_CLIENT_KEY.getPropertyKey());
		String secretKey = pluginPropertyValues.get(CMISProperties.CMIS_SECRET_KEY.getPropertyKey());
		String redirectURL = pluginPropertyValues.get(CMISProperties.CMIS_REDIRECT_URL.getPropertyKey());
		return alfrescoCMISOAuth.getAuthenticationURL(clientKey, secretKey, redirectURL);
	}

	/**
	 * This method to get the tokens map.
	 * 
	 * @return map {@link Map<String, String>}
	 * @param pluginPropertyValues {@link Map<String, String>} plugin properties of the batch class used for connection.
	 * @throws DCMAException {@link DCMAException} If not able to connect to repository server.
	 */
	@Override
	public Map<String, String> getTokensMap(final Map<String, String> pluginPropertyValues) throws DCMAException {
		AlfrescoCMISOAuth alfrescoCMISOAuth = new AlfrescoCMISOAuth();
		String clientKey = pluginPropertyValues.get(CMISProperties.CMIS_CLIENT_KEY.getPropertyKey());
		String secretKey = pluginPropertyValues.get(CMISProperties.CMIS_SECRET_KEY.getPropertyKey());
		String redirectURL = pluginPropertyValues.get(CMISProperties.CMIS_REDIRECT_URL.getPropertyKey());
		try {
			return alfrescoCMISOAuth.getTokenMap(clientKey, secretKey, redirectURL);
		} catch (DCMAApplicationException e) {
			throw new DCMAException(e.getMessage(), e);
		}
	}
}
