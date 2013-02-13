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

package com.ephesoft.dcma.gwt.core.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The <code>BatchClassCloudConfigDTO</code> class represents a data transfer
 * object for batch class limit validation data.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.shared
 * 
 */
public class BatchClassCloudConfigDTO implements IsSerializable {

	/**
	 * The batchInstanceCounter {@link Integer} holds data for counter keeping
	 * count of number of batch instances created for the days limit.
	 */
	private Integer batchInstanceCounter;

	/**
	 * The batchInstanceLimit {@link Integer} holds data the limit for batch
	 * instances allowed per batch class for days limit.
	 */
	private Integer batchInstanceLimit;

	/**
	 * The batchInstanceImageLimit {@link Integer} holds data the limit for
	 * batch instances image allowed per batch instance.
	 */
	private Integer batchInstanceImageLimit;

	/**
	 * The <code>getBatchInstanceCounter</code> method is a getter for getting
	 * batch instance counter.
	 * 
	 * @return {@link Integer} batch instance counter
	 */
	public Integer getBatchInstanceCounter() {
		return batchInstanceCounter;
	}

	/**
	 * The <code>setBatchInstanceCounter</code> method is a setter for batch
	 * instance counter.
	 * 
	 * @param batchInstanceCounter
	 *            {@link Integer} updated batch instance counter.
	 */
	public void setBatchInstanceCounter(Integer batchInstanceCounter) {
		this.batchInstanceCounter = batchInstanceCounter;
	}

	/**
	 * The <code>getBatchInstanceLimit</code> method is a getter for getting
	 * batch instance limit.
	 * 
	 * @return {@link Integer} batch instance limit
	 */
	public Integer getBatchInstanceLimit() {
		return batchInstanceLimit;
	}

	/**
	 * The <code>setBatchInstanceLimit</code> method is a setter for batch
	 * instance limit.
	 * 
	 * @param batchInstanceLimit
	 *            {@link Integer} updated batch instance limit.
	 */
	public void setBatchInstanceLimit(Integer batchInstanceLimit) {
		this.batchInstanceLimit = batchInstanceLimit;
	}

	/**
	 * The <code>getBatchInstanceImageLimit</code> method is a getter for
	 * getting batch instance image limit.
	 * 
	 * @return {@link Integer} batch instance image limit
	 */
	public Integer getBatchInstanceImageLimit() {
		return batchInstanceImageLimit;
	}

	/**
	 * The <code>setBatchInstanceImageLimit</code> method is a setter for batch
	 * instance image limit.
	 * 
	 * @param batchInstanceImageLimit
	 *            {@link Integer} updated batch instance image limit.
	 */
	public void setBatchInstanceImageLimit(Integer batchInstanceImageLimit) {
		this.batchInstanceImageLimit = batchInstanceImageLimit;
	}
}
