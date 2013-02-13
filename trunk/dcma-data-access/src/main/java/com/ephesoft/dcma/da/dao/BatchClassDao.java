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

package com.ephesoft.dcma.da.dao;

import java.util.List;
import java.util.Set;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.dao.CacheableDao;
import com.ephesoft.dcma.da.domain.BatchClass;

/**
 * This Dao deals with Batch Class.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.da.dao.hibernate.BatchClassDaoImpl
 */
public interface BatchClassDao extends CacheableDao<BatchClass> {

	/**
	 * An api to getch batch class by unc folder name.
	 * 
	 * @param folderName String
	 * @return BatchClass
	 */
	BatchClass getBatchClassbyUncFolder(String folderName);

	/**
	 * An api to fetch BatchClass by batch Class name.
	 * 
	 * @param batchClassName String
	 * @return BatchClass
	 */
	BatchClass getBatchClassbyName(String batchClassName);

	/**
	 * An api to fetch BatchClass by batch Class processName.
	 * 
	 * @param processName String
	 * @return BatchClass
	 */
	BatchClass getBatchClassbyProcessName(String processName);

	/**
	 * This API will fetch all the batch classes.
	 * 
	 * @return List<BatchClass>
	 */
	List<BatchClass> getAllBatchClasses();

	/**
	 * This API will fetch all the unlocked batch classes.
	 * 
	 * @return List<BatchClass>
	 */
	List<BatchClass> getAllUnlockedBatchClasses();

	/**
	 * API to fetch a batch class by Identifier.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @return BatchClass
	 */
	BatchClass getBatchClassByIdentifier(String batchClassIdentifier);

	/**
	 * This method will update the existing batch class.
	 * 
	 * @param batchClass BatchClass
	 */
	void updateBatchClass(BatchClass batchClass);

	/**
	 * API to fetch BatchClass for the current user.
	 * 
	 * @param currentUser {@link String}
	 * @return List<BatchClass>
	 */
	List<BatchClass> getAllBatchClassesForCurrentUser(String currentUser);

	/**
	 * This API will fetch all the unc folder paths.
	 * 
	 * @return List<String>
	 */
	List<String> getAllUNCFolderPaths();

	/**
	 * API to get the list of Batch Classes specifying startindex, no of results and sorting if any.
	 * 
	 * @param startIndex int
	 * @param maxResults int 
	 * @param order List<Order>
	 * @param userRoles Set<String>
	 * @return List of batch class.
	 */
	List<BatchClass> getBatchClassList(int startIndex, int maxResults, List<Order> order, Set<String> userRoles);

	/**
	 * This API will fetch all the batch classes of current user role.
	 * 
	 * @param userRoles Set<String>
	 * @return List<BatchClass>
	 */
	List<BatchClass> getAllBatchClassesByUserRoles(Set<String> userRoles);

	/**
	 * This API will fetch the size of batchclass.
	 * 
	 * @param userRoles Set<String>
	 * @return batchClass size
	 */
	int getAllBatchClassCountExcludeDeleted(Set<String> userRoles);

	/**
	 * This API will fetch the batch class list excluding the deleted batch class.
	 * 
	 * @return List<BatchClass>
	 */
	List<BatchClass> getAllBatchClassExcludeDeleted();

	/**
	 * This API will fetch the batch class (eagerly loaded) list excluding the deleted batch class.
	 * 
	 * @return List<BatchClass>
	 */
	List<BatchClass> getAllLoadedBatchClassExcludeDeleted();

	/**
	 * API to fetch the UNC folders for a batch class by name.
	 * 
	 * @param batchClassName {@link String}
	 * @return List<String>
	 */
	List<String> getAllAssociatedUNCFolders(String batchClassName);

	/**
	 * API to get Batch Class by name including deleted.
	 * 
	 * @param batchClassName {@link String}
	 * @return {@link BatchClass}
	 */
	BatchClass getBatchClassByNameIncludingDeleted(String batchClassName);

	/**
	 * API to get batch class identifier by UNC folder.
	 * 
	 * @param uncFolder {@link String}
	 * @return {@link String}
	 */
	String getBatchClassIdentifierByUNCfolder(String uncFolder);

	/**
	 * API to get all batch class identifiers.
	 * 
	 * @return {@link List<String>}
	 */
	List<String> getAllBatchClassIdentifiers();

	/**
	 * API to get all batch classes on the basis of excluding the deleted batch class and on the basis of ascending or desending order
	 * of specified property.
	 * 
	 * @param isExcludeDeleted boolean
	 * @param isAsc boolean
	 * @param propertyName {@link String}
	 * @return List<{@link BatchClass}>
	 */
	List<BatchClass> getAllBatchClasses(boolean isExcludeDeleted, boolean isAsc, String propertyName);
	
	/**
	 * API to get all the UNC folders on the basis of excluding the deleted batch class.
	 * 
	 * @param isExcludeDeleted {@link Boolean}
	 * @return {@link List}<{@link String}>
	 */
	List<String> getAllUNCList(boolean isExcludeDeleted);
	
	/**
	 * API to fetch the system folder for the given batch class identifier.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @return {@link String} the system folder path.
	 */
	String getSystemFolderForBatchClassIdentifier(String batchClassIdentifier);
	
	/**
	 * API to the batch class with respect to the given batch class id and the roles provided.
	 * 
	 * @param userRoles {@link Set<{@link String}>}
	 * @param batchClassID {@link String}
	 * @return {@link BatchClass}
	 */
	BatchClass getBatchClassByUserRoles(Set<String> userRoles, String batchClassID);
}
