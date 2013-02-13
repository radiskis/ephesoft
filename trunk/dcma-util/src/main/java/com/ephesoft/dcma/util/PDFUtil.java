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

package com.ephesoft.dcma.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ephesoft.dcma.constant.UtilConstants;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

/**
 * This is util class for PDF.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.itextpdf.text.Document
 */
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
		PdfReader pdfReader = null;
		try {
			pdfReader = new PdfReader(new RandomAccessFileOrArray(filePath),
					null);
			numberOfPage = pdfReader.getNumberOfPages();
		} catch (IOException e) {
			LOG.error("Error in reading the file:" + filePath + UtilConstants.SPACE + e.getMessage(), e);
		} finally {
			if (pdfReader != null) {
				pdfReader.close();
			}
		}
		return numberOfPage;
	}

	/**
	 * The <code>getSelectedPdfFile</code> method is used to limit the file
	 * to the page limit given.
	 * 
	 * @param pdfFile {@link File} pdf file from which limit has to be applied
	 * @param pageLimit int
	 * @throws IOException if file is not found
	 * @throws DocumentException if document cannot be created
	 */
	public static void getSelectedPdfFile(final File pdfFile, final int pageLimit)
			throws IOException, DocumentException {
		PdfReader reader = null;
		Document document = null;
		PdfContentByte contentByte = null;
		PdfWriter writer = null;
		FileInputStream fileInputStream  = null;
		FileOutputStream fileOutputStream = null;
		File newFile = null;
		if (null != pdfFile && pdfFile.exists()) {
			try{
				document = new Document();
				fileInputStream = new FileInputStream(pdfFile);
	
				String name = pdfFile.getName();
				final int indexOf = name.lastIndexOf(IUtilCommonConstants.DOT);
				name = name.substring(0, indexOf);
				final String finalPath = pdfFile.getParent() + File.separator + name
						+ System.currentTimeMillis() + IUtilCommonConstants.EXTENSION_PDF;
				newFile = new File(finalPath);
				fileOutputStream = new FileOutputStream(finalPath);
				writer = PdfWriter
						.getInstance(document, fileOutputStream);
				document.open();
				contentByte = writer.getDirectContent();
	
				reader = new PdfReader(fileInputStream);
				for (int i = 1; i <= pageLimit; i++) {
					document.newPage();
	
					// import the page from source pdf
					final PdfImportedPage page = writer.getImportedPage(reader, i);
	
					// add the page to the destination pdf
					contentByte.addTemplate(page, 0, 0);
					page.closePath();
				}
			} finally {
				closePassedStream(reader, document, contentByte, writer, fileInputStream, fileOutputStream);
			}
			if (pdfFile.delete() && null != newFile) {
				newFile.renameTo(pdfFile);
			} else {
				if (null != newFile) {
					newFile.delete();
				}
			}
		}
	}

	/**
	 * The <code>closePassedStream</code> method closes the stream passed.
	 * 
	 * @param reader {@link PdfReader}
	 * @param document {@link Document}
	 * @param contentByte {@link PdfContentByte}
	 * @param writer {@link PdfWriter}
	 * @param fileInputStream {@link FileInputStream}
	 * @param fileOutputStream {@link FileOutputStream}
	 * @throws IOException {@link} if unable to close input or output stream
	 */
	private static void closePassedStream(final PdfReader reader, final Document document,
			final PdfContentByte contentByte, final PdfWriter writer,
			final FileInputStream fileInputStream, final FileOutputStream fileOutputStream) throws IOException {
		if (null != reader) {
			reader.close();
		} 
		if (null != document) {
			document.close();
		}
		if (null != contentByte) {
			contentByte.closePath();
		}
		if (null != writer) {
			writer.close();
		}
		if (null != fileInputStream) {
			fileInputStream.close();
		}
		
		if (null != fileOutputStream) {
			fileOutputStream.flush();
			fileOutputStream.close();
		}
	}
	
	
}
