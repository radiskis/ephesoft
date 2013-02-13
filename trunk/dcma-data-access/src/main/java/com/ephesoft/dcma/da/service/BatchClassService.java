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

package com.ephesoft.dcma.da.service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.exception.BatchAlreadyLockedException;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.BatchClass;

/**
 * This is a database service to read data required by Batch Class Service.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.da.service.BatchClassServiceImpl
 */
public interface BatchClassService {

	/**
	 * An API to fetch BatchClass by id.
	 * 
	 * @param identifier {@link Serializable}
	 * @return {@link BatchClass}
	 */
	BatchClass get(Serializable identifier);

	/**
	 * API to delete BatchClass by id.
	 * 
	 * @param identifier {@link Serializable}
	 */
	void delete(Serializable identifier);

	/**
	 * An API to save or update the batch class.
	 * 
	 * @param batchClass {@link BatchClass}
	 */
	void saveOrUpdate(BatchClass batchClass);

	/**
	 * An API to fetch BatchClass by uncFolder name.
	 * 
	 * @param folderName {@link String}
	 * @return {@link BatchClass}
	 */
	BatchClass getBatchClassbyUncFolder(String folderName);

	/**
	 * An API to fetch BatchClass by batch Class name.
	 * 
	 * @param batchClassName {@link String}
	 * @return {@link BatchClass}
	 */
	BatchClass getBatchClassbyName(String batchClassName);

	/**
	 * An API to fetch BatchClass by batch Class processName.
	 * 
	 * @param processName {@link String}
	 * @return {@link BatchClass}
	 */
	BatchClass getBatchClassbyProcessName(String processName);

	/**
	 * This API will fetch all the batch classes.
	 * 
	 * @return List<{@link BatchClass}>
	 */
	List<BatchClass> getAllBatchClasses();

	/**
	 * This API will fetch all the unlocked batch classes.
	 * 
	 * @return List<{@link BatchClass}>
	 */
	List<BatchClass> getAllUnlockedBatchClasses();

	/**
	 * API to fetch a batch class by id.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @return {@link BatchClass}
	 */
	BatchClass getBatchClassByIdentifier(String batchClassIdentifier);

	/**
	 * API to acquire a batch on the basis of batchClassID and userName.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param currentUser {@link String}
	 * @return {@link BatchClass}
	 * @throws BatchAlreadyLockedException in case of error
	 */
	BatchClass acquireBatchClass(String batchClassIdentifier, String currentUser) throws BatchAlreadyLockedException;

	/**
	 * Unlocks the currently acquired batch by the user (currentUser).
	 * 
	 * @param batchClassIdentifier {@link String}
	 */
	void unlockCurrentBatchClass(String batchClassIdentifier);

	/**
	 * Unlocks all the batches acquired by the user.
	 * 
	 * @param currentUser {@link String}
	 */
	void unlockAllBatchClassesForCurrentUser(String currentUser);

	/**
	 * API to merge the batch class.
	 * 
	 * @param batchClass {@link BatchClass}
	 * @param isBatchClassDeleted boolean
	 * @return {@link BatchClass}
	 */ 
	BatchClass merge(BatchClass batchClass, boolean isBatchClassDeleted);

	/**
	 * API to evict a batch class object.
	 * 
	 * @param batchClass {@link BatchClass}
	 */
	void evict(BatchClass batchClass);

	/**
	 * API to get the list of Batch Classes specifying start index, no of results and sorting if any.
	 * 
	 * @param firstResult int
	 * @param maxResults int
	 * @param order {@link Order}
	 * @param userRoles Set<{@link BatchClass}>
	 * @return List<{@link BatchClass}>
	 */
	List<BatchClass> getBatchClassList(int firstResult, int maxResults, Order order, Set<String> userRoles);

	/**
	 * API to count all the batch classes. * @param userRoles
	 * 
	 * @param userRoles Set<String>
	 * @return count int
	 */
	int countAllBatchClassesExcludeDeleted(Set<String> userRoles);

	/**
	 * API to create batch class.
	 * 
	 * @param batchClass {@link BatchClass}
	 * @return {@link BatchClass}
	 */
	BatchClass createBatchClass(BatchClass batchClass);

	/**
	 * This API will fetch all the batch classes on user role.
	 * 
	 * @param userRoles Set<String>
	 * @return List<{@link BatchClass}>
	 */
	List<BatchClass> getAllBatchClassesByUserRoles(Set<String> userRoles);

	/**
	 * API to get all batch classes excluding the one's deleted.
	 * 
	 * @return List<{@link BatchClass}>
	 */
	List<BatchClass> getAllBatchClassesExcludeDeleted();

	/**
	 * This API will fetch the batch class (eagerly loaded) on the basis of identifier.
	 * 
	 * @param batchClassIdentifier String
	 * @return List<{@link BatchClass}>
	 */
	BatchClass getLoadedBatchClassByIdentifier(String batchClassIdentifier);

	/**
	 * This API will fetch the batch class (eagerly loaded) list excluding the deleted batch class.
	 * 
	 * @return List<{@link BatchClass}>
	 */
	List<BatchClass> getAllLoadedBatchClassExcludeDeleted();

	/**
	 * API to merge a batch class.
	 * 
	 * @param batchClass {@link BatchClass}
	 * @return {@link BatchClass}
	 */
	BatchClass merge(BatchClass batchClass);

	/**
	 * API to get all the associated UNC folders with a workflow.
	 * 
	 * @param workflowName {@link String}
	 * @return List<{@link String}>
	 */
	List<String> getAssociatedUNCList(String workflowName);

	/**
	 * API to get the batch class loaded with batch class modules configurations.
	 * 
	 * @param batchClassIdentifier(@link String)
	 * @return {@link BatchClass}
	 */
	BatchClass getLoadedBatchClassForImport(String batchClassIdentifier);

	/**
	 * API to get the batch class loaded by UNC folder path.
	 * 
	 * @param folderName {@link String}
	 * @return {@link BatchClass}
	 */
	BatchClass getLoadedBatchClassByUNC(String folderName);

	/**
	 * API to get the batch class loaded by UNC name.
	 * 
	 * @param batchClassName {@link String}
	 * @return {@link BatchClass}
	 */
	BatchClass getBatchClassByNameIncludingDeleted(String batchClassName);

	/**
	 * API to create batch class without adding watch to it.
	 * 
	 * @param batchClass {@link BatchClass}
	 * @return {@link BatchClass}
	 */
	BatchClass createBatchClassWithoutWatch(BatchClass batchClass);

	/**
	 * API to get loaded batch class by workflow name.
	 * 
	 * @param batchClassName {@link String}
	 * @return {@link BatchClass}
	 */
	BatchClass getLoadedBatchClassByNameIncludingDeleted(String batchClassName);

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
	 * @return List<{@link String}>
	 */
	List<String> getAllBatchClassIdentifier();

	/**
	 * API to get all batch classes on the basis of excluding the deleted batch class and on the basis of ascending or desending order
	 * of specified property.
	 * 
	 * @param isExcludeDeleted boolean
	 * @param isAsc boolean
	 * @param propertyName String
	 * @return List<{@link BatchClass}>
	 */
	List<BatchClass> getAllBatchClasses(boolean isExcludeDeleted, boolean isAsc, String propertyName);

	/**
	 * API to get all the UNC folders on the basis of excluding the deleted batch class.
	 * 
	 * @param isExcludeDeleted boolean
	 * @return List<{@link String}>
	 */
	List<String> getAllUNCList(boolean isExcludeDeleted);

	/**
	 * API to copy the batch class with given id and create a new batch class with the details provided as arguments . This API also
	 * creates a new batch class group with given name (if not null) and assign that group to this newly created batch class
	 * 
	 * @param batchClassId{@link String}
	 * @param batchClassName{@link String}
	 * @param batchClassDesc{@link String}
	 * @param batchClassGroup{@link String}
	 * @param batchClassPriority{@link String}
	 * @param uncFolder{@link String}
	 * @param isConfigureExportFolder boolean
	 * @return {@link BatchClass}
	 * @throws DCMAApplicationException in case of error
	 */
	BatchClass copyBatchClass(String batchClassId, String batchClassName, String batchClassDesc, String batchClassGroup,
			String batchClassPriority, String uncFolder, boolean isConfigureExportFolder) throws DCMAApplicationException;

	/**
	 * API to fetch the system folder for the given batch class identifier.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @return {@link String} the system folder path.
	 */
	String getSystemFolderForBatchClassIdentifier(String batchClassIdentifier);
	
	/**
	 * This API will fetch the batch classes on user role if any.
	 * 
	 * @param userRoles Set<String>
	 * @param batchClassID String
	 * @return (@link BatchClass)
	 */
	BatchClass getBatchClassByUserRoles(Set<String> userRoles, String batchClassID);
}
