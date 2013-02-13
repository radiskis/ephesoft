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
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.hibernate.DynamicHibernateDao;
import com.ephesoft.dcma.core.hibernate.DynamicHibernateDao.ColumnDefinition;
import com.ephesoft.dcma.da.dao.BatchInstanceDao;
import com.ephesoft.dcma.da.service.BatchClassPluginConfigService;
import com.ephesoft.dcma.da.service.FieldTypeService;
import com.ephesoft.dcma.da.service.PageTypeService;
import com.ephesoft.dcma.fuzzydb.constants.FuzzyDBSearchConstants;

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
public class FuzzyLuceneEngine {

	/**
	 * Logger instance for logging using slf4j for logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(FuzzyLuceneEngine.class);

	/**
	 * Instance of {@link BatchSchemaService}.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;
	/**
	 * Instance of {@link BatchInstanceDao}.
	 */
	@Autowired
	private BatchInstanceDao batchInstanceDao;

	/**
	 * Instance of {@link PluginPropertiesService} for batch instance.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;
	/**
	 * Instance of {@link PluginPropertiesService} for batch class.
	 */
	@Autowired
	@Qualifier("batchClassPluginPropertiesService")
	private PluginPropertiesService pluginPropertiesServiceBatchClass;

	/**
	 * Instance of {@link BatchClassPluginConfigService}.
	 */
	@Autowired
	private BatchClassPluginConfigService batchClassPluginConfigService;

	/**
	 * Instance of {@link PageTypeService}.
	 */
	@Autowired
	private PageTypeService pageTypeService;

	/**
	 * Instance of {@link FieldTypeService}.
	 */
	@Autowired
	private FieldTypeService fieldTypeService;

	/**
	 * row id which is the primary key or return column name for each table.
	 */
	private transient String rowID;

	/**
	 * List of batch class IDs in properties file for which learn DB should be called.
	 */
	private transient String batchClassIDList;

	/**
	 * Ignore words array that are to be ignored while performing fuzzy db extraction.
	 */
	private String[] ignoreWordList;

	/**
	 * Set ignore word list.
	 * 
	 * @param ignoreList {@link String}
	 */
	public void setIgnoreList(String ignoreList) {
		this.ignoreWordList = ignoreList.split(FuzzyDBSearchConstants.SPLIT_CONSTANT);
	}

	/**
	 * getter for batchSchemaService.
	 * @return {@link BatchSchemaService}
	 */
	public BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * setter for batchSchemaService.
	 * @param batchSchemaService {@link BatchSchemaService}
	 */
	public void setBatchSchemaService(BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * @return the {@link PluginPropertiesService}
	 */
	public PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	/**
	 * @param pluginPropertiesService the {@link PluginPropertiesService}
	 */
	public void setPluginPropertiesService(PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	/**
	 * @return the {@link PageTypeService}
	 */
	public PageTypeService getPageTypeService() {
		return pageTypeService;
	}

	/**
	 * @param pageTypeService the {@link PageTypeService}
	 */
	public void setPageTypeService(PageTypeService pageTypeService) {
		this.pageTypeService = pageTypeService;
	}

	/**
	 * @return the {@link BatchClassPluginConfigService}
	 */
	public BatchClassPluginConfigService getBatchClassPluginConfigService() {
		return batchClassPluginConfigService;
	}

	/**
	 * @param batchClassPluginConfigService the {@link BatchClassPluginConfigService}
	 */
	public void setBatchClassPluginConfigService(BatchClassPluginConfigService batchClassPluginConfigService) {
		this.batchClassPluginConfigService = batchClassPluginConfigService;
	}

	/**
	 * @return the {@link FieldTypeService}
	 */
	public FieldTypeService getFieldTypeService() {
		return fieldTypeService;
	}

	/**
	 * @param the fieldTypeService {@link FieldTypeService}
	 */
	public void setFieldTypeService(FieldTypeService fieldTypeService) {
		this.fieldTypeService = fieldTypeService;
	}

	/**
	 * @return the pluginPropertiesServiceBatchClass {@link PluginPropertiesService}
	 */
	public PluginPropertiesService getPluginPropertiesServiceBatchClass() {
		return pluginPropertiesServiceBatchClass;
	}

	/**
	 * @param pluginPropertiesServiceBatchClass the {@link PluginPropertiesService}
	 */
	public void setPluginPropertiesServiceBatchClass(PluginPropertiesService pluginPropertiesServiceBatchClass) {
		this.pluginPropertiesServiceBatchClass = pluginPropertiesServiceBatchClass;
	}

	/**
	 * @return {@link String}
	 */
	public String getRowID() {
		return rowID;
	}

	/**
	 * @param rowID {@link String}
	 */
	public void setRowID(String rowID) {
		this.rowID = rowID;
	}

	/**
	 * @return {@link BatchInstanceDao}.
	 */
	public BatchInstanceDao getBatchInstanceDao() {
		return batchInstanceDao;
	}

	/**
	 * @param batchInstanceDao {@link BatchInstanceDao} 
	 */
	public void setBatchInstanceDao(BatchInstanceDao batchInstanceDao) {
		this.batchInstanceDao = batchInstanceDao;
	}

	/**
	 * This API learns DB for a list of batch classes 'batchClassIDList' given in fuzzy-db properties file.
	 */
	public void learnDataBaseForMultipleBatchClasses() {
		LOGGER.info("Entering method learnDataBaseForMultipleBatchClasses...");
		String batchClassIDList = getBatchClassIDList();
		if (batchClassIDList != null && !batchClassIDList.isEmpty()) {
			LOGGER.info("Start splitting batch class list given in fuzzy DB  properties file ");
			String[] batchClassIDs = batchClassIDList.split(FuzzyDBSearchConstants.SPLIT_CONSTANT);
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
	 * @param batchClassIdentifier {@link String}
	 * @param createIndex boolean
	 */
	public void learnFuzzyDatabase(final String batchClassIdentifier, boolean createIndex) throws DCMAApplicationException {

		Map<String, String> properties = batchClassPluginConfigService.getPluginPropertiesForBatchClass(batchClassIdentifier,
				FuzzyDBSearchConstants.FUZZYDB_PLUGIN, null);
		if (properties != null && !properties.isEmpty()) {
			LOGGER.info("Fetching properties from DB");
			// String dbName = properties.get(FuzzyDBProperties.FUZZYDB_DB_NAME.getPropertyKey());
			String dbDriver = properties.get(FuzzyDBProperties.FUZZYDB_DB_DRIVER.getPropertyKey());
			String dbUserName = properties.get(FuzzyDBProperties.FUZZYDB_DB_USER_NAME.getPropertyKey());
			String dbPassword = properties.get(FuzzyDBProperties.FUZZYDB_DB_PASSWORD.getPropertyKey());
			String dbConnectionURL = properties.get(FuzzyDBProperties.FUZZYDB_CONNECTION_URL.getPropertyKey());
			String dateFormat = properties.get(FuzzyDBProperties.FUZZYDB_DATE_FORMAT.getPropertyKey());
			String dbName = FuzzyDBSearchConstants.EMPTY_STRING;
			if (dbConnectionURL != null && dbConnectionURL.length() > 0) {
				dbName = getDatabaseName(dbConnectionURL, dbDriver);
			}
			LOGGER.info("Properties fetched successfully from DB");
			String baseFuzzyIndexFolder = batchSchemaService.getFuzzyDBIndexFolder(batchClassIdentifier, false);
			if (dbName.length() <= 0) {
				LOGGER.info(FuzzyDBSearchConstants.WRONG_DB_NAME_MESSAGE);
				return;
			}

			BatchDynamicPluginConfiguration[] pluginPropsDocType = pluginPropertiesServiceBatchClass.getDynamicPluginProperties(
					batchClassIdentifier, FuzzyDBSearchConstants.FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_DOCUMENT_TYPE);
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

	private String getDatabaseName(String dbConnectionURL, String dbDriver) {
		String dbName = FuzzyDBSearchConstants.EMPTY_STRING;
		if (dbDriver.equals(FuzzyDBSearchConstants.MYSQL_DRIVER)) {
			dbName = dbConnectionURL.substring(dbConnectionURL.lastIndexOf(FuzzyDBSearchConstants.DATABASE_URL_SEPERATOR) + 1,
					dbConnectionURL.length());
		} else if (dbDriver.equals(FuzzyDBSearchConstants.MSSQL_DRIVER)) {
			String dbKeywordString = FuzzyDBSearchConstants.DATABASE_CONSTANT;
			dbName = extractDbNameValueFromUrl(dbConnectionURL, dbKeywordString);
		} else if (dbDriver.equals(FuzzyDBSearchConstants.JTDS_DRIVER)) {
			String dbKeywordString = FuzzyDBSearchConstants.DATABASE_NAME_CONSTANT;
			dbName = extractDbNameValueFromUrl(dbConnectionURL, dbKeywordString);
		}
		dbName = com.ephesoft.dcma.util.FileUtils.replaceInvalidFileChars(dbName);
		return dbName;
	}

	private String extractDbNameValueFromUrl(String dbConnectionURL, String dbKeywordString) {
		String dbName = FuzzyDBSearchConstants.EMPTY_STRING;
		StringTokenizer stringTokenizer = new StringTokenizer(dbConnectionURL, FuzzyDBSearchConstants.SPLIT_CONSTANT);
		while (stringTokenizer.hasMoreTokens()) {
			String token = stringTokenizer.nextToken();
			if (token.contains(dbKeywordString)) {
				final String[] databaseString = token.split(FuzzyDBSearchConstants.EQUALS_DELIM);
				if (databaseString.length == 2) {
					dbName = databaseString[1];
					break;
				}
			}
		}
		return dbName;
	}

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
	 * @param rowId long
	 * @param tableName {@link String}
	 * @param dbConnectionURL {@link String}
	 * @param dbName {@link String}
	 * @param dbDriver {@link String}
	 * @param dbUserName {@link String}
	 * @param dbPassword {@link String}
	 * @param eachConfig {@link com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchDynamicPluginConfiguration}
	 * @return {@link List<Object[]>}
	 * @throws DCMAApplicationException if any exception occurs.
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> fetchDataForRow(long rowId, String tableName, String dbConnectionURL, String dbName, String dbDriver,
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
								if (dbConnectionURL.contains(FuzzyDBSearchConstants.MYSQL_DATABASE)) {
									retrunFieldName = FuzzyDBSearchConstants.SINGLE_QUOTES + eachColumn.getValue()
											+ FuzzyDBSearchConstants.SINGLE_QUOTES;
								} else {
									retrunFieldName = FuzzyDBSearchConstants.DOUBLE_QUOTES + eachColumn.getValue()
											+ FuzzyDBSearchConstants.DOUBLE_QUOTES;
								}
							}
						}
					} else {
						LOGGER.info(FuzzyDBSearchConstants.COLUMN_NAME_ERROR_MSG);
						throw new DCMAApplicationException(FuzzyDBSearchConstants.COLUMN_NAME_ERROR_MSG);
					}
					dbQuery.append(" from ").append(tableName).append(" where ").append(retrunFieldName).append(
							FuzzyDBSearchConstants.EQUALS).append(rowId);
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
	 * @param rowId long
	 * @param confidenceScore {@link String}
	 * @param tableName {@link String}
	 * @param dbConnectionURL {@link String}
	 * @param dbName {@link String}
	 * @param dbDriver {@link String}
	 * @param dbUserName {@link String}
	 * @param dbPassword {@link String}
	 * @param eachConfig {@link com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchDynamicPluginConfiguration}
	 * @param isHeaderAdded boolean
	 * @return {@link List<List<String>>}
	 * @throws DCMAApplicationException if any exception occurs.
	 */
	@SuppressWarnings("unchecked")
	public List<List<String>> fetchFuzzySearchResult(long rowId, String confidenceScore, String tableName, String dbConnectionURL,
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
								if (dbConnectionURL.contains(FuzzyDBSearchConstants.MYSQL_DATABASE)) {
									retrunFieldName = FuzzyDBSearchConstants.SINGLE_QUOTES + eachColumn.getValue()
											+ FuzzyDBSearchConstants.SINGLE_QUOTES;
								} else {
									retrunFieldName = FuzzyDBSearchConstants.DOUBLE_QUOTES + eachColumn.getValue()
											+ FuzzyDBSearchConstants.DOUBLE_QUOTES;
								}
							}
						}
						if (isHeaderAdded) {
							list.add("Confidence Score");
							extractedData.add(list);
						}
					} else {
						LOGGER.info(FuzzyDBSearchConstants.COLUMN_NAME_ERROR_MSG);
						throw new DCMAApplicationException(FuzzyDBSearchConstants.COLUMN_NAME_ERROR_MSG);
					}
					dbQuery.append(new StringBuffer(" from ")).append(tableName).append(new StringBuffer(" where ")).append(
							retrunFieldName).append(new StringBuffer(FuzzyDBSearchConstants.EQUALS)).append(rowId);
					SQLQuery query = dynamicHibernateDao.createQuery(dbQuery.toString());
					List<Object[]> dataList = query.list();

					if (null != dataList) {
						for (Object[] obj : dataList) {
							List<String> list = new ArrayList<String>();
							for (Object object : obj) {
								if (object == null) {
									object = new StringBuffer(FuzzyDBSearchConstants.SPACE_DELIMITER);
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
	 * @param tableName {@link String}
	 * @param dbConnectionURL {@link String}
	 * @param dbName {@link String}
	 * @param dbDriver {@link String}
	 * @param dbUserName {@link String}
	 * @param dbPassword {@link String}
	 * @param dateFormat {@link String}
	 * @param eachConfig {@link com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchDynamicPluginConfiguration}
	 * @return {@link List<String>}
	 * @throws DCMAApplicationException if any exception occurs.
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
								&& (colDataType.equalsIgnoreCase(FuzzyDBSearchConstants.TIMESTAMP_NAME) || colDataType
										.equalsIgnoreCase(FuzzyDBSearchConstants.DATE_NAME))) {
							dateColIndex.add(count);
						}
					}
					dbQuery.append(new StringBuffer(" from ")).append(tableName);
					SQLQuery query = dynamicHibernateDao.createQuery(dbQuery.toString());
					List<Object[]> dataList = query.list();

					StringBuffer dbRow = new StringBuffer(FuzzyDBSearchConstants.EMPTY_STRING);
					int indexCount = 0;
					if (dataList != null && !dataList.isEmpty()) {
						returnList = new ArrayList<String>();
						if (dataList.size() > 1) {
							LOGGER.info("More than One records found. So picking first record.");
						}
						for (Object[] eachRecord : dataList) {
							indexCount = 0;
							dbRow = new StringBuffer(FuzzyDBSearchConstants.EMPTY_STRING);
							for (Object eachElement : eachRecord) {
								indexCount++;
								if (eachElement != null) {
									if (dateColIndex.contains(indexCount)) {
										SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
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
		if (dbConnectionURL.contains(FuzzyDBSearchConstants.MYSQL_DATABASE)) {
			if (count < allColumnNames.size()) {
				dbQuery.append(FuzzyDBSearchConstants.SINGLE_QUOTES).append(eachColumn.getValue()).append(
						FuzzyDBSearchConstants.SINGLE_QUOTES).append(FuzzyDBSearchConstants.COMMA);
			} else {
				dbQuery.append(FuzzyDBSearchConstants.SINGLE_QUOTES).append(eachColumn.getValue()).append(
						FuzzyDBSearchConstants.SINGLE_QUOTES);
			}
		} else {
			if (count < allColumnNames.size()) {
				dbQuery.append(FuzzyDBSearchConstants.DOUBLE_QUOTES).append(eachColumn.getValue()).append(
						FuzzyDBSearchConstants.DOUBLE_QUOTES).append(FuzzyDBSearchConstants.COMMA);
			} else {
				dbQuery.append(FuzzyDBSearchConstants.DOUBLE_QUOTES).append(eachColumn.getValue()).append(
						FuzzyDBSearchConstants.DOUBLE_QUOTES);
			}
		}
	}

	/**
	 * This method fetches the data type of a column supplied.
	 * 
	 * @param eachColumn {@link String}
	 * @param colNames {@link List<ColumnDefinition>}
	 * @return {@link String}
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
	 * @param allColumnNames {@link Set<BatchPluginConfiguration>}
	 * @param colName {@link String}
	 * @return int
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
	 * @param batchInstanceIdentifier {@link String}
	 * @param createIndex boolean
	 */
	public boolean extractDataBaseFields(final String batchInstanceIdentifier) throws DCMAApplicationException {
		LOGGER.info("Initializing properties...");
		String switchValue = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FuzzyDBSearchConstants.FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_SWITCH);
		String searchableColumns = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				FuzzyDBSearchConstants.FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_SEARCH_COLUMNS);
		String dbUserName = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FuzzyDBSearchConstants.FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_DB_USER_NAME);
		String dbPassword = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FuzzyDBSearchConstants.FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_DB_PASSWORD);
		String thresholdValue = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				FuzzyDBSearchConstants.FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_THRESHOLD_VALUE);
		String numOfPages = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FuzzyDBSearchConstants.FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_NO_OF_PAGES);

		String dbDriver = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FuzzyDBSearchConstants.FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_DB_DRIVER);
		boolean isSearchSuccessful = false;
		if (FuzzyDBSearchConstants.SWITCH_ON.equalsIgnoreCase(switchValue)) {
			isSearchSuccessful = processFuzzyDBExtraction(batchInstanceIdentifier, searchableColumns, dbUserName, dbPassword,
					thresholdValue, numOfPages, dbDriver);
		} else {
			LOGGER.info("Skipping FuzzyDB extraction. Switch set as off.");
		}
		return isSearchSuccessful;
	}

	private boolean processFuzzyDBExtraction(final String batchInstanceIdentifier, String searchableColumns, String dbUserName,
			String dbPassword, String thresholdValue, String numOfPages, String dbDriver) throws DCMAApplicationException {
		String dbConnectionURL = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				FuzzyDBSearchConstants.FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_CONNECTION_URL);
		boolean isSearchSuccessful = true;
		String dbName = FuzzyDBSearchConstants.EMPTY_STRING;
		if (dbConnectionURL != null && dbConnectionURL.length() > 0) {
			dbName = getDatabaseName(dbConnectionURL, dbDriver);
		}
		if (dbName.length() <= 0) {
			LOGGER.info(FuzzyDBSearchConstants.WRONG_DB_NAME_MESSAGE);
			isSearchSuccessful = false;
		} else {
			LOGGER.info("Properties Initialized Successfully");
			Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);
			String batchClassIdentifier = batch.getBatchClassIdentifier();
			String indexFolder = batchSchemaService.getFuzzyDBIndexFolder(batchClassIdentifier, false);

			File baseFuzzyDbIndexFolder = new File(indexFolder);
			if (baseFuzzyDbIndexFolder != null && !baseFuzzyDbIndexFolder.exists()) {
				LOGGER.info(FuzzyDBSearchConstants.FUZZY_DB_INDEX_FOLDER_DOES_NOT_EXIST_MESSAGE);
				isSearchSuccessful = false;
			} else {
				IndexReader reader = null;
				try {
					List<com.ephesoft.dcma.da.domain.PageType> allPageTypes = pluginPropertiesService
							.getPageTypes(batchInstanceIdentifier);
					if (!(allPageTypes != null && !allPageTypes.isEmpty())) {
						LOGGER.info("Page Types not configured in Database");
						isSearchSuccessful = false;
					} else {
						BatchDynamicPluginConfiguration[] pluginPropsDocType = pluginPropertiesService.getDynamicPluginProperties(
								batchInstanceIdentifier, FuzzyDBSearchConstants.FUZZYDB_PLUGIN,
								FuzzyDBProperties.FUZZYDB_DOCUMENT_TYPE);
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
										isSearchSuccessful = false;
									} catch (IOException e) {
										LOGGER.error("IOException while reading Index" + e.getMessage(), e);
										cleanUpResource(reader);
										isSearchSuccessful = false;
									}
								} else {
									LOGGER.info("No index created for : " + eachConfig.getKey());
									continue;
								}
								if (isSearchSuccessful) {
									List<Document> xmlDocuments = batch.getDocuments().getDocument();
									if (!searchableColumns.isEmpty()) {
										// New Code here
										extractThroughAlreadyExtractedContent(batchInstanceIdentifier, numOfPages, dbDriver,
												dbConnectionURL, dbUserName, dbPassword, thresholdValue, dbName, batch, reader,
												eachConfig, tableName, fuzzyIndexFolder, xmlDocuments, searchableColumns);
									} else {
										extractThroughOCRContent(batchInstanceIdentifier, numOfPages, dbDriver, dbConnectionURL,
												dbUserName, dbPassword, thresholdValue, dbName, batch, reader, eachConfig, tableName,
												fuzzyIndexFolder, xmlDocuments);
									}
								}
							}// end of dynamic loop
						} else {
							LOGGER.info("No properties configured for FUZZYDB_DOCUMENT_TYPE");
						}
					}
				} finally {
					LOGGER.info("Closing input stream for index.");
					cleanUpResource(reader);
				}
				if (isSearchSuccessful) {
					batchSchemaService.updateBatch(batch);
					LOGGER.info("updateBatchXML done.");
				}
			}
		}
		return isSearchSuccessful;
	}

	private void extractThroughAlreadyExtractedContent(final String batchInstanceIdentifier, String numOfPages, String dbDriver,
			String dbConnectionURL, String dbUserName, String dbPassword, String thresholdValue, String dbName, Batch batch,
			IndexReader reader, BatchDynamicPluginConfiguration eachConfig, String tableName, String fuzzyIndexFolder,
			List<Document> xmlDocuments, String searchableColumns) throws DCMAApplicationException {

		Map<String, Float> returnMap = null;
		List<String> searchColumnList = new ArrayList<String>(0);
		String queryDelimiters = FuzzyDBSearchConstants.DOLLAR_DELIMITER + FuzzyDBSearchConstants.COMMA;
		StringTokenizer tokens = new StringTokenizer(searchableColumns, queryDelimiters);
		while (tokens.hasMoreTokens()) {
			searchColumnList.add(tokens.nextToken());
		}

		for (Document eachDoc : xmlDocuments) {
			StringBuilder queryBuffer = new StringBuilder();
			// only if * is not there
			// To be compared with the mapped table
			if (eachConfig.getDescription().equalsIgnoreCase(eachDoc.getType())) {
				List<DocField> documentLevelField = eachDoc.getDocumentLevelFields().getDocumentLevelField();

				for (DocField docField : documentLevelField) {
					if (searchColumnList.contains(FuzzyDBSearchConstants.STAR) || searchColumnList.contains(docField.getName())) {
						// Append values of all the DLF's rather than *
						final String fieldValue = docField.getValue();
						if (fieldValue != null) {
							queryBuffer.append(FuzzyDBSearchConstants.INDEX_FIELD);
							queryBuffer.append(FuzzyDBSearchConstants.QUERY_STRING_DELIMITER);
							queryBuffer.append(fieldValue);
							queryBuffer.append(FuzzyDBSearchConstants.SPACE_DELIMITER);
						}
					}

				}
			}

			if (queryBuffer != null && queryBuffer.toString() != null && queryBuffer.length() > 0) {
				LOGGER.info(FuzzyDBSearchConstants.GENERATING_CONFIDENCE_MESSAGE + eachDoc);
				try {
					returnMap = SearchFiles.generateConfidence(fuzzyIndexFolder, queryBuffer.toString(),
							FuzzyDBSearchConstants.INDEX_FIELD, Integer.valueOf(numOfPages), ignoreWordList);
				} catch (NumberFormatException e) {
					LOGGER.error(FuzzyDBSearchConstants.CONFIDENCE_ERROR_MSG + eachDoc, e);
					continue;
				} catch (Exception e) {
					LOGGER.error(FuzzyDBSearchConstants.CONFIDENCE_ERROR_MSG + eachDoc, e);
					continue;
				}
				LOGGER.info(FuzzyDBSearchConstants.RETURN_MAP_MESSAGE + returnMap);
				FuzzyDBResultInfo fuzzyDBResultInfo = fetchDocumentWithHighestScoreValue(returnMap, thresholdValue);
				if (fuzzyDBResultInfo != null) {
					long highestScoreDoc = parsingConfidenceScore(fuzzyDBResultInfo);
					if (highestScoreDoc != 0) {
						List<Object[]> extractedData = fetchDataForRow(highestScoreDoc, tableName, dbConnectionURL, dbName, dbDriver,
								dbUserName, dbPassword, eachConfig);
						LOGGER.info(FuzzyDBSearchConstants.EXTRACTED_DATA_MESSAGE + extractedData);
						LOGGER.info("Updating XML....");
						updateBatchXML(batch, extractedData, eachDoc.getIdentifier(), batchInstanceIdentifier, eachConfig,
								fuzzyDBResultInfo.getConfidence());
					} else {
						LOGGER.info("No document found with confidence score greater than threshold value : " + thresholdValue);

						String hocrSwitchValue = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
								FuzzyDBSearchConstants.FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_HOCR_SWITCH);
						if (FuzzyDBSearchConstants.SWITCH_ON.equalsIgnoreCase(hocrSwitchValue)) {
							LOGGER.info("HOCR search switch is ON.");
							LOGGER.info("Trying Extracting through the complete HOCR Content.");
							extractThroughOCRContent(batchInstanceIdentifier, numOfPages, dbDriver, dbConnectionURL, dbUserName,
									dbPassword, thresholdValue, dbName, batch, reader, eachConfig, tableName, fuzzyIndexFolder,
									xmlDocuments);
						} else {
							LOGGER.info("HOCR search switch is off. No updation made to XML file.");
						}
					}
				}
			} else {
				LOGGER.info(FuzzyDBSearchConstants.EMPTY_QUERY_MESSAGE + eachDoc);
				String hocrSwitchValue = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
						FuzzyDBSearchConstants.FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_HOCR_SWITCH);
				if (FuzzyDBSearchConstants.SWITCH_ON.equalsIgnoreCase(hocrSwitchValue)) {
					LOGGER.info("HOCR search switch is ON.");
					LOGGER.info("Trying Extracting through the complete HOCR Content.");
					extractThroughOCRContent(batchInstanceIdentifier, numOfPages, dbDriver, dbConnectionURL, dbUserName, dbPassword,
							thresholdValue, dbName, batch, reader, eachConfig, tableName, fuzzyIndexFolder, xmlDocuments);
				} else {
					LOGGER.info("HOCR search switch is off. No updation made to XML file.");
				}
			}
		}

	}

	private Map<String, Float> extractThroughOCRContent(final String batchInstanceIdentifier, String numOfPages, String dbDriver,
			String dbConnectionURL, String dbUserName, String dbPassword, String thresholdValue, String dbName, Batch batch,
			IndexReader reader, BatchDynamicPluginConfiguration eachConfig, String tableName, String fuzzyIndexFolder,
			List<Document> xmlDocuments) throws DCMAApplicationException {
		String minTermFreq = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FuzzyDBSearchConstants.FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_MIN_TERM_FREQ);
		String minDocFreq = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FuzzyDBSearchConstants.FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_MIN_DOC_FREQ);
		String minWordLength = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				FuzzyDBSearchConstants.FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_MIN_WORD_LENGTH);
		String maxQueryTerms = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				FuzzyDBSearchConstants.FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_MAX_QUERY_TERMS);
		String includePages = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FuzzyDBSearchConstants.FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_INCLUDE_PAGES);
		String stopWords = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FuzzyDBSearchConstants.FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_STOP_WORDS);
		String[] allStopWords = stopWords.split(FuzzyDBSearchConstants.SPLIT_CONSTANT);
		String indexFields = FuzzyDBSearchConstants.INDEX_FIELD;
		String[] allIndexFields = indexFields.split(FuzzyDBSearchConstants.SPLIT_CONSTANT);
		Map<String, Float> returnMap = null;
		Query query = null;
		MoreLikeThis moreLikeThis = updateQueryInfo(reader, minTermFreq, minDocFreq, minWordLength, maxQueryTerms, allStopWords,
				allIndexFields);
		Map<String, String> docHocrContent = new HashMap<String, String>();
		if (includePages != null && includePages.equalsIgnoreCase(FuzzyDBSearchConstants.ALLPAGES)) {
			updateDocHOCRForAllDoc(batchInstanceIdentifier, xmlDocuments, docHocrContent);
		} else if (includePages != null && includePages.equalsIgnoreCase(FuzzyDBSearchConstants.FIRSTPAGE)) {
			updateDocHocrContent(batchInstanceIdentifier, xmlDocuments, docHocrContent);
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
							LOGGER.error(FuzzyDBSearchConstants.PROBLEM_GENERATING_QUERY_MESSAGE + eachDoc, e);
							continue;
						} catch (IOException e) {
							LOGGER.error(FuzzyDBSearchConstants.PROBLEM_GENERATING_QUERY_MESSAGE + eachDoc, e);
							continue;
						} finally {
							IOUtils.closeQuietly(inputStream);
						}
					} else {
						LOGGER.info("Empty HOCR content found for Document :  " + eachDoc);
						continue;
					}
					if (query != null && query.toString() != null && query.toString().length() > 0) {
						LOGGER.info(FuzzyDBSearchConstants.GENERATING_CONFIDENCE_MESSAGE + eachDoc);
						try {
							returnMap = SearchFiles.generateConfidence(fuzzyIndexFolder, query.toString(),
									FuzzyDBSearchConstants.INDEX_FIELD, Integer.valueOf(numOfPages), ignoreWordList);
						} catch (NumberFormatException e) {
							LOGGER.error(FuzzyDBSearchConstants.CONFIDENCE_ERROR_MSG + eachDoc, e);
							continue;
						} catch (Exception e) {
							LOGGER.error(FuzzyDBSearchConstants.CONFIDENCE_ERROR_MSG + eachDoc, e);
							continue;
						}
						LOGGER.info(FuzzyDBSearchConstants.RETURN_MAP_MESSAGE + returnMap);
						FuzzyDBResultInfo fuzzyDBResultInfo = fetchDocumentWithHighestScoreValue(returnMap, thresholdValue);
						if (fuzzyDBResultInfo != null) {
							long highestScoreDoc = parsingConfidenceScore(fuzzyDBResultInfo);
							if (highestScoreDoc != 0) {
								List<Object[]> extractedData = fetchDataForRow(highestScoreDoc, tableName, dbConnectionURL, dbName,
										dbDriver, dbUserName, dbPassword, eachConfig);
								LOGGER.info(FuzzyDBSearchConstants.EXTRACTED_DATA_MESSAGE + extractedData);
								LOGGER.info("Updating XML....");
								updateBatchXML(batch, extractedData, eachDoc, batchInstanceIdentifier, eachConfig, fuzzyDBResultInfo
										.getConfidence());
							} else {
								LOGGER
										.info("No document found with confidence score greater than threshold value : "
												+ thresholdValue);
							}
						}
					} else {
						LOGGER.info(FuzzyDBSearchConstants.EMPTY_QUERY_MESSAGE + eachDoc);
					}
				}
			}
		}
		return returnMap;
	}

	private MoreLikeThis updateQueryInfo(IndexReader reader, String minTermFreq, String minDocFreq, String minWordLength,
			String maxQueryTerms, String[] allStopWords, String[] allIndexFields) {
		MoreLikeThis moreLikeThis = new MoreLikeThis(reader);
		moreLikeThis.setFieldNames(allIndexFields);
		moreLikeThis.setMinTermFreq(Integer.parseInt(minTermFreq));
		moreLikeThis.setMinDocFreq(Integer.parseInt(minDocFreq));
		moreLikeThis.setMinWordLen(Integer.parseInt(minWordLength));
		moreLikeThis.setMaxQueryTerms(Integer.parseInt(maxQueryTerms));
		if (allStopWords != null && allStopWords.length > 0) {
			Set<String> stopWordsTemp = new HashSet<String>();
			for (int index = 0; index < allStopWords.length; index++) {
				stopWordsTemp.add(allStopWords[index]);
			}
			moreLikeThis.setStopWords(stopWordsTemp);
		}
		return moreLikeThis;
	}

	private void updateDocHOCRForAllDoc(final String batchInstanceIdentifier, List<Document> xmlDocuments,
			Map<String, String> docHocrContent) {
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
				hocrContent.append(FuzzyDBSearchConstants.SPACE_DELIMITER);
			}
			docHocrContent.put(eachDocType.getIdentifier(), hocrContent.toString());
		}
	}

	private void updateDocHocrContent(final String batchInstanceIdentifier, List<Document> xmlDocuments,
			Map<String, String> docHocrContent) {
		for (Document eachDocType : xmlDocuments) {
			String hocrContent = FuzzyDBSearchConstants.EMPTY_STRING;
			List<Page> pages = eachDocType.getPages().getPage();
			String pageId = pages.get(0).getIdentifier();
			HocrPages hocrPages = batchSchemaService.getHocrPages(batchInstanceIdentifier, pageId);
			List<HocrPage> hocrPageList = hocrPages.getHocrPage();
			HocrPage hocrPage = hocrPageList.get(0);
			hocrContent = hocrPage.getHocrContent();
			docHocrContent.put(eachDocType.getIdentifier(), hocrContent);
		}
	}

	private String removeIgnoreWordsFromHOCR(final String docHocr, final String[] ignoreWordList) {
		String finalDocHocr = docHocr;
		if (null != finalDocHocr && null != ignoreWordList && ignoreWordList.length > 0) {
			for (String ignoreWord : ignoreWordList) {
				if (ignoreWord != null && !ignoreWord.isEmpty()) {
					try {
						finalDocHocr = finalDocHocr.replaceAll(FuzzyDBSearchConstants.WORD_BOUNDRY_REGEX + ignoreWord
								+ FuzzyDBSearchConstants.WORD_BOUNDRY_REGEX, FuzzyDBSearchConstants.EMPTY_STRING);
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
	 * @param batchInstanceIdentifier {@link String}
	 * @param documentType {@link String}
	 * @param searchText {@link String}
	 * @throws DCMAApplicationException for handling any exception.
	 * @return {@link List<List<String>>}
	 */
	public List<List<String>> fuzzyTextSearch(final String batchInstanceIdentifier, final String documentType, final String searchText)
			throws DCMAApplicationException {
		LOGGER.info("Initializing properties...");
		String searchTextInLowerCase = searchText.toLowerCase(Locale.getDefault());
		List<List<String>> extractedData = null;
		String numOfPages = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FuzzyDBSearchConstants.FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_NO_OF_PAGES);
		String dbDriver = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FuzzyDBSearchConstants.FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_DB_DRIVER);
		String dbConnectionURL = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				FuzzyDBSearchConstants.FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_CONNECTION_URL);
		String dbUserName = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FuzzyDBSearchConstants.FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_DB_USER_NAME);
		String thresholdValue = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				FuzzyDBSearchConstants.FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_THRESHOLD_VALUE);
		String dbPassword = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, FuzzyDBSearchConstants.FUZZYDB_PLUGIN,
				FuzzyDBProperties.FUZZYDB_DB_PASSWORD);
		String dbName = FuzzyDBSearchConstants.EMPTY_STRING;
		if (dbConnectionURL != null && dbConnectionURL.length() > 0) {
			dbName = getDatabaseName(dbConnectionURL, dbDriver);
		}
		String queryDelimiters = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				FuzzyDBSearchConstants.FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_QUERY_DELIMITERS);
		LOGGER.info("Properties Initialized Successfully");
		Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);
		String batchClassIdentifier = batch.getBatchClassIdentifier();
		String indexFolder = batchSchemaService.getFuzzyDBIndexFolder(batchClassIdentifier, false);

		if (dbName.length() <= 0) {
			LOGGER.info(FuzzyDBSearchConstants.WRONG_DB_NAME_MESSAGE);
			return extractedData;
		}

		File baseFuzzyDbIndexFolder = new File(indexFolder);
		if (baseFuzzyDbIndexFolder != null && !baseFuzzyDbIndexFolder.exists()) {
			LOGGER.info(FuzzyDBSearchConstants.FUZZY_DB_INDEX_FOLDER_DOES_NOT_EXIST_MESSAGE);
			return extractedData;
		}

		Map<String, Float> returnMap = new HashMap<String, Float>();
		Set<FuzzyDBResultInfo> resultSet = new TreeSet<FuzzyDBResultInfo>();

		List<com.ephesoft.dcma.da.domain.PageType> allPageTypes = pluginPropertiesService.getPageTypes(batchInstanceIdentifier);
		if (!(allPageTypes != null && !allPageTypes.isEmpty())) {
			LOGGER.info("Page Types not configured in Database");
			return extractedData;
		}

		BatchDynamicPluginConfiguration[] pluginPropsDocType = pluginPropertiesService.getDynamicPluginProperties(
				batchInstanceIdentifier, FuzzyDBSearchConstants.FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_DOCUMENT_TYPE);
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
					StringTokenizer tokens = new StringTokenizer(searchTextInLowerCase, queryDelimiters);

					StringBuffer query = createQueryForTextSearch(tokens);
					if (query != null && query.toString() != null && query.toString().length() > 0) {
						LOGGER.info(FuzzyDBSearchConstants.GENERATING_CONFIDENCE_MESSAGE + searchTextInLowerCase);
						try {
							returnMap = SearchFiles.generateConfidence(fuzzyIndexFolder, query.toString(),
									FuzzyDBSearchConstants.INDEX_FIELD, Integer.valueOf(numOfPages), this.ignoreWordList);
							for (String key : returnMap.keySet()) {
								FuzzyDBResultInfo fuzzyDBResultInfo = new FuzzyDBResultInfo(returnMap.get(key), key);
								resultSet.add(fuzzyDBResultInfo);
							}
						} catch (NumberFormatException e) {
							LOGGER.error(FuzzyDBSearchConstants.CONFIDENCE_ERROR_MSG + searchTextInLowerCase, e);
							continue;
						} catch (Exception e) {
							LOGGER.error(FuzzyDBSearchConstants.CONFIDENCE_ERROR_MSG + searchTextInLowerCase, e);
							continue;
						}
						LOGGER.info(FuzzyDBSearchConstants.RETURN_MAP_MESSAGE + returnMap);
						if (resultSet != null && resultSet.size() > 0) {
							if (extractedData == null) {
								extractedData = new ArrayList<List<String>>();
							}
							boolean isHeaderAdded = true;
							Set<FuzzyDBResultInfo> set = fetchDocumentHavingThresholdValue(resultSet, thresholdValue);
							for (FuzzyDBResultInfo fuzzyDBResultInfo : set) {
								Float confidenceScore = fuzzyDBResultInfo.getConfidence();
								long keyLong = 0;
								try {
									keyLong = Long.parseLong(fuzzyDBResultInfo.getRowId());
								} catch (NumberFormatException nfe) {
									LOGGER.error(nfe.getMessage(), nfe);
									return null;
								}
								extractedData.addAll(fetchFuzzySearchResult(keyLong, confidenceScore.toString(), tableName,
										dbConnectionURL, dbName, dbDriver, dbUserName, dbPassword, eachConfig, isHeaderAdded));
								isHeaderAdded = false;
								LOGGER.info(FuzzyDBSearchConstants.EXTRACTED_DATA_MESSAGE + extractedData);
							}
						} else {
							LOGGER.info("No record found ");
						}
					} else {
						LOGGER.info(FuzzyDBSearchConstants.EMPTY_QUERY_MESSAGE + searchTextInLowerCase);
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

	private StringBuffer createQueryForTextSearch(StringTokenizer tokens) {
		StringBuffer query = new StringBuffer();
		while (tokens.hasMoreTokens()) {
			query.append(FuzzyDBSearchConstants.INDEX_FIELD);
			query.append(FuzzyDBSearchConstants.QUERY_STRING_DELIMITER);
			query.append(tokens.nextToken());
			query.append(FuzzyDBSearchConstants.SPACE_DELIMITER);
		}
		return query;
	}

	private String getDocTypeByID(String docID, Batch batch) {
		String returnValue = FuzzyDBSearchConstants.EMPTY_STRING;
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
	 * @param batch {@link Batch}
	 * @param extractedData {@link List<Object []>}
	 * @param documentId {@link String}
	 * @param batchInstanceId {@link String}
	 * @param eachConfig {@link com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchDynamicPluginConfiguration}
	 * @param confidenceScore {@link Float}
	 */
	public void updateBatchXML(final Batch batch, final List<Object[]> extractedData, final String documentId, String batchInstanceId,
			final BatchDynamicPluginConfiguration eachConfig, Float confidenceScore) {

		BatchDynamicPluginConfiguration[] pluginPropsDocType = pluginPropertiesService.getDynamicPluginProperties(batchInstanceId,
				FuzzyDBSearchConstants.FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_DOCUMENT_TYPE);
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
											newDocLevelField.setConfidence(confidenceScore);
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
										docLevelField.setConfidence(confidenceScore);
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
	 * @param extractedData {@link List<Object []>}
	 * @param eachConfig {@link com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchDynamicPluginConfiguration}
	 * @param batchClassIdentifier {@link String}
	 * @param documentType {@link String}
	 * @return {@link Document}
	 */
	public Documents updateDocument(final List<Object[]> extractedData, final BatchDynamicPluginConfiguration eachConfig,
			String batchClassIdentifier, String documentType) {
		Documents documents = new Documents();
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

			List<com.ephesoft.dcma.da.domain.FieldType> allFdTypes = fieldTypeService.getFdTypeByDocTypeNameForBatchClass(
					documentType, batchClassIdentifier);

			if (allFdTypes != null) {
				for (com.ephesoft.dcma.da.domain.FieldType fdType : allFdTypes) {
					DocField docLevelField = new DocField();
					docLevelField.setName(fdType.getName());
					docLevelField.setFieldOrderNumber(fdType.getFieldOrderNumber());
					docLevelField.setType(fdType.getDataType().name());
					Object newValue = getValueForDocField(fdType.getName(), allColumnNames, extractedData);
					if (newValue != null) {
						docLevelField.setValue(newValue.toString());
					} else {
						docLevelField.setValue(FuzzyDBSearchConstants.EMPTY_STRING);
					}
					docLevelFields.add(docLevelField);
				}
			} else {
				LOGGER.info("No field types could be found for document type :" + documentType);
			}
		}
		return documents;
	}

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
	 * @param docField {@link String}
	 * @param allColumnNames {@link Set<BatchDynamicPluginConfiguration>}
	 * @param extractedData {@link List<Object []>}
	 * @return {@link Object}
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
	 * @param batchDocNameScore {@link Map<String, Float>}
	 * @param thresholdValue {@link String}
	 * @return {@link FuzzyDBResultInfo}
	 */
	public FuzzyDBResultInfo fetchDocumentWithHighestScoreValue(Map<String, Float> batchDocNameScore, String thresholdValue) {
		FuzzyDBResultInfo fuzzyDBResultInfo = null;
		float highestValue = 0f;
		Set<String> set = batchDocNameScore.keySet();
		Iterator<String> iterator = set.iterator();
		String rowId = null;
		while (iterator.hasNext()) {
			String learnName = iterator.next();
			float eachValue = batchDocNameScore.get(learnName);
			if (eachValue > highestValue) {
				highestValue = eachValue;
				rowId = learnName;
			}
		}
		if (highestValue > Float.valueOf(thresholdValue)) {
			fuzzyDBResultInfo = new FuzzyDBResultInfo(highestValue, rowId);

		}
		return fuzzyDBResultInfo;
	}

	/**
	 * To fetch the documents that have threshold value.
	 * @param resultSet {@link Set<FuzzyDBResultInfo>}
	 * @param thresholdValue {@link String}
	 * @return {@link Set<FuzzyDBResultInfo>}
	 */
	public Set<FuzzyDBResultInfo> fetchDocumentHavingThresholdValue(Set<FuzzyDBResultInfo> resultSet, String thresholdValue) {
		Set<FuzzyDBResultInfo> thresholdSet = new TreeSet<FuzzyDBResultInfo>();
		for (FuzzyDBResultInfo fuzzyDBResultInfo : resultSet) {
			float eachValue = fuzzyDBResultInfo.getConfidence();
			if (eachValue > Float.valueOf(thresholdValue)) {
				thresholdSet.add(fuzzyDBResultInfo);
			}
		}
		return thresholdSet;
	}

	/**
	 * @param batchClassIDList {@link String}
	 */
	public void setBatchClassIDList(String batchClassIDList) {
		this.batchClassIDList = batchClassIDList;
	}

	/**
	 * @return {@link String}
	 */
	public String getBatchClassIDList() {
		return batchClassIDList;
	}

	/**
	 * This method creates/updates the value of document level fields for each document by searching for similarities in HOCR content
	 * with the data in database tables mapped for each document type.
	 * @param batchClassIdentifier {@link String}
	 * @param documentType {@link String}
	 * @param hocrPage {@link HocrPages}
	 * @return {@link Documents}
	 * @throws DCMAApplicationException if there is no index file present inside folder.
	 */
	public Documents extractDataBaseFields(final String batchClassIdentifier, String documentType, HocrPages hocrPage)
			throws DCMAApplicationException {
		Documents documents = null;
		String indexFields = FuzzyDBSearchConstants.INDEX_FIELD;
		String stopWords = pluginPropertiesServiceBatchClass.getPropertyValue(batchClassIdentifier,
				FuzzyDBSearchConstants.FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_STOP_WORDS);
		String minTermFreq = pluginPropertiesServiceBatchClass.getPropertyValue(batchClassIdentifier,
				FuzzyDBSearchConstants.FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_MIN_TERM_FREQ);
		String minDocFreq = pluginPropertiesServiceBatchClass.getPropertyValue(batchClassIdentifier,
				FuzzyDBSearchConstants.FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_MIN_DOC_FREQ);
		String minWordLength = pluginPropertiesServiceBatchClass.getPropertyValue(batchClassIdentifier,
				FuzzyDBSearchConstants.FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_MIN_WORD_LENGTH);
		String maxQueryTerms = pluginPropertiesServiceBatchClass.getPropertyValue(batchClassIdentifier,
				FuzzyDBSearchConstants.FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_MAX_QUERY_TERMS);
		String numOfPages = pluginPropertiesServiceBatchClass.getPropertyValue(batchClassIdentifier,
				FuzzyDBSearchConstants.FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_NO_OF_PAGES);
		String dbDriver = pluginPropertiesServiceBatchClass.getPropertyValue(batchClassIdentifier,
				FuzzyDBSearchConstants.FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_DB_DRIVER);
		String dbConnectionURL = pluginPropertiesServiceBatchClass.getPropertyValue(batchClassIdentifier,
				FuzzyDBSearchConstants.FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_CONNECTION_URL);
		String dbUserName = pluginPropertiesServiceBatchClass.getPropertyValue(batchClassIdentifier,
				FuzzyDBSearchConstants.FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_DB_USER_NAME);
		String dbPassword = pluginPropertiesServiceBatchClass.getPropertyValue(batchClassIdentifier,
				FuzzyDBSearchConstants.FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_DB_PASSWORD);
		String thresholdValue = pluginPropertiesServiceBatchClass.getPropertyValue(batchClassIdentifier,
				FuzzyDBSearchConstants.FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_THRESHOLD_VALUE);
		String dbName = FuzzyDBSearchConstants.EMPTY_STRING;

		if (dbConnectionURL != null && dbConnectionURL.length() > 0) {
			dbName = getDatabaseName(dbConnectionURL, dbDriver);
		}
		LOGGER.info("Properties Initialized Successfully");
		String indexFolder = batchSchemaService.getFuzzyDBIndexFolder(batchClassIdentifier, false);
		if (dbName.length() <= 0) {
			throw new DCMAApplicationException(FuzzyDBSearchConstants.WRONG_DB_NAME_MESSAGE);
		}
		File baseFuzzyDbIndexFolder = new File(indexFolder);
		if (baseFuzzyDbIndexFolder != null && !baseFuzzyDbIndexFolder.exists()) {
			throw new DCMAApplicationException(FuzzyDBSearchConstants.FUZZY_DB_INDEX_FOLDER_DOES_NOT_EXIST_MESSAGE);
		}
		String[] allIndexFields = indexFields.split(FuzzyDBSearchConstants.SPLIT_CONSTANT);
		String[] allStopWords = stopWords.split(FuzzyDBSearchConstants.SPLIT_CONSTANT);
		IndexReader reader = null;
		Query query = null;
		Map<String, Float> returnMap = new HashMap<String, Float>();
		try {
			BatchDynamicPluginConfiguration[] pluginPropsDocType = pluginPropertiesServiceBatchClass.getDynamicPluginProperties(
					batchClassIdentifier, FuzzyDBSearchConstants.FUZZYDB_PLUGIN, FuzzyDBProperties.FUZZYDB_DOCUMENT_TYPE);
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
						reader = getReader(reader, fuzzyIndexFolder);
					} else {
						LOGGER.info("No index created for : " + eachConfig.getKey());
						continue;
					}
					MoreLikeThis moreLikeThis = updateQueryInfo(reader, minTermFreq, minDocFreq, minWordLength, maxQueryTerms,
							allStopWords, allIndexFields);
					Map<String, String> docHocrContent = new HashMap<String, String>();
					String hocrContent = getHocrContent(hocrPage);
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
										LOGGER.error(FuzzyDBSearchConstants.PROBLEM_GENERATING_QUERY_MESSAGE + eachDoc, e);
										continue;
									} catch (IOException e) {
										LOGGER.error(FuzzyDBSearchConstants.PROBLEM_GENERATING_QUERY_MESSAGE + eachDoc, e);
										continue;
									} finally {
										IOUtils.closeQuietly(inputStream);
									}
								} else {
									LOGGER.info("Empty HOCR content found for Document :  " + eachDoc);
									continue;
								}
								if (query != null && query.toString() != null && query.toString().length() > 0) {
									LOGGER.info(FuzzyDBSearchConstants.GENERATING_CONFIDENCE_MESSAGE + eachDoc);
									try {
										returnMap = SearchFiles.generateConfidence(fuzzyIndexFolder, query.toString(),
												FuzzyDBSearchConstants.INDEX_FIELD, Integer.valueOf(numOfPages), ignoreWordList);
									} catch (NumberFormatException e) {
										LOGGER.error(FuzzyDBSearchConstants.CONFIDENCE_ERROR_MSG + eachDoc, e);
										continue;
									} catch (Exception e) {
										LOGGER.error(FuzzyDBSearchConstants.CONFIDENCE_ERROR_MSG + eachDoc, e);
										continue;
									}
									LOGGER.info(FuzzyDBSearchConstants.RETURN_MAP_MESSAGE + returnMap);
									FuzzyDBResultInfo fuzzyDBResultInfo = fetchDocumentWithHighestScoreValue(returnMap, thresholdValue);
									if (fuzzyDBResultInfo != null) {
										long highestScoreDoc = parsingConfidenceScore(fuzzyDBResultInfo);
										List<Object[]> extractedData = fetchDataForRow(highestScoreDoc, tableName, dbConnectionURL,
												dbName, dbDriver, dbUserName, dbPassword, eachConfig);
										documents = updateDocument(extractedData, eachConfig, batchClassIdentifier, documentType);
										LOGGER.info(FuzzyDBSearchConstants.EXTRACTED_DATA_MESSAGE + extractedData);
									} else {
										LOGGER.info("No document found with confidence score greater than threshold value : "
												+ thresholdValue);
									}
								} else {
									LOGGER.info(FuzzyDBSearchConstants.EMPTY_QUERY_MESSAGE + eachDoc);
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

	private String getHocrContent(HocrPages hocrPage) {
		String hocrContent = FuzzyDBSearchConstants.EMPTY_STRING;
		if (hocrPage.getHocrPage() != null && hocrPage.getHocrPage().size() > 0) {
			hocrContent = hocrPage.getHocrPage().get(0).getHocrContent();
		}
		return hocrContent;
	}

	private long parsingConfidenceScore(FuzzyDBResultInfo fuzzyDBResultInfo) {
		long highestScoreDoc = 0;
		try {
			highestScoreDoc = Long.parseLong(fuzzyDBResultInfo.getRowId());
		} catch (NumberFormatException e) {
			LOGGER.error("Unable to fetch Fuzzy DB result. Non Integer RowId returned.");
		}
		return highestScoreDoc;
	}

	private IndexReader getReader(IndexReader reader, String fuzzyIndexFolder) throws DCMAApplicationException {
		IndexReader localReader = reader;
		try {
			localReader = IndexReader.open(FSDirectory.open(new File(fuzzyIndexFolder)), true);
		} catch (CorruptIndexException e) {
			cleanUpResource(localReader);
			throw new DCMAApplicationException("CorruptIndexException while reading Index" + e.getMessage(), e);
		} catch (IOException e) {
			cleanUpResource(localReader);
			throw new DCMAApplicationException("IOException while reading Index" + e.getMessage(), e);
		}
		return localReader;
	}

	/**
	 * The results info class for fuzzy db process.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 * @see com.ephesoft.dcma.core.component.ICommonConstants 
	 */
	public class FuzzyDBResultInfo implements Comparable<FuzzyDBResultInfo> {

		/**
		 * Confidence score.
		 */
		private Float confidence;
		/**
		 * To store row id.
		 */
		private String rowId;
		/**
		 * Parameterized constructor.
		 * @param confidence {@link Float}
		 * @param rowId {@link String}
		 */
		public FuzzyDBResultInfo(Float confidence, String rowId) {
			this.confidence = confidence;
			this.rowId = rowId;
		}
		/**
		 * getter for confidence.
		 * @return {@link Float}
		 */
		public Float getConfidence() {
			return confidence;
		}
		/**
		 * Setter for confidence.
		 * @param confidence {@link Float}
		 */
		public void setConfidence(Float confidence) {
			this.confidence = confidence;
		}
		/**
		 * getter for rowId.
		 * @return {@link String}
		 */
		public String getRowId() {
			return rowId;
		}
		/**
		 * setter for rowId.
		 * @param rowId {@link String}
		 */
		public void setRowId(String rowId) {
			this.rowId = rowId;
		}

		@Override
		public int compareTo(FuzzyDBResultInfo fuzzyDBResultInfo) {
			if (this.getConfidence() > fuzzyDBResultInfo.getConfidence()) {
				return -1;
			} else {
				return 1;
			}
		}
	}
}
