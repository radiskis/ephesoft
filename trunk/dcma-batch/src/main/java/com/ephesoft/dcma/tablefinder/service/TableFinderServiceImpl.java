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

package com.ephesoft.dcma.tablefinder.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.TableColumnsInfo;
import com.ephesoft.dcma.da.domain.TableInfo;
import com.ephesoft.dcma.kvfinder.KVFinderConstants;
import com.ephesoft.dcma.kvfinder.data.OutputDataCarrier;
import com.ephesoft.dcma.tablefinder.constants.TableExtractionConstants;
import com.ephesoft.dcma.tablefinder.data.DataCarrier;
import com.ephesoft.dcma.tablefinder.data.LineDataCarrier;

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
	 * @return the confidenceScore
	 */
	public final String getConfidenceScore() {
		return confidenceScore;
	}

	/**
	 * @param confidenceScore the confidenceScore to set
	 */
	public final void setConfidenceScore(final String confidenceScore) {
		this.confidenceScore = confidenceScore;
	}

	/**
	 * Width of line.
	 */
	private String widthOfLine;

	/**
	 * @return the widthOfLine
	 */
	public String getWidthOfLine() {
		return widthOfLine;
	}

	/**
	 * @param widthOfLine the widthOfLine to set
	 */
	public void setWidthOfLine(String widthOfLine) {
		this.widthOfLine = widthOfLine;
	}

	public DataTable findTableData(final List<TableInfo> inputDataCarrier, final HocrPage hocrPage, final int maxResults)
			throws DCMAApplicationException {

		DataTable dataTable = extractFields(inputDataCarrier, hocrPage, maxResults);
		return dataTable;
	}

	private final DataTable extractFields(final List<TableInfo> tableInfoList, final HocrPage hocrPage, final int maxResults)
			throws DCMAApplicationException {
		DataTable dataTable = readAllDataTables(tableInfoList, hocrPage);
		return dataTable;
	}

	/**
	 * readAllDataTables.
	 * 
	 * @param tableInfoList
	 * @param startDataCarrier
	 * @param lineDataCarrierList
	 * @param dataTableList
	 * @param pageList
	 * @return
	 * @throws DCMAApplicationException
	 */
	private DataTable readAllDataTables(final List<TableInfo> tableInfoList, final HocrPage hocrPage) throws DCMAApplicationException {

		DataTable dataTable = new DataTable();
		DataCarrier startDataCarrier = null;
		// Map to store the column header info against column name.
		Map<String, DataCarrier> colHeaderInfoMap = new HashMap<String, DataCarrier>();
		for (TableInfo tableInfo : tableInfoList) {
			if (null == tableInfo) {
				LOGGER.info("Table info is null.");
				continue;
			}

			List<LineDataCarrier> lineDataCarrierList = new ArrayList<LineDataCarrier>();

			final String tableName = tableInfo.getName();
			final String startPattern = tableInfo.getStartPattern();
			final String endPattern = tableInfo.getEndPattern();

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
			List<Column> columnHeaderList = columnsHeader.getColumn();

			List<TableColumnsInfo> tableColumnsInfoList = tableInfo.getTableColumnsInfo();

			if (null == tableColumnsInfoList || tableColumnsInfoList.isEmpty()) {
				LOGGER.error("TableColumnsInfo list is null or empty.");
				continue;
			}

			Rows rows = dataTable.getRows();
			if (null == rows) {
				rows = new Rows();
				dataTable.setRows(rows);
			}

			List<Row> rowList = rows.getRow();

			for (TableColumnsInfo tableColumnsInfo : tableColumnsInfoList) {
				// Create the table header first and then all the columns for the header.
				Column column = new Column();
				String nameOfColumn = tableColumnsInfo.getColumnName();
				String colHeaderRegex = tableColumnsInfo.getColumnHeaderPattern();
				column.setName(nameOfColumn);
				columnHeaderList.add(column);
				colHeaderInfoMap.put(nameOfColumn, new DataCarrier(null, 0, colHeaderRegex));
			}

			startDataCarrier = searchAllRowOfTables(hocrPage, lineDataCarrierList, startPattern, endPattern, startDataCarrier);
			// call method to populate the columnHeaderInfoMap.
			setColumnHeaderInfo(lineDataCarrierList, colHeaderInfoMap, tableColumnsInfoList);
			startDataCarrier = addDataTablesValues(lineDataCarrierList, tableColumnsInfoList, rowList, startDataCarrier,
					colHeaderInfoMap);
			colHeaderInfoMap.clear();
		}

		return dataTable;
	}

	/**
	 * This method extracts and stores the column header information in map.
	 * 
	 * @param lineDataCarrierList
	 * @param colHeaderInfoMap
	 * @param tableColumnsInfoList
	 * @throws DCMAApplicationException
	 */
	private void setColumnHeaderInfo(final List<LineDataCarrier> lineDataCarrierList, final Map<String, DataCarrier> colHeaderInfoMap,
			final List<TableColumnsInfo> tableColumnsInfoList) throws DCMAApplicationException {
		for (TableColumnsInfo tableColumnsInfo : tableColumnsInfoList) {
			DataCarrier colHeaderInfo = colHeaderInfoMap.get(tableColumnsInfo.getColumnName());
			if (null != colHeaderInfo && null != colHeaderInfo.getValue()) {
				for (LineDataCarrier lineDataCarrier : lineDataCarrierList) {
					String rowData = null;
					if (null != lineDataCarrier) {
						rowData = lineDataCarrier.getLineRowData();
						LOGGER.info("Line Row Data = " + rowData);
						setColumnHeaderSpan(rowData, colHeaderInfo, lineDataCarrier.getSpanList());
						if (null != colHeaderInfo.getSpan()) {
							LOGGER.info("Header span found for column : " + tableColumnsInfo.getColumnName() + "; Span : "
									+ colHeaderInfo.getValue());
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * This method extracts the table data.
	 * 
	 * @param lineDataCarrierList
	 * @param tableColumnsInfoList
	 * @param rowList
	 * @param startDataCarrier
	 * @param colHeaderInfoMap
	 * @return
	 * @throws DCMAApplicationException
	 */
	private DataCarrier addDataTablesValues(final List<LineDataCarrier> lineDataCarrierList,
			final List<TableColumnsInfo> tableColumnsInfoList, final List<Row> rowList, DataCarrier startDataCarrier,
			final Map<String, DataCarrier> colHeaderInfoMap) throws DCMAApplicationException {
		boolean isRowAvaliable = false;
		boolean isRowValidForAllMandatoryColumns = true;
		for (LineDataCarrier lineDataCarrier : lineDataCarrierList) {
			String pageID = lineDataCarrier.getPageID();
			String rowData = lineDataCarrier.getLineRowData();
			List<Span> spanList = lineDataCarrier.getSpanList();
			LOGGER.info("Row Data : " + rowData);
			isRowValidForAllMandatoryColumns = true;
			Row row = new Row();
			Row.Columns columnsRow = row.getColumns();

			if (null == columnsRow) {
				columnsRow = new Row.Columns();
				row.setColumns(columnsRow);
			}

			row.setRowCoordinates(lineDataCarrier.getRowCoordinates());
			List<Column> columnRowList = columnsRow.getColumn();
			isRowAvaliable = false;

			for (TableColumnsInfo tableColumnsInfo : tableColumnsInfoList) {
				// Search for all the table row data one by one.
				LOGGER.info("Extracting column data for column = " + tableColumnsInfo.getColumnName());
				String patternOfColumnData = tableColumnsInfo.getColumnPattern();
				String betweenLeft = tableColumnsInfo.getBetweenLeft();
				String betweenRight = tableColumnsInfo.getBetweenRight();
				boolean isRequired = tableColumnsInfo.isRequired();

				List<OutputDataCarrier> dataCarrierList = findPattern(rowData, patternOfColumnData, spanList);
				Column column = new Column();

				if (null == dataCarrierList || dataCarrierList.isEmpty()) {
					LOGGER.info("No data found for table column = " + tableColumnsInfo.getColumnName());
					createNewColumn(pageID, column, null, 0);
					columnRowList.add(column);
				} else {
					createNewColumn(pageID, column, TableExtractionConstants.EMPTY, TableExtractionConstants.DEFAULT_CONFIDENCE_VALUE);
					columnRowList.add(column);
					isRowAvaliable = true;
					boolean isFound = false;

					for (final OutputDataCarrier outputDataCarrier : dataCarrierList) {
						if (outputDataCarrier == null || null == outputDataCarrier.getValue() || null == outputDataCarrier.getSpan()) {
							continue;
						}
						boolean isSpanValidWithColumnHeader = true;
						String outputValue = outputDataCarrier.getValue();
						String[] foundValArr = outputValue.split(KVFinderConstants.SPACE);
						Span outputSpan = outputDataCarrier.getSpan();
						final int spanIndex = lineDataCarrier.getIndexOfSpan(outputSpan);
						List<Coordinates> coordinatesList = new ArrayList<Coordinates>();
						coordinatesList.add(outputSpan.getCoordinates());
						int localSpanIndex = spanIndex;
						if (null != foundValArr && foundValArr.length > 1) {
							for (int index = 0; index < foundValArr.length; index++, localSpanIndex++) {
								Span rightSpan = lineDataCarrier.getRightSpan(localSpanIndex);
								if (null != rightSpan) {
									coordinatesList.add(rightSpan.getCoordinates());
								}
							}
						}
						LOGGER.info("Getting rectangle coordinates for the value: " + outputValue);
						Coordinates valueCoordinates = getValueCoordinates(coordinatesList);
						if (!isFound && null != colHeaderInfoMap && !colHeaderInfoMap.isEmpty()) {
							DataCarrier dataCarrier = colHeaderInfoMap.get(tableColumnsInfo.getColumnName());
							// If no column header is specified by admin, do not apply column header verification.
							if (null != dataCarrier && null != dataCarrier.getValue()) {
								LOGGER.info("Checking if the value: " + outputSpan.getValue()
										+ " is valid with respect to column header: " + dataCarrier);
								isSpanValidWithColumnHeader = isValidCoordinatesWithColumnHeader(valueCoordinates, dataCarrier
										.getSpan());
								LOGGER.info(outputSpan.getValue() + " valid with column header : " + isSpanValidWithColumnHeader);
							}
						}
						if (!isFound && isSpanValidWithColumnHeader) {
							column.setValue(outputValue);
							setColumnCoordinates(coordinatesList, column);
							// Validate the field with left right pattern specified by admin.
							if (betweenLeft != null && !betweenLeft.isEmpty()) {
								if (betweenRight != null && !betweenRight.isEmpty()) {
									Span leftSpan = lineDataCarrier.getLeftSpan(spanIndex);
									if (null == leftSpan) {
										LOGGER.info("Left Span is null. betweenLeft = " + betweenLeft);
										continue;
									}
									Span rightSpan = lineDataCarrier.getRightSpan(spanIndex);
									if (null == rightSpan) {
										LOGGER.info("Right Span is null. betweenRight = " + betweenRight);
										continue;
									}
									DataCarrier leftDataCarrier = findPattern(leftSpan, betweenLeft);
									DataCarrier rightDataCarrier = findPattern(rightSpan, betweenRight);
									if (null != leftDataCarrier && null != rightDataCarrier) {
										LOGGER.info("Between left found = " + leftDataCarrier.getValue());
										LOGGER.info("Between left found = " + rightDataCarrier.getValue());
										column.setValid(true);
										isFound = true;
										continue;
									}
								} else {
									Span leftSpan = lineDataCarrier.getLeftSpan(spanIndex);
									if (null == leftSpan) {
										LOGGER.info("Left Span is null. betweenLeft = " + betweenLeft);
										continue;
									}
									DataCarrier leftDataCarrier = findPattern(leftSpan, betweenLeft);
									if (null != leftDataCarrier) {
										LOGGER.info("Between left found = " + leftDataCarrier.getValue());
										column.setValid(true);
										isFound = true;
										continue;
									}
								}
							} else {
								if (betweenRight != null && !betweenRight.isEmpty()) {
									Span rightSpan = lineDataCarrier.getRightSpan(spanIndex);
									if (null == rightSpan) {
										LOGGER.info("Right Span is null. betweenRight = " + betweenRight);
										continue;
									}
									DataCarrier rightDataCarrier = findPattern(rightSpan, betweenRight);
									if (null != rightDataCarrier) {
										LOGGER.info("Between left found = " + rightDataCarrier.getValue());
										column.setValid(true);
										isFound = true;
										continue;
									}
								} else {
									column.setValid(true);
								}
							}
						}
						addAlternateValues(column, pageID, outputSpan);
					}
				}
				// Checking if row contains valid data for every mandatory column
				LOGGER.info("Checking if row contains valid data for every mandatory column");
				if (isRequired && !column.isValid()) {
					isRowValidForAllMandatoryColumns = false;
					LOGGER.info("Data Not valid for required column: " + column.getName());
				}
			}

			if (isRowAvaliable && isRowValidForAllMandatoryColumns) {
				LOGGER.info("Row data : " + rowData);
				rowList.add(row);
				startDataCarrier = null;
			}

		}
		return startDataCarrier;
	}

	/**
	 * This method gets the rectangle coordinates for the value.
	 * 
	 * @param coordinatesList
	 * @return Coordinates
	 */
	private Coordinates getValueCoordinates(final List<Coordinates> coordinatesList) {
		long minX0 = 0;
		long minY0 = 0;
		long maxX1 = 0;
		long maxY1 = 0;
		Coordinates recCoord = new Coordinates();
		boolean isFirstCoord = true;
		for (Coordinates coordinate : coordinatesList) {
			if (isFirstCoord) {
				recCoord = coordinate;
				isFirstCoord = false;
			} else {
				minX0 = recCoord.getX0().longValue();
				minY0 = recCoord.getY0().longValue();
				maxX1 = recCoord.getX1().longValue();
				maxY1 = recCoord.getY1().longValue();
				long coordX0 = coordinate.getX0().longValue();
				long coordY0 = coordinate.getY0().longValue();
				long coordX1 = coordinate.getX1().longValue();
				long coordY1 = coordinate.getY1().longValue();
				if (coordX0 < minX0) {
					recCoord.setX0(coordinate.getX0());
				}
				if (coordY0 < minY0) {
					recCoord.setY0(coordinate.getY0());
				}
				if (coordX1 > maxX1) {
					recCoord.setX1(coordinate.getX1());
				}
				if (coordY1 > maxY1) {
					recCoord.setY1(coordinate.getY1());
				}
			}
		}
		return recCoord;
	}

	private void createNewColumn(final String pageID, final Column column, final String value, final float confidence) {
		Column.AlternateValues alternateValues = new Column.AlternateValues();
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
	 * @param column
	 * @param pageID
	 * @param outputSpan
	 */
	private void addAlternateValues(final Column column, final String pageID, final Span outputSpan) {
		Coordinates hocrCoordinates = outputSpan.getCoordinates();
		if (null != outputSpan && null != outputSpan.getValue() && null != hocrCoordinates) {
			Field alternateValue = new Field();
			alternateValue.setValue(outputSpan.getValue());
			alternateValue.setName(null);
			alternateValue.setConfidence(TableExtractionConstants.DEFAULT_CONFIDENCE_VALUE);
			alternateValue.setPage(pageID);
			CoordinatesList coordinatesList = new CoordinatesList();
			alternateValue.setCoordinatesList(coordinatesList);
			Coordinates coordinates = new Coordinates();
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
	 * @param coordinatesList
	 * @param column
	 * @param colHeaderInfoMap
	 */
	private void setColumnCoordinates(final List<Coordinates> coordinatesList, final Column column) {
		if (null != column && null != coordinatesList && !coordinatesList.isEmpty()) {
			column.getCoordinatesList().getCoordinates().clear();
			for (Coordinates coordinates : coordinatesList) {
				column.getCoordinatesList().getCoordinates().add(coordinates);
			}
		}
	}

	/**
	 * Method to check if span is valid with respect to the column header specified by admin.
	 * 
	 * @param valueCorodinates
	 * @param headerSpan
	 * @return
	 */
	private boolean isValidCoordinatesWithColumnHeader(final Coordinates valueCorodinates, final Span headerSpan) {
		Coordinates headerSpanHocrCoordinates = null;
		boolean isValid = false;
		if (null != headerSpan && null != valueCorodinates && null != headerSpan.getCoordinates()) {
			headerSpanHocrCoordinates = headerSpan.getCoordinates();
			BigInteger outputSpanX0 = valueCorodinates.getX0();
			BigInteger outputSpanX1 = valueCorodinates.getX1();
			BigInteger headerSpanX0 = headerSpanHocrCoordinates.getX0();
			BigInteger headerSpanX1 = headerSpanHocrCoordinates.getX1();
			LOGGER.info("Header Column Coordinates: X0=" + outputSpanX0.intValue() + " ,X1=" + outputSpanX1.intValue());
			if ((outputSpanX0.compareTo(headerSpanX0) == 1 && outputSpanX0.compareTo(headerSpanX1) == -1)
					|| (outputSpanX1.compareTo(headerSpanX0) == 1 && outputSpanX1.compareTo(headerSpanX1) == -1)
					|| (outputSpanX0.compareTo(headerSpanX0) == -1 && outputSpanX1.compareTo(headerSpanX1) == 1)
					|| outputSpanX0.compareTo(headerSpanX0) == 0 || outputSpanX1.compareTo(headerSpanX1) == 0) {
				isValid = true;
			}
		}
		return isValid;
	}

	/**
	 * This method extracts all the rows, i.e, all rows between start and end pattern.
	 * 
	 * @param pageList
	 * @param lineDataCarrierList
	 * @param startDataCarrier
	 * @param startPattern
	 * @param endPattern
	 * @return
	 * @throws DCMAApplicationException
	 */
	private DataCarrier searchAllRowOfTables(final HocrPage hocrPage, final List<LineDataCarrier> lineDataCarrierList,
			final String startPattern, final String endPattern, DataCarrier startDataCarrier) throws DCMAApplicationException {

		String errMsg = null;
		String pageID = hocrPage.getPageID();

		LOGGER.info("HocrPage page ID : " + pageID);
		final Spans spans = hocrPage.getSpans();

		LineDataCarrier lineDataCarrier = new LineDataCarrier(pageID);
		lineDataCarrierList.add(lineDataCarrier);

		if (null != spans) {

			List<Span> linkedList = getSortedSpanList(spans);
			if (null == linkedList || linkedList.isEmpty()) {
				LOGGER.info("Return linked list is null for the page id = " + pageID);
				return null;
			}
			ListIterator<Span> listItr = linkedList.listIterator();
			if (null == listItr) {
				LOGGER.info("Return list iterator is null for the page id = " + pageID);
				return null;
			}
			if (startDataCarrier == null) {
				while (listItr.hasNext()) {
					final Span span = listItr.next();
					try {
						startDataCarrier = findPattern(span, startPattern);
						if (null != startDataCarrier) {
							// nextIndex = listItr.nextIndex();
							LOGGER.info("Start pattern found for table where start pattern : " + startPattern);
							break;
						} else {
							LOGGER.info("No start pattern found for table where start pattern : " + startPattern);
						}
					} catch (Exception e) {
						errMsg = e.getMessage();
						LOGGER.error(errMsg, e);
					}
				}
			}
			if (startDataCarrier != null) {
				while (listItr.hasNext()) {
					final Span span = listItr.next();
					DataCarrier endDataCarrier = findPattern(span, endPattern);
					if (null != endDataCarrier) {
						LOGGER.info("End pattern found for table where end pattern : " + endPattern);
						// continue documentLabel;
						break;
					} else {
						// get the line array as word by word fetch from db all the columns and patterns.
						// validate using left and right pattern.
						List<Span> spanList = lineDataCarrier.getSpanList();
						if (spanList.isEmpty()) {
							spanList.add(span);
						} else {
							Span lastSpan = spanList.get(spanList.size() - 1);
							int compare = lastSpan.getCoordinates().getY1().intValue() - span.getCoordinates().getY1().intValue();
							int defaultValue = 20;
							try {
								defaultValue = Integer.parseInt(getWidthOfLine());
							} catch (NumberFormatException nfe) {
								LOGGER.error(nfe.getMessage(), nfe);
								defaultValue = 20;
							}
							if (compare >= -defaultValue && compare <= defaultValue) {
								spanList.add(span);
							} else {
								lineDataCarrier = new LineDataCarrier(pageID);
								lineDataCarrierList.add(lineDataCarrier);
								spanList = lineDataCarrier.getSpanList();
								spanList.add(span);
							}
						}
					}
				}
			}
		}
		return startDataCarrier;
	}

	/**
	 * Method is responsible for finding the patterns and returned the found data List.
	 * 
	 * @param span Span
	 * @param patternStr String
	 * @return DataCarrier
	 * @throws DCMAApplicationException Check for all the input parameters and find the pattern.
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
						} catch (NumberFormatException nfe) {
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
	 * @param value
	 * @param patternStr
	 * @param spanList
	 * @return
	 * @throws DCMAApplicationException Check for all the input parameters and find the pattern.
	 */
	private final List<OutputDataCarrier> findPattern(final String value, final String patternStr, final List<Span> spanList)
			throws DCMAApplicationException {

		String errMsg = null;
		final CharSequence inputStr = value;
		List<OutputDataCarrier> dataCarrierList = new ArrayList<OutputDataCarrier>();
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
					String groupStr = matcher.group(i);
					final int startIndex = matcher.start();
					Span matchedSpan = null;
					if (groupStr != null) {
						final float confidence = (groupStr.length() * TableExtractionConstants.DEFAULT_CONFIDENCE_VALUE)
								/ inputStr.length();
						matchedSpan = getMatchedSpan(spanList, startIndex);
						OutputDataCarrier dataCarrier = new OutputDataCarrier(matchedSpan, confidence, groupStr);
						dataCarrierList.add(dataCarrier);
						LOGGER.info(groupStr);
					}
				}
			}
		}

		return dataCarrierList;
	}

	private List<Span> getSortedSpanList(final Spans spans) {
		final List<Span> spanList = spans.getSpan();
		int width = 20;
		try {
			width = Integer.parseInt(getWidthOfLine());
		} catch (NumberFormatException nfe) {
			LOGGER.error(nfe.getMessage(), nfe);
			width = 20;
		}
		LOGGER.info("width of line : " + width);
		final int defaultValue = width;
		final Set<Span> setQ = new TreeSet<Span>(new Comparator<Span>() {

			public int compare(final Span firstSpan, final Span secSpan) {
				BigInteger s1Y1 = firstSpan.getCoordinates().getY1();
				BigInteger s2Y1 = secSpan.getCoordinates().getY1();
				int returnValue = s1Y1.intValue() - s2Y1.intValue();
				if (returnValue == 0) {
					returnValue = -1;
				}
				return returnValue;
			}
		});
		setQ.addAll(spanList);
		Set<Span> set = getSortedTreeSet(defaultValue);
		final List<Span> linkedList = new LinkedList<Span>();
		List<Span> sortedSpanList = new ArrayList<Span>();
		boolean isFirst = true;
		int y1Coord = 0;
		for (Span span : setQ) {
			int spanY1Coord = span.getCoordinates().getY1().intValue();
			if (isFirst) {
				y1Coord = spanY1Coord;
				isFirst = false;
			}
			if (spanY1Coord - y1Coord >= -defaultValue && spanY1Coord - y1Coord <= defaultValue) {
				sortedSpanList.add(span);
			} else {
				set.addAll(sortedSpanList);
				LOGGER.info("Sorted Row : " + sortedSpanList);
				linkedList.addAll(set);
				sortedSpanList = new ArrayList<Span>();
				sortedSpanList.add(span);
				set.clear();
				y1Coord = spanY1Coord;
			}
		}
		set.addAll(sortedSpanList);
		linkedList.addAll(set);
		LOGGER.info("Span List sorted");
		return linkedList;
	}

	private Set<Span> getSortedTreeSet(final int defaultValue) {

		final Set<Span> set = new TreeSet<Span>(new Comparator<Span>() {

			public int compare(final Span firstSpan, final Span secSpan) {
				BigInteger s1Y1 = firstSpan.getCoordinates().getY1();
				BigInteger s2Y1 = secSpan.getCoordinates().getY1();
				int returnValue = 0;
				int compare = s1Y1.intValue() - s2Y1.intValue();
				if (compare >= -defaultValue && compare <= defaultValue) {
					BigInteger s1X1 = firstSpan.getCoordinates().getX1();
					BigInteger s2X1 = secSpan.getCoordinates().getX1();
					returnValue = s1X1.compareTo(s2X1);
				} else {
					returnValue = s1Y1.compareTo(s2Y1);
				}
				return returnValue;
			}
		});
		return set;
	}

	private final void setColumnHeaderSpan(final String inputData, final DataCarrier colHeaderInfo, final List<Span> spanList)
			throws DCMAApplicationException {
		String errMsg = null;
		int confidenceInt = TableExtractionConstants.DEFAULT_CONFIDENCE_VALUE;
		float previousConfidence = 0;
		final CharSequence inputStr = inputData;
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
							final float confidence = (groupStr.length() * confidenceInt) / inputStr.length();
							if (confidence > previousConfidence) {
								matchedSpan = getMatchedSpan(spanList, startIndex);
								colHeaderInfo.setSpan(matchedSpan);
								colHeaderInfo.setConfidence(confidence);
								previousConfidence = confidence;
							}
							LOGGER.info(groupStr);
						}
					}
				}
			}
		}
	}

	private Span getMatchedSpan(final List<Span> spanList, final int startIndex) {
		int spanIndex = 0;
		boolean isFirstSpan = Boolean.TRUE;
		Span matchedSpan = null;
		for (Span span : spanList) {
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
