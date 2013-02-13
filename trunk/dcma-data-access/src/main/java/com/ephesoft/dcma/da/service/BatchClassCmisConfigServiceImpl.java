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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ephesoft.dcma.da.dao.BatchClassCmisConfigDao;
import com.ephesoft.dcma.da.domain.BatchClassCmisConfiguration;

/**
 * This service deals with Batch Class Cmis Configurations.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.da.service.BatchClassCmisConfigService
 */
@Service
public class BatchClassCmisConfigServiceImpl implements BatchClassCmisConfigService {

	/**
	 * batConfigDao {@link BatchClassCmisConfigDao}.
	 */
	@Autowired
	private BatchClassCmisConfigDao batConfigDao;

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchClassEmailConfigServiceImpl.class);

	/**
	 * An API to fetch all Cmis configurations by batch class id.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @return List<{@link BatchClassCmisConfiguration}>
	 */
	@Transactional(readOnly = true)
	@Override
	public List<BatchClassCmisConfiguration> getCmisConfigByBatchClassIdentifier(String batchClassIdentifier) {
		LOGGER.debug(batchClassIdentifier);
		return batConfigDao.getCmisConfigByBatchClassIdentifier(batchClassIdentifier);
	}

	/**
	 * An API to fetch all BatchClassCmisConfiguration by batch class id starting at firstIndex and maxResults is the total number of records.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param firstIndex int
	 * @param maxResults int
	 * @return List<{@link BatchClassCmisConfiguration}>
	 */
	@Transactional(readOnly = true)
	@Override
	public List<BatchClassCmisConfiguration> getCmisConfigByBatchClassIdentifier(String batchClassIdentifier, int firstIndex,
			int maxResults) {
		return batConfigDao.getCmisConfigByBatchClassIdentifier(batchClassIdentifier, firstIndex, maxResults);
	}

	/**
	 * An API to fetch Cmis Configuration by id.
	 * 
	 * @param emailConfigId int
	 * @return {@link BatchClassCmisConfiguration}
	 */
	@Transactional(readOnly = true)
	@Override
	public BatchClassCmisConfiguration getCmisConfigById(int emailConfigId) {
		return batConfigDao.get(emailConfigId);
	}

	/**
	 * An API to insert a new Cmis Config object.
	 * 
	 * @param emailConfiguration {@link BatchClassCmisConfiguration}
	 */
	@Transactional
	@Override
	public void insertCmisConfiguration(BatchClassCmisConfiguration emailConfiguration) {
		batConfigDao.create(emailConfiguration);
	}

	/**
	 * An API to remove the Cmis Config object.
	 * 
	 * @param emailConfiguration {@link BatchClassCmisConfiguration}
	 */
	@Transactional
	@Override
	public void removeCmisConfiguration(BatchClassCmisConfiguration emailConfiguration) {
		batConfigDao.remove(emailConfiguration);

	}

	/**
	 * An API to update the Cmis Config object.
	 * 
	 * @param emailConfiguration {@link BatchClassCmisConfiguration}
	 */
	@Transactional
	@Override
	public void updateCmisConfiguration(BatchClassCmisConfiguration emailConfiguration) {
		batConfigDao.saveOrUpdate(emailConfiguration);

	}

	/**
	 * An API to fetch all batch class Cmis configs.
	 * 
	 * @return List<{@link BatchClassCmisConfiguration}>
	 */
	@Override
	public List<BatchClassCmisConfiguration> getAllCmisConfigs() {
		return batConfigDao.getAllCmisConfigs();
	}

}
