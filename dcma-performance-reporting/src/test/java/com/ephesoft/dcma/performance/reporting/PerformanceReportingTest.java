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

package com.ephesoft.dcma.performance.reporting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.common.WorkflowType;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.performance.reporting.domain.ReportDisplayData;
import com.ephesoft.dcma.performance.reporting.service.ReportDataService;

/**
 * This is Junit test for ReportDataService. It contains three positive test cases for generating reports for system statistics,batches
 * by workflow and batches by user respectively.
 * 
 * @author Ephesoft
 * @see com.ephesoft.dcma.performance.reporting.AbstractPerformanceReportingTest
 * 
 */
public class PerformanceReportingTest extends AbstractPerformanceReportingTest {

	/**
	 * String constants.
	 */
	private static final String PROP_FILE_REPORTING_TEST = "test.properties";
	/**
	 * String constants.
	 */
	private static final String TESTCASE3_USER_NAME = "testcase3.user.name";
	
	/**
	 *  Variable for range of records per page.
	 */
	private static final int RANGE = 10;
	/**
	 * Variable for property file.
	 */
	private transient Properties prop = new Properties();

	/**
	 * Instance of ReportDataService.
	 */
	@Autowired
	private transient ReportDataService reportDataService;
	/**
	 * Instance of BatchClassService.
	 */
	@Autowired
	private transient BatchClassService batchClassService;
	/**
	 * Variable for list of all batch classes for reports.
	 */
	private final transient List<String> batchClassNameList = new ArrayList<String>();

	/**
	 * This method initializes the resources for test.
	 */
	@Before
	public void setUp() {
		boolean result = false;
		List<BatchClass> batchClassList = new ArrayList<BatchClass>();
		try {
			prop.load(PerformanceReportingTest.class.getClassLoader().getResourceAsStream(PROP_FILE_REPORTING_TEST));
		} catch (IOException e) {
			assertTrue(e.getMessage(), result);
		}
		batchClassList = batchClassService.getAllBatchClasses();
		for (BatchClass batchClass : batchClassList) {
			batchClassNameList.add(batchClass.getIdentifier());
		}
	}

	/**
	 * This method tests the report generation for system statistics.
	 */
	@Test
	public void testSystemstatistics() {
		boolean resultEx = false;
		Date startDate = new Date();
		Date endDate = new Date();
		try {
			List<Integer> result = reportDataService.getSystemStatistics(endDate, startDate);
			if (null == result) {
				throw new DCMAException();
			}
		} catch (DCMAException e) {
			assertTrue(e.getMessage(), resultEx);
		} catch (Exception e) {
			assertTrue(e.getMessage(), resultEx);
		}

	}

	/**
	 * This method test the report generation for batches by workflow.
	 */
	@Test
	public void testReportByWorkFlow() {
		boolean resultEx = false;
		Date startDate = new Date();
		Date endDate = new Date();
		Order order = new Order();
		WorkflowType workflowType = WorkflowType.WORKFLOW;
		try {
			List<ReportDisplayData> reportData = reportDataService.getReportByWorkflow(batchClassNameList, workflowType, endDate,
					startDate, 1, RANGE, order);
			if (null == reportData) {
				throw new DCMAException();
			}
		} catch (DCMAException e) {
			assertTrue(e.getMessage(), resultEx);
		}

	}

	/**
	 * This method test the report generation for batches by user.
	 */
	@Test
	public void testReportByUser() {
		boolean result = false;
		Date startDate = new Date();
		Date endDate = new Date();
		Order order = new Order();
		String userName = prop.getProperty(TESTCASE3_USER_NAME);
		try {
			List<ReportDisplayData> reportData = reportDataService.getReportByUser(batchClassNameList, userName, endDate, startDate,
					1, RANGE, order);
			if (null == reportData) {
				throw new DCMAException();
			}
		} catch (DCMAException e) {
			assertTrue(e.getMessage(), result);
		}

	}

}
