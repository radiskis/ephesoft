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

package com.ephesoft.dcma.docassembler.factory;

import com.ephesoft.dcma.docassembler.classification.DocumentClassification;
import com.ephesoft.dcma.docassembler.classification.automatic.AutomaticClassification;
import com.ephesoft.dcma.docassembler.classification.barcode.BarcodeClassification;
import com.ephesoft.dcma.docassembler.classification.engine.SearchClassification;
import com.ephesoft.dcma.docassembler.classification.image.ImageClassification;
import com.ephesoft.dcma.docassembler.classification.searchablepdf.SearchablePdfClassification;

/**
 * This enum is factory which returns BarcodeClassification or SearchClassification or ImageClassification on the basis of input
 * parameter.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.docassembler.classification.DocumentClassification
 */
public enum DocumentClassificationFactory {
	/**
	 * Creating instance of BARCODE.
	 */
	BARCODE(new BarcodeClassification(), "BarcodeClassification"), 
	/**
	 * Creating instance of SEARCHCLASSIFICATION.
	 */
	SEARCHCLASSIFICATION(new SearchClassification(), "SearchClassification"), 
	/**
	 * Creating instance of IMAGE.
	 */
	IMAGE(new ImageClassification(), "ImageClassification"), 
	/**
	 * Creating instance of SEARCHABLEPDF.
	 */
	SEARCHABLEPDF(new SearchablePdfClassification(), "SearchablePdfClassification"),
	/**
	 * Creating instance of AUTOMATIC.
	 */
	AUTOMATIC(new AutomaticClassification(), "AutomaticClassification");

	/**
	 * documentClassification.
	 */
	private DocumentClassification documentClassification;

	/**
	 * nameClassification.
	 */
	private String nameClassification;

	/**
	 * Constructor.
	 * @param documentClassification DocumentClassification
	 * @param nameClassification String
	 */
	DocumentClassificationFactory(final DocumentClassification documentClassification, final String nameClassification) {
		this.documentClassification = documentClassification;
		this.nameClassification = nameClassification;
	}

	/**
	 * To get Document Classification.
	 * @return the documentClassification
	 */
	public DocumentClassification getDocumentClassification() {
		return documentClassification;
	}

	/**
	 * To get name Classification.
	 * @return the nameClassification
	 */
	public String getNameClassification() {
		return nameClassification;
	}

	/**
	 * On the basis of nameClassification this method will return the DocumentClassification object reference.
	 * 
	 * @param nameClassification {@link String}
	 * @return DocumentClassification
	 */
	public static DocumentClassification getDocumentClassification(final String nameClassification) {

		DocumentClassification documentClassification = null;
		if (null != nameClassification && !nameClassification.isEmpty()) {
			for (DocumentClassificationFactory factory : DocumentClassificationFactory.values()) {
				if (nameClassification.equals(factory.getNameClassification())) {
					documentClassification = factory.getDocumentClassification();
					break;
				}
			}
		}

		return documentClassification;
	}
}
