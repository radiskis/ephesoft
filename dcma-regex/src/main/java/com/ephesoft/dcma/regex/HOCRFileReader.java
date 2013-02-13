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

package com.ephesoft.dcma.regex;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.regex.constant.RegexConstants;
import com.ephesoft.dcma.regex.util.ExtractDocField;

/**
 * This class is used to read the HOCR files. Read the file line by line and provide the whole file as string output.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.regex.Extraction
 */
public final class HOCRFileReader {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ExtractDocField.class);

	/**
	 * Reference of HOCRFileReader for singleton instance.
	 */
	private static HOCRFileReader readerHOCRFile;

	/**
	 * object Object.
	 */
	private final Object object = new Object();

	/**
	 * private constructor.
	 */
	private HOCRFileReader() {
	}

	/**
	 * Return the HOCRFileReader object for singleton design pattern.
	 * 
	 * @return readerHOCRFile HOCRFileReader
	 */
	public static HOCRFileReader getInstance() {

		if (readerHOCRFile == null) {
			synchronized (HOCRFileReader.class) {
				if (readerHOCRFile == null) {
					readerHOCRFile = new HOCRFileReader();
					LOGGER.debug("Create the instance HOCRFileReader.");
				}
			}
		}
		return readerHOCRFile;
	}

	/**
	 * This method reads a text file line by line and add to the StringBuilder. It uses FileInputStream to read the file.
	 * 
	 * @param pathOfHOCRFile String
	 * @return infoHOCRFile String
	 * @throws DCMAApplicationException Check for input parameters and read the file.
	 */
	public String readHOCRFile(final String pathOfHOCRFile) throws DCMAApplicationException {

		final StringBuffer infoHOCRFile = new StringBuffer();
		synchronized (object) {
			String errMsg = null;
			if (null == pathOfHOCRFile || RegexConstants.EMPTY.equals(pathOfHOCRFile)) {
				errMsg = "Invalid HOCR file path.";
				throw new DCMAApplicationException(errMsg);
			}

			FileInputStream fstream = null;
			DataInputStream dataInStr = null;
			BufferedReader bufReader = null;
			try {
				// Open the file that is the first
				// command line parameter
				fstream = new FileInputStream(pathOfHOCRFile);
				// Get the object of DataInputStream
				dataInStr = new DataInputStream(fstream);
				bufReader = new BufferedReader(new InputStreamReader(dataInStr));
				// Read File Line By Line
				String strLine = bufReader.readLine();
				while (strLine != null) {
					infoHOCRFile.append(strLine);
					strLine = bufReader.readLine();
				}
			} catch (Exception e) {
				// Catch exception if any
				LOGGER.error("Error: " + e.getMessage());
				throw new DCMAApplicationException(e.getMessage(), e);
			} finally {
				if (fstream != null) {
					try {
						fstream.close();
					} catch (IOException e) {
						LOGGER.error("Problem in closing input stream: " + e.getMessage(), e);
					}
				}
				if (dataInStr != null) {
					try {
						dataInStr.close();
					} catch (IOException e) {
						LOGGER.error("Problem in closing input stream: " + e.getMessage(), e);
					}
				}
				if (bufReader != null) {
					try {
						bufReader.close();
					} catch (IOException e) {
						LOGGER.error("Problem in closing input stream: " + e.getMessage(), e);
					}
				}
			}
		}
		return infoHOCRFile.toString();
	}

}
