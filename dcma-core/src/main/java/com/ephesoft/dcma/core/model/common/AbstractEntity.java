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

package com.ephesoft.dcma.core.model.common;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * Abstract superclass for simple entities implementing {@link DomainEntity}. This class provides {@code id} and {@code creationDate}
 * fields.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see javax.persistence.MappedSuperclass
 */
@MappedSuperclass
public class AbstractEntity implements DomainEntity {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * id long.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;

	/**
	 * creationDate Date.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="creation_date", nullable = false, updatable=false)
	private Date creationDate = new Date();

	/**
	 * entityState EntityState.
	 */
	@Transient
	private EntityState entityState;

	/**
	 * To get id.
	 * @return the id
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * To set id.
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * To get creation date.
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return this.creationDate;
	}

	/**
	 * To set creation date.
	 * @param creationDate Date
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * To get entity name.
	 * @return EntityState
	 */
	@Override
	public EntityState getEntityState() {
		return entityState;
	}

	/**
	 * To set entity name.
	 * @param entityState EntityState
	 */
	@Override
	public void setEntityState(EntityState entityState) {
		this.entityState =  entityState;
	}
	
	/**
	 * To persist values other than in pojo.
	 */
	public void postPersist() {
		// Explicitly kept the method as empty
	}
}
