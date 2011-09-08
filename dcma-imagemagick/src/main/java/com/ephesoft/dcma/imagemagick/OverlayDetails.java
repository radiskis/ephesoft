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

package com.ephesoft.dcma.imagemagick;

import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;

public class OverlayDetails {

	/**
	 * Document Id
	 */
	private String documentId;
	/**
	 * Field Name
	 */
	private String fieldName;
	/**
	 * Document Index.
	 */
	private int documentIndex;
	/**
	 * Field Index.
	 */
	private int fieldIndex;
	/**
	 * Page Id.
	 */
	private String pageID;
	/**
	 * OCR FileName.
	 */
	private String ocrFileName;
	/**
	 * Alternate Vlaue.
	 */
	private boolean alternateValue;
	/**
	 * Index.
	 */
	private int alternateValueIndex;
	/**
	 * OCR File Path
	 */
	private String ocrFilePath;
	/**
	 * Overlayed File Name.
	 */
	private String overlayedFileName;
	/**
	 * Overlayed File Path.
	 */
	private String overlayedFilePath;
	/**
	 * Coordinates
	 */
	private CoordinatesList coordinatesList;
	/**
	 * Field Value.
	 */
	private String fieldValue;

	/**
	 * This is the getter for documentId.
	 * 
	 * @return documentId
	 */
	public String getDocumentId() {
		return documentId;
	}

	/**
	 * This is the setter for documentId.
	 * 
	 * @param documentId
	 */
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	/**
	 * This is the getter for fieldName.
	 * 
	 * @return fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * This is the setter for fieldName.
	 * 
	 * @param fieldName
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * This is the getter for documentIndex.
	 * 
	 * @return documentIndex
	 */
	public int getDocumentIndex() {
		return documentIndex;
	}

	/**
	 * This is the setter for documentIndex.
	 * 
	 * @param documentIndex
	 */
	public void setDocumentIndex(int documentIndex) {
		this.documentIndex = documentIndex;
	}

	/**
	 * This is the getter for fieldIndex.
	 * 
	 * @return fieldIndex
	 */
	public int getFieldIndex() {
		return fieldIndex;
	}

	/**
	 * This is the setter for fieldIndex.
	 * 
	 * @param fieldIndex
	 */
	public void setFieldIndex(int fieldIndex) {
		this.fieldIndex = fieldIndex;
	}

	/**
	 * This is a getter for alternateValue.
	 * 
	 * @return alternateValue
	 */
	public boolean isAlternateValue() {
		return alternateValue;
	}

	/**
	 * This is the setter for alternateValue.
	 * 
	 * @param alternateValue
	 */
	public void setAlternateValue(boolean alternateValue) {
		this.alternateValue = alternateValue;
	}

	/**
	 * This is the getter for alternateValueIndex.
	 * 
	 * @return alternateValueIndex
	 */
	public int getAlternateValueIndex() {
		return alternateValueIndex;
	}

	/**
	 * This is the setter for alternateValueIndex.
	 * 
	 * @param alternateValueIndex
	 */
	public void setAlternateValueIndex(int alternateValueIndex) {
		this.alternateValueIndex = alternateValueIndex;
	}

	/**
	 * This is the getter for overlayedFilePath.
	 * 
	 * @return overlayedFilePath
	 */
	public String getOverlayedFilePath() {
		return overlayedFilePath;
	}

	/**
	 * This is the setter for overlayedFilePath.
	 * 
	 * @param overlayedFilePath
	 */
	public void setOverlayedFilePath(String overlayedFilePath) {
		this.overlayedFilePath = overlayedFilePath;
	}

	/**
	 * This is the getter for coordinates.
	 * 
	 * @return coordinates
	 */

	/**
	 * This is the setter for pageID.
	 * 
	 * @param pageID
	 */
	public void setPageID(String pageID) {
		this.pageID = pageID;
	}

	/**
	 * This is the getter for pageID.
	 * 
	 * @return pageID
	 */
	public String getPageID() {
		return pageID;
	}

	/**
	 * This is the setter for ocrFileName.
	 * 
	 * @param ocrFileName
	 */
	public void setOcrFileName(String ocrFileName) {
		this.ocrFileName = ocrFileName;
	}

	/**
	 * This is the getter for ocrFileName.
	 * 
	 * @return ocrFileName
	 */
	public String getOcrFileName() {
		return ocrFileName;
	}

	/**
	 * This is the setter for ocrFilePath.
	 * 
	 * @param ocrFilePath
	 */
	public void setOcrFilePath(String ocrFilePath) {
		this.ocrFilePath = ocrFilePath;
	}

	/**
	 * This is the getter for ocrFilePath.
	 * 
	 * @return ocrFilePath
	 */
	public String getOcrFilePath() {
		return ocrFilePath;
	}

	/**
	 * This is the setter for overlayedFileName.
	 * 
	 * @param overlayedFileName
	 */
	public void setOverlayedFileName(String overlayedFileName) {
		this.overlayedFileName = overlayedFileName;
	}

	/**
	 * This is the getter for overlayedFileName.
	 * 
	 * @return overlayedFileName
	 */
	public String getOverlayedFileName() {
		return overlayedFileName;
	}

	/**
	 * This is the setter for fieldValue.
	 * 
	 * @param fieldValue
	 */
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	/**
	 * This is the getter for fieldValue.
	 * 
	 * @return fieldValue
	 */
	public String getFieldValue() {
		return fieldValue;
	}

	/**
	 * @return the coordinatesList
	 */
	public CoordinatesList getCoordinatesList() {
		return coordinatesList;
	}

	/**
	 * @param coordinatesList the coordinatesList to set
	 */
	public void setCoordinatesList(CoordinatesList coordinatesList) {
		this.coordinatesList = coordinatesList;
	}

}
