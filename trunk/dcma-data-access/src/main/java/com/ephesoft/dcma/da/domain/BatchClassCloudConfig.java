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

package com.ephesoft.dcma.da.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;

/**
 * The <code>BatchClassCloudConfig</code> is a Entity class for batch_class_cloud_configuration table used for configuring per batch
 * class processing limitation.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.model.common.AbstractChangeableEntity
 * @see com.ephesoft.dcma.core.model.common.AbstractEntity
 */
@Entity
@Table(name = "batch_class_cloud_config")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class BatchClassCloudConfig extends AbstractChangeableEntity implements Serializable {

	/**
	 * Serial Version Id of a class.
	 */
	private static final long serialVersionUID = 219540281290816508L;

	/**
	 * The batchClass {@link BatchClass} is the parent class for this class.
	 */
	@OneToOne
	@JoinColumn(name = "batch_class_id")
	private BatchClass batchClass;

	/**
	 * The noOfDays {@link Integer} is days limit for batch instance limit.
	 */
	@Column(name = "no_of_days")
	private Integer noOfDays;

	/**
	 * The batchInstanceLimit {@link Integer} is limit for batch instance.
	 */
	@Column(name = "batch_instance_limit")
	private Integer batchInstanceLimit;

	/**
	 * The batchInstanceImageLimit {@link Integer} is limit for batch instance image.
	 */
	@Column(name = "page_count")
	private Integer pageCount;

	/**
	 * The currentCounter {@link Integer} is current count of number of batch instances processed within days limit.
	 */
	@Column(name = "current_counter")
	private Integer currentCounter;

	/**
	 * lastReset Date.
	 */
	@Column(name = "last_reset")
	private Date lastReset;

	/**
	 * The <code>getBatchClass</code> method is a getter for batch class.
	 * 
	 * @return {@link BatchClass} parent batch class
	 */
	public BatchClass getBatchClass() {
		return batchClass;
	}

	/**
	 * The <code>setBatchClass</code> method is a setter for batch class.
	 * 
	 * @param batchClass {@link BatchClass} parent batch class
	 */
	public void setBatchClass(BatchClass batchClass) {
		this.batchClass = batchClass;
	}

	/**
	 * The <code>getNoOfDays</code> is a getter for no of days.
	 * 
	 * @return {@link Integer} no of days limit
	 */
	public Integer getNoOfDays() {
		return noOfDays;
	}

	/**
	 * The <code>setNoOfDays</code> is a setter for no of days.
	 * 
	 * @param noOfDays {@link Integer} no of days limit
	 */
	public void setNoOfDays(Integer noOfDays) {
		this.noOfDays = noOfDays;
	}

	/**
	 * The <code>getBatchInstanceLimit</code> is a getter for batch instance limit.
	 * 
	 * @return {@link Integer} batch instance limit
	 */
	public Integer getBatchInstanceLimit() {
		return batchInstanceLimit;
	}

	/**
	 * The <code>setBatchInstanceLimit</code> is a setter for batch instance limit.
	 * 
	 * @param batchInstanceLimit {@link Integer} batch instance limit
	 */
	public void setBatchInstanceLimit(Integer batchInstanceLimit) {
		this.batchInstanceLimit = batchInstanceLimit;
	}

	/**
	 * The <code>getPageCount</code> is a getter for page count.
	 * 
	 * @return {@link Integer} page count
	 */
	public Integer getPageCount() {
		return pageCount;
	}

	/**
	 * The <code>setPageCount</code> is a setter for page count.
	 * 
	 * @param pageCount {@link Integer} page count
	 */
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	/**
	 * The <code>getCurrentCounter</code> is a getter for current batch instance count.
	 * 
	 * @return {@link Integer} current batch instance count
	 */
	public Integer getCurrentCounter() {
		if (null != getNoOfDays()) {
			if (null != getLastReset()) {
				float localDaysCounter = (System.currentTimeMillis() - getLastReset().getTime())
						/ (ICommonConstants.TIME_IN_MILLISECOND * getNoOfDays());
				if (localDaysCounter >= getNoOfDays()) {
					float localCurrentDayCounter = (System.currentTimeMillis() - getCreationDate().getTime())
							/ (ICommonConstants.TIME_IN_MILLISECOND * getNoOfDays());
					int numberOfDaysIncrease = (int) (localCurrentDayCounter / getNoOfDays());
					lastReset = new Date((long) (getCreationDate().getTime() + numberOfDaysIncrease
							* ICommonConstants.TIME_IN_MILLISECOND));
					currentCounter = 0;
				}
			} else {
				float localDaysCounter = (System.currentTimeMillis() - getCreationDate().getTime())
						/ (ICommonConstants.TIME_IN_MILLISECOND * getNoOfDays());
				if (localDaysCounter >= getNoOfDays()) {
					int numberOfDaysIncrease = ((int) localDaysCounter / getNoOfDays());
					lastReset = new Date((long) (getCreationDate().getTime() + numberOfDaysIncrease
							* ICommonConstants.TIME_IN_MILLISECOND));
					currentCounter = 0;
				}
			}
		}
		return currentCounter;
	}

	/**
	 * The <code>setCurrentCounter</code> is a setter for current batch instance count.
	 * 
	 * @param currentCounter {@link Integer} current batch instance count
	 */
	public void setCurrentCounter(Integer currentCounter) {
		this.currentCounter = currentCounter;
	}

	/**
	 * To get Last Reset.
	 * @return Date
	 */
	public Date getLastReset() {
		return lastReset;
	}

	/**
	 * To set Last Reset.
	 * @param lastReset Date
	 */
	public void setLastReset(Date lastReset) {
		this.lastReset = lastReset;
	}
}
