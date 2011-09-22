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

package com.ephesoft.dcma.core.hibernate;

import java.io.File;
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

import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.encryption.core.PasswordDecryptor;
import com.ephesoft.dcma.encryption.exception.CryptographyException;

public class DynamicHibernateDao {

	private static final String META_INF = "META-INF";
	private static final String FOLDER_NAME = "dcma-performance-reporting";
	private static final String FILE_NAME = "dcma-report-db";
	private SessionFactory sessionFactory = null;
	private ConnectionProvider connectionProvider = null;
	private Connection connection = null;

	private static final Logger logger = LoggerFactory.getLogger(DynamicHibernateDao.class);
	private static final String PASSWORD = "hibernate.connection.password";

	public DynamicHibernateDao(String userName, String password, String driverName, String jdbcUrl) {
		Properties properties = createHibernateProperties(userName, password, driverName, jdbcUrl);

		Configuration configuration = new Configuration().addProperties(properties);
		try {
			if (sessionFactory != null) {
				sessionFactory.close();
			}
		} catch (Exception e) {
			logger.error("Could not close session factory", e);
		}
		sessionFactory = configuration.buildSessionFactory();
	}

	/* Changes for reporting module Start */

	public DynamicHibernateDao(String cfgLocation) {
		Configuration configuration = new Configuration().configure(cfgLocation);
		String filePath = META_INF + File.separator + FOLDER_NAME + File.separator + FILE_NAME + ".properties";
		Properties properties;
		try {
			InputStream propertyInStream;
			propertyInStream = new ClassPathResource(filePath).getInputStream();
			properties = new Properties();
			properties.load(propertyInStream);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new HibernateException(e.getMessage(), e);
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
					logger.error(e.getMessage(), e);
					throw new HibernateException(e.getMessage(), e);
				}
			}
		}
		try {
			if (sessionFactory != null) {
				sessionFactory.close();
			}
		} catch (Exception e) {
			logger.error("Could not close session factory", e);
		}
		sessionFactory = configuration.buildSessionFactory();
	}

	public StatelessSession getStatelessSession(DynamicHibernateDao dynamicHibernateDao) throws DCMAException {
		try {
			if (dynamicHibernateDao.sessionFactory instanceof SessionFactoryImplementor) {
				try {
					if (connection != null) {
						connection.close();
					}
					if (connectionProvider != null) {
						connectionProvider.close();
					}
				} catch (Exception e) {
					logger.error("Unable to close open connections", e);
				}
				connectionProvider = ((SessionFactoryImplementor) dynamicHibernateDao.sessionFactory).getConnectionProvider();
				connection = connectionProvider.getConnection();
				return sessionFactory.openStatelessSession(connection);
			} else {
				return sessionFactory.openStatelessSession();
			}
		} catch (SQLException sqle) {
			throw new DCMAException("Exception occurred while getting report connection.", sqle);
		}
	}

	/* Changes for reporting module End */

	private Properties createHibernateProperties(String userName, String password, String driverName, String jdbcUrl) {
		Properties properties = new Properties();
		properties.put("hibernate.connection.driver_class", driverName);
		properties.put("hibernate.connection.url", jdbcUrl);
		properties.put("hibernate.connection.username", userName);
		properties.put("hibernate.connection.password", password);

		return properties;
	}

	public SQLQuery createQuery(String queryString, List<AliasType> aliasTypes, Object... params) {
		SQLQuery sqlQuery = createQuery(queryString, params);

		if (aliasTypes != null) {
			for (AliasType aliasType : aliasTypes) {
				sqlQuery.addScalar(aliasType.alias, aliasType.type);
			}
		}
		return sqlQuery;
	}

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
				logger.error("Could not close connections ", e);
			}
			connectionProvider = ((SessionFactoryImplementor) sessionFactory).getConnectionProvider();
			connection = connectionProvider.getConnection();
			DatabaseMetaData databaseMetaData = connection.getMetaData();

			ResultSet rSet = databaseMetaData.getTables("", "", "", new String[] {"TABLE", "VIEW"});
			while (rSet.next()) {
				if (rSet.getString("TABLE_TYPE").equals("TABLE")) {
					tableNames.add(rSet.getString("TABLE_NAME"));
				} else if (rSet.getString("TABLE_TYPE").equals("VIEW")) {
					viewNames.add(rSet.getString("TABLE_NAME"));
				}
			}
			tableMap.put("TABLE", tableNames);
			tableMap.put("VIEW", viewNames);
			try {
				if (rSet != null) {
					rSet.close();
				}
			} catch (Exception e) {
				logger.error("Could not close result set ", e);
			}
		}
		return tableMap;
	}

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
				logger.error("Could not close connections ", e);
			}
			connectionProvider = ((SessionFactoryImplementor) sessionFactory).getConnectionProvider();
			connection = connectionProvider.getConnection();
			DatabaseMetaData databaseMetaData = connection.getMetaData();

			ResultSet rSet = databaseMetaData.getColumns("", "", table, "");
			while (rSet.next()) {
				columnDefinitions
						.add(new ColumnDefinition(rSet.getString("COLUMN_NAME"), getColumnClassName(rSet.getInt("DATA_TYPE"))));
			}
			try {
				if (rSet != null) {
					rSet.close();
				}
			} catch (Exception e) {
				logger.error("Could not close result set ", e);
			}
		}
		return columnDefinitions;
	}

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
				logger.error("Could not close connections ", e);
			}
			connectionProvider = ((SessionFactoryImplementor) sessionFactory).getConnectionProvider();
			connection = connectionProvider.getConnection();
			DatabaseMetaData databaseMetaData = connection.getMetaData();

			if (tableType.equals("TABLE")) {
				rSet = databaseMetaData.getPrimaryKeys("", "", table);
			} else if (tableType.equals("VIEW")) {
				rSet = databaseMetaData.getBestRowIdentifier("", "", table, 1, false);
			}
			while (rSet.next()) {
				primaryKeys.add(rSet.getString("COLUMN_NAME"));
			}
			try {
				if (rSet != null) {
					rSet.close();
				}
			} catch (Exception e) {
				logger.error("Could not close result set ", e);
			}
		}
		return primaryKeys;
	}

	private Class<?> getColumnClassName(int sqlType) throws SQLException {
		Class<?> clazz = (new String()).getClass();

		switch (sqlType) {

			case Types.NUMERIC:
			case Types.DECIMAL:
				clazz = (new java.math.BigDecimal(0)).getClass();
				break;

			case Types.BIT:
				clazz = (new Boolean(false)).getClass();
				break;

			case Types.TINYINT:
				clazz = (new Byte("0")).getClass();
				break;

			case Types.SMALLINT:
				clazz = (new Short("0")).getClass();
				break;

			case Types.INTEGER:
				clazz = (new Integer(0)).getClass();
				break;

			case Types.BIGINT:
				clazz = (new Long(0)).getClass();
				break;

			case Types.REAL:
				clazz = (new Float(0)).getClass();
				break;

			case Types.FLOAT:
			case Types.DOUBLE:
				clazz = (new Double(0)).getClass();
				;
				break;

			case Types.BINARY:
			case Types.VARBINARY:
			case Types.LONGVARBINARY:
				byte[] b = {};
				clazz = (b.getClass());
				;
				break;

			case Types.DATE:
				clazz = (new java.sql.Date(123456)).getClass();
				break;

			case Types.TIME:
				clazz = (new java.sql.Time(123456)).getClass();
				break;

			case Types.TIMESTAMP:
				clazz = (new java.sql.Timestamp(123456)).getClass();
				break;

			case Types.BLOB:
				byte[] blob = {};
				clazz = (blob.getClass());
				break;

			case Types.CLOB:
				char[] c = {};
				clazz = (c.getClass());
				break;
		}
		return clazz;
	}

	public static class AliasType {

		private String alias;
		private NullableType type;

		public AliasType(String alias, NullableType type) {
			this.alias = alias;
			this.type = type;
		}
	}

	public static class ColumnDefinition {

		private String columnName;
		private Class<?> type;

		public ColumnDefinition(String columnName, Class<?> type) {
			this.columnName = columnName;
			this.type = type;
		}

		public String getColumnName() {
			return columnName;
		}

		public Class<?> getType() {
			return type;
		}
	}

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
			logger.error("Could not close open connections", e);
		}

	}
}
