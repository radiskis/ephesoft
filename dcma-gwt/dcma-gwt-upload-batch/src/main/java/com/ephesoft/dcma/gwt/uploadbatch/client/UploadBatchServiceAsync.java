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

import com.ephesoft.dcma.gwt.core.client.DCMARemoteServiceAsync;
import com.ephesoft.dcma.gwt.core.shared.BatchClassCloudConfigDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassFieldDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * This service is responsible for handling the batch upload tasks asynchronously.
 * 
 * @author Ephesoft
 */

public interface UploadBatchServiceAsync extends DCMARemoteServiceAsync {

	/**
	 * API to get Rows Count asynchronously.
	 * @param callback {@link AsyncCallback}< {@link Integer} >
	 */
	void getRowsCount(AsyncCallback<Integer> callback);

	/**
	 * API to finish Batch asynchronously.
	 * 
	 * @param currentBatchUploadFolder {@link String}
	 * @param batchClassName {@link String}
	 * @param callback {@link AsyncCallback}< {@link String} >
	 */
	void finishBatch(String currentBatchUploadFolder, String batchClassName, AsyncCallback<String> asyncCallback);

	/**
	 * API to get Batch Class description along with the identifier asynchronously.
	 * 
	 * @param callback {@link AsyncCallback}< Map<{@link String},{@link String}> >
	 */
	void getBatchClassName(AsyncCallback<Map<String, String>> asyncCallback);

	/**
	 * API to get BatchClassFieldDTO By BatchClass Identifier asynchronously.
	 * 
	 * @param identifier {@link String}
	 * @param callback {@link AsyncCallback}< {@link List}<{@link BatchClassFieldDTO}> >
	 */
	void getBatchClassFieldDTOByBatchClassIdentifier(String identifier, AsyncCallback<List<BatchClassFieldDTO>> asyncCallback);

	/**
	 * API to serialize BatchClass Field for the given BatchClassFieldDTO values to the given folder name asynchronously.
	 * 
	 * @param folderName {@link String}
	 * @param values {@link List}<{@link BatchClassFieldDTO}>
	 * @param callback {@link AsyncCallback}< {@link Void} >
	 */
	void serializeBatchClassField(String folderName, List<BatchClassFieldDTO> values, AsyncCallback<Void> asyncCallback);

	/**
	 * API to reset Current Batch Upload Folder asynchronously.
	 * 
	 * @param folderName {@link String}
	 * @param callback {@link AsyncCallback}< {@link Void} >
	 */
	void resetCurrentBatchUploadFolder(String folderName, AsyncCallback<Void> asyncCallback);

	/**
	 * API to delete Files By Name asynchronously.
	 * 
	 * @param folderName {@link String}
	 * @param fileNames List<{@link String}>
	 * @param callback {@link AsyncCallback}< List<{@link String}> >
	 */
	void deleteFilesByName(String folderName, List<String> fileNames, AsyncCallback<List<String>> asyncCallback);

	/**
	 * API to get Current Batch Folder Name asynchronously.
	 * 
	 * @param callback {@link AsyncCallback}< {@link String} >
	 */
	void getCurrentBatchFolderName(AsyncCallback<String> asyncCallback);
	
	
	/**
	 * The <code>getBatchClassImageLimit</code> method is used to get batch class
	 * instance image limit.
	 * 
	 * @param callback {@link AsyncCallback}< Map<{@link String},{@link BatchClassCloudConfigDTO}> >
	 */
	void getBatchClassImageLimit(AsyncCallback<Map<String, BatchClassCloudConfigDTO>> asyncCallback);
	
	/**
	 * The <code>getFileSizeLimit</code> method is used for getting upload
	 * file limit.
	 * 
	 * @param callback {@link AsyncCallback}< {@link Long} >
	 */
	void getFileSizeLimit(AsyncCallback<Long> asyncCallBack);
}
