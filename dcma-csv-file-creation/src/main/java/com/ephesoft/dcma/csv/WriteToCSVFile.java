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

package com.ephesoft.dcma.csv;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.csv.constant.CSVFileCreationConstant;

/**
 * This API is using for generating CSV file and writing data into CSV file.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.csv.service.CSVFileCreationService
 */
public class WriteToCSVFile {

	/**
	 * Variable for CSV file name.
	 */
	private String csvFileName;

	/**
	 * Variable for data column size.
	 */
	private int columnSize;

	/**
	 * Logger instance for logging using slf4j for logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(WriteToCSVFile.class);

	/**
	 * Empty constructor.
	 */
	public WriteToCSVFile() {
		// Empty constructor.
	}

	/**
	 * Parameterized constructor.
	 * @param csvFileName {@link String}
	 */
	public WriteToCSVFile(final String csvFileName) {
		this.csvFileName = csvFileName;
	}

	/**
	 * This API is used to create and write data into it.
	 * 
	 * @param csvFileName {@link String}
	 * @param headerList {@link List<string>}
	 * @param data {@link List<List<String>>}
	 * @throws IOException if any exception or error occur.
	 */
	public WriteToCSVFile(final String csvFileName, final List<String> headerList, final List<List<String>> data) throws IOException {
		BufferedWriter writer = null;
		try {
			this.csvFileName = csvFileName;
			this.columnSize = headerList.size();
			writer = new BufferedWriter(new FileWriter(this.csvFileName));
			addHeaderRow(writer, headerList);
			for (List<String> list : data) {
				addDataRow(writer, list);
			}
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}
	}

	private void addHeaderRow(final BufferedWriter writer, final List<String> headerRow) throws IOException {
		this.columnSize = headerRow.size();
		for (int index = 0; index < this.columnSize; index++) {
			String header = headerRow.get(index);
			writer.append(CSVFileCreationConstant.QUOTES + header + CSVFileCreationConstant.QUOTES);
			if (index < this.columnSize - 1) {
				writer.append(CSVFileCreationConstant.COMMA_STRING);
			}
		}
		writer.newLine();
	}

	private void addDataRow(final BufferedWriter writer, final List<String> dataRow) throws IOException {
		boolean isValid = true;
		if (dataRow != null) {
			int dataRowLength = dataRow.size();
			if (dataRowLength != this.columnSize) {
				isValid = false;
				LOGGER.error("Cannot add data row. Data row size greater than header row.");
			}
		}
		if (isValid) {
			for (int i = 0; i < dataRow.size(); i++) {
				String data = dataRow.get(i);
				writer.append(CSVFileCreationConstant.QUOTES + data + CSVFileCreationConstant.QUOTES);
				if (i < columnSize - 1 && i < dataRow.size() - 1) {
					writer.append(CSVFileCreationConstant.COMMA_STRING);
				}
			}
			writer.newLine();
		}
	}
}
