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

package com.ephesoft.dcma.da.service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.exception.BatchAlreadyLockedException;
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
	 * An api to fetch BatchClass by id.
	 * 
	 * @param id Serializable
	 * @return BatchClass
	 */
	BatchClass get(Serializable id);

	void delete(Serializable id);

	/**
	 * An api to save or update the batch class.
	 * 
	 * @param batchClass BatchClass
	 */
	void saveOrUpdate(BatchClass batchClass);

	/**
	 * An api to fetch BatchClass by uncFolder name.
	 * 
	 * @param folderName String
	 * @return BatchClass
	 */
	BatchClass getBatchClassbyUncFolder(String folderName);

	/**
	 * An api to fetch BatchClass by batch Class name.
	 * 
	 * @param batchClassName String
	 * @return List<BatchClass>
	 */
	List<BatchClass> getBatchClassbyName(String batchClassName);

	/**
	 * An api to fetch BatchClass by batch Class processName.
	 * 
	 * @param processName String
	 * @return List<BatchClass>
	 */
	List<BatchClass> getBatchClassbyProcessName(String processName);

	/**
	 * This API will fetch all the batch classes.
	 * 
	 * @return List<BatchClass>
	 */
	public List<BatchClass> getAllBatchClasses();

	/**
	 * This API will fetch all the unlocked batch classes.
	 * 
	 * @return List<BatchClass>
	 */
	public List<BatchClass> getAllUnlockedBatchClasses();

	/**
	 * API to fetch a batch class by id.
	 * 
	 * @param batchClassId
	 * @return BatchClass
	 */
	public BatchClass getBatchClassByIdentifier(String batchClassIdentifier);

	/**
	 * API to acquire a batch on the basis of batchClassID and userName.
	 * 
	 * @param batchClassID
	 * @param userName
	 * @return BatchClass
	 */
	public BatchClass acquireBatchClass(String batchClassIdentifier, String currentUser) throws BatchAlreadyLockedException;

	/**
	 * Unlocks the currently acquired batch by the user (currentUser).
	 * 
	 * @param batchInstanceId
	 * @param currentUser
	 */
	public void unlockCurrentBatchClass(String batchClassIdentifier);

	/**
	 * Unlocks all the batches acquired by the user.
	 * 
	 * @param currentUser
	 */
	public void unlockAllBatchClassesForCurrentUser(String currentUser);

	/**
	 * Api to merge the batch class.
	 * 
	 * @param batchClass
	 */
	BatchClass merge(BatchClass batchClass, boolean isBatchClassDeleted);

	/**
	 * API to evict a batch class object.
	 * 
	 * @param batchClass
	 */
	public void evict(BatchClass batchClass);

	/**
	 * API to get the list of Batch Classes specifying startindex, no of results and sorting if any.
	 * 
	 * @param firstResult
	 * @param maxResults
	 * @param order
	 * @return List of batch class.
	 */
	public List<BatchClass> getBatchClassList(int firstResult, int maxResults, Order order);

	/**
	 * API to count all the batch classes.
	 * 
	 * @return count
	 */
	public int countAllBatchClassesExcludeDeleted();

	public BatchClass createBatchClass(BatchClass batchClass);

	/**
	 * This API will fetch all the batch classes on user role.
	 * 
	 * @return List<BatchClass>
	 */
	public List<BatchClass> getAllBatchClassesByUserRoles(Set<String> userRoles);

	public List<BatchClass> getAllBatchClassesExcludeDeleted();

	/**
	 * This API will fetch the batch class (eagerly loaded) on the basis of identifier.
	 * 
	 * @return List<BatchClass>
	 */
	public BatchClass getLoadedBatchClassByIdentifier(String batchClassIdentifier);

	/**
	 * This API will fetch the batch class (eagerly loaded) list excluding the deleted batch class
	 * 
	 * @return List<BatchClass>
	 */
	public List<BatchClass> getAllLoadedBatchClassExcludeDeleted();

	/**
	 * API to merge a batch class.
	 * 
	 * @param batchClass
	 * @return BatchClass
	 */
	public BatchClass merge(BatchClass batchClass);

	
	/** API to  get all the associated UNC folders with a workflow
	 * 
	 * @param workflowName {@link String}
	 * @return 
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
	 * @param batchClass
	 * @return
	 */
	public BatchClass createBatchClassWithoutWatch(BatchClass batchClass);
}
