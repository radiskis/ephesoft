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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyHtmlSerializer;
import org.htmlcleaner.TagNode;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.tidy.Tidy;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This is XML Util class.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see javax.xml.parsers.DocumentBuilder
 * 
 */
public class XMLUtil {

	/**
	 * WRITER_CONST int.
	 */
	private static final int WRITER_CONST = 1024;

	/**
	 * ISO_ENCODING String.
	 */
	private static final String ISO_ENCODING = "iso-8859-1";

	/**
	 * DOC_TYPE_OMIT String.
	 */
	private static final String DOC_TYPE_OMIT = "omit";

	/**
	 * UTF_ENCODING String.
	 */
	private static final String UTF_ENCODING = "UTF-8";

	/**
	 * PROPERTY_INDENT String.
	 */
	private static final String PROPERTY_INDENT = "indent";

	/**
	 * VALUE_YES String.
	 */
	private static final String VALUE_YES = "yes";

	/**
	 * HTML_PARSER String.
	 */
	public static final String HTML_PARSER = "html_parser";

	/**
	 * HTML_CLEANER String.
	 */
	public static final String HTML_CLEANER = "0";

	/**
	 * JTIDY String.
	 */
	public static final String JTIDY = "1";

	/**
	 * WRITER_SIZE int.
	 */
	public static final int WRITER_SIZE = WRITER_CONST * 4;

	private static DocumentBuilder getBuilder() throws ParserConfigurationException {
		return getBuilder(false);
	}

	private static DocumentBuilder getBuilder(boolean isXPATH) throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		if (isXPATH) {
			factory.setNamespaceAware(true);
		}
		return factory.newDocumentBuilder();
	}

	/**
	 * To create Document from given inputstream.
	 * 
	 * @param input InputStream
	 * @return Document
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document createDocumentFrom(InputStream input) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder builder = getBuilder();
		return builder.parse(input);
	}

	/**
	 * To create Document from given file.
	 * 
	 * @param file File
	 * @return Document
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document createDocumentFrom(File file) throws ParserConfigurationException, SAXException, IOException {
		return createDocumentFrom(file, false);
	}

	/**
	 * To create Document from given file.
	 * 
	 * @param file File
	 * @param isXPATH boolean
	 * @return Document
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document createDocumentFrom(File file, boolean isXPATH) throws ParserConfigurationException, SAXException,
			IOException {
		DocumentBuilder builder = getBuilder(isXPATH);
		return builder.parse(file);
	}

	/**
	 * To create Document from Resource.
	 * 
	 * @param resourceName String
	 * @return Document
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document createDocumentFromResource(String resourceName) throws ParserConfigurationException, SAXException,
			IOException {
		ClassLoader loader = XMLUtil.class.getClassLoader();
		InputStream inputStream = loader.getResourceAsStream(resourceName);
		return createDocumentFrom(inputStream);
	}

	/**
	 * To create Document from Absolute Resource.
	 * 
	 * @param resourceName String
	 * @return Document
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document createDocumentFromAbsoluteResource(String resourceName) throws ParserConfigurationException, SAXException,
			IOException {
		return createDocumentFrom(new File(resourceName));
	}

	/**
	 * To create Document from string.
	 * 
	 * @param xmlString String
	 * @return Document
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document createDocumentFrom(String xmlString) throws ParserConfigurationException, SAXException, IOException {
		StringReader strReader = new StringReader(xmlString);
		InputSource iSrc = new InputSource(strReader);
		DocumentBuilder builder = getBuilder();
		return builder.parse(iSrc);
	}

	/**
	 * To create new Document.
	 * 
	 * @return Document
	 * @throws ParserConfigurationException
	 */
	public static Document createNewDocument() throws ParserConfigurationException {
		DocumentBuilder builder = getBuilder();
		return builder.newDocument();
	}

	/**
	 * To create Source from File.
	 * 
	 * @param file File
	 * @return DOMSource
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerConfigurationException
	 * @throws TransformerFactoryConfigurationError
	 */
	public static DOMSource createSourceFromFile(File file) throws ParserConfigurationException, SAXException, IOException,
			TransformerConfigurationException, TransformerFactoryConfigurationError {
		Document document = createDocumentFrom(file);
		return getDomSourceForDoc(document);
	}

	/**
	 * To create Source from Stream.
	 * 
	 * @param inputStream InputStream
	 * @return DOMSource
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerConfigurationException
	 * @throws TransformerFactoryConfigurationError
	 */
	public static DOMSource createSourceFromStream(InputStream inputStream) throws ParserConfigurationException, SAXException,
			IOException, TransformerConfigurationException, TransformerFactoryConfigurationError {
		Document document = createDocumentFrom(inputStream);
		return getDomSourceForDoc(document);
	}

	private static DOMSource getDomSourceForDoc(Document document) throws TransformerFactoryConfigurationError,
			TransformerConfigurationException {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty(PROPERTY_INDENT, VALUE_YES);
		return new javax.xml.transform.dom.DOMSource(document);
	}

	/**
	 * toXMLString.
	 * 
	 * @param document
	 * @return
	 * @throws TransformerException
	 */
	public static String toXMLString(Document document) throws TransformerException {

		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty(PROPERTY_INDENT, VALUE_YES);
		javax.xml.transform.dom.DOMSource src = new javax.xml.transform.dom.DOMSource(document);
		java.io.CharArrayWriter writer = new java.io.CharArrayWriter(WRITER_CONST);
		javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(writer);
		transformer.transform(src, result);
		return writer.toString();
	}

	/**
	 * This method should eventually replace the toXMLString(Document doc) method.
	 * 
	 * @param xmlNode Node
	 * @return String
	 * @throws TransformerException
	 * @throws Exception
	 */
	public static String xmlNode2String(Node xmlNode) throws TransformerException {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty(PROPERTY_INDENT, VALUE_YES);
		javax.xml.transform.dom.DOMSource src = new javax.xml.transform.dom.DOMSource(xmlNode);
		java.io.CharArrayWriter writer = new java.io.CharArrayWriter(WRITER_CONST);
		javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(writer);
		transformer.transform(src, result);
		return writer.toString();
	}

	/**
	 * To flush Document to File.
	 * 
	 * @param document Document
	 * @param fileName String
	 * @throws FileNotFoundException
	 * @throws TransformerException
	 */
	public static void flushDocumentToFile(Document document, String fileName) throws FileNotFoundException, TransformerException {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty(PROPERTY_INDENT, VALUE_YES);
		javax.xml.transform.dom.DOMSource src = new javax.xml.transform.dom.DOMSource(document);
		javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(new FileOutputStream(fileName));
		transformer.transform(src, result);

	}

	/**
	 * To append Leaf Child.
	 * 
	 * @param doc Document
	 * @param parent Node
	 * @param childName String
	 * @param childData String
	 */
	public static void appendLeafChild(Document doc, Node parent, String childName, String childData) {
		Element child = doc.createElement(childName);
		if (childData != null && childData.length() != 0) {
			Text text = doc.createTextNode(childData);

			child.appendChild(text);
		}
		parent.appendChild(child);
	}

	/**
	 * To get Cloned XML Document.
	 * 
	 * @param xmlDoc Document
	 * @return Document
	 * @throws TransformerException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document getClonedXMLDocument(Document xmlDoc) throws TransformerException, ParserConfigurationException,
			SAXException, IOException {
		String XMLString = toXMLString(xmlDoc);
		return createDocumentFrom(XMLString);

	}

	/**
	 * To apply Transformation.
	 * 
	 * @param doc Document
	 * @param xsltPath String
	 * @return String
	 * @throws TransformerException
	 */
	public static String applyTransformation(Document doc, String xsltPath) throws TransformerException {
		InputStream xsltFile = XMLUtil.class.getClassLoader().getResourceAsStream(xsltPath);
		TransformerFactory xsltFactory = TransformerFactory.newInstance();
		StreamSource inputSource = new StreamSource(xsltFile);
		Transformer transformer = xsltFactory.newTransformer(inputSource);
		javax.xml.transform.dom.DOMSource src = new javax.xml.transform.dom.DOMSource(doc);
		java.io.CharArrayWriter writer = new java.io.CharArrayWriter(WRITER_CONST);
		javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(writer);
		transformer.transform(src, result);
		return writer.toString();
	}

	/**
	 * To apply XSL Transformation.
	 * 
	 * @param xmlDocument Document
	 * @param stylesheetFileLocation String
	 * @return byte[]
	 * @throws TransformerException
	 */
	public static byte[] applyXSLTransformation(Document xmlDocument, String stylesheetFileLocation) throws TransformerException {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer(new StreamSource(stylesheetFileLocation));
		transformer.setOutputProperty(PROPERTY_INDENT, VALUE_YES);
		javax.xml.transform.dom.DOMSource src = new javax.xml.transform.dom.DOMSource(xmlDocument);
		java.io.CharArrayWriter writer = new java.io.CharArrayWriter(WRITER_CONST);
		javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(writer);
		transformer.transform(src, result);
		return writer.toString().getBytes();
	}

	/**
	 * To output html Stream.
	 * 
	 * @param pathOfHOCRFile String
	 * @param outputFilePath String
	 * @return FileWriter
	 * @throws IOException
	 */
	public static void htmlOutputStream(final String pathOfHOCRFile, final String outputFilePath) throws IOException {
		ApplicationConfigProperties applicationConfigProperties = ApplicationConfigProperties.getApplicationConfigProperties();
		String htmlParser = applicationConfigProperties.getProperty(HTML_PARSER);
		if (htmlParser != null && htmlParser.equals(HTML_CLEANER)) {
			htmlOutputStreamViaHtmlCleaner(pathOfHOCRFile, outputFilePath);
		} else {
			htmlOutputStreamViaTidy(pathOfHOCRFile, outputFilePath);
		}
	}

	/**
	 * To Output html Stream via Tidy.
	 * 
	 * @param pathOfHOCRFile String
	 * @param outputFilePath String
	 * @throws IOException
	 */
	public static void htmlOutputStreamViaTidy(final String pathOfHOCRFile, final String outputFilePath) throws IOException {

		Tidy tidy = new Tidy();
		tidy.setXHTML(true);
		tidy.setDocType(DOC_TYPE_OMIT);
		tidy.setInputEncoding(UTF_ENCODING);
		tidy.setOutputEncoding(UTF_ENCODING);
		tidy.setForceOutput(true);
		tidy.setWraplen(0);
		FileInputStream inputStream = null;

		OutputStream fout = null;
		OutputStream bout = null;
		OutputStreamWriter out = null;
		try {
			/*
			 * Fix for UTF-8 encoding to support special characters in turkish and czech language. UTF-8 encoding supports major
			 * characters in all the languages
			 */
			fout = new FileOutputStream(outputFilePath);
			bout = new BufferedOutputStream(fout);
			out = new OutputStreamWriter(bout, UTF_ENCODING);

			inputStream = new FileInputStream(pathOfHOCRFile);
			tidy.parse(inputStream, out);
		} finally {
			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(bout);
			IOUtils.closeQuietly(fout);
		}
	}

	/**
	 * To Output html Stream via Html Cleaner.
	 * 
	 * @param pathOfHOCRFile String
	 * @param outputFilePath String
	 * @throws IOException
	 */
	public static void htmlOutputStreamViaHtmlCleaner(String pathOfHOCRFile, String outputFilePath) throws IOException {
		CleanerProperties cleanerProps = new CleanerProperties();

		// set some properties to non-default values
		cleanerProps.setTransResCharsToNCR(true);
		cleanerProps.setTranslateSpecialEntities(true);
		cleanerProps.setOmitComments(true);
		cleanerProps.setOmitDoctypeDeclaration(true);
		cleanerProps.setOmitXmlDeclaration(false);
		HtmlCleaner cleaner = new HtmlCleaner(cleanerProps);

		// take default cleaner properties
		// CleanerProperties props = cleaner.getProperties();
		FileInputStream hOCRFileInputStream = new FileInputStream(pathOfHOCRFile);
		TagNode tagNode = cleaner.clean(hOCRFileInputStream, UTF_ENCODING);
		if (null != hOCRFileInputStream) {
			hOCRFileInputStream.close();
		}
		try {
			new PrettyHtmlSerializer(cleanerProps).writeToFile(tagNode, outputFilePath, UTF_ENCODING);
		} catch (Exception e) { // NOPMD.
		}
	}

	/**
	 * To Output html Stream for ISO Encoding.
	 * 
	 * @param pathOfHOCRFile String
	 * @param outputFilePath String
	 * @return FileWriter
	 * @throws IOException
	 */
	public static void htmlOutputStreamForISOEncoding(final String pathOfHOCRFile, final String outputFilePath) throws IOException {

		Tidy tidy = new Tidy();
		tidy.setXHTML(true);
		tidy.setDocType(DOC_TYPE_OMIT);
		tidy.setInputEncoding(ISO_ENCODING);
		tidy.setOutputEncoding(ISO_ENCODING);
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
				outputStream.flush();
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
	 * @throws IOException
	 * @throws TransformerFactoryConfigurationError
	 */
	public static void transformXML(final String pathToSourceXML, final String pathToTargetXML, final InputStream xslStream)
			throws TransformerException, TransformerFactoryConfigurationError, IOException {
		InputStream fis = new FileInputStream(new File(pathToSourceXML));
		transformXMLWithStream(fis, pathToTargetXML, xslStream);
	}

	/**
	 * To transform XML with Stream.
	 * 
	 * @param fis {@link InputStream}
	 * @param pathToTargetXML {@link String}
	 * @param xslStream {@link InputStream}
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerConfigurationException
	 * @throws FileNotFoundException
	 * @throws TransformerException
	 * @throws IOException
	 */
	public static void transformXMLWithStream(final InputStream fis, final String pathToTargetXML, final InputStream xslStream)
			throws TransformerFactoryConfigurationError, TransformerConfigurationException, FileNotFoundException,
			TransformerException, IOException {
		FileOutputStream fileOutputStream = null;
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(xslStream));
			fileOutputStream = new FileOutputStream(pathToTargetXML);
			transformer.transform(new javax.xml.transform.stream.StreamSource(fis), new javax.xml.transform.stream.StreamResult(
					fileOutputStream));
		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.flush();
				fileOutputStream.close();
			}

			if (fis != null) {
				fis.close();
			}
		}
	}

	/**
	 * API for creating JDOM document using file path.
	 * 
	 * @param filePath {@link String}
	 * @return org.jdom.Document
	 * @throws IOException
	 * @throws JDOMException
	 */
	public static org.jdom.Document createJDOMDocumentFrom(String filePath) throws JDOMException, IOException {
		return new SAXBuilder().build(filePath);
	}

	/**
	 * API for creating JDOM document using file.
	 * 
	 * @param file {@link File}
	 * @return org.jdom.Document
	 * @throws IOException
	 * @throws JDOMException
	 */
	public static org.jdom.Document createJDOMDocumentFromFile(File file) throws JDOMException, IOException {
		return new SAXBuilder().build(file);
	}

	/**
	 * API for creating JDOM document from input stream.
	 * 
	 * @param inputStream {@link InputStream}
	 * @return org.jdom.Document
	 * @throws IOException
	 * @throws JDOMException
	 */
	public static org.jdom.Document createJDOMDocumentFromInputStream(InputStream inputStream) throws JDOMException, IOException {
		return new SAXBuilder().build(inputStream);
	}

	/**
	 * @param doc {@link org.w3c.dom.Document}
	 * @param xPathExpression {@link String}
	 * @return
	 */
	public static String getValueFromXML(final Document doc, final String xPathExpression) throws XPathExpressionException {
		XPath xpath = XPathFactory.newInstance().newXPath();
		String requiredValue = "";
		XPathExpression expr = xpath.compile(xPathExpression);
		Object result = expr.evaluate(doc, XPathConstants.NODESET);
		NodeList nodes = (NodeList) result;
		Node item = nodes.item(0);
		if (item != null) {
			requiredValue = item.getFirstChild().getNodeValue();
		}
		return requiredValue;
	}
}
