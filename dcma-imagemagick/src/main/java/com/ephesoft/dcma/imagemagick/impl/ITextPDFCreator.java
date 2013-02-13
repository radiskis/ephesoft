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

package com.ephesoft.dcma.imagemagick.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.HocrPages;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.imagemagick.ImageMagicProperties;
import com.ephesoft.dcma.imagemagick.MultiPageExecutor;
import com.ephesoft.dcma.imagemagick.constant.ImageMagicKConstants;

/**
 * This class is used to create the pdf file using IText API.
 * 
 * @author Ephesoft
 */
public class ITextPDFCreator {

	/**
	 * Variable for logging.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ITextPDFCreator.class);

	private static final String SWITCH_VALUE_TRUE = "true";

	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Variable for pdf height.
	 */
	private transient String heightOfPdfPage;

	/**
	 * Variable for pdf width.
	 */
	private transient String widthOfPdfPage;

	/**
	 * @param heightOfPdfPage the heightOfPdfPage to set.
	 */
	public void setHeightOfPdfPage(String heightOfPdfPage) {
		this.heightOfPdfPage = heightOfPdfPage;
	}

	/**
	 * @param widthOfPdfPage the widthOfPdfPage to set.
	 */
	public void setWidthOfPdfPage(String widthOfPdfPage) {
		this.widthOfPdfPage = widthOfPdfPage;
	}

	/**
	 * Reference for pluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * API for creating the PDF using IText.
	 * 
	 * @param pages
	 * @param batchInstanceThread
	 * @param multiPageExecutors
	 * @param documentIdInt
	 * @throws DCMAApplicationException
	 */
	public void createPDFUsingIText(String[] pages, BatchInstanceThread batchInstanceThread,
			List<MultiPageExecutor> multiPageExecutors, String documentIdInt) throws DCMAApplicationException {
		createNonSearchablePDF(pages, batchInstanceThread, multiPageExecutors);
	}


	/**
	 * API for creating the PDF using IText.
	 * 
	 * @param pages
	 * @param batchInstanceThread
	 * @param multiPageExecutors
	 * @param documentIdInt
	 * @throws DCMAApplicationException
	 */
	public void createPDFUsingIText(String[] pages, BatchInstanceThread batchInstanceThread,
			List<MultiPageExecutor> multiPageExecutors, String documentIdInt, Batch batchXml, String batchInstanceID,
			final int widthOfLine) throws DCMAApplicationException {
		createSearchablePDF(pages, batchInstanceThread, multiPageExecutors, documentIdInt, batchXml, batchInstanceID, widthOfLine);
	}

	/**
	 * @param pages
	 * @param batchInstanceThread
	 * @param multiPageExecutors
	 */
	private void createNonSearchablePDF(String[] pages, BatchInstanceThread batchInstanceThread,
			List<MultiPageExecutor> multiPageExecutors) {
		LOGGER.info("Adding command for multi page pdf execution");
		
		int widthOfPdfPageInt = getWidthOfPdfPageInt();
		int heightOfPdfPageInt = getHeightOfPdfPageInt();
		
		multiPageExecutors.add(new MultiPageExecutor(batchInstanceThread, pages, widthOfPdfPageInt, heightOfPdfPageInt));
	}
	/**
	 * @param pages
	 * @param batchInstanceThread
	 * @param multiPageExecutors
	 * @param documentIdInt
	 * @param batchXml
	 * @param batchInstanceID
	 * @param widthOfLine
	 */
	private void createSearchablePDF(String[] pages, BatchInstanceThread batchInstanceThread,
			List<MultiPageExecutor> multiPageExecutors, String documentIdInt, Batch batchXml, String batchInstanceID,
			final int widthOfLine) {
		LOGGER.info("Adding command for multi page searchable pdf execution for batch : " + batchInstanceID + " and document: "
				+ documentIdInt);
		Map<String, HocrPage> imageHtmlMap = null;
		final boolean isColoredPDF = SWITCH_VALUE_TRUE.equalsIgnoreCase(pluginPropertiesService.getPropertyValue(batchInstanceID,
				ImageMagicKConstants.CREATEMULTIPAGE_FILES_PLUGIN, ImageMagicProperties.CHECK_COLOURED_PDF));
		boolean isSearchablePDF = SWITCH_VALUE_TRUE.equalsIgnoreCase(pluginPropertiesService.getPropertyValue(batchInstanceID,
				ImageMagicKConstants.CREATEMULTIPAGE_FILES_PLUGIN, ImageMagicProperties.CHECK_SEARCHABLE_PDF));
		String pdfFilePath = null;
		if (null != pages && pages.length > 0) {
			final int lastIndex = pages.length - 1;
			imageHtmlMap = new HashMap<String, HocrPage>(lastIndex);
			for (int i = 0; i < lastIndex; i++) {
				imageHtmlMap.put(pages[i], (isSearchablePDF) ? getHOCRFileForImage(batchXml, pages[i]) : null);
			}
			pdfFilePath = pages[lastIndex];
		}
		multiPageExecutors.add(new MultiPageExecutor(batchInstanceThread, imageHtmlMap, isColoredPDF, isSearchablePDF, pdfFilePath,
				widthOfLine, pages));
	}

	/**
	 * This API will create the pdf using array of pages provided in it.
	 * 
	 * @param pages
	 * @param batchInstanceThread
	 */
	public void createPDFUsingIText(String[] pages, BatchInstanceThread batchInstanceThread) {
		LOGGER.info("Adding command for multi page pdf execution");

		int widthOfPdfPageInt = getWidthOfPdfPageInt();
		int heightOfPdfPageInt = getHeightOfPdfPageInt();

		new MultiPageExecutor(batchInstanceThread, pages, widthOfPdfPageInt, heightOfPdfPageInt);
	}

	/**
	 * API for getting the height for output pdf.
	 * 
	 * @return
	 */
	private int getHeightOfPdfPageInt() {
		int heightOfPdfPageInt = ImageMagicKConstants.DEFAULT_PDF_PAGE_HEIGHT;
		try {
			heightOfPdfPageInt = Integer.parseInt(heightOfPdfPage);
		} catch (NumberFormatException nfe) {
			LOGGER.info("Invalid or no value for heightOfPdfPage specified in properties file. Setting it its default value 792.");
		}
		return heightOfPdfPageInt;
	}

	/**
	 * API for getting the width for output pdf.
	 * 
	 * @return
	 */
	private int getWidthOfPdfPageInt() {
		int widthOfPdfPageInt = ImageMagicKConstants.DEFAULT_PDF_PAGE_WIDTH;
		try {
			widthOfPdfPageInt = Integer.parseInt(widthOfPdfPage);
		} catch (NumberFormatException nfe) {
			LOGGER.info("Invalid or no value for heightOfPdfPage specified in properties file. Setting it its default value 792.");
		}
		return widthOfPdfPageInt;
	}

	/**
	 * API for getting HOCR file name for specified image.
	 * 
	 * @param batchXML
	 * @param imageName
	 * @return
	 */
	private HocrPage getHOCRFileForImage(Batch batchXML, String imageName) {
		List<Document> xmlDocuments = batchXML.getDocuments().getDocument();
		HocrPages hocrPages = null;
		HocrPage hocrPage = null;
		List<HocrPage> hocrPageList = null;
		for (Document document : xmlDocuments) {
			List<Page> listOfPages = document.getPages().getPage();
			for (Page page : listOfPages) {
				String fileName = imageName.substring(imageName.lastIndexOf(File.separator) + 1, imageName.length());
				if (fileName != null && fileName.equalsIgnoreCase(page.getNewFileName())) {
					hocrPages = batchSchemaService.getHocrPages(batchXML.getBatchInstanceIdentifier(), page.getIdentifier());
					if (hocrPages != null) {
						hocrPageList = hocrPages.getHocrPage();
						if (!hocrPageList.isEmpty()) {
							hocrPage = hocrPageList.get(0);
							break;
						}
					}
				}
			}
		}
		return hocrPage;
	}
}
