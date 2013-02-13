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

package com.ephesoft.dcma.core.common;

/**
 * The <code>UserType</code> is a enum representing Epheosft user type.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.common
 * 
 */
public enum UserType {
	
	/**
	 * LIMITED.
	 */
	LIMITED(0), 
	/**
	 * SHARED.
	 */
	SHARED(1),
	/**
	 * OTHERS.
	 */
	OTHERS(2);
	
	/**
	 * The userType is for holding Ephesoft user type.
	 */
	private int userType;
	
	/**
	 * Constructor to initialize ephesoft user type.
	 * 
	 * @param userType user type value
	 */
	private UserType(int userType) {
		this.userType = userType;
	}
	
	/**
	 * The <code>getUserType</code> is used to get the value
	 * of user type.
	 * 
	 * @return value of user type.
	 */
	public int getUserType(){
		return userType;
	}
}
