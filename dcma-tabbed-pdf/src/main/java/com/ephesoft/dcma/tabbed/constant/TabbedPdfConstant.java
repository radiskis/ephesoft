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

package com.ephesoft.dcma.tabbed.constant;

/**
 * This is a common constants file for IM Load Export plugin.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public interface TabbedPdfConstant {
	/**
	 * Total document size.
	 */
	/**
	 * TOTAL_DOCUMENT_SIZE String.
	 */
	String TOTAL_DOCUMENT_SIZE = "totalDocSize";
	/**
	 * Tabbed pdf plugin name.
	 */
	/**
	 * TABBED_PDF_PLUGIN String.
	 */
	String TABBED_PDF_PLUGIN = "TABBED_PDF";
	/**
	 * XML file extension.
	 */
	/**
	 * XML_EXTENSION String.
	 */
	String XML_EXTENSION = ".xml";
	/**
	 * Batch creation date.
	 */
	/**
	 * BATCH_CREATION_DATE String.
	 */
	String BATCH_CREATION_DATE = "batchCreationDate";
	/**
	 * Batch creation time.
	 */
	/**
	 * BATCH_CREATION_TIME String.
	 */
	String BATCH_CREATION_TIME = "batchCreationTime";
	/**
	 * Constant for underscore.
	 */
	/**
	 * UNDERSCORE String.
	 */
	String UNDERSCORE = "_";
	/**
	 * Ghostscript home value.
	 */
	/**
	 * GHOSTSCRIPT_HOME String.
	 */
	String GHOSTSCRIPT_HOME = "GHOSTSCRIPT_HOME";
	/**
	 * Ghostscript execute command.
	 */
	/**
	 * GHOSTSCRIPT_COMMAND String.
	 */
	String GHOSTSCRIPT_COMMAND = "gswin32c.exe";
	/**
	 * Pdf marks file name.
	 */
	/**
	 * PDF_MARKS_FILE_NAME String.
	 */
	String PDF_MARKS_FILE_NAME = "pdfmarks.dat";
	/**
	 * Pdf marks template.
	 */
	/**
	 * PDF_MARKS_TEMPLATE String.
	 */
	String PDF_MARKS_TEMPLATE = "[ /Page ## /Title (**) /OUT pdfmark";
	/**
	 * Sample name for pdf file name.
	 */
	/**
	 * SAMPLE_PDF_FILE_NAME String.
	 */
	String SAMPLE_PDF_FILE_NAME = "error.pdf";
	/**
	 * Subpoena type.
	 */
	/**
	 * SUBPOENA String.
	 */
	String SUBPOENA = "Subpoena";
	/**
	 * Loan number type.
	 */
	/**
	 * LOAN_NUMBER String.
	 */
	String LOAN_NUMBER = "LoanPerf";
	/**
	 * Mapping separator.
	 */
	/**
	 * MAPPING_SEPERATOR String.
	 */
	String MAPPING_SEPERATOR = "===";
	/**
	 * Constant value "YES".
	 */
	/**
	 * YES String.
	 */
	String YES = "YES";
	/**
	 * Switch value constant "ON".
	 */
	/**
	 * SWITCH_ON String.
	 */
	String SWITCH_ON = "ON";
	/**
	 * Document identifier constant.
	 */
	/**
	 * DOCUMENT_IDENTIFIER String.
	 */
	String DOCUMENT_IDENTIFIER = "DOC ";
	/**
	 * Bookmark title placeholder.
	 */
	/**
	 * BOOKMARK_TITLE_PLACEHOLDER String.
	 */
	String BOOKMARK_TITLE_PLACEHOLDER = "**";
	/**
	 * Bookmark page number placeholder.
	 */
	/**
	 * BOOKMARK_PAGE_NUMBER_PLACEHOLDER String.
	 */
	String BOOKMARK_PAGE_NUMBER_PLACEHOLDER = "##";
	/**
	 * Error constant for number of pdf pages.
	 */
	/**
	 * ERROR_PDF_NUMBER_OF_PAGES int.
	 */
	int ERROR_PDF_NUMBER_OF_PAGES = 1;
	/**
	 * Line separator.
	 */
	/**
	 * LINE_SEPARATOR String.
	 */
	String LINE_SEPARATOR = "line.separator";

	/**
	 * SPACE String.
	 */
	String SPACE = " ";

	/**
	 * EMPTY String.
	 */
	String EMPTY = "";

	/**
	 * QUOTES String.
	 */
	String QUOTES = "\"";

	/**
	 * CMD String.
	 */
	String CMD = "cmd";

	/**
	 * SLASH_C String.
	 */
	String SLASH_C = "/c";

	/**
	 * GHOSTCRIPT_COMMAND_NOT_FOUND String.
	 */
	String GHOSTCRIPT_COMMAND_NOT_FOUND = "No ghostcript command specified in properties file.  ghostcript command = ";

}
