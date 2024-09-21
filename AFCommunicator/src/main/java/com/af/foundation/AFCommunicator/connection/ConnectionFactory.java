/* Decompiler 28ms, total 451ms, lines 59 */
package com.af.foundation.AFCommunicator.connection;

import com.af.foundation.AFCommon.configuration.AFConfiguration;
import com.af.foundation.AFCommon.logging.AFLogger;
import com.af.foundation.AFCommunicator.exceptions.AFCommunicatorExceptions;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	private static AFLogger logger = AFLogger.getLogger("database");
	static Connection connection = null;

	public static Connection getConnection() throws AFCommunicatorExceptions {
		logger.info("Trying to get connection");
		Connection connection = null;
		String DBType = AFConfiguration.DB_TYPE.toUpperCase();
		switch (DBType) {
		case "MSSQL":
			try {
				logger.debug("DB URL: " + AFConfiguration.DB_URL);
				logger.debug("DB User: " + AFConfiguration.DB_USER);
				logger.debug("DB timeout: " + AFConfiguration.DB_TIMEOUT);
				DriverManager.setLoginTimeout(AFConfiguration.DB_TIMEOUT);
				connection = DriverManager.getConnection(AFConfiguration.DB_URL, AFConfiguration.DB_USER,
						AFConfiguration.DB_PASS);
				if (connection != null) {
					DatabaseMetaData dm = connection.getMetaData();
					System.out.println(dm.getDriverVersion());
					logger.info("Driver name: " + dm.getDriverName());
					logger.info("Driver version: " + dm.getDriverVersion());
					logger.info("Product name: " + dm.getDatabaseProductName());
					logger.info("Product version: " + dm.getDatabaseProductVersion());
				}
			} catch (SQLException connectionIssue) {
				System.err.println(connectionIssue.getMessage());
				logger.error(connectionIssue.getLocalizedMessage());
				throw new AFCommunicatorExceptions("Unable to get the managed connection", new SQLException());
			}
		default:
			return connection;
		}

	}

	public static void main(String[] args) {
		Connection connnection = getConnection();
		if (connnection == null) {
			System.out.println("faild");
		}

	}
}