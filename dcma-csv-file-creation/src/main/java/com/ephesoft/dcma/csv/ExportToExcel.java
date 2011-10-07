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

package com.ephesoft.dcma.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.exception.NestableException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.contrib.HSSFCellUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.BatchLevelField;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.csv.constant.CSVFileCreationConstant;
import com.ephesoft.dcma.da.domain.BatchInstance;

/**
 * This class is used to create CSV file and writing data into CSV file.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public class ExportToExcel {

	/**
	 * variable for writing into CSV case 0.
	 */
	private static final int WRITE_TO_CSV_CASE0 = 0;

	/**
	 * variable for writing into CSV case 1.
	 */
	private static final int WRITE_TO_CSV_CASE1 = 1;

	/**
	 * variable for writing into CSV case 2.
	 */
	private static final int WRITE_TO_CSV_CASE2 = 2;

	/**
	 * variable for writing into CSV case 3.
	 */
	private static final int WRITE_TO_CSV_CASE3 = 3;

	/**
	 * variable for writing into CSV case 4.
	 */
	private static final int WRITE_TO_CSV_CASE4 = 4;

	/**
	 * variable for writing into CSV case 5.
	 */
	private static final int WRITE_TO_CSV_CASE5 = 5;

	/**
	 * variable for writing into CSV case 6.
	 */
	private static final int WRITE_TO_CSV_CASE6 = 6;

	/**
	 * variable for writing into CSV case 7.
	 */
	private static final int WRITE_TO_CSV_CASE7 = 7;

	/**
	 * variable for writing into CSV case 8.
	 */
	private static final int WRITE_TO_CSV_CASE8 = 8;

	/**
	 * variable for writing into CSV case 9.
	 */
	private static final int WRITE_TO_CSV_CASE9 = 9;

	/**
	 * variable for writing into CSV case 10.
	 */
	private static final int WRITE_TO_CSV_CASE10 = 10;

	/**
	 * variable for writing into CSV case 11.
	 */
	private static final int WRITE_TO_CSV_CASE11 = 11;

	/**
	 * variable for writing into CSV case 12.
	 */
	private static final int WRITE_TO_CSV_CASE12 = 12;

	/**
	 * variable for writing into CSV case 13.
	 */
	private static final int WRITE_TO_CSV_CASE13 = 13;

	/**
	 * variable for writing into CSV case 14.
	 */
	private static final int WRITE_TO_CSV_CASE14 = 14;

	/**
	 * Instance of Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ExportToExcel.class);

	/**
	 * Instance of HSSFWorkbook.
	 */
	private final HSSFWorkbook workbook;

	/**
	 * Instance of sheetName.
	 */
	private final String sheetName;

	/**
	 * Instance of HSSFFont.
	 */
	private final HSSFFont boldFont;

	/**
	 * Instance of headerColumns.
	 */
	private final List<String> headerColumns = new LinkedList<String>();

	/**
	 * This API adding header columns to the CSV file.
	 */
	private void addHeaderColumns() {
		LOGGER.info("Start adding header column type in csv");
		this.headerColumns.add(CSVFileCreationConstant.BATCH_ID.getId());
		LOGGER.info("Add 1st header column type in csv :" + CSVFileCreationConstant.BATCH_ID.getId());
		this.headerColumns.add(CSVFileCreationConstant.BATCH_CREATION_DATE_TIME.getId());
		LOGGER.info("Add 2nd header column type in csv :" + CSVFileCreationConstant.BATCH_CREATION_DATE_TIME.getId());
		this.headerColumns.add(CSVFileCreationConstant.CURRENT_DATE.getId());
		LOGGER.info("Add 3rd header column type in csv :" + CSVFileCreationConstant.CURRENT_DATE.getId());
		this.headerColumns.add(CSVFileCreationConstant.CURRENT_TIME.getId());
		LOGGER.info("Add 4th header column type in csv :" + CSVFileCreationConstant.CURRENT_TIME.getId());
		this.headerColumns.add(CSVFileCreationConstant.DOC_ID.getId());
		LOGGER.info("Add 5th header column type in csv :" + CSVFileCreationConstant.DOC_ID.getId());
		this.headerColumns.add(CSVFileCreationConstant.TRIGGER_TYPE.getId());
		LOGGER.info("Add 6th header column type in csv :" + CSVFileCreationConstant.TRIGGER_TYPE.getId());
		this.headerColumns.add(CSVFileCreationConstant.TRIGGER.getId());
		LOGGER.info("Add 7th header column type in csv :" + CSVFileCreationConstant.TRIGGER.getId());
		this.headerColumns.add(CSVFileCreationConstant.BOOKMARK_LEVEL.getId());
		LOGGER.info("Add 8th header column type in csv :" + CSVFileCreationConstant.BOOKMARK_LEVEL.getId());
		this.headerColumns.add(CSVFileCreationConstant.PREFIX.getId());
		LOGGER.info("Add 9th header column type in csv :" + CSVFileCreationConstant.PREFIX.getId());
		this.headerColumns.add(CSVFileCreationConstant.BOOKMARK_VALUE.getId());
		LOGGER.info("Add 10th header column type in csv :" + CSVFileCreationConstant.BOOKMARK_VALUE.getId());
		this.headerColumns.add(CSVFileCreationConstant.SUFFIX.getId());
		LOGGER.info("Add 11th header column type in csv :" + CSVFileCreationConstant.SUFFIX.getId());
		this.headerColumns.add(CSVFileCreationConstant.PG_COUNT.getId());
		LOGGER.info("Add 12th header column type in csv :" + CSVFileCreationConstant.PG_COUNT.getId());
		this.headerColumns.add(CSVFileCreationConstant.BOOKMARK_CREATED.getId());
		LOGGER.info("Add 13th header column type in csv :" + CSVFileCreationConstant.BOOKMARK_CREATED.getId());
		this.headerColumns.add(CSVFileCreationConstant.PLACEHOLDER.getId());
		LOGGER.info("Add 14th header column type in csv :" + CSVFileCreationConstant.PLACEHOLDER.getId());
		this.headerColumns.add(CSVFileCreationConstant.BATCH_FIELDS.getId());
		LOGGER.info("Add 15th header column type in csv :" + CSVFileCreationConstant.BATCH_FIELDS.getId());
		LOGGER.info("Finished adding header column type in csv");
	}

	public ExportToExcel() {
		this.workbook = new HSSFWorkbook();
		this.sheetName = CSVFileCreationConstant.EPHESOFT.getId();
		this.boldFont = this.workbook.createFont();
		this.boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		addHeaderColumns();
	}

	public ExportToExcel(final String sheetName) {
		this.workbook = new HSSFWorkbook();
		this.sheetName = sheetName;
		this.boldFont = this.workbook.createFont();
		this.boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		addHeaderColumns();
	}

	/**
	 * This API is used to write the data into CSV file.
	 * 
	 * @param outputStream {@link OutputStream}
	 * @param batch {@link Batch}
	 * @param batchInstance {@link BatchInstance}
	 * @throws NestableException
	 * @throws DCMAApplicationException
	 */
	private void writeStandaloneData(final OutputStream outputStream, final Batch batch, final BatchInstance batchInstance)
			throws NestableException, DCMAApplicationException {
		// Generate data for CSV file
		generate(this.workbook.createSheet(this.sheetName), batch, batchInstance);
		try {
			// Create the CSV file.
			this.workbook.write(outputStream);
		} catch (IOException e) {
			LOGGER.error("Unable to write data into CSV file :" + e.getMessage(), e);
			throw new DCMAApplicationException("Unable to write data into CSV file :" + e.getMessage(), e);
		}
	}

	/**
	 * This API is used to generate data for CSV file and write the information in the CSV file cells.
	 * 
	 * @param sheet {@link HSSFSheet}
	 * @param batch {@link Batch}
	 * @param batchInstance {@link BatchInstance}
	 * @throws NestableException
	 */
	private void generate(final HSSFSheet sheet, final Batch batch, final BatchInstance batchInstance) throws NestableException {
		int currentRow = 0;
		HSSFRow row = sheet.createRow(currentRow);
		int numCols = this.headerColumns.size();

		// Create header row
		for (int headerRowIndex = 0; headerRowIndex < numCols; headerRowIndex++) {
			String title = (String) this.headerColumns.get(headerRowIndex);
			writeCell(row, headerRowIndex, title, HSSFColor.YELLOW.index, this.boldFont);
		}
		currentRow++;

		List<Document> documentList = batch.getDocuments().getDocument();

		// Create information for cell data
		for (int documentIndex = 0; documentIndex < documentList.size(); documentIndex++) {
			List<DocField> documentLevelFields = ((Document) documentList.get(documentIndex)).getDocumentLevelFields()
					.getDocumentLevelField();
			for (int docFieldIndex = 0; docFieldIndex < documentLevelFields.size(); docFieldIndex++) {
				row = sheet.createRow(currentRow++);
				for (int i = 0; i < numCols; i++) {
					writeDataToCell(row, i, batch, (Document) documentList.get(documentIndex), (DocField) documentLevelFields
							.get(docFieldIndex), batchInstance);
				}

			}
		}

		// Auto size the columns
		for (int autosizeIndex = 0; autosizeIndex < numCols; autosizeIndex++) {
			sheet.autoSizeColumn((short) autosizeIndex);
		}
	}

	/**
	 * This API added data into cells of CSV sheet.
	 * 
	 * @param row {@link HSSFRow}
	 * @param columnID
	 * @param batch {@link Batch}
	 * @param document {@link Document}
	 * @param docField {@link DocField}
	 * @param batchInstance {@link BatchInstance}
	 * @throws NestableException
	 */
	private void writeDataToCell(final HSSFRow row, final int columnID, final Batch batch, final Document document,
			final DocField docField, final BatchInstance batchInstance) throws NestableException {
		switch (columnID) {
			case WRITE_TO_CSV_CASE0:
				// Adding information for BatchID column
				writeCell(row, columnID, batch.getBatchInstanceIdentifier());
				break;
			case WRITE_TO_CSV_CASE1:
				// Adding information for BatchCreationDateTime column
				writeCell(row, columnID, batchInstance.getCreationDate().toString());
				break;
			case WRITE_TO_CSV_CASE2:
				// Adding information for CurrentDate column
				String datePattern = CSVFileCreationConstant.MM_DD_YYYY.getId();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern, Locale.getDefault());
				String batchCreationDate = simpleDateFormat.format(new Date());
				writeCell(row, columnID, batchCreationDate);
				break;
			case WRITE_TO_CSV_CASE3:
				// Adding information for CurrentTime column
				String timePattern = CSVFileCreationConstant.HH_MM_SS.getId();
				SimpleDateFormat simpleTimeFormat = new SimpleDateFormat(timePattern, Locale.getDefault());
				String batchCreationTime = simpleTimeFormat.format(new Date());
				writeCell(row, columnID, batchCreationTime);
				break;
			case WRITE_TO_CSV_CASE4:
				// Adding information for DocID column
				writeCell(row, columnID, document.getIdentifier());
				break;
			case WRITE_TO_CSV_CASE5:
				// Adding information for TriggerType column
				writeCell(row, columnID, docField.getType());
				break;
			case WRITE_TO_CSV_CASE6:
				// Adding information for Trigger column
				writeCell(row, columnID, docField.getName());
				break;
			case WRITE_TO_CSV_CASE7:
				// Adding information for BookmarkLevel column
				writeCell(row, columnID, CSVFileCreationConstant.ONE_VALUE.getId());
				break;
			case WRITE_TO_CSV_CASE8:
				// Adding information for Prefix column
				writeCell(row, columnID, CSVFileCreationConstant.EMPTY_STRING.getId());
				break;
			case WRITE_TO_CSV_CASE9:
				// Adding information for BookmarkValue column
				writeCell(row, columnID, document.getType());
				break;
			case WRITE_TO_CSV_CASE10:
				// Adding information for Suffix column
				writeCell(row, columnID, CSVFileCreationConstant.EMPTY_STRING.getId());
				break;
			case WRITE_TO_CSV_CASE11:
				// Adding information for PgCount column
				writeCell(row, columnID, String.valueOf(document.getPages().getPage().size()));
				break;
			case WRITE_TO_CSV_CASE12:
				// Adding information for BookmarkCreated column
				writeCell(row, columnID, CSVFileCreationConstant.YES.getId());
				break;
			case WRITE_TO_CSV_CASE13:
				// Adding information for Placeholder column
				writeCell(row, columnID, CSVFileCreationConstant.NO_STRING.getId());
				break;
			case WRITE_TO_CSV_CASE14:
				// Adding information for BatchFields column
				if (batch.getBatchLevelFields() != null) {
					StringBuffer batchLevelFieldString = new StringBuffer(CSVFileCreationConstant.EMPTY_STRING.getId());
					for (BatchLevelField batchLevelField : batch.getBatchLevelFields().getBatchLevelField()) {
						batchLevelFieldString.append(batchLevelField.getName());
						batchLevelFieldString.append(CSVFileCreationConstant.COMMA.getId());
					}
					writeCell(row, columnID, batchLevelFieldString.toString().substring(0,
							batchLevelFieldString.toString().length() - 1));
				} else {
					writeCell(row, columnID, CSVFileCreationConstant.EMPTY_STRING.getId());
				}
				break;
			default:
				// Adding default empty value
				writeCell(row, columnID, CSVFileCreationConstant.EMPTY_STRING.getId());
		}
	}

	/**
	 * This API is used to write the data into CSV file.
	 * 
	 * @param file {@link File}
	 * @param batch {@link Batch}
	 * @param batchInstance {@link BatchInstance}
	 * @throws NestableException
	 * @throws DCMAApplicationException
	 */
	public void writeStandaloneData(final File file, final Batch batch, final BatchInstance batchInstance) throws NestableException,
			DCMAApplicationException {
		try {
			writeStandaloneData(new FileOutputStream(file), batch, batchInstance);
		} catch (FileNotFoundException e) {
			LOGGER.error("Unable to write data in exported CSV file :" + e.getMessage(), e);
			throw new DCMAApplicationException("Unable to write data in CSV file" + e.getMessage(), e);
		}
	}

	/**
	 * This API added data into the selected cell.
	 * 
	 * @param row {@link HSSFRow}
	 * @param col
	 * @param value {@link String}
	 * @throws NestableException
	 */
	private void writeCell(final HSSFRow row, final int col, final String value) throws NestableException {
		writeCell(row, col, value, null, null);
	}

	/**
	 * This API added data into the selected cell with selected back ground color and font.
	 * 
	 * @param row {@link HSSFRow}
	 * @param col
	 * @param value {@link String}
	 * @param bgColor {@link Short}
	 * @param font {@link HSSFFont}
	 * @throws NestableException
	 */
	private void writeCell(final HSSFRow row, final int col, final String value, final Short bgColor, final HSSFFont font)
			throws NestableException {
		HSSFCell cell = HSSFCellUtil.createCell(row, col, value);
		if (value != null) {
			if (font != null) {
				HSSFCellStyle style = this.workbook.createCellStyle();
				style.setFont(font);
				cell.setCellStyle(style);
			}

			// Added data into cell
			HSSFCellUtil.setCellStyleProperty(cell, this.workbook, CSVFileCreationConstant.BORDER_RIGHT.getId(),
					HSSFColor.GREY_50_PERCENT.index);

			if (bgColor != null) {
				HSSFCellUtil.setCellStyleProperty(cell, this.workbook, CSVFileCreationConstant.FILL_FOREGROUND_COLOR.getId(), bgColor);
				HSSFCellUtil.setCellStyleProperty(cell, this.workbook, CSVFileCreationConstant.FILL_PATTERN.getId(),
						HSSFCellStyle.SOLID_FOREGROUND);
			}
		}
	}
}
