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

package com.ephesoft.dcma.core.service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.jdbc.SimpleJdbcTestUtils;

/**
 * Class to execute DB script.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see org.springframework.jdbc.core.simple.SimpleJdbcTemplate
 */
public class DBScriptExecuter {
	
	/**
	 * LOGGER to print the logging information.
	 */
	protected static final Logger LOG = LoggerFactory.getLogger(DBScriptExecuter.class);
	
	/**
	 * simpleJdbcTemplate SimpleJdbcTemplate.
	 */
	private SimpleJdbcTemplate simpleJdbcTemplate;
	
	/**
	 * dataSource DataSource.
	 */
	private DataSource dataSource;
	
	/**
	 * For post initialization.
	 */
	@PostConstruct
	public void postInitialize() {
		this.simpleJdbcTemplate = new SimpleJdbcTemplate(this.getDataSource());
	}
	
	/**
	 * Execute method.
	 * @param resource Resource
	 */
	public void execute(Resource resource) {
		SimpleJdbcTestUtils.executeSqlScript(this.simpleJdbcTemplate, new EncodedResource(resource, "UTF-8"), true);
	}
	
	/**
	 * Execute method.
	 * @param resourceLoc String
	 */
	public void execute(String resourceLoc) {
		Resource resource = new ClassPathResource(resourceLoc, this.getClass());
		SimpleJdbcTestUtils.executeSqlScript(this.simpleJdbcTemplate, new EncodedResource(resource, "UTF-8"), false);
	}
	
	/**
	 * To set Data Source.
	 * @param dataSource DataSource
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	/**
	 * To get Data Source.
	 * @return DataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}
}
