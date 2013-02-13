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

package com.ephesoft.dcma.mail.constants;

/**
 * Constants interface used in this plugin.
 * 
 * @author Ephesoft
 *
 */
public interface MailConstants {

	/**
	 * A constant for space.
	 */
	char SPACE = ' ';
	/**
	 * A constant to store closing bracket.
	 */
	char CLOSING_BRACKET = ']';
	/**
	 * Constant to store opening bracket.
	 */
	char OPENING_BRACKET = '[';
	/**
	 * Constant to store double quotes.
	 */
	char QUOTES = '\'';
	/**
	 * Constant to store the error file name.
	 */
	String ERROR_FILE = "error.pdf";
	/**
	 * The default port number pop3.
	 */
	Integer DEFAULT_PORT_NUMBER_POP3 = 995;
	/**
	 * The default port number IMAP.
	 */
	Integer DEFAULT_PORT_NUMBER_IMAP = 993;
	/**
	 * The SSL factory.
	 */
	String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	/**
	 * zzzzz attachment type.
	 */
	String ZZZZZ_ATTACHMENT = "zzzzz-";
	/**
	 * Name of temporary file.
	 */
	String TEMP_FILE_NAME = "!!!!mail";
	/**
	 * Period separator.
	 */
	String DOT_SEPARATOR = ".";
	/**
	 * Separator constant.
	 */
	String EXTENSION_SEPARATOR_CONSTANT = ";";
	/**
	 * Character plus(+).
	 */
	String CHARCTER_PLUS = "+";
	/**
	 * Value for encoded string for plus.
	 */
	String ENCODED_STRING_FOR_PLUS = "%20";
	/**
	 * Character underscore.
	 */
	char UNDERSCORE = '_';
	/**
	 * UTF-8 encoding.
	 */
	String UTF_8_ENCODING = "UTF-8";
	/**
	 * Opening angular brackets.
	 */
	char OPENING_ANGULAR_BRACKET = '<';
	/**
	 * Closing angular brackets.
	 */
	char CLOSING_ANGULAR_BRACKET = '>';
	/**
	 * MIME type for html.
	 */
	String MIME_TYPE_HTML_TEXT = "text/html";
	/**
	 * MIME type for plain text.
	 */
	String MIME_TYPE_PLAIN_TEXT = "text/plain";
	/**
	 * MIME type alternative value.
	 */
	String MIME_TYPE_ALTERNATIVE = "multipart/alternative";
	/**
	 * MIME type for text.
	 */
	String MIME_TYPE_TEXT = "text/*";
	/**
	 * The URL seperator.
	 */
	char URL_SEPARATOR = '/';
}
