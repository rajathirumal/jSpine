/* Decompiler 28ms, total 451ms, lines 59 */
package com.spine.communicator.connection;

import com.spine.common.configuration.SpineConfiguration;
import com.spine.common.logging.SpineLogger;
import com.spine.communicator.exceptions.SpineCommunicatorExceptions;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	private static SpineLogger logger = SpineLogger.getLogger("database");
	static Connection connection = null;

	public static Connection getConnection() throws SpineCommunicatorExceptions {
		logger.info("Trying to get connection");
		Connection connection = null;
		String DBType = SpineConfiguration.DB_TYPE.toUpperCase();
		switch (DBType) {
		case "MSSQL":
			try {
				logger.debug("DB URL: " + SpineConfiguration.DB_URL);
				logger.debug("DB User: " + SpineConfiguration.DB_USER);
				logger.debug("DB timeout: " + SpineConfiguration.DB_TIMEOUT);
				DriverManager.setLoginTimeout(SpineConfiguration.DB_TIMEOUT);
				connection = DriverManager.getConnection(SpineConfiguration.DB_URL, SpineConfiguration.DB_USER,
						SpineConfiguration.DB_PASS);
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
				throw new SpineCommunicatorExceptions("Unable to get the managed connection", new SQLException());
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