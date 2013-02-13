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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ephesoft.dcma.core.EphesoftProperty;
import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;

/**
 * Entity class for page_type.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.model.common.AbstractChangeableEntity
 */
@Entity
@Table(name = "page_type")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class PageType extends AbstractChangeableEntity implements Serializable {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * docType {@link DocumentType}.
	 */
	@ManyToOne
	@JoinColumn(name = "document_type_id")
	private DocumentType docType;

	/**
	 * name String.
	 */
	@Column(name = "page_type_name")
	private String name;

	/**
	 * description String.
	 */
	@Column(name = "page_type_description")
	private String description;

	/**
	 * identifier String.
	 */
	@Column(name = "identifier")
	private String identifier;

	/**
	 * To get Doc Type.
	 * @return DocumentType
	 */
	public DocumentType getDocType() {
		return docType;
	}

	/**
	 * To set Doc Type.
	 * @param docType DocumentType
	 */  
	public void setDocType(DocumentType docType) {
		this.docType = docType;
	}

	/**
	 * To get name.
	 * @return String
	 */ 
	public String getName() {
		return name;
	}

	/**
	 * To set name.
	 * @param name String
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * To get Description.
	 * @return String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * To set Description.
	 * @param description String
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * To get Identifier.
	 * @return String
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * To set Identifier.
	 * @param identifier String
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	/**
	 * To add variables other than in pojo.
	 */
	public void postPersist() {
		super.postPersist();
		this.identifier = EphesoftProperty.PAGE_TYPE.getProperty() + Long.toHexString(this.getId()).toUpperCase();
	}

}
