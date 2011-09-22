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

package com.ephesoft.dcma.da.dao;

import java.util.List;

import com.ephesoft.dcma.core.dao.CacheableDao;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.Module;

public interface BatchClassModuleDao extends CacheableDao<BatchClassModule> {

	/**
	 * API to get the count of modules related to a batch class.
	 * 
	 * @param batchClassId
	 * @return Integer (count of modules)
	 */
	Integer countModules(String batchClassIdentifier);

	/**
	 * API to fetch List of modules corresponding to a batch class.
	 * 
	 * @param batchClassId
	 * @return List<Module>
	 */
	List<Module> getBatchClassModule(String batchClassIdentifier);

	/**
	 * API to fetch Modules starting from firstIndex and as many results as MaxResults from a batch class.
	 * 
	 * @param batchClassId
	 * @param firstIndex
	 * @param maxResults
	 * @return List<Module>
	 */
	List<Module> getModules(String batchClassIdentifier, final int firstIndex, final int maxResults);

	/**
	 * API to fetch the modules by name
	 * 
	 * @param batchClassIdentifier
	 * @param moduleName
	 * @return
	 */
	BatchClassModule getModuleByName(String batchClassIdentifier, String moduleName);

	/**
	 * API to fetch the modules by workflow name
	 * 
	 * @param batchClassIdentifier
	 * @param workflowName
	 * @return batch class module object
	 */
	BatchClassModule getModuleByWorkflowName(String batchClassIdentifier, String workflowName);

}
