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

package com.ephesoft.dcma.barcodeextraction;

import com.ephesoft.dcma.barcodeextraction.BarcodeExtractionReader.BarcodeReaderTypes;

/**
 * This is a bean which stores the results of the scanning barcodes on each image file using Zxing barcode plug in.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.barcodeextraction.BarcodeExtractionReader
 */
public class BarcodeExtractionResult {

	/**
	 * x co-ordinate from page left to the top-left corner of barcode.
	 */
	private double x0Coordinate;

	/**
	 * y co-ordinate from page top to the top-left corner of barcode.
	 */
	private double y0Coordinate;

	/**
	 * x co-ordinate from page left to the bottom-right corner of barcode.
	 */
	private double x1Coordinate;

	/**
	 * y co-ordinate from page left to the top-left corner of barcode.
	 */
	private double y1Coordinate;

	/**
	 * The value on barcode.
	 */
	private String texts;

	/**
	 * The type of Barcode viz Code 39, QR, Datamatrix etc.
	 */
	private BarcodeReaderTypes barcodeType;

	/**
	 * getter for x0Coordinate.
	 * @return double
	 */
	public double getX0() {
		return x0Coordinate;
	}

	/**
	 * setter for x0Coordinate.
	 * @param x0Coordinate double
	 */
	public void setX0Coordinate(final double x0Coordinate) {
		this.x0Coordinate = x0Coordinate;
	}

	/**
	 * getter for y0Coordinate.
	 * @return double
	 */
	public double getY0Coordinate() {
		return y0Coordinate;
	}

	/**
	 * setter for y0Coordinate.
	 * @param y0Coordinate double
	 */
	public void setY0Coordinate(final double y0Coordinate) {
		this.y0Coordinate = y0Coordinate;
	}

	/**
	 * getter for x1Coordinate.
	 * @return double
	 */
	public double getX1Coordinate() {
		return x1Coordinate;
	}

	/**
	 * setter for x1Coordinate.
	 * @param x1Coordinate double
	 */
	public void setX1Coordinate(final double x1Coordinate) {
		this.x1Coordinate = x1Coordinate;
	}

	/**
	 * getter for y1Coordinate.
	 * @return double
	 */
	public double getY1Coordinate() {
		return y1Coordinate;
	}

	/**
	 * setter for y1Coordinate.
	 * @param y1Coordinate double
	 */
	public void setY1Coordinate(final double y1Coordinate) {
		this.y1Coordinate = y1Coordinate;
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
