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

package com.ephesoft.dcma.dbexport;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.hibernate.DynamicHibernateDao;
import com.ephesoft.dcma.dbexport.constant.DbExportConstant;

/**
 * This plugin is responsible for storing the values of the extracted document level fields depending on the mapping file provided.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.dbexport.constant.DbExportConstant
 */
public class DbExporter {

	/**
	 * Logger used for logging the information, errors etc.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DbExporter.class);

	/**
	 * Mapping dictionary storing the values from mapping file with key being the document type.
	 */
	private static Dictionary<String, ArrayList<String[]>> mappingDict;

	/**
	 * {@link DynamicHibernateDao} object used for doing database operations.
	 */
	private static DynamicHibernateDao dynamicHibernateDao;

	/**
	 * pluginMappingFileName to store the mapping file name.
	 */
	private String pluginMappingFileName;

	/**
	 * {@link BatchSchemaService} used for performing batch operations.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Instance of PluginPropertiesService.
	 **/
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * @return the batchSchemaService
	 */
	public BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * @param batchSchemaService the batchSchemaService to set
	 */
	public void setBatchSchemaService(final BatchSchemaService batchSchemaService) {
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
	public void setPluginPropertiesService(final PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	private String[] defineMapping(final String mapFileLine) {
		LOGGER.info("Parsing the mapping " + mapFileLine);
		String[] mappingArr = new String[DbExportConstant.MAPPING_CONTENT_SIZE_4];

		String[] tempMappingArr = new String[DbExportConstant.TEMP_CONTENT_SIZE_2];
		String tempString = mapFileLine;

		String delimiter = DbExportConstant.PERIOD;
		tempMappingArr = extractPlaceHolder(tempString, delimiter);
		mappingArr[DbExportConstant.DOC_TYPE_POSITION] = tempMappingArr[DbExportConstant.TEMP_ZERO_POSITION];

		tempString = tempMappingArr[DbExportConstant.TEMP_ONE_POSITION];

		delimiter = DbExportConstant.EQUALS_SIGN;
		tempMappingArr = extractPlaceHolder(tempString, delimiter);
		mappingArr[DbExportConstant.DOC_FIELD_POSITION] = tempMappingArr[DbExportConstant.TEMP_ZERO_POSITION];
		tempString = tempMappingArr[DbExportConstant.TEMP_ONE_POSITION];

		delimiter = DbExportConstant.COLON;
		tempMappingArr = extractPlaceHolder(tempString, delimiter);

		mappingArr[DbExportConstant.TABLE_NAME_POSITION] = tempMappingArr[DbExportConstant.TEMP_ZERO_POSITION];

		mappingArr[DbExportConstant.COLUMN_NAME_POSITION] = tempMappingArr[DbExportConstant.TEMP_ONE_POSITION];
		LOGGER.info("Parsed values are " + mappingArr);
		return mappingArr;
	}

	private String[] extractPlaceHolder(final String tempString, final String delimiter) {
		String[] tokenValues = new String[DbExportConstant.TEMP_CONTENT_SIZE_2];
		StringTokenizer stringTokenizer;
		stringTokenizer = new StringTokenizer(tempString, delimiter);
		if (stringTokenizer.hasMoreTokens()) {
			tokenValues[DbExportConstant.TEMP_ZERO_POSITION] = stringTokenizer.nextToken();
			tokenValues[DbExportConstant.TEMP_ONE_POSITION] = stringTokenizer.nextToken();
		}
		return tokenValues;
	}

	private void parseMapping(final File mappingFile) throws DCMAApplicationException {
		LOGGER.info("Parsing the Document level fields to database table mappings from mapping file: " + mappingFile);
		ArrayList<String[]> tempArr = new ArrayList<String[]>();
		try {
			Scanner scanner = new Scanner(mappingFile);
			while (scanner.hasNextLine()) {
				String currLine = scanner.nextLine().trim();
				if (!(currLine.isEmpty() && currLine.startsWith(DbExportConstant.PROPERTIES_COMMENT_PREFIX))) {
					String[] sArr = defineMapping(currLine);
					if (mappingDict.get(sArr[DbExportConstant.TEMP_ZERO_POSITION]) != null) {
						mappingDict.get(sArr[DbExportConstant.TEMP_ZERO_POSITION]).add(sArr);
					} else {
						tempArr = new ArrayList<String[]>();
						tempArr.add(sArr);
						mappingDict.put(sArr[DbExportConstant.TEMP_ZERO_POSITION], tempArr);
					}
				}
			}
		} catch (FileNotFoundException fne) {
			LOGGER.error("Error in parsing DB Export Plugin mapping file, FileNotFoundException: " + fne.getMessage(), fne);
			throw new DCMAApplicationException("Error in parsing DB Export Plugin mapping file, FileNotFoundException: "
					+ fne.getMessage(), fne);
		} catch (NoSuchElementException nse) {
			LOGGER.error("Error in parsing DB Export Plugin mapping file, NoSuchElementException: " + nse.getMessage(), nse);
			throw new DCMAApplicationException("Error in parsing DB Export Plugin mapping file, NoSuchElementException: "
					+ nse.getMessage(), nse);
		} catch (Exception e) {
			LOGGER.error("Error in parsing DB Export Plugin mapping file, Exception: " + e.getMessage(), e);
			throw new DCMAApplicationException("Error in parsing DB Export Plugin mapping file, Exception: " + e.getMessage(), e);
		}
	}

	private void matchMap(final String batchId, final String batchClass, final String docId, final String docType,
			final String docField, final String docFieldVal) throws DCMAApplicationException {

		LOGGER.info("Matching the mapping for the " + "batchId: " + batchId + " batchClass: " + batchClass + " docId: " + docId
				+ " DocType: " + docType + " DocField: " + docField + " docFieldValue: " + docFieldVal);
		ArrayList<String[]> tempArr = null;
		if (mappingDict.get(docType) != null) {
			tempArr = mappingDict.get(docType);
			for (int i = 0; i < tempArr.size(); i++) {
				String[] mapArr = tempArr.get(i);
				String mappedDocField = mapArr[DbExportConstant.DOC_FIELD_POSITION];
				if (mappedDocField.equalsIgnoreCase(docField)) {
					String mappedDocType = mapArr[DbExportConstant.DOC_TYPE_POSITION];
					String mappedDbTable = mapArr[DbExportConstant.TABLE_NAME_POSITION];
					String mappedDbTableColumn = mapArr[DbExportConstant.COLUMN_NAME_POSITION];
					LOGGER.info("DB_Export_Plugin, Exporting to DB:\n" + "batchId: " + batchId + " batchClass: " + batchClass
							+ " docId: " + docId + " DocType: " + mappedDocType + " DocField: " + mappedDocField + " docFieldValue: "
							+ docFieldVal + " to the DB table: " + mappedDbTable);

					exportMetaDataToDB(mappedDbTable, batchId, batchClass, mappedDocType, mappedDocField, docFieldVal,
							mappedDbTableColumn);
				}
			}
		}
	}

	private void exportMetaDataToDB(final String dbTable, final String batchId, final String batchClass, final String docType,
			final String docField, final String docFieldValue, final String mappedDbTableColumn) throws DCMAApplicationException {
		String errorMessage = "DB Export Plugin: Problem occured in updating database table ";
		Connection connection = null;
		try {
			LOGGER.info("Preparing Query..");
			connection = dynamicHibernateDao.getConnectionProvider().getConnection();
			StatelessSession statelessSession = dynamicHibernateDao.getStatelessSession(connection);
			Transaction transaction = statelessSession.getTransaction();
			StringBuffer dbQueryBuffer = new StringBuffer(DbExportConstant.INSERT_INTO);
			dbQueryBuffer.append(dbTable);

			String columnNames = prepareColumnNamesString(mappedDbTableColumn);
			dbQueryBuffer.append(columnNames);

			String values = prepareQueryValues(batchId, batchClass, docType, docField, docFieldValue);
			dbQueryBuffer.append(values);

			String dbQuery = null;
			dbQuery = dbQueryBuffer.toString();
			LOGGER.info("Prepared query : " + dbQuery);

			SQLQuery query = dynamicHibernateDao.createUpdateOrInsertQuery(statelessSession, dbQuery);
			transaction.begin();
			int result = query.executeUpdate();
			transaction.commit();
			statelessSession.close();
			LOGGER.info("DB Export Plugin: Result of SQL commital transaction = " + result);
		} catch (HibernateException e) {
			LOGGER.error(errorMessage + dbTable + e.getMessage(), e);
			throw new DCMAApplicationException(errorMessage + dbTable + e.getMessage(), e);
		} catch (SQLException e) {
			LOGGER.error(errorMessage + dbTable + e.getMessage(), e);
			throw new DCMAApplicationException(errorMessage + dbTable + e.getMessage(), e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					LOGGER.error("Error closing the database session: " + e.getMessage(), e);
				}
			}
			if (dynamicHibernateDao != null) {
				dynamicHibernateDao.closeSession();
			}
		}
	}

	private String prepareQueryValues(final String batchId, final String batchClass, final String docType, final String docField,
			final String docFieldValue) {
		StringBuffer dbQueryValueBuffer = new StringBuffer(DbExportConstant.VALUES_CONSTANT);
		dbQueryValueBuffer.append(batchClass);
		dbQueryValueBuffer.append(DbExportConstant.QOUTED_COMMA);
		dbQueryValueBuffer.append(batchId);
		dbQueryValueBuffer.append(DbExportConstant.QOUTED_COMMA);
		dbQueryValueBuffer.append(docType);
		dbQueryValueBuffer.append(DbExportConstant.QOUTED_COMMA);
		dbQueryValueBuffer.append(docField);
		dbQueryValueBuffer.append(DbExportConstant.QOUTED_COMMA);
		dbQueryValueBuffer.append(docFieldValue);
		dbQueryValueBuffer.append(DbExportConstant.SINGLE_QOUTE);
		dbQueryValueBuffer.append(DbExportConstant.CLOSING_BRACKET);
		return dbQueryValueBuffer.toString();
	}

	private String prepareColumnNamesString(final String mappedDbTableColumn) {
		StringBuffer dbQueryColumnBuffer = new StringBuffer(DbExportConstant.OPEN_BRACKET);
		dbQueryColumnBuffer.append(DbExportConstant.BATCH_CLASS_ID);
		dbQueryColumnBuffer.append(DbExportConstant.COMMA);
		dbQueryColumnBuffer.append(DbExportConstant.BATCH_INSTANCE_ID);
		dbQueryColumnBuffer.append(DbExportConstant.COMMA);
		dbQueryColumnBuffer.append(DbExportConstant.DOCUMENT_TYPE);
		dbQueryColumnBuffer.append(DbExportConstant.COMMA);
		dbQueryColumnBuffer.append(DbExportConstant.DOCUMENT_LEVEL_FIELD_NAME);
		dbQueryColumnBuffer.append(DbExportConstant.COMMA);
		dbQueryColumnBuffer.append(mappedDbTableColumn);
		dbQueryColumnBuffer.append(DbExportConstant.CLOSING_BRACKET);
		return dbQueryColumnBuffer.toString();
	}

	private void initHibernateDao(final String batchId) throws DCMAApplicationException {
		LOGGER.info("DB Export: Initializing database connectivity properties...");
		String userName = pluginPropertiesService.getPropertyValue(batchId, DbExportConstant.DB_EXPORT_PLUGIN_NAME,
				DbExporterProperties.DB_EXPORT_USER);
		String password = pluginPropertiesService.getPropertyValue(batchId, DbExportConstant.DB_EXPORT_PLUGIN_NAME,
				DbExporterProperties.DB_EXPORT_PASSWORD);
		String dbDriver = pluginPropertiesService.getPropertyValue(batchId, DbExportConstant.DB_EXPORT_PLUGIN_NAME,
				DbExporterProperties.DB_EXPORT_DRIVER);
		String connectionUrl = pluginPropertiesService.getPropertyValue(batchId, DbExportConstant.DB_EXPORT_PLUGIN_NAME,
				DbExporterProperties.DB_EXPORT_CONN_URL);

		LOGGER.info("User Name: " + userName);
		LOGGER.info("Password: " + password);
		LOGGER.info("Database Driver: " + dbDriver);
		LOGGER.info("Database Connection URL: " + connectionUrl);

		if (userName == null || userName.isEmpty()) {
			String errorMessage = "DB Export Plugin: User Name property value is null/empty from the database. Invalid initializing of properties.";
			LOGGER.error(errorMessage);
			throw new DCMAApplicationException(errorMessage);
		}
		if (password == null || password.isEmpty()) {
			String errorMessage = "DB Export Plugin: Password property value is null/empty from the database. Invalid initializing of properties.";
			LOGGER.error(errorMessage);
			throw new DCMAApplicationException(errorMessage);
		}
		if (dbDriver == null || dbDriver.isEmpty()) {
			String errorMessage = "DB Export Plugin: Database Driver property value is null/empty from the database. Invalid initializing of properties.";
			LOGGER.error(errorMessage);
			throw new DCMAApplicationException(errorMessage);
		}
		if (connectionUrl == null || connectionUrl.isEmpty()) {
			String errorMessage = "DB Export Plugin: Connection URL property value is null/empty from the database. Invalid initializing of properties.";
			LOGGER.error(errorMessage);
			throw new DCMAApplicationException(errorMessage);
		}
		LOGGER.info("DB Export Plugin: Properties Initialized Successfully");
		// dynamicHibernateDao = null;
		try {
			dynamicHibernateDao = new DynamicHibernateDao(userName, password, dbDriver, connectionUrl);
		} catch (Exception e) {
			String errorMessage = "DB Export Plugin: Error in inititializing Hibernate Connection: ";
			LOGGER.error(errorMessage + e.getMessage(), e);
			throw new DCMAApplicationException(errorMessage + e.getMessage(), e);
		}
	}

	private void processBatchDocs(final Batch batch) throws DCMAApplicationException {
		String batchInstanceId = batch.getBatchInstanceIdentifier();
		String batchClassId = batch.getBatchClassIdentifier();
		List<Document> documentList = batch.getDocuments().getDocument();
		for (int documentIndex = 0; documentIndex < documentList.size(); documentIndex++) {
			Document document = documentList.get(documentIndex);
			String docType = document.getType();
			String docId = document.getIdentifier();
			List<DocField> dlfList = document.getDocumentLevelFields().getDocumentLevelField();
			for (int i = 0; i < dlfList.size(); i++) {
				DocField dlf = dlfList.get(i);
				if (dlf.getName() != null && dlf.getValue() != null) {
					String docFieldName = dlf.getName();
					String docFieldValue = dlf.getValue();
					matchMap(batchInstanceId, batchClassId, docId, docType, docFieldName, docFieldValue);
				}
			}
		}
	}

	/***
	 * The exportBatchToDb() method is the primary method exposed by the DbExporter class. It initiates the processes of parsing the
	 * plugin's mapping file and commencing the export sequence.
	 * 
	 * @param batchInstanceId {@link String}
	 * @throws DCMAApplicationException exception in exporting batch document level fields to database
	 */
	public void exportBatchToDb(final String batchInstanceId) throws DCMAApplicationException {
		LOGGER.info("Entering Database Export plugin (DB_EXPORT_PLUGIN)...");
		String isDbExportSwitchON = pluginPropertiesService.getPropertyValue(batchInstanceId, DbExportConstant.DB_EXPORT_PLUGIN_NAME,
				DbExporterProperties.DB_EXPORT_SWITCH);
		if (DbExportConstant.STATE.equalsIgnoreCase(isDbExportSwitchON)) {
			LOGGER.info("DB_EXPORT_PLUGIN is switched on.");
			try {

				Batch batch = batchSchemaService.getBatch(batchInstanceId);
				String pluginMappingFolderName = batchSchemaService.getAbsolutePath(batch.getBatchClassIdentifier(),
						batchSchemaService.getDbExportMappingFolderName(), false);
				String mappingFileCompletePath = pluginMappingFolderName + File.separator + getPluginMappingFileName();

				File file = new File(mappingFileCompletePath);

				mappingDict = new Hashtable<String, ArrayList<String[]>>();
				parseMapping(file);

				initHibernateDao(batch.getBatchInstanceIdentifier());
				processBatchDocs(batch);
			} catch (Exception e) {
				String errorMessage = "DB Export Plugin: Problem occured in exporting batch document level fields to database";
				LOGGER.error(errorMessage, e);
				throw new DCMAApplicationException(errorMessage, e);
			}
	} else {
			LOGGER.info("DB_EXPORT_PLUGIN is switched off.");
		}
	}

	/**
	 * getter for pluginMappingFileName.
	 * @return {@link String} the pluginMappingFileName
	 */
	public String getPluginMappingFileName() {
		return pluginMappingFileName;
	}

	/**
	 * setter for pluginMappingFileName.
	 * @param pluginMappingFileName {@link String}
	 */
	public void setPluginMappingFileName(final String pluginMappingFileName) {
		this.pluginMappingFileName = pluginMappingFileName;
	}

}
