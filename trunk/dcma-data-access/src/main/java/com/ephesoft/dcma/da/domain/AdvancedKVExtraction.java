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

package com.ephesoft.dcma.da.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;

@Entity
@Table(name = "advanced_kv_extraction")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class AdvancedKVExtraction extends AbstractChangeableEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * PNG image name.
	 */
	@Column(name = "display_image_name", nullable = false)
	private String displayImageName;

	/**
	 * Tiff image Name.
	 */
	@Column(name = "image_name", nullable = false)
	private String imageName;

	/**
	 * Key X0 coordinate.
	 */
	@Column(name = "key_x0_coor", nullable = false)
	private Integer keyX0Coord;

	/**
	 * Key X1 Coordinate.
	 */
	@Column(name = "key_x1_coor", nullable = false)
	private Integer keyX1Coord;

	/**
	 * Key Y0 Coordinate.
	 */
	@Column(name = "key_y0_coor", nullable = false)
	private Integer keyY0Coord;

	/**
	 * Key Y1 Coordinate.
	 */
	@Column(name = "key_y1_coor", nullable = false)
	private Integer keyY1Coord;

	/**
	 * value X0 coordinate.
	 */
	@Column(name = "value_x0_coor", nullable = false)
	private Integer valueX0Coord;

	/**
	 * value X1 Coordinate.
	 */
	@Column(name = "value_x1_coor", nullable = false)
	private Integer valueX1Coord;

	/**
	 * value Y0 Coordinate.
	 */
	@Column(name = "value_y0_coor", nullable = false)
	private Integer valueY0Coord;

	/**
	 * value Y1 Coordinate.
	 */
	@Column(name = "value_y1_coor", nullable = false)
	private Integer valueY1Coord;

	public Integer getValueX0Coord() {
		return valueX0Coord;
	}

	public void setValueX0Coord(Integer valueX0Coord) {
		this.valueX0Coord = valueX0Coord;
	}

	public Integer getValueX1Coord() {
		return valueX1Coord;
	}

	public void setValueX1Coord(Integer valueX1Coord) {
		this.valueX1Coord = valueX1Coord;
	}

	public Integer getValueY0Coord() {
		return valueY0Coord;
	}

	public void setValueY0Coord(Integer valueY0Coord) {
		this.valueY0Coord = valueY0Coord;
	}

	public Integer getValueY1Coord() {
		return valueY1Coord;
	}

	public void setValueY1Coord(Integer valueY1Coord) {
		this.valueY1Coord = valueY1Coord;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Integer getKeyX0Coord() {
		return keyX0Coord;
	}

	public void setKeyX0Coord(Integer keyX0Coord) {
		this.keyX0Coord = keyX0Coord;
	}

	public Integer getKeyX1Coord() {
		return keyX1Coord;
	}

	public void setKeyX1Coord(Integer keyX1Coord) {
		this.keyX1Coord = keyX1Coord;
	}

	public Integer getKeyY0Coord() {
		return keyY0Coord;
	}

	public void setKeyY0Coord(Integer keyY0Coord) {
		this.keyY0Coord = keyY0Coord;
	}

	public Integer getKeyY1Coord() {
		return keyY1Coord;
	}

	public void setKeyY1Coord(Integer keyY1Coord) {
		this.keyY1Coord = keyY1Coord;
	}

	public String getDisplayImageName() {
		return displayImageName;
	}

	public void setDisplayImageName(String displayImageName) {
		this.displayImageName = displayImageName;
	}

}
