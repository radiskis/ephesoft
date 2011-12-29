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

package com.ephesoft.dcma.gwt.home.client;

import java.util.List;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.gwt.core.client.DCMARemoteServiceAsync;
import com.ephesoft.dcma.gwt.core.shared.BatchInstanceDTO;
import com.ephesoft.dcma.gwt.core.shared.DataFilter;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * TableModelService is a service interface that provides data for displaying in a GWT AdvancedTable widget. The implementing class
 * should provide paging, filtering and sorting as this interface specifies.
 * 
 * Life-cycle: 1) getColumns() is called by the client to populate the table columns 2) getRowsCount() is called by the client to
 * estimate the number of available records on the server. 3) getRows() is called by the client to display a particular page (a subset
 * of the available data) The client call getRowsCount() and getRows() with the same filter. The implementing class can use database or
 * other back-end as data source.
 * 
 * The first table column is used as row identifier (primary key). It can be visible in the table or can be hidden and is passed as row
 * id when a row is selected.
 * 
 * @author Ephesoft
 */

public interface TableModelServiceAsync extends DCMARemoteServiceAsync {

	void getRowsCount(DataFilter[] filters, AsyncCallback<Integer> callback);

	void getRows(int startRow, int rowsCount, DataFilter[] filters, Order order, AsyncCallback<List<BatchInstanceDTO>> callback);

	void getIndividualRowCounts(AsyncCallback<Integer[]> asyncCallback);

	void getNextBatchInstance(AsyncCallback<String> callback);

	void getBatchInstanceDTOs(final String batchName, final DataFilter[] filters, AsyncCallback<List<BatchInstanceDTO>> asyncCallback);
}
