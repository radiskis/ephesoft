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

import com.ephesoft.dcma.da.dao.PluginConfigSampleValueDao;
import com.ephesoft.dcma.da.domain.PluginConfigSampleValue;

/**
 * This is a database service to get the plugin config sample values.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.da.service.PluginConfigSampleValueService
 */
@Service
public class PluginConfigSampleValueServiceImpl implements PluginConfigSampleValueService {

	/**
	 * LOGGER to print the Logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PluginConfigSampleValueServiceImpl.class);

	/**
	 * pluginConfigSampleValueDao PluginConfigSampleValueDao.
	 */
	@Autowired
	private PluginConfigSampleValueDao pluginConfigSampleValueDao;

	/**
	 * API to create a new plugin config sample value.
	 * 
	 * @param pluginConfigSampleValue {@link PluginConfigSampleValue}
	 */
	@Override
	@Transactional(readOnly = false)
	public void createNewPluginConfigSampleValue(PluginConfigSampleValue pluginConfigSampleValue) {

		LOGGER.info("Creating new Plugin config sample value: " + pluginConfigSampleValue.getSampleValue());
		pluginConfigSampleValueDao.create(pluginConfigSampleValue);
	}	
	
	/**
	 * API to get list sample values for plugin config id.
	 * 
	 * @param pluginConfigId {@link Long}  
	 * @return List<PluginConfigSampleValue> 
	 */
	@Override
	public List<PluginConfigSampleValue> getAllPluginConfigSampleValuesForPluginConfigId(Long pluginConfigId) {
		LOGGER.info("Retrieving list of plugin config sample for plugin config id: " + pluginConfigId);
		return pluginConfigSampleValueDao.getAllPluginConfigSampleValuesForPluginConfigId(pluginConfigId);
	}

}
