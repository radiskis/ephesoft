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

package com.ephesoft.dcma.core.hibernate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.cfg.Configuration;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.type.NullableType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.ephesoft.dcma.encryption.core.PasswordDecryptor;
import com.ephesoft.dcma.encryption.exception.CryptographyException;

/**
 * Class to get connections, seesion and create queries.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.encryption.core.PasswordDecryptor
 *
 */
public class DynamicHibernateDao {

	/**
	 * META_INF String.
	 */
	private static final String META_INF = "META-INF";
	
	/**
	 * FOLDER_NAME String.
	 */
	private static final String FOLDER_NAME = "dcma-performance-reporting";
	
	/**
	 * FILE_NAME String.
	 */
	private static final String FILE_NAME = "dcma-report-db";
	
	/**
	 * TABLE String.
	 */
	private static final String TABLE = "TABLE";
	
	/**
	 * VIEW String.
	 */
	private static final String VIEW = "VIEW";
	
	/**
	 * sessionFactory SessionFactory.
	 */
	private SessionFactory sessionFactory = null;
	
	/**
	 * connectionProvider ConnectionProvider.
	 */ 
	private ConnectionProvider connectionProvider = null;
	
	/**
	 * connection Connection.
	 */
	private Connection connection = null;

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(DynamicHibernateDao.class);
	
	/**
	 * PASSWORD String.
	 */
	private static final String PASSWORD = "hibernate.connection.password";

	
	/**
	 * Constructor.
	 * 
	 * @param userName String
	 * @param password String
	 * @param driverName String
	 * @param jdbcUrl String
	 * @param dialectName String
	 */
	public DynamicHibernateDao(String userName, String password, String driverName, String jdbcUrl, String dialectName) {
		Properties properties = createHibernateProperties(userName, password, driverName, jdbcUrl, dialectName);

		Configuration configuration = new Configuration().addProperties(properties);
		try {
			if (sessionFactory != null) {
				sessionFactory.close();
			}
		} catch (Exception e) {
			LOG.error("Could not close session factory", e);
		}
		sessionFactory = configuration.buildSessionFactory();
	}

	/**
	 * Constructor.
	 * 
	 * @param userName String
	 * @param password String
	 * @param driverName String
	 * @param jdbcUrl String
	 */
	public DynamicHibernateDao(String userName, String password, String driverName, String jdbcUrl) {
		this(userName, password, driverName, jdbcUrl, null);
	}

	/**
	 * Constructor.
	 * 
	 * @param cfgLocation String
	 */
	public DynamicHibernateDao(String cfgLocation) {
		Configuration configuration = new Configuration().configure(cfgLocation);
		String filePath = META_INF + File.separator + FOLDER_NAME + File.separator + FILE_NAME + ".properties";
		Properties properties;
		InputStream propertyInStream = null;
		try {
			propertyInStream = new ClassPathResource(filePath).getInputStream();
			properties = new Properties();
			properties.load(propertyInStream);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new HibernateException(e.getMessage(), e);
		} finally {
			if (propertyInStream != null) {
				try {
					propertyInStream.close();
				} catch (IOException ioe) {
					LOG.info("Could not close property input stream in Dynamic Hibernate Dao.");
				}
			}
		}
		for (Object propertyKey : properties.keySet()) {
			String propertyKeyString = (String) propertyKey;
			if (!propertyKeyString.equalsIgnoreCase(PASSWORD)) {
				configuration.setProperty(propertyKeyString, properties.getProperty(propertyKeyString));
			} else {
				PasswordDecryptor passwordDecryptor = new PasswordDecryptor(properties.getProperty(propertyKeyString));
				try {
					configuration.setProperty(propertyKeyString, passwordDecryptor.getDecryptedString());
				} catch (CryptographyException e) {
					LOG.error(e.getMessage(), e);
					throw new HibernateException(e.getMessage(), e);
				}
			}
		}
		try {
			if (sessionFactory != null) {
				sessionFactory.close();
			}
		} catch (Exception e) {
			LOG.error("Could not close session factory", e);
		}
		sessionFactory = configuration.buildSessionFactory();
	}

	/**
	 * To get Connection Provider.
	 * @return ConnectionProvider
	 */
	public ConnectionProvider getConnectionProvider() {
		ConnectionProvider connectionProvider = null;
		connectionProvider = ((SessionFactoryImplementor) this.sessionFactory).getConnectionProvider();
		return connectionProvider;
	}

	/**
	 * To get Stateless Session.
	 * @param connection Connection
	 * @return StatelessSession
	 */
	public StatelessSession getStatelessSession(Connection connection) {
		return sessionFactory.openStatelessSession(connection);
	}

	/**
	 * To get Stateless Session.
	 * @return StatelessSession
	 */
	public StatelessSession getStatelessSession() {
		return sessionFactory.openStatelessSession();
	}

	private Properties createHibernateProperties(String userName, String password, String driverName, String jdbcUrl,
			String dialectName) {
		Properties properties = new Properties();
		properties.put("hibernate.connection.driver_class", driverName);
		properties.put("hibernate.connection.url", jdbcUrl);
		properties.put("hibernate.connection.username", userName);
		properties.put("hibernate.connection.password", password);
		if (dialectName != null) {
			properties.put("hibernate.dialect", dialectName);
		}
		return properties;
	}

	/**
	 * To create queries.
	 * 
	 * @param queryString String
	 * @param aliasTypes List<AliasType>
	 * @param params Object
	 * @return SQLQuery
	 */
	public SQLQuery createQuery(String queryString, List<AliasType> aliasTypes, Object... params) {
		SQLQuery sqlQuery = createQuery(queryString, params);

		if (aliasTypes != null) {
			for (AliasType aliasType : aliasTypes) {
				sqlQuery.addScalar(aliasType.alias, aliasType.type);
			}
		}
		return sqlQuery;
	}

	/**
	 * To create queries.
	 * 
	 * @param queryString String
	 * @param params Object
	 * @return SQLQuery
	 */
	public SQLQuery createQuery(String queryString, Object... params) {
		SQLQuery sqlQuery = sessionFactory.openStatelessSession().createSQLQuery(queryString);

		if (params != null) {
			int pos = 1;
			for (Object p : params) {
				sqlQuery.setParameter(pos++, p);
			}
		}
		return sqlQuery;
	}

	/**
	 * To create, update or insert query.
	 * 
	 * @param session StatelessSession
	 * @param queryString String
	 * @param params Object
	 * @return SQLQuery
	 */
	public SQLQuery createUpdateOrInsertQuery(StatelessSession session, String queryString, Object... params) {
		SQLQuery sqlQuery = session.createSQLQuery(queryString);

		if (params != null) {
			int pos = 1;
			for (Object p : params) {
				sqlQuery.setParameter(pos++, p);
			}
		}
		return sqlQuery;
	}

	/**
	 * To get all table names.
	 * 
	 * @return Map<String, List<String>>
	 * @throws SQLException in case of error
	 */
	public Map<String, List<String>> getAllTableNames() throws SQLException {
		Map<String, List<String>> tableMap = new HashMap<String, List<String>>();

		List<String> tableNames = new LinkedList<String>();
		List<String> viewNames = new LinkedList<String>();
		if (sessionFactory instanceof SessionFactoryImplementor) {
			try {
				if (connection != null) {
					connection.close();
				}
				if (connectionProvider != null) {
					connectionProvider.close();
				}
			} catch (Exception e) {
				LOG.error("Could not close connections ", e);
			}
			connectionProvider = ((SessionFactoryImplementor) sessionFactory).getConnectionProvider();
			connection = connectionProvider.getConnection();
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			ResultSet rSet = null;
			try {
				rSet = databaseMetaData.getTables(null, null, null, new String[] {TABLE, VIEW});
				while (rSet.next()) {
					if (rSet.getString("TABLE_TYPE").equals(TABLE)) {
						tableNames.add(rSet.getString("TABLE_NAME"));
					} else if (rSet.getString("TABLE_TYPE").equals(VIEW)) {
						viewNames.add(rSet.getString("TABLE_NAME"));
					}
				}
				tableMap.put(TABLE, tableNames);
				tableMap.put(VIEW, viewNames);

				if (rSet != null) {
					rSet.close();
				}
			} catch (Exception e) {
				LOG.error("Could not close result set ", e);
			} finally {
				if (rSet != null) {
					rSet.close();
				}
			}
		}
		return tableMap;
	}

	/**
	 * To get all columns for table.
	 * 
	 * @param table String
	 * @return List<ColumnDefinition>
	 * @throws SQLException in case of error
	 */
	public List<ColumnDefinition> getAllColumnsForTable(String table) throws SQLException {
		List<ColumnDefinition> columnDefinitions = new LinkedList<ColumnDefinition>();

		if (sessionFactory instanceof SessionFactoryImplementor) {
			try {
				if (connection != null) {
					connection.close();
				}
				if (connectionProvider != null) {
					connectionProvider.close();
				}
			} catch (Exception e) {
				LOG.error("Could not close connections ", e);
			}
			connectionProvider = ((SessionFactoryImplementor) sessionFactory).getConnectionProvider();
			connection = connectionProvider.getConnection();
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			ResultSet rSet = null;
			try {
				rSet = databaseMetaData.getColumns(null, null, table, null);
				while (rSet.next()) {
					columnDefinitions.add(new ColumnDefinition(rSet.getString("COLUMN_NAME"), getColumnClassName(rSet
							.getInt("DATA_TYPE"))));
				}
			} finally {
				if (rSet != null) {
					rSet.close();
				}
			}
		}
		return columnDefinitions;
	}

	/**
	 * To get primary keys for table.
	 * 
	 * @param table String
	 * @param tableType String
	 * @return List<String>
	 * @throws SQLException in case of error
	 */
	public List<String> getPrimaryKeysForTable(String table, String tableType) throws SQLException {
		List<String> primaryKeys = new LinkedList<String>();
		ResultSet rSet = null;
		if (sessionFactory instanceof SessionFactoryImplementor) {
			try {
				if (connection != null) {
					connection.close();
				}
				if (connectionProvider != null) {
					connectionProvider.close();
				}
			} catch (Exception e) {
				LOG.error("Could not close connections ", e);
			}
			connectionProvider = ((SessionFactoryImplementor) sessionFactory).getConnectionProvider();
			connection = connectionProvider.getConnection();
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			try {
				if (tableType.equals(TABLE)) {
					rSet = databaseMetaData.getPrimaryKeys(null, null, table);
				} else if (tableType.equals(VIEW)) {
					rSet = databaseMetaData.getBestRowIdentifier(null, null, table, 1, false);
				}
				while (rSet.next()) {
					final String primaryKey = rSet.getString("COLUMN_NAME");
					LOG.info("Found primary key: " + primaryKey);
					primaryKeys.add(primaryKey);
				}

			} catch (Exception e) {
				LOG.error("Error processing the result set");
			} finally {
				if (rSet != null) {
					rSet.close();
				}
			}
		}
		return primaryKeys;
	}

	private Class<?> getColumnClassName(int sqlType) throws SQLException {
		Class<?> clazz = String.class;

		switch (sqlType) {

			case Types.NUMERIC:
			case Types.DECIMAL:
				clazz = java.math.BigDecimal.ZERO.getClass();
				break;

			case Types.BIT:
				clazz = Boolean.FALSE.getClass();
				break;

			case Types.TINYINT:
				clazz = Byte.valueOf("0").getClass();
				break;

			case Types.SMALLINT:
				clazz = Short.valueOf("0").getClass();
				break;

			case Types.INTEGER:
				clazz = Integer.valueOf(0).getClass();
				break;

			case Types.BIGINT:
				clazz = Long.valueOf(0L).getClass();
				break;

			case Types.REAL:
				clazz = Float.valueOf(0f).getClass();
				break;

			case Types.FLOAT:
			case Types.DOUBLE:
				clazz = Double.valueOf(0d).getClass();

				break;

			case Types.BINARY:
			case Types.VARBINARY:
			case Types.LONGVARBINARY:
				byte[] binaryVar = {};
				clazz = binaryVar.getClass();

				break;

			case Types.DATE:
				clazz = java.sql.Date.class;
				break;

			case Types.TIME:
				clazz = java.sql.Time.class;
				break;

			case Types.TIMESTAMP:
				clazz = java.sql.Timestamp.class;
				break;

			case Types.BLOB:
				byte[] blob = {};
				clazz = blob.getClass();
				break;

			case Types.CLOB:
				char[] character = {};
				clazz = character.getClass();
				break;
			default:
		}
		return clazz;
	}

	/**
	 * Class for alias type.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 */
	public static class AliasType {

		/**
		 * alias String.
		 */
		private final String alias;
		
		/**
		 * type NullableType.
		 */
		private final NullableType type;

		/**
		 * Constructor.
		 *  
		 * @param alias String
		 * @param type NullableType
		 */
		public AliasType(String alias, NullableType type) {
			this.alias = alias;
			this.type = type;
		}
	}

	/**
	 * Class for Column Definition.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 */
	public static class ColumnDefinition {

		/**
		 * columnName String.
		 */
		private final String columnName;
		
		/**
		 * type Class<?>.
		 */
		private final Class<?> type;

		/**
		 * Constructor.
		 * 
		 * @param columnName String
		 * @param type Class<?>
		 */
		public ColumnDefinition(String columnName, Class<?> type) {
			this.columnName = columnName;
			this.type = type;
		}

		/**
		 * To get column name.
		 * 
		 * @return String
		 */
		public String getColumnName() {
			return columnName;
		}

		/**
		 * To get type.
		 * 
		 * @return Class<?>
		 */
		public Class<?> getType() {
			return type;
		}
	}

	/**
	 * To close the session.
	 */
	public void closeSession() {
		try {
			if (connection != null) {
				connection.close();
			}
			if (connectionProvider != null) {
				connectionProvider.close();
			}
			if (sessionFactory != null) {
				sessionFactory.close();
			}
		} catch (Exception e) {
			LOG.error("Could not close open connections", e);
		}

	}
}
