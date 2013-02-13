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

package com.ephesoft.dcma.batch.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.batch.dao.PluginPropertiesDao;
import com.ephesoft.dcma.da.dao.BatchClassDynamicPluginConfigDao;
import com.ephesoft.dcma.da.dao.BatchClassPluginConfigDao;
import com.ephesoft.dcma.da.domain.BatchClassDynamicPluginConfig;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;

/**
 * This class is used to get the values of batch class object from database.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.batch.dao.PluginPropertiesDao
 * 
 */
@Repository("batchClassPluginPropertiesDao")
public class BatchClassPluginPropertiesDao implements PluginPropertiesDao {

	/**
	 * Instance of {@link BatchClassPluginConfigDao}.
	 */
	@Autowired
	private BatchClassPluginConfigDao batchClassPluginConfigDao;

	/**
	 * Instance of {@link BatchClassDynamicPluginConfigDao}.
	 */
	@Autowired
	private BatchClassDynamicPluginConfigDao batchClassDynamicPluginConfigDao;

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchInstancePluginPropertiesDao.class);

	/**
	 * boolean field to get if debug mode of logging is enabled.
	 */
	private static final boolean IS_DEBUG_ENABLE = LOGGER.isDebugEnabled();

	/**
	 * This method is to get the plugin properties.
	 * 
	 * @param batchClassIdentifier String.
	 * @return {@link BatchPluginPropertyContainer}
	 */
	public BatchPluginPropertyContainer getPluginProperties(final String batchClassIdentifier) {
		String loggingPrefix = BatchConstants.LOG_AREA + batchClassIdentifier;
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix + " : Executing getPluginProperties(String) API of BatchClassPluginPropertiesDao.");
		}
		final List<BatchClassPluginConfig> batchClassPluginConfigs = batchClassPluginConfigDao
				.getAllPluginPropertiesForBatchClass(batchClassIdentifier);
		final List<BatchClassDynamicPluginConfig> batchClassDynamicPluginConfigs = batchClassDynamicPluginConfigDao
				.getAllDynamicPluginPropertiesForBatchClass(batchClassIdentifier);
		final BatchPluginPropertyContainer container = new BatchPluginPropertyContainer(String.valueOf(batchClassIdentifier));
		container.populate(batchClassPluginConfigs);
		container.populateDynamicPluginConfigs(batchClassDynamicPluginConfigs);
		if (IS_DEBUG_ENABLE) {
			LOGGER.debug(loggingPrefix + " : Executed getPluginProperties(String) API of BatchClassPluginPropertiesDao successfully.");
		}
		return container;
	}

	/**
	 * This method is to clear the plugin properties.
	 * 
	 * @param batchClassIdentifier {@link String}.
	 */
	public void clearPluginProperties(final String batchInstanceIdentifier) {
		if (IS_DEBUG_ENABLE) {
			String loggingPrefix = BatchConstants.LOG_AREA + batchInstanceIdentifier;
			LOGGER
					.debug(loggingPrefix
							+ " : Executing clearPluginProperties(String) API of BatchClassPluginPropertiesDao. Throwing UnsupportedOperationException.");
		}
		throw new UnsupportedOperationException("Operation not supported.");
	}
}
