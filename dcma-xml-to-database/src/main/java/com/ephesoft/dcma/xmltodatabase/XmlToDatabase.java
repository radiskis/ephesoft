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

package com.ephesoft.dcma.xmltodatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.xmltodatabase.contants.XmlToDatabaseConstants;

/**
 * This class is responsible to saving the document level fields of all the document of a particular batch.
 * 
 * @author Ephesoft
 * @version 1.0
 * 
 */
@Component
public class XmlToDatabase implements XmlToDatabaseConstants {

	/**
	 * properties to load data from properties file.
	 */
	private Properties properties;

	/**
	 * userName for the DB.
	 */
	private String userName;

	/**
	 * Switch for the XML to database plugin.
	 */
	private String pluginSwitch;

	/**
	 * Password for the DB.
	 */
	private String password;

	/**
	 * Driver name for a particular DBMS.
	 */
	private String driverName;

	/**
	 * JDBC URL corresponding to a DBMS.
	 */
	private String jdbcUrl;

	/**
	 * Connection to the DB.
	 */
	private Connection dbConnection;

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(XmlToDatabase.class);

	/**
	 * Reference of BatchSchemaService.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * @param batchSchemaService the batchSchemaService to set.
	 */
	public void setBatchSchemaService(final BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * Plugin switch for ON/OFF.
	 * 
	 * @param pluginSwitch
	 */
	public void setPluginSwitch(final String pluginSwitch) {
		this.pluginSwitch = pluginSwitch;
	}

	/**
	 * saves all the {@link DocumentLevelField} for a {@link BatchInstance} with {@link BatchInstanceID}.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @throws DCMAApplicationException on some error or failure
	 */
	public void getDLFFromXml(final String batchInstanceID) throws DCMAApplicationException {

		if (null == batchInstanceID) {
			LOGGER.error("batchInstanceId is null");
		}

		if (!ON_STRING.equalsIgnoreCase(pluginSwitch)) {
			LOGGER.info("XML to database plugin switch is off or null");
			return;
		}

		final Batch batch = batchSchemaService.getBatch(batchInstanceID);
		List<Document> documents = batch.getDocuments().getDocument();
		Set<String> tableNames = new HashSet<String>();
		Map<String, String> valueMap = new HashMap<String, String>();
		List<String> queries = new ArrayList<String>();

		for (Document document : documents) {
			List<DocField> documentLevelFields = new ArrayList<DocField>();
			if (document != null && document.getDocumentLevelFields() != null) {
				documentLevelFields = document.getDocumentLevelFields().getDocumentLevelField();
				LOGGER.info("Document level fields retrived from the XML");

				LOGGER.info("Preparing document level fields into query");
				for (DocField docField : documentLevelFields) {
					StringBuffer columnNameBuffer = new StringBuffer();
					StringBuffer valueBuffer = new StringBuffer();
					final String documentType = document.getType();
					final String docFieldName = docField.getName();

					columnNameBuffer.append(documentType);
					columnNameBuffer.append(DOT_SEPERATOR);
					columnNameBuffer.append(docFieldName);

					String tableName = properties.getProperty(columnNameBuffer.toString());
					tableNames.add(tableName.substring(0, tableName.indexOf(DOT_SEPERATOR)));
					final String docFieldType = docField.getType();
					if ((docFieldType == null) || docFieldType.equalsIgnoreCase(STRING_TYPE)) {
						valueBuffer.append(SINGLE_QUOTE_SEPERATOR);
						valueBuffer.append(docField.getValue());
						valueBuffer.append(SINGLE_QUOTE_SEPERATOR);
					} else {
						valueBuffer.append(docField.getValue());
					}
					valueMap.put(tableName, valueBuffer.toString());
				}
			}
		}

		Map<String, Map<String, String>> dlfMap = new HashMap<String, Map<String, String>>();
		for (Object object : valueMap.keySet()) {
			String stringObject = object.toString();
			String tableName = stringObject.substring(0, stringObject.indexOf(DOT_SEPERATOR));
			Map<String, String> tempMap = new HashMap<String, String>();
			if (tableNames.contains(tableName)) {
				if (dlfMap.containsKey(tableName)) {
					tempMap = dlfMap.get(tableName);
				}
				tempMap.put(object.toString(), valueMap.get(object.toString()));
				dlfMap.put(tableName, tempMap);
			}
		}

		for (String string : tableNames) {
			String columnNames = dlfMap.get(string).keySet().toString();
			String values = dlfMap.get(string).values().toString();
			StringBuffer queryStringBuffer = new StringBuffer();

			queryStringBuffer.append(INSERT_KEYWORD);
			queryStringBuffer.append(string);
			queryStringBuffer.append(OPEN_BRACKET);
			queryStringBuffer.append(columnNames.substring(1, columnNames.length() - 1));
			queryStringBuffer.append(CLOSE_BRACKET);
			queryStringBuffer.append(VALUES_KEYWORD);
			queryStringBuffer.append(OPEN_BRACKET);
			queryStringBuffer.append(values.substring(1, values.length() - 1));
			queryStringBuffer.append(CLOSE_BRACKET);
			queries.add(queryStringBuffer.toString());
		}
		try {
			LOGGER.info("Executing the queries" + queries);
			saveDLF(queries);
		} catch (DCMAApplicationException e) {
			LOGGER.error("Error Executing the queries: " + queries + e.getMessage(), e);
		}
	}

	/**
	 * Executes the query to save DLF to database.
	 * 
	 * @param queries
	 * @throws DCMAApplicationException
	 */
	public void saveDLF(final List<String> queries) throws DCMAApplicationException {

		for (String query : queries) {
			try {
				getDatabaseConnection();
				if (dbConnection != null) {
					LOGGER.info("Executing the query" + query);
					Statement statement = dbConnection.createStatement();
					statement.executeUpdate(query);
					statement.close();
				} else {
					LOGGER.error("Database connection is null.");
					throw new DCMAApplicationException("Database connection is null.");
				}
			} catch (SQLException e) {
				LOGGER.error("Invalid query for execution: " + query + e.getMessage(), e);
				throw new DCMAApplicationException("Invalid query: " + query + e.getMessage(), e);
			} finally {
				try {
					if (dbConnection != null) {
						dbConnection.close();
					}
				} catch (SQLException e) {
					LOGGER.info("Error in closing database connection: " + e.getMessage(), e);
					throw new DCMAApplicationException("Error in closing database connection: " + e.getMessage(), e);
				}
			}

		}
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(final String userName) {
		this.userName = userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * @return the driverName
	 */
	public String getDriverName() {
		return driverName;
	}

	/**
	 * @param driverName the driverName to set
	 */
	public void setDriverName(final String driverName) {
		this.driverName = driverName;
	}

	/**
	 * @return the jdbcUrl
	 */
	public String getJdbcUrl() {
		return jdbcUrl;
	}

	/**
	 * @param jdbcUrl the jdbcUrl to set
	 */
	public void setJdbcUrl(final String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * API to get {@link Connection} for the database.
	 * 
	 * @return {@link Connection}
	 * @throws DCMAApplicationException
	 */
	public Connection getDatabaseConnection() throws DCMAApplicationException {
		if (dbConnection == null) {
			try {
				LOGGER.info("Loading DB driver");
				Class.forName(driverName);
				LOGGER.info("Making DB connection");
				dbConnection = DriverManager.getConnection(jdbcUrl, userName, password);
			} catch (ClassNotFoundException e) {
				LOGGER.error("Database driver not found: " + e.getMessage(), e);
				throw new DCMAApplicationException("Database driver not found: " + e.getMessage());
			} catch (SQLException e) {
				LOGGER.error("Unable to create database connection: " + e.getMessage(), e);
				throw new DCMAApplicationException("Unable to create database connection: " + e.getMessage());
			}
		}
		return dbConnection;
	}

	public void setCon(final Connection dbConnection) {
		this.dbConnection = dbConnection;
	}

	public void setProperties(final Properties properties) {
		this.properties = properties;
	}
}
