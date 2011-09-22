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

package com.ephesoft.dcma.gwt.rv.client;

import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.BatchStatus;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Row;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.gwt.core.client.DCMARemoteService;
import com.ephesoft.dcma.gwt.core.shared.BatchDTO;
import com.ephesoft.dcma.gwt.core.shared.DocumentTypeDBBean;
import com.ephesoft.dcma.gwt.core.shared.FieldTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.FunctionKeyDTO;
import com.ephesoft.dcma.gwt.core.shared.PointCoordinate;
import com.ephesoft.dcma.gwt.core.shared.exception.GWTException;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rvService")
public interface ReviewValidateDocService extends DCMARemoteService {

	BatchDTO getHighestPriortyBatch();

	BatchDTO getBatch(String batchInstanceIdentifier);

	BatchStatus updateBatch(Batch batch);

	List<DocumentTypeDBBean> getDocTypeByBatchInstanceID(String batchInstanceIdentifierIdentifierIdentifier);

	BatchDTO mergeDocument(Batch batch, String documentId, String documentIdToBeMerged);

	Document getFdTypeByDocTypeName(String batchInstanceIdentifier, String docTypeName);

	BatchDTO duplicatePageOfDocument(Batch batch, String docID, String duplicatePageID);

	BatchDTO deletePageOfDocument(Batch batch, String docID, String pageId);

	BatchDTO splitDocument(Batch batch, String docID, String pageId);

	BatchDTO movePageOfDocument(Batch batch, String selectedPageId, String selectedDocumentId, String moveToDocumentId1,
			String moveToPageId1, Boolean moveAfterchecked);

	Page roatateImage(Batch batch, Page page, String documentId);

	List<List<String>> fuzzyTextSearch(Batch batch, String searchText);

	int getRowsCount();

	void saveBatch(BatchDTO batchDTO);

	List<FieldTypeDTO> getFieldTypeDTOs(String documentName, String batchInstanceIdentifier);

	List<String> getColumnRegexPattern(String documentName, String tableName);

	String getDefaultDocTypeView();

	BatchStatus signalWorkflow(Batch batch);

	BatchDTO executeScript(Batch batch) throws GWTException;

	List<Span> getHOCRContent(PointCoordinate pointCoordinate1, PointCoordinate pointCoordinate2, String batchInstanceIdentifier,
			String pageID);

	List<Span> getHOCRContent(List<PointCoordinate> pointCoordinates, String batchInstanceIdentifier, String pageID);

	BatchDTO executeScript(String shortcutKeyName, Batch batch, Document document) throws GWTException;

	BatchDTO executeAddNewTable(Batch batch, String documentId) throws GWTException;

	List<Span> getTableHOCRContent(final PointCoordinate initialCoordinate, final PointCoordinate finalCoordinate,
			final String batchInstanceIdentifier, final String pageID);

	List<Row> getTableData(final Map<Integer, Coordinates> columnVsCoordinates, final String documentTypeName,
			final DataTable selectedDataTable, final String batchClassIdentifier, final String batchInstanceIdentifier,
			final String pageID);

	List<FunctionKeyDTO> getFunctionKeyDTOs(String documentTypeName, String batchInstanceIdentifier);

	Map<String, String> getUrlsOfExternalAppByShortcuts(String batchInstanceIdentifier);

	Map<String, String> getDimensionsForPopUp(String batchInstanceIdentifier);

}
