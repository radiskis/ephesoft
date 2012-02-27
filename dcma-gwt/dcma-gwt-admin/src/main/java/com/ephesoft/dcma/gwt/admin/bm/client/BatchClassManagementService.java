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

	/**
	 * API to get All Batch Classes.
	 * @return List<{@link BatchClassDTO}>
	 */
	List<BatchClassDTO> getAllBatchClasses();

	/**
	 * API to update Batch Class given BatchClassDTO.
	 * @param batchClassDTO {@link BatchClassDTO}
	 * @return {@link BatchClassDTO}
	 */
	BatchClassDTO updateBatchClass(BatchClassDTO batchClassDTO);

	/**
	 * API to get Batch Class given batchClassIdentifier.
	 * @param batchClassIdentifier {@link String}
	 * @return {@link BatchClassDTO}
	 */
	BatchClassDTO getBatchClass(String batchClassIdentifier);

	/**
	 * API to learn File For Batch Class given batchClassID.
	 * @param batchClassID {@link String}
	 * @throws Exception
	 */
	void learnFileForBatchClass(String batchClassID) throws Exception;

	/**
	 * API for sample Generation given the batch Class ID's List.
	 * @param batchClassIDList List<{@link String}>
	 */
	void sampleGeneration(List<String> batchClassIDList);

	/**
	 * API to get All Tables given the DB configuration.
	 * @param driverName {@link String}
	 * @param url {@link String}
	 * @param userName {@link String}
	 * @param password {@link String}
	 * @return Map<{@link String}, List<{@link String}>>
	 * @throws Exception
	 */
	Map<String, List<String>> getAllTables(String driverName, String url, String userName, String password) throws Exception;

	/**
	 * API to get All columns of a Table given the DB configuration.
	 @param driverName {@link String}
	 * @param url {@link String}
	 * @param userName {@link String}
	 * @param password {@link String}
	 * @param tableName {@link String}
	 * @return Map<{@link String}, {@link String}>
	 * @throws Exception
	 */
	Map<String, String> getAllColumnsForTable(String driverName, String url, String userName, String password, String tableName)
			throws Exception;

	/**
	 * API to get Document Level Fields given the document Name and batch Class Id.
	 * @param documentName {@link String}
	 * @param batchClassId {@link String}
	 * @return Map<{@link String}, {@link String}>
	 */
	Map<String, String> getDocumentLevelFields(String documentName, String batchClassId);

	/**
	 * API to learn DataBase for a given batch class id depending on create Index.
	 * @param batchClassId {@link String}
	 * @param createIndex boolean
	 * @throws Exception
	 */
	void learnDataBase(final String batchClassId, final boolean createIndex) throws Exception;

	/**
	 * API to copy Batch Class given a BatchClassDTO.
	 * @param batchClassDTO {@link BatchClassDTO}
	 * @throws Exception
	 */
	void copyBatchClass(BatchClassDTO batchClassDTO) throws Exception;

	/**
	 * API to get Batch Folder List.
	 * @return {@link BatchClassDTO}
	 */
	BatchFolderListDTO getBatchFolderList();

	/**
	 * API to get Batch Classes in a particular order.
	 * @param firstResult int
	 * @param maxResults int
	 * @param order {@link Order}
	 * @return List<{@link BatchClassDTO}>
	 */
	List<BatchClassDTO> getBatchClasses(int firstResult, int maxResults, Order order);

	/**
	 * API to count All Batch Classes Excluding the one's Deleted.
	 * @return int
	 */
	int countAllBatchClassesExcludeDeleted();

	/**
	 * API to create Unc Folder given the path
	 * @param path {@link String}
	 * @throws GWTException
	 */
	void createUncFolder(String path) throws GWTException;

	/**
	 * API to get Project Files For Document Type given the batch Class Identifier and document Type Name.
	 * @param batchClassIdentifier {@link String}
	 * @param documentTypeName {@link String}
	 * @return List<{@link String}>
	 * @throws GWTException
	 */
	List<String> getProjectFilesForDocumentType(String batchClassIdentifier, String documentTypeName) throws GWTException;

	/**
	 * API to
	 * @param batchClassDTO {@link BatchClassDTO}
	 * @param kvExtractionDTO {@link KVExtractionDTO}
	 * @return list<{@link OutputDataCarrierDTO}>
	 * @throws GWTException
	 */
	List<OutputDataCarrierDTO> testKVExtraction(BatchClassDTO batchClassDTO, KVExtractionDTO kvExtractionDTO) throws GWTException;

	/**
	 * API to get All Roles available.
	 * @return List<{@link RoleDTO}>
	 */
	List<RoleDTO> getAllRoles();

	/**
	 * API to get All Barcode Types available.
	 * @return List<{@link String}>
	 */
	List<String> getAllBarcodeTypes();

	/**
	 * 
	 *API to delete Batch Class given the BatchClassDTO.
	 * @param batchClassDTO {@link BatchClassDTO}
	 * @return {@link BatchClassDTO}
	 */
	BatchClassDTO deleteBatchClass(BatchClassDTO batchClassDTO);

	/**
	 * API to match Base Folder to given unc folder path. 
	 * 
	 * @param uncFolder {@link String}
	 * @return {@link String}
	 */
	String matchBaseFolder(String uncFolder);

	/**
	 * API to copy the Document given the documentTypeDTO.
	 * @param documentTypeDTO {@link DocumentTypeDTO}
	 * @throws Exception
	 */
	void copyDocument(DocumentTypeDTO documentTypeDTO) throws Exception;

	/**
	 * API to get Advanced Key Value Image Upload Path given the image name for a particular batch class id.
	 * @param batchClassId {@link String}
	 * @param imageName {@link String}
	 * @return {@link String}
	 */
	String getAdvancedKVImageUploadPath(String batchClassId, String imageName);

	/**
	 * API to test Table Pattern given the BatchClassDTO and TableInfoDTO.
	 * @param batchClassDTO {@link BatchClassDTO}
	 * @param tableInfoDTO {@link TableInfoDTO}
	 * @return List<{@link TestTableResultDTO}>
	 * @throws GWTException
	 */
	public List<TestTableResultDTO> testTablePattern(BatchClassDTO batchClassDTO, TableInfoDTO tableInfoDTO) throws GWTException;

	/**
	 * API to get import BatchClass UI Configuration given the workflow Name and zip Source Path.
	 * @param workflowName {@link String}
	 * @param zipSourcePath {@link String}
	 * @return {@link ImportBatchClassSuperConfig}
	 */
	ImportBatchClassSuperConfig getImportBatchClassUIConfig(String workflowName, String zipSourcePath);

	/**
	 * API to import Batch Class for the given ImportBatchClassUserOptionDTO object.
	 * @param userOptions {@link ImportBatchClassUserOptionDTO}
	 * @return boolean
	 * @throws GWTException
	 */
	boolean importBatchClass(ImportBatchClassUserOptionDTO userOptions) throws GWTException;

	/**
	 * API to get All BatchClasses Including the one's Deleted.
	 * @return List<{@link BatchClassDTO}>
	 */
	List<BatchClassDTO> getAllBatchClassesIncludingDeleted();

	/**
	 * API to get All Primary Keys For Table given DB config and table name.
	 * @param driverName {@link String}
	 * @param url {@link String}
	 * @param userName {@link String}
	 * @param password {@link String}
	 * @param table {@link String}
	 * @param tableType {@link String}
	 * @return List<{@link String}>
	 * @throws Exception
	 */
	List<String> getAllPrimaryKeysForTable(String driverName, String url, String userName, String password, String table,
			String tableType) throws Exception;

	/**
	 * API to delete Attached Folders given zip File Name.
	 * @param zipFileName {@link String}
	 */
	void deleteAttachedFolders(final String zipFileName);

	/**
	 * API to get BatchClass Row Count.
	 * @return {@link String}
	 * @throws GWTException
	 */
	String getBatchClassRowCount() throws GWTException;

}
