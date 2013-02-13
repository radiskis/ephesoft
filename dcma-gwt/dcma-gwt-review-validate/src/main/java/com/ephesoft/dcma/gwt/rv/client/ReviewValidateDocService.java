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
import com.ephesoft.dcma.gwt.core.client.DCMARemoteService;
import com.ephesoft.dcma.gwt.core.shared.BatchDTO;
import com.ephesoft.dcma.gwt.core.shared.DocumentTypeDBBean;
import com.ephesoft.dcma.gwt.core.shared.FieldTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.FunctionKeyDTO;
import com.ephesoft.dcma.gwt.core.shared.PointCoordinate;
import com.ephesoft.dcma.gwt.core.shared.exception.GWTException;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * This class is responsible for handling the review and validate operations on the batches.
 * 
 * @author Ephesoft
 * @see com.ephesoft.dcma.gwt.rv.server.ReviewValidateDocServiceImpl
 * 
 */
@RemoteServiceRelativePath("rvService")
public interface ReviewValidateDocService extends DCMARemoteService {

	/**
	 * API to get the Highest Priority Batch.
	 * 
	 * @return {@link BatchDTO}
	 * @throws GWTException 
	 */
	BatchDTO getHighestPriortyBatch() throws GWTException;

	/**
	 * API to get Batch by batch Instance Identifier.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @return {@link BatchDTO}
	 * @throws GWTException 
	 */
	BatchDTO getBatch(String batchInstanceIdentifier) throws GWTException;

	/**
	 * API to get Document Type of a batch By BatchInstanceID.
	 * 
	 * @param batchInstanceIdentifierIdentifierIdentifier String
	 * @return List<{@link DocumentTypeDBBean}>
	 */
	List<DocumentTypeDBBean> getDocTypeByBatchInstanceID(String batchInstanceIdentifierIdentifierIdentifier);

	/**
	 * API to merge two Document for a batch
	 * 
	 * @param batch {@link Batch}
	 * @param documentId {@link String}
	 * @param documentIdToBeMerged {@link String}
	 * @return {@link BatchDTO}
	 * @throws GWTException 
	 */
	BatchDTO mergeDocument(Batch batch, String documentId, String documentIdToBeMerged) throws GWTException;

	/**
	 * API to get Field Type By Document Type Name.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @param docTypeName {@link String}
	 * @return {@link Document}
	 */
	Document getFdTypeByDocTypeName(String batchInstanceIdentifier, String docTypeName);

	/**
	 * API to duplicate Page Of Document for a batch.
	 * 
	 * @param batch {@link Batch}
	 * @param docID {@link String}
	 * @param duplicatePageID {@link String}
	 * @return {@link BatchDTO}
	 * @throws GWTException 
	 */
	BatchDTO duplicatePageOfDocument(Batch batch, String docID, String duplicatePageID) throws GWTException;

	/**
	 * API to delete Page Of Document for a batch.
	 * 
	 * @param batch {@link Batch}
	 * @param docID {@link String}
	 * @param pageId {@link String}
	 * @return {@link BatchDTO}
	 * @throws GWTException 
	 */
	BatchDTO deletePageOfDocument(Batch batch, String docID, String pageId) throws GWTException;

	/**
	 * API to split Document for a batch.
	 * 
	 * @param batch {@link Batch}
	 * @param docID {@link String}
	 * @param pageId {@link String}
	 * @return {@link BatchDTO}
	 * @throws GWTException 
	 */
	BatchDTO splitDocument(Batch batch, String docID, String pageId) throws GWTException;

	/**
	 * API to move Page Of Document for a batch.
	 * 
	 * @param batch {@link Batch}
	 * @param selectedPageId {@link String}
	 * @param selectedDocumentId {@link String}
	 * @param moveToDocumentId1 {@link String}
	 * @param moveToPageId1 {@link String}
	 * @param moveAfterchecked {@link Boolean}
	 * @return {@link BatchDTO}
	 * @throws GWTException 
	 */
	BatchDTO movePageOfDocument(Batch batch, String selectedPageId, String selectedDocumentId, String moveToDocumentId1,
			String moveToPageId1, Boolean moveAfterchecked) throws GWTException;

	/**
	 * API to rotate Image for a batch.
	 * 
	 * @param batch {@link Batch}
	 * @param page {@link Page}
	 * @param documentId {@link String}
	 * @return {@link Page}
	 * @throws GWTException 
	 */
	Page rotateImage(Batch batch, Page page, String documentId) throws GWTException;

	/**
	 * API to Text Search using Fuzzy DB for a batch.
	 * 
	 * @param batch {@link Batch}
	 * @param documentType {@link String}
	 * @param searchText {@link String}
	 * @return List< List < {@link String}>>
	 */
	List<List<String>> fuzzyTextSearch(Batch batch, String documentType, String searchText);

	/**
	 * API to get Rows Count for a batch.
	 * 
	 * @return int
	 */
	int getRowsCount();

	/**
	 * API to get FieldTypeDTO objects for a batch.
	 * 
	 * @param documentName {@link String}
	 * @param batchInstanceIdentifier {@link String}
	 * @return List<{@link FieldTypeDTO}>
	 */
	List<FieldTypeDTO> getFieldTypeDTOs(String documentName, String batchInstanceIdentifier);

	/**
	 * API to get Column Regular expressions Pattern for a document.
	 * 
	 * @param documentName {@link String}
	 * @param tableName {@link String}
	 * @return List<{@link String}>
	 */
	List<String> getColumnRegexPattern(String documentName, String tableName);

	/**
	 * API to get Default Document Type View.
	 * 
	 * @return {@link String}
	 */
	String getDefaultDocTypeView();

	/**
	 * API to execute Script for a batch.
	 * 
	 * @param batch {@link Batch}
	 * @param document {@link Document}
	 * @return {@link BatchDTO}
	 * @throws GWTException
	 */
	BatchDTO executeScript(Batch batch, Document document) throws GWTException;

	/**
	 * API to get HOCR Content the given coordinates set for a page of a batch.
	 * 
	 * @param pointCoordinate1 {@link PointCoordinate}
	 * @param pointCoordinate2 {@link PointCoordinate}
	 * @param batchInstanceIdentifier {@link String}
	 * @param pageID {@link String}
	 * @param rectangularCoordinateSet boolean
	 * @return List<{@link Span>
	 */
	List<Span> getHOCRContent(PointCoordinate pointCoordinate1, PointCoordinate pointCoordinate2, String batchInstanceIdentifier,
			String pageID, boolean rectangularCoordinateSet);

	/**
	 * API to get HOCR Content the given coordinates set for a page of a batch.
	 * 
	 * @param pointCoordinates List<{@link PointCoordinate}>
	 * @param batchInstanceIdentifier {@link String}
	 * @param pageID {@link String}
	 * @return List<{@link Span}>
	 */
	List<Span> getHOCRContent(List<PointCoordinate> pointCoordinates, String batchInstanceIdentifier, String pageID);

	/**
	 * API to execute Script for a batch.
	 * 
	 * @param shortcutKeyName {@link String}
	 * @param batch {@link Batch}
	 * @param document {@link Document}
	 * @return BatchDTO {@link BatchDTO}
	 * @throws GWTException
	 */
	BatchDTO executeScript(String shortcutKeyName, Batch batch, Document document) throws GWTException;

	/**
	 * API to execute Add New Table for a batch.
	 * 
	 * @param batch {@link Batch}
	 * @param documentId {@link String}
	 * @return BatchDTO {@link BatchDTO}
	 * @throws GWTException
	 */
	BatchDTO executeAddNewTable(Batch batch, String documentId) throws GWTException;

	/**
	 * API to get Table HOCR Content for the given coordinates set for a page of a batch.
	 * 
	 * @param initialCoordinate {@link PointCoordinate}
	 * @param finalCoordinate {@link PointCoordinate}
	 * @param batchInstanceIdentifier {@link String}
	 * @param pageID {@link String}
	 * @return List<{@link Span}>
	 */
	List<Span> getTableHOCRContent(final PointCoordinate initialCoordinate, final PointCoordinate finalCoordinate,
			final String batchInstanceIdentifier, final String pageID);

	/**
	 * API to get Data in tabular form for a batch from a page.
	 * 
	 * @param columnVsCoordinates Map< {@link Integer}, {@link Coordinates}>
	 * @param documentTypeName {@link String}
	 * @param selectedDataTable {@link DataTable}
	 * @param batchClassIdentifier {@link String}
	 * @param batchInstanceIdentifier {@link String}
	 * @param pageID {@link String}
	 * @return List<{@link Row}>
	 */
	List<Row> getTableData(final Map<Integer, Coordinates> columnVsCoordinates, final String documentTypeName,
			final DataTable selectedDataTable, final String batchClassIdentifier, final String batchInstanceIdentifier,
			final String pageID);

	/**
	 * API to get Function Key DTO for a batch.
	 * 
	 * @param documentTypeName {@link String}
	 * @param batchInstanceIdentifier {@link String}
	 * @return List<{@link FunctionKeyDTO}>
	 */
	List<FunctionKeyDTO> getFunctionKeyDTOs(String documentTypeName, String batchInstanceIdentifier);

	/**
	 * API to execute Script On Field Change for a batch.
	 * 
	 * @param batch {@link Batch}
	 * @param document {@link Document}
	 * @param fieldName {@link String}
	 * @return {@link BatchDTO}
	 * @throws GWTException
	 */
	BatchDTO executeScriptOnFieldChange(Batch batch, Document document, String fieldName) throws GWTException;

	/**
	 * API to get Generated Security Token For External Application.
	 * 
	 * @return {@link String}
	 */
	String getGeneratedSecurityTokenForExternalApp();

	/**
	 * API to get Encoded String for the given path Of Batch Xml.
	 * 
	 * @param pathOfBatchXml {@link String}
	 * @return {@link String}
	 */
	String getEncodedString(String pathOfBatchXml);

	/**
	 * API to signal Workflow for a batch.
	 * 
	 * @param batch {@link Batch}
	 * @throws GWTException 
	 */
	void signalWorkflow(Batch batch) throws GWTException;

	/**
	 * API to save a batch.
	 * 
	 * @param batch {@link Batch}
	 * @throws GWTException 
	 */
	void saveBatch(Batch batch) throws GWTException;

	/**
	 * API to get Encoded String for the given path Of Batch Xml after checking whether it is a zip or xml format.
	 * 
	 * @param pathOfBatchXml {@link String}
	 * @return {@link String}
	 */
	String getEncodedStringForXMLPath(String pathOfBatchXml);
  
	/**API to get zoomCount value
	 * 
	 * 
	 * @return{@link String}
	 */
	String getZoomCount();

	/**
	 * API to update the end time of review or validation of batchInstance
	 * 
	 * @param batchInstanceId
	 */
	void updateEndTimeAndCalculateDuration(String batchInstanceId);

	/**
	 * API to record the start time of review or validation of batchInstance
	 * 
	 * @param batchInstanceId
	 * @param batchInstanceStatus
	 */
	void recordReviewOrValidateDuration( String batchInstanceId, BatchInstanceStatus batchInstanceStatus);
}
