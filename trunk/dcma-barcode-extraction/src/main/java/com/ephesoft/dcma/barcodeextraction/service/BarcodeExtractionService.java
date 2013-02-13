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

package com.ephesoft.dcma.barcodeextraction.service;

import java.util.List;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.da.id.BatchInstanceID;

/**
 * This service is used to read Barcode from an image file and writes the value, coordinates and confidence score in page level Fields
 * inside batch.xml.The service can read barcodes of type Code 39, QR, Code 93, Code 128, ITF, PDF417, Codabar and Datamatrix. If more than one barcode is found a seperate page
 * level field is created for it. Confidence score is decided on the basis of barcode value ie. 100 if barcode is found a and 0 if not
 * found.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.barcodeextraction.service.BarcodeExtractionServiceImpl
 * 
 */
public interface BarcodeExtractionService {

	/**
	 * This method extracts the Barcode values, co-ordinates and confidence for each image file and updates the batch.xml for those
	 * images respectively.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 * @throws DCMAException if any exception is occurred during readBarcode method.
	 */
	void extractPageBarCode(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException;
	
	/**
	 * This method extracts the Barcode values and confidence for each image file.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param folderLocation {@link String}
	 * @param imageName {@link String}
	 * @param docType {@link String}
	 * @return List<DocField>
	 * @throws DCMAException if any exception is occurred during readBarcode method.
	 * @returns list of document level fields
	 */
	List<DocField> extractPageBarCodeAPI(final String batchClassIdentifier, final String folderLocation, final String imageName, final String docType) throws DCMAException;
}
