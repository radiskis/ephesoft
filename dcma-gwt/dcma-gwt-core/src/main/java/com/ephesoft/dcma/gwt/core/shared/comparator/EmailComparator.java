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
import com.ephesoft.dcma.gwt.core.shared.EmailConfigurationDTO;

/**
 * @author Ephesoft 
 * Compares two EmailConfigurationDTOs based on property defined in order object
 */
public class EmailComparator implements Comparator<Object> {

	private final Order order;

	private static final String EMAIL_USERNAME = "username";
	private static final String EMAIL_PASSWORD = "password";
	private static final String EMAIL_SERVER_NAME = "serverName";
	private static final String EMAIL_SERVER_TYPE = "serverType";
	private static final String EMAIL_FOLDER_NAME = "folderName";
	private static final String EMAIL_PORTNUMBER = "portNumber";
	
	public EmailComparator(final Order order) {

		this.order = order;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final Object EmailConfigurationOne, final Object EmailConfigurationTwo) {
		int isEqualOrGreater = 0;
		final EmailConfigurationDTO emailConfigurationDTOOne = (EmailConfigurationDTO) EmailConfigurationOne;
		final EmailConfigurationDTO emailConfigurationDTOTwo = (EmailConfigurationDTO) EmailConfigurationTwo;
		final Boolean isAsc = order.isAscending();
		if (order.getSortProperty().getProperty().equals(EMAIL_PORTNUMBER)) {

			final Integer portNumberOne = emailConfigurationDTOOne.getPortNumber();
			final Integer portNumberTwo = emailConfigurationDTOTwo.getPortNumber();

			if (isAsc) {
				isEqualOrGreater = portNumberOne.compareTo(portNumberTwo);
			} else {
				isEqualOrGreater = portNumberTwo.compareTo(portNumberOne);
			}

		}

		final String EmailConfigPropertyOne = getProperty(order.getSortProperty(), emailConfigurationDTOOne);
		final String EmailConfigPropertyTwo = getProperty(order.getSortProperty(), emailConfigurationDTOTwo);
		if (isAsc) {
			isEqualOrGreater = EmailConfigPropertyOne.compareTo(EmailConfigPropertyTwo);
		} else {
			isEqualOrGreater = EmailConfigPropertyTwo.compareTo(EmailConfigPropertyOne);
		}
		return isEqualOrGreater;
	}

	/**
	 * Getting the name of domain property to sort on
	 * 
	 * @param domainProperty
	 * @param emailConfigurationDTO
	 * @return
	 */
	public String getProperty(final DomainProperty domainProperty, final EmailConfigurationDTO emailConfigurationDTO) {
		String property = null;
		if (domainProperty.getProperty().equals(EMAIL_USERNAME)) {
			property = emailConfigurationDTO.getUserName();
		}

		else if (domainProperty.getProperty().equals(EMAIL_PASSWORD)) {
			property = emailConfigurationDTO.getPassword();
		}

		else if (domainProperty.getProperty().equals(EMAIL_SERVER_NAME)) {
			property = emailConfigurationDTO.getServerName();
		} else if (domainProperty.getProperty().equals(EMAIL_SERVER_TYPE)) {
			property = emailConfigurationDTO.getServerType();
		} else if (domainProperty.getProperty().equals(EMAIL_FOLDER_NAME)) {
			property = emailConfigurationDTO.getFolderName();
		}
		return property;
	}

}
