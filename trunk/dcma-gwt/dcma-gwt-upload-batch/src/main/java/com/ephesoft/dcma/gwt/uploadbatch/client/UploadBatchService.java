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

package com.ephesoft.dcma.gwt.uploadbatch.client;

import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.gwt.core.client.DCMARemoteService;
import com.ephesoft.dcma.gwt.core.shared.BatchClassCloudConfigDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassFieldDTO;
import com.ephesoft.dcma.gwt.core.shared.exception.GWTException;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * This service is responsible for handling the batch upload tasks.
 * 
 * @author Ephesoft
 * @see com.ephesoft.dcma.gwt.uploadbatch.server.UploadBatchServiceImpl
 */
@RemoteServiceRelativePath("uploadBatchService")
public interface UploadBatchService extends DCMARemoteService {

	/**
	 * API to finish Batch.
	 * 
	 * @param currentBatchUploadFolder {@link String}
	 * @param batchClassName {@link String}
	 * @return {@link String}
	 * @throws GWTException
	 */
	String finishBatch(String currentBatchUploadFolder, String batchClassName) throws GWTException;

	/**
	 * API to get Batch Class description along with the identifier.
	 * 
	 * @return Map<{@link String},{@link String}>
	 */
	Map<String, String> getBatchClassName();

	/**
	 * API to get BatchClassFieldDTO By BatchClass Identifier.
	 * 
	 * @param identifier {@link String}
	 * @return {@link List}<{@link BatchClassFieldDTO}>
	 */
	List<BatchClassFieldDTO> getBatchClassFieldDTOByBatchClassIdentifier(String identifier);

	/**
	 * API to serialize BatchClass Field for the given BatchClassFieldDTO values to the given folder name.
	 * 
	 * @param folderName {@link String}
	 * @param values {@link List}<{@link BatchClassFieldDTO}>
	 * @throws GWTException
	 */
	void serializeBatchClassField(String folderName, List<BatchClassFieldDTO> values) throws GWTException;

	/**
	 * API to get Rows Count.
	 * 
	 * @return int
	 */
	int getRowsCount();

	/**
	 * API to reset Current Batch Upload Folder.
	 * 
	 * @param folderName {@link String}
	 */
	void resetCurrentBatchUploadFolder(String folderName);

	/**
	 * API to delete Files By Name.
	 * 
	 * @param folderName {@link String}
	 * @param fileNames List<{@link String}>
	 * @return List<{@link String}>
	 */
	List<String> deleteFilesByName(String folderName, List<String> fileNames);

	/**
	 * API to get Current Batch Folder Name.
	 * 
	 * @return {@link String}
	 */
	String getCurrentBatchFolderName();

	/**
	 * The <code>getBatchClassImageLimit</code> method is used to get batch class
	 * instance image limit.
	 * 
	 * @return Map<{@link String},{@link BatchClassCloudConfigDTO}>.
	 */
	Map<String, BatchClassCloudConfigDTO> getBatchClassImageLimit();
	
	/**
	 * The <code>getFileSizeLimit</code> method is used for getting upload
	 * file limit.
	 * 
	 * @return {@link Long} file upload size in kb
	 */
	Long getFileSizeLimit();
}
