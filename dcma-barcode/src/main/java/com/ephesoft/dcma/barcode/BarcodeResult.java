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

package com.ephesoft.dcma.barcode;

import com.ephesoft.dcma.barcode.BarcodeReader.BarcodeReaderTypes;

/**
 * This is a bean which stores the results of the scanning barcodes on each image file using Zxing barcode plugin.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.barcode.BarcodeReader
 * 
 */
public class BarcodeResult {

	/**
	 * x co-ordinate from page left to the top-left corner of barcode.
	 */
	private double x0Cord;

	/**
	 * y co-ordinate from page top to the top-left corner of barcode.
	 */
	private double y0Cord;

	/**
	 * x co-ordinate from page left to the bottom-right corner of barcode.
	 */
	private double x1Cord;

	/**
	 * y co-ordinate from page left to the top-left corner of barcode.
	 */
	private double y1Cord;

	/**
	 * The value on barcode.
	 */
	private String texts;

	/**
	 * The type of Barcode viz Code 39, QR, Datamatrix.
	 */
	private BarcodeReaderTypes barcodeType;

	/**
	 * getter for x0Cord.
	 * @return double
	 */
	public double getX0Cord() {
		return x0Cord;
	}

	/**
	 * setter for x0Cord.
	 * @param x0Cord double
	 */
	public void setX0Cord(final double x0Cord) {
		this.x0Cord = x0Cord;
	}

	/**
	 * getter for y0Cord.
	 * @return double
	 */
	public double getY0Cord() {
		return y0Cord;
	}

	/**
	 * setter for x0Cord.
	 * @param y0Cord double
	 */
	public void setY0Cord(final double y0Cord) {
		this.y0Cord = y0Cord;
	}

	/**
	 * getter for x1Cord.
	 * @return double
	 */
	public double getX1Cord() {
		return x1Cord;
	}

	/**
	 * setter for x1Cord.
	 * @param x1Cord double
	 */
	public void setX1Cord(final double x1Cord) {
		this.x1Cord = x1Cord;
	}

	/**
	 * getter for y1Cord.
	 * @return double
	 */
	public double getY1Cord() {
		return y1Cord;
	}

	/**
	 * setter for y1Cord.
	 * @param y1Cord double
	 */
	public void setY1Cord(final double y1Cord) {
		this.y1Cord = y1Cord;
	}

	/**
	 * getter for texts.
	 * @return {@link String}
	 */
	public String getTexts() {
		return texts;
	}

	/**
	 * setter for texts.
	 * @param texts {@link String}
	 */
	public void setTexts(final String texts) {
		this.texts = texts;
	}

	/**
	 * getter for barcodeType.
	 * @return {@link BarcodeReaderTypes}
	 */
	public BarcodeReaderTypes getBarcodeType() {
		return barcodeType;
	}

	/**
	 * setter for barcodeType.
	 * @param barcodeType {@link BarcodeReaderTypes}
	 */
	public void setBarcodeType(final BarcodeReaderTypes barcodeType) {
		this.barcodeType = barcodeType;
	}

}
