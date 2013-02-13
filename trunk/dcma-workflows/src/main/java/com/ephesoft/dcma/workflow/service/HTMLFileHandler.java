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

package com.ephesoft.dcma.workflow.service;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.ephesoft.dcma.util.XMLUtil;
import com.ephesoft.dcma.workflow.constant.WorkFlowConstants;

/**
 * This Class provides the functionality of the parsing HTML file and changing and saving HTML file.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see javax.xml.parsers.DocumentBuilder
 */
public class HTMLFileHandler {

	/**
	 * Initializing logger {@link Logger}.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HTMLFileHandler.class);

	/**
	 * API changes old batchInstance identifier to new batch instance identifier in html file specified in file path.
	 * 
	 * @param filePath {@link String}
	 * @param newBatchInstanceIdentifier {@link String}
	 * @param oldBatchInstanceIdentifier {@link String}
	 */
	public void parseHTMLFile(final String filePath, final String newBatchInstanceIdentifier, final String oldBatchInstanceIdentifier) {

		if (filePath != null && newBatchInstanceIdentifier != null && oldBatchInstanceIdentifier != null) {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			Document dom = null;
			try {
				// Using factory get an instance of document builder
				DocumentBuilder databaseBuilder = dbf.newDocumentBuilder();
				// parse using builder to get DOM representation of the XML file
				dom = databaseBuilder.parse(filePath);
				if (dom != null) {
					Node node = dom.getElementsByTagName(WorkFlowConstants.DIV_HTML_TAG).item(0);
					NamedNodeMap nodeAttribute = node.getAttributes();
					Node nodeTitle = nodeAttribute.getNamedItem(WorkFlowConstants.TITLE_HTML_TAG);
					String string = nodeTitle.getTextContent();
					string = string.replaceAll(oldBatchInstanceIdentifier, newBatchInstanceIdentifier);
					nodeTitle.setTextContent(string);
					// writing file
					writeToFile(dom, filePath);
					String newFilePath = filePath + WorkFlowConstants.UNDERSCORE_ABC;
					XMLUtil.htmlOutputStream(filePath, newFilePath);

					File file = new File(filePath);
					boolean isDeleted = file.delete();
					if (isDeleted) {
						File newFile = new File(newFilePath);
						newFile.renameTo(file);
					} else {
						LOGGER.error("Error in deleting file :" + filePath);
						LOGGER.error("Unable to correct the file information.");
					}
				}
			} catch (ParserConfigurationException pce) {
				LOGGER.error(pce.getMessage(), pce);
			} catch (SAXException se) {
				LOGGER.error(se.getMessage(), se);
			} catch (IOException ioe) {
				LOGGER.error(ioe.getMessage(), ioe);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		} else {
			LOGGER.error("Either file path or batchInstance identifiers is null during parsing HTML file" + filePath);
		}
	}

	/**
	 * This API is used to save the document to the file path specified.
	 * 
	 * @param document {@link Document}
	 * @param filePath {@link String}
	 */
	private void writeToFile(Document document, String filePath) {
		Source source = null;
		Result result = null;
		Transformer xformer = null;
		try {
			if (document != null) {
				source = new DOMSource(document);
				File htmlFile = new File(filePath);
				result = new StreamResult(htmlFile);
				xformer = TransformerFactory.newInstance().newTransformer();
			}
		} catch (TransformerConfigurationException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (TransformerFactoryConfigurationError e) {
			LOGGER.error(e.getMessage(), e);
		}
		try {
			if (source != null && result != null && xformer != null) {
				xformer.transform(source, result);
			}
		} catch (TransformerException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
