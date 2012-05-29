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

package com.ephesoft.dcma.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.itextpdf.text.pdf.PdfReader;

public class PDFUtil {

	/**
	 * log reference.
	 */
	private static final Log LOG = LogFactory.getLog(PDFUtil.class);

	/**
	 * API for getting the number of pages in the pdf file.
	 * 
	 * @param filePath file path {@link String}
	 * @return numberOfPage
	 */
	public static int getPDFPageCount(String filePath) {
		int numberOfPage = 0;
		File file = new File(filePath);
		FileInputStream fileInputStream = null;
		PdfReader pdfReader = null;
		try {
			fileInputStream = new FileInputStream(file);
			pdfReader = new PdfReader(fileInputStream);
			numberOfPage = pdfReader.getNumberOfPages();
		} catch (FileNotFoundException e) {
			LOG.error("File not exists on the path:" + filePath + " " + e.getMessage(), e);
		} catch (IOException e) {
			LOG.error("Error in reading the file:" + filePath + " " + e.getMessage(), e);
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					LOG.info("Error in closing the file stream:" + filePath + " " + e.getMessage(), e);
				}
			}
			if (pdfReader != null) {
				pdfReader.close();
			}
		}
		return numberOfPage;
	}
}
