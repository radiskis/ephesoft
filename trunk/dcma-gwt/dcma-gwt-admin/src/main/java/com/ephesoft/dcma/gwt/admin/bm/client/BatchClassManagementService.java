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

package com.ephesoft.dcma.gwt.admin.bm.client;

import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.gwt.core.client.DCMARemoteService;
import com.ephesoft.dcma.gwt.core.shared.BatchClassDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchFolderListDTO;
import com.ephesoft.dcma.gwt.core.shared.DocumentTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.ImportBatchClassSuperConfig;
import com.ephesoft.dcma.gwt.core.shared.ImportBatchClassUserOptionDTO;
import com.ephesoft.dcma.gwt.core.shared.KVExtractionDTO;
import com.ephesoft.dcma.gwt.core.shared.OutputDataCarrierDTO;
import com.ephesoft.dcma.gwt.core.shared.RoleDTO;
import com.ephesoft.dcma.gwt.core.shared.TableInfoDTO;
import com.ephesoft.dcma.gwt.core.shared.TestTableResultDTO;
import com.ephesoft.dcma.gwt.core.shared.exception.GWTException;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("bmService")
public interface BatchClassManagementService extends DCMARemoteService {

	List<BatchClassDTO> getAllBatchClasses();

	BatchClassDTO updateBatchClass(BatchClassDTO batchClassDTO);

	BatchClassDTO getBatchClass(String batchClassIdentifier);

	void learnFileForBatchClass(String batchClassID) throws Exception;

	void sampleGeneration(List<String> batchClassIDList);

	Map<String, List<String>> getAllTables(String driverName, String url, String userName, String password) throws Exception;

	Map<String, String> getAllColumnsForTable(String driverName, String url, String userName, String password, String tableName)
			throws Exception;

	Map<String, String> getDocumentLevelFields(String documentName, String batchClassId);

	void learnDataBase(final String batchClassId, final boolean createIndex) throws Exception;

	void copyBatchClass(BatchClassDTO batchClassDTO) throws Exception;

	BatchFolderListDTO getBatchFolderList();

	List<BatchClassDTO> getBatchClasses(int firstResult, int maxResults, Order order);

	int countAllBatchClassesExcludeDeleted();

	void createUncFolder(String path) throws GWTException;

	List<String> getProjectFilesForDocumentType(String batchClassIdentifier, String documentTypeName) throws GWTException;

	List<OutputDataCarrierDTO> testKVExtraction(BatchClassDTO batchClassDTO, KVExtractionDTO kvExtractionDTO) throws GWTException;

	List<RoleDTO> getAllRoles();

	List<String> getAllBarcodeTypes();

	BatchClassDTO deleteBatchClass(BatchClassDTO batchClassDTO);

	String matchBaseFolder(String uncFolder);

	void copyDocument(DocumentTypeDTO documentTypeDTO) throws Exception;

	String getAdvancedKVImageUploadPath(String batchClassId, String imageName);

	public List<TestTableResultDTO> testTablePattern(BatchClassDTO batchClassDTO, TableInfoDTO tableInfoDTO) throws GWTException;

	ImportBatchClassSuperConfig getImportBatchClassUIConfig(String workflowName, String zipSourcePath);

	boolean importBatchClass(ImportBatchClassUserOptionDTO userOptions) throws GWTException;

	List<BatchClassDTO> getAllBatchClassesIncludingDeleted();

	List<String> getAllPrimaryKeysForTable(String driverName, String url, String userName, String password, String table,
			String tableType) throws Exception;

	void deleteAttachedFolders(final String zipFileName);

	String getBatchClassRowCount() throws GWTException;

}
