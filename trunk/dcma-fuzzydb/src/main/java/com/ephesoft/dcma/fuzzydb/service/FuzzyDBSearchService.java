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

package com.ephesoft.dcma.fuzzydb.service;

import java.util.List;

import com.ephesoft.dcma.batch.schema.Documents;
import com.ephesoft.dcma.batch.schema.HocrPages;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.da.id.BatchClassID;
import com.ephesoft.dcma.da.id.BatchInstanceID;

/**
 * This service first indexes the tables corresponding to document types defined in database and then it overwrites/creates the value
 * of document level fields for each document inside batch xml.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.fuzzydb.service.FuzzyDBSearchServiceImpl
 * 
 */
public interface FuzzyDBSearchService {

	/**
	 * This method is used to generate the indexes for the tables mapped for each document type in database. The indexes are stored in
	 * a hierarchical structure: batch class id >> database name >> table name.
	 * 
	 * @param batchClassIdentifier {@link BatchClassID}
	 * @param createIndex boolean
	 * @throws DCMAException
	 */
	void learnDataBase(final BatchClassID batchClassID, boolean createIndex) throws DCMAException;

	/**
	 * This method is used to generate the indexes for the tables mapped for each document type in database. The indexes are stored in
	 * a hierarchical structure: batch class id >> database name >> table name. This API learns DB for list of batch classes given in
	 * properties file
	 * 
	 */
	void learnDataBaseForMultipleBatchClasses();

	/**
	 * This method creates/updates the value of document level fields for each document by searching for similarities in HOCR content
	 * with the data in database tables mapped for each document type.
	 * 
	 * @param batchInstanceIdentifier {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 * @throws DCMAException
	 */
	void extractDataBaseFields(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException;

	/**
	 * This method updates the value of document level fields for each document by searching for similarities in input search text with
	 * the data in database tables mapped for each document type.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param documentType {@link String}
	 * @param searchText {@link String}
	 * @return List< List< {@link String} > >
	 * @throws DCMAException
	 */
	List<List<String>> fuzzyTextSearch(final BatchInstanceID batchInstanceID, String documentType, String searchText)
			throws DCMAException;
	
	/**
	 * This method is used to extract the database field values.
	 * @param batchClassIdentifier {@link String}
	 * @param documentType {@link String}
	 * @param hocrPage {@link HocrPages}
	 * @return {@link Documents}
	 * @throws DCMAException
	 */
	Documents extractDataBaseFields(final String batchClassIdentifier, String documentType, HocrPages hocrPage)
	throws DCMAException;
}
