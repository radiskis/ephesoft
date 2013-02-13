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

package com.ephesoft.dcma.batch.service;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchInstance;

/**
 * Service to import the batch class.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.batch.service.ImportBatchServiceImpl
 */
public interface ImportBatchService {

	/**
	 * Method to import the batch class as specified with the options XML.
	 * 
	 * @param optionXML {@link ImportBatchClassOptions}
	 * @param isDeployed boolean
	 * @param isFromWebService boolean
	 * @param gropsToAssign {@link Set<{@link String}>}
	 * @return {@link Map<{@link Boolean}, {@link String}>}
	 */
	Map<Boolean, String> importBatchClass(ImportBatchClassOptions optionXML, boolean isDeployed, boolean isFromWebService,
			Set<String> gropsToAssign);

	/**
	 * Method to validate the user provided option XML while calling from Web Service.
	 * 
	 * @param option {@link ImportBatchClassOptions}
	 * @return {@link Map<{@link Boolean}, {@link String}>}
	 */
	Map<Boolean, String> validateInputXML(ImportBatchClassOptions option);

	/**
	 * Method to determine if the batch class being imported is equal to the batch class of the workflow name specified by user.
	 * 
	 * @param importBatchClass {@link BatchClass}
	 * @param userInputWorkflowName {@link String}
	 * @return  boolean
	 */
	boolean isImportWorkflowEqualDeployedWorkflow(BatchClass importBatchClass, String userInputWorkflowName);

	/**
	 * Method to act as utility for Restart Batch API.
	 * 
	 * @param properties {@link Properties}
	 * @param batchInstance {@link BatchInstance}
	 * @param moduleName {@link String}
	 * @param isZipSwitchOn boolean
	 * @throws DCMAApplicationException
	 */
	void updateBatchFolders(Properties properties, BatchInstance batchInstance, String moduleName, boolean isZipSwitchOn)
			throws DCMAApplicationException;

	/**
	 * Method to remove the batch folders and .SER files for batch instances.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @return boolean
	 * 
	 */
	boolean removeFolders(BatchInstance batchInstance);
}
