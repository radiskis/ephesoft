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

package com.ephesoft.dcma.core.threadpool;

import com.ephesoft.dcma.core.exception.DCMAApplicationException;

/**
 * Class to get and set dcma Application Exception object.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.exception.DCMAApplicationException
 *
 */
public abstract class AbstractRunnable implements Runnable {

	/**
	 * completed boolean.
	 */
	private boolean completed;

	/**
	 * started boolean.
	 */
	private boolean started;

	/**
	 * dcmaApplicationException DCMAApplicationException.
	 */
	private DCMAApplicationException dcmaApplicationException;

	/**
	 * To check whether completed or not.
	 * @return boolean
	 */
	public boolean isCompleted() {
		return completed;
	}

	/**
	 * To set completed.
	 * @param completed boolean
	 */
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	/**
	 * To get Dcma Application Exception.
	 * @return DCMAApplicationException
	 */
	public DCMAApplicationException getDcmaApplicationException() {
		return dcmaApplicationException;
	}

	/**
	 * To set Dcma Application Exception.
	 * @param dcmaApplicationException DCMAApplicationException
	 */
	public void setDcmaApplicationException(DCMAApplicationException dcmaApplicationException) {
		this.dcmaApplicationException = dcmaApplicationException;
	}

	/**
	 * To set Started.
	 * @param started boolean
	 */
	public void setStarted(boolean started) {
		this.started = started;
	}

	/**
	 * To check whether started or not.
	 * @return boolean
	 */
	public boolean isStarted() {
		return started;
	}
}
