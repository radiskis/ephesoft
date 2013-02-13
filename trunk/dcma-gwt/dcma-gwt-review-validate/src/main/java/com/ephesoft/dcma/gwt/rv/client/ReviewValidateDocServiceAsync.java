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

package com.ephesoft.dcma.gwt.rv.client;

import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Row;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.gwt.core.client.DCMARemoteServiceAsync;
import com.ephesoft.dcma.gwt.core.shared.BatchDTO;
import com.ephesoft.dcma.gwt.core.shared.DocumentTypeDBBean;
import com.ephesoft.dcma.gwt.core.shared.FieldTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.FunctionKeyDTO;
import com.ephesoft.dcma.gwt.core.shared.PointCoordinate;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * This class is responsible for handling the review and validate operations on the batches asynchronously.
 * 
 * @author Ephesoft
 * 
 */
public interface ReviewValidateDocServiceAsync extends DCMARemoteServiceAsync {

	/**
	 * API to get the Highest Priority Batch asynchronously.
	 * 
	 * @param callback {@link AsyncCallback} < {@link BatchDTO} >
	 */
	void getHighestPriortyBatch(AsyncCallback<BatchDTO> callback);

	/**
	 * API to get Batch by batch Instance Identifier asynchronously.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @param callback {@link AsyncCallback} < {@link BatchDTO} >
	 */
	void getBatch(String batchInstanceIdentifier, AsyncCallback<BatchDTO> callback);

	/**
	 * API to get Document Type of a batch By BatchInstanceID asynchronously.
	 * 
	 * @param batchInstanceIdentifierIdentifierIdentifier String
	 * @param callback {@link AsyncCallback} < List<{@link DocumentTypeDBBean}> >
	 */
	void getDocTypeByBatchInstanceID(String batchInstanceIdentifierIdentifierIdentifier,
			AsyncCallback<List<DocumentTypeDBBean>> callback);

	/**
	 * API to merge two Document for a batch asynchronously.
	 * 
	 * @param batch {@link Batch}
	 * @param documentId {@link String}
	 * @param documentIdToBeMerged {@link String}
	 * @param callback {@link AsyncCallback} < {@link BatchDTO} >
	 */
	void mergeDocument(Batch batch, String documentId, String documentIdToBeMerged, AsyncCallback<BatchDTO> callback);

	/**
	 * API to get Field Type By Document Type Name asynchronously.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @param docTypeName {@link String}
	 * @param callback {@link AsyncCallback} < {@link Document} >
	 */
	void getFdTypeByDocTypeName(String batchInstanceIdentifier, String docTypeName, AsyncCallback<Document> callback);

	/**
	 * API to duplicate Page Of Document for a batch asynchronously.
	 * 
	 * @param batch {@link Batch}
	 * @param docID {@link String}
	 * @param duplicatePageID {@link String}
	 * @param callback {@link AsyncCallback} < {@link BatchDTO} >
	 */
	void duplicatePageOfDocument(Batch batch, String docID, String duplicatePageID, AsyncCallback<BatchDTO> callback);

	/**
	 * API to delete Page Of Document for a batch.
	 * 
	 * @param batch {@link Batch}
	 * @param docID {@link String}
	 * @param pageId {@link String}
	 * @param callback {@link AsyncCallback} < {@link BatchDTO} >
	 */
	void deletePageOfDocument(Batch batch, String docID, String pageId, AsyncCallback<BatchDTO> callback);

	/**
	 * API to split Document for a batch.
	 * 
	 * @param batch {@link Batch}
	 * @param docID {@link String}
	 * @param pageId {@link String}
	 * @param callback {@link AsyncCallback} < {@link BatchDTO} >
	 */
	void splitDocument(Batch batch, String docID, String pageId, AsyncCallback<BatchDTO> callback);

	/**
	 * API to move Page Of Document for a batch asynchronously.
	 * 
	 * @param batch {@link Batch}
	 * @param selectedPageId {@link String}
	 * @param selectedDocumentId {@link String}
	 * @param moveToDocumentId1 {@link String}
	 * @param moveToPageId1 {@link String}
	 * @param moveAfterchecked {@link Boolean}
	 * @param callback {@link AsyncCallback} < {@link BatchDTO} >
	 */
	void movePageOfDocument(Batch batch, String selectedPageId, String selectedDocumentId, String moveToDocumentId1,
			String moveToPageId1, Boolean moveAfterchecked, AsyncCallback<BatchDTO> callback);

	/**
	 * API to rotate Image for a batch asynchronously.
	 * 
	 * @param batch {@link Batch}
	 * @param page {@link Page}
	 * @param documentId {@link String}
	 * @param callback {@link AsyncCallback} < {@link Page} >
	 */
	void rotateImage(Batch batch, Page page, String documentId, AsyncCallback<Page> callback);

	/**
	 * API to get Rows Count for a batch asynchronously.
	 * 
	 * @param callback {@link AsyncCallback} < {@link Integer} >
	 */
	void getRowsCount(AsyncCallback<Integer> callback);

	/**
	 * API to Text Search using Fuzzy DB for a batch asynchronously.
	 * 
	 * @param batch {@link Batch}
	 * @param documentType {@link String}
	 * @param searchText {@link String}
	 * @param callback {@link AsyncCallback} < List< List < {@link String} > > >
	 */
	void fuzzyTextSearch(Batch batch, String documentType, String searchText, AsyncCallback<List<List<String>>> callback);

	/**
	 * API to get FieldTypeDTO objects for a batch asynchronously.
	 * 
	 * @param documentName {@link String}
	 * @param batchInstanceIdentifier {@link String}
	 * @param callback {@link AsyncCallback} < List<{@link FieldTypeDTO}> >
	 */
	void getFieldTypeDTOs(String documentName, String batchInstanceIdentifier, AsyncCallback<List<FieldTypeDTO>> callback);

	/**
	 * API to get Column Regular expressions Pattern for a document asynchronously.
	 * 
	 * @param documentName {@link String}
	 * @param tableName {@link String}
	 * @param callback {@link AsyncCallback} < List<{@link String}> >
	 */
	void getColumnRegexPattern(String documentName, String tableName, AsyncCallback<List<String>> callback);

	/**
	 * API to get Default Document Type View asynchronously.
	 * 
	 * @param callback {@link AsyncCallback} < {@link String} >
	 */
	void getDefaultDocTypeView(AsyncCallback<String> asyncCallback);

	/**
	 * API to execute Script for a batch asynchronously.
	 * 
	 * @param batch {@link Batch}
	 * @param document {@link Document}
	 * @param callback {@link AsyncCallback} < {@link BatchDTO} >
	 */
	void executeScript(Batch batch, Document document, AsyncCallback<BatchDTO> callback);

	/**
	 * API to get HOCR Content the given coordinates set for a page of a batch asynchronously.
	 * 
	 * @param pointCoordinate1 {@link PointCoordinate}
	 * @param pointCoordinate2 {@link PointCoordinate}
	 * @param batchInstanceIdentifier {@link String}
	 * @param pageID {@link String}
	 * @param rectangularCoordinateSet boolean
	 * @param callback {@link AsyncCallback} < List<{@link Span> >
	 */
	void getHOCRContent(PointCoordinate pointCoordinate1, PointCoordinate pointCoordinate2, String batchInstanceIdentifier,
			String pageID, boolean rectangularCoordinateSet, AsyncCallback<List<Span>> asyncCallback);

	/**
	 * API to get HOCR Content the given coordinates set for a page of a batch asynchronously.
	 * 
	 * @param pointCoordinates List<{@link PointCoordinate}>
	 * @param batchInstanceIdentifier {@link String}
	 * @param pageID {@link String}
	 * @param callback {@link AsyncCallback} < List<{@link Span> >
	 */
	void getHOCRContent(List<PointCoordinate> pointCoordinates, String batchInstanceIdentifier, String identifier,
			AsyncCallback<List<Span>> callback);

	/**
	 * API to execute Script for a batch asynchronously.
	 * 
	 * @param shortcutKeyName {@link String}
	 * @param batch {@link Batch}
	 * @param document {@link Document}
	 * @param callback {@link AsyncCallback} < {@link BatchDTO} >
	 */
	void executeScript(String shortcutKeyName, Batch batch, Document document, AsyncCallback<BatchDTO> callback);

	/**
	 * API to execute Add New Table for a batch asynchronously.
	 * 
	 * @param batch {@link Batch}
	 * @param documentId {@link String}
	 * @param callback {@link AsyncCallback} < {@link BatchDTO} >
	 */
	void executeAddNewTable(Batch batch, String documentId, AsyncCallback<BatchDTO> callback);

	/**
	 * API to get Table HOCR Content for the given coordinates set for a page of a batch asynchronously.
	 * 
	 * @param initialCoordinate {@link PointCoordinate}
	 * @param finalCoordinate {@link PointCoordinate}
	 * @param batchInstanceIdentifier {@link String}
	 * @param pageID {@link String}
	 * @param callback {@link AsyncCallback} < List<{@link Span}> >
	 */
	void getTableHOCRContent(final PointCoordinate initialCoordinate, final PointCoordinate finalCoordinate,
			final String batchInstanceIdentifier, final String pageID, AsyncCallback<List<Span>> asyncCallback);

	/**
	 * API to get Data in tabular form for a batch from a page asynchronously.
	 * 
	 * @param columnVsCoordinates Map< {@link Integer}, {@link Coordinates}>
	 * @param documentTypeName {@link String}
	 * @param selectedDataTable {@link DataTable}
	 * @param batchClassIdentifier {@link String}
	 * @param batchInstanceIdentifier {@link String}
	 * @param pageID {@link String}
	 * @param callback {@link AsyncCallback} < List<{@link Row}> >
	 */
	void getTableData(final Map<Integer, Coordinates> columnVsCoordinates, String documnentTypeName, DataTable selectedDataTable,
			String batchClassIdentifier, final String batchInstanceIdentifier, final String pageID,
			AsyncCallback<List<Row>> asyncCallback);

	/**
	 * API to get Function Key DTO for a batch asynchronously.
	 * 
	 * @param documentTypeName {@link String}
	 * @param batchInstanceIdentifier {@link String}
	 * @param callback {@link AsyncCallback} < List<{@link FunctionKeyDTO}> >
	 */
	void getFunctionKeyDTOs(final String documentTypeName, final String batchInstanceIdentifier,
			AsyncCallback<List<FunctionKeyDTO>> asyncCallback);

	/**
	 * API to execute Script On Field Change for a batch asynchronously.
	 * 
	 * @param batch {@link Batch}
	 * @param document {@link Document}
	 * @param fieldName {@link String}
	 * @param callback {@link AsyncCallback} < {@link BatchDTO} >
	 */
	void executeScriptOnFieldChange(Batch batch, Document document, String fieldName, AsyncCallback<BatchDTO> asyncCallback);
	
	/**
	 * API to get Generated Security Token For External Application asynchronously.
	 * 
	 * @param callback {@link AsyncCallback} < {@link String} >
	 */
	void getGeneratedSecurityTokenForExternalApp(AsyncCallback<String> asyncCallback);

	/**
	 * API to get Encoded String for the given path Of Batch Xml asynchronously.
	 * 
	 * @param pathOfBatchXml {@link String}
	 * @param callback {@link AsyncCallback} < {@link String} >
	 */
	void getEncodedString(String pathOfBatchXml, AsyncCallback<String> asyncCallback);

	/**
	 * API to save a batch asynchronously.
	 * 
	 * @param batch {@link Batch}
	 * @param callback {@link AsyncCallback} < {@link Void} >
	 */

	void saveBatch(Batch batch, AsyncCallback<Void> callback);

	/**
	 * API to signal Workflow for a batch asynchronously.
	 * 
	 * @param batch {@link Batch}
	 * @param callback {@link AsyncCallback} < {@link Void} >
	 */

	void signalWorkflow(Batch batch, AsyncCallback<Void> asyncCallback);

	/**
	 * API to get Encoded String for the given path Of Batch Xml asynchronously after checking whether it is in xml or zip format.
	 * 
	 * @param pathOfBatchXml {@link String}
	 * @param callback {@link AsyncCallback} < {@link String} >
	 */
	void getEncodedStringForXMLPath(String pathOfBatchXml, AsyncCallback<String> asyncCallback);
     
    /**API to get zoomCount
     *
     * @param callback {@link AsyncCallback}< {@link String} >
     */
    void getZoomCount(AsyncCallback<String> asyncCallback);

    /**API to update the end time of review or validation of batchInstance 
     * @param batchInstanceId
     * @param asyncCallback
     */
    void updateEndTimeAndCalculateDuration(String batchInstanceId,AsyncCallback<Void> asyncCallback);
    
    /**API to record the start time of review or validation of batchInstance 
     * @param batchInstanceId
     * @param asyncCallback
     * @param batchInstanceStatus
     */
    void recordReviewOrValidateDuration( String batchInstanceId, BatchInstanceStatus batchInstanceStatus,AsyncCallback<Void> asyncCallback);

}
