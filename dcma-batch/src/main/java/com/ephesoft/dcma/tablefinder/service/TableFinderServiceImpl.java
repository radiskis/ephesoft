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

package com.ephesoft.dcma.tablefinder.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.schema.Column;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.Field;
import com.ephesoft.dcma.batch.schema.HeaderRow;
import com.ephesoft.dcma.batch.schema.Row;
import com.ephesoft.dcma.batch.schema.DataTable.Rows;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.common.HocrUtil;
import com.ephesoft.dcma.common.LineDataCarrier;
import com.ephesoft.dcma.core.common.ExpressionEvaluator;
import com.ephesoft.dcma.core.common.TableExtractionTechnique;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.TableColumnsInfo;
import com.ephesoft.dcma.da.domain.TableInfo;
import com.ephesoft.dcma.kvfinder.KVFinderConstants;
import com.ephesoft.dcma.tablefinder.constants.TableExtractionConstants;
import com.ephesoft.dcma.tablefinder.data.DataCarrier;

/**
 * This service provides APIs for retrieving table data from HOCR page.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.tablefinder.service.TableFinderService
 */
public class TableFinderServiceImpl implements TableFinderService {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(TableFinderServiceImpl.class);

	/**
	 * Confidence score.
	 */
	private String confidenceScore;

	/**
	 * To get Confidence Score.
	 * 
	 * @return the confidenceScore
	 */
	public final String getConfidenceScore() {
		return confidenceScore;
	}

	/**
	 * To set Confidence Score.
	 * 
	 * @param confidenceScore String
	 */
	public final void setConfidenceScore(final String confidenceScore) {
		this.confidenceScore = confidenceScore;
	}

	/**
	 * Width of line.
	 */
	private String widthOfLine;

	/**
	 * Gap between words of a column.
	 */
	private int gapBetweenColumnWords;

	/**
	 * To get Width Of Line.
	 * 
	 * @return the widthOfLine
	 */
	public String getWidthOfLine() {
		return widthOfLine;
	}

	/**
	 * To get Gap Between Column Words.
	 * 
	 * @return int
	 */
	public int getGapBetweenColumnWords() {
		return gapBetweenColumnWords;
	}

	/**
	 * To set Gap Between Column Words.
	 * 
	 * @param gapBetweenColumnWords
	 */
	public void setGapBetweenColumnWords(final int gapBetweenColumnWords) {
		this.gapBetweenColumnWords = gapBetweenColumnWords;
	}

	/**
	 * To set Width Of Line.
	 * 
	 * @param widthOfLine String
	 */
	public void setWidthOfLine(final String widthOfLine) {
		this.widthOfLine = widthOfLine;
	}

	/**
	 * To find Table Data.
	 * 
	 * @param inputDataCarrier List<TableInfo>
	 * @param hocrPage HocrPage
	 * @param maxResults int
	 * @return DataTable
	 * @throws DCMAApplicationException
	 */
	public DataTable findTableData(final List<TableInfo> inputDataCarrier, final HocrPage hocrPage, final int maxResults)
			throws DCMAApplicationException {

		return extractFields(inputDataCarrier, hocrPage);

	}

	private final DataTable extractFields(final List<TableInfo> tableInfoList, final HocrPage hocrPage)
			throws DCMAApplicationException {
		return readAllDataTables(tableInfoList, hocrPage);

	}

	private DataTable readAllDataTables(final List<TableInfo> tableInfoList, final HocrPage hocrPage) throws DCMAApplicationException {

		final DataTable dataTable = new DataTable();
		// Map to store the column header info against column name.
		final Map<Integer, DataCarrier> colHeaderInfoMap = new HashMap<Integer, DataCarrier>();
		for (final TableInfo tableInfo : tableInfoList) {
			if (null == tableInfo) {
				LOGGER.info("Table info is null.");
				continue;
			}
			LOGGER.info("Extracting data for table : " + tableInfo.getName());
			final List<LineDataCarrier> lineDataCarrierList = new ArrayList<LineDataCarrier>();
			final String tableName = tableInfo.getName();
			final String startPattern = tableInfo.getStartPattern();
			final String endPattern = tableInfo.getEndPattern();
			String tableExtractionAPI = tableInfo.getTableExtractionAPI();
			boolean regexValidationRequired = false;
			boolean colHeaderValidationRequired = false;
			boolean colCoordValidationRequired = false;
			if (tableExtractionAPI != null && !tableExtractionAPI.isEmpty()) {
				regexValidationRequired = tableExtractionAPI.toUpperCase(Locale.getDefault()).contains(
						TableExtractionTechnique.REGEX_VALIDATION.name());
				colHeaderValidationRequired = tableExtractionAPI.toUpperCase(Locale.getDefault()).contains(
						TableExtractionTechnique.COLUMN_HEADER_VALIDATION.name());
				colCoordValidationRequired = tableExtractionAPI.toUpperCase(Locale.getDefault()).contains(
						TableExtractionTechnique.COLUMN_COORDINATES_VALIDATION.name());
			}
			if (!regexValidationRequired && !colHeaderValidationRequired && !colCoordValidationRequired) {
				tableExtractionAPI = TableExtractionTechnique.REGEX_VALIDATION.name();
				LOGGER.info("No validation method specified by admin. Using regex validation for table extraction.");
				regexValidationRequired = true;
			}
			if (null == tableName || tableName.isEmpty()) {
				throw new DCMAApplicationException("Table name is null or empty.");
			}

			if (null == startPattern || startPattern.isEmpty()) {
				throw new DCMAApplicationException("Table start pattern is null or empty.");
			}

			if (null == endPattern || endPattern.isEmpty()) {
				throw new DCMAApplicationException("Table end pattern is null or empty.");
			}
			dataTable.setName(tableName);
			HeaderRow headerRow = dataTable.getHeaderRow();
			if (null == headerRow) {
				headerRow = new HeaderRow();
				dataTable.setHeaderRow(headerRow);
			}
			HeaderRow.Columns columnsHeader = headerRow.getColumns();
			if (null == columnsHeader) {
				columnsHeader = new HeaderRow.Columns();
				headerRow.setColumns(columnsHeader);
			}
			final List<Column> columnHeaderList = columnsHeader.getColumn();
			final List<TableColumnsInfo> tableColumnsInfoList = tableInfo.getTableColumnsInfo();
			if (null == tableColumnsInfoList || tableColumnsInfoList.isEmpty()) {
				LOGGER.error("TableColumnsInfo list is null or empty.");
				continue;
			}
			Rows rows = dataTable.getRows();
			if (null == rows) {
				rows = new Rows();
				dataTable.setRows(rows);
			}
			final List<Row> rowList = rows.getRow();
			boolean isMandatoryUsed = false;
			for (final TableColumnsInfo tableColumnsInfo : tableColumnsInfoList) {
				// Create the table header first and then all the columns for the header.
				final Column column = new Column();
				final String nameOfColumn = tableColumnsInfo.getColumnName();
				final String colHeaderRegex = tableColumnsInfo.getColumnHeaderPattern();
				column.setName(nameOfColumn);
				columnHeaderList.add(column);
				colHeaderInfoMap.put(tableColumnsInfoList.indexOf(tableColumnsInfo), new DataCarrier(null,
						TableExtractionConstants.ZERO, colHeaderRegex));
				if (!isMandatoryUsed) {
					isMandatoryUsed |= tableColumnsInfo.isMandatory();
				}
			}

			int defaultWidthOfMultipleLines = TableExtractionConstants.ZERO;
			try {
				if (tableInfo.getWidthOfMultiline() != null) {
					defaultWidthOfMultipleLines = tableInfo.getWidthOfMultiline();
				}
			} catch (final NumberFormatException e) {
				LOGGER.error("Width of multiline is either invalid or not specified.");
			}
			searchAllRowOfTables(hocrPage, lineDataCarrierList, startPattern, endPattern, isMandatoryUsed, defaultWidthOfMultipleLines);
			// call method to populate the columnHeaderInfoMap.
			if (colHeaderValidationRequired) {
				setColumnHeaderInfo(lineDataCarrierList, colHeaderInfoMap, tableColumnsInfoList);
			}
			addDataTablesValues(lineDataCarrierList, tableColumnsInfoList, rowList, colHeaderInfoMap, tableExtractionAPI,
					regexValidationRequired, colHeaderValidationRequired, colCoordValidationRequired);
			colHeaderInfoMap.clear();
		}
		return dataTable;
	}

	/**
	 * This method extracts and stores the column header information in map.
	 * 
	 * @param lineDataCarrierList {@link List<{@link LineDataCarrier}>}
	 * @param colHeaderInfoMap {@link Map<{@link Integer}, {@link DataCarrier}>}
	 * @param tableColumnsInfoList {@link List<{@link TableColumnsInfo}>}
	 * @throws DCMAApplicationException
	 */
	private void setColumnHeaderInfo(final List<LineDataCarrier> lineDataCarrierList,
			final Map<Integer, DataCarrier> colHeaderInfoMap, final List<TableColumnsInfo> tableColumnsInfoList)
			throws DCMAApplicationException {
		for (final TableColumnsInfo tableColumnsInfo : tableColumnsInfoList) {
			final DataCarrier colHeaderInfo = colHeaderInfoMap.get(tableColumnsInfoList.indexOf(tableColumnsInfo));
			if (null != colHeaderInfo && null != colHeaderInfo.getValue()) {
				addColumnHeaderData(lineDataCarrierList, tableColumnsInfo, colHeaderInfo);
			}
		}
	}

	private void addColumnHeaderData(final List<LineDataCarrier> lineDataCarrierList, final TableColumnsInfo tableColumnsInfo,
			final DataCarrier colHeaderInfo) throws DCMAApplicationException {
		for (final LineDataCarrier lineDataCarrier : lineDataCarrierList) {
			String rowData = null;
			if (null != lineDataCarrier) {
				rowData = lineDataCarrier.getLineRowData();
				LOGGER.info("Line Row Data = " + rowData);
				setColumnHeaderSpan(rowData, colHeaderInfo, lineDataCarrier.getSpanList(), lineDataCarrier);
				if (null != colHeaderInfo.getSpan()) {
					LOGGER.info("Header span found for column : " + tableColumnsInfo.getColumnName() + "; Span : "
							+ colHeaderInfo.getValue());
					break;
				}
			}
		}
	}

	/**
	 * This method extracts the table data.
	 * 
	 * @param lineDataCarrierList {@link List<{@link LineDataCarrier}>}
	 * @param tableColumnsInfoList {@link List<{@link TableColumnInfo}>}
	 * @param rowList {@link List<{@link Row}>}
	 * @param colHeaderInfoMap {@link Map<{@link Integer}, {@link DataCarrier}>}
	 * @param tableExtractionAPI {@link String}
	 * @param regexValidationRequired boolean
	 * @param colHeaderValidationRequired boolean
	 * @param colCoordValidationRequired boolean
	 * @throws DCMAApplicationException
	 */
	private void addDataTablesValues(final List<LineDataCarrier> lineDataCarrierList,
			final List<TableColumnsInfo> tableColumnsInfoList, final List<Row> rowList,
			final Map<Integer, DataCarrier> colHeaderInfoMap, final String tableExtractionAPI, final boolean regexValidationRequired,
			final boolean colHeaderValidationRequired, final boolean colCoordValidationRequired) throws DCMAApplicationException {
		boolean isRowAvaliable = false;
		boolean isRowValidForAllRequiredColumns = true;
		boolean isRowValidForAllMandatoryColumns = true;
		final ExpressionEvaluator<Boolean> expressionEvaluator = new ExpressionEvaluator<Boolean>(tableExtractionAPI
				.toUpperCase(Locale.getDefault()));
		Row previousRow = new Row();

		for (final LineDataCarrier lineDataCarrier : lineDataCarrierList) {
			final String pageID = lineDataCarrier.getPageID();
			final String rowData = lineDataCarrier.getLineRowData();
			final List<Span> spanList = lineDataCarrier.getSpanList();
			LOGGER.info("Row Data : " + rowData);
			isRowValidForAllRequiredColumns = true;
			isRowValidForAllMandatoryColumns = true;
			final Row row = new Row();
			Row.Columns columnsRow = row.getColumns();
			if (null == columnsRow) {
				columnsRow = new Row.Columns();
				row.setColumns(columnsRow);
			}
			row.setRowCoordinates(lineDataCarrier.getRowCoordinates());

			final List<Column> columnRowList = columnsRow.getColumn();
			isRowAvaliable = false;
			for (final TableColumnsInfo tableColumnsInfo : tableColumnsInfoList) {
				LOGGER.info("Extracting column data for column = " + tableColumnsInfo.getColumnName());
				// Search for all the table row data one by one.
				final boolean isRequired = tableColumnsInfo.isRequired();
				final Integer columnCoordX0 = tableColumnsInfo.getColumnStartCoordinate();
				final Integer columnCoordX1 = tableColumnsInfo.getColumnEndCoordinate();
				final boolean isMandatory = tableColumnsInfo.isMandatory();

				final Column column = new Column();
				setColumnProperties(pageID, column, null, TableExtractionConstants.ZERO);
				columnRowList.add(column);
				DataCarrier colHeaderDataCarrier = null;
				final Integer indexOfTableColumn = tableColumnsInfoList.indexOf(tableColumnsInfo);
				if (null != colHeaderInfoMap && !colHeaderInfoMap.isEmpty()) {
					colHeaderDataCarrier = colHeaderInfoMap.get(indexOfTableColumn);
				}
				boolean isRegexValidationPassed = false;
				boolean isColHeaderValidationPassed = false;
				boolean isColCoordValidationPassed = false;
				boolean furtherValidationRequired = true;
				Coordinates valueCoordinates = null;
				if (colHeaderValidationRequired) {
					LOGGER.info("Applying Column Header Validation for table extraction....");
					if (null != colHeaderDataCarrier && null != colHeaderDataCarrier.getValue()) {
						isColHeaderValidationPassed = getColumnDataByHeaderValidation(colHeaderDataCarrier, column, spanList,
								lineDataCarrier);
					}
					LOGGER.info("Getting rectangle coordinates for the value. ");
					valueCoordinates = HocrUtil.getRectangleCoordinates(column.getCoordinatesList().getCoordinates());
				}
				if (isDataValid(expressionEvaluator, false, isColHeaderValidationPassed, false)
						|| !isDataValid(expressionEvaluator, true, isColHeaderValidationPassed, true)) {
					LOGGER.info("No further validation required.....");
					furtherValidationRequired = false;
				}
				if (furtherValidationRequired && colCoordValidationRequired) {
					LOGGER.info("Applying Column Coordinates Validation for table extraction....");
					if (isDataValid(expressionEvaluator, false, false, true)) {
						isColCoordValidationPassed = getColumnDataByColCoordValidation(columnCoordX0, columnCoordX1, column, spanList);
					} else if (isColHeaderValidationPassed
							&& valueCoordinates != null
							&& isColumnValidWithColCoord(valueCoordinates.getX0().intValue(), valueCoordinates.getX1().intValue(),
									columnCoordX0, columnCoordX1)) {
						isColCoordValidationPassed = true;
					} else {
						isColCoordValidationPassed = getColumnDataByColCoordValidation(columnCoordX0, columnCoordX1, column, spanList);
					}
					LOGGER.info("Getting rectangle coordinates for the value. ");
					valueCoordinates = HocrUtil.getRectangleCoordinates(column.getCoordinatesList().getCoordinates());
					if ((isDataValid(expressionEvaluator, false, isColHeaderValidationPassed, isColCoordValidationPassed) || !isDataValid(
							expressionEvaluator, true, isColHeaderValidationPassed, isColCoordValidationPassed))) {
						LOGGER.info("No further validation required....");
						furtherValidationRequired = false;
					}
					if (isColCoordValidationPassed) {
						column.setValid(Boolean.TRUE);
					}
				}
				if (furtherValidationRequired && regexValidationRequired) {
					LOGGER.info("Applying Regex Validation for table extraction ..... ");
					if (isDataValid(expressionEvaluator, true, false, false)) {
						isRegexValidationPassed = applyRegexValidation(lineDataCarrier, tableColumnsInfo, column, null, spanList,
								rowData, pageID);
					} else {
						isRegexValidationPassed = applyRegexValidation(lineDataCarrier, tableColumnsInfo, column, valueCoordinates,
								spanList, rowData, pageID);
					}
				}
				if (isDataValid(expressionEvaluator, isRegexValidationPassed, isColHeaderValidationPassed, isColCoordValidationPassed)) {
					isRowAvaliable = true;
					LOGGER.info("Data valid with respect to all te validations..");
				} else {
					setColumnProperties(pageID, column, TableExtractionConstants.EMPTY, TableExtractionConstants.ZERO);
				}
				LOGGER.info("Checking if row contains valid data for required columns...");
				if (isRequired && !column.isValid()) {
					LOGGER.info("Data not valid for required column: " + column.getName());
					isRowValidForAllRequiredColumns = false;
					break;
				}
				LOGGER.info("Checking if row contains valid data for mandatory columns...");
				if (isRowValidForAllMandatoryColumns && isMandatory && (column.getValue() == null || column.getValue().isEmpty())) {
					isRowValidForAllMandatoryColumns = false;
				}
			}

			if (isRowAvaliable && isRowValidForAllRequiredColumns) {
				LOGGER.info("Merging rows of multiline data...");
				if (!isRowValidForAllMandatoryColumns && previousRow.getColumns() != null) {
					mergeMultilineRows(previousRow, columnRowList);
					setMergedRowCoordinates(previousRow, row);
				} else {
					LOGGER.info("Adding row to the row list.");
					rowList.add(row);
					previousRow = row;
				}
			}
		}
	}

	/**
	 * This method merges the value and coordinates of the multiline rows.
	 * 
	 * @param previousRow {@link Row}
	 * @param currentRowColumnsList {@link List<{@link Column}>}
	 */
	private void mergeMultilineRows(Row previousRow, final List<Column> currentRowColumnsList) {
		List<Column> previousRowColumnsList = previousRow.getColumns().getColumn();
		Iterator<Column> previousRowColumnsListIterator = previousRowColumnsList.iterator();
		Iterator<Column> currentRowColumnsListIterator = currentRowColumnsList.iterator();

		while (previousRowColumnsListIterator.hasNext() && currentRowColumnsListIterator.hasNext()) {
			Column previousRowColumn = previousRowColumnsListIterator.next();
			Column currentRowColumn = currentRowColumnsListIterator.next();
			if (validateColumnForMerging(currentRowColumn)) {
				StringBuilder newColumnValue = new StringBuilder();
				if (previousRowColumn != null && previousRowColumn.getValue() != null && !previousRowColumn.getValue().isEmpty()) {
					newColumnValue.append(previousRowColumn.getValue());
					newColumnValue.append(TableExtractionConstants.SPACE);
				}
				newColumnValue.append(currentRowColumn.getValue());
				previousRowColumn.setValue(newColumnValue.toString());
				if (previousRowColumn.getCoordinatesList() == null) {
					previousRowColumn.setCoordinatesList(new CoordinatesList());
				}
				for (Coordinates coordinates : currentRowColumn.getCoordinatesList().getCoordinates()) {
					previousRowColumn.getCoordinatesList().getCoordinates().add(coordinates);
				}
			}
		}
	}

	/**
	 * This column validates a column for merging.
	 * 
	 * @param column {@link Column}
	 * @return boolean {@link boolean}
	 */
	private boolean validateColumnForMerging(final Column column) {
		if (column != null && column.getValue() != null && !column.getValue().isEmpty() && column.getCoordinatesList() != null
				&& !column.getCoordinatesList().getCoordinates().isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method gets the rectangle coordinates for the new merged row value.
	 * 
	 * @param coordinatesList {@link Row}
	 * @param currentRow {@link Row}
	 */
	private void setMergedRowCoordinates(Row previousRow, final Row currentRow) {
		final Coordinates coordinates = new Coordinates();
		BigInteger minX0 = BigInteger.ZERO;
		BigInteger minY0 = BigInteger.ZERO;
		BigInteger maxX1 = BigInteger.ZERO;
		BigInteger maxY1 = BigInteger.ZERO;

		final Coordinates previousRowCoordinates = previousRow.getRowCoordinates();
		if (previousRowCoordinates == null) {
			return;
		}
		final BigInteger previousRowX0 = previousRowCoordinates.getX0();
		final BigInteger previousRowY0 = previousRowCoordinates.getY0();
		final BigInteger previousRowX1 = previousRowCoordinates.getX1();
		final BigInteger previousRowY1 = previousRowCoordinates.getY1();

		final Coordinates currentRowCoordinates = currentRow.getRowCoordinates();
		if (currentRowCoordinates == null) {
			return;
		}
		final BigInteger currentRowX0 = currentRowCoordinates.getX0();
		final BigInteger currentRowY0 = currentRowCoordinates.getY0();
		final BigInteger currentRowX1 = currentRowCoordinates.getX1();
		final BigInteger currentRowY1 = currentRowCoordinates.getY1();

		if (previousRowX0.compareTo(currentRowX0) < TableExtractionConstants.ZERO) {
			minX0 = previousRowX0;
		} else {
			minX0 = currentRowX0;
		}
		if (previousRowY0.compareTo(currentRowY0) < TableExtractionConstants.ZERO) {
			minY0 = previousRowY0;
		} else {
			minY0 = currentRowY0;
		}
		if (previousRowX1.compareTo(currentRowX1) > TableExtractionConstants.ZERO) {
			maxX1 = previousRowX1;
		} else {
			maxX1 = currentRowX1;
		}
		if (previousRowY1.compareTo(currentRowY1) > TableExtractionConstants.ZERO) {
			maxY1 = previousRowY1;
		} else {
			maxY1 = currentRowY1;
		}

		coordinates.setX0(minX0);
		coordinates.setX1(maxX1);
		coordinates.setY0(minY0);
		coordinates.setY1(maxY1);
		previousRow.setRowCoordinates(coordinates);
	}

	/**
	 * This method extracts column data by applying regex validation.
	 * 
	 * @param lineDataCarrier {@link LineDataCarrier}
	 * @param tableColumnsInfo {@link TableColumnInfo}
	 * @param column {@link Column}
	 * @param columnCoordinates {@link Coordinates}
	 * @param spanList {@link List<{@link Span}>}
	 * @param rowData {@link String}
	 * @param pageID {@link String}
	 * @return boolean
	 * @throws DCMAApplicationException
	 */
	private boolean applyRegexValidation(final LineDataCarrier lineDataCarrier, final TableColumnsInfo tableColumnsInfo,
			final Column column, final Coordinates columnCoordinates, final List<Span> spanList, final String rowData,
			final String pageID) throws DCMAApplicationException {
		LOGGER.info("Applying Regex Validation for table extraction ..... ");
		boolean isRegexValidationPassed = false;
		LOGGER.info("Extracting column data for column = " + tableColumnsInfo.getColumnName());
		final String patternOfColumnData = tableColumnsInfo.getColumnPattern();
		final String betweenLeft = tableColumnsInfo.getBetweenLeft();
		final String betweenRight = tableColumnsInfo.getBetweenRight();
		LOGGER.info("Column Pattern : " + patternOfColumnData);
		LOGGER.info("Between Left Pattern : " + betweenLeft);
		LOGGER.info("Between Right Pattern : " + betweenRight);
		if (patternOfColumnData != null && !patternOfColumnData.isEmpty()) {
			final List<DataCarrier> dataCarrierList = findPattern(rowData, patternOfColumnData, spanList);
			if (null == dataCarrierList || dataCarrierList.isEmpty()) {
				LOGGER.info("No data found for table column = " + tableColumnsInfo.getColumnName());
			} else {
				column.setValue(TableExtractionConstants.EMPTY);
				column.setConfidence(TableExtractionConstants.HUNDRED);
				boolean isFound = false;
				for (final DataCarrier outputDataCarrier : dataCarrierList) {
					if (outputDataCarrier == null || null == outputDataCarrier.getValue() || null == outputDataCarrier.getSpan()) {
						continue;
					}
					final String outputValue = outputDataCarrier.getValue();
					final String[] foundValArr = outputValue.split(KVFinderConstants.SPACE);
					final Span outputSpan = outputDataCarrier.getSpan();
					final Integer spanIndex = lineDataCarrier.getIndexOfSpan(outputSpan);
					final List<Coordinates> coordinatesList = new ArrayList<Coordinates>();
					coordinatesList.add(outputSpan.getCoordinates());
					boolean isValid = true;
					if (spanIndex != null) {
						if (null != foundValArr && foundValArr.length > TableExtractionConstants.ONE) {
							for (int localSpanIndex = spanIndex, count = TableExtractionConstants.ZERO; count < foundValArr.length
									- TableExtractionConstants.ONE; localSpanIndex++, count++) {
								final Span rightSpan = lineDataCarrier.getRightSpan(localSpanIndex);
								if (null != rightSpan) {
									coordinatesList.add(rightSpan.getCoordinates());
								}
							}
						}
						final Coordinates valCoordinates = HocrUtil.getRectangleCoordinates(coordinatesList);
						if (valCoordinates != null
								&& columnCoordinates != null
								&& !isColumnValidWithColCoord(valCoordinates.getX0().intValue(), valCoordinates.getX1().intValue(),
										columnCoordinates.getX0().intValue(), columnCoordinates.getX1().intValue())) {
							isValid = false;
						}
						if (isValid) {
							if (!isFound) {
								column.setValid(true);
								isRegexValidationPassed = true;
								column.setValue(outputValue);

								setColumnCoordinates(coordinatesList, column);
								// Validate the field with left right pattern specified by admin.
								if (betweenLeft != null && !betweenLeft.isEmpty()) {
									if (betweenRight != null && !betweenRight.isEmpty()) {
										final Span leftSpan = lineDataCarrier.getLeftSpan(spanIndex);
										if (null == leftSpan) {
											LOGGER.info("Left Span is null. betweenLeft = " + betweenLeft);
											continue;
										}
										final Span rightSpan = lineDataCarrier.getRightSpan(spanIndex);
										if (null == rightSpan) {
											LOGGER.info("Right Span is null. betweenRight = " + betweenRight);
											continue;
										}
										final DataCarrier leftDataCarrier = findPattern(leftSpan, betweenLeft);
										final DataCarrier rightDataCarrier = findPattern(rightSpan, betweenRight);
										if (null != leftDataCarrier && null != rightDataCarrier) {
											LOGGER.info(TableExtractionConstants.BETWEEN_LEFT_FOUND + leftDataCarrier.getValue());
											LOGGER.info(TableExtractionConstants.BETWEEN_LEFT_FOUND + rightDataCarrier.getValue());
											column.setValid(true);
											isFound = true;
											continue;
										}
									} else {
										final Span leftSpan = lineDataCarrier.getLeftSpan(spanIndex);
										if (null == leftSpan) {
											LOGGER.info("Left Span is null. betweenLeft = " + betweenLeft);
											continue;
										}
										final DataCarrier leftDataCarrier = findPattern(leftSpan, betweenLeft);
										if (null != leftDataCarrier) {
											LOGGER.info(TableExtractionConstants.BETWEEN_LEFT_FOUND + leftDataCarrier.getValue());
											column.setValid(true);
											isFound = true;
											continue;
										}
									}
								} else {
									if (betweenRight != null && !betweenRight.isEmpty()) {
										final Span rightSpan = lineDataCarrier.getRightSpan(spanIndex);
										if (null == rightSpan) {
											LOGGER.info("Right Span is null. betweenRight = " + betweenRight);
											continue;
										}
										final DataCarrier rightDataCarrier = findPattern(rightSpan, betweenRight);
										if (null != rightDataCarrier) {
											LOGGER.info(TableExtractionConstants.BETWEEN_LEFT_FOUND + rightDataCarrier.getValue());
											column.setValid(true);
											isFound = true;
											continue;
										}
									} else {
										column.setValid(true);
										isFound = true;
									}
								}
							}
							addAlternateValues(column, pageID, outputSpan);
						}
					}
				}
			}
		}
		return isRegexValidationPassed;
	}

	/**
	 * This method checks if extracted column data is valid with validation methods.
	 * 
	 * @param expressionEvaluator {@link ExpressEvaluator<{@link Boolean}>}
	 * @param isRegexValidationPassed boolean
	 * @param isColumnHeaderValidationPassed boolean
	 * @param isColumnCoordValidationPassed boolean
	 * @return boolean
	 */
	private boolean isDataValid(final ExpressionEvaluator<Boolean> expressionEvaluator, final boolean isRegexValidationPassed,
			final boolean isColumnHeaderValidationPassed, final boolean isColumnCoordValidationPassed) {
		boolean isDataValid = false;
		try {
			expressionEvaluator.putValue(TableExtractionTechnique.REGEX_VALIDATION.name(), Boolean.valueOf(isRegexValidationPassed));
			expressionEvaluator.putValue(TableExtractionTechnique.COLUMN_HEADER_VALIDATION.name(), Boolean
					.valueOf(isColumnHeaderValidationPassed));
			expressionEvaluator.putValue(TableExtractionTechnique.COLUMN_COORDINATES_VALIDATION.name(), Boolean
					.valueOf(isColumnCoordValidationPassed));
			isDataValid = expressionEvaluator.eval();
		} catch (final ScriptException se) {
			LOGGER.error("Exception occurred while ", se);
		}
		return isDataValid;
	}

	/**
	 * This method extracts the column data based on the column coordinates.
	 * 
	 * @param columnCoordX0 {@link Integer}
	 * @param columnCoordX1 {@link Integer}
	 * @param column {@link Column}
	 * @param spanList {@link List<{@link Span}>}
	 * @return boolean
	 */
	private boolean getColumnDataByColCoordValidation(final Integer columnCoordX0, final Integer columnCoordX1, final Column column,
			final List<Span> spanList) {
		LOGGER.info("Applying Column Coordinates Validation for table extraction ..... ");
		final StringBuffer outputValue = new StringBuffer();
		final CoordinatesList coordinatesList = new CoordinatesList();
		boolean isColCoordValidationPassed = false;
		for (final Span span : spanList) {
			if (span != null && span.getValue() != null && !span.getValue().isEmpty() && span.getCoordinates() != null) {
				final Coordinates coordinates = span.getCoordinates();
				LOGGER.info("Checking if the value: " + span.getValue() + " is valid with respect to column coordinates: X0="
						+ columnCoordX0 + " ,X1=" + columnCoordX1);
				if (isColumnValidWithColCoord(coordinates.getX0().intValue(), coordinates.getX1().intValue(), columnCoordX0,
						columnCoordX1)) {
					LOGGER.info(span.getValue() + " valid with column coordinates .");
					appendToColumnData(outputValue, coordinatesList, span);
				}
			}
		}
		if (outputValue.length() > TableExtractionConstants.ZERO && coordinatesList != null) {
			LOGGER.info("Column value = " + outputValue);
			column.setValue(outputValue.toString());
			column.setCoordinatesList(coordinatesList);
			isColCoordValidationPassed = true;
		}
		LOGGER.info("Column coordinate validation passed = " + isColCoordValidationPassed);
		return isColCoordValidationPassed;
	}

	/**
	 * This method will extract the column data by applying column header validation and column coordinates validation, if needed.
	 * Column coordinates validation will be applied based on the value of colCoordalidationRequired.
	 * 
	 * @param colHeaderDataCarrier {@link DataCarrier}
	 * @param column {@link Column}
	 * @param spanList {@link List<{@link Span}>}
	 * @param lineDataCarrier {@link LineDataCarrier}
	 * @return boolean
	 */
	private boolean getColumnDataByHeaderValidation(final DataCarrier colHeaderDataCarrier, final Column column,
			final List<Span> spanList, final LineDataCarrier lineDataCarrier) {
		LOGGER.info("Applying Column Header Validation for table extraction ..... ");
		final StringBuffer outputValue = new StringBuffer();
		final CoordinatesList coordinatesList = new CoordinatesList();
		boolean isColHeaderValidationPassed = false;
		List<Span> leftSpanList = null;
		List<Span> rightSpanList = null;
		boolean isFirst = true;
		Span lastSpan = null;
		for (final Span span : spanList) {
			if (span != null && span.getValue() != null && !span.getValue().isEmpty() && span.getCoordinates() != null) {
				final Coordinates coordinates = span.getCoordinates();
				LOGGER.info("Checking if the value: " + span.getValue() + " is valid with respect to column header: "
						+ colHeaderDataCarrier.getValue());
				if (isValidCoordinatesWithColumnHeader(coordinates, colHeaderDataCarrier.getSpan())) {
					LOGGER.info(span.getValue() + " valid with column header.");
					lastSpan = span;
					if (isFirst) {
						isFirst = false;
						appendToColumnData(outputValue, coordinatesList, span);
						leftSpanList = lineDataCarrier.appendSpansLeft(span, gapBetweenColumnWords);
						if (leftSpanList != null && !leftSpanList.isEmpty()) {
							for (final Span leftSpan : leftSpanList) {
								if (leftSpan.getValue() != null) {
									appendToColumnData(outputValue, coordinatesList, leftSpan);
								}
							}
						}
					} else {
						appendToColumnData(outputValue, coordinatesList, span);
					}
				}
			}
		}
		if (!outputValue.toString().isEmpty() && coordinatesList != null) {
			rightSpanList = lineDataCarrier.appendSpansRight(lastSpan, gapBetweenColumnWords);
			if (rightSpanList != null && !rightSpanList.isEmpty()) {
				for (final Span rightSpan : rightSpanList) {
					if (rightSpan.getValue() != null) {
						appendToColumnData(outputValue, coordinatesList, rightSpan);
					}
				}
			}
			LOGGER.info(outputValue + " added to column value.");
			column.setValue(outputValue.toString());
			column.setValid(Boolean.TRUE);
			column.setCoordinatesList(coordinatesList);
			isColHeaderValidationPassed = true;
		}
		LOGGER.info("Column header validation passed = " + isColHeaderValidationPassed);
		return isColHeaderValidationPassed;
	}

	private void appendToColumnData(final StringBuffer outputValue, final CoordinatesList coordinatesList, final Span span) {
		outputValue.append(span.getValue());
		outputValue.append(TableExtractionConstants.SPACE);
		coordinatesList.getCoordinates().add(span.getCoordinates());
	}

	/**
	 * API to set column properties.
	 * 
	 * @param pageID {@link String}
	 * @param column {@link Column}
	 * @param value {@link String}
	 * @param confidence float
	 */
	private void setColumnProperties(final String pageID, final Column column, final String value, final float confidence) {
		final Column.AlternateValues alternateValues = new Column.AlternateValues();
		column.setName(null);
		column.setConfidence(confidence);
		column.setPage(pageID);
		column.setValid(false);
		column.setCoordinatesList(new CoordinatesList());
		column.setAlternateValues(alternateValues);
		column.setValue(value);
	}

	/**
	 * This method adds alternate value to the column.
	 * 
	 * @param column {@link Column}
	 * @param pageID {@link String}
	 * @param outputSpan {@link Span}
	 */
	private void addAlternateValues(final Column column, final String pageID, final Span outputSpan) {
		final Coordinates hocrCoordinates = outputSpan.getCoordinates();
		if (null != outputSpan && null != outputSpan.getValue() && null != hocrCoordinates) {
			final Field alternateValue = new Field();
			alternateValue.setValue(outputSpan.getValue());
			alternateValue.setName(null);
			alternateValue.setConfidence(TableExtractionConstants.DEFAULT_CONFIDENCE_VALUE);
			alternateValue.setPage(pageID);
			final CoordinatesList coordinatesList = new CoordinatesList();
			alternateValue.setCoordinatesList(coordinatesList);
			final Coordinates coordinates = new Coordinates();
			coordinates.setX0(hocrCoordinates.getX0());
			coordinates.setX1(hocrCoordinates.getX1());
			coordinates.setY0(hocrCoordinates.getY0());
			coordinates.setY1(hocrCoordinates.getY1());
			alternateValue.getCoordinatesList().getCoordinates().add(coordinates);
			LOGGER.info("Add alternate value = " + alternateValue.getValue() + " to column = " + column.getName());
			column.getAlternateValues().getAlternateValue().add(alternateValue);
		}
	}

	/**
	 * This method checks if span is valid with respect to the column header specified by admin and if is valid, then adds the
	 * coordinates of current span to the coordinates list of a column.
	 * 
	 * @param coordinatesList {@link List<{@link Coordinates}>}
	 * @param column {@link Column}
	 */
	private void setColumnCoordinates(final List<Coordinates> coordinatesList, final Column column) {
		if (null != column && null != coordinatesList && !coordinatesList.isEmpty()) {
			column.getCoordinatesList().getCoordinates().clear();
			for (final Coordinates coordinates : coordinatesList) {
				column.getCoordinatesList().getCoordinates().add(coordinates);
			}
		}
	}

	/**
	 * Method to check if span is valid with respect to the column header specified by admin.
	 * 
	 * @param valueCorodinates {@link Coordinates}
	 * @param headerSpan {@link Span}
	 * @return boolean
	 */
	private boolean isValidCoordinatesWithColumnHeader(final Coordinates valueCoordinates, final Span headerSpan) {
		LOGGER.info("Entering method isValidCoordinatesWithColumnHeader.");
		Coordinates headerSpanHocrCoordinates = null;
		boolean isValid = false;
		if (null != headerSpan && null != valueCoordinates && null != headerSpan.getCoordinates()) {
			headerSpanHocrCoordinates = headerSpan.getCoordinates();
			final BigInteger outputSpanX0 = valueCoordinates.getX0();
			final BigInteger outputSpanX1 = valueCoordinates.getX1();
			final BigInteger outputSpanY0 = valueCoordinates.getY0();
			final BigInteger headerSpanX0 = headerSpanHocrCoordinates.getX0();
			final BigInteger headerSpanX1 = headerSpanHocrCoordinates.getX1();
			final BigInteger headerSpanY1 = headerSpanHocrCoordinates.getY1();
			LOGGER.info("Value Coordinates: X0=" + outputSpanX0 + ", X1=" + outputSpanX1 + ", Y0=" + outputSpanY0);
			LOGGER.info("Column Header Coordinates: X0=" + headerSpanX0 + ", X1=" + headerSpanX1 + ", Y1=" + headerSpanY1);
			if (outputSpanX0 != null
					&& outputSpanX0 != null
					&& outputSpanY0 != null
					&& headerSpanX0 != null
					&& headerSpanX1 != null
					&& headerSpanY1 != null
					&& (outputSpanY0.compareTo(headerSpanY1) == 1 && ((outputSpanX0.compareTo(headerSpanX0) == 1 && outputSpanX0
							.compareTo(headerSpanX1) == -1)
							|| (outputSpanX1.compareTo(headerSpanX0) == 1 && outputSpanX1.compareTo(headerSpanX1) == -1)
							|| (outputSpanX0.compareTo(headerSpanX0) == -1 && outputSpanX1.compareTo(headerSpanX1) == 1)
							|| outputSpanX0.compareTo(headerSpanX0) == 0 || outputSpanX1.compareTo(headerSpanX1) == 0))) {
				isValid = true;
			}
			LOGGER.info("Is span valid with column header : " + isValid);
		}
		LOGGER.info("Exiting method isValidCoordinatesWithColumnHeader.");
		return isValid;
	}

	/**
	 * Method to check if value coordinates passed are valid with respect to column coordinates.
	 * 
	 * @param valueX0 {@link Integer}
	 * @param valueX1 {@link Integer}
	 * @param columnX0 {@link Integer}
	 * @param columnX1 {@link Integer}
	 * @return boolean
	 */
	private boolean isColumnValidWithColCoord(final Integer valueX0, final Integer valueX1, final Integer columnX0,
			final Integer columnX1) {
		LOGGER.info("Entering method isColumnValidWithColCoord.....");
		boolean isValid = false;
		LOGGER.info("Column Coordinates: X0=" + valueX0 + " ,X1=" + valueX1);
		LOGGER.info("Value Coordinates: X0=" + columnX0 + " ,X1=" + columnX1);
		if (valueX0 != null
				&& valueX1 != null
				&& columnX0 != null
				&& columnX0 != null
				&& (columnX0 != 0 || columnX1 != 0)
				&& ((valueX0.compareTo(columnX0) == 1 && valueX0.compareTo(columnX1) == -1)
						|| (valueX1.compareTo(columnX0) == 1 && valueX1.compareTo(columnX1) == -1)
						|| (valueX0.compareTo(columnX0) == -1 && valueX1.compareTo(columnX1) == 1) || valueX0.compareTo(columnX0) == 0 || valueX1
						.compareTo(columnX1) == 0)) {
			isValid = true;
		}
		LOGGER.info("Is value coordinates valid with column coordinates : " + isValid);
		LOGGER.info("Exiting method isColumnValidWithColCoord.....");
		return isValid;
	}

	/**
	 * This method extracts all the rows, i.e, all rows between start and end pattern.
	 * 
	 * @param hocrPage {@link HocrPage}
	 * @param lineDataCarrierList {@link List<{@link LineDataCarrier}>}
	 * @param startPattern {@link String}
	 * @param endPattern {@link String}
	 * @throws DCMAApplicationException
	 */
	private void searchAllRowOfTables(final HocrPage hocrPage, final List<LineDataCarrier> lineDataCarrierList,
			final String startPattern, final String endPattern, final boolean isMandatoryUsed, final int defaultWidthOfMultline)
			throws DCMAApplicationException {
		String errMsg = null;
		final String pageID = hocrPage.getPageID();
		List<DataCarrier> startDataCarrier = null;
		LOGGER.info("HocrPage page ID : " + pageID);
		final Spans spans = hocrPage.getSpans();
		LineDataCarrier lineDataCarrier = new LineDataCarrier(pageID);
		// lineDataCarrierList.add(lineDataCarrier);
		if (null != spans) {
			final List<Span> linkedList = getSortedSpanList(spans);
			if (null == linkedList || linkedList.isEmpty()) {
				LOGGER.info("Return linked list is null for the page id = " + pageID);
				return;
			}
			final ListIterator<Span> listItr = linkedList.listIterator();
			if (null == listItr) {
				LOGGER.info("Return list iterator is null for the page id = " + pageID);
				return;
			}
			int defaultValue = TableExtractionConstants.TWENTY;
			try {
				defaultValue = Integer.parseInt(getWidthOfLine());
			} catch (final NumberFormatException nfe) {
				LOGGER.error(nfe.getMessage(), nfe);
			}

			if (startDataCarrier == null) {
				LOGGER.info("Find start pattern.");
				// iterating the sorted sorted span list for searching the start pattern
				while (listItr.hasNext()) {
					final Span span = listItr.next();
					try {
						List<Span> spanList = lineDataCarrier.getSpanList();
						if (spanList.isEmpty()) {
							spanList.add(span);
						} else {
							final Span lastSpan = spanList.get(spanList.size() - 1);
							final int compare = lastSpan.getCoordinates().getY1().intValue()
									- span.getCoordinates().getY1().intValue();
							if (compare >= -defaultValue && compare <= defaultValue) {
								spanList.add(span);
							} else {
								lineDataCarrier = new LineDataCarrier(pageID);
								spanList = lineDataCarrier.getSpanList();
								spanList.add(span);
							}
						}
						startDataCarrier = findPattern(lineDataCarrier.getLineRowData(), startPattern, spanList);
						if (null != startDataCarrier) {
							LOGGER.info("Start pattern found for table where start pattern : " + startPattern);
							lineDataCarrierList.add(lineDataCarrier);
							break;
						}
					} catch (final Exception e) {
						errMsg = e.getMessage();
						LOGGER.error(errMsg, e);
					}
				}
			}
			if (startDataCarrier != null) {
				LOGGER.info("Find end pattern.");
				while (listItr.hasNext()) {
					final Span span = listItr.next();
					List<Span> spanList = lineDataCarrier.getSpanList();
					if (spanList.isEmpty()) {
						spanList.add(span);
					} else {
						final Span lastSpan = spanList.get(spanList.size() - 1);
						final int compare = lastSpan.getCoordinates().getY1().intValue() - span.getCoordinates().getY1().intValue();
						if (compare >= -defaultValue && compare <= defaultValue) {
							spanList.add(span);
						} else if (!isMandatoryUsed && defaultWidthOfMultline != 0 && compare >= -defaultWidthOfMultline
								&& compare <= defaultWidthOfMultline) {
							spanList.add(span);
						} else {
							lineDataCarrier = new LineDataCarrier(pageID);
							lineDataCarrierList.add(lineDataCarrier);
							spanList = lineDataCarrier.getSpanList();
							spanList.add(span);
						}
					}
					final List<DataCarrier> endDataCarrier = findPattern(lineDataCarrier.getLineRowData(), endPattern, lineDataCarrier
							.getSpanList());
					if (null != endDataCarrier && !endDataCarrier.isEmpty()) {
						LOGGER.info("End pattern found for table where end pattern : " + endPattern);
						break;
					}
				}
			} else {
				LOGGER.info("No start pattern found for table where start pattern : " + startPattern);
			}
		}
	}

	/**
	 * Method is responsible for finding the patterns and returned the found data List.
	 * 
	 * @param span {@link Span}
	 * @param patternStr {@link String}
	 * @return DataCarrier
	 * @throws DCMAApplicationException
	 */
	private final DataCarrier findPattern(final Span span, final String patternStr) throws DCMAApplicationException {
		String errMsg = null;
		DataCarrier dataCarrier = null;
		final CharSequence inputStr = span.getValue();
		if (null == inputStr || TableExtractionConstants.EMPTY.equals(inputStr)) {
			errMsg = "Invalid input character sequence.";
			LOGGER.info(errMsg);
		} else {
			if (null == patternStr || TableExtractionConstants.EMPTY.equals(patternStr)) {
				errMsg = "Invalid input pattern sequence.";
				throw new DCMAApplicationException(errMsg);
			}
			// Compile and use regular expression
			final Pattern pattern = Pattern.compile(patternStr);
			final Matcher matcher = pattern.matcher(inputStr);
			while (matcher.find()) {
				// Get all groups for this match
				for (int i = 0; i <= matcher.groupCount(); i++) {
					final String groupStr = matcher.group(i);
					if (groupStr != null) {
						int confidenceInt = TableExtractionConstants.DEFAULT_CONFIDENCE_VALUE;
						try {
							confidenceInt = Integer.parseInt(getConfidenceScore());
						} catch (final NumberFormatException nfe) {
							LOGGER.error(nfe.getMessage(), nfe);
						}
						final float confidence = (groupStr.length() * confidenceInt) / inputStr.length();
						dataCarrier = new DataCarrier(span, confidence, groupStr);
						LOGGER.info(groupStr);
					}
				}
			}
		}
		return dataCarrier;
	}

	/**
	 * Method is responsible for finding the patterns and returned the found data List.
	 * 
	 * @param value {@link String}
	 * @param patternStr {@link String}
	 * @param spanList {@link List< {@link Span}>}
	 * @return List<DataCarrier>
	 * @throws DCMAApplicationException
	 */
	private final List<DataCarrier> findPattern(final String value, final String patternStr, final List<Span> spanList)
			throws DCMAApplicationException {
		String errMsg = null;
		final CharSequence inputStr = value;
		List<DataCarrier> dataCarrierList = null;
		if (null == inputStr || TableExtractionConstants.EMPTY.equals(inputStr)) {
			errMsg = "Invalid input character sequence.";
			LOGGER.info(errMsg);
		} else {
			if (null == patternStr || TableExtractionConstants.EMPTY.equals(patternStr)) {
				errMsg = "Invalid input pattern sequence.";
				throw new DCMAApplicationException(errMsg);
			}
			// Compile and use regular expression
			final Pattern pattern = Pattern.compile(patternStr);
			final Matcher matcher = pattern.matcher(inputStr);
			while (matcher.find()) {
				// Get all groups for this match
				for (int i = 0; i <= matcher.groupCount(); i++) {
					final String groupStr = matcher.group(i);
					final int startIndex = matcher.start();
					Span matchedSpan = null;
					if (groupStr != null) {
						final float confidence = (groupStr.length() * TableExtractionConstants.DEFAULT_CONFIDENCE_VALUE)
								/ inputStr.length();
						matchedSpan = getMatchedSpan(spanList, startIndex);
						final DataCarrier dataCarrier = new DataCarrier(matchedSpan, confidence, groupStr);
						if (dataCarrierList == null) {
							dataCarrierList = new ArrayList<DataCarrier>();
						}
						dataCarrierList.add(dataCarrier);
						LOGGER.info(groupStr);
					}
				}
			}
		}
		return dataCarrierList;
	}

	/**
	 * API to get the sorted span list.
	 * 
	 * @param spans {@link Spans}
	 * @return linkedList {@link List<Span>}
	 */
	private List<Span> getSortedSpanList(final Spans spans) {
		final List<Span> spanList = spans.getSpan();
		int defaultValue = TableExtractionConstants.TWENTY;
		try {
			defaultValue = Integer.parseInt(getWidthOfLine());
		} catch (NumberFormatException nfe) {
			LOGGER.error(nfe.getMessage(), nfe);
			defaultValue = TableExtractionConstants.TWENTY;
		}
		final int lineWidth = defaultValue;
		final Set<Span> set = new TreeSet<Span>(new Comparator<Span>() {

			public int compare(final Span firstSpan, final Span secSpan) {
				final BigInteger s1Y1 = firstSpan.getCoordinates().getY1();
				final BigInteger s2Y1 = secSpan.getCoordinates().getY1();
				int returnValue = 0;
				final int compare = s1Y1.intValue() - s2Y1.intValue();
				if (compare >= -lineWidth && compare <= lineWidth) {
					final BigInteger s1X1 = firstSpan.getCoordinates().getX1();
					final BigInteger s2X1 = secSpan.getCoordinates().getX1();
					returnValue = s1X1.compareTo(s2X1);
				} else {
					returnValue = s1Y1.compareTo(s2Y1);
				}
				return returnValue;
			}
		});
		set.addAll(spanList);
		final List<Span> linkedList = new LinkedList<Span>();
		linkedList.addAll(set);
		// TODO add the clear method to remove all elements of set since it not required after adding it to linked list.
		// set.clear();
		return linkedList;
	}

	/**
	 * API to set the column header span.
	 * 
	 * @param inputData {@link String}
	 * @param colHeaderInfo {@link DataCarrier}
	 * @param spanList {@link List<{@link Span}>}
	 * @param lineDataCarrier {@link LineDataCarrier}
	 * @throws DCMAApplicationException
	 */
	private final void setColumnHeaderSpan(final String inputData, final DataCarrier colHeaderInfo, final List<Span> spanList,
			final LineDataCarrier lineDataCarrier) throws DCMAApplicationException {
		String errMsg = null;
		final int confidenceInt = TableExtractionConstants.DEFAULT_CONFIDENCE_VALUE;
		float previousConfidence = 0;
		final CharSequence inputStr = inputData;
		final List<Coordinates> coordinatesList = new ArrayList<Coordinates>();
		if (null == inputStr || KVFinderConstants.EMPTY.equals(inputStr)) {
			errMsg = "Invalid input character sequence.";
			LOGGER.info(errMsg);
		} else {
			if (null == colHeaderInfo || null == colHeaderInfo.getValue() || KVFinderConstants.EMPTY.equals(colHeaderInfo.getValue())) {
				errMsg = "Invalid input pattern sequence.";
			} else {
				// Compile and use regular expression
				final Pattern pattern = Pattern.compile(colHeaderInfo.getValue());
				final Matcher matcher = pattern.matcher(inputStr);
				while (matcher.find()) {
					// Get all groups for this match
					for (int i = 0; i <= matcher.groupCount(); i++) {
						final String groupStr = matcher.group(i);
						final int startIndex = matcher.start();
						Span matchedSpan = null;
						if (groupStr != null) {
							final String[] foundValArr = groupStr.split(KVFinderConstants.SPACE);
							final float confidence = (groupStr.length() * confidenceInt) / inputStr.length();
							LOGGER.info("Matched Value : " + groupStr + " ,Confidence : " + confidence);
							if (confidence > previousConfidence) {
								coordinatesList.clear();
								final Span span = new Span();
								colHeaderInfo.setSpan(span);
								matchedSpan = getMatchedSpan(spanList, startIndex);
								coordinatesList.add(matchedSpan.getCoordinates());
								span.setValue(matchedSpan.getValue());
								colHeaderInfo.setConfidence(confidence);
								previousConfidence = confidence;
								final int spanIndex = lineDataCarrier.getIndexOfSpan(matchedSpan);
								if (null != foundValArr && foundValArr.length > 1) {
									for (int localSpanIndex = spanIndex, count = 0; count < foundValArr.length - 1; localSpanIndex++, count++) {
										final Span rightSpan = lineDataCarrier.getRightSpan(localSpanIndex);
										if (null != rightSpan) {
											coordinatesList.add(rightSpan.getCoordinates());
										}
									}
								}
								span.setCoordinates(HocrUtil.getRectangleCoordinates(coordinatesList));
							}
							LOGGER.info(groupStr);
						}
					}
				}
			}
		}
	}

	/**
	 * API to get the matched span.
	 * 
	 * @param spanList {@link List<{@link Span}>}
	 * @param startIndex int
	 * @return Span
	 */
	private Span getMatchedSpan(final List<Span> spanList, final int startIndex) {
		int spanIndex = 0;
		boolean isFirstSpan = Boolean.TRUE;
		Span matchedSpan = null;
		for (final Span span : spanList) {
			if (null != span && null != span.getValue()) {
				spanIndex = spanIndex + span.getValue().length();
				if (!isFirstSpan) {
					spanIndex = spanIndex + 1;
				}
				if (spanIndex > startIndex) {
					matchedSpan = span;
					break;
				}
			}
			isFirstSpan = Boolean.FALSE;
		}
		return matchedSpan;
	}
}
