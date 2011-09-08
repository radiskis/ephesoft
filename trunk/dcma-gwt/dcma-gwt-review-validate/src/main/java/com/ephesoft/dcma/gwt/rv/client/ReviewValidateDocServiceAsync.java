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
import com.ephesoft.dcma.gwt.core.client.DCMARemoteServiceAsync;
import com.ephesoft.dcma.gwt.core.shared.BatchDTO;
import com.ephesoft.dcma.gwt.core.shared.DocumentTypeDBBean;
import com.ephesoft.dcma.gwt.core.shared.FieldTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.FunctionKeyDTO;
import com.ephesoft.dcma.gwt.core.shared.PointCoordinate;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ReviewValidateDocServiceAsync extends DCMARemoteServiceAsync {

	void getHighestPriortyBatch(AsyncCallback<BatchDTO> callback);

	void getBatch(String batchInstanceIdentifier, AsyncCallback<BatchDTO> callback);

	void updateBatch(Batch batch, AsyncCallback<BatchStatus> callback);

	void getDocTypeByBatchInstanceID(String batchInstanceIdentifierIdentifierIdentifier,
			AsyncCallback<List<DocumentTypeDBBean>> callback);

	void mergeDocument(Batch batch, String documentId, String documentIdToBeMerged, AsyncCallback<BatchDTO> callback);

	void getFdTypeByDocTypeName(String batchInstanceIdentifier, String docTypeName, AsyncCallback<Document> callback);

	void duplicatePageOfDocument(Batch batch, String docID, String duplicatePageID, AsyncCallback<BatchDTO> callback);

	void deletePageOfDocument(Batch batch, String docID, String pageId, AsyncCallback<BatchDTO> callback);

	void splitDocument(Batch batch, String docID, String pageId, AsyncCallback<BatchDTO> callback);

	void movePageOfDocument(Batch batch, String selectedPageId, String selectedDocumentId, String moveToDocumentId1,
			String moveToPageId1, Boolean moveAfterchecked, AsyncCallback<BatchDTO> callback);

	void roatateImage(Batch batch, Page page, String documentId, AsyncCallback<Page> callback);

	void getRowsCount(AsyncCallback<Integer> callback);

	void fuzzyTextSearch(Batch batch, String searchText, AsyncCallback<List<List<String>>> callback);

	void saveBatch(BatchDTO batchDTO, AsyncCallback<Void> callback);

	void getFieldTypeDTOs(String documentName, String batchInstanceIdentifier, AsyncCallback<List<FieldTypeDTO>> callback);

	void getColumnRegexPattern(String documentName, String tableName, AsyncCallback<List<String>> callback);

	void getDefaultDocTypeView(AsyncCallback<String> asyncCallback);

	void signalWorkflow(Batch batch, AsyncCallback<BatchStatus> callback);

	void executeScript(Batch batch, AsyncCallback<BatchDTO> callback);

	void getHOCRContent(PointCoordinate pointCoordinate1, PointCoordinate pointCoordinate2, String batchInstanceIdentifier,
			String pageID, AsyncCallback<List<Span>> asyncCallback);

	void getHOCRContent(List<PointCoordinate> pointCoordinates, String batchInstanceIdentifier, String identifier,
			AsyncCallback<List<Span>> callback);

	void executeScript(String shortcutKeyName, Batch batch, Document document, AsyncCallback<BatchDTO> callback);

	void executeAddNewTable(Batch batch, String documentId, AsyncCallback<BatchDTO> callback);

	void getTableHOCRContent(final PointCoordinate initialCoordinate, final PointCoordinate finalCoordinate,
			final String batchInstanceIdentifier, final String pageID, AsyncCallback<List<Span>> asyncCallback);

	void getTableData(final Map<Integer, Coordinates> columnVsCoordinates, String documnentTypeName, DataTable selectedDataTable,
			String batchClassIdentifier, final String batchInstanceIdentifier, final String pageID,
			AsyncCallback<List<Row>> asyncCallback);

	void getFunctionKeyDTOs(final String documentTypeName, final String batchInstanceIdentifier,
			AsyncCallback<List<FunctionKeyDTO>> asyncCallback);

	void getUrlsOfExternalAppByShortcuts(String batchInstanceIdentifier, AsyncCallback<Map<String, String>> asyncCallback);

	void getDimensionsForPopUp(String batchInstanceIdentifier, AsyncCallback<Map<String, String>> asyncCallback);

}
