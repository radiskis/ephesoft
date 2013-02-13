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
import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ephesoft.dcma.constant.UtilConstants;

/**
 * This is util class for OCREngine.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see javax.xml.xpath.XPath
 */
public class OCREngineUtil {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(OCREngineUtil.class);

	/**
	 * To format HOCR for Tesseract.
	 * @param outputFilePath {@link String}
	 * @param actualFolderLocation  {@link String}
	 * @param pageId {@link String}
	 * @throws XPathExpressionException if error occurs
	 * @throws TransformerException if error occurs
	 * @throws IOException if error occurs
	 */
	public static void formatHOCRForTesseract(final String outputFilePath, final String actualFolderLocation, final String pageId)
			throws XPathExpressionException, TransformerException, IOException {
		LOGGER.info("Entering format HOCR for tessearct . outputfilepath : " + outputFilePath);
		InputStream inputStream = new FileInputStream(outputFilePath);
		XPathFactory xFactory = new org.apache.xpath.jaxp.XPathFactoryImpl();
		XPath xpath = xFactory.newXPath();
		XPathExpression pageExpr = xpath.compile("//div[@class=\"ocr_page\"]");
		XPathExpression wordExpr = xpath.compile("//span[@class=\"ocr_word\"]");
		
		// Output format supported by Tesseract 3.00
		XPathExpression xOcrWordExpr = xpath.compile("//span[@class=\"xocr_word\"]");
		
		// Output format supported by Tesseract 3.01
		XPathExpression ocrXWordExpr = xpath.compile("//span[@class=\"ocrx_word\"]");
		
		org.w3c.dom.Document doc2 = null;
		try {
			doc2 = XMLUtil.createDocumentFrom(inputStream);
		} catch (Exception e) {
			LOGGER.info("Premature end of file for " + outputFilePath + e);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
		if (doc2 != null) {
			LOGGER.info("document is not null.");
			NodeList wordList = (NodeList) wordExpr.evaluate(doc2, XPathConstants.NODESET);
			for (int wordNodeIndex = 0; wordNodeIndex < wordList.getLength(); wordNodeIndex++) {
				setWordNodeTextContent(xOcrWordExpr, ocrXWordExpr, wordList,
						wordNodeIndex);
			}

			NodeList pageList = (NodeList) pageExpr.evaluate(doc2, XPathConstants.NODESET);
			for (int pageNodeIndex = 0; pageNodeIndex < pageList.getLength(); pageNodeIndex++) {
				Node pageNode = pageList.item(pageNodeIndex);
				if (pageNode != null && ((Node) pageNode.getAttributes().getNamedItem(UtilConstants.ID_ATTR)) != null) {
					String pageID = ((Node) pageNode.getAttributes().getNamedItem(UtilConstants.ID_ATTR)).getTextContent();
					wordExpr = xpath.compile("//div[@id='" + pageID + "']//span[@class='ocr_word']");
					NodeList wordInPageList = (NodeList) wordExpr.evaluate(pageNode, XPathConstants.NODESET);

					Node pageNodeClone = pageNode.cloneNode(false);
					for (int i = 0; i < wordInPageList.getLength(); i++) {
						pageNodeClone.appendChild(wordInPageList.item(i));
					}
					pageNode.getParentNode().appendChild(pageNodeClone);
					pageNode.getParentNode().removeChild(pageNode);
				}
			}

			XMLUtil.flushDocumentToFile(doc2.getDocumentElement().getOwnerDocument(), outputFilePath);
			File tempFile = new File(actualFolderLocation + File.separator + pageId + "_tempFile_hocr.html");
			FileUtils.copyFile(new File(outputFilePath), tempFile);

			XMLUtil.htmlOutputStream(tempFile.getAbsolutePath(), outputFilePath);
			boolean isTempFileDeleted = tempFile.delete();
			if (!isTempFileDeleted) {
				tempFile.delete();
			}
		}
		LOGGER.info("Exiting format HOCR for tessearct . outputfilepath : " + outputFilePath);
	}

	private static void setWordNodeTextContent(XPathExpression xOcrWordExpr,
			XPathExpression ocrXWordExpr, NodeList wordList, int wordNodeIndex)
			throws XPathExpressionException {
		Node wordNode = wordList.item(wordNodeIndex);
		if (wordNode != null) {
			Node word = (Node) xOcrWordExpr.evaluate(wordNode, XPathConstants.NODE);
			if (word != null) {
				wordNode.setTextContent(word.getTextContent());
			} else {
				word = (Node) ocrXWordExpr.evaluate(wordNode, XPathConstants.NODE);
				if (word != null) {
					wordNode.setTextContent(word.getTextContent());
				}
			}
		}
	}
}
