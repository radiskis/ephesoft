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

package com.ephesoft.dcma.gwt.core.shared.comparator;

import java.util.Comparator;

import com.ephesoft.dcma.core.common.DomainProperty;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.da.property.ModuleProperty;
import com.ephesoft.dcma.gwt.core.shared.BatchClassModuleDTO;

/**
 * @author Ephesoft Compares two BatchClassModuleDTOs based on property defined in order object
 */
public class ModuleComparator implements Comparator<Object> {

	private final Order order;
	/*String MODULE_NAME = "name";
	String MODULE_DESCRIPTION = "description";*/

	public ModuleComparator(final Order order) {

		this.order = order;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final Object moduleOne, final Object moduleTwo) {
		int isEqualOrGreater = 0;
		final BatchClassModuleDTO batchClassModuleDTOOne = (BatchClassModuleDTO) moduleOne;
		final BatchClassModuleDTO batchClassModuleDTOTwo = (BatchClassModuleDTO) moduleTwo;
		final Boolean isAsc = order.isAscending();
		if (!order.getSortProperty().getProperty().equals(ModuleProperty.ORDER.getProperty())) {
			isEqualOrGreater = compareStringValue(batchClassModuleDTOOne, batchClassModuleDTOTwo, isAsc);
		} else {
			isEqualOrGreater = compareIntValue(batchClassModuleDTOOne, batchClassModuleDTOTwo, isAsc);
		}
		return isEqualOrGreater;
	}

	/**
	 * @param batchClassModuleDTOOne
	 * @param batchClassModuleDTOTwo
	 * @param isAsc
	 * @return
	 */
	private int compareStringValue(final BatchClassModuleDTO batchClassModuleDTOOne, final BatchClassModuleDTO batchClassModuleDTOTwo,
			final Boolean isAsc) {
		int isEqualOrGreater;
		final String modulePropertyOne = getProperty(order.getSortProperty(), batchClassModuleDTOOne);
		final String modulePropertyTwo = getProperty(order.getSortProperty(), batchClassModuleDTOTwo);
		if (isAsc) {
			isEqualOrGreater = modulePropertyOne.compareTo(modulePropertyTwo);
		} else {
			isEqualOrGreater = modulePropertyTwo.compareTo(modulePropertyOne);
		}
		return isEqualOrGreater;
	}

	/**
	 * @param batchClassModuleDTOOne
	 * @param batchClassModuleDTOTwo
	 * @param isAsc
	 * @return
	 */
	private int compareIntValue(final BatchClassModuleDTO batchClassModuleDTOOne, final BatchClassModuleDTO batchClassModuleDTOTwo,
			final Boolean isAsc) {
		int isEqualOrGreater;
		final Integer modulePropertyOne = batchClassModuleDTOOne.getOrderNumber();
		final Integer modulePropertyTwo = batchClassModuleDTOTwo.getOrderNumber();
		if (isAsc) {
			isEqualOrGreater = modulePropertyOne.compareTo(modulePropertyTwo);
		} else {
			isEqualOrGreater = modulePropertyTwo.compareTo(modulePropertyOne);
		}
		return isEqualOrGreater;
	}

	/**
	 * Getting the name of domain property to sort on
	 * 
	 * @param domainProperty
	 * @param batchClassModuleDTO
	 * @return
	 */
	public String getProperty(final DomainProperty domainProperty, final BatchClassModuleDTO batchClassModuleDTO) {
		String property = null;
		if (domainProperty.getProperty().equals(ModuleProperty.NAME.getProperty())) {
			property = batchClassModuleDTO.getModule().getName();
		} else if (domainProperty.getProperty().equals(ModuleProperty.DESCRIPTION.getProperty())) {
			property = batchClassModuleDTO.getModule().getDescription();
		} else if (domainProperty.getProperty().equals(ModuleProperty.ORDER.getProperty())) {
			property = String.valueOf(batchClassModuleDTO.getOrderNumber());
		}
		return property;
	}

}
