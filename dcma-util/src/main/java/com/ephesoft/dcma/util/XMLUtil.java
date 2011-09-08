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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.tidy.Tidy;
import org.xml.sax.InputSource;

public class XMLUtil {

	protected static Log log = LogFactory.getLog(XMLUtil.class);

	private static DocumentBuilder getBuilder() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder;
	}

	public static Document createDocumentFrom(InputStream is) throws Exception {
		DocumentBuilder builder = getBuilder();
		Document document = builder.parse(is);
		return document;
	}

	public static Document createDocumentFrom(File file) throws Exception {
		DocumentBuilder builder = getBuilder();
		Document document = builder.parse(file);
		return document;
	}

	public static Document createDocumentFromResource(String resourceName) throws Exception {
		ClassLoader loader = XMLUtil.class.getClassLoader();
		InputStream is = loader.getResourceAsStream(resourceName);
		return createDocumentFrom(is);
	}

	public static Document createDocumentFromAbsoluteResource(String resourceName) throws Exception {
		return createDocumentFrom(new File(resourceName));
	}

	public static Document createDocumentFrom(String xmlString) throws Exception {
		StringReader strReader = new StringReader(xmlString);
		InputSource iSrc = new InputSource(strReader);
		DocumentBuilder builder = getBuilder();
		Document document = builder.parse(iSrc);
		return document;
	}

	public static Document createNewDocument() throws Exception {
		DocumentBuilder builder = getBuilder();
		Document document = builder.newDocument();
		return document;
	}

	public static DOMSource createSourceFromFile(File file) throws Exception {
		Document document = createDocumentFrom(file);
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty("indent", "yes");
		return new javax.xml.transform.dom.DOMSource(document);
	}

	public static final int WRITER_SIZE = 1024 * 4;

	public static String toXMLString(Document document) throws Exception {

		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty("indent", "yes");
		javax.xml.transform.dom.DOMSource src = new javax.xml.transform.dom.DOMSource(document);
		java.io.CharArrayWriter writer = new java.io.CharArrayWriter(1024);
		javax.xml.transform.stream.StreamResult rs = new javax.xml.transform.stream.StreamResult(writer);
		transformer.transform(src, rs);
		return writer.toString();
	}

	/**
	 * This method should eventually replace the toXMLString(Document doc) method
	 * 
	 * @param xmlNode
	 * @return
	 * @throws Exception
	 */
	public static String XMLNode2String(Node xmlNode) throws Exception {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty("indent", "yes");
		javax.xml.transform.dom.DOMSource src = new javax.xml.transform.dom.DOMSource(xmlNode);
		java.io.CharArrayWriter writer = new java.io.CharArrayWriter(1024);
		javax.xml.transform.stream.StreamResult rs = new javax.xml.transform.stream.StreamResult(writer);
		transformer.transform(src, rs);
		return writer.toString();
	}

	public static void flushDocumentToFile(Document document, String fileName) throws FileNotFoundException, TransformerException {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty("indent", "yes");
		javax.xml.transform.dom.DOMSource src = new javax.xml.transform.dom.DOMSource(document);
		javax.xml.transform.stream.StreamResult rs = new javax.xml.transform.stream.StreamResult(new FileOutputStream(fileName));
		transformer.transform(src, rs);

	}

	public static void appendLeafChild(Document doc, Node parent, String childName, String childData) {
		Element child = doc.createElement(childName);
		if (childData != null && childData.length() != 0) {
			Text text = doc.createTextNode(childData);

			child.appendChild(text);
		}
		parent.appendChild(child);
	}

	public static Document getClonedXMLDocument(Document xmlDoc) throws Exception {
		String XMLString = toXMLString(xmlDoc);
		return createDocumentFrom(XMLString);

	}

	public static String applyTransformation(Document doc, String xsltPath) throws Exception {
		InputStream xsltFile = XMLUtil.class.getClassLoader().getResourceAsStream(xsltPath);
		TransformerFactory xsltFactory = TransformerFactory.newInstance();
		StreamSource inputSource = new StreamSource(xsltFile);
		Transformer transformer = xsltFactory.newTransformer(inputSource);
		javax.xml.transform.dom.DOMSource src = new javax.xml.transform.dom.DOMSource(doc);
		java.io.CharArrayWriter writer = new java.io.CharArrayWriter(1024);
		javax.xml.transform.stream.StreamResult rs = new javax.xml.transform.stream.StreamResult(writer);
		transformer.transform(src, rs);
		return writer.toString();
	}

	public static byte[] applyXSLTransformation(Document xmlDocument, String stylesheetFileLocation) throws Exception {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer(new StreamSource(stylesheetFileLocation));
		transformer.setOutputProperty("indent", "yes");
		javax.xml.transform.dom.DOMSource src = new javax.xml.transform.dom.DOMSource(xmlDocument);
		java.io.CharArrayWriter writer = new java.io.CharArrayWriter(1024);
		javax.xml.transform.stream.StreamResult rs = new javax.xml.transform.stream.StreamResult(writer);
		transformer.transform(src, rs);
		return writer.toString().getBytes();
	}

	/**
	 * @param pathOfHOCRFile String
	 * @param outputFilePath String
	 * @return FileWriter
	 * @throws IOException
	 */
	public static void htmlOutputStream(final String pathOfHOCRFile, final String outputFilePath) throws IOException {

		Tidy tidy = new Tidy();
		tidy.setXHTML(true);
		tidy.setDocType("omit");
		tidy.setInputEncoding("UTF-32");
		tidy.setOutputEncoding("UTF-32");
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(pathOfHOCRFile);
			outputStream = new FileOutputStream(outputFilePath);
			tidy.parse(inputStream, outputStream);
		} finally {
			if (null != inputStream) {
				inputStream.close();
			}
			if (null != outputStream) {
				outputStream.close();
			}
		}
	}
	

	/**
	 * @param pathOfHOCRFile String
	 * @param outputFilePath String
	 * @return FileWriter
	 * @throws IOException
	 */
	
	public static void htmlOutputStreamForISOEncoding(final String pathOfHOCRFile, final String outputFilePath) throws IOException {

		Tidy tidy = new Tidy();
		tidy.setXHTML(true);
		tidy.setDocType("omit");
		tidy.setInputEncoding("iso-8859-1");
		tidy.setOutputEncoding("iso-8859-1");
		tidy.setHideEndTags(false);
		
		FileInputStream inputStream = null;
		FileWriter outputStream = null;
		try {
			inputStream = new FileInputStream(pathOfHOCRFile);
			outputStream = new FileWriter(outputFilePath);
			tidy.parse(inputStream, outputStream);
		} finally {
			if (null != inputStream) {
				inputStream.close();
			}
			if (null != outputStream) {
				outputStream.close();
			}
		}
	}

	/**
	 * This method transforms source xml into target xml using XSLT provided.
	 * 
	 * @param pathToSourceXML String
	 * @param pathToTargetXML String
	 * @param pathToXSL String
	 * @throws TransformerException
	 * @throws FileNotFoundException
	 */
	public static void transformXML(final String pathToSourceXML, final String pathToTargetXML, final InputStream xslStream)
			throws TransformerException, FileNotFoundException {
		
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(xslStream));
		transformer.transform(new javax.xml.transform.stream.StreamSource(pathToSourceXML),
				new javax.xml.transform.stream.StreamResult(new FileOutputStream(pathToTargetXML)));
		
	}
}
