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

package com.ephesoft.dcma.fuzzydb;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.io.FileUtils;
import org.apache.fuzzydb.demo.IndexHTML;
import org.apache.fuzzydb.demo.SearchFiles;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.similar.MoreLikeThis;
import org.apache.lucene.store.FSDirectory;
import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchDynamicPluginConfiguration;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPluginConfiguration;
import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Documents;
import com.ephesoft.dcma.batch.schema.HocrPages;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.hibernate.DynamicHibernateDao;
import com.ephesoft.dcma.core.hibernate.DynamicHibernateDao.ColumnDefinition;
import com.ephesoft.dcma.da.dao.BatchInstanceDao;
import com.ephesoft.dcma.da.service.BatchClassPluginConfigService;
import com.ephesoft.dcma.da.service.DocumentTypeService;
import com.ephesoft.dcma.da.service.FieldTypeService;
import com.ephesoft.dcma.da.service.PageTypeService;

/**
 * This class first indexes the tables corresponding to document types defined in database and then it overwrites/creates the value of
 * document level fields for each document inside batch xml.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.component.ICommonConstants
 * 
 */
@Component
public class FuzzyLuceneEngine implements ICommonConstants {

	private static final String SPLIT_CONSTANT = ";";
	private static final String FUZZYDB_PLUGIN = "FUZZYDB";
	private static final String COLUMN_NAME_ERROR_MSG = "Unable to fetch column names from DB";
	private static final String CONFIDENCE_ERROR_MSG = "Problem generating confidence score for Document :  ";
	private static final String SPACE_DELIMITER = " ";
	private static final String QUERY_STRING_DELIMITER = ":";
	public static final String TIMESTAMP_NAME = "Timestamp";
	public static final String DATE_NAME = "Date";
	public static final String ALLPAGES = "ALLPAGES";
	public static final String FIRSTPAGE = "FIRSTPAGE";
	public static final String INDEX_FIELD = "rowData";
	public static final String COMMA = ",";
	public static final String DOUBLE_QUOTES = "\"";
	public static final String SINGLE_QUOTES = "`";
	public static final String MYSQL_DATABASE = "mysql";
	private static final String EMPTY_STRING = "";
	private static final String WORD_BOUNDRY_REGEX = "\\b";
	private static final String EQUALS = " = ";

	/**
	 * Logger instance for logging using slf4j for logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(FuzzyLuceneEngine.class);

	/**
	 * Instance of BatchSchemaService.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;
	/**
	 * Instance of BatchInstanceDao
	 */
	@Autowired
	private BatchInstanceDao batchInstanceDao;

	/**
	 * Instance of PluginPropertiesService for batch instance.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;
	/**
	 * Instance of PluginPropertiesService for batch class.
	 */
	@Autowired
	@Qualifier("batchClassPluginPropertiesService")
	private PluginPropertiesService pluginPropertiesServiceBatchClass;

	/**
	 * Instance of BatchClassPluginConfigService.
	 */
	@Autowired
	private BatchClassPluginConfigService batchClassPluginConfigService;

	/**
	 * Instance of PageTypeService.
	 */
	@Autowired
	private PageTypeService pageTypeService;

	@Autowired
	private DocumentTypeService documentTypeService;

	/**
	 * Instance of FieldTypeService.
	 */
	@Autowired
	private FieldTypeService fieldTypeService;

	/**
	 * row id which is the primary key or return column name for each table.
	 */
	private transient String rowID;

	/**
	 * List of batch class IDs in properties file for which learn DB should be called
	 */
	private transient String batchClassIDList;

	/**
	 * Ignore words separated by ';' that are to be ignored while performing fuzzy db extraction.
	 */
	private transient String ignoreList;

	/**
	 * Ignore words array that are to be ignored while performing fuzzy db extraction.
	 */
	private String[] ignoreWordList;

	/**
	 * Set ignore word list.
	 * 
	 * @param ignoreList
	 */
	public void setIgnoreList(String ignoreList) {
		this.ignoreList = ignoreList;
		this.ignoreWordList = ignoreList.split(SPLIT_CONSTANT);
	}

	/**
	 * @return the batchSchemaService
	 */
	public BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * @param batchSchemaService the batchSchemaService to set
	 */
	public void setBatchSchemaService(BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * @return the pluginPropertiesService
	 */
	public PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	/**
	 * @param pluginPropertiesService the pluginPropertiesService to set
	 */
	public void setPluginPropertiesService(PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	/**
	 * @return the pageTypeService
	 */
	public PageTypeService getPageTypeService() {
		return pageTypeService;
	}

	/**
	 * @param pageTypeService the pageTypeService to set
	 */
	public void setPageTypeService(PageTypeService pageTypeService) {
		this.pageTypeService = pageTypeService;
	}

	/**
	 * @return the batchClassPluginConfigService
	 */
	public BatchClassPluginConfigService getBatchClassPluginConfigService() {
		return batchClassPluginConfigService;
	}

	/**
	 * @param batchClassPluginConfigService the batchClassPluginConfigService to set
	 */
	public void setBatchClassPluginConfigService(BatchClassPluginConfigService batchClassPluginConfigService) {
		this.batchClassPluginConfigService = batchClassPluginConfigService;
	}

	/**
	 * @return the fieldTypeService
	 */
	public FieldTypeService getFieldTypeService() {
		return fieldTypeService;
	}

	/**
	 * @param the fieldTypeService to set
	 */
	public void setFieldTypeService(FieldTypeService fieldTypeService) {
		this.fieldTypeService = fieldTypeService;
	}

	/**
	 * @return the pluginPropertiesServiceBatchClass
	 */
	public PluginPropertiesService getPluginPropertiesServiceBatchClass() {
		return pluginPropertiesServiceBatchClass;
	}

	/**
	 * @param pluginPropertiesServiceBatchClass the pluginPropertiesServiceBatchClass to set
	 */
	public void setPluginPropertiesServiceBatchClass(PluginPropertiesService pluginPropertiesServiceBatchClass) {
		this.pluginPropertiesServiceBatchClass = pluginPropertiesServiceBatchClass;
	}

	/**
	 * @return the rowID
	 */
	public String getRowID() {
		return rowID;
	}

	/**
	 * @param rowID the rowID to set
	 */
	public void setRowID(String rowID) {
		this.rowID = rowID;
	}

	/**
	 * @return the batchInstanceDao
	 */
	public BatchInstanceDao getBatchInstanceDao() {
		return batchInstanceDao;
	}

	/**
	 * @param batchInstanceDao the batchInstanceDao to set
	 */
	public void setBatchInstanceDao(BatchInstanceDao batchInstanceDao) {
		this.batchInstanceDao = batchInstanceDao;
	}

	/**
	 * This API learns DB for a list of batchclasses 'batchClassIDList' given in fuzzy-db properties file
	 */
	public void learnDataBaseForMultipleBatchClasses() {
		LOGGER.info("Entering method learnDataBaseForMultipleBatchClasses...");
		String batchClassIDList = getBatchClassIDList();
		if (batchClassIDList != null && !batchClassIDList.isEmpty()) {
			LOGGER.info("Start splitting batch class list given in fuzzy DB  properties file ");
			String[] batchClassIDs = batchClassIDList.split(SPLIT_CONSTANT);
			for (String batchClassID : batchClassIDs) {
				if (!batchClassID.isEmpty()) {
					try {
						LOGGER.info("Learning fuzzy DB for batch class::" + batchClassID);
						learnFuzzyDatabase(batchClassID, true);
					} catch (Exception e) {
						LOGGER.error("Uncaught Exception in learnDataBase method for batch class " + batchClassID, e);
					}
				}
			}
		} else {
			LOGGER.info("Batch Class ID is empty or not specified");
		}
		LOGGER.info("Exiting method learnDataBaseForMultipleBatchClasses...");
	}

	/**
	 * This method is used to generate the indexes for the tables mapped for each document type in database. The indexes are stored in
	 * a hierarchical structure: batch class id >> database name >> table name.
	 * 
	 * @param batchClassIdentifier String
	 * @param createIndex boolean
	 */
	public void learnFuzzyDatabase(final String batchClassIdentifier, boolean createIndex) throws DCMAApplicationException {

		Map<String, String> properties = batchClassPluginConfigService.getPluginPropertiesForBatchClass(batchClassIdentifier,
				FUZZYDB_PLUGIN, null);
		if (properties != null && !properties.isEmpty()) {
			LOGGER.info("Fetching properties from DB");
			// String dbName = properties.get(FuzzyDBProperties.FUZZYDB_DB_NAME.getPropertyKey());
			String dbDriver = properties.get(FuzzyDBProperties.FUZZYDB_DB_DRIVER.getPropertyKey());
			String dbUserName = properties.get(FuzzyDBProperties.FUZZYDB_DB_USER_NAME.getPropertyKey());
			String dbPassword = properties.get(FuzzyDBProperties.FUZZYDB_DB_PASSWORD.getPropertyKey());
			String dbConnectionURL = properties.get(FuzzyDBProperties.FUZZYDB_CONNECTION_URL.getPropertyKey());
			String dateFormat = properties.get(FuzzyDBProperties.FUZZYDB_DATE_FORMAT.getPropertyKey());
			String dbName = EMPTY_STRING;
			if (dbConnectionURL != null && dbConnectionURL.length() > 0) {
				dbName = dbConnectionURL.substring(dbConnectionURL.lastIndexOf('/') + 1, dbConnectionURL.length());
			}
			LOGGER.info("Properties fetched successfully from DB");
			String baseFuzzyIndexFolder = batchSchemaService.getFuzzyDBIndexFolder(batchClassIdentifier, false);
			if (dbName.length() <= 0) {
				LOGGER.info("Wrong DB name found");
				return;
			}

			BatchDynamicPluginConfiguration[] pluginPropsDocType = pluginPropertiesServiceBatchClass.getDynamicPluginProperties(
					batchClassIdentifier, FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_DOCUMENT_TYPE);
			if (pluginPropsDocType != null && pluginPropsDocType.length > 0) {
				for (BatchDynamicPluginConfiguration eachConfig : pluginPropsDocType) {
					String tableName = eachConfig.getValue();
					String actualFuzzyIndexFolder = baseFuzzyIndexFolder + File.separator + dbName + File.separator + tableName;
					deleteIndexes(actualFuzzyIndexFolder, batchClassIdentifier);
					List<String> allRows = fetchAllRecordsToBeIndexed(tableName, dbConnectionURL, dbName, dbDriver, dbUserName,
							dbPassword, dateFormat, eachConfig);
					int indexRowID = findIndexOfRowID(eachConfig);
					if (allRows != null && !allRows.isEmpty()) {
						IndexHTML.generateIndex(actualFuzzyIndexFolder, createIndex, allRows, indexRowID);
					} else {
						LOGGER.info("No record found in Database. So cannot learn.");
					}
				}
			} else {
				LOGGER.info("No properties configured for FUZZYDB_DOCUMENT_TYPE. So cannot generate indexes for batch Class : "
						+ batchClassIdentifier);
			}
		} else {
			LOGGER.info("Properties could not be fetched. So cannot learn database for batch class : " + batchClassIdentifier);
		}
	}

	/**
	 * This method deletes the previous indexes before creating new one.
	 * 
	 * @param fuzzyIndexFolder
	 * @param batchClassId
	 */
	public void deleteIndexes(final String fuzzyIndexFolder, final String batchClassId) {
		File oldIndexes = new File(fuzzyIndexFolder);
		if (oldIndexes.exists()) {
			try {
				FileUtils.forceDelete(oldIndexes);
			} catch (IOException e) {
				LOGGER.error("Problem deleting fuzzy DB indexes for batch class : " + batchClassId, e);
			}
		} else {
			LOGGER.info("Fuzzy Db index folder does not exist");
		}
	}

	/**
	 * This method fetches the data for a row returned by lucene search.
	 * 
	 * @param rowId
	 * @param tableName
	 * @param dbConnectionURL
	 * @param dbName
	 * @param dbDriver
	 * @param dbUserName
	 * @param dbPassword
	 * @param eachConfig
	 * @return
	 * @throws DCMAApplicationException
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> fetchDataForRow(int rowId, String tableName, String dbConnectionURL, String dbName, String dbDriver,
			String dbUserName, String dbPassword, BatchDynamicPluginConfiguration eachConfig) throws DCMAApplicationException {
		Set<BatchDynamicPluginConfiguration> allColumnNames = null;
		List<Object[]> dataList = null;
		if (eachConfig != null) {
			allColumnNames = eachConfig.getChildren();
			if (allColumnNames != null && !allColumnNames.isEmpty()) {
				DynamicHibernateDao dynamicHibernateDao = null;
				try {
					dynamicHibernateDao = new DynamicHibernateDao(dbUserName, dbPassword, dbDriver, dbConnectionURL);
					StringBuffer dbQuery = new StringBuffer("select ");
					int count = 0;
					String retrunFieldName = "id";
					if (allColumnNames != null && !allColumnNames.isEmpty()) {
						for (BatchDynamicPluginConfiguration eachColumn : allColumnNames) {
							count++;
							appendColumnNameByDatabase(dbConnectionURL, allColumnNames, dbQuery, count, eachColumn);
							if (eachColumn.getKey().equalsIgnoreCase(rowID)) {
								if (dbConnectionURL.contains(MYSQL_DATABASE)) {
									retrunFieldName = SINGLE_QUOTES + eachColumn.getValue() + SINGLE_QUOTES;
								} else {
									retrunFieldName = DOUBLE_QUOTES + eachColumn.getValue() + DOUBLE_QUOTES;
								}
							}
						}
					} else {
						LOGGER.info(COLUMN_NAME_ERROR_MSG);
						throw new DCMAApplicationException(COLUMN_NAME_ERROR_MSG);
					}
					dbQuery.append(" from ").append(tableName).append(" where ").append(retrunFieldName).append(EQUALS).append(rowId);
					SQLQuery query = dynamicHibernateDao.createQuery(dbQuery.toString());
					dataList = query.list();
				} finally {
					if (dynamicHibernateDao != null) {
						dynamicHibernateDao.closeSession();
					}
				}

			}
		}
		return dataList;
	}

	/**
	 * This method fetches the data for a row returned by lucene search.
	 * 
	 * @param rowId
	 * @param tableName
	 * @param dbConnectionURL
	 * @param dbName
	 * @param dbDriver
	 * @param dbUserName
	 * @param dbPassword
	 * @param eachConfig
	 * @param isHeaderAdded boolean
	 * @return
	 * @throws DCMAApplicationException
	 */
	@SuppressWarnings("unchecked")
	public List<List<String>> fetchFuzzySearchResult(int rowId, String confidenceScore, String tableName, String dbConnectionURL,
			String dbName, String dbDriver, String dbUserName, String dbPassword, BatchDynamicPluginConfiguration eachConfig,
			boolean isHeaderAdded) throws DCMAApplicationException {
		Set<BatchDynamicPluginConfiguration> allColumnNames = null;
		List<List<String>> extractedData = null;
		if (eachConfig != null) {
			extractedData = new ArrayList<List<String>>();
			allColumnNames = eachConfig.getChildren();
			if (allColumnNames != null && !allColumnNames.isEmpty()) {
				DynamicHibernateDao dynamicHibernateDao = null;
				try {
					dynamicHibernateDao = new DynamicHibernateDao(dbUserName, dbPassword, dbDriver, dbConnectionURL);
					StringBuffer dbQuery = new StringBuffer("select ");
					int count = 0;
					String retrunFieldName = "id";

					if (allColumnNames != null && !allColumnNames.isEmpty()) {
						List<String> list = new ArrayList<String>();
						for (BatchDynamicPluginConfiguration eachColumn : allColumnNames) {
							list.add(eachColumn.getDescription());
							count++;
							appendColumnNameByDatabase(dbConnectionURL, allColumnNames, dbQuery, count, eachColumn);
							if (eachColumn.getKey().equalsIgnoreCase(rowID)) {
								if (dbConnectionURL.contains(MYSQL_DATABASE)) {
									retrunFieldName = SINGLE_QUOTES + eachColumn.getValue() + SINGLE_QUOTES;
								} else {
									retrunFieldName = DOUBLE_QUOTES + eachColumn.getValue() + DOUBLE_QUOTES;
								}
							}
						}
						if (isHeaderAdded) {
							list.add("Confidence Score");
							extractedData.add(list);
						}
					} else {
						LOGGER.info(COLUMN_NAME_ERROR_MSG);
						throw new DCMAApplicationException(COLUMN_NAME_ERROR_MSG);
					}
					dbQuery.append(new StringBuffer(" from ")).append(tableName).append(new StringBuffer(" where ")).append(
							retrunFieldName).append(new StringBuffer(EQUALS)).append(rowId);
					SQLQuery query = dynamicHibernateDao.createQuery(dbQuery.toString());
					List<Object[]> dataList = query.list();

					if (null != dataList) {
						for (Object[] obj : dataList) {
							List<String> list = new ArrayList<String>();
							for (Object object : obj) {
								if (object == null) {
									object = new StringBuffer(SPACE_DELIMITER);
								}
								list.add(object.toString());
							}
							list.add(confidenceScore);
							extractedData.add(list);
						}
					}
				} finally {
					if (dynamicHibernateDao != null) {
						dynamicHibernateDao.closeSession();
					}
				}
			}
		}
		return extractedData;
	}

	/**
	 * This method finds the index of row ID to be returned.
	 * 
	 * @param eachConfig
	 * @return
	 */
	private int findIndexOfRowID(BatchDynamicPluginConfiguration eachConfig) {
		int returnValue = 0;
		Set<BatchDynamicPluginConfiguration> allColumnNames = null;
		if (eachConfig != null) {
			allColumnNames = eachConfig.getChildren();
			if (allColumnNames != null && !allColumnNames.isEmpty()) {
				int count = 0;
				for (BatchDynamicPluginConfiguration eachColumn : allColumnNames) {
					if (eachColumn.getKey().equalsIgnoreCase(rowID)) {
						returnValue = count;
					}
					if (eachColumn.getValue() != null) {
						count++;
					}
				}
			}
		}
		return returnValue;
	}

	/**
	 * This method finds all the records that needs to be indexed for all the tables configured for document types.
	 * 
	 * @param tableName
	 * @param dbConnectionURL
	 * @param dbName
	 * @param dbDriver
	 * @param dbUserName
	 * @param dbPassword
	 * @param dateFormat
	 * @param eachConfig
	 * @return List<String>
	 * @throws DCMAApplicationException
	 */
	@SuppressWarnings("unchecked")
	public List<String> fetchAllRecordsToBeIndexed(String tableName, String dbConnectionURL, String dbName, String dbDriver,
			String dbUserName, String dbPassword, String dateFormat, BatchDynamicPluginConfiguration eachConfig)
			throws DCMAApplicationException {
		List<String> returnList = null;
		Set<BatchDynamicPluginConfiguration> allColumnNames = null;
		if (eachConfig != null) {
			allColumnNames = eachConfig.getChildren();
			if (allColumnNames != null && !allColumnNames.isEmpty()) {
				DynamicHibernateDao dynamicHibernateDao = null;
				try {
					dynamicHibernateDao = new DynamicHibernateDao(dbUserName, dbPassword, dbDriver, dbConnectionURL);
					List<ColumnDefinition> colNames = null;
					try {
						colNames = dynamicHibernateDao.getAllColumnsForTable(tableName);
					} catch (SQLException e) {
						LOGGER.info("Could not find Column names from table : " + tableName);
					}
					StringBuffer dbQuery = new StringBuffer("select ");
					int count = 0;
					List<Integer> dateColIndex = new ArrayList<Integer>();
					for (BatchDynamicPluginConfiguration eachColumn : allColumnNames) {
						count++;
						appendColumnNameByDatabase(dbConnectionURL, allColumnNames, dbQuery, count, eachColumn);
						String colDataType = getColumnDataType(eachColumn.getValue(), colNames);
						if (colDataType != null
								&& (colDataType.equalsIgnoreCase(TIMESTAMP_NAME) || colDataType.equalsIgnoreCase(DATE_NAME))) {
							dateColIndex.add(count);
						}
					}
					dbQuery.append(new StringBuffer(" from ")).append(tableName);
					SQLQuery query = dynamicHibernateDao.createQuery(dbQuery.toString());
					List<Object[]> dataList = query.list();

					StringBuffer dbRow = new StringBuffer(EMPTY_STRING);
					int indexCount = 0;
					if (dataList != null && !dataList.isEmpty()) {
						returnList = new ArrayList<String>();
						if (dataList.size() > 1) {
							LOGGER.info("More than One records found. So picking first record.");
						}
						for (Object[] eachRecord : dataList) {
							indexCount = 0;
							dbRow = new StringBuffer(EMPTY_STRING);
							for (Object eachElement : eachRecord) {
								indexCount++;
								if (eachElement != null) {
									if (dateColIndex.contains(indexCount)) {
										SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
										eachElement = simpleDateFormat.format(eachElement);
									}
									dbRow.append(eachElement).append(";;;");
								} else {
									dbRow.append(" ;;;");
								}
							}
							returnList.add(dbRow.toString());
						}
					} else {
						LOGGER.info("Unable to fetch data for query : " + dbQuery);
					}
				} finally {
					if (dynamicHibernateDao != null) {
						dynamicHibernateDao.closeSession();
					}
				}
			} else {
				LOGGER.info("No column names configured for table name : " + eachConfig.getValue());
			}
		}
		return returnList;
	}

	private void appendColumnNameByDatabase(String dbConnectionURL, Set<BatchDynamicPluginConfiguration> allColumnNames,
			StringBuffer dbQuery, int count, BatchDynamicPluginConfiguration eachColumn) {
		if (dbConnectionURL.contains(MYSQL_DATABASE)) {
			if (count < allColumnNames.size()) {
				dbQuery.append(SINGLE_QUOTES).append(eachColumn.getValue()).append(SINGLE_QUOTES).append(COMMA);
			} else {
				dbQuery.append(SINGLE_QUOTES).append(eachColumn.getValue()).append(SINGLE_QUOTES);
			}
		} else {
			if (count < allColumnNames.size()) {
				dbQuery.append(DOUBLE_QUOTES).append(eachColumn.getValue()).append(DOUBLE_QUOTES).append(COMMA);
			} else {
				dbQuery.append(DOUBLE_QUOTES).append(eachColumn.getValue()).append(DOUBLE_QUOTES);
			}
		}
	}

	/**
	 * This method fetches the data type of a column supplied.
	 * 
	 * @param eachColumn
	 * @param colNames
	 * @return
	 */
	public String getColumnDataType(String eachColumn, List<ColumnDefinition> colNames) {
		String columnDataType = null;
		if (colNames != null && !colNames.isEmpty()) {
			for (ColumnDefinition eachColName : colNames) {
				if (eachColumn.equalsIgnoreCase(eachColName.getColumnName())) {
					columnDataType = eachColName.getType().getSimpleName();
					break;
				}
			}
		}
		return columnDataType;
	}

	/**
	 * This method finds the index of a column from the all column list.
	 * 
	 * @param allColumnNames
	 * @param colName
	 * @return
	 */
	public int findIndexOf(Set<BatchPluginConfiguration> allColumnNames, String colName) {
		int returnIndex = -1;
		if (allColumnNames != null && !allColumnNames.isEmpty()) {
			List<BatchPluginConfiguration> tempList = new ArrayList<BatchPluginConfiguration>(allColumnNames);
			returnIndex = tempList.indexOf(colName);
		}
		return returnIndex;
	}

	/**
	 * This method creates/updates the value of document level fields for each document by searching for similarities in HOCR content
	 * with the data in database tables mapped for each document type.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param createIndex boolean
	 */
	public boolean extractDataBaseFields(final String batchInstanceIdentifier) throws DCMAApplicationException {
		LOGGER.info("Initializing properties...");
		String switchValue = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_SWITCH);
		if (("ON".equalsIgnoreCase(switchValue))) {
			String indexFields = INDEX_FIELD;
			String stopWords = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FUZZYDB_PLUGIN,
					FuzzyDBProperties.FUZZYDB_STOP_WORDS);
			String minTermFreq = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FUZZYDB_PLUGIN,
					FuzzyDBProperties.FUZZYDB_MIN_TERM_FREQ);
			String minDocFreq = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FUZZYDB_PLUGIN,
					FuzzyDBProperties.FUZZYDB_MIN_DOC_FREQ);
			String minWordLength = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FUZZYDB_PLUGIN,
					FuzzyDBProperties.FUZZYDB_MIN_WORD_LENGTH);
			String maxQueryTerms = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FUZZYDB_PLUGIN,
					FuzzyDBProperties.FUZZYDB_MAX_QUERY_TERMS);

			String numOfPages = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FUZZYDB_PLUGIN,
					FuzzyDBProperties.FUZZYDB_NO_OF_PAGES);

			String dbDriver = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FUZZYDB_PLUGIN,
					FuzzyDBProperties.FUZZYDB_DB_DRIVER);
			String dbConnectionURL = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FUZZYDB_PLUGIN,
					FuzzyDBProperties.FUZZYDB_CONNECTION_URL);
			String dbUserName = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FUZZYDB_PLUGIN,
					FuzzyDBProperties.FUZZYDB_DB_USER_NAME);
			String dbPassword = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FUZZYDB_PLUGIN,
					FuzzyDBProperties.FUZZYDB_DB_PASSWORD);
			String thresholdValue = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FUZZYDB_PLUGIN,
					FuzzyDBProperties.FUZZYDB_THRESHOLD_VALUE);
			String includePages = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FUZZYDB_PLUGIN,
					FuzzyDBProperties.FUZZYDB_INCLUDE_PAGES);
			String dbName = EMPTY_STRING;
			if (dbConnectionURL != null && dbConnectionURL.length() > 0) {
				dbName = dbConnectionURL.substring(dbConnectionURL.lastIndexOf('/') + 1, dbConnectionURL.length());
			}
			LOGGER.info("Properties Initialized Successfully");

			Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);
			String batchClassIdentifier = batch.getBatchClassIdentifier();
			String indexFolder = batchSchemaService.getFuzzyDBIndexFolder(batchClassIdentifier, false);

			if (dbName.length() <= 0) {
				LOGGER.info("Wrong DB name found");
				return false;
			}

			File baseFuzzyDbIndexFolder = new File(indexFolder);
			if (baseFuzzyDbIndexFolder != null && !baseFuzzyDbIndexFolder.exists()) {
				LOGGER.info("The base fuzzy db index folder does not exist. So cannot extract database fields.");
				return false;
			}

			String[] allIndexFields = indexFields.split(SPLIT_CONSTANT);
			String[] allStopWords = stopWords.split(SPLIT_CONSTANT);
			IndexReader reader = null;
			Query query = null;
			Map<String, Float> returnMap = new HashMap<String, Float>();

			// List<com.ephesoft.dcma.da.domain.PageType> allPageTypes =
			// pageTypeService.getPageTypesByBatchInstanceID(batchInstanceID);
			try {
				List<com.ephesoft.dcma.da.domain.PageType> allPageTypes = pluginPropertiesService
						.getPageTypes(batchInstanceIdentifier);
				if (!(allPageTypes != null && !allPageTypes.isEmpty())) {
					LOGGER.info("Page Types not configured in Database");
					return false;
				}

				BatchDynamicPluginConfiguration[] pluginPropsDocType = pluginPropertiesService.getDynamicPluginProperties(
						batchInstanceIdentifier, FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_DOCUMENT_TYPE);
				if (pluginPropsDocType != null && pluginPropsDocType.length > 0) {
					for (BatchDynamicPluginConfiguration eachConfig : pluginPropsDocType) {
						String tableName = eachConfig.getValue();
						String fuzzyIndexFolder = indexFolder + File.separator + dbName + File.separator + tableName;
						File indexFolderDirectory = new File(fuzzyIndexFolder);
						if (indexFolderDirectory != null && indexFolderDirectory.exists()) {
							String[] indexFiles = indexFolderDirectory.list();
							if (indexFiles == null || indexFiles.length <= 0) {
								LOGGER.info("No index files exist inside folder : " + indexFolderDirectory);
								continue;
							}
							try {
								reader = IndexReader.open(FSDirectory.open(new File(fuzzyIndexFolder)), true);
							} catch (CorruptIndexException e) {
								LOGGER.error("CorruptIndexException while reading Index" + e.getMessage(), e);
								cleanUpResource(reader);
								return false;
							} catch (IOException e) {
								LOGGER.error("IOException while reading Index" + e.getMessage(), e);
								cleanUpResource(reader);
								return false;
							}
						} else {
							LOGGER.info("No index created for : " + eachConfig.getKey());
							continue;
						}
						MoreLikeThis moreLikeThis = new MoreLikeThis(reader);
						moreLikeThis.setFieldNames(allIndexFields);
						moreLikeThis.setMinTermFreq(Integer.parseInt(minTermFreq));
						moreLikeThis.setMinDocFreq(Integer.parseInt(minDocFreq));
						moreLikeThis.setMinWordLen(Integer.parseInt(minWordLength));
						moreLikeThis.setMaxQueryTerms(Integer.parseInt(maxQueryTerms));
						if (allStopWords != null && allStopWords.length > 0) {
							Set<String> stopWordsTemp = new HashSet<String>();
							for (int i = 0; i < allStopWords.length; i++) {
								stopWordsTemp.add(allStopWords[i]);
							}
							moreLikeThis.setStopWords(stopWordsTemp);
						}

						Map<String, String> docHocrContent = new HashMap<String, String>();

						if (includePages != null && includePages.equalsIgnoreCase(ALLPAGES)) {
							List<Document> xmlDocuments = batch.getDocuments().getDocument();
							for (Document eachDocType : xmlDocuments) {
								StringBuilder hocrContent = new StringBuilder();
								List<Page> pages = eachDocType.getPages().getPage();
								for (Page eachPage : pages) {
									String pageId = eachPage.getIdentifier();
									HocrPages hocrPages = batchSchemaService.getHocrPages(batchInstanceIdentifier, pageId);
									List<HocrPage> hocrPageList = hocrPages.getHocrPage();
									HocrPage hocrPage = hocrPageList.get(0);
									if (hocrPage.getPageID().equals(eachPage.getIdentifier())) {
										hocrContent.append(hocrPage.getHocrContent());
									}
									hocrContent.append(SPACE_DELIMITER);
								}
								docHocrContent.put(eachDocType.getIdentifier(), hocrContent.toString());
							}
						} else if (includePages != null && includePages.equalsIgnoreCase(FIRSTPAGE)) {
							List<Document> xmlDocuments = batch.getDocuments().getDocument();
							for (Document eachDocType : xmlDocuments) {
								String hocrContent = EMPTY_STRING;
								List<Page> pages = eachDocType.getPages().getPage();
								String pageId = pages.get(0).getIdentifier();
								HocrPages hocrPages = batchSchemaService.getHocrPages(batchInstanceIdentifier, pageId);
								List<HocrPage> hocrPageList = hocrPages.getHocrPage();
								HocrPage hocrPage = hocrPageList.get(0);
								hocrContent = hocrPage.getHocrContent();
								docHocrContent.put(eachDocType.getIdentifier(), hocrContent);
							}
						}

						if (docHocrContent.size() > 0) {
							Set<String> docIds = docHocrContent.keySet();
							for (String eachDoc : docIds) {
								String docTypeForID = getDocTypeByID(eachDoc, batch);
								if (eachConfig.getDescription().equalsIgnoreCase(docTypeForID)) {
									LOGGER.info("Generating query for Document: " + eachDoc);
									String docHocr = docHocrContent.get(eachDoc);
									if (null != docHocr) {
										InputStream inputStream = null;
										if (null != ignoreWordList && ignoreWordList.length > 0) {
											docHocr = removeIgnoreWordsFromHOCR(docHocr, ignoreWordList);
										}
										try {
											inputStream = new ByteArrayInputStream(docHocr.getBytes("UTF-8"));
											query = moreLikeThis.like(inputStream);
										} catch (UnsupportedEncodingException e) {
											LOGGER.error("Problem generating query for Document :  " + eachDoc, e);
											continue;
										} catch (IOException e) {
											LOGGER.error("Problem generating query for Document :  " + eachDoc, e);
											continue;
										} finally {
											if (inputStream != null) {
												try {
													inputStream.close();
												} catch (IOException e) {
													LOGGER.error("Problem in closing input stream. " + e.getMessage(), e);
												}
											}
										}
									} else {
										LOGGER.info("Empty HOCR content found for Document :  " + eachDoc);
										continue;
									}
									if (query != null && query.toString() != null && query.toString().length() > 0) {
										LOGGER.info("Generating confidence score for Document: " + eachDoc);
										try {
											returnMap = SearchFiles.generateConfidence(fuzzyIndexFolder, query.toString(),
													INDEX_FIELD, Integer.valueOf(numOfPages), ignoreWordList);
										} catch (NumberFormatException e) {
											LOGGER.error(CONFIDENCE_ERROR_MSG + eachDoc, e);
											continue;
										} catch (Exception e) {
											LOGGER.error(CONFIDENCE_ERROR_MSG + eachDoc, e);
											continue;
										}
										LOGGER.info("Return Map is : " + returnMap);
										int highestScoreDoc = fetchDocumentWithHighestScoreValue(returnMap, thresholdValue);
										if (highestScoreDoc != 0) {
											List<Object[]> extractedData = fetchDataForRow(highestScoreDoc, tableName,
													dbConnectionURL, dbName, dbDriver, dbUserName, dbPassword, eachConfig);
											LOGGER.info("Extracted data is : " + extractedData);
											LOGGER.info("Updating XML....");
											updateBatchXML(batch, extractedData, eachDoc, batchInstanceIdentifier, eachConfig);
										} else {
											LOGGER.info("No document found with confidence score greater than threshold value : "
													+ thresholdValue);
										}
									} else {
										LOGGER.info("Empty query generated for Document : " + eachDoc);
									}
								}
							}
						}

					}
				} else {
					LOGGER.info("No properties configured for FUZZYDB_DOCUMENT_TYPE");
				}
			} finally {
				LOGGER.info("Closing input stream for index.");
				cleanUpResource(reader);
			}
			batchSchemaService.updateBatch(batch);
			LOGGER.info("updateBatchXML done.");
			return false;
		} else {
			LOGGER.info("Skipping FuzzyDB extraction. Switch set as off.");
			return false;
		}
	}

	/**
	 * This method removes the words specified in ignore list from input string.
	 * 
	 * @param docHocr
	 * @param ignoreWordList
	 * @return
	 */
	private String removeIgnoreWordsFromHOCR(final String docHocr, final String[] ignoreWordList) {
		String finalDocHocr = docHocr;
		if (null != finalDocHocr && null != ignoreWordList && ignoreWordList.length > 0) {
			for (String ignoreWord : ignoreWordList) {
				if (ignoreWord != null && !ignoreWord.isEmpty()) {
					try {
						finalDocHocr = finalDocHocr.replaceAll(WORD_BOUNDRY_REGEX + ignoreWord + WORD_BOUNDRY_REGEX, EMPTY_STRING);
						LOGGER.info("Word ignored : " + ignoreWord);
					} catch (PatternSyntaxException pattexception) {
						LOGGER.info("Incorrect ignoreWord specifeid in properties file :: " + ignoreWord, pattexception);
					}
				}
			}
		}
		return finalDocHocr;
	}

	public void cleanUpResource(IndexReader reader) {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				LOGGER.info("There was a problem in closing the Index reader.");
			}
		}
	}

	/**
	 * This method updates the value of document level fields for each document by searching for similarities in input search text with
	 * the data in database tables mapped for each document type.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param documentType String
	 * @param searchText String
	 * @throws DCMAApplicationException
	 */
	public List<List<String>> fuzzyTextSearch(final String batchInstanceIdentifier, final String documentType, final String searchText)
			throws DCMAApplicationException {
		LOGGER.info("Initializing properties...");

		String searchTextInLowerCase = searchText.toLowerCase();
		List<List<String>> extractedData = null;
		String numOfPages = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_NO_OF_PAGES);
		String dbDriver = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_DB_DRIVER);
		String dbConnectionURL = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_CONNECTION_URL);
		String dbUserName = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_DB_USER_NAME);
		String thresholdValue = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_THRESHOLD_VALUE);
		String dbPassword = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_DB_PASSWORD);
		String dbName = EMPTY_STRING;
		if (dbConnectionURL != null && dbConnectionURL.length() > 0) {
			dbName = dbConnectionURL.substring(dbConnectionURL.lastIndexOf('/') + 1, dbConnectionURL.length());
		}

		String queryDelimiters = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_QUERY_DELIMITERS);
		LOGGER.info("Properties Initialized Successfully");

		Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);
		String batchClassIdentifier = batch.getBatchClassIdentifier();
		String indexFolder = batchSchemaService.getFuzzyDBIndexFolder(batchClassIdentifier, false);

		if (dbName.length() <= 0) {
			LOGGER.info("Wrong DB name found");
			return extractedData;
		}

		File baseFuzzyDbIndexFolder = new File(indexFolder);
		if (baseFuzzyDbIndexFolder != null && !baseFuzzyDbIndexFolder.exists()) {
			LOGGER.info("The base fuzzy db index folder does not exist. So cannot extract database fields.");
			return extractedData;
		}

		Map<String, Float> returnMap = new HashMap<String, Float>();

		List<com.ephesoft.dcma.da.domain.PageType> allPageTypes = pluginPropertiesService.getPageTypes(batchInstanceIdentifier);
		if (!(allPageTypes != null && !allPageTypes.isEmpty())) {
			LOGGER.info("Page Types not configured in Database");
			return extractedData;
		}

		BatchDynamicPluginConfiguration[] pluginPropsDocType = pluginPropertiesService.getDynamicPluginProperties(
				batchInstanceIdentifier, FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_DOCUMENT_TYPE);
		if (pluginPropsDocType != null && pluginPropsDocType.length > 0) {
			for (BatchDynamicPluginConfiguration eachConfig : pluginPropsDocType) {
				if (eachConfig.getDescription().equals(documentType)) {
					String tableName = eachConfig.getValue();
					String fuzzyIndexFolder = indexFolder + File.separator + dbName + File.separator + tableName;
					File indexFolderDirectory = new File(fuzzyIndexFolder);
					if (indexFolderDirectory != null && indexFolderDirectory.exists()) {
						String[] indexFiles = indexFolderDirectory.list();
						if (indexFiles == null || indexFiles.length <= 0) {
							LOGGER.info("No index files exist inside folder : " + indexFolderDirectory);
							continue;
						}
					} else {
						LOGGER.info("No index created for : " + eachConfig.getDescription());
						continue;
					}
					StringBuffer query = new StringBuffer();

					StringTokenizer tokens = new StringTokenizer(searchTextInLowerCase, queryDelimiters);
					while (tokens.hasMoreTokens()) {
						query.append(INDEX_FIELD + QUERY_STRING_DELIMITER + tokens.nextToken());
						query.append(SPACE_DELIMITER);
					}
					if (query != null && query.toString() != null && query.toString().length() > 0) {
						LOGGER.info("Generating confidence score for Document: " + searchTextInLowerCase);
						try {
							returnMap = SearchFiles.generateConfidence(fuzzyIndexFolder, query.toString(), INDEX_FIELD, Integer
									.valueOf(numOfPages), this.ignoreWordList);
						} catch (NumberFormatException e) {
							LOGGER.error(CONFIDENCE_ERROR_MSG + searchTextInLowerCase, e);
							continue;
						} catch (Exception e) {
							LOGGER.error(CONFIDENCE_ERROR_MSG + searchTextInLowerCase, e);
							continue;
						}
						LOGGER.info("Return Map is : " + returnMap);
						if (returnMap != null && returnMap.size() > 0) {
							if (extractedData == null) {
								extractedData = new ArrayList<List<String>>();
							}
							boolean isHeaderAdded = true;
							Set<String> set = fetchDocumentHavingThresholdValue(returnMap, thresholdValue);
							Iterator<String> iterator = set.iterator();
							while (iterator.hasNext()) {
								String key = iterator.next();
								Float confidenceScore = returnMap.get(key);
								int keyInt = 0;
								try {
									keyInt = Integer.parseInt(key);
								} catch (NumberFormatException nfe) {
									LOGGER.error(nfe.getMessage(), nfe);
									return null;
								}
								extractedData.addAll(fetchFuzzySearchResult(keyInt, confidenceScore.toString(), tableName,
										dbConnectionURL, dbName, dbDriver, dbUserName, dbPassword, eachConfig, isHeaderAdded));
								isHeaderAdded = false;
								LOGGER.info("Extracted data is : " + extractedData);
							}
						} else {
							LOGGER.info("No record found ");
						}
					} else {
						LOGGER.info("Empty query generated for Document : " + searchTextInLowerCase);
					}
				}
			}
		} else {
			LOGGER.info("No properties configured for FUZZYDB_DOCUMENT_TYPE");
		}

		if (extractedData != null && extractedData.isEmpty()) {
			return null;
		}

		return extractedData;

	}

	/**
	 * This method fetches the document type from document ID.
	 * 
	 * @param docID
	 * @param batch
	 * @return
	 */
	private String getDocTypeByID(String docID, Batch batch) {
		String returnValue = EMPTY_STRING;
		List<Document> xmlDocuments = batch.getDocuments().getDocument();
		for (Document eachDocType : xmlDocuments) {
			if (eachDocType.getIdentifier().equals(docID)) {
				returnValue = eachDocType.getType();
			}
		}
		return returnValue;
	}

	/**
	 * This method updated the batch xml for all the document level fields for which values are found in database.
	 * 
	 * @param batch
	 * @param extractedData
	 * @param documentId
	 * @param batchInstanceId
	 * @param eachConfig
	 */
	public void updateBatchXML(final Batch batch, final List<Object[]> extractedData, final String documentId, String batchInstanceId,
			final BatchDynamicPluginConfiguration eachConfig) {

		BatchDynamicPluginConfiguration[] pluginPropsDocType = pluginPropertiesService.getDynamicPluginProperties(batchInstanceId,
				FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_DOCUMENT_TYPE);
		if (pluginPropsDocType != null && pluginPropsDocType.length > 0) {
			for (BatchDynamicPluginConfiguration tempConfig : pluginPropsDocType) {
				if (tempConfig.getId().equals(eachConfig.getId())) {
					Set<BatchDynamicPluginConfiguration> allColumnNames = eachConfig.getChildren();
					List<Document> xmlDocuments = batch.getDocuments().getDocument();
					for (Document eachDocType : xmlDocuments) {
						if (eachDocType.getIdentifier().equals(documentId)) {
							List<DocField> docLevelFields = eachDocType.getDocumentLevelFields().getDocumentLevelField();
							if (docLevelFields != null && !docLevelFields.isEmpty()) {
								for (DocField eachDocLevelField : docLevelFields) {
									Object newValue = getValueForDocField(eachDocLevelField.getName(), allColumnNames, extractedData);
									if (newValue != null) {
										int index = findIndexOfDocLevelFieldInList(docLevelFields, eachDocLevelField);
										if (index >= 0) {
											DocField newDocLevelField = new DocField();
											newDocLevelField.setName(eachDocLevelField.getName());
											newDocLevelField.setFieldOrderNumber(eachDocLevelField.getFieldOrderNumber());
											newDocLevelField.setValue(newValue.toString());
											newDocLevelField.setType(eachDocLevelField.getType());
											docLevelFields.set(index, newDocLevelField);
										}
									}
								}
							} else {
								/*
								 * List<com.ephesoft.dcma.da.domain.FieldType> allFdTypes = fieldTypeService.getFdTypeByDocTypeName(
								 * eachDocType.getType(), batchInstanceId);
								 */
								List<com.ephesoft.dcma.da.domain.FieldType> allFdTypes = pluginPropertiesService.getFieldTypes(
										batchInstanceId, eachDocType.getType());
								if (allFdTypes != null) {
									for (com.ephesoft.dcma.da.domain.FieldType fdType : allFdTypes) {
										DocField docLevelField = new DocField();
										docLevelField.setName(fdType.getName());
										docLevelField.setFieldOrderNumber(fdType.getFieldOrderNumber());
										docLevelField.setType(fdType.getDataType().name());
										Object newValue = getValueForDocField(fdType.getName(), allColumnNames, extractedData);
										docLevelField.setValue(newValue.toString());
										docLevelFields.add(docLevelField);
									}
								} else {
									LOGGER.info("No field types could be found for document type :" + eachDocType.getType());
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * This method updated the batch xml for all the document level fields for which values are found in database.
	 * 
	 * @param batch
	 * @param extractedData
	 * @param documentId
	 * @param batchInstanceId
	 * @param eachConfig
	 */
	public void updateDocument(Documents documents, final List<Object[]> extractedData,
			final BatchDynamicPluginConfiguration eachConfig, String batchClassIdentifier, String documentType) {

		Set<BatchDynamicPluginConfiguration> allColumnNames = eachConfig.getChildren();
		List<Document> docList = documents.getDocument();
		if (docList.isEmpty()) {
			Document document = new Document();
			document.setType(documentType);
			document.setDocumentLevelFields(new DocumentLevelFields());
			documents.getDocument().add(document);
		}
		List<DocField> docLevelFields = documents.getDocument().get(0).getDocumentLevelFields().getDocumentLevelField();
		if (docLevelFields != null && !docLevelFields.isEmpty()) {
			for (DocField eachDocLevelField : docLevelFields) {
				Object newValue = getValueForDocField(eachDocLevelField.getName(), allColumnNames, extractedData);
				if (newValue != null) {
					int index = findIndexOfDocLevelFieldInList(docLevelFields, eachDocLevelField);
					if (index >= 0) {
						DocField newDocLevelField = new DocField();
						newDocLevelField.setName(eachDocLevelField.getName());
						newDocLevelField.setFieldOrderNumber(eachDocLevelField.getFieldOrderNumber());
						newDocLevelField.setValue(newValue.toString());
						newDocLevelField.setType(eachDocLevelField.getType());
						docLevelFields.set(index, newDocLevelField);
					}
				}
			}
		} else {

			List<com.ephesoft.dcma.da.domain.FieldType> allFdTypes = fieldTypeService.getFdTypeByDocTypeNameForBatchClass(documentType,
					batchClassIdentifier);

			if (allFdTypes != null) {
				for (com.ephesoft.dcma.da.domain.FieldType fdType : allFdTypes) {
					DocField docLevelField = new DocField();
					docLevelField.setName(fdType.getName());
					docLevelField.setFieldOrderNumber(fdType.getFieldOrderNumber());
					docLevelField.setType(fdType.getDataType().name());
					Object newValue = getValueForDocField(fdType.getName(), allColumnNames, extractedData);
					docLevelField.setValue(newValue.toString());
					docLevelFields.add(docLevelField);
				}
			} else {
				LOGGER.info("No field types could be found for document type :" + documentType);
			}
		}
	}

	/**
	 * This method finds the index of specified document Level Field in the specified List of Document Level Fields.
	 * 
	 * @param docLevelFields
	 * @param eachDocLevelField
	 * @return
	 */
	private int findIndexOfDocLevelFieldInList(List<DocField> docLevelFields, DocField eachDocLevelField) {
		int returnValue = -1;
		if (docLevelFields != null && !docLevelFields.isEmpty()) {
			int count = 0;
			for (DocField docFieldType : docLevelFields) {
				if (docFieldType.getName() != null && eachDocLevelField.getName() != null
						&& docFieldType.getName().equalsIgnoreCase(eachDocLevelField.getName())) {
					returnValue = count;
					break;
				}
				count++;
			}
			if (count >= docLevelFields.size()) {
				returnValue = -1;
			}
		}
		return returnValue;
	}

	/**
	 * This method fetches the value for each document level field.
	 * 
	 * @param docField
	 * @param allColumnNames
	 * @param extractedData
	 * @return
	 */
	public Object getValueForDocField(String docField, Set<BatchDynamicPluginConfiguration> allColumnNames,
			List<Object[]> extractedData) {
		if (docField == null || docField.length() <= 0) {
			return null;
		}
		if (allColumnNames != null && !allColumnNames.isEmpty()) {
			int count = 0;
			for (BatchDynamicPluginConfiguration eachColumn : allColumnNames) {
				if (docField.equalsIgnoreCase(eachColumn.getDescription())) {
					break;
				}
				count++;
			}
			if (count >= allColumnNames.size()) {
				return null;
			}
			if (extractedData != null && !extractedData.isEmpty()) {
				if (extractedData.size() > 1) {
					LOGGER.info("More than one records found. So picking first record.");
				}
				Object[] data = extractedData.get(0);
				return data[count];
			}
		}
		return null;
	}

	/**
	 * This method finds the highest score among all the scores inside batchDocNameScore.
	 * 
	 * @param batchDocNameScore Map<String, Float>
	 * @return float
	 */
	public int fetchDocumentWithHighestScoreValue(Map<String, Float> batchDocNameScore, String thresholdValue) {
		float highestValue = 0f;
		int highestScoreDoc = 0;
		Set<String> set = batchDocNameScore.keySet();
		Iterator<String> iterator = set.iterator();
		while (iterator.hasNext()) {
			String learnName = iterator.next();
			float eachValue = batchDocNameScore.get(learnName);
			if (eachValue > highestValue) {
				highestValue = eachValue;
				highestScoreDoc = Integer.parseInt(learnName);
			}
		}
		if (highestValue > Float.valueOf(thresholdValue)) {
			return highestScoreDoc;
		} else {
			return 0;
		}
	}

	/**
	 * @param batchDocNameScore
	 * @param thresholdValue
	 * @return
	 */
	public Set<String> fetchDocumentHavingThresholdValue(Map<String, Float> batchDocNameScore, String thresholdValue) {
		Set<String> resultSet = new HashSet<String>();
		Set<String> set = batchDocNameScore.keySet();
		Iterator<String> iterator = set.iterator();
		while (iterator.hasNext()) {
			String learnName = iterator.next();
			float eachValue = batchDocNameScore.get(learnName);
			if (eachValue > Float.valueOf(thresholdValue)) {
				resultSet.add(learnName);
			}
		}
		return resultSet;
	}

	/**
	 * @param batchClassIDList
	 */
	public void setBatchClassIDList(String batchClassIDList) {
		this.batchClassIDList = batchClassIDList;
	}

	/**
	 * @return
	 */
	public String getBatchClassIDList() {
		return batchClassIDList;
	}

	/**
	 * This method creates/updates the value of document level fields for each document by searching for similarities in HOCR content
	 * with the data in database tables mapped for each document type.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param createIndex boolean
	 */
	public Documents extractDataBaseFields(final String batchClassIdentifier, String documentType, HocrPages hocrPage)
			throws DCMAApplicationException {
		LOGGER.info("Initializing properties...");
		Documents documents = null;

		String indexFields = INDEX_FIELD;
		String stopWords = pluginPropertiesServiceBatchClass.getPropertyValue(batchClassIdentifier, FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_STOP_WORDS);
		String minTermFreq = pluginPropertiesServiceBatchClass.getPropertyValue(batchClassIdentifier, FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_MIN_TERM_FREQ);
		String minDocFreq = pluginPropertiesServiceBatchClass.getPropertyValue(batchClassIdentifier, FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_MIN_DOC_FREQ);
		String minWordLength = pluginPropertiesServiceBatchClass.getPropertyValue(batchClassIdentifier, FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_MIN_WORD_LENGTH);
		String maxQueryTerms = pluginPropertiesServiceBatchClass.getPropertyValue(batchClassIdentifier, FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_MAX_QUERY_TERMS);

		String numOfPages = pluginPropertiesServiceBatchClass.getPropertyValue(batchClassIdentifier, FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_NO_OF_PAGES);

		String dbDriver = pluginPropertiesServiceBatchClass.getPropertyValue(batchClassIdentifier, FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_DB_DRIVER);
		String dbConnectionURL = pluginPropertiesServiceBatchClass.getPropertyValue(batchClassIdentifier, FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_CONNECTION_URL);
		String dbUserName = pluginPropertiesServiceBatchClass.getPropertyValue(batchClassIdentifier, FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_DB_USER_NAME);
		String dbPassword = pluginPropertiesServiceBatchClass.getPropertyValue(batchClassIdentifier, FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_DB_PASSWORD);
		String thresholdValue = pluginPropertiesServiceBatchClass.getPropertyValue(batchClassIdentifier, FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_THRESHOLD_VALUE);
		String dbName = EMPTY_STRING;

		if (dbConnectionURL != null && dbConnectionURL.length() > 0) {
			dbName = dbConnectionURL.substring(dbConnectionURL.lastIndexOf('/') + 1, dbConnectionURL.length());
		}
		LOGGER.info("Properties Initialized Successfully");

		String indexFolder = batchSchemaService.getFuzzyDBIndexFolder(batchClassIdentifier, false);

		if (dbName.length() <= 0) {
			LOGGER.error("Wrong DB name found");
			throw new DCMAApplicationException("Wrong DB name found");
		}

		File baseFuzzyDbIndexFolder = new File(indexFolder);
		if (baseFuzzyDbIndexFolder != null && !baseFuzzyDbIndexFolder.exists()) {
			LOGGER.info("The base fuzzy db index folder does not exist. So cannot extract database fields.");
			throw new DCMAApplicationException("The base fuzzy db index folder does not exist. So cannot extract database fields.");
		}

		String[] allIndexFields = indexFields.split(SPLIT_CONSTANT);
		String[] allStopWords = stopWords.split(SPLIT_CONSTANT);
		IndexReader reader = null;
		Query query = null;
		Map<String, Float> returnMap = new HashMap<String, Float>();

		// List<com.ephesoft.dcma.da.domain.PageType> allPageTypes =
		// pageTypeService.getPageTypesByBatchInstanceID(batchInstanceID);
		try {
			// List<com.ephesoft.dcma.da.domain.PageType> allPageTypes =
			// pluginPropertiesServiceBatchClass.getPageTypes(batchInstanceIdentifier);
			// if (!(allPageTypes != null && !allPageTypes.isEmpty())) {
			// LOGGER.info("Page Types not configured in Database");
			// return false;
			// }

			BatchDynamicPluginConfiguration[] pluginPropsDocType = pluginPropertiesServiceBatchClass.getDynamicPluginProperties(
					batchClassIdentifier, FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_DOCUMENT_TYPE);
			if (pluginPropsDocType != null && pluginPropsDocType.length > 0) {
				for (BatchDynamicPluginConfiguration eachConfig : pluginPropsDocType) {
					String tableName = eachConfig.getValue();
					String fuzzyIndexFolder = indexFolder + File.separator + dbName + File.separator + tableName;
					File indexFolderDirectory = new File(fuzzyIndexFolder);
					if (indexFolderDirectory != null && indexFolderDirectory.exists()) {
						String[] indexFiles = indexFolderDirectory.list();
						if (indexFiles == null || indexFiles.length <= 0) {
							LOGGER.info("No index files exist inside folder : " + indexFolderDirectory);
							continue;
						}
						try {
							reader = IndexReader.open(FSDirectory.open(new File(fuzzyIndexFolder)), true);
						} catch (CorruptIndexException e) {
							LOGGER.error("CorruptIndexException while reading Index" + e.getMessage(), e);
							cleanUpResource(reader);
							throw new DCMAApplicationException("CorruptIndexException while reading Index" + e.getMessage(), e);
						} catch (IOException e) {
							LOGGER.error("IOException while reading Index" + e.getMessage(), e);
							cleanUpResource(reader);
							throw new DCMAApplicationException("IOException while reading Index" + e.getMessage(), e);
						}
					} else {
						LOGGER.info("No index created for : " + eachConfig.getKey());
						continue;
					}
					MoreLikeThis moreLikeThis = new MoreLikeThis(reader);
					moreLikeThis.setFieldNames(allIndexFields);
					moreLikeThis.setMinTermFreq(Integer.parseInt(minTermFreq));
					moreLikeThis.setMinDocFreq(Integer.parseInt(minDocFreq));
					moreLikeThis.setMinWordLen(Integer.parseInt(minWordLength));
					moreLikeThis.setMaxQueryTerms(Integer.parseInt(maxQueryTerms));
					if (allStopWords != null && allStopWords.length > 0) {
						Set<String> stopWordsTemp = new HashSet<String>();
						for (int i = 0; i < allStopWords.length; i++) {
							stopWordsTemp.add(allStopWords[i]);
						}
						moreLikeThis.setStopWords(stopWordsTemp);
					}

					Map<String, String> docHocrContent = new HashMap<String, String>();

					String hocrContent = EMPTY_STRING;
					if (hocrPage.getHocrPage() != null && hocrPage.getHocrPage().size() > 0) {
						hocrContent = hocrPage.getHocrPage().get(0).getHocrContent();
					}
					docHocrContent.put(documentType, hocrContent);

					if (docHocrContent.size() > 0) {
						Set<String> docIds = docHocrContent.keySet();
						for (String eachDoc : docIds) {
							if (eachConfig.getDescription().equalsIgnoreCase(eachDoc)) {
								LOGGER.info("Generating query for Document: " + eachDoc);
								String docHocr = docHocrContent.get(eachDoc);
								if (null != docHocr) {
									InputStream inputStream = null;
									if (null != ignoreWordList && ignoreWordList.length > 0) {
										docHocr = removeIgnoreWordsFromHOCR(docHocr, ignoreWordList);
									}
									try {
										inputStream = new ByteArrayInputStream(docHocr.getBytes("UTF-8"));
										query = moreLikeThis.like(inputStream);
									} catch (UnsupportedEncodingException e) {
										LOGGER.error("Problem generating query for Document :  " + eachDoc, e);
										continue;
									} catch (IOException e) {
										LOGGER.error("Problem generating query for Document :  " + eachDoc, e);
										continue;
									} finally {
										if (inputStream != null) {
											try {
												inputStream.close();
											} catch (IOException e) {
												LOGGER.error("Problem in closing input stream. " + e.getMessage(), e);
											}
										}
									}
								} else {
									LOGGER.info("Empty HOCR content found for Document :  " + eachDoc);
									continue;
								}
								if (query != null && query.toString() != null && query.toString().length() > 0) {
									LOGGER.info("Generating confidence score for Document: " + eachDoc);
									try {
										returnMap = SearchFiles.generateConfidence(fuzzyIndexFolder, query.toString(), INDEX_FIELD,
												Integer.valueOf(numOfPages), ignoreWordList);
									} catch (NumberFormatException e) {
										LOGGER.error(CONFIDENCE_ERROR_MSG + eachDoc, e);
										continue;
									} catch (Exception e) {
										LOGGER.error(CONFIDENCE_ERROR_MSG + eachDoc, e);
										continue;
									}
									LOGGER.info("Return Map is : " + returnMap);
									int highestScoreDoc = fetchDocumentWithHighestScoreValue(returnMap, thresholdValue);
									if (highestScoreDoc != 0) {
										List<Object[]> extractedData = fetchDataForRow(highestScoreDoc, tableName, dbConnectionURL,
												dbName, dbDriver, dbUserName, dbPassword, eachConfig);
										documents = new Documents();
										updateDocument(documents, extractedData, eachConfig, batchClassIdentifier, documentType);
										LOGGER.info("Extracted data is : " + extractedData);
									} else {
										LOGGER.info("No document found with confidence score greater than threshold value : "
												+ thresholdValue);
									}
								} else {
									LOGGER.info("Empty query generated for Document : " + eachDoc);
								}
							}
						}
					}

				}
			} else {
				LOGGER.info("No properties configured for FUZZYDB_DOCUMENT_TYPE");
			}
		} finally {
			LOGGER.info("Closing input stream for index.");
			cleanUpResource(reader);
		}
		return documents;
	}
}
