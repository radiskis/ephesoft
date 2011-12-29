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

package com.ephesoft.dcma.gwt.admin.bm.client.util;

import java.util.Comparator;

import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.core.common.DomainProperty;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.core.shared.BatchClassFieldDTO;

/**
 * @author Ephesoft 
 * Compares two BatchClassFieldDTOs based on property defined in order object
 */
public class BatchClassFieldComparator implements Comparator<Object>, AdminConstants {

	private final Order order;

	public BatchClassFieldComparator(final Order order) {
		this.order = order;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final Object batchClassfieldOne, final Object batchClassfieldTwo) {
		int isEqualOrGreater = 0;
		final BatchClassFieldDTO batchClassFieldDTOOne = (BatchClassFieldDTO) batchClassfieldOne;
		final BatchClassFieldDTO batchClassFieldDTOTwo = (BatchClassFieldDTO) batchClassfieldTwo;
		final Boolean isAsc = order.isAscending();
		final Object fieldPropertyOne = getProperty(order.getSortProperty(), batchClassFieldDTOOne);
		final Object fieldPropertyTwo = getProperty(order.getSortProperty(), batchClassFieldDTOTwo);
		if (order.getSortProperty().getProperty().equalsIgnoreCase(BATCH_CLASS_FIELD_DATATYPE)) {
			final DataType dataTypeOne = (DataType) batchClassFieldDTOOne.getDataType();
			final DataType dataTypeTwo = (DataType) batchClassFieldDTOTwo.getDataType();
			if (isAsc) {
				isEqualOrGreater = dataTypeOne.compareTo(dataTypeTwo);
			} else {
				isEqualOrGreater = dataTypeTwo.compareTo(dataTypeOne);
			}
		} else if (isIntegerProperty(fieldPropertyOne)) {
			// sort on field order number
			final Integer fieldOrderNumOne = Integer.parseInt(((String) fieldPropertyOne));
			final Integer fieldOrderNumTwo = Integer.parseInt(((String) fieldPropertyTwo));
			if (isAsc) {
				isEqualOrGreater = fieldOrderNumOne.compareTo(fieldOrderNumTwo);
			} else {
				isEqualOrGreater = fieldOrderNumTwo.compareTo(fieldOrderNumOne);
			}
		} else {
			// sort on some other batch class field property
			if (isAsc) {
				isEqualOrGreater = ((String) fieldPropertyOne).compareTo((String) fieldPropertyTwo);
			} else {
				isEqualOrGreater = ((String) fieldPropertyTwo).compareTo((String) fieldPropertyOne);
			}
		}
		return isEqualOrGreater;
	}

	/**
	 * If Integer property, return true
	 * 
	 * @param property
	 * @return
	 */
	public Boolean isIntegerProperty(final Object property) {
		Boolean isInteger = true;
		try {
			Integer.parseInt((String) property);
		} catch (Exception e) {
			isInteger = false;
		}
		return isInteger;

	}

	/**
	 * Getting the name of domain property to sort on
	 * 
	 * @param domainProperty
	 * @param documentTypeDTO
	 * @return
	 */
	public Object getProperty(final DomainProperty domainProperty, final BatchClassFieldDTO batchClassFieldDTO) {
		Object propObj = null;
		if (domainProperty.getProperty().equals(BATCH_CLASS_FIELD_NAME)) {
			propObj = batchClassFieldDTO.getName();
		} else if (domainProperty.getProperty().equals(BATCH_CLASS_FIELD_ORDER_NUMBER)) {
			propObj = batchClassFieldDTO.getFieldOrderNumber();
		} else if (domainProperty.getProperty().equals(BATCH_CLASS_FIELD_DESCRIPTION)) {
			propObj = batchClassFieldDTO.getDescription();
		}
		return propObj;
	}
}
